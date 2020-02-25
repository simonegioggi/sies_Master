package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.richiesta.action.ICostantiRichiesta;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActInserisciRichiestaRevocaEspulsione</p> 
 * <p>Description: Classe Action per l'Inserimento della Richiesta Revoca Espulsione
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActInserisciRichiestaRevocaEspulsione extends ActionSiap implements ICostantiSanzioneSostitutiva
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * La funzione prevede l'inserimento di un unico evento (per ora):
   * 01-25-{da combo} Annotazione Mancata Espulsione
   * 
   */
  public String processRequest() throws F3BException
  {
    // 
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

//    //==========================================================================
//    // Recupero la Sanzione Sostitutiva In Sentenza
//    //==========================================================================
//    IPenaComplessiva lCtrlPenaComp = SIEPLookupRemote.getPenaComplessivaRemote();
//    PenaComplessivaSanzioneSostitutivaModel lPenaCompSanzModel = lCtrlPenaComp.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lIdFascicolo);
//    
//    SanzioneSostitutivaModel lSanzSost = lPenaCompSanzModel.getSanzioneSostitutiva();
    

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

    lEventoRichiesta.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
                                                                     ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
                                                                     ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

    
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
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lEventoRichiesta = "+lEventoRichiesta);
    
    

    EventoNotificaModel lEvNotModel = new EventoNotificaModel();
    lEvNotModel.setEvento(lEventoRichiesta);
    
      
    //===========================================
    // Carico i dati delle notifiche (destinatari)
    //===========================================
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Carico le notifiche");
    //==========================================================================
    // Notifica a Autorità di Polizia
    //==========================================================================
    NotificaModel lNotMod = new NotificaModel();

    lNotMod.setCodEsito("-");
    lNotMod.setCodTipoNotifica("E"); // Esecuzione
    lNotMod.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
                                                 ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
                                                 ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));
    
    String lUfficio = null;
    lUfficio= this.getCodUfficioByCodTipoUfficioDescrComune(this.getRequestStringParameter(ICostantiRichiesta.CAMPO_COD_UFFICIO),getRequestStringParameter(ICostantiRichiesta.CAMPO_SEDE_UFFICIO));

    lNotMod.setUffCodUfficio(lUfficio);
    lNotMod.setNote(getRequestStringParameter(ICostantiRichiesta.CAMPO_NOTE_UFFICIO));
    
    lNotMod.setCodOperatoreInserimento (lEventoRichiesta.getCodOperatoreInserimento());
    lNotMod.setCodUfficioInserimento   (lEventoRichiesta.getCodUfficioInserimento());
    lNotMod.setDataInserimento         (lEventoRichiesta.getDataInserimento());

    // Autorità esterna
    // non prevista, la notifica va al GE che DEVE esistere. E' sufficiente il campo
    // UFF_COD_UFFICIO della tabella NOTIFICA
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lNotMod = "+lNotMod);
    //
    NotificaModel[] lNotifiche = new NotificaModel[1];
    lNotifiche[0] = lNotMod;
   
    lEvNotModel.setNotifiche(lNotifiche);
    
    
	  //===========================
	  // Effettuo la registrazione 
    //===========================
	  //EventoNotificaModel lEventoNotInserito = null;
    ISanzioneSostitutiva lSanzioneCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
    
    EventoNotificaModel lEventoNotInserito = lSanzioneCtrl.exInserisciRichiestaRevocaEspulsione(lEvNotModel);
    
    //==============================================
    // Restituisco la pagina di Dettaglio
    //==============================================
    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRichiestaRevocaEspulsione&"+
            ICostantiEvento.CAMPO_ID_EVENTO+"="+lEventoNotInserito.getEvento().getIdEvento().toString();
    return lPage;
    
  }
}