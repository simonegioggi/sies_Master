package siap.sius.richiestaatti.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.dao.NotificaDAO;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sius.SIUSException;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.motivazionedecreto.dao.MotivazioneDecretoDAO;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>Title: RichiestaAttiController </p>
 * <p>Description: Classe controller di gestione stampa delle Rihieste atti istruttori</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class RichiestaAttiController extends SiapController
implements IRichiestaAtti
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Esecuzione stampa Solleciti
   * <p>
   * @param aModel
   * @return ByteArrayOutputStream
   * @throws F3BException
   */
  public DocumentoAllegatoModel ExInserisciSollecito ( EventoNotificaModel aEveNot ,DocumentoAllegatoModel aDocAllegato,UfficioModel lUfficio,UtenteModel lUtenteMod)
  throws F3BException
  {
    ByteArrayOutputStream lByteArrayOut = null;

    // Preleva i dati per la generazione.
    // TreeModel lTree = this.prelevaDati( aEveNot.getEvento() , lUfficio ,lUtenteMod);

    // Invoca il Report generator.
    // ReportGenerator lReport = new ReportGenerator();

    //Preleva dalla tabella Template il nome del template RTF.
    IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
    TemplateModel lTemplateMod = lEventoCtrl.ExRicercaTemplateByCodMotivo("0518");

    String lNomeTemplate = lTemplateMod.getPathRicerca() + lTemplateMod.getNomeTemplate();

    //  lByteArrayOut = (ByteArrayOutputStream)lReport.generateDocument(lTree,lNomeTemplate);


/////////////////////////////////   Luigi 20-9-2005
     aEveNot.getEvento().setTemIdTemplate(lNomeTemplate);
    // Generazione documento di stampa
    IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
    lByteArrayOut = lCtrlSta.ExPreStampaRichiestaAtti(aEveNot.getEvento(), lUfficio.getCodUfficio(), lUtenteMod);
/////////////////////////////////


    ByteArrayInputStream  lByteArrayInput = new ByteArrayInputStream( lByteArrayOut.toByteArray() );


    // Si imposta il ByteArrayInput ovverro il doc generato nel docallegato
    // precisamente nel attributo DocBlobIn.
    aDocAllegato.setDocBlobIn( lByteArrayInput );

    // Inserisce il documento generato nel model di ritorno
    aDocAllegato.setDocBlobOut(lByteArrayOut);

    //Inserisce il documento allegato
    IDocumentoAllegato lDocCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
    DocumentoAllegatoModel aDocAllegatoRet = lDocCtrl.ExInserisciDocumentoAllegato(aDocAllegato);
    return aDocAllegatoRet;
  }

  /**
   * Metodo di wrapper verso metodo di generaione stampa del controller
   * di stampa.
   * <p>
   * @param aEvento EventoModel contente tutti i dati necessari per la stampa.
   * @param lUfficio UfficioModel contente i dati dell'ufficio.
   * @param lUtenteMod UtenteModel contente i dati dell'utente connesso.
   * @return L'EventoNotificaModel contente oltre i dati Evento e Notifica anche il Blob
   * @throws F3BException propaga errore di eccezione.
   */
  public EventoNotificaModel ExStampaRichiestaAtti ( EventoModel aEvento, UfficioModel lUfficio, UtenteModel lUtenteMod )
  throws F3BException
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .ExStampaRichiestaAtti: inizio " );

    ByteArrayOutputStream lByteArrayOut = null;
    EventoNotificaModel lEveNotifica = null;

    // Preleva i dati per la generazione.
    // TreeModel lTree = this.prelevaDati( aEvento, lUfficio ,lUtenteMod);

    // Invoca il Report generator.
    // ReportGenerator lReport = new ReportGenerator();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info( "######  Valore dell'Template in evento : " + aEvento.getTemIdTemplate() );

    // Se l'id del template non è stato valorizzato, significa che la ricerca avviene attraverso
    // il codice Motivo.
    if( aEvento.getTemIdTemplate() == null )
    {
      IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
      TemplateModel lTemplateMod = lEventoCtrl.ExRicercaTemplateByCodMotivo(aEvento.getCodMotivo());
      // Imposta l'id del template nell'evento
      aEvento.setTemIdTemplate( lTemplateMod.getIdTemplate() );
    }

    // Recupera dalla TemplateManger il path completo del template.
    // Nota : Per conformità, anche se l'informazione desiderata è già disponibile
    // nel TemplateModel, si richiede al TemplateManager il path completo e il nome
    // del template RTF. Questa operazione risulta ridondante ma ci si adegua alla
    // filosofia del TemplateManger.
    String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getTemIdTemplate());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("#### NOME TEMPLATE >>>" + lNomeTemplate );

    /////////////////////////////////   Luigi 20-9-2005
    aEvento.setTemIdTemplate(lNomeTemplate);

    // Generazione documento di stampa
    IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
    lByteArrayOut = lCtrlSta.ExPreStampaRichiestaAtti(aEvento, lUfficio.getCodUfficio(), lUtenteMod);
    /////////////////////////////////

    ByteArrayInputStream  lByteArrayInput = new ByteArrayInputStream( lByteArrayOut.toByteArray() );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info( ">>>>>> Generato il Documento ." );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(">>> EVENTO : " + aEvento.toString() );

    // Si imposta il ByteArrayInput ovverro il doc generato nell'evento
    // precisamente nel attributo DocBlobIn.
    aEvento.setDocBlobIn( lByteArrayInput );

    // Inserisce il documento generato nel model di ritorno
    // In esso inserisce il Nome del template di ritorno
    // e il documento generato.
    lEveNotifica = new EventoNotificaModel();
    // Non viene utilizzato
    //lEveNotifica.setNomeTemplate( lTemplateMod.getNomeTemplate() );
    lEveNotifica.setEvento(aEvento);
    lEveNotifica.getEvento().setDocBlobOut(lByteArrayOut);

    Connection lConn = null;
    EventoDAO lEveDao = null;

    try
    {
      // Preleva connessione dal Db
      lConn = getDBConnection();

      // Prepara un EventoDAO
      lEveDao = new EventoDAO(lConn);
      lEveDao.setDAOFromModelForUpdateBlob(aEvento);

      // Seleziona le condizioni di Update
      lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
      lEveDao.update();

      commit(lConn);
    }
    catch (DAOException daoex)
    {
      rollback(lConn);
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error("DAOException: " + daoex);
      throw new F3BException("RichiestaAttiController.ExStampaRichiestaAtti: Non posso inserire il documento nell'evento : " + daoex);
    }finally{
      cleanup(lEveDao);
      cleanup(lConn);

      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "" + getClass().getName() + " .ExStampaRichiestaAtti: fine " );
    }

    return lEveNotifica;
  }

  /*   Non più utilizzato
   * Preleva dati dai dao ed li organizza gerarchicamente.
   * <p>
   * @throws F3BException

  private TreeModel prelevaDati( EventoModel aEvento, UfficioModel lUfficio ,UtenteModel lUtenteMod)
  throws F3BException
  {
    TreeModel lTreeRoot = new TreeModel();

    AutoritaEsternaSqlDAO lAutoritaSqlDao   = null;
    EventoSqlDAO lEveSqlDao                 = null;
    NotificaEventoSqlDAO lNotEveSqlDao      = null;
    FascicoloGPSqlDAO lFasGPSqlDao          = null;
    UfficioSqlDAO lUfficioSqlDao            = null;
    SoggettoSqlDAO lSogSqlDao               = null;
    ResidenzaSqlDAO lResSqlDao              = null;
    FascicoloSiepSqlDAO lFasSiepSqlDao      = null;
    PosizioneGiuridicaSqlDAO lPosGiuSqlDao  = null;
    LuogoDetenzioneSqlDAO lDetenzioneSqlDao = null;
    CampoNotaSqlDAO lCampoNotaSqlDao        = null;
    SentenzaSqlDAO lSentenzaSqlDao          = null;
    CSSASqlDAO lCSSASqlDao                  = null;

    Connection lConn = null;

    try
    {
      // Get Connection.
      lConn = getDBConnection();

      // Si prelevano le notifiche.
      lNotEveSqlDao = new NotificaEventoSqlDAO( lConn );
      lNotEveSqlDao.ricercaNotificaByEvento( aEvento.getIdEvento() );
      Vector lNotifiche = new Vector( lNotEveSqlDao.getModels() ) ;

      //Fascicolo Generale Procedimento.
      lFasGPSqlDao = new FascicoloGPSqlDAO( lConn );
      lFasGPSqlDao.ricercaFascicoloByKey( aEvento.getFasSiuIdFascicoloSius() );
      FascicoloGPModel lFasGPModel = (FascicoloGPModel)lFasGPSqlDao.getModelByKey();

      // Estrazione del FascicoloSius dal Model GP.
      FascicoloSiusModel lFasSiusModel = lFasGPModel.getFascicoloSiusModel();

      // Estrazione del Generale procedimento model.
      GeneraleProcedimentoModel lGPModel = lFasGPModel.getGeneraleProcedimentoModel();

       // Fascicolo Siep.
      FascicoloSiepModel lFasSiepModel = null;
      // Controlla se l'id nel model sius di siep è valorizzato.
      if( lFasSiusModel.getFasSieIdFascicoloSiep() != null )
      {
        lFasSiepSqlDao = new FascicoloSiepSqlDAO( lConn );
        lFasSiepSqlDao.ricercaFascicoloByKey( lFasSiusModel.getFasSieIdFascicoloSiep() );
        lFasSiepModel = (FascicoloSiepModel)lFasSiepSqlDao.getModelByKey();
      }

      // Sentenza
      SentenzaModel lSentenzaModel = null;
      // Controllo se esiste un fascicolo SIEP.
      if( lFasSiepModel != null )
      {
        lSentenzaSqlDao = new SentenzaSqlDAO( lConn );
        lSentenzaSqlDao.ricercaSentenzaBykey( lFasSiepModel.getSenIdSentenza() );
        lSentenzaModel = (SentenzaModel)lSentenzaSqlDao.getModelByKey();
      }

      // Posizione Giuridica.
      PosizioneGiuridicaModel lPosGiuModel = null;
      if( lFasSiepModel != null )
      {
        lPosGiuSqlDao = new PosizioneGiuridicaSqlDAO( lConn );
        lPosGiuSqlDao.ricercaPosizioneGiuridicaByIdFascicolo( lFasSiusModel.getFasSieIdFascicoloSiep() );
        lPosGiuModel = (PosizioneGiuridicaModel)lPosGiuSqlDao.getModelByKey();
      }

      // Imposta la data di emissione nel model prelevata dal Dbase
      // nel model di passaggio dati EveNot.
      // Questa operazione consente al metodo createRoot di valorizzare
      // nel XModel la data di emissione prelevata dal DB.
      aEvento.setDataEmissione( aEvento.getDataEmissione() );

      // Costruzione albero dei model per la generazione del XML.
      // Creazione radice.
      lTreeRoot = new TreeModel(createRoot(aEvento, lUfficio));


      TreeModel lTreeEvento       = new TreeModel( aEvento );
      TreeModel lTreeFasSiusModel = new TreeModel( lFasSiusModel );
      TreeModel lTreeFasSiepModel = new TreeModel( lFasSiepModel );

      // Inserisce il GPmodel sotto il fasccicolo.
      if( lGPModel != null )
        lTreeFasSiusModel.add( new TreeModel( lGPModel ) );

      // Inserisce il luogo di detenzione sotto il fascicolo SIUS.
       IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
      //Riempi l'Array contenente le tipologie di dati da prelevare
      int[] aTipoDati  =  {ICostantiStampaSius.TREE_LUOGODET};

      //Crea il TreeModel con i dati che occorrono
      lTreeFasSiusModel = lCtrlSta.ExAggiungiDatiStampa(lFasSiusModel.getIdFascicoloSius(),aTipoDati,lTreeFasSiusModel);



      // Inserisce la posizione giuridica sotto il fascicolo SIEP.
      if( lPosGiuModel != null )
        lTreeFasSiepModel.add( new TreeModel( lPosGiuModel ) );

      // Inserisce la sentenza sotto il fascicolo Siep
      if( lSentenzaModel != null )
        lTreeFasSiepModel.add( new TreeModel(lSentenzaModel) );


      // Passa la connessione all'uFFicioSQL Dao.
      lUfficioSqlDao = new UfficioSqlDAO( lConn );
      // Si passa la connessione all'autorita Esterna.
      lAutoritaSqlDao = new AutoritaEsternaSqlDAO( lConn );
      // Passa la connessione al CSSA SqlDAO.
      lCSSASqlDao = new CSSASqlDAO( lConn );

      // Cicla per le notifiche, ed inserisce esse sotto l'evento.
      Iterator itx = lNotifiche.iterator();

      while( itx.hasNext() )
      {
        NotificaModel lNotifica = (NotificaModel)itx.next();

        lUfficioSqlDao.selUfficioByCod( lNotifica.getUffCodUfficio() );
        UfficioModel lUfficioDest = (UfficioModel) lUfficioSqlDao.getModelByKey();
        TreeModel lTreeNotifica = new TreeModel( lNotifica );

        if( lUfficioDest != null )
          lTreeNotifica.add( new TreeModel(lUfficioDest) );

        // Cerca le Autorita Esterne, se vengono trattate
        // le inserisce in gerarchia.
        if( lNotifica.getAutEstIdAutoritaEsterna() != null )
        {
          lAutoritaSqlDao.ricercaAutoritaEsternaByKey( lNotifica.getAutEstIdAutoritaEsterna() );
          AutoritaEsternaModel lAutorita = (AutoritaEsternaModel)lAutoritaSqlDao.getModelByKey();


          if( lAutorita != null )
            lTreeNotifica.add( new TreeModel(lAutorita) );
        }

        // Cerca le sedi CSSA, se vengono trattate
        // le inserisce in gerarchia.
        if( lNotifica.getCssIdCssa() != null )
        {
          lCSSASqlDao.ricercaCSSAByKey( lNotifica.getCssIdCssa() );
          CSSAModel lCSSA = (CSSAModel)lCSSASqlDao.getModelByKey();

          if( lCSSA != null )
            lTreeNotifica.add( new TreeModel(lCSSA) );
        }

        lTreeEvento.add(lTreeNotifica);
      }

      // Si prelevano del note
      lCampoNotaSqlDao = new CampoNotaSqlDAO( lConn );
      lCampoNotaSqlDao.ricercaCampoNotaByKeyEvento( aEvento.getIdEvento() );
      Vector lCampiNote = new Vector( lCampoNotaSqlDao.getModels() );

      Iterator lItx = lCampiNote.iterator();
      // Aggiunge alla gerarchia sotto Evento le note.
      while( lItx.hasNext() )
        lTreeEvento.add(new TreeModel( (CampoNotaModel)lItx.next() ) );

      // Colpo Finale :))
      lTreeRoot.add( new TreeModel( lUtenteMod ));
      lTreeRoot.add( lTreeFasSiusModel );
      // STUB 15/10/2004 Aggiunto ICostantiStampaSius.TREE_RIF_FAS_SIEP.
      int[] aTipoDatiSog  =  {ICostantiStampaSius.TREE_SOGGETTO, ICostantiStampaSius.TREE_RIF_FAS_SIEP};
      lTreeRoot = lCtrlSta.ExAggiungiDatiStampa(lFasSiusModel.getIdFascicoloSius(),aTipoDatiSog,lTreeRoot);
      lTreeRoot.add( lTreeEvento );
      lTreeRoot.add( lTreeFasSiepModel );

    }
    catch( DAOException daoEx )
    {
      throw new F3BException("RichiestaAttiController.prelevaDati: Non posso leggere : " + daoEx);
    }
    catch( SQLException sqlEx )
    {
      throw new F3BException("RichiestaAttiController.prelevaDati: Non posso leggere : " + sqlEx);
    }
    finally
    {
      cleanup(lSogSqlDao);
      cleanup(lFasGPSqlDao);
      cleanup(lEveSqlDao);
      cleanup(lNotEveSqlDao);
      cleanup(lPosGiuSqlDao);
      cleanup(lUfficioSqlDao);
      cleanup(lResSqlDao);
      cleanup(lFasSiepSqlDao);
      cleanup(lSentenzaSqlDao);
      cleanup(lAutoritaSqlDao);
      cleanup(lCSSASqlDao);
      cleanup(lCampoNotaSqlDao);
      cleanup(lDetenzioneSqlDao);

      cleanup(lConn);
    }

    return lTreeRoot;
  }

  private EventoModel getEventoModel( BigDecimal aKeyEvento )
  throws F3BException
  {
    EventoSqlDAO lEveSqlDao = null;
    EventoModel lEvento = null;

    Connection lConn = getDBConnection();

    try
    {

      // Si prevela l'evento .
      lEveSqlDao = new EventoSqlDAO( lConn );
      lEveSqlDao.ricercaEventoByKey( aKeyEvento );
      lEvento = (EventoModel)lEveSqlDao.getModelByKey();

      if( lEvento == null )
        throw new F3BException(F3BException.USER_MESSAGE, "Evento Inesistente" );

    }
    catch( DAOException daoEx )
    {
      throw new F3BException("RichiestaAttiController.getEventoModel: Non posso leggere : " + daoEx);
    }
    catch( SQLException sqlEx )
    {
      throw new F3BException("RichiestaAttiController.getEventoModel: Non posso leggere : " + sqlEx);
    }
    finally
    {
      cleanup(lEveSqlDao);
      cleanup(lConn);
    }

    return lEvento;
  }
*/
  /*  Non più utilizzato
   * Creazione della radice dell'albero.
   * <p>
   * @return il model di radice.

  private XModel createRoot( EventoModel aEvento, UfficioModel lUfficio )
  {
    XModel lXMod = new XModel();

    lXMod.setTipoUfficioT1(aEvento.getDescrUfficioEmittente().toUpperCase());
    lXMod.setUfficio(aEvento.getDescrLuogoEmittente().toUpperCase());
    lXMod.setIndirizzo( lUfficio.getIndirizzo() );
    lXMod.setCap( lUfficio.getCap() );
    lXMod.setFax( lUfficio.getFax() );
    lXMod.setTelefono( lUfficio.getTelefono() );
    lXMod.setDataElaborazione( DateUtils.getSysDate());

    return lXMod;
  }
*/

  /**
 * Esegue la cancellazione ddei dati inerenti una Richiesta Atti, che sono:.
 * <p>
 * Notifica, Campo Note, Documento Allegato (Sollecito), Evento.
 * @param aIdEvento: Chiave Identificativa dell'Evento.
 * @throws Exception
 */

public void ExCancellaRichiestaAtti( BigDecimal aIdEvento )
throws Exception
{
  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug( "" + getClass().getName() + " .ExCancellaRichiestaAtti: inizio " );

  NotificaDAO lNotDAO = null;
  CampoNotaDAO lCampoNotaDao = null;
  DocumentoAllegatoDAO lDocAllDAO = null;
  EventoDAO lEveDao = null;
  MotivazioneDecretoDAO lMotivDecDao = null;
  
  ProvvedimentoSigeDAO provDao = null;
  Connection lConn = null;
  try
  {
    lConn = getDBConnection();

    // cancellazione Notifiche collegate
    lNotDAO = new NotificaDAO(lConn);
    lNotDAO.setCondizioneEvento(aIdEvento);
    lNotDAO.delete();

    // cancellazione Campi Note collegati
    lCampoNotaDao = new CampoNotaDAO(lConn);
    lCampoNotaDao.setCondizioneEvento(aIdEvento);
    lCampoNotaDao.delete();

    // cancellazione Motivazioni Decreti collegati (motivazioni Parere Inammissibilità)
    lMotivDecDao = new MotivazioneDecretoDAO(lConn);
    lMotivDecDao.setCondizioneByEve(aIdEvento);
    lMotivDecDao.delete();

    // cancellazione Documenti Allegati collegati
    lDocAllDAO = new DocumentoAllegatoDAO(lConn);
    lDocAllDAO.setCondizioneByEve(aIdEvento);
    lDocAllDAO.delete();
    
    provDao = new ProvvedimentoSigeDAO(lConn);
    provDao.selCondizioneByIdEvento(aIdEvento);
    provDao.delete();
    
    // cancellazione Evento  collegato
    lEveDao = new EventoDAO(lConn);
    lEveDao.selCondizioneUpdate(aIdEvento);
    lEveDao.delete();

    commit(lConn);

  }
  catch (Exception e)
  {
    rollback(lConn);
    throw new SIUSException("RichiestaAttiController.ExCancellaRichiestaAtti: " + e);
  }
  finally
  {
    cleanup(lNotDAO);
    cleanup(lCampoNotaDao);
    cleanup(lDocAllDAO);
    cleanup(lEveDao);
    cleanup(lConn);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .ExCancellaRichiestaAtti: fine " );

  }
  return;
}



}