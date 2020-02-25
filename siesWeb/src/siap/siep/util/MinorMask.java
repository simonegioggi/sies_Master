package siap.siep.util;

import siap.sico.ufficio.model.UfficioModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import f3b.util.StringUtils;

public class MinorMask {

	public final static String ComboEmittenteId = "comboEmittenteMinor";
	public final static String ComboCSSAId = "comboCSSAMinor";
	public final static String ComboMagistratoId = "comboMagistratoMinor";
	public final static String ComboTribunaleId = "comboTribunaleMinor";

	public final static String EmittenteTribunale = "EmittenteTribunale";
	public final static String EmittenteUfficio = "EmittenteUfficio";
	// MEV10-s3: aggiunta costante
	public final static String EmittenteUfficioBis = "EmittenteUfficioBis";
	public final static String EmittenteMagistrato = "EmittenteMagistrato";
	public final static String EmittenteAutoritaUff = "EmittenteAutoritaUff";
	public final static String EmittenteAutoritaMag = "EmittenteAutoritaMag";

	public final static String SorveglianzaUfficio = "SorveglianzaUfficio";
	public final static String SorveglianzaMagistrato = "SorveglianzaMagistrato";
	public final static String SorveglianzaTribunale = "SorveglianzaTribunale ";

	/**
	 * @return
	 */
	private static String getComboEmittente(String tipo, String script, String field) {
		StringBuffer sb = new StringBuffer();

		if (EmittenteTribunale.equals(tipo)) {
		} else if (EmittenteUfficio.equals(tipo)) {
		} else if (EmittenteMagistrato.equals(tipo)) {
		} else if (EmittenteAutoritaUff.equals(tipo)) {
		} else if (EmittenteAutoritaMag.equals(tipo)) {
		}

		sb.append("<select name='" + field + "' id='" + ComboEmittenteId + "' title='Emittente' " + script + ">");
		sb.append("<option value = '-' />-");
		// MEV10-s3: aggiunto controllo per caricare i valori nella combo
		if (!EmittenteUfficioBis.equals(tipo)) {
			sb.append("<option value = 'TDS' />TRIBUNALE DI SORVEGLIANZA");
			sb.append("<option value = 'UDS' />MAGISTRATO DI SORVEGLIANZA");
			sb.append("<option value = 'TDSM' />TRIBUNALE PER I MINORENNI IN FUNZIONE DI TRIBUNALE DI SORVEGLIANZA");
		} else
			sb.append("<option value = 'UDS' />UFFICIO DI SORVEGLIANZA");
		// MEV10-s3: modificato valore opzione: ex "UFFICIO DI SORVEGLIANZA PER I MINORENNI"
		sb.append("<option value = 'UDSM' />MAGISTRATO DI SORVEGLIANZA PER I MINORENNI");
		sb.append("</select>");

		return sb.toString();
	}

	/**
	 * @param filter
	 * @return
	 */
	public static String comboEmittente(String filter, String tipo) {
		return comboEmittente(filter, tipo, "");
	}

	public static String comboEmittente(String filter, String tipo, String script) {
		return comboEmittente(filter, tipo, script, ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA);
	}

	public static String comboEmittente(String filter, String tipo, String script, String field) {
		return comboEmittente(filter, tipo, script, ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA, "");
	}

	public static String comboEmittente(String filter, String tipo, String script, String field, String option) {

		StringBuffer sb = new StringBuffer();

		if ("true".equals(filter) && StringUtils.isNullOrWhiteSpace(option)) {
			sb.append(getComboEmittente(tipo, script, field));
		} else if ("true".equals(filter) && !StringUtils.isNullOrWhiteSpace(option)) {
			sb.append("<select name='" + field + "' id='" + ComboEmittenteId + "' title='Emittente' " + script + ">");
			sb.append(option);
			sb.append("<option value = 'TDSM' />TRIBUNALE PER I MINORENNI IN FUNZIONE DI TRIBUNALE DI SORVEGLIANZA");
			// MEV10-s3: modificato valore opzione: ex "UFFICIO DI SORVEGLIANZA PER I MINORENNI"
			sb.append("<option value = 'UDSM' />MAGISTRATO DI SORVEGLIANZA PER I MINORENNI");
			sb.append("</select>");
		} else {
			if (EmittenteTribunale.equals(tipo)) {
				sb.append("<font class='campo'>TRIBUNALE DI SORVEGLIANZA</font>");
				sb.append("<input type='hidden' name='" + field + "' value='TDS'>");
			} else if (EmittenteUfficio.equals(tipo)) {
				sb.append("<font class='campo'>UFFICIO DI SORVEGLIANZA</font>");
				sb.append("<input type='hidden' name='" + field + "' value='UDS'>");
			} else if (EmittenteMagistrato.equals(tipo)) {
				sb.append("<font class='campo'>MAGISTRATO DI SORVEGLIANZA</font>");
				sb.append("<input type='hidden' name='" + field + "' value='TDS'>");
			} else if (EmittenteAutoritaUff.equals(tipo)) {

				sb.append("<select name='" + field + "' id='" + ComboEmittenteId + "' title='Emittente' " + script + ">");

				if (option != null && !"".equals(option.trim())) {
					sb.append(option);
				} else {
					sb.append("<option value = '-' />-");
					sb.append("<option value = 'TDS' />Tribunale di Sorveglianza");
					sb.append("<option value = 'UDS' />Ufficio di Sorveglianza");
				}

				sb.append("</select>");

			} else if (EmittenteAutoritaMag.equals(tipo)) {

				sb.append("<select name='" + field + "' id='" + ComboEmittenteId + "' title='Emittente' " + script + ">");
				if (option != null && !"".equals(option.trim())) {
					sb.append(option);
				} else {
					sb.append("<option value = '-' />-");
					sb.append("<option value = 'TDS' />Tribunale di Sorveglianza");
					sb.append("<option value = 'UDS' />Magistrato di Sorveglianza");
				}

				sb.append("</select>");

			}
		}
		return sb.toString();
	}

	/**
	 * @param vect
	 * @return
	 */
	private static String getCombo(String[][] vect) {
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='" + vect[0][2] + "' id='" + vect[0][1] + "' title='" + vect[0][0] + "'>");
		// sb.append("<option value = '-' />-");
		int i = 1;
		while (i < vect.length) {
			sb.append("<option value = '" + vect[i][0] + "' />" + vect[i][1] + "");
			i++;
		}
		sb.append("</select>");
		return sb.toString();
	}

	/**
	 * @param filter
	 * @return
	 */
	public static String comboCSSA(String filter) {
		return comboCSSA(filter, "");
	}

	public static String comboCSSA(String filter, String idField) {
		String ret = "";
		if ("true".equals(filter)) {

			if ("".equals(idField)) {
				idField = ComboCSSAId;
			}

			String[][] vect = { { "Ufficio", idField, idField }, { "UEPE", "UEPE" }, { "USSM", "USSM" } };
			ret = getCombo(vect);
		} else {
			ret = "UEPE";
		}
		return ret;
	}

	/**
	 * @param filter
	 * @return
	 */
	public static String comboMagistrato(String filter) {
		return comboMagistrato(filter, "");
	}

	public static String comboMagistrato(String filter, String tipo) {
		String ret = "";
		if ("true".equals(filter)) {
			String[][] vect = null;

			if (SorveglianzaUfficio.equals(tipo)) {
				// SorveglianzaUfficio
				vect = new String[][] { { "Ufficio", ComboMagistratoId, ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO },
						{ "UDS", "Ufficio di Sorveglianza" }, { "UDSM", "Ufficio di Sorveglianza per i minorenni" } };
			} else {
				// SorveglianzaMagistrato
				vect = new String[][] { { "Magistrato", ComboMagistratoId, ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO },
						{ "UDS", "Magistrato di Sorveglianza" }, { "UDSM", "Magistrato di Sorveglianza per i minorenni" } };
			}
			ret = getCombo(vect);
		} else {
			if (SorveglianzaUfficio.equals(tipo)) {
				// SorveglianzaUfficio
				ret = "Ufficio di Sorveglianza";
			} else {
				// SorveglianzaMagistrato
				ret = "Magistrato di Sorveglianza";
			}
		}
		return ret;
	}

	/**
	 * @param filter
	 * @return
	 */
	public static String comboTribunale(String filter) {
		return comboTribunale(filter, "");
	}

	public static String comboTribunale(String filter, String tipo) {
		String ret = "";
		if ("true".equals(filter)) {
			String[][] vect = null;

			// if (SorveglianzaTribunale.equals(tipo)) {
			// SorveglianzaTribunale
			vect = new String[][] { { "Tribunale", ComboTribunaleId, ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE }, { "TDS", "Tribunale di Sorveglianza" }, { "TDSM", "Tribunale per i  Minorenni in funzione di Tribunale di Sorveglianza" } };
			// }

			ret = getCombo(vect);
		} else {
			ret += "<input type='hidden' value='TDS' name='" + ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE + "'>";
			ret += "Tribunale di Sorveglianza";
		}
		return ret;
	}

	public static String dettaglioMagistrato(UfficioModel model, String lCodTipoUfficio) {
		StringBuffer sb = new StringBuffer();

		// String descUfficio = "UFFICIO DI SORVEGLIANZA";
		// if ("UDSM".equals(model.getCodTipoUfficio())) {
		// descUfficio += " PER I MINORENNI";
		// }
		// MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente
    	String descrTipoUfficio = model.getDescrTipoUfficio();
    	String codiceTipoUfficio = model.getCodTipoUfficio();
    	if (("PM".equals(lCodTipoUfficio) || "PMM".equals(lCodTipoUfficio) || "PGCAP".equals(lCodTipoUfficio)) &&
    			"UDSM".equals(codiceTipoUfficio)) {
    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
    	}
		sb.append("<font class='campo'>" + descrTipoUfficio + "</font>");
		sb.append(" di ");
		sb.append("<font class='campo'>" + model.getDescrComune() + "</font>");

		return sb.toString();
	}

	public static String dettaglioTribunale(UfficioModel model) {
		StringBuffer sb = new StringBuffer();

		// String descUfficio = "TRIBUNALE DI SORVEGLIANZA";
		// if ("TDSM".equals(model.getCodTipoUfficio())) {
		// descUfficio += " PER I MINORENNI";
		// }
		sb.append("<font class='campo'>" + model.getDescrTipoUfficio() + "</font>");
		sb.append(" di ");
		sb.append("<font class='campo'>" + model.getDescrComune() + "</font>");

		return sb.toString();
	}

	public static String minorCondition(String aliasSoggetto) {
		return " and (nvl(" + aliasSoggetto + ".eta_soggetto_ora, nvl(" + aliasSoggetto + ".eta_presunta_anni, 18)) >= 18 ) ";
	}

	public static String minorCondition(String aliasSoggetto, String codUfficio) {
		return " and (nvl(" + aliasSoggetto + ".eta_soggetto_ora, nvl(" + aliasSoggetto + ".eta_presunta_anni, 18)) >= 18 or soggetto.cod_ufficio_inserimento = '" + codUfficio + "' ) ";
	}

	public static String minorCondition(String aliasSoggetto, String aliasFascicolo, String codUfficio) {
		return " and ( (nvl(" + aliasSoggetto + ".eta_ora, 18) >= 18 ) or (" + aliasFascicolo + ".cod_ufficio_inserimento = '" + codUfficio + "') ) ";
	}

	public static String minorConditionPGCAP(String aliasSoggetto, String aliasFascicolo, String codUfficio) {
		return " and ( (nvl(" + aliasSoggetto + ".eta_ora, 18) < 18 ) or (" + aliasFascicolo + ".cod_ufficio_inserimento = '" + codUfficio + "') ) ";
	}

	/**
	 * MEV10-s3: aggiunto metodo con aggiunta del trattino nelle opzioni
	 * 
	 * @param idField
	 * @return String
	 */
	public static String comboCSSATrattino(String idField) {
		String[][] vect = { { "Ufficio", idField, idField }, { "-", "-" }, { "UEPE", "UEPE" }, { "USSM", "USSM" } };
		// valore di ritorno
		return getCombo(vect);
	}

	/**
	 * MEV10-s3: aggiunto metodo con aggiunta del trattino nelle opzioni
	 * 
	 * @return String
	 */
	public static String comboMagistratoTrattino() {
		String[][] vect = new String[][] { { "Ufficio", ComboMagistratoId, ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO },
				{ "-", "-" },
				{ "UDS", "Ufficio di Sorveglianza" },
				{ "UDSM", "Magistrato di Sorveglianza per i minorenni" } };
		// valore di ritorno
		return getCombo(vect);
	}

	/**
	 * MEV10-s3: aggiunto metodo con aggiunta del trattino nelle opzioni
	 * 
	 * @return String
	 */
	public static String comboTribunaleTrattino() {
			String[][] vect = new String[][] { { "Tribunale", ComboTribunaleId, ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE },
					{ "-", "-" },
					{ "TDS", "Tribunale di Sorveglianza" },
					{ "TDSM", "Tribunale per i  Minorenni in funzione di Tribunale di Sorveglianza" } };
			// valore di ritorno
			return getCombo(vect);
	}

}