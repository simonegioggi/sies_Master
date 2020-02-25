package siap.siep.competenza.model;

/**
* <p>Title: CompetenzaModel</p>
* <p>Description: Classe Model che rappresenta il Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;
import java.math.BigDecimal;
import f3b.model.GenericModel;

public class CompetenzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5533913360983605360L;

	private BigDecimal mIdCompetenza;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mDataProvvedimento;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mNumSezioneAutoritaEmittente;
	private BigDecimal mAnnoSentenza;
	private String mNumeroSentenza;
	private Date mDataIrrevocabilita;
	private BigDecimal mSenIdSentenza;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mEveIdEvento;

	private BigDecimal mChiaveAnno;
	private String mChiaveUfficio;
	private BigDecimal mChiaveProgr;
	private String mFlagAccorpato;
	private String mChiaveUfficioOrigine;
	private BigDecimal mChiaveProgrOrigine;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	private String mCodTipoAutoritaComp;
	private String mCodLuogoAutoritaComp;
	private String mDescrTipoAutoritaComp;
	private String mDescrLuogoAutoritaComp;
	private String mCodUfficioAutoritaComp;

	private BigDecimal mIdMessaggioRichiesta;

	// Solo per i Record Competenza associati a Messaggi di: TRASFERIMENTO / RIGETTO / RESTITUZIONE Atti X
	// Competenza
	// Dati Relativi al titolo Richiesto
	private String mCodTipoProvvedimento_Rich;
	private String mDescrTipoProvvedimento_Rich;
	private Date mDataProvvedimento_Rich;
	private String mCodTipoAutoritaEmittente_Rich;
	private String mDescrTipoAutoritaEmittente_Rich;
	private String mCodLuogoEmittente_Rich;
	private String mDescrLuogoEmittente_Rich;
	private String mNumSezioneAutoritaEmittente_Rich;
	private BigDecimal mAnnoSentenza_Rich;
	private String mNumeroSentenza_Rich;
	private Date mDataIrrevocabilita_Rich;
	private String mCognome_Soggetto_Rich;
	private String mNome_Soggetto_Rich;
	private Date mDataNascita_Soggetto_Rich;
	private String mCodStatoNascita_Soggetto_Rich;
	private String mCodComuneNascita_Soggetto_Rich;
	private String mDescrComuneNascita_Soggetto_Rich;
	private String mSigla_Provincia_Soggetto_Rich;
	private String mCodiceCui_Soggetto_Rich;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public CompetenzaModel() {
		this.mIdCompetenza = null;
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mDataProvvedimento = null;
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mNumSezioneAutoritaEmittente = "";
		this.mAnnoSentenza = null;
		this.mNumeroSentenza = "";
		this.mDataIrrevocabilita = null;
		this.mSenIdSentenza = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mEveIdEvento = null;

		this.mChiaveAnno = null;
		this.mChiaveUfficio = "";
		this.mChiaveProgr = null;
		this.mFlagAccorpato = "";
		this.mChiaveUfficioOrigine = "";
		this.mChiaveProgrOrigine = null;

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";

		this.mCodTipoAutoritaComp = "";
		this.mCodLuogoAutoritaComp = "";
		this.mDescrTipoAutoritaComp = "";
		this.mDescrLuogoAutoritaComp = "";
		this.mCodUfficioAutoritaComp = "";

		this.mIdMessaggioRichiesta = null;

		// Dati Relativi al titolo Richiesto
		this.mCodTipoProvvedimento_Rich = "";
		this.mDescrTipoProvvedimento_Rich = "";
		this.mDataProvvedimento_Rich = null;
		this.mCodTipoAutoritaEmittente_Rich = "";
		this.mDescrTipoAutoritaEmittente_Rich = "";
		this.mCodLuogoEmittente_Rich = "";
		this.mDescrLuogoEmittente_Rich = "";
		this.mNumSezioneAutoritaEmittente_Rich = "";
		this.mAnnoSentenza_Rich = null;
		this.mNumeroSentenza_Rich = "";
		this.mDataIrrevocabilita_Rich = null;
		this.mCognome_Soggetto_Rich = "";
		this.mNome_Soggetto_Rich = "";
		this.mDataNascita_Soggetto_Rich = null;
		this.mCodStatoNascita_Soggetto_Rich = "";
		this.mCodComuneNascita_Soggetto_Rich = "";
		this.mDescrComuneNascita_Soggetto_Rich = "";
		this.mSigla_Provincia_Soggetto_Rich = "";
		this.mCodiceCui_Soggetto_Rich = "";

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public CompetenzaModel(CompetenzaModel aModel) {
		this.mIdCompetenza = aModel.mIdCompetenza;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mDataProvvedimento = aModel.mDataProvvedimento;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mNumSezioneAutoritaEmittente = aModel.mNumSezioneAutoritaEmittente;
		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumeroSentenza = aModel.mNumeroSentenza;
		this.mDataIrrevocabilita = aModel.mDataIrrevocabilita;
		this.mSenIdSentenza = aModel.mSenIdSentenza;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mEveIdEvento = aModel.mEveIdEvento;

		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveUfficio = aModel.mChiaveUfficio;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mFlagAccorpato = aModel.mFlagAccorpato;
		this.mChiaveUfficioOrigine = aModel.mChiaveUfficioOrigine;
		this.mChiaveProgrOrigine = aModel.mChiaveProgrOrigine;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mCodTipoAutoritaComp = aModel.mCodTipoAutoritaComp;
		this.mCodLuogoAutoritaComp = aModel.mCodLuogoAutoritaComp;
		this.mDescrTipoAutoritaComp = aModel.mDescrTipoAutoritaComp;
		this.mDescrLuogoAutoritaComp = aModel.mDescrLuogoAutoritaComp;
		this.mCodUfficioAutoritaComp = aModel.mCodUfficioAutoritaComp;
		this.mIdMessaggioRichiesta = aModel.mIdMessaggioRichiesta;

		// Dati Relativi al titolo Richiesto
		this.mCodTipoProvvedimento_Rich = aModel.mCodTipoProvvedimento_Rich;
		this.mDescrTipoProvvedimento_Rich = aModel.mDescrTipoProvvedimento_Rich;
		this.mDataProvvedimento_Rich = aModel.mDataProvvedimento_Rich;
		this.mCodTipoAutoritaEmittente_Rich = aModel.mCodTipoAutoritaEmittente_Rich;
		this.mDescrTipoAutoritaEmittente_Rich = aModel.mDescrTipoAutoritaEmittente_Rich;
		this.mCodLuogoEmittente_Rich = aModel.mCodLuogoEmittente_Rich;
		this.mDescrLuogoEmittente_Rich = aModel.mDescrLuogoEmittente_Rich;
		this.mNumSezioneAutoritaEmittente_Rich = aModel.mNumSezioneAutoritaEmittente_Rich;
		this.mAnnoSentenza_Rich = aModel.mAnnoSentenza_Rich;
		this.mNumeroSentenza_Rich = aModel.mNumeroSentenza_Rich;
		this.mDataIrrevocabilita_Rich = aModel.mDataIrrevocabilita_Rich;
		this.mCognome_Soggetto_Rich = aModel.mCognome_Soggetto_Rich;
		this.mNome_Soggetto_Rich = aModel.mNome_Soggetto_Rich;
		this.mDataNascita_Soggetto_Rich = aModel.mDataNascita_Soggetto_Rich;
		this.mCodStatoNascita_Soggetto_Rich = aModel.mCodStatoNascita_Soggetto_Rich;
		this.mCodComuneNascita_Soggetto_Rich = aModel.mCodComuneNascita_Soggetto_Rich;
		this.mDescrComuneNascita_Soggetto_Rich = aModel.mDescrComuneNascita_Soggetto_Rich;
		this.mSigla_Provincia_Soggetto_Rich = aModel.mSigla_Provincia_Soggetto_Rich;
		this.mCodiceCui_Soggetto_Rich = aModel.mCodiceCui_Soggetto_Rich;

	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public CompetenzaModel(BigDecimal aIdCompetenza, String aCodTipoProvvedimento,
			String aDescrTipoProvvedimento, Date aDataProvvedimento, String aCodTipoAutoritaEmittente,
			String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente, String aDescrLuogoEmittente,
			String aNumSezioneAutoritaEmittente, BigDecimal aAnnoSentenza, String aNumeroSentenza,
			Date aDataIrrevocabilita, BigDecimal aSenIdSentenza, BigDecimal aFasSieIdFascicoloSiep,
			BigDecimal aEveIdEvento,

			BigDecimal aChiaveAnno, String aChiaveUfficio, BigDecimal aChiaveProgr, String aFlagAccorpato,
			String aChiaveUfficioOrigine, BigDecimal aChiaveProgrOrigine,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,

			String aCodTipoAutoritaComp, String aCodLuogoAutoritaComp, String aDescrTipoAutoritaComp,
			String aDescrLuogoAutoritaComp, String aCodUfficioAutoritaComp, BigDecimal aIdMessaggioRichiesta,

			// Dati Relativi al titolo Richiesto
			String aCodTipoProvvedimento_Rich, String aDescrTipoProvvedimento_Rich,
			Date aDataProvvedimento_Rich, String aCodTipoAutoritaEmittente_Rich,
			String aDescrTipoAutoritaEmittente_Rich, String aCodLuogoEmittente_Rich,
			String aDescrLuogoEmittente_Rich, String aNumSezioneAutoritaEmittente_Rich,
			BigDecimal aAnnoSentenza_Rich, String aNumeroSentenza_Rich, Date aDataIrrevocabilita_Rich,
			String aCognome_Soggetto_Rich, String aNome_Soggetto_Rich, Date aDataNascita_Soggetto_Rich,
			String aCodStatoNascita_Soggetto_Rich, String aCodComuneNascita_Soggetto_Rich,
			String aDescrComuneNascita_Soggetto_Rich, String aSigla_Provincia_Soggetto_Rich,
			String aCodiceCui_Soggetto_Rich

	) {
		this.mIdCompetenza = aIdCompetenza;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mDataProvvedimento = aDataProvvedimento;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mNumSezioneAutoritaEmittente = aNumSezioneAutoritaEmittente;
		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumeroSentenza = aNumeroSentenza;
		this.mDataIrrevocabilita = aDataIrrevocabilita;
		this.mSenIdSentenza = aSenIdSentenza;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mEveIdEvento = aEveIdEvento;

		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveUfficio = aChiaveUfficio;
		this.mChiaveProgr = aChiaveProgr;
		this.mFlagAccorpato = aFlagAccorpato;
		this.mChiaveUfficioOrigine = aChiaveUfficioOrigine;
		this.mChiaveProgrOrigine = aChiaveProgrOrigine;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;

		this.mCodTipoAutoritaComp = aCodTipoAutoritaComp;
		this.mCodLuogoAutoritaComp = aCodLuogoAutoritaComp;
		this.mDescrTipoAutoritaComp = aDescrTipoAutoritaComp;
		this.mDescrLuogoAutoritaComp = aDescrLuogoAutoritaComp;
		this.mCodUfficioAutoritaComp = aCodUfficioAutoritaComp;
		this.mIdMessaggioRichiesta = aIdMessaggioRichiesta;

		// Dati Relativi al titolo Richiesto
		this.mCodTipoProvvedimento_Rich = aCodTipoProvvedimento_Rich;
		this.mDescrTipoProvvedimento_Rich = aDescrTipoProvvedimento_Rich;
		this.mDataProvvedimento_Rich = aDataProvvedimento_Rich;
		this.mCodTipoAutoritaEmittente_Rich = aCodTipoAutoritaEmittente_Rich;
		this.mDescrTipoAutoritaEmittente_Rich = aDescrTipoAutoritaEmittente_Rich;
		this.mCodLuogoEmittente_Rich = aCodLuogoEmittente_Rich;
		this.mDescrLuogoEmittente_Rich = aDescrLuogoEmittente_Rich;
		this.mNumSezioneAutoritaEmittente_Rich = aNumSezioneAutoritaEmittente_Rich;
		this.mAnnoSentenza_Rich = aAnnoSentenza_Rich;
		this.mNumeroSentenza_Rich = aNumeroSentenza_Rich;
		this.mDataIrrevocabilita_Rich = aDataIrrevocabilita_Rich;
		this.mCognome_Soggetto_Rich = aCognome_Soggetto_Rich;
		this.mNome_Soggetto_Rich = aNome_Soggetto_Rich;
		this.mDataNascita_Soggetto_Rich = aDataNascita_Soggetto_Rich;
		this.mCodStatoNascita_Soggetto_Rich = aCodStatoNascita_Soggetto_Rich;
		this.mCodComuneNascita_Soggetto_Rich = aCodComuneNascita_Soggetto_Rich;
		this.mDescrComuneNascita_Soggetto_Rich = aDescrComuneNascita_Soggetto_Rich;
		this.mSigla_Provincia_Soggetto_Rich = aSigla_Provincia_Soggetto_Rich;
		this.mCodiceCui_Soggetto_Rich = aCodiceCui_Soggetto_Rich;

	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdCompetenza() {
		return mIdCompetenza;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public Date getDataProvvedimento() {
		return mDataProvvedimento;
	}

	public String getCodTipoAutoritaEmittente() {
		return mCodTipoAutoritaEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public String getNumSezioneAutoritaEmittente() {
		return mNumSezioneAutoritaEmittente;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumeroSentenza() {
		return mNumeroSentenza;
	}

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilita;
	}

	public BigDecimal getSenIdSentenza() {
		return mSenIdSentenza;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public String getChiaveUfficio() {
		return mChiaveUfficio;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
	}

	public String getFlagAccorpato() {
		return mFlagAccorpato;
	}

	public String getChiaveUfficioOrigine() {
		return mChiaveUfficioOrigine;
	}

	public BigDecimal getChiaveProgrOrigine() {
		return mChiaveProgrOrigine;
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

	public String getCodTipoAutoritaComp() {
		return mCodTipoAutoritaComp;
	}

	public String getCodLuogoAutoritaComp() {
		return mCodLuogoAutoritaComp;
	}

	public String getDescrTipoAutoritaComp() {
		return mDescrTipoAutoritaComp;
	}

	public String getDescrLuogoAutoritaComp() {
		return mDescrLuogoAutoritaComp;
	}

	public String getCodUfficioAutoritaComp() {
		return mCodUfficioAutoritaComp;
	}

	public BigDecimal getIdMessaggioRichiesta() {
		return mIdMessaggioRichiesta;
	}

	// Dati Relativi al titolo Richiesto
	public String getCodTipoProvvedimento_Rich() {
		return mCodTipoProvvedimento_Rich;
	}

	public String getDescrTipoProvvedimento_Rich() {
		return mDescrTipoProvvedimento_Rich;
	}

	public Date getDataProvvedimento_Rich() {
		return mDataProvvedimento_Rich;
	}

	public String getCodTipoAutoritaEmittente_Rich() {
		return mCodTipoAutoritaEmittente_Rich;
	}

	public String getDescrTipoAutoritaEmittente_Rich() {
		return mDescrTipoAutoritaEmittente_Rich;
	}

	public String getCodLuogoEmittente_Rich() {
		return mCodLuogoEmittente_Rich;
	}

	public String getDescrLuogoEmittente_Rich() {
		return mDescrLuogoEmittente_Rich;
	}

	public String getNumSezioneAutoritaEmittente_Rich() {
		return mNumSezioneAutoritaEmittente_Rich;
	}

	public BigDecimal getAnnoSentenza_Rich() {
		return mAnnoSentenza_Rich;
	}

	public String getNumeroSentenza_Rich() {
		return mNumeroSentenza_Rich;
	}

	public Date getDataIrrevocabilita_Rich() {
		return mDataIrrevocabilita_Rich;
	}

	public String getCognome_Soggetto_Rich() {
		return mCognome_Soggetto_Rich;
	}

	public String getNome_Soggetto_Rich() {
		return mNome_Soggetto_Rich;
	}

	public Date getDataNascita_Soggetto_Rich() {
		return mDataNascita_Soggetto_Rich;
	}

	public String getCodStatoNascita_Soggetto_Rich() {
		return mCodStatoNascita_Soggetto_Rich;
	}

	public String getCodComuneNascita_Soggetto_Rich() {
		return mCodComuneNascita_Soggetto_Rich;
	}

	public String getDescrComuneNascita_Soggetto_Rich() {
		return mDescrComuneNascita_Soggetto_Rich;
	}

	public String getSigla_Provincia_Soggetto_Rich() {
		return mSigla_Provincia_Soggetto_Rich;
	}

	public String getCodiceCui_Soggetto_Rich() {
		return mCodiceCui_Soggetto_Rich;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdCompetenza(BigDecimal aValore) {
		mIdCompetenza = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setDataProvvedimento(Date aValore) {
		mDataProvvedimento = aValore;
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		mCodTipoAutoritaEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setNumSezioneAutoritaEmittente(String aValore) {
		mNumSezioneAutoritaEmittente = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumeroSentenza(String aValore) {
		mNumeroSentenza = aValore;
	}

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilita = aValore;
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveUfficio(String aValore) {
		mChiaveUfficio = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
	}

	public void setFlagAccorpato(String aValore) {
		mFlagAccorpato = aValore;
	}

	public void setChiaveUfficioOrigine(String aValore) {
		mChiaveUfficioOrigine = aValore;
	}

	public void setChiaveProgrOrigine(BigDecimal aValore) {
		mChiaveProgrOrigine = aValore;
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

	public void setCodTipoAutoritaComp(String aValore) {
		mCodTipoAutoritaComp = aValore;
	}

	public void setCodLuogoAutoritaComp(String aValore) {
		mCodLuogoAutoritaComp = aValore;
	}

	public void setDescrTipoAutoritaComp(String aValore) {
		mDescrTipoAutoritaComp = aValore;
	}

	public void setDescrLuogoAutoritaComp(String aValore) {
		mDescrLuogoAutoritaComp = aValore;
	}

	public void setCodUfficioAutoritaComp(String aValore) {
		mCodUfficioAutoritaComp = aValore;
	}

	public void setIdMessaggiorichiesta(BigDecimal aValore) {
		mIdMessaggioRichiesta = aValore;
	}

	// Dati Relativi al titolo Richiesto
	public void setCodTipoProvvedimento_Rich(String aValore) {
		mCodTipoProvvedimento_Rich = aValore;
	}

	public void setDescrTipoProvvedimento_Rich(String aValore) {
		mDescrTipoProvvedimento_Rich = aValore;
	}

	public void setDataProvvedimento_Rich(Date aValore) {
		mDataProvvedimento_Rich = aValore;
	}

	public void setCodTipoAutoritaEmittente_Rich(String aValore) {
		mCodTipoAutoritaEmittente_Rich = aValore;
	}

	public void setDescrTipoAutoritaEmittente_Rich(String aValore) {
		mDescrTipoAutoritaEmittente_Rich = aValore;
	}

	public void setCodLuogoEmittente_Rich(String aValore) {
		mCodLuogoEmittente_Rich = aValore;
	}

	public void setDescrLuogoEmittente_Rich(String aValore) {
		mDescrLuogoEmittente_Rich = aValore;
	}

	public void setNumSezioneAutoritaEmittente_Rich(String aValore) {
		mNumSezioneAutoritaEmittente_Rich = aValore;
	}

	public void setAnnoSentenza_Rich(BigDecimal aValore) {
		mAnnoSentenza_Rich = aValore;
	}

	public void setNumeroSentenza_Rich(String aValore) {
		mNumeroSentenza_Rich = aValore;
	}

	public void setDataIrrevocabilita_Rich(Date aValore) {
		mDataIrrevocabilita_Rich = aValore;
	}

	public void setCognome_Soggetto_Rich(String aValore) {
		mCognome_Soggetto_Rich = aValore;
	}

	public void setNome_Soggetto_Rich(String aValore) {
		mNome_Soggetto_Rich = aValore;
	}

	public void setDataNascita_Soggetto_Rich(Date aValore) {
		mDataNascita_Soggetto_Rich = aValore;
	}

	public void setCodStatoNascita_Soggetto_Rich(String aValore) {
		mCodStatoNascita_Soggetto_Rich = aValore;
	}

	public void setCodComuneNascita_Soggetto_Rich(String aValore) {
		mCodComuneNascita_Soggetto_Rich = aValore;
	}

	public void setDescrComuneNascita_Soggetto_Rich(String aValore) {
		mDescrComuneNascita_Soggetto_Rich = aValore;
	}

	public void setSigla_Provincia_Soggetto_Rich(String aValore) {
		mSigla_Provincia_Soggetto_Rich = aValore;
	}

	public void setCodiceCui_Soggetto_Rich(String aValore) {
		mCodiceCui_Soggetto_Rich = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "CompetenzaModel:\n" + "[ mIdCompetenza                = " + mIdCompetenza + " ]\n"
				+ "[ mCodTipoProvvedimento        = " + mCodTipoProvvedimento + " ]\n"
				+ "[ mDataProvvedimento           = " + mDataProvvedimento + " ]\n"
				+ "[ mCodTipoAutoritaEmittente    = " + mCodTipoAutoritaEmittente + " ]\n"
				+ "[ mCodLuogoEmittente           = " + mCodLuogoEmittente + " ]\n"
				+ "[ mNumSezioneAutoritaEmittente = " + mNumSezioneAutoritaEmittente + " ]\n"
				+ "[ mAnnoSentenza                = " + mAnnoSentenza + " ]\n"
				+ "[ mNumeroSentenza              = " + mNumeroSentenza + " ]\n"
				+ "[ mDataIrrevocabilita          = " + mDataIrrevocabilita + " ]\n"
				+ "[ mSenIdSentenza               = " + mSenIdSentenza + " ]\n"
				+ "[ mFasSieIdFascicoloSiep       = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mEveIdEvento                 = " + mEveIdEvento + " ]\n"
				+ "[ mChiaveAnno                  = " + mChiaveAnno + " ]\n"
				+ "[ mChiaveUfficio               = " + mChiaveUfficio + " ]\n"
				+ "[ mChiaveProgr                 = " + mChiaveProgr + " ]\n"
				+ "[ mFlagAccorpato               = " + mFlagAccorpato + " ]\n"
				+ "[ mChiaveUfficioOrigine        = " + mChiaveUfficioOrigine + " ]\n"
				+ "[ mChiaveProgrOrigine          = " + mChiaveProgrOrigine + " ]\n"
				+ "[ mCodOperatoreInserimento     = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento             = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento       = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento   = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento           = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento     = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mCodTipoAutoritaComp   	   = " + mCodTipoAutoritaComp + "]\n"
				+ "[ mCodLuogoAutoritaComp  	   = " + mCodLuogoAutoritaComp + "]\n"
				+ "[ mCodUfficioAutoritaComp 	   = " + mCodUfficioAutoritaComp + "]\n"
				+ "[ mIdMessaggioRichiesta 	   = " + mIdMessaggioRichiesta + "]\n" +

				"[ mCodTipoProvvedimento_Rich        = " + mCodTipoProvvedimento_Rich + " ]\n"
				+ "[ mDescrTipoProvvedimento_Rich      = " + mDescrTipoProvvedimento_Rich + " ]\n"
				+ "[ mDataProvvedimento_Rich           = " + mDataProvvedimento_Rich + " ]\n"
				+ "[ mCodTipoAutoritaEmittente_Rich    = " + mCodTipoAutoritaEmittente_Rich + " ]\n"
				+ "[ mDescrTipoAutoritaEmittente_Rich  = " + mDescrTipoAutoritaEmittente_Rich + " ]\n"
				+ "[ mCodLuogoEmittente_Rich           = " + mCodLuogoEmittente_Rich + " ]\n"
				+ "[ mDescrLuogoEmittente_Rich         = " + mDescrLuogoEmittente_Rich + " ]\n"
				+ "[ mNumSezioneAutoritaEmittente_Rich = " + mNumSezioneAutoritaEmittente_Rich + " ]\n"
				+ "[ mAnnoSentenza_Rich                = " + mAnnoSentenza_Rich + " ]\n"
				+ "[ mNumeroSentenza_Rich              = " + mNumeroSentenza_Rich + " ]\n"
				+ "[ mDataIrrevocabilita_Rich          = " + mDataIrrevocabilita_Rich + " ]\n" +

				"[ mNome_Soggetto_Rich         		= " + mNome_Soggetto_Rich + " ]\n"
				+ "[ mCognome_Soggetto_Rich 			= " + mCognome_Soggetto_Rich + " ]\n"
				+ "[ mDataNascita_Soggetto_Rich       = " + mDataNascita_Soggetto_Rich + " ]\n"
				+ "[ mCodStatoNascita_Soggetto_Rich   = " + mCodStatoNascita_Soggetto_Rich + " ]\n"
				+ "[ mCodComuneNascita_Soggetto_Rich  = " + mCodComuneNascita_Soggetto_Rich + " ]\n"
				+ "[ mDescrComuneNascita_Soggetto_Rich = " + mDescrComuneNascita_Soggetto_Rich + " ]\n"
				+ "[ mSigla_Provincia_Soggetto_Rich   = " + mSigla_Provincia_Soggetto_Rich + " ]\n"
				+ "[ mCodiceCui_Soggetto_Rich 	    = " + mCodiceCui_Soggetto_Rich + "]";

		return lStr;
	}

}