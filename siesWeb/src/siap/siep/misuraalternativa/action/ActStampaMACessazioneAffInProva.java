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

/**
 * <p>Title: ActStampaMACessazioneAffInProva</p>
 * <p>Description: Produce il documento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaMACessazioneAffInProva
    extends ActionSiap
    implements ICostantiMisuraAlternativa
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();

    String tipoMisura = this.getRequestStringParameter("tipoMisura");
    
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
    if(lPosMod != null && lPosMod.getCodPosizioneGiuridica()!= null)
    {
      if(  (tipoMisura != null && tipoMisura.equals("AFFIDAMENTO") && (lPosMod.getCodPosizioneGiuridica().equals("32") || lPosMod.getCodPosizioneGiuridica().equals("37")))
         ||(tipoMisura != null && tipoMisura.equals("INDULTINO") && (lPosMod.getCodPosizioneGiuridica().equals("35") || lPosMod.getCodPosizioneGiuridica().equals("40"))) )
      {
        flagTemplate = "2";  // TEMPLATE IN SOSPENSIONE 
      }
      else if (   tipoMisura != null 
               && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) 
               && (lPosMod.getCodPosizioneGiuridica().equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_POS_GIU) || lPosMod.getCodPosizioneGiuridica().equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_POS_GIU)))
      { // Posizionei 51 e 52 ovvero in Sospensione Cautelativa o Provvisoria
      	if (lMisMod.getDataIngressoIstituto()!=null)
      		flagTemplate = "2";  // contro soggetto detenuto (esegue SORV)
      	else
      		flagTemplate = "4";  // contro soggetto NON detenuto (esegue PROC) si aggancia lo stesso template LIBERO
      }
      else if(   tipoMisura != null && tipoMisura.equals("AFFIDAMENTO") && lPosMod.getCodPosizioneGiuridica().equals("13")
              && lMisMod.getCodTipoUfficioScarcerazione() != null && lMisMod.getCodTipoUfficioScarcerazione().equals("SORV")
             )
      { // In misura esegue sorveglianza quindi gia detenuto
        flagTemplate = "2"; //FIXME perchè aggancia il template in sospensione? AFFIDAMENTO, IN MISURA esegue SORV ovvero detenuto
      }
      else if(   (tipoMisura != null && tipoMisura.equals("AFFIDAMENTO") && lPosMod.getCodPosizioneGiuridica().equals("13"))
              || (tipoMisura != null && tipoMisura.equals("INDULTINO") && lPosMod.getCodPosizioneGiuridica().equals("27"))
        	    || (tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) && lPosMod.getCodPosizioneGiuridica().equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM_POS_GIU)))
      {
        flagTemplate = "3"; // IN MISURA
      }
      else if(lPosMod.isLibero())
      {
        flagTemplate = "4";  // LIBERO (tutte le misure)
      }
    }

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("flagTemplate = "+flagTemplate);
    
    if(flagTemplate != null && lMotivo!= null && !lMotivo.equals("0000"))
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
    
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request

//Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}