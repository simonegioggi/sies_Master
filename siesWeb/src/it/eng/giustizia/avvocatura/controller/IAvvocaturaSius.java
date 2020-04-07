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
			String codiceFiscaleAvvocato, int annoProcedimento, int numeroProcedimento, String codUfficio)
			throws F3BException;// MEV_20_Avvocatura_SIES_Sede aggiunto parametro codUfficio

}