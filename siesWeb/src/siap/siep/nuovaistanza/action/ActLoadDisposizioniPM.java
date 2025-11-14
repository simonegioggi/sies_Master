package siap.siep.nuovaistanza.action;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
// import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.EventoController;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.NuovaIstanzaController;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadDisposizioniPM extends ActionSiap implements ICostantiNuovaIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		/* Controllo se un fascicolo e' presente in sessione altrimenti lo faccio selezionare */
		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.nuovaistanza.action.ActLoadDisposizioniPM";

			return lPage;
		}
		isFascicoloSiepDiCompetenza();

		// ricerca Istanza per il fascicolo in sessione
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		NuovaIstanzaController lIstController = new NuovaIstanzaController();
		EventoController lEveController = new EventoController();

		// Per visualizzare l'elenco disposizioni in caso di registro istanze oppure
		// direttamente solo l'istanza
		Collection<NuovaIstanzaModel> lVect1 = new Vector<>();
		Vector lVectEventiInoltro = null;
		EventoModel evemod = new EventoModel();
		// 20251113 [SG]: aggiunto controllo
		if (isRequestParameterNullEmptyObj(ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA) || Utils
				.isNullObj(getRequestBigDecimalParameter(ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA))) {
			lVect1 = lIstController.ExRicercaNuovaIstanzaByIdFascicolo(lFascMod.getIdFascicoloSiep());
			evemod.setCodMotivo("1002");
			evemod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			try {
				lVectEventiInoltro = lEveController.ExRicercaEvento(evemod);
			} catch (F3BException fe) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(fe);
			}
		} else {
			java.math.BigDecimal idista = getRequestBigDecimalParameter(
					ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA);
			NuovaIstanzaModel lModel = lIstController.ExRicercaNuovaIstanzaById(idista);
			lVect1.add(lModel);
			evemod.setCodMotivo("1002");
			evemod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			evemod.setEveIdEvento(lModel.getEveIdEvento());
			try {
				lVectEventiInoltro = lEveController.ExRicercaEvento(evemod);
			} catch (F3BException fe) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(fe);
			}
		}
		this.setRequestAttribute("listaeventi", lVectEventiInoltro);
		//// FINE

		Collection<NuovaIstanzaModel> lVect = new Vector<>();
		if (lVect1 != null && lVect1.size() > 0) {
			java.util.Iterator<NuovaIstanzaModel> lItx = lVect1.iterator();
			while (lItx.hasNext()) {
				NuovaIstanzaModel lModel = lItx.next();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("lModel.getDataInoltroPM()=" + lModel.getDataInoltroPM());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("lModel.getCodStatoIstanza()=" + lModel.getCodStatoIstanza());
				String IsInoltroValidato = lIstController
						.ExRicercaFlagValNuovaIstanza(lModel.getEveIdEvento(), "I");

				if (lModel.getDataInoltroPM() != null && IsInoltroValidato.charAt(0) == 'S') {
					String IsDispoValidata = lIstController
							.ExRicercaFlagValNuovaIstanza(lModel.getEveIdEvento(), "D");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("IsDispoValidato=" + IsDispoValidata + "<<<");
					// la stringa IsDispoValidata è nel formato "c*id_evento" dove c=flag di validato

					if (!IsDispoValidata.equals("")) {
						lModel.setCodEsito(IsDispoValidata.substring(IsDispoValidata.indexOf('*') + 1));
						if (IsDispoValidata.charAt(0) == 'N') {
							// Appoggio i due dati nel model solo per poterli gestire nella jsp
							// ma NON perchè devo cambiarli!!
							lModel.setDescrStatoIstanza("Disposizione da Validare");
						}
						if (IsDispoValidata.charAt(0) == 'A') {
							// L'evento trovato è una disposizione annullata pertanto
							// devo considerarlo come ancora non lavorato
							lModel.setDescrStatoIstanza("Da compilare e da Validare");
						}
					} else {
						lModel.setDescrStatoIstanza("Da compilare e da Validare");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Nell'else di IsDispoValidato, descr=="
								+ lModel.getDescrStatoIstanza() + "<<<");
					}

					// 14/03/2011 Lettura dell'Avvocato.
					if (lModel.getAvvIdAvvocato() != null) {
						AvvocatoModel lAvvMod = new AvvocatoModel();
						lAvvMod.setIdAvvocato(lModel.getAvvIdAvvocato());

						IAvvocato lCtrlAvv = SIEPLookupRemote.getAvvocatoRemote();
						AvvocatoModel lAvv = lCtrlAvv.ExRicercaAvvocatoByKey(lModel.getAvvIdAvvocato());
						lModel.setAvvocato(lAvv);
					}

					lVect.add(lModel);
				}
			}
		}

		if (lVect == null || (lVect != null && lVect.size() == 0)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Nessuna Istanza in attesa di disposizioni del PM. " + lFascMod.getChiaveAnno() + "/"
							+ lFascMod.getChiaveProgr() + "<br>Impossibile procedere.");

			/*
			 * setSessionAttribute("fascicolo",null); setSessionAttribute("soggetto",null);
			 * setSessionAttribute("sentenza",null);
			 * lRedirigi.setAction("siap.siep.nuovaistanza.action.ActLoadDisposizioniPM&" +
			 * ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			 */
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		this.setRequestAttribute("listaIstanze", lVect);

		// Posizione Giuridica
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltra = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
						lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPosLuoAltra);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = new Vector();
		try {
			lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvocati);
		} catch (Exception ex) {
			setRequestAttribute("avvocati", lAvvocati);
		}

		// Autorità esterna E
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita(), "35");
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// Autorità esterna avvocato
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		// Autorità esterna destinazione condannato
		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		// Inserire Eventuali ComboBOX
		Option lOptionOggetto = new Option(DecodificheManager.getInstance().getTipoContenutoIstanza());
		setRequestAttribute("oggettoIstanza", "" + lOptionOggetto);

		try {
			Option lOptionStato = new Option(DecodificheManager.getInstance().getStatoNuovaIstanza());
			lOptionStato.setFilter(new String[] { "-", "02", "03", "04", "05", "06", "07", "08" });
			setRequestAttribute("statoIstanza", "" + lOptionStato);
		} catch (Exception ex) {
			throw new F3BException("ActLoadDisposizioniPM: " + ex);
		}

		Option lOptionUffGE = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionUffGE.setFilter(new String[] { "-", "CAP", "CAS", "CASAP", "GIP", "GIPM", "GP", "GUP", "GUPM",
				"TRIBSD", "CAPSM", "DIB", "DIBM" }); // solo le Autorità Emittenti.
		setRequestAttribute("ufficioGE", "" + lOptionUffGE);

		Option lOptionUffSorv = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// MEV_66: aggiunto Tribunale per i minorenni in funzione di Tribunale di Sorveglianza
		lOptionUffSorv.setFilter(new String[] { "TDS", "-", "TMIDS", "TDSM" });
		setRequestAttribute("ufficioTdS", "" + lOptionUffSorv);

		Option lOptionUffPM = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
		setRequestAttribute("ufficioPM", "" + lOptionUffPM);

		// MEV_66: aggiunto Magistrato di sorveglianza per i minorenni
		// Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficio());
		Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM());
		lOptionUffSIUS.setFilter(new String[] { "UDS", "UDSM", "-" });
		setRequestAttribute("ufficioMdS", "" + lOptionUffSIUS);

		if (lVect.size() == 1) {
			java.util.Iterator<NuovaIstanzaModel> lItx = lVect.iterator();
			NuovaIstanzaModel lModel = lItx.next();
			if (!lModel.getDescrStatoIstanza().equals("Da compilare e da Validare")
					&& lVectEventiInoltro.size() == 0) {
				String lPage = "";
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza";
				lPage += "&" + "IdEvento" + "=" + lModel.getCodEsito();
				lPage += "&" + "TipoVis=Disposizione";
				return lPage;
			} else
				return IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadDisposizioniPM.jsp";
		} else
			return IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadDisposizioniPM.jsp";
	}

}