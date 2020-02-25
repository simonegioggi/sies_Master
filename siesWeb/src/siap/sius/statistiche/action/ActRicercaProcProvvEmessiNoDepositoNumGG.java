package siap.sius.statistiche.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

//import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

public class ActRicercaProcProvvEmessiNoDepositoNumGG extends ActionSiap implements 
	ICostantiStatistiche {

    public String processRequest() throws Exception {
	    this.setLinkRitorno();
	    String lPagina = "1";
	    if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
	        lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		Date lDataEmissioneInizio 				                  = null; 
		Date lDataEmissioneFine 	     				          = null;
		Date lDataIscrizioneInizio 						          = null;
		Date lDataIscrizioneFine 					    	      = null;
		Date lDataFine                                            = null;
		Integer lNumeroGiorni                                     = null;
		RicercaProcedimentoModel          lRicercaProcedimento    = null;
		Collection<EveFasGepSogProvModel> lElencoPaginato         = null;
			
		//
		// Lettura dati dalla form
		//
		lDataEmissioneInizio = getRequestDateParameter( CAMPO_ANNO_DATA_EMISSIONE_INIZIO, 
														CAMPO_MESE_DATA_EMISSIONE_INIZIO, 
														CAMPO_GIORNO_DATA_EMISSIONE_INIZIO);

		lDataEmissioneFine = getRequestDateParameter( CAMPO_ANNO_DATA_EMISSIONE_FINE, 
													  CAMPO_MESE_DATA_EMISSIONE_FINE, 
													  CAMPO_GIORNO_DATA_EMISSIONE_FINE);

		lDataIscrizioneInizio = getRequestDateParameter( CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO, 
														 CAMPO_MESE_DATA_ISCRIZIONE_INIZIO, 
														 CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO );

		lDataIscrizioneFine = getRequestDateParameter( CAMPO_ANNO_DATA_ISCRIZIONE_FINE, 
													   CAMPO_MESE_DATA_ISCRIZIONE_FINE, 
													   CAMPO_GIORNO_DATA_ISCRIZIONE_FINE );

        lDataFine = getRequestDateParameter(CAMPO_ANNO_INIZIALE, 
                                            CAMPO_MESE_INIZIALE, 
                                            CAMPO_GIORNO_INIZIALE );

        lNumeroGiorni = getRequestIntParameter(CAMPO_NUMERO_GIORNI);

		lRicercaProcedimento = new RicercaProcedimentoModel();
		lRicercaProcedimento.setDataEmissioneInizio(lDataEmissioneInizio);
		lRicercaProcedimento.setDataEmissioneFine(lDataEmissioneFine);
		lRicercaProcedimento.setDataIscrizioneInizio(lDataIscrizioneInizio);
		lRicercaProcedimento.setDataIscrizioneFine(lDataIscrizioneFine);
		lRicercaProcedimento.setDataFine(lDataFine);
		lRicercaProcedimento.setNumeroGiorni(lNumeroGiorni);
		
		lRicercaProcedimento.setUtenteConnesso(getUtenteConnesso());
				
		IStatisticheSius lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
        lElencoPaginato = lCtrl.ExRicercaProcProvvEmessiNoDepNumGGPaginata(lRicercaProcedimento, Integer.parseInt(lPagina));
		
		// Paginazione.
	    BigDecimal lRecords;
	    if (isRequestParameterNullObj("CountRisultati"))
	        lRecords = lCtrl.ExGetNumRicercaProcProvvEmessiNoDepNumGG(lRicercaProcedimento);
	    else
	    	lRecords = getRequestBigDecimalParameter("CountRisultati");

	    setRequestAttribute("CountRisultati",lRecords);
	    setRequestAttribute(IWebConstants.NUM_PAGE,lPagina);
	    setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				
		setSessionAttribute("ricercaProcedimenti", lRicercaProcedimento);
		setRequestAttribute("elencoProcedimenti", lElencoPaginato);
    	
        return PG_RICERCA_PROC_PROVV_EMESSI_NO_DEPOSITO_NUMGG;
    }
}