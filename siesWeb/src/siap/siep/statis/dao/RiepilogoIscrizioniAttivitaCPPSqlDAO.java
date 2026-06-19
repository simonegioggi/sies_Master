package siap.siep.statis.dao;

import org.apache.log4j.Logger;
/**
* <p>Title: IspTempiIscrizioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella IspTempiIscrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import siap.siep.statis.model.RiepilogoIscrizioniAttivitaCPPModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class RiepilogoIscrizioniAttivitaCPPSqlDAO extends SqlDAO 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public RiepilogoIscrizioniAttivitaCPPSqlDAO (Connection con) 
  {
      super(con);
  }

  //
  // METODI RICERCA()
  //

  public void ricercaRiepilogoGeneraleIscrizioniAttivita(int aAnno, int aAnnoIni, int aAnnoFin, String aggmmIni, String aggmmFin) throws DAOException
  {
    String lStatement = new String("");
 
   // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   // siesLogger.debug("--XX-- ActCreaRiepilogoIscrizioniAttivitaCPP - aAnno = "+aAnno); 
    
    String data1="";
    String data2="";
    if(aAnno == aAnnoIni)
    {
    	data1=aggmmIni+aAnno;
    	data2="3112"+aAnno;
    }
    else if(aAnno == aAnnoFin)
    {
    	data1="0101"+aAnno;
    	data2=aggmmFin+aAnno;
    }
    else 	//if(aAnno > aAnnoIni && aAnno < aAnnoFin)
    {
    	data1="0101"+aAnno;
    	data2="3112"+aAnno;
    }
    	
   // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   // siesLogger.debug("--XX-- ActCreaRiepilogoIscrizioniAttivitaCPP - Ricerca da "+data1+" a "+data2);
    
    lStatement += "SELECT " + aAnno + " ANNO, ISCRITTI, ERRORI, ATTESA_ESECUZIONE, ATTESA_RISPOSTA, ATTESA_INOLTRO, SENZA_CLASSE_I from " +
            "(SELECT count(*) ISCRITTI FROM ISP_TEMPI_ISCRIZIONE_CPP where ( (COD_MOTIVO_ULT_EVENTO_PM is null OR COD_MOTIVO_ULT_EVENTO_PM != '0353') or (COD_STATO_FASCICOLO is null OR COD_STATO_FASCICOLO != '01') ) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy')) ) A, " +
            "(SELECT count(*) ERRORI FROM ISP_TEMPI_ISCRIZIONE_CPP where COD_MOTIVO_ULT_EVENTO_PM = '0353' and COD_STATO_FASCICOLO = '01' and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy')) ) B, " +
          	"(SELECT count(*) ATTESA_ESECUZIONE FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_SORV is not null and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy')) ) C, " +
            "(SELECT count(*) ATTESA_RISPOSTA 	FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_TRASMISSIONE is not null and ID_EVENTO_PRIMO_ATTO IS not null and ID_EVENTO_SORV is null and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy')) ) D, " +
            "(SELECT count(*) ATTESA_INOLTRO    FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_PRIMO_ATTO is not null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy')) ) E, " + 
            "(SELECT count(*) SENZA_CLASSE_I    FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_PRIMO_ATTO is null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null " +
            		"and (  (COD_MOTIVO_ULT_EVENTO_PM != '0353' or COD_MOTIVO_ULT_EVENTO_PM is null ) or (COD_STATO_FASCICOLO != '01' or COD_STATO_FASCICOLO is null) ) " +
            		"and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy')) ) F "; 


    setStatement(lStatement);
  }
  
  public void ricercaRiepilogoGeneraleIscrizioniAttivita(int aAnno) throws DAOException
  {
    String lStatement = new String("");
   
    lStatement += "SELECT " + aAnno + " ANNO, ISCRITTI, ERRORI, ATTESA_ESECUZIONE, ATTESA_RISPOSTA, ATTESA_INOLTRO, SENZA_CLASSE_I from " +
        "(SELECT count(*) ISCRITTI FROM ISP_TEMPI_ISCRIZIONE_CPP where ( (COD_MOTIVO_ULT_EVENTO_PM is null OR COD_MOTIVO_ULT_EVENTO_PM != '0353') or (COD_STATO_FASCICOLO is null OR COD_STATO_FASCICOLO != '01') ) and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy')) ) A, " +
        "(SELECT count(*) ERRORI FROM ISP_TEMPI_ISCRIZIONE_CPP where COD_MOTIVO_ULT_EVENTO_PM = '0353' and COD_STATO_FASCICOLO = '01' and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy')) ) B, " +
      	"(SELECT count(*) ATTESA_ESECUZIONE FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_SORV is not null and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy')) ) C, " +
        "(SELECT count(*) ATTESA_RISPOSTA 	FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_TRASMISSIONE is not null and ID_EVENTO_PRIMO_ATTO IS not null and ID_EVENTO_SORV is null and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy')) ) D, " +
        "(SELECT count(*) ATTESA_INOLTRO    FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_PRIMO_ATTO is not null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy')) ) E, " + 
        "(SELECT count(*) SENZA_CLASSE_I    FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_PRIMO_ATTO is null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null " +
        		"and (  (COD_MOTIVO_ULT_EVENTO_PM != '0353' or COD_MOTIVO_ULT_EVENTO_PM is null ) or (COD_STATO_FASCICOLO != '01' or COD_STATO_FASCICOLO is null) ) " +
        		"and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy')) ) F "; 

    setStatement(lStatement);
  }
  
  public void ricercaRiepilogoGeneraleIscrizioniAttivitaPerMese(int aAnno, String aggmmIni, String aggmmFin, String aTipo, String aQuale_Trim_Sem) throws DAOException
  {
	  // Parametri	aTipo			 	= Tipo di Ricerca (Semestrale / Trimestrale)
	  //			aQuale_Trim_Sem 	= Quale Semestre o Trimestre (PRIMO, SECONDO, etc...)

    String lStatement = new String("");
        
    String lAnno = Integer.toString(aAnno);
    String data1 = aggmmIni+lAnno ;
    String data2 = aggmmFin+lAnno;
    
    String lPeriodo="";
    lPeriodo = aQuale_Trim_Sem+" "+aTipo.toUpperCase();
    
    lStatement += "SELECT '"+lPeriodo+"' PERIODO, ISCRITTI, ERRORI, ATTESA_ESECUZIONE, ATTESA_RISPOSTA, ATTESA_INOLTRO, SENZA_CLASSE_I from " +
        "(SELECT count(*) ISCRITTI FROM ISP_TEMPI_ISCRIZIONE_CPP where ( (COD_MOTIVO_ULT_EVENTO_PM is null OR COD_MOTIVO_ULT_EVENTO_PM != '0353') or (COD_STATO_FASCICOLO is null OR COD_STATO_FASCICOLO != '01') ) and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') ) ) A, " +
        "(SELECT count(*) ERRORI FROM ISP_TEMPI_ISCRIZIONE_CPP where COD_MOTIVO_ULT_EVENTO_PM = '0353' and COD_STATO_FASCICOLO = '01' and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') ) ) B, " +
      	"(SELECT count(*) ATTESA_ESECUZIONE FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_SORV is not null and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') ) ) C, " +
        "(SELECT count(*) ATTESA_RISPOSTA 	FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_TRASMISSIONE is not null and ID_EVENTO_PRIMO_ATTO IS not null and ID_EVENTO_SORV is null and ( DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') ) ) D, " +
        "(SELECT count(*) ATTESA_INOLTRO    FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_PRIMO_ATTO is not null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null and (  DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') ) ) E, " + 
        "(SELECT count(*) SENZA_CLASSE_I    FROM ISP_TEMPI_ISCRIZIONE_CPP where ID_EVENTO_PRIMO_ATTO is null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null " +
        		"and (  (COD_MOTIVO_ULT_EVENTO_PM != '0353' or COD_MOTIVO_ULT_EVENTO_PM is null ) or (COD_STATO_FASCICOLO != '01' or COD_STATO_FASCICOLO is null) ) " +
        		"and (  DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') ) ) F "; 

    setStatement(lStatement);
  }
  
   //
  // METODO GETMODEL()
  //

  public GenericModel     getModelIscrizioni() throws DAOException
  { 
	  RiepilogoIscrizioniAttivitaCPPModel aModel = new  RiepilogoIscrizioniAttivitaCPPModel(); 

	  aModel.setIscritti(getInteger("ISCRITTI"));
	  aModel.setIscrittiErrore(getInteger("ERRORI"));
	  aModel.setInAttesaEsecuzione(getInteger("ATTESA_ESECUZIONE"));
	  aModel.setInoltroUDSinAttesadiRisposta(getInteger("ATTESA_RISPOSTA"));
	  aModel.setAttesaInoltroUDS(getInteger("ATTESA_INOLTRO"));
	  aModel.setSenzaClasseI(getInteger("SENZA_CLASSE_I"));
	  aModel.setAnno(getInteger("ANNO"));
         
	  return aModel;
  }
  
  public GenericModel  getModelIscrizioniPerMese() throws DAOException
  { 
	  RiepilogoIscrizioniAttivitaCPPModel aModel = new  RiepilogoIscrizioniAttivitaCPPModel(); 

	  aModel.setIscritti(getInteger("ISCRITTI"));
	  aModel.setIscrittiErrore(getInteger("ERRORI"));
	  aModel.setInAttesaEsecuzione(getInteger("ATTESA_ESECUZIONE"));
	  aModel.setInoltroUDSinAttesadiRisposta(getInteger("ATTESA_RISPOSTA"));
	  aModel.setAttesaInoltroUDS(getInteger("ATTESA_INOLTRO"));
	  aModel.setSenzaClasseI(getInteger("SENZA_CLASSE_I"));
	  aModel.setPeriodo(getString("PERIODO"));
         
	  return aModel;
  }
  
}	// CHIUDE RiepilogoIscrizioniAttivitaCPPSqlDAO()

