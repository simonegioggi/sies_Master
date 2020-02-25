package siap.siep.sentenza.action;



/**
 * <p>Title: ActInserisciDecreto</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class ActInserisciSentenzaStraniera  extends ActInserisciSentenzaGenerale implements ICostantiSentenza
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
		
		// SENTENZA STRANIERA
		mSenMod.setCodTipoProvvedimento( "05");

		/*if(isRequestChecked(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA))
			mSenMod.setFlagSentenzaApplicazPena("S");
		else
			mSenMod.setFlagSentenzaApplicazPena("N");*/
		
		if (mModifica)
			lPage = modifica();
		else
			lPage = inserimento();

		return lPage;
	}

}