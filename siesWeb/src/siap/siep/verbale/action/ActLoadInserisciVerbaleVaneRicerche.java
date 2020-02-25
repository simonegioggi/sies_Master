package siap.siep.verbale.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciVerbale</p>
 * <p>Description: Classe Action per la load inserisci di Verbale</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciVerbaleVaneRicerche extends ActionSiap implements ICostantiVerbale 
{
	public String processRequest() throws F3BException 
	{
		if (this.isSessionAttributeNullObj("fascicolo")) 
		{
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO
					+ getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) 
		{
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/"
							+ lFascMod.getChiaveProgr()
							+ " non è stato Validato. Impossibile procedere!");
			lRedirigi
					.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
							+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE
							+ "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) 
		{
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/"
							+ lFascMod.getChiaveProgr()
							+ " risulta Definito. Impossibile procedere!");
			lRedirigi
					.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
							+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE
							+ "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		
		//se proviene dalla maschera di omesse notifiche
		if(!this.isRequestParameterNullObj("FlagOmesse"))
			setRequestAttribute("FlagOmesse", this.getRequestStringParameter("FlagOmesse"));			
		
		
		
		/*// ho inserito il controllo su segnalazione di Pina Marchese se era stato notificato
		 * il' OE con sospensione all'avvocato l'ho tolto su indicazione di Nunzia forse va
		 * spostato sul decreto di irreperibilità ma piu che un blocco dovrebbe essere un avviso 
		 * quindi rinuncio per ora
//		Controllo se esiste il decreto di Sospensione
	    EventoModel lEventoMod = new EventoModel();
	    IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
	    lEventoMod = lEventoCtrl.ExRicercaEventoByFascicoloSiepOESospensione(lFascMod.getIdFascicoloSiep());
		if (lEventoMod != null) {
			// Controllo esistenza almeno un avvocato per fascicolo.
		    IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
			Vector  lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			// Blocco se il Decreto simeone non è stato notificato agli avvocati.
				Vector lNotifiche = null;	
				NotificaModel lNotMod = new NotificaModel();
				INotifica INotifica = SIEPLookupRemote.getNotificaRemote();
				lNotMod.setEveIdEvento(lEventoMod.getIdEvento());
				lNotMod.setCodTipoNotifica("N");
				lNotifiche = INotifica.ExRicercaNotificaAvvocatoNonAvvenuta(lNotMod);
				if (lNotifiche != null && lNotifiche.size() == lAvvVect.size()) {
				}else
				 {
				    if (this.isRequestParameterNullObj("warning"))
				    {
				    throw new SIEPException( SIEPException.USER_MESSAGE,
				    		" L'OE con sospensione non è stato notificato all'avvocato. " +
				    		" Impossibile inserire il verbale di vane ricerche!" );
	    
				    
				    }
				 }
		 }*/
		
		// Imposta Tipo Istituto
		//  Option lOption = null;
		//  lOption = new Option( DecodificheManager.getInstance().getTipoIstituto());

		Option lOptionAutorita = null;
		lOptionAutorita = new Option(DecodificheManager.getInstance()
				.getTipoAutorita());

		// setRequestAttribute("tipoIstituto", "" + lOption );
		setRequestAttribute("tipoAutorita", "" + lOptionAutorita);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		return PG_LOAD_INSERISCI_VERBALE_VANE_RICERCHE; //restituisce la jsp di VIEW

	}

}