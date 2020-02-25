package siap.siep.statis.util;

import java.util.Date;

import f3b.util.DateUtils;

/**
 * MEV_39: creata classe di utilities
 *
 * @author Gioggi
 *
 */
public class StatsUtils {

	public static String calcolaGiorniMesiFinali(String anno) {

		Date dataCorrente = DateUtils.getSysDate();
		String annoCorrente = DateUtils.getYearToString(dataCorrente);
		String meseCorrente = DateUtils.getMonthToString(dataCorrente);
		String giornoCorrente = DateUtils.getDayToString(dataCorrente);
		String ggmmFinali = "";
		if (annoCorrente.equals(anno))
			ggmmFinali = giornoCorrente + meseCorrente;
		else
			ggmmFinali = "3112";
		return ggmmFinali;
	}

	public static String calcolaDataFinale(String anno) {

		Date dataCorrente = DateUtils.getSysDate();
		String annoCorrente = DateUtils.getYearToString(dataCorrente);
		String dataFinale = "";
		if (annoCorrente.equals(anno))
			dataFinale = DateUtils.getDateToString(dataCorrente, "dd/MM/yyyy");
		else
			dataFinale = "31/12/" + anno;
		return dataFinale;
	}

	// public static String calcolaDataIniziale(String soloAnnoFinale) {
	//
	// Calendar oggi = Calendar.getInstance();
	// oggi.setTime(DateUtils.getSysDate());
	// int giorno = oggi.get(Calendar.DAY_OF_MONTH);
	// int mese = oggi.get(Calendar.MONTH) + 1;
	// String dataIniziale = "" + giorno + "/" + "" + mese + "/"
	// + (new Integer(soloAnnoFinale).intValue() - 7); // DATA FITTIZIA
	// Date d = DateUtils.moveDateTo(DateUtils.getDate(dataIniziale, "dd/MM/yyyy"), Calendar.DAY_OF_MONTH,
	// 1);
	// dataIniziale = DateUtils.getDateToString(d, "dd/MM/yyyy");
	// return dataIniziale;
	// }
	//
	// public static String calcolaGiorniMesiIniziali(String dataIniziale) {
	//
	// Date di = DateUtils.getDate(dataIniziale, "dd/MM/yyyy");
	// return DateUtils.getDayToString(di) + DateUtils.getMonthToString(di);
	// }
	//
	// public static String calcolaDataInizialeCompleta(String dataFinale) {
	//
	// Calendar df = Calendar.getInstance();
	// df.setTime(DateUtils.getDate(dataFinale, "dd/MM/yyyy"));
	// int giorno = df.get(Calendar.DAY_OF_MONTH);
	// int mese = df.get(Calendar.MONTH) + 1;
	// int anno = df.get(Calendar.YEAR);
	// // DATA FITTIZIA
	// String dataIniziale = "" + giorno + "/" + "" + mese + "/" + (anno - 7);
	// Date d = DateUtils.moveDateTo(DateUtils.getDate(dataIniziale, "dd/MM/yyyy"), Calendar.DAY_OF_MONTH,
	// 1);
	// dataIniziale = DateUtils.getDateToString(d, "dd/MM/yyyy");
	// return dataIniziale;
	// }

}