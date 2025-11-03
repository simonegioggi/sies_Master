package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per il caricamento della form di ricerca del fascciolo su cui trasferire
 * i dati dell'istruttoria
 * @author difiorlett
 * @since MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
 */
public class ActLoadTrasferisciIstruttoria extends ActionSiap implements ICostantiIstruttoriaCumulo {
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws F3BException {
	    if (this.isSessionAttributeNullObj("fascicolo"))
	    {
	      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
	    }

	    siesLogger.debug("ActLoadTrasferisciIstruttoria");
	    
	    if (!isFascicoloSiepDiCompetenza()) {
	          RedirectTo lRedirigi = new RedirectTo();
	          lRedirigi.setPage(IWebConstants.PG_MAIN);
	          setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Fascicolo non di competenza. Impossibile procedere." );
	          lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
	          setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
	          return IWebConstants.PG_MESSAGE;	        
	    }
	    
	    //==========================================================================
	    // Verifico se è stata passata l'istruttoria da trasferire e se risulta 
	    // aperta e se è di un fascicolo di competenza
	    //==========================================================================
	    if (   isRequestParameterNullObj(CAMPO_ID_ISTRUTTORIA_CUMULO)
	        || getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO)==null
	       )
	    {
	      RedirectTo lRedirigi = new RedirectTo();
	      lRedirigi.setPage(IWebConstants.PG_MAIN);
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Selezionare prima l'istruttoria da trasferire" );
	      lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
	      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
	      return IWebConstants.PG_MESSAGE;
	    }
	    else 
	    {
	      // Recupero l'istruttoria e controllo se è aperta
	      BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO) ;
	      
	      siesLogger.debug("lIdIstruttoriaCumulo = "+lIdIstruttoriaCumulo);
	      
	      IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
	      
	      IstruttoriaCumuloModel lIstruttoriaModel = null;

	      lIstruttoriaModel = lIstrCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoriaCumulo);	      
	      
	      if (lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA)){
	        setRequestAttribute("IstruttoriaCumulo", lIstruttoriaModel);
	        
	        // model degli elementi del vettore
	        String codUfficioUtente = getCodUfficioUtenteConnesso();

	        // Recuperare l'elenco degli uffici accorpati di tipo PM ed appartenenti al
	        // distretto dell'utente loggato
	        IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
	        Vector lUffAcc = lUACon.ListaUfficiAccorpati("PM", codUfficioUtente);

	        // imposta sulla request la lista degli uffici accorpati
	        setRequestAttribute("elencoUfficiAccorpati", lUffAcc);    
	        
	        
	        return PG_LOAD_CERCA_FASCICOLO_PER_TRASFERIMENTO;
	      }
	      else
	      {// Sto visualizzando una Istruttoria non aperta, non posso chiuderla e trasferirla
	        RedirectTo lRedirigi = new RedirectTo();
	        lRedirigi.setPage(IWebConstants.PG_MAIN);
	        if (lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_ANNULLATA))
	        {
	          setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'istruttoria corrente "+lIstruttoriaModel.getAnnoProtocollo()+"/"+lIstruttoriaModel.getNumProtocollo()+" del "+DateUtils.getDateToString(lIstruttoriaModel.getDataApertura(),"dd-MM-yyyy")+" risulta annullata" );
	        }
	        else if (lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_CHIUSA))
	        {
	          setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'istruttoria corrente "+lIstruttoriaModel.getAnnoProtocollo()+"/"+lIstruttoriaModel.getNumProtocollo()+" del "+DateUtils.getDateToString(lIstruttoriaModel.getDataApertura(),"dd-MM-yyyy")+" risulta chiusa a seguito emissione Provvedimento di Cumulo" );
	        }
	        else
	        {
	          setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'istruttoria corrente "+lIstruttoriaModel.getAnnoProtocollo()+"/"+lIstruttoriaModel.getNumProtocollo()+" del "+DateUtils.getDateToString(lIstruttoriaModel.getDataApertura(),"dd-MM-yyyy")+" non risulta aperta" );
	        }
	        
	        lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&"+CAMPO_ID_ISTRUTTORIA_CUMULO+"="+lIstruttoriaModel.getIdIstruttoriaCumulo());
	        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

	        return IWebConstants.PG_MESSAGE;
	      }  
	    }
	  }
}
