package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadRicercaScadenzarioRinnovoVerbaleVaneRicerche</p>
* <p>Description: Classe Action per la load ricerca di Scadenzario</p>
* <p> di tipo 03 (vane Ricerche) con Verbale Vane Ricerche Pervenuto
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaScadenzarioRinnovoVerbaleVaneRicerche extends ActionSiap implements ICostantiScadenzario
{
	public String processRequest() throws F3BException
	 {
		 return PG_LOAD_RICERCASCADENZARIO_RINNOVO_VANERICERCHE;  //restituisce la jsp di VIEW
	 }
}