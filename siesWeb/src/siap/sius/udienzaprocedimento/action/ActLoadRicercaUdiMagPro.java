package siap.sius.udienzaprocedimento.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadRicercaUdiMagPro</p>
* <p>Description: Classe Action per la load ricerca di 
* Procedimenti x Magistrati e x Udienze.</p>
* Viene caricata la stessa jsp della ricerca x Udienze ma passando 
* il parametro tiporicerca nella request per differenziare la ricerca
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* <p>author: Luigi</p>
* @version 1.0
*/
public class ActLoadRicercaUdiMagPro extends ActionSiap 
implements ICostantiUdienzaProcedimento
{
	public String processRequest() throws F3BException
	{      
		setRequestAttribute("tiporicerca", CARICO_MAGISTRATI);
		return PG_LOAD_RICERCAUDIENZEMAGPROC;  //restituisce la jsp di VIEW
	}
}