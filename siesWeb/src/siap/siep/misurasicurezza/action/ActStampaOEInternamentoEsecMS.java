package siap.siep.misurasicurezza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

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
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActStampaOEInternamentoEsecMS
 * </p>
 * <p>
 * Description: Stampa dell'Ordine di esecuzione alle forze di polizia
 * </p>
 * <p>
 * per esecuzione della MIS. SIC.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROSINO
 * @version 8.3
 */
@SuppressWarnings("rawtypes")
public class ActStampaOEInternamentoEsecMS extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter("IdEvento");
//		String lCod = getRequestStringParameter("CodMotivo");
		String lMisIdMis = getRequestStringParameter(CAMPO_MIS_ID_MISURA_SICUREZZA);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --XX-- lId evento  = "+lId);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --XX-- lCod mot  = "+lCod);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --XX-- lMisIdMis  = "+lMisIdMis);

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

		// Diamo per scontato che questa Funzione si applica alle MISURE di Tipo DETENTIVO
		// altrimenti è ERRORE
		//
		String lPrecMis = "";
		if (CodTipoMis.compareTo("01") == 0
				|| // Tipo Misura Detentiva
				CodTipoMis.compareTo("03") == 0 || CodTipoMis.compareTo("04") == 0
				|| CodTipoMis.compareTo("06") == 0 || CodTipoMis.compareTo("08") == 0) {
			if (!lMisIdMis.equals("")) {
				// Cerco Misura Precedente
				lPrecMis = CercaMisuraPrec(new BigDecimal(lMisIdMis));
			}

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" --XX-- lPrecMis  = "+lPrecMis);
			if (lPrecMis.compareTo("02") == 0) // Libertà Vigilata
			{
				if (CodTipoSentenza.compareTo("01") == 0) {
					// Misura nata in sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_981"); // SIEP_MS_OE_LIB_VIG
					flagTemplate = "2";
				} else {
					// Misura provvisoria e/o disposta Fuori Sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_963"); // SIEP_MS_FS_OE_LIB_VIG --> NON HA SENSO ---
					// VUOTO
					flagTemplate = "3";
				}
			} else if (CodPos.compareTo("07") == 0 || // Soggetto Libero
					CodPos.compareTo("10") == 0 || CodPos.compareTo("16") == 0 || CodPos.compareTo("46") == 0) {
				if (CodTipoSentenza.compareTo("01") == 0) {
					// Misura nata in sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_997_1"); //SIEP_MS_COMUPOL_LIB_DET
					flagTemplate = "0";
				} else {
					// Misura provvisoria e/o disposta Fuori Sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_950"); //SIEP_MS_FS_COMUPOL_LIB_DET --> OK fatta
					flagTemplate = "4";
				}

			} else // Soggetto Detenuto (o NON LIBERO)
			{
				if (CodTipoSentenza.compareTo("01") == 0) {
					// Misura nata in sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_982"); //SIEP_MS_COMU_OE_DET_DET
					flagTemplate = "1";
				} else {
					// Misura provvisoria e/o disposta Fuori Sentenza
					// lEveMod.setNomeTemplate("SIEP_MS_965"); //SIEP_MS_FS_COMU_OE_DET_DET --> OK fatta
					flagTemplate = "5";
				}
			}
		} else // Tipo misura NON DETENTIVA quindi ERRORE
		{
			lEveMod.setNomeTemplate("VUOTO"); // Errore
			flagTemplate = "9";
		}
		// --

		// ===============================================================
		// Recupero il template se esiste, paolo modifica 28 aprile 2009
		// ================================================================

		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("inzio ricerca >>>");
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

	} // Chiude process

	private String CercaMisuraPrec(BigDecimal aIdMisuraColl) throws F3BException {

		String lMis = "";
		// Ricerca eventuale Misura sicurezza trasformata da SIUS
		MisuraSicurezzaModel lMisSicMod = null;
		Vector lVecColl = new Vector();
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		// lMisSicMod = lMisCtrl.ExRicercaMisuraSicurezzaByKey(aIdMisuraColl) ;
		lVecColl = lMisCtrl.ExRicercaMisuraSicurezzaByMisIdMisuraSicurezza(aIdMisuraColl);

		if (lVecColl != null && lVecColl.size() > 0) {
			lMisSicMod = (MisuraSicurezzaModel) lVecColl.get(lVecColl.size() - 1);
			if (lMisSicMod != null && lMisSicMod.getIdMisuraSicurezza() != null
					&& lMisSicMod.getCodTipo() != null) {
				if (lMisSicMod.getCodTipo().compareTo("02") == 0)
					lMis = lMisSicMod.getCodTipo();
			}
		}

		return lMis;
	}

} // Chiude Classe