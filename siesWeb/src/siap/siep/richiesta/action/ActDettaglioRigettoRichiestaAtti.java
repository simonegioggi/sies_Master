package siap.siep.richiesta.action;


import java.math.BigDecimal;

import f3b.util.F3BException;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>Title: ActDettaglioRigettoRichiestaAtti</p>
 * <p>Description: Classe Action per il Dettaglio della Comunicazione di RIGETTO RICHIESTA Trasmissione Atti per Competenza</p>
 */

public class ActDettaglioRigettoRichiestaAtti extends ActSIESDettaglioProvvedimento implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {	

	FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");	  
	  
// id dell'evento inserito
   	BigDecimal lIdEvento = new BigDecimal(this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));    	

    PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,lFascMod.getIdFascicoloSiep());
    setRequestAttribute("penaresidua", llPenMod);

//ricerca posizione giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,lFascMod.getIdFascicoloSiep());
    setRequestAttribute("posizioneluogoaltra", lPos);

//ricerca evento notifica
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
    this.setRequestAttribute("eventonotifica", lEveMod);
    
//Ricerca Magistrato
    IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
    MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
    setRequestAttribute("magistrato", lMagMod);
    
//Ricerca COMPETENZA relativa al Provvedimento di RIGETTO : Ci sono i dati del Titolo Cumulante e del Titolo Richiesto
    ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();	
    CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);     
    setRequestAttribute("competenza", mComp);   

    return PG_DETTAGLIO_RIGETTO_RICHIESTA_ATTI_TRASM_COMP;
    
  }	// End processRequest()

}	// End Class ActDettaglioRigettoRichiestaAtti()

