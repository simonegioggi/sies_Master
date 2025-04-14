package siap.sius.avvocato.action;


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
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.util.SIUSLookupRemote;
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
  	IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
  	if (!this.isRequestParameterNullObj("numeroDifensori") && this.getRequestStringParameter("numeroDifensori").equals("2"))
      throw new F3BException(F3BException.USER_MESSAGE, "Attenzione: Sono già assegnati due difensori!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug( " tipoDifensore ");
	 
		if(!this.isRequestParameterNullObj(CAMPO_ID_AVVOCATO) )
		{
	    String lAvvId = this.getRequestStringParameter(CAMPO_ID_AVVOCATO);

	    AvvocatoModel lAvv = lCtrl.ExRicercaAvvocatoByKey(new BigDecimal(lAvvId));
	    this.setRequestAttribute("avvocato", lAvv);
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug( "paolo ActLoadInserisciAssegnaAvvocato2"+ lAvv.getDescLuogoNascita());
		}
	
		if (!this.isRequestParameterNullObj("tipoDifensore") && (this.getRequestStringParameter("tipoDifensore").equalsIgnoreCase("D'UFFICIO") || this.getRequestStringParameter("tipoDifensore").equalsIgnoreCase("DELLA FASE DI GIUDIZIO")))
      throw new F3BException(F3BException.USER_MESSAGE, "I difensori possono essere due solo se entrambi sono di fiducia!");

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAvvocato());
    setRequestAttribute("tipoAvvocato", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("autoritaEsterna", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),"22");
    setRequestAttribute("autoritaEsternaDif", "" + lOption);


    lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
    setRequestAttribute("motivoDesignazione", "" + lOption);

    setRequestAttribute("modalita", "I");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( " tipoDifensore1 ");
	
    if (!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE))
      setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));

    this.gestioneRitorno();

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.
    //	IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
    //Vector lVect = lCtrl.ExRicercaForo();
		//this.setRequestAttribute("foro", lVect);
	
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug( " tipoDifensore2 ");
	
		UfficioModel lUffUte = this.getUfficioUtenteConnesso();
		String lDescrComune = lUffUte.getDescrComune();
		this.setRequestAttribute("comune", lDescrComune);

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.
		lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
		setRequestAttribute("foro", ""+ lOption);
		
		
    // MEV_21 Nuova gestione Combo per Stato di Nascita
  	lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
  	setRequestAttribute("nazione", "" + lOption );      

  	// MEV_21 Nuova gestione Combo per Stato Difensore
  	lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");
  	setRequestAttribute("statoAvv", "" + lOption ); 
		

    return PG_LOAD_INSERISCIAVVOCATO; //restituisce la jsp di VIEW
  }
}