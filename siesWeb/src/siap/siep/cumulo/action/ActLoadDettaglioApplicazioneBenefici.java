package siap.siep.cumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.beneficio.model.BeneficioPenaAccessoriaModel;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.ulterioresanzionecumulo.controller.IUlterioreSanzioneCumulo;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioApplicazioneBenefici
 * </p>
 * <p>
 * Description: Carica la pagina di Dettaglio Applicazione Benefici
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadDettaglioApplicazioneBenefici extends ActionSiap implements ICostantiAnnotazioneManuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		try {
			String lPageErr = loadDettaglio("0122", AMINISTIA_INDULTO);

			if (lPageErr != null)
				return lPageErr;
		} catch (F3BException ex) {
			new SIEPException(SIEPException.EX_NOT_FOUND, "Nessun elememento trovato.");
		}

		if (!isRequestParameterNullObj("dataScarcerazione")) {
			String dataScarcerazioneStr = getRequestStringParameter("dataScarcerazione");
			Date dataScarcerazione = DateUtils.getDate(dataScarcerazioneStr, "dd/MM/yyyy");
			setRequestAttribute("dataScarcerazione", dataScarcerazione);
		}

		return PG_LOAD_DETTAGLIO_APPL_BENEFICI;
	}

	/**
	 * Load del Dettaglio Richiesta Applicazione Benefici
	 * 
	 * @param lMotivoProvvedimento
	 * @param lTipoAnnotazione
	 * @return
	 * @throws F3BException
	 */
	protected String loadDettaglio(String lMotivoProvvedimento, String lTipoAnnotazione) throws F3BException {

		// Controllo Presenza del Fascicolo in Sessione
		if (isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Posizione Giuridica
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);
		setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);

		// Annotazione Manuale
		// I dati vengono caricati nella sezione "Quantum Pena della Richiesta"
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
//		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
		AnnotazioneManualeModel lAnnManIns = null;
		Vector lListAnnMan = new Vector();

		if (!isRequestParameterNullObj(CAMPO_ID_ANNOTAZIONE_MANUALE)) { // Cerco per Id Annotazione Manuale
			BigDecimal lIdAnn = getRequestBigDecimalParameter(CAMPO_ID_ANNOTAZIONE_MANUALE);
			lAnnManIns = lCtrlAnnMan.ExRicercaAnnotazioneManualeByKey(lIdAnn);
			lListAnnMan.add(lAnnManIns);
		}

		setRequestAttribute("AnnotazioneManualeInserita", lAnnManIns);

		// Identificativo della Pena Residua
		BigDecimal lIdPenaResidua = null;
		if (!isRequestParameterNullObj("IdPenaResidua")) {
			lIdPenaResidua = getRequestBigDecimalParameter("IdPenaResidua");
		}

		// Recupero l'ultima Pena Residua che viene visualizzata nella
		// sezione "Pena Principale"
		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenRes = new PenaResiduaModel();
		if (lIdPenaResidua != null) {
			lUltimaPenRes = IPenRes.ExRicercaPenaResiduaByKey(lIdPenaResidua);
		} else {
			lUltimaPenRes = IPenRes.ExRicercaPenaResiduaUltimaByDate(lIdFascicolo);
		}

		setRequestAttribute("UltimaPenRes", lUltimaPenRes);

		if (lAnnManIns != null && lAnnManIns.getFlagBeneficioDetratto() != null
				&& lAnnManIns.getFlagBeneficioDetratto().equals("N")) {

			// Effettuo il ricalcolo della pena Residua
			PenaResiduaModel lPenaResiduaCorrente = null;

			try {
				lPenaResiduaCorrente = getPenaDaEspiare(lUltimaPenRes, lAnnManIns);

				// Aggiornamento Pena Residua
				IPenRes.ExAggiornaPenaResidua(lPenaResiduaCorrente);

			} catch (Exception e) {
				throw new F3BException(e);
			}

			setRequestAttribute("PenaResiduaCorrente", lPenaResiduaCorrente);

		} else {
			// Non effettuo il ricalcolo della pena perchè il Beneficio
			// è stato già detratto dal Quantum di Pena
			// In questo caso la Pena Residua coincide con la pena l'ultima
			// Pena Residua
			setRequestAttribute("PenaResiduaCorrente", lUltimaPenRes);
		}

		// Ulteriori Sanzioni Cumulo
		// Recupero i record CUMULO non ancora validati ordinati per data_inserimento
		// ascendente
		// Ricerca cumulo per FasSieIdFascicoloSiep
//		CumuloModel lCumMod = new CumuloModel();
		ICumulo iCum = SIEPLookupRemote.getCumuloRemote();
		/*Vector cumuli = */iCum.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lIdFascicolo);
//		if (cumuli.size() > 0) {
//			lCumMod = ((CumuloModel) (cumuli).get(0));
//		}

		IUlterioreSanzioneCumulo lUlt = SIEPLookupRemote.getUlterioreSanzioneCumuloRemote();
		// Vector lUltMod = lUlt.ExRicercaUlterioreSanzioneCumuloByFascicoloIDCumul0(lIdFascicolo,
		// lCumMod.getIdCumulo());
		Vector lUltMod = lUlt.ExRicercaUlterioreSanzioneCumuloByFascicoloCumulante(lFascMod);

		setRequestAttribute("ulterioriSanzione", lUltMod);

		// Lettura Pene Accessorie per selezionare la form pertinente.
		Vector lVect = new Vector();
		Vector lPenBenVec = new Vector();
		try {
			IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
			PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
			lPenMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel) getSessionAttribute("fascicolo"))
					.getIdFascicoloSiep());
			lVect = lCtrl.ExRicercaPenaAccessoria(lPenMod);

			BeneficioPenaAccessoriaModel lBenPenMod = null;
			if (lVect != null) {
				Iterator itx = lVect.iterator();
				while (itx.hasNext()) {
					lBenPenMod = new BeneficioPenaAccessoriaModel();
					PenaAccessoriaModel lPenModel = (PenaAccessoriaModel) itx.next();
					lBenPenMod.setPenaAccessoria(lPenModel);

					if (lPenModel != null && lPenModel.getBenIdBeneficio() != null) {
						IBeneficio lCtrlBen = SIEPLookupRemote.getBeneficioRemote();
						BeneficioModel lBenMod = lCtrlBen.ExRicercaBeneficioByKey(lPenModel
								.getBenIdBeneficio());
						lBenPenMod.setBeneficio(lBenMod);
					}
					lPenBenVec.add(lBenPenMod);
				}
			}

		} catch (F3BException e) {
			// Nessuna Pena Accessoria collegata al Fascicolo Cumulante
			// Viene visualizzato il link "Inserimento Pene Accessorie"
		}
		setRequestAttribute("peneaccessorie", lVect);
		setRequestAttribute("beneficiopenaaccessoria", lPenBenVec);

		// Lettura degli eventi di Pena Accessoria del fascicolo.
		Vector lEventi = new Vector();
		try {
			IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
			PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
			lPenMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel) getSessionAttribute("fascicolo"))
					.getIdFascicoloSiep());
			lVect = lCtrl.ExRicercaPenaAccessoria(lPenMod);

			// Si Invoca il controller per gli eventi di Esecuzione Pena Accessoria del fascicolo.
			IEvento lCtrl2 = SICOLookupRemote.getEventoRemote();
			String[] lTipoEvento = { "16", "17", "18" };
			lEventi = lCtrl2.ExRicercaEventoNotificaByFascicoloSiep(lPenMod.getFasSieIdFascicoloSiep(),
					lTipoEvento);
		} catch (F3BException e) {
			// setRequestAttribute("modalita", "P");
			// setRequestAttribute("eveCorrelati", lEventi);
			// return siap.siep.cumulo.action.ICostantiCumulo.PG_LOAD_DETTAGLIO_APPLICAZIONE_BENEFICI;
		}
		setRequestAttribute("eveCorrelati", lEventi);

		// Lettura Misure di Sicurezza.
		List lListMis = new ArrayList();

		try {
			IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
			PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
			lPenMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel) getSessionAttribute("fascicolo"))
					.getIdFascicoloSiep());
			lListMis = lCtrl.ExRicercaMisuraSicurezzaByIdFascicolo(lIdFascicolo);
		} catch (F3BException e) {
			// Nessuna Misura di Sicurezza collegata al Fascicolo Cumulante
		}
		setRequestAttribute("misuresicurezza", lListMis);
		return null;

	}

	public PenaResiduaModel getPenaDaEspiare(PenaResiduaModel lUltimaPenRes,
			AnnotazioneManualeModel lAnnManIns) throws Exception {

		PenaResiduaModel lPenaDaEspiare = new PenaResiduaModel(lUltimaPenRes);

		if (lUltimaPenRes.getDataInizio() != null) {
			Date lDataFinePena = DateUtils.getDate(9999, 12, 31);
			lPenaDaEspiare.setDataInizio(lUltimaPenRes.getDataInizio());
			lPenaDaEspiare.setDataFine(lDataFinePena); // 31/12/9999
		}

		CalendarUtil lCalUtil = new CalendarUtil();

		// Dati Pena Residua
		CalendarModel lCalReclusioneTotMod = new CalendarModel();
		CalendarModel lCalArrestiTotMod = new CalendarModel();

		// Recupero la Reclusione della Pena Residua
		lCalReclusioneTotMod.setNumAnni(lUltimaPenRes.getNumAnniReclusione());
		lCalReclusioneTotMod.setNumMesi(lUltimaPenRes.getNumMesiReclusione());
		lCalReclusioneTotMod.setNumGiorni(lUltimaPenRes.getNumGiorniReclusione());
		if (lUltimaPenRes.getImportoMulta() != null) {
			lCalReclusioneTotMod.setImportoMulta(lUltimaPenRes.getImportoMulta().doubleValue());
		}

		// Recupero l'Arresto della Pena Residua
		lCalArrestiTotMod.setNumAnni(lUltimaPenRes.getNumAnniArresto());
		lCalArrestiTotMod.setNumMesi(lUltimaPenRes.getNumMesiArresto());
		lCalArrestiTotMod.setNumGiorni(lUltimaPenRes.getNumGiorniArresto());
		if (lUltimaPenRes.getImportoAmmenda() != null)
			lCalArrestiTotMod.setImportoAmmenda(lUltimaPenRes.getImportoAmmenda().doubleValue());

		// Dati Annotazione Manuale della Richiesta Applicazione Benefici
		CalendarModel lCalReclusioneTotBen = new CalendarModel();
		CalendarModel lCalArrestiTotBen = new CalendarModel();

		// Recupero la Reclusione della Richiesta Applicazione Benefici
		lCalReclusioneTotBen.setNumAnni(lAnnManIns.getNumAnniReclusione());
		lCalReclusioneTotBen.setNumMesi(lAnnManIns.getNumMesiReclusione());
		lCalReclusioneTotBen.setNumGiorni(lAnnManIns.getNumGiorniReclusione());
		if (lAnnManIns.getImportoMulta() != null) {
			lCalReclusioneTotBen.setImportoMulta(lAnnManIns.getImportoMulta().doubleValue());
		}

		// Recupero l'Arresto della Richiesta Applicazione Benefici
		lCalArrestiTotBen.setNumAnni(lAnnManIns.getNumAnniArresto());
		lCalArrestiTotBen.setNumMesi(lAnnManIns.getNumMesiArresto());
		lCalArrestiTotBen.setNumGiorni(lAnnManIns.getNumGiorniArresto());
		if (lAnnManIns.getImportoAmmenda() != null)
			lCalArrestiTotBen.setImportoAmmenda(lAnnManIns.getImportoAmmenda().doubleValue());

		if (lAnnManIns.getFlagPiuMeno() != null && lAnnManIns.getFlagPiuMeno().equals("-")) {
			// Sottraggo i Quantum di Pena della richiesta
			lCalReclusioneTotMod = lCalUtil
					.sottraiGiorniValuteNew(lCalReclusioneTotMod, lCalReclusioneTotBen);
			lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod, lCalArrestiTotBen);
		} else {
			// Sommo i Quantum di Pena della richiesta
			lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod, lCalReclusioneTotBen);
			lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod, lCalArrestiTotBen);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Reclusione: " + lCalReclusioneTotMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Arresti   : " + lCalArrestiTotMod);

		// ==========================================================================
		// Verifico se i quantum di Reclusione sono diventati Negativi, li azzero.
		// Verifico se l'importo della Multa è negativo azzero l'importo.
		// ==========================================================================
		if (!lCalUtil.isPositiveTime(lCalReclusioneTotMod)) {
			// Quantum di Reclusione negativi
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Attenzione Quantum di Reclusione Negativi: " + lCalReclusioneTotMod);

			// Azzero i quantum di reclusione
			lCalReclusioneTotMod.setNumAnni(0);
			lCalReclusioneTotMod.setNumMesi(0);
			lCalReclusioneTotMod.setNumGiorni(0);
		}

		if (lCalReclusioneTotMod.getImportoMulta() < 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ImportoMulta negativo: " + lCalReclusioneTotMod.getImportoMulta());

			lCalReclusioneTotMod.setImportoMulta(0);
		}

		// ==========================================================================
		// Verifico se i quantum di Arresto sono diventati Negativi, li azzero.
		// Verifico se l'importo della Ammenda è negativo azzero l'importo.
		// ==========================================================================
		if (!lCalUtil.isPositiveTime(lCalArrestiTotMod)) {
			// Quantum di Reclusione negativi
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Attenzione Quantum di Arresto Negativi: " + lCalArrestiTotMod);

			// Azzero i quantum di reclusione
			lCalArrestiTotMod.setNumAnni(0);
			lCalArrestiTotMod.setNumMesi(0);
			lCalArrestiTotMod.setNumGiorni(0);
		}

		if (lCalArrestiTotMod.getImportoAmmenda() < 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ImportoAmmenda negativo: " + lCalArrestiTotMod.getImportoAmmenda());

			lCalArrestiTotMod.setImportoAmmenda(0);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Reclusione: " + lCalReclusioneTotMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Arresti   : " + lCalArrestiTotMod);

		// ==========================================================================
		// Carico il model di ritorno con quantum e importi calcolati
		// ==========================================================================
		lPenaDaEspiare.setQuantumReclusione(lCalReclusioneTotMod);
		lPenaDaEspiare.setImportoMulta(new BigDecimal(lCalReclusioneTotMod.getImportoMulta()));

		lPenaDaEspiare.setQuantumArresto(lCalArrestiTotMod);
		lPenaDaEspiare.setImportoAmmenda(new BigDecimal(lCalArrestiTotMod.getImportoAmmenda()));

		// ==========================================================================
		// Determino le date di espiazione a partire dai quantum
		// ==========================================================================
		if (lUltimaPenRes.getDataInizio() != null) {
			ICalcoloPena lCalPenCtrl = SIEPLookupRemote.getCalcoloPenaRemote();

			Vector lDateFine = lCalPenCtrl.exCalcolaDataFinePena(lUltimaPenRes.getDataInizio(),
					lPenaDaEspiare, true);

			lPenaDaEspiare.setDataInizio(lUltimaPenRes.getDataInizio());

			if (lDateFine.size() == 1) { // solo Reclusione o Arresti: ho quindi solo data fine
											// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
											// siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Ho solo una data: " + lDateFine.get(0));

				if (!lCalUtil.isZero(lPenaDaEspiare.getQuantumArresto())) {
					// Solo arresti
					lPenaDaEspiare.setDataInizioArresto(lUltimaPenRes.getDataInizio());
				}

				lPenaDaEspiare.setDataFine((Date) lDateFine.get(0));
				lPenaDaEspiare.setDataFinePresunta(lPenaDaEspiare.getDataFine());
			} else if (lDateFine.size() == 2) { // Sono presenti sia Reclusione che Arresti
				lPenaDaEspiare.setDataFineReclusione((Date) lDateFine.get(0));
				lPenaDaEspiare.setDataInizioArresto(DateUtils.getDayAfter(lPenaDaEspiare
						.getDataFineReclusione()));
				lPenaDaEspiare.setDataFine((Date) lDateFine.get(1));
				lPenaDaEspiare.setDataFinePresunta(lPenaDaEspiare.getDataFine());
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("Attenzione quantum negativi o nulli Impossibile determinare la data fine pena");
			}
		}

		lPenaDaEspiare.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lPenaDaEspiare.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lPenaDaEspiare.setDataAggiornamento(DateUtils.getSysDate());

		return lPenaDaEspiare;

	}

}