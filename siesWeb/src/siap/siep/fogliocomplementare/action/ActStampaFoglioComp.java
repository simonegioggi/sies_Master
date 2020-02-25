package siap.siep.fogliocomplementare.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaFoglioComp </p>
 * <p>Description: Classe Azione responsabile della richiesta stampa Foglio Complementare
 * </p>
 * <p>Created: A.S.</p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaFoglioComp  extends ActionSiap
 implements ICostantiDocumentoAllegato
{
  public String processRequest() throws Exception
  {
	  IDocumentoAllegato lCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
	  
	  // Preleva dalla request la chiave del Documento Allegato
	  BigDecimal lKeyDocAll = getRequestBigDecimalParameter(CAMPO_ID_DOCUMENTO_ALLEGATO );

    // Generazione documento di stampa
    ByteArrayOutputStream lReport = null;

    lReport = lCtrl.ExStampaFoglioComp ( lKeyDocAll, getCodUfficioUtenteConnesso(), getUtenteConnesso() );

    //Prepara la pagina di destinazione
    if (lReport != null)
      setRequestAttribute("report", lReport);
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

    return IWebConstants.PG_DOWNLOAD;

  }
}
