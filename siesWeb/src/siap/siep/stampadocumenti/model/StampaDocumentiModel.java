package siap.siep.stampadocumenti.model;

/**
* <p>Title: StampaDocumentiModel</p>
* <p>Description: Classe Model che rappresenta il StampaDocumenti</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class StampaDocumentiModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7054370951300334086L;

	private BigDecimal mIdStampa;
	private String mIdUtente;
	private Date mData;
	private String mStato;
	private Integer mNumStampeRichieste;
	private Integer mNumeStampeEffettuate;
	private ByteArrayInputStream mDocBlobIn;
	private ByteArrayOutputStream mDocBlobOut;
	private String mDescrizione;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public StampaDocumentiModel() {
		this.mIdStampa = null;
		this.mIdUtente = "";
		this.mData = null;
		this.mStato = "";
		this.mNumStampeRichieste = null;
		this.mNumeStampeEffettuate = null;
		this.mDocBlobIn = null;
		this.mDocBlobOut = null;
		this.mDescrizione = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public StampaDocumentiModel(StampaDocumentiModel aModel) {
		this.mIdStampa = aModel.mIdStampa;
		this.mIdUtente = aModel.mIdUtente;
		this.mData = aModel.mData;
		this.mStato = aModel.mStato;
		this.mNumStampeRichieste = aModel.mNumStampeRichieste;
		this.mNumeStampeEffettuate = aModel.mNumeStampeEffettuate;
		this.mDescrizione = aModel.mDescrizione;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public StampaDocumentiModel(BigDecimal aIdStampa, String aIdUtente, Date aData, String aStato,
			Integer aNumStampeRichieste, Integer aNumeStampeEffettuate, String aDescrizione) {
		this.mIdStampa = aIdStampa;
		this.mIdUtente = aIdUtente;
		this.mData = aData;
		this.mStato = aStato;
		this.mNumStampeRichieste = aNumStampeRichieste;
		this.mNumeStampeEffettuate = aNumeStampeEffettuate;
		this.mDescrizione = aDescrizione;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdStampa() {
		return mIdStampa;
	}

	public String getIdUtente() {
		return mIdUtente;
	}

	public Date getData() {
		return mData;
	}

	public String getStato() {
		return mStato;
	}

	public Integer getNumStampeRichieste() {
		return mNumStampeRichieste;
	}

	public Integer getNumeStampeEffettuate() {
		return mNumeStampeEffettuate;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdStampa(BigDecimal aValore) {
		mIdStampa = aValore;
	}

	public void setIdUtente(String aValore) {
		mIdUtente = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setStato(String aValore) {
		mStato = aValore;
	}

	public void setNumStampeRichieste(Integer aValore) {
		mNumStampeRichieste = aValore;
	}

	public void setNumeStampeEffettuate(Integer aValore) {
		mNumeStampeEffettuate = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "StampaDocumentiModel:\n" + "[ mIdStampa             = " + mIdStampa + " ]\n"
				+ "[ mIdUtente             = " + mIdUtente + " ]\n" + "[ mData                 = " + mData
				+ " ]\n" + "[ mStato                = " + mStato + " ]\n" + "[ mNumStampeRichieste   = "
				+ mNumStampeRichieste + " ]\n" + "[ mNumeStampeEffettuate = " + mNumeStampeEffettuate + " ]";
		return lStr;
	}

	public ByteArrayInputStream getDocBlobIn() {
		return mDocBlobIn;
	}

	public void setDocBlobIn(ByteArrayInputStream docBlobIn) {
		mDocBlobIn = docBlobIn;
	}

	public ByteArrayOutputStream getDocBlobOut() {
		return mDocBlobOut;
	}

	public void setDocBlobOut(ByteArrayOutputStream docBlobOut) {
		mDocBlobOut = docBlobOut;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	public void setDescrizione(String descrizione) {
		mDescrizione = descrizione;
	}

}