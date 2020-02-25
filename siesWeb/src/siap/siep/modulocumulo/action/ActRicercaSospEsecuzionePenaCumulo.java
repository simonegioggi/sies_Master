package siap.siep.modulocumulo.action;

/**
* <p>Title: ActRicercaSospEsecuzionePenaCumulo</p>
* <p>Description: Classe Action per la ricerca di Attività della Sorveglianza - Sospensione Esecuzione pena per un 
*                 certo Titolo</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.log.LogF3B;
import f3b.util.F3BException;

import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

public class ActRicercaSospEsecuzionePenaCumulo extends ActionModuloCumulo implements ICostantiComputiCumulo
{
 /*****************************************************************************************
  * Azione di Ricerca Delle Attività della Sorveglianza - Sospensione Esecuzione pena. 
  * 
  * Recupera i provvedimenti di Sospensione Esecuzione pena associati a un certo titolo.
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************************/
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
    
    //========================================================================================
    //Ricerca dei provvedimenti SORV. di Sospensione Esecuzione pena per titolo/istruttoria
    //========================================================================================
    
    Vector <StatoEsecTitoloCumulatoModel> lVect = new Vector <StatoEsecTitoloCumulatoModel>();
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

    Vector<String> listaTipoProvv = new Vector<String>();
    
    listaTipoProvv.add("02");	// 	decreto
    listaTipoProvv.add("03");	// 	ordinanza

    Vector<String> listaProvv = new Vector<String>();
    
    listaProvv.add("2000");		// Sospensione Esecuzione Pena per Affidamento in prova al Servizio Sociale (art.47/4 O.P.).
    listaProvv.add("2001"); 	// Sospensione Esecuzione Pena per Semilibertà (art.47/4 O.P.). 
    listaProvv.add("2480"); 	// Provvisoria Esecuzione Pena ex art. 90 DPR 309/90.

    // La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
    // idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo. 
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(lTitolo.getIdTitoloCumulato(),"01",listaTipoProvv,listaProvv);
    siesLogger.debug("lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaSospEsecuzionePena", lVect);
    
    return PG_ELENCO_SOSP_ESECUZIONEPENA_CUMULO;
  }
}