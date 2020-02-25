package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.jms.util.ParserMessageRec;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.ActionSius;
import siap.sius.curatore.controller.ICuratoreSius;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.action.ICostantiProvvedimento;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciDataDepositoDecreto
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Data Deposito Decreto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ActLoadInserisciDataDepositoDecreto extends ActionSius
		implements ICostantiDepositoDecreto, ICostantiProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String mRetPage = PG_LOAD_INSERISCIDATADEPOSITODECRETO;

	// Decreto
	DepositoDecretoModel mDepDecMod = null;
	// Fascicolo
	FascicoloGPModel mFasGPMod = null;

	public boolean mIsDepositato = false;

	// Controller
	IStampaSius mStaCtrl = null;
	INotifica mNotCtrll = null;
	IDocumentoAllegato mDocAllCtrl = null;
	IUfficio mUffCtrl = null; // Interfaccia al Controller Ufficio
	IDepositoDecreto mDepCtrl = null;
	BigDecimal lIdEvento = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".processRequest: inizio");

		// Fascicolo Sius
		mFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (mFasGPMod == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Fascicolo SIUS non in sessione !");

		// Ricerca del Deposito Decreto dall'ID Evento
		lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		ricercaProvvedimento(lIdEvento);
		// Ricerca Avvocati e Luogo Detenzione
		LuogoDetenzioneModel lLuogoDetMod = leggiAvvocatiLuogoDetenzione();
		String filtroMinorenni = super.getFiltroMinorenni();
		Collection<DecodificheModel> autorita = new Vector<>();
		autorita.addAll(DecodificheManager.getInstance().getTipoAutorita());
		if (filtroMinorenni.equalsIgnoreCase("true")) {
			autorita.addAll(DecodificheManager.getInstance().getTipoAutoritaMinorenni());
			Collections.sort((List<DecodificheModel>) autorita, new DecodificheModel.OrderByDescrizione());
		}
		Option lOptionAut = new Option(autorita, 75);

		// 16/03/2007 Preleva elenco delle altre autorita' giudiziarie.
		Collection<DecodificheModel> options = new Vector<>();
		options.addAll(DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep());

		if (filtroMinorenni.equalsIgnoreCase("true")) {
			options.add(new DecodificheModel("TDS", "Tribunale di Sorveglianza", "TIPO_UFFICIO_CUMULO", "",
					"", "", "", "", ""));
			options.add(new DecodificheModel("UDS", "Ufficio di Sorveglianza", "TIPO_UFFICIO_CUMULO", "", "",
					"", "", "", ""));
		}
		// Modifica del 16/11/2016 MEV_50
		// Questi uffici devono essere visibili sia per gli uffici maggiorenni
		// che per gli uffici minorenni
		options.add(new DecodificheModel("TDSM",
				"Tribunale per i Minorenni in Funzione di Tribunale di Sorveglianza", "TIPO_UFFICIO_CUMULO",
				"", "", "", "", "", ""));
		options.add(new DecodificheModel("UDSM", "Ufficio di Sorveglianza presso il Tribunale per Minorenni",
				"TIPO_UFFICIO_CUMULO", "", "", "", "", "", ""));
		// Modificata descrizione ufficio
		// options.add(new DecodificheModel("UDSM", "Ufficio di Sorveglianza per i Minorenni",
		// "TIPO_UFFICIO_CUMULO", "", "", "", "", "", ""));

		Option lOptionAltreAut = new Option(options, "-");

		// LISTA UFFICI SOGGETTO
		Option lOptionSog = null;
		if (lLuogoDetMod != null && lLuogoDetMod.getIstitutoDetenzione() != null
				&& lLuogoDetMod.getIstitutoDetenzione().getCodTipoIstituto().length() > 0)
			lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(),
					lLuogoDetMod.getIstitutoDetenzione().getCodTipoIstituto(), 75);
		else
			lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);

		// LISTA UFFICI
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		// MEV10-s3: modificato array per i minorenni
		String[] lStringFilter = null;
		if ("true".equals(filtroMinorenni))
			lStringFilter = new String[] { "-", "22", "A2" };
		else
			lStringFilter = new String[] { "-", "22" };

		// MEV_65 aggiunte voci per notifica avvocato e gestita selezione
		if ("true".equals(filtroMinorenni))
			// lStringFilter = new String[] { "-", "22", "A2" };
			lStringFilter = new String[] { "-", "22", "A2", "C1" };
		else
			// lStringFilter = new String[] { "-", "22" };
			lStringFilter = new String[] { "-", "22", "C0", "C1" };
		Option lOptionAvv = new Option();
		if (isRequestParameterNullObj("Aggiungi")) {
			// lOptionAvv = new Option(lTipoIstituto, "22", 75);
			lOptionAvv = new Option(lTipoIstituto, "C1", 75); // predefinito
		} else {
			lOptionAvv = new Option(lTipoIstituto, "-", 75);
		}
		lOptionAvv.setFilter(lStringFilter);

		// Ricerca Notifiche eventualmente gia' emesse
		ricercaNotifiche(lIdEvento);

		// Eventuale Curatore Sius 18/05/2011
		UfficioModel lUff = getUfficioUtenteConnesso();
		CuratoreSiusModel lCuratore = null;
		if ("UDS".equals(lUff.getCodTipoUfficio())) {
			ICuratoreSius lCurSiusCtrl = SIUSLookupRemote.getCuratoreSiusRemote();
			lCuratore = lCurSiusCtrl
					.ExRicercaCurSiusByFascicolo(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			if (lCuratore != null) {
				// Curatore
				if (lCuratore.getCurIdCuratore() != null) {
					ICuratore lCurCtrl = SIGELookupRemote.getCuratoreRemote();
					CuratoreModel lCurMod = lCurCtrl.ExRicercaCuratoreByKey(lCuratore.getCurIdCuratore());
					lCuratore.setCuratore(lCurMod);
				}
			}
			setRequestAttribute("curatore", lCuratore);
		}

		// Ricerca Descrizione Comune per il Magistrato di Sorveglianza
		ricercaComuneMagSorveglianza();

		// Ricerca dell'elenco dei possibili destinatari
		Vector lDestinatari = new Vector(DecodificheManager.getInstance().getDestinatarioDeposito());

		// Per gli uffici TDS e UDS il campo USSM non deve essere visualizzato
		Vector lDestinatariAppo = new Vector();
		for (Object destinatari : lDestinatari) {
			DecodificheModel destinatario = (DecodificheModel) destinatari;
			if (destinatario.getCode().equals("USSM")
					&& (lUff.getCodTipoUfficio().equals("TDS") || lUff.getCodTipoUfficio().equals("UDS"))) {
				// l'elemento non viene aggiunto al vettore di appoggio
				// poiche' il campo USSM deve essere visibile solo agli uffici dei minori
			} else {
				lDestinatariAppo.add(destinatario);
			}
		}
		lDestinatari = lDestinatariAppo;

		// MEV10-s3: aggiunta or condition per gestire
		// "Ufficio di Sorveglianza presso il Tribunale per minorenni"
		if (lUff.getCodTipoUfficio().equals("TDSM") || lUff.getCodTipoUfficio().equals("UDSM"))
			// Ricerca dell'elenco dei possibili destinatari consideranno i minorenni
			lDestinatari = new Vector(DecodificheManager.getInstance().getDestinatarioDepositoMinorenni());

		// setRequestAttribute("lDescMagComp", lDescComune);
		setRequestAttribute("destDeposito", lDestinatari);
		setRequestAttribute("TipiIstitutiSog", "" + lOptionSog);
		setRequestAttribute("TipiIstituti1", "" + lOptionAvv);
		setRequestAttribute("tipoAutorita", lOptionAut.toString());

		// 16/03/2007 Notifica ad altre Autorita giudiziarie.
		setRequestAttribute("altreAutoritaGiudiziarie", lOptionAltreAut.toString());

		if (!this.isRequestParameterNullObj("Aggiungi")) {
			setRequestAttribute("Aggiungi", "yes");
		} else {
			setRequestAttribute("Aggiungi", "no");
		}
		// }
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".processRequest: fine");

		return mRetPage; // restituisce la jsp di VIEW
	}

	/**
	 * Effettua la ricerca degli Avvocati e del Luogo di detenzione. Se presenti vengono passati alla request.
	 */
	private LuogoDetenzioneModel leggiAvvocatiLuogoDetenzione() throws Exception {

		// Preleva dati AVVOCATI e LUOGODETENZIONE
		ParserMessageRec lParser = null;
		if (mStaCtrl == null)
			mStaCtrl = SIUSLookupRemote.getStampaRemote();
		// l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSius.TREE_LUOGODET, ICostantiStampaSius.TREE_AVVOCATOSIUS };
		// Creazione del TreeModel con i dati che occorrono
		if (mFasGPMod == null || mFasGPMod.getFascicoloSiusModel() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Fascicolo SIUS non in sessione !");

		TreeModel lTreeDati = mStaCtrl
				.ExPrelevaDatiVideo(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(), aTipoDati);
		// Converte i dati ottenuti per utilizzarli come model
		lParser = new ParserMessageRec(lTreeDati);
		LuogoDetenzioneModel lLuogoDetMod = lParser.getLuogoDetenzione();
		setRequestAttribute("luogodet", lLuogoDetMod);
		setRequestAttribute("avvocato", lParser.getAvvocatoSius());
		return lLuogoDetMod;
	}

	/**
	 * Effettua la ricerca delle Eventuali Notifiche gia' emesse per il Decreto Depositato. Se presenti esse
	 * vengono passate alla request.
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

	/**
	 * Effettua la ricerca della descrizione del Comune del Magistrato di Sorveglianza. La stringa viene
	 * passata alla request.
	 */
	private void ricercaComuneMagSorveglianza() throws Exception {

		String lDescComune = null; // Stringa restituita
		if (mUffCtrl == null)
			mUffCtrl = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUfficio = null;
		if (mDepDecMod != null && mDepDecMod.getCodUfficioCompetente() != null
				&& mDepDecMod.getCodUfficioCompetente().compareTo("-") != 0) {
			lUfficio = mUffCtrl.getUfficioByKey(mDepDecMod.getCodUfficioCompetente());
			lDescComune = lUfficio.getDescrComune();
		}
		setRequestAttribute("lDescMagComp", lDescComune);
	}

	/**
	 * Passa alla request il codice del tipo di Ufficio dell'utente connesso se UDS o TDS. Passa inoltre la
	 * descrizione del comune dell'ufficio.
	 */
	// private void ricercaSedeProcura() throws Exception {
	// if (mUffCtrl == null)
	// mUffCtrl = SICOLookupRemote.getUfficioRemote();
	// String strCodDistretto = getUfficioUtenteConnesso().getCodDistretto();
	// String strCodComune = getUfficioUtenteConnesso().getCodComune();
	// String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
	// String strTipoUfficioRichiesto = new String();
	//
	// if (strCodTipoUfficio.equals("TDS")) {
	// setRequestAttribute("TipoUfficioConnesso", "TDS");
	// strTipoUfficioRichiesto = "UDS";
	// } else if (strCodTipoUfficio.equals("UDS")) {
	// setRequestAttribute("TipoUfficioConnesso", "UDS");
	// strTipoUfficioRichiesto = "TDS";
	// }
	// String strCodUfficioRichiesto = (mUffCtrl.getUfficioUDSTDS(strCodDistretto, strTipoUfficioRichiesto,
	// strCodComune)).getDescrComune();
	// setRequestAttribute("lDescUDSTDS", strCodUfficioRichiesto);
	//
	// }

	// Ricerca del Deposito Decreto dall'ID Evento
	public void ricercaProvvedimento(BigDecimal aIdEvento) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".ricercaProvvedimento: inizio");

		if (mDepCtrl == null)
			mDepCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		mDepDecMod = mDepCtrl.ExRicercaDepositoDecretoByIdEvento(aIdEvento);

		if (mDepDecMod == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Nessun Decreto e' stato emesso per il procedimento ");

		// Se il Decreto non è già depositato bisogna lockare per evitare che 2 utenti tentino di depositarlo
		// contemporaneamente.
		if (mDepDecMod.getNumS72() == null) {
			// Lock
			LockModel lck = LockController.lockIfNotLocked(getServletContext(), "DEPOSITO_DECRETO",
					mDepDecMod.getIdDepositoDecreto().toString(), getCodUtenteConnesso(),
					getSession().getId());

			if (lck != null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il " + lck.getEntity() + " (ID: " + lck.getIdEntity()
								+ ") e' in gestione ad un altro utente!<BR>Riprovare piu' tardi !");
				mRetPage = IWebConstants.PG_MESSAGE;
				return;
			}
		}

		if (mDepDecMod.getDataDeposito() != null) {
			mIsDepositato = true;
			if (isRequestParameterNullObj("Aggiungi")) {
				// Se il decreto è già stato depositato
				if (mDocAllCtrl == null)
					mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
				DocumentoAllegatoModel lDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO), "03");
				// Potrebbe capitare che il documento allegato non sia recuperabile. Luigi 9-1-2006
				if (lDocAll == null || lDocAll.getIdDocumentoAllegato() == null)
					throw new F3BException(F3BException.EX_NOT_FOUND,
							"Errore nella lettura del Documento Allegato per IDEvento -> "
									+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));

				// Si passa al dettaglio
				RedirectTo lPage = new RedirectTo();
				lPage.setPage(IWebConstants.PG_MAIN);
				lPage.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDataDepositoDecreto");
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

		setRequestAttribute("lDepositoDecreto", mDepDecMod);
		setSessionAttribute("lDepositoDecreto", mDepDecMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".ricercaProvvedimento: fine");
	}

}