package siap.sico.evento.model;

import java.util.List;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.model.GenericModel;
//import siap.sico.passaggioevento.model.PassaggioEventoModel;

/**
 * <p>
 * Title: EventoFascicoloModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta l'Evento legata al FascicoloSiepModel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class EventoFascicoloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 74071177050031388L;

	private List mListEventi;
	private FascicoloSiepModel mFascicoloSiep;

	// COSTRUTTORE DI DEFAULT
	public EventoFascicoloModel() {
		mFascicoloSiep = null;
		mListEventi = null;

	}

	public EventoFascicoloModel(FascicoloSiepModel aFascicoloSiep, List aListEventi) {
		mFascicoloSiep = aFascicoloSiep;
		mListEventi = aListEventi;

	}

	//
	// METODI GET()
	//
	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicoloSiep;
	}

	public List getEventi() {
		return mListEventi;
	}

	//
	// METODI SET()
	//
	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicoloSiep = aValore;
	}

	public void setEventi(List aValore) {
		mListEventi = aValore;
	}

}