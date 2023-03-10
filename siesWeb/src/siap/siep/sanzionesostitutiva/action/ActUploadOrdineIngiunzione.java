package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

public class ActUploadOrdineIngiunzione extends ActionSiap
    implements ICostantiEvento
{
    public String processRequest() throws Exception
    {
      
      //===============================================
      // Recupero l'id dell'evento
      //===============================================
      EventoModel lEveUpdateModel = new EventoModel();
      lEveUpdateModel.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );

      InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

      if(lInput != null)
      {
        byte[] lBuffer = new byte[lInput.available()];

        lInput.read(lBuffer);
        ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
        lEveUpdateModel.setDocBlobIn(lSt);
      }


      lEveUpdateModel.setDataAggiornamento        (DateUtils.getSysDate());
      lEveUpdateModel.setCodUfficioAggiornamento  (getCodUfficioUtenteConnesso() );
      lEveUpdateModel.setCodOperatoreAggiornamento(getCodUtenteConnesso() );

      if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
      { // devo effettuare la validazione
        //====================================================
        // Invoco la funzione di validazione
        //====================================================
        lEveUpdateModel.setFlagDocumentoRegistrato("S");

        ISanzioneSostitutiva lSanzioneCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
        
        lSanzioneCtrl.exUpdateOrdineIngiunzione(lEveUpdateModel);
      }
      else
      { // aggiorno solo il blob
        lEveUpdateModel.setFlagDocumentoRegistrato("N");

        IEvento lCtrl = SICOLookupRemote.getEventoRemote();
        lCtrl.ExUpdateDocument(lEveUpdateModel);
      }

      // 
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage( IWebConstants.PG_MAIN );
      lRedirigi.setAction( getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) );
      lRedirigi.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lEveUpdateModel.getIdEvento().toString() );
      setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

      return IWebConstants.PG_MESSAGE;
    }
}
