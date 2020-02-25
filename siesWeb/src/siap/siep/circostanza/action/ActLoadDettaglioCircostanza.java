package siap.siep.circostanza.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.util.SIEPLookupRemote;


/**
* <p>Title: ActLoadDettaglioCircostanza</p>
* <p>Description: Classe Action per la load dettaglio di Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioCircostanza extends ActionSiap implements ICostantiCircostanza
{
  protected CircostanzaModel mlCirMod =null;

  public String processRequest() throws Exception
  {
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_CIRCOSTANZA);

    ICircostanza lCtrl = SIEPLookupRemote.getCircostanzaRemote();
    mlCirMod = lCtrl.ExRicercaCircostanzaByKey(lId);

    setRequestAttribute("circostanza", mlCirMod);

    return PG_LOAD_DETTAGLIOCIRCOSTANZA;
  }
}