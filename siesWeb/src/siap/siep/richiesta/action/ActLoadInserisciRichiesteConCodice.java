package siap.siep.richiesta.action;

/**
 * <p>Title: ActLoadInserisciRichiesteConCodice</p>
 * <p>Description: Azione Load delle form di inserimento delle Richieste al
 *    Giudice dell'esecuzione di Rideterminazione Pena per Amnistia e Indulto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadInserisciRichiesteConCodice extends ActionSiap implements ICostantiRichiesta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Invocata da VediCalcoloPenaValidataRichiestaGE.jsp, form delle stampe delle Richieste al GE. I
	 * parametri ricevuti in input sono: codice = codice motivo della richiesta testo = descrizione della
	 * richiesta
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		SentenzaModel lSentMod = lFascMod.getSentenza();
		if (lSentMod == null) {
			lSentMod = new SentenzaModel();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("sentenza null !!! ");

		}

		Vector reati = new Vector();
		EventoModel lEveMod = null;
		AnnotazioneManualeModel lAnnModel = new AnnotazioneManualeModel();

		// Controllo Validazione Fascicolo
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")
				&& !this.getRequestStringParameter("codice").equals("0290") // Indulto anche se
																			// Definito/Archiviato
		) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// ============================================
		// Posizione Giuridica
		// ============================================
		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosMod = lCtrlPos
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);
		this.setRequestAttribute("posizioneGiuridica", lPosMod);

		// ******************************* CHIAMATA JSP **********************************/
		String codice = this.getRequestStringParameter("codice");
		String testo = this.getRequestStringParameter("testo");
		// *************************************************************************/

		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		BigDecimal lIdAnnotazioneManuale = null;
		Vector lAnnMod = new Vector();

		// ==========================================================================
		// Se manca l'id annotazione vuol dire che sono giunto sulla form delle
		// stampe selezionando una richiesta già validata
		// Funzione ettiva solo per amnistia e indulto (0122)
		// ==========================================================================
		if (isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE)
				|| (getRequestStringParameter(
						ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE) != null
						&& getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE)
								.equals(""))) {
			// ******************************* Ricerca evento legato all'annotazione manuale
			// **********************************/
			// ========================================================================
			// Ricerco l'ultimo (data ins) provvedimento di richiesta di amnistia/indulto
			// validato o meno. n.b. viene ricercato l'evento fittizio a cui sono
			// collegate le annotazioni
			// ========================================================================
			EventoModel lEveModRic = new EventoModel();
			lEveModRic.setCodTipoEvento("01");
			lEveModRic.setFasSieIdFascicoloSiep(lIdFascicolo);

			try {
				String[] lMotivi = { "0122" }; // 0122 - Richiesta applicazione amnistia / indulto
				String[] lProvv = { "04", "26" }; // 04 - Provvedimento 26 - richiesta
				lEveMod = lCtrlEve.ExRicercaEventoPerMotivoPerProvv(lMotivi, lProvv, lEveModRic);
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("eccezione" + e);
			}

			if (lEveMod == null) {
				throw new SIEPException(SIEPException.USER_MESSAGE, "Non esiste la " + testo);
			}

			if (lEveMod != null) {
				this.setRequestAttribute("evento", lEveMod);

				// Recupera le annotazioni manuali collegate alla richiesta
				IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
				lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveMod.getIdEvento());

				this.setRequestAttribute("annotazioneManuale", lAnnMod);
			}
		} else {
			lIdAnnotazioneManuale = getRequestBigDecimalParameter(
					ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);

			IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			AnnotazioneManualeModel aAnnManMod = lCtrlAnn
					.ExRicercaAnnotazioneManualeByKey(lIdAnnotazioneManuale);

			if (aAnnManMod != null) {
				lAnnMod.add(aAnnManMod);
			}

			this.setRequestAttribute("annotazioneManuale", lAnnMod);
		}

		// ****************************ricerca reati*****************************************/
		for (int i = 0; i < lAnnMod.size(); i++) {
			lAnnModel = (AnnotazioneManualeModel) lAnnMod.get(i);
			if (lAnnModel.getReaIdReato() != null) {
				IReato lCtrlReato = SIEPLookupRemote.getReatoRemote();
				ReatoModel lReatoModel = lCtrlReato.ExRicercaReatoByKey(lAnnModel.getReaIdReato());
				reati.add(lReatoModel);
				this.setRequestAttribute("reati", reati);
			}
		}
		// ***********************************************************************/

		// ******************************Ricerca ordine esecuzione per codice 9
		// **********************************************************/
		EventoModel lEveOESet = new EventoModel();
		lEveOESet.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveOESet.setCodTipoEvento("01");
		lEveOESet.setCodTipoProvvedimento("06");
		Vector eventi = new Vector();
		try {
			eventi = lCtrlEve.ExRicercaEvento(lEveOESet);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Eccezione " + ex);
		}

		EventoModel lEveOE = new EventoModel();
		if (eventi.size() > 0) {
			lEveOE = (EventoModel) eventi.get(0);
			if (lEveOE != null) {
				this.setRequestAttribute("lEveOE", lEveOE);
			}
		}
		// ****************************************************************************************/

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null) {
			setRequestAttribute("magistratocompetente", lMagMod);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("magistratocompetente: " + lMagMod);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(),
				lSentMod.getCodTipoAutoritaEmittente());
		setRequestAttribute("tipoUfficio", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsterna", "" + lOption);

		setRequestAttribute("testo", testo);

		this.setRequestAttribute("codice", codice);
		this.setRequestAttribute("sentenza", lSentMod);

		return PG_LOAD_RICHIESTA_CON_CODICE;
	}
}