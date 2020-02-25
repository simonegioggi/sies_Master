package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;

/**
 * Azione per la insert delle notifiche di tutte le Action di modifica dei provvedimenti delle Misure
 * Sicurezza.
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
public class ActNotificheMS extends ActionSiap implements ICostantiMisuraAlternativa {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected NotificaModel[] setNotificheMS(Date lDataTrasmissione, String lCodMotivo) throws F3BException {

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		// Notifiche ---------> PRIMA AUTORITA' ESTERNA
		ArrayList lNotificheArray = new ArrayList();

		String lSedeDestinatario_E = null;
		String lDestinatario_E = null;
		String lNote_E = null;

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			lDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			lSedeDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
		}

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
			lNote_E = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

		if (lDestinatario_E != null ||  lCodMotivo.compareTo("1132") == 0) {
			NotificaModel lNotMod = new NotificaModel();

			// OE per Esecuzione e OS per Liberazione mettono 'E' come TIPO_NOTIFICA
			if (lCodMotivo.compareTo("1128") == 0 // O.E. per Internamento
					|| lCodMotivo.compareTo("1129") == 0 // O.S. per Liberazione
					// MEV_39: aggiunto motivo per OL differimento MS)
					|| lCodMotivo.compareTo("1132") == 0)
				lNotMod.setCodTipoNotifica("E");
			else
				lNotMod.setCodTipoNotifica("AA");

			lNotMod.setDataInvio(lDataTrasmissione);
			lNotMod.setCodEsito("-");
			lNotMod.setNote(lNote_E);

			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setCodiceOperatoreAggiornamento(lCodiceOperatore);
			lNotMod.setDataAggiornamento(DateUtils.getSysDate());
			lNotMod.setCodUfficioAggiornamento(lCodiceUfficio);

			if (lDestinatario_E != null ) {
				AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
				lAutMod.setCodTipoAutorita(lDestinatario_E);
				ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
				lAutMod.setCodSede(lComModel.getCodComune());
	
				lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
				lAutMod.setDataInserimento(DateUtils.getSysDate());
				lAutMod.setCodUfficioInserimento(lCodiceUfficio);
				lAutMod.setCodOperatoreAggiornamento(lCodiceOperatore);
				lAutMod.setCodUfficioAggiornamento(lCodiceUfficio);
				lAutMod.setDataAggiornamento(DateUtils.getSysDate());
				lNotMod.setAutoritaEsterna(lAutMod);
			}
			
//			// MEV_39: aggiunto motivo per OL differimento MS)
//			if(lCodMotivo.compareTo("1132") == 0){
//				if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)){				
//					lNotMod.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
//				}
//			}else{
//				lNotMod.setIstDetIdIstitutoDetenzione("");
//			}
			
			lNotificheArray.add(lNotMod);
		}

		// Notifiche ---------> ALTRA AUTORITA' ESTERNA (variabili usate in
		// 'ActModificaComunicazioneOrdineConsegnaMS.java')
		String lSedeDestinatario_Altro = null;
		String lTipoDestinatario_Altro = null;
		String lNote_Altro = null;

		if (!isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA)
				&& !getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA)
						.equals("-")) {
			lTipoDestinatario_Altro = getRequestStringParameter(
					ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA);
			lSedeDestinatario_Altro = getRequestStringParameter(
					ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA);
		}

		if (!isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_NOTE_ALTRA_AUTORITA))
			lNote_Altro = getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_NOTE_ALTRA_AUTORITA);

		if (lTipoDestinatario_Altro != null) {
			NotificaModel lNotModC = new NotificaModel();
			lNotModC.setCodTipoNotifica("AA");
			lNotModC.setDataInvio(lDataTrasmissione);
			lNotModC.setCodEsito("-");
			lNotModC.setNote(lNote_Altro);

			lNotModC.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModC.setDataInserimento(DateUtils.getSysDate());
			lNotModC.setCodUfficioInserimento(lCodiceUfficio);
			lNotModC.setCodiceOperatoreAggiornamento(lCodiceOperatore);
			lNotModC.setDataAggiornamento(DateUtils.getSysDate());
			lNotModC.setCodUfficioAggiornamento(lCodiceUfficio);

			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
			lAutMod.setCodTipoAutorita(lTipoDestinatario_Altro);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_Altro));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setCodOperatoreAggiornamento(lCodiceOperatore);
			lAutMod.setCodUfficioAggiornamento(lCodiceUfficio);
			lAutMod.setDataAggiornamento(DateUtils.getSysDate());
			lNotModC.setIstDetIdIstitutoDetenzione("");

			lNotModC.setAutoritaEsterna(lAutMod);
			lNotificheArray.add(lNotModC);
		}

		// Notifiche ---------> ALTRA AUTORITA' - Destinatari Giudici Esecuzione
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-")) {
			String lTipoGe = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C); // per
																											// es.
																											// TRIB
			String lComuneGe = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C); // per
																										// es.
																										// BARI
			String lCodUffGe = getCodUfficioByCodTipoUfficioDescrComune(lTipoGe, lComuneGe);

			NotificaModel lNotUffGE = new NotificaModel();

			lNotUffGE.setCodEsito("-");
			lNotUffGE.setCodTipoNotifica("NG");
			lNotUffGE.setDataInvio(lDataTrasmissione);

			lNotUffGE.setCodOperatoreInserimento(lCodiceOperatore);
			lNotUffGE.setDataInserimento(DateUtils.getSysDate());
			lNotUffGE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotUffGE.setCodiceOperatoreAggiornamento(lCodiceOperatore);
			lNotUffGE.setDataAggiornamento(DateUtils.getSysDate());
			lNotUffGE.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_NOTE_C)
					&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_NOTE_C) != null) {
				lNotUffGE.setNote(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_NOTE_C));
			}

			lNotUffGE.setUffCodUfficio(lCodUffGe);

			lNotificheArray.add(lNotUffGE);
		}

		// SETTO Destinatari UDS (Revisione del 24/11/2014)
		if (!isRequestParameterNullObj("tipoUDS") && !getRequestStringParameter("tipoUDS").equals("-")) {
			String lTipoUds = getRequestStringParameter("tipoUDS");
			String lComuneUds = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);
			String lCodUffUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUds, lComuneUds);

			NotificaModel lNotUffUDS = new NotificaModel();

			lNotUffUDS.setCodEsito("-");
			lNotUffUDS.setCodTipoNotifica("MS");
			lNotUffUDS.setDataInvio(lDataTrasmissione);
			lNotUffUDS.setUffCodUfficio(lCodUffUDS);

			lNotUffUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotUffUDS.setDataInserimento(DateUtils.getSysDate());
			lNotUffUDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotUffUDS.setCodiceOperatoreAggiornamento(lCodiceOperatore);
			lNotUffUDS.setDataAggiornamento(DateUtils.getSysDate());
			lNotUffUDS.setCodUfficioAggiornamento(lCodiceUfficio);

			lNotificheArray.add(lNotUffUDS);
		}

		// Notifica Istituto di detenzione  
		String lDestinatario_ist = null;
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals(""))
			lDestinatario_ist = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		if (lDestinatario_ist != null) {
			NotificaModel lNotistMod = new NotificaModel();
			lNotistMod.setCodTipoNotifica("E");
			lNotistMod.setDataInvio(lDataTrasmissione);
			lNotistMod.setCodEsito("-");

			lNotistMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotistMod.setDataInserimento(DateUtils.getSysDate());
			lNotistMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotistMod.setCodiceOperatoreAggiornamento(lCodiceOperatore);
			lNotistMod.setDataAggiornamento(DateUtils.getSysDate());
			lNotistMod.setCodUfficioAggiornamento(lCodiceUfficio);

			lNotistMod.setIstDetIdIstitutoDetenzione(lDestinatario_ist);
			lNotificheArray.add(lNotistMod);
		}

		// ========================================================================
		// Notifiche agli Avvocati (Notifica Difensori)
		// Solo se selezionato in Maschera il check: Notifiche Atti (Difensore - Condannato)
		// ========================================================================
		if (isRequestChecked("Difesa")) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--XX-- Difesa checked");
			String[] lAvvocati = null;
			if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
				lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			}

			String[] lSedeDestinatario_avv = null;
			String[] lDestinatario_avv = null;
			String[] lNote_avv = null;

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)
					&& getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE) != null
					&& !getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE).toString()
							.equals("")) {
				lSedeDestinatario_avv = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			}

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
					&& getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
					&& !getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
							.toString().equals("-")) {
				lDestinatario_avv = getRequestStringParameters(
						ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			}

			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
					&& getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE) != null
					&& !getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE).equals("")) {
				lNote_avv = getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
			}

			int lIndNotifiche = 0;
			int lNumAvvNotifiche = 0;
			if (lAvvocati != null)
				lNumAvvNotifiche = lAvvocati.length;

			if (lAvvocati != null) {
				while (lIndNotifiche < lNumAvvNotifiche) {
					NotificaModel lNot = new NotificaModel();
					lNot.setCodTipoNotifica("ND");
					lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndNotifiche]));
					if (lNote_avv != null)
						lNot.setNote(lNote_avv[lIndNotifiche]);
					lNot.setDataInvio(lDataTrasmissione);
					lNot.setCodEsito("-");

					lNot.setCodOperatoreInserimento(lCodiceOperatore);
					lNot.setDataInserimento(DateUtils.getSysDate());
					lNot.setCodUfficioInserimento(lCodiceUfficio);
					lNot.setCodiceOperatoreAggiornamento(lCodiceOperatore);
					lNot.setDataAggiornamento(DateUtils.getSysDate());
					lNot.setCodUfficioAggiornamento(lCodiceUfficio);

					AutoritaEsternaModel lAut = new AutoritaEsternaModel();
					if (isRequestChecked("SiNoTe")) { // Sistema Notifiche Telematiche
														// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
														// variabile di istanza siesLogger al posto di
														// LogF3B.getLogger()
														// siesLogger.debug("Sistema Notifiche Telematiche");
						lAut.setCodTipoAutorita("C0");
						lAut.setCodSede("-");
					} else {
						// Notifica tramite UNEP
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("Notifiche all'UNEP");
						lAut.setCodTipoAutorita(lDestinatario_avv[lIndNotifiche]);
						ComuneModel lComMod = new ComuneModel(
								getCodComuneByDescr(lSedeDestinatario_avv[lIndNotifiche]));
						lAut.setCodSede(lComMod.getCodComune());
						lAut.setDescrizione(lComMod.getDescrizione());
					}

					lAut.setCodOperatoreInserimento(lCodiceOperatore);
					lAut.setDataInserimento(DateUtils.getSysDate());
					lAut.setCodUfficioInserimento(lCodiceUfficio);
					lAut.setCodOperatoreAggiornamento(lCodiceOperatore);
					lAut.setCodUfficioAggiornamento(lCodiceUfficio);
					lAut.setDataAggiornamento(DateUtils.getSysDate());

					// Setto l'Autorita Esterna per la notifica corrente
					lNot.setAutoritaEsterna(lAut);
					lIndNotifiche++;
					lNotificheArray.add(lNot);
				}
			}

		} // End notifiche al difensore

		return (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
	}
}