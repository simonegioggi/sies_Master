package siap.bdmc.statoprenotazionibdmc.action;

import java.math.BigDecimal;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.action.ICostantiFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.bdmc.notifichesies.controller.INotificheSies;
import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioFascicoloSiepBdmc
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di FascicoloSiepBdmc
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
public class ActLoadDettaglioAssociazioneBdmc extends ActionSiap implements ICostantiFascicoloSiepBdmc {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Dettaglio dei dati.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// ==========================================
		// Recupera la key del record da Visualizzare
		// ==========================================

		BigDecimal lIdNotifica = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_BDMC);

		NotificheSiesModel lNotMod = new NotificheSiesModel();
		INotificheSies lCtrlNot = BDMCLookupRemote.getNotificheSiesRemote();
		lNotMod = lCtrlNot.ExRicercaNotificheSiesById(lIdNotifica);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		IFascicoloSiepBdmc lCtrl = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
		FascicoloSiepBdmcModel lFasMod = lCtrl.ExRicercaFascicoloSiepBdmcById(lNotMod.getIdFascicoloBdmc());

		String descUfficioBdmc = new String();
		String descUfficioSiep = new String();

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lFasMod == null) {
			setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /siap/frame.htm
			// setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
			return ISIAPCostantiWeb.PG_MESSAGE;
		} else {
			descUfficioBdmc = UfficioUtils.getDescTipoUffByCodUfficio(lFasMod.getChiaveUfficioBdmc());
			descUfficioSiep = UfficioUtils.getDescTipoUffByCodUfficio(lFasMod.getChiaveUfficioSiep());

		}

		// ====================================================
		// Passa la decodifica dell'ufficio Bdmc
		// e dell'ufficio Sieo
		// ====================================================
		setRequestAttribute("descUfficioBdmc", descUfficioBdmc);
		setRequestAttribute("descUfficioSiep", descUfficioSiep);

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================

		setRequestAttribute("fascicolosiepbdmc", lFasMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");
		return ICostantiStatoPrenotazioniBdmc.PG_LOAD_DETTAGLIOFASCICOLOSIEPBDMC;
	}

}