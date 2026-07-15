package siap.sico.soggetto.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import f3b.util.Utils;

/**
 * Classe di aiuto per la gestione dei codici fiscali
 *
 * @author sgioggi
 * @version 1.0
 */
public class CodiceFiscale {

	/**
	 * Rappresenta l'esito del controllo di validit‡ Fornisce informazioni sulla validit‡ assoluta, sulla
	 * validit‡ formale e sulla forzabilit‡ del codice
	 *
	 * @author sgioggi
	 * @version 1.0
	 */
	public class EsitoControllo {

		private boolean toBeForced;
		private boolean valid;
		private boolean formallyValid;

		public boolean isToBeForced() {
			return toBeForced;
		}

		public void setToBeForced(boolean toBeForced) {
			this.toBeForced = toBeForced;
		}

		public boolean isValid() {

			return valid;
		}

		public void setValid(boolean valid) {

			this.valid = valid;
		}

		public boolean isFormallyValid() {

			return formallyValid;
		}

		public void setFormallyValid(boolean formallyValid) {

			this.formallyValid = formallyValid;
		}
	}

	final static String consonanti = "BCDFGHJKLMNPQRSTVWXYZ";
	final static String vocali = "AEIOU";
	final static String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	final static String mesi = "ABCDEHLMPRST";
	final static String vocaliTedesche = "ƒÀœ÷‹";

	private String nome, cognome, comune, dataNascita;
	private char sesso;
	private Hashtable<String, String> valoriOmocodici;
	int[][] matricecod;
	int[] indiceCarattNumerico;

	private static Logger log = Logger.getLogger(CodiceFiscale.class);

	private String thisCF;

	// Costruttore
	public CodiceFiscale() {

		creaValoriOmocodici();
		creaMatricePerCalcoloCaratteriCtrl();
		creaIndiciCaratteriNumerici();
	}

	/**
	 * Costruttore parametrizzato
	 *
	 * @param cognome
	 *            il cognome dell'utente
	 * @param nome
	 *            il nome dell'utente
	 * @param sesso
	 *            il sesso dell'utente
	 * @param giorno
	 *            il giorno di nascita dell'utente
	 * @param mese
	 *            il mese di nascita dell'utente
	 * @param anno
	 *            l'anno di nascita dell'utente
	 * @param codiceCatastoComune
	 *            il codice catastale del comune di nascita dell'utente
	 * @param codiceCatastoStato
	 *            il codice catastale dello stato di nascita dell'utente
	 */
	public CodiceFiscale(String cognome, String nome, char sesso, String giorno, String mese, String anno,
			String codiceCatastoComune, String codiceCatastoStato) {

		creaValoriOmocodici();
		creaMatricePerCalcoloCaratteriCtrl();
		creaIndiciCaratteriNumerici();
		this.cognome = cognome.toUpperCase();
		this.nome = nome.toUpperCase();
		this.sesso = ("" + sesso).toUpperCase().charAt(0);
		if (!Utils.isPresent(giorno))
			giorno = "01";
		if (!Utils.isPresent(mese))
			mese = "01";

		if (giorno.length() == 1)
			giorno = "0" + giorno;
		if (mese.length() == 1)
			mese = "0" + mese;

		this.dataNascita = giorno + "/" + mese + "/" + anno;
		if (Utils.isPresent(codiceCatastoComune))
			this.comune = codiceCatastoComune;
		else
			this.comune = codiceCatastoStato;

		this.thisCF = getCodiceFiscale();
	}

	private void creaIndiciCaratteriNumerici() {

		indiceCarattNumerico = new int[] { 14, 13, 12, 10, 9, 7, 6 };
	}

	private void creaValoriOmocodici() {

		valoriOmocodici = new Hashtable<>();

		valoriOmocodici.put("0", "L");
		valoriOmocodici.put("1", "M");
		valoriOmocodici.put("2", "N");
		valoriOmocodici.put("3", "P");
		valoriOmocodici.put("4", "Q");
		valoriOmocodici.put("5", "R");
		valoriOmocodici.put("6", "S");
		valoriOmocodici.put("7", "T");
		valoriOmocodici.put("8", "U");
		valoriOmocodici.put("9", "V");
	}

	// matrice x il calcolo del carattere di controllo
	private void creaMatricePerCalcoloCaratteriCtrl() {

		matricecod = new int[91][2];
		matricecod[0][1] = 1;
		matricecod[0][0] = 0;
		matricecod[1][1] = 0;
		matricecod[1][0] = 1;
		matricecod[2][1] = 5;
		matricecod[2][0] = 2;
		matricecod[3][1] = 7;
		matricecod[3][0] = 3;
		matricecod[4][1] = 9;
		matricecod[4][0] = 4;
		matricecod[5][1] = 13;
		matricecod[5][0] = 5;
		matricecod[6][1] = 15;
		matricecod[6][0] = 6;
		matricecod[7][1] = 17;
		matricecod[7][0] = 7;
		matricecod[8][1] = 19;
		matricecod[8][0] = 8;
		matricecod[9][1] = 21;
		matricecod[9][0] = 9;
		matricecod[10][1] = 1;
		matricecod[10][0] = 0;
		matricecod[11][1] = 0;
		matricecod[11][0] = 1;
		matricecod[12][1] = 5;
		matricecod[12][0] = 2;
		matricecod[13][1] = 7;
		matricecod[13][0] = 3;
		matricecod[14][1] = 9;
		matricecod[14][0] = 4;
		matricecod[15][1] = 13;
		matricecod[15][0] = 5;
		matricecod[16][1] = 15;
		matricecod[16][0] = 6;
		matricecod[17][1] = 17;
		matricecod[17][0] = 7;
		matricecod[18][1] = 19;
		matricecod[18][0] = 8;
		matricecod[19][1] = 21;
		matricecod[19][0] = 9;
		matricecod[20][1] = 2;
		matricecod[20][0] = 10;
		matricecod[21][1] = 4;
		matricecod[21][0] = 11;
		matricecod[22][1] = 18;
		matricecod[22][0] = 12;
		matricecod[23][1] = 20;
		matricecod[23][0] = 13;
		matricecod[24][1] = 11;
		matricecod[24][0] = 14;
		matricecod[25][1] = 3;
		matricecod[25][0] = 15;
		matricecod[26][1] = 6;
		matricecod[26][0] = 16;
		matricecod[27][1] = 8;
		matricecod[27][0] = 17;
		matricecod[28][1] = 12;
		matricecod[28][0] = 18;
		matricecod[29][1] = 14;
		matricecod[29][0] = 19;
		matricecod[30][1] = 16;
		matricecod[30][0] = 20;
		matricecod[31][1] = 10;
		matricecod[31][0] = 21;
		matricecod[32][1] = 22;
		matricecod[32][0] = 22;
		matricecod[33][1] = 25;
		matricecod[33][0] = 23;
		matricecod[34][1] = 24;
		matricecod[34][0] = 24;
		matricecod[35][1] = 23;
		matricecod[35][0] = 25;
	}

	public boolean isValidCodiceFiscale(String codFiscToCtrl) {

		boolean isValid = false;

		if (codFiscToCtrl != null)
			codFiscToCtrl = codFiscToCtrl.toUpperCase();

		if ((codFiscToCtrl != null) && (!codFiscToCtrl.equals(""))) {
			String codFiscStandard = getCodiceFiscale();
			if (codFiscStandard != null && !codFiscStandard.equals("")) {
				isValid = (codFiscToCtrl.substring(0, 11)).equals(codFiscStandard.substring(0, 11));

				if (!isValid) {
					isValid = isFormattazioneOk(codFiscStandard, codFiscToCtrl)
							&& isOmocodice(codFiscStandard, codFiscToCtrl);
				}
			}
		}
		return isValid;
	}

	public String[] generaCodFiscValidi() {

		String[] codiciValidi = new String[8];

		codiciValidi[0] = getCodiceFiscale();
		String appoCodice = getCodiceFiscale();

		for (int i = 0; i < indiceCarattNumerico.length; i++) {
			int indiceCarNum = indiceCarattNumerico[i];
			String valueAttuale = appoCodice.substring(indiceCarNum, indiceCarNum + 1);

			String valueDaSost = valoriOmocodici.get(valueAttuale);

			String appo = appoCodice.substring(0, indiceCarNum) + valueDaSost
					+ appoCodice.substring(indiceCarNum + 1);
			appo = appo.substring(0, appo.length() - 1) + generaCarattereControllo(appo, 0);
			codiciValidi[i + 1] = appo;

			appoCodice = codiciValidi[i + 1];

		}
		return codiciValidi;
	}

	private boolean isFormattazioneOk(String codFiscStandard, String codFiscToCtrl) {

		boolean isFormattazioneOk = false;

		if ((codFiscToCtrl != null) && (codFiscToCtrl.length() == 16)) {
			String primoBloccoCarStandard = codFiscStandard.substring(0, 6);
			String primoBloccoCarToCtrl = codFiscToCtrl.substring(0, 6);
			boolean isPrimoBloccoOk = primoBloccoCarStandard.equals(primoBloccoCarToCtrl);

			String secondoBloccoCarStandard = codFiscStandard.substring(8, 9);
			String secondoBloccoCarToCtrl = codFiscToCtrl.substring(8, 9);
			boolean isSecondoBloccoOk = secondoBloccoCarStandard.equals(secondoBloccoCarToCtrl);

			String terzoBloccoCarStandard = codFiscStandard.substring(11, 12);
			String terzoBloccoCarToCtrl = codFiscToCtrl.substring(11, 12);
			boolean isTerzoBloccoOk = terzoBloccoCarStandard.equals(terzoBloccoCarToCtrl);

			isFormattazioneOk = (isPrimoBloccoOk && isSecondoBloccoOk && isTerzoBloccoOk);
		}

		return isFormattazioneOk;
	}

	private boolean isOmocodice(String codiceStandard, String codiceOmonimo) {

		boolean isOmocodice = true;

		for (int i = 0; i < indiceCarattNumerico.length && isOmocodice; i++) {
			int indexCarNum = indiceCarattNumerico[i];
			char valoreStandard = codiceStandard.charAt(indexCarNum);
			char valoreDaCtrl = codiceOmonimo.charAt(indexCarNum);
			char valoreOmonimo = valoriOmocodici.get(String.valueOf(valoreStandard)).charAt(0);
			isOmocodice = verificaCaratteri(valoreStandard, valoreDaCtrl, valoreOmonimo);
		}

		if (isOmocodice) {
			char carattereChkOmonimo = codiceOmonimo.charAt(15);
			char carattereChkCorretto = generaCarattereControllo(codiceOmonimo, 0).charAt(0);

			if (carattereChkCorretto != carattereChkOmonimo) {
				isOmocodice = false;
			}
		}

		return isOmocodice;
	}

	private boolean verificaCaratteri(char valoreStandard, char valoreDaCtrl, char valoreOmonimo) {

		boolean isValid = false;

		isValid = (valoreDaCtrl == valoreStandard);
		if (!isValid)
			isValid = (valoreDaCtrl == valoreOmonimo);

		return isValid;
	}

	public static String calcolaCognome(String cogn) {

		// restituisce i 3 caratteri del codice derivati dal cognome //
		cogn = cogn.toUpperCase();
		// innanzi tutto chiamo il metodo che sostituisce le vocali con dieresi
		// con quelle senza
		cogn = replaceDieresi(cogn);
		int i = 0;
		String stringa = "";
		// trova consonanti
		while ((stringa.length() < 3) && (i + 1 <= cogn.length())) {
			if (consonanti.indexOf(cogn.charAt(i)) > -1) {
				stringa += cogn.charAt(i);
			}
			i++;
		}
		i = 0;
		// se non bastano prende vocali
		while ((stringa.length() < 3) && (i + 1 <= cogn.length())) {
			if (vocali.indexOf(cogn.charAt(i)) > -1) {
				stringa += cogn.charAt(i);
			}
			i++;
		}
		// se non bastano aggiungo le x
		if (stringa.length() < 3) {
			for (i = stringa.length(); i < 3; i++) {
				stringa += "X";
			}
		}
		return stringa;
	}

	public static String calcolaNome(String nom) {

		// restituisce i 3 caratteri del codice derivati dal nome //
		nom = nom.toUpperCase();
		// innanzi tutto chiamo il metodo che sostituisce le vocali con dieresi
		// con quelle senza
		nom = replaceDieresi(nom);
		int i = 0;
		String stringa = "", cons = "";
		// trova consonanti
		while ((cons.length() < 4) && (i + 1 <= nom.length())) {
			if (consonanti.indexOf(nom.charAt(i)) > -1) {
				cons += nom.charAt(i);
			}
			i++;
		}
		// se sono + di 3 prende 1∞ 3∞ 4∞
		if (cons.length() > 3) {
			stringa = cons.substring(0, 1) + cons.substring(2, 4);
			return stringa;
		} else {
			stringa = cons;
		}
		i = 0;
		// se non bastano prende vocali
		while ((stringa.length() < 3) && (i + 1 <= nom.length())) {
			if (vocali.indexOf(nom.charAt(i)) > -1) {
				stringa += nom.charAt(i);
			}
			i++;
		}
		// se non bastano aggiungo le x
		if (stringa.length() < 3) {
			for (i = stringa.length(); i < 3; i++) {
				stringa += "X";
			}
		}
		return stringa;

	}

	String noAccentate(String s) {

		// noAccentate //
		// restituisce la stringa s trasformando le lettere accentate in non
		// accentate //
		// ad esempio "andÚ" viene trasformata "ando" //
		final String ACCENTATE = "¿»…Ã“Ÿ‡ËÈÏÚ˘";
		final String NOACCENTO = "AEEIOUAEEIOU";
		int i = 0;
		// scorre la stringa originale
		while (i < s.length()) {
			int p = ACCENTATE.indexOf(s.charAt(i));
			// se ha trovato una lettera accentata
			if (p > -1) {
				// sostituisce con la relativa non accentata
				s = s.substring(0, i) + NOACCENTO.charAt(p) + s.substring(i + 1);
			}
			i++;
		}

		return s;
	}

	private boolean datiCreazCodiceOk() {

		boolean parametriSufficienti = false;
		parametriSufficienti = ((nome != null) && (!nome.equals("")) && (cognome != null)
				&& (!cognome.equals("")) && (dataNascita != null) && (!dataNascita.equals("")));
		return parametriSufficienti;
	}

	public String getCodiceFiscale() {

		String codice = "";

		if (datiCreazCodiceOk()) {
			String nome = this.nome.toUpperCase();
			String cognome = this.cognome.toUpperCase();
			int anno = 0, mese = 0, giorno = 0, codcontrollo = 0;
			Date data = null;
			// calcola data
			SimpleDateFormat formatodata = new SimpleDateFormat("dd/MM/yyyy");
			try {
				data = formatodata.parse(dataNascita);

				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(data);
				String a = Integer.toString(cal.get(GregorianCalendar.YEAR));
				a = a.substring(a.length() - 2, a.length());
				anno = Integer.parseInt(a);
				mese = cal.get(GregorianCalendar.MONTH);
				giorno = cal.get(GregorianCalendar.DATE);

				codice = calcolaCognome(noAccentate(cognome.trim())) + calcolaNome(noAccentate(nome.trim()));
				if (sesso == 'F') {
					giorno = giorno + 40;
				}
				codice += ((anno < 10) ? "0" : "") + Integer.toString(anno) + mesi.charAt(mese)
						+ ((giorno < 10) ? "0" : "") + Integer.toString(giorno);
				codice += comune;

				for (int i = 0; i < 15; i++) {
					codcontrollo += matricecod[Character.getNumericValue(codice.charAt(i))][(i + 1) % 2];
				}
				codice += alfabeto.charAt(codcontrollo % 26);
			} catch (ParseException ex) {
				log.error("ParseException", ex);
			}
		}
		return codice;
	}

	public String generaCarattereControllo(String codice, int codcontrollo) {

		for (int i = 0; i < 15; i++) {
			codcontrollo += matricecod[Character.getNumericValue(codice.charAt(i))][(i + 1) % 2];
		}
		return ("" + alfabeto.charAt(codcontrollo % 26));
	}

	public void setCognome(String s) {
		cognome = new String(s);
	}

	public void setNome(String s) {
		nome = new String(s);
	}

	public void setDataNascita(String s) {
		dataNascita = new String(s);
	}

	public void setSesso(String s) {
		if (s != null)
			sesso = s.toUpperCase().charAt(0);
	}

	public void setComune(String s) {
		comune = s.toUpperCase();
	}

	/**
	 * Effettua la sostituzione delle vocali con dieresi con quelle senza ES: M‹ÀL ---> MUEL
	 *
	 * @param str
	 * @return Stringa
	 */
	private static String replaceDieresi(String str) {

		int i = 0;
		String stringa = "";
		while (i + 1 <= str.length()) {
			stringa += String.valueOf(str.charAt(i));
			if (vocaliTedesche.indexOf(str.charAt(i)) > -1) {
				stringa = stringa.replace(str.charAt(i),
						vocali.charAt(vocaliTedesche.indexOf(str.charAt(i))));
			}
			i++;
		}
		return stringa;
	}

	public boolean isEqualCFFirstSixDigit(String codFiscToCtrl, String codFiscStandard) {

		boolean isValid = false;
		if (codFiscToCtrl != null)
			codFiscToCtrl = codFiscToCtrl.toUpperCase();
		if (Utils.isPresent(codFiscToCtrl)) {
			if (Utils.isPresent(codFiscStandard)) {
				isValid = (codFiscToCtrl.substring(0, 6)).equals(codFiscStandard.substring(0, 6));
				if (!isValid)
					isValid = isFormattazioneOk(codFiscStandard, codFiscToCtrl)
							&& isOmocodice(codFiscStandard, codFiscToCtrl);
			}
		}
		return isValid;
	}

	/**
	 * Controlla la validit‡ del codice fiscale
	 *
	 * @param userCF
	 *            il codice fiscale da controllare
	 * @return un'istanza di EsitoControllo
	 */
	public EsitoControllo check(String userCF) {

		EsitoControllo esito = this.new EsitoControllo();

		// inizializzo il flag
		boolean isValid = false;
		boolean isFormallyValid = false;
		boolean isToBeForced = false;

		// se l'utente non ha inserito il codice fiscale,
		// considero superato il controllo
		if (!Utils.isPresent(userCF) || !Utils.isPresent(thisCF))
			isValid = true;

		// 1) controllo l'uguaglianza col codice di input userCF
		if (!isValid) {
			// trasformo il codice fiscale in uppercase
			userCF = userCF.toUpperCase();
			isValid = userCF.equals(thisCF);
		}

		// 2) se non Ë valido, controllo se presente tra gli 8 cf generati dal metodo generaCodFiscValidi
		if (!isValid) {
			List<String> cfValidi = Arrays.asList(generaCodFiscValidi());
			if (cfValidi.contains(userCF))
				isValid = true;
		}

		// 3) se non Ë valido, controllo la congruit‡ formale dalla classe CodiceFiscaleFormale
		if (!isValid) {
			CodiceFiscaleFormale cff = new CodiceFiscaleFormale(userCF);
			isFormallyValid = cff.controllaCorrettezzaFormale();
		}

		// 4) se Ë formalmente valido, controllo i primi 11 caratteri, nome cognome e data di nascita
		if (isFormallyValid) {
			Pattern num = Pattern.compile("[0-9]{2}");
			String thisLNFN = thisCF.substring(0, 6);
			String userLNFN = userCF.substring(0, 6);
			String thisYear = thisCF.substring(6, 8);
			String userYear = userCF.substring(6, 8);
			char thisMonth = thisCF.charAt(8);
			char userMonth = userCF.charAt(8);
			String thisDay = thisCF.substring(9, 11);
			String userDay = userCF.substring(9, 11);
			// Confrontando il cf di input, userCF, con quello generato, thisCF,
			// se si verifica anche una sola delle seguenti condizioni:
			// - differenza sulla lettera relativa al mese
			// - differenza sui caratteri relativi a cognome e nome
			// - differenza sull'anno di nascita, solo se l'anno di userCF Ë numerico
			// - differenza sul giorno di nascita, solo se il giorno di userCF Ë numerico
			// il codice fiscale non Ë da ritenersi corretto, quindi lo invalido anche formalmente.
			if (thisMonth != userMonth || !thisLNFN.equals(userLNFN)
					|| num.matcher(userYear).matches() && !userYear.equals(thisYear)
					|| num.matcher(userDay).matches() && !userDay.equals(thisDay)) {
				isFormallyValid = false;
			} else { // negli altri casi lo ritengo non valido e forzabile
				isToBeForced = true;
			}
		}

		esito.setValid(isValid);
		esito.setToBeForced(isToBeForced);
		esito.setFormallyValid(isFormallyValid);

		return esito;
	}

}