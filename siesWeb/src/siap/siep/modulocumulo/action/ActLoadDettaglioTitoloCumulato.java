package siap.siep.modulocumulo.action;


/**
* <p>Title: ActLoadDettaglioTitoloCumulato</p>
* <p>Description: Classe Action per la load dettaglio di TitoloCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;

import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

public class ActLoadDettaglioTitoloCumulato extends ActionModuloCumulo implements ICostantiTitoloCumulato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
 /*****************************************************************************
  * Azione di caricamento della pagina di Dettaglio dei dati. 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    super.getDatiIstruttoria();
    
    //==========================================
    // Recupera la key del record da Visualizzare 
    //==========================================
    BigDecimal lIdTitoloCumulato  = getRequestBigDecimalParameter ( CAMPO_ID_TITOLO_CUMULATO) ;
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lIdTitoloCumulato = "+lIdTitoloCumulato);

    //========================================== 
    // Recupera i dati del record da modificare  
    //========================================== 
    ITitoloCumulato lCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
    TitoloCumulatoModel lTitMod = lCtrl.ExRicercaTitoloCumulatoById( lIdTitoloCumulato);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lTitMod = "+lTitMod);
    
    if ("02".equals (lTitMod.getCodTipoProvvedimento())) {
      String [] lUfficiSorv = new String[] {"UDS","TDS","UDSM"};
      if (!Arrays.asList(lUfficiSorv).contains(lTitMod.getCodTipoAutoritaEmittente())){
        // Rimappo il codice per poterlo gestire nella jsp
        lTitMod.setCodTipoProvvedimento("02bis"); // Decreto Penale
      }
    }    

    
    // Aggiungo Procedimento Se presente 
    ProcedimentoCumulatoModel lProcMod = lCtrl.ExRicercaProcedimentoCumulatoByIdTitolo( lIdTitoloCumulato);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lProcMod = "+lProcMod);
    lTitMod.setProcedimentoCumulato(lProcMod);
    
    
    // Aggiungo Soggetto Cumulato Se presente 
    SoggettoCumulatoModel lSoggMod = lCtrl.ExRicercaSoggettoCumulatoByIdTitolo( lIdTitoloCumulato);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSoggMod = "+lSoggMod);
    lTitMod.setSoggettoCumulato(lSoggMod);
    
    
    
    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
//    if (lTitMod==null){ 
//      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
//      // Specificare eventualmente la jump page dove verrà ridirezionata la 
//      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
//      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
//      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
//      //      della root_dir es /siap/frame.htm  
//      setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
//      return IWebConstants.PG_MESSAGE;
//    }

    //==================================================== 
    // Passa il Model alla componente di visualizzazione  
    //==================================================== 
    setRequestAttribute("titolocumulato", lTitMod);

    //=========================================================
    // Restituisce la pagina di Visualizzazione del Dettaglio.
    //=========================================================
    // Imposta Modalità.
    setRequestAttribute("modalita", "D");

    return PG_LOAD_DETTAGLIOTITOLOCUMULATO;
  }
}