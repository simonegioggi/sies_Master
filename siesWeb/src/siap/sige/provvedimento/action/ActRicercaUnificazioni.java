package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActRicercaProvvedimentii
 * </p>
 * <p>
 * Description: Azione specializzazione per la ricerca dei Provvedimenti legati al fascicolo SIGE.
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
public class ActRicercaUnificazioni extends ActionSige implements ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		// Gestione del punto di ritorno
		this.setLinkRitorno();

		Vector<ProvvedimentoSigeEventoModel> lVect = null;
		BigDecimal lIdFascicolo = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActRicercaUnificazioni : inizio");

		// Fascicolo SIGE Esteso.
		FascicoloSigeEstesoModel lFasEsteso = new FascicoloSigeEstesoModel();
		// String
		// idFascicolo=(String)super.getRequest().getParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca Provvedimendi da ID FASCICOLO->" + lIdFascicolo);
			IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
			lFasEsteso = lCtrlFas.ExRicercaEstesaFascicoloSigeByKey(lIdFascicolo);

		} else {
			lFasEsteso = this.getFascicoloSigeEstesoInSessione();
			lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca Provvedimendi da sessione");
		}

		ProvvedimentoSigeModel lProvSige = new ProvvedimentoSigeModel();
		lProvSige.setFasIdFascicoloSige(lIdFascicolo);
		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();

		// lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(lIdFascicolo, TIPI_PROVVEDIMENTI);
		// lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(lIdFascicolo, TIPI_PROVVEDIMENTI_DM);
		// @emma 13072018 intervento post COLLAUDO 11.2
		String lCodProvvSige = "'02'";
		lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvvProvvSige(lIdFascicolo, lCodProvvSige,
				TIPI_PROVVEDIMENTI_UNIFICAZIONI);
		setRequestAttribute("provvedimenti", lVect);

		IImpugnazioneSige ctrIS = SIGELookupRemote.getImpugnazioneSigeRemote();
		Vector<ImpugnazioneSigeModel> impugnazioniProvvedimento = new Vector<>();
		Vector<ImpugnazioneSigeModel> impugnazioniProvvedimenti = new Vector<>();
		for (ProvvedimentoSigeEventoModel lProvEve : lVect) {

			impugnazioniProvvedimento = ctrIS.ExRicercaImpugnazioniProvvedimentoSige(
					lProvEve.getProvvedimento().getIdProvvedimentoSige());
			if (impugnazioniProvvedimento != null && impugnazioniProvvedimento.size() > 0)
				impugnazioniProvvedimenti.addElement(impugnazioniProvvedimento.firstElement());
		}
		setRequestAttribute("impugnazioni", impugnazioniProvvedimenti);
		// fine Ricerca

		// Controlla se il fascicolo e' modificabile (in questo contesto la modificabilità va impostata per
		// tutti
		// i fascicoli di competenza, tranne quelli con provvedimento definitorio con deposito validato).
		String lModificabile = "NO";

		FascicoloSigeUtils lFasUtil = new FascicoloSigeUtils();

		if (getCodUfficioUtenteConnesso()
				.equalsIgnoreCase(lFasEsteso.getFascicoloSige().getChiaveUfficio())) {
			if (lFasUtil.HasFascicoloSigeProvvDefinitorioConDepositoValidato(lIdFascicolo))
				lModificabile = "NO";
			else
				lModificabile = "SI";
		}

		setRequestAttribute("isModificabile", lModificabile);
		// @emma 13072018 intervento post COLLAUDO 11.2
		setRequestAttribute("tipoRicerca", "unificazioni");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActRicercaUnificazioni : fine");
		return PG_ELENCOPROVVEDIMENTI;
	}

}