package siap.sico.camponota.model;

/**
* <p>Title: CampoNotaModel</p>
* <p>Description: Classe Model che rappresenta il CampoNota</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class CampoNotaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1689548632155451377L;

	private BigDecimal mIdCampoNota;
	private BigDecimal mProgressivo;
	private String mDescr;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mEveIdEvento;
	private String mOggettoNotaRes; // STUB 31/01/2006
	private BigDecimal mFasSieIdFascicoloSiep; // STUB 31/01/2006

	// COSTRUTTORE DI DEFAULT
	/**
	 * Costruttore di classe.
	 */
	public CampoNotaModel() {
		this.mIdCampoNota = null;
		this.mProgressivo = null;
		this.mDescr = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mEveIdEvento = null;
		this.mOggettoNotaRes = ""; // STUB 31/01/2006
		this.mFasSieIdFascicoloSiep = null; // STUB 31/01/2006
	}

	// COSTRUTTORE DI COPIA
	/**
	 * Costruttore di classe con argomento l'istanza del model stesso. effattua una copia dei dati passati
	 * come argomenti con quelli della propria istanza.
	 * <p>
	 * 
	 * @param aModel
	 *            model con i dati da copiare.
	 */
	public CampoNotaModel(CampoNotaModel aModel) {
		this.mIdCampoNota = aModel.mIdCampoNota;
		this.mProgressivo = aModel.mProgressivo;
		this.mDescr = aModel.mDescr;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mOggettoNotaRes = aModel.mOggettoNotaRes; // STUB 31/01/2006
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep; // STUB 31/01/2006
	}

	// COSTRUTTORE MODEL
	public CampoNotaModel(BigDecimal aIdCampoNota, BigDecimal aProgressivo, String aDescr,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aEveIdEvento,
			String aOggettoNotaRes, BigDecimal aFasSieIdFascicoloSiep) {
		this.mIdCampoNota = aIdCampoNota;
		this.mProgressivo = aProgressivo;
		this.mDescr = aDescr;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mEveIdEvento = aEveIdEvento;
		this.mOggettoNotaRes = aOggettoNotaRes; // STUB 31/01/2006
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep; // STUB 31/01/2006
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdCampoNota() {
		return mIdCampoNota;
	}

	public BigDecimal getProgressivo() {
		return mProgressivo;
	}

	public String getDescr() {
		return mDescr;
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

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getOggettoNotaRes() {
		return mOggettoNotaRes;
	} // STUB 31/01/2006

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	} // STUB 31/01/2006

	//
	// METODI SET()
	//

	public void setIdCampoNota(BigDecimal aValore) {
		mIdCampoNota = aValore;
	}

	public void setProgressivo(BigDecimal aValore) {
		mProgressivo = aValore;
	}

	public void setDescr(String aValore) {
		mDescr = aValore;
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

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setOggettoNotaRes(String aValore) {
		mOggettoNotaRes = aValore;
	} // STUB 31/01/2006

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	} // STUB 31/01/2006

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdCampoNota + " - " + mProgressivo + " - " + mDescr + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - " + mEveIdEvento + " - "
				+ mOggettoNotaRes + " - " + mFasSieIdFascicoloSiep;

		return lStr;
	}

}