package siap.bdmc.sbviewnotifiche.action;

import java.math.BigDecimal;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewnotifiche.controller.ISbViewNotifiche;
import siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioSbViewNotifiche
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di SbViewNotifiche
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
public class ActLoadDettaglioSbViewNotifiche extends ActionSiap implements ICostantiSbViewNotifiche {

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
		BigDecimal lProgNoti = getRequestBigDecimalParameter(CAMPO_PROG_NOTI);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		ISbViewNotifiche lCtrl = BDMCLookupRemote.getSbViewNotificheRemote();
		SbViewNotificheModel lSbVMod = lCtrl.ExRicercaSbViewNotificheById(lProgNoti);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lSbVMod == null) {
			setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /siap/frame.htm
			setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
			return ISIAPCostantiWeb.PG_MESSAGE;
		}

		FascicoloSiepModel lFasMod = new FascicoloSiepModel();
		if (lSbVMod.getIdProvSies() != null) {
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoModel eveMod = lCtrlEve.ExRicercaEventoByKey(lSbVMod.getIdProvSies());

			if (eveMod == null) {
				setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT,
						"Eveno associato alla notifica non trovato");
				// Specificare eventualmente la jump page dove verrà ridirezionata la
				// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
				// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
				// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
				// della root_dir es /siap/frame.htm
				setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
				return ISIAPCostantiWeb.PG_MESSAGE;

			}

			IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasMod = lCtrlFas.ExRicercaFascicoloByKey(eveMod.getFasSieIdFascicoloSiep());

			if (lFasMod == null) {
				setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT,
						"Fascicolo Siep associato alla notifica non trovato");
				// Specificare eventualmente la jump page dove verrà ridirezionata la
				// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
				// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
				// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
				// della root_dir es /siap/frame.htm
				setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
				return ISIAPCostantiWeb.PG_MESSAGE;
			}
		}

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("sbviewnotifiche", lSbVMod);
		setRequestAttribute("lFascicoloSiep", lFasMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");

		return PG_LOAD_DETTAGLIOSBVIEWNOTIFICHE;
	}

}