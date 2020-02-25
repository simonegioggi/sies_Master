package siap.sige.udienzaprocedimento.dettaglioruolo.action;

import org.apache.log4j.Logger;

//import f3b.util.F3BException;
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.sico.web.ActionSiap;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadRicercaUdienzaProcedimentoMag</p>
* <p>Description: Classe Action per la load ricerca di UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile</p>
* @version 1.0
*/
public class ActLoadRicercaUdienzaProcedimentoMag extends ActLoadRicercaUdienzaProcedimento 
implements ICostantiUdienzaProcedimentoSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws Exception {
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.info( "inizio" );

	  setRequestAttribute("tiporicerca", "xmagistrato");
	    
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.info( "fine" );

	  return PG_LOAD_RICERCAUDIENZAPROCEDIMENTO;  //restituisce la jsp di VIEW
	}
}