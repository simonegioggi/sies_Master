package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciCircostanzaCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load di: Inserimento Circostanza Cumulo (Aggravanti/Attenuanti)
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciCircostanzaCumulo extends ActionModuloCumulo
		implements ICostantiCircostanzaCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// =========================================================================
		// Recupero i dati del TITOLOCUMULO e ISTRUTTORIACUMULO da passare alla form
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// FascicoloSiepModel lFasMod = ((FascicoloSiepModel)getSessionAttribute("fascicolo"));
		BigDecimal lTito = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		ICircostanzaCumulo lCtrlC = SIEPLookupRemote.getCircostanzaCumuloRemote();
		Vector<CircostanzaCumuloModel> lVect = lCtrlC.ExRicercaCircostanzaCumulobyTitolo(lTito);

		for (int i = 0; i < lVect.size(); i++) {
			CircostanzaCumuloModel lCirMod = lVect.get(i);
			siesLogger.debug("lCirMod = " + i + " " + lCirMod);
			setRequestAttribute("circostanzaCumulo", lCirMod);
		}

		setRequestAttribute("VectorCircosCumulo", lVect);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), "-");
		setRequestAttribute("TipiFontiReato", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
		setRequestAttribute("TipiSottonumerazione", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getBilanciamentoCircostanze());
		setRequestAttribute("BilanciamentoCircostanze", "" + lOption);

		setRequestAttribute("modalita", "I");

		return PG_LOAD_INSERISCICIRCOSTANZA_CUMULO; // restituisce la jsp di VIEW
	}

}