package siap.siep.pagoPA.action;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;

public class ActLoadVerificaErroriPagopa extends ActionSiap implements ICostantiErroriSiesPagopa {
  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws Exception {

    siesLogger.debug(getClass().getName() + ".processRequest: inizio");      

    removeSessionAttribute(ICostantiErroriSiesPagopa.CRITERI_RICERCA_SESSION_OBJ);
    removeSessionAttribute(ICostantiErroriSiesPagopa.CRITERI_RICERCA_SESSION_CURRENT_PAGE);
    
    Collection <DecodificheModel> lListaTipoEvento = new Vector<>();
    
    lListaTipoEvento.add(new DecodificheModel("-", "-", "-", "", "", "", "", "", ""));
    lListaTipoEvento.add(new DecodificheModel(ICostantiErroriSiesPagopa.DESC_ACTION_VERIFICA
                                            , ICostantiErroriSiesPagopa.DESC_FUNZIONE_VERIFICA, "", "", "", "", "", "", ""));
    lListaTipoEvento.add(new DecodificheModel(ICostantiErroriSiesPagopa.DESC_ACTION_RICHIESTA
                                            , ICostantiErroriSiesPagopa.DESC_FUNZIONE_RICHIESTA, "", "", "", "", "", "", ""));

    Option lOptionTipoEvento = new Option(lListaTipoEvento, "-");
    setRequestAttribute("TipoEvento", lOptionTipoEvento.toString());
    
    return ICostantiErroriSiesPagopa.PG_LOAD_VERIFICA_ERRORI;
  }
}