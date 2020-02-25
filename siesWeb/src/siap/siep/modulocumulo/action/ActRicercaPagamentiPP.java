package siap.siep.modulocumulo.action;

/**
* <p>Title: ActRicercaPagamentiPP</p>
* <p>Description: Classe Action per la ricerca di Annotazioni Pagamenti Pena Pecuniaria per un 
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

public class ActRicercaPagamentiPP extends ActionModuloCumulo implements ICostantiComputiCumulo
{
 /*****************************************************************************
  * Azione di Ricerca Delle annotazioni Pag. Pene Pecuniarie per titolo. 
  * 
  * Recupera i provvedimenti di presofferto associati a un certo titole e relativi 
  * periodi
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException {
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
    
    //=====================================================================================
    // Implementare qui la ricerca dei provvedimenti di Pagamento PP per titolo/istruttoria
    //=====================================================================================
    
    Vector <StatoEsecTitoloCumulatoModel> lVect = new Vector <StatoEsecTitoloCumulatoModel>();
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    // La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
    // idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo. 
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloTipoProvv(lTitolo.getIdTitoloCumulato(),"01","25","1007");
    siesLogger.debug("lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaPagamentiPP", lVect);
    
    return PG_ELENCO_PAGAMENTI_PP_CUMULO;
  }
}