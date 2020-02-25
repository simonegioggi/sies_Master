package siap.sico.certificato_omonimi_nsc.model;

/**
* <p>Title: CertificatoOmonimiNscModel</p>
* <p>Description: Classe Model che rappresenta il CertificatoOmonimiNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class CertificatoOmonimiNscModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4375386795694254353L;

	private BigDecimal mIdCertificatoOmonimi;
	private Date mDataInserimento;
	private ByteArrayInputStream mDocBlobCertificato;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public CertificatoOmonimiNscModel() {
		this.mIdCertificatoOmonimi = null;
		this.mDataInserimento = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public CertificatoOmonimiNscModel(CertificatoOmonimiNscModel aModel) {
		this.mIdCertificatoOmonimi = aModel.mIdCertificatoOmonimi;
		this.mDataInserimento = aModel.mDataInserimento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public CertificatoOmonimiNscModel(BigDecimal aIdCertificatoOmonimi, Date aDataInserimento) {
		this.mIdCertificatoOmonimi = aIdCertificatoOmonimi;
		this.mDataInserimento = aDataInserimento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdCertificatoOmonimi() {
		return mIdCertificatoOmonimi;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public ByteArrayInputStream getDocBlobCertificato() {
		return mDocBlobCertificato;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdCertificatoOmonimi(BigDecimal aValore) {
		mIdCertificatoOmonimi = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setDocBlobCertificato(ByteArrayInputStream aValore) {
		mDocBlobCertificato = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "CertificatoOmonimiNscModel:\n" + "[ mIdCertificatoOmonimi = " + mIdCertificatoOmonimi + " ]\n"
				+ "[ mDataInserimento      = " + mDataInserimento + " ]";
		return lStr;
	}

}