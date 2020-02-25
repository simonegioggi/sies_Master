package siap.siep.statis.dao;

/**
* <p>Title: DettaglioTempiIscrizioneProcedimentiCPPSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta parte della tabella la tabella Isp_TempiIscrizione_CPP</p>
* <p>	 SqlDAO  usato per le Query di esrazione dati per la procuzione del Foglio  DETTAGLIO 		</p>
* <p>		Statistiche: Tempi Iscrizione Procedimenti Classe VII ( Conversione Pene Pecuniarie)	</p>
*/

import java.sql.Connection;

import siap.siep.statis.model.DettaglioTempiIscrizioneProcedimentiCPPModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class DettaglioTempiIscrizioneProcedimentiCPPSqlDAO extends SqlDAO 
{

  public DettaglioTempiIscrizioneProcedimentiCPPSqlDAO (Connection con) 
  {
      super(con);
  }

  //
  // METODI RICERCA()
  //

  public void RicercaDettaglioTempiIscrizioneCPP(int aAnno, int Tipo, int aAnnoIni, int aAnnoFin, String aggmmIni, String aggmmFin) throws DAOException
  {
	    String lStatement = new String("");
	    
	    String data1="";
	    String data2="";
	    
	    data1=aggmmIni+aAnnoIni;
	    data2=aggmmFin+aAnnoFin;
	/*    if(aAnno == aAnnoIni)
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
	 */   
	    
	    String TipoDettaglio="";
	    if(Tipo == 1)
	    {
	    	TipoDettaglio="DISTINTA_1";
	    	lStatement += "SELECT DISTINCT(CPP.ID_FASCICOLO_SIEP), " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR," +
	    	    	" CPP.DATA_ARRIVO_ATTO, CPP.DATA_TRASMISSIONE, CPP.DATA_ULT_EVENTO_PM, CPP.DATA_ISCRIZIONE_FASCICOLO, CPP.DATA_IMP_ESAZIONE, CPP.DATA_EVE_SORV," +
	    	    	" CPP.COGNOME_SOGG, CPP.NOME_SOGG," +
	    	    	" CPP.COD_MAGISTRATO, M.COGNOME, M.NOME," +
	    	    	" CPP.TEMPI_ARRIVO_COMIMPESA, CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.TEMPI_TRASM_PROV_SORV " +
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP CPP, MAGISTRATO M" + 
	    	    	" WHERE CPP.TEMPI_ARRIVO_COMIMPESA is not null" +
	    	        " AND (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )"+
	    	    	" AND NVL(CPP.COD_MAGISTRATO,'-') = M.COD_MAGISTRATO" +
	    	        " ORDER BY CPP.TEMPI_ARRIVO_COMIMPESA, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR";

	    }
	    else if(Tipo == 2)
	    {
	    	TipoDettaglio="DISTINTA_2";
	    	lStatement += "SELECT DISTINCT(CPP.ID_FASCICOLO_SIEP), " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR," +
	    			" CPP.DATA_ARRIVO_ATTO, CPP.DATA_TRASMISSIONE, CPP.DATA_ULT_EVENTO_PM, CPP.DATA_ISCRIZIONE_FASCICOLO, CPP.DATA_IMP_ESAZIONE, CPP.DATA_EVE_SORV," +
	    	    	" CPP.COGNOME_SOGG, CPP.NOME_SOGG," +
	    	    	" CPP.COD_MAGISTRATO, M.COGNOME, M.NOME," +
	    	    	" CPP.TEMPI_ARRIVO_COMIMPESA, CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.TEMPI_TRASM_PROV_SORV " +
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP CPP, MAGISTRATO M" +
	    	    	" WHERE CPP.TEMPI_ARRIVO_ISCRIZIONE is not null" +
	    	        " AND (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )"+
	    	    	" AND NVL(CPP.COD_MAGISTRATO,'-') = M.COD_MAGISTRATO" +
	    	        " ORDER BY CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR";

	    }
	    else if(Tipo == 3)
	    {
	    	TipoDettaglio="DISTINTA_3";
	    	lStatement += "SELECT DISTINCT(CPP.ID_FASCICOLO_SIEP), " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR," +
	    			" CPP.DATA_ARRIVO_ATTO, CPP.DATA_TRASMISSIONE, CPP.DATA_ULT_EVENTO_PM, CPP.DATA_ISCRIZIONE_FASCICOLO, CPP.DATA_IMP_ESAZIONE, CPP.DATA_EVE_SORV," +
	    	    	" CPP.COGNOME_SOGG, CPP.NOME_SOGG," +
	    	    	" CPP.COD_MAGISTRATO, M.COGNOME, M.NOME," +
	    	    	" CPP.TEMPI_ARRIVO_COMIMPESA, CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.TEMPI_TRASM_PROV_SORV " +
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP CPP, MAGISTRATO M" +
	    	    	" WHERE CPP.TEMPI_ISCRIZIONE_TRASMISSIONE is not null" +
	    	        " AND (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )"+
	    	    	" AND NVL(CPP.COD_MAGISTRATO,'-') = M.COD_MAGISTRATO" +
	    	        " ORDER BY CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR";

	    }
	    else if(Tipo == 4)
	    {
	    	TipoDettaglio="DISTINTA_4";
	    	lStatement += "SELECT DISTINCT(CPP.ID_FASCICOLO_SIEP), " + aAnno + " ANNO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR," +
	    			" CPP.DATA_ARRIVO_ATTO, CPP.DATA_TRASMISSIONE, CPP.DATA_ULT_EVENTO_PM, CPP.DATA_ISCRIZIONE_FASCICOLO, CPP.DATA_IMP_ESAZIONE, CPP.DATA_EVE_SORV," +
	    	    	" CPP.COGNOME_SOGG, CPP.NOME_SOGG," +
	    	    	" CPP.COD_MAGISTRATO, M.COGNOME, M.NOME," +
	    	    	" CPP.TEMPI_ARRIVO_COMIMPESA, CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.TEMPI_TRASM_PROV_SORV " +
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP CPP, MAGISTRATO M" +
	    	    	" WHERE CPP.TEMPI_TRASM_PROV_SORV is not null" +
	    	        " AND (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )"+
	    	    	" AND NVL(CPP.COD_MAGISTRATO,'-') = M.COD_MAGISTRATO" +
	    	        " ORDER BY CPP.TEMPI_TRASM_PROV_SORV, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR";
	    }

	    setStatement(lStatement);
  }
  
  
  public void RicercaDettaglioTempiIscrizioneCPP_PerMese(int aAnno, int Tipo, String aggmmIni, String aggmmFin, String aTipoMese, String aQuale_Trim_Sem) throws DAOException
  {
	  // Parametri:	Tipo = Tipo Distinta (es: tra Data Arrivo e data Iscrizione, o tra data iscrizione e data decisione etc..));	
	  //			aTipoMese = Tipo di Ricerca (Semestrale / Trimestrale)
	  //			aQuale_Trim_Sem = Quale Semestre o Trimestre (PRIMO, SECONDO, etc...)
	  
	    String lStatement = new String("");
        
	    String lAnno = Integer.toString(aAnno);
	    String data1 = aggmmIni+lAnno;
	    String data2 = aggmmFin+lAnno;
	    
	    String lPeriodo="";
	    lPeriodo = aQuale_Trim_Sem+" "+aTipoMese.toUpperCase();
	    
	    String TipoDettaglio="";
	    if(Tipo == 1)
	    {
	    	TipoDettaglio="DISTINTA_1";
	    	lStatement += "SELECT DISTINCT(CPP.ID_FASCICOLO_SIEP), '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR," +
	    	    	" CPP.DATA_ARRIVO_ATTO, CPP.DATA_TRASMISSIONE, CPP.DATA_ULT_EVENTO_PM, CPP.DATA_ISCRIZIONE_FASCICOLO, CPP.DATA_IMP_ESAZIONE, CPP.DATA_EVE_SORV," +
	    	    	" CPP.COGNOME_SOGG, CPP.NOME_SOGG," +
	    	    	" CPP.COD_MAGISTRATO, M.COGNOME, M.NOME," +
	    	    	" CPP.TEMPI_ARRIVO_COMIMPESA, CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.TEMPI_TRASM_PROV_SORV " +
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP CPP, MAGISTRATO M" +
	    	    	" WHERE CPP.TEMPI_ARRIVO_COMIMPESA is not null" +
	    	        " AND (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )"+
	    	    	" AND NVL(CPP.COD_MAGISTRATO,'-') = M.COD_MAGISTRATO" +
	    	        " ORDER BY CPP.TEMPI_ARRIVO_COMIMPESA, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR";
	    }
	    else if(Tipo == 2)
	    {
	    	TipoDettaglio="DISTINTA_2";
	    	lStatement += "SELECT DISTINCT(CPP.ID_FASCICOLO_SIEP), '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR," +
	    			" CPP.DATA_ARRIVO_ATTO, CPP.DATA_TRASMISSIONE, CPP.DATA_ULT_EVENTO_PM, CPP.DATA_ISCRIZIONE_FASCICOLO, CPP.DATA_IMP_ESAZIONE, CPP.DATA_EVE_SORV," +
	    	    	" CPP.COGNOME_SOGG, CPP.NOME_SOGG," +
	    	    	" CPP.COD_MAGISTRATO, M.COGNOME, M.NOME," +
	    	    	" CPP.TEMPI_ARRIVO_COMIMPESA, CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.TEMPI_TRASM_PROV_SORV " +
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP CPP, MAGISTRATO M" +
	    	    	" WHERE CPP.TEMPI_ARRIVO_ISCRIZIONE is not null" +
	    	        " AND (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )"+
	    	    	" AND NVL(CPP.COD_MAGISTRATO,'-') = M.COD_MAGISTRATO" +
	    	        " ORDER BY CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR";
	    }
	    else if(Tipo == 3)
	    {
	    	TipoDettaglio="DISTINTA_3";
	    	lStatement += "SELECT DISTINCT(CPP.ID_FASCICOLO_SIEP), '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR," +
	    			" CPP.DATA_ARRIVO_ATTO, CPP.DATA_TRASMISSIONE, CPP.DATA_ULT_EVENTO_PM, CPP.DATA_ISCRIZIONE_FASCICOLO, CPP.DATA_IMP_ESAZIONE, CPP.DATA_EVE_SORV," +
	    	    	" CPP.COGNOME_SOGG, CPP.NOME_SOGG," +
	    	    	" CPP.COD_MAGISTRATO, M.COGNOME, M.NOME," +
	    	    	" CPP.TEMPI_ARRIVO_COMIMPESA, CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.TEMPI_TRASM_PROV_SORV " +
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP CPP, MAGISTRATO M" +
	    	    	" WHERE CPP.TEMPI_ISCRIZIONE_TRASMISSIONE is not null" +
	    	        " AND (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )"+
	    	    	" AND NVL(CPP.COD_MAGISTRATO,'-') = M.COD_MAGISTRATO" +
	    	        " ORDER BY CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR";

	    }
	    else if(Tipo == 4)
	    {
	    	TipoDettaglio="DISTINTA_4";
	    	lStatement += "SELECT DISTINCT(CPP.ID_FASCICOLO_SIEP), '"+lPeriodo+"' PERIODO, '" +TipoDettaglio+"' TIPO_DETTAGLIO, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR," +
	    			" CPP.DATA_ARRIVO_ATTO, CPP.DATA_TRASMISSIONE, CPP.DATA_ULT_EVENTO_PM, CPP.DATA_ISCRIZIONE_FASCICOLO, CPP.DATA_IMP_ESAZIONE, CPP.DATA_EVE_SORV," +
	    	    	" CPP.COGNOME_SOGG, CPP.NOME_SOGG," +
	    	    	" CPP.COD_MAGISTRATO, M.COGNOME, M.NOME," +
	    	    	" CPP.TEMPI_ARRIVO_COMIMPESA, CPP.TEMPI_ARRIVO_ISCRIZIONE, CPP.TEMPI_ISCRIZIONE_TRASMISSIONE, CPP.TEMPI_TRASM_PROV_SORV " +
	    	    	" FROM ISP_TEMPI_ISCRIZIONE_CPP CPP, MAGISTRATO M" +
	    	    	" WHERE CPP.TEMPI_TRASM_PROV_SORV is not null" +
	    	        " AND (DATA_ISCRIZIONE_FASCICOLO >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE_FASCICOLO <= to_date('"+data2+"', 'ddmmyyyy') )"+
	    	    	" AND NVL(CPP.COD_MAGISTRATO,'-') = M.COD_MAGISTRATO" +
	    	        " ORDER BY CPP.TEMPI_TRASM_PROV_SORV, CPP.CHIAVE_ANNO, CPP.CHIAVE_PROGR";
	    }
	    
	    setStatement(lStatement);
  
  }	 // CHIUDE RicercaDettaglioTempiIscrizioneCPP_PerMese()
  
  //
  // METODO GETMODEL()
  //

  public GenericModel  getModelDettaglioTempiIscrCPP() throws DAOException
  { 
	  DettaglioTempiIscrizioneProcedimentiCPPModel aModel = new  DettaglioTempiIscrizioneProcedimentiCPPModel(); 

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
  
	  aModel.setCodiceMag(getString("COD_MAGISTRATO"));
	  aModel.setCognomeMag(getString("COGNOME"));
	  aModel.setNomeMag(getString("NOME"));
	  aModel.setTempi_Arrivo_Comu_ImpEsa(getInteger("TEMPI_ARRIVO_COMIMPESA"));
	  aModel.setTempi_Arrivo_Iscrizione(getInteger("TEMPI_ARRIVO_ISCRIZIONE"));
	  aModel.setTempi_Iscrizione_Trasmissione(getInteger("TEMPI_ISCRIZIONE_TRASMISSIONE"));
	  aModel.setTempi_Trasmissione_Provv_Sorv(getInteger("TEMPI_TRASM_PROV_SORV"));
	  
	  aModel.setTipoDettaglio(getString("TIPO_DETTAGLIO"));
	  aModel.setAnno(getInteger("ANNO"));
       
	  return aModel;
  }
  
  public GenericModel getModelDettaglioTempiIscrCPP_PerMese() throws DAOException
  { 
	  DettaglioTempiIscrizioneProcedimentiCPPModel aModel = new  DettaglioTempiIscrizioneProcedimentiCPPModel(); 

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

	  aModel.setCodiceMag(getString("COD_MAGISTRATO"));
	  aModel.setCognomeMag(getString("COGNOME"));
	  aModel.setNomeMag(getString("NOME"));
	  aModel.setTempi_Arrivo_Comu_ImpEsa(getInteger("TEMPI_ARRIVO_COMIMPESA"));
	  aModel.setTempi_Arrivo_Iscrizione(getInteger("TEMPI_ARRIVO_ISCRIZIONE"));
	  aModel.setTempi_Iscrizione_Trasmissione(getInteger("TEMPI_ISCRIZIONE_TRASMISSIONE"));
	  aModel.setTempi_Trasmissione_Provv_Sorv(getInteger("TEMPI_TRASM_PROV_SORV"));
	  
	  aModel.setTipoDettaglio(getString("TIPO_DETTAGLIO"));
	  aModel.setPeriodo(getString("PERIODO"));
       
	  return aModel;
  }
  
 
}