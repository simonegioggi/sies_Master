package siap.siep.rateizzazionepp.model;

import java.util.Vector;

import f3b.model.GenericModel;
import siap.sico.evento.model.EventoModel;

/**
 * Model per contenere l'evento e le sue rateizzazioni
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class EventoRateizzazionePPModel extends GenericModel {

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -1106846677812291796L;

	private EventoModel em;
	private Vector<RateizzazionePPModel> listaRateizzazioniPP;
	
	// Metodi GETTER
	public EventoModel getEvento() {
		return em;
	}

	public Vector<RateizzazionePPModel> getListaRateizzazioniPP() {
		return listaRateizzazioniPP;
	}

	public int getNumTotRate () {
		int totRate = 0;
		for (RateizzazionePPModel rata : listaRateizzazioniPP) {
			if (rata.getNumeroRate()!=null)
				totRate = totRate + rata.getNumeroRate().intValue();
		}
			
		return totRate;
	}
	
	// METODI SETTER
	public void setEvento(EventoModel em) {
		this.em = em;
	}

	public void setListaRateizzazioniPP(Vector<RateizzazionePPModel> listaRateizzazioniPP) {
		this.listaRateizzazioniPP = listaRateizzazioniPP;
	}

}