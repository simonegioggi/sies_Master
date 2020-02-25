package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActInserisciRichRiesamePericoloSociale
 * </p>
 * <p>
 * Description: Inserimento della Richiesta al Magistrato di Sorveglianza di
 * </p>
 * <p>
 * Accertamento di pericolosità Sociale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROS
 */
public class ActInserisciRichRiesamePericoloSociale extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {

	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
//		String lCodPosGiu = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		String lPage = "";

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		String codRich = getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC);

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();

		lEve.getEvento().setCodTipoEvento("01");
		lEve.getEvento().setCodTipoProvvedimento("26");
		lEve.getEvento().setCodMotivo(codRich);
		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");

		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.getEvento().setDataEmissione(lDataEmissione);

		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);

		lEve.getEvento().setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");

		// 16-05-2016 - Aggiornamento Uffici
		// lEve.getEvento().setCodTipoUfficioDestinatario("-");
		String lTipoUff = getRequestStringParameter("tipoUDS");
		String lComuneUff = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);

		String lCodUff = getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lComuneUff);

		lEve.getEvento().setCodTipoUfficioDestinatario(lTipoUff);
		lEve.getEvento().setCodUfficioDestinatario(lCodUff);

		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// imposto il campo contenuto nella tabella CAMPONOTE
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE).equals("")) {
			ArrayList lCampoNote = new ArrayList();
			CampoNotaModel lCampMod = new CampoNotaModel();
			lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampMod.setCodUfficioInserimento(lCodiceUfficio);
			lCampMod.setDataInserimento(DateUtils.getSysDate());
			lCampMod.setDescr(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
			lCampMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
				;

			lEve.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
		}

		// Notifiche
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		ArrayList lNotificheArray = new ArrayList();

		String lSedeDestinatario_E = null;
		String lDestinatario_E = null;
		String lNote_E = null;

		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			lSedeDestinatario_E = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
		}

		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
			lNote_E = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

		// Altra Autorita
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("AA");
		lNotMod.setDataInvio(lDataTrasmissione);
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setNote(lNote_E);

		if (lDestinatario_E != null) {
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_E);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setIstDetIdIstitutoDetenzione("");

			lNotMod.setAutoritaEsterna(lAutMod);
		}

		if (lDestinatario_E != null) {
			lNotificheArray.add(lNotMod);
		}

		// SETTO UDS (Revisione del 24/10/2014)
		if (!isRequestParameterNullObj("tipoUDS") && !getRequestStringParameter("tipoUDS").equals("-")) {
			String lTipoUds = getRequestStringParameter("tipoUDS");
			String lComuneUds = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);
			String lCodUffUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUds, lComuneUds);

			NotificaModel lNotUffUDS = new NotificaModel();

			lNotUffUDS.setCodEsito("-");
			lNotUffUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotUffUDS.setDataInserimento(DateUtils.getSysDate());
			lNotUffUDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			// 01/12/2014 Si Imposta il Codice Tipo Notifica in base all'autorità destinazione.
			lNotUffUDS.setCodTipoNotifica("MS");
			DecodificheModel lDecMod = new DecodificheModel();
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			lDecMod.setContesto("TIPO_NOTIFICA");
			lDecMod.setCodiceAlternativo(lTipoUds);
			lDecMod = lDecodifiche.ExRicercaDecodificheByAbbByHigh(lDecMod);
			if (lDecMod != null && lDecMod.getCode() != null && lDecMod.getCode().length() > 0)
				lNotUffUDS.setCodTipoNotifica(lDecMod.getCode());

			lNotUffUDS.setDataInvio(lDataTrasmissione);

			lNotUffUDS.setUffCodUfficio(lCodUffUDS);

			lNotificheArray.add(lNotUffUDS);
		}

		String lCodiceUffPM = "";

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_COD_UFFICIO_PM)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_COD_UFFICIO_PM) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_COD_UFFICIO_PM).equals("-")
				&& !isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM) != null) {

			lCodiceUffPM = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiNotifica.CAMPO_COD_UFFICIO_PM),
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM));

			NotificaModel lNotPM = new NotificaModel();

			lNotPM.setCodTipoNotifica("N");
			lNotPM.setDataInvio(lDataTrasmissione);
			lNotPM.setCodEsito("-");
			lNotPM.setCodOperatoreInserimento(lCodiceOperatore);
			lNotPM.setDataInserimento(DateUtils.getSysDate());
			lNotPM.setCodUfficioInserimento(lCodiceUfficio);
			lNotPM.setUffCodUfficio(lCodiceUffPM);

			lNotificheArray.add(lNotPM);

		}

		NotificaModel[] lNotifiche = (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);

		// Pena residua (04/11/2014 va impostata = null se assente)
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)) {
			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			lPenaRes.setIdPenaResidua(lIdPenaRes);

			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		} else {
			lPenaRes = null;
		}
		// ---> INSERIMENTO RICHIESTA
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		lEve.setNotifiche(lNotifiche);
		lRetModel = lCtrl.ExInserisciOENotifica(lEve, lPenaRes);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActDettaglioRichiestaAccertaPericoloSociale&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	} // Chiude processRequest

} // Chiude Action