package siap.siep.sentenza.action;



/**
 * <p>Title: ActInserisciSentenzaAltriTitoli</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: </p>
 * @author A.S.
 * @version 1.0
 */
public class ActInserisciSentenzaAltriTitoli  extends ActInserisciSentenzaGenerale implements ICostantiSentenza
{
	
	public String processRequest() throws Exception
	{
		return super.processRequest();
	}
	
	
	/**
	 * Implementazione della funzione abstract richiamata 
	 * dalla processRequest() del super che valorizza 
	 * i dati specifici di questo tipo di sentenza.
	 */
	public String preparazioneDatiSpecifici()  throws Exception
	{
		String lPage = "";
		
		if (!isRequestParameterNullObj("TipoProvvedimento"))
		{
			String CodTipoProvvedimento=getRequestStringParameter("TipoProvvedimento");
			mSenMod.setCodTipoProvvedimento(CodTipoProvvedimento);
		}
		
		if (mModifica)
			lPage = modifica();
		else
			lPage = inserimento();

		return lPage;
	}

}
