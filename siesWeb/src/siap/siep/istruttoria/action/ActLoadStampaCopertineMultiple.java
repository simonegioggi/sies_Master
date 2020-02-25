package siap.siep.istruttoria.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.controller.IIstruttoria;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadStampaCopertineMultiple
 * 
 * @author Giselda De Vita
 *
 */
public class ActLoadStampaCopertineMultiple extends ActionSiap implements ICostantiIstruttoria

{
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE))
			lFasMod.setChiaveAnnoIniziale(
					getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE))
			lFasMod.setChiaveProgrIniziale(
					getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE))
			lFasMod.setChiaveAnnoFinale(
					getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE))
			lFasMod.setChiaveProgrFinale(
					getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE));

		this.setRequestAttribute(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE,
				getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE));
		this.setRequestAttribute(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE,
				getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE));
		this.setRequestAttribute(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE,
				getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE));
		this.setRequestAttribute(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE,
				getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE));

		lFasMod.setChiaveUfficio(this.getCodUfficioUtenteConnesso());

		String lPage = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoria/StampaCopertineMultiple.jsp";

		try {
			IIstruttoria lCtrl = SIEPLookupRemote.getIstruttoriaRemote();
			Vector lVect = lCtrl.ExCercaIntervalloFascicoli(lFasMod, false);

			this.setRequestAttribute("fascicoli", lVect);
		} catch (F3BException ex) {
			lPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Nessun procedimento nell'intervallo selezionato!");
		}

		return lPage;

	}

}