package siap.sige.udienza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.util.UfficioUtils;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.SIGEException;
import siap.sige.aula.action.ICostantiAula;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.camponota.controller.ICampoNota;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.magistrato.action.ICostantiMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.richiesta.model.RichiestaSigeModel;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSigeRuolo;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;

/**
 * <p>
 * Title: ActInserisciFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento della Fissazione Udienza.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciFissazioneUdienza extends ActRicercaFSigePuntuale implements ICostantiUdienzaSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private FascicoloSigeEstesoModel lFasEsteso;
	private FascicoloSigeModel lFasc;
	private EventoNotificaModel lEve;
	private UdienzaProcedimentoSigeModel lUdiProc;
	private UdienzaProcedimentoSigeModel lUdiProcVecchia;
	private ProvvedimentoSigeModel lProvModel;
	// private ProvvedimentoSigeEventoModel lProvEveModel;
	private Vector lTenori;
	private UdienzaSigeModel lUdienzaSige;
	private String modalita;

	private void aggiungiDati(UdienzaSigeModel lUdiMod) throws F3BException {

		lUdiMod.setOraInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_INIZIO));
		lUdiMod.setMinInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_INIZIO));
		lUdiMod.setOraFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_FINE));
		lUdiMod.setMinFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_FINE));
		BigDecimal sez = null;
		try {
			sez = getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA);
		} catch (Exception e) {
		}
		lUdiMod.setCodIdSezioneUdienza(sez);
		lUdiMod.setCodIdAulaUdienza(getRequestBigDecimalParameter(ICostantiAula.CAMPO_ID_AULA));
		lUdiMod.setLuogoUdienza(
				getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO));

		String tipoGiudizio = super.getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);

		// 20170914: [SG] gestione magistrato assegnatario
		FascicoloSigeEstesoModel fascicolo = super.getFascicoloSigeEstesoInSessione();
		if (lUdienzaSige != null && lUdienzaSige.getCodGiudice() != null) {
			lUdiMod.setCodMagistratoAss(lUdienzaSige.getCodGiudice());
			lUdiMod.setCodGiudice(lUdienzaSige.getCodGiudice());
		} else if (fascicolo != null && fascicolo.getMagAssegnatario() != null) {
			lUdiMod.setCodMagistratoAss(fascicolo.getMagAssegnatario().getMagCodMagistrato());
			lUdiMod.setCodGiudice(fascicolo.getMagAssegnatario().getMagCodMagistrato());
		} else
			throw new SIGEException(SIGEException.USER_MESSAGE, "Magistrato Assegnatario non presente!");

		BigDecimal idCollegio = super.getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO);

		if (tipoGiudizio.equalsIgnoreCase("C") && idCollegio == null) {
			CollegioModel collegio = insertCollegio();
			lUdiMod.setColIdCollegio(collegio.getIdCollegio());
			lUdiMod.setCollegio(collegio);
		}
	}

	private void valorizzaUdienza(BigDecimal lIdUdienzaSige, String lTipoGiudizio) throws F3BException {

		// Controllo congruenza tra i Tipi Rito.
		IUdienzaSige lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRemote();
		lUdienzaSige = lCtrlUdienza.ExRicercaUdienzaSigeById(lIdUdienzaSige);

		if (lUdienzaSige == null || lUdienzaSige.getIdUdienzaSige() == null) {
			throw new SIGEException(SIGEException.USER_MESSAGE, "Udienza non presente!");
		}

		if (lUdienzaSige.getColIdCollegio() != null && lTipoGiudizio.compareTo("M") == 0) {
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Udienza selezionata non conforme al Tipo Rito scelto!");
		}

		if (lUdienzaSige.getColIdCollegio() == null && lTipoGiudizio.compareTo("C") == 0) {
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Impostare il collegio relativo alla data udienza!");
		}

		// aggiunge dati mancanti sulla udienza sige
		aggiungiDati(lUdienzaSige);

	}

	private void caricaDati() throws F3BException {

		// modalita I-inserimento, R-rifissazione
		modalita = getParameter("modalita");

		// Codice Tipo Ufficio dell'utente connesso
		String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodiceUfficioPG = null;
		String lCodComune = getCodComuneUtenteConnesso();
		String lDescrComune = getUfficioUtenteConnesso().getDescrComune();
		String lSedi[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		String lDestinatari[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
		String lAvvocato[] = getRequestStringParameters(ICostantiUdienzaSige.CAMPO_COD_AVVOCATO);
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		// Mev 15: siccome la data emissione non è più obbligatoria, in alternativa per le notifiche se usa
		// la sysdate
		Date lDataInvioNotifiche = (lDataEmissione != null) ? lDataEmissione : DateUtils.getSysDate();
		String[] lNote = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_NOTE);
		String lTipoNotifica = getRequestStringParameter(CAMPO_TIPONOTIFICA);

		// Fascicolo Sige Esteso in sessione.
		lFasEsteso = getFascicoloSigeEstesoInSessione();

		// Gestione Accoglimento Opposizione:
		Boolean esisteImpugnazioniProvvedimento = checkImpugnazioniProvvedimento(lFasEsteso);

		// Se esisteImpugnazioniProvvedimento=true (CodStatoFascicolo=="07" && CodTenoreDecisione=="10") ==>
		// Opposizione accolta non devo bloccare ==> NO MESSAGGIO
		if (IsFascicoloSigeIscrittoCompetenza() == false && !esisteImpugnazioniProvvedimento) {
			throw new SIGEException(SIGEException.USER_MESSAGE, ICostantiFascicoloSige.MSG_NON_MODIFICABILE);
		}

		// Preleva l'id dell'Istituto Detenzione
		String lIstitutoDetenzione = null;
		String lSediSog = null;
		String lDestinatariSog = null;
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lIstitutoDetenzione = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		} else {
			lSediSog = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE);
			lDestinatariSog = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_IST_DETENZIONE);
		}

		// Fascicolo Sige Esteso in sessione.
		// lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		if (isSessionAttributeNullObj("tenori")) {
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti !");
		}

		// Si prelevano i tenori Sige in sessione.
		Vector lTenoriEstesi = (Vector) getSessionAttribute("tenori");

		if (lTenoriEstesi.size() == 0) {
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti !");
		}

		// Preleva dalla request la data udienza.
		// Date lDataUdienza =
		// getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA,CAMPO_MESE_DATA_UDIENZA,CAMPO_GIORNO_DATA_UDIENZA);

		// Preleva dalla request id UdienzaSige
		String lTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO)
				.trim();
		BigDecimal lIdUdienzaSige = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);
		Date lDataUdienza = getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA);

		// [EC] 20171019: recupero la sezione se è stata specificata
		BigDecimal sez = null;
		try {
			sez = getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA);
		} catch (Exception e) {
		}

		if (lIdUdienzaSige != null) {

			valorizzaUdienza(lIdUdienzaSige, lTipoGiudizio);

		} else if (lDataUdienza != null) {

			// qui dovrebbe entrare solo per MONOCRATICHE
			// [EC] 20171019: PRIMA DI INSERIRE L'UDIENZA, OCCORRE VERIFICARE CHE GIA' NE ESISTA QUALCUNA CON
			// LE STESSE CARATTERISTICHE(data, rito, sezione e magistrato)
			UdienzaSigeModel lUdiModRicerca = new UdienzaSigeModel();
			IUdienzaSige lCtrlUdi = SIGELookupRemote.getUdienzaSigeRemote();
			aggiungiDati(lUdiModRicerca);
			lUdiModRicerca
					.setDataUdienza(getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
							ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA,
							ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA));
			Vector lVect = lCtrlUdi.ExRicercaUdienzaSige(lUdiModRicerca);

			if (lVect.size() == 0) {
				// 20171020: [EC] inserisco direttamento l'udienza solo per uffici senza sezioni
				boolean isUfficioConSezioni = UfficioUtils.isUfficioConSezioni(getCodUfficioUtenteConnesso());
				if (!isUfficioConSezioni) {
					lUdienzaSige = insertUdienzaSIGE();
					lIdUdienzaSige = lUdienzaSige.getIdUdienzaSige();
				} else
					throw new SIGEException(SIGEException.USER_MESSAGE,
							"Non sono state trovate udienze con i dati immessi! Utilizzare l' apposita funzione (Inserimento Udienza - Visualizza)");
			}
			// [EC] 20171019: PER SIZE > 0 PRENDO L'ULTIMA CREATA IN QUANTO LE UDIENZE SONO RESTITUITE
			// ORDINATE PERA DATA INSERIMENTO
			else if (lVect.size() > 0) {
				// devo recuperare l'udienza ritrovata
				lUdienzaSige = (UdienzaSigeModel) lVect.get(0);
			}
			// else{
			// throw new SIGEException(SIGEException.USER_MESSAGE,
			// "Sono state trovate più udienze con i dati immessi! Utilizzare la funzione
			// Inserimento-Visualizza Udienza");
			// }

			// lUdienzaSige = insertUdienzaSIGE();
			// lIdUdienzaSige = lUdienzaSige.getIdUdienzaSige();

			// fine intervento [EC] 20171019:

			MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
			if (lMagAss != null && lMagAss.getMagCodMagistrato() != null) {

				// ==========================================
				// Verifica se è presente un udienza
				// ==========================================
				IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
				// 20171004: [SG] le ricerche sono fatte sotto
				Vector vIdUdienzaSige = null;
				if ("C".equals(lTipoGiudizio)) {
					vIdUdienzaSige = lCtrl.ExRicercaUdienzaCollegialeSige(lMagAss.getMagCodMagistrato(),
							DateUtils.getDateToString(lDataUdienza, "dd/MM/yyyy"), lCodiceUfficio);
				} else if ("M".equals(lTipoGiudizio)) {
					vIdUdienzaSige = lCtrl.ExRicercaUdienzaMonocraticaSige(lMagAss.getMagCodMagistrato(),
							DateUtils.getDateToString(lDataUdienza, "dd/MM/yyyy"), sez, lCodiceUfficio);
				}
				if (vIdUdienzaSige.size() > 0) {
					lIdUdienzaSige = (BigDecimal) vIdUdienzaSige.get(0);
					valorizzaUdienza(lIdUdienzaSige, lTipoGiudizio);
					setRequestAttribute(CAMPO_NUMERO_UDIENZE_MAGRISTRATO, "" + vIdUdienzaSige.size());
				}

			}
		}

		// Si istanzia un model UdienzaProcedimentoModel.
		lUdiProc = new UdienzaProcedimentoSigeModel();
		lUdiProc.setUdiIdUdienzaSige(lIdUdienzaSige);
		lUdiProc.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		lUdiProc.setCodOperatoreInserimento(lCodiceOperatore);
		lUdiProc.setCodUfficioInserimento(lCodiceUfficio);
		lUdiProc.setDataInserimento(DateUtils.getSysDate());
		lUdiProc.setFlagRinviata(ICostantiUdienzaProcedimentoSige.UDIENZA_FISSATA);

		// Valorizzazione dell'eventuale UdienzaProcedimento da aggiornare
		lUdiProcVecchia = null;
		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE)) {
			BigDecimal lIdUdiProOld = getRequestBigDecimalParameter(
					ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);
			if (lIdUdiProOld != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("UDIENZA_PROCEDIMENTO_SIGE da aggiornare: " + lIdUdiProOld.toString());

				IUdienzaProcedimentoSige lUdiProCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
				lUdiProcVecchia = lUdiProCtrl.ExRicercaUdienzaProcedimentoSigeByKey(lIdUdiProOld);

				// lUdiProcVecchia = new UdienzaProcedimentoSigeModel();
				// lUdiProcVecchia.setIdUdienzaProcedimentoSige(lIdUdiProOld);
				lUdiProcVecchia.setCodOperatoreAggiornamento(lCodiceOperatore);
				lUdiProcVecchia.setCodUfficioAggiornamento(lCodiceUfficio);
				lUdiProcVecchia.setDataAggiornamento(DateUtils.getSysDate());
				// lUdiProcVecchia.setFlagRinviata(ICostantiUdienzaProcedimentoSige.UDIENZA_MODIFICATA);
			}
		}

		// ID Udienza
		// Inserisci Udienza_Procedimento con l'id del FasciolcoSIGE in sessione.

		// Update della data_CAMERA_CONSIGLIO in generale_proceidmento

		// Update della DATA_UDIENZA del Fascicolo Sige.
		lFasc = lFasEsteso.getFascicoloSige();
		lFasc.setCodTipoGiudizio(lTipoGiudizio);
		lFasc.setCodUfficioAggiornamento(lCodiceUfficio);
		lFasc.setCodOperatoreAggiornamento(lCodiceOperatore);
		lFasc.setDataAggiornamento(DateUtils.getSysDate());

		// Prepara il model EventoNotifica.
		lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("01"); // Tipo Evento = Provvedimento
		lEve.getEvento().setCodTipoProvvedimento("02"); // Tipo Provvedimento = Decreto
		// lEve.getEvento().setCodMotivo("0601"); // Fissazione Udienza
		lEve.getEvento().setCodEsito("0601");
		lEve.setNomeTemplate("FU1");

		// lEve.getEvento().setFasSiuIdFascicoloSius( lIdFasSius );
		lEve.getEvento().setDataEmissione(lDataEmissione);// lDataInvioNotifiche
		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setCodLuogoEmittente(lCodComune);
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		// Preleva le note dalla form
		String lCampiNoteReq = getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_AGGIUNTIVO);

		if (lCampiNoteReq != null && lCampiNoteReq.length() > 0) {
			CampoNotaModel[] lCampiNote = new CampoNotaModel[1];
			lCampiNote[0] = new CampoNotaModel();
			lCampiNote[0].setDescr(lCampiNoteReq);
			lCampiNote[0].setCodOperatoreInserimento(lCodiceOperatore);
			lCampiNote[0].setCodUfficioInserimento(lCodiceUfficio);
			lCampiNote[0].setDataInserimento(DateUtils.getSysDate());
			lEve.setCampoNote(lCampiNote);
		}

		// Vector per le notifiche.
		Vector<NotificaModel> lNotifiche = new Vector<>();

		String[] destinatarioflagSNT = getParameterValues("flagSNT");
		HashSet<Integer> hs = new HashSet<>();
		if (destinatarioflagSNT != null && destinatarioflagSNT.length > 0) {
			for (String flagId : destinatarioflagSNT) {
				hs.add(Integer.valueOf(flagId));
			}
		}

		// Avvocati & Altro Destinatario
		int lSize = lDestinatari.length;
		for (int x = 0; x < lSize; x++) {
			if ((!lDestinatari[x].equals("-") && !lSedi[x].equals("")) || (hs.contains(x))) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();

				NotificaModel lNotifica = new NotificaModel();
				lNotifica.setDataInvio(lDataInvioNotifiche);
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 0) {
					// Avvocati
					lNotifica.setCodTipoNotifica(CODTIPONOTIFICA);
					BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
					lNotifica.setAvvIdAvvocatoFascicoloSige(lAvvid);
					AvvocatoSigeModel lAvvSige = new AvvocatoSigeModel();
					lAvvSige.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(lAvvid);
					lNotifica.setAvvSige(lAvvSige);
				} else {
					// Altro Destinatario
					lNotifica.setCodTipoNotifica(lTipoNotifica);
					lNotifica.setNote(lNote[1]);
				}

				// verifica se non sia stata impostata la notifica telematica
				if (!hs.contains(x)) {
					// Crea Model Autorità Esterna
					AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
					lAutorita.setCodTipoAutorita(lDestinatari[x]);
					lAutorita.setCodSede(lCodComuneSede);
					lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
					lAutorita.setCodUfficioInserimento(lCodiceUfficio);
					lAutorita.setDataInserimento(DateUtils.getSysDate());

					// Aggiunge il model Autorità Esterna alla Notifica
					lNotifica.setAutoritaEsterna(lAutorita);
				}

				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNotifica);
			}
		}

		// Soggetto con autorita' esterna
		if (!Utils.isNullObj(lDestinatariSog) && !Utils.isNullObj(lSediSog)) {
			if (!lDestinatariSog.equals("-") && !lSediSog.equals("")) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSediSog).getCodComune();

				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setCodTipoNotifica(CODTIPONOTIFICA);
				lNotifica.setDataInvio(lDataInvioNotifiche);
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				lNotifica.setSogIdSoggetto(lFasEsteso.getFascicoloSige().getSogIdSoggetto());
				lNotifica.setNote(lNote[0]);
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatariSog);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
				lAutorita.setCodUfficioInserimento(lCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNotifica);
			}
		}

		// Soggetto con id
		if (!Utils.isNullObj(lIstitutoDetenzione)) {

			NotificaModel lNotifica = new NotificaModel();
			lNotifica.setCodTipoNotifica(CODTIPONOTIFICA);
			lNotifica.setDataInvio(lDataInvioNotifiche);
			// lNotifica.setNote(lNote);
			lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
			lNotifica.setDataInserimento(DateUtils.getSysDate());
			lNotifica.setCodUfficioInserimento(lCodiceUfficio);
			lNotifica.setCodEsito("-");
			lNotifica.setUffCodUfficio("-");
			lNotifica.setIstDetIdIstitutoDetenzione(lIstitutoDetenzione);
			lNotifica.setSogIdSoggetto(lFasEsteso.getFascicoloSige().getSogIdSoggetto());
			lNotifica.setNote(lNote[0]);
			// Aggiunge il model delle notifiche al vettore.
			lNotifiche.add(lNotifica);
		}

		// Notifica alla "Procura Generale della Repubblica presso la Corte di Appello" nel caso del Tribunale
		// di Sorveglianza
		// oppure notifica alla "Procura della Repubblica presso il Tribunale Ordinario" nel caso del
		// Tribunale di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_PROCURA_GENERALE)) {
			NotificaModel lNot = null;
			if (lCodTipoUfficio.equalsIgnoreCase("CASAP") || lCodTipoUfficio.equalsIgnoreCase("CAP"))
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PGCAP", lDescrComune);
			else if (lCodTipoUfficio.equalsIgnoreCase("TRIBSD")) {
				lCodComune = getUfficioUtenteConnesso().getCodDistretto().substring(0, 6);
				IComune lCtrl = SICOLookupRemote.getComuneRemote();
				lDescrComune = lCtrl.ExRicercaComuneByKey(lCodComune).getDescrizione();
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PM", lDescrComune);
			} else
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PM", lDescrComune);

			// Procura Generale dell'ufficio di riferimento dell'utente connesso
			lNot = new NotificaModel();
			lNot.setCodTipoNotifica(CODTIPONOTIFICACOMUNICAZIONE);
			lNot.setDataInvio(lDataInvioNotifiche);
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(lCodiceOperatore);
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(lCodiceUfficio);
			lNot.setUffCodUfficio(lCodiceUfficioPG);
			lNotifiche.add(lNot);
		}

		// Inserisce le notifiche nell'eventoNotificaModel, prelevando un array di oggetti dal vettore.
		lEve.setNotifiche(lNotifiche.toArray(new NotificaModel[0]));

		// Impostazione del Provvedimento SIGE.
		lProvModel = new ProvvedimentoSigeModel();
		lProvModel.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		// l'anno va impostato al momento del deposito.
		// lProvModel.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lProvModel.setChiaveUfficio(getCodUfficioUtenteConnesso());
		lProvModel.setDataEmissione(lDataEmissione);
		lProvModel.setCodTipoProvvedimento("02");
		lProvModel.setCodTipoProvvedimentoSige(ICostantiProvvedimentoSige.COD_FISSAZIONE_UDIENZA);
		lProvModel.setDefinitorio("N");
		lProvModel.setLuogoSvolgimento(
				getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO));
		String lFlagOrdineTraduzione = "N";
		if (isRequestChecked(ICostantiProvvedimentoSige.CAMPO_TRADUZIONE))
			lFlagOrdineTraduzione = "S";
		lProvModel.setFlagOrdineTraduzione(lFlagOrdineTraduzione);
		lProvModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che inserisce
		lProvModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																			// dell'operatore che inserisce
		lProvModel.setDataInserimento(DateUtils.getSysDate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Provvedimento SIGE Valorizzato :" + lProvModel);

		// Tipo di template da assegnare all'evento, solo per quelli
		// presenti nella cbx e sono diversi da 01 ( che generazione automantica )
		// inserisce l'id del template nel model evento.
		// Da gestire per le stampe???
		// if( getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("02") )
		// lEvento.setTemIdTemplate( TEMPLATE_ORDINANZA_GENERICO );
		// else if(getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("03") )
		// lEvento.setTemIdTemplate( TEMPLATE_ORDINANZA_RIGETTO_GENERICO );
		// else if(getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("04") )
		// lEvento.setTemIdTemplate( TEMPLATE_ORDINANZA_NLP_GENERICO );

		// lProvEveModel = new ProvvedimentoSigeEventoModel();
		// lProvEveModel.setProvvedimento(lProvModel);
		// lProvEveModel.setEventoNotifica(lEve);

		// In sessione c'è una lista di TenoreEstesoModel
		TenoriSigeUtil lTenUtil = new TenoriSigeUtil();
		lTenori = lTenUtil.listaTenoriDaListaTenoriEstesi(lTenoriEstesi);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Dimensione lista Tenori :" + lTenori.size());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see f3b.web.Action#processRequest()
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// carica dati sui model
		caricaDati();

		if (lUdienzaSige != null) {
			// esegue operazioni di inserimento
			// IUdienzaSige lCtrlSige = SIGELookupRemote.getUdienzaSigeRemote();
			// INTERVENTO PER NUOVA GESTIONE UDIENZE MONOCRATICHE/COLLEGIALI PER 11.2.1
			// POSSO MODIFICARE L'UDIENZA SOLO SE iL NUMERO DI fascicoli SIGE che puntanto all'adienza = 0
			Collection<ProcedimentixUdienzaModel> lVect = new Vector<>();
			// devo individuare tutti i fascicoli SIGE che puntanto all'adienza che sto modificando
			IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
			lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza(lUdienzaSige.getIdUdienzaSige(),
					STATO_FASCICOLO, null);
			siesLogger.debug("NUMERO DI PROCEDIMENTI COLLEGATI ALL'UDIENZA:" + lVect.size());

			// if(lVect.size() == 0){
			// lCtrlSige.ExModificaUdienzaSige(lUdienzaSige);
			// }

			// controllo e gestione dell'aggiornamento del magistrato assegnatario
			if ("I".equals(modalita)) {
				MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();
				// MagistratoAssegnatarioModel llMagModRet = null;
				// vado in else quando l'udienza associata a tale fascicolo, è utilizzata anche da altri
				// fascicoli
				// in questo caso il record dell'udienza sulla tabella udienza_sige NON E' MODIFICABILE
				// quindi ogni cambio del magistrato, o procuratore oppure del cancelliere deveno essere
				// inserite
				// sulla tabella magistrato_assegnatario per lo specifico idFascicolo
				String codMagPrecedente = "";
				if (getFascicoloSigeEstesoInSessione().getMagAssegnatario() != null
						&& getFascicoloSigeEstesoInSessione().getMagAssegnatario()
								.getMagCodMagistrato() != null) {
					codMagPrecedente = getFascicoloSigeEstesoInSessione().getMagAssegnatario()
							.getMagCodMagistrato();
				}
				String codMagNuovo = "";
				if (this.getParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO) != null) {
					codMagNuovo = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
				} else {
					codMagNuovo = getRequestStringParameter(CAMPO_COD_GIUDICE);
				}
				// se sono diversi lo sostituisce, altrimento no
				if (!codMagNuovo.equals(codMagPrecedente) && !"".equals(codMagNuovo)) {
					lMagistrato = prepareModificaAssegnatarioModel(
							getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
					MagistratoModel magMod = new MagistratoModel();
					magMod.setCodMagistrato(codMagPrecedente);
					lMagistrato.setMagistrato(magMod);
					IMagistratoAssegnatario lCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
					/* llMagModRet = */lCtrl.ExInserisciAggiornaMagistratoAssegnatario(lMagistrato);
				}
			}
		}

		UdienzaProcedimentoSigeModel lRetModel = null;
		if ("R".equals(modalita)) {
			// siamo in rifissazione
			IUdienzaSigeRuolo lCtrl = SIGELookupRemote.getUdienzaSigeRuoloRemote();
			lRetModel = lCtrl.ExModificaFissazioneUdienza(lUdiProc, lFasc, lEve, lProvModel, lTenori,
					lUdiProcVecchia);
		} else {
			// siamo in inserimento
			IUdienzaSigeRuolo lCtrl = SIGELookupRemote.getUdienzaSigeRuoloRemote();
			lRetModel = lCtrl.ExInserisciFissazioneUdienza(lUdiProc, lFasc, lEve, lProvModel, lTenori,
					lUdiProcVecchia);
		}

		BigDecimal idEvento = lRetModel.getEveIdEvento();
		String lCampiNoteReq = getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_AGGIUNTIVO);
		CampoNotaModel[] lCampiNote = new CampoNotaModel[0];
		if (lCampiNoteReq != null && lCampiNoteReq.length() > 0) {
			lCampiNote = new CampoNotaModel[1];
			lCampiNote[0] = new CampoNotaModel();
			lCampiNote[0].setDescr(lCampiNoteReq);
			lCampiNote[0].setCodOperatoreInserimento(super.getCodUfficioUtenteConnesso());
			lCampiNote[0].setCodUfficioInserimento(super.getCodUfficioUtenteConnesso());
			lCampiNote[0].setDataInserimento(DateUtils.getSysDate());
			lCampiNote[0].setEveIdEvento(idEvento);
			lEve.setCampoNote(lCampiNote);
		}

		ICampoNota ctrlNote = SIGELookupRemote.getCampoNotaController();
		ctrlNote.ExAggiornaNoteByIdEvento(lCampiNote, idEvento);

		// Aggiornamento del Fascicolo in sessione
		reloadFascicoloSigeEsteso();

		// @emma 10072018 intervento post COLLAUDO 11.2
		// se la fissazione udienza è relativa ad un fascicolo SIGE il cui stato è Ricorso convertito in
		// opposizione
		// occorre aggiornato lo stato del fascicolo in Ricorso convertito in opposizione (Fissa Udienza)
		// 1) Recupero il Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");
		if (lFasEsteso != null && ICostantiFascicoloSige.COD_RICORSO_CONVERTITO_OPPOSIZIONE
				.equals(lFasEsteso.getFascicoloSige().getCodStatoFascicolo())) {
			aggiornaStatoFascicolo(ICostantiFascicoloSige.COD_RICORSO_CONVERTITO_OPPOSIZIONE_UDI, null);
		}

		// Prepara la pagina di redirezione.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sige.udienza.action.ActLoadDettaglioFissazioneUdienza");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lRetModel.getEveIdEvento());
		lPage.setParameter("modalita", "I");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");
		return lPage.toString();
	}

	private UdienzaSigeModel insertUdienzaSIGE() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".insertUdienzaSIGE");

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		UdienzaSigeModel lUdiMod = new UdienzaSigeModel();
		aggiungiDati(lUdiMod);

		// lUdiMod.setColIdCollegio(lColModRet.getIdCollegio());

		lUdiMod.setDataUdienza(getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA));

		// if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
		// lUdiMod.setCodIdAssistente(getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
		// if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE))
		// lUdiMod.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));
		// lUdiMod.setLuogoUdienza(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA));

		// 20090402 - Commentato così come richiesto dall'amministrazione.
		// lUdiMod.setNumeroMaxFascicoli(
		// getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_NUMERO_MAX_FASCICOLI));

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lUdiMod.setDataInserimento(DateUtils.getSysDate());
		lUdiMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lUdiMod.setCodUfficioInserimento(getUfficioUtenteConnesso().getCodUfficio());

		IUdienzaSige lCtrlUdi = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel lUdiRetMod = new UdienzaSigeModel();
		lUdiRetMod = lCtrlUdi.ExInserisciUdienzaSige(lUdiMod, null);

		return lUdiRetMod;
	}

	private CollegioModel insertCollegio() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".insertCollegio");
		CollegioModel lColMod = new CollegioModel();
		BigDecimal idSezione = null;
		String tipoGiudizio = "";
		try {
			idSezione = getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA);
			// [EC] Recupero il tipoGiudizio per distinguere le MONOCRATICHE e le COLLEGIALI
			tipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);

		} catch (Exception e) {
			siesLogger.info("NESSUNA SEZIONE, O CAMPO_COD_TIPO_GIUDIZIO IN REQUEST");
		}
		lColMod.setSezIdSezione(idSezione);
		lColMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lColMod.setDataInizioValidita(DateUtils.getSysDate());
		lColMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lColMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lColMod.setDataInserimento(DateUtils.getSysDate());

		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		// 20170913: [SG] aggiunti parametri di passaggio
		String codMagis = "";
		Date dataUdienza = null;
		if (Utils.isPresent(lUdienzaSige))
			dataUdienza = lUdienzaSige.getDataUdienza();
		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)
				&& Utils.isPresent(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)))
			codMagis = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		else if (Utils.isPresent(lUdienzaSige) && Utils.isPresent(lUdienzaSige.getCodMagistratoAss()))
			codMagis = lUdienzaSige.getCodMagistratoAss();
		else {
			MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
			codMagis = lMagAss.getMagCodMagistrato();
		}
		// 20171005: [SG] cambiata firma del metodo
		// 20171129: [EC] cambiata firma del metodo
		String ris = lCtrl.ExRicercaMaxCodCollegio(lColMod, dataUdienza, codMagis, tipoGiudizio, "I");
		int max = 0;
		BigDecimal idCollegio = null;
		// String operazione = "";
		if (ris.contains("#")) {
			// 20171122: [EC] allineo il codice per una modifica al metodo ExRicercaMaxCodCollegio che ora
			// restituisce una
			// stringa così composta codCollegio#idCollegio#(flagCodicePresente)
			String[] st = ris.split("#");
			max = new Integer(st[0]).intValue();
			idCollegio = new BigDecimal(st[1]);
			// operazione = st[2];
			lColMod.setIdCollegio(idCollegio);
		} else {
			if (Utils.isPresent(ris))
				max = new Integer(ris).intValue();
		}
		// determina il cod collegio
		// 20171005: [SG] se esiste un collegio lo aggancio, altrimenti lo inserisco ex novo
		lColMod.setCodCollegio("" + max);
		// lColMod.setCodCollegio("" + (max + 1));
		CollegioMagistratoModel[] magistrati = letturaDatiMagistrati(new BigDecimal(max + 1));
		lColMod.setCollegioMagistrati(magistrati);

		// Esegue l'insert.
		CollegioModel lColModRet = lCtrl.ExInserisciCollegio(lColMod);

		return lColModRet;
	}

	private CollegioMagistratoModel[] letturaDatiMagistrati(BigDecimal idCollegio) throws F3BException {

		ArrayList<CollegioMagistratoModel> lArrayList = new ArrayList<>();
		lFasEsteso = getFascicoloSigeEstesoInSessione();
		CollegioMagistratoModel lModel = new CollegioMagistratoModel();
		lModel.setColIdCollegio(idCollegio);
		lModel.setMagCodMagistrato(lFasEsteso.getMagAssegnatario().getMagCodMagistrato());
		lModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lModel.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lModel.setDataInserimento(DateUtils.getSysDate());
		// 20171013: [SG] aggiunta variabile di collegamento all'udienza sige
		if (Utils.isPresent(lUdienzaSige))
			lModel.setUdiIdUdienzaSige(lUdienzaSige.getIdUdienzaSige());
		lArrayList.add(lModel);
		return lArrayList.toArray(new CollegioMagistratoModel[1]);
	}

	/**
	 * Il metodo aggiorna lo stato del fascicolo lo aggiungo per intervento post COLLAUDO 11.2
	 * 
	 * @param stato
	 * @param dataDefinizione
	 * @throws F3BException
	 */
	private void aggiornaStatoFascicolo(String stato, Date dataDefinizione) throws F3BException {
		FascicoloSigeEstesoModel fascicoloEsteso = (FascicoloSigeEstesoModel) super.getSessionAttribute(
				"FascicoloSigeEsteso");
		FascicoloSigeModel fascicolo = fascicoloEsteso.getFascicoloSige();
		IFascicoloSige ctrlFasc = SIGELookupRemote.getFascicoloSigeRemote();
		fascicolo.setCodStatoFascicolo(stato);
		if (dataDefinizione != null)
			fascicolo.setDataDefinizione(dataDefinizione);

		fascicolo.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		fascicolo.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		fascicolo.setDataAggiornamento(DateUtils.getSysDate());

		RichiestaSigeModel richiesta = fascicoloEsteso.getRichiestaSige();
		richiesta.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		richiesta.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		richiesta.setDataAggiornamento(DateUtils.getSysDate());
		ctrlFasc.ExModificaFascicoloSige(fascicolo, richiesta);
		fascicoloEsteso.setFascicoloSige(fascicolo);
		setSessionAttribute("FascicoloSigeEsteso", fascicoloEsteso);
	}

	/**
	 * metodo introdotto per la nuova gestione dell'udienza monocratica/collegiale per sies 11.2.1
	 *
	 * @param idFascicoloSige
	 * @return
	 * @throws F3BException
	 */
	private MagistratoAssegnatarioMagistratoModel prepareModificaAssegnatarioModel(BigDecimal idFascicoloSige)
			throws F3BException {

		MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();
		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)) {
			lMagistrato.getMagistratoAssegnatario().setMagCodMagistrato(
					this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		} else {
			lMagistrato.getMagistratoAssegnatario()
					.setMagCodMagistrato(this.getRequestStringParameter(CAMPO_COD_GIUDICE));
		}

		lMagistrato.getMagistratoAssegnatario().setFasSigeIdFascicoloSige(idFascicoloSige);
		lMagistrato.getMagistratoAssegnatario().setDataInizio(DateUtils.getSysDate());
		lMagistrato.getMagistratoAssegnatario().setCodRuoloMagistrato("03");
		lMagistrato.getMagistratoAssegnatario().setDataInserimento(DateUtils.getSysDate());
		lMagistrato.getMagistratoAssegnatario().setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lMagistrato.getMagistratoAssegnatario().setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
			lMagistrato.getMagistratoAssegnatario().setIdAssistente(
					getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE))
			lMagistrato.getMagistratoAssegnatario()
					.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));

		return lMagistrato;
	}

}