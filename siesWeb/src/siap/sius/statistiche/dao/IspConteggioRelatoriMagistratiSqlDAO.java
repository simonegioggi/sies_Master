package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sius.statistiche.model.IspConteggioRelatoriMagistratiModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.Utils;

public class IspConteggioRelatoriMagistratiSqlDAO extends SIAPSqlDAO {

	public IspConteggioRelatoriMagistratiSqlDAO(Connection aCon) {
		super(aCon);
	}
	
	public void ricercaConteggioRelatoriMagistrati(RicercaProcedimentoModel aModel) {
        String lStatement = "";
        //String lDataPattern = "yyyyMMdd";
        
        lStatement =  "SELECT "
        				  + " MAG.COD_MAGISTRATO MAG_COD_MAGISTRATO, "
        				  + " MAG.COGNOME MAG_COGNOME, "
        				  + " MAG.NOME MAG_NOME, "
        				  + " NUM_PENDENTI_INIZIO, "
        				  + " NUM_PENDENTI_FINE, "
        				  + " NUM_CANCELLATI, "	
        				  + " NUM_DEF_ESITO1, "
        				  + " NUM_DEF_ESITO2, "
        				  + " NUM_DEF_ESITO3, "
        				  + " NUM_DEF_ESITO4, "
        				  + " NUM_DEF_ESITO5, "
        				  + " NUM_DEF_ESITO6, "
        				  + " NUM_UNIFICATI, "
        				  + " NUM_SOPRAVVENUTI, "
        				  + " NUM_DEF_ISC_ERR,  "
        				  + " FAS_SIU_CHIAVE_UFFICIO "
        			 + " FROM ISP_CONTEGGIO_RELATORI  "         				  
						+ " LEFT JOIN MAGISTRATO MAG "
						+ " ON MAG.COD_MAGISTRATO = ISP_CONTEGGIO_RELATORI.COD_RELATORE " 
        		           + " AND MAG.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficio() + "' "
        		    + "WHERE FAS_SIU_CHIAVE_UFFICIO = '" + aModel.getCodUfficio() + "' ";
        if (aModel.getCodMagistrati() != null) {
        	lStatement += " AND COD_RELATORE IN ( '" + Utils.arrayToString(aModel.getCodMagistrati(), "','") + "')";
        }
        
        /*
        lStatement =  " SELECT "
        		+ " MAG.COD_MAGISTRATO MAG_COD_MAGISTRATO, "
        		+ " MAG.COGNOME MAG_COGNOME, "
        		+ " MAG.NOME MAG_NOME, "
        		+ " ISP_CONTEGGIO_RELATORI.FAS_SIU_CHIAVE_UFFICIO, "
        		+ " SUM(NUM_PENDENTI_INIZIO) AS NUM_PENDENTI_INIZIO, "
        		+ " SUM(NUM_PENDENTI_FINE) AS NUM_PENDENTI_FINE, "
        		+ " SUM(NUM_CANCELLATI) AS NUM_CANCELLATI, "
        		+ " SUM(NUM_DEF_ESITO1) AS NUM_DEF_ESITO1, "
        		+ " SUM(NUM_DEF_ESITO2) AS NUM_DEF_ESITO2, "
        		+ " SUM(NUM_DEF_ESITO3) AS NUM_DEF_ESITO3, "
        		+ " SUM(NUM_DEF_ESITO4) AS NUM_DEF_ESITO4, "
        		+ " SUM(NUM_DEF_ESITO5) AS NUM_DEF_ESITO5, "
        		+ " SUM(NUM_DEF_ESITO6) AS NUM_DEF_ESITO6, "
        		+ " SUM(NUM_UNIFICATI) 	AS NUM_UNIFICATI, "
        		+ " SUM(NUM_SOPRAVVENUTI) AS NUM_SOPRAVVENUTI, "
        		+ " SUM(NUM_DEF_ISC_ERR) AS NUM_DEF_ISC_ERR "
        	+ " FROM ISP_CONTEGGIO_RELATORI  "         				  
			+ " LEFT JOIN MAGISTRATO MAG "
			+ " ON MAG.COD_MAGISTRATO = ISP_CONTEGGIO_RELATORI.COD_RELATORE " 
	           + " AND MAG.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficio() + "' "
	    + "WHERE FAS_SIU_CHIAVE_UFFICIO = '" + aModel.getCodUfficio() + "' ";
        if (aModel.getCodMagistrati() != null) {
        	lStatement += " AND COD_RELATORE IN ( '" + Utils.arrayToString(aModel.getCodMagistrati(), "','") + "')";
        }
        lStatement += " GROUP BY MAG.COD_MAGISTRATO, MAG.COGNOME, MAG.NOME, ISP_CONTEGGIO_RELATORI.FAS_SIU_CHIAVE_UFFICIO ";
        */
        
        
		setStatement(lStatement);
	}
	
	/*
	public GenericModel getModel() throws DAOException {
	     
		
		
		lModel = new EveFasGepSogCancModel();
	    
	    // popola fascicolo sius.
	    
	    
	    lModel.setFascicoloSius(new FascicoloSiusModel());
	    lModel.getFascicoloSius().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
	    lModel.getFascicoloSius().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
        lModel.getFascicoloSius().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
        lModel.getFascicoloSius().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
        lModel.getFascicoloSius().setDataDefinizione(getDate("DATA_DEFINIZIONE"));
        // popola fascicolo sius -> Soggetto.
        lModel.getFascicoloSius().setSoggetto(new SoggettoModel());
        lModel.getFascicoloSius().getSoggetto().setCognome(getString("COGNOME"));
        lModel.getFascicoloSius().getSoggetto().setNome(getString("NOME"));
        // popola generale procedimento.
        lModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
        lModel.getGeneraleProcedimento().setDataCameraConsiglio(getDate("DATA_UDIENZA"));
        lModel.getGeneraleProcedimento().setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
        lModel.getGeneraleProcedimento().setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));
        // popola evento.        
        lModel.setEvento(new EventoModel());
        lModel.getEvento().setDataEmissione(getDate("DATA_EMISSIONE"));
        // popola documento allegato.
        lModel.setDocumentoAllegato(new DocumentoAllegatoModel());
        lModel.getDocumentoAllegato().setDataEmissione(getDate("DATA_DEPOSITO"));

	    return lModel;
	    */
	
	public GenericModel getModel() throws DAOException { 
		IspConteggioRelatoriMagistratiModel lModel = new IspConteggioRelatoriMagistratiModel();

		lModel.setNumPendentiInizio(getBigDecimal("NUM_PENDENTI_INIZIO"));
		lModel.setNumSopravvenuti(getBigDecimal("NUM_SOPRAVVENUTI"));
		lModel.setNumDefEsito1(getBigDecimal("NUM_DEF_ESITO1"));
		lModel.setNumDefEsito2(getBigDecimal("NUM_DEF_ESITO2"));
		lModel.setNumDefEsito3(getBigDecimal("NUM_DEF_ESITO3"));
		lModel.setNumDefEsito4(getBigDecimal("NUM_DEF_ESITO4"));
		lModel.setNumDefEsito5(getBigDecimal("NUM_DEF_ESITO5"));
		lModel.setNumDefEsito6(getBigDecimal("NUM_DEF_ESITO6"));
		lModel.setNumPendentiFine( getBigDecimal("NUM_PENDENTI_FINE") ); 
		lModel.setFasSiuChiaveUfficio(getString("FAS_SIU_CHIAVE_UFFICIO"));
		lModel.setNumDefIscErr(getBigDecimal("NUM_DEF_ISC_ERR"));
		lModel.setNumCancellati(getBigDecimal("NUM_CANCELLATI"));		
		lModel.setNumUnificati(getBigDecimal("NUM_UNIFICATI")); 
		lModel.getMagistrato().setCodMagistrato(getString("MAG_COD_MAGISTRATO"));
		lModel.getMagistrato().setCognome(getString("MAG_COGNOME"));
		lModel.getMagistrato().setNome(getString("MAG_NOME"));
		
		return lModel;
	
	}	   
}