package siap.sico.soggetto.action;

public class ActLoadDettaglioSoggettoRicerca extends ActLoadDettaglioSoggetto implements ICostantiSoggetto {

	public String processRequest() throws Exception
	{
		
		String TipoOperazione = getRequestStringParameter("TipoOperazione");
		this.setRequestAttribute("TipoOperazione", TipoOperazione);
		
		return (super.processRequest());
	}

}