package siap.sico.evento.model;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.Date;

import f3b.model.GenericModel;

public class XModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6907432134646139703L;

	private String mUfficio;
	private String mTipoUfficio;
	private String mIndirizzo;
	private String mCap;
	private String mTelefono;
	private String mFax;
	private String mEMail;
	private String mFirmatario;
	private String mTipoUfficioT1;
	private String mTipoUfficioT2;
	private Date mDataElaborazione;
	// Decreizione (comune) delll'Ufficio della Corte di Appello relatia
	private String mUfficioCAP;

	public XModel() {
	}

	// Costruttore di copia
	public XModel(XModel aModel) {
		mUfficio = aModel.mUfficio;
		mTipoUfficio = aModel.mTipoUfficio;
		mIndirizzo = aModel.mIndirizzo;
		mCap = aModel.mCap;
		mTelefono = aModel.mTelefono;
		mFax = aModel.mFax;
		mEMail = aModel.mEMail;
		mFirmatario = aModel.mFirmatario;
		mTipoUfficioT1 = aModel.mTipoUfficioT1;
		mTipoUfficioT2 = aModel.mTipoUfficioT2;
		mDataElaborazione = aModel.mDataElaborazione;
		mUfficioCAP = aModel.mUfficioCAP;

	}

	public XModel(String aUff, String aTipoUff, String aTipoUffT1, String aTipoUffT2) {
		mUfficio = aUff;
		mTipoUfficio = aTipoUff;
	}

	public String getUfficio() {
		return mUfficio;
	}

	public String getTipoUfficio() {
		return mTipoUfficio;
	}

	public String getTipoUfficioT1() {
		return mTipoUfficioT1;
	}

	public String getTipoUfficioT2() {
		return mTipoUfficioT2;
	}

	public String getIndirizzo() {
		return mIndirizzo;
	}

	public String getCap() {
		return mCap;
	}

	public String getTelefono() {
		return mTelefono;
	}

	public String getFax() {
		return mFax;
	}

	public String getEMail() {
		return mEMail;
	}

	public Date getDataElaborazione() {
		return mDataElaborazione;
	}

	public String getUfficioCAP() {
		return mUfficioCAP;
	}

	public String getFirmatario() {
		return mFirmatario;
	}

	// Ridefinito per renderlo visibile nell'XML generato
	// attraverso la funzione ParserModelTree().
	public String getMessage() {
		return super.getMessage();
	}

	public void setUfficio(String aValore) {
		mUfficio = aValore;
	}

	public void setTipoUfficio(String aValore) {
		mTipoUfficio = aValore;
	}

	public void setTipoUfficioT1(String aValore) {
		mTipoUfficioT1 = aValore;
	}

	public void setTipoUfficioT2(String aValore) {
		mTipoUfficioT2 = aValore;
	}

	public void setIndirizzo(String aValore) {
		mIndirizzo = aValore;
	}

	public void setCap(String aValore) {
		mCap = aValore;
	}

	public void setTelefono(String aValore) {
		mTelefono = aValore;
	}

	public void setFax(String aValore) {
		mFax = aValore;
	}

	public void setEMail(String aValore) {
		mEMail = aValore;
	}

	public void setDataElaborazione(Date aValore) {
		mDataElaborazione = aValore;
	}

	public void setUfficioCAP(String aValore) {
		mUfficioCAP = aValore;
	}

	public void setFirmatario(String aValore) {
		mFirmatario = aValore;
	}

}