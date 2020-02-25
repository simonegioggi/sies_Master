package siap.siep.statis.model;

/**
* <p>Title: IspProvvedimentiModel</p>
* <p>Description: Classe Model che rappresenta il IspProvvedimenti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class IspProvvedimentiModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -2088655275835209127L;
	private BigDecimal mIdFascicoloSiep;
	private BigDecimal mNres;
	private Integer mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mUltTipoProvvedimento;
	private String mUltCodMotivo;
	private String mPenTipoProvvedimento;
	private String mPenCodMotivo;
	private Integer mConta;
	private String mCodUfficio;
	private String mDescrUfficio;
	private String mCodStatoProcedimento;
	private String mDescrStatoProcedimento;
	private Integer mCodStatoFascicoloRes;
	private String mDescrStatoFascicoloRes;
	private String mCodPosizioneGiuridica;
	private String mDescrPosizioneGiuridica;
	// NGG Statistiche SIEP
	private String mCodUfficioInserimento;
	private BigDecimal mChiaveProgrOrig;
	private String mDescUfficioInserimento;
	private String mTipoCampo;
	//24/11/2019 (INTERVENTO POST COLLAUDO 11.3) : aggiungo per gestire il tipoMisura Provvisoria
	private String mTipoMisura;

	// COSTRUTTORE DI DEFAULT
	public IspProvvedimentiModel() {
		this.mIdFascicoloSiep = null;
		this.mNres = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mUltTipoProvvedimento = "";
		this.mUltCodMotivo = "";
		this.mPenTipoProvvedimento = "";
		this.mPenCodMotivo = "";
		this.mConta = null;
		this.mCodUfficio = "";
		this.mDescrUfficio = "";
		this.mCodStatoProcedimento = "";
		this.mDescrStatoProcedimento = "";
		this.mCodStatoFascicoloRes = null;
		this.mDescrStatoFascicoloRes = "";
		this.mCodPosizioneGiuridica = "";
		this.mDescrPosizioneGiuridica = "";
		// NGG Statistiche SIEP
		this.mCodUfficioInserimento = null;
		this.mChiaveProgrOrig = null;
		this.mDescUfficioInserimento = null;
		this.mTipoCampo = null;
	}

	// COSTRUTTORE DI COPIA
	public IspProvvedimentiModel(IspProvvedimentiModel aModel) {
		this.mIdFascicoloSiep = aModel.mIdFascicoloSiep;
		this.mNres = aModel.mNres;
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mUltTipoProvvedimento = aModel.mUltTipoProvvedimento;
		this.mUltCodMotivo = aModel.mUltCodMotivo;
		this.mPenTipoProvvedimento = aModel.mPenTipoProvvedimento;
		this.mPenCodMotivo = aModel.mPenCodMotivo;
		this.mConta = aModel.mConta;
		this.mCodUfficio = aModel.mCodUfficio;
		this.mDescrUfficio = aModel.mDescrUfficio;
		this.mCodStatoProcedimento = aModel.mCodStatoProcedimento;
		this.mDescrStatoProcedimento = aModel.mDescrStatoProcedimento;
		this.mCodStatoFascicoloRes = aModel.mCodStatoFascicoloRes;
		this.mDescrStatoFascicoloRes = aModel.mDescrStatoFascicoloRes;
		this.mCodPosizioneGiuridica = aModel.mCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aModel.mDescrPosizioneGiuridica;
		// NGG Statistiche SIEP
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mChiaveProgrOrig = aModel.mChiaveProgrOrig;
		this.mDescUfficioInserimento = aModel.mDescUfficioInserimento;
		this.mTipoCampo = aModel.mTipoCampo;
	}

	// COSTRUTTORE MODEL
	public IspProvvedimentiModel(BigDecimal aIdFascicoloSiep, BigDecimal aNres, Integer aChiaveAnno,
			BigDecimal aChiaveProgr, String aUltTipoProvvedimento, String aUltCodMotivo,
			String aPenTipoProvvedimento, String aPenCodMotivo, Integer aConta, String aCodUfficio,
			String aDescrUfficio, String aCodStatoProcedimento, String aDescrStatoProcedimento,
			Integer aCodStatoFascicoloRes, String aDescrStatoFascicoloRes, String aCodPosizioneGiuridica,
			String aDescrPosizioneGiuridica,
			// NGG Statistiche SIEP
			String aCodUfficioInserimento, BigDecimal aChiaveProgrOrig, String aDescUfficioInserimento,
			String aTipoCampo) {
		this.mIdFascicoloSiep = aIdFascicoloSiep;
		this.mNres = aNres;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mUltTipoProvvedimento = aUltTipoProvvedimento;
		this.mUltCodMotivo = aUltCodMotivo;
		this.mPenTipoProvvedimento = aPenTipoProvvedimento;
		this.mPenCodMotivo = aPenCodMotivo;
		this.mConta = aConta;
		this.mCodUfficio = aCodUfficio;
		this.mDescrUfficio = aDescrUfficio;
		this.mCodStatoProcedimento = aCodStatoProcedimento;
		this.mDescrStatoProcedimento = aDescrStatoProcedimento;
		this.mCodStatoFascicoloRes = aCodStatoFascicoloRes;
		this.mDescrStatoFascicoloRes = aDescrStatoFascicoloRes;
		this.mCodPosizioneGiuridica = aCodPosizioneGiuridica;
		this.mDescrPosizioneGiuridica = aDescrPosizioneGiuridica;
		// NGG Statistiche SIEP
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mChiaveProgrOrig = aChiaveProgrOrig;
		this.mDescUfficioInserimento = aDescUfficioInserimento;
		this.mTipoCampo = aTipoCampo;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdFascicoloSiep() {
		return mIdFascicoloSiep;
	}

	public BigDecimal getNres() {
		return mNres;
	}

	public Integer getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getUltTipoProvvedimento() {
		return mUltTipoProvvedimento;
	}

	public String getUltCodMotivo() {
		return mUltCodMotivo;
	}

	public String getPenTipoProvvedimento() {
		return mPenTipoProvvedimento;
	}

	public String getPenCodMotivo() {
		return mPenCodMotivo;
	}

	public Integer getConta() {
		return mConta;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getDescrUfficio() {
		return mDescrUfficio;
	}

	public String getCodStatoProcedimento() {
		return mCodStatoProcedimento;
	}

	public String getDescrStatoProcedimento() {
		return mDescrStatoProcedimento;
	}

	public Integer getCodStatoFascicoloRes() {
		return mCodStatoFascicoloRes;
	}

	public String getDescrStatoFascicoloRes() {
		return mDescrStatoFascicoloRes;
	}

	public String getCodPosizioneGiuridica() {
		return mCodPosizioneGiuridica;
	}

	public String getDescrPosizioneGiuridica() {
		return mDescrPosizioneGiuridica;
	}

	// NGG Statistiche SIEP
	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public BigDecimal getChiaveProgrOrig() {
		return mChiaveProgrOrig;
	}

	public String getDescUfficioInserimento() {
		return mDescUfficioInserimento;
	}

	public String getTipoCampo() {
		return mTipoCampo;
	}

	//
	// METODI SET()
	//
	public void setIdFascicoloSiep(BigDecimal aValore) {
		mIdFascicoloSiep = aValore;
	}

	public void setNres(BigDecimal aValore) {
		mNres = aValore;
	}

	public void setChiaveAnno(Integer aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setUltTipoProvvedimento(String aValore) {
		mUltTipoProvvedimento = aValore;
	}

	public void setUltCodMotivo(String aValore) {
		mUltCodMotivo = aValore;
	}

	public void setPenTipoProvvedimento(String aValore) {
		mPenTipoProvvedimento = aValore;
	}

	public void setPenCodMotivo(String aValore) {
		mPenCodMotivo = aValore;
	}

	public void setConta(Integer aValore) {
		mConta = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDescrUfficio(String aValore) {
		mDescrUfficio = aValore;
	}

	public void setCodStatoProcedimento(String aValore) {
		mCodStatoProcedimento = aValore;
	}

	public void setDescrStatoProcedimento(String aValore) {
		mDescrStatoProcedimento = aValore;
	}

	public void setCodStatoFascicoloRes(Integer aValore) {
		mCodStatoFascicoloRes = aValore;
	}

	public void setDescrStatoFascicoloRes(String aValore) {
		mDescrStatoFascicoloRes = aValore;
	}

	public void setCodPosizioneGiuridica(String aValore) {
		mCodPosizioneGiuridica = aValore;
	}

	public void setDescrPosizioneGiuridica(String aValore) {
		mDescrPosizioneGiuridica = aValore;
	}

	// NGG Statistiche SIEP
	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setChiaveProgrOrig(BigDecimal aValore) {
		mChiaveProgrOrig = aValore;
	}

	public void setDescUfficioInserimento(String aValore) {
		mDescUfficioInserimento = aValore;
	}

	public void setTipoCampo(String aValore) {
		mTipoCampo = aValore;
	}

	public String getTipoMisura() {
		return mTipoMisura;
	}

	public void setTipoMisura(String mTipoMisura) {
		this.mTipoMisura = mTipoMisura;
	}
	
}
