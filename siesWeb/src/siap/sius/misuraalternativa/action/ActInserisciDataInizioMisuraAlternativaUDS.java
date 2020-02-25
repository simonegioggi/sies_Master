package siap.sius.misuraalternativa.action;

/**
* <p>Title: ActInserisciDataInizioMisuraAlternativaUDS</p>
* <p>Description: Classe Action per l'inserimento di Verbale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sius.scadenzario.action.ICostantiScadenzarioSius;

public class ActInserisciDataInizioMisuraAlternativaUDS extends ActionSius implements ICostantiVerbale {

	/**
	 * Azione di Inserimento del Verbale
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		BigDecimal lIdFasSius = null;
		BigDecimal lIdFasSiusOrigine = null;
		FascicoloGPModel lFasGPMod = null;
		// BigDecimal idEvento = null;

		// Fascicolo Sius
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		lIdFasSiusOrigine = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();

		VerbaleModel lVerMod = new VerbaleModel();

		// Controlla che sia stata emessa un'ordinanza della misura alternativa
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEve = lCtrlEvento.ExRicercaEventoMisuraAlternativaByIdFasSius(lIdFasSiusOrigine);
		if (lEve != null)
			lVerMod.setEveIdEvento(lEve.getEveIdEvento());

		lVerMod.setFasSiuIdFascicoloSius(lIdFasSius);
		lVerMod.setCodTipoVerbale("03");
		lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
		lVerMod.setDataEmissione(
				getRequestDateParameter(ICostantiScadenzarioSius.CAMPO_ANNO_DATA_FINE_SCADENZA,
						ICostantiScadenzarioSius.CAMPO_MESE_DATA_FINE_SCADENZA,
						ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_FINE_SCADENZA));

		// Controlla CSSA
		if (!this.isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_SEDE)) {
			// Preleva l'id del CSSA attraverso la propria descrizione.
			String lDescrCSSA = getRequestStringParameter(ICostantiRichiestaAtti.CAMPO_SEDE);
			BigDecimal lIdCSSA = this.getIdCSSAByDescrComune(lDescrCSSA);
			lVerMod.setCssIdCssa(lIdCSSA);
		}

		if (!this.isRequestParameterNullObj(ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lVerMod.setIstDetIdIstitutoDetenzione(
					this.getRequestStringParameter(ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE));
		} else {
			lVerMod.setIstDetIdIstitutoDetenzione("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)
				&& !this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)) {
			ComuneModel lComMod = this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO));
			lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());
			lVerMod.setCodTipoUfficioFirmatario(
					this.getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));
		} else {
			lVerMod.setCodTipoUfficioFirmatario("-");
			lVerMod.setCodLuogoUfficioFirmatario("-");
		}

		lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));
		lVerMod.setDataInserimento(DateUtils.getSysDate());

		// all'interno del controller aggiorna la posizione giuridica
		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
		VerbaleModel llVerModRet = lCtrl.ExInserisciDataInizioMisuraAlternativaUDS(lIdFasSius, lVerMod);

		setRequestAttribute("ufficio", "UDS");
		setRequestAttribute("verbale", llVerModRet);
		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.misuraalternativa.action.ActLoadDettaglioDataInizioMisuraAlternativa";
		return lPage;
	}

}