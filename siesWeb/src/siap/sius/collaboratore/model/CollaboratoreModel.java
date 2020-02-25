package siap.sius.collaboratore.model;

/**
* <p>Title: CollaboratoreModel</p>
* <p>Description: Classe Model che rappresenta il Collaboratore di Giustizia</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Eutelia</p>
* @version 3.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class CollaboratoreModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3687164413482511530L;

	private BigDecimal mIdCollaboratore;
	private BigDecimal mIdFascicoloSius;
	private String mCodUfficio;
	private String mDescrUfficio;
	private Date mDataInizio;
	private Date mDataFine;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public CollaboratoreModel() {
		super();
		mIdCollaboratore = null;
		mIdFascicoloSius = null;
		mCodUfficio = null;
		mDescrUfficio = null;
		mDataInizio = null;
		mDataFine = null;
		mCodOperatoreInserimento = null;
		mDataInserimento = null;
		mCodUfficioInserimento = null;
		mCodOperatoreAggiornamento = null;
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = null;

	}

	// COSTRUTTORE DI COPIA
	public CollaboratoreModel(CollaboratoreModel aModel) {
		mIdCollaboratore = aModel.mIdCollaboratore;
		mIdFascicoloSius = aModel.mIdFascicoloSius;
		mCodUfficio = aModel.mCodUfficio;
		mDescrUfficio = aModel.mDescrUfficio;
		mDataInizio = aModel.mDataInizio;
		mDataFine = aModel.mDataFine;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.getDataAggiornamento();
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdCollaboratore() {
		return mIdCollaboratore;
	}

	public BigDecimal getIdFascicoloSius() {
		return mIdFascicoloSius;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getDescrUfficio() {
		return mDescrUfficio;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
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

	// public String getDescrUfficioInserimento() { return mDescrUfficioInserimento; }
	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}
	// public String getDescrUfficioAggiornamento(){ return mDescrUfficioAggiornamento; }

	//
	// METODI SET()
	//
	public void setIdCollaboratore(BigDecimal aValore) {
		mIdCollaboratore = aValore;
	}

	public void setIdFascicoloSius(BigDecimal aValore) {
		mIdFascicoloSius = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDescrUfficio(String aValore) {
		mDescrUfficio = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
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

	// public void setDescrUfficioInserimento(String aValore ) { mDescrUfficioInserimento = aValore; }
	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}
	// public void setDescrUfficioAggiornamento(String aValore ) { mDescrUfficioAggiornamento = aValore; }

}