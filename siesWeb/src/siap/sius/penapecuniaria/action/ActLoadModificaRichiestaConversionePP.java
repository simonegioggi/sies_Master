package siap.sius.penapecuniaria.action;


/**
* <p>Title: ActLoadModificaRichiestaConversionePP</p>
* 
* <p>Description: Classe Action per la load Modifica di RichiestaConversione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/


import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadModificaRichiestaConversionePP extends ActionSius implements ICostantiSiusPenaPecuniaria
{ 
 /**
  * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare in tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  */
  public String processRequest()  throws F3BException 
  {
  	BigDecimal aIdFascicoloSius = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
  	Date lDataIrrevocabilita = null;

    // Si Preleva dalla sessione il model fascicoloSiusGP
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

    // Controllo congruenza dati di sessione.
    if (lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().compareTo(aIdFascicoloSius)==0 &&
    		lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null )
    {
    	// Lettura della Data Irrevocabilità di SIEP.
    	IFascicoloSiep lCtrlFasSiep = SIEPLookupRemote.getFascicoloSiepRemote();
    	FascicoloSiepModel lFasSiep = lCtrlFasSiep.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
    	if (lFasSiep!=null)
    		lDataIrrevocabilita = lFasSiep.getDataIrrevocabilita();
    }
  	setRequestAttribute("dataIrrevocabilita", lDataIrrevocabilita);

  	IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
  	RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
  	lRicMod.setIdRichiestaConversione ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_CONVERSIONE) );
  	lRicMod=lCtrl.ExRicercaRichiestaConversioneByIdSenzaEvento(lRicMod.getIdRichiestaConversione() );
  	setRequestAttribute("richiestaconversione", lRicMod);

  	// Autorità per la conversione
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutorita(), lRicMod.getCodTipoAutoritaEmittente());
    lOption.setFilter( new String[] {"-","36", "99", "57","37", "97", "98", "38"});

    setRequestAttribute("autoritaConv", "" + lOption ); 
      
    // Imposta la Modalità a Modifica.
    setRequestAttribute("modalita", "M");
      
  	return PG_LOAD_MODIFICA_RICHIESTA_CONVERSIONE_PP;	  
  }
}