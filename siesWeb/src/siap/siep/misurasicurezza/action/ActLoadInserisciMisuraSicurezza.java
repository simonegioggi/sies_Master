package siap.siep.misurasicurezza.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
//import per le combo
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di MisuraSicurezza
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
public class ActLoadInserisciMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.setRequestAttribute("idfascicolo", lFascMod.getIdFascicoloSiep() + "");

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));

		}

		preparaForm();

		return PG_LOAD_INSERISCIMISURASICUREZZA; // restituisce la jsp di VIEW
	}

	@SuppressWarnings("rawtypes")
	protected void preparaForm() {

		// ComboBOX X Natura Misura Sicurezza
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --lOptionN = "+lOptionN.toString());

		// ComboBOX X Tipo Misura Sicurezza
		Vector lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza();
		setRequestAttribute("tipoMisuraSicurezza", lVec);

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --lVec = "+lVec.toString());

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

	}

}