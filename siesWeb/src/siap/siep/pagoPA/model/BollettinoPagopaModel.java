package siap.siep.pagoPA.model;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * MEV_2023-13: aggiunta classe model per pagoPA
 *
 * @author sgioggi
 * @version 1.0
 */
public class BollettinoPagopaModel extends GenericModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3481007881258575439L;

	private BigDecimal mIdBollettinoPagopa;
	private int mProgRata;
	private int mNumeroRate;
	private String mTipoRateizzazione;
	private String mIuv;
	private BigDecimal mImportoRata;
	private BigDecimal mImportoPagato;
	private Date mDataAvvPagamento;
	private Date mDataScadenza;
	private Date mDataScadenzaRich;
	private String mStatoPagamento;
	private ByteArrayInputStream mDocBollBlob;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicolSiep;
	private BigDecimal mRatIdRateizzazionePP;
	private String mDescrTipoRateizzazione;
	private String mDescrStatoPagamento;
	private Date mDataUltimoControllo;
	private String mCodiceFiscale;
    private String mStatoPagopa;
    private String mErrorePagopa;
    private String mCodiceDistretto;
    
	// COSTRUTTORE DI DEFAULT
	public BollettinoPagopaModel() {

		this.mIdBollettinoPagopa = null;
		this.mProgRata = 0;
		this.mNumeroRate = 0;
		this.mTipoRateizzazione = "";
		this.mIuv = "";
		this.mImportoRata = null;
		this.mImportoPagato = null;
		this.mDataAvvPagamento = null;
		this.mDataScadenza = null;
		this.mDataScadenzaRich = null;
		this.mStatoPagamento = "";
		this.mDocBollBlob = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mFasSieIdFascicolSiep = null;
		this.mRatIdRateizzazionePP = null;
		this.mDescrTipoRateizzazione = "";
		this.mDescrStatoPagamento = "";
		this.mDataUltimoControllo = null;
		this.mCodiceFiscale = null;		
		this.mStatoPagopa = null;
		this.mErrorePagopa = null;
		this.mCodiceDistretto = null;
	}

	// COSTRUTTORE DI COPIA
	public BollettinoPagopaModel(BollettinoPagopaModel aModel) {

		this.mIdBollettinoPagopa = aModel.mIdBollettinoPagopa;
		this.mProgRata = aModel.mProgRata;
		this.mNumeroRate = aModel.mNumeroRate;
		this.mTipoRateizzazione = aModel.mTipoRateizzazione;
		this.mIuv = aModel.mIuv;
		this.mImportoRata = aModel.mImportoRata;
		this.mImportoPagato = aModel.mImportoPagato;
		this.mDataAvvPagamento = aModel.mDataAvvPagamento;
		this.mDataScadenza = aModel.mDataScadenza;
		this.mDataScadenzaRich = aModel.mDataScadenzaRich;
		this.mStatoPagamento = aModel.mStatoPagamento;
		this.mDocBollBlob = aModel.mDocBollBlob;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mFasSieIdFascicolSiep = aModel.mFasSieIdFascicolSiep;
		this.mRatIdRateizzazionePP = aModel.mRatIdRateizzazionePP;
		this.mDescrTipoRateizzazione = aModel.mDescrTipoRateizzazione;
		this.mDescrStatoPagamento = aModel.mDescrStatoPagamento;
		this.mDataUltimoControllo = aModel.mDataUltimoControllo;
		this.mCodiceFiscale = aModel.mCodiceFiscale;
	    this.mStatoPagopa = aModel.mStatoPagopa;
	    this.mErrorePagopa = aModel.mErrorePagopa;
	    this.mCodiceDistretto = aModel.mCodiceDistretto;
	}

	// COSTRUTTORE MODEL
	public BollettinoPagopaModel(BigDecimal aIdBollettinoPagopa, int aProgRata, int aNumeroRate,
			String aTipoRateizzazione, String aIuv, BigDecimal aImportoRata, BigDecimal aImportoPagato,
			Date aDataAvvPagamento, Date aDataScadenza, Date aDataScadenzaRich, String aStatoPagamento,
			String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, BigDecimal aFasSieIdFascicolSiep,
			BigDecimal aRatIdRateizzazionePP, Date aDataUltimoControllo, String aCodiceFiscale,
			String aStatoPagopa, String aErrorePagopa, String aCodiceDistretto		
			,  String aDescrTipoRateizzazione, String aDescrStatoPagamento) {

		this.mIdBollettinoPagopa = aIdBollettinoPagopa;
		this.mProgRata = aProgRata;
		this.mNumeroRate = aNumeroRate;
		this.mTipoRateizzazione = aTipoRateizzazione;
		this.mIuv = aIuv;
		this.mImportoRata = aImportoRata;
		this.mImportoPagato = aImportoPagato;
		this.mDataAvvPagamento = aDataAvvPagamento;
		this.mDataScadenza = aDataScadenza;
		this.mDataScadenzaRich = aDataScadenzaRich;
		if (aStatoPagamento != null)
			this.mStatoPagamento = aStatoPagamento.toUpperCase();
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mFasSieIdFascicolSiep = aFasSieIdFascicolSiep;
		this.mRatIdRateizzazionePP = aRatIdRateizzazionePP;
		this.mDescrTipoRateizzazione = aDescrTipoRateizzazione;
		this.mDescrStatoPagamento = aDescrStatoPagamento;
		this.mDataUltimoControllo = aDataUltimoControllo;
		this.mCodiceFiscale = aCodiceFiscale;
		this.mStatoPagopa = aStatoPagopa;
		this.mErrorePagopa = aErrorePagopa;
		this.mCodiceDistretto = aCodiceDistretto;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdBollettinoPagopa() {
		return mIdBollettinoPagopa;
	}

	public int getProgRata() {
		return mProgRata;
	}

	public int getNumeroRate() {
		return mNumeroRate;
	}

	public String getTipoRateizzazione() {
		return mTipoRateizzazione;
	}

	public String getIuv() {
		return mIuv;
	}

	public BigDecimal getImportoRata() {
		return mImportoRata;
	}

	public BigDecimal getImportoPagato() {
		return mImportoPagato;
	}

	public Date getDataAvvPagamento() {
		return mDataAvvPagamento;
	}

	public Date getDataScadenza() {
		return mDataScadenza;
	}

	public Date getDataScadenzaRich() {
		return mDataScadenzaRich;
	}

	public String getStatoPagamento() {
		return mStatoPagamento;
	}

	public ByteArrayInputStream getDocBollBlob() {
		return mDocBollBlob;
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

	public BigDecimal getFasSieIdFascicolSiep() {
		return mFasSieIdFascicolSiep;
	}

	public BigDecimal getRatIdRateizzazionePP() {
		return mRatIdRateizzazionePP;
	}

	public String getDescrTipoRateizzazione() {
		return mDescrTipoRateizzazione;
	}
	public String getDescrStatoPagamento() {
		return mDescrStatoPagamento;
	}
	
    public Date getDataUltimoControllo() {
        return mDataUltimoControllo;
    }
    
    public String getCodiceFiscale() {
        return mCodiceFiscale;
    }
    
    public String getStatoPagopa() {
        return mStatoPagopa;
    }
    
    public String getErrorePagopa() {
        return mErrorePagopa;
    }

    public String getCodiceDistretto() {
        return mCodiceDistretto;
    }
	//
	// METODI SET()
	//
	public void setIdBollettinoPagopa(BigDecimal aValore) {
		mIdBollettinoPagopa = aValore;
	}

	public void setProgRata(int aValore) {
		mProgRata = aValore;
	}

	public void setNumeroRate(int aValore) {
		mNumeroRate = aValore;
	}

	public void setTipoRateizzazione(String aValore) {
		mTipoRateizzazione = aValore;
	}

	public void setIuv(String aValore) {
		mIuv = aValore;
	}

	public void setImportoRata(BigDecimal aValore) {
		mImportoRata = aValore;
	}

	public void setImportoPagato(BigDecimal aValore) {
		mImportoPagato = aValore;
	}

	public void setDataAvvPagamento(Date aValore) {
		mDataAvvPagamento = aValore;
	}

	public void setDataScadenza(Date aValore) {
		mDataScadenza = aValore;
	}

	public void setDataScadenzaRich(Date aValore) {
		mDataScadenzaRich = aValore;
	}

	public void setStatoPagamento(String aValore) {
		if (aValore != null)
			mStatoPagamento = aValore.toUpperCase();
	}

	public void setDocBollBlob(ByteArrayInputStream aValore) {
		mDocBollBlob = aValore;
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

	public void setFasSieIdFascicolSiep(BigDecimal mFasSieIdFascicolSiep) {
		this.mFasSieIdFascicolSiep = mFasSieIdFascicolSiep;
	}

	public void setRatIdRateizzazionePP(BigDecimal mRatIdRateizzazionePP) {
		this.mRatIdRateizzazionePP = mRatIdRateizzazionePP;
	}

	public void setDescrTipoRateizzazione(String mDescrTipoRateizzazione) {
		this.mDescrTipoRateizzazione = mDescrTipoRateizzazione;
	}

	public void setDescrStatoPagamento(String mDescrStatoPagamento) {
		this.mDescrStatoPagamento = mDescrStatoPagamento;
	}

   public void setDataUltimoControllo(Date aValore) {
        this.mDataUltimoControllo = aValore;
   }
   
   public void setCodiceFiscale(String aValore) {
       this.mCodiceFiscale = aValore;
   }
   
   public void setStatoPagopa(String aValore) {
       this.mStatoPagopa = aValore;
   }
   
   public void setErrorePagopa(String aValore) {
       this.mErrorePagopa = aValore;
   }
   
   public void setCodiceDistretto(String aValore) {
       this.mCodiceDistretto= aValore;
   }
}