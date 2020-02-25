package siap.sius.fascicolo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.model.XModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

public class ActStampaProcedimentiDelSoggetto extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    BigDecimal lSogKey = getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);
    setRequestAttribute(ICostantiSoggetto.CAMPO_ID_SOGGETTO, lSogKey.toString());

    /* Informazioni ufficio e Template */
    String lTipoUfficio = super.getUfficioUtenteConnesso().getCodTipoUfficio();
    String lCodUfficio = getUfficioUtenteConnesso().getCodUfficio();
    String lDescTipoUfficio = super.getUfficioUtenteConnesso().getDescrTipoUfficio().toUpperCase();

    String lIdDocumento = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);

    XModel lXModel = new XModel();

    lXModel.setTipoUfficio(lTipoUfficio);
    lXModel.setTipoUfficioT1(lDescTipoUfficio);
    lXModel.setUfficio(super.getUfficioUtenteConnesso().getDescrComune().toUpperCase());

    // Lookup.
    IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaProcedimentiDelSoggetto(lSogKey, lIdDocumento, lXModel, lCodUfficio, super.getUtenteConnesso() );

    // Prepara la pagina di destinazione.
    if (lReport != null)
      setRequestAttribute("report", lReport);

    //return IWebConstants.PG_DOWNLOAD;
    return IWebConstants.PG_DOWNLOAD_NEW;
  }
}
