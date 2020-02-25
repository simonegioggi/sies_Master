package siap.sige.provvInterlocutori.controller;


/**
* <p>Title: ProvvInterlocutoriSigeController</p>
* <p>Description: Classe Controller per ProvvInterlocutori</p>
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: </p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.report.ReportGenerator;
import siap.sige.stampa.action.ICostantiStampaSige;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.util.SIGELookupRemote;
import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

public class ProvvInterlocutoriSigeController extends GenericController implements IProvvInterlocutoriSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  /**
   * Esecuzione Stampa Provvedimento Interlocutorio SIGE.
   * <p>
   * @param aIdEvento
   * @param aIdFascicolo
   * @param lTipoUfficio
   * @param aUtenteModel
   * @return ByteArrayOutputStream
   * @throws F3BException
   */
   public ByteArrayOutputStream ExStampaProvvInterlocutorio ( EventoModel lEvento, BigDecimal aIdFascicolo, String lTipoUfficio, UtenteModel aUtenteModel )
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
     int aTipoStampa = ICostantiStampaSige.STAMPA_PROVVINTERLOCUTORI;
     
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
       throw new F3BException("ProvvInterlocutoriSigeController.ExStampaProvvInterlocutorio: " + daoex);
     }
     catch (Exception ex)
     {
       rollback(lConn);
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.debug("SQLException: " + ex);
       throw new F3BException("ProvvInterlocutoriSigeController.ExStampaProvvInterlocutorio: " + ex );
     }
     finally
     {
       cleanup(lEveDao);
       cleanup(lConn);
     }
     
     return lByteArrayOut;
   }

}