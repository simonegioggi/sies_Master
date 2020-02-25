package siap.siep.tipoeventibdmc.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.tipoeventibdmc.controller.ITipoEventiBdmc;
import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadCancellaTipoEventiBdmc
 * </p>
 * <p>
 * Description: Classe Action per la load cancella di TipoEventiBdmc
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
public class ActLoadCancellaTipoEventiBdmc extends ActionSiap implements ICostantiTipoEventiBdmc {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Cancellazione dei dati. Viene caricata la stessa pagina di
	 * Dettaglio con il tasto cancella
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// ==========================================
		// Recupera la key del record da Visualizzare
		// ==========================================
		BigDecimal lIdTipoEventiBdmc = getRequestBigDecimalParameter(CAMPO_ID_TIPO_EVENTI_BDMC);

		// ==========================================
		// Recupera i dati del record da Cancellare
		// ==========================================
		ITipoEventiBdmc lCtrl = SIEPLookupRemote.getTipoEventiBdmcRemote();
		TipoEventiBdmcModel lTipMod = lCtrl.ExRicercaTipoEventiBdmcById(lIdTipoEventiBdmc);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lTipMod == null) {
			setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /siep/frame.htm
			setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
			return ISIAPCostantiWeb.PG_MESSAGE;
		}

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("tipoeventibdmc", lTipMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "C");

		return PG_LOAD_CANCELLATIPOEVENTIBDMC;
	}

}