package siap.sico.evento.model;

import siap.siep.verbale.model.VerbaleModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: EventoVerbaleModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta l'Evento legata al Verbale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class EventoVerbaleModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1513433992727198498L;

	private EventoModel mEvento = null;
	private VerbaleModel mVerbale = null;

	// COSTRUTTORE DI DEFAULT
	public EventoVerbaleModel() {
		this.mEvento = new EventoModel();
		this.mVerbale = new VerbaleModel();
	}

	// COSTRUTTORE DI COPIA
	public EventoVerbaleModel(EventoVerbaleModel aModel) {
		this.mEvento = new EventoModel(aModel.getEvento());
		this.mVerbale = new VerbaleModel(aModel.getVerbale());
	}

	// COSTRUTTORE MODEL
	public EventoVerbaleModel(EventoModel aEvento, VerbaleModel aVerbale) {
		this.mEvento = aEvento;
		this.mVerbale = aVerbale;
	}

	//
	// METODI GET()
	//
	public EventoModel getEvento() {
		return mEvento;
	}

	public VerbaleModel getVerbale() {
		return mVerbale;
	}

	//
	// METODI SET()
	//

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public void setVerbale(VerbaleModel aValore) {
		mVerbale = aValore;
	}

	public String toString() {
		String lStr = new String();

		if (mEvento != null)
			lStr = mEvento.toString();

		if (mVerbale != null)
			lStr += " - " + mVerbale.toString();

		return lStr;
	}

}