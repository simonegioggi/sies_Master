package siap.siep.ordineesecuzione.action;

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
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: </p>
 * <p>Description: Funzione per la stampa comunicazione variazione decorrenza 
 * scadenza pena questa causa
 * </p>
 * @author not attributable
 * @version 1.0
 * @since 3.1upd02
 */

public class ActStampaVariazioneDecorrenzaScadenzaQC extends ActionSiap implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws F3BException
  {
     String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);
     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
     UtenteModel lUtenteMod = this.getUtenteConnesso();
     
     EventoModel lEveMod = null;
     IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
     lEveMod = lEveCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
     
     EventoNotificaModel lEveNotMod = new EventoNotificaModel();

     lEveNotMod.getEvento().setIdEvento(new BigDecimal(lId));
     lEveNotMod.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );
     lEveNotMod.getEvento().setFlagDocumentoRegistrato("N");

     lEveNotMod.getEvento().setDescrLuogoEmittente(lUtenteMod.getUfficioUtente().getDescrComune());
     lEveNotMod.getEvento().setDescrUfficioEmittente(lUtenteMod.getUfficioUtente().getDescrTipoUfficio());
     
     lEveNotMod.getEvento().setCodOperatoreAggiornamento(lUtenteMod.getUserId());
     lEveNotMod.getEvento().setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
     lEveNotMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());

     
     //=========================================================================
     // Recupero il template per l'evento
     //=========================================================================
     TemplateModel lModTem = new TemplateModel();
     ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
//     lModTem = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "12", "0368", "0");
  
     lModTem = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEveMod.getCodTipoEvento(),
                                                                               lEveMod.getCodTipoProvvedimento(),
                                                                               lEveMod.getCodMotivo(),
                                                                               "0");
     
     
     lEveNotMod.setNomeTemplate(lModTem.getIdTemplate());
     

     //=========================================================================
     // Produco la stampa
     //=========================================================================
     IEvento lCtrl = SICOLookupRemote.getEventoRemote();
 		 ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveNotMod, lUtenteMod);		 

     setRequestAttribute("report", lReport);

     return IWebConstants.PG_DOWNLOAD;
  }
}
