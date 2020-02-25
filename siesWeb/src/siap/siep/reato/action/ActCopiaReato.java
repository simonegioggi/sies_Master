package siap.siep.reato.action;

/**
* <p>Title: ActCopiaReato</p>
* <p>Description: 	Classe Action per l'inserimento dei Reati</p>
* <p>				copiati da altro procedimento
* <p>Copyright: Copyright (c) 20002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActCopiaReato extends ActionSiap implements ICostantiReato
{

	public String processRequest() throws Exception
 	{

	    FascicoloSiepModel lFascicolo = (FascicoloSiepModel)getSessionAttribute("fascicolo");

    // Procedimento da cui copiare i reati 
		  BigDecimal IdFas = (getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_DA_COPIA));

	// Reati da Copiare	importati dalla popup precedente  
		  String [] ArrNumdeiReati = getRequestStringParameters(CAMPO_NUM_REATI_N);
		  String StrNumdeuReati = ArrNumdeiReati[0];
	
		  String[] ReatoSingolo = null;
		  ReatoSingolo = StrNumdeuReati.split(",");


		  ReatoModel lReaMod = new ReatoModel();
	      lReaMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	      lReaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	      lReaMod.setDataInserimento(DateUtils.getSysDate());
	      lReaMod.setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
		  
	      String ArrNumRitorno = null;
		  IReato lCtrl = SIEPLookupRemote.getReatoRemote();
		  ArrNumRitorno = lCtrl.ExInserisciReatiCopiati(ReatoSingolo,IdFas,lReaMod);
		  
		  StrNumdeuReati=null;
		  StrNumdeuReati=ArrNumRitorno;
		  
		  setRequestAttribute("ComingFromInsert","YES");
	
	    String lPage = "";
	    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.reato.action.ActRicercaReato&"+CAMPO_PROGR_REATO+"="+lFascicolo.getChiaveProgr()+"&"+CAMPO_NUM_REATI_N+"="+StrNumdeuReati;
	    return lPage;
	    
 	} // Chiude processrequest
  
} // Chiude classe
