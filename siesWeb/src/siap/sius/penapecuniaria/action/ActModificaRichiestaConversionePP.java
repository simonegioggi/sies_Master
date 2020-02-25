package siap.sius.penapecuniaria.action;

/**
* <p>Title: ActModificaRichiestaConversione</p>
* <p>Description: Classe Action per la modifica di RichiestaConversione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActModificaRichiestaConversionePP extends ActionSius  implements ICostantiSiusPenaPecuniaria 
{
 /**
  * Azione di Modifica del RichiestaConversione
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException 
  {   
  	IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
  	RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
  	lRicMod.setIdRichiestaConversione      ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_CONVERSIONE) );
  	lRicMod=lCtrl.ExRicercaRichiestaConversioneByIdSenzaEvento(lRicMod.getIdRichiestaConversione() );
	
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
	
    lRicMod.setDataRicezioneAtto           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_RICEZIONE_ATTO,CAMPO_MESE_DATA_RICEZIONE_ATTO,CAMPO_GIORNO_DATA_RICEZIONE_ATTO) );
    lRicMod.setDataIscrizioneAtto          ( getRequestDateParameter       ( CAMPO_ANNO_DATA_ISCRIZIONE_ATTO,CAMPO_MESE_DATA_ISCRIZIONE_ATTO,CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO) );
    lRicMod.setDataEsazione                ( getRequestDateParameter       ( CAMPO_ANNO_DATA_ESAZIONE,CAMPO_MESE_DATA_ESAZIONE,CAMPO_GIORNO_DATA_ESAZIONE) );
    if (  (getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") != null && !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")).equals("")) 
        ||(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC") != null && !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")).equals("")) ) 
    { 
      lRicMod.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") + "."+getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC"))); 
    } 
    lRicMod.setDataPrescrizioneMulta       ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PRESCRIZIONE_MULTA,CAMPO_MESE_DATA_PRESCRIZIONE_MULTA,CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA) );
    if (isRequestChecked  ( CAMPO_FLAG_IMPRESCRITTIBILE_MULTA) ){
		lRicMod.setFlagImprescrittibileMulta ("S");
    }else{
    	lRicMod.setFlagImprescrittibileMulta ("N");
    } 
    
    if (  (getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") != null && !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT")).equals("")) 
        ||(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC") != null && !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC")).equals("")) ) 
    { 
      lRicMod.setImportoAmmenda(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") + "."+getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC"))); 
    } 
    lRicMod.setDataPrescrizioneAmmenda     ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PRESCRIZIONE_AMMENDA,CAMPO_MESE_DATA_PRESCRIZIONE_AMMENDA,CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA) );

    if (isRequestChecked  ( CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA) ){ 
    	lRicMod.setFlagImprescrittibileAmmenda ("S");
    }else{
    	lRicMod.setFlagImprescrittibileAmmenda ("N");
    } 
    
    lRicMod.setNote(getRequestStringParameter(CAMPO_NOTE) );
    		
    lRicMod.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    lRicMod.setDataAggiornamento(DateUtils.getSysDate());
    lRicMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
  
    // Si effettua l'aggiornamento 
    lCtrl.ExModificaRichiestaConversione(lRicMod);

    // Si Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena modificati
  	
    lRicMod=lCtrl.ExRicercaRichiestaConversioneByIdSenzaEvento(lRicMod.getIdRichiestaConversione() );
    setRequestAttribute("richiestaconversione", lRicMod);
    return PG_LOAD_DETTAGLIO_RICHIESTA_CONVERSIONE_PP;	  
  }
}