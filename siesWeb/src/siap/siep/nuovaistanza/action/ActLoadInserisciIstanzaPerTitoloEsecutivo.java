package siap.siep.nuovaistanza.action;


/**
* <p>Title: ActLoadInserisciIstanzaPerTitoloEsecutivo</p>
* <p>Description: Classe Action per la load inserisci di Istanza per titolo esecutivo</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile Servizi S.r.l.</p>
* @version 5.0
*/

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadInserisciIstanzaPerTitoloEsecutivo extends ActionSiap implements ICostantiNuovaIstanza
{
 /*****************************************************************************
  * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare i tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws Exception 
  {

	    this.removeSessionAttribute("fascicolo");
	    this.removeSessionAttribute("sentenza");
	    this.removeSessionAttribute("penaresidua");	    
	    
	 //indica se inserisco istanza o sentenza+istanza o soggetto+sentenza+istanza 
	String lTipoInserimento = "nuovo";  
	if(!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP))//istanza  
	{
		setRequestAttribute("idfascicolo",""+ getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));
		lTipoInserimento = "fascicolo";
		
	    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

	    DettaglioFascicoloModel lDettaglio = lCtrl.ExDettaglioFascicoloSiep(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));
	    setSessionAttribute("fascicolo", lDettaglio.getFascicoloSiep());

	    setSessionAttribute("penaresidua", lDettaglio.getPenaResidua());

	}
	else if(!isRequestParameterNullObj(ICostantiSentenza.CAMPO_ID_SENTENZA))//sentenza+istanza
	{
		setRequestAttribute("idsentenza",""+ getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ID_SENTENZA));
		lTipoInserimento = "sentenza";
		
	    ISentenza lCtrl = SIEPLookupRemote.getSentenzaRemote();
	    SentenzaModel lSen = lCtrl.ExRicercaSentenzaByKey( getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ID_SENTENZA));
	    setSessionAttribute("sentenza",lSen );
	
		
	}else//soggetto+sentenza+istanza 
	{
		lTipoInserimento = "nuovo";
	}
	
	setRequestAttribute("tipoinserimento",lTipoInserimento);
	
	  
  // Riempie la combo delle nazioni
     Option lOption = new Option( DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(),"-"), "039");
     setRequestAttribute("nazioni", "" + lOption );

     lOption = new Option( DecodificheManager.getInstance().getSesso(),"M");
     setRequestAttribute("sesso", "" + lOption );

     // 28/04/2010 lOption = new Option( DecodificheManager.getInstance().getNazionalita(),"I");
     //setRequestAttribute("nazionalita", "" + lOption );
     //ANNA per consentire la presenza del - nella lista nazioni
     //lOption = new Option( DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getStatoCittadinanza(),"-"), "039");
     lOption = new Option(DecodificheManager.getInstance().getStatoCittadinanza(),"-");
     setRequestAttribute("StatoCittadinanza", "" + lOption );

    //Flag Data Nascita Presunta
     lOption = new Option( DecodificheManager.getInstance().getFlagSNTrattino(), "N");
     setRequestAttribute("dataNascitaPresunta", "" + lOption );
	  
     // 19/04/2010 TipoAutoritaMittenteIstanza() per Gestione "Nuova Istanza" 
     lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaMittenteIstanza(), "-" );
     setRequestAttribute("autorita", "" + lOption );
     
     Option lOptionTA = new Option(DecodificheManager.getInstance().getTipoAvvocato());
     setRequestAttribute("tipoAvvocato", "" + lOptionTA);

     Option lOptionCI = new Option(DecodificheManager.getInstance().getTipoContenutoIstanza());
     setRequestAttribute("contenuto", "" + lOptionCI);    
   
  	// Imposta la decisione cassazione
     lOption = new Option( DecodificheManager.getInstance().getTipoDecisioneCassazione(), "-");
     setRequestAttribute("tipoDecisioneCassazione", "" + lOption );
  
    // Imposta i provvedimenti Rif.
  	lOption = new Option( DecodificheManager.getInstance().getTipoProvvedimentiRif(), "-");
    setRequestAttribute("tipoProvvedimentiRif", "" + lOption );

    // Imposta le autorità.
    lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
    setRequestAttribute("autoritaEmi", "" + lOption );
    setRequestAttribute("autoritaProvRif", "" + lOption );

    lOption = new Option( DecodificheManager.getInstance().getFlagSN(), "N");
    setRequestAttribute("flagSN", "" + lOption );
    
    lOption = new Option( DecodificheManager.getInstance().getTipoRitoSentenza(), "-");
    setRequestAttribute("tipoRito1", "" + lOption );
    setRequestAttribute("tipoRito2", "" + lOption );

  	// PARTE RELATIVA ALL'ISCRIZIONE SENTENZA STRANIERA
    // 14/06/2010 Sostituzione Elenco Autorità Emittenti
    // lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS(), "-");
    lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");

    setRequestAttribute("autoritaEmi2", "" + lOption );     
     
    Option lOptionProvv = new Option( DecodificheManager.getInstance().getTipoProvvedimenti(), "-");
    lOptionProvv.setFilter(new String[]{"-", "01", "53"});    
    setRequestAttribute("tipoProvvedimenti", "" + lOptionProvv );
    setRequestAttribute("tipoProvvedimentiAltro", "" + lOptionProvv );

    // 20210730 Gestione Combo per Foro avvocato.
    //lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
    //String lStatoForo = DecodificheUtils.getCodAltebyCode(DecodificheManager.getInstance().getForoAll(), avvocato.getAvvocato().getForo());
    
    //if ("SOPPRESSO".equals(lStatoForo)){
    // aggiungo un black item. La combo foro non deve presentare un valore preselezionato
    lOption = new Option(DecodificheManager.getInstance().getForo(), Option.BLANK_ITEM);
    //} else {
    //  lOption = new Option(DecodificheManager.getInstance().getForo(), avvocato.getAvvocato().getForo(),Option.NO_BLANK_ITEM);
    //}
    setRequestAttribute("foro", ""+ lOption);
    setRequestAttribute("foroP", ""+ lOption);

    // 20210730 MEV_21 Nuova gestione Combo per Stato di Nascita
	lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
	setRequestAttribute("nazione", "" + lOption );      
	setRequestAttribute("nazioneP", "" + lOption );      
	
	// 20210730 MEV_21 Nuova gestione Combo per Stato Difensore
	lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");
	setRequestAttribute("statoAvv", "" + lOption );      
	setRequestAttribute("statoAvvP", "" + lOption );      
    
    // Imposta la Modalità a Inserimento.
    setRequestAttribute("modalita", "I");

    // Restituisce la pagina di Inserimento dei Dati 
    return PG_LOAD_INSERISCI_TIT_ESEC_ISTANZA; 
  }
}