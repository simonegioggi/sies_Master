package siap.sius.depositoordinanzapc.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

// 20261124-MEV_2026-124_SG - 3.12 periodo di sospensione calcolato tra data decorrenza e data "Fino al"
public class PeriodoSospensioneUtil {

	private static boolean isZero(BigDecimal aValue) {
		return aValue == null || aValue.signum() == 0;
	}

	/**
	 * @return {anni, mesi, giorni} calcolati tra le due date se anni/mesi/giorni sono tutti a zero (o nulli) e le
	 *         date sono valorizzate e coerenti; altrimenti null
	 */
	public static BigDecimal[] getPeriodoCalcolato(BigDecimal aAnni, BigDecimal aMesi, BigDecimal aGiorni,
			Date aDataDecorrenza, Date aDataFine) {
		if (!isZero(aAnni) || !isZero(aMesi) || !isZero(aGiorni) || aDataDecorrenza == null || aDataFine == null
				|| aDataFine.before(aDataDecorrenza))
			return null;
		Calendar lDa = Calendar.getInstance();
		lDa.setTime(aDataDecorrenza);
		Calendar lA = Calendar.getInstance();
		lA.setTime(aDataFine);
		lDa.set(Calendar.HOUR_OF_DAY, 0);
		lDa.set(Calendar.MINUTE, 0);
		lDa.set(Calendar.SECOND, 0);
		lDa.set(Calendar.MILLISECOND, 0);
		lA.set(Calendar.HOUR_OF_DAY, 0);
		lA.set(Calendar.MINUTE, 0);
		lA.set(Calendar.SECOND, 0);
		lA.set(Calendar.MILLISECOND, 0);
		// Mesi interi contenuti nell'intervallo, poi i giorni residui
		int lTotMesi = (lA.get(Calendar.YEAR) - lDa.get(Calendar.YEAR)) * 12
				+ (lA.get(Calendar.MONTH) - lDa.get(Calendar.MONTH));
		Calendar lTmp = (Calendar) lDa.clone();
		lTmp.add(Calendar.MONTH, lTotMesi);
		if (lTmp.after(lA)) {
			lTotMesi--;
			lTmp = (Calendar) lDa.clone();
			lTmp.add(Calendar.MONTH, lTotMesi);
		}
		int lGiorni = (int) Math.round((lA.getTimeInMillis() - lTmp.getTimeInMillis()) / 86400000.0);
		int lAnni = lTotMesi / 12;
		int lMesi = lTotMesi % 12;
		return new BigDecimal[] { new BigDecimal(lAnni), new BigDecimal(lMesi), new BigDecimal(lGiorni) };
	}
}
