package siap.sius.penapecuniaria.action;


/**
* <p>Title: ActLoadInserisciRichiestaConversionePP</p>
* <p>Description: Classe Action per la load inserisci di RichiestaConversione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadInserisciRichiestaConversionePP extends ActionSius  implements ICostantiSiusPenaPecuniaria
{
  public String processRequest() 
    throws F3BException 
  {
    if (this.IsFascicoloSiusModificabile()==false)
      throw new SIUSException(SIUSException.USER_MESSAGE,ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

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
        
        // 08/09/2015 Inibita l'iscrizione di nuove Richieste Conversione lato SIUS, se al procedimento SIUS è legato un procedimento SIEP di classe VII.
	    int lFascProg = lFasSiep.getChiaveProgr().intValue();
	    if  (lFascProg > 70000  && 
	    	 lFascProg < 80000  	)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Non è consentito iscrivere nuove Richieste Conversione se al procedimento SIUS è legato un procedimento SIEP di classe VII!");
    }
    	
  	setRequestAttribute("dataIrrevocabilita", lDataIrrevocabilita);

  	// autorità per la conversione
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutorita() );
    lOption.setFilter( new String[] {"-","36", "99", "57","37", "97", "98", "38"});
    setRequestAttribute("autoritaConv", "" + lOption ); 

    // Imposta la Modalità a Inserimento.
    setRequestAttribute("modalita", "I");
	
    // Restituisce la pagina di Inserimento dei Dati 
    return PG_LOAD_INSERISCI_RICHIESTACONVERSIONE_PP; 
	}
}
 