package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.jms.messaggio.model.MessaggioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action per il caricamento dell'elenco dei Fascicoli Ricevuti x Competenza
 * già presi in carico ma non ancora iscritti in fase Istruttoria 
 * 
 * @author 
 * @since 5.0
 */

public class ActLoadCaricaFascicoliTrasmessi extends ActionModuloCumulo implements ICostantiIstruttoriaCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
    public String processRequest() throws F3BException
    {
        if (this.isSessionAttributeNullObj("fascicolo"))
        {
          return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
        }
        
        FascicoloSiepModel lFascMod = ((FascicoloSiepModel)(getSessionAttribute("fascicolo")));
    
        //==========================================================================
        // Recupero i dati di Istruttoria 
        //==========================================================================    
        super.getDatiIstruttoria();
        
        //==========================================================================
        // Recupero l'elenco dei Fascicoli Ricevuti per competenza 
        // già presi in carico ma non ancora iscritti in istruttoria
        //==========================================================================
        Vector <MessaggioModel> lListaAttiRicevuti = new Vector <MessaggioModel> ();
        IIstruttoriaCumulo lIstrCumCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
        
        lListaAttiRicevuti = lIstrCumCtrl.ExRicercaFascicoliTrasmessi(lFascMod.getChiaveAnno()
                                                                    , lFascMod.getChiaveProgr()
                                                                    , lFascMod.getChiaveUfficio()
                                                                    , "S");		//	Flag_Visto 	= S, prende solo i Procedimenti PRESI in CARICO
        siesLogger.debug("Messaggi Ricevuti size() = "+lListaAttiRicevuti.size());
        // Recupero l'elenco dei fascicoli già in Istruttoria per eliminarli
        // dall'elenco
        BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO) ;
        
        Vector <TitoloCumulatoModel> lListaTitoliInIstruttoria = null;        
        lListaTitoliInIstruttoria = lIstrCumCtrl.ExRicercaTitoliByIstruttoria (lIdIstruttoriaCumulo);
        
       //Vector <FascicoloSiepModel> lListaFascicoliDaIscrivereInIstruttoria = new Vector <FascicoloSiepModel>();
        
        // Escludo dall'elenco quelli già iscritti in istruttoria
        Hashtable <BigDecimal, FascicoloSiepModel> lHashFascicoli = new Hashtable <BigDecimal, FascicoloSiepModel>();

        for (int i=0; i<lListaAttiRicevuti.size();i++)
        {
          MessaggioModel lMessModel = (MessaggioModel) lListaAttiRicevuti.elementAt(i);
          
          // Già preso in carico, Ricerco il fascicolo (Soggetto/Sentenza) a sistema
          IFascicoloSiep lFascCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
          FascicoloSiepModel lFascModelRicerca = new FascicoloSiepModel();
          lFascModelRicerca.setChiaveAnno    (lMessModel.getChiaveAnnoSiep());
          lFascModelRicerca.setChiaveProgr   (lMessModel.getChiaveProgrSiep());
          lFascModelRicerca.setChiaveUfficio (lMessModel.getChiaveUfficioSiep());
          
          siesLogger.debug("Ricerco Fascicolo a sistema: "+lMessModel.getChiaveAnnoSiep()+"/"+lMessModel.getChiaveProgrSiep()+" "+lMessModel.getChiaveUfficioSiep());
          FascicoloSiepModel lFascModel = lFascCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio (lFascModelRicerca);

          if (lFascModel!=null && lFascModel.getIdFascicoloSiep()!=null) 
          {
	            siesLogger.debug("Fascicolo trovato ("+lFascModel.getIdFascicoloSiep()+"). Verifico se in istruttoria...");
	            boolean giaInIstruttoria = false;
	            for (int j=0;j<lListaTitoliInIstruttoria.size();j++)
	            {
		              TitoloCumulatoModel lTitolo = lListaTitoliInIstruttoria.elementAt(j);
		              ProcedimentoCumulatoModel lProcCumulato = lTitolo.getProcedimentoCumulato();
		              
		              if (   lProcCumulato!=null && lProcCumulato.getIdFascicoloSiepOrigine()!=null
		                  && lProcCumulato.getIdFascicoloSiepOrigine().compareTo(lFascModel.getIdFascicoloSiep())==0)
		              {
			                siesLogger.debug("Già in istruttoria");
			                giaInIstruttoria = true;
			                lListaAttiRicevuti.remove(i); // non leggibile, lo rimuovo
			                i--;  
		              }
	            }
            
	            if (!giaInIstruttoria) {
	              siesLogger.debug("Non Ancora in istruttoria");
	              lHashFascicoli.put (lMessModel.getIdMessaggio(), lFascModel);
	              //lListaFascicoliDaIscrivereInIstruttoria.add (lFascModel);
	            }            
          }
          else {
            // Non gestibile
            siesLogger.debug("Fascicolo non trovato a sistema!! ");
            lListaAttiRicevuti.remove(i); // non leggibile, lo rimuovo
            i--;  
          }
        }
        
        // Controllo se gli ATTI sono stati direttamente RIGETTATI senza essere stati presi in carico
        for (int i=0; i<lListaAttiRicevuti.size();i++)
        {
	        MessaggioModel lMessRigModel = (MessaggioModel) lListaAttiRicevuti.elementAt(i);
	        if(lMessRigModel.getCodEsito()!=null && lMessRigModel.getCodEsito().compareTo("01007")==0 )
	        {
	        	siesLogger.debug("Atti Rigettati ");
                lListaAttiRicevuti.remove(i); 
                i--;
	        }
        }    
        
        setRequestAttribute("ListaTitoliInIstruttoria", lListaTitoliInIstruttoria);
        setRequestAttribute("ListaTitoli", lListaAttiRicevuti);
        setRequestAttribute("HashFascicoli", lHashFascicoli);
        
        // Ritorno a questa funzione (Elenco Fascicoli Ricevuti Non ancora Iscritti In Istruttoria)
        // dopo l'eventuale messaggio di restituzione Atti
        setRequestAttribute (IWebConstants.GOTO_PAGE, getClass().getName());
        
        return PG_CARICA_FASCICOLI_TRASMESSI;
    
    } // Chiude processRequest()
    
} // Chiude Classe ActLoadCaricaFascicoliTrasmessi
