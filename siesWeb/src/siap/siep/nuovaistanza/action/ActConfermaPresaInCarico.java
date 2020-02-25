package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoModel;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 *
 * <p>Title: ActConfermaPresaInCarico</p>
 * <p>Description: Prende in Carico l'istanza </p>
 */
public class ActConfermaPresaInCarico extends ActionSiap implements ICostantiJMS
{
  public String processRequest() throws Exception
  {
    //Controllo che non si stia lavorando su una entità in modifica ad altri
    LockModel lck =
    lockIfNotLocked("caricoordinanza", getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
    if (lck != null)
    {
      setRequestAttribute (IWebConstants.MESSAGE_TEXT, "La presa in carico di questa istanza è in gestione ad un altro utente! <BR>Riprovare più tardi!");
      return IWebConstants.PG_MESSAGE;
    }
    
    if (   JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE")!=null
        && JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")
       )
    {
      SIAPReceiver.getInstance().testInArrivo();
      SIAPReceiver.getInstance().testInPartenza();
      SIAPReceiver.getInstance().testStampa();
    }
    else{
      SIAPReceiver.getInstance();
    }
      

    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    //DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
    // tabella DI MESSAGGIO.

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

    UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

    //!!!RENDERE DINAMICO!!!!
    String lEsito = "00000"; //Setto l'esito positivo

    MessaggioModel lMessage = new MessaggioModel();
    lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());

    lMessage.setCodBdiMittente(lBDI.getCodDistretto());
    lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setCodTipoMessaggio(ESITO);
    lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_ORDINANZA);
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    
    lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
    lMessage.setDataInvio(DateUtils.getSysDate());
    lMessage.setDataEsito(DateUtils.getSysDate());
    lMessage.setCodEsito(lEsito);
    lMessage.setTreeModel(lMess.getTreeModel());

    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);

		//Elimino il messaggio di richiesta evaso.
		//--------- TEST ----- Per ORa non cancello -------- lCrtl.ExCancellaMessaggio(lIdMess);

    lMess.setFlagVisto("S");
    lMess.setDataEsito(DateUtils.getSysDate());
    lCrtl.ExModificaMessaggio(lMess);

    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Ricezione Istanza Completata e Esito rispedito al Mittente!");

    // devo creare il registro istanza corrente
    //------------------------------------------------------------------------/
    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());
    this.setRequestAttribute("fascicolo", lParser.getFascicolo());
	//preparo il model della sentenza
	SentenzaModel lSenMod = null;
	lSenMod=lParser.getFascicolo().getSentenza();
	
	//preparo il model del soggetto
	SoggettoModel lSogMod = null;
	lSogMod=lParser.getFascicolo().getSoggetto();
	
	//preparo il model del fascicolo
    //data irrevocabilità per ora non sò come gestirla verrà inserita nella modifica??
	  
    UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
    FascicoloSiepModel lFasMod = new FascicoloSiepModel();

    lFasMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); //Anno corrente
    //Il progressivo del fascicolo (in base all'anno e all'ufficio) viene calcolato applicativamente
      
    lFasMod.setChiaveUfficio(lUtenteMod.getUfficioUtente().getCodUfficio()); //Ufficio dell'operatore che inserisce
    
    lFasMod.setCodStatoFascicolo("02"); //Stato fascicolo settato ad aperto
    lFasMod.setCodMotivoArchiviazione("-"); //Motivo di archiviazione '-' per le join
    lFasMod.setCodTipoPosLibero("-"); //Motivo di archiviazione '-' per le join

    lFasMod.setDataIscrizione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
    lFasMod.setFlagValidato("N"); // Il flag di validazione viene impostato a 'NO'
    lFasMod.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della posizione giuridica

    lFasMod.setCodOperatoreInserimento( lUtenteMod.getUserId() );
    lFasMod.setDataInserimento( DateUtils.getSysDate() );
    lFasMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

    lFasMod.setSenIdSentenza(lSenMod.getIdSentenza());
    lFasMod.setTipoProgressivo(9); 


    //Evento
    EventoModel lEveMod = new EventoModel();
    
	lEveMod.setCodTipoEvento("03");        
	lEveMod.setCodTipoProvvedimento("08");
	lEveMod.setCodMotivo("0993");    //da verificare con gis     
	lEveMod.setFlagDocumentoRegistrato("S");
	lEveMod.setFlagVideoSiep("S");
	lEveMod.setFlagStampaSiep("S");
	lEveMod.setFasSieIdFascicoloSiep(null);
	lEveMod.setDataInserimento(DateUtils.getSysDate());  
	lEveMod.setCodOperatoreInserimento(getCodUtenteConnesso());
	lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());  
  // 11/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
	// lEveMod.setDataEmissione(DateUtils.getSysDate());
  lEveMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
	lEveMod.setCodEsito("-");
	lEveMod.setCodMagistrato("-");
	lEveMod.setCodLuogoDestinatario("-");
	lEveMod.setCodTipoUfficioDestinatario("-");
  lEveMod.setCodLuogoEmittente(getCodComuneUtenteConnesso());
  lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());		  
  
  //Istanza
	NuovaIstanzaModel lNuoMod = new NuovaIstanzaModel();
	lNuoMod=lParser.getNuovaIstanza();
    
    //inserisci i model richiesti
    INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
    NuovaIstanzaModel lNuoRetMod = new NuovaIstanzaModel();
    lNuoRetMod=lCtrl.ExInserisciNuovaIstanza(lEveMod,lNuoMod,lSenMod,lSogMod,lFasMod);
    
   /** 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza";
    lPage += "&" + "IdEvento" + "=" + lNuoRetMod.getEveIdEvento().toString();
    
    ///////lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo";
    ///////lPage += "&" + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lNuoRetMod.getFasSieIdFascicoloSiep().toString();
    return lPage;
    */
   //----------------------------------------------------------------------------/


    //Prepara la "pagina" di destinAction
    
    FascicoloSiepModel lNewFasMod = new FascicoloSiepModel();
    IFascicoloSiep lCtrl1 = SIEPLookupRemote.getFascicoloSiepRemote();
    lNewFasMod=lCtrl1.ExRicercaFascicoloByKey(lNuoRetMod.getFasSieIdFascicoloSiep());
    
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction("siap.siep.nuovaistanza.action.ActListaNuoveIstanzeRicevute&FlagConfermaPresaInCarico=s");
   // setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    String retPage = ritornoDopoCancellazione("Presa in carico effettuata. " +
    		"<br> Creato il Registro Istanze n° "+
    		lNewFasMod.getChiaveAnno().toString() + "/" +
    		lNewFasMod.getChiaveProgr().toString(), lRedirigi.toString());


   // return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
   return retPage;
   
   
  }
}
