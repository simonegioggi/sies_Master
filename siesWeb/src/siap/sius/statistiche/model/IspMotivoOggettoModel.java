package siap.sius.statistiche.model;

import f3b.model.GenericModel;

public class IspMotivoOggettoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -246770787810785041L;
	private String mCodMotivo;
	private String mDescMotivo;
	private String mCodOggetto;
	private String mDescOggetto;
	private String mTipoUfficio;

	// Costruttore di default
	//
	public IspMotivoOggettoModel() {
		this.mCodMotivo = null;
		this.mDescMotivo = null;
		this.mCodOggetto = null;
		this.mDescOggetto = null;
		this.mTipoUfficio = null;
	}

	//
	// Costruttore di copia
	//
	public IspMotivoOggettoModel(IspMotivoOggettoModel aModel) {
		this.mCodMotivo = aModel.mCodMotivo;
		this.mDescMotivo = aModel.mDescMotivo;
		this.mCodOggetto = aModel.mCodOggetto;
		this.mDescOggetto = aModel.mDescOggetto;
		this.mTipoUfficio = aModel.mTipoUfficio;
	}

	//
	// Costruttore parametrizzato
	//
	public IspMotivoOggettoModel(String lCodMotivo, String lDescMotivo, String lCodOggetto,
			String lDescOggetto, String lTipoUfficio) {
		this.mCodMotivo = lCodMotivo;
		this.mDescMotivo = lDescMotivo;
		this.mCodOggetto = lCodOggetto;
		this.mDescOggetto = lDescOggetto;
		this.mTipoUfficio = lTipoUfficio;
	}

	//
	// metodi get
	//
	public String getCodMotivo() {
		return this.mCodMotivo;
	}

	public String getDescMotivo() {
		return this.mDescMotivo;
	}

	public String getCodOggetto() {
		return this.mCodOggetto;
	}

	public String getDescOggetto() {
		return this.mDescOggetto;
	}

	public String getTipoUfficio() {
		return this.mTipoUfficio;
	}

	//
	// metodi set
	//
	public void setCodMotivo(String lCodMotivo) {
		this.mCodMotivo = lCodMotivo;
	}

	public void setDescMotivo(String lDescMotivo) {
		this.mDescMotivo = lDescMotivo;
	}

	public void setCodOggetto(String lCodOggetto) {
		this.mCodOggetto = lCodOggetto;
	}

	public void setDescOggetto(String lDescOggetto) {
		this.mDescOggetto = lDescOggetto;
	}

	public void setTipoUfficio(String lTipoUfficio) {
		this.mTipoUfficio = lTipoUfficio;
	}
}
