package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiusMinor;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActRicercaSportello extends ActionSiusMinor implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		setLinkRitorno();
		SoggettoModel lSogMod = new SoggettoModel();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Si Riempie il model del Soggetto.
		lSogMod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));

		/* 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni.
		if (!this.isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)
				&& getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA).length() > 1) {
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(
					getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
			lSogMod.setCodComuneNascita(lComMod.getCodComune());
		} */
		// Recupero dati del Comune di nascita
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = new ComuneModel(getDatiComuneByCodDescr(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(getDatiComuneByDescrOmonimia(
					getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		}
		lSogMod.setCodComuneNascita(lComMod.getCodComune());

		if (getRequestStringParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA).length() > 2)
			lSogMod.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
					ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));

		if (!getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA).equals("-"))
			lSogMod.setCodStatoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));

		lSogMod.setPaternita(getRequestStringParameter(ICostantiSoggetto.CAMPO_PATERNITA));
		// 20190301 [SG]: modifica per il cui; NO CAMPO_COD_CS SI CAMPO_COD_AFIS
		lSogMod.setCodAfis(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_AFIS));

		// UtenteModel lUtenteMod = new UtenteModel(
		// (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		// lSogMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		// Recupero informazioni per i filtri di ricerca.
		String strCodUfficioUtenteConnesso = getUfficioUtenteConnesso().getCodUfficio();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String strCodUfficioOTribunale = new String();
		String lCodCompetenza = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO);

		// Se è stato selezionato il 2 criterio di competenza.
		if ("1".equals(lCodCompetenza)) {
			// Recupero del codice Ufficio di Sorveglianza (se è connesso l'utente TDS)
			// oppure del codice del Tribunale di Sorveglianza ( se è connesso l'utente UDS)
			IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();

			String strCodDistretto = getUfficioUtenteConnesso().getCodDistretto();
			String strCodComune = getUfficioUtenteConnesso().getCodComune();
			String strTipoUfficioRichiesto = new String();
			// MERGE v10 COLLAUDO: aggiunte casistiche
			if ("TDS".equals(strCodTipoUfficio))
				strTipoUfficioRichiesto = "UDS";
			else if ("UDS".equals(strCodTipoUfficio))
				strTipoUfficioRichiesto = "TDS";
			else if ("TDSM".equals(strCodTipoUfficio))
				strTipoUfficioRichiesto = "UDSM";
			else if ("UDSM".equals(strCodTipoUfficio))
				strTipoUfficioRichiesto = "TDSM";

			strCodUfficioOTribunale = (lUctrl.getUfficioUDSTDS(strCodDistretto, strTipoUfficioRichiesto,
					strCodComune)).getCodUfficio();
		}

		String lCodDistretto = "";
		if ("1".equals(lCodCompetenza))
			lCodDistretto = lCodCompetenza;

		if ("2".equals(lCodCompetenza))
			lCodDistretto = getUfficioUtenteConnesso().getCodDistretto();

		if ("3".equals(lCodCompetenza))
			lCodDistretto = lCodCompetenza;

		String lIncludeArchiviati = "S";

		if (isRequestChecked(ICostantiFascicoloSius.CAMPO_SOLO_ARCHIVIATI))
			lIncludeArchiviati = "A";

		String lCodContenuto = getRequestStringParameter(CAMPO_COD_CONTENUTO);
		String lDescrContenuto = DecodificheUtils
				.getDescbyCode(DecodificheManager.getInstance().getOggettoProcedimento(), lCodContenuto);

		Date dataDalInCancelleria = (getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		Date dataAlInCancelleria = (getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));

		// Chiama il controller.
		IFascicoloSius lFascSogCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoliBySoggettoPagina(lSogMod,
				strCodUfficioUtenteConnesso, strCodUfficioOTribunale, lCodDistretto, lIncludeArchiviati,
				lCodContenuto, dataDalInCancelleria, dataAlInCancelleria, Integer.parseInt(lPagina),
				checkMinori());

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSoggetti != null) {
			SoggettoModel lSoggetto = ((FascicoloGPModel) lFascicoliSoggetti.get(0)).getFascicoloSiusModel()
					.getSoggetto();
			setRequestAttribute("soggetto", lSoggetto);
			setSessionAttribute("soggetto", lSoggetto);
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("ufUtConnesso", strCodUfficioUtenteConnesso);
		setRequestAttribute("ufOTribunale", strCodUfficioOTribunale);
		setRequestAttribute("codDistretto", lCodDistretto);
		setRequestAttribute("lIncludeArchiviati", lIncludeArchiviati);
		setRequestAttribute("codContenuto", lCodContenuto);
		setRequestAttribute("descrContenuto", lDescrContenuto);
		setRequestAttribute("dataDalInCancelleria",
				DateUtils.getDateToString(dataDalInCancelleria, "dd/MM/yyyy"));
		setRequestAttribute("dataAlInCancelleria",
				DateUtils.getDateToString(dataAlInCancelleria, "dd/MM/yyyy"));
		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lFascSogCtrl.ExGetNumRicercaFascicoliBySoggetto(lSogMod,
					strCodUfficioUtenteConnesso, strCodUfficioOTribunale, lCodDistretto, lIncludeArchiviati,
					lCodContenuto, dataDalInCancelleria, dataAlInCancelleria);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// Setta la risposta nella request.
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSius.PG_RICERCA_SPORTELLO;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lReturnPage =  " + lReturnPage);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lReturnPage; // restituisce la jsp di VIEW
	}

}