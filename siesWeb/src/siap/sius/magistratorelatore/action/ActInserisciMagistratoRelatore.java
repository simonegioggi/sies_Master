package siap.sius.magistratorelatore.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.sius.esperto.action.ICostantiEsperto;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInserisciMagistratoRelatore
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di MagistratoRelatore
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

public class ActInserisciMagistratoRelatore extends ActionSiap implements ICostantiMagistratoRelatore {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del MagistratoRelatore.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {
		// Generale.
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciEmissioneOrdinanza: inizio");
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// MAGISTRATO Relatore
		String lCheck = getRequestStringParameter("magistratoMod"); // valorizzato a YES se magistrato nuovo
		String lCheckEsp = getRequestStringParameter("espertoMod"); // valorizzato a YES se esperto nuovo
		if (lCheck.compareTo("YES") == 0 || lCheckEsp.compareTo("YES") == 0) {
			MagistratoRelatoreModel lMagistrato = new MagistratoRelatoreModel();
			// Dati nuovo Magistrato.
			lMagistrato
					.setMagCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			lMagistrato.setEspIdEsperto(getRequestBigDecimalParameter(ICostantiEsperto.CAMPO_ID_ESPERTO));
			lMagistrato.setFasSiuIdFascicoloSius(lIdFasSius);
			lMagistrato.setDataInizio(DateUtils.getSysDate());
			lMagistrato.setCodRuoloMagistrato("02");
			lMagistrato.setDataInserimento(DateUtils.getSysDate());
			lMagistrato.setCodOperatoreInserimento(getCodUtenteConnesso());
			lMagistrato.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			IMagistratoRelatore lCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
			/* MagistratoRelatoreModel lMagModRet = */lCtrl.ExInserisciMagistratoRelatore(lMagistrato);
		}

		// Prepara la pagina di destinazione, il Dettaglio.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.magistratorelatore.action.ActLoadDettaglioMagistratoRelatore");
		lPage.setParameter(IWebConstants.LINK_RITORNO, "10");
		return lPage.toString();
	}

}