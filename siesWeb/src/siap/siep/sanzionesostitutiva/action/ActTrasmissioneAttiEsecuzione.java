package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;

/**
 * Classe Action per la trasmissione atti per l'esecuzione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActTrasmissioneAttiEsecuzione extends ActionSiap implements ICostantiSanzioneSostitutiva {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		EventoNotificaModel enm = new EventoNotificaModel();

		// Tipo Evento = Richiesta (poiché in sostanza l'evento che si trasmette corrisponde ad una Richiesta
		// di applicazione Sanzione Sostitutiva
		enm.getEvento().setCodTipoEvento("02");
		// Tipo Provvedimento = Trasmissione Atti
		enm.getEvento().setCodTipoProvvedimento("31");

		if (isRequestChecked("ritrasmissione"))
			enm.getEvento().setCodMotivo("0941");
		else
			enm.getEvento().setCodMotivo("1312");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());

		Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		enm.getEvento().setDataEmissione(dataEmissione);

		UfficioModel um = getUfficioUtenteConnesso();
		enm.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		enm.getEvento().setCodLuogoEmittente(um.getCodComune());
		enm.getEvento().setCodUfficioEmittente(um.getCodUfficio());
		enm.getEvento().setDataInserimento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioInserimento(um.getCodUfficio());
		enm.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		enm.getEvento().setCodEsito("-");
		enm.getEvento().setCodLuogoDestinatario("-");
		enm.getEvento().setCodUfficioDestinatario("-");
		enm.getEvento().setCodTipoUfficioDestinatario("-");
		enm.getEvento().setFlagStampaSiep("S");
		enm.getEvento().setFlagVideoSiep("S");
		enm.getEvento().setCodMagistrato(calcolaMagistrato());

		enm.setNotifiche(getNotifiche());

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enmRet = ie.ExInserisciEventoNotifica(enm);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioTrasmissioneAttiEsecuzione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + enmRet.getEvento().getIdEvento() + "&modalita=I";

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return lPage;
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

		String[] arrayDestinatari = getRequestStringParameters(
				ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		String[] arraySedeDestinatari = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		String[] arrayNote = getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
		String[] avvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

		// Dimensione dell'Array di Notifiche...
		// Gli Avvocati più Una Notifica di Esecuzione
		int contNotifiche = 0;
		int numAvvNotifiche = avvocati.length;
		if (arrayDestinatari[0].compareTo("-") == 0)
			numAvvNotifiche -= 1;

		// ===================================================
		// Notifica per l'esecuzione
		// ===================================================
		{
			String sedeDestinatario_E = null;
			String destinatario_E = null;
			String destinatario_EAE = null;
			String note_E = null;

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
				destinatario_EAE = getRequestStringParameter(
						ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
				sedeDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			}

			if (!isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				destinatario_E = getRequestStringParameter(
						ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				destinatario_E = getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
				note_E = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel nm = new NotificaModel();
			nm.setCodTipoNotifica("E");
			nm.setDataInvio(dataTrasmissione);
			nm.setCodEsito("-");
			nm.setCodOperatoreInserimento(codUtenteConnesso);
			nm.setDataInserimento(DateUtils.getSysDate());
			nm.setCodUfficioInserimento(codUfficioUtenteConnesso);
			nm.setNote(note_E);

			// Prima notifica esecuzione
			if (destinatario_E != null)
				nm.setIstDetIdIstitutoDetenzione(destinatario_E);

			if (destinatario_EAE != null) {
				AutoritaEsternaModel aem = new AutoritaEsternaModel();
				aem.setCodTipoAutorita(destinatario_EAE);
				ComuneModel cm = new ComuneModel(getCodComuneByDescrFlagVal(sedeDestinatario_E));
				aem.setCodSede(cm.getCodComune());
				aem.setCodOperatoreInserimento(codUtenteConnesso);
				aem.setCodUfficioInserimento(codUfficioUtenteConnesso);
				aem.setDataInserimento(DateUtils.getSysDate());
				nm.setIstDetIdIstitutoDetenzione("");
				nm.setAutoritaEsterna(aem);
			}

			nmArray.add(nm);
		}

		// ===================================================
		// Notifica agli avvocati del Condannato
		// ===================================================
		// Notifiche all'avvocato
		while (contNotifiche < numAvvNotifiche) {
			NotificaModel nm = new NotificaModel();
			nm.setCodTipoNotifica("N");
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
			nm.setCodTipoNotifica("E");
			nm.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			nm.setUffCodUfficio(codUfficio);
		}
		nmArray.add(nm);

		// info per il log
		siesLogger.info("getNotifiche(): fine");

		// valore di ritorno
		return nmArray.toArray(new NotificaModel[0]);
	}

}