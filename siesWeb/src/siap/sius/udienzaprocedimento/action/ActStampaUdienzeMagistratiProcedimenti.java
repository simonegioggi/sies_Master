package siap.sius.udienzaprocedimento.action;

import java.io.ByteArrayOutputStream;

import siap.sico.evento.model.XModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.web.ActionSiap;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.udienza.action.ICostantiUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaUdienzeMagistratiProcedimenti</p>
 * <p>Description: Azione per la stampa "N.ro Procedimenti per Magistrati Relatore per Udienze"</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaUdienzeMagistratiProcedimenti extends ActionSiap implements ICostantiUdienza
{
  public String processRequest() throws Exception
  {
    String returnpage =IWebConstants.PG_DOWNLOAD_NEW;

    // Lettura dell' ID del Template dalla request
    String lIdDocumento = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);

    // Popola il model UdienzaModel con le date prelevata dalla request
    UdienzaModel lUdienzaMod = new UdienzaModel();
    lUdienzaMod.setDataUdienza(getRequestDateParameter("Data1","ddMMyyyy"));
    lUdienzaMod.setDataUdienzaFine(getRequestDateParameter("Data2","ddMMyyyy"));
    lUdienzaMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso() );

    /* Informazioni ufficio */
    String lTipoUfficio = super.getUfficioUtenteConnesso().getCodTipoUfficio();
    String lDescTipoUfficio = super.getUfficioUtenteConnesso().getDescrTipoUfficio().toUpperCase();

    // Costruzione della pagina XML contenente l'intestazione di stampa
    XModel lXModel = new XModel();
    lXModel.setTipoUfficio(lTipoUfficio);
    lXModel.setTipoUfficioT1(lDescTipoUfficio);
    lXModel.setUfficio(getUfficioUtenteConnesso().getDescrComune().toUpperCase());
    lXModel.setMessage("Udienze tra il: " + DateUtils.getDateToString(lUdienzaMod.getDataUdienza(), "dd-MM-yyyy") + " e il: " +   DateUtils.getDateToString(lUdienzaMod.getDataUdienzaFine(), "dd-MM-yyyy")   );

    // Stampa attraverso il Controller di Stampa Sius
    IStampaSius lCtrlStampa = SIUSLookupRemote.getStampaRemote();
    ByteArrayOutputStream lReport = lCtrlStampa.ExPreStampaUdienzeMagistratiProcedimenti(lUdienzaMod,lXModel,lIdDocumento);

    // Prepara la pagina di destinazione.
    if (lReport != null)
      setRequestAttribute("report", lReport);
    return returnpage;
  }
}
