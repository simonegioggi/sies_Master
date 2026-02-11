package siap.siep.modulocumulo.action;

import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;

public class ActDettaglioPosGiuridicaCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo
{
  public String processRequest() throws F3BException
  {    
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    
    
    DatiFinaliCumuloAggregatoModel lDatiFinaliAggModel = super.getDatiFinaliCumuloAggregato();

    // MEV_2025-48 - 2.12 Alert su continuazione e revoche benefici
    super.getListaTitContSganciate(null);
    super.getListaTitConRevBenSganciati(null);
    // MEV_2025-48 - 2.12 Alert su continuazione e revoche benefici
    
    if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo()!=null) {
//      if (   !isRequestParameterNullObj("FunzioneMenu")
//          && "PG".equals(getRequestStringParameter("FunzioneMenu"))
//         )
//      {
//        // Chiamata dal Dettaglio della PG, richiamola Action di Modifica
//        if (!lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA)){
//          // Se l'istruttoria non è in stato APERTA non consento l'inserimento, ma 
//          // visualizzo solo un messaggio
//          setRequestAttribute("FunzioneMenu", "PG");
//          return PG_LOAD_DETTAGLIO_POSIZIONE_GIURIDICA; 
//        }  
//        else {
//          // carico la funzione di inserimento con MODALITA = MODIFICA
//          String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadInserisciPosGiuridicaCumulo" 
//              + "&" +ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +getIdIstruttoria()
//              + "&" +ICostantiModuloCumulo.MODALITA+"="+ICostantiModuloCumulo.MODALITA_MODIFICA;
//          return lPage;
//          }
//      }
//      else {
//        setRequestAttribute("FunzioneMenu", "PG");
        return PG_LOAD_DETTAGLIO_POSIZIONE_GIURIDICA;
//      }
    }
    else {
      
      if (!lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA)){
        // Se l'istruttoria non è in stato APERTA non consento l'inserimento, ma 
        // visualizzo solo un messaggio
        throw new SIEPException (SIEPException.USER_MESSAGE, "Dati della Posizione Giuridica non presenti. L'istruttoria non risulta Aperta, non è possibile inserire ulteriori dati.");
      }  
      
      // carico la funzione di inserimento
      String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadInserisciPosGiuridicaCumulo&" 
          + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +getIdIstruttoria();
      return lPage;
    }
  }
}
