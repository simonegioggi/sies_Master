package siap.siep.scambiosanzione.action;

import java.math.BigDecimal;
import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.util.SIEPLookupRemote;

public class ActListaDocumentiSius extends ActionSiap implements ICostantiScambioSanzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

		String lNaturaSS = getRequestStringParameter(CAMPO_NATURA_SS);

		String[] lTipoDecisone = getCodTipoDecisione(lNaturaSS);
		String[] lNaturaSanzione = getCodNaturaSanzione(lNaturaSS);
		String[] lTipoSanzione = getCodTipoSanzione(lNaturaSS);

		IScambioSanzione lCtrlSanzione = SIEPLookupRemote.getScambioSanzionRemote();
		List lListaEventiOrdinanze = lCtrlSanzione.ExRicercaScambioSanzioneByIdFascicoloSiepNaturaTipo(
				lIdFascicoloSiep, lTipoDecisone, lNaturaSanzione, lTipoSanzione);

		setRequestAttribute("documentiSius", lListaEventiOrdinanze);

		return PG_LISTA_DOCUMENTI_SIUS;
	}

	// METODI PRIVATE
	private String[] getCodTipoSanzione(String aNaturaSS) {

		String[] lCodMotivi = new String[0];

		if (aNaturaSS.equals(ANNOTAZIONE)) {
			lCodMotivi = new String[2];
			lCodMotivi[0] = "2080";
			lCodMotivi[1] = "2081";
		} else if (aNaturaSS.equals(REVOCA_CONVERSIONE)) {
			lCodMotivi = new String[3];
			lCodMotivi[0] = "0234";
			lCodMotivi[1] = "0261";
			lCodMotivi[2] = "0262";
		}

		return lCodMotivi;
	}

	private String[] getCodTipoDecisione(String aNaturaSS) {

		String[] lCodTipoDecisione = null;

		if (aNaturaSS.equals(ANNOTAZIONE)) {
			lCodTipoDecisione = new String[2];
			lCodTipoDecisione[0] = "02";
			lCodTipoDecisione[1] = "03";
		} else if (aNaturaSS.equals(REVOCA_CONVERSIONE)) {
			lCodTipoDecisione = new String[1];
			lCodTipoDecisione[0] = "03";
		}

		return lCodTipoDecisione;
	}

	private String[] getCodNaturaSanzione(String lNaturaSS) {

		String[] lCodNatura = new String[1];

		if (lNaturaSS.equals(ANNOTAZIONE)) {
			lCodNatura = new String[5];
			lCodNatura[0] = "AS";
			lCodNatura[1] = "IR";
			lCodNatura[2] = "IT";
			lCodNatura[3] = "NP";
			lCodNatura[4] = "RG";

		} else if (lNaturaSS.equals(REVOCA_CONVERSIONE)) {
			lCodNatura[0] = "CS";
		}

		return lCodNatura;
	}

}