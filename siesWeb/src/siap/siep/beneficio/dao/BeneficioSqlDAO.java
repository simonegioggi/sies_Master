package siap.siep.beneficio.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.sige.beneficio.model.BeneficioSigeModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: BeneficioSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Beneficio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class BeneficioSqlDAO extends SIAPSqlDAO
{
  public BeneficioSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //

  public void ricercaBeneficio(BeneficioModel aModel) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aModel);
    lSql += " ORDER BY COD_NATURA_BENEFICIO";

    setStatement(lSql);
  }

  public void ricercaBeneficioByKeyFascicolo(BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP= " + aKey;
    lSql += " ORDER BY COD_NATURA_BENEFICIO";
    setStatement(lSql);
  }

  public void ricercaBeneficioByKey(BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  public void ricercaBeneficioByBenIdBeneficio(BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizioniByBenIdBeneficio(aKey);
    setStatement(lSql);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_BENEFICIO, "+
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
                  "INAPPLICABILITA_MIS_SICUREZZA, "+
                  "COD_OPERATORE_INSERIMENTO, "+
                  "DATA_INSERIMENTO, "+
                  "COD_UFFICIO_INSERIMENTO, "+
                  "COD_OPERATORE_AGGIORNAMENTO, "+
                  "DATA_AGGIORNAMENTO, "+
                  "COD_UFFICIO_AGGIORNAMENTO, "+
                  "FAS_SIE_ID_FASCICOLO_SIEP, "+
                  "EVE_ID_EVENTO, "+
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
				  "BEN_ID_BENEFICIO, "+
				  "RIF_DATA_IRREVOCABILITA, "+
				  "NUM_ANNI_ADEMPIMENTO, "+
				  "NUM_MESI_ADEMPIMENTO, "+
				  "NUM_GIORNI_ADEMPIMENTO "; 
    lStatement += " FROM BENEFICIO,COMUNE C,CG_REF_CODES D_NATBEN,CG_REF_CODES D_TIPBEN,CG_REF_CODES D_TIPSOSPSUB,CG_REF_CODES D_DPR,CG_REF_CODES D_TIPPRO,CG_REF_CODES D_UFFICIOEMI,CG_REF_CODES SOTTOTIPO ";
    lStatement += " WHERE ";
    lStatement += " D_NATBEN.RV_DOMAIN='NATURA_BENEFICIO' AND D_NATBEN.RV_LOW_VALUE=BENEFICIO.COD_NATURA_BENEFICIO ";
    lStatement += " AND D_TIPBEN.RV_DOMAIN='TIPO_BENEFICIO' AND D_TIPBEN.RV_LOW_VALUE=BENEFICIO.COD_TIPO_BENEFICIO  ";
    lStatement += " AND D_TIPSOSPSUB.RV_DOMAIN='TIPO_SOSP_SUBORDINATA' AND D_TIPSOSPSUB.RV_LOW_VALUE=BENEFICIO.COD_TIPO_SOSP_SUBORDINATA  ";
    lStatement += " AND D_DPR.RV_DOMAIN='DPR' AND D_DPR.RV_LOW_VALUE=BENEFICIO.COD_DPR  ";
    lStatement += " AND D_TIPPRO.RV_DOMAIN='TIPO_PROVVEDIMENTO' AND D_TIPPRO.RV_LOW_VALUE=BENEFICIO.RIF_COD_TIPO_PROVVEDIMENTO  ";
    lStatement += " AND D_UFFICIOEMI.RV_DOMAIN='TIPO_UFFICIO_EMITTENTE' AND D_UFFICIOEMI.RV_LOW_VALUE=BENEFICIO.RIF_COD_TIPO_AUTO_EMITTENTE  ";
    lStatement += " AND BENEFICIO.RIF_COD_LUOGO_EMITTENTE = C.COD_COMUNE(+)";
    lStatement += " AND SOTTOTIPO.RV_DOMAIN='SOTTOTIPO_BENEFICIO' AND  NVL ( beneficio.cod_sottotipo_beneficio, '-') = sottotipo.rv_low_value ";
   
    
    
    return lStatement;
  }

  //
  // METODO GETMODEL()
  //

	public GenericModel getModel() throws DAOException
  {
    BeneficioModel aModel = new  BeneficioModel();

//Inserire le opportune set delle descrizioni!
    aModel.setIdBeneficio(getBigDecimal("ID_BENEFICIO") );
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
    aModel.setInapplicabilitaMisSicurezza(getString("INAPPLICABILITA_MIS_SICUREZZA") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP") );
    aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO") );

    aModel.setCodSottotipoBeneficio(getString("COD_SOTTOTIPO_BENEFICIO") );
    aModel.setDescrSottotipoBeneficio(getString("DESCSOTTO"));
    aModel.setNumAnniSospensione(getBigDecimal("NUM_ANNI_SOSPENSIONE") ) ;     
    aModel.setNumGiorniPrestazione(getBigDecimal("NUM_GIORNI_PRESTAZIONE") );     
    aModel.setNumMesiPrestazione(getBigDecimal("NUM_MESI_PRESTAZIONE") );
    aModel.setNumOreSettimanali(getBigDecimal("NUM_ORE_SETTIMANALI") );         
    aModel.setFlagFrequenzaSettimanale(getString("FLAG_FREQUENZA_SETTIMANALE") ) ;  
    
    aModel.setRifIdProvvedimento(getBigDecimal("RIF_ID_PROVVEDIMENTO") );
    aModel.setRifCodTipoProvvedimento(getString("RIF_COD_TIPO_PROVVEDIMENTO") );
    aModel.setDescrTipoProvvedimento(getString("DESCTIPPRO") );
	aModel.setRifDataProvvedimento(getDate("RIF_DATA_PROVVEDIMENTO") );
	aModel.setRifCodTipoAutoEmittente(getString("RIF_COD_TIPO_AUTO_EMITTENTE") );
	aModel.setRifCodLuogoAutoEmittente(getString("RIF_COD_LUOGO_EMITTENTE") );
	aModel.setDescrTipoAutoritaEmittente(getString("DESCRTIPAUTEMI") );
	aModel.setDescrLuogoAutoritaEmittente(getString("DESCRIZIONE") );
	aModel.setRifAnnoProvvedimento(getBigDecimal("RIF_ANNO_PROVVEDIMENTO") );
	aModel.setRifNumeroProvvedimento(getString("RIF_NUMERO_PROVVEDIMENTO") );  
	aModel.setBenIdBeneficio(getBigDecimal("BEN_ID_BENEFICIO"));


	aModel.setNumAnniAdempimento(getBigDecimal("NUM_ANNI_ADEMPIMENTO") );
	aModel.setNumMesiAdempimento(getBigDecimal("NUM_MESI_ADEMPIMENTO") );
	aModel.setNumGiorniAdempimento(getBigDecimal("NUM_GIORNI_ADEMPIMENTO") );

    aModel.setRifNumSezioneAutoEmittente(getString("RIF_NUM_SEZIONE_AUTO_EMITTENTE") );
    aModel.setRifDataIrrevocabilita(getDate("RIF_DATA_IRREVOCABILITA") );

    return aModel;
  }
/*
  public String  setCondizione(BeneficioModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if (aModel.getFasSieIdFascicoloSiep()!=null)
    {
      lCondizioni=" AND FAS_SIE_ID_FASCICOLO_SIEP="+ aModel.getFasSieIdFascicoloSiep();
      lInserito=true;
    }
    if (aModel.getBenIdBeneficio()!=null)
    {
      lCondizioni=" AND BEN_ID_BENEFICIO="+ aModel.getBenIdBeneficio();
      lInserito=true;
    }
    if (aModel.getCodNaturaBeneficio()!=null && !aModel.getCodNaturaBeneficio().equals(""))
    {
      if (lInserito)
      {
        lCondizioni+= " AND COD_NATURA_BENEFICIO='"+ aModel.getCodNaturaBeneficio()+"'";
      }
      else
      {
        lCondizioni=" COD_NATURA_BENEFICIO='"+ aModel.getCodNaturaBeneficio()+"'";
        lInserito=true;
      }
    }
    if (aModel.getCodTipoBeneficio()!=null && !aModel.getCodTipoBeneficio().equals(""))
    {
      if (lInserito)
      {
        lCondizioni+= " AND COD_TIPO_BENEFICIO='"+ aModel.getCodTipoBeneficio()+"'";
      }
      else
      {
        lCondizioni=" COD_TIPO_BENEFICIO='"+ aModel.getCodTipoBeneficio()+"'";
        lInserito=true;
      }
    }

    return lCondizioni;
  }
  
*/
/**
 * Valorizzazione del filtro di ricerca Benefici.
 * Aggiunta la condizione per Beneficio SIGE.
 * Luigi 8-11-2010.
 */
  public String  setCondizione(BeneficioModel aModel)
  {
    String lCondizioni = new String();

	if (aModel.getFasSieIdFascicoloSiep()!= null)
      lCondizioni=" AND FAS_SIE_ID_FASCICOLO_SIEP="+ aModel.getFasSieIdFascicoloSiep();
    if (aModel.getCodNaturaBeneficio()!= null && !aModel.getCodNaturaBeneficio().equals(""))
        lCondizioni+= " AND COD_NATURA_BENEFICIO='"+ aModel.getCodNaturaBeneficio()+"'";
    if (aModel.getCodTipoBeneficio()!= null && !aModel.getCodTipoBeneficio().equals(""))
        lCondizioni+= " AND COD_TIPO_BENEFICIO='"+ aModel.getCodTipoBeneficio()+"'";
    if (aModel.getCodSottotipoBeneficio()!= null && !aModel.getCodSottotipoBeneficio().equals(""))
        lCondizioni+= " AND COD_SOTTOTIPO_BENEFICIO='"+ aModel.getCodSottotipoBeneficio()+"'";
    if (aModel.getBenIdBeneficio()!= null)
        lCondizioni=" AND BEN_ID_BENEFICIO="+ aModel.getBenIdBeneficio();
    else if( aModel instanceof BeneficioSigeModel )
        lCondizioni = " AND ID_BENEFICIO IN (SELECT BEN_ID_BENEFICIO FROM BENEFICIO_SENTENZA_SIGE WHERE FAS_SIGE_SEN_ID = " + ((BeneficioSigeModel) aModel).getFasSigeSenId() + ")";

    return lCondizioni;
  }
  

  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " AND ID_BENEFICIO = " + aKey;
  }
  
  public String setCondizioniByBenIdBeneficio(BigDecimal aKey)
  {
    return " AND BEN_ID_BENEFICIO = " + aKey;
  }
}
