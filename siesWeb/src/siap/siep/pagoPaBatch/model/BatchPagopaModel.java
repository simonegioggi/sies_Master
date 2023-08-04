package siap.siep.pagoPaBatch.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * Batch per PagoPA
 *
 * @author d.fiorletta
 * @since MEV_2023-13
 * @version 1.0
 */
public class BatchPagopaModel extends GenericModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3481007881258575439L;

	private BigDecimal mIdBatchPagopa;
	private Date mDataInizioEsecuzione;
	private Date mDataFineEsecuzione;
	private BigDecimal mNumPosDebitorieVerificate;
	private BigDecimal mNumBollettiniAggiornati;

	private String mEsitoEsecuzione;
	private String mErroreEsecuzione;

	// COSTRUTTORE DI DEFAULT
	public BatchPagopaModel() {
		this.mIdBatchPagopa = null;
		this.mDataInizioEsecuzione = null;
		this.mDataFineEsecuzione = null;
		this.mNumPosDebitorieVerificate = null;
		this.mNumBollettiniAggiornati = null;
		this.mEsitoEsecuzione = null;
		this.mErroreEsecuzione = null;
	}

	// COSTRUTTORE DI COPIA
	public BatchPagopaModel(BatchPagopaModel aModel) {

		this.mIdBatchPagopa = aModel.mIdBatchPagopa;
		this.mDataInizioEsecuzione = aModel.mDataInizioEsecuzione;
		this.mDataFineEsecuzione = aModel.mDataFineEsecuzione;
		this.mNumPosDebitorieVerificate = aModel.mNumPosDebitorieVerificate;
		this.mNumBollettiniAggiornati = aModel.mNumBollettiniAggiornati;
		this.mEsitoEsecuzione = aModel.mEsitoEsecuzione;
		this.mErroreEsecuzione = aModel.mErroreEsecuzione;
	}

	// COSTRUTTORE MODEL
	/*
	 * public BatchPagopaModel(BigDecimal aIdBollettinoPagopa, int aProgRata, int aNumeroRate, String
	 * aTipoRateizzazione, String aIuv, BigDecimal aImportoRata, BigDecimal aImportoPagato, Date
	 * aDataAvvPagamento, Date aDataScadenza, Date aDataScadenzaRich, String aStatoPagamento, String
	 * aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento, String
	 * aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento, BigDecimal
	 * aFasSieIdFascicolSiep, BigDecimal aRatIdRateizzazionePP, Date aDataUltimoControllo, String
	 * aCodiceFiscale, String aStatoPagopa, String aErrorePagopa , String aDescrTipoRateizzazione, String
	 * aDescrStatoPagamento) {
	 * 
	 * this.mIdBollettinoPagopa = aIdBollettinoPagopa; this.mProgRata = aProgRata; this.mNumeroRate =
	 * aNumeroRate; this.mTipoRateizzazione = aTipoRateizzazione; this.mIuv = aIuv; this.mImportoRata =
	 * aImportoRata; }
	 */
	public BigDecimal getIdBatchPagopa() {
		return mIdBatchPagopa;
	}

	public Date getDataInizioEsecuzione() {
		return mDataInizioEsecuzione;
	}

	public Date getDataFineEsecuzione() {
		return mDataFineEsecuzione;
	}

	public BigDecimal getNumPosDebitorieVerificate() {
		return mNumPosDebitorieVerificate;
	}

	public BigDecimal getNumBollettiniAggiornati() {
		return mNumBollettiniAggiornati;
	}

	public String getEsitoEsecuzione() {
		return mEsitoEsecuzione;
	}

	public String getErroreEsecuzione() {
		return mErroreEsecuzione;
	}

	// SETTER
	public void setIdBatchPagopa(BigDecimal mIdBatchPagopa) {
		this.mIdBatchPagopa = mIdBatchPagopa;
	}

	public void setDataInizioEsecuzione(Date mDataInizioEsecuzione) {
		this.mDataInizioEsecuzione = mDataInizioEsecuzione;
	}

	public void setDataFineEsecuzione(Date mDataFineEsecuzione) {
		this.mDataFineEsecuzione = mDataFineEsecuzione;
	}

	public void setNumPosDebitorieVerificate(BigDecimal mNumPosDebitorieVerificate) {
		this.mNumPosDebitorieVerificate = mNumPosDebitorieVerificate;
	}

	public void setNumBollettiniAggiornati(BigDecimal mNumBollettiniAggiornati) {
		this.mNumBollettiniAggiornati = mNumBollettiniAggiornati;
	}

	public void setEsitoEsecuzione(String mEsitoEsecuzione) {
		this.mEsitoEsecuzione = mEsitoEsecuzione;
	}

	public void setErroreEsecuzione(String mErroreEsecuzione) {
		this.mErroreEsecuzione = mErroreEsecuzione;
	}
	
	/*
	 * Restituisce la durata del batch in formato hh:mm:ss,mmm
	 */
	public String getDurataAsString() {
	    String durataStr = "n.d.";
	    if (mDataInizioEsecuzione!=null && mDataFineEsecuzione!=null) {
	        durataStr = DateUtils.getTempoEsecuzione(mDataInizioEsecuzione,mDataFineEsecuzione);
	    }
	        
	    return durataStr;
	}
	
	public String toString() {
		String lStr = new String();

		lStr = "BatchPagopaModel:\n" + "[ mIdBatchPagopa             = " + mIdBatchPagopa + " ]\n"
				+ "[ mDataInizioEsecuzione      = " + mDataInizioEsecuzione + " ]\n"
				+ "[ mDataFineEsecuzione        = " + mDataFineEsecuzione + " ]\n"
				+ "[ mNumPosDebitorieVerificate = " + mNumPosDebitorieVerificate + " ]\n"
				+ "[ mNumBollettiniAggiornati   = " + mNumBollettiniAggiornati + " ]\n"
				+ "[ mEsitoEsecuzione           = " + mEsitoEsecuzione + " ]\n"
				+ "[ mErroreEsecuzione          = " + mErroreEsecuzione + " ]";

		return lStr;
	}

}