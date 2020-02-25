package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load jsp di trasferimento telematico richiesta atti
 * 
 * @author d.fiorletta
 * @since 06/2015
 */
public class ActLoadTrasferisciRichiestaTrasmissioneTitolo extends ActionSiap implements ICostantiModuloCumulo
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
    EventoModel lEveMod = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
    
    // Recupero i dati della COMPETENZA
    ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();  
    CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);     
    setRequestAttribute("competenza", mComp);   
    
    UfficioModel ufficioDest = getUfficioByCodUfficio(mComp.getChiaveUfficio());
    
    this.setRequestAttribute("eventoTrasm", lEveMod);
    this.setRequestAttribute("ufficioDest", ufficioDest);
    
    return PG_LOAD_TRASFERISCI_RICHIESTA_TRASMISSIONE_TITOLO;
  }
  
  
}
