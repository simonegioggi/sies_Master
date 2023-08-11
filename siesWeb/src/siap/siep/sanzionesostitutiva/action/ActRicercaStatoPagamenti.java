package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-33: Action per la ricerca dello stato dei pagamenti
 *
 * @author df
 * @version 1.0
 */
public class ActRicercaStatoPagamenti extends ActionSiap
		implements ICostantiSanzioneSostitutiva, ICostantiFascicoloSiep {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		siesLogger.debug("ActRicercaStatoPagamenti....");
		this.setLinkRitorno();

		if (this.isSessionAttributeNullObj("fascicolo")) {
			setRequestAttribute("fascicoloNotInSession", "S");
		}

		// =======================================================
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			lFasMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			lFasMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			lFasMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			lFasMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

		// Data iscrizione Iniziale
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_INIZIALE)) {
			lFasMod.setDataIscrizioneIniziale(
					getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE,
							ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE,
							ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE));

		}

		// Data iscrizione Finale
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_FINALE)) {
			lFasMod.setDataIscrizioneFinale(
					getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE,
							ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE,
							ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE));
		}

		String tipoRicerca = getRequestStringParameter(
				ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_STATO_PAGAMENTO);

		String chiaveUfficio = getCodUfficioUtenteConnesso();
		lFasMod.setChiaveUfficio(chiaveUfficio);

		//
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		ISanzioneSostitutiva lCtrlSanzSost = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		Vector lVect = lCtrlSanzSost.ExRicercaFascicoliPerStatoPagamentoPaged(lFasMod,
				Integer.parseInt(lPagina), tipoRicerca);

		// Solo per test
		// lVect.add(getSessionAttribute("fascicolo"));

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrlSanzSost.ExGetCountRicercaFascicoliPerStatoPagamento(lFasMod, tipoRicerca);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		String descTipoRicerca = "";
		if (tipoRicerca.equals(CAMPO_TIPO_RICERCA_INTERAMENTE_PAGATO))
			descTipoRicerca = "Procedimenti con pena pecuniaria totalmente pagata";
		else if (tipoRicerca.equals(CAMPO_TIPO_RICERCA_RETEIZZATO_NON_PAGATO))
			descTipoRicerca = "Procedimenti con pagamento rateizzato con rate non pagate";
		else if (tipoRicerca.equals(CAMPO_TIPO_RICERCA_UNICA_RATA_NON_PAGATO))
			descTipoRicerca = "Procedimenti con pagamento in unica soluzione non pagata";

		setRequestAttribute("criteriRicerca", lFasMod);
		setRequestAttribute("descTipoRicerca", descTipoRicerca);
		setRequestAttribute("elencoFascicoli", lVect);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		// Lancio la ricerca
		return PG_ESITO_RICERCA_STATO_PAGAMENTI;
	}

}