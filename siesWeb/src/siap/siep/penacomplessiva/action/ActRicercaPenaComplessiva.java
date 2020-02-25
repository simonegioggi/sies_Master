package siap.siep.penacomplessiva.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>Title: ActRicercaPenaComplessiva</p>
 * <p>Description: Classe Action per la ricerca di PenaComplessiva</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActRicercaPenaComplessiva extends ActionSiap implements ICostantiPenaComplessiva
{
  public String processRequest() throws Exception
  {
    //PenaComplessivaModel lPenMod = new PenaComplessivaModel();
    //lPenMod.setIdPenaComplessiva( getRequestBigDecimalParameter( CAMPO_ID_PENA_COMPLESSIVA) );

    BigDecimal lIdPena = getRequestBigDecimalParameter( CAMPO_ID_PENA_COMPLESSIVA);

    IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
    DettaglioPenaComplessivaModel lDettPena = lCtrl.ExRicercaPenaCompSanzioneSostContinuazioniByKey(lIdPena);

    setRequestAttribute("dettaglioPenaComplessiva", lDettPena);

    return PG_RICERCAPENACOMPLESSIVA;
  }
}
