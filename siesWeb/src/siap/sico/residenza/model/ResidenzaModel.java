package siap.sico.residenza.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * Title: ResidenzaModel
 * Description: Classe Model che rappresenta il Residenza
 *
 * @version 1.0
 */
public class ResidenzaModel extends GenericModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -478622976384235458L;

	private BigDecimal mIdResidenza;
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
	private BigDecimal mSogIdSoggetto;
	private String mFlgDomAvv;
	private BigDecimal mIdParteUdienza;
	private String mFlgDomicilioDifensore;
	// MEV_2023-13: aggiunta variabile nel modello e gestita ovunque
	private BigDecimal mIdCivilmenteObbligato;

	// COSTRUTTORE DI DEFAULT
	public ResidenzaModel() {

		this.mIdResidenza = null;
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
		this.mSogIdSoggetto = null;
		this.mFlgDomAvv = "";
		this.mIdParteUdienza = null;
		this.mFlgDomicilioDifensore = "";
		this.mIdCivilmenteObbligato = null;
	}

	// COSTRUTTORE DI COPIA
	public ResidenzaModel(ResidenzaModel aModel) {

		this.mIdResidenza = aModel.mIdResidenza;
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
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mDescComuneEstero = aModel.mDescComuneEstero;
		this.mFlgDomAvv = aModel.mFlgDomAvv;
		this.mIdParteUdienza = aModel.mIdParteUdienza;
		this.mFlgDomicilioDifensore = aModel.mFlgDomicilioDifensore;
		this.mIdCivilmenteObbligato = aModel.mIdCivilmenteObbligato;
	}

	// COSTRUTTORE MODEL
	public ResidenzaModel(BigDecimal aIdResidenza, String aCodStato, String aDescrStato, String aCodProvincia,
			String aDescrProvincia, String aCodComune, String aDescrComune, String aCap, String aIndirizzo,
			String aCodTipoResidenza, String aDescrTipoResidenza, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aSogIdSoggetto, String aFlgDomAvv,
			BigDecimal aIdParteUdienza, String aFlgDomicilioDifensore, BigDecimal aIdCivilmenteObbligato) {

		this.mIdResidenza = aIdResidenza;
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
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mFlgDomAvv = aFlgDomAvv;
		this.mIdParteUdienza = aIdParteUdienza;
		this.mFlgDomicilioDifensore = aFlgDomicilioDifensore;
		this.mIdCivilmenteObbligato = aIdCivilmenteObbligato;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdResidenza() {
		return mIdResidenza;
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

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public String getFlgDomAvv() {
		return mFlgDomAvv;
	}

	public BigDecimal getIdParteUdienza() {
		return mIdParteUdienza;
	}

	//
	// METODI SET()
	//
	public BigDecimal getIdCivilmenteObbligato() {
		return mIdCivilmenteObbligato;
	}

	public void setIdCivilmenteObbligato(BigDecimal mIdCivilmenteObbligato) {
		this.mIdCivilmenteObbligato = mIdCivilmenteObbligato;
	}

	public void setIdResidenza(BigDecimal aValore) {
		mIdResidenza = aValore;
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

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setFlgDomAvv(String aValore) {
		mFlgDomAvv = aValore;
	}

	public void setIdParteUdienza(BigDecimal aValore) {
		mIdParteUdienza = aValore;
	}

	public void setFlgDomicilioDifensore(String aValore) {
		mFlgDomicilioDifensore = aValore;
	}

	public String getFlgDomicilioDifensore() {
		return mFlgDomicilioDifensore;
	}

	public String toString() {

		String lStr = new String();
		lStr = "" + mIdResidenza + " - " + mCodStato + " - " + mDescrStato + " - " + mCodProvincia + " - "
				+ mDescrProvincia + " - " + mCodComune + " - " + mDescrComune + " - " + mCap + " - "
				+ mIndirizzo + " - " + mCodTipoResidenza + " - " + mDescrTipoResidenza + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mDescComuneEstero + " - " + mSogIdSoggetto + " - " + mFlgDomAvv + " - " + mIdParteUdienza
				+ " - " + mFlgDomicilioDifensore + " - " + mIdCivilmenteObbligato;

		return lStr;
	}

	/**
	 * Compone una stringa residenza con i parametri valorizzati del Model
	 */
	public String toStringaResidenza() {

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

		return lTempString;
	}

}