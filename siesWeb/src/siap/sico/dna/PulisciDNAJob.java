package siap.sico.dna;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BProperties;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.jms.controller.IPresaInCarico;
import siap.sico.jms.model.PresaInCaricoModel;
import siap.sico.util.SICOLookupRemote;

/**
 * Nuovo job per schedulare la cancellazione delle tracce di attività degli utenti DNA
 * 
 * Attualmente cancella i dati solo dalla tabella MESSAGGIO, unica tabella su cui è tracciata l'attività
 * 
 * E' possibile estendere l'attività del job ad altri dati es invocasione dell apulisci altre BDI o di Stored 
 * sviluppate ad hoc
 * 
 * 
 * Per la schedulazione del job verificare il contenuto del file f3b.properties
 * # Nuovo JOB di pulizia DNA
 * DNASchedulerEnabled=true
 * DNACronExpression=0 0/1 * * * ?
 * 
 * @since MEV_2024-DNA
 */
public class PulisciDNAJob implements Job {
  
  public static final String JOB_NAME = "PuliziaDNAJob";
  public static final String JOB_GROUP = "PuliziaDNAGroup";
  public static final String JOB_DESC = "Demone responsabile della pulizia delle attivita'' svolte dalla DNA";
  
  public static final String JOB_DESC_COL = "xxxxxxxxxxxxxxx";
  
  private static Logger dnaLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public PulisciDNAJob() {
  }
  
  public void execute(JobExecutionContext arg0) throws JobExecutionException {

    MDC.put("utente", "DNAJOB");
    MDC.put("ufficio", "xxx");

    dnaLogger.debug("===================================================");
    dnaLogger.debug(" Avvio job di Pulizia DNA                          ");
    dnaLogger.debug("===================================================");
    dnaLogger.debug("");
    


    try {
      dnaLogger.debug(" Pulizia tabella MESSAGGIO... ");
      String lCodUffDNA = F3BProperties.getProperty("DNACodUfficio");
      dnaLogger.debug(" lDNACodUfficio = >"+lCodUffDNA+"<");
      
      /*
      String lCodUffDNA = "05809100302";
      Date lDataInviaDal = DateUtils.getDate("01/08/2024","dd/MM/yyyy");
      Date lDataInviaAl  = DateUtils.getDate("31/08/2024","dd/MM/yyyy");
      */
      //String lCodUffDNA = "05809100302";
      
      // Eventuale intrevallo di date
      Date lDataInviaDal = null; // DateUtils.getDate("18/07/2024","dd/MM/yyyy");
      Date lDataInviaAl  = null; // DateUtils.getDate("31/08/2024","dd/MM/yyyy");      
      
      IMessaggio lCTRLMessaggio = JMSLookupRemote.getMessaggioRemote();
      
      dnaLogger.debug("Lancio Cancellazione MESSAGGIO...");
      lCTRLMessaggio.ExCancellaMessaggioByCodUfficio (lCodUffDNA, lDataInviaDal, lDataInviaAl);
      dnaLogger.debug("... Cancellazione MESSAGGIO terminata");

    } catch (Exception e) {
      dnaLogger.error("Eccezione in fase di lancio del job di Pulizia MESSAGGIO ", e);
    } finally {
      dnaLogger.debug(" Pulizia tabella MESSAGGIO Terminata ");
    }
    
    // Aggiungere eventuali altre attivita' da pilotare
    try {    
      dnaLogger.debug(" Inizio Pulizia fascicoli presi in carico dalla DNA... ");
      String lDNACodUfficio = F3BProperties.getProperty("DNACodUfficio");
      dnaLogger.debug(" lDNACodUfficio = >"+lDNACodUfficio+"<");
      
      IPresaInCarico lCtrlPresaInCarico = SICOLookupRemote.getPresaInCaricoRemote();      
      Vector <PresaInCaricoModel> lListaPreseInCaricoDNA = lCtrlPresaInCarico.ExRicercaPresaInCaricoDNA (lDNACodUfficio);
      dnaLogger.debug(" Record Presa In Carico Trovati: "+lListaPreseInCaricoDNA.size());
      
      BigDecimal lLastIdFascicolo = null;
      boolean isSoloDNA=true;
      for (PresaInCaricoModel lPresaInCaricoModel : lListaPreseInCaricoDNA) {
        dnaLogger.debug("isSoloDNA = "+isSoloDNA);
        if (lLastIdFascicolo==null)
          lLastIdFascicolo = lPresaInCaricoModel.getFasSieIdFascicoloSiep();
        
        if (lPresaInCaricoModel.getFasSieIdFascicoloSiep().compareTo(lLastIdFascicolo)==0) {
          dnaLogger.debug(" getCodUfficioPresaInCarico = >"+lPresaInCaricoModel.getCodUfficioPresaInCarico()+"<");
          if (!lDNACodUfficio.equals(lPresaInCaricoModel.getCodUfficioPresaInCarico()))
            isSoloDNA = false;
        }
        else {
          // Ho cambiato fascicolo lavoro il precedente
          dnaLogger.debug("Ho cambiato fascicolo lavoro il precedente");
          dnaLogger.debug("Cancello la tracciatura su PRESA_IN_CARICO");
          MDC.put("utente", "Jxxx"); // Cambio utente per scartare loggature idFasciolo
          lCtrlPresaInCarico.ExCancellaPreseInCaricoDNAbyIdFascicolo(lLastIdFascicolo, lDNACodUfficio);
          MDC.put("utente", "DNAJOB");
          dnaLogger.debug("isSoloDNA = "+isSoloDNA);
          if (isSoloDNA) {
            dnaLogger.debug("Fascicolo trattato solo dalla DNA lo posso cancellare anche dal DB...");
            MDC.put("utente", "Jxxx"); // Cambio utente per scartare loggature idFasciolo
            lCtrlPresaInCarico.ExCancellaFascicoloPresoInCaricoDNAbyIdFascicolo(lLastIdFascicolo);
          } else {
            MDC.put("utente", "DNAJOB");
            dnaLogger.debug("Fascicolo trattato anche da altri uffici non lo cancello dal DB");
          }
          
          // Devo inizializzare il nuovo fascicolo
          isSoloDNA = true;
          if (!lDNACodUfficio.equals(lPresaInCaricoModel.getCodUfficioPresaInCarico()))
            isSoloDNA = false;
        }
        
        lLastIdFascicolo = lPresaInCaricoModel.getFasSieIdFascicoloSiep();
        
      }
      
      // Test fuori ciclo per l'ultimo record
      if (lLastIdFascicolo!=null) { // E'  presente almeno un record nella lista        
        dnaLogger.debug("Ultimo test fuori ciclo...");
        dnaLogger.debug("Cancello la tracciatura su PRESA_IN_CARICO");
        MDC.put("utente", "Jxxx"); // Cambio utente per scartare loggature idFasciolo
        lCtrlPresaInCarico.ExCancellaPreseInCaricoDNAbyIdFascicolo(lLastIdFascicolo, lDNACodUfficio);
        dnaLogger.debug("isSoloDNA = "+isSoloDNA);
        if (isSoloDNA) {
          MDC.put("utente", "DNAJOB");
          dnaLogger.debug("Fascicolo trattato solo dalla DNA lo posso cancellare anche dal DB...");
          MDC.put("utente", "Jxxx"); // Cambio utente per scartare loggature idFasciolo
          lCtrlPresaInCarico.ExCancellaFascicoloPresoInCaricoDNAbyIdFascicolo(lLastIdFascicolo);
          MDC.put("utente", "DNAJOB");
        } else {
          dnaLogger.debug("Fascicolo trattato anche da altri uffici non lo cancello dal DB");
        }
      }
      
    } catch (Exception e) {
      MDC.put("utente", "DNAJOB");
      dnaLogger.error("Eccezione in fase di lancio del job di Pulizia DNA ", e);
    } finally {
      //dnaLogger.debug(" Pulizia tabella MESSAGGIO Terminata ");
    }
    
    MDC.put("utente", "DNAJOB");
    dnaLogger.debug("===================================================");
    dnaLogger.debug(" job di Pulizia DNA Terminato                      ");
    dnaLogger.debug("===================================================");
    dnaLogger.debug("");
  }
}
