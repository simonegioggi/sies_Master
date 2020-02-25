package siap.siep.penasospesa.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;

/**
 * <p>
 * Title: ActInserisciRicAdempObblighi
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * <p>
 * @author Luigi
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciRicAdempObblighi extends ActionSiap implements ICostantiPenaSospesa {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciRicAdempObblighi: inizio");

		// Inizializzazioni
		EventoNotificaModel lEve = new EventoNotificaModel();
		EventoNotificaModel lRetModel;
		// String lPage = null;

		// VALORIZZAZIONE EVENTO
		EventoModel lEveModel = letturaEvento();
		lEve.setEvento(lEveModel);

		// VALORIZZAZIONE NOTIFICA
		lEve.setNotifiche(letturaNotifica());

		// VALORIZZAZIONE CAMPO NOTE
		CampoNotaModel[] lNote = null;
		// Lettura Tipologia Obbligo
		CampoNotaModel lCampoNote1 = letturaTipologiaObbligo();
		// Lettura Note (non obbligatorio)
		CampoNotaModel lCampoNote2 = letturaCampoNote();
		if (lCampoNote2 != null) {
			lNote = new CampoNotaModel[2];
			lNote[0] = lCampoNote1;
			lNote[1] = lCampoNote2;
		} else {
			lNote = new CampoNotaModel[1];
			lNote[0] = lCampoNote1;
		}
		lEve.setCampoNote(lNote);

		// Memorizzazione
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		// Costruzione della pagina di Dettaglio
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.siep.penasospesa.action.ActDettaglioRicAdempObblighi");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
				lRetModel.getEvento().getIdEvento().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciRicAdempObblighi: fine");

		return lRedirectTo.toString();
	}

	/**
	 * Viene letto il codice della Tipologia di Obbligo selezionata nella Form, decodificata e memorizzata in
	 * un CampoNote.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected CampoNotaModel letturaTipologiaObbligo() throws Exception {
		String lCodTipologia = getRequestStringParameter(CAMPO_COD_ARTICOLO);
		String lDescTipologia = DecodificheUtils
				.getDescbyCode(DecodificheManager.getInstance().getTipoSospSubordinata(), lCodTipologia);

		CampoNotaModel lCampoNotaMod = new CampoNotaModel();
		lCampoNotaMod.setDescr(lDescTipologia);
		lCampoNotaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lCampoNotaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());

		return lCampoNotaMod;

	}

	protected EventoModel letturaEvento() throws Exception {
		EventoModel lEveModel = new EventoModel();

		// ID Fascicolo SIEP dalla sessione
		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		lEveModel.setCodUfficioEmittente(getCodUfficioUtenteConnesso());

		lEveModel.setCodTipoEvento("02");
		lEveModel.setCodTipoProvvedimento("26"); // Richiesta
		lEveModel.setCodMotivo("1112");

		lEveModel.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveModel.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEveModel.setCodUfficioDestinatario("-");
		lEveModel.setCodTipoUfficioDestinatario("-");

		lEveModel.setCodLuogoDestinatario("-");
		lEveModel.setCodEsito("-");

		// Data di Emissione e di Trasmissione se non presenti nella FORM valorizzate con quella di sistema
		if (isRequestParameterNullObj(ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE))
			// 11/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
			// lEveModel.setDataEmissione(DateUtils.getSysDate());
			lEveModel.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		else
			lEveModel.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		if (isRequestParameterNullObj(ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI))
			lEveModel.setDataTrasmissioneAtti(DateUtils.getSysDate());
		else
			lEveModel.setDataTrasmissioneAtti(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
							ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
							ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		// lEveModel.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModel.setCodOperatoreInserimento(getCodUfficioUtenteConnesso());
		lEveModel.setDataInserimento(DateUtils.getSysDate());
		lEveModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveModel.setFlagStampaSiep("S");
		lEveModel.setFlagVideoSiep("S");
		lEveModel.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveModel.setDataTrasmissioneAtti(DateUtils.getSysDate());

		return lEveModel;

	}

	protected NotificaModel[] letturaNotifica() throws Exception {
		// Lettura dati dei destinatari

		ArrayList<NotificaModel> lNotifiche = new ArrayList<>();

		// SETTO NOTIFICA UFFICIO GE
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_UFFICIO_GE)
				&& !("-").equals(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_GE))
				&& !isRequestParameterNullObj(CAMPO_SEDE_UFFICIO_GE)
				&& (getRequestStringParameter(CAMPO_SEDE_UFFICIO_GE).length() > 1)) {
			String lDescrComune = getRequestStringParameter(CAMPO_SEDE_UFFICIO_GE);
			String lCodTipoUfficioDest = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_GE);
			String lCodUfficioDest = null;
			if (lCodTipoUfficioDest.length() > 1 && lDescrComune.length() > 1)
				lCodUfficioDest = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioDest, lDescrComune);

			NotificaModel lNot = new NotificaModel();
			lNot.setCodTipoNotifica("N");
			lNot.setDataInvio(DateUtils.getSysDate());
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNot.setUffCodUfficio(lCodUfficioDest);

			if (!isRequestParameterNullObj(CAMPO_SEZIONE_UFFICIO_GE)
					&& !("-").equals(getRequestStringParameter(CAMPO_SEZIONE_UFFICIO_GE)))
				lNot.setNote("Sezione " + getRequestStringParameter(CAMPO_SEZIONE_UFFICIO_GE));

			lNotifiche.add(lNot);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica GE=" + lNot);
		}

		// SETTO NOTIFICA Autorità di Polizia
		if (!this.isRequestParameterNullObj("CodTipoAutorita")
				&& this.getRequestStringParameter("CodTipoAutorita") != null
				&& !this.getRequestStringParameter("CodTipoAutorita").equals("-")
				&& !this.getRequestStringParameter("CodSede").equals(""))

		{
			String lPolizia = this.getRequestStringParameter("CodTipoAutorita");
			String lSedePolizia = this.getRequestStringParameter("CodSede");
			NotificaModel lNotModPol = new NotificaModel();
			if (!this.isRequestParameterNullObj("Descrizione")) {
				String lNotePolizia = this.getRequestStringParameter("Descrizione");
				lNotModPol.setNote(lNotePolizia);
			}
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotModPol.setCodTipoNotifica("NC");
			lNotModPol.setDataInvio(DateUtils.getSysDate());

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica POLIZIA=" + lNotModPol);
			lNotifiche.add(lNotModPol);
		}

		// ALTRO DESTINATARIO
		if (!this.isRequestParameterNullObj("CodTipoAutoritaE")
				&& this.getRequestStringParameter("CodTipoAutoritaE") != null
				&& !this.getRequestStringParameter("CodTipoAutoritaE").equals("-")
				&& !this.getRequestStringParameter("CodSedeE").equals(""))

		{
			String lPolizia = this.getRequestStringParameter("CodTipoAutoritaE");
			String lSedePolizia = this.getRequestStringParameter("CodSedeE");
			NotificaModel lNotModPol = new NotificaModel();
			if (!this.isRequestParameterNullObj("NoteE")) {
				String lNotePolizia = this.getRequestStringParameter("NoteE");
				lNotModPol.setNote(lNotePolizia);
			}
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotModPol.setCodTipoNotifica("AA");
			lNotModPol.setDataInvio(DateUtils.getSysDate());

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica ALTRO=" + lNotModPol);
			lNotifiche.add(lNotModPol);
		}

		// SETTO Le NOTIFICHE ai Difensori
		// BigDecimal lIdavvocato = (BigDecimal) this.getRequestBigDecimalParameter("AvvIdAvvocato");
		// =========================================
		// Notifiche agli Avvocati
		// =========================================
		if (!this.isRequestParameterNullObj("ck_avvocati")) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("NOTIFICA AGLI AVVOCATI da ESEGUIRE!!!!");

			String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			int lIndex = 0;

			for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				NotificaModel lNotAvv = new NotificaModel();

				lNotAvv.setCodTipoNotifica("ND");
				lNotAvv.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				lNotAvv.setCodEsito("-");
				lNotAvv.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNotAvv.setDataInserimento(DateUtils.getSysDate());
				lNotAvv.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

				if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)) {
					String[] lTipoAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
					String[] lSedeAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
					String[] lNoteAvvocato = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);

					lNotAvv.setNote(lNoteAvvocato[lIndex]);

					AutoritaEsternaModel lAut = new AutoritaEsternaModel();
					lAut.setCodTipoAutorita(lTipoAutoritaEsternaAvvocato[lIndex]);

					ComuneModel lComMod = new ComuneModel(
							getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
					lAut.setCodSede(lComMod.getCodComune());
					lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
					lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lAut.setDataInserimento(DateUtils.getSysDate());
					lNotAvv.setAutoritaEsterna(lAut);
				}

				lNotifiche.add(lNotAvv);
			}
			// fine if su check avvocati
		}

		return lNotifiche.toArray(new NotificaModel[0]);

	}

	/*******************************************************************/
	protected CampoNotaModel letturaCampoNote() throws Exception
	/*******************************************************************/
	{
		String lNote = getRequestStringParameter(CAMPO_NOTE);
		CampoNotaModel lCampoNotaMod = null;

		if (lNote.trim().length() > 0) {
			lCampoNotaMod = new CampoNotaModel();
			lCampoNotaMod.setDescr(lNote);
			lCampoNotaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampoNotaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
		}
		return lCampoNotaMod;

	}

}