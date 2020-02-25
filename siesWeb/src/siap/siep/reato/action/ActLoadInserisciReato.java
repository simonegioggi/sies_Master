package siap.siep.reato.action;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: ActLoadInserisciReato
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Reato
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
public class ActLoadInserisciReato extends ActionSiap implements ICostantiReato {

	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		// boolean proceed=true;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile aggiungere Reati");

		if (lFascMod.getFlagValidato().equalsIgnoreCase("S"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " è stato Validato. Impossibile aggiungere Reati.");

		Option lOption = new Option(DecodificheManager.getInstance().getTipoReato());
		setRequestAttribute("TipiReato", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato());
		setRequestAttribute("TipiFontiReato", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
		setRequestAttribute("TipiSottonumerazione", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getPeriodoConsumazione());
		setRequestAttribute("PeriodoConsumazione", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getTipoPenaDetentiva());
		setRequestAttribute("TipiPeneDetentive", "" + lOption);

		setRequestAttribute("modalita", "I");

		return PG_LOAD_INSERISCIREATO; // restituisce la jsp di VIEW

	}
}