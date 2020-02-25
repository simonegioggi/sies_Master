package siap.siep.parametro.model;

/**
* <p>Title: ParametroModel</p>
* <p>Description: Classe Model che rappresenta il Parametro</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ParametroModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = -7474376683713926010L;
	private BigDecimal mIdParametro;
	private String mNomeParametro;
	private String mValore;
	private BigDecimal mAnni;
	private BigDecimal mMesi;
	private BigDecimal mGiorni;
	private BigDecimal mImporto;
	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private String mCodUfficioValidita;
	private String mDescrUfficioValidita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiormanento;
	private String mDescrUfficioAggiormanento;

	// COSTRUTTORE DI DEFAULT
	public ParametroModel() {
		this.mIdParametro = null;
		this.mNomeParametro = "";
		this.mValore = "";
		this.mAnni = null;
		this.mMesi = null;
		this.mGiorni = null;
		this.mImporto = null;
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mCodUfficioValidita = "";
		this.mDescrUfficioValidita = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiormanento = "";
		this.mDescrUfficioAggiormanento = "";
	}

	// COSTRUTTORE DI COPIA
	public ParametroModel(ParametroModel aModel) {
		this.mIdParametro = aModel.mIdParametro;
		this.mNomeParametro = aModel.mNomeParametro;
		this.mValore = aModel.mValore;
		this.mAnni = aModel.mAnni;
		this.mMesi = aModel.mMesi;
		this.mGiorni = aModel.mGiorni;
		this.mImporto = aModel.mImporto;
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mCodUfficioValidita = aModel.mCodUfficioValidita;
		this.mDescrUfficioValidita = aModel.mDescrUfficioValidita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiormanento = aModel.mCodUfficioAggiormanento;
		this.mDescrUfficioAggiormanento = aModel.mDescrUfficioAggiormanento;
	}

	// COSTRUTTORE MODEL
	public ParametroModel(BigDecimal aIdParametro, String aNomeParametro, String aValore, BigDecimal aAnni,
			BigDecimal aMesi, BigDecimal aGiorni, BigDecimal aImporto, Date aDataInizioValidita,
			Date aDataFineValidita, String aCodUfficioValidita, String aDescrUfficioValidita,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiormanento, String aDescrUfficioAggiormanento) {
		this.mIdParametro = aIdParametro;
		this.mNomeParametro = aNomeParametro;
		this.mValore = aValore;
		this.mAnni = aAnni;
		this.mMesi = aMesi;
		this.mGiorni = aGiorni;
		this.mImporto = aImporto;
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mCodUfficioValidita = aCodUfficioValidita;
		this.mDescrUfficioValidita = aDescrUfficioValidita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiormanento = aCodUfficioAggiormanento;
		this.mDescrUfficioAggiormanento = aDescrUfficioAggiormanento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdParametro() {
		return mIdParametro;
	}

	public String getNomeParametro() {
		return mNomeParametro;
	}

	public String getValore() {
		return mValore;
	}

	public BigDecimal getAnni() {
		return mAnni;
	}

	public BigDecimal getMesi() {
		return mMesi;
	}

	public BigDecimal getGiorni() {
		return mGiorni;
	}

	public BigDecimal getImporto() {
		return mImporto;
	}

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public String getCodUfficioValidita() {
		return mCodUfficioValidita;
	}

	public String getDescrUfficioValidita() {
		return mDescrUfficioValidita;
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

	public String getCodUfficioAggiormanento() {
		return mCodUfficioAggiormanento;
	}

	public String getDescrUfficioAggiormanento() {
		return mDescrUfficioAggiormanento;
	}

	//
	// METODI SET()
	//

	public void setIdParametro(BigDecimal aValore) {
		mIdParametro = aValore;
	}

	public void setNomeParametro(String aValore) {
		mNomeParametro = aValore;
	}

	public void setValore(String aValore) {
		mValore = aValore;
	}

	public void setAnni(BigDecimal aValore) {
		mAnni = aValore;
	}

	public void setMesi(BigDecimal aValore) {
		mMesi = aValore;
	}

	public void setGiorni(BigDecimal aValore) {
		mGiorni = aValore;
	}

	public void setImporto(BigDecimal aValore) {
		mImporto = aValore;
	}

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setCodUfficioValidita(String aValore) {
		mCodUfficioValidita = aValore;
	}

	public void setDescrUfficioValidita(String aValore) {
		mDescrUfficioValidita = aValore;
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

	public void setCodUfficioAggiormanento(String aValore) {
		mCodUfficioAggiormanento = aValore;
	}

	public void setDescrUfficioAggiormanento(String aValore) {
		mDescrUfficioAggiormanento = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdParametro + " - " + mNomeParametro + " - " + mValore + " - " + mAnni + " - " + mMesi
				+ " - " + mGiorni + " - " + mImporto + " - " + mDataInizioValidita + " - " + mDataFineValidita
				+ " - " + mCodUfficioValidita + " - " + mDescrUfficioValidita + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiormanento;

		return lStr;
	}
}
