package siap.sius.fascicolo.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciDefinizioneProcedimento
 * </p>
 * <p>
 * Description: Classe Azione di definizione Fascicolo SIUS.
 * </p>
 * In base ai dati nella request viene chiamato l'Update delle tabelle: </p> Fascicolo_SIUS e
 * Generale_procedimento. </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class ActInserisciDefinizioneProcedimento extends ActionSiap implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();

		IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

		// Istanzio il Model e lo carico con quello posto in sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Dati da aggiornare in Fascicolo SIUS
		lFasGPMod.getFascicoloSiusModel().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																								// dell'operatore
																								// che
																								// inserisce
		lFasGPMod.getFascicoloSiusModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																										// dell'operatore
																										// che
																										// inserisce
		lFasGPMod.getFascicoloSiusModel().setDataAggiornamento(DateUtils.getSysDate());
		lFasGPMod.getFascicoloSiusModel().setDataDefinizione(
				getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE, CAMPO_MESE_DATA_DEFINIZIONE,
						CAMPO_GIORNO_DATA_DEFINIZIONE));
		lFasGPMod.getFascicoloSiusModel().setCodStatoFascicolo(COD_DEFINITO);
		// Dati da aggiornare in Generale Procedimento
		lFasGPMod.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lFasGPMod.getGeneraleProcedimentoModel().setDataAggiornamento(DateUtils.getSysDate());
		lFasGPMod.getGeneraleProcedimentoModel().setDataDefinizione(
				lFasGPMod.getFascicoloSiusModel().getDataDefinizione());
		lFasGPMod.getGeneraleProcedimentoModel().setTipoDefinizione(
				getRequestStringParameter(CAMPO_TIPO_DEFINIZIONE));
		lFasGPMod.getGeneraleProcedimentoModel().setDescrDefinizione(
				getRequestStringParameter(CAMPO_DESCR_DEFINIZIONE));
		// Viene richiamato il Controller per eseguire l'Update
		lFasCtrl.ExInserisciDefinizioneFascicoloSius(lFasGPMod);

		// Prepara la "pagina" di dettaglio
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sius.fascicolo.action.ActLoadDefinizioneProcedimento");
		lRedirigi.setParameter(CAMPO_CHIAVE_ANNO, lFasGPMod.getFascicoloSiusModel().getChiaveAnno()
				.toString());
		lRedirigi.setParameter(CAMPO_CHIAVE_PROGR, lFasGPMod.getFascicoloSiusModel().getChiaveProgr()
				.toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");
		return lRedirigi.toString();
	}

}