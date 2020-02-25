package siap.sige.udienzaprocedimento.dettaglioruolo.action;

import siap.sico.web.ActionSiap;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadRicercaUdienzeMagistratiProcedimenti</p>
 * <p>Description: Classe Action per il caricamento della maschera ricerca
 * dei Procedimenti raggruppati per Magistrati Relatori e per Udienze
 * in un arco di date selezionato. </p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Agile </p>
 * @version 1.0
 */

public class ActLoadRicercaUdienzeMagistratiProcedimenti extends ActionSiap 
implements ICostantiUdienzaProcedimentoSige {
  public String processRequest() throws F3BException{
		 return PG_LOAD_RICERCAUDIENZEMAGPROC;  //restituisce la jsp di VIEW
	 }
}