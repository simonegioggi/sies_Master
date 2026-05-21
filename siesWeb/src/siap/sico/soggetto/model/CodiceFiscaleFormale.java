package siap.sico.soggetto.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe per il controllo formale del cf
 *
 * @author sgioggi
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CodiceFiscaleFormale {

	// LETTERE SOSTITUTIVE DEGLI OMOCODICI
	// private static final String omocodici = "LMNPQRSTUV";

	private String strCodFisc;

	private List lstCd;
	private List lstCp;
	private List lstNd;
	private List lstNp;
	private List lstVm; // List dei mesi

	// private static List ceck;
	private String ritorno;

	// costruttore
	public CodiceFiscaleFormale() {
	}

	/**
	 * Costruttore aparametrico della classe.
	 */
	public CodiceFiscaleFormale(String g) {

		strCodFisc = g;

		creaCarattereDispari();
		creaCaratterePari();
		creaNumeroDispari();
		creaNumeroPari();
		creaMese();
	}

	/**
	 * Classe per il controllo del cf
	 *
	 * @return char
	 */
	public boolean controllaCheckDigit() {

		int intAppoggio = 0;
		char chrCarattereEsaminato;

		// Ciclo di conteggio dei valori sui primi 15 caratteri del codice
		// fiscale
		for (int i = 0; i < 15; i++) {
			chrCarattereEsaminato = getCodFisc().charAt(i);
			String strElem = getCodFisc().substring(i, i + 1);
			int intResto = (i % 2);
			switch (intResto) {
			case 0:
				if (!Character.isDigit(chrCarattereEsaminato)) {
					intAppoggio += getVectCarDisp(strElem);
				} else {
					intAppoggio += getVectNumDisp(strElem);
				}
				break;
			case 1:
				if (!Character.isDigit(chrCarattereEsaminato)) {
					intAppoggio += getVectCarPari(strElem);
				} else {
					intAppoggio += getVectNumPari(strElem);
				}
				break;
			default:
				break;
			}
		}

		// Estraggo il carattere di controllo
		String ceckdigit = getCodFisc().substring(15, 16);
		return (intAppoggio % 26) == getVectCarPari(ceckdigit);
	}

	/**
	 * Questo metodo è stato creato VisualAge.
	 *
	 * @return boolean
	 */
	public boolean controllaCorrettezza() {

		return (controllaCorrettezzaChar() == '0');
	}

	/**
	 * Classe per il controllo del cf
	 *
	 * @return char @
	 */
	public char controllaCorrettezzaChar() {

		boolean bolLettera = false;
		for (int i = 0; i < 6; i++) {
			// controllo dei primi 6 digit
			if (!(Character.isLetter(strCodFisc.charAt(i))))
				return '2'; // caratteri alfabetici
		}
		for (int i = 6; i < 8; i++) {
			// controllo dell'anno
			if (!(Character.isDigit(strCodFisc.charAt(i))))
				return '2';
		}

		// controllo del mese
		if (!((strCodFisc.charAt(8) != 'A') || (strCodFisc.charAt(8) != 'B') || (strCodFisc.charAt(8) != 'C')
				|| (strCodFisc.charAt(8) != 'D') || (strCodFisc.charAt(8) != 'E')
				|| (strCodFisc.charAt(8) != 'H') || (strCodFisc.charAt(8) != 'L')
				|| (strCodFisc.charAt(8) != 'M') || (strCodFisc.charAt(8) != 'P')
				|| (strCodFisc.charAt(8) != 'R') || (strCodFisc.charAt(8) != 'S')
				|| (strCodFisc.charAt(8) != 'T')))
			return '2';

		for (int i = 9; i < 11; i++) {
			// controllo dell'anno
			if (!(Character.isDigit(strCodFisc.charAt(i))))
				return '2';
		}
		// controllo formale del giorno
		int intGiorno = Integer.parseInt(strCodFisc.substring(9, 11));
		if (intGiorno > 31)
			intGiorno -= 40;
		if (intGiorno < 1 || intGiorno > 31)
			return '2';
		// lettera del mese
		String strElem = strCodFisc.substring(8, 9);
		// valore della lettera del mese
		String strMese = String.valueOf(getVectMese(strElem));
		// se mese ha una sola cifra viene aggiunto uno zero
		if (strMese.length() == 1)
			strMese = "0" + strMese;
		String strAnno = strCodFisc.substring(6, 8);
		// se giorno ha una sola cifra viene aggiunto uno zero
		String strGiorno = String.valueOf(intGiorno);
		if (strGiorno.length() == 1)
			strGiorno = "0" + strGiorno;
		// controllo dell'intera data
		String data = strGiorno + strMese + strAnno;
		if (!(controllaData(data)))
			return '2';
		if (((strCodFisc.charAt(11) != 'A') && (strCodFisc.charAt(11) != 'B') &&
		// controllo del 1° carattere
				(strCodFisc.charAt(11) != 'C') && (strCodFisc.charAt(11) != 'D') &&
				// del codice catastale
				(strCodFisc.charAt(11) != 'E') && (strCodFisc.charAt(11) != 'F')
				&& (strCodFisc.charAt(11) != 'G') && (strCodFisc.charAt(11) != 'H')
				&& (strCodFisc.charAt(11) != 'I') && (strCodFisc.charAt(11) != 'L')
				&& (strCodFisc.charAt(11) != 'M') && (strCodFisc.charAt(11) != 'Z')))
			return '2';
		for (int i = 6; i < 8; i++) {
			if (!(Character.isDigit(strCodFisc.charAt(i))))
				// controllo del 1° carattere
				if ((strCodFisc.charAt(i) != 'L') && (strCodFisc.charAt(i) != 'M') &&
				// del codice catastale
						(strCodFisc.charAt(i) != 'N') && (strCodFisc.charAt(i) != 'P')
						&& (strCodFisc.charAt(i) != 'Q') && (strCodFisc.charAt(i) != 'R')
						&& (strCodFisc.charAt(i) != 'S') && (strCodFisc.charAt(i) != 'T')
						&& (strCodFisc.charAt(i) != 'U') && (strCodFisc.charAt(i) != 'V'))
					return '3';
		}

		for (int i = 9; i < 11; i++) {
			if (!(Character.isDigit(strCodFisc.charAt(i))))
				// controllo del 1°/ carattere
				if ((strCodFisc.charAt(i) != 'L') && (strCodFisc.charAt(i) != 'M') &&
				// del codice catastale
						(strCodFisc.charAt(i) != 'N') && (strCodFisc.charAt(i) != 'P')
						&& (strCodFisc.charAt(i) != 'Q') && (strCodFisc.charAt(i) != 'R')
						&& (strCodFisc.charAt(i) != 'S') && (strCodFisc.charAt(i) != 'T')
						&& (strCodFisc.charAt(i) != 'U') && (strCodFisc.charAt(i) != 'V'))
					return '3';
		}

		for (int i = 12; i < 15; i++) {
			if (!(Character.isDigit(strCodFisc.charAt(i)))) {
				bolLettera = true;
				// controllo del 1° carattere
				if ((strCodFisc.charAt(i) != 'L') && (strCodFisc.charAt(i) != 'M') &&
				// del codice catastale
						(strCodFisc.charAt(i) != 'N') && (strCodFisc.charAt(i) != 'P')
						&& (strCodFisc.charAt(i) != 'Q') && (strCodFisc.charAt(i) != 'R')
						&& (strCodFisc.charAt(i) != 'S') && (strCodFisc.charAt(i) != 'T')
						&& (strCodFisc.charAt(i) != 'U') && (strCodFisc.charAt(i) != 'V'))
					return '3';
			}
		}

		if (!bolLettera) {
			int intNumeroCodCat = Integer.parseInt(strCodFisc.substring(12, 15));
			// se lettera M le 3 cifre del cod. cat. non > di 399
			if ((intNumeroCodCat == 000) || ((strCodFisc.charAt(11) == 'M') && (intNumeroCodCat > 399)))
				return '2';
		}
		if (controllaCheckDigit())
			return '0';
		return '1';
	}

	/**
	 * Questo metodo è stato creato VisualAge.
	 *
	 * @return int
	 */
	public int controllaCorrettezzaInt() {

		return (controllaCorrettezzaChar() - 48);
	}

	/**
	 * Determina in base all'anno corrente, il secolo da appendere alla string di input: Se, nel 2012: - viene
	 * passato "04", verrà appeso "20" in modo da ottenere "2004" - viene passato "44", verrà appeso "19" in
	 * modo da ottenere "1944" - viene passato "18", verrà sollevata un'eccezione a runtime, in quanto il
	 * soggetto ha più di ottantanni
	 *
	 * @param anno
	 *            l'anno di input a due cifre
	 * @return l'anno di input a 4 cifre
	 */
	private String determinaAnno(String anno) {

		String realYear = "";
		// recupero l'anno corrente
		Calendar cal = new GregorianCalendar();
		int currentYear = cal.get(Calendar.YEAR);
		// estrapolo il secolo
		String sCurrentCentury = ("" + currentYear).substring(0, 2);
		int iCurrentCentury = Integer.parseInt(sCurrentCentury);
		// estrapolo l'anno dalle decine
		String sCurrentYear = ("" + currentYear).substring(2);
		int iCurrentYear = Integer.parseInt(sCurrentYear);
		int iInput = Integer.parseInt(anno);
		if (iInput > iCurrentYear) {
			if (iInput + 80 < iCurrentYear + 100)
				throw new IllegalArgumentException("Soggetto con più di 80 anni.");
			else
				realYear = (iCurrentCentury - 1) + anno;
		} else {
			if (iInput + 80 < iCurrentYear)
				throw new IllegalArgumentException("Soggetto con più di 80 anni.");
			else
				realYear = iCurrentCentury + anno;
		}
		return realYear;
	}

	/**
	 * Classe per il controllo del cf
	 *
	 * @return boolean
	 */
	public boolean controllaData(String s) {

		// controllo l'anno dopo averlo estrapolato dalla stringa
		try {
			String strAnno = s.substring(4, s.length());
			if ((s.length() == 8) || (s.length() == 6)) {
				if (s.length() == 6) {
					strAnno = determinaAnno(strAnno);
				}
				if (Integer.parseInt(strAnno) < 1870) {
					return false;
				}
			} else {
				return false;
			}

			// Estrapolazione mese e giorno
			String strMese = s.substring(2, 4);
			String strGiorno = s.substring(0, 2);

			// Trasformazione delle stringhe in interi
			int intMese = Integer.parseInt(strMese);
			int intGiorno = Integer.parseInt(strGiorno);
			int intAnno = Integer.parseInt(strAnno);

			// controlli di ammissibilità sul giorno e sul mese
			if ((intMese > 12) || (intGiorno > 31) || (intMese < 1) || (intGiorno < 1)) {
				return false;
			}

			// controllo mese
			switch (intMese) {
			// febbraio
			case 2:
				boolean bisestile = false;
				if (intAnno % 4 == 0) {
					bisestile = true;
					if (intAnno % 100 == 0) {
						bisestile = intAnno % 400 == 0;
					}
				}
				if ((bisestile && (intGiorno > 29)) || (!bisestile && (intGiorno > 28))) {
					return false;
				}
				break;
			// aprile
			case 4:
				if (intGiorno > 30) {
					return false;
				}
				break;
			// giugno
			case 6:
				if (intGiorno > 30) {
					return false;
				}
				break;
			// settembre
			case 9:
				if (intGiorno > 30) {
					return false;
				}
				break;
			// novembre
			case 11:
				if (intGiorno > 30) {
					return false;
				}
				break;

			default:
				break;
			}
			// se arrivo a questo punto vuol dire che la data è corretta
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Tabella (di tipo List) dei valori dei caratteri dispari.
	 */
	public List creaCarattereDispari() {

		this.lstCd = new ArrayList();

		lstCd.add("B"); // valore dei caratteri dispari
		lstCd.add("A");
		lstCd.add("K");
		lstCd.add("P");
		lstCd.add("L");
		lstCd.add("C");
		lstCd.add("Q");
		lstCd.add("D");
		lstCd.add("R");
		lstCd.add("E");
		lstCd.add("V");
		lstCd.add("O");
		lstCd.add("S");
		lstCd.add("F");
		lstCd.add("T");
		lstCd.add("G");
		lstCd.add("U");
		lstCd.add("H");
		lstCd.add("M");
		lstCd.add("I");
		lstCd.add("N");
		lstCd.add("J");
		lstCd.add("W");
		lstCd.add("Z");
		lstCd.add("Y");
		lstCd.add("X");

		return lstCd;
	}

	/**
	 * Tabella (di tipo List) dei valori dei caratteri pari.
	 */
	public List creaCaratterePari() {

		this.lstCp = new ArrayList();

		lstCp.add("A"); // valore dei caratteri pari
		lstCp.add("B");
		lstCp.add("C");
		lstCp.add("D");
		lstCp.add("E");
		lstCp.add("F");
		lstCp.add("G");
		lstCp.add("H");
		lstCp.add("I");
		lstCp.add("J");
		lstCp.add("K");
		lstCp.add("L");
		lstCp.add("M");
		lstCp.add("N");
		lstCp.add("O");
		lstCp.add("P");
		lstCp.add("Q");
		lstCp.add("R");
		lstCp.add("S");
		lstCp.add("T");
		lstCp.add("U");
		lstCp.add("V");
		lstCp.add("W");
		lstCp.add("X");
		lstCp.add("Y");
		lstCp.add("Z");

		return lstCp;
	}

	/**
	 * Classe per il controllo del cf
	 *
	 * @return List
	 */
	public List creaMese() {

		this.lstVm = new ArrayList();

		lstVm.add(" ");
		lstVm.add("A"); // gennaio
		lstVm.add("B"); // febbraio
		lstVm.add("C"); // marzo
		lstVm.add("D"); // aprile
		lstVm.add("E"); // maggio
		lstVm.add("H"); // giugno
		lstVm.add("L"); // luglio
		lstVm.add("M"); // agosto
		lstVm.add("P"); // settembre
		lstVm.add("R"); // ottobre
		lstVm.add("S"); // novembre
		lstVm.add("T"); // dicembre

		return lstVm;
	}

	/**
	 * Tabella (di tipo List) dei valori dei numeri dispari.
	 */
	public List creaNumeroDispari() {

		this.lstNd = new ArrayList();

		lstNd.add("1"); // valore dei numeri dispari
		lstNd.add("0");
		lstNd.add(" ");
		lstNd.add(" ");
		lstNd.add(" ");
		lstNd.add("2");
		lstNd.add(" ");
		lstNd.add("3");
		lstNd.add(" ");
		lstNd.add("4");
		lstNd.add(" ");
		lstNd.add(" ");
		lstNd.add(" ");
		lstNd.add("5");
		lstNd.add(" ");
		lstNd.add("6");
		lstNd.add(" ");
		lstNd.add("7");
		lstNd.add(" ");
		lstNd.add("8");
		lstNd.add(" ");
		lstNd.add("9");

		return lstNd;
	}

	/**
	 * Tabella (di tipo List) dei valori dei numeri pari.
	 */
	public List creaNumeroPari() {

		this.lstNp = new ArrayList();

		lstNp.add("0"); // valore dei numeri pari
		lstNp.add("1");
		lstNp.add("2");
		lstNp.add("3");
		lstNp.add("4");
		lstNp.add("5");
		lstNp.add("6");
		lstNp.add("7");
		lstNp.add("8");
		lstNp.add("9");

		return lstNp;
	}

	/**
	 * Questo metodo prende il codice fiscale come parametro e torna una stringa.
	 *
	 * @return java.lang.String
	 */
	public String getCodFisc() {

		String s = new String(this.strCodFisc);
		return s;
	}

	/**
	 * Classe per il controllo del cf
	 *
	 * @return java.lang.String
	 */
	public String getRitorno() {

		String s = new String(this.ritorno);
		return s;
	}

	/**
	 * Calcola valore del carattere dispari.
	 */
	public int getVectCarDisp(String elem) {

		return this.lstCd.indexOf(elem);

	}

	/**
	 * Calcola valore del carattere pari.
	 *
	 * @return int
	 */
	public int getVectCarPari(String elem) {

		return this.lstCp.indexOf(elem);

	}

	/**
	 * Classe per il controllo del cf
	 *
	 * @return int
	 * @param elem
	 *            java.lang.String
	 */
	public int getVectMese(String stringa) {

		return this.lstVm.indexOf(stringa);
	}

	/**
	 * Calcola valore del numero dispari.
	 *
	 * @return int
	 */
	public int getVectNumDisp(String elem) {

		return this.lstNd.indexOf(elem);
	}

	/**
	 * Calcola valore del numero pari.
	 *
	 * @return int
	 */
	public int getVectNumPari(String elem) {

		return this.lstNp.indexOf(elem);
	}

	/**
	 * Inizializzazione di un codice fiscale come stringa.
	 *
	 * @param s java.lang.String
	 */
	public void setCodFisc(String s) {

		this.strCodFisc = new String(s);
		return;
	}

	/**
	 * Controlla la correttezza formale del codice fiscale
	 *
	 * @return boolean true, se formalmente corretto, altrimenti false
	 */
	public boolean controllaCorrettezzaFormale() {

		if (this.strCodFisc == null)
			return false;
		Pattern cf = Pattern.compile(
				"[A-Z]{6}[\\dLMNPQRSTUV]{2}[A-EHLMPRST][\\dLMNPQRSTUV]{2}[A-Z][\\dLMNPQRSTUV]{3}[A-Z]",
				Pattern.CASE_INSENSITIVE);
		Pattern numCodCat = Pattern.compile("[0-9]{3}");
		if (!cf.matcher(strCodFisc).matches())
			return false;
		char letteraCodiceCatastale = strCodFisc.charAt(11);
		String numeriCodiceCatastale = strCodFisc.substring(12, 15);
		// se i numeri del codice catastale sono "000", il codice non è valido
		if (numeriCodiceCatastale.equals("000"))
			return false;
		// se i numeri del codice catastale sono cifre, verifico
		// che se la lettera è M il numero non superi 399
		if (numCodCat.matcher(numeriCodiceCatastale).matches()) {
			int iNumCodCat = Integer.parseInt(numeriCodiceCatastale);
			if (letteraCodiceCatastale == 'M' && iNumCodCat > 399)
				return false;
		}
		// verifico il carattere di controllo
		return controllaCheckDigit();
	}

}