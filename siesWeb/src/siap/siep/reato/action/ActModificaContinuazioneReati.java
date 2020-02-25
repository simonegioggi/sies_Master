package siap.siep.reato.action;

/**
* <p>Title: ActModificaReato</p>
* <p>Description: Classe Action per la modifica di Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.ReatoContinuazioneController;
import siap.siep.reato.model.ReatoModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaContinuazioneReati extends ActionSiap implements ICostantiReato {

	/**
	 * Azione di Modifica del Reato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = new FascicoloSiepModel();

		lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFas = lFascMod.getIdFascicoloSiep();

		String strCont = (String) getRequestStringParameter("strreaticont");
		String strTipo = (String) getRequestStringParameter("strtipocont");
		String[] arrCont = strCont.split("=");
		String[] arrTipo = strTipo.split("=");

		ArrayList lReati = new ArrayList();

		int idCont = 0;

		for (int i = 0; i < arrCont.length; i++) {

			idCont++;
			String[] numReati = arrCont[i].split("-");

			for (int y = 0; y < numReati.length; y++) {

				ReatoModel lReaMod = new ReatoModel();

				lReaMod.setFasSieIdFascicoloSiep(idFas);
				lReaMod.setProgrReato(new BigDecimal(numReati[y]));
				lReaMod.setIdContinuazioneReato(new BigDecimal(idCont));
				lReaMod.setTipoContinuazioneReato(arrTipo[i]);

				lReati.add(lReaMod);
			}
		}

		ReatoContinuazioneController lCtrl = new ReatoContinuazioneController();
		lCtrl.ExModificaContinuazioneReati(lReati);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.reato.action.ActRicercaReato";

		return lPage;
	}

}