package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActStampaMACessazione51Bis extends ActionSiap implements ICostantiMisuraAlternativa
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();
  
    String tipoMisura = this.getRequestStringParameter("tipoCessazione");
    
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaModel lPosMod = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
  
    String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
  
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lIdEvento));
    String lMotivo = lEventoModel.getCodMotivo();

    EventoNotificaModel lEveMod = new EventoNotificaModel();
  
    lEveMod.getEvento().setIdEvento(new BigDecimal(lIdEvento));
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
  
    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
  
    lEveMod.getEvento().setDataAggiornamento         (DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento   (lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento (this.getCodUtenteConnesso());
    
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    IMisuraAlternativa lCtrlMis = SICOLookupRemote.getMisuraAlternativaRemote();
    MisuraAlternativaModel lMisMod = lCtrlMis.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());
    
    String flagTemplate = null;
    // flagTemplate
    // 4 = Libero
    // 2 = In sospensione di Misura
    // 3 = In Misura
    // 5 = Detenuto
    String lCodPosizione = lPosMod.getCodPosizioneGiuridica();
    

    if (   tipoMisura.equals("AFFIDAMENTO")  
        && ( lCodPosizione.equals("13") || lCodPosizione.equals("54") )  // In misura
        && ("SORV").equals(lMisMod.getCodTipoUfficioScarcerazione())  // detenuto
       )
    { // In misura esegue sorveglianza quindi gia detenuto
      // Si aggancia il Template di SOSP perchè gestisce il caso con data decorrenza? 
      // AFFIDAMENTO, IN MISURA esegue SORV ovvero detenuto
      //flagTemplate = "2"; 
      flagTemplate = "6"; 
    }
    else if(   (tipoMisura.equals("AFFIDAMENTO")    && (lCodPosizione.equals("13") || lCodPosizione.equals("54") ) )
            || (tipoMisura.equals("DETENZIONE")     && (lCodPosizione.equals("12") || lCodPosizione.equals("25") || lCodPosizione.equals("29")) )
            || (tipoMisura.equals("SEMILIBERTA")    && lCodPosizione.equals("14"))
            || (tipoMisura.equals("INDULTINO")      && lCodPosizione.equals("27"))
            || (tipoMisura.equals("ESP_PRESSO_DOM") && lCodPosizione.equals("50"))
        )
    {
      if ( ("SORV").equals(lMisMod.getCodTipoUfficioScarcerazione() ) ) {
        flagTemplate = "6"; // IN MISURA - GIA' DETENUTO
      }
      else {
        flagTemplate = "3"; // IN MISURA
      }
    }
    else if(lPosMod.isLibero())
    {
      flagTemplate = "4";  // LIBERO (tutte le misure)
    }
    else if(lCodPosizione.equals("03")) // Espiazione pena in regime carcerario
    {
      flagTemplate = "5"; 
    }

  
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lMotivo = "+lMotivo);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("flagTemplate = "+flagTemplate);
    
    if(flagTemplate != null && !lMotivo.equals("0000"))
    {
     ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
     TemplateModel lTemMod = new TemplateModel();
     lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01","03",lMotivo,flagTemplate);
     lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
    }
    else
    {
       lEveMod.setNomeTemplate(TEMPLATE_VUOTO);
    }

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Nome Template = "+lEveMod.getNomeTemplate());
    
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);
  
    setRequestAttribute("report", lReport);
  
    return IWebConstants.PG_DOWNLOAD;
  }    
}