package siap.bdmc.fascicolosiepbdmc.action;

import java.math.BigDecimal;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
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
public class ActLoadDettaglioFascicoloSiepBdmc extends ActionSiap implements ICostantiFascicoloSiepBdmc {

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

		BigDecimal lIdFascicoloBdmc = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_BDMC);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		IFascicoloSiepBdmc lCtrl = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
		FascicoloSiepBdmcModel lFasMod = lCtrl.ExRicercaFascicoloSiepBdmcById(lIdFascicoloBdmc);

		String codUfficio = this.getUfficioUtenteConnesso().getCodUfficio(); // per il codice ufficio
																				// dell'operatore connesso
		String descrUfficio = this.getUfficioUtenteConnesso().getDescrTipoUfficio(); // per la descrizione del
																						// tipo ufficio
																						// dell'utente
																						// connesso
		String descrComune = this.getUfficioUtenteConnesso().getDescrComune(); // per il comune dell'ufficio
																				// connesso
		String codProv = this.getUfficioUtenteConnesso().getCodProvincia(); // per la sigla della provincia

		setRequestAttribute("codUfficio", codUfficio);
		setRequestAttribute("descrUfficio", descrUfficio);
		setRequestAttribute("descrComune", descrComune);
		setRequestAttribute("codProv", codProv);

		setRequestAttribute("descrLuogoEmittente", getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));
		setRequestAttribute("codTipoAutoritaEmittente", getRequestStringParameter(CAMPO_DESCR_AUTEMI_BDMC));

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
			setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
			return ISIAPCostantiWeb.PG_MESSAGE;
		}

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("fascicolosiepbdmc", lFasMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");
		return PG_LOAD_DETTAGLIOFASCICOLOSIEPBDMC;
	}

}