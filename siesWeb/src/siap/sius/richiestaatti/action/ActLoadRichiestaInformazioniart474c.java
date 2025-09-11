package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaInformazioniart474c extends ActionSiap implements ICostantiRichiestaAtti {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// LISTA DESTINATARI
		Collection lCol = DecodificheManager.getInstance().getTipoAutorita();
		String[] lStringFilter = { "-", "19", "20", "28", "58", "59", "60", "61" };
		Option lOption = new Option(lCol);
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		// LISTA CSSA
		Collection lCol2 = DecodificheManager.getInstance().getTipoAutorita();
		// MEV10-s3: aggiunto controllo su tipologia di ufficio connesso
		String codTipoUfficio = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getCodTipoUfficio();
		String[] lStringFilter2;
		if ("UDSM".equals(codTipoUfficio) || "TDSM".equals(codTipoUfficio))
			lStringFilter2 = new String[] { "-", "40", "B5" };
		else
			lStringFilter2 = new String[] { "-", "40" };
		Option lOption2 = new Option(lCol2);
		lOption2.setFilter(lStringFilter2);
		setRequestAttribute("TipiIstituti2", "" + lOption2);

		// restituisce la jsp di VIEW
		return PG_LOAD_RICHIESTAINFORMAZIONIART474C;
	}
}