package siap.sige.provvInterlocutori.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.istruttoria.action.ICostantiIstruttoria;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaLatitanzaIrreperibilita
 * </p>
 * <p>
 * Description: Classe Action per la modifica decreti
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class ActModificaLatitanzaIrreperibilita extends ActInserisciNominaPeriti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String[] mCheck = null;
	ProvvedimentoSigeModel mProvvedimento = null;
	Date mDataEmissione = null;

	public String processRequest() throws Exception {

		String lPage = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".processRequest: inizio");

		BigDecimal lIdEvento = null;

		// Aggiornamento dell'Evento e Inserimento della Notifica per ciascun destinatario.
		// Preparo il model EventoNotifica.
		lIdEvento = aggiornaProvvedimento();
		EventoNotificaModel lEveNot = new EventoNotificaModel();

		// Evento. Leggo l'evento collegato al decreto.
		EventoModel lEveMod = new EventoModel();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// BigDecimal lIdEvento = lDecMod.getIdEventoGenerato();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE)
				&& !isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE)
				&& !isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE))
			mDataEmissione = getRequestDateParameter(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE,
					ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE);
		else
			mDataEmissione = lEveMod.getDataEmissione();

		// Aggiorno l'Evento.
		lEveMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEveMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lEveMod.setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.setDataEmissione(mDataEmissione);

		// Imposto l'Evento nel'EventoNotificaModel.
		lEveNot.setEvento(lEveMod); //

		// Preparo le notifiche.
		/*
		 * Vector lNotifiche = leggiNotifiche(); if (lNotifiche != null && lNotifiche.size() >= 0)
		 * lEveNot.setNotifiche( (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
		 */

		// Aggiornamento dei dati sul DB
		lPage = inserisciDati(lEveNot);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".processRequest: fine");

		return lPage;
	}

	// Lettura delle Notifiche
	// private Vector leggiNotifiche() throws Exception {
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug(super.getClass().getName() + ".leggiNotifiche: inizio");
	//
	// String[] lId_Notifica = null;
	// if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ID_NOTIFICA)) {
	// lId_Notifica = getRequestStringParameters(ICostantiNotifica.CAMPO_ID_NOTIFICA);
	// }
	//
	// if (!isRequestParameterNullObj("lCheck")) {
	// mCheck = getRequestStringParameters("lCheck");
	// }
	// String lTestoNote = getRequestStringParameter(ICostantiIstruttoria.CAMPO_NOTE);
	// Vector lNotifiche = new Vector();
	// NotificaModel lNotifica = new NotificaModel();
	// boolean lCancella = false;
	// // Cerca le notifiche ed aggiunge i valori nuovi
	// if (lId_Notifica != null && lId_Notifica.length > 0) {
	// for (int z = 0; z < lId_Notifica.length; z++) {
	//
	// INotifica lCtrlNotifica = SIEPLookupRemote.getNotificaRemote();
	// NotificaModel notifica = lCtrlNotifica.ExRicercaNotificaByKey(((BigDecimal) new BigDecimal(
	// lId_Notifica[z])));
	//
	// notifica.setCodiceOperatoreAggiornamento(this.getCodUtenteConnesso());
	// notifica.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
	// notifica.setDataAggiornamento(DateUtils.getSysDate());
	// notifica.setDataInvio(mDataEmissione);
	// notifica.setNote(lTestoNote);
	// lNotifiche.add(notifica);
	//
	// }
	// }
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug(super.getClass().getName() + ".leggiNotifiche: fine");
	// return lNotifiche;
	// }

	public BigDecimal aggiornaProvvedimento() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".aggiornaProvvedimento: inizio");

		BigDecimal lIdProvvedimento = null;
		// Ricerca Provvedimento dalla chiave (passaggio per Parametro).
		lIdProvvedimento = getRequestBigDecimalParameter(CAMPO_ID_PROVVEDIMENTO_SIGE);

		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel mProvvedimentoEvento = lCtrlProv
				.ExRicercaProvvedimentoById(lIdProvvedimento);

		mProvvedimento = mProvvedimentoEvento.getProvvedimento();

		// Effettuo l'inserimento in ProvvedimentoModel; carico i dati da aggiornare.
		mProvvedimento.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		mProvvedimento.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		mProvvedimento.setDataAggiornamento(DateUtils.getSysDate());
		mProvvedimento.setNote(getRequestStringParameter(ICostantiIstruttoria.CAMPO_NOTE));

		// Modifica del 08/03/2017 *** INIZIO *******
		// Aggiornati i campi "Ufficio Competente" e "Sede"
		if (getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO) != null
				&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO) != null
				&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO).trim().length() > 1
				&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO).trim()
						.length() > 1)
			mProvvedimento.setCodUfficioDestinatario(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO),
					getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO)));
		// Modifica del 08/03/2017 *** FINE *******

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".aggiornaProvvedimento: fine");
		return mProvvedimento.getIdEventoGenerato();
	}

	public String inserisciDati(EventoNotificaModel aEveNot) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".inserisciDati: inizio");
		String lPage = null;

		// Il controller effettuerà tutte le operazioni sui dati.
		IProvvedimentoSige lCtrlPS = SIGELookupRemote.getProvvedimentoRemote();
		lCtrlPS.ExModificaProvvSigeEveNotifica(mProvvedimento, aEveNot, mCheck);

		// restituisce la jsp di VIEW.
		if (mProvvedimento.getCodTipoProvvedimentoSige().equalsIgnoreCase("09"))
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sige.provvInterlocutori.action.ActDettaglioDecretoLatitanza";
		else if (mProvvedimento.getCodTipoProvvedimentoSige().equalsIgnoreCase("12"))
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sige.provvInterlocutori.action.ActDettaglioDecretoIrreperibilita";

		return lPage;
	}

}