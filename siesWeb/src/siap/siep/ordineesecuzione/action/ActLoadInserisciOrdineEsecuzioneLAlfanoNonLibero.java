package siap.siep.ordineesecuzione.action;

import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
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
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 *
 * <p>
 * Title: ActLoadInserisciOrdineEsecuzioneLAlfanoNonLibero
 * </p>
 * <p>
 * Description: Classe d'inserimento dell'Ordine di Esecuzione Decreto Alfano (Pos Giu NonLibero 01, 02, 03)
 * </p>
 * <p>
 * </p>
 * <p>
 * Company:
 * </p>
 *
 */
public class ActLoadInserisciOrdineEsecuzioneLAlfanoNonLibero extends ActionSiap
		implements ICostantiOrdineEsecuzione {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " non è stato Validato. Impossibile inserire un ordine d'esecuzione con sospensione!");
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

		this.isEventoNonValidato();
		// Controllo Esistenza pena residua non validata per quel fascicolo

		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N")) {
			setRequestAttribute("dataeditabile", "S");
		}

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			/* Vector lAvvVect = */lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
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

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());
		String lCodPosGiu = "";

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

		lCodPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().trim();
		setRequestAttribute("posizioneluogoaltra", lPos);

		// 9/12/2010 Posizioni giuridiche di partenza trattate: 01, 02 o 03
		// A seguito di un Ordine di Esecuzione della legge Alfano 199/2010
		// alla validazione si hanno le seguenti transazioni di posizione:
		// 01 --> 03 (stato_procedimento -> 0412) con motivo provvedimento 0498 LED-DET
		// 02 --> 53 (stato_procedimento -> 0412) con motivo provvedimento 0365 LED-ARR
		// 03 --> 03 (se trattata) (stato_procedimento -> 0412) con motivo provvedimento 0498 LED-DET

		// MEV 10 S3
		// Verranno trattate anche le seguenti posizioni giuridiche di partenza: 70, 71 e 72
    // 70 --> 87				(stato_procedimento -> 0412)	con motivo provvedimento 5504
		// 71 --> 85 (stato_procedimento -> 0412) con motivo provvedimento 5527
		// 72 --> 86 (stato_procedimento -> 0412) con motivo provvedimento 5528

		if (lCodPosGiu.equals("01") // Custodia Cautelare per Questa Causa in Regime di Detenzione
				|| lCodPosGiu.equals("02") // Custodia Cautelare per Questa Causa in Regime di Arresti
											// Domiciliari
				// || lCodPosGiu.equals("03")) // Espiazione Pena in Regime Carcerario
				|| lCodPosGiu.equals("70") // Custodia Cautelare per Questa Causa in Regime di Arresti
											// Domiciliari ex art 89 dpr 309/90
				|| lCodPosGiu.equals("71") // Custodia Cautelare per Questa Causa in Regime di Permanenza in
											// Casa
				|| lCodPosGiu.equals("72") // Custodia Cautelare per Questa Causa Collocamento in Comunità
		) {
		} else {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Posizione Giuridica non gestita per il provvedimento selezionato");
		}

		// Imposta Tipo Istituto
		Option lOptionIstituto = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOptionIstituto);

		// Riempimento ComboBox
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// modifica relativa al tipo istituto
		if (lPos.getPosizioneGiuridica().isLibero() || lCodPosGiu.equals("02") || // In effetti unica
																					// possibilità, dopo il
																					// filtraggio precedente è
																					// "02"
				lCodPosGiu.equals("71") || lCodPosGiu.equals("04")) {
			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		} else {
			if (lPos.getLuogoDetenzione() != null
					&& lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
						lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
		}
		lOption.setSelected("-");
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "SSPA");
		setRequestAttribute("tipoUfficio", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "UDS");
		setRequestAttribute("tipoUfficioMagistrato", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "UDS");
		setRequestAttribute("tipoUfficioSIUS", "" + lOption);

		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("avvocati", lAvvocati);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("evento", lEve);

		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
		// lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyy"));
		setRequestAttribute("penaresidua", llPenMod);

		// Lista UFFICIO DI SORVEGLIANZA (-/UDS/UDSM)
		Option lOptionUffDest = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM(),
				"-");
		String[] lFiltro = new String[3];
		lFiltro[0] = "-";
		lFiltro[1] = "UDS";
		lFiltro[2] = "UDSM";
		lOptionUffDest.setFilter(lFiltro);
		setRequestAttribute("tipUffDestMagSor", "" + lOptionUffDest);

		// Istanza
		EventoModel lEveMod = new EventoModel();
		lEveMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveMod.setCodTipoEvento("03");
		lEveMod.setFlagDocumentoRegistrato("S");
		IEvento lEvCtrl = SICOLookupRemote.getEventoRemote();
		// Vector lVectEvento = null;
		try {
			/* lVectEvento = */lEvCtrl.ExRicercaEvento(lEveMod);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
		}

		// NuovaIstanza
		INuovaIstanza lNICtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
		Vector lVectNI = null;
		try {
			lVectNI = new Vector(lNICtrl.ExRicercaNuovaIstanzaByIdFascicolo(lFascMod.getIdFascicoloSiep()));
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
		}

		if ((lVectNI != null) && (lVectNI.size() > 0))
			setRequestAttribute("nuovaistanza", lVectNI.firstElement());

		return PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_ALFANO_NON_LIBERO; // restituisce la jsp di VIEW
	}

}