package siap.siep.misuracautelare.action;


import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;


/**
* <p>Title: ActLoadDettaglioMisuraCautelare</p>
* <p>Description: Classe Action per la load dettaglio di MisuraCautelare</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioMisuraCautelare extends ActionSiap implements ICostantiMisuraCautelare
 {
	public String processRequest() throws F3BException {
		
		BigDecimal lIdFascicolo = ( (FascicoloSiepModel) getSessionAttribute("fascicolo") ).getIdFascicoloSiep();
		String lId = getRequestStringParameter(CAMPO_ID_MISURA_CAUTELARE);
		// riempie il model
		// chiama il controller

		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
		MisuraCautelareModel llMisMod = lCtrl
				.ExRicercaMisuraCautelareByKey(new BigDecimal(lId));

		String lFlagComp = llMisMod.getFlagComputabile();
		if (lFlagComp.equals("N"))
			setRequestAttribute("computabilità", "N");
		else
			setRequestAttribute("computabilità", "S");

		setRequestAttribute("misuracautelare", llMisMod);

		//ufficio PM 
		if(llMisMod!=null && llMisMod.getCodiceUfficioPmSede()!=null){
			IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
			UfficioModel ufficio = lCtrlUff.ExRicercaUfficioByCod(llMisMod.getCodiceUfficioPmSede());
			Option lOptionUffPM = new Option();
			lOptionUffPM  = new Option( DecodificheManager.getInstance().getTipoUfficioPM()); 
			lOptionUffPM.setFilter( new String[] {"PM","PMM","PGCAP"}); 
			//default codice ufficio utente
			lOptionUffPM.setSelected(ufficio.getCodTipoUfficio());  
			String tipoUfficioPM = lOptionUffPM.getSelecteds()[0];
			setRequestAttribute("tipoUfficioPM", "" + tipoUfficioPM );
			//sede dell'ufficio PM
			String tipoUfficioPmSede="";
			IComune comctrl=SICOLookupRemote.getComuneRemote();
		  	ComuneModel comunemod=comctrl.ExRicercaComuneByKey(ufficio.getCodComune());
		  	tipoUfficioPmSede=comunemod.getDescrizione();
		  	setRequestAttribute("tipoUfficioPmSede", "" + tipoUfficioPmSede );
		}
		
		//Autorità emittente 
		if(llMisMod!=null && llMisMod.getAutoritaEmittente()!=null){
			Option lOptionAutoritaEmittenteUff  = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(),llMisMod.getAutoritaEmittente());
			String AutoritaEmittenteDesc= lOptionAutoritaEmittenteUff.getSelecteds()[0];
			setRequestAttribute("AutoritaEmittenteDesc", "" + AutoritaEmittenteDesc );  
		}
		
		//Autorita' competente per territorio    
	    Option lOptionAutoritaCompTerritorio = new Option( DecodificheManager.getInstance().getAutoritaCompetentePerTerritorio(),llMisMod.getAutoritaCompetente());
		// Autorita' competente per territorio
	    lOptionAutoritaCompTerritorio.setSelected(llMisMod.getAutoritaCompetente());  
		String AutoritaCompetentePerTerritorio = lOptionAutoritaCompTerritorio.getSelecteds()[0];
	    setRequestAttribute("autoritaCompTerritorio", "" + AutoritaCompetentePerTerritorio );
		
	    // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
	    // paolo cherubini lunedi 11/10/2010
	    // Ricerca l'ultima pena residua per quel fascicolo
	    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
	    PenaResiduaModel lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
	    if (lPenaResMod!=null ) {
	    	setRequestAttribute("lPenaResMod", lPenaResMod);
	      }
	    // fine a9/rr/075
	    
		return PG_LOAD_DETTAGLIOMISURACAUTELARE;
	}

}