package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;

import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>Title: ActRicercaPenaComplessivaCumulo</p>
 * <p>Description: Classe Action per la ricerca di PenaComplessiva</p>
 * <p>    in ambito Cumulo (Pena_complessiva_Cumulo) </p>
 * @version 4.0
 */

public class ActRicercaPenaComplessivaCumulo extends ActionModuloCumulo implements ICostantiPenaComplessivaCumulo
{
  public String processRequest() throws Exception
  {
    BigDecimal lIdTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
    
    //==========================================================================
    // Recupero i dati dell'IStruttoria e Titolo Cumulato da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoria = super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();      

    DettaglioPenaComplessivaCumuloModel lDetCumPena = new DettaglioPenaComplessivaCumuloModel() ;
    IPenaComplessivaCumulo lCtrl = SIEPLookupRemote.getPenaComplessivaCumuloRemote();
    lDetCumPena = lCtrl.ExRicercaPenaCompSanzioneSostContinuazioniCumByIdTitolo(lIdTitolo);
    
    if(lDetCumPena == null)
    { 
      if ("A".equals(lIstruttoria.getFlagStato())) 
      {
        String lPage = "";
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadInserisciPenaComplessivaCumulo";
        return lPage;
      }
      else {
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Principale non presente");
        return IWebConstants.PG_MESSAGE;
      }
    }
    else
    {
      setRequestAttribute("dettaglioPenaComplessivaCum", lDetCumPena);
      return PG_RICERCAPENACOMPLESSIVA_CUM;
    }
  }
}
