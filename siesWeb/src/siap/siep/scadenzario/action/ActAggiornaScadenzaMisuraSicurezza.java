package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.parametro.action.ICostantiParametro;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * MEV_39
 * <p>
 * Title: ActAggiornaScadenzaMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per l'aggiornamento della Scadenza ella misura di sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActAggiornaScadenzaMisuraSicurezza extends ActionSiap implements ICostantiParametro {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		ParametroModel pm = new ParametroModel();
		// Ricerca se esiste Periodo per quell'Ufficio
		pm.setCodUfficioValidita(getCodUfficioUtenteConnesso());
		pm.setNomeParametro("INIZIO MISURA");
		IParametro ip = SIEPLookupRemote.getParametroRemote();
		Vector parametri = ip.ExRicercaParametroUfficioConnesso(pm);

		if (parametri != null && parametri.size() != 0) { // Aggiorna
			ParametroModel pam = (ParametroModel) parametri.firstElement();
			pm.setIdParametro(pam.getIdParametro());
			pm.setCodUfficioAggiormanento(pam.getCodUfficioInserimento());
			pm.setCodOperatoreAggiornamento(pam.getCodOperatoreInserimento());
			pm.setDataAggiornamento(DateUtils.getSysDate());
			pm.setCodUfficioValidita(pam.getCodUfficioValidita());
			// Anni
			if (getRequestBigDecimalParameter(CAMPO_ANNI) == null) {
				pm.setAnni(new BigDecimal(0));
			} else {
				pm.setAnni(getRequestBigDecimalParameter(CAMPO_ANNI));
			}
			// Mesi
			if (getRequestBigDecimalParameter(CAMPO_MESI) == null) {
				pm.setMesi(new BigDecimal(0));
			} else {
				pm.setMesi(getRequestBigDecimalParameter(CAMPO_MESI));
			}
			// Giorni
			if (getRequestBigDecimalParameter(CAMPO_GIORNI) == null) {
				pm.setGiorni(new BigDecimal(0));
			} else {
				pm.setGiorni(getRequestBigDecimalParameter(CAMPO_GIORNI));
			}
			pm.setDataInizioValidita(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			ip.ExModificaParametro(pm);
			throw new F3BException(F3BException.USER_MESSAGE, "Aggiornamento Avvenuto con Successo");
		} else { // Inserisci
			pm.setCodOperatoreInserimento(getCodUtenteConnesso());
			pm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			pm.setDataInserimento(DateUtils.getSysDate());
			// Anni
			if (getRequestBigDecimalParameter(CAMPO_ANNI) == null) {
				pm.setAnni(new BigDecimal(0));
			} else {
				pm.setAnni(getRequestBigDecimalParameter(CAMPO_ANNI));
			}
			// Mesi
			if (getRequestBigDecimalParameter(CAMPO_MESI) == null) {
				pm.setMesi(new BigDecimal(0));
			} else {
				pm.setMesi(getRequestBigDecimalParameter(CAMPO_MESI));
			}
			// Giorni
			if (getRequestBigDecimalParameter(CAMPO_GIORNI) == null) {
				pm.setGiorni(new BigDecimal(0));
			} else {
				pm.setGiorni(getRequestBigDecimalParameter(CAMPO_GIORNI));
			}
			pm.setDataInizioValidita(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			ip.ExInserisciParametro(pm);
			throw new F3BException(F3BException.USER_MESSAGE, "Inserimento Avvenuto con Successo");
		}
	}

}