package siap.sige.fascicolo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.util.SIGELookupRemote;
import f3b.web.IWebConstants;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaProcedimento extends ActionSiap implements ICostantiFascicoloSige
{
  public String processRequest() throws Exception
  {
    BigDecimal lFasKey = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIGE);
    String lTipoUfficio = getUfficioUtenteConnesso().getCodUfficio();

    // Generazione documento di stampa
    IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaProcedimento(lFasKey, lTipoUfficio, super.getUtenteConnesso() );

    // Prepara la pagina di destinazione.
    if (lReport != null)
      setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}
