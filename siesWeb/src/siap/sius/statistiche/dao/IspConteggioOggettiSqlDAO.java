package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.sius.statistiche.model.IspConteggioOggettiModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class IspConteggioOggettiSqlDAO extends SqlDAO {
    
    public IspConteggioOggettiSqlDAO(Connection conn)
    {
        super(conn);
    }
    
    public void ricercaConteggioOggettiAttivi(String aCodUfficio) {

        String lStatement = " SELECT "+  
        						" NUM_DEF_ESITO6, " +
        						" NUM_DEF_ESITO5, " +
        						" NUM_DEF_ESITO4, " +
        						" NUM_DEF_ESITO3, " +
        						" NUM_PENDENTI_FINE, " +
        						" ISP_MOTIVO_OGGETTO.DESC_MOTIVO AS DESC_OGGETTO, " + 
        						" NUM_DEF_ESITO2, " +
        						" NUM_PENDENTI_INIZIO, " +
        						" NUM_DEF_ESITO1, " +
        						" ISP_MOTIVO_OGGETTO.COD_MOTIVO AS COD_OGGETTO, " + 
        						" NUM_UNIFICATI, " +
        						" NUM_DEF_ISC_ERR, " +
        						" NUM_SOPRAVVENUTI, " +
        						" NUM_CANCELLATI, " +
        						" DESC_CONTENUTO_STATIS, " +
        						" NUM_APP_PROVV, " + //MEV_2019-09
        						" NULL FAS_SIU_CHIAVE_UFFICIO " +
        					" FROM " +
        						" ISP_CONTEGGIO_OGGETTI " + 
        						" JOIN ISP_MOTIVO_OGGETTO ON ISP_MOTIVO_OGGETTO.COD_MOTIVO = ISP_CONTEGGIO_OGGETTI.COD_OGGETTO ";
    			lStatement += " WHERE FAS_SIU_CHIAVE_UFFICIO = '" + aCodUfficio + "'" ;

    	
    	setStatement(lStatement);
    }
    

    public void ricercaConteggioOggetti(String aCodUfficio) {

    	String lStatement = 
    			" select nvl(num_def_esito6,0) as num_def_esito6 ," +
		        " nvl(num_def_esito5,0) as num_def_esito5 , " +
		        " nvl(num_def_esito4,0) as num_def_esito4 , " +
		        " nvl(num_def_esito3,0) as num_def_esito3 , " +
		        " nvl(num_pendenti_fine,0) as num_pendenti_fine , " +
		        " isp_motivo_oggetto_selezionati.desc_motivo as desc_oggetto, " +
		        " nvl(num_def_esito2,0) as num_def_esito2 , " +
		        " nvl(num_pendenti_inizio,0) as num_pendenti_inizio , " +
		        " NVL(NUM_DEF_ESITO1,0) as num_def_esito1 , " +
		        " isp_motivo_oggetto_selezionati.cod_motivo as cod_oggetto, " +
		        " nvl(num_unificati,0) as num_unificati , " +
		        " nvl(num_def_isc_err,0) as num_def_isc_err , " +
		        " nvl(num_sopravvenuti,0) as num_sopravvenuti , " +
		        " nvl(num_cancellati,0) as num_cancellati , " +
		        " isp_oggetto_procedimento.isp_cod_oggetto_procedimento desc_contenuto_statis, " +
		        " ISP_CONTEGGIO_OGGETTI.FAS_SIU_CHIAVE_UFFICIO " +
		        " , nvl(NUM_APP_PROVV,0) as NUM_APP_PROVV " + // MEV_2019-09
		   " FROM ISP_MOTIVO_OGGETTO_SELEZIONATI " +
		        " LEFT JOIN ISP_CONTEGGIO_OGGETTI " +
		           " ON ISP_MOTIVO_OGGETTO_SELEZIONATI.COD_MOTIVO = ISP_CONTEGGIO_OGGETTI.COD_OGGETTO " +
		           "	and ISP_CONTEGGIO_OGGETTI.fas_siu_chiave_ufficio = '" + aCodUfficio + "' " +
		        " left join isp_motivo_oggetto " +
		           " ON isp_motivo_oggetto.cod_motivo = isp_conteggio_oggetti.cod_oggetto " +
		           "	and ISP_CONTEGGIO_OGGETTI.fas_siu_chiave_ufficio = '" + aCodUfficio + "' " +
		      " left join isp_oggetto_procedimento " +
		      " on isp_oggetto_procedimento.cod_oggetto_procedimento = isp_motivo_oggetto_selezionati.cod_oggetto ";
    	
    	
    	setStatement(lStatement);
    }

    
    
    
    
    
    public GenericModel getModel() throws DAOException
    {
        IspConteggioOggettiModel aModel = new IspConteggioOggettiModel();

        aModel.setNumDefEsito6(getBigDecimal("NUM_DEF_ESITO6"));
        aModel.setNumDefEsito5(getBigDecimal("NUM_DEF_ESITO5"));
        aModel.setNumDefEsito4(getBigDecimal("NUM_DEF_ESITO4"));
        aModel.setNumDefEsito3(getBigDecimal("NUM_DEF_ESITO3"));
        aModel.setNumPendentiFine(getBigDecimal("NUM_PENDENTI_FINE"));
        aModel.setDescOggetto(getString("DESC_OGGETTO"));
        aModel.setNumDefEsito2(getBigDecimal("NUM_DEF_ESITO2"));
        aModel.setNumPendentiInizio(getBigDecimal("NUM_PENDENTI_INIZIO"));
        aModel.setNumDefEsito1(getBigDecimal("NUM_DEF_ESITO1"));
        aModel.setCodOggetto(getString("COD_OGGETTO"));
        aModel.setNumUnificati(getBigDecimal("NUM_UNIFICATI"));
        aModel.setNumDefIscErr(getBigDecimal("NUM_DEF_ISC_ERR"));
        aModel.setNumSopravvenuti(getBigDecimal("NUM_SOPRAVVENUTI"));
        aModel.setNumCancellati(getBigDecimal("NUM_CANCELLATI"));
        aModel.setDescContenutoStatis(getString("DESC_CONTENUTO_STATIS"));
        aModel.setFasSiuChiaveUfficio(getString("FAS_SIU_CHIAVE_UFFICIO"));
        
        //MEV_2019-09
        aModel.setNumAccoltiProvv(getBigDecimal("NUM_APP_PROVV"));
        

        return aModel;
    }
    
}
