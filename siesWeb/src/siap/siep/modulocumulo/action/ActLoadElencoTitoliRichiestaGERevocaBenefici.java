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
* di Revoca Beneficio  (Non Menzione/sospensione/Indulto/Amnistia etc...)
* 
* @author Intersistemi Italia S.p.A.
* 
*/

public class ActLoadElencoTitoliRichiestaGERevocaBenefici extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo
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
    
    String lModalita = "I"; //default inserimento
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    siesLogger.debug("--XX-- Inizio ActLoadElencoTitoliRichiestaGERevocaBenefici - Modalita = "+lModalita);
    
    String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();

    IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    
    String lCodNatBen = "C";	// Concesso
    
    Vector<String> lCodTipiBen = new Vector<String>();
    lCodTipiBen.add("01");		// Sospensione Condizionale
    lCodTipiBen.add("02");		// Non Menzione
    lCodTipiBen.add("03");		// Indulto
    lCodTipiBen.add("04");		// Amnistia
    	
    // La Query trova Titolo_Cumulato_Model in join con Beneficio_Cumulo e il Procedimento_Cumulato Aggregato
    // 24-07-2018 - Cerca anche AMNISTIA/INDULTO assegnati con Provvedimento (STATO_ESEC_TITOLO_CUMULATO)
    lListaTitoli = new Vector<TitoloCumulatoModel>(lIstrCtrl.ExRicercaTitoliBeneficiCumByIstruttoriaOrderBy(lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento, lCodNatBen, lCodTipiBen) );
    
	setRequestAttribute("ListaTitoli", lListaTitoli);
    setRequestAttribute("modalita", lModalita);
    
    return PG_ELENCO_TITOLI_RIC_GE_REV_BENEFICI;
  }
  
  
}	// Chiude Classe
