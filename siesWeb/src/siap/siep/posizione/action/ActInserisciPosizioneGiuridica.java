package siap.siep.posizione.action;

import java.math.BigDecimal;

import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;

/**
* <p>Title: ActInserisciPosizioneGiuridica</p>
* <p>Description: Classe Action per l'inserimento di PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActInserisciPosizioneGiuridica extends ActGestionePosizioneGiuridica implements ICostantiPosizioneGiuridica {

	/**
	 * Inserisce la Posizione Giuridica
	 * @return la Pagina JSP da visualizzare
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		// Recupero il Fascicolo Siep dalla Sessione
		FascicoloSiepModel lFasMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String codMaschera = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_MASCHERA);

		// Gestione Flag 'Detenuto Per Altra Causa' sulla tabella FASCICOLO_SIEP
		checkFlagAltraCausa(lFasMod, codMaschera);
		lFasMod.setCodTipoPosLibero("-");

		BigDecimal lIdFascicoloSiepAssociato = lFasMod.getIdFascicoloSiep();
		PosizioneGiuridicaModel lPosMod = getPosizioneGiuridica(lIdFascicoloSiepAssociato, codMaschera);
		AltraCausaModel lAltraCausa = getAltraCausa(lIdFascicoloSiepAssociato, codMaschera);
		MisuraCautelareModel lMisuraCautelare = getMisuraCautelare(lIdFascicoloSiepAssociato, codMaschera);
		LuogoDetenzioneModel lLuogoDetenzione = getLuogoDetenzione(lIdFascicoloSiepAssociato, codMaschera);

		// PosizioneGiuridicaController lCtrl = new PosizioneGiuridicaController();
		IPosizioneGiuridica lCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosRet = lCtrl.ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo(lPosMod, lFasMod, lLuogoDetenzione, lAltraCausa, lMisuraCautelare);

		// Se l'operazione è andata a buon fine il fascicolo in sessione
		// risulta aggiornato automaticamente
		String lPage = "";
		if (!this.isSessionAttributeNullObj("cumulowiz") && getSessionAttribute("cumulowiz").equals("PG")) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.cumulo.action.ActInserisciPenaComplessivaCumulo";
		} else if (this.isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.posizione.action.ActLoadDettaglioPosizioneGiuridica&" + CAMPO_ID_POSIZIONE_GIURIDICA + "=" + lPosRet.getIdPosizioneGiuridica().toString();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=" + getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
		}
		return lPage;
	}
}
