package siap.sius.richiestaatti.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActInserisciRicAtti</p>
* <p>Description: Classe Action aggregante usata per aggreggare funzioni comuni utilizzate
 * nell'inserimento Richiesta Atti.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActInserisciRicAtti extends ActionSiap
{
  protected String getPaginaDettaglio(BigDecimal aIdEvento)
  {
    // Prepara la pagina di destinazione, precisamente punta
    // all'azione di dettaglio.
    RedirectTo lPage = new RedirectTo();
    lPage.setPage(IWebConstants.PG_MAIN);
    lPage.setAction("siap.sius.richiestaatti.action.ActLoadDettaglioRichiestaAtti");
    lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,""+ aIdEvento);
    lPage.setParameter(IWebConstants.LINK_RITORNO,"10");

    return lPage.toString();
  }

 	
}

