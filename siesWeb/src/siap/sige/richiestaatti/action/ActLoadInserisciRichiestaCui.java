package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.controller.UfficioController;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.notiziareato.controller.NotiziaReatoController;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.web.ActionSige;
import siap.web.ISIAPCostantiWeb;

/**
 *
 * <p>
 * Title: ActLoadInserisciRichiestaCui
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
public class ActLoadInserisciRichiestaCui extends ActionSige implements ICostantiRichiestaAtti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// OOKKKKKKKthis.isFascicoloSiepDiCompetenza();
		// OKKKKKKthis.isEventoNonValidato();

		// ------Controllo se ci sono Notizia di reato da importare da rege
		// ------per questo fascicolo NECESSARIO????????

		// IstruttoriaController lIstruttoria = new IstruttoriaController();
		// String lKeyFileRege
		// =lIstruttoria.ExControllaNotizieDiReatoRege((SoggettoModel)getSessionAttribute("soggetto"),(SentenzaModel)getSessionAttribute("sentenza"));
		// if(lKeyFileRege!=null)
		// setRequestAttribute("IdRegeFile",lKeyFileRege);

		// Recupero ilprovvedimento SIGE

		BigDecimal lIdFascicolo = null;
		FascicoloSigeEstesoModel lFasEsteso = null;

		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca Provvedimendi da ID FASCICOLO->" + lIdFascicolo);
		} else {
			lFasEsteso = this.getFascicoloSigeEstesoInSessione();
			lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca Provvedimendi da sessione");
		}

		// Trova le Notizie di Reato
		// chiama il controller
		NotiziaReatoController lCtrl = new NotiziaReatoController();

		// Instanzia il vettore
		// se non ci sono notizie di reato accedo comunque alla pagina
		Vector lVect;
		try {
			// lVect = lCtrl.ExRicercaNotiziaReatoByIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			lVect = lCtrl.ExRicercaNotiziaReatoByIdFascicoloSige(lIdFascicolo);
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
				DateUtils.getDateToString(lFasEsteso.getFascicoloSige().getDataIscrizione(), "dd/MM/yyyy"));

		// IMPOSTAZIONI PER FUNZIONALITà BACK
		String lAzione = "siap.sige.richiestaatti.action.ActLoadInserisciRichiestaCui";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

		return PG_LOAD_INSERISCI_RICHIESTA_CUI;
	}

}