package siap.siep.presaincarico.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActValidaAnnotaEsitoTrasmComp extends ActionSiap
{
  public String processRequest() throws Exception
  {

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    //==========================================================================
    // Recupero l'evento da Validare
    //==========================================================================
    EventoModel lEveModel = new EventoModel();
    lEveModel.setIdEvento( getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO) );

    //==========================================================================
    // Scarico l'eventuale report
    //==========================================================================
    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    if(lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lEveModel.setDocBlobIn(lSt);
    }

    //==========================================================================
    // Imposto i dati dell'aggiornamento
    //==========================================================================
    lEveModel.setDataAggiornamento         (DateUtils.getSysDate());
    lEveModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lEveModel.setCodOperatoreAggiornamento (getCodUtenteConnesso() );

    //==========================================================================
    // Validazione
    //==========================================================================
    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    {
      lEveModel.setFlagDocumentoRegistrato("S");
      IMisuraSicurezza lCtrlMisSic = SIEPLookupRemote.getMisuraSicurezzaRemote();

      lCtrlMisSic.ExUpdateValidaAnnotazioneEsito(lEveModel,lFascMod);
    }
    else
    {
      lEveModel.setFlagDocumentoRegistrato("N");

      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
      lCtrl.ExUpdateDocument(lEveModel);
    }

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

    if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
    }

    return IWebConstants.PG_MESSAGE;
  }
  

}
