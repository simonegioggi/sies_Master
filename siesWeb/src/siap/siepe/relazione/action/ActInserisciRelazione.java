package siap.siepe.relazione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.relazione.controller.IRelazione;
import siap.siepe.relazione.model.RelazioneModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
* <p>Title: ActInserisciRelazione</p>
* <p>Description: Classe Action per l'inserimento di Relazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActInserisciRelazione extends ActionSiap implements ICostantiRelazione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Azione di Inserimento del Relazione
   * @return Nome della pagina JSP da visualizzare
   * al termine dell'elaborazione
   * @throws F3BException
   */
   public String processRequest() throws Exception
   {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

      RelazioneModel lRelMod = new RelazioneModel();

      // Esegue il controllo e valorizza opportunamente il Model per il trattamento dei
      // dati afferenti all'ID di Attività e Richiesta.

      // ATTIVITA
      if( !isRequestParameterNullObj(CAMPO_ATT_ID_ATTIVITA) )
        lRelMod.setAttIdAttivita(getRequestBigDecimalParameter( CAMPO_ATT_ID_ATTIVITA));
      // RICHIESTA
      if( !isRequestParameterNullObj(CAMPO_RIC_ID_RICHIESTA) )
        lRelMod.setRicIdRichiesta(getRequestBigDecimalParameter( CAMPO_RIC_ID_RICHIESTA ));

      lRelMod.setNote(getRequestStringParameter(CAMPO_NOTE));
      lRelMod.setDataEmissione(getRequestDateParameter( CAMPO_ANNO_DATA_EMISSIONE,CAMPO_MESE_DATA_EMISSIONE,CAMPO_GIORNO_DATA_EMISSIONE));
      lRelMod.setDocBlobIn(leggiFileUpload());
      lRelMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
      lRelMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
      lRelMod.setDataInserimento(DateUtils.getSysDate());

      IRelazione lCtrl = SIEPELookupRemote.getRelazioneRemote();
      RelazioneModel llRelModRet = lCtrl.ExInserisciRelazione(lRelMod);		 // setta la risposta nella request
      setRequestAttribute("relazione", llRelModRet);

      // Prepara la pagina di destinazione
      // Se c'è lo stack di ritorno effettua un ritorno in cima
      // String lPage = goToRitorno();
      String lPage= this.ritornoDopoCancellazione("La Relazione è stata inserita!",null);

      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("pagina ->" + lPage);
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

      return lPage;
   }

   /**
    * Metodo private che legge il file di Upload.
    * <p>
    * @throws Exception propaga errore di eccezione.
    * @return ByteArrayInputStream Ritorna il contenuto del file.
    */
   private ByteArrayInputStream leggiFileUpload() throws Exception
   {
      // Lettura del file di Upload
      InputStream lInput = null;
      lInput = getFile(CAMPO_DOC_BLOB);
      ByteArrayInputStream lInStream = null;

      if (lInput != null && lInput.available() > 0)
      {
         byte[] lBuffer = new byte[lInput.available()];
         lInput.read(lBuffer);
         lInStream = new ByteArrayInputStream(lBuffer);
         // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
         siesLogger.debug("BYTE ARRAY INPUT LENGTH >>> " +   lInStream.available());
      }
      else
         throw new SIEPEException(F3BException.USER_MESSAGE, "file di Upload non disponibile !" );
      return lInStream;
   }
}