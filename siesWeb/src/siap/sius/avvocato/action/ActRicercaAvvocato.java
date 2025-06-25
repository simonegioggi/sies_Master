package siap.sius.avvocato.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
//import siap.sius.avvocato.controller.AvvocatoController;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaAvvocato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Avvocato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaAvvocato extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		AvvocatoModel lAvvMod = new AvvocatoModel();
//		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();

		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));		

		// MEV_21: aggiungo il nome nella ricerca. Prevista inoltre la ricerca su tutti i fori 
		lAvvMod.setNome(getRequestStringParameter(CAMPO_NOME));
	
		if (isRequestChecked(CAMPO_FLAG_TUTTI_FORI))
			lAvvMod.setForo(null);
		else
			lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO));
		// MEV_21: FINE		
		
//		if (!this.isRequestParameterNullObj(CAMPO_FORO))
//			lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO));		
	  // MEV_21: la ricerca per cod ufficio appartenenza non è più pertinente
		//lAvvMod.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());

		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		
		// MEV_21: si gestisce la chiamata in try catch. Se nessun avvocato trovati si torna alla 
		// jsp con vettore vuoto per gestire inserimento manuale
		Vector lVect = new Vector();
		try {
			lVect = lCtrl.ExRicercaDifensore(lAvvMod);
		}
		catch (SIUSException se) {
			// 	
		}
		catch (Exception e) {
			throw e;	
		}
		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("avvocato", lVect);

		String lPage = PG_RICERCAAVVOCATO;

		if (getRequestStringParameter("modalita").compareTo("BREVE") == 0)
			lPage = PG_RICERCAAVVOCATOBREVE;

		return lPage;
	}

}