package siap.siepe.attivita.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.controller.TemplateManager;
import siap.sico.util.report.ReportGenerator;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.jms.controller.ITrasmissioneJMS;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaAttivita </p>
 * <p>Description: Classe Azione demandata all'attuazione della stampa dell'Attività.
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaAttivita  extends ActionSiap
 implements ICostantiAttivita
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  // ID TEMPLATE fissato per testare prima di implementare la COMBO
  public final String TEMPLATE_ATTIVITA_01 = "SIEPE_ATT_001";

  public String processRequest() throws Exception
  {
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug(this.getClass().getName() + ".processRequest : inizio");

	  
     // Model Attivita
     AttivitaModel lAttivita = null;
     // Interfaccia a AttivitaController
    IAttivita lAttCtrl = null;

     // Documento di stampa
     ByteArrayOutputStream lReport = null;

    // Preleva dalla request la chiave dell'attività come parametro
    BigDecimal lKeyAttivita = getRequestBigDecimalParameter(ICostantiAttivita.CAMPO_ID_ATTIVITA );

    // Ricerca Attivita
    lAttCtrl = SIEPELookupRemote.getAttivitaRemote();
    lAttivita = lAttCtrl.ExRicercaAttivitaByKey(lKeyAttivita);

    // Se il documento è validato si fornisce la stampa già memorizzata
    if (lAttivita.getFlagDocumentoRegistrato() != null && lAttivita.getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
    {
       lReport = lAttCtrl.ExGetDocumento(lAttivita);
    }
    else
    {
       // Creazione del documento di stampa

       // Si ricava il template da utilizzare
       // Si fissa il valore di default ad un modello di prova per la stampa Attività
       String lIdTemplate = TEMPLATE_ATTIVITA_01;
       if(!isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE))
       {
          lIdTemplate = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);
       }

       lReport = generaStampa(lAttivita.getIdAttivita(), lIdTemplate);

       // Valorizzazione del model dell'Attivita
       lAttivita.setDataAggiornamento(DateUtils.getSysDate());
       lAttivita.setCodOperatoreAggiornamento(getCodUtenteConnesso());
       lAttivita.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
       lAttivita.setFlagDocumentoRegistrato("N");

       // Inserimento del documento generato nel model da archiviare
       ByteArrayInputStream  lByteArrayInput= new ByteArrayInputStream( lReport.toByteArray() );
       lAttivita.setDocBlobIn( lByteArrayInput );
       lAttCtrl.ExUpdateDocument(lAttivita);
    }

    //Prepara la pagina di destinazione
    if (lReport != null)
      setRequestAttribute("report", lReport);
    else
      throw new SIEPEException(SIEPEException.USER_MESSAGE, "Nessun documento è stato generato. Funzione non ancora disponibile!");
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest : fine");

    return IWebConstants.PG_DOWNLOAD_NEW;
  }

  /**
   * Funzione per la creazione della stampa Attività.
   * La funzione utilizza il template, il cui riferimento è passato come argomento.
   * Crea il documento XML e poi da questo quello RTF che viene restituito.
   * @param aIdAttivita
   * @param lIdTemplate
   * @return ByteArrayOutputStream
   * @throws Exception
   */
  public ByteArrayOutputStream generaStampa(BigDecimal aIdAttivita, String lIdTemplate ) throws Exception
  {
	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.debug(this.getClass().getName() + ".generaStampa : inizio");

     // Documento di stampa generato
     ByteArrayOutputStream lReport = null;
     // Documento XML contenente i dati oggetto della stampa
     TreeModel lXMLDocument = null;
     // Report generator per la costruzione del report
     ReportGenerator lReportCtrl = null;

     // Preleva dalla sessione i dati dell'utente connesso.
     String lCodiceUfficio     = getCodUfficioUtenteConnesso();
 
     // Lettura dei dati del Fascicolo in sessione
     FascicoloSiepeEstesoModel lFascicoloEsteso = (FascicoloSiepeEstesoModel) getSessionAttribute("FascicoloSiepeEsteso");

     // Generazione del documento XML
     ITrasmissioneJMS lCtrlMess = SIEPELookupRemote.getTrasmissioneJMSRemote();
     lXMLDocument = lCtrlMess.getTreeModelForAttivita    (aIdAttivita,   lFascicoloEsteso, getUfficioUtenteConnesso(), getUtenteConnesso() );

     // Si ricava il nome del template
      String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);

      lReportCtrl = new ReportGenerator(lCodiceUfficio);
      lReport = (ByteArrayOutputStream) lReportCtrl.generateDocument(lXMLDocument,lNomeTemplate);
	 
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug(this.getClass().getName() + ".generaStampa : fine");

  return lReport;
}

}





