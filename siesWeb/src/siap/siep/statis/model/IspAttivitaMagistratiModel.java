package siap.siep.statis.model;

/**
* <p>Title: IspAttivitaMagistratiModel</p>
* <p>Description: Classe Model che rappresenta il IspAttivitaMagistrati</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class IspAttivitaMagistratiModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = -1487877600826237149L;
	private BigDecimal mIdFascicoloSiep;
	private Integer mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mCodUfficio;
	private String mDescrUfficio;
	private String mCodMagistrato;
	private String mDescrMagistrato;
	private Date mDataEmissione;
	private String mCodMotivo;
	private String mDescrMotivo;
	private String mTipologia;
	private BigDecimal mIdEvento;

	private String mDescrTipologia;
	private Integer mConta;
	private Integer mAnno;
	// NGG
	private String mCodUfficioInserimento;
	private BigDecimal mChiaveProgrOrig;
	private String mDescUfficioInserimento;
	// END NGG
			 private 	String	mDescrMotivoOrig;

	// COSTRUTTORE DI DEFAULT
	public IspAttivitaMagistratiModel() {
		this.mIdFascicoloSiep = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mCodUfficio = "";
		this.mDescrUfficio = "";
		this.mCodMagistrato = "";
		this.mDescrMagistrato = "";
		this.mDataEmissione = null;
		this.mCodMotivo = "";
		this.mDescrMotivo = "";
		this.mTipologia = "";
		this.mIdEvento = null;
		this.mDescrTipologia = "";
		this.mConta = null;
		this.mAnno = null;
		// NGG
		this.mCodUfficioInserimento = "";
		this.mChiaveProgrOrig = null;
		this.mDescUfficioInserimento = "";
		// END NGG
			 this.mDescrMotivoOrig = null;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdFascicoloSiep() {
		return mIdFascicoloSiep;
	}

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

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getDescrMagistrato() {
		return mDescrMagistrato;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
	}

	public String getTipologia() {
		return mTipologia;
	}

	public BigDecimal getIdEvento() {
		return mIdEvento;
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
		 public String 			 getDescrMotivoOrig() 			 { return mDescrMotivoOrig; } 


	//
	// METODI SET()
	//

	public void setIdFascicoloSiep(BigDecimal aValore) {
		mIdFascicoloSiep = aValore;
	}

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

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setDescrMagistrato(String aValore) {
		mDescrMagistrato = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}

	public void setTipologia(String aValore) {
		mTipologia = aValore;
	}

	public void setIdEvento(BigDecimal aValore) {
		mIdEvento = aValore;
	}

	// NGG Statistiche SIEP
	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setChiaveProgrOrig(BigDecimal aValore) {
		mChiaveProgrOrig = aValore;
	}

	public void setdescUfficioInserimento(String aValore) {
		mDescUfficioInserimento = aValore;
	}
	
	public void  	 setDescrMotivoOrig(String aValore ) 			 { mDescrMotivoOrig = aValore; } 

	// Ulteriori meteodi
	// set e get

	public Integer getAnno() {
		return mAnno;
	}

	public void setAnno(Integer anno) {
		mAnno = anno;
	}

	public Integer getConta() {
		return mConta;
	}

	public void setConta(Integer conta) {
		mConta = conta;
	}

	public String getDescrTipologia() {
		return mDescrTipologia;
	}

	public void setDescrTipologia(String descrTipologia) {
		mDescrTipologia = descrTipologia;
	}
}
