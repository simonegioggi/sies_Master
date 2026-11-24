package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.scadenzario.penasostitutiva.ScadenzarioPenaSostitutivaController;
import f3b.web.IWebConstants;

// 20261124-MEV_2026-124_SG - ricerca scadenzario pene sostitutive scadute
public class ActRicercaScadenzarioPenaSostitutiva extends ActionSiap {

	public static final String PG_RICERCA = IWebConstants.ROOT_DIR
			+ "files/siap/siep/scadenzario/RicercaScadenzarioPenaSostitutiva.jsp";

	public String processRequest() throws Exception {
		String lTipo = getRequestStringParameter("tipo");
		String lTitolo = "Tutti";
		int lAnni = 0, lMesi = 0, lGiorni = 0;
		if (ScadenzarioPenaSostitutivaController.TIPO_OGGI.equals(lTipo)) {
			lTitolo = "In Scadenza Oggi";
		} else if (ScadenzarioPenaSostitutivaController.TIPO_SCADUTI.equals(lTipo)) {
			lTitolo = "Scadute";
		} else if (ScadenzarioPenaSostitutivaController.TIPO_ENTRO.equals(lTipo)) {
			lTitolo = "In Scadenza";
			lAnni = toInt(getRequestBigDecimalParameter("AnniScadenza"));
			lMesi = toInt(getRequestBigDecimalParameter("MesiScadenza"));
			lGiorni = toInt(getRequestBigDecimalParameter("GiorniScadenza"));
		}
		List lRighe = new ScadenzarioPenaSostitutivaController().exRicerca(getCodUfficioUtenteConnesso(), lTipo,
				lAnni, lMesi, lGiorni);
		setRequestAttribute("scadenzario", lRighe);
		setRequestAttribute("tipo", lTipo);
		setRequestAttribute("titolo", lTitolo);
		return PG_RICERCA;
	}

	private int toInt(BigDecimal aValue) {
		return aValue == null ? 0 : aValue.intValue();
	}
}
