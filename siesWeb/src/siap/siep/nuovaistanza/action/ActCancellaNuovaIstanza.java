package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
* <p>Title: ActCancellaNuovaIstanza</p>
* <p>Description: Classe Action per la cancellazione dell'istanza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile S.r.l.</p>
* @version 5.0
*/

public class ActCancellaNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza
{
 public String processRequest() throws Exception
  {
    String motivazioni =null;
    BigDecimal lId = this.getRequestBigDecimalParameter(CAMPO_ID_NUOVA_ISTANZA);
    String lPage = null;
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
    "=siap.siep.nuovaistanza.action.ActRicercaNuovaIstanza";

    //Riempie il model
    NuovaIstanzaModel lIstaMod = new NuovaIstanzaModel();
    CampoNotaModel lCampoMod = new CampoNotaModel();

    INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
    lIstaMod=lCtrl.ExRicercaNuovaIstanzaById(lId);

    if(lIstaMod != null)
    {
      // istanza cancellazione logica
        if(!this.isRequestParameterNullObj("motivazioni"))
          motivazioni = this.getRequestStringParameter("motivazioni");

        lIstaMod.setCodStatoIstanza("08");
        lCampoMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
        lCampoMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
        lCampoMod.setDataInserimento(DateUtils.getSysDate());
        lCampoMod.setDescr(motivazioni);

        lCtrl.ExAnnulamentoIstanza(lIstaMod,lCampoMod);
        
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo";
        lPage += "&" + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lIstaMod.getFasSieIdFascicoloSiep().toString();

    }

    return lPage ;
  }
}