package f3b.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/*
 * <p>Title: DateUtils</p>
 * <p>Description: Classe di utilità per gestione delle date. ( Versione Non stabile ).</p>
 * <p>Copyright: Bull Italia Copyright (c) 2002</p>
 * <p>Company: BULL Italia S.p.A.</p>
 */
public class DateUtils {

	/**
	 * Test dei metodi della classe.
	 * <p>
	 *
	 * @param args
	 */
	// public static void main(String[] args) {
	//
	// System.out.println("01) Test getSysDate(String aPattern) : " + getSysDate("dd/MM/yyyy"));
	// System.out.println("02) Test getDateToString ( Date aDate, String aPattern ) : "
	// + getDateToString(new Date(), "dd/MM/yyyy"));
	// System.out.println("03) Test getEndOfMonth(int aYear, int aMonth ) : " + getEndOfMonth(0, 0));
	// System.out.println("04) Test getStartOfMonth(int aYear, int aMonth ) : " + getStartOfMonth(2002, 01));
	// System.out.println("05) Test getEndOfMonth(String aYear, String aMonth ) : "
	// + getEndOfMonth("2002", "01"));
	// System.out.println("06) Test getStartOfMonth(String aYear, String aMonth ) : "
	// + getStartOfMonth("2002", "01"));
	// System.out.println("07) Test getDate( int aYear, int aMonth ) : " + getDate(2002, 11));
	// System.out
	// .println("08) Test getDate( int aYear, int aMonth, int aDay, int aHours, int aMinutes, int aSeconds ) :
	// "
	// + getDate(2002, 11, 15, 10, 30, 25));
	// System.out
	// .println("09) Test getDate( String aYear, String aMonth, String aDay, String aHours, String aMinutes,
	// String aSeconds ) :"
	// + getDate("2002", "11", "15", "10", "30", "25"));
	// System.out.println("10) Test getDate( String aDate, String aPattern ) : "
	// + getDate("20020103", "yyyyMMdd"));
	// System.out.println("11) Test getDate( int aYear, int aMonth, int aDay, int aHours, int aMinutes) : "
	// + getDate(2002, 11, 15, 10, 30));
	// System.out
	// .println("12) Test getDate( String aYear, String aMonth, String aDay, String aHours, String aMinutes )
	// : "
	// + getDate("2002", "11", "15", "10", "30"));
	// System.out.println("13) Test getDate( int aYear, int aMonth, int aDay ) : " + getDate(2002, 11, 15));
	// System.out.println("14) Test getDate( String aYear, String aMonth, String aDay ) : "
	// + getDate("2000", "01", "01"));
	// System.out.println("15) Test getDate( String aDate, String aPattern ) : "
	// + getDate("20021115", "yyyyMMdd"));
	// }

	/**
	 * Ritorna la data di sistema in formato <code>String</code>.
	 * <p>
	 *
	 * @param aPattern
	 *            formato della data da restituire.
	 * @return data di sistema nel formato richiesto.
	 */
	public static String getSysDate(String aPattern) {
		return getDateToString(new Date(), aPattern);
	}

	public static Date getSysDateAsDate(String aPattern) {
		String lStrDate = getDateToString(new Date(), aPattern);
		Date lDate = getDate(lStrDate, aPattern);

		return lDate;
	}

	/**
	 * Ritorna la data di sistema in formato <code>Date</code>.
	 * <p>
	 *
	 * @return data di sistema nel formato richiesto.
	 */
	public static Date getSysDate() {
		return new Date();
	}

	/**
	 * Formattazione di una data passata come parametro.
	 * <p>
	 *
	 * @param aDate
	 *            Data da ritornare formatta i <code>String</code>
	 * @param aPattern
	 *            Pattern di formattazione ( Es.: gg/MM/yyyy ).
	 * @return la data in formato <code>String</code>.
	 */
	public static String getDateToString(Date aDate, String aPattern) {

		if (aDate == null)
			return null;

		String lDate = null;

		try {
			// SimpleDateFormat lFormatter = new SimpleDateFormat ( aPattern, Locale.getDefault() );
			SimpleDateFormat lFormatter = new SimpleDateFormat(aPattern, Locale.ITALIAN);
			lDate = lFormatter.format(aDate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return lDate;
	}

	/**
	 * STUB-20021104: Da Rivedere ... Ritorna l'ultimo giorno del mese.
	 * <p>
	 *
	 * @param aYear
	 *            Anno di riferimento.
	 * @param aMonth
	 *            Mese di riferimento.
	 * @return oggetto <code>Date</code> ultimo giorno del mese.
	 */
	public static Date getEndOfMonth(int aYear, int aMonth) {
		GregorianCalendar lCalendar = new GregorianCalendar(aYear, aMonth - 1, 1, 0, 0, 0);
		lCalendar.add(Calendar.MONTH, 1);
		lCalendar.add(Calendar.SECOND, -1);

		return (lCalendar.getTime());
	}

	/**
	 * STUB-20021104: Da rivedere ...
	 * <p>
	 *
	 * @param aYear
	 *            Anno di riferimento.
	 * @param aMonth
	 *            Mese di riferimento.
	 * @return oggetto <code>Date</code> primo giorno del mese.
	 */
	public static Date getStartOfMonth(int aYear, int aMonth) {
		GregorianCalendar lCalendar = new GregorianCalendar(aYear, aMonth - 1, 1, 0, 0, 0);
		return (lCalendar.getTime());
	}

	/**
	 * STUB-20021031: Da Rivedere ... -- Con altri metodi, per risoluzione problema oraraio --
	 * <p>
	 *
	 * @param aYear
	 *            Anno di riferimento
	 * @param aMonth
	 *            Mese di riferimento
	 * @return oggetto <code>Date</code> ultimo giorno del mese.
	 */
	public static Date getEndOfMonth(String aYear, String aMonth) {
		Date lDate = null;

		try {
			int lYear = Integer.parseInt(aYear);
			int lMonth = Integer.parseInt(aMonth);
			lDate = getEndOfMonth(lYear, lMonth);
		} catch (NumberFormatException nex) {
		}

		return lDate;

	}

	/**
	 * STUB-20021104: Da rivedere .... -- Con altri metodi, per risoluzione problema oraraio --
	 * <p>
	 *
	 * @param aYear
	 *            Anno di riferimento.
	 * @param aMonth
	 *            Mese di riferimento.
	 * @return oggetto <code>Date</code> primo giorno del mese.
	 */
	public static Date getStartOfMonth(String aYear, String aMonth) {
		Date lDate = null;

		try {
			int lYear = Integer.parseInt(aYear);
			int lMonth = Integer.parseInt(aMonth);
			lDate = getStartOfMonth(lYear, lMonth);
		} catch (NumberFormatException nex) {
		}

		return lDate;
	}

	/**
	 * Ritorna la data, passando come parametri Anno e Mese.
	 * <p>
	 *
	 * @param aYear
	 *            Anno di riferimento in formato <code>int</code>.
	 * @param aMonth
	 *            Mese di riferimento in formato <code>int</code>.
	 * @return ritorna l'oggetto <code>Date</code>.
	 */
	public static Date getDate(int aYear, int aMonth) {
		return getDate(aYear, aMonth, 1, 0, 0, 0);
	}

	/**
	 * Ritorna la data, passando come parametri Anno, Mese, Giorno, Ore, Minuti e Secondi.
	 * <p>
	 *
	 * @param aDay
	 *            Giorno di riferimento.
	 * @param aMonth
	 *            Mese di riferimento.
	 * @param aYear
	 *            Anno di riferimento.
	 * @param aHours
	 *            Ore di riferimento
	 * @param aMinutes
	 *            Minuti di riferimento.
	 * @param aSeconds
	 *            Secondi di riferimento.
	 * @return
	 */
	public static Date getDate(int aYear, int aMonth, int aDay, int aHours, int aMinutes, int aSeconds) {
		GregorianCalendar lCal = new GregorianCalendar(aYear, (aMonth - 1), aDay, aHours, aMinutes, aSeconds);

		return lCal.getTime();
	}

	/**
	 * Ritorna la data, passando come parametri Anno, Mese, Giorno, Ore, Minuti e Secondi, in formato
	 * <code>String</code>.
	 * <p>
	 *
	 * @param aYear
	 *            Anno di riferimento.
	 * @param aMonth
	 *            Mese di riferimento.
	 * @param aDay
	 *            Giorno di riferimento.
	 * @param aHours
	 *            Ore di riferimento.
	 * @param aMinutes
	 *            Minuti di riferimento.
	 * @param aSeconds
	 *            Secomdi di riferimento.
	 * @return
	 */
	public static Date getDate(String aYear, String aMonth, String aDay, String aHours, String aMinutes,
			String aSeconds) {
		Date lDate = null;

		if (aYear == null || aMonth == null || aDay == null || aHours == null || aMinutes == null
				|| aSeconds == null || aYear.equals("") || aMonth.equals("") || aDay.equals("")
				|| aHours.equals("") || aMinutes.equals("") || aSeconds.equals(""))
			return lDate;

		int lYear = Integer.parseInt(aYear);
		int lMonth = Integer.parseInt(aMonth);
		int lDay = Integer.parseInt(aDay);
		int lHours = Integer.parseInt(aHours);
		int lMinutes = Integer.parseInt(aMinutes);
		int lSeconds = Integer.parseInt(aSeconds);

		lDate = getDate(lYear, lMonth, lDay, lHours, lMinutes, lSeconds);

		return lDate;
	}

	/**
	 * Ritorna la data passando come parametri data in formato stringa e il relativo pattern di formato (es.:
	 * gg/MM/yyyy).
	 * <p>
	 *
	 * @param aDate
	 *            data da convertire
	 * @param aPattern
	 *            formattazione data
	 * @return oggetto <code>Date</code>
	 */
	public static Date getDate(String aDate, String aPattern) {
		SimpleDateFormat lSDF = new SimpleDateFormat();
		lSDF.applyPattern(aPattern);

		Date lDate = null;

		try {
			lDate = lSDF.parse(aDate);
		} catch (ParseException pex) {
			// pex.printStackTrace();
		}

		return lDate;
	}

	/**
	 * Ritorna la data passando come parametri tutti gli elementi ( es.: mese, anno, giorno ecc...) in formato
	 * intero numerico.
	 * <p>
	 *
	 * @param aYear
	 *            Anno di riferimento
	 * @param aMonth
	 *            Mese di rriferimento.
	 * @param aDay
	 *            Giorno di riferimento.
	 * @param aHours
	 *            Ore di riferimento
	 * @param aMinutes
	 *            Minuti di riferimento.
	 * @return Oggetto <code>Date</code> data convertita.
	 */
	public static Date getDate(int aYear, int aMonth, int aDay, int aHours, int aMinutes) {
		return getDate(aYear, aMonth, aDay, aHours, aMinutes, 0);
	}

	/**
	 * Ritorna la data passando come parametri tutti gli elementi necessari ( es.: mese, anno, giorno ecc... )
	 * in formato Stringa.
	 * <p>
	 *
	 * @param aYear
	 *            anno 4 cifre ( 2000, 2001 ... )
	 * @param aMonth
	 *            mese 2 cifre ( 01, 02, 03 ... 11 )
	 * @param aDay
	 *            giorno 2 cifre ( 01, 02, 03 ... 11 )
	 * @param aHours
	 *            ore 2 cifre ( 01, 02, 03 ... 11 )
	 * @param aMinutes
	 *            minuti 2 cifre ( 01, 02, 03 ... 58 )
	 * @return l'oggetto data.
	 */
	public static Date getDate(String aYear, String aMonth, String aDay, String aHours, String aMinutes) {
		return getDate(aYear, aMonth, aDay, aHours, aMinutes, "00");
	}

	/**
	 * STUB-20021104: OK !!! da Testare Ritorna la data passando come parametri gli elementi necessari ( es.:
	 * mese, anno, giorno ) in formato intero numerico.
	 * <p>
	 *
	 * @param aYear
	 *            Anno di riferimento.
	 * @param aMonth
	 *            Mese di riferimento.
	 * @param aDay
	 *            Giorno di riferimento.
	 * @return Oggetto <code>Date</code> data convertita.
	 */
	public static Date getDate(int aYear, int aMonth, int aDay) {
		return getDate(aYear, aMonth, aDay, 0, 0, 0);
	}

	/**
	 * STUB-20021104: OK !!! Da Testare ... e Rivedere !!!!! Ritorna la data passando come parametri gli
	 * elementi necessari ( es.: mese, anno, giorno ) in formato <code>String</code>.
	 * <p>
	 *
	 * @param aYear
	 *            Anno di riferimento.
	 * @param aMonth
	 *            Mese di riferimento.
	 * @param aDay
	 *            Giorno di riferimento.
	 * @return Oggetto <code>Date</code> data convertita.
	 */
	public static Date getDate(String aYear, String aMonth, String aDay) {
		return getDate(aYear, aMonth, aDay, "00", "00", "00");
	}

	/**
	 * Ritorna il campo <code>Calendar</code> di una data come un intero. Es.: Calendar.MONTH - Ritorna il
	 * mese come un intero.
	 * <p>
	 *
	 * @param aDate
	 *            Data di riferimento.
	 * @param aField
	 *            Campo di azione vedi <code>Calendar</code>.
	 * @return il valore del cmapo selezionato della data in intero.
	 */
	public static int getDateToInt(Date aDate, int aField) {
		GregorianCalendar lGC = new GregorianCalendar();
		lGC.setTime(aDate);
		return (lGC.get(aField));
	}

	/**
	 * STUB-20021104: Da rivedere .... Utilizzare altri metodi di formattazione. Ritorna la data passata come
	 * parametro in un intero con il formato <code>yyyyMMdd</code>
	 * <p>
	 *
	 * @param aDate
	 *            data da convertire in intero.
	 * @return data convertita in formato intero.
	 */
	public static int getDateToInt(Date aDate) {
		String lValue = getDateToString(aDate, "yyyyMMdd");
		return (Integer.parseInt(lValue));
	}

	/**
	 * Ritorna il massimo tra due date.
	 * <p>
	 *
	 * @param aDateOne
	 *            Prima data da confrontare.
	 * @param aDateTwo
	 *            Seconda data da confrontare.
	 * @return la data più grande tra le due.
	 */
	public static Date getMaxDate(Date aDateOne, Date aDateTwo) {
		if (aDateOne.after(aDateTwo))
			return aDateOne;
		else
			return aDateTwo;
	}

	/**
	 * Ritorna il minimo tra due date.
	 * <p>
	 *
	 * @param aDateOne
	 *            Prima data da confrontare !
	 * @param aDateTwo
	 *            Seconda data da confrontare !
	 * @return la data più piccola tra le due.
	 */
	public static Date getMinDate(Date aDateOne, Date aDateTwo) {
		if (aDateOne.before(aDateTwo))
			return aDateOne;
		else
			return aDateTwo;
	}

	/**
	 * Ritorna ritorna la data incrementata di un giorno.
	 * <p>
	 *
	 * @param aDate
	 *            data di riferimento.
	 * @return la data incrementata di un giorno.
	 */
	public static Date getDayAfter(Date aDate) {
		return moveDateTo(aDate, Calendar.DAY_OF_MONTH, 1);
	}

	/**
	 * Ritorna la data decrementata di un giorno.
	 * <p>
	 *
	 * @param aDate
	 *            data di riferimento.
	 * @return la data decrementata di un giorno.
	 */
	public static Date getDayBefore(Date aDate) {
		return moveDateTo(aDate, Calendar.DAY_OF_MONTH, -1);
	}

	/**
	 * Ritorna la data decrementata di un mese.
	 * <p>
	 *
	 * @param aDate
	 *            data di riferimento.
	 * @return la data decrementata di un mese.
	 */
	public static Date getMonthBefore(Date aDate) {
		return moveDateTo(aDate, Calendar.MONTH, -1);
	}

	/**
	 * Ritorna la data decrementata di un nemero di mesi specificato in input.
	 * <p>
	 *
	 * @param aDate
	 *            data di riferimento.
	 * @param aNumeroMesi
	 * @return la data decrementata decrementata di "N" mesi
	 */
	public static Date getEnneMonthBefore(Date aDate, int aNumeroMesi) {
		return moveDateTo(aDate, Calendar.MONTH, -aNumeroMesi);
	}

	/**
	 * Ritorna la data modificata secondo i parametri passati al metodo. Infatti è possibile, definendo il
	 * campo di azione e l'incremento di manipolare la data in qualunque campo.
	 * <p>
	 *
	 * @param aDate
	 *            Data di riferimento.
	 * @param aField
	 *            Campo da modificare <code>Calendar.nome_costante</code> es. Calendar.DAY_OF_MONTH se si
	 *            vogliono aggiungere/sottrarre dei giorni alla data in input es. Calendar.MONTH se si
	 *            vogliono aggiungere dei mesi alla data in input es. Calendar.YEAR se si vogliono aggiungere
	 *            degli anni alla data in input Attenzione!! La data viene incrementata in modo esatto solo se
	 *            si opera sui giorni. Se si incrementa il mese o l'anno è possibile che la data di arrivo non
	 *            sia valida: es aData = 30/01/2007, aField = Calendar.MONTH, aInc = 1 in questo caso si
	 *            raggiunge il 30/02/2007, la funzione restituisce il 28/02/2007 approssima per difetto
	 * @param aInc
	 *            Step di incremento.
	 * @return la data modificata.
	 */
	public static Date moveDateTo(Date aDate, int aField, int aInc) {
		GregorianCalendar lGC = new GregorianCalendar();

		try {
			lGC.setTime(aDate);
			lGC.add(aField, aInc);
		} catch (IllegalArgumentException iaex) {
			return aDate;
		}

		return lGC.getTime();
	}

	/**
	 * STUB-20021104: Da rivedere. Ritorna il giorno in formato <code>String</code>.
	 * <p>
	 *
	 * @param aDate
	 *            Data di riferimento.
	 * @return
	 */
	public static String getDayToString(Date aDate) {

		if (aDate == null)
			return null;
		GregorianCalendar lGC = new GregorianCalendar();
		lGC.setTime(aDate);
		String lDay = (new Integer(lGC.get(Calendar.DAY_OF_MONTH))).toString();

		return lDay;
	}

	/**
	 * STUB-20021104: Da rivedere ( Bisognerebbe riscriverlo con altri metodi ). Ritorna il mese in formato
	 * <code>String</code>.
	 * <p>
	 *
	 * @param aDate
	 *            data di riferimento.
	 * @return il mese della data di riferimento.
	 */
	public static String getMonthToString(Date aDate) {

		if (aDate == null)
			return null;
		GregorianCalendar lGC = new GregorianCalendar();
		lGC.setTime(aDate);
		String lMonth = (new Integer(lGC.get(Calendar.MONTH) + 1)).toString();

		return lMonth;
	}

	/**
	 * STUB-20021104: Da rivedere. ( Bisognerebbe riscriverlo con altri metodi ). Ritorna l'anno in formato
	 * <code>String</code>.
	 * <p>
	 *
	 * @param aDate
	 *            data di riferimento.
	 * @return l'anno della data di riferimento.
	 */
	public static String getYearToString(Date aDate) {

		if (aDate == null)
			return null;
		GregorianCalendar lGC = new GregorianCalendar();
		lGC.setTime(aDate);
		String lYear = (new Integer(lGC.get(Calendar.YEAR))).toString();

		return lYear;
	}

	/**
	 * Ritorna l'ultimo giorno desiderato della settimana nel mese.
	 * <p>
	 *
	 * @param aDate
	 *            Data di riferimento.
	 * @param aDay
	 *            Giorno desiderato.
	 * @return ultimo giorno della settimena del mese.
	 */
	public static Date getLastSpecificDayOfMonth(Date aDate, int aDay) {
		// Se il parametro data è null ritorna null.
		if (aDate == null)
			return aDate;

		GregorianCalendar lGC = new GregorianCalendar();
		lGC.setTime(aDate);
		lGC.set(Calendar.DAY_OF_MONTH, lGC.getActualMaximum(Calendar.DAY_OF_MONTH));

		while (lGC.get(Calendar.DAY_OF_WEEK) != aDay)
			lGC.add(Calendar.DAY_OF_MONTH, -1);

		return lGC.getTime();
	}

	/**
	 * Ritorna il primo giorno desiderato della settimena nel mese.
	 * <p>
	 *
	 * @param aDate
	 *            data di riferimento.
	 * @param aDay
	 *            giorno desiderato.
	 * @return primo giorno della settimena del mese.
	 */
	public static Date getFirstSpecificDayOfMonth(Date aDate, int aDay) {
		if (aDate == null)
			return aDate;

		GregorianCalendar lGC = new GregorianCalendar();
		lGC.setTime(aDate);
		lGC.set(Calendar.DAY_OF_MONTH, lGC.getActualMinimum(Calendar.DAY_OF_MONTH));

		while (lGC.get(Calendar.DAY_OF_WEEK) != aDay)
			lGC.add(Calendar.DAY_OF_MONTH, 1);

		return lGC.getTime();
	}

	/**
	 * Controlla la validità di una data in formato <code>String</code>.
	 * <p>
	 *
	 * @param aDate
	 *            data da controllare
	 * @param aPattern
	 *            formato della data
	 * @return esito della verifica.
	 */
	public static boolean isValidDate(String aDate, String aPattern) {
		if (aDate == null)
			return false;

		try {
			SimpleDateFormat lSDF = new SimpleDateFormat(aPattern);
			lSDF.setLenient(false); // Evita il Rolling sulla data
			lSDF.parse(aDate);
		} catch (ParseException pex) {
			return false;
		}

		return true;

	}

	/**
	 * Controlla la validità di una data in formato <code>String</code>.
	 * <p>
	 *
	 * @param aDate
	 *            data da verificare in formato intero primitivo.
	 * @param aPattern
	 *            formattazione data.
	 * @return
	 */
	public static boolean isValidDate(int aDate, String aPattern) {
		try {
			SimpleDateFormat lSDF = new SimpleDateFormat(aPattern);
			lSDF.setLenient(false); // Evita il Rolling sulla data.
			lSDF.parse(Integer.toString(aDate));
		} catch (ParseException pex) {
			return false;
		}

		return true;
	}

	/**
	 * Ritorna il numero di mesi compresi tra 2 date, precisamente tra un range di Anno e Mese.
	 * <p>
	 *
	 * @param aFromYear
	 *            Anno di inizio range.
	 * @param aFromMonth
	 *            Mese di inizio range.
	 * @param aToYear
	 *            Anno di fine range.
	 * @param aToMonth
	 *            Mese di fine range.
	 * @return il numero di mesi compresi nel range.
	 */
	public static long countMonths(int aFromYear, int aFromMonth, int aToYear, int aToMonth) {
		GregorianCalendar lGCFrom = new GregorianCalendar(aFromYear, aFromMonth - 1, 1);
		GregorianCalendar lGCTo = new GregorianCalendar(aToYear, aToMonth - 1, 1);

		long lMillisecsGCFrom = (lGCFrom.getTime()).getTime(); // Converte in Oggetto Date ed tira fuori i
																// mSec
		long lMillisecsGCTo = (lGCTo.getTime()).getTime(); // Converte in Oggetto Date ed tira fuori i mSec

		long lDeltaMonths = ((lMillisecsGCTo - lMillisecsGCFrom) / 1000); // Secondi

		lDeltaMonths = lDeltaMonths / 60; // Minuti
		lDeltaMonths = lDeltaMonths / 60; // Ore
		lDeltaMonths = lDeltaMonths / 24; // Giorni
		lDeltaMonths = lDeltaMonths / 30; // Mesi
		lDeltaMonths = lDeltaMonths + 1; // Include il mese

		return lDeltaMonths;
	}

	/**
	 * Ritorna il numero di mesi compresi tra 2 date, precisamente tra un range di Anno e Mese.
	 * <p>
	 *
	 * @param aFromYear
	 *            Anno di inizio range.
	 * @param aFromMonth
	 *            Mese di inizio range.
	 * @param aToYear
	 *            Anno di fine range.
	 * @param aToMonth
	 *            Mese di fine range.
	 * @return il numero di mesi compresi nel range.
	 */
	public static long countMonths(String aFromYear, String aFromMonth, String aToYear, String aToMonth) {
		long lDeltaMonths = 0;

		int lFromYear = Integer.parseInt(aFromYear);
		int lFromMonth = Integer.parseInt(aFromMonth);
		int lToYear = Integer.parseInt(aToYear);
		int lToMonth = Integer.parseInt(aToMonth);

		lDeltaMonths = countMonths(lFromYear, lFromMonth, lToYear, lToMonth);

		return lDeltaMonths;
	}

	/**
	 * Ritorna la descrizione del mese corrispondente.
	 * <p>
	 *
	 * @param aValue
	 *            mese in cifre da decodificare .
	 * @return descrizione del mese.
	 */
	public static String getMonth(String aValue) {
		int lNumMese = Integer.parseInt(aValue);
		String lMese = new String();

		switch (lNumMese) {
		case Calendar.JANUARY:
			lMese = "Gennaio";
			break;
		case Calendar.FEBRUARY:
			lMese = "Febbraio";
			break;
		case Calendar.MARCH:
			lMese = "Marzo";
			break;
		case Calendar.APRIL:
			lMese = "Aprile";
			break;
		case Calendar.MAY:
			lMese = "Maggio";
			break;
		case Calendar.JUNE:
			lMese = "Giugno";
			break;
		case Calendar.JULY:
			lMese = "Luglio";
			break;
		case Calendar.AUGUST:
			lMese = "Agosto";
			break;
		case Calendar.SEPTEMBER:
			lMese = "Settembre";
			break;
		case Calendar.OCTOBER:
			lMese = "Ottobre";
			break;
		case Calendar.NOVEMBER:
			lMese = "Novembre";
			break;
		case Calendar.DECEMBER:
			lMese = "Dicembre";
			break;
		}

		return lMese;
	}

	/**
	 * Ritorna la descrizione del giorno corrispondente.
	 * <p>
	 *
	 * @param aValue
	 *            giorno in cifre da decodificare.
	 * @return descrizione del giorno.
	 */
	public static String getDay(String aValue) {
		int lNumDay = Integer.parseInt(aValue);
		String lDay = new String();

		switch (lNumDay) {
		case Calendar.MONDAY:
			lDay = "Lunedì";
			break;
		case Calendar.TUESDAY:
			lDay = "Martedì";
			break;
		case Calendar.WEDNESDAY:
			lDay = "Mercoledì";
			break;
		case Calendar.THURSDAY:
			lDay = "Giovedì";
			break;
		case Calendar.FRIDAY:
			lDay = "Venerdì";
			break;
		case Calendar.SATURDAY:
			lDay = "Sabato";
			break;
		case Calendar.SUNDAY:
			lDay = "Domenica";
			break;
		}

		return lDay;
	}

	/**
	 * Effettua il confronto tra due date e restituisce true se le date sono uguali. Il confronto viene
	 * effettuato solo su giorno, mese e anno. Vengono ignorati ora, minuti e secondi.
	 *
	 * @param aDateA
	 * @param aDateB
	 * @return true se le date sono identiche, false altrimenti
	 */
	public static boolean isEquals(Date aDateA, Date aDateB) {
		GregorianCalendar lDateA = new GregorianCalendar();
		GregorianCalendar lDateB = new GregorianCalendar();

		lDateA.setTime(aDateA);
		lDateB.setTime(aDateB);

		if (lDateA.get(Calendar.YEAR) == lDateB.get(Calendar.YEAR)
				&& lDateA.get(Calendar.MONTH) == lDateB.get(Calendar.MONTH)
				&& lDateA.get(Calendar.DAY_OF_MONTH) == lDateB.get(Calendar.DAY_OF_MONTH)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Confronta le due date e restituisce true se aDateA>aDateB. Il confronto viene fatto solo su giorni,
	 * mesi e anni, non su ore minuti e secondi.
	 *
	 * @param aDateA
	 * @param aDateB
	 * @return
	 */
	public static boolean isGreater(Date aDateA, Date aDateB) {
		String lGiorno = getDateToString(aDateA, "dd");
		String lMese = getDateToString(aDateA, "MM");
		String lAnno = getDateToString(aDateA, "yyyy");
		Date lDateA = getDate(lAnno, lMese, lGiorno);

		lGiorno = getDateToString(aDateB, "dd");
		lMese = getDateToString(aDateB, "MM");
		lAnno = getDateToString(aDateB, "yyyy");
		Date lDateB = getDate(lAnno, lMese, lGiorno);

		if (lDateA.after(lDateB)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Confronta le due date e restituisce true se aDateA<aDateB. Il confronto viene fatto solo su giorni,
	 * mesi e anni, non su ore minuti e secondi.
	 *
	 * @param aDateA
	 * @param aDateB
	 * @return
	 */
	public static boolean isLower(Date aDateA, Date aDateB) {
		String lGiorno = getDateToString(aDateA, "dd");
		String lMese = getDateToString(aDateA, "MM");
		String lAnno = getDateToString(aDateA, "yyyy");
		Date lDateA = getDate(lAnno, lMese, lGiorno);

		lGiorno = getDateToString(aDateB, "dd");
		lMese = getDateToString(aDateB, "MM");
		lAnno = getDateToString(aDateB, "yyyy");
		Date lDateB = getDate(lAnno, lMese, lGiorno);

		if (lDateA.before(lDateB)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Restituisce la differenza in giorni tra le due date passate in input
	 *
	 * @param aDataDal
	 * @param aDataAl
	 * @return
	 */
	public static int getIntervallo(Date aDataDal, Date aDataAl) {
		int lIntervallo = 0;
		long lFactorMilliSec = 60000 * 60 * 24; // trasforma un giorno in millisecondi
		long lDifferenza = ((aDataAl.getTime() - aDataDal.getTime()) / (lFactorMilliSec));

		lIntervallo = (int) lDifferenza;

		return lIntervallo;
	}

	public static int getDaysBetween(java.util.Date d1, java.util.Date d2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);

		return DateUtils.getDaysBetween(cal1, cal2);
	}

	/**
	 * Calcola il numero dei giorni tra due date. L'ordine delle due date non importa (non importa che la
	 * prima sia minore della seconda)
	 *
	 * @param d1
	 *            La prima data.
	 * @param d2
	 *            La seconda data.
	 *
	 * @return Il numero di giorni tra le due date. Torna 0 se le due date sono uguali
	 */
	public static int getDaysBetween(java.util.Calendar d1, java.util.Calendar d2) {
		// Se la data d1 è successiva alla data d2, le scambia
		if (d1.after(d2)) {
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}

		// Quanti giorni di differenza ci sono tra le due date se fossero dello stesso anno
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);

		// L'anno della data d2 (la maggiore)
		int y2 = d2.get(java.util.Calendar.YEAR);

		// Se le due date non sono dello stesso anno
		if (d1.get(java.util.Calendar.YEAR) != y2) {
			// Copia della data d1 (la minore),
			// per avere un reference diverso da quello passato al metodo
			// e poter manipolare la data senza modificare la data passata
			d1 = (java.util.Calendar) d1.clone();

			do {
				// Aggiunge ai giorni un anno (365 o 366)
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				// Aggiunge un anno alla data d1 (la minore) clonata
				d1.add(java.util.Calendar.YEAR, 1);
			} while (d1.get(java.util.Calendar.YEAR) != y2); // Finchè non sono stati aggiunti tutti gli anni
																// per cui le date differiscono
		}

		return days;
	}

}