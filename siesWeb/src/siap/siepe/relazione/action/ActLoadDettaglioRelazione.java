package siap.siepe.relazione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.relazione.controller.IRelazione;
import siap.siepe.relazione.model.RelazioneModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadDettaglioRelazione</p>
* <p>Description: Classe Action per la load dettaglio di Relazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadDettaglioRelazione extends ActionSiap implements ICostantiRelazione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
   public String processRequest() throws Exception
   {
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

      // Documento di stampa
      ByteArrayOutputStream lReport = null;
      // Chiave di ricerca letto dalla request
      BigDecimal lKeyRelazione = getRequestBigDecimalParameter(CAMPO_ID_RELAZIONE );
	  
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Attivazione ExRicercaRelazioneByKey");

      // chiama il controller
      IRelazione lCtrl = SIEPELookupRemote.getRelazioneRemote();
      RelazioneModel lRelMod = lCtrl.ExRicercaRelazioneByKey(lKeyRelazione);
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Estrazione del BLOB");
      lReport = lRelMod.getDocBlobOut();
      

       //Prepara la pagina di destinazione
      if (lReport != null)
        setRequestAttribute("report", lReport);
      else
          throw new SIEPEException(SIEPEException.USER_MESSAGE, "Stampa non disponibile !");
	 
     int lDimBlob = lReport.size();
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug("Dim Blob -> " + lDimBlob);
     
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug(this.getClass().getName() + ".processRequest(): FINE");

      return IWebConstants.PG_DOWNLOAD_NEW;
   }
}