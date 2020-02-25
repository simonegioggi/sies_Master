package siap.sius.permesso.model;

/**
* <p>Title: CriteriRicercaProvPermessiLicenzeModel</p>
* <p>Description: Classe Model che rappresenta tutti i dati afferenti ai criteri di ricerca </p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class CriteriRicercaProvPermessiLicenzeModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8379483189964742937L;

	Date mDataDepositoIniziale = null;
	Date mDataDepositoFinale = null;
	String mDescrTipoRicerca = null;
	String mCodTipoRicerca = null;
	String mCodMotivo = null;
	String mCodUfficio = null;

	// COSTRUTTORE DI DEFAULT
	public CriteriRicercaProvPermessiLicenzeModel() {
		mDescrTipoRicerca = "";
	}

	// COSTRUTTORE DI COPIA
	public CriteriRicercaProvPermessiLicenzeModel(CriteriRicercaProvPermessiLicenzeModel aModel) {
		mDataDepositoIniziale = aModel.mDataDepositoIniziale;
		mDataDepositoFinale = aModel.mDataDepositoFinale;
		mDescrTipoRicerca = aModel.mDescrTipoRicerca;
		mCodTipoRicerca = aModel.mCodTipoRicerca;
		mCodMotivo = aModel.mCodMotivo;
		mCodUfficio = aModel.mCodUfficio;
	}

	// COSTRUTTORE MODEL
	public CriteriRicercaProvPermessiLicenzeModel(Date aDataDepositoIniziale, Date aDataDepositoFinale,
			String aCodTipoRicerca, String aDescrTipoRicerca, String aCodMotivo, String aCodUfficio) {
		mDataDepositoIniziale = aDataDepositoIniziale;
		mDataDepositoFinale = aDataDepositoFinale;
		mCodTipoRicerca = aCodTipoRicerca;
		mDescrTipoRicerca = aDescrTipoRicerca;
		mCodMotivo = aCodMotivo;
		mCodUfficio = aCodUfficio;
	}

	//
	// METODI GET()
	//
	public Date getDataDepositoIniziale() {
		return mDataDepositoIniziale;
	}

	public Date getDataDepositoFinale() {
		return mDataDepositoFinale;
	}

	public String getCodTipoRicerca() {
		return mCodTipoRicerca;
	}

	public String getDescrTipoRicerca() {
		return mDescrTipoRicerca;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	//
	// METODI SET()
	//
	public void setDataDepositoIniziale(Date aValore) {
		mDataDepositoIniziale = aValore;
	}

	public void setDataDepositoFinale(Date aValore) {
		mDataDepositoFinale = aValore;
	}

	public void setCodTipoRicerca(String aValore) {
		mCodTipoRicerca = aValore;
	}

	public void setDescrTipoRicerca(String aValore) {
		mDescrTipoRicerca = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

}