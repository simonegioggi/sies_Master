package siap.sico.webservice.model;

import it.mig.sies.type.OMONIMODocument;
import it.mig.sippi.service.richiestacertificato.type.OMONIMO;

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class OmonimiModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1455375422165162739L;

	private OMONIMODocument.OMONIMO mArrayOmonimi;
	private String mDescLuogoNascita;
	private String mProvNascita;
	private String mDescStatoEstero;
	private BigDecimal mIDCertificatoOmonimiNSc;
	private OMONIMO mArrayOmonimia;

	// Costruttore
	public OmonimiModel() {
		this.mArrayOmonimi = null;
		this.mDescLuogoNascita = null;
		this.mProvNascita = null;
		this.mDescStatoEstero = null;
		this.mIDCertificatoOmonimiNSc = null;
		this.mArrayOmonimia = null;
	}

	// METODI GET
	public OMONIMODocument.OMONIMO getOMONIMODocument() {
		return mArrayOmonimi;
	}

	public String getDescLuogoNascita() {
		return mDescLuogoNascita;
	}

	public String getProvNascita() {
		return mProvNascita;
	}

	public String getDescStatoEstero() {
		return mDescStatoEstero;
	}

	public BigDecimal getIDCertificatoOmonimiNsc() {
		return mIDCertificatoOmonimiNSc;
	}

	public OMONIMO getOMONIMO() {
		return mArrayOmonimia;
	}

	// METODI SET
	public void setOMONIMODocument(OMONIMODocument.OMONIMO aValore) {
		mArrayOmonimi = aValore;
	}

	public void setDescLuogoNascita(String aValore) {
		mDescLuogoNascita = aValore;
	}

	public void setProvNascita(String aValore) {
		mProvNascita = aValore;
	}

	public void setDescStatoEstero(String aValore) {
		mDescStatoEstero = aValore;
	}

	public void setIDCertificatoOmonimiNsc(BigDecimal aValore) {
		mIDCertificatoOmonimiNSc = aValore;
	}

	public void setOMONIMO(OMONIMO aValore) {
		mArrayOmonimia = aValore;
	}

}