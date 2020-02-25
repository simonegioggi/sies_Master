package siap.sico.ufficio.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class UfficioModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -307214120777654189L;

	private String mCodUfficio;
	private String mCodTipoUfficio;
	private String mCodDistretto;
	private String mCodProvincia;
	private String mCodComune;
	private Date mDataCaricamentoRege;
	private String mCodUfficioCompetente;
	private String mIndirizzo;
	private String mCap;
	private String mTelefono;
	private String mFax;
	private String mEMail;

	private String mDescrTipoUfficio;
	private String mDescrProvincia;
	private String mDescrComune;
	private String mStringaUfficio;

	// Nuovi campi Luigi 30-08-2005
	private String mCodOperatoreAgg;
	private Date mDataAgg;
	private String mCodUfficioAgg;
	private String mDescrUfficioAgg;

	private List /* <UfficioAccorpatoModel> */ mUfficiAccorpati;

	// COSTRUTTORE DI DEFAULT
	public UfficioModel() {
		this.mCodUfficio = "";
		this.mCodTipoUfficio = "";
		this.mCodDistretto = "";
		this.mCodProvincia = "";
		this.mCodComune = "";
		this.mDataCaricamentoRege = null;
		this.mCodUfficioCompetente = "";
		this.mIndirizzo = "";
		this.mCap = "";
		this.mTelefono = "";
		this.mFax = "";
		this.mEMail = "";

		this.mDescrTipoUfficio = "";
		this.mDescrProvincia = "";
		this.mDescrComune = "";

		this.mCodOperatoreAgg = "";
		this.mDataAgg = null;
		this.mCodUfficioAgg = "";
		this.mDescrUfficioAgg = "";

		this.mUfficiAccorpati = null;

	}

	// COSTRUTTORE DI COPIA
	public UfficioModel(UfficioModel aModel) {
		this.mCodUfficio = aModel.mCodUfficio;
		this.mCodTipoUfficio = aModel.mCodTipoUfficio;
		this.mCodDistretto = aModel.mCodDistretto;
		this.mCodProvincia = aModel.mCodProvincia;
		this.mCodComune = aModel.mCodComune;
		this.mDataCaricamentoRege = aModel.mDataCaricamentoRege;
		this.mCodUfficioCompetente = aModel.mCodUfficioCompetente;
		this.mIndirizzo = aModel.mIndirizzo;
		this.mCap = aModel.mCap;
		this.mTelefono = aModel.mTelefono;
		this.mFax = aModel.mFax;
		this.mEMail = aModel.mEMail;

		this.mDescrTipoUfficio = aModel.mDescrTipoUfficio;
		this.mDescrProvincia = aModel.mDescrProvincia;
		this.mDescrComune = aModel.mDescrComune;

		this.mCodOperatoreAgg = aModel.mCodOperatoreAgg;
		this.mDataAgg = aModel.mDataAgg;
		this.mCodUfficioAgg = aModel.mCodUfficioAgg;
		this.mDescrUfficioAgg = aModel.mDescrUfficioAgg;

		this.mUfficiAccorpati = aModel.mUfficiAccorpati;
	}

	// COSTRUTTORE MODEL
	public UfficioModel(String aCodUfficio, String aCodTipoUfficio, String aDescrTipoUfficio,
			String aCodDistretto, String aCodProvincia, String aDescrProvincia, String aCodComune,
			String aDescrComune, Date aDataCaricamentoRege, String aCodUfficioCompetente, String aIndirizzo,
			String aCap, String aTelefono, String aFax, String aEMail, List aUfficiAccorpati) {
		this.mCodUfficio = aCodUfficio;
		this.mCodTipoUfficio = aCodTipoUfficio;
		this.mCodDistretto = aCodDistretto;
		this.mCodProvincia = aCodProvincia;
		this.mCodComune = aCodComune;
		this.mDataCaricamentoRege = aDataCaricamentoRege;
		this.mCodUfficioCompetente = aCodUfficioCompetente;
		this.mIndirizzo = aIndirizzo;
		this.mCap = aCap;
		this.mTelefono = aTelefono;
		this.mFax = aFax;
		this.mEMail = aEMail;

		this.mDescrTipoUfficio = aDescrTipoUfficio;
		this.mDescrProvincia = aDescrProvincia;
		this.mDescrComune = aDescrComune;

		this.mCodOperatoreAgg = "";
		this.mDataAgg = null;
		this.mCodUfficioAgg = "";
		this.mDescrUfficioAgg = "";

		this.mUfficiAccorpati = aUfficiAccorpati;
	}

	//
	// METODI GET()
	//
	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getCodTipoUfficio() {
		return mCodTipoUfficio;
	}

	public String getCodDistretto() {
		return mCodDistretto;
	}

	public String getCodProvincia() {
		return mCodProvincia;
	}

	public String getCodComune() {
		return mCodComune;
	}

	public Date getDateCarimentoRege() {
		return mDataCaricamentoRege;
	}

	public String getCodUfficioCompetente() {
		return mCodUfficioCompetente;
	}

	public String getIndirizzo() {
		return mIndirizzo;
	}

	public String getCap() {
		return mCap;
	}

	public String getTelefono() {
		return mTelefono;
	}

	public String getFax() {
		return mFax;
	}

	public String getEMail() {
		return mEMail;
	}

	public String getDescrTipoUfficio() {
		return mDescrTipoUfficio;
	}

	public String getDescProvincia() {
		return mDescrProvincia;
	}

	public String getDescrComune() {
		return mDescrComune;
	}

	public String getStringaUfficio() {
		return mStringaUfficio;
	}

	public String getCodOperatoreAgg() {
		return mCodOperatoreAgg;
	}

	public Date getDataAgg() {
		return mDataAgg;
	}

	public String getCodUfficioAgg() {
		return mCodUfficioAgg;
	}

	public String getDescrUfficioAgg() {
		return mDescrUfficioAgg;
	}

	public List getUfficiAccorpati() {
		return mUfficiAccorpati;
	}

	//
	// METODI SET()
	//
	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setCodTipoUfficio(String aValore) {
		mCodTipoUfficio = aValore;
	}

	public void setCodDistretto(String aValore) {
		mCodDistretto = aValore;
	}

	public void setCodProvincia(String aValore) {
		mCodProvincia = aValore;
	}

	public void setCodComune(String aValore) {
		mCodComune = aValore;
	}

	public void setDateCarimentoRege(Date aValore) {
		mDataCaricamentoRege = aValore;
	}

	public void setCodUfficioCompetente(String aValore) {
		mCodUfficioCompetente = aValore;
	}

	public void setIndirizzo(String aValore) {
		mIndirizzo = aValore;
	}

	public void setCap(String aValore) {
		mCap = aValore;
	}

	public void setTelefono(String aValore) {
		mTelefono = aValore;
	}

	public void setFax(String aValore) {
		mFax = aValore;
	}

	public void setEMail(String aValore) {
		mEMail = aValore;
	}

	public void setDescrTipoUfficio(String aValore) {
		mDescrTipoUfficio = aValore;
	}

	public void setDescrProvincia(String aValore) {
		mDescrProvincia = aValore;
	}

	public void setDescrComune(String aValore) {
		mDescrComune = aValore;
	}

	public void setStringaUfficio(String aValore) {
		mStringaUfficio = aValore;
	}

	public void setCodOperatoreAgg(String aValore) {
		mCodOperatoreAgg = aValore;
	}

	public void setDataAgg(Date aValore) {
		mDataAgg = aValore;
	}

	public void setCodUfficioAgg(String aValore) {
		mCodUfficioAgg = aValore;
	}

	public void setDescrUfficioAgg(String aValore) {
		mDescrUfficioAgg = aValore;
	}

	public void setUfficiAccorpati(List aValore) {
		mUfficiAccorpati = aValore;
	}

	public String toString() {
		String lToString = this.mCodUfficio + " - " + this.mCodTipoUfficio + " - " + this.mDescrTipoUfficio
				+ " - " + this.mCodDistretto + " - " + this.mCodProvincia + " - " + this.mDescrProvincia
				+ " - " + this.mCodComune + " - " + this.mDescrComune + " - " + this.mDataCaricamentoRege
				+ " - " + this.mCodUfficioCompetente + " - " + this.mIndirizzo + " - " + this.mCap + " - "
				+ this.mTelefono + " - " + this.mFax + " - " + mEMail + " - " + mCodOperatoreAgg + " - "
				+ mDataAgg + " - " + mCodUfficioAgg + " - " + mUfficiAccorpati;
		return lToString;
	}

	public void settaStringaUfficio() {
		String lStringaUfficio = "";
		if (this.getCodTipoUfficio().equals("PGCAP") || this.getCodTipoUfficio().equals("PGCSS")) {

			lStringaUfficio = "Corte d'Appello";
			this.mStringaUfficio = lStringaUfficio;

		} else if (this.getCodTipoUfficio().equals("PM") || this.getCodTipoUfficio().equals("PMM")
				|| this.getCodTipoUfficio().equals("PMPT") || this.getCodTipoUfficio().equals("PROC")) {
			lStringaUfficio = "Tribunale";
			this.mStringaUfficio = lStringaUfficio;

		}
	}

	/**
	 * Metodo che verifica se il aCodUfficio è il codice di un ufficio di competenza ovvero se stesso o un
	 * ufficio accorpato
	 * 
	 * @param aCodUfficio
	 * @return true se aCodUfficio coincide con lufficio corrente o uno degli uffici accorpati false
	 *         altrimenti
	 * @since 19/09/2013
	 */
	public boolean isUfficioDiCompetenza(String aCodUfficio) {
		boolean lIsDiCompetenza = false;

		if (this.mCodUfficio.equalsIgnoreCase(aCodUfficio))
			lIsDiCompetenza = true;
		else if (this.mUfficiAccorpati != null) {
			Iterator itx = this.mUfficiAccorpati.iterator();
			while (itx.hasNext()) {
				UfficioAccorpatoModel uffAcc = (UfficioAccorpatoModel) itx.next();

				if (aCodUfficio.equalsIgnoreCase(uffAcc.getCodUfficio())) {
					lIsDiCompetenza = true;
					break;
				}
			}
		}
		return lIsDiCompetenza;

	}

}