package siap.siep.richiesta.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;


/**
 * <p>Title: ActLoadTrasferisciTrasmissioneCompetenza</p>
 * <p>Description: prepara la pagina per Trasferire la richietsa di Trasmissione per Competenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActLoadTrasferisciTrasmissioneCompetenza extends ActionSiap implements ICostantiRichiesta{
	
	public String processRequest() throws Exception
	{
		BigDecimal lIdEvento =	getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		
	    // ricerca evento competenza
	    ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();	
	    CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);     
	    setRequestAttribute("competenza", mComp);
	    
	    // Ricerca Evento (per conoscere Cod:Motivo)
	    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
	    EventoNotificaModel lEveMod = new EventoNotificaModel();
	    lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
	    setRequestAttribute("eventonotifica", lEveMod);
	    
	    FascicoloSiepModel mFascComp = new FascicoloSiepModel();   	
	 	mFascComp.setChiaveUfficio(mComp.getChiaveUfficio());
	 	mFascComp.setChiaveAnno(mComp.getChiaveAnno());
	 	mFascComp.setChiaveProgr(mComp.getChiaveProgr());
	 	
	 	IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
	 	FascicoloSiepModel findedFasc = null;
	 	if(mComp.getFasSieIdFascicoloSiep()!=null){
	 		findedFasc = lCtrlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(mFascComp);
	 		setRequestAttribute("fascCompetenza", findedFasc);
	 	}	    
		
		setRequestAttribute("IDEvento",lIdEvento.toString());		
		
		return PG_LOAD_TRASFERISCI_TRASMISSIONE_COMP;
	  }
}
