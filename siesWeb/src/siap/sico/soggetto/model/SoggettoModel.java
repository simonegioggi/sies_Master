package siap.sico.soggetto.model;

/**
* <p>Title: SoggettoModel</p>
* <p>Description: Classe Model che rappresenta il Soggetto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class SoggettoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5196622006537788731L;

	private BigDecimal mIdSoggetto;
	private String mCodFiscale;
	private String mCodCs;
	private String mCodAfis;
	private String mCognome;
	private String mNome;
	private BigDecimal mAnnoNascita;
	private Date mDataNascita;
	private Date mDataReatoSius;// data commesso reato
	private String mDataNascitaPresunta;
	private Date mDataNascitaPresuntaCalc;
	private String mCodComuneNascita;
	private String mDescrComuneNascita;
	private String mCodProvinciaNascita;
	private String mDescrProvinciaNascita;
	private String mCodStatoNascita;
	private String mDescrStatoNascita;
	private String mDescComuneNascitaEstero;
	private String mNazionalita;
	private String mDescrNazionalita;
	private String mPaternita;
	private String mCognomeMadre;
	private String mNomeMadre;
	private String mSesso;
	private String mAttoNascita;
	private String mNote;
	private String mCodComuneCasellario;
	private String mDescrComuneCasellario;
	private String mFlagPresenzaFascicolo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mMeseNascita;
	private Vector mDettaglioFascicoli;
	// Per Ricerca Alias
	private String mSogIdSoggetto;
	private String[] mClassiFascicolo;
	private BigDecimal mKeySoggNsc;
	private BigDecimal mEtaPresuntaAnni;
	private BigDecimal mEtaPresuntaMesi;
	// variabile utilizzata nelle stampe, indica
	// se il soggetto è maggiorenne oppure minorenne
	private String mStatoMinorMagg;

	// COSTRUTTORE DI DEFAULT
	public SoggettoModel() {
		this.mIdSoggetto = new BigDecimal(0);
		this.mCodFiscale = "";
		this.mCodCs = "";
		this.mCodAfis = "";
		this.mCognome = "";
		this.mNome = "";
		this.mAnnoNascita = null;
		this.mDataNascita = null;
		this.mDataReatoSius = null;
		this.mDataNascitaPresunta = "";
		this.mDataNascitaPresuntaCalc = null;
		this.mCodComuneNascita = "";
		this.mDescrComuneNascita = "";
		this.mCodProvinciaNascita = "";
		this.mDescrProvinciaNascita = "";
		this.mCodStatoNascita = "";
		this.mDescrStatoNascita = "";
		this.mDescComuneNascitaEstero = "";
		this.mNazionalita = "";
		this.mDescrNazionalita = "";
		this.mPaternita = "";
		this.mCognomeMadre = "";
		this.mNomeMadre = "";
		this.mSesso = "";
		this.mAttoNascita = "";
		this.mNote = "";
		this.mCodComuneCasellario = "";
		this.mDescrComuneCasellario = "";
		this.mFlagPresenzaFascicolo = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mMeseNascita = null;
		this.mDettaglioFascicoli = null;
		this.mSogIdSoggetto = "";
		this.mClassiFascicolo = null;
		this.mKeySoggNsc = null;
		this.mEtaPresuntaAnni = null;
		this.mEtaPresuntaMesi = null;
		this.mStatoMinorMagg = "";
	}

	// COSTRUTTORE DI COPIA
	public SoggettoModel(SoggettoModel aModel) {
		this.mIdSoggetto = aModel.mIdSoggetto;
		this.mCodFiscale = aModel.mCodFiscale;
		this.mCodCs = aModel.mCodCs;
		this.mCodAfis = aModel.mCodAfis;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mAnnoNascita = aModel.mAnnoNascita;
		this.mDataNascita = aModel.mDataNascita;
		this.mDataReatoSius = aModel.mDataReatoSius;
		this.mDataNascitaPresunta = aModel.mDataNascitaPresunta;
		this.mDataNascitaPresuntaCalc = aModel.mDataNascitaPresuntaCalc;
		this.mCodComuneNascita = aModel.mCodComuneNascita;
		this.mDescrComuneNascita = aModel.mDescrComuneNascita;
		this.mCodProvinciaNascita = aModel.mCodProvinciaNascita;
		this.mDescrProvinciaNascita = aModel.mDescrProvinciaNascita;
		this.mCodStatoNascita = aModel.mCodStatoNascita;
		this.mDescrStatoNascita = aModel.mDescrStatoNascita;
		this.mDescComuneNascitaEstero = aModel.mDescComuneNascitaEstero;
		this.mNazionalita = aModel.mNazionalita;
		this.mDescrNazionalita = aModel.mDescrNazionalita;
		this.mPaternita = aModel.mPaternita;
		this.mCognomeMadre = aModel.mCognomeMadre;
		this.mNomeMadre = aModel.mNomeMadre;
		this.mSesso = aModel.mSesso;
		this.mAttoNascita = aModel.mAttoNascita;
		this.mNote = aModel.mNote;
		this.mCodComuneCasellario = aModel.mCodComuneCasellario;
		this.mDescrComuneCasellario = aModel.mDescrComuneCasellario;
		this.mFlagPresenzaFascicolo = aModel.mFlagPresenzaFascicolo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mMeseNascita = aModel.mMeseNascita;
		this.mDettaglioFascicoli = aModel.mDettaglioFascicoli;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mClassiFascicolo = aModel.mClassiFascicolo;
		this.mKeySoggNsc = aModel.mKeySoggNsc;
		this.mEtaPresuntaAnni = aModel.mEtaPresuntaAnni;
		this.mEtaPresuntaMesi = aModel.mEtaPresuntaMesi;
	}

	// COSTRUTTORE MODEL
	public SoggettoModel(BigDecimal aIdSoggetto, String aCodFiscale, String aCodCs, String aCodAfis,
			String aCognome, String aNome, BigDecimal aAnnoNascita, Date aDataNascita, Date aDataReatoSius,
			String aDataNascitaPresunta, Date aDataNascitaPresuntaCalc, String aCodComuneNascita,
			String aDescrComuneNascita, String aCodProvinciaNascita, String aDescrProvinciaNascita,
			String aCodStatoNascita, String aDescrStatoNascita, String aDescComuneNascitaEstero,
			String aNazionalita, String aDescrNazionalita, String aPaternita, String aCognomeMadre,
			String aNomeMadre, String aSesso, String aAttoNascita, String aNote, String aCodComuneCasellario,
			String aDescrComuneCasellario, String aFlagPresenzaFascicolo, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			BigDecimal aMeseNascita, Vector aDettaglioFascicoli, String aSogIdSoggetto,
			BigDecimal aKeySoggNsc, BigDecimal aEtaPresuntaAnni, BigDecimal aEtaPresuntaMesi) {
		this();

		this.mIdSoggetto = aIdSoggetto;
		if (aCodFiscale != null)
			this.mCodFiscale = aCodFiscale;

		if (aCodCs != null)
			this.mCodCs = aCodCs;

		if (aCodAfis != null)
			this.mCodAfis = aCodAfis;

		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mAnnoNascita = aAnnoNascita;
		this.mDataNascita = aDataNascita;
		this.mDataReatoSius = aDataReatoSius;
		this.mDataNascitaPresunta = aDataNascitaPresunta;
		this.mDataNascitaPresuntaCalc = aDataNascitaPresuntaCalc;
		this.mCodComuneNascita = aCodComuneNascita;
		this.mDescrComuneNascita = aDescrComuneNascita;
		this.mCodProvinciaNascita = aCodProvinciaNascita;
		this.mDescrProvinciaNascita = aDescrProvinciaNascita;
		this.mCodStatoNascita = aCodStatoNascita;

		if (aDescrStatoNascita != null)
			this.mDescrStatoNascita = aDescrStatoNascita.toUpperCase();

		if (aDescComuneNascitaEstero != null)
			this.mDescComuneNascitaEstero = aDescComuneNascitaEstero.toUpperCase();

		this.mNazionalita = aNazionalita;

		if (aDescrNazionalita != null)
			this.mDescrNazionalita = aDescrNazionalita.toUpperCase();

		if (aPaternita != null)
			this.mPaternita = aPaternita;

		if (aCognomeMadre != null)
			this.mCognomeMadre = aCognomeMadre;

		if (aNomeMadre != null)
			this.mNomeMadre = aNomeMadre;

		this.mSesso = aSesso;

		if (aAttoNascita != null)
			this.mAttoNascita = aAttoNascita;

		if (aNote != null)
			this.mNote = aNote;

		this.mCodComuneCasellario = aCodComuneCasellario;
		this.mDescrComuneCasellario = aDescrComuneCasellario;
		this.mFlagPresenzaFascicolo = aFlagPresenzaFascicolo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		// this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mMeseNascita = aMeseNascita;
		this.mDettaglioFascicoli = aDettaglioFascicoli;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mClassiFascicolo = null;

		if (aKeySoggNsc != null)
			this.mKeySoggNsc = aKeySoggNsc;

		this.mEtaPresuntaAnni = aEtaPresuntaAnni;
		this.mEtaPresuntaMesi = aEtaPresuntaMesi;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdSoggetto() {
		return mIdSoggetto;
	}

	public String getCodFiscale() {
		return mCodFiscale;
	}

	public String getCodCs() {
		return mCodCs;
	}

	public String getCodAfis() {
		return mCodAfis;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public BigDecimal getAnnoNascita() {
		return mAnnoNascita;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	public Date getDataReatoSius() {
		return mDataReatoSius;
	}

	public String getDataNascitaPresunta() {
		return mDataNascitaPresunta;
	}

	public Date getDataNascitaPresuntaCalc() {
		return mDataNascitaPresuntaCalc;
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

	public String getDescComuneNascitaEstero() {
		return mDescComuneNascitaEstero;
	}

	public String getNazionalita() {
		return mNazionalita;
	}

	public String getDescrNazionalita() {
		return mDescrNazionalita;
	}

	public String getPaternita() {
		return mPaternita;
	}

	public String getCognomeMadre() {
		return mCognomeMadre;
	}

	public String getNomeMadre() {
		return mNomeMadre;
	}

	public String getSesso() {
		return mSesso;
	}

	public String getAttoNascita() {
		return mAttoNascita;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodComuneCasellario() {
		return mCodComuneCasellario;
	}

	public String getDescrComuneCasellario() {
		return mDescrComuneCasellario;
	}

	public String getFlagPresenzaFascicolo() {
		return mFlagPresenzaFascicolo;
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

	public BigDecimal getMeseNascita() {
		return mMeseNascita;
	}

	public Vector getDettaglioFascicoli() {
		return mDettaglioFascicoli;
	}

	public String getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public String[] getClassiFascicolo() {
		return mClassiFascicolo;
	}

	public BigDecimal getKeySoggNsc() {
		return mKeySoggNsc;
	}

	public BigDecimal getEtaPresuntaAnni() {
		return mEtaPresuntaAnni;
	}

	public BigDecimal getEtaPresuntaMesi() {
		return mEtaPresuntaMesi;
	}

	public String getStatoMinorMagg() {
		return mStatoMinorMagg;
	}

	//
	// METODI SET()
	//

	public void setIdSoggetto(BigDecimal aValore) {
		mIdSoggetto = aValore;
	}

	public void setCodFiscale(String aValore) {
		if (aValore != null)
			mCodFiscale = aValore.toUpperCase();
	}

	public void setCodCs(String aValore) {
		mCodCs = aValore;
	}

	public void setCodAfis(String aValore) {
		if (aValore != null)
			mCodAfis = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore.toUpperCase();
	}

	public void setNome(String aValore) {
		mNome = aValore.toUpperCase();
	}

	public void setAnnoNascita(BigDecimal aValore) {
		mAnnoNascita = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setDataReatoSius(Date aValore) {
		mDataReatoSius = aValore;
	}

	public void setDataNascitaPresunta(String aValore) {
		mDataNascitaPresunta = aValore;
	}

	public void setDataNascitaPresuntaCalc(Date aValore) {
		mDataNascitaPresuntaCalc = aValore;
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
		if (aValore != null)
			mDescrStatoNascita = aValore.toUpperCase();
	}

	public void setDescComuneNascitaEstero(String aValore) {
		if (aValore != null)
			mDescComuneNascitaEstero = aValore.toUpperCase();
	}

	public void setNazionalita(String aValore) {
		if (aValore != null)
			mNazionalita = aValore;
	}

	public void setDescrNazionalita(String aValore) {
		if (aValore != null)
			mDescrNazionalita = aValore;
	}

	public void setPaternita(String aValore) {
		if (aValore != null)
			mPaternita = aValore.toUpperCase();
	}

	public void setCognomeMadre(String aValore) {
		if (aValore != null)
			mCognomeMadre = aValore.toUpperCase();
	}

	public void setNomeMadre(String aValore) {
		if (aValore != null)
			mNomeMadre = aValore.toUpperCase();
	}

	public void setSesso(String aValore) {
		if (aValore != null)
			mSesso = aValore;
	}

	public void setAttoNascita(String aValore) {
		if (aValore != null)
			mAttoNascita = aValore;
	}

	public void setNote(String aValore) {
		if (aValore != null)
			mNote = aValore;
	}

	public void setCodComuneCasellario(String aValore) {
		if (aValore != null)
			mCodComuneCasellario = aValore;
	}

	public void setDescrComuneCasellario(String aValore) {
		if (aValore != null)
			mDescrComuneCasellario = aValore;
	}

	public void setFlagPresenzaFascicolo(String aValore) {
		if (aValore != null)
			mFlagPresenzaFascicolo = aValore;
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

	public void setMeseNascita(BigDecimal aValore) {
		mMeseNascita = aValore;
	}

	public void setDettaglioFascicoli(Vector aValore) {
		mDettaglioFascicoli = aValore;
	}

	public void setSogIdSoggetto(String aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setClassiFascicolo(String[] aValore) {
		mClassiFascicolo = aValore;
	}

	public void setKeySoggNsc(BigDecimal aValore) {
		mKeySoggNsc = aValore;
	}

	public void setEtaPresuntaAnni(BigDecimal aValore) {
		mEtaPresuntaAnni = aValore;
	}

	public void setEtaPresuntaMesi(BigDecimal aValore) {
		mEtaPresuntaMesi = aValore;
	}

	public void setStatoMinorMagg(String aValore) {
		mStatoMinorMagg = aValore;
	}

	public String toString() {
		String lToString = this.mIdSoggetto + " - " + this.mCodFiscale + " - " + this.mCodCs + " - "
				+ this.mCodAfis + " - " + this.mCognome + " - " + this.mNome + " - " + this.mDataNascita
				+ " - " + this.mDataReatoSius + " - " + this.mCodComuneNascita + " - "
				+ this.mDescrComuneNascita + " - " + this.mCodProvinciaNascita + " - "
				+ this.mDescrProvinciaNascita + " - " + this.mCodStatoNascita + " - "
				+ this.mDescrStatoNascita + " - " + this.mDescComuneNascitaEstero + " - " + this.mNazionalita
				+ " - " + this.mDescrNazionalita + " - " + this.mPaternita + " - " + this.mCognomeMadre
				+ " - " + this.mNomeMadre + " - " + this.mSesso + " - " + this.mAttoNascita + " - "
				+ this.mNote + " - " + this.mCodComuneCasellario + " - " + this.mCodOperatoreInserimento
				+ " - " + this.mDataInserimento + " - " + this.mCodOperatoreAggiornamento + " - "
				+ this.mDataAggiornamento + " - " + this.mMeseNascita + " - " + this.mDettaglioFascicoli
				+ " - " + this.mSogIdSoggetto + " - " + this.mKeySoggNsc + " - " + this.mEtaPresuntaAnni
				+ " - " + this.mEtaPresuntaMesi;

		return lToString;
	}

}