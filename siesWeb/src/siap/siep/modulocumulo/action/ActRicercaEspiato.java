package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.log.LogF3B;
import f3b.util.F3BException;

import siap.siep.modulocumulo.controller.IPosizioneGiuridicaCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

public class ActRicercaEspiato extends ActionModuloCumulo implements ICostantiModuloCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException 
  {
    //==========================================================================
    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    TitoloCumulatoModel lTitoloCumulato = super.getDatiTitoloCumulato();

    //==========================================================================
    // Effettuo la ricerca delle Posizioni Giuridiche collegate al Titolo
    //==========================================================================
    IPosizioneGiuridicaCumulo lCtrlPosGiuridicaCumulo = SIEPLookupRemote.getPosizioneGiuridicaCumuloRemote();
    Vector <PosizioneGiuridicaCumuloModel> lListaEspiati = lCtrlPosGiuridicaCumulo.ExRicercaPosizioneGiuridicaCumulobyIdTitCum (lTitoloCumulato.getIdTitoloCumulato());
    
    setRequestAttribute("ListaEspiati", lListaEspiati);

    //==========================================================================
    // Effettuo la ricerca delle Espiazioni Pregresse collegate al Titolo
    //==========================================================================
    Vector <StatoEsecTitoloCumulatoModel> lListaEspPregresse = new Vector <StatoEsecTitoloCumulatoModel>();
    IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

    Vector<String> listaTipoProvv = new Vector<String>();
    
    listaTipoProvv.add("04");	// 	provvedimento
    listaTipoProvv.add("09"); //  Ordine scarcerazione x estratti 
    listaTipoProvv.add("12"); //  Comunicazione x i 0267 estratti
    listaTipoProvv.add("25"); //  Annotazione x 0270 estratti 

    Vector<String> listaProvv = new Vector<String>();

    listaProvv.add("0900");		// Sospensione esecuzione della pena detentiva ex art. 91 DPR 309/90
    listaProvv.add("0901"); 	// Sospensione esecuzione della pena detentiva ex art. 94 DPR 309/90 
    listaProvv.add("0902"); 	// Sospensione esecuzione della pena detentiva per dubbio sull'identità fisica della persona detenuta ex art. 667 c.p.p. 
    listaProvv.add("0903"); 	// Sospensione esecuzione della pena detentiva 
    listaProvv.add("0937"); 	// Sospensione Esecuzione Contro Persona Condannata per Errore di Nome (Art. 668 C.P.P.) 
    listaProvv.add("0267"); 	// avvenuta evasione 
    listaProvv.add("0270"); 	// interruzione della esecuzione della pena
    listaProvv.add("0675");   // Espiazione Pregressa - Codice fittizio Cumulo

    // La ricerca dei provvedimenti per idTitolo e TipoProvvedimento richiede, nell'ordine, i parametri:
    // idTitolo, CodTipoEvento, CodTipoProvvedimento, CodMotivo. 
    lListaEspPregresse = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(lTitoloCumulato.getIdTitoloCumulato(),"01",listaTipoProvv,listaProvv);
    siesLogger.debug("lListaEspPregresse.size() = "+lListaEspPregresse.size());
    
    // Passo alla form i dati trovati
    setRequestAttribute("ListaEspPregressa", lListaEspPregresse);
     
    return PG_LOAD_ELENCO_ESPIAZIONE;
    
  }
}
