package siap.sius.cancassfascsius.model;

/**
* <p>Title: CancAssFascSiusModel</p>
* <p>Description: Classe Model che rappresenta l'entità: CancAssFascSius,
 * ovvero la relazione tra un Fascicolo SIUS e la Cancelleria Assegnataria cui è stato assegnato.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;

public class CancAssFascSiusModel extends CancelleriaAssegnatariaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 394182941598023662L;

	private BigDecimal mFasSiusIdFascicoloSius;
	private String mCodStatoProcedimento;
	private String mDescrStatoProcedimento;
	private Date mDataInizio;
	private Date mDataFine;

	// COSTRUTTORE DI DEFAULT
	public CancAssFascSiusModel() {
		super();
		mFasSiusIdFascicoloSius = null;
		mCodStatoProcedimento = "";
		mDescrStatoProcedimento = "";
		mDataInizio = null;
		mDataFine = null;
	}

	// COSTRUTTORE DI COPIA
	public CancAssFascSiusModel(CancAssFascSiusModel aModel) {
		super(aModel);
		this.mFasSiusIdFascicoloSius = aModel.mFasSiusIdFascicoloSius;
		this.mCodStatoProcedimento = aModel.mCodStatoProcedimento;
		this.mDescrStatoProcedimento = aModel.mDescrStatoProcedimento;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
	}

	// COSTRUTTORE MODEL
	public CancAssFascSiusModel(String aCodCancelleriaAssegnataria, String aDescrCancelleriaAssegnataria,
			String aCodUfficio, String aDescrUfficio, BigDecimal aFasSiusIdFascicoloSius,
			String aCodStatoProcedimento, String aDescrStatoProcedimento, Date aDataInizio, Date aDataFine,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento) {
		this.mCodCancelleriaAssegnataria = aCodCancelleriaAssegnataria;
		this.mDescCancelleriaAssegnataria = aDescrCancelleriaAssegnataria;
		this.mCodUfficio = aCodUfficio;
		this.mDescrUfficio = aDescrUfficio;
		this.mFasSiusIdFascicoloSius = aFasSiusIdFascicoloSius;
		this.mCodStatoProcedimento = aCodStatoProcedimento;
		this.mDescrStatoProcedimento = aDescrStatoProcedimento;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getFasSiusIdFascicoloSius() {
		return mFasSiusIdFascicoloSius;
	}

	public String getCodStatoProcedimento() {
		return mCodStatoProcedimento;
	}

	public String getDescrStatoProcedimento() {
		return mDescrStatoProcedimento;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	//
	// METODI SET()
	//

	public void setFasSiusIdFascicoloSius(BigDecimal aValore) {
		mFasSiusIdFascicoloSius = aValore;
	}

	public void setCodStatoProcedimento(String aValore) {
		mCodStatoProcedimento = aValore;
	}

	public void setDescrStatoProcedimento(String aValore) {
		mDescrStatoProcedimento = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

}