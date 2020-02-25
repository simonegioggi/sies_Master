package siap.siep.tipoeventibdmc.action;

import siap.sico.web.ActionSiap;
import siap.siep.tipoeventibdmc.controller.ITipoEventiBdmc;
import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActInserisciTipoEventiBdmc
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di TipoEventiBdmc
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
public class ActInserisciTipoEventiBdmc extends ActionSiap implements ICostantiTipoEventiBdmc {

	/*****************************************************************************
	 * Azione di Inserimento del TipoEventiBdmc
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		TipoEventiBdmcModel lTipMod = new TipoEventiBdmcModel();

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		lTipMod.setIdTipoEventiBdmc(getRequestBigDecimalParameter(CAMPO_ID_TIPO_EVENTI_BDMC));
		lTipMod.setCodTipoEvento(getRequestStringParameter(CAMPO_COD_TIPO_EVENTO));
		lTipMod.setCodProvvedimento(getRequestStringParameter(CAMPO_COD_PROVVEDIMENTO));
		lTipMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));

		// ===================================================
		// Recupera il controller ed effettua l'inserimento
		// ===================================================
		ITipoEventiBdmc lCtrl = SIEPLookupRemote.getTipoEventiBdmcRemote();
		TipoEventiBdmcModel lTipRetMod = new TipoEventiBdmcModel();
		lTipRetMod = lCtrl.ExInserisciTipoEventiBdmc(lTipMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.siep.tipoeventibdmc.action.ActLoadDettaglioTipoEventiBdmc";
		lPage += "&" + CAMPO_ID_TIPO_EVENTI_BDMC + "=" + lTipRetMod.getIdTipoEventiBdmc().toString();

		return lPage;
	}

}