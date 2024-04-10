package siap.siep.pagoPA.util;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.sico.utente.model.UtenteModel;
import siap.siep.pagoPA.controller.IErroriSiesPagopa;
import siap.siep.pagoPA.model.ErroriSiesPagopaModel;
import siap.siep.util.SIEPLookupRemote;

public class RegistraErrorePagopaUtil {
  private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);
  
  public void registraErrore(BigDecimal idFascicoloSiep, BigDecimal idEvento, UtenteModel aUtenteModel){
    
    try {
      ErroriSiesPagopaModel erroreModel = new ErroriSiesPagopaModel();
      
      erroreModel.setIdFascicoloSiep(idFascicoloSiep);    
      erroreModel.setIdEvento(idEvento);
      
      erroreModel.setAzioneContestoJava ("Test azione");
      erroreModel.setDescrizioneFunzione ("Test decs Funzione");
      erroreModel.setCodUtente  (aUtenteModel.getUserId());
      erroreModel.setCodUfficio (aUtenteModel.getUfficioUtente().getCodUfficio());
      erroreModel.setErroreEsecuzione ("Test Errore Esecuzione");
      erroreModel.setDataInserimento (DateUtils.getSysDate());
      erroreModel.setDataVisualizzazione (null);
      erroreModel.setCodUtenteVisualizzazione (null);
      
      siesLogger.warn("Inserimento errore comunicazione PagoPa\n "+erroreModel);
      
      IErroriSiesPagopa lCtrl = SIEPLookupRemote.getErroriSiesPagopaRemote();
      lCtrl.ExInserisciErroreSiesPagopa (erroreModel);
    } catch (Exception e){
      siesLogger.error("Errore in fase di registrazione ErroriSiesPagopaModel sul DB",e);
      // NON rilancio l'eccezione. Di fatto trattasi di tracciatura
    }    
  }
  
  public void registraErrore (ErroriSiesPagopaModel aErroreModel){
    
    try {
     
      siesLogger.warn("Inserimento errore comunicazione PagoPa\n "+aErroreModel);
      
      IErroriSiesPagopa lCtrl = SIEPLookupRemote.getErroriSiesPagopaRemote();
      
      // Verifi
      lCtrl.ExInserisciAggiornaErroreSiesPagopa (aErroreModel);
      
      //lCtrl.ExInserisciErroreSiesPagopa (aErroreModel);
    } catch (Exception e){
      siesLogger.error("Errore in fase di registrazione ErroriSiesPagopaModel sul DB",e);
      // NON rilancio l'eccezione. Di fatto trattasi di tracciatura
    }    
  }
  
  public void rimuoviErrore (ErroriSiesPagopaModel aErroreModel){
    try {
      siesLogger.warn("Inserimento errore comunicazione PagoPa\n "+aErroreModel);
      IErroriSiesPagopa lCtrl = SIEPLookupRemote.getErroriSiesPagopaRemote();
      
      // 
      lCtrl.ExRimuoviErroreSiesPagopa (aErroreModel);
      
      //lCtrl.ExInserisciErroreSiesPagopa (aErroreModel);
    } catch (Exception e){
      siesLogger.error("Errore in fase di registrazione ErroriSiesPagopaModel sul DB",e);
      // NON rilancio l'eccezione. Di fatto trattasi di tracciatura
    }    
  }
}
