package siap.sius.permesso.action;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.permesso.model.CriteriRicercaProvPermessiLicenzeModel;
import siap.sius.permesso.util.PermessoUtils;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActStampaProvvedimentiPremessiLicenze - Classe Azione responsabile della richiesta stampa elenco
 * provvedimenti permessi o licenza.
 *
 * @version 1.0
 */
public class ActStampaProvvedimentiPremessiLicenze extends ActionSius implements ICostantiPermesso {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

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
		lCriteri.setCodMotivo(PermessoUtils.getCodMotivo(lTipoRicerca));
		lCriteri.setCodUfficio(super.getCodUfficioUtenteConnesso());

		IPermesso lCtrl = SIUSLookupRemote.getPermessoRemote();
		ByteArrayOutputStream lReport = lCtrl.ExStampaProvvedimentiPermessiLicenze(lCriteri,
				super.getUtenteConnesso());

		// Prepara la pagina di destinazione.
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return IWebConstants.PG_DOWNLOAD;
	}

}