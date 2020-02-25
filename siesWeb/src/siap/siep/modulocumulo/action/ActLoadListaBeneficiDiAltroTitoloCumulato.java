package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadListaBeneficiDiAltroTitoloCumulato
 * </p>
 * <p>
 * Description: Classe Action Load elenco dei procedimenti
 * </p>
 * <p>
 * con Benefici associati all'Istruttoria_Cumulo
 * </p>
 * <p>
 * (Gestione Revoca Benefici in Cumulo
 * </p>
 */

public class ActLoadListaBeneficiDiAltroTitoloCumulato extends ActionModuloCumulo
		implements ICostantiBeneficiCumulo {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		BigDecimal lIdIstruttoriaCorrente = this
				.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		String lTipoForm = getRequestStringParameter(TIPO_FORM_BENEFICIO);

		// cerca tutti i Titoli Cumulati con benefici correlati all'Istruttoria
		String TipoBen = "";
		if (lTipoForm.equals(TIPO_FORM_SOSPENSIONE))
			TipoBen = "IN('01', '02')";
		else if (lTipoForm.equals(TIPO_FORM_INDULTO))
			TipoBen = "= '03'";

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		Vector lTitoli = lCtrlT.ExRicercaTitoloCumulatoPerRevocaBeneficio(lIdTitolo, lIdIstruttoriaCorrente,
				TipoBen);
		setRequestAttribute("TitoliConBenefici", lTitoli);
		setRequestAttribute(TIPO_FORM_BENEFICIO, lTipoForm);

		return PG_POPUP_ELENCO_BENEFICI_CUMULO;
	}

}