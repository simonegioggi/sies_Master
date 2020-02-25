package siap.sico.log.model;

/**
* <p>Title: LogAttivitaModel</p>
* <p>Description: Classe Model che rappresenta il LogAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class LogAttivitaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 972449418567911495L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	private String mRecord;
	private String mCodOperatore;
	private Date mData;
	private String mIpUtente;
	private String mAzioneContestoJava;
	private String mNome;
	private String mCognome;

	// COSTRUTTORE DI DEFAULT
	public LogAttivitaModel() {
		this.mRecord = "";
		this.mCodOperatore = "";
		this.mData = DateUtils.getSysDate();
		this.mIpUtente = "";
		this.mAzioneContestoJava = "";
		this.mNome = "";
		this.mCognome = "";
	}

	// COSTRUTTORE DI COPIA
	public LogAttivitaModel(LogAttivitaModel aModel) {
		this.mRecord = aModel.mRecord;
		this.mCodOperatore = aModel.mCodOperatore;
		this.mData = aModel.mData;
		this.mIpUtente = aModel.mIpUtente;
		this.mAzioneContestoJava = aModel.mAzioneContestoJava;
		this.mNome = aModel.mNome;
		this.mCognome = aModel.mCognome;
	}

	// COSTRUTTORE MODEL
	public LogAttivitaModel(String aRecord, String aCodOperatore, Date aData, String aIpUtente,
			String aAzioneContestoJava, String aNome, String aCognome) {
		this.mRecord = aRecord;
		this.mCodOperatore = aCodOperatore;
		this.mData = aData;
		this.mIpUtente = aIpUtente;
		this.mAzioneContestoJava = aAzioneContestoJava;
		this.mNome = aNome;
		this.mCognome = aCognome;
	}

	//
	// METODI GET()
	//

	public String getRecord() {
		return mRecord;
	}

	public String getCodOperatore() {
		return mCodOperatore;
	}

	public Date getData() {
		return mData;
	}

	public String getIpUtente() {
		return mIpUtente;
	}

	public String getAzioneContestoJava() {
		return mAzioneContestoJava;
	}

	public String getNome() {
		return mNome;
	}

	public String getCognome() {
		return mCognome;
	}

	//
	// METODI SET()
	//

	// 15/11/2010 Controllo Stringa Record > 4000
	public void setRecord(String aValore) {
		if (aValore.length() > 4000) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("LogAttività troncato perché log_rec.length() > 4000 ");
			mRecord = aValore.substring(0, 4000);
		} else {
			mRecord = aValore;
		}
	}

	public void setCodOperatore(String aValore) {
		mCodOperatore = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setIpUtente(String aValore) {
		mIpUtente = aValore;
	}

	public void setAzioneContestoJava(String aValore) {
		mAzioneContestoJava = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mRecord + " - " + mCodOperatore + " - " + mData + " - " + mIpUtente + " - "
				+ mAzioneContestoJava + " - " + mNome + " - " + mCognome;

		return lStr;
	}

}