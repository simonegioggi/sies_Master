package siap.siep.penacomplessiva.action;

/**
* <p>Title: ActInserisciPenaComplessiva</p>
* <p>Description: Classe Action per l'inserimento di PenaComplessiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.continuazione.action.ICostantiContinuazione;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciPenaComplessiva extends ActionSiap
		implements ICostantiPenaComplessiva, ICostantiSanzioneSostitutiva {

	protected PenaComplessivaModel mPenaCompMod = new PenaComplessivaModel();
	// SANZIONE SOSTITUTIVA
	protected SanzioneSostitutivaModel mSanzSostMod = null;
	// CONTINUAZIONE CON ALTRI REATI
	protected List mContList = new ArrayList();

	/**
	 * Azione di Inserimento del PenaComplessiva
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		FascicoloSiepModel lFasMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Valorizzazione della Pena Complessiva
		letturaDati(lFasMod.getIdFascicoloSiep());

		// Inserimento
		IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComRitMod = lCtrl.ExInserisciPenaCompSanzioneSostContinuazioni(mPenaCompMod,
				mSanzSostMod, mContList);

		// Dettaglio
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&"
				+ CAMPO_ID_PENA_COMPLESSIVA + "=" + lPenComRitMod.getIdPenaComplessiva().toString();

		return lPage;
	}

	protected void letturaDati(BigDecimal aIdFascicoloSiep) throws Exception {

		mPenaCompMod.setCodTipoPenaDetentiva(getRequestStringParameter(CAMPO_COD_TIPO_PENA_DETENTIVA));
		mPenaCompMod.setNumAnniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE));
		mPenaCompMod.setNumMesiReclusione(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE));
		mPenaCompMod.setNumGiorniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE));

		mPenaCompMod
				.setNumAnniIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO));
		mPenaCompMod
				.setNumMesiIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ISOLAMENTO_DIURNO));
		mPenaCompMod.setNumGiorniIsolamentoDiurno(
				getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO));

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

		// ? mPenaCompMod.setDataInizio( getRequestDateParameter(
		// CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
		// ? mPenaCompMod.setDataFine( getRequestDateParameter(
		// CAMPO_ANNO_DATA_FINE,CAMPO_MESE_DATA_FINE,CAMPO_GIORNO_DATA_FINE) );
		mPenaCompMod.setCodTipoRito("-");

		/*
		 * mPenaCompMod.setDataInizioIsolamentoDiurno( getRequestDateParameter(
		 * CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO, CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO,
		 * CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO) ); mPenaCompMod.setDataFineIsolamentoDiurno(
		 * getRequestDateParameter( CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO,
		 * CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO, CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO) );
		 */

		mPenaCompMod.setDataPrescrizione(getRequestDateParameter(CAMPO_ANNO_DATA_PRESCRIZIONE,
				CAMPO_MESE_DATA_PRESCRIZIONE, CAMPO_GIORNO_DATA_PRESCRIZIONE));

		// SANZIONE SOSTITUTIVA
		// SanzioneSostitutivaModel mSanzSostMod = null;

		boolean flagSanzioneSostitutiva = isRequestChecked(CAMPO_FLAG_SANZIONE_SOSTITUTIVA);
		if (flagSanzioneSostitutiva) {
			mSanzSostMod = new SanzioneSostitutivaModel();

			mSanzSostMod.setCodTipoSanzione(getRequestStringParameter(CAMPO_COD_TIPO_SANZIONE));
			if (!this.isRequestParameterNullObj(CAMPO_NUM_ANNI))
				mSanzSostMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));

			if (!this.isRequestParameterNullObj(CAMPO_NUM_MESI))
				mSanzSostMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));

			if (!this.isRequestParameterNullObj(CAMPO_NUM_GIORNI))
				mSanzSostMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));

			if (!this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)
					&& ((getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA) != null
							&& !(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA))
									.equals(""))
							|| (getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA) != null
									&& !(getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA))
											.equals("")))) {
				if (getRequestStringParameter(CAMPO_VALUTA_SANZIONE_PECUNIARIA).equals("LIT")) {
					mSanzSostMod.setSanzionePecuniariaMulta(
							Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)));
				} else {
					mSanzSostMod.setSanzionePecuniariaMulta(new BigDecimal(
							getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA) + "."
									+ getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA)));
				}
			}

			if (!this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)
					&& ((getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA) != null
							&& !(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA))
									.equals(""))
							|| (getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA) != null
									&& !(getRequestStringParameter(
											CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA)).equals("")))) {
				if (getRequestStringParameter(CAMPO_VALUTA_SANZIONE_PECUNIARIA).equals("LIT")) {
					mSanzSostMod.setSanzionePecuniariaAmmenda(Utils
							.toEuro(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)));
				} else {
					mSanzSostMod.setSanzionePecuniariaAmmenda(new BigDecimal(
							getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA) + "."
									+ getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA)));
				}
			}

			mSanzSostMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			mSanzSostMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			mSanzSostMod.setDataInserimento(DateUtils.getSysDate());
		}

		mPenaCompMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		mPenaCompMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		mPenaCompMod.setDataInserimento(DateUtils.getSysDate());
		mPenaCompMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

		// CONTINUAZIONE CON ALTRI REATI
		// List mContList = new ArrayList();

		if (isRequestChecked(CAMPO_FLAG_PENA_IN_CONTINUAZIONE)) {
			mPenaCompMod.setFlagPenaInContinuazione("S");

			String[] lTipoContinuazione = getRequestStringParameters(
					ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE);

			String[] lAnnoSentenza = getRequestStringParameters(ICostantiContinuazione.CAMPO_ANNO_SENTENZA);
			String[] lNumSentenza = getRequestStringParameters(ICostantiContinuazione.CAMPO_NUM_SENTENZA);

			String[] lGiornoDataSentenza = getRequestStringParameters(
					ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA);
			String[] lMeseDataSentenza = getRequestStringParameters(
					ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA);
			String[] lAnnoDataSentenza = getRequestStringParameters(
					ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA);

			String[] lTipoUfficio = getRequestStringParameters(
					ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA);
			String[] lLuogoUfficio = getRequestStringParameters(
					ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA);

			String[] lAnnoRegePM = getRequestStringParameters(ICostantiContinuazione.CAMPO_ANNO_REGE_PM);
			String[] lNumRegePM = getRequestStringParameters(ICostantiContinuazione.CAMPO_NUM_REGE_PM);

			String[] lAnnoRegeGenerico = getRequestStringParameters("ARG");
			String[] lNumRegeGenerico = getRequestStringParameters("NRG");
			String[] TipoRGGenerico = getRequestStringParameters("TipoRG");

			ContinuazioneModel lCont = null;
			// Viene considerata valida una sentenza in continuazione
			// se ha almeno o
			// l'anno e il numero sentenza
			// o la data sentenza
			// valorizzati
			for (int i = 0; i < 2; i++) {
				if ((lAnnoSentenza[i] != null && !lAnnoSentenza[i].equals("") && lNumSentenza[i] != null
						&& !lNumSentenza[i].equals(""))) {
					lCont = new ContinuazioneModel();

					lCont.setCodTipoContinuazione(lTipoContinuazione[i]);

					if (lAnnoSentenza[i] != null && !lAnnoSentenza[i].equals(""))
						lCont.setAnnoSentenza(new BigDecimal(lAnnoSentenza[i]));
					lCont.setNumSentenza(lNumSentenza[i]);

					lCont.setDataSentenza(DateUtils.getDate(lAnnoDataSentenza[i], lMeseDataSentenza[i],
							lGiornoDataSentenza[i]));

					lCont.setCodTipoAutorita(lTipoUfficio[i]);
					lCont.setCodLuogoAutorita(getCodComuneByDescr(lLuogoUfficio[i]).getCodComune());

					// Controllo esistenza ufficio
					if (lTipoUfficio[i] != null && !lTipoUfficio[i].equals("") && !lTipoUfficio[i].equals("-")
							&& lLuogoUfficio[i] != null && !lLuogoUfficio[i].equals("")
							&& !lLuogoUfficio[i].equals("-")) {
						getCodUfficioByCodTipoUfficioDescrComune(lTipoUfficio[i], lLuogoUfficio[i]);
					}

					if (lAnnoRegePM[i] != null && !lAnnoRegePM[i].equals(""))
						lCont.setAnnoRegePm(new BigDecimal(lAnnoRegePM[i]));
					lCont.setNumRegePm(lNumRegePM[i]);

					if (TipoRGGenerico[i].equalsIgnoreCase("gip")) {
						if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
							lCont.setAnnoRegeGip(new BigDecimal(lAnnoRegeGenerico[i]));
						}
						lCont.setNumRegeGip(lNumRegeGenerico[i]);
					}
					if (TipoRGGenerico[i].equalsIgnoreCase("dib")) {
						if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
							lCont.setAnnoRegeDib(new BigDecimal(lAnnoRegeGenerico[i]));
						}
						lCont.setNumRegeDib(lNumRegeGenerico[i]);
					}
					if (TipoRGGenerico[i].equalsIgnoreCase("cas")) {
						if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
							lCont.setAnnoRegeCas(new BigDecimal(lAnnoRegeGenerico[i]));
						}
						lCont.setNumRegeCas(lNumRegeGenerico[i]);
					}
					if (TipoRGGenerico[i].equalsIgnoreCase("cap")) {
						if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
							lCont.setAnnoRegeCap(new BigDecimal(lAnnoRegeGenerico[i]));
						}
						lCont.setNumRegeCap(lNumRegeGenerico[i]);
					}
					if (TipoRGGenerico[i].equalsIgnoreCase("casap")) {
						if (lAnnoRegeGenerico[i] != null && !lAnnoRegeGenerico[i].equals("")) {
							lCont.setAnnoRegeCasap(new BigDecimal(lAnnoRegeGenerico[i]));
						}
						lCont.setNumRegeCasap(lNumRegeGenerico[i]);
					}

					lCont.setCodOperatoreInserimento(getCodUtenteConnesso());
					lCont.setDataInserimento(DateUtils.getSysDate());
					lCont.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

					mContList.add(lCont);
				}
			}
		} else {
			mPenaCompMod.setFlagPenaInContinuazione("N");
		}
	}

}