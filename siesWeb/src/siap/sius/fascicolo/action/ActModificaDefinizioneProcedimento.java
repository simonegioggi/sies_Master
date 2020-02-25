package siap.sius.fascicolo.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 *
 * <p>
 * Title: ActModificaDefinizioneProcedimento
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActModificaDefinizioneProcedimento extends ActionSius implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();

		String lRetPage = PG_LOAD_DEFINIZIONE_PROCEDIMENTO; // pagina di input
		FascicoloGPModel lFasGPMod = null;
		String lmodalita = "modifica";

		// il Fascicolo è in sessione
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati del Fascicolo non in sessione!");

		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (lFasGPMod == null || lFasGPMod.getFascicoloSiusModel() == null
				|| lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nei dati del Fascicolo in sessione!");

		// Lock per evitare più definizioni contemporanee del Fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS", lFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius().toString(), getCodUtenteConnesso(),
				getSession().getId());
		if (lck != null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Il  " + lck.getEntity()
					+ " è in gestione ad un altro utente!<BR>Riprovare più tardi !");

		IGeneraleProcedimento lGenProcCtrl = SIUSLookupRemote.getGeneraleProcedimentoRemote();
		GeneraleProcedimentoModel lGenProc = lGenProcCtrl.ExRicercaGeneraleProcedimentoByFascicolo(lFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius());

		// Costruzione dell'Option filtrata dal Codice tipo Ufficio (UDS o TDS)
		Option lOption = new Option(DecodificheUtils.getDecodificheFiltrateByCodAlt(DecodificheManager
				.getInstance().getTipoDefinizione(), getUfficioUtenteConnesso().getCodTipoUfficio()));
		lOption.setSelected(lGenProc.getTipoDefinizione());
		// valorizzazione dei dati nella request
		setRequestAttribute("TipoDefinizione", "" + lOption);
		setRequestAttribute("modalita", lmodalita);
		setRequestAttribute("descrizione", lGenProc.getDescrDefinizione());
		setRequestAttribute("data_definizione", lGenProc.getDataDefinizione());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRetPage;
	}

}