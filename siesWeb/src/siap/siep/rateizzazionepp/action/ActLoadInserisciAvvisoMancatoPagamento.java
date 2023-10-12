package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
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
import siap.siep.notifica.model.NotificaModel;
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
 * Classe action per il caricamento dell'inserimento dell'Avviso Mancato Pagamento
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadInserisciAvvisoMancatoPagamento extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();
		siesLogger.debug("ID_FASCICOLO = " + idFascicolo);

		String test = "";
		if (!isRequestParameterNullObj("test"))
			test = getRequestStringParameter("test");

		if (!Utils.isPresent(test)) {
			// Controlli preliminari all'inserimento di un nuovo evento
			if (fsm.getFlagValidato().equalsIgnoreCase("N")) {
				RedirectTo rt = new RedirectTo();
				rt.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno()
						+ "/" + fsm.getChiaveProgr()
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
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il Procedimento N." + fsm.getChiaveAnno() + "/" + fsm.getChiaveProgr()
								+ " Il fascicolo risulta Definito. Impossibile procedere!");
				rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
						+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
				return IWebConstants.PG_MESSAGE;
			}

			// controllo se evento non sia validato
			isEventoNonValidato();

			// Ricerca pagamento rateizzato della pena pecuniaria con rate non pagate
			IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
			Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
					.exRicercaAvvisoMancatoPagamento(idFascicolo);

			BigDecimal idEvento = null;
			if (listaRichiestaBollettini.isEmpty()) {
				RedirectTo rt = new RedirectTo();
				rt.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Non e' stato inserito un metodo di pagamento rateizzato con rate non pagate. "
								+ "Impossibile procedere! "
								+ "Si reindirizza alla pagina di Gestione Modalita' Pagamento.");
				rt.setAction("siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione&"
						+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
				return IWebConstants.PG_MESSAGE;
			} else {
				Iterator<EventoRateizzazionePPModel> iterERPPM = listaRichiestaBollettini.iterator();
				while (iterERPPM.hasNext()) {
					if (!Utils.isNullObj(idEvento))
						break;
					EventoRateizzazionePPModel erppm = iterERPPM.next();
					Vector<RateizzazionePPModel> rateizzazioni = erppm.getListaRateizzazioniPP();
					Iterator<RateizzazionePPModel> iterRPP = rateizzazioni.iterator();
					while (iterRPP.hasNext()) {
						RateizzazionePPModel rata = iterRPP.next();
						idEvento = rata.getEveIdEvento();
						break;
					}
				}
			}

			// Ricerca lo stato dei pagamenti per id fascicolo
			IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
			Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
					.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiepIdEvento(idFascicolo, idEvento);
			Iterator<BollettinoPagopaModel> iterBPM = elencoStatoPagamenti.iterator();
			BigDecimal importoPagato = new BigDecimal(0);
			BigDecimal importoDaPagare = new BigDecimal(0);
			while (iterBPM.hasNext()) {
				BollettinoPagopaModel bpm = iterBPM.next();
				if ("PA".equals(bpm.getStatoPagamento()) || "PP".equals(bpm.getStatoPagamento()))
					importoPagato = importoPagato.add(bpm.getImportoPagato());
				else
					importoDaPagare = importoDaPagare.add(bpm.getImportoRata());
			}
			setRequestAttribute("importoPagato", importoPagato.toString());
			setRequestAttribute("importoDaPagare", importoDaPagare.toString());
			setRequestAttribute("elencoStatoPagamenti", elencoStatoPagamenti);

			// MEV_2023-33: aggiunte le notifiche all'evento
			IEvento ie = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);
			Date dataAvvenutaNotifica = null;
			if (Utils.isPresent(enm.getNotifiche())) {
				for (int i = 0; i < enm.getNotifiche().length; i++) {
					NotificaModel nm = enm.getNotifiche()[i];
					if (!Utils.isNullObj(nm.getDataAvvenutaNotifica())
							&& "E".equals(nm.getCodTipoNotifica())) {
						dataAvvenutaNotifica = nm.getDataAvvenutaNotifica();
						break;
					}
				}
			}
			setRequestAttribute("dataAvvenutaNotifica", Utils.isNullObj(dataAvvenutaNotifica) ? "-"
					: DateUtils.getDateToString(dataAvvenutaNotifica, "dd-MM-yyyy"));

			Vector<RateizzazionePPModel> listaRateizzazioniEvento = irpp
					.exRicercaRateizzazioniByIdEvento(idEvento);
			Vector<EventoRateizzazionePPModel> listaRichiestaBollettiniEvento = new Vector<>();
			EventoRateizzazionePPModel erppm = new EventoRateizzazionePPModel();
			erppm.setEvento(enm.getEvento());
			erppm.setListaRateizzazioniPP(listaRateizzazioniEvento);
			listaRichiestaBollettiniEvento.add(erppm);

			if (!listaRichiestaBollettiniEvento.isEmpty()) {
				Iterator<EventoRateizzazionePPModel> iterERPPM = listaRichiestaBollettiniEvento.iterator();
				while (iterERPPM.hasNext()) {
					erppm = iterERPPM.next();
					Vector<RateizzazionePPModel> rateizzazioni = erppm.getListaRateizzazioniPP();
					// EventoModel em = erppm.getEvento();
					setRequestAttribute("evento", enm.getEvento());
					Iterator<RateizzazionePPModel> iter = rateizzazioni.iterator();
					String testo = "";
					int cont = 0;
					while (iter.hasNext()) {
						RateizzazionePPModel rata = iter.next();
						if (cont == 0)
							testo = "Importo da Pagare:  <font class='cRosso'>"
									+ StringUtils.toEuroFormat(rata.getImportoDaPagare()) + "</font> ";
						if ("R".equals(rata.getTipoRateizzazione())) { // RATE
							if (cont == 0) {
								testo += " con le seguenti modalit&agrave:";
								testo += "<ul>";
							}
							testo += "<li><font class='cViola'>" + "" + rata.getNumeroRate()
									+ "</font> rate da " + "<font class='cViola'>"
									+ StringUtils.toEuroFormat(rata.getImportoRata()) + "</font>";
							if (!Utils.isNullObj(rata.getScadenzaGiorni()) /* && cont == 0 */)
								testo += ", con scadenza pagamento entro n.ro giorni <font class='cViola'>"
										+ rata.getScadenzaGiorni().toString()
										+ "</font> dalla Notifica dell'Ingiunzione" + "</li>";
							else
								testo += "</li>";
							if (cont == rateizzazioni.size() - 1)
								testo += "</ul>";
						} else { // UNICA SOLUZIONE
							testo += " in un'unica soluzione";
							if (!Utils.isNullObj(rata.getScadenzaGiorni()))
								testo += ", termine di pagamento fissato entro "
										+ rata.getScadenzaGiorni().toString()
										+ " giorni dalla Notifica dell'Avviso di Pagamento";
						}
						cont++;
					}
					setRequestAttribute("modalitaPagamento", testo);
				}
			}
			// info per il log
			siesLogger.debug(getClass().getName() + ".processRequest: fine");

			// pagina di ritorno
			return PG_PRE_LOAD_INSERISCI_AVVISO_MANCATO_PAGAMENTO;
		} else {
			// Ricerca pagamento rateizzato della pena pecuniaria con rate non pagate
			IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
			Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
					.exRicercaAvvisoMancatoPagamento(idFascicolo);

			BigDecimal idEvento = null;
			Iterator<EventoRateizzazionePPModel> iterERPPM = listaRichiestaBollettini.iterator();
			while (iterERPPM.hasNext()) {
				if (!Utils.isNullObj(idEvento))
					break;
				EventoRateizzazionePPModel erppm = iterERPPM.next();
				Vector<RateizzazionePPModel> rateizzazioni = erppm.getListaRateizzazioniPP();
				Iterator<RateizzazionePPModel> iterRPP = rateizzazioni.iterator();
				while (iterRPP.hasNext()) {
					RateizzazionePPModel rata = iterRPP.next();
					idEvento = rata.getEveIdEvento();
					break;
				}
			}

			// Ricerca lo stato dei pagamenti per id fascicolo
			IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
			Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
					.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiepIdEvento(idFascicolo, idEvento);
			Iterator<BollettinoPagopaModel> iterBPM = elencoStatoPagamenti.iterator();
			BigDecimal importoPagato = new BigDecimal(0);
			BigDecimal importoDaPagare = new BigDecimal(0);
			while (iterBPM.hasNext()) {
				BollettinoPagopaModel bpm = iterBPM.next();
				if ("PA".equals(bpm.getStatoPagamento()) || "PP".equals(bpm.getStatoPagamento()))
					importoPagato = importoPagato.add(bpm.getImportoPagato());
				else
					importoDaPagare = importoDaPagare.add(bpm.getImportoRata());
			}
			setRequestAttribute("importoPagato", importoPagato.toString());
			setRequestAttribute("importoDaPagare", importoDaPagare.toString());
			setRequestAttribute("elencoStatoPagamenti", elencoStatoPagamenti);

			// Posizione giuridica
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
			IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(idFascicolo);
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
					.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(idFascicolo);
			setRequestAttribute("civilmenteObbligati", coms);

			// Magistrato
			IMagistratoCompetente imc = SICOLookupRemote.getMagistratoCompetenteRemote();
			MagistratoCompetenteMagistratoModel mcmm = imc
					.ExRicercaMagistratoCompetenteByFascicoloDataFine(idFascicolo);
			setRequestAttribute("magistrato", mcmm);

			// Avvocati
			try {
				IAvvocato ia = SIEPLookupRemote.getAvvocatoRemote();
				Vector avvocati = ia.ExRicercaAvvocatiByFascicolo(idFascicolo);
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
			return PG_LOAD_INSERISCI_AVVISO_MANCATO_PAGAMENTO;
		}
	}

}