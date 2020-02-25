package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

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
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaMARevocaAffInProva</p>
 * <p>Description: Produce il documento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaMARevocaAffInProva
    extends ActionSiap
    implements ICostantiMisuraAlternativa
{

  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();

    String tipoMisura = this.getRequestStringParameter("tipoMisura");
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaModel lPosMod = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

    String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
    String lMotivo = lEventoModel.getCodMotivo();

    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    IMisuraAlternativa lCtrlMis = SICOLookupRemote.getMisuraAlternativaRemote();
    MisuraAlternativaModel lMisMod = lCtrlMis.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());

    String flagTemplate = null;

    if(lPosMod != null && lPosMod.getCodPosizioneGiuridica()!= null && tipoMisura != null )
    {
      if(  (tipoMisura.equals("AFFIDAMENTO") && (lPosMod.getCodPosizioneGiuridica().equals("32") || lPosMod.getCodPosizioneGiuridica().equals("37")))
         ||(tipoMisura.equals("INDULTINO") && (lPosMod.getCodPosizioneGiuridica().equals("35") || lPosMod.getCodPosizioneGiuridica().equals("40"))) 
        )
      {
        flagTemplate = "2"; // SOSP (Se in AFFI = SIEP_MA_REVO_AFFI_SOSP)
      }
      else if (   tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) 
               && (   lPosMod.getCodPosizioneGiuridica().equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_POS_GIU) 
                   || lPosMod.getCodPosizioneGiuridica().equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_POS_GIU)
                  )
              )
      {
      	if (lMisMod.getDataIngressoIstituto()!=null)
      		flagTemplate = "2"; // SOSP
      	else
      		flagTemplate = "4";
      }
      else if(   tipoMisura.equals("AFFIDAMENTO") 
              && lPosMod.getCodPosizioneGiuridica().equals("13")
              && lMisMod.getCodTipoUfficioScarcerazione() != null 
              && lMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
      { 
        flagTemplate = "2"; // eseguito SORV quindi DETENUTO ergo come se fosse in SOSP con data ingresso in istituto
      }
      else if(   tipoMisura.equals("AFFIDAMENTO") 
              && lPosMod.getCodPosizioneGiuridica().equals("54") // Affidamento Provvisorio
              && lMisMod.getCodTipoUfficioScarcerazione() != null 
              && lMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
      { 
        flagTemplate = "5";  // SIEP_MA_REVO_AFFI_PROVV_DET.RTF
      }
      else if(   (tipoMisura.equals("AFFIDAMENTO") && ( lPosMod.getCodPosizioneGiuridica().equals("13") || lPosMod.getCodPosizioneGiuridica().equals("54") )       )
              || (tipoMisura.equals("INDULTINO") && lPosMod.getCodPosizioneGiuridica().equals("27"))
        	    || (tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) && lPosMod.getCodPosizioneGiuridica().equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM_POS_GIU)))
      {
        flagTemplate = "3"; // in misura (già detenuto/non detenuto) (es SIEP_MA_REVO_AFFI_MISU.RTF)
      }
      else if(lPosMod.isLibero())
      {
        flagTemplate = "4";
      }
    }

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

    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request

//Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}