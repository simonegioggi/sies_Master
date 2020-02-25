package siap.regesies.regeresidenza.model;

/**
 * <p>Title: RegeResidenzaModel</p>
 * <p>Description: Classe Model che rappresenta il RegeResidenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.util.Date;

import siap.sico.residenza.model.ResidenzaModel;
import f3b.model.GenericModel;

public class RegeResidenzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -348955208719077479L;

	private String mIdFile;
	private String mCodStato;
	private String mDescrStato;
	private String mCodProvincia;
	private String mDescrProvincia;
	private String mCodComune;
	private String mDescrComune;
	private String mCap;
	private String mIndirizzo;
	private String mCodTipoResidenza;
	private String mDescrTipoResidenza;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private String mDescComuneEstero;
	private String mStringaResidenza;

	// COSTRUTTORE DI DEFAULT
	public RegeResidenzaModel() {
		this.mIdFile = "";
		this.mCodStato = "";
		this.mDescrStato = "";
		this.mCodProvincia = "";
		this.mDescrProvincia = "";
		this.mCodComune = "";
		this.mDescrComune = "";
		this.mCap = "";
		this.mIndirizzo = "";
		this.mCodTipoResidenza = "";
		this.mDescrTipoResidenza = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDescComuneEstero = "";
		this.mStringaResidenza = "-";
	}

	// COSTRUTTORE DI COPIA
	public RegeResidenzaModel(RegeResidenzaModel aModel) {
		this.mIdFile = aModel.mIdFile;
		this.mCodStato = aModel.mCodStato;
		this.mDescrStato = aModel.mDescrStato;
		this.mCodProvincia = aModel.mCodProvincia;
		this.mDescrProvincia = aModel.mDescrProvincia;
		this.mCodComune = aModel.mCodComune;
		this.mDescrComune = aModel.mDescrComune;
		this.mCap = aModel.mCap;
		this.mIndirizzo = aModel.mIndirizzo;
		this.mCodTipoResidenza = aModel.mCodTipoResidenza;
		this.mDescrTipoResidenza = aModel.mDescrTipoResidenza;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDescComuneEstero = aModel.mDescComuneEstero;
		calcolaStringaResidenza();
	}

	// COSTRUTTORE MODEL
	public RegeResidenzaModel(String aIdFile, String aCodStato, String aDescrStato, String aCodProvincia,
			String aDescrProvincia, String aCodComune, String aDescrComune, String aCap, String aIndirizzo,
			String aCodTipoResidenza, String aDescrTipoResidenza, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, String aDescComuneEstero) {
		this.mIdFile = aIdFile;
		this.mCodStato = aCodStato;
		this.mDescrStato = aDescrStato;
		this.mCodProvincia = aCodProvincia;
		this.mDescrProvincia = aDescrProvincia;
		this.mCodComune = aCodComune;
		this.mDescrComune = aDescrComune;
		this.mCap = aCap;
		this.mIndirizzo = aIndirizzo;
		this.mCodTipoResidenza = aCodTipoResidenza;
		this.mDescrTipoResidenza = aDescrTipoResidenza;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDescComuneEstero = aDescComuneEstero;
	}

	public ResidenzaModel toResidenza() {
		ResidenzaModel lResidenza = new ResidenzaModel();
		// --lResidenza.setIdFile(this.mIdFile);
		lResidenza.setCodStato(this.mCodStato);
		lResidenza.setDescrStato(this.mDescrStato);
		lResidenza.setCodProvincia(this.mCodProvincia);
		lResidenza.setDescrProvincia(this.mDescrProvincia);
		lResidenza.setCodComune(this.mCodComune);
		lResidenza.setDescrComune(this.mDescrComune);
		lResidenza.setCap(this.mCap);
		lResidenza.setIndirizzo(this.mIndirizzo);
		lResidenza.setCodTipoResidenza(this.mCodTipoResidenza);
		lResidenza.setDescrTipoResidenza(this.mDescrTipoResidenza);
		lResidenza.setCodOperatoreInserimento(this.mCodOperatoreInserimento);
		lResidenza.setDataInserimento(this.mDataInserimento);
		lResidenza.setCodUfficioInserimento(this.mCodUfficioInserimento);
		lResidenza.setDescrUfficioInserimento(this.mDescrUfficioInserimento);
		lResidenza.setCodOperatoreAggiornamento(this.mCodOperatoreAggiornamento);
		lResidenza.setDataAggiornamento(this.mDataAggiornamento);
		lResidenza.setCodUfficioAggiornamento(this.mCodUfficioAggiornamento);
		lResidenza.setDescrUfficioAggiornamento(this.mDescrUfficioAggiornamento);
		lResidenza.setDescComuneEstero(this.mDescComuneEstero);

		return lResidenza;
	}

	//
	// METODI GET()
	//

	public String getIdFile() {
		return mIdFile;
	}

	public String getCodStato() {
		return mCodStato;
	}

	public String getDescrStato() {
		return mDescrStato;
	}

	public String getCodProvincia() {
		return mCodProvincia;
	}

	public String getDescrProvincia() {
		return mDescrProvincia;
	}

	public String getCodComune() {
		return mCodComune;
	}

	public String getDescrComune() {
		return mDescrComune;
	}

	public String getCap() {
		return mCap;
	}

	public String getIndirizzo() {
		return mIndirizzo;
	}

	public String getCodTipoResidenza() {
		return mCodTipoResidenza;
	}

	public String getDescrTipoResidenza() {
		return mDescrTipoResidenza;
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

	public String getDescComuneEstero() {
		return mDescComuneEstero;
	}

	public String getStringaResidenza() {
		return mStringaResidenza;
	}

	//
	// METODI SET()
	//

	public void setIdFile(String aValore) {
		mIdFile = aValore;
	}

	public void setCodStato(String aValore) {
		mCodStato = aValore;
	}

	public void setDescrStato(String aValore) {
		mDescrStato = aValore;
	}

	public void setCodProvincia(String aValore) {
		mCodProvincia = aValore;
	}

	public void setDescrProvincia(String aValore) {
		mDescrProvincia = aValore;
	}

	public void setCodComune(String aValore) {
		mCodComune = aValore;
	}

	public void setDescrComune(String aValore) {
		mDescrComune = aValore;
	}

	public void setCap(String aValore) {
		mCap = aValore;
	}

	public void setIndirizzo(String aValore) {
		mIndirizzo = aValore;
	}

	public void setCodTipoResidenza(String aValore) {
		mCodTipoResidenza = aValore;
	}

	public void setDescrTipoResidenza(String aValore) {
		mDescrTipoResidenza = aValore;
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

	public void setDescComuneEstero(String aValore) {
		mDescComuneEstero = aValore;
	}

	public void setStringaResidenza(String aValore) {
		mStringaResidenza = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdFile + " - " + mCodStato + " - " + mDescrStato + " - " + mCodProvincia + " - "
				+ mDescrProvincia + " - " + mCodComune + " - " + mDescrComune + " - " + mCap + " - "
				+ mIndirizzo + " - " + mCodTipoResidenza + " - " + mDescrTipoResidenza + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mDescComuneEstero + " - " + mStringaResidenza;

		return lStr;
	}

	/**
	 * Compone una stringa residenza con i parametri valorizzati del Model
	 */
	public void calcolaStringaResidenza() {
		String lTempString = "";

		if (this.mDescrComune != null && this.mDescrComune.compareTo("-") != 0)
			lTempString += mDescrComune + " ";
		if (this.mCodProvincia != null && this.mCodProvincia.compareTo("-") != 0)
			lTempString += "(Prov. " + mCodProvincia + ") ";

		if (this.mCodStato.compareTo("039") != 0) { // straniero
			if (this.mDescComuneEstero != null && this.mDescComuneEstero != "")
				lTempString += this.mDescComuneEstero + " ";

			lTempString += this.mDescrStato + " ";
		}
		if (this.mIndirizzo != null && this.mIndirizzo != "")
			lTempString += this.mIndirizzo + " ";

		this.mStringaResidenza = lTempString;

	}

}