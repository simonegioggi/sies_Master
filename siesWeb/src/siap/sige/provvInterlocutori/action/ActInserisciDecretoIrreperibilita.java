package siap.sige.provvInterlocutori.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.istruttoria.action.ICostantiIstruttoria;
import siap.sige.SIGEException;
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

public class ActInserisciDecretoIrreperibilita extends ActionSige implements ICostantiProvvInterlocutoriSige,
		ICostantiProvvedimentoSige, ICostantiMotivazioneProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

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
		// 04/12/2010 Impostazione COD_ESITO_DICHIARA_IRREPERIBILITA.
		if (lTenori != null)
			for (int i = 0; i < lTenori.size(); i++) {
				// Valorizzazione dell'Esito Automatico previsto per tale Ordinanza
				((TenoreSigeModel) lTenori.get(i)).setCodEsitoSige(COD_ESITO_DICHIARA_IRREPERIBILITA);
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

				if (lCodMagAssegnatario.length() > 0 && lIdCollegio != null) {
					// Verifica appartenenza al Collegio del Magistrato Assegnatario
					ProvvedimentoSigeUtils lProSigeUtils = new ProvvedimentoSigeUtils();
					if (!lProSigeUtils.isMagistratoAssInCollegio(lIdCollegio, lCodMagAssegnatario))
						throw new SIGEException(SIGEException.USER_MESSAGE,
								"Il Magistrato Assegnatario deve far parte del collegio !");
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
		lProvModel.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		// l'anno va impostato al momento del deposito.
		// lProvModel.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lProvModel.setChiaveUfficio(getCodUfficioUtenteConnesso());
		lProvModel.setDataEmissione(lDataEmissione);
		lProvModel.setCodTipoProvvedimento("02"); // Decreto
		lProvModel.setCodTipoProvvedimentoSige("12");
		lProvModel.setDefinitorio("N");
		lProvModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che inserisce
		lProvModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																			// dell'operatore che inserisce
		lProvModel.setDataInserimento(DateUtils.getSysDate());
		lProvModel.setColIdCollegio(lIdCollegio);
		lProvModel.setNote(getRequestStringParameter(ICostantiIstruttoria.CAMPO_NOTE));

		// Modifica del 08/03/2017 *** INIZIO *******
		// Aggiunti campi "Ufficio Competente" e "Sede"
		if (getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO) != null
				&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO) != null
				&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO).trim().length() > 1
				&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO).trim()
						.length() > 1)
			lProvModel.setCodUfficioDestinatario(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO),
					getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO)));

		// preleva i dati della udienza sige
		BigDecimal idUdienza = super
				.getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		lProvModel.setUdiIdUdienzaSige(idUdienza);
		// Modifica del 08/03/2017 *** FINE *******

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Provvedimento SIGE Valorizzato :" + lProvModel);

		// Impostazione EventoModel
		EventoModel lEvento = new EventoModel();

		lEvento.setCodTipoEvento("01");
		// lEvento.setCodTipoProvvedimento(ICostantiProvvedimentoSige.COD_ORDINANZA_INCOMPETENZA);
		lEvento.setCodTipoProvvedimento("02");
		lEvento.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEvento.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEvento.setDataEmissione(lDataEmissione);
		// lEvento.setDataRichiesta(lDataRichiesta);
		// lEvento.setFasSieIdFascicoloSiep(lFasEsteso.getFascicoloSiep().getIdFascicoloSiep() );
		lEvento.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEvento.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEvento.setDataInserimento(DateUtils.getSysDate());
		lEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEvento.setCodTipoUfficioDestinatario("-");
		lEvento.setCodLuogoDestinatario("-");
		lEvento.setCodMotivo("-");
		lEvento.setCodEsito("-");

		// Definizione note
		// NotificaModel lNotifiche[] = new NotificaModel[1];
		// NotificaModel lNot = new NotificaModel();

		ProvvedimentoSigeEventoModel lProvEveModel = new ProvvedimentoSigeEventoModel();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel(lEvento);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Evento Valorizzato :" + lEvento);

		lProvEveModel.setProvvedimento(lProvModel);
		lProvEveModel.setEventoNotifica(lEveNotMod);

		// Chiamata al Controller per gli inserimenti.
		lProvEveModel = lProvCtrl.ExInserisciProvvedimento(lProvEveModel, lTenori, lTipoGiudizio);

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
		siesLogger.debug("ActInserisciRogatoriaMdS: fine");

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.provvInterlocutori.action.ActDettaglioDecretoIrreperibilita&"
				+ ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE + "="
				+ lProvEveModel.getProvvedimento().getIdProvvedimentoSige().toString() + "&modalita=I";

		return lPage;
	}

}