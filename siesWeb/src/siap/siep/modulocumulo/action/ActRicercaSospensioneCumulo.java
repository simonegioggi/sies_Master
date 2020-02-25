package siap.siep.modulocumulo.action;


/**
* <p>Title: ActRicercaSospensioneCumulo</p>
* <p>Description: Classe Action per la ricerca di Annotazioni Decisioni G.E. Sospensione per un 
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

public class ActRicercaSospensioneCumulo extends ActionModuloCumulo implements ICostantiComputiCumulo
{
 /*****************************************************************************
  * Azione di Ricerca Delle annotazioni su Provv. G.E. Sospensione. 
  * 
  * Recupera i provvedimenti di Sospensione associati a un certo titolo
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
    //Ricerca dei provvedimenti G.E. di Sospensione per titolo/istruttoria
    //=====================================================================================
    
    Vector <StatoEsecTitoloCumulatoModel> lVect = new Vector <StatoEsecTitoloCumulatoModel>();
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

    Vector<String> listaTipoProvv = new Vector<String>();
    
    listaTipoProvv.add("02");	// 	decreto
    listaTipoProvv.add("03");	// 	ordinanza

    Vector<String> listaProvv = new Vector<String>();
    
    listaProvv.add("0800");
    listaProvv.add("0801");
    listaProvv.add("0802");
    listaProvv.add("0803");
    listaProvv.add("0804");
    listaProvv.add("0805");
    listaProvv.add("0806");
    listaProvv.add("0807");
    listaProvv.add("0808");
    listaProvv.add("0809");
    //listaProvv.add("0840");
    
    // La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
    // idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo. 
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(lTitolo.getIdTitoloCumulato(),"01",listaTipoProvv,listaProvv);
    siesLogger.debug("lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaSospensioni", lVect);
    
    return PG_ELENCO_SOSPENSIONE_CUMULO;
  }
}