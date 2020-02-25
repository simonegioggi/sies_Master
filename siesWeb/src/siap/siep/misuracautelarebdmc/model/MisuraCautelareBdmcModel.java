package siap.siep.misuracautelarebdmc.model;

/**
* <p>Title: MisuraCautelareBdmcModel</p>
* <p>Description: Classe Model che rappresenta il MisuraCautelareBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class MisuraCautelareBdmcModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 467008525819976538L;
	private BigDecimal mIdMisuraCautelareBdmc;
	private BigDecimal mFasSieIdFascicoloSiep;
	private BigDecimal mSogIdSoggetto;
	private BigDecimal mEveIdEvento;
	private BigDecimal mPenResIdPenaResidua;
	private BigDecimal mIdPren;
	private BigDecimal mProgPeriPres;
	private String mFlagCaricamento;
	private String mFlagStato;
	private String mCodTipoMisura;
	private String mDescrTipoMisura;
	private Date mDataInizio;
	private Date mDataFine;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private String mIstDetIdIstitutoDetenzione;
	private String mAltroLuogoDetenzione;
	private String mFlagComputabile;
	private String mCodMotivoNonComputabile;
	private String mDescrMotivoNonComputabile;
	private String mCodTipoUfficioRifer;
	private String mDescrTipoUfficioRifer;
	private String mCodLuogoUfficioRifer;
	private String mDescrLuogoUfficioRifer;
	private Date mDataComputo;
	private BigDecimal mAnnoFascSiep;
	private BigDecimal mNumeFascSiep;
	private String mNote;
	private BigDecimal mAnnoFascBdmc;
	private BigDecimal mNumeFascBdmc;
	private String mCodUfficioBdmc;
	private String mDescrUfficioBdmc;
	private BigDecimal mAnnoRgnr;
	private BigDecimal mNumeRgnr;
	private String mCodUfficioRgnr;
	private String mDescrUfficioRgnr;
	private BigDecimal mAnnoRegeGip;
	private BigDecimal mNumeroRegeGip;
	private String mCodUfficioGip;
	private String mDescrUfficioGip;
	private BigDecimal mAnnoRegeDib;
	private BigDecimal mNumeroRegeDib;
	private String mCodUfficioDib;
	private String mDescrUfficioDib;
	private BigDecimal mAnnoRegeCas;
	private BigDecimal mNumeroRegeCas;
	private String mCodUfficioCas;
	private String mDescrUfficioCas;
	private BigDecimal mAnnoRegeCap;
	private BigDecimal mNumeroRegeCap;
	private String mCodUfficioCap;
	private String mDescrUfficioCap;
	private BigDecimal mAnnoRegeCasap;
	private BigDecimal mNumeroRegeCasap;
	private String mCodUfficioCasap;
	private String mDescrUfficioCasap;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mIdMisuraCautelare;
	private BigDecimal mIdAnnotazioneManuale;
	private BigDecimal mIdProvvBdmc;
	private String mStatoTrasmissioneIsc;
	private String mStatoTrasmissioneVal;
	private Date mDataInizioUsata;
	private Date mDataFineUsata;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public MisuraCautelareBdmcModel() {
		this.mIdMisuraCautelareBdmc = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mSogIdSoggetto = null;
		this.mEveIdEvento = null;
		this.mPenResIdPenaResidua = null;
		this.mIdPren = null;
		this.mProgPeriPres = null;
		this.mFlagCaricamento = "";
		this.mFlagStato = "";
		this.mCodTipoMisura = "";
		this.mDescrTipoMisura = "";
		this.mDataInizio = null;
		this.mDataFine = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mIstDetIdIstitutoDetenzione = "";
		this.mAltroLuogoDetenzione = "";
		this.mFlagComputabile = "";
		this.mCodMotivoNonComputabile = "";
		this.mDescrMotivoNonComputabile = "";
		this.mCodTipoUfficioRifer = "";
		this.mDescrTipoUfficioRifer = "";
		this.mCodLuogoUfficioRifer = "";
		this.mDescrLuogoUfficioRifer = "";
		this.mDataComputo = null;
		this.mAnnoFascSiep = null;
		this.mNumeFascSiep = null;
		this.mNote = "";
		this.mAnnoFascBdmc = null;
		this.mNumeFascBdmc = null;
		this.mCodUfficioBdmc = "";
		this.mDescrUfficioBdmc = "";
		this.mAnnoRgnr = null;
		this.mNumeRgnr = null;
		this.mCodUfficioRgnr = "";
		this.mDescrUfficioRgnr = "";
		this.mAnnoRegeGip = null;
		this.mNumeroRegeGip = null;
		this.mCodUfficioGip = "";
		this.mDescrUfficioGip = "";
		this.mAnnoRegeDib = null;
		this.mNumeroRegeDib = null;
		this.mCodUfficioDib = "";
		this.mDescrUfficioDib = "";
		this.mAnnoRegeCas = null;
		this.mNumeroRegeCas = null;
		this.mCodUfficioCas = "";
		this.mDescrUfficioCas = "";
		this.mAnnoRegeCap = null;
		this.mNumeroRegeCap = null;
		this.mCodUfficioCap = "";
		this.mDescrUfficioCap = "";
		this.mAnnoRegeCasap = null;
		this.mNumeroRegeCasap = null;
		this.mCodUfficioCasap = "";
		this.mDescrUfficioCasap = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mIdMisuraCautelare = null;
		this.mIdAnnotazioneManuale = null;
		this.mIdProvvBdmc = null;
		this.mStatoTrasmissioneIsc = null;
		this.mStatoTrasmissioneVal = null;
		this.mDataInizioUsata = null;
		this.mDataFineUsata = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public MisuraCautelareBdmcModel(MisuraCautelareBdmcModel aModel) {
		this.mIdMisuraCautelareBdmc = aModel.mIdMisuraCautelareBdmc;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mPenResIdPenaResidua = aModel.mPenResIdPenaResidua;
		this.mIdPren = aModel.mIdPren;
		this.mProgPeriPres = aModel.mProgPeriPres;
		this.mFlagCaricamento = aModel.mFlagCaricamento;
		this.mFlagStato = aModel.mFlagStato;
		this.mCodTipoMisura = aModel.mCodTipoMisura;
		this.mDescrTipoMisura = aModel.mDescrTipoMisura;
		this.mDataInizio = aModel.mDataInizio;
		this.mDataFine = aModel.mDataFine;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mAltroLuogoDetenzione = aModel.mAltroLuogoDetenzione;
		this.mFlagComputabile = aModel.mFlagComputabile;
		this.mCodMotivoNonComputabile = aModel.mCodMotivoNonComputabile;
		this.mDescrMotivoNonComputabile = aModel.mDescrMotivoNonComputabile;
		this.mCodTipoUfficioRifer = aModel.mCodTipoUfficioRifer;
		this.mDescrTipoUfficioRifer = aModel.mDescrTipoUfficioRifer;
		this.mCodLuogoUfficioRifer = aModel.mCodLuogoUfficioRifer;
		this.mDescrLuogoUfficioRifer = aModel.mDescrLuogoUfficioRifer;
		this.mDataComputo = aModel.mDataComputo;
		this.mAnnoFascSiep = aModel.mAnnoFascSiep;
		this.mNumeFascSiep = aModel.mNumeFascSiep;
		this.mNote = aModel.mNote;
		this.mAnnoFascBdmc = aModel.mAnnoFascBdmc;
		this.mNumeFascBdmc = aModel.mNumeFascBdmc;
		this.mCodUfficioBdmc = aModel.mCodUfficioBdmc;
		this.mDescrUfficioBdmc = aModel.mDescrUfficioBdmc;
		this.mAnnoRgnr = aModel.mAnnoRgnr;
		this.mNumeRgnr = aModel.mNumeRgnr;
		this.mCodUfficioRgnr = aModel.mCodUfficioRgnr;
		this.mDescrUfficioRgnr = aModel.mDescrUfficioRgnr;
		this.mAnnoRegeGip = aModel.mAnnoRegeGip;
		this.mNumeroRegeGip = aModel.mNumeroRegeGip;
		this.mCodUfficioGip = aModel.mCodUfficioGip;
		this.mDescrUfficioGip = aModel.mDescrUfficioGip;
		this.mAnnoRegeDib = aModel.mAnnoRegeDib;
		this.mNumeroRegeDib = aModel.mNumeroRegeDib;
		this.mCodUfficioDib = aModel.mCodUfficioDib;
		this.mDescrUfficioDib = aModel.mDescrUfficioDib;
		this.mAnnoRegeCas = aModel.mAnnoRegeCas;
		this.mNumeroRegeCas = aModel.mNumeroRegeCas;
		this.mCodUfficioCas = aModel.mCodUfficioCas;
		this.mDescrUfficioCas = aModel.mDescrUfficioCas;
		this.mAnnoRegeCap = aModel.mAnnoRegeCap;
		this.mNumeroRegeCap = aModel.mNumeroRegeCap;
		this.mCodUfficioCap = aModel.mCodUfficioCap;
		this.mDescrUfficioCap = aModel.mDescrUfficioCap;
		this.mAnnoRegeCasap = aModel.mAnnoRegeCasap;
		this.mNumeroRegeCasap = aModel.mNumeroRegeCasap;
		this.mCodUfficioCasap = aModel.mCodUfficioCasap;
		this.mDescrUfficioCasap = aModel.mDescrUfficioCasap;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mIdMisuraCautelare = aModel.mIdMisuraCautelare;
		this.mIdAnnotazioneManuale = aModel.mIdAnnotazioneManuale;
		this.mIdProvvBdmc = aModel.mIdProvvBdmc;
		this.mStatoTrasmissioneIsc = aModel.mStatoTrasmissioneIsc;
		this.mStatoTrasmissioneVal = aModel.mStatoTrasmissioneVal;
		this.mDataInizioUsata = aModel.mDataInizioUsata;
		this.mDataFineUsata = aModel.mDataFineUsata;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public MisuraCautelareBdmcModel(BigDecimal aIdMisuraCautelareBdmc, BigDecimal aFasSieIdFascicoloSiep,
			BigDecimal aSogIdSoggetto, BigDecimal aEveIdEvento, BigDecimal aPenResIdPenaResidua,
			BigDecimal aIdPren, BigDecimal aProgPeriPres, String aFlagCaricamento, String aFlagStato,
			String aCodTipoMisura, String aDescrTipoMisura, Date aDataInizio, Date aDataFine,
			BigDecimal aNumAnni, BigDecimal aNumMesi, BigDecimal aNumGiorni,
			String aIstDetIdIstitutoDetenzione, String aAltroLuogoDetenzione, String aFlagComputabile,
			String aCodMotivoNonComputabile, String aDescrMotivoNonComputabile, String aCodTipoUfficioRifer,
			String aDescrTipoUfficioRifer, String aCodLuogoUfficioRifer, String aDescrLuogoUfficioRifer,
			Date aDataComputo, BigDecimal aAnnoFascSiep, BigDecimal aNumeFascSiep, String aNote,
			BigDecimal aAnnoFascBdmc, BigDecimal aNumeFascBdmc, String aCodUfficioBdmc,
			String aDescrUfficioBdmc, BigDecimal aAnnoRgnr, BigDecimal aNumeRgnr, String aCodUfficioRgnr,
			String aDescrUfficioRgnr, BigDecimal aAnnoRegeGip, BigDecimal aNumeroRegeGip,
			String aCodUfficioGip, String aDescrUfficioGip, BigDecimal aAnnoRegeDib,
			BigDecimal aNumeroRegeDib, String aCodUfficioDib, String aDescrUfficioDib,
			BigDecimal aAnnoRegeCas, BigDecimal aNumeroRegeCas, String aCodUfficioCas,
			String aDescrUfficioCas, BigDecimal aAnnoRegeCap, BigDecimal aNumeroRegeCap,
			String aCodUfficioCap, String aDescrUfficioCap, BigDecimal aAnnoRegeCasap,
			BigDecimal aNumeroRegeCasap, String aCodUfficioCasap, String aDescrUfficioCasap,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, BigDecimal aIdMisuraCautelare, BigDecimal aIdAnnotazioneManuale,
			BigDecimal aIdProvvBdmc, String aStatoTrasmissioneIsc, String aStatoTrasmissioneVal,
			Date aDataInizioUsata, Date aDataFineUsata// ,
	/* String aDescrUfficioAggiornamento */) {
		this.mIdMisuraCautelareBdmc = aIdMisuraCautelareBdmc;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mEveIdEvento = aEveIdEvento;
		this.mPenResIdPenaResidua = aPenResIdPenaResidua;
		this.mIdPren = aIdPren;
		this.mProgPeriPres = aProgPeriPres;
		this.mFlagCaricamento = aFlagCaricamento;
		this.mFlagStato = aFlagStato;
		this.mCodTipoMisura = aCodTipoMisura;
		this.mDescrTipoMisura = aDescrTipoMisura;
		this.mDataInizio = aDataInizio;
		this.mDataFine = aDataFine;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mAltroLuogoDetenzione = aAltroLuogoDetenzione;
		this.mFlagComputabile = aFlagComputabile;
		this.mCodMotivoNonComputabile = aCodMotivoNonComputabile;
		this.mDescrMotivoNonComputabile = aDescrMotivoNonComputabile;
		this.mCodTipoUfficioRifer = aCodTipoUfficioRifer;
		this.mDescrTipoUfficioRifer = aDescrTipoUfficioRifer;
		this.mCodLuogoUfficioRifer = aCodLuogoUfficioRifer;
		this.mDescrLuogoUfficioRifer = aDescrLuogoUfficioRifer;
		this.mDataComputo = aDataComputo;
		this.mAnnoFascSiep = aAnnoFascSiep;
		this.mNumeFascSiep = aNumeFascSiep;
		this.mNote = aNote;
		this.mAnnoFascBdmc = aAnnoFascBdmc;
		this.mNumeFascBdmc = aNumeFascBdmc;
		this.mCodUfficioBdmc = aCodUfficioBdmc;
		this.mDescrUfficioBdmc = aDescrUfficioBdmc;
		this.mAnnoRgnr = aAnnoRgnr;
		this.mNumeRgnr = aNumeRgnr;
		this.mCodUfficioRgnr = aCodUfficioRgnr;
		this.mDescrUfficioRgnr = aDescrUfficioRgnr;
		this.mAnnoRegeGip = aAnnoRegeGip;
		this.mNumeroRegeGip = aNumeroRegeGip;
		this.mCodUfficioGip = aCodUfficioGip;
		this.mDescrUfficioGip = aDescrUfficioGip;
		this.mAnnoRegeDib = aAnnoRegeDib;
		this.mNumeroRegeDib = aNumeroRegeDib;
		this.mCodUfficioDib = aCodUfficioDib;
		this.mDescrUfficioDib = aDescrUfficioDib;
		this.mAnnoRegeCas = aAnnoRegeCas;
		this.mNumeroRegeCas = aNumeroRegeCas;
		this.mCodUfficioCas = aCodUfficioCas;
		this.mDescrUfficioCas = aDescrUfficioCas;
		this.mAnnoRegeCap = aAnnoRegeCap;
		this.mNumeroRegeCap = aNumeroRegeCap;
		this.mCodUfficioCap = aCodUfficioCap;
		this.mDescrUfficioCap = aDescrUfficioCap;
		this.mAnnoRegeCasap = aAnnoRegeCasap;
		this.mNumeroRegeCasap = aNumeroRegeCasap;
		this.mCodUfficioCasap = aCodUfficioCasap;
		this.mDescrUfficioCasap = aDescrUfficioCasap;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		// this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mIdMisuraCautelare = aIdMisuraCautelare;
		this.mIdAnnotazioneManuale = aIdAnnotazioneManuale;
		this.mIdProvvBdmc = aIdProvvBdmc;
		this.mStatoTrasmissioneIsc = aStatoTrasmissioneIsc;
		this.mStatoTrasmissioneVal = aStatoTrasmissioneVal;
		this.mDataInizioUsata = aDataInizioUsata;
		this.mDataFineUsata = aDataFineUsata;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdMisuraCautelareBdmc() {
		return mIdMisuraCautelareBdmc;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getPenResIdPenaResidua() {
		return mPenResIdPenaResidua;
	}

	public BigDecimal getIdPren() {
		return mIdPren;
	}

	public BigDecimal getProgPeriPres() {
		return mProgPeriPres;
	}

	public String getFlagCaricamento() {
		return mFlagCaricamento;
	}

	public String getFlagStato() {
		return mFlagStato;
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

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public String getAltroLuogoDetenzione() {
		return mAltroLuogoDetenzione;
	}

	public String getFlagComputabile() {
		return mFlagComputabile;
	}

	public String getCodMotivoNonComputabile() {
		return mCodMotivoNonComputabile;
	}

	public String getDescrMotivoNonComputabile() {
		return mDescrMotivoNonComputabile;
	}

	public String getCodTipoUfficioRifer() {
		return mCodTipoUfficioRifer;
	}

	public String getDescrTipoUfficioRifer() {
		return mDescrTipoUfficioRifer;
	}

	public String getCodLuogoUfficioRifer() {
		return mCodLuogoUfficioRifer;
	}

	public String getDescrLuogoUfficioRifer() {
		return mDescrLuogoUfficioRifer;
	}

	public Date getDataComputo() {
		return mDataComputo;
	}

	public BigDecimal getAnnoFascSiep() {
		return mAnnoFascSiep;
	}

	public BigDecimal getNumeFascSiep() {
		return mNumeFascSiep;
	}

	public String getNote() {
		return mNote;
	}

	public BigDecimal getAnnoFascBdmc() {
		return mAnnoFascBdmc;
	}

	public BigDecimal getNumeFascBdmc() {
		return mNumeFascBdmc;
	}

	public String getCodUfficioBdmc() {
		return mCodUfficioBdmc;
	}

	public String getDescrUfficioBdmc() {
		return mDescrUfficioBdmc;
	}

	public BigDecimal getAnnoRgnr() {
		return mAnnoRgnr;
	}

	public BigDecimal getNumeRgnr() {
		return mNumeRgnr;
	}

	public String getCodUfficioRgnr() {
		return mCodUfficioRgnr;
	}

	public String getDescrUfficioRgnr() {
		return mDescrUfficioRgnr;
	}

	public BigDecimal getAnnoRegeGip() {
		return mAnnoRegeGip;
	}

	public BigDecimal getNumeroRegeGip() {
		return mNumeroRegeGip;
	}

	public String getCodUfficioGip() {
		return mCodUfficioGip;
	}

	public String getDescrUfficioGip() {
		return mDescrUfficioGip;
	}

	public BigDecimal getAnnoRegeDib() {
		return mAnnoRegeDib;
	}

	public BigDecimal getNumeroRegeDib() {
		return mNumeroRegeDib;
	}

	public String getCodUfficioDib() {
		return mCodUfficioDib;
	}

	public String getDescrUfficioDib() {
		return mDescrUfficioDib;
	}

	public BigDecimal getAnnoRegeCas() {
		return mAnnoRegeCas;
	}

	public BigDecimal getNumeroRegeCas() {
		return mNumeroRegeCas;
	}

	public String getCodUfficioCas() {
		return mCodUfficioCas;
	}

	public String getDescrUfficioCas() {
		return mDescrUfficioCas;
	}

	public BigDecimal getAnnoRegeCap() {
		return mAnnoRegeCap;
	}

	public BigDecimal getNumeroRegeCap() {
		return mNumeroRegeCap;
	}

	public String getCodUfficioCap() {
		return mCodUfficioCap;
	}

	public String getDescrUfficioCap() {
		return mDescrUfficioCap;
	}

	public BigDecimal getAnnoRegeCasap() {
		return mAnnoRegeCasap;
	}

	public BigDecimal getNumeroRegeCasap() {
		return mNumeroRegeCasap;
	}

	public String getCodUfficioCasap() {
		return mCodUfficioCasap;
	}

	public String getDescrUfficioCasap() {
		return mDescrUfficioCasap;
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

	public BigDecimal getIdMisuraCautelare() {
		return mIdMisuraCautelare;
	}

	public BigDecimal getIdAnnotazioneManuale() {
		return mIdAnnotazioneManuale;
	}

	public BigDecimal getIdProvvBdmc() {
		return mIdProvvBdmc;
	}

	public String getStatoTrasmissioneIsc() {
		return mStatoTrasmissioneIsc;
	}

	public String getStatoTrasmissioneVal() {
		return mStatoTrasmissioneVal;
	}

	public Date getDataInizioUsata() {
		return mDataInizioUsata;
	}

	public Date getDataFineUsata() {
		return mDataFineUsata;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdMisuraCautelareBdmc(BigDecimal aValore) {
		mIdMisuraCautelareBdmc = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setPenResIdPenaResidua(BigDecimal aValore) {
		mPenResIdPenaResidua = aValore;
	}

	public void setIdPren(BigDecimal aValore) {
		mIdPren = aValore;
	}

	public void setProgPeriPres(BigDecimal aValore) {
		mProgPeriPres = aValore;
	}

	public void setFlagCaricamento(String aValore) {
		mFlagCaricamento = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
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

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setAltroLuogoDetenzione(String aValore) {
		mAltroLuogoDetenzione = aValore;
	}

	public void setFlagComputabile(String aValore) {
		mFlagComputabile = aValore;
	}

	public void setCodMotivoNonComputabile(String aValore) {
		mCodMotivoNonComputabile = aValore;
	}

	public void setDescrMotivoNonComputabile(String aValore) {
		mDescrMotivoNonComputabile = aValore;
	}

	public void setCodTipoUfficioRifer(String aValore) {
		mCodTipoUfficioRifer = aValore;
	}

	public void setDescrTipoUfficioRifer(String aValore) {
		mDescrTipoUfficioRifer = aValore;
	}

	public void setCodLuogoUfficioRifer(String aValore) {
		mCodLuogoUfficioRifer = aValore;
	}

	public void setDescrLuogoUfficioRifer(String aValore) {
		mDescrLuogoUfficioRifer = aValore;
	}

	public void setDataComputo(Date aValore) {
		mDataComputo = aValore;
	}

	public void setAnnoFascSiep(BigDecimal aValore) {
		mAnnoFascSiep = aValore;
	}

	public void setNumeFascSiep(BigDecimal aValore) {
		mNumeFascSiep = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		mAnnoFascBdmc = aValore;
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		mNumeFascBdmc = aValore;
	}

	public void setCodUfficioBdmc(String aValore) {
		mCodUfficioBdmc = aValore;
	}

	public void setDescrUfficioBdmc(String aValore) {
		mDescrUfficioBdmc = aValore;
	}

	public void setAnnoRgnr(BigDecimal aValore) {
		mAnnoRgnr = aValore;
	}

	public void setNumeRgnr(BigDecimal aValore) {
		mNumeRgnr = aValore;
	}

	public void setCodUfficioRgnr(String aValore) {
		mCodUfficioRgnr = aValore;
	}

	public void setDescrUfficioRgnr(String aValore) {
		mDescrUfficioRgnr = aValore;
	}

	public void setAnnoRegeGip(BigDecimal aValore) {
		mAnnoRegeGip = aValore;
	}

	public void setNumeroRegeGip(BigDecimal aValore) {
		mNumeroRegeGip = aValore;
	}

	public void setCodUfficioGip(String aValore) {
		mCodUfficioGip = aValore;
	}

	public void setDescrUfficioGip(String aValore) {
		mDescrUfficioGip = aValore;
	}

	public void setAnnoRegeDib(BigDecimal aValore) {
		mAnnoRegeDib = aValore;
	}

	public void setNumeroRegeDib(BigDecimal aValore) {
		mNumeroRegeDib = aValore;
	}

	public void setCodUfficioDib(String aValore) {
		mCodUfficioDib = aValore;
	}

	public void setDescrUfficioDib(String aValore) {
		mDescrUfficioDib = aValore;
	}

	public void setAnnoRegeCas(BigDecimal aValore) {
		mAnnoRegeCas = aValore;
	}

	public void setNumeroRegeCas(BigDecimal aValore) {
		mNumeroRegeCas = aValore;
	}

	public void setCodUfficioCas(String aValore) {
		mCodUfficioCas = aValore;
	}

	public void setDescrUfficioCas(String aValore) {
		mDescrUfficioCas = aValore;
	}

	public void setAnnoRegeCap(BigDecimal aValore) {
		mAnnoRegeCap = aValore;
	}

	public void setNumeroRegeCap(BigDecimal aValore) {
		mNumeroRegeCap = aValore;
	}

	public void setCodUfficioCap(String aValore) {
		mCodUfficioCap = aValore;
	}

	public void setDescrUfficioCap(String aValore) {
		mDescrUfficioCap = aValore;
	}

	public void setAnnoRegeCasap(BigDecimal aValore) {
		mAnnoRegeCasap = aValore;
	}

	public void setNumeroRegeCasap(BigDecimal aValore) {
		mNumeroRegeCasap = aValore;
	}

	public void setCodUfficioCasap(String aValore) {
		mCodUfficioCasap = aValore;
	}

	public void setDescrUfficioCasap(String aValore) {
		mDescrUfficioCasap = aValore;
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

	public void setIdMisuraCautelare(BigDecimal aValore) {
		mIdMisuraCautelare = aValore;
	}

	public void setIdAnnotazioneManuale(BigDecimal aValore) {
		mIdAnnotazioneManuale = aValore;
	}

	public void setIdProvvBdmc(BigDecimal aValore) {
		mIdProvvBdmc = aValore;
	}

	public void setStatoTrasmissioneIsc(String aValore) {
		mStatoTrasmissioneIsc = aValore;
	}

	public void setStatoTrasmissioneVal(String aValore) {
		mStatoTrasmissioneVal = aValore;
	}

	public void setDataInizioUsata(Date aValore) {
		mDataInizioUsata = aValore;
	}

	public void setDataFineUsata(Date aValore) {
		mDataFineUsata = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "MisuraCautelareBdmcModel:\n" + "[ mIdMisuraCautelareBdmc          = " + mIdMisuraCautelareBdmc
				+ " ]\n" + "[ mIdMisuraCautelare          = " + mIdMisuraCautelare + " ]\n"
				+ "[ mIdAnnotazioneManuale         = " + mIdAnnotazioneManuale + " ]\n"
				+ "[ mIdProvvBdmc         = " + mIdProvvBdmc + " ]\n" + "[ mStatoTrasmissioneIsc         = "
				+ mStatoTrasmissioneIsc + " ]\n" + "[ mStatoTrasmissioneVal         = "
				+ mStatoTrasmissioneVal + " ]\n" + "[ mDataInizioUsata          = " + mDataInizioUsata
				+ " ]\n" + "[ mDataFineUsata          = " + mDataFineUsata + " ]\n"
				+ "[ mFasSieIdFascicoloSiep      = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mSogIdSoggetto              = " + mSogIdSoggetto + " ]\n"
				+ "[ mEveIdEvento                = " + mEveIdEvento + " ]\n"
				+ "[ mPenResIdPenaResidua        = " + mPenResIdPenaResidua + " ]\n"
				+ "[ mIdPren                     = " + mIdPren + " ]\n" + "[ mProgPeriPres               = "
				+ mProgPeriPres + " ]\n" + "[ mFlagCaricamento            = " + mFlagCaricamento + " ]\n"
				+ "[ mFlagStato                  = " + mFlagStato + " ]\n"
				+ "[ mCodTipoMisura              = " + mCodTipoMisura + " ]\n"
				+ "[ mDataInizio                 = " + mDataInizio + " ]\n"
				+ "[ mDataFine                   = " + mDataFine + " ]\n" + "[ mNumAnni                    = "
				+ mNumAnni + " ]\n" + "[ mNumMesi                    = " + mNumMesi + " ]\n"
				+ "[ mNumGiorni                  = " + mNumGiorni + " ]\n"
				+ "[ mIstDetIdIstitutoDetenzione = " + mIstDetIdIstitutoDetenzione + " ]\n"
				+ "[ mAltroLuogoDetenzione       = " + mAltroLuogoDetenzione + " ]\n"
				+ "[ mFlagComputabile            = " + mFlagComputabile + " ]\n"
				+ "[ mCodMotivoNonComputabile    = " + mCodMotivoNonComputabile + " ]\n"
				+ "[ mCodTipoUfficioRifer        = " + mCodTipoUfficioRifer + " ]\n"
				+ "[ mCodLuogoUfficioRifer       = " + mCodLuogoUfficioRifer + " ]\n"
				+ "[ mDataComputo                = " + mDataComputo + " ]\n"
				+ "[ mAnnoFascSiep               = " + mAnnoFascSiep + " ]\n"
				+ "[ mNumeFascSiep               = " + mNumeFascSiep + " ]\n"
				+ "[ mNote                       = " + mNote + " ]\n" + "[ mAnnoFascBdmc               = "
				+ mAnnoFascBdmc + " ]\n" + "[ mNumeFascBdmc               = " + mNumeFascBdmc + " ]\n"
				+ "[ mCodUfficioBdmc             = " + mCodUfficioBdmc + " ]\n"
				+ "[ mAnnoRgnr                   = " + mAnnoRgnr + " ]\n" + "[ mNumeRgnr                   = "
				+ mNumeRgnr + " ]\n" + "[ mCodUfficioRgnr             = " + mCodUfficioRgnr + " ]\n"
				+ "[ mAnnoRegeGip                = " + mAnnoRegeGip + " ]\n"
				+ "[ mNumeroRegeGip              = " + mNumeroRegeGip + " ]\n"
				+ "[ mCodUfficioGip              = " + mCodUfficioGip + " ]\n"
				+ "[ mAnnoRegeDib                = " + mAnnoRegeDib + " ]\n"
				+ "[ mNumeroRegeDib              = " + mNumeroRegeDib + " ]\n"
				+ "[ mCodUfficioDib              = " + mCodUfficioDib + " ]\n"
				+ "[ mAnnoRegeCas                = " + mAnnoRegeCas + " ]\n"
				+ "[ mNumeroRegeCas              = " + mNumeroRegeCas + " ]\n"
				+ "[ mCodUfficioCas              = " + mCodUfficioCas + " ]\n"
				+ "[ mAnnoRegeCap                = " + mAnnoRegeCap + " ]\n"
				+ "[ mNumeroRegeCap              = " + mNumeroRegeCap + " ]\n"
				+ "[ mCodUfficioCap              = " + mCodUfficioCap + " ]\n"
				+ "[ mAnnoRegeCasap              = " + mAnnoRegeCasap + " ]\n"
				+ "[ mNumeroRegeCasap            = " + mNumeroRegeCasap + " ]\n"
				+ "[ mCodUfficioCasap            = " + mCodUfficioCasap + " ]\n"
				+ "[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}
}
