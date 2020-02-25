package siap.sius.fascicolo.action;

/**
* <p>Title: ActLoadRicercaProcedimentiPerDFP</p>
* <p>Description: Classe Action per la load di RicercaFascicolo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.model.DecodeModel;
import f3b.web.html.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadRicercaProcedimentiPerDFP extends ActionSiap implements ICostantiFascicoloSius {

	public String processRequest() throws Exception {

		// Imposta la combo della posizione giuridica.
		// lPosizioneGiuridica viene composta dai 3 gruppi distinti di P.G.
		Collection lPosizioneGiuridica = new ArrayList(
				DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione());

		// Si aggiungono le posizioni giuridiche di Esecuzione.
		Iterator lItx = (DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione()).iterator();
		while (lItx.hasNext()) {
			DecodeModel lElemento = (DecodeModel) lItx.next();
			if (!(lElemento.getCode().equals("-")))
				lPosizioneGiuridica.add(lElemento);
		}

		// Si aggiungono le posizioni giuridiche di Altra Causa.
		lItx = (DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa()).iterator();
		while (lItx.hasNext()) {
			DecodeModel lElemento = (DecodeModel) lItx.next();
			if (!(lElemento.getCode().equals("-")))
				lPosizioneGiuridica.add(lElemento);
		}

		Option lOption = new Option(lPosizioneGiuridica, 66);
		setRequestAttribute("posizioneGiuridica", "" + lOption);

		// Imposta la combo del Contenuto.
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		if (strCodTipoUfficio.compareTo("TDS") == 0)
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(), 75);
		else if (strCodTipoUfficio.compareTo("UDS") == 0)
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDS(), 75);
		else if (strCodTipoUfficio.compareTo("TDSM") == 0)
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDSM(), 75);
		else if (strCodTipoUfficio.compareTo("UDSM") == 0)
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), 75);
		else
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento(), 75);
		setRequestAttribute("contenuto", "" + lOption);

		return PG_LOAD_RICERCA_PROC_PERDFP; // restituisce la jsp di VIEW
	}

}
