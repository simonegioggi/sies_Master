package siap.siep.istruttoriacumulo.action;


/**
* <p>Title: ActInserisciIstruttoriaCumulo</p>
* <p>Description: Classe Action per l'inserimento di IstruttoriaCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciIstruttoriaCumulo extends ActionSiap implements ICostantiIstruttoriaCumulo {

 /*****************************************************************************
  * Azione di Inserimento del IstruttoriaCumulo
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  ****************************************************************************/
  public String processRequest() throws F3BException {

    FascicoloSiepModel lFascMod = ((FascicoloSiepModel)(getSessionAttribute("fascicolo")));
    
    
    //==========================================================================
    // Carico i campi del model
    //==========================================================================    
    IstruttoriaCumuloModel lIstMod = new IstruttoriaCumuloModel();

    lIstMod.setFasSieIdFascicoloSiep (lFascMod.getIdFascicoloSiep());
    lIstMod.setDataApertura          (DateUtils.getSysDate()); // n.b. mi servono anche hh:mm:ss perchè nell'arco della gionata potrebbero venir aperte e chiuse più istruttorie
    lIstMod.setFlagStato             ( FLAG_STATO_APERTA );  // Aperta
    lIstMod.setAnnoProtocollo        ( new BigDecimal(DateUtils.getSysDate("yyyy")));
//    lIstMod.setNumProtocollo         (new BigDecimal(1));// assegnato dal controller
    lIstMod.setChiaveUfficio         (getCodUfficioUtenteConnesso());
    lIstMod.setCodOperatoreInserimento (getCodUtenteConnesso());
    lIstMod.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    lIstMod.setDataInserimento         (DateUtils.getSysDate());
    
    // Ordinamento di default per data irrevocabilità crescente
    lIstMod.setOrdinamentoTitoli (ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC);

    //========================================================================== 
    // Recupera il controller ed effettua l'inserimento con contestuale estrazione
    // dei dati del cumulante
    //========================================================================== 
    IIstruttoriaCumulo lCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    IstruttoriaCumuloModel lIstRetMod = new IstruttoriaCumuloModel();
    /* MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata 
     * Modifica firma metodo */
    // lIstRetMod=lCtrl.ExInserisciIstruttoriaCumulo(lIstMod,lFascMod);
    lIstRetMod=lCtrl.ExInserisciIstruttoriaCumulo(lIstMod,lFascMod, null);
    
    
    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //======================================================================
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.istruttoriacumulo.action.ActLoadDettaglioIstruttoriaCumulo";
    lPage += "&" + CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + lIstRetMod.getIdIstruttoriaCumulo().toString();

    return lPage;
  }
}