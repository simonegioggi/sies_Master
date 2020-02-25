package siap.sige.magistrato.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratosezione.model.MagistratoSezioneModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciMagistrato
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Magistrato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia s.p.a.
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciMagistrato extends ActionSiap implements ICostantiMagistrato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	MagistratoModel lMagMod = new MagistratoModel();

	/**
	 * Azione di Inserimento del Magistrato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		letturaDatiPreInserimento();

		IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();

		// 20170919: [SG] aggiunta query per controllo preventivo
		// 20171122: [EC] commentiamo il blocco sul magistrato. Deve essere possibile inserire lo stesso magistrato su diveersi uffici dello stesso distretto.
//		Vector<String> ufficiAttiviMAgistrato = lCtrl.controllaMagistratoAttivoAltriUffici(lMagMod);
//		if (ufficiAttiviMAgistrato.size() > 0) {
//			String listaUffici = "";
//			for (Iterator iterator = ufficiAttiviMAgistrato.iterator(); iterator.hasNext();) {
//				String ufficio = (String) iterator.next();
//				listaUffici = listaUffici.concat(" - " + ufficio);
//			}
//			throw new F3BException(
//					F3BException.USER_MESSAGE,
//					"Impossibile inserire il Magistrato! E' attivo nei seguenti uffici:"
//							+ listaUffici
//							+ ". Per poter procedere occorre impostare la data fine di validita' sull'ufficio in cui risulta attivo.");
//		}

		// controllo se è presente un'udienza legata alla sezione
		boolean associataUdienza = lCtrl.controlloCollegamentoUdienza(lMagMod);
		if (associataUdienza)
			throw new F3BException(
					F3BException.USER_MESSAGE,
					"Impossibile inserire il Magistrato e/o Sezione! Magistrato già presente oppure è presente già un'udienza legata alla sezione selezionata!");

		// MagistratoModel lMagModRet = lCtrl.ExInserisciMagistrato(lMagMod);
		MagistratoModel lMagModRet = null;
		lMagModRet = lCtrl.ExInserisciMagistratoMultiSezione(lMagMod, "I");

		// imposta il magistrato nella request
		setRequestAttribute("magistrato", lMagModRet);

		// Prepara la pagina di destinazione.
		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.magistrato.action.ActLoadDettaglioMagistrato");
		lRedir.setParameter(CAMPO_COD_MAGISTRATO, lMagModRet.getCodMagistrato().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lRedir.toString();
	}

	/**
	 * Lettura dei dati dalla request.
	 * 
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void letturaDati() throws F3BException {

		lMagMod.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
		lMagMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lMagMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lMagMod.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO));
		lMagMod.setEMailUfficio(getRequestStringParameter(CAMPO_E_MAIL_UFFICIO));
		lMagMod.setEMailPrivata(getRequestStringParameter(CAMPO_E_MAIL_PRIVATA));
		lMagMod.setNumCellulare(getRequestStringParameter(CAMPO_NUM_CELLULARE));
		lMagMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lMagMod.setDataInizioValidita(DateUtils.getSysDate());
		// 20170918: [SG] solo in modifica
		// lMagMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
		// CAMPO_MESE_DATA_FINE_VALIDITA,
		// CAMPO_GIORNO_DATA_FINE_VALIDITA));
		lMagMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMagMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lMagMod.setDataInserimento(DateUtils.getSysDate());

		// Legge le sezioni di appartenenza
		String[] lSezioni = (isRequestParameterNullObj(CAMPO_SEZIONE) == true ? new String[0]
				: getRequestStringParameters(CAMPO_SEZIONE));

		ArrayList lArrayList = new ArrayList();
		MagistratoSezioneModel lModel = null;

		// inizio controllo sezione
		boolean sezioneSelezionata = isRequestParameterNullEmptyObj(CAMPO_SEZIONE);
		// fine controllo sezione

		if (sezioneSelezionata) {
			// Sezione non seleziona dall'utente
			lModel = new MagistratoSezioneModel();
			lModel.setMagCodMagistrato(lMagMod.getCodMagistrato());
			// lModel.setSezIdSezione(new BigDecimal(lSezioni[i]));
			lModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lModel.setDataInserimento(DateUtils.getSysDate());
			lModel.setDataInizioAssegnazione(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_ASSEGNAZIONE,
					CAMPO_MESE_DATA_INIZIO_ASSEGNAZIONE, CAMPO_GIORNO_DATA_INIZIO_ASSEGNAZIONE));
			lModel.setDataFineAssegnazione(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_ASSEGNAZIONE,
					CAMPO_MESE_DATA_FINE_ASSEGNAZIONE, CAMPO_GIORNO_DATA_FINE_ASSEGNAZIONE));
			lArrayList.add(lModel);
		} else {
			// Sezione, selezionato dall'utente
			for (int i = 0; i < lSezioni.length; i++) {
				if (!lSezioni[i].equals("")) {
					lModel = new MagistratoSezioneModel();
					lModel.setMagCodMagistrato(lMagMod.getCodMagistrato());
					lModel.setSezIdSezione(new BigDecimal(lSezioni[i]));
					// lModel.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
					lModel.setCodOperatoreInserimento(getCodUtenteConnesso());
					lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lModel.setDataInserimento(DateUtils.getSysDate());
					lModel.setDataInizioAssegnazione(getRequestDateParameter(
							CAMPO_ANNO_DATA_INIZIO_ASSEGNAZIONE, CAMPO_MESE_DATA_INIZIO_ASSEGNAZIONE,
							CAMPO_GIORNO_DATA_INIZIO_ASSEGNAZIONE));
					lModel.setDataFineAssegnazione(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_ASSEGNAZIONE,
							CAMPO_MESE_DATA_FINE_ASSEGNAZIONE, CAMPO_GIORNO_DATA_FINE_ASSEGNAZIONE));
					lArrayList.add(lModel);
				}
			}
		}

		if (lArrayList.size() != 0)
			lMagMod.setMagistratoSezioni((MagistratoSezioneModel[]) lArrayList
					.toArray(new MagistratoSezioneModel[0]));
	}

	/**
	 * Lettura dei dati dalla request.
	 * 
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void letturaDatiPreInserimento() throws F3BException {

		lMagMod.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
		lMagMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lMagMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lMagMod.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO));
		lMagMod.setEMailUfficio(getRequestStringParameter(CAMPO_E_MAIL_UFFICIO));
		lMagMod.setEMailPrivata(getRequestStringParameter(CAMPO_E_MAIL_PRIVATA));
		lMagMod.setNumCellulare(getRequestStringParameter(CAMPO_NUM_CELLULARE));
		lMagMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lMagMod.setDataInizioValidita(DateUtils.getSysDate());
		// 20170918: [SG] solo in modifica
		// lMagMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
		// CAMPO_MESE_DATA_FINE_VALIDITA,
		// CAMPO_GIORNO_DATA_FINE_VALIDITA));
		lMagMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMagMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lMagMod.setDataInserimento(DateUtils.getSysDate());

		// Legge le sezioni di appartenenza
		String[] lSezioni = (isRequestParameterNullObj(CAMPO_SEZIONE) == true ? new String[0]
				: getRequestStringParameters(CAMPO_SEZIONE));

		ArrayList lArrayList = new ArrayList();
		MagistratoSezioneModel lModel = null;

		if (lSezioni != null && lSezioni.length > 0) {
			// Sezione, selezionato dall'utente
			for (int i = 0; i < lSezioni.length; i++) {
				if (!lSezioni[i].equals("")) {
					lModel = new MagistratoSezioneModel();
					lModel.setMagCodMagistrato(lMagMod.getCodMagistrato());
					lModel.setSezIdSezione(new BigDecimal(lSezioni[i]));
					// lModel.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
					lModel.setCodOperatoreInserimento(getCodUtenteConnesso());
					lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lModel.setDataInserimento(DateUtils.getSysDate());
					lModel.setDataInizioAssegnazione(getRequestDateParameter(
							CAMPO_ANNO_DATA_INIZIO_ASSEGNAZIONE, CAMPO_MESE_DATA_INIZIO_ASSEGNAZIONE,
							CAMPO_GIORNO_DATA_INIZIO_ASSEGNAZIONE));
					lModel.setDataFineAssegnazione(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_ASSEGNAZIONE,
							CAMPO_MESE_DATA_FINE_ASSEGNAZIONE, CAMPO_GIORNO_DATA_FINE_ASSEGNAZIONE));
					lArrayList.add(lModel);
				}
			}
		}

		if (lArrayList.size() != 0)
			lMagMod.setMagistratoSezioni((MagistratoSezioneModel[]) lArrayList
					.toArray(new MagistratoSezioneModel[0]));
	}

}