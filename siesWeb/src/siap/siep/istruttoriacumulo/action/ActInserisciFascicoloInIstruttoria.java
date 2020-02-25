package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

import siap.sico.utente.model.DatiOperazioneModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;

/**
* <p>Title: ActInserisciFascicoloInIstruttoria</p>
* <p>Description: Classe Action per l'inserimento in istruttoria cumulo di fascicolo preso in carico
* Azione di Inserimento dei dati dei titoli da cumulare. Inserisce i dati del
* CUMULO che contiene i riferimenti al titolo esecutivo da cumulare: Sentenza,
* Decreto, 'Cumulo' (Cumulo di Cumulo).
* </p>
* @version 1.0
*/
public class ActInserisciFascicoloInIstruttoria extends ActionModuloCumulo implements ICostantiIstruttoriaCumulo {

  
  public String processRequest() throws F3BException 
  {
    // Per ora mi aspetto che il procedimento SIA GIA' A SISTEMA DOPO LA PRESA
    // in carico. Questa o altra BDI.
    
    //==========================================================================
    // Recupero l'istruttoria da passare alla form
    //==========================================================================
    super.getDatiIstruttoria();
    BigDecimal lIdIstruttoriaCorrente = this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
    
    
    //=============================================
    // Recupero il fascicolo da Cumulare
    //=============================================
    BigDecimal lIdFasDaCumulare = getRequestBigDecimalParameter("idFasDaCumulare");
    
    //====================================================================
    // Recupero il messaggio di Invio atti presi in carico da Cumulare
    //====================================================================
    BigDecimal lIdMessaggioInvioAtti = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
  
    IFascicoloSiep lFascCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
    FascicoloSiepModel lFascSiep = lFascCtrl.ExRicercaFascicoloByKey(lIdFasDaCumulare);
    
    // Prima di procedere verifico che il fascicolo non si trovi già in istruttoria
    IIstruttoriaCumulo lIstruttoriaCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    Vector <TitoloCumulatoModel> lListaTitoli = lIstruttoriaCtrl.ExRicercaTitoliByIstruttoria (lIdIstruttoriaCorrente) ;
    for (int i=0; i<lListaTitoli.size();i++){
      TitoloCumulatoModel lTitoli = lListaTitoli.elementAt(i);
      ProcedimentoCumulatoModel lProcCum = lTitoli.getProcedimentoCumulato();
      
      if (lProcCum!=null) {
        if (   lProcCum.getChiaveAnnoFasCumulato().compareTo(lFascSiep.getChiaveAnno())==0     
            && lProcCum.getChiaveProgrFasCumulato().compareTo(lFascSiep.getChiaveProgr())==0     
            && lProcCum.getCodUfficioFasCumulato().equals(lFascSiep.getChiaveUfficio()) //Potrebbe non esistere
            )
        {
          String lMsgErr = "Il procedimento "+lFascSiep.getChiaveAnno()+"/"+lFascSiep.getChiaveProgr()+" di "+lFascSiep.getDescrTipoUfficio()+" di "+lFascSiep.getDescrComuneUfficio()
                          +" risulta già iscritto in istruttoria.";
          throw new F3BException(F3BException.USER_MESSAGE,lMsgErr);
        }
      }
    }
    
    DatiOperazioneModel lDatiOpModel = new DatiOperazioneModel();
    lDatiOpModel.setCodOperatore (getCodUtenteConnesso());
    lDatiOpModel.setCodUfficio   (getCodUfficioUtenteConnesso());
    lDatiOpModel.setData         (DateUtils.getSysDate());
    
    IModuloCumulo lCtrlModCumulo = SIEPLookupRemote.getModuloCumuloRemote();
    lCtrlModCumulo.ExInserisciTitoloInIstruttoria (lIdIstruttoriaCorrente,lIdFasDaCumulare,lDatiOpModel,null,lIdMessaggioInvioAtti,"01" );   // 01 = Iscrizione da presa in carico
    
    
    //=============================================
    //
    //=============================================
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage(IWebConstants.PG_MAIN);
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Fascicolo Cumulato Avvenuto Correttamente!");
    lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti");
    lRedirigi.setParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO, ""+lIdIstruttoriaCorrente);
    //lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
    setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

    return IWebConstants.PG_MESSAGE;
  }
  
  

  
}  // CHIUDE CLASSE
