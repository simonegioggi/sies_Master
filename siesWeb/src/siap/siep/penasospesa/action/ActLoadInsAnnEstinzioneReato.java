package siap.siep.penasospesa.action;



/**
* <p>Title: ActLoadInserisciRicEstinzioneReato</p>
* <p>Description: Classe Action per la load inserisci di una Richiesta Estinzione Reato.</p> 
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: Agile</p>
* <p> @author: Luigi</p>
* @version 1.0
*/

public class ActLoadInsAnnEstinzioneReato extends ActLoadInserisciRicEstinzioneReato
{

  
  public String processRequest() throws Exception
  {
	 mNomeAction = "siap.siep.penasospesa.action.ActLoadInsAnnEstinzioneReato";
	 mNomeJsp = PG_LOAD_INSERISCI_ANN_ESTINZIONE_REATO;
 
	 return super.processRequest();

  }
  
 


}