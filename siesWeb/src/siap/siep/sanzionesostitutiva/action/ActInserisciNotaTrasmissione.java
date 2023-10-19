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
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione di inserimento della Comunicazione Nota Trasmissione Bollettini - Rate successive alla prima
 *
 * @author d.fiorletta #since MEV_2023-33
 */
public class ActInserisciNotaTrasmissione extends ActionSiap implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: inizio");
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		EventoModel lEve = getEventoNotaTrasmissione();

		// Lego la nota di trasmissione all'OI
		BigDecimal idEventoOI = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		lEve.setEveIdEvento(idEventoOI);

		// Recupero le notifiche
		NotificaModel[] lNotifiche = this.getNotificheNotaTrasmissione();
		lEveNot.setEvento(lEve);
		lEveNot.setNotifiche(lNotifiche);

		ISanzioneSostitutiva lCtrlSS = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		EventoNotificaModel lRetModel = lCtrlSS.exInserisciNotaTrasmissione(lEveNot);

		String lPage = null;

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioNotaTrasmissione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return lPage;
	}

	/**
	 * 
	 * @return
	 * @throws F3BException
	 */
	protected EventoModel getEventoNotaTrasmissione() throws F3BException {

		// info per il log
		siesLogger.info("getEventoNotaTrasmissione(): inizio");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel lEve = new EventoModel();

		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEve.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEve.setCodTipoProvvedimento("12"); // Tipo Provvedimento = COMUNICAZIONE
		lEve.setCodMotivo("1306"); // 1306 - Nota trasmissione Bollettini rate successive alla prima

		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);

		lEve.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEve.setCodUfficioEmittente(getCodUfficioUtenteConnesso());

		// Magistrato
		lEve.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.setCodEsito("-");
		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");

		lEve.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// info per il log
		siesLogger.info("getEventoNotaTrasmissione(): fine");

		return lEve;
	}

	/**
	 * Imposta le notifiche per la nota trasmissione
	 *
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected NotificaModel[] getNotificheNotaTrasmissione() throws F3BException {

		// info per il log
		siesLogger.info("getNotificheNotaTrasmissione(): inizio");

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		Date lTrasmissione = lDataEmissione;
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO))
			lTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		ArrayList lNotificheArray = new ArrayList();

		// ===================================================
		// Notifica per l'esecuzione
		// ===================================================
		{
			String lSedeDestinatario_E = null;
			String lDestinatario_E = null;
			String lDestinatario_EAE = null;
			String lNote_E = null;

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
				lDestinatario_EAE = this
						.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
				lSedeDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			}

			if (!isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				lDestinatario_E = this
						.getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				lDestinatario_E = this.getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
				lNote_E = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel lNotMod = new NotificaModel();
			lNotMod.setCodTipoNotifica("E"); // ???????
			lNotMod.setDataInvio(lTrasmissione);
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setNote(lNote_E);

			// Prima notifica esecuzione
			if (lDestinatario_E != null)
				lNotMod.setIstDetIdIstitutoDetenzione(lDestinatario_E);

			if (lDestinatario_EAE != null) {
				AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

				lAutMod.setCodTipoAutorita(lDestinatario_EAE);
				ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
				lAutMod.setCodSede(lComModel.getCodComune());
				lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
				lAutMod.setCodUfficioInserimento(lCodiceUfficio);
				lAutMod.setDataInserimento(DateUtils.getSysDate());
				lNotMod.setIstDetIdIstitutoDetenzione("");

				lNotMod.setAutoritaEsterna(lAutMod);
			}

			lNotificheArray.add(lNotMod);
		}

		// info per il log
		siesLogger.info("getNotificheNotaTrasmissione(): fine");

		// valore di ritorno
		return (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
	}

}