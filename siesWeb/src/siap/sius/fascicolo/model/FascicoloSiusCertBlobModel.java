package siap.sius.fascicolo.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

public class FascicoloSiusCertBlobModel extends FascicoloSiusModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1166181679452085968L;

	// MEV 12 (Richiesta Certificato Penale)
	private ByteArrayInputStream mCertPenaleBlobIn;
	private ByteArrayOutputStream mCertPenaleBlobOut;
	private BigDecimal mLengthCertPenaleBlob;
	private FascicoloSiusModel fascicoloSius;

	public ByteArrayInputStream ritornaCertPenaleBlobIn() {
		return mCertPenaleBlobIn;
	}

	public ByteArrayOutputStream ritornaCertPenaleBlobOut() {
		return mCertPenaleBlobOut;
	}

	public BigDecimal getLengthCertPenaleBlob() {
		return mLengthCertPenaleBlob;
	}

	public FascicoloSiusModel getFascicoloSius() {
		return fascicoloSius;
	}

	public void caricaCertPenaleBlobIn(ByteArrayInputStream aValore) {
		mCertPenaleBlobIn = aValore;
	}

	public void caricaCertPenaleBlobOut(ByteArrayOutputStream aValore) {
		mCertPenaleBlobOut = aValore;
	}

	public void setLengthCertPenaleBlob(BigDecimal aValore) {
		mLengthCertPenaleBlob = aValore;
	}

	public void setFascicoloSius(FascicoloSiusModel aValore) {
		fascicoloSius = aValore;
	}

}