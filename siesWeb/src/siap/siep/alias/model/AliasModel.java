package siap.siep.alias.model;

/**
* <p>Title: AliasModel</p>
* <p>Description: Classe Model che rappresenta il Alias</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class AliasModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6658493332835453492L;

	private BigDecimal mIdAlias;
	private String mCognome;
	private String mNome;
	private String mPaternita;
	private String mCodFiscale;
	private String mDescrFiscale;
	private String mCodCs;
	private String mDescrCs;
	private String mCodAfis;
	private String mDescrAfis;
	private String mAttoNascita;
	private String mSesso;
	private String mCodComuneNascita;
	private String mDescrComuneNascita;
	private String mCodProvinciaNascita;
	private String mDescrProvinciaNascita;
	private String mCodStatoNascita;
	private String mDescrStatoNascita;
	private Date mDataNascita;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mSogIdSoggetto;
	private String mDescComuneNascitaEstero;

	// COSTRUTTORE DI DEFAULT
	public AliasModel() {
		this.mIdAlias = null;
		this.mCognome = "";
		this.mNome = "";
		this.mPaternita = "";
		this.mCodFiscale = "";
		this.mDescrFiscale = "";
		this.mCodCs = "";
		this.mDescrCs = "";
		this.mCodAfis = "";
		this.mDescrAfis = "";
		this.mAttoNascita = "";
		this.mSesso = "";
		this.mCodComuneNascita = "";
		this.mDescrComuneNascita = "";
		this.mDescComuneNascitaEstero = "";
		this.mCodProvinciaNascita = "";
		this.mDescrProvinciaNascita = "";
		this.mCodStatoNascita = "";
		this.mDescrStatoNascita = "";
		this.mDataNascita = null;
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mSogIdSoggetto = null;
		this.mDescComuneNascitaEstero = null;
	}

	// COSTRUTTORE DI COPIA
	public AliasModel(AliasModel aModel) {
		this.mIdAlias = aModel.mIdAlias;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mPaternita = aModel.mPaternita;
		this.mCodFiscale = aModel.mCodFiscale;
		this.mDescrFiscale = aModel.mDescrFiscale;
		this.mDescComuneNascitaEstero = aModel.mDescComuneNascitaEstero;
		this.mCodCs = aModel.mCodCs;
		this.mDescrCs = aModel.mDescrCs;
		this.mCodAfis = aModel.mCodAfis;
		this.mDescrAfis = aModel.mDescrAfis;
		this.mAttoNascita = aModel.mAttoNascita;
		this.mSesso = aModel.mSesso;
		this.mCodComuneNascita = aModel.mCodComuneNascita;
		this.mDescrComuneNascita = aModel.mDescrComuneNascita;
		this.mCodProvinciaNascita = aModel.mCodProvinciaNascita;
		this.mDescrProvinciaNascita = aModel.mDescrProvinciaNascita;
		this.mCodStatoNascita = aModel.mCodStatoNascita;
		this.mDescrStatoNascita = aModel.mDescrStatoNascita;
		this.mDataNascita = aModel.mDataNascita;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mDescComuneNascitaEstero = aModel.mDescComuneNascitaEstero;
	}

	// COSTRUTTORE MODEL
	public AliasModel(BigDecimal aIdAlias, String aCognome, String aNome, String aPaternita,
			String aCodFiscale, String aDescrFiscale, String aCodCs, String aDescrCs, String aCodAfis,
			String aDescrAfis, String aAttoNascita, String aSesso, String aCodComuneNascita,
			String aDescrComuneNascita, String aCodProvinciaNascita, String aDescrProvinciaNascita,
			String aCodStatoNascita, String aDescrStatoNascita, Date aDataNascita, String aNote,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aSogIdSoggetto,
			String aDescComuneNascitaEstero) {
		this.mIdAlias = aIdAlias;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mPaternita = aPaternita;
		this.mCodFiscale = aCodFiscale;
		this.mDescrFiscale = aDescrFiscale;
		this.mCodCs = aCodCs;
		this.mDescrCs = aDescrCs;
		this.mCodAfis = aCodAfis;
		this.mDescrAfis = aDescrAfis;
		this.mAttoNascita = aAttoNascita;
		this.mSesso = aSesso;
		this.mCodComuneNascita = aCodComuneNascita;
		this.mDescrComuneNascita = aDescrComuneNascita;
		this.mCodProvinciaNascita = aCodProvinciaNascita;
		this.mDescrProvinciaNascita = aDescrProvinciaNascita;
		this.mCodStatoNascita = aCodStatoNascita;
		this.mDescrStatoNascita = aDescrStatoNascita;
		this.mDataNascita = aDataNascita;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mDescComuneNascitaEstero = aDescComuneNascitaEstero;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAlias() {
		return mIdAlias;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getPaternita() {
		return mPaternita;
	}

	public String getCodFiscale() {
		return mCodFiscale;
	}

	public String getDescrFiscale() {
		return mDescrFiscale;
	}

	public String getCodCs() {
		return mCodCs;
	}

	public String getDescrCs() {
		return mDescrCs;
	}

	public String getCodAfis() {
		return mCodAfis;
	}

	public String getDescrAfis() {
		return mDescrAfis;
	}

	public String getAttoNascita() {
		return mAttoNascita;
	}

	public String getSesso() {
		return mSesso;
	}

	public String getCodComuneNascita() {
		return mCodComuneNascita;
	}

	public String getDescrComuneNascita() {
		return mDescrComuneNascita;
	}

	public String getCodProvinciaNascita() {
		return mCodProvinciaNascita;
	}

	public String getDescrProvinciaNascita() {
		return mDescrProvinciaNascita;
	}

	public String getCodStatoNascita() {
		return mCodStatoNascita;
	}

	public String getDescrStatoNascita() {
		return mDescrStatoNascita;
	}

	public Date getDataNascita() {
		return mDataNascita;
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

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public String getDescComuneNascitaEstero() {
		return mDescComuneNascitaEstero;
	}

	//
	// METODI SET()
	//

	public void setIdAlias(BigDecimal aValore) {
		mIdAlias = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setPaternita(String aValore) {
		mPaternita = aValore;
	}

	public void setCodFiscale(String aValore) {
		mCodFiscale = aValore;
	}

	public void setDescrFiscale(String aValore) {
		mDescrFiscale = aValore;
	}

	public void setCodCs(String aValore) {
		mCodCs = aValore;
	}

	public void setDescrCs(String aValore) {
		mDescrCs = aValore;
	}

	public void setCodAfis(String aValore) {
		mCodAfis = aValore;
	}

	public void setDescrAfis(String aValore) {
		mDescrAfis = aValore;
	}

	public void setAttoNascita(String aValore) {
		mAttoNascita = aValore;
	}

	public void setSesso(String aValore) {
		mSesso = aValore;
	}

	public void setCodComuneNascita(String aValore) {
		mCodComuneNascita = aValore;
	}

	public void setDescrComuneNascita(String aValore) {
		mDescrComuneNascita = aValore;
	}

	public void setCodProvinciaNascita(String aValore) {
		mCodProvinciaNascita = aValore;
	}

	public void setDescrProvinciaNascita(String aValore) {
		mDescrProvinciaNascita = aValore;
	}

	public void setCodStatoNascita(String aValore) {
		mCodStatoNascita = aValore;
	}

	public void setDescrStatoNascita(String aValore) {
		mDescrStatoNascita = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
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

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setDescComuneNascitaEstero(String aValore) {
		mDescComuneNascitaEstero = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAlias + " - " + mCognome + " - " + mNome + " - " + mPaternita + " - " + mCodFiscale
				+ " - " + mDescrFiscale + " - " + mCodCs + " - " + mDescrCs + " - " + mCodAfis + " - "
				+ mDescrAfis + " - " + mAttoNascita + " - " + mSesso + " - " + mCodComuneNascita + " - "
				+ mDescrComuneNascita + " - " + mCodProvinciaNascita + " - " + mDescrProvinciaNascita + " - "
				+ mCodStatoNascita + " - " + mDescrStatoNascita + " - " + mDataNascita + " - " + mNote + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mDescComuneNascitaEstero + " - " + mSogIdSoggetto;

		return lStr;
	}

}