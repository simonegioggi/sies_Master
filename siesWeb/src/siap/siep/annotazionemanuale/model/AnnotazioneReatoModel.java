package siap.siep.annotazionemanuale.model;

import siap.siep.reato.model.ReatoModel;
import f3b.model.GenericModel;

public class AnnotazioneReatoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7014317799001189772L;

	private AnnotazioneManualeModel mAnnotazioneManuale;
	private ReatoModel mReato;

	public AnnotazioneReatoModel() {
		mAnnotazioneManuale = null;
		mReato = null;
	}

	public AnnotazioneReatoModel(AnnotazioneManualeModel aAnnotazioneManuale, ReatoModel aReato) {
		mAnnotazioneManuale = aAnnotazioneManuale;
		mReato = aReato;
	}

	//
	// METODI GET()
	//

	public AnnotazioneManualeModel getAnnotazioneManuale() {
		return mAnnotazioneManuale;
	}

	public ReatoModel getReato() {
		return mReato;
	}

	//
	// METODI SET()
	//

	public void setAnnotazioneManuale(AnnotazioneManualeModel aValore) {
		mAnnotazioneManuale = aValore;
	}

	public void setReato(ReatoModel aValore) {
		mReato = aValore;
	}

}