package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.web.ActionSiap;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
* <p>Title: ActCancellaInoltroNuovaIstanza</p>
* <p>Description: Classe Action per la cancellazione dell'inoltro </p>
* <p> e della disposizione del PM sull'istanza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile S.r.l.</p>
* @version 5.0
*/

public class ActCancellaInoltroNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
 public String processRequest() throws Exception
  {
    String motivazioni =null;
    BigDecimal lId = this.getRequestBigDecimalParameter("IdEvento");
    BigDecimal lIdIstanza = this.getRequestBigDecimalParameter("IdIstanza");
    String tipoOper=this.getRequestStringParameter("TipoOp");
    String tipoProv=this.getRequestStringParameter("TipoProvvedimento");
    String lPage = null;
    CampoNotaModel lCampoMod = new CampoNotaModel();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("Nella ActCancellaInoltroNuovaIstanza");
    INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();

  // istanza cancellazione logica
    if(!this.isRequestParameterNullObj("motivazioni"))
      motivazioni = this.getRequestStringParameter("motivazioni");

    lCampoMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lCampoMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lCampoMod.setDataInserimento(DateUtils.getSysDate());
    lCampoMod.setDescr(motivazioni);

    if (tipoProv.equals("Inoltro")){
    	lCtrl.ExAnnullamentoInoltroIstanza(lId,lIdIstanza,lCampoMod, tipoOper);
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.nuovaistanza.action.ActLoadInoltroPM";
    }
    else {
    	lCtrl.ExAnnullamentoDisposizioneIstanza(lId,lIdIstanza,lCampoMod, tipoOper);
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.nuovaistanza.action.ActLoadDisposizioniPM";
    }	
    
    return lPage ;
  }
}