package siap.siep.istanza.action;

import java.math.BigDecimal;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.web.ActionSiap;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
* <p>Title: ActCancellaIstanza</p>
* <p>Description: Classe Action per la cancellazione dell'istanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaIstanza extends ActionSiap implements ICostantiIstanza
{
 public String processRequest() throws Exception
  {
    String motivazioni =null;
    BigDecimal lId = this.getRequestBigDecimalParameter(CAMPO_ID_ISTANZA);

    //Riempie il model
    IstanzaModel lIstaMod = new IstanzaModel();
    CampoNotaModel lCampoMod = new CampoNotaModel();

    IIstanza lCtrl = SIEPLookupRemote.getIstanzaRemote();
    lIstaMod=lCtrl.ExRicercaIstanzaByKey(lId);

    if(lIstaMod != null)
    {
      // istanza cancellazione logica
        if(!this.isRequestParameterNullObj("motivazioni"))
          motivazioni = this.getRequestStringParameter("motivazioni");

        lIstaMod.setCodStatoIstanza("C");
        lCampoMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
        lCampoMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
        lCampoMod.setDataInserimento(DateUtils.getSysDate());
        lCampoMod.setDescr(motivazioni);

        lCtrl.ExAnnulamentoIstanzaInserisciCampoNota(lIstaMod,lCampoMod);
    }

     String lPage = null;

     if(!this.isRequestParameterNullObj(IWebConstants.GOTO_PAGE) &&
        this.getRequestStringParameter(IWebConstants.GOTO_PAGE) != null &&
        !this.getRequestStringParameter(IWebConstants.GOTO_PAGE).equals(""))
     {
       lPage = this.getRequestStringParameter(IWebConstants.GOTO_PAGE);
     }
     else
     {
       lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
           "=siap.siep.istanza.action.ActLoadRicercaIstanza";
     }

    return lPage ;
  }
}