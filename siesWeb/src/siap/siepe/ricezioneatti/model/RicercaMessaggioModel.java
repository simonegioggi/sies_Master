package siap.siepe.ricezioneatti.model;

/**
* <p>Title: RicercaMessaggioModel</p>
* <p>Description: Classe Model che atto a contenere
 * i criteri utilizzati in un'operazione di ricerca  Messaggi.
 * </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import siap.jms.messaggio.model.MessaggioModel;

public class RicercaMessaggioModel extends MessaggioModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -8265806169525128049L;
	private Date mDataIniziale;
	private Date mDataFinale;
	private String mDescrComuneNascita;

	// COSTRUTTORE DI DEFAULT
	public RicercaMessaggioModel() {
		super();
		this.mDataIniziale = null;
		this.mDataFinale = null;
		this.mDescrComuneNascita = "";
	}

	// COSTRUTTORE DI COPIA
	public RicercaMessaggioModel(RicercaMessaggioModel aModel) {
		super(aModel);
		this.mDataIniziale = aModel.mDataIniziale;
		this.mDataFinale = aModel.mDataFinale;
		this.mDescrComuneNascita = aModel.mDescrComuneNascita;
	}

	//
	// METODI GET()
	//
	public Date getDataIniziale() {
		return mDataIniziale;
	}

	public Date getDataFinale() {
		return mDataFinale;
	}

	public String getDescrComuneNascita() {
		return mDescrComuneNascita;
	}

	//
	// METODI SET()
	//

	public void setDataIniziale(Date aValore) {
		mDataIniziale = aValore;
	}

	public void setDataFinale(Date aValore) {
		mDataFinale = aValore;
	}

	public void setDescrComuneNascita(String aValore) {
		mDescrComuneNascita = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = super.toString() + mDataIniziale + " - " + mDataIniziale + " - " + mDescrComuneNascita + " - ";
		return lStr;
	}
}
