package siap.siepe.fascicolo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActLoadModificaFascicoloSiepe
 * </p>
 * <p>
 * Description: Azione di caricamento della form di Modifica dati del fascicolo
 * </p>
 * <p>
 * Copyright: Bull Italia Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia
 * </p>
 */
public class ActLoadModificaFascicoloSiepe extends ActionSiap implements ICostantiFascicoloSiepe {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo di processRequest, per la load della JSP di Modifica.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 * @return String ritorna la JSP di modifica.
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): inizio");

		// Recupera l'id del fascicolo dalla request.
		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEPE);

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "FASCICOLO_SIEPE",
				lId.toString(), getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "L' " + lck.getEntity()
					+ " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Recupera il fascicolo dal DB.
		IFascicoloSiepe lCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
		FascicoloSiepeModel lFascicoloSiepe = lCtrl.ExRicercaFascicoloSiepeByKey(lId);

		// Inserisce nella Request il fascicolo SIEPE precedentemente recuperato.
		setRequestAttribute("fascicolosiepe", lFascicoloSiepe);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_MODIFICA_FASCICOLO_SIEPE; // restituisce la jsp di VIEW
	}

}