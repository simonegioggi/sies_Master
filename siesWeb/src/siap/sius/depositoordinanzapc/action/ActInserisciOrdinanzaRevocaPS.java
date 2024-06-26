package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.util.SIUSLookupRemote;

/**
 * Inserimento dell'ordinanza di Revoca e Conversione Pena  Sostitutiva
 * 
 * n.b inserisce un nuovo record su ESECUZIONE_SANZ_SOST con i dati inseriti in form
 * 
 * @since MEV_2023-35
 * */
public class ActInserisciOrdinanzaRevocaPS extends ActInserisciOrdinanzaUDS {
  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws Exception {
    siesLogger.debug("ActInserisciOrdinanzaRevocaPS.processRequest()");
    return super.processRequest();
  }
  
  public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
      throws F3BException 
  {
    siesLogger.debug("ActInserisciOrdinanzaRevocaPS.inserimento()");
    
    // Dati relativi alla eseuzione san sot.

    String[] lEsitiTenore = this.getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE);

    boolean isRevoca = false;
    for (int i = 0 ; i <lEsitiTenore.length; i++) {
      siesLogger.debug("lEsitoTenore = "+lEsitiTenore[i]);
      
      if (   "3114".equals(lEsitiTenore[i]) || "3115".equals(lEsitiTenore[i])
          || "3120".equals(lEsitiTenore[i]) || "3121".equals(lEsitiTenore[i])
          )
      {
        siesLogger.debug("esito 'revoca', procedo a registrare EsecuzioneSanzioneSostitutivaModel ");
        isRevoca = true;
      }      
    }
    
    EsecuzioneSanzioneSostitutivaModel lEsecSanSost = null;   
    if (isRevoca){
      siesLogger.debug("isRevoca ");
      lEsecSanSost = new EsecuzioneSanzioneSostitutivaModel();  
      
      lEsecSanSost.setAnnoS07  (mFasGPMod.getGeneraleProcedimentoModel().getAnnoS1());
      lEsecSanSost.setProgrS07 (mFasGPMod.getGeneraleProcedimentoModel().getProgrS1());
      
      lEsecSanSost.setCodTipoSanzione (getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE));
      
      lEsecSanSost.setNumAnniSanzione   (getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE));
      lEsecSanSost.setNumMesiSanzione   (getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_NUM_MESI_SANZIONE));
      lEsecSanSost.setNumGiorniSanzione (getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_NUM_GIORNI_SANZIONE));
      
      lEsecSanSost.setGenPridGeneraleProcedimento(aOrdEveTenGP.getGeneraleProcedimento().getIdGeneraleProcedimento());
      lEsecSanSost.setDepOpidDepositoOrdinanzaPc (aOrdEveTenGP.getOrdinanza().getIdDepositoOrdinanzaPc());
      
      lEsecSanSost.setCodOperatoreInserimento (aOrdEveTenGP.getEvento().getCodOperatoreInserimento());
      lEsecSanSost.setCodUfficioInserimento   (aOrdEveTenGP.getEvento().getCodUfficioInserimento());
      lEsecSanSost.setDataInserimento         (aOrdEveTenGP.getEvento().getDataInserimento());
    }



    // inserimento
    IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
//    OrdinanzaEventoTenoriGProcModel lModRet = IDepOrdCtrl.ExInserisciOrdinanzaRevocaConversionePPS(aOrdEveTenGP, lRicConv);
    OrdinanzaEventoTenoriGProcModel lModRet = IDepOrdCtrl.ExInserisciOrdinanzaRevocaPS(aOrdEveTenGP, lEsecSanSost);

    if (lModRet == null)
      throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO.");
    return lModRet;
  }    
}
