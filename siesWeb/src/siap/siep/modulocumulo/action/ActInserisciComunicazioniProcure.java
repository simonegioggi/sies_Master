package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.controller.UfficioController;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.notifica.model.NotificaModel;

/**
 *
 * <p>
 * Title: ActInserisciComunicazioniProcure
 * </p>
 * <p>
 * Description: Inserimento Comunicazioni Cumulo agli uffici titolari dei fascicoli interessati
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: intersistemi Italia spa
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciComunicazioniProcure extends ActionModuloCumulo implements ICostantiModuloCumulo {

	/**
	 * Azione di Inserimento dell' EventoNotificaModel per la comunicazione agli Uffici destinatari
	 * selezionati nella form
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoriaCumulo = super.getDatiIstruttoria(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

		EventoModel lEveProvv = lCtrlEvento.ExRicercaEventoByKey(lIstruttoriaCumulo.getEveIdEventoProv());

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		isEventoNonValidatoPerFascicoloCumulato(lFascicoloModel.getIdFascicoloSiep());

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		// Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
		// ICostantiNotifica.CAMPO_MESE_DATA_INVIO,
		// ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodMotivo("0670");
		lEve.getEvento().setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEve.getEvento().setCodTipoProvvedimento("12"); // Tipo Provvedimento = COMUNICAZIONE
		lEve.getEvento().setIstruidIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
		// 26/03/2019 MEV70 - Si rendono non visibili le comunicazioni di Cumulo
		// lEve.getEvento().setFlagStampaSiep("S");
		// lEve.getEvento().setFlagVideoSiep("S");
		lEve.getEvento().setFlagStampaSiep("N");
		lEve.getEvento().setFlagVideoSiep("N");
		lEve.getEvento().setEveIdEvento(lEveProvv.getIdEvento()); // 29/03/2019 MEV70

		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.getEvento().setDataEmissione(lDataEmissione);

		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);

		lEve.getEvento().setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");
		lEve.getEvento().setCodMagistrato("-");

		// Aggiunte le Notifiche per gli Uffici Selezionati
		lEve = setNotificheUfficiSelezionati(lEve);

		EventoNotificaModel lRetModel = lCtrlEvento.ExInserisciEventoNotifica(lEve);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActDettaglioComunicazioniProcure&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&"
				+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
				+ lIstruttoriaCumulo.getIdIstruttoriaCumulo() + "&modalita=I";

		return lPage;
	}

	/**
	 *
	 * @return
	 */
	private EventoNotificaModel setNotificheUfficiSelezionati(EventoNotificaModel lEve) throws F3BException {
		// Evento già impostato
		EventoNotificaModel lEveNot = lEve;

		ArrayList lNotifiche = new ArrayList();

		// ==========================================================================
		// Carica i dati delle notifiche in base agli uffici selezionati
		// ==========================================================================

		// Id Uffici selezionati
		String[] lUfficiSelezionati = getRequestStringParameters(ICostantiUfficio.CAMPO_COD_UFFICIO);
		// String[] lTitoliSelezionati =
		// getRequestStringParameters(ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO);

		UfficioController lUffCtrl = new UfficioController();

		int lNumUfficiSelezionati = lUfficiSelezionati.length;
		for (int i = 0; i < lNumUfficiSelezionati; i++) {
			String lUffSel = lUfficiSelezionati[i].substring(0, lUfficiSelezionati[i].indexOf("-")).trim();
			String lTitSel = lUfficiSelezionati[i].substring(lUfficiSelezionati[i].indexOf("-") + 1).trim();

			UfficioModel lUffMod = null;
			lUffMod = lUffCtrl.ExRicercaUfficioByCod(lUffSel);
			// Gestione Notifica per ufficio selezionato
			if (lUffMod != null && lUffMod.getCodUfficio() != null) {
				NotificaModel lNotMod = new NotificaModel();

				lNotMod.setCodEsito("-");
				lNotMod.setCodTipoNotifica("E");
				lNotMod.setDataInvio(lEveNot.getEvento().getDataEmissione());

				lNotMod.setUffCodUfficio(lUffMod.getCodUfficio());

				// Se l'ufficio è di Sorveglianza non viene valorizzato il campo "CurIdCuratore",
				// utilizzato per gli uffici dell'esecuzione al fine di pilotare la stampa con le indicazioni
				// dei titoli e degli uffici che hanno emesso sentenza.
				String[] lUfficiSorv = new String[] { "UDS", "TDS", "UDSM", "TDSM" };
				if (!Arrays.asList(lUfficiSorv).contains(lUffMod.getCodTipoUfficio()))
					lNotMod.setCurIdCuratore(new BigDecimal(lTitSel));

				lNotMod.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
				lNotMod.setDataInserimento(lEveNot.getEvento().getDataInserimento());
				lNotMod.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

				lNotifiche.add(lNotMod);
			}
		}

		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// LogF3B.getLogger().debug("Evento = "+lEveNot.getEvento());
		// LogF3B.getLogger().debug("Numero Notifiche = "+lNotifiche.size());

		// Iterator lIterNotifiche = lNotifiche.iterator();
		// while (lIterNotifiche.hasNext()) {
		// NotificaModel lNotifica = (NotificaModel) lIterNotifiche.next();
		// // LogF3B.getLogger().debug("lNotifica = "+lNotifica);
		// }
		return lEveNot;
	}

	/*
	 * metodo specializzato per gli eventi del fascicolo cumulato SE CodTipoEvento = 05(Richiesta Istruttoria)
	 * NON DEVE FARE IL CONTROLLO
	 */
	protected void isEventoNonValidatoPerFascicoloCumulato(BigDecimal aIdFascCumulato) throws F3BException {
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrl.ExRicercaEventoByFascicoloSiepDescUfficioConnesso(aIdFascCumulato,
				getCodUfficioUtenteConnesso());

		if (lEveMod != null && lEveMod.getCodTipoEvento() != null
				&& !lEveMod.getCodTipoEvento().equals("05")) {
			if (lEveMod.getFlagDocumentoRegistrato() == null
					|| "N".equals(lEveMod.getFlagDocumentoRegistrato())) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Esiste un evento NON validato del fascicolo cumulato. Validarlo o cancellarlo e rieseguire la funzione.");
			}
		}
	}

}