package siap.sige.misurasicurezza.action;


/**
* <p>Title: ActInserisciMisuraSicurezzaSige</p>
* <p>Description: Classe Action per l'inserimento di MisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/
import java.math.BigDecimal;

import siap.siep.misurasicurezza.action.ActInserisciMisuraSicurezza;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.misurasicurezza.model.MisuraSicurezzaSigeModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActInserisciMisuraSicurezzaSige extends ActInserisciMisuraSicurezza 
{

	public String processRequest() throws Exception
    {
	  // Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
	  BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
		
	  // Misura di Sicurezza da inserire
	  MisuraSicurezzaSigeModel lMisura = null;
	  
	  // I dati della Misura vengono letti usando la funzione ereditata dall'ancestor
	  lMisura = new MisuraSicurezzaSigeModel(letturaDatiMisura(null), lIdFasSigeSen);
	  
	  // Inserimento
	  IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
	  MisuraSicurezzaModel llMisModRet = lCtrl.ExInserisciMisuraSicurezza(lMisura);

     
       //Prepara la "pagina" di destinActione.
       RedirectTo lRedirigi = new RedirectTo();
       lRedirigi.setPage(IWebConstants.PG_MAIN);
       lRedirigi.setAction("siap.sige.misurasicurezza.action.ActLoadDettaglioMisuraSicurezzaSige");
       lRedirigi.setParameter(CAMPO_ID_MISURA_SICUREZZA, llMisModRet.getIdMisuraSicurezza().toString());
       lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "10");
     
       return lRedirigi.toString();
       
    }
}