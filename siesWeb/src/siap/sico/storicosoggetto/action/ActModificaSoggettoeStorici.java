package siap.sico.storicosoggetto.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.lock.model.LockModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * ActModificaSoggettoeStorici - Classe Action per la modifica di Soggetto
 *
 * @version 1.0
 */
public class ActModificaSoggettoeStorici extends ActionSiap implements ICostantiSoggetto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Modifica del Soggetto e di inserimento dei storici con i fascicoli selezionati
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("soggetto", getRequestStringParameter(CAMPO_ID_SOGGETTO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}
		// id del vecchio soggetto
		BigDecimal IdSoggettoVecchio = this.getRequestBigDecimalParameter("IdSogg");

		// verifica se il profilo è sius o siep
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute("UtenteConnesso");

		ProfileModel lProfilo = lUtenteMod.getUserProfile();

		// riempie il model
		SoggettoModel lSogMod = new SoggettoModel();

		lSogMod.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase());
		lSogMod.setCodCs(getRequestStringParameter(CAMPO_COD_CS).toUpperCase());
		lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS).toUpperCase());
		lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lSogMod.setAnnoNascita(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_NASCITA));
		lSogMod.setMeseNascita(getRequestBigDecimalParameter(CAMPO_MESE_DATA_NASCITA));
		lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
				CAMPO_GIORNO_DATA_NASCITA));
		lSogMod.setDataNascitaPresunta(getRequestStringParameter(CAMPO_DATA_NASCITA_PRESUNTA));
		lSogMod.setEtaPresuntaAnni(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_ANNI));
		lSogMod.setEtaPresuntaMesi(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_MESI));

		// MERGE v10: aggiunti campi in estrazione
		if (!this.isRequestParameterNullObj(CAMPO_ANNO_DATA_COMMESSO_REATO)
				&& !this.isRequestParameterNullObj(CAMPO_MESE_DATA_COMMESSO_REATO)
				&& !this.isRequestParameterNullObj(CAMPO_GIORNO_DATA_COMMESSO_REATO))
			lSogMod.setDataReatoSius(getRequestDateParameter(CAMPO_ANNO_DATA_COMMESSO_REATO,
					CAMPO_MESE_DATA_COMMESSO_REATO, CAMPO_GIORNO_DATA_COMMESSO_REATO));

		// 20210830 MEV_21 Recupero Codice Comune di Nascita
		ComuneModel lComMod = null;
		if (!this.isRequestParameterNullObj(CAMPO_COD_COMUNE_NASCITA)) {
			lComMod = new ComuneModel(
					getDatiComuneByCodDescr(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA),
							getRequestStringParameter("DescrComuneNascita")));
		} else
			lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter("DescrComuneNascita")));
		lSogMod.setCodComuneNascita(lComMod.getCodComune());
		lSogMod.setCodProvinciaNascita(lComMod.getCodProvincia());
		lSogMod.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
		lSogMod.setFlagPresenzaFascicolo("S");
		lSogMod.setDataInserimento(DateUtils.getSysDate());
		lSogMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lSogMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lSogMod.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO));
		lSogMod.setNazionalita(getRequestStringParameter(CAMPO_NAZIONALITA));
		lSogMod.setPaternita(getRequestStringParameter(CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(CAMPO_NOME_MADRE));
		lSogMod.setSesso(getRequestStringParameter(CAMPO_SESSO));
		lSogMod.setAttoNascita(getRequestStringParameter(CAMPO_ATTO_NASCITA).toUpperCase());
		lSogMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lSogMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lSogMod.setDataAggiornamento(DateUtils.getSysDate());
		lSogMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());

		// Imposto la descrizione della nazione
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lSogMod.getCodStatoNascita());
		List lNazioni = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModel = lNazioni.indexOf(lDecMod);
		String lDescri = ((DecodificheModel) lNazioni.get(lIndModel)).getDescription();
		lSogMod.setDescrStatoNascita(lDescri);

		lSogMod.setDescrComuneNascita(lComMod.getDescrizione());

		// Imposto la descrizione del comune
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("PROVINCIA");
		lDecMod.setCode(lSogMod.getCodProvinciaNascita());
		List lProvincie = (List) DecodificheManager.getInstance().getProvincie();
		lIndModel = lProvincie.indexOf(lDecMod);
		lDescri = ((DecodificheModel) lProvincie.get(lIndModel)).getDescription();
		lSogMod.setDescrProvinciaNascita(lDescri);

		// Imposto la descrizione di Stato Cittadinanza (nel model sono NAZIONALITA e DESCRNAZIONALITA)
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lSogMod.getNazionalita());
		List lStatoCitt = (List) DecodificheManager.getInstance().getStatoCittadinanza();
		lIndModel = lStatoCitt.indexOf(lDecMod);
		lDescri = ((DecodificheModel) lStatoCitt.get(lIndModel)).getDescription();
		lSogMod.setDescrNazionalita(lDescri);

		// 20260415 [SG]: aggiunto controllo su CF che deve essere obbligatorio e conforme
		// SoggettoUtil.controllaCF(lSogMod);

		// Option lOption = new Option( DecodificheManager.getInstance().getNazioni());
		// prendo il numero dei fascicoli presenti per questo soggetto
		int lFascicoli = this.getRequestIntParameter("numerofascicoli");
		int lFascicoliAltriUff = this.getRequestIntParameter("numerofascicoliAltriUff");

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("numerofascicoli -> " + lFascicoli);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("numerofascicoliAltriUff -> " + lFascicoliAltriUff);

		// BigDecimal[] lKeyFascicoliPresenti = new BigDecimal[lFascicoli];
		Vector lKeyFascicoliSelezionati = new Vector();
		// boolean tutti = true;
		// boolean nessuno = true;
		// int i = 0;
		for (int y = 0; y < lFascicoli; y++) {
			if (!isRequestParameterNullObj("fascicolo" + y)) {
				// nessuno = false;
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("id Fascicolo selezionato "+ getRequestStringParameter("fascicolo"
				// + y));
				lKeyFascicoliSelezionati.add(getRequestBigDecimalParameter("fascicolo" + y));
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("id Fascicolo selezionato "+
				// getRequestBigDecimalParameter("fascicolo" + y));
			} else {
				// tutti = false;
			}
		}

		SoggettoModel lSogRet = new SoggettoModel();

		// Chiama il controller.
		if (lKeyFascicoliSelezionati.size() > 0) {
			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("fascicoli selezionati--->" + lKeyFascicoliSelezionati.size());

			// CONTROLLO SE è UTENTE è SIUS O SIEP o SIGE
			if (lProfilo.isSige()) { // SIGE
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("UTENTE---->SIGE");
				lSogRet = lSogCtrl.ExModificaSoggettoStoriciSige(lSogMod, lKeyFascicoliSelezionati,
						lFascicoli, lFascicoliAltriUff, IdSoggettoVecchio);
			} else if (lProfilo.isSiep()) { // SIEP
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("UTENTE---->SIEP");
				lSogRet = lSogCtrl.ExModificaSoggettoStorici(lSogMod, lKeyFascicoliSelezionati,
						IdSoggettoVecchio);
			} else { // sius
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("UTENTE---->SIUS");
				lSogRet = lSogCtrl.ExModificaSoggettoStoriciSius(lSogMod, lKeyFascicoliSelezionati,
						lFascicoli, lFascicoliAltriUff, IdSoggettoVecchio);
			}
		}
		// Controllo se nullo ????
		if (lSogRet == null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("colpa di lSogRet");

		// setta la risposta nella request
		setRequestAttribute("soggetto", lSogRet);
		String lPage = "";
		if (lSogRet.getMessage() == null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("colpa di getMessage");

		if (lSogRet.getMessage() != null) {
			lPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, lSogRet.getMessage());
			// Prepara la "pagina" di destinazione
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.sico.soggetto.action.ActLoadDettaglioSoggettoModificato");
			lRedirigi.setParameter(CAMPO_ID_SOGGETTO, lSogRet.getIdSoggetto().toString());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			// +"&mofificaFasSius="+lSogRet.getMessage()
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sico.soggetto.action.ActLoadDettaglioSoggettoModificato&" + CAMPO_ID_SOGGETTO
					+ "=" + lSogRet.getIdSoggetto().toString();
		}

		// pagina di ritorno
		return lPage;
	}

}