package siap.siep.penapecuniaria.action;
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
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
  * <p>Title: ActStampaTrasmissioneConversione</p>
  * <p>Description: Produce il documento</p>
  * <p>Copyright: Copyright (c) 2007</p>
  * <p>Company: </p>
  * @author not attributable
  * @version 1.0
  */

public class ActStampaTrasmAnnotazioneProvvedimentoSor extends ActionSiap
{

  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

//ricerca evento
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(lIdEvento);

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
  //  lEveMod.getEvento().setCodTipoEvento(lEventoModel.getCodTipoEvento());
  //  lEveMod.getEvento().setCodTipoProvvedimento(lEventoModel.getCodTipoProvvedimento());
  //  lEveMod.getEvento().setCodMotivo(lEventoModel.getCodMotivo());
    
    String Esi = lEventoModel.getCodEsito();
    String Flag = "0";
    
    if(Esi.equals("0148") || Esi.equals("0149") || Esi.equals("0150") || Esi.equals("0151") ||
       Esi.equals("0152") || Esi.equals("0153") || Esi.equals("0154") || Esi.equals("0155")
       || Esi.equals("0002") || Esi.equals("0003") || Esi.equals("0004") || Esi.equals("0005")
       )
    {
      // Esiti N.L.P. -  SIEP_COMUNICAZIONE_NON_PROVVEDERE.RTF
      Flag = "1";
    }

//cerco il documento
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    TemplateModel lTemMod = new TemplateModel();
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoModel.getCodTipoEvento(),lEventoModel.getCodTipoProvvedimento(),lEventoModel.getCodMotivo(),Flag);
  
    lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
 

//produce la stampa    
    IRichiestaConversione lCtrlStampa = SIEPLookupRemote.getRichiestaConversioneRemote();
    ByteArrayOutputStream lReport = lCtrlStampa.exStampaCP(lEveMod, lUtenteMod); 
    setRequestAttribute("report", lReport);
  

    return IWebConstants.PG_DOWNLOAD;
  }
}