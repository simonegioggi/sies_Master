package siap.sius.statistiche.action;


/**
 * <p>Title: ActLoadRicercaPerImpugnazione</p>
 * <p>Description: Visualizza la form di "Ricerca procedimeno per estremi Ricorso/Impugnazione".
 * Poichè la jsp utilizzata è la stessa per la "Ricerca procedimento per estremi ordinanza" 
 * il titolo della funzione viene passato nella requesy.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadRicercaPerImpugnazione extends ActionSiap implements ICostantiStatistiche
{
  public String processRequest() throws Exception
  {
	setRequestAttribute("modalitaRicerca", RICERCA_IMPUGNAZIONE); 
	
    // Imposta ComboBOX Tipo Ricorso.
    Option  lOption = new Option( DecodificheManager.getInstance().getTipoRicorso());
    String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

    // Per il TDS si vuole filtrare per High_vALUE = "tds"
    if (strCodTipoUfficio.compareTo("TDS")==0) {
      //lOption.setFilter("01");
      lOption.setFilter( new String[]{"01","04"} );
    }
    setRequestAttribute("tipoRicorso", lOption.toString() );
	
    return PG_LOAD_RICERCHE_ORDINANZA;
  }
}
