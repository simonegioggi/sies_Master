package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.log.LogF3B;

import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.controller.ReatoContinuazioneCumuloController;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;

import siap.siep.cumulo.action.ICostantiCumulo;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
* <p>Title: ActRicercaReatoCumulo</p>
* <p>Description: Classe Action per la ricerca di Reato </p>
* <p>  Legato ad un certo titolo CUMULATO (CUMULO) </p> 
* @version 4.0
*/

public class ActRicercaReatoCumulo extends ActionModuloCumulo implements  ICostantiReatoCumulo,ICostantiCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws Exception
  {
    //==========================================================================
    //
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();   

    BigDecimal lIdTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
    
    //===============================================
    // Reati Cumulati
    //===============================================
    ReatoCumuloModel lReaCumMod = new ReatoCumuloModel(); 
    lReaCumMod.setTitIdTitoloCumulato (lIdTitolo);
    IReatoCumulo lCtrlR = SIEPLookupRemote.getReatoCumuloRemote();
    Vector<ReatoCumuloModel> lReaVect = lCtrlR.ExRicercaReatoCumulo(lReaCumMod);
    
    for (int i = 0; i < lReaVect.size(); i++)
    {
    	lReaCumMod = lReaVect.get(i);
    	siesLogger.debug("Reato Cumulo Model = "+i+ " "+lReaCumMod);
    }
    
    setRequestAttribute("reati", lReaVect);
    
    //=========================================================
    // Circostanze Aggravanti Soggettive e attenuanti Cumulate
    //=========================================================
    CircostanzaCumuloModel lCirCumMod = null;
    ICircostanzaCumulo lCtrlC = SIEPLookupRemote.getCircostanzaCumuloRemote();

    Vector<CircostanzaCumuloModel> lVectCirc = lCtrlC.ExRicercaCircostanzaCumulobyTitolo(lIdTitolo);
  
    for (int i = 0; i < lVectCirc.size(); i++)
    {
    	lCirCumMod = (CircostanzaCumuloModel) lVectCirc.get(i);
    	siesLogger.debug("CirCostanza Cumulo Model = "+i+ " "+lCirCumMod);
    }
  
    setRequestAttribute("circostanzeCum", lVectCirc);
    
    //===============================================
    // Calcola le continuazioni a partire dai reati
    //===============================================
    ReatoContinuazioneCumuloController lRCtrl = new ReatoContinuazioneCumuloController();
    setRequestAttribute("continuazioni", lRCtrl.getTableContinuazioni(lReaVect));

    if(lReaVect.size()> 0)
    {  //FIXME perchè in session?????
      setSessionAttribute("reato", lReaVect.get(0));
    }

    return PG_RICERCAREATO_CUM;
  }
}
