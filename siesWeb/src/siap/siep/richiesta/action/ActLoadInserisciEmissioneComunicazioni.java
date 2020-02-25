package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciEmissioneComunicazioni
 * </p>
 * <p>
 * Description: Azione per l'inserimento delle Comunicazioni di richiesta o concessione Amnistia/Indulto,
 * Depenalizzazione, Incostituzionalità
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ActLoadInserisciEmissioneComunicazioni extends ActionSiap implements ICostantiRichiesta {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di caricamento della form di produzione della comunicazione
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		// Indulto anche se archiviato
		if (isFascicoloArchiviatoDefinito() && !this.getRequestStringParameter("annotazioni").equals("0301")) {
			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidatoAnnotazioniManuali();

		// ==========================================================================
		// Recupero la Posizione Giuridica da passare alla form
		// ==========================================================================
		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosMod = lCtrlPos
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);
		this.setRequestAttribute("posizioneGiuridica", lPosMod);

		// ==========================================================================
		// Recupero i parametri passati sulla chiamata:
		// puntoPartenza: - AN se decisione del GE
		// - GE se richieste al GE
		// annotazioni: è il codice motivo da inserire nell'eventro legato alla
		// comunicazione
		// ==========================================================================
		String codice = this.getRequestStringParameter("annotazioni");
		String partenza = this.getRequestStringParameter("puntoPartenza");

		// ==========================================================================
		// Ricerca evento legato all'annotazione manuale.
		// ==========================================================================
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		BigDecimal lIdAnnotazioneManuale = null;
		Vector lAnnMod = new Vector();

		if (isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE) ||
				(getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE) != null &&
				getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE).equals(""))) {
			EventoModel lEveModRic = new EventoModel();
			lEveModRic.setFasSieIdFascicoloSiep(lIdFascicolo);
			lEveModRic.setCodTipoEvento("01");
			String[] lMotivi = { "0210", "0211", "0122", "0210", "0211", "0122" };
			String[] lTipoProv = { "04", "04", "04", "26", "26", "26" };
			String[] lProvv = { "04", "26" }; // STUB 12-10-2005 REWORK STATO ESECUZIONE.

			EventoModel lEveMod = null;
			if (partenza.equals("AN")) { // ========================================================================
											// Decisioni del GE: ricerco il provvedimento di concessione al
											// quale
											// sono legate le annotazioni
											// ========================================================================
											// STUB 12-10-2005 REWORK STATO ESECUZIONE.
											// lEveMod =
											// lCtrlEve.ExRicercaEventoPerMotivo(motiviGE,lEveModRic);
											// 0284 - Applicazione Amnistia / Indulto
											// 0285 - Applicazione depenalizzazione
											// 0286 - Applicazione incostituzionalita'
				String[] motiviGE = { "0284", "0285", "0286" };
				lEveMod = lCtrlEve.ExRicercaEventoPerMotivoPerProvv(motiviGE, lProvv, lEveModRic);
			} else {
				// STUB 12-10-2005 REWORK STATO ESECUZIONE.
				if (partenza.equals("GE")) { // richieste al GE: recupero la richiesta (quella fittizia)
					lEveMod = lCtrlEve.ExRicercaEventoPerMotivoPerProvv(lMotivi, lProvv, lEveModRic);
				} else { // ??????????????
					lEveMod = lCtrlEve.ExRicercaEventoUnicoTipoProvTipoMot(lEveModRic, lTipoProv, lMotivi);
				}
			}

			// ==========================================================================
			// Recupero le annotazioni legate all'evento (Richiesta o Provvedimento di
			// concessione)
			// ==========================================================================

			// Vector lAnnMod = new Vector();
			if (lEveMod != null) {
				this.setRequestAttribute("evento", lEveMod);
				IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
				lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveMod.getIdEvento());
				this.setRequestAttribute("annotazioneManuale", lAnnMod);
			}
		} else {
			lIdAnnotazioneManuale = getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);

			IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			AnnotazioneManualeModel aAnnManMod = lCtrlAnn
					.ExRicercaAnnotazioneManualeByKey(lIdAnnotazioneManuale);

			// MEV37 - Richieste al GE. Se si sta tentando di effettuare la Comunicazione
			// Depenalizzazione o Amnistia/Indulto prima di aver emesso la richiesta
			// il sistema va in errore in fase di validazione.
			// Verifico che l'AM non sia legata all'evento fittizio (01-26-0210/0211/0122) , ,
			// La validazione della richiesta infatti sposta le AM sulla richiesta vera e propria e cancella
			// quella fittizia.
			//
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("MEV37 - Verifico l'evento puntato delle AM");
			EventoModel lEventoAppoggio = lCtrlEve.ExRicercaEventoByKey(aAnnManMod.getEveIdEvento());
			if ("26".equals(lEventoAppoggio.getCodTipoProvvedimento())
					&& ("0122".equals(lEventoAppoggio.getCodMotivo()) // 0122 = Indulto
							|| "0210".equals(lEventoAppoggio.getCodMotivo()) // 0210 = Depen
					|| "0211".equals(lEventoAppoggio.getCodMotivo()) // 0211 = Incost
					)) {
				// throw new F3BException(F3BException);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Prima di emettere la Comunicazione è necessario effettuare la richiesta di applicazione del beneficio.");
				return IWebConstants.PG_MESSAGE;
			}
			// Fine MEV 37

			if (aAnnManMod != null) {
				lAnnMod.add(aAnnManMod);
			}

			this.setRequestAttribute("annotazioneManuale", lAnnMod);
		}

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// MEV 37 - Inizio
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTrattino());
		// lOption.setFilter(new String[] {"UDS", "TDS", "-"});
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOption.setFilter(new String[] { "UDS", "TDS", "UDSM", "TDSM", "-" });
		// MEV 37 - Fine
		setRequestAttribute("tipoUfficio", "" + lOption);

		String lPage = "";

		// ==========================================================================
		// Restituisco la pagina di visualizzazione in funzione del tipo di beneficio
		// 0301 = Decisioni del GE - Indulto/Amnistia
		// 0298 = Richieste al GE - Indulto/Amnistia
		// 0300 = Decisioni del GE - Depenalizzazione/Incostituzionalità
		// 0299 = Richieste al GE - Depenalizzazione/Incostituzionalità
		// ==========================================================================
		if (codice.equals("0301") || codice.equals("0298")) { // Benefici amnistia/indulto
			this.setRequestAttribute("codice", codice);
			this.setRequestAttribute("partenza", partenza);
			lPage = PG_LOAD_EMISSIONE_CONCESSIONI;
		} else { // Depenalizzazione/Incostituzionalità (revoca sentenza per abolizione reati)
			this.setRequestAttribute("codice", codice);
			this.setRequestAttribute("partenza", partenza);
			lPage = PG_LOAD_EMISSIONE_REVOCHE;
		}

		// GDV 27-11-06 Riempimento ComboBoX
		Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsterna", "" + lOptionAutorita);

		return lPage;
	}

}