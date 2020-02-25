package siap.siep.avvocato.action;

/**
* <p>Title: ActRicercaAvvocatoFascicolo</p>
* <p>Description: Classe Action per la ricerca degli Avvocati associati
*    al fascicolo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
//import siap.siep.avvocato.controller.AvvocatoController;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActRicercaAvvocatoFascicolo extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Action di caricamento degli avvocati attualmente associati al fascicolo
	 * 
	 * @return pagina di visualizzazione dell'elenco avvocati @throws
	 */
	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo"))
			throw new SIEPException(SIEPException.USER_MESSAGE, "Selezionare il fascicolo.");

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
		lAvvFascMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) (getSessionAttribute("fascicolo"))).getIdFascicoloSiep());

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaAvvocatiAttualiFascicolo(lAvvMod, lAvvFascMod);

		setRequestAttribute("modalita", "NoPop");
		setRequestAttribute("avvocato", lVect);

		String lFascicoloCompetenza = "S";
		try {
			this.isFascicoloSiepDiCompetenza();
		} catch (Exception e) {
			lFascicoloCompetenza = "N";
		}
		setRequestAttribute("lFascicoloCompetenza", lFascicoloCompetenza);

		return PG_ELENCO_AVVOCATI;
	}

}