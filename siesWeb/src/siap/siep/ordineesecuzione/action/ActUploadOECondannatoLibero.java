package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
 * <p>Title: ActUploadOECondannatoLibero</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActUploadOECondannatoLibero extends ActionSiap
implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws Exception
  {
    EventoModel lModel = new EventoModel();
    lModel.setIdEvento(new BigDecimal(getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO)));


    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    if(lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lModel.setDocBlobIn(lSt);
    }
    
    lModel.setDataAggiornamento( DateUtils.getSysDate());
    lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso() );
    lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso() );

     if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
       lModel.setFlagDocumentoRegistrato("S");
     else
       lModel.setFlagDocumentoRegistrato("N");

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
 	  lCtrl.ExUpdateDocument(lModel);

   //Prepara la pagina di destinazione
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Aggiornamento Documento Avvenuto Correttamente!");
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );

    lRedirigi.setAction("siap.sico.evento.action.ActRicercaEvento" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE;
  }

}
