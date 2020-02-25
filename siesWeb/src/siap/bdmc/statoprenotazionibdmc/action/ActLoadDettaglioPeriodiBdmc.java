package siap.bdmc.statoprenotazionibdmc.action;

import java.math.BigDecimal;

import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.web.ActionSiap;
import siap.siep.misuracautelarebdmc.action.ICostantiMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioMisuraCautelareBdmc
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di MisuraCautelareBdmc
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
public class ActLoadDettaglioPeriodiBdmc extends ActionSiap implements ICostantiMisuraCautelareBdmc {

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
		BigDecimal lIdMisuraCautelare = getRequestBigDecimalParameter(CAMPO_ID_MISURA_CAUTELARE);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		IMisuraCautelareBdmc lCtrl = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
		MisuraCautelareBdmcModel lMisMod = lCtrl.ExRicercaMisuraCautelareBdmcById(lIdMisuraCautelare);

		String descUfficioBdmc = new String();
		String descUfficioSiep = new String();

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lMisMod == null) {
			setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /siap/frame.htm
			// setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
			return ISIAPCostantiWeb.PG_MESSAGE;
		}

		else {
			descUfficioBdmc = UfficioUtils.getDescTipoUffByCodUfficio(lMisMod.getCodUfficioBdmc());
			descUfficioSiep = UfficioUtils.getDescTipoUffByCodUfficio(lMisMod.getCodUfficioInserimento());

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

		setRequestAttribute("misuracautelarebdmc", lMisMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");

		return ICostantiStatoPrenotazioniBdmc.PG_LOAD_DETTAGLIOMISURACAUTELAREBDMC;
	}

}