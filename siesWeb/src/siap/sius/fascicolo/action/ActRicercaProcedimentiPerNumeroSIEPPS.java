package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fascicolo.penasostitutiva.ElencoAnnotazioniPenaSostitutivaController;

// 20261124-MEV_2026-124_SG - Elenco Procedimenti della Sorveglianza e del GE: per i procedimenti privi di provvedimenti
// (es. EPS) ma con annotazioni dell'esecuzione della pena sostitutiva, mostra comunque l'elenco con le annotazioni
public class ActRicercaProcedimentiPerNumeroSIEPPS extends ActRicercaProcedimentiPerNumeroSIEP {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {
		String lReturnPage = super.processRequest();
		if (ICostantiFascicoloSius.PG_RICERCA_PROCEDIMENTIPERNUMEROSIEP.equals(lReturnPage)
				|| lReturnPage == null || !lReturnPage.startsWith(IWebConstants.PG_MAIN))
			return lReturnPage;

		BigDecimal lIdFascicoloSiep = null;
		if (!this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP))
			lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		else if (!this.isSessionAttributeNullObj("fascicolo"))
			lIdFascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

		if (lIdFascicoloSiep != null && !(this.getCodUtenteConnesso().startsWith("D")
				|| this.getCodUtenteConnesso().startsWith("E"))
				&& !new ElencoAnnotazioniPenaSostitutivaController().exRicerca(lIdFascicoloSiep).isEmpty()) {
			setRequestAttribute("misuraaggregato", new Vector());
			setRequestAttribute("fascicoli", new Vector());
			return ICostantiFascicoloSius.PG_RICERCA_PROCEDIMENTIPERNUMEROSIEP;
		}
		return lReturnPage;
	}
}
