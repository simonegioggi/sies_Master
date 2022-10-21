package siap.jms.controller;

import java.io.ByteArrayInputStream;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.controller.SiapController;
import siap.jms.ICostantiJMS;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.dao.MagistratoDAO;
import siap.sico.note.dao.NoteDAO;
import siap.sico.note.model.NoteModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiusDAO;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penapecuniaria.dao.RichiestaConversioneDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.scambiosanzione.dao.ScambioSanzioneDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.sius.avvocato.dao.AvvocatoDAO;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusDAO;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.magistratorelatore.dao.MagistratoRelatoreDAO;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneDAO;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.tenore.dao.TenoreDAO;

/**
 * <p>
 * Title: SiapPresaInCaricoJMSController
 * </p>
 * <p>
 * Description: Classe che centralizza le prese incarico comuni a SIEP e a SIUS
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
public class SiapPresaInCaricoJMSController extends SiapController implements ICostantiJMS {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	/**
	 * Inserisce l'Evento le Notifiche associate, gli avvocati e i campi Nota eventuali
	 *
	 * @param lPars
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	protected String inserisciEventoNotificheAvvocati(EventoNotificaModel lEve, Connection lConn)
			throws F3BException {

		EventoDAO lEveDao = null; // Dopo FascicoloSius
		NotificaDAO lNotDao = null; // Dopo Evento/Autorita Esterna
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null; // Dopo Evento
		AvvocatoDAO lAvvDao = null;
		AvvocatoFascicoloSiusDAO lAvvFasSiuDao = null;

		String lRapporto = new String("");

		try {
			// int length = 0;
			// EVENTO
			try {
				lEveDao = new EventoDAO(lConn);
				lEveDao.setDAOFromModel(lEve.getEvento());
				lEveDao.setWithoutSequence(true);
				lEveDao.insert();
				lEveDao.stop();
			} catch (DAOException ex) {
				if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
					lRapporto += buildRapporto("Inserimento ", "Evento " + lEve.getEvento().getIdEvento(),
							CHIAVE_DUPLICATA);
				} else
					throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l'Evento! ");
			}

			// NOTIFICHE - AUTORITA ESTERNE
			NotificaModel[] lNotifiche = lEve.getNotifiche();

			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			int count = 0;

			while (count < lNotifiche.length) {
				if (lNotifiche[count] != null) {
					try {
						// AUTORITA ESTERNA
						if (lNotifiche[count].getAutoritaEsterna() != null) {
							lAutDao.setDAOFromModel(lNotifiche[count].getAutoritaEsterna());
							lAutDao.setWithoutSequence(true);
							lAutDao.insert();
							lAutDao.stop();
						}
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							lRapporto += buildRapporto("Inserimento ",
									"Autorità Esterna "
											+ lNotifiche[count].getAutoritaEsterna().getIdAutoritaEsterna(),
									CHIAVE_DUPLICATA);
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire l' Autorità Esterna! ");
					}

					// AVVOCATO SIUS
					if (lNotifiche[count].getAvvIdAvvocatoFascicoloSius() != null) {
						try {
							lAvvDao = new AvvocatoDAO(lConn);

							lAvvDao.setDAOFromModel(lNotifiche[count].getAvvSius().getAvvocato());
							lAvvDao.setWithoutSequence(true);
							lAvvDao.insert();
							lAvvDao.stop();
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								lRapporto += buildRapporto("Inserimento ", "Avvocato ", CHIAVE_DUPLICATA);
							} else
								throw new F3BException(F3BException.USER_MESSAGE,
										"Impossibile inserire l'Avvocato! ");
						}
					}

					// AVVOCATO FASCICOLO SIUS
					if (lNotifiche[count].getAvvIdAvvocatoFascicoloSius() != null) {
						try {
							lAvvFasSiuDao = new AvvocatoFascicoloSiusDAO(lConn);
							lAvvFasSiuDao.setDAOFromModel(
									lNotifiche[count].getAvvSius().getAvvocatoFascicoloSiusModel());
							lAvvFasSiuDao.setWithoutSequence(true);
							lAvvFasSiuDao.insert();
							lAvvFasSiuDao.stop();
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								lRapporto += buildRapporto("Inserimento ", "Avvocato Fascicolo ",
										CHIAVE_DUPLICATA);
							} else
								throw new F3BException(F3BException.USER_MESSAGE,
										"Impossibile inserire l'Avvocato Fascicolo Sius! ");
						}
					}

					// NOTIFICA
					try {
						lNotDao.setDAOFromModel(lNotifiche[count]);
						lNotDao.setWithoutSequence(true);
						lNotDao.insert();
						lNotDao.stop();
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							lRapporto += buildRapporto("Inserimento ", "Notifica ", CHIAVE_DUPLICATA);
						}
						// STUB 20/04/2005
						else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
							lRapporto += buildRapporto("Inserimento ", "Notifica ", CONSTRAINT_VIOLATA);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.warn("Integrità referenziale violata : " + ex.getMessage());
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire la Notifica! ");
					}
				}

				count++;
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
			// posto di LogF3B.getLogger()
			siesLogger.error("inserisciEventoNotificheAvvocati: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lCampoNotaDao);
			cleanup(lAvvDao);
			cleanup(lAvvFasSiuDao);
		}

		return lRapporto;
	}

	protected String[] inserisciEventoNotificaAvvocatoSiep(EventoNotificaModel lEve, Connection lConn)
			throws F3BException {

		String[] lEsito = new String[2]; // 21/01/2008
		String lEsitoEvento = "00000"; // 21/01/2008
		String lCodEsito = "00000";

		EventoDAO lEveDao = null; // Dopo FascicoloSiep
		NotificaDAO lNotDao = null; // Dopo Evento/Autorita Esterna
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null; // Dopo Evento
		siap.siep.avvocato.dao.AvvocatoDAO lAvvDao = null;
		FascicoloSiusDAO lFasSiusDao = null;
		GeneraleProcedimentoDAO lGenProDao = null; // STUB 19/04/2005
		TenoreDAO lTenDao = null; // STUB 19/04/2005
		DepositoDecretoDAO lDepDecDao = null; // STUB 26/04/2005
		DepositoOrdinanzaPcDAO lDepOrdDao = null; // STUB 26/04/2005
		AvvocatoFascicoloSiepDAO lAvvFasSieDao = null;
		siap.sius.avvocato.dao.AvvocatoDAO lAvvSiusDao = null;
		AvvocatoFascicoloSiusDAO lAvvFasSiusDao = null;

		String lRapporto = new String("");

		// STUB 26/04/2005 - AGGREGATO FASCICOLO SIUS PRIMA DELL'EVENTO.
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.warn("AGGREGATO FASCICOLO SIUS : "+lEve.getFascicoloGPTP() );
		if (lEve.getFascicoloGPTP() != null) {
			// 22/06/2010 Inserimento soggetto SIUS.
			if (lEve.getFascicoloGPTP().getFascicoloSiusModel() != null
					&& lEve.getFascicoloGPTP().getFascicoloSiusModel().getSoggetto() != null) {
				try {
					SoggettoDAO lSogDao = new SoggettoDAO(lConn);
					lSogDao.setDAOFromModel(lEve.getFascicoloGPTP().getFascicoloSiusModel().getSoggetto());
					lSogDao.setWithoutSequence(true);
					lSogDao.insert();
					lCodEsito = "00000";
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						lCodEsito = "00001";
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED)
						lCodEsito = "00002";
					else {
						lCodEsito = "33333";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
								+ ex.getMessage());
					}
				}
				lRapporto += buildRapporto("Inserimento ",
						"Soggetto Sius " + lEve.getFascicoloGPTP().getFascicoloSiusModel().getSogIdSoggetto()
								+ " x l'Evento " + lEve.getEvento().getIdEvento(),
						lCodEsito);
			}

			if (lEve.getFascicoloGPTP().getFascicoloSiusModel() != null
					&& lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius() != null) {
				// STUB 26/04/2005 Cancellazione prima dell'inserimento.
				// if (! this.stressoDistretto( this.COD_UFFICIO_DESTINATARIO,
				// lEve.getFascicoloGPTP().getFascicoloSiusModel().getChiaveUfficio(), lConn) )
				// this.delFascicoloSius(lEve, lConn);
				try {
					// Si inserisce il fascicolo SIUS
					lFasSiusDao = new FascicoloSiusDAO(lConn);
					lFasSiusDao.setDAOFromModel(lEve.getFascicoloGPTP().getFascicoloSiusModel());
					lFasSiusDao.setWithoutSequence(true);
					lFasSiusDao.insert();
					lCodEsito = "00000";
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						lCodEsito = "00001";
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED)
						lCodEsito = "00002";
					else {
						lCodEsito = "33333";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
								+ ex.getMessage());
					}
				}
				lRapporto += buildRapporto("Inserimento ",
						"Fascicolo Sius "
								+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius()
								+ " x l'Evento " + lEve.getEvento().getIdEvento(),
						lCodEsito);
			}
			// Fase di inserimento per il Generale Procedimento.
			if (lEve.getFascicoloGPTP().getGeneraleProcedimentoModel() != null && lEve.getFascicoloGPTP()
					.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() != null) {
				try {
					lGenProDao = new GeneraleProcedimentoDAO(lConn);
					lGenProDao.setDAOFromModel(lEve.getFascicoloGPTP().getGeneraleProcedimentoModel());
					lGenProDao.setWithoutSequence(true);
					lGenProDao.insert();
					lCodEsito = "00000";
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						lCodEsito = "00001";
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED)
						lCodEsito = "00002";
					else {
						lCodEsito = "33333";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
								+ ex.getMessage());
					}
				}
				lRapporto += buildRapporto("Inserimento ",
						"Gen. Procedimento "
								+ lEve.getFascicoloGPTP().getGeneraleProcedimentoModel()
										.getIdGeneraleProcedimento()
								+ " x il Fascicolo "
								+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
						lCodEsito);
			}
		}

		// EVENTO
		try {
			// STUB 19/04/2005 Sostituito il FascicoloSiusModel con l'aggregato FascicoloGPModel, inserito in
			// anticipo.
			lEveDao = new EventoDAO(lConn);

			// 22/01/2007 Inserimento di EVENTO con annullamento delle Integrità referenziali propedeutiche.
			// Sarà operatoil successivo aggiornamento.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.info("=========> evento sto x scrivere") ;
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.info("Evento.getIdPenaResidua = "+lEve.getEvento().getPenIdPenaResidua());
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.info("evento.getIdEvento = "+lEve.getEvento().getIdEvento());
			lEve.getEvento().setPenIdPenaResidua(null);
			lEve.getEvento().setAnnIdAnnotazioneManuale(null);

			lEveDao.setDAOFromModel(lEve.getEvento());
			if (lEve.getEvento().getDocPerTrasferimento() != null
					&& lEve.getEvento().getDocPerTrasferimento().length > 1) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.info("Controllo BBBLOBBB Out" +
				// lEve.getEvento().getDocPerTrasferimento().length);
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.info("Settato BLOBBBB");
				lEveDao.setDocBlob(new ByteArrayInputStream(lEve.getEvento().getDocPerTrasferimento()));
			}

			lEveDao.setWithoutSequence(true);

			lEveDao.insert();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.info("=========> evento scritto----->") ;

			lCodEsito = "00000";
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
			}
			// STUB 20/04/2005
			else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
				lCodEsito = "00002";
			} else {
				lCodEsito = "33333";
				lRapporto += buildRapporto("Inserimento ", "Evento " + lEve.getEvento().getIdEvento(),
						ERRORE_GENERICO);
				lEsito[0] = lCodEsito;
				lEsito[1] = lRapporto;
				return lEsito;
			}
		}
		lRapporto += buildRapporto("Inserimento ", "Evento " + lEve.getEvento().getIdEvento(), lCodEsito);
		lEsitoEvento = lCodEsito;

		// STUB 02/05/2005 - DOPO L'EVENTO, DALL'AGGREGATO FASCICOLO SIUS SI RECUPERANO I TENORI E I
		// PROVVEDIMENTI.
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.warn("AGGREGATO FASCICOLO SIUS : "+lEve.getFascicoloGPTP() );
		lRapporto = inserisciTenoriEProvvedimenti(lRapporto, lEve, lConn);

		// STUB 16/01/2008 - DOPO I TENORI e i PROVVEDIMENTI del Fascicolo SIUS SI RECUPERANO gli ALLEGATI.
		if (lEve.getFascicoloGPTP() != null)
			if (lEve.getFascicoloGPTP().getAllegati() != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.warn("ALLEGATI FASCICOLO SIUS : "+lEve.getFascicoloGPTP().getAllegati() );
				lRapporto = inserisciAllegati(lRapporto, lEve, lConn);
			}

		// STUB 16/01/2008 - Inserimento di ALTRI DATI SIUS.
		if (lEve.getFascicoloGPTP() != null)
			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.warn("ALTRI DATI FASCICOLO SIUS :
				// "+lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() );
				lRapporto = inserisciAltriDatiSius(lRapporto, lEve, lConn);
			}

		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.info("NOTIFICHE trattate : "+lEve.getNotifiche() );
		// NOTIFICHE - AUTORITA ESTERNE
		NotificaModel[] lNotifiche = lEve.getNotifiche();

		lAutDao = new AutoritaEsternaDAO(lConn);
		lNotDao = new NotificaDAO(lConn);
		lCampoNotaDao = new CampoNotaDAO(lConn);

		int count = 0;

		while (count < lNotifiche.length) {

			if (lNotifiche[count] != null) {
				try {
					// AUTORITA ESTERNA
					if (lNotifiche[count].getAutoritaEsterna() != null) {
						lAutDao.setDAOFromModel(lNotifiche[count].getAutoritaEsterna());
						lAutDao.setWithoutSequence(true);
						lAutDao.insert();
						lAutDao.stop();
						lCodEsito = "00000";
						// lRapporto += buildRapporto("Inserimento ", "Autorità Esterna " +
						// lNotifiche[count].getAutoritaEsterna().getIdAutoritaEsterna(), lCodEsito);
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						lCodEsito = "00001";
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire l'Autorità esterna! ");
				}

				// AVVOCATO SIEP
				if (lNotifiche[count].getAvvIdAvvocatoFascicoloSiep() != null) {
					try {
						if (lNotifiche[count].getAvvSiep() != null) {
							lAvvDao = new siap.siep.avvocato.dao.AvvocatoDAO(lConn);

							lAvvDao.setDAOFromModel(lNotifiche[count].getAvvSiep().getAvvocato());
							lAvvDao.setWithoutSequence(true);
							lAvvDao.insert();
							lAvvDao.stop();
							lCodEsito = "00000";

							lAvvFasSieDao = new AvvocatoFascicoloSiepDAO(lConn);
							lAvvFasSieDao.setDAOFromModel(
									lNotifiche[count].getAvvSiep().getAvvocatoFascicoloSiepModel());
							lAvvFasSieDao.setAvvIdAvvocato(
									lNotifiche[count].getAvvSiep().getAvvocato().getIdAvvocato());
							lAvvFasSieDao.setWithoutSequence(true);
							lAvvFasSieDao.insert();
							lAvvFasSieDao.stop();
							lCodEsito = "00000";

						}
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							lCodEsito = "00001";
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire l'Avvocato! ");
					}
					// lRapporto += buildRapporto("Inserimento ", "Avvocato " , lCodEsito);
				}

				// AVVOCATO SIUS
				if (lNotifiche[count].getAvvIdAvvocatoFascicoloSius() != null) {
					try {
						if (lNotifiche[count].getAvvSius() != null) {

							lAvvSiusDao = new siap.sius.avvocato.dao.AvvocatoDAO(lConn);

							lAvvSiusDao.setDAOFromModel(lNotifiche[count].getAvvSius().getAvvocato());
							lAvvSiusDao.setWithoutSequence(true);
							lAvvSiusDao.insert();
							lAvvSiusDao.stop();
							lCodEsito = "00000";

							lAvvFasSiusDao = new AvvocatoFascicoloSiusDAO(lConn);
							lAvvFasSiusDao.setDAOFromModel(
									lNotifiche[count].getAvvSius().getAvvocatoFascicoloSiusModel());
							lAvvFasSiusDao.setAvvIdAvvocato(
									lNotifiche[count].getAvvSius().getAvvocato().getIdAvvocato());
							lAvvFasSiusDao.setWithoutSequence(true);
							lAvvFasSiusDao.insert();
							lAvvFasSiusDao.stop();
							lCodEsito = "00000";

						}
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							lCodEsito = "00001";
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire l'Avvocato! ");
					}
				}
				// NOTIFICA
				try {
					lNotDao.setDAOFromModel(lNotifiche[count]);
					lNotDao.setWithoutSequence(true);
					lNotDao.insert();
					lNotDao.stop();
					lCodEsito = "00000";
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Notifica gia' presente...");
						lCodEsito = "00001";

					}
					// STUB 20/04/2005
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("Integrità referenziale violata : " + ex.getMessage());
						lCodEsito = "00002";
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire la Notifica! ");
				}
			}
			count++;
		}

		// NOTE AGGIUNTIVE
		try {
			// Inserimento delle eventuali note aggiuntive.
			CampoNotaModel[] lNote = lEve.getCampoNote();
			if (lNote != null) {
				count = 0;

				while (count < lNote.length) {
					lCampoNotaDao.setDAOFromModel(lNote[count]);
					lCampoNotaDao.setWithoutSequence(true);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();
					lCodEsito = "00000";

					count++;
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00000";
				// Ticket#20220816012 - Se l'evento non è stato inserito sicuramente l'inserimento
				// delle note collegate violano l'integrità referenziale. L'errore non è bloccante
				// come per il caso dell'injsert delle NOTIFICHE
			} else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
				siesLogger.warn("Integrità referenziale violata : " + ex.getMessage());
				lCodEsito = "00002";
				// Ticket#20220816012 - FINE
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire le Note! ");

			lRapporto += buildRapporto("Inserimento ", "Note ", lCodEsito);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lCampoNotaDao);
			cleanup(lAvvDao);
			cleanup(lFasSiusDao);
			cleanup(lGenProDao); // STUB 19/04/2005
			cleanup(lTenDao); // STUB 19/04/2005
			cleanup(lDepDecDao); // STUB 26/04/2005
			cleanup(lDepOrdDao); // STUB 26/04/2005
			cleanup(lAvvFasSiusDao); // STUB 26/04/2005
			cleanup(lAvvSiusDao); // STUB 26/04/2005
			cleanup(lAvvFasSieDao);
		}
		lEsito[0] = lEsitoEvento;
		lEsito[1] = lRapporto;
		return lEsito;
	}

	/**
	 * Costruisce il Rapporto in HTMl per farlo vedere in output
	 *
	 * @param tipoOper
	 * @param arg
	 * @param esito
	 * @return
	 */
	protected String buildRapporto(String tipoOper, String arg, String esito) {

		String lString = "";

		lString = "<tr><td class=\"l\"> " + tipoOper + arg + " con esito </td>";

		if (esito.equals(POSITIVO))
			lString += " <td class=\"cVerde\"> POSITIVO </td> ";
		if (esito.equals(CHIAVE_DUPLICATA))
			lString += " <td class=\"cRosso\"> GIA' PRESENTE IN BANCA DATI </td>  ";
		if (esito.equals(NULL_NON_CONSENTITO))
			lString += " <td class=\"cRosso\"> INSERITO NULL IN UN CAMPO NON CONSENTITO </td>";
		if (esito.equals(ERRORE_GENERICO))
			lString += " <td class=\"cRosso\" bgcolor=\"#ffff00\"> ERRORE GENERICO  </td>";
		// STUB 20/04/2005
		if (esito.equals(CONSTRAINT_VIOLATA))
			lString += " <td class=\"cRosso\"> PRESENTE INTEGRITA' REFERENZIALE </td>  ";
		// inizio MEV_67
		if (esito.equals(ERRORE_CARICAMENTO)) {
			lString = "";
			lString = "<tr><td class=\"l\"> " + tipoOper + " </td>";
			lString += " <td class=\"cRosso\"> " + arg + " </td>  ";
		}
		// fine MEV_67

		lString += "</tr>";

		return lString;
	}

	/**
	 * Inserimento Tenori e Provvedimenti per Fascicolo SIUS (caricamento pilotato dall'evento).
	 *
	 * @param lEve
	 * @param lConn
	 * @return lRapporto
	 * @throws F3BException
	 */
	private String inserisciTenoriEProvvedimenti(String aRapporto, EventoNotificaModel lEve, Connection lConn)
			throws F3BException {

		// Fase di inserimento dei Provvedimenti e relativi Tenore.
		DepositoDecretoDAO lDepDecDao = null;
		DepositoOrdinanzaPcDAO lDepOrdDao = null;
		TenoreDAO lTenDao = null;

		String lCodEsito = "";

		if (lEve.getFascicoloGPTP() != null && lEve.getFascicoloGPTP().getTenori() != null) {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Numero Tenori trattati : " + lEve.getFascicoloGPTP().getTenori().length);
			// for (int i=0; i<lEve.getFascicoloGPTP().getTenori().length-1; i++)
			for (int i = 0; i < lEve.getFascicoloGPTP().getTenori().length; i++) {
				// Deposito Decreto
				if (lEve.getFascicoloGPTP().getTenori()[i] != null
						&& lEve.getFascicoloGPTP().getTenori()[i].getTenore() != null
						&& lEve.getFascicoloGPTP().getTenori()[i].getTenore()
								.getDepDecIdDepositoDecreto() != null) {
					DepositoDecretoModel lDecreto = lEve.getFascicoloGPTP().getTenori()[i].getDecreto();
					if (lDecreto != null) {
						try
						//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						//// posto di LogF3B.getLogger()
						// siesLogger.info("Nel TRY");
						{
							lDepDecDao = new DepositoDecretoDAO(lConn);
							lDepDecDao.setDAOFromModel(lEve.getFascicoloGPTP().getTenori()[i].getDecreto());
							lDepDecDao.setWithoutSequence(true);
							lDepDecDao.insert();
							lCodEsito = "00000";
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED)
								lCodEsito = "00001";
							else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
								lCodEsito = "00002";
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.error("Errore di Costraint di Integrità violata : "
										+ ex.getErrorCode() + " - " + ex.getMessage());
							} else {
								lCodEsito = "33333";
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
										+ ex.getMessage());
							}
						}
						aRapporto += buildRapporto("Inserimento ",
								"Deposito Decreto "
										+ lEve.getFascicoloGPTP().getTenori()[i].getDecreto()
												.getIdDepositoDecreto()
										+ " x il Decreto " + lEve.getFascicoloGPTP().getTenori()[i]
												.getDecreto().getIdDepositoDecreto().toString(),
								lCodEsito);
					}
				}

				// DepositoOrdinanzaPC
				if (lEve.getFascicoloGPTP().getTenori()[i] != null
						&& lEve.getFascicoloGPTP().getTenori()[i].getTenore() != null
						&& lEve.getFascicoloGPTP().getTenori()[i].getTenore()
								.getDepOpidDepositoOrdinanzaPc() != null) {
					DepositoOrdinanzaPcModel lOrdinanza = lEve.getFascicoloGPTP().getTenori()[i]
							.getOrdinanza();
					if (lOrdinanza != null) {
						try {
							lDepOrdDao = new DepositoOrdinanzaPcDAO(lConn);
							lDepOrdDao.setDAOFromModel(lEve.getFascicoloGPTP().getTenori()[i].getOrdinanza());
							lDepOrdDao.setWithoutSequence(true);
							lDepOrdDao.insert();
							lCodEsito = "00000";
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED)
								lCodEsito = "00001";
							else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
								lCodEsito = "00002";
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.error("Errore di Costraint di Integrità violata : "
										+ ex.getErrorCode() + " - " + ex.getMessage());
							} else {
								lCodEsito = "33333";
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
										+ ex.getMessage());
							}
						}
						aRapporto += buildRapporto("Inserimento ",
								"Deposito Ordinanza "
										+ lEve.getFascicoloGPTP().getTenori()[i].getOrdinanza()
												.getIdDepositoOrdinanzaPc()
										+ " x l' Ordinanza " + lEve.getFascicoloGPTP().getTenori()[i]
												.getOrdinanza().getIdDepositoOrdinanzaPc().toString(),
								lCodEsito);
					}
				}

				// Fase di inserimento per i Tenore. 20/08/2010 Aggiunto controllo su IdTenore.
				if (lEve.getFascicoloGPTP().getTenori()[i] != null
						&& lEve.getFascicoloGPTP().getTenori()[i].getTenore() != null
						&& lEve.getFascicoloGPTP().getTenori()[i].getTenore().getIdTenore() != null) {
					try {
						lTenDao = new TenoreDAO(lConn);
						// Inserimento del tenore.
						lTenDao.setDAOFromModel(lEve.getFascicoloGPTP().getTenori()[i].getTenore());
						lTenDao.setWithoutSequence(true);
						lTenDao.insert();
						lCodEsito = "00000";
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED)
							lCodEsito = "00001";
						else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
							lCodEsito = "00002";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
									+ " - " + ex.getMessage());
						} else {
							lCodEsito = "33333";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
									+ ex.getMessage());
						}
					}
					aRapporto += buildRapporto("Inserimento ",
							"Tenore " + lEve.getFascicoloGPTP().getTenori()[i].getTenore().getIdTenore()
									+ " x il Fascicolo "
									+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
							lCodEsito);
				}
			}
		}
		cleanup(lDepDecDao);
		cleanup(lDepOrdDao);
		cleanup(lTenDao);

		return aRapporto;
	}

	/**
	 * Inserimento Allegati per Fascicolo SIUS (caricamento pilotato dall'evento).
	 *
	 * @param lEve
	 * @param lConn
	 * @return lRapporto
	 * @throws F3BException
	 */
	private String inserisciAllegati(String aRapporto, EventoNotificaModel lEve, Connection lConn)
			throws F3BException {

		// Fase di inserimento degli Allegati.
		DocumentoAllegatoDAO lAllegatoDao = null;
		String lCodEsito;

		if (!Utils.isNullObj(lEve.getFascicoloGPTP())
				&& !Utils.isNullObj(lEve.getFascicoloGPTP().getAllegati())) {
			// Fase di inserimento degli Allegati.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Numero Allegati trattati : " + lEve.getFascicoloGPTP().getAllegati().size());
			// for (int i=0; i<lEve.getFascicoloGPTP().getTenori().length-1; i++)
			for (int i = 0; i < lEve.getFascicoloGPTP().getAllegati().size(); i++) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.info("Nel ciclo FOR x gli Allegati");
				try {
					lAllegatoDao = new DocumentoAllegatoDAO(lConn);
					// Inserimento del DOCUMENTO_ALLEGATO.
					DocumentoAllegatoModel allegato = (DocumentoAllegatoModel) lEve.getFascicoloGPTP()
							.getAllegati().get(i);
					lAllegatoDao.setDAOFromModel(allegato);
					if (allegato.getDocPerTrasferimento() != null
							&& allegato.getDocPerTrasferimento().length > 1) {
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.info("Controllo BBBLOBBB Out" +
						// allegato.getDocPerTrasferimento().length);
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.info("Settato BLOBBBB");
						lAllegatoDao.setDocBlob(new ByteArrayInputStream(allegato.getDocPerTrasferimento()));
					}
					lAllegatoDao.setWithoutSequence(true);
					lAllegatoDao.insert();
					lCodEsito = "00000";

					aRapporto += buildRapporto("Inserimento ",
							"Allegato " + allegato.getIdDocumentoAllegato() + " x il Fascicolo "
									+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
							lCodEsito);
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						lCodEsito = "00001";
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						lCodEsito = "00002";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
								+ " - " + ex.getMessage());
					} else {
						lCodEsito = "33333";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
								+ ex.getMessage());
					}
				} finally {
					// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
					cleanup(lAllegatoDao);
				}
			}
		}

		return aRapporto;
	}

	/**
	 * Inserimento Altri Dati per Fascicolo SIUS (caricamento pilotato dall'evento).
	 *
	 * @param lEve
	 * @param lConn
	 * @return lRapporto
	 * @throws F3BException
	 */
	private String inserisciAltriDatiSius(String aRapporto, EventoNotificaModel lEve, Connection lConn)
			throws F3BException {

		// Fase di inserimento degli Avvocati.
		siap.sius.avvocato.dao.AvvocatoDAO lAvvDao = null;
		AvvocatoFascicoloSiusDAO lAvvFasSiusDao = null;

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		MisuraSicurezzaDAO lMSDao = null;
		RichiestaConversioneDAO lRCDao = null;
		ScambioSanzioneDAO lSSDao = null;
		ResidenzaFascicoloSiusDAO lResFasSiuDao = null;
		ResidenzaDAO lResDao = null;
		MagistratoRelatoreDAO lMagRelDao = null;
		MagistratoDAO lMagDao = null;
		NoteDAO lNoteDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		EsecuzioneSanzioneSostitutivaDAO lESSDao = null;
		PeriodoAltraSanzioneDAO lPASDao = null;

		String lCodEsito;

		try {
			if (!Utils.isNullObj(lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento()) && (!Utils
					.isNullObj(lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getAvvocatiFasSius()))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("Numero Avvocati trattati : "
						+ lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getAvvocatiFasSius().size());
				for (int i = 0; i < lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getAvvocatiFasSius()
						.size(); i++) {
					try {
						AvvocatoSiusModel lAvvSIUS = (AvvocatoSiusModel) (lEve.getFascicoloGPTP()
								.getDatiSiusPerTrasferimento().getAvvocatiFasSius().get(i));

						lAvvDao = new siap.sius.avvocato.dao.AvvocatoDAO(lConn);

						lAvvDao.setDAOFromModel(lAvvSIUS.getAvvocato());
						lAvvDao.setWithoutSequence(true);
						lAvvDao.insert();
						lAvvDao.stop();
						lCodEsito = "00000";

						lAvvFasSiusDao = new AvvocatoFascicoloSiusDAO(lConn);
						lAvvFasSiusDao.setDAOFromModel(lAvvSIUS.getAvvocatoFascicoloSiusModel());
						lAvvFasSiusDao.setAvvIdAvvocato(lAvvSIUS.getAvvocato().getIdAvvocato());
						lAvvFasSiusDao.setWithoutSequence(true);
						lAvvFasSiusDao.insert();
						lAvvFasSiusDao.stop();
						lCodEsito = "00000";

						aRapporto += buildRapporto("Inserimento ", "Avvocato "
								+ lAvvSIUS.getAvvocato().getIdAvvocato() + " x il Fascicolo "
								+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
								lCodEsito);
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED)
							lCodEsito = "00001";
						else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
							lCodEsito = "00002";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto
							// di LogF3B.getLogger()
							siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
									+ " - " + ex.getMessage());
						} else {
							lCodEsito = "33333";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
									+ ex.getMessage());
						}
					}
				}
			}

			// Fase di inserimento delle Residenze/Domicilii.
			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null && lEve.getFascicoloGPTP()
					.getDatiSiusPerTrasferimento().getListResidenzaFasSius() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("Numero Residenze trattate : " + lEve.getFascicoloGPTP()
						.getDatiSiusPerTrasferimento().getListResidenzaFasSius().size());
				for (int i = 0; i < lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento()
						.getListResidenzaFasSius().size(); i++) {
					ResidenzaAssociataModel lResSIUS = (ResidenzaAssociataModel) (lEve.getFascicoloGPTP()
							.getDatiSiusPerTrasferimento().getListResidenzaFasSius().get(i));
					try {
						lResDao = new ResidenzaDAO(lConn);

						lResDao.setDAOFromModel(lResSIUS.getResidenza());
						lResDao.setWithoutSequence(true);
						lResDao.insert();
						lResDao.stop();
						lCodEsito = "00000";
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED)
							lCodEsito = "00001";
						else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
							lCodEsito = "00002";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
									+ " - " + ex.getMessage());
						} else {
							lCodEsito = "33333";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
									+ ex.getMessage());
						}
					}
					aRapporto += buildRapporto("Inserimento ",
							"Residenza " + lResSIUS.getResidenza().getIdResidenza() + " x il Fascicolo "
									+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
							lCodEsito);

					try {
						lResFasSiuDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFasSiuDao.setDAOFromModel(lResSIUS.getResidenzaFascicoloSius());
						lResFasSiuDao.setResIdResidenza(lResSIUS.getResidenza().getIdResidenza());
						lResFasSiuDao.setWithoutSequence(true);
						lResFasSiuDao.insert();
						lResFasSiuDao.stop();
						lCodEsito = "00000";
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED)
							lCodEsito = "00001";
						else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
							lCodEsito = "00002";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
									+ " - " + ex.getMessage());
						} else {
							lCodEsito = "33333";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
									+ ex.getMessage());
						}
					}
					aRapporto += buildRapporto("Inserimento ",
							"Residenza SIUS " + lResSIUS.getResidenzaFascicoloSius().getResIdResidenza()
									+ " x il Fascicolo "
									+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
							lCodEsito);
				}
			}

			// Fase di inserimento del MAGISTRATO RELATORE.
			MagistratoRelatoreModel lMagRelMod = null;

			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getMagistratoRelatore() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getMagistratoRelatore()
							.getMagistrato() != null) {
				try {
					lMagRelMod = lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento()
							.getMagistratoRelatore();

					lMagDao = new MagistratoDAO(lConn);
					lMagDao.setDAOFromModel(lMagRelMod.getMagistrato());
					lMagDao.setWithoutSequence(true);
					lMagDao.insert();
					lMagDao.stop();
					lCodEsito = "00000";
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						lCodEsito = "00001";
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						lCodEsito = "00002";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
								+ " - " + ex.getMessage());
					} else {
						lCodEsito = "33333";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
								+ ex.getMessage());
					}
				}
				aRapporto += buildRapporto("Inserimento ",
						"Magistrato " + lMagRelMod.getMagistrato().getCodMagistrato() + " x il Fascicolo "
								+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
						lCodEsito);

				try {
					lMagRelDao = new MagistratoRelatoreDAO(lConn);

					// 14/07/2008 Segnalazione interna: In fase di presa in carico Dati SIUS, il Mag.
					// Relatore, privo di Sequence, veniva comunque inserito, con duplicazioni.
					lMagRelDao.setCondizionePerFascicoloSius(lMagRelMod.getFasSiuIdFascicoloSius());
					lMagRelDao.delete();
					lMagRelDao.stop();

					lMagRelDao.setDAOFromModel(lMagRelMod);
					lMagRelDao.setWithoutSequence(true);
					lMagRelDao.insert();
					lMagRelDao.stop();
					lCodEsito = "00000";
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						lCodEsito = "00001";
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						lCodEsito = "00002";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
								+ " - " + ex.getMessage());
					} else {
						lCodEsito = "33333";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
								+ ex.getMessage());
					}
				}
				aRapporto += buildRapporto("Inserimento ",
						"Magistrato Relatore " + lMagRelMod.getMagCodMagistrato() + " x il Fascicolo "
								+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
						lCodEsito);
			}

			// Fase di inserimento del LuogoDetenzione.
			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getLuogoDetenzione() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getLuogoDetenzione()
							.getIdLuogoDetenzione() != null) {
				LuogoDetenzioneModel lLuogoDet = lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento()
						.getLuogoDetenzione();
				try {
					lLuoDetDao = new LuogoDetenzioneDAO(lConn);

					lLuoDetDao.setDAOFromModel(lLuogoDet);
					lLuoDetDao.setWithoutSequence(true);
					lLuoDetDao.insert();
					lLuoDetDao.stop();
					lCodEsito = "00000";
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						lCodEsito = "00001";
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						lCodEsito = "00002";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
								+ " - " + ex.getMessage());
					} else {
						lCodEsito = "33333";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
								+ ex.getMessage());
					}
				}
				aRapporto += buildRapporto("Inserimento ",
						"LuogoDetenzione " + lLuogoDet.getIdLuogoDetenzione() + " x il Fascicolo "
								+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
						lCodEsito);
			}

			// Fase di inserimento delle Note per ID Fascicolo SIUS.
			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getNote() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("Numero Note trattate : "
						+ lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getNote().size());
				for (int i = 0; i < lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getNote()
						.size(); i++) {
					NoteModel lNote = (NoteModel) (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento()
							.getNote().get(i));
					try {
						lNoteDao = new NoteDAO(lConn);

						lNoteDao.setDAOFromModel(lNote);
						lNoteDao.setWithoutSequence(true);
						lNoteDao.insert();
						lNoteDao.stop();
						lCodEsito = "00000";
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED)
							lCodEsito = "00001";
						else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
							lCodEsito = "00002";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
									+ " - " + ex.getMessage());
						} else {
							lCodEsito = "33333";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
									+ ex.getMessage());
						}
					}
					aRapporto += buildRapporto("Inserimento ",
							"Nota " + lNote.getIdNote() + " x il Fascicolo "
									+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
							lCodEsito);
				}
			}

			// 25/02/2008 Fase di inserimento dell' EsecuzioneSanzioneSostitutiva.
			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getESS() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getESS()
							.getIdEsecuzioneSanzioneSost() != null) {
				EsecuzioneSanzioneSostitutivaModel lESSMod = lEve.getFascicoloGPTP()
						.getDatiSiusPerTrasferimento().getESS();
				try {
					lESSDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
					lESSDao.setDAOFromModel(lESSMod);
					lESSDao.setWithoutSequence(true);
					lESSDao.insert();
					lESSDao.stop();
					lCodEsito = "00000";
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						lCodEsito = "00001";
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						lCodEsito = "00002";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
								+ " - " + ex.getMessage());
					} else {
						lCodEsito = "33333";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
								+ ex.getMessage());
					}
				}
				aRapporto += buildRapporto("Inserimento ",
						"Esec. Sanz. Sost. " + lESSMod.getIdEsecuzioneSanzioneSost() + " x il Fascicolo "
								+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
						lCodEsito);
			}

			// 25/02/2008 Fase di inserimento dei Periodi Altra Sanzione per ID Fascicolo SIUS.
			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getPAS() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("Numero Periodi SS trattati : "
						+ lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getPAS().size());
				for (int i = 0; i < lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getPAS()
						.size(); i++) {
					PeriodoAltraSanzioneModel lPASMod = (PeriodoAltraSanzioneModel) (lEve.getFascicoloGPTP()
							.getDatiSiusPerTrasferimento().getPAS().get(i));
					try {
						lPASDao = new PeriodoAltraSanzioneDAO(lConn);
						lPASDao.setDAOFromModel(lPASMod);
						lPASDao.setWithoutSequence(true);
						lPASDao.insert();
						lPASDao.stop();
						lCodEsito = "00000";
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED)
							lCodEsito = "00001";
						else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
							lCodEsito = "00002";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
									+ " - " + ex.getMessage());
						} else {
							lCodEsito = "33333";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
									+ ex.getMessage());
						}
					}
					aRapporto += buildRapporto("Inserimento ",
							"Periodo Altra Sanzione " + lPASMod.getIdPeriodoAltraSanzione()
									+ " x il Fascicolo "
									+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
							lCodEsito);
				}
			}

			// 25/02/2008 Fase di inserimento di Scambio Sanzione.
			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getSS() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getSS()
							.getIdScambioSanzione() != null) {
				ScambioSanzioneModel lSSMod = lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getSS();
				try {
					lSSDao = new ScambioSanzioneDAO(lConn);
					lSSDao.setDAOFromModel(lSSMod);
					lSSDao.setWithoutSequence(true);
					lSSDao.insert();
					lSSDao.stop();
					lCodEsito = "00000";
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						lCodEsito = "00001";
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						lCodEsito = "00002";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
								+ " - " + ex.getMessage());
					} else {
						lCodEsito = "33333";
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
								+ ex.getMessage());
					}
				}
				aRapporto += buildRapporto("Inserimento ",
						"Scambio Sanzione " + lSSMod.getIdScambioSanzione() + " x il Fascicolo "
								+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
						lCodEsito);
			}

			// 23/03/2009 Fase di inserimento delle Richieste Conversione Pena Pecuniaria ID Fascicolo SIUS.
			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getRCPP() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("Numero Richieste Conv. PP trattate : "
						+ lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getRCPP().size());
				for (int i = 0; i < lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getRCPP()
						.size(); i++) {
					RichiestaConversioneModel lRCMod = (RichiestaConversioneModel) (lEve.getFascicoloGPTP()
							.getDatiSiusPerTrasferimento().getRCPP().get(i));
					try {
						lRCDao = new RichiestaConversioneDAO(lConn);
						lRCDao.setDAOFromModel(lRCMod);
						lRCDao.setWithoutSequence(true);
						/* RichiestaConversioneModel lRCM = (RichiestaConversioneModel) */lRCDao
								.getModelByKey();
						lRCDao.insert();
						lRCDao.stop();
						lCodEsito = "00000";
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							// 03/03/2015 In caso già esista la Richiesta Conversione, occorre aggiornarla coi
							// dati elaborati dalla Sorveglianza.
							try {
								lRCDao = new RichiestaConversioneDAO(lConn);
								lRCDao.setDAOFromModel(lRCMod);
								lRCDao.selCondizioneUpdate(lRCMod.getIdRichiestaConversione());
								lRCDao.update();
								lRCDao.stop();
								lCodEsito = "00000";
							} catch (DAOException ex2) {
								lCodEsito = "00001";
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.error("Errore di Costraint di Integrità violata su update : "
										+ ex2.getErrorCode() + " - " + ex2.getMessage());
							}
						} else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
							lCodEsito = "00002";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
									+ " - " + ex.getMessage());
						} else {
							lCodEsito = "33333";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
									+ ex.getMessage());
						}
					}
					aRapporto += buildRapporto("Inserimento ",
							"Periodo Altra Sanzione " + lRCMod.getIdRichiestaConversione()
									+ " x il Fascicolo "
									+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
							lCodEsito);
				}
			}

			// 08/01/2013 Fase di inserimento delle Misure di Sicurezza Applicata per ID Fascicolo SIUS.
			if (lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento() != null
					&& lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getMSA() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("Numero Misure di Sicurezza trattate : "
						+ lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getMSA().size());
				for (int i = 0; i < lEve.getFascicoloGPTP().getDatiSiusPerTrasferimento().getMSA()
						.size(); i++) {
					MisuraSicurezzaModel lMSMod = (MisuraSicurezzaModel) (lEve.getFascicoloGPTP()
							.getDatiSiusPerTrasferimento().getMSA().get(i));
					try {
						lMSDao = new MisuraSicurezzaDAO(lConn);
						lMSDao.setDAOFromModel(lMSMod);
						lMSDao.setWithoutSequence(true);
						lMSDao.insert();
						lMSDao.stop();
						lCodEsito = "00000";
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED)
							lCodEsito = "00001";
						else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
							lCodEsito = "00002";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore di Costraint di Integrità violata : " + ex.getErrorCode()
									+ " - " + ex.getMessage());
						} else {
							lCodEsito = "33333";
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.error("Errore DAO non Classificato : " + ex.getErrorCode() + " - "
									+ ex.getMessage());
						}
					}
					aRapporto += buildRapporto("Inserimento ",
							"Misura di Sicurezza " + lMSMod.getIdMisuraSicurezza() + " x il Fascicolo "
									+ lEve.getFascicoloGPTP().getFascicoloSiusModel().getIdFascicoloSius(),
							lCodEsito);
				}
			}
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAvvFasSiusDao);
			cleanup(lAvvDao);
			cleanup(lResDao);
			cleanup(lResFasSiuDao);
			cleanup(lMagDao);
			cleanup(lMagRelDao);
			cleanup(lNoteDao);
			cleanup(lLuoDetDao);
			cleanup(lESSDao);
			cleanup(lPASDao);
			cleanup(lSSDao);
			cleanup(lRCDao);
			cleanup(lMSDao); // 08/01/2015
		}

		return aRapporto;
	}

}