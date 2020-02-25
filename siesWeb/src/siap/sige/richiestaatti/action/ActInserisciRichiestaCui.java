package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.istruttoria.action.ICostantiIstruttoria;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciRichiestaCodiceCui
 * </p>
 * <p>
 * Description: Act per l'inserimento della Richiesta Codice CUI
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciRichiestaCui extends ActionSiap implements ICostantiIstruttoria {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// SEDE di
		// ComuneModel lCom
		// =this.getCodComuneByDescr(getRequestStringParameter(ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO));
		// String lSede = lCom.getCodComune();
		String lSede = getRequestStringParameter(ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO);
		// *************************************
		// *************************************
		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("05"); // Tipo Evento = RIchiesta Istruttoria
		lEve.getEvento().setCodTipoProvvedimento("-"); // Tipo Provvedimento = Ordinanza

		/*
		 * in base a quanto indicato indico il template di stampa ed imposto il codice motivo secondo il
		 * criterio ListaTemplate=0 --> Richiesta Codice CUI (codice motivo 0557) ListaTemplate=1 -->
		 * Richiesta Cartellino Fotosegnaletico (codice motivo 0578)
		 */
		String codMotivo = null;
		if (!isRequestParameterNullObj("ListaTemplate")) {
			// setRequestAttribute("ListaTemplate", getRequestStringParameter("ListaTemplate"));
			// codMotivo = getRequestStringParameter("ListaTemplate").equals("0")?"0557":"0578";
			if (getRequestStringParameter("ListaTemplate").equals("0")) {
				codMotivo = "0557";
			} else if (getRequestStringParameter("ListaTemplate").equals("1")) {
				codMotivo = "0578";
			}
		}

		// Codice motivo da CG_REF_CODES....
		// lEve.getEvento().setCodMotivo("0557");
		lEve.getEvento().setCodMotivo(codMotivo);

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
		// lEve.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);

		UfficioModel lUff = this.getUfficioUtenteConnesso();

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		// lEve.getEvento().setFlagDocumentoRegistrato("N");
		// ***************************************************

		// ANGELA
		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");
		ProvvedimentoSigeModel lProvvedimentoSigeModel = new ProvvedimentoSigeModel();
		lProvvedimentoSigeModel.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		lProvvedimentoSigeModel.setFasIdFascicoloSige(lProvvedimentoSigeModel.getFasIdFascicoloSige());
		lProvvedimentoSigeModel.setDataEmissione(lDataEmissione); // DATA EMISSIONE DI EVENTO
		lProvvedimentoSigeModel.setCodTipoProvvedimento("52");
		lProvvedimentoSigeModel.setCodTipoProvvedimentoSige("52");
		lProvvedimentoSigeModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lProvvedimentoSigeModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lProvvedimentoSigeModel.setDataInserimento(DateUtils.getSysDate());
		lProvvedimentoSigeModel.setDefinitorio("N");
		lProvvedimentoSigeModel.setChiaveUfficio(getCodUfficioUtenteConnesso());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Provvedimento SIGE Valorizzato :" + lProvvedimentoSigeModel);
		// ANGELA

		// NotificaModel lNotifiche[] = new NotificaModel[1];
		ArrayList lNotArr = new ArrayList();
		NotificaModel lNot = new NotificaModel();

		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(lDataEmissione);
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(lUff.getCodUfficio());

		// Campo Notizie di reato (viene inserito nella tabella CAMPO_NOTA solo se è stato compilato)
		String lTestoNote = getRequestStringParameter(ICostantiIstruttoria.CAMPO_NOTE);
		if (!lTestoNote.trim().equals("")) {
			Vector lCampiNote = new Vector();
			CampoNotaModel lCampoNotaMod = new CampoNotaModel();
			lCampoNotaMod.setDescr(lTestoNote);
			lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
			lCampoNotaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampoNotaMod.setCodUfficioInserimento(lUff.getCodUfficio());
			// lCampoNotaMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			lCampiNote.add(lCampoNotaMod);

			lEve.setCampoNote((CampoNotaModel[]) lCampiNote.toArray(new CampoNotaModel[0]));
		}

		// Setto il NotificaModel
		NotificaModel lNotDA = new NotificaModel();
		boolean insAut = false;
		if (!isRequestParameterNullObj(ICostantiIstruttoria.FLAG_GABINETTO)
				&& getRequestStringParameter(ICostantiIstruttoria.FLAG_GABINETTO) != null) {

			AutoritaEsternaModel lAutEstMod = new AutoritaEsternaModel();

			lAutEstMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiIstruttoria.AUTORITA_DESTINATARIO));
			lAutEstMod.setCodSede(lSede);
			lAutEstMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAutEstMod.setDataInserimento(DateUtils.getSysDate());
			lAutEstMod.setCodUfficioInserimento(lUff.getCodUfficio());

			lNot.setAutoritaEsterna(lAutEstMod);
			// Inserisco il campo indirizzo nelle note della notifica
			lNot.setNote(getRequestStringParameter(ICostantiIstruttoria.INDIRIZZO_DESTINATARIO));

			lNotArr.add(lNot);
			insAut = true;
		}

		if (!isRequestParameterNullObj(ICostantiIstruttoria.FLAG_QUESTURA)
				&& getRequestStringParameter(ICostantiIstruttoria.FLAG_QUESTURA) != null) {

			AutoritaEsternaModel lAutEstDAMod = new AutoritaEsternaModel();

			lAutEstDAMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiIstruttoria.AUT_QUESTURA_DIVISIONE_ANTICRIMINE));
			lAutEstDAMod.setCodSede(lSede);
			lAutEstDAMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAutEstDAMod.setDataInserimento(DateUtils.getSysDate());
			lAutEstDAMod.setCodUfficioInserimento(lUff.getCodUfficio());

			lNotDA.setAutoritaEsterna(lAutEstDAMod);
			// Inserisco il campo indirizzo nelle note della notifica
			lNotDA.setNote(getRequestStringParameter(ICostantiIstruttoria.INDIRIZZO_DESTINATARIO));

			lNotArr.add(lNotDA);
			insAut = true;
		}
		// se è stata inserita almeno una autorità completo i campi per la notifica
		if (insAut) {
			lNotDA.setCodTipoNotifica("N");
			lNotDA.setDataInvio(lDataEmissione);
			lNotDA.setCodEsito("-");
			lNotDA.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotDA.setDataInserimento(DateUtils.getSysDate());
			lNotDA.setCodUfficioInserimento(lUff.getCodUfficio());
		}

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche((NotificaModel[]) lNotArr.toArray(new NotificaModel[0]));

		// lEve.getEvento().setFlagDocumentoRegistrato("N");
		// IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeModel lRetModel = lProvCtrl.ExInserisciEventoNotificaProv(lEve,
				lProvvedimentoSigeModel);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.richiestaatti.action.ActDettaglioRichiestaCui&"
				+ ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE + "="
				+ lRetModel.getIdProvvedimentoSige() + "&modalita=I";

		// String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.istruttoria.action.ActDettaglioRichiestaCodiceCui&" +
		// ICostantiEvento.CAMPO_ID_EVENTO + "=" +
		// lRetModel.getEvento().getIdEvento() + "&modalita=I";

		if (!isRequestParameterNullObj("ListaTemplate")) {
			setRequestAttribute("ListaTemplate", getRequestStringParameter("ListaTemplate"));
		}

		// IMPOSTAZIONI PER FUNZIONALITà BACK
		String lAzione = "siap.sige.richiestaatti.action.ActLoadInserisciRichiestaCui";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

		return lPage;

	}

}