package siap.siep.modulocumulo.action;

import java.util.Vector;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
* Action per la load Elenco dei Titoli possibili oggetto della Richieste al GE 
* di Revoca Sanzione Sostitutiva  (Libertà Controllata/semidetenzione/Pena Pec./Espulsione)
* 
* @author Intersistemi Italia S.p.A.
* 
*/

public class ActLoadElencoTitoliRichiestaGERevocaSSCum extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo
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
    
    
    
    String lModalita = "I"; //default inserimento
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    siesLogger.debug("--XX-- Inizio - Modalita = "+lModalita);
    
    String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();
    IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    
/*    Vector<String> lCodTipiSS = new Vector<String>();
    lCodTipiSS.add("S");		// Semidetenzione
    lCodTipiSS.add("L");		// Liberta' Controllata
    lCodTipiSS.add("P");		// Pena Pecuniaria
    lCodTipiSS.add("E");		// Espulsione
*/    
    // La Query trova Titolo_Cumulato_Model in join con Sanzione_Sost_Cumulo e il Procedimento_Cumulato Aggregato
    //lListaTitoli = new Vector<TitoloCumulatoModel>(lIstrCtrl.ExRicercaTitoliSanzioneSostCumByIstruttoriaOrderBy(lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento, lCodTipiSS, null) );
    Vector <TitoloCumulatoModel> lListaTitoli = null;
    lListaTitoli = new Vector<TitoloCumulatoModel>(lIstrCtrl.ExRicercaTitoliSanzioneSostCumByIstruttoriaOrderBy(lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento, null) );

    
    //"MAC" riunione 04/10/2018 richietso di consentire la revoca solo dell'espulsione
    for (int i=0; i<lListaTitoli.size(); i++) {
      TitoloCumulatoModel lTitolo = lListaTitoli.elementAt(i);
      SanzioneSostitutivaCumuloModel lSS = lTitolo.getSanzioneSostitutivaCumulo();
      if (!"E".equals(lSS.getCodTipoSanzione())) {
        lListaTitoli.remove(i);
        i--;
      }        
    }
    
	setRequestAttribute("ListaTitoli", lListaTitoli);
    setRequestAttribute("modalita", lModalita);
    
    return PG_ELENCO_TITOLI_RICG_GE_REVOCA_SS_CUM;
  }
  
  
}	// Chiude Classe
