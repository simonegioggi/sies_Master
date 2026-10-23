// 20261023-MEV_2026-105_INLAY - MEV Legge 140/2026
package siap.sico.magistratocompetente.action;

import f3b.util.F3BException;

/**
 * MEV_2026-105 - Utilita' di validazione dei criteri di riassegnazione massiva.
 */
public final class RiassegnazioneMassivaUtil {

	public static final String TIPO_COGNOME = "COGNOME";
	public static final String TIPO_PROGRESSIVO = "PROGRESSIVO";

	private RiassegnazioneMassivaUtil() {
	}

	/**
	 * Normalizza il valore del criterio: per COGNOME restituisce le lettere iniziali (A-Z) senza duplicati,
	 * per PROGRESSIVO le cifre finali (0-9) senza duplicati, separate da virgola.
	 */
	public static String normalizzaValore(String aTipo, String aValore) throws F3BException {
		if (!TIPO_COGNOME.equals(aTipo) && !TIPO_PROGRESSIVO.equals(aTipo)) {
			throw new F3BException(F3BException.USER_MESSAGE, "Selezionare il tipo di criterio");
		}
		boolean lettere = TIPO_COGNOME.equals(aTipo);
		String valore = aValore == null ? "" : aValore.toUpperCase();
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < valore.length(); i++) {
			char c = valore.charAt(i);
			if (c == ',' || c == ';' || Character.isWhitespace(c)) {
				continue;
			}
			boolean ok = lettere ? (c >= 'A' && c <= 'Z') : (c >= '0' && c <= '9');
			if (!ok) {
				throw new F3BException(F3BException.USER_MESSAGE, lettere
						? "Indicare solo lettere (A-Z) iniziali del cognome del condannato"
						: "Indicare solo cifre da 0 a 9 finali del numero progressivo");
			}
			if (out.indexOf(String.valueOf(c)) < 0) {
				if (out.length() > 0) {
					out.append(",");
				}
				out.append(c);
			}
		}
		if (out.length() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE, "Indicare il valore del criterio di selezione");
		}
		return out.toString();
	}

	public static String descrizioneTipo(String aTipo) {
		return TIPO_PROGRESSIVO.equals(aTipo) ? "Cifra finale del progressivo"
				: "Lettera iniziale del cognome del condannato";
	}
}
