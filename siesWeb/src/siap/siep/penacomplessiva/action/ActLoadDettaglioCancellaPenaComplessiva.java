package siap.siep.penacomplessiva.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioCancellaPenaComplessiva</p>
 * <p>Description: Classe Action per la load dettaglio di PenaComplessiva</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class ActLoadDettaglioCancellaPenaComplessiva extends ActionSiap implements ICostantiPenaComplessiva
{
  public String processRequest() throws Exception
  {
    IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();

    DettaglioPenaComplessivaModel lDettMod = null;

    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA);

    lDettMod = lCtrl.ExRicercaPenaCompSanzioneSostContinuazioniByKey(lId);

    if(lDettMod == null)
      throw new F3BException(F3BException.USER_MESSAGE, "Pena Complessiva non presente");

    setRequestAttribute("dettaglioPenaComplessiva", lDettMod);

    return PG_LOAD_DETTAGLIOCANCELLAPENACOMPLESSIVA;
  }
}