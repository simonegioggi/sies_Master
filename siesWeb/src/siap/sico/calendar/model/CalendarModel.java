package siap.sico.calendar.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: CalendarModel
 * </p>
 * <p>
 * Description: Classe Model per il calcolo delle date
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class CalendarModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1147285223772990178L;

	private Date mDataInizio;
	private Date mDataFine;
	private int mGG;
	private int mMM;
	private int mAA;
	private double mImportoMulta;
	private double mImportoAmmenda;
	private String ErrCode;

	public CalendarModel() {
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mAA = 0;
		this.mMM = 0;
		this.mGG = 0;
		this.mImportoMulta = 0;
		this.mImportoAmmenda = 0;
		this.ErrCode = "";
	}

	public CalendarModel(CalendarModel aModel) {
		this.mDataInizio = aModel.getDataInizio();
		this.mDataFine = aModel.getDataFine();
		this.mAA = aModel.getNumAnni();
		this.mMM = aModel.getNumMesi();
		this.mGG = aModel.getNumGiorni();
		this.mImportoMulta = aModel.getImportoMulta();
		this.mImportoAmmenda = aModel.getImportoAmmenda();
		this.ErrCode = aModel.getErrorMsg();
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public int getNumGiorni() {
		return mGG;
	}

	public int getNumMesi() {
		return mMM;
	}

	public int getNumAnni() {
		return mAA;
	}

	public double getImportoAmmenda() {
		return mImportoAmmenda;
	}

	public double getImportoMulta() {
		return mImportoMulta;
	}

	public String getErrorMsg() {
		return ErrCode;
	}

	public void setDataInizio(Date aDate) {
		this.mDataInizio = aDate;
	}

	public void setDataFine(Date aDate) {
		this.mDataFine = aDate;
	}

	public void setNumGiorni(int aValue) {
		this.mGG = aValue;
	}

	public void setNumMesi(int aValue) {
		this.mMM = aValue;
	}

	public void setNumAnni(int aValue) {
		this.mAA = aValue;
	}

	public void setNumGiorni(BigDecimal aValue) {
		if (aValue != null) {
			this.mGG = aValue.intValue();
		} else
			this.mGG = 0;
	}

	public void setNumMesi(BigDecimal aValue) {
		if (aValue != null) {
			this.mMM = aValue.intValue();
		} else
			this.mMM = 0;
	}

	public void setNumAnni(BigDecimal aValue) {
		if (aValue != null) {
			this.mAA = aValue.intValue();
		} else
			this.mAA = 0;
	}

	public void setImportoMulta(double aValue) {
		this.mImportoMulta = aValue;
	}

	public void setImportoAmmenda(double aValue) {
		this.mImportoAmmenda = aValue;
	}

	public void setErrorMsg(String aString) {
		this.ErrCode = aString;
	}

	public boolean isQuantumZero() {
		return (getNumAnni() == 0 && getNumMesi() == 0 && getNumGiorni() == 0);
	}

	// public String toString()
	// {
	// String lStr = new String();
	//
	// lStr = "" +
	// mDataInizio +" - " +
	// mDataFine +" - " +
	// mGG +" - " +
	// mMM +" - " +
	// mAA +" - " +
	// mImportoMulta +" - " +
	// mImportoAmmenda +" - " +
	// ErrCode;
	//
	// return lStr;
	// }
	// public String toString()
	// {
	// String lStr = new String();
	//
	// lStr = "[ Data Inizio = " + mDataInizio +"]\n" +
	// "[ Data Fine = " + mDataFine +"]\n" +
	// "[ Giorni = " + mGG +"]\n" +
	// "[ Mesi = " + mMM +"]\n" +
	// "[ Anni = " + mAA +"]\n" +
	// "[ Multa = " + mImportoMulta +"]\n" +
	// "[ Ammenda = " + mImportoAmmenda +"]\n" +
	// "[ ErrCode = " + ErrCode+"]";
	//
	// return lStr;
	// }

	public String toString() {
		String lStr = new String();

		lStr = "[ Anni = " + mAA + ", Mesi = " + mMM + ", Giorni = " + mGG + "] " + "[ Data Inizio = "
				+ mDataInizio + ", Data Fine = " + mDataFine + "] " + "[ Multa = " + mImportoMulta
				+ ", Ammenda = " + mImportoAmmenda + "] " + "[ ErrCode = " + ErrCode + "]";

		return lStr;
	}

	public String getStringPerStampa() {
		String lStr = "";

		if (this.mAA != 0)
			lStr = "Anni " + mAA;
		if (this.mMM != 0) {
			if (lStr.length() > 0)
				lStr += " ";

			lStr += "Mesi " + this.mMM;
		}
		if (this.mGG != 0) {
			if (lStr.length() > 0)
				lStr += " ";

			lStr += "Giorni " + this.mGG;
		}

		return lStr;
	}

}