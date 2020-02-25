package siap.siep.posizione.action;

import java.math.BigDecimal;

import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
* <p>Title: ActModificaPosizioneGiuridica</p>
* <p>Description: Classe Action per la modifica di PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActModificaPosizioneGiuridica extends ActGestionePosizioneGiuridica implements ICostantiPosizioneGiuridica {

	/**
	 * Azione di Modifica del PosizioneGiuridica
	 * @return Nome della pagina JSP da visualizzare
	 * al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFasMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicoloSiepAssociato = lFasMod.getIdFascicoloSiep();
		PosizioneGiuridicaModel lPosMod = getPosizioneGiuridica(lIdFascicoloSiepAssociato, "");

		lPosMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lPosMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lPosMod.setDataAggiornamento(DateUtils.getSysDate());

		// Gestione campi Riferiti a 'Detenuto Per Altra Causa' sulla tabella FASCICOLO_SIEP
		checkFlagAltraCausa(lFasMod, "");

		// Gestione campo COD_TIPO_POS_LIBERO (irreperibile) sulla tabella FASCICOLO_SIEP
		if (isRequestChecked(ICostantiFascicoloSiep.CAMPO_COD_TIPO_POS_LIBERO))
			lFasMod.setCodTipoPosLibero("I");
		else
			lFasMod.setCodTipoPosLibero("-");

		LuogoDetenzioneModel lLuogoDetenzione = null;
		AltraCausaModel lAltraCausa = null;
		// *** Gestione Luogo Detenzione / Altra Causa ***
		// * Il luogo di detenzione viene inserito se e solo se *
		// * il check detenuto per altra causa non è selezionato *
		// if (!isRequestChecked(ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA)) {
		// LUOGO DETENZIONE
		lLuogoDetenzione = getLuogoDetenzione(lIdFascicoloSiepAssociato, "");
		// } else {
		// ALTRA CAUSA
		lAltraCausa = getAltraCausa(lIdFascicoloSiepAssociato, "");
		// }

		// PosizioneGiuridicaController lCtrl = new PosizioneGiuridicaController();
		IPosizioneGiuridica lCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		// lPosMod = lCtrl.ExModificaPosizioneGiuridicaModificaFascicoloSiepAssociatoLuogoDetenzione(lPosMod, lFasMod, lLuogoDetenzione);
		lPosMod = lCtrl.ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo(lPosMod, lFasMod, lLuogoDetenzione, lAltraCausa);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.posizione.action.ActLoadDettaglioPosizioneGiuridica&" + CAMPO_ID_POSIZIONE_GIURIDICA + "=" + lPosMod.getIdPosizioneGiuridica().toString();
		return lPage;
	}
}