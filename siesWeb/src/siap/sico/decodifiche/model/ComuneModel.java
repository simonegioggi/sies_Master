package siap.sico.decodifiche.model;

/**
 * <p>Title: ComuneModel</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.util.Date;

import f3b.model.GenericModel;

public class ComuneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2843687370809917623L;

	private String mCodComune;
	private String mCodProvincia;
	private String mDescrizione;
	private String mCap;
	private Date mDataCaricamentoRege;
	private String mCodSedeGiudiziaria;
	private String mDescrSedeGiudiziaria;
	private boolean mControlloOmonimi;
	private String mFlagValidita;			// 2021/05/17 MEV_21
	private String mCodCatastaleComune;		// 2021/05/17 MEV_21
	private Date mDataAggiornamentoComune;	// 2021/05/17 MEV_21
	private Date mDataFineValiditaComune;	// 2021/05/17 MEV_21

	// COSTRUTTORE DI DEFAULT
	public ComuneModel() {
		this.mCodComune = "";
		this.mCodProvincia = "";
		this.mDescrizione = "";
		this.mCap = "";
		this.mDataCaricamentoRege = null;
		this.mCodSedeGiudiziaria = "";
		this.mDescrSedeGiudiziaria = "";
		this.mControlloOmonimi = false;
		this.mFlagValidita = "";
		this.mCodCatastaleComune = "";
		this.mDataAggiornamentoComune = null;
		this.mDataFineValiditaComune = null;
	}

	// COSTRUTTORE DI COPIA
	public ComuneModel(ComuneModel aModel) {
		this.mCodComune = aModel.mCodComune;
		this.mCodProvincia = aModel.mCodProvincia;
		this.mDescrizione = aModel.mDescrizione;
		this.mCap = aModel.mCap;
		this.mDataCaricamentoRege = aModel.mDataCaricamentoRege;
		this.mCodSedeGiudiziaria = aModel.mCodSedeGiudiziaria;
		this.mDescrSedeGiudiziaria = aModel.mDescrSedeGiudiziaria;
		this.mControlloOmonimi = aModel.mControlloOmonimi;
		this.mFlagValidita = aModel.mFlagValidita;
		this.mCodCatastaleComune = aModel.mCodCatastaleComune;
		this.mDataAggiornamentoComune = aModel.mDataAggiornamentoComune;
		this.mDataFineValiditaComune = aModel.mDataFineValiditaComune;
	}

	// COSTRUTTORE MODEL
	public ComuneModel(String aCodComune, String aCodProvincia, String aDescrizione, String aCap,
			Date aDataCaricamentoRege, String aCodSedeGiu, String aDescrSedeGiu, boolean aControlloOmonimi, 
			String aFlagValidita, String aCodCatastaleComune, Date aDataAggiornamentoComune, Date aDataFineValiditaComune) {
		this.mCodComune = aCodComune;
		this.mCodProvincia = aCodProvincia;
		this.mDescrizione = aDescrizione;
		this.mCap = aCap;
		this.mDataCaricamentoRege = aDataCaricamentoRege;
		this.mCodSedeGiudiziaria = aCodSedeGiu;
		this.mDescrSedeGiudiziaria = aDescrSedeGiu;
		this.mControlloOmonimi = aControlloOmonimi;
		this.mFlagValidita = aFlagValidita;
		this.mCodCatastaleComune = aCodCatastaleComune;
		this.mDataAggiornamentoComune = aDataAggiornamentoComune;
		this.mDataFineValiditaComune = aDataFineValiditaComune;
	}

	//
	// METODI GET()
	//
	public String getCodComune() {
		return mCodComune;
	}

	public String getCodProvincia() {
		return mCodProvincia;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	public String getCap() {
		return mCap;
	}

	public Date getDataCaricamentoRege() {
		return mDataCaricamentoRege;
	}

	public String getCodSedeGiudiziaria() {
		return mCodSedeGiudiziaria;
	}

	public String getDescrSedeGiudiziaria() {
		return mDescrSedeGiudiziaria;
	}

	public boolean getControlloOmonimi() {
		return mControlloOmonimi;
	}
	
	public String getFlagValidita() {
		return mFlagValidita;
	}

	public String getCodCatastaleComune() {
		return mCodCatastaleComune;
	}
	
	public Date getDataAggiornamentoComune() {
		return mDataAggiornamentoComune;
	}

	public Date getDataFineValiditaComune() {
		return mDataFineValiditaComune;
	}
	
	//
	// METODI SET()
	//
	public void setCodComune(String aValore) {
		this.mCodComune = aValore;
	}

	public void setCodProvincia(String aValore) {
		this.mCodProvincia = aValore;
	}

	public void setDescrizione(String aValore) {
		this.mDescrizione = aValore;
	}

	public void setCap(String aValore) {
		this.mCap = aValore;
	}

	public void setDataCaricamentoRege(Date aValore) {
		this.mDataCaricamentoRege = aValore;
	}

	public void setCodSedeGiudiziaria(String aValore) {
		this.mCodSedeGiudiziaria = aValore;
	}

	public void setDescrSedeGiudiziaria(String aValore) {
		this.mDescrSedeGiudiziaria = aValore;
	}

	public void setControlloOmonimi(boolean aValore) {
		this.mControlloOmonimi = aValore;
	}

	public void setFlagValidita(String aValore) {
		this.mFlagValidita = aValore;
	}

	public void setCodCatastaleComune(String aValore) {
		this.mCodCatastaleComune = aValore;
	}

	public void setDataAggiornamentoComune(Date aValore) {
		this.mDataAggiornamentoComune = aValore;
	}

	public void setDataFineValiditaComune(Date aValore) {
		this.mDataFineValiditaComune = aValore;
	}

	public String toString() {
		String lToString = this.mCodComune + " - " + this.mCodProvincia + " - " + this.mDescrizione + " - "
				+ this.mCap + " - " + this.mDataCaricamentoRege + " - " + this.mCodSedeGiudiziaria + " - "
				+ this.mDescrSedeGiudiziaria + " - " + this.mControlloOmonimi + " - " + this.mFlagValidita + " - "
				+ this.mCodCatastaleComune + " - " + this.mDataAggiornamentoComune + " - " + this.mCodSedeGiudiziaria;
		return lToString;
	}

}