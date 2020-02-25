package siap.siep.modulocumulo.action;

/**
* <p>Title: ActModificaCircostanzaCumulo</p>
* <p>Description: classe action di Modifica Circostanza_Cumulo (Aggravanti/Attenuati relative al titolo Cumulato) </p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

public class ActModificaCircostanzaCumulo extends ActionModuloCumulo implements ICostantiCircostanzaCumulo
{
	protected ICircostanzaCumulo lCtrl = null;
	
	protected CircostanzaCumuloModel lCirMod = null;
	protected boolean flagAgg;
	protected String flagGiudizio = "";
	protected String flagSentenza = "";
	protected String codBil= "";
	protected String noteBil= "";
	
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws Exception
  {
	BigDecimal lIdTitolo = getRequestBigDecimalParameter (ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
	
    letturaDati(lIdTitolo);
     
    CircostanzaCumuloModel llCirModRet = lCtrl.ExModificaCircostanzaCumulo(lCirMod, flagAgg, flagGiudizio, 
    				flagSentenza, codBil, noteBil, lIdTitolo );
  
    setRequestAttribute("modalita", "M");
    setRequestAttribute("circostanza", llCirModRet);

    String lPage = "";
    //lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaCircostanzaCumulo&" + ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +lIdTitolo;
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaReatoCumulo&" + ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +lIdTitolo;

    return lPage;
  }
  
  protected void letturaDati(BigDecimal aIdTitolo) throws Exception
  {
	  // chiama il controller
	  lCtrl = SIEPLookupRemote.getCircostanzaCumuloRemote();

	  // riempie il model
	  lCirMod = new CircostanzaCumuloModel ();

	  lCirMod = lCtrl.ExRicercaCircostanzaCumuloByKey(getRequestBigDecimalParameter( CAMPO_ID_CIRCOSTANZA_CUMULO) );
	  siesLogger.debug("--XX-- ActModificaCircostanzaCumulo - Circo da modificare = "+lCirMod );
   
	  if (!isRequestParameterNullObj( CAMPO_COD_FONTE))
		lCirMod.setCodFonte( getRequestStringParameter( CAMPO_COD_FONTE) );

	  if (!isRequestParameterNullObj( CAMPO_ANNO_FONTE))
		lCirMod.setAnnoFonte( getRequestBigDecimalParameter( CAMPO_ANNO_FONTE) );

	  if (!isRequestParameterNullObj( CAMPO_NUMERO_FONTE))
		lCirMod.setNumeroFonte( getRequestStringParameter( CAMPO_NUMERO_FONTE) );

	  if (!isRequestParameterNullObj(CAMPO_COD_SOTTONUMERAZIONE))
		lCirMod.setCodSottonumerazione( getRequestStringParameter( CAMPO_COD_SOTTONUMERAZIONE) );

	  if (!isRequestParameterNullObj(CAMPO_COMMA))
		lCirMod.setComma( getRequestStringParameter( CAMPO_COMMA) );

	  if (!isRequestParameterNullObj( CAMPO_COMMA_QUALIFICANTE))
		lCirMod.setCommaQualificante( getRequestStringParameter( CAMPO_COMMA_QUALIFICANTE) );

	  if (!isRequestParameterNullObj( CAMPO_LETTERA))
		lCirMod.setLettera( getRequestStringParameter( CAMPO_LETTERA) );

	  if (!isRequestParameterNullObj( CAMPO_NUMERO))
		lCirMod.setNumero( getRequestStringParameter( CAMPO_NUMERO) );

	  if (!isRequestParameterNullObj( CAMPO_ARTICOLO))
		lCirMod.setArticolo( getRequestStringParameter( CAMPO_ARTICOLO) );

 // Campi con i dati del cumulo
	  String lFlagStato = getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_FLAG_STATO);
	  if (lFlagStato.equals("E") || lFlagStato.equals("M"))
	     lCirMod.setFlagStato ("M");
	  else 
	     lCirMod.setFlagStato (lFlagStato); // Resta invariato I
	    
     if(!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA) )
        lCirMod.setMotivoModifica(getRequestStringParameter (ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA) );

      lCirMod.setTitIdTitoloCumulato(aIdTitolo);
//
      
	  lCirMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	  lCirMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
	  lCirMod.setDataAggiornamento(DateUtils.getSysDate());

	  // Cod Bilanciamento Circostanze
	  lCirMod.setCodBilanciamentoCircostanze(getRequestStringParameter(CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE));
 	  // Note Bilanciamento
	  lCirMod.setNoteBilanciamento(getRequestStringParameter(CAMPO_NOTE_BILANCIAMENTO));
        
	  //  VALORI CHE DEVONO ESSERE PASSATI PER AGGIORNAMENTO A TAPPETO    
	  flagAgg = false;
  
	  if(!getRequestStringParameter("verifyCampiComuni").equals("0")) 
		  flagAgg=true;
   
	  if(flagAgg)	   
	  {	  
		  if(lCirMod.getCodBilanciamentoCircostanze()!=null)
			  codBil = lCirMod.getCodBilanciamentoCircostanze();	     
		  if(lCirMod.getNoteBilanciamento()!=null)
			  noteBil = lCirMod.getNoteBilanciamento();
	  }

  }		// Chiude letturaDati()
  
}