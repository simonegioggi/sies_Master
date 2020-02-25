package siap.siep.modulocumulo.action;

import java.util.Date;
import java.util.Vector;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
* Action per la load Elenco dei Titoli possibili oggetto della Richieste al GE 
* di Revoca della Pena Principale
* 
* @author Intersistemi Italia S.p.A.
* 
*/

public class ActLoadElencoTitoliRichiestaGERevocaPenaCum extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo
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
    
    Vector <TitoloCumulatoModel> lListaTitoli = null;
    
    String lModalita = "I"; //default inserimento
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    siesLogger.debug("--XX-- Inizio - Modalita = "+lModalita);
    
    String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();
    IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    
    
    if(   isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI)
        || "T".equals(getRequestStringParameter(ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI))
       )
    {
      // La Query trova Titolo_Cumulato_Model e i suoi aggregati   Procedimento_Cumulato e Soggetto_Cumulato
      lListaTitoli = new Vector<TitoloCumulatoModel>(lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy(lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento) );
    }
    else
    {
    	Date lDataRea = getRequestDateParameter(CAMPO_ANNO_DATA_COMMESSO_REATO, CAMPO_MESE_DATA_COMMESSO_REATO, CAMPO_GIORNO_DATA_COMMESSO_REATO);
    	// La Query trova solo i Titolo_Cumulato_Model (che hanno i Reati con DataReato come Richiesto), e i suoi aggregati: Procedimento_Cumulato e Soggetto_Cumulato
    	lListaTitoli = new Vector<TitoloCumulatoModel>(lIstrCtrl.ExRicercaTitoliByIstruttoriaDataReatoCumOrderBy(lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento, lDataRea) );

    	setRequestAttribute("sceltaReato", "SI");
    	setRequestAttribute("AnnoReato", getRequestStringParameter(CAMPO_ANNO_DATA_COMMESSO_REATO));
    	setRequestAttribute("MeseReato", getRequestStringParameter(CAMPO_MESE_DATA_COMMESSO_REATO));
    	setRequestAttribute("GiornoReato", getRequestStringParameter(CAMPO_GIORNO_DATA_COMMESSO_REATO));
    }

    //--------------------------------------------------------------------------------------
    // Con una seconda query (per non appesantire la precedente query)
    // Aggiungo :  Reati_Cumulo, Pene_Accessorie_Cumulo e Misure_Sicutrzza_Cumulo 
    // -------------------------------------------------------------------------------------
    IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
    Vector<TitoloCumulatoModel> NewlistaTitoli = lCtrlRic.ExRicercaAggregatiAlTitolo(lListaTitoli);
    
    setRequestAttribute("ListaTitoli", NewlistaTitoli);
    setRequestAttribute("modalita", lModalita);
    
    return PG_ELENCO_TITOLI_RICG_GE_REVOCA_PENA;
  }
  
  
}	// Chiude Classe
