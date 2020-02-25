package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un provedimento di pagamento PP
 * disposto su uno dei titoli cumulati
 * 
 * @author
 *
 */
public class ActInserisciPagamentoPP extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		// ==========================================================================
		//
		//
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActRicercaPagamentiPP" + "&"
				+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
				+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) + "&"
				+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
				+ this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		String lModalita = "";
		lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella

		IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		if ("I".equals(lModalita)) {
			siesLogger.debug("Sono in INSERIMENTO");

			StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("I");

			// Si carica in StatoEsecuzione l'ufficio emittente
			if (super.getDatiTitoloCumulato().getProcedimentoCumulato() != null) {
				lStatoEsecMod.setCodUfficioEmittente(
						super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato());
				lStatoEsecMod.setCodLuogoEmittente(super.getDatiTitoloCumulato().getProcedimentoCumulato()
						.getCodLuogoUfficioFasCumulato());
			}

			siesLogger.debug("lStatoEsecMod = " + lStatoEsecMod);

			Vector<ComputiCumuloModel> lListaComputi = this.getDatiComputo("I");
			siesLogger.debug("lListaComputi = " + lListaComputi);

			lStatoEsecMod = lCtrlStatoEsec.ExInserisciPagamentiPP(lStatoEsecMod, lListaComputi);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioPagamentoPP" + "&"
					+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato() + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		} else if ("M".equals(lModalita)) {
			siesLogger.debug("Sono in MODIFICA");

			StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("M");
			siesLogger.debug("lStatoEsecMod = " + lStatoEsecMod);

			// Si carica in StatoEsecuzione l'ufficio emittente
			if (super.getDatiTitoloCumulato().getProcedimentoCumulato() != null) {
				lStatoEsecMod.setCodUfficioEmittente(
						super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato());
				lStatoEsecMod.setCodLuogoEmittente(super.getDatiTitoloCumulato().getProcedimentoCumulato()
						.getCodLuogoUfficioFasCumulato());
			}
			Vector<ComputiCumuloModel> lListaComputi = this.getDatiComputo("M");
			// ComputiCumuloModel lComputo = lListaComputi.elementAt(0);
			siesLogger.debug("lListaComputi = " + lListaComputi);

			for (int ii = 0; ii < lListaComputi.size(); ii++) {
				lListaComputi.get(ii).setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());
			}

			lCtrlStatoEsec.ExModificaStatoEsecComputiCumulo(lStatoEsecMod, lListaComputi);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioPagamentoPP" + "&"
					+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato() + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		} else if ("NP".equals(lModalita)) {
			siesLogger.debug("Sono in Aggiunta Nuovo Periodo");

			StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("M");
			siesLogger.debug("lStatoEsecMod = " + lStatoEsecMod);

			// Si carica in StatoEsecuzione l'ufficio emittente
			if (super.getDatiTitoloCumulato().getProcedimentoCumulato() != null) {
				lStatoEsecMod.setCodUfficioEmittente(
						super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato());
				lStatoEsecMod.setCodLuogoEmittente(super.getDatiTitoloCumulato().getProcedimentoCumulato()
						.getCodLuogoUfficioFasCumulato());
			}

			Vector<ComputiCumuloModel> lComputiModel = this.getDatiComputo("M");
			siesLogger.debug("lComputiModel = " + lComputiModel);
			lComputiModel.get(0).setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());

			lCtrlStatoEsec.ExModificaStatoEsecComputoCumulo(lStatoEsecMod, lComputiModel.get(0));

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioPagamentoPP" + "&"
					+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato() + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		} else if ("C".equals(lModalita)) {
			siesLogger.debug("Sono in CANCELLAZIONE");

			BigDecimal lIdStatoEsecuzione = getRequestBigDecimalParameter(
					CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
			BigDecimal lIdComputo = getRequestBigDecimalParameter(CAMPO_ID_COMPUTI_CUMULO);

			// Devo verificare se cancellare un solo computo o l'intero provvedimento
			IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			StatoEsecTitoloCumulatoModel lStato = lCtrlStato
					.ExRicercaStatoEsecTitoloCumulatoByIdFull(lIdStatoEsecuzione);

			if (lStato.getListaComputi().size() == 1) {
				// Un solo computro, cancello tutto
				siesLogger.debug("Elimino l'intero provvedimento di computo: " + lIdStatoEsecuzione);
				lCtrlStatoEsec.ExCancellaStatoEsecTitoloCumulatoById(lIdStatoEsecuzione, null);
			} else {
				// Ho più computi cancello solo quello indicato sulla request
				siesLogger.debug("Elimino il solo conmputo:" + lIdComputo);
				IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
				lCtrlComputi.ExCancellaComputiCumuloBykey(lIdComputo);
			}
		}

		return lPage;
	}

	/**
	 * 
	 * @throws F3BException
	 */
	private StatoEsecTitoloCumulatoModel getDatiProvvedimento(String aTipoOper) throws F3BException {
		StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();

		lStaMod.setIdStatoEsecTitoloCumulato(
				getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO));
		lStaMod.setCodTipoEvento("01");
		lStaMod.setCodTipoProvvedimento("25");
		lStaMod.setCodMotivo("1007"); // Annotazione Avvenuto Pagamento PP
		// lStaMod.setCodUfficioEmittente ( getRequestStringParameter ( CAMPO_COD_UFFICIO_EMITTENTE) );
		// lStaMod.setCodLuogoEmittente ( getRequestStringParameter ( CAMPO_COD_LUOGO_EMITTENTE) );
		lStaMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		lStaMod.setCodEsito("-");
		lStaMod.setCodEsitoTenore("-");
		lStaMod.setAnnoProcedimento(null);
		lStaMod.setProgrProcedimento(null);
		lStaMod.setAnnoProvvedimento(null);
		lStaMod.setProgrProvvedimento(null);

		if (!isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE))
			lStaMod.setNote(getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE));

		lStaMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		lStaMod.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
		lStaMod.setFlagStato("I");
		lStaMod.setMotivoModifica(null);

		if ("I".equals(aTipoOper)) {
			lStaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lStaMod.setDataInserimento(DateUtils.getSysDate());
			lStaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		} else if ("M".equals(aTipoOper)) {
			// La Modifica cambia solo lo stato di "Estratto / Modificato".
			String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO);
			if ("E".equals(flagStato) || "M".equals(flagStato))
				lStaMod.setFlagStato("M");
			lStaMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lStaMod.setDataAggiornamento(DateUtils.getSysDate());
			lStaMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		}

		return lStaMod;
	}

	/**
	 * Recupera i dati dei computi cumulo
	 * 
	 * @return
	 * @throws F3BException
	 */
	private Vector<ComputiCumuloModel> getDatiComputo(String aTipoOper) throws F3BException {
		// ==============================================================================
		// Recupero i dati delle annotazioni Pagamenti Pene Pecuniaria
		// ==============================================================================
		Vector<ComputiCumuloModel> lListaComputi = new Vector<>();

		if (!this.isRequestParameterNullObj("maxNumComputi")) {
			int maxNumComputi = getRequestIntParameter("maxNumComputi");
			for (int i = 0; i < maxNumComputi; i++) {
				if ((getRequestStringParameter("Multa_" + i).trim().length() != 0
						|| getRequestStringParameter("Mul_dec_" + i).trim().length() != 0
						|| getRequestStringParameter("Ammenda_" + i).trim().length() != 0
						|| getRequestStringParameter("Amm_dec_" + i).trim().length() != 0)) {
					// siesLogger.debug(" Inserisco l'elemento "+i);
					ComputiCumuloModel lComCum = getComputo(i, aTipoOper);
					lListaComputi.add(lComCum);
				} else {
					siesLogger.debug("Parametro Multa_" + i + " assente nella form");
				}
			}

			// Solo per il debug
			siesLogger.debug("lListaComputi.size() = " + lListaComputi.size());
			for (int i = 0; i < lListaComputi.size(); i++) {
				ComputiCumuloModel lComCum = lListaComputi.elementAt(i);
				siesLogger.debug(lComCum.toString());
			}
		}

		return lListaComputi;
	}

	/**
	 * Recupera dalla form i dati dei quantum di computo relativi all'id passato in input
	 * 
	 * @param id_computo
	 * @return
	 * @throws F3BException
	 */
	private ComputiCumuloModel getComputo(int id_computo, String aTipoOper) throws F3BException {
		ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

		// IdComputiCumulo
		BigDecimal lIdComputiCumulo = getRequestBigDecimalParameter("IdComputiCumulo_" + id_computo);

		if ("I".compareTo(aTipoOper) != 0 && lIdComputiCumulo != null)
			lComputiModel.setIdComputiCumulo(lIdComputiCumulo);
		lComputiModel.setCodTipoAnnotazione("014"); // 014-Altro
		lComputiModel.setCodCausaleComputo("-"); // Fisso a trattino (res migrava 08)

		lComputiModel.setDataRichiesta(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		if (getRequestDateParameter("AnnoDataEmissione_AA", "MeseDataEmissione_AA",
				"GiornoDataEmissione_AA") != null) {
			lComputiModel.setDataEmissioneProvv(getRequestDateParameter("AnnoDataEmissione_AA",
					"MeseDataEmissione_AA", "GiornoDataEmissione_AA"));
			lComputiModel.setDataRicezioneProvv(getRequestDateParameter("AnnoDataRicezioneAtti",
					"MeseDataRicezioneAtti", "GiornoDataRicezioneAtti"));
			lComputiModel.setAnnoProvv(getRequestBigDecimalParameter("AnnoProtocollo"));
			lComputiModel.setProgrProvv(getRequestBigDecimalParameter("ProgrProtocollo"));
			lComputiModel.setNote(getRequestStringParameter("NumExCampionePenale"));
			lComputiModel.setSezioneProvv(getRequestStringParameter("SezioneProvv"));
			lComputiModel.setCodUfficioEmittenteProvv(
					getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter("CodUfficioEmittente"),
							getRequestStringParameter("CodLuogoEmittente")));
			lComputiModel.setCodLuogoUfficioProvv(
					getCodComuneByDescr(getRequestStringParameter("CodLuogoEmittente")).getCodComune());
		} else {
			lComputiModel.setDataEmissioneProvv(null);
			lComputiModel.setDataRicezioneProvv(null);
			lComputiModel.setAnnoProvv(null);
			lComputiModel.setProgrProvv(null);
			lComputiModel.setNote("");
			lComputiModel.setSezioneProvv("");
			lComputiModel.setCodUfficioEmittenteProvv(null);
			lComputiModel.setCodLuogoUfficioProvv(null);
		}

		lComputiModel.setFlagPiuMeno("-");

		// Multa
		String Multa = getRequestStringParameter("Multa_" + id_computo);
		String Multa_dec = getRequestStringParameter("Mul_dec_" + id_computo);
		if (!Multa.equals("")) {
			if (!Multa_dec.equals(""))
				lComputiModel.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
			else
				lComputiModel.setImportoMulta(new BigDecimal(Multa));
		} else if (!Multa_dec.equals(""))
			lComputiModel.setImportoMulta(new BigDecimal("0." + Multa_dec));

		// Ammenda
		String Ammenda = getRequestStringParameter("Ammenda_" + id_computo);
		String Ammenda_dec = getRequestStringParameter("Amm_dec_" + id_computo);
		if (!Ammenda.equals("")) {
			if (!Ammenda_dec.equals(""))
				lComputiModel.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
			else
				lComputiModel.setImportoAmmenda(new BigDecimal(Ammenda));
		} else if (!Ammenda_dec.equals(""))
			lComputiModel.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));

		lComputiModel.setFlagStato("I");
		lComputiModel.setMotivoModifica(null);

		lComputiModel.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		lComputiModel.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		if ("I".equals(aTipoOper)) {
			lComputiModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lComputiModel.setDataInserimento(DateUtils.getSysDate());
			lComputiModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		} else if ("M".equals(aTipoOper)) {
			lComputiModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lComputiModel.setDataAggiornamento(DateUtils.getSysDate());
			lComputiModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		}

		return lComputiModel;
	}

}
