package siap.sius.produzioneatti.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.produzioneatti.model.ParereModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActdRicercaPareri
 * </p>
 * <p>
 * Description: Attiva la Ricerca delle richieste di parere sia per la sua visualizzazione che per la stampa.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaPareri extends ActionSius implements ICostantiProduzioneAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Model Parere usato per settare i parametri di filtro nella ricerca
	private ParereModel mParMod = new ParereModel();
	// Risultato della ricerca
	@SuppressWarnings("rawtypes")
	private Vector mVect = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		// Model usato per i parametri di ricerca
		mParMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());

		// Inizializzazione numero di pagina
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Lettura dei dati di filtro per la ricerca
		letturaFiltroRicerca();

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

		String lReturnPage = PG_RICERCAPARERI;
		if (!isRequestParameterNullObj("Stampa")) {
			// Ricerca completa
			mVect = lCtrl.ExRicercaPareriPaginata(mParMod, -1);
			if (mVect != null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Risultati ricerca completa : " + mVect.size());

			lReturnPage = Stampa();
		} else {
			// Bottone di ritorno
			setLinkRitorno();
			// Ricerca paginata
			mVect = lCtrl.ExRicercaPareriPaginata(mParMod, Integer.parseInt(lPagina));
			if (mVect != null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Risultati ricerca paginata : " + mVect.size());

			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetNumRicercaPareri(mParMod);
			else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
			setRequestAttribute("Pareri", mVect);

			// 28/03/2007 Visualizzazione Filtri di Ricerca Pareri.
			setRequestAttribute("FiltriPareri", mParMod);

			// Composizione della intestazione della pagina di uscita con il periodo di ricerca
			String lInTesta = " nel periodo "
					+ DateUtils.getDateToString(mParMod.getDataEmissione(), "dd/MM/yyyy") + " - "
					+ DateUtils.getDateToString(mParMod.getDataEmissione2(), "dd/MM/yyyy");
			setRequestAttribute("InTesta", lInTesta);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return lReturnPage;
	}

	/**
	 * Esegue stampa elenco dei pareri.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 * @return String Ritorna documneto rtf di stampa.
	 */
	private String Stampa() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .Stampa(): inizio ");

		String lRet = IWebConstants.PG_DOWNLOAD;

		// Si Recupera l'utente dalla sessione.
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		ByteArrayOutputStream lReport = lCtrlSta.ExPreStampaPareri(mParMod, mVect, lUtenteMod); // lCtrlSta.ExPreStampaRichiestaAtti(aEvento,
																								// lUfficio.getCodUfficio(),
																								// lUtenteMod);

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .Stampa(): fine ");

		return lRet;
	}

	/**
	 * Metodo private che esegue la lettura del filtro d'applicare per la ricerca.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	private void letturaFiltroRicerca() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .letturaFiltroRicerca(): inizio ");

		// Lettura Codice Tipo Parere
		String lCodTipoParere = getRequestStringParameter(ICostantiProduzioneAtti.CAMPO_COD_MOTIVO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Cod Tipo Parere : " + lCodTipoParere);

		mParMod.setCodMotivo(lCodTipoParere);
		if (lCodTipoParere.length() > 1)
			mParMod.setDescrMotivo(DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getMotivoProvvedimento(), mParMod.getCodMotivo()));
		else
			mParMod.setDescrMotivo("-");

		// Lettura Codice Contenuto
		String lCodContenuto = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("codice contenuto : " + lCodContenuto);

		mParMod.setCodOggettoProcedimento(lCodContenuto);
		if (lCodContenuto.length() > 1)
			mParMod.setDescrOggettoProcedimento(
					DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getOggettoProcedimento(),
							mParMod.getCodOggettoProcedimento()));
		else
			mParMod.setDescrOggettoProcedimento("-");

		// Recupera dalla request il criterio di Ordinamento e l'imposta nell'oggetto
		// ereditato da GenericModel mMessage
		mParMod.setMessage(getRequestStringParameter(CAMPO_TIPO_ORDINAMENTO));

		// Lettura intervallo data di emissione
		Date lDataIni = null, lDataFin = null;

		lDataIni = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);
		lDataFin = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE2, CAMPO_MESE_DATA_EMISSIONE2,
				CAMPO_GIORNO_DATA_EMISSIONE2);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("data iniziale : " + DateUtils.getDateToString(lDataIni, "dd/MM/yyyy"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("data finale : " + DateUtils.getDateToString(lDataFin, "dd/MM/yyyy"));

		mParMod.setDataEmissione(lDataIni);
		mParMod.setDataEmissione2(lDataFin);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .letturaFiltroRicerca(): fine ");

		// 27/03/2007 Lettura Codice Utente.
		String lCodiceUtente = getRequestStringParameter(ICostantiSicoJMS.CAMPO_COD_UTENTE);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Codice Utente : " + lCodiceUtente);
		mParMod.setCodiceUtente(lCodiceUtente);

		return;
	}

}