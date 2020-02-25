package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.web.IWebConstants;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
* Action per la load Elenco dei Titoli possibili oggetto della Richieste alla SORV 
* di Unificazione Misure di Sicurezza.
* 
* @author Intersistemi Italia S.p.A.
* 
*/

public class ActLoadElencoTitoliRichiestaSORVUnificaMS extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo
{
  public String processRequest() throws Exception
  {
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())){
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "L'istruttoria risulta chiusa. Non è possibile procedere all'emissione di ulteriori richieste");
      return IWebConstants.PG_MESSAGE;
    }    
    
    Vector <TitoloCumulatoModel> lListaTitoli = null;
    
    String lModalita = "I"; //default inserimento
    
    String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();
    IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    
    // La Query trova Titolo_Cumulato_Model in join con MisuraSicurezza_Cumulo e il Procedimento_Cumulato Aggregato
    lListaTitoli = new Vector<TitoloCumulatoModel>(lIstrCtrl.ExRicercaTitoliMisureSicCumByIstruttoriaOrderBy(lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento ) );
    
	setRequestAttribute("ListaTitoli", lListaTitoli);
    setRequestAttribute("modalita", lModalita);
    
    return PG_ELENCO_TITOLI_RIC_SORV_UNIF_MIS_SIC;
  }
  
  
}	// Chiude Classe
