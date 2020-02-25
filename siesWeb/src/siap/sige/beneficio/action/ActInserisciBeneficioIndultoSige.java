package siap.sige.beneficio.action;


/**
* <p>Title: ActInserisciBeneficioIndultoSige</p>
* <p>Description: Classe Action per l'inserimento di BeneficioIndultoSige</p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile</p>
* @version 1.0
*/
import java.math.BigDecimal;

import siap.siep.beneficio.action.ActInserisciBeneficioIndulto;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.beneficio.model.BeneficioSigeModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActInserisciBeneficioIndultoSige extends ActInserisciBeneficioIndulto 
{

	public String processRequest() throws F3BException
    {
		BeneficioSigeModel lBenMod = null;
		
	  // Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
	  BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
	
	  // lettura dati dalla form usando la funzione ereditata dall'ancestor
	  letturaBeneficio(null);
	  
	  // Conversione Beneficio
	  if (mBenMod != null)
		  lBenMod = new BeneficioSigeModel(mBenMod, lIdFasSigeSen);
	  
	  //Inserimento utilizzando il Controller
	  IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
	  mBenMod = lCtrl.ExInserisciBeneficioTipOrario(lBenMod,null,mListaIdPeneAcc,null);

      //Prepara la "pagina" di destinActione.
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction("siap.sige.beneficio.action.ActLoadDettaglioBeneficioIndultoSige");
      lRedirigi.setParameter(CAMPO_ID_BENEFICIO, mBenMod.getIdBeneficio().toString());
      lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "10");
    
	  
      return lRedirigi.toString();
 	  
    }
}