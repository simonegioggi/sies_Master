package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GPTenoreModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInserisciEmissioneOrdinanza
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di una emissione ordinanza.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */
public class ActInserisciEmissioneOrdinanza extends ActionSiap implements ICostantiDepositoOrdinanzaPc {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lRetPage = PG_INSERISCI_EMISSIONE_ORDINANZA;

		// Recupero l'utente e il Fascicolo SIEP dalla sessione
		// Istanzio il Model che incapsula il FascicoloSIUS, il GeneraleProcedimento
		// e il Tenore
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva id generale procedimento.
		BigDecimal lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		if (lIdGenProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "ID Generale Procedimento non in sessione");

		// MEV_65: Punto 1.13 se magistrato è scaduto impossibile inserire decreto od ordinanza
		if (lFasGPMod.getFascicoloSiusModel() != null) {
			IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
			MagistratoRelatoreModel mrm = imr
					.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			if (mrm != null && mrm.getMagistrato() != null) {
				IMagistrato im = SICOLookupRemote.getMagistratoRemote();
				MagistratoModel mm = new MagistratoModel();
				mm.setCodMagistrato(mrm.getMagistrato().getCodMagistrato());
				mm.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
				mm.setCognome(mrm.getMagistrato().getCognome());
				mm.setNome(mrm.getMagistrato().getNome());
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
		}

		// Preleva data di emissione
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);

		// IDepositoOrdinanzaPc lDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		// Verifica esistenza di un deposito ordinanza per il fasciclo sius
		// selezionato con la stessa data di emissione.
		// Controllo eliminato Luigi 17-5-2004
		/*
		 * if( lDepOrdCtrl.ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc(lIdGenProc,lDataEmissione)) {
		 * lRetPage = IWebConstants.PG_MESSAGE; setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		 * "Operazione non consentita. Per il procedimento indicato è già stato emesso una ordinanza nella stessa data."
		 * ); }
		 */
		GPTenoreModel lGPModel = new GPTenoreModel();

		lGPModel.setGeneraleProcedimentoModel(lFasGPMod.getGeneraleProcedimentoModel());
		lGPModel.getGeneraleProcedimentoModel().setCodOggettoProcedimento(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		lGPModel.getGeneraleProcedimentoModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																											// dell'ufficio
																											// dell'operatore
																											// che
																											// inserisce
		lGPModel.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																										// dell'operatore
																										// che
																										// inserisce
		lGPModel.getGeneraleProcedimentoModel().setDataAggiornamento(DateUtils.getSysDate());
		lGPModel.getGeneraleProcedimentoModel()
				.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lGPModel.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

		// Il campo FasSiuIdFascicoloSius di Generale Procedimento viene impostato nel controller

		// Caricamento Tenore
		StringTokenizer lStCodice = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO), "|");
		StringTokenizer lStDescr = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO), "\n");
		// STUB 12/11/2003 Aggiunti i Codici Dettaglio Oggetti.
		String lStCodiceDet = new String(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO));
		int lSizeVector = lStCodice.countTokens();
		TenoreModel lTenori[] = new TenoreModel[lSizeVector];
		int lIndex = 0;

		String[] lEsiti = new String[lSizeVector];
		while (lStCodice.hasMoreTokens()) {
			TenoreModel lTenModel = new TenoreModel();

			lTenModel.setCodOggettoTenore(lStCodice.nextToken());
			lTenModel.setDescrOggettoTenore(lStDescr.nextToken());
			lTenModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																				// dell'operatore che
																				// inserisce
			lTenModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che
																			// inserisce
			lTenModel.setDataInserimento(DateUtils.getSysDate());
			lTenModel.setGenPridGeneraleProcedimento(
					lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenModel.setProgrTenore(new BigDecimal((double) (lIndex + 1)));
			lTenModel.setCodEsitoTenore("-");

			// 21/04/2004 Aggiunta la valorizzazione dell'eventuale Dettaglio Oggetto.
			if ((lStCodiceDet).indexOf(lTenModel.getCodOggettoTenore() + "0") < 0) {
				lTenModel.setCodDettaglioOggetto("-");
			} else {
				String lCodDettaglioCorrente = lStCodiceDet.substring(
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 4,
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 8);
				lTenModel.setCodDettaglioOggetto(lCodDettaglioCorrente);
			}

			// Setto l'Array su GPtenoreModel
			lTenori[lIndex] = lTenModel;
			lEsiti[lIndex] = getEsito(lTenModel.getCodOggettoTenore());

			lIndex++;
		}

		lGPModel.setTenori(lTenori);

		IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		lGPModel = lCtrl.ExInserisciEmissioneOrdinanza(lGPModel);

		lGPModel.setEsiti(lEsiti);

		// Si Rilegge il fasciclo SIUS GP Model e lo si inserisce in SESSIONE.
		IFascicoloSius lCtrlFas = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasGPMod = lCtrlFas.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		setSessionAttribute("fascicoloSiusGP", lFasGPMod);

		setRequestAttribute("lGPTenModel", lGPModel);
		setRequestAttribute("data_emissione", lDataEmissione);

		// Riempimento dei campi della pagina successiva

		// restituisce la jsp di VIEW
		// return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS+"="+lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
		return lRetPage;
	}

	// Preleva gli esiti dalla CG_REF_CODES.
	@SuppressWarnings("rawtypes")
	private String getEsito(String codiceOggetti) throws Exception {

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		Collection lColl = lDecodifiche.ExRicercaEsitiByOggetto(codiceOggetti);
		Option lOption = new Option(lColl);

		return lOption.toString();
	}

}