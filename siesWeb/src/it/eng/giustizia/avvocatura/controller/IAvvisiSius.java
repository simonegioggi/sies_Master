package it.eng.giustizia.avvocatura.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sius.avvocatura.model.AvvisiElencoModel;
import f3b.util.F3BException;

/**
 * @author Gioggi, Caporizzo
 *
 */
public interface IAvvisiSius {
	
	/**
	 * Il metodo aggiorna il flagVisualizzazione dell-avviso
	 * 
	 * @param idAvviso
	 * @throws F3BException
	 */
	public void aggiornaAvvisiAvvocato(BigDecimal idAvviso) throws F3BException;

	/**
	 * Il metodo effettua una ricerca di avvisi dati i parametri di input
	 * 
	 * @param datiAvviso
	 * @return
	 * @throws F3BException
	 */
	public Vector<AvvisiElencoModel> ricercaAvvisiSius(String codFiscaleAvv, String codTipoUfficio, String codDistretto,
			                        String flagVisualizzazione, Date dataInizioRicerca, Date dataFineRicerca ) throws F3BException;
				
	
}
