package siap.sige.udienzaprocedimento.dettaglioruolo.action;

//import f3b.util.F3BException;
import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaUdienzaProcedimento</p>
* <p>Description: Classe Action per la load ricerca di UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile S.r.l.</p>
* @version 1.0
*/

public class ActLoadRicercaUdienzaProcedimento extends ActionSiap 
implements ICostantiUdienzaProcedimentoSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  Option mOptionGiudizio; 
  
  
  public void init() {
    try {
    mOptionGiudizio = 
      new Option( DecodificheManager.getInstance().getTipoGiudizioSige(), 
                  Option.BLANK_ITEM );
    mOptionGiudizio.setValueBlankItem("-");
    
    if( getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAP") ||
        getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAS") || 
        getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP") ) { 
      mOptionGiudizio.setSelected("C");
      mOptionGiudizio.setFilter("C");
      setRequestAttribute("isCollegiale", "true" );
    }
    
    setRequestAttribute("tipoGiudizio", mOptionGiudizio.toString() );
    //setRequestAttribute("tiporicerca", "xdata");    

    } catch (Exception e) {
      // TODO: handle exception
    }
    
  }

	public String processRequest() throws Exception {
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.info( "inizio" );
	  
	  setRequestAttribute("tiporicerca", "xdata");

	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.info( "fine" );
	
	  return PG_LOAD_RICERCAUDIENZAPROCEDIMENTO;  //restituisce la jsp di VIEW
	}
	
}