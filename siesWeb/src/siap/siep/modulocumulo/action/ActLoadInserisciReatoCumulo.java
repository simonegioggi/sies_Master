package siap.siep.modulocumulo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciReatoCumulo</p>
* <p>Description: Classe Action per la load inserisci Reato</p>
* <p>      in ambito Cumulo (tabelle Reato_Cumulo)</p>
*/

public class ActLoadInserisciReatoCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo
{
  public String processRequest() throws Exception
  {
    //==========================================================================
    // Recupero i dati da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();   
    
    Option lOption = new Option( DecodificheManager.getInstance().getTipoReato());
    setRequestAttribute("TipiReato", "" + lOption );
    
    lOption  = new Option( DecodificheManager.getInstance().getTipoFonteReato() );
    setRequestAttribute("TipiFontiReato", "" + lOption );
    
    lOption  = new Option( DecodificheManager.getInstance().getSottonumerazione() );
    setRequestAttribute("TipiSottonumerazione", "" + lOption );
    
    lOption  = new Option( DecodificheManager.getInstance().getPeriodoConsumazione() );
    setRequestAttribute("PeriodoConsumazione", "" + lOption );
    
    lOption  = new Option( DecodificheManager.getInstance().getTipoPenaDetentiva() );
    setRequestAttribute("TipiPeneDetentive", "" + lOption );

    setRequestAttribute("modalita", "I");

    return PG_LOAD_INSERISCIREATO_CUM;
  }
}