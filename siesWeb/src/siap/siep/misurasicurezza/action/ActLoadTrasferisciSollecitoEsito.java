package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;


/**
 * <p>Title: ActLoadTrasferisciSollecitoEsito</p>
 * <p>Description: prepara la pagina per Trasferire il sollecito Misure di Sicurezza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author d.f.
 * @version 1.0
 */

public class ActLoadTrasferisciSollecitoEsito extends ActionSiap implements ICostantiMisuraSicurezza
{
  
  public String processRequest() throws Exception
  {
    BigDecimal lIdEvento =  getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    
    //==========================================================================
    // Verifico se fascicolo di competenza
    //==========================================================================
    this.isFascicoloSiepDiCompetenza();
    
    //==========================================================================
    // Ricerca evento per recuperare il destinatario da visualizzare in maschera
    //==========================================================================
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    
    EventoModel lEveMod = new EventoModel();    
    
    lEveMod = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
    
    
    //=========================================================
    // Recupero i dati del sollecito
    //=========================================================
    ISollecitoEsitoTrasmissione lCtrlSollecito = SIEPLookupRemote.getSollecitoEsitoTrasmissioneRemote();
    SollecitoEsitoTrasmissioneModel lSollecitoModel = lCtrlSollecito.ExRicercaSollecitoEsitoTrasmissioneByIdEvento(lEveMod.getIdEvento());
    this.setRequestAttribute("sollecitoEsito", lSollecitoModel);
    
    UfficioModel ufficioDest = getUfficioByCodUfficio(lSollecitoModel.getCodUffSollecitato());
    
    this.setRequestAttribute("eventoSollecito", lEveMod);
    this.setRequestAttribute("ufficioDest", ufficioDest);
    
    return PG_LOAD_TRASFERISCI_SOLLECITO_ESITO;
  }
}
