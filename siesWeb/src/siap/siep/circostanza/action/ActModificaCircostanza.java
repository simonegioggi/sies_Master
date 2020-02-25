package siap.siep.circostanza.action;


/**
* <p>Title: ActModificaCircostanza</p>
* <p>Description: Classe Action per la modifica di Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaCircostanza extends ActionSiap implements ICostantiCircostanza
{
	protected ICircostanza lCtrl = null;
	
	protected CircostanzaModel lCirMod = null;
	protected boolean flagAgg;
	protected String flagGiudizio = "";
	protected String flagSentenza = "";
	protected String codBil= "";
	protected String noteBil= "";
	
  /**
  * Azione di Modifica del Circostanza
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascicolo = ((FascicoloSiepModel)getSessionAttribute("fascicolo"));
    
    letturaDati(lFascicolo.getIdFascicoloSiep());
     
    CircostanzaModel llCirModRet = lCtrl.ExModificaCircostanza(lCirMod, flagAgg, flagGiudizio, 
  		flagSentenza, codBil, noteBil, lFascicolo.getIdFascicoloSiep(),null);
  
    setRequestAttribute("modalita", "M");
    setRequestAttribute("circostanza", llCirModRet);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.circostanza.action.ActRicercaCircostanza";
    return lPage;
  }
  
  protected void letturaDati(BigDecimal aIdFascicoloSiep) throws Exception
  {
	  // chiama il controller
	  lCtrl = SIEPLookupRemote.getCircostanzaRemote();

	  // riempie il model
	  lCirMod = new CircostanzaModel ();

	  lCirMod = lCtrl.ExRicercaCircostanzaByKey(getRequestBigDecimalParameter( CAMPO_ID_CIRCOSTANZA) );
   
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

	  //***********************************************************************************
	  //Federica - a9-rr-078
	  //aggiunto campo Comma-Qualificante 
	  if (!isRequestParameterNullObj( CAMPO_COMMA_QUALIFICANTE))
		lCirMod.setCommaQualificante( getRequestStringParameter( CAMPO_COMMA_QUALIFICANTE) );

	  //***********************************************************************************
	  if (!isRequestParameterNullObj( CAMPO_LETTERA))
		lCirMod.setLettera( getRequestStringParameter( CAMPO_LETTERA) );

	  if (!isRequestParameterNullObj( CAMPO_NUMERO))
		lCirMod.setNumero( getRequestStringParameter( CAMPO_NUMERO) );

	  if (!isRequestParameterNullObj( CAMPO_ARTICOLO))
		lCirMod.setArticolo( getRequestStringParameter( CAMPO_ARTICOLO) );

	  lCirMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	  lCirMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
	  lCirMod.setDataAggiornamento(DateUtils.getSysDate());
	  lCirMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);
	  // Cod Bilanciamento Circostanze
	  lCirMod.setCodBilanciamentoCircostanze(getRequestStringParameter(CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE));
 	  // Note Bilanciamento
	  lCirMod.setNoteBilanciamento(getRequestStringParameter(CAMPO_NOTE_BILANCIAMENTO));
        
	  //  VALORI CHE DEVONO ESSERE PASSATI PER AGGIORNAMENTO A TAPPETO    
	  flagAgg = false;
  
	  if(!getRequestStringParameter("verifyCampiComuni").equals("0")) 
		  flagAgg=true;
   
	  if(flagAgg){	   

  	if(lCirMod.getCodBilanciamentoCircostanze()!=null)
	    	codBil = lCirMod.getCodBilanciamentoCircostanze();	     
	    if(lCirMod.getNoteBilanciamento()!=null)
	    	noteBil = lCirMod.getNoteBilanciamento();
  }

}
  
  
}