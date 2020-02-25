package siap.sige.fascicolo.action;


import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.web.ActionSige;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaFSigePerTitoloEsecutivo</p>
* <p>Description: Classe Action visualizza lasiesna
*  form di ricerca Procedimenti SIGE per Titolo Esecutivo (sentenza / Fascicolo SIEP).
*  </p>
*  L'action specializza   siap.siep.fascicolo.action.ActLoadRicercaSentenza solo 
*  per cambiare l'intestazione della pagina.
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: </p>
* @author
* @version 1.0
 */

public class ActLoadRicercaFSigePerTitoloEsecutivo extends ActionSige implements ICostantiFascicoloSige
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

	 return PG_LOAD_RICERCAFSIGE_PERTITOLO;
  }
}
