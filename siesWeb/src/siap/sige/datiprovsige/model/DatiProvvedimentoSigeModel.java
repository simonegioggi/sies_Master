package siap.sige.datiprovsige.model;

/**
* <p>Title: DatiProvvedimentoSigeModel</p>
* <p>Description: Classe Model che rappresenta il DatiProvvedimentoSige</p>
* Dati aggiuntivi legati all'oggetto di un Provvedimento SIGE.
* Dati memorizzati nella tabella DATI_PROVVEDIMENTO_SIGE.
* * <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class DatiProvvedimentoSigeModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -6750057688577275337L;

	private BigDecimal mIdDatiProvvedimentoSige;
	private String mCodTipoDatiProv;
	private String mDescrTipoDatiProv;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mTenIdTenoreSige;

	// COSTRUTTORE DI DEFAULT
	public DatiProvvedimentoSigeModel() {
		mIdDatiProvvedimentoSige = null;
		mCodTipoDatiProv = "";
		mDescrTipoDatiProv = "";
		mNote = "";
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodUfficioInserimento = "";
		mDescrUfficioInserimento = "";
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = "";
		mDescrUfficioAggiornamento = "";
		mTenIdTenoreSige = null;
	}

	// COSTRUTTORE DI COPIA
	public DatiProvvedimentoSigeModel(DatiProvvedimentoSigeModel aModel) {
		mIdDatiProvvedimentoSige = aModel.mIdDatiProvvedimentoSige;
		mCodTipoDatiProv = aModel.mCodTipoDatiProv;
		mDescrTipoDatiProv = aModel.mDescrTipoDatiProv;
		mNote = aModel.mNote;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		mTenIdTenoreSige = aModel.mTenIdTenoreSige;
	}

	// COSTRUTTORE MODEL
	public DatiProvvedimentoSigeModel(BigDecimal aIdDatiProvvedimentoSige, String aCodTipoDatiProv,
			String aDescrTipoDatiProv, String aNote, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aTenIdTenoreSige) {
		mIdDatiProvvedimentoSige = aIdDatiProvvedimentoSige;
		mCodTipoDatiProv = aCodTipoDatiProv;
		mDescrTipoDatiProv = aDescrTipoDatiProv;
		mNote = aNote;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		mTenIdTenoreSige = aTenIdTenoreSige;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdDatiProvvedimentoSige() {
		return mIdDatiProvvedimentoSige;
	}

	public String getCodTipoDatiProv() {
		return mCodTipoDatiProv;
	}

	public String getDescrTipoDatiProv() {
		return mDescrTipoDatiProv;
	}

	public String getNote() {
		return mNote;
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

	public BigDecimal getTenIdTenoreSige() {
		return mTenIdTenoreSige;
	}

	//
	// METODI SET()
	//

	public void setIdDatiProvvedimentoSige(BigDecimal aValore) {
		mIdDatiProvvedimentoSige = aValore;
	}

	public void setCodTipoDatiProv(String aValore) {
		mCodTipoDatiProv = aValore;
	}

	public void setDescrTipoDatiProv(String aValore) {
		mDescrTipoDatiProv = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
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

	public void setTenIdTenoreSige(BigDecimal aValore) {
		mTenIdTenoreSige = aValore;
	}

}