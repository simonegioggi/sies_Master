package siap.siep.scambiosanzione.action;

import java.math.BigDecimal;
import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>Title: ActListaProvvedimentiPeneSostitutiveSius</p>
 * <p>Description: Popup dei provvedimenti SIUS delle pene sostitutive da annotare in SIEP</p>
 * @author SG
 * @since 20261124-MEV_2026-124_SG
 */
public class ActListaProvvedimentiPeneSostitutiveSius extends ActionSiap implements ICostantiScambioSanzione {

	// Applicazione pena sostitutiva
	private static final String[] MOTIVI_ANNOTAZIONE = { "2900", "2901", "3180", "3181" }; // 20261127-MEV_2026-124_SG conversione pene pecuniarie in pena sostitutiva
	// Conversione e revoca pena sostitutiva
	private static final String[] MOTIVI_REVOCA_CONVERSIONE = { "2910", "2911", "2912", "2913", "2914", "2935",
			"2936", "2937", "2938", "2940", "2941", "3200", "3190" };

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		String lNaturaSS = getRequestStringParameter(CAMPO_NATURA_SS);

		String[] lMotivi = REVOCA_CONVERSIONE_PENE_SOSTITUTIVE.equals(lNaturaSS) ? MOTIVI_REVOCA_CONVERSIONE
				: MOTIVI_ANNOTAZIONE;

		IScambioSanzione lCtrl = SIEPLookupRemote.getScambioSanzionRemote();
		List lProvvedimenti = lCtrl.ExRicercaProvvedimentiPenaSostitutiva(lIdFascicoloSiep, lMotivi);

		setRequestAttribute("documentiSius", lProvvedimenti);

		return PG_LISTA_DOCUMENTI_SIUS;
	}
}
