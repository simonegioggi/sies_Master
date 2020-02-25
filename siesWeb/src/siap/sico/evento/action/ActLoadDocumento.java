package siap.sico.evento.action;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
//import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActLoadDocumento</p>
 * <p>Description: Carica il Documento BLOB </p>
 * <p>Copyright: Copyright (c) 2002</p>
 */
public class ActLoadDocumento extends ActionSiap
implements ICostantiEvento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info(" START ");
    
    EventoModel lEveMod = new EventoModel();

    lEveMod.setIdEvento( getRequestBigDecimalParameter(CAMPO_ID_EVENTO) );

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExGetDocumento( lEveMod );
 
    setRequestAttribute("report", lReport);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("Report Size : " + lReport.size());
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info(" END ");
    
    return IWebConstants.PG_DOWNLOAD_DOCUMENT;
  }
}