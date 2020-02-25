package siap.sius.depositosentenza.action;

import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActModificaMagistratoSentenza extends ActionSiap implements ICostantiDepositoSentenza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("processRequest() - start");
		
		// prepara i dati per la modifica.
		DepositoSentenzaModel lDepSen = new DepositoSentenzaModel();
		lDepSen.setIdEventoGenerato(getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ID_EVENTO_GENERATO));
		lDepSen.setCodMagistrato(getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_MAGISTRATO));
		lDepSen.setCodOperatoreAggiornamento(super.getCodUtenteConnesso());
		lDepSen.setCodUfficioAggiornamento(super.getCodUfficioUtenteConnesso());
		lDepSen.setDataAggiornamento(new Date());

		IDepositoSentenza lctrl = SIUSLookupRemote.getDepositoSentenzaRemote();
		DepositoSentenzaModel lDepSenMod = lctrl.ExModificaMagistratoSentenza(lDepSen);
		
		RedirectTo lret = new RedirectTo();
		lret.setPage(IWebConstants.PG_MAIN);
		lret.setAction("siap.sius.depositosentenza.action.ActLoadDettaglioSentenza");
		lret.setParameter("IdEvento", lDepSenMod.getIdEventoGenerato().toPlainString());
		lret.setParameter(IWebConstants.LINK_RITORNO, "20");
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("processRequest() - stop");
		
		return lret.toString();
	}
}