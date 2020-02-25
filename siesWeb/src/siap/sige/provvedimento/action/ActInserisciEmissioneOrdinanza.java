package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
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
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActInserisciEmissioneOrdinanza
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di emissione ordinanza SIGE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciEmissioneOrdinanza extends ActionSige implements ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Eventuale Codice Tipo Giudizio da assegnare al Fascicolo SIGE
	protected String mTipoGiudizio = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciEmissioneOrdinanza: inizio");

		if (isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "FascicoloSige non in sessione");

		gestioneRitorno();

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

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
		Vector<TenoreSigeModel> lTenori = lTenUtil.listaTenoriDaListaTenoriEstesi(lTenoriEstesi);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Dimensione lista Tenori :" + lTenori.size());

		// 15/03/2011 Impostazione a null del CodEsito dei nuovi Tenori
		for (TenoreSigeModel lTenMod : lTenori) {
			lTenMod.setCodEsitoSige(null);
		}

		ProvvedimentoSigeEventoModel lProvEveModel = letturaDatiProvvedimento();

		// MERGE v10: gestione udienza già fissata
		// Chiamata al Controller per gli inserimenti e le ricerche.
		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		BigDecimal idUdienza = getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		lProvEveModel = lProvCtrl.ExInserisciProvvedimento(lProvEveModel, lTenori, mTipoGiudizio);
		setRequestAttribute("lProvvinserito", lProvEveModel.getProvvedimento());
		setRequestAttribute("data_emissione", lProvEveModel.getProvvedimento().getDataEmissione());
		// 20170901: [SG] aggiunta costante
		String idProvvedimentoSige = "";
		if (lProvEveModel != null && lProvEveModel.getProvvedimento() != null
				&& lProvEveModel.getProvvedimento().getIdProvvedimentoSige() != null)
			idProvvedimentoSige = lProvEveModel.getProvvedimento().getIdProvvedimentoSige().toString();

		// Si Rilegge il fasciclo SIGE Esteso e lo si inserisce in SESSIONE.
		IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
		lFasEsteso = lCtrlFas
				.ExRicercaEstesaFascicoloSigeByKey(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);

		IUdienzaSige ctrlUdiSige = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel udienza = ctrlUdiSige.ExRicercaUdienzaSigeById(idUdienza);
		setRequestAttribute("UdienzaSige", udienza);

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

		// Prepara la "pagina" di destinazione
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.provvedimento.action.ActDettaglioOrdinanza");

		// 20170901: [SG] modificata gestione impostazione parametro; se esiste l'udienza allora
		// veniva impostato idProvvedimentoSige dell'udienza, altrimenti quello del ordinanza!
		// DEVE ESSERE SEMPRE quello dell'ordinanza
		lRedirigi.setParameter(CAMPO_ID_PROVVEDIMENTO_SIGE, idProvvedimentoSige);
		siesLogger.debug("ActInserisciEmissioneOrdinanza: fine");
		return lRedirigi.toString();
	}

	/**
	 * Confronta i Magistrati componenti del Collegio con il Magistrato Assegnatario per valutare se questi fa
	 * parte del Collegio stesso.
	 * 
	 * @param aIdCollegio
	 * @return
	 * @throws Exception
	 */
	private boolean isMagistratoAssInCollegio(BigDecimal aIdCollegio) throws Exception {

		boolean lRet = false;
		String lCodMagAssegnatario = "";

		// Si risale al codice del Magistrato Assegnatario
		FascicoloSigeEstesoModel lFasEsteso = getFascicoloSigeEstesoInSessione();
		if (lFasEsteso != null && lFasEsteso.getMagAssegnatario() != null)
			lCodMagAssegnatario = lFasEsteso.getMagAssegnatario().getMagCodMagistrato();

		if (lCodMagAssegnatario.length() > 0) {
			// Ricerca del Collegio
			ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
			CollegioModel lColMod = lCtrl.ExRicercaCollegioByKey(aIdCollegio);
			if (lColMod != null && lColMod.getCollegioMagistrati() != null) {
				// Magistrati del Collegio
				CollegioMagistratoModel[] lElencoMag = lColMod.getCollegioMagistrati();

				for (int i = 0; i < lElencoMag.length; i++) {
					if (lElencoMag[i].getMagCodMagistrato().equalsIgnoreCase(lCodMagAssegnatario)) {
						lRet = true;
						break;
					}
				}
			}
		}

		return lRet;

	}

	/**
	 * Lettura dei dati dalla request.
	 * 
	 * @return ProvvedimentoSigeEventoModel
	 * @throws Exception
	 */
	protected ProvvedimentoSigeEventoModel letturaDatiProvvedimento() throws Exception {

		// Eventuale Codice Tipo Giudizio da assegnare al Fascicolo SIGE
		BigDecimal lIdCollegio = null;

		if (isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "FascicoloSige non in sessione");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		// Preleva data di emissione
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data Emissione :" + lDataEmissione);

		// eventuale lettura Tipo Giudizio e Collegio
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO)) {
			mTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO).trim();

			if (!isRequestParameterNullObj(ICostantiCollegio.CAMPO_ID_COLLEGIO)
					// 20170907: [SG] aggiunto controllo
					&& !Utils.isNullObj(getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO))) {
				lIdCollegio = getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO);
				// Verifica appartenenza al Collegio del Magistrato Assegnatario
				if (!mTipoGiudizio.equalsIgnoreCase("M") && !isMagistratoAssInCollegio(lIdCollegio))
					throw new SIGEException(SIGEException.USER_MESSAGE,
							"Il Magistrato Assegnatario deve far parte del collegio !");
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Tipo Giudizio :" + mTipoGiudizio);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID Collegio :" + lIdCollegio);
		}

		// Impostazione ProvvedimentoModel
		ProvvedimentoSigeModel lProvModel = new ProvvedimentoSigeModel();
		lProvModel.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		// l'anno va impostato al momento del deposito.
		// lProvModel.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lProvModel.setChiaveUfficio(getCodUfficioUtenteConnesso());
		lProvModel.setDataEmissione(lDataEmissione);
		lProvModel.setCodTipoProvvedimento(ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA);
		lProvModel.setCodTipoProvvedimentoSige(ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA);
		lProvModel.setDefinitorio("S");
		lProvModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che inserisce
		lProvModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																			// dell'operatore che inserisce
		lProvModel.setDataInserimento(DateUtils.getSysDate());
		lProvModel.setColIdCollegio(lIdCollegio);

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

		return lProvEveModel;
	}

	/**
	 * Lettura Motivazioni per Ordinanza di Incompetenza
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

	/**
	 * Ritorna l'Ufficio dal tipo ufficio, codice comune
	 * <p>
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

	protected MotivazioneProvvedimentoSigeModel[] letturaMotivazioniInammissibilita() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".letturaMotivazioniInammissibilita: inizio");

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

		if (isRequestChecked(ICostantiMotivazioneProvvedimento.CAMPO_CK_01)) {
			String[] lMotiviValues = getRequestStringParameters(
					ICostantiMotivazioneProvvedimento.CAMPO_CK_01);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("N.ro Motivi inseriti: " + lMotiviValues.length);

			for (int i = 0; i < lMotiviValues.length; i++) {
				String lCodMotivo = lMotiviValues[i];

				MotivazioneProvvedimentoSigeModel lMPSMod1 = new MotivazioneProvvedimentoSigeModel(lMPSMod);
				lMPSMod1.setCodTipoMotivazione(lCodMotivo);
				String lNomeCampo1 = ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE + lCodMotivo;
				String lNomeCampo2 = ICostantiMotivazioneProvvedimento.CAMPO_ALTRA_MOTIVAZIONE + lCodMotivo;

				if (!isRequestParameterNullObj(lNomeCampo1))
					lMPSMod1.setDescrMotivazione(getRequestStringParameter(lNomeCampo1));
				if (!isRequestParameterNullObj(lNomeCampo2))
					lMPSMod1.setAltraMotivazione(getRequestStringParameter(lNomeCampo2));
				lMotivazioni.add(lMPSMod1);
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".letturaMotivazioniInammissibilita: fine");
		return (MotivazioneProvvedimentoSigeModel[]) lMotivazioni
				.toArray(new MotivazioneProvvedimentoSigeModel[0]);
	}

	/**
	 * metodo introdotto per la nuova gestione dell'udienza monocratica/collegiale per sies 11.2.1
	 * 
	 * @param idFascicoloSige
	 * @return
	 * @throws F3BException
	 */
	protected MagistratoAssegnatarioMagistratoModel prepareModificaAssegnatarioModel(BigDecimal idFascicoloSige)
			throws F3BException {

		MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();
		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)) {
			lMagistrato.getMagistratoAssegnatario().setMagCodMagistrato(
					this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		} else {
			lMagistrato.getMagistratoAssegnatario().setMagCodMagistrato(
					this.getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE));
		}

		lMagistrato.getMagistratoAssegnatario().setFasSigeIdFascicoloSige(idFascicoloSige);
		lMagistrato.getMagistratoAssegnatario().setDataInizio(DateUtils.getSysDate());
		lMagistrato.getMagistratoAssegnatario().setCodRuoloMagistrato("03");
		lMagistrato.getMagistratoAssegnatario().setDataInserimento(DateUtils.getSysDate());
		lMagistrato.getMagistratoAssegnatario().setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lMagistrato.getMagistratoAssegnatario().setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
			lMagistrato.getMagistratoAssegnatario().setIdAssistente(
					getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE))
			lMagistrato.getMagistratoAssegnatario()
					.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));

		return lMagistrato;
	}

}