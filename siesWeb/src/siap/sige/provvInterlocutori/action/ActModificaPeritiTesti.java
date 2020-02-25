package siap.sige.provvInterlocutori.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.cssa.controller.ICSSA;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaOrdinanza
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaPeritiTesti extends ActInserisciNominaPeriti {

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
		// Vector lNotifiche = new Vector();
		Vector lNotifiche = leggiNotifiche();

		// Preleva le note dalla form
		// leggo i periti
		/*
		 * String lCampiNoteReq = getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_AGGIUNTIVO); if
		 * (lCampiNoteReq != null && lCampiNoteReq.length() > 0) { CampoNotaModel[] lCampiNote = new
		 * CampoNotaModel[1]; lCampiNote[0] = new CampoNotaModel(); lCampiNote[0].setDescr(lCampiNoteReq);
		 * lCampiNote[0].setCodOperatoreInserimento(getCodUtenteConnesso());
		 * lCampiNote[0].setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		 * lCampiNote[0].setDataInserimento(DateUtils.getSysDate());
		 * 
		 * lEveNot.setCampoNote(lCampiNote); }
		 */

		// Consentita la cancellazione di tutti i destinatari.
		// if (lNotifiche != null && lNotifiche.size() > 0)
		if (lNotifiche != null && lNotifiche.size() >= 0)
			lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// Aggiornamento dei dati sul DB
		lPage = inserisciDati(lEveNot);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".processRequest: fine");

		return lPage;
	}

	// Lettura delle Notifiche
	private Vector leggiNotifiche() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".leggiNotifiche: inizio");

		String[] lId_Notifica = null;
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ID_NOTIFICA)) {
			lId_Notifica = getRequestStringParameters(ICostantiNotifica.CAMPO_ID_NOTIFICA);
		}
		String[] lSedi = null;
		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_SEDE)) {
			lSedi = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		}
		if (!isRequestParameterNullObj("lCheck")) {
			mCheck = getRequestStringParameters("lCheck");
		}

		Vector lNotifiche = new Vector();
		// NotificaModel lNotifica = new NotificaModel();
		// boolean lCancella = false;
		// Cerca le notifiche ed aggiunge i valori nuovi
		if (lId_Notifica != null && lId_Notifica.length > 0) {
			for (int z = 0; z < lId_Notifica.length; z++) {
				INotifica lCtrlNotifica = SIEPLookupRemote.getNotificaRemote();
				NotificaModel notifica = lCtrlNotifica.ExRicercaNotificaByKey(((BigDecimal) new BigDecimal(
						lId_Notifica[z])));

				if (notifica.getAutoritaEsterna() != null) { // Autorita' Esterna
					// Controlla e preleva il codice comune di sede.
					String lCodSede = getCodComuneByDescr(lSedi[z].toUpperCase()).getCodComune();
					// Crea Model Autorità Esterna
					AutoritaEsternaModel lAutorita = new AutoritaEsternaModel(notifica.getAutoritaEsterna());
					lAutorita.setCodSede(lCodSede);

					// Controllo sede UNEP
					controlloAutorita(lAutorita, lSedi[z]);

					// Aggiunge il model Autorità Esterna alla Notifica
					notifica.setAutoritaEsterna(lAutorita);
					if (notifica.getAutoritaEsterna().getCodTipoAutorita() != null) {
						notifica.setNote(getRequestStringParameter("note_"
								+ notifica.getIdNotifica().toString()));
					}
				} else if (notifica.getIstitutoDetenzione() != null) { // Istituto Detenzione
					String lIstitutoDetenzione = null;
					if (!this
							.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
						lIstitutoDetenzione = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
						notifica.setIstDetIdIstitutoDetenzione(lIstitutoDetenzione);
					}
				} else if (notifica.getCssIdCssa() != null) { // CSSA
					if (notifica.getCSSA() != null) {
						ICSSA lCtrlCSSA = SICOLookupRemote.getCSSARemote();
						notifica.setCssIdCssa(lCtrlCSSA.getCSSAByDescrComune(lSedi[z].toUpperCase())
								.getIdCSSA());
					}
				}
				notifica.setCodiceOperatoreAggiornamento(this.getCodUtenteConnesso());
				notifica.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
				notifica.setDataAggiornamento(DateUtils.getSysDate());
				notifica.setDataInvio(mDataEmissione);
				lNotifiche.add(notifica);

			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".leggiNotifiche: fine");
		return lNotifiche;
	}

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
		mProvvedimento.setNote(getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_AGGIUNTIVO));

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
		// DocumentoAllegatoModel lDocAllMod = lCtrlPS.ExModificaDataDeposito( mProvvedimento, aEveNot,
		// mCheck);

		lCtrlPS.ExModificaProvvSigeEveNotifica(mProvvedimento, aEveNot, mCheck);

		// restituisce la jsp di VIEW.
		if (mProvvedimento.getCodTipoProvvedimentoSige().equalsIgnoreCase("13"))
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sige.provvInterlocutori.action.ActDettaglioNominaPeriti";
		else if (mProvvedimento.getCodTipoProvvedimentoSige().equalsIgnoreCase("17"))
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sige.provvInterlocutori.action.ActDettaglioCitazioneTesti";

		return lPage;
	}

	// La funzione controlla nel caso l'autorità esterna sia UNEP che il comune indicato come sede sia una
	// sede UNEP
	private void controlloAutorita(AutoritaEsternaModel aAutorEst, String aDescrSede) throws F3BException {

		IComune lCtrl = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("controlloAutorita: inizio");
		if (aAutorEst != null && aAutorEst.getCodTipoAutorita() != null && aDescrSede != null)
			// Controllo Comune sede UNEP
			if (aAutorEst.getCodTipoAutorita().equalsIgnoreCase(COD_UNEP)) {
				// Interfaccia al Controller che effettua il controllo
				lCtrl = SICOLookupRemote.getComuneRemote();
				if (!lCtrl.ExIsComuneSedeUNEP(aAutorEst.getCodSede()))
					throw new F3BException(F3BException.USER_MESSAGE, aDescrSede + " non è comune sede UNEP");
			}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("controlloAutorita: fine");
	}

	protected UdienzaSigeModel getUdienzaSige(String aIdUdienzaSige) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("inizio");
		// ==========================================
		// Recupera i dati del record
		// ==========================================
		IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel lUdiMod = lCtrl.ExRicercaUdienzaSigeById(new BigDecimal(aIdUdienzaSige));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Lettura di lUdiMod : " + lUdiMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("fine");
		return lUdiMod;
	}

}