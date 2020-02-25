package siap.siep.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.stampadocumenti.controller.IStampaDocumenti;
import siap.siep.statis.controller.StatisController;
import siap.siep.statistiche.controller.IStatisticheSiep;
import siap.siep.statistiche.model.RicercaFogliCompModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;


public class ActExportStatisticheFogliComplementariInExcel extends ActionSiap {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    public String processRequest() throws Exception {
		 RicercaFogliCompModel filtro=(RicercaFogliCompModel)getSessionAttribute("FiltroRicerca");
		 IStatisticheSiep ctrl=SIEPLookupRemote.getStatisticheSiepRemote();
		 if (!super.isRequestParameterNullObj("stampa")) {
			 return this.stampa();
		 }
		 
		 StatisticheFogliComplementariContainerModel container=ctrl.ExEstraiStatisticheFogliComplementariExportExcel(filtro);
		 container.setFiltro(filtro);
		 container.setUffUteConnesso(getUfficioUtenteConnesso());
		 container.setUtenteConnesso(super.getUtenteConnesso());

		 StatisController ctrlStats=new StatisController();
		 HSSFWorkbook excel=ctrlStats.getReportStatisticheFogliComplementari(container);
		 ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		 excel.write(fileOut);
		 setRequestAttribute("report", fileOut);
		 setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 
		 setRequestAttribute("report", fileOut);
		 setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 
		 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 siesLogger.debug( "" + getClass().getName() + " .processRequest: fine " );
         return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	  }
    
    
    /**
     * Esegue la stampa.
     * <p>
     * @throws Exception propaga errore di eccezione.
     * @return String Ritorna documneto rtf di stampa.
     */
    private String stampa() throws Exception {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "" + getClass().getName() + " .Stampa(): inizio " );
      RicercaFogliCompModel filtro=(RicercaFogliCompModel)getSessionAttribute("FiltroRicerca");
      StatisticheFogliComplementariContainerModel container=new StatisticheFogliComplementariContainerModel();
      container.setUffUteConnesso(getUfficioUtenteConnesso());
	  container.setUtenteConnesso(super.getUtenteConnesso());
	  container.setFiltro(filtro);
	  IStatisticheSiep ctrl=SIEPLookupRemote.getStatisticheSiepRemote();
	  Vector <StatisticheFogliComplementariModel> fogli = ctrl.ExEstraiStatisticheFogliComplementari(filtro, -1); 
      container.setElenco(fogli);
     
      // Generazione documento di stampa
      IStampaDocumenti lCtrlSta=SIEPLookupRemote.getStampaDocumentiRemote();  
      ByteArrayOutputStream lReport = lCtrlSta.ExPreStampaStatisticheFC(container);
     
      //Prepara la pagina di destinazione
      if (lReport != null)
        setRequestAttribute("report", lReport);
      else
        throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun documento è stato generato!");

      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "" + getClass().getName() + " .Stampa(): fine " );
      return IWebConstants.PG_DOWNLOAD;
    }
}