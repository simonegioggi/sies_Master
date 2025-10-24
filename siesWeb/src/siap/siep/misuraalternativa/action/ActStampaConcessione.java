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
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaConcessione</p>
 * <p>Description: Produce il documento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaConcessione extends ActConcessione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

//posizione giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPosAltra = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
    String lPosizioneGiu = lPosAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();

//ricerca evento
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(lIdEvento);
    String lMotivo = lEventoModel.getCodMotivo();
      
//ricerca mA
    IMisuraAlternativa lCtrlMis = SICOLookupRemote.getMisuraAlternativaRemote();
    MisuraAlternativaModel lMisMod = lCtrlMis.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento()); //lEventoModel


//evento
    EventoNotificaModel lEveMod = new EventoNotificaModel();
    lEveMod.getEvento().setIdEvento(lIdEvento);
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    //ricerca posizione precedente
    PosizioneGiuridicaModel lPosPrec = new PosizioneGiuridicaModel();
    lPosPrec = lPosCtrl.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
   
    //setta il flag template
    String lTipoMisura = this.getRequestStringParameter("tipoMisura");
    String IdEveAPF = this.getRequestStringParameter("IdEventoAmmProvvAff");
    String flagTemplate = null;
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("===========> lTipoMisura === "+lTipoMisura);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("===========> lMotivo === "+lMotivo);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("===========> lPosizioneGiu " + lPosizioneGiu + "<--------");
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("===========> flagTemplate " + flagTemplate + "<--------");
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("===========> IdEveAPF " + IdEveAPF + "<--------");
    
    if(lTipoMisura.equals("AFFIDAMENTO"))
    {
       //se vengo dalla sanzione sostitutiva forzo il la posizione giuridica che è 19 in 03 
       //perchè deve richiamare lo stesso template
       if("0605".equals(lMotivo) || "0606".equals(lMotivo) || "0607".equals(lMotivo)){
    	  lPosizioneGiu = "03"; 
        }

       flagTemplate = getFlagTemplateAffidamento(lPosPrec,lPosizioneGiu,lMisMod,IdEveAPF);
    }else if(lTipoMisura.equals("DETENZIONE")){
       flagTemplate = getFlagTemplateDetDom(lPosPrec,lPosizioneGiu,lMisMod);
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.info("===========> +++++++++++++++++++++++++++++++++ <--------");
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.info("===========> lMotivo " + lMotivo + "<--------");	
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.info("===========> lPosizioneGiu " + lPosizioneGiu + "<--------");
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.info("===========> flagTemplate " + flagTemplate + "<--------");
    }
    else if(lTipoMisura.equals("SEMILIBERTA"))
       flagTemplate = getFlagTemplateSemiliberta(lPosPrec,lPosizioneGiu,lMisMod);
    else if(lTipoMisura.equals("INDULTINO"))
       flagTemplate = getFlagTemplateIndultino(lPosPrec,lPosizioneGiu,lMisMod);
    // 24/08/2010 Stampa Ordinanza Detenzione presso domicilio
    else if(lTipoMisura.equals("ESP_PRESSO_DOM"))
      flagTemplate = getFlagTemplateEspPressoDomicilio(lPosPrec,lPosizioneGiu,lMisMod);
    // 23/11/2010 Gestione SOSP_ESP_PRESSO_DOM e SOSP_ESP_PRESSO_DOM_51BIS
    else if(lTipoMisura.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM))
      flagTemplate = getFlagTemplateSospensioneEspPressoDomicilio(lPosPrec,lMisMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("------------> flagTemplate " + flagTemplate + "<--------");

//cerco il documento
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    TemplateModel lTemMod = new TemplateModel();
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("===========> flagTemplate === "+flagTemplate);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("===========> getCodTipoEvento === "+lEventoModel.getCodTipoEvento());
    
    if ( lMisMod != null && lMisMod.getCodTipoMisura() != null ) {
    	// mev 62
    	if("0610".equals(lMisMod.getCodTipoMisura())){
    		// deve prendee gli stessi template del tipo misura 2630
    		lMisMod.setCodTipoMisura("2630");
    	}
    	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	siesLogger.info("===========> lMisMod === "+lMisMod.getCodTipoMisura());
    }
    
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.info(">>>>>>>> flagTemplate = "+flagTemplate);
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.info(">>>>>>>> CodTipoEvento = "+lEventoModel.getCodTipoEvento());
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.info(">>>>>>>> CodMotivo = "+lEventoModel.getCodMotivo());
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.info(">>>>>>>> CodTipoMisura = "+lMisMod.getCodTipoMisura());
    if(flagTemplate != null && lEventoModel != null && lEventoModel.getCodTipoEvento() != null
       && lEventoModel.getCodMotivo() != null && !lEventoModel.getCodMotivo().equals("0000")
       && lMisMod != null && lMisMod.getCodTipoMisura() != null )
    {
      lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoModel.getCodTipoEvento(),"03",
                                                                                lMisMod.getCodTipoMisura(),flagTemplate);
      lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
    }
    else
      lEveMod.setNomeTemplate(TEMPLATE_VUOTO);

//produce la stampa
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}