package siap.siep.modulocumulo.action;

/**
* <p>Title: ActInserisciPenaComplessivaCumulo</p>
* <p>Description: Classe Action per l'inserimento di PenaComplessiva</p>
* <p>   in ambito Cumulo (Pena_complessiva_Cumulo) </p>
*/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciPenaComplessivaCumulo extends ActionModuloCumulo implements
		ICostantiPenaComplessivaCumulo, ICostantiSanzioneSostitutivaCumulo, ICostantiContinuazioneCumulo {

	protected PenaComplessivaCumuloModel mPenaCompMod = new PenaComplessivaCumuloModel();
	// SANZIONE SOSTITUTIVA
	protected SanzioneSostitutivaCumuloModel mSanzSostMod = null;
	// CONTINUAZIONE CON ALTRE SENTENZE
	protected List mContList = new ArrayList();

	/**
	 * Azione di Inserimento del PenaComplessivaCumulo
	 * 
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFasMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Valorizzazione della Pena Complessiva
		letturaDati(lFasMod.getIdFascicoloSiep());

		// Inserimento
		IPenaComplessivaCumulo lCtrl = SIEPLookupRemote.getPenaComplessivaCumuloRemote();
		PenaComplessivaCumuloModel lPenComRitMod = lCtrl
				.ExInserisciPenaCompSanzioneSostContinuazioniCum(mPenaCompMod, mSanzSostMod, mContList);

		// Dettaglio
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActLoadDettaglioPenaComplessivaCumulo&"
				+ CAMPO_ID_PENA_COMPLESSIVA_CUM + "=" + lPenComRitMod.getIdPenaComplessivaCum().toString();

		return lPage;
	}

	/**
	 * 
	 * @param aIdTitoloCumulato
	 * @throws Exception
	 */
	protected void letturaDati(BigDecimal aIdTitoloCumulato) throws Exception {
		// ==========================================================================
		// PENA COMPLESSIVA
		// ==========================================================================
		mPenaCompMod.setFlagStato("I");
		mPenaCompMod.setMotivoModifica(
				getRequestStringParameter(ICostantiPenaComplessivaCumulo.CAMPO_MOTIVO_MODIFICA));

		mPenaCompMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

		mPenaCompMod.setCodTipoPenaDetentiva(getRequestStringParameter(CAMPO_COD_TIPO_PENA_DETENTIVA));

		mPenaCompMod.setNumAnniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE));
		mPenaCompMod.setNumMesiReclusione(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE));
		mPenaCompMod.setNumGiorniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE));

		// Multa
		if ((getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA) != null
				&& !(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)).equals(""))
				|| (getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA) != null
						&& !(getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA)).equals(""))) {
			if (getRequestStringParameter(CAMPO_VALUTA_IMPORTO_MULTA).equals("LIT")) {
				mPenaCompMod
						.setImportoMulta(Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)));
			} else {
				mPenaCompMod
						.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)
								+ "." + getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA)));
			}
		}

		mPenaCompMod.setNumAnniArresto(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ARRESTO));
		mPenaCompMod.setNumMesiArresto(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ARRESTO));
		mPenaCompMod.setNumGiorniArresto(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ARRESTO));

		if ((getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA) != null
				&& !(getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA)).equals(""))
				|| (getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_AMMENDA) != null
						&& !(getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_AMMENDA)).equals(""))) {
			if (getRequestStringParameter(CAMPO_VALUTA_IMPORTO_AMMENDA).equals("LIT")) {
				mPenaCompMod.setImportoAmmenda(
						Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA)));
			} else {
				mPenaCompMod.setImportoAmmenda(
						new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA) + "."
								+ getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_AMMENDA)));
			}
		}

		mPenaCompMod
				.setNumAnniIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO));
		mPenaCompMod
				.setNumMesiIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ISOLAMENTO_DIURNO));
		mPenaCompMod.setNumGiorniIsolamentoDiurno(
				getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO));

		mPenaCompMod.setDataPrescrizione(getRequestDateParameter(CAMPO_ANNO_DATA_PRESCRIZIONE,
				CAMPO_MESE_DATA_PRESCRIZIONE, CAMPO_GIORNO_DATA_PRESCRIZIONE));

		mPenaCompMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		mPenaCompMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		mPenaCompMod.setDataInserimento(DateUtils.getSysDate());

		// ==========================================================================
		// SANZIONE SOSTITUTIVA
		// ==========================================================================
		boolean flagSanzioneSostitutiva = isRequestChecked(CAMPO_FLAG_SANZIONE_SOSTITUTIVA);
		if (flagSanzioneSostitutiva) {
			mSanzSostMod = new SanzioneSostitutivaCumuloModel();

			mSanzSostMod.setCodTipoSanzione(getRequestStringParameter(CAMPO_COD_TIPO_SANZIONE));

			if (!this.isRequestParameterNullObj(CAMPO_NUM_ANNI))
				mSanzSostMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));

			if (!this.isRequestParameterNullObj(CAMPO_NUM_MESI))
				mSanzSostMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));

			if (!this.isRequestParameterNullObj(CAMPO_NUM_GIORNI))
				mSanzSostMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));

			// Sanzione Pecuniaria MULTA
			if (!this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)
					&& (!getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA).equals("")
							|| !getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA)
									.equals(""))) {
				if (getRequestStringParameter(CAMPO_VALUTA_SANZIONE_PECUNIARIA).equals("LIT")) {
					mSanzSostMod.setSanzionePecuniariaMulta(
							Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)));
				} else {
					mSanzSostMod.setSanzionePecuniariaMulta(new BigDecimal(
							getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA) + "."
									+ getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA)));
				}
			}

			// Sanzione Pecuniaria AMMENDA - Nuovo campo su Sanzione Sostitutiva messo a LUGLIO 2009
			if (!this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)
					&& (!getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA).equals("")
							|| !getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA)
									.equals(""))) {
				if (getRequestStringParameter(CAMPO_VALUTA_SANZIONE_PECUNIARIA).equals("LIT")) {
					mSanzSostMod.setSanzionePecuniariaAmmenda(Utils
							.toEuro(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)));
				} else {
					mSanzSostMod.setSanzionePecuniariaAmmenda(new BigDecimal(
							getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA) + "."
									+ getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA)));
				}
			}

			mSanzSostMod.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

			mSanzSostMod.setFlagStato("I");
			mSanzSostMod.setMotivoModifica(getRequestStringParameter(CAMPO_MOTIVO_MODIFICA_NOTE));

			mSanzSostMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			mSanzSostMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			mSanzSostMod.setDataInserimento(DateUtils.getSysDate());

		} // Chiudo if flagSanzioneSostitutiva

		// ==========================================================================
		// CONTINUAZIONE CON ALTRE SENTENZE
		// ==========================================================================
		if (isRequestChecked(CAMPO_FLAG_PENA_IN_CONTINUAZIONE)) {
			mPenaCompMod.setFlagPenaInContinuazione("S");

			ContinuazioneCumuloModel lCont = new ContinuazioneCumuloModel();

			lCont.setCodTipoContinuazione(
					getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE));
			lCont.setAnnoSentenza(
					getRequestBigDecimalParameter(ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA));
			lCont.setNumSentenza(getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA));

			lCont.setDataSentenza(
					getRequestDateParameter(ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA,
							ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA,
							ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA));

			// Autorità
			lCont.setCodTipoAutorita(
					getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA));
			String lDescLuogo = getRequestStringParameter(
					ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA);
			ComuneModel lComuneLuogo = getCodComuneByDescr(lDescLuogo);

			// Controllo esistenza Ufficio
			// String lCodLuogo = getCodUfficioByCodTipoUfficioDescrComune(lCont.getCodTipoAutorita(),
			// lDescLuogo);

			lCont.setCodLuogoAutorita(lComuneLuogo.getCodComune());

			if (!isRequestParameterNullObj(ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM)) {
				lCont.setAnnoRegePm(
						getRequestBigDecimalParameter(ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM));
				lCont.setNumRegePm(getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM));
			}

			if (!isRequestParameterNullObj(ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN)) {
				lCont.setAnnoRegGen(
						getRequestBigDecimalParameter(ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN));
				lCont.setNumeroRegGen(
						getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN));
				lCont.setTipoRegGen(
						getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN));
			}

			lCont.setMotivoModifica(
					getRequestStringParameter(ICostantiContinuazioneCumulo.CAMPO_MOTIVO_INS_MOD));

			lCont.setFlagStato("I");
			lCont.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

			// Se selezionato dalla lista
			if (!isRequestParameterNullObj(ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT))
				lCont.setTitIdTitoloCumulatoCont(getRequestBigDecimalParameter(
						ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT));

			lCont.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCont.setDataInserimento(DateUtils.getSysDate());
			lCont.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			mContList.add(lCont);

		} // Chiude if CAMPO_FLAG_PENA_IN_CONTINUAZIONE
		else {
			mPenaCompMod.setFlagPenaInContinuazione("N");
		}
	}

}