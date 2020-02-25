package siap.siep.reatopredisposto.action;
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.reatopredisposto.controller.ReatoPredispostoController;
import siap.siep.reatopredisposto.model.ReatoPredispostoModel;
import siap.web.ISIAPCostantiWeb;


public class ActCancellaReatoPredisposto extends ActionSiap implements ICostantiReatoPredisposto
{
  public String processRequest() throws Exception
  {

      ReatoPredispostoModel lReato=new ReatoPredispostoModel();

      lReato.setIdReatoPredisposto((BigDecimal)getRequestBigDecimalParameter(CAMPO_ID_REATO_PREDISPOSTO));
      //IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
      
      ReatoPredispostoController lReaCtrl = new ReatoPredispostoController();
      lReato = lReaCtrl.ExRicercaReatoPredispostoByKey(lReato.getIdReatoPredisposto());

      lReaCtrl.ExCancellaReatoPredisposto(lReato);

      String lPage="";
      //lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.reatopredisposto.action.ActRicercaReatoPredisposto";
      lPage = ISIAPCostantiWeb.PG_PAGINA_VUOTA;
      // &"+ICostantiReatoPredisposto.CAMPO_ID_REATO_PREDISPOSTO+"="+lReato.getIdReatoPredisposto() 
      return lPage; //restituisce la jsp di VIEW


  }
}
