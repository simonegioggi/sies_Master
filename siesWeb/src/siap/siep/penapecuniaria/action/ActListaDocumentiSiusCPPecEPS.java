package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.scambiosanzione.action.ICostantiScambioSanzione;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.util.SIEPLookupRemote;

public class ActListaDocumentiSiusCPPecEPS extends ActionSiap
		implements ICostantiPenaPecuniaria, ICostantiScambioSanzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		Vector[] lScaSanTipPro = new Vector[2];
		Vector lScambioSanzioneRC = new Vector();
		Vector lCodTipProv = new Vector();

		IScambioSanzione lCtrlSanzione = SIEPLookupRemote.getScambioSanzionRemote();
		String[] TipoDec = { "02", "03" };

		lScaSanTipPro = lCtrlSanzione.ExRicercaScambioSanzioneRichConvEPS(TipoDec, lIdFascicoloSiep);
		lScambioSanzioneRC = lScaSanTipPro[0];
		lCodTipProv = lScaSanTipPro[1];

		// ScambioSanzioneRichiestaConvModel lScaRic = new ScambioSanzioneRichiestaConvModel();

		// Iterator itx = lScambioSanzioneRC.iterator();
		// while (itx.hasNext()) {
		// lScaRic = (ScambioSanzioneRichiestaConvModel) itx.next();
		// }

		if (lScambioSanzioneRC.size() == 0) {
			// lScaRic = new ScambioSanzioneRichiestaConvModel();
			setRequestAttribute("lScaSanzRC", lScambioSanzioneRC);
			setRequestAttribute("lCodTipPro", lCodTipProv);
			return PG_LISTA_DOCUMENTI_SIUS_CP_PEC_EPS;
		} else {
			setRequestAttribute("lScaSanzRC", lScambioSanzioneRC);
			setRequestAttribute("lCodTipPro", lCodTipProv);
			return PG_LISTA_DOCUMENTI_SIUS_CP_PEC_EPS;
		}
	}

}