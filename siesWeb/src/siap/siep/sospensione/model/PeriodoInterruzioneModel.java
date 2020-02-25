package siap.siep.sospensione.model;

import f3b.model.GenericModel;
import siap.sico.calendar.model.CalendarModel;

public class PeriodoInterruzioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 2902123546404478846L;
	private CalendarModel mQuantum;
	private String mPosizioneGiuridica;

	public PeriodoInterruzioneModel() {
		this.mQuantum = new CalendarModel();
		this.mPosizioneGiuridica = "";
	}

	// METODI GET
	public CalendarModel getQuantum() {
		return mQuantum;
	}

	public String getPosizioneGiuridica() {
		return mPosizioneGiuridica;
	}

	// METODI SET
	public void setQuantum(CalendarModel aValore) {
		mQuantum = aValore;
	}

	public void setPosizioneGiuridica(String aValore) {
		mPosizioneGiuridica = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mQuantum + " - " + mPosizioneGiuridica;

		return lStr;
	}

}