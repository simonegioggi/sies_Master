package siap.siep.penapecuniaria.action;

/**
* <p>Title: ActLoadInserisciAnnullaPartita</p>
* <p>Description: Classe Action per la load inserisci 
* <p>"Annotazione Annullamento Partita di Credito"</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadInserisciAnnullaPartita extends ActionSiap 
                                                  implements ICostantiPenaPecuniaria
{
  
 /*****************************************************************************
  * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare in tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() 
    throws F3BException 
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    // 27/07/2015  Controllo che sia un fascicolo di classe I, II o III.  
	 int lFascProg = lFascMod.getChiaveProgr().intValue();
	 if  (lFascProg >= 40000)
	 {	  
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
	        "Impossibile procedere all'inserimento: funzionalità consentita solo per procedimenti di classe I, II e III");		
	      return IWebConstants.PG_MESSAGE;
	 }
    
    if (isFascicoloSiepDiCompetenza());
    if(isFascicoloNonValidato())
	      return IWebConstants.PG_MESSAGE;
	
  //  int lFascProg = lFascMod.getChiaveProgr().intValue();
	this.isEventoNonValidato();
 	    //Controllo Esistenza pena residua non validata per quel fascicolo
	PenaResiduaModel lPenaResMod = new PenaResiduaModel();
	IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
	lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
	notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod);

  
	 // autorità per la conversione
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutorita() , "-");
	lOption.setFilter( new String[] {"-","36", "99", "57","37",  "98", "38"});
    setRequestAttribute("autoritaConv", "" + lOption ); 
    
	return PG_LOAD_INSERISCI_ANNULLA_PARTITA;
	
    /* bho
    IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = lCtrlPos.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

     ricerca richieste conversione
    IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
	RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
	Vector lRichiestaConversioni = new Vector();
	lRicMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
	lRichiestaConversioni=lCtrlRic.ExRicercaRichiestaConversione(lRicMod);

	if (lRichiestaConversioni.size() > 0)
	{ 
		lRicMod= (RichiestaConversioneModel)lRichiestaConversioni.get(0);
	//	lRicMod = lCtrlRic.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());
  
		return PG_LOAD_INSERISCI_ANNULLA_PARTITA;	 
	}
	else
	{  
		
	//  residenza  
	    IResidenza lResCtrl = SICOLookupRemote.getResidenzaRemote();
	    Vector lVecRes = lResCtrl.ExRicercaResidenzeByIdFascicolo(lFascMod.getIdFascicoloSiep());
	    ResidenzaAssociataModel lResAssMod = new ResidenzaAssociataModel();
	    
	    if(lVecRes != null && !lVecRes.isEmpty())
	    {
	    	lResAssMod = (ResidenzaAssociataModel)lVecRes.get(0);
	    }
	    setRequestAttribute("residenzaassociata", lResAssMod);
	    
	//  domicilio  
	    IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
	    ResidenzaAssociataModel lDomAssMod = lCtrlFas.ExRicercaDomicilioFascicoloSiepCorrente(lFascMod.getIdFascicoloSiep());
	    setRequestAttribute("domicilioassociato", lDomAssMod);

	    // Imposta la Modalità a Inserimento.
	    setRequestAttribute("modalita", "I");
	
	    // Restituisce la pagina di Inserimento dei Dati 
	    return PG_LOAD_INSERISCI_ANNULLA_PARTITA; 
	}  // chiude lRichiestaConversioni.size() > 0
	
  } 
  */
  }
}