package siap.sius.udienza.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Title: ActInserisciUdienza
 * Description: Classe Action per l'inserimento di Udienza
 *
 * @version 1.0
 */
public class ActInserisciUdienzaUDS extends ActionSiap implements ICostantiUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Udienza
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		UdienzaModel lUdiMod = new UdienzaModel();

		// lUdiMod.setIdUdienza( getRequestBigDecimalParameter( CAMPO_ID_UDIENZA) );

		lUdiMod.setDataUdienza(getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
				CAMPO_GIORNO_DATA_UDIENZA));
		lUdiMod.setNumCollegio(getRequestBigDecimalParameter(CAMPO_NUM_COLLEGIO));
		lUdiMod.setCodPresidente(getRequestStringParameter(CAMPO_COD_PRESIDENTE));
		// lUdiMod.setCodGiudice1( getRequestStringParameter( CAMPO_COD_GIUDICE_1) );
		// lUdiMod.setCodGiudice2( getRequestStringParameter( CAMPO_COD_GIUDICE_2) );
		lUdiMod.setCodPg(getRequestStringParameter(CAMPO_COD_PG));
		// lUdiMod.setCodIdEsperto1( getRequestBigDecimalParameter( CAMPO_COD_ID_ESPERTO_1) );
		// lUdiMod.setCodIdEsperto2( getRequestBigDecimalParameter( CAMPO_COD_ID_ESPERTO_2) );
		lUdiMod.setCodIdAssistente(getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE));
		lUdiMod.setNumeroMaxFascicoli(getRequestBigDecimalParameter(CAMPO_NUMERO_MAX_FASCICOLI));
		// lUdiMod.setLuogoUdienza(StringUtils.convertSqlString(getRequestStringParameter(
		// CAMPO_LUOGO_UDIENZA)));
		lUdiMod.setLuogoUdienza(getRequestStringParameter(CAMPO_LUOGO_UDIENZA));

		lUdiMod.setOraInizio(getRequestStringParameter(CAMPO_ORA_INIZIO));
		lUdiMod.setMinInizio(getRequestStringParameter(CAMPO_MIN_INIZIO));
		lUdiMod.setOraFine(getRequestStringParameter(CAMPO_ORA_FINE));
		lUdiMod.setMinFine(getRequestStringParameter(CAMPO_MIN_FINE));

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lUdiMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lUdiMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lUdiMod.setDataInserimento(DateUtils.getSysDate());

		IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
		UdienzaModel lUdiModRet = lCtrl.ExInserisciUdienza(lUdiMod); // setta la risposta nella request

		// System.out
		// .println("====>>> Mi trovo in ActInserisciUdienzaUDS dopo di ExInserisciUdienzaUDS e
		// CAMPO_ID_UDIENZA = "
		// + lUdiModRet.getIdUdienza().toString());
		siesLogger.debug(
				"====>>> Mi trovo in ActInserisciUdienzaUDS dopo di ExInserisciUdienzaUDS e CAMPO_ID_UDIENZA = "
						+ lUdiModRet.getIdUdienza().toString());

		setRequestAttribute("udienza", lUdiModRet);

		// Modifica del 04/10/2013 mev "Revisione Misure di Sicurezza SIUS"
		// In fase di Fissazione Udienza, dare la possibilità all'utente di definire
		// una nuova udienza direttamente dalla pagina "Inserimento Fissazione Udienza"
		// senza passare dalle Funzioni Amministrative
		String checkInsFissUdienza = null;
		if (!this.isRequestParameterNullObj(ICostantiUdienza.CAMPO_CHECK_INS_FISS_UDIENZA)) {
			checkInsFissUdienza = this
					.getRequestStringParameter(ICostantiUdienza.CAMPO_CHECK_INS_FISS_UDIENZA);
		}

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.udienza.action.ActLoadDettaglioUdienzaUDS&" + CAMPO_ID_UDIENZA + "="
				+ lUdiModRet.getIdUdienza().toString() + "&CheckInsFissUdienza=" + checkInsFissUdienza;

		return lPage;
	}

}