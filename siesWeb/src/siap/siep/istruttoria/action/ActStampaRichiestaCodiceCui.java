package siap.siep.istruttoria.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.controller.IIstruttoria;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
/**
 * <p>Title: ActStampaRichiestaCodiceCui</p>
 * <p>Description: Stampa Informazioni Richiesta Codice CUI</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaRichiestaCodiceCui extends ActionSiap implements ICostantiIstruttoria
{
  public String processRequest() throws Exception
  {
     // preleva l'ID dell'evento
     String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);

     // Preleva dalla Session l'ID del fascicolo
     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
     UtenteModel lUtenteMod = this.getUtenteConnesso();

     // Istanzia il Model dell'evento
     EventoNotificaModel lEveMod = new EventoNotificaModel();

     // Carica nel model l'ID dell'evento e l'id fascicolo
     lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
     lEveMod.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

     // UfficioModel lUff = this.getUfficioUtenteConnesso();

     // Carica nel model il codice utente connesso il flag se il docuemento è registrato
     lEveMod.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
     lEveMod.getEvento().setFlagDocumentoRegistrato("N");

     // Ricerca Evento
     IEvento lCtrl = SICOLookupRemote.getEventoRemote();
     EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
     
     // Ricerca nella tabella TEMPLATE il Template per la stampa
     ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
     TemplateModel lTemMod = new TemplateModel();
     
     // Prendo il valore del FlagTemplate in base a quanto scelto sul form dettaglio     
     //lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoModel.getCodTipoEvento(), null,lEventoModel.getCodMotivo(),"0");
     lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoModel.getCodTipoEvento(), null,lEventoModel.getCodMotivo(),getRequestStringParameter("ListaTemplate"));

     lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
     
     IIstruttoria lCtrlIstruttoria = SIEPLookupRemote.getIstruttoriaRemote();
     //rtf
     ByteArrayOutputStream lReport = lCtrlIstruttoria.ExStampaIstruttoria(lEveMod, lUtenteMod);	 // setta la risposta nella request

      //Prepara la pagina di destinazione

     setRequestAttribute("report", lReport);

     return IWebConstants.PG_DOWNLOAD;
  }
}