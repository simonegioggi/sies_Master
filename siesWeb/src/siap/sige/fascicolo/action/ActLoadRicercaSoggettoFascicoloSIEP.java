package siap.sige.fascicolo.action;

import siap.sius.fascicolo.action.ActLoadRicercaSoggettoFascicoloSiep;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaSoggettoFascicoloSIEP</p>
* <p>Description: Classe Action per la ricerca di Soggetti titolari di Procedimento SIEP.</p>
*  Viene richiamata la maschera  che attiva la funzione di Ricerca Procedimento SIEP per Soggetto per l'iscrizione manuale.
*  L'action specializza siap.sius.fascicolo.action.ActLoadRicercaSoggettoFascicoloSiep solo per cambiare l'intestazione della maschera.
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 5.0
*/

public class ActLoadRicercaSoggettoFascicoloSIEP extends ActLoadRicercaSoggettoFascicoloSiep
{
	public String processRequest() throws F3BException 
	{
		 setRequestAttribute("next_action", "siap.sige.fascicolo.action.ActRicercaSoggettoFascicoloSIEP");
		 
		 setRequestAttribute("titolo", "Ricerca Procedimento SIEP per Soggetto");

		 // MEV 15 - Revisione SIGE
		 // Aggiunto parametro per identificare la funzione che richiama la maschera
		 // di Ricerca Procedimento SIEP per Soggetto per l'iscrizione manuale.
		 // Quando viene richiamata da SIGE sulla maschera è presente un ulteriore
		 // parametro di ricerca "Codice CUI" e sul risultato della ricerca è stata
		 // sostituita la colonna "Stato" con la "Data di Nascita" del soggetto.
		 setRequestAttribute("codFunzione", ICostantiFascicoloSius.COD_FUNZIONE_90020012);
		 
		 return super.processRequest();
	 }



}
