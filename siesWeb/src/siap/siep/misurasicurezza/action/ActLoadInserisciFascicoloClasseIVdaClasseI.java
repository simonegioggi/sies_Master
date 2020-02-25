package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Action di caricamento della jsp di inserimento dei dati di iscrizione del fascicolo di classe IV da un
 * fascicolo di classe I. La action funziona sia nel caso in cui si effettua l'iscrizione a partire dal
 * fascicolo corrente caricato in Sessione, sia nel caso dell'iscrizione dalla presa in carico. In questo
 * ultimo caso alla Action arriva solo l'id del Messaggio (messaggio Model) forse
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciFascicoloClasseIVdaClasseI extends ActionSiap implements
		ICostantiMisuraSicurezza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = null;
		BigDecimal lIdMessaggio = null;
		boolean daPresaInCarico = true;

		SentenzaModel lSentenzaModel = null;
		SoggettoModel lSoggettoModel = null;
		FascicoloSiepModel lFascSiepOrigModel = null;

		if (!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			// Provengo dalla presa in carico
			lIdMessaggio = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
			daPresaInCarico = true;
		} else {
			daPresaInCarico = false;
			// Sto iscrivendo dal fascicolo in sessione (di competenza)
			if (this.isSessionAttributeNullObj("fascicolo"))
				return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		/*
		 * 05/11/2014 Eliminato il controllo che l'ufficio corrente debba essere sede di un MDS try {
		 * //======================================================================== // Per poter iscrivere
		 * fascicoli in classe IV, l'ufficio corrente deve // essere sede di un MDS
		 * //======================================================================== UfficioModel lUfficio =
		 * getUfficioUtenteConnesso();
		 * 
		 * IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote(); UfficioModel lMDS =
		 * lCtrlUff.getUfficioUDSTDS(lUfficio.getCodDistretto(), "UDS", lUfficio.getCodComune());
		 * 
		 * if (lMDS==null || lMDS.getCodUfficio()==null) throw new SIEPException(SICOException.USER_MESSAGE,
		 * "I Procedimenti di classe IV possono essere iscritti solo da Uffici di Procura presso il Magistrato di Sorveglianza."
		 * );
		 * 
		 * } catch(SICOException e){ if ("Ufficio inesistente".equals(e.getMessage())){ throw new
		 * SIEPException(SICOException.USER_MESSAGE,
		 * "I Procedimenti di classe IV possono essere iscritti solo da Uffici di Procura presso il Magistrato di Sorveglianza."
		 * ); } }
		 */

		// ==========================================================================
		// 2 casi possibili
		// 1) Voglio iscrivere un procedimento di classe IV a partire dal procedimento
		// attualmente in sessione
		// 2) Voglio iscrivere un procedimento di classe IV a partire da un procedimento
		// recuperato dalla presa in carico. Tale fascicolo non è in sessione, sebbene
		// a sistema. In sessione potrei avere in altro fascicolo.
		// ==========================================================================

		if (daPresaInCarico) {
			// Se provengo dalla presa in carico pulisco gli attuali dati di sessione
			setSessionAttribute("fascicolo", null);
			setSessionAttribute("soggetto", null);
			setSessionAttribute("sentenza", null);

			setSessionAttribute("cumulowiz", null);
			setSessionAttribute("penaresidua", null);
			setSessionAttribute("reato", null);

			setRequestAttribute("IdMessaggio", "" + lIdMessaggio);

			// setRequestAttribute("NumerazioneManuale", "N");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Da Presa in Carico - NumerazioneManuale is null = "
					+ isRequestParameterNullObj("NumerazioneManuale"));
			if (!isRequestParameterNullObj("NumerazioneManuale")
					&& "S".equals(getRequestStringParameter("NumerazioneManuale"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" Da Presa in Carico -NumerazioneManuale = "
						+ getRequestStringParameter("NumerazioneManuale"));
				setRequestAttribute("NumerazioneManuale", "S");
			}

			// Recupero il messaggio
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			MessaggioModel lMessModel = lCrtl.ExRicercaMessaggioByKey(lIdMessaggio);

			// Recupero il fascicolo by CHIAVE_ANNO, CHIAVE_PROGR e CHIAVE_UFFICIO
			// n.b. il fascicolo è a sistema, è stato preso in carico
			IFascicoloSiep lCtrlFascSiep = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFascSiepModelRicerca = new FascicoloSiepModel();
			lFascSiepModelRicerca.setChiaveAnno(lMessModel.getChiaveAnnoSiep());
			lFascSiepModelRicerca.setChiaveProgr(lMessModel.getChiaveProgrSiep());
			lFascSiepModelRicerca.setChiaveUfficio(lMessModel.getChiaveUfficioSiep());

			lFascSiepOrigModel = lCtrlFascSiep
					.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFascSiepModelRicerca);

			if (lFascSiepOrigModel != null) {
				ISoggetto lCtrlSoggetto = SICOLookupRemote.getSoggettoRemote();
				lSoggettoModel = lCtrlSoggetto.ExRicercaSoggettoByKey(lFascSiepOrigModel.getSogIdSoggetto());

				ISentenza lCtrlSentenza = SIEPLookupRemote.getSentenzaRemote();
				lSentenzaModel = lCtrlSentenza.ExRicercaSentenzaByKey(lFascSiepOrigModel.getSenIdSentenza());
			} else {
				// TODO devo rilanciare un messaggio di errore. Il fascicolo risulta
				// preso in carico ma manca a sistema
			}

		} else {
			lFascSiepOrigModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			// lSoggettoModel = (SoggettoModel) getSessionAttribute("soggetto");
			// lSentenzaModel = (SentenzaModel) getSessionAttribute("sentenza");
			if (lFascSiepOrigModel != null) {
				ISoggetto lCtrlSoggetto = SICOLookupRemote.getSoggettoRemote();
				lSoggettoModel = lCtrlSoggetto.ExRicercaSoggettoByKey(lFascSiepOrigModel.getSogIdSoggetto());

				ISentenza lCtrlSentenza = SIEPLookupRemote.getSentenzaRemote();
				lSentenzaModel = lCtrlSentenza.ExRicercaSentenzaByKey(lFascSiepOrigModel.getSenIdSentenza());
			} else {
				// TODO devo rilanciare un messaggio di errore. Il fascicolo risulta
				// preso in carico ma manca a sistema
			}

			//
			// 02-07-2015 - Punto 1) - subito dopo il Rilascio di MEV2 STEP1 (Misure Sicurezza) - Interventi
			// Urgenti per Gestione Misure di Sicurezza:
			// Su Segnalazione di M.T. Viene tolto questo Controllo Bloccante per Fascicoloi di altre BDI
			/*
			 * UfficioModel lUfficioUtente = getUfficioUtenteConnesso();
			 * 
			 * if (!lUfficioUtente.isUfficioDiCompetenza(lFascSiepOrigModel.getChiaveUfficio())){ throw new
			 * SIEPException(SICOException.USER_MESSAGE,
			 * "Il Procedimento "+lFascSiepOrigModel.getChiaveAnno()+
			 * "/"+lFascSiepOrigModel.getChiaveProgr()+" non è di propria competenza."); }
			 */
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("NumerazioneManuale is null = "
					+ isRequestParameterNullObj("NumerazioneManuale"));
			if (!isRequestParameterNullObj("NumerazioneManuale")
					&& "S".equals(getRequestStringParameter("NumerazioneManuale"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("NumerazioneManuale = " + getRequestStringParameter("NumerazioneManuale"));
				setRequestAttribute("NumerazioneManuale", "S");
			}
		}

		{
			// ========================================================================
			// New d.f. 14/04/2015
			// Devo controllare se per l'anno corrente sono stati iscritti procedimenti
			// di classe IV. In caso negativo devo obbligare l'utente a indicare
			// manualmente il numero di procedimento che rappresenterà l'inizio della
			// numerazione automatica per i fascicoli telematici dell'anno corrente.
			// Infatti sono stati già iscritti sicuramente fascicoli cartacei che
			// andranno eventualmente caricati manualmente. La numerazione automatica
			// vale solo per i nuovi e non puù sovrapporsi a quella cartecea già assegnata
			// dall'ufficio
			//
			// Verificare se subordinare il controllo al 2015
			// ========================================================================
			BigDecimal lAnnoCorrente = new BigDecimal(DateUtils.getSysDate("yyyy"));

			if (lAnnoCorrente.intValue() == 2015) {
				// n.b. controllo solo per il 2015, anno di avvio delle Misure Sicurezza
				IMisuraSicurezza lCtrlMS = SIEPLookupRemote.getMisuraSicurezzaRemote();
				boolean lEsisteFascicoloClasseIVAnnoCorrente = lCtrlMS.ExEsistonoFascicoliClasseIVAnno(
						getUfficioUtenteConnesso(), lAnnoCorrente);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lEsisteFascicoloClasseIVAnnoCorrente = "
						+ lEsisteFascicoloClasseIVAnnoCorrente);
				// Se non esiste devo forzare la numerazione manuale
				if (!lEsisteFascicoloClasseIVAnnoCorrente) {
					setRequestAttribute("NumerazioneManuale", "S");
					setRequestAttribute("EsisteFascicoloClasseIVAnnoCorrente", "N");
					setRequestAttribute("AnnoCorrente", DateUtils.getSysDate("yyyy"));

				}
			}
		}

		// ==========================================================================
		//
		// ==========================================================================
		{
			// Verifico che per il fascicolo non sia già stato iscritto un fascicolo
			// di classe IV. E' un controllo di sicurezza. Può accadere che venga
			// effettuata la presa in carico, ma non aggiornato l'esito sulla tabella
			// MESSAGGIO (i due processi non sono in transazione). Potrebbe accadere
			// inoltre che lo stesso fascicolo venga trasmesso più volte.
			// Si visualizza solo un warning all'utente.
			FascMsToFascSiepModel lFascMsToFascSiepModel = new FascMsToFascSiepModel();

			lFascMsToFascSiepModel.setFasSieIdFascicoloSiep(lFascSiepOrigModel.getIdFascicoloSiep());
			lFascMsToFascSiepModel
					.setCodTipoRelazioneMS(ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_ISCRITTO_AL);

			// Solo se già collegato a un proprio fascicolo
			lFascMsToFascSiepModel.setChiaveUfficioSiepCollegato(getCodUfficioUtenteConnesso());

			IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
			Vector<FascMsToFascSiepModel> lLista = lCtrl
					.ExRicercaFascMsToFascSiepByFascSiep(lFascMsToFascSiepModel);

			if (lLista != null && lLista.size() > 0) {
				// Presente almeno un Fascicolo di classe IV Iscritto
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti fascicoli di classe IV iscritti.");
				setRequestAttribute("fascicoliClasseIVIscritti", lLista);
			}
		}

		{
			// Aggiunto il 05/03/2015 per controllo esistenza e correttezza delle MS
			// Passo alla jsp la lista delle MS presenti sul fascicolo di origine
			// per effettuare i controlli

			// IMisuraSicurezza lCtrlMs = SIEPLookupRemote.getMisuraSicurezzaRemote();
			// List lListMisure =
			// lCtrlMs.ExRicercaMisuraSicurezzaByIdFascicolo(lFascSiepOrigModel.getIdFascicoloSiep());
			// setRequestAttribute("listaMisure", lListMisure );

			Boolean warningMisuraCumulo = false;

			if ("S".equalsIgnoreCase(lFascSiepOrigModel.getFlagCumulante())) {
				ICumulo lCtrlCum = SIEPLookupRemote.getCumuloRemote();
				Vector lCumuli = lCtrlCum
						.ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(lFascSiepOrigModel
								.getIdFascicoloSiep());

				PenaCumuloModel lPenCumMod = new PenaCumuloModel();

				if (lCumuli.size() > 0) {
					CumuloModel lCumMod = ((CumuloModel) (lCumuli).get(0));

					if (lCumMod != null && lCumMod.getIdCumulo() != null) {
						IPenaCumulo lCtrlPen = SIEPLookupRemote.getPenaCumuloRemote();
						lPenCumMod = lCtrlPen.ExRicercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());

						// 02-07-2015 - Punto 2) -
						if (lPenCumMod != null && lPenCumMod.getIdPenaCumulo() != null) {
							if (lPenCumMod.getMisuraSicurezza() != null
									&& !lPenCumMod.getMisuraSicurezza().equals("")) {
								warningMisuraCumulo = true;
							}
						}
					}
				}

				this.setRequestAttribute("penacumulo", lPenCumMod);
			}
			// else {
			// IMisuraSicurezza lCtrlMs = SIEPLookupRemote.getMisuraSicurezzaRemote();
			// List lListMisure =
			// lCtrlMs.ExRicercaMisuraSicurezzaByIdFascicolo(lFascSiepOrigModel.getIdFascicoloSiep());
			// setRequestAttribute("listaMisure", lListMisure );
			// }

			if (!warningMisuraCumulo) {
				IMisuraSicurezza lCtrlMs = SIEPLookupRemote.getMisuraSicurezzaRemote();
				// 10-11-2015 - NON Blocchiamo la creazione di un Classe IV che viene da un ClasseI PRIVO DI
				// MISURE (Richiesta M.T.)
				// Sostituisco la Ricerca con una Che NON Rilancia L'eccezione

				// List lListMisure =
				// lCtrlMs.ExRicercaMisuraSicurezzaByIdFascicolo(lFascSiepOrigModel.getIdFascicoloSiep());
				List lListMisure = lCtrlMs
						.ExRicercaMisuraSicurezzaByIdFascicoloNONRilancia(lFascSiepOrigModel
								.getIdFascicoloSiep());
				if (lListMisure.size() == 0 && isRequestParameterNullObj(CAMPO_CK_WARNING_MIS)) {
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Attenzione: Procedimento privo di Misure di Sicurezza:\n "
									+ " Se si procede con l'iscrizione, bisognerà inserirle MANUALMENTE."
									+ " Si vuole continuare con l'Iscrizione del procedimento? ");

					return PG_WARNING_MIS_SIC_FUORI_SENT;
				}

				setRequestAttribute("listaMisure", lListMisure);
			}

			if (warningMisuraCumulo && isRequestParameterNullObj(CAMPO_CK_WARNING_MIS)) {
				setRequestAttribute(
						IWebConstants.MESSAGE_TEXT,
						"Attenzione: Procedimento di cumulo:\n"
								+ " Sono presenti Misure di Sicurezza, iscritte a testo libero,\n"
								+ " che non è possibile riportare in automatico sul procedimento di classe IV che si sta iscrivendo.\n"
								+ " Se si procede con l'iscrizione, bisognerà inserirle MANUALMENTE."
								+ " Si vuole continuare con l'Iscrizione del procedimento? ");

				return PG_WARNING_MIS_SIC_FUORI_SENT;
			}

		}

		// ===================================================
		// Recupero soggetto e sentenza da passare alla jsp
		// n.b. per generalità la jsp non lavora sui dati in sessione ma su quelli
		// della request
		// ===================================================
		setRequestAttribute("soggetto", lSoggettoModel);
		setRequestAttribute("sentenza", lSentenzaModel);
		setRequestAttribute("fascicolo", lFascSiepOrigModel);

		setRequestAttribute(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP, lIdFascicoloSiep);

		return PG_LOAD_INS_FASCICOLO_CLASSE_IV;
	}

}