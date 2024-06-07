package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.util.SIUSLookupRemote;


/**
 * Inserimento dell'ordinanza di Revoca e Conversione Pena Pecuniaria Sostitutiva
 * 
 * n.b inserisce un nuovo record su RICHIESTA_CONVERSIONE con i dati inseriti in form
 * 
 * @since MEV_2023-35
 * */
public class ActInserisciOrdinanzaConversioneRevocaPPS extends ActInserisciOrdinanzaUDS {
  
  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws Exception {
    siesLogger.debug("ActInserisciOrdinanzaConversioneRevocaPPS.processRequest()");
    return super.processRequest();
  }
  
  public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
      throws F3BException 
  {
    siesLogger.debug("ActInserisciOrdinanzaConversioneRevocaPPS.inserimento()");
    // Dati relativi alle Richieste Conversioni Pene Pecuniarie.
    RichiestaConversioneModel lRicConv = new RichiestaConversioneModel();
    
    //String lCodTipoRichiesta = "";

    String lTipoRichiestaCPP = this.getRequestStringParameter(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE);

    // Importo da convertire
    String lValoreIntRata = this.getRequestStringParameter (ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTO_MULTA);
    String lValoreDecRata = this.getRequestStringParameter (ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTO_MULTA);
    
    BigDecimal lValoreRata = null;
    if ("".equals(lValoreIntRata.trim())) 
      lValoreIntRata = "0";
    
    lValoreRata = new BigDecimal(lValoreIntRata);
    
    if (!"".equals(lValoreDecRata.trim())) {
      lValoreRata.add(new BigDecimal("." + lValoreDecRata.trim()));
    }
    
    lRicConv.setImportoMulta(lValoreRata);
    
    // Tipo sanzione sostitutiva 
    String lCodTipoSanzione = this.getRequestStringParameter(ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE);
    lRicConv.setCodTipoSanzione(lCodTipoSanzione);

    // DUrata sanzione sostitutiva
    String lNumGiorniDurataEsito = this.getRequestStringParameter(ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_SS);
    String lNumMesiDurataEsito   = this.getRequestStringParameter(ICostantiSiusPenaPecuniaria.CAMPO_NUM_MESI_SS);
    String lNumAnniDurataEsito   = this.getRequestStringParameter(ICostantiSiusPenaPecuniaria.CAMPO_NUM_ANNI_SS);

    lRicConv.setDurataEsitoAnni   (lNumAnniDurataEsito.trim().length()>0   ? new BigDecimal(lNumAnniDurataEsito)   : null);
    lRicConv.setDurataEsitoMesi   (lNumMesiDurataEsito.trim().length()>0   ? new BigDecimal(lNumMesiDurataEsito)   : null);
    lRicConv.setDurataEsitoGiorni (lNumGiorniDurataEsito.trim().length()>0 ? new BigDecimal(lNumGiorniDurataEsito) : null); 

    // inserimento
    IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
    OrdinanzaEventoTenoriGProcModel lModRet = IDepOrdCtrl.ExInserisciOrdinanzaRevocaConversionePPS(aOrdEveTenGP, lRicConv);

    if (lModRet == null)
      throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO.");
    return lModRet;
  }  
}
