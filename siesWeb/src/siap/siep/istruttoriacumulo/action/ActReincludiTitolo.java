package siap.siep.istruttoriacumulo.action;

/**
* <p>Title: ActReincludiTitolo</p>
* <p>Description: Classe Action per il Reinserimento in istruttoria cumulo </p>  
* <p> di un Titolo che era stato escluso </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.modulocumulo.action.ICostantiTitoloCumulato;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActReincludiTitolo extends ActionModuloCumulo implements ICostantiIstruttoriaCumulo 
{
  public String processRequest() throws F3BException {

    BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter (CAMPO_ID_ISTRUTTORIA_CUMULO);
    BigDecimal lIdTitolo = getRequestBigDecimalParameter (ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

    //========================================================================== 
    // Cerco il Titolo da reincludere e lo modifico con N su FLAG_ESCLUSO
    //========================================================================== 
    ITitoloCumulato lCtrlTitoloCum = SIEPLookupRemote.getTitoloCumulatoRemote();
    
    TitoloCumulatoModel lTitolo = lCtrlTitoloCum.ExRicercaTitoloCumulatoById (lIdTitolo);
    lTitolo.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    lTitolo.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
    lTitolo.setDataAggiornamento         (DateUtils.getSysDate());
    lTitolo.setFlagEscluso("N");
    
    lCtrlTitoloCum.ExIncludiEscludiTitoloDaIstruttoria(lTitolo);
    
    //========================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina con l'elenco dei fascicoli presi in carico
    // ancora da iscrivere
    //==========================================================================
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti";
    lPage += "&" + CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + lIdIstruttoriaCumulo.toString();
    return lPage;
  }
}