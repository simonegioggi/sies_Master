package siap.sius.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaSoggettiConProcDiEsecuzioneSS</p>
 * <p>Description: Azione di caricamento della form di ricerca dei Soggetti con Procedimenti di Esecuzione S.S.</p>
 * <p>Copyright: Bull Italia Copyright (c) 2007</p>
 * <p>Company: EUNICS S.p.A.</p>
 */

public class ActLoadRicercaSoggettiConProcDiEsecuzioneSS extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws F3BException
  {
    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "-");
    setRequestAttribute("nazioni", "" + lOption );

    // Il codice Ufficio Connesso viene impostato a "UDS" per consentire l'utilizzo degli stessi metodi e delle stesse query della ricerca soggetti con procedimenti.
    setRequestAttribute("TipoUfficioConnesso", "UDS" );

    // Il codice Ufficio richiesto viene impostato a "" per consentire l'utilizzo degli stessi metodi e delle stesse query della ricerca soggetti con procedimenti.
    setRequestAttribute("CodUDSTDS", "" );

    // Imposta Contenuto.
    lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimento(), 75);
    lOption.setFilter("U019");
    setRequestAttribute("contenuto", "" + lOption );
    
    // MEV_2023-35 si passa il cotenuto alla form per parametrizzare le etichette
    // essendo la jsp utilizzata anche per EPS (U126)
    setRequestAttribute("ContenutoES","U019");

    return PG_LOAD_RICERCASOGGETTICONPROCDIESECUZIONESS; //restituisce la jsp di VIEW
  }
}
