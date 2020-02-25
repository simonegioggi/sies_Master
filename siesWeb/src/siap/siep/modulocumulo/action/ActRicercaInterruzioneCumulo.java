package siap.siep.modulocumulo.action;

/**
* <p>Title: ActRicercaInterruzioneCumulo</p>
* <p>Description: Classe Action per la ricerca di Annotazioni Decisioni G.E. Interruzione della pena per un 
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

public class ActRicercaInterruzioneCumulo extends ActionModuloCumulo implements ICostantiComputiCumulo
{
 /*****************************************************************************
  * Azione di Ricerca Delle annotazioni su Provv. G.E. Interruzione. 
  * 
  * Recupera i provvedimenti di Interruzione associati a un certo titolo
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
    
    //=====================================================================================
    //Ricerca dei provvedimenti G.E. di Interruzione pena per titolo/istruttoria
    //=====================================================================================
    
    Vector<String> listaTipoProvv = new Vector<String>();
    
    listaTipoProvv.add("12");	// 	comunicazione
    listaTipoProvv.add("25");	// 	annotazione

    Vector<String> listaProvv = new Vector<String>();
    
    listaProvv.add("0266");		// 	avvenuto decesso
    listaProvv.add("0267");		// 	avvenuta evasione
    listaProvv.add("0268");		// 	consegna temporanea art. 709 comma 1c.p.p.
    listaProvv.add("0269");		// 	esecuzione penale all'estero della condanna ex art. 742 c.p.p.
    listaProvv.add("0270");		// 	interruzione della esecuzione della pena
    listaProvv.add("0366");		// 	Scarcerazione provvisoria ex art. 672 co. 3 c.p.p.

    Vector <StatoEsecTitoloCumulatoModel> lVect = new Vector <StatoEsecTitoloCumulatoModel>();
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    
    // La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
    // idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo. 
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(lTitolo.getIdTitoloCumulato(),"01", listaTipoProvv, listaProvv);
    siesLogger.debug("lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaInterruzioni", lVect);
    
    return PG_ELENCO_INTERRUZIONE_CUMULO;
  }
}