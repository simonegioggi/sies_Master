package siap.sige.web;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.reato.model.ReatoModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.impugnazione.action.ICostantiImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.Action;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActionSige
 * </p>
 * <p>
 * Description: Azione estensione della ActionSiap. Questa classe mette a disposizione nuove funzioni
 * specifiche dell'utente SIGE.
 * </p>
 * <p>
 * Copyright: Eutelia 2008
 * </p>
 * 
 * @version 1.0
 */
public class ActionSige extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Costruttore di classe.
	 */
	public ActionSige() {
		super();
	}

	/**
	 * Costruttore con copia
	 * 
	 * @param aAct
	 */
	public ActionSige(Action aAct) {
		super(aAct);
	}

	/**
	 * <p>
	 * Restituisce la possibilità di modificare i dati del Fascicolo SIGE. STUB: per il momento si è definito
	 * modificabile solo il Fascicolo in stato "02" ovvero Iscritto, che non abbia nessun provvedimento.
	 * Definitorio. Eventualmente da modificare !!
	 * 
	 * @return lRet Condizione di modificabilità del fascicolo.
	 * @throws F3BException
	 *             Propagazione errori di eccezione.
	 */
	public boolean IsFascicoloSigeModificabile() throws F3BException {
		boolean lRet = false;

		if (getSession() == null)
			throw new F3BException(F3BException.EX_OPERATION_FAILED,
					"Non è possibile usare questa funzione senza definire la session !!");

		if (IsFascicoloSigeIscrittoCompetenza() && !esisteProvvedimentoDefinitorio())
			lRet = true;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("IsModificabile : " + (lRet ? "true" : "false"));

		return lRet;
	}

	/**
	 * Restituisce la possibilità di modificare i dati del Fascicolo SIGE. STUB: per il momento si è definito
	 * modificabile solo il Fascicolo in stato "02" ovvero Iscritto, che non abbia nessun provvedimento.
	 * Definitorio. Eventualmente da modificare
	 */
	/*
	 * private boolean IsFascicoloSigeModificabile(FascicoloSigeModel aFascicolo) throws F3BException {
	 * boolean lRet = false; if ( IsFascicoloSigeIscrittoCompetenza(aFascicolo)) { // Ricerca Provvedimenti
	 * Definitori IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
	 * ProvvedimentoSigeModel lProvvedimento =
	 * lCtrlProv.ExRicercaProvDefinitorioByFasc(aFascicolo.getIdFascicoloSige()); if (lProvvedimento == null )
	 * lRet = true; }
	 * 
	 * return lRet; }
	 */

	/**
	 * Testa se il Fascicolo in sessione è in stato Iscritto e se appartiene allo stesso ufficio
	 * dell'operatore.
	 * 
	 * @return boolean
	 * @throws F3BException
	 */

	protected boolean IsFascicoloSigeIscrittoCompetenza() throws F3BException {
		return IsFascicoloSigeIscrittoCompetenza(getFascicoloSigeInSessione());
	}

	/**
	 * Testa se il Fascicolo in sessione è in stato Definito.
	 * 
	 * @return boolean
	 * @throws F3BException
	 */

	protected boolean IsFascicoloSigeDefinito() throws F3BException {
		return IsFascicoloSigeDefinito(getFascicoloSigeInSessione());
	}

	private boolean IsFascicoloSigeIscrittoCompetenza(FascicoloSigeModel aFascicolo) throws F3BException {
		boolean lRet = false;

		if ((aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("02")
				|| aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("10")
				|| aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("14")
				|| aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("15")
				|| aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("16")
				|| aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("20")
				////@emma 10072018 intervento post COLLAUDO 11.2
				|| aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("21"))
				&& getCodUfficioUtenteConnesso().equalsIgnoreCase(aFascicolo.getChiaveUfficio()))
			lRet = true;

		return lRet;
	}

	private boolean IsFascicoloSigeDefinito(FascicoloSigeModel aFascicolo) throws F3BException {
		boolean lRet = false;

		if (aFascicolo.getCodStatoFascicolo().equalsIgnoreCase(ICostantiFascicoloSige.COD_DEFINITO))
			lRet = true;

		return lRet;
	}

	/**
	 * Risale al FascicoloSigeEstesoModel in sessione.
	 * 
	 * @return FascicoloSigeEstesoModel
	 * @throws F3BException
	 */
	protected FascicoloSigeEstesoModel getFascicoloSigeEstesoInSessione() throws F3BException {
		if (isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Procedimento SIGE non in sessione !!");

		FascicoloSigeEstesoModel lFascicoloEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		if (lFascicoloEsteso == null || lFascicoloEsteso.getFascicoloSige() == null
				|| lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Non è possibile recuperare dalla sessione i dati fascicolo");

		return lFascicoloEsteso;
	}

	/**
	 * Rimuove se presente il dato FascicoloSigeEsteso dalla Session.
	 * 
	 * @throws F3BException
	 */
	protected void rimuoviFascicoloSigeEstesoDallaSessione() throws F3BException {
		if (!isSessionAttributeNullObj("FascicoloSigeEsteso"))
			removeSessionAttribute("FascicoloSigeEsteso");

		return;
	}

	/**
	 * Risale al FascicoloSigeModel in sessione.
	 * 
	 * @return
	 * @throws F3BException
	 */
	protected FascicoloSigeModel getFascicoloSigeInSessione() throws F3BException {
		FascicoloSigeEstesoModel lFascicoloEsteso = getFascicoloSigeEstesoInSessione();

		if (lFascicoloEsteso == null || lFascicoloEsteso.getFascicoloSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Non è possibile recuperare dalla sessione i dati fascicolo");

		return lFascicoloEsteso.getFascicoloSige();
	}

	/**
	 * Passa nella request modificaOggettiAtto = SI se è stata verificata la possibilità di
	 * modificare/cancellare/inserire Oggetti x l'Atto; NO negli altri casi. L'oggetto dell'Atto/Richiesta è
	 * modificabile se risulta modificabile il Fascicolo Sige e non esistono Provvedimenti di tipo Definitorio
	 * legati a quel fascicolo. Il Fascicolo di riferimento è quello in sessione.
	 *
	 */
	protected void setModificabileOggettiAtto() throws F3BException {
		String lModificabile = "NO";

		if (IsFascicoloSigeModificabile())
			lModificabile = "SI";

		setRequestAttribute("modificaOggettoAtto", lModificabile);
	}

	/**
	 * Passa nella request 2 parametri: Modificabile, Cancellabile. Se è stata verificata la possibilità di
	 * modificare/cancellare il Titolo assegnato al Procedimento Sige i parametri assumeranno valore "SI"
	 * altrimenti "NO". L'assegnazione del Titolo è modificabile se risulta modificabile il Fascicolo Sige e
	 * non esistono Provvedimenti di tipo Definitorio legati a quel fascicolo. E' cancellabile se modificabile
	 * ed inoltre non si tratta del Fascicolo SIEP "generatore" ovvero quello legato alla Richiesta. Il
	 * Fascicolo di riferimento è quello in sessione.
	 * 
	 */
	protected void setModificaCancellaTitolo(BigDecimal aIdFasSiep) throws F3BException {
		String lModificabile = "NO";
		String lCancellabile = "NO";

		if (IsFascicoloSigeModificabile()) {
			lModificabile = "SI";
			// Non è possibile cancellare il Fascicolo SIEP legato alla Richiesta
			if (aIdFasSiep != null
					&& getFascicoloSigeEstesoInSessione().getRichiestaSige() != null
					&& getFascicoloSigeEstesoInSessione().getRichiestaSige().getFasSieIdFascicoloSiep() != null
					&& aIdFasSiep.compareTo(getFascicoloSigeEstesoInSessione().getRichiestaSige()
							.getFasSieIdFascicoloSiep()) == 0)
				lCancellabile = "NO";
			else
				lCancellabile = "SI";
		}

		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Cancellabile", lCancellabile);

	}

	/**
	 * Risale al ID_FAS_SIGE_SENTENZA in sessione. Il dato restituito costituisci l'identificativo univoco
	 * della relazione FascicoloSige-Sentenza memorizzata nella tabella FAS_SIGE_SENTENZA.
	 * 
	 * @return lIdFasSigeSentenza : BigDecimal
	 * @throws F3BException
	 */
	protected BigDecimal getIdFasSigeSentenzaInSessione() throws F3BException {
		if (isSessionAttributeNullObj(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA))
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Titolo Esecutivo non in sessione !!");

		BigDecimal lIdFasSigeSentenza = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);

		if (lIdFasSigeSentenza == null)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Non è possibile recuperare dalla sessione i del Titolo SIGE");

		return lIdFasSigeSentenza;
	}

	/**
	 * Questo metodo utilizza il LockController per lockare un'entità su cui operare. Se un'altra funzione
	 * richiama lo stesso metodo sulla stessa entità prima che essa sia stata rilasciata, verrà lanciata una
	 * eccezione che segnala il lock per impedire che due funzioni operino simultaneamente sulla stessa entità
	 * dello stesso procedimento SIGE. Il nome della entità da lockare viene passato come argomento mentre
	 * l'ID utilizzato è quello del Fascicolo SIGE in sessione, questo per lockare l'entità a livello
	 * dell'intero Procedimento. Se il Fascicolo SIGE non è in sessione viene lanciata un'eccezione.
	 * 
	 * @param aNomeEntita
	 * @throws F3BException
	 */
	protected void lockApplicativo(String aNomeEntita) throws F3BException {
		// Preleva il fascicolo dalla sessione.
		FascicoloSigeModel lFascicoloSige = getFascicoloSigeInSessione();

		// Lock per evitare accesso contemporaneo alla funzione chiamante che operi sullo stesso fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), aNomeEntita, lFascicoloSige
				.getIdFascicoloSige().toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null)
			throw new SIGEException(F3BException.USER_MESSAGE, "La gestione della  " + lck.getEntity()
					+ " per il Procedimento è in gestione ad un altro utente!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Fissato il Lock su " + aNomeEntita + " del Fascicolo con id:"
				+ lFascicoloSige.getIdFascicoloSige().toString());
	}

	/**
	 * La funzione effettua un lock sul Fascicolo in sessione. Può essere richiamato per bloccare modifiche
	 * concorrenti a dati legati al Fascicolo che stiano agendo sullo stesso Fascicolo Sige.
	 * 
	 * @throws F3BException
	 */
	protected void lockApplicativoFascicoloSige() throws F3BException {
		// Preleva il fascicolo dalla sessione.
		FascicoloSigeModel lFascicoloSige = getFascicoloSigeInSessione();

		// Lock per evitare accesso contemporaneo a funzioni che operino sullo stesso fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "FASCICOLO_SIGE", lFascicoloSige
				.getIdFascicoloSige().toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null)
			throw new SIGEException(F3BException.USER_MESSAGE,
					"La modifica dei dati del Procedimento SIGE è in gestione ad un altro utente!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Fissato il Lock su " + "FASCICOLO_SIGE" + " del Fascicolo con id:"
				+ lFascicoloSige.getIdFascicoloSige().toString());
	}

	protected void lockApplicativoSuReato() throws F3BException {
		// Preleva il reato dalla sessione.
		ReatoModel lReatoInSessione = (ReatoModel) getSessionAttribute("reato");
		if (lReatoInSessione == null)
			throw new SIGEException(F3BException.USER_MESSAGE, "Mancano i dati del reato in sessione !");

		// Lock per evitare accesso contemporaneo alla funzione chiamante che operi sullo stesso reato
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "reato", lReatoInSessione
				.getIdReato().toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null)
			throw new SIGEException(F3BException.USER_MESSAGE, "La gestione della  " + lck.getEntity()
					+ " per il Procedimento è in gestione ad un altro utente!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Fissato il Lock su REATO con id:" + lReatoInSessione.getIdReato().toString());
	}

	/**
	 * Funzione di utilità per ottenere la pagina di destinazione cui redirigere una Action. La funzione
	 * consente di passare un parametro alla Action da chiamare ed inoltre gestisce i parametri del bottone di
	 * ritorno.
	 * 
	 * @param aAction
	 *            : nome della Action cui redirigere;
	 * @param aNomeParametro
	 *            : nome del parametro da passare;
	 * @param aValoreParametro
	 *            : valore del parametro da passare.
	 * @return
	 * @throws Exception
	 */
	// STUB: da migliorare ed eventualmente spostare in ActioSiap. Luigi 21-10-2008
	protected String paginaDestinazione(String aAction, String aNomeParametro, String aValoreParametro)
			throws Exception {
		// Costruzione della pagina di redirect
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);

		lRedirectTo.setAction(aAction);
		if (aNomeParametro != null && aValoreParametro != null)
			lRedirectTo.setParameter(aNomeParametro, aValoreParametro);
		// Passaggio dei parametri inerenti il bottone di ritorno
		if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
			lRedirectTo.setParameter(IWebConstants.LINK_RITORNO,
					getRequestStringParameter(IWebConstants.LINK_RITORNO));
		else if (!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			lRedirectTo.setParameter(IWebConstants.FLAG_RITORNO,
					getRequestStringParameter(IWebConstants.FLAG_RITORNO));

		// Pagina di destinazione
		String lPage = lRedirectTo.toString();

		return lPage;
	}

	/**
	 * Effettua un controllo sui Provvedimenti legati al Fascicolo Sige in sessione. Controlla se esiste un
	 * provvedimento di tipo Definitorio.
	 * 
	 * @return lRet Condizione di modificabilità del fascicolo.
	 * @throws F3BException
	 *             Propagazione errori di eccezione.
	 */
	protected boolean esisteProvvedimentoDefinitorio() throws F3BException {
		boolean lRet = false;

		// STUB: da modificare ed usare una funzione nel controller che verifichi solo l'esistenza del
		// provvedimento. Luigi 16-2-2009
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvvEventoSige = lCtrlProv
				.ExRicercaProvvedimentoDefinitorioByIdFascicolo(getFascicoloSigeInSessione()
						.getIdFascicoloSige());

		// ProvvedimentoSigeModel lProvvedimento =
		// lCtrlProv.ExRicercaProvDefinitorioByFasc(getFascicoloSigeInSessione().getIdFascicoloSige());

		if (lProvvEventoSige != null && lProvvEventoSige.getProvvedimento() != null)
			lRet = true;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("EsisteProvvedimentoDefinitorio : " + (lRet ? "true" : "false"));
		return lRet;
	}

	/**
	 * La funzione passa nella request la combo per la modifica del Tipo Giudizio del Fascicolo in sessione e
	 * il tipo ufficio dell'utente connesso. La funzione ritorna il codice del tipo giudizio attualmente
	 * definito.
	 * 
	 * @return: String lCodTipoGiudizio
	 * @throws Exception
	 */
	/*
	 * protected String setComboTipoGiudizio() throws Exception { // Codice Tipo Ufficio Utente connesso
	 * String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio(); // // [FT] - 03/08/2016 -
	 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger() //
	 * siesLogger.debug("tipo ufficio : " + lCodTipoUfficio);
	 * 
	 * String lCodTipoGiudizio = (getFascicoloSigeInSessione().getCodTipoGiudizio()==null ? "-" :
	 * getFascicoloSigeInSessione().getCodTipoGiudizio().trim() ); Option lOptionGiudizio = new Option(
	 * DecodificheManager.getInstance().getTipoGiudizioSige(), lCodTipoGiudizio, Option.BLANK_ITEM); if
	 * (lCodTipoUfficio.compareTo("GIP")==0) lOptionGiudizio.setFilter("M"); else if
	 * ("CASAP_CAS_CAP".indexOf(lCodTipoUfficio)>=0) lOptionGiudizio.setFilter("C");
	 * lOptionGiudizio.setValueBlankItem("-"); setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString()
	 * ); setRequestAttribute("tipoUfficioUtente", lCodTipoUfficio );
	 * 
	 * // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() // siesLogger.debug("setComboTipoGiudizio : " + lOptionGiudizio);
	 * 
	 * return lCodTipoGiudizio; }
	 */

	protected static Set<String> filterColl = new HashSet<String>();
	protected static Set<String> filterMono = new HashSet<String>();
	protected static Set<String> filterAll = new HashSet<String>();

	static {
		filterColl.add("CAS"); // CAS = Corte Assise
		filterColl.add("CASAP");// CASAP = Corte Assise Appello
		filterColl.add("CAP");// CAP = Corte appello
		filterColl.add("CAPSM"); // CAPSM = Sezione per i Minorenni Corte di appello
		filterColl.add("DIBM");// DIBM = Tribunale per i Minorenni
		// MERGE v10 COLLAUDO: rimetto il filtro come era all'origine
		filterColl.add("GUPM");// GUPM = GUP Tribunale per i Minorenni

		// *** GIP ***
		filterMono.add("GIP"); // GIP = Gip Presso il Tribunale Ordinario
		filterMono.add("GIPMI"); // GIPMI = Gip presso il Tribunale Militare
		filterMono.add("GIPP"); // GIPP = Gip Pretura Circondariale
		filterMono.add("GIPPSD"); // GIPPSD = Gip presso Sezione Distaccara della Pretura Circondariale
		// MERGE v10 COLLAUDO: rimetto il filtro come era all'origine
		filterMono.add("GIPM");// GIPM = GIP Tribunale per i Minorenni

		// *** GUP ***
		filterMono.add("GUP"); // GUP = Gup Presso Tribunale Ordinario
		filterMono.add("GUPMI"); // GUPMI = Gup presso il Tribunale Militare
		filterMono.add("GUPP"); // GUPP = Gup Pretura Circondariale

		filterAll.add("DIB"); // DIB = Tribunale
		// MERGE v10 COLLAUDO: rimetto il filtro come era all'origine
		// filterAll.add("GUPM"); // GUPM = GUP Tribunale per i Minorenni
		// filterAll.add("GIPM"); // GIPM = GIP Tribunale per i Minorenni
	}

	protected String setComboTipoGiudizio() throws Exception {
		// Codice Tipo Ufficio Utente connesso
		String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		String lCodTipoGiudizio = (getFascicoloSigeInSessione().getCodTipoGiudizio() == null ? "-"
				: getFascicoloSigeInSessione().getCodTipoGiudizio().trim());
		Option lOptionGiudizio = new Option(DecodificheManager.getInstance().getTipoGiudizioSige(),
				lCodTipoGiudizio, Option.BLANK_ITEM);
		if (filterColl.contains(lCodTipoUfficio)) {
			// solo Rito Collegiale
			// lOptionGiudizio = new Option(DecodificheManager.getInstance().getTipoGiudizioSige(), "C");
			lOptionGiudizio.setFilter("C");
		} else if (filterMono.contains(lCodTipoUfficio)) {
			// solo Rito Monocratico
			// lOptionGiudizio = new Option(DecodificheManager.getInstance().getTipoGiudizioSige(), "M");
			lOptionGiudizio.setFilter("M");
		}
		lOptionGiudizio.setValueBlankItem("-");
		setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());
		setRequestAttribute("tipoUfficioUtente", lCodTipoUfficio);

		return lCodTipoGiudizio;
	}

	protected void setComboUfficioCompetente() throws Exception {
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// lOption.setFilter( new String[] {"-","CSS"} );
		// lOption.setSelected("-");
		lOption.setFilter(new String[] { "CSS" }); // solo l'Ufficio Competente della Corte Suprema di
													// Cassazione
		setRequestAttribute("ufficioCompetenteCorteSuprema", "" + lOption);
	}

	protected Option getComboTipoGiudizio(String lTipoGiudizio, String lCodTipoUfficio) throws Exception {
		// Carica Combo x TipoGiudizio.
		Option lOptionGiudizio = new Option(DecodificheManager.getInstance().getTipoGiudizioSige(),
				lTipoGiudizio, Option.BLANK_ITEM);

		if (filterColl.contains(lCodTipoUfficio)) {
			// solo Rito Collegiale
			lOptionGiudizio.setFilter("C");
		} else if (filterMono.contains(lCodTipoUfficio)) {
			// solo Rito Monocratico
			lOptionGiudizio.setFilter("M");
		} else if (filterAll.contains(lCodTipoUfficio)) {
			// Rito Collegiale / Monocratico
			// lOptionGiudizio.setFilter("*");
		} else {
			// no option
			lOptionGiudizio.setFilter("");
		}

		// 20170907: [SG] aggiunto controllo
		if ("TRIBSD".equals(lCodTipoUfficio))
			lOptionGiudizio = new Option(DecodificheManager.getInstance().getTipoGiudizioSige(),
					lTipoGiudizio, Option.NO_BLANK_ITEM);
		else
			lOptionGiudizio.setValueBlankItem("-");
		return lOptionGiudizio;
	}

	protected boolean showDestinatariImpugnazioni(ImpugnazioneSigeModel impugnazione) {
		boolean show = false;
		String codImpugnazione = impugnazione.getCodTenoreDecisione();

		String codTipoImpugnazione = impugnazione.getCodTipoImpugnazione();
		if (codTipoImpugnazione.equals("04"))
			return show;

		if (codImpugnazione == null || codImpugnazione.equals("-"))
			return show;

		switch (Short.parseShort(codImpugnazione)) {
		case ICostantiImpugnazioneSige.SHORT_COD_ESITO_ANNULLA_SENZA_RINVIO:
		case ICostantiImpugnazioneSige.SHORT_COD_ESITO_ANNULLA_PARZIALMENTE:
		case ICostantiImpugnazioneSige.SHORT_COD_ESITO_DICHIARA_INAMISSIBILE_IL_RICORSO:
		case ICostantiImpugnazioneSige.SHORT_COD_ESITO_RIGETTA:
		case ICostantiImpugnazioneSige.SHORT_COD_ESITO_RETTIFICA:
		case ICostantiImpugnazioneSige.SHORT_COD_ESITO_ACCOGLIE:
		case ICostantiImpugnazioneSige.SHORT_COD_ESITO_DICHIARA_NDP_NLP:
			show = true;
			break;
		default:
			break;
		}
		return show;
	}

	protected boolean showDestinatariImpugnazioniOpposizione(ImpugnazioneSigeModel impugnazione) {
		boolean show = false;
		String codImpugnazione = impugnazione.getCodTenoreDecisione();
		String codTipoImpugnazione = impugnazione.getCodTipoImpugnazione();
		if (codTipoImpugnazione.equals("01"))
			return show;

		if (codImpugnazione == null || codImpugnazione.equals("-"))
			return show;

		switch (Short.parseShort(codImpugnazione)) {
		case ICostantiImpugnazioneSige.SHORT_COD_ESITO_RIGETTA:
		case ICostantiImpugnazioneSige.SHORT_COD_ESITO_DICHIARA_INAMISSIBILE:
			show = true;
			break;
		default:
			break;
		}
		return show;
	}

	/*
	 * ISSUE MEV 	: aggiunti metodi per controllo età soggetto
	 * Numero MEV 	: 57
	 * Autore 		: Gioggi
	 * Data 		: 15/gen/2018
	 * Branch 		: MEV_57
	 */
	private static Set<String> ufficiMinori = new HashSet<String>();
	static {
		ufficiMinori.add("PMM");
		ufficiMinori.add("DIBM");
		ufficiMinori.add("GIPM");
		ufficiMinori.add("GUPM");
		ufficiMinori.add("CAPSM");
		ufficiMinori.add("TDSM");
		ufficiMinori.add("UDSM");
	}

	protected String checkMinori() throws F3BException {
		String ret = "";
		if (getUtenteConnesso().getUfficioUtente() != null
				&& getUtenteConnesso().getUfficioUtente().getCodTipoUfficio() != null) {
			String tipoUfficioUtente = getUtenteConnesso().getUfficioUtente().getCodTipoUfficio();
			if (!ufficiMinori.contains(tipoUfficioUtente)) {
				ret = getUtenteConnesso().getUfficioUtente().getCodUfficio();
			}
		}
		return ret;
	}
	// ***** FINE INTERVENTO MEV_57 *****//
	
	protected boolean IsFascicoloUnificato() throws F3BException {
		FascicoloSigeModel aFascicolo= getFascicoloSigeInSessione();
		
		boolean lRet = false;

		if ( aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("05")	
				&& getCodUfficioUtenteConnesso().equalsIgnoreCase(aFascicolo.getChiaveUfficio()))
			lRet = true;

		return lRet;
	}
	
	protected boolean IsFascicoloDefinito() throws F3BException {
		FascicoloSigeModel aFascicolo= getFascicoloSigeInSessione();
		
		boolean lRet = false;

		if (( aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("01")	|| aFascicolo.getCodStatoFascicolo().equalsIgnoreCase("06")	 )
				&& getCodUfficioUtenteConnesso().equalsIgnoreCase(aFascicolo.getChiaveUfficio()))
			lRet = true;

		return lRet;
	}
	
	/**
	 * metodo che rimuove dalla sessione ogni attributo il cui nome è definito in array
	 * 
	 */
	protected void pulisciSessione(){		
		
		 String lNomi [] = { "fascicolo","soggetto","sentenza",
			        "FascicoloSigeEsteso","IdEventoInviato",  "IDepositoDecreto","magistratorelatore","eventoNotTA","avvocatoSius" };		
		
	     int lNumNomi = lNomi.length;
	     for (int i = 0; i < lNumNomi; i++)
	     {
	       if( ! this.isSessionAttributeNullObj(lNomi[i]))
	       {
	         this.removeSessionAttribute(lNomi[i]);
	         // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	         siesLogger.debug( "Rimosso dalla sessione : " +lNomi[i]);
	       }
	     }
	}

}