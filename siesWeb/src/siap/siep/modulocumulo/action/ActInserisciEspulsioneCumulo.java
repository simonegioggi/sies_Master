package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un provedimento di Espulsione disposto
 * su uno dei titoli cumulati
 *
 * @author Intersistemi SpA
 *
 */
public class ActInserisciEspulsioneCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		String lPage = "";
		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.modulocumulo.action.ActRicercaRevocheMisureAlternativeCumulo"
		// + "&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
		// +this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
		// + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
		// +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		String lModalita = "";
		lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella

		IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		if ("I".equals(lModalita)) {
			siesLogger.debug("Sono in INSERIMENTO");

			StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("I");

			// Si carica in StatoEsecuzione l'ufficio emittente
			lStatoEsecMod.setCodUfficioEmittente(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)));
			lStatoEsecMod.setCodLuogoEmittente(
					getCodComuneByDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE))
							.getCodComune());

			// Si forza l'esito positivo (Concede, Accoglie) in base al COD_MOTIVO.
			String[] arrayUDS = new String[] { "2140" };
			String[] arrayTDS = new String[] { "0029" };
			if (Arrays.asList(arrayUDS).contains(getRequestStringParameter(CAMPO_COD_MOTIVO)))
				lStatoEsecMod.setCodEsito("0073");

			if (Arrays.asList(arrayTDS).contains(getRequestStringParameter(CAMPO_COD_MOTIVO)))
				lStatoEsecMod.setCodEsito("0019");

			lStatoEsecMod.setCodLuogoEmittente(
					getCodComuneByDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE))
							.getCodComune());

			siesLogger.debug("lStatoEsecMod = " + lStatoEsecMod);

			ComputiCumuloModel lComputo = this.getDatiComputo("I");
			siesLogger.debug("lComputo = " + lComputo);

			lStatoEsecMod = lCtrlStatoEsec.ExInserisciStatoEsecComputoCumulo(lStatoEsecMod, lComputo);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioEspulsioneCumulo" + "&"
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
			// siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);

			// Si carica in StatoEsecuzione l'ufficio emittente reimpostato.
			lStatoEsecMod.setCodUfficioEmittente(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)));
			lStatoEsecMod.setCodLuogoEmittente(
					getCodComuneByDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE))
							.getCodComune());

			// Si carica l'esito Procedimento impostato nella form.
			String[] arrayUDS = new String[] { "2140" };
			String[] arrayTDS = new String[] { "0029" };
			if (Arrays.asList(arrayUDS).contains(getRequestStringParameter(CAMPO_COD_MOTIVO)))
				lStatoEsecMod.setCodEsito("0073");

			if (Arrays.asList(arrayTDS).contains(getRequestStringParameter(CAMPO_COD_MOTIVO)))
				lStatoEsecMod.setCodEsito("0019");

			ComputiCumuloModel lComputo = this.getDatiComputo("M");
			siesLogger.debug("lComputo = " + lComputo);

			lComputo.setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());

			// NB Per la modifica di StatoEsec. e ComputoCumulo può essere nomenclato ExModificaPresofferto
			// --> ExModificaStatoEsecComputoCumulo.
			lCtrlStatoEsec.ExModificaStatoEsecComputoCumulo(lStatoEsecMod, lComputo);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioEspulsioneCumulo" + "&"
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
				// Un solo computo, cancello tutto
				siesLogger.debug("Elimino l'intero provvedimento di computo: " + lIdStatoEsecuzione);
				lCtrlStatoEsec.ExCancellaStatoEsecTitoloCumulatoById(lIdStatoEsecuzione, null);
			} else {
				// Ho più computi cancello solo quello indicato sulla request
				siesLogger.debug("Elimino il solo conmputo:" + lIdComputo);
				IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
				lCtrlComputi.ExCancellaComputiCumuloBykey(lIdComputo);
			}
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActRicercaEspulsioneCumulo" + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
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
		lStaMod.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
		lStaMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));
		if (super.getDatiTitoloCumulato().getProcedimentoCumulato() != null) {
			lStaMod.setCodUfficioEmittente(
					super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato());
			lStaMod.setCodLuogoEmittente(
					super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato());
		}
		lStaMod.setDataEmissione(DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_EMISSIONE),
				getRequestStringParameter(CAMPO_MESE_DATA_EMISSIONE),
				getRequestStringParameter(CAMPO_GIORNO_DATA_EMISSIONE)));
		lStaMod.setCodEsito("0001");
		lStaMod.setCodEsitoTenore("-");
		lStaMod.setAnnoProcedimento(null);
		lStaMod.setProgrProcedimento(null);
		lStaMod.setAnnoProvvedimento(null);
		lStaMod.setProgrProvvedimento(null);

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
	private ComputiCumuloModel getDatiComputo(String aTipoOper) throws F3BException {
		// ==============================================================================
		// Recupero i dati dell' Espulsione.
		// ==============================================================================
		ComputiCumuloModel lComputo = new ComputiCumuloModel();

		lComputo.setCodTipoAnnotazione("-");
		lComputo.setCodCausaleComputo("-");
		lComputo.setCodDpr("-");

		lComputo.setDataEmissioneProvv(DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_EMISSIONE),
				getRequestStringParameter(CAMPO_MESE_DATA_EMISSIONE),
				getRequestStringParameter(CAMPO_GIORNO_DATA_EMISSIONE)));
		lComputo.setAnnoProvv(getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
		lComputo.setProgrProvv(getRequestBigDecimalParameter(CAMPO_PROGR_PROVVEDIMENTO));
		lComputo.setAnnoProc(getRequestBigDecimalParameter(CAMPO_ANNO_PROCEDIMENTO));
		lComputo.setProgrProc(getRequestBigDecimalParameter(CAMPO_PROGR_PROCEDIMENTO));

		// lComputo.setCodTipoAutoritaEmittente(getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		// lComputo.setCodLuogoEmittente(getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE));
		lComputo.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		lComputo.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		lComputo.setCodUfficioEmittenteProvv(getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE)));
		lComputo.setCodLuogoUfficioProvv(getCodComuneByDescr(
				getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE)).getCodComune());

		// ==============================================================
		// Recupero dei Campi opzionali in base all'oggetto procedimento
		// ==============================================================
		String[] arrayUDS = new String[] { "2140" };
		String[] arrayTDS = new String[] { "0029" };

		if (Arrays.asList(arrayUDS).contains(getRequestStringParameter(CAMPO_COD_MOTIVO))) {
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE).equals(""))
				lComputo.setDataEmissioneProvv(DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE)));
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA).equals(""))
				lComputo.setNumAnniMisura(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA));
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA).equals(""))
				lComputo.setNumMesiMisura(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA));
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA).equals(""))
				lComputo.setNumGiorniMisura(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA));
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE).equals(""))
				lComputo.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
		}

		if (Arrays.asList(arrayTDS).contains(getRequestStringParameter(CAMPO_COD_MOTIVO))) {
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE).equals(""))
				lComputo.setDataEmissioneProvv(DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE)));
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE).equals(""))
				lComputo.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
		}

		if ("I".equals(aTipoOper)) {
			lComputo.setCodOperatoreInserimento(getCodUtenteConnesso());
			lComputo.setDataInserimento(DateUtils.getSysDate());
			lComputo.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		} else if ("M".equals(aTipoOper)) {
			lComputo.setIdComputiCumulo(
					getRequestBigDecimalParameter(ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO));

			lComputo.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lComputo.setDataAggiornamento(DateUtils.getSysDate());
			lComputo.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		}

		return lComputo;
	}

}
