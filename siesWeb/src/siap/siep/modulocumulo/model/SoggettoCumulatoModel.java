package siap.siep.modulocumulo.model;

/**
* <p>Title: SoggettoCumulatoModel</p>
* <p>Description: Classe Model che rappresenta il SoggettoCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.soggetto.model.SoggettoModel;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

public class SoggettoCumulatoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3293552392584193878L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	private BigDecimal mIdSoggettoCumulato;
	private String mCognome;
	private String mNome;
	private String mSesso;
	private Date mDataNascita;
	private String mDataNascitaPresunta;
	private BigDecimal mAnnoNascita;
	private BigDecimal mMeseNascita;
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
	private String mCodFiscale;
	private String mAttoNascita;
	private String mCodAfis;
	private String mCodComuneCasellario;
	private String mDescrComuneCasellario;
	private String mNote;
	private BigDecimal mKeySoggNsc;

	private BigDecimal mTitIdTitoloCumulato;
	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mIdSoggettoOrigine;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;

	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mStringaSoggetto;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SoggettoCumulatoModel() {
		this.mIdSoggettoCumulato = null;
		this.mCognome = "";
		this.mNome = "";
		this.mSesso = "";
		this.mDataNascita = null;
		this.mDataNascitaPresunta = "";
		this.mAnnoNascita = null;
		this.mMeseNascita = null;
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
		this.mCodFiscale = "";
		this.mAttoNascita = "";
		this.mCodAfis = "";
		this.mCodComuneCasellario = "";
		this.mDescrComuneCasellario = "";
		this.mNote = "";
		this.mKeySoggNsc = null;

		this.mTitIdTitoloCumulato = null;
		this.mFlagStato = "";
		this.mMotivoModifica = "";
		this.mIdSoggettoOrigine = null;

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SoggettoCumulatoModel(SoggettoCumulatoModel aModel) {
		this.mIdSoggettoCumulato = aModel.mIdSoggettoCumulato;
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
		this.mSesso = aModel.mSesso;
		this.mDataNascita = aModel.mDataNascita;
		this.mDataNascitaPresunta = aModel.mDataNascitaPresunta;
		this.mAnnoNascita = aModel.mAnnoNascita;
		this.mMeseNascita = aModel.mMeseNascita;
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
		this.mCodFiscale = aModel.mCodFiscale;
		this.mAttoNascita = aModel.mAttoNascita;
		this.mCodAfis = aModel.mCodAfis;
		this.mCodComuneCasellario = aModel.mCodComuneCasellario;
		this.mDescrComuneCasellario = aModel.mDescrComuneCasellario;
		this.mNote = aModel.mNote;
		this.mKeySoggNsc = aModel.mKeySoggNsc;

		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mIdSoggettoOrigine = aModel.mIdSoggettoOrigine;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SoggettoCumulatoModel(BigDecimal aIdSoggettoCumulato, String aCognome, String aNome, String aSesso,
			Date aDataNascita, String aDataNascitaPresunta, BigDecimal aAnnoNascita, BigDecimal aMeseNascita,
			String aCodComuneNascita, String aDescrComuneNascita, String aCodProvinciaNascita,
			String aDescrProvinciaNascita, String aCodStatoNascita, String aDescrStatoNascita,
			String aDescComuneNascitaEstero, String aNazionalita, String aDescrNazionalita, String aPaternita,
			String aCognomeMadre, String aNomeMadre, String aCodFiscale, String aAttoNascita, String aCodAfis,
			String aCodComuneCasellario, String aDescrComuneCasellario, String aNote, BigDecimal aKeySoggNsc,

			BigDecimal aTitIdTitoloCumulato, String aFlagStato, String aMotivoModifica,
			BigDecimal aIdSoggettoOrigine,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdSoggettoCumulato = aIdSoggettoCumulato;
		this.mCognome = aCognome;
		this.mNome = aNome;
		this.mSesso = aSesso;
		this.mDataNascita = aDataNascita;
		this.mDataNascitaPresunta = aDataNascitaPresunta;
		this.mAnnoNascita = aAnnoNascita;
		this.mMeseNascita = aMeseNascita;
		this.mCodComuneNascita = aCodComuneNascita;
		this.mDescrComuneNascita = aDescrComuneNascita;
		this.mCodProvinciaNascita = aCodProvinciaNascita;
		this.mDescrProvinciaNascita = aDescrProvinciaNascita;
		this.mCodStatoNascita = aCodStatoNascita;
		this.mDescrStatoNascita = aDescrStatoNascita;
		this.mDescComuneNascitaEstero = aDescComuneNascitaEstero;
		this.mNazionalita = aNazionalita;
		this.mDescrNazionalita = aDescrNazionalita;
		this.mPaternita = aPaternita;
		this.mCognomeMadre = aCognomeMadre;
		this.mNomeMadre = aNomeMadre;
		this.mCodFiscale = aCodFiscale;
		this.mAttoNascita = aAttoNascita;
		this.mCodAfis = aCodAfis;
		this.mCodComuneCasellario = aCodComuneCasellario;
		this.mDescrComuneCasellario = aDescrComuneCasellario;
		this.mNote = aNote;
		this.mKeySoggNsc = aKeySoggNsc;

		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mIdSoggettoOrigine = aIdSoggettoOrigine;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	/**
	 * Costruttore di copia a partire da SoggettoModel
	 * 
	 * @param aSoggModel
	 */
	public SoggettoCumulatoModel(SoggettoModel aSoggModel) {
		this.mCognome = aSoggModel.getCognome();
		this.mNome = aSoggModel.getNome();
		this.mSesso = aSoggModel.getSesso();
		this.mDataNascita = aSoggModel.getDataNascita();
		this.mDataNascitaPresunta = aSoggModel.getDataNascitaPresunta();
		this.mAnnoNascita = aSoggModel.getAnnoNascita();
		this.mMeseNascita = aSoggModel.getMeseNascita();
		this.mCodComuneNascita = aSoggModel.getCodComuneNascita();
		this.mCodProvinciaNascita = aSoggModel.getCodProvinciaNascita();
		this.mCodStatoNascita = aSoggModel.getCodStatoNascita();
		this.mDescComuneNascitaEstero = aSoggModel.getDescComuneNascitaEstero();
		this.mNazionalita = aSoggModel.getNazionalita();
		this.mDescrNazionalita = aSoggModel.getDescrNazionalita();
		this.mPaternita = aSoggModel.getPaternita();
		this.mCognomeMadre = aSoggModel.getCognomeMadre();
		this.mNomeMadre = aSoggModel.getNomeMadre();
		this.mCodFiscale = aSoggModel.getCodFiscale();
		this.mAttoNascita = aSoggModel.getAttoNascita();
		this.mCodAfis = aSoggModel.getCodAfis();
		this.mCodComuneCasellario = aSoggModel.getCodComuneCasellario();
		this.mNote = aSoggModel.getNote();
		this.mKeySoggNsc = aSoggModel.getKeySoggNsc();

		this.mIdSoggettoOrigine = aSoggModel.getIdSoggetto();

	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdSoggettoCumulato() {
		return mIdSoggettoCumulato;
	}

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getSesso() {
		return mSesso;
	}

	public Date getDataNascita() {
		return mDataNascita;
	}

	public String getDataNascitaPresunta() {
		return mDataNascitaPresunta;
	}

	public BigDecimal getAnnoNascita() {
		return mAnnoNascita;
	}

	public BigDecimal getMeseNascita() {
		return mMeseNascita;
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

	public String getCodFiscale() {
		return mCodFiscale;
	}

	public String getAttoNascita() {
		return mAttoNascita;
	}

	public String getCodAfis() {
		return mCodAfis;
	}

	public String getCodComuneCasellario() {
		return mCodComuneCasellario;
	}

	public String getDescrComuneCasellario() {
		return mDescrComuneCasellario;
	}

	public String getNote() {
		return mNote;
	}

	public BigDecimal getKeySoggNsc() {
		return mKeySoggNsc;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getIdSoggettoOrigine() {
		return mIdSoggettoOrigine;
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

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getStringaSoggetto() {
		return mStringaSoggetto;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdSoggettoCumulato(BigDecimal aValore) {
		mIdSoggettoCumulato = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setSesso(String aValore) {
		mSesso = aValore;
	}

	public void setDataNascita(Date aValore) {
		mDataNascita = aValore;
	}

	public void setDataNascitaPresunta(String aValore) {
		mDataNascitaPresunta = aValore;
	}

	public void setAnnoNascita(BigDecimal aValore) {
		mAnnoNascita = aValore;
	}

	public void setMeseNascita(BigDecimal aValore) {
		mMeseNascita = aValore;
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

	public void setCodFiscale(String aValore) {
		mCodFiscale = aValore;
	}

	public void setAttoNascita(String aValore) {
		mAttoNascita = aValore;
	}

	public void setCodAfis(String aValore) {
		mCodAfis = aValore;
	}

	public void setCodComuneCasellario(String aValore) {
		mCodComuneCasellario = aValore;
	}

	public void setDescrComuneCasellario(String aValore) {
		mDescrComuneCasellario = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setKeySoggNsc(BigDecimal aValore) {
		mKeySoggNsc = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setIdSoggettoOrigine(BigDecimal aValore) {
		mIdSoggettoOrigine = aValore;
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

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setStringaSoggetto(String aValore) {
		mStringaSoggetto = aValore;
	}

	/**
	 * Confronta i dati del Soggetto con il SoggettoModel in input
	 * 
	 * @param aSoggetto
	 */
	public boolean isStessoSoggetto(SoggettoModel aSoggetto) {
		boolean isStessoSoggetto = true;

		if (mNome != null && aSoggetto.getNome() != null
				&& !mNome.toUpperCase().equals(aSoggetto.getNome().toUpperCase())) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"Nome differente: " + mNome.toUpperCase() + " vs " + aSoggetto.getNome().toUpperCase());
			isStessoSoggetto = false;
		}

		if (mCognome != null && aSoggetto.getCognome() != null
				&& !mCognome.toUpperCase().equals(aSoggetto.getCognome().toUpperCase())) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cognome differente: " + mCognome.toUpperCase() + " vs "
					+ aSoggetto.getCognome().toUpperCase());
			isStessoSoggetto = false;
		}

		if (mDataNascita != null && aSoggetto.getDataNascita() != null
				&& !DateUtils.isEquals(mDataNascita, aSoggetto.getDataNascita())) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("DataNascita differente: " + DateUtils.getDateToString(mDataNascita, "dd-MM-yyyy")
							+ " vs " + DateUtils.getDateToString(aSoggetto.getDataNascita(), "dd-MM-yyyy"));
			isStessoSoggetto = false;
		}

		if (mCodStatoNascita != null && aSoggetto.getCodStatoNascita() != null
				&& !mCodStatoNascita.equals(aSoggetto.getCodStatoNascita())) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("CodStatoNascita differente: " + mCodStatoNascita.toUpperCase() + " vs "
					+ aSoggetto.getCodStatoNascita().toUpperCase());
			isStessoSoggetto = false;
		}

		if (mCodComuneNascita != null && aSoggetto.getCodComuneNascita() != null
				&& !mCodComuneNascita.equals(aSoggetto.getCodComuneNascita())) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("CodComuneNascita differente: " + mCodComuneNascita.toUpperCase() + " vs "
					+ aSoggetto.getCodComuneNascita().toUpperCase());
			isStessoSoggetto = false;
		}

		if (mCodAfis != null && aSoggetto.getCodAfis() != null && !mCodAfis.equals("")
				&& !aSoggetto.getCodAfis().equals("") && !mCodAfis.equals(aSoggetto.getCodAfis())) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("CodCUI differente: " + mCodAfis.toUpperCase() + " vs "
					+ aSoggetto.getCodAfis().toUpperCase());
			isStessoSoggetto = false;
		}

		return isStessoSoggetto;
	}

	public String calcolaStringaSoggetto() {
		String lStringa = "";

		String lNato = "M".equals(mSesso) ? "nato" : "nata";

		lStringa += mCognome + " " + mNome;
		lStringa += ", " + lNato;
		if ("039".equals(mCodStatoNascita)) {
			lStringa += " a " + mDescrComuneNascita;
		} else {
			if (mDescComuneNascitaEstero != null && !mDescComuneNascitaEstero.equals(""))
				lStringa += " a " + mDescComuneNascitaEstero + " (" + mDescrStatoNascita + ")";
			else
				lStringa += " in " + mDescrStatoNascita;
		}

		lStringa += " il " + DateUtils.getDateToString(mDataNascita, "dd-MM-yyyy");

		lStringa += " (C.U.I. " + StringUtils.toStringJSP(mCodAfis, "") + ")";

		mStringaSoggetto = lStringa;
		return mStringaSoggetto;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SoggettoCumulatoModel:\n" + "[ mIdSoggettoCumulato        = " + mIdSoggettoCumulato + " ]\n"
				+ "[ mCognome                   = " + mCognome + " ]\n" + "[ mNome                      = "
				+ mNome + " ]\n" + "[ mSesso                     = " + mSesso + " ]\n"
				+ "[ mDataNascita               = " + mDataNascita + " ]\n"
				+ "[ mDataNascitaPresunta       = " + mDataNascitaPresunta + " ]\n"
				+ "[ mAnnoNascita               = " + mAnnoNascita + " ]\n"
				+ "[ mMeseNascita               = " + mMeseNascita + " ]\n"
				+ "[ mCodComuneNascita          = " + mCodComuneNascita + " ]\n"
				+ "[ mCodProvinciaNascita       = " + mCodProvinciaNascita + " ]\n"
				+ "[ mCodStatoNascita           = " + mCodStatoNascita + " ]\n"
				+ "[ mDescComuneNascitaEstero   = " + mDescComuneNascitaEstero + " ]\n"
				+ "[ mNazionalita               = " + mNazionalita + " ]\n"
				+ "[ mDescrNazionalita		     = " + mDescrNazionalita + " ]\n"
				+ "[ mPaternita                 = " + mPaternita + " ]\n" + "[ mCognomeMadre              = "
				+ mCognomeMadre + " ]\n" + "[ mNomeMadre                 = " + mNomeMadre + " ]\n"
				+ "[ mCodFiscale                = " + mCodFiscale + " ]\n" + "[ mAttoNascita               = "
				+ mAttoNascita + " ]\n" + "[ mCodAfis                   = " + mCodAfis + " ]\n"
				+ "[ mCodComuneCasellario       = " + mCodComuneCasellario + " ]\n"
				+ "[ mNote                      = " + mNote + " ]\n" + "[ mKeySoggNsc                = "
				+ mKeySoggNsc + " ]\n" +

				"[ mTitIdTitoloCumulato       = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mFlagStato                 = " + mFlagStato + " ]\n" + "[ mMotivoModifica            = "
				+ mMotivoModifica + " ]\n" + "[ mIdSoggettoOrigine         = " + mIdSoggettoOrigine + " ]\n" +

				"[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}