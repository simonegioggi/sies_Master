package siap.siep.istruttoria.action;

import java.util.Vector;
import f3b.log.LogF3B;

import siap.siep.modulocumulo.action.*;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

public class ActCaricaElencoTitoliPerIstruttorieRichieste extends ActionModuloCumulo implements ICostantiIstruttoria
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws Exception
  {
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    
    String lParentFormName = getRequestStringParameter(CAMPO_PARENT_FORM_NAME);
    String lParentFormType = getRequestStringParameter(CAMPO_PARENT_FORM_TYPE);
    
    setRequestAttribute(CAMPO_PARENT_FORM_NAME, lParentFormName);
    setRequestAttribute(CAMPO_PARENT_FORM_TYPE, lParentFormType);
    
    if(!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO))
    {
    	setRequestAttribute("IdTitoloPrincipale", getRequestStringParameter(ICostantiRichiestePmInCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO));
    }
    
    //==========================================================================
    // Effettua la ricerca dei Titoli in funzione della tipologia di Form chiamante
    //==========================================================================
    siesLogger.debug("lParentFormType = "+lParentFormType);
    
    String lPage="";
    
    if (FORM_TYPE_SENTENZA_INTEGRALE.equals(lParentFormType) || 
   		FORM_TYPE_CERTIFICATO_ESECUZIONE.equals(lParentFormType) || 
   		FORM_TYPE_PAGAMENTO_PENA_PEC.equals(lParentFormType) ) 
    {
      getDatiRichiestaBenefici(lIstruttoriaModel);
      lPage = PG_LOAD_POPUP_LISTA_TITOLI_ESTRATTO_SENTENZA;
    }
    else if (ICostantiRichiestePmInCumulo.FORM_TYPE_RIC_REV_BENEFICI.equals(lParentFormType)  ) 
    {
         getDatiRichiestaBenefici(lIstruttoriaModel);
         lPage = ICostantiRichiestePmInCumulo.PG_POPUP_ELENCO_TITOLI_REVOCANTI;
    }
    //else if (FORM_TYPE_RIC_SOST_PA.equals(lParentFormType)) {
      // richiama una jsp di test per il doc analisi
      //getDatiRichiestaBenefici(lIstruttoriaModel);
    //  lPage = PG_POPUP_TEST;
  //  }    
    else {
      //Aggiungere gli altri casi
    }
    
    
    return lPage;
  }
  
  /**
   * Recupera la lista dei Titoli su cui applicare i benefici di Amnistia/Indulto
   * 
   */
  private void getDatiRichiestaBenefici (IstruttoriaCumuloModel aIstruttoriaModel) throws Exception 
  {
 
    String lOrdinamento = aIstruttoriaModel.getOrdinamentoTitoli();
    
    IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    Vector <TitoloCumulatoModel> lListaTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy (aIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento);
    setRequestAttribute("ListaTitoli", lListaTitoli);

    
  }
  
  
}
