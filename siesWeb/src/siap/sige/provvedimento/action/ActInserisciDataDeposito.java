package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.SIGEException;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
//import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;

/**
 * <p>
 * Title: ActInserisciDataDeposito
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento della data di Deposito Provvedimento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciDataDeposito extends ActionSige implements ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Variabili di classe
	Date mDataTrasmissione = null;
	ProvvedimentoSigeEventoModel mProvvedimento = null;
	FascicoloSigeEstesoModel mFasEsteso = null;
	// Date mDataDeposito = null;

	// Flag che individua la presenza di lock sul Provvedimento
	public boolean isLocked = false;

	/**
	 * Azione di Inserimento della data di Deposito Provvedimento
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		String lPage = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciDataDeposito.processRequest: inizio");
		// Reset delflag di lock
		isLocked = false;

		BigDecimal lIdSoggetto = null;
		BigDecimal lIdEvento = null;

		String lTipo = null;
		if (!this.isRequestParameterNullObj("tipo")) {
			lTipo = getRequestStringParameter("tipo");
		}

		// mDataDeposito =
		// getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO,CAMPO_MESE_DATA_DEPOSITO,CAMPO_GIORNO_DATA_DEPOSITO);
		mDataTrasmissione = getRequestDateParameter(ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE);

		// if (this.IsFascicoloSigeModificabile() == false)
		// throw new SIGEException(SIGEException.USER_MESSAGE,ICostantiFascicoloSige.MSG_NON_MODIFICABILE);

		// Fascicolo Sige Esteso in sessione.
		mFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		// mFasEsteso = this.getFascicoloSigeEstesoInSessione();

		// Recupero ID Soggetto
		if (mFasEsteso != null && mFasEsteso.getFascicoloSige() != null)
			lIdSoggetto = mFasEsteso.getFascicoloSige().getSogIdSoggetto();

		lIdEvento = aggiornaProvvedimento();

		// Aggiornamento dell'Evento e Inserimento della Notifica per ciascun destinatario.
		// Preparo il model EventoNotifica.
		EventoNotificaModel lEveNot = new EventoNotificaModel();

		// Evento. Leggo l'evento collegato al provvedimento.
		EventoModel lEveMod = new EventoModel();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		// Aggiorno l'Evento.
		lEveMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lEveMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.setDataTrasmissioneAtti(mDataTrasmissione);

		// Imposto l'Evento nel'EventoNotificaModel.
		lEveNot.setEvento(lEveMod);

		// Preparo le notifiche.
		Vector lNotifiche = new Vector();
		if (lTipo != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Tipo -> " + lTipo);
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Tipo -> null");

		if (lTipo == null || lTipo.toString().equals("DepTrasm")) {
			// Ciclo di caricamento delle notifiche.
			leggiNuoviDestinatari(lNotifiche, lIdEvento);
			leggiNotificheAvvocatiAltroDestinatario(lNotifiche, lIdEvento);
			leggiNotificaAlSoggetto(lNotifiche, lIdEvento, lIdSoggetto);

			// Inserisce le notifiche nell'eventoModel, prelevando un array di oggetti dal vettore.
			lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
		}

		// Aggiornamento dei dati sul DB
		lPage = inserisciDati(lEveNot);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciDataDeposito.processRequest:pagina -> " + lPage + "fine");

		return lPage;
	}

	private void leggiNuoviDestinatari(Vector aDestinatari, BigDecimal aIdEvento) throws Exception {
		// Elenco delle Notifiche a Nuovi Destinatari
		int lNumSentenze = getRequestIntParameter("num_sentenze");
		int numCheck = lNumSentenze * 2;
		String[] lCodNotifiche = new String[0];
		String[] lSedi = null;
		String[] lNote = null;
		String[] lDati = null;
		String[] lCheck = new String[numCheck];
		for (int k = 0; k < (numCheck); k++) {
			if (isRequestChecked("lCheck_" + k))
				lCheck[k] = "1";
			else
				lCheck[k] = "0";
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("leggiNuoviDestinatari: inizio");

		if (!isRequestParameterNullObj("cod_destinatari")) {
			lCodNotifiche = getRequestStringParameters("cod_destinatari");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("N.ro campi destinatari -> " + lCodNotifiche.length);
			lSedi = getRequestStringParameters("sede_destinatari");
			lNote = getRequestStringParameters("nota_destinatari");
			lDati = getRequestStringParameters("dato_destinatari");

			// Lettura dei campi valorizzati.
			int j = 0; // indice di riferimento per i check-box ( autorità giudicanti e uffici recupero
						// credito).
			String checkCorrente = ""; // valore corrente check per le autorità giudicanti e uffici recupero
										// credito.
			for (int i = 0; i < lCodNotifiche.length; i++) {
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.info("Destinatario[" + (i+1) + "]");
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.info("+++ Codice: " + lCodNotifiche[i]);
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.info("+++ Sedi: " + lSedi[i]);
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.info("+++ Note: " + lNote[i]);
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.info("+++ Dati: " + lDati[i]);

				// Preparazione valore del "checkCorrente" riferito ai checkbox.
				checkCorrente = "";
				if (lDati[i].indexOf("Check") == 0) {
					checkCorrente = lCheck[j];
					j++;
				}

				// Se è valorizzata la sede del Destinatario (o è selezionato il Check-box dell'autorità
				// giudicante/ufficio recupero credito )
				// si inserisce una Notifica.
				if ((lSedi[i] != null && lSedi[i].trim().length() > 0 && checkCorrente == "")
						|| (checkCorrente == "1")) {
					NotificaModel lNotifica = new NotificaModel();

					if (lDati[i].indexOf("Check") != 0 && lDati[i].length() > 1)
						lNotifica.setMessage(lDati[i]);

					lNotifica.setDataInvio(mDataTrasmissione);
					lNotifica.setNote(lNote[i]);
					lNotifica.setCodOperatoreInserimento(getCodUtenteConnesso());
					lNotifica.setDataInserimento(DateUtils.getSysDate());
					lNotifica.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lNotifica.setCodEsito("-");
					lNotifica = inserisciSedeDestinatario(lNotifica, lCodNotifiche[i], lSedi[i], lNote[i]);
					lNotifica.setEveIdEvento(aIdEvento);
					aDestinatari.add(lNotifica);
				}
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("leggiNuoviDestinatari: fine");

		return;

	}

	private NotificaModel inserisciSedeDestinatario(NotificaModel aNotificaDest, String aCode,
			String aDescrSede, String aDescrNote) throws Exception {
		// Destinatario Interno/Esterno (dominio DESTINATARI_DEPOSITO).
		String lCodiceAlternativo = DecodificheUtils
				.getCodAltebyCode(DecodificheManager.getInstance().getDestinatarioDeposito(), aCode);
		String lFiltro = DecodificheUtils
				.getFiltrobyCode(DecodificheManager.getInstance().getDestinatarioDeposito(), aCode);
		if (lCodiceAlternativo == "") {
			lCodiceAlternativo = DecodificheUtils
					.getCodAltebyCode(DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep(), aCode);
			lFiltro = DecodificheUtils
					.getFiltrobyCode(DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep(), aCode);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("inserisciSedeDestinatario: inizio");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Codice Alternativo -> " + lCodiceAlternativo);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Filtro -> " + lFiltro);

		// 25/06/2010 Distinzione tipo notifica.
		aNotificaDest.setCodTipoNotifica("C");
		if (aDescrNote.contains("secuzione"))
			aNotificaDest.setCodTipoNotifica("E");

		// Per le altre autorità Giudiziarie (Uffici interni) il Codice non è valorizzato
		if (lCodiceAlternativo.equals("-") || lCodiceAlternativo.equals("")) {
			String lCodUfficioDestinatario = getCodiceUfficioDest(lFiltro, aDescrSede, aCode);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Codice Ufficio Destinatario -> " + lCodUfficioDestinatario);

			// Se interno, valorizzo il Codice CSSA o il CodiceUfficioDestinazione.
			if (lFiltro.equalsIgnoreCase("CSSA"))
				aNotificaDest.setCssIdCssa(new BigDecimal(lCodUfficioDestinatario));
			else
				aNotificaDest.setUffCodUfficio(lCodUfficioDestinatario);
		} else {
			// Valorizzo l'autorità esterna.
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lCodiceAlternativo);
			lAut.setCodSede(getCodComuneByDescrFlagVal(aDescrSede.toUpperCase()).getCodComune());
			lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lAut.setDataInserimento(DateUtils.getSysDate());
			// Controllo sede UNEP
			controlloAutorita(lAut, aDescrSede);
			// Setto l'Autorita Esterna per la Notifica corrente.
			aNotificaDest.setAutoritaEsterna(lAut);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Notifica ad Aut. Esterna. Cod Sede -> " + lAut.getCodSede());
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("inserisciSedeDestinatario: fine");

		return aNotificaDest;
	}

	private String getCodiceUfficioDest(String aFiltro, String aDescrSede, String aCode) throws Exception {
		String lCodiceUfficio = "";

		if (aFiltro.equalsIgnoreCase("C"))
			lCodiceUfficio = getCodComuneByDescrFlagVal(aDescrSede.toUpperCase()).getCodComune();
		else if (aFiltro.equalsIgnoreCase("UDS"))
			lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune("UDS", aDescrSede.toUpperCase());
		else if (aFiltro.equalsIgnoreCase("TDS"))
			lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune("TDS", aDescrSede.toUpperCase());
		else if (aFiltro.equalsIgnoreCase("U"))
			lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune("PM", aDescrSede.toUpperCase());
		else if (aFiltro.equalsIgnoreCase("PGCAP"))
			lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune("PGCAP", aDescrSede.toUpperCase());
		else if (aFiltro.equalsIgnoreCase("CSSA")) {
			ICSSA lCtrlCSSA = SICOLookupRemote.getCSSARemote();
			lCodiceUfficio = (lCtrlCSSA.getCSSAByDescrComune(aDescrSede.toUpperCase())).getIdCSSA()
					.toString();
		}
		// 23/03/2007 Aggiunta Ulteriori Destinatari
		else
			lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(aCode, aDescrSede.toUpperCase());
		return lCodiceUfficio;
	}

	private void leggiNotificaAlSoggetto(Vector aNotifiche, BigDecimal aIdEvento, BigDecimal aISoggetto)
			throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("leggiNotificaAlSoggetto: inizio");

		String lDestinatarioSog = null;
		String lIstitutoDetenzione = null;
		String lSedeSog = null;
		String lNota = null;

		if (!isRequestParameterNullObj("nota_soggetto"))
			lNota = getRequestStringParameter("nota_soggetto");

		// Letture
		// Preleva l'id dell'istituto detenzione
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lIstitutoDetenzione = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		} else {
			if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE)) {
				lSedeSog = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE);
				lDestinatarioSog = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_IST_DETENZIONE);
			} else {
				// MEV 15 - Revisione Sige
				// selezionando il campo "Domiciliato presso il difensore (ex art. 161 cpp)"
				// il domicilio del Soggetto (Autorità) diventa lo stesso del difensore
				if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO)) {
					lSedeSog = getRequestStringParameter(ICostantiRichiestaAtti.CAMPO_SEDE);
					lDestinatarioSog = getRequestStringParameter(
							ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
				}
			}
		}

		// Soggetto con autorita' esterna
		if (lDestinatarioSog != null && lSedeSog != null) {
			if (!lDestinatarioSog.equals("-") && !lSedeSog.equals("")) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSedeSog).getCodComune();

				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setCodTipoNotifica("N");
				lNotifica.setDataInvio(mDataTrasmissione);
				lNotifica.setNote(lNota);
				lNotifica.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				lNotifica.setSogIdSoggetto(aISoggetto);

				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatarioSog);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(getCodUtenteConnesso());
				lAutorita.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Controllo sede UNEP
				controlloAutorita(lAutorita, lSedeSog);

				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.
				lNotifica.setEveIdEvento(aIdEvento);
				aNotifiche.add(lNotifica);
			}
		}

		// Soggetto con id
		if (lIstitutoDetenzione != null) {
			NotificaModel lNotifica = new NotificaModel();
			lNotifica.setCodTipoNotifica("N");
			lNotifica.setDataInvio(mDataTrasmissione);
			lNotifica.setNote(lNota);
			lNotifica.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotifica.setDataInserimento(DateUtils.getSysDate());
			lNotifica.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotifica.setCodEsito("-");
			lNotifica.setUffCodUfficio("-");
			lNotifica.setIstDetIdIstitutoDetenzione(lIstitutoDetenzione);
			lNotifica.setSogIdSoggetto(aISoggetto);
			// Aggiunge il model delle notifiche al vettore.
			lNotifica.setEveIdEvento(aIdEvento);
			aNotifiche.add(lNotifica);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("leggiNotificaAlSoggetto: fine");
		return;
	}

	private void leggiNotificheAvvocatiAltroDestinatario(Vector<NotificaModel> lNotifiche,
			BigDecimal aIdEvento) throws Exception {
		String[] destinatarioflagSNT = getParameterValues("flagSNT");
		HashSet<Integer> hs = new HashSet<>();
		if (destinatarioflagSNT != null && destinatarioflagSNT.length > 0) {
			for (String flagId : destinatarioflagSNT) {
				hs.add(Integer.valueOf(flagId));
			}
		}

		/*
		 * ISSUE MAC : impostata la come data invio la data di trasmissione anzichè quella di emissione
		 * ordinanza Numero MAC : 20200107012 Autore : monica Data : 10/gen/2020 Branch : 11.2.5
		 */
		// MG Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
		// ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		// Mev 15: siccome la data emissione non è più obbligatoria, in alternativa per le notifiche se usa la
		// sysdate
		// Date lDataInvioNotifiche = (lDataEmissione != null) ? lDataEmissione : DateUtils.getSysDate();
		String lAvvocato[] = getRequestStringParameters(ICostantiUdienzaSige.CAMPO_COD_AVVOCATO);
		String lDestinatari[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
		String lSedi[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		int lSize = lDestinatari.length;
		for (int x = 0; x < lSize; x++) {
			if ((!lDestinatari[x].equals("-") && !lSedi[x].equals("")) || (hs.contains(x))) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();

				NotificaModel lNotifica = new NotificaModel();
				// MG lNotifica.setDataInvio(lDataInvioNotifiche);
				lNotifica.setDataInvio(mDataTrasmissione);
				lNotifica.setCodOperatoreInserimento(super.getCodUtenteConnesso());
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(super.getCodUfficioUtenteConnesso());
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				lNotifica.setCodTipoNotifica("N");

				if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 0) {
					BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
					lNotifica.setAvvIdAvvocatoFascicoloSige(lAvvid);
					AvvocatoSigeModel lAvvSige = new AvvocatoSigeModel();
					lAvvSige.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(lAvvid);
					lNotifica.setAvvSige(lAvvSige);
				}

				// verifica se non sia stata impostata la notifica telematica
				if (!hs.contains(x)) {
					// Crea Model Autorità Esterna
					AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
					lAutorita.setCodTipoAutorita(lDestinatari[x]);
					lAutorita.setCodSede(lCodComuneSede);
					lAutorita.setCodOperatoreInserimento(super.getCodUtenteConnesso());
					lAutorita.setCodUfficioInserimento(super.getCodUfficioUtenteConnesso());
					lAutorita.setDataInserimento(DateUtils.getSysDate());

					// Aggiunge il model Autorità Esterna alla Notifica
					lNotifica.setAutoritaEsterna(lAutorita);
				}
				lNotifica.setEveIdEvento(aIdEvento);
				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNotifica);
			}
		}
		// ***** FINE INTERVENTO 20200107012 *****//
	}

	/**
	 * Aggiornamento del Provvedimento
	 *
	 * @throws Exception
	 */
	private BigDecimal aggiornaProvvedimento() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aggiornaProvvedimento: inizio");

		if (isSessionAttributeNullObj("lProvvedimento"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Dati Provvedimento non in sessione!");

		// Lettura Provvedimento dalla sessione
		mProvvedimento = (ProvvedimentoSigeEventoModel) getSessionAttribute("lProvvedimento");

		// Se il Provvedimento non è già depositato bisogna lockare per evitare che a 2 Provvedimenti venga
		// attribuito lo stesso progressivo.
		if (mProvvedimento.getProvvedimento().getChiaveProgr() == null) {
			// Lock
			LockModel lck = LockController.lockIfNotLocked(getServletContext(), "PROVVEDIMENTO_SIGE",
					getCodUfficioUtenteConnesso(), getCodUtenteConnesso(), getSession().getId());
			if (lck != null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Un altro utente dello stesso ufficio (" + lck.getIdEntity() + ") sta effettuando un"
								+ lck.getEntity() + " ! <BR>Riprovare subito!");
				isLocked = true;
			}
		}

		// Si Effettua l'inserimento data deposito in ProvvedimentoModel; si caricano i dati da aggiornare.
		mProvvedimento.getProvvedimento().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		mProvvedimento.getProvvedimento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		mProvvedimento.getProvvedimento().setDataAggiornamento(DateUtils.getSysDate());
		mProvvedimento.getProvvedimento().setDataDeposito(getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO,
				CAMPO_MESE_DATA_DEPOSITO, CAMPO_GIORNO_DATA_DEPOSITO));
		mProvvedimento.getProvvedimento()
				.setChiaveAnno(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));
		// Il campo ChiaveProgr viene valorizzato (nel controller) con l'ultimo valore presente + 1
		// (Contestualmente al Tipo Provvedimento Decreto/Ordinanza).
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aggiornaProvvedimento: fine");
		return mProvvedimento.getProvvedimento().getIdEventoGenerato();
	}

	/**
	 * La funzione effettua l'aggionamento dei dati nel DB ed indirizza alla pagina di dettaglio
	 *
	 * @param aEveNot
	 * @return
	 * @throws Exception
	 */
	public String inserisciDati(EventoNotificaModel aEveNot) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".inserisciDati: inizio");

		String lPage = null;
		String[] lCheck = null;

		if (isLocked) {
			lPage = IWebConstants.PG_MESSAGE;
		} else {
			// Lettura dei Check
			if (!this.isRequestParameterNullObj("lCheck"))
				lCheck = getRequestStringParameters("lCheck");

			// Il controller effettuerà tutte le operazioni sui dati.
			IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
			DocumentoAllegatoModel lDocAllMod = null;
			if (!this.isDecretoFissazioneUdienza())
				lDocAllMod = lCtrlProv.ExInserisciDataDeposito(mFasEsteso.getFascicoloSige(), mProvvedimento,
						aEveNot, lCheck);
			else
				lDocAllMod = lCtrlProv.ExInserisciDataDepositoFissazioneUdienza(mFasEsteso.getFascicoloSige(),
						mProvvedimento, aEveNot, lCheck);

			// restituisce la jsp di VIEW.
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sige.provvedimento.action.ActLoadDettaglioDataDeposito&"
					+ CAMPO_ID_DOCUMENTO_ALLEGATO + "=" + lDocAllMod.getIdDocumentoAllegato();
		}
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

}