package siap.sige.udienzaprocedimento.dettaglioruolo.action;

import siap.sico.web.ActionSiap;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadRicercaUdiMagPro</p>
* <p>Description: Classe Action per la load ricerca di 
* Procedimenti x Magistrati e x Udienze.</p>
* Viene caricata la stessa jsp della ricerca x Udienze ma passando 
* il parametro tiporicerca nella request per differenziare la ricerca
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile</p>
* <p>author: </p>
* @version 1.0
*/

public class ActLoadRicercaUdiMagPro extends ActionSiap 
implements ICostantiUdienzaProcedimentoSige {  
	public String processRequest() throws F3BException {      
		setRequestAttribute("tiporicerca", "CARICO_MAGISTRATI");
		return PG_LOAD_RICERCAUDIENZEMAGPROC;  //restituisce la jsp di VIEW
	}
}