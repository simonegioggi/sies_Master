package siap.sige.richiestaatti.controller;


/**
* <p>Title: RichiestaAttiController</p>
* <p>Description: Classe Controller per RichiestaAtti</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.motivazioneprovvedimento.dao.MotivazioneProvvedimentoSigeDAO;
import siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.stampa.action.ICostantiStampaSige;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.util.SIGELookupRemote;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;




public class RichiestaAttiSigeController extends GenericController implements IRichiestaAttiSige
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
	  public DocumentoAllegatoModel ExInserisciSollecito ( EventoNotificaModel aEveNot ,DocumentoAllegatoModel aDocAllegato,UfficioModel lUfficio,UtenteModel lUtenteMod,FascicoloSigeEstesoModel lFasEsteso)
	  throws F3BException
	  {
	    

	    //Preleva dalla tabella Template il nome del template RTF.
	    IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
	    //TemplateModel lTemplateMod = lEventoCtrl.ExRicercaTemplateByCodMotivo("0518");
	    TemplateModel lTemplateMod = lEventoCtrl.ExRicercaTemplateByCodMotivo("0703");
	    String lNomeTemplate = lTemplateMod.getPathRicerca() + lTemplateMod.getNomeTemplate();

	     aEveNot.getEvento().setTemIdTemplate(lNomeTemplate);
	  
	     
	    aDocAllegato.setDocBlobIn( null );

	    // Inserisce il documento generato nel model di ritorno
	   // aDocAllegato.setDocBlobOut(lByteArrayOut);
	    aDocAllegato.setDocBlobOut(null);

	    //Inserisce il documento allegato
	    IDocumentoAllegato lDocCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
	    DocumentoAllegatoModel aDocAllegatoRet = lDocCtrl.ExInserisciDocumentoAllegato(aDocAllegato);
	    return aDocAllegatoRet;
	  }


  /**
   * Esecuzione Stampa Richieste Istruttorie SIGE.
   * <p>
   * @param aIdEvento
   * @param aIdFascicolo
   * @param lProvEvento
   * @param lTipoUfficio
   * @param aUtenteModel
   * @return ByteArrayOutputStream
   * @throws F3BException
   */
   public ByteArrayOutputStream ExStampaRichiestaAtti ( EventoModel lEvento, BigDecimal aIdFascicolo, String lTipoUfficio, UtenteModel aUtenteModel )
   throws F3BException
   {
     ByteArrayOutputStream lByteArrayOut = null;

     IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
     //Riempie l'Array contenente le tipologie di dati da prelevare
     int[] aTipoDati  =  {
         ICostantiStampaSige.TREE_SOGGETTO,
         ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO,
         ICostantiStampaSige.TREE_PROVVEDIMENTO,
         ICostantiStampaSige.TREE_FASCICOLOSIEP,
         ICostantiStampaSige.TREE_SENTENZA,
         ICostantiStampaSige.TREE_AVVOCATO,
         ICostantiStampaSige.TREE_LUOGODET,
         ICostantiStampaSige.TREE_MAGISTRATO,
         ICostantiStampaSige.TREEs_PROVVEDIMENTI ,
         ICostantiStampaSige.TREE_TIT_ESE_REF ,
         ICostantiStampaSige.TREE_UDIENZA };
     int aTipoStampa = ICostantiStampaSige.STAMPA_RICHIESTAATTI;
     
     TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa (lEvento.getIdEvento(), aIdFascicolo, aTipoDati, aTipoStampa, lTipoUfficio );

     //ReportGenerator lReport = new ReportGenerator();
     ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

     String lIdTemplate = lEvento.getTemIdTemplate();
  	 String lNomeTemplate = "";
  	 if (lIdTemplate.length()==0)
    	 lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_OR_001");
     else
    	 lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

  	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	 siesLogger.warn("ID TEMPLATENAME >>>" + lIdTemplate.toString());
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
     
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.info("DATI STAMPA -> " + lTree);
	   
     lByteArrayOut = (ByteArrayOutputStream)lReport.generateDocument(lTree,lNomeTemplate);

     ByteArrayInputStream  lByteArrayInput= new ByteArrayInputStream( lByteArrayOut.toByteArray() );

     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug( ">>>>>> Generato il Documento ." );
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug("EVENTO >>> "+ lEvento.toString() );
     
     // Si imposta il ByteArrayInput ovverro il doc generato nell'evento
     // precisamente nel attributo DocBlobIn.
     lEvento.setDocBlobIn( lByteArrayInput );

     // Inserisce il documento generato nel model di ritorno
     // In esso inserisce il Nome del template di ritorno
     // e il documento generato.

     Connection lConn = null;
     EventoDAO lEveDao = null;

     try
     {
       // Preleva connessione dal Db
       lConn = getDBConnection();

       // Prepara un EventoDAO
       lEveDao = new EventoDAO(lConn);
       lEveDao.setDAOFromModelForUpdateBlob(lEvento);

       // Seleziona le condizioni di Update
       lEveDao.selCondizioneUpdate(lEvento.getIdEvento());
       lEveDao.update();

       commit(lConn);
     }
     catch (DAOException daoex)
     {
       rollback(lConn);
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.debug("DAOException: " + daoex);
       throw new F3BException("RichiestaAttiSigeController.ExStampaRichiestaAtti: " + daoex);
     }
     catch (Exception ex)
     {
       rollback(lConn);
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.debug("SQLException: " + ex);
       throw new F3BException("RichiestaAttiSigeController.ExStampaRichiestaAtti: " + ex );
     }
     finally
     {
       cleanup(lEveDao);
       cleanup(lConn);
     }
     
     return lByteArrayOut;
   }
   
   // 01.07.2009 ******************************************************************
   /**
    * Inserisce Evento Notifica, Autorita Esterne associate e eventuali
    * Campi note aggiuntive, Motivazioni Decreto per la "Richiesta Parere".
    * <p>
    * @param EventoNotificaModel, MotivazioneDecretoModel[]
    * @return EventoNotificaModel
    * @throws F3BException
    */
   public ProvvedimentoSigeModel ExInserisciRichiestaParereProv(EventoNotificaModel aEvento, MotivazioneProvvedimentoSigeModel[] aMotivazioniDecreto,ProvvedimentoSigeModel lProvvedimentoSigeModel) throws F3BException
   {
  // Provvedimento inserito
 	  	ProvvedimentoSigeModel lProvvedimento = new ProvvedimentoSigeModel(lProvvedimentoSigeModel);

 	    Connection lConn = null;
 	    ProvvedimentoSigeDAO 	lProvDao	 = null;

 	    try
 	    {
 	    	lConn = getDBConnection();
 	    	lProvDao = new ProvvedimentoSigeDAO(lConn );
 	      
 	    	// Inserimento Evento e Notifiche
 	    	IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
 	    	EventoNotificaModel lEveNot = lEveCtrl.ExInserisciEventoNotifica(aEvento, lConn);

 	    	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 	    	siesLogger.debug("ExInserisciEventoProvvedimento : Inserito Evento -> " + lEveNot.getEvento());
 	   
 	    	lProvvedimento.setIdEventoGenerato(lEveNot.getEvento().getIdEvento());
 	    		       
 	    	// Inserimento Provvedimento.
 	    	lProvDao.setDAOFromModel( lProvvedimento );
 	    	BigDecimal lIdProvvedimento = lProvDao.insert();
 	    	lProvDao.stop();
 	    	lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);
 	    	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 	    	siesLogger.debug("ExInserisciEventoNotificaProv : Inserito Provvedimento -> " + lIdProvvedimento);	  		

	 	    	
 	    	// Eventuale Inserimento dei Motivi Decreti
 	        if(aMotivazioniDecreto != null && aMotivazioniDecreto.length > 0)
 	        {
 	          // Viene valorizzato il campo EVE_ID_EVENTO all'evento appena inserito
 	          for (int i = 0; i < aMotivazioniDecreto.length; i++)
 	          {
 	            aMotivazioniDecreto[i].setEveIdEvento(lProvvedimento.getIdEventoGenerato());
 	            
 	          }
 	         ExInserisciMotivazioniDecreto(aMotivazioniDecreto, lConn);
 	        }
 	    	
 	    	
 	    	//COMMIT
 	    	commit(lConn);
 	    }
    
 	    catch (F3BException fe)
   		{ 
   			rollback(lConn);
   			throw fe;
   		}		 
   		catch (DAOException ex)
   		{ 
   			rollback(lConn);
   			throw new F3BException("ProvvedimentoSigeController.ExInserisciEventoNotificaProv : " + ex);
   		}		 
   		catch (Exception e)
   		{
   			rollback(lConn);
   			throw new F3BException("ProvvedimentoSigeController.ExInserisciEventoNotificaProv -> " + e);
   		}
 	    finally
 	    {
 	    	cleanup(lProvDao);
 	    	cleanup(lConn);
 	    }
 	  	return lProvvedimento;
 	  }
   
   // 01.07.2009 x inserimento delle motivazioni nella tabella Motivazione Provvedimento Sige
   public void ExInserisciMotivazioniDecreto (MotivazioneProvvedimentoSigeModel[] aMotivazioniDecreto, Connection aConn )
   throws Exception
   {
       
       MotivazioneProvvedimentoSigeDAO cc = null;
       cc = new MotivazioneProvvedimentoSigeDAO(aConn);
       
       
       // Effettua l'inserimento delle Motivazioni
       int lSize = aMotivazioniDecreto.length;
       for (int lIndex = 0; lIndex < lSize; lIndex++)
       {
         // Imposta nel model il progressivo motivazione lIndex + 1.
         aMotivazioniDecreto[lIndex].setProgrMotivazione(new BigDecimal( (double)lIndex + 1 ));
         cc.setDAOFromModel(aMotivazioniDecreto[lIndex]);
         cc.insert();
         cc.stop();
       }
       cleanup(cc);
   }
   
   
   
   /*
    * (non-Javadoc)
    * @see siap.sige.richiestaatti.controller.IRichiestaAttiSige#ExStampaSollecitiSige(siap.sico.evento.model.EventoModel, java.math.BigDecimal, java.lang.String, siap.sico.utente.model.UtenteModel)
    */
   public ByteArrayOutputStream ExStampaSollecitiSige ( EventoModel lEvento, BigDecimal aIdFascicolo, String lTipoUfficio, UtenteModel aUtenteModel )
   throws F3BException
   {
     ByteArrayOutputStream lByteArrayOut = null;

     IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
     //Riempie l'Array contenente le tipologie di dati da prelevare
     int[] aTipoDati  =  {
         ICostantiStampaSige.TREE_SOGGETTO,
         ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO,
         ICostantiStampaSige.TREE_PROVVEDIMENTO,
         ICostantiStampaSige.TREE_FASCICOLOSIEP,
         ICostantiStampaSige.TREE_SENTENZA,
         ICostantiStampaSige.TREE_AVVOCATO,
         ICostantiStampaSige.TREE_LUOGODET,
         ICostantiStampaSige.TREE_MAGISTRATO,
         ICostantiStampaSige.TREEs_PROVVEDIMENTI ,
         ICostantiStampaSige.TREE_TIT_ESE_REF ,
         ICostantiStampaSige.TREE_UDIENZA };
     int aTipoStampa = ICostantiStampaSige.STAMPA_RICHIESTAATTI;
     
     TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa (lEvento.getIdEvento(), aIdFascicolo, aTipoDati, aTipoStampa, lTipoUfficio );

     
     ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

     String lIdTemplate = lEvento.getTemIdTemplate();
     
  	 String lNomeTemplate = "";
  	 if (lIdTemplate.length()==0)
    	 lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_IS_021");
     else
    	 lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

  	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	 siesLogger.warn("ID TEMPLATENAME >>>" + lIdTemplate);
     
     lNomeTemplate=lIdTemplate;							
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
     
     lByteArrayOut = (ByteArrayOutputStream)lReport.generateDocument(lTree,lNomeTemplate);

     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug( ">>>>>> Generato il Documento ." );
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug("EVENTO >>> "+ lEvento.toString() );
     
     Connection lConn = null;
     EventoDAO lEveDao = null;

     try
     {
       // Preleva connessione dal Db
       lConn = getDBConnection();

       // Prepara un EventoDAO
       lEveDao = new EventoDAO(lConn);
       lEveDao.setDAOFromModelForUpdateBlob(lEvento);

       // Seleziona le condizioni di Update
       lEveDao.selCondizioneUpdate(lEvento.getIdEvento());
       lEveDao.update();

       commit(lConn);
     }
     catch (DAOException daoex)
     {
       rollback(lConn);
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.debug("DAOException: " + daoex);
       throw new F3BException("RichiestaAttiSigeController.ExStampaRichiestaAtti: " + daoex);
     }
     catch (Exception ex)
     {
       rollback(lConn);
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.debug("SQLException: " + ex);
       throw new F3BException("RichiestaAttiSigeController.ExStampaRichiestaAtti: " + ex );
     }
     finally
     {
       cleanup(lEveDao);
       cleanup(lConn);
     }
     
     return lByteArrayOut;   
   }


   
  





@Override
public DocumentoAllegatoModel ExModificaSollecito(
		DocumentoAllegatoModel aDocAllegato) throws F3BException{
	Connection lConn = null;
	   DocumentoAllegatoDAO docAllDao=null;
	   try {
	       // Preleva connessione dal Db
	       lConn = getDBConnection();

	       // Prepara un EventoDAO
	       docAllDao = new DocumentoAllegatoDAO(lConn);
	       docAllDao.setDAOFromModelForUpdateBlob(aDocAllegato);

	       // Seleziona le condizioni di Update
	       docAllDao.setCondizioneUpdate(aDocAllegato.getIdDocumentoAllegato() );
	       docAllDao.update();

	       commit(lConn);
	     } catch (DAOException daoex)  {
	       rollback(lConn);
	       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	       siesLogger.debug("DAOException: " + daoex);
	       throw new F3BException("RichiestaAttiSigeController.ExStampaRichiestaAtti: " + daoex);
	     } catch (Exception ex){
	       rollback(lConn);
	       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	       siesLogger.debug("SQLException: " + ex);
	       throw new F3BException("RichiestaAttiSigeController.ExStampaRichiestaAtti: " + ex );
	     } finally  {
	       cleanup(docAllDao);
	       cleanup(lConn);
	     }
	   
	   return aDocAllegato;
}
  
}