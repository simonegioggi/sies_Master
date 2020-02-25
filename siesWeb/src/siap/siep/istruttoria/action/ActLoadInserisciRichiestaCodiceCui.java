package siap.siep.istruttoria.action;

import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.UfficioController;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.controller.IstruttoriaController;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.notiziareato.controller.NotiziaReatoController;
import siap.siep.sentenza.model.SentenzaModel;
import siap.web.ISIAPCostantiWeb;

/**
 *
 * <p>
 * Title: ActLoadInserisciRichiestaCodiceCui
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciRichiestaCodiceCui extends ActionSiap implements ICostantiIstruttoria {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicolo") || isSessionAttributeNullObj("soggetto")
				|| isSessionAttributeNullObj("sentenza")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.istruttoria.action.ActLoadInserisciRichiestaCodiceCui";

			return lPage;
		}

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.isFascicoloSiepDiCompetenza();
		this.isEventoNonValidato();

		// ------Controllo se ci sono Notizia di reato da importare da rege
		// ------per questo fascicolo

		IstruttoriaController lIstruttoria = new IstruttoriaController();
		String lKeyFileRege = lIstruttoria.ExControllaNotizieDiReatoRege(
				(SoggettoModel) getSessionAttribute("soggetto"),
				(SentenzaModel) getSessionAttribute("sentenza"));

		if (lKeyFileRege != null)
			setRequestAttribute("IdRegeFile", lKeyFileRege);

		NotiziaReatoController lCtrl = new NotiziaReatoController();

		// -----
		// -----
		// Trova le Notizie di Reato
		// chiama il controller

		// Instanzia il vettore
		// se non ci sono notizie di reato accedo comunque alla pagina
		Vector lVect;
		try {
			lVect = lCtrl.ExRicercaNotiziaReatoByIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		} catch (F3BException ex) {
			lVect = null;
		}

		// setta la risposta nella request
		setRequestAttribute("elenconotiziareato", lVect);

		Option lOption = new Option(DecodificheManager.getInstance().getSedeGabPS());

		UfficioModel lUffMod = new UfficioModel();
		UfficioController lUffCtrl = new UfficioController();
		lUffMod = lUffCtrl.getUfficioByKey(this.getCodUfficioUtenteConnesso());
		String sedeGab = DecodificheUtils.getCodAltebyCode(DecodificheManager.getInstance().getProvincie(),
				lUffMod.getCodProvincia());

		lOption.setSelected(sedeGab);

		setRequestAttribute("sededi", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getQuesture());

		setRequestAttribute("questure", "" + lOption);

		setRequestAttribute("datairrevocabilita",
				DateUtils.getDateToString(lFascicoloModel.getDataIrrevocabilita(), "dd/MM/yyyy"));

		// Se vengo da IstruttoriaCUMULO
		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)) {
			setRequestAttribute("IdIstruttoriaCumulo",
					getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
		}

		// IMPOSTAZIONI PER FUNZIONALITà BACK
		String lAzione = "siap.siep.istruttoria.action.ActLoadInserisciRichiestaCodiceCui";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

		return PG_LOAD_INSERISCI_RICHIESTA_CODICE_CUI;
	}

}