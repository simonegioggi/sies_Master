package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sige.SIGEException;
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
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOrdinanzaNDPNLP extends ActInserisciEmissioneOrdinanza
		implements ICostantiProvvedimentoSige, ICostantiMotivazioneProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciOrdinanzaNDPNLP: inizio");

		if (this.isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Fascicolo Sige non in sessione!");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		// Eventuale Codice Tipo Giudizio da assegnare al Fascicolo SIGE
		String lTipoGiudizio = null;
		BigDecimal lIdCollegio = null;

		if (isSessionAttributeNullObj("tenori"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti!");

		// Si prelevano i tenori Sige in sessione.
		Vector lTenoriEstesi = (Vector) getSessionAttribute("tenori");

		if (lTenoriEstesi.size() == 0)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti!");

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
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data Emissione :" + lDataEmissione);

		// eventuale lettura Tipo Giudizio e Collegio
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO)) {
			lTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO).trim();
			String lCodMagAssegnatario = "";
			if (!isRequestParameterNullObj(ICostantiCollegio.CAMPO_ID_COLLEGIO)) {
				lIdCollegio = getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO);

				// Si risale al codice del Magistrato Assegnatario
				if (lFasEsteso != null && lFasEsteso.getMagAssegnatario() != null)
					lCodMagAssegnatario = lFasEsteso.getMagAssegnatario().getMagCodMagistrato();

				if (lCodMagAssegnatario.length() > 0 && lIdCollegio != null) {
					// Verifica appartenenza al Collegio del Magistrato Assegnatario
					ProvvedimentoSigeUtils lProSigeUtils = new ProvvedimentoSigeUtils();
					if (!lProSigeUtils.isMagistratoAssInCollegio(lIdCollegio, lCodMagAssegnatario))
						throw new SIGEException(SIGEException.USER_MESSAGE,
								"Il Magistrato Assegnatario deve far parte del collegio!");
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
		lProvModel.setCodTipoProvvedimento(ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA);
		lProvModel.setCodTipoProvvedimentoSige(ICostantiProvvedimentoSige.COD_ORDINANZA_NDPNLP);
		lProvModel.setDefinitorio("S");
		lProvModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che inserisce
		lProvModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																			// dell'operatore che inserisce
		lProvModel.setDataInserimento(DateUtils.getSysDate());
		lProvModel.setColIdCollegio(lIdCollegio);

		// lProvModel.setNote(getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_NOTE).trim());

		if (getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO) != null
				&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO) != null
				&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO).trim().length() > 1
				&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO).trim()
						.length() > 1)
			lProvModel.setCodUfficioDestinatario(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO),
					getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO)));

		if (!isRequestParameterNullObj(SEDE_MAGISTRATO_COMPETENTE)
				&& getRequestStringParameter(ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE).trim()
						.length() > 0)
			lProvModel.setCodUfficioDestinatario(UfficioUtils.getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE).trim()));

		// lTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO).trim();

		// Modifica del 08/03/2017 INIZIO *******************
		// preleva i dati della udienza sige
		BigDecimal idUdienza = super.getRequestBigDecimalParameter(
				ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		lProvModel.setUdiIdUdienzaSige(idUdienza);
		// Modifica del 08/03/2017 FINE *******************

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Provvedimento SIGE Valorizzato :" + lProvModel);

		// Impostazione EventoModel
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento("01");
		lEvento.setCodTipoProvvedimento("03");
		lEvento.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEvento.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEvento.setDataEmissione(lDataEmissione);
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
		siesLogger.debug("Evento Valorizzato: " + lEvento);

		// Tipo di template da assegnare all'evento, solo per quelli
		// presenti nella cbx e sono diversi da 01 ( che generazione automantica )
		// inserisce l'id del template nel model evento.
		// Da gestire per le stampe???
		// if( getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("02") )
		// lEvento.setTemIdTemplate( TEMPLATE_ORDINANZA_GENERICO );
		// else if(getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("03") )
		// lEvento.setTemIdTemplate( TEMPLATE_ORDINANZA_RIGETTO_GENERICO );
		// else if(getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("04") )
		// lEvento.setTemIdTemplate( TEMPLATE_ORDINANZA_NLP_GENERICO );

		ProvvedimentoSigeEventoModel lProvEveModel = new ProvvedimentoSigeEventoModel();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel(lEvento);

		lProvEveModel.setProvvedimento(lProvModel);
		lProvEveModel.setEventoNotifica(lEveNotMod);

		MotivazioneProvvedimentoSigeModel[] lMotivazioni = letturaMotivazioni();

		// Modifica del 08/03/2017 *** INIZIO *******
		// Chiamata al Controller per gli inserimenti.
		// lProvEveModel = lProvCtrl.ExInserisciProvvedimento(lProvEveModel, lTenori, lTipoGiudizio);
		lProvEveModel = lProvCtrl.ExInserisciProvvedimento(lProvEveModel, lTenori, lTipoGiudizio,
				lMotivazioni);

		IUdienzaSige ctrlUdiSige = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel udienza = ctrlUdiSige.ExRicercaUdienzaSigeById(idUdienza);
		setRequestAttribute("UdienzaSige", udienza);
		// Modifica del 08/03/2017 *** FINE *******

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

		// Si Rilegge il fasciclo SIGE Esteso e lo si inserisce in SESSIONE.
		IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
		lFasEsteso = lCtrlFas
				.ExRicercaEstesaFascicoloSigeByKey(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);

		setRequestAttribute("lProvvinserito", lProvModel);
		setRequestAttribute("data_emissione", lDataEmissione);

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");

		// Prepara la "pagina" di destinazione
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.provvedimento.action.ActDettaglioOrdinanza");
		lRedirigi.setParameter(CAMPO_ID_PROVVEDIMENTO_SIGE,
				(lProvEveModel.getProvvedimento().getIdProvvedimentoSige()).toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciOrdinanzaNDPNLP: fine");
		return lRedirigi.toString();
	}

	/**
	 * Lettura Motivazioni
	 * 
	 * @return
	 * @throws Exception
	 */
	protected MotivazioneProvvedimentoSigeModel[] letturaMotivazioni() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".letturaMotivazioni: inizio");

		Vector lMotivazioni = new Vector();
		BigDecimal lIdProvv = null;
		if (!isRequestParameterNullObj(ICostantiMotivazioneProvvedimento.CAMPO_PRO_SIG_ID_PROVVED_SIGE))
			lIdProvv = getRequestBigDecimalParameter(
					ICostantiMotivazioneProvvedimento.CAMPO_PRO_SIG_ID_PROVVED_SIGE);

		MotivazioneProvvedimentoSigeModel lMPSMod = new MotivazioneProvvedimentoSigeModel();

		lMPSMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMPSMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lMPSMod.setDataInserimento(DateUtils.getSysDate());
		lMPSMod.setProSigIdProvvedSige(lIdProvv);
		lMPSMod.setAltraMotivazione(
				getRequestStringParameter(ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE));

		boolean lMotivazioneInserita = false;

		if (isRequestChecked(ICostantiMotivazioneProvvedimento.CAMPO_CK_01)) {
			lMPSMod.setCodTipoMotivazione(
					getRequestStringParameter(ICostantiMotivazioneProvvedimento.CAMPO_CK_01));

			if (lMPSMod.getCodTipoMotivazione().equalsIgnoreCase("C1")) {
				lMPSMod.setDescrMotivazione("Restituzione degli atti");
				lMotivazioneInserita = true;
			} else if (lMPSMod.getCodTipoMotivazione().equalsIgnoreCase("C2")) {
				lMPSMod.setDescrMotivazione("Trasmissione degli atti");
				lMotivazioneInserita = true;
			} else if (lMPSMod.getCodTipoMotivazione().equalsIgnoreCase("C0")) {
				lMPSMod.setDescrMotivazione(
						getRequestStringParameter(ICostantiMotivazioneProvvedimento.CAMPO_ALTRA_MOTIVAZIONE));
				// Altra motivazione viene inserita solo se ne è stata fornita una descrizione
				if (lMPSMod.getAltraMotivazione().trim().length() > 0)
					lMotivazioneInserita = true;
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.info("Valore Cod Tipo Motivazione non previsto: " + lMPSMod.getCodTipoMotivazione());

			if (lMotivazioneInserita)
				lMotivazioni.add(lMPSMod);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Motivazione -> " + lMPSMod);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".letturaMotivazioni: fine");
		return (MotivazioneProvvedimentoSigeModel[]) lMotivazioni
				.toArray(new MotivazioneProvvedimentoSigeModel[0]);
	}

}