package siap.sius.jms.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.assistentegiudiziario.dao.AssistenteGiudiziarioSqlDAO;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO; // STUB 14/04/2005
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel; // STUB 14/04/2005
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penapecuniaria.dao.RichiestaConversioneSqlDAO;
import siap.siep.scambiosanzione.dao.ScambioSanzioneSqlDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel; // STUB 14/04/2005
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.dao.ImpugnazioneSigeDAO;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.SIUSException;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO; // STUB 07/04/2005
import siap.sius.avvocato.model.AvvocatoSiusModel; // STUB 07/04/2005
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.dao.DepositoSentenzaSqlDAO;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato; //  STUB 12/04/2005
import siap.sius.documentoallegato.dao.DocumentoAllegatoSqlDAO; //  STUB 07/04/2005
import siap.sius.documentoallegato.model.DocumentoAllegatoModel; //  STUB 12/04/2005
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaSqlDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.esperto.dao.EspertoSqlDAO;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloGPTPModel;
import siap.sius.impugnazione.dao.ImpugnazioneSqlDAO;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.motivazionedecreto.dao.MotivazioneDecretoSqlDAO;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.permesso.dao.EventoPermessoLicenzaSqlDAO;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import siap.sius.prescrizione.dao.PrescrizioneSqlDAO;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.rifasiep.dao.RiferimentoFascicoloSiepSqlDAO; // STUB 14/04/2005
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.rifasius.controller.IRiferimentoFascicoloSius;
import siap.sius.rifasius.dao.RiferimentoFascicoloSiusDAO;
import siap.sius.rifasius.model.RiferimentoFascicoloSiusModel;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneSqlDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.dao.UdienzaSqlDAO;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title:TrasmissioneJMScontroller
 * </p>
 * <p>
 * Description:
 * </p>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TrasmissioneJMSController extends SiapController implements ITrasmissioneJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Preleva i dati dal DB per la stampe di Ordinanza
	 * 
	 * @param aKeyEvento
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel getMessageForOrdinanza(BigDecimal aKeyEvento) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getMessageForOrdinanza: inizio");

		Connection lConn = null;

		FascicoloSiepSqlDAO lFasSiepDao = null;
		SentenzaSqlDAO lSentenzaDao = null;
		FascicoloGPSqlDAO lFasDao = null;
		UdienzaSqlDAO lUdiDao = null;
		DepositoOrdinanzaPcSqlDAO lOrdDao = null;
		PrescrizioneSqlDAO lPreDao = null;
		TenoreSqlDAO lTenDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		MisuraSicurezzaSqlDAO lMisSicSqlDao = null;
		ImpugnazioneSqlDAO lImpDao = null;

		// inserite 11/01/2005 serena
		LicenzaLibanticipataSqlDAO lLibAntSqlDAO = null;
		PeriodoLibanticipataSqlDAO lPerAntSqlDAO = null;
		// /EventoPermessoLicenzaDAO lEvPermLicDAO = null;
		EventoPermessoLicenzaSqlDAO lEvPermLicSqlDAO = null;
		// STUB 07/04/2005.
		AvvocatoFascicoloSiusSqlDAO lAvvDao = null;
		// STUB 12/04/2005.
		DocumentoAllegatoSqlDAO lAllSqlDao = null;
		// STUB 14/04/2005.
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		RiferimentoFascicoloSiepSqlDAO lRFSSqlDao = null;

		// 21/02/2008
		EsecuzioneSanzioneSostitutivaSqlDAO lESSSqlDao = null;
		PeriodoAltraSanzioneSqlDAO lPASSqlDao = null;
		ScambioSanzioneSqlDAO lSSSqlDao = null;
		// 23/03/2009
		RichiestaConversioneSqlDAO lRCSqlDao = null;

		MessaggioModel lMessage = null;

		try {
			lConn = getDBConnection();

			/**
			 * Cerca L'evento e le informazioni correlate --AVVOCATO_FASCICOLO_SIUS --EVENTO --NOTIFICA
			 * --AUTORITA_ESTERNA
			 */
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(aKeyEvento);

			if (lEveNot == null || lEveNot.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Errore nei dati:evento mancante!");

			// Lettura del BLOB e suo inserimento nel formato utile al trasferimento (byte[])
			lEveNot.getEvento().setDocPerTrasferimento(
					lCtrlEve.ExGetDocPerTrasferimento(lEveNot.getEvento().getIdEvento()));
			// Creazione della Root del TreeModel
			TreeModel lTreeRoot = new TreeModel(createRoot(lEveNot.getEvento()));
			// Aggiunta Evento
			lTreeRoot.add(new TreeModel(lEveNot));

			// STUB 12/04/2005 Aggiunto DOCUMENTO_ALLEGATO
			lAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			lAllSqlDao.ricercaDocumentoAllegatoByIdEvento(lEveNot.getEvento().getIdEvento());
			DocumentoAllegatoModel lDAModel = (DocumentoAllegatoModel) lAllSqlDao.getModelByKey();

			// Lettura del BLOB e suo inserimento nel formato utile al trasferimento (byte[])
			IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote();
			if (lDAModel != null)
				lDAModel.setDocPerTrasferimento(lCtrlDA.ExGetDocPerTrasferimento(lDAModel
						.getIdDocumentoAllegato()));
			// lDAModel.setDocPerTrasferimento(lDAModel.getDocBlobOut().toByteArray()) ;

			lTreeRoot.add(new TreeModel(lDAModel));

			// FASCICOLO SIUS - GENERALE PROCEDIMENTO
			BigDecimal lKeyFascicolo = lEveNot.getEvento().getFasSiuIdFascicoloSius();

			lFasDao = new FascicoloGPSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloGPModel lFasModel = (FascicoloGPModel) lFasDao.getModelByKey();

			// STUB 25/11/2004 lettura dell'UDIENZA solo per la data da caricare in DATA_CAMERA_CONSIGLIO.
			BigDecimal lKeyUdienza = lFasModel.getGeneraleProcedimentoModel().getUdiIdUdienza();
			if (lKeyUdienza != null) {
				lUdiDao = new UdienzaSqlDAO(lConn);
				lUdiDao.ricercaUdienzaByKey(lKeyUdienza);
				UdienzaModel lUdienza = (UdienzaModel) lUdiDao.getModelByKey();

				if (lUdienza != null && lUdienza.getDataUdienza() != null) {
					lFasModel.getGeneraleProcedimentoModel()
							.setDataCameraConsiglio(lUdienza.getDataUdienza());
				}
			}

			// SOGGETTO SIUS ( Dal 12/10/2010 i Soggetti SIUS e SIEP sono slegati )
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoByKey(lFasModel.getFascicoloSiusModel().getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
			lSogSqlDao.stop(); // 12/10/2010

			lFasModel.getFascicoloSiusModel().setSoggetto(lSogModel);

			lTreeRoot.add(new TreeModel(lFasModel));

			// 12/10/2010 FASCICOLO SIEP - SENTENZA - SOGGETTO
			if (lFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lFasSiepDao = new FascicoloSiepSqlDAO(lConn);
				lFasSiepDao.ricercaFascicoloByKey(lFasModel.getFascicoloSiusModel()
						.getFasSieIdFascicoloSiep());
				FascicoloSiepModel lFasSiep = (FascicoloSiepModel) lFasSiepDao.getModelByKey();
				cleanup(lFasSiepDao);

				lSentenzaDao = new SentenzaSqlDAO(lConn);
				lSentenzaDao.ricercaSentenzaBykey(lFasSiep.getSenIdSentenza());
				SentenzaModel lSentenza = (SentenzaModel) lSentenzaDao.getModelByKey();
				cleanup(lSentenzaDao);

				// 12/10/2010 Inserito Soggetto SIEP.
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(lFasSiep.getSogIdSoggetto());
				lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
				lFasSiep.setSoggetto(lSogModel);

				lTreeRoot.add(new TreeModel(lFasSiep));
				lTreeRoot.add(new TreeModel(lSentenza));
			}

			// RESIDENZA FASCICOLO SIUS
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			lResSqlDao.ricercaResidenzaByFascicoloSius(lKeyFascicolo);
			ResidenzaModel lResMod = (ResidenzaModel) lResSqlDao.getModelByKey();

			if (lResMod != null) {
				ResidenzaFascicoloSiusModel lResFasSiuMod = null;

				lResSqlDao.stop();
				lResSqlDao.ricercaResidenzaByFascicoloSius(lKeyFascicolo);
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					lResFasSiuMod = (ResidenzaFascicoloSiusModel) lResSqlDao.getModelResidenzaFascicoloSius();
				}
				ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
				lResAss.setResidenza(lResMod);
				lResAss.setResidenzaFascicoloSius(lResFasSiuMod);

				lTreeRoot.add(new TreeModel(lResAss));
			}

			// STUB 07/04/2005 AVVOCATI SIUS.
			lAvvDao = new AvvocatoFascicoloSiusSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);

			List lAvvocati = new ArrayList(lAvvDao.getModels());
			Iterator lItAvv = lAvvocati.iterator();
			while (lItAvv.hasNext()) {
				lTreeRoot.add(new TreeModel((AvvocatoSiusModel) lItAvv.next()));
			}

			// STUB 14/04/2005 RIFERIMENTI_FASCICOLI_SIEP.
			lRFSSqlDao = new RiferimentoFascicoloSiepSqlDAO(lConn);
			lRFSSqlDao.ricercaRiferimentoFascicoloSiepByIdFasSius(lKeyFascicolo);

			List lRifFasSie = new ArrayList(lRFSSqlDao.getModels());
			Iterator lItRFS = lRifFasSie.iterator();
			while (lItRFS.hasNext()) {
				lTreeRoot.add(new TreeModel((RiferimentoFascicoloSiepModel) lItRFS.next()));
			}

			// STUB 14/04/2005 LUOGO_DETENZIONE.
			lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDetSqlDao.ricercaLuoghiDetenzioneByFascicoloSius(lKeyFascicolo);
			LuogoDetenzioneModel lLDModel = (LuogoDetenzioneModel) lLuoDetSqlDao.getModelByKey();
			if (lLDModel != null)
				lTreeRoot.add(new TreeModel(lLDModel));

			// DEPOSITO ORDINANZA PC
			lOrdDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lOrdDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aKeyEvento);
			DepositoOrdinanzaPcModel lDepOrd = (DepositoOrdinanzaPcModel) lOrdDao.getModelByKey();

			if (lDepOrd != null) {
				lDepOrd.setAnnoDataCameraConsiglio(DateUtils.getDateToString(
						lDepOrd.getDataCameraConsiglio(), "yyyy"));
				lDepOrd.setGiornoDataCameraConsiglio(DateUtils.getDateToString(
						lDepOrd.getDataCameraConsiglio(), "dd"));
				lDepOrd.setMeseDataCameraConsiglio(DateUtils.getDateToString(
						lDepOrd.getDataCameraConsiglio(), "MMMM"));

				// LICENZA LIBERAZIONE ANTICIPATA E PERIODI inserito 11/01/2005
				lLibAntSqlDAO = new LicenzaLibanticipataSqlDAO(lConn);

				lLibAntSqlDAO.ricercaLicenzaLibanticipataByEve(lDepOrd.getIdEventoGenerato());
				LicenzaLibAnticipataModel lLicenzaMod = (LicenzaLibAnticipataModel) lLibAntSqlDAO
						.getModelByKey();

				LicenzaPeriodiLibAnticipataModel aModel = new LicenzaPeriodiLibAnticipataModel();
				lPerAntSqlDAO = new PeriodoLibanticipataSqlDAO(lConn);

				ArrayList lPeriodi = null;
				ArrayList lEventiPermLic = null; // 23/08/2006
				if (lLicenzaMod != null) {
					aModel.setLicenza(lLicenzaMod);
					lPerAntSqlDAO.ricercaPeriodoLibanticipataByLic(lLicenzaMod.getIdLicenzaLibanticipata());
					lPeriodi = new ArrayList(lPerAntSqlDAO.getModels());
					aModel.setPeriodi((PeriodoLibAnticipataModel[]) lPeriodi
							.toArray(new PeriodoLibAnticipataModel[1]));
				}
				lDepOrd.setLicenzaPeriodiLibAnticipata(aModel);
				lTreeRoot.add(new TreeModel(lDepOrd));
				lLibAntSqlDAO.stop();

				// STUB 21/03/2005 ARRAY LIST LICENZE LIBERAZIONE ANTICIPATA E PERIODI 21/03/2005.
				lLibAntSqlDAO = new LicenzaLibanticipataSqlDAO(lConn);

				Vector lLicenze = null;
				lLibAntSqlDAO.ricercaLicenzaLibanticipataByEve(lDepOrd.getIdEventoGenerato());
				lLicenze = new Vector(lLibAntSqlDAO.getModels());

				Iterator it = lLicenze.iterator();
				while (it.hasNext()) {
					lLicenzaMod = (LicenzaLibAnticipataModel) it.next();

					// Periodi per la LicenzaLibAnticipata Corrente.
					aModel = new LicenzaPeriodiLibAnticipataModel();
					lPerAntSqlDAO = new PeriodoLibanticipataSqlDAO(lConn);
					lEvPermLicSqlDAO = new EventoPermessoLicenzaSqlDAO(lConn); // 23/06/2008

					lPeriodi = null;
					if (lLicenzaMod != null) {
						aModel.setLicenza(lLicenzaMod);
						lPerAntSqlDAO.ricercaPeriodoLibanticipataByLic(lLicenzaMod
								.getIdLicenzaLibanticipata());
						lPeriodi = new ArrayList(lPerAntSqlDAO.getModels());
						aModel.setPeriodi((PeriodoLibAnticipataModel[]) lPeriodi
								.toArray(new PeriodoLibAnticipataModel[1]));

						// Recupero dati degli eventi permessi licenze.
						// /lEvPermLicDAO.setCondizioneByIdLic( lLicenzaMod.getIdLicenzaLibanticipata() );
						// /lEventiPermLic = new ArrayList(lEvPermLicDAO.getModels());
						// /aModel.setEventiPermLic((EventoPermessoLicenzaModel[])lEventiPermLic.toArray(new
						// EventoPermessoLicenzaModel[1]));

						// 23/06/2008 Corretto Recupero dati degli eventi permessi licenze.
						lEvPermLicSqlDAO.ricercaEventoPermessoLicenzaByKeyLicLib(lLicenzaMod
								.getIdLicenzaLibanticipata());
						lEventiPermLic = new ArrayList(lEvPermLicSqlDAO.getModels());
						aModel.setEventiPermLic((EventoPermessoLicenzaModel[]) lEventiPermLic
								.toArray(new EventoPermessoLicenzaModel[1]));
					}
					lTreeRoot.add(new TreeModel(aModel));
				}

				// PRESCRIZIONI
				lPreDao = new PrescrizioneSqlDAO(lConn);
				/*
				 * Le prescrizioni sono collegate all'evento e non più al deposito ordinanza. Luigi 12-12-2003
				 */
				lPreDao.ricercaPrescrizioneByIdEve(aKeyEvento);

				List lPrescrizioni = new ArrayList(lPreDao.getModels());
				Iterator lItx = lPrescrizioni.iterator();
				while (lItx.hasNext()) {
					lTreeRoot.add(new TreeModel((PrescrizioneModel) lItx.next()));
				}

				// IMPUGNAZIONE
				lImpDao = new ImpugnazioneSqlDAO(lConn);
				lImpDao.ricercaImpugnazioneByIdDepositoOrdinanza(lDepOrd.getIdDepositoOrdinanzaPc());
				ImpugnazioneModel lImpMod = (ImpugnazioneModel) lImpDao.getModelByKey();
				if (lImpMod != null) {
					lTreeRoot.add(new TreeModel(lImpMod));
				}

				// TENORI
				lTenDao = new TenoreSqlDAO(lConn);
				// Ricerca Tenori x ID DepositoOrdinanza Luigi 5-12-2003
				lTenDao.ricercaTenoriByOrdinanzaOrderByPeso(lDepOrd.getIdDepositoOrdinanzaPc());
				List lTenori = new ArrayList(lTenDao.getModels());

				if (lTenori.size() == 0) {
					throw new F3BException(F3BException.USER_MESSAGE, "Errore nei dati: tenore mancante!");
				} else {
					Iterator lItxTen = lTenori.iterator();
					while (lItxTen.hasNext()) {
						lTreeRoot.add(new TreeModel((TenoreModel) lItxTen.next()));
					}
				}
			} else
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"TrasmissioneJMSController.getMessageForOrdinanza : Deposito Ordinanza inesistente");

			// MISURA ALTERNATIVA
			lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMisAltSqlDao.ricercaMisuraAlternativaByIdEvento(aKeyEvento);
			MisuraAlternativaModel lMisAltMod = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();
			if (lMisAltMod != null) {
				lTreeRoot.add(new TreeModel(lMisAltMod));
			}

			// MISURA SICUREZZA APPLICATA DA TRASFERIRE PER EVENTUALE ESECUZIONE
			/*
			 * 08/01/2015 Possono esserci + Misure di sicurezza afferenti a un fascicolo SIUS, e non solo con
			 * CodOggettoProcedimento = "U023". if
			 * (lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U023")==0 ) {
			 * // Anche fattibile su tipo ordinanza MS lMisSicSqlDao = new MisuraSicurezzaSqlDAO(lConn);
			 * MisuraSicurezzaModel lMisSicRic = new MisuraSicurezzaModel();
			 * lMisSicRic.setFasSiuIdFascicoloSius(lKeyFascicolo); lMisSicRic.setEveIdEvento(aKeyEvento); //
			 * Se ID evento allora è l'unica attiva. Altrimenti prendo la prima!
			 * lMisSicSqlDao.ricercaMisuraSicurezzaApplicata(lMisSicRic); MisuraSicurezzaModel lMisSicMod =
			 * (MisuraSicurezzaModel)lMisSicSqlDao.getModelByKey(); if( lMisSicMod == null ) {
			 * lMisSicRic.setEveIdEvento(null); // Se ID evento è nullo prendo la prima!
			 * lMisSicSqlDao.ricercaMisuraSicurezzaApplicata(lMisSicRic); lMisSicMod =
			 * (MisuraSicurezzaModel)lMisSicSqlDao.getModelByKey(); } if( lMisSicMod != null ) {
			 * lTreeRoot.add(new TreeModel(lMisSicMod)); } }
			 */

			// 25/02/2008 ESECUZIONE SANZIONE SOSTITUTIVA.
			FascicoloGPTPModel lFasGPTPModel = new FascicoloGPTPModel();
			if (lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019") == 0) {
				lESSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
				lESSSqlDao.ricercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lFasModel
						.getFascicoloSiusModel().getIdFascicoloSius());
				EsecuzioneSanzioneSostitutivaModel lESSMod = (EsecuzioneSanzioneSostitutivaModel) lESSSqlDao
						.getModelByKey();
				lFasGPTPModel.getDatiSiusPerTrasferimento().setESS(lESSMod);
			}

			// 25/02/2008 Periodi Altra Sanzione.
			if (lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019") == 0) {
				Vector lPAS = new Vector();
				lPASSqlDao = new PeriodoAltraSanzioneSqlDAO(lConn);
				lPASSqlDao.ricercaSanzioneSostitutivaByIdFascicolo(lFasModel.getFascicoloSiusModel()
						.getIdFascicoloSius(), "DESC");
				lPAS = new Vector(lPASSqlDao.getModels());
				if (lPAS.size() > 0)
					lFasGPTPModel.getDatiSiusPerTrasferimento().setPAS(lPAS);
			}

			// 25/02/2008 Ricerca di SCAMBIO_SANZIONE per Fascicolo SIUS.
			// 23/03/2009 SCAMBIO_SANZIONE Previsto anche per Conversione Pene Pecuniarie.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA Scambio Sanzione");
			if (lFasModel.getGeneraleProcedimentoModel() != null
					&& lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento() != null
					&& (lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo(ICostantiDepositoOrdinanzaPc.OGG_APPL_SANZ_SOSTITUTIVE) == 0 || lFasModel
							.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE) == 0)) {
				lSSSqlDao = new ScambioSanzioneSqlDAO(lConn);
				ScambioSanzioneModel lSSMod = new ScambioSanzioneModel();
				// 28/11/2011 Corretto il riferimento al model valorizzato (lFasModel invece di
				// lFasGPTPModel.)
				// lSSMod.setChiaveAnnoFascicoloSius(lFasGPTPModel.getFascicoloSiusModel().getChiaveAnno());
				// lSSMod.setChiaveProgrFascicoloSius(lFasGPTPModel.getFascicoloSiusModel().getChiaveProgr());
				// lSSMod.setCodUfficioSorveglianza(lFasGPTPModel.getFascicoloSiusModel().getChiaveUfficio());
				lSSMod.setChiaveAnnoFascicoloSius(lFasModel.getFascicoloSiusModel().getChiaveAnno());
				lSSMod.setChiaveProgrFascicoloSius(lFasModel.getFascicoloSiusModel().getChiaveProgr());
				lSSMod.setCodUfficioSorveglianza(lFasModel.getFascicoloSiusModel().getChiaveUfficio());
				lSSSqlDao.ricercaScambioSanzione(lSSMod);
				lSSSqlDao.start();
				if (lSSSqlDao.next()) {
					lSSMod = (ScambioSanzioneModel) lSSSqlDao.getModelByKey();
					lFasGPTPModel.getDatiSiusPerTrasferimento().setSS(lSSMod);
				}
			}
			// 23/03/2009 Ricerca di RICHIESTA_CONVERSIONE per Fascicolo SIUS (Conversione Pene Pecuniarie).
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di ricerca  RICHIESTA_CONVERSIONE");
			if (lFasModel.getGeneraleProcedimentoModel() != null
					&& lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento() != null
					&& lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE) == 0) {
				Vector lRCPP = new Vector();
				lRCSqlDao = new RichiestaConversioneSqlDAO(lConn);
				lRCSqlDao.ricercaRichiestaConversioneByIdFasSIUS(lFasModel.getFascicoloSiusModel()
						.getIdFascicoloSius());
				lRCPP = new Vector(lRCSqlDao.getModels());
				if (lRCPP.size() > 0)
					lFasGPTPModel.getDatiSiusPerTrasferimento().setRCPP(lRCPP);
			}

			// 08/01/2015 MISURE di SICUREZZA APPLICATA.
			// Vengono gestite più Misure di sicurezza afferenti a un fascicolo SIUS,
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di ricerca MISURE di SICUREZZA APPLICATA");
			lMisSicSqlDao = new MisuraSicurezzaSqlDAO(lConn);
			MisuraSicurezzaModel lMisSicRic = new MisuraSicurezzaModel();
			lMisSicRic.setFasSiuIdFascicoloSius(lKeyFascicolo);
			// lMisSicRic.setEveIdEvento(aKeyEvento); // 18/02/2015 Le Mis.Sicurezza SIUS non sono di norma
			// contrassegnate per EVE_ID_EVENTO.
			lMisSicSqlDao.ricercaMisuraSicurezzaApplicata(lMisSicRic);
			Vector lMSA = new Vector(lMisSicSqlDao.getModels());
			if (lMSA.size() > 0)
				lFasGPTPModel.getDatiSiusPerTrasferimento().setMSA(lMSA);

			if (lFasGPTPModel != null) {
				lTreeRoot.add(new TreeModel(lFasGPTPModel));
			}

			lMessage = new MessaggioModel();
			lMessage.setTreeModel(lTreeRoot);

			// ReportGenerator lRep = new ReportGenerator();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug( "Treemodel nel messaggio " + lRep.debugTreeXML(lTreeRoot));

		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioneJMSController.getMessageForOrdinanza: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TrasmissioneJMSController.getMessageForOrdinanza: " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lUdiDao);
			cleanup(lOrdDao);
			cleanup(lPreDao);
			cleanup(lTenDao);
			cleanup(lSogSqlDao);
			cleanup(lResSqlDao);
			cleanup(lMisAltSqlDao);
			cleanup(lMisSicSqlDao);
			cleanup(lImpDao);
			cleanup(lFasSiepDao);
			cleanup(lSentenzaDao);
			cleanup(lLibAntSqlDAO);
			cleanup(lPerAntSqlDAO);
			cleanup(lAvvDao); // STUB 07/04/2005.
			cleanup(lAllSqlDao); // STUB 12/04/2005.
			cleanup(lESSSqlDao);
			cleanup(lPASSqlDao);
			cleanup(lSSSqlDao);
			// /cleanup(lEvPermLicDAO);
			cleanup(lEvPermLicSqlDAO);
			cleanup(lRCSqlDao); // 23/03/2009
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getMessageForOrdinanza: fine");

		return lMessage;
	}

	/**
	 * Crea la root del Documento
	 * 
	 * @param aEveModel
	 * @return
	 */
	private XModel createRoot(EventoModel lEve) {
		XModel lStampa = new XModel();

		String descrTipoUff = lEve.getDescrUfficioEmittente().toUpperCase();

		lStampa.setUfficio(lEve.getDescrLuogoEmittente().toUpperCase());
		lStampa.setTipoUfficio(descrTipoUff);

		if (descrTipoUff != null) {
			if (descrTipoUff.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
			}
		}
		return lStampa;
	}

	/**
	 * Crea la root per il Riferimento Fascicolo Sius
	 * 
	 * @param lRiFaSiusModel
	 * @return
	 */
	private XModel createRoot(RiferimentoFascicoloSiusModel lRiFaSiusModel) {
		XModel lRiFaSius = new XModel();

		String descrTipoUff = lRiFaSiusModel.getDescrUffFascicoloSius();

		lRiFaSius.setUfficio(descrTipoUff);
		lRiFaSius.setTipoUfficio(descrTipoUff);

		return lRiFaSius;
	}

	private FascicoloGPModel RicercaFascicolo(Connection aConn, BigDecimal aKeyFascicolo) throws F3BException {
		FascicoloGPModel lFasModel = null;
		FascicoloGPSqlDAO lFasDao = null;
		try {
			lFasDao = new FascicoloGPSqlDAO(aConn);
			lFasDao.ricercaFascicoloByKey(aKeyFascicolo);
			lFasModel = (FascicoloGPModel) lFasDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioneJMSController.RicercaFascicolo: " + daoEx);
		} finally {
			cleanup(lFasDao);
		}
		return lFasModel;
	}

//	private SoggettoModel RicercaSoggetto(Connection aConn, BigDecimal aKeySoggetto) throws F3BException {
//		SoggettoModel lSogModel = null;
//		SoggettoSqlDAO lSogSqlDao = null;
//		try {
//			// SOGGETTO SIUS ( IL SOGGETTO IN REALTA' E' QUELLO LEGATO AL FASCICOLO SIEP )
//			lSogSqlDao = new SoggettoSqlDAO(aConn);
//			lSogSqlDao.ricercaSoggettoByKey(aKeySoggetto);
//			lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
//		} catch (DAOException daoEx) {
//			throw new F3BException("TrasmissioneJMSController.RicercaSoggetto: " + daoEx);
//		} catch (SQLException sqe) {
//			throw new F3BException("TrasmissioneJMSController.RicercaSoggetto: " + sqe);
//		} finally {
//			cleanup(lSogSqlDao);
//		}
//		return lSogModel;
//	}

	/*
	 * STUB 25/11/2004 Esclusione di dati non utilizzati in presa in carico. private UdienzaModel
	 * RicercaUdienza( Connection aConn, BigDecimal aKeyUdienza) throws F3BException { UdienzaModel lUdienza =
	 * null; UdienzaSqlDAO lUdiDao = null; try { lUdiDao = new UdienzaSqlDAO(aConn);
	 * lUdiDao.ricercaUdienzaByKey(aKeyUdienza); lUdienza = (UdienzaModel)lUdiDao.getModelByKey(); } catch
	 * (DAOException daoEx) { throw new
	 * F3BException("TrasmissioneJMSController.RicercaUdienza: " + daoEx); } catch (SQLException sqe) {
	 * throw new
	 * F3BException("TrasmissioneJMSController.RicercaUdienza: " + sqe); } finally { cleanup(lUdiDao); return
	 * lUdienza; } }
	 */

	private DepositoDecretoModel RicercaDecreto(Connection aConn, BigDecimal aKeyEvento) throws F3BException {
		DepositoDecretoModel lDepDecrMod = null;
		DepositoDecretoSqlDAO lDepDecDao = null;
		try {
			// DEPOSITO DECRETO.
			lDepDecDao = new DepositoDecretoSqlDAO(aConn);
			lDepDecDao.ricercaDepositoDecretoByIdEveGenerato(aKeyEvento);
			lDepDecrMod = (DepositoDecretoModel) lDepDecDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioneJMSController.RicercaDecreto: " + daoEx);
		} finally {
			cleanup(lDepDecDao);
		}
		return lDepDecrMod;
	}

	private ArrayList RicercaMotivazioniDecreto(Connection aConn, BigDecimal aKeyDepDec) throws F3BException {
		MotivazioneDecretoSqlDAO lMotDecDao = null;
		ArrayList lMotivazioniDecreti = null;
		try {
			lMotDecDao = new MotivazioneDecretoSqlDAO(aConn);
			lMotDecDao.ricercaMotivazioneDecretoByDepDec(aKeyDepDec);
			// lMotDecDao.ricercaMotivazioneDecretoInammissibilitaByDepDec(aKeyDepDec);
			lMotivazioniDecreti = new ArrayList(lMotDecDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioneJMSController.RicercaMotivazioniDecreto: " + daoEx);
		} finally {
			cleanup(lMotDecDao);
		}
		return lMotivazioniDecreti;
	}

	private ImpugnazioneModel RicercaImpugnazioneByDecreto(Connection aConn, BigDecimal aKeyDepDec)
			throws F3BException {
		ImpugnazioneSqlDAO lImpDao = null;
		ImpugnazioneModel lImpMod = null;
		try {
			lImpDao = new ImpugnazioneSqlDAO(aConn);
			lImpDao.ricercaImpugnazioneByIdDepositoDecreto(aKeyDepDec);
			lImpMod = (ImpugnazioneModel) lImpDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioneJMSController.RicercaImpugnazioneByDecreto: " + daoEx);
		} finally {
			cleanup(lImpDao);
		}
		return lImpMod;
	}

	/**
	 * Preleva i dati dal DB per la trasmissione del decreto
	 * 
	 * @param aKeyEvento
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel getMessageForDecreto(BigDecimal aKeyEvento) throws F3BException {
		Connection lConn = null;

		FascicoloSiepSqlDAO lFasSiepDao = null;
		SentenzaSqlDAO lSentenzaDao = null;
		TenoreSqlDAO lTenDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		MessaggioModel lMessage = null;

		AssistenteGiudiziarioSqlDAO lAssGiuDao = null;
		EspertoSqlDAO lEspertoDao = null;
		MagistratoSqlDAO lMagDao = null;
		UdienzaSqlDAO lUdiDao = null;

		// STUB 21/03/2005
		LicenzaLibanticipataSqlDAO lLibAntSqlDAO = null;
		PeriodoLibanticipataSqlDAO lPerAntSqlDAO = null;

		// /EventoPermessoLicenzaDAO lEvPermLicDAO = null;
		EventoPermessoLicenzaSqlDAO lEvPermLicSqlDAO = null;

		// STUB 07/04/2005.
		AvvocatoFascicoloSiusSqlDAO lAvvDao = null;
		// STUB 12/04/2005.
		DocumentoAllegatoSqlDAO lAllSqlDao = null;

		try {
			lConn = getDBConnection();

			// Cerca L'evento e le informazioni correlate.
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(aKeyEvento);

			if (lEveNot == null || lEveNot.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Errore nei dati:evento mancante!");

			// Lettura del BLOB e suo inserimento nel formato utile al trasferimento (byte[])
			lEveNot.getEvento().setDocPerTrasferimento(
					lCtrlEve.ExGetDocPerTrasferimento(lEveNot.getEvento().getIdEvento()));

			TreeModel lTreeRoot = new TreeModel(createRoot(lEveNot.getEvento()));

			lTreeRoot.add(new TreeModel(lEveNot));

			// STUB 12/04/2005 Aggiunto DOCUMENTO_ALLEGATO
			lAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			lAllSqlDao.ricercaDocumentoAllegatoByIdEvento(lEveNot.getEvento().getIdEvento());
			DocumentoAllegatoModel lDAModel = (DocumentoAllegatoModel) lAllSqlDao.getModelByKey();
			lTreeRoot.add(new TreeModel(lDAModel));

			// FASCICOLO SIUS - GENERALE PROCEDIMENTO
			FascicoloGPModel lFasModel = RicercaFascicolo(lConn, lEveNot.getEvento()
					.getFasSiuIdFascicoloSius());

			if (lFasModel == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"TrasmissioneJMSController.getMessageForDecreto: Fascicolo SIUS inesistente con id ->"
								+ lEveNot.getEvento().getFasSiuIdFascicoloSius());

			if (lFasModel.getGeneraleProcedimentoModel() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"TrasmissioneJMSController.getMessageForDecreto: Generale Procedimento inesistente");

			// STUB 25/11/2004 lettura dell'UDIENZA solo per la data da caricare in DATA_CAMERA_CONSIGLIO.
			BigDecimal lKeyUdienza = lFasModel.getGeneraleProcedimentoModel().getUdiIdUdienza();
			if (lKeyUdienza != null) {
				lUdiDao = new UdienzaSqlDAO(lConn);
				lUdiDao.ricercaUdienzaByKey(lKeyUdienza);
				UdienzaModel lUdienza = (UdienzaModel) lUdiDao.getModelByKey();

				if (lUdienza != null && lUdienza.getDataUdienza() != null) {
					lFasModel.getGeneraleProcedimentoModel()
							.setDataCameraConsiglio(lUdienza.getDataUdienza());
				}
			}

			// SOGGETTO SIUS ( Dal 12/10/2010 i Soggetti SIUS e SIEP sono slegati )
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoByKey(lFasModel.getFascicoloSiusModel().getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
			lSogSqlDao.stop(); // 12/10/2010

			lFasModel.getFascicoloSiusModel().setSoggetto(lSogModel);

			lTreeRoot.add(new TreeModel(lFasModel));

			// 12/10/2010 FASCICOLO SIEP - SENTENZA - SOGGETTO
			if (lFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lFasSiepDao = new FascicoloSiepSqlDAO(lConn);
				lFasSiepDao.ricercaFascicoloByKey(lFasModel.getFascicoloSiusModel()
						.getFasSieIdFascicoloSiep());
				FascicoloSiepModel lFasSiep = (FascicoloSiepModel) lFasSiepDao.getModelByKey();
				cleanup(lFasSiepDao);

				lSentenzaDao = new SentenzaSqlDAO(lConn);
				lSentenzaDao.ricercaSentenzaBykey(lFasSiep.getSenIdSentenza());
				SentenzaModel lSentenza = (SentenzaModel) lSentenzaDao.getModelByKey();
				cleanup(lSentenzaDao);

				// 12/10/2010 Inserito Soggetto SIEP.
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(lFasSiep.getSogIdSoggetto());
				lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
				lFasSiep.setSoggetto(lSogModel);

				lTreeRoot.add(new TreeModel(lFasSiep));
				lTreeRoot.add(new TreeModel(lSentenza));
			}

			// RESIDENZA FASCICOLO SIUS
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			lResSqlDao.ricercaResidenzaByFascicoloSius(lFasModel.getFascicoloSiusModel()
					.getFasSiuIdFascicoloSius());
			ResidenzaModel lResMod = (ResidenzaModel) lResSqlDao.getModelByKey();

			if (lResMod != null) {
				ResidenzaFascicoloSiusModel lResFasSiuMod = null;

				lResSqlDao.stop();
				lResSqlDao.ricercaResidenzaByFascicoloSius(lFasModel.getFascicoloSiusModel()
						.getFasSiuIdFascicoloSius());
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					lResFasSiuMod = (ResidenzaFascicoloSiusModel) lResSqlDao.getModelResidenzaFascicoloSius();
				}

				ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
				lResAss.setResidenza(lResMod);
				lResAss.setResidenzaFascicoloSius(lResFasSiuMod);
				lTreeRoot.add(new TreeModel(lResAss));
			}

			// STUB 07/04/2005 AVVOCATI SIUS.
			lAvvDao = new AvvocatoFascicoloSiusSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lFasModel.getFascicoloSiusModel().getIdFascicoloSius());

			List lAvvocati = new ArrayList(lAvvDao.getModels());
			Iterator lItAvv = lAvvocati.iterator();
			while (lItAvv.hasNext()) {
				lTreeRoot.add(new TreeModel((AvvocatoSiusModel) lItAvv.next()));
			}

			// DEPOSITO DECRETO PC
			DepositoDecretoModel lDepDecr = RicercaDecreto(lConn, aKeyEvento);
			if (lDepDecr != null) {
				// STUB: occorre inserire la data come sotto ??
				// lDepOrd.setAnnoDataCameraConsiglio(DateUtils.getDateToString(lDepOrd.getDataCameraConsiglio(),"yyyy"));
				// lDepOrd.setGiornoDataCameraConsiglio(DateUtils.getDateToString(lDepOrd.getDataCameraConsiglio(),"dd"));
				// lDepOrd.setMeseDataCameraConsiglio(DateUtils.getDateToString(lDepOrd.getDataCameraConsiglio(),"MMMM"));

				// STUB 21/03/2005 ARRAY LIST LICENZE LIBERAZIONE ANTICIPATA E PERIODI 21/03/2005.
				lLibAntSqlDAO = new LicenzaLibanticipataSqlDAO(lConn);

				Vector lLicenze = null;
				lLibAntSqlDAO.ricercaLicenzaLibanticipataByEve(lDepDecr.getIdEventoGenerato());
				lLicenze = new Vector(lLibAntSqlDAO.getModels());

				Iterator it = lLicenze.iterator();
				ArrayList lPeriodi = null;
				ArrayList lEventiPermLic = null;
				while (it.hasNext()) {
					LicenzaLibAnticipataModel lLicenzaMod = (LicenzaLibAnticipataModel) it.next();

					// Periodi per la LicenzaLibAnticipata Corrente.
					LicenzaPeriodiLibAnticipataModel aModel = new LicenzaPeriodiLibAnticipataModel();
					lPerAntSqlDAO = new PeriodoLibanticipataSqlDAO(lConn);
					// /lEvPermLicDAO = new EventoPermessoLicenzaDAO(lConn);
					lEvPermLicSqlDAO = new EventoPermessoLicenzaSqlDAO(lConn);

					lPeriodi = null;
					if (lLicenzaMod != null) {
						aModel.setLicenza(lLicenzaMod);
						lPerAntSqlDAO.ricercaPeriodoLibanticipataByLic(lLicenzaMod
								.getIdLicenzaLibanticipata());
						lPeriodi = new ArrayList(lPerAntSqlDAO.getModels());
						aModel.setPeriodi((PeriodoLibAnticipataModel[]) lPeriodi
								.toArray(new PeriodoLibAnticipataModel[1]));

						// Recupero dati degli eventi permessi licenze.
						// /lEvPermLicDAO.setCondizioneByIdLic( lLicenzaMod.getIdLicenzaLibanticipata() );
						// /lEventiPermLic = new ArrayList(lEvPermLicDAO.getModels());
						// /aModel.setEventiPermLic((EventoPermessoLicenzaModel[])lEventiPermLic.toArray(new
						// EventoPermessoLicenzaModel[1]));

						// 23/06/2008 Corretto Recupero dati degli eventi permessi licenze.
						lEvPermLicSqlDAO.ricercaEventoPermessoLicenzaByKeyLicLib(lLicenzaMod
								.getIdLicenzaLibanticipata());
						lEventiPermLic = new ArrayList(lEvPermLicSqlDAO.getModels());
						aModel.setEventiPermLic((EventoPermessoLicenzaModel[]) lEventiPermLic
								.toArray(new EventoPermessoLicenzaModel[1]));

					}
					lTreeRoot.add(new TreeModel(aModel));
				}
				lTreeRoot.add(new TreeModel(lDepDecr));

				// MOTIVAZIONE DECRETO
				List lMotivazioniDecreti = RicercaMotivazioniDecreto(lConn, lDepDecr.getIdDepositoDecreto());
				Iterator lItx = lMotivazioniDecreti.iterator();
				while (lItx.hasNext()) {
					lTreeRoot.add(new TreeModel((MotivazioneDecretoModel) lItx.next()));
				}

				// IMPUGNAZIONE
				ImpugnazioneModel lImpMod = RicercaImpugnazioneByDecreto(lConn,
						lDepDecr.getIdDepositoDecreto());
				if (lImpMod != null) {
					lTreeRoot.add(new TreeModel(lImpMod));
				}

				// TENORI
				lTenDao = new TenoreSqlDAO(lConn);
				// lTenDao.ricercaTenoriByGeneraleProcOrderByPeso( lKeyGeneraleProcedimento );
				lTenDao.ricercaTenoriByDecretoOrderByPeso(lDepDecr.getIdDepositoDecreto());

				List lTenori = new ArrayList(lTenDao.getModels());
				Iterator lItxTen = lTenori.iterator();
				while (lItxTen.hasNext()) {
					lTreeRoot.add(new TreeModel((TenoreModel) lItxTen.next()));
				}
			} else
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"TrasmissioneJMSController.getMessageForDecreto : Deposito Decreto inesistente");

			// MISURA ALTERNATIVA
			lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMisAltSqlDao.ricercaMisuraAlternativaByIdEvento(aKeyEvento);
			MisuraAlternativaModel lMisAltMod = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();
			if (lMisAltMod != null) {
				lTreeRoot.add(new TreeModel(lMisAltMod));
			}
			lMessage = new MessaggioModel();
			lMessage.setTreeModel(lTreeRoot);
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioneJMSController.getMessageForDecreto: " + daoEx);
		} catch (F3BException e) {
			throw e;
		} finally {
			cleanup(lTenDao);
			cleanup(lResSqlDao);
			cleanup(lMisAltSqlDao);
			cleanup(lAssGiuDao);
			cleanup(lEspertoDao);
			cleanup(lMagDao);
			// /cleanup(lEvPermLicDAO);
			cleanup(lEvPermLicSqlDAO);
			cleanup(lFasSiepDao);
			cleanup(lSentenzaDao);
			cleanup(lSogSqlDao);
			cleanup(lAvvDao); // STUB 07/04/2005.
			cleanup(lAllSqlDao); // STUB 12/04/2005.

			cleanup(lConn);
		}
		return lMessage;
	}

	/**
	 * Prepara il Messaggio per la Trasmissione del Riferimento Fascicolo SIUS.
	 * 
	 * @param aKeyRiFaSius
	 * @return lMessage
	 * @throws F3BException
	 */
	public MessaggioModel getMessageForRiFaSius(BigDecimal annoFaSius, BigDecimal progrFaSius,
			String codUfficioFaSius, BigDecimal idFascicoloSiep) throws F3BException {
		Connection lConn = null;
		RiferimentoFascicoloSiusDAO lRiFaSiusDao = null;

		RiferimentoFascicoloSiusModel lRiFaSiusModel = null;
		MessaggioModel lMessage = null;

		try {
			lConn = getDBConnection();

			// Si Cerca il Riferimento Fascicolo Sius.
			lRiFaSiusModel = new RiferimentoFascicoloSiusModel();
			lRiFaSiusModel.setAnnoFascicoloSius(annoFaSius);
			lRiFaSiusModel.setProgrFascicoloSius(progrFaSius);
			lRiFaSiusModel.setCodUffFascicoloSius(codUfficioFaSius);
			lRiFaSiusModel.setFasSieIdFascicoloSiep(idFascicoloSiep);
			IRiferimentoFascicoloSius lCtrlRFS = SIUSLookupRemote.getRiferimentoFascicoloSiusRemote();
			Vector riFaSius = lCtrlRFS.ExRicercaRiferimentoFascicoloSius(lRiFaSiusModel);

			if (riFaSius.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"TrasmissioneJMSController.getMessageForRiFaSius: Riferimento Fascicolo Sius inesistente! ");

			lRiFaSiusModel = (RiferimentoFascicoloSiusModel) riFaSius.firstElement();
			if (lRiFaSiusModel == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"TrasmissioneJMSController.getMessageForRiFaSius: Riferimento Fascicolo Sius inesistente! ");

			TreeModel lTreeRoot = new TreeModel(createRoot(lRiFaSiusModel));
			lMessage = new MessaggioModel();
			lMessage.setTreeModel(lTreeRoot);
		} catch (F3BException e) {
			throw e;
		} finally {
			cleanup(lRiFaSiusDao);
			cleanup(lConn);
		}
		return lMessage;
	}

	/**
	 * Prepara il Messaggio per la Trasmissione del Ricorso / Impugnazione.
	 * 
	 * @param lImpKey
	 * @param lEveKey
	 * @param lTipoUfficio
	 * @return lMessage
	 * @throws F3BException
	 */
	public MessaggioModel getMessageForImpugnazione(BigDecimal lEveKey, String lTipoProvvedimento)
			throws F3BException {
		MessaggioModel lMessage = null;

		if (lTipoProvvedimento.compareTo("02") == 0)
			lMessage = this.getMessageForDecreto(lEveKey);

		if (lTipoProvvedimento.compareTo("03") == 0)
			lMessage = this.getMessageForOrdinanza(lEveKey);

		return lMessage;

	}

	/**
	 * Preleva i dati dal DB per la trasmissione della Richiesta al CSSA
	 * 
	 * @param aKeyEvento
	 * @return MessaggioModel
	 * @throws F3BException
	 */
	public MessaggioModel getMessageForRichiesta(BigDecimal aKeyEvento) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getMessageForRichiesta: inizio");

		Connection lConn = null;

		FascicoloSiepSqlDAO lFasSiepDao = null;
		SentenzaSqlDAO lSentenzaDao = null;
		FascicoloGPSqlDAO lFasDao = null;
		UdienzaSqlDAO lUdiDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		AvvocatoFascicoloSiusSqlDAO lAvvDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		RiferimentoFascicoloSiepSqlDAO lRFSSqlDao = null;

		MessaggioModel lMessage = null;

		try {
			lConn = getDBConnection();

			/***********************************************
			 * Cerca L'evento e le informazioni correlate ** --AVVOCATO_FASCICOLO_SIUS --EVENTO --NOTIFICA
			 * --AUTORITA_ESTERNA
			 **********************************************/
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(aKeyEvento);

			if (lEveNot == null || lEveNot.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Errore nei dati:evento mancante!");

			// Lettura del BLOB e suo inserimento nel formato utile al trasferimento (byte[])
			lEveNot.getEvento().setDocPerTrasferimento(
					lCtrlEve.ExGetDocPerTrasferimento(lEveNot.getEvento().getIdEvento()));
			// Creazione della Root del TreeModel
			TreeModel lTreeRoot = new TreeModel(createRoot(lEveNot.getEvento()));
			// Aggiunta Evento
			lTreeRoot.add(new TreeModel(lEveNot));

			// FASCICOLO SIUS - GENERALE PROCEDIMENTO
			BigDecimal lKeyFascicolo = lEveNot.getEvento().getFasSiuIdFascicoloSius();

			lFasDao = new FascicoloGPSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloGPModel lFasModel = (FascicoloGPModel) lFasDao.getModelByKey();

			// Lettura dell'UDIENZA solo per la data da caricare in DATA_CAMERA_CONSIGLIO.
			BigDecimal lKeyUdienza = lFasModel.getGeneraleProcedimentoModel().getUdiIdUdienza();
			if (lKeyUdienza != null) {
				lUdiDao = new UdienzaSqlDAO(lConn);
				lUdiDao.ricercaUdienzaByKey(lKeyUdienza);
				UdienzaModel lUdienza = (UdienzaModel) lUdiDao.getModelByKey();

				if (lUdienza != null && lUdienza.getDataUdienza() != null) {
					lFasModel.getGeneraleProcedimentoModel()
							.setDataCameraConsiglio(lUdienza.getDataUdienza());
				}
			}

			// SOGGETTO SIUS ( Dal 12/10/2010 i Soggetti SIUS e SIEP sono slegati )
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoByKey(lFasModel.getFascicoloSiusModel().getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
			lSogSqlDao.stop(); // 12/10/2010

			lFasModel.getFascicoloSiusModel().setSoggetto(lSogModel);

			lTreeRoot.add(new TreeModel(lSogModel));
			lTreeRoot.add(new TreeModel(lFasModel));

			// 12/10/2010 FASCICOLO SIEP - SENTENZA - SOGGETTO
			if (lFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lFasSiepDao = new FascicoloSiepSqlDAO(lConn);
				lFasSiepDao.ricercaFascicoloByKey(lFasModel.getFascicoloSiusModel()
						.getFasSieIdFascicoloSiep());
				FascicoloSiepModel lFasSiep = (FascicoloSiepModel) lFasSiepDao.getModelByKey();
				cleanup(lFasSiepDao);

				lSentenzaDao = new SentenzaSqlDAO(lConn);
				lSentenzaDao.ricercaSentenzaBykey(lFasSiep.getSenIdSentenza());
				SentenzaModel lSentenza = (SentenzaModel) lSentenzaDao.getModelByKey();
				cleanup(lSentenzaDao);

				// 12/10/2010 Inserito Soggetto SIEP.
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(lFasSiep.getSogIdSoggetto());
				lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
				lFasSiep.setSoggetto(lSogModel);

				lTreeRoot.add(new TreeModel(lFasSiep));
				lTreeRoot.add(new TreeModel(lSentenza));
			}

			// RESIDENZA FASCICOLO SIUS
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			lResSqlDao.ricercaResidenzaByFascicoloSius(lKeyFascicolo);
			ResidenzaModel lResMod = (ResidenzaModel) lResSqlDao.getModelByKey();

			if (lResMod != null) {
				ResidenzaFascicoloSiusModel lResFasSiuMod = null;

				lResSqlDao.stop();
				lResSqlDao.ricercaResidenzaByFascicoloSius(lKeyFascicolo);
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					lResFasSiuMod = (ResidenzaFascicoloSiusModel) lResSqlDao.getModelResidenzaFascicoloSius();
				}
				ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
				lResAss.setResidenza(lResMod);
				lResAss.setResidenzaFascicoloSius(lResFasSiuMod);

				lTreeRoot.add(new TreeModel(lResAss));
			}

			// AVVOCATI SIUS.
			lAvvDao = new AvvocatoFascicoloSiusSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);

			List lAvvocati = new ArrayList(lAvvDao.getModels());
			Iterator lItAvv = lAvvocati.iterator();
			while (lItAvv.hasNext()) {
				lTreeRoot.add(new TreeModel((AvvocatoSiusModel) lItAvv.next()));
			}

			// RIFERIMENTI_FASCICOLI_SIEP.
			lRFSSqlDao = new RiferimentoFascicoloSiepSqlDAO(lConn);
			lRFSSqlDao.ricercaRiferimentoFascicoloSiepByIdFasSius(lKeyFascicolo);

			List lRifFasSie = new ArrayList(lRFSSqlDao.getModels());
			Iterator lItRFS = lRifFasSie.iterator();
			while (lItRFS.hasNext()) {
				lTreeRoot.add(new TreeModel((RiferimentoFascicoloSiepModel) lItRFS.next()));
			}

			// LUOGO_DETENZIONE.
			lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDetSqlDao.ricercaLuoghiDetenzioneByFascicoloSius(lKeyFascicolo);
			LuogoDetenzioneModel lLDModel = (LuogoDetenzioneModel) lLuoDetSqlDao.getModelByKey();
			if (lLDModel != null)
				lTreeRoot.add(new TreeModel(lLDModel));

			lMessage = new MessaggioModel();
			lMessage.setTreeModel(lTreeRoot);

			ReportGenerator lRep = new ReportGenerator();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Treemodel nel messaggio " + lRep.debugTreeXML(lTreeRoot));

		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioneJMSController.getMessageForRichiesta: " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lUdiDao);
			cleanup(lSogSqlDao);
			cleanup(lResSqlDao);
			cleanup(lFasSiepDao);
			cleanup(lSentenzaDao);
			cleanup(lAvvDao);
			cleanup(lRFSSqlDao);
			cleanup(lLuoDetSqlDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getMessageForRichiesta: fine");

		return lMessage;
	}

	/**
	 * Preleva i dati dal DB per la stampe di Sentenza
	 * 
	 * @param aKeyEvento
	 * @return MessaggioModel
	 * @throws F3BException
	 */
	public MessaggioModel getMessageForSentenza(BigDecimal aKeyEvento) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getMessageForSentenza: inizio");

		Connection lConn = null;

		FascicoloSiepSqlDAO lFasSiepDao = null;
		SentenzaSqlDAO lSentenzaDao = null;
		FascicoloGPSqlDAO lFasDao = null;
		UdienzaSqlDAO lUdiDao = null;
		// DepositoOrdinanzaPcSqlDAO lOrdDao = null;
		DepositoSentenzaSqlDAO lSenDao = null;
		PrescrizioneSqlDAO lPreDao = null;
		TenoreSqlDAO lTenDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		MisuraSicurezzaSqlDAO lMisSicSqlDao = null;
		ImpugnazioneSqlDAO lImpDao = null;

		// inserite 11/01/2005 serena
		LicenzaLibanticipataSqlDAO lLibAntSqlDAO = null;
		PeriodoLibanticipataSqlDAO lPerAntSqlDAO = null;
		// /EventoPermessoLicenzaDAO lEvPermLicDAO = null;
		EventoPermessoLicenzaSqlDAO lEvPermLicSqlDAO = null;
		// STUB 07/04/2005.
		AvvocatoFascicoloSiusSqlDAO lAvvDao = null;
		// STUB 12/04/2005.
		DocumentoAllegatoSqlDAO lAllSqlDao = null;
		// STUB 14/04/2005.
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		RiferimentoFascicoloSiepSqlDAO lRFSSqlDao = null;

		// 21/02/2008
		EsecuzioneSanzioneSostitutivaSqlDAO lESSSqlDao = null;
		PeriodoAltraSanzioneSqlDAO lPASSqlDao = null;
		ScambioSanzioneSqlDAO lSSSqlDao = null;
		// 23/03/2009
		RichiestaConversioneSqlDAO lRCSqlDao = null;

		MessaggioModel lMessage = null;

		try {
			lConn = getDBConnection();

			/**
			 * Cerca L'evento e le informazioni correlate --AVVOCATO_FASCICOLO_SIUS --EVENTO --NOTIFICA
			 * --AUTORITA_ESTERNA
			 */
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(aKeyEvento);

			if (lEveNot == null || lEveNot.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Errore nei dati:evento mancante!");

			// Lettura del BLOB e suo inserimento nel formato utile al trasferimento (byte[])
			lEveNot.getEvento().setDocPerTrasferimento(
					lCtrlEve.ExGetDocPerTrasferimento(lEveNot.getEvento().getIdEvento()));
			// Creazione della Root del TreeModel
			TreeModel lTreeRoot = new TreeModel(createRoot(lEveNot.getEvento()));
			// Aggiunta Evento
			lTreeRoot.add(new TreeModel(lEveNot));

			// Aggiunto DOCUMENTO_ALLEGATO
			lAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			lAllSqlDao.ricercaDocumentoAllegatoByIdEvento(lEveNot.getEvento().getIdEvento());
			DocumentoAllegatoModel lDAModel = (DocumentoAllegatoModel) lAllSqlDao.getModelByKey();

			// Lettura del BLOB e suo inserimento nel formato utile al trasferimento (byte[])
			IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote();
			lDAModel.setDocPerTrasferimento(lCtrlDA.ExGetDocPerTrasferimento(lDAModel
					.getIdDocumentoAllegato()));
			// lDAModel.setDocPerTrasferimento(lDAModel.getDocBlobOut().toByteArray()) ;

			lTreeRoot.add(new TreeModel(lDAModel));

			// FASCICOLO SIUS - GENERALE PROCEDIMENTO
			BigDecimal lKeyFascicolo = lEveNot.getEvento().getFasSiuIdFascicoloSius();

			lFasDao = new FascicoloGPSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloGPModel lFasModel = (FascicoloGPModel) lFasDao.getModelByKey();

			// lettura dell'UDIENZA solo per la data da caricare in DATA_CAMERA_CONSIGLIO.
			BigDecimal lKeyUdienza = lFasModel.getGeneraleProcedimentoModel().getUdiIdUdienza();
			if (lKeyUdienza != null) {
				lUdiDao = new UdienzaSqlDAO(lConn);
				lUdiDao.ricercaUdienzaByKey(lKeyUdienza);
				UdienzaModel lUdienza = (UdienzaModel) lUdiDao.getModelByKey();

				if (lUdienza != null && lUdienza.getDataUdienza() != null) {
					lFasModel.getGeneraleProcedimentoModel()
							.setDataCameraConsiglio(lUdienza.getDataUdienza());
				}
			}

			// SOGGETTO SIUS ( Dal 12/10/2010 i Soggetti SIUS e SIEP sono slegati )
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoByKey(lFasModel.getFascicoloSiusModel().getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
			lSogSqlDao.stop();

			lFasModel.getFascicoloSiusModel().setSoggetto(lSogModel);

			lTreeRoot.add(new TreeModel(lFasModel));

			// 12/10/2010 FASCICOLO SIEP - SENTENZA - SOGGETTO
			if (lFasModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lFasSiepDao = new FascicoloSiepSqlDAO(lConn);
				lFasSiepDao.ricercaFascicoloByKey(lFasModel.getFascicoloSiusModel()
						.getFasSieIdFascicoloSiep());
				FascicoloSiepModel lFasSiep = (FascicoloSiepModel) lFasSiepDao.getModelByKey();
				cleanup(lFasSiepDao);

				lSentenzaDao = new SentenzaSqlDAO(lConn);
				lSentenzaDao.ricercaSentenzaBykey(lFasSiep.getSenIdSentenza());
				SentenzaModel lSentenza = (SentenzaModel) lSentenzaDao.getModelByKey();
				cleanup(lSentenzaDao);

				// Inserito Soggetto SIEP.
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(lFasSiep.getSogIdSoggetto());
				lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
				lFasSiep.setSoggetto(lSogModel);

				lTreeRoot.add(new TreeModel(lFasSiep));
				lTreeRoot.add(new TreeModel(lSentenza));
			}

			// RESIDENZA FASCICOLO SIUS
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			lResSqlDao.ricercaResidenzaByFascicoloSius(lKeyFascicolo);
			ResidenzaModel lResMod = (ResidenzaModel) lResSqlDao.getModelByKey();

			if (lResMod != null) {
				ResidenzaFascicoloSiusModel lResFasSiuMod = null;

				lResSqlDao.stop();
				lResSqlDao.ricercaResidenzaByFascicoloSius(lKeyFascicolo);
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					lResFasSiuMod = (ResidenzaFascicoloSiusModel) lResSqlDao.getModelResidenzaFascicoloSius();
				}
				ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
				lResAss.setResidenza(lResMod);
				lResAss.setResidenzaFascicoloSius(lResFasSiuMod);

				lTreeRoot.add(new TreeModel(lResAss));
			}

			// AVVOCATI SIUS.
			lAvvDao = new AvvocatoFascicoloSiusSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);

			List lAvvocati = new ArrayList(lAvvDao.getModels());
			Iterator lItAvv = lAvvocati.iterator();
			while (lItAvv.hasNext()) {
				lTreeRoot.add(new TreeModel((AvvocatoSiusModel) lItAvv.next()));
			}

			// RIFERIMENTI_FASCICOLI_SIEP.
			lRFSSqlDao = new RiferimentoFascicoloSiepSqlDAO(lConn);
			lRFSSqlDao.ricercaRiferimentoFascicoloSiepByIdFasSius(lKeyFascicolo);

			List lRifFasSie = new ArrayList(lRFSSqlDao.getModels());
			Iterator lItRFS = lRifFasSie.iterator();
			while (lItRFS.hasNext()) {
				lTreeRoot.add(new TreeModel((RiferimentoFascicoloSiepModel) lItRFS.next()));
			}

			// LUOGO_DETENZIONE.
			lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDetSqlDao.ricercaLuoghiDetenzioneByFascicoloSius(lKeyFascicolo);
			LuogoDetenzioneModel lLDModel = (LuogoDetenzioneModel) lLuoDetSqlDao.getModelByKey();
			if (lLDModel != null)
				lTreeRoot.add(new TreeModel(lLDModel));

			// DEPOSITO ORDINANZA PC
			// lOrdDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			// lOrdDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aKeyEvento);
			// DepositoOrdinanzaPcModel lDepOrd = (DepositoOrdinanzaPcModel)lOrdDao.getModelByKey();
			lSenDao = new DepositoSentenzaSqlDAO(lConn);
			lSenDao.ricercaDepositoSentenzaByIdEveGenerato(aKeyEvento);
			DepositoSentenzaModel lDepSen = (DepositoSentenzaModel) lSenDao.getModelByKey();

			if (lDepSen != null) {
				// lDepOrd.setAnnoDataCameraConsiglio(DateUtils.getDateToString(lDepOrd.getDataCameraConsiglio(),"yyyy"));
				// lDepOrd.setGiornoDataCameraConsiglio(DateUtils.getDateToString(lDepOrd.getDataCameraConsiglio(),"dd"));
				// lDepOrd.setMeseDataCameraConsiglio(DateUtils.getDateToString(lDepOrd.getDataCameraConsiglio(),"MMMM"));

				// LICENZA LIBERAZIONE ANTICIPATA E PERIODI inserito 11/01/2005
				/*
				 * lLibAntSqlDAO = new LicenzaLibanticipataSqlDAO(lConn);
				 * 
				 * lLibAntSqlDAO.ricercaLicenzaLibanticipataByEve(lDepOrd.getIdEventoGenerato());
				 * LicenzaLibAnticipataModel lLicenzaMod =
				 * (LicenzaLibAnticipataModel)lLibAntSqlDAO.getModelByKey();
				 * 
				 * LicenzaPeriodiLibAnticipataModel aModel = new LicenzaPeriodiLibAnticipataModel();
				 * lPerAntSqlDAO = new PeriodoLibanticipataSqlDAO(lConn);
				 * 
				 * ArrayList lPeriodi = null; ArrayList lEventiPermLic = null; // 23/08/2006 if(lLicenzaMod!=
				 * null) { aModel.setLicenza(lLicenzaMod);
				 * lPerAntSqlDAO.ricercaPeriodoLibanticipataByLic(lLicenzaMod.getIdLicenzaLibanticipata());
				 * lPeriodi = new ArrayList(lPerAntSqlDAO.getModels());
				 * aModel.setPeriodi((PeriodoLibAnticipataModel[])lPeriodi.toArray(new
				 * PeriodoLibAnticipataModel[1]) ); } lDepOrd.setLicenzaPeriodiLibAnticipata(aModel);
				 * lTreeRoot.add(new TreeModel(lDepOrd)); lLibAntSqlDAO.stop();
				 * 
				 * // STUB 21/03/2005 ARRAY LIST LICENZE LIBERAZIONE ANTICIPATA E PERIODI 21/03/2005.
				 * lLibAntSqlDAO = new LicenzaLibanticipataSqlDAO(lConn);
				 * 
				 * Vector lLicenze = null;
				 * lLibAntSqlDAO.ricercaLicenzaLibanticipataByEve(lDepOrd.getIdEventoGenerato()); lLicenze =
				 * new Vector(lLibAntSqlDAO.getModels());
				 * 
				 * Iterator it = lLicenze.iterator(); while (it.hasNext()) { lLicenzaMod =
				 * (LicenzaLibAnticipataModel)it.next();
				 * 
				 * // Periodi per la LicenzaLibAnticipata Corrente. aModel = new
				 * LicenzaPeriodiLibAnticipataModel(); lPerAntSqlDAO = new PeriodoLibanticipataSqlDAO(lConn);
				 * lEvPermLicSqlDAO = new EventoPermessoLicenzaSqlDAO(lConn); // 23/06/2008
				 * 
				 * lPeriodi = null; if(lLicenzaMod!= null) { aModel.setLicenza(lLicenzaMod);
				 * lPerAntSqlDAO.ricercaPeriodoLibanticipataByLic(lLicenzaMod.getIdLicenzaLibanticipata());
				 * lPeriodi = new ArrayList(lPerAntSqlDAO.getModels());
				 * aModel.setPeriodi((PeriodoLibAnticipataModel[])lPeriodi.toArray(new
				 * PeriodoLibAnticipataModel[1]) );
				 * 
				 * // Recupero dati degli eventi permessi licenze. ///lEvPermLicDAO.setCondizioneByIdLic(
				 * lLicenzaMod.getIdLicenzaLibanticipata() ); ///lEventiPermLic = new
				 * ArrayList(lEvPermLicDAO.getModels());
				 * ///aModel.setEventiPermLic((EventoPermessoLicenzaModel[])lEventiPermLic.toArray(new
				 * EventoPermessoLicenzaModel[1]));
				 * 
				 * // 23/06/2008 Corretto Recupero dati degli eventi permessi licenze.
				 * lEvPermLicSqlDAO.ricercaEventoPermessoLicenzaByKeyLicLib
				 * (lLicenzaMod.getIdLicenzaLibanticipata()); lEventiPermLic = new
				 * ArrayList(lEvPermLicSqlDAO.getModels()) ;
				 * aModel.setEventiPermLic((EventoPermessoLicenzaModel[])lEventiPermLic.toArray(new
				 * EventoPermessoLicenzaModel[1])); } lTreeRoot.add(new TreeModel(aModel)); }
				 */

				// PRESCRIZIONI
				/*
				 * lPreDao = new PrescrizioneSqlDAO(lConn); //Le prescrizioni sono collegate all'evento e non
				 * più //al deposito ordinanza. lPreDao.ricercaPrescrizioneByIdEve(aKeyEvento);
				 * 
				 * List lPrescrizioni = new ArrayList(lPreDao.getModels()); Iterator lItx =
				 * lPrescrizioni.iterator(); while (lItx.hasNext()) { lTreeRoot.add(new
				 * TreeModel((PrescrizioneModel) lItx.next())); }
				 */

				// IMPUGNAZIONE
				/*
				 * lImpDao = new ImpugnazioneSqlDAO(lConn);
				 * lImpDao.ricercaImpugnazioneByIdDepositoOrdinanza(lDepOrd.getIdDepositoOrdinanzaPc());
				 * ImpugnazioneModel lImpMod = (ImpugnazioneModel)lImpDao.getModelByKey(); if( lImpMod != null
				 * ) { lTreeRoot.add(new TreeModel(lImpMod)); }
				 */

				// TENORI
				lTenDao = new TenoreSqlDAO(lConn);
				// Ricerca Tenori x ID DepositoSentenza
				lTenDao.ricercaTenoriBySentenzaOrderByPeso(lDepSen.getIdDepositoSentenza());
				List lTenori = new ArrayList(lTenDao.getModels());

				if (lTenori.size() == 0) {
					throw new F3BException(F3BException.USER_MESSAGE, "Errore nei dati: tenore mancante!");
				} else {
					Iterator lItxTen = lTenori.iterator();
					while (lItxTen.hasNext()) {
						lTreeRoot.add(new TreeModel((TenoreModel) lItxTen.next()));
					}
				}
			} else
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"TrasmissioneJMSController.getMessageForSentenza : Deposito Sentenza inesistente");

			// MISURA ALTERNATIVA
			/*
			 * lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
			 * lMisAltSqlDao.ricercaMisuraAlternativaByIdEvento(aKeyEvento); MisuraAlternativaModel lMisAltMod
			 * = (MisuraAlternativaModel)lMisAltSqlDao.getModelByKey(); if( lMisAltMod != null ) {
			 * lTreeRoot.add(new TreeModel(lMisAltMod)); }
			 */

			// MISURA SICUREZZA APPLICATA DA TRASFERIRE PER EVENTUALE ESECUZIONE
			/*
			 * if (lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U023")==0 )
			 * { // Anche fattibile su tipo ordinanza MS lMisSicSqlDao = new MisuraSicurezzaSqlDAO(lConn);
			 * MisuraSicurezzaModel lMisSicRic = new MisuraSicurezzaModel();
			 * lMisSicRic.setFasSiuIdFascicoloSius(lKeyFascicolo); lMisSicRic.setEveIdEvento(aKeyEvento); //
			 * Se ID evento allora è l'unica attiva. Altrimenti prendo la prima!
			 * lMisSicSqlDao.ricercaMisuraSicurezzaApplicata(lMisSicRic); MisuraSicurezzaModel lMisSicMod =
			 * (MisuraSicurezzaModel)lMisSicSqlDao.getModelByKey(); if( lMisSicMod == null ) {
			 * lMisSicRic.setEveIdEvento(null); // Se ID evento è nullo prendo la prima!
			 * lMisSicSqlDao.ricercaMisuraSicurezzaApplicata(lMisSicRic); lMisSicMod =
			 * (MisuraSicurezzaModel)lMisSicSqlDao.getModelByKey(); } if( lMisSicMod != null ) {
			 * lTreeRoot.add(new TreeModel(lMisSicMod)); } }
			 */

			// 25/02/2008 ESECUZIONE SANZIONE SOSTITUTIVA.
			/*
			 * FascicoloGPTPModel lFasGPTPModel = new FascicoloGPTPModel(); if
			 * (lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019")==0) {
			 * lESSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
			 * lESSSqlDao.ricercaEsecuzioneSanzioneSostitutivaByIdFascicolo
			 * (lFasModel.getFascicoloSiusModel().getIdFascicoloSius()); EsecuzioneSanzioneSostitutivaModel
			 * lESSMod = (EsecuzioneSanzioneSostitutivaModel) lESSSqlDao.getModelByKey();
			 * lFasGPTPModel.getDatiSiusPerTrasferimento().setESS(lESSMod); }
			 */

			// 25/02/2008 Periodi Altra Sanzione.
			/*
			 * if (lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019")==0)
			 * { Vector lPAS = new Vector(); lPASSqlDao = new PeriodoAltraSanzioneSqlDAO(lConn);
			 * lPASSqlDao.ricercaSanzioneSostitutivaByIdFascicolo
			 * (lFasModel.getFascicoloSiusModel().getIdFascicoloSius(), "DESC"); lPAS = new
			 * Vector(lPASSqlDao.getModels()); if ( lPAS.size() > 0 )
			 * lFasGPTPModel.getDatiSiusPerTrasferimento().setPAS(lPAS); }
			 */

			// 25/02/2008 Ricerca di SCAMBIO_SANZIONE per Fascicolo SIUS.
			// 23/03/2009 SCAMBIO_SANZIONE Previsto anche per Conversione Pene Pecuniarie.
			/*
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.warn("FASE di RICERCA Scambio Sanzione"); if
			 * (lFasModel.getGeneraleProcedimentoModel()!=null &&
			 * lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()!=null &&
			 * (lFasModel.getGeneraleProcedimentoModel
			 * ().getCodOggettoProcedimento().compareTo(ICostantiDepositoOrdinanzaPc
			 * .OGG_APPL_SANZ_SOSTITUTIVE)==0 ||
			 * lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento
			 * ().compareTo(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE)==0) ) { lSSSqlDao = new
			 * ScambioSanzioneSqlDAO(lConn); ScambioSanzioneModel lSSMod = new ScambioSanzioneModel(); //
			 * 28/11/2011 Corretto il riferimento al model valorizzato (lFasModel invece di lFasGPTPModel.)
			 * //lSSMod.setChiaveAnnoFascicoloSius(lFasGPTPModel.getFascicoloSiusModel().getChiaveAnno());
			 * //lSSMod.setChiaveProgrFascicoloSius(lFasGPTPModel.getFascicoloSiusModel().getChiaveProgr());
			 * //lSSMod.setCodUfficioSorveglianza(lFasGPTPModel.getFascicoloSiusModel().getChiaveUfficio());
			 * lSSMod.setChiaveAnnoFascicoloSius(lFasModel.getFascicoloSiusModel().getChiaveAnno());
			 * lSSMod.setChiaveProgrFascicoloSius(lFasModel.getFascicoloSiusModel().getChiaveProgr());
			 * lSSMod.setCodUfficioSorveglianza(lFasModel.getFascicoloSiusModel().getChiaveUfficio());
			 * lSSSqlDao.ricercaScambioSanzione(lSSMod); lSSSqlDao.start(); if (lSSSqlDao.next()) { lSSMod =
			 * (ScambioSanzioneModel) lSSSqlDao.getModelByKey();
			 * lFasGPTPModel.getDatiSiusPerTrasferimento().setSS(lSSMod); } }
			 */

			// 23/03/2009 Ricerca di RICHIESTA_CONVERSIONE per Fascicolo SIUS (Conversione Pene Pecuniarie).
			/*
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.warn("FASE di ricerca  RICHIESTA_CONVERSIONE"); if
			 * (lFasModel.getGeneraleProcedimentoModel()!=null &&
			 * lFasModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()!=null &&
			 * lFasModel.getGeneraleProcedimentoModel
			 * ().getCodOggettoProcedimento().compareTo(ICostantiDepositoOrdinanzaPc
			 * .OGG_CONV_PENE_PECUNIARIE)==0) { Vector lRCPP = new Vector(); lRCSqlDao = new
			 * RichiestaConversioneSqlDAO(lConn);
			 * lRCSqlDao.ricercaRichiestaConversioneByIdFasSIUS(lFasModel.getFascicoloSiusModel
			 * ().getIdFascicoloSius() ); lRCPP = new Vector(lRCSqlDao.getModels()); if ( lRCPP.size() > 0 )
			 * lFasGPTPModel.getDatiSiusPerTrasferimento().setRCPP(lRCPP); }
			 * 
			 * if( lFasGPTPModel != null ) { lTreeRoot.add(new TreeModel(lFasGPTPModel)); }
			 */

			lMessage = new MessaggioModel();
			lMessage.setTreeModel(lTreeRoot);

			// ReportGenerator lRep = new ReportGenerator();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug( "Treemodel nel messaggio " + lRep.debugTreeXML(lTreeRoot));

		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioneJMSController.getMessageForSentenza: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TrasmissioneJMSController.getMessageForSentenza: " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lUdiDao);
			cleanup(lSenDao);
			cleanup(lPreDao);
			cleanup(lTenDao);
			cleanup(lSogSqlDao);
			cleanup(lResSqlDao);
			cleanup(lMisAltSqlDao);
			cleanup(lMisSicSqlDao);
			cleanup(lImpDao);
			cleanup(lFasSiepDao);
			cleanup(lSentenzaDao);
			cleanup(lLibAntSqlDAO);
			cleanup(lPerAntSqlDAO);
			cleanup(lAvvDao); // STUB 07/04/2005.
			cleanup(lAllSqlDao); // STUB 12/04/2005.
			cleanup(lESSSqlDao);
			cleanup(lPASSqlDao);
			cleanup(lSSSqlDao);
			// /cleanup(lEvPermLicDAO);
			cleanup(lEvPermLicSqlDAO);
			cleanup(lRCSqlDao); // 23/03/2009
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getMessageForSentenza: fine");

		return lMessage;
	}

	/**
	 * Prepara il Messaggio per la Trasmissione dell'Opposizione/Ricorso.
	 * 
	 * @param aKeyImpugSige
	 * @return lMessage
	 * @throws F3BException
	 */
	public MessaggioModel getMessageForOpposizioneRicorso(BigDecimal aKeyEvento, BigDecimal aKeyImpugSige)
			throws F3BException {
		Connection lConn = null;
		ImpugnazioneSigeDAO lImpugSigeDao = null;
		ImpugnazioneSigeModel lImpugSigeModel = null;
		MessaggioModel lMessage = null;

		try {
			lConn = getDBConnection();

			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(aKeyEvento);

			if (lEveNot == null || lEveNot.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Errore nei dati:evento mancante!");

			// Lettura del BLOB e suo inserimento nel formato utile al trasferimento (byte[])
			lEveNot.getEvento().setDocPerTrasferimento(
					lCtrlEve.ExGetDocPerTrasferimento(lEveNot.getEvento().getIdEvento()));

			// Creazione della Root del TreeModel
			TreeModel lTreeRoot = new TreeModel(createRoot(lEveNot.getEvento()));
			// Aggiunta Evento
			lTreeRoot.add(new TreeModel(lEveNot));

			lImpugSigeModel = new ImpugnazioneSigeModel();
			IImpugnazioneSige iCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
			lImpugSigeModel = iCtrl.ExRicercaImpugnazioneByKey(aKeyImpugSige);

			lTreeRoot.add(new TreeModel(lImpugSigeModel));

			lMessage = new MessaggioModel();
			lMessage.setTreeModel(lTreeRoot);
		} catch (F3BException e) {
			throw e;
		} finally {
			cleanup(lImpugSigeDao);
			cleanup(lConn);
		}
		return lMessage;
	}

}