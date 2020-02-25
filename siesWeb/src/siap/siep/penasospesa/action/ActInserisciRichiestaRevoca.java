package siap.siep.penasospesa.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.PenaComplessivaController;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.penasospesa.controller.IPenaSospesa;
import siap.siep.reato.controller.ReatoController;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciRichiestaGenerica
 * </p>
 * <p>
 * Description: ActInserisciRichiestaGenerica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciRichiestaRevoca extends ActionSiap implements ICostantiPenaSospesa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("02");// Tipo Evento = Richiesta
		lEve.getEvento().setCodTipoProvvedimento("26");
		if (getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("Sentenza")) {
			// lEve.getEvento().setCodMotivo("1100");
			lEve.getEvento().setCodMotivo(getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_MOTIVO));
		} else
			lEve.getEvento().setCodMotivo(getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_MOTIVO));

		// lEve.getEvento().setFlagDocumentoRegistrato("S");
		lEve.getEvento().setFlagVideoSiep("S");
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = DateUtils.getSysDate();
		lEve.getEvento().setDataEmissione(lDataEmissione);
		lEve.getEvento().setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEve.getEvento().setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());

		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// imposto il campo contenuto nella tabella CampoNote
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		CampoNotaModel lCampMod = new CampoNotaModel();
		if (!this.isRequestParameterNullObj(ICostantiPenaSospesa.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiPenaSospesa.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiPenaSospesa.CAMPO_NOTE).equals("")) {
			lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampMod.setCodUfficioInserimento(lUff.getCodUfficio());
			lCampMod.setDataInserimento(DateUtils.getSysDate());
			lCampMod.setDescr(getRequestStringParameter(ICostantiPenaSospesa.CAMPO_NOTE));
			lCampMod.setProgressivo(new BigDecimal(1));
		}

		// Se non ho selezionato il provvedimento dalla lista, nel sistema non c'è e quindi
		// devo inserirlo a mano. Nel caso di sentenza inserisco una annotazione manuale

		DettaglioPenaComplessivaModel dettaglioPenaComplessiva = new DettaglioPenaComplessivaModel();
		Vector reati = new Vector();
		AnnotazioneManualeModel lAnnotazione = new AnnotazioneManualeModel();

		// if (getRequestStringParameter("flagDaLista").equals("")){
		if (getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("Sentenza")) {
			// Valorizzazione campi specifici
			lAnnotazione.setCodTipoAnnotazione("016"); // Gestione Pene Sospese
			lAnnotazione.setFlagAppProvvisoria("-");
			lAnnotazione.setFlagValidato("S");
			lAnnotazione.setCodFonte("-");
			lAnnotazione.setCodSottonumerazione("-");
			lAnnotazione.setCodCausaleComputo("-");
			lAnnotazione.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAnnotazione.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAnnotazione.setDataInserimento(DateUtils.getSysDate());
			lAnnotazione.setCodDpr("-");

			// I dati relativi alla nuova sentenza inserita vengono letti dalla request
			lAnnotazione.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

			lAnnotazione.setAnnoSentenzaSiap(
					getRequestBigDecimalParameter(ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA));
			lAnnotazione.setNumeroSentenzaSiap(
					getRequestStringParameter(ICostantiPenaSospesa.CAMPO_NUMERO_SENTENZA_REVOCA));

			lAnnotazione.setDataSentenzaSiap(
					getRequestDateParameter(ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA,
							ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA,
							ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA));

			lAnnotazione.setCodTipoUfficioSiep(
					getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO));
			lAnnotazione.setCodLuogoUfficioSiep(getCodComuneByDescr(
					getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_LUOGO_SENTENZA_REVOCA))
							.getCodComune());

			// data irrevocabilità
			lAnnotazione.setDataIscrizioneSiep(
					getRequestDateParameter(ICostantiPenaSospesa.CAMPO_ANNO_DATA_ORDINANZA_REVOCA,
							ICostantiPenaSospesa.CAMPO_MESE_DATA_ORDINANZA_REVOCA,
							ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ORDINANZA_REVOCA));

			// Si lega l'Annotazione del Provvedimento del PM all'evento di annotazione revoca
			// ma lo faccio nel controller quando ho inserito l'evento e quindi conosco l'id
			// lAnnMod.setEveIdEvento();
		}

		// se ho inserito il provvedimento a mano, potrei aver inserito anche reati e pena complessiva
		if (getRequestStringParameter("flagDaLista").equals("")) {
			if (!isSessionAttributeNullObj("reati")) {
				if (!getSessionAttribute("reati").equals(""))
					reati = (Vector) getSessionAttribute("reati");
				else
					reati = new Vector();
			} else
				reati = new Vector();

			if (!isSessionAttributeNullObj("dettaglioPenaComplessiva")) {
				if (!getSessionAttribute("dettaglioPenaComplessiva").equals(""))
					dettaglioPenaComplessiva = (DettaglioPenaComplessivaModel) getSessionAttribute(
							"dettaglioPenaComplessiva");
				else
					dettaglioPenaComplessiva = new DettaglioPenaComplessivaModel();
			} else
				dettaglioPenaComplessiva = new DettaglioPenaComplessivaModel();
		} else {
			// Luigi 1-7-2011

			// Selezione da lista
			String lIdFascicolo = getRequestStringParameter("idFas");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID del Fascicolo Selezionato da lista -> " + lIdFascicolo);

			// Reati
			ReatoController lCtrlreati = new ReatoController();
			reati = lCtrlreati.ExRicercaReatiNoCircostanzaByFascicolo(getRequestBigDecimalParameter("idFas"));

			// Pena Complessiva
			PenaComplessivaController lCtrlpenacompl = new PenaComplessivaController();
			dettaglioPenaComplessiva = lCtrlpenacompl.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(
					getRequestBigDecimalParameter("idFas"));
		}

		// Preparo le notifiche
		ArrayList lNotifiche = new ArrayList();

		// Giudice dell'esecuzione
		// Controllo sull'esistenza dell'ufficio per quel comune
		if (!isRequestParameterNullObj(ICostantiPenaSospesa.CAMPO_COD_UFFICIO_GE)
				&& getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_UFFICIO_GE) != null
				&& !getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_UFFICIO_GE).equals("-")
				&& !isRequestParameterNullObj(ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE)
				&& getRequestStringParameter(ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE) != null)

		{
			String lCodiceUffGE = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_UFFICIO_GE),
					getRequestStringParameter(ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE));
			NotificaModel lNotGE = new NotificaModel();

			lNotGE.setCodTipoNotifica("E");
			lNotGE.setDataInvio(lDataEmissione);
			lNotGE.setCodEsito("-");
			lNotGE.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotGE.setDataInserimento(lDataEmissione);
			lNotGE.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotGE.setUffCodUfficio(lCodiceUffGE);
			// MEV70
			lNotGE.setNote(
					"Sez. " + (getRequestStringParameter(ICostantiPenaSospesa.CAMPO_SEZIONE_UFFICIO_GE)));

			lNotifiche.add(lNotGE);
		}

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);
		IPenaSospesa lCtrlPSosp = SIEPLookupRemote.getPenaSospesaRemote();
		EventoNotificaModel lEveRet = lCtrlPSosp.ExInserisciRichiestaRevoca(lAnnotazione, lEve, lCampMod,
				reati, dettaglioPenaComplessiva);

		this.setSessionAttribute("reati", "");
		this.setSessionAttribute("dettaglioPenaComplessiva", "");

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.penasospesa.action.ActLoadDettaglioRichiestaRevoca&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveRet.getEvento().getIdEvento();

		return lPage;
	}

}