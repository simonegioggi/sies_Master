package siap.sius.depositoordinanzapc.action;

import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActModificaMagistratoOrdinanza extends ActionSiap implements ICostantiDepositoOrdinanzaPc {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("processRequest() - start");
		// prepara i dati per la modifica.
		DepositoOrdinanzaPcModel lDepOrd = new DepositoOrdinanzaPcModel();
		lDepOrd.setIdEventoGenerato(getRequestBigDecimalParameter(CAMPO_ID_EVENTO_GENERATO));
		lDepOrd.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
		lDepOrd.setCodOperatoreAggiornamento(super.getCodUtenteConnesso());
		lDepOrd.setCodUfficioAggiornamento(super.getCodUfficioUtenteConnesso());
		lDepOrd.setDataAggiornamento(new Date());
		
		IDepositoOrdinanzaPc lctrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepositoOrdinanzaPcModel lDepOrdMod = lctrl.ExModificaMagistratoOrdinanza(lDepOrd);
		
		RedirectTo lret = new RedirectTo();
		lret.setPage(IWebConstants.PG_MAIN);
		lret.setAction("siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");
		lret.setParameter("IdEvento", lDepOrdMod.getIdEventoGenerato().toPlainString());
		lret.setParameter(IWebConstants.LINK_RITORNO, "20");
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("processRequest() - stop");
		
		return lret.toString();
	}
}