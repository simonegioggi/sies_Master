package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.action.ICostantiImpugnazioneSige;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadInserisciFascicoloCollegato extends ActLoadInserisciFascicolo {

	public String processRequest() throws Exception {

		// STUB: Fascicolo SIEP potrebbe non essere in sessione mentre il soggetto deve esserci !!!		

		//@emma 10072018 intervento post COLLAUDO 11.2 
		String  page =  PG_LOAD_INSERISCIFASCICOLOSIGE;
		if (!isSessionAttributeNullObj("fascicolo")) {
			FascicoloSiepModel fascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			SoggettoModel soggetto = fascicolo.getSoggetto();
			setSessionAttribute("soggetto", soggetto);
			setRequestAttribute("modalita", "IF");
			page = super.processRequest();
		}else if (!isSessionAttributeNullObj("FascicoloSigeEsteso")) {
			FascicoloSigeEstesoModel lFasEst = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
			SoggettoModel soggetto = lFasEst.getSoggetto();
			setSessionAttribute("soggetto", soggetto);		
			//IMPOSTO IL FASCICOLO A NULL VISTO CHE NON ' PRESENTE
			setSessionAttribute("fascicolo", null);	  
		    //IMPOSTO ID Soggetto.
		    soggetto = (SoggettoModel)getSessionAttribute("soggetto");
		    setRequestAttribute("IDSoggetto",soggetto.getIdSoggetto().toString());		 
		    // Si rimuovono eventuali dati preesistenti
		    rimuoviFascicoloSigeEstesoDallaSessione();
		    // Viene chiamata questa funzione con null perchè non c'è il Fascicolo SIEP
		    preparaRequest(null);		    
		    // Modalità Inserimento da Soggetto
		    setRequestAttribute("modalita", "IS");	    				
		} else
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo SIEP non presente in sessione ");

		//String page = super.processRequest();
		Option lMittenteOpt = new Option(DecodificheManager.getInstance().getTipoRichiedenteSige(), 56);
		lMittenteOpt.setFilter(new String[] { "11" });
		setRequestAttribute("mittenteAtto", "" + lMittenteOpt);
		setRequestAttribute("comuneUfficio", "Roma");

		BigDecimal idRicorso = super
				.getRequestBigDecimalParameter(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE);
		
		BigDecimal idFascicoloSigeOrigine = super
				.getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);

		if (idRicorso != null) {
			// Imposta Tipo Atto SIGE uguale a "Ordinanza".
			Option lTipoAttoOpt = new Option(DecodificheManager.getInstance().getTipoAttoSige(), "04");
			setRequestAttribute("tipoAtto", "" + lTipoAttoOpt);
		}

		IImpugnazioneSige iCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
		ImpugnazioneSigeModel impugnazione = iCtrl.ExRicercaImpugnazioneByKey(idRicorso);
		Date dataAtto = impugnazione.getDataRicorso();
		Date dataArrivoCancelleria = impugnazione.getDataArrivoCancelleria();
		setRequestAttribute("dataAtto", dataAtto);
		setRequestAttribute("dataArrivoCancelleria", dataArrivoCancelleria);
		setRequestAttribute("dataDecisione", impugnazione.getDataDecisione());
		setRequestAttribute("isFascicoloCollegato", "true");
		setRequestAttribute("idFascicoloSigeOrigine", idFascicoloSigeOrigine.toString());		
		setRequestAttribute("codTenoreDecisione", impugnazione.getCodTenoreDecisione());
		return page;
	}

}