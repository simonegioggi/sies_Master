package siap.sius.permesso.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel; // STUB 11/07/2005 Correzione codice comune.
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActRicercaSoggettiConLicenza - Azione di ricerca dei Soggetti con Procedimenti di sorveglianza relativi a
 * Permessi
 *
 * @version 1.0
 */
public class ActRicercaSoggettiConLicenza extends ActionSiap
		implements ICostantiFascicoloSius, ICostantiPermesso {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		SoggettoModel lSogMod = new SoggettoModel();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Si Riempie il model del Soggetto.
		lSogMod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));
		// STUB 11/07/2005 Correzione Codice comune.
		// lSogMod.setCodComuneNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA));

		/*
		 * 20210524 MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni. if
		 * (!this.isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA) &&
		 * getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA).length() > 1) { ComuneModel
		 * lComMod = new ComuneModel(getCodComuneByDescr(
		 * getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		 * lSogMod.setCodComuneNascita(lComMod.getCodComune()); }
		 */
		// Recupero dati del Comune di nascita
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = new ComuneModel(
					getDatiComuneByCodDescr(getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
							getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(getDatiComuneByDescrOmonimia(
					getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		}
		// 20250612 [SG]: risolto problema ricerca soggetto col "-" pari al cod comune nascita
		// Ticket#20250612016 - SIES - ricerche soggetto
		String codComuneNascita = "-".equals(lComMod.getCodComune()) ? "" : lComMod.getCodComune();
		lSogMod.setCodComuneNascita(codComuneNascita);

		if (getRequestStringParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA).length() > 2)
			lSogMod.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
					ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));

		if (!getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA).equals("-"))
			lSogMod.setCodStatoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));

		lSogMod.setPaternita(getRequestStringParameter(ICostantiSoggetto.CAMPO_PATERNITA));
		lSogMod.setCodCs(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_CS));

		// UtenteModel lUtenteMod = new UtenteModel(
		// (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		// Recupero informazioni per i filtri di ricerca.
		String strCodUfficioUtenteConnesso = getUfficioUtenteConnesso().getCodUfficio();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String strCodUfficioOTribunale = new String();
		String lCodCompetenza = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO);

		// Se è stato selezionato il 2 criterio di competenza.
		if (lCodCompetenza.equals("1")) {
			// Recupero del codice Ufficio di Sorveglianza (se è connesso l'utente TDS)
			// oppure del codice del Tribunale di Sorveglianza ( se è connesso l'utente UDS)
			IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();

			String strCodDistretto = getUfficioUtenteConnesso().getCodDistretto();
			String strCodComune = getUfficioUtenteConnesso().getCodComune();
			String strTipoUfficioRichiesto = new String();
			// MERGE v10 COLLAUDO: aggiunte casistiche
			if (strCodTipoUfficio.equals("TDS")) {
				strTipoUfficioRichiesto = "UDS";
			} else if (strCodTipoUfficio.equals("UDS")) {
				strTipoUfficioRichiesto = "TDS";
			} else if (strCodTipoUfficio.equals("UDSM")) {
				strTipoUfficioRichiesto = "TDSM";
			} else if (strCodTipoUfficio.equals("TDSM")) {
				strTipoUfficioRichiesto = "UDSM";
			}

			strCodUfficioOTribunale = (lUctrl.getUfficioUDSTDS(strCodDistretto, strTipoUfficioRichiesto,
					strCodComune)).getCodUfficio();
		}

		String lCodDistretto = "";
		if (lCodCompetenza.equals("2"))
			lCodDistretto = getUfficioUtenteConnesso().getCodDistretto();

		if (lCodCompetenza.equals("3"))
			lCodDistretto = lCodCompetenza;

		String lIncludeRigettati = "";
		if (isRequestChecked(ICostantiPermesso.CAMPO_INCLUDE_RIGETTATI))
			lIncludeRigettati = "S";

		String lCodLicenza = getRequestStringParameter(CAMPO_COD_LICENZA);
		String lDescrLicenza = DecodificheUtils
				.getDescbyCode(DecodificheManager.getInstance().getTipoLicenza(), lCodLicenza);

		Date dataDalInCancelleria = (getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		Date dataAlInCancelleria = (getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));

		// Chiama il controller.
		IPermesso lPermCtrl = SIUSLookupRemote.getPermessoRemote();
		Vector lLicenzeSoggetti = lPermCtrl.ExRicercaLicenzeBySoggettoPagina(lSogMod,
				strCodUfficioUtenteConnesso, strCodUfficioOTribunale, lCodDistretto, lIncludeRigettati,
				lCodLicenza, dataDalInCancelleria, dataAlInCancelleria, Integer.parseInt(lPagina));

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lLicenzeSoggetti != null) {
			SoggettoModel lSoggetto = ((FascicoloGPModel) lLicenzeSoggetti.get(0)).getFascicoloSiusModel()
					.getSoggetto();
			setRequestAttribute("soggetto", lSoggetto);
			setSessionAttribute("soggetto", lSoggetto);
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("ufUtConnesso", strCodUfficioUtenteConnesso);
		setRequestAttribute("ufOTribunale", strCodUfficioOTribunale);
		setRequestAttribute("codDistretto", lCodDistretto);
		setRequestAttribute("lIncludeRigettati", lIncludeRigettati);
		setRequestAttribute("codLicenza", lCodLicenza);
		setRequestAttribute("descrLicenza", lDescrLicenza);
		setRequestAttribute("dataDalInCancelleria",
				DateUtils.getDateToString(dataDalInCancelleria, "dd/MM/yyyy"));
		setRequestAttribute("dataAlInCancelleria",
				DateUtils.getDateToString(dataAlInCancelleria, "dd/MM/yyyy"));
		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lPermCtrl.ExGetNumRicercaLicenzeBySoggetto(lSogMod, strCodUfficioUtenteConnesso,
					strCodUfficioOTribunale, lCodDistretto, lIncludeRigettati, lCodLicenza,
					dataDalInCancelleria, dataAlInCancelleria);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("CountRisultati: " + CountRisultati);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// Setta la risposta nella request.
		setRequestAttribute("fascicoli", lLicenzeSoggetti);

		lReturnPage = ICostantiPermesso.PG_RICERCA_SOGGETTICONLICENZA;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn(" lReturnPage = " + lReturnPage);

		// restituisce la jsp di VIEW
		return lReturnPage;
	}

}