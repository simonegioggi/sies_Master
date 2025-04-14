package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaSoggettiConProcSige extends ActionSige implements ICostantiFascicoloSige {

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
		lSogMod.setCodCs(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_CS));

		// UtenteModel lUtenteMod = new UtenteModel(
		// (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		// Recupero informazioni per i filtri di ricerca.
		String strCodUfficioUtenteConnesso = getUfficioUtenteConnesso().getCodUfficio();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String lCodCompetenza = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_UFFICIO_DISTRETTO);

		String lCodDistretto = "";
		if (lCodCompetenza.equals("1"))
			lCodDistretto = lCodCompetenza;

		// Chiama il controller.
		IFascicoloSige lFascSigeCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		// MEV_57: aggiunto parametro di passaggio
		Vector lFascicoliSoggetti = lFascSigeCtrl.ExRicercaFascicoliBySoggettoPagina(lSogMod,
				strCodUfficioUtenteConnesso, lCodDistretto, Integer.parseInt(lPagina), checkMinori());

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSoggetti != null) {
			SoggettoModel lSoggetto = ((FascicoloSigeEstesoModel) lFascicoliSoggetti.get(0)).getSoggetto();
			setRequestAttribute("soggetto", lSoggetto);
			setSessionAttribute("soggetto", lSoggetto); // da rivedere
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("ufUtConnesso", strCodUfficioUtenteConnesso);
		setRequestAttribute("codDistretto", lCodDistretto);
		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			// MEV_57: aggiunto parametro di passaggio
			// CountRisultati = lFascSigeCtrl.ExGetNumRicercaFascicoliBySoggetto(lSogMod,
			// strCodUfficioUtenteConnesso, lCodDistretto, checkMinori());
			// Ticket#202311060117 - Ricerca  soggetti per procedimento Sige: ottimizzazione query
			CountRisultati = ((FascicoloSigeEstesoModel) lFascicoliSoggetti.get(0)).getFascicoloSige()
					.getCountRisultati();
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// Setta la risposta nella request.
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSige.PG_RICERCASOGGETTICONFASCICOLOSIGE;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lReturnPage =  " + lReturnPage);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// restituisce la jsp di VIEW
		return lReturnPage;
	}

}