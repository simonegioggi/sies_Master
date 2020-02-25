package siap.sius.statistiche.action;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

//import siap.sius.impugnazione.action.ICostantiImpugnazione;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
//import siap.sico.evento.action.ICostantiEvento;
//import f3b.web.RedirectTo;
//import f3b.util.F3BException;
import siap.sius.ActionSius;
//import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
//import siap.sius.depositoordinanzapc.action.ActRicercaDepositoOrdinanzaPcByAnnoNumUff;
//import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.util.SIUSLookupRemote;
//import f3b.log.LogF3B;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActRicercaImpugnazioneByAnnoNumUff</p>
 * <p>Description: Classe Action per la ricerca del Ricorso/Impugnazione 
 * da Anno, Num e codice Ufficio di Inserimento.</p>
 * <p>La classe è stata ottenuta specializzando la ActRicercaDepositoOrdinanzaPc 
 * per riutilizzare la stessa funzione per la ricerca dei dati da inserire in session.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActRicercaProcDataUdienzaFissataNonDefiniti extends ActionSius implements ICostantiStatistiche {
	public String processRequest() throws Exception {

	    this.setLinkRitorno();
	    String lPagina = "1";
	    if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
	        lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		Date lDataCameraConsiglioInizio 	= null; 
		Date lDataCameraConsiglioFine 		= null;

		Date lDataIscrizioneInizio 			= null;
		Date lDataIscrizioneFine 			= null;

		String lCodPosizioneGiuridica		= null;
		String lCodOggettoProcedimento		= null;

		RicercaProcedimentoModel lRicercaProcedimento = null;
		
		
		//
		// Lettura dati dalla form
		//
		lDataCameraConsiglioInizio = getRequestDateParameter(	CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_INIZIO, 
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

		lCodPosizioneGiuridica = getRequestStringParameter(CAMPO_COD_POSIZIONE_GIURIDICA);
		lCodOggettoProcedimento = getRequestStringParameter(CAMPO_COD_OGGETTO_PROCEDIMENTO);

		lRicercaProcedimento = new RicercaProcedimentoModel();
		lRicercaProcedimento.setDataCameraConsiglioInizio(lDataCameraConsiglioInizio);
		lRicercaProcedimento.setDataCameraConsiglioFine(lDataCameraConsiglioFine);
		lRicercaProcedimento.setDataIscrizioneInizio(lDataIscrizioneInizio);
		lRicercaProcedimento.setDataIscrizioneFine(lDataIscrizioneFine);
		lRicercaProcedimento.setCodPosizioneGiuridica(lCodPosizioneGiuridica);
		
		lRicercaProcedimento.setDescrPosizioneGiuridica( 
				DecodificheUtils.getDescbyCode( 
						DecodificheManager.getInstance().getPosizioneGiuridica(), lCodPosizioneGiuridica) );
		
		lRicercaProcedimento.setCodOggettoProcedimento(lCodOggettoProcedimento);
		lRicercaProcedimento.setDescrOggettoProcedimento(
				DecodificheUtils.getDescbyCode( 
						DecodificheManager.getInstance().getOggettoProcedimento(), lCodOggettoProcedimento) );
		
		lRicercaProcedimento.setUtenteConnesso(getUtenteConnesso());
				
		IStatisticheSius lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
		
		//Collection<EveFasGepSogProvPosGiuModel> lElenco = lCtrl.ExRicercaProcFissatiNoDef(lRicercaProcedimento);

        Collection<EveFasGepSogProvModel> lElencoPaginato = 
        		lCtrl.ExRicercaProcFissatiNoDefPaginata(lRicercaProcedimento, Integer.parseInt(lPagina));
		
		// Paginazione.
	    BigDecimal lRecords;
	    if (isRequestParameterNullObj("CountRisultati"))
	      lRecords = lCtrl.ExGetNumRicercaProcFissatiNoDef(lRicercaProcedimento);
	    else
	    	lRecords=getRequestBigDecimalParameter("CountRisultati");

	    setRequestAttribute("CountRisultati",lRecords);
	    setRequestAttribute(IWebConstants.NUM_PAGE,lPagina);
	    setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		
		
		setSessionAttribute("ricercaProcedimenti", lRicercaProcedimento);
		setRequestAttribute("elencoProcedimenti", lElencoPaginato);
		

		return PG_RICERCA_PROC_DATA_UDI_FISS_NO_DEF;
	}
}
