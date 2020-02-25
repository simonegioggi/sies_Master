package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.produzioneatti.action.ICostantiProduzioneAtti;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActInserisciEsitoParere
extends ActionSiap implements ICostantiProduzioneAtti
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .processRequest: inizio " );

    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore = getCodUtenteConnesso();
    String lCodiceUfficio   = getCodUfficioUtenteConnesso();
    String lCodComune       = getCodComuneUtenteConnesso();

    BigDecimal lIdEvento  = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

   //Chiama il controller.
   IEvento lCtrl = SICOLookupRemote.getEventoRemote();
   //EventoModel lEve = lCtrl.ExRicercaEventoEsitoParereInamm(lIdEvento);
   
// riempie il model
   EventoModel lEveMod = new EventoModel();
   lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

   //recupero il provvedimento a partire dall'evento
   IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
   ProvvedimentoSigeEventoModel lRetModel = lProvCtrl.ExRicercaProvvedimentoByIdEvento(lEveMod.getIdEvento());
   
   // Data Emissione Parere
   Date lDataEmissione2         = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE2,CAMPO_MESE_DATA_EMISSIONE2,CAMPO_GIORNO_DATA_EMISSIONE2);
   String     lEsitoParere = "Favorevole";
   String    Ck_Parere= getRequestStringParameter( ICostantiProduzioneAtti.CAMPO_ESITO_PARERE );

   if(Ck_Parere.equals("FAVOREVOLE") )
   {
     lEsitoParere = "Favorevole";
   }
   if(Ck_Parere.equals("CONTRARIO") )
   {
     lEsitoParere = "Contrario";
   }
   if(Ck_Parere.equals("PARZIALMENTE FAVOREVOLE") )
   {
     lEsitoParere = "Parzialmente Favorevole";
   }
   if(Ck_Parere.equals("INCOMPETENZA") )
   {
     lEsitoParere = "Incompetenza";
   }
   if(Ck_Parere.equals("-") )
   {
     lEsitoParere = "Non esprime parere";
   }
   if(Ck_Parere.equals("INAMMISSIBILE") )
   {
     lEsitoParere = "Inammissibile";
   }
   if(Ck_Parere.equals("ALTRO") )
   {
     lEsitoParere = "Altro";
   }

    // Preleva dalla sessione il FascicoloGPModel.
   // FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

    // Prepara il model EventoNotifica.
    // Imposta i dati necessari per la gestione dell'evento.
    //AAA**EventoModel lEveNot = new EventoModel();
    //AAA** lEveNot = lEve;
   //AAA** lEveNot.setCodTipoEvento(ICostantiProduzioneAtti.CODTIPOEVENTO);

    lEveMod.setCodTipoEvento(ICostantiProduzioneAtti.CODTIPOEVENTO);
    

    // Vincenzo 19/01/2007 e 09/05/2007
    lEveMod.setIdEvento(lIdEvento);

    if (lEsitoParere.compareTo("Favorevole")==0)
    {
    	lEveMod.setCodEsito("0751");
    }
    else if (lEsitoParere.compareTo("Contrario")==0)
    {
    	lEveMod.setCodEsito("0752");
    }
    else if (lEsitoParere.compareTo("Parzialmente Favorevole")==0)
    {
    	lEveMod.setCodEsito("0753");
    }
    else if (lEsitoParere.compareTo("Incompetenza")==0)
    {
    	lEveMod.setCodEsito("0755");
    }
    else if (lEsitoParere.compareTo("Inammissibile")==0)
    {
    	lEveMod.setCodEsito("0761");
    }
    else if (lEsitoParere.compareTo("Altro")==0)
    {
    	lEveMod.setCodEsito("0762");
    }
    else
    {
    	lEveMod.setCodEsito("0750");
    }
    
    lEveMod.setDataRicezioneAtti(lDataEmissione2);
    
    lEveMod.setCodUfficioEmittente(lCodiceUfficio);
    lEveMod.setCodLuogoEmittente(lCodComune);

    lEveMod.setCodOperatoreAggiornamento(lCodiceOperatore);
    lEveMod.setCodUfficioAggiornamento(lCodiceUfficio);
    lEveMod.setDataAggiornamento(DateUtils.getSysDate());

    // MEV 15 - Revisione SIGE
    String lNote = "";
    if( ! isRequestParameterNullObj( ICostantiProvvedimentoSige.CAMPO_NOTE ) ){
    	lNote = getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_NOTE);
    }

    // Chiamata al Controller
    // Update dell'evento
    IEvento lCtrl2 = SICOLookupRemote.getEventoRemote();

    // se sono presenti delle Motivazioni legate all'esito del parere
    // aggiorno oltre all'evento anche il campo NOTE della tabella PROVVEDIMENTO_SIGE
    if(!lNote.equals("")){
    	lRetModel.getProvvedimento().setNote(lNote);
    	lRetModel.getProvvedimento().setDataAggiornamento(DateUtils.getSysDate());
    	lRetModel.getProvvedimento().setCodOperatoreAggiornamento(lCodiceOperatore);
    	lRetModel.getProvvedimento().setCodUfficioAggiornamento(lCodiceUfficio);
    	lCtrl2.ExModificaEventoProvvedimentoSIGE(lEveMod, lRetModel);
    } else {
    	lCtrl2.ExModificaEvento(lEveMod);
    }

    // Prepara la pagina di destinazione, precisamente punta
    // all'azione di dettaglio.
    RedirectTo lPage = new RedirectTo();
    lPage.setPage(IWebConstants.PG_MAIN);
    lPage.setAction("siap.sige.richiestaatti.action.ActLoadDettaglioEsitoParere");
    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE,""+ lRetModel.getProvvedimento().getIdProvvedimentoSige());
    if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
        lPage.setParameter(IWebConstants.LINK_RITORNO, "10");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .processRequest: inizio " );

    return "" + lPage;
  }
}