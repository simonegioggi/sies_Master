package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.sico.web.ActionSiap;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
* <p>Title: ActLoadDettaglioStatoEsecTitoloCumulato</p>
* <p>Description: Classe Action per la load dettaglio di StatoEsecTitoloCumulato</p>
* Carica il dettaglio del procedimento generico
* @version 1.0
*/

public class ActLoadDettaglioStatoEsecTitoloCumulato extends ActionSiap implements ICostantiStatoEsecTitoloCumulato
{
  public String processRequest() throws F3BException {

    BigDecimal lIdStatoEsecTitoloCumulato  = getRequestBigDecimalParameter ( CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO) ;

    IStatoEsecTitoloCumulato lCtrl = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    StatoEsecTitoloCumulatoModel lStaMod = lCtrl.ExRicercaStatoEsecTitoloCumulatoById( lIdStatoEsecTitoloCumulato);

    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lStaMod==null){ 
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
      setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
      return IWebConstants.PG_MESSAGE;
    }

    //==================================================== 
    // Passa il Model alla componente di visualizzazione  
    //==================================================== 
    setRequestAttribute("Provvedimento", lStaMod);

    return PG_LOAD_DETTAGLIO_STATO_ESEC_TITOLO_CUMULATO;
  }
}