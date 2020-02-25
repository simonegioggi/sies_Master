package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;


/**
 * <p>Title: ActLoadTrasferisciSollecitoRichiestaAttiTrasfCompCumulo</p>
 * <p>Description: prepara la pagina per Trasferire il sollecito Richiesta Atti </p>
 */

public class ActLoadTrasferisciSollecitoRichiestaAttiTrasfCompCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo, ICostantiMessaggio
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
    
    return PG_LOAD_TRASFERISCI_SOLLECITO_RICHIESTA_ATTI_TC_CUMULO;
  }
}
