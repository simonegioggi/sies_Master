package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;


/**
 * <p>Title: ActLoadTrasferisciTrasmissioneCompetenza</p>
 * <p>Description: prepara la pagina per Trasferire la richietsa di Trasmissione per Competenza Misure di Sicurezza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author d.f.
 * @version 1.0
 */

public class ActLoadTrasferisciTrasmissioneCompetenza extends ActionSiap implements ICostantiMisuraSicurezza
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
    
    UfficioModel ufficioDest = getUfficioByCodUfficio(lEveMod.getCodUfficioDestinatario());
    
    this.setRequestAttribute("eventoTrasm", lEveMod);
    this.setRequestAttribute("ufficioDest", ufficioDest);
    
    return PG_LOAD_TRASFERISCI_TRASMISSIONE_COMP;
  }
}
