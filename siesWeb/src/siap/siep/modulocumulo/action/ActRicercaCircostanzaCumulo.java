package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaCircostanzaCumulo
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Circostanza_Cumulo
 * </p>
 * 
 * @version 1.0
 */

public class ActRicercaCircostanzaCumulo extends ActionModuloCumulo implements ICostantiCircostanzaCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// ==========================================================================
		// Recupero i dati del TITOLOCUMULO e ISTRUTTORIACUMULO da passare alla form
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// FascicoloSiepModel lFasMod = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));
		BigDecimal lTito = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		ICircostanzaCumulo lCtrl = SIEPLookupRemote.getCircostanzaCumuloRemote();

		Vector<CircostanzaCumuloModel> lVect = lCtrl.ExRicercaCircostanzaCumulobyTitolo(lTito);

		for (int i = 0; i < lVect.size(); i++) {
			CircostanzaCumuloModel lCirMod = lVect.get(i);
			siesLogger.debug("lCirMod = " + i + " " + lCirMod);
		}

		setRequestAttribute("circostanzaCum", lVect);
		return PG_RICERCA_CIRCOSTANZA_CUMULO;
	}
}
