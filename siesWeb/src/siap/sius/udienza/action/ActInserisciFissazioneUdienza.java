package siap.sius.udienza.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.curatore.action.ICostantiCuratore;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.action.ICostantiGeneraleProcedimento;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInserisciFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento della Fissazione Udienza.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class ActInserisciFissazioneUdienza extends ActionSius implements ICostantiUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

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
		String lAvvocato[] = getRequestStringParameters(CAMPO_COD_AVVOCATO);
		// String lViaFax[] = getRequestStringParameter ( ICostantiRichiestaAtti.CAMPO_NOTIFICHE_VIA_FAX
		// ).split(",");
		// String lViaFax[] = null;
		String lSediSog = null;
		String lDestinatariSog = null;
		BigDecimal lIdFasSius = null;
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		String[] lNote = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_NOTE);
		String lTipoNotifica = getRequestStringParameter(CAMPO_TIPONOTIFICA);

		if (this.IsFascicoloSiusModificabile() == false)
			throw new SIUSException(SIUSException.USER_MESSAGE, ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

		// Preleva l'id dell'istituto detenzione
		String lIstitutoDetenzione = null;
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lIstitutoDetenzione = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		} else {
			lSediSog = getRequestStringParameter(ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE);
			lDestinatariSog = getRequestStringParameter(ICostantiUdienza.CAMPO_COD_IST_DETENZIONE);
		}

		// Preleva dalla sessione il model fascicoloSIUSGP
		FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		lIdFasSius = lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius();

		// MEV_65: Punto 1.13 se magistrato è scaduto impossibile inserire decreto od ordinanza
		if (Utils.isPresent(lIdFasSius)) {
			IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
			MagistratoRelatoreModel mrm = imr.ExRicercaEstesaMagRelByFascicolo(lIdFasSius);
			if (mrm != null && mrm.getMagistrato() != null) {
				IMagistrato im = SICOLookupRemote.getMagistratoRemote();
				MagistratoModel mm = new MagistratoModel();
				mm.setCodMagistrato(mrm.getMagistrato().getCodMagistrato());
				mm.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
				mm.setCognome(mrm.getMagistrato().getCognome());
				mm.setNome(mrm.getMagistrato().getNome());
				Vector v = im.ExRicercaMagistrato(mm);
				if (!v.isEmpty()) {
					MagistratoModel mag = (MagistratoModel) v.get(0);
					if (mag.getDataFineValidita() != null
							&& (DateUtils.isLower(mag.getDataFineValidita(), DateUtils.getSysDate())
									|| DateUtils.isEquals(mag.getDataFineValidita(), DateUtils.getSysDate())))
						throw new SIUSException(SIUSException.USER_MESSAGE,
								"Attenzione! Impossibile emettere il provvedimento. Assegnatario del procedimento è un magistrato non più in servizio!");
				}
			}
		}

		// Preleva dalla request la data udienza.
		Date lDataUdienza = getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
				CAMPO_GIORNO_DATA_UDIENZA);

		// Preleva dalla request id Udienza
		BigDecimal lIdUdienza = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA);

		//
		// Si istanzia un model UdienzaProcedimentoModel.
		//
		UdienzaProcedimentoModel lUdiProc = new UdienzaProcedimentoModel();
		lUdiProc.setUdiIdUdienza(lIdUdienza);
		lUdiProc.setGenPridGeneraleProcedimento(
				lFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		lUdiProc.setCodOperatoreInserimento(lCodiceOperatore);
		lUdiProc.setCodUfficioInserimento(lCodiceUfficio);
		lUdiProc.setDataInserimento(DateUtils.getSysDate());
		lUdiProc.setFlagRinviata(ICostantiUdienzaProcedimento.UDIENZA_FISSATA);

		// Valorizzazione dell'eventuale UdienzaProcedimento da aggiornare
		UdienzaProcedimentoModel lUdiProcVecchia = null;
		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)) {
			BigDecimal lIdUdiProOld = getRequestBigDecimalParameter(
					ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO);
			if (lIdUdiProOld != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("UDIENZA_PROCEDIMENTO da aggiornare: " + lIdUdiProOld.toString());

				lUdiProcVecchia = new UdienzaProcedimentoModel();
				lUdiProcVecchia.setIdUdienzaProcedimento(lIdUdiProOld);
				lUdiProcVecchia.setCodOperatoreAggiornamento(lCodiceOperatore);
				lUdiProcVecchia.setCodUfficioAggiornamento(lCodiceUfficio);
				lUdiProcVecchia.setDataAggiornamento(DateUtils.getSysDate());
				lUdiProcVecchia.setFlagRinviata(ICostantiUdienzaProcedimento.UDIENZA_MODIFICATA);
			}
		}

		// ID Udienza
		// Inserisci Udienza_Procedimento con l'id del generale_Procedimento associato
		// con il FasciolcoSIUS in sessione.
		// Update della data_CAMERA_CONSIGLIO in generale_proceidmento
		FascicoloGPModel lFasc = lFascicoloGPModel;

		// Imposta la data camera di consiglio, con la data udienza.
		lFasc.getGeneraleProcedimentoModel().setDataCameraConsiglio(lDataUdienza);
		// lFasc.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(lFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		lFasc.getGeneraleProcedimentoModel().setCodUfficioAggiornamento(lCodiceUfficio);
		lFasc.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(lCodiceOperatore);
		lFasc.getGeneraleProcedimentoModel().setDataAggiornamento(DateUtils.getSysDate());
		lFasc.getGeneraleProcedimentoModel()
				.setAnnotazione(getRequestStringParameter(ICostantiGeneraleProcedimento.CAMPO_ANNOTAZIONE));
		lFasc.getGeneraleProcedimentoModel().setCodOggettoProcedimento(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		// Il campo UDI_ID_UDIENZA è impostato nel controller.

		// Prepara il model EventoNotifica.
		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("01"); // Tipo Evento = Provvedimento
		lEve.getEvento().setCodTipoProvvedimento("02"); // Tipo Provvedimento = Decreto
		// lEve.getEvento().setCodMotivo("0601"); // Fissazione Udienza
		lEve.getEvento().setCodEsito("0601");
		lEve.setNomeTemplate("FU1");

		lEve.getEvento().setFasSiuIdFascicoloSius(lIdFasSius);
		lEve.getEvento().setDataEmissione(lDataEmissione);
		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setCodLuogoEmittente(lCodComune);
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setFasSieIdFascicoloSiep(
				lFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		// Preleva le note dalla form
		String lCampiNoteReq = getRequestStringParameter(ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO);
		// Luigi 6-10-2004
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

		// Avvocati & Altro Destinatario
		int lSize = lDestinatari.length;
		for (int x = 0; x < lSize; x++) {
			// MEV 10 - Eliminato controllo sulla sede, poichè per la notifica al
			// difensore selezionando la dicitura "Notifica ai sensi dell'art. 148
			// comma 2 bis c.p.p." (in questo caso si tratta di notifica effettuata
			// via fax) non viene indicata alcuna sede
			// if( !lDestinatari[x].equals("-") && !lSedi[x].equals("") )
			if (!lDestinatari[x].equals("-")) {
				String lCodComuneSede = "-";
				if (!lSedi[x].equals("")) {
					lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();
				}

				NotificaModel lNotifica = new NotificaModel();
				lNotifica.setCodTipoNotifica(lTipoNotifica);
				lNotifica.setDataInvio(lDataEmissione);
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 1) {
					lNotifica.setCodTipoNotifica(CODTIPONOTIFICA);
					BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
					lNotifica.setAvvIdAvvocatoFascicoloSius(lAvvid);

					// BigDecimal flagNotificaViaFax = new BigDecimal (lViaFax[x]);
					BigDecimal flagNotificaViaFax = null;
					lNotifica.setFlagNotificaViaFax(flagNotificaViaFax);

				} else if (lAvvocato[x] != null && lAvvocato[x].trim().compareTo("C") == 0) {
					lNotifica.setCodTipoNotifica(CODTIPONOTIFICA);
					BigDecimal lCurid = new BigDecimal(
							getRequestStringParameter(ICostantiCuratore.CAMPO_ID_CURATORE));
					lNotifica.setCurIdCuratore(lCurid);
				}

				lNotifica.setNote(lNote[x + 1]); // Vincenzo 18/01/2007 Allineato il Campo Note rispetto ai
													// destinatari.
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatari[x]);
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

		// SOggetto con autorita' esterna
		if (!Utils.isNullObj(lDestinatariSog) && !Utils.isNullObj(lSediSog)) {
			if (!lDestinatariSog.equals("-") && !lSediSog.equals("")) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSediSog).getCodComune();

				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setCodTipoNotifica(CODTIPONOTIFICA);
				lNotifica.setDataInvio(lDataEmissione);
				// lNotifica.setNote(lNote);
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				lNotifica.setSogIdSoggetto(lFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto());
				if (!lNote[0].equals("")) {
					lNotifica.setNote(lNote[0]);
				}
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
			lNotifica.setDataInvio(lDataEmissione);
			// lNotifica.setNote(lNote);
			lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
			lNotifica.setDataInserimento(DateUtils.getSysDate());
			lNotifica.setCodUfficioInserimento(lCodiceUfficio);
			lNotifica.setCodEsito("-");
			lNotifica.setUffCodUfficio("-");
			lNotifica.setIstDetIdIstitutoDetenzione(lIstitutoDetenzione);
			lNotifica.setSogIdSoggetto(lFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto());
			if (!lNote[0].equals("")) {
				lNotifica.setNote(lNote[0]);
			}
			// Aggiunge il model delle notifiche al vettore.
			lNotifiche.add(lNotifica);
		}

		// Notifica alla "Procura Generale della Repubblica presso la Corte di Appello" nel caso del Tribunale
		// di Sorveglianza
		// oppure notifica alla "Procura della Repubblica presso il Tribunale Ordinario" nel caso del
		// Tribunale di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiUdienza.CAMPO_PROCURA_GENERALE)) {
			NotificaModel lNot = null;

			if ("UDS".equalsIgnoreCase(lCodTipoUfficio))
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PM", lDescrComune);
			// MERGE v10: aggiunta gestione casistica per minori
			else if ("TDSM".equalsIgnoreCase(lCodTipoUfficio) || "UDSM".equalsIgnoreCase(lCodTipoUfficio))
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PMM", lDescrComune);
			else
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PGCAP", lDescrComune);

			// Procura Generale dell'ufficio di riferimento dell'utente connesso
			lNot = new NotificaModel();
			lNot.setCodTipoNotifica(CODTIPONOTIFICACOMUNICAZIONE);
			lNot.setDataInvio(lDataEmissione);
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(lCodiceOperatore);
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(lCodiceUfficio);
			lNot.setUffCodUfficio(lCodiceUfficioPG);
			lNotifiche.add(lNot);
		}

		// Inserisce le notifiche nell'eventoModel, prelevando
		// un array di oggetti dal vettore.
		lEve.setNotifiche(lNotifiche.toArray(new NotificaModel[0]));

		// Gestione oggetti Tenore
		String lCodOggetti = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO);
		String lDescOggetti = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO);
		// 15/04/2004 Aggiunti i Codici Dettaglio Oggetti.
		String lCodDettaglioOggetti = getRequestStringParameter(
				ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO);

		TenoreModel[] lTenori = this.parseOggettiTenori(lCodOggetti, lDescOggetti, lCodDettaglioOggetti);

		IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
		UdienzaProcedimentoModel lRetModel = lCtrl.ExInserisciFissazioneUdienza(lUdiProc, lFasc, lEve,
				lTenori, lUdiProcVecchia);

		// Aggiornamento del Fascicolo in sessione
		IFascicoloSius lCtrFas = SIUSLookupRemote.getFascicoloSiusRemote();
		lFascicoloGPModel = lCtrFas
				.ExRicercaFascicoloByKey(lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
		setSessionAttribute("fascicoloSiusGP", lFascicoloGPModel);

		// Prepara la pagina di redirezione.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.udienza.action.ActLoadDettaglioFissazioneUdienza");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lRetModel.getEveIdEvento());
		lPage.setParameter("modalita", "I");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");
		return lPage.toString();
	}

}