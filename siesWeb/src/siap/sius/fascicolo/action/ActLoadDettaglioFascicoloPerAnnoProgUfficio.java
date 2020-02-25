package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.jms.util.ParserMessageRec;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.note.controller.INote;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.cancassfascsius.controller.ICancAssFascSius;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.posizionematerialefascsius.controller.IPosizioneMaterialeFascSius;
import siap.sius.provvedimento.action.ICostantiProvvedimento;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.stralcio.controller.IStralcio;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.ulterioreistanza.controller.IUlterioreIstanza;
import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.xml.TreeModel;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ActLoadDettaglioFascicoloPerAnnoProgUfficio extends ActionSius implements
		ICostantiFascicoloSius, ICostantiProvvedimento {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest : inizio");

		setLinkRitorno();

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		BigDecimal aChiaveAnno = this.getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO);
		BigDecimal aChiaveProgr = this.getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR);
		String aChiaveUfficio = this.getRequestStringParameter(CAMPO_CHIAVE_UFFICIO);

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasGPMod = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(aChiaveAnno, aChiaveProgr, aChiaveUfficio);

		Vector lVectFas = null;
		if (lFasGPMod.getFascicoloSiusModel() != null
				&& lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null
				&& lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0) {
			FascicoloSiusModel lFasRicModel = new FascicoloSiusModel();
			lFasRicModel.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			lVectFas = lCtrl.ExRicercaElencoFascicoliUnificati(lFasRicModel);
		}
		setRequestAttribute("elencoFasUnificati", lVectFas);

		// 20/06/2006 Elenco Fascicoli Collegati
		Vector lVectCol = null;
		if (lFasGPMod.getFascicoloSiusModel() != null)
			lVectCol = lCtrl.ExRicercaFascicoliXIdOrigine(lFasGPMod.getFascicoloSiusModel()
					.getIdFascicoloSius());

		setRequestAttribute("elencoFasCollegati", lVectCol);

		// Fascicolo Padre
		FascicoloGPModel lFasPadre = null;
		if (lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
			// 29/01/2008 Per fascicoli ExtraUfficio Il Fascicolo Padre può anche non esistere in archivio.
			if (CAMPO_CHIAVE_UFFICIO.compareTo(lFasGPMod.getFascicoloSiusModel().getChiaveUfficio()) == 0)
				lFasPadre = lCtrl.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel()
						.getIdFascicoloSiusOrigine());
			else
				lFasPadre = lCtrl.ExRicercaFascicoloCollegato(lFasGPMod.getFascicoloSiusModel()
						.getIdFascicoloSiusOrigine());
		}
		setRequestAttribute("fascicoloPadre", lFasPadre);

		FascicoloGPModel lUnificante = null;
		if (lFasGPMod.getFascicoloSiusModel() != null
				&& lFasGPMod.getFascicoloSiusModel().getFasSiuIdFascicoloSius() != null) {
			BigDecimal lCodUnificante = lFasGPMod.getFascicoloSiusModel().getFasSiuIdFascicoloSius();
			// 29/01/2008 Per fascicoli ExtraUfficio Il Fascicolo Unificante può anche non esistere in
			// archivio.
			if (CAMPO_CHIAVE_UFFICIO.compareTo(lFasGPMod.getFascicoloSiusModel().getChiaveUfficio()) == 0)
				lUnificante = lCtrl.ExRicercaFascicoloByKey(lCodUnificante);
			else
				lUnificante = lCtrl.ExRicercaFascicoloCollegato(lCodUnificante);
		}
		setRequestAttribute("fascicoloUnificante", lUnificante);

		// Metto in sessione il fascicolo SIUS per consentire le funzionalità annesse.
		setSessionAttribute("fascicoloSiusGP", lFasGPMod);

		// Leggo se il fascicolo e' modificabile
		String lModificabile = "NO";
		if (this.IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";

		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		// Riempi l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSius.TREE_SOGGETTO, ICostantiStampaSius.TREE_FASCICOLOSIEP,
				ICostantiStampaSius.TREE_SENTENZA, ICostantiStampaSius.TREE_AVVOCATO,
				ICostantiStampaSius.TREE_LUOGODET, ICostantiStampaSius.TREE_MAGISTRATO,
				ICostantiStampaSius.TREE_UDIENZA };
		// Crea il TreeModel con i dati che occorrono
		// Modifica del 09/09/2013 per correzione errore preesistente
		// TreeModel lTreeDati = lCtrlSta.ExPrelevaDatiVideo(
		// getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS), aTipoDati);
		TreeModel lTreeDati = lCtrlSta.ExPrelevaDatiVideo(lFasGPMod.getFascicoloSiusModel()
				.getIdFascicoloSius(), aTipoDati);

		// Converte i dati ottenuti per utilizzarli come model
		ParserMessageRec lParser = new ParserMessageRec(lTreeDati);

		this.setRequestAttribute("isModificabile", lModificabile);
		this.setRequestAttribute("UtenteConnesso", lUtenteMod);
		this.setRequestAttribute("fascicoloSiusGP", lFasGPMod);
		this.setRequestAttribute("fascicolo", lParser.getFascicolo());
		this.setRequestAttribute("residenza", lParser.getResidenza());
		this.setRequestAttribute("sentenza", lParser.getSentenza());
		this.setRequestAttribute("udienza", lParser.getUdienza());
		this.setRequestAttribute("magistrato", lParser.getMagistrati());
		this.setRequestAttribute("luogodet", lParser.getLuogoDetenzione());
		this.setRequestAttribute("istitutodet", lParser.getIstitutoDetenzione());
		this.setRequestAttribute("avvocato", lParser.getAvvocato());

		// 08/2014 - Magistrato ESPERTO

		if (lParser.getMagistrati() != null) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			// siesLogger.debug(" XXXXX ----> lMagistrato != null");
		} else if (lParser.getEsperti() != null) {
			Iterator itx33 = lParser.getEsperti().iterator();
			while (itx33.hasNext()) {
				EspertoModel lEspMod = (EspertoModel) itx33.next();
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				// siesLogger.debug(" XXXXX ----> lEspMod = "+lEspMod);
				this.setRequestAttribute("esperto", lEspMod);
			}
		} else {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			// siesLogger.debug(" XXXXX ----> lMagistrato e Esperto = null");
		}

		// Permesso / Licenza Depositata
		LicenzaLibAnticipataModel lLibAnt = ricercaPermessoLicenzaDepositata(lFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("licenza", lLibAnt);

		// 20061113
		// Inserisce il Soggetto e la Sentenza nel Fascicolo SIEP, questo passaggio è necessario
		// poichè nella JSP di elenco provvedimenti, viene utiliìzzata una include di una JSP del
		// dettaglioSoggettoSentenza che preleva i dati del soggetto e della sentenza dal fascicolo SIEP
		// che risiede in sessione, pertanto al fine di risolvere un errore di null pointer
		// exception si inserisce l'entità soggetto e sentenza nel fascicolo.
		// 20061113
		// Quando si proviene da SIUS, è possibile che un determinato procedimento non ha
		// un fascicolo SIEP associato pertanto, al fine di evitare un null pointer exception,
		// si condiziona il != null per il fascicolo SIEP e rimanendo inalterato il flusso si
		// rimette in sessione il fascicolo SIEP indipendentemente se è null oppure no.
		if (lParser.getFascicolo() != null) {
			lParser.getFascicolo().setSoggetto(lParser.getSoggetto());
			lParser.getFascicolo().setSentenza(lParser.getSentenza());
		}

		// Il Fascicolo SIEP va messo in sessione.
		setSessionAttribute("fascicolo", lParser.getFascicolo());

		// ricerca gli eventi collegati al fascicolo e li passa nella Request
		INotifica nCtrl = SIEPLookupRemote.getNotificaRemote();
		Vector lVect = nCtrl.ExRicercaNotificheByFascicoloSius(lFasGPMod.getFascicoloSiusModel()
				.getIdFascicoloSius(), "05");
		setRequestAttribute("atti", lVect);

		// Metto in sessione il Dettaglio Fascicolo Siep per recuperare Pena Residua, Posizione Giuridica etc.
		if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			IFascicoloSiep fCtrl = (IFascicoloSiep) SIEPLookupRemote.getFascicoloSiepRemote();
			DettaglioFascicoloModel detFasSiep = fCtrl.ExDettaglioFascicoloSiep(lFasGPMod
					.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
			setRequestAttribute("dettagliofascicolo", detFasSiep);
		}

		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		lVect = mCtrl.ExRicercaEventoByFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),
				COD_EVENTO_PROVVEDIMENTO);
		setRequestAttribute("provvedimenti", lVect);

		// ========================================================================
		// new DL 146 opposizione e impugnazioni vanno recuperate in modo separato
		Hashtable<BigDecimal, Vector<ImpugnazioneModel>> lImpugnazioniEvento = new Hashtable<BigDecimal, Vector<ImpugnazioneModel>>();
		Hashtable<BigDecimal, Vector<ImpugnazioneModel>> lOpposizioniEvento = new Hashtable<BigDecimal, Vector<ImpugnazioneModel>>();

		IImpugnazione lCtrlImp = SIUSLookupRemote.getImpugnazioneRemote();
		for (int i = 0; i < lVect.size(); i++) {
			EventoModel lProv = (EventoModel) lVect.elementAt(i);

			// =================================================
			Vector<ImpugnazioneModel> lImpugnazioni = null;
			lImpugnazioni = lCtrlImp.ExRicercaImpugnazioniByIdEventoTipoProvvTipoImpFlagAnn(
					lProv.getIdEvento(), lProv.getCodTipoProvvedimento(), new String[] { "01", "02", "03" } // solo
																											// Impugnazioni/ricorsi
					, null);
			lImpugnazioniEvento.put(lProv.getIdEvento(), lImpugnazioni);

			// =================================================
			Vector<ImpugnazioneModel> lOpposizioni = null;
			lOpposizioni = lCtrlImp.ExRicercaImpugnazioniByIdEventoTipoProvvTipoImpFlagAnn(
					lProv.getIdEvento(), lProv.getCodTipoProvvedimento(), new String[] { "04" } // solo
																								// opposizioni
					, null);
			lOpposizioniEvento.put(lProv.getIdEvento(), lOpposizioni);
		}
		setRequestAttribute("impugnazioniEvento", lImpugnazioniEvento);
		setRequestAttribute("opposizioniEvento", lOpposizioniEvento);
		//
		// ========================================================================

		/*
		 * IImpugnazione oCtrl = SIUSLookupRemote.getImpugnazioneRemote(); Vector aData = new Vector(); if
		 * (lVect.size() > 0 ){ Iterator itx = lVect.iterator(); while (itx.hasNext()) { EventoModel lProv =
		 * (EventoModel) itx.next();
		 * //aData.add(oCtrl.ExRicercaDataRicorso(lProv.getIdEvento(),lProv.getCodTipoProvvedimento() ));
		 * aData.add(oCtrl.ExRicercaDateRicorsi(lProv.getIdEvento(),lProv.getCodTipoProvvedimento() )); } }
		 * setRequestAttribute("provvedimentiDataRicorso", aData);
		 */

		Vector lVectAltri = mCtrl.ExRicercaAltroEventoByFascicoloSius(lFasGPMod.getFascicoloSiusModel()
				.getIdFascicoloSius(), COD_EVENTO_PROVVEDIMENTO);
		if (lVectAltri != null)
			setRequestAttribute("provvedimentiAltri", lVectAltri);
		else
			siesLogger.debug("provvedimentiAltri assente");

		// Elenco Altri Titoli Esecutivi
		RiferimentoFascicoloSiepModel lRFSMod = new RiferimentoFascicoloSiepModel();
		lRFSMod.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		IRiferimentoFascicoloSiep lCtrlRFS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
		Vector lRiferimenti = new Vector();
		lRiferimenti = lCtrlRFS.ExRicercaRiferimentoFascicoloSiep(lRFSMod);
		setRequestAttribute("riferimenti", lRiferimenti);

		// Elenco Movimenti Udienza
		Vector lUdiProVect = null;
		IUdienzaProcedimento lUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		lUdiProVect = lUdiProCtrl.ExRicercaUdienzaProcedimentoUdiByGeneraleProcedimento(lFasGPMod
				.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		setRequestAttribute("MovimentiUdienze", lUdiProVect);

		// Elenco NOTE STUB 06/09/2005
		Vector lNoteVect = null;
		INote lNoteCtrl = SIUSLookupRemote.getNoteRemote();
		lNoteVect = lNoteCtrl.ExRicercaNote(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("elencoNote", lNoteVect);

		// Posizione Materiale Fascicolo
		IPosizioneMaterialeFascSius lPosMatCtrl = SIUSLookupRemote.getPosizioneMaterialeFascSiusRemote();
		Vector lPosizioniMat = lPosMatCtrl.ExRicercaPosizioneMaterialeFascAttiva(lFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius());
		if (lPosizioniMat != null && lPosizioniMat.size() > 0)
			setRequestAttribute("posizione_materiale", (PosizioneMaterialeFascModel) lPosizioniMat.get(0));

		// Cancelleria Assegnataria
		ICancAssFascSius lCancAssFascCtrl = SIUSLookupRemote.getCancAssFascSiusRemote();
		CancAssFascSiusModel lCancAssFascAttiva = lCancAssFascCtrl.ExRicercaCancAssFascSiusAttiva(lFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius());
		// Se esiste una Cancelleria Assegnataria per il fascicolo viene passato alla request
		if (lCancAssFascAttiva != null)
			setRequestAttribute("cancelleria_assegnataria", lCancAssFascAttiva);

		// Elenco Provvedimenti X Foglio Complementare
		DocumentoAllegatoModel mDocAll = null;
		IDocumentoAllegato mDocAllCtrl = null;

		Vector lFogliCompVect = new Vector();
		Vector lEstremiFCVect = new Vector();
		// MEV10-s3: aggiunto parametro di passaggio per gestire tipologia ufficio minorenni
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		Vector lVectFC = mCtrl.ExRicercaEventoXCFC(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),
				COD_EVENTO_PROVVEDIMENTO, strCodTipoUfficio);

		if (lVectFC.size() > 0) {
			Iterator itx = lVectFC.iterator();
//			int k = 0;
			while (itx.hasNext()) {
				EventoModel lProv = (EventoModel) itx.next();
				if (lProv.getNumAllegati() > 0)
					;
				{
					mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
					mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lProv.getIdEvento(),
							"06");
					if (mDocAll != null) {
						lFogliCompVect.add(lProv.getIdEvento());
						lEstremiFCVect.add(mDocAll.getAnnoFoglioComplementare().toString() + "/"
								+ mDocAll.getProgrFoglioComplementare());
					}
				}
//				k++;
			}
		}

		setRequestAttribute("provvedimentiFoglioComp", lFogliCompVect);
		setRequestAttribute("estremiFoglioComp", lEstremiFCVect);

		// 08/01/2007 Elenco Tenori Stralciati
		Vector lVectTenStralcio = null;
		IStralcio lCtrlStra = SIUSLookupRemote.getStralcioRemote();
		if (lFasGPMod.getFascicoloSiusModel() != null) {
			lVectTenStralcio = lCtrlStra.ExRicercaTenoriStralciatiByIdFascicolo(lFasGPMod
					.getFascicoloSiusModel().getIdFascicoloSius());
		}
		setRequestAttribute("elencoTenoriStralcio", lVectTenStralcio);

		// 06/06/2007 Elenco Ulteriori Istanze
		Vector lVectUI = null;
		if (lFasGPMod.getFascicoloSiusModel() != null) {
			UlterioreIstanzaModel lUltIstMod = new UlterioreIstanzaModel();
			lUltIstMod.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			IUlterioreIstanza lCtrlUI = SIUSLookupRemote.getUlterioreIstanzaRemote();
			lVectUI = lCtrlUI.ExRicercaUlterioreIstanza(lUltIstMod);
		}
		// Imposta l'elenco delle ulteriori istanze.
		setRequestAttribute("ulterioriistanze", lVectUI);

		// Collaboratore di giustizia
		if (isCollaboratoreDiGiustizia(lFasGPMod)) {
			setRequestAttribute("collaboratore", "Collaboratore di Giustizia");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("è un collaboratore di Giustizia");
		}

		// Per un fascicolo EMS verificare la congruenza con il fascicolo AMS o EMS trasformato collegato
		if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
				.compareTo(COD_OGGETTO_PROCEDIMENTO_MS) == 0)
			verificaOggettiEMSAMS(lFasGPMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest : fine");

		return PG_DETTAGLIOFASCICOLOSIUS;
	}

	private boolean isCollaboratoreDiGiustizia(FascicoloGPModel aFasGPMod) throws Exception {
		boolean retValue = false;
		if (aFasGPMod != null && aFasGPMod.getFascicoloSiusModel() != null
				&& aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() != null) {
			ICollaboratore lCtrl = SIUSLookupRemote.getCollaboratoreRemote();
			if (lCtrl.ExIsPackage())
				retValue = lCtrl.ExIsCollaboratore(aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),
						getCodUfficioUtenteConnesso());
		}
		return retValue;
	}

	/**
	 * Esegue la ricerca del permesso o licenza depositata.
	 * <p>
	 * 
	 * @param aIdFascSius
	 *            id del fascilo sius di riferimento.
	 * @return
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	private LicenzaLibAnticipataModel ricercaPermessoLicenzaDepositata(BigDecimal aIdFascSius)
			throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("##### RicercaPermessoLicenzaDepositata ###### ");
		IPermesso lCtrl = SIUSLookupRemote.getPermessoRemote();
		return lCtrl.ExRicercaTipoPermessoLicenzaDepositata(aIdFascSius);
	}

	/**
	 * Verifica che le misure applicate nell'ordinanza del fascicolo collegato siano oggetto di questo o di
	 * altri fascicoli, quindi prepara un warning che sarà letto nella jsp di dettaglio
	 * <p>
	 * 
	 * @param lFasGPEMS
	 *            FascicoloGPModel di riferimento.
	 * @return
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	private void verificaOggettiEMSAMS(FascicoloGPModel lFasGPEMS) throws Exception {
		String warnigToJsp = null;

		IEsecuzioneMS lCtrEMS = SIUSLookupRemote.getEsecuzioneMSRemote();
		EsecuzioneMisuraSicurezzaModel lEMSMod = new EsecuzioneMisuraSicurezzaModel();

		lEMSMod = lCtrEMS.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lFasGPEMS.getFascicoloSiusModel()
				.getIdFascicoloSius());
		if (lEMSMod != null && lEMSMod.getDepOpidDepositoOrdinanzaPc() != null) {
			IDepositoOrdinanzaPc lCtrDOPC = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel DOPCMod = lCtrDOPC.ExRicercaDepositoOrdinanzaPcByKey(lEMSMod
					.getDepOpidDepositoOrdinanzaPc());

			if (DOPCMod != null && DOPCMod.getIdEventoGenerato() != null) {
				IEvento lCtrEve = SICOLookupRemote.getEventoRemote();
				EventoModel EveEMSMod = lCtrEve.ExRicercaEventoByKey(DOPCMod.getIdEventoGenerato());

				// Occorre distinguere tra eredità da AMS e da EMS trasformata

				if (EveEMSMod != null && EveEMSMod.getFasSiuIdFascicoloSius() != null) {
					IFascicoloSius lCtrFasAMS = SIUSLookupRemote.getFascicoloSiusRemote();
					FascicoloGPModel lFasGPAMS = lCtrFasAMS.ExRicercaFascicoloByKey(EveEMSMod
							.getFasSiuIdFascicoloSius());
					Vector listaOggettiMisure = new Vector();
					if (EveEMSMod.getCodMotivo() != null
							&& (EveEMSMod.getCodMotivo().equals("2110")
									|| EveEMSMod.getCodMotivo().equals("2111")
									|| EveEMSMod.getCodMotivo().equals("2112")
									|| EveEMSMod.getCodMotivo().equals("2113") || EveEMSMod.getCodMotivo()
									.equals("2114"))) { // Deriva da Ordinanza AMS
						IMisuraSicurezza lCtrMS = SIEPLookupRemote.getMisuraSicurezzaRemote();
						MisuraSicurezzaModel lMSMod = new MisuraSicurezzaModel();
						lMSMod.setFasSiuIdFascicoloSius(EveEMSMod.getFasSiuIdFascicoloSius());
						Vector listaMisureAMS = lCtrMS.ExRicercaMisuraSicurezza(lMSMod);
						Iterator itxAMS = listaMisureAMS.iterator();
						while (itxAMS.hasNext()) {
							MisuraSicurezzaModel lAMSMod = (MisuraSicurezzaModel) itxAMS.next();
							if (lAMSMod.getCodTipo() != null)
								listaOggettiMisure.addElement(lAMSMod.getCodOggettoEsecuzione());
							// listaOggettiMisure.addElement(lAMSMod.getDescrTipo());
						}
					} else { // Deriva da Ordinanza EMS Trasformata
						if (lEMSMod.getCodTipoMisura() != null)
							listaOggettiMisure.addElement(lEMSMod.getCodTipoMisura());
					}

					warnigToJsp = "Attenzione! Nessun oggetto in esecuzione corrisponde alla misura applicata!";
					if (listaOggettiMisure != null && lFasGPAMS.getTenori() != null
							&& lFasGPAMS.getTenori().length > 0) {
						for (int jOgg = 0; jOgg < lFasGPEMS.getTenori().length; jOgg++) {
							if (listaOggettiMisure.indexOf(lFasGPEMS.getTenori()[jOgg].getCodOggettoTenore()) >= 0)
								// lFasGPAMS.getTenori()[jOgg].getCodOggettoTenore();
								warnigToJsp = "OK";
						}
					}

				}

			}

		}

		setRequestAttribute("fascEMSdaAMS", warnigToJsp);

	}

}