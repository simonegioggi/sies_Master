package siap.sius.statistiche.action;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import siap.sius.ActionSius;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
//import f3b.log.LogF3B;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActRicercaProcPerProvvNoValidatiNoDeposito </p>
 * <p>Description: Classe Action per la ricerca dei procedimenti per provvedimenti non validati 
 * o depositati 
 * da Anno, Num e codice Ufficio di Inserimento.</p>
 * <p>La classe è stata ottenuta specializzando la ActRicercaDepositoOrdinanzaPc 
 * per riutilizzare la stessa funzione per la ricerca dei dati da inserire in session.</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Intersistemi </p>
 * @version 1.0
 */
public class ActRicercaProcPerProvvNoValidatiNoDeposito extends ActionSius implements ICostantiStatistiche {
	public String processRequest() throws Exception {

	    this.setLinkRitorno();
	    String lPagina = "1";
	    if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
	        lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
	    
	    int lStatoProcedimento = Integer.parseInt(ICostantiStatistiche.VALUE_RICERCA_PER_STATO_PROVVEDIMENTO_NO);
	    BigDecimal lAnnoIni = null;
	    BigDecimal lNumIni = null;
	    BigDecimal lAnnoFine = null;
	    BigDecimal lNumFine = null;
	    Date lDataDepositoIni = null;
	    Date lDataDepositoFine = null;
	    RicercaProcedimentoModel lRicercaModel = null;
	    IStatisticheSius lCtrl = null;
	    Collection<EveFasGepSogProvModel> lElenco;
        BigDecimal lRecords = null;
	    
	    lStatoProcedimento = getRequestIntParameter(ICostantiStatistiche.RADIO_RICERCA_PER_STATO_PROVVEDIMENTO);
	    lAnnoIni = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_INI);
	    lNumIni = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_NUM_INI);
	    lAnnoFine = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_FINE);
        lNumFine = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_NUM_FINE);
        
        lDataDepositoIni = getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI, 
                                                   ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI, 
                                                   ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI);

        lDataDepositoFine = getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE, 
                                                    ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE, 
                                                    ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE);
        
        lRicercaModel = new RicercaProcedimentoModel();
        
        lRicercaModel.setStatoProcedimento(lStatoProcedimento);
        lRicercaModel.setAnnoInizio(lAnnoIni);
        lRicercaModel.setNumeroInizio(lNumIni);
        lRicercaModel.setAnnoFine(lAnnoFine);
        lRicercaModel.setNumeroFine(lNumFine);
        lRicercaModel.setDataDepositoInizio(lDataDepositoIni);
        lRicercaModel.setDataDepositoFine(lDataDepositoFine);
        lRicercaModel.setUtenteConnesso(getUtenteConnesso());
        
        lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
        lElenco =  lCtrl.ExRicercaProcPerProvvNoValidatiNoDepositoPaginata(lRicercaModel, Integer.parseInt(lPagina));
		
		// Paginazione.
	    if (isRequestParameterNullObj("CountRisultati")) 
	        lRecords = lCtrl.ExGetNumRicercaProcPerProvvNoValidatiNoDeposito(lRicercaModel);
	    else 
	    	lRecords = getRequestBigDecimalParameter("CountRisultati");
	    
        setRequestAttribute("CountRisultati",lRecords);
        setRequestAttribute(IWebConstants.NUM_PAGE,lPagina);
        setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
        
        setSessionAttribute("ricercaProcedimenti", lRicercaModel);
        setRequestAttribute("elencoProcedimenti", lElenco);

		return PG_RICERCA_PROC_PROVV_NO_VALIDATI_NO_DEP;
	}
}