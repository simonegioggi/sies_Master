package siap.siep.richiesta.action;

/**
 * <p>Title: ActLoadRichiestaAmnistiaIndulto</p>
 * <p>Description: Azione Load del Calcolo Pena</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import f3b.util.F3BException;


public class ActLoadRichiestaIncostituzionalita extends ActLoadRichiestaGE
                                                implements ICostantiRichiesta
{
  /**
   * Azione di caricamento della form d'inserimento della richiesta Incostituzionalita
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {

    this.isFascicoloSiepDiCompetenza();
    String lPageErr = loadRichiestaGE(INCOSTITUZIONALITA);

    if (lPageErr != null && isRequestParameterNullObj("ForzaInserimento") )
      return lPageErr;

    return PG_LOAD_RICHIESTA_INCOSTITUZIONALITA;
  }
}
