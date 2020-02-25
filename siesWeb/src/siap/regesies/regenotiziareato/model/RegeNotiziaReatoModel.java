package siap.regesies.regenotiziareato.model;

/**
* <p>Title: RegeNotiziaReatoModel</p>
* <p>Description: Classe Model che rappresenta il RegeNotiziaReato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import siap.siep.notiziareato.model.NotiziaReatoModel;
import f3b.model.GenericModel;

public class RegeNotiziaReatoModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -4305520987027667760L;

	private String mIdFile;
	private String mProgrNotizia;
	private Date mDataPervenimento;
	private String mAcquisizioneDiretta;
	private Date mDataFatto;
	private String mCodFonte;
	private String mDescrFonte;
	private String mTipoFonte;
	private String mCodComuneFonte;
	private String mDescrComuneFonte;
	private String mNumRegAutorita;
	private String mLuogoProvenienza;
	private Date mDataAcquisizione;
	private String mNumeroRicevuta;
	private String mDescrizioneFonte;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public RegeNotiziaReatoModel() {
		this.mIdFile = "";
		this.mProgrNotizia = "";
		this.mDataPervenimento = null;
		this.mAcquisizioneDiretta = "";
		this.mDataFatto = null;
		this.mCodFonte = "";
		this.mDescrFonte = "";
		this.mTipoFonte = "";
		this.mCodComuneFonte = "";
		this.mDescrComuneFonte = "";
		this.mNumRegAutorita = "";
		this.mLuogoProvenienza = "";
		this.mDataAcquisizione = null;
		this.mNumeroRicevuta = "";
		this.mDescrizioneFonte = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
	}

	// COSTRUTTORE DI COPIA
	public RegeNotiziaReatoModel(RegeNotiziaReatoModel aModel) {
		this.mIdFile = aModel.mIdFile;
		this.mProgrNotizia = aModel.mProgrNotizia;
		this.mDataPervenimento = aModel.mDataPervenimento;
		this.mAcquisizioneDiretta = aModel.mAcquisizioneDiretta;
		this.mDataFatto = aModel.mDataFatto;
		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mTipoFonte = aModel.mTipoFonte;
		this.mCodComuneFonte = aModel.mCodComuneFonte;
		this.mDescrComuneFonte = aModel.mDescrComuneFonte;
		this.mNumRegAutorita = aModel.mNumRegAutorita;
		this.mLuogoProvenienza = aModel.mLuogoProvenienza;
		this.mDataAcquisizione = aModel.mDataAcquisizione;
		this.mNumeroRicevuta = aModel.mNumeroRicevuta;
		this.mDescrizioneFonte = aModel.mDescrizioneFonte;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	public NotiziaReatoModel toNotiziaReato() {
		NotiziaReatoModel lNot = new NotiziaReatoModel();

		lNot.setProgrNotizia(this.mProgrNotizia);
		lNot.setDataPervenimento(this.mDataPervenimento);
		lNot.setAcquisizioneDiretta(this.mAcquisizioneDiretta);
		lNot.setDataFatto(this.mDataFatto);
		lNot.setCodFonte(this.mCodFonte);
		lNot.setDescrFonte(this.mDescrFonte);
		lNot.setTipoFonte(this.mTipoFonte);
		lNot.setCodComuneFonte(this.mCodComuneFonte);
		lNot.setDescrComuneFonte(this.mDescrComuneFonte);
		lNot.setNumRegAutorita(this.mNumRegAutorita);
		lNot.setLuogoProvenienza(this.mLuogoProvenienza);
		lNot.setDataAcquisizione(this.mDataAcquisizione);
		lNot.setNumeroRicevuta(this.mNumeroRicevuta);
		lNot.setDescrizioneFonte(this.mDescrizioneFonte);
		lNot.setCodOperatoreInserimento(this.mCodOperatoreInserimento);
		lNot.setDataInserimento(this.mDataInserimento);
		lNot.setCodUfficioInserimento(this.mCodUfficioInserimento);
		lNot.setDescrUfficioInserimento(this.mDescrUfficioInserimento);
		lNot.setCodOperatoreAggiornamento(this.mCodOperatoreAggiornamento);
		lNot.setDataAggiornamento(this.mDataAggiornamento);
		lNot.setCodUfficioAggiornamento(this.mCodUfficioAggiornamento);
		lNot.setDescrUfficioAggiornamento(this.mDescrUfficioAggiornamento);
		// Inserite successivamente per richiesta CUI
		lNot.setCodAutoritaFoto("-");
		lNot.setCodComuneFoto("-");

		return lNot;
	}

	// COSTRUTTORE MODEL
	public RegeNotiziaReatoModel(String aIdFile, String aProgrNotizia, Date aDataPervenimento,
			String aAcquisizioneDiretta, Date aDataFatto, String aCodFonte, String aDescrFonte,
			String aTipoFonte, String aCodComuneFonte, String aDescrComuneFonte, String aNumRegAutorita,
			String aLuogoProvenienza, Date aDataAcquisizione, String aNumeroRicevuta,
			String aDescrizioneFonte, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdFile = aIdFile;
		this.mProgrNotizia = aProgrNotizia;
		this.mDataPervenimento = aDataPervenimento;
		this.mAcquisizioneDiretta = aAcquisizioneDiretta;
		this.mDataFatto = aDataFatto;
		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mTipoFonte = aTipoFonte;
		this.mCodComuneFonte = aCodComuneFonte;
		this.mDescrComuneFonte = aDescrComuneFonte;
		this.mNumRegAutorita = aNumRegAutorita;
		this.mLuogoProvenienza = aLuogoProvenienza;
		this.mDataAcquisizione = aDataAcquisizione;
		this.mNumeroRicevuta = aNumeroRicevuta;
		this.mDescrizioneFonte = aDescrizioneFonte;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	//
	// METODI GET()
	//

	public String getIdFile() {
		return mIdFile;
	}

	public String getProgrNotizia() {
		return mProgrNotizia;
	}

	public Date getDataPervenimento() {
		return mDataPervenimento;
	}

	public String getAcquisizioneDiretta() {
		return mAcquisizioneDiretta;
	}

	public Date getDataFatto() {
		return mDataFatto;
	}

	public String getCodFonte() {
		return mCodFonte;
	}

	public String getDescrFonte() {
		return mDescrFonte;
	}

	public String getTipoFonte() {
		return mTipoFonte;
	}

	public String getCodComuneFonte() {
		return mCodComuneFonte;
	}

	public String getDescrComuneFonte() {
		return mDescrComuneFonte;
	}

	public String getNumRegAutorita() {
		return mNumRegAutorita;
	}

	public String getLuogoProvenienza() {
		return mLuogoProvenienza;
	}

	public Date getDataAcquisizione() {
		return mDataAcquisizione;
	}

	public String getNumeroRicevuta() {
		return mNumeroRicevuta;
	}

	public String getDescrizioneFonte() {
		return mDescrizioneFonte;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	//
	// METODI SET()
	//

	public void setIdFile(String aValore) {
		mIdFile = aValore;
	}

	public void setProgrNotizia(String aValore) {
		mProgrNotizia = aValore;
	}

	public void setDataPervenimento(Date aValore) {
		mDataPervenimento = aValore;
	}

	public void setAcquisizioneDiretta(String aValore) {
		mAcquisizioneDiretta = aValore;
	}

	public void setDataFatto(Date aValore) {
		mDataFatto = aValore;
	}

	public void setCodFonte(String aValore) {
		mCodFonte = aValore;
	}

	public void setDescrFonte(String aValore) {
		mDescrFonte = aValore;
	}

	public void setTipoFonte(String aValore) {
		mTipoFonte = aValore;
	}

	public void setCodComuneFonte(String aValore) {
		mCodComuneFonte = aValore;
	}

	public void setDescrComuneFonte(String aValore) {
		mDescrComuneFonte = aValore;
	}

	public void setNumRegAutorita(String aValore) {
		mNumRegAutorita = aValore;
	}

	public void setLuogoProvenienza(String aValore) {
		mLuogoProvenienza = aValore;
	}

	public void setDataAcquisizione(Date aValore) {
		mDataAcquisizione = aValore;
	}

	public void setNumeroRicevuta(String aValore) {
		mNumeroRicevuta = aValore;
	}

	public void setDescrizioneFonte(String aValore) {
		mDescrizioneFonte = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdFile + " - " + mProgrNotizia + " - " + mDataPervenimento + " - " + mAcquisizioneDiretta
				+ " - " + mDataFatto + " - " + mCodFonte + " - " + mDescrFonte + " - " + mTipoFonte + " - "
				+ mCodComuneFonte + " - " + mDescrComuneFonte + " - " + mNumRegAutorita + " - "
				+ mLuogoProvenienza + " - " + mDataAcquisizione + " - " + mNumeroRicevuta + " - "
				+ mDescrizioneFonte + " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento;

		return lStr;
	}

}