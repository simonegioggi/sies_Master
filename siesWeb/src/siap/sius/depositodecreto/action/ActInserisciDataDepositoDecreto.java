package siap.sius.depositodecreto.action;

/**
 * <p>Title: ActInserisciDataDepositoDecreto</p>
 * <p>Description: Classe Action per l'inserimento della data di Deposito Decreto</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

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
import siap.sige.curatore.action.ICostantiCuratore;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.sius.misurasicurezza.util.InserisciPeriodoAltraMisuraModificaEMS;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sius.sanzionesostitutiva.util.InserisciPeriodoAltraSanzioneModificaESS;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.udienza.action.ICostantiUdienza;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ActInserisciDataDepositoDecreto extends ActionSius implements ICostantiDepositoDecreto {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// Variabili di classe
	Date mDataTrasmissione = null;
	DepositoDecretoModel mDecreto = null;
	public FascicoloGPModel mFasGP = null;
	// Date mDataDeposito = null;

	ScadenzarioSiusModel lScadenzarioSiusModPrincipal = null; // Model Scadenzario
	ScadenzarioSiusModel lScadenzarioSiusModSecond = null;
	// Flag che individua la presenza di lock sul deposito
	public boolean isLocked = false;

	/**
	 * Azione di Inserimento della data di Deposito Decreto
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {
		String lPage = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("--------->ActInserisciDataDepositoDecreto.processRequest: inizio");
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
				ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE, ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE);

		if (this.IsFascicoloSiusModificabile() == false)
			throw new SIUSException(SIUSException.USER_MESSAGE, ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

		// Recupero il Fascicolo dalla sessione.
		mFasGP = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		// Recupero ID Soggetto
		if (mFasGP != null && mFasGP.getFascicoloSiusModel() != null)
			lIdSoggetto = mFasGP.getFascicoloSiusModel().getSogIdSoggetto();

		lIdEvento = aggiornaProvvedimento();

		// Aggiornamento dell'Evento e Inserimento della Notifica per ciascun destinatario.
		// Preparo il model EventoNotifica.
		EventoNotificaModel lEveNot = new EventoNotificaModel();

		// Evento. Leggo l'evento collegato al decreto.
		EventoModel lEveMod = new EventoModel();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// BigDecimal lIdEvento = mDecreto.getIdEventoGenerato();
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
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Tipo ---------------> " + lTipo);
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Tipo ----------------> null");

		if (lTipo == null || lTipo.toString().equals("DepTrasm")) {
			// Ciclo di caricamento delle notifiche.
			leggiNuoviDestinatari(lNotifiche, lIdEvento);
			leggiNotificheAvvocatiAltroDestinatario(lNotifiche, lIdEvento);
			leggiNotificaAlSoggetto(lNotifiche, lIdEvento, lIdSoggetto);

			// Inserisce le notifiche nell'eventoModel, prelevando
			// un array di oggetti dal vettore.
			lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
		}

		// Aggiornamento dei dati sul DB
		lPage = inserisciDati(lEveNot);

		return lPage;
	}

	private void leggiNuoviDestinatari(Vector aDestinatari, BigDecimal aIdEvento) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(
				"-----------------------> ActInserisciDataDepositoDecreto.leggiNuoviDestinatari -> inizio");

		// Elenco delle Notifiche a Nuovi Destinatari
		String[] lCodNotifiche = new String[0];
		String[] lSedi = null;
		String[] lNote = null;

		if (!isRequestParameterNullObj("cod_destinatari")) {
			lCodNotifiche = getRequestStringParameters("cod_destinatari");
			lSedi = getRequestStringParameters("sede_destinatari");
			lNote = getRequestStringParameters("nota_destinatari");

			// Lettura dei campi valorizzati
			for (int i = 0; i < lCodNotifiche.length; i++) {
				/*
				 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				 * siesLogger.debug("Destinatario[" + (i+1) + "]"); siesLogger.debug("+++ Codice: " +
				 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				 * lCodNotifiche[i]); siesLogger.debug("+++ Sedi: " + lSedi[i]);
				 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				 * siesLogger.debug("+++ Note: " + lNote[i]);
				 */
				// Se è valorizzata la sede del Destinatario si inserisce una Notifica
				if (lSedi[i] != null && lSedi[i].trim().length() > 0) {
					NotificaModel lNotifica = new NotificaModel();

					lNotifica.setDataInvio(mDataTrasmissione);
					lNotifica.setNote(lNote[i]);
					lNotifica.setCodOperatoreInserimento(getCodUtenteConnesso());
					lNotifica.setDataInserimento(DateUtils.getSysDate());
					lNotifica.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lNotifica.setCodEsito("-");
					lNotifica = inserisciSedeDestinatario(lNotifica, lCodNotifiche[i], lSedi[i]);
					lNotifica.setEveIdEvento(aIdEvento);
					aDestinatari.add(lNotifica);
				}
			}
		}

		return;

	}

	private NotificaModel inserisciSedeDestinatario(NotificaModel aNotificaDest, String aCode, String aDescrSede)
			throws Exception {
		// Destinatario Interno/Esterno.
		// 05/04/2007 Ampliato l'elenco dei Destinatari Deposito alle altre autorità giudiziarie.
		// Poichè il dominio DESTINATARI_DEPOSITO deve restare quelle originario per gestire l'input, al caricamento del
		String lCodiceAlternativo = DecodificheUtils.getCodAltebyCode(DecodificheManager.getInstance()
				.getDestinatarioDeposito(), aCode);
		String lFiltro = DecodificheUtils.getFiltrobyCode(DecodificheManager.getInstance().getDestinatarioDeposito(),
				aCode);
		// MEV10-s3: modificata condizione per gestire codici nulli dal DB!!!
//		if (lCodiceAlternativo == "") {
		if (!Utils.isPresent(lCodiceAlternativo)) {
			lCodiceAlternativo = DecodificheUtils.getCodAltebyCode(DecodificheManager.getInstance()
					.getTipoUfficioCumuloRifSiep(), aCode);
			lFiltro = DecodificheUtils.getFiltrobyCode(DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep(),
					aCode);
		}
		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug(" -----------------------------------------------> inserisciSedeDestinatario: inizio"
		 * ); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * ); siesLogger.debug(" --------------------> Codice Alternativo -> " + lCodiceAlternativo);
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug(" --------------------> Filtro -> " + lFiltro);
		 */
		aNotificaDest.setCodTipoNotifica("C");

		// 23/03/2007 Per le altre autorità Giudiziarie (Uffici interni) il Codice non è valorizzato
		// if (lCodiceAlternativo.equals("-"))
		if (lCodiceAlternativo.equals("-") || lCodiceAlternativo.equals("")) {
			String lCodUfficioDestinatario = getCodiceUfficioDest(lFiltro, aDescrSede, aCode);
			// aNotificaDest.setCodTipoNotifica("C"); // 20080111
			// Se interno, valorizzo il Codice CSSA o il CodiceUfficioDestinazione.
			if (lFiltro.equalsIgnoreCase("CSSA"))
				aNotificaDest.setCssIdCssa(new BigDecimal(lCodUfficioDestinatario));
			else
				aNotificaDest.setUffCodUfficio(lCodUfficioDestinatario);
		} else {
			// aNotificaDest.setCodTipoNotifica("N"); // 20080111

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

		}

		return aNotificaDest;
	}

	private String getCodiceUfficioDest(String aFiltro, String aDescrSede, String aCode) throws Exception {
		String lCodiceUfficio = "";
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("--------------> getCodiceUfficioDest Cod Sede -> " + aDescrSede);
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
			lCodiceUfficio = (lCtrlCSSA.getCSSAByDescrComune(aDescrSede.toUpperCase())).getIdCSSA().toString();
		}
		// 23/03/2007 Aggiunta Ulteriori Destinatari
		else
			lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(aCode, aDescrSede.toUpperCase());
		return lCodiceUfficio;
	}

	private void leggiNotificaAlSoggetto(Vector aNotifiche, BigDecimal aIdEvento, BigDecimal aISoggetto)
			throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
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
			lIstitutoDetenzione = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		} else {
			if (!isRequestParameterNullObj(ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE)) {
				lSedeSog = getRequestStringParameter(ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE);
				lDestinatarioSog = getRequestStringParameter(ICostantiUdienza.CAMPO_COD_IST_DETENZIONE);
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
			// Modifica: Precedentemente Cod Tipo notifica veniva valorizzato ad "R". Luigi 9-10-2007
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
		return;
	}

	private void leggiNotificheAvvocatiAltroDestinatario(Vector aNotifiche, BigDecimal aIdEvento) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("leggiNotificheAvvocatiAltroDestinatario: inizio");

		String lSedi[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		String lDestinatari[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
		String lNote[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_NOTE);
		String lAvvocato[] = getRequestStringParameters(ICostantiUdienza.CAMPO_COD_AVVOCATO);
		String lTipoNotifica[] = getRequestStringParameters(ICostantiRichiestaAtti.CODTIPONOTIFICA);
		//String lViaFax[] = getRequestStringParameter(ICostantiRichiestaAtti.CAMPO_NOTIFICHE_VIA_FAX).split(",");
		String lViaFax[] = null;
		
		// Avvocati & Altro Destinatario
		int lSize = lDestinatari.length;
		for (int x = 0; x < lSize; x++) {
			// MEV 10 - Eliminato controllo sulla sede, poichè per la notifica al 
			// difensore selezionando la dicitura "Notifica ai sensi dell'art. 148 
			// comma 2 bis c.p.p." (in questo caso si tratta di notifica effettuata 
			// via fax) non viene indicata alcuna sede
			//if (!lDestinatari[x].trim().equals("-") && !lSedi[x].trim().equals("")) {
			if (!lDestinatari[x].trim().equals("-")) {
				/*
				 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				 * siesLogger.debug("Destinatario[" + (x+1) + "]");
				 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				 * siesLogger.debug("+++ Cod. Destinatario: " + lDestinatari[x]);
				 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				 * siesLogger.debug("+++ Sede: " + lSedi[x]); siesLogger.debug("+++ Note: " + lNote[x]);
				 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				 * siesLogger.debug("+++ Tipo di Notifica " + lTipoNotifica[x]);
				 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				 * siesLogger.debug("+++ Cod. Avvocato: " + lAvvocato[x]);
				 */
				String lCodComuneSede = "-";
				if(!lSedi[x].trim().equals("")){
					lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();
				}
				
				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setDataInvio(mDataTrasmissione);
				lNotifica.setNote(lNote[x]);
				lNotifica.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				lNotifica.setCodTipoNotifica(lTipoNotifica[x]);
				// MEV10-s3: aggiunto controllo di prevenzione
				if (lViaFax != null && lViaFax.length > 0) {
					int l = lViaFax.length;
					if (x < l && lViaFax[x] != "")
						lNotifica.setFlagNotificaViaFax(new BigDecimal(lViaFax[x]));
				}
				if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 1) {
					lNotifica.setCodTipoNotifica("N");
					BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
					lNotifica.setAvvIdAvvocatoFascicoloSius(lAvvid);
					// 23/05/2011 Eventuale Curatore
				} else if (lAvvocato[x] != null && lAvvocato[x].trim().compareTo("C") == 0) {
					lNotifica.setCodTipoNotifica("N");
					BigDecimal lCurid = new BigDecimal(getRequestStringParameter(ICostantiCuratore.CAMPO_ID_CURATORE));
					lNotifica.setCurIdCuratore(lCurid);
				}
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatari[x]);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(getCodUtenteConnesso());
				lAutorita.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lAutorita.setDataInserimento(DateUtils.getSysDate());

				// controllo sede UNEP
				controlloAutorita(lAutorita, lSedi[x]);

				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.
				lNotifica.setEveIdEvento(aIdEvento);
				aNotifiche.add(lNotifica);
			}
		}

		return;
	}

	/**
	 * Aggiornamento del Deposito Decreto
	 * 
	 * @throws Exception
	 */
	public BigDecimal aggiornaProvvedimento() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("aggiornaProvvedimento: inizio");

		if (isSessionAttributeNullObj("lDepositoDecreto"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati Decreto non in sessione!");

		// Lettura DEposito Decreto dalla sessione
		mDecreto = (DepositoDecretoModel) getSessionAttribute("lDepositoDecreto");

		// Se il Decreto non è già depositato bisogna lockare per evitare che a 2 Decreti venga attribuito lo stesso
		// progressivo.
		if (mDecreto.getNumS72() == null) {
			// Lock
			LockModel lck = LockController.lockIfNotLocked(getServletContext(), "DEPOSITO_DECRETO",
					getCodUfficioUtenteConnesso(), getCodUtenteConnesso(), getSession().getId());
			if (lck != null) {
				setRequestAttribute(
						IWebConstants.MESSAGE_TEXT,
						"Un altro utente dello stesso ufficio (" + lck.getIdEntity() + ") sta effettuando un"
								+ lck.getEntity() + " ! <BR>Riprovare subito!");
				isLocked = true;
			}
		}

		// Effettuo l'inserimento data deposito in DepositoDecretoModel; carico i dati da aggiornare.
		mDecreto.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		mDecreto.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		mDecreto.setDataAggiornamento(DateUtils.getSysDate());
		mDecreto.setDataDeposito(getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO, CAMPO_MESE_DATA_DEPOSITO,
				CAMPO_GIORNO_DATA_DEPOSITO));
		// AnnoS72 e Num_S72 vengono valorizzati nel Controller
		// mDecreto.setAnnoS72(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));
		// Il campo Num_S72 viene valorizzato (nel controller) con l'ultimo valore presente + 1
		return mDecreto.getIdEventoGenerato();
	}

	/**
	 * La funzione effettua l'aggionamento dei dati nel DB ed indirizza alla pagina di dettaglio
	 * 
	 * @param aEveNot
	 * @return
	 * @throws Exception
	 */
	public String inserisciDati(EventoNotificaModel aEveNot) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(super.getClass().getName() + ".inserisciDati: inizio");

		String lPage = null;
		String[] lCheck = null;

		if (isLocked) {
			lPage = IWebConstants.PG_MESSAGE;
		} else {
			// #### Gestione SCADENZARIO Sanzioni Sostitutive
			// --- Sospensione Esecuzione Sanzione Sostitutiva
			if (mDecreto.getCodTipoDecreto().compareTo(SOSPENSIONE_ESECUZIONE_SS) == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug(
						super.getClass().getName() + ".inserisciDati: Inizio Carica Scadenzario - CodiceTipoDecreto: "
								+ mDecreto.getCodTipoDecreto());

				// Parametri da passare alla action di Util
				// ---- ID delle Decreto e dell'Ordinanza
				BigDecimal lIdEveDecOrd = mDecreto.getIdEventoGenerato();
				// ---- Codice Tipo Scadenzario Principale
				String lCodTipoScadenzarioPrincipal = "80";
				// ---- Codice Tipo Scadenzario Secondario
				String lCodTipoScadenzarioSecond = "81";

				// Richiama la funzione di "util"
				InserisciPeriodoAltraSanzioneModificaESS lPASmESS = new InserisciPeriodoAltraSanzioneModificaESS();
				// passa alla funzione di "util" i dati della Request e della Session
				lPASmESS.setReqSes(this.getRequest(), this.getSession());

				// esegue la funzione per popolare i model
				lScadenzarioSiusModSecond = lPASmESS.caricaScadenzarioSiusPerAltSanzModESS(mFasGP, lIdEveDecOrd,
						lCodTipoScadenzarioPrincipal, lCodTipoScadenzarioSecond);
				// Carica model
				lScadenzarioSiusModPrincipal = lPASmESS.getScadenzarioPrincipal();

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				// siesLogger.debug(
				// super.getClass().getName()+".inserisciDati: Fine Carica Scadenzario - CodiceTipoDecreto: "+
				// mDecreto.getCodTipoDecreto());
			}
			// #### FINE -- Gestione SCADENZARIO Sanzioni Sostitutive

			// #### Gestione SCADENZARIO Misure Sicurezza
			// --- Sospensione Esecuzione Misura Sicurezza
			if (mDecreto.getCodTipoDecreto().compareTo(SOSPENSIONE_ESECUZIONE_MS) == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug(
						super.getClass().getName() + ".inserisciDati: Inizio Carica Scadenzario - CodiceTipoDecreto: "
								+ mDecreto.getCodTipoDecreto());

				// Parametri da passare alla action di Util
				// ---- ID delle Decreto e dell'Ordinanza
				BigDecimal lIdEveDecOrd = mDecreto.getIdEventoGenerato();
				// ---- Codice Tipo Scadenzario Principale (Termine Misura Sicurezza)
				String lCodTipoScadenzarioPrincipal = "83";
				// ---- Codice Tipo Scadenzario Secondario(Termine Differimento Misura Sicurezza
				String lCodTipoScadenzarioSecond = "84";

				// Richiama la funzione di "util"
				InserisciPeriodoAltraMisuraModificaEMS lPAMmEMS = new InserisciPeriodoAltraMisuraModificaEMS();
				// passa alla funzione di "util" i dati della Request e della Session
				lPAMmEMS.setReqSes(this.getRequest(), this.getSession());

				// esegue la funzione per popolare i model
				lScadenzarioSiusModSecond = lPAMmEMS.caricaScadenzarioSiusPerAltMisModEMS(mFasGP, lIdEveDecOrd,
						lCodTipoScadenzarioPrincipal, lCodTipoScadenzarioSecond);
				// Carica model
				lScadenzarioSiusModPrincipal = lPAMmEMS.getScadenzarioPrincipal();

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				// siesLogger.debug(
				// super.getClass().getName()+".inserisciDati: Fine Carica Scadenzario - CodiceTipoDecreto: "+
				// mDecreto.getCodTipoDecreto());
			}
			// #### FINE -- Gestione SCADENZARIO Misure Sicurezza

			// Lettura dei Check
			if (!this.isRequestParameterNullObj("lCheck"))
				lCheck = getRequestStringParameters("lCheck");

			// Il controller effettuerà tutte le operazioni sui dati.
			IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
			DocumentoAllegatoModel lDocAllMod = lCtrlDD.ExInserisciDataDepositoDecreto(mFasGP, mDecreto, aEveNot,
					lCheck, lScadenzarioSiusModPrincipal, lScadenzarioSiusModSecond);

			// restituisce la jsp di VIEW.
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sius.depositodecreto.action.ActLoadDettaglioDataDepositoDecreto&"
					+ CAMPO_ID_DOCUMENTO_ALLEGATO + "=" + lDocAllMod.getIdDocumentoAllegato();
		}
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		// siesLogger.debug( super.getClass().getName()+".inserisciDati: fine");

		return lPage;

	}

	// La funzione controlla nel caso l'autorità esterna sia UNEP che il comune indicato come sede sia una sede UNEP
	private void controlloAutorita(AutoritaEsternaModel aAutorEst, String aDescrSede) throws F3BException {
		IComune lCtrl = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(" ------------------> controlloAutorita: inizio");
		if (aAutorEst != null && aAutorEst.getCodTipoAutorita() != null && aDescrSede != null)
			// Controllo Comune sede UNEP
			if (aAutorEst.getCodTipoAutorita().equalsIgnoreCase(COD_UNEP)) {
				// Interfaccia al Controller che effettua il controllo
				lCtrl = SICOLookupRemote.getComuneRemote();
				if (!lCtrl.ExIsComuneSedeUNEP(aAutorEst.getCodSede()))
					throw new F3BException(F3BException.USER_MESSAGE, aDescrSede + " non è comune sede UNEP");
			}

	}

}