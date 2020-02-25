package siap.siep.misuraalternativa.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Action di validazione del provvedimento di Esecuzione della prosecuzione
 * 51bis
 * @author d.fiorletta
 *
 */
public class ActUploadMAProsecuzione51Bis extends ActionSiap implements ICostantiEvento
{
  public String processRequest() throws Exception
  {

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    EventoModel lEveModel = new EventoModel();
    lEveModel.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );
    
    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

    if(lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];

      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lEveModel.setDocBlobIn(lSt);
    }

    lEveModel.setDataAggiornamento         (DateUtils.getSysDate());
    lEveModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lEveModel.setCodOperatoreAggiornamento (getCodUtenteConnesso() );


    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    {
      lEveModel.setFlagDocumentoRegistrato("S");
      IMisuraAlternativaIndultino lCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
      
      lCtrl.ExUpdateValidaMAProsecuzione51Bis(lEveModel, lFascMod);
    }
    else
    {
      lEveModel.setFlagDocumentoRegistrato("N");

      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
      lCtrl.ExUpdateDocument(lEveModel);
    }

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");
    
    String lMsgUlteriore = "";
    if(    isRequestChecked( ICostantiEvento.CAMPO_VALIDA) 
        && !"S".equals(lFascMod.getFlagCumulante())  //senza cumulo
       )
    {
      // Recupero l'evento
      IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
      EventoModel lEveMod = lCtrlEvento.ExRicercaEventoByKey(lEveModel.getIdEvento());
      
      // Recupero la MA
      IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
      MisuraAlternativaModel lMisuraModel = new MisuraAlternativaModel();
      lMisuraModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEveIdEvento());
      
      
      if (lMisuraModel.getDataInizioMisura()!=null) {
        if (DateUtils.isGreater(lMisuraModel.getDataInizioMisura(), DateUtils.getSysDate()))
          lMsgUlteriore = "&ChgPosMsg=SI";
      }
      
    } 


    if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO)+lMsgUlteriore);
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
    }

    return IWebConstants.PG_MESSAGE;    
  }
}
