package siap.sius.trasmissioneatti.action;

/**
 * <p>Title: ActTrasmissioneAtti</p>
 * <p>Description: Classe Azione di Trasmissione Atti SIUS
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 */
import siap.sico.decodifiche.model.ComuneModel;
//import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.model.NotificaModel;
//import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.trasmissioneatti.controller.ITrasmissioneAtti;
//import siap.sius.fascicolo.controller.FascicoloSiusController;
//import siap.sius.fascicolo.controller.IFascicoloSius;
//import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
//import f3b.web.RedirectTo;
import f3b.web.IWebConstants;

public class ActTrasmissioneAtti extends ActionSiap implements ICostantiTrasmissioneAtti
{
  public String processRequest() throws Exception
  {
    //Recupero l'utente e il Fascicolo SIUS dalla sessione
    UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

    //Verifico se l'ufficio è stato correttamente impostato
    String lCodiceUffDest = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO),getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO) );

    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_DESCR_COMUNE_UFFICIO )) );
    String lCodiceLuogoDest = lComMod.getCodComune();

    //Fase di caricamento dati per l'aggiornamento del fascicolo SIUS.

    //Caricamento Fascicolo_SIUS
    lFasGPMod.getFascicoloSiusModel().setCodStatoFascicolo("01");
    lFasGPMod.getFascicoloSiusModel().setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
    lFasGPMod.getFascicoloSiusModel().setCodOperatoreAggiornamento(lUtenteMod.getUserId());
    lFasGPMod.getFascicoloSiusModel().setDataAggiornamento(DateUtils.getSysDate());

    //Caricamento Generale Procedimento
    lFasGPMod.getGeneraleProcedimentoModel().setTipoDefinizione("01");
    lFasGPMod.getGeneraleProcedimentoModel().setDataDefinizione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE, CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
    lFasGPMod.getGeneraleProcedimentoModel().setDescrDefinizione(lCodiceUffDest);
    lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
    lFasGPMod.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(lUtenteMod.getUserId());
    lFasGPMod.getGeneraleProcedimentoModel().setDataAggiornamento(DateUtils.getSysDate());

    //Fase di caricamento dati per l'inserimento di EVENTO.
    //L'Id_Evento viene calcolato applicativamente nel controller.
    EventoNotificaModel lEveNotMod = new EventoNotificaModel();
    lEveNotMod.getEvento().setCodTipoEvento("01");
    lEveNotMod.getEvento().setCodTipoProvvedimento("04");
    lEveNotMod.getEvento().setCodMotivo(lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
    lEveNotMod.getEvento().setCodUfficioEmittente(lUtenteMod.getUfficioUtente().getCodUfficio());
    lEveNotMod.getEvento().setCodLuogoEmittente(lUtenteMod.getUfficioUtente().getCodComune());
    lEveNotMod.getEvento().setCodEsito("-");
    lEveNotMod.getEvento().setDataTrasmissioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE, CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
    lEveNotMod.getEvento().setCodUfficioDestinatario(lCodiceUffDest);
    lEveNotMod.getEvento().setCodTipoUfficioDestinatario(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO));
    lEveNotMod.getEvento().setCodLuogoDestinatario(lCodiceLuogoDest);
    lEveNotMod.getEvento().setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
    lEveNotMod.getEvento().setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
    lEveNotMod.getEvento().setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
    lEveNotMod.getEvento().setCodOperatoreInserimento(lUtenteMod.getUserId());
    lEveNotMod.getEvento().setDataInserimento(DateUtils.getSysDate());

    /*
    //Fase di caricamento dati per l'inserimento di PASSAGGIO_EVENTO.
    //L'Id_PassaggioEvento viene calcolato applicativamente nel controller.
    //L'EveIdEvento viene caricato nel controller in successione alla insert di Evento.
    lEveNotMod.getPassaggioEvento().setCodUfficioMittente(lUtenteMod.getUfficioUtente().getCodUfficio());
    lEveNotMod.getPassaggioEvento().setCodUfficioDestinatario(lCodiceUffDest);
    lEveNotMod.getPassaggioEvento().setDataInserimento(DateUtils.getSysDate());
    lEveNotMod.getPassaggioEvento().setDataTrasmissioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE, CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
    lEveNotMod.getPassaggioEvento().setAnnoFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getChiaveAnnoSIEP() == null ? null : lFasGPMod.getFascicoloSiusModel().getChiaveAnnoSIEP());
    lEveNotMod.getPassaggioEvento().setProgrFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getChiaveProgrSIEP() == null ? null : lFasGPMod.getFascicoloSiusModel().getChiaveProgrSIEP());
    lEveNotMod.getPassaggioEvento().setUfficioFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getChiaveUfficioSIEP() == null ? null : lFasGPMod.getFascicoloSiusModel().getChiaveUfficioSIEP());
    lEveNotMod.getPassaggioEvento().setAnnoFascicoloSius(lFasGPMod.getFascicoloSiusModel().getChiaveAnno());
    lEveNotMod.getPassaggioEvento().setProgrFascicoloSius(lFasGPMod.getFascicoloSiusModel().getChiaveProgr());
    lEveNotMod.getPassaggioEvento().setUfficioFascicoloSius(lFasGPMod.getFascicoloSiusModel().getChiaveUfficio());
    */
    //L'Id_Notifica viene calcolato applicativamente nel controller.
    //L'EveIdEvento viene caricato nel controller in successione alla insert di Evento.

    //Istanzio l'array di NotificaModel e il Model di Notifica da utilizzare.
    NotificaModel lNotifiche[] = new NotificaModel[1];
    NotificaModel lNot = new NotificaModel();

    lNot.setCodEsito("-");
    lNot.setCodTipoNotifica("T");
    lNot.setDataInvio(DateUtils.getSysDate());
    lNot.setNote(getRequestStringParameter(CAMPO_NOTE));
    lNot.setUffCodUfficio(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO));
    lNot.setDataInserimento(DateUtils.getSysDate());
    lNot.setCodOperatoreInserimento(lUtenteMod.getUserId());
    lNot.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
    lNotifiche[0] = lNot;

    //Inserisco l'array di Notifiche nell'Evento
    lEveNotMod.setNotifiche(lNotifiche);

    //TrasmissioneAttiController lCtrl = new TrasmissioneAttiController();
    ITrasmissioneAtti lCtrl = SIUSLookupRemote.getTrasmissioneAttiRemote();

    lEveNotMod = lCtrl.ExTrasmettiAtto(lFasGPMod, lEveNotMod);

    //Metto in sessione il fascicolo SIUS per consentire le funzionalità annesse.
    setRequestAttribute("fascicoloSiusGP", lFasGPMod);
    setSessionAttribute("fascicoloSiusGP", lFasGPMod);

    //restituisce la jsp di VIEW
    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.trasmissioneatti.action.ActLoadDettaglioTrasmAtti&"+CAMPO_ID_EVENTO+"="+lEveNotMod.getEvento().getIdEvento().toString();
  }
}
