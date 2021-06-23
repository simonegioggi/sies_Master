package siap.siep.avvocato.action;


/**
* <p>Title: ActLoadInserisciAssegnaAvvocato</p>
* <p>Description: Classe Action per la load inserisci di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadInserisciAssegnaAvvocato
    extends ActionSiap
    implements ICostantiAvvocato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
	
	if(!this.isRequestParameterNullObj(CAMPO_ID_AVVOCATO) )
	{
	    String lAvvId = this.getRequestStringParameter(CAMPO_ID_AVVOCATO);

		AvvocatoModel lAvv = lCtrl.ExRicercaAvvocatoByKey(new BigDecimal(lAvvId));
		this.setRequestAttribute("avvocato", lAvv);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug( "paolo ActLoadInserisciAssegnaAvvocato1"+ lAvv.getDescLuogoNascita());
	}
	  
	
    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }

    if (!this.isRequestParameterNullObj("numeroDifensori") && this.getRequestStringParameter("numeroDifensori").equals("2"))
      throw new F3BException(F3BException.USER_MESSAGE, "Attenzione: Sono già assegnati due difensori!");

    if (!this.isRequestParameterNullObj("tipoDifensore") && this.getRequestStringParameter("tipoDifensore").equalsIgnoreCase("D'UFFICIO"))
      throw new F3BException(F3BException.USER_MESSAGE, "I difensori possono essere due solo se entrambi di Fiducia o entrambi della Fase di Giudizio!");
    
    Option lOption = new Option(DecodificheManager.getInstance().getTipoAvvocato());
    setRequestAttribute("tipoAvvocato", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("autoritaEsterna", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),"22");
    setRequestAttribute("autoritaEsternaDif", "" + lOption);


    lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
    setRequestAttribute("motivoDesignazione", "" + lOption);

    setRequestAttribute("modalita", "I");

    if (!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE))
      setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
	
    // 19/03/2010 Nuova gestione Combo per Foro avvocato.
    //Vector lVect = lCtrl.ExRicercaForiCaricati();
    //this.setRequestAttribute("foro", lVect);
	
    UfficioModel lUffUte = this.getUfficioUtenteConnesso();
    String lDescrComune = lUffUte.getDescrComune();
    this.setRequestAttribute("comune", lDescrComune);

    // 19/03/2010 Nuova gestione Combo per Foro avvocato.
  	lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
  	setRequestAttribute("foro", ""+ lOption);
  	
    // 20210620 MEV_21 Nuova gestione Combo per Stato di Nascita
  	lOption = new Option( DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(),"039"), "-");
  	setRequestAttribute("nazioni", "" + lOption );      
    
    return PG_ASSEGNA_INSERISCI_DIFENSORE; //restituisce la jsp di VIEW

  }

}