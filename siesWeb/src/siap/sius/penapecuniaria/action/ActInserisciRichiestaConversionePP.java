package siap.sius.penapecuniaria.action;

/**
* <p>Title: ActInserisciRichiestaConversionePP</p>
* <p>Description: Classe Action per l'inserimento di RichiestaConversione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciRichiestaConversionePP extends ActionSius implements ICostantiSiusPenaPecuniaria 
{
 /**
  * Azione di Inserimento del RichiestaConversione
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException 
  {
    RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();

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
	
    lRicMod.setDataRicezioneAtto           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_RICEZIONE_ATTO,CAMPO_MESE_DATA_RICEZIONE_ATTO,CAMPO_GIORNO_DATA_RICEZIONE_ATTO) );
    lRicMod.setDataIscrizioneAtto          ( getRequestDateParameter       ( CAMPO_ANNO_DATA_ISCRIZIONE_ATTO,CAMPO_MESE_DATA_ISCRIZIONE_ATTO,CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO) );
    lRicMod.setDataEsazione                ( getRequestDateParameter       ( CAMPO_ANNO_DATA_ESAZIONE,CAMPO_MESE_DATA_ESAZIONE,CAMPO_GIORNO_DATA_ESAZIONE) );
    if (  (getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") != null && !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")).equals("")) 
        ||(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC") != null && !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")).equals("")) ) 
    { 
      lRicMod.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") + "."+getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC"))); 
    } 
    lRicMod.setDataPrescrizioneMulta       ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PRESCRIZIONE_MULTA,CAMPO_MESE_DATA_PRESCRIZIONE_MULTA,CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA) );
    
    if (!isRequestParameterNullObj(CAMPO_FLAG_IMPRESCRITTIBILE_MULTA)  &&  isRequestChecked  ( CAMPO_FLAG_IMPRESCRITTIBILE_MULTA) ){
    	lRicMod.setFlagImprescrittibileMulta ("S");
    }else{
  		lRicMod.setFlagImprescrittibileMulta ("N");
  	} 
	
    if (  (getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") != null && !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT")).equals("")) 
        ||(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC") != null && !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC")).equals("")) ) 
    { 
      lRicMod.setImportoAmmenda(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") + "."+getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC"))); 
    } 
    lRicMod.setDataPrescrizioneAmmenda ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PRESCRIZIONE_AMMENDA,CAMPO_MESE_DATA_PRESCRIZIONE_AMMENDA,CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA) );
    if (!isRequestParameterNullObj(CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA)  &&  isRequestChecked  ( CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA) ){
    		lRicMod.setFlagImprescrittibileAmmenda ("S");
  	}else{
  		lRicMod.setFlagImprescrittibileAmmenda ("N");
  	} 

    lRicMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lRicMod.setDataInserimento(DateUtils.getSysDate());
    lRicMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lRicMod.setNote(CAMPO_NOTE);
    
    // Recupera il controller ed effettua l'inserimento della Richiesta Conversione PP. 
    IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
		lRicMod.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lRicMod.setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

		if (getRequestStringParameter("data_Irrevocabilita") != null )
				lRicMod.setDataIrrevocabilita( ( getRequestDateParameter ( CAMPO_ANNO_DATA_IRREVOCABILITA, CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA) ));

		lRicMod=lCtrlRic.ExInserisciRichiestaConversione(lRicMod);

    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.penapecuniaria.action.ActLoadDettaglioRichiestaConversionePP";
    lPage += "&" + CAMPO_ID_RICHIESTA_CONVERSIONE + "=" + lRicMod.getIdRichiestaConversione().toString();

    return lPage;
  }
}