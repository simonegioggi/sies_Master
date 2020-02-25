package siap.sico.soggettodattilo.model;

/**
* <p>Title: SoggettoDattiloModel</p>
* <p>Description: Classe Model che rappresenta il SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class SoggettoDattiloModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 8808070857236302360L;

	private BigDecimal mIdDattilo;
	private BigDecimal mCodSoggetto;
	private String mDocTipo;
	private String mDocNome;
	private Date mDataInserimento;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private ByteArrayInputStream mDocBlobIn;
	private ByteArrayOutputStream mDocBlobOut;

	// COSTRUTTORE DI DEFAULT
	public SoggettoDattiloModel() {
		mIdDattilo = null;
		mCodSoggetto = null;
		mDocTipo = "";
		mDocNome = "";
		mDataInserimento = null;
		mCodOperatoreInserimento = "";
		mCodUfficioInserimento = "";
		mDocBlobIn = null;
		mDocBlobOut = null;
	}

	// COSTRUTTORE DI COPIA
	public SoggettoDattiloModel(SoggettoDattiloModel aModel) {
		mIdDattilo = aModel.mIdDattilo;
		mCodSoggetto = aModel.mCodSoggetto;
		mDocTipo = aModel.mDocTipo;
		mDocNome = aModel.mDocNome;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
	}

	// COSTRUTTORE MODEL
	public SoggettoDattiloModel(BigDecimal aIdDattilo, BigDecimal aCodSoggetto, String aDocTipo,
			String aDocNome, Date aDataInserimento, String aCodOperatoreInserimento,
			String aCodUfficioInserimento) {
		mIdDattilo = aIdDattilo;
		mCodSoggetto = aCodSoggetto;
		mDocTipo = aDocTipo;
		mDocNome = aDocNome;
		mDataInserimento = aDataInserimento;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdDattilo() {
		return mIdDattilo;
	}

	public BigDecimal getCodSoggetto() {
		return mCodSoggetto;
	}

	public String getDocTipo() {
		return mDocTipo;
	}

	public String getDocTipoDesc() {
		String ret = "";
		if ("A".equals(mDocTipo)) {
			ret = "Codice CUI";
		} else if ("B".equals(mDocTipo)) {
			ret = "Cartellino Fotosegnaletico";
		}
		return ret;
	}

	public String getDocNome() {
		return mDocNome;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public ByteArrayInputStream getDocBlobIn() {
		return mDocBlobIn;
	}

	public ByteArrayOutputStream getDocBlobOut() {
		return mDocBlobOut;
	}

	//
	// METODI SET()
	//

	public void setIdDattilo(BigDecimal aValore) {
		mIdDattilo = aValore;
	}

	public void setCodSoggetto(BigDecimal aValore) {
		mCodSoggetto = aValore;
	}

	public void setDocTipo(String aValore) {
		mDocTipo = aValore;
	}

	public void setDocNome(String aValore) {
		mDocNome = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDocBlobIn(ByteArrayInputStream aValore) {
		mDocBlobIn = aValore;
	}

	public void setDocBlobOut(ByteArrayOutputStream aValore) {
		mDocBlobOut = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdDattilo + " - " + mCodSoggetto + " - " + mDocTipo + " - " + mDocNome + " - "
				+ mDataInserimento + " - " + mCodOperatoreInserimento + " - " + mCodUfficioInserimento;

		return lStr;
	}

}