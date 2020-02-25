package siap.sius.statistiche.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

//import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

public class ActRicercaProcDataUdienzaFissataNoDefinitiNumGG extends ActionSiap implements 
	ICostantiStatistiche {

    public String processRequest() throws Exception {
	    this.setLinkRitorno();
	    String lPagina = "1";
	    if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
	        lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		Date lDataCameraConsiglioInizio 				      = null; 
		Date lDataCameraConsiglioFine 					      = null;
		Date lDataIscrizioneInizio 						      = null;
		Date lDataIscrizioneFine 						      = null;
		Date lDataFine                                        = null;
		Integer lNumeroGiorni                                 = null;
		RicercaProcedimentoModel      lRicercaProcedimento    = null;
		Collection<EveFasGepSogModel> lElencoPaginato         = null;
			
		//
		// Lettura dati dalla form
		//
		lDataCameraConsiglioInizio = getRequestDateParameter( CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_INIZIO, 
															  CAMPO_MESE_DATA_CAMERA_CONSIGLIO_INIZIO, 
															  CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_INIZIO );

		lDataCameraConsiglioFine = getRequestDateParameter( CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_FINE, 
															CAMPO_MESE_DATA_CAMERA_CONSIGLIO_FINE, 
															CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_FINE );

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
		lRicercaProcedimento.setDataCameraConsiglioInizio(lDataCameraConsiglioInizio);
		lRicercaProcedimento.setDataCameraConsiglioFine(lDataCameraConsiglioFine);
		lRicercaProcedimento.setDataIscrizioneInizio(lDataIscrizioneInizio);
		lRicercaProcedimento.setDataIscrizioneFine(lDataIscrizioneFine);
		lRicercaProcedimento.setDataFine(lDataFine);
		lRicercaProcedimento.setNumeroGiorni(lNumeroGiorni);
		
		lRicercaProcedimento.setUtenteConnesso(getUtenteConnesso());
				
		IStatisticheSius lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();

        lElencoPaginato = lCtrl.ExRicercaProcFissatiNoDefNumGGPaginata(lRicercaProcedimento, Integer.parseInt(lPagina));
		
		// Paginazione.
	    BigDecimal lRecords;
	    if (isRequestParameterNullObj("CountRisultati"))
	        lRecords = lCtrl.ExGetNumRicercaProcFissatiNoDefNumGG(lRicercaProcedimento);
	    else
	    	lRecords = getRequestBigDecimalParameter("CountRisultati");

	    setRequestAttribute("CountRisultati",lRecords);
	    setRequestAttribute(IWebConstants.NUM_PAGE,lPagina);
	    setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				
		setSessionAttribute("ricercaProcedimenti", lRicercaProcedimento);
		setRequestAttribute("elencoProcedimenti", lElencoPaginato);
    	
        return PG_RICERCA_PROC_DATA_UDIENZA_FISSATA_NO_DEF_NUMGG;
    }
}