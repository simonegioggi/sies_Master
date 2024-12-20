package siap.sius.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * MEV_2023-35 Nuovo aggancio per la ricerca dei procedimenti di esecuzione Pene Sostitutive per SOggettp
 *
 *
 */
public class ActLoadRicercaSoggettiConProcDiEsecuzionePS extends ActionSiap implements ICostantiFascicoloSius
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
    lOption.setFilter("U126");
    setRequestAttribute("contenuto", "" + lOption );
    
    // MEV_2023-35 si passa il cotenuto alla form per parametrizzare le etichette
    // essendo la jsp utilizzata anche per EAS (U019)
    setRequestAttribute("ContenutoES","U126");

    return PG_LOAD_RICERCASOGGETTICONPROCDIESECUZIONESS;
  }
}
