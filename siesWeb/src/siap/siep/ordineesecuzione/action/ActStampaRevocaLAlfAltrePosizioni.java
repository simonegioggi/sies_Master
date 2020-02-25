package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.motivoevento.controller.IMotivoEvento;
import siap.siep.motivoevento.model.MotivoEventoModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaRevocaLAlfAltrePosizioni extends ActionSiap implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();

    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

    EventoNotificaModel lEveMod = new EventoNotificaModel();

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

    lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    String lTemplate = null;

    //ricerca motivo_evento
    IMotivoEvento lCtrlMotivoEvento = SIEPLookupRemote.getMotivoEventoRemote();
    MotivoEventoModel lMotivoEveMod = new MotivoEventoModel();
    lMotivoEveMod = lCtrlMotivoEvento.ExRicercaMotivoEventoByEveIdEvento(new BigDecimal(lId));


    if (lMotivoEveMod != null && lMotivoEveMod.getCodMotivoRevoca() != null &&
        lMotivoEveMod.getCodMotivoRevoca().equals("0002")) // reiezione
    {
     if(lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().isLibero())
      {
        lTemplate = "0";
      }
      else if(lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null &&
              (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("01") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
               || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("03") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("53")
               || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("85") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("86")
               || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("87") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("73")
               || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("70") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("71")
               || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("72") )
              )
      {
        lTemplate = "0";
      }
    }
    else if (lMotivoEveMod != null && lMotivoEveMod.getCodMotivoRevoca() != null &&
        lMotivoEveMod.getCodMotivoRevoca().equals("0003"))//omessa
    {
        if(lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().isLibero())
        {
          lTemplate = "0";
        }
        else if(lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null &&
                (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("01") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("03") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("53")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("85") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("86")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("87") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("73")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("70") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("71")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("72") )
        		)
        {
          lTemplate = "0";
        }
      }
    else if (lMotivoEveMod != null && lMotivoEveMod.getCodMotivoRevoca() != null &&
        lMotivoEveMod.getCodMotivoRevoca().equals("0001"))//revoca pm
    {
        if(lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().isLibero())
        {
          lTemplate = "0";
        }
        else if(lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null &&
                (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("01") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("03") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("53")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("85") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("86")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("87") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("73")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("70") || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("71")
              	 || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("72") )
        		)
        {
          lTemplate = "0";
        }
      }

    TemplateModel lTemMod = new TemplateModel();
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

    if(lTemplate != null && lTemplate!= null && lEventoModel.getCodMotivo()!= null && !lEventoModel.getCodMotivo().equals("0000"))
    {
       lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoModel.getCodTipoEvento(),lEventoModel.getCodTipoProvvedimento(), lEventoModel.getCodMotivo(),
         lTemplate);
       lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
    }
    else
    {
       lEveMod.setNomeTemplate(TEMPLATE_ALTRE_POSIZIONI_VUOTO);
    }

    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request

    setRequestAttribute("report", lReport);
    setRequestAttribute("fc", getRequestStringParameter("fc"));

    return IWebConstants.PG_DOWNLOAD;
  }
}