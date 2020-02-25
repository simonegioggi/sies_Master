package siap.sige.reato.action;


import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.siep.reato.action.ICostantiReato;
import siap.sige.web.ActionSige;
import f3b.web.IWebConstants;
import f3b.web.html.Option;


/**
* <p>Title: ActLoadInserisciReato</p>
* <p>Description: Classe Action per la load inserisci di Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciUlterioriReatiSige extends ActionSige implements ICostantiReato
{
  public String processRequest() throws Exception
  {
	    //Controllo che non si stia lavorando su una entità in modifica ad altri
	    LockModel lck = lockIfNotLocked("reato", getRequestStringParameter(CAMPO_ID_REATO), getCodUtenteConnesso());
	    if (lck != null)
	    {
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
	      return IWebConstants.PG_MESSAGE;
	    }
	    
	    gestioneRitorno();

	    Option lOption = new Option( DecodificheManager.getInstance().getTipoReato());
      setRequestAttribute("TipiReato", "" + lOption );
      lOption  = new Option( DecodificheManager.getInstance().getTipoFonteReato() );
      setRequestAttribute("TipiFontiReato", "" + lOption );
      lOption  = new Option( DecodificheManager.getInstance().getSottonumerazione() );
      setRequestAttribute("TipiSottonumerazione", "" + lOption );
      lOption  = new Option( DecodificheManager.getInstance().getPeriodoConsumazione() );
      setRequestAttribute("PeriodoConsumazione", "" + lOption );
      lOption  = new Option( DecodificheManager.getInstance().getTipoPenaDetentiva() );
      setRequestAttribute("TipiPeneDetentive", "" + lOption );
      lOption  = new Option( DecodificheManager.getInstance().getFlagLireEuro(),"EUR" );
      setRequestAttribute("Valute", "" + lOption );

	    setRequestAttribute("modo", "SIGE");

      return PG_LOAD_INSERISCIULTERIORIREATI;  
	}
}