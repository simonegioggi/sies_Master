package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

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
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaSoggettiConProcDiEsecuzioneSS extends ActionSiusMinor implements
		ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		this.setLinkRitorno();
		SoggettoModel lSogMod = new SoggettoModel();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Si Riempie il model del Soggetto.
		lSogMod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));

		// Correzione Codice comune.
		if (!this.isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)
				&& getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA).length() > 1) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
			lSogMod.setCodComuneNascita(lComMod.getCodComune());
		}

		if (getRequestStringParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA).length() > 2)
			lSogMod.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
					ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));

		if (!getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA).equals("-"))
			lSogMod.setCodStatoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));

		lSogMod.setPaternita(getRequestStringParameter(ICostantiSoggetto.CAMPO_PATERNITA));
		lSogMod.setCodCs(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_CS));

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
			if (strCodTipoUfficio.equals("TDS")) {
				strTipoUfficioRichiesto = "UDS";
			} else {
				strTipoUfficioRichiesto = "TDS";
			}
			strCodUfficioOTribunale = (lUctrl.getUfficioUDSTDS(strCodDistretto, strTipoUfficioRichiesto,
					strCodComune)).getCodUfficio();
		}

		String lCodDistretto = "";
		if (lCodCompetenza.equals("2"))
			lCodDistretto = getUfficioUtenteConnesso().getCodDistretto();

		if (lCodCompetenza.equals("3"))
			lCodDistretto = lCodCompetenza;

		String lIncludeArchiviati = "";
		if (isRequestChecked(ICostantiFascicoloSius.CAMPO_INCLUDE_ARCHIVIATI))
			lIncludeArchiviati = "S";

		String lCodContenuto = getRequestStringParameter(CAMPO_COD_CONTENUTO);
		String lDescrContenuto = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getOggettoProcedimento(), lCodContenuto);

		Date dataDalInCancelleria = (getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		Date dataAlInCancelleria = (getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));

		// Chiama il controller.
		IFascicoloSius lFascSogCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

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
		// setRequestAttribute("fascicoli", lVect);

		// Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoliBySoggetto(lSogMod,
		// strCodUfficioUtenteConnesso, strCodUfficioOTribunale, lCodDistretto, lIncludeArchiviati,
		// lCodContenuto, dataDalInCancelleria, dataAlInCancelleria ) ;
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
			setSessionAttribute("soggetto", lSoggetto); // STUB 26/01/2004 Da rivedere
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

		// Setta la risposta nella request.
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSius.PG_RICERCA_SOGGETTICONPROCDIESECUZIONESS;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lReturnPage =  " + lReturnPage);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lReturnPage; // restituisce la jsp di VIEW
	}
}