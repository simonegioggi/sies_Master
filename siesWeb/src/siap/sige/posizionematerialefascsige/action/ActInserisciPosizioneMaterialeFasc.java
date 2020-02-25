package siap.sige.posizionematerialefascsige.action;

import java.util.Calendar;
import java.util.Date;

import siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.posizionematerialefascsige.controller.IPosizioneMaterialeFascSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciPosizioneMaterialeFasc
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di PosizioneMaterialeFasc
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Engineering
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciPosizioneMaterialeFasc extends ActionSige implements ICostantiPosizioneMaterialeFasc {

	/**
	 * Azione di Inserimento del PosizioneMaterialeFasc
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Si ricava il Fascicolo SIGE dalla sessione
		FascicoloSigeEstesoModel lFascMod = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		if (lFascMod == null || lFascMod.getFascicoloSige() == null
				|| lFascMod.getFascicoloSige().getIdFascicoloSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Fascicolo non in sessione");

		// Prelevo Data Inizio
//		Calendar lCal = Calendar.getInstance();
		Date lDataInizio = DateUtils.getDate(getRequestIntParameter(CAMPO_ANNO_DATA_INIZIO),
				getRequestIntParameter(CAMPO_MESE_DATA_INIZIO),
				getRequestIntParameter(CAMPO_GIORNO_DATA_INIZIO),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.HOUR_OF_DAY),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.MINUTE),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.SECOND));

		// Valorizzazione del record da inserire
		PosizioneMaterialeFascModel lPosMod = new PosizioneMaterialeFascModel(
				PosizioneMaterialeFascModel.POSIZIONE_MATERIALE_FASCICOLO_SIGE);
		lPosMod.setCodPosizioneMateriale(getRequestStringParameter(CAMPO_COD_POSIZIONE_MATERIALE));
		lPosMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
		lPosMod.setFasSieIdFascicoloSiep(lFascMod.getFascicoloSige().getIdFascicoloSige());
		lPosMod.setCodStatoProcedimento(lFascMod.getFascicoloSige().getCodStatoFascicolo());
		lPosMod.setDescrStatoProcedimento(lFascMod.getFascicoloSige().getDescrStatoFascicolo());
		lPosMod.setDataInizio(lDataInizio);
		lPosMod.setDataInserimento(DateUtils.getSysDate());
		lPosMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPosMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// Inserimento
		IPosizioneMaterialeFascSige lCtrlPosMatFasSige = SIGELookupRemote
				.getPosizioneMaterialeFascSigeRemote();
		/*PosizioneMaterialeFascModel llPosModRet = */lCtrlPosMatFasSige
				.ExInserisciPosizioneMaterialeFasc(lPosMod);

		// Dettaglio
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sige.posizionematerialefascsige.action.ActLoadDettaglioPosizioneMaterialeFasc");
		lPage.setParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP, lPosMod.getFasSieIdFascicoloSiep().toString());

		return lPage.toString();
	}

}