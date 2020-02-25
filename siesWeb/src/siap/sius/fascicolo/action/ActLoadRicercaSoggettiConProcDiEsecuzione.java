package siap.sius.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaSoggettiConProcDiEsecuzione</p>
 * <p>Description: Azione di caricamento della form di ricerca dei Soggetti con Procedimenti di Esecuzione</p>
 * <p>Copyright: Bull Italia Copyright (c) 2003</p>
 * <p>Company: Bull Italia</p>
 */

public class ActLoadRicercaSoggettiConProcDiEsecuzione extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws F3BException
  {
    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "-");
    setRequestAttribute("nazioni", "" + lOption );

    // Recupero del codice Ufficio di Sorveglianza (se è connesso l'utente TDS)
    //String strCodDistretto = getUfficioUtenteConnesso().getCodDistretto();
    //String strCodComune = getUfficioUtenteConnesso().getCodComune();

    // Il codice Ufficio Connesso viene impostato a "UDS" per consentire l'utilizzo degli stessi metodi e delle stesse query della ricerca soggetti con procedimenti.
    setRequestAttribute("TipoUfficioConnesso", "UDS" );

    // Il codice Ufficio richiesto viene impostato a "" per consentire l'utilizzo degli stessi metodi e delle stesse query della ricerca soggetti con procedimenti.
    setRequestAttribute("CodUDSTDS", "" );

    // Imposta Contenuto.
    lOption = new Option( DecodificheManager.getInstance().getTipoMisuraEsecuzione(), 75);
    setRequestAttribute("contenuto", "" + lOption );

    return PG_LOAD_RICERCASOGGETTICONPROCDIESECUZIONE; //restituisce la jsp di VIEW
  }
}