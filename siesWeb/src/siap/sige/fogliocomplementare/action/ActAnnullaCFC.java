package siap.sige.fogliocomplementare.action;

import org.apache.log4j.Logger;

import siap.sige.SIGEException;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;


/**
* <p>Title: ActAnnullaCFC</p>
* <p>Description: Classe Action per annullare un Foglio Complementare </p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActAnnullaCFC extends ActionSige implements ICostantiFoglioComp
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
	public String processRequest() throws Exception
    {
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
	    String lRectPage = null;
	    if (isRequestParameterNullObj(CAMPO_ID_DOCUMENTO_ALLEGATO) || isRequestParameterNullObj(CAMPO_MOTIVO_ANNULLAMENTO) )
	      throw new SIGEException(SIGEException.USER_MESSAGE, "Errore nei dati");
	
	    gestioneRitorno();
	
	    annulla();
	
        lRectPage = ritornoDopoCancellazione("Foglio Complementare annullato!", lRectPage);
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug( getClass().getName() + ".processRequest: fine" );
	
	    return lRectPage;
  }


  private void annulla() throws Exception
  {
	    //Istanzio il Model e lo carico con quello posto nella request.
	    DocumentoAllegatoModel lDocAll = new DocumentoAllegatoModel();
	
	   //Dati da aggiornare
	   lDocAll.setIdDocumentoAllegato(getRequestBigDecimalParameter(CAMPO_ID_DOCUMENTO_ALLEGATO));
	   lDocAll.setCodOperatoreAggiornamento(getCodUtenteConnesso()); //Codice dell'operatore che annulla
	   lDocAll.setCodUfficioAggiornamento( getCodUfficioUtenteConnesso()); //Codice dell'operatore che annulla
	   lDocAll.setDataAggiornamento(DateUtils.getSysDate());
	   lDocAll.setDataAnnullamento(lDocAll.getDataAggiornamento());
	   lDocAll.setMotivoAnnullamento(getRequestStringParameter(CAMPO_MOTIVO_ANNULLAMENTO));
	
	   // Viene richiamato il Controller per eseguire l'Update
	   IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
	   lDocAllCtrl.ExAnnullaDocumentoAllegato(lDocAll);
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug( "ID Documento Allegato Aggiornato: " + lDocAll.getIdDocumentoAllegato() );

  }

}