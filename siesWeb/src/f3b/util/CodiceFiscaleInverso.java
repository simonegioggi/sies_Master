package f3b.util;

import java.util.HashMap;
import java.util.Map;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.util.AvvocatoUtil;

/**
 * MEV_21: aggiunta classe di utility per calcolare il codice fiscale inverso dell'avvocato
 *
 * @author sgioggi
 * @version 1.0
 */
public class CodiceFiscaleInverso {

	private static final Map<Character, Integer> mesi = new HashMap<>();

	static {
		mesi.put('A', 1);
		mesi.put('B', 2);
		mesi.put('C', 3);
		mesi.put('D', 4);
		mesi.put('E', 5);
		mesi.put('H', 6);
		mesi.put('L', 7);
		mesi.put('M', 8);
		mesi.put('P', 9);
		mesi.put('R', 10);
		mesi.put('S', 11);
		mesi.put('T', 12);
	}

	public static class DatiEstratti {

		public int anno;
		public int mese;
		public int giorno;
		public String sesso;
		public String comune;

		@Override
		public String toString() {

			return "Anno: " + anno + ", Mese: " + mese + ", Giorno: " + giorno + ", Sesso: " + sesso
					+ ", Codice Comune: " + comune;
		}
	}

	public static DatiEstratti estraiDati(String codiceFiscale) throws F3BException {

		if (codiceFiscale == null || codiceFiscale.length() < 16)
			throw new IllegalArgumentException("Codice Fiscale non valido");

		DatiEstratti dati = new DatiEstratti();

		int anno = Integer.parseInt(codiceFiscale.substring(6, 8));
		if (anno < 40)
			anno += 2000;
		else
			anno += 1900;
		char meseChar = codiceFiscale.charAt(8);
		int mese = mesi.getOrDefault(meseChar, 0);
		int giornoRaw = Integer.parseInt(codiceFiscale.substring(9, 11));
		String sesso;
		int giorno;
		if (giornoRaw > 40) {
			sesso = "F";
			giorno = giornoRaw - 40;
		} else {
			sesso = "M";
			giorno = giornoRaw;
		}

		dati.anno = anno;
		dati.mese = mese;
		dati.giorno = giorno;
		dati.sesso = sesso;
		ComuneModel cm = AvvocatoUtil.calcolaComuneNascita(codiceFiscale);
		String descrComune = cm.getDescrizione();
		dati.comune = Utils.isPresent(descrComune) ? descrComune : "-";

		return dati;
	}

}