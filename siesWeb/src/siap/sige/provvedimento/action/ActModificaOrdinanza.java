package siap.sige.provvedimento.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.ufficio.controller.UfficioUtils;
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
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActModificaOrdinanza
 * </p>
 * <p>
 * Description: Classe Action per la modifica ordinanza SIGE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Luigi
 * @version 1.0
 */
public class ActModificaOrdinanza extends ActInserisciEmissioneOrdinanza implements ICostantiUdienzaSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActModificaOrdinanza: inizio");

		MotivazioneProvvedimentoSigeModel[] lMotivazioni = null;
		BigDecimal lIdProvvedimento = getRequestBigDecimalParameter(CAMPO_ID_PROVVEDIMENTO_SIGE);
		BigDecimal lIdEvento = getRequestBigDecimalParameter(CAMPO_ID_EVENTO_GENERATO);

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = getFascicoloSigeEstesoInSessione();

		// I dati vengono letti utilizzando una funzione dell'ancestor
		ProvvedimentoSigeEventoModel lProvEveModel = letturaDatiProvvedimento();

		// Vengono inseriti i dati relativi all'aggionamento
		lProvEveModel.getProvvedimento().setIdProvvedimentoSige(lIdProvvedimento);
		lProvEveModel.getProvvedimento().setIdEventoGenerato(lIdEvento);
		lProvEveModel.getProvvedimento().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																								// dell'operatore
																								// che
																								// inserisce
		lProvEveModel.getProvvedimento().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																									// dell'ufficio
																									// dell'operatore
																									// che
																									// inserisce
		lProvEveModel.getProvvedimento().setDataAggiornamento(DateUtils.getSysDate());

		// Ordinanza NDP/NLP
		if (!isRequestParameterNullObj(CAMPO_NOTE)) {
			lProvEveModel.getProvvedimento()
					.setNote(getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_NOTE).trim());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MOTIVAZIONI: " + lProvEveModel.getProvvedimento().getNote());
		}

		// Lettura Ufficio di Competenza Corte Suprema di Cassazione
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA)) {
			String lCodTipoUffCompCorteSuprema = getRequestStringParameter(
					ICostantiFascicoloSige.CAMPO_COD_TIPO_UFF_COMP_CORTE_SUPREMA);
			String lDescComuneSedeUffCompCorteSuprema = getRequestStringParameter(
					ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFF_COMP_CORTE_SUPREMA);
			String lCodUffCompCorteSuprema = null;
			if (lCodTipoUffCompCorteSuprema != null && !lCodTipoUffCompCorteSuprema.equals("-")
					&& !lCodTipoUffCompCorteSuprema.equals("")) {
				lCodUffCompCorteSuprema = getCodUfficioByCodTipoUfficioDescrComune(
						lCodTipoUffCompCorteSuprema, lDescComuneSedeUffCompCorteSuprema);
				lProvEveModel.getProvvedimento().setCodUffCompCorteSuprema(lCodUffCompCorteSuprema);
			}
		}

		// preleva i dati della udienza sige
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE)) {
			BigDecimal idUdienza = super.getRequestBigDecimalParameter(
					ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
			lProvEveModel.getProvvedimento().setUdiIdUdienzaSige(idUdienza);
		}

		// Gestione Ordinanza di Sospensione Precedente Ordinanza
		// Mancando i dati del provvedimento (es. tipo Ordinanza) occorre leggerli
		IProvvedimentoSige ctrPS = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEveDaId = ctrPS.ExRicercaProvvedimentoById(lIdProvvedimento);

		if (lProvEveDaId != null && lProvEveDaId.getProvvedimento() != null
				&& lProvEveDaId.getProvvedimento().getCodTipoProvvedimentoSige() != null) {
			if (!isRequestParameterNullObj(
					ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE_SOSPENSIONE)
					&& lProvEveDaId.getProvvedimento().getCodTipoProvvedimentoSige()
							.compareTo(COD_ORDINANZA_SOSPENSIONE) == 0) {
				lProvEveModel.getProvvedimento().setNote(getRequestStringParameter(
						ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE_SOSPENSIONE));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("MOTIVAZIONI SOSPENSIONE: " + lProvEveModel.getProvvedimento().getNote());
			}
		}

		if (!isRequestParameterNullObj(SEDE_MAGISTRATO_COMPETENTE)
				&& getRequestStringParameter(ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE).trim()
						.length() > 0) {
			lProvEveModel.getProvvedimento()
					.setCodUfficioDestinatario(UfficioUtils.getCodUfficioByCodTipoUfficioDescrComune("UDS",
							getRequestStringParameter(ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE)
									.trim()));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SEDE MAG : " + lProvEveModel.getProvvedimento().getCodUfficioDestinatario());
		}

		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO)
				&& !isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO)) {
			if (getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO).trim().length() > 1
					&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO).trim()
							.length() > 1)

				lProvEveModel.getProvvedimento()
						.setCodUfficioDestinatario(getCodUfficioByCodTipoUfficioDescrComune(
								getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO),
								getRequestStringParameter(
										ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO)));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"COD UFF. DEST. : " + lProvEveModel.getProvvedimento().getCodUfficioDestinatario());
		}

		// Eventuali Motivazioni
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROV_SIGE)) {
			String lCodTipoProvSige = getRequestStringParameter(CAMPO_COD_TIPO_PROV_SIGE);
			if (lCodTipoProvSige.equalsIgnoreCase(COD_DECRETO_INAMMISSIBILITA))
				lMotivazioni = letturaMotivazioniInammissibilita();
			else if (lCodTipoProvSige.equalsIgnoreCase(COD_ORDINANZA_INCOMPETENZA))
				lMotivazioni = letturaMotivazioni();
		}

		// Si chiama Controller per l'aggiornamento dati.
		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		lProvCtrl.ExModificaProvvedimentoSige(lProvEveModel.getProvvedimento(), lMotivazioni);

		// Si Rilegge il fasciclo SIGE Esteso e lo si inserisce in SESSIONE.
		IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
		lFasEsteso = lCtrlFas
				.ExRicercaEstesaFascicoloSigeByKey(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");

		// INTERVENTO PER 11.2.1
		// controllo e gestione dell'eventuale aggiornamento del magistrato assegnatario
		MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();
		// MagistratoAssegnatarioModel llMagModRet = null;
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
		lRedirigi.setParameter(CAMPO_ID_PROVVEDIMENTO_SIGE,
				(lProvEveModel.getProvvedimento().getIdProvvedimentoSige()).toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActModificaOrdinanza: fine");
		return lRedirigi.toString();
	}
}