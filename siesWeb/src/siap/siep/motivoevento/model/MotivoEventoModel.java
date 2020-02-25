package siap.siep.motivoevento.model;

/**
* <p>Title: MotivoEventoModel</p>
* <p>Description: Classe Model che rappresenta il MotivoEvento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class MotivoEventoModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = 438802615140771114L;
	private BigDecimal mIdMotivoEvento;
	private String mCodMotivoRevoca;
	private String mDescrMotivoRevoca;
	private String mCodMotivoRevocaPm;
	private String mDescrMotivoRevocaPm;
	private String mMotivazioni;
	private BigDecimal mEveIdEvento;

	// COSTRUTTORE DI DEFAULT
	public MotivoEventoModel() {
		this.mIdMotivoEvento = null;
		this.mCodMotivoRevoca = "";
		this.mDescrMotivoRevoca = "";
		this.mCodMotivoRevocaPm = "";
		this.mDescrMotivoRevocaPm = "";
		this.mMotivazioni = "";
		this.mEveIdEvento = null;
	}

	// COSTRUTTORE DI COPIA
	public MotivoEventoModel(MotivoEventoModel aModel) {
		this.mIdMotivoEvento = aModel.mIdMotivoEvento;
		this.mCodMotivoRevoca = aModel.mCodMotivoRevoca;
		this.mDescrMotivoRevoca = aModel.mDescrMotivoRevoca;
		this.mCodMotivoRevocaPm = aModel.mCodMotivoRevocaPm;
		this.mDescrMotivoRevocaPm = aModel.mDescrMotivoRevocaPm;
		this.mMotivazioni = aModel.mMotivazioni;
		this.mEveIdEvento = aModel.mEveIdEvento;
	}

	// COSTRUTTORE MODEL
	public MotivoEventoModel(BigDecimal aIdMotivoEvento, String aCodMotivoRevoca, String aDescrMotivoRevoca,
			String aCodMotivoRevocaPm, String aDescrMotivoRevocaPm, String aMotivazioni,
			BigDecimal aEveIdEvento) {
		this.mIdMotivoEvento = aIdMotivoEvento;
		this.mCodMotivoRevoca = aCodMotivoRevoca;
		this.mDescrMotivoRevoca = aDescrMotivoRevoca;
		this.mCodMotivoRevocaPm = aCodMotivoRevocaPm;
		this.mDescrMotivoRevocaPm = aDescrMotivoRevocaPm;
		this.mMotivazioni = aMotivazioni;
		this.mEveIdEvento = aEveIdEvento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdMotivoEvento() {
		return mIdMotivoEvento;
	}

	public String getCodMotivoRevoca() {
		return mCodMotivoRevoca;
	}

	public String getDescrMotivoRevoca() {
		return mDescrMotivoRevoca;
	}

	public String getCodMotivoRevocaPm() {
		return mCodMotivoRevocaPm;
	}

	public String getDescrMotivoRevocaPm() {
		return mDescrMotivoRevocaPm;
	}

	public String getMotivazioni() {
		return mMotivazioni;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	//
	// METODI SET()
	//

	public void setIdMotivoEvento(BigDecimal aValore) {
		mIdMotivoEvento = aValore;
	}

	public void setCodMotivoRevoca(String aValore) {
		mCodMotivoRevoca = aValore;
	}

	public void setDescrMotivoRevoca(String aValore) {
		mDescrMotivoRevoca = aValore;
	}

	public void setCodMotivoRevocaPm(String aValore) {
		mCodMotivoRevocaPm = aValore;
	}

	public void setDescrMotivoRevocaPm(String aValore) {
		mDescrMotivoRevocaPm = aValore;
	}

	public void setMotivazioni(String aValore) {
		mMotivazioni = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdMotivoEvento + " - " + mCodMotivoRevoca + " - " + mDescrMotivoRevoca + " - "
				+ mCodMotivoRevocaPm + " - " + mDescrMotivoRevocaPm + " - " + mMotivazioni + " - "
				+ mEveIdEvento;

		return lStr;
	}
}
