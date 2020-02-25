package siap.sius.posizionematerialefascsius.action;

import java.util.Calendar;
import java.util.Date;

import siap.sico.web.ActionSiap;
import siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.posizionematerialefascsius.controller.IPosizioneMaterialeFascSius;
import siap.sius.util.SIUSLookupRemote;
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
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciPosizioneMaterialeFasc extends ActionSiap implements ICostantiPosizioneMaterialeFasc {

	/**
	 * Azione di Inserimento del PosizioneMaterialeFasc
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

		// Prelievo Data Inizio
		// STUB 15/02/2006 Date lDataInizio = getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO,
		// CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO );
//		Calendar lCal = Calendar.getInstance();
		Date lDataInizio = DateUtils.getDate(getRequestIntParameter(CAMPO_ANNO_DATA_INIZIO),
				getRequestIntParameter(CAMPO_MESE_DATA_INIZIO),
				getRequestIntParameter(CAMPO_GIORNO_DATA_INIZIO),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.HOUR_OF_DAY),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.MINUTE),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.SECOND));

		// Valorizzazione del record da inserire
		PosizioneMaterialeFascModel lPosMod = new PosizioneMaterialeFascModel(
				PosizioneMaterialeFascModel.POSIZIONE_MATERIALE_FASCICOLO_SIUS);
		lPosMod.setCodPosizioneMateriale(getRequestStringParameter(CAMPO_COD_POSIZIONE_MATERIALE));
		lPosMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
		lPosMod.setFasSieIdFascicoloSiep(lFasGPMod.getGeneraleProcedimentoModel().getFasSiuIdFascicoloSius());
		lPosMod.setCodStatoProcedimento(lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo());
		lPosMod.setDescrStatoProcedimento(lFasGPMod.getFascicoloSiusModel().getDescrStatoFascicolo());
		lPosMod.setDataInizio(lDataInizio);
		lPosMod.setDataInserimento(DateUtils.getSysDate());
		lPosMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPosMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// Inserimento
		IPosizioneMaterialeFascSius lCtrlPosMatFasSius = SIUSLookupRemote
				.getPosizioneMaterialeFascSiusRemote();
		/* PosizioneMaterialeFascModel llPosModRet = */lCtrlPosMatFasSius
				.ExInserisciPosizioneMaterialeFasc(lPosMod);

		// Dettaglio
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.posizionematerialefascsius.action.ActLoadDettaglioPosizioneMaterialeFasc");
		lPage.setParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP, lPosMod.getFasSieIdFascicoloSiep().toString());

		return lPage.toString();
	}

}