package siap.siep.statis.model;

/**
* <p>Title: StatRisSiesModel</p>
* <p>Description: Classe Model che rappresenta il StatRisSies</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class StatRisSiesModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 1548913261097940930L;

	private Integer mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mCodUfficio;
	private String mDescrUfficio;
	private Date mDataEstrazione;
	private String mCodPosizioneGiuridica;
	private String mDescrPosizioneGiuridica;
	private String mCodStatoProcedimento;
	private String mDescrStatoProcedimento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private Date mDataInserimento;

	// COSTRUTTORE DI DEFAULT
	public StatRisSiesModel() {
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mCodUfficio = "";
		this.mDescrUfficio = "";
		this.mDataEstrazione = null;
		this.mCodPosizioneGiuridica = "";
		this.mDescrPosizioneGiuridica = "";
		this.mCodStatoProcedimento = "";
		this.mDescrStatoProcedimento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mCodOperatoreInserimento = "";
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mDataInserimento = null;
	}

	// COSTRUTTORE DI COPIA
	public StatRisSiesModel(StatRisSiesModel aModel) {
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mCodUfficio = aModel.mCodUfficio;
		this.mDescrUfficio = aModel.mDescrUfficio;
		this.mDataEstrazione = aModel.mDataEstrazione;
		this.mCodPosizioneGiuridica = aModel.mCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aModel.mDescrPosizioneGiuridica;
		this.mCodStatoProcedimento = aModel.mCodStatoProcedimento;
		this.mDescrStatoProcedimento = aModel.mDescrStatoProcedimento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
	}

	// COSTRUTTORE MODEL
	public StatRisSiesModel(Integer aChiaveAnno, BigDecimal aChiaveProgr, String aCodUfficio,
			String aDescrUfficio, Date aDataEstrazione, String aCodPosizioneGiuridica,
			String aDescrPosizioneGiuridica, String aCodStatoProcedimento, String aDescrStatoProcedimento,
			BigDecimal aFasSieIdFascicoloSiep, String aCodOperatoreInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, Date aDataInserimento) {
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mCodUfficio = aCodUfficio;
		this.mDescrUfficio = aDescrUfficio;
		this.mDataEstrazione = aDataEstrazione;
		this.mCodPosizioneGiuridica = aCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aDescrPosizioneGiuridica;
		this.mCodStatoProcedimento = aCodStatoProcedimento;
		this.mDescrStatoProcedimento = aDescrStatoProcedimento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mDataInserimento = aDataInserimento;
	}

	//
	// METODI GET()
	//

	public Integer getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getDescrUfficio() {
		return mDescrUfficio;
	}

	public Date getDataEstrazione() {
		return mDataEstrazione;
	}

	public String getCodPosizioneGiuridica() {
		return mCodPosizioneGiuridica;
	}

	public String getDescrPosizioneGiuridica() {
		return mDescrPosizioneGiuridica;
	}

	public String getCodStatoProcedimento() {
		return mCodStatoProcedimento;
	}

	public String getDescrStatoProcedimento() {
		return mDescrStatoProcedimento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	//
	// METODI SET()
	//

	public void setChiaveAnno(Integer aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDescrUfficio(String aValore) {
		mDescrUfficio = aValore;
	}

	public void setDataEstrazione(Date aValore) {
		mDataEstrazione = aValore;
	}

	public void setCodPosizioneGiuridica(String aValore) {
		mCodPosizioneGiuridica = aValore;
	}

	public void setDescrPosizioneGiuridica(String aValore) {
		mDescrPosizioneGiuridica = aValore;
	}

	public void setCodStatoProcedimento(String aValore) {
		mCodStatoProcedimento = aValore;
	}

	public void setDescrStatoProcedimento(String aValore) {
		mDescrStatoProcedimento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

}