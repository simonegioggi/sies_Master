package siap.siep.risultatoricerca.action;

import java.util.ArrayList;
import java.util.List;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRicercaRisultatoRicerca
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di RisultatoRicerca
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadRicercaRisultatoRicerca extends ActionSiap implements ICostantiRisultatoRicerca {

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

	public String processRequest() throws F3BException {

		// Imposta Combo posizione Giuridica
		ArrayList lTipoPosizioni = null;
		lTipoPosizioni = new ArrayList(DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione());
		setRequestAttribute("posizioneGiuridica", lTipoPosizioni);

		Option lOption = new Option(elencoNazioni());
		setRequestAttribute("TipiNazione", "" + lOption);

		return PG_LOAD_RICERCA_RISULTATO_RICERCA; // restituisce la jsp di VIEW
	}

}