package siap.siep.dettaglioprovvedimento.model;

/**
* <p>Title: DettaglioProvvedimentoModel</p>
* <p>Description: Classe Model che rappresenta il DettaglioProvvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class DettaglioProvvedimentoModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -2772574192876324213L;

	private String mCodTipoEvento;
	private String mCodTipoProvvedimento;
	private String mCodMotivo;
	private String mActionDettaglio;
	private String mActionUpload;
	private String mChiaveEvento;

	// COSTRUTTORE DI DEFAULT
	public DettaglioProvvedimentoModel() {
		this.mCodTipoEvento = "";
		this.mCodTipoProvvedimento = "";
		this.mCodMotivo = "";
		this.mActionDettaglio = "";
		this.mActionUpload = "";
		this.mChiaveEvento = "";
	}

	// COSTRUTTORE DI COPIA
	public DettaglioProvvedimentoModel(DettaglioProvvedimentoModel aModel) {
		this.mCodTipoEvento = aModel.mCodTipoEvento;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mCodMotivo = aModel.mCodMotivo;
		this.mActionDettaglio = aModel.mActionDettaglio;
		this.mActionUpload = aModel.mActionUpload;
		this.mChiaveEvento = aModel.mChiaveEvento;
	}

	// COSTRUTTORE MODEL
	public DettaglioProvvedimentoModel(String aCodTipoEvento, String aCodTipoProvvedimento, String aCodMotivo,
			String aActionDettaglio, String aActionUpload, String aChiaveEvento) {
		this.mCodTipoEvento = aCodTipoEvento;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mCodMotivo = aCodMotivo;
		this.mActionDettaglio = aActionDettaglio;
		this.mActionUpload = aActionUpload;
		this.mChiaveEvento = aChiaveEvento;
	}

	//
	// METODI GET()
	//

	public String getCodTipoEvento() {
		return mCodTipoEvento;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getActionDettaglio() {
		return mActionDettaglio;
	}

	public String getChiaveEvento() {
		return mChiaveEvento;
	}

	public String getActionUpload() {
		return mActionUpload;
	}

	//
	// METODI SET()
	//

	public void setCodTipoEvento(String aValore) {
		mCodTipoEvento = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setActionDettaglio(String aValore) {
		mActionDettaglio = aValore;
	}

	public void setChiaveEvento(String aValore) {
		mChiaveEvento = aValore;
	}

	public void setActionUpload(String aValore) {
		mActionUpload = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mCodTipoEvento + " - " + mCodTipoProvvedimento + " - " + mCodMotivo + " - "
				+ mActionDettaglio + " - " + mActionUpload + " - " + mChiaveEvento;

		return lStr;
	}

}