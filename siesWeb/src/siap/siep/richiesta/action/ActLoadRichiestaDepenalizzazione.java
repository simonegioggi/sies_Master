package siap.siep.richiesta.action;

/**
 * <p>Title: ActLoadRichiestaDepenalizzazione</p>
 * <p>Description: Azione Load della Fomr di inserimento della richiesta al
 * GE di Depenalizzazione</p>
 * <p>Rideterminazione Pena - Provvedimenti con richiesta al GE - Depenalizzazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;


public class ActLoadRichiestaDepenalizzazione extends ActLoadRichiestaGE
                                              implements ICostantiRichiesta
{
  /**
   * Azione di caricamento della form d'inserimento della richiesta Depenalizzazione
   * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {

    this.isFascicoloSiepDiCompetenza();
    String lPageErr = loadRichiestaGE(DEPENALIZZAZIONE);

    if (lPageErr != null&& isRequestParameterNullObj("ForzaInserimento") )
      return lPageErr;

    Option lOption  = new Option( DecodificheManager.getInstance().getTipoFonteReato() );
    setRequestAttribute("TipiFontiReato", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getSottonumerazione() );
    setRequestAttribute("TipiSottonumerazione", "" + lOption );

    return PG_LOAD_RICHIESTA_DEPENALIZZAZIONE;
  }
}
