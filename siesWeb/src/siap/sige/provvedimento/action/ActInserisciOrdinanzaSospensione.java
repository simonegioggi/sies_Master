package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.SIGEException;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistrato.action.ICostantiMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.motivazioneprovvedimento.action.ICostantiMotivazioneProvvedimento;
import siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.provvedimento.util.ProvvedimentoSigeUtils;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.util.SIGELookupRemote;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOrdinanzaSospensione extends ActInserisciEmissioneOrdinanza
		implements ICostantiProvvedimentoSige, ICostantiMotivazioneProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	FascicoloSigeEstesoModel mFasEsteso = null;
	Date mDataEmissione = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciOrdinanzaSospensione: inizio");

		if (this.isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Fascicolo Sige non in sessione");

		// Fascicolo Sige Esteso in sessione.
		mFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		// Eventuale Codice Tipo Giudizio da assegnare al Fascicolo SIGE
		String lTipoGiudizio = null;
		BigDecimal lIdCollegio = null;

		if (isSessionAttributeNullObj("tenori"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti !");

		// Si prelevano i tenori Sige in sessione.
		Vector lTenoriEstesi = (Vector) getSessionAttribute("tenori");

		if (lTenoriEstesi.size() == 0)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti !");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Dimensione lista Tenori Estesi :" + lTenoriEstesi.size());

		// In sessione c'è una lista di TenoreEstesoModel
		TenoriSigeUtil lTenUtil = new TenoriSigeUtil();
		Vector lTenori = lTenUtil.listaTenoriDaListaTenoriEstesi(lTenoriEstesi);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Dimensione lista Tenori :" + lTenori.size());

		// Preleva data di emissione
		mDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data Emissione :" + mDataEmissione);

		// eventuale lettura Tipo Giudizio e Collegio
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO)) {
			lTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO).trim();
			String lCodMagAssegnatario = "";
			if (!isRequestParameterNullObj(ICostantiCollegio.CAMPO_ID_COLLEGIO)) {
				lIdCollegio = getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO);

				if (lTipoGiudizio != null && lTipoGiudizio.equalsIgnoreCase("C")) {
					// Si risale al codice del Magistrato Assegnatario
					if (mFasEsteso != null && mFasEsteso.getMagAssegnatario() != null)
						lCodMagAssegnatario = mFasEsteso.getMagAssegnatario().getMagCodMagistrato();

					if (lCodMagAssegnatario.length() > 0) {
						// Verifica appartenenza al Collegio del Magistrato Assegnatario
						ProvvedimentoSigeUtils lProSigeUtils = new ProvvedimentoSigeUtils();
						if (!lProSigeUtils.isMagistratoAssInCollegio(lIdCollegio, lCodMagAssegnatario))
							throw new SIGEException(SIGEException.USER_MESSAGE,
									"Il Magistrato Assegnatario deve far parte del collegio !");
					}
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Tipo Giudizio :" + lTipoGiudizio);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID Collegio :" + lIdCollegio);
		}

		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();

		// Impostazione del Provvedimento.
		ProvvedimentoSigeModel lProvModel = new ProvvedimentoSigeModel();
		lProvModel.setFasIdFascicoloSige(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		// l'anno va impostato al momento del deposito.
		// lProvModel.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lProvModel.setChiaveUfficio(getCodUfficioUtenteConnesso());
		lProvModel.setDataEmissione(mDataEmissione);
		lProvModel.setCodTipoProvvedimento(ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA);
		lProvModel.setCodTipoProvvedimentoSige(ICostantiProvvedimentoSige.COD_ORDINANZA_SOSPENSIONE);
		if (isRequestParameterNullObj(ICostantiProvvedimentoSige.CAMPO_PROVV_ID_PROVVEDIMENTO_SIGE))
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Impossibile trovare i dati dell'Ordinanza da Sospendere !");
		lProvModel.setProvvIdProvvedimentoSige(
				getRequestBigDecimalParameter(ICostantiProvvedimentoSige.CAMPO_PROVV_ID_PROVVEDIMENTO_SIGE));
		lProvModel.setDefinitorio("N");
		lProvModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che inserisce
		lProvModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																			// dell'operatore che inserisce
		lProvModel.setDataInserimento(DateUtils.getSysDate());
		lProvModel.setColIdCollegio(lIdCollegio);
		lProvModel.setNote(getRequestStringParameter(
				ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE_SOSPENSIONE));

		// Lettura Ufficio di Competenza Corte Suprema di Cassazione
		String lCodTipoUffCompCorteSuprema = getRequestStringParameter(
				ICostantiFascicoloSige.CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA);
		String lDescComuneSedeUffCompCorteSuprema = getRequestStringParameter(
				ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA);
		String lCodUffCompCorteSuprema = null;
		if (lCodTipoUffCompCorteSuprema != null && !lCodTipoUffCompCorteSuprema.equals("-")
				&& !lCodTipoUffCompCorteSuprema.equals("")) {
			lCodUffCompCorteSuprema = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUffCompCorteSuprema,
					lDescComuneSedeUffCompCorteSuprema);
			lProvModel.setCodUffCompCorteSuprema(lCodUffCompCorteSuprema);
		}
		// preleva i dati della udienza sige
		BigDecimal idUdienza = super.getRequestBigDecimalParameter(
				ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		lProvModel.setUdiIdUdienzaSige(idUdienza);

		// if(getRequestStringParameter( ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO ) != null &&
		// getRequestStringParameter( ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO ) != null &&
		// getRequestStringParameter( ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO ).trim().length() > 1 &&
		// getRequestStringParameter( ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO ).trim().length() > 1)
		// lProvModel.setCodUfficioDestinatario(getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(
		// ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO ), getRequestStringParameter(
		// ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO )));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Provvedimento SIGE Valorizzato :" + lProvModel);

		// Impostazione EventoModel
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento("01");
		lEvento.setCodTipoProvvedimento("03");
		lEvento.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEvento.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEvento.setDataEmissione(mDataEmissione);
		// lEvento.setFasSieIdFascicoloSiep(lFasEsteso.getFascicoloSiep().getIdFascicoloSiep() );
		lEvento.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEvento.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEvento.setDataInserimento(DateUtils.getSysDate());
		lEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEvento.setCodTipoUfficioDestinatario("-");
		lEvento.setCodLuogoDestinatario("-");
		lEvento.setCodMotivo("-");
		lEvento.setCodEsito("-");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Evento Valorizzato :" + lEvento);

		Vector lDestinatariNotifiche = creaListaDestinatariNotifiche(mFasEsteso, mDataEmissione);

		// Creazione dell'EventoNotificaModel

		EventoNotificaModel lEveNotMod = new EventoNotificaModel(lEvento);
		lEveNotMod.setNotifiche((NotificaModel[]) lDestinatariNotifiche.toArray(new NotificaModel[0]));
		/**
		 * ProvvedimentoSigeEventoModel lProvEveModel = new ProvvedimentoSigeEventoModel();
		 * lProvEveModel.setProvvedimento(lProvModel); lProvEveModel.setEventoNotifica(lEveNotMod);
		 **/

		MotivazioneProvvedimentoSigeModel[] lMotivazioni = letturaMotivazioni();

		// Chiamata al Controller per gli inserimenti.
		// lProvModel = lProvCtrl.ExInserisciEventoNotificaProv(lEveNotMod, lProvModel);

		ProvvedimentoSigeEventoModel lProvEveModel = new ProvvedimentoSigeEventoModel();
		lProvEveModel.setProvvedimento(lProvModel);
		lProvEveModel.setEventoNotifica(lEveNotMod);

		// Da inserire le notifiche (destinatari prima)
		lProvEveModel = lProvCtrl.ExInserisciProvvSospensione(lProvEveModel, lTenori, lTipoGiudizio,
				lMotivazioni);

		// INTERVENTO PER 11.2.1
		// controllo e gestione dell'eventuale aggiornamento del magistrato assegnatario
		MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();
		// MagistratoAssegnatarioModel llMagModRet = null;
		// vado in else quando l'udienza associata a tale fascicolo, è utilizzata anche da altri fascicoli
		// in questo caso il record dell'udienza sulla tabella udienza_sige NON E' MODIFICABILE
		// quindi ogni cambio del magistrato, o procuratore oppure del cancelliere deveno essere inserite
		// sulla tabella magistrato_assegnatario per lo specifico idFascicolo
		String codMagPrecedente = "";
		if (getFascicoloSigeEstesoInSessione().getMagAssegnatario() != null
				&& getFascicoloSigeEstesoInSessione().getMagAssegnatario().getMagCodMagistrato() != null) {
			codMagPrecedente = getFascicoloSigeEstesoInSessione().getMagAssegnatario().getMagCodMagistrato();
		}
		String codMagNuovo = "";
		if (this.getParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO) != null) {
			codMagNuovo = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		} else {
			codMagNuovo = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE);
		}
		// se sono diversi lo sostituisce, altrimento no
		if (!codMagNuovo.equals(codMagPrecedente) && !codMagNuovo.equals("")) {
			lMagistrato = prepareModificaAssegnatarioModel(
					getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
			MagistratoModel magMod = new MagistratoModel();
			magMod.setCodMagistrato(codMagPrecedente);
			lMagistrato.setMagistrato(magMod);
			IMagistratoAssegnatario lCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
			/* llMagModRet = */lCtrl.ExInserisciAggiornaMagistratoAssegnatario(lMagistrato);
		}

		// Si Rilegge il fascicolo SIGE Esteso e lo si inserisce in SESSIONE.
		IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
		FascicoloSigeEstesoModel lFasEsteso = lCtrlFas
				.ExRicercaEstesaFascicoloSigeByKey(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);

		setRequestAttribute("lProvvinserito", lProvModel);
		setRequestAttribute("data_emissione", mDataEmissione);

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");

		// Prepara la "pagina" di destinazione
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.provvedimento.action.ActDettaglioOrdinanzaSospensione");
		lRedirigi.setParameter(CAMPO_ID_PROVVEDIMENTO_SIGE,
				(lProvEveModel.getProvvedimento().getIdProvvedimentoSige()).toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciOrdinanzaSospensione: fine");
		return lRedirigi.toString();
	}

	// FUNZIONE per la GESTIONE DESTINATARI NOTIFICHE
	protected Vector creaListaDestinatariNotifiche(FascicoloSigeEstesoModel aFasEsteso, Date aDataEmissione)
			throws Exception {

		// Vector per le notifiche.
		Vector lNotifiche = new Vector();

		String lDestinatari[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
		String lSedi[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		String lAvvocato[] = getRequestStringParameters(ICostantiUdienzaSige.CAMPO_COD_AVVOCATO);
		String lSediSog = null;
		String lDestinatariSog = null;

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodiceUfficioPG = null;
		String lCodComune = getCodComuneUtenteConnesso();
		String lDescrComune = getUfficioUtenteConnesso().getDescrComune();
		String[] lNote = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_NOTE);

		String lTipoNotifica = getRequestStringParameter("TipoNotifica");

		// Preleva l'id dell'Istituto Detenzione
		String lIstitutoDetenzione = null;
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lIstitutoDetenzione = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		} else {
			lSediSog = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE);
			lDestinatariSog = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_IST_DETENZIONE);
		}

		// Avvocati & Altro Destinatario
		int lSize = lDestinatari.length;
		for (int x = 0; x < lSize; x++) {
			if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {
				String lCodComuneSede = getCodComuneByDescr(lSedi[x]).getCodComune();

				NotificaModel lNotifica = new NotificaModel();
				lNotifica.setCodTipoNotifica(lTipoNotifica);
				lNotifica.setDataInvio(aDataEmissione);
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 0) {
					lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
					BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
					lNotifica.setAvvIdAvvocatoFascicoloSige(lAvvid);
					AvvocatoSigeModel lAvvSige = new AvvocatoSigeModel();
					lAvvSige.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(lAvvid);
					lNotifica.setAvvSige(lAvvSige);
				}
				lNotifica.setNote(lNote[x + 1]);
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatari[x]);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
				lAutorita.setCodUfficioInserimento(lCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());

				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNotifica);
			}
		}

		// Soggetto con autorita' esterna
		if (!Utils.isNullObj(lDestinatariSog) && !Utils.isNullObj(lSediSog)) {
			if (!lDestinatariSog.equals("-") && !lSediSog.equals("")) {
				String lCodComuneSede = getCodComuneByDescr(lSediSog).getCodComune();

				NotificaModel lNotifica = new NotificaModel();

				lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
				lNotifica.setDataInvio(aDataEmissione);
				lNotifica.setNote(lNote[0]);
				lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(lCodiceUfficio);
				lNotifica.setCodEsito("-");
				lNotifica.setUffCodUfficio("-");
				if (mFasEsteso == null)
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Fascicolo Sige Esteso in sessione mFasEsteso null");
				lNotifica.setSogIdSoggetto(aFasEsteso.getFascicoloSige().getSogIdSoggetto());
				if (!lNote[0].equals("")) {
					lNotifica.setNote(lNote[0]);
				}
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatariSog);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
				lAutorita.setCodUfficioInserimento(lCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNotifica);
			}
		}
		// Soggetto con id
		if (!Utils.isNullObj(lIstitutoDetenzione)) {

			NotificaModel lNotifica = new NotificaModel();
			lNotifica.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICA);
			lNotifica.setDataInvio(aDataEmissione);
			// lNotifica.setNote(lNote);
			lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
			lNotifica.setDataInserimento(DateUtils.getSysDate());
			lNotifica.setCodUfficioInserimento(lCodiceUfficio);
			lNotifica.setCodEsito("-");
			lNotifica.setUffCodUfficio("-");
			lNotifica.setIstDetIdIstitutoDetenzione(lIstitutoDetenzione);
			lNotifica.setSogIdSoggetto(aFasEsteso.getFascicoloSige().getSogIdSoggetto());
			if (!lNote[0].equals("")) {
				lNotifica.setNote(lNote[0]);
			}
			// Aggiunge il model delle notifiche al vettore.
			lNotifiche.add(lNotifica);
		}

		// Notifica alla "Procura Generale della Repubblica presso la Corte di Appello" nel caso del Tribunale
		// di Sorveglianza
		// oppure notifica alla "Procura della Repubblica presso il Tribunale Ordinario" nel caso del
		// Tribunale di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_PROCURA_GENERALE)) {
			NotificaModel lNot = null;
			if (lCodTipoUfficio.equalsIgnoreCase("CASAP") || lCodTipoUfficio.equalsIgnoreCase("CAP"))
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PGCAP", lDescrComune);
			else if (lCodTipoUfficio.equalsIgnoreCase("TRIBSD")) {
				lCodComune = getUfficioUtenteConnesso().getCodDistretto().substring(0, 6);
				IComune lCtrl = SICOLookupRemote.getComuneRemote();
				lDescrComune = lCtrl.ExRicercaComuneByKey(lCodComune).getDescrizione();
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PM", lDescrComune);
			} else
				lCodiceUfficioPG = getCodUfficioByCodTipoUfficioDescrComune("PM", lDescrComune);

			// Procura Generale dell'ufficio di riferimento dell'utente connesso
			lNot = new NotificaModel();
			lNot.setCodTipoNotifica(ICostantiUdienzaSige.CODTIPONOTIFICACOMUNICAZIONE);
			lNot.setDataInvio(aDataEmissione);
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(lCodiceOperatore);
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(lCodiceUfficio);
			lNot.setUffCodUfficio(lCodiceUfficioPG);
			lNotifiche.add(lNot);
		}

		return lNotifiche;
		// FINE DESTINATARI NOTIFICHE
	}

}