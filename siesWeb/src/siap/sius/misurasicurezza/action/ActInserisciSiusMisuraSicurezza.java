package siap.sius.misurasicurezza.action;

/**
* <p>Title: ActInserisciRichiestaRemissioneDebito</p>
* <p>Description: Classe Action per l'inserimento di RichiestaRemissioneDebito</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciSiusMisuraSicurezza extends ActionSius implements ICostantiSiusMisuraSicurezza 
{
	
	
	/**
	* Azione di Inserimento del MisuraSicurezza in Sius
	* @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	* @throws F3BException
	*/
	  public String processRequest() throws Exception
	    {
			// Nuova Misura di Sicurezza da inserire
			MisuraSicurezzaModel lMisMod = null;
		  
	      if(!this.isRequestParameterNullObj("lTipoFun")) // paramentro passato solo nel caso di iscrizione guidata
	       {
	         this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFun"));
	       }

	       FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
	       //	Controllo Fascicolo non archiviato
	       //	lFasc.getCodMotivoArchiviazione().equals("01") /* STATO_FASCICOLO = ARCHIVIATO/DEFINITO */
	       if(lFasGPMod == null || lFasGPMod.getFascicoloSiusModel() == null || "01".equals(lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo()) )
	       {
	         throw new F3BException(F3BException.USER_MESSAGE, "Il Procedimento non esiste o risulta archiviato");
	       }
	       
	       lMisMod = letturaDatiMisura(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

	       IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
	       MisuraSicurezzaModel llMisModRet = lCtrl.ExInserisciMisuraSicurezza(lMisMod);

	        //Prepara la pagina di destinazione
	       String lPage = "";
	       lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.misurasicurezza.action.ActLoadDettaglioSiusMisuraSicurezza&"+CAMPO_ID_MISURA_SICUREZZA+"="+llMisModRet.getIdMisuraSicurezza().toString();
	       return lPage;
	    }
	  
	  protected MisuraSicurezzaModel letturaDatiMisura(BigDecimal aIdFascicoloSius) throws Exception
	  {
		  MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel();

	      lMisMod.setCodNatura( getRequestStringParameter( CAMPO_COD_NATURA) );
	      lMisMod.setCodTipo( getRequestStringParameter( CAMPO_COD_TIPO) );
	      lMisMod.setNumAnni( getRequestBigDecimalParameter( CAMPO_NUM_ANNI) );
	      lMisMod.setNumMesi( getRequestBigDecimalParameter( CAMPO_NUM_MESI) );
	      lMisMod.setNumGiorni( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI) );
	      lMisMod.setAnnoReg38(new BigDecimal( DateUtils.getSysDate("yyyy")));
	      //lMisMod.setNumReg38( getRequestBigDecimalParameter( CAMPO_NUM_REG_38) );
//TODO carmela recuperato valore selezionato da combo "Riferimento Titolo Esecutivo"
	      
	      // Recupero il Riferimento Titolo Esecutivo associato alla Misura di Sicurezza
	      String rifTitoloEsecConc = getRequestStringParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_RIF);
	      // devo eliminare dalla stringa il carattere finale che mi dice se si tratta
	      // di un riferimento ad un titolo esecutivo principale (P) oppure un riferimento 
	      //ad un altro titolo esecutivo (R)
	      String flagTitEse = "";
	      String rifTitoloEsec = "";
	      if(rifTitoloEsecConc != null && !rifTitoloEsecConc.equals("-")){
	    	  rifTitoloEsec = rifTitoloEsecConc.substring(0, rifTitoloEsecConc.indexOf("/"));
	    	  flagTitEse = rifTitoloEsecConc.substring(rifTitoloEsecConc.indexOf("/")+1, rifTitoloEsecConc.length()); 
	      }
	      
	      if(flagTitEse != null && !flagTitEse.equals("")){
	    	  if(flagTitEse.equals("P")){
		    	  lMisMod.setSenIdSentenza(BigDecimal.valueOf(Long.parseLong(rifTitoloEsec)) );
	    	  } else {
	    		  lMisMod.setFasSieIdFascicoloSiepRif( BigDecimal.valueOf(Long.parseLong(rifTitoloEsec)) );
	    	  }
	      }
	      
	      lMisMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	      lMisMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	      lMisMod.setDataInserimento(DateUtils.getSysDate());
	      lMisMod.setFasSiuIdFascicoloSius(aIdFascicoloSius);
	      
	      //lMisMod.setEveIdEvento( getRequestBigDecimalParameter( CAMPO_EVE_ID_EVENTO) );
	      return lMisMod;
	  }
}