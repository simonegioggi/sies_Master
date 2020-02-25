package siap.siep.calcolopena.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;

/**
 * Classe per la Load della Grigla dei provvedimenti collegati al computo
 * per 'Rideterminazione pena - Altro'
 *  
 * @author 
 * @since 4.0
 */

public class ActLoadGrigliaRidetPenaRidimLA extends ActionSiap implements ICostantiEvento, ICostantiCalcoloPena
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   *
   * @return jsp di visualizzazione del detteglio
   */
  public String processRequest() throws Exception
  {
    
    // id dell'evento di computo
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    
    //============================================
    // Ricerca evento di computo inserito
    //============================================
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoModel lEveComputo = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

    this.setRequestAttribute("aEventoComputo",lEveComputo);

    //==============================================
    // Ricerca la pena residua collegata al computo
    //==============================================
    IPenaResidua lCtrlPenaRes = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lPenRes = lCtrlPenaRes.ExRicercaPenaResiduaByIdEvento(lIdEvento);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("DENTRO ActLoadGrigliaRidetPenaRidimLA Penaresidua="+ lPenRes);

    setRequestAttribute("penaresidua", lPenRes);
    
    //==============================================
    // Ricerca l'eventuale fungibilità
    //==============================================
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("DENTRO ActLoadGrigliaRidetPenaRidimLA prima di fungibilità");
    IFungibilita lFungCtrl = SIEPLookupRemote.getFungibilitaRemote();
    FungibilitaModel lFunMod = lFungCtrl.ExRicercaFungibilitaByKeyEvento(lIdEvento);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("DENTRO ActLoadGrigliaRidetPenaRidimLA dopo fungibilità");
    
    setRequestAttribute("fungibilita", lFunMod);
    
    return PG_GRIGLIA_RIDETERMINAZIONE_PENA_RIDIM_LA;
  }

}
