package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe action per il caricamento dell'inserimento della Trasmissione Atti Conversione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadInserisciTrasmissioneAttiConversione extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Controlli preliminari all'inserimento di un nuovo evento
		if (fsm.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr()
					+ " non è stato Validato. Impossibile inserire una Rideterminzaione Pena Pecuniaria!");
			rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (fsm.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
			rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		}

		// controllo se evento non sia validato
		isEventoNonValidato();

		// Ricerca i pagamenti per id Fascicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		// 2023.09.19 Si visualizzano solo quelle "Libere"
		// listaRateizzazioni = irpp.exRicercaRateizzazioniByIdFasc(fsm.getIdFascicoloSiep());
		listaRateizzazioni = irpp.exRicercaRateizzazioniLibereByIdFasc(fsm.getIdFascicoloSiep());
		// 2023.09.19 - FINE
		boolean isProvvedimentoEmissibile = false;
		for (RateizzazionePPModel rata : listaRateizzazioni) {
			if (Utils.isNullObj(rata.getEveIdEvento())) {
				isProvvedimentoEmissibile = true;
				break;
			}
		}
		setRequestAttribute("isProvvedimentoEmissibile", isProvvedimentoEmissibile);

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

		// controllo se esiste un OI validato
		IEvento ie = SICOLookupRemote.getEventoRemote();
		// EventoModel em = new EventoModel();
		// em.setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		// em.setCodMotivo("0622");
		// em.setCodTipoEvento("01");
		// em.setCodTipoProvvedimento("06");
		// try {
		// ie.ExRicercaEventoTipoEveTipoProvMot(em, "S");
		// } catch (Exception e) {
		// throw new F3BException(F3BException.USER_MESSAGE,
		// "Non e' stato inserito un ordine di ingiunzione validato." + " Impossibile procedere!");
		// }

		Hashtable<BigDecimal, EventoNotificaModel> listaRideterminazioniPena = new Hashtable<>();
		// MEV_2023-33: aggiunto controllo per storicizzazione evento OIP
		for (RateizzazionePPModel rata : listaRateizzazioni) {
			if (rata.getEveIdEvento() != null) {
				EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(rata.getEveIdEvento());
				if (listaRideterminazioniPena.get(rata.getEveIdEvento()) != null) {
					rata.setOrdineIngiunzione(listaRideterminazioniPena.get(rata.getEveIdEvento()));
				} else {
					rata.setOrdineIngiunzione(enm);
					listaRideterminazioniPena.put(enm.getEvento().getIdEvento(), enm);
				}
				rata.setStoricizzato("A".equals(enm.getEvento().getFlagDocumentoRegistrato()));
			}
		}

		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

		// Sezione con l'importo da pagare a la rateizzazione
		// controllo per storicizzazione evento RPP
		Iterator<RateizzazionePPModel> iterLR = listaRateizzazioni.iterator();
		String tipoRateizzazione = "";
		BigDecimal importoDaPagare = new BigDecimal(0);
		while (iterLR.hasNext()) {
			RateizzazionePPModel rata = iterLR.next();
			if (!rata.isStoricizzato()) {
				importoDaPagare = rata.getImportoDaPagare();
				tipoRateizzazione = rata.getTipoRateizzazione();
				break;
			} else {
				importoDaPagare = importoDaPagare.add(rata.getImportoDaPagare());

			}
		}
		setRequestAttribute("importoDaPagare", importoDaPagare);
		setRequestAttribute("tipoRateizzazione", tipoRateizzazione);

		// Ricerca lo stato dei pagamenti per id fascicolo
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		Iterator<BollettinoPagopaModel> iterBPM = elencoStatoPagamenti.iterator();
		BigDecimal importoPagato = new BigDecimal(0);
		while (iterBPM.hasNext()) {
			BollettinoPagopaModel bpm = iterBPM.next();
			if ("PA".equals(bpm.getStatoPagamento())) {
				importoPagato.add(bpm.getImportoPagato());
			}
		}
		boolean isImportoPagatoMinore = false;
		if (importoPagato.compareTo(importoDaPagare) <= 0)
			isImportoPagatoMinore = true;
		setRequestAttribute("isImportoPagatoMinore", isImportoPagatoMinore);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());
		if (pgldacm == null || pgldacm.getPosizioneGiuridica() == null) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			rt.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

			return IWebConstants.PG_MESSAGE;
		}
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Ricerco il civilmente Obbligato se esiste
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		setRequestAttribute("civilmenteObbligati", coms);

		// Magistrato
		IMagistratoCompetente imc = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel mcmm = imc
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(fsm.getIdFascicoloSiep());
		setRequestAttribute("magistrato", mcmm);

		// Avvocati
		try {
			IAvvocato ia = SIEPLookupRemote.getAvvocatoRemote();
			Vector avvocati = ia.ExRicercaAvvocatiByFascicolo(fsm.getIdFascicoloSiep());
			setRequestAttribute("avvocati", avvocati);
		} catch (SIEPException e) {
			// nessun avvocato trovato
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " non è stato associato alcun avvocato.");
			rt.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

			return IWebConstants.PG_MESSAGE;
		}

		// Autorità esterna
		Option tipoAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		// Verifico se sovrescrivere l'auturità esterna
		if (fsm.getFlagAltraCausa() != null && fsm.getFlagAltraCausa().equals("S")) {
			// modifica relativa al tipo istituto
			if (pgldacm.getAltraCausa() != null
					&& (pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("23")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("78")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("79")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("80")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) {
				tipoAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
			} else {
				if (pgldacm.getAltraCausa() != null
						&& pgldacm.getAltraCausa().getIstitutoDetenzione() != null)
					tipoAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							pgldacm.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
			}
		} else {
			if (pgldacm.getPosizioneGiuridica().isLibero()
					|| pgldacm.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| pgldacm.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
				tipoAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (pgldacm.getLuogoDetenzione() != null
						&& pgldacm.getLuogoDetenzione().getIstitutoDetenzione() != null)
					tipoAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							pgldacm.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}
		tipoAutoritaEsternaE.setSelected("-");
		setRequestAttribute("autoritaEsternaE", "" + tipoAutoritaEsternaE);

		// Autorita Notifica Avvocato
		Option tipoAutoritaEsternaN = new Option(DecodificheManager.getInstance().getTipoAutorita(), "C0");
		setRequestAttribute("autoritaEsternaN", "" + tipoAutoritaEsternaN);

		// Autorita Notifica Civilmente Obbligati
		Option tipoAutoritaEsternaCO = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaCivilObb", "" + tipoAutoritaEsternaCO);

		// carico il tipo provvedimento
		Option tipoProvvedimenti = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		tipoProvvedimenti.setFilter(new String[] { "-", "02", "03" }); // DECRETO o ORDINANZA
		tipoProvvedimenti.setSelected("-");
		setRequestAttribute("tipoprovvedimento", "" + tipoProvvedimenti);

		// carico AUTORITA' EMITTENTE
		Option tipoUfficio = new Option(DecodificheManager.getInstance().getTipoUfficio());
		tipoUfficio.setFilter(new String[] { "CAP", "DIB", "GUP", "GIP", "CAS", "CASAP", "TRIBSD", "GUPM",
				"CAPSM", "DIBM", "GIPM", "GP" });
		setRequestAttribute("autorita", "" + tipoUfficio);

		setRequestAttribute("modalita", "I");

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_INSERISCI_TRASMISSIONE_ATTI_CONVERSIONE;
	}

}