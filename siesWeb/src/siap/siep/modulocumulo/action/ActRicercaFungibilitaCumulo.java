package siap.siep.modulocumulo.action;

import java.util.Vector;

import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

import org.apache.log4j.Logger;

public class ActRicercaFungibilitaCumulo extends ActionModuloCumulo implements ICostantiPresoffertoCumulo 
{

  /*****************************************************************************
   * Azione per la ricerca delle annotazioni dei provvedimenti di fungibilità
   * associati ad un certo titolo/istruttoria
   * 
   * @return Nome della pagina JSP da visualizzare
   * @throws F3BException
   *****************************************************************************/
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException 
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    //==========================================================================
    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    TitoloCumulatoModel lTitolo = super.getDatiTitoloCumulato();

    //==========================================================================
    // Ricerca dei procedimenti per titolo/istruttoria
    //==========================================================================
    Vector <StatoEsecTitoloCumulatoModel> lVect = new Vector <StatoEsecTitoloCumulatoModel>();
    
    Vector<String> listaProvv = new Vector<String>();
    
    listaProvv.add("0212");		// 	computo Misura Cautelare Altro Reato art. 657 c.p.p
    listaProvv.add("0213");		// 	computo Pena Detentiva Espiata per Altro Reato art. 657 c.p.p
    
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListaProvv(lTitolo.getIdTitoloCumulato(),"01","04",listaProvv);
    siesLogger.debug("lista computi - lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaFungibilita", lVect);

    // Restituisce la pagina di Inserimento dei Dati 
    return PG_ELENCO_FUNGIBILITA_CUMULO; 
  }
}
