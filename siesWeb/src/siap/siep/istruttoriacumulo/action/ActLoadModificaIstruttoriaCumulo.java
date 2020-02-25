package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadModificaIstruttoriaCumulo</p>
* <p>Description: Classe Action per la load modifica di IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Eutelia S.p.A</p>
* @deprecated Action generata del framework, Non Utilizzata al momento, codice
* non verificato
* @version 1.0
*/
public class ActLoadModificaIstruttoriaCumulo extends ActionSiap implements ICostantiIstruttoriaCumulo
{
 /*****************************************************************************
  * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare i tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    //==========================================
    // Recupera la key del record da modificare 
    //==========================================
    BigDecimal lIdIstruttoriaCumulo       = getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO) ;

    //========================================== 
    // Recupera i dati del record da modificare  
    //========================================== 
    IIstruttoriaCumulo lCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    IstruttoriaCumuloModel lIstruttoriaCumulo = lCtrl.ExRicercaIstruttoriaCumuloById( lIdIstruttoriaCumulo );
 
    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lIstruttoriaCumulo==null){ 
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
      // Specificare eventualmente la jump page dove verrà ridirezionata la 
      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
      //      della root_dir es /agost-tomcat/frame.htm  
    setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
      return IWebConstants.PG_MESSAGE;
    }

    //==================================================== 
    // Passa il Model alla componente di visualizzazione  
    //==================================================== 
    setRequestAttribute("istruttoriacumulo", lIstruttoriaCumulo);

    // Inserire Eventuali ComboBOX precaricando i dati del model
    // Option lOption = new Option( DecodificheManager.getInstance().get???());
    // setRequestAttribute("???", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");

    //==========================================================================
    // Restituisce la pagina di modifica.
    // n.b. è la stessa della pagina di Inserimento ma con modalità differente 
    //==========================================================================
    return PG_LOAD_INSERISCI_ISTRUTTORIA_CUMULO; 
  }
}