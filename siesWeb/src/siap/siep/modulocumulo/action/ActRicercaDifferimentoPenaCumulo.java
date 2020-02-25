package siap.siep.modulocumulo.action;

/**
* <p>Title: ActRicercaDifferimentoPenaCumulo</p>
* <p>Description: Classe Action per la ricerca di Attività della Sorveglianza - Differimento pena per un 
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

public class ActRicercaDifferimentoPenaCumulo extends ActionModuloCumulo implements ICostantiComputiCumulo
{
 /*****************************************************************************************
  * Azione di Ricerca Delle Attività della Sorveglianza - Differimento pena. 
  * 
  * Recupera i provvedimenti di Differimento pena associati a un certo titolo.
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
    //Ricerca dei provvedimenti SORV. di Differimento pena per titolo/istruttoria
    //========================================================================================
    
    Vector <StatoEsecTitoloCumulatoModel> lVect = new Vector <StatoEsecTitoloCumulatoModel>();
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

    Vector<String> listaTipoProvv = new Vector<String>();
    
    listaTipoProvv.add("02");	// 	decreto
    listaTipoProvv.add("03");	// 	ordinanza

    Vector<String> listaProvv = new Vector<String>();
    
    listaProvv.add("2010");		// Differimento Pena facoltativo art.147 C.P.
    listaProvv.add("2011"); 	// Differimento Pena obbligatorio art.146 C.P. 
    listaProvv.add("0030"); 	// Differimento Pena facoltativo attesa grazia.
    listaProvv.add("0031"); 	// Differimento Pena facoltativo grave infermità.
    listaProvv.add("0032"); 	// Differimento Pena facoltativo maternità.
    listaProvv.add("0033"); 	// Differimento Pena obbligatorio nei confronti di donna incinta.
    listaProvv.add("0201"); 	// Differimento Pena obbligatorio nei confronti di madre infante di età inferiore ad Anni Uno.
    listaProvv.add("0202"); 	// Differimento Pena obbligatorio nei confronti di persona affetta da malattia.
    listaProvv.add("0203"); 	// Revoca Differimento Pena Facoltativa Attesa Grazia.
    listaProvv.add("0204"); 	// Revoca Differimento Pena Facoltativo Grave Infermita'.
    listaProvv.add("0205"); 	// Revoca Differimento Pena Facoltativo Maternita'.
    listaProvv.add("0206"); 	// Revoca Differimento Pena Obbligatoria nei Confronti di Donna Incinta.
    listaProvv.add("0207"); 	// Revoca Differimento Pena Obbligatorio nei Confronti di Madre Infante di Eta' Inferiore Ad Anni Uno.
    listaProvv.add("0208"); 	// Revoca Differimento Pena Obbligatorio nei Confronti di Persona Affetta da Malattia

    // La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
    // idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo. 
    lVect = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(lTitolo.getIdTitoloCumulato(),"01",listaTipoProvv,listaProvv);
    siesLogger.debug("lVect.size() = "+lVect.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaDifferimentoPena", lVect);
    
    return PG_ELENCO_DIFFERIMENTOPENA_CUMULO;
  }
}