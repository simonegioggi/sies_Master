package siap.siep.calcolopena.action;

import java.util.ArrayList;
import java.util.List;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadElencoProcReato
 * </p>
 * <p>
 * Description: Carica la pagina della Ricerca dei Procedimenti per Reato
 * </p>
 * <p>
 * Copyright: Bull Italia Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadElencoProcReato extends ActionSiap implements ICostantiAnnotazioneManuale {

	private List elencoNazioni() {

		DecodificheModel defVal = new DecodificheModel();
		defVal.setCode("-");
		defVal.setDescription("-");

		DecodificheModel itaVal = new DecodificheModel();
		itaVal.setCode("I");
		itaVal.setDescription("italiana");

		DecodificheModel strVal = new DecodificheModel();
		strVal.setCode("S");
		strVal.setDescription("straniera");

		List listVal = new ArrayList();
		listVal.add(defVal);
		listVal.add(itaVal);
		listVal.add(strVal);

		return listVal;
	}

	/**
	 * Azione di caricamento della form di Ricerca dei Procedimenti per Reato.
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		Option lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato());
		setRequestAttribute("TipiFontiReato", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
		setRequestAttribute("TipiSottonumerazione", "" + lOption);
		lOption = new Option(elencoNazioni());
		setRequestAttribute("TipiNazione", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getBilanciamentoCircostanze());
		setRequestAttribute("BilanciamentoCircostanze", "" + lOption);

		return PG_LOAD_ELENCO_PROVV_REATO; // restituisce la jsp di VIEW
	}

}