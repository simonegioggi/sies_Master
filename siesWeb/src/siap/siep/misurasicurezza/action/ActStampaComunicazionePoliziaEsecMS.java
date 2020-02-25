package siap.siep.misurasicurezza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActStampaComunicazionePoliziaEsecMS
 * </p>
 * <p>
 * Description: Stampa Provvedimento di Comunicazione alle forze di polizia,
 * </p>
 * <p>
 * per esecuzione della MIS. SIC.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Intersistemi Italia S.P.A.
 * </p>
 *
 * @author AMBROSINO
 * @version 8.3
 */
@SuppressWarnings("rawtypes")
public class ActStampaComunicazionePoliziaEsecMS extends ActionSiap
		implements ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter("IdEvento");
		// String lCod = getRequestStringParameter("CodMotivo");
		String lMisIdMis = getRequestStringParameter(CAMPO_MIS_ID_MISURA_SICUREZZA);

		String CodPos = "";
		if (!isRequestParameterNullObj(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA))
			CodPos = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		String CodTipoMis = "";
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO))
			CodTipoMis = getRequestStringParameter(CAMPO_COD_TIPO);

		String flagTemplate = "0";

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		// Controllo se trattasi di: a) Fascicolo con Misura nata in sentenza; b) Misura Disposta Fuori
		// Sentenza e/o Misura Provvisoria
		String CodTipoSentenza = "";
		CodTipoSentenza = this.getTipoSentenza();
		//
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		// Controllo se trattasi di Misura trasformata da SIUS, vado quindi a cercare la Mis. Sic precedente
		String lPrecMis = "";
		if (CodTipoMis.compareTo("01") == 0 || // Tipo Misura Detentiva
				CodTipoMis.compareTo("03") == 0 || CodTipoMis.compareTo("04") == 0
				|| CodTipoMis.compareTo("06") == 0 || CodTipoMis.compareTo("08") == 0) {
			if (!lMisIdMis.equals("")) {
				lPrecMis = CercaMisuraPrec(new BigDecimal(lMisIdMis));
			}
		}

		// Controllo se trattasi di Comunicazione di Ordinanza di Proroga Misura su Riesame Pericolosità
		// sociale
		Vector lVecMis = new Vector();
		Boolean lProroga = false;
		// Cerco tutti gli Eventi VALIDATI ordinati per DATA_EMISSIONE Desc
		lVecMis = lCtrl.ExRicercaTuttiEventiValidatiByFascicoloSiepDesc(lFascicoloModel.getIdFascicoloSiep());
		if (lVecMis.size() > 0) {
			EventoModel EveProroMod = (EventoModel) lVecMis.get(0);

			if (EveProroMod.getCodMotivo().compareTo("1137") == 0
					|| EveProroMod.getCodMotivo().compareTo("2440") == 0
					|| EveProroMod.getCodMotivo().compareTo("2441") == 0) {
				lProroga = true;
			}
		}

		// Scelta del tipo di stampa da innescare
		if (lProroga) {
			// Misura nate in sentenza
			// lEveMod.setNomeTemplate("SIEP_MS_935"); //SIEP_MS_PROROGA
			flagTemplate = "E";
		} else if (CodPos.compareTo("07") == 0 || // Soggetto Libero
				CodPos.compareTo("10") == 0 || CodPos.compareTo("46") == 0) {
			if (CodTipoMis.compareTo("05") == 0 // Tipo Misura Non Detentiva Espulsione
					|| CodTipoMis.compareTo("07") == 0
					// 20191122 [SG]: aggiunti 4 codici (post collaudo 11.3)
					|| CodTipoMis.compareTo("20") == 0 || CodTipoMis.compareTo("21") == 0
					|| CodTipoMis.compareTo("22") == 0 || CodTipoMis.compareTo("23") == 0) {
				if (CodTipoSentenza.compareTo("01") == 0) {
					// Misura nate in sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_992_1"); //SIEP_MS_COMUESP_LIB
					flagTemplate = "1";
				} else {
					// Misura provvisorie e/o disposte Fuori Sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_954"); //SIEP_MS_FS_COMUESP_LIB --> OK fatta
					flagTemplate = "C";
				}
			} else if (CodTipoMis.compareTo("02") == 0 || // Tipo Misura Non Detentiva
					CodTipoMis.compareTo("09") == 0 || CodTipoMis.compareTo("10") == 0
					|| CodTipoMis.compareTo("13") == 0 || // Tipo Misura Non Detentiva
					CodTipoMis.compareTo("14") == 0 || CodTipoMis.compareTo("15") == 0
					|| CodTipoMis.compareTo("16") == 0) {
				if (CodTipoSentenza.compareTo("01") == 0) {
					// Misura nata in Sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_999_1"); //SIEP_MS_COMUPOL_LIB_NOD
					flagTemplate = "0";
				} else {
					// Misura provvisorie e/o disposte Fuori Sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_956"); //SIEP_MS_FS_COMUPOL_LIB_NOD --> OK fatta
					flagTemplate = "B";
				}
			} else if (CodTipoMis.compareTo("01") == 0 || // Tipo Misura Detentiva
					CodTipoMis.compareTo("03") == 0 || CodTipoMis.compareTo("04") == 0
					|| CodTipoMis.compareTo("06") == 0 || CodTipoMis.compareTo("08") == 0) {
				if (lPrecMis.compareTo("02") == 0) {
					if (CodTipoSentenza.compareTo("01") == 0) {
						// misura nata in sentenza
						// lEveMod.setNomeTemplate("SIEP_MS_981_2"); // SIEP_MS_OE_LIB_VIG
						flagTemplate = "6";
					} else {
						// Misura provvisorie e/o disposte Fuori Sentenza
						// lEveMod.setNomeTemplate("SIEP_MS_961"); // SIEP_MS_FS_OE_LIB_VIG --> NON HA SENSO
						// --VUOTO
						flagTemplate = "D";
					}
				} else {
					if (CodTipoSentenza.compareTo("01") == 0) {
						// misura nata in sentenza
						// lEveMod.setNomeTemplate("SIEP_MS_997_2"); // SIEP_MS_COMUPOL_LIB_DET
						flagTemplate = "2";
					} else {
						// Misura provvisorie e/o disposte Fuori Sentenza
						// lEveMod.setNomeTemplate("SIEP_MS_957"); // SIEP_MS_FS_COMUPOL_LIB_DET --> OK fatta
						flagTemplate = "A";
					}
				}
			}
		} else { // Soggetto NON LIBERO
			if (CodTipoMis.compareTo("05") == 0 // Tipo Misura Non Detentiva Espulsione
					|| CodTipoMis.compareTo("07") == 0
					// 20191122 [SG]: aggiunti 4 codici (post collaudo 11.3)
					|| CodTipoMis.compareTo("20") == 0 || CodTipoMis.compareTo("21") == 0
					|| CodTipoMis.compareTo("22") == 0 || CodTipoMis.compareTo("23") == 0) {
				if (CodTipoSentenza.compareTo("01") == 0) {
					// misura nata in sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_983_1"); // SIEP_MS_COMUESP_DET
					flagTemplate = "4";
				} else {
					// Misura provvisorie e/o disposte Fuori Sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_958"); // SIEP_MS_FS_COMUESP_DET --> OK, fatta
					flagTemplate = "9";
				}
			} else if (CodTipoMis.compareTo("02") == 0 || // Tipo Misura Non Detentiva
					CodTipoMis.compareTo("09") == 0 || CodTipoMis.compareTo("10") == 0
					|| CodTipoMis.compareTo("13") == 0 || // Tipo Misura Non Detentiva
					CodTipoMis.compareTo("14") == 0 || CodTipoMis.compareTo("15") == 0
					|| CodTipoMis.compareTo("16") == 0) {
				if (CodTipoSentenza.compareTo("01") == 0) {
					// misura nata in sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_991_1"); // SIEP_MS_COMUPOL_DET_NOD
					flagTemplate = "3";
				} else {
					// Misura provvisorie e/o disposte Fuori Sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_960"); // SIEP_MS_FS_COMUPOL_DET_NOD --> OK fatta
					flagTemplate = "8";
				}
			} else if (CodTipoMis.compareTo("01") == 0 || // Tipo Misura Detentiva
					CodTipoMis.compareTo("03") == 0 || CodTipoMis.compareTo("04") == 0
					|| CodTipoMis.compareTo("06") == 0 || CodTipoMis.compareTo("08") == 0) {
				if (lPrecMis.compareTo("02") == 0) {
					if (CodTipoSentenza.compareTo("01") == 0) {
						// misura nata in sentenza
						// lEveMod.setNomeTemplate("SIEP_MS_981_2"); // SIEP_MS_OE_LIB_VIG
						flagTemplate = "6";
					} else {
						// Misura provvisorie e/o disposte Fuori Sentenza
						// lEveMod.setNomeTemplate("SIEP_MS_961"); // SIEP_MS_FS_OE_LIB_VIG --> NON HA SENSO
						// --- VUOTO
						flagTemplate = "D";
					}
				} else {
					if (CodTipoSentenza.compareTo("01") == 0) {
						// misura nata in sentenza
						// lEveMod.setNomeTemplate("SIEP_MS_982_2"); // SIEP_MS_COMU_OE_DET_DET
						flagTemplate = "5";
					} else {
						// Misura provvisorie e/o disposte Fuori Sentenza
						// lEveMod.setNomeTemplate("SIEP_MS_964"); // SIEP_MS_FS_COMU_OE_DET_DET --> OK fatto
						flagTemplate = "7";
					}
				}
			}

		}

		// ========================================
		// Recupero il template se esiste, paolo modifica 28 aprile 2009
		// ========================================

		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("inzio ricerca >>> - flagTemplate = >" + flagTemplate + "<");
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
					lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(),
					lEventoModel.getCodMotivo(), flagTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lTemMod  >>>" + lTemMod);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
		}

		if (lTemMod != null && lTemMod.getIdTemplate() != null && !lTemMod.getIdTemplate().equals("")) {
			lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lTemMod.getIdTemplate  >>>" + lTemMod.getIdTemplate());
		} else {
			lEveMod.setNomeTemplate("VUOTO");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lTemMod.getIdTemplate Vuoto  >>>VUOTO<<<<");
		}

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

	private String CercaMisuraPrec(BigDecimal aIdMisuraColl) throws F3BException {

		String lMis = "";
		// Ricerca eventuale Misura sicurezza trasformata da SIUS
		MisuraSicurezzaModel lMisSicMod = null;
		Vector lVecColl = new Vector();
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		// lMisSicMod = lMisCtrl.ExRicercaMisuraSicurezzaByKey(aIdMisuraColl);
		lVecColl = lMisCtrl.ExRicercaMisuraSicurezzaByMisIdMisuraSicurezza(aIdMisuraColl);

		if (lVecColl != null && lVecColl.size() > 0) {
			lMisSicMod = (MisuraSicurezzaModel) lVecColl.get(lVecColl.size() - 1);
			if (lMisSicMod != null && lMisSicMod.getIdMisuraSicurezza() != null
					&& lMisSicMod.getCodTipo() != null) {
				lMis = lMisSicMod.getCodTipo();
			}
		}

		return lMis;
	}

} // Chiude classe ActStampaComunicazionePoliziaEsecMS