package siap.sige.collegioesperto.model;

/**
* <p>Title: CollegioEspertoModel</p>
* <p>Description: Classe Model che rappresenta il CollegioGiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sius.esperto.model.EspertoModel;
import f3b.model.GenericModel;

public class CollegioEspertoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1278500411695028041L;

	private BigDecimal mEspIdEsperto;
	private BigDecimal mColIdCollegio;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;

	private EspertoModel mEsperto;

	// COSTRUTTORE DI DEFAULT
	public CollegioEspertoModel() {
		super();
	}

	// COSTRUTTORE DI COPIA
	public CollegioEspertoModel(CollegioEspertoModel aModel) {
		this.mColIdCollegio = aModel.mColIdCollegio;
		this.mEspIdEsperto = aModel.mEspIdEsperto;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;

		this.mEsperto = aModel.mEsperto;
	}

	// COSTRUTTORE MODEL
	public CollegioEspertoModel(BigDecimal aEspIdEsperto, BigDecimal aColIdCollegio,
			// String aCodUfficioAppartenenza,
			// String aDescrUfficioAppartenenza,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento) {
		this.mEspIdEsperto = aEspIdEsperto;
		this.mColIdCollegio = aColIdCollegio;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
	}

	//
	// METODI GET()
	//
	public BigDecimal getEspIdEsperto() {
		return mEspIdEsperto;
	}

	public BigDecimal getColIdCollegio() {
		return mColIdCollegio;
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

	public EspertoModel getEsperto() {
		return mEsperto;
	}

	//
	// METODI SET()
	//
	public void setEspIdEsperto(BigDecimal aValore) {
		mEspIdEsperto = aValore;
	}

	public void setColIdCollegio(BigDecimal aValore) {
		mColIdCollegio = aValore;
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

	public void setEsperto(EspertoModel aValore) {
		mEsperto = aValore;
	}

}