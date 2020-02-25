package siap.siep.istanza.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadTrasferisciIstanzaStessaBDI extends ActionSiap implements ICostantiIstanza
{
	public String processRequest() throws Exception
	{

		//Insieme degli uffici destinatari
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "-");
	  setRequestAttribute("uffici", "" + lOption);

		return PG_LOAD_TRASFERISCI_ISTANZA_STESSA_BDI;
  }

}