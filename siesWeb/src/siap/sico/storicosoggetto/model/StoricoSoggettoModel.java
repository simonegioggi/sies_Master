package siap.sico.storicosoggetto.model;

/**
* <p>Title: StoricoSoggettoModel</p>
* <p>Description: Classe Model che rappresenta il StoricoSoggetto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.soggetto.model.SoggettoModel;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class StoricoSoggettoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2942680289089684621L;

	private BigDecimal mProgressivoStorico;
	private Date mDataVariazione;
	private BigDecimal mIdSoggettoVariato;
	private String mCodFiscale;
	private String mCodCs;
	private String mCodAfis;
	private String mCognome;
	private String mNome;
	private BigDecimal mAnnoNascita;
	private Date mDataNascita;
	private String mDataNascitaPresunta;
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
	private BigDecimal mMeseNascita;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mIdSoggettoNuovo;
	private BigDecimal mFasSieIdFascicoloSius;

	private BigDecimal mEtaPresuntaAnni;
	private BigDecimal mEtaPresuntaMesi;

	// COSTRUTTORE DI DEFAULT
	public StoricoSoggettoModel() {
		this.mProgressivoStorico = null;
		this.mDataVariazione = null;
		this.mIdSoggettoVariato = null;
		this.mCodFiscale = "";
		this.mCodCs = "";
		this.mCodAfis = "";
		this.mCognome = "";
		this.mNome = "";
		this.mAnnoNascita = null;
		this.mDataNascita = null;
		this.mDataNascitaPresunta = "";
		this.mCodComuneNascita = "";
		this.mDescrComuneNascita = "";
		this.mCodProvinciaNascita = "";
		this.mDescrProvinciaNascita = "";
		this.mCodStatoNascita = "";
		this.mDescrStatoNascita = "";
		this.mDescComuneNascitaEstero = "";
		this.mNazionalita = "";
		this.mPaternita = "";
		this.mCognomeMadre = "";
		this.mNomeMadre = "";
		this.mSesso = "";
		this.mAttoNascita = "";
		this.mNote = "";
		this.mCodComuneCasellario = "";
		this.mDescrComuneCasellario = "";
		this.mFlagPresenzaFascicolo = "";
		this.mMeseNascita = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mIdSoggettoNuovo = null;
		this.mFasSieIdFascicoloSius = null;
		this.mEtaPresuntaAnni = null;
		this.mEtaPresuntaMesi = null;
	}

	// COSTRUTTORE DI COPIA
	public StoricoSoggettoModel(StoricoSoggettoModel aModel) {
		this.mProgressivoStorico = aModel.mProgressivoStorico;
		this.mDataVariazione = aModel.mDataVariazione;
		this.mIdSoggettoVariato = aModel.mIdSoggettoVariato;
		this.mCodFiscale = aModel.mCodFiscale;
		this.mCodCs = aModel.mCodCs;
		this.mCodAfis = aModel.mCodAfis;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mAnnoNascita = aModel.mAnnoNascita;
		this.mDataNascita = aModel.mDataNascita;
		this.mDataNascitaPresunta = aModel.mDataNascitaPresunta;
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
		this.mMeseNascita = aModel.mMeseNascita;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mIdSoggettoNuovo = aModel.mIdSoggettoNuovo;
		this.mFasSieIdFascicoloSius = aModel.mFasSieIdFascicoloSius;
		this.mEtaPresuntaAnni = aModel.mEtaPresuntaAnni;
		this.mEtaPresuntaMesi = aModel.mEtaPresuntaMesi;
	}

	// COSTRUTTORE MODEL
	public StoricoSoggettoModel(BigDecimal aProgressivoStorico, Date aDataVariazione,
			BigDecimal aIdSoggettoVariato, String aCodFiscale, String aDescrFiscale, String aCodCs,
			String aDescrCs, String aCodAfis, String aDescrAfis, String aCognome, String aNome,
			BigDecimal aAnnoNascita, Date aDataNascita, String aDataNascitaPresunta, String aCodComuneNascita,
			String aDescrComuneNascita, String aCodProvinciaNascita, String aDescrProvinciaNascita,
			String aCodStatoNascita, String aDescrStatoNascita, String aDescComuneNascitaEstero,
			String aNazionalita, String aPaternita, String aCognomeMadre, String aNomeMadre, String aSesso,
			String aAttoNascita, String aNote, String aCodComuneCasellario, String aDescrComuneCasellario,
			String aFlagPresenzaFascicolo, BigDecimal aMeseNascita, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aFasSieIdFascicoloSiep, BigDecimal aIdSoggettoNuovo,
			BigDecimal aFasSieIdFascicoloSius, BigDecimal aEtaPresuntaAnni, BigDecimal aEtaPresuntaMesi) {
		this.mProgressivoStorico = aProgressivoStorico;
		this.mDataVariazione = aDataVariazione;
		this.mIdSoggettoVariato = aIdSoggettoVariato;
		this.mCodFiscale = aCodFiscale;
		this.mCodCs = aCodCs;
		this.mCodAfis = aCodAfis;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mAnnoNascita = aAnnoNascita;
		this.mDataNascita = aDataNascita;
		this.mDataNascitaPresunta = aDataNascitaPresunta;
		this.mCodComuneNascita = aCodComuneNascita;
		this.mDescrComuneNascita = aDescrComuneNascita;
		this.mCodProvinciaNascita = aCodProvinciaNascita;
		this.mDescrProvinciaNascita = aDescrProvinciaNascita;
		this.mCodStatoNascita = aCodStatoNascita;
		this.mDescrStatoNascita = aDescrStatoNascita;
		this.mDescComuneNascitaEstero = aDescComuneNascitaEstero;
		this.mNazionalita = aNazionalita;
		this.mPaternita = aPaternita;
		this.mCognomeMadre = aCognomeMadre;
		this.mNomeMadre = aNomeMadre;
		this.mSesso = aSesso;
		this.mAttoNascita = aAttoNascita;
		this.mNote = aNote;
		this.mCodComuneCasellario = aCodComuneCasellario;
		this.mDescrComuneCasellario = aDescrComuneCasellario;
		this.mFlagPresenzaFascicolo = aFlagPresenzaFascicolo;
		this.mMeseNascita = aMeseNascita;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mIdSoggettoNuovo = aIdSoggettoNuovo;
		this.mFasSieIdFascicoloSius = aFasSieIdFascicoloSius;
		this.mEtaPresuntaAnni = aEtaPresuntaAnni;
		this.mEtaPresuntaMesi = aEtaPresuntaMesi;
	}

	// COSTRUTTORE A PARTIRE DA UN SOGGETTO
	public StoricoSoggettoModel(SoggettoModel aSoggettoModel) {
		this.mProgressivoStorico = new BigDecimal(1);
		this.mDataVariazione = DateUtils.getSysDate();
		this.mIdSoggettoVariato = aSoggettoModel.getIdSoggetto();
		this.mCodFiscale = aSoggettoModel.getCodFiscale();
		this.mCodCs = aSoggettoModel.getCodCs();
		this.mCodAfis = aSoggettoModel.getCodAfis();
		this.mCognome = aSoggettoModel.getCognome();
		this.mNome = aSoggettoModel.getNome();
		this.mAnnoNascita = aSoggettoModel.getAnnoNascita();
		this.mDataNascita = aSoggettoModel.getDataNascita();
		this.mDataNascitaPresunta = aSoggettoModel.getDataNascitaPresunta();
		this.mCodComuneNascita = aSoggettoModel.getCodComuneNascita();
		this.mDescrComuneNascita = aSoggettoModel.getDescrComuneNascita();
		this.mCodProvinciaNascita = aSoggettoModel.getCodProvinciaNascita();
		this.mDescrProvinciaNascita = aSoggettoModel.getDescrProvinciaNascita();
		this.mCodStatoNascita = aSoggettoModel.getCodStatoNascita();
		this.mDescrStatoNascita = aSoggettoModel.getDescrStatoNascita();
		this.mDescComuneNascitaEstero = aSoggettoModel.getDescComuneNascitaEstero();
		this.mNazionalita = aSoggettoModel.getNazionalita();
		this.mPaternita = aSoggettoModel.getPaternita();
		this.mCognomeMadre = aSoggettoModel.getCognomeMadre();
		this.mNomeMadre = aSoggettoModel.getNomeMadre();
		this.mSesso = aSoggettoModel.getSesso();
		this.mAttoNascita = aSoggettoModel.getAttoNascita();
		this.mNote = aSoggettoModel.getNote();
		this.mCodComuneCasellario = aSoggettoModel.getCodComuneCasellario();
		this.mDescrComuneCasellario = aSoggettoModel.getDescrComuneCasellario();
		this.mFlagPresenzaFascicolo = aSoggettoModel.getFlagPresenzaFascicolo();
		this.mMeseNascita = aSoggettoModel.getMeseNascita();
		this.mCodOperatoreInserimento = aSoggettoModel.getCodOperatoreInserimento();
		this.mDataInserimento = aSoggettoModel.getDataInserimento();
		this.mCodUfficioInserimento = aSoggettoModel.getCodUfficioInserimento();
		this.mDescrUfficioInserimento = aSoggettoModel.getDescrUfficioInserimento();
		this.mCodOperatoreAggiornamento = aSoggettoModel.getCodOperatoreAggiornamento();
		this.mDataAggiornamento = aSoggettoModel.getDataAggiornamento();
		this.mCodUfficioAggiornamento = aSoggettoModel.getCodUfficioAggiornamento();
		this.mDescrUfficioAggiornamento = aSoggettoModel.getDescrUfficioAggiornamento();
		this.mFasSieIdFascicoloSiep = null;
		this.mIdSoggettoNuovo = null;
		this.mFasSieIdFascicoloSius = null;
		this.mEtaPresuntaAnni = aSoggettoModel.getEtaPresuntaAnni();
		this.mEtaPresuntaMesi = aSoggettoModel.getEtaPresuntaMesi();
	}

	//
	// METODI GET()
	//
	public BigDecimal getProgressivoStorico() {
		return mProgressivoStorico;
	}

	public Date getDataVariazione() {
		return mDataVariazione;
	}

	public BigDecimal getIdSoggettoVariato() {
		return mIdSoggettoVariato;
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

	public String getDataNascitaPresunta() {
		return mDataNascitaPresunta;
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

	public BigDecimal getMeseNascita() {
		return mMeseNascita;
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

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getIdSoggettoNuovo() {
		return mIdSoggettoNuovo;
	}

	public BigDecimal getFasSieIdFascicoloSius() {
		return mFasSieIdFascicoloSius;
	}

	public BigDecimal getEtaPresuntaAnni() {
		return mEtaPresuntaAnni;
	}

	public BigDecimal getEtaPresuntaMesi() {
		return mEtaPresuntaMesi;
	}

	//
	// METODI SET()
	//
	public void setProgressivoStorico(BigDecimal aValore) {
		mProgressivoStorico = aValore;
	}

	public void setDataVariazione(Date aValore) {
		mDataVariazione = aValore;
	}

	public void setIdSoggettoVariato(BigDecimal aValore) {
		mIdSoggettoVariato = aValore;
	}

	public void setCodFiscale(String aValore) {
		mCodFiscale = aValore;
	}

	public void setCodCs(String aValore) {
		mCodCs = aValore;
	}

	public void setCodAfis(String aValore) {
		mCodAfis = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setAnnoNascita(BigDecimal aValore) {
		mAnnoNascita = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setDataNascitaPresunta(String aValore) {
		mDataNascitaPresunta = aValore;
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

	public void setDescComuneNascitaEstero(String aValore) {
		mDescComuneNascitaEstero = aValore;
	}

	public void setNazionalita(String aValore) {
		mNazionalita = aValore;
	}

	public void setDescrNazionalita(String aValore) {
		if (aValore != null)
			mDescrNazionalita = aValore;
	}

	public void setPaternita(String aValore) {
		mPaternita = aValore;
	}

	public void setCognomeMadre(String aValore) {
		mCognomeMadre = aValore;
	}

	public void setNomeMadre(String aValore) {
		mNomeMadre = aValore;
	}

	public void setSesso(String aValore) {
		mSesso = aValore;
	}

	public void setAttoNascita(String aValore) {
		mAttoNascita = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodComuneCasellario(String aValore) {
		mCodComuneCasellario = aValore;
	}

	public void setDescrComuneCasellario(String aValore) {
		mDescrComuneCasellario = aValore;
	}

	public void setFlagPresenzaFascicolo(String aValore) {
		mFlagPresenzaFascicolo = aValore;
	}

	public void setMeseNascita(BigDecimal aValore) {
		mMeseNascita = aValore;
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setIdSoggettoNuovo(BigDecimal aValore) {
		mIdSoggettoNuovo = aValore;
	}

	public void setFasSieIdFascicoloSius(BigDecimal aValore) {
		mFasSieIdFascicoloSius = aValore;
	}

	public void setEtaPresuntaAnni(BigDecimal aValore) {
		mEtaPresuntaAnni = aValore;
	}

	public void setEtaPresuntaMesi(BigDecimal aValore) {
		mEtaPresuntaMesi = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mProgressivoStorico + " - " + mDataVariazione + " - " + mIdSoggettoVariato + " - "
				+ mCodFiscale + " - " + mCodCs + " - " + mCodAfis + " - " + mCognome + " - " + mNome + " - "
				+ mAnnoNascita + " - " + mDataNascita + " - " + mDataNascitaPresunta + " - "
				+ mCodComuneNascita + " - " + mDescrComuneNascita + " - " + mCodProvinciaNascita + " - "
				+ mDescrProvinciaNascita + " - " + mCodStatoNascita + " - " + mDescrStatoNascita + " - "
				+ mDescComuneNascitaEstero + " - " + mNazionalita + " - " + mPaternita + " - " + mCognomeMadre
				+ " - " + mNomeMadre + " - " + mSesso + " - " + mAttoNascita + " - " + mNote + " - "
				+ mCodComuneCasellario + " - " + mDescrComuneCasellario + " - " + mFlagPresenzaFascicolo
				+ " - " + mMeseNascita + " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - "
				+ mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mFasSieIdFascicoloSiep + " - "
				+ mIdSoggettoNuovo + " - " + mFasSieIdFascicoloSius + " - " + mEtaPresuntaAnni + " - "
				+ mEtaPresuntaMesi;

		return lStr;
	}

}