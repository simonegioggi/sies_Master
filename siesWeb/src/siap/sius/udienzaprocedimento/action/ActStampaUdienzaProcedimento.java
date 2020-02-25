package siap.sius.udienzaprocedimento.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

//import siap.sico.evento.model.StampaModel;
import siap.sico.evento.model.XModel;
//import siap.siep.fascicolo.model.FascicoloSiepModel;
//import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.web.ActionSiap;
import siap.sius.udienza.action.ICostantiUdienza;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaUdienzaProcedimento</p>
 * <p>Description: Azione per la stampa "Procedimenti fissati per Udienza"</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaUdienzaProcedimento extends ActionSiap implements ICostantiUdienzaProcedimento
{
  public String processRequest() throws Exception
  {
    //BigDecimal lIdUdienza;
    String returnpage =IWebConstants.PG_DOWNLOAD_NEW;
    
    String lOrderBy = null;
    lOrderBy = this.getRequestStringParameter("tipo") ;

    // 18/04/2007 Nuovi Parametri x Ricerca Procedimenti x Udienza.
    String tipoProc = this.getRequestStringParameter("tipoProc") ;
    String lStatoProcedimento = this.getRequestStringParameter("lStatoProcedimento") ;
    
    // ???
    setRequestAttribute("tipoProc", tipoProc);
    setRequestAttribute("lStatoProcedimento", lStatoProcedimento);

    // Lettura IDUdienza
    BigDecimal lIdUdienza = null;
    if( !isRequestParameterNullObj(CAMPO_UDI_ID_UDIENZA) )
      lIdUdienza = getRequestBigDecimalParameter( CAMPO_UDI_ID_UDIENZA );
    
    // Lettura DataUdienza
    Date lDataUdienza = null;
    if( !isRequestParameterNullObj(ICostantiUdienza.CAMPO_DATA_UDIENZA) )
      lDataUdienza = getRequestDateParameter( ICostantiUdienza.CAMPO_DATA_UDIENZA, "yyyyMMdd" );
    
    // Lettura CodMagistrato
    String lCodMagistrato = null;
    if (!this.isRequestParameterNullObj(CAMPO_COD_MAGISTRATO) )
      lCodMagistrato = getRequestStringParameter(CAMPO_COD_MAGISTRATO);
    
    // Lettura IDEsperto
    BigDecimal lIdEsperto = null;
    if (!this.isRequestParameterNullObj(CAMPO_ID_ESPERTO) )
      lIdEsperto = getRequestBigDecimalParameter(CAMPO_ID_ESPERTO);


    /* Informazioni ufficio */
    String lTipoUfficio = super.getUfficioUtenteConnesso().getCodTipoUfficio();
    String lDescTipoUfficio = super.getUfficioUtenteConnesso().getDescrTipoUfficio().toUpperCase();

    String lIdDocumento = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);

    XModel lXModel = new XModel();

    lXModel.setTipoUfficio(lTipoUfficio);
    lXModel.setTipoUfficioT1(lDescTipoUfficio);
    lXModel.setUfficio(super.getUfficioUtenteConnesso().getDescrComune().toUpperCase());

    ByteArrayOutputStream lReport = null;
    // Lookup.
    IUdienzaProcedimento lCtrlUdPr = SIUSLookupRemote.getUdienzaProcedimentoRemote();
    if( lIdUdienza != null )
      lReport = lCtrlUdPr.ExStampaProcedimentixUdienza(lIdUdienza, lCodMagistrato, lIdEsperto, lXModel , lIdDocumento, lOrderBy, super.getUtenteConnesso(), lStatoProcedimento, tipoProc, this.getCodUfficioUtenteConnesso());
    else if( lDataUdienza != null )
      lReport = lCtrlUdPr.ExStampaProcedimentixDataUdienza(lDataUdienza, lCodMagistrato, lIdEsperto, lXModel , lIdDocumento, lOrderBy, super.getUtenteConnesso(), lStatoProcedimento, tipoProc, this.getCodUfficioUtenteConnesso() );
      
    // Prepara la pagina di destinazione.
    if (lReport != null)
      setRequestAttribute("report", lReport);
    return returnpage;
  }
}