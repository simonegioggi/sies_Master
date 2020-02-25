package siap.sige.beneficio.action;


/**
* <p>Title: ActInserisciBeneficioSige</p>
* <p>Description: Classe Action per l'inserimento di BeneficioSige</p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile</p>
* @version 1.0
*/
import java.math.BigDecimal;

import siap.siep.beneficio.action.ActInserisciBeneficio;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.beneficio.model.BeneficioSigeModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActInserisciBeneficioSige extends ActInserisciBeneficio 
{

	public String processRequest() throws Exception
    {
		BeneficioSigeModel lBenMod = null;
		BeneficioSigeModel lBenNMMod = null;
		
	  // Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
	  BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
	
	  // lettura dati dalla form usando la funzione ereditata dall'ancestor
	  letturaBeneficio(null);
	  
	  // Conversione Beneficio
	  if (mBenMod != null)
		  lBenMod = new BeneficioSigeModel(mBenMod, lIdFasSigeSen);
	  // Conversione Beneficio Non Menzione
	  if (mBenNMMod != null)
		  lBenNMMod = new BeneficioSigeModel(mBenNMMod, lIdFasSigeSen);
	  
	  //Inserimento utilizzando il Controller
	  IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
	  mBenMod  = lCtrl.ExInserisciBeneficioTipOrario(lBenMod,mTipologie,null,lBenNMMod);
     
      //Prepara la "pagina" di destinActione.
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction("siap.sige.beneficio.action.ActLoadDettaglioBeneficioSige");
      lRedirigi.setParameter(CAMPO_ID_BENEFICIO, mBenMod.getIdBeneficio().toString());
      lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "10");
    
	  
      return lRedirigi.toString();
 	  
    }
}