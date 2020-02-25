package siap.siep.modulocumulo.action;

import java.util.Vector;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
* Action per la load Elenco dei Titoli possibili oggetto della Richieste al GE 
* di Revoca Pena Accessoria / Richiesta Applicazione Pena Accessoria
* 
* @author Intersistemi Italia S.p.A.
* 
*/

public class ActLoadElencoTitoliRichGERevocaPenaAccCum extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws Exception
  {
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    
    if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())){
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "L'istruttoria risulta chiusa. Non è possibile procedere all'emissione di ulteriori richieste");
      return IWebConstants.PG_MESSAGE;
    }
    
    Vector <TitoloCumulatoModel> lListaTitoli = null;
    
    //String lParentFormType = getRequestStringParameter(CAMPO_PARENT_FORM_TYPE);
    //setRequestAttribute(CAMPO_PARENT_FORM_TYPE, lParentFormType);
    
    String lModalita = "I"; //default inserimento
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    siesLogger.debug("--XX-- Inizio - Modalita = "+lModalita);
    
    String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();
    IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    
    // La ricerca trova Titolo_Cumulato in join con Pena_Accessoria_Cumulo e, in un secondo momento, il relativo Procedimento_Cumulato Aggregato
    // In questa fase il terzo parametro è settato a NULL 
    lListaTitoli = new Vector<TitoloCumulatoModel>(lIstrCtrl.ExRicercaTitoliPenaAccCumByIstruttoriaOrderBy(lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento, null) );
    
	setRequestAttribute("ListaTitoli", lListaTitoli);
    setRequestAttribute("modalita", lModalita);
    
    return PG_ELE_TITOLI_RICH_GE_REVOCA_PENA_ACC_CUM;
  }
  
  
}	// Chiude Classe
