package siap.jms.messaggio.model;

import siap.jms.ICostantiJMS;
import f3b.model.GenericModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class RootJMSModel extends GenericModel implements ICostantiJMS {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8587928768096542988L;

	private String mCodTipoMessaggio;
	private String mDescrTipoMessaggio;

	private String mCodTipoOperazione;
	private String mDescrTipoOperazione;
	private String mEsito;

	public RootJMSModel() {
		this.mCodTipoMessaggio = "";
		this.mCodTipoOperazione = "";
		this.mDescrTipoOperazione = "";
		this.mDescrTipoMessaggio = "";
		this.mEsito = "";
	}

	// Get
	public String getCodTipoMessaggio() {
		return mCodTipoMessaggio;
	}

	public String getDescrTipoMessaggio() {
		return mDescrTipoMessaggio;
	}

	public String getCodTipoOperazione() {
		return mCodTipoOperazione;
	}

	public String getDescrTipoOperazione() {
		return mDescrTipoOperazione;
	}

	public String getEsito() {
		return mEsito;
	}

	// Set
	public void setCodTipoMessaggio(String aValore) {
		mCodTipoMessaggio = aValore;
	}

	public void setDescrTipoMessaggio(String aValore) {
		mDescrTipoMessaggio = aValore;
	}

	public void setCodTipoOperazione(String aValore) {
		mCodTipoOperazione = aValore;
	}

	public void setDescrTipoOperazione(String aValore) {
		mDescrTipoOperazione = aValore;
	}

	public void setEsito(String aValore) {
		mEsito = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mCodTipoMessaggio + " - " + mDescrTipoMessaggio + " - " + mCodTipoOperazione + " - "
				+ mEsito + " - " + mDescrTipoOperazione + " - ";

		return lStr;
	}

}