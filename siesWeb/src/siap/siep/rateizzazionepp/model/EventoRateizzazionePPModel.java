package siap.siep.rateizzazionepp.model;

import java.util.Vector;

import siap.sico.evento.model.EventoModel;

/**
 * Model per contenere l'evento e le sue rateizzazioni
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class EventoRateizzazionePPModel {

	private EventoModel em;
	private Vector<RateizzazionePPModel> listaRateizzazioniPP;

	// Metodi GETTER
	public EventoModel getEvento() {
		return em;
	}

	public Vector<RateizzazionePPModel> getListaRateizzazioniPP() {
		return listaRateizzazioniPP;
	}

	// METODI SETTER
	public void setEvento(EventoModel em) {
		this.em = em;
	}

	public void setListaRateizzazioniPP(Vector<RateizzazionePPModel> listaRateizzazioniPP) {
		this.listaRateizzazioniPP = listaRateizzazioniPP;
	}

}