package siap.sius.misuresicurezzarichiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;

public class ActLoadInserisciCertificatiComune extends ActionSiap
		implements ICostantiRichiestaAtti, ICostantiMisureSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica DESTINATARIO
		Collection lCol = (DecodificheManager.getInstance()).getTipoAutorita();

		setRequestAttribute("codTipoUfficioS", "26");
		setRequestAttribute("descTipoUfficioS", DecodificheUtils.getDescbyCode(lCol, "26"));

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_CERTIFICATO");
		Collection lCertificati = lDecodifiche.ExRicercaDecodifiche(lModel);
		Option lOptionC = new Option(lCertificati);
		setRequestAttribute("elencoCertificati", "" + lOptionC);

		return PG_LOAD_RICHIESTACERTIFICATICOMUNE; // restituisce la jsp di VIEW
	}

}