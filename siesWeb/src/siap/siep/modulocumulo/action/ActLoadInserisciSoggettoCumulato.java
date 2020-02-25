package siap.siep.modulocumulo.action;


/**
* <p>Title: ActLoadInserisciSoggettoCumulato</p>
* <p>Description: Classe Action per la load inserisci/Modifica di SoggettoCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

//import f3b.web.html.Option; 
import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.modulocumulo.controller.ISoggettoCumulato;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadInserisciSoggettoCumulato extends ActionModuloCumulo implements ICostantiSoggettoCumulato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  Logger logger = Logger.getLogger("actionLogger");
 /*****************************************************************************
  * Azione di caricamento della pagina di Inserimento/Modifica dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare in tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
	  
	  	super.getDatiIstruttoria();
	    super.getDatiTitoloCumulato();
	    
	    // Vediamo se deve Inserire o Modificare : SE parametro NULL è INSERIMENTO
	    String lInsMod = "";
	    
	    if(isRequestParameterNullObj("modalita") )
	    {	
	    	lInsMod = "I";
	    }	
		else
		{	/////
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("--XX-- ActLoadInserisciSoggettoCumulato - modalita = "+getRequestStringParameter("modalita" ));
			lInsMod = getRequestStringParameter("modalita");
		}	

	    SoggettoCumulatoModel lSogMod = null;
	    ISoggettoCumulato lSgCtrl = SIEPLookupRemote.getSoggettoCumuloRemote();
	    
	    if(lInsMod.compareTo("M")==0)
	    {
	    	BigDecimal lIdSog = getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO_CUMULATO);
	    	lSogMod = (SoggettoCumulatoModel)lSgCtrl.ExRicercaSoggettoCumulatoByKey(lIdSog);
	    	setRequestAttribute("soggetto", lSogMod );
	    }

	    Option lOption = new Option( DecodificheManager.getInstance().getSesso(),"M");
	    if(lSogMod != null && lSogMod.getSesso() != null)
	    {	
	    	lOption.setSelected(lSogMod.getSesso());
	    }
	   	setRequestAttribute("sesso", "" + lOption );
	    
	    //Flag Data Nascita Presunta
	    lOption = new Option( DecodificheManager.getInstance().getFlagSNTrattino(), "N");
	    if(lSogMod != null && lSogMod.getDataNascitaPresunta() != null)
	    {	
	    	lOption.setSelected(lSogMod.getDataNascitaPresunta());
	    }
	    setRequestAttribute("dataNascitaPresunta", "" + lOption );    

// Nella Form riempie la Combo 'STATO CITTADINANZA' ; Nel DB  va nella colonna SOGGETTO_CUMULATO.NAZIONALITA    
	    lOption = new Option(DecodificheManager.getInstance().getStatoCittadinanza(),"-");
	    if(lSogMod != null && lSogMod.getNazionalita() != null)
	    {	
	    	lOption.setSelected(lSogMod.getNazionalita());
	    }
	    setRequestAttribute("StatoCittadinanza", "" + lOption );

	    
//Nella Form  Riempie la combo 'STATO DI NASCITA'; Nel DB va nella colonna SOGGETTO_CUMULATO.COD_STATO_NASCITA
	    lOption = new Option( DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(),"-"), "039");
	    if(lSogMod != null && lSogMod.getCodStatoNascita() != null)
	    {	
	    	lOption.setSelected(lSogMod.getCodStatoNascita());
	    }
	    setRequestAttribute("nazioni", "" + lOption );      

// Imposta la Modalità 'I' (ins.) 'M' (mod.)
		 setRequestAttribute("modalita", lInsMod);

		 BigDecimal lIdTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		 
		 String lPage="";
		 if(lInsMod.compareTo("C")==0)
		 {
			 BigDecimal lIdSog = getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO_CUMULATO);
			 lSogMod = (SoggettoCumulatoModel)lSgCtrl.ExRicercaSoggettoCumulatoByKey(lIdSog);
			 // Cancellazione
			 lSgCtrl.ExCancellaSoggettoCumulato(lSogMod);
			 
			 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadDettaglioTitoloCumulato&" + ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" + lIdTitolo;
			 
		 }	 
		 else
		 {	 
			 lPage = PG_LOAD_INSERISCISOGGETTOCUMULATO;
		 }	 
			 
		 return lPage; 
		 
   } // CHIUDE processRequest()
  
}	// CHIUDE Classe