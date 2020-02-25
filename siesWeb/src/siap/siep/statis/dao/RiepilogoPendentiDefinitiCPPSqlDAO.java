package siap.siep.statis.dao;

/**
* <p>Title: RiepilogoPendentiDefinitiCPPSqlDAO</p>
* <p>Description: Classe che rappresenta l'output del foglio Xls 'Riepilogo Definiti CPP'	</p>
* <p>	La statistica è 'Riepilogo procedimenti pendenti CPP' (Procedimenti di classe VII) 	</p>
* <p>	I totali raggruppati per anni sono presi dalla Tab. ISP_PROVVEDIMENTI_CPP			</p>
*/

import java.sql.Connection;

import siap.siep.statis.model.RiepilogoIscrizioniAttivitaCPPModel;
import siap.siep.statis.model.RiepilogoPendentiDefinitiCPPModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class RiepilogoPendentiDefinitiCPPSqlDAO extends SqlDAO 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public RiepilogoPendentiDefinitiCPPSqlDAO (Connection con) 
  {
      super(con);
  }

  //
  // METODI RICERCA()
  //

  public void ricercaRiepilogoDefinitiCPP(int aAnno, int aAnnoIni, int aAnnoFin, String aggmmIni, String aggmmFin) throws DAOException
  {
    String lStatement = new String("");
 
   // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   // siesLogger.debug("--XX-- ricercaRiepilogoDefinitiCPP - aAnno = "+aAnno); 
    
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
    	
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.debug("--XX1111-- ricercaRiepilogoDefinitiCPP - Ricerca da "+data1+" a "+data2);
    
    // 07-06-2016 - Riciclo dopo primo collaudo V.10 - La Query ora cerca per DATA_ARCHIVIAZIONE e NON per DATA_ISCRIZIONE. 
    //													Il conteggio di AMNISTIA deve uscire a ZERO e quindi cerca un codice=XXXX
    
/*    lStatement += "SELECT " + aAnno + " ANNO, INDULTO, AMNISTIA, MORTE_REO_GE, DECORSO_TEMPO, DEPENALIZZAZIONE, ALTRO_GE," +	// DEFINITI X PROC GR
    			  " DECLA_LC, RV_LC_PENADET, CONV_LC_PENADET, ALTRO_LC," +			// DEFINITI CONVERSIONE LIB. CONT. IN PENA DET.				
    			  " DECLA_LS, RV_LS_PENADET, CONV_LS_PENADET, ALTRO_LS," +			// DEFINITI CONVERSIONE LAV. SOST. IN PENA DET.	
    			  "	PAGAMENTO, MORTE_REO_NLP, IRREPERIBILITA, SOLVIBILITA, PRESCRIZIONE, CUMULO, ALTRO_NLP," +		// NLP Non Luogo a Proc edere
    			  " ALTRE_DEFINIZIONI from " +
            "(SELECT count(*) INDULTO FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '0417' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) A, " +
            "(SELECT count(*) AMNISTIA FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '0417' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) B, " +
          	"(SELECT count(*) MORTE_REO_GE FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '0412' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) C, " +
            "(SELECT count(*) DECORSO_TEMPO	FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '0411' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) D, " +
            "(SELECT count(*) DEPENALIZZAZIONE FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1780' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) E, " + 
            "(SELECT count(*) ALTRO_GE FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO where " +
            	"DATA_ARCHIVIAZIONE is not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and MOTIVO.RV_HIGH_VALUE = 'DEFI_GE' and " +
            	"NVL(ULT_COD_MOTIVO, 'N') not IN('0411','0412','0417','1780')  and " +
            	"NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157')  and " +
            	"(DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) F, " + 
            	
			"(SELECT count(*) DECLA_LC FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1786' and NVL(COD_NATURA_SANZIONE, 'N') = '0156' and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) G, " +
			"(SELECT count(*) RV_LC_PENADET FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1011' and NVL(COD_NATURA_SANZIONE, 'N') = '0156' and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) H, " +
			"(SELECT count(*) CONV_LC_PENADET FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1012' and NVL(COD_NATURA_SANZIONE, 'N') = '0156' and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) I, " +
			//"(SELECT count(*) ALTRO_LC FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ( ULT_COD_MOTIVO not IN('1011','1012','1786') or ULT_COD_MOTIVO is null ) and COD_NATURA_SANZIONE = '0156' and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) L, " +
			"(SELECT count(*) ALTRO_LC FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO where " +
				"DATA_ARCHIVIAZIONE is not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and MOTIVO.RV_HIGH_VALUE = 'DEFI_SORV' and " +
				"NVL(ULT_COD_MOTIVO, 'N') not IN('1011','1012','1786') and " +
				"NVL(COD_NATURA_SANZIONE, 'N') = '0156' and " +
				"(DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) L, " + 
			
			"(SELECT count(*) DECLA_LS FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1786' and NVL(COD_NATURA_SANZIONE, 'N') = '0157' and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) M, " +
			"(SELECT count(*) RV_LS_PENADET FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1011' and NVL(COD_NATURA_SANZIONE, 'N') = '0157' and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) N, " +
			"(SELECT count(*) CONV_LS_PENADET FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1012' and NVL(COD_NATURA_SANZIONE, 'N') = '0157' and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) O, " +
			//"(SELECT count(*) ALTRO_LS FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ( ULT_COD_MOTIVO not IN('1011','1012','1786') or ULT_COD_MOTIVO is null ) and COD_NATURA_SANZIONE = '0157' and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) P, " +
			"(SELECT count(*) ALTRO_LS FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO where " +
				"DATA_ARCHIVIAZIONE is not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and MOTIVO.RV_HIGH_VALUE = 'DEFI_SORV' and " +
				"NVL(ULT_COD_MOTIVO, 'N') not IN('1011','1012','1786') and " +
				"NVL(COD_NATURA_SANZIONE, 'N') = '0157' and " +
				"(DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) P, " + 

			"(SELECT count(*) PAGAMENTO FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1775' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) Q, " +
			"(SELECT count(*) MORTE_REO_NLP FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1776' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) R, " +
			"(SELECT count(*) IRREPERIBILITA FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1777' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) S, " +
			"(SELECT count(*) SOLVIBILITA	FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1778' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) T, " +
			"(SELECT count(*) PRESCRIZIONE FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1779' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) U, " +
			"(SELECT count(*) CUMULO FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1781' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) V, " + 
			"(SELECT count(*) ALTRO_NLP FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO where " +
				"DATA_ARCHIVIAZIONE is not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and MOTIVO.RV_HIGH_VALUE = 'DEFI_SORV' and " +
				"NVL(ULT_COD_MOTIVO, 'N') not IN('1775','1776','1777','1778','1779','1781') and " +
				"NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and " +
				"(DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) Z, " +
				
			"(SELECT count(*) ALTRE_DEFINIZIONI FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO  where " +
				"DATA_ARCHIVIAZIONE IS not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and NVL(MOTIVO.RV_HIGH_VALUE, 'N') not IN ('DEFI_GE','DEFI_SORV')  and " + 
				"NVL(COD_NATURA_SANZIONE, 'N') not IN ('0156','0157') and " +
				"(DATA_ISCRIZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ISCRIZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) AA ";
 */   
    
    lStatement += "SELECT " + aAnno + " ANNO, INDULTO, AMNISTIA, MORTE_REO_GE, DECORSO_TEMPO, DEPENALIZZAZIONE, ALTRO_GE," +	// DEFINITI X PROC GR
			  " DECLA_LC, RV_LC_PENADET, CONV_LC_PENADET, ALTRO_LC," +			// DEFINITI CONVERSIONE LIB. CONT. IN PENA DET.				
			  " DECLA_LS, RV_LS_PENADET, CONV_LS_PENADET, ALTRO_LS," +			// DEFINITI CONVERSIONE LAV. SOST. IN PENA DET.	
			  "	PAGAMENTO, MORTE_REO_NLP, IRREPERIBILITA, SOLVIBILITA, PRESCRIZIONE, CUMULO, ALTRO_NLP," +		// NLP Non Luogo a Proc edere
			  " ALTRE_DEFINIZIONI from " +
      "(SELECT count(*) INDULTO FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '0417' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) A, " +
      "(SELECT count(*) AMNISTIA FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = 'XXXX' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) B, " +
    	"(SELECT count(*) MORTE_REO_GE FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '0412' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) C, " +
      "(SELECT count(*) DECORSO_TEMPO	FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '0411' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) D, " +
      "(SELECT count(*) DEPENALIZZAZIONE FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1780' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) E, " + 
      "(SELECT count(*) ALTRO_GE FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO where " +
      	"DATA_ARCHIVIAZIONE is not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and MOTIVO.RV_HIGH_VALUE = 'DEFI_GE' and " +
      	"NVL(ULT_COD_MOTIVO, 'N') not IN('0411','0412','0417','1780')  and " +
      	"NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157')  and " +
      	"(DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) F, " + 
      	
		"(SELECT count(*) DECLA_LC FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1786' and NVL(COD_NATURA_SANZIONE, 'N') = '0156' and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) G, " +
		"(SELECT count(*) RV_LC_PENADET FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1011' and NVL(COD_NATURA_SANZIONE, 'N') = '0156' and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) H, " +
		"(SELECT count(*) CONV_LC_PENADET FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1012' and NVL(COD_NATURA_SANZIONE, 'N') = '0156' and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) I, " +
		
		"(SELECT count(*) ALTRO_LC FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO where " +
			"DATA_ARCHIVIAZIONE is not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and MOTIVO.RV_HIGH_VALUE IN('DEFI_SORV','ESPIAZ') and " +
			"NVL(ULT_COD_MOTIVO, 'N') not IN('1011','1012','1786') and " +
			"NVL(COD_NATURA_SANZIONE, 'N') = '0156' and " +
			"(DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) L, " + 
		
		"(SELECT count(*) DECLA_LS FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1786' and NVL(COD_NATURA_SANZIONE, 'N') = '0157' and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) M, " +
		"(SELECT count(*) RV_LS_PENADET FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1011' and NVL(COD_NATURA_SANZIONE, 'N') = '0157' and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) N, " +
		"(SELECT count(*) CONV_LS_PENADET FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1012' and NVL(COD_NATURA_SANZIONE, 'N') = '0157' and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) O, " +
		
		"(SELECT count(*) ALTRO_LS FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO where " +
			"DATA_ARCHIVIAZIONE is not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and MOTIVO.RV_HIGH_VALUE IN('DEFI_SORV','ESPIAZ') and " +
			"NVL(ULT_COD_MOTIVO, 'N') not IN('1011','1012','1786') and " +
			"NVL(COD_NATURA_SANZIONE, 'N') = '0157' and " +
			"(DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) P, " + 

		"(SELECT count(*) PAGAMENTO FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1775' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) Q, " +
		"(SELECT count(*) MORTE_REO_NLP FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1776' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) R, " +
		"(SELECT count(*) IRREPERIBILITA FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1777' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) S, " +
		"(SELECT count(*) SOLVIBILITA	FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1778' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) T, " +
		"(SELECT count(*) PRESCRIZIONE FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1779' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) U, " +
		"(SELECT count(*) CUMULO FROM ISP_PROVVEDIMENTI_CPP where DATA_ARCHIVIAZIONE is not null and ULT_COD_MOTIVO = '1781' and NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and (DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) V, " + 
		"(SELECT count(*) ALTRO_NLP FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO where " +
			"DATA_ARCHIVIAZIONE is not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and MOTIVO.RV_HIGH_VALUE = 'DEFI_SORV' and " +
			"NVL(ULT_COD_MOTIVO, 'N') not IN('1775','1776','1777','1778','1779','1781') and " +
			"NVL(COD_NATURA_SANZIONE, 'N') not IN('0156','0157') and " +
			"(DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) Z, " +
			
		"(SELECT count(*) ALTRE_DEFINIZIONI FROM ISP_PROVVEDIMENTI_CPP, CG_REF_CODES MOTIVO  where " +
			"DATA_ARCHIVIAZIONE IS not null and MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' and MOTIVO.RV_LOW_VALUE = ULT_COD_MOTIVO and NVL(MOTIVO.RV_HIGH_VALUE, 'N') not IN ('DEFI_GE','DEFI_SORV')  and " + 
			"NVL(COD_NATURA_SANZIONE, 'N') not IN ('0156','0157') and " +
			"(DATA_ARCHIVIAZIONE >= to_date('"+data1+"', 'ddmmyyyy') and DATA_ARCHIVIAZIONE <= to_date('"+data2+"', 'ddmmyyyy')) ) AA ";

    // 07-06-2016 - END Riciclo 
    
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

  public GenericModel  getModelPendDefCPP() throws DAOException
  { 
	  RiepilogoPendentiDefinitiCPPModel aModel = new  RiepilogoPendentiDefinitiCPPModel(); 

	  aModel.setDefGEIndulto(getInteger("INDULTO"));
	  aModel.setDefGEAmnistia(getInteger("AMNISTIA"));
	  aModel.setDefGEMorteReo(getInteger("MORTE_REO_GE"));
	  aModel.setDefGE_EstinzioneperDecorsoTempo(getInteger("DECORSO_TEMPO"));
	  aModel.setDefGEDepenalizzazione(getInteger("DEPENALIZZAZIONE"));
	  aModel.setDefGEAltro(getInteger("ALTRO_GE"));

	  aModel.setDefSanSos_Est_libCon(getInteger("DECLA_LC"));
	  aModel.setDefSanSos_RevocaLC_inPenaDet(getInteger("RV_LC_PENADET"));
	  aModel.setDefSanSos_ConvLC_inPenaDet(getInteger("CONV_LC_PENADET"));
	  aModel.setDefSanSos_AltroLC(getInteger("ALTRO_LC"));
	  
	  aModel.setDefSanSos_Est_LavSos(getInteger("DECLA_LS"));
	  aModel.setDefSanSos_RevocaLS_inPenaDet(getInteger("RV_LS_PENADET"));
	  aModel.setDefSanSos_ConvLS_inPenaDet(getInteger("CONV_LS_PENADET"));
	  aModel.setDefSanSos_AltroLS(getInteger("ALTRO_LS"));
	  
	  aModel.setDefNLP_Pagamento(getInteger("PAGAMENTO"));
	  aModel.setDefNLP_MorteReo(getInteger("MORTE_REO_NLP"));
	  aModel.setDefNLP_Irreperibilita(getInteger("IRREPERIBILITA"));
	  aModel.setDefNLP_Solvibilita(getInteger("SOLVIBILITA"));
	  aModel.setDefNLP_Prescrizione(getInteger("PRESCRIZIONE"));
	  aModel.setDefNLP_AssorbimentoCumulo(getInteger("CUMULO"));
	  aModel.setDefNLP_Altro(getInteger("ALTRO_NLP"));
	  
	  aModel.setAltreDefinizioni(getInteger("ALTRE_DEFINIZIONI"));
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
