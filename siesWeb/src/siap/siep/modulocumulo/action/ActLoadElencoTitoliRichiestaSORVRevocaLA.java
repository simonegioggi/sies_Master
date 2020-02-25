package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
* Action per la load Elenco dei Titoli possibili oggetto delle Richieste al SORV 
* di Richiesta Revoca Liberazione Anticipata
* 
* @author Intersistemi Italia S.p.A.
* 
*/

public class ActLoadElencoTitoliRichiestaSORVRevocaLA extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws Exception
  {
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())){
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "L'istruttoria risulta chiusa. Non è possibile procedere all'emissione di ulteriori richieste");
      return IWebConstants.PG_MESSAGE;
    }    
    
    String lModalita = "I"; //default inserimento
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    siesLogger.debug("--XX-- Inizio - Modalita = "+lModalita);
    
    RichiestePmInCumuloModel lRichiestaModel = null;
    
    if ("I".equals(lModalita)){
      // Inserimento      
    }
    else if ("M".equals(lModalita)){
      // Modifica 
      BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
      //RichiestaAlGE
      IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
      lRichiestaModel = lCtrlRich.ExRicercaRichiestePmInCumuloById(lId);
      setRequestAttribute("RichiestaAlGE", lRichiestaModel);      
    }
    else {
      //Rilanciare Eccezione - Operazione non supportata
    }
    
    // Precarico l'elenco dei titoli con beneficio
    this.getElencoTitoli (lIstruttoriaModel);
    
    setRequestAttribute("modalita", lModalita);
    
    return PG_ELENCO_TITOLI_RIC_SORV_REV_LA;
  }
  
  /**
   * Recupera la lista dei Titoli su cui applicare i benefici di Amnistia/Indulto
   * 
   */
  private void getElencoTitoli (IstruttoriaCumuloModel aIstruttoriaModel) throws Exception 
  {
    String lOrdinamento = aIstruttoriaModel.getOrdinamentoTitoli();
    //------------------------------------------------------------------------------------------------------
    // La Query trova Titolo_Cumulato_Model e i suoi aggregati   Procedimento_Cumulato e Soggetto_Cumulato
    //------------------------------------------------------------------------------------------------------
    IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    Vector <TitoloCumulatoModel> lListaTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy (aIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento);
    
    //--------------------------------------------------------------------------------------
    // Con una seconda query (per non appesantire la precedente query)
    // Aggiungo :  Liberazione_Anticipata 
    // -------------------------------------------------------------------------------------
    IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
    Vector<TitoloCumulatoModel> NewlistaTitoli = lCtrlRic.ExCaricaLibAntDelTitolo(lListaTitoli);
    
    setRequestAttribute("ListaTitoli", NewlistaTitoli);
    
  } // Chiude Metodo getElencoTitoli()
  
}	// Chiude Classe
