package siap.siep.presaincarico.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per il caricamento della funzione di inserimento Annotazione esito Trasmissione Atti x Competenza
 * cumulo. La action viene chiamata sia dal dettaglio esito trasmissione sia dal dettaglio dell'evento di
 * trasmissione. Nel primo caso si ha in input l'id del Messaggio di esito (vedi Istruttorie/Richieste -
 * Riscontro trasmissioni - Dettaglio). Nel secindo caso l'id dell'evento
 *
 * @author d.fiorletta
 *
 */
public class ActLoadInsAnnotaEsitoTrasmComp extends ActionSiap implements ICostantiPresaincarico {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		MessaggioModel lMessaggioEsito = null;
		MessaggioModel lMessaggioTrasmissione = null;
		CompetenzaModel lCompetenzaModel = null;

		if (!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)
				&& !"".equals(getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO))) {
			// L'annotazione esito viene sempre fatta sulla risposta (PRESO IN CARICO, RESTITUITO)
			BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			lMessaggioEsito = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

			// Recupero i dati del fascicolo cumulato per caricarlo in sessione: anno/prog/uff
			// I dati sono presenti nel messaggio di trasmissione e nel messaggio di 
			// comunicazione
			FascicoloSiepModel lFasMod = new FascicoloSiepModel();

			if ("00079".equals(lMessaggioEsito.getCodTipoOperazione())) {

				// Sto lavorando su messaggio di "Comunicazione alle Procure" (Tipo_Operazione = 00079)
				lFasMod.setChiaveProgr(lMessaggioEsito.getChiaveProgrSiep());
				lFasMod.setChiaveAnno(lMessaggioEsito.getChiaveAnnoSiep());
			} else {
				// Sto lavorando su messaggio di "Esito Trasmissione" e vado a prendere i dati dal messaggio
				// di Trasmissione
				lMessaggioTrasmissione = lCrtl.ExRicercaMessaggioByKey(
						new BigDecimal(lMessaggioEsito.getJmsCorrelationIdMessage()));

				lFasMod.setChiaveProgr(lMessaggioTrasmissione.getChiaveProgrSiep());
				lFasMod.setChiaveAnno(lMessaggioTrasmissione.getChiaveAnnoSiep());
			}

			//
			lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());

			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

			// Pulisco la sessione
			setSessionAttribute("fascicolo", null);
			setSessionAttribute("soggetto", null);
			setSessionAttribute("sentenza", null);

			setSessionAttribute("cumulowiz", null);
			setSessionAttribute("penaresidua", null);
			setSessionAttribute("reato", null);

			// Metto in sessione il fascicolo
			setSessionAttribute("fascicolo", lFasRet);
			setSessionAttribute("soggetto", lFasRet.getSoggetto());
			setSessionAttribute("sentenza", lFasRet.getSentenza());
		} else if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

			// recupero l'evento
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveMod = new EventoNotificaModel();

			lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
			this.setRequestAttribute("eventonotifica", lEveMod);

			ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();
			lCompetenzaModel = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);
			setRequestAttribute("competenza", lCompetenzaModel);
		} else {
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile Procedere");
		}

		super.setLinkRitorno(); // TEST

		// ==========================================================================
		// Verifico se fascicolo di competenza
		// ==========================================================================
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.isFascicoloSiepDiCompetenza();

		// ==========================================================================
		// Controllo Validazione Fascicolo
		// ==========================================================================
		if (this.isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		// ==========================================================================
		// Verifico se esistono eventi non validati
		// ==========================================================================
		this.isEventoNonValidato();

		setRequestAttribute("messaggioEsito", lMessaggioEsito);
		setRequestAttribute("messaggioTrasmissione", lMessaggioTrasmissione);

		// ========================
		// Dati per le combo
		// ========================
		Option lOption = null;

		// Tipo UFFICIO Destinatario
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
		// 20170926: [SG] aggiunta procura generale
		String[] lFiltroUffici = { "-", "PGCAP", "PM", "PMM" }; // i destinatari sono solo PM e PMM + PGCAP
		lOption.setFilter(lFiltroUffici);
		if (lCompetenzaModel != null && lCompetenzaModel.getCodTipoAutoritaComp() != null)
			lOption.setSelected(lCompetenzaModel.getCodTipoAutoritaComp());
		else
			lOption.setSelected("-");

		setRequestAttribute("comboUfficiPM", "" + lOption);

		// ==========================================================================
		// Se sto annotando l'esito di un messaggio, precarico i dati dell'esito
		// dal messaggio e li posto alla jsp in AnnotazioneEsitoTrasmissioneModel
		// stessa struttura utlizzata nel caso di Modifica
		// ==========================================================================
		if (lMessaggioEsito != null) {
			UfficioModel ufficioEsito = getUfficioByCodUfficio(lMessaggioEsito.getCodUfficioMittente());
			setRequestAttribute("ufficioEsito", ufficioEsito);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lMessaggioEsito = " + lMessaggioEsito);

			AnnotazioneEsitoTrasmissioneModel lAnnotazModel = new AnnotazioneEsitoTrasmissioneModel();
			lAnnotazModel.setDataEsito(lMessaggioEsito.getDataInvio());
			lAnnotazModel.setCodEsito(lMessaggioEsito.getCodEsito());
			lAnnotazModel.setDescrEsito(lMessaggioEsito.getDescrEsito());

			lAnnotazModel.setChiaveAnno(lMessaggioEsito.getChiaveAnnoFasCumulante());
			lAnnotazModel.setChiaveProgr(lMessaggioEsito.getChiaveProgrFasCumulante());
			lAnnotazModel.setChiaveUfficio(lMessaggioEsito.getChiaveUfficioFasCumulante());

			lAnnotazModel.setNoteEsito(lMessaggioEsito.getNote());

			setRequestAttribute("annotazioneEsito", lAnnotazModel);

			siesLogger.debug("--XX--   annotazioneEsito = " + lAnnotazModel);
		}

		// ==========================================================================
		// RICERCA MESSAGGI di COMUNICAZIONE CUMULO DA ALTRE BDI per il Fascicolo
		// ==========================================================================
		//FIXME da verificare
		IMessaggio lCrtlMes = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggioComunicazione = new MessaggioModel();
		lMessaggioComunicazione.setCodTipoMessaggio("01");
		lMessaggioComunicazione.setCodTipoOperazione(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);
		lMessaggioComunicazione.setChiaveAnnoSiep(lFascMod.getChiaveAnno());
		lMessaggioComunicazione.setChiaveProgrSiep(lFascMod.getChiaveProgr());
		lMessaggioComunicazione.setChiaveUfficioSiep(lFascMod.getChiaveUfficio());
		Vector lMessaggi = lCrtlMes.ExRicercaMessaggio(lMessaggioComunicazione);
		if (lMessaggi.size() > 0) {
			lMessaggioComunicazione = (MessaggioModel) lMessaggi.get(0);
			setRequestAttribute("messaggioComunicazione", lMessaggioComunicazione);
		}

		// Tipologia Atto: EVENTO.TIPO_PROVVEDIMENTO
		// lOption = new Option(DecodificheManager.getInstance().getTipoRichiestaT());
		lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOption.setFilter("25");
		setRequestAttribute("comboTipoProvv", "" + lOption);

		// EVENTO.COD_MOTIVO
		Vector<DecodificheModel> lMotiviProvv = new Vector<>();
		lMotiviProvv.add(new DecodificheModel("5202",
				"Esito Trasmissione Atti per competenza (ex artt. 663 e 665 comma 4 c.p.p.)", "", "", "", "",
				"", "", ""));
		Option lOptionMotivi = new Option(lMotiviProvv);
		setRequestAttribute("comboMotivoProvv", "" + lOptionMotivi);

		// Combo esiti
		Vector<DecodificheModel> lEsiti = new Vector<>();
		lEsiti.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lEsiti.add(new DecodificheModel(ICostantiJMS.PRESAINCARICO, "Atti Presi in carico", "", "", "", "",
				"", "", ""));
		lEsiti.add(
				new DecodificheModel(ICostantiJMS.RESTITUITO, "Atti restituiti", "", "", "", "", "", "", ""));
		lEsiti.add(new DecodificheModel(ICostantiJMS.ASSORBITO_IN_CUMULO, "Assorbito in Cumulo", "", "", "",
				"", "", "", ""));

		Option lOptionEsiti = new Option(lEsiti);
		if (lMessaggioComunicazione.getIdMessaggio() != null)
			lOptionEsiti.setSelected(ICostantiJMS.ASSORBITO_IN_CUMULO);
		else
			lOptionEsiti.setSelected("-");

		setRequestAttribute("comboEsitiProvvedimento", "" + lOptionEsiti);

		// MAGISTRATO COMPETENTE
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistratocompetente", lMagi);

		setRequestAttribute("modalita", "I");

		return PG_ANNOTAZIONE_ESITO_TRASM_COMPETENZA;
	}

}