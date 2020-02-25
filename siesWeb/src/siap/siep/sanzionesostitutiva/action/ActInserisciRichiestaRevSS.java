package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActInserisciRichiestaRevSS</p> 
 * <p>Description: Classe Action per l'Inserimento della Richiesta Revoca SS
 * </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 3.1
 */

public class ActInserisciRichiestaRevSS extends ActionSiap implements ICostantiSanzioneSostitutiva
{


  public String processRequest() throws F3BException
  {
    // 
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();


    

    //======================================
    // Carico i dati dell'evento Richiesta
    //======================================
    EventoModel lEventoRichiesta = new EventoModel();

    lEventoRichiesta.setFasSieIdFascicoloSiep(lIdFascicolo);

    lEventoRichiesta.setCodTipoEvento("01");        //   01 - Provvedimento
    lEventoRichiesta.setCodTipoProvvedimento("26"); //   26 - Richiesta
    lEventoRichiesta.setCodMotivo(this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));  // Da combo Annotazione
    lEventoRichiesta.setCodEsito("-");
    
    lEventoRichiesta.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
    lEventoRichiesta.setCodLuogoEmittente(getCodComuneUtenteConnesso());  

    lEventoRichiesta.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
                                                              ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
                                                              ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

    lEventoRichiesta.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
    																 ICostantiNotifica.CAMPO_MESE_DATA_INVIO,
    																 ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));

    
    lEventoRichiesta.setFlagDocumentoRegistrato("N"); 
    lEventoRichiesta.setFlagStampaSiep("S");
    lEventoRichiesta.setFlagVideoSiep("S");
    

    if (!this.isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO )){
      lEventoRichiesta.setCodMagistrato(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
    }
    else {
      lEventoRichiesta.setCodMagistrato("-");
    }
    
    lEventoRichiesta.setCodLuogoDestinatario("-");
    lEventoRichiesta.setCodUfficioDestinatario("-");
    lEventoRichiesta.setCodTipoUfficioDestinatario("-");

    lEventoRichiesta.setCodOperatoreInserimento (getCodUtenteConnesso());
    lEventoRichiesta.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    lEventoRichiesta.setDataInserimento         (DateUtils.getSysDate());
    
    EventoNotificaModel lEvNotModel = new EventoNotificaModel();
    lEvNotModel.setEvento(lEventoRichiesta);
    
      
    //===========================================
    // Carico i dati delle notifiche (destinatari)
    //===========================================
   NotificaModel lNotMod = new NotificaModel();

    lNotMod.setCodEsito("-");
    lNotMod.setCodTipoNotifica("E"); // Esecuzione
    lNotMod.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
												 ICostantiNotifica.CAMPO_MESE_DATA_INVIO,
												 ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
    
    String lUfficio = null;
    lUfficio= this.getCodUfficioByCodTipoUfficioDescrComune(this.getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA),getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT));

    lNotMod.setUffCodUfficio(lUfficio);
    lNotMod.setNote(getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_NOTE ));
    
    lNotMod.setCodOperatoreInserimento (lEventoRichiesta.getCodOperatoreInserimento());
    lNotMod.setCodUfficioInserimento   (lEventoRichiesta.getCodUfficioInserimento());
    lNotMod.setDataInserimento         (lEventoRichiesta.getDataInserimento());

    
    NotificaModel[] lNotifiche = new NotificaModel[1];
    lNotifiche[0] = lNotMod;
   
    lEvNotModel.setNotifiche(lNotifiche);

    ISanzioneSostitutiva lSanzioneCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();   
    EventoNotificaModel lEventoNotInserito = lSanzioneCtrl.exInserisciRichiestaRevocaEspulsione(lEvNotModel);
    

    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRichiestaRevSS&"+
            ICostantiEvento.CAMPO_ID_EVENTO+"="+lEventoNotInserito.getEvento().getIdEvento().toString();
    return lPage;
    
  }
}