package siap.sius.depositodecreto.action;

/**
* <p>Title: ActModificaDataDepositoDecreto</p>
* <p>Description: Classe Action per l'inserimento della data di Deposito Decreto</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
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
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sius.util.SIUSLookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaDataDepositoDecreto extends ActionSius implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String[] mCheck = null;
	DepositoDecretoModel mDecreto = null;
	Date mDataTrasmissione = null;

	/**
	 * Azione di Inserimento della data di Deposito Decreto
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		String lPage = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".processRequest: inizio");

		BigDecimal lIdEvento = null;

		if (this.IsFascicoloSiusModificabile() == false)
			throw new SIUSException(SIUSException.USER_MESSAGE, ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

		lIdEvento = aggiornaProvvedimento();

		// Aggiornamento dell'Evento e Inserimento della Notifica per ciascun destinatario.
		// Preparo il model EventoNotifica.
		EventoNotificaModel lEveNot = new EventoNotificaModel();

		// Evento. Leggo l'evento collegato al decreto.
		EventoModel lEveMod = new EventoModel();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// BigDecimal lIdEvento = lDecMod.getIdEventoGenerato();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		// Correzione bug. Luigi 5-1-2006
		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE)
				&& !isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE)
				&& !isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE))
			mDataTrasmissione = getRequestDateParameter(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE,
					ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE);
		else
			mDataTrasmissione = lEveMod.getDataTrasmissioneAtti();

		// Aggiorno l'Evento.
		lEveMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEveMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lEveMod.setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.setDataTrasmissioneAtti(mDataTrasmissione);

		// Imposto l'Evento nel'EventoNotificaModel.
		lEveNot.setEvento(lEveMod); // (dovrebbe essere un eventomodel)

		// Preparo le notifiche.
		// Vector lNotifiche = new Vector();
		Vector lNotifiche = leggiNotifiche();

		// 06/07/2007 Consentita la cancellazione di tutti i destinatari.
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

		if (!isRequestParameterNullObj("lCheck")) {
			mCheck = getRequestStringParameters("lCheck");
		}
		String[] lId_Notifica = null;
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ID_NOTIFICA)) {
			lId_Notifica = getRequestStringParameters(ICostantiNotifica.CAMPO_ID_NOTIFICA);
		}
		String[] lSedi = null;
		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_SEDE)) {
			lSedi = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		}

		Vector lNotifiche = new Vector();
		// NotificaModel lNotifica = new NotificaModel();
		boolean lCancella = false;
		// Cerca le notifiche ed aggiunge i valori nuovi
		if (lId_Notifica != null && lId_Notifica.length > 0) {
			for (int z = 0; z < lId_Notifica.length; z++) {
				lCancella = false;
				for (int i = 0; i < mCheck.length; i++) {
					if (lId_Notifica[z].equals(mCheck[i].toString())) {
						lCancella = true;
					}
				}
				if (!lCancella) {
					INotifica lCtrlNotifica = SIEPLookupRemote.getNotificaRemote();
					NotificaModel notifica = lCtrlNotifica
							.ExRicercaNotificaByKey((new BigDecimal(lId_Notifica[z])));

					if (notifica.getUfficio() != null) // UFFICIO
					{
						if (notifica.getUfficio().getCodTipoUfficio().equals("PGCAP")) {
							notifica.setUffCodUfficio(getCodUfficioByCodTipoUfficioDescrComune("PGCAP",
									lSedi[z].toUpperCase()));

						} else if (notifica.getUfficio().getCodTipoUfficio().equals("UDS")) {
							notifica.setUffCodUfficio(
									getCodUfficioByCodTipoUfficioDescrComune("UDS", lSedi[z].toUpperCase()));

						} else if (notifica.getUfficio().getCodTipoUfficio().equals("TDS")) {
							notifica.setUffCodUfficio(
									getCodUfficioByCodTipoUfficioDescrComune("TDS", lSedi[z].toUpperCase()));

						} else if (notifica.getUfficio().getCodTipoUfficio().equals("PM")) {
							notifica.setUffCodUfficio(
									getCodUfficioByCodTipoUfficioDescrComune("PM", lSedi[z].toUpperCase()));
						} else if (notifica.getUfficio().getCodTipoUfficio().equals("UEPE")) {
							notifica.setUffCodUfficio(
									getCodUfficioByCodTipoUfficioDescrComune("UEPE", lSedi[z].toUpperCase()));
						} else if (notifica.getUfficio().getCodTipoUfficio().equals("UEPESS")) {
							notifica.setUffCodUfficio(getCodUfficioByCodTipoUfficioDescrComune("UEPESS",
									lSedi[z].toUpperCase()));
						}
					} else if (notifica.getAutoritaEsterna() != null) // Autorita' Esterna
					{
						// Controlla e preleva il codice comune di sede.
						String lCodSede = getCodComuneByDescr(lSedi[z].toUpperCase()).getCodComune();
						// Crea Model Autorità Esterna
						AutoritaEsternaModel lAutorita = new AutoritaEsternaModel(
								notifica.getAutoritaEsterna());
						lAutorita.setCodSede(lCodSede);

						// Controllo sede UNEP
						controlloAutorita(lAutorita, lSedi[z]);

						// Aggiunge il model Autorità Esterna alla Notifica
						notifica.setAutoritaEsterna(lAutorita);
						if (notifica.getAutoritaEsterna().getCodTipoAutorita() != null) {
							notifica.setNote(
									getRequestStringParameter("note_" + notifica.getIdNotifica().toString()));
						}
					} else if (notifica.getIstitutoDetenzione() != null) // Istituto Detenzione
					{
						String lIstitutoDetenzione = null;
						if (!this.isRequestParameterNullObj(
								ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
							lIstitutoDetenzione = getRequestStringParameter(
									ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
							notifica.setIstDetIdIstitutoDetenzione(lIstitutoDetenzione);
						}
					} else if (notifica.getCssIdCssa() != null) // CSSA
					{
						if (notifica.getCSSA() != null) {
							ICSSA lCtrlCSSA = SICOLookupRemote.getCSSARemote();
							notifica.setCssIdCssa(
									lCtrlCSSA.getCSSAByDescrComune(lSedi[z].toUpperCase()).getIdCSSA());
						}
					}
					notifica.setCodiceOperatoreAggiornamento(this.getCodUtenteConnesso());
					notifica.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
					notifica.setDataAggiornamento(DateUtils.getSysDate());
					notifica.setDataInvio(mDataTrasmissione);
					lNotifiche.add(notifica);
				}
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

		if (isSessionAttributeNullObj("lDepositoDecreto"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati Decreto non in sessione!");

		// Lettura DEposito Decreto dalla sessione
		mDecreto = (DepositoDecretoModel) getSessionAttribute("lDepositoDecreto");

		// Effettuo l'inserimento data deposito in DepositoDecretoModel; carico i dati da aggiornare.
		mDecreto.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		mDecreto.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		mDecreto.setDataAggiornamento(DateUtils.getSysDate());
		mDecreto.setDataDeposito(getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO, CAMPO_MESE_DATA_DEPOSITO,
				CAMPO_GIORNO_DATA_DEPOSITO));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".aggiornaProvvedimento: fine");
		return mDecreto.getIdEventoGenerato();
	}

	public String inserisciDati(EventoNotificaModel aEveNot) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".inserisciDati: inizio");

		// Recupero l'utente, il Fascicolo e il Decreto dalla sessione.
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Il controller effettuerà tutte le operazioni sui dati.
		IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
		DocumentoAllegatoModel lDocAllMod = lCtrlDD.ExModificaDataDepositoDecreto(lFasGPMod, mDecreto,
				aEveNot, mCheck);

		// restituisce la jsp di VIEW.
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.depositodecreto.action.ActLoadDettaglioDataDepositoDecreto&"
				+ CAMPO_ID_DOCUMENTO_ALLEGATO + "=" + lDocAllMod.getIdDocumentoAllegato();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".inserisciDati: fine");

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

}