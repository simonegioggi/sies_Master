package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
//import siap.sico.evento.model.EventoModel;
//import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.model.SoggettoModel;
//import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
//import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
//import siap.sius.statistiche.model.EveFasGepSogDetModel;
import siap.sius.statistiche.model.EveFasGepSogModel;
//import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class ProcAggregatiProcuraMittenteSqlDAO extends SIAPSqlDAO {

	private static final int SQL_QUERY_ELENCO_PROCURA_MITT_IST_DET = 0;
	private static final int SQL_QUERY_AGGR_PROCURA_MITT_IST_DET = 1;
	
	private int mSqlQueryType = -1; 
		
	public ProcAggregatiProcuraMittenteSqlDAO(Connection aCon) {
		super(aCon);
	}

	private String getCondizione(RicercaProcedimentoModel aModel) {
	    String lCondizione = "";
        String lDataPattern = "ddMMyyyy";
        String lDataORAPattern = "DDMMYYYY HH24MISS";
        
        lCondizione = " D.CHIAVE_UFFICIO = '" + aModel.getUtenteConnesso().getUfficioUtente().getCodUfficio() + "' " ; 
		if ( aModel.getDataIscrizioneInizio() != null && aModel.getDataIscrizioneFine() != null  ) {
			lCondizione += " AND (D.DATA_ISCRIZIONE BETWEEN TO_DATE('" + 
					DateUtils.getDateToString(aModel.getDataIscrizioneInizio(), lDataPattern) + " 000000','" + lDataORAPattern + "') " ; 			
			lCondizione += " AND TO_DATE('" + 
					DateUtils.getDateToString(aModel.getDataIscrizioneFine(), lDataPattern) + " 235959','" + lDataORAPattern + "') )";
		}
		
        lCondizione += " AND (A.FAS_SIU_ID_FASCICOLO_SIUS = D.ID_FASCICOLO_SIUS) " +
    				   " AND (A.COD_TIPO_MITTENTE_ATTO IN ('03','21')) " +
    				   " AND (A.COD_TIPO_MITTENTE_ATTO = MA.RV_LOW_VALUE AND MA.RV_DOMAIN='MITTENTE_ATTO') " +
    				   " AND (SM.COD_COMUNE=A.COD_SEDE_MITTENTE) " ; 
 	    
		return lCondizione;
	}
	
	
	private String getCondizioneProcTotaliPerProcuraMittente(RicercaProcedimentoModel aModel) {
		String lCondizione = this.getCondizione(aModel);
		return lCondizione;
	}
	
	public void ricercaProcPerProcuraMittente(RicercaProcedimentoModel aModel) {
		mSqlQueryType = SQL_QUERY_ELENCO_PROCURA_MITT_IST_DET;
		String lStatement = "";
		//Elenco dei procedimenti estratti,ordinati per Procura  mittente
		lStatement = " SELECT A.COD_TIPO_MITTENTE_ATTO, " + 
				   " MA.RV_MEANING MITTENTE_ATTO, "  +
				   " A.COD_SEDE_MITTENTE, " + 
				   " SM.DESCRIZIONE, " + 
				   " E.COGNOME, " +
				   " E.NOME, " + 
				   " E.DATA_NASCITA, " + 
				   " F.DESCRIZIONE LUOGO_NASCITA, " + 
				   " D.CHIAVE_ANNO ANNO , " +
				   " D.CHIAVE_PROGR PROGRESSIVO, " + 
				   " C.RV_MEANING POSIZIONE_GIURIDICA, " + 
				   " C1.RV_MEANING CONTENUTO, " +
				   " C2.RV_MEANING STATO_PROCEDIMENTO " +  
			  " FROM GENERALE_PROCEDIMENTO A, " +
			 	   " CG_REF_CODES C, " +
			 	   " FASCICOLO_SIUS D, " +
			 	   " SOGGETTO E, " +
			 	   " CG_REF_CODES C1, " + 
			 	   " CG_REF_CODES C2, " +
			 	   " COMUNE F, " +
			 	   " CG_REF_CODES MA, " + 
			 	   " COMUNE SM  " +
			 " WHERE "  +
			 	   this.getCodizioneProcPerProcuraMittente(aModel) + 
			" ORDER BY  MA.RV_MEANING, SM.DESCRIZIONE, D.CHIAVE_ANNO, D.CHIAVE_PROGR ASC ";
		setStatement(lStatement);
	}

	private String getCodizioneProcPerProcuraMittente(RicercaProcedimentoModel aModel) {
		String lCondizione = "";
		lCondizione += this.getCondizione(aModel);
		lCondizione += 	" AND (A.COD_OGGETTO_PROCEDIMENTO=C1.RV_LOW_VALUE AND C1.RV_DOMAIN='OGGETTO_PROCEDIMENTO') " +
				   		" AND (A.COD_POSIZIONE_GIURIDICA=C.RV_LOW_VALUE AND C.RV_DOMAIN='POSIZIONE_GIURIDICA') " +
				   		" AND (SM.COD_COMUNE=A.COD_SEDE_MITTENTE) " +
				   		" AND (E.ID_SOGGETTO=D.SOG_ID_SOGGETTO) " +
				   		" AND (F.COD_COMUNE=E.COD_COMUNE_NASCITA) " +
				   		" AND (D.COD_STATO_FASCICOLO=C2.RV_LOW_VALUE AND C2.RV_DOMAIN='STATO_FASCICOLO') " ;
		return lCondizione;
	}
	
	public void ricercaProcTotaliPerProcuraMittente(RicercaProcedimentoModel aModel ) {
		mSqlQueryType = SQL_QUERY_AGGR_PROCURA_MITT_IST_DET;
		String lStatement = "";
		//Totali procedimenti iscritti aggregati per Procura mittente atto
		lStatement = " SELECT  A.COD_TIPO_MITTENTE_ATTO, " +
							 " A.COD_SEDE_MITTENTE, " + 
							 " MA.RV_MEANING MITTENTE_ATTO, " +
							 " SM.DESCRIZIONE , " +
							 " COUNT(*) TOTALI_PROCEDIMENTI " + 
					   " FROM  FASCICOLO_SIUS D, " +
					   		"  GENERALE_PROCEDIMENTO A, " +
					   		"  COMUNE SM, " +
					   		"  CG_REF_CODES MA " + 
					  " WHERE " +
					   		this.getCondizioneProcTotaliPerProcuraMittente(aModel) +
					  " GROUP BY A.COD_TIPO_MITTENTE_ATTO, " +  
					   			" A.COD_SEDE_MITTENTE , " + 
					   			" MA.RV_MEANING, " +
					   			" SM.DESCRIZIONE " +
					  " ORDER BY A.COD_TIPO_MITTENTE_ATTO, " +  
								" A.COD_SEDE_MITTENTE , " +
								" MA.RV_MEANING, " + 
								" SM.DESCRIZIONE " ;
		
		setStatement(lStatement);
	}
	
	public GenericModel getModel() throws DAOException {
	    EveFasGepSogModel lModel = new EveFasGepSogModel();
	    
	    if( mSqlQueryType == SQL_QUERY_ELENCO_PROCURA_MITT_IST_DET ) {
	    	lModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
	    	lModel.getGeneraleProcedimento().setCodTipoMittenteAtto(getString("COD_TIPO_MITTENTE_ATTO"));
	    	lModel.getGeneraleProcedimento().setDescrTipoMittenteAtto(getString("MITTENTE_ATTO"));
	    	lModel.getGeneraleProcedimento().setCodSedeMittente(getString("COD_SEDE_MITTENTE"));
	    	lModel.getGeneraleProcedimento().setDescrMittente(getString("DESCRIZIONE"));
	    	
	        lModel.getGeneraleProcedimento().setDescrOggettoProcedimento(getString("CONTENUTO"));
	        lModel.getGeneraleProcedimento().setDescrPosGiuridica(getString("POSIZIONE_GIURIDICA"));
	    	
	    	lModel.setFascicoloSius(new FascicoloSiusModel());
	    	lModel.getFascicoloSius().setChiaveAnno(getBigDecimal("ANNO"));
	    	lModel.getFascicoloSius().setChiaveProgr(getBigDecimal("PROGRESSIVO"));
	    	lModel.getFascicoloSius().setDescrStatoFascicolo(getString("STATO_PROCEDIMENTO"));
	    	
	    	lModel.getFascicoloSius().setSoggetto(new SoggettoModel());
	    	lModel.getFascicoloSius().getSoggetto().setCognome(getString("COGNOME"));
	    	lModel.getFascicoloSius().getSoggetto().setNome(getString("NOME"));
	    	lModel.getFascicoloSius().getSoggetto().setDataNascita(getDate("DATA_NASCITA"));
	    	lModel.getFascicoloSius().getSoggetto().setDescrComuneNascita(getString("LUOGO_NASCITA"));
	    }
	    
	    if( mSqlQueryType == SQL_QUERY_AGGR_PROCURA_MITT_IST_DET) {
	    	lModel.setGeneraleProcedimento(new GeneraleProcedimentoModel());
	    	lModel.getGeneraleProcedimento().setCodTipoMittenteAtto(getString("COD_TIPO_MITTENTE_ATTO"));
	    	lModel.getGeneraleProcedimento().setDescrTipoMittenteAtto(getString("MITTENTE_ATTO"));
	    	lModel.getGeneraleProcedimento().setCodSedeMittente(getString("COD_SEDE_MITTENTE"));
	    	lModel.getGeneraleProcedimento().setDescrMittente(getString("DESCRIZIONE"));
	    	
	    	lModel.setTotale(getInteger("TOTALI_PROCEDIMENTI"));	    	
	    }
	    
	    return lModel;
	}
}