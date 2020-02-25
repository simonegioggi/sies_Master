package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.log.LogF3B;
import f3b.util.F3BException;


/**
 * <p>Title: ActLoadDettaglioRichiestaRevocaEspulsione</p> 
 * <p>Description: Classe Action per la Load del Dettaglio Richiesta al GE di
 * Revoca Espulsione.
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Eutelia</p>
 * @since 3.0
 * @version 1.0
 */

public class ActLoadDettaglioRichiestaRevocaEspulsione extends ActSIESDettaglioProvvedimento implements ICostantiSanzioneSostitutiva
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    // Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    //==========================================================================
    // Recupero l'id dell'evento passato sulla request 
    //==========================================================================
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    
    EventoNotificaModel lEvNotModel = null;
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    lEvNotModel = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lEvNotModel = "+lEvNotModel);
    
    //=========================================
    // Carico la pena Residua 
    //=========================================
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Carico la pena Residua");
    PenaResiduaModel lPenResMod = null;
    IPenaResidua lCtrlPenaResidua = SIEPLookupRemote.getPenaResiduaRemote();
    lPenResMod = lCtrlPenaResidua.ExRicercaPenaResiduaByIdEvento(lIdEvento); 
    
    //============================================
    // Ricerca Magistrato firmatario
    //============================================
    IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
    MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEvNotModel.getEvento().getCodMagistrato());

    setRequestAttribute("magistrato", lMagi);
    
    //==========================================================
    // Passo i dati alla form di visualizzazione del dettaglio 
    //==========================================================
    setRequestAttribute("aEveNotRichiesta", lEvNotModel);
    setRequestAttribute("aPenaResidua", lPenResMod);
    
    //==========================================
    // Recupero la posizione giuridica (quale?) 
    //==========================================
    if (   lEvNotModel.getEvento().getFlagDocumentoRegistrato()==null
        || lEvNotModel.getEvento().getFlagDocumentoRegistrato().equals("N")
       )
    { // n.b. recupero la posizione corrente solo se sto inserendo il provvedimento
      //      mentre se sto visualizzando un provvedimento già validato non 
      //      ha senso recuperare la posizione corrente. 
      IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
      PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);
      setRequestAttribute("aPosizioneluogoaltra", lPosLuoAltr);
    }
    
	  return PG_LOAD_DETTAGLIO_RICHIESTA_REVOCA_ESPULSIONE;
  }
}