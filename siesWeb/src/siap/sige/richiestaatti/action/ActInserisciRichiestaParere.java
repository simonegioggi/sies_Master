package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.motivazioneprovvedimento.action.ICostantiMotivazioneProvvedimento;
import siap.sige.provvedimento.action.ActInserisciDecretoInammissibilita;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

public class ActInserisciRichiestaParere extends ActInserisciDecretoInammissibilita
		implements ICostantiRichiestaAtti, ICostantiMotivazioneProvvedimento, ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		// Preleva dati dalla form.
		String lDescrComune = getRequestStringParameter(CAMPO_SEDE);
		String lNote = "";
		String lCodTipoUfficioDest = getRequestStringParameter(CAMPO_COD_DESTINATARIO);
		Date lDataEmissione = getRequestDateParameter(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE);
		String[] lCampiNoteReq = null;
		int lSize = 0;

		// Lettura Campo Note
		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO)) {
			lCampiNoteReq = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO);
			setRequestAttribute("campiNote", lCampiNoteReq);
		}
		// Controlla e preleva il codice comune di sede.
		// Tale metodo ereditato, effettua un'accesso alla base dati ove verifica e ritorna il codice del
		// comune.
		/* String lCodSede = */getCodComuneByDescr(lDescrComune).getCodComune();

		// Preleva il codice d'ufficio per la coppia comune e codice tipo ufficio.
		String lCodice = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioDest, lDescrComune);

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		EventoNotificaModel lEve = new EventoNotificaModel();
		// lEve.getEvento().setCodTipoEvento(ICostantiRichiestaAtti.CODTIPOEVENTO); //Tipo Evento = RIchiesta
		// Istruttoria
		// lEve.getEvento().setCodTipoEvento("05");
		lEve.getEvento().setCodTipoEvento("08"); // Richiesta Parere
		lEve.getEvento().setCodTipoProvvedimento("-"); // Tipo Provvedimento = Ordinanza
		// 05/08/2010 lEve.getEvento().setCodMotivo("0751"); // PARERE 0751, 0750 Parere Inammissibilità
		lEve.getEvento().setCodMotivo("0753"); // 05/08/2010
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

		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();

		ProvvedimentoSigeModel lProvvedimentoSigeModel = new ProvvedimentoSigeModel();
		lProvvedimentoSigeModel.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		lProvvedimentoSigeModel.setFasIdFascicoloSige(lProvvedimentoSigeModel.getFasIdFascicoloSige());
		lProvvedimentoSigeModel.setDataEmissione(lDataEmissione); // DATA EMISSIONE DI EVENTO
		lProvvedimentoSigeModel.setCodTipoProvvedimento("52");
		lProvvedimentoSigeModel.setCodTipoProvvedimentoSige("11");
		lProvvedimentoSigeModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lProvvedimentoSigeModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lProvvedimentoSigeModel.setDataInserimento(DateUtils.getSysDate());
		lProvvedimentoSigeModel.setDefinitorio("N");
		lProvvedimentoSigeModel.setChiaveUfficio(getCodUfficioUtenteConnesso());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Provvedimento SIGE Valorizzato :" + lProvvedimentoSigeModel);

		NotificaModel lNotifiche[] = new NotificaModel[1];
		NotificaModel lNot = new NotificaModel();

		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(DateUtils.getSysDate());
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(lUff.getCodUfficio());
		lNot.setUffCodUfficio(lCodice);
		lNot.setNote(lNote);
		lNotifiche[0] = lNot;

		// Popola il model campo note aggiuntive
		if (lCampiNoteReq != null) {
			Vector lCampiNote = new Vector();
			lSize = lCampiNoteReq.length;

			for (int x = 0; x < lSize; x++) {
				if (!lCampiNoteReq[x].equals("")) {
					CampoNotaModel lCampoNotaMod = new CampoNotaModel();
					lCampoNotaMod.setDescr(lCampiNoteReq[x].toString());
					lCampoNotaMod.setCodOperatoreInserimento(lCodiceOperatore);
					lCampoNotaMod.setCodUfficioInserimento(lCodiceUfficio);
					lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
					lCampiNote.add(lCampoNotaMod);
				}
			}

			// Se esistono campi di note aggiuntive
			// inserisce il contenuto del vettore nell'eventoModel
			if (lCampiNote.size() != 0)
				lEve.setCampoNote((CampoNotaModel[]) lCampiNote.toArray(new CampoNotaModel[0]));
		}

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche(lNotifiche);

		// Chiamata al Controller per gli inserimenti.
		ProvvedimentoSigeModel lRetModel = lProvCtrl.ExInserisciEventoNotificaProv(lEve,
				lProvvedimentoSigeModel);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.richiestaatti.action.ActLoadDettaglioRichiestaParere&"
				+ ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE + "="
				+ lRetModel.getIdProvvedimentoSige() + "&modalita=I";

		return "" + lPage;
	}

}