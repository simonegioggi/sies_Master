package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.controller.ReatoContinuazioneCumuloController;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadModificaContinuazioneReatiCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load di Modifica Continuazione Reati
 * </p>
 * <p>
 * legati al titolo Cumulato (Reato_Cumulo /ì
 * </p>
 */

public class ActLoadModificaContinuazioneReatiCumulo extends ActionModuloCumulo
		implements ICostantiReatoCumulo {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// ==========================================================================
		// Recupero i dati del da passare alla form di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		ReatoCumuloModel lReaCumMod = new ReatoCumuloModel();
		lReaCumMod.setTitIdTitoloCumulato(lIdTitolo);
		IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
		Vector lVect = lCtrl.ExRicercaReatoCumulo(lReaCumMod);

		ReatoContinuazioneCumuloController lRCtrl = new ReatoContinuazioneCumuloController();

		setRequestAttribute("reaticum", lVect);
		setRequestAttribute("stringareaticum", lRCtrl.getStringheReati(lVect));
		setRequestAttribute("table", lRCtrl.getTableContinuazioni(lVect));

		return PG_LOADMODIFICACONTINUAZIONEREATI_CUM;
	}

}