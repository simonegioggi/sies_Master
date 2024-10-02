package siap.sius.statistiche.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * 
 * @author d.fiorletta
 * @since MEV_2019-09
 */
public class ActRicercaAttiIstruttoriDataRest extends ActionSius implements ICostantiStatistiche {
	public String processRequest() throws Exception {

	    this.setLinkRitorno();
	    String lPagina = "1";
	    if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
	        lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
	    
	    BigDecimal lAnnoIni = null;
	    BigDecimal lNumIni = null;
	    BigDecimal lAnnoFine = null;
	    BigDecimal lNumFine = null;
	    Date lDataDepositoIni = null;
	    Date lDataDepositoFine = null;
	    
	    RicercaProcedimentoModel lRicercaModel = null;

	    Collection<EveFasGepSogProvModel> lElenco;
        BigDecimal lRecords = null;
	    
	    lAnnoIni = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_INI);
	    lNumIni = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_NUM_INI);
	    lAnnoFine = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_FINE);
        lNumFine = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_NUM_FINE);
        
        // Sarebbe la data Iscrizione
        lDataDepositoIni = getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI, 
                                                   ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI, 
                                                   ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI);

        lDataDepositoFine = getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE, 
                                                    ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE, 
                                                    ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE);
        
	    Date lDataRestituzioneIni = null;
	    Date lDataRestituzioneFine = null;
	    lDataRestituzioneIni = getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_DATA_RESTITUZIONE_INI, 
                                                       ICostantiStatistiche.CAMPO_MESE_DATA_RESTITUZIONE_INI, 
                                                       ICostantiStatistiche.CAMPO_GIORNO_DATA_RESTITUZIONE_INI);

	    lDataRestituzioneFine = getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_DATA_RESTITUZIONE_FINE, 
                                                        ICostantiStatistiche.CAMPO_MESE_DATA_RESTITUZIONE_FINE, 
                                                        ICostantiStatistiche.CAMPO_GIORNO_DATA_RESTITUZIONE_FINE);
        
	    //
        lRicercaModel = new RicercaProcedimentoModel();
        
        lRicercaModel.setAnnoInizio(lAnnoIni);
        lRicercaModel.setNumeroInizio(lNumIni);
        lRicercaModel.setAnnoFine(lAnnoFine);
        lRicercaModel.setNumeroFine(lNumFine);
        
        lRicercaModel.setDataDepositoInizio(lDataDepositoIni);
        lRicercaModel.setDataDepositoFine(lDataDepositoFine);
        
        lRicercaModel.setDataRestituzioneInizio(lDataRestituzioneIni);
        lRicercaModel.setDataRestituzioneFine(lDataRestituzioneFine);
        
        lRicercaModel.setUtenteConnesso(getUtenteConnesso());
        
	    IStatisticheSius lCtrl = null;
        lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
        lElenco =  lCtrl.ExRicercaAttiIstruttoriDataRestPaginata(lRicercaModel, Integer.parseInt(lPagina));
		
		// Paginazione.
	    if (isRequestParameterNullObj("CountRisultati")) 
	        lRecords = lCtrl.ExGetNumRicercaAttiIstruttoriDataRestPaginata(lRicercaModel);
	    else 
	    	lRecords = getRequestBigDecimalParameter("CountRisultati");
	    
        setRequestAttribute("CountRisultati",lRecords);
        setRequestAttribute(IWebConstants.NUM_PAGE,lPagina);
        setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
        
        setSessionAttribute("ricercaAttiIstruttori", lRicercaModel);
        setRequestAttribute("elencoProcedimenti", lElenco);

		return PG_RICERCA_ATTI_ISTRUTTORI_DATA_REST;
	}
}
