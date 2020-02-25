package siap.sius.penapecuniaria.action;

import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioRichiestaConversionePP</p>
 * <p>Description: Classe Action per la load inserisci Richiesta Conversione Pena Pecuniaria</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @version 1.0
 */

public class ActLoadDettaglioRichiestaConversionePP extends ActionSius implements ICostantiSiusPenaPecuniaria
{
  public String processRequest() throws F3BException
  { 
		IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		lRicMod.setIdRichiestaConversione      ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_CONVERSIONE) );
		lRicMod=lCtrl.ExRicercaRichiestaConversioneByIdSenzaEvento(lRicMod.getIdRichiestaConversione() );
		setRequestAttribute("richiestaconversione", lRicMod);

		//Modificabilità del fascicolo.
    String lModificabile = "NO";
    if (this.IsFascicoloSiusModificabile()==true) lModificabile="SI"; else lModificabile="NO";
    this.setRequestAttribute("isModificabile", lModificabile);
		 
		return PG_LOAD_DETTAGLIO_RICHIESTA_CONVERSIONE_PP;	  
  }
}