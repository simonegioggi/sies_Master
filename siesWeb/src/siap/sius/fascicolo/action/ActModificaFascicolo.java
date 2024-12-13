package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.StringTokenizer;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisuraalternativa.action.ICostantiEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActModificaFascicolo - Classe Azione di modifica del Fascicolo SIUS
 */
public class ActModificaFascicolo extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Recupero l'utente e il Fascicolo SIUS dalla sessione
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		// Istanzio il Model e lo carico con quello posto in sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// MEV10-s3: gestito passaggio alla maggiore età del soggetto
		if (!isRequestParameterNullObj("oscuraEtichettaMinore")) {
			oscuraProcessRequest(lFasGPMod, lUtenteMod);
			// restituisce la jsp di VIEW
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIUS + "="
					+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
		}

		// 27/03/2009 Controllo Ufficio mittente.
		// Le EMA accettano mittenti di TDS o UDS; oppure PM o PGCAP in caso di arresti domiciliari.
		String aDescrUfficioMittente = DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getMittenteAtto(),
				getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
		String lCodTipoUfficioMittente = DecodificheUtils
				.getCodebyDesc(DecodificheManager.getInstance().getTipoUfficio(), aDescrUfficioMittente);

		// Modifica del 10/09/2013 mev "Revisione Misure di Sicurezza SIUS"
		// Eliminato controllo su ufficio mittente se il campo non è valorizzato
		// nel caso di "Iscrizione di una Esecuzione Misura di Sicurezza"
		String[] lArrayCodici = (getRequestStringParameters(ICostantiFascicoloSius.CAMPO_COD_OGGETTO));
		String lCodiciOggetto = "";

		for (int i = 0; i < lArrayCodici.length; i++)
			lCodiciOggetto += lArrayCodici[i];

		if (!isRequestParameterNullObj(CAMPO_COD_CONTENUTO)) {
			// MEV10-s3: aggiunte or condition per gestire uffici minorenni
			if (((CAMPO_COD_CONTENUTO.compareTo("U019") == 0 && lCodTipoUfficioMittente.compareTo("UDS") != 0
					&& lCodTipoUfficioMittente.compareTo("UDSM") != 0
					&& lCodTipoUfficioMittente.compareTo("TDS") != 0
					&& lCodTipoUfficioMittente.compareTo("TDSM") != 0)
					|| (CAMPO_COD_CONTENUTO.compareTo("U004") == 0
							&& lCodTipoUfficioMittente.compareTo("TDS") != 0
							&& lCodTipoUfficioMittente.compareTo("TDSM") != 0
							&& lCodTipoUfficioMittente.compareTo("UDS") != 0
							&& lCodTipoUfficioMittente.compareTo("UDSM") != 0
							&& lCodTipoUfficioMittente.compareTo("-") != 0))
					&& (!(CAMPO_COD_CONTENUTO.compareTo("U004") == 0
							&& (lCodTipoUfficioMittente.compareTo("PM") == 0
									|| lCodTipoUfficioMittente.compareTo("PGCAP") == 0)
							&& lCodiciOggetto.indexOf("2368") >= 0)))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Ufficio Mittente non valido per il contenuto selezionato");
		} else {
			// MERGE v10: aggiunte casistiche per gli uffici minori
			if ((getRequestStringParameter("CodContenutoHidden").compareTo("U019") == 0
					&& lCodTipoUfficioMittente.compareTo("UDS") != 0
					&& lCodTipoUfficioMittente.compareTo("UDSM") != 0)
					|| (getRequestStringParameter("CodContenutoHidden").compareTo("U004") == 0
							&& (lCodTipoUfficioMittente.compareTo("TDS") != 0
									&& lCodTipoUfficioMittente.compareTo("TDSM") != 0
									&& lCodTipoUfficioMittente.compareTo("UDS") != 0
									&& lCodTipoUfficioMittente.compareTo("UDSM") != 0
									&& lCodTipoUfficioMittente.compareTo("-") != 0)))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Ufficio Mittente non valido per il contenuto selezionato");
		}

		// Caricamento Fascicolo SIUS (solo dati modificati)
		lFasGPMod.getFascicoloSiusModel().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																								// dell'operatore
																								// che
																								// inserisce
		lFasGPMod.getFascicoloSiusModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																										// dell'operatore
																										// che
																										// inserisce
		lFasGPMod.getFascicoloSiusModel().setDataAggiornamento(DateUtils.getSysDate());

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_SEDE_MITTENTE)));

		// Caricamento Generale Procedimento
		lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(new String(lComMod.getCodComune()));
		// STUB 05/05/2004 Per la modifica fascicolo da UDS può essere utilizzato un campo HIDDEN.
		// STUB 14/12/2004 Per la modifica G.P. di una E.M.A. padre, non devo modificare AnnoS1 e ProgrS1
		// (impostati a null).
		if (isRequestParameterNullObj(CAMPO_COD_CONTENUTO)) {
			lFasGPMod.getGeneraleProcedimentoModel()
					.setCodOggettoProcedimento(getRequestStringParameter("CodContenutoHidden"));
			lFasGPMod.getGeneraleProcedimentoModel().setAnnoS1(null);
			lFasGPMod.getGeneraleProcedimentoModel().setProgrS1(null);
		} else
			lFasGPMod.getGeneraleProcedimentoModel()
					.setCodOggettoProcedimento(getRequestStringParameter(CAMPO_COD_CONTENUTO));

		lFasGPMod.getGeneraleProcedimentoModel().setDataRichiesta(
				getRequestDateParameter(CAMPO_ANNO_DATA_ATTO, CAMPO_MESE_DATA_ATTO, CAMPO_GIORNO_DATA_ATTO));
		lFasGPMod.getGeneraleProcedimentoModel().setDataArrivoCancelleria(getRequestDateParameter(
				CAMPO_ANNO_DATA_ARRIVO, CAMPO_MESE_DATA_ARRIVO, CAMPO_GIORNO_DATA_ARRIVO));
		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodTipoAtto(getRequestStringParameter(CAMPO_COD_TIPO_ATTO));
		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodTipoMittenteAtto(getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
		if ((getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE)).equalsIgnoreCase(""))
			lFasGPMod.getGeneraleProcedimentoModel()
					.setCodSedeMittente(super.getCodComuneByDescr("-").getCodComune());
		else
			lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(
					super.getCodComuneByDescr(getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE))
							.getCodComune());
		lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(
				super.getCodComuneByDescr(getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE))
						.getCodComune());
		lFasGPMod.getGeneraleProcedimentoModel().setAnnotazione(getRequestStringParameter(CAMPO_NOTE));
		// Codice dell'ufficio dell'operatore che inserisce
		lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		// Codice dell'operatore che inserisce
		lFasGPMod.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lFasGPMod.getGeneraleProcedimentoModel().setDataAggiornamento(DateUtils.getSysDate());
		lFasGPMod.getGeneraleProcedimentoModel().setDataFinePena(
				getRequestDateParameter(CAMPO_ANNO_FINE_PENA, CAMPO_MESE_FINE_PENA, CAMPO_GIORNO_FINE_PENA));
		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodPosGiuridica(getRequestStringParameter(CAMPO_COD_POS_GIURIDICA));
		// 14/01/2004 Impostazione della Descrizione del Mittente.
		lFasGPMod.getGeneraleProcedimentoModel()
				.setDescrMittente(getRequestStringParameter(CAMPO_DESCR_MITTENTE));
		// 03/01/2005 Impostazione del Tipo Registro.
		if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).length() > 0)
			lFasGPMod.getGeneraleProcedimentoModel()
					.setCodTipoRegistro(getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO));

		// Caricamento Tenore
		// Preleva dalla request i codici e descrizioni dei tenori, impipati rispettivamente con separatore
		// "|" e "\n".
		// Stabilisce la size dell'Array di Tenori da caricare in FascicoloGpModel.
		// String lCodOgg = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO);
		StringTokenizer lCodOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO), "|");
		StringTokenizer lDescrOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO), "\n");
		// STUB 12/11/2003 Aggiunti i Codici Dettaglio Oggetti.
		String lStCodiceDet = new String(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO));

		int lSizeVector = lCodOggetto.countTokens();
		TenoreModel lTenori[] = new TenoreModel[lSizeVector];

		int lIndex = 0;

		while (lCodOggetto.hasMoreTokens()) {
			TenoreModel lTenModel = new TenoreModel();

			lTenModel.setCodOggettoTenore(lCodOggetto.nextToken());
			lTenModel.setDescrOggettoTenore(lDescrOggetto.nextToken());
			lTenModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																				// dell'operatore che
																				// inserisce
			lTenModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che
																			// inserisce
			lTenModel.setDataInserimento(DateUtils.getSysDate());
			lTenModel
					.setCodMagistrato(getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO));
			lTenModel.setProgrTenore(new BigDecimal((double) (lIndex + 1)));
			lTenModel.setCodEsitoTenore("-");
			// Il campo Id_Generale_Procedimento di Tenore viene impostato nel controller

			// 12/11/2003 Aggiunta la valorizzazione dell'eventuale Dettaglio Oggetto.
			if ((lStCodiceDet).indexOf(lTenModel.getCodOggettoTenore() + "0") < 0) {
				lTenModel.setCodDettaglioOggetto("-");
			} else {
				String lCodDettaglioCorrente = lStCodiceDet.substring(
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 4,
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 8);
				lTenModel.setCodDettaglioOggetto(lCodDettaglioCorrente);
			}

			// MEV_39: modifica al codice per oggetto tenore 2422 --> 24221204|
			if ("2422".equals(lTenModel.getCodOggettoTenore())) {
				if (!lStCodiceDet.startsWith(lTenModel.getCodOggettoTenore()))
					lTenModel.setCodDettaglioOggetto("-");
				else
					lTenModel.setCodDettaglioOggetto(lStCodiceDet.substring(4, 8));
			}

			// Setto l'Array su GPtenoreModel
			lTenori[lIndex] = lTenModel;
			lIndex++;
		}
		lFasGPMod.setTenori(lTenori);

		// MEV_66: aggiunto codice per salvare coppia anno/numero S22 come in inserimento
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_S22)
				&& !isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_S22)) {
			if (getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null
					&& getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22) != null) {
				lFasGPMod.getGeneraleProcedimentoModel()
						.setAnnoS1(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22));
				lFasGPMod.getGeneraleProcedimentoModel()
						.setProgrS1(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22));
			}
		} else
			lFasGPMod.getGeneraleProcedimentoModel().setAnnoS1(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// FINE MEV_66

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasGPMod = lCtrl.ExModificaFascicoloSius(lFasGPMod);

		// STUB 13/12/2004 Caricamento Esecuzione Misura Alternativa.
		// BigDecimal lIdEsecuzioneMA = null;
		if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") == 0) {
			if (!isRequestParameterNullObj(ICostantiEsecuzioneMA.CAMPO_ID_ESECUZIONE_MA)
					&& getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_ESECUZIONE_MA).length() > 4) {
				IEsecuzioneMA lEMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();
				EsecuzioneMisuraAlternativaModel lEMAModel = lEMACtrl
						.ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(
								lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

				if (lSizeVector > 0)
					lEMAModel.setCodTipoMisura(lTenori[0].getCodOggettoTenore());
				lEMAModel.setDataOrdinanza(getRequestDateParameter(CAMPO_ANNO_DATA_ATTO, CAMPO_MESE_DATA_ATTO,
						CAMPO_GIORNO_DATA_ATTO));
				// Valorizzazione del codice Ufficio
				// Collection lMittenti = DecodificheManager.getInstance().getMittenteAtto();
				// String lDescrMittente = DecodificheUtils.getDescbyCode(lMittenti,
				// getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
				// Collection lUffici = DecodificheManager.getInstance().getTipoUfficio();

				if (lCodTipoUfficioMittente.compareTo("-") == 0)
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Ufficio Mittente non valido per la Misura Alternativa");

				lEMAModel.setCodAutoritaEmittOrd(getCodUfficioByCodTipoUfficioDescrComune(
						lCodTipoUfficioMittente, getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE)));

				if (lEMAModel.getDepOpidDepositoOrdinanzaPc() == null
						&& getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null
						&& getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22) != null) {
					lEMAModel.setAnnoS07(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22));
					lEMAModel.setProgrS07(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22));
				}
				lEMAModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lEMAModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lEMAModel.setDataAggiornamento(DateUtils.getSysDate());
				lEMAModel = lEMACtrl.ExModificaEMAbyFascicolo(lEMAModel);
			}
		}

		// STUB 30/07/2007 Caricamento Esecuzione Sanzione Sostitutiva.
		// BigDecimal lIdEsecuzioneSS = null;
		if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U019") == 0
				// MEV_2023-35 aggiunta gestione U126 EPS
				|| lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U126") == 0) {
			if (!isRequestParameterNullObj(ICostantiEsecuzioneSS.CAMPO_ID_ESECUZIONE_SS)
					&& getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_ESECUZIONE_SS).length() > 4) {
				IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
				EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl
						.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(
								lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				if (lSizeVector > 0)
					lESSModel.setCodTipoSanzione(lTenori[0].getCodOggettoTenore());
				lESSModel.setDataOrdinanza(getRequestDateParameter(CAMPO_ANNO_DATA_ATTO, CAMPO_MESE_DATA_ATTO,
						CAMPO_GIORNO_DATA_ATTO));
				// Valorizzazione del codice Ufficio
				Collection lMittenti = DecodificheManager.getInstance().getMittenteAtto();
				String lDescrMittente = DecodificheUtils.getDescbyCode(lMittenti,
						getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
				Collection lUffici = DecodificheManager.getInstance().getTipoUfficio();
				lCodTipoUfficioMittente = DecodificheUtils.getCodebyDesc(lUffici, lDescrMittente);
				if (lCodTipoUfficioMittente.compareTo("-") == 0) {
					if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo("U019") == 0)
						throw new SIUSException(SIUSException.USER_MESSAGE,
								"Ufficio Mittente non valido per la Sanzione Sostitutiva");
					// MEV_2023-35 aggiunta gestione U126 EPS
					else if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo("U126") == 0)
						throw new SIUSException(SIUSException.USER_MESSAGE,
								"Ufficio Mittente non valido per la Pena Sostitutiva");
				}

				lESSModel.setCodAutoritaEmittOrd(getCodUfficioByCodTipoUfficioDescrComune(
						lCodTipoUfficioMittente, getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE)));

				if (lESSModel.getDepOpidDepositoOrdinanzaPc() == null
						&& getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null
						&& getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22) != null) {
					lESSModel.setAnnoS07(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22));
					lESSModel.setProgrS07(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22));
				}
				lESSModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lESSModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lESSModel.setDataAggiornamento(DateUtils.getSysDate());
				lESSModel = lESSCtrl.ExModificaESSbyFascicolo(lESSModel);
			}
		}

		// 02/05/2011 Caricamento Esecuzione Misura Sicurezza.
		// BigDecimal lIdEsecuzioneMS = null;
		if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U024") == 0) {
			if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS)
					&& getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS).length() > 4) {
				IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
				EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl
						.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(
								lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

				if (lSizeVector > 0)
					lEMSModel.setCodTipoMisura(lTenori[0].getCodOggettoTenore());
				lEMSModel.setDataOrdinanza(getRequestDateParameter(CAMPO_ANNO_DATA_ATTO, CAMPO_MESE_DATA_ATTO,
						CAMPO_GIORNO_DATA_ATTO));
				// Valorizzazione del codice Ufficio
				Collection lMittenti = DecodificheManager.getInstance().getMittenteAtto();
				String lDescrMittente = DecodificheUtils.getDescbyCode(lMittenti,
						getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
				Collection lUffici = DecodificheManager.getInstance().getTipoUfficio();
				lCodTipoUfficioMittente = DecodificheUtils.getCodebyDesc(lUffici, lDescrMittente);

				// Modifica del 10/09/2013 mev "Revisione Misure di Sicurezza SIUS"
				// Eliminato controllo su ufficio mittente se il campo non è valorizzato
				// nel caso di "Iscrizione di una Esecuzione Misura di Sicurezza"
				// if (lCodTipoUfficioMittente.compareTo("-")==0)
				// throw new SIUSException( SIUSException.USER_MESSAGE,
				// "Ufficio Mittente non valido per la Misura Sicurezza" );
				if (!lCodTipoUfficioMittente.equals("-")) {
					lEMSModel.setCodAutoritaEmittOrd(getCodUfficioByCodTipoUfficioDescrComune(
							lCodTipoUfficioMittente, getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE)));
				}

				if (lEMSModel.getDepOpidDepositoOrdinanzaPc() == null
						&& getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null
						&& getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22) != null) {
					lEMSModel.setAnnoS07(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22));
					lEMSModel.setProgrS07(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22));
				}
				lEMSModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lEMSModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lEMSModel.setDataAggiornamento(DateUtils.getSysDate());
				lEMSModel = lEMSCtrl.ExModificaEMSbyFascicolo(lEMSModel);
			}
		}

		// restituisce la jsp di VIEW
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIUS + "="
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
	}

	/**
	 * MEV10-s3: aggiunto metodo per gestire passaggio alla maggiore età del soggetto
	 * 
	 * @param lFasGPMod
	 * @param lUtenteMod
	 * @throws Exception
	 */
	private void oscuraProcessRequest(FascicoloGPModel lFasGPMod, UtenteModel lUtenteMod) throws Exception {

		lFasGPMod.getFascicoloSiusModel()
				.setIdFascicoloSius(getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS));
		lFasGPMod.getFascicoloSiusModel().setVisibilitaMinorenne("N");
		lFasGPMod.getFascicoloSiusModel().setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lFasGPMod.getFascicoloSiusModel().setDataAggiornamento(DateUtils.getSysDate());
		lFasGPMod.getFascicoloSiusModel()
				.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lCtrl.ExModificaVisibilitaMinoreFascicoloSius(lFasGPMod);
	}

}