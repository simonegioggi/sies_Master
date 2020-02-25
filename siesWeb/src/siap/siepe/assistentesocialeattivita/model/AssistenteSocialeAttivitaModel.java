package siap.siepe.assistentesocialeattivita.model;

/**
* <p>Title: AssistenteSocialeAttivitaModel</p>
* <p>Description: Classe Model che rappresenta l'entità AssistenteSocialeAttivita
 * che rappresenta la relazione tra un assistente sociale e l'attività di sua competenza.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AssistenteSocialeAttivitaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -327266738053164714L;
	private Date mDataInizio;
	private Date mDataFine;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mAssSocIdAssSociale;
	private BigDecimal mAttIdAttivita;

	// COSTRUTTORE DI DEFAULT
	public AssistenteSocialeAttivitaModel() {
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mAssSocIdAssSociale = null;
		this.mAttIdAttivita = null;
	}

	// COSTRUTTORE DI COPIA
	public AssistenteSocialeAttivitaModel(AssistenteSocialeAttivitaModel aModel) {
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mAssSocIdAssSociale = aModel.mAssSocIdAssSociale;
		this.mAttIdAttivita = aModel.mAttIdAttivita;
	}

	// COSTRUTTORE MODEL
	public AssistenteSocialeAttivitaModel(Date aDataInizio, Date aDataFine, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aAssSocIdAssSociale, BigDecimal aAttIdAttivita) {
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
		this.mAssSocIdAssSociale = aAssSocIdAssSociale;
		this.mAttIdAttivita = aAttIdAttivita;
	}

	//
	// METODI GET()
	//

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

	public BigDecimal getAssSocIdAssSociale() {
		return mAssSocIdAssSociale;
	}

	public BigDecimal getAttIdAttivita() {
		return mAttIdAttivita;
	}

	//
	// METODI SET()
	//

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

	public void setAssSocIdAssSociale(BigDecimal aValore) {
		mAssSocIdAssSociale = aValore;
	}

	public void setAttIdAttivita(BigDecimal aValore) {
		mAttIdAttivita = aValore;
	}

}
