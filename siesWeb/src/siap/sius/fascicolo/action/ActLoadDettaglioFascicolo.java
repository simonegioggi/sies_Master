package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.util.xml.TreeModel;
import siap.jms.util.ParserMessageRec;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.note.controller.INote;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.model.SentenzaModel;
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

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadDettaglioFascicolo extends ActionSius
		implements ICostantiFascicoloSius, ICostantiProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".processRequest : inizio");

		setLinkRitorno();

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		// Modifica del 09/09/2013 "Revisione Misure di Sicurezza SIUS"
		// Risolto errore preesistente (Questa classe viene invocata anche dopo aver inserito
		// e poi successivamente cancellato un Riferimento Altro Titolo Esecutivo.
		// In questo caso il campo "CAMPO_ID_FASCICOLO_SIUS" deve essere recuperato dalla
		// sessione e non dalla form altrimenti restituisce null).
		String idFascSius = getParameter(CAMPO_ID_FASCICOLO_SIUS);
		BigDecimal aIdFascicoloSius = null;
		if (idFascSius != null) {
			aIdFascicoloSius = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS);
		} else {
			aIdFascicoloSius = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
					.getFascicoloSiusModel().getIdFascicoloSius();
		}

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod.getFascicoloSiusModel().setIdFascicoloSius(aIdFascicoloSius);

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasGPMod = lCtrl.ExRicercaFascicoloByKey(aIdFascicoloSius);

		BigDecimal lengthCertPenale = lCtrl.ExGetLengthCertPenaleByIdFascicolo(aIdFascicoloSius);

		Vector lVectFas = null;
		if (lFasGPMod.getFascicoloSiusModel() != null
				&& lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null
				&& lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0) {
			FascicoloSiusModel lFasRicModel = new FascicoloSiusModel();
			lFasRicModel.setFasSiuIdFascicoloSius(aIdFascicoloSius);
			lVectFas = lCtrl.ExRicercaElencoFascicoliUnificati(lFasRicModel);
		}
		setRequestAttribute("elencoFasUnificati", lVectFas);

		// 20/06/2006 Elenco Fascicoli Collegati
		Vector lVectCol = null;
		if (lFasGPMod.getFascicoloSiusModel() != null)
			lVectCol = lCtrl.ExRicercaFascicoliXIdOrigine(aIdFascicoloSius);

		setRequestAttribute("elencoFasCollegati", lVectCol);

		// Fascicolo Padre
		FascicoloGPModel lFasPadre = null;
		if (lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
			// 29/01/2008 Per fascicoli ExtraUfficio Il Fascicolo Padre può anche non esistere in archivio.
			if (CAMPO_CHIAVE_UFFICIO.compareTo(lFasGPMod.getFascicoloSiusModel().getChiaveUfficio()) == 0)
				lFasPadre = lCtrl.ExRicercaFascicoloByKey(
						lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine());
			else
				lFasPadre = lCtrl.ExRicercaFascicoloCollegato(
						lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine());
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
		if (IsFascicoloSiusModificabile())
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
		TreeModel lTreeDati = lCtrlSta.ExPrelevaDatiVideo(aIdFascicoloSius, aTipoDati);

		// Converte i dati ottenuti per utilizzarli come model
		ParserMessageRec lParser = new ParserMessageRec(lTreeDati);

		// Permesso / Licenza Depositata
		LicenzaLibAnticipataModel lLibAnt = ricercaPermessoLicenzaDepositata(aIdFascicoloSius);
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
		Vector lVect = nCtrl.ExRicercaNotificheByFascicoloSius(aIdFascicoloSius, "05");
		setRequestAttribute("atti", lVect);

		// Metto in sessione il Dettaglio Fascicolo Siep per recuperare Pena Residua, Posizione Giuridica etc.
		if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			IFascicoloSiep fCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			DettaglioFascicoloModel detFasSiep = fCtrl
					.ExDettaglioFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
			setRequestAttribute("dettagliofascicolo", detFasSiep);
		}

		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		lVect = mCtrl.ExRicercaEventoByFascicoloSius(aIdFascicoloSius, COD_EVENTO_PROVVEDIMENTO);
		setRequestAttribute("provvedimenti", lVect);

		// ========================================================================
		// new DL 146 opposizione e impugnazioni vanno recuperate in modo separato
		Hashtable<BigDecimal, Vector<ImpugnazioneModel>> lImpugnazioniEvento = new Hashtable<>();
		Hashtable<BigDecimal, Vector<ImpugnazioneModel>> lOpposizioniEvento = new Hashtable<>();

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
		Vector lVectAltri = mCtrl.ExRicercaAltroEventoByFascicoloSius(aIdFascicoloSius,
				COD_EVENTO_PROVVEDIMENTO);
		if (lVectAltri != null)
			setRequestAttribute("provvedimentiAltri", lVectAltri);
		else
			siesLogger.info("provvedimentiAltri assente");

		// Elenco Altri Titoli Esecutivi
		RiferimentoFascicoloSiepModel lRFSMod = new RiferimentoFascicoloSiepModel();
		lRFSMod.setFasSiuIdFascicoloSius(aIdFascicoloSius);
		IRiferimentoFascicoloSiep lCtrlRFS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
		Vector lRiferimenti = new Vector();
		lRiferimenti = lCtrlRFS.ExRicercaRiferimentoFascicoloSiep(lRFSMod);
		setRequestAttribute("riferimenti", lRiferimenti);

		// Elenco Movimenti Udienza
		Vector lUdiProVect = null;
		IUdienzaProcedimento lUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		lUdiProVect = lUdiProCtrl.ExRicercaUdienzaProcedimentoUdiByGeneraleProcedimento(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		setRequestAttribute("MovimentiUdienze", lUdiProVect);

		// Elenco NOTE STUB 06/09/2005
		Vector lNoteVect = null;
		INote lNoteCtrl = SIUSLookupRemote.getNoteRemote();
		lNoteVect = lNoteCtrl.ExRicercaNote(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("elencoNote", lNoteVect);

		// Posizione Materiale Fascicolo
		IPosizioneMaterialeFascSius lPosMatCtrl = SIUSLookupRemote.getPosizioneMaterialeFascSiusRemote();
		Vector lPosizioniMat = lPosMatCtrl.ExRicercaPosizioneMaterialeFascAttiva(
				lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		if (lPosizioniMat != null && lPosizioniMat.size() > 0)
			setRequestAttribute("posizione_materiale", lPosizioniMat.get(0));

		setRequestAttribute("isModificabile", lModificabile);
		setRequestAttribute("UtenteConnesso", lUtenteMod);
		setRequestAttribute("fascicoloSiusGP", lFasGPMod);
		setRequestAttribute("fascicolo", lParser.getFascicolo());
		setRequestAttribute("residenza", lParser.getResidenza());
		setRequestAttribute("sentenza", lParser.getSentenza());
		setRequestAttribute("udienza", lParser.getUdienza());
		setRequestAttribute("magistrato", lParser.getMagistrati());
		setRequestAttribute("luogodet", lParser.getLuogoDetenzione());
		setRequestAttribute("istitutodet", lParser.getIstitutoDetenzione());
		setRequestAttribute("avvocato", lParser.getAvvocato());

		// 08/2014 - Magistrato ESPERTO
		if (lParser.getMagistrati() != null) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" XXXXX ----> lMagistrato != null");
		} else if (lParser.getEsperti() != null) {
			Iterator itx33 = lParser.getEsperti().iterator();
			while (itx33.hasNext()) {
				EspertoModel lEspMod = (EspertoModel) itx33.next();
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" XXXXX ----> lEspMod = "+lEspMod);
				setRequestAttribute("esperto", lEspMod);
			}
		}

		// Cancelleria Assegnataria
		ICancAssFascSius lCancAssFascCtrl = SIUSLookupRemote.getCancAssFascSiusRemote();
		CancAssFascSiusModel lCancAssFascAttiva = lCancAssFascCtrl
				.ExRicercaCancAssFascSiusAttiva(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
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
			while (itx.hasNext()) {
				EventoModel lProv = (EventoModel) itx.next();
				if (lProv.getNumAllegati() > 0) {
					mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
					mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lProv.getIdEvento(),
							"06");
					if (mDocAll != null) {
						lFogliCompVect.add(lProv.getIdEvento());
						lEstremiFCVect.add(mDocAll.getAnnoFoglioComplementare().toString() + "/"
								+ mDocAll.getProgrFoglioComplementare());
					}
				}
			}
		}

		setRequestAttribute("provvedimentiFoglioComp", lFogliCompVect);
		setRequestAttribute("estremiFoglioComp", lEstremiFCVect);

		// 08/01/2007 Elenco Tenori Stralciati
		Vector lVectTenStralcio = null;
		IStralcio lCtrlStra = SIUSLookupRemote.getStralcioRemote();
		if (lFasGPMod.getFascicoloSiusModel() != null) {
			lVectTenStralcio = lCtrlStra.ExRicercaTenoriStralciatiByIdFascicolo(aIdFascicoloSius);
		}
		setRequestAttribute("elencoTenoriStralcio", lVectTenStralcio);

		// 06/06/2007 Elenco Ulteriori Istanze
		Vector lVectUI = null;
		if (lFasGPMod.getFascicoloSiusModel() != null) {
			UlterioreIstanzaModel lUltIstMod = new UlterioreIstanzaModel();
			lUltIstMod.setFasSiuIdFascicoloSius(aIdFascicoloSius);
			IUlterioreIstanza lCtrlUI = SIUSLookupRemote.getUlterioreIstanzaRemote();
			lVectUI = lCtrlUI.ExRicercaUlterioreIstanza(lUltIstMod);
		}
		// Imposta l'elenco delle ulteriori istanze.
		setRequestAttribute("ulterioriistanze", lVectUI);

		// Collaboratore di giustizia
		if (isCollaboratoreDiGiustizia(lFasGPMod)) {
			setRequestAttribute("collaboratore", "Collaboratore di Giustizia");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("è un collaboratore di Giustizia");
		}

		// Per un fascicolo EMS verificare la congruenza con il fascicolo AMS o EMS trasformato collegato
		if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
				.compareTo(COD_OGGETTO_PROCEDIMENTO_MS) == 0)
			verificaOggettiEMSAMS(lFasGPMod);

		// =========================================================================
		// MEV 12 - Richiesta Certificato Penale
		// =========================================================================
		// se è presente il Certificato Casellario Giudiziale recupero la data INVIO dello stesso.
		if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null
				&& lengthCertPenale.intValue() > 0) {
			INotifica mCtrlNot = SIEPLookupRemote.getNotificaRemote();
			String lTipoEvento = "05";
			String lCodMotivo = "0050";

			Date dataInvioCertificato = mCtrlNot.ExRicercaDataInvioCertCasellario(
					lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(), lTipoEvento, lCodMotivo);
			setRequestAttribute("dataInvioCertificato", dataInvioCertificato);
			setRequestAttribute("certificatoPenale", "SI");
		} else {
			setRequestAttribute("certificatoPenale", "NO");
		}

		// MEV10-s3: aggiunta gestione soggetto Maggiorenne/Minorenne
		// if ((soggMod.getDataNascitaPresunta() != null && "S".equals(soggMod.getDataNascitaPresunta()))
		// || soggMod.getEtaPresuntaAnni() != null)
		if (!"N".equals(lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne()))
			checkMinorenne(lFasGPMod.getFascicoloSiusModel().getSoggetto(), lFasGPMod, lParser.getSentenza());

		/* 
		 * ISSUE MEV : aggiunta estrazione data esecutivita
		 * Numero MEV : 9
		 * Autore    : sgioggi
		 * Data      : 5 dic 2022
		 * Branch    : MEV_9
		 */
		if (!Utils.isNullObj(lFasGPMod) && !Utils.isNullObj(lFasGPMod.getGeneraleProcedimentoModel())
				&& !Utils.isNullObj(lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
				&& (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C050") == 0
				|| lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C051") == 0)) {
			IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel dopm = idop.ExRicercaDepositoOrdinanzaPcByGenProc(
					lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			Date dataEsecutivita = null;
			if (!Utils.isNullObj(dopm) && !Utils.isNullObj(dopm.getDataEsecutivita()))
				dataEsecutivita = dopm.getDataEsecutivita();
			setRequestAttribute("dataEsecutivita", dataEsecutivita);
		}
		//***** FINE INTERVENTO MEV_9 *****//

		// info per il log
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".processRequest : fine");

		// valore di ritorno
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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
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

		lEMSMod = lCtrEMS.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(
				lFasGPEMS.getFascicoloSiusModel().getIdFascicoloSius());
		if (lEMSMod != null && lEMSMod.getDepOpidDepositoOrdinanzaPc() != null) {
			IDepositoOrdinanzaPc lCtrDOPC = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel DOPCMod = lCtrDOPC
					.ExRicercaDepositoOrdinanzaPcByKey(lEMSMod.getDepOpidDepositoOrdinanzaPc());

			if (DOPCMod != null && DOPCMod.getIdEventoGenerato() != null) {
				IEvento lCtrEve = SICOLookupRemote.getEventoRemote();
				EventoModel EveEMSMod = lCtrEve.ExRicercaEventoByKey(DOPCMod.getIdEventoGenerato());

				// Occorre distinguere tra eredità da AMS e da EMS trasformata

				if (EveEMSMod != null && EveEMSMod.getFasSiuIdFascicoloSius() != null) {
					IFascicoloSius lCtrFasAMS = SIUSLookupRemote.getFascicoloSiusRemote();
					FascicoloGPModel lFasGPAMS = lCtrFasAMS
							.ExRicercaFascicoloByKey(EveEMSMod.getFasSiuIdFascicoloSius());
					Vector listaOggettiMisure = new Vector();
					if (EveEMSMod.getCodMotivo() != null && (EveEMSMod.getCodMotivo().equals("2110")
							|| EveEMSMod.getCodMotivo().equals("2111")
							|| EveEMSMod.getCodMotivo().equals("2112")
							|| EveEMSMod.getCodMotivo().equals("2113")
							|| EveEMSMod.getCodMotivo().equals("2114"))) { // Deriva da Ordinanza AMS
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
							if (listaOggettiMisure
									.indexOf(lFasGPEMS.getTenori()[jOgg].getCodOggettoTenore()) >= 0)
								// lFasGPAMS.getTenori()[jOgg].getCodOggettoTenore();
								warnigToJsp = "OK";
						}
					}

				}

			}

		}

		setRequestAttribute("fascEMSdaAMS", warnigToJsp);

	}

	/**
	 * MEV10-s3: aggiunto metodo di controllo età soggetto
	 *
	 * @param soggMod
	 * @param lFasGPMod
	 * @param lSentenzaMod
	 * @throws F3BException
	 */
	private void checkMinorenne(SoggettoModel soggMod, FascicoloGPModel lFasGPMod, SentenzaModel lSentenzaMod)
			throws F3BException {

		// data di sistema
		Date dataSistema = DateUtils.getSysDate();
		// data di nascita
		Date dataNascitaSoggetto = elaboraDataNascitaSoggetto(soggMod);
		// data del reato
		Date dataReato = soggMod.getDataReatoSius();

		boolean anni_18_Maggiorenne = false;
		if (dataNascitaSoggetto != null) {
			// calcolo gli anni del soggetto
			int anniSoggetto = deltaAnni(dataNascitaSoggetto, dataSistema);
			int anniReato = deltaAnni(dataNascitaSoggetto, dataReato);
			impostaEtichetta(anniSoggetto, anniReato, lFasGPMod, anni_18_Maggiorenne, lSentenzaMod, "");
		} else if (soggMod.getEtaPresuntaAnni() != null && dataReato != null) {
			// calcolo gli anni presunti del soggetto
			Date dataNascitaPresunta = DateUtils.moveDateTo(dataReato, Calendar.YEAR,
					-soggMod.getEtaPresuntaAnni().intValue());
			if (soggMod.getEtaPresuntaMesi() != null) {
				dataNascitaPresunta = DateUtils.moveDateTo(dataNascitaPresunta, Calendar.MONTH,
						-soggMod.getEtaPresuntaMesi().intValue());
			}
			int anniPresunti = deltaAnni(dataNascitaPresunta, dataSistema);
			int anniPresuntiReato = deltaAnni(dataNascitaPresunta, dataReato);

			// quando il calcolo degli anni presunti restituisce 18, bisogna
			// verificare se il giorno della data di sistema è maggiore
			// della data ultimo reato, in questo caso il soggetto è maggiorenne
			if (anniPresunti == 18) {
				int lGiornoDataSistema = Integer.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
				int lGiornoDataReato = Integer.parseInt(DateUtils.getDateToString(dataReato, "dd"));
				if (lGiornoDataSistema > lGiornoDataReato) {
					anni_18_Maggiorenne = true;
				} else {
					anni_18_Maggiorenne = false;
				}
			}
			impostaEtichetta(anniPresunti, anniPresuntiReato, lFasGPMod, anni_18_Maggiorenne, lSentenzaMod,
					"");
		} else if (soggMod.getEtaPresuntaAnni() != null && dataReato == null) {
			// caso in cui il soggetto ha età presunta ma non c'è data commesso reato,
			// in questo caso bisogna recuperare le informazioni del Fasicolo Siep associato,
			// per impostare l'etichetta Maggiorenne/Minorenne
			if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null) {
				BigDecimal idFascicoloSiep = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
				String codTipoUfficioSiep = "";
				if (Utils.isPresent(idFascicoloSiep)) {
					IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
					DettaglioFascicoloModel lDettaglio = lCtrl.ExDettaglioFascicoloSiep(idFascicoloSiep);
					FascicoloSiepModel lFasMod = lDettaglio.getFascicoloSiep();
					codTipoUfficioSiep = lFasMod.getCodTipoUfficio();

					// Reati (SIEP)
					IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
					Vector lReati = lReaCtrl.ExRicercaReatoCircostanzaByFascicolo(idFascicoloSiep);

					// data del primo reato e ultimo legati al Fascicolo Siep
					Date dataPrimoReato = elaboraDataPrimoReato(lReati);
					Date dataUltimoReato = elaboraDataUltimoReato(lReati);

					if (dataPrimoReato != null) {
						// calcolo gli anni presunti del soggetto
						Date dataNascitaPresunta = DateUtils.moveDateTo(dataPrimoReato, Calendar.YEAR,
								-soggMod.getEtaPresuntaAnni().intValue());
						if (soggMod.getEtaPresuntaMesi() != null)
							dataNascitaPresunta = DateUtils.moveDateTo(dataNascitaPresunta, Calendar.MONTH,
									-soggMod.getEtaPresuntaMesi().intValue());
						int anniPresunti = deltaAnni(dataNascitaPresunta, dataSistema);
						int anniPresuntiUltimoReato = deltaAnni(dataNascitaPresunta, dataUltimoReato);

						// quando il calcolo degli anni presunti restituisce 18, bisogna
						// verificare se il giorno della data di sistema è maggiore
						// della data ultimo reato, in questo caso il soggetto è maggiorenne
						if (anniPresunti == 18) {
							int lGiornoDataSistema = Integer
									.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
							int lGiornoDataUltimoReato = Integer
									.parseInt(DateUtils.getDateToString(dataUltimoReato, "dd"));
							if (lGiornoDataSistema > lGiornoDataUltimoReato) {
								anni_18_Maggiorenne = true;
							} else {
								anni_18_Maggiorenne = false;
							}
						}
						impostaEtichetta(anniPresunti, anniPresuntiUltimoReato, lFasGPMod,
								anni_18_Maggiorenne, lSentenzaMod, codTipoUfficioSiep);
					}
				}
			}
		}
	}

	/**
	 * MEV10-s3: aggiunto metodo di elaborazione età soggetto
	 *
	 * @param soggMod
	 * @return
	 */
	private Date elaboraDataNascitaSoggetto(SoggettoModel soggMod) {

		Date ret = null;
		if (soggMod.getDataNascita() != null) {
			ret = soggMod.getDataNascita();
		} else if (soggMod.getMeseNascita() != null && soggMod.getAnnoNascita() != null) {
			ret = DateUtils.getDate(soggMod.getAnnoNascita().intValue(), soggMod.getMeseNascita().intValue(),
					1);
		} else if (soggMod.getAnnoNascita() != null) {
			ret = DateUtils.getDate(soggMod.getAnnoNascita().intValue(), 1, 1);
		}
		return ret;
	}

	/**
	 * MEV10-s3: aggiunto metodo di elaborazione età soggetto
	 *
	 * @param dataStart
	 * @param dataEnd
	 * @return
	 */
	private int deltaAnni(Date dataStart, Date dataEnd) {

		int anni = 0;
		if (dataStart != null && dataEnd != null) {
			long lStart = dataStart.getTime();
			long lEnd = dataEnd.getTime();
			long delta = lEnd - lStart;
			long days = Math.round((delta / (1000 * 60 * 60 * 24)));
			anni = Math.round(days / 365);
		}
		// valore di ritorno
		return anni;
	}

	/**
	 * MEV10-s3: aggiunto metodo di impostazione label nella jsp
	 *
	 * @param anni
	 * @param anniUltimoReato
	 * @param lFasGPMod
	 * @param anni_18_Maggiorenne
	 * @param lSentenzaMod
	 * @param codTipoUfficioSiep
	 * @throws F3BException
	 */
	private void impostaEtichetta(int anni, int anniUltimoReato, FascicoloGPModel lFasGPMod,
			boolean anni_18_Maggiorenne, SentenzaModel lSentenzaMod, String codTipoUfficioSiep)
			throws F3BException {

		String codUfficioUtente = "";
		if (getUtenteConnesso().getUfficioUtente() != null
				&& getUtenteConnesso().getUfficioUtente().getCodUfficio() != null)
			codUfficioUtente = getUtenteConnesso().getUfficioUtente().getCodUfficio();

		String etichettaEta = "";
		String oscuraEta = "";

		String codTipoUfficio = lFasGPMod.getFascicoloSiusModel().getCodTipoUfficio();
		if (Utils.isPresent(codTipoUfficioSiep))
			codTipoUfficio = codTipoUfficioSiep;

		// a) il soggetto iscritto dai seguenti uffici : PM-GIP-DIB-TDS-UDS-CAP
		// è sempre 'MAGGIORENNE';
		if (Utils.isPresent(codTipoUfficio) && ("PM".equals(codTipoUfficio) || "GIP".equals(codTipoUfficio)
				|| "DIB".equals(codTipoUfficio) || "TDS".equals(codTipoUfficio)
				|| "UDS".equals(codTipoUfficio) || "CAP".equals(codTipoUfficio))) {
			// etichetta non visibile
			etichettaEta = "";
			// rimozione etichetta non visibile
			oscuraEta = "NO";
		}
		// b) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla
		// data di sistema ha meno di 18 anni (per precisione, meno di 18 anni ed un giorno);
		else if (codTipoUfficio != null && !"".equals(codTipoUfficio)
				&& ("PMM".equals(codTipoUfficio) || "DIBM".equals(codTipoUfficio)
						|| "GIPM".equals(codTipoUfficio) || "CAPSM".equals(codTipoUfficio)
						// 20170802: aggiunto = ai 18
						|| "UDSM".equals(codTipoUfficio) || "TDSM".equals(codTipoUfficio))
				&& anni <= 18 && !anni_18_Maggiorenne) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#FF0040'>&nbsp;Minorenne (Anni "
					+ anni + ")&nbsp;</span>";
			oscuraEta = "NO";
		}
		// c) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la
		// 'Età presunta' alla data di sistema ha più di 18 anni ma meno di 26 anni
		// ed avente campo VISIBILITA_EX_MINORENNE = 'N';
		else if (codTipoUfficio != null && !"".equals(codTipoUfficio)
				&& ("PMM".equals(codTipoUfficio) || "DIBM".equals(codTipoUfficio)
						|| "GIPM".equals(codTipoUfficio) || "CAPSM".equals(codTipoUfficio)
						|| "UDSM".equals(codTipoUfficio) || "TDSM".equals(codTipoUfficio))
				&& anni <= 24 && lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne() != null
				&& "N".equals(lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne())) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			// rimozione etichetta non visibile
			oscuraEta = "NO";
		}
		// d) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla data di
		// sistema ha più di 18 anni ma meno di 26 anni ed avente campo VISIBILITA_EX_MINORENNE = '';
		else if (codTipoUfficio != null && !"".equals(codTipoUfficio)
				&& ("PMM".equals(codTipoUfficio) || "DIBM".equals(codTipoUfficio)
						|| "GIPM".equals(codTipoUfficio) || "CAPSM".equals(codTipoUfficio)
						|| "UDSM".equals(codTipoUfficio) || "TDSM".equals(codTipoUfficio))
				&& anni <= 24 && (lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne() == null
						|| "".equals(lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne()))) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			if (lFasGPMod.getFascicoloSiusModel().getChiaveUfficio().equals(codUfficioUtente)) {
				oscuraEta = "SI";
			} else {
				oscuraEta = "NO";
			}
		}
		// e) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo
		// la 'Età presunta' alla data di sistema ha più di 25 anni;
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (codTipoUfficio != null && !"".equals(codTipoUfficio)
				&& ("PMM".equals(codTipoUfficio) || "DIBM".equals(codTipoUfficio)
						|| "GIPM".equals(codTipoUfficio) || "CAPSM".equals(codTipoUfficio)
						|| "UDSM".equals(codTipoUfficio) || "TDSM".equals(codTipoUfficio))
				&& anni > 24) {
			// etichetta non visibile
			etichettaEta = "";
			// rimozione etichetta non visibile
			oscuraEta = "NO";
		}
		// f) il soggetto iscritto dalla Procura Generale presso la Corte di Appello è sempre 'MAGGIORENNE';
		// (CONDIZIONE INSERITA NEL PUNTO a)
		// g) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla
		// data di sistema ha meno di 18 anni (per precisione, meno di 18 anni ed un giorno);
		else if (codTipoUfficio != null && !"".equals(codTipoUfficio) && "PGCAP".equals(codTipoUfficio)
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null
						&& ("CAPSM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())
								|| "DIBM".equals(lSentenzaMod.getCodTipoAutoritaEmittente()) || "GIPM"
										// 20170802: aggiunto = ai 18
										.equals(lSentenzaMod.getCodTipoAutoritaEmittente())))
				&& anni <= 18 && !anni_18_Maggiorenne) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#FF0040'>&nbsp;Minorenne (Anni "
					+ anni + ")&nbsp;</span>";
			oscuraEta = "NO";
		}
		// h) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo
		// la 'Età presunta' alla data di sistema ha più di 18 anni ma meno di 26 anni
		// ed avente campo VISIBILITA_EX_MINORENNE = 'N';
		else if (codTipoUfficio != null && !"".equals(codTipoUfficio) && "PGCAP".equals(codTipoUfficio)
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null
						&& ("CAPSM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())
								|| "DIBM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())
								|| "GIPM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())))
				&& anni <= 24 && lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne() != null
				&& "N".equals(lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne())) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			oscuraEta = "NO";
		}
		// i) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta'
		// alla data di sistema ha più di 18 anni ma meno di 26 anni ed avente
		// campo VISIBILITA_EX_MINORENNE = '';
		else if (codTipoUfficio != null && !"".equals(codTipoUfficio) && "PGCAP".equals(codTipoUfficio)
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null
						&& ("CAPSM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())
								|| "DIBM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())
								|| "GIPM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())))
				&& anni <= 24 && (lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne() == null
						|| "".equals(lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne()))) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			if (lFasGPMod.getFascicoloSiusModel().getChiaveUfficio().equals(codUfficioUtente)) {
				oscuraEta = "SI";
			} else {
				oscuraEta = "NO";
			}
		}
		// j) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure
		// secondo la 'Età presunta' alla data di sistema ha più di 25 anni.
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (codTipoUfficio != null && !"".equals(codTipoUfficio) && "PGCAP".equals(codTipoUfficio)
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null
						&& ("CAPSM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())
								|| "DIBM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())
								|| "GIPM".equals(lSentenzaMod.getCodTipoAutoritaEmittente())))
				&& anni > 24) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			oscuraEta = "NO";
		}
		setRequestAttribute("etichettaEta", etichettaEta);
		setRequestAttribute("oscuraEta", oscuraEta);
	}

	private Date elaboraDataPrimoReato(Vector reatiVect) {

		// data del primo reato
		Date dataPrimoReato = null;

		Iterator itx = reatiVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = null;
			Object lObj = itx.next();
			if (lObj instanceof ReatoModel) {
				lReato = (ReatoModel) lObj;
			} else if (lObj instanceof ReatoCircostanzaModel) {
				ReatoCircostanzaModel lReatoCirc = (ReatoCircostanzaModel) lObj;
				lReato = lReatoCirc.getReato();
			}

			Date dataReato = elaboraDataReato(lReato);
			if (dataReato != null) {
				if (dataPrimoReato == null) {
					dataPrimoReato = dataReato;
				} else if (dataPrimoReato.compareTo(dataReato) > 0) {
					dataPrimoReato = dataReato;
				}
			}
		}

		return dataPrimoReato;
	}

	private Date elaboraDataReato(ReatoModel lReato) {

		Date ret = null;
		if (lReato.getDataInizio() != null) {
			ret = lReato.getDataInizio();
		} else if (lReato.getMeseInizio() != null && lReato.getAnnoInizio() != null) {
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), lReato.getMeseInizio().intValue(), 1);
		} else if (lReato.getAnnoInizio() != null) {
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), 1, 1);
		}
		return ret;
	}

	private Date elaboraDataUltimoReato(Vector reatiVect) {

		// data del primo reato
		Date dataUltimoReato = null;

		Iterator itx = reatiVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = null;
			Object lObj = itx.next();
			if (lObj instanceof ReatoModel) {
				lReato = (ReatoModel) lObj;
			} else if (lObj instanceof ReatoCircostanzaModel) {
				ReatoCircostanzaModel lReatoCirc = (ReatoCircostanzaModel) lObj;
				lReato = lReatoCirc.getReato();
			}

			Date dataReato = elaboraDataReato(lReato);
			if (dataReato != null) {
				if (dataUltimoReato == null) {
					dataUltimoReato = dataReato;
				} else if (dataUltimoReato.compareTo(dataReato) < 0) {
					dataUltimoReato = dataReato;
				}
			}
		}

		return dataUltimoReato;
	}

}