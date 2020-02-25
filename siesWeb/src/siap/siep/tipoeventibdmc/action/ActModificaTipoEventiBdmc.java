package siap.siep.tipoeventibdmc.action;

import siap.sico.web.ActionSiap;
import siap.siep.tipoeventibdmc.controller.ITipoEventiBdmc;
import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActModificaTipoEventiBdmc
 * </p>
 * <p>
 * Description: Classe Action per la modifica di TipoEventiBdmc
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
public class ActModificaTipoEventiBdmc extends ActionSiap implements ICostantiTipoEventiBdmc {

	/*****************************************************************************
	 * Azione di Modifica del TipoEventiBdmc
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// =====================================================
		// Riempie il model con i campi recuperati dalla form
		// n.b. per i campi del model non valorizzati, i corrispondenti
		// campi della tabella verranno impostati a null
		// Il campo chiave è obbligatorio perchè utilizzato nelle clausola where
		// per individuare il record da aggiornare
		// =====================================================
		TipoEventiBdmcModel lTipMod = new TipoEventiBdmcModel();

		lTipMod.setIdTipoEventiBdmc(getRequestBigDecimalParameter(CAMPO_ID_TIPO_EVENTI_BDMC));
		lTipMod.setCodTipoEvento(getRequestStringParameter(CAMPO_COD_TIPO_EVENTO));
		lTipMod.setCodProvvedimento(getRequestStringParameter(CAMPO_COD_PROVVEDIMENTO));
		lTipMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));

		// ===================================================
		// Recupera il controller ed effettua la modifica
		// ===================================================
		ITipoEventiBdmc lCtrl = SIEPLookupRemote.getTipoEventiBdmcRemote();
		lCtrl.ExModificaTipoEventiBdmc(lTipMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.siep.tipoeventibdmc.action.ActLoadDettaglioTipoEventiBdmc";
		lPage += "&" + CAMPO_ID_TIPO_EVENTI_BDMC + "=" + lTipMod.getIdTipoEventiBdmc().toString();

		return lPage;
	}

}