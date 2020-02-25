package siap.sius.cancelleriaassegnataria.model;

/**
* <p>Title: CancelleriaAssegnatariaModel</p>
* <p>Description: Classe Model che rappresenta i dati inerenti la CancelleriaAssegnataria</p>
 * La CancelleriaAssegnataria rappresenta la Cancelleria cui viene associato un Fascicolo SIUS.
 * Ogni Ufficio SIUS può definire le Cancellerie da utilizzarsi a tale scopo.
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class CancelleriaAssegnatariaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1185693997823885546L;

	protected String mCodCancelleriaAssegnataria;
	protected String mDescCancelleriaAssegnataria;
	protected String mCodUfficio;
	protected String mDescrUfficio;
	protected String mCodOperatoreInserimento;
	protected Date mDataInserimento;
	protected String mCodUfficioInserimento;
	protected String mDescrUfficioInserimento;
	protected String mCodOperatoreAggiornamento;
	protected Date mDataAggiornamento;
	protected String mCodUfficioAggiornamento;
	protected String mDescrUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public CancelleriaAssegnatariaModel() {
		this.mCodCancelleriaAssegnataria = "";
		this.mCodUfficio = "";
		this.mDescrUfficio = "";
		this.mDescCancelleriaAssegnataria = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
	}

	// COSTRUTTORE DI COPIA
	public CancelleriaAssegnatariaModel(CancelleriaAssegnatariaModel aModel) {
		this.mCodCancelleriaAssegnataria = aModel.mCodCancelleriaAssegnataria;
		this.mDescCancelleriaAssegnataria = aModel.mDescCancelleriaAssegnataria;
		this.mCodUfficio = aModel.mCodUfficio;
		this.mDescrUfficio = aModel.mDescrUfficio;
		this.mDescCancelleriaAssegnataria = aModel.mDescCancelleriaAssegnataria;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	// COSTRUTTORE MODEL con parametri di assegnazione
	public CancelleriaAssegnatariaModel(String aCodCancelleriaAssegnataria,
			String aDescCancelleriaAssegnataria, String aCodUfficio, String aDescrUfficio,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento) {
		this.mCodCancelleriaAssegnataria = aCodCancelleriaAssegnataria;
		this.mDescCancelleriaAssegnataria = aDescCancelleriaAssegnataria;
		this.mCodUfficio = aCodUfficio;
		this.mDescrUfficio = aDescrUfficio;
		this.mDescCancelleriaAssegnataria = aDescCancelleriaAssegnataria;
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

	public String getCodCancelleriaAssegnataria() {
		return mCodCancelleriaAssegnataria;
	}

	public String getDescCancelleriaAssegnataria() {
		return mDescCancelleriaAssegnataria;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getDescrUfficio() {
		return mDescrUfficio;
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

	//
	// METODI SET()
	//

	public void setCodCancelleriaAssegnataria(String aValore) {
		mCodCancelleriaAssegnataria = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDescrUfficio(String aValore) {
		mDescrUfficio = aValore;
	}

	public void setDescCancelleriaAssegnataria(String aValore) {
		mDescCancelleriaAssegnataria = aValore;
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

}