package siap.siep.modulocumulo.action;

/**
* <p>Title: ActModificaReatoCumulo</p>
* <p>Description: Classe Action per la modifica di Reato</p>
* <p>   in ambito Cumulo (legato al titolo Cumulato)</p>
*/

import siap.siep.cumulo.action.ICostantiCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaReatoCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo, ICostantiCumulo
{
  /**
  * Azione di Modifica del Reato in ambito Cumulo (legato al titolo Cumulato)
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
  {
    
    // riempie il model
    ReatoCumuloModel lReaMod = new ReatoCumuloModel ();
    lReaMod.setIdReatoCum (getRequestBigDecimalParameter(CAMPO_ID_REATO_CUM));

    lReaMod.setProgrReato       (getRequestBigDecimalParameter(CAMPO_PROGR_REATO));    
    lReaMod.setProgrCircostanza (getRequestBigDecimalParameter(CAMPO_PROGR_CIRCOSTANZA));
    
  
    if(!isRequestParameterNullObj(CAMPO_PROGR_NUMERO_MANUALE))
      lReaMod.setProgrNumeroManuale( getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE) );


    if(!isRequestParameterNullObj(CAMPO_COD_TIPO_REATO))
      lReaMod.setCodTipoReato( getRequestStringParameter( CAMPO_COD_TIPO_REATO) );
    else
      lReaMod.setCodTipoReato("-");

    if(  !isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO)
      && !isRequestParameterNullObj(CAMPO_MESE_DATA_INIZIO)
      && !isRequestParameterNullObj(CAMPO_GIORNO_DATA_INIZIO) )
    {
      lReaMod.setDataInizio( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO) );
    }
    

    if( !isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO))
      lReaMod.setAnnoInizio( getRequestBigDecimalParameter( CAMPO_ANNO_DATA_INIZIO) );
    if( !isRequestParameterNullObj(CAMPO_MESE_DATA_INIZIO))
      lReaMod.setMeseInizio( getRequestBigDecimalParameter( CAMPO_MESE_DATA_INIZIO) );
    if( !isRequestParameterNullObj(CAMPO_GIORNO_DATA_INIZIO))
      lReaMod.setGiornoInizio( getRequestBigDecimalParameter( CAMPO_GIORNO_DATA_INIZIO) );
    
    if(  !isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE)
      && !isRequestParameterNullObj(CAMPO_MESE_DATA_FINE)
      && !isRequestParameterNullObj(CAMPO_GIORNO_DATA_FINE) )
    {
      lReaMod.setDataFine( getRequestDateParameter( CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE) );
    }
    
    if( !isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE))
      lReaMod.setAnnoFine( getRequestBigDecimalParameter( CAMPO_ANNO_DATA_FINE) );
    if( !isRequestParameterNullObj(CAMPO_MESE_DATA_FINE))
      lReaMod.setMeseFine( getRequestBigDecimalParameter( CAMPO_MESE_DATA_FINE) );
    if( !isRequestParameterNullObj(CAMPO_GIORNO_DATA_FINE))
      lReaMod.setGiornoFine( getRequestBigDecimalParameter( CAMPO_GIORNO_DATA_FINE) );
    
    if(!isRequestParameterNullObj(CAMPO_COD_PERIODO_CONSUMAZIONE))
      lReaMod.setCodPeriodoConsumazione( getRequestStringParameter( CAMPO_COD_PERIODO_CONSUMAZIONE) );
    else
      lReaMod.setCodPeriodoConsumazione( "-" );

    if(!isRequestParameterNullObj(CAMPO_DESC_LUOGO))
      lReaMod.setDescLuogo( getRequestStringParameter( CAMPO_DESC_LUOGO) );
    
    //
    lReaMod.setCodFonte    ( getRequestStringParameter( CAMPO_COD_FONTE) );
    lReaMod.setAnnoFonte   ( getRequestBigDecimalParameter( CAMPO_ANNO_FONTE) );
    lReaMod.setNumeroFonte ( getRequestStringParameter( CAMPO_NUMERO_FONTE) );
    lReaMod.setArticolo    ( getRequestStringParameter( CAMPO_ARTICOLO) );
    lReaMod.setCodSottonumerazione( getRequestStringParameter( CAMPO_COD_SOTTONUMERAZIONE) );
    lReaMod.setComma             ( getRequestStringParameter( CAMPO_COMMA) );
    lReaMod.setCommaQualificante ( getRequestStringParameter( CAMPO_COMMA_QUALIFICANTE) );    
    lReaMod.setLettera           ( getRequestStringParameter( CAMPO_LETTERA) );
    lReaMod.setNumero            ( getRequestStringParameter( CAMPO_NUMERO) );
    
    
    if(!isRequestParameterNullObj(ICostantiReatoCumulo.CAMPO_NOTE))
      lReaMod.setNote( getRequestStringParameter (ICostantiReatoCumulo.CAMPO_NOTE) );

    //FIXME da aprofondire se gestire per singolo record o a livello di reato
    String lFlagStato = getRequestStringParameter(CAMPO_FLAG_STATO);
    if (lFlagStato.equals("E") || lFlagStato.equals("M"))
      lReaMod.setFlagStato ("M");
    else 
      lReaMod.setFlagStato (lFlagStato); // Resta invariato I
    
    if(!isRequestParameterNullObj(ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA))
        lReaMod.setMotivoModificaNote( getRequestStringParameter (ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA) );    

    // Serve per le WHERE CONDITION
    lReaMod.setTitIdTitoloCumulato (getRequestBigDecimalParameter( CAMPO_TIT_ID_TITOLO_CUMULATO));
    
    lReaMod.setCodOperatoreAggiornamento  (this.getCodUtenteConnesso());
    lReaMod.setCodUfficioAggiornamento    (this.getCodUfficioUtenteConnesso());
    lReaMod.setDataAggiornamento          (DateUtils.getSysDate());

    //==========================================================================
    // Chiama il controller
    //==========================================================================
    ReatoCumuloModel mReaModRet;
    IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
    mReaModRet = lCtrl.ExModificaReatoCumulo(lReaMod);

    setRequestAttribute("reato", mReaModRet);
    
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadDettaglioReatoCumulo&"+CAMPO_ID_REATO_CUM+"="+mReaModRet.getIdReatoCum().toString();
     
    return lPage;
  }
}