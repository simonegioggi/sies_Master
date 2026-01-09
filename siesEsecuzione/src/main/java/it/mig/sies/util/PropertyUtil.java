package it.mig.sies.util;

import it.mig.sies.model.MisuraSicurezza;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PropertyUtil {

	/**
	 * Verifica la presenza di un BigInteger
	 */
	public static boolean isPresent(BigInteger i) {
		if (i == null)
			return false;
		else
			return isPresent(i.intValue());
	}

	/**
	 * Verifica la presenza di un int, dove il valore 0 significa non presente
	 */
	public static boolean isPresent(int i) {
		if (i == 0)
			return false;
		else
			return true;
	}

	/*
	 * Metodo che verifica la presenza di una misura di sicurezza. E' necessario il controllo di tutti i campi
	 * in caso di una sola misura perchï¿½ per come ï¿½ strutturata la query di estrazione potrebbe comunque
	 * tornare un oggetto MisuraSicurezza con tutti i campi a NULL
	 */
	public static boolean checkMisureSicurezza(List<MisuraSicurezza> misuraSicurezzaList) {
		boolean check = true;
		if ((misuraSicurezzaList == null) || (misuraSicurezzaList.size() == 0)) {
			check = false;
		}

		else {
			if (misuraSicurezzaList.size() == 1) {
				MisuraSicurezza ms = misuraSicurezzaList.getFirst();
				if (ms == null) {
					check = false;
				} else {
					if ((ms.getAnni() == 0) && (ms.getAnniOld() == 0) && (ms.getCodice() == null)
							&& (ms.getCodiceOld() == null) && (ms.getGiorni() == 0)
							&& (ms.getGiorniOld() == 0) && (ms.getMesi() == 0) && (ms.getMesiOld() == 0)) {
						check = false;
					}

				}

			}
		}

		return check;
	}

	// MEV 23010
	public static boolean isPresent(byte[] estratto) {
		if ((estratto == null) || (estratto.length == 0))
			return false;
		else
			return true;
	}

	// MEV 23010
	public static boolean isPresent(String string) {
		return (string != null && string.length() > 0);
	}

	/**
	 * MEV 16 CUMULO: aggiunto metodo di controllo
	 */
	public static boolean isPresent(Object o) {
		return o != null;
	}

	/**
	 * MEV 16 CUMULO: aggiunti metodi
	 * 
	 * @param data
	 * @param pattern
	 * @return String
	 */
	public static String getDateToString(Date data, String pattern) {

		DateFormat df = new SimpleDateFormat(pattern, Locale.ITALY);

		// valore di ritorno
		return df.format(data);
	}

	/**
	 * Verifica la presenza di un List
	 * 
	 * @param l
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isPresent(List l) {

		// valore di ritorno
		return l != null && !l.isEmpty();
	}

}