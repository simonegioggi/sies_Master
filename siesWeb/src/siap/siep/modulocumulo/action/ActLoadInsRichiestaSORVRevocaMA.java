package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import f3b.util.F3BException;

import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la Load della form 'Inserimento, Modifica, Cancellazione di una richiesta alla SORV  
 * di Revoca Misura Alternativa (Gestione Cumulo)
 * 
 * @author Intersistemi Italia S.p.A.
 */

public class ActLoadInsRichiestaSORVRevocaMA extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo
{
  public String processRequest() throws F3BException {

    IstruttoriaCumuloModel lIstrCumulo = super.getDatiIstruttoria();
    RichiestePmInCumuloModel lRichiestaModel = null;

    String lModalita = "I"; //default inserimento
    String lPage = "";
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    if ("I".equals(lModalita)){
      // Inserimento  
    	this.getTitoliScelti (lIstrCumulo);
    	lPage = PG_INS_RICH_SORV_REV_MA;
    }
    else if ("M".equals(lModalita)){
    	// Modifica 
    	BigDecimal aIdRich = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
    	
    	//RichiestaAllaSorv
    	IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
    	lRichiestaModel = lCtrlRich.ExRicercaRichiestePmInCumuloById(aIdRich);
    	setRequestAttribute("RichiestaSORV", lRichiestaModel);
    	
    	// Ricerca del Titolo  e del provvedimento di M.A. collegati alla Richiesta (tramite tabella di Relazione RICHPM_TITOLO_CUM, RICHPM_STATO_ESEC_CUM) 
        TitoloCumulatoModel lTitoloMod = null;
        lTitoloMod = (TitoloCumulatoModel)lCtrlRich.ExRicercaTitolo_e_StatoEsecTitoloCumByRichiestaGE(aIdRich);
        setRequestAttribute("Titolo", lTitoloMod);
    	 
    	lPage = PG_INS_RICH_SORV_REV_MA;
    }
    else {
      //Rilanciare Eccezione - Operazione non supportata
    	 throw new SIEPException(SIEPException.USER_MESSAGE, "Modalità operazione sconosciuta. Impossibile eseguire la richiesta.");
    }
   
    setRequestAttribute("modalita", lModalita);
    return lPage;
  }
  
  private void getTitoliScelti (IstruttoriaCumuloModel aIstruttoriaModel) throws F3BException 
  {
	  //LogF3B.getLogger().debug("--XX-- Inserimento -  Start getTitoliScelti");

	    //  check Selezionati dall'Utente: ( è per forza solo 1 check )
	    String lIdTitoliSelezionato = null;
    	lIdTitoliSelezionato = getRequestStringParameter(CAMPO_ID_TITOLO_MIS_ALT_SELEZIONATO);
    	
    	String[] EleSplit = lIdTitoliSelezionato.split(";"); 
    	String lIdTitolo = EleSplit[0];
    	String lIdStatoEsec = EleSplit[1];
	    
    	ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
	    TitoloCumulatoModel lTitolo = (TitoloCumulatoModel)lCtrlT.ExRicercaTitoloCumulatoStatoEsecTitoloCum(new BigDecimal(lIdTitolo), new BigDecimal(lIdStatoEsec));
	    
	    setRequestAttribute("Titolo", lTitolo);
  } 

}
