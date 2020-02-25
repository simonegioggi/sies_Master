package siap.sico.note.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: NoteModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta le Note
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class NoteModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6182815677010957406L;

	private BigDecimal mIdNote;
	private Date mData;
	private String mDescrizione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private BigDecimal mFasSiuIdFascicoloSius;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mFasSigeIdFascicoloSige;

	// COSTRUTTORE DI DEFAULT
	/**
	 * Costruttore di classe.
	 */
	public NoteModel() {
		this.mIdNote = null;
		this.mData = null;
		this.mDescrizione = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mFasSiuIdFascicoloSius = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mFasSigeIdFascicoloSige = null;
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
	public NoteModel(NoteModel aModel) {
		this.mIdNote = aModel.mIdNote;
		this.mData = aModel.mData;
		this.mDescrizione = aModel.mDescrizione;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mFasSigeIdFascicoloSige = aModel.mFasSigeIdFascicoloSige;
	}

	// COSTRUTTORE MODEL
	public NoteModel(BigDecimal aIdNote, Date aData, String aDescrizione, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			BigDecimal aFasSiuIdFascicoloSius, BigDecimal aFasSieIdFascicoloSiep,
			BigDecimal aFasSigeIdFascicoloSige) {
		this.mIdNote = aIdNote;
		this.mData = aData;
		this.mDescrizione = aDescrizione;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mFasSigeIdFascicoloSige = aFasSigeIdFascicoloSige;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdNote() {
		return mIdNote;
	}

	public Date getData() {
		return mData;
	}

	public String getDescrizione() {
		return mDescrizione;
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getFasSigeIdFascicoloSige() {
		return mFasSigeIdFascicoloSige;
	}

	//
	// METODI SET()
	//

	public void setIdNote(BigDecimal aValore) {
		mIdNote = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setFasSigeIdFascicoloSige(BigDecimal aValore) {
		mFasSigeIdFascicoloSige = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdNote + " - " + mData + " - " + mDescrizione + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mFasSiuIdFascicoloSius + " - " + mFasSieIdFascicoloSiep + " - " + mFasSigeIdFascicoloSige;
		return lStr;
	}

}