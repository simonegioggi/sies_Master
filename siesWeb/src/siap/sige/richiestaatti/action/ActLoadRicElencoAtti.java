package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadRicElencoAtti
 * </p>
 * <p>
 * Description: Azione specializzata per la ricerca degli Atti Istruttori legati al fascicolo SIGE.
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadRicElencoAtti extends ActionSige implements ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// Gestione del punto di ritorno
		this.setLinkRitorno();

		BigDecimal lIdFascicolo = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadRicElencoAtti : inizio");

		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
		} else {
			FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();
			lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
		}
		ProvvedimentoSigeModel lProvSige = new ProvvedimentoSigeModel();
		lProvSige.setFasIdFascicoloSige(lIdFascicolo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerca Provvedimendi da ID FASCICOLO->" + lIdFascicolo);
		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
		String lTipiProvv = "'" + ICostantiProvvedimentoSige.COD_ISTRUTTORIE + "'";
		// String lTipiProvv = "'52','11'";
		Vector<ProvvedimentoSigeEventoModel> lVect = mCtrl
				.ExRicercaProvvSigePerIdFasSigeTipiProvv(lIdFascicolo, lTipiProvv);
		setRequestAttribute("atti", lVect);

		// IEvento lCtrl = SICOLookupRemote.getEventoRemote();

		// Controlla se il fascicolo e' modificabile
		String lModificabile = "NO";

		if (IsFascicoloSigeModificabile() == true)
			lModificabile = "M";
		else
			lModificabile = "NO";

		setRequestAttribute("modalita", lModificabile);
		// setRequestAttribute("modalita", "M");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadRicElencoAtti : fine");
		return PG_ELENCOPROVVEDIMENTI;
	}

}