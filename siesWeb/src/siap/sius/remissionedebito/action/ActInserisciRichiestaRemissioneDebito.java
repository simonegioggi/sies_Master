package siap.sius.remissionedebito.action;

/**
* <p>Title: ActInserisciRichiestaRemissioneDebito</p>
* <p>Description: Classe Action per l'inserimento di RichiestaRemissioneDebito</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.remissionedebito.controller.IRichiestaRemissione;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciRichiestaRemissioneDebito extends ActionSius implements ICostantiSiusRemissioneDebito 
{

 /**
  * Azione di Inserimento del RichiestaRemissione
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException 
  {
	  RichiestaRemissioneModel lRicMod = new RichiestaRemissioneModel();

    //Recupero il Fascicolo SIUS dalla sessione.
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

    // Recupero i dati presenti in maschera. 
    lRicMod.setAnnoPartita                 ( getRequestBigDecimalParameter ( CAMPO_ANNO_PARTITA) );
    lRicMod.setNumPartita                  ( getRequestBigDecimalParameter ( CAMPO_NUM_PARTITA) );
    lRicMod.setNumExCampione               ( getRequestStringParameter     ( CAMPO_NUM_EX_CAMPIONE) );
    lRicMod.setProtCircosrizioneDoganale   ( getRequestStringParameter     ( CAMPO_PROT_CIRCOSRIZIONE_DOGANALE) );
    lRicMod.setCodTipoAutoritaEmittente    ( getRequestStringParameter     ( CAMPO_COD_TIPO_AUTORITA_EMITTENTE) );
     
    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUOGO_EMITTENTE )) );
    lRicMod.setCodLuogoEmittente( lComMod.getCodComune());
	
    lRicMod.setCodTipoProvvedimento        ( getRequestStringParameter     ( COD_TIPO_PROVVEDIMENTO) );
    lRicMod.setDataEmissione               ( getRequestDateParameter       ( CAMPO_ANNO_DATA_EMISSIONE,CAMPO_MESE_DATA_EMISSIONE,CAMPO_GIORNO_DATA_EMISSIONE) );
    lRicMod.setCodAutoritaEmittenteProvv   ( getRequestStringParameter     ( COD_AUTORITA_EMITTENTE_PROVV) );
    ComuneModel lComProvvMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( COD_LUOGO_EMITTENTE_PROVV )) );
    lRicMod.setCodLuogoEmittenteProvv      ( lComProvvMod.getCodComune());

    if (  (getRequestStringParameter(IMPORTO_SPESE_CARCERE + "INT") != null && !(getRequestStringParameter(IMPORTO_SPESE_CARCERE + "INT")).equals("")) 
        ||(getRequestStringParameter(IMPORTO_SPESE_CARCERE + "DEC") != null && !(getRequestStringParameter(IMPORTO_SPESE_CARCERE + "DEC")).equals("")) ) 

    { 
      lRicMod.setImportoSpeseCarcere (new BigDecimal(getRequestStringParameter(IMPORTO_SPESE_CARCERE + "INT") + "."+getRequestStringParameter(IMPORTO_SPESE_CARCERE + "DEC"))); 
    } 
    
    if (!isRequestParameterNullObj(FLAG_SPESE_CARCERE)  &&  isRequestChecked  ( FLAG_SPESE_CARCERE) ){
    	lRicMod.setFlagSpeseCarcere ("S");
    }else{
  		lRicMod.setFlagSpeseCarcere ("N");
  	} 

    if (  (getRequestStringParameter(IMPORTO_SPESE_PROCEDIMENTO + "INT") != null && !(getRequestStringParameter(IMPORTO_SPESE_PROCEDIMENTO + "INT")).equals("")) 
            ||(getRequestStringParameter(IMPORTO_SPESE_PROCEDIMENTO + "DEC") != null && !(getRequestStringParameter(IMPORTO_SPESE_PROCEDIMENTO + "DEC")).equals("")) ) 

        { 
          lRicMod.setImportoSpeseProcedimento (new BigDecimal(getRequestStringParameter(IMPORTO_SPESE_PROCEDIMENTO + "INT") + "."+getRequestStringParameter(IMPORTO_SPESE_PROCEDIMENTO + "DEC"))); 
        } 
        
        if (!isRequestParameterNullObj(FLAG_SPESE_PROCEDIMENTO)  &&  isRequestChecked  ( FLAG_SPESE_PROCEDIMENTO) ){
        	lRicMod.setFlagSpeseProcedimento ("S");
        }else{
      		lRicMod.setFlagSpeseProcedimento ("N");
      	} 

    lRicMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lRicMod.setDataInserimento(DateUtils.getSysDate());
    lRicMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lRicMod.setNote( getRequestStringParameter (CAMPO_NOTE));
    
    // Recupera il controller ed effettua l'inserimento della Richiesta Rimessione Debito. 
    IRichiestaRemissione lCtrlRic = SIUSLookupRemote.getRichiestaRemissioneRemote();
	lRicMod.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
	lRicMod.setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

	lRicMod=lCtrlRic.ExInserisciRichiestaRemissione(lRicMod);

    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.remissionedebito.action.ActLoadDettaglioRichiestaRemissioneDebito";
    lPage += "&" + CAMPO_ID_RICHIESTA_REMISSIONE + "=" + lRicMod.getIdRichiestaRemissione().toString();

    return lPage;
  }
}