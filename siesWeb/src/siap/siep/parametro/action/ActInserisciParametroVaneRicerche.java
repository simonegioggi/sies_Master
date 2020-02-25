package siap.siep.parametro.action;

/**
* <p>Title: ActInserisciParametroVaneRicerche</p>
* <p>Description: Classe Action per l'inserimento di Parametro</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciParametroVaneRicerche extends ActionSiap implements ICostantiParametro {

	/**
	 * Azione di Inserimento del Parametro per Vane Ricerche
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		ParametroModel lParMod = new ParametroModel();
		// Ricerca se esiste Periodo per quell'Ufficio
		if (this.getRequestStringParameter("Scad").equals("OE")) // ORDINE ESECUZIONE
			lParMod.setNomeParametro("VANE RICERCHE");
		else if (this.getRequestStringParameter("Scad").equals("VVR")) // VERBALE VANE RICERCHE
			lParMod.setNomeParametro("VANE RICERCHE PERVENUTO");

		lParMod.setCodUfficioValidita(getCodUfficioUtenteConnesso());
		IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
		Vector lParVect = lCtrlPar.ExRicercaParametroUfficioConnesso(lParMod);

		if (lParVect != null && lParVect.size() != 0) { // Aggiorna
			ParametroModel lParamMod = (ParametroModel) lParVect.firstElement();
			lParMod.setIdParametro(lParamMod.getIdParametro());
			lParMod.setCodUfficioAggiormanento(lParamMod.getCodUfficioInserimento());
			lParMod.setCodOperatoreAggiornamento(lParamMod.getCodOperatoreInserimento());
			lParMod.setDataAggiornamento(DateUtils.getSysDate());
			lParMod.setCodUfficioValidita(lParamMod.getCodUfficioValidita());
			// Anni
			if (getRequestBigDecimalParameter(CAMPO_ANNI) == null) {
				lParMod.setAnni(new BigDecimal(0));
			} else {
				lParMod.setAnni(getRequestBigDecimalParameter(CAMPO_ANNI));
			}
			// Mesi
			if (getRequestBigDecimalParameter(CAMPO_MESI) == null) {
				lParMod.setMesi(new BigDecimal(0));
			} else {
				lParMod.setMesi(getRequestBigDecimalParameter(CAMPO_MESI));
			}
			// Giorni
			if (getRequestBigDecimalParameter(CAMPO_GIORNI) == null) {
				lParMod.setGiorni(new BigDecimal(0));
			} else {
				lParMod.setGiorni(getRequestBigDecimalParameter(CAMPO_GIORNI));
			}

			lParMod.setDataInizioValidita(DateUtils.getSysDate());

			IParametro lCtrlAgg = SIEPLookupRemote.getParametroRemote();
			lCtrlAgg.ExModificaParametro(lParMod);

		} else { // Inserisci
			lParMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lParMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lParMod.setDataInserimento(DateUtils.getSysDate());

			// Anni
			if (getRequestBigDecimalParameter(CAMPO_ANNI) == null) {
				lParMod.setAnni(new BigDecimal(0));
			} else {
				lParMod.setAnni(getRequestBigDecimalParameter(CAMPO_ANNI));
			}
			// Mesi
			if (getRequestBigDecimalParameter(CAMPO_MESI) == null) {
				lParMod.setMesi(new BigDecimal(0));
			} else {
				lParMod.setMesi(getRequestBigDecimalParameter(CAMPO_MESI));
			}
			// Giorni
			if (getRequestBigDecimalParameter(CAMPO_GIORNI) == null) {
				lParMod.setGiorni(new BigDecimal(0));
			} else {
				lParMod.setGiorni(getRequestBigDecimalParameter(CAMPO_GIORNI));
			}

			lParMod.setDataInizioValidita(DateUtils.getSysDate());

			IParametro lCtrl = SIEPLookupRemote.getParametroRemote();
			ParametroModel lParametroModel = lCtrl.ExInserisciParametro(lParMod);
			lParMod.setIdParametro(lParametroModel.getIdParametro());

		}

		// setta la risposta nella request
		// setRequestAttribute("parametro", lParMod);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.parametro.action.ActLoadDettaglioParametro&" + CAMPO_ID_PARAMETRO + "="
				+ lParMod.getIdParametro().toString();
		return lPage;
	}

}