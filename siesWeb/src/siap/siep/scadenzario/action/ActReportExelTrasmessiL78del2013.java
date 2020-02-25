package siap.siep.scadenzario.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statis.controller.StatisController;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActReportExelTrasmessiL78del2013</p>
* <p>Description: Classe Action per la creazione dei report ricerca Trasmessi legge 78/2013</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActReportExelTrasmessiL78del2013 extends ActionSiap 
                                  implements ICostantiScadenzario, ICostantiFascicoloSiep
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws F3BException 
	{	
		ScadenzarioModel lScaMod = new ScadenzarioModel();
    
		lScaMod.setCodTipoScadenzario("01");
		lScaMod.setCodUfficioInserimento( this.getCodUfficioUtenteConnesso() );
		String uffuteconn=this.getCodUfficioUtenteConnesso();

			// L78/2013

	       if( ! isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE)  )
	    	   lScaMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));
	      
	       if( ! isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE) )
	    	   lScaMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));
	      
	       if( ! isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE) )
	    	   lScaMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));
	      
	       if( ! isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE) )
	    	   lScaMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));
	      
	       if (!isRequestParameterNullObj("GiornoEmissioneIniziale") && !isRequestParameterNullObj("MeseEmissioneIniziale") &&
	         !isRequestParameterNullObj("AnnoEmissioneIniziale") )
	       {
	    	   lScaMod.setDataEmissioneIniziale(getRequestDateParameter( "AnnoEmissioneIniziale", 
	                                                                   "MeseEmissioneIniziale",
	                                                                   "GiornoEmissioneIniziale") );
	       }
       
	       if (!isRequestParameterNullObj("GiornoEmissioneFinale") && !isRequestParameterNullObj("MeseEmissioneFinale") &&
	         !isRequestParameterNullObj("AnnoEmissioneFinale") )
	       {
	    	   lScaMod.setDataEmissioneFinale(getRequestDateParameter( "AnnoEmissioneFinale", 
	                                                                 "MeseEmissioneFinale",
	                                                                 "GiornoEmissioneFinale") );
	       }
       
	       String[] lCodiciStatoNotifica = this.getRequestStringParameters("tipoNotifica");
	       String[] lFiltro = new String[lCodiciStatoNotifica.length];
	       
	       Boolean Attivi = false;
	       if( !isRequestParameterNullObj("AttivoSiNo") )
	       {	
	       		String[] Attivo = this.getRequestStringParameters("AttivoSiNo");
	       		String SelAttivo = Attivo[0];

		       	if( "Attivi".equals(SelAttivo) ) 
		       	{	
		       		Attivi=true;
		       	}
	       
	       }
	       
	       Boolean NOstato = false;
	       if( !isRequestParameterNullObj("NonAttivi") )
	       {	
		       	String[] NAttivo = this.getRequestStringParameters("NonAttivi");
		       	String SelnoAttivo = NAttivo[0];
	
		       	if( "NoAttivi".equals(SelnoAttivo) ) 
		       	{	
		       		NOstato=true;
		       	}
	       
	       }
           
	       for (int i = 0; i < lCodiciStatoNotifica.length; i++)
	       {
		   	      String lSelezione = lCodiciStatoNotifica[i];
		   	      
		   	      if( "Tutti".equals(lSelezione) ) 
		   	      {
		   	    	  	lScaMod.setCodiciStatoNotifica( new String[] {"B", "Q", "O","A", "P", "C"} );
		   	    	  	break;
		   	      }
		   	      else
		   	      {
		   		        if( "ComunicaBis".equals(lSelezione) )
		   		        {
		   		        	lFiltro[i] = "B";
		   		        }
		   		        else if ( "ComunicaQua".equals(lSelezione) )
		   		        {
		   		        	lFiltro[i] = "Q"; 
		   		        }
		   		        else if ( "OrdineEsec".equals(lSelezione) )
		   		        {
		   		        	lFiltro[i] = "O"; 
		   		        }
		   		        else if ( "ComunicazioneArresti".equals(lSelezione) )
				        {
				        	lFiltro[i] = "A"; 
				        }
				        else if ( "ComunicazionePermanenza".equals(lSelezione) )
				        {
				        	lFiltro[i] = "P"; 
				        }
				        else if ( "ComunicazioneCollocamento".equals(lSelezione) )
				        {
				        	lFiltro[i] = "C"; 
				        }
	
		   		        if( i == ( lCodiciStatoNotifica.length - 1 ) )
		   		        {
		   		        	lScaMod.setCodiciStatoNotifica( lFiltro );
		   		        }
		   	      }
	       }

	       HSSFWorkbook wb = new HSSFWorkbook();
	       // creazione del file excel (foglio dettaglio)
	       StatisController lStatisCtrl = new StatisController();
		 
	       lStatisCtrl.ExCreateReportTrasmessiL78del2013(lScaMod, getUfficioUtenteConnesso(), wb, Attivi, NOstato, uffuteconn);
			
	       // Generazione file xls
	       ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
    
	       try 
	       {
	    	   wb.write(fileOut);
	       }
	       catch (IOException ioe)
	       {
	    	   	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    	   	siesLogger.error("IOException: " + ioe);
	    	   	throw new F3BException("siap.siep.scadenzario.action.ActReportExel.processRequest: " + ioe);	    	
	       }
	    	    
	       setRequestAttribute("report", fileOut);
	       setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 
		
	       return IWebConstants.PG_DOWNLOAD_DOCUMENT;

  }	 // Chiude processRequest()
	
}	// Chiude Classe
