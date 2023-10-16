package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.jms.util.ParserMessageRec;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoDepositoModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * Aggiunta classe action di caricamento dati
 *
 * @author Gioggi
 * @since MEV_9
 * @version 1.0
 */
public class ActLoadRegistrazioneEsecutivitaApplicazioneProvvisoriaMA extends ActionSius
		implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// variabile di classe
	private FascicoloSiepModel fsm = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();

		String retPage = null; // pagina di input
		FascicoloGPModel fgpm = null;
		boolean fascicoloInSessione = false;

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO)) {
			fgpm = ricercaFascicolo();
		} else {
			// il Fascicolo è in sessione
			if (isSessionAttributeNullObj("fascicoloSiusGP"))
				throw new SIUSException(SIUSException.USER_MESSAGE, "Dati del Fascicolo non in sessione!");
			fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
			fascicoloInSessione = true;
		}
		if (fgpm.getTenori() == null || fgpm.getTenori().length == 0) {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Esecutivita' Ordinanza Applicazione Provvisoria M.A. non consentita con campo Oggetto vuoto!");
		}

		retPage = analisiStatoFascicolo(fgpm);

		if (!fascicoloInSessione) {
			setSessionAttribute("fascicoloSiusGP", fgpm);
			setSessionAttribute("fascicolo", fsm);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return retPage;
	}

	// Il Fascicolo viene cercato nel DB attraverso le chiavi ANNO e PROG
	private FascicoloGPModel ricercaFascicolo() throws Exception {

		if (isRequestParameterNullObj(CAMPO_CHIAVE_ANNO) || isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Assenti ANNO/PROG!");

		FascicoloGPModel fgpm = null;
		IFascicoloSius ifss = SIUSLookupRemote.getFascicoloSiusRemote();
		fgpm = ifss.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
				getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
				getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), getCodUfficioUtenteConnesso());

		// Si cerca il fascicolo SIEP da mettere in sessione
		if (fgpm != null && fgpm.getFascicoloSiusModel() != null
				&& fgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			IFascicoloSiep ifsp = SIEPLookupRemote.getFascicoloSiepRemote();
			fsm = ifsp
					.ExRicercaFascicoloByKeyNoError(fgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		}

		return fgpm;
	}

	/**
	 * La funzione analizza il Fascicolo SIUS ed in base allo stato prepara la form da presentare. I casi
	 * sono: 1) STATO = COD_UNIFICATO, COD_EMESSO_PROVVEDIMENTOO : viene lanciata un'eccezione, l'operazione
	 * non può essere eseguita. 2) STATO = COD_DEFINITO : il fascicolo è già in stato definito, viene
	 * visualizzato il dettaglio della definizione. 3) Negli altri casi viene preparata la form di input per
	 * la Esecutivita' Ordinanza Applicazione Provvisoria M.A. del procedimento.
	 *
	 * @param fgpm
	 * @return String pagina di input o di dettaglio
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String analisiStatoFascicolo(FascicoloGPModel fgpm) throws Exception {

		String retPage = PG_LOAD_ESECUTIVITA_ORDINANZA_APPLICAZIONE_PROVVISORIA_MA;
		if (fgpm == null || fgpm.getFascicoloSiusModel() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo non trovato!");

		// Ricerco evento del fascicolo:
		// Ordinanza Affidamento in Prova al Servizio Sociale (Art. 47 O.P. - Art. 678 comma 1-ter
		// c.p.p.) - Applica provvisoriamente
		IEvento ie = SICOLookupRemote.getEventoRemote();
		Vector<?> v = ie.ExRicercaEventoByFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius(),
				null);
		boolean existOrdinanzaApplicazioneProvvisoria = false;
		for (int i = 0; i < v.size(); i++) {
			EventoDepositoModel edm = (EventoDepositoModel) v.elementAt(i);
			if ("0270".equals(edm.getCodEsito()) && "S".equals(edm.getFlagDocumentoRegistrato())
					&& edm.getNumAllValidati() > 0) {
				existOrdinanzaApplicazioneProvvisoria = true;
				setRequestAttribute("eventoDepositoModel", edm);
				INotifica in = SIEPLookupRemote.getNotificaRemote();
				Date maxDataAvvenutaNotifica = in.ExRicercaDataNotifica(edm.getIdEvento());
				String mdan = "";
				if (maxDataAvvenutaNotifica != null)
					mdan = DateUtils.getDateToString(maxDataAvvenutaNotifica, "dd/MM/yyyy");
				setRequestAttribute("maxDataAvvenutaNotifica", mdan);
				siesLogger.debug("maxDataAvvenutaNotifica = " + mdan);
				break;
			}
		}
		if (!existOrdinanzaApplicazioneProvvisoria)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione consentita solo se sul Procedimento sia stata emessa un'ordinanza di "
							+ "Applicazione Provvisoria M.A. con esito 'Applica provvisoriamente' "
							+ "depositata e validata!");

		if (fgpm.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_UNIFICATO))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita su Procedimento Unificato!");

		if (fgpm.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_EMESSO_PROVVEDIMENTOO)
				&& !existOrdinanzaApplicazioneProvvisoria)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita su Procedimento con Provvedimento!");

		if (getCodUfficioUtenteConnesso().compareTo(fgpm.getFascicoloSiusModel().getChiaveUfficio()) != 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita per Procedimento di altro ufficio!");

		// controllo consistenza della data esecutivita'
		IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepositoOrdinanzaPcModel dopm = idopc.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
				fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "AM");
		// DepositoOrdinanzaPcModel dopm = new DepositoOrdinanzaPcModel();
		// dopm.setGenPridGeneraleProcedimento(
		// fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		// Vector<?> depositoOrdinanzaVector = idopc.ExRicercaDepositoOrdinanzaPc(dopm);
		// setRequestAttribute("depositoOrdinanzaVector", depositoOrdinanzaVector);
		if (!Utils.isNullObj(dopm) && Utils.isPresent(dopm.getDataEsecutivita())) {
			if (isRequestParameterNullObj("provenienza")) {
				// Prepara la "pagina" di destinAction
				RedirectTo rt = new RedirectTo();
				rt.setPage(IWebConstants.PG_MAIN);
				rt.setAction(
						"siap.sius.fascicolo.action.ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(getClass().getName() + ".processRequest: fine");

				// valore di ritorno
				return rt.toString();
			} else {
				setRequestAttribute("dataEsecutivita",
						DateUtils.getDateToString(dopm.getDataEsecutivita(), "dd/MM/yyyy"));
				setRequestAttribute("noteDataEsecutivita", dopm.getNoteDataEsecutivita());
				setRequestAttribute("provenienza", "modifica");
			}
		}

		// possibile inserire Restituzione Procedimento e quindi lock
		// Lock per evitare più definizioni contemporanee del Fascicolo
		LockModel lm = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS",
				fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString(), getCodUtenteConnesso(),
				getSession().getId());
		if (lm != null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Il " + lm.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi!");

		// Ricerca Notifiche eventualmente gia' emesse
		ricercaNotifiche(dopm.getIdEventoGenerato());

		// Ricerca dell'elenco dei possibili destinatari
		Vector listaDestinatari = new Vector(DecodificheManager.getInstance().getDestinatarioDeposito());
		UfficioModel um = getUfficioUtenteConnesso();

		// Per gli uffici TDS il campo USSM non deve essere visualizzato
		Vector listaDestinatariAppo = new Vector();
		for (Object destinatari : listaDestinatari) {
			DecodificheModel destinatario = (DecodificheModel) destinatari;
			if ("USSM".equals(destinatario.getCode()) && "TDS".equals(um.getCodTipoUfficio())) {
				// l'elemento non viene aggiunto al vettore di appoggio
				// poiche' il campo USSM deve essere visibile solo agli uffici dei minori
				siesLogger.debug("Il campo USSM deve essere visibile solo agli uffici dei minori");
			} else {
				listaDestinatariAppo.add(destinatario);
			}
		}
		listaDestinatari = listaDestinatariAppo;

		if ("TDSM".equals(um.getCodTipoUfficio()))
			// Ricerca dell'elenco dei possibili destinatari consideranno i minorenni
			listaDestinatari = new Vector(
					DecodificheManager.getInstance().getDestinatarioDepositoMinorenni());

		// Ricerca Avvocati e Luogo Detenzione
		LuogoDetenzioneModel ldm = leggiAvvocatiLuogoDetenzione(fgpm);
		// LISTA UFFICI SOGGETTO
		Option optionSog = null;
		if (ldm != null && ldm.getIstitutoDetenzione() != null
				&& ldm.getIstitutoDetenzione().getCodTipoIstituto().length() > 0)
			optionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(),
					ldm.getIstitutoDetenzione().getCodTipoIstituto(), 75);
		else
			optionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);

		// LISTA UFFICI
		Collection tipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		String filtroMinorenni = super.getFiltroMinorenni();
		String[] stringFilter = null;
		if ("true".equals(filtroMinorenni))
			stringFilter = new String[] { "-", "22", "A2" };
		else
			stringFilter = new String[] { "-", "22" };

		if ("true".equals(filtroMinorenni))
			stringFilter = new String[] { "-", "22", "A2", "C1" };
		else
			// stringFilter = new String[] { "-", "22" };
			stringFilter = new String[] { "-", "22", "C0", "C1" };

		Option optionAvv = new Option();
		if (isRequestParameterNullObj("Aggiungi")) {
			optionAvv = new Option(tipoIstituto, "C1", 75); // predefinito
		} else {
			optionAvv = new Option(tipoIstituto, "-", 75);
		}
		optionAvv.setFilter(stringFilter);

		Collection<DecodificheModel> autorita = new Vector<>();
		autorita.addAll(DecodificheManager.getInstance().getTipoAutorita());
		if (filtroMinorenni.equalsIgnoreCase("true")) {
			autorita.addAll(DecodificheManager.getInstance().getTipoAutoritaMinorenni());
			Collections.sort((List<DecodificheModel>) autorita, new DecodificheModel.OrderByDescrizione());
		}
		Option optionAut = new Option(autorita, 75);

		Collection<DecodificheModel> options = new Vector<>();
		options.addAll(DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep());

		if (filtroMinorenni.equalsIgnoreCase("true"))
			options.add(new DecodificheModel("TDS", "Tribunale di Sorveglianza", "TIPO_UFFICIO_CUMULO", "",
					"", "", "", "", ""));

		// Questi uffici devono essere visibili sia per gli uffici maggiorenni che per gli uffici minorenni
		options.add(new DecodificheModel("TDSM",
				"Tribunale per i Minorenni in Funzione di Tribunale di Sorveglianza", "TIPO_UFFICIO_CUMULO",
				"", "", "", "", "", ""));
		Option optionAltreAut = new Option(options, "-");

		setRequestAttribute("destDeposito", listaDestinatari);
		setRequestAttribute("TipiIstitutiSog", "" + optionSog);
		setRequestAttribute("TipiIstituti1", "" + optionAvv);
		setRequestAttribute("tipoAutorita", optionAut.toString());
		setRequestAttribute("altreAutoritaGiudiziarie", optionAltreAut.toString());

		if (!isRequestParameterNullObj("Aggiungi"))
			setRequestAttribute("Aggiungi", "yes");
		else
			setRequestAttribute("Aggiungi", "no");

		// valore di ritorno
		return retPage;
	}

	/**
	 * Effettua la ricerca delle Eventuali Notifiche gia' emesse per il Decreto Depositato. Se presenti esse
	 * vengono passate alla request.
	 */
	private void ricercaNotifiche(BigDecimal idEvento) throws Exception {

		// Flag per indicare la presenza o meno della Notifica al Soggetto
		String trovatoSog = "NO";
		// Flag per indicare la presenza o meno delle Notifiche
		String trovateNotifiche = "NO";

		// Lettura delle notifiche.
		INotifica in = SIEPLookupRemote.getNotificaRemote();
		Vector<NotificaModel> notifiche = in.ExRicercaEstesaNotificaByKeyEvento(idEvento);
		setRequestAttribute("notifiche", notifiche);
		if (notifiche.size() > 0) {
			trovateNotifiche = "SI";
			Iterator<NotificaModel> itx2 = notifiche.iterator();
			while (itx2.hasNext()) {
				NotificaModel notifica = itx2.next();
				if (notifica.getSogIdSoggetto() != null) {
					trovatoSog = "SI";
				}
			}
		}
		setRequestAttribute("notificheSog", trovatoSog);
		setRequestAttribute("NotifichePresenti", trovateNotifiche);
	}

	/**
	 * Effettua la ricerca degli Avvocati e del Luogo di detenzione. Se presenti vengono passati alla request.
	 *
	 * @param fgpm
	 */
	private LuogoDetenzioneModel leggiAvvocatiLuogoDetenzione(FascicoloGPModel fgpm) throws Exception {

		// Preleva dati AVVOCATI e LUOGODETENZIONE
		ParserMessageRec pmr = null;
		IStampaSius iss = SIUSLookupRemote.getStampaRemote();
		// l'Array contenente le tipologie di dati da prelevare
		int[] tipoDati = { ICostantiStampaSius.TREE_LUOGODET, ICostantiStampaSius.TREE_AVVOCATOSIUS };
		// Creazione del TreeModel con i dati che occorrono
		TreeModel tm = iss.ExPrelevaDatiVideo(fgpm.getFascicoloSiusModel().getIdFascicoloSius(), tipoDati);
		// Converte i dati ottenuti per utilizzarli come model
		pmr = new ParserMessageRec(tm);
		LuogoDetenzioneModel ldm = pmr.getLuogoDetenzione();
		setRequestAttribute("luogodet", ldm);
		setRequestAttribute("avvocato", pmr.getAvvocatoSius());
		return ldm;
	}

}