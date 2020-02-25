package siap.sius.permesso.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.permesso.model.CriteriRicercaProvPermessiLicenzeModel;
import siap.sius.permesso.model.TotaliPermessiLicenzeModel;
import siap.sius.permesso.util.PermessoUtils;
import siap.sius.util.SIUSLookupRemote;

public class ActRelazioneTrimestralePermessoLicenza extends ActionSius implements ICostantiPermesso {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("inizio");

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		super.setLinkRitorno();

		Date lDataIniziale = super.getRequestDateParameter(
				ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO_INIZIALE,
				ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO_INIZIALE,
				ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_INIZIALE);

		Date lDataFinale = super.getRequestDateParameter(
				ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO_FINALE,
				ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO_FINALE,
				ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO_FINALE);

		String lTipoRicerca = super.getRequestStringParameter("tipoRicerca");

		CriteriRicercaProvPermessiLicenzeModel lCriteri = new CriteriRicercaProvPermessiLicenzeModel();

		lCriteri.setDataDepositoIniziale(lDataIniziale);
		lCriteri.setDataDepositoFinale(lDataFinale);
		lCriteri.setDescrTipoRicerca(getDescrTipoRicerca(lTipoRicerca));
		lCriteri.setCodMotivo(getCodMotivo(lTipoRicerca));
		lCriteri.setCodUfficio(super.getCodUfficioUtenteConnesso());

		IPermesso lCtrl = SIUSLookupRemote.getPermessoRemote();
		Collection lColl = lCtrl.ExRicercaProvvedimentiPermessiLicenze(lCriteri);

		// Paginazione
		int CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetNumProvvedimentiPermessiLicenze(lCriteri);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("CountRisultati : " + CountRisultati);
		} else
			CountRisultati = getRequestIntParameter("CountRisultati");

		setRequestAttribute("CountRisultati", new BigDecimal("" + CountRisultati));
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// *** Recupera i Totali ***//
		TotaliPermessiLicenzeModel lTotali = new TotaliPermessiLicenzeModel();
		lTotali = lCtrl.ExGetTotProvvedimentiPermessiLicenze(lCriteri);

		super.setRequestAttribute("elenco", lColl);
		super.setRequestAttribute("totali", lTotali);
		super.setRequestAttribute("criteriRicerca", lCriteri);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("fine");

		return PG_RICERCA_PROVVEDIMENTI_PERMESSOLICENZA; // restituisce la jsp di VIEW
	}

	private String getDescrTipoRicerca(String aValue) {
		return PermessoUtils.getDescrTipoRicerca(aValue);
	}

	private String getCodMotivo(String aValue) {
		return PermessoUtils.getCodMotivo(aValue);
	}
}