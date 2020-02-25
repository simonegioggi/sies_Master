package siap.sige.collegiogiudicepopolare.model;

/**
* <p>Title: CollegioGiudicePopolareModel</p>
* <p>Description: Classe Model che rappresenta il CollegioGiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/
import java.math.BigDecimal;
import java.util.Date;

import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import f3b.model.GenericModel;

public class CollegioGiudicePopolareModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4558334002905360950L;

	private BigDecimal mGiuPopIdGiudicePopolare;
	private BigDecimal mColIdCollegio;
	private String mCodUfficioAppartenenza;
	private String mDescrUfficioAppartenenza;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;

	private GiudicePopolareModel mGiudicePopolare;

	// COSTRUTTORE DI DEFAULT
	public CollegioGiudicePopolareModel() {
		super();
	}

	// COSTRUTTORE DI COPIA
	public CollegioGiudicePopolareModel(CollegioGiudicePopolareModel aModel) {
		this.mColIdCollegio = aModel.mColIdCollegio;
		this.mGiuPopIdGiudicePopolare = aModel.mGiuPopIdGiudicePopolare;
		this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;

		this.mGiudicePopolare = aModel.mGiudicePopolare;
	}

	// COSTRUTTORE MODEL
	public CollegioGiudicePopolareModel(BigDecimal aGiuPopIdGiudicePopolare, BigDecimal aColIdCollegio,
			String aCodUfficioAppartenenza, String aDescrUfficioAppartenenza, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento) {
		this.mGiuPopIdGiudicePopolare = aGiuPopIdGiudicePopolare;
		this.mColIdCollegio = aColIdCollegio;
		this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
	}

	//
	// METODI GET()
	//
	public BigDecimal getGiuPopIdGiudicePopolare() {
		return mGiuPopIdGiudicePopolare;
	}

	public BigDecimal getColIdCollegio() {
		return mColIdCollegio;
	}

	public String getCodUfficioAppartenenza() {
		return mCodUfficioAppartenenza;
	}

	public String getDescrUfficioAppartenenza() {
		return mDescrUfficioAppartenenza;
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

	public GiudicePopolareModel getGiudicePopolare() {
		return mGiudicePopolare;
	}

	//
	// METODI SET()
	//
	public void setGiuPopIdGiudicePopolare(BigDecimal aValore) {
		mGiuPopIdGiudicePopolare = aValore;
	}

	public void setColIdCollegio(BigDecimal aValore) {
		mColIdCollegio = aValore;
	}

	public void setCodUfficioAppartenenza(String aValore) {
		mCodUfficioAppartenenza = aValore;
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

	public void setGiudicePopolare(GiudicePopolareModel aValore) {
		mGiudicePopolare = aValore;
	}

}