package siap.sius.presaincarico.action;


import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.jms.action.ICostantiSiepJMS;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActRitrasmissioneAttiCompetenza</p>
 * <p>Description: Traferimento ad altro ufficio del fascicolo ricevuto per competenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActRitrasmissioneAttiCompetenza extends ActionSiap implements ICostantiJMS{

	public String processRequest() throws Exception
	  {
	    if (   JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE")!=null
	        && JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")
	       )
	    {
	      SIAPReceiver.getInstance().testInArrivo();
	      SIAPReceiver.getInstance().testInPartenza();
	      SIAPReceiver.getInstance().testStampa();
	    }
	    else{
	      SIAPReceiver.getInstance();
	    }
	    
	    
	    //Cerco l'ufficio (nuovo mittente) selezionato in maschera			  
	    String lTipoUff = getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO);
		String lSedeUff = getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO);

		String lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);

		UfficioModel lLocal = getUfficioByCodUfficio(lCodiceUfficio);
		UfficioModel destBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());
		
		//eseguo le operazioni per il messaggio di risposta (esito della trasmissione)
	    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

	    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
	    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);	    

	    UfficioModel mBDI = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
	    MessaggioModel lMessage = new MessaggioModel(lMess);
	    
	    //SETTA RIFERIMENTI FASCICOLO SIEP
	    lMessage.setDescrBdiDestinataria(destBDI.getDescrComune());
	    lMessage.setCodBdiDestinataria(destBDI.getCodUfficio());
	    lMessage.setCodUfficioDestinatario(lCodiceUfficio);
	    lMessage.setCodBdiMittente(mBDI.getCodUfficio());
	    lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
	    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
	    lMessage.setCodTipoMessaggio(RICHIESTA);
	    lMessage.setCodTipoOperazione(TRASFERIMENTO_COMPETENZA);
	    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
	    lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
	    lMessage.setDataInvio(DateUtils.getSysDate());
	    lMessage.setDataEsito(DateUtils.getSysDate());	    
	    lMessage.setTreeModel(lMess.getTreeModel());
	    SIAPSender lSender = new SIAPSender();
	    lSender.send(lMessage);

	    //Marco il messaggio di richiesta evaso.
	    lMess.setFlagVisto("S");
	    lMess.setDataEsito(DateUtils.getSysDate());
	    lCrtl.ExModificaMessaggio(lMess);

	    // setta la risposta nella request
	    setRequestAttribute(IWebConstants.MESSAGE_TEXT, 
	    		"Il Fascicolo è stato trasmesso all'ufficio selezionato");

		 //Prepara la "pagina" di destinAction
	    RedirectTo lRedirigi = new RedirectTo();
	    lRedirigi.setPage( IWebConstants.PG_MAIN );
	    lRedirigi.setAction( "siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti" );
	    
	    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

	    return IWebConstants.PG_MESSAGE;
		
		
	  }
}