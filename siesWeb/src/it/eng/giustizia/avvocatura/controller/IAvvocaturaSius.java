/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.controller;

import f3b.util.F3BException;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.DATIPROCEDIMENTOOUTPUT;

/**
 * @author Gioggi
 *
 */
public interface IAvvocaturaSius {

	public DATIPROCEDIMENTOOUTPUT callRicercaFascicoloSius(String codDistretto, String codTipoUfficio,
			String codiceFiscaleAvvocato, int annoProcedimento, int numeroProcedimento) throws F3BException;	

}