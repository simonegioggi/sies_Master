package siap.siep.modulocumulo.model;

import java.math.BigDecimal;

/**
* <p>Title: ProvvedimentoGeSorvCumModel</p>
* <p>Description: Classe Model che rappresenta il ProvvedimentoGeSorvCum</p>
* <p>Author: Intersistemi S.p.A.</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.StringUtils;
import siap.sico.calendar.model.CalendarModel;

public class ProvvedimentoGeSorvCumModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 4368745830952585995L;
	private BigDecimal mIdProvvedimentoGeSorvCum;
	private String mCodUfficioEmittente;
	private String mDescrUfficioEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private Date mDataD;
	private BigDecimal mAnnoProvv;
	private String mNumeroProvv;
	private String mFlagConforme;
	private String mFlagPiuMenoD;
	private BigDecimal mNumAnniReclusioneD;
	private BigDecimal mNumMesiReclusioneD;
	private BigDecimal mNumGiorniReclusioneD;
	private BigDecimal mImportoMultaD;
	private BigDecimal mNumAnniArrestoD;
	private BigDecimal mNumMesiArrestoD;
	private BigDecimal mNumGiorniArrestoD;
	private BigDecimal mImportoAmmendaD;

	private BigDecimal mNumGiorniRevocaLaD;
	private BigDecimal mNumGiorniRevocaLsD;
	private BigDecimal mNumGiorniRevocaLiD;

	private String mMotivazioniD;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private BigDecimal mAnnoSIUS;
	private String mNumeroSIUS;
	private String mCodTipoMsD;
	private String mDescrTipoMsD;
	private BigDecimal mNumAnniMsD;
	private BigDecimal mNumMesiMsD;
	private BigDecimal mNumGiorniMsD;
	private String mBenSospCond;
	private String mBenNonMenzione;
	private String mBenIndulto;

	private String mCodTipoPenaAccessoriaD;
	private String mDescrTipoPenaAccessoriaD;
	private String mCodTipoDurataPaD;
	private String mDescrTipoDurataPaD;
	private BigDecimal mNumAnniPaD;
	private BigDecimal mNumMesiPaD;
	private BigDecimal mNumGiorniPaD;
	private Date mDataRevoca;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	// Relazioni
	private BigDecimal mRicIdRichiestePmInCumulo;
	
	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public ProvvedimentoGeSorvCumModel() {
		this.mIdProvvedimentoGeSorvCum = null;
		this.mCodUfficioEmittente = "";
		this.mDescrUfficioEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mDataD = null;
		this.mAnnoProvv = null;
		this.mNumeroProvv = "";
		this.mFlagConforme = "";
		this.mFlagPiuMenoD = "";
		this.mNumAnniReclusioneD = null;
		this.mNumMesiReclusioneD = null;
		this.mNumGiorniReclusioneD = null;
		this.mImportoMultaD = null;
		this.mNumAnniArrestoD = null;
		this.mNumMesiArrestoD = null;
		this.mNumGiorniArrestoD = null;
		this.mImportoAmmendaD = null;
		this.mNumGiorniRevocaLaD = null;
		this.mNumGiorniRevocaLsD = null;
		this.mNumGiorniRevocaLiD = null;
		this.mMotivazioniD = "";
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mAnnoSIUS = null;
		this.mNumeroSIUS = "";
		this.mCodTipoMsD = "";
		this.mDescrTipoMsD = "";
		this.mNumAnniMsD = null;
		this.mNumMesiMsD = null;
		this.mNumGiorniMsD = null;
		this.mBenSospCond = "";
		this.mBenNonMenzione = "";
		this.mBenIndulto = "";

		this.mCodTipoPenaAccessoriaD = "";
		this.mDescrTipoPenaAccessoriaD = "";
		this.mCodTipoDurataPaD = "";
		this.mDescrTipoDurataPaD = "";
		this.mNumAnniPaD = null;
		this.mNumMesiPaD = null;
		this.mNumGiorniPaD = null;
		this.mDataRevoca = null;

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mRicIdRichiestePmInCumulo = null;
	}

	/*******************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 *******************************************************************************/
	public ProvvedimentoGeSorvCumModel(ProvvedimentoGeSorvCumModel aModel) {
		this.mIdProvvedimentoGeSorvCum = aModel.mIdProvvedimentoGeSorvCum;
		this.mCodUfficioEmittente = aModel.mCodUfficioEmittente;
		this.mDescrUfficioEmittente = aModel.mDescrUfficioEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mDataD = aModel.mDataD;
		this.mAnnoProvv = aModel.mAnnoProvv;
		this.mNumeroProvv = aModel.mNumeroProvv;
		this.mFlagConforme = aModel.mFlagConforme;
		this.mFlagPiuMenoD = aModel.mFlagPiuMenoD;
		this.mNumAnniReclusioneD = aModel.mNumAnniReclusioneD;
		this.mNumMesiReclusioneD = aModel.mNumMesiReclusioneD;
		this.mNumGiorniReclusioneD = aModel.mNumGiorniReclusioneD;
		this.mImportoMultaD = aModel.mImportoMultaD;
		this.mNumAnniArrestoD = aModel.mNumAnniArrestoD;
		this.mNumMesiArrestoD = aModel.mNumMesiArrestoD;
		this.mNumGiorniArrestoD = aModel.mNumGiorniArrestoD;
		this.mImportoAmmendaD = aModel.mImportoAmmendaD;
		this.mNumGiorniRevocaLaD = aModel.mNumGiorniRevocaLaD;
		this.mNumGiorniRevocaLsD = aModel.mNumGiorniRevocaLsD;
		this.mNumGiorniRevocaLiD = aModel.mNumGiorniRevocaLiD;
		this.mMotivazioniD = aModel.mMotivazioniD;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mAnnoSIUS = aModel.mAnnoSIUS;
		this.mNumeroSIUS = aModel.mNumeroSIUS;
		this.mCodTipoMsD = aModel.mCodTipoMsD;
		this.mDescrTipoMsD = aModel.mDescrTipoMsD;
		this.mNumAnniMsD = aModel.mNumAnniMsD;
		this.mNumMesiMsD = aModel.mNumMesiMsD;
		this.mNumGiorniMsD = aModel.mNumGiorniMsD;
		this.mBenSospCond = aModel.mBenSospCond;
		this.mBenNonMenzione = aModel.mBenNonMenzione;
		this.mBenIndulto = aModel.mBenIndulto;

		this.mCodTipoPenaAccessoriaD = aModel.mCodTipoPenaAccessoriaD;
		this.mDescrTipoPenaAccessoriaD = aModel.mDescrTipoPenaAccessoriaD;
		this.mCodTipoDurataPaD = aModel.mCodTipoDurataPaD;
		this.mDescrTipoDurataPaD = aModel.mDescrTipoDurataPaD;
		this.mNumAnniPaD = aModel.mNumAnniPaD;
		this.mNumMesiPaD = aModel.mNumMesiPaD;
		this.mNumGiorniPaD = aModel.mNumGiorniPaD;
		this.mDataRevoca = aModel.mDataRevoca;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mRicIdRichiestePmInCumulo = aModel.mRicIdRichiestePmInCumulo;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public ProvvedimentoGeSorvCumModel(BigDecimal aIdProvvedimentoGeSorvCum, String aCodUfficioEmittente,
			String aDescrUfficioEmittente, String aCodLuogoEmittente, String aDescrLuogoEmittente,
			Date aDataD, BigDecimal aAnnoProvv, String aNumeroProvv, String aFlagConforme,
			String aFlagPiuMenoD, BigDecimal aNumAnniReclusioneD, BigDecimal aNumMesiReclusioneD,
			BigDecimal aNumGiorniReclusioneD, BigDecimal aImportoMultaD, BigDecimal aNumAnniArrestoD,
			BigDecimal aNumMesiArrestoD, BigDecimal aNumGiorniArrestoD, BigDecimal aImportoAmmendaD,
			BigDecimal aNumGiorniRevocaLaD, BigDecimal aNumGiorniRevocaLsD, BigDecimal aNumGiorniRevocaLiD,
			String aMotivazioniD, String aCodTipoProvvedimento, String aDescrTipoProvvedimento,
			BigDecimal aAnnoSIUS, String aNumeroSIUS, String aCodTipoMsD, String aDescrTipoMsD,
			BigDecimal aNumAnniMsD, BigDecimal aNumMesiMsD, BigDecimal aNumGiorniMsD, String aBenSospCond,
			String aBenNonMenzione, String aBenIndulto,

			String aCodTipoPenaAccessoriaD, String aDescrTipoPenaAccessoriaD, String aCodTipoDurataPaD,
			String aDescrTipoDurataPaD, BigDecimal aNumAnniPaD, BigDecimal aNumMesiPaD,
			BigDecimal aNumGiorniPaD, Date aDataRevoca,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			BigDecimal aRicIdRichiestePmInCumulo) {
		this.mIdProvvedimentoGeSorvCum = aIdProvvedimentoGeSorvCum;
		this.mCodUfficioEmittente = aCodUfficioEmittente;
		this.mDescrUfficioEmittente = aDescrUfficioEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mDataD = aDataD;
		this.mAnnoProvv = aAnnoProvv;
		this.mNumeroProvv = aNumeroProvv;
		this.mFlagConforme = aFlagConforme;
		this.mFlagPiuMenoD = aFlagPiuMenoD;
		this.mNumAnniReclusioneD = aNumAnniReclusioneD;
		this.mNumMesiReclusioneD = aNumMesiReclusioneD;
		this.mNumGiorniReclusioneD = aNumGiorniReclusioneD;
		this.mImportoMultaD = aImportoMultaD;
		this.mNumAnniArrestoD = aNumAnniArrestoD;
		this.mNumMesiArrestoD = aNumMesiArrestoD;
		this.mNumGiorniArrestoD = aNumGiorniArrestoD;
		this.mImportoAmmendaD = aImportoAmmendaD;
		this.mNumGiorniRevocaLaD = aNumGiorniRevocaLaD;
		this.mNumGiorniRevocaLsD = aNumGiorniRevocaLsD;
		this.mNumGiorniRevocaLiD = aNumGiorniRevocaLiD;
		this.mMotivazioniD = aMotivazioniD;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mAnnoSIUS = aAnnoSIUS;
		this.mNumeroSIUS = aNumeroSIUS;
		this.mCodTipoMsD = aCodTipoMsD;
		this.mDescrTipoMsD = aDescrTipoMsD;
		this.mNumAnniMsD = aNumAnniMsD;
		this.mNumMesiMsD = aNumMesiMsD;
		this.mNumGiorniMsD = aNumGiorniMsD;
		this.mBenSospCond = aBenSospCond;
		this.mBenNonMenzione = aBenNonMenzione;
		this.mBenIndulto = aBenIndulto;

		this.mCodTipoPenaAccessoriaD = aCodTipoPenaAccessoriaD;
		this.mDescrTipoPenaAccessoriaD = aDescrTipoPenaAccessoriaD;
		this.mCodTipoDurataPaD = aCodTipoDurataPaD;
		this.mDescrTipoDurataPaD = aDescrTipoDurataPaD;
		this.mNumAnniPaD = aNumAnniPaD;
		this.mNumMesiPaD = aNumMesiPaD;
		this.mNumGiorniPaD = aNumGiorniPaD;
		this.mDataRevoca = aDataRevoca;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mRicIdRichiestePmInCumulo = aRicIdRichiestePmInCumulo;
	}

	// ===================================================================================
	// METODI GET()
	// ===================================================================================
	public BigDecimal getIdProvvedimentoGeSorvCum() {
		return mIdProvvedimentoGeSorvCum;
	}

	public String getCodUfficioEmittente() {
		return mCodUfficioEmittente;
	}

	public String getDescrUfficioEmittente() {
		return mDescrUfficioEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public Date getDataD() {
		return mDataD;
	}

	public BigDecimal getAnnoProvv() {
		return mAnnoProvv;
	}

	public String getNumeroProvv() {
		return mNumeroProvv;
	}

	public String getFlagConforme() {
		return mFlagConforme;
	}

	public String getFlagPiuMenoD() {
		return mFlagPiuMenoD;
	}

	public BigDecimal getNumAnniReclusioneD() {
		return mNumAnniReclusioneD;
	}

	public BigDecimal getNumMesiReclusioneD() {
		return mNumMesiReclusioneD;
	}

	public BigDecimal getNumGiorniReclusioneD() {
		return mNumGiorniReclusioneD;
	}

	public BigDecimal getImportoMultaD() {
		return mImportoMultaD;
	}

	public BigDecimal getNumAnniArrestoD() {
		return mNumAnniArrestoD;
	}

	public BigDecimal getNumMesiArrestoD() {
		return mNumMesiArrestoD;
	}

	public BigDecimal getNumGiorniArrestoD() {
		return mNumGiorniArrestoD;
	}

	public BigDecimal getImportoAmmendaD() {
		return mImportoAmmendaD;
	}

	public BigDecimal getNumGiorniRevocaLaD() {
		return mNumGiorniRevocaLaD;
	}

	public BigDecimal getNumGiorniRevocaLsD() {
		return mNumGiorniRevocaLsD;
	}

	public BigDecimal getNumGiorniRevocaLiD() {
		return mNumGiorniRevocaLiD;
	}

	public String getMotivazioniD() {
		return mMotivazioniD;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public BigDecimal getAnnoSIUS() {
		return mAnnoSIUS;
	}

	public String getNumeroSIUS() {
		return mNumeroSIUS;
	}

	public String getCodTipoMsD() {
		return mCodTipoMsD;
	}

	public String getDescrTipoMsD() {
		return mDescrTipoMsD;
	}

	public BigDecimal getNumAnniMsD() {
		return mNumAnniMsD;
	}

	public BigDecimal getNumMesiMsD() {
		return mNumMesiMsD;
	}

	public BigDecimal getNumGiorniMsD() {
		return mNumGiorniMsD;
	}

	public String getBenSospCond() {
		return mBenSospCond;
	}

	public String getBenNonMenzione() {
		return mBenNonMenzione;
	}

	public String getBenIndulto() {
		return mBenIndulto;
	}

	public String getCodTipoPenaAccessoriaD() {
		return mCodTipoPenaAccessoriaD;
	}

	public String getDescrTipoPenaAccessoriaD() {
		return mDescrTipoPenaAccessoriaD;
	}

	public String getCodTipoDurataPaD() {
		return mCodTipoDurataPaD;
	}

	public String getDescrTipoDurataPaD() {
		return mDescrTipoDurataPaD;
	}

	public BigDecimal getNumAnniPaD() {
		return mNumAnniPaD;
	}

	public BigDecimal getNumMesiPaD() {
		return mNumMesiPaD;
	}

	public BigDecimal getNumGiorniPaD() {
		return mNumGiorniPaD;
	}

	public Date getDataRevoca() {
		return mDataRevoca;
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

	public BigDecimal getRicIdRichiestePmInCumulo() {
		return mRicIdRichiestePmInCumulo;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdProvvedimentoGeSorvCum(BigDecimal aValore) {
		mIdProvvedimentoGeSorvCum = aValore;
	}

	public void setCodUfficioEmittente(String aValore) {
		mCodUfficioEmittente = aValore;
	}

	public void setDescrUfficioEmittente(String aValore) {
		mDescrUfficioEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setDataD(Date aValore) {
		mDataD = aValore;
	}

	public void setAnnoProvv(BigDecimal aValore) {
		mAnnoProvv = aValore;
	}

	public void setNumeroProvv(String aValore) {
		mNumeroProvv = aValore;
	}

	public void setFlagConforme(String aValore) {
		mFlagConforme = aValore;
	}

	public void setFlagPiuMenoD(String aValore) {
		mFlagPiuMenoD = aValore;
	}

	public void setNumAnniReclusioneD(BigDecimal aValore) {
		mNumAnniReclusioneD = aValore;
	}

	public void setNumMesiReclusioneD(BigDecimal aValore) {
		mNumMesiReclusioneD = aValore;
	}

	public void setNumGiorniReclusioneD(BigDecimal aValore) {
		mNumGiorniReclusioneD = aValore;
	}

	public void setImportoMultaD(BigDecimal aValore) {
		mImportoMultaD = aValore;
	}

	public void setNumAnniArrestoD(BigDecimal aValore) {
		mNumAnniArrestoD = aValore;
	}

	public void setNumMesiArrestoD(BigDecimal aValore) {
		mNumMesiArrestoD = aValore;
	}

	public void setNumGiorniArrestoD(BigDecimal aValore) {
		mNumGiorniArrestoD = aValore;
	}

	public void setImportoAmmendaD(BigDecimal aValore) {
		mImportoAmmendaD = aValore;
	}

	public void setNumGiorniRevocaLaD(BigDecimal aValore) {
		mNumGiorniRevocaLaD = aValore;
	}

	public void setNumGiorniRevocaLsD(BigDecimal aValore) {
		mNumGiorniRevocaLsD = aValore;
	}

	public void setNumGiorniRevocaLiD(BigDecimal aValore) {
		mNumGiorniRevocaLiD = aValore;
	}

	public void setMotivazioniD(String aValore) {
		mMotivazioniD = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setAnnoSIUS(BigDecimal aValore) {
		mAnnoSIUS = aValore;
	}

	public void setNumeroSIUS(String aValore) {
		mNumeroSIUS = aValore;
	}

	public void setCodTipoMsD(String aValore) {
		mCodTipoMsD = aValore;
	}

	public void setDescrTipoMsD(String aValore) {
		mDescrTipoMsD = aValore;
	}

	public void setNumAnniMsD(BigDecimal aValore) {
		mNumAnniMsD = aValore;
	}

	public void setNumMesiMsD(BigDecimal aValore) {
		mNumMesiMsD = aValore;
	}

	public void setNumGiorniMsD(BigDecimal aValore) {
		mNumGiorniMsD = aValore;
	}

	public void setBenSospCond(String aValore) {
		mBenSospCond = aValore;
	}

	public void setBenNonMenzione(String aValore) {
		mBenNonMenzione = aValore;
	}

	public void setBenIndulto(String aValore) {
		mBenIndulto = aValore;
	}

	public void setCodTipoPenaAccessoriaD(String aValore) {
		mCodTipoPenaAccessoriaD = aValore;
	}

	public void setDescrTipoPenaAccessoriaD(String aValore) {
		mDescrTipoPenaAccessoriaD = aValore;
	}

	public void setCodTipoDurataPaD(String aValore) {
		mCodTipoDurataPaD = aValore;
	}

	public void setDescrTipoDurataPaD(String aValore) {
		mDescrTipoDurataPaD = aValore;
	}

	public void setNumAnniPaD(BigDecimal aValore) {
		mNumAnniPaD = aValore;
	}

	public void setNumMesiPaD(BigDecimal aValore) {
		mNumMesiPaD = aValore;
	}

	public void setNumGiorniPaD(BigDecimal aValore) {
		mNumGiorniPaD = aValore;
	}

	public void setDataRevoca(Date aValore) {
		mDataRevoca = aValore;
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

	public void setRicIdRichiestePmInCumulo(BigDecimal aValore) {
		mRicIdRichiestePmInCumulo = aValore;
	}

	public CalendarModel getQuantumReclusione() {
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumGiorni(this.mNumGiorniReclusioneD);
		lCalReclusione.setNumMesi(this.mNumMesiReclusioneD);
		lCalReclusione.setNumAnni(this.mNumAnniReclusioneD);

		if (mImportoMultaD != null)
			lCalReclusione.setImportoMulta(this.mImportoMultaD.doubleValue());

		return lCalReclusione;
	}

	/**
	 * 
	 */
	public CalendarModel getQuantumArresto() {
		CalendarModel lCalArresto = new CalendarModel();

		lCalArresto.setNumGiorni(this.mNumGiorniArrestoD);
		lCalArresto.setNumMesi(this.mNumMesiArrestoD);
		lCalArresto.setNumAnni(this.mNumAnniArrestoD);

		if (mImportoAmmendaD != null)
			lCalArresto.setImportoAmmenda(this.mImportoAmmendaD.doubleValue());

		return lCalArresto;
	}
	
	// MEV_2025-48 - ALTRO 
	public String getStringaQuantumXStampa() {
        String lStringaQuantumXStampa = "";

        String lStringReclusione = "";
        if (mNumAnniReclusioneD != null && mNumAnniReclusioneD.intValue() != 0)
            lStringReclusione = "Anni " + mNumAnniReclusioneD;

        if (mNumMesiReclusioneD != null && mNumMesiReclusioneD.intValue() != 0)
            lStringReclusione += " Mesi " + mNumMesiReclusioneD;

        if (mNumGiorniReclusioneD != null && mNumGiorniReclusioneD.intValue() != 0)
            lStringReclusione += " Giorni " + mNumGiorniReclusioneD;

        lStringReclusione = lStringReclusione.trim();
        

        if (lStringReclusione != null)
            lStringaQuantumXStampa += lStringReclusione+" Reclusione ";

        if (mImportoMultaD != null && mImportoMultaD.intValue() > 0)
            lStringaQuantumXStampa += " Euro " + StringUtils.toEuroFormat(mImportoMultaD)+" Multa";

        // ARRESTI
        String lStringArresto = "";
        if (mNumAnniArrestoD != null && mNumAnniArrestoD.intValue() != 0)
            lStringArresto = " Anni " + mNumAnniArrestoD;

        if (mNumMesiArrestoD != null && mNumMesiArrestoD.intValue() != 0)
            lStringArresto += " Mesi " + mNumMesiArrestoD;

        if (mNumGiorniArrestoD != null && mNumGiorniArrestoD.intValue() != 0)
            lStringArresto += " Giorni " + mNumGiorniArrestoD;

        if (lStringArresto != null)
            lStringaQuantumXStampa += lStringArresto+" Arresto ";

        if (mImportoAmmendaD != null && mImportoAmmendaD.intValue() > 0)
            lStringaQuantumXStampa += " Euro " + StringUtils.toEuroFormat(mImportoAmmendaD)+" Ammenda";
        

        lStringaQuantumXStampa = lStringaQuantumXStampa.trim();	    
	    
	    
	    return lStringaQuantumXStampa;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "ProvvedimentoGeSorvCumModel:\n" + "[ mIdProvvedimentoGeSorvCum     = "
				+ mIdProvvedimentoGeSorvCum + " ]\n" + "[ mCodUfficioEmittente          = "
				+ mCodUfficioEmittente + " ]\n" + "[ mDescrUfficioEmittente        = "
				+ mDescrUfficioEmittente + " ]\n" + "[ mCodLuogoEmittente            = " + mCodLuogoEmittente
				+ " ]\n" + "[ mDescrLuogoEmittente        	= " + mDescrLuogoEmittente + " ]\n"
				+ "[ mDataD        				= " + mDataD + " ]\n" + "[ mAnnoProvv      				= "
				+ mAnnoProvv + " ]\n" + "[ mNumeroProvv             		= " + mNumeroProvv + " ]\n"
				+ "[ mFlagConforme           		= " + mFlagConforme + " ]\n"
				+ "[ mFlagPiuMenoD           		= " + mFlagPiuMenoD + " ]\n"
				+ "[ mNumAnniReclusioneD         	= " + mNumAnniReclusioneD + " ]\n"
				+ "[ mNumMesiReclusioneD         	= " + mNumMesiReclusioneD + " ]\n"
				+ "[ mNumGiorniReclusioneD         = " + mNumGiorniReclusioneD + " ]\n"
				+ "[ mImportoMultaD         		= " + mImportoMultaD + " ]\n"
				+ "[ mNumAnniArrestoD         		= " + mNumAnniArrestoD + " ]\n"
				+ "[ mNumMesiArrestoD         		= " + mNumMesiArrestoD + " ]\n"
				+ "[ mNumGiorniArrestoD         	= " + mNumGiorniArrestoD + " ]\n"
				+ "[ mImportoAmmendaD         		= " + mImportoAmmendaD + " ]\n"
				+ "[ mNumGiorniRevocaLaD         	= " + mNumGiorniRevocaLaD + " ]\n"
				+ "[ mNumGiorniRevocaLsD           = " + mNumGiorniRevocaLsD + " ]\n"
				+ "[ mNumGiorniRevocaLiD           = " + mNumGiorniRevocaLiD + " ]\n"
				+ "[ mMotivazioniD         		= " + mMotivazioniD + " ]\n"
				+ "[ mCodTipoProvvedimento         = " + mCodTipoProvvedimento + " ]\n"
				+ "[ mDescrTipoProvvedimento       = " + mDescrTipoProvvedimento + " ]\n"
				+ "[ mAnnoSIUS         			= " + mAnnoSIUS + " ]\n"
				+ "[ mNumeroSIUS         			= " + mNumeroSIUS + " ]\n"
				+ "[ mCodTipoMsD         			= " + mCodTipoMsD + " ]\n"
				+ "[ mDescrTipoMsD        			= " + mDescrTipoMsD + " ]\n"
				+ "[ mNumAnniMsD         			= " + mNumAnniMsD + " ]\n"
				+ "[ mNumMesiMsD         			= " + mNumMesiMsD + " ]\n"
				+ "[ mNumGiorniMsD         		= " + mNumGiorniMsD + " ]\n"
				+ "[ mBenSospCond          		= " + mBenSospCond + " ]\n"
				+ "[ mBenNonMenzione          		= " + mBenNonMenzione + " ]\n"
				+ "[ mBenIndulto	          		= " + mBenIndulto + " ]\n" +

				"[ mCodTipoPenaAccessoriaD     = " + mCodTipoPenaAccessoriaD + " ]\n"
				+ "[ mCodTipoDurataPaD           = " + mCodTipoDurataPaD + " ]\n"
				+ "[ mNumAnniPaD                 = " + mNumAnniPaD + " ]\n"
				+ "[ mNumMesiPaD                 = " + mNumMesiPaD + " ]\n"
				+ "[ mNumGiorniPaD               = " + mNumGiorniPaD + " ]\n"
				+ "[ mDataRevoca                 = " + mDataRevoca + " ]\n" +

				"[ mCodOperatoreInserimento		= " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento				= " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento		= " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento	= " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento			= " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento		= " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mRicIdRichiestePmInCumulo		= " + mRicIdRichiestePmInCumulo + " ]\n";

		return lStr;
	}
}
