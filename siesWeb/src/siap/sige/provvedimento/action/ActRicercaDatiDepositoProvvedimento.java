package siap.sige.provvedimento.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.siep.notifica.controller.INotifica;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
//import siap.sius.depositodecreto.model.DepositoDecretoModel;
//import siap.sius.stampa.action.ICostantiStampaSius;
//import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

 /**
 * <p>Title: ActRicercaDatiDepositoProvvedimento</p>
 * <p>Description: Classe Action per l'inserimento della Data Deposito</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia S.p.A.</p>
 * @version 1.0
 */

 public class ActRicercaDatiDepositoProvvedimento extends ActionSige
 				implements ICostantiProvvedimentoSige
 {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
   public String mRetPage = PG_LOAD_INSERISCIDATADEPOSITO;

   // Provvedimento
   ProvvedimentoSigeModel mProvMod = null;
   ProvvedimentoSigeEventoModel mProvEveMod = null;

   // Fascicolo
   FascicoloSigeEstesoModel mFasSigeEsteso = null;

   public boolean mIsDepositato = false;

   // Controller
   IStampaSige mStaCtrl = null;
   INotifica mNotCtrll = null;
   IDocumentoAllegato mDocAllCtrl = null;
   IUfficio mUffCtrl = null; // Interfaccia al Controller Ufficio
   IProvvedimentoSige mProvCtrl = null;
   BigDecimal lIdProvvedimento = null;

   public String processRequest() throws Exception
   {
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug( super.getClass().getName()+".processRequest: inizio");

     // Fascicolo Sige Esteso in sessione.
     FascicoloSigeEstesoModel mFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
     if (mFasEsteso == null)
       throw new F3BException(F3BException.USER_MESSAGE,"Dati del Fascicolo SIGE non in sessione !");

     // Ricerca del Provvedimento dall'ID
     lIdProvvedimento = getRequestBigDecimalParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);
     ricercaProvvedimento( lIdProvvedimento);
     
     // Ricerca Avvocati e Luogo Detenzione
     //LuogoDetenzioneModel lLuogoDetMod = leggiAvvocatiLuogoDetenzione();

    // Preleva elenco degli altri destinatari.
    //Option lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);

    // Preleva elenco delle altre autorità giudiziarie.
    //Option lOptionAltreAut = new Option( DecodificheManager.getInstance().getTipoUfficioCumuloRifSiep(), "-");

    // LISTA UFFICI SOGGETTO
    //Option lOptionSog = null;
    //if (lLuogoDetMod != null && lLuogoDetMod.getIstitutoDetenzione() != null &&
    //    lLuogoDetMod.getIstitutoDetenzione().getCodTipoIstituto().length() > 0)
    //  lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), lLuogoDetMod.getIstitutoDetenzione().getCodTipoIstituto(), 75);
    //else
    //  lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);

    // LISTA UFFICI
    //Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
    //String[] lStringFilter = {"-", "22"};
    //Option lOptionAvv = new Option();
    //if (this.isRequestParameterNullObj("Aggiungi"))
    //{
    //  lOptionAvv = new Option(lTipoIstituto, "22", 75);
    //} else {
    //  lOptionAvv = new Option(lTipoIstituto, "-", 75);
    //}
    //lOptionAvv.setFilter(lStringFilter);

    //// Ricerca Notifiche eventualmente già emesse
    //ricercaNotifiche(lIdEvento);

    //// Ricerca Descrizione Comune per il Magistrato di Sorveglianza
    //ricercaComuneMagSorveglianza();

    //// Ricerca dell'elenco dei possibili destinatari
    //Vector lDestinatari = new Vector(DecodificheManager.getInstance().getDestinatarioDeposito());

    //setRequestAttribute("destDeposito", lDestinatari);
    //setRequestAttribute("TipiIstitutiSog", "" + lOptionSog);
    //setRequestAttribute("TipiIstituti1", "" + lOptionAvv);
    //setRequestAttribute("tipoAutorita", lOptionAut.toString());

    //// Notifica ad altre Autorita giudiziarie.
    //setRequestAttribute("altreAutoritaGiudiziarie", lOptionAltreAut.toString());

    //if (!this.isRequestParameterNullObj("Aggiungi"))
    //{
    //  setRequestAttribute("Aggiungi", "yes");
    //} else {
    //	setRequestAttribute("Aggiungi", "no");
    //}

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".processRequest: fine");

    return mRetPage; //restituisce la jsp di VIEW
  }

   /**
    * Effettua la ricerca degli Avvocati e del Luogo di detenzione.
    * Se presenti vengono passati alla request.
    */
/*
   private LuogoDetenzioneModel leggiAvvocatiLuogoDetenzione() throws Exception
   {
     //Preleva dati AVVOCATI e LUOGODETENZIONE
     ParserMessageRec lParser = null;
     if (mStaCtrl == null)
       mStaCtrl = SIUSLookupRemote.getStampaRemote();
       //l'Array contenente le tipologie di dati da prelevare
     int[] aTipoDati =
         {
         ICostantiStampaSius.TREE_LUOGODET,
         ICostantiStampaSius.TREE_AVVOCATOSIUS};
     //Creazione del TreeModel con i dati che occorrono
     if (mFasGPMod == null || mFasGPMod.getFascicoloSiusModel() == null)
       throw new F3BException(F3BException.USER_MESSAGE,
                              "Dati del Fascicolo SIUS non in sessione !");

     TreeModel lTreeDati = mStaCtrl.ExPrelevaDatiVideo(mFasGPMod.
         getFascicoloSiusModel().getIdFascicoloSius(), aTipoDati);
     //Converte i dati ottenuti per utilizzarli come model
     lParser = new ParserMessageRec(lTreeDati);
     LuogoDetenzioneModel lLuogoDetMod = lParser.getLuogoDetenzione();
     setRequestAttribute("luogodet", lLuogoDetMod);
     setRequestAttribute("avvocato", lParser.getAvvocatoSius());
     return lLuogoDetMod;
   }
*/
   /**
    * Effettua la ricerca delle Eventuali Notifiche già emesse per il Decreto Depositato.
    * Se presenti esse vengono passate alla request.
    */
/*
   private void ricercaNotifiche(BigDecimal aIdEvento) throws Exception
   {
     // Flag per indicare la presenza o meno della Notifica al Soggetto
     String lTrovatoSog = "NO";
     // Flag per indicare la presenza o meno delle Notifiche
     String lTrovateNotifiche = "NO";

     if (mIsDepositato)
     {
       // Lettura delle notifiche.
       if (mNotCtrll == null)
         mNotCtrll = SIEPLookupRemote.getNotificaRemote();
       Vector lVect = mNotCtrll.ExRicercaEstesaNotificaByKeyEvento(aIdEvento);
       setRequestAttribute("notifiche", lVect);
       if (lVect.size() > 0)
       {
         lTrovateNotifiche = "SI";
         Iterator itx2 = lVect.iterator();
         while (itx2.hasNext())
         {
           NotificaModel notifica = (NotificaModel) itx2.next();
           if (notifica.getSogIdSoggetto() != null)
           {
             lTrovatoSog = "SI";
           }
         }
       }
     }
     setRequestAttribute("notificheSog", lTrovatoSog);
     setRequestAttribute("NotifichePresenti", lTrovateNotifiche);
   }
*/

   /**
    * Effettua la ricerca della descrizione del Comune del Magistrato di Sorveglianza.
    * La stringa viene passata alla request.
    */
/*
   private void ricercaComuneMagSorveglianza() throws Exception
   {
     String lDescComune = null; // Stringa restituita
     if (mUffCtrl == null)
       mUffCtrl = SICOLookupRemote.getUfficioRemote();
     UfficioModel lUfficio = null;
     if ( mDepDecMod != null && mDepDecMod.getCodUfficioCompetente() != null && mDepDecMod.getCodUfficioCompetente().compareTo("-") != 0)
     {
       lUfficio = mUffCtrl.getUfficioByKey(mDepDecMod.getCodUfficioCompetente());
       lDescComune = lUfficio.getDescrComune();
     }
     setRequestAttribute("lDescMagComp", lDescComune);
   }
*/
   
   /**
    * Passa alla request il codice del tipo di Ufficio dell'utente connesso se UDS o TDS.
    * Passa inoltre la descrizione del comune dell'ufficio.
    */
/*
   private void ricercaSedeProcura() throws Exception
   {
     if (mUffCtrl == null)
       mUffCtrl = SICOLookupRemote.getUfficioRemote();
     String strCodDistretto = getUfficioUtenteConnesso().getCodDistretto();
     String strCodComune = getUfficioUtenteConnesso().getCodComune();
     String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
     String strTipoUfficioRichiesto = new String();

     if (strCodTipoUfficio.equals("TDS"))
     {
       setRequestAttribute("TipoUfficioConnesso", "TDS");
       strTipoUfficioRichiesto = "UDS";
     } else if (strCodTipoUfficio.equals("UDS"))
     {
       setRequestAttribute("TipoUfficioConnesso", "UDS");
       strTipoUfficioRichiesto = "TDS";
     }
     String strCodUfficioRichiesto = (mUffCtrl.getUfficioUDSTDS(strCodDistretto,
         strTipoUfficioRichiesto, strCodComune)).getDescrComune();
     setRequestAttribute("lDescUDSTDS", strCodUfficioRichiesto);

   } */

   // Ricerca del Provvedimento dall'ID
   public void  ricercaProvvedimento(BigDecimal aIdProvvedimento) throws Exception
   {
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug( super.getClass().getName()+".ricercaProvvedimento: inizio");

     if (mProvCtrl == null)
       mProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
     
     mProvEveMod = mProvCtrl.ExRicercaProvvedimentoById(aIdProvvedimento);

     if (mProvEveMod == null)
       throw new F3BException(F3BException.USER_MESSAGE,"Provvedimento non trovato ");
     
     // Se il Provvedimento non è già depositato bisogna lockare la risorsa per evitare che 2 utenti tentino di depositarlo contemporaneamente.
     if(mProvEveMod.getProvvedimento().getChiaveProgr()== null )
     {
    	 // Lock
    	 LockModel lck = LockController.lockIfNotLocked(getServletContext(),"PROVVEDIMENTO_SIGE",mProvEveMod.getProvvedimento().getIdProvvedimentoSige().toString(), getCodUtenteConnesso(),getSession().getId());

    	 if (lck!=null)
    	 {
    		 setRequestAttribute(IWebConstants.MESSAGE_TEXT,   "Il "+lck.getEntity()+" (ID: " + lck.getIdEntity()+ ") è in gestione ad un altro utente!<BR>Riprovare più tardi !" );
    		 mRetPage =  IWebConstants.PG_MESSAGE;
    		 return;
    	 }
     }
     
     if (mProvEveMod.getProvvedimento().getDataDeposito() != null)
     {
       mIsDepositato = true;
         if (isRequestParameterNullObj("Aggiungi"))
         {
           // Se il provvedimento è già stato depositato
           if (mDocAllCtrl == null)
             mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
           DocumentoAllegatoModel lDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(mProvEveMod.getEventoNotifica().getEvento().getIdEvento(), mProvEveMod.getProvvedimento().getCodTipoProvvedimento() );

           // Potrebbe capitare che il documento allegato non sia recuperabile.
           if (lDocAll == null || lDocAll.getIdDocumentoAllegato() == null)
             throw new F3BException(F3BException.EX_NOT_FOUND, "Errore nella lettura del Documento Allegato per IDEvento -> "+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));

           // Si passa al dettaglio
           RedirectTo lPage = new RedirectTo();
           lPage.setPage(IWebConstants.PG_MAIN);
           lPage.setAction( "siap.sige.provvedimento.action.ActLoadDettaglioDataDeposito");
           lPage.setParameter(CAMPO_ID_DOCUMENTO_ALLEGATO, "" + lDocAll.getIdDocumentoAllegato());

           // Passaggio al dettaglio del LINK di ritorno
           if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
             lPage.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO));
           if (!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
            lPage.setParameter(IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO));
           mRetPage = lPage.toString();
         }
         else
           setLinkRitorno();
       }
       else
         setLinkRitorno();

      setRequestAttribute("lProvvedimento", mProvEveMod);
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( super.getClass().getName()+".ricercaProvvedimento: fine");
   }
 }