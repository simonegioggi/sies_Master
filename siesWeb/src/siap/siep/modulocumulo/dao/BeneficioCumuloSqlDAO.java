package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.model.GenericModel;

import siap.dao.SIAPSqlDAO;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;

/**
* <p>Title: BeneficioCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Beneficio_Cumulo</p>
* @version 1.0
*/

public class BeneficioCumuloSqlDAO extends SIAPSqlDAO
{
  public BeneficioCumuloSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //

  public void ricercaBeneficioCumulo(BeneficioCumuloModel aModel) throws DAOException
  {
    String lSql = getSqlQueryCumulo();

    lSql += " " + setCondizione(aModel);
    lSql += " ORDER BY COD_NATURA_BENEFICIO";

    setStatement(lSql);
  }

  public void ricercaBeneficioByKeyFascicolo(BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQueryCumulo();

    lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP= " + aKey;
    lSql += " ORDER BY COD_NATURA_BENEFICIO";
    setStatement(lSql);
  }

  public void ricercaBeneficioCumuloByKey(BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQueryCumulo();

    lSql += " " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  public void ricercaBeneficioCumuloByBenIdBeneficioCum(BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQueryCumulo();

    lSql += " " + setCondizioniByBenIdBeneficioCumulo(aKey);
    setStatement(lSql);
  }
  
  public void ricercaBeneficioCumuloByKeyBeneficioOrig(BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQueryCumulo();

    lSql += " " + setCondizioneByKeyOrig(aKey);
    setStatement(lSql);
  }
  
  public void ricercaBeneficioCumuloByTitoloCum(BigDecimal aKey, String aCodNatBen, Vector<String> aCodTipiBen) throws DAOException
  {
    String lSql = getSqlQueryCumulo();

    lSql += " " + setCondizioneByTitoloCum(aKey);
    
    if( aCodNatBen!= null && !"".equals(aCodNatBen) && !("null").equals(aCodNatBen))
    {	
    	lSql += " and COD_NATURA_BENEFICIO = '" + aCodNatBen +"' ";
    }
    
    if (aCodTipiBen!=null && aCodTipiBen.size()>0)
    {
    	lSql += " AND COD_TIPO_BENEFICIO in (";
    	for (int i=0;i<aCodTipiBen.size();i++) 
    	{
    		lSql += " '"+aCodTipiBen.elementAt(i)+"'";
    		if (i<aCodTipiBen.size()-1)
    		{	
    			lSql += ",";
    		}	
    	}
          
    	lSql += " ) ";      
    }
    
    setStatement(lSql);
  }
  
  // Ricerca BENEFICIO_CUMULO per Tit_Id_Titolo_Cumulato in Join con RICHPM_BENEFICIO_CUM
  public void ricercaBeneficioCumuloByIdTitoloCumRichGE(BigDecimal aKeyTitolo, BigDecimal aKeyRichiesta) throws DAOException
  {
    String lSql = getSqlQueryJoinRichiestaGE();

    lSql += " " + setCondizioneByTitoloCum(aKeyTitolo);
    
    lSql += " AND RICHPM_BENEFICIO_CUM.RIC_ID_RICHIESTE_PM_IN_CUMULO = "+aKeyRichiesta;
    lSql += " AND RICHPM_BENEFICIO_CUM.BEN_ID_BENEFICIO_CUM = ID_BENEFICIO_CUMULO ";
    
    setStatement(lSql);
  }
  
  
  public void ricercaBeneficioCumuloByIdIstruttoria (BigDecimal aIdIstruttoria) throws DAOException
  {
    String lSql = getSqlQueryCumuloJoinTitolo();

    lSql += " AND TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria;
    
    setStatement(lSql);
  }
  
//============================================================
//	BENEFICIO_CUMULO
//============================================================  
protected String getSqlQueryCumulo()
{
	String lStatement = new String("");

	lStatement += " SELECT " +
            "ID_BENEFICIO_CUMULO, "+
            "COD_NATURA_BENEFICIO, "+
            "COD_TIPO_BENEFICIO, "+
            "COD_TIPO_SOSP_SUBORDINATA, "+
            "NUM_ANNI_RECLUSIONE, "+
            "NUM_MESI_RECLUSIONE, "+
            "NUM_GIORNI_RECLUSIONE, "+
            "IMPORTO_MULTA, "+
            "NUM_ANNI_ARRESTO, "+
            "NUM_MESI_ARRESTO, "+
            "NUM_GIORNI_ARRESTO, "+
            "IMPORTO_AMMENDA, "+
            "COD_DPR, "+
            "NOTE, "+
            "COD_OPERATORE_INSERIMENTO, "+
            "DATA_INSERIMENTO, "+
            "COD_UFFICIO_INSERIMENTO, "+
            "COD_OPERATORE_AGGIORNAMENTO, "+
            "DATA_AGGIORNAMENTO, "+
            "COD_UFFICIO_AGGIORNAMENTO, "+
            "D_NATBEN.RV_MEANING DESCNATBEN, "+
            "D_TIPBEN.RV_MEANING DESCTIPBEN,"+
            "D_TIPSOSPSUB.RV_MEANING DESCSOSPSUB, "+
            "D_DPR.RV_MEANING DESCDPR, "+
		      "COD_SOTTOTIPO_BENEFICIO, "+	
		      "SOTTOTIPO.RV_MEANING DESCSOTTO, "+
		      "NUM_ANNI_SOSPENSIONE, "+
		      "NUM_MESI_PRESTAZIONE, "+
		      "NUM_GIORNI_PRESTAZIONE, "+
		      "NUM_ORE_SETTIMANALI, "+
			  "FLAG_FREQUENZA_SETTIMANALE, "+
			  "RIF_ID_PROVVEDIMENTO, "+
			  "RIF_COD_TIPO_PROVVEDIMENTO, "+
			  "D_TIPPRO.RV_MEANING DESCTIPPRO, "+
			  "RIF_DATA_PROVVEDIMENTO, "+
			  "RIF_COD_TIPO_AUTO_EMITTENTE, "+
			  "RIF_COD_LUOGO_EMITTENTE, "+
			  "D_UFFICIOEMI.RV_MEANING DESCRTIPAUTEMI, "+
			  "C.DESCRIZIONE, "+
			  "RIF_NUM_SEZIONE_AUTO_EMITTENTE, "+
			  "RIF_ANNO_PROVVEDIMENTO, "	+		
			  "RIF_NUMERO_PROVVEDIMENTO, "+ 
			  "BEN_ID_BENEFICIO_CUMULO, "+
			  "RIF_DATA_IRREVOCABILITA, "+
			  "NUM_ANNI_ADEMPIMENTO, "+
			  "NUM_MESI_ADEMPIMENTO, "+
			  "NUM_GIORNI_ADEMPIMENTO, "+ 
			  "ENTE_INCARICATO, "+
			  "ID_BENEFICIO_ORIGINE, "+
			  "BEN_ID_BENEFICIO_ORIG, "+
			  "FLAG_STATO, "+
			  "MOTIVO_MODIFICA, "+
			  "TIT_ID_TITOLO_CUMULATO, "+
			  "TIT_ID_TITOLO_CUMULO_COLLEGATO"
			  ; 
	lStatement += " FROM ";
	lStatement += " BENEFICIO_CUMULO, COMUNE C,"; 
	lStatement += " CG_REF_CODES D_NATBEN, CG_REF_CODES D_TIPBEN, CG_REF_CODES SOTTOTIPO, CG_REF_CODES D_TIPSOSPSUB,";
	lStatement += " CG_REF_CODES D_DPR, CG_REF_CODES D_TIPPRO, CG_REF_CODES D_UFFICIOEMI";
	lStatement += " WHERE ";
	lStatement += " D_NATBEN.RV_DOMAIN = 'NATURA_BENEFICIO' AND D_NATBEN.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_NATURA_BENEFICIO ";
	lStatement += " AND D_TIPBEN.RV_DOMAIN = 'TIPO_BENEFICIO' AND D_TIPBEN.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_TIPO_BENEFICIO  ";
	lStatement += " AND D_TIPSOSPSUB.RV_DOMAIN = 'TIPO_SOSP_SUBORDINATA' AND D_TIPSOSPSUB.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_TIPO_SOSP_SUBORDINATA  ";
	lStatement += " AND D_DPR.RV_DOMAIN = 'DPR' AND D_DPR.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_DPR  ";
	lStatement += " AND D_TIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND D_TIPPRO.RV_LOW_VALUE = BENEFICIO_CUMULO.RIF_COD_TIPO_PROVVEDIMENTO  ";
	lStatement += " AND D_UFFICIOEMI.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' AND D_UFFICIOEMI.RV_LOW_VALUE = BENEFICIO_CUMULO.RIF_COD_TIPO_AUTO_EMITTENTE  ";
	lStatement += " AND BENEFICIO_CUMULO.RIF_COD_LUOGO_EMITTENTE = C.COD_COMUNE(+)";
	lStatement += " AND SOTTOTIPO.RV_DOMAIN='SOTTOTIPO_BENEFICIO' AND  NVL ( beneficio_cumulo.cod_sottotipo_beneficio, '-') = sottotipo.rv_low_value ";

	return lStatement;

}  

// Query  BENEFICIO_CUMULO in Join con RICHPM_BENEFICIO_CUM
protected String getSqlQueryJoinRichiestaGE()
{
	String lStatement = new String("");

	lStatement += " SELECT " +
            "ID_BENEFICIO_CUMULO, "+
            "COD_NATURA_BENEFICIO, "+
            "COD_TIPO_BENEFICIO, "+
            "COD_TIPO_SOSP_SUBORDINATA, "+
            "NUM_ANNI_RECLUSIONE, "+
            "NUM_MESI_RECLUSIONE, "+
            "NUM_GIORNI_RECLUSIONE, "+
            "IMPORTO_MULTA, "+
            "NUM_ANNI_ARRESTO, "+
            "NUM_MESI_ARRESTO, "+
            "NUM_GIORNI_ARRESTO, "+
            "IMPORTO_AMMENDA, "+
            "COD_DPR, "+
            "NOTE, "+
            "COD_OPERATORE_INSERIMENTO, "+
            "DATA_INSERIMENTO, "+
            "COD_UFFICIO_INSERIMENTO, "+
            "COD_OPERATORE_AGGIORNAMENTO, "+
            "DATA_AGGIORNAMENTO, "+
            "COD_UFFICIO_AGGIORNAMENTO, "+
            "D_NATBEN.RV_MEANING DESCNATBEN, "+
            "D_TIPBEN.RV_MEANING DESCTIPBEN,"+
            "D_TIPSOSPSUB.RV_MEANING DESCSOSPSUB, "+
            "D_DPR.RV_MEANING DESCDPR, "+
		      "COD_SOTTOTIPO_BENEFICIO, "+	
		      "SOTTOTIPO.RV_MEANING DESCSOTTO, "+
		      "NUM_ANNI_SOSPENSIONE, "+
		      "NUM_MESI_PRESTAZIONE, "+
		      "NUM_GIORNI_PRESTAZIONE, "+
		      "NUM_ORE_SETTIMANALI, "+
			  "FLAG_FREQUENZA_SETTIMANALE, "+
			  "RIF_ID_PROVVEDIMENTO, "+
			  "RIF_COD_TIPO_PROVVEDIMENTO, "+
			  "D_TIPPRO.RV_MEANING DESCTIPPRO, "+
			  "RIF_DATA_PROVVEDIMENTO, "+
			  "RIF_COD_TIPO_AUTO_EMITTENTE, "+
			  "RIF_COD_LUOGO_EMITTENTE, "+
			  "D_UFFICIOEMI.RV_MEANING DESCRTIPAUTEMI, "+
			  "C.DESCRIZIONE, "+
			  "RIF_NUM_SEZIONE_AUTO_EMITTENTE, "+
			  "RIF_ANNO_PROVVEDIMENTO, "	+		
			  "RIF_NUMERO_PROVVEDIMENTO, "+ 
			  "BEN_ID_BENEFICIO_CUMULO, "+
			  "RIF_DATA_IRREVOCABILITA, "+
			  "NUM_ANNI_ADEMPIMENTO, "+
			  "NUM_MESI_ADEMPIMENTO, "+
			  "NUM_GIORNI_ADEMPIMENTO, "+ 
			  "ENTE_INCARICATO, "+
			  "ID_BENEFICIO_ORIGINE, "+
			  "BEN_ID_BENEFICIO_ORIG, "+
			  "FLAG_STATO, "+
			  "MOTIVO_MODIFICA, "+
			  "TIT_ID_TITOLO_CUMULATO, "+
			  "TIT_ID_TITOLO_CUMULO_COLLEGATO"
			  ; 
	lStatement += " FROM ";
	lStatement += " BENEFICIO_CUMULO, RICHPM_BENEFICIO_CUM, COMUNE C,"; 
	lStatement += " CG_REF_CODES D_NATBEN, CG_REF_CODES D_TIPBEN, CG_REF_CODES SOTTOTIPO, CG_REF_CODES D_TIPSOSPSUB,";
	lStatement += " CG_REF_CODES D_DPR, CG_REF_CODES D_TIPPRO, CG_REF_CODES D_UFFICIOEMI";
	lStatement += " WHERE ";
	lStatement += " D_NATBEN.RV_DOMAIN = 'NATURA_BENEFICIO' AND D_NATBEN.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_NATURA_BENEFICIO ";
	lStatement += " AND D_TIPBEN.RV_DOMAIN = 'TIPO_BENEFICIO' AND D_TIPBEN.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_TIPO_BENEFICIO  ";
	lStatement += " AND D_TIPSOSPSUB.RV_DOMAIN = 'TIPO_SOSP_SUBORDINATA' AND D_TIPSOSPSUB.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_TIPO_SOSP_SUBORDINATA  ";
	lStatement += " AND D_DPR.RV_DOMAIN = 'DPR' AND D_DPR.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_DPR  ";
	lStatement += " AND D_TIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND D_TIPPRO.RV_LOW_VALUE = BENEFICIO_CUMULO.RIF_COD_TIPO_PROVVEDIMENTO  ";
	lStatement += " AND D_UFFICIOEMI.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' AND D_UFFICIOEMI.RV_LOW_VALUE = BENEFICIO_CUMULO.RIF_COD_TIPO_AUTO_EMITTENTE  ";
	lStatement += " AND BENEFICIO_CUMULO.RIF_COD_LUOGO_EMITTENTE = C.COD_COMUNE(+)";
	lStatement += " AND SOTTOTIPO.RV_DOMAIN='SOTTOTIPO_BENEFICIO' AND  NVL ( beneficio_cumulo.cod_sottotipo_beneficio, '-') = sottotipo.rv_low_value ";

	return lStatement;

}

protected String getSqlQueryCumuloJoinTitolo()
{
  String lStatement = new String("");

  lStatement += " SELECT " +
                "BENEFICIO_CUMULO.ID_BENEFICIO_CUMULO, "+
                "BENEFICIO_CUMULO.COD_NATURA_BENEFICIO, "+
                "BENEFICIO_CUMULO.COD_TIPO_BENEFICIO, "+
                "BENEFICIO_CUMULO.COD_TIPO_SOSP_SUBORDINATA, "+
                "BENEFICIO_CUMULO.NUM_ANNI_RECLUSIONE, "+
                "BENEFICIO_CUMULO.NUM_MESI_RECLUSIONE, "+
                "BENEFICIO_CUMULO.NUM_GIORNI_RECLUSIONE, "+
                "BENEFICIO_CUMULO.IMPORTO_MULTA, "+
                "BENEFICIO_CUMULO.NUM_ANNI_ARRESTO, "+
                "BENEFICIO_CUMULO.NUM_MESI_ARRESTO, "+
                "BENEFICIO_CUMULO.NUM_GIORNI_ARRESTO, "+
                "BENEFICIO_CUMULO.IMPORTO_AMMENDA, "+
                "BENEFICIO_CUMULO.COD_DPR, "+
                "BENEFICIO_CUMULO.NOTE, "+
                "BENEFICIO_CUMULO.COD_OPERATORE_INSERIMENTO, "+
                "BENEFICIO_CUMULO.DATA_INSERIMENTO, "+
                "BENEFICIO_CUMULO.COD_UFFICIO_INSERIMENTO, "+
                "BENEFICIO_CUMULO.COD_OPERATORE_AGGIORNAMENTO, "+
                "BENEFICIO_CUMULO.DATA_AGGIORNAMENTO, "+
                "BENEFICIO_CUMULO.COD_UFFICIO_AGGIORNAMENTO, "+
            "D_NATBEN.RV_MEANING DESCNATBEN, "+
            "D_TIPBEN.RV_MEANING DESCTIPBEN,"+
            "D_TIPSOSPSUB.RV_MEANING DESCSOSPSUB, "+
            "D_DPR.RV_MEANING DESCDPR, "+
                "BENEFICIO_CUMULO.COD_SOTTOTIPO_BENEFICIO, "+  
            "SOTTOTIPO.RV_MEANING DESCSOTTO, "+
                "BENEFICIO_CUMULO.NUM_ANNI_SOSPENSIONE, "+
                "BENEFICIO_CUMULO.NUM_MESI_PRESTAZIONE, "+
                "BENEFICIO_CUMULO.NUM_GIORNI_PRESTAZIONE, "+
                "BENEFICIO_CUMULO.NUM_ORE_SETTIMANALI, "+
                "BENEFICIO_CUMULO.FLAG_FREQUENZA_SETTIMANALE, "+
                "BENEFICIO_CUMULO.RIF_ID_PROVVEDIMENTO, "+
                "BENEFICIO_CUMULO.RIF_COD_TIPO_PROVVEDIMENTO, "+
            "D_TIPPRO.RV_MEANING DESCTIPPRO, "+
                "BENEFICIO_CUMULO.RIF_DATA_PROVVEDIMENTO, "+
                "BENEFICIO_CUMULO.RIF_COD_TIPO_AUTO_EMITTENTE, "+
                "BENEFICIO_CUMULO.RIF_COD_LUOGO_EMITTENTE, "+
            "D_UFFICIOEMI.RV_MEANING DESCRTIPAUTEMI, "+
            "C.DESCRIZIONE, "+
                "BENEFICIO_CUMULO.RIF_NUM_SEZIONE_AUTO_EMITTENTE, "+
                "BENEFICIO_CUMULO.RIF_ANNO_PROVVEDIMENTO, "  +   
                "BENEFICIO_CUMULO.RIF_NUMERO_PROVVEDIMENTO, "+ 
                "BENEFICIO_CUMULO.BEN_ID_BENEFICIO_CUMULO, "+
                "BENEFICIO_CUMULO.RIF_DATA_IRREVOCABILITA, "+
                "BENEFICIO_CUMULO.NUM_ANNI_ADEMPIMENTO, "+
                "BENEFICIO_CUMULO.NUM_MESI_ADEMPIMENTO, "+
                "BENEFICIO_CUMULO.NUM_GIORNI_ADEMPIMENTO, "+ 
                "BENEFICIO_CUMULO.ENTE_INCARICATO, "+
                "BENEFICIO_CUMULO.ID_BENEFICIO_ORIGINE, "+
                "BENEFICIO_CUMULO.BEN_ID_BENEFICIO_ORIG, "+
                "BENEFICIO_CUMULO.FLAG_STATO, "+
                "BENEFICIO_CUMULO.MOTIVO_MODIFICA, "+
                "BENEFICIO_CUMULO.TIT_ID_TITOLO_CUMULATO, "+
                "BENEFICIO_CUMULO.TIT_ID_TITOLO_CUMULO_COLLEGATO"
                ;
  lStatement += " FROM ";
  lStatement += " BENEFICIO_CUMULO, COMUNE C,"; 
  lStatement += " CG_REF_CODES D_NATBEN, CG_REF_CODES D_TIPBEN, CG_REF_CODES SOTTOTIPO, CG_REF_CODES D_TIPSOSPSUB,";
  lStatement += " CG_REF_CODES D_DPR, CG_REF_CODES D_TIPPRO, CG_REF_CODES D_UFFICIOEMI";
  lStatement += " , TITOLO_CUMULATO ";
  
  lStatement += " WHERE ";
  lStatement += " D_NATBEN.RV_DOMAIN = 'NATURA_BENEFICIO' AND D_NATBEN.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_NATURA_BENEFICIO ";
  lStatement += " AND D_TIPBEN.RV_DOMAIN = 'TIPO_BENEFICIO' AND D_TIPBEN.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_TIPO_BENEFICIO  ";
  lStatement += " AND D_TIPSOSPSUB.RV_DOMAIN = 'TIPO_SOSP_SUBORDINATA' AND D_TIPSOSPSUB.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_TIPO_SOSP_SUBORDINATA  ";
  lStatement += " AND D_DPR.RV_DOMAIN = 'DPR' AND D_DPR.RV_LOW_VALUE = BENEFICIO_CUMULO.COD_DPR  ";
  lStatement += " AND D_TIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND D_TIPPRO.RV_LOW_VALUE = BENEFICIO_CUMULO.RIF_COD_TIPO_PROVVEDIMENTO  ";
  lStatement += " AND D_UFFICIOEMI.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' AND D_UFFICIOEMI.RV_LOW_VALUE = BENEFICIO_CUMULO.RIF_COD_TIPO_AUTO_EMITTENTE  ";
  lStatement += " AND BENEFICIO_CUMULO.RIF_COD_LUOGO_EMITTENTE = C.COD_COMUNE(+)";
  lStatement += " AND SOTTOTIPO.RV_DOMAIN='SOTTOTIPO_BENEFICIO' AND  NVL ( beneficio_cumulo.cod_sottotipo_beneficio, '-') = sottotipo.rv_low_value ";

  lStatement += " AND BENEFICIO_CUMULO.TIT_ID_TITOLO_CUMULATO = TITOLO_CUMULATO.ID_TITOLO_CUMULATO ";
  
  
  return lStatement;

}  
 
  //
  // METODO GETMODEL()
  //

	public GenericModel getModel() throws DAOException
  {
    BeneficioCumuloModel aModel = new  BeneficioCumuloModel();

//Inserire le opportune set delle descrizioni!
    aModel.setIdBeneficioCumulo(getBigDecimal("ID_BENEFICIO_CUMULO") );
    aModel.setCodNaturaBeneficio(getString("COD_NATURA_BENEFICIO") );
    aModel.setDescrNaturaBeneficio(getString("DESCNATBEN") );
    aModel.setCodTipoBeneficio(getString("COD_TIPO_BENEFICIO") );
    aModel.setDescrTipoBeneficio(getString("DESCTIPBEN") );
    aModel.setCodTipoSospSubordinata(getString("COD_TIPO_SOSP_SUBORDINATA") );
    aModel.setDescrTipoSospSubordinata(getString("DESCSOSPSUB") );
    aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE") );
    aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE") );
    aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE") );
    aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA") );
    aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO") );
    aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO") );
    aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO") );
    aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA") );
    aModel.setCodDpr(getString("COD_DPR") );
    aModel.setDescrDpr(getString("DESCDPR") );
    aModel.setNote(getString("NOTE") );

    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

    aModel.setCodSottotipoBeneficio(getString("COD_SOTTOTIPO_BENEFICIO") );
    aModel.setDescrSottotipoBeneficio(getString("DESCSOTTO"));
    aModel.setNumAnniSospensione(getBigDecimal("NUM_ANNI_SOSPENSIONE") ) ;     
    aModel.setNumGiorniPrestazione(getBigDecimal("NUM_GIORNI_PRESTAZIONE") );     
    aModel.setNumMesiPrestazione(getBigDecimal("NUM_MESI_PRESTAZIONE") );
    aModel.setNumOreSettimanali(getBigDecimal("NUM_ORE_SETTIMANALI") );         
    aModel.setFlagFrequenzaSettimanale(getString("FLAG_FREQUENZA_SETTIMANALE") ) ; 
    aModel.setNumAnniAdempimento(getBigDecimal("NUM_ANNI_ADEMPIMENTO") );
	aModel.setNumMesiAdempimento(getBigDecimal("NUM_MESI_ADEMPIMENTO") );
	aModel.setNumGiorniAdempimento(getBigDecimal("NUM_GIORNI_ADEMPIMENTO") );
	aModel.setEnteIncaricato(getString("ENTE_INCARICATO") );
    
    aModel.setRifIdProvvedimento(getBigDecimal("RIF_ID_PROVVEDIMENTO") );
    aModel.setRifCodTipoProvvedimento(getString("RIF_COD_TIPO_PROVVEDIMENTO") );
    aModel.setDescrTipoProvvedimento(getString("DESCTIPPRO") );
	aModel.setRifDataProvvedimento(getDate("RIF_DATA_PROVVEDIMENTO") );
	aModel.setRifCodTipoAutoEmittente(getString("RIF_COD_TIPO_AUTO_EMITTENTE") );
	aModel.setRifCodLuogoEmittente(getString("RIF_COD_LUOGO_EMITTENTE") );
	aModel.setDescrTipoAutoEmittente(getString("DESCRTIPAUTEMI") );
	aModel.setDescrLuogoEmittente(getString("DESCRIZIONE") );
	aModel.setRifAnnoProvvedimento(getBigDecimal("RIF_ANNO_PROVVEDIMENTO") );
	aModel.setRifNumeroProvvedimento(getString("RIF_NUMERO_PROVVEDIMENTO") );
	aModel.setRifNumSezioneAutoEmittente(getString("RIF_NUM_SEZIONE_AUTO_EMITTENTE") );
    aModel.setRifDataIrrevocabilita(getDate("RIF_DATA_IRREVOCABILITA") );
    
	aModel.setBenIdBeneficioCumulo(getBigDecimal("BEN_ID_BENEFICIO_CUMULO") );
	aModel.setIdBeneficioOrigine(getBigDecimal("ID_BENEFICIO_ORIGINE") );
	aModel.setBenIdBeneficioOrig(getBigDecimal("BEN_ID_BENEFICIO_ORIG") );
	aModel.setFlagStato(getString("FLAG_STATO") );
	aModel.setMotivoModifica(getString("MOTIVO_MODIFICA") );
	aModel.setTitIdTitoloCumulato(getBigDecimal("TIT_ID_TITOLO_CUMULATO") );
	
	aModel.setTitIdTitoloCumulatoCollegato(getBigDecimal("TIT_ID_TITOLO_CUMULO_COLLEGATO") );

	return aModel;
  }
	

	/**
	 * 
	 * @param aModel
	 * @return
	 */
  public String  setCondizione(BeneficioCumuloModel aModel)
  {
    String lCondizioni = new String();

	if (aModel.getTitIdTitoloCumulato()!= null)
      lCondizioni=" AND TIT_ID_TITOLO_CUMULATO = "+ aModel.getTitIdTitoloCumulato();
	
    if (aModel.getCodNaturaBeneficio()!= null && !aModel.getCodNaturaBeneficio().equals(""))
        lCondizioni+= " AND COD_NATURA_BENEFICIO = '"+ aModel.getCodNaturaBeneficio()+"'";
    
    if (aModel.getCodTipoBeneficio()!= null && !aModel.getCodTipoBeneficio().equals(""))
        lCondizioni+= " AND COD_TIPO_BENEFICIO = '"+ aModel.getCodTipoBeneficio()+"'";
    
    if (aModel.getCodSottotipoBeneficio()!= null && !aModel.getCodSottotipoBeneficio().equals(""))
        lCondizioni+= " AND COD_SOTTOTIPO_BENEFICIO = '"+ aModel.getCodSottotipoBeneficio()+"'";
    
    if(aModel.getCodDpr()!=null && !aModel.getCodDpr().equals("") && !aModel.getCodDpr().equals("-"))
    	lCondizioni += " AND COD_DPR = "+aModel.getCodDpr();
    
    if (aModel.getBenIdBeneficioCumulo()!= null)
        lCondizioni=" AND BEN_ID_BENEFICIO_CUMULO = "+ aModel.getBenIdBeneficioCumulo();
//    
    if (aModel.getTitIdTitoloCumulatoCollegato()!= null)
        lCondizioni=" AND TIT_ID_TITOLO_CUMULO_COLLEGATO = "+ aModel.getTitIdTitoloCumulatoCollegato();
    
  //  else if( aModel instanceof BeneficioSigeModel )
  //      lCondizioni = " AND ID_BENEFICIO IN (SELECT BEN_ID_BENEFICIO FROM BENEFICIO_SENTENZA_SIGE WHERE FAS_SIGE_SEN_ID = " + ((BeneficioSigeModel) aModel).getFasSigeSenId() + ")";

    return lCondizioni;
  }
  

  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " AND ID_BENEFICIO_CUMULO = " + aKey;
  }
  
  public String setCondizioniByBenIdBeneficioCumulo(BigDecimal aKey)
  {
    return " AND BEN_ID_BENEFICIO_CUMULO = " + aKey;
  }
  
  public String setCondizioneByKeyOrig(BigDecimal aKey)
  {
    return " AND ID_BENEFICIO_ORIGINE = " + aKey;
  }
  
  public String setCondizioneByTitoloCum(BigDecimal aKey)
  {
    return " AND TIT_ID_TITOLO_CUMULATO = " + aKey;
  }
  
}
