package siap.sius.remissionedebito.action;

/**
* <p>Title: ActModificaRichiestaRemissioneDebito</p>
* <p>Description: Classe Action per la modifica di RichiestaRemissioneDebito</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sius.ActionSius;
import siap.sius.remissionedebito.controller.IRichiestaRemissione;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActModificaRichiestaRemissioneDebito extends ActionSius  implements ICostantiSiusRemissioneDebito 
{
 /**
  * Azione di Modifica del RichiestaRemissione
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException 
  {   
  	IRichiestaRemissione lCtrl = SIUSLookupRemote.getRichiestaRemissioneRemote();
  	RichiestaRemissioneModel lRicMod = new RichiestaRemissioneModel();
  	lRicMod.setIdRichiestaRemissione      ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_REMISSIONE) );
  	lRicMod=lCtrl.ExRicercaRichiestaRemissioneById(lRicMod.getIdRichiestaRemissione() );
	
    // Recupero i dati presenti in maschera 
    // n.b. eliminare o commentare i campi non presenti in maschera 
    //      es: chiave della tabella, date_ins, foreignkey... 
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

    lRicMod.setNote(getRequestStringParameter(CAMPO_NOTE) );
   		
    lRicMod.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    lRicMod.setDataAggiornamento(DateUtils.getSysDate());
    lRicMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
  
    // Si effettua l'aggiornamento 
    lCtrl.ExModificaRichiestaRemissione(lRicMod);

    // Si Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena modificati
  	
    lRicMod=lCtrl.ExRicercaRichiestaRemissioneById(lRicMod.getIdRichiestaRemissione() );
    setRequestAttribute("richiestaremissione", lRicMod);
    return PG_LOAD_DETTAGLIO_RICHIESTA_REMISSIONE_DEBITO;	  
  }
}