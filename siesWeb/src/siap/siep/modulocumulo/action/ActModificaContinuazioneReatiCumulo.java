package siap.siep.modulocumulo.action;

/**
* <p>Title: ActModificaContinuazioneReatiCumulo</p>
* <p>Description: Classe Action per la modifica della Continuazione di Reato</p>
*   <p>     legati al titolo Cumulato (Reato_Cumulo /Continuazioni )</p>
*/

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.siep.modulocumulo.controller.ReatoContinuazioneCumuloController;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import f3b.web.IWebConstants;

/**
 * Action che inserisce/modifica le continuazioni tra i reati
 * 
 * @author d.fiorletta
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaContinuazioneReatiCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo {

	public String processRequest() throws Exception {

		BigDecimal idTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

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

				ReatoCumuloModel lReaMod = new ReatoCumuloModel();

				lReaMod.setTitIdTitoloCumulato(idTitolo);

				lReaMod.setProgrReato(new BigDecimal(numReati[y]));

				lReaMod.setIdContinuazioneReatoCum(new BigDecimal(idCont));
				lReaMod.setTipoContinuazioneReato(arrTipo[i]);

				lReati.add(lReaMod);
			}
		}

		ReatoContinuazioneCumuloController lCtrl = new ReatoContinuazioneCumuloController();
		lCtrl.ExModificaContinuazioneReati(lReati);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActRicercaReatoCumulo";

		return lPage;
	}

}