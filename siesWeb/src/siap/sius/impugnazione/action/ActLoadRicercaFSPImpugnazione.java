package siap.sius.impugnazione.action;

/**
* <p>Title: ActLoadRicercaFSPImpugnazione</p>
* <p>Description: Classe Action per la load di RicercaFSPuntuale</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadRicercaFSPImpugnazione extends ActLoadRicercaFSPuntuale
{
  public String processRequest() throws Exception
  {
    super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
    setRequestAttribute("nextAction", "siap.sius.impugnazione.action.ActRicercaFSPImpugnazione" );
    String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
    if (strCodTipoUfficio.compareTo("TDS")==0 )
      setRequestAttribute("functionName", "Ricerca Provvedimenti per Ricorso" );
    else
      setRequestAttribute("functionName", "Ricerca Provvedimenti per Impugnazione/Ricorso" );

    return PG_LOAD_RICERCAFSPUNTUALE; //restituisce la jsp di VIEW.
  }
}