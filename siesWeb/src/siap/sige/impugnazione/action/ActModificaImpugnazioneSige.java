package siap.sige.impugnazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.model.EventoModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActModificaImpugnazioneSige
 * </p>
 * <p>
 * Description: Classe Action per la modifica dell'Impugnazione Sige
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
public class ActModificaImpugnazioneSige extends ActionSige implements ICostantiImpugnazioneSige {

	/**
	 * Azione di Inserimento del Impugnazione
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		BigDecimal idImpugnazione = getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE);
		IImpugnazioneSige lCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
		ImpugnazioneSigeModel impugnazione = lCtrl.ExRicercaImpugnazioneByKey(idImpugnazione);
		impugnazione.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		impugnazione.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		impugnazione.setDataAggiornamento(DateUtils.getSysDate());
		if (!super.isRequestParameterNullEmptyObj(CAMPO_SOGGETTO_IMPUGNANTE))
			impugnazione.setSoggettoImpugnante(getRequestStringParameter(CAMPO_SOGGETTO_IMPUGNANTE));

		if (!super.isRequestParameterNullEmptyObj(CAMPO_GIORNO_DATA_RICORSO))
			impugnazione.setDataRicorso(getRequestDateParameter(CAMPO_ANNO_DATA_RICORSO,
					CAMPO_MESE_DATA_RICORSO, CAMPO_GIORNO_DATA_RICORSO));

		if (!super.isRequestParameterNullEmptyObj(CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA))
			impugnazione.setDataArrivoCancelleria(getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA,
					CAMPO_MESE_DATA_ARRIVO_CANCELLERIA, CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA));

		if (!super.isRequestParameterNullEmptyObj(CAMPO_COD_AUTORITA_DESTINATARIA))
			impugnazione.setCodAutoritaDestinataria(super.getRequestStringParameter(
					ICostantiImpugnazioneSige.CAMPO_COD_AUTORITA_DESTINATARIA));

		if (!super.isRequestParameterNullEmptyObj(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI))
			impugnazione.setDataTrasmissioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		impugnazione.setAnnotazione(getRequestStringParameter(CAMPO_NOTE));

		impugnazione.setNotifiche(this.getNotifiche(impugnazione));
		ImpugnazioneSigeModel llImpModRet = lCtrl.ExModificaImpugnazione(impugnazione);
		setRequestAttribute("impugnazione", llImpModRet);

		// Prepara la pagina di destinazione, puntando all'azione di dettaglio.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige");
		lPage.setParameter(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE,
				"" + llImpModRet.getIdImpugnazioneSige());
		return "" + lPage;
	}

	private Vector<NotificaModel> getNotifiche(ImpugnazioneSigeModel impugnazione) throws F3BException {

		Vector<NotificaModel> notifiche = new Vector<>();
		if (super.isRequestParameterNullObj(CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO))
			return notifiche;

		FascicoloSigeEstesoModel fascicoloEsteso = super.getFascicoloSigeEstesoInSessione();
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// Ticket#201911050112 — RIF. Vs ticket 20191104014
		EventoModel eventoNotifica = null;
		if (impugnazione.getProvvedimentoSigeGenerato() != null
				&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento() != null) {
			eventoNotifica = impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento();
		}

		String codDestinatarioPubblicoMinistero = super.getRequestStringParameter(
				CAMPO_COD_DESTINATARIO_PUBBLICO_MINISTERO);
		// Ticket#201911050112 — RIF. Vs ticket 20191104014
		if (!codDestinatarioPubblicoMinistero.equals("-") && eventoNotifica != null) {
			NotificaModel notificaPm = new NotificaModel();
			notificaPm.setEveIdEvento(eventoNotifica.getIdEvento());
			notificaPm.setCodTipoNotifica("N");
			notificaPm.setDataInvio(impugnazione.getDataDecisione());
			notificaPm.setCodOperatoreInserimento(lCodiceOperatore);
			notificaPm.setDataInserimento(DateUtils.getSysDate());
			notificaPm.setCodUfficioInserimento(lCodiceUfficio);
			notificaPm.setCodEsito("-");
			notificaPm.setUffCodUfficio("-");
			notificaPm.setSogIdSoggetto(fascicoloEsteso.getFascicoloSige().getSogIdSoggetto());
			String descrComuneSedeUfficio = super.getRequestStringParameter(
					CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO);
			String codiceUfficioDestinatario = getCodUfficioByCodTipoUfficioDescrComune(
					codDestinatarioPubblicoMinistero, descrComuneSedeUfficio);
			notificaPm.setUffCodUfficio(codiceUfficioDestinatario);
			notifiche.add(notificaPm);
		}

		String codDestinatarioUfficioRecuperoCredito = super.getRequestStringParameter(
				CAMPO_COD_DESTINATARIO_UFFICIO_RECUPERO_CREDITI);
		// Ticket#201911050112 — RIF. Vs ticket 20191104014
		if (!codDestinatarioUfficioRecuperoCredito.equals("-") && eventoNotifica != null) {
			NotificaModel notificaRecupero = new NotificaModel();
			notificaRecupero.setEveIdEvento(eventoNotifica.getIdEvento());
			notificaRecupero.setCodTipoNotifica("N");
			notificaRecupero.setDataInvio(impugnazione.getDataDecisione());
			notificaRecupero.setCodOperatoreInserimento(lCodiceOperatore);
			notificaRecupero.setDataInserimento(DateUtils.getSysDate());
			notificaRecupero.setCodUfficioInserimento(lCodiceUfficio);
			notificaRecupero.setCodEsito("-");
			notificaRecupero.setUffCodUfficio("-");
			notificaRecupero.setSogIdSoggetto(fascicoloEsteso.getFascicoloSige().getSogIdSoggetto());

			AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
			lAutorita.setCodTipoAutorita(codDestinatarioUfficioRecuperoCredito);
			lAutorita.setCodSede(super.getCodComuneByDescrFlagVal(
					super.getRequestStringParameter(CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI))
							.getCodComune());
			lAutorita.setDescrSede(
					super.getRequestStringParameter(CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI));
			lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
			lAutorita.setCodUfficioInserimento(lCodiceUfficio);
			lAutorita.setDataInserimento(DateUtils.getSysDate());
			notificaRecupero.setAutoritaEsterna(lAutorita);

			notifiche.add(notificaRecupero);
		}
		return notifiche;
	}

}