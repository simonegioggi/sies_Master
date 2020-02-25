package siap.siep.reato.action;
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.ReatoContinuazioneController;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;


public class ActCancellaContinuazioneReati extends ActionSiap implements ICostantiReato
{
  public String processRequest() throws Exception
  {
    boolean proceed=true;
    FascicoloSiepModel lFascMod=new FascicoloSiepModel();

    lFascMod.setIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() );
    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
    lFascMod=lCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());
    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
    {
      proceed=false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile cancellare la continuazione dei reati");
    }
    if (lFascMod.getFlagValidato().equalsIgnoreCase("S"))
    {
      proceed=false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Il fascicolo è stato validato! Impossibile cancellare la continuazione dei reati");
    }
     if (proceed)
    {
      ReatoModel lReato=new ReatoModel();
      
      lReato.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
      lReato.setProgrReato((BigDecimal)getRequestBigDecimalParameter(CAMPO_PROGR_REATO));
      lReato.setProgrCircostanza(new BigDecimal(1));
      
      ReatoContinuazioneController lReaCtrl = new ReatoContinuazioneController();
      
      lReaCtrl.ExCancellazioneContinuazioneReati(lReato);
      
    }

      String lPage="";
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() ;
      return lPage; //restituisce la jsp di VIEW


  }
}
