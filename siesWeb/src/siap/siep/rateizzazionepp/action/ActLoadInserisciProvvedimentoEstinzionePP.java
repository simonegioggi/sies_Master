package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
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
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe action per il caricamento dell'inserimento del Provvedimento di AVVENUTO PAGAMENTO pena pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadInserisciProvvedimentoEstinzionePP extends ActionSiap
		implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Controlli preliminari all'inserimento di un nuovo evento
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " non è stato Validato. Impossibile inserire il Provvedimento di Avvenuto Pagamento!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		// controllo se evento non sia validato
		isEventoNonValidato();

		// Gli OI o Assimilabile e relative rate e bollettini
		// Attenzione serve per controllare che tutte le rate siano state pagate o meglio che l'importo dovuto
		// si stato pagato.
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		String[] listaCodici = new String[] { "0622", "1307", "1308" };
		// True = Solo validati
		Vector<EventoRateizzazionePPModel> listaOrdiniIngiunzione = irpp
				.exRicercaEventiRateizzazionePP(lFascMod.getIdFascicoloSiep(), listaCodici, true);

		IBollettinoPagopa lBollCtrl = SIEPLookupRemote.getBollettinoPagopaRemote();
		for (EventoRateizzazionePPModel evento : listaOrdiniIngiunzione) {
			Vector<RateizzazionePPModel> listaRateizzazioni = evento.getListaRateizzazioniPP();
			for (RateizzazionePPModel rata : listaRateizzazioni) {
				Vector<BollettinoPagopaModel> listaBollettini = lBollCtrl
						.ExRicercaBollettiniPagopaByIdRateizzazione(rata.getIdRateizzazionePP());
				rata.setListaBollettini(listaBollettini);
			}
		}
		// setRequestAttribute("listaOrdiniIngiunzione", listaOrdiniIngiunzione);

		EventoRateizzazionePPModel ultimoOI = null;
		Vector<RateizzazionePPModel> listaRateizzazioni = null;
		if (listaOrdiniIngiunzione.size() > 0) {
			// Prendo in considerazione l'ultimo OI o assimilabile
			ultimoOI = listaOrdiniIngiunzione.elementAt(0);

			// Carico in form solo il più recente
			Vector<EventoRateizzazionePPModel> listaForm = new Vector<>();
			listaForm.add(ultimoOI);
			setRequestAttribute("listaOrdiniIngiunzione", listaForm);

			listaRateizzazioni = ultimoOI.getListaRateizzazioniPP();

			BigDecimal importoDaPagare = listaRateizzazioni.elementAt(0).getImportoDaPagare();
			BigDecimal importoRate = new BigDecimal(0);
			BigDecimal importoPagato = new BigDecimal(0);

			for (RateizzazionePPModel rate : listaRateizzazioni) {
				Vector<BollettinoPagopaModel> listaBollettini = rate.getListaBollettini();
				for (BollettinoPagopaModel bollettino : listaBollettini) {
					importoRate = importoRate.add(bollettino.getImportoRata());
					importoPagato = importoPagato
							.add(bollettino.getImportoPagato() != null ? bollettino.getImportoPagato()
									: new BigDecimal(0));
				}
			}

			if (importoDaPagare.compareTo(importoPagato) != 0) {
				EventoModel eve = ultimoOI.getEvento();
				String pre = "l'";
				if (eve.getCodTipoProvvedimento().equals("04"))
					pre = "il ";

				String warnImportoRate = "Per " + pre + eve.getDescrTipoProvvedimento() + " "
						+ eve.getDescrMotivo();
				warnImportoRate += " del " + DateUtils.getDateToString(eve.getDataEmissione(), "dd/MM/yyyy")
						+ "";
				warnImportoRate += " l'importo pagato " + StringUtils.toEuroFormat(importoPagato) + " Euro";
				warnImportoRate += " non corrisponde all'importo da pagare "
						+ StringUtils.toEuroFormat(importoDaPagare) + " Euro.";
				warnImportoRate += " Non è possibile procedere con l'emissione del provvedimento.";

				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, warnImportoRate);

				lRedirigi.setAction("siap.siep.sanzionesostitutiva.action.ActElencoStatoPagamenti&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + eve.getIdEvento());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}
		} else {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Non risulta emesso alcun ordine di ingiunzione. Impossibile procedere.");

			lRedirigi.setAction(
					"siap.siep.sanzionesostitutiva.action.ActGrigliaOrdineIngiunzioneAltriProvvedimenti");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		// TODO VERIFICARE
		/*
		 * IEvento lCtrl = SICOLookupRemote.getEventoRemote(); Hashtable<BigDecimal, EventoNotificaModel>
		 * listaOrdiniIngiunzione = new Hashtable<>(); // MEV_2023-33: aggiunto controllo per storicizzazione
		 * evento OIP for (RateizzazionePPModel rata : listaRateizzazioni) { if (rata.getEveIdEvento() !=
		 * null) { EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(rata.getEveIdEvento());
		 * if (listaOrdiniIngiunzione.get(rata.getEveIdEvento()) != null) {
		 * rata.setOrdineIngiunzione(listaOrdiniIngiunzione.get(rata.getEveIdEvento())); } else {
		 * rata.setOrdineIngiunzione(lEveNotMod);
		 * listaOrdiniIngiunzione.put(lEveNotMod.getEvento().getIdEvento(), lEveNotMod); }
		 * rata.setStoricizzato("A".equals(lEveNotMod.getEvento().getFlagDocumentoRegistrato())); } }
		 *
		 * setRequestAttribute("listaRateizzazioni", listaRateizzazioni);
		 */

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());
		if (lPos == null || lPos.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Ricerco il civilmente Obbligato se esiste
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("civilmenteObbligati", coms);

		// Magistrato
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// Avvocati
		try {
			IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
			Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvocati);
		} catch (SIEPException e) {
			// nessun avvocato trovato
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato associato alcun avvocato.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Autorità esterna
		Option lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		// Verifico se sovrescrivere l'auturità esterna
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
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

		setRequestAttribute("modalita", "I");

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_INSERISCI_PROVVEDIMENTO_ESTINZIONE_PP;
	}

}