package siap.sius.luogodetenzione.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.web.ActionSiap;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
//import siap.siep.posizione.controller.PosizioneGiuridicaController;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciLuogoDetenzione
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento del Luogo Detenzione in Sius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciLuogoDetenzione extends ActionSiap implements ICostantiLuogoDetenzione,
		ICostantiFascicoloSius {

	/**
	 * Inserisce il Luogo Detenzione
	 * 
	 * @return la Pagina JSP da visualizzare
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSIEP = null;
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFascicoloSIUS = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		// BigDecimal lIdFascicoloSIEP = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();

		lIdFascicoloSIEP = getRequestBigDecimalParameter(ICostantiLuogoDetenzione.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP);
		// BigDecimal lIdFascicoloSIUS = getRequestBigDecimalParameter(
		// ICostantiLuogoDetenzione.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS );
		String lCodTipoIstitutoDetenzione = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		String lAltroLuogo = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_ALTRO_LUOGO);
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_DETENZIONE,
				CAMPO_MESE_DATA_INIZIO_DETENZIONE, CAMPO_GIORNO_DATA_INIZIO_DETENZIONE);

		// ** Seleziona l'ultima eventuale Posizione Giuridica associata al Fascicolo Sius/Siep **
		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = lCtrlPos
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicoloSius(
						lIdFascicoloSIEP, lIdFascicoloSIUS);
		/* Opero solo su luogo detenzione SIUS */
		/*
		 * if (lPosLuoAltMod.getLuogoDetenzione() == null ) { lPosLuoAltMod =
		 * lCtrlPos.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
		 * lIdFascicoloSIEP); }
		 */
		// PosizioneGiuridicaModel lPosGiu = null;
		LuogoDetenzioneModel lLuogoDet = null;
		AltraCausaModel lAltraCausa = null;
		// lPosGiu = lPosLuoAltMod.getPosizioneGiuridica();
		lLuogoDet = lPosLuoAltMod.getLuogoDetenzione();
		lAltraCausa = lPosLuoAltMod.getAltraCausa();

		// Controllo Posizione Giuridica (SIUS-UC-001-23-UM-AB Passo 7)
		/**
		 * ci sganciamo dalla pos giuridica siep if (lIdFascicoloSIEP != null) { if (lPosGiu == null) {
		 * RedirectTo lRedirigi = new RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN);
		 * lRedirigi.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
		 * lRedirigi.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS,lIdFascicoloSIEP.toString());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi); throw new
		 * F3BException(F3BException.USER_MESSAGE
		 * ,"Posizione giuridica non valorizzata: Luogo detenzione non aggiornabile"); } }
		 **/

		// Crea Model Luogo detenzione per insert
		LuogoDetenzioneModel lLuogoDetenzione = new LuogoDetenzioneModel();

		// Controllo Esistenza Luogo_Detenzione (SIUS-UC-001-23-UM-AB Passo 8)
		if (lLuogoDet != null) {
			// Aggiorna Campi Luogo Detenzione
			lLuogoDet.setDataFineDetenzione(lDataEmissione);
			lLuogoDet.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lLuogoDet.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lLuogoDet.setDataAggiornamento(DateUtils.getSysDate());
			// lLuogoDetenzione.setPosGiuIdPosizioneGiuridica( lLuogoDet.getPosGiuIdPosizioneGiuridica());
			// 16/3/2010
		}

		// Inserimento nuovo record Luogo_Detenzione (SIUS-UC-001-23-UM-AB Passo 9)
		// Nuovi Campi Luogo Detenzione
		lLuogoDetenzione.setIstDetIdIstitutoDetenzione(lCodTipoIstitutoDetenzione);
		lLuogoDetenzione.setAltroLuogo(lAltroLuogo);
		lLuogoDetenzione.setDataInizioDetenzione(lDataEmissione);
		// lLuogoDetenzione.setFasSieIdFascicoloSiep(lIdFascicoloSIEP); michele 16/3/2010
		lLuogoDetenzione.setFasSiuIdFascicoloSius(lIdFascicoloSIUS);
		lLuogoDetenzione.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLuogoDetenzione.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLuogoDetenzione.setDataInserimento(DateUtils.getSysDate());

		// Aggiorna Campi AltraCausa
		if (lAltraCausa != null) {
			lAltraCausa
					.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
			lAltraCausa.setAltroLuogo(getRequestStringParameter(ICostantiAltraCausa.CAMPO_ALTRO_LUOGO_ALTRA));
			lAltraCausa.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lAltraCausa.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lAltraCausa.setDataAggiornamento(DateUtils.getSysDate());
		}

		// Controller
		// LuogoDetenzioneModel lLuoDetResult = new LuogoDetenzioneModel();
		ILuogoDetenzione lCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();

		/* lLuoDetResult = */lCtrl.ExInserisciLuogoDetenzioneSius(lLuogoDet, lLuogoDetenzione, lAltraCausa);

		// Se l'operazione è andata a buon fine il fascicolo in sessione
		// risulta aggiornato automaticamente
		// String lPage = "";
		return /* lPage = */IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.luogodetenzione.action.ActLoadDettaglioLuogoDetenzione&"
				+ ICostantiFascicoloSius.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS + "=" + lIdFascicoloSIUS.toString();
	}

}