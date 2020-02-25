package siap.sius.impugnazione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaImpugnazione extends ActionSiap implements ICostantiImpugnazione, ICostantiEvento
{
  public String processRequest() throws Exception
  {
    BigDecimal lImpKey = getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE);
    BigDecimal lEveKey = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);
    BigDecimal lFasKey = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
    String lIdTemplate = getRequestStringParameter("CodTemplate");
    String lTipoUfficio = getUfficioUtenteConnesso().getCodUfficio();

    // Lookup.
    IImpugnazione lCtrl = SIUSLookupRemote.getImpugnazioneRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaImpugnazione(lImpKey, lEveKey, lFasKey, lIdTemplate, lTipoUfficio, super.getUtenteConnesso() );

    // Prepara la pagina di destinazione.
    if (lReport != null)
      setRequestAttribute("report", lReport);

    //return IWebConstants.PG_DOWNLOAD;
    return IWebConstants.PG_DOWNLOAD_NEW;

  }
}
