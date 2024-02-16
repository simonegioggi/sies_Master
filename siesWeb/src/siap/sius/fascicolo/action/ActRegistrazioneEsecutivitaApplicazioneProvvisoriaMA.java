package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.curatore.action.ICostantiCuratore;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sius.udienza.action.ICostantiUdienza;
import siap.sius.util.SIUSLookupRemote;

/**
 * Aggiunta classe action di inserimento dati
 *
 * @author Gioggi
 * @since MEV_9
 * @version 1.0
 */
public class ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA extends ActionSius
		implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String[] check = null;
	boolean existOrdinanzaApplicazioneProvvisoria = false;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();

		String retPage = PG_DETTAGLIO_ESECUTIVITA_ORDINANZA_APPLICAZIONE_PROVVISORIA_MA;
		FascicoloGPModel fgpm = null;

		// il Fascicolo è in sessione
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati del Fascicolo non in sessione!");
		fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (fgpm == null || fgpm.getFascicoloSiusModel() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo non trovato!");

		if (getCodUfficioUtenteConnesso().compareTo(fgpm.getFascicoloSiusModel().getChiaveUfficio()) != 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita per Procedimento di altro ufficio!");

		// possibile inserire Restituzione Procedimento e quindi lock
		// Lock per evitare più definizioni contemporanee del Fascicolo
		LockModel lm = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS",
				fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString(), getCodUtenteConnesso(),
				getSession().getId());
		if (lm != null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Il " + lm.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi!");

		// Ricerco evento del fascicolo:
		// Ordinanza Affidamento in Prova al Servizio Sociale (Art. 47 O.P. - Art. 678 comma 1-ter
		// c.p.p.) - Applica provvisoriamente
		EventoModel em = new EventoModel();
		IEvento ie = SICOLookupRemote.getEventoRemote();
		if (!isRequestParameterNullObj("IdEvento")) {
			em = ie.ExRicercaEventoByKey(getRequestBigDecimalParameter("IdEvento"));
			controllaEvento(em, true);
		} else {
			Vector<?> v = ie.ExRicercaEventoByFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius(),
					null);
			for (int i = 0; i < v.size(); i++) {
				em = (EventoModel) v.elementAt(i);
				controllaEvento(em, false);
				if (existOrdinanzaApplicazioneProvvisoria)
					break;
			}
		}
		if (!existOrdinanzaApplicazioneProvvisoria)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione consentita solo se sul Procedimento sia stata emessa un'ordinanza di "
							+ "Applicazione Provvisoria M.A. con esito 'Applica provvisoriamente' "
							+ "depositata e validata!");

		// aggiorno dati sulla tabella "deposito_ordinanza_pc" (colonne "DATA_ESECUTIVITA" e "NOTE_ATTI")
		IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepositoOrdinanzaPcModel dopm = idopc.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
				fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "AM");
		DepositoOrdinanzaPcModel dopcmMA = idopc.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
				fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "MA");
		boolean existConfermaDecisioneMR = false;
		if (!Utils.isNullObj(dopcmMA)) {
			BigDecimal idEventoMA = dopcmMA.getIdEventoGenerato();
			EventoModel emMA = ie.ExRicercaEventoByKey(idEventoMA);
			if (!"A".equals(emMA.getFlagDocumentoRegistrato()))
				existConfermaDecisioneMR = true;
		}
		// DepositoOrdinanzaPcModel dopm = new DepositoOrdinanzaPcModel();
		// dopm.setGenPridGeneraleProcedimento(
		// fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		// Vector<?> depositoOrdinanzaVector = idopc.ExRicercaDepositoOrdinanzaPc(dopm);
		// setRequestAttribute("depositoOrdinanzaVector", depositoOrdinanzaVector);
		// controllo consistenza della data esecutivita': se non esiste allora la gestisco
		if ((!Utils.isPresent(dopm.getDataEsecutivita()) || !isRequestParameterNullObj("provenienza"))
				&& !existConfermaDecisioneMR) {
			dopm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			dopm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			dopm.setDataAggiornamento(DateUtils.getSysDate());
			if ("cancella".equals(getRequestStringParameter("provenienza"))) {
				dopm.setDataEsecutivita(null);
				dopm.setNoteDataEsecutivita(null);
				idopc.ExModificaDepositoOrdinanzaPc(dopm);
				// messaggio di ritorno
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione Avvenuta Correttamente!");
				// Prepara la "pagina" di destinAction
				RedirectTo rt = new RedirectTo();
				rt.setPage(IWebConstants.PG_MAIN);
				rt.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
				rt.setParameter(CAMPO_ID_FASCICOLO_SIUS,
						fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(getClass().getName() + ".processRequest: fine");

				// valore di ritorno
				return IWebConstants.PG_MESSAGE; /* rt.toString(); */
			} else {
				if (!isRequestParameterNullEmptyObj(CAMPO_NOTE))
					dopm.setNoteDataEsecutivita(getRequestStringParameter(CAMPO_NOTE));
				dopm.setDataEsecutivita(getRequestDateParameter(CAMPO_ANNO_DATA_ESECUTIVITA,
						CAMPO_MESE_DATA_ESECUTIVITA, CAMPO_GIORNO_DATA_ESECUTIVITA));
				
				// MEV9 si aggiorna anche Misura Alternativa
				//idopc.ExModificaDepositoOrdinanzaPc(dopm);				
				idopc.ExAggiornaDataEsecutivitaDepositoOrdinanzaPc(dopm);
				// MEV9 - FINE
				
				INotifica in = SIEPLookupRemote.getNotificaRemote();
				if ("modifica".equals(getRequestStringParameter("provenienza"))) {
					Vector<NotificaModel> notifiche = leggiNotifiche(em);
					in.ExModificaNotifiche(notifiche, check);
				} else {
					// Preparo le notifiche.
					Vector<NotificaModel> notifiche = new Vector<>();
					// Ciclo di caricamento delle notifiche.
					leggiNuoviDestinatari(notifiche, em.getIdEvento());
					leggiNotificheAvvocatiAltroDestinatario(notifiche, em.getIdEvento());
					leggiNotificaAlSoggetto(notifiche, em.getIdEvento(),
							fgpm.getFascicoloSiusModel().getSogIdSoggetto());
					in.ExInserisciNotifiche(new ArrayList<>(notifiche));
				}
			}
		}

		// Lettura delle notifiche
		INotifica in = SIEPLookupRemote.getNotificaRemote();
		siesLogger.debug(">>>>> IdEventoGenerato = " + dopm.getIdEventoGenerato());
		Vector<NotificaModel> notifiche = in.ExRicercaEstesaNotificaByKeyEvento(dopm.getIdEventoGenerato());
		siesLogger.debug(">>>>> Numero notifiche = " + notifiche.size());
		setRequestAttribute("notifiche", notifiche);

		setRequestAttribute("dataEsecutivita", dopm.getDataEsecutivita());
		setRequestAttribute("noteDataEsecutivita", dopm.getNoteDataEsecutivita());
		setRequestAttribute("Upload", "NO");
		setRequestAttribute("ListaTemplate", "SIUS_OR_0270");
		setRequestAttribute("existConfermaDecisioneMR", "" + existConfermaDecisioneMR);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return retPage;
	}

	private boolean controllaEvento(EventoModel em, boolean existIdEvento) throws F3BException {

		if (existIdEvento) {
			existOrdinanzaApplicazioneProvvisoria = true;
			setRequestAttribute("eventoModel", em);
		} else {
			if ("0270".equals(em.getCodEsito()) && "S".equals(em.getFlagDocumentoRegistrato())
					&& em.getNumAllValidati() > 0) {
				existOrdinanzaApplicazioneProvvisoria = true;
				setRequestAttribute("eventoModel", em);
				INotifica in = SIEPLookupRemote.getNotificaRemote();
				Date maxDataAvvenutaNotifica = in.ExRicercaDataNotifica(em.getIdEvento());
				String mdan = "";
				if (maxDataAvvenutaNotifica != null)
					mdan = DateUtils.getDateToString(maxDataAvvenutaNotifica, "dd/MM/yyyy");
				setRequestAttribute("maxDataAvvenutaNotifica", mdan);
				siesLogger.debug("maxDataAvvenutaNotifica = " + mdan);
			}
		}
		return existOrdinanzaApplicazioneProvvisoria;
	}

	private void leggiNotificaAlSoggetto(Vector<NotificaModel> notifiche, BigDecimal idEvento,
			BigDecimal idSoggetto) throws Exception {

		siesLogger.debug(
				"ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA.leggiNotificaAlSoggetto: inizio");

		String destinatarioSog = null;
		String istitutoDetenzione = null;
		String sedeSog = null;
		String nota = null;

		if (!isRequestParameterNullObj("nota_soggetto"))
			nota = getRequestStringParameter("nota_soggetto");

		// Preleva l'id dell'istituto detenzione
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			istitutoDetenzione = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		} else {
			if (!isRequestParameterNullObj(ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE)) {
				sedeSog = getRequestStringParameter(ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE);
				destinatarioSog = getRequestStringParameter(ICostantiUdienza.CAMPO_COD_IST_DETENZIONE);
			}
		}

		// Soggetto con autorita' esterna
		if (destinatarioSog != null && sedeSog != null) {
			if (!destinatarioSog.equals("-") && !sedeSog.equals("")) {
				String codComuneSede = getCodComuneByDescrFlagVal(sedeSog).getCodComune();
				NotificaModel nm = new NotificaModel();
				nm.setCodTipoNotifica("N");
				nm.setDataInvio(getRequestDateParameter(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE));
				nm.setNote(nota);
				nm.setCodOperatoreInserimento(getCodUtenteConnesso());
				nm.setDataInserimento(DateUtils.getSysDate());
				nm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				nm.setCodEsito("-");
				nm.setUffCodUfficio("-");
				nm.setSogIdSoggetto(idSoggetto);
				// Crea Model Autorità Esterna
				AutoritaEsternaModel aem = new AutoritaEsternaModel();
				aem.setCodTipoAutorita(destinatarioSog);
				aem.setCodSede(codComuneSede);
				aem.setCodOperatoreInserimento(getCodUtenteConnesso());
				aem.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				aem.setDataInserimento(DateUtils.getSysDate());
				// Controllo sede UNEP
				controlloAutorita(aem, sedeSog);
				// Aggiunge il model Autorità Esterna alla Notifica
				nm.setAutoritaEsterna(aem);
				// Aggiunge il model delle notifiche al vettore.
				nm.setEveIdEvento(idEvento);
				notifiche.add(nm);
			}
		}

		// Soggetto con id
		if (istitutoDetenzione != null) {
			NotificaModel nm = new NotificaModel();
			nm.setCodTipoNotifica("N");
			nm.setDataInvio(getRequestDateParameter(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE,
					ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE));
			nm.setNote(nota);
			nm.setCodOperatoreInserimento(getCodUtenteConnesso());
			nm.setDataInserimento(DateUtils.getSysDate());
			nm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			nm.setCodEsito("-");
			nm.setUffCodUfficio("-");
			nm.setIstDetIdIstitutoDetenzione(istitutoDetenzione);
			nm.setSogIdSoggetto(idSoggetto);
			// Aggiunge il model delle notifiche al vettore.
			nm.setEveIdEvento(idEvento);
			notifiche.add(nm);
		}
		return;
	}

	private void leggiNotificheAvvocatiAltroDestinatario(Vector<NotificaModel> notifiche, BigDecimal idEvento)
			throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				"ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA.leggiNotificheAvvocatiAltroDestinatario: inizio");

		String sedi[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		String destinatari[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
		String note[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_NOTE);
		String avvocato[] = getRequestStringParameters(ICostantiUdienza.CAMPO_COD_AVVOCATO);
		String tipoNotifica[] = getRequestStringParameters(ICostantiRichiestaAtti.CODTIPONOTIFICA);
		String viaFax[] = null;

		// Avvocati & Altro Destinatario
		int length = destinatari.length;
		for (int x = 0; x < length; x++) {
			if (!destinatari[x].trim().equals("-")) {
				String codComuneSede = "-";
				if (!sedi[x].trim().equals("")) {
					codComuneSede = getCodComuneByDescrFlagVal(sedi[x]).getCodComune();
				}
				NotificaModel nm = new NotificaModel();
				nm.setDataInvio(getRequestDateParameter(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE));
				nm.setNote(note[x]);
				nm.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				nm.setDataInserimento(DateUtils.getSysDate());
				nm.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				nm.setCodEsito("-");
				nm.setUffCodUfficio("-");
				nm.setCodTipoNotifica(tipoNotifica[x]);
				if (viaFax != null && viaFax.length > 0) {
					int l = viaFax.length;
					if (x < l && viaFax[x] != "")
						nm.setFlagNotificaViaFax(new BigDecimal(viaFax[x]));
				}
				if (avvocato[x] != null && avvocato[x].trim().length() > 1) {
					nm.setCodTipoNotifica("N");
					BigDecimal idAvvocato = new BigDecimal(avvocato[x]);
					nm.setAvvIdAvvocatoFascicoloSius(idAvvocato);
				} else if (avvocato[x] != null && avvocato[x].trim().compareTo("C") == 0) {
					nm.setCodTipoNotifica("N");
					BigDecimal idCur = new BigDecimal(
							getRequestStringParameter(ICostantiCuratore.CAMPO_ID_CURATORE));
					nm.setCurIdCuratore(idCur);
				}
				// Crea Model Autorità Esterna
				AutoritaEsternaModel aem = new AutoritaEsternaModel();
				aem.setCodTipoAutorita(destinatari[x]);
				aem.setCodSede(codComuneSede);
				aem.setCodOperatoreInserimento(getCodUtenteConnesso());
				aem.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				aem.setDataInserimento(DateUtils.getSysDate());

				// controllo sede UNEP
				controlloAutorita(aem, sedi[x]);

				// Aggiunge il model Autorità Esterna alla Notifica
				nm.setAutoritaEsterna(aem);
				// Aggiunge il model delle notifiche al vettore.
				nm.setEveIdEvento(idEvento);
				notifiche.add(nm);
			}
		}
	}

	private void leggiNuoviDestinatari(Vector<NotificaModel> destinatari, BigDecimal idEvento)
			throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug("ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA.leggiNuoviDestinatari: inizio");

		// Elenco delle Notifiche a Nuovi Destinatari
		String[] codNotifiche = new String[0];
		String[] sedi = null;
		String[] note = null;

		if (!isRequestParameterNullObj("cod_destinatari")) {
			codNotifiche = getRequestStringParameters("cod_destinatari");
			sedi = getRequestStringParameters("sede_destinatari");
			note = getRequestStringParameters("nota_destinatari");
			// Lettura dei campi valorizzati
			for (int i = 0; i < codNotifiche.length; i++) {
				// Se è valorizzata la sede del Destinatario si inserisce una Notifica
				if (sedi[i] != null && sedi[i].trim().length() > 0) {
					NotificaModel nm = new NotificaModel();
					nm.setDataInvio(getRequestDateParameter(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE,
							ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE,
							ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE));
					nm.setNote(note[i]);
					nm.setCodOperatoreInserimento(getCodUtenteConnesso());
					nm.setDataInserimento(DateUtils.getSysDate());
					nm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					nm.setCodEsito("-");
					nm = inserisciSedeDestinatario(nm, codNotifiche[i], sedi[i]);
					nm.setEveIdEvento(idEvento);
					destinatari.add(nm);
				}
			}
		}

		return;
	}

	private NotificaModel inserisciSedeDestinatario(NotificaModel nm, String code, String descrSede)
			throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				"ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA.inserisciSedeDestinatario: inizio");

		// Destinatario Interno/Esterno.
		String codiceAlternativo = DecodificheUtils
				.getCodAltebyCode(DecodificheManager.getInstance().getDestinatarioDeposito(), code);
		String filtro = DecodificheUtils
				.getFiltrobyCode(DecodificheManager.getInstance().getDestinatarioDeposito(), code);
		if (!Utils.isPresent(codiceAlternativo)) {
			codiceAlternativo = DecodificheUtils
					.getCodAltebyCode(DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep(), code);
			filtro = DecodificheUtils
					.getFiltrobyCode(DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep(), code);
		}
		nm.setCodTipoNotifica("C");

		// Per le altre autorità Giudiziarie (Uffici interni) il Codice non è valorizzato
		if (codiceAlternativo.equals("-") || codiceAlternativo.equals("")) {
			String lCodUfficioDestinatario = getCodiceUfficioDest(filtro, descrSede, code);
			// Se interno, valorizzo il Codice CSSA o il CodiceUfficioDestinazione.
			if (filtro.equalsIgnoreCase("CSSA"))
				nm.setCssIdCssa(new BigDecimal(lCodUfficioDestinatario));
			else
				nm.setUffCodUfficio(lCodUfficioDestinatario);
		} else {
			// Valorizzo l'autorità esterna.
			AutoritaEsternaModel aem = new AutoritaEsternaModel();
			aem.setCodTipoAutorita(codiceAlternativo);
			aem.setCodSede(getCodComuneByDescrFlagVal(descrSede.toUpperCase()).getCodComune());
			aem.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			aem.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			aem.setDataInserimento(DateUtils.getSysDate());
			// Controllo sede UNEP
			controlloAutorita(aem, descrSede);
			// Setto l'Autorita Esterna per la Notifica corrente.
			nm.setAutoritaEsterna(aem);
		}

		return nm;
	}

	// La funzione controlla nel caso l'autorità esterna sia UNEP che il comune indicato come sede sia una
	// sede UNEP
	private void controlloAutorita(AutoritaEsternaModel aem, String descrSede) throws F3BException {

		IComune ic = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA.controlloAutorita: inizio");
		if (aem != null && aem.getCodTipoAutorita() != null && descrSede != null)
			// Controllo Comune sede UNEP
			if (aem.getCodTipoAutorita().equalsIgnoreCase("22")) {
				// Interfaccia al Controller che effettua il controllo
				ic = SICOLookupRemote.getComuneRemote();
				if (!ic.ExIsComuneSedeUNEP(aem.getCodSede()))
					throw new F3BException(F3BException.USER_MESSAGE, descrSede + " non è comune sede UNEP");
			}
	}

	private String getCodiceUfficioDest(String filtro, String descrSede, String code) throws Exception {

		String codiceUfficio = "";
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				"ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA.getCodiceUfficioDest: inizio con "
						+ "descrSede = " + descrSede + " e code = " + code);
		if (filtro.equalsIgnoreCase("C"))
			codiceUfficio = getCodComuneByDescrFlagVal(descrSede.toUpperCase()).getCodComune();
		else if (filtro.equalsIgnoreCase("UDS"))
			codiceUfficio = getCodUfficioByCodTipoUfficioDescrComune("UDS", descrSede.toUpperCase());
		else if (filtro.equalsIgnoreCase("TDS"))
			codiceUfficio = getCodUfficioByCodTipoUfficioDescrComune("TDS", descrSede.toUpperCase());
		else if (filtro.equalsIgnoreCase("U"))
			codiceUfficio = getCodUfficioByCodTipoUfficioDescrComune("PM", descrSede.toUpperCase());
		else if (filtro.equalsIgnoreCase("PGCAP"))
			codiceUfficio = getCodUfficioByCodTipoUfficioDescrComune("PGCAP", descrSede.toUpperCase());
		else if (filtro.equalsIgnoreCase("CSSA")) {
			ICSSA icssa = SICOLookupRemote.getCSSARemote();
			codiceUfficio = (icssa.getCSSAByDescrComune(descrSede.toUpperCase())).getIdCSSA().toString();
		} else
			codiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(code, descrSede.toUpperCase());
		return codiceUfficio;
	}

	// Lettura delle Notifiche
	private Vector<NotificaModel> leggiNotifiche(EventoModel em) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".leggiNotifiche: inizio");

		if (!isRequestParameterNullObj("lCheck")) {
			check = getRequestStringParameters("lCheck");
		}

		String[] id_Notifica = null;
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ID_NOTIFICA)) {
			id_Notifica = getRequestStringParameters(ICostantiNotifica.CAMPO_ID_NOTIFICA);
		}
		String[] sedi = null;
		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_SEDE)) {
			sedi = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		}

		Vector<NotificaModel> notifiche = new Vector<>();
		boolean cancella = false;
		// Cerca le notifiche ed aggiunge i valori nuovi
		if (id_Notifica != null && id_Notifica.length > 0) {
			for (int z = 0; z < id_Notifica.length; z++) {
				cancella = false;
				for (int i = 0; i < check.length; i++) {
					if (id_Notifica[z].equals(check[i].toString())) {
						cancella = true;
					}
				}
				if (!cancella) {
					INotifica in = SIEPLookupRemote.getNotificaRemote();
					NotificaModel notifica = in.ExRicercaNotificaByKey((new BigDecimal(id_Notifica[z])));
					if (notifica.getUfficio() != null) {
						if (notifica.getUfficio().getCodTipoUfficio().equals("PGCAP")) {
							notifica.setUffCodUfficio(
									getCodUfficioByCodTipoUfficioDescrComune("PGCAP", sedi[z].toUpperCase()));
						} else if (notifica.getUfficio().getCodTipoUfficio().equals("UDS")) {
							notifica.setUffCodUfficio(
									getCodUfficioByCodTipoUfficioDescrComune("UDS", sedi[z].toUpperCase()));
						} else if (notifica.getUfficio().getCodTipoUfficio().equals("TDS")) {
							notifica.setUffCodUfficio(
									getCodUfficioByCodTipoUfficioDescrComune("TDS", sedi[z].toUpperCase()));
						} else if (notifica.getUfficio().getCodTipoUfficio().equals("PM")) {
							notifica.setUffCodUfficio(
									getCodUfficioByCodTipoUfficioDescrComune("PM", sedi[z].toUpperCase()));
						} else if (notifica.getUfficio().getCodTipoUfficio().equals("UEPE")) {
							notifica.setUffCodUfficio(
									getCodUfficioByCodTipoUfficioDescrComune("UEPE", sedi[z].toUpperCase()));
						} else if (notifica.getUfficio().getCodTipoUfficio().equals("UEPESS")) {
							notifica.setUffCodUfficio(getCodUfficioByCodTipoUfficioDescrComune("UEPESS",
									sedi[z].toUpperCase()));
						}
					} else if (notifica.getAutoritaEsterna() != null) { // Autorita' Esterna
						// Controlla e preleva il codice comune di sede.
						String codSede = getCodComuneByDescr(sedi[z].toUpperCase()).getCodComune();
						// Crea Model Autorità Esterna
						AutoritaEsternaModel aem = new AutoritaEsternaModel(notifica.getAutoritaEsterna());
						aem.setCodSede(codSede);

						// Controllo sede UNEP
						controlloAutorita(aem, sedi[z]);

						// Aggiunge il model Autorità Esterna alla Notifica
						notifica.setAutoritaEsterna(aem);
						if (notifica.getAutoritaEsterna().getCodTipoAutorita() != null) {
							notifica.setNote(
									getRequestStringParameter("note_" + notifica.getIdNotifica().toString()));
						}
					} else if (notifica.getIstitutoDetenzione() != null) { // Istituto Detenzione
						String istitutoDetenzione = null;
						if (!this.isRequestParameterNullObj(
								ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
							istitutoDetenzione = getRequestStringParameter(
									ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
							notifica.setIstDetIdIstitutoDetenzione(istitutoDetenzione);
						}
					} else if (notifica.getCssIdCssa() != null) { // CSSA
						if (notifica.getCSSA() != null) {
							ICSSA icssa = SICOLookupRemote.getCSSARemote();
							notifica.setCssIdCssa(
									icssa.getCSSAByDescrComune(sedi[z].toUpperCase()).getIdCSSA());
						}
					}
					notifica.setCodiceOperatoreAggiornamento(this.getCodUtenteConnesso());
					notifica.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
					notifica.setDataAggiornamento(DateUtils.getSysDate());
					Date dataTrasmissione = null;
					if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE)
							&& !isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE)
							&& !isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE))
						dataTrasmissione = getRequestDateParameter(
								ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE,
								ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE,
								ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE);
					else
						dataTrasmissione = em.getDataTrasmissioneAtti();
					notifica.setDataInvio(dataTrasmissione);
					notifiche.add(notifica);
				}
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".leggiNotifiche: fine");
		return notifiche;
	}

}