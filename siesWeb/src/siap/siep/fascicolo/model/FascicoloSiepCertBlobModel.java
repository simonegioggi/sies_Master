package siap.siep.fascicolo.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

public class FascicoloSiepCertBlobModel extends FascicoloSiepModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7429385028414758511L;

	// MEV 12 (Richiesta Certificato Penale)
	private ByteArrayInputStream mCertPenaleBlobIn;
	private ByteArrayOutputStream mCertPenaleBlobOut;
	private BigDecimal mLengthCertPenaleBlob;
	private FascicoloSiepModel fascicoloSiep;

	public ByteArrayInputStream ritornaCertPenaleBlobIn() {
		return mCertPenaleBlobIn;
	}

	public ByteArrayOutputStream ritornaCertPenaleBlobOut() {
		return mCertPenaleBlobOut;
	}

	public BigDecimal getLengthCertPenaleBlob() {
		return mLengthCertPenaleBlob;
	}

	public FascicoloSiepModel getFascicoloSiep() {
		return fascicoloSiep;
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

	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		fascicoloSiep = aValore;
	}

}