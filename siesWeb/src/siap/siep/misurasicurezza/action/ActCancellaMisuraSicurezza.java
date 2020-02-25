package siap.siep.misurasicurezza.action;

/**
 * <p>Title: ActLoadCancellaSoggetto</p>
 * <p>Description: Azione Load del Cancella Soggetto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di cancellazione di Misura Sicurezza
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// riempie il model
		MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel();

		lMisMod.setIdMisuraSicurezza(getRequestBigDecimalParameter(CAMPO_ID_MISURA_SICUREZZA));

		// chiama il controller
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lMisMod = lCtrl.ExRicercaMisuraSicurezzaByKey(lMisMod.getIdMisuraSicurezza());

		// Ricerca il Fascicolo Siep per metterlo in sessione
		// nel caso la Cancellazione venga richiamata dall'Elenco di procedimenti con Misure Sicurezza
		IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFasMod = lCtrlFasc.ExRicercaFascicoloByKey(lMisMod.getFasSieIdFascicoloSiep());

		if (lFasMod != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lFasMod " + lFasMod);

			setRequestAttribute("fascicolo", lFasMod);
			setSessionAttribute("fascicolo", lFasMod);
		}
		// ---------------------------------------------------------------------------------------------------

		lCtrl.ExCancellaMisuraSicurezza(lMisMod);

		List lListMis = new ArrayList();
		try {
			lListMis = lCtrl.ExRicercaMisuraSicurezzaByIdFascicolo(lFasMod.getIdFascicoloSiep());
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Eccezione in ActCancellaMisuraSicurezza " + e);
		}

		if (!this.isRequestParameterNullObj(IWebConstants.LINK_RITORNO)) {
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "="
					+ this.getRequestStringParameter(IWebConstants.LINK_RITORNO);
		} else if (lListMis.size() == 0) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lFasMod.getIdFascicoloSiep();
			return lPage;
		} else {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misurasicurezza.action.ActRicercaMisuraSicurezza&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lFasMod.getIdFascicoloSiep();

			return lPage;
		}
	}

}