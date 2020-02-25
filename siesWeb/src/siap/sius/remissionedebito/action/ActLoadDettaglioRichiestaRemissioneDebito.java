package siap.sius.remissionedebito.action;

import siap.sius.ActionSius;
import siap.sius.remissionedebito.controller.IRichiestaRemissione;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioRichiestaRemissioneDebito</p>
 * <p>Description: Classe Action per la load inserisci Richiesta Remissione Debito</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @version 1.0
 */

public class ActLoadDettaglioRichiestaRemissioneDebito extends ActionSius implements ICostantiSiusRemissioneDebito
{
  public String processRequest() throws F3BException
  { 
		IRichiestaRemissione lCtrl = SIUSLookupRemote.getRichiestaRemissioneRemote();
		RichiestaRemissioneModel lRicMod = new RichiestaRemissioneModel();
		lRicMod.setIdRichiestaRemissione      ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_REMISSIONE) );
		lRicMod=lCtrl.ExRicercaRichiestaRemissioneById(lRicMod.getIdRichiestaRemissione() );
		setRequestAttribute("richiestaremissione", lRicMod);

		//Modificabilità del fascicolo.
    String lModificabile = "NO";
    if (this.IsFascicoloSiusModificabile()==true) lModificabile="SI"; else lModificabile="NO";
    this.setRequestAttribute("isModificabile", lModificabile);
		 
		return PG_LOAD_DETTAGLIO_RICHIESTA_REMISSIONE_DEBITO;	  
  }
}