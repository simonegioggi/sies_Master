package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che effettua l'inserimento dei dati del provvedimento di Scomputo Permessi (Titolo Cumulato)
 */
public class ActInserisciScomputoPermessiCumulo extends ActionModuloCumulo
		implements ICostantiLibAnticipataCumulo, ICostantiStatoEsecTitoloCumulato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		siesLogger.debug("--XX-- INIZIO ActInserisciScomputoPermessiCumulo ");

		StatoEsecTitoloCumulatoModel lStatoEsec = new StatoEsecTitoloCumulatoModel();

		String lModalita = "";
		lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella, NP=nuovo
															// Periodo

		String lStato = "I";
		if (lModalita.equals("M")) {
			// Cambia stato solo se il dato NON è Iscritto manualmente
			if (getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("E")
					|| getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("M")) {
				lStato = "M";
			}
		}

		// ===============================================================================================
		// Stato_Esecuzione_Titolo_Cumulato
		// ================================================================================================
		if (!lModalita.equals("C")) {
			String lCodTipoUfficio = getRequestStringParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE);
			String lDescrComune = getRequestStringParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE);
			String lCodUfficio = getUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComune)
					.getCodUfficio();
			ComuneModel lComune = getCodComuneByDescr(lDescrComune);

			if ("M".equals(lModalita)) {
				siesLogger.debug(" ----- >  StatoEsec - modifica ");
				lStatoEsec.setIdStatoEsecTitoloCumulato(
						getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO));

				lStatoEsec.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lStatoEsec.setDataAggiornamento(DateUtils.getSysDate());
				lStatoEsec.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			} else if ("I".equals(lModalita)) {
				lStatoEsec.setCodOperatoreInserimento(getCodUtenteConnesso());
				lStatoEsec.setDataInserimento(DateUtils.getSysDate());
				lStatoEsec.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			}

			lStatoEsec.setCodUfficioEmittente(lCodUfficio);
			lStatoEsec.setCodLuogoEmittente(lComune.getCodComune());

			lStatoEsec.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,
					CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE));

			lStatoEsec.setAnnoProcedimento(getRequestBigDecimalParameter(CAMPO_ANNO_PROCEDIMENTO));
			lStatoEsec.setProgrProcedimento(getRequestBigDecimalParameter(CAMPO_PROGR_PROCEDIMENTO));
			lStatoEsec.setAnnoProvvedimento(getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
			lStatoEsec.setProgrProvvedimento(getRequestBigDecimalParameter(CAMPO_PROGR_PROVVEDIMENTO));

			lStatoEsec.setCodTipoEvento("01"); // 01 = Provvedimento.
			lStatoEsec.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO)); // 02
																											// =
																											// Decreto
																											// ;
																											// 03
																											// =
																											// Ordinanza.

			lStatoEsec.setCodEsitoTenore("-");

			lStatoEsec.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));
			if (lStatoEsec.getCodMotivo().equals("2250")) // Scomputo Permesso
			{
				lStatoEsec.setCodEsito("0013"); // //Dichiara Non Validamente Espiata la Pena (si deve
												// aggiungere pena)
			} else if (lStatoEsec.getCodMotivo().equals("0039")) // Reclamo Scomputo Permesso
			{
				lStatoEsec.setCodEsito("0028"); // Accoglie il Reclamo dell'interessato/difensore (si deve
												// togliere pena)
			}

			if (getRequestStringParameter(CAMPO_NOTE) != null
					&& !getRequestStringParameter(CAMPO_NOTE).equals(""))
				lStatoEsec.setNote(getRequestStringParameter(CAMPO_NOTE));
			else
				lStatoEsec.setNote("");

			lStatoEsec.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
			lStatoEsec.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
			lStatoEsec.setFlagStato(lStato);
			lStatoEsec.setMotivoModifica(null);
		}
		// ------------------------------------------------------------------------------------------------------------------------------------
		Vector<LibAnticipataCumuloModel> VecLiberazioni = new Vector<>();
		LibAnticipataCumuloModel LibAntMod = null;

		// =========================================================================
		// Gestione dei dati dei GG di Scomputo Permessi
		// ===========================================================================
		if (!lModalita.equals("C"))// Casi di INSERIMENTO e MODIFICA
		{
			LibAntMod = new LibAnticipataCumuloModel();

			LibAntMod.setNumeroGiorni(new BigDecimal(
					this.getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUMERO_GIORNI)));

			if (lStatoEsec.getCodMotivo().equals("2250")) // Scomputo Permesso
			{
				LibAntMod.setCodTipoLicenza("PP");
				LibAntMod.setFlagConcesso("S"); // si deve aggiungere pena
			} else if (lStatoEsec.getCodMotivo().equals("0039")) // Reclamo Scomputo Permesso
			{
				LibAntMod.setCodTipoLicenza("EP");
				LibAntMod.setFlagConcesso("C"); // Si deve togliere pena
			}

			LibAntMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			LibAntMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			LibAntMod.setDataInserimento(DateUtils.getSysDate());

			// In caso di MODIFICA inserisco volutamente Attributi Ins e Mod
			if ("M".equals(lModalita)) {
				LibAntMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				LibAntMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				LibAntMod.setDataAggiornamento(DateUtils.getSysDate());
			}

			LibAntMod.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
			LibAntMod.setFlagStato(lStato);
			LibAntMod.setMotivoModifica(null);

			VecLiberazioni.add(LibAntMod);
		}

		// ===================================
		// Effettuo l'inserimento dei dati
		// ===================================

		IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		BigDecimal IdStatoEsec = null;

		if ("I".equals(lModalita)) {
			IdStatoEsec = lCtrlStatoEsec.ExInserisciRimediRisarcitoriCumulo(lStatoEsec, VecLiberazioni);
		} else if ("M".equals(lModalita)) {
			IdStatoEsec = lCtrlStatoEsec.ExModificaRimediRisarcitoriCumulo(lStatoEsec, VecLiberazioni);
		} else if ("C".equals(lModalita)) {
			lCtrlStatoEsec.ExCancellaStatoEsecTitoloCumulatoById(
					getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO), null);
		}

		String lPage = "";

		if ("I".equals(lModalita) || "M".equals(lModalita)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioScomputoPermessiCumulo" + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
					+ "&" + ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ IdStatoEsec;
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActRicercaScomputoPermessiCumulo" + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		}

		return lPage;

		// if (1==1)
		// throw new F3BException(F3BException.USER_MESSAGE, " FATTO ");

	}

	/**
	 * Recupera dalla form in dati dei periodi per il singolo computo
	 *
	 * @param aNomeDiv
	 * @return
	 * @throws Exception
	 */
	// private Vector<PeriodoLibAntCumuloModel> recuperaPeriodi(String aNomeDiv, String aModalita, String
	// aStato)
	// throws Exception {
	// // siesLogger.debug("recuperaPeriodi per Nome Div = "+aNomeDiv);
	//
	// int lNumPeriodi = getRequestIntParameter("NumPeriodi");
	//
	// Vector<PeriodoLibAntCumuloModel> lVectPeriodi = new Vector<>();
	//
	// // Recupero i periodi inseriti in form
	// for (int i = 0; i < lNumPeriodi; i++) {
	// String lNomeCampoGiornoDa = ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO + "_" + aNomeDiv
	// + "_" + i;
	//
	// if (!isRequestParameterNullObj(lNomeCampoGiornoDa)
	// && getRequestStringParameter(lNomeCampoGiornoDa).length() > 0) {
	// PeriodoLibAntCumuloModel lPeriodoLib = new PeriodoLibAntCumuloModel();
	// Date lDataInizio = getRequestDateParameter(
	// ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO + "_" + aNomeDiv + "_" + i,
	// ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO + "_" + aNomeDiv + "_" + i,
	// ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO + "_" + aNomeDiv + "_" + i);
	//
	// Date lDataFine = getRequestDateParameter(
	// ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE + "_" + aNomeDiv + "_" + i,
	// ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE + "_" + aNomeDiv + "_" + i,
	// ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE + "_" + aNomeDiv + "_" + i);
	// //
	// lPeriodoLib.setDataInizio(lDataInizio);
	// lPeriodoLib.setDataFine(lDataFine);
	//
	// lPeriodoLib.setFlagStato(aStato);
	// lPeriodoLib.setMotivoModifica(null);
	//
	// lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
	// lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	// lPeriodoLib.setDataInserimento(DateUtils.getSysDate());
	//
	// if ("M".equals(aModalita)) {
	// lPeriodoLib.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	// lPeriodoLib.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
	// lPeriodoLib.setDataAggiornamento(DateUtils.getSysDate());
	// }
	//
	// lVectPeriodi.add(lPeriodoLib);
	// }
	// }
	//
	// return lVectPeriodi;
	// }

}