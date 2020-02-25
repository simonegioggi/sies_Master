package siap.siep.modulocumulo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

//import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.util.SIEPLookupRemote;

public class ActStampaComunicazioniAltre extends ActionSiap
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    UtenteModel lUtente = getUtenteConnesso();
    UfficioModel lUfficio = getUfficioUtenteConnesso();

    String lId = getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    String lIdNotifica = getRequestStringParameter(ICostantiNotifica.CAMPO_ID_NOTIFICA);


    IIstruttoriaCumulo lCtrlIC = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    IstruttoriaCumuloModel lIstCumMod = lCtrlIC.ExRicercaIstruttoriaCumuloById(new BigDecimal(lId));
    
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIstCumMod.getEveIdEventoProv());
    lEveMod.setEvento(lCtrl.ExRicercaEventoByKey(lIstCumMod.getEveIdEventoProv()));

    lEveMod.setNomeTemplate("SIEP_CUM_AA");
    lEveMod.getEvento().setTemIdTemplate ("SIEP_CUM_AA");

    lEveMod.getEvento().setDataAggiornamento         (DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
    lEveMod.getEvento().setCodOperatoreAggiornamento (getCodUtenteConnesso());
    
    lEveMod.getEvento().setFlagDocumentoRegistrato   ("N");

    IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
    ByteArrayOutputStream lReport = lCtrlDatiFinali.ExStampaComunicazioni(lEveMod, lFascicoloModel, lUtente, lUfficio, "N"+lIdNotifica);     

    
    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}