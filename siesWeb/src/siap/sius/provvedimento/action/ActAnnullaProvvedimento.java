package siap.sius.provvedimento.action;

import java.math.BigDecimal;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaFoglioComp </p>
 * <p>Description: Effettua l'annullamento logico del Provvedimento e l'inserimento
 * della Motivazione in CAMPO_NOTA.
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActAnnullaProvvedimento  extends ActionSiap
 implements ICostantiProvvedimento
{
  public String processRequest() throws Exception
  {
    String retPage = "";
	  
	// Preleva dalla request la chiave dell'evento come parametro
    BigDecimal lKeyEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO );
    
    // Setta i dati per l'aggiornamento
    CampoNotaModel lCampoMod = new CampoNotaModel();
    lCampoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lCampoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lCampoMod.setDataInserimento(DateUtils.getSysDate());
    lCampoMod.setEveIdEvento(lKeyEvento);
    lCampoMod.setDescr(getRequestStringParameter("motivazioni"));

    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    
    // Verifico se è presente il Foglio Complementare, ed è stato trasmesso,
    // allegato al Provvedimento che si vuole annullare 
    boolean presenzaFC = lCtrlEve.ExRicercaFoglioComplementareTrasmesso(lCampoMod.getEveIdEvento());
    
    // Indica la presenza del FC e la trasmissione dello stesso a NSC
    if(presenzaFC){
	    // Inserimento Motivazioni e Update Evento
    	// Quando il Foglio Complementare è stato trasmesso a NSC 
    	// i campi FLAG_DOCUMENTO_REGISTRATO
    	// delle tabelle EVENTO e DOCUMENTO ALLEGATO vengono
    	// impostati uguali ad 'A' in SiesEsecuzione.
	    lCtrlEve.ExAnnullaEventoInserisciCampoNotaFC(lCampoMod);
    } else {
	    // Inserimento Motivazioni e Update Evento
	    lCtrlEve.ExAnnullaEventoInserisciCampoNota(lCampoMod);
    }

    // Stub 06/03/2006 si consente di attivare con un parametro la prossima azione da eseguire.
    if(!this.isRequestParameterNullObj("nextAction"))
    {
      String lPage;
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
           "=" + this.getRequestStringParameter("nextAction");
      return lPage ;
    }

    // 19/05/2008 Ricerca il Periodo Altra Sanzione tramite l'ID dell'evento
    IPeriodoAltraSanzione lCtrlPAS = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
    PeriodoAltraSanzioneModel mPerAlSanz = lCtrlPAS.ExRicercaSanzioneSostitutivaByIdEvento(lKeyEvento);

    IPeriodoAltraMisura lCtrlPAM = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
    PeriodoAltraMisuraModel mPerAlMisu = lCtrlPAM.ExRicercaMisuraSicurezzaByIdEvento(lKeyEvento);

    //Prepara la "pagina" di destinAction
    // 19/05/2008 Diversificazione del messaggio in caso di presenza Periodo Altra Sanzione o Periodo Altra Misura
    if (mPerAlSanz != null  && mPerAlSanz.getIdPeriodoAltraSanzione() != null ){
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Provvedimento Annullato! <br><br> Attenzione: esistenza di periodo sanzione sostitutiva! <br> Verificare durata sanzione con la specifica funzione!");
    } else if (mPerAlMisu != null  && mPerAlMisu.getIdPeriodoAltraMisura() != null ){
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Provvedimento Annullato! <br><br> Attenzione: esistenza di periodo MISURA SICUREZZA! <br> Verificare durata MISURA con la specifica funzione!");
    } else {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Provvedimento Annullato!");
    }
    
    setRequestAttribute(ICostantiEvento.CAMPO_ID_EVENTO, lKeyEvento);
    // se è presente il Foglio Complementare ed è stato trasmesso
    // viene richiamata una pagina jsp che al caricamento apre una popup 
    // che richiama l'applicazione SiesEsecuzione, la quale provvede 
    // ad annullare il Foglio Complementare su NSC.
    if(presenzaFC){
    	retPage = IWebConstants.PG_MESSAGE_CFC;
    } else {
    	retPage = IWebConstants.PG_MESSAGE;
    }
    
    goToRitorno();

    return retPage;
  }
}
