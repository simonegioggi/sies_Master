package siap.sige.sentenza.action;


import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.web.ActionSige;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicSenDaAssegnare</p>
* <p>Description: Classe Action visualizza la form di ricerca Titolo Esecutivo (sentenza) da assegnare ad un Procedimento SIGE.
*  </p>
*  L'action specializza   siap.siep.sentenza.action.ActLoadRicercaSentenza solo 
*  per cambiare l'intestazione della pagina.
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @author luigi
* @version 1.0
 */

public class ActLoadRicSenDaAssegnare extends ActionSige implements ICostantiFasSigeSentenza
{
  public String processRequest() throws F3BException
  {
	 // Dati per la combo "Tipo Ufficio" nella ricerca Fascicolo SIEP
	 Option lOption1 = new Option( DecodificheManager.getInstance().getTipoUfficioSIEPTrattino());
	 setRequestAttribute("tipoUfficioSIEPTrattino", "-" + lOption1 );

	 // Dati per la Ricerca Sentenza
	 Option lOption2 = new Option(DecodificheManager.getInstance().getTipoUfficioSIEPTrattino());
	 setRequestAttribute("autoritaEsterna", "" + lOption2 );

	 // Rimozione dalla sessione di Fascicolo SIEP e 
	 // Sentenza perchè saranno risultato della ricerca.
	 if( !isSessionAttributeNullObj("fascicolo") )
		 removeSessionAttribute("fascicolo");
	 if( !isSessionAttributeNullObj("sentenza") )
		 removeSessionAttribute("sentenza");

	 return PG_LOAD_RICERCA_TITOLO;
  }
}
