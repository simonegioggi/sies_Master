package siap.siep.modulocumulo.model;

/**
* <p>Title: MisuraCautelareCumuloModel</p>
* <p>Description: Classe Model che rappresenta il MisuraCautelareCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.misuracautelare.model.MisuraCautelareModel;

public class MisuraCautelareCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 4530737009629100156L;
	private BigDecimal mIdMisuraCautelareCumulo;
	private String mCodTipoMisura;
	private String mDescrTipoMisura;
	private Date mDataInizio;
	private Date mDataFine;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mGiorni;
	private String mFlagModificaManuale;

	private String mIstDetIdIstitutoDetenzione;
	private String mAltroLuogoDetenzione;
	private String mAutoritaCompetente;
	private String mDescrAutoritaCompetente;
	private String mAutoritaCompetenteSede;
	private String mDescrAutoritaCompetenteSede;
	private String mAutoritaCompetenteIndirizzo;

	private BigDecimal mTitIdTitoloCumulato;
	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mIdMisuraCautelareOrigine;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	// Campi per gestire in visualizzazione le Misure Continuative
	private boolean mIsInContinuazione = false; // Indica se la MC è in continuazione con altre misure
	private Integer mProgressivoContinuazione = 0;
	private BigDecimal mNumAnniContinuativi;
	private BigDecimal mNumMesiContinuativi;
	private BigDecimal mNumGiorniContinuativi;
	private boolean mIsAggregato = false;

	private String mIsPenaSospesa; // Indica se la pena è sospesa e quindi la MC non computabile

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public MisuraCautelareCumuloModel() {
		this.mIdMisuraCautelareCumulo = null;
		this.mCodTipoMisura = "";
		this.mDescrTipoMisura = "";
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mGiorni = null;
		this.mFlagModificaManuale = "";

		this.mIstDetIdIstitutoDetenzione = "";
		this.mAltroLuogoDetenzione = "";
		this.mAutoritaCompetente = "";
		this.mDescrAutoritaCompetente = "";
		this.mAutoritaCompetenteSede = "";
		this.mDescrAutoritaCompetenteSede = "";
		this.mAutoritaCompetenteIndirizzo = "";

		this.mTitIdTitoloCumulato = null;
		this.mFlagStato = "";
		this.mMotivoModifica = "";
		this.mIdMisuraCautelareOrigine = null;

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";

		//
		this.mIsInContinuazione = false;
		this.mProgressivoContinuazione = null;
		this.mNumAnniContinuativi = null;
		this.mNumMesiContinuativi = null;
		this.mNumGiorniContinuativi = null;
		this.mIsAggregato = false;

		this.mIsPenaSospesa = "";

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public MisuraCautelareCumuloModel(MisuraCautelareCumuloModel aModel) {
		this.mIdMisuraCautelareCumulo = aModel.mIdMisuraCautelareCumulo;
		this.mCodTipoMisura = aModel.mCodTipoMisura;
		this.mDescrTipoMisura = aModel.mDescrTipoMisura;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mGiorni = aModel.mGiorni;
		this.mFlagModificaManuale = aModel.mFlagModificaManuale;

		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mAltroLuogoDetenzione = aModel.mAltroLuogoDetenzione;
		this.mAutoritaCompetente = aModel.mAutoritaCompetente;
		this.mDescrAutoritaCompetente = aModel.mDescrAutoritaCompetente;
		this.mAutoritaCompetenteSede = aModel.mAutoritaCompetenteSede;
		this.mDescrAutoritaCompetenteSede = aModel.mDescrAutoritaCompetenteSede;
		this.mAutoritaCompetenteIndirizzo = aModel.mAutoritaCompetenteIndirizzo;

		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mIdMisuraCautelareOrigine = aModel.mIdMisuraCautelareOrigine;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;

		this.mIsInContinuazione = aModel.mIsInContinuazione;
		this.mProgressivoContinuazione = aModel.mProgressivoContinuazione;

		this.mIsPenaSospesa = aModel.mIsPenaSospesa;

	}

	/**
	 * Costruttore che istanzia il model Cumulo a partire dal Model MisuraCautelare per il ribaltamento dei
	 * dati originari nel cumulo
	 * 
	 * @param aModel
	 */
	public MisuraCautelareCumuloModel(MisuraCautelareModel aModel) {
		this.mCodTipoMisura = aModel.getCodTipoMisura();
		this.mDataInizio = aModel.getDataInizio();
		this.mDataFine = aModel.getDataFine();
		this.mNumAnni = aModel.getNumAnni();
		this.mNumMesi = aModel.getNumMesi();
		this.mNumGiorni = aModel.getNumGiorni();
		// this.mGiorni = aModel.getGiorni();
		// this.mFlagModificaManuale = aModel.getFlagModificaManuale();

		this.mIstDetIdIstitutoDetenzione = aModel.getIstDetIdIstitutoDetenzione();
		this.mAltroLuogoDetenzione = aModel.getAltroLuogoDetenzione();
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public MisuraCautelareCumuloModel(BigDecimal aIdMisuraCautelareCumulo, String aCodTipoMisura,
			String aDescrTipoMisura, Date aDataInizio, Date aDataFine, BigDecimal aNumAnni,
			BigDecimal aNumMesi, BigDecimal aNumGiorni, BigDecimal aGiorni, String aFlagModificaManuale,
			String aIstDetIdIstitutoDetenzione, String aAltroLuogoDetenzione, String aAutoritaCompetente,
			String aAutoritaCompetenteSede, String aAutoritaCompetenteIndirizzo,

			BigDecimal aTitIdTitoloCumulato, String aFlagStato, String aMotivoModifica,
			BigDecimal aIdMisuraCautelareOrigine, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdMisuraCautelareCumulo = aIdMisuraCautelareCumulo;
		this.mCodTipoMisura = aCodTipoMisura;
		this.mDescrTipoMisura = aDescrTipoMisura;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mGiorni = aGiorni;
		this.mFlagModificaManuale = aFlagModificaManuale;

		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mAltroLuogoDetenzione = aAltroLuogoDetenzione;
		this.mAutoritaCompetente = aAutoritaCompetente;
		this.mAutoritaCompetenteSede = aAutoritaCompetenteSede;
		this.mAutoritaCompetenteIndirizzo = aAutoritaCompetenteIndirizzo;

		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mIdMisuraCautelareOrigine = aIdMisuraCautelareOrigine;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdMisuraCautelareCumulo() {
		return mIdMisuraCautelareCumulo;
	}

	public String getCodTipoMisura() {
		return mCodTipoMisura;
	}

	public String getDescrTipoMisura() {
		return mDescrTipoMisura;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getNumAnni() {
		return mNumAnni;
	}

	public BigDecimal getNumMesi() {
		return mNumMesi;
	}

	public BigDecimal getNumGiorni() {
		return mNumGiorni;
	}

	public BigDecimal getGiorni() {
		return mGiorni;
	}

	public String getFlagModificaManuale() {
		return mFlagModificaManuale;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public String getAltroLuogoDetenzione() {
		return mAltroLuogoDetenzione;
	}

	public String getAutoritaCompetente() {
		return mAutoritaCompetente;
	}

	public String getDescrAutoritaCompetente() {
		return mDescrAutoritaCompetente;
	}

	public String getAutoritaCompetenteSede() {
		return mAutoritaCompetenteSede;
	}

	public String getDescrAutoritaCompetenteSede() {
		return mDescrAutoritaCompetenteSede;
	}

	public String getAutoritaCompetenteIndirizzo() {
		return mAutoritaCompetenteIndirizzo;
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

	public BigDecimal getIdMisuraCautelareOrigine() {
		return mIdMisuraCautelareOrigine;
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

	public boolean getIsInContinuazione() {
		return mIsInContinuazione;
	}

	public Integer getProgressivoContinuazione() {
		return mProgressivoContinuazione;
	}

	public BigDecimal getNumAnniContinuativi() {
		return mNumAnniContinuativi;
	}

	public BigDecimal getNumMesiContinuativi() {
		return mNumMesiContinuativi;
	}

	public BigDecimal getNumGiorniContinuativi() {
		return mNumGiorniContinuativi;
	}

	public boolean getIsAggregato() {
		return mIsAggregato;
	}

	public String getIsPenaSospesa() {
		return mIsPenaSospesa;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdMisuraCautelareCumulo(BigDecimal aValore) {
		mIdMisuraCautelareCumulo = aValore;
	}

	public void setCodTipoMisura(String aValore) {
		mCodTipoMisura = aValore;
	}

	public void setDescrTipoMisura(String aValore) {
		mDescrTipoMisura = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setNumAnni(BigDecimal aValore) {
		mNumAnni = aValore;
	}

	public void setNumMesi(BigDecimal aValore) {
		mNumMesi = aValore;
	}

	public void setNumGiorni(BigDecimal aValore) {
		mNumGiorni = aValore;
	}

	public void setGiorni(BigDecimal aValore) {
		mGiorni = aValore;
	}

	public void setFlagModificaManuale(String aValore) {
		mFlagModificaManuale = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setAltroLuogoDetenzione(String aValore) {
		mAltroLuogoDetenzione = aValore;
	}

	public void setAutoritaCompetente(String aValore) {
		mAutoritaCompetente = aValore;
	}

	public void setDescrAutoritaCompetente(String aValore) {
		mDescrAutoritaCompetente = aValore;
	}

	public void setAutoritaCompetenteSede(String aValore) {
		mAutoritaCompetenteSede = aValore;
	}

	public void setDescrAutoritaCompetenteSede(String aValore) {
		mDescrAutoritaCompetenteSede = aValore;
	}

	public void setAutoritaCompetenteIndirizzo(String aValore) {
		mAutoritaCompetenteIndirizzo = aValore;
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

	public void setIdMisuraCautelareOrigine(BigDecimal aValore) {
		mIdMisuraCautelareOrigine = aValore;
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

	public void setIsInContinuazione(boolean aValore) {
		mIsInContinuazione = aValore;
	}

	public void setProgressivoContinuazione(Integer aValore) {
		mProgressivoContinuazione = aValore;
	}

	public void setNumAnniContinuativi(BigDecimal aValore) {
		mNumAnniContinuativi = aValore;
	}

	public void setNumMesiContinuativi(BigDecimal aValore) {
		mNumMesiContinuativi = aValore;
	}

	public void setNumGiorniContinuativi(BigDecimal aValore) {
		mNumGiorniContinuativi = aValore;
	}

	public void setIsAggregato(boolean aValore) {
		mIsAggregato = aValore;
	}

	public void setIsPenaSospesa(String aValore) {
		mIsPenaSospesa = aValore;
	}

	/***
	 * Converte i giorni effettivamente espiati (lCMEspiato) nei corrispondenti giorni computabili nel caso di
	 * Messa Alla Prova, ovvero ogni 3 giorni espiati conta come un giorno di Misura Cautelare computabile. La
	 * DIV per 3 viene approssimata all'intero più vicino. n.b. per come vengono calcolati i giorniEspiati,
	 * ovvero 360*anni+30*mesi+giorni, la parte decimale da approssimare riguarda solo i giorni, essendo 360 e
	 * 30 divisibili per 3. I giorni vanno da 0 a 30 e la frazione decimale della divisione per 3 vale: 0,
	 * 0.33, 0.67. Quindi 0.33 viene approx a 0 e 0.67 a 1.
	 * 
	 * @param lCMEspiato
	 * @return
	 */
	public static CalendarModel getQuantumMessaAllaProva(CalendarModel lCMEspiato) {
		CalendarModel lCalendarMessaAllaProva = new CalendarModel();

		CalendarUtil lCalendarUtils = new CalendarUtil();

		// Per sicurezza normalizzo prima di calcolare i gg
		lCMEspiato = lCalendarUtils.ricalcolaGAM(lCMEspiato);

		int lTOTGiorni = 360 * lCMEspiato.getNumAnni() + 30 * lCMEspiato.getNumMesi()
				+ lCMEspiato.getNumGiorni();

		float lTotConvertiti = (float) lTOTGiorni / (float) 3;

		int lTotConvertitiRound = Math.round(lTotConvertiti);

		lCalendarMessaAllaProva.setNumGiorni(lTotConvertitiRound);

		lCalendarMessaAllaProva = lCalendarUtils.ricalcolaGAM(lCalendarMessaAllaProva);

		return lCalendarMessaAllaProva;
	}

	public CalendarModel getQuantumMisura() {
		CalendarModel lCalendarEspiato = new CalendarModel();

		lCalendarEspiato.setNumAnni(mNumAnni);
		lCalendarEspiato.setNumMesi(mNumMesi);
		lCalendarEspiato.setNumGiorni(mNumGiorni);

		return lCalendarEspiato;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "MisuraCautelareCumuloModel:\n" + "[ mIdMisuraCautelareCumulo     = "
				+ mIdMisuraCautelareCumulo + " ]\n" + "[ mCodTipoMisura               = " + mCodTipoMisura
				+ " ]\n" + "[ mDataInizio                  = " + mDataInizio + " ]\n"
				+ "[ mDataFine                    = " + mDataFine + " ]\n"
				+ "[ mNumAnni                     = " + mNumAnni + " ]\n"
				+ "[ mNumMesi                     = " + mNumMesi + " ]\n"
				+ "[ mNumGiorni                   = " + mNumGiorni + " ]\n"
				+ "[ mGiorni                      = " + mGiorni + " ]\n" + "[ mFlagModificaManuale         = "
				+ mFlagModificaManuale + " ]\n" + "[ mIstDetIdIstitutoDetenzione  = "
				+ mIstDetIdIstitutoDetenzione + " ]\n" + "[ mAltroLuogoDetenzione        = "
				+ mAltroLuogoDetenzione + " ]\n" + "[ mAutoritaCompetente          = " + mAutoritaCompetente
				+ " ]\n" + "[ mAutoritaCompetenteSede      = " + mAutoritaCompetenteSede + " ]\n"
				+ "[ mAutoritaCompetenteIndirizzo = " + mAutoritaCompetenteIndirizzo + " ]\n" +

				"[ mIsInContinuazione           = " + mIsInContinuazione + " ]\n"
				+ "[ mProgressivoContinuazione    = " + mProgressivoContinuazione + " ]\n" +

				"[ mTitIdTitoloCumulato         = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mFlagStato                   = " + mFlagStato + " ]\n"
				+ "[ mMotivoModifica              = " + mMotivoModifica + " ]\n"
				+ "[ mIdMisuraCautelareOrigine    = " + mIdMisuraCautelareOrigine + " ]\n"
				+ "[ mCodOperatoreInserimento     = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento             = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento       = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento   = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento           = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento     = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}