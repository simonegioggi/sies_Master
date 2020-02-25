package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.istruttoria.action.ICostantiIstruttoria;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciRichiestaPM
 * </p>
 * <p>
 * Description: ActInserisciRichiestaPM
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciRichiestaPM extends ActionSiap implements ICostantiIstruttoria {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		int lSize = 0;
		EventoNotificaModel lEve = new EventoNotificaModel();

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		lEve.getEvento().setCodTipoEvento("05"); // Tipo Evento = RIchiesta Istruttoria
		lEve.getEvento().setCodTipoProvvedimento("-"); // Tipo Provvedimento = Ordinanza

		lEve.getEvento().setCodMotivo("0701"); // 0701

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

		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();

		String lCodice = getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiIstruttoria.AUTORITA_DESTINATARIO),
				getRequestStringParameter(ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO));

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
		/* FINIRE */

		NotificaModel lNotifiche[] = new NotificaModel[1];
		NotificaModel lNot = new NotificaModel();

		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(DateUtils.getSysDate());
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(lUff.getCodUfficio());
		lNot.setUffCodUfficio(lCodice);

		// String lTipoDoc = getRequestStringParameter(ICostantiIstruttoria.TIPO_DOCUMENTO);
		// if (getRequestBigDecimalParameter(ICostantiIstruttoria.NUMERO_COPIE).intValue() > 1 )
		// {
		// if (lTipoDoc.startsWith("ESTR"))
		// lTipoDoc = "ESTRATTI";
		// else
		// lTipoDoc = "COPIE INTEGRALI";
		// }
		// String lTestoNote = getRequestStringParameter(ICostantiIstruttoria.NUMERO_COPIE) + " " +
		// lTipoDoc + " della SENTENZA " +
		// getRequestStringParameter(ICostantiIstruttoria.ESTREMI_SENTENZA);

		String lTestoNote = getRequestStringParameter(ICostantiIstruttoria.TIPO_DOCUMENTO);

		lNot.setNote(lTestoNote);

		lNotifiche[0] = lNot;

		String[] lCampiNoteReq = getRequestStringParameters(CAMPO_NOTE);

		// Popola il model campo note aggiuntive
		if (lCampiNoteReq != null) {
			Vector lCampiNote = new Vector();
			lSize = lCampiNoteReq.length;

			for (int x = 0; x < lSize; x++) {
				if (!lCampiNoteReq[x].equals("")) {
					CampoNotaModel lCampoNotaMod = new CampoNotaModel();
					lCampoNotaMod.setDescr(lCampiNoteReq[x]);
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
				+ "=siap.sige.richiestaatti.action.ActDettaglioRichiestaPM&"
				+ ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE + "="
				+ lRetModel.getIdProvvedimentoSige() + "&modalita=I";

		return lPage;
	}

}