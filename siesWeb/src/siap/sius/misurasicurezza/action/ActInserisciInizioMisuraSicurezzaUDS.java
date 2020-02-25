package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;

//import siap.sico.evento.model.EventoModel;
//import siap.sius.SIUSException;
//import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
//import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciInizioMisuraSicurezzaUDS extends ActMisuraSicurezza 
implements  ICostantiSiusMisuraSicurezza
{
	 public String processRequest() throws F3BException {
	    
			//recupero ID fascicolo e dati maschera
		 	PeriodoAltraMisuraModel lPerMod = new PeriodoAltraMisuraModel();
			lPerMod = recuperoDatiMaschera(lPerMod);
			BigDecimal lIdFasSius = recuperoIdFascicolo();
			BigDecimal lIdFasSiep = recuperoIdFascicoloSiep();
			lPerMod.setFasSiuIdFascicoloSius(lIdFasSius);
			lPerMod.setFasSieIdFascicoloSiep(lIdFasSiep);
			
			//setto i dati dell'utente che effettua l'inserimento
			lPerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPerMod.setDataInserimento(DateUtils.getSysDate());     

		    // Recupera il controller ed effettua l'inserimento inserisce periodo, modifica EMS
		    IPeriodoAltraMisura lCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		    lPerMod = lCtrl.ExInserisciPeriodoAltraMisura(lPerMod);
		    
		    //====================================================================== 
		    // Prepara la pagina di destinazione
		    // Viene restituita la pagina di dettaglio con i dati appena inseriti
		    //====================================================================== 	   
		    String lPage="";
		    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.misurasicurezza.action.ActLoadValidaInizioMisuraSicurezzaUDS";
		    lPage += "&" + CAMPO_ID_PERIODO_ALTRA_MISURA + "=" + lPerMod.getIdPeriodoAltraMisura().toString();
		    return lPage;
		  }
}
