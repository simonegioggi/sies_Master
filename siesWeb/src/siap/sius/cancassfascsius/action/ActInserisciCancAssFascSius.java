package siap.sius.cancassfascsius.action;

import java.util.Calendar;
import java.util.Date;

import siap.sico.web.ActionSiap;
import siap.sius.cancassfascsius.controller.ICancAssFascSius;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciCancAssFascSius
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di CancAssFascSius
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
public class ActInserisciCancAssFascSius extends ActionSiap implements ICostantiCancAssFascSius {

	/**
	 * Azione di Inserimento del CancAssFascSius
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Si ricava il Fascicolo SIUS dalla sessione
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (lFasGPMod == null || lFasGPMod.getFascicoloSiusModel() == null
				|| lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Fascicolo non in sessione");

		// Lettura Data Inizio
//		Calendar lCal = Calendar.getInstance();
		Date lDataInizio = DateUtils.getDate(getRequestIntParameter(CAMPO_ANNO_DATA_INIZIO),
				getRequestIntParameter(CAMPO_MESE_DATA_INIZIO),
				getRequestIntParameter(CAMPO_GIORNO_DATA_INIZIO),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.HOUR_OF_DAY),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.MINUTE),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.SECOND));

		// Valorizzazione del record da inserire
		CancAssFascSiusModel lCancAssFasc = new CancAssFascSiusModel();
		lCancAssFasc
				.setCodCancelleriaAssegnataria(getRequestStringParameter(CAMPO_COD_CANCELLERIA_ASSEGNATARIA));
		lCancAssFasc.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
		lCancAssFasc.setFasSiusIdFascicoloSius(lFasGPMod.getGeneraleProcedimentoModel()
				.getFasSiuIdFascicoloSius());
		lCancAssFasc.setCodStatoProcedimento(lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo());
		lCancAssFasc.setDescrStatoProcedimento(lFasGPMod.getFascicoloSiusModel().getDescrStatoFascicolo());
		lCancAssFasc.setDataInizio(lDataInizio);
		lCancAssFasc.setDataInserimento(DateUtils.getSysDate());
		lCancAssFasc.setCodOperatoreInserimento(getCodUtenteConnesso());
		lCancAssFasc.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// Inserimento
		ICancAssFascSius lCancAssFascCtrl = SIUSLookupRemote.getCancAssFascSiusRemote();
		lCancAssFasc = lCancAssFascCtrl.ExInserisciCancAssFascSius(lCancAssFasc);

		// Dettaglio
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.cancassfascsius.action.ActLoadDettaglioCancAssFascSius");
		lPage.setParameter(CAMPO_FAS_SIUS_ID_FASCICOLO_SIUS, lCancAssFasc.getFasSiusIdFascicoloSius()
				.toString());

		return lPage.toString();
	}

}