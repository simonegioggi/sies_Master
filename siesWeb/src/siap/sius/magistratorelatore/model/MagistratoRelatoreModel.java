package siap.sius.magistratorelatore.model;

/**
* <p>Title: MagistratoRelatoreModel</p>
* <p>Description: Classe Model che rappresenta il MagistratoRelatore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sius.esperto.model.EspertoModel;

public class MagistratoRelatoreModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = 6819294964302584669L;
	private Date mDataInizio;
	private Date mDataFine;
	private String mCodRuoloMagistrato;
	private String mDescrRuoloMagistrato;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mMagCodMagistrato;
	private BigDecimal mFasSiuIdFascicoloSius;
	private BigDecimal mEspIdEsperto;
	private MagistratoModel mMagistrato;
	private EspertoModel mEsperto;

	// COSTRUTTORE DI DEFAULT
	public MagistratoRelatoreModel() {
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mCodRuoloMagistrato = "";
		this.mDescrRuoloMagistrato = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mMagCodMagistrato = "";
		this.mFasSiuIdFascicoloSius = null;
		this.mEspIdEsperto = null;
		mMagistrato = null;
		mEsperto = null;
	}

	// COSTRUTTORE DI COPIA
	public MagistratoRelatoreModel(MagistratoRelatoreModel aModel) {
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mCodRuoloMagistrato = aModel.mCodRuoloMagistrato;
		this.mDescrRuoloMagistrato = aModel.mDescrRuoloMagistrato;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mMagCodMagistrato = aModel.mMagCodMagistrato;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mEspIdEsperto = aModel.mEspIdEsperto;
		mMagistrato = aModel.mMagistrato;
		mEsperto = aModel.mEsperto;

	}

	// COSTRUTTORE MODEL
	public MagistratoRelatoreModel(Date aDataInizio, Date aDataFine, String aCodRuoloMagistrato,
			String aDescrRuoloMagistrato, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			String aMagCodMagistrato, BigDecimal aFasSiuIdFascicoloSius, BigDecimal aEspIdEsperto) {
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mCodRuoloMagistrato = aCodRuoloMagistrato;
		this.mDescrRuoloMagistrato = aDescrRuoloMagistrato;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mMagCodMagistrato = aMagCodMagistrato;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mEspIdEsperto = aEspIdEsperto;
		mMagistrato = null;
		mEsperto = null;

	}

	//
	// METODI GET()
	//

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public String getCodRuoloMagistrato() {
		return mCodRuoloMagistrato;
	}

	public String getDescrRuoloMagistrato() {
		return mDescrRuoloMagistrato;
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

	public String getMagCodMagistrato() {
		return mMagCodMagistrato;
	}

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public BigDecimal getEspIdEsperto() {
		return this.mEspIdEsperto;
	}

	public EspertoModel getEsperto() {
		return mEsperto;
	}

	public MagistratoModel getMagistrato() {
		return mMagistrato;
	}

	//
	// METODI SET()
	//

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setCodRuoloMagistrato(String aValore) {
		mCodRuoloMagistrato = aValore;
	}

	public void setDescrRuoloMagistrato(String aValore) {
		mDescrRuoloMagistrato = aValore;
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

	public void setMagCodMagistrato(String aValore) {
		mMagCodMagistrato = aValore;
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setEspIdEsperto(BigDecimal aValore) {
		mEspIdEsperto = aValore;
	}

	public void setEsperto(EspertoModel aValore) {
		mEsperto = aValore;
	}

	public void setMagistrato(MagistratoModel aValore) {
		mMagistrato = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mDataInizio + " - " + mDataFine + " - " + mCodRuoloMagistrato + " - "
				+ mDescrRuoloMagistrato + " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mMagCodMagistrato + " - "
				+ mFasSiuIdFascicoloSius + " - " + mEspIdEsperto;
		if (this.mMagistrato != null) {
			lStr += " MAGISTRATO ";
			lStr += mMagistrato.toString();
		}
		if (this.mEsperto != null) {
			lStr += " ESPERTO ";
			lStr += mEsperto.toString();
		}

		return lStr;
	}
}
