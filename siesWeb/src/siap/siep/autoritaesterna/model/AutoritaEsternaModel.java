package siap.siep.autoritaesterna.model;

/**
* <p>Title: AutoritaEsternaModel</p>
* <p>Description: Classe Model che rappresenta il AutoritaEsterna</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AutoritaEsternaModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 1955032637287238503L;

	private BigDecimal mIdAutoritaEsterna;
	private String mCodTipoAutorita;
	private String mDescrTipoAutorita;
	private String mDescrizione;
	private String mCodSede;
	private String mDescrSede;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public AutoritaEsternaModel() {
		this.mIdAutoritaEsterna = null;
		this.mCodTipoAutorita = null;
		this.mDescrTipoAutorita = null;
		this.mDescrizione = null;
		this.mCodSede = null;
		this.mDescrSede = null;
		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = null;
		this.mDescrUfficioInserimento = null;
		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = null;
		this.mDescrUfficioAggiornamento = null;
	}

	// COSTRUTTORE DI COPIA
	public AutoritaEsternaModel(AutoritaEsternaModel aModel) {
		this.mIdAutoritaEsterna = aModel.mIdAutoritaEsterna;
		this.mCodTipoAutorita = aModel.mCodTipoAutorita;
		this.mDescrTipoAutorita = aModel.mDescrTipoAutorita;
		this.mDescrizione = aModel.mDescrizione;
		this.mCodSede = aModel.mCodSede;
		this.mDescrSede = aModel.mDescrSede;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	// COSTRUTTORE MODEL
	public AutoritaEsternaModel(BigDecimal aIdAutoritaEsterna, String aCodTipoAutorita,
			String aDescrTipoAutorita, String aDescrizione, String aCodSede, String aDescrSede,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdAutoritaEsterna = aIdAutoritaEsterna;
		this.mCodTipoAutorita = aCodTipoAutorita;
		this.mDescrTipoAutorita = aDescrTipoAutorita;
		this.mDescrizione = aDescrizione;
		this.mCodSede = aCodSede;
		this.mDescrSede = aDescrSede;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;

	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAutoritaEsterna() {
		return mIdAutoritaEsterna;
	}

	public String getCodTipoAutorita() {
		return mCodTipoAutorita;
	}

	public String getDescrTipoAutorita() {
		return mDescrTipoAutorita;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	public String getCodSede() {
		return mCodSede;
	}

	public String getDescrSede() {
		return mDescrSede;
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

	public void setIdAutoritaEsterna(BigDecimal aValore) {
		mIdAutoritaEsterna = aValore;
	}

	public void setCodTipoAutorita(String aValore) {
		mCodTipoAutorita = aValore;
	}

	public void setDescrTipoAutorita(String aValore) {
		mDescrTipoAutorita = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	public void setCodSede(String aValore) {
		mCodSede = aValore;
	}

	public void setDescrSede(String aValore) {
		mDescrSede = aValore;
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

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAutoritaEsterna + " - " + mCodTipoAutorita + " - " + mDescrTipoAutorita + " - "
				+ mDescrizione + " - " + mCodSede + " - " + mDescrSede + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento
				+ " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - "
				+ mCodUfficioAggiornamento;

		return lStr;
	}

}