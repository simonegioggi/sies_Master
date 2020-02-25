package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
//import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
 * <p>Title: ActUploadMancataEspulsione</p>
 * <p>Description: Classe action per l'Upload/Validazione dell'annotazione mancata 
 * espulsione </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActUploadMancataEspulsione extends ActionSiap
  implements ICostantiEvento
{
  public String processRequest() throws Exception
  {
    //FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    //===============================================
    // Recupero l'id della Annotazione
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
      
      lSanzioneCtrl.exUpdateMancataEspulsione(lEveUpdateModel);
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
    lRedirigi.setAction( "siap.siep.sanzionesostitutiva.action.ActLoadDettaglioMancataEspulsione" );
    lRedirigi.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lEveUpdateModel.getIdEvento().toString() );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

  	return IWebConstants.PG_MESSAGE;
  }
}
