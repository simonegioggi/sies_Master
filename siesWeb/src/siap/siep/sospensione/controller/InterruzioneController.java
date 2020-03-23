package siap.siep.sospensione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.CalendarUtil;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.dao.SospensioneDAO;
import siap.siep.sospensione.model.PeriodoInterruzioneModel;
import siap.siep.sospensione.model.SospensioneModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class InterruzioneController extends SiapController implements IInterruzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public List ExRicercaPeriodiInterruzione(BigDecimal aKeyFascicolo) throws F3BException {

		List lPeriodiInterruzione = new ArrayList();

		EventoDAO lEveDao = null;
		PenaResiduaDAO lPenResDao = null;
		SospensioneDAO lSospDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;

		Connection lConn = null;

		try {
			lConn = getDBConnection();

			lEveDao = new EventoDAO(lConn);

			lEveDao.selCondizioneRicerca(aKeyFascicolo, "01", (new String[] { "04", "12", "04", "25" }),
					(new String[] { "0267", "0267", "0270", "0270" }), null);

			List lListEventiDal = new ArrayList(lEveDao.getModels());
			lEveDao.stop();

			// AMBROS 05/2013 Aggiunto "09" e "0272" (per tripletta 01 09 0272) - per template SIEP_RIPRI_ESP
			// (comunicazione)
			lEveDao.selCondizioneRicerca(aKeyFascicolo, "01", (new String[] { "04", "09" }),
					(new String[] { "0272", "0272" }), null);

			List lListEventiAl = new ArrayList(lEveDao.getModels());
			lEveDao.stop();

			if (!lListEventiDal.isEmpty()) {
				lPenResDao = new PenaResiduaDAO(lConn);
				lSospDao = new SospensioneDAO(lConn);
				lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);

				EventoModel lEveMod = null;
				PenaResiduaModel lPenMod = null;
				SospensioneModel lSospModDal, lSospModAl = null;

				Iterator iterDal = lListEventiDal.iterator();
				Iterator iterAl = lListEventiAl.iterator();

				while (iterDal.hasNext()) {
					lEveMod = (EventoModel) iterDal.next();

					PeriodoInterruzioneModel lPerInt = new PeriodoInterruzioneModel();
					CalendarModel lQuantumDate = new CalendarModel();

					if (lEveMod != null) {
						lPenResDao.setCondizioneByIdEvento(lEveMod.getIdEvento());
						lPenMod = (PenaResiduaModel) lPenResDao.getModelByKey();
						lPenResDao.stop();

						if (lPenMod != null) {
							lSospDao.setCondizioneIdPenaResidua(lPenMod.getIdPenaResidua());
							lSospModDal = (SospensioneModel) lSospDao.getModelByKey();
							lSospDao.stop();

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Sospensione DAL = " + lSospModDal);

							if (lSospModDal != null)
								lQuantumDate.setDataInizio(lSospModDal.getDataInizio());
						}

						if (iterAl.hasNext()) {
							lEveMod = (EventoModel) iterAl.next();

							if (lEveMod != null) {
								lPenResDao.setCondizioneByIdEvento(lEveMod.getIdEvento());
								lPenMod = (PenaResiduaModel) lPenResDao.getModelByKey();
								lPenResDao.stop();

								if (lPenMod != null) {
									lSospDao.setCondizioneIdPenaResidua(lPenMod.getIdPenaResidua());
									lSospModAl = (SospensioneModel) lSospDao.getModelByKey();
									lSospDao.stop();

									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di LogF3B.getLogger()
									siesLogger.debug("Sospensione AL = " + lSospModAl);

									if (lSospModAl != null)
										lQuantumDate.setDataFine(lSospModAl.getDataFine());
								}
							}
						}
					}

					CalendarUtil lCalUtil = new CalendarUtil();
					CalendarModel lQuantum = lCalUtil.CalcolaNumGiorniMesiAnni(lQuantumDate, false);
					// lQuantum = lCalUtil.ricalcolaGAM(lQuantum);

					lQuantum.setDataInizio(lQuantumDate.getDataInizio());
					lQuantum.setDataFine(lQuantumDate.getDataFine());

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Quantum = " + lQuantum);

					lPerInt.setQuantum(lQuantum);

					PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
					lPosMod.setFasSieIdFascicoloSiep(aKeyFascicolo);
					lPosMod.setDataInizio(lQuantum.getDataInizio());
					lPosSqlDao.ricercaPosizioneGiuridica(lPosMod);
					lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
					lPosSqlDao.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Posizione = " + lPosMod);

					if (lPosMod != null && lPosMod.getDescrPosizioneGiuridica() != null)
						lPerInt.setPosizioneGiuridica(lPosMod.getDescrPosizioneGiuridica());

					// Aggiunge alla lista
					lPeriodiInterruzione.add(lPerInt);
				}
			}
		} catch (DAOException ex) {
			throw new F3BException("InterruzioneController.ExRicercaPeriodiInterruzione: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lPenResDao);
			cleanup(lSospDao);
			cleanup(lPosSqlDao);

			cleanup(lConn);
		}

		return lPeriodiInterruzione;
	}

}