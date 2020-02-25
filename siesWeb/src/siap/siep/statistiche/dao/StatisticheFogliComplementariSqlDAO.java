package siap.siep.statistiche.dao;

import java.sql.Connection;
import java.util.Vector;

import siap.dao.SIAPSqlDAO;
import siap.siep.statistiche.model.RicercaFogliCompModel;
import siap.siep.statistiche.model.RiepilogoStatisticheFogliComplementari;
import siap.siep.statistiche.model.StatisticheFogliComplementariModel;
import f3b.dao.DAOException;
import f3b.util.DateUtils;

public class StatisticheFogliComplementariSqlDAO extends SIAPSqlDAO {
	public StatisticheFogliComplementariSqlDAO (Connection con) {
	    super(con);
	}
	
	public void ricercaStatisticheFogliComplementari (RicercaFogliCompModel filtroModel) {
		String sql="";
		if (filtroModel.isFcTrasmessi()) {
			sql=getQueryFcTrasmessi (filtroModel);
		}
		
		if (filtroModel.isFcIscrittiManualmente()) {
			if (!sql.equals(""))
			    sql += " UNION ALL ";
			
			sql+=this.getQueryFcTrasmessiManualmente(filtroModel);
		}
		
		if (filtroModel.isProvvedimentiPriviFC()) {
			if (!sql.equals(""))
			    sql += " UNION ALL ";
			
			sql+=this.getQueryProvvedimentiPriviFC(filtroModel);
		}
		
		if (filtroModel.isFcAnnullati()) {
			if (!sql.equals(""))
			    sql += " UNION ALL ";
			
			sql+=this.getQueryProvvedimentiFcAnnullati(filtroModel);
		}
		
		if (filtroModel.isFcNonTrasmessi() ) {
			if (!sql.equals(""))
			    sql += " UNION ALL ";
			
			sql+=this.getQueryFcNonTrasmessi(filtroModel);
		}
		
		if (filtroModel.isFcTrasmessiErrore()) {
			if (!sql.equals(""))
			    sql += " UNION ALL ";
			
			sql+=this.getQueryFcTrasmessiConErrore(filtroModel);
		}
		
		sql+=" order by data_emissione_provv ";
		
		super.setStatement(sql);
	}

	public void ricercaFcAnnullati (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryProvvedimentiFcAnnullati(filtroModel);
		sql+=" order by data_emissione_provv ";
		super.setStatement(sql);
	}
	
	public void ricercaConteggioFcAnnullati (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryConteggioProvvedimentiFcAnnullati(filtroModel);
		super.setStatement(sql);
	}
	
	public void ricercaIscrittiManualmente (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryFcTrasmessiManualmente(filtroModel);
		sql+=" order by data_emissione_provv ";
		super.setStatement(sql);
	}
	
	public void ricercaConteggioFcIscrittiManualmente (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryConteggioFcTrasmessiManualmente(filtroModel);
		super.setStatement(sql);
	}	
	
	public void ricercaProvvedimentiPriviFC (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryProvvedimentiPriviFC(filtroModel);
		sql+=" order by data_emissione_provv ";
		super.setStatement(sql);
	}
	
	public void ricercaFCNonTrasmessi (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryFcNonTrasmessi(filtroModel);
		sql+=" order by data_emissione_provv ";
		super.setStatement(sql);
	}
	
	
	public void ricercaConteggioProvvedimentiPriviFC (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryConteggioProvvedimentiPriviFC(filtroModel);
		super.setStatement(sql);
	}
	
	public void ricercaConteggioProvvedimentiConFC (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryConteggioProvvedimentiConFC(filtroModel);
		super.setStatement(sql);
	}
	
	public void ricercaConteggioFCNonTrasmessi (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryConteggioFCNonTrasmessi (filtroModel);
		super.setStatement(sql);
	}
	
	public void ricercaConteggioFCTrasmessiConErrore (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryConteggioFCTrasmessiConErrore (filtroModel);
		super.setStatement(sql);
	}

	public void ricercaProvvedimentiConFC (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryFcTrasmessi(filtroModel);
		super.setStatement(sql);
	}
	
	public void ricercaFCTrasmessiConErrore (RicercaFogliCompModel filtroModel) {
		String sql=this.getQueryFcTrasmessiConErrore(filtroModel);
		super.setStatement(sql);
	}
	
	
	
	private String getQueryFcTrasmessiConErrore (RicercaFogliCompModel filtroModel) {
		//String sql= "SELECT distinct F.ID_FASCICOLO_SIEP PRG, " +
		//            "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP, " +
		//            "DA.data_emissione data_emissione_provv, " + 
		//            "CG.RV_MEANING motivo, " + 
		//            "to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, " + 
		//            "'Trasmesso con Errore' as esito, " +
		//            "F.chiave_anno as chiave_anno " +
		//            "FROM DOCUMENTO_ALLEGATO DA, EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES CG,  LOG_TRASFERIMENTO_ESECUZIONE L  WHERE " + 
		//            "DA.COD_TIPO_DOCUMENTO = '06' and " +
		//            "DA.COD_UFFICIO_INSERIMENTO = '" + filtroModel.getCodUfficioInserimento() +"' and " +
 		//            "DA.EVE_ID_EVENTO = E.ID_EVENTO and " +
 		//            "DA.ANNO_FOGLIO_COMPLEMENTARE is not NULL and " +
		//            "E.KEY_ESEC_NSC IS NULL and " +
		//            "E.ID_EVENTO=L.CHIAVE_SIES and " +
		//            "L.CHIAVE_NSC=0 and " +
		//            "F.ID_FASCICOLO_SIEP =E.Fas_Sie_Id_Fascicolo_Siep and " +
		//            "(CG.rv_low_value = E.cod_motivo and cg.rv_domain='MOTIVO_PROVVEDIMENTO')";
		
		//String sql="SELECT distinct DA.ID_DOCUMENTO_ALLEGATO as ID_DOCUMENTO_ALLEGATO, " +
		//           "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP,"
		//           + "F.ID_FASCICOLO_SIEP as PRG,"
		//           + "DA.data_emissione data_emissione_provv,"
		//           + "CG.RV_MEANING motivo, to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO,"
		//           + "'Trasmesso con Errore' as esito,"
		//           + "DA.ANNO_FOGLIO_COMPLEMENTARE as anno_foglio_complementare FROM "
		//           + "DOCUMENTO_ALLEGATO DA, EVENTO E, CG_REF_CODES CG,  LOG_TRASFERIMENTO_ESECUZIONE L, FASCICOLO_SIEP F  WHERE "
		//           + "DA.COD_TIPO_DOCUMENTO = '06' "
		//           + "and DA.COD_UFFICIO_INSERIMENTO = '"+ filtroModel.getCodUfficioInserimento() +"'"
		//           + "and DA.EVE_ID_EVENTO = E.ID_EVENTO "
		//           + "and F.ID_FASCICOLO_SIEP = E.Fas_Sie_Id_Fascicolo_Siep and  "
		//           + "DA.ANNO_FOGLIO_COMPLEMENTARE is not NULL and "
		//           + "E.KEY_ESEC_NSC IS NULL and "
		//           + "E.ID_EVENTO=L.CHIAVE_SIES and "
		//           + "L.CHIAVE_NSC=0 and (CG.rv_low_value = E.cod_motivo and cg.rv_domain='MOTIVO_PROVVEDIMENTO')";
		
		String sql = "SELECT distinct E.id_evento as id_evento, " +
	             "F.ID_FASCICOLO_SIEP PRG, " +
			     "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP, " + 
	             "F.ID_FASCICOLO_SIEP, " +
			     "E.data_emissione as data_emissione_provv, " +
			     "to_char(E.data_emissione,'YYYY') as chiave_anno_provv, " +
			     //"CG.RV_MEANING motivo, " +
	             //Aggiunta concatenazione con Tipo_provvedimento
	             "TIPO_PROV.RV_MEANING||' '||CG.RV_MEANING motivo, " +

			     "da.data_emissione as data_emissione, " +
			     "to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, " +
			     "'Trasmesso con Errore' as esito, " +
			     "F.chiave_anno as chiave_anno  " +
			     "FROM EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES CG, DOCUMENTO_ALLEGATO DA, CG_REF_CODES TIPO_PROV, LOG_TRASFERIMENTO_ESECUZIONE L where  " +
			     "f.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and  " +
			     "F.ID_FASCICOLO_SIEP=E.Fas_Sie_Id_Fascicolo_Siep and  " +
			     "E.ID_EVENTO in (SELECT DISTINCT B.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO B, EVENTO G WHERE G.ID_EVENTO = B.EVE_ID_EVENTO AND B.COD_TIPO_DOCUMENTO  = '06' and b.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and b.ANNO_FOGLIO_COMPLEMENTARE is not NULL) and  " +
			     "E.KEY_ESEC_NSC IS NULL and " +
			     "E.ID_EVENTO=L.CHIAVE_SIES and " +
			     "L.CHIAVE_NSC=0 and " +
			     
  				 //condizione per concatenzaione tipo_provvedimento||' '||motivo provvedimento
  				 "E.COD_TIPO_PROVVEDIMENTO = TIPO_PROV.RV_LOW_VALUE and " +
  				 "TIPO_PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' and " +
  				 "(nvl(E.COD_MOTIVO, '') = CG.RV_LOW_VALUE AND " +
  				 "CG.RV_DOMAIN IN ('MOTIVO_PROVVEDIMENTO','OGGETTO_PROCEDIMENTO')) and " +
                 // Modifica del 01/03/2016 Nuova Infrastruttura - INIZIO ******
                 // vengono esclusi i procedimenti archiviati per affidamento in prova (COD_MOTIVO=0424)
                 "E.COD_MOTIVO <> '0424' and " +
				 //Modifica del 01/03/2016 Nuova Infrastruttura - FINE ******
				 
			     //"CG.rv_low_value = E.cod_motivo and  " +
			     //"cg.rv_domain='MOTIVO_PROVVEDIMENTO' and  " +
			     //"F.COD_UFFICIO_INSERIMENTO = '"+filtroModel.getCodUfficioInserimento()+"' and " +
			     "E.DATA_EMISSIONE is NOT NULL AND " +
			     "DA.EVE_ID_EVENTO=E.ID_EVENTO ";
		
		if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and TO_CHAR(DA.DATA_EMISSIONE,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char (DA.DATA_EMISSIONE,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
 	
 	   if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and TO_CHAR (DA.data_emissione,'YYYYMMDD') = '" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "'";
			} else {
			    sql += " and trunc(DA.data_emissione) between TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + ",'YYYYMMDD' ) and TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
			}
		}
		
		return sql;
	}
	
	
	private String getQueryFcNonTrasmessi (RicercaFogliCompModel filtroModel) {
		//String sql = "SELECT " +
		//             "distinct F.ID_FASCICOLO_SIEP PRG, " +
		//             "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP," +
		//             "DA.data_emissione data_emissione_provv, " +
		//             "CG.RV_MEANING motivo, " +
		//             "to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, " +
		//             "'Non Trasmesso' as esito, " +
		//             "F.CHIAVE_ANNO as chiave_anno " +
		//             "FROM DOCUMENTO_ALLEGATO DA " +
		//             "JOIN EVENTO E " +
		//             "ON (DA.EVE_ID_EVENTO = E.ID_EVENTO) " +
		//             "JOIN FASCICOLO_SIEP F " +
		//             "ON (E.Fas_Sie_Id_Fascicolo_Siep = F.ID_FASCICOLO_SIEP) " +
		//             "JOIN CG_REF_CODES CG " +
		//             "ON (CG.rv_low_value = e.cod_motivo and cg.rv_domain='MOTIVO_PROVVEDIMENTO') " +
		//             "WHERE DA.COD_TIPO_DOCUMENTO = '06' " +
		//             "AND DA.DATA_TRASMISSIONE IS NULL and DATA_INS_MAN is NULL and DA.DATA_ANNULLAMENTO is NULL " +
		//             "AND DA.COD_UFFICIO_INSERIMENTO = '" + filtroModel.getCodUfficioInserimento() + "'";
		
		//String sql="SELECT distinct F.ID_FASCICOLO_SIEP as PRG, " +
		//           "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP,"
		//           + "DA.ID_DOCUMENTO_ALLEGATO as ID_DOCUMENTO_ALLEGATO,"
		//           + "DA.data_emissione data_emissione_provv,"
		//           + "CG.RV_MEANING motivo, to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO,"
		//           + "'FC Non Trasmessi' as esito,"
		//           + "DA.ANNO_FOGLIO_COMPLEMENTARE as anno_foglio_complementare FROM "
		//           + "DOCUMENTO_ALLEGATO DA, EVENTO E, CG_REF_CODES CG, FASCICOLO_SIEP F  WHERE "
		//           + "DA.ANNO_FOGLIO_COMPLEMENTARE is not NULL "
		//           + "and DA.COD_TIPO_DOCUMENTO = '06' "
		//           + "AND DA.DATA_ANNULLAMENTO IS NULL AND DA.DATA_TRASMISSIONE is NULL AND DA.DATA_INS_MAN is NULL " 
		//           + "and DA.COD_UFFICIO_INSERIMENTO = '"+ filtroModel.getCodUfficioInserimento() +"' "
		//           + "and DA.EVE_ID_EVENTO = E.ID_EVENTO "
		//           + "and F.ID_FASCICOLO_SIEP = E.Fas_Sie_Id_Fascicolo_Siep "
		//           + "and (CG.rv_low_value = E.cod_motivo and cg.rv_domain='MOTIVO_PROVVEDIMENTO')";
				
		String sql = "SELECT distinct E.id_evento as id_evento, " +
	             "F.ID_FASCICOLO_SIEP PRG, " +
			     "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP, " + 
	             "F.ID_FASCICOLO_SIEP, " +
			     "E.data_emissione as data_emissione_provv, " +
			     "to_char(E.data_emissione,'YYYY') as chiave_anno_provv, " +
			     //"CG.RV_MEANING motivo, " +
	             //Aggiunta concatenazione con Tipo_provvedimento
	             "TIPO_PROV.RV_MEANING||' '||CG.RV_MEANING motivo, " +

			     "da.data_emissione as data_emissione, " +
			     "to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, " +
			     "'Non Trasmesso' as esito, " +
			     "F.chiave_anno as chiave_anno  " +
			     "FROM EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES CG, DOCUMENTO_ALLEGATO DA, CG_REF_CODES TIPO_PROV where  " +
			     "f.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and  " +
			     "F.ID_FASCICOLO_SIEP=E.Fas_Sie_Id_Fascicolo_Siep and  " +
			     "E.ID_EVENTO in (SELECT DISTINCT B.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO B, EVENTO G WHERE G.ID_EVENTO = B.EVE_ID_EVENTO AND B.COD_TIPO_DOCUMENTO  = '06' and b.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and b.DATA_ANNULLAMENTO is NULL and b.DATA_INS_MAN is NULL and DA.DATA_TRASMISSIONE is NULL) and  " +
			     //condizione per concatenzaione tipo_provvedimento||' '||motivo provvedimento
			     "E.COD_TIPO_PROVVEDIMENTO = TIPO_PROV.RV_LOW_VALUE and " +
			     "TIPO_PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' and " +
			     "((nvl(E.COD_MOTIVO, '') = CG.RV_LOW_VALUE AND " +
                 // Modifica del 01/03/2016 Nuova Infrastruttura - INIZIO ******
                 // vengono esclusi i procedimenti archiviati per affidamento in prova (COD_MOTIVO=0424)
			     "CG.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND E.COD_MOTIVO <> '0424') OR " +
			     // Modifica del 01/03/2016 Nuova Infrastruttura - FINE ******
			     "(E.COD_MOTIVO = CG.RV_LOW_VALUE AND " +
			     "CG.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) and " +

			     //"CG.rv_low_value = E.cod_motivo and  " +
			     //"cg.rv_domain='MOTIVO_PROVVEDIMENTO' and  " +
			     
   				 // 23/11/2015 aggiunta condizione che il non trasmesso non sia trasmesso con errore
   				 "e.id_evento not in (select l.chiave_sies from LOG_TRASFERIMENTO_ESECUZIONE l) and " +

			     "F.COD_UFFICIO_INSERIMENTO = '"+filtroModel.getCodUfficioInserimento()+"' and " +
			     "E.DATA_EMISSIONE is NOT NULL AND " +
			     "DA.EVE_ID_EVENTO=E.ID_EVENTO ";
		
		if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and TO_CHAR(E.DATA_EMISSIONE,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char (E.DATA_EMISSIONE,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
  	
  	   if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and TO_CHAR (DA.data_emissione,'YYYYMMDD') = '" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "'";
			} else {
			    sql += " and trunc(DA.data_emissione) between TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + ",'YYYYMMDD' ) and TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
			}
		}
		
		return sql;
	}

	private String getQueryFcTrasmessi (RicercaFogliCompModel filtroModel) {
		//String sql = "SELECT " +
		//             "distinct F.ID_FASCICOLO_SIEP PRG, " +
		//             "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP," +
		//             "DA.data_emissione data_emissione_provv, " +
		//             "CG.RV_MEANING motivo, " +
		//             "to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, " +
		//             "'Trasmesso' as esito, " +
		//             "F.chiave_anno as chiave_anno " +
		//             "FROM DOCUMENTO_ALLEGATO DA " +
		//             "JOIN EVENTO E " +
		//             "ON (DA.EVE_ID_EVENTO = E.ID_EVENTO) " +
		//             "JOIN FASCICOLO_SIEP F " +
		//             "ON (E.Fas_Sie_Id_Fascicolo_Siep = F.ID_FASCICOLO_SIEP) " +
		//             "JOIN CG_REF_CODES CG " +
		//             "ON (CG.rv_low_value = e.cod_motivo and cg.rv_domain='MOTIVO_PROVVEDIMENTO') " +
		//             "WHERE DA.COD_TIPO_DOCUMENTO = '06' " +
		//             "AND DA.DATA_TRASMISSIONE IS NOT NULL " +
		//             "AND DA.COD_UFFICIO_INSERIMENTO = '" + filtroModel.getCodUfficioInserimento() + "'";
		
		
		//String sql="SELECT distinct DA.ID_DOCUMENTO_ALLEGATO as ID_DOCUMENTO_ALLEGATO, " +
		//           "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP,"
		//           + "F.ID_FASCICOLO_SIEP as PRG,"
		//           + "DA.data_emissione data_emissione_provv,"
		//           + "CG.RV_MEANING motivo, to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO,"
		//           + "'Trasmesso' as esito,"
		//           + "DA.ANNO_FOGLIO_COMPLEMENTARE as anno_foglio_complementare FROM "
		//           + "DOCUMENTO_ALLEGATO DA, EVENTO E, CG_REF_CODES CG, FASCICOLO_SIEP F  WHERE "
		//           + "DA.ANNO_FOGLIO_COMPLEMENTARE is not NULL "
		//           + "and DA.COD_TIPO_DOCUMENTO = '06' "
		//           + "AND DA.DATA_ANNULLAMENTO IS NULL AND DA.DATA_TRASMISSIONE is NOT NULL " 
		//           + "and DA.COD_UFFICIO_INSERIMENTO = '"+ filtroModel.getCodUfficioInserimento() +"' "
		//           + "and DA.EVE_ID_EVENTO = E.ID_EVENTO "
		//           + "and F.ID_FASCICOLO_SIEP = E.Fas_Sie_Id_Fascicolo_Siep "
		//           + "and (CG.rv_low_value = E.cod_motivo and cg.rv_domain='MOTIVO_PROVVEDIMENTO')";
		//
		
		String sql = "SELECT distinct E.id_evento as id_evento, " +
	             "F.ID_FASCICOLO_SIEP PRG, " +
			     "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP, " + 
	             "F.ID_FASCICOLO_SIEP, " +
			     "E.data_emissione as data_emissione_provv, " +
			     "to_char(E.data_emissione,'YYYY') as chiave_anno_provv, " +
			     //"CG.RV_MEANING motivo, " +
	             //Aggiunta concatenazione con Tipo_provvedimento
	             "TIPO_PROV.RV_MEANING||' '||CG.RV_MEANING motivo, " +
			     "da.data_emissione as data_emissione, " +
			     "to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, " +
			     "'Trasmesso' as esito, " +
			     "F.chiave_anno as chiave_anno  " +
			     "FROM EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES CG, DOCUMENTO_ALLEGATO DA, CG_REF_CODES TIPO_PROV where  " +
			     "f.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and  " +
			     "F.ID_FASCICOLO_SIEP=E.Fas_Sie_Id_Fascicolo_Siep and  " +
			     "E.ID_EVENTO in (SELECT DISTINCT B.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO B, EVENTO G WHERE G.ID_EVENTO = B.EVE_ID_EVENTO AND B.COD_TIPO_DOCUMENTO  = '06' and b.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and b.DATA_ANNULLAMENTO is NULL and b.DATA_INS_MAN is NULL and DA.DATA_TRASMISSIONE is NOT NULL) and  " +
			     
    			 //condizione per concatenzaione tipo_provvedimento||' '||motivo provvedimento
    			 "E.COD_TIPO_PROVVEDIMENTO = TIPO_PROV.RV_LOW_VALUE and " +
                 "TIPO_PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' and " +
                 "((nvl(E.COD_MOTIVO, '') = CG.RV_LOW_VALUE AND " +
                 // Modifica del 01/03/2016 Nuova Infrastruttura - INIZIO ******
                 // vengono esclusi i procedimenti archiviati per affidamento in prova (COD_MOTIVO=0424)
                 "CG.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND E.COD_MOTIVO <> '0424') OR " +
                 // Modifica del 01/03/2016 Nuova Infrastruttura - FINE ******
                 "(E.COD_MOTIVO = CG.RV_LOW_VALUE AND " +
                 "CG.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) and " +
			     //"CG.rv_low_value = E.cod_motivo and  " +
			     //"cg.rv_domain='MOTIVO_PROVVEDIMENTO' and  " +
			     
			     "F.COD_UFFICIO_INSERIMENTO = '"+filtroModel.getCodUfficioInserimento()+"' and " +
			     "E.DATA_EMISSIONE is NOT NULL AND " +
			     "DA.EVE_ID_EVENTO=E.ID_EVENTO ";
		
		if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and TO_CHAR(DA.DATA_EMISSIONE,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char (DA.DATA_EMISSIONE,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
   	
   	    if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and TO_CHAR (DA.data_emissione,'YYYYMMDD') = '" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "'";
			} else {
			    sql += " and trunc(DA.data_emissione) between TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + ",'YYYYMMDD' ) and TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
			}
		}
		
		return sql;
	}
	
	private String getQueryFcTrasmessiManualmente (RicercaFogliCompModel filtroModel) {
		//String sql = "SELECT " +
	    //         "distinct F.ID_FASCICOLO_SIEP PRG, " +
	    //         "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP," +
	    //         "DA.data_emissione data_emissione_provv, " +
	    //         "CG.RV_MEANING motivo, " +
	    //         "to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, " +
	    //         "'Iscritto Manualmente' as esito, " +
	    //         "F.chiave_anno as chiave_anno " +
	    //         "FROM DOCUMENTO_ALLEGATO DA " +
	    //         "JOIN EVENTO E " +
	    //         "ON (DA.EVE_ID_EVENTO = E.ID_EVENTO) " +
	    //         "JOIN FASCICOLO_SIEP F " +
	    //         "ON (E.Fas_Sie_Id_Fascicolo_Siep = F.ID_FASCICOLO_SIEP) " +
	    //         "JOIN CG_REF_CODES CG " +
	    //         "ON (CG.rv_low_value = e.cod_motivo and cg.rv_domain='MOTIVO_PROVVEDIMENTO') " +
	    //         "WHERE DA.COD_TIPO_DOCUMENTO = '06' " +
    	//         "AND DA.DATA_ANNULLAMENTO IS NULL AND DA.DATA_INS_MAN is not NULL " +
	    //         "AND DA.COD_UFFICIO_INSERIMENTO = '" + filtroModel.getCodUfficioInserimento() + "' ";
		
		String sql = "SELECT distinct E.id_evento as id_evento, " +
	             "F.ID_FASCICOLO_SIEP PRG, " +
			     "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP, " + 
	             "F.ID_FASCICOLO_SIEP, " +
			     "E.data_emissione as data_emissione_provv, " +
			     "to_char(E.data_emissione,'YYYY') as chiave_anno_provv, " +
			     //"CG.RV_MEANING motivo, " +
                 //Aggiunta concatenazione con Tipo_provvedimento
                 "TIPO_PROV.RV_MEANING||' '||CG.RV_MEANING motivo, " +

			     "da.data_emissione as data_emissione, " +
			     "to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, " +
			     "'Trasmesso Manualmente' as esito, " +
			     "F.chiave_anno as chiave_anno  " +
			     "FROM EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES CG, DOCUMENTO_ALLEGATO DA, CG_REF_CODES TIPO_PROV where  " +
			     "f.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and  " +
			     "F.ID_FASCICOLO_SIEP=E.Fas_Sie_Id_Fascicolo_Siep and  " +
			     "E.ID_EVENTO in (SELECT DISTINCT B.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO B, EVENTO G WHERE G.ID_EVENTO = B.EVE_ID_EVENTO AND B.COD_TIPO_DOCUMENTO  = '06' and b.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and b.DATA_ANNULLAMENTO is NULL and b.DATA_INS_MAN is not NULL) and  " +
    			 //condizione per concatenzaione tipo_provvedimento||' '||motivo provvedimento
    			 "E.COD_TIPO_PROVVEDIMENTO = TIPO_PROV.RV_LOW_VALUE and " +
                 "TIPO_PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' and " +
                 "((nvl(E.COD_MOTIVO, '') = CG.RV_LOW_VALUE AND " +
                 // Modifica del 01/03/2016 Nuova Infrastruttura - INIZIO ******
                 // vengono esclusi i procedimenti archiviati per affidamento in prova (COD_MOTIVO=0424)
                 "CG.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND E.COD_MOTIVO <> '0424') OR " +
                 // Modifica del 01/03/2016 Nuova Infrastruttura - FINE ******
                 "(E.COD_MOTIVO = CG.RV_LOW_VALUE AND " +
                 "CG.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) and " +
			     //"CG.rv_low_value = E.cod_motivo and  " +
			     //"cg.rv_domain='MOTIVO_PROVVEDIMENTO' and  " +
			     
			     "F.COD_UFFICIO_INSERIMENTO = '"+filtroModel.getCodUfficioInserimento()+"' and " +
			     "E.DATA_EMISSIONE is NOT NULL AND " +
			     "DA.EVE_ID_EVENTO=E.ID_EVENTO ";
		
		if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and TO_CHAR(DA.DATA_EMISSIONE,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char (DA.DATA_EMISSIONE,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
    	
    	if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and TO_CHAR (DA.data_emissione,'YYYYMMDD') = '" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "'";
			} else {
			    sql += " and trunc(DA.data_emissione) between TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + ",'YYYYMMDD' ) and TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
			}
		}
		
		return sql;
	}

    private String getQueryProvvedimentiPriviFC (RicercaFogliCompModel filtroModel) {
    	//String sql="SELECT distinct F.ID_FASCICOLO_SIEP PRG, " +
    	//"F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP, " +
    	//"F.ID_FASCICOLO_SIEP, " +
    	//"E.data_emissione as data_emissione_provv, " +
    	//"CG.RV_MEANING motivo, " +
    	//"'-' DATA_EMISSIONE_FOGLIO, " +  
    	//"'Privo di FC' as esito, " +
    	//"F.chiave_anno as chiave_anno " +
    	//" FROM EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES CG where " +
    	//" f.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and " + 
    	//" F.ID_FASCICOLO_SIEP=E.Fas_Sie_Id_Fascicolo_Siep and " + 
    	//" E.ID_EVENTO not in (SELECT B.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO B, EVENTO G WHERE G.ID_EVENTO = B.EVE_ID_EVENTO AND B.COD_TIPO_DOCUMENTO  = '06') and " +
    	//" CG.rv_low_value = E.cod_motivo and " +
    	//" cg.rv_domain='MOTIVO_PROVVEDIMENTO' and " + 
    	//" F.COD_UFFICIO_INSERIMENTO = '" +filtroModel.getCodUfficioInserimento()+ "'"; 
   
    	String sql = "SELECT distinct E.id_evento as id_evento, " +
    	             "F.ID_FASCICOLO_SIEP PRG, " +
    			     "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP, " + 
    	             "F.ID_FASCICOLO_SIEP, " +
    			     "E.data_emissione as data_emissione_provv, " +
    			     "to_char(E.data_emissione,'YYYY') as chiave_anno_provv, " +
    			     //"CG.RV_MEANING motivo, " +
    	             //Aggiunta concatenazione con Tipo_provvedimento
    	             "TIPO_PROV.RV_MEANING||' '||CG.RV_MEANING motivo, " +

    			     "NULL as data_emissione, " +
    			     "'-' DATA_EMISSIONE_FOGLIO, " +
    			     "'Privo di FC' as esito, " +
    			     "F.chiave_anno as chiave_anno  " +
    			     "FROM EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES CG, CG_REF_CODES TIPO_PROV, codici_univoci_mappati m where  " +
    			     "f.cod_ufficio_inserimento='"+filtroModel.getCodUfficioInserimento()+"' and  " +
    			     "F.ID_FASCICOLO_SIEP=E.Fas_Sie_Id_Fascicolo_Siep and  " +
    			     "E.ID_EVENTO not in (SELECT DISTINCT B.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO B, EVENTO G WHERE G.ID_EVENTO = B.EVE_ID_EVENTO AND B.COD_TIPO_DOCUMENTO  = '06') and " +
    			     
    				 //condizione per concatenzaione tipo_provvedimento||' '||motivo provvedimento
    				 "E.COD_TIPO_PROVVEDIMENTO = TIPO_PROV.RV_LOW_VALUE and " +
                     "TIPO_PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' and " +
    				 "(nvl(E.COD_MOTIVO, '') = CG.RV_LOW_VALUE AND " +
    			     "CG.RV_DOMAIN IN ('MOTIVO_PROVVEDIMENTO', 'OGGETTO_PROCEDIMENTO')) and " +
    			     "e.cod_motivo=m.id_oggetto and m.id_esito is null and " + 
                     // Modifica del 01/03/2016 Nuova Infrastruttura - INIZIO ******
                     // vengono esclusi i procedimenti archiviati per affidamento in prova (COD_MOTIVO=0424)
                     "e.cod_motivo <> '0424' AND " +
					 //Modifica del 01/03/2016 Nuova Infrastruttura - FINE ******
    			     //"CG.rv_low_value = E.cod_motivo and  " +
    			     //"cg.rv_domain='MOTIVO_PROVVEDIMENTO' and  " +
    			     "F.COD_UFFICIO_INSERIMENTO = '"+filtroModel.getCodUfficioInserimento()+"' and " +
    			     "E.DATA_EMISSIONE is NOT NULL ";
    	
    	if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and TO_CHAR(E.DATA_EMISSIONE,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char (E.DATA_EMISSIONE,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
    	
    	if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and TO_CHAR (E.data_emissione,'YYYYMMDD') = '" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "'";
			} else {
			    sql += " and trunc(E.data_emissione) between TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + ",'YYYYMMDD' ) and TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
			}
		}
    	return sql;
    }
    
    private String getQueryProvvedimentiFcAnnullati (RicercaFogliCompModel filtroModel) {
    	//String sql = "SELECT " +
	    //         "distinct F.ID_FASCICOLO_SIEP PRG, " +
	    //         "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP," +
	    //         "DA.data_emissione data_emissione_provv, " +
	    //         "CG.RV_MEANING motivo, " +
	    //         "to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, " +
	    //         "'Annullato' as esito, " +
	    //         "F.chiave_anno as chiave_anno " +
	    //         "FROM DOCUMENTO_ALLEGATO DA " +
	    //         "JOIN EVENTO E " +
	    //         "ON (DA.EVE_ID_EVENTO = E.ID_EVENTO) " +
	    //         "JOIN FASCICOLO_SIEP F " +
	    //         "ON (E.Fas_Sie_Id_Fascicolo_Siep = F.ID_FASCICOLO_SIEP) " +
	    //         "JOIN CG_REF_CODES CG " +
	    //         "ON (CG.rv_low_value = e.cod_motivo and cg.rv_domain='MOTIVO_PROVVEDIMENTO') " +
	    //         "WHERE DA.COD_TIPO_DOCUMENTO = '06' " +
   	    //         "AND DA.DATA_ANNULLAMENTO IS not NULL " +
	    //         "AND DA.COD_UFFICIO_INSERIMENTO = '" + filtroModel.getCodUfficioInserimento() + "' ";
    	
    	//String sql="SELECT distinct DA.ID_DOCUMENTO_ALLEGATO as ID_DOCUMENTO_ALLEGATO, " +
		//           "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP,"
		//           + "F.ID_FASCICOLO_SIEP as PRG,"
		//           + "DA.data_emissione data_emissione_provv,"
		//           + "CG.RV_MEANING motivo, to_char(DA.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO,"
		//           + "'Annullato' as esito,"
		//           + "DA.ANNO_FOGLIO_COMPLEMENTARE as anno_foglio_complementare FROM "
		//           + "DOCUMENTO_ALLEGATO DA, EVENTO E, CG_REF_CODES CG, FASCICOLO_SIEP F  WHERE "
		//           + "DA.ANNO_FOGLIO_COMPLEMENTARE is not NULL "
		//           + "and DA.COD_TIPO_DOCUMENTO = '06' "
		//           + "and DA.DATA_ANNULLAMENTO IS not NULL " 
		//           + "and DA.COD_UFFICIO_INSERIMENTO = '"+ filtroModel.getCodUfficioInserimento() +"' "
		//           + "and DA.EVE_ID_EVENTO = E.ID_EVENTO "
		//           + "and F.ID_FASCICOLO_SIEP = E.Fas_Sie_Id_Fascicolo_Siep "
		//           + "and (CG.rv_low_value = E.cod_motivo and cg.rv_domain='MOTIVO_PROVVEDIMENTO')";
    	
    	
    	String sql = "SELECT distinct E.id_evento as id_evento, " +
	             "F.ID_FASCICOLO_SIEP PRG, " +
			     "F.chiave_anno||'/'||F.chiave_progr num_fasc_SIEP, " + 
	             "F.ID_FASCICOLO_SIEP, " +
			     "E.data_emissione as data_emissione_provv, " +
			     "to_char(E.data_emissione,'YYYY') as chiave_anno_provv, " +
			     //"CG.RV_MEANING motivo, " +
	             //Aggiunta concatenazione con Tipo_provvedimento
	             "TIPO_PROV.RV_MEANING||' '||CG.RV_MEANING motivo, " +

			     "da.data_emissione as data_emissione, " +
			     "TO_CHAR (DA.DATA_EMISSIONE,'DD-MM-YYYY')  as DATA_EMISSIONE_FOGLIO, " +
			     "'Annullato' as esito, " +
			     "F.chiave_anno as chiave_anno  " +
			     "FROM EVENTO E, FASCICOLO_SIEP F, CG_REF_CODES CG,  DOCUMENTO_ALLEGATO DA, CG_REF_CODES TIPO_PROV where  " +
			     "F.ID_FASCICOLO_SIEP=E.Fas_Sie_Id_Fascicolo_Siep and  " +
			     "E.ID_EVENTO in (SELECT DISTINCT B.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO B WHERE B.COD_TIPO_DOCUMENTO = '06' and B.COD_UFFICIO_INSERIMENTO='"+filtroModel.getCodUfficioInserimento()+"' and  DA.DATA_ANNULLAMENTO IS not NULL) and " +
			     "E.DATA_EMISSIONE is NOT NULL and " +
			     
   				 //condizione per concatenzaione tipo_provvedimento||' '||motivo provvedimento
   				 "E.COD_TIPO_PROVVEDIMENTO = TIPO_PROV.RV_LOW_VALUE and " +
   				 "TIPO_PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' and " +
   				 "((nvl(E.COD_MOTIVO, '') = CG.RV_LOW_VALUE AND " +
                 // Modifica del 01/03/2016 Nuova Infrastruttura - INIZIO ******
                 // vengono esclusi i procedimenti archiviati per affidamento in prova (COD_MOTIVO=0424)
   				 "CG.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND E.COD_MOTIVO <> '0424') OR " +
   			     // Modifica del 01/03/2016 Nuova Infrastruttura - FINE ******
   				 "(E.COD_MOTIVO = CG.RV_LOW_VALUE AND " +
   				 "CG.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) and " +
			     
			     //"CG.rv_low_value = E.cod_motivo and  " +
			     //"cg.rv_domain='MOTIVO_PROVVEDIMENTO' and  " +
			     "F.COD_UFFICIO_INSERIMENTO = '"+filtroModel.getCodUfficioInserimento()+"' and " +
			     "DA.EVE_ID_EVENTO=E.ID_EVENTO ";
    	
    	if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and TO_CHAR(DA.DATA_EMISSIONE,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char (DA.DATA_EMISSIONE,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
    	
    	if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and TO_CHAR (DA.data_emissione,'YYYYMMDD') = '" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "'";
			} else {
			    sql += " and trunc(DA.data_emissione) between TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + ",'YYYYMMDD' ) and TO_DATE(" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
			}
		}
		
    	return sql;
    }
 
    private String getQueryConteggioProvvedimentiFcAnnullati (RicercaFogliCompModel filtroModel) {
    	String sql="select to_char(daa.data_emissione,'YYYY') as anno, count (distinct b.id_evento) as conteggio, 'Fogli Complementari Annullati' as descrizione " +
                " from evento ee, DOCUMENTO_ALLEGATO DAA, " + 
    	    	"("+
    	    	this.getQueryProvvedimentiFcAnnullati(filtroModel) +
    	    	" ) B where " +
    	    	" ee.ID_EVENTO=B.ID_EVENTO (+) " +
    	    	" and daa.eve_id_evento=ee.ID_EVENTO " +
    	        " and daa.DATA_EMISSIONE is not NULL "; 
    	    	
    	if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and to_char(daa.data_emissione,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char(daa.data_emissione,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
		
    	if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and daa.data_emissione = TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			} else {
			    sql += " and daa.data_emissione between TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' ) and TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			
			}
    	}

        sql += " group by to_char(daa.data_emissione,'YYYY') ";
    	sql += " order by to_char(daa.data_emissione,'YYYY') ";
    	return sql;
    }
    
    private String getQueryConteggioFcTrasmessiManualmente (RicercaFogliCompModel filtroModel) {
    	String sql="select to_char(daa.data_emissione,'YYYY') as anno, count (distinct b.id_evento) as conteggio, 'Fogli Complementari Iscritti Manualmente o con altre opzioni' as descrizione " +
                " from evento ee, DOCUMENTO_ALLEGATO DAA, " + 
    	    	"("+
    	    	this.getQueryFcTrasmessiManualmente (filtroModel) +
    	    	" ) B where " +
    	    	" ee.ID_EVENTO=B.ID_EVENTO (+) " +
    	    	" and daa.eve_id_evento=ee.ID_EVENTO " +
    	        " and daa.DATA_EMISSIONE is not NULL "; 
    	    	
    	if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and to_char(daa.data_emissione,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char(daa.data_emissione,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
		
    	if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and daa.data_emissione = TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			} else {
			    sql += " and daa.data_emissione between TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' ) and TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			
			}
    	}

        sql += " group by to_char(daa.data_emissione,'YYYY') ";
    	sql += " order by to_char(daa.data_emissione,'YYYY') ";
    	return sql;
    }
    
    
    private String getQueryConteggioProvvedimentiPriviFC (RicercaFogliCompModel filtroModel) {
    	String sql="select to_char(ee.data_emissione,'YYYY') as anno, count (distinct b.id_evento) as conteggio, 'Provvedimenti Privi di Fogli Complementari' as descrizione from evento ee, " + 
    	    	"("+
    	    	this.getQueryProvvedimentiPriviFC(filtroModel)+
    	    	" ) B where " +
    	    	" ee.ID_EVENTO=B.ID_EVENTO (+)" + 
    	        " and ee.DATA_EMISSIONE is not NULL " +
    	        " and to_char(ee.DATA_EMISSIONE, 'YYYY') between 2010 and 2016 ";
    	    	
    	if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and to_char(ee.data_emissione,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char(ee.data_emissione,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
		
    	if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and ee.data_emissione = TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			} else {
			    sql += " and ee.data_emissione between TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' ) and TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			
			}
    	}

        sql += " group by to_char(ee.data_emissione,'YYYY') ";
    	sql += " order by to_char(ee.data_emissione,'YYYY') ";
    	return sql;
    }
    
    private String getQueryConteggioFCNonTrasmessi (RicercaFogliCompModel filtroModel) {
    	String sql="select to_char(daa.data_emissione,'YYYY') as anno, count (distinct b.id_evento) as conteggio, 'Provvedimenti con Fogli Complementari compilati ma non trasmessi' as descrizione " +
                " from evento ee, DOCUMENTO_ALLEGATO DAA, " + 
    	    	"("+
    	    	this.getQueryFcNonTrasmessi(filtroModel) +
    	    	" ) B where " +
    	    	" ee.ID_EVENTO=B.ID_EVENTO (+) " +
    	    	" and daa.eve_id_evento=ee.ID_EVENTO " +
    	        " and daa.DATA_EMISSIONE is not NULL "; 
  
    	if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and to_char(daa.data_emissione,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char(daa.data_emissione,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
		
    	if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and daa.data_emissione = TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			} else {
			    sql += " and daa.data_emissione between TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' ) and TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			
			}
    	}

    	sql += " group by to_char(daa.data_emissione,'YYYY') ";
    	sql += " order by to_char(daa.data_emissione,'YYYY') ";
    	return sql;
    }
    
    private String getQueryConteggioFCTrasmessiConErrore (RicercaFogliCompModel filtroModel) {
    	String sql="select to_char(daa.data_emissione,'YYYY') as anno, count (distinct b.id_evento) as conteggio, 'Fogli Complementari Trasmessi con Errore' as descrizione " +
                " from evento ee, DOCUMENTO_ALLEGATO DAA, " + 
    	    	"("+
    	    	this.getQueryFcTrasmessiConErrore(filtroModel) +
    	    	" ) B where " +
    	    	" ee.ID_EVENTO=B.ID_EVENTO (+) " +
    	    	" and daa.eve_id_evento=ee.ID_EVENTO " +
    	        " and daa.DATA_EMISSIONE is not NULL "; 
  
    	if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and to_char(daa.data_emissione,'YYYY')=" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char(daa.data_emissione,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
		
    	if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and daa.data_emissione = TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			} else {
			    sql += " and daa.data_emissione between TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' ) and TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			
			}
    	}

        sql += " group by to_char(daa.data_emissione,'YYYY') ";
    	sql += " order by to_char(daa.data_emissione,'YYYY') ";
    	return sql;
    }
    
    private String getQueryConteggioProvvedimentiConFC (RicercaFogliCompModel filtroModel) {
    	String sql="select to_char(daa.data_emissione,'YYYY') as anno, count (distinct b.id_evento) as conteggio, 'Fogli Complementari Trasmessi' as descrizione " +
                " from evento ee, DOCUMENTO_ALLEGATO DAA, " + 
    	    	"("+
    	    	this.getQueryFcTrasmessi(filtroModel) +
    	    	" ) B where " +
    	    	" ee.ID_EVENTO=B.ID_EVENTO (+)" +
    	    	" and daa.eve_id_evento=ee.ID_EVENTO " +
    	        " and daa.DATA_EMISSIONE is not NULL "; 
    	    	
    	if (filtroModel.getAnnoIniziale() != null) {
		    if (filtroModel.getAnnoFinale() == null)
			    sql += " and to_char(daa.data_emissione,'YYYY') =" + filtroModel.getAnnoIniziale();
		    if (filtroModel.getAnnoFinale() != null)
			    sql += " and to_char(daa.data_emissione,'YYYY') between " + filtroModel.getAnnoIniziale() +" and " + filtroModel.getAnnoFinale();
		}
		
    	if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and daa.data_emissione = TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			} else {
			    sql += " and daa.data_emissione between TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneIniziale(), "yyyyMMdd" ) + "','YYYYMMDD' ) and TO_DATE('" + DateUtils.getDateToString( filtroModel.getDataEmissioneFinale(), "yyyyMMdd" ) + "','YYYYMMDD' )";
			
			}
    	}
 
        sql += " group by to_char(daa.data_emissione,'YYYY') ";
    	sql += " order by to_char(daa.data_emissione,'YYYY') ";
    	return sql;
    }

	public StatisticheFogliComplementariModel getModel () throws DAOException{
		StatisticheFogliComplementariModel model=new StatisticheFogliComplementariModel();
		model.setIdFascicolo(super.getBigDecimal("PRG"));
		model.setDescrFascicolo(super.getString("num_fasc_SIEP"));
		model.setDataProvvedimento(super.getDate("data_emissione_provv"));
		model.setDataFoglioComplementare(super.getString("DATA_EMISSIONE_FOGLIO"));
		model.setDescrEsito(super.getString("esito"));
		model.setDescrProvvedimento(super.getString("motivo"));
		return model;
	}
	
	public RiepilogoStatisticheFogliComplementari getRiepilogoModel () throws DAOException{
		RiepilogoStatisticheFogliComplementari model=new RiepilogoStatisticheFogliComplementari();
		model.setAnno(super.getBigDecimal("anno"));
		model.setConteggio(super.getBigDecimal("conteggio"));
		model.setDescrizione(super.getString("descrizione"));
		return model;
	}

	public Vector <StatisticheFogliComplementariModel>getLista () throws DAOException{
		Vector <StatisticheFogliComplementariModel>lElencoFC=new  Vector <StatisticheFogliComplementariModel>(); 
        start();
        while(next()) {
            lElencoFC.add(getModel());
        }
        stop();
        return lElencoFC;
	}
	
	public Vector <RiepilogoStatisticheFogliComplementari> getListaRiepilogo() throws DAOException{
		Vector <RiepilogoStatisticheFogliComplementari> lista=new Vector<RiepilogoStatisticheFogliComplementari>();
		start();
	    while (next()) {
	        lista.add(this.getRiepilogoModel());
	    }
	    stop();
	    return lista;
	}
}
