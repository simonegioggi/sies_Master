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

public class ActRicercaLiberazioneAnticipataCumulo extends ActionModuloCumulo implements ICostantiLibAnticipataCumulo, ICostantiStatoEsecTitoloCumulato  
{
  /*****************************************************************************
   * Azione per la ricerca dei provvedimenti di Liberazione Anticipata
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
    
    listaProvv.add("2130");		// 	Concessione Liberazione Anticipata	UDS / MDS
    listaProvv.add("2131");		// 	Concessione Liberazione Anticipata	UDS / MDS (solo L.A. Speciale)
    listaProvv.add("2132");		// 	Concessione Liberazione Anticipata	UDS / MDS (solo Integrazione L.A.)	
    
    listaProvv.add("0076");		// 	Concessione Liberazione Anticipata	TDS

    listaProvv.add("2135");  	//  Revoca Liberazione Anticipata		UDS / MDS
    listaProvv.add("2136");  	//  Revoca Liberazione Anticipata		UDS / MDS (solo L.A. Speciale)
    listaProvv.add("2137");  	//  Revoca Liberazione Anticipata		UDS / MDS (solo Integrazione L.A.)
    
    listaProvv.add("0028");  	//  Revoca Liberazione Anticipata		TDS
    listaProvv.add("0620");  	//  Revoca Liberazione Anticipata		TDS  (solo L.A. Speciale)
    listaProvv.add("0621");  	//  Revoca Liberazione Anticipata		TDS  (solo Integrazione L.A.)
    
    listaProvv.add("0113");  	//  Reclamo Liberazione Anticipata		(Ridimensionamento: Riduce per il periodo)
    							//										(Concessione      : Concede a seguito Reclamo )
    
    Vector<String> listaTipoProvv = new Vector<String>();
    listaTipoProvv.add("02");		// 	Decreto
    listaTipoProvv.add("03");		// 	Ordinanza   
    
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(lTitolo.getIdTitoloCumulato(), "01", listaTipoProvv, listaProvv);
    siesLogger.debug("ListaLiberazioni - lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaLiberazioni", lVect);

    // Restituisce la pagina di Inserimento dei Dati 
    return PG_LOAD_ELENCO_LIB_ANTICIPATA_CUMULO; 
  }
}
