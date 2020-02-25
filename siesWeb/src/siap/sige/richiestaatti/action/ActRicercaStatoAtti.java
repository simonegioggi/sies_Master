package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
//import siap.sico.evento.controller.IEvento;
//import siap.sico.util.SICOLookupRemote;



/**
 * <p>Title: ActRicercaStatoAtti </p>
 * <p>Description: Azione specializzazione della  ActRicercaFSigePuntuale.
 * Usa il metodo <code>processrequest()<code> del padre per ricavare il FascicoloSIUS.
 * 
 * <p>Copyright: Bull Italia S.p.A.Copyright (c) 2002</p>
 * <p>Company: Bull Italia S.p.A.</p>
 * @version 1.0
 */

public class ActRicercaStatoAtti extends ActRicercaFSigePuntuale
implements ICostantiRichiestaAtti
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  	public String processRequest() throws Exception
  	{
  		if( isRequestParameterNullObj( "noQuery") )
  	      super.processRequest();
  		
  	      // Gestione del punto di ritorno
  	      this.setLinkRitorno();
  	  
  	      
  	      BigDecimal  lIdFascicolo = null;
  	      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	      siesLogger.debug("ActRicercaStatoAtti : inizio");
  	  
  	      FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();
  	      lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
  	      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	      siesLogger.debug("Ricerca Provvedimendi da sessione");
  	  
  	      ProvvedimentoSigeModel lProvSige = new ProvvedimentoSigeModel();
  	      lProvSige.setFasIdFascicoloSige(lIdFascicolo);
  	      IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
  	      //lVect = mCtrl.ExRicercaProvvedimentiSigePerIdFasSige(lIdFascicolo);
  	      String lTipiProvv =  "'"+ICostantiProvvedimentoSige.COD_ISTRUTTORIE+"'"; // CODICE PROVVEDIMENTO_SIGE.
  	      Vector <ProvvedimentoSigeEventoModel>lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(lIdFascicolo, lTipiProvv);
  	      setRequestAttribute("provvedimenti", lVect);
  	      
  	      setRequestAttribute("atti", lVect);
  	       
  	      // Controlla se il fascicolo e' modificabile
  	      String lModificabile = "NO";
  	      
  	        if (IsFascicoloSigeModificabile()==true)	
  	          lModificabile="M";
  	        else
  	          lModificabile="NO";
  	          
  	        setRequestAttribute("modalita", lModificabile);
  	        
  	      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	      siesLogger.debug("ActRicercaStatoAtti : fine");
  	      return PG_ELENCOSTATOATTI;
  	    
    }
}