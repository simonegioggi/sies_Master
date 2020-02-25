package siap.sius.statistiche.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.IspConteggioRelatoriMagistratiModel;
import siap.sius.statistiche.model.IspMotivoOggettoSelezionatiModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

public class ActRicercaStatisticaComparataMagistrati extends ActionSiap implements ICostantiStatistiche {	
    public String processRequest() throws Exception {
    	Date	 lDataInizio	= null;
    	Date 	 lDataFine		= null;
    	String[] lCodMotiviOggetti	= null;
    	String[] lCodMagistrati = null;
    	
    	lDataInizio = getRequestDateParameter(
    			ICostantiStatistiche.CAMPO_ANNO_INIZIALE, 
    			ICostantiStatistiche.CAMPO_MESE_INIZIALE, 
    			ICostantiStatistiche.CAMPO_GIORNO_INIZIALE);
    	lDataFine = getRequestDateParameter(
    			ICostantiStatistiche.CAMPO_ANNO_FINALE, 
    			ICostantiStatistiche.CAMPO_MESE_FINALE, 
    			ICostantiStatistiche.CAMPO_GIORNO_FINALE);
    	
    	lCodMotiviOggetti = getParameterValues(ICostantiStatistiche.CAMPO_COD_OGGETTO);
    	lCodMagistrati = getParameterValues(ICostantiStatistiche.CAMPO_COD_MAGISTRATO);
    	
    	RicercaProcedimentoModel lRicerca = new RicercaProcedimentoModel();
    	lRicerca.setDataIscrizioneInizio(lDataInizio);
    	lRicerca.setDataIscrizioneFine(lDataFine);
    	lRicerca.setCodMotivi(lCodMotiviOggetti);
    	lRicerca.setCodUfficio(getCodUfficioUtenteConnesso());
    	lRicerca.setUtenteConnesso(getUtenteConnesso());
    	lRicerca.setCodTipoUfficio(getUfficioUtenteConnesso().getCodTipoUfficio());
    	lRicerca.setCodMagistrati(lCodMagistrati);
    	
    	IStatisticheSius ctrl = SIUSLookupRemote.getStatisticheSiusRemote();
    	Collection <IspConteggioRelatoriMagistratiModel> lLista =  ctrl.ExRicercaStatisticaComparataMagistrati(lRicerca);
        Collection <IspMotivoOggettoSelezionatiModel> lOggettiSelezionati = ctrl.ExListaMotiviOggettiSelezionati();
    	
    	setRequestAttribute("elencoConteggioRelatori", lLista);
    	setRequestAttribute("elencoOggettiSelezionatiCbx", elencoOggettiSelezionatiCbx(lOggettiSelezionati).toString());
        
        setSessionAttribute("ricercaProcedimento", lRicerca);
        
        return PG_RICERCA_STATISTICA_COMPARATA_MAGISTRATI; //restituisce la jsp di VIEW
    }
    
    private Option elencoOggettiSelezionatiCbx(Collection <IspMotivoOggettoSelezionatiModel> aOggettiSelezionati ) {    
    	Collection <DecodificheModel> lDecodeModels = new ArrayList<DecodificheModel>();    
		for( IspMotivoOggettoSelezionatiModel lOggetto : aOggettiSelezionati ) {
			DecodificheModel lDecModel = new DecodificheModel();
			lDecModel.setCode(lOggetto.getCodOggetto());
			lDecModel.setCodiceAlternativo(lOggetto.getCodMotivo());
			lDecModel.setDescription(lOggetto.getDescMotivo());
			lDecodeModels.add(lDecModel);    		
		}
		Option lOggettiSelezionatiCbx = new Option();
		lOggettiSelezionatiCbx.setValues(lDecodeModels);
		return lOggettiSelezionatiCbx;	
    }   
}