package siap.sico.soggetto.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.lock.model.LockModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActModificaSoggetto
 * </p>
 * <p>
 * Description: Classe Action per la modifica di Soggetto
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaSoggetto extends ActionSiap implements ICostantiSoggetto {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Soggetto modificato
	protected SoggettoModel lSogMod;
	// Soggetto prima della modifica
	protected SoggettoModel lSogVec;
	// Controller del Soggetto
	protected ISoggetto lSogCtrl;

	/**
	 * Azione di Modifica del Soggetto
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("soggetto", getRequestStringParameter(CAMPO_ID_SOGGETTO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il " + lck.getEntity()
					+ " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		BigDecimal idSoggetto = this.getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO);
		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			// paramentro passato solo nel caso di iscrizione guidata
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		lSogVec = lSogCtrl.ExRicercaSoggettoByKey(idSoggetto);

		// riempie il model
		lSogMod = new SoggettoModel();

		lSogMod.setIdSoggetto(getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO));
		lSogMod.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase());
		// lSogMod.setCodCs(getRequestStringParameter(CAMPO_COD_CS).toUpperCase());
		lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS).toUpperCase());
		lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lSogMod.setAnnoNascita(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_NASCITA));
		lSogMod.setMeseNascita(getRequestBigDecimalParameter(CAMPO_MESE_DATA_NASCITA));
		lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
				CAMPO_GIORNO_DATA_NASCITA));
		if (!this.isRequestParameterNullObj(CAMPO_ANNO_DATA_COMMESSO_REATO)
				&& !this.isRequestParameterNullObj(CAMPO_MESE_DATA_COMMESSO_REATO)
				&& !this.isRequestParameterNullObj(CAMPO_GIORNO_DATA_COMMESSO_REATO)) {
			lSogMod.setDataReatoSius(getRequestDateParameter(CAMPO_ANNO_DATA_COMMESSO_REATO,
					CAMPO_MESE_DATA_COMMESSO_REATO, CAMPO_GIORNO_DATA_COMMESSO_REATO));
		}
		lSogMod.setDataNascitaPresunta(getRequestStringParameter(CAMPO_DATA_NASCITA_PRESUNTA));

		// MERGE v10 COLLAUDO: aggiunti controlli preventivi
		if (!this.isRequestParameterNullObj(CAMPO_ETA_PRESUNTA_ANNI))
			lSogMod.setEtaPresuntaAnni(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_ANNI));
		if (!this.isRequestParameterNullObj(CAMPO_ETA_PRESUNTA_MESI))
			lSogMod.setEtaPresuntaMesi(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_MESI));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
			//lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(
			lComMod = new ComuneModel(getDatiComuneByCodDescr(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
					// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
					//getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
					getDatiComuneByDescrOmonimia(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		}

		lSogMod.setCodComuneNascita(lComMod.getCodComune());
		lSogMod.setCodProvinciaNascita(lComMod.getCodProvincia());
		lSogMod.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("lSogMod.GetNazionalita = " + lSogMod.getNazionalita());

		String lPage = modificaSoggetto();
		return lPage;
	}

	protected String modificaSoggetto() throws F3BException {

		setRequestAttribute("contaUffici", "0");
		setRequestAttribute("soggettonuovo", lSogMod);
		setRequestAttribute("soggettovecchio", lSogVec);
		setRequestAttribute("chiaveUfficio", getCodUfficioUtenteConnesso());

		String lPage = "";
		Vector lFascicoliSoggetti = new Vector();

		// VERIFICO SE IL SOGGETTO è DI SIUS O DI SEP E FACCIO LA RICERCA DEI FASCICOLI IN BASE A QUESTO
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute("UtenteConnesso");
		ProfileModel lProfilo = (ProfileModel) lUtenteMod.getUserProfile();
		setRequestAttribute("profilo", lProfilo.getProfileId());
		String profilo = lProfilo.getProfileId().toString();

		if (lProfilo.isSige()) {

			IFascicoloSige lFasSigeCtrl = SIGELookupRemote.getFascicoloSigeRemote();
			FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
			lFascicolo.setSogIdSoggetto(lSogMod.getIdSoggetto());
			lFascicoliSoggetti = lFasSigeCtrl.ExRicercaFascicoloSige(lFascicolo);

			if (lFascicoliSoggetti.size() > 0) {

				Vector lFascicoliSigexSogUff = new Vector();
				FascicoloSigeModel lFascModVerifica = (FascicoloSigeModel) lFascicoliSoggetti.get(0);
				lFascicoliSigexSogUff.add(lFascModVerifica);
				setRequestAttribute("fascicoli", lFascicoliSoggetti);
				setRequestAttribute("fascicoliUfficio", lFascicoliSigexSogUff);
				return IWebConstants.ROOT_DIR
						+ "files/siap/sico/storicosoggetto/LoadModificaSoggettoPerFascicolo.jsp";
			}
		} else if (lProfilo.isSius()) {

			// FascicoloSiusModel lFascSiusMod = new FascicoloSiusModel();
			IFascicoloSius lFascSogSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			try {
				lFascicoliSoggetti = lFascSogSiusCtrl.ExRicercaFascicoloSiusBySoggettoForStorico(lSogMod);
			} catch (Exception sEx) {
			}

			if (lFascicoliSoggetti.size() > 0) {
				Vector FascicoliSiusUfficio = new Vector();
				FascicoloGPModel FascSiusGP = (FascicoloGPModel) lFascicoliSoggetti.get(0);
				// FascicoloSiusModel FascSiusModVerifica = (FascicoloSiusModel)
				// FascSiusGP.getFascicoloSiusModel();
				FascicoliSiusUfficio.add(FascSiusGP);
				setRequestAttribute("fascicoli", lFascicoliSoggetti);
				setRequestAttribute("fascicoliUfficio", FascicoliSiusUfficio);
				return IWebConstants.ROOT_DIR
						+ "files/siap/sico/storicosoggetto/LoadModificaSoggettoPerFascicolo.jsp";
			}
		} else {

			FascicoloSiepModel lFascMod = new FascicoloSiepModel();
			lFascMod.setSogIdSoggetto(lSogMod.getIdSoggetto());
			IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

			try {
				lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoloSiepSoggetto(lFascMod);
			} catch (SIEPException sEx) {
			}

			if (lFascicoliSoggetti.size() > 0) { // Paolo Cherubini 12 dicembre 2011
													// aggiungo il ciclo sui fascicolo per scartare se
													// presente un cumulo
				for (int i = 0; i < lFascicoliSoggetti.size(); i++) {
					Vector FascicoliUfficio = new Vector();
					FascicoloSiepModel FascModVerifica = (FascicoloSiepModel) lFascicoliSoggetti.get(i);
					int NumFasc = FascModVerifica.getChiaveProgr().intValue();
					if (!(NumFasc > 700000 && NumFasc < 800000)) {
						FascicoliUfficio.add(FascModVerifica);
						setRequestAttribute("fascicoli", lFascicoliSoggetti);
						setRequestAttribute("fascicoliUfficio", FascicoliUfficio);
						return IWebConstants.ROOT_DIR
								+ "files/siap/sico/storicosoggetto/LoadModificaSoggettoPerFascicolo.jsp";
					}
				}
			}
		}

		// Il soggetto non ha fascicoli
		SoggettoModel lSogRet = new SoggettoModel();
		if (lFascicoliSoggetti.size() == 0) {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("lSogMod.GetNazionalita 2 = " + lSogMod.getNazionalita());

			lSogRet = lSogCtrl.ExModificaSoggettoStorico(lSogMod, profilo, lSogVec, null);
			lSogRet.setMessage("Aggiornamento");

			// if (lSogRet == null)
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			// siesLogger.debug("colpa di lSogRet");

			if (lSogRet.getMessage() == null) {
				lSogRet.setMessage("Problemi durante la modifica del soggetto");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("colpa di getMessage");
			}

			setRequestAttribute("soggetto", lSogRet);
			if (lSogRet.getMessage().startsWith("Aggiornamento")) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&" + CAMPO_ID_SOGGETTO + "="
						+ lSogRet.getIdSoggetto().toString();
			} else {
				lPage = IWebConstants.PG_MESSAGE;
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, lSogRet.getMessage());
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction("siap.sico.soggetto.action.ActLoadDettaglioSoggetto");
				lRedirigi.setParameter(CAMPO_ID_SOGGETTO, lSogRet.getIdSoggetto().toString());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			}
		}

		return lPage;
	}

}