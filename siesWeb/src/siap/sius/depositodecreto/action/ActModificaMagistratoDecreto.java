package siap.sius.depositodecreto.action;

import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActModificaMagistratoDecreto extends ActionSiap implements ICostantiDepositoDecreto {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("processRequest() - start");
		// prepara i dati per la modifica.
		DepositoDecretoModel lDepDecr = new DepositoDecretoModel();
		
		lDepDecr.setIdEventoGenerato(getRequestBigDecimalParameter(CAMPO_ID_EVENTO_GENERATO));
		lDepDecr.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
		lDepDecr.setCodOperatoreAggiornamento(super.getCodUtenteConnesso());
		lDepDecr.setCodUfficioAggiornamento(super.getCodUfficioUtenteConnesso());
		lDepDecr.setDataAggiornamento(new Date());
		
		IDepositoDecreto lctrl = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoModel lDepDecrMod = lctrl.ExModificaMagistratoDecreto(lDepDecr);
		
		RedirectTo lret = new RedirectTo();
		lret.setPage(IWebConstants.PG_MAIN);
		lret.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito");
		lret.setParameter("IdEvento", lDepDecrMod.getIdEventoGenerato().toPlainString());
		lret.setParameter(IWebConstants.LINK_RITORNO, "20");
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("processRequest() - stop");
		
		return lret.toString();
	}
}