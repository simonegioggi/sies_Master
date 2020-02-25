package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per il caricamento delle Griglia della Gestione Cumulo,
 *
 * @author
 */
public class ActLoadGrigliaCumulo extends ActionSiap implements ICostantiIstruttoriaCumulo {

	/**
	 * Può essere invocata direttamente dal menù verticale o dalle funzioni di dettaglio, in questo caso viene
	 * passato l'id dell'istruttoria su cui si sta lavorando
	 */
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// SOLO Se si proviene dall'elenco Istruttorie -
		// Gestione dell'Eventuale Lettura/Sostituzione del Fascicolo SIEP in sessione .
		if (!this.isRequestParameterNullObj("ParentFormName") && this
				.getRequestStringParameter("ParentFormName").trim().equals("ElencoEstesoIstruttorieCumulo")) {
			siesLogger.debug("Provengo dalla ricerca istruttorie");
			BigDecimal lIdFasSIEPCollegato = this
					.getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

			// per gestire il 'tornaIndietro' all'Elenco Istruttorie Estese
			setRequestAttribute("ParentFormName", getRequestStringParameter("ParentFormName").trim());

			FascicoloSiepModel lFascModSess = null;
			if (!this.isSessionAttributeNullObj("fascicolo"))
				lFascModSess = ((FascicoloSiepModel) (getSessionAttribute("fascicolo")));

			// Se non ho il fascicolo in sessiono oppure quello in sessione è diverso
			// da quello che devo visualizzare, rimuovo i dati el fascicolo di sessione
			// e carico i dati del nuovo fascicolo
			if (lFascModSess == null || lFascModSess.getIdFascicoloSiep() == null
					|| lFascModSess.getIdFascicoloSiep().compareTo(lIdFasSIEPCollegato) != 0) {
				// ripulisco la sessione (vedi ActCleanWorkSessionSIEP)
				setSessionAttribute("fascicolo", null);
				setSessionAttribute("soggetto", null);
				setSessionAttribute("sentenza", null);

				setSessionAttribute("cumulowiz", null);
				setSessionAttribute("penaresidua", null);
				setSessionAttribute("reato", null);

				// Carico il nuovo fascicolo in sessione
				IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
				FascicoloSiepModel lFascMod = lCtrlFasc.ExRicercaFascicoloByKey(lIdFasSIEPCollegato);

				setSessionAttribute("fascicolo", lFascMod);
				setSessionAttribute("soggetto", lFascMod.getSoggetto());
				setSessionAttribute("sentenza", lFascMod.getSentenza());
			} else {
				// Il fascicolo selezionato nella ricerca coincide con quello già in sessione
			}
		}

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = ((FascicoloSiepModel) (getSessionAttribute("fascicolo")));

		BigDecimal lIdIstruttoriaCorrente = null;

		// EventoModel lEventoIstruttoria = new EventoModel();
		IstruttoriaCumuloModel lIstruttoriaModel = new IstruttoriaCumuloModel();

		// Verifico se sto già trattando una Istruttoria (aperta o meno)
		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)) {
			// Istruttoria già in consultazione, può essere una istruttoria storica
			// (già chiusa) , o l'istruttoria corrente aperta
			lIdIstruttoriaCorrente = this
					.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

			siesLogger.debug("Load Istruttoria");

			IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			lIstruttoriaModel = lIstrCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoriaCorrente);

			setRequestAttribute("IstruttoriaCumulo", lIstruttoriaModel);
		} else {
			// ==========================================================================
			// Provengo direttamente dal Menù Verticale, verifico se presente una istruttoria
			// aperta e la carico passandola alla finestra
			// ==========================================================================
			if (getUfficioUtenteConnesso().getCodUfficio().equals(lFascMod.getChiaveUfficio())) {
				siesLogger.debug("Nessuna Istruttoria Corrente");

				lIstruttoriaModel.setFlagStato(FLAG_STATO_APERTA); // A = Aperte
				lIstruttoriaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

				Vector lListaIstruttorie = new Vector();

				IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
				lListaIstruttorie = lIstrCtrl.ExRicercaIstruttoriaCumulo(lIstruttoriaModel);

				if (lListaIstruttorie.size() > 0) {
					lIstruttoriaModel = (IstruttoriaCumuloModel) lListaIstruttorie.elementAt(0);
					setRequestAttribute("IstruttoriaCumulo", lIstruttoriaModel);
				}
			}

		}

		// ======================================================================================================
		// Ricerca del Totale MESSAGGI di tipo 'Atti Ricevuti per Competenza' (legati all'ISTRUTTORIA
		// Corrente).
		// Questo Totale viene evidenziato nella successiva griglia sul bottone <Atti Ricevuti per Competenza>
		// ======================================================================================================
		MessaggioModel lMessaggio = new MessaggioModel();
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();

		lMessaggio.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());
		lMessaggio.setCodTipoMessaggio(ICostantiJMS.RICHIESTA);
		lMessaggio.setFlagVisto("N"); // non ancora presi in carico
		lMessaggio.setCodUfficioMittente("");

		lMessaggio.setChiaveAnnoFasCumulante(lFascMod.getChiaveAnno());
		lMessaggio.setChiaveProgrFasCumulante(lFascMod.getChiaveProgr());

		Date lDataInizio = DateUtils.getEnneMonthBefore(DateUtils.getSysDate(), 2);
		Date lDataFine = DateUtils.getSysDate();

		Vector<String> lListaTipoOperazione = new Vector<>();
		lListaTipoOperazione.add(ICostantiJMS.TRASFERIMENTO_COMPETENZA);
		lListaTipoOperazione.add(ICostantiJMS.SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA);
		lListaTipoOperazione.add(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);

		BigDecimal lCountRisultati = null;
		lCountRisultati = lCrtl.ExCountMessaggiRicevutiPaged(lMessaggio, lListaTipoOperazione, lDataInizio,
				lDataFine, 0);
		setRequestAttribute("CountRisultati", "" + lCountRisultati);
		// siesLogger.debug("--XX-- Totale Messaggi Trovati = "+lCountRisultati);

		return PG_LOAD_GRIGLIA_CUMULO;
	}

}