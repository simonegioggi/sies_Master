package siap.siep.circostanza.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

public class ActCancellaCircostanza extends ActionSiap implements ICostantiCircostanza
{
  public String processRequest() throws Exception
  {
    boolean proceed=true;

    FascicoloSiepModel lFascMod = new FascicoloSiepModel();

    lFascMod.setIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() );
    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
    lFascMod=lCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());

    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
      proceed=false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile cancellare Capi di Imputazione");
    }
    if (lFascMod.getFlagValidato().equalsIgnoreCase("S"))
    {
      proceed=false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Il fascicolo è stato validato! Impossibile cancellare Capi di Imputazione");
    }
     if (proceed)
    {
      siap.siep.circostanza.model.CircostanzaModel lCircostanza=new CircostanzaModel();

      lCircostanza.setIdCircostanza((BigDecimal)getRequestBigDecimalParameter(CAMPO_ID_CIRCOSTANZA));

      ICircostanza lReaCtrl = SIEPLookupRemote.getCircostanzaRemote();
      lReaCtrl.ExCancellaCircostanza(lCircostanza);

    }

      String lPage="";
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() ;

      return lPage; //restituisce la jsp di VIEW
  }
}
