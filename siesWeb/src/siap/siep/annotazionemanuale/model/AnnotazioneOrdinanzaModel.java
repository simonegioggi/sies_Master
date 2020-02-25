package siap.siep.annotazionemanuale.model;

import siap.sico.evento.model.EventoModel;
import f3b.model.GenericModel;

public class AnnotazioneOrdinanzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 930297697343510067L;

	protected AnnotazioneManualeModel mAnnotazioneManuale;
	protected EventoModel mEvento;

	public AnnotazioneOrdinanzaModel() {
		mAnnotazioneManuale = null;
		mEvento = null;
	}

	public AnnotazioneOrdinanzaModel(AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento) {
		mAnnotazioneManuale = aAnnotazioneManuale;
		mEvento = aEvento;
	}

	//
	// METODI GET()
	//

	public AnnotazioneManualeModel getAnnotazioneManuale() {
		return mAnnotazioneManuale;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	//
	// METODI SET()
	//

	public void setAnnotazioneManuale(AnnotazioneManualeModel aValore) {
		mAnnotazioneManuale = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

}