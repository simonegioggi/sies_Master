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

public class ActRicercaRimediRisarcitoriDL201492Cumulo extends ActionModuloCumulo implements ICostantiLibAnticipataCumulo, ICostantiStatoEsecTitoloCumulato 
{

  /*****************************************************************************
   * Azione per la ricerca dei provvedimenti di
   * Rimedi Risarcitori DL N. 92 del 2014
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
    
    // Rimedi Risarcitori D.L. del 26 Giugno 2014 , N. 92
    listaProvv.add("2790");		// 	Riduzione pena da espiare/risarcimento del danno (art. 35 ter O.P.) 
    listaProvv.add("9027");		// 	Riduzione pena da espiare/risarcimento del danno (Reclamo art. 35 ter O.P.)
    
    Vector<String> listaTipoProvv = new Vector<String>();
    listaTipoProvv.add("02");		// 	Decreto
    listaTipoProvv.add("03");		// 	Ordinanza   
    
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(lTitolo.getIdTitoloCumulato(), "01", listaTipoProvv, listaProvv);
    siesLogger.debug("Lista Rimedi Risarcitori - lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaRisarcitori", lVect);

    // Restituisce la pagina di Inserimento dei Dati 
    return PG_LOAD_ELENCO_RIMEDI_RISARCITORI_CUMULO; 
  }
}
