package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;

/**
 * Classe Action per la modifica della trasmissione atti per l'esecuzione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActModificaTrasmissioneAttiEsecuzione extends ActionSiap
		implements ICostantiSanzioneSostitutiva {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// recupero l'ID Evento dalla form
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// instanzio nuovo modello
		EventoNotificaModel enm = new EventoNotificaModel();
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = ie.ExRicercaEventoByKey(idEvento);
		enm.setEvento(em);

		if (isRequestChecked("ritrasmissione"))
			enm.getEvento().setCodMotivo("1313");
		else
			enm.getEvento().setCodMotivo("1312");

		Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		enm.getEvento().setDataEmissione(dataEmissione);

		UfficioModel um = getUfficioUtenteConnesso();
		enm.getEvento().setIdEvento(idEvento);
		enm.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		enm.getEvento().setCodLuogoEmittente(um.getCodComune());
		enm.getEvento().setCodUfficioEmittente(um.getCodUfficio());
		enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioAggiornamento(um.getCodUfficio());
		enm.getEvento().setCodMagistrato(calcolaMagistrato());
		// recupero le notifiche
		enm.setNotifiche(getNotifiche());
		// eseguo la modifica
		ie.ExModificaEventoNotifiche(enm);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioTrasmissioneAttiEsecuzione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + idEvento;
	}

	/**
	 * Imposta le notifiche per l'ordine di ingiunzione
	 *
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] getNotifiche() throws F3BException {

		// info per il log
		siesLogger.info("getNotifiche(): inizio");

		String codUtenteConnesso = getCodUtenteConnesso();
		String codUfficioUtenteConnesso = getCodUfficioUtenteConnesso();
		Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date dataTrasmissione = dataEmissione;

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO))
			dataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		ArrayList<NotificaModel> nmArray = new ArrayList<>();

		String[] arrayDestinatari = null;
		String[] arraySedeDestinatari = null;
		String[] arrayNote = null;
		String[] avvocati = null;
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA))
			arrayDestinatari = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE))
			arraySedeDestinatari = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE))
			arrayNote = getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO))
			avvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

		// Dimensione dell'Array di Notifiche...
		// Gli Avvocati più Una Notifica di Esecuzione
		int contNotifiche = 0;
		int numAvvNotifiche = Utils.isPresent(avvocati) ? avvocati.length : 0;
		if (arrayDestinatari != null && arrayDestinatari[0].compareTo("-") == 0)
			numAvvNotifiche -= 1;

		// ===================================================
		// Notifica al condannato
		// ===================================================
		{
			String sedeDestinatario_E = null;
			String destinatario_EAE = null;
			String note_E = null;

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
				destinatario_EAE = getRequestStringParameter(
						ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
				sedeDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			}

			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
				note_E = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			if (!Utils.isNullObj(destinatario_EAE) && !"-".equals(destinatario_EAE)) {
				NotificaModel nm = new NotificaModel();
				nm.setCodTipoNotifica("NC");
				nm.setDataInvio(dataTrasmissione);
				nm.setCodEsito("-");
				nm.setCodOperatoreInserimento(codUtenteConnesso);
				nm.setDataInserimento(DateUtils.getSysDate());
				nm.setCodUfficioInserimento(codUfficioUtenteConnesso);
				nm.setNote(note_E);

				AutoritaEsternaModel aem = new AutoritaEsternaModel();
				aem.setCodTipoAutorita(destinatario_EAE);
				ComuneModel cm = new ComuneModel(getCodComuneByDescrFlagVal(sedeDestinatario_E));
				aem.setCodSede(cm.getCodComune());
				aem.setCodOperatoreInserimento(codUtenteConnesso);
				aem.setCodUfficioInserimento(codUfficioUtenteConnesso);
				aem.setDataInserimento(DateUtils.getSysDate());
				nm.setIstDetIdIstitutoDetenzione("");
				nm.setAutoritaEsterna(aem);

				nmArray.add(nm);
			}
		}

		// ===================================================
		// Notifica ad Istituto Detenzione
		// ===================================================
		{
			String destinatario_IST = null;
			String note_IST = null;
			if (!isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				destinatario_IST = getRequestStringParameter(
						ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				destinatario_IST = getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_IST))
				note_IST = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_IST);

			if (!Utils.isNullObj(destinatario_IST) && !"-".equals(destinatario_IST)) {
				NotificaModel nm = new NotificaModel();
				nm.setCodTipoNotifica("E");
				nm.setDataInvio(dataTrasmissione);
				nm.setCodEsito("-");
				nm.setCodOperatoreInserimento(codUtenteConnesso);
				nm.setDataInserimento(DateUtils.getSysDate());
				nm.setCodUfficioInserimento(codUfficioUtenteConnesso);
				nm.setNote(note_IST);
				nm.setIstDetIdIstitutoDetenzione(destinatario_IST);

				nmArray.add(nm);
			}
		}

		// ===================================================
		// Notifica altro destinatario
		// ===================================================
		{
			String sedeDestinatario_C = null;
			String destinatario_AEC = null;
			String note_C = null;

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)) {
				destinatario_AEC = getRequestStringParameter(
						ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
				sedeDestinatario_C = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
			}
			// indirizzo
			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_C))
				note_C = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_C);

			if (!Utils.isNullObj(destinatario_AEC) && !"-".equals(destinatario_AEC)) {
				NotificaModel nm = new NotificaModel();
				nm.setCodTipoNotifica("AA");
				nm.setDataInvio(dataTrasmissione);
				nm.setCodEsito("-");
				nm.setCodOperatoreInserimento(codUtenteConnesso);
				nm.setDataInserimento(DateUtils.getSysDate());
				nm.setCodUfficioInserimento(codUfficioUtenteConnesso);
				nm.setNote(note_C);

				AutoritaEsternaModel aem = new AutoritaEsternaModel();
				aem.setCodTipoAutorita(destinatario_AEC);
				ComuneModel cm = new ComuneModel(getCodComuneByDescrFlagVal(sedeDestinatario_C));
				aem.setCodSede(cm.getCodComune());
				aem.setCodOperatoreInserimento(codUtenteConnesso);
				aem.setCodUfficioInserimento(codUfficioUtenteConnesso);
				aem.setDataInserimento(DateUtils.getSysDate());
				nm.setIstDetIdIstitutoDetenzione("");
				nm.setAutoritaEsterna(aem);

				nmArray.add(nm);
			}
		}

		// ===================================================
		// Notifica agli avvocati del Condannato
		// ===================================================
		// Notifiche all'avvocato
		while (contNotifiche < numAvvNotifiche) {
			NotificaModel nm = new NotificaModel();
			nm.setCodTipoNotifica("ND");
			nm.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(avvocati[contNotifiche]));
			nm.setNote(arrayNote[contNotifiche]);
			nm.setDataInvio(dataTrasmissione);
			nm.setCodEsito("-");
			nm.setCodOperatoreInserimento(codUtenteConnesso);
			nm.setDataInserimento(DateUtils.getSysDate());
			nm.setCodUfficioInserimento(codUfficioUtenteConnesso);

			AutoritaEsternaModel aem = new AutoritaEsternaModel();
			aem.setCodTipoAutorita(arrayDestinatari[contNotifiche]);

			ComuneModel cm = new ComuneModel(getCodComuneByDescr(arraySedeDestinatari[contNotifiche]));

			aem.setCodSede(cm.getCodComune());
			aem.setCodOperatoreInserimento(codUtenteConnesso);
			aem.setCodUfficioInserimento(codUfficioUtenteConnesso);
			aem.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			nm.setAutoritaEsterna(aem);
			contNotifiche++;
			nmArray.add(nm);
		}

		// Notifica UDS
		NotificaModel nm = new NotificaModel();
		if (!isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO)
				&& getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO) != null
				&& !getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO).equals("")) {
			String tipoUfficio = "UDS";
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO) != null
					&& !"".equals(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)))
				tipoUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
			String codUfficio = getCodUfficioByCodTipoUfficioDescrComune(tipoUfficio,
					getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO));
			nm.setCodEsito("-");
			nm.setCodOperatoreInserimento(getCodUtenteConnesso());
			nm.setDataInserimento(DateUtils.getSysDate());
			nm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			nm.setCodTipoNotifica("UDS".equals(tipoUfficio) ? "MS" : "MM");
			nm.setDataInvio(dataTrasmissione);
			nm.setUffCodUfficio(codUfficio);
		}
		nmArray.add(nm);

		// info per il log
		siesLogger.info("getNotifiche(): fine");

		// valore di ritorno
		return nmArray.toArray(new NotificaModel[0]);
	}

}