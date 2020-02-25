package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

/**
 * <p>
 * Title: TitoloCumulatoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella TitoloCumulato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class TitoloCumulatoSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  /***************************************************************************** 
   * Costruttore  
	 * 
   * @param con 
   ****************************************************************************/ 
  public TitoloCumulatoSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountTitoloCumulato(TitoloCumulatoModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM TITOLO_CUMULATO ";

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lStatement+=" WHERE " + lCondizioni;

    // Imposta lo statement da eseguire 
    setStatement(lStatement);
    siesLogger.debug("lStatement = "+lStatement); 
  }

  /***************************************************************************** 
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 * 
   * @param aModel 
   * @param aPage 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaTitoloCumulatoPaged(TitoloCumulatoModel aModel, int aPage) throws DAOException { 
    String lStatement = new String(""); 

    lStatement += getSqlQuery(); 

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    lStatement+=lCondizioni;

    lStatement += " "+getOrderBy()+" "; 

    String lPaginedStatement = ""; 
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

    setStatement(lPaginedStatement); 
    siesLogger.debug("lPaginedStatement = "+lPaginedStatement); 
  } 


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
	 * 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaTitoloCumulato( TitoloCumulatoModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    lSql+=lCondizioni;

    lSql += " "+getOrderBy()+" "; 

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    siesLogger.debug("lSql = "+lSql); 
  }

  public void ricercaTitoloCumulatoByIstruttoria ( BigDecimal  aIdIstruttoria)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    lSql+=  " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    siesLogger.debug("lSql = "+lSql); 
  }
  
  public void ricercaTitoloCumulatoByIstrIdOrig ( BigDecimal  aIdIstruttoria, BigDecimal aIdTitoloOrigine)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    lSql+=  " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;
    lSql+=  " AND ID_SENTENZA_ORIGINE = "+aIdTitoloOrigine ;

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    siesLogger.debug("lSql = "+lSql); 
  }
  
	public void ricercaTitoloCumulatoByIstruttoriaOrderBy(BigDecimal aIdIstruttoria, String aOrdinamento)
			throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    lSql+=  " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;
    
		if (aOrdinamento != null) {
      if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC))
        lSql += " ORDER BY DATA_IRREVOCABILITA ASC "; 
      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
        lSql += " ORDER BY DATA_IRREVOCABILITA DESC "; 
      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC))
        lSql += " ORDER BY DATA_PROVVEDIMENTO ASC "; 
      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
        lSql += " ORDER BY DATA_PROVVEDIMENTO DESC "; 
    }

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    siesLogger.debug("lSql = "+lSql); 
  }

  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
	 * 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaTitoloCumulatoByKey( BigDecimal aIdTitoloCumulato) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += setCondizioniByKey( aIdTitoloCumulato);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    siesLogger.debug("lSql = "+lSql); 
  }
  
  public void ricercaTitoloCumulatoByIstruttoriaDataReatoCumOrderBy ( BigDecimal  aIdIstruttoria, 
			String aOrdinamento, Date aDataReato) throws DAOException {

	    // Recupera la select...from 
	    String lSql = getSqlQueryDataReatoCum();

	    lSql+=  " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;
	    
	    lSql += setCondizioniDataRea(aDataReato);
	    
		if (aOrdinamento != null) {
	      if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA DESC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO DESC "; 
	    }

	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
	    siesLogger.debug("--XX-- lSqlQueryDataReatoCum = "+lSql); 
  }
  
  
  // La Query Seleziona i Titoli_Cumulati che hanno almeno un Beneficio_Cumulo aggregato;
  // NON viene estratto nessun canpo di BENEFICIO_CUMULO
	public void ricercaTitoloCumulatoJoinBeneficioCumByIstruttoriaOrderBy(BigDecimal aIdIstruttoria,
			String aOrdinamento, String aCodNaturaBen, Vector<String> aCodTipiBen) throws DAOException {
	    // Recupera la select...from 
	    String lSql = getSqlQueryJoinBeneficioCum();

	    lSql+= " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;
	    lSql+= " AND BENECUM.TIT_ID_TITOLO_CUMULATO = ID_TITOLO_CUMULATO ";
	    
		if (!"".equals(aCodNaturaBen) && !("null").equals(aCodNaturaBen)) {
	    	lSql+= " AND BENECUM.COD_NATURA_BENEFICIO = '"+aCodNaturaBen+"' ";
	    }
	    
		if (aCodTipiBen != null && aCodTipiBen.size() > 0) {
	    	lSql += " AND BENECUM.COD_TIPO_BENEFICIO in (";
			for (int i = 0; i < aCodTipiBen.size(); i++) {
	    		lSql += " '"+aCodTipiBen.elementAt(i)+"'";
				if (i < aCodTipiBen.size() - 1) {
	    			lSql += ",";
	    		}	
	    	}
	          
	    	lSql += " ) ";      
	    }

		if (aOrdinamento != null) {
	      if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA DESC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO DESC "; 
	    } 
	    
	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
	    siesLogger.debug("--XX-- lSqlQueryJoinBeneficioCum = "+lSql);
  }
  
  // La Query Seleziona i Titoli_Cumulati che hanno almeno una Sanzione_Sost_Cumulo aggregata;
  // NON viene estratto nessun canpo di SANZIONE_SOST_CUM
	public void ricercaTitoloCumulatoJoinSanzioneSostCumByIstruttoriaOrderBy(BigDecimal aIdIstruttoria,
			String aOrdinamento) throws DAOException {
	    // Recupera la select...from 
	    String lSql = getSqlQueryJoinSanzioneSostCum();

	    lSql+= " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;
	    lSql+= " AND SSCUM.TIT_ID_TITOLO_CUMULATO = ID_TITOLO_CUMULATO ";
	    
		/*
		 * if (aCodTipiSS!=null && aCodTipiSS.size()>0) { lSql += " AND SSCUM.COD_TIPO_SANZIONE in ("; for
		 * (int i=0; i<aCodTipiSS.size(); i++) { lSql += " '"+aCodTipiSS.elementAt(i)+"'"; if
		 * (i<aCodTipiSS.size()-1) { lSql += ","; } }
		 * 
		 * lSql += " ) "; }
*/
	    
		if (aOrdinamento != null) {
	      if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA DESC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO DESC "; 
	    } 
	    
	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
	    siesLogger.debug("--XX-- getSqlQueryJoinSanzioneSostCum = "+lSql);
  }
	
  // La Query Seleziona i Titoli_Cumulati che hanno almeno una Pena_Accessoria_Cumulo aggregata;
  // NON viene estratto nessun canpo di PENA_ACCESSORIA_CUMULO
	public void ricercaTitoloCumulatoJoinPenaAccCumByIstruttoriaOrderBy(BigDecimal aIdIstruttoria,
			String aOrdinamento) throws DAOException {
	    // Recupera la select...from 
	    String lSql = getSqlQueryJoinPACum();

	    lSql+= " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;
	    lSql+= " AND PACUM.TIT_ID_TITOLO_CUMULATO = ID_TITOLO_CUMULATO ";
	    
		if (aOrdinamento != null) {
	      if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA DESC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO DESC "; 
	    } 
	    
	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
	    siesLogger.debug("--XX-- getSqlQueryJoinPACum = "+lSql);
 }
  
 //La Query Seleziona i Titoli_Cumulati e Stato_Esec_Titolo_Cum 
 // NON viene estratto nessun canpo di STATO_ESEC_TITOLO_CUM
	public void ricercaTitoloCumulatoJoinStatoEsecTitoloCumByIstruttoriaOrderBy(BigDecimal aIdIstruttoria,
			String aOrdinamento, Vector<String> aCodici) throws DAOException {
	    // Recupera la select...from 
	    String lSql = getSqlQueryJoinStatoEsecCum();

	    lSql+= " AND TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;
	    lSql+= " AND STESEC.TIT_ID_TITOLO_CUMULATO = ID_TITOLO_CUMULATO ";
	    lSql+= " AND STESEC.COD_TIPO_PROVVEDIMENTO IN ('02', '03') ";
	    lSql+= " AND STESEC.COD_ESITO = '0001' ";
	    
		if (aCodici != null && aCodici.size() > 0) {
	    	lSql += " AND STESEC.COD_MOTIVO in (";
			for (int i = 0; i < aCodici.size(); i++) {
	    		lSql += " '"+aCodici.elementAt(i)+"'";
				if (i < aCodici.size() - 1) {
	    			lSql += ",";
	    		}	
	    	}
	          
	    	lSql += " ) ";      
	    }

		if (aOrdinamento != null) {
	      if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA DESC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO DESC "; 
	    } 
	    
	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
	    siesLogger.debug("--XX-- getSqlQueryJoinStatoEsecCum = "+lSql);
 }
 
 public void ricercaTitoloCumulatoJoinStatoEsecTitoloCumByIstruttoria_e_Provvedimento ( BigDecimal  aIdIstruttoria, String aOrdinamento, 
		 									Vector<String> aTipoEve, Vector<String> aTipoProv, Vector<String> aCodMot) throws DAOException
 {
	    // Recupera la select...from 
	    String lSql = getSqlQueryJoinStatoEsecCum();

	    lSql+= " AND TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;
	    lSql+= " AND STESEC.TIT_ID_TITOLO_CUMULATO = ID_TITOLO_CUMULATO ";
	    
	    // Cod Tipo Evento
	    if (aTipoEve!=null && aTipoEve.size()>0)
	    {
	   	 lSql += " AND STESEC.COD_TIPO_EVENTO in (";
	   	 for (int i=0; i<aTipoEve.size(); i++) 
	   	 {
	   		 lSql += " '"+aTipoEve.elementAt(i)+"'";
	   		 if (i<aTipoEve.size()-1)
	   		 {	
	   			 lSql += ",";
	   		 }	
	   	 }
		          
	   	 lSql += " ) ";      
	    }
	    
	    // Cod Tipo Provvedimento
	    if (aTipoProv!=null && aTipoProv.size()>0)
	    {
	   	 lSql += " AND STESEC.COD_TIPO_PROVVEDIMENTO in (";
	   	 for (int i=0; i<aTipoProv.size(); i++) 
	   	 {
	   		 lSql += " '"+aTipoProv.elementAt(i)+"'";
	   		 if (i<aTipoProv.size()-1)
	   		 {	
	   			 lSql += ",";
	   		 }	
	   	 }
		          
	   	 lSql += " ) ";      
	    }
	    
	    // Cod Motivo
    	if (aCodMot!=null && aCodMot.size()>0)
    	{
	    	lSql += " AND STESEC.COD_MOTIVO in (";
	    	for (int i=0; i<aCodMot.size(); i++) 
	    	{
	    		lSql += " '"+aCodMot.elementAt(i)+"'";
	    		if (i<aCodMot.size()-1)
	    		{	
	    			lSql += ",";
	    		}	
	    	}
	          
	    	lSql += " ) ";      
    	}
	    
	    if (aOrdinamento!=null)
	    {
	      if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA DESC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO DESC "; 
	    } 
	    
	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
	    siesLogger.debug("--XX-- getSqlQueryJoinStatoEsecCum = "+lSql);
 }
 
// ================================= 

	public void RicercaTitoloCumulatoPerRevocaBeneficio(BigDecimal aIdTitoloCumulato,
			BigDecimal aIdIstruttoria, String aTipoBen) throws DAOException {
	    String lSql = new String("");
		lSql += " SELECT "
				+ "DISTINCT(tc.ID_TITOLO_CUMULATO), "
				+ "b.COD_DPR, b.COD_NATURA_BENEFICIO, b.COD_TIPO_BENEFICIO, B.ID_BENEFICIO_CUMULO, B.TIT_ID_TITOLO_CUMULO_COLLEGATO, "
				+ "tc.DATA_PROVVEDIMENTO, tc.DATA_IRREVOCABILITA, "
				+ "tc.COD_TIPO_PROVVEDIMENTO, CODTIPOPROVVEDIMENTO.RV_MEANING DESCRTIPOPROVVEDIMENTO, "
				+ "tc.COD_TIPO_AUTORITA_EMITTENTE, CODTIPOAUTORITAEMITTENTE.RV_MEANING DESCRTIPOAUTORITAEMITTENTE, "
				+ "tc.COD_LUOGO_EMITTENTE, CODLUOGOEMITTENTE.DESCRIZIONE DESC_LUOGO_EMITTENTE, "
				+ "tc.NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "tc.ANNO_SENTENZA, tc.NUMERO_SENTENZA, "
				+ "ud.DESCR_TIPO_UFFICIO, ud.DESCR_COMUNE, "
				+ "pc.CHIAVE_ANNO_FAS_CUMULATO, pc.CHIAVE_PROGR_FAS_CUMULATO "
				+ "FROM "
				+ "TITOLO_CUMULATO tc, COMUNE CODLUOGOEMITTENTE, BENEFICIO_CUMULO b, PROCEDIMENTO_CUMULATO pc, "
				+ "CG_REF_CODES CODTIPOPROVVEDIMENTO, CG_REF_CODES CODTIPOAUTORITAEMITTENTE, UFFICIO_DESCR ud";
	    lSql +=" WHERE";
	    lSql +=" tc.ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria;
	    lSql +=" AND tc.ID_TITOLO_CUMULATO = B.TIT_ID_TITOLO_CUMULATO";
	    lSql +=" AND b.COD_NATURA_BENEFICIO = 'C'";
	    lSql +=" AND b.COD_TIPO_BENEFICIO "+aTipoBen;
	    lSql +=" AND b.TIT_ID_TITOLO_CUMULATO != "+aIdTitoloCumulato;
	    lSql +=" AND tc.COD_TIPO_PROVVEDIMENTO in ('01', '02')";
	    
	    lSql +=" AND ( nvl(tc.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
	    lSql +=" AND ( nvl(tc.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ) "; //TIPO_UFFICIO_EMITTENTE
	    lSql +=" AND ( nvl(tc.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE ) "; 
	    
	    lSql +=" AND tc.ID_TITOLO_CUMULATO = pc.TIT_ID_TITOLO_CUMULATO (+) ";
	    lSql +=" AND ud.COD_UFFICIO (+) = pc.COD_UFFICIO_FAS_CUMULATO ";

	    lSql += " ORDER BY tc.DATA_IRREVOCABILITA DESC "; 

	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
	   // siesLogger.debug("lSql = "+lSql); 
  }
  
  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
	 * 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModelXRevoche() throws DAOException {
     TitoloCumulatoModel aModel = new  TitoloCumulatoModel(); 
     
     ProcedimentoCumulatoModel lProcModel = new ProcedimentoCumulatoModel();
     BeneficioCumuloModel lBenModel = new BeneficioCumuloModel();

    //Inserire le opportune set delle descrizioni!
     	aModel.setIdTitoloCumulato            ( getBigDecimal ("ID_TITOLO_CUMULATO"            ) ); 
     	aModel.setCodTipoProvvedimento        ( getString     ("COD_TIPO_PROVVEDIMENTO"        ) ); 
     	aModel.setDescrTipoProvvedimento 	  (getString	  ("DESCRTIPOPROVVEDIMENTO"		   ) );
    	aModel.setDataIrrevocabilita          ( getDate       ("DATA_IRREVOCABILITA"           ) ); 
    	aModel.setDataProvvedimento           ( getDate       ("DATA_PROVVEDIMENTO"            ) ); 
    	aModel.setAnnoSentenza                ( getBigDecimal ("ANNO_SENTENZA"                 ) ); 
    	aModel.setNumeroSentenza              ( getString     ("NUMERO_SENTENZA"               ) ); 
    	aModel.setCodTipoAutoritaEmittente    ( getString     ("COD_TIPO_AUTORITA_EMITTENTE"   ) ); 
    	aModel.setDescrTipoAutoritaEmittente  (getString	  ("DESCRTIPOAUTORITAEMITTENTE"		) );
    	aModel.setCodLuogoEmittente           ( getString     ("COD_LUOGO_EMITTENTE"           ) ); 
    	aModel.setDescrLuogoEmittente  		  (getString	  ("DESC_LUOGO_EMITTENTE" 		   ) );
    	aModel.setNumSezioneAutoritaEmittente ( getString     ("NUM_SEZIONE_AUTORITA_EMITTENTE") ); 
   // aModel.setIstrIdIstruttoriaCumulo     ( getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"    ) ); 
    
    	lProcModel.setChiaveAnnoFasCumulato			(getBigDecimal ("CHIAVE_ANNO_FAS_CUMULATO"   ) );
    	lProcModel.setChiaveProgrFasCumulato		(getBigDecimal ("CHIAVE_PROGR_FAS_CUMULATO"  ) );
    	lProcModel.setDescrTipoUfficioFasCumulato	(getString	   ("DESCR_TIPO_UFFICIO" 		 ) );
    	lProcModel.setDescrLuogoUfficioFasCumulato	(getString	   ("DESCR_COMUNE" 		  		 ) );
    //lProcModel.setDescrUfficioFasCumulato(aValore)
    	
    	lBenModel.setCodDpr							(getString		("COD_DPR")					);
    	lBenModel.setCodTipoBeneficio				(getString		("COD_TIPO_BENEFICIO")		);
    	lBenModel.setCodNaturaBeneficio				(getString		("COD_NATURA_BENEFICIO")	);
    	lBenModel.setIdBeneficioCumulo				(getBigDecimal	("ID_BENEFICIO_CUMULO")		);
    	lBenModel.setTitIdTitoloCumulatoCollegato	(getBigDecimal	("TIT_ID_TITOLO_CUMULO_COLLEGATO") );
    
    	aModel.setProcedimentoCumulato(lProcModel);
    	aModel.setBeneficioCumulato(lBenModel);


    return aModel;
  }

  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
	 * 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

		lStatement += " SELECT "
				+ "ID_TITOLO_CUMULATO, "
				+ "COD_TIPO_PROVVEDIMENTO, CODTIPOPROVVEDIMENTO.RV_MEANING DESC_TIPO_PROVVEDIMENTO, "
				+ "DATA_IRREVOCABILITA, "
				+ "ANNO_REGE_PM, "
				+ "NUMERO_REGE_PM, "
				+ "COD_SEDE_NOTIZIA_REATO, CODSEDENOTIZIAREATO.DESCRIZIONE DESC_SEDE_NOTIZIA_REATO, "
				+ "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, "
				+ "TIPO_REG_GEN, "
				+ "DATA_PROVVEDIMENTO, "
				+ "ANNO_SENTENZA, "
				+ "NUMERO_SENTENZA, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, CODTIPOAUTORITAEMITTENTE.RV_MEANING DESC_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, CODLUOGOEMITTENTE.DESCRIZIONE DESC_LUOGO_EMITTENTE, "
				+ "NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "COD_TIPO_RITO, CODTIPORITO.RV_MEANING DESC_TIPO_RITO, "
				+ "COD_TIPO_PROVVEDIMENTO_RIF, CODTIPOPROVVEDIMENTORIF.RV_MEANING DESC_TIPO_PROVVEDIMENTO_RIF, "
				+ "COD_TIPO_PROVV_RIF, CODTIPOPROVVRIF.RV_MEANING DESC_TIPO_PROVV_RIF, "
				+ "DATA_PROVV_RIF, "
				+ "ANNO_PROVV_RIF, "
				+ "NUMERO_PROVV_RIF, "
				+ "COD_TIPO_AUTORITA_PROVV_RIF, CODTIPOAUTORITAPROVVRIF.RV_MEANING DESC_TIPO_AUTORITA_PROVV_RIF, "
				+ "COD_LUOGO_PROVV_RIF, CODLUOGOPROVVRIF.DESCRIZIONE DESC_LUOGO_PROVV_RIF, "
				+ "NUM_SEZIONE_AUTORITA_PROVV_RIF, "
				+ "COD_TIPO_RITO_RIF, CODTIPORITORIF.RV_MEANING DESC_TIPO_RITO_RIF, "
				+ "COD_TIPO_PROVVEDIMENTO_ALTRO, CODTIPOPROVVEDIMENTOALTRO.RV_MEANING DESC_TIPO_PROVVEDIMENTO_ALTRO, "
				+ "NOTE1_DECISIONE_CASSAZIONE, "
				+ "NOTE2_DECISIONE_CASSAZIONE, "
				+ "ANNO_SENTENZA_CASSAZIONE, "
				+ "NUMERO_SENTENZA_CASSAZIONE, "
				+ "ANNO_RACCOLTA_GENERALE, "
				+ "NUMERO_RACCOLTA_GENERALE, "
				+ "COD_TIPO_DECISIONE_CASSAZIONE, CODTIPODECISIONECASSAZIONE.RV_MEANING DESC_TIPO_DECISIONE_CASSAZIONE, "
				+ "NOTE, " + "ISTR_ID_ISTRUTTORIA_CUMULO, " + "FLAG_STATO, " + "MOTIVO_MODIFICA, "
				+ "ID_SENTENZA_ORIGINE," + "FLAG_ESCLUSO," +
                
                  "MESS_ID_MESSAGGIO, TIPO_ISCRIZIONE, "+
                  
                  "DATA_PRESA_IN_CARICO, "+
                  
				"COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM TITOLO_CUMULATO ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTO ";
    lStatement +=    " , COMUNE CODSEDENOTIZIAREATO ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAEMITTENTE ";
    lStatement +=    " , COMUNE CODLUOGOEMITTENTE ";
    lStatement +=    " , CG_REF_CODES CODTIPORITO ";    
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAPROVVRIF ";
    lStatement +=    " , COMUNE CODLUOGOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPORITORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTOALTRO ";
    lStatement +=    " , CG_REF_CODES CODTIPODECISIONECASSAZIONE ";    
    lStatement += " WHERE 1=1 ";
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_SEDE_NOTIZIA_REATO,'-') = CODSEDENOTIZIAREATO.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ) "; //TIPO_UFFICIO_EMITTENTE
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO,'-') = CODTIPORITO.RV_LOW_VALUE AND CODTIPORITO.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_RIF,'-') = CODTIPOPROVVEDIMENTORIF.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTORIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVV_RIF,'-') = CODTIPOPROVVRIF.RV_LOW_VALUE AND CODTIPOPROVVRIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_PROVV_RIF,'-') = CODTIPOAUTORITAPROVVRIF.RV_LOW_VALUE AND CODTIPOAUTORITAPROVVRIF.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_PROVV_RIF,'-') = CODLUOGOPROVVRIF.COD_COMUNE  ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO_RIF,'-') = CODTIPORITORIF.RV_LOW_VALUE AND CODTIPORITORIF.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_ALTRO,'-') = CODTIPOPROVVEDIMENTOALTRO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTOALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_DECISIONE_CASSAZIONE,'-') = CODTIPODECISIONECASSAZIONE.RV_LOW_VALUE AND CODTIPODECISIONECASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' ) "; 

    //siesLogger.debug("lStatement = "+lStatement); 
    return lStatement;
  }
  
	protected String getSqlQueryDataReatoCum() {
	    String lStatement = new String("");

		lStatement += " SELECT DISTINCT "
				+ "ID_TITOLO_CUMULATO, "
				+ "COD_TIPO_PROVVEDIMENTO, CODTIPOPROVVEDIMENTO.RV_MEANING DESC_TIPO_PROVVEDIMENTO, "
				+ "DATA_IRREVOCABILITA, "
				+ "ANNO_REGE_PM, "
				+ "NUMERO_REGE_PM, "
				+ "COD_SEDE_NOTIZIA_REATO, CODSEDENOTIZIAREATO.DESCRIZIONE DESC_SEDE_NOTIZIA_REATO, "
				+ "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, "
				+ "TIPO_REG_GEN, "
				+ "DATA_PROVVEDIMENTO, "
				+ "ANNO_SENTENZA, "
				+ "NUMERO_SENTENZA, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, CODTIPOAUTORITAEMITTENTE.RV_MEANING DESC_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, CODLUOGOEMITTENTE.DESCRIZIONE DESC_LUOGO_EMITTENTE, "
				+ "NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "COD_TIPO_RITO, CODTIPORITO.RV_MEANING DESC_TIPO_RITO, "
				+ "COD_TIPO_PROVVEDIMENTO_RIF, CODTIPOPROVVEDIMENTORIF.RV_MEANING DESC_TIPO_PROVVEDIMENTO_RIF, "
				+ "COD_TIPO_PROVV_RIF, CODTIPOPROVVRIF.RV_MEANING DESC_TIPO_PROVV_RIF, "
				+ "DATA_PROVV_RIF, "
				+ "ANNO_PROVV_RIF, "
				+ "NUMERO_PROVV_RIF, "
				+ "COD_TIPO_AUTORITA_PROVV_RIF, CODTIPOAUTORITAPROVVRIF.RV_MEANING DESC_TIPO_AUTORITA_PROVV_RIF, "
				+ "COD_LUOGO_PROVV_RIF, CODLUOGOPROVVRIF.DESCRIZIONE DESC_LUOGO_PROVV_RIF, "
				+ "NUM_SEZIONE_AUTORITA_PROVV_RIF, "
				+ "COD_TIPO_RITO_RIF, CODTIPORITORIF.RV_MEANING DESC_TIPO_RITO_RIF, "
				+ "COD_TIPO_PROVVEDIMENTO_ALTRO, CODTIPOPROVVEDIMENTOALTRO.RV_MEANING DESC_TIPO_PROVVEDIMENTO_ALTRO, "
				+ "NOTE1_DECISIONE_CASSAZIONE, "
				+ "NOTE2_DECISIONE_CASSAZIONE, "
				+ "ANNO_SENTENZA_CASSAZIONE, "
				+ "NUMERO_SENTENZA_CASSAZIONE, "
				+ "ANNO_RACCOLTA_GENERALE, "
				+ "NUMERO_RACCOLTA_GENERALE, "
				+ "COD_TIPO_DECISIONE_CASSAZIONE, CODTIPODECISIONECASSAZIONE.RV_MEANING DESC_TIPO_DECISIONE_CASSAZIONE, "
				+ "TITOLO_CUMULATO.NOTE, " + "TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO, "
				+ "TITOLO_CUMULATO.FLAG_STATO, " + "TITOLO_CUMULATO.MOTIVO_MODIFICA, "
				+ "ID_SENTENZA_ORIGINE," + "FLAG_ESCLUSO," +
	                
	                  "MESS_ID_MESSAGGIO, TIPO_ISCRIZIONE, "+
	                  "DATA_PRESA_IN_CARICO, "+
	                  
				"TITOLO_CUMULATO.COD_OPERATORE_INSERIMENTO, " + "TITOLO_CUMULATO.DATA_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_OPERATORE_AGGIORNAMENTO, " + "TITOLO_CUMULATO.DATA_AGGIORNAMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_AGGIORNAMENTO ";

	    lStatement += " FROM TITOLO_CUMULATO, REATO_CUMULO REATOC ";
	    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTO ";
	    lStatement +=    " , COMUNE CODSEDENOTIZIAREATO ";
	    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAEMITTENTE ";
	    lStatement +=    " , COMUNE CODLUOGOEMITTENTE ";
	    lStatement +=    " , CG_REF_CODES CODTIPORITO ";    
	    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTORIF ";
	    lStatement +=    " , CG_REF_CODES CODTIPOPROVVRIF ";
	    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAPROVVRIF ";
	    lStatement +=    " , COMUNE CODLUOGOPROVVRIF ";
	    lStatement +=    " , CG_REF_CODES CODTIPORITORIF ";
	    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTOALTRO ";
	    lStatement +=    " , CG_REF_CODES CODTIPODECISIONECASSAZIONE ";    
	    lStatement += " WHERE 1=1 ";
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_SEDE_NOTIZIA_REATO,'-') = CODSEDENOTIZIAREATO.COD_COMUNE ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ) "; //TIPO_UFFICIO_EMITTENTE
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO,'-') = CODTIPORITO.RV_LOW_VALUE AND CODTIPORITO.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_RIF,'-') = CODTIPOPROVVEDIMENTORIF.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTORIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVV_RIF,'-') = CODTIPOPROVVRIF.RV_LOW_VALUE AND CODTIPOPROVVRIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_PROVV_RIF,'-') = CODTIPOAUTORITAPROVVRIF.RV_LOW_VALUE AND CODTIPOAUTORITAPROVVRIF.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_PROVV_RIF,'-') = CODLUOGOPROVVRIF.COD_COMUNE  ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO_RIF,'-') = CODTIPORITORIF.RV_LOW_VALUE AND CODTIPORITORIF.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_ALTRO,'-') = CODTIPOPROVVEDIMENTOALTRO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTOALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
	    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_DECISIONE_CASSAZIONE,'-') = CODTIPODECISIONECASSAZIONE.RV_LOW_VALUE AND CODTIPODECISIONECASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' ) "; 

	    //siesLogger.debug("lStatement = "+lStatement); 
	    return lStatement;
	  }

  // TITOLO_CUMULATO Join BENEFICIO_CUMULO 
	protected String getSqlQueryJoinBeneficioCum() {
    String lStatement = new String("");

		lStatement += " SELECT DISTINCT "
				+ "ID_TITOLO_CUMULATO, "
				+ "COD_TIPO_PROVVEDIMENTO, CODTIPOPROVVEDIMENTO.RV_MEANING DESC_TIPO_PROVVEDIMENTO, "
				+ "DATA_IRREVOCABILITA, "
				+ "ANNO_REGE_PM, "
				+ "NUMERO_REGE_PM, "
				+ "COD_SEDE_NOTIZIA_REATO, CODSEDENOTIZIAREATO.DESCRIZIONE DESC_SEDE_NOTIZIA_REATO, "
				+ "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, "
				+ "TIPO_REG_GEN, "
				+ "DATA_PROVVEDIMENTO, "
				+ "ANNO_SENTENZA, "
				+ "NUMERO_SENTENZA, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, CODTIPOAUTORITAEMITTENTE.RV_MEANING DESC_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, CODLUOGOEMITTENTE.DESCRIZIONE DESC_LUOGO_EMITTENTE, "
				+ "NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "COD_TIPO_RITO, CODTIPORITO.RV_MEANING DESC_TIPO_RITO, "
				+ "COD_TIPO_PROVVEDIMENTO_RIF, CODTIPOPROVVEDIMENTORIF.RV_MEANING DESC_TIPO_PROVVEDIMENTO_RIF, "
				+ "COD_TIPO_PROVV_RIF, CODTIPOPROVVRIF.RV_MEANING DESC_TIPO_PROVV_RIF, "
				+ "DATA_PROVV_RIF, "
				+ "ANNO_PROVV_RIF, "
				+ "NUMERO_PROVV_RIF, "
				+ "COD_TIPO_AUTORITA_PROVV_RIF, CODTIPOAUTORITAPROVVRIF.RV_MEANING DESC_TIPO_AUTORITA_PROVV_RIF, "
				+ "COD_LUOGO_PROVV_RIF, CODLUOGOPROVVRIF.DESCRIZIONE DESC_LUOGO_PROVV_RIF, "
				+ "NUM_SEZIONE_AUTORITA_PROVV_RIF, "
				+ "COD_TIPO_RITO_RIF, CODTIPORITORIF.RV_MEANING DESC_TIPO_RITO_RIF, "
				+ "COD_TIPO_PROVVEDIMENTO_ALTRO, CODTIPOPROVVEDIMENTOALTRO.RV_MEANING DESC_TIPO_PROVVEDIMENTO_ALTRO, "
				+ "NOTE1_DECISIONE_CASSAZIONE, "
				+ "NOTE2_DECISIONE_CASSAZIONE, "
				+ "ANNO_SENTENZA_CASSAZIONE, "
				+ "NUMERO_SENTENZA_CASSAZIONE, "
				+ "ANNO_RACCOLTA_GENERALE, "
				+ "NUMERO_RACCOLTA_GENERALE, "
				+ "COD_TIPO_DECISIONE_CASSAZIONE, CODTIPODECISIONECASSAZIONE.RV_MEANING DESC_TIPO_DECISIONE_CASSAZIONE, "
				+ "TITOLO_CUMULATO.NOTE, " + "TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO, "
				+ "TITOLO_CUMULATO.FLAG_STATO, " + "TITOLO_CUMULATO.MOTIVO_MODIFICA, "
				+ "ID_SENTENZA_ORIGINE," + "FLAG_ESCLUSO," +
                
                  "MESS_ID_MESSAGGIO, TIPO_ISCRIZIONE, "+
                  "DATA_PRESA_IN_CARICO, "+
                  
				"TITOLO_CUMULATO.COD_OPERATORE_INSERIMENTO, " + "TITOLO_CUMULATO.DATA_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_OPERATORE_AGGIORNAMENTO, " + "TITOLO_CUMULATO.DATA_AGGIORNAMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_AGGIORNAMENTO ";

    lStatement += " FROM TITOLO_CUMULATO, BENEFICIO_CUMULO BENECUM ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTO ";
    lStatement +=    " , COMUNE CODSEDENOTIZIAREATO ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAEMITTENTE ";
    lStatement +=    " , COMUNE CODLUOGOEMITTENTE ";
    lStatement +=    " , CG_REF_CODES CODTIPORITO ";    
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAPROVVRIF ";
    lStatement +=    " , COMUNE CODLUOGOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPORITORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTOALTRO ";
    lStatement +=    " , CG_REF_CODES CODTIPODECISIONECASSAZIONE ";    
    lStatement += " WHERE 1=1 ";
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_SEDE_NOTIZIA_REATO,'-') = CODSEDENOTIZIAREATO.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ) "; //TIPO_UFFICIO_EMITTENTE
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO,'-') = CODTIPORITO.RV_LOW_VALUE AND CODTIPORITO.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_RIF,'-') = CODTIPOPROVVEDIMENTORIF.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTORIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVV_RIF,'-') = CODTIPOPROVVRIF.RV_LOW_VALUE AND CODTIPOPROVVRIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_PROVV_RIF,'-') = CODTIPOAUTORITAPROVVRIF.RV_LOW_VALUE AND CODTIPOAUTORITAPROVVRIF.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_PROVV_RIF,'-') = CODLUOGOPROVVRIF.COD_COMUNE  ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO_RIF,'-') = CODTIPORITORIF.RV_LOW_VALUE AND CODTIPORITORIF.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_ALTRO,'-') = CODTIPOPROVVEDIMENTOALTRO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTOALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_DECISIONE_CASSAZIONE,'-') = CODTIPODECISIONECASSAZIONE.RV_LOW_VALUE AND CODTIPODECISIONECASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' ) "; 

    //siesLogger.debug("lStatement = "+lStatement); 
    return lStatement;
  }
  
  
  //TITOLO_CUMULATO Join SANZIONE_SOST_CUM 
	protected String getSqlQueryJoinSanzioneSostCum() {
    String lStatement = new String("");

		lStatement += " SELECT DISTINCT "
				+ "ID_TITOLO_CUMULATO, "
				+ "COD_TIPO_PROVVEDIMENTO, CODTIPOPROVVEDIMENTO.RV_MEANING DESC_TIPO_PROVVEDIMENTO, "
				+ "DATA_IRREVOCABILITA, "
				+ "ANNO_REGE_PM, "
				+ "NUMERO_REGE_PM, "
				+ "COD_SEDE_NOTIZIA_REATO, CODSEDENOTIZIAREATO.DESCRIZIONE DESC_SEDE_NOTIZIA_REATO, "
				+ "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, "
				+ "TIPO_REG_GEN, "
				+ "DATA_PROVVEDIMENTO, "
				+ "ANNO_SENTENZA, "
				+ "NUMERO_SENTENZA, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, CODTIPOAUTORITAEMITTENTE.RV_MEANING DESC_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, CODLUOGOEMITTENTE.DESCRIZIONE DESC_LUOGO_EMITTENTE, "
				+ "NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "COD_TIPO_RITO, CODTIPORITO.RV_MEANING DESC_TIPO_RITO, "
				+ "COD_TIPO_PROVVEDIMENTO_RIF, CODTIPOPROVVEDIMENTORIF.RV_MEANING DESC_TIPO_PROVVEDIMENTO_RIF, "
				+ "COD_TIPO_PROVV_RIF, CODTIPOPROVVRIF.RV_MEANING DESC_TIPO_PROVV_RIF, "
				+ "DATA_PROVV_RIF, "
				+ "ANNO_PROVV_RIF, "
				+ "NUMERO_PROVV_RIF, "
				+ "COD_TIPO_AUTORITA_PROVV_RIF, CODTIPOAUTORITAPROVVRIF.RV_MEANING DESC_TIPO_AUTORITA_PROVV_RIF, "
				+ "COD_LUOGO_PROVV_RIF, CODLUOGOPROVVRIF.DESCRIZIONE DESC_LUOGO_PROVV_RIF, "
				+ "NUM_SEZIONE_AUTORITA_PROVV_RIF, "
				+ "COD_TIPO_RITO_RIF, CODTIPORITORIF.RV_MEANING DESC_TIPO_RITO_RIF, "
				+ "COD_TIPO_PROVVEDIMENTO_ALTRO, CODTIPOPROVVEDIMENTOALTRO.RV_MEANING DESC_TIPO_PROVVEDIMENTO_ALTRO, "
				+ "NOTE1_DECISIONE_CASSAZIONE, "
				+ "NOTE2_DECISIONE_CASSAZIONE, "
				+ "ANNO_SENTENZA_CASSAZIONE, "
				+ "NUMERO_SENTENZA_CASSAZIONE, "
				+ "ANNO_RACCOLTA_GENERALE, "
				+ "NUMERO_RACCOLTA_GENERALE, "
				+ "COD_TIPO_DECISIONE_CASSAZIONE, CODTIPODECISIONECASSAZIONE.RV_MEANING DESC_TIPO_DECISIONE_CASSAZIONE, "
				+ "TITOLO_CUMULATO.NOTE, " + "TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO, "
				+ "TITOLO_CUMULATO.FLAG_STATO, " + "TITOLO_CUMULATO.MOTIVO_MODIFICA, "
				+ "ID_SENTENZA_ORIGINE," + "FLAG_ESCLUSO," +
                
                  "MESS_ID_MESSAGGIO, TIPO_ISCRIZIONE, "+
                  "DATA_PRESA_IN_CARICO, "+
                  
				"TITOLO_CUMULATO.COD_OPERATORE_INSERIMENTO, " + "TITOLO_CUMULATO.DATA_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_OPERATORE_AGGIORNAMENTO, " + "TITOLO_CUMULATO.DATA_AGGIORNAMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_AGGIORNAMENTO ";

    lStatement += " FROM TITOLO_CUMULATO, SANZIONE_SOST_CUM SSCUM ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTO ";
    lStatement +=    " , COMUNE CODSEDENOTIZIAREATO ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAEMITTENTE ";
    lStatement +=    " , COMUNE CODLUOGOEMITTENTE ";
    lStatement +=    " , CG_REF_CODES CODTIPORITO ";    
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAPROVVRIF ";
    lStatement +=    " , COMUNE CODLUOGOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPORITORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTOALTRO ";
    lStatement +=    " , CG_REF_CODES CODTIPODECISIONECASSAZIONE ";    
    lStatement += " WHERE 1=1 ";
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_SEDE_NOTIZIA_REATO,'-') = CODSEDENOTIZIAREATO.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ) "; //TIPO_UFFICIO_EMITTENTE
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO,'-') = CODTIPORITO.RV_LOW_VALUE AND CODTIPORITO.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_RIF,'-') = CODTIPOPROVVEDIMENTORIF.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTORIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVV_RIF,'-') = CODTIPOPROVVRIF.RV_LOW_VALUE AND CODTIPOPROVVRIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_PROVV_RIF,'-') = CODTIPOAUTORITAPROVVRIF.RV_LOW_VALUE AND CODTIPOAUTORITAPROVVRIF.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_PROVV_RIF,'-') = CODLUOGOPROVVRIF.COD_COMUNE  ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO_RIF,'-') = CODTIPORITORIF.RV_LOW_VALUE AND CODTIPORITORIF.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_ALTRO,'-') = CODTIPOPROVVEDIMENTOALTRO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTOALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_DECISIONE_CASSAZIONE,'-') = CODTIPODECISIONECASSAZIONE.RV_LOW_VALUE AND CODTIPODECISIONECASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' ) "; 

    //siesLogger.debug("lStatement = "+lStatement); 
    return lStatement;
  }

  //TITOLO_CUMULATO Join PENA_ACCESSORIA_CUMULO 
	protected String getSqlQueryJoinPACum() {
    String lStatement = new String("");

		lStatement += " SELECT DISTINCT "
				+ "ID_TITOLO_CUMULATO, "
				+ "COD_TIPO_PROVVEDIMENTO, CODTIPOPROVVEDIMENTO.RV_MEANING DESC_TIPO_PROVVEDIMENTO, "
				+ "DATA_IRREVOCABILITA, "
				+ "ANNO_REGE_PM, "
				+ "NUMERO_REGE_PM, "
				+ "COD_SEDE_NOTIZIA_REATO, CODSEDENOTIZIAREATO.DESCRIZIONE DESC_SEDE_NOTIZIA_REATO, "
				+ "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, "
				+ "TIPO_REG_GEN, "
				+ "DATA_PROVVEDIMENTO, "
				+ "ANNO_SENTENZA, "
				+ "NUMERO_SENTENZA, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, CODTIPOAUTORITAEMITTENTE.RV_MEANING DESC_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, CODLUOGOEMITTENTE.DESCRIZIONE DESC_LUOGO_EMITTENTE, "
				+ "NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "COD_TIPO_RITO, CODTIPORITO.RV_MEANING DESC_TIPO_RITO, "
				+ "COD_TIPO_PROVVEDIMENTO_RIF, CODTIPOPROVVEDIMENTORIF.RV_MEANING DESC_TIPO_PROVVEDIMENTO_RIF, "
				+ "COD_TIPO_PROVV_RIF, CODTIPOPROVVRIF.RV_MEANING DESC_TIPO_PROVV_RIF, "
				+ "DATA_PROVV_RIF, "
				+ "ANNO_PROVV_RIF, "
				+ "NUMERO_PROVV_RIF, "
				+ "COD_TIPO_AUTORITA_PROVV_RIF, CODTIPOAUTORITAPROVVRIF.RV_MEANING DESC_TIPO_AUTORITA_PROVV_RIF, "
				+ "COD_LUOGO_PROVV_RIF, CODLUOGOPROVVRIF.DESCRIZIONE DESC_LUOGO_PROVV_RIF, "
				+ "NUM_SEZIONE_AUTORITA_PROVV_RIF, "
				+ "COD_TIPO_RITO_RIF, CODTIPORITORIF.RV_MEANING DESC_TIPO_RITO_RIF, "
				+ "COD_TIPO_PROVVEDIMENTO_ALTRO, CODTIPOPROVVEDIMENTOALTRO.RV_MEANING DESC_TIPO_PROVVEDIMENTO_ALTRO, "
				+ "NOTE1_DECISIONE_CASSAZIONE, "
				+ "NOTE2_DECISIONE_CASSAZIONE, "
				+ "ANNO_SENTENZA_CASSAZIONE, "
				+ "NUMERO_SENTENZA_CASSAZIONE, "
				+ "ANNO_RACCOLTA_GENERALE, "
				+ "NUMERO_RACCOLTA_GENERALE, "
				+ "COD_TIPO_DECISIONE_CASSAZIONE, CODTIPODECISIONECASSAZIONE.RV_MEANING DESC_TIPO_DECISIONE_CASSAZIONE, "
				+ "TITOLO_CUMULATO.NOTE, " + "TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO, "
				+ "TITOLO_CUMULATO.FLAG_STATO, " + "TITOLO_CUMULATO.MOTIVO_MODIFICA, "
				+ "ID_SENTENZA_ORIGINE," + "FLAG_ESCLUSO," +
                
                  "MESS_ID_MESSAGGIO, TIPO_ISCRIZIONE, "+
                  "DATA_PRESA_IN_CARICO, "+
                  
				"TITOLO_CUMULATO.COD_OPERATORE_INSERIMENTO, " + "TITOLO_CUMULATO.DATA_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_OPERATORE_AGGIORNAMENTO, " + "TITOLO_CUMULATO.DATA_AGGIORNAMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_AGGIORNAMENTO ";

    lStatement += " FROM TITOLO_CUMULATO, PENA_ACCESSORIA_CUMULO PACUM ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTO ";
    lStatement +=    " , COMUNE CODSEDENOTIZIAREATO ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAEMITTENTE ";
    lStatement +=    " , COMUNE CODLUOGOEMITTENTE ";
    lStatement +=    " , CG_REF_CODES CODTIPORITO ";    
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAPROVVRIF ";
    lStatement +=    " , COMUNE CODLUOGOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPORITORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTOALTRO ";
    lStatement +=    " , CG_REF_CODES CODTIPODECISIONECASSAZIONE ";    
    lStatement += " WHERE 1=1 ";
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_SEDE_NOTIZIA_REATO,'-') = CODSEDENOTIZIAREATO.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ) "; //TIPO_UFFICIO_EMITTENTE
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO,'-') = CODTIPORITO.RV_LOW_VALUE AND CODTIPORITO.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_RIF,'-') = CODTIPOPROVVEDIMENTORIF.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTORIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVV_RIF,'-') = CODTIPOPROVVRIF.RV_LOW_VALUE AND CODTIPOPROVVRIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_PROVV_RIF,'-') = CODTIPOAUTORITAPROVVRIF.RV_LOW_VALUE AND CODTIPOAUTORITAPROVVRIF.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_PROVV_RIF,'-') = CODLUOGOPROVVRIF.COD_COMUNE  ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO_RIF,'-') = CODTIPORITORIF.RV_LOW_VALUE AND CODTIPORITORIF.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_ALTRO,'-') = CODTIPOPROVVEDIMENTOALTRO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTOALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_DECISIONE_CASSAZIONE,'-') = CODTIPODECISIONECASSAZIONE.RV_LOW_VALUE AND CODTIPODECISIONECASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' ) "; 

    //siesLogger.debug("lStatement = "+lStatement); 
    return lStatement;
  }
  
  //TITOLO_CUMULATO Join STATO_ESEC_TITOLO_CUMULATO 
	protected String getSqlQueryJoinStatoEsecCum() {
    String lStatement = new String("");

		lStatement += " SELECT DISTINCT "
				+ "ID_TITOLO_CUMULATO, "
				+ "TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO, CODTIPOPROVVEDIMENTO.RV_MEANING DESC_TIPO_PROVVEDIMENTO, "
				+ "DATA_IRREVOCABILITA, "
				+ "ANNO_REGE_PM, "
				+ "NUMERO_REGE_PM, "
				+ "COD_SEDE_NOTIZIA_REATO, CODSEDENOTIZIAREATO.DESCRIZIONE DESC_SEDE_NOTIZIA_REATO, "
				+ "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, "
				+ "TIPO_REG_GEN, "
				+ "DATA_PROVVEDIMENTO, "
				+ "ANNO_SENTENZA, "
				+ "NUMERO_SENTENZA, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, CODTIPOAUTORITAEMITTENTE.RV_MEANING DESC_TIPO_AUTORITA_EMITTENTE, "
				+ "TITOLO_CUMULATO.COD_LUOGO_EMITTENTE, CODLUOGOEMITTENTE.DESCRIZIONE DESC_LUOGO_EMITTENTE, "
				+ "NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "COD_TIPO_RITO, CODTIPORITO.RV_MEANING DESC_TIPO_RITO, "
				+ "COD_TIPO_PROVVEDIMENTO_RIF, CODTIPOPROVVEDIMENTORIF.RV_MEANING DESC_TIPO_PROVVEDIMENTO_RIF, "
				+ "COD_TIPO_PROVV_RIF, CODTIPOPROVVRIF.RV_MEANING DESC_TIPO_PROVV_RIF, "
				+ "DATA_PROVV_RIF, "
				+ "ANNO_PROVV_RIF, "
				+ "NUMERO_PROVV_RIF, "
				+ "COD_TIPO_AUTORITA_PROVV_RIF, CODTIPOAUTORITAPROVVRIF.RV_MEANING DESC_TIPO_AUTORITA_PROVV_RIF, "
				+ "COD_LUOGO_PROVV_RIF, CODLUOGOPROVVRIF.DESCRIZIONE DESC_LUOGO_PROVV_RIF, "
				+ "NUM_SEZIONE_AUTORITA_PROVV_RIF, "
				+ "COD_TIPO_RITO_RIF, CODTIPORITORIF.RV_MEANING DESC_TIPO_RITO_RIF, "
				+ "COD_TIPO_PROVVEDIMENTO_ALTRO, CODTIPOPROVVEDIMENTOALTRO.RV_MEANING DESC_TIPO_PROVVEDIMENTO_ALTRO, "
				+ "NOTE1_DECISIONE_CASSAZIONE, "
				+ "NOTE2_DECISIONE_CASSAZIONE, "
				+ "ANNO_SENTENZA_CASSAZIONE, "
				+ "NUMERO_SENTENZA_CASSAZIONE, "
				+ "ANNO_RACCOLTA_GENERALE, "
				+ "NUMERO_RACCOLTA_GENERALE, "
				+ "COD_TIPO_DECISIONE_CASSAZIONE, CODTIPODECISIONECASSAZIONE.RV_MEANING DESC_TIPO_DECISIONE_CASSAZIONE, "
				+ "TITOLO_CUMULATO.NOTE, " + "TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO, "
				+ "TITOLO_CUMULATO.FLAG_STATO, " + "TITOLO_CUMULATO.MOTIVO_MODIFICA, "
				+ "ID_SENTENZA_ORIGINE," + "FLAG_ESCLUSO," +
                
                  "MESS_ID_MESSAGGIO, TIPO_ISCRIZIONE, "+
                  "DATA_PRESA_IN_CARICO, "+
                  
				"TITOLO_CUMULATO.COD_OPERATORE_INSERIMENTO, " + "TITOLO_CUMULATO.DATA_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_OPERATORE_AGGIORNAMENTO, " + "TITOLO_CUMULATO.DATA_AGGIORNAMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_AGGIORNAMENTO ";

    lStatement += " FROM TITOLO_CUMULATO, STATO_ESEC_TITOLO_CUMULATO STESEC ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTO ";
    lStatement +=    " , COMUNE CODSEDENOTIZIAREATO ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAEMITTENTE ";
    lStatement +=    " , COMUNE CODLUOGOEMITTENTE ";
    lStatement +=    " , CG_REF_CODES CODTIPORITO ";    
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAPROVVRIF ";
    lStatement +=    " , COMUNE CODLUOGOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPORITORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTOALTRO ";
    lStatement +=    " , CG_REF_CODES CODTIPODECISIONECASSAZIONE ";    
    lStatement += " WHERE 1=1 ";
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_SEDE_NOTIZIA_REATO,'-') = CODSEDENOTIZIAREATO.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ) "; //TIPO_UFFICIO_EMITTENTE
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO,'-') = CODTIPORITO.RV_LOW_VALUE AND CODTIPORITO.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_RIF,'-') = CODTIPOPROVVEDIMENTORIF.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTORIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVV_RIF,'-') = CODTIPOPROVVRIF.RV_LOW_VALUE AND CODTIPOPROVVRIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_PROVV_RIF,'-') = CODTIPOAUTORITAPROVVRIF.RV_LOW_VALUE AND CODTIPOAUTORITAPROVVRIF.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_PROVV_RIF,'-') = CODLUOGOPROVVRIF.COD_COMUNE  ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO_RIF,'-') = CODTIPORITORIF.RV_LOW_VALUE AND CODTIPORITORIF.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_ALTRO,'-') = CODTIPOPROVVEDIMENTOALTRO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTOALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_DECISIONE_CASSAZIONE,'-') = CODTIPODECISIONECASSAZIONE.RV_LOW_VALUE AND CODTIPODECISIONECASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' ) "; 

    //siesLogger.debug("lStatement = "+lStatement); 
    return lStatement;
  }
  
// ============================  

  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
	 * 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     TitoloCumulatoModel aModel = new  TitoloCumulatoModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdTitoloCumulato            ( getBigDecimal ("ID_TITOLO_CUMULATO"            ) ); 
    aModel.setCodTipoProvvedimento        ( getString     ("COD_TIPO_PROVVEDIMENTO"        ) ); 
aModel.setDescrTipoProvvedimento (getString("DESC_TIPO_PROVVEDIMENTO") );
    aModel.setDataIrrevocabilita          ( getDate       ("DATA_IRREVOCABILITA"           ) ); 
    aModel.setAnnoRegePm                  ( getBigDecimal ("ANNO_REGE_PM"                  ) ); 
    aModel.setNumeroRegePm                ( getString     ("NUMERO_REGE_PM"                ) ); 
    aModel.setCodSedeNotiziaReato         ( getString     ("COD_SEDE_NOTIZIA_REATO"        ) ); 
aModel.setDescrSedeNotiziaReato (getString("DESC_SEDE_NOTIZIA_REATO") );
    aModel.setAnnoRegGen                  ( getBigDecimal ("ANNO_REG_GEN"                  ) ); 
    aModel.setNumeroRegGen                ( getString     ("NUMERO_REG_GEN"                ) ); 
    aModel.setTipoRegGen                  ( getString     ("TIPO_REG_GEN"                  ) ); 
    aModel.setDataProvvedimento           ( getDate       ("DATA_PROVVEDIMENTO"            ) ); 
    aModel.setAnnoSentenza                ( getBigDecimal ("ANNO_SENTENZA"                 ) ); 
    aModel.setNumeroSentenza              ( getString     ("NUMERO_SENTENZA"               ) ); 
    aModel.setCodTipoAutoritaEmittente    ( getString     ("COD_TIPO_AUTORITA_EMITTENTE"   ) ); 
aModel.setDescrTipoAutoritaEmittente (getString("DESC_TIPO_AUTORITA_EMITTENTE") );
    aModel.setCodLuogoEmittente           ( getString     ("COD_LUOGO_EMITTENTE"           ) ); 
aModel.setDescrLuogoEmittente  (getString("DESC_LUOGO_EMITTENTE") );
    aModel.setNumSezioneAutoritaEmittente ( getString     ("NUM_SEZIONE_AUTORITA_EMITTENTE") ); 
    aModel.setCodTipoRito                 ( getString     ("COD_TIPO_RITO"                 ) ); 
aModel.setDescrTipoRito (getString("DESC_TIPO_RITO") );
    aModel.setCodTipoProvvedimentoRif     ( getString     ("COD_TIPO_PROVVEDIMENTO_RIF"    ) ); 
aModel.setDescrTipoProvvedimentoRif(getString("DESC_TIPO_PROVVEDIMENTO_RIF") );
    aModel.setCodTipoProvvRif             ( getString     ("COD_TIPO_PROVV_RIF"            ) ); 
aModel.setDescrTipoProvvRif (getString("DESC_TIPO_PROVV_RIF") );
    aModel.setDataProvvRif                ( getDate       ("DATA_PROVV_RIF"                ) ); 
    aModel.setAnnoProvvRif                ( getBigDecimal ("ANNO_PROVV_RIF"                ) ); 
    aModel.setNumeroProvvRif              ( getString     ("NUMERO_PROVV_RIF"              ) ); 
    aModel.setCodTipoAutoritaProvvRif     ( getString     ("COD_TIPO_AUTORITA_PROVV_RIF"   ) ); 
aModel.setDescrTipoAutoritaProvvRif(getString("DESC_TIPO_AUTORITA_PROVV_RIF") );
    aModel.setCodLuogoProvvRif            ( getString     ("COD_LUOGO_PROVV_RIF"           ) ); 
aModel.setDescrLuogoProvvRif (getString("DESC_LUOGO_PROVV_RIF") );
    aModel.setNumSezioneAutoritaProvvRif  ( getString     ("NUM_SEZIONE_AUTORITA_PROVV_RIF") ); 
    aModel.setCodTipoRitoRif              ( getString     ("COD_TIPO_RITO_RIF"             ) ); 
aModel.setDescrTipoRitoRif (getString("DESC_TIPO_RITO_RIF") );
    aModel.setCodTipoProvvedimentoAltro   ( getString     ("COD_TIPO_PROVVEDIMENTO_ALTRO"  ) ); 
aModel.setDescrTipoProvvedimentoAltro (getString("DESC_TIPO_PROVVEDIMENTO_ALTRO") );
    aModel.setNote1DecisioneCassazione    ( getString     ("NOTE1_DECISIONE_CASSAZIONE"    ) ); 
    aModel.setNote2DecisioneCassazione    ( getString     ("NOTE2_DECISIONE_CASSAZIONE"    ) ); 
    aModel.setAnnoSentenzaCassazione      ( getBigDecimal ("ANNO_SENTENZA_CASSAZIONE"      ) ); 
    aModel.setNumeroSentenzaCassazione    ( getString     ("NUMERO_SENTENZA_CASSAZIONE"    ) ); 
    aModel.setAnnoRaccoltaGenerale        ( getBigDecimal ("ANNO_RACCOLTA_GENERALE"        ) ); 
    aModel.setNumeroRaccoltaGenerale      ( getString     ("NUMERO_RACCOLTA_GENERALE"      ) ); 
    aModel.setCodTipoDecisioneCassazione  ( getString     ("COD_TIPO_DECISIONE_CASSAZIONE" ) ); 
aModel.setDescrTipoDecisioneCassazione(getString("DESC_TIPO_DECISIONE_CASSAZIONE") );
    aModel.setNote                        ( getString     ("NOTE"                          ) ); 
    aModel.setIstrIdIstruttoriaCumulo     ( getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"    ) ); 
    aModel.setFlagStato                   ( getString     ("FLAG_STATO"                    ) ); 
    aModel.setMotivoModifica              ( getString     ("MOTIVO_MODIFICA"               ) ); 
    aModel.setIdSentenzaOrigine           ( getBigDecimal ("ID_SENTENZA_ORIGINE"           ) ); 
    aModel.setFlagEscluso                 ( getString     ("FLAG_ESCLUSO"                  ) ); 
    
    aModel.setMessIdMessaggio			(getBigDecimal	("MESS_ID_MESSAGGIO"				) );
    aModel.setTipoIscrizione			(getString		("TIPO_ISCRIZIONE"					) );
    
    aModel.setDataPresaInCarico  ( getDate       ("DATA_PRESA_IN_CARICO"              ) ); 
    
    aModel.setCodOperatoreInserimento     ( getString     ("COD_OPERATORE_INSERIMENTO"     ) ); 
    aModel.setDataInserimento             ( getDate       ("DATA_INSERIMENTO"              ) ); 
    aModel.setCodUfficioInserimento       ( getString     ("COD_UFFICIO_INSERIMENTO"       ) ); 
    aModel.setCodOperatoreAggiornamento   ( getString     ("COD_OPERATORE_AGGIORNAMENTO"   ) ); 
    aModel.setDataAggiornamento           ( getDate       ("DATA_AGGIORNAMENTO"            ) ); 
    aModel.setCodUfficioAggiornamento     ( getString     ("COD_UFFICIO_AGGIORNAMENTO"     ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
	 * 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(TitoloCumulatoModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdTitoloCumulato() != null ) { 
      lCondizioni += " and ID_TITOLO_CUMULATO = " + aModel.getIdTitoloCumulato() + ""; 
    } 
    if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' "; 
    } 
    if (aModel.getDataIrrevocabilita() != null ) { 
			lCondizioni += " and to_char(DATA_IRREVOCABILITA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataIrrevocabilita(), "dd/MM/yyyy") + "' ";
    } 
    if (aModel.getAnnoRegePm() != null ) { 
      lCondizioni += " and ANNO_REGE_PM = " + aModel.getAnnoRegePm() + ""; 
    } 
    if (aModel.getNumeroRegePm() != null && aModel.getNumeroRegePm().length() > 0) { 
      lCondizioni += " and NUMERO_REGE_PM = '" + aModel.getNumeroRegePm() + "' "; 
    } 
    if (aModel.getCodSedeNotiziaReato() != null && aModel.getCodSedeNotiziaReato().length() > 0) { 
      lCondizioni += " and COD_SEDE_NOTIZIA_REATO = '" + aModel.getCodSedeNotiziaReato() + "' "; 
    } 
    if (aModel.getAnnoRegGen() != null ) { 
      lCondizioni += " and ANNO_REG_GEN = " + aModel.getAnnoRegGen() + ""; 
    } 
    if (aModel.getNumeroRegGen() != null && aModel.getNumeroRegGen().length() > 0) { 
      lCondizioni += " and NUMERO_REG_GEN = '" + aModel.getNumeroRegGen() + "' "; 
    } 
    if (aModel.getTipoRegGen() != null && aModel.getTipoRegGen().length() > 0) { 
      lCondizioni += " and TIPO_REG_GEN = '" + aModel.getTipoRegGen() + "' "; 
    } 
    if (aModel.getDataProvvedimento() != null ) { 
			lCondizioni += " and to_char(DATA_PROVVEDIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataProvvedimento(), "dd/MM/yyyy") + "' ";
    } 
    if (aModel.getAnnoSentenza() != null ) { 
      lCondizioni += " and ANNO_SENTENZA = " + aModel.getAnnoSentenza() + ""; 
    } 
    if (aModel.getNumeroSentenza() != null && aModel.getNumeroSentenza().length() > 0) { 
      lCondizioni += " and NUMERO_SENTENZA = '" + aModel.getNumeroSentenza() + "' "; 
    } 
    if (aModel.getCodTipoAutoritaEmittente() != null && aModel.getCodTipoAutoritaEmittente().length() > 0) { 
			lCondizioni += " and COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente()
					+ "' ";
    } 
    if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) { 
      lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' "; 
    } 
		if (aModel.getNumSezioneAutoritaEmittente() != null
				&& aModel.getNumSezioneAutoritaEmittente().length() > 0) {
			lCondizioni += " and NUM_SEZIONE_AUTORITA_EMITTENTE = '"
					+ aModel.getNumSezioneAutoritaEmittente() + "' ";
    } 
    if (aModel.getCodTipoRito() != null && aModel.getCodTipoRito().length() > 0) { 
      lCondizioni += " and COD_TIPO_RITO = '" + aModel.getCodTipoRito() + "' "; 
    } 
    if (aModel.getCodTipoProvvedimentoRif() != null && aModel.getCodTipoProvvedimentoRif().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO_RIF = '" + aModel.getCodTipoProvvedimentoRif() + "' "; 
    } 
    if (aModel.getCodTipoProvvRif() != null && aModel.getCodTipoProvvRif().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVV_RIF = '" + aModel.getCodTipoProvvRif() + "' "; 
    } 
    if (aModel.getDataProvvRif() != null ) { 
			lCondizioni += " and to_char(DATA_PROVV_RIF,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataProvvRif(), "dd/MM/yyyy") + "' ";
    } 
    if (aModel.getAnnoProvvRif() != null ) { 
      lCondizioni += " and ANNO_PROVV_RIF = " + aModel.getAnnoProvvRif() + ""; 
    } 
    if (aModel.getNumeroProvvRif() != null && aModel.getNumeroProvvRif().length() > 0) { 
      lCondizioni += " and NUMERO_PROVV_RIF = '" + aModel.getNumeroProvvRif() + "' "; 
    } 
    if (aModel.getCodTipoAutoritaProvvRif() != null && aModel.getCodTipoAutoritaProvvRif().length() > 0) { 
			lCondizioni += " and COD_TIPO_AUTORITA_PROVV_RIF = '" + aModel.getCodTipoAutoritaProvvRif()
					+ "' ";
    } 
    if (aModel.getCodLuogoProvvRif() != null && aModel.getCodLuogoProvvRif().length() > 0) { 
      lCondizioni += " and COD_LUOGO_PROVV_RIF = '" + aModel.getCodLuogoProvvRif() + "' "; 
    } 
		if (aModel.getNumSezioneAutoritaProvvRif() != null
				&& aModel.getNumSezioneAutoritaProvvRif().length() > 0) {
			lCondizioni += " and NUM_SEZIONE_AUTORITA_PROVV_RIF = '" + aModel.getNumSezioneAutoritaProvvRif()
					+ "' ";
    } 
    if (aModel.getCodTipoRitoRif() != null && aModel.getCodTipoRitoRif().length() > 0) { 
      lCondizioni += " and COD_TIPO_RITO_RIF = '" + aModel.getCodTipoRitoRif() + "' "; 
    } 
		if (aModel.getCodTipoProvvedimentoAltro() != null
				&& aModel.getCodTipoProvvedimentoAltro().length() > 0) {
			lCondizioni += " and COD_TIPO_PROVVEDIMENTO_ALTRO = '" + aModel.getCodTipoProvvedimentoAltro()
					+ "' ";
    } 
    if (aModel.getNote1DecisioneCassazione() != null && aModel.getNote1DecisioneCassazione().length() > 0) { 
			lCondizioni += " and NOTE1_DECISIONE_CASSAZIONE = '" + aModel.getNote1DecisioneCassazione()
					+ "' ";
    } 
    if (aModel.getNote2DecisioneCassazione() != null && aModel.getNote2DecisioneCassazione().length() > 0) { 
			lCondizioni += " and NOTE2_DECISIONE_CASSAZIONE = '" + aModel.getNote2DecisioneCassazione()
					+ "' ";
    } 
    if (aModel.getAnnoSentenzaCassazione() != null ) { 
      lCondizioni += " and ANNO_SENTENZA_CASSAZIONE = " + aModel.getAnnoSentenzaCassazione() + ""; 
    } 
    if (aModel.getNumeroSentenzaCassazione() != null && aModel.getNumeroSentenzaCassazione().length() > 0) { 
			lCondizioni += " and NUMERO_SENTENZA_CASSAZIONE = '" + aModel.getNumeroSentenzaCassazione()
					+ "' ";
    } 
    if (aModel.getAnnoRaccoltaGenerale() != null ) { 
      lCondizioni += " and ANNO_RACCOLTA_GENERALE = " + aModel.getAnnoRaccoltaGenerale() + ""; 
    } 
    if (aModel.getNumeroRaccoltaGenerale() != null && aModel.getNumeroRaccoltaGenerale().length() > 0) { 
      lCondizioni += " and NUMERO_RACCOLTA_GENERALE = '" + aModel.getNumeroRaccoltaGenerale() + "' "; 
    } 
		if (aModel.getCodTipoDecisioneCassazione() != null
				&& aModel.getCodTipoDecisioneCassazione().length() > 0) {
			lCondizioni += " and COD_TIPO_DECISIONE_CASSAZIONE = '" + aModel.getCodTipoDecisioneCassazione()
					+ "' ";
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    
    if (aModel.getIstrIdIstruttoriaCumulo() != null ) { 
      lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + ""; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getIdSentenzaOrigine() != null ) { 
      lCondizioni += " and ID_SENTENZA_ORIGINE = " + aModel.getIdSentenzaOrigine() + ""; 
    } 
    if (aModel.getFlagEscluso() != null && aModel.getFlagEscluso().length() > 0) { 
      lCondizioni += " and FLAG_ESCLUSO = '" + aModel.getFlagEscluso() + "' "; 
    } 
    
    if (aModel.getDataPresaInCarico() != null ) { 
      lCondizioni += " and to_char(DATA_PRESA_IN_CARICO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataPresaInCarico(),"dd/MM/yyyy") + "' "; 
    } 
    
    if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' "; 
    } 
    if (aModel.getDataInserimento() != null ) { 
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
    } 
    if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' "; 
    } 
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
    } 
    if (aModel.getDataAggiornamento() != null ) { 
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
    } 
    if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' "; 
    } 

    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
	 * 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( BigDecimal aIdTitoloCumulato  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_TITOLO_CUMULATO = " + aIdTitoloCumulato;

    return lCondizioni;
  }


  /***************************************************************************** 
   * Metodo per la costruzione della sezione order by 
	 * 
   * @return 
   ****************************************************************************/ 
  protected String getOrderBy() { 
    String orderBy = new String(""); 
    //orderBy = " ORDER BY "; 
    return orderBy; 
  } 
  
	public String setCondizioniDataRea(Date aDataRea) {
	  	String lcond = "<= TO_DATE('" + DateUtils.getDateToString(aDataRea, "ddMMyyyy") + "', 'DDMMYYYY')";
	    String lCondizioni = new String();

	    lCondizioni += " and REATOC.TIT_ID_TITOLO_CUMULATO = ID_TITOLO_CUMULATO ";
	    lCondizioni += " and ( "; 
	    
   		lCondizioni += " ( (REATOC.DATA_INIZIO is not null) and (REATOC.DATA_INIZIO " + lcond + " ) ) ";
		lCondizioni += "  or ((REATOC.DATA_INIZIO is null) and (REATOC.ANNO_INIZIO is not null) and (REATOC.MESE_INIZIO is not null) and (REATOC.MESE_INIZIO <10 ) and (TO_DATE('0'||REATOC.MESE_INIZIO||REATOC.ANNO_INIZIO, 'MMYYYY') "
				+ lcond + " )) ";
		lCondizioni += "  or ((REATOC.DATA_INIZIO is null) and (REATOC.ANNO_INIZIO is not null) and (REATOC.MESE_INIZIO is not null) and (REATOC.MESE_INIZIO >=10 ) and (TO_DATE(REATOC.MESE_INIZIO||REATOC.ANNO_INIZIO, 'MMYYYY') "
				+ lcond + " )) ";
		lCondizioni += "  or ((REATOC.DATA_INIZIO is null) and (REATOC.ANNO_INIZIO is not null) and (REATOC.MESE_INIZIO is null) and (TO_DATE(REATOC.ANNO_INIZIO, 'YYYY') "
				+ lcond + " )) ";
   		lCondizioni += "  or ( REATOC.DATA_INIZIO is null and REATOC.ANNO_INIZIO is null and REATOC.MESE_INIZIO is null and REATOC.GIORNO_INIZIO is null ) ";
		
   		    lCondizioni += " ) ";

	    return lCondizioni;
  }

  // La Query Seleziona i Titoli_Cumulati che hanno almeno una Misura_Sicurezza aggregata;
  // NON viene estratto nessun campo di MISURA_SICUREZZA_CUMULO
	public void ricercaTitoloCumulatoJoinMisuraSicCumByIstruttoriaOrderBy(BigDecimal aIdIstruttoria,
			String aOrdinamento) throws DAOException {
	    // Recupera la select...from 
	    String lSql = getSqlQueryJoinMisuraSicCum();

	    lSql+= " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria ;
	    lSql+= " AND MISURASICCUM.TIT_ID_TITOLO_CUMULATO = ID_TITOLO_CUMULATO ";
	    
		if (aOrdinamento != null) {
	      if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_ASC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC))
	        lSql += " ORDER BY DATA_IRREVOCABILITA DESC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_ASC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO ASC "; 
	      else if (aOrdinamento.equals(ICostantiIstruttoriaCumulo.ORDER_BY_DATA_PROVVEDIMENTO_DESC))
	        lSql += " ORDER BY DATA_PROVVEDIMENTO DESC "; 
	    } 
	    
	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
	    siesLogger.debug("--XX-- lSqlQueryJoinMisuraSicCum = "+lSql);
  }

	protected String getSqlQueryJoinMisuraSicCum() {
    String lStatement = new String("");

		lStatement += " SELECT DISTINCT "
				+ "ID_TITOLO_CUMULATO, "
				+ "COD_TIPO_PROVVEDIMENTO, CODTIPOPROVVEDIMENTO.RV_MEANING DESC_TIPO_PROVVEDIMENTO, "
				+ "DATA_IRREVOCABILITA, "
				+ "ANNO_REGE_PM, "
				+ "NUMERO_REGE_PM, "
				+ "COD_SEDE_NOTIZIA_REATO, CODSEDENOTIZIAREATO.DESCRIZIONE DESC_SEDE_NOTIZIA_REATO, "
				+ "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, "
				+ "TIPO_REG_GEN, "
				+ "DATA_PROVVEDIMENTO, "
				+ "ANNO_SENTENZA, "
				+ "NUMERO_SENTENZA, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, CODTIPOAUTORITAEMITTENTE.RV_MEANING DESC_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, CODLUOGOEMITTENTE.DESCRIZIONE DESC_LUOGO_EMITTENTE, "
				+ "NUM_SEZIONE_AUTORITA_EMITTENTE, "
				+ "COD_TIPO_RITO, CODTIPORITO.RV_MEANING DESC_TIPO_RITO, "
				+ "COD_TIPO_PROVVEDIMENTO_RIF, CODTIPOPROVVEDIMENTORIF.RV_MEANING DESC_TIPO_PROVVEDIMENTO_RIF, "
				+ "COD_TIPO_PROVV_RIF, CODTIPOPROVVRIF.RV_MEANING DESC_TIPO_PROVV_RIF, "
				+ "DATA_PROVV_RIF, "
				+ "ANNO_PROVV_RIF, "
				+ "NUMERO_PROVV_RIF, "
				+ "COD_TIPO_AUTORITA_PROVV_RIF, CODTIPOAUTORITAPROVVRIF.RV_MEANING DESC_TIPO_AUTORITA_PROVV_RIF, "
				+ "COD_LUOGO_PROVV_RIF, CODLUOGOPROVVRIF.DESCRIZIONE DESC_LUOGO_PROVV_RIF, "
				+ "NUM_SEZIONE_AUTORITA_PROVV_RIF, "
				+ "COD_TIPO_RITO_RIF, CODTIPORITORIF.RV_MEANING DESC_TIPO_RITO_RIF, "
				+ "COD_TIPO_PROVVEDIMENTO_ALTRO, CODTIPOPROVVEDIMENTOALTRO.RV_MEANING DESC_TIPO_PROVVEDIMENTO_ALTRO, "
				+ "NOTE1_DECISIONE_CASSAZIONE, "
				+ "NOTE2_DECISIONE_CASSAZIONE, "
				+ "ANNO_SENTENZA_CASSAZIONE, "
				+ "NUMERO_SENTENZA_CASSAZIONE, "
				+ "ANNO_RACCOLTA_GENERALE, "
				+ "NUMERO_RACCOLTA_GENERALE, "
				+ "COD_TIPO_DECISIONE_CASSAZIONE, CODTIPODECISIONECASSAZIONE.RV_MEANING DESC_TIPO_DECISIONE_CASSAZIONE, "
				+ "TITOLO_CUMULATO.NOTE, " + "TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO, "
				+ "TITOLO_CUMULATO.FLAG_STATO, " + "TITOLO_CUMULATO.MOTIVO_MODIFICA, "
				+ "ID_SENTENZA_ORIGINE," + "FLAG_ESCLUSO," +
                
                  "MESS_ID_MESSAGGIO, TIPO_ISCRIZIONE, "+
                  "DATA_PRESA_IN_CARICO, "+
                  
				"TITOLO_CUMULATO.COD_OPERATORE_INSERIMENTO, " + "TITOLO_CUMULATO.DATA_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_INSERIMENTO, "
				+ "TITOLO_CUMULATO.COD_OPERATORE_AGGIORNAMENTO, " + "TITOLO_CUMULATO.DATA_AGGIORNAMENTO, "
				+ "TITOLO_CUMULATO.COD_UFFICIO_AGGIORNAMENTO ";

    lStatement += " FROM TITOLO_CUMULATO, MISURA_SICUREZZA_CUMULO MISURASICCUM ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTO ";
    lStatement +=    " , COMUNE CODSEDENOTIZIAREATO ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAEMITTENTE ";
    lStatement +=    " , COMUNE CODLUOGOEMITTENTE ";
    lStatement +=    " , CG_REF_CODES CODTIPORITO ";    
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOAUTORITAPROVVRIF ";
    lStatement +=    " , COMUNE CODLUOGOPROVVRIF ";
    lStatement +=    " , CG_REF_CODES CODTIPORITORIF ";
    lStatement +=    " , CG_REF_CODES CODTIPOPROVVEDIMENTOALTRO ";
    lStatement +=    " , CG_REF_CODES CODTIPODECISIONECASSAZIONE ";    
    lStatement += " WHERE 1=1 ";
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_SEDE_NOTIZIA_REATO,'-') = CODSEDENOTIZIAREATO.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ) "; //TIPO_UFFICIO_EMITTENTE
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO,'-') = CODTIPORITO.RV_LOW_VALUE AND CODTIPORITO.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_RIF,'-') = CODTIPOPROVVEDIMENTORIF.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTORIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVV_RIF,'-') = CODTIPOPROVVRIF.RV_LOW_VALUE AND CODTIPOPROVVRIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_AUTORITA_PROVV_RIF,'-') = CODTIPOAUTORITAPROVVRIF.RV_LOW_VALUE AND CODTIPOAUTORITAPROVVRIF.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_LUOGO_PROVV_RIF,'-') = CODLUOGOPROVVRIF.COD_COMUNE  ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_RITO_RIF,'-') = CODTIPORITORIF.RV_LOW_VALUE AND CODTIPORITORIF.RV_DOMAIN = 'TIPO_RITO_SENTENZA' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_PROVVEDIMENTO_ALTRO,'-') = CODTIPOPROVVEDIMENTOALTRO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTOALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 
    lStatement +=   " AND ( nvl(TITOLO_CUMULATO.COD_TIPO_DECISIONE_CASSAZIONE,'-') = CODTIPODECISIONECASSAZIONE.RV_LOW_VALUE AND CODTIPODECISIONECASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' ) "; 

    //siesLogger.debug("lStatement = "+lStatement); 
    return lStatement;
  }
  
} // Chiude TitoloCumulatoSqlDao