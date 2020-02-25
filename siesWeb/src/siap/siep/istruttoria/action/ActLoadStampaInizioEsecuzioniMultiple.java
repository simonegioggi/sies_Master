package siap.siep.istruttoria.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.utente.model.UtenteModel;
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
public class ActLoadStampaInizioEsecuzioniMultiple extends ActionSiap implements ICostantiIstruttoria

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

		UtenteModel lUtenteMod = this.getUtenteConnesso();

		lFasMod.setChiaveUfficio(this.getCodUfficioUtenteConnesso());
		lFasMod.setDescrTipoUfficio(
				this.getUfficioByCodUfficio(getCodUfficioUtenteConnesso()).getDescrTipoUfficio());
		lFasMod.setDescrComuneUfficio(
				this.getUfficioByCodUfficio(getCodUfficioUtenteConnesso()).getDescrComune());

		String lPage = IWebConstants.PG_MESSAGE;
		try {
			IIstruttoria lCtrl = SIEPLookupRemote.getIstruttoriaRemote();
			Vector lVect = lCtrl.ExCercaIntervalloFascicoli(lFasMod, true);
			this.setRequestAttribute("fascicoli", lVect);
		} catch (F3BException ex) {
			lPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Nessun procedimento nell'intervallo selezionato!");
		}

		try {

			IIstruttoria lCtrl = SIEPLookupRemote.getIstruttoriaRemote();
			BigDecimal lSequence = lCtrl.ExInserisciRichiestaJmsStampaInizioEsecuzioneMultiple(lFasMod,
					lUtenteMod, this.getUfficioUtenteConnesso());

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.istruttoria.action.ActIstruttorieGriglia");

			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			lPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Richiesta di Stampa Inizio Esecuzione per Intervalli sottomessa al sistema con id <font color=\"orange\">"
							+ lSequence + "</font>"
							+ ". <br> Stampare il documento dal tasto presente nella griglia delle istruttorie "
							+ "<a href=\"" + IWebConstants.PG_MAIN
							+ "?Action=siap.siep.stampadocumenti.action.ActRicercaStampaDocumenti\"> Verifica Stampa Inizio Esecuzione Multiple</a>.");

		} catch (F3BException ex) {
			lPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Servizio JMS non attivo. \n Rivolgersi all'amministratore del sistema.");
		}

		return lPage;

	}

}