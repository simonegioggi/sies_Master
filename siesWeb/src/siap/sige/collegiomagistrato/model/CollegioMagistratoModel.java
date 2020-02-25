package siap.sige.collegiomagistrato.model;

import java.math.BigDecimal;
import java.util.Date;

import siap.sige.magistrato.model.MagistratoModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: CollegioMagistratoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il CollegioMagistrato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class CollegioMagistratoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6174554221018043640L;

	private BigDecimal mColIdCollegio;
	private String mMagCodMagistrato;
	private String mCodUfficioAppartenenza;
	private String mDescrUfficioAppartenenza;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;

	private int mProgr = 0; // Attributo aggiuntivo, non presente in db, che indica
							// il progressivo magistrato.
	private MagistratoModel mMagistrato;

	// 20171013: [SG] aggiunta variabile di collegamento all'udienza sige
	private BigDecimal mUdiIdUdienzaSige;

	// COSTRUTTORE DI DEFAULT
	public CollegioMagistratoModel() {
		super();
	}

	// COSTRUTTORE DI COPIA
	public CollegioMagistratoModel(CollegioMagistratoModel aModel) {
		this.mColIdCollegio = aModel.mColIdCollegio;
		this.mMagCodMagistrato = aModel.mMagCodMagistrato;
		this.mCodUfficioAppartenenza = aModel.mCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aModel.mDescrUfficioAppartenenza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mProgr = aModel.mProgr;
		this.mMagistrato = aModel.mMagistrato;
		this.mUdiIdUdienzaSige = aModel.mUdiIdUdienzaSige;
	}

	// COSTRUTTORE MODEL
	public CollegioMagistratoModel(BigDecimal aColIdCollegio, String aMagCodMagistrato,
			String aCodUfficioAppartenenza, String aDescrUfficioAppartenenza, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento, int aProgr,
			MagistratoModel aMagistrato, BigDecimal aUdiIdUdienzaSige) {
		this.mColIdCollegio = aColIdCollegio;
		this.mMagCodMagistrato = aMagCodMagistrato;
		this.mCodUfficioAppartenenza = aCodUfficioAppartenenza;
		this.mDescrUfficioAppartenenza = aDescrUfficioAppartenenza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mProgr = aProgr;
		this.mMagistrato = aMagistrato;
		this.mUdiIdUdienzaSige = aUdiIdUdienzaSige;
	}

	//
	// METODI GET()
	//
	public BigDecimal getColIdCollegio() {
		return mColIdCollegio;
	}

	public String getMagCodMagistrato() {
		return mMagCodMagistrato;
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

	public int getProgr() {
		return mProgr;
	}

	public MagistratoModel getMagistrato() {
		return mMagistrato;
	}

	public BigDecimal getUdiIdUdienzaSige() {
		return mUdiIdUdienzaSige;
	}

	//
	// METODI SET()
	//
	public void setMagCodMagistrato(String aValore) {
		mMagCodMagistrato = aValore;
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

	public void setProgr(int aValore) {
		mProgr = aValore;
	}

	public void setMagistrato(MagistratoModel aValore) {
		mMagistrato = aValore;
	}

	public void setUdiIdUdienzaSige(BigDecimal aValore) {
		mUdiIdUdienzaSige = aValore;
	}

}