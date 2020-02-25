package siap.sius.richiestaatti.action;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActUploadRichiestaAtti extends ActionSiap
implements ICostantiEvento
{
  public String processRequest() throws Exception
  {
    // Istanzia un oggetto eventoModel.
    EventoModel lModel = new EventoModel();
    // Preleva dalla form l' ID_EVENTO e lo setta nel model relativo
    lModel.setIdEvento(new BigDecimal(getRequestStringParameter(CAMPO_ID_EVENTO)));
    // Preleva dall'HTTP il contenuto del file in formato  <code> ByteArrayInputStream </code>
    ByteArrayInputStream lBlob = getFileByteArrayInputStream( ICostantiEvento.CAMPO_BLOB);
    // impostiamo il file ricevuto nel model
    lModel.setDocBlobIn(lBlob);

    lModel.setDataAggiornamento( DateUtils.getSysDate());
    lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso() );
    lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso() );

    // Controlla se il check box validata è stato impostato
    if(isRequestChecked(CAMPO_VALIDA) )
      lModel.setFlagDocumentoRegistrato("S");
    else
      lModel.setFlagDocumentoRegistrato("N");

    // Effettua il lookup dell' interfaccia dell' evento
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    // Effettua l'aggiornamento del campo blob.
    lCtrl.ExUpdateDocument(lModel);

    //Msg di conferma
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Aggiornamento Documento Avvenuto Correttamente!");

    //Prepara la pagina di destinazione
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction("siap.sius.richiestaatti.action.ActRicercaFSPRichiestaAtti" );
    // Indica all'azione chiamata di non effettuare una nuava ricerca puntuale del fascicolo
    lRedirigi.setParameter("viewElenco", "S");

    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE;
  }
}