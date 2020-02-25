package siap.sius.avvocato.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciAvvocato
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Avvocato
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
public class ActLoadInserisciAvvocato extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Selezionare il procedimento.");

		// Gestione bottone di ritorno
		this.gestioneRitorno();

		if (!this.isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					this.getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
		}

		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lVect = null;

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();
		lAvvFascMod.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		// AvvocatoController lCtrl = new AvvocatoController();
		try {
			lVect = new Vector();
			lVect = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);
		} catch (Exception e) {
		}
		if (lVect.size() > 0) {
			setRequestAttribute("modalita", "NoPop");
			setRequestAttribute("avvocato", lVect);

			return PG_ASSEGNA_INSERISCI_AVVOCATO; // restituisce la jsp di VIEW
		} else {
			Option lOption = new Option(DecodificheManager.getInstance().getTipoAvvocato());
			setRequestAttribute("tipoAvvocato", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			setRequestAttribute("autoritaEsterna", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
			setRequestAttribute("autoritaEsternaDif", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
			setRequestAttribute("motivoDesignazione", "" + lOption);

			setRequestAttribute("modalita", "I");

			// 19/03/2010 Nuova gestione Combo per Foro avvocato.
			// IAvvocato lCtrl1 = SIUSLookupRemote.getAvvocatoRemote();
			// Vector lVect1 = lCtrl.ExRicercaForo();
			// this.setRequestAttribute("foro", lVect1);

			UfficioModel lUffUte = this.getUfficioUtenteConnesso();
			String lDescrComune = lUffUte.getDescrComune();
			this.setRequestAttribute("comune", lDescrComune);

			// 19/03/2010 Nuova gestione Combo per Foro avvocato.
			lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase()
					.trim(), Option.NO_BLANK_ITEM);
			setRequestAttribute("foro", "" + lOption);
			return PG_LOAD_INSERISCIAVVOCATO; // restituisce la jsp di VIEW
		}

		// Passa la action di destinazione
		/*
		 * if (!isRequestParameterNullObj("acdest")) {this.setRequestAttribute("acdest",
		 * this.getRequestStringParameter("acdest"));}
		 * 
		 * Option lOption = new Option( DecodificheManager.getInstance().getTipoAvvocato());
		 * setRequestAttribute("tipoAvvocato", "" + lOption ); setRequestAttribute("modalita", "I"); return
		 * PG_LOAD_INSERISCIAVVOCATO; //restituisce la jsp di VIEW
		 */
	}

}