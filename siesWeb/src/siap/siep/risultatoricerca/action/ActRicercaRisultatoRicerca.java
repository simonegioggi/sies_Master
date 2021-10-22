package siap.siep.risultatoricerca.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.siep.risultatoricerca.controller.IRisultatoRicerca;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaRisultatoRicerca
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di RisultatoRicerca
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActRicercaRisultatoRicerca extends ActionSiap implements ICostantiRisultatoRicerca {

	// Creazione HashMap per il trasporto dei parametri di filtro ricerca
	HashMap<String, Object> mParams = null;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		mParams = new HashMap<>();

		IRisultatoRicerca lCtrl = SIEPLookupRemote.getRisultatoRicercaRemote();
		BigDecimal lIdRisultatoRicerca = null;

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodUtente = getCodUtenteConnesso();

		BigDecimal lAnnoInzio = null;
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE)
				&& getRequestStringParameter(CAMPO_CHIAVE_ANNO_INIZIALE) != null
				&& !getRequestStringParameter(CAMPO_CHIAVE_ANNO_INIZIALE).equals(""))
			lAnnoInzio = this.getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE);

		BigDecimal lNumeroInzio = null;
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE)
				&& getRequestStringParameter(CAMPO_CHIAVE_PROGR_INIZIALE) != null
				&& !getRequestStringParameter(CAMPO_CHIAVE_PROGR_INIZIALE).equals(""))
			lNumeroInzio = getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE);

		BigDecimal lAnnoFine = null;
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE)
				&& getRequestStringParameter(CAMPO_CHIAVE_ANNO_FINALE) != null
				&& !getRequestStringParameter(CAMPO_CHIAVE_ANNO_FINALE).equals(""))
			lAnnoFine = this.getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE);

		BigDecimal lNumeroFine = null;
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE)
				&& getRequestStringParameter(CAMPO_CHIAVE_PROGR_FINALE) != null
				&& !getRequestStringParameter(CAMPO_CHIAVE_PROGR_FINALE).equals(""))
			lNumeroFine = getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE);

		String lDataReato = null;
		String lRequestDataReato = null;
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_REATO)
				&& getRequestStringParameter(CAMPO_ANNO_DATA_REATO) != null
				&& !getRequestStringParameter(CAMPO_ANNO_DATA_REATO).equals("")) {
			lDataReato = getRequestStringParameter(CAMPO_GIORNO_DATA_REATO);
			lDataReato += getRequestStringParameter(CAMPO_MESE_DATA_REATO);
			lDataReato += getRequestStringParameter(CAMPO_ANNO_DATA_REATO);

			// per passare la data già formatta nella request
			lRequestDataReato = getRequestStringParameter(CAMPO_GIORNO_DATA_REATO) + "-";
			lRequestDataReato += getRequestStringParameter(CAMPO_MESE_DATA_REATO) + "-";
			lRequestDataReato += getRequestStringParameter(CAMPO_ANNO_DATA_REATO);

		}

		BigDecimal lAnniRes = null;
		if (!isRequestParameterNullObj(CAMPO_NUM_ANNI_PENA_RES)
				&& getRequestStringParameter(CAMPO_NUM_ANNI_PENA_RES) != null
				&& !getRequestStringParameter(CAMPO_NUM_ANNI_PENA_RES).equals(""))
			lAnniRes = this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_PENA_RES);

		BigDecimal lGiorniRes = null;
		if (!isRequestParameterNullObj(CAMPO_NUM_GIORNI_PENA_RES)
				&& getRequestStringParameter(CAMPO_NUM_GIORNI_PENA_RES) != null
				&& !getRequestStringParameter(CAMPO_NUM_GIORNI_PENA_RES).equals(""))
			lGiorniRes = this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PENA_RES);

		BigDecimal lMesiRes = null;
		if (!isRequestParameterNullObj(CAMPO_NUM_MESI_PENA_RES)
				&& getRequestStringParameter(CAMPO_NUM_MESI_PENA_RES) != null
				&& !getRequestStringParameter(CAMPO_NUM_MESI_PENA_RES).equals(""))
			lMesiRes = this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_PENA_RES);

		BigDecimal lAnniSen = null;
		if (!isRequestParameterNullObj(CAMPO_NUM_ANNI_PENA_SEN)
				&& getRequestStringParameter(CAMPO_NUM_ANNI_PENA_SEN) != null
				&& !getRequestStringParameter(CAMPO_NUM_ANNI_PENA_SEN).equals(""))
			lAnniSen = this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_PENA_SEN);

		BigDecimal lGiorniSen = null;
		if (!isRequestParameterNullObj(CAMPO_NUM_GIORNI_PENA_SEN)
				&& getRequestStringParameter(CAMPO_NUM_GIORNI_PENA_SEN) != null
				&& !getRequestStringParameter(CAMPO_NUM_GIORNI_PENA_SEN).equals(""))
			lGiorniSen = this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PENA_SEN);

		BigDecimal lMesiSen = null;
		if (!isRequestParameterNullObj(CAMPO_NUM_MESI_PENA_SEN)
				&& getRequestStringParameter(CAMPO_NUM_MESI_PENA_SEN) != null
				&& !getRequestStringParameter(CAMPO_NUM_MESI_PENA_SEN).equals(""))
			lMesiSen = this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_PENA_SEN);

		String lDataPr = null;

		BigDecimal lPosAggregata = null;
		if (!isRequestParameterNullObj(CAMPO_POSIZIONE_GIURIDICA_AGGREGATA)
				&& getRequestStringParameter(CAMPO_POSIZIONE_GIURIDICA_AGGREGATA) != null
				&& !getRequestStringParameter(CAMPO_POSIZIONE_GIURIDICA_AGGREGATA).equals(""))
			lPosAggregata = getRequestBigDecimalParameter(CAMPO_POSIZIONE_GIURIDICA_AGGREGATA);

		String lCodPosGiuridica = null;
		if (!this.isRequestParameterNullObj(CAMPO_COD_POSIZIONE_GIURIDICA)
				&& getRequestStringParameter(CAMPO_COD_POSIZIONE_GIURIDICA) != null
				&& !getRequestStringParameter(CAMPO_COD_POSIZIONE_GIURIDICA).equals("-"))
			lCodPosGiuridica = getRequestStringParameter(CAMPO_COD_POSIZIONE_GIURIDICA);

		String lNazione = null;
		if (!isRequestParameterNullObj(ICostantiRisultatoRicerca.CAMPO_COD_NAZIONE)) {
			lNazione = getRequestStringParameter(ICostantiRisultatoRicerca.CAMPO_COD_NAZIONE);
		}

		// passo i parametri di ricerca nell'elenco per poterli visualizzare
		setRequestAttribute("lAnnoInzio", "" + lAnnoInzio);
		setRequestAttribute("lNumeroInzio", "" + lNumeroInzio);
		setRequestAttribute("lAnnoFine", "" + lAnnoFine);
		setRequestAttribute("lNumeroFine", "" + lNumeroFine);
		setRequestAttribute("lDataReato", lRequestDataReato);
		setRequestAttribute("lAnniRes", "" + lAnniRes);
		setRequestAttribute("lMesiRes", "" + lMesiRes);
		setRequestAttribute("lGiorniRes", "" + lGiorniRes);
		setRequestAttribute("lAnniSen", "" + lAnniSen);
		setRequestAttribute("lGiorniSen", "" + lGiorniSen);
		setRequestAttribute("lMesiSen", "" + lMesiSen);
		setRequestAttribute("lPosAggregata", "" + lPosAggregata);
		setRequestAttribute("lNazione", "" + lNazione);

		mParams.put("lAnnoInzio", lAnnoInzio);
		mParams.put("lNumeroInzio", lNumeroInzio);
		mParams.put("lAnnoFine", lAnnoFine);
		mParams.put("lNumeroFine", lNumeroFine);
		mParams.put("lDataReato", lRequestDataReato);
		mParams.put("lAnniRes", lAnniRes);
		mParams.put("lMesiRes", lMesiRes);
		mParams.put("lGiorniRes", lGiorniRes);
		mParams.put("lAnniSen", lAnniSen);
		mParams.put("lGiorniSen", lGiorniSen);
		mParams.put("lMesiSen", lMesiSen);
		mParams.put("lPosAggregata", lPosAggregata);
		mParams.put("lNazione", lNazione);

		String lDescPosGiuridica = null;
		if (lCodPosGiuridica != null)
			lDescPosGiuridica = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione(), lCodPosGiuridica);

		setRequestAttribute("lDescPosGiuridica", lDescPosGiuridica);
		mParams.put("lDescPosGiuridica", lDescPosGiuridica);

		if (isRequestParameterNullObj(ICostantiRisultatoRicerca.CAMPO_ID_RICERCA)
				&& isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			lIdRisultatoRicerca = lCtrl.ExRicercaConStoreProcedure(lCodUtente, lCodUfficio, lAnnoInzio,
					lNumeroInzio, lAnnoFine, lNumeroFine, lDataReato, lAnniRes, lMesiRes, lGiorniRes, lDataPr,
					lPosAggregata, lCodPosGiuridica, lAnniSen, lMesiSen, lGiorniSen, lNazione);

		} else {
			lIdRisultatoRicerca = getRequestBigDecimalParameter(CAMPO_ID_RICERCA);
		}

		// ricerca risultato ricerca della store procedure
		if ((isRequestParameterNullObj(IWebConstants.NUM_PAGE)
				&& isRequestParameterNullObj(ICostantiRisultatoRicerca.CAMPO_ID_RICERCA))
				|| !isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			String lPagina = "1";
			if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
				lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

			Vector lVect = lCtrl.ExRicercaRisultatoRicercaByKeyPage(lIdRisultatoRicerca,
					Integer.parseInt(lPagina));
			setRequestAttribute("risultatoricerca", lVect);

			if (lVect == null || lVect.size() == 0 || lVect.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetCountRicercaRisultatoByKey(lIdRisultatoRicerca);
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL() + "&"
					+ ICostantiRisultatoRicerca.CAMPO_ID_RICERCA + "=" + lIdRisultatoRicerca);

			setRequestAttribute("lIdRisultatoRicerca", "" + lIdRisultatoRicerca);

			setRequestAttribute("elencocompleto", "N");
		} else {

			Vector lVector = lCtrl.ExRicercaCompletoById(lIdRisultatoRicerca);
			setRequestAttribute("risultatoricerca", lVector);

			setRequestAttribute("elencocompleto", "S");

			// passo i parametri di ricerca nell'elnco per poterli visualizzare
			setRequestAttribute("lAnnoInzio", getRequestStringParameter("lAnnoInzio"));
			setRequestAttribute("lNumeroInzio", getRequestStringParameter("lNumeroInzio"));
			setRequestAttribute("lAnnoFine", getRequestStringParameter("lAnnoFine"));
			setRequestAttribute("lNumeroFine", getRequestStringParameter("lNumeroFine"));
			setRequestAttribute("lDataReato", getRequestStringParameter("lDataReato"));
			setRequestAttribute("lAnniRes", getRequestStringParameter("lAnniRes"));
			setRequestAttribute("lMesiRes", getRequestStringParameter("lMesiRes"));
			setRequestAttribute("lGiorniRes", getRequestStringParameter("lGiorniRes"));
			setRequestAttribute("lAnniSen", getRequestStringParameter("lAnniSen"));
			setRequestAttribute("lGiorniSen", getRequestStringParameter("lGiorniSen"));
			setRequestAttribute("lMesiSen", getRequestStringParameter("lMesiSen"));
			setRequestAttribute("lPosAggregata", getRequestStringParameter("lPosAggregata"));
			setRequestAttribute("lDescPosGiuridica", getRequestStringParameter("lDescPosGiuridica"));
			setRequestAttribute("lNazione", getRequestStringParameter("lNazione"));

			mParams.put("lAnnoInzio", getRequestStringParameter("lAnnoInzio"));
			mParams.put("lNumeroInzio", getRequestStringParameter("lNumeroInzio"));
			mParams.put("lAnnoFine", getRequestStringParameter("lAnnoFine"));
			mParams.put("lNumeroFine", getRequestStringParameter("lNumeroFine"));
			mParams.put("lDataReato", getRequestStringParameter("lDataReato"));
			mParams.put("lAnniRes", getRequestStringParameter("lAnniRes"));
			mParams.put("lMesiRes", getRequestStringParameter("lMesiRes"));
			mParams.put("lGiorniRes", getRequestStringParameter("lGiorniRes"));
			mParams.put("lAnniSen", getRequestStringParameter("lAnniSen"));
			mParams.put("lGiorniSen", getRequestStringParameter("lGiorniSen"));
			mParams.put("lMesiSen", getRequestStringParameter("lMesiSen"));
			mParams.put("lPosAggregata", getRequestStringParameter("lPosAggregata"));
			mParams.put("lDescPosGiuridica", getRequestStringParameter("lDescPosGiuridica"));
			mParams.put("lNazione", getRequestStringParameter("lNazione"));

		}

		setSessionAttribute("parametri_ricerca", mParams);

		return PG_RICERCARISULTATORICERCA;
	}
}