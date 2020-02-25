package siap.sius.misurasicurezza.action;

/**
* <p>Title: ActModificaSiusMisuraSicurezza</p>
* <p>Description: Classe Action per la modifica di MisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaSiusMisuraSicurezza extends ActionSius  implements ICostantiSiusMisuraSicurezza 
{
	/**
	* Azione di Modifica del MisuraSicurezza
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	  public String processRequest() throws F3BException
	  {
	    String lId = getRequestStringParameter(CAMPO_ID_MISURA_SICUREZZA);
	     // riempie il model
	     MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel ();

	     lMisMod.setIdMisuraSicurezza(new BigDecimal(lId));
	     lMisMod.setCodNatura( getRequestStringParameter( CAMPO_COD_NATURA) );
	     lMisMod.setCodTipo( getRequestStringParameter( CAMPO_COD_TIPO) );
	     lMisMod.setNumAnni( getRequestBigDecimalParameter( CAMPO_NUM_ANNI) );
	     lMisMod.setNumMesi( getRequestBigDecimalParameter( CAMPO_NUM_MESI) );
	     lMisMod.setNumGiorni( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI) );
	     //lMisMod.setAnnoReg38( getRequestBigDecimalParameter( CAMPO_ANNO_REG_38) );
	     //lMisMod.setNumReg38( getRequestBigDecimalParameter( CAMPO_NUM_REG_38) );
//TODO carmela recuperato valore selezionato da combo "Riferimento Titolo Esecutivo"
	     //lMisMod.setFasSieIdFascicoloSiepRif( getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_RIF) );
	      // Recupero il Riferimento Titolo Esecutivo associato alla Misura di Sicurezza
	      String rifTitoloEsecConc = getRequestStringParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_RIF);
	      // devo eliminare dalla stringa il carattere finale che mi dice se si tratta
	      // di un riferimento ad un titolo esecutivo principale (P) oppure un riferimento 
	      //ad un altro titolo esecutivo (R)
	      String flagTitEse = "";
	      String rifTitoloEsec = "";
	      if(rifTitoloEsecConc != null && !rifTitoloEsecConc.equals("-")){
	    	  rifTitoloEsec = rifTitoloEsecConc.substring(0, rifTitoloEsecConc.indexOf("/"));
	    	  flagTitEse = rifTitoloEsecConc.substring(rifTitoloEsecConc.indexOf("/") + 1, rifTitoloEsecConc.length()); 
	      }
	      
	      if(flagTitEse != null && !flagTitEse.equals("")){
	    	  if(flagTitEse.equals("P")){
		    	  lMisMod.setSenIdSentenza(BigDecimal.valueOf(Long.parseLong(rifTitoloEsec)) );
	    		  
	    	  } else {
	    		  lMisMod.setFasSieIdFascicoloSiepRif(BigDecimal.valueOf(Long.parseLong(rifTitoloEsec)) );
	    	  }
	      }
	     UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
	     lMisMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
	     lMisMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
	     lMisMod.setDataAggiornamento(DateUtils.getSysDate());
	     if(! isSessionAttributeNullObj("fascicoloSiusGP")) {
	    	 FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
	    	 lMisMod.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
	     }

	     // chiama il controller
	     IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
	     MisuraSicurezzaModel llMisModRet = lCtrl.ExModificaMisuraSicurezza(lMisMod);
	     setRequestAttribute("misurasicurezza", llMisModRet);
	     String lPage = "";
	     lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.misurasicurezza.action.ActLoadDettaglioSiusMisuraSicurezza&"+CAMPO_ID_MISURA_SICUREZZA+"="+llMisModRet.getIdMisuraSicurezza().toString();
	     return lPage;
	  }
}