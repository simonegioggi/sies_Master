package siap.sige.circostanza.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.circostanza.action.ICostantiCircostanza;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.circostanza.model.CircostanzaSigeModel;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadInserisciCircostanza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Circostanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciCircostanza extends ActionSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		Vector lCirc = null;

		// Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione
		BigDecimal lIdFasSigeSen = getIdFasSigeSentenzaInSessione();

		// Circostanza Sige per il passaggio della condizione di ricerca
		CircostanzaSigeModel lCircostanzaSige = null;
		lCircostanzaSige = new CircostanzaSigeModel();
		lCircostanzaSige.setFasSigeSenId(lIdFasSigeSen);

		try {
			ICircostanza lCtrl = SIEPLookupRemote.getCircostanzaRemote();
			lCirc = lCtrl.ExRicercaCircostanza(lCircostanzaSige);
		} catch (F3BException fe) {
			// Viene intercettato il messaggio di "elementi non trovati"
			if (fe.getErrorCode() != F3BException.USER_MESSAGE)
				throw fe;
		} catch (Exception e) {
			// Nessun Elemento Trovato
			throw new SIGEException(SIGEException.USER_MESSAGE, e.getMessage());
		}

		setRequestAttribute("circostanze", lCirc);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), "-");
		setRequestAttribute("TipiFontiReato", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
		setRequestAttribute("TipiSottonumerazione", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getBilanciamentoCircostanze());
		setRequestAttribute("BilanciamentoCircostanze", "" + lOption);

		setRequestAttribute("modalita", "I");
		setRequestAttribute("modo", "SIGE");

		return ICostantiCircostanza.PG_LOAD_INSERISCICIRCOSTANZA; // restituisce la jsp di VIEW
	}

}