package siap.sius.depositoordinanzapc.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActLoadEmissioneDecreto;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRimessioneAtti
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Rimessione Atti
 * </p>
 * Poichè l'azione deve implementare la stessa funzione implementata da ActLoadEmissioneDecreto, viene estesa
 * questa in modo di utilizzare il suo processRequest(). Si sfrutta l'override della funzione
 * generaListaTipi() per differenziare la jsp.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadRimessioneAtti extends ActLoadEmissioneDecreto {

	// Generazione della lista di tipi ordinanza

	public String processRequest() throws Exception {

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String lPage = super.processRequest();
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (lPage == PG_WARNING)
			return PG_WARNING;

		// Controllo esistenza udienza solo in TDS !!
		if (strCodTipoUfficio.equals("TDS")) {
			if (lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Impossibile emettere un'ordinanza per il fascicolo "
								+ lFasGPMod.getFascicoloSiusModel().getChiaveAnno() + "/"
								+ lFasGPMod.getFascicoloSiusModel().getChiaveProgr()
								+ ". Non è stata fissata l'Udienza.");
		}
		setRequestAttribute("flagOrdinanza", "rimessioneatti");

		// Preleva elenco degli altri destinatari.
		Option lOptionAut = new Option();
		lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);
		setRequestAttribute("tipoAutorita", lOptionAut.toString());

		// LISTA UFFICI per notifica all'avvocato
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		String[] lStringFilter = { "-", "22" };
		Option lOptionAvv = new Option(lTipoIstituto, "22", 75);
		lOptionAvv.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOptionAvv);

		// return lPage;
		// jsp ad hoc per la rimessione atti
		return ICostantiDepositoOrdinanzaPc.PG_LOAD_RIMESSIONE_ATTI;
	}

	public List generaListaTipi() {

		List lTipoOrdinanza = null;
		setRequestAttribute("flagOrdinanza", "ordinanza");
		if (DecodificheManager.getInstance().getTipoOrdinanza() == null) {
			lTipoOrdinanza = new ArrayList();
		} else {
			lTipoOrdinanza = new ArrayList(DecodificheManager.getInstance().getTipoOrdinanza());
		}
		lTipoOrdinanza.add(new DecodificheModel("00", "Generazione Automatica", "-", "-", "-", "-", "-", "-",
				"-"));

		return lTipoOrdinanza;
	}

}