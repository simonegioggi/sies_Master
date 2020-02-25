package siap.siep.misuraalternativa.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActUploadMACessazione51Bis extends ActionSiap implements ICostantiEvento
{
  
  public String processRequest() throws Exception
  {
  
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    EventoModel lEveModel = new EventoModel();
    lEveModel.setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));
    
    InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);
  
    if(lInput != null)
    {
      byte[] lBuffer = new byte[lInput.available()];
  
      lInput.read(lBuffer);
      ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
      lEveModel.setDocBlobIn(lSt);
    }
  
    lEveModel.setCodOperatoreAggiornamento (getCodUtenteConnesso() );
    lEveModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lEveModel.setDataAggiornamento         (DateUtils.getSysDate());  
  
    
    //
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosModel = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPosModel = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

    
    if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
    {
      lEveModel.setFlagDocumentoRegistrato("S");
      IMisuraAlternativa lCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
      
      lCtrl.ExUpdateValidaMACessazione51bisMDS(lEveModel, lFascMod);
    }
    else
    {
      lEveModel.setFlagDocumentoRegistrato("N");
  
      IEvento lCtrl = SICOLookupRemote.getEventoRemote();
      lCtrl.ExUpdateDocument(lEveModel);
    }
  
    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");
  
    
    //==========================================================================
    // In caso di validazione per soggetto Libero, con data ingresso in istituto
    // valorizzata, ovvero detenuto altra causa esegue SORV, dopo il messagio
    // di avvanuta validazione, devo visualizzare un ilteripre messaggio
    // vedi action di dettagli
    //==========================================================================
    String lMsgUlteriore = "";
    if(    isRequestChecked( ICostantiEvento.CAMPO_VALIDA) 
        && (   lPosModel.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07")
            || lPosModel.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10")
           ) 
       )
    {
      //
      IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
      EventoModel lEveMod = lCtrlEvento.ExRicercaEventoByKey(lEveModel.getIdEvento());

      
      //
      IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
      MisuraAlternativaModel lMisuraModel = new MisuraAlternativaModel();
      lMisuraModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEveIdEvento());
      
      if (lMisuraModel!=null && lMisuraModel.getDataIngressoIstituto()!=null)     
        lMsgUlteriore = "&ChgPosMsg=SI";
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
