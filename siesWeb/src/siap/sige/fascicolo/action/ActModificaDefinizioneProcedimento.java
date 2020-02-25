package siap.sige.fascicolo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActModificaDefinizioneProcedimento
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActModificaDefinizioneProcedimento extends ActionSige implements ICostantiFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();

		String lRetPage = PG_LOAD_DEFINIZIONE_PROCEDIMENTO; // pagina di input
		String lmodalita = "modifica";

		// il Fascicolo si ricava dalla sessione
		FascicoloSigeModel lFascicolo = getFascicoloSigeInSessione();
		// Lock
		lockApplicativoFascicoloSige();

		Vector lVect = null;
		ProvvedimentoSigeEventoModel provvSigeEveMod = null;
		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
		String lTipiProvv = "'" + ICostantiProvvedimentoSige.COD_ANNOTAZIONE + "'"; // CODICE
																					// PROVVEDIMENTO_SIGE.
		lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(lFascicolo.getIdFascicoloSige(), lTipiProvv);
		if (lVect != null && lVect.size() > 0) {
			provvSigeEveMod = (ProvvedimentoSigeEventoModel) lVect.firstElement();
		}
		setRequestAttribute("provvedimento", provvSigeEveMod);

		// Costruzione dell'Option filtrata dal Codice tipo Ufficio (UDS o TDS)
		Option lOption = new Option(DecodificheUtils.getDecodificheFiltrateByCodAlt(
				DecodificheManager.getInstance().getTipoDefinzioneSiepe(),
				getUfficioUtenteConnesso().getCodTipoUfficio()));
		lOption.setSelected(lFascicolo.getCodTipoDefinizione());

		// Valorizzazione dei dati nella request
		setRequestAttribute("FlagFasSiepe", "SI"); // Imposta il flag per abilitare la visualizzazione dei
													// dati di sintesi SIEPE
		setRequestAttribute("TipoDefinizione", "" + lOption);
		setRequestAttribute("modalita", lmodalita);
		setRequestAttribute("descrizione", lFascicolo.getDescrDefinizione());
		setRequestAttribute("data_definizione", lFascicolo.getDataDefinizione());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRetPage;
	}

}