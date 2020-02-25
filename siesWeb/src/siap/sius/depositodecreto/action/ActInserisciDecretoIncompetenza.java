package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoEventoModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GPTenoreModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.motivazionedecreto.action.ICostantiMotivazioneDecreto;
import siap.sius.motivazionedecreto.controller.IMotivazioneDecreto;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciDecretoIncompetenza extends ActionSius implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Si prelevano dati di sessione.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodiceComune = getCodComuneUtenteConnesso();

		// Si preleva dall sessione il fascicolo GPModel.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// genny 18/11/03 Utilizzo il campo setCodUfficioInserimento come veicolo per trasmettere il codice
		// ufficio recuperato dal tipo ufficio e dalla descr ufficio
		if (!getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO).equalsIgnoreCase("-"))
			lFasGPMod.getFascicoloSiusModel()
					.setCodUfficioInserimento(getCodUfficioByCodTipoUfficioDescrComune(
							getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO),
							getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO)));

		// Preleva id generale procedimento.
		BigDecimal lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();

		// Istanzia model generale procedimento.
		GeneraleProcedimentoModel lGenProcModel = new GeneraleProcedimentoModel();
		lGenProcModel.setIdGeneraleProcedimento(lIdGenProc);
		lGenProcModel.setCodOggettoProcedimento(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		lGenProcModel.setDataAggiornamento(DateUtils.getSysDate());
		lGenProcModel.setCodUfficioAggiornamento(lCodiceUfficio);
		lGenProcModel.setCodOperatoreAggiornamento(lCodiceOperatore);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		if (lMagRel == null || lMagRel.getMagistrato() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Magistrato relatore non definito !");
		// MEV_65: Punto 1.13 se magistrato è scaduto impossibile inserire decreto od ordinanza
		else {
			IMagistrato im = SICOLookupRemote.getMagistratoRemote();
			MagistratoModel mm = new MagistratoModel();
			mm.setCodMagistrato(lMagRel.getMagistrato().getCodMagistrato());
			mm.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
			mm.setCognome(lMagRel.getMagistrato().getCognome());
			mm.setNome(lMagRel.getMagistrato().getNome());
			Vector v = im.ExRicercaMagistrato(mm);
			if (!v.isEmpty()) {
				MagistratoModel mag = (MagistratoModel) v.get(0);
				if (mag.getDataFineValidita() != null
						&& (DateUtils.isLower(mag.getDataFineValidita(), DateUtils.getSysDate())
								|| DateUtils.isEquals(mag.getDataFineValidita(), DateUtils.getSysDate())))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! Impossibile emettere il provvedimento. Assegnatario del procedimento è un magistrato non più in servizio!");
			}
		}

		// Prelevare il codice Magistrato_Relatore
		String lCodMagistrato = lMagRel.getMagistrato().getCodMagistrato();

		// Gestione oggetti Tenore.
		String lCodOggetti = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO);
		String lDescOggetti = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO);
		// STUB 19/04/2004 Aggiunti i Codici Dettaglio Oggetti.
		String lCodDettaglioOggetti = getRequestStringParameter(
				ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO);

		// Inserisce nel model aggregante l'Array di model dei Tenori e Il model GeneraleProcedimento.
		GPTenoreModel lGPTenoreModel = new GPTenoreModel();

		// Imposta il tipo di codice esito in funzione del tipo contenuto e della scelta del tipo motivazione
		String lCodEsito = this.impostaCodEsito();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(">>>>> Valore Cod Esito : " + lCodEsito);

		// STUB 19/04/2004 lGPTenoreModel.setTenori( this.parseOggettiTenori( lCodOggetti, lDescOggetti,
		// lCodMagistrato ) );
		TenoreModel[] lTenori = this.parseOggettiTenori(lCodOggetti, lDescOggetti, lCodDettaglioOggetti,
				lCodEsito, lCodMagistrato);
		lGPTenoreModel.setTenori(lTenori);
		lGPTenoreModel.setGeneraleProcedimentoModel(lGenProcModel);

		// Prepara il model DepositoDecreto.
		DepositoDecretoModel lDepDecrModel = new DepositoDecretoModel();
		lDepDecrModel
				.setDataEmissione(getRequestDateParameter(ICostantiDepositoDecreto.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiDepositoDecreto.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_EMISSIONE));
		lDepDecrModel.setCodTipoDecreto("04");
		lDepDecrModel.setCodMagistrato(lCodMagistrato);
		lDepDecrModel.setGenPridGeneraleProcedimento(lIdGenProc);
		lDepDecrModel.setCodOperatoreInserimento(lCodiceOperatore);
		lDepDecrModel.setCodUfficioInserimento(lCodiceUfficio);
		lDepDecrModel.setDataInserimento(DateUtils.getSysDate());

		// genny 18/11/03 Utilizzo il campo setCodUfficioComp codice ufficio recuperato dal tipo ufficio e
		// dalla descr ufficio
		if (!getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO).equalsIgnoreCase("-"))
			lDepDecrModel.setCodUfficioCompetente(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO),
					getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO)));

		// Si inserisce in decretoModel il valore inserito nel campo "rilevato".
		lDepDecrModel.setNote(getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_NOTE));

		// Prepara Model Evento.
		EventoModel lEventoModel = new EventoModel();
		lEventoModel.setCodTipoEvento("01"); // 01 = Provvedimento.
		lEventoModel.setCodTipoProvvedimento("02"); // 02 = Decreto.
		lEventoModel.setCodLuogoEmittente(lCodiceComune);
		lEventoModel.setCodUfficioEmittente(lCodiceUfficio);
		lEventoModel.setCodEsito(lCodEsito);
		// Angela aggiunta del codice magistrato
		lEventoModel.setCodMagistrato(lCodMagistrato);
		lEventoModel
				.setDataEmissione(getRequestDateParameter(ICostantiDepositoDecreto.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiDepositoDecreto.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_EMISSIONE));

		lEventoModel.setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lEventoModel.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lEventoModel.setCodOperatoreInserimento(lCodiceOperatore);
		lEventoModel.setCodUfficioInserimento(lCodiceUfficio);
		lEventoModel.setDataInserimento(DateUtils.getSysDate());
		lEventoModel.setCodLuogoDestinatario("-");
		lEventoModel.setCodTipoUfficioDestinatario("-");
		lEventoModel.setCodUfficioDestinatario("-");

		DepositoDecretoEventoModel lDepDecrEveModel = new DepositoDecretoEventoModel();
		lDepDecrEveModel.setDepositoDecreto(lDepDecrModel);
		lDepDecrEveModel.setEvento(lEventoModel);

		// Chiamata Controller per Inserimento.
		IDepositoDecreto lDepDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecreto(lGPTenoreModel, lDepDecrEveModel);

		// Lettura Motivazioni decreto
		Vector lMotivazioni = letturaMotivazioni(
				lDepDecrEveModel.getDepositoDecreto().getIdDepositoDecreto());

		// Chiamata al controller per Inserimento Motivazioni.
		IMotivazioneDecreto lCtrl = SIUSLookupRemote.getMotivazioneDecretoRemote();
		lCtrl.ExInserisciMotivazioniDecretoIncompetenza(
				(MotivazioneDecretoModel[]) lMotivazioni.toArray(new MotivazioneDecretoModel[0]));

		// Prepara la pagina di destinazione, in questo caso è il dettaglio del decreto d'inammissibilità.
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		// lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoIncompetenza");
		lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito");

		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
				lDepDecrEveModel.getEvento().getIdEvento().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRedirectTo.toString();
	}

	/**
	 * Metodo che ritorna il codice Esito, opportunamente valorizzato in funzione del tipo di Cod_Contenuto e
	 * Motivazione decreto.
	 *
	 * Per U017 ( Sanzioni Sostitutive ) - Restituzione Atto : 0183 - Trasmissione Atto : 0184
	 *
	 * Per Tutte le altre condizioni : 0005
	 *
	 * <p>
	 *
	 * @return ritorna il codice esito opportunamente impostato.
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	private String impostaCodEsito() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".impostaCodEsito(): inizio");

		String lCodEsito = "";
		if (getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equalsIgnoreCase("U017")) {
			// Recupera valore del radio button dalla request.
			String lCodMotivazioneDecreto = getRequestStringParameter(
					ICostantiMotivazioneDecreto.CAMPO_CK_01);

			if (lCodMotivazioneDecreto.equalsIgnoreCase("C0"))
				lCodEsito = "0005";
			else if (lCodMotivazioneDecreto.equalsIgnoreCase("C1"))
				lCodEsito = "0183";
			else if (lCodMotivazioneDecreto.equalsIgnoreCase("C2"))
				lCodEsito = "0184";
		} else
			lCodEsito = "0005";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".impostaCodEsito(): fine");

		return lCodEsito;
	}

	/**
	 * Lettura delle motivazioni del decreto di Incompetenza. Attualmente può essere inserita una sola
	 * motivazione.
	 * <p>
	 *
	 * @param aIdDepDecreto
	 * @return
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	Vector letturaMotivazioni(BigDecimal aIdDepDecreto) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".letturaMotivazioni(): inizio");
		// Lista delle motivazioni inserite
		Vector lMotivazioni = new Vector();

		// Istanziazione del Model Motivazione Decreto
		MotivazioneDecretoModel lMotMod = new MotivazioneDecretoModel();

		// Valorizzazione dei dati comuni a qualunque motivazione
		lMotMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMotMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lMotMod.setDataInserimento(DateUtils.getSysDate());
		lMotMod.setDepDecIdDepositoDecreto(aIdDepDecreto);

		boolean lMotivazioneInserita = false;

		if (isRequestChecked(ICostantiMotivazioneDecreto.CAMPO_CK_01)) {
			MotivazioneDecretoModel lMotModel = new MotivazioneDecretoModel(lMotMod);
			lMotModel.setCodTipoMotivazione(
					getRequestStringParameter(ICostantiMotivazioneDecreto.CAMPO_CK_01));

			if (lMotModel.getCodTipoMotivazione().equalsIgnoreCase("C1")) {
				lMotModel.setDescrMotivazione("Restituzione degli atti");
				lMotivazioneInserita = true;
			} else if (lMotModel.getCodTipoMotivazione().equalsIgnoreCase("C2")) {
				lMotModel.setDescrMotivazione("Trasmissione degli atti");
				lMotivazioneInserita = true;
			} else if (lMotModel.getCodTipoMotivazione().equalsIgnoreCase("C0")) {
				lMotModel.setAltraMotivazione(
						getRequestStringParameter(ICostantiMotivazioneDecreto.CAMPO_ALTRA_MOTIVAZIONE));
				// Altra motivazione viene inserita solo se ne è stata fornita una descrizione
				if (lMotModel.getAltraMotivazione().trim().length() > 0)
					lMotivazioneInserita = true;
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(
						"Valore Cod Tipo Motivazione non previsto: " + lMotModel.getCodTipoMotivazione());

			if (lMotivazioneInserita)
				lMotivazioni.add(lMotModel);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".letturaMotivazioni(): fine");

		return lMotivazioni;
	}

}