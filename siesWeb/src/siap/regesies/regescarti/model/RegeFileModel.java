package siap.regesies.regescarti.model;

/**
* <p>Title: RegeFileModel</p>
* <p>Description: Classe Model che rappresenta il RegeFile</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class RegeFileModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 7929474163450394451L;

	private String mIdFile;
	private Date mDataInserimento;
	private String mCodComune;
	private String mDescrComune;
	// private Blob mFileBlob;
	private String mAf10fasc;
	private String mAf10prog;
	private String mAf10tipor;
	private BigDecimal mCodStato;
	// private BigDecimal mDescrStato;
	private String mDescErr;

	// COSTRUTTORE DI DEFAULT
	public RegeFileModel() {
		this.mIdFile = "";
		this.mDataInserimento = null;
		this.mCodComune = "";
		this.mDescrComune = "";
		// this.mFileBlob = null;
		this.mAf10fasc = "";
		this.mAf10prog = "";
		this.mAf10tipor = "";
		this.mCodStato = null;
		// this.mDescrStato = "";
		this.mDescErr = "";
	}

	// COSTRUTTORE DI COPIA
	public RegeFileModel(RegeFileModel aModel) {
		this.mIdFile = aModel.mIdFile;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodComune = aModel.mCodComune;
		this.mDescrComune = aModel.mDescrComune;
		// this.mFileBlob = aModel.mFileBlob;
		this.mAf10fasc = aModel.mAf10fasc;
		this.mAf10prog = aModel.mAf10prog;
		this.mAf10tipor = aModel.mAf10tipor;
		this.mCodStato = aModel.mCodStato;
		// this.mDescrStato = aModel.mDescrStato;
		this.mDescErr = aModel.mDescErr;
	}

	// COSTRUTTORE MODEL
	public RegeFileModel(String aIdFile, Date aDataInserimento, String aCodComune, String aDescrComune,
			// Blob aFileBlob,
			String aAf10fasc, String aAf10prog, String aAf10tipor, BigDecimal aCodStato,
			// BigDecimal aDescrStato,
			String aDescErr) {
		this.mIdFile = aIdFile;
		this.mDataInserimento = aDataInserimento;
		this.mCodComune = aCodComune;
		this.mDescrComune = aDescrComune;
		// this.mFileBlob = aFileBlob;
		this.mAf10fasc = aAf10fasc;
		this.mAf10prog = aAf10prog;
		this.mAf10tipor = aAf10tipor;
		this.mCodStato = aCodStato;
		// this.mDescrStato = aDescrStato;
		this.mDescErr = aDescErr;
	}

	//
	// METODI GET()
	//

	public String getIdFile() {
		return mIdFile;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodComune() {
		return mCodComune;
	}

	public String getDescrComune() {
		return mDescrComune;
	}

	// public Blob getFileBlob() { return mFileBlob; }
	public String getAf10fasc() {
		return mAf10fasc;
	}

	public String getAf10prog() {
		return mAf10prog;
	}

	public String getAf10tipor() {
		return mAf10tipor;
	}

	public BigDecimal getCodStato() {
		return mCodStato;
	}

	// public BigDecimal getDescrStato() { return mDescrStato; }
	public String getDescErr() {
		return mDescErr;
	}

	//
	// METODI SET()
	//

	public void setIdFile(String aValore) {
		mIdFile = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodComune(String aValore) {
		mCodComune = aValore;
	}

	public void setDescrComune(String aValore) {
		mDescrComune = aValore;
	}

	// public void setFileBlob(Blob aValore ) { mFileBlob = aValore; }
	public void setAf10fasc(String aValore) {
		mAf10fasc = aValore;
	}

	public void setAf10prog(String aValore) {
		mAf10prog = aValore;
	}

	public void setAf10tipor(String aValore) {
		mAf10tipor = aValore;
	}

	public void setCodStato(BigDecimal aValore) {
		mCodStato = aValore;
	}

	// public void setDescrStato(BigDecimal aValore ) { mDescrStato = aValore; }
	public void setDescErr(String aValore) {
		mDescErr = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdFile + " - " + mDataInserimento + " - " + mCodComune + " - " + mDescrComune + " - " +
		// mFileBlob +" - " +
				mAf10fasc + " - " + mAf10prog + " - " + mAf10tipor + " - " + mCodStato + " - " +
				// mDescrStato +" - " +
				mDescErr;

		return lStr;
	}

}