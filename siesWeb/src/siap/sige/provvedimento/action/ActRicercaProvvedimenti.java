package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.impugnazione.action.ICostantiImpugnazioneSige;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * ActRicercaProvvedimenti - Azione specializzata per la ricerca dei Provvedimenti legati al fascicolo SIGE
 * <<<<<<< HEAD
 *
 * =======
 *
 * >>>>>>> MEV_2024_092_FASE-1
 *
 * @version 1.0
 */
public class ActRicercaProvvedimenti extends ActionSige implements ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Gestione del punto di ritorno
		this.setLinkRitorno();

		Vector<ProvvedimentoSigeEventoModel> lVect = null;
		BigDecimal lIdFascicolo = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActRicercaProvvedimenti : inizio");

		// Fascicolo SIGE Esteso.
		FascicoloSigeEstesoModel lFasEsteso = new FascicoloSigeEstesoModel();

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
		lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(lIdFascicolo, TIPI_PROVVEDIMENTI_DM);

		setRequestAttribute("provvedimenti", lVect);

		// Ricerca dell'eventuale prima impugnazione valida per ciascun provvedimento
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
		// tutti i fascicoli di competenza, tranne quelli con provvedimento definitorio con deposito
		// validato).
		String lModificabile = "NO";
		boolean isFascModificabile = false;

		FascicoloSigeUtils lFasUtil = new FascicoloSigeUtils();

		if (getCodUfficioUtenteConnesso()
				.equalsIgnoreCase(lFasEsteso.getFascicoloSige().getChiaveUfficio())) {
			// @emma - inizio 10/11/2016 introdotto nuovo controllo per rendere modificabile il fascicolo
			// viene richiesto che se per il provvedimento definitorio esiste un'opposizione/ricorso con esito
			// uguale ad Accoglie (Fissa udienza) (COD_TENORE_DECISIONE = 10)
			// o Converte Ricorso in Opposizione (COD_TENORE_DECISIONE = 12)
			// deve diventare MODIFICABILE (anche in caso di annullamento dell'opposizione deve essere
			// modificabile)
			ProvvedimentoSigeEventoModel provvDefin = lFasUtil
					.getProvvDefinitorioConDepositoValidatoByFascicolo(lIdFascicolo);
			if (provvDefin != null && !provvDefin.getImpugnazioni().isEmpty()) {
				List<ImpugnazioneSigeModel> impugn = provvDefin.getImpugnazioni();
				for (Iterator iterator = impugn.iterator(); iterator.hasNext();) {
					ImpugnazioneSigeModel impugnazioneSigeModel = (ImpugnazioneSigeModel) iterator.next();
					if (impugnazioneSigeModel.getCodTenoreDecisione() != null && (impugnazioneSigeModel
							.getCodTenoreDecisione()
							.equals(ICostantiImpugnazioneSige.COD_ESITO_ACCOGLIE_FISSA_UDIENZA)
							|| impugnazioneSigeModel.getCodTenoreDecisione().equals(
									ICostantiImpugnazioneSige.COD_ESITO_CONVERTE_RICORSO_IN_OPPOSIZIONE))) {
						isFascModificabile = true;
					}
				}
			}
			// @emma - fine

			if (lFasUtil.HasFascicoloSigeProvvDefinitorioConDepositoValidato(lIdFascicolo)
					&& !isFascModificabile)
				lModificabile = "NO";
			else
				lModificabile = "SI";
		}
		setRequestAttribute("isModificabile", lModificabile);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActRicercaProvvedimenti : fine");

		// pagina di ritorno
		return PG_ELENCOPROVVEDIMENTI;
	}

}