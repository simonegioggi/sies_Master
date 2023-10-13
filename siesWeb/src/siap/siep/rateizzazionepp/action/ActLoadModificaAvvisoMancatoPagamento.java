package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per caricare la modifica dell'Avviso Mancato Pagamento
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadModificaAvvisoMancatoPagamento extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();
		siesLogger.debug("ID_FASCICOLO = " + idFascicolo);

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		siesLogger.debug("ID_EVENTO = " + idEvento);

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lm = lockIfNotLocked("evento", "" + idEvento, getCodUtenteConnesso());
		if (lm != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La " + lm.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		// Ricerca i pagamenti per id Fascicolo
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<RateizzazionePPModel> listaRateizzazioni = irpp.exRicercaRateizzazioniByIdEvento(idEvento);

		// Sezione con l'importo da pagare a la rateizzazione
		Iterator<RateizzazionePPModel> iterLR = listaRateizzazioni.iterator();
		BigDecimal importoDaPagare = new BigDecimal(0);
		while (iterLR.hasNext()) {
			RateizzazionePPModel rata = iterLR.next();
			if (!rata.isStoricizzato()) {
				importoDaPagare = rata.getImportoDaPagare();
				break;
			} else {
				importoDaPagare = importoDaPagare.add(rata.getImportoDaPagare());
			}
		}
		setRequestAttribute("importoDaPagare", "" + importoDaPagare);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());
		// imposto valore nella request
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Ricerco il civilmente Obbligato se esiste
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		setRequestAttribute("civilmenteObbligati", coms);

		// Magistrato
		MagistratoModel mm = enm.getMagistrato();
		MagistratoCompetenteMagistratoModel mcmm = new MagistratoCompetenteMagistratoModel();
		mcmm.setMagistrato(mm);
		setRequestAttribute("magistrato", mcmm);

		// Avvocati
		IAvvocato ia = SIEPLookupRemote.getAvvocatoRemote();
		Vector avvocati = ia.ExRicercaAvvocatiByFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("avvocati", avvocati);

		// Autorità esterna
		Option tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
		// Verifico se sovrescrivere l'auturità esterna
		if (fsm.getFlagAltraCausa() != null && fsm.getFlagAltraCausa().equals("S")) {
			// modifica relativa al tipo istituto
			if (pgldacm.getAltraCausa() != null
					&& (pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("23")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("78")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("79")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("80")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) {
				tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
			} else {
				if (pgldacm.getAltraCausa() != null
						&& pgldacm.getAltraCausa().getIstitutoDetenzione() != null)
					tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							pgldacm.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
			}
		} else {
			if (pgldacm.getPosizioneGiuridica().isLibero()
					|| pgldacm.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| pgldacm.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
				tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (pgldacm.getLuogoDetenzione() != null
						&& pgldacm.getLuogoDetenzione().getIstitutoDetenzione() != null)
					tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							pgldacm.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}
		tipoAutorita.setSelected("-");
		setRequestAttribute("autoritaEsternaE", "" + tipoAutorita);

		// Autorita Notifica Avvocato
		Option autoritaEsternaN = new Option(DecodificheManager.getInstance().getTipoAutorita(), "C0");
		setRequestAttribute("autoritaEsternaN", "" + autoritaEsternaN);

		// Autorita Notifica Civilmente Obbligati
		Option autoritaEsternaCivilObb = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaCivilObb", "" + autoritaEsternaCivilObb);

		setRequestAttribute("modalita", "M");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_INSERISCI_AVVISO_MANCATO_PAGAMENTO;
	}

}