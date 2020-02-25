package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciDataDeposito
 * </p>
 * <p>
 * Description: Classe Action per la l'inserimento della Data Deposito Provvedimento
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciDataDeposito extends ActionSige implements ICostantiProvvedimentoSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String mRetPage = PG_LOAD_INSERISCIDATADEPOSITO;

	// Provvedimento
	ProvvedimentoSigeModel mProvMod = null;
	ProvvedimentoSigeEventoModel mProvEveMod = null;

	public boolean mIsDepositato = false;

	// Controller
	IStampaSige mStaCtrl = null;
	INotifica mNotCtrll = null;
	IDocumentoAllegato mDocAllCtrl = null;
	IUfficio mUffCtrl = null; // Interfaccia al Controller Ufficio
	IProvvedimentoSige mProvCtrl = null;
	BigDecimal lIdProvvedimento = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".processRequest: inizio");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel mFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		if (mFasEsteso == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Fascicolo SIGE non in sessione !");

		// Ricerca del Provvedimento dall'ID
		lIdProvvedimento = getRequestBigDecimalParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);
		ProvvedimentoSigeEventoModel mProvEveMod = ricercaProvvedimento(lIdProvvedimento);

		// Ricerca tenori legati al provvedimento
		TenoreSigeModel lTenore = new TenoreSigeModel();
		lTenore.setProvIdProvvedimentoSige(lIdProvvedimento);
		ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
		// Vector lTenori = lTenCtrl.ExRicercaTenoriAttivi(lTenore);
		Vector<TenoreSigeEstesoModel> lTenori = lTenCtrl
				.ExRicercaTenoriEstesiByIdProvvedimento(lIdProvvedimento);
		setRequestAttribute("tenoriEstesi", lTenori);
		TenoriSigeUtil tsu = new TenoriSigeUtil();
		setRequestAttribute("tenori", tsu.listaTenoriDaListaTenoriEstesi(lTenori));

		// Ricerca Sentenze assegnate al Fascicolo (Ulteriori Titoli Esecutivi)
		// IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		// Vector lSentenze =
		// lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		// setRequestAttribute("sentenze", lSentenze);

		ModificabileStampabile(mProvEveMod, lTenori);

		// Ricerca Avvocati e Luogo Detenzione
		LuogoDetenzioneModel lLuogoDetMod = leggiAvvocatiLuogoDetenzione(mFasEsteso);

		// Preleva elenco degli altri destinatari.
		Option lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);

		// Preleva elenco delle altre autorità giudiziarie.
		// Option lOptionAltreAut = new Option(
		// DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep(), "-");
		Option lOptionAltreAut = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionAltreAut.setFilter(new String[] { "-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS",
				"GIPMI", "GIP", "GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "TRIBSD", "CAPSM", "TMI", "DIB",
				"DIBM" }); // solo le Autorità Emittenti.

		// LISTA UFFICI SOGGETTO
		Option lOptionSog = null;
		if (lLuogoDetMod != null && lLuogoDetMod.getIstitutoDetenzione() != null
				&& lLuogoDetMod.getIstitutoDetenzione().getCodTipoIstituto().length() > 0)
			lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), lLuogoDetMod
					.getIstitutoDetenzione().getCodTipoIstituto(), 75);
		else
			lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);

		// LISTA UFFICI
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		String[] lStringFilter = { "-", "22" };
		Option lOptionAvv = new Option();
		if (this.isRequestParameterNullObj("Aggiungi")) {
			lOptionAvv = new Option(lTipoIstituto, "22", 75);
		} else {
			lOptionAvv = new Option(lTipoIstituto, "-", 75);
		}
		lOptionAvv.setFilter(lStringFilter);

		// Ricerca Notifiche eventualmente già emesse
		ricercaNotifiche(mProvEveMod.getProvvedimento().getIdEventoGenerato());

		// Ricerca dell'elenco dei possibili destinatari
		Vector lDestinatari = new Vector(DecodificheManager.getInstance().getDestinatarioDepositoSige());

		setRequestAttribute("destDeposito", lDestinatari);
		setRequestAttribute("TipiIstitutiSog", "" + lOptionSog);
		setRequestAttribute("TipiIstituti1", "" + lOptionAvv);
		setRequestAttribute("tipoAutorita", lOptionAut.toString());
		setRequestAttribute("idProvvedimentoTitoliEsecutivi", lIdProvvedimento);

		// Notifica ad altre Autorita giudiziarie.
		setRequestAttribute("altreAutoritaGiudiziarie", lOptionAltreAut.toString());

		if (!this.isRequestParameterNullObj("Aggiungi")) {
			setRequestAttribute("Aggiungi", "yes");
		} else {
			setRequestAttribute("Aggiungi", "no");
		}

		// MEV 15
		// Recupero il domicilio del soggetto
		ResidenzaModel domicilioSoggetto = null;
		if (mFasEsteso.getDomicilio() != null) {
			domicilioSoggetto = mFasEsteso.getDomicilio();
		}
		setRequestAttribute("domicilioSoggetto", domicilioSoggetto);

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		BigDecimal idEvento = mProvEveMod.getProvvedimento().getIdEventoGenerato();
		EventoNotificaModel eventoNotificaModel = lCtrlEvento.ExRicercaEventoNotificaByKey(idEvento);
		Vector<NotificaModel> vectAvvoca = getNotificheAvvocati(eventoNotificaModel);
		super.setRequestAttribute("vectNotAvv", vectAvvoca);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".processRequest: fine");

		if (isDecretoFissazioneUdienza())
			mRetPage = ICostantiProvvedimentoSige.PG_LOAD_INSERISCIDATADEPOSITO_FISSAZIONE_UDIENZA;

		return mRetPage; // restituisce la jsp di VIEW
	}

	// Ricerca del Provvedimento dall'ID
	public ProvvedimentoSigeEventoModel ricercaProvvedimento(BigDecimal aIdProvvedimento) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".ricercaProvvedimento: inizio");

		if (mProvCtrl == null)
			mProvCtrl = SIGELookupRemote.getProvvedimentoRemote();

		mProvEveMod = mProvCtrl.ExRicercaProvvedimentoById(aIdProvvedimento);

		if (mProvEveMod == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Provvedimento non trovato ");

		if (mProvEveMod.getProvvedimento().getCodTipoProvvedimentoSige().equalsIgnoreCase("01")) {
			String elencoDecreti = super.getParameter(CAMPO_ELENCO_DECRETI);

			if ("true".equalsIgnoreCase(elencoDecreti)) {
				throw new F3BException(
						F3BException.USER_MESSAGE,
						"Deposito non possibile per il decreto di fissazione udienza, utilizzare l'apposita funzione presente nel dettaglio di fissazione udienza.");
			}
		}

		// Se il Provvedimento non è già depositato bisogna lockare la risorsa per evitare che 2 utenti
		// tentino di depositarlo contemporaneamente.
		if (mProvEveMod.getProvvedimento().getChiaveProgr() == null) {
			// Lock
			LockModel lck = LockController.lockIfNotLocked(getServletContext(), "PROVVEDIMENTO_SIGE",
					mProvEveMod.getProvvedimento().getIdProvvedimentoSige().toString(),
					getCodUtenteConnesso(), getSession().getId());

			if (lck != null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il " + lck.getEntity() + " (ID: " + lck.getIdEntity()
								+ ") è in gestione ad un altro utente!<BR>Riprovare più tardi !");
				mRetPage = IWebConstants.PG_MESSAGE;
				return mProvEveMod;
			}
		}

		if (mProvEveMod.getProvvedimento().getDataDeposito() != null) {
			mIsDepositato = true;
			if (isRequestParameterNullObj("Aggiungi")) {
				// Se il provvedimento è già stato depositato
				if (mDocAllCtrl == null)
					mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
				DocumentoAllegatoModel lDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(
						mProvEveMod.getEventoNotifica().getEvento().getIdEvento(), mProvEveMod
								.getProvvedimento().getCodTipoProvvedimento());

				// Potrebbe capitare che il documento allegato non sia recuperabile.
				if (lDocAll == null || lDocAll.getIdDocumentoAllegato() == null)
					throw new F3BException(F3BException.EX_NOT_FOUND,
							"Errore nella lettura del Documento Allegato per IDEvento -> " + ""
									+ mProvEveMod.getEventoNotifica().getEvento().getIdEvento());

				// Si passa al dettaglio
				RedirectTo lPage = new RedirectTo();
				lPage.setPage(IWebConstants.PG_MAIN);
				lPage.setAction("siap.sige.provvedimento.action.ActLoadDettaglioDataDeposito");
				lPage.setParameter(CAMPO_ID_DOCUMENTO_ALLEGATO, "" + lDocAll.getIdDocumentoAllegato());

				// Passaggio al dettaglio del LINK di ritorno
				if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
					lPage.setParameter(IWebConstants.LINK_RITORNO,
							getRequestStringParameter(IWebConstants.LINK_RITORNO));
				if (!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
					lPage.setParameter(IWebConstants.FLAG_RITORNO,
							getRequestStringParameter(IWebConstants.FLAG_RITORNO));
				mRetPage = lPage.toString();
			} else
				setLinkRitorno();
		} else
			setLinkRitorno();

		setRequestAttribute("lProvvedimento", mProvEveMod);
		setSessionAttribute("lProvvedimento", mProvEveMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".ricercaProvvedimento: fine");
		return mProvEveMod;
	}

	/**
	 * Effettua la ricerca degli Avvocati e del Luogo di detenzione. Se presenti vengono passati alla request.
	 */

	private LuogoDetenzioneModel leggiAvvocatiLuogoDetenzione(FascicoloSigeEstesoModel mFasEsteso)
			throws Exception {

		// Preleva dati AVVOCATI e LUOGODETENZIONE

		// Ricerca Avvocati
		IAvvocato lCtrlAvv = SIGELookupRemote.getAvvocatoRemote();
		Vector lAvvocati = new Vector();

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvFascMod.setFasSigeIdFascicoloSige(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		lAvvocati = lCtrlAvv.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);

		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		// Magistrato Assegnatario
		MagistratoAssegnatarioModel lMagAss = mFasEsteso.getMagAssegnatario();
		setRequestAttribute("magistratoassegnatario", lMagAss);

		// LuogoDetenzioneModel lLuogoDetMod = lParser.getLuogoDetenzione();
		LuogoDetenzioneModel lLuogoDetMod = null;
		if (mFasEsteso.getDetenzione() != null && mFasEsteso.getDetenzione().getLuogoDetenzione() != null) {
			lLuogoDetMod = mFasEsteso.getDetenzione().getLuogoDetenzione();
			setRequestAttribute("luogodet", lLuogoDetMod);
		}

		return lLuogoDetMod;
	}

	/**
	 * Effettua la ricerca delle Eventuali Notifiche già emesse per il Provvedimento Depositato. Se presenti
	 * esse vengono passate alla request.
	 */

	private void ricercaNotifiche(BigDecimal aIdEvento) throws Exception {

		// Flag per indicare la presenza o meno della Notifica al Soggetto
		String lTrovatoSog = "NO";
		// Flag per indicare la presenza o meno delle Notifiche
		String lTrovateNotifiche = "NO";

		if (mIsDepositato) {
			// Lettura delle notifiche.
			if (mNotCtrll == null)
				mNotCtrll = SIEPLookupRemote.getNotificaRemote();
			Vector lVect = mNotCtrll.ExRicercaEstesaNotificaByKeyEvento(aIdEvento);
			setRequestAttribute("notifiche", lVect);
			if (lVect.size() > 0) {
				lTrovateNotifiche = "SI";
				Iterator itx2 = lVect.iterator();
				while (itx2.hasNext()) {
					NotificaModel notifica = (NotificaModel) itx2.next();
					if (notifica.getSogIdSoggetto() != null) {
						lTrovatoSog = "SI";
					}
				}
			}
		}
		setRequestAttribute("notificheSog", lTrovatoSog);
		setRequestAttribute("NotifichePresenti", lTrovateNotifiche);
	}

	private void ModificabileStampabile(ProvvedimentoSigeEventoModel lProvEvento,
			Vector<TenoreSigeEstesoModel> lTenori) throws Exception {

		String lStampabile = "NO";
		String lModificabile = "NO";

		// Fascicolo Modificabile e Provvedimento non validato
		if (IsFascicoloSigeModificabile()
				&& (lProvEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null || lProvEvento
						.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)) {
			lStampabile = "SI";
			lModificabile = "SI";

			for (TenoreSigeEstesoModel tenoreEsteso : lTenori) {
				TenoreSigeModel lTenore = tenoreEsteso.getTenoreSige();
				if (lTenore.getCodEsitoSige() == null) {
					lStampabile = "NO";
					break;
				}
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Modificabile : " + lModificabile);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Stampabile : " + lStampabile);

		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Stampabile", lStampabile);
	}

	private boolean isDecretoFissazioneUdienza() {

		boolean flag = false;
		try {
			String udienza = getRequestStringParameter("isUdienza");
			flag = udienza.equalsIgnoreCase("true");
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	private Vector<NotificaModel> getNotificheAvvocati(EventoNotificaModel eventoNotificaModel) {

		Vector<NotificaModel> vectNotAvv = new Vector<NotificaModel>();
		if (eventoNotificaModel != null && eventoNotificaModel.getNotifiche() != null) {
			NotificaModel[] notList = eventoNotificaModel.getNotifiche();
			for (NotificaModel nm : notList) {
				if (nm.getAvvIdAvvocatoFascicoloSige() != null) {
					vectNotAvv.add(nm);
				}
			}
		}

		return vectNotAvv;
	}

}