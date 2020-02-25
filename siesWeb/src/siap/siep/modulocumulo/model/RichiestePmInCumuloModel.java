package siap.siep.modulocumulo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import f3b.model.GenericModel;
import siap.sico.calendar.model.CalendarModel;

/**
 * <p>
 * Title: RichiestePmInCumuloModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il RichiestePmInCumulo
 * </p>
 *
 * @version 1.0
 */
public class RichiestePmInCumuloModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 3546574532717145438L;

	private BigDecimal mIdRichiestePmInCumulo;
	private String mCodTipoRichiesta;
	private String mDescrTipoRichiesta;
	private String mCodTipoAnnotazione;
	private String mDescrTipoAnnotazione;
	private Date mDataEmissione;

	private String mFlagPiuMenoR;
	private BigDecimal mNumAnniReclusioneR;
	private BigDecimal mNumMesiReclusioneR;
	private BigDecimal mNumGiorniReclusioneR;
	private String mStringaReclusioneR;
	private BigDecimal mImportoMultaR;

	private BigDecimal mNumAnniArrestoR;
	private BigDecimal mNumMesiArrestoR;
	private BigDecimal mNumGiorniArrestoR;
	private String mStringaArrestoR;
	private BigDecimal mImportoAmmendaR;

	private String mFlagAppProvvisoria;
	private String mCodTipoPenaAccessoria;
	private String mDescrTipoPenaAccessoria;
	private String mCodTipoDurataPa;
	private String mDescrTipoDurataPa;
	private BigDecimal mNumAnniPa;
	private BigDecimal mNumMesiPa;
	private BigDecimal mNumGiorniPa;

	// Depenalizzazione
	private String mCodFonte;
	private String mDescrFonte;
	private String mDescrFonteSigla;
	private BigDecimal mAnnoFonte;
	private String mNumeroFonte;
	private String mArticolo;
	private String mCodSottonumerazione;
	private String mDescrSottonumerazione;
	private String mComma;
	private String mLettera;
	private String mNumero;
	// Incostituzionalità
	private BigDecimal mAnnoCc;
	private String mNumeroCc;
	private Date mDataCc;
	// Amnistia/Indulto
	private String mCodTipoBeneficio;
	private String mDescrBeneficio;
	private String mCodDpr;
	private String mDescrDpr;
	//
	private String mMotivazioni;
	private String mNoteReclusione;

	private BigDecimal mNumGiorniRevocaLA;
	private BigDecimal mNumGiorniRevocaLS;
	private BigDecimal mNumGiorniRevocaLI;

	private String mCodMotivo;

	// MEV_70
	private String mDescrMotivo;
	private String mDescrArticolo;
	private String mFlagInteroCumulo;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	// Relazioni
	private BigDecimal mIstrIdIstruttoriaCumulo;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mTitIdTitoloCumulatoRef;
	private BigDecimal mRicIdRichiesteInviateCum;
	private BigDecimal mProIdProvvGeSorvCum;

	private RichiesteInviateCumModel mRichiesteInviateCum;
	private ProvvedimentoGeSorvCumModel mDecisioneGeSorvCum;
	private String mAnnoSentenza;
	private String mNumeroSentenza;
	private String mAltri;

	private List<RichPMTitoloCumModel> mListaRichPmTitoloCum;
	private List<RichPMBeneficioCumModel> mListaRichPmBeneficioCum;
	private List<RichPMMisSicCumModel> mlistaRichPmMisuraSicCum;
	private List<RichPMPenAccCumModel> mListaRichPmPenAccCum;
	private List<RichPMReatoCumModel> mListaRichPmReatoCum;
	private List<RichPMSanSostCumModel> mListaRichPMSSCum;
	private List<RichPMStatoEsecCumModel> mListaRichPmStatoEsecCum;

	private Vector<TitoloCumulatoModel> mListaTitoli;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichiestePmInCumuloModel() {
		this.mIdRichiestePmInCumulo = null;
		this.mCodTipoRichiesta = "";
		this.mDescrTipoRichiesta = "";
		this.mCodTipoAnnotazione = "";
		this.mDescrTipoAnnotazione = "";
		this.mDataEmissione = null;
		this.mFlagPiuMenoR = "";
		this.mNumAnniReclusioneR = null;
		this.mNumMesiReclusioneR = null;
		this.mNumGiorniReclusioneR = null;
		this.mImportoMultaR = null;
		this.mNumAnniArrestoR = null;
		this.mNumMesiArrestoR = null;
		this.mNumGiorniArrestoR = null;
		this.mImportoAmmendaR = null;
		this.mFlagAppProvvisoria = "";
		this.mCodTipoPenaAccessoria = "";
		this.mDescrTipoPenaAccessoria = "";
		this.mCodTipoDurataPa = "";
		this.mDescrTipoDurataPa = "";
		this.mNumAnniPa = null;
		this.mNumMesiPa = null;
		this.mNumGiorniPa = null;
		this.mCodFonte = "";
		this.mDescrFonte = "";
		this.mDescrFonteSigla = "";
		this.mAnnoFonte = null;
		this.mNumeroFonte = "";
		this.mArticolo = "";
		this.mCodSottonumerazione = "";
		this.mDescrSottonumerazione = "";
		this.mComma = "";
		this.mLettera = "";
		this.mNumero = "";
		this.mAnnoCc = null;
		this.mNumeroCc = "";
		this.mDataCc = null;

		this.mCodTipoBeneficio = "";
		this.mDescrBeneficio = "";
		this.mCodDpr = "";
		this.mDescrDpr = "";
		this.mMotivazioni = "";
		this.mNoteReclusione = "";

		this.mNumGiorniRevocaLA = null;
		this.mNumGiorniRevocaLS = null;
		this.mNumGiorniRevocaLI = null;
		this.mCodMotivo = null;
		// MEV_70
		this.mDescrMotivo = null;
		this.mDescrArticolo = null;
		this.mFlagInteroCumulo = null;

		this.mIstrIdIstruttoriaCumulo = null;
		this.mTitIdTitoloCumulatoRef = null;

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";

		this.mTitIdTitoloCumulato = null;
		this.mRicIdRichiesteInviateCum = null;
		this.mProIdProvvGeSorvCum = null;

		this.mRichiesteInviateCum = null;
		this.mDecisioneGeSorvCum = null;
		this.mAnnoSentenza = "";
		this.mNumeroSentenza = "";
		this.mAltri = "";

		this.mListaRichPmTitoloCum = null;
		this.mListaRichPmBeneficioCum = null;
		this.mlistaRichPmMisuraSicCum = null;
		this.mListaRichPmPenAccCum = null;

		this.mListaRichPmReatoCum = null;
		this.mListaRichPMSSCum = null;
		this.mListaRichPmStatoEsecCum = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 *
	 * @param aModel
	 ****************************************************************************/
	public RichiestePmInCumuloModel(RichiestePmInCumuloModel aModel) {
		this.mIdRichiestePmInCumulo = aModel.mIdRichiestePmInCumulo;
		this.mCodTipoRichiesta = aModel.mCodTipoRichiesta;
		this.mDescrTipoRichiesta = aModel.mDescrTipoRichiesta;
		this.mCodTipoAnnotazione = aModel.mCodTipoAnnotazione;
		this.mDescrTipoAnnotazione = aModel.mDescrTipoAnnotazione;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mFlagPiuMenoR = aModel.mFlagPiuMenoR;
		this.mNumAnniReclusioneR = aModel.mNumAnniReclusioneR;
		this.mNumMesiReclusioneR = aModel.mNumMesiReclusioneR;
		this.mNumGiorniReclusioneR = aModel.mNumGiorniReclusioneR;
		this.mImportoMultaR = aModel.mImportoMultaR;
		this.mNumAnniArrestoR = aModel.mNumAnniArrestoR;
		this.mNumMesiArrestoR = aModel.mNumMesiArrestoR;
		this.mNumGiorniArrestoR = aModel.mNumGiorniArrestoR;
		this.mImportoAmmendaR = aModel.mImportoAmmendaR;
		this.mFlagAppProvvisoria = aModel.mFlagAppProvvisoria;
		this.mCodTipoPenaAccessoria = aModel.mCodTipoPenaAccessoria;
		this.mDescrTipoPenaAccessoria = aModel.mDescrTipoPenaAccessoria;
		this.mCodTipoDurataPa = aModel.mCodTipoDurataPa;
		this.mDescrTipoDurataPa = aModel.mDescrTipoDurataPa;
		this.mNumAnniPa = aModel.mNumAnniPa;
		this.mNumMesiPa = aModel.mNumMesiPa;
		this.mNumGiorniPa = aModel.mNumGiorniPa;
		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mDescrFonteSigla = aModel.mDescrFonteSigla;
		this.mAnnoFonte = aModel.mAnnoFonte;
		this.mNumeroFonte = aModel.mNumeroFonte;
		this.mArticolo = aModel.mArticolo;
		this.mCodSottonumerazione = aModel.mCodSottonumerazione;
		this.mDescrSottonumerazione = aModel.mDescrSottonumerazione;
		this.mComma = aModel.mComma;
		this.mLettera = aModel.mLettera;
		this.mNumero = aModel.mNumero;
		this.mAnnoCc = aModel.mAnnoCc;
		this.mNumeroCc = aModel.mNumeroCc;
		this.mDataCc = aModel.mDataCc;

		this.mCodTipoBeneficio = aModel.mCodTipoBeneficio;
		this.mDescrBeneficio = aModel.mDescrBeneficio;
		this.mCodDpr = aModel.mCodDpr;
		this.mDescrDpr = aModel.mDescrDpr;
		this.mMotivazioni = aModel.mMotivazioni;
		this.mNoteReclusione = aModel.mNoteReclusione;

		this.mNumGiorniRevocaLA = aModel.mNumGiorniRevocaLA;
		this.mNumGiorniRevocaLS = aModel.mNumGiorniRevocaLS;
		this.mNumGiorniRevocaLI = aModel.mNumGiorniRevocaLI;
		this.mCodMotivo = aModel.mCodMotivo;

		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mTitIdTitoloCumulatoRef = aModel.mTitIdTitoloCumulatoRef;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;

		this.mRicIdRichiesteInviateCum = aModel.mRicIdRichiesteInviateCum;
		this.mProIdProvvGeSorvCum = aModel.mProIdProvvGeSorvCum;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public RichiestePmInCumuloModel(BigDecimal aIdRichiestePmInCumulo, String aCodTipoRichiesta,
			String aDescrTipoRichiesta, String aCodTipoAnnotazione, String aDescrTipoAnnotazione,
			Date aDataEmissione, String aFlagPiuMenoR, BigDecimal aNumAnniReclusioneR,
			BigDecimal aNumMesiReclusioneR, BigDecimal aNumGiorniReclusioneR, BigDecimal aImportoMultaR,
			BigDecimal aNumAnniArrestoR, BigDecimal aNumMesiArrestoR, BigDecimal aNumGiorniArrestoR,
			BigDecimal aImportoAmmendaR, String aFlagAppProvvisoria, String aCodTipoPenaAccessoria,
			String aDescrTipoPenaAccessoria, String aCodTipoDurataPa, String aDescrTipoDurataPa,
			BigDecimal aNumAnniPa, BigDecimal aNumMesiPa, BigDecimal aNumGiorniPa, String aCodFonte,
			String aDescrFonte, String aDescrFonteSigla, BigDecimal aAnnoFonte, String aNumeroFonte,
			String aArticolo, String aCodSottonumerazione, String aDescrSottonumerazione, String aComma,
			String aLettera, String aNumero, BigDecimal aAnnoCc, String aNumeroCc, Date aDataCc,

			String aCodTipoBeneficio, String aDescrBeneficio, String aCodDpr, String aDescrDpr,
			String aMotivazioni, String aNoteReclusione,

			BigDecimal aNumGiorniRevocaLA, BigDecimal aNumGiorniRevocaLS, BigDecimal aNumGiorniRevocaLI,

			String aCodMotivo,

			BigDecimal aIstrIdIstruttoriaCumulo, BigDecimal aTitIdTitoloCumulato,
			BigDecimal aTitIdTitoloCumulatoRef,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			BigDecimal aRicIdRichiesteInviateCum) {
		this.mIdRichiestePmInCumulo = aIdRichiestePmInCumulo;
		this.mCodTipoRichiesta = aCodTipoRichiesta;
		this.mDescrTipoRichiesta = aDescrTipoRichiesta;
		this.mCodTipoAnnotazione = aCodTipoAnnotazione;
		this.mDescrTipoAnnotazione = aDescrTipoAnnotazione;
		this.mDataEmissione = aDataEmissione;
		this.mFlagPiuMenoR = aFlagPiuMenoR;
		this.mNumAnniReclusioneR = aNumAnniReclusioneR;
		this.mNumMesiReclusioneR = aNumMesiReclusioneR;
		this.mNumGiorniReclusioneR = aNumGiorniReclusioneR;
		this.mImportoMultaR = aImportoMultaR;
		this.mNumAnniArrestoR = aNumAnniArrestoR;
		this.mNumMesiArrestoR = aNumMesiArrestoR;
		this.mNumGiorniArrestoR = aNumGiorniArrestoR;
		this.mImportoAmmendaR = aImportoAmmendaR;
		this.mFlagAppProvvisoria = aFlagAppProvvisoria;
		this.mCodTipoPenaAccessoria = aCodTipoPenaAccessoria;
		this.mDescrTipoPenaAccessoria = aDescrTipoPenaAccessoria;
		this.mCodTipoDurataPa = aCodTipoDurataPa;
		this.mDescrTipoDurataPa = aDescrTipoDurataPa;
		this.mNumAnniPa = aNumAnniPa;
		this.mNumMesiPa = aNumMesiPa;
		this.mNumGiorniPa = aNumGiorniPa;
		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mDescrFonteSigla = aDescrFonteSigla;
		this.mAnnoFonte = aAnnoFonte;
		this.mNumeroFonte = aNumeroFonte;
		this.mArticolo = aArticolo;
		this.mCodSottonumerazione = aCodSottonumerazione;
		this.mDescrSottonumerazione = aDescrSottonumerazione;
		this.mComma = aComma;
		this.mLettera = aLettera;
		this.mNumero = aNumero;
		this.mAnnoCc = aAnnoCc;
		this.mNumeroCc = aNumeroCc;
		this.mDataCc = aDataCc;

		this.mCodTipoBeneficio = aCodTipoBeneficio;
		this.mDescrBeneficio = aDescrBeneficio;
		this.mCodDpr = aCodDpr;
		this.mDescrDpr = aDescrDpr;
		this.mMotivazioni = aMotivazioni;
		this.mNoteReclusione = aNoteReclusione;

		this.mNumGiorniRevocaLA = aNumGiorniRevocaLA;
		this.mNumGiorniRevocaLS = aNumGiorniRevocaLS;
		this.mNumGiorniRevocaLI = aNumGiorniRevocaLI;
		this.mCodMotivo = aCodMotivo;

		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mTitIdTitoloCumulatoRef = aTitIdTitoloCumulatoRef;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mRicIdRichiesteInviateCum = aRicIdRichiesteInviateCum;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdRichiestePmInCumulo() {
		return mIdRichiestePmInCumulo;
	}

	public String getCodTipoRichiesta() {
		return mCodTipoRichiesta;
	}

	public String getDescrTipoRichiesta() {
		return mDescrTipoRichiesta;
	}

	public String getCodTipoAnnotazione() {
		return mCodTipoAnnotazione;
	}

	public String getDescrTipoAnnotazione() {
		return mDescrTipoAnnotazione;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public String getFlagPiuMenoR() {
		return mFlagPiuMenoR;
	}

	public BigDecimal getNumAnniReclusioneR() {
		return mNumAnniReclusioneR;
	}

	public BigDecimal getNumMesiReclusioneR() {
		return mNumMesiReclusioneR;
	}

	public BigDecimal getNumGiorniReclusioneR() {
		return mNumGiorniReclusioneR;
	}

	public BigDecimal getImportoMultaR() {
		return mImportoMultaR;
	}

	public BigDecimal getNumAnniArrestoR() {
		return mNumAnniArrestoR;
	}

	public BigDecimal getNumMesiArrestoR() {
		return mNumMesiArrestoR;
	}

	public BigDecimal getNumGiorniArrestoR() {
		return mNumGiorniArrestoR;
	}

	public BigDecimal getImportoAmmendaR() {
		return mImportoAmmendaR;
	}

	public String getFlagAppProvvisoria() {
		return mFlagAppProvvisoria;
	}

	public String getCodTipoPenaAccessoria() {
		return mCodTipoPenaAccessoria;
	}

	public String getDescrTipoPenaAccessoria() {
		return mDescrTipoPenaAccessoria;
	}

	public String getCodTipoDurataPa() {
		return mCodTipoDurataPa;
	}

	public String getDescrTipoDurataPa() {
		return mDescrTipoDurataPa;
	}

	public BigDecimal getNumAnniPa() {
		return mNumAnniPa;
	}

	public BigDecimal getNumMesiPa() {
		return mNumMesiPa;
	}

	public BigDecimal getNumGiorniPa() {
		return mNumGiorniPa;
	}

	public String getCodFonte() {
		return mCodFonte;
	}

	public String getDescrFonte() {
		return mDescrFonte;
	}

	public String getDescrFonteSigla() {
		return mDescrFonteSigla;
	}

	public BigDecimal getAnnoFonte() {
		return mAnnoFonte;
	}

	public String getNumeroFonte() {
		return mNumeroFonte;
	}

	public String getArticolo() {
		return mArticolo;
	}

	public String getCodSottonumerazione() {
		return mCodSottonumerazione;
	}

	public String getDescrSottonumerazione() {
		return mDescrSottonumerazione;
	}

	public String getComma() {
		return mComma;
	}

	public String getLettera() {
		return mLettera;
	}

	public String getNumero() {
		return mNumero;
	}

	public BigDecimal getAnnoCc() {
		return mAnnoCc;
	}

	public String getNumeroCc() {
		return mNumeroCc;
	}

	public Date getDataCc() {
		return mDataCc;
	}

	public String getCodTipoBeneficio() {
		return mCodTipoBeneficio;
	}

	public String getDescrBeneficio() {
		return mDescrBeneficio;
	}

	public String getCodDpr() {
		return mCodDpr;
	}

	public String getDescrDpr() {
		return mDescrDpr;
	}

	public String getMotivazioni() {
		return mMotivazioni;
	}

	public String getNoteReclusione() {
		return mNoteReclusione;
	}

	public BigDecimal getNumGiorniRevocaLA() {
		return mNumGiorniRevocaLA;
	}

	public BigDecimal getNumGiorniRevocaLS() {
		return mNumGiorniRevocaLS;
	}

	public BigDecimal getNumGiorniRevocaLI() {
		return mNumGiorniRevocaLI;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

// MEV_70
	public String getDescrMotivo() {
		return mDescrMotivo;
	}
	public String getDescrArticolo() {
		return mDescrArticolo;
	}
	public String getFlagInteroCumulo() {
		return mFlagInteroCumulo;
	}	
//
	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
	}

	public BigDecimal getTitIdTitoloCumulatoRef() {
		return mTitIdTitoloCumulatoRef;
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

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public BigDecimal getRicIdRichiesteInviateCum() {
		return mRicIdRichiesteInviateCum;
	}

	public RichiesteInviateCumModel getRichiesteInviateCum() {
		return mRichiesteInviateCum;
	}

	public ProvvedimentoGeSorvCumModel getDecisioneGeSorvCum() {
		return mDecisioneGeSorvCum;
	}

	public String getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumeroSentenza() {
		return mNumeroSentenza;
	}

	public String getAltri() {
		return mAltri;
	}

	public List<RichPMTitoloCumModel> getListaRichPmTitoloCum() {
		return mListaRichPmTitoloCum;
	}

	public List<RichPMBeneficioCumModel> getListaRichPmBeneficioCum() {
		return mListaRichPmBeneficioCum;
	}

	public List<RichPMMisSicCumModel> getListaRichPmMisureSicurezzaCum() {
		return mlistaRichPmMisuraSicCum;
	}

	public List<RichPMPenAccCumModel> getListaRichPmPenaAccessoriaCum() {
		return mListaRichPmPenAccCum;
	}

	public List<RichPMReatoCumModel> getListaRichPmReatoCum() {
		return mListaRichPmReatoCum;
	}

	public List<RichPMSanSostCumModel> getListaRichPmSanzioneSostCum() {
		return mListaRichPMSSCum;
	}

	public List<RichPMStatoEsecCumModel> getListaRichPmStatoEsecCum() {
		return mListaRichPmStatoEsecCum;
	}

	public String getStringaReclusioneR() {
		return mStringaReclusioneR;
	}

	public String getStringaArrestoR() {
		return mStringaArrestoR;
	}

	public Vector<TitoloCumulatoModel> getListaTitoli() {
		return mListaTitoli;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdRichiestePmInCumulo(BigDecimal aValore) {
		mIdRichiestePmInCumulo = aValore;
	}

	public void setCodTipoRichiesta(String aValore) {
		mCodTipoRichiesta = aValore;
	}

	public void setDescrTipoRichiesta(String aValore) {
		mDescrTipoRichiesta = aValore;
	}

	public void setCodTipoAnnotazione(String aValore) {
		mCodTipoAnnotazione = aValore;
	}

	public void setDescrTipoAnnotazione(String aValore) {
		mDescrTipoAnnotazione = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setFlagPiuMenoR(String aValore) {
		mFlagPiuMenoR = aValore;
	}

	public void setNumAnniReclusioneR(BigDecimal aValore) {
		mNumAnniReclusioneR = aValore;
	}

	public void setNumMesiReclusioneR(BigDecimal aValore) {
		mNumMesiReclusioneR = aValore;
	}

	public void setNumGiorniReclusioneR(BigDecimal aValore) {
		mNumGiorniReclusioneR = aValore;
	}

	public void setImportoMultaR(BigDecimal aValore) {
		mImportoMultaR = aValore;
	}

	public void setNumAnniArrestoR(BigDecimal aValore) {
		mNumAnniArrestoR = aValore;
	}

	public void setNumMesiArrestoR(BigDecimal aValore) {
		mNumMesiArrestoR = aValore;
	}

	public void setNumGiorniArrestoR(BigDecimal aValore) {
		mNumGiorniArrestoR = aValore;
	}

	public void setImportoAmmendaR(BigDecimal aValore) {
		mImportoAmmendaR = aValore;
	}

	public void setFlagAppProvvisoria(String aValore) {
		mFlagAppProvvisoria = aValore;
	}

	public void setCodTipoPenaAccessoria(String aValore) {
		mCodTipoPenaAccessoria = aValore;
	}

	public void setDescrTipoPenaAccessoria(String aValore) {
		mDescrTipoPenaAccessoria = aValore;
	}

	public void setCodTipoDurataPa(String aValore) {
		mCodTipoDurataPa = aValore;
	}

	public void setDescrTipoDurataPa(String aValore) {
		mDescrTipoDurataPa = aValore;
	}

	public void setNumAnniPa(BigDecimal aValore) {
		mNumAnniPa = aValore;
	}

	public void setNumMesiPa(BigDecimal aValore) {
		mNumMesiPa = aValore;
	}

	public void setNumGiorniPa(BigDecimal aValore) {
		mNumGiorniPa = aValore;
	}

	public void setCodFonte(String aValore) {
		mCodFonte = aValore;
	}

	public void setDescrFonte(String aValore) {
		mDescrFonte = aValore;
	}

	public void setDescrFonteSigla(String aValore) {
		mDescrFonteSigla = aValore;
	}

	public void setAnnoFonte(BigDecimal aValore) {
		mAnnoFonte = aValore;
	}

	public void setNumeroFonte(String aValore) {
		mNumeroFonte = aValore;
	}

	public void setArticolo(String aValore) {
		mArticolo = aValore;
	}

	public void setCodSottonumerazione(String aValore) {
		mCodSottonumerazione = aValore;
	}

	public void setDescrSottonumerazione(String aValore) {
		mDescrSottonumerazione = aValore;
	}

	public void setComma(String aValore) {
		mComma = aValore;
	}

	public void setLettera(String aValore) {
		mLettera = aValore;
	}

	public void setNumero(String aValore) {
		mNumero = aValore;
	}

	public void setAnnoCc(BigDecimal aValore) {
		mAnnoCc = aValore;
	}

	public void setNumeroCc(String aValore) {
		mNumeroCc = aValore;
	}

	public void setDataCc(Date aValore) {
		mDataCc = aValore;
	}

	public void setCodTipoBeneficio(String aValore) {
		mCodTipoBeneficio = aValore;
	}

	public void setDescrBeneficio(String aValore) {
		mDescrBeneficio = aValore;
	}

	public void setCodDpr(String aValore) {
		mCodDpr = aValore;
	}

	public void setDescrDpr(String aValore) {
		mDescrDpr = aValore;
	}

	public void setMotivazioni(String aValore) {
		mMotivazioni = aValore;
	}

	public void setNoteReclusione(String aValore) {
		mNoteReclusione = aValore;
	}

	public void setNumGiorniRevocaLA(BigDecimal aValore) {
		mNumGiorniRevocaLA = aValore;
	}

	public void setNumGiorniRevocaLS(BigDecimal aValore) {
		mNumGiorniRevocaLS = aValore;
	}

	public void setNumGiorniRevocaLI(BigDecimal aValore) {
		mNumGiorniRevocaLI = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

// MEV_70
	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}
	public void setDescrArticolo(String aValore) {
		mDescrArticolo = aValore;
	}
	public void setFlagInteroCumulo(String aValore) {
		mFlagInteroCumulo = aValore;
	}
	
//
	
	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
	}

	public void setTitIdTitoloCumulatoRef(BigDecimal aValore) {
		mTitIdTitoloCumulatoRef = aValore;
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

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setRicIdRichiesteInviateCum(BigDecimal aValore) {
		mRicIdRichiesteInviateCum = aValore;
	}

	public void setRichiesteInviateCum(RichiesteInviateCumModel aValore) {
		mRichiesteInviateCum = aValore;
	}

	public void setDecisioneGeSorvCum(ProvvedimentoGeSorvCumModel aValore) {
		mDecisioneGeSorvCum = aValore;
	}

	public void setAnnoSentenza(String aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumeroSentenza(String aValore) {
		mNumeroSentenza = aValore;
	}

	public void setAltri(String aValore) {
		mAltri = aValore;
	}

	public void setListaRichPmTitoloCum(List<RichPMTitoloCumModel> aValore) {
		mListaRichPmTitoloCum = aValore;
	}

	public void setListaRichPmBeneficioCum(List<RichPMBeneficioCumModel> aValore) {
		mListaRichPmBeneficioCum = aValore;
	}

	public void setListaRichPmMisuraSicurezzaCum(List<RichPMMisSicCumModel> aValore) {
		mlistaRichPmMisuraSicCum = aValore;
	}

	public void setListaRichPmPenaAccessoriaCum(List<RichPMPenAccCumModel> aValore) {
		mListaRichPmPenAccCum = aValore;
	}

	public void setListaRichPmReatoCum(List<RichPMReatoCumModel> aValore) {
		mListaRichPmReatoCum = aValore;
	}

	public void setListaRichPmSanzioneSostCum(List<RichPMSanSostCumModel> aValore) {
		mListaRichPMSSCum = aValore;
	}

	public void setListaRichPmStatoEsecCum(List<RichPMStatoEsecCumModel> aValore) {
		mListaRichPmStatoEsecCum = aValore;
	}

	public void setStringaReclusioneR(String aValore) {
		mStringaReclusioneR = aValore;
	}

	public void setStringaArrestoR(String aValore) {
		mStringaArrestoR = aValore;
	}

	public void setListaTitoli(Vector<TitoloCumulatoModel> aValore) {
		mListaTitoli = aValore;
	}

	public void addTitolo(TitoloCumulatoModel aValore) {
		if (mListaTitoli == null)
			mListaTitoli = new Vector<>();

		mListaTitoli.add(aValore);
	}

	public boolean isQuantumReclusioneZero() {
		return ((mNumAnniReclusioneR == null || mNumAnniReclusioneR.intValue() == 0)
				&& (mNumMesiReclusioneR == null || mNumMesiReclusioneR.intValue() == 0)
				&& (mNumGiorniReclusioneR == null || mNumGiorniReclusioneR.intValue() == 0));
	}

	public boolean isQuantumArrestoZero() {
		return ((mNumAnniArrestoR == null || mNumAnniArrestoR.intValue() == 0)
				&& (mNumMesiArrestoR == null || mNumMesiArrestoR.intValue() == 0)
				&& (mNumGiorniArrestoR == null || mNumGiorniArrestoR.intValue() == 0));
	}

	public void calcolaStringaReclusioneinRichiestePm() {
		String lStringReclusione = "";
		if (mNumAnniReclusioneR != null && mNumAnniReclusioneR.intValue() != 0)
			lStringReclusione = "Anni " + mNumAnniReclusioneR;

		if (mNumMesiReclusioneR != null && mNumMesiReclusioneR.intValue() != 0)
			lStringReclusione += " Mesi " + mNumMesiReclusioneR;

		if (mNumGiorniReclusioneR != null && mNumGiorniReclusioneR.intValue() != 0)
			lStringReclusione += " Giorni " + mNumGiorniReclusioneR;

		if (lStringReclusione.length() > 1) {
			this.mStringaReclusioneR = lStringReclusione.trim();
		} else {
			this.mStringaReclusioneR = null;
		}
	}

	public void calcolaStringaArrestoinRichiestePm() {
		String lStringArresto = "";

		if (mNumAnniArrestoR != null && mNumAnniArrestoR.intValue() != 0)
			lStringArresto = "Anni " + mNumAnniArrestoR;

		if (mNumMesiArrestoR != null && mNumMesiArrestoR.intValue() != 0)
			lStringArresto += " Mesi " + mNumMesiArrestoR;

		if (mNumGiorniArrestoR != null && mNumGiorniArrestoR.intValue() != 0)
			lStringArresto += " Giorni " + mNumGiorniArrestoR;

		if (lStringArresto.length() > 1) {
			this.mStringaArrestoR = lStringArresto.trim();
		} else {
			this.mStringaArrestoR = null;
		}
	}

	public CalendarModel getQuantumReclusione() {
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumGiorni(this.mNumGiorniReclusioneR);
		lCalReclusione.setNumMesi(this.mNumMesiReclusioneR);
		lCalReclusione.setNumAnni(this.mNumAnniReclusioneR);

		if (mImportoMultaR != null)
			lCalReclusione.setImportoMulta(this.mImportoMultaR.doubleValue());

		return lCalReclusione;
	}

	/**
	* 
	*/
	public CalendarModel getQuantumArresto() {
		CalendarModel lCalArresto = new CalendarModel();

		lCalArresto.setNumGiorni(this.mNumGiorniArrestoR);
		lCalArresto.setNumMesi(this.mNumMesiArrestoR);
		lCalArresto.setNumAnni(this.mNumAnniArrestoR);

		if (mImportoAmmendaR != null)
			lCalArresto.setImportoAmmenda(this.mImportoAmmendaR.doubleValue());

		return lCalArresto;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichiestePmInCumuloModel:\n" + "[ mIdRichiestePmInCumulo     = " + mIdRichiestePmInCumulo
				+ " ]\n" + "[ mCodTipoRichiesta          = " + mCodTipoRichiesta + " ]\n"
				+ "[ mCodTipoAnnotazione        = " + mCodTipoAnnotazione + " ]\n"
				+ "[ mDataEmissione             = " + mDataEmissione + " ]\n"
				+ "[ mFlagPiuMenoR              = " + mFlagPiuMenoR + " ]\n"
				+ "[ mNumAnniReclusioneR        = " + mNumAnniReclusioneR + " ]\n"
				+ "[ mNumMesiReclusioneR        = " + mNumMesiReclusioneR + " ]\n"
				+ "[ mNumGiorniReclusioneR      = " + mNumGiorniReclusioneR + " ]\n"
				+ "[ mImportoMultaR             = " + mImportoMultaR + " ]\n"
				+ "[ mNumAnniArrestoR           = " + mNumAnniArrestoR + " ]\n"
				+ "[ mNumMesiArrestoR           = " + mNumMesiArrestoR + " ]\n"
				+ "[ mNumGiorniArrestoR         = " + mNumGiorniArrestoR + " ]\n"
				+ "[ mImportoAmmendaR           = " + mImportoAmmendaR + " ]\n"
				+ "[ mFlagAppProvvisoria        = " + mFlagAppProvvisoria + " ]\n"
				+ "[ mCodTipoPenaAccessoria     = " + mCodTipoPenaAccessoria + " ]\n"
				+ "[ mCodTipoDurataPa           = " + mCodTipoDurataPa + " ]\n"
				+ "[ mNumAnniPa                 = " + mNumAnniPa + " ]\n" + "[ mNumMesiPa                 = "
				+ mNumMesiPa + " ]\n" + "[ mNumGiorniPa               = " + mNumGiorniPa + " ]\n"
				+ "[ mCodFonte                  = " + mCodFonte + " ]\n" + "[ mDescrFonteSigla           = "
				+ mDescrFonteSigla + " ]\n" + "[ mAnnoFonte                 = " + mAnnoFonte + " ]\n"
				+ "[ mNumeroFonte               = " + mNumeroFonte + " ]\n"
				+ "[ mArticolo                  = " + mArticolo + " ]\n" + "[ mCodSottonumerazione       = "
				+ mCodSottonumerazione + " ]\n" + "[ mComma                     = " + mComma + " ]\n"
				+ "[ mLettera                   = " + mLettera + " ]\n" + "[ mNumero                    = "
				+ mNumero + " ]\n" + "[ mAnnoCc                    = " + mAnnoCc + " ]\n"
				+ "[ mNumeroCc                  = " + mNumeroCc + " ]\n" + "[ mDataCc                    = "
				+ mDataCc + " ]\n" + "[ mCodTipoBeneficio          = " + mCodTipoBeneficio + " ]\n"
				+ "[ mCodDpr                    = " + mCodDpr + " ]\n" + "[ mMotivazioni               = "
				+ mMotivazioni + " ]\n" + "[ mNoteReclusione            = " + mNoteReclusione + " ]\n" +

				"[ mNumGiorniRevocaLA         = " + mNumGiorniRevocaLA + " ]\n"
				+ "[ mNumGiorniRevocaLS         = " + mNumGiorniRevocaLS + " ]\n"
				+ "[ mNumGiorniRevocaLI         = " + mNumGiorniRevocaLI + " ]\n"
				+ "[ mCodMotivo        		 = " + mCodMotivo + " ]\n" 
				+ "[ mDescrMotivo        	 = " + mDescrMotivo + " ]\n" 
				+ "[ mDescrArticolo         = " + mDescrArticolo + " ]\n"
				+ "[ mFlagInteroCumulo       = " + mFlagInteroCumulo + " ]\n" +

				"[ mIstrIdIstruttoriaCumulo   = " + mIstrIdIstruttoriaCumulo + " ]\n"
				+ "[ mTitIdTitoloCumulatoRef    = " + mTitIdTitoloCumulatoRef + " ]\n" +

				"[ mTitIdTitoloCumulato       = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mRicIdRichiesteInviateCum  = " + mRicIdRichiesteInviateCum + " ]\n" +

				"[ mAnnoSentenza          	= " + mAnnoSentenza + " ]\n" + "[ mNumeroSentenza          = "
				+ mNumeroSentenza + " ]\n" + "[ mAltri         			= " + mAltri + " ]\n" +

				"[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}