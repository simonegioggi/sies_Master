package siap.sico.web;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.security.model.FunctionModel;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.Action;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.util.MultipartContent;
import f3b.web.util.TikaParser;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.helponline.controller.IHelponline;
import siap.sico.helponline.model.HelponlineModel;
import siap.sico.lock.model.LockModel;
import siap.sico.log.controller.ILogAttivita;
import siap.sico.log.model.LogAttivitaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.security.controller.ISecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.ufficio.util.UfficioAccorpatoUtils;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.web.ISIAPCostantiWeb;

/**
 * Title: ActionSiap Description: Azione padre delle classi figlie ActXxxx. Questa classe mette a disposizione
 * alle classi figlie i metodi per estrarre i dati dagli oggetti <code>session</code> e <code>request</code>,
 * inoltre cosa fondamentale ha la responsabilità di caricare dinamicamente la classe azione figlia, metodo
 * direttamente invocato dalla <code>Main.jsp</code>.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActionSiap extends Action {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Costruttore di classe.
	 */
	public ActionSiap() {
		super();
	}

	/**
	 * Costruttore con copia
	 *
	 * @param aAct
	 */
	public ActionSiap(Action aAct) {
		super(aAct);
	}

	/**
	 * Inserisce nella request l'elenco delle fuzioni figlie della funzione indicata e filtrate in base al
	 * profilo dell'utente connesso.
	 *
	 * @param aIdFunction
	 *            id della funzione padre
	 * @throws F3BException
	 *             Propagazione errori di eccezione.
	 */
	public void setFunctionsAvailableToRequest(BigDecimal aIdFunction) throws F3BException {

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// SecurityController lSctrl = new SecurityController();
		ISecurity lSctrl = SICOLookupRemote.getSecurityRemote();
		Collection lCollFun = lSctrl.getFunFiglieByCodProfilo(lUtenteConnesso.getUserProfile().getProfileId(),
				aIdFunction);

		setRequestAttribute(ICostantiSecurity.FUN_FIGLIE, lCollFun);
	}

	/**
	 * Inserisce nella request l'elenco delle fuzioni figlie dell'azione indicata e filtrate in base al
	 * profilo dell'utente connesso.
	 *
	 * @param aNameAction
	 *            nome dell'azione padre
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void setFunctionsAvailableToRequest(String aNameAction) throws F3BException {

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// ---> Inizio Blocco per HELPONLINE
		if (isSessionAttributeNullObj("HelpPage")) {
			if (lUtenteConnesso.getUserProfile().getProfileId().intValue() == 90
					|| lUtenteConnesso.getUserProfile().getProfileId().intValue() == 99) {
				setSessionAttribute("HelpPage", "/help/AMM_Pagina_Iniziale_Titolo.htm");
			} else if (lUtenteConnesso.getUserProfile().isSiep()) {
				setSessionAttribute("HelpPage", "/help/T_UNDER_CONSTRUCTION.htm");
			} else if (lUtenteConnesso.getUserProfile().isSius()) {
				setSessionAttribute("HelpPage", "/help/SIUS_Pagina_Iniziale_Titolo.htm");
			} else {
				setSessionAttribute("HelpPage", "/help/T_UNDER_CONSTRUCTION.htm");
			}
		}
		// ---> Fine Blocco per HELPONLINE

		FunctionModel lFun = getFunctionByNameAction(aNameAction);

		// ---> Inizio Blocco per HELPONLINE
		if (aNameAction.equals("siap.sico.utente.action.ActListaUtenti")
				&& (!isRequestParameterNullObj("tip"))) {
			lFun = getFunctionByNameAction("siap.sico.utente.action.ActListaUtenti&tip=0");
		}
		// ---> Fine Blocco per HELPONLINE

		if (!lFun.getNameAction().equals("siap.sico.sessionstate.action.ActVediSessioneSIEP")
				&& !lFun.getNameAction().equals("siap.sico.sessionstate.action.ActVediSessioneSIUS")
				&& !lFun.getNameAction().equals("siap.sico.sessionstate.action.ActVediSessioneSIEPE")
				&& !lFun.getNameAction().equals("siap.sico.sessionstate.action.ActVediSessioneSIGE")) {
			String ActName = "";
			if (lFun.getNameAction() == null || lFun.getNameAction().equals("")) {
				ActName = lFun.getLabelFunction();
			} else
				ActName = lFun.getNameAction();

			setSessionAttribute("LastFunctionID", lFun.getFunctionId() + " - " + ActName);
			IHelponline lCtrl = SICOLookupRemote.getHelponlineRemote();
			// ----> Helponline
			HelponlineModel lHelMod = lCtrl.ExRicercaHelponlineById(lFun.getFunctionId());
			if (lHelMod != null) {
				setSessionAttribute("HelpPage", lHelMod.getNomePagina());
			}
			// ---> Helponline
		}

		ISecurity lSctrl = SICOLookupRemote.getSecurityRemote();
		Collection lCollFun = lSctrl.getFunFiglieByCodProfilo(lUtenteConnesso.getUserProfile().getProfileId(),
				lFun.getFunctionId());

		setRequestAttribute(ICostantiSecurity.FUN_FIGLIE, lCollFun);
	}

	/**
	 * Torna la funzione associata all'azione richiesta cercandola tra quelle in cui l'utente connesso è
	 * abilitato. In questo modo è posssible verificare se chi sta chiedendo una funzione è abilitato ad
	 * eseguirla.
	 *
	 * @param aNameAction
	 *            nome dell'azione.
	 * @return Il model delle funzioni.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	protected FunctionModel getFunctionByNameAction(String aNameAction) throws F3BException {

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// SecurityController lSctrl = new SecurityController();
		ISecurity lSctrl = SICOLookupRemote.getSecurityRemote();
		FunctionModel lFun = lSctrl.getFunzioneByAzioneCodProfilo(aNameAction,
				lUtenteConnesso.getUserProfile().getProfileId());
		if (lFun == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Funzione '" + aNameAction + "' non disponibile per il profilo abilitato!");

		return lFun;
	}

	/**
	 * Ritorna i dati del Comune dalla descrizione, non effettua controllo omonimia.
	 *
	 * @param aDescrComune
	 *            decrizione comune
	 * @return istanza del ComuneModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected ComuneModel getCodComuneByDescr(String aDescrComune) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("Action getCodComuneByDescr");

		ComuneModel lComMod = new ComuneModel();

		lComMod.setDescrizione(aDescrComune.toUpperCase());

		IComune lComCtrl = SICOLookupRemote.getComuneRemote();
		ComuneModel lComModRitorno = new ComuneModel(lComCtrl.ExGetCodiceComune(lComMod));

		return lComModRitorno;
	}

	// Ricerca comune da InserisciDomicilio/Residenza - Query con Flag_Validita
	protected ComuneModel getCodComuneByDescrFlagVal(String aDescrComune) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("Action getCodComuneByDescrFlagVal");

		ComuneModel lComMod = new ComuneModel();

		lComMod.setDescrizione(aDescrComune.toUpperCase());

		IComune lComCtrl = SICOLookupRemote.getComuneRemote();
		ComuneModel lComModRitorno = new ComuneModel(lComCtrl.ExGetCodiceComuneValidita(lComMod));

		return lComModRitorno;
	}

	/**
	 * Ritorna i dati del Comune dalla descrizione, effettuando controllo omonimia.
	 *
	 * @param aDescrComune
	 *            decrizione comune
	 * @return istanza del ComuneModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected ComuneModel getDatiComuneByDescrOmonimia(String aDescrComune) throws F3BException {

		ComuneModel lComMod = new ComuneModel();

		lComMod.setDescrizione(aDescrComune.toUpperCase());
		lComMod.setControlloOmonimi(true);

		IComune lComCtrl = SICOLookupRemote.getComuneRemote();
		ComuneModel lComModRitorno = new ComuneModel(lComCtrl.ExGetCodiceComune(lComMod));

		return lComModRitorno;
	}

	// Ricerca comune da Soggetto - Query con Flag_Validita
	protected ComuneModel getDatiComuneByDescrOmonimiaFlagVal(String aDescrComune) throws F3BException {

		ComuneModel lComMod = new ComuneModel();

		lComMod.setDescrizione(aDescrComune.toUpperCase());
		lComMod.setControlloOmonimi(true);

		IComune lComCtrl = SICOLookupRemote.getComuneRemote();
		ComuneModel lComModRitorno = new ComuneModel(lComCtrl.ExGetCodiceComuneValidita(lComMod));

		return lComModRitorno;
	}

	/**
	 * Ritorna i dati del Comune dal codice. Risolve il problema dei comuni omonimi.
	 *
	 * @param aCodComune
	 *            codice comune
	 * @return istanza del ComuneModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected ComuneModel getDatiComuneByCodDescr(String aCodComune, String aDescrComune)
			throws F3BException {

		ComuneModel lComMod = new ComuneModel();

		lComMod.setCodComune(aCodComune);
		lComMod.setDescrizione(aDescrComune.toUpperCase());

		IComune lComCtrl = SICOLookupRemote.getComuneRemote();
		ComuneModel lComModRitorno = new ComuneModel(lComCtrl.ExGetCodiceComune(lComMod));

		return lComModRitorno;
	}

	// Ricerca comune da Soggetto - Query con Flag_Validita
	protected ComuneModel getDatiComuneByCodDescrFlagVal(String aCodComune, String aDescrComune)
			throws F3BException {

		ComuneModel lComMod = new ComuneModel();

		lComMod.setCodComune(aCodComune);
		lComMod.setDescrizione(aDescrComune.toUpperCase());

		IComune lComCtrl = SICOLookupRemote.getComuneRemote();
		ComuneModel lComModRitorno = new ComuneModel(lComCtrl.ExGetCodiceComuneValidita(lComMod));

		return lComModRitorno;
	}

	/**
	 * Ritorna l'Ufficio dal tipo ufficio, codice comune
	 *
	 * @param aCodTipoUfficio
	 *            codice del tipo ufficio.
	 * @param aCodComune
	 *            codice comune.
	 * @return UfficioModel
	 * @throws F3BException
	 *             propaga errori di eccezioni.
	 */
	protected String getCodUfficioByCodTipoUfficioDescrComune(String aCodTipoUfficio, String aDescrComune)
			throws F3BException {
		IUfficio lUff = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUffMod = lUff.getUfficioByCodTipoUffDescrComune(aCodTipoUfficio.toUpperCase(),
				aDescrComune.toUpperCase());

		return lUffMod.getCodUfficio();
	}

	protected UfficioModel getUfficioByCodTipoUfficioDescrComune(String aCodTipoUfficio, String aDescrComune)
			throws F3BException {
		IUfficio lUff = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUffMod = lUff.getUfficioByCodTipoUffDescrComune(aCodTipoUfficio.toUpperCase(),
				aDescrComune.toUpperCase());

		return lUffMod;
	}

	/**
	 * Ritorna il model ufficio del codice ufficio passato come argomento.
	 *
	 * @param aCodUfficio
	 *            Codice ufficio per il quale puntare al model ufficio
	 * @return l'intero model dell'ufficio
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected UfficioModel getUfficioByCodUfficio(String aCodUfficio) throws F3BException {
		UfficioModel lUfficio = new UfficioModel();
		// Chiamata al controller.
		IUfficio lUff = SICOLookupRemote.getUfficioRemote();
		lUfficio = lUff.getUfficioByKey(aCodUfficio);

		return lUfficio;
	}

	/**
	 * Restituisce i dati dell'ufficio di origine di un procedimento accorpato noti l'accorpante e
	 * l'incremento della chiave fascicolo
	 *
	 * @param aCodUfficioAccorpante
	 * @param aIncremento
	 * @return
	 * @throws F3BException
	 */
	protected UfficioModel getUfficioAccorpatoByCodAccorpanteIncrement(String aCodUfficioAccorpante,
			String aIncremento) throws F3BException {
		UfficioModel lUfficio = new UfficioModel();

		UfficioAccorpatoUtils lUffAccUtils = new UfficioAccorpatoUtils();
		lUfficio = lUffAccUtils.getUfficioAccorpatoByCodAccorpanteIncrement(aCodUfficioAccorpante,
				aIncremento);

		return lUfficio;
	}

	/**
	 * Restituisce i dati dell'ufficio Accorpato Model, noti l'accorpante e la nuova chiave fascicolo
	 *
	 * @param aCodUfficioAccorpante
	 * @param aChiaveProgr
	 * @return UfficioAccorpatoModel, null se il progressivo non è accorpato
	 * @throws F3BException
	 */
	protected UfficioAccorpatoModel getUfficioAccorpatoByCodAccorpanteProgr(String aCodUfficioAccorpante,
			BigDecimal aChiaveProgr) throws F3BException {
		UfficioAccorpatoModel lAccorpato = null;

		UfficioAccorpatoUtils lUffAccUtils = new UfficioAccorpatoUtils();

		lAccorpato = lUffAccUtils.getUfficioAccorpatoByCodAccorpanteProgr(aCodUfficioAccorpante,
				aChiaveProgr);

		return lAccorpato;
	}

	/**
	 * Ritorno l'Ufficio dell'utente connesso
	 *
	 * @return l'Ufficio dell'utente connesso
	 * @throws F3BException
	 */
	protected UfficioModel getUfficioUtenteConnesso() throws F3BException {
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		return lUtenteMod.getUfficioUtente();
	}

	/**
	 * Ritorna il codice ufficio dell'utente connesso
	 *
	 * @return cod ufficio
	 * @throws F3BException
	 */
	protected String getCodUfficioUtenteConnesso() throws F3BException {
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		return lUtenteMod.getUfficioUtente().getCodUfficio();
	}

	/**
	 * Ritorna lo UserId dell'utente connesso
	 *
	 * @return UserId
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	protected String getCodUtenteConnesso() throws F3BException {
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		return lUtenteMod.getUserId();
	}

	/**
	 * Ritorna l'utente connesso
	 *
	 * @return UserId
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	protected UtenteModel getUtenteConnesso() throws F3BException {
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		return lUtenteMod;
	}

	/**
	 * Ritorna il codice comune dell'utente connesso
	 *
	 * @return cod comune
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	protected String getCodComuneUtenteConnesso() throws F3BException {
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		return lUtenteMod.getUfficioUtente().getCodComune();
	}

	/**
	 * Ritorna il codice distretto dell'utente connesso
	 *
	 * @return il codice del distretto dell'utente connesso.
	 * @throws F3BException
	 *             propaga eventuali errori di eccezione.
	 */
	protected String getCodDistrettoUtenteConnesso() throws F3BException {
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		return lUtenteMod.getUfficioUtente().getCodDistretto();
	}

	/**
	 * Ritorna l'Id del CSSA passandogli il Comune.
	 *
	 * @param aDescComune
	 *            Descrizione Comune
	 * @return Id CSSA
	 * @throws Exception
	 *             Propaga errore di eccezione
	 */
	protected BigDecimal getIdCSSAByDescrComune(String aDescComune) throws Exception {
		CSSAModel lCSSAModel = new CSSAModel();
		if (aDescComune != null && !aDescComune.equals("")) {
			ICSSA lCtrl = SICOLookupRemote.getCSSARemote();
			lCSSAModel = lCtrl.getCSSAByDescrComune(aDescComune.toUpperCase());
		}
		return lCSSAModel.getIdCSSA();
	}

	/**
	 * Metodo che scrive il log del sistema
	 *
	 * @param aNameAction
	 * @throws F3BException
	 */
	public void WriteActivityLog(String aNameAction) throws F3BException {
		LogAttivitaModel log = new LogAttivitaModel();
		ILogAttivita ilog = SICOLookupRemote.getLogAttivitaRemote();

		// MEV_2024-DNA - eliminazione tracciature degli utenti DNA che iniziano per J
		String lCodOperatore = "";

		if (!isSessionAttributeNullObj(ICostantiSecurity.SESSION_UTENTE_CONNESSO))
			lCodOperatore = ((UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO))
					.getUserId();
		else
			// solo alla login!!!!
			lCodOperatore = getRequestStringParameter(ICostantiSecurity.CAMPO_USER_ID);

		if (lCodOperatore.startsWith("J"))
			return;
		else
			log.setCodOperatore(lCodOperatore);

		/*
		 * if (!isSessionAttributeNullObj(ICostantiSecurity.SESSION_UTENTE_CONNESSO))
		 * log.setCodOperatore(((UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO))
		 * .getUserId()); else // solo alla login!!!!
		 * log.setCodOperatore(getRequestStringParameter(ICostantiSecurity.CAMPO_USER_ID));
		 */
		// MEV_2024-DNA - FINE

		log.setIpUtente(getRequest().getRemoteAddr());
		log.setAzioneContestoJava(aNameAction);

		String log_rec = "";

		// 31/03/2009 Lettura dati salienti dalla sessione (fascicoli SIEP, SIUS, SIGE, SIEPE e Soggetto).
		if (!isSessionAttributeNullObj("UtenteConnesso")) {

			UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute("UtenteConnesso");
			ProfileModel lProfilo = lUtenteMod.getUserProfile();
			String lIdFasc = "", lIdSogg = "";

			if (lProfilo.isSiep() && !this.isSessionAttributeNullObj("fascicolo")) {
				FascicoloSiepModel fascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
				if (fascicolo != null && fascicolo.getIdFascicoloSiep() != null) {
					lIdFasc = fascicolo.getIdFascicoloSiep().toString();
					lIdSogg = fascicolo.getSogIdSoggetto().toString();
					log_rec += " , [ ID_FASCICOLO_SIEP ] = [";
					log_rec += lIdFasc + "]";
					log_rec += " , [ ID_SOGGETTO ] = [";
					log_rec += lIdSogg + "]";
				}
			}
			if (lProfilo.isSius() && !this.isSessionAttributeNullObj("fascicoloSiusGP")) {
				FascicoloGPModel fasSiusGP = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
				if (fasSiusGP.getFascicoloSiusModel() != null
						&& fasSiusGP.getFascicoloSiusModel().getIdFascicoloSius() != null
						&& fasSiusGP.getFascicoloSiusModel().getSogIdSoggetto() != null) {
					lIdFasc = fasSiusGP.getFascicoloSiusModel().getIdFascicoloSius().toString();
					lIdSogg = fasSiusGP.getFascicoloSiusModel().getSogIdSoggetto().toString();
					log_rec += " , [ ID_FASCICOLO_SIUS ] = [";
					log_rec += lIdFasc + "]";
					log_rec += " , [ ID_SOGGETTO ] = [";
					log_rec += lIdSogg + "]";
				}
			}
			if (lProfilo.isSige() && !this.isSessionAttributeNullObj("FascicoloSigeEsteso")) {
				FascicoloSigeEstesoModel fasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
						"FascicoloSigeEsteso");
				if (fasSigeEsteso.getFascicoloSige() != null
						&& fasSigeEsteso.getFascicoloSige().getIdFascicoloSige() != null
						&& fasSigeEsteso.getSoggetto().getIdSoggetto() != null) {
					lIdFasc = fasSigeEsteso.getFascicoloSige().getIdFascicoloSige().toString();
					lIdSogg = fasSigeEsteso.getSoggetto().getIdSoggetto().toString();
					log_rec += " , [ ID_FASCICOLO_SIGE ] = [";
					log_rec += lIdFasc + "]";
					log_rec += " , [ ID_SOGGETTO ] = [";
					log_rec += lIdSogg + "]";
				}
			}
			// lFasSiepeEstesoMod = (FascicoloSiepeEstesoModel)getSessionAttribute("FascicoloSiepeEsteso");

			if (lProfilo.isSiepe() && !this.isSessionAttributeNullObj("FascicoloSiepeEsteso")) {
				FascicoloSiepeEstesoModel fasSiepeEsteso = (FascicoloSiepeEstesoModel) getSessionAttribute(
						"FascicoloSiepeEsteso");
				lIdFasc = fasSiepeEsteso.getFascicoloSiepe().getIdFascicoloSiepe().toString();
				lIdSogg = fasSiepeEsteso.getSoggetto().getIdSoggetto().toString();
				log_rec += " , [ ID_FASCICOLO_SIEPE ] = [";
				log_rec += lIdFasc + "]";
				log_rec += " , [ ID_SOGGETTO ] = [";
				log_rec += lIdSogg + "]";
			}
		}
		// ==========================================================================
		// Action di Load Dettaglio Fascicolo SIEP. Devo loggare solo il parametro
		// ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP se presente. Altrimenti
		// recupero l'id_fascicolo dal fascicolo in sessione se presente

		// !!!!!!!!!!!!!! Sperimentale non ancor a rilasciato !!!!!!!!!!!!!

		// ==========================================================================
		// if ("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo".equals(aNameAction))
		// {
		// log_rec += "";
		// Map param_map = getRequest().getParameterMap();
		// Set keys = param_map.keySet();
		//
		// boolean isKeyFasc = false;
		// Iterator itx = keys.iterator();
		// while (itx.hasNext())
		// {
		// String key = (String) itx.next();
		// if ( key.equals(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)
		// || key.equals(IWebConstants.ACTION_FIELD)
		// )
		// {
		// if (key.equals(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP))
		// {
		// isKeyFasc = true;
		// }
		// String[] tmp = (String[]) param_map.get(key);
		// log_rec += " , [" + key + "] = [";
		// String value = "";
		// for (int i = 0; i < tmp.length; i++)
		// value += "-" + tmp[i];
		// value = value.substring(1);
		// log_rec += value + "]";
		// }
		// }
		//
		// if (!isKeyFasc)
		// {
		// if (!this.isSessionAttributeNullObj("fascicolo")) {
		// // Se provengo del Menù Scelta Rapida devo recuparere i dati dalla sessione
		// // Cerco in sessione il fascicolo per recuperare l'id
		// String lIdFasc = ( (FascicoloSiepModel) getSessionAttribute("fascicolo")
		// ).getIdFascicoloSiep().toString();
		// log_rec += " , [" + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "] = [";
		// log_rec += lIdFasc + "]";
		// }
		// }
		//
		// if (log_rec.length() > 3)
		// {
		// log_rec = log_rec.substring(3);
		// }
		//
		// log_rec = ""; // Per ora resetto il codice
		// }
		// Fine sezione sperimentale, per ora non ha effetto
		// ==========================================================================

		// ==========================================================================
		// Costruisce il contenuto da loggare sulla colonna RECORD della LOG_ATTIVITA.
		// Il contenuto è nel formato [key] = [value] , [key] = [value] ...
		// Se presenti più chiavi uguali la loggatura è del tipo:
		// [key] = [value_1-value_2-value_3] , [key] = [value] ...
		// dove value_1 è il più recente
		// ==========================================================================
		// Intervento 1) Sustituzione della Action con '*'
		if (!MultipartContent.isMultipartContent(getRequest())) {
			Map param_map = getRequest().getParameterMap();
			Set keys = param_map.keySet();

			Iterator itx = keys.iterator();
			while (itx.hasNext()) {
				String key = (String) itx.next();
				// if (isKeyDaLoggare(key,log.getAzioneContestoJava()))
				// {
				if (key.toLowerCase().equals("password")) {
					log_rec += " , [password] = [******]";
				} else {
					String[] tmp = (String[]) param_map.get(key);
					log_rec += " , [" + key + "] = [";
					String value = "";
					for (int i = 0; i < tmp.length; i++) {
						// if ( key.toLowerCase().equals("action") &&
						// tmp[i].equals(log.getAzioneContestoJava())){
						// Sostituisco in RECORD la Action se uguale a quella invocata
						// [Action] = [*] oppure [Action] = [*-<azione OLD>]
						// value += "-" + "*";
						// }else{
						value += "-" + tmp[i];
						// }
					}
					value = value.substring(1);
					log_rec += value + "]";
				}
				// }
			}
		} else {
			MultipartContent lReqMultipart = getRequestMultipart();

			Hashtable param_map = lReqMultipart.getParameters();
			Set keys = param_map.keySet();

			Iterator itx = keys.iterator();
			while (itx.hasNext()) {
				String key = (String) itx.next();

				if (key.toLowerCase().equals("password")) {
					log_rec += " , [password] = [******]";
				} else {
					String tmp = lReqMultipart.getParameter(key);
					log_rec += " , [" + key + "] = [";
					String value = tmp;
					log_rec += value + "]";
				}
			}
		}

		if (log_rec.length() > 3) {
			log_rec = log_rec.substring(3);
		}

		// 15/11/2010 Controllo Stringa Record > 4000
		if (log_rec.length() > 4000) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("LogAttività troncato perché log_rec.length()>4000 ");
			log_rec = log_rec.substring(0, 3999);
		}
		log.setRecord(log_rec);

		// Modifica per centralizzazione:
		// Verifica se inserire o meno il record
		if (isRecordDaLoggare(log)) {
			ilog.ExInserisciLogAttivita(log);
		}
	}

	/**
	 * Compone la Stringa completa di una request NON multipart e NON con array di valori...
	 *
	 * @return
	 * @throws F3BException
	 */
	public String getCompleteRequestURL() throws F3BException {
		String lRequest = this.getRequest().getRequestURL() + "?";
		Set lKeys = getRequest().getParameterMap().keySet();

		Iterator itx = lKeys.iterator();
		while (itx.hasNext()) {
			String key = (String) itx.next();
			if (!(key.equals(IWebConstants.NUM_PAGE) || key.equals(IWebConstants.LINK_RITORNO)
					|| key.equals(IWebConstants.FLAG_RITORNO)))
				lRequest += key + "=" + getRequestStringParameter(key) + "&";
		}

		return lRequest.substring(0, lRequest.length() - 1);
	}

	/**
	 * Compone la Stringa completa di una request NON multipart e NON con array di valori... Vengono saltati i
	 * 2 parametri utilizzati nella gestione del ritorno, che sono: TornaQui, StoTornando.
	 *
	 * @return String alterata
	 * @throws F3BException
	 */
	public String getRetRequestURL() throws F3BException {
		String lRequest = this.getRequest().getRequestURL() + "?";
		Set lKeys = getRequest().getParameterMap().keySet();

		Iterator itx = lKeys.iterator();
		while (itx.hasNext()) {
			String key = (String) itx.next();
			// Copia di tutti gli attributi tranne LINK_RITORNO e FLAG_RITORNO
			if (!(key.equals(IWebConstants.LINK_RITORNO) || key.equals(IWebConstants.FLAG_RITORNO))) {
				lRequest += key + "=" + getRequestStringParameter(key) + "&";
			}
		}
		return lRequest.substring(0, lRequest.length() - 1);
	}

	/**
	 * Questa funzione chiamata dopo la cancellazione ritorna la pagina PG_MESSAGE, setta il messaggio passato
	 * come parametro aMessaggio e definisce la prossima pagina GOTO_PAGE secondo la regola seguente: Se
	 * esiste bottone di ritorno passa alla pagina di ritorno, altrimenti passa alla action passata come
	 * parametro aActDopo se non null, in tutti gli altri casi passa alla pagina bianca
	 * ISIAPCostantiWeb.PG_PAGINA_VUOTA.
	 */
	protected String ritornoDopoCancellazione(String aMessaggio, String aActDopo) throws Exception {

		String lLinkRet = null;

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, aMessaggio);

		// Se esiste un bottone di ritorno si salta al suo link
		if (!isSessionAttributeNullObj("StackDiRitorno")) {
			// Se c'è LINK_RITORNO e proveniamo da una Action CHIAMANTE bisogna skippare un ritorno
			if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO)) {
				String lRitorno = getRequestStringParameter(IWebConstants.LINK_RITORNO).trim();
				if (lRitorno.compareTo("20") == 0)
					popStackRitorno();
			}
			lLinkRet = peekStackRitorno();
		}

		if (lLinkRet != null)
			setRequestAttribute(IWebConstants.GOTO_PAGE,
					"" + lLinkRet + "&" + IWebConstants.FLAG_RITORNO + "=1");
		else if (aActDopo != null && aActDopo.trim().length() > 1) {
			// Prepara la "pagina" di destinAction nel caso sia stata passata l'action.
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(aActDopo);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		} else
			setRequestAttribute(IWebConstants.GOTO_PAGE, ISIAPCostantiWeb.PG_PAGINA_VUOTA);

		return IWebConstants.PG_MESSAGE;
	}

	/**
	 * Questa funzione estrae se esiste il link al ritorno e lo assegna alla pagina GOTO_PAGE oltre a
	 * restituirla come ritorno.
	 *
	 * @return String:
	 * @throws Exception
	 */
	protected String goToRitorno() throws Exception {

		String lLinkRet = null;
		// Se esiste un bottone di ritorno si salta al suo link
		if (!isSessionAttributeNullObj("StackDiRitorno")) {
			lLinkRet = peekStackRitorno();

			if (lLinkRet != null) {
				lLinkRet = lLinkRet.substring(lLinkRet.indexOf("?"));
				lLinkRet = IWebConstants.PG_MAIN + lLinkRet;
				if (lLinkRet != null) {
					lLinkRet += "&" + IWebConstants.FLAG_RITORNO + "=1";
					setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lLinkRet);
				}
			}
		}

		return lLinkRet;
	}

	/**
	 * Funzione di utilità per la gestione del bottone di ritorno. Questa funzione va chiamata nella Action
	 * "CHIAMANTE" nel ciclo del bottone di ritorno.
	 */
	protected void setLinkRitorno() throws Exception {
		gestioneStackRitorno();
		// Flag 20 indica Action Chiamante
		setRequestAttribute(IWebConstants.LINK_RITORNO, "20");

		// Metto nello stack in sessione il nuovo punto di ritorno
		String lRitorno = getRetRequestURL();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Push ritorno nello stack -> " + lRitorno);
		pushStackRitorno(lRitorno);
	}

	/**
	 * Funzione di utilità per la gestione del bottone di ritorno. Questa funzione va chiamata nella Action
	 * che gestisce un bottone di ritorno nella relativa jsp e che non è CHIAMANTE ovvero non pone se sterssa
	 * come punto di ritorno.
	 */
	protected void gestioneRitorno() throws Exception {
		// Flag 10 indica Action "Passante"
		if (gestioneStackRitorno())
			setRequestAttribute(IWebConstants.LINK_RITORNO, "10");
	}

	/**
	 * Funzione di utilità per la gestione del bottone di ritorno. Questa funzione va chiamata nella Action
	 * che gestisce un bottone di ritorno nella relativa jsp e che non è CHIAMANTE ovvero non pone se sterssa
	 * come punto di ritorno.
	 */
	protected void gestioneRitornoCodCui() throws Exception {
		setRequestAttribute(IWebConstants.LINK_RITORNO, "10");
	}

	/**
	 * Funzione per la gestione di uno stack in sessione contenente i punti di ritorno tra pagine linkate tra
	 * loro. Ritorna false se lo stack è stato rimosso.
	 */
	/*
	 * Luigi 29-9-2004 Modifica il LINK_RITORNO viene ora messo in sessione anticipatamente dalla Action
	 * chiamante.
	 */
	private boolean gestioneStackRitorno() throws Exception {
		boolean lRitorno = true;
		if (!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO)) {
			String lFlag = this.getRequestStringParameter(IWebConstants.FLAG_RITORNO);
			int lvolte = (lFlag.compareTo("2") == 0) ? 2 : 1;
			for (int i = 0; i < lvolte; i++) {
				// Sto tornando, allora svuoto lo stack
				String lRitornoPop = popStackRitorno();
				if (lRitornoPop == null) {
					lRitornoPop = "vuoto";
					lRitorno = false;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pop ritorno dallo stack-> " + lRitornoPop);

			}
		}
		// Se non c'è indicazione di ritorno lo stack va eliminato
		else if (isRequestParameterNullObj(IWebConstants.LINK_RITORNO)
				|| getRequestStringParameter(IWebConstants.LINK_RITORNO).trim().length() < 1) {
			deleteStackRitorno();
			lRitorno = false;
		}
		return lRitorno;
	}

	/**
	 * Eliminazione Stack Di Ritorno
	 */
	private void deleteStackRitorno() {
		if (!isSessionAttributeNullObj("StackDiRitorno")) {
			// Rimuovo lo stack dalla sessione
			removeSessionAttribute("StackDiRitorno");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Rimosso lo StacDiRitorno dalla sessione");
		}

	}

	/**
	 * Estrae un elemento dallo stack in sessione e lo ripone in sessione se non vuooto.
	 */
	private String popStackRitorno() throws Exception {
		Stack lRetStack = null;
		String lRet = null;

		if (isSessionAttributeNullObj("StackDiRitorno")) {
			// Non c'è lo stack in sessione
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Non c'è lo stack in sessione");
		} else {
			// leggo lo stack dalla sessione
			lRetStack = (Stack) getSessionAttribute("StackDiRitorno");
			if (!lRetStack.isEmpty()) {
				// Lo Stack non è vuoto
				// Occorre estrarre un ritorno dallo stack
				lRet = (String) lRetStack.pop();
			}
			if (lRetStack.isEmpty()) {
				// Rimuovo lo stack dalla sessione
				removeSessionAttribute("StackDiRitorno");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Rimosso lo StacDiRitorno dalla sessione");
			} else {
				// Rimetto in sessione lo Stack
				setSessionAttribute("StackDiRitorno", lRetStack);
			}
		}
		return lRet;
	}

	/**
	 * Inserisce un nuovo elemento nello stack in sessione.
	 *
	 * @param aValore
	 * @throws Exception
	 */
	private void pushStackRitorno(String aValore) throws Exception {
		Stack lRetStack = null;
		if (isSessionAttributeNullObj("StackDiRitorno")) {
			// Non c'è lo stack in sessione
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Non c'è lo stack in sessione per il push");
			// Creazione dello stack ex-novo
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Creazione dello stack");
			lRetStack = new Stack();
		} else {
			// Esiste lo stack in sessione
			// Prelevo lo stack dalla sessione per il push
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Prelevo lo stack dalla sessione per il push");
			lRetStack = (Stack) getSessionAttribute("StackDiRitorno");
		}
		// Evito di pushare 2 volte lo stesso valore di ritorno per evitare loop
		if ((lRetStack.isEmpty()) || lRetStack.peek().toString().compareTo(aValore) != 0)
			lRetStack.push(aValore);
		// Rimetto in sessione lo Stack
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("Rimetto lo stack in sessione dopo il push");
		this.setSessionAttribute("StackDiRitorno", lRetStack);
		return;
	}

	/**
	 * Estrae un elemento dallo stack in sessione tramite un peek.
	 *
	 * @return
	 * @throws Exception
	 */
	private String peekStackRitorno() throws Exception {
		Stack lRetStack = null;
		String lRet = null;

		if (isSessionAttributeNullObj("StackDiRitorno")) {
			// Non c'è lo stack in sessione
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Non c'è lo stack in sessione");
		} else {
			// leggo lo stack dalla sessione
			lRetStack = (Stack) getSessionAttribute("StackDiRitorno");
			if (!lRetStack.isEmpty()) {
				// Lo Stack non è vuoto
				// Occorre estrarre un ritorno dallo stack
				lRet = (String) lRetStack.peek();
			} else if (lRetStack.isEmpty()) {
				// Rimuovo lo stack dalla sessione
				removeSessionAttribute("StackDiRitorno");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Rimosso lo StacDiRitorno dalla sessione perchè vuoto");
			}
		}
		return lRet;
	}

	/**
	 * VErifica che il fascicolo si adi competenza dell'ufficio
	 *
	 * @return vero se il fascicolo è di competenza
	 * @throws F3BException
	 */
	public boolean isFascicoloSiepDiCompetenza() throws F3BException {
		FascicoloSiepModel lFas = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();

		if (lFas != null) {
			if (lUffUtente.equals(lFas.getChiaveUfficio()))
				return true;
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile eseguire l'operazione richiesta. " + "Il Procedimento N."
								+ lFas.getChiaveAnno() + "/" + lFas.getChiaveProgr() + " dell'ufficio "
								+ lFas.getDescrTipoUfficio() + " " + lFas.getDescrComuneUfficio()
								+ " non è di propria competenza.");

		}

		return true;
	}

	/**
	 *
	 * @param Entity
	 * @param idEntity
	 * @param Cod_utente
	 * @return
	 */
	public LockModel lockIfNotLocked(String Entity, String idEntity, String Cod_utente) {

		Hashtable lockTable = (Hashtable) getServletContext().getAttribute("lockTable");
		// Object lckModels[] = lockTable.values().toArray();
		Object lckKeys[] = lockTable.keySet().toArray();
		for (int i = 0; i < lckKeys.length; i++) {
			LockModel lck = (LockModel) lockTable.get(lckKeys[i]);
			if (!lck.getCodOperatore().equals(Cod_utente)) {
				if (lck.getEntity().equals(Entity) && lck.getIdEntity().equals(idEntity)) {
					return lck;
				}
			}
		}

		LockModel lck = new LockModel();
		lck.setCodOperatore(Cod_utente);
		lck.setIdEntity(idEntity);
		lck.setEntity(Entity);
		lockTable.put(getSession().getId(), lck);
		return null;
	}

	/**
	 * metodo per vedere se l'evento precedente sia validato o meno -- 30-03-05 -- Dario -- luciana SE
	 * CodTipoEvento = 05(Richiesta Istruttoria) NON DEVE FARE IL CONTROLLO
	 *
	 * @throws F3BException
	 */
	protected void isEventoNonValidato() throws F3BException {
		FascicoloSiepModel lFas = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrl.ExRicercaEventoByFascicoloSiepDescUfficioConnesso(lFas.getIdFascicoloSiep(),
				this.getCodUfficioUtenteConnesso());

		if (lEveMod != null && lEveMod.getCodTipoEvento() != null && !lEveMod.getCodTipoEvento().equals("05")
				&& !lEveMod.getCodMotivo().equals("0076") // 0076 = Concessione Liberazione Anticipata
				&& !lEveMod.getCodMotivo().equals("2130") // 2130 = Concessione Liberazione Anticipata
				&& !lEveMod.getCodMotivo().equals("2790") // 2790 = Concessione Rimedi Risarcitori
				&& !lEveMod.getCodMotivo().equals("9027") // 9027 = Concessione Reclamo Rimedi Risarcitori
		) {
			if (lEveMod.getFlagDocumentoRegistrato() == null
					|| "N".equals(lEveMod.getFlagDocumentoRegistrato())) {
				/*
				 * ISSUE MAC : aggiunte info del provvedimento non validato nel messaggio dell'eccezione
				 * Numero MAC : 20200220017 Autore : monica Data : 20/feb/2020 Branch : 12.1
				 */
				String infoEventoNONValidato = lEveMod.getDescrTipoProvvedimento() != null
						? " L'evento non validato è il seguente: " + lEveMod.getDescrTipoProvvedimento()
						: "";
				infoEventoNONValidato += lEveMod.getDescrMotivo() != null ? " " + lEveMod.getDescrMotivo()
						: "";
				infoEventoNONValidato += lEveMod.getDataEmissione() != null
						? " con data inserimento del "
								+ DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy") + "."
						: "";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Esiste un evento NON validato. Validarlo o cancellarlo e rieseguire la funzione."
								+ infoEventoNONValidato);
				// ***** FINE INTERVENTO 20200220017 *****//
			}
		}
	}

	/**
	 * Controlla se l'evento precedente sia validato o meno SE CodTipoEvento = 05(Richiesta Istruttoria) NON
	 * DEVE FARE IL CONTROLLO Se evento tipo 0284 (Decisioni del GE - Applicazione Amnistia / Indulto ) 0285
	 * (Decisioni del GE - Applicazione depenalizzazione) 0286 (Decisioni del GE - Applicazione
	 * incostituzionalita')
	 *
	 * @throws F3BException
	 */
	protected void isEventoNonValidatoAnnotazioniManuali() throws F3BException {
		FascicoloSiepModel lFas = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrl.ExRicercaEventoByFascicoloSiepDescUfficioConnesso(lFas.getIdFascicoloSiep(),
				this.getCodUfficioUtenteConnesso());

		if (lEveMod != null && lEveMod.getCodTipoEvento() != null && !lEveMod.getCodTipoEvento().equals("05")
				&& !lEveMod.getCodMotivo().equals("0076") // 0076 = Concessione Liberazione Anticipata
				&& !lEveMod.getCodMotivo().equals("2130") // 2130 = Concessione Liberazione Anticipata
				&& !lEveMod.getCodMotivo().equals("0284") // 0284 = Decisioni del GE - Applicazione Amnistia /
															// Indulto
				&& !lEveMod.getCodMotivo().equals("0285") // 0285 = Decisioni del GE - Applicazione
															// depenalizzazione
				&& !lEveMod.getCodMotivo().equals("0286") // 0286 = Decisioni del GE - Applicazione
															// incostituzionalita'
		) {
			if (lEveMod.getFlagDocumentoRegistrato() == null
					|| "N".equals(lEveMod.getFlagDocumentoRegistrato())) {
				/*
				 * ISSUE MAC : aggiunte info del provvedimento non validato nel messaggio dell'eccezione
				 * Numero MAC : 20200220017 Autore : monica Data : 20/feb/2020 Branch : 12.1
				 */
				String infoEventoNONValidato = lEveMod.getDescrTipoProvvedimento() != null
						? " L'evento non validato è il seguente: " + lEveMod.getDescrTipoProvvedimento()
						: "";
				infoEventoNONValidato += lEveMod.getDescrMotivo() != null ? " " + lEveMod.getDescrMotivo()
						: "";
				infoEventoNONValidato += lEveMod.getDataEmissione() != null
						? " con data inserimento del "
								+ DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy") + "."
						: "";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Esiste un evento NON validato. Validarlo o cancellarlo e rieseguire la funzione."
								+ infoEventoNONValidato);
				// ***** FINE INTERVENTO 20200220017 *****//
			}
		}
	}

	/**
	 * metodo specializzato per il cumulo vedere se l'evento precedente sia validato - 19-05-05 - Dario -
	 * Luciana SE CodTipoEvento = 05(Richiesta Istruttoria) e CodMotivo = 0222, 0223, 0224, 0277
	 * (Provvedimento di Esecuzione di Pene Concorrenti) NON DEVE FARE IL CONTROLLO
	 *
	 * @throws F3BException
	 */
	protected void isEventoNonValidatoPerCumulo() throws F3BException {
		FascicoloSiepModel lFas = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrl.ExRicercaEventoByFascicoloSiepDescUfficioConnesso(lFas.getIdFascicoloSiep(),
				this.getCodUfficioUtenteConnesso());

		if (lEveMod != null && lEveMod.getCodMotivo() != null && !lEveMod.getCodMotivo().equals("0222")
				&& !lEveMod.getCodMotivo().equals("0223") && !lEveMod.getCodMotivo().equals("0224")
				&& !lEveMod.getCodMotivo().equals("0277") && lEveMod.getCodTipoEvento() != null
				&& !lEveMod.getCodTipoEvento().equals("05")) {
			if (lEveMod.getFlagDocumentoRegistrato() == null
					|| "N".equals(lEveMod.getFlagDocumentoRegistrato())) {
				/*
				 * ISSUE MAC : aggiunte info del provvedimento non validato nel messaggio dell'eccezione
				 *
				 * Numero MAC : 20200220017 Autore : monica Data : 20/feb/2020 Branch : 12.1
				 */
				String infoEventoNONValidato = lEveMod.getDescrTipoProvvedimento() != null
						? " L'evento non validato è il seguente: " + lEveMod.getDescrTipoProvvedimento()
						: "";
				infoEventoNONValidato += lEveMod.getDescrMotivo() != null ? " " + lEveMod.getDescrMotivo()
						: "";
				infoEventoNONValidato += lEveMod.getDataEmissione() != null
						? " con data inserimento del "
								+ DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy") + "."
						: "";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Esiste un evento NON validato. Validarlo o cancellarlo e rieseguire la funzione."
								+ infoEventoNONValidato);
				// ***** FINE INTERVENTO 20200220017 *****//
			}
		}
	}

	/**
	 * metodo specializzato per la pena cumulo vedere se l'evento precedente sia validato SE CodTipoEvento =
	 * 01(Provvedimento) e CodMotivo = 0222, 0223, 0224, 0277 (Provvedimento di Esecuzione di Pene
	 * Concorrenti) NON DEVE FARE IL CONTROLLO
	 *
	 * @throws F3BException
	 */
	protected void isEventoNonValidatoPerPenaCumulo() throws F3BException {
		FascicoloSiepModel lFas = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrl.ExRicercaEventoByFascicoloSiepDescUfficioConnesso(lFas.getIdFascicoloSiep(),
				this.getCodUfficioUtenteConnesso());

		if (lEveMod != null && lEveMod.getCodMotivo() != null && !lEveMod.getCodMotivo().equals("0222")
				&& !lEveMod.getCodMotivo().equals("0223") && !lEveMod.getCodMotivo().equals("0224")
				&& !lEveMod.getCodMotivo().equals("0277") && lEveMod.getCodTipoEvento() != null
				&& !lEveMod.getCodTipoEvento().equals("01")) {
			if (lEveMod.getFlagDocumentoRegistrato() == null
					|| "N".equals(lEveMod.getFlagDocumentoRegistrato())) {
				/*
				 * ISSUE MAC : aggiunte info del provvedimento non validato nel messaggio dell'eccezione
				 *
				 * Numero MAC : 20200220017 Autore : monica Data : 20/feb/2020 Branch : 12.1
				 */
				String infoEventoNONValidato = lEveMod.getDescrTipoProvvedimento() != null
						? " L'evento non validato è il seguente: " + lEveMod.getDescrTipoProvvedimento()
						: "";
				infoEventoNONValidato += lEveMod.getDescrMotivo() != null ? " " + lEveMod.getDescrMotivo()
						: "";
				infoEventoNONValidato += lEveMod.getDataEmissione() != null
						? " con data inserimento del "
								+ DateUtils.getDateToString(lEveMod.getDataEmissione(), "dd-MM-yyyy") + "."
						: "";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Esiste un evento NON validato. Validarlo o cancellarlo e rieseguire la funzione."
								+ infoEventoNONValidato);
				// ***** FINE INTERVENTO 20200220017 *****//
			}
		}
	}

	/**
	 * Verifica se l'ultimo evento inserito non è validato, ma escude dal controllo il computo in quanto
	 * potrebbe non essere validato.
	 *
	 * @throws F3BException
	 */
	protected EventoModel isEventoNonValidatoRidetPenaAltro(EventoModel aEveComputo) throws F3BException {
		FascicoloSiepModel lFas = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrl.ExRicercaEventoByFascicoloSiepDescUfficioConnesso(lFas.getIdFascicoloSiep(),
				this.getCodUfficioUtenteConnesso());

		return lEveMod;

		// if ( lEveMod != null
		// && lEveMod.getIdEvento().compareTo(aEveComputo.getIdEvento())!=0
		// && lEveMod.getCodMotivo() != null
		// && lEveMod.getCodTipoEvento() != null && !lEveMod.getCodTipoEvento().equals("05")
		// && !lEveMod.getCodMotivo().equals("0076") // 0076 = Concessione Liberazione Anticipata
		// && !lEveMod.getCodMotivo().equals("2130") // 2130 = Concessione Liberazione Anticipata
		// )
		// {
		// return lEveMod;
		// if (lEveMod.getFlagDocumentoRegistrato() == null ||
		// "N".equals(lEveMod.getFlagDocumentoRegistrato()))
		// {
		// throw new F3BException(F3BException.USER_MESSAGE,
		// "Esiste un evento '"+lEveMod.getDescrTipoProvvedimento()+" - "+lEveMod.getDescrMotivo()+"' NON
		// validato. Validarlo o cancellarlo e rieseguire la funzione.");
		// }
		// }
	}

	/**
	 * metodo per vedere se il fascicolo sia validato o meno
	 *
	 * @return
	 * @throws F3BException
	 */
	protected boolean isFascicoloNonValidato() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		if ("N".equalsIgnoreCase(lFascMod.getFlagValidato())) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return true;
		}
		return false;
	}

	/**
	 * metodo per vedere se il fascicolo sia archiviato/definito
	 *
	 * @return
	 * @throws F3BException
	 */
	protected boolean isFascicoloArchiviatoDefinito() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		if ("01".equals(lFascMod.getCodStatoFascicolo())) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return true;
		}
		return false;
	}

	/**
	 * metodo per vedere se la pena residua esiste o meno
	 */
	protected boolean notEsistePenaResiduaCorrenteByFascicoloSiep(PenaResiduaModel aPenaResMod)
			throws F3BException {
		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		if (aPenaResMod == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return true;
		}
		return false;
	}

	/*
	 * metodo per vedere se la posizione giuridica esiste o meno viene passata il
	 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel perchè la maggior parte delle funzioni ricerca questo
	 * bean, e quindi passandogli solo il PosizioneGiuridicaModel non avremmo tenuto conto di 'aPos == null' e
	 * di conseguenza questo metodo sarebbe stato inutile.
	 */
	protected boolean notEsistePosizioneGiuridica(PosizioneGiuridicaLuogoDetenzioneAltraCausaModel aPos)
			throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		if (aPos == null || aPos.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return true;
		}
		return false;
	}

	/**
	 * calcolaMagistrato
	 *
	 * @return
	 */
	protected String calcolaMagistrato() throws F3BException {
		String lCodiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		if (lCodiceMagistrato.compareTo("") == 0) {
			MagistratoModel lMagMod = new MagistratoModel();
			lMagMod.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME));
			lMagMod.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME));

			IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaMagistrato(lMagMod);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}
			lMagMod = (MagistratoModel) lVect.firstElement();
			lCodiceMagistrato = lMagMod.getCodMagistrato();
		}
		return lCodiceMagistrato;
	}

	/**
	 * OverWrite del metodo "getFile" della classe padre Action oggetto InputStream.
	 *
	 * @param aParamName
	 *            nome chiave del parametro nella request.
	 * @return contenuto file come classe <code>InputStream</code>
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected InputStream getFile(String aParamName) throws F3BException {
		if (MultipartContent.isMultipartContent(this.getRequest())) {
			fileUploadParser(aParamName);
			return this.getRequestMultipart().getFile(aParamName);
		} else
			return null;
	}

	/**
	 * Metodo che esegue il parsing del file in upload.
	 *
	 * @param aParamName
	 *            nome del campo del file in Upload
	 * @throws F3BException
	 */
	protected void fileUploadParser(String aParamName) throws F3BException {
		byte[] mBytes = getFileBytes(aParamName);

		if (mBytes != null && mBytes.length > 0) {
			TikaParser lTikaParser = new TikaParser(mBytes, getServletContext(),
					getRequestMultipart().getExtName(aParamName));

			try {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("################# [ Start -> lTikaParser.autoDetect() ] ################");
				lTikaParser.autoDetect();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("################# [ End   -> lTikaParser.autoDetect() ] ################");
			} catch (Exception ex) {
				throw new F3BException(ex);
			}
		}
	}

	/**
	 * Metodo che si occupa di verificare se il record va loggato o meno. Inserire in questo metodo i criteri
	 * per scartare eventuali loggature non significative. Da implementare
	 *
	 * @param aLogModel
	 * @return
	 */
	private boolean isRecordDaLoggare(LogAttivitaModel aLogModel) {

		if (aLogModel.getAzioneContestoJava()
				.equals("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo")) {
		}

		return true;
	}

	/**
	 * Metodo che in funzione della Action decide se loggare o meno un certo parametro. Serve per mettere un
	 * filtro per le loggature doppie.
	 *
	 * @param aKey
	 * @param aAction
	 * @return
	 */
	// private boolean isKeyDaLoggare(String aKey, String aAction) {
	// // Nel caso della ActLoadDettaglioFascicolo carico solo i parametri
	// // effettivamente letti dalla action. Tutti gli altri sono quelli girati
	// // dalla forward della action precedente e quindi già loggati
	// if (aAction.equals("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo")) {
	// if ("Action".equals(aKey) || "lTipoFunzione".equals(aKey) || "NomeAzione".equals(aKey)
	// || "ChiaveFascicolo".equals(aKey)) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	// return true;
	// }

	/**
	 * metodo per vedere se il fascicolo sia nello stato "ISCRITTO"
	 *
	 * @return
	 * @throws F3BException
	 */
	protected boolean isFascicoloIscritto() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		if (lFascMod.getCodStatoFascicolo().equalsIgnoreCase("02")) {
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Impossibile accedere alla funzionalità!<br>Il procedimento è nello stato ISCRITTO");
			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.sico.security.action.ActLoadOrizontalMenu&IdFunzione=48");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return true;
		}
		return false;
	}

	/**
	 * MEV 10 - filtro sui minorenni
	 *
	 * @param idSoggetto
	 * @return
	 * @throws F3BException
	 */
	protected String getFiltroMinorenni() throws F3BException {
		String ret = "true";

		// *************
		// MEV 10: diversamente dalle richieste iniziali non ci deve essere distinzione
		// tra uffici abilitati e non alla gestione minorenni
		// *************
		// prende dalla sessione il codice dell'ufficio dell'utente connesso
		// UfficioModel um = getUfficioUtenteConnesso();
		// String tipoUfficio = um.getCodTipoUfficio();
		// Set elenco = new HashSet();
		// elenco.add("PM");
		// if (elenco.contains(tipoUfficio))
		// {
		// ret = "false";
		// }

		return ret;
	}

	/**
	 * MEV10-s3: aggiunto metodo di estrazione campo
	 *
	 * @return
	 * @throws F3BException
	 */
	protected String getCodTipoUfficioConnesso() throws F3BException {
		UfficioModel um = getUfficioUtenteConnesso();
		return um.getCodTipoUfficio();
	}

	/**
	 * MEV10-s3: Ritorna l'Id del CSSA passandogli il Comune ed il tipo.
	 *
	 * @param aDescComune
	 * @param aTipo
	 * @return Id CSSA
	 * @throws Exception
	 */
	protected BigDecimal getIdCSSAByDescrComuneETipo(String aDescComune, String aTipo) throws Exception {
		CSSAModel lCSSAModel = new CSSAModel();
		ICSSA lCtrl = SICOLookupRemote.getCSSARemote();
		lCSSAModel = lCtrl.getCSSAByDescrComuneETipo(aDescComune.toUpperCase(), aTipo);
		return lCSSAModel.getIdCSSA();
	}

	/**
	 * MEV 15 - Revisione SIGE Recupero il codice della funzione chiamante (menù verticale).
	 *
	 * @return codFunzione codice della funzione chiamante.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected String getCodFunMenuVerticale() throws F3BException {
		LinkedList LastFunctionAnt = (LinkedList) getSessionAttribute("FunAntenate");
		String codFunzione = null;
		if (LastFunctionAnt.size() > 0) {
			FunctionModel funzione = (FunctionModel) LastFunctionAnt.get(0);
			codFunzione = funzione.getFunctionId().toString();
		}
		return codFunzione;
	}

	/**
	 * Controllo su Tabella Sentenza, per vedere se trattasi di fascicolo di MIS SIC icritta in Sentenza,
	 * OPPURE se trattasi di fascicolo di MIS SIC iscritto Fuori Sentenza o di Misura Provvisoria
	 *
	 * @return il cod. tipo sentenza
	 * @throws F3BException
	 */
	protected String getTipoSentenza() throws F3BException {
		String TipoSent = "";
		FascicoloSiepModel lFas = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
		ISentenza lCtrlS = SIEPLookupRemote.getSentenzaRemote();
		SentenzaModel lSenMod = lCtrlS.ExRicercaSentenzaByKey(lFas.getSenIdSentenza());
		if (lSenMod != null && lSenMod.getIdSentenza() != null)
			TipoSent = lSenMod.getCodTipoProvvedimento();

		return TipoSent;
	}

} // Chiudo ActionSiap