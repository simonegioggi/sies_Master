package siap.siep.sospensione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaSospensioneArt47</p>
 * <p>Description: Classe Action per la Stampa di Decreto Sospensione Art. 47 </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActStampaSospensioneArt47
    extends ActionSiap
    implements ICostantiSospensione
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel  lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff       = this.getUfficioUtenteConnesso();

    String lId = getRequestStringParameter("IdEvento");

   
    //==========================================================================
    //  Perchè carica un EventoNotificaModel invece di un model semplice?????
    //==========================================================================
    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento        (DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento  (lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    //==========================================================================
    //  Recupero il decreto_ordinanza_siep
    //==========================================================================
    IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
    DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd.ExRicercaUltimaDecretoOrdinanzaSiepByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

    
    //==========================================================================
    // impostare template
    // ??? nel caso del differimento il flag template sembra inutile in quanto
    // tipo evento, tipo provvedimento e motivo provvedimento dell'evento, già
    // identificano in modo unico il template
    //==========================================================================
    String flagTemplate = null;
    if (!this.isRequestParameterNullObj("tipo")) // SE VIENE DAL DIFFERIMENTO
    {
      if (lDecOrd.getCodTipoAutoritaEmittente().equals("UDS"))
      {
        if (lDecOrd.getFlagScarcerareScarcerato().equals("D"))
        {
          flagTemplate = "0";
        }
        else
        {
          flagTemplate = "1";
        }
      }
      else if (lDecOrd.getCodTipoAutoritaEmittente().equals("TDS"))
      {
        if (lDecOrd.getFlagScarcerareScarcerato().equals("D"))
        {
          flagTemplate = "2";
        }
        else
        {
          flagTemplate = "3";
        }
      }
    }
    else
    {
      if (lDecOrd.getFlagScarcerareScarcerato().equals("D"))
      {
        flagTemplate = "0";
      }
      else
      {
        flagTemplate = "1";
      }
    }

    //==========================================================================
    // Recupero l'evento e quindi il template associato 
    //==========================================================================
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    TemplateModel lTemMod = new TemplateModel();
 //   lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "04", lEventoModel.getCodMotivo(), flagTemplate);
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(), lEventoModel.getCodMotivo(), flagTemplate);
    
    
    lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
    
    //==========================================================================
    // Genero la stampa
    //==========================================================================
    ISospensione lctrSosp = SIEPLookupRemote.getSospensioneRemote();
    ByteArrayOutputStream lReport = lctrSosp.ExStampaDocumentoSospensioni(lEveMod, lUtenteMod); // setta la risposta nella request

    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}