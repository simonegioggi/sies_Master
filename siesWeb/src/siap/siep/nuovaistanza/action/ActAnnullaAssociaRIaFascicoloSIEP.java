package siap.siep.nuovaistanza.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * 
 *
 *
 */
public class ActAnnullaAssociaRIaFascicoloSIEP extends ActionSiap {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	
	public String processRequest() throws F3BException
	 {
		
		FascicoloSiepModel lRIMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lRIMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lRIMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		
		IFascicoloSiep fsCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFasSiepMod = fsCtrl.ExRicercaFascicoloByKey(lRIMod.getFasSieIdFascicoloSiep());

		if (lFasSiepMod == null)
    	  	throw new F3BException(F3BException.USER_MESSAGE, "Il registro istanza non è collegato ad alcun fascicolo siep!");

	    INuovaIstanza lCtrl2 = SIEPLookupRemote.getNuovaIstanzaRemote();

	    lRIMod = lCtrl2.ExAnnullaAssociaNuovaIstanza(lRIMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("MODEL MODIFICATO: " + lRIMod);
	    
		//setRequestAttribute("fascicolo", lRIMod);
	    //setSessionAttribute("fascicolo",lRIMod);
	    //setSessionAttribute("soggetto",lRIMod.getSoggetto());
	    //setSessionAttribute("sentenza",lRIMod.getSentenza());
		// Dopo aver cancellato l'associazione si vuole essere riportati nel dettaglio del fascicolo siep
		// (non del registro istanze)
	    setRequestAttribute("fascicolo", lFasSiepMod);
	    setSessionAttribute("fascicolo",lFasSiepMod);
	    setSessionAttribute("soggetto",lFasSiepMod.getSoggetto());
	    setSessionAttribute("sentenza",lFasSiepMod.getSentenza());

	    
	    // setta la risposta nella request
	    String lWarning = "L'associazione del registro istanza "; 
	    if (lRIMod.getChiaveAnno()!=null && lRIMod.getChiaveProgr()!=null)
	    	lWarning+= lRIMod.getChiaveAnno() + "/" + lRIMod.getChiaveProgr() + " ";
	    if (lFasSiepMod.getChiaveAnno()!=null && lFasSiepMod.getChiaveProgr()!=null)
	    	lWarning+= " al fascicolo Siep " + lFasSiepMod.getChiaveAnno() + "/" + lFasSiepMod.getChiaveProgr() + " ";
	    lWarning+= "è stata eliminata!";
	    lWarning+= "Proseguire con l'aggiornamento del fascicolo Siep.";
	  	    
	    setRequestAttribute(IWebConstants.MESSAGE_TEXT, lWarning);

	    //Prepara la "pagina" di destinAction
	    RedirectTo lRedirigi = new RedirectTo();
	    lRedirigi.setPage( IWebConstants.PG_MAIN );
	    lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo" );
	    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
	    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW

	 }

}