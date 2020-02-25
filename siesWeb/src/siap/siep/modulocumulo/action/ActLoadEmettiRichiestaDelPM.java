package siap.siep.modulocumulo.action;

/**
* <p>Title: ActLoadEmettiRichiestaDelPM</p>
* <p>Description: Classe Action per la load Inserimento delle Richieste Del PM</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;

import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

public class ActLoadEmettiRichiestaDelPM extends ActionModuloCumulo implements ICostantiModuloCumulo
{
 /*****************************************************************************
  * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare i tale pagina (es: combo) 
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
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    this.isFascicoloSiepDiCompetenza();
    
    String lModalita = "I"; //default inserimento
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    siesLogger.debug("--XX-- Inizio ActLoadEmettiRichiestaDelPM - Modalita = "+lModalita);
    setRequestAttribute("modalita", lModalita);
    
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    
    if (!ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())){
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "L'istruttoria risulta chiusa. Non è possibile procedere all'emissione di ulteriori richieste");
      return IWebConstants.PG_MESSAGE;
    }    

    IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
    Vector<RichiestePmInCumuloModel> VecRichiesteGE = null;
    
    // Questa Query produce in Output un Elenco di richieste ANCORA DA INVIARE ( Ric_Id_Richieste_Inviate_Cum = null) 
    // Il secondo parametro indica il tipo di richiesta PM (01: al GE / 02: alla SORV.)
    // Il terzo parametro indica le richieste cercare:  (tutte / dainviare / inviate)
    VecRichiesteGE = (Vector<RichiestePmInCumuloModel>)lCtrlRic.ExRicercaRichiestePmInCumuloByIdIstruttoriaTipoRichiesta(lIstruttoriaModel.getIdIstruttoriaCumulo(), "01", "dainviare");
    setRequestAttribute("ListaRichiesteGE", VecRichiesteGE);

    //ricerca magistrato firmatario
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
      setRequestAttribute("magistratocompetente", lMagMod);

    //  ufficio giudice dell'esecuzione
    //Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioGE());
    Option lOption = new Option (DecodificheManager.getInstance().getTipoUfficioPerCodice());
    lOption.setFilter(new String[]{"CAP", "CAS","CASAP","CSS","GIP","GIPM","GUPM","GUP","TRIBSD","CAPSM","DIB","DIBM","-" });

    setRequestAttribute("ufficioge", "" + lOption);

    return PG_LOAD_EMETTI_RICHIESTE_DEL_PM;
  }
}