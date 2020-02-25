package siap.siep.modulocumulo.action;

/**
* <p>Title: ActRicercaRevocheMisureAlternativeCumulo</p>
* <p>Description: Classe Action per la ricerca di Attività della Sorveglianza - Revoca Misure Alternative per un 
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

public class ActRicercaRevocheMisureAlternativeCumulo extends ActionModuloCumulo implements ICostantiComputiCumulo
{
 /*****************************************************************************************
  * Azione di Ricerca Delle Attività della Sorveglianza - Revoca Misure Alternative. 
  * 
  * Recupera i provvedimenti di Revoca Misure Alternative associati a un certo titolo.
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
    //Ricerca dei provvedimenti SORV. di Revoca Misure Alternative per titolo/istruttoria
    //========================================================================================
    
    Vector <StatoEsecTitoloCumulatoModel> lVect = new Vector <StatoEsecTitoloCumulatoModel>();
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

    Vector<String> listaTipoProvv = new Vector<String>();
    
    listaTipoProvv.add("02");	// 	decreto
    listaTipoProvv.add("03");	// 	ordinanza

    Vector<String> listaProvv = new Vector<String>();
    
    listaProvv.add("0086");		// Revoca Affidamento in prova art. 47 quater o.p.      DATA REVOCA  - QUANTUM RECLUSIONE (AA-MM-GG) E QUANTUM ARRESTO (AA-MM-GG) RESISUO DA ESPIARE
    listaProvv.add("0014"); 	// Revoca Affidamento in Prova all' UEPE		“			“ 
    listaProvv.add("0015"); 	// Revoca Affidamento in casi particolari		“			“ 
    listaProvv.add("0089"); 	// Revoca Detenzione Domiciliare art. 47 quater o.p.    DATA REVOCA NON è PRESENTE NELLA FORM, MA VIENE IMPOSTATA SU DATA EMISSIONE PROVV DEL PM 
    listaProvv.add("0087"); 	// Revoca Detenzione Domiciliare Art. 47 Ter 1 Bis O.P. 	“			“ 
    listaProvv.add("0016"); 	// Revoca Detenzione Domiciliare				“			“ 
    listaProvv.add("0088"); 	// Revoca Differimento Nelle Forme della Detenzione Domiciliare Art.47 Ter 1 Ter O.P. “			“
    listaProvv.add("2270"); 	// Revoca ammissione provvisoria alla detenzione domiciliare	“			“
    listaProvv.add("0091"); 	// Revoca Semiliberta'	DATA REVOCA NON è PRESENTE NELLA FORM, MA VIENE IMPOSTATA SU DATA EMISSIONE PROVV DEL PM
    listaProvv.add("0196"); 	// Revoca Sospensione condizionata della pena Art. 2 L. 207/2003	DATA REVOCA  - QUANTUM RECLUSIONE (AA-MM-GG) E QUANTUM ARRESTO (AA-MM-GG) RESISUO DA ESPIARE
    listaProvv.add("0316"); 	// Revoca esecuzione presso domicilio della pena detentiva SE TDS	DATA REVOCA  - QUANTUM RECLUSIONE (AA-MM-GG) E QUANTUM ARRESTO (AA-MM-GG) RESISUO DA ESPIARE
    listaProvv.add("2640"); 	// Revoca esecuzione presso domicilio della pena detentiva SE UDS	DATA REVOCA  - QUANTUM RECLUSIONE (AA-MM-GG) E QUANTUM ARRESTO (AA-MM-GG) RESISUO DA ESPIARE
    listaProvv.add("0232"); 	// Revoca Arresti Domiciliari
    listaProvv.add("2744"); 	// Revoca Arresti Domiciliari ex art. 656 comma 10
    listaProvv.add("2747"); 	// Revoca Collocamento in Comunità
    listaProvv.add("2757"); 	// Revoca Domiciliari ex art. 89 dpr 309/90
    listaProvv.add("2746"); 	// Revoca Permanenza in Casa

    // La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
    // idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo. 
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(lTitolo.getIdTitoloCumulato(),"01",listaTipoProvv,listaProvv);
    siesLogger.debug("lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaRevocheMisureAlt", lVect);
    
    return PG_ELENCO_REVOCHE_MISUREALTERNATIVE_CUMULO;
  }
}