package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
//import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
//import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.statistiche.model.EveFasGepSogDetModel;
//import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class ProcAggregatiIstitutoDetenzioneSqlDAO extends SIAPSqlDAO {

	private static final int SQL_QUERY_AGGR_IST_DET = 0;
	private static final int SQL_QUERY_SOGG_IST_DET = 1;
	
	private int mSqlQueryType = -1; 
	
	public ProcAggregatiIstitutoDetenzioneSqlDAO(Connection aCon) {
		super(aCon);
	}
	
	private String getCondizione(RicercaProcedimentoModel aModel) {
	    String lCondizione = "";
	    String lDataPattern = "ddMMyyyy";
        String lDataORAPattern = "DDMMYYYY HH24:MI:SS";
        
        lCondizione = " D.CHIAVE_UFFICIO = '" + aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' " + 
				" AND L.ID_LUOGO_DETENZIONE = " + 
						" (SELECT MIN (ID_LUOGO_DETENZIONE) FROM LUOGO_DETENZIONE WHERE FAS_SIU_ID_FASCICOLO_SIUS=D.ID_FASCICOLO_SIUS ) ";
        
		if ( aModel.getDataIscrizioneInizio() != null && aModel.getDataIscrizioneFine() != null  ) {
			lCondizione += " AND (D.DATA_ISCRIZIONE BETWEEN TO_DATE('" + 
					DateUtils.getDateToString(aModel.getDataIscrizioneInizio(), lDataPattern) + " 00:00:00','" + lDataORAPattern + "') " ; 			
			lCondizione += " AND TO_DATE('" + 
					DateUtils.getDateToString(aModel.getDataIscrizioneFine(), lDataPattern) + " 23:59:59','" + lDataORAPattern + "') )";
		}
		
/*
		if ( aModel.getDataIscrizioneInizio() != null && aModel.getDataIscrizioneFine() != null  ) {
			lCondizione += " AND (D.DATA_ISCRIZIONE BETWEEN TO_DATE('" + 
					DateUtils.getDateToString(
							DateUtils.setTime(aModel.getDataIscrizioneInizio(), 00, 00, 00), lDataPattern) + "','" + lDataORAPattern + "') " ; 			
			lCondizione += " AND TO_DATE('" + 
					DateUtils.getDateToString(
							DateUtils.setTime(aModel.getDataIscrizioneFine(), 23, 59, 59), lDataPattern) + "','" + lDataORAPattern + "') )";
		}
*/
		
		lCondizione += " AND (I.ID_ISTITUTO_DETENZIONE=L.IST_DET_ID_ISTITUTO_DETENZIONE) "; 
	    
		return lCondizione;
	}
	
	
	private String getCondizioneSoggettiIstitutoDetenzione(RicercaProcedimentoModel aModel) {
		String lCondizione = this.getCondizione(aModel);
		lCondizione += " AND (A.FAS_SIU_ID_FASCICOLO_SIUS = D.ID_FASCICOLO_SIUS) " + 
					   " AND (A.COD_POSIZIONE_GIURIDICA=C.RV_LOW_VALUE AND C.RV_DOMAIN='POSIZIONE_GIURIDICA') " +
					   " AND (E.ID_SOGGETTO=D.SOG_ID_SOGGETTO)" +
					   " AND (A.COD_OGGETTO_PROCEDIMENTO=C1.RV_LOW_VALUE AND C1.RV_DOMAIN='OGGETTO_PROCEDIMENTO') " + 
					   " AND (D.COD_STATO_FASCICOLO=C2.RV_LOW_VALUE AND C2.RV_DOMAIN='STATO_FASCICOLO') " +
					   " AND (I.COD_TIPO_ISTITUTO=TI.RV_LOW_VALUE AND TI.RV_DOMAIN='TIPO_ISTITUTO') " ;
		return lCondizione;
	}
	
	
	/*
	SELECT DISTINCT I.DESCRIZIONE "ISTITUTO_DETENZIONE",
	  COUNT(*) TOTALI
	FROM FASCICOLO_SIUS D,
	  LUOGO_DETENZIONE L,
	  ISTITUTO_DETENZIONE I
	WHERE D.CHIAVE_UFFICIO           = '03200601305'
	AND L.ID_LUOGO_DETENZIONE        =
	  (SELECT MIN (ID_LUOGO_DETENZIONE)
	  FROM LUOGO_DETENZIONE
	  WHERE FAS_SIU_ID_FASCICOLO_SIUS=D.ID_FASCICOLO_SIUS
	  )
	AND (D.DATA_ISCRIZIONE BETWEEN TO_DATE(' 01012010','DDMMYYYY') AND TO_DATE(' 01012012','DDMMYYYY') )
	AND (I.ID_ISTITUTO_DETENZIONE  =L.IST_DET_ID_ISTITUTO_DETENZIONE)
	GROUP BY I.DESCRIZIONE
	ORDER BY I.DESCRIZIONE ASC 
	*/
	
	public void ricercaProcedimentiSoggettiIstitutoDetenzione(RicercaProcedimentoModel aModel) {
		mSqlQueryType = SQL_QUERY_SOGG_IST_DET;
		String lStatement = "";
		lStatement = 
		" SELECT DISTINCT " + 
		" I.DESCRIZIONE \"ISTITUTO_DETENZIONE\", " +
		" TI.RV_MEANING TIPO_ISTITUTO, " +
		" M.COGNOME \"COGNOME_MAGISTRATO\", " + 
		" M.NOME \"NOME_MAGISTRATO\", " + 
		" D.CHIAVE_ANNO , " + 
		" D.CHIAVE_PROGR, " + 
		" D.COD_STATO_FASCICOLO, " +
		" C2.RV_MEANING DESCR_STATO_FASCICOLO, " +
		" E.COGNOME, " + 
		" E.NOME, " + 
		" D.DATA_ISCRIZIONE, " +
		" D.DATA_DEFINIZIONE, " +  
		" C1.RV_MEANING CONTENUTO, " +   
		" C.RV_MEANING POSIZIONE_GIURIDICA," + 
		" L.IST_DET_ID_ISTITUTO_DETENZIONE " +
		" FROM GENERALE_PROCEDIMENTO A,  " + 
			 " CG_REF_CODES C, " + 
			 " FASCICOLO_SIUS D " +
		" LEFT OUTER JOIN MAGISTRATO_RELATORE MR " + 
		"	ON MR.FAS_SIU_ID_FASCICOLO_SIUS = D.ID_FASCICOLO_SIUS AND DATA_FINE IS NULL " +
		" LEFT OUTER JOIN MAGISTRATO M " + 
		"	ON M.COD_MAGISTRATO = MR.MAG_COD_MAGISTRATO AND M.COD_UFFICIO_APPARTENENZA='" + aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio()+ "', " +
		" SOGGETTO E, CG_REF_CODES C1, CG_REF_CODES C2, " +
		" LUOGO_DETENZIONE L, " +
		" ISTITUTO_DETENZIONE I, CG_REF_CODES TI " +  
		" WHERE " +
		this.getCondizioneSoggettiIstitutoDetenzione(aModel) +
		" ORDER BY I.DESCRIZIONE ASC, " + 		
				 " M.COGNOME ASC, " +
				 " M.NOME ASC, " +
				 " D.DATA_ISCRIZIONE ASC, " + 
				 " D.CHIAVE_ANNO ASC, " + 
				 " D.CHIAVE_PROGR ASC ";
		
		setStatement(lStatement);
	}

	private String getCodizioneArregatiIstitutoDetenzione(RicercaProcedimentoModel aModel) {
		return this.getCondizione(aModel);
	}
	
	public void ricercaAggregatiPerIstitutiDetenzione(RicercaProcedimentoModel aModel ) {
		mSqlQueryType = SQL_QUERY_AGGR_IST_DET;
		String lStatement = " SELECT  DISTINCT I.DESCRIZIONE \"ISTITUTO_DETENZIONE\", COUNT(*) TOTALI_PROCEDIMENTI " +
						  	" FROM  FASCICOLO_SIUS D, " + 
						  		  " LUOGO_DETENZIONE L, " +
						  		  " ISTITUTO_DETENZIONE I " +
						  	" WHERE " + 
						  		this.getCodizioneArregatiIstitutoDetenzione(aModel) +  
						  	" GROUP BY I.DESCRIZIONE " +
						  	" ORDER BY I.DESCRIZIONE ASC ";
	
		setStatement(lStatement);
	}
	
	public GenericModel getModel() throws DAOException {
	    EveFasGepSogDetModel lModel = new EveFasGepSogDetModel();
	    
	    if( mSqlQueryType == SQL_QUERY_SOGG_IST_DET ) {
	    	// popola Istituto detenzione.
	    	lModel.setIstitutoDetenzione(new IstitutoDetenzioneModel());
	    	lModel.getIstitutoDetenzione().setDescrizione(getString("ISTITUTO_DETENZIONE"));
	    	lModel.getIstitutoDetenzione().setIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
	    	lModel.getIstitutoDetenzione().setDescrTipoIstituto(getString("TIPO_ISTITUTO"));
		    // popola magistrato.
		    lModel.setMagistrato(new MagistratoModel());
		    lModel.getMagistrato().setNome(getString("NOME_MAGISTRATO"));
		    lModel.getMagistrato().setCognome(getString("COGNOME_MAGISTRATO"));	
		    // popola fascicolo sius.
		    lModel.setFascicoloSius(new FascicoloSiusModel());
		    lModel.getFascicoloSius().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
	        lModel.getFascicoloSius().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
	        lModel.getFascicoloSius().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
	        lModel.getFascicoloSius().setDataDefinizione(getDate("DATA_DEFINIZIONE"));
	        lModel.getFascicoloSius().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
	        lModel.getFascicoloSius().setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));
	        // popola fascicolo sius -> Soggetto.
	        lModel.getFascicoloSius().setSoggetto(new SoggettoModel());
	        lModel.getFascicoloSius().getSoggetto().setCognome(getString("COGNOME"));
	        lModel.getFascicoloSius().getSoggetto().setNome(getString("NOME"));
	        // popola generale procedimento.
	        lModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
	        lModel.getGeneraleProcedimento().setDescrOggettoProcedimento(getString("CONTENUTO"));
	        lModel.getGeneraleProcedimento().setDescrPosGiuridica(getString("POSIZIONE_GIURIDICA"));
	    }
	    
	    if ( mSqlQueryType == SQL_QUERY_AGGR_IST_DET ) {
	    	lModel.setIstitutoDetenzione(new IstitutoDetenzioneModel());
	    	lModel.getIstitutoDetenzione().setDescrizione(getString("ISTITUTO_DETENZIONE"));
	    	lModel.setTotale(getInteger("TOTALI_PROCEDIMENTI"));
	    }
	    
	    return lModel;
	}
}