package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * Action per la Load di Inserimento dell'Ordine di Esecuzione conseguente al provvedimento di
 * Rideterminazione Pena - Altro. Questa Action viene invocata dalla form della griglia dei provvedimenti
 * (stampe) della rideterminazione pena.
 *
 * @since 4.0
 */
public class ActLoadInserisciOERidetPenaAltro extends ActionSiap implements ICostantiOrdineEsecuzione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

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

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non è stato Validato. Impossibile inserire un ordine d'esecuzione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Recupero l'evento di computo e lo passo alla form di visualizzazione
		// ==========================================================================
		BigDecimal lIdEveComputo = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveComputo = lCtrlEvento.ExRicercaEventoByKey(lIdEveComputo);

		this.setRequestAttribute("aEventoComputo", lEveComputo);

		// recupero codiceMotivo="0959" da EventoComputo
		this.setRequestAttribute("codMotivoEventoComputo", lEveComputo.getCodMotivo());

		// ==========================================================================
		// Verifico se presente evento non validato ad eccezione ovviamente del
		// computo corrente che potrebbe essere non validato
		// Sperimentale: se l'evento è un OE ne carico il dettaglio
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Verifico la presenza di eventi non validati");
		EventoModel lUltimoEve = this.isEventoNonValidatoRidetPenaAltro(lEveComputo);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lUltimoEve = " + lUltimoEve);
		if (lUltimoEve != null && lUltimoEve.getIdEvento().compareTo(lEveComputo.getIdEvento()) != 0 // escludo
																										// l'evento
																										// di
																										// computo
				&& !lUltimoEve.getCodTipoEvento().equals("05") // Richiesta Istruttoria
				&& lUltimoEve.getCodMotivo() != null && !lUltimoEve.getCodMotivo().equals("0076") // 0076 =
																									// Concessione
																									// Liberazione
																									// Anticipata
				&& !lUltimoEve.getCodMotivo().equals("2130") // 2130 = Concessione Liberazione Anticipata
				&& (lUltimoEve.getFlagDocumentoRegistrato() == null
						|| "N".equals(lUltimoEve.getFlagDocumentoRegistrato()))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Esiste evento non validato: " + lUltimoEve);
			if (lUltimoEve.getCodTipoEvento().equals("01")
					&& lUltimoEve.getCodTipoProvvedimento().equals("06")
					&& (lUltimoEve.getCodMotivo().equals("0960") || lUltimoEve.getCodMotivo().equals("0961")
							|| lUltimoEve.getCodMotivo().equals("0962"))) {
				// Carico il dettaglio
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trattasi di OE, carico il dettaglio");
				String lPage = "";
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOERidetPenaAltro&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lUltimoEve.getIdEvento();
				return lPage;
			} else {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Esiste un evento '" + lUltimoEve.getDescrTipoProvvedimento() + " - "
								+ lUltimoEve.getDescrMotivo()
								+ "' NON validato. Validarlo o cancellarlo e rieseguire la funzione.");
			}
		}

		// ======================================================
		// Controllo esistenza almeno un avvocato per fascicolo.
		// ======================================================
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = null;

		try {
			lAvvocati = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					e.getMessage() + " Impossibile eseguire l'Ordine di Esecuzione.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Recupero la Posizione Giuridica
		// ==========================================================================
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

		// ==========================================================================
		// !!!! POSIZIONI GIURIDICHE ATTUALMENTE GESTITE IN QUESTA FUNZIONE !!!!
		// 10,07,20 Libero
		// 02-04 Arresti Domiciliari
		// Libero detenuto Altra Causa
		// ==========================================================================
		if (lPos != null && lPos.getPosizioneGiuridica() != null
				&& (!lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") // Libero (dopo OE)
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07") // Libero
																									// (prima
																									// di OE)
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("20") // Evaso
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02") // Custodia
																									// Cautelare
																									// per
																									// Questa
																									// Causa
																									// in
																									// Regime
																									// di
																									// Arresti
																									// Domiciliari
																									// (prima
																									// di
																									// emissione
																									// OE)
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04") // Arresti
																									// Domiciliari
																									// ex art.
																									// 656/10
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("16") // Libero in
																									// Differimento
																									// Pena
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("17") // Libero in
																									// Differimento
																									// Pena
																									// (Provvisorio)
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("46") // Libero in
																									// Sospensione
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("47") // Libero in
																									// Sospensione
																									// DPR
																									// 309/90
				)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Posizione Giuridica non gestita, impossibile procedere!");

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// Ricerca l'ultima pena residua per quel fascicolo
		// n.b. Da verificare. Questo OE viene emesso a seguito di un provvedimento
		// di rideterminazione pena che ha ricalcolato la pena e al quale deve
		// essere collegato. Per cui in teoria la PR da recuperare ed eseguire
		// dovrebbe essere quella rideterminata con il computo.
		// Tuttavia questo OE può essere emesso anche in un secondo momento
		// passando per il dettaglio del provvedimento di computo. In questo
		// caso la pena potrebbe essere stata modificata da provvedimenti
		// intermedi per cui andrebbe eseguita quest'ultima pena. Anche se in
		// questo caso che senso ha emettere questo OE? In teoria quindi l'unico
		// evento che può aver modificato al pena dovrebbe essere una correzione
		// (0219).
		// ==========================================================================
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null || (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
				&& !lPos.getPosizioneGiuridica().isLibero()
				&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-") && lPenaResMod != null
				&& lPenaResMod.getDataInizio() == null)) {
			// throw new SIEPException(SIEPException.USER_MESSAGE, "Eseguire prima il calcolo della pena.
			// Impossibile eseguire l'ordine di esecuzione.");

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			if (lPenaResMod == null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			} else {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare incoerente con Posizione Giuridica. Eseguire Calcolo della pena?");
			}

			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		//
		// ==========================================================================

		// L'Annotazione manuale corrisponde alla pena convertita, ovvero alla nuova pena residua
		// che viene ricalcolata durante l'inserimento dell'OE
		/*
		 * BigDecimal lIdEvePenaConvertita = getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO );
		 *
		 * setRequestAttribute( ICostantiEvento.CAMPO_ID_EVENTO, "" + lIdEvePenaConvertita);
		 *
		 * IAnnotazioneManuale lCtrlAnn= SIEPLookupRemote.getAnnotazioneManualeRemote();
		 * AnnotazioneManualeModel lAnnMod = lCtrlAnn.ExRicercaAnnotazioniManualiByIdEvento(
		 * lIdEvePenaConvertita );
		 *
		 * if( lAnnMod != null ) { PenaResiduaModel lPenaSanSosConvertita = new PenaResiduaModel();
		 *
		 * lPenaSanSosConvertita.setQuantumReclusione( lAnnMod.getQuantumReclusione() );
		 * lPenaSanSosConvertita.setQuantumArresto( lAnnMod.getQuantumArresto() );
		 * lPenaSanSosConvertita.setImportoMulta( lAnnMod.getImportoMulta() );
		 * lPenaSanSosConvertita.setImportoAmmenda( lAnnMod.getImportoAmmenda() );
		 *
		 * lPenaSanSosConvertita.setFlagValidato("S"); // solo per problemi di visualizzazione
		 *
		 * lPenaResMod = lPenaSanSosConvertita; }
		 */

		// ==========================================================================
		// Caricamento delle Combo da Visualizzare sulla maschera
		// ==========================================================================
		// Destinatari per la Notifica (22 = UNEP)
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// Destinatari per l'Esecuzione
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
			// 20191002 [SG]: intervento post collaudo 11.3 --> aggiunto controllo preventivo
			if (lPos.getAltraCausa() != null) {
				if (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("23")) {
					lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
				} else {
					if (lPos.getAltraCausa().getIstitutoDetenzione() != null)
						lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
								lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
				}
			}
		} else {
			if (lPos.getPosizioneGiuridica().isLibero()
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (lPos.getLuogoDetenzione() != null
						&& lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
					lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}

		setRequestAttribute("autoritaEsternaE", "" + lOption);

		// ==========================================================================
		// Recupero il Magistrato a cui è assegnato il fascicolo
		// ==========================================================================
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// Passo gli avvocati alla form
		setRequestAttribute("avvocati", lAvvocati);

		// ===
		// Date lDataInizioPena = lPenaResMod.getDataInizio();
		// Date lDataFinePenaM = lPenaResMod.getDataFine();
		// Date lDataFinePenaA = lPenaResMod.getDataFinePresunta();
		//
		// setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		// setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", lPenaResMod);

		return PG_LOAD_INSERISCI_OE_RIDET_PENA_ALTRO;
	}
}