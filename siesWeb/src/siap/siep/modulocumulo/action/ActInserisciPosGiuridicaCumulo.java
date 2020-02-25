package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.controller.IPosizioneGiuridicaCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che effettua l'inserimento o la modifica della posizione giuridica puù ulterori dati
 *
 * @author d.fiorletta
 *
 */
public class ActInserisciPosGiuridicaCumulo extends ActionModuloCumulo
		implements ICostantiModuloCumulo, ICostantiPosizioneGiuridicaCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		super.getDatiIstruttoria();

		DatiFinaliCumuloAggregatoModel lDatiAggregati = super.getDatiFinaliCumuloAggregato();
		PosizioneGiuridicaCumuloModel lPosizioneGiuridicaOld = lDatiAggregati.getPosizioneGiuridicaCumulo();

		// ==========================================================================
		// Vado in insert o update
		// ==========================================================================
		String lModalita = getRequestStringParameter("modalita");

		PosizioneGiuridicaCumuloModel lPosizioneGiuridica = null;
		if (isRequestParameterNullObj("FLAG_POS_GIU_TITOLO"))
			lPosizioneGiuridica = getDatiForm();
		else
			lPosizioneGiuridica = getDatiDaTitolo();

		// MG spostata assegnazione variabile
		IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();

		if (lModalita.equals(ICostantiModuloCumulo.MODALITA_INSERIMENTO)) {
			lPosizioneGiuridica.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPosizioneGiuridica.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPosizioneGiuridica.setDataInserimento(DateUtils.getSysDate());

			// siesLogger.debug("--XX-- ho Inserito questa Posiz Giur --> "+lPosizioneGiuridica);
			/*
			 * ISSUE MAC : Aggiornamento del flag altra causa (posizione giuridica) sul fascicolo Numero MAC :
			 * 20191128013 Autore : monica Data : 19/dic/2019 Branch : 11.2.4
			 */
			// MG spostata assegnazione variabile
			// IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
			// ***** FINE INTERVENTO 20191128013 *****//
			lCtrlDatiFinali.ExInserisciPosizioneGiuridicaCumulo(lPosizioneGiuridica);
		} else if (lModalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) {
			lPosizioneGiuridica.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lPosizioneGiuridica.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lPosizioneGiuridica.setDataAggiornamento(DateUtils.getSysDate());

			/*
			 * ISSUE MAC : Aggiornamento del flag altra causa (posizione giuridica) sul fascicolo Numero MAC :
			 * 20191128013 Autore : monica Data : 19/dic/2019 Branch : 11.2.4
			 */
			// MG spostata assegnazione variabile
			// IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
			// ***** FINE INTERVENTO 20191128013 *****//
			lCtrlDatiFinali.ExModificaPosizioneGiuridicaCumulo(lPosizioneGiuridica);

			// Vado in aggiornamento del Flag sulla pena se presente in modo da segnalare
			// la necessità di ricalcolare la pena
			PenaRideterminataCumuloModel lPenaResidua = lDatiAggregati.getPenaResiduaCumulo();
			if (lPenaResidua != null && lPenaResidua.getIdPenaRideterminataCumulo() != null) {
				if ((lPosizioneGiuridicaOld.getDataInizio() != null
						&& lPosizioneGiuridica.getDataInizio() == null)
						|| (lPosizioneGiuridicaOld.getDataInizio() == null
								&& lPosizioneGiuridica.getDataInizio() != null)
						|| (lPosizioneGiuridicaOld.getDataInizio() != null
								&& lPosizioneGiuridica.getDataInizio() != null
								&& !DateUtils.isEquals(lPosizioneGiuridicaOld.getDataInizio(),
										lPosizioneGiuridica.getDataInizio()))) {
					lPenaResidua.setIsPenaDaRicalcolare("S");

					lCtrlDatiFinali.ExModificaPenaRideterminataCumulo(lPenaResidua);
				}
			}
		}

		/*
		 * ISSUE MAC : Aggiornamento del flag altra causa (posizione giuridica) sul fascicolo Numero MAC :
		 * 20191128013 Autore : monica Data : 19/dic/2019 Branch : 11.2.4
		 */
		// ==========================================================================
		// Recupero il fascicolo da aggiornare
		// ==========================================================================
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String sFlagAltraCausa = this.getFlagAltraCausa(lPosizioneGiuridica.getCodPosizioneGiuridica());

		lFascMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lFascMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lFascMod.setDataAggiornamento(DateUtils.getSysDate());
		lCtrlDatiFinali.ExUpdateFlagAltraCausaFascicolo(lFascMod, sFlagAltraCausa);
		// ***** FINE INTERVENTO 20191128013 *****//

		// =============================
		// Invoco la Action di dettaglio
		// =============================
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActDettaglioPosGiuridicaCumulo&"
				+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
				+ this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		return lPage;
	}

	/*
	 * ISSUE MAC : aggiunto metodo che restituisce il valore del flag altra causa (posizione giuridica) Numero
	 * MAC : 20191128013 Autore : monica Data : 19/dic/2019 Branch : 11.2.4
	 */

	/**
	 * Metodo che restituisce il valore del FLAG_ALTRA_CAUSA
	 *
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getFlagAltraCausa(String codPosizioneGiuridica) throws F3BException {

		String flagAltraCausa = "N";
		Collection<DecodificheModel> lCollPosGiuAltra = DecodificheManager.getInstance()
				.getPosizioneGiuridicaAltraCausa();
		Iterator iteCollPosGiuAltra = lCollPosGiuAltra.iterator();

		while (iteCollPosGiuAltra.hasNext()) {
			DecodificheModel dMPosGiuAltra = (DecodificheModel) iteCollPosGiuAltra.next();
			String code = dMPosGiuAltra.getCode();
			if (code.equals(codPosizioneGiuridica)) {
				flagAltraCausa = "S";
				break;
			}
	}

		return flagAltraCausa;
	}
	// ***** FINE INTERVENTO 20191128013 *****//

	/**
	 * Metodo che recupera i dati dalla form
	 * 
	 * @return
	 */
	private PosizioneGiuridicaCumuloModel getDatiForm() throws F3BException {

		PosizioneGiuridicaCumuloModel lPosMod = new PosizioneGiuridicaCumuloModel();

		siesLogger.debug("--XX-- ------------ getDatiFormr -------------------- ");
		lPosMod.setIdPosizioneGiuridicaCum(getRequestBigDecimalParameter(CAMPO_ID_POSIZIONE_GIURIDICA_CUM));
		lPosMod.setCodPosizioneGiuridica(getRequestStringParameter(CAMPO_COD_POSIZIONE_GIURIDICA));

		String lTipoPosizione = (getRequestStringParameter(CAMPO_RADIO_TIPO_POS));

		if (lTipoPosizione.equals(CAMPO_CHECK_TIPO_POS_ESPIST)) {
			lPosMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
					CAMPO_GIORNO_DATA_INIZIO));
			lPosMod.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

			if ("14".equals(lPosMod.getCodPosizioneGiuridica())
					|| "31".equals(lPosMod.getCodPosizioneGiuridica())
					|| "32".equals(lPosMod.getCodPosizioneGiuridica())
					|| "33".equals(lPosMod.getCodPosizioneGiuridica())) {
				// x il semilibero recupero anche i dati dell'ordinanza di concessione
				lPosMod.setChiaveAnnoFasSius(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FAS_SIUS));
				lPosMod.setChiaveProgrFasSius(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FAS_SIUS));

				String lCodTipoUff = getRequestStringParameter(CAMPO_TIPO_UFF_FAS_SIUS);
				String lDescSedeUff = getRequestStringParameter(CAMPO_SEDE_UFF_FAS_SIUS);

				String lCodUfficioTDS = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescSedeUff);

				lPosMod.setChiaveUffFasSius(lCodUfficioTDS);

				lPosMod.setAnnoRegistro(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO));
				lPosMod.setNumeroRegistro(getRequestBigDecimalParameter(CAMPO_NUMERO_REGISTRO));
				lPosMod.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
				lPosMod.setDataEmissioneProvv(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_PROVV,
						CAMPO_MESE_DATA_EMISSIONE_PROVV, CAMPO_GIORNO_DATA_EMISSIONE_PROVV));

				if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO_MISURA))
					lPosMod.setDataInizioMisura(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_MISURA,
							CAMPO_MESE_DATA_INIZIO_MISURA, CAMPO_GIORNO_DATA_INIZIO_MISURA));
			}
		} else if (lTipoPosizione.equals(CAMPO_CHECK_TIPO_POS_ESPALTRO)) {
			lPosMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
					CAMPO_GIORNO_DATA_INIZIO));
			lPosMod.setAltroLuogo(getRequestStringParameter(CAMPO_ALTRO_LUOGO));

			if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO_MISURA))
				lPosMod.setDataInizioMisura(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_MISURA,
						CAMPO_MESE_DATA_INIZIO_MISURA, CAMPO_GIORNO_DATA_INIZIO_MISURA));

			if (lPosMod.isMisura()) {
				lPosMod.setChiaveAnnoFasSius(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FAS_SIUS));
				lPosMod.setChiaveProgrFasSius(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FAS_SIUS));

				String lCodTipoUff = getRequestStringParameter(CAMPO_TIPO_UFF_FAS_SIUS);
				String lDescSedeUff = getRequestStringParameter(CAMPO_SEDE_UFF_FAS_SIUS);

				String lCodUfficioTDS = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescSedeUff);

				lPosMod.setChiaveUffFasSius(lCodUfficioTDS);

				lPosMod.setAnnoRegistro(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO));
				lPosMod.setNumeroRegistro(getRequestBigDecimalParameter(CAMPO_NUMERO_REGISTRO));
				lPosMod.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
				lPosMod.setDataEmissioneProvv(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_PROVV,
						CAMPO_MESE_DATA_EMISSIONE_PROVV, CAMPO_GIORNO_DATA_EMISSIONE_PROVV));

				// Codice_Posizione_Giurica 12 + Check = Differimento pena nella forma della detenzione
				// domiciliare
				if ("12".equals(lPosMod.getCodPosizioneGiuridica())) {
					if (isRequestChecked(CAMPO_CHECK_DIFFERIMENTO_DET_DOM)) {
						lPosMod.setFlagDifferimentoDetDom("S");
						lPosMod.setDataInizioMisura(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_MISURA,
								CAMPO_MESE_DATA_INIZIO_MISURA, CAMPO_GIORNO_DATA_INIZIO_MISURA));

						// Calcolo del periodo di Detenzione Domiciliare
						BigDecimal lAnniMisura = null;
						BigDecimal lGiorniMisura = null;
						BigDecimal lMesiMisura = null;

						if (!this.isRequestParameterNullObj(CAMPO_NUM_ANNI_MISURA)
								&& !getRequestStringParameter(CAMPO_NUM_ANNI_MISURA).equals("")) {
							lAnniMisura = new BigDecimal(getRequestStringParameter(CAMPO_NUM_ANNI_MISURA));
							lPosMod.setNumAnniMisura(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_MISURA));
						}

						if (!this.isRequestParameterNullObj(CAMPO_NUM_MESI_MISURA)
								&& !getRequestStringParameter(CAMPO_NUM_MESI_MISURA).equals("")) {
							lMesiMisura = new BigDecimal(getRequestStringParameter(CAMPO_NUM_MESI_MISURA));
							lPosMod.setNumMesiMisura(getRequestBigDecimalParameter(CAMPO_NUM_MESI_MISURA));
						}

						if (!this.isRequestParameterNullObj(CAMPO_NUM_GIORNI_MISURA)
								&& !getRequestStringParameter(CAMPO_NUM_GIORNI_MISURA).equals("")) {
							lGiorniMisura = new BigDecimal(
									getRequestStringParameter(CAMPO_NUM_GIORNI_MISURA));
							lPosMod.setNumGiorniMisura(
									getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_MISURA));
						}

						// Se la DATA_FINE_MISURA è presente viene inserita direttamente
						if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE_MISURA)
								&& getRequestStringParameter(CAMPO_ANNO_DATA_FINE_MISURA) != null
								&& !getRequestStringParameter(CAMPO_ANNO_DATA_FINE_MISURA).equals("")) {
							lPosMod.setDataFineMisura(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_MISURA,
									CAMPO_MESE_DATA_FINE_MISURA, CAMPO_GIORNO_DATA_FINE_MISURA));
							// lPosMod.setNumAnniMisura(null);
							// lPosMod.setNumMesiMisura(null);
							// lPosMod.setNumGiorniMisura(null);
						} else {
							// Alrimenti si Calcola la Data_Fine_Misura attraverso i QUANTUM
							// Date lDataFineMisura = null;
							CalendarModel lCalMod = new CalendarModel();

							lCalMod.setNumAnni(lAnniMisura);
							lCalMod.setNumGiorni(lGiorniMisura);
							lCalMod.setNumMesi(lMesiMisura);

							ICalcoloPena lCtrlCalcolo = SIEPLookupRemote.getCalcoloPenaRemote();
							/* lDataFineMisura= */lCtrlCalcolo
									.exCalcolaNuovaDataFine(lPosMod.getDataInizioMisura(), lCalMod, true);
							// lPosMod.setDataFineMisura(lDataFineMisura);
						}
					} // chiude if(isRequestChecked(CAMPO_CHECK_DIFFERIMENTO_DET_DOM) )
				} // Chiude if("12".equals(lPosMod.getCodPosizioneGiuridica()) )
			} // Chiude if lPos.isMisura
		} else if (lTipoPosizione.equals(CAMPO_CHECK_TIPO_POS_LIBERO)) {
			if ("16".equals(lPosMod.getCodPosizioneGiuridica())
					|| "17".equals(lPosMod.getCodPosizioneGiuridica())) { // Differimento Recupero i dati del
																			// provvedimento di concessione
				lPosMod.setChiaveAnnoFasSius(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FAS_SIUS));
				lPosMod.setChiaveProgrFasSius(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FAS_SIUS));

				String lCodTipoUff = getRequestStringParameter(CAMPO_TIPO_UFF_FAS_SIUS);
				String lDescSedeUff = getRequestStringParameter(CAMPO_SEDE_UFF_FAS_SIUS);

				String lCodUfficioTDS = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescSedeUff);

				lPosMod.setChiaveUffFasSius(lCodUfficioTDS);

				lPosMod.setAnnoRegistro(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO));
				lPosMod.setNumeroRegistro(getRequestBigDecimalParameter(CAMPO_NUMERO_REGISTRO));
				lPosMod.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
				// lPosMod.setDataInizio ( getRequestDateParameter (
				// CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
				lPosMod.setDataEmissioneProvv(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_PROVV,
						CAMPO_MESE_DATA_EMISSIONE_PROVV, CAMPO_GIORNO_DATA_EMISSIONE_PROVV));

				lPosMod.setNumAnniMisura(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_MISURA));
				lPosMod.setNumMesiMisura(getRequestBigDecimalParameter(CAMPO_NUM_MESI_MISURA));
				lPosMod.setNumGiorniMisura(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_MISURA));

				lPosMod.setDataFineMisura(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_MISURA,
						CAMPO_MESE_DATA_FINE_MISURA, CAMPO_GIORNO_DATA_FINE_MISURA));
			}

			if ("17".equals(lPosMod.getCodPosizioneGiuridica())) {
				if (isRequestChecked(CAMPO_CHECK_DECISIONE_TDS)) {
					// siesLogger.debug("--XX-- ActInserisciPosizioneGiuridicaCumulo - Chek Decisione TDS -
					// true");
					lPosMod.setFlagDecisioneTDS("S");
				}
			}
		}

		lPosMod.setDatIdDatiFinaliCumulo(
				getRequestBigDecimalParameter(ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO));
		lPosMod.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		// siesLogger.debug("lPosMod = "+lPosMod);

		return lPosMod;
	}

	/**
	 * 
	 * @return
	 * @throws F3BException
	 */
	private PosizioneGiuridicaCumuloModel getDatiDaTitolo() throws F3BException {

		siesLogger.debug("getDatiDaTitolo!!!");

		PosizioneGiuridicaCumuloModel lPosMod = new PosizioneGiuridicaCumuloModel();

		BigDecimal lIdPosTit = getRequestBigDecimalParameter(
				ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM + "_LOAD");

		// IPosizioneGiuridicaCumulo lCtrlPosGiuCum = SIEPLookupRemote.getPosizioneGiuridicaCumuloRemote();

		IPosizioneGiuridicaCumulo lCtrlPosGiuridicaCumulo = SIEPLookupRemote
				.getPosizioneGiuridicaCumuloRemote();
		lPosMod = lCtrlPosGiuridicaCumulo.ExRicercaPosizioneGiuridicaCumuloById(lIdPosTit);

		// Ripulisco i campi
		lPosMod.setIdPosizioneGiuridicaCum(null);

		String lModalita = getRequestStringParameter("modalita");

		if (lModalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA))
			lPosMod.setIdPosizioneGiuridicaCum(
					getRequestBigDecimalParameter(CAMPO_ID_POSIZIONE_GIURIDICA_CUM));

		lPosMod.setTitIdTitoloCumulato(null);

		lPosMod.setDatIdDatiFinaliCumulo(
				getRequestBigDecimalParameter(ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO));
		lPosMod.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		siesLogger.debug("lPosMod duplicazione " + lPosMod);

		return lPosMod;
	}

}