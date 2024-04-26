package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
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
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per caricare la modifica dell'ordine di ingiunzione
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActLoadModificaOrdineIngiunzione extends ActionSiap implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

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

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Ricerca i pagamenti per id Fascicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
		// 2023.09.19 Si visualizzano solo quelle legate all'evento
		// listaRateizzazioni = lRateCTRL.exRicercaRateizzazioniByIdFasc(fsm.getIdFascicoloSiep());
		listaRateizzazioni = lRateCTRL.exRicercaRateizzazioniByIdEvento(idEvento);
		// 2023.09.19 - FINE

		if (listaRateizzazioni.size() == 0) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Non e' stato inserito un metodo di pagamento: unica rata o rateizzazione. "
							+ "Impossibile procedere! "
							+ "Si reindirizza alla pagina di Gestione Modalita' Pagamento.");
			rt.setAction("siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		}
		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Ricerco il civilmente Obbligato se esiste
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		setRequestAttribute("civilmenteObbligati", coms);

		// Magistrato
		MagistratoModel lMag = enm.getMagistrato();
		MagistratoCompetenteMagistratoModel lMagModel = new MagistratoCompetenteMagistratoModel();
		lMagModel.setMagistrato(lMag);
		setRequestAttribute("magistrato", lMagModel);

		// Avvocati
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Autorità esterna
		Option lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		// Verifico se sovrescrivere l'auturità esterna
		if (fsm.getFlagAltraCausa() != null && fsm.getFlagAltraCausa().equals("S")) {
			// modifica relativa al tipo istituto
			if (lPos.getAltraCausa() != null && (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("23")
					|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
					|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
					|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
					|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) {
				lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
			} else {
				if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIstitutoDetenzione() != null)
					lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
			}
		} else {
			if (lPos.getPosizioneGiuridica().isLibero()
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
				lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (lPos.getLuogoDetenzione() != null
						&& lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
					lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}
		lOptionAutoritaEsternaE.setSelected("-");
		setRequestAttribute("autoritaEsternaE", "" + lOptionAutoritaEsternaE);

		// Autorita Notifica Avvocato
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "C0");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// Autorita Notifica Civilmente Obbligati
		Option lOptCivilObb = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaCivilObb", "" + lOptCivilObb);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		setRequestAttribute("modalita", "M");

		// pagina di ritorno
		return PG_LOAD_INSERISCI_ORDINE_INGIUNZIONE;
	}

}