package siap.sius.richiestaatti.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
 * <p>Title: ActInserisciSanzSostInformazioniNuovoDomicilio </p>
 * <p>Description: 
 * Classe di tipo Azione responsabile della raccolta dei dati
 * inputati nella form d'inserimento. Inoltre, creazione evento e relative
 * notifiche.
 * La classe estende la classe ActInserisciSanzSostDocumentiIstruttori, per 
 * l'uso di funzioni generalizzate.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eunics Spa</p>
 */
public class ActInserisciSanzSostInformazioniNuovoDomicilio
extends ActInserisciSanzSostDocumentiIstruttori implements ICostantiRichiestaAtti
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Azione di: recupero dati in request, creazione evento e notifiche.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws Exception propaga errore di eccezione.
  */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    lEveNot.getEvento().setCodMotivo("0577");  //  CodMotivo - Informazioni su Nuovo Domicilio
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return super.processRequest();
  }
}