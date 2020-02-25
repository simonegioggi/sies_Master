package siap.siep.statis.dao;

/**
* <p>Title: DettaglioArchiviazioniCPPSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Isp_Provvedimenti_CPP</p>
* <p>	usato per foglio xls DETTAGLIO_ARCHIVIAZIONI 						</p>
* <p>	prodotto per la statistica 'Riepilogo procedimenti pendenti' per 	</p>
* <p>	la Classe VII (Fascicoi di Conversione Pene Pecuniarie)				</p>
*/

import java.sql.Connection;

import siap.siep.statis.model.DettaglioArchiviazioniCPPModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class DettaglioArchiviazioniCPPSqlDAO extends SqlDAO 
{

  public DettaglioArchiviazioniCPPSqlDAO (Connection con) 
  {
    super(con);
  }
  
//07-06-2016 - Riciclo dopo primo collaudo V.10  
  //public void RicercaDettaglioArchiviazioni_CPP(String[]  aCod)   throws DAOException
  public void RicercaDettaglioArchiviazioni_CPP(String[] aCod, String dataIni, String dataFin)   throws DAOException
//07-06-2016 - END Riciclo  
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  " ISP.ID_FASCICOLO_SIEP, "+  
                  " ISP.CHIAVE_ANNO, "+  
                  " ISP.CHIAVE_PROGR, "+  
                  " ISP.DATA_ISCRIZIONE, "+
                  " ISP.DATA_ARCHIVIAZIONE, "+
                  " DATA_INIZIO_SS, "+
                  " DATA_SCADENZA_SS, "+
                  " COD_TIPO_SANZIONE, "+
                  " ULT_COD_MOTIVO, "+  
                  " COGNOME, "+  
                  " NOME, "+  
                  " SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE, "+  
                  " MOTIVO_ARCHIVIAZIONE.RV_MEANING DESCR_MOTIVO_ARCHIVIAZIONE";  
    
    lStatement += " FROM ISP_PROVVEDIMENTI_CPP ISP, STATO_FASCICOLO_RES FRES, SOGGETTO SOG, FASCICOLO_SIEP FAS, " +
    			  " CG_REF_CODES SANZIONE, CG_REF_CODES MOTIVO_ARCHIVIAZIONE";	
    lStatement += " WHERE ";

  //07-06-2016 - Riciclo dopo primo collaudo V.10   
    //lStatement += " " + setCondizioni(aCod);
    lStatement += "ISP.COD_STATO_FASCICOLO_RES = FRES.COD_STATO_FASCICOLO ";
    lStatement +="	AND (ISP.DATA_ARCHIVIAZIONE >= TO_DATE ('"+dataIni+"', 'dd/mm/yyyy') AND ISP.DATA_ARCHIVIAZIONE <= TO_DATE ('"+dataFin+"', 'dd/mm/yyyy') )"; 
  //07-06-2016 - END Riciclo     
    lStatement += " AND SANZIONE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND ISP.COD_TIPO_SANZIONE = SANZIONE.RV_LOW_VALUE" + 
    			  " AND MOTIVO_ARCHIVIAZIONE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND ISP.ULT_COD_MOTIVO = MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE" +
    			  " AND FAS.ID_FASCICOLO_SIEP = ISP.ID_FASCICOLO_SIEP AND SOG.ID_SOGGETTO = FAS.SOG_ID_SOGGETTO ";
    lStatement += " " + setOrder();
      
    setStatement(lStatement);
  }

  public GenericModel getModel() throws DAOException
  {
    DettaglioArchiviazioniCPPModel aModel = new  DettaglioArchiviazioniCPPModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP") ); 
    aModel.setChiaveAnno(getInteger("CHIAVE_ANNO") ); 
    aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR") ); 
    aModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
    aModel.setDataArchiviazione(getDate("DATA_ARCHIVIAZIONE"));
    aModel.setDataInizioSS(getDate("DATA_INIZIO_SS"));
    aModel.setDataScadenzaSS(getDate("DATA_SCADENZA_SS"));
    aModel.setCodTiposanzione(getString("COD_TIPO_SANZIONE") ); 
    aModel.setUltCodMotivo(getString("ULT_COD_MOTIVO") ); 
    aModel.setCognome(getString("COGNOME") ); 
    aModel.setNome(getString("NOME") ); 
    aModel.setDescTipoSanzione(getString("DESCR_TIPO_SANZIONE") );
    aModel.setDescMotivoArch(getString("DESCR_MOTIVO_ARCHIVIAZIONE") );
    
    return aModel;
  }

  public String setCondizioni(String[] aCodStati)
  {
     String lCondizioni = new String(); 
    
     lCondizioni = " ISP.COD_STATO_FASCICOLO_RES in (";
     
     for(int i=0; i<aCodStati.length; i++) {
       lCondizioni += aCodStati[i];
       
       if (i != aCodStati.length - 1)
         lCondizioni += ", ";
     }
     
    lCondizioni += ") AND ISP.COD_STATO_FASCICOLO_RES = FRES.COD_STATO_FASCICOLO ";
         
     return lCondizioni;
   }
   
   public String setOrder()
   {
       String lOrder = new String(); 
       
      // lOrder = " order by ORDINAMENTO, CHIAVE_ANNO, COD_UFFICIO_INSERIMENTO, CHIAVE_PROGR"; 
       lOrder = " order by ISP.DATA_ARCHIVIAZIONE, ISP.CHIAVE_ANNO, ISP.CHIAVE_PROGR";
        
       return lOrder; 
   }
   
}