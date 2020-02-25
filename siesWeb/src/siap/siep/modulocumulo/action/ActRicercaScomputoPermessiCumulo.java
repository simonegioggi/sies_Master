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

public class ActRicercaScomputoPermessiCumulo extends ActionModuloCumulo implements ICostantiLibAnticipataCumulo, ICostantiStatoEsecTitoloCumulato  
{

  /*****************************************************************************
   * Azione per la ricerca dei provvedimenti di
   * Scomputo Permessi
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
    
    // Scomputo Permessi
    listaProvv.add("2250");		// 	Esclusione Computo Permesso 
    listaProvv.add("0039");		// 	Reclamo Avverso scomputo periodo permesso
    
    Vector<String> listaTipoProvv = new Vector<String>();
    listaTipoProvv.add("02");		// 	Decreto
    listaTipoProvv.add("03");		// 	Ordinanza   
    
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(lTitolo.getIdTitoloCumulato(), "01", listaTipoProvv, listaProvv);
    siesLogger.debug("Lista Scomputi Permessi - lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaScomputi", lVect);

    // Restituisce la pagina di Inserimento dei Dati 
    return PG_LOAD_ELENCO_SCOMPUTO_PERMESSI; 
  }
}
