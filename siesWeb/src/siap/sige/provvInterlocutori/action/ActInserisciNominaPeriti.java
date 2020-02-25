package siap.sige.provvInterlocutori.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.SIGEException;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.motivazioneprovvedimento.action.ICostantiMotivazioneProvvedimento;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.provvedimento.util.ProvvedimentoSigeUtils;
import siap.sige.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActInserisciNominaPeriti extends ActionSige implements ICostantiProvvInterlocutoriSige,
		ICostantiProvvedimentoSige, ICostantiMotivazioneProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		String lSedi[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		String lDestinatari[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
		String lAvvocato[] = getRequestStringParameters(ICostantiUdienzaSige.CAMPO_COD_AVVOCATO);
		String[] lNote = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_NOTE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciDecretoIrreperibilita: inizio");

		if (this.isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Fascicolo Sige non in sessione");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

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
		// 04/12/2010 Impostazione COD_ESITO_NOMINA_PERITI.
		if (lTenori != null)
			for (int i = 0; i < lTenori.size(); i++) {
				// Valorizzazione dell'Esito Automatico previsto per tale Ordinanza
				((TenoreSigeModel) lTenori.get(i)).setCodEsitoSige(COD_ESITO_NOMINA_PERITI);
			}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Dimensione lista Tenori :" + lTenori.size());

		// Preleva data di emissione
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);
		// Date lDataRichiesta=getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_INSERIMENTO,
		// ICostantiEvento.CAMPO_MESE_DATA_INSERIMENTO, ICostantiEvento.CAMPO_GIORNO_DATA_INSERIMENTO );

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data Emissione :" + lDataEmissione);

		// eventuale lettura Tipo Giudizio e Collegio
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO)) {
			lTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO).trim();
			String lCodMagAssegnatario = "";
			if (!isRequestParameterNullObj(ICostantiCollegio.CAMPO_ID_COLLEGIO)
					&& lTipoGiudizio.compareTo("C") == 0) {
				lIdCollegio = getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO);

				// Si risale al codice del Magistrato Assegnatario
				if (lFasEsteso != null && lFasEsteso.getMagAssegnatario() != null)
					lCodMagAssegnatario = lFasEsteso.getMagAssegnatario().getMagCodMagistrato();

				if (lCodMagAssegnatario.length() > 0 && lIdCollegio!=null) {
					// Verifica appartenenza al Collegio del Magistrato Assegnatario
					ProvvedimentoSigeUtils lProSigeUtils = new ProvvedimentoSigeUtils();
					if (!lProSigeUtils.isMagistratoAssInCollegio(lIdCollegio, lCodMagAssegnatario))
						throw new SIGEException(SIGEException.USER_MESSAGE,
								"Il Magistrato Assegnatario deve far parte del collegio !");
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Tipo Giudizio :" + lTipoGiudizio);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("ID Collegio :" + lIdCollegio);
		}

		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		// Impostazione del Provvedimento.
		ProvvedimentoSigeModel lProvModel = new ProvvedimentoSigeModel();
		lProvModel.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		lProvModel.setChiaveUfficio(getCodUfficioUtenteConnesso());
		lProvModel.setDataEmissione(lDataEmissione);
		lProvModel.setCodTipoProvvedimento("02"); // Decreto
		lProvModel.setCodTipoProvvedimentoSige("13");
		lProvModel.setDefinitorio("N");
		lProvModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che inserisce
		lProvModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																			// dell'operatore che inserisce
		lProvModel.setDataInserimento(DateUtils.getSysDate());
		lProvModel.setColIdCollegio(lIdCollegio);
		lProvModel.setNote(getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_AGGIUNTIVO));

		// Modifica del 08/03/2017 *** INIZIO *******
		// preleva i dati della udienza sige
		BigDecimal idUdienza = super
				.getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		lProvModel.setUdiIdUdienzaSige(idUdienza);
		// Modifica del 08/03/2017 *** FINE *******

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Provvedimento SIGE Valorizzato :" + lProvModel);

		// Impostazione EventoModel
		// EventoModel lEvento = new EventoModel();
		EventoNotificaModel lEvento = new EventoNotificaModel();

		lEvento.getEvento().setCodTipoEvento("01");
		// lEvento.setCodTipoProvvedimento(ICostantiProvvedimentoSige.COD_ORDINANZA_INCOMPETENZA);
		lEvento.getEvento().setCodTipoProvvedimento("02");
		lEvento.getEvento().setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEvento.getEvento().setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEvento.getEvento().setDataEmissione(lDataEmissione);
		lEvento.getEvento().setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEvento.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEvento.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEvento.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEvento.getEvento().setCodTipoUfficioDestinatario("-");
		lEvento.getEvento().setCodLuogoDestinatario("-");
		lEvento.getEvento().setCodMotivo("-");
		lEvento.getEvento().setCodEsito("-");

		ProvvedimentoSigeEventoModel lProvEveModel = new ProvvedimentoSigeEventoModel();

		// Preleva le note dalla form
		/*
		 * String lCampiNoteReq = getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_AGGIUNTIVO); if
		 * (lCampiNoteReq != null && lCampiNoteReq.length() > 0) { CampoNotaModel[] lCampiNote = new
		 * CampoNotaModel[1]; lCampiNote[0] = new CampoNotaModel(); lCampiNote[0].setDescr(lCampiNoteReq);
		 * lCampiNote[0].setCodOperatoreInserimento(getCodUtenteConnesso());
		 * lCampiNote[0].setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		 * lCampiNote[0].setDataInserimento(DateUtils.getSysDate()); lEvento.setCampoNote(lCampiNote); }
		 */
		// Vector per le notifiche.
		Vector lNotificaV = new Vector();

		// Avvocati & Altro Destinatario
		int lSize = lDestinatari.length;
		for (int x = 0; x < lSize; x++) {
			if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {

				String lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();

				NotificaModel lNotifica = new NotificaModel();
				// lNotifica.setEveIdEvento(lProvEveModel.getProvvedimento().getIdEventoGenerato());
				lNotifica.setCodTipoNotifica("N");
				lNotifica.setDataInvio(DateUtils.getSysDate());
				lNotifica.setCodEsito("-");
				lNotifica.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNotifica.setDataInserimento(DateUtils.getSysDate());
				lNotifica.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lNotifica.setUffCodUfficio("-");

				if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 0) {
					lNotifica.setCodTipoNotifica("N");
					BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
					lNotifica.setAvvIdAvvocatoFascicoloSige(lAvvid);
					AvvocatoSigeModel lAvvSige = new AvvocatoSigeModel();
					lAvvSige.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(lAvvid);
					lNotifica.setAvvSige(lAvvSige);
				}
				// lNotifica.setNote(lNote[x+1]);
				lNotifica.setNote(lNote[x]);
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatari[x]);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(getCodUtenteConnesso());
				lAutorita.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lAutorita.setDataInserimento(DateUtils.getSysDate());

				// Aggiunge il model Autorità Esterna alla Notifica
				lNotifica.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.
				lNotificaV.add(lNotifica);
			}
		}

		lEvento.setNotifiche((NotificaModel[]) lNotificaV.toArray(new NotificaModel[0]));
		lProvEveModel.setProvvedimento(lProvModel);
		lProvEveModel.setEventoNotifica(lEvento);
		lProvEveModel = lProvCtrl.ExInserisciProvvEveNotifica(lProvEveModel, lEvento, lTenori, lTipoGiudizio);

		// Modifica del 08/03/2017 *** INIZIO *******
		IUdienzaSige ctrlUdiSige = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel udienza = ctrlUdiSige.ExRicercaUdienzaSigeById(idUdienza);
		setRequestAttribute("UdienzaSige", udienza);
		// Modifica del 08/03/2017 *** FINE *******

		// Si Rilegge il fasciclo SIGE Esteso e lo si inserisce in SESSIONE.
		IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
		lFasEsteso = lCtrlFas.ExRicercaEstesaFascicoloSigeByKey(lFasEsteso.getFascicoloSige()
				.getIdFascicoloSige());
		setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);

		setRequestAttribute("lProvvinserito", lProvModel);
		setRequestAttribute("data_emissione", lDataEmissione);

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciNominaPeriti: fine");

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.provvInterlocutori.action.ActDettaglioNominaPeriti&"
				+ ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE + "="
				+ lProvEveModel.getProvvedimento().getIdProvvedimentoSige().toString() + "&modalita=I";

		return lPage;
	}

}