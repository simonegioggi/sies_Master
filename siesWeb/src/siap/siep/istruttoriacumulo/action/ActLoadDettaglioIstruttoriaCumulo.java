package siap.siep.istruttoriacumulo.action;


/**
* <p>Title: ActLoadDettaglioIstruttoriaCumulo</p>
* <p>Description: Classe Action per la load dettaglio di IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
//import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActLoadDettaglioIstruttoriaCumulo extends ActionSiap implements ICostantiIstruttoriaCumulo
{
 /*****************************************************************************
  * Azione di caricamento della pagina di Dettaglio dei dati. 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    
    //FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    //==========================================
    // Recupera la key del record da Visualizzare 
    //==========================================
    BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO) ;

    //========================================== 
    // Recupera i dati del record da visualizzare  
    //========================================== 
    IIstruttoriaCumulo lCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    IstruttoriaCumuloModel lIstMod = lCtrl.ExRicercaIstruttoriaCumuloById( lIdIstruttoriaCumulo);

    //==================================================== 
    // Passa il Model alla componente di visualizzazione  
    //==================================================== 
    setRequestAttribute("IstruttoriaCumulo", lIstMod);

    //=========================================================
    // Restituisce la pagina di Visualizzazione del Dettaglio.
    //=========================================================

    return PG_LOAD_DETTAGLIO_ISTRUTTORIA_CUMULO;
  }
}