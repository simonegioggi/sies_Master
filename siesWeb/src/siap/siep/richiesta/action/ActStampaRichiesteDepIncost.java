package siap.siep.richiesta.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
*
* <p>Title: ActStampaRichiesteDepIncost </p>
* <p>Description: Action per la stampa delle Richieste di Depenalizzazione/Incostutizionalità
*    e restituzione ordine di esecuzione</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: </p>
*/
public class ActStampaRichiesteDepIncost extends ActionSiap
                                              implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {
     UtenteModel lUtenteMod = this.getUtenteConnesso();
     UfficioModel lUff = this.getUfficioUtenteConnesso();
     
     // Ricerca EventoNotifica
     BigDecimal lIdEvento = this.getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO );
     EventoNotificaModel lEveMod = new EventoNotificaModel();
     IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
     lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

     //===============================================
     // Recupero il template
     //===============================================
     String  flagTemplate ="0";

     TemplateModel lTemMod = new TemplateModel();
     ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
     lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01",lEveMod.getEvento().getCodTipoProvvedimento(),lEveMod.getEvento().getCodMotivo(),flagTemplate);

     lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
     
     lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
     lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
     lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
     lEveMod.getEvento().setFlagDocumentoRegistrato("N");

     //===============================================
     // Produco la stampa 
     //===============================================
     IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
     ByteArrayOutputStream lReport = lCtrlAnn.ExStampaDocumentoXAnnotazioni(lEveMod, lUtenteMod);

     setRequestAttribute("report", lReport);

     return IWebConstants.PG_DOWNLOAD;
  }
}