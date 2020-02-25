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
 * Action per la generazione della stampa della Comunicazione di Richiesta o 
 * Concessione di Amnistia/Indulto, Depenalizzazione e Incostituzionalità
 * Utilizzata sia nella Richieste al GE, sia nelle Decisioni del GE
 * 
 * @author 
 *
 */
public class ActStampaEmissioneComunicazioni
    extends ActionSiap
    implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();
    
    //==========================================================================
    // Recupero l'evento (comunicazione) e le notifiche associate (con le notifiche non ci fa nulla)
    //==========================================================================
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveMod = new EventoNotificaModel();
    lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

    String lMotivo = lEveMod.getEvento().getCodMotivo();

    //==========================================================================
    // Recupero il template
    //==========================================================================
    TemplateModel lTemMod = new TemplateModel();
    String flagTemplate = "0";
//nuova ricerca ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lTipoEvento,lTipoProv,lMotivo,lFlagTemplate);

    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "12", lMotivo, flagTemplate);

    lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
    
    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");
    
    //==========================================================================
    // Genera il documento di stampa
    //==========================================================================
    IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();

    ByteArrayOutputStream lReport = lCtrlAnn.ExStampaDocumentoXAnnotazioni(lEveMod, lUtenteMod); 

//Prepara la pagina di destinazione
//if (lReport != null)
    setRequestAttribute("report", lReport);
//    setRequestAttribute("fc", getRequestStringParameter("fc"));

    return IWebConstants.PG_DOWNLOAD;
  }
}