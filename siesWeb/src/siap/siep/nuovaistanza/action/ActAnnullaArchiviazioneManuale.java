package siap.siep.nuovaistanza.action;


/**
 * <p>Title: ActAnnullaArchiviazioneManuale</p>
 * <p>Description: La Action esegue l'annullamento 
 *  dell'archiviazione del  Fascicolo SIEP.</p>
 * <p> L'annullamento viene eseguito utilizzando la Caction ancestor. </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Agile</p>
 * @author Luigi
 * @version 1.0
 */

public class ActAnnullaArchiviazioneManuale extends ActArchiviazioneManuale
{
	
  public String processRequest() throws Exception
  {
	  isAnnullamento = true;
	   super.processRequest();
	   return ritornoDopoCancellazione("Archiviazione Annullata " , null);
  }
}