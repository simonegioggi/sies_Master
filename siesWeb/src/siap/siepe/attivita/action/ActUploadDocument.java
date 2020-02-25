package siap.siepe.attivita.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
 * <p>Title: ActUploadDocument</p>
 * <p>Description: Azione demandata alla realizzazione delle funzioni
 * di validazione ed upload sulla tabella EVENTO.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActUploadDocument extends siap.sico.evento.action.ActUploadDocument
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  // Controller IAttivita utilizzato per accedere alla tabella ATTIVITA.
   public IAttivita mAttCtrl = null;

  public String processRequest() throws Exception
  {
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): inizio");

     // Viene valorizzato l'attributo di classe contenente l'azione da passare ad eventuale jsp Warning
     mAzione = "siap.siepe.attivita.action.ActUploadDocument";

     String lPage = super.processRequest();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): fine");
    return lPage;
  }

 /**
  * La funzione ridefinisce la stessa del super.
  * Esegue l'Update della tabella ATTIVITA con o senza l'inserimento
  * del documento di upload nel BLOB.
  * @param aId
  * @throws Exception
  */
 public void updateTabella(BigDecimal aId) throws Exception
 {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getPackage().getName() + ".updateTabella(): inizio");

    // Valorizzazione del Model
    AttivitaModel lModel = new AttivitaModel();
    lModel.setIdAttivita(aId);
     if (mInStr != null)
        lModel.setDocBlobIn(mInStr);
     lModel.setDataAggiornamento( DateUtils.getSysDate());
     lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso() );
     lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso() );
     if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
        lModel.setFlagDocumentoRegistrato("S");
     else
        lModel.setFlagDocumentoRegistrato("N");

        // Aggiornamento del record attraverso la chiamata al Controller
     if (mAttCtrl == null)
        mAttCtrl = SIEPELookupRemote.getAttivitaRemote();
     mAttCtrl.ExUpdateDocument(lModel);
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug(this.getClass().getPackage().getName() + ".updateTabella(): fine");
     return;
 }

 /**
  * La funzione ridefinisce la stessa del super.
  * Funzione di utilità. Viene richiamata per controllare l'esistenza
  * del documento nel BLOB della tabella ATTIVITA.
  * Se tale documento non esiste viene lanciata un'eccezione.
  * @param aId
  * @throws Exception
  */
 public void leggiDocumento( BigDecimal aId) throws Exception
 {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getPackage().getName() + ".leggiDocumento(): inizio");

    // Valorizzazione del Model con la chiave di ricerca
    AttivitaModel lModel = new AttivitaModel();
    lModel.setIdAttivita(aId);
    // Attivazione della funzione attraverso il Controller
    if (mAttCtrl == null)
       mAttCtrl = SIEPELookupRemote.getAttivitaRemote();
    mAttCtrl.ExGetDocumento(lModel);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getPackage().getName() + ".leggiDocumento(): fine");
    return;
 }



}