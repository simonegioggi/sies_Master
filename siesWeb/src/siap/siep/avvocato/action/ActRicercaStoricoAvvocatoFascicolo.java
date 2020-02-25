package siap.siep.avvocato.action;

/**
* <p>Title: ActRicercatoricoAvvocatoFascicolo</p>
* <p>Description: Classe Action per la ricerca degli Avvocati associati
*    al fascicolo nella storia</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

@SuppressWarnings("rawtypes")
public class ActRicercaStoricoAvvocatoFascicolo extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Action di caricamento degli avvocati associati al fascicolo nella storia
	 * 
	 * @return pagina di visualizzazione dell'elenco avvocati @throws Exception @throws
	 */
	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicolo"))
			throw new SIEPException(SIEPException.USER_MESSAGE, "Selezionare il fascicolo.");

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
		lAvvFascMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) (getSessionAttribute("fascicolo"))).getIdFascicoloSiep());

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		List lListAvvocati = lCtrl.ExRicercaStoricoAvvocatiFascicolo(lAvvMod, lAvvFascMod);

		setRequestAttribute("listaStorico", lListAvvocati);
		// this.setLinkRitorno();

		String lAzione = "siap.siep.avvocato.action.ActRicercaStoricoAvvocatoFascicolo";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);
		return PG_ELENCO_STORICO_AVVOCATI;
	}

}