package siap.siep.statis.dao;

/**
* <p>Title: DettaglioIscrizioniAttivitaCPPSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta parte della tabella la tabella Isp_TempiIscrizione_CPP</p>
* <p>	 SqlDAO  usato per le Query di estrazione dati per la produzione del Foglio  di DETTAGLIO 		</p>
* <p>	Statistiche: Riepilogo Iscrizione e Attività Procedimenti Classe VII ( Conversione Pene Pecuniarie)	</p>
*/

import java.sql.Connection;

import siap.siep.statis.model.DettaglioIscrizioniAttivitaCPPModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class DettaglioIscrizioniAttivitaCPPSqlDAO extends SqlDAO 
{

  public DettaglioIscrizioniAttivitaCPPSqlDAO (Connection con) 
  {
      super(con);
  }

  //
  // METODI RICERCA()
  //

  // Al momento NON è Usato; è invece usato il metodo successivo.
  public void ricercaDettagliIscrizioniCPP(int aAnno, int Tipo) throws DAOException
  {
	    String lStatement = new String("");
	    
	    String TipoDettaglio="";
	    
	    if(Tipo == 0)
	    {	
	    	TipoDettaglio="Iscritti";
		    lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
		    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
		    	" COGNOME_SOGG, NOME_SOGG" +	
		    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
		    	" ( (COD_MOTIVO_ULT_EVENTO_PM is null OR COD_MOTIVO_ULT_EVENTO_PM != '0353') or (COD_STATO_FASCICOLO is null OR COD_STATO_FASCICOLO != '01') ) and" + 
		        " (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy') ) ";
	    }
	    else if(Tipo == 1)
	    {
	    	TipoDettaglio="Errori";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" COD_MOTIVO_ULT_EVENTO_PM = '0353' and COD_STATO_FASCICOLO = '01' and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy') ) ";

	    }
	    else if(Tipo == 2)
	    {
	    	TipoDettaglio="Attesa_Esecuzione";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_SORV is not null and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy') ) ";

	    }
	    else if(Tipo == 3)
	    {
	    	TipoDettaglio="Attesa_Risposta";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_TRASMISSIONE is not null and ID_EVENTO_PRIMO_ATTO IS not null and ID_EVENTO_SORV is null and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy') ) ";

	    }
	    else if(Tipo == 4)
	    {
	    	TipoDettaglio="Attesa_Inoltro";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_PRIMO_ATTO is not null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy') ) ";

	    }
	    else if(Tipo == 5)
	    {
	    	TipoDettaglio="Senza_Attivita";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_PRIMO_ATTO is null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null" + 
	    	    	" and (  (COD_MOTIVO_ULT_EVENTO_PM != '0353' or COD_MOTIVO_ULT_EVENTO_PM is null ) or (COD_STATO_FASCICOLO != '01' or COD_STATO_FASCICOLO is null) )" +
	    	        " and (DATA_ISCRIZIONE_FASCICOLO > to_date('3112" + (aAnno-1) + ":235959', 'ddmmyyyy:hh24miss') and DATA_ISCRIZIONE_FASCICOLO < to_date('0101" + (aAnno+1) + "', 'ddmmyyyy') ) ";

	    }
	    
	    setStatement(lStatement);
  }
  
//////////////
  
  public void ricercaDettagliIscrizioniCPP(int aAnno, int Tipo, int aAnnoIni, int aAnnoFin, String aggmmIni, String aggmmFin) throws DAOException
  {
	    String lStatement = new String("");
	    
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
	    
	    String TipoDettaglio="";
	    if(Tipo == 0)
	    {	
	    	TipoDettaglio="Iscritti";
		    lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
		    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
		    	" COGNOME_SOGG, NOME_SOGG" +	
		    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
		    	" ( (COD_MOTIVO_ULT_EVENTO_PM is null OR COD_MOTIVO_ULT_EVENTO_PM != '0353') or (COD_STATO_FASCICOLO is null OR COD_STATO_FASCICOLO != '01') ) and" + 
		        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";
	    }
	    else if(Tipo == 1)
	    {
	    	TipoDettaglio="Errori";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" COD_MOTIVO_ULT_EVENTO_PM = '0353' and COD_STATO_FASCICOLO = '01' and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    else if(Tipo == 2)
	    {
	    	TipoDettaglio="Attesa_Esecuzione";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_SORV is not null and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    else if(Tipo == 3)
	    {
	    	TipoDettaglio="Attesa_Risposta";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_TRASMISSIONE is not null and ID_EVENTO_PRIMO_ATTO IS not null and ID_EVENTO_SORV is null and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    else if(Tipo == 4)
	    {
	    	TipoDettaglio="Attesa_Inoltro";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_PRIMO_ATTO is not null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    else if(Tipo == 5)
	    {
	    	TipoDettaglio="Senza_Attivita";
	    	lStatement += "SELECT " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_PRIMO_ATTO is null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null" + 
	    	    	" and (  (COD_MOTIVO_ULT_EVENTO_PM != '0353' or COD_MOTIVO_ULT_EVENTO_PM is null ) or (COD_STATO_FASCICOLO != '01' or COD_STATO_FASCICOLO is null) )" +
	    	        " and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    
	    lStatement += " ORDER BY CHIAVE_ANNO, CHIAVE_PROGR";
	    setStatement(lStatement);
  }
  
  
  public void ricercaDettagliIscrizioniCPPPerMese(int aAnno, int Tipo, String aggmmIni, String aggmmFin, String aTipoMese, String aQuale_Trim_Sem) throws DAOException
  {
	  // Parametri:	Tipo = Tipo di Elenco (Iscritti, Errori, In Attesa etc...);	
	  //			aTipoMese = Tipo di Ricerca (Semestrale / Trimestrale)
	  //			aQuale_Trim_Sem = Quale Semestre o Trimestre (PRIMO, SECONDO, etc...)
	  
	    String lStatement = new String("");
        
	    String lAnno = Integer.toString(aAnno);
	    String data1 = aggmmIni+lAnno;
	    String data2 = aggmmFin+lAnno;
	    
	    String lPeriodo="";
	    lPeriodo = aQuale_Trim_Sem+" "+aTipoMese.toUpperCase();
	    
	    String TipoDettaglio="";
	    if(Tipo == 0)
	    {	
	    	TipoDettaglio="Iscritti";
		    lStatement += "SELECT '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
		    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
		    	" COGNOME_SOGG, NOME_SOGG" +	
		    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
		    	" ( (COD_MOTIVO_ULT_EVENTO_PM is null OR COD_MOTIVO_ULT_EVENTO_PM != '0353') or (COD_STATO_FASCICOLO is null OR COD_STATO_FASCICOLO != '01') ) and" + 
		        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";
	    }
	    else if(Tipo == 1)
	    {
	    	TipoDettaglio="Errori";
	    	lStatement += "SELECT '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" COD_MOTIVO_ULT_EVENTO_PM = '0353' and COD_STATO_FASCICOLO = '01' and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    else if(Tipo == 2)
	    {
	    	TipoDettaglio="Attesa_Esecuzione";
	    	lStatement += "SELECT '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_SORV is not null and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    else if(Tipo == 3)
	    {
	    	TipoDettaglio="Attesa_Risposta";
	    	lStatement += "SELECT '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_TRASMISSIONE is not null and ID_EVENTO_PRIMO_ATTO IS not null and ID_EVENTO_SORV is null and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    else if(Tipo == 4)
	    {
	    	TipoDettaglio="Attesa_Inoltro";
	    	lStatement += "SELECT '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_PRIMO_ATTO is not null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null and" + 
	    	        " (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    else if(Tipo == 5)
	    {
	    	TipoDettaglio="Senza_Attivita";
	    	lStatement += "SELECT '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, ID_FASCICOLO_SIEP, CHIAVE_ANNO, CHIAVE_PROGR," +
	    	    	" DATA_ARRIVO_ATTO, DATA_TRASMISSIONE, DATA_ULT_EVENTO_PM, DATA_ISCRIZIONE_FASCICOLO, DATA_IMP_ESAZIONE, DATA_EVE_SORV," +
	    	    	" COGNOME_SOGG, NOME_SOGG" +	
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP WHERE"+
	    	    	" ID_EVENTO_PRIMO_ATTO is null and ID_EVENTO_TRASMISSIONE is null and ID_EVENTO_SORV is null" + 
	    	    	" and (  (COD_MOTIVO_ULT_EVENTO_PM != '0353' or COD_MOTIVO_ULT_EVENTO_PM is null ) or (COD_STATO_FASCICOLO != '01' or COD_STATO_FASCICOLO is null) )" +
	    	        " and (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )";

	    }
	    
	    lStatement += " ORDER BY CHIAVE_ANNO, CHIAVE_PROGR";
	    
	    setStatement(lStatement);
  }
  
  //
  // METODO GETMODEL()
  //

  public GenericModel  getModelDettaglioCPP() throws DAOException
  { 
	  DettaglioIscrizioniAttivitaCPPModel aModel = new  DettaglioIscrizioniAttivitaCPPModel(); 

	  aModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
	  aModel.setChiaveAnno(getInteger("CHIAVE_ANNO"));
	  aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
	  aModel.setDataArrivoAttoinCancelleria(getDate("DATA_ARRIVO_ATTO"));
	  aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));
	  aModel.setDataUltEventoPM(getDate("DATA_ULT_EVENTO_PM"));
	  aModel.setDataIscrizione(getDate("DATA_ISCRIZIONE_FASCICOLO"));
	  aModel.setDataImpEsazione(getDate("DATA_IMP_ESAZIONE"));
	  aModel.setDataEventoSorv(getDate("DATA_EVE_SORV"));
	  aModel.setCognome(getString("COGNOME_SOGG"));
	  aModel.setNome(getString("NOME_SOGG"));
	  aModel.setTipoDettaglio(getString("TIPO_DETTAGLIO"));
	  aModel.setAnno(getInteger("ANNO"));
       
	  return aModel;
  }
  
  public GenericModel getModelDettaglioCPPPerMese() throws DAOException
  { 
	  DettaglioIscrizioniAttivitaCPPModel aModel = new  DettaglioIscrizioniAttivitaCPPModel(); 

	  aModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
	  aModel.setChiaveAnno(getInteger("CHIAVE_ANNO"));
	  aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
	  aModel.setDataArrivoAttoinCancelleria(getDate("DATA_ARRIVO_ATTO"));
	  aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));
	  aModel.setDataUltEventoPM(getDate("DATA_ULT_EVENTO_PM"));
	  aModel.setDataIscrizione(getDate("DATA_ISCRIZIONE_FASCICOLO"));
	  aModel.setDataImpEsazione(getDate("DATA_IMP_ESAZIONE"));
	  aModel.setDataEventoSorv(getDate("DATA_EVE_SORV"));
	  aModel.setCognome(getString("COGNOME_SOGG"));
	  aModel.setNome(getString("NOME_SOGG"));
	  aModel.setTipoDettaglio(getString("TIPO_DETTAGLIO"));
	  aModel.setPeriodo(getString("PERIODO"));
       
	  return aModel;
  }
  
 
}