package siap.sico.template.model;

/**
* <p>Title: TemplateModel</p>
* <p>Description: Classe Model che rappresenta il Template</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class TemplateModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 3313453987557678215L;

	private String mIdTemplate;
	private String mNomeTemplate;
	private String mDescr;
	private String mPathRicerca;
	private String mCodTipoEvento;
	private String mDescrTipoEvento;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private String mCodMotivo;
	private String mDescrMotivo;
	private String mFlagTemplate;
	// Nuovi campi Luigi 3-2-04
	private String mCodEsito;
	private String mDescrEsito;
	private String mCodOggettoProcedimento;
	private String mDescrOggettoProcedimento;
	// 30-9-04
	private String mCodMagistrato;
	private String mCodTipoProvvedimentoSige; // 21-05-2009

	// COSTRUTTORE DI DEFAULT
	public TemplateModel() {
		this.mIdTemplate = "";
		this.mNomeTemplate = "";
		this.mDescr = "";
		this.mPathRicerca = "";
		this.mCodTipoEvento = "";
		this.mDescrTipoEvento = "";
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mCodMotivo = "";
		this.mDescrMotivo = "";
		this.mFlagTemplate = "";
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mCodOggettoProcedimento = "";
		this.mDescrOggettoProcedimento = "";
		this.mCodMagistrato = "";
		this.mCodTipoProvvedimentoSige = "";
	}

	// COSTRUTTORE DI COPIA
	public TemplateModel(TemplateModel aModel) {
		this.mIdTemplate = aModel.mIdTemplate;
		this.mNomeTemplate = aModel.mNomeTemplate;
		this.mDescr = aModel.mDescr;
		this.mPathRicerca = aModel.mPathRicerca;
		this.mCodTipoEvento = aModel.mCodTipoEvento;
		this.mDescrTipoEvento = aModel.mDescrTipoEvento;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mCodMotivo = aModel.mCodMotivo;
		this.mDescrMotivo = aModel.mDescrMotivo;
		this.mFlagTemplate = aModel.mFlagTemplate;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mCodOggettoProcedimento = aModel.mCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aModel.mDescrOggettoProcedimento;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mCodTipoProvvedimentoSige = aModel.mCodTipoProvvedimentoSige;
	}

	// COSTRUTTORE MODEL
	public TemplateModel(String aIdTemplate, String aNomeTemplate, String aDescr, String aPathRicerca,
			String aCodTipoEvento, String aDescrTipoEvento, String aCodTipoProvvedimento,
			String aDescrTipoProvvedimento, String aCodMotivo, String aDescrMotivo, String aFlagTemplate,
			String aCodEsito, String aDescrEsito, String aCodOggettoProcedimento,
			String aDescrOggettoProcedimento, String aCodMagistrato, String aCodTipoProvvedimentoSige) {
		this.mIdTemplate = aIdTemplate;
		this.mNomeTemplate = aNomeTemplate;
		this.mDescr = aDescr;
		this.mPathRicerca = aPathRicerca;
		this.mCodTipoEvento = aCodTipoEvento;
		this.mDescrTipoEvento = aDescrTipoEvento;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mCodMotivo = aCodMotivo;
		this.mDescrMotivo = aDescrMotivo;
		this.mFlagTemplate = aFlagTemplate;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mCodOggettoProcedimento = aCodOggettoProcedimento;
		this.mDescrOggettoProcedimento = aDescrOggettoProcedimento;
		this.mCodMagistrato = aCodMagistrato;
		this.mCodTipoProvvedimentoSige = aCodTipoProvvedimentoSige;
	}

	//
	// METODI GET()
	//

	public String getIdTemplate() {
		return mIdTemplate;
	}

	public String getNomeTemplate() {
		return mNomeTemplate;
	}

	public String getDescr() {
		return mDescr;
	}

	public String getPathRicerca() {
		return mPathRicerca;
	}

	public String getCodTipoEvento() {
		return mCodTipoEvento;
	}

	public String getDescrTipoEvento() {
		return mDescrTipoEvento;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
	}

	public String getFlagTemplate() {
		return mFlagTemplate;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public String getCodOggettoProcedimento() {
		return mCodOggettoProcedimento;
	}

	public String getDescrOggettoProcedimento() {
		return mDescrOggettoProcedimento;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getCodTipoProvvedimentoSige() {
		return mCodTipoProvvedimentoSige;
	}

	//
	// METODI SET()
	//
	public void setIdTemplate(String aValore) {
		mIdTemplate = aValore;
	}

	public void setNomeTemplate(String aValore) {
		mNomeTemplate = aValore;
	}

	public void setDescr(String aValore) {
		mDescr = aValore;
	}

	public void setPathRicerca(String aValore) {
		mPathRicerca = aValore;
	}

	public void setCodTipoEvento(String aValore) {
		mCodTipoEvento = aValore;
	}

	public void setDescrTipoEvento(String aValore) {
		mDescrTipoEvento = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}

	public void setFlagTemplate(String aValore) {
		mFlagTemplate = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setCodOggettoProcedimento(String aValore) {
		mCodOggettoProcedimento = aValore;
	}

	public void setDescrOggettoProcedimento(String aValore) {
		mDescrOggettoProcedimento = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setCodTipoProvvedimentoSige(String aValore) {
		mCodTipoProvvedimentoSige = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdTemplate + " - " + mNomeTemplate + " - " + mDescr + " - " + mPathRicerca + " - "
				+ mCodTipoEvento + " - " + mDescrTipoEvento + " - " + mCodTipoProvvedimento + " - "
				+ mDescrTipoProvvedimento + " - " + mCodMotivo + " - " + mFlagTemplate + " - " + mCodEsito
				+ " - " + mDescrEsito + " - " + mCodOggettoProcedimento + " - " + mDescrOggettoProcedimento
				+ " - " + mCodMagistrato + " - " + mCodTipoProvvedimentoSige;
		return lStr;
	}

}