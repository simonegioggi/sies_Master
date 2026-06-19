package siap.siep.misurasicurezza.action;

import org.apache.log4j.Logger;
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;


/**
* <p>Title: ActLoadDettaglioMisuraSicurezza</p>
* <p>Description: Classe Action per la load dettaglio di MisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  protected MisuraSicurezzaModel mlMisMod = null;
	
  public String processRequest() throws Exception
  {
    //  paramentro passato solo nel caso di iscrizione guidata
    if(!this.isRequestAttributeNullObj("lTipoFunzione")) 
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
    }

    // paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
    if(!this.isRequestParameterNullObj("lTipoFunzione"))
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }

    BigDecimal lId = this.getRequestBigDecimalParameter(CAMPO_ID_MISURA_SICUREZZA);
    // chiama il controller
    IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
    mlMisMod = lCtrl.ExRicercaMisuraSicurezzaByKey(lId);
    
    FascicoloSiepModel lFasRet = null;
    if (mlMisMod.getFasSieIdFascicoloSiep() != null)
    {
    	// Ricerca il Fascicolo Siep per metterlo in sessione
    	// nel caso il Dettaglio venga richiamato dall'Elenco di procedimenti con Misure Sicurezza
    	IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
    	lFasRet = lCtrlFasc.ExRicercaFascicoloByKey(mlMisMod.getFasSieIdFascicoloSiep());
    
    	if (lFasRet != null)
    	{          
    		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    		//siesLogger.debug("lFasRet " + lFasRet);

    		setRequestAttribute("fascicolo", lFasRet);
    		setSessionAttribute("fascicolo", lFasRet);
    	}
    }
    
// Ricerca Titolo Esecutivo per associarlo alla M.S.
    RiferimentoFascicoloSiepModel lRifMod = null;
    if(mlMisMod.getFasSieIdFascicoloSiepRif() != null)
    {	
    	IRiferimentoFascicoloSiep lCtrlS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
    	lRifMod = lCtrlS.ExRicercaRiferimentoFascicoloSiepByKey(mlMisMod.getFasSieIdFascicoloSiepRif());
    }	
   
    mlMisMod.setRiferimentoFascicoloSiep(lRifMod);
    setRequestAttribute("misurasicurezza", mlMisMod);
    
    String lModificabile = "SI";
    if(mlMisMod != null && mlMisMod.getIdMisuraSicurezza() != null )
    	if(mlMisMod.getFlagAnnullaMisura() != null && mlMisMod.getFlagAnnullaMisura().compareTo("A") == 0)
    		lModificabile = "NO";
    
    setRequestAttribute("Modificabile", lModificabile);
    
   return PG_LOAD_DETTAGLIOMISURASICUREZZA;
  }
}
