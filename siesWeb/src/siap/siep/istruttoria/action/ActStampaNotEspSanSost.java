package siap.siep.istruttoria.action;

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
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaNotEspSanSost</p>
 * <p>Description: Stampa un Certificato</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: EUTELIA</p>
 * @author DB
 * @version 1.0
 */

public class ActStampaNotEspSanSost extends ActionSiap implements ICostantiIstruttoria
{
  public String processRequest() throws Exception
  {
     String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);

     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
     UtenteModel lUtenteMod = this.getUtenteConnesso();
     UfficioModel lUff = this.getUfficioUtenteConnesso();

     EventoNotificaModel lEveMod = new EventoNotificaModel();

     lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
     lEveMod.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );


     lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
     lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
     lEveMod.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
     lEveMod.getEvento().setFlagDocumentoRegistrato("N");

     
     IEvento lCtrl = SICOLookupRemote.getEventoRemote();
     EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
     String lMotivo = lEventoModel.getCodMotivo();
     
     ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
     TemplateModel lTemMod = new TemplateModel();
     lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("05","-",lMotivo,"0");
     lEveMod.setNomeTemplate(lTemMod.getIdTemplate());

     lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
     lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());

      ISanzioneSostitutiva lCtrlStampa = SIEPLookupRemote.getSanzioneSostitutivaRemote();
      ByteArrayOutputStream lReport = lCtrlStampa.exStampaSS(lEveMod, lUtenteMod); // setta la risposta nella request

      //Prepara la pagina di destinazione

     setRequestAttribute("report", lReport);

     return IWebConstants.PG_DOWNLOAD;
  }
}