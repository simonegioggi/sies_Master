package siap.sico.soggetto.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciSoggetto</p>
 * <p>Description: Azione Load dell'Inserisci Soggetto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class ActLoadInserisciSoggetto extends ActionSiap implements ICostantiSoggetto {

	/**
	 * Azione di caricamento della form d'inserimento del Soggetto
	 * @return Nome della pagina JSP su cui posizionarsi
	 * al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

    	Option lOption = new Option( DecodificheManager.getInstance().getSesso(),"M");
    	setRequestAttribute("sesso", "" + lOption );
    
    	// Flag Data Nascita Presunta
   		lOption = new Option( DecodificheManager.getInstance().getFlagSNTrattino(), "N");
    	setRequestAttribute("dataNascitaPresunta", "" + lOption );    

		//  SuperSoggetto Ambrosino 04/2010
		// La Nazionalità diventa : Stato Cittadinanza  nella FORM .
		// Nel DB  questo dato va nella colonna SOGGETTO.NAZIONALITA    
    
    	// lOption = new Option( DecodificheManager.getInstance().getNazionalita(),"I");
    	// setRequestAttribute("nazionalita", "" + lOption );
    
    	lOption = new Option(DecodificheManager.getInstance().getStatoCittadinanza(),"-");
    	setRequestAttribute("StatoCittadinanza", "" + lOption );

   		// Riempie la combo delle nazioni che nella Form diventa lo Stato di Nascita
   		// Nel DB  questo dato va nella colonna SOGGETTO.COD_STATO_NASCITA
      	lOption = new Option( DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(),"-"), "039");
      	setRequestAttribute("nazioni", "" + lOption );      

    	setRequestAttribute("modalita", "I");

    	if (!this.isRequestAttributeNullObj("lTipoFunzione")) {
    		// paramentro passato solo nel caso di iscrizione guidata 
      		this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
    	}

		// MEV 15 - Revisione SIGE
		// Aggiunto parametro per identificare la funzione che richiama la maschera
		// di Iscrizione Soggetto da Iscrizione Manuale.
		// Quando viene richiamata da SIGE sulla maschera viene inserito
    	// il Calendario in corrispondenza di ogni campo data
    	String codFunzione = getCodFunMenuVerticale();
		setRequestAttribute("codFunzione", codFunzione);

    	return PG_LOAD_INSERISCISOGGETTO; //restituisce la jsp di VIEW
	}

}