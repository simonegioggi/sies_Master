package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per l' Inserimento/Modifica della decisione del G.E. a fronte di una Richiesta del P.M. di
 * apllicazione Benefici
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciDecisioneDelGECumulo extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lOperazione = "";
		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		String[] lIdMisureSelezionate = null; // MISURA_SICUREZZA_CUMULO
		String[] lIdPenaAccSelezionate = null; // PENA_ACCESSORIA_CUMULO

		BigDecimal aIdRich = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);

		RichiestePmInCumuloModel lRicMod = null;
		ProvvedimentoGeSorvCumModel lProvvMod = new ProvvedimentoGeSorvCumModel();
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();

		if (!"C".equals(lModalita)) {
			// Inserimento/Modifica
			lRicMod = ICtrlRic.ExRicercaRichiestePmInCumuloById(aIdRich);
			siesLogger.debug("--XX-- Inserimento/Modifica Decisione del G.E. su Richiesta di >"
					+ lRicMod.getDescrTipoAnnotazione() + "<");
			lOperazione = lRicMod.getCodTipoAnnotazione();

			// Ordinanza = 03
			lProvvMod.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));

			// Flag_Conforme : Conformità, Difformità, Rigetto, Inammissibilità
			lProvvMod.setFlagConforme(getRequestStringParameter(CAMPO_FLAG_CONFORME));

			if (!isRequestParameterNullObj(CAMPO_MOTIVAZIONI_D)) {
				lProvvMod.setMotivazioniD(getRequestStringParameter(CAMPO_MOTIVAZIONI_D));
			}

			if (!isRequestParameterNullObj(CAMPO_FLAG_PIU_MENO_D)) {
				lProvvMod.setFlagPiuMenoD(getRequestStringParameter(CAMPO_FLAG_PIU_MENO_D));

				// Reclusione e Multa
				lProvvMod.setNumAnniReclusioneD(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE_D));
				lProvvMod.setNumMesiReclusioneD(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE_D));
				lProvvMod.setNumGiorniReclusioneD(
						getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE_D));

				if ((getRequestStringParameter(CAMPO_IMPORTO_MULTA_D + "INT") != null
						&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA_D + "INT")).equals(""))
						|| (getRequestStringParameter(CAMPO_IMPORTO_MULTA_D + "DEC") != null
								&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA_D + "DEC")).equals(""))) {
					lProvvMod.setImportoMultaD(
							new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA_D + "INT") + "."
									+ getRequestStringParameter(CAMPO_IMPORTO_MULTA_D + "DEC")));
				}

				// Arresto e Ammenda
				lProvvMod.setNumAnniArrestoD(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ARRESTO_D));
				lProvvMod.setNumMesiArrestoD(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ARRESTO_D));
				lProvvMod.setNumGiorniArrestoD(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ARRESTO_D));

				if ((getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_D + "INT") != null
						&& !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_D + "INT")).equals(""))
						|| (getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_D + "DEC") != null
								&& !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_D + "DEC"))
										.equals(""))) {
					lProvvMod.setImportoAmmendaD(
							new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_D + "INT") + "."
									+ getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_D + "DEC")));
				}

			}

			lProvvMod.setDataD(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_D,
					CAMPO_MESE_DATA_EMISSIONE_D, CAMPO_GIORNO_DATA_EMISSIONE_D));
			lProvvMod.setAnnoProvv(getRequestBigDecimalParameter(CAMPO_ANNO_PROVV));
			lProvvMod.setNumeroProvv(getRequestStringParameter(CAMPO_NUMERO_PROVV));

			String lCodTipoUfficio = getRequestStringParameter(CAMPO_COD_UFFICIO_EMITTENTE);
			String lComune = getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE);

			String lCodUfficio = getUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lComune)
					.getCodUfficio();
			ComuneModel lComuneMod = getCodComuneByDescr(lComune);

			lProvvMod.setCodUfficioEmittente(lCodUfficio);
			lProvvMod.setCodLuogoEmittente(lComuneMod.getCodComune());

			lProvvMod.setRicIdRichiestePmInCumulo(lRicMod.getIdRichiestePmInCumulo());

			// ======================================================================================================
			// operazioni di set per la Decisione su Revoca Benefici
			if (!isRequestParameterNullObj(CAMPO_CHECK_REVOCA_BEN_1)) {
				if (getRequestStringParameter(CAMPO_CHECK_REVOCA_BEN_1).equals("S"))
					lProvvMod.setBenSospCond("S");
				else if (getRequestStringParameter(CAMPO_CHECK_REVOCA_BEN_1).equals("N"))
					lProvvMod.setBenNonMenzione("N");
				else if (getRequestStringParameter(CAMPO_CHECK_REVOCA_BEN_1).equals("I"))
					lProvvMod.setBenIndulto("I");
				else if (getRequestStringParameter(CAMPO_CHECK_REVOCA_BEN_1).equals("A"))
					lProvvMod.setBenIndulto("A");
			}

			if (!isRequestParameterNullObj(CAMPO_CHECK_REVOCA_BEN_2)) {
				if (getRequestStringParameter(CAMPO_CHECK_REVOCA_BEN_2).equals("N"))
					lProvvMod.setBenNonMenzione("N");
			}

			// ======================================================================================================
			// operazioni di set per la Decisione su Sostituzione P.A.
			if ("024".equals(lOperazione) || "025".equals(lOperazione)) {
				if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PENA_ACCESSORIA)) {
					lProvvMod.setCodTipoPenaAccessoriaD(
							getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA));
					lProvvMod.setCodTipoDurataPaD(getRequestStringParameter(CAMPO_COD_TIPO_DURATA_PA));

					lProvvMod.setNumAnniPaD(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_PA));
					lProvvMod.setNumMesiPaD(getRequestBigDecimalParameter(CAMPO_NUM_MESI_PA));
					lProvvMod.setNumGiorniPaD(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PA));

					// lProvvMod.setMotivazioniD(getRequestStringParameter( CAMPO_MOTIVAZIONI_D) );
				}
			}

			// ======================================================================================================
			// operazioni di set per la Decisione su Applicazioni P.A.
			if ("029".equals(lOperazione) || "030".equals(lOperazione)) {
				if (getRequestStringParameter(CAMPO_FLAG_CONFORME).equals("C")
						|| getRequestStringParameter(CAMPO_FLAG_CONFORME).equals("D")) {
					lProvvMod.setCodTipoPenaAccessoriaD(
							getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA));
					lProvvMod.setCodTipoDurataPaD(getRequestStringParameter(CAMPO_COD_TIPO_DURATA_PA));

					lProvvMod.setNumAnniPaD(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_PA));
					lProvvMod.setNumMesiPaD(getRequestBigDecimalParameter(CAMPO_NUM_MESI_PA));
					lProvvMod.setNumGiorniPaD(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PA));
				}

			}

			// ======================================================================================================
			// operazioni di set per la Decisione su Applicazioni Benefici

			if (!isRequestParameterNullObj("idMisuraSel"))
				lIdMisureSelezionate = getRequestStringParameters("idMisuraSel");

			if (!isRequestParameterNullObj("idPenaAccSel"))
				lIdPenaAccSelezionate = getRequestStringParameters("idPenaAccSel");

			if ("002".equals(lOperazione) || "003".equals(lOperazione)) {

			}
			//
			// ==============================================================================
			//
			lRicMod.setDecisioneGeSorvCum(lProvvMod);

			if (!isRequestParameterNullObj(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO))
				lRicMod.setIstrIdIstruttoriaCumulo(
						getRequestBigDecimalParameter(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO));

			// Richieste_Pm_In_Cumulo: viene comunque effettuata una UPDATE
			lRicMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lRicMod.setDataAggiornamento(DateUtils.getSysDate());
			lRicMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			// siesLogger.debug("INSERIMENTO/MODIFICA lRichModel = "+lRicMod);
			// siesLogger.debug("INSERIMENTO/MODIFICA lProvvMod = "+lProvvMod);
		}

		// NON DOVREBBE MAI VERIFICARSI; La Action che porta alla Cancellazione è un'altra !!!
		if ("C".equals(lModalita)) {
			// Cancellazione
			ICtrlRic.ExCancellaRichiestePmInCumuloFull(aIdRich);
		} else {
			// Inserimento / Modifica
			ICtrlRic.ExModificaRichiestaEProvvPmInCumulo(lRicMod, lIdMisureSelezionate,
					lIdPenaAccSelezionate);
		}

		// ==========================================================================
		//
		// ==========================================================================
		String lPage = "";
		if (!"C".equals(lModalita)) {
			if ("021".equals(lOperazione)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaBenefici";
				lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "=" + aIdRich.toString();
			} else if ("004".equals(lOperazione) || "013".equals(lOperazione) || "017".equals(lOperazione)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaPenaPrincCum";
				lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "=" + aIdRich.toString();
			} else if ("002".equals(lOperazione) || "003".equals(lOperazione)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGEBenefici";
				lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "=" + aIdRich.toString();
			} else if ("023".equals(lOperazione)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaSSCum";
				lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "=" + aIdRich.toString();
			} else if ("024".equals(lOperazione) || "025".equals(lOperazione)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGESostPenaAcc";
				lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "=" + aIdRich.toString();
			} else if ("026".equals(lOperazione) || "027".equals(lOperazione) || "028".equals(lOperazione)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaPenaAcc";
				lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "=" + aIdRich.toString();
			} else if ("029".equals(lOperazione) || "030".equals(lOperazione)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGEApplicaPenaAccCum";
				lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "=" + aIdRich.toString();
			} else if ("014".equals(lOperazione)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGEAltro";
				lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "=" + aIdRich.toString();
			}
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE";
		}

		return lPage;
	}

} // Chiude classe