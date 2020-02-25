package siap.sius.udienzaprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: ProcedimentixUdienzaSqlDAO</p>
* <p>Description: Classe SqlDAO che consente l'accesso ai Procedimenti </p>
* fissati per un'Udienza la tabella UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ProcedimentixUdienzaSqlDAO extends SIAPSqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public ProcedimentixUdienzaSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //

  /**
   * Metodo che imposta le condizioni di ricerca, dei procedimenti per udienza.
   * <p>
   * @param aIdUdienza id dell'udienza.
   * @param aOrderBy tipo di ordinamento.
   */
  public void ricercaProcedimentixUdienzaByUdienza( BigDecimal aIdUdienza, String aOrderBy )
  throws DAOException
  {
    String lSqlOrder = "";
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("ricercaProcedimentixUdienzaByUdienza: OrderBy -> "+ aOrderBy);

    if(aOrderBy.substring(0,1).compareTo("M") != 0 ) // Per Codice Magistrato
       {lSqlOrder = " ORDER BY ";}
    else
       {lSqlOrder = " ORDER BY COGNOMEMAG, NOMEMAG, COD_MAGISTRATO, ";}

    if (aOrderBy.equals("PP")== true || aOrderBy.equals("MPP")== true)    // Per Progressivo Procedimento
       lSqlOrder += " CHIAVE_ANNO, CHIAVE_PROGR ";
    else if (aOrderBy.equals("CNS")== true || aOrderBy.equals("MCNS")== true)    // Per Cognome/Nome Soggetto
       lSqlOrder += " COGNOME, NOME ";
    else if(aOrderBy.equals("PGCNS") == true || aOrderBy.equals("MPGCNS")== true) // Per Posizione Giuridica e Cognome/Nome Soggetto
       lSqlOrder += " DESC_POSIZIONE_GIURIDICASIUS, DESC_POSIZIONE_GIURIDICA, COGNOME, NOME";
    else if(aOrderBy.equals("PGPP") == true || aOrderBy.equals("MPGPP")== true) // Per Posizione Giuridica e Progressivo Procedimento
       lSqlOrder += " DESC_POSIZIONE_GIURIDICASIUS, DESC_POSIZIONE_GIURIDICA, CHIAVE_ANNO, CHIAVE_PROGR ";
    else
      lSqlOrder = " ORDER BY COGNOMEMAG, NOMEMAG, COD_MAGISTRATO ";

    String lSql = getSqlQuery(aIdUdienza);
    String lSql2 = getSqlQuery2(aIdUdienza);
    
    lSql += " " + setCondizioniByKey(aIdUdienza);
    lSql += " AND  udienza_procedimento.flag_rinviata not in ('M','A')";
    //lSql += " AND  to_char(UD_RINVIATA.DATA_RINVIO ,'yyyy-mm-dd') != to_char(DATA_UDIENZA, 'yyyy-mm-dd')" ;
    lSql2 += " " + setCondizioniByKey(aIdUdienza);
    lSql2 += " AND  udienza_procedimento.flag_rinviata not in ('M','A')";
    
    // Aggiunta della AND condition solo per il 2° statement in UNION
    lSql2 += " AND NOT EXISTS " +
    		 " (SELECT ID_EVENTO FROM EVENTO WHERE FASCICOLO_SIUS.ID_FASCICOLO_SIUS = EVENTO.FAS_SIU_ID_FASCICOLO_SIUS " +
    		 "AND (EVENTO.DATA_INSERIMENTO,FASCICOLO_SIUS.ID_FASCICOLO_SIUS) " + 
			 "	in (   SELECT  MAX(DATA_INSERIMENTO), FAS_SIU_ID_FASCICOLO_SIUS " +
			 "			FROM EVENTO " +
			 "			WHERE (COD_TIPO_PROVVEDIMENTO = '03' or " +
			 "				(COD_TIPO_PROVVEDIMENTO = '02' and EVENTO.cod_esito = '0601')) " +
			 "				AND (FLAG_DOCUMENTO_REGISTRATO <> 'A' or " +
			 "					FLAG_DOCUMENTO_REGISTRATO IS NULL ) " +
			 "				AND NOT (FAS_SIU_ID_FASCICOLO_SIUS IS NULL) " + 
			 "			GROUP BY   FAS_SIU_ID_FASCICOLO_SIUS )  ) ";
    
    String lSqlUnion = lSql + " UNION " + lSql2;
    lSqlUnion += lSqlOrder;
    setStatement(lSqlUnion);
  }

  /**
   * Metodo di ricerca procedimenti per udienza.
   * <p>
   * @param aIdUdienza id dell'udienza.
   * @param aOrderBy tipo di ordinamnto.
   * @param aStatoProcedimento tipo stato del procedimento.
   * @param aTipoProc tipo procedimento.
   * @throws DAOException propga errore di eccezione.
   */
  public void ricercaProcedimentixUdienzaByUdienza( BigDecimal aIdUdienza, String aOrderBy, String aStatoProcedimento, String aTipoProc)
  throws DAOException
  {
    String lSqlOrder = "";
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("ricercaProcedimentixUdienzaByUdienza: OrderBy -> "+ aOrderBy);

    if(aOrderBy.substring(0,1).compareTo("M") != 0 ) // Per Codice Magistrato
       {lSqlOrder = " ORDER BY ";}
      else
       {lSqlOrder = " ORDER BY COGNOMEMAG, NOMEMAG, COD_MAGISTRATO, ";}

    if (aOrderBy.equals("PP")== true || aOrderBy.equals("MPP")== true)    // Per Progressivo Procedimento
       lSqlOrder += " CHIAVE_ANNO, CHIAVE_PROGR ";
    else if (aOrderBy.equals("CNS")== true || aOrderBy.equals("MCNS")== true)    // Per Cognome/Nome Soggetto
       lSqlOrder += " COGNOME, NOME ";
    else if(aOrderBy.equals("PGCNS") == true || aOrderBy.equals("MPGCNS")== true) // Per Posizione Giuridica e Cognome/Nome Soggetto
       lSqlOrder += " DESC_POSIZIONE_GIURIDICASIUS, DESC_POSIZIONE_GIURIDICA, COGNOME, NOME";
    else if(aOrderBy.equals("PGPP") == true || aOrderBy.equals("MPGPP")== true) // Per Posizione Giuridica e Progressivo Procedimento
       lSqlOrder += " DESC_POSIZIONE_GIURIDICASIUS, DESC_POSIZIONE_GIURIDICA, CHIAVE_ANNO, CHIAVE_PROGR ";
    else
      lSqlOrder = " ORDER BY COGNOMEMAG, NOMEMAG, COD_MAGISTRATO ";

    String lSql = getSqlQuery(aIdUdienza);
    lSql += " " + setCondizioniByKey(aIdUdienza);
    lSql += " AND  udienza_procedimento.flag_rinviata not in ('M','A')";
    
    String lSql2 = getSqlQuery2(aIdUdienza);
    lSql2 += " " + setCondizioniByKey(aIdUdienza);
    lSql2 += " AND  udienza_procedimento.flag_rinviata not in ('M','A')";
   
    // 12/04/2007 Aggiunti filtri per "Procedimenti Unificati" e per "Stato fissazione udienza"
    // 22/05/2007 Gestione filtro per "Stato procedimento"
    if(aStatoProcedimento.compareTo("D") == 0 )
    {
      lSql += " AND (FASCICOLO_SIUS.COD_STATO_FASCICOLO IN ('07', '05', '10') ";
      lSql += " OR udienza_procedimento.flag_rinviata in ('R') ) ";
      lSql2 += " AND (FASCICOLO_SIUS.COD_STATO_FASCICOLO IN ('07', '05', '10') ";
      lSql2 += " OR udienza_procedimento.flag_rinviata in ('R') ) ";
    }
    else if(aStatoProcedimento.compareTo("U") != 0 )
    {
      lSql += " AND FASCICOLO_SIUS.COD_STATO_FASCICOLO NOT IN ('05') ";
      lSql2 += " AND FASCICOLO_SIUS.COD_STATO_FASCICOLO NOT IN ('05') ";
    }

    if(aTipoProc.compareTo("FISSATI") == 0 )
    {
      lSql += " AND  udienza_procedimento.flag_rinviata in ('F','S','R')";
      lSql2 += " AND  udienza_procedimento.flag_rinviata in ('F','S','R')";
    }

    else if(aTipoProc.compareTo("PREFISSATI") == 0 )
    {
      lSql += " AND  udienza_procedimento.flag_rinviata in ('P')";
      lSql2 += " AND  udienza_procedimento.flag_rinviata in ('P')";
    }

    else if(aTipoProc.compareTo("TUTTI") == 0 )
    {
      lSql += " AND  udienza_procedimento.flag_rinviata in ('F','S','R','P','N')";
      lSql2 += " AND  udienza_procedimento.flag_rinviata in ('F','S','R','P','N')";
    }

    // Aggiunta della AND condition solo per il 2° statement in UNION
    lSql2 += " AND NOT EXISTS " +
    		 " (SELECT ID_EVENTO FROM EVENTO WHERE FASCICOLO_SIUS.ID_FASCICOLO_SIUS = EVENTO.FAS_SIU_ID_FASCICOLO_SIUS " +
    		 "AND (EVENTO.DATA_INSERIMENTO,FASCICOLO_SIUS.ID_FASCICOLO_SIUS) " + 
			 "	in (   SELECT  MAX(DATA_INSERIMENTO), FAS_SIU_ID_FASCICOLO_SIUS " +
			 "			FROM EVENTO " +
			 "			WHERE (COD_TIPO_PROVVEDIMENTO = '03' or " +
			 "				(COD_TIPO_PROVVEDIMENTO = '02' and EVENTO.cod_esito = '0601')) " +
			 "				AND (FLAG_DOCUMENTO_REGISTRATO <> 'A' or " +
			 "					FLAG_DOCUMENTO_REGISTRATO IS NULL ) " +
			 "				AND NOT (FAS_SIU_ID_FASCICOLO_SIUS IS NULL) " + 
			 "			GROUP BY   FAS_SIU_ID_FASCICOLO_SIUS )  ) ";
    
    String lSqlUnion = lSql + " UNION " + lSql2;
    lSqlUnion += lSqlOrder;
    setStatement(lSqlUnion);
  }

  /**
   * Metodo di ricerca procedimenti per data udienza.
   * <p>
   * @param aDataUdienza data udienza.
   * @param aOrderBy tipo di ordinamnto.
   * @param aStatoProcedimento tipo stato del procedimento.
   * @param aTipoProc tipo procedimento.
   * @throws DAOException propga errore di eccezione.
   */
  public void ricercaProcedimentixUdienzaByDataUdienza( Date aDataUdienza, String aOrderBy, String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso)
  throws DAOException
  {
    String lSqlOrder = "";
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("ricercaProcedimentixUdienzaByDataUdienza: OrderBy -> "+ aOrderBy);

    if(aOrderBy.substring(0,1).compareTo("M") != 0 ) // Per Codice Magistrato
      lSqlOrder = " ORDER BY ";
    else
      lSqlOrder = " ORDER BY COGNOMEMAG, NOMEMAG, COD_MAGISTRATO, ";

    if (aOrderBy.equals("PP")== true || aOrderBy.equals("MPP")== true)    // Per Progressivo Procedimento
       lSqlOrder += " CHIAVE_ANNO, CHIAVE_PROGR ";
    else if (aOrderBy.equals("CNS")== true || aOrderBy.equals("MCNS")== true)    // Per Cognome/Nome Soggetto
       lSqlOrder += " COGNOME, NOME";
    else if(aOrderBy.equals("PGCNS") == true || aOrderBy.equals("MPGCNS")== true) // Per Posizione Giuridica e Cognome/Nome Soggetto
       lSqlOrder += " DESC_POSIZIONE_GIURIDICASIUS, DESC_POSIZIONE_GIURIDICA, COGNOME, NOME";
    else if(aOrderBy.equals("PGPP") == true || aOrderBy.equals("MPGPP")== true) // Per Posizione Giuridica e Progressivo Procedimento
       lSqlOrder += " DESC_POSIZIONE_GIURIDICASIUS, DESC_POSIZIONE_GIURIDICA, CHIAVE_ANNO, CHIAVE_PROGR ";
    else
      lSqlOrder = " ORDER BY COGNOMEMAG, NOMEMAG, MAGISTRATO.COD_MAGISTRATO ";

    String lSql = getSqlQuery(aDataUdienza);
    String lSql2 = getSqlQuery2(aDataUdienza);
    
    lSql += " " + setCondizioniByDate(aDataUdienza);
    lSql += " AND  udienza_procedimento.flag_rinviata not in ('M','A')";
    lSql2 += " " + setCondizioniByDate(aDataUdienza);
    lSql2 += " AND  udienza_procedimento.flag_rinviata not in ('M','A')";
    
    // 12/04/2007 Aggiunti filtri per "Procedimenti Unificati" e per "Stato fissazione udienza"
    // 22/05/2007 Gestione filtro per "Stato procedimento"
    if(aStatoProcedimento.compareTo("D") == 0 )
    {
      lSql += " AND (FASCICOLO_SIUS.COD_STATO_FASCICOLO IN ('07', '05', '10') ";
      lSql += " OR udienza_procedimento.flag_rinviata in ('R') ) ";
      lSql2 += " AND (FASCICOLO_SIUS.COD_STATO_FASCICOLO IN ('07', '05', '10') ";
      lSql2 += " OR udienza_procedimento.flag_rinviata in ('R') ) ";
    }
    else if(aStatoProcedimento.compareTo("U") != 0 )
    {
      lSql += " AND FASCICOLO_SIUS.COD_STATO_FASCICOLO NOT IN ('05') ";
      lSql2 += " AND FASCICOLO_SIUS.COD_STATO_FASCICOLO NOT IN ('05') ";
    }

    if(aTipoProc.compareTo("FISSATI") == 0 )
    {
      lSql += " AND  udienza_procedimento.flag_rinviata in ('F','S','R')";
      lSql2 += " AND  udienza_procedimento.flag_rinviata in ('F','S','R')";
    }
    else if(aTipoProc.compareTo("PREFISSATI") == 0 )
    {
      lSql += " AND  udienza_procedimento.flag_rinviata in ('P')";
      lSql2 += " AND  udienza_procedimento.flag_rinviata in ('P')";
    }
    else if(aTipoProc.compareTo("TUTTI") == 0 )
    {
      lSql += " AND  udienza_procedimento.flag_rinviata in ('F','S','R','P','N')";
      lSql2 += " AND  udienza_procedimento.flag_rinviata in ('F','S','R','P','N')";
    }

    // 16/12/2008 Introdotto il parametro COD_UFFICIO per estrarre le sole udienze pertinenti.
    if(aCodUfficioConnesso != null && aCodUfficioConnesso.length()>1 )
    {
      lSql += " AND  udienza.cod_ufficio_appartenenza = '"+aCodUfficioConnesso+"'" ;
      lSql2 += " AND  udienza.cod_ufficio_appartenenza = '"+aCodUfficioConnesso+"'" ;
    }
    
    // Aggiunta della AND condition solo per il 2° statement in UNION
    lSql2 += " AND NOT EXISTS " +
    		 " (SELECT ID_EVENTO FROM EVENTO WHERE FASCICOLO_SIUS.ID_FASCICOLO_SIUS = EVENTO.FAS_SIU_ID_FASCICOLO_SIUS " +
    		 "AND (EVENTO.DATA_INSERIMENTO,FASCICOLO_SIUS.ID_FASCICOLO_SIUS) " + 
			 "	in (   SELECT  MAX(DATA_INSERIMENTO), FAS_SIU_ID_FASCICOLO_SIUS " +
			 "			FROM EVENTO " +
			 "			WHERE (COD_TIPO_PROVVEDIMENTO = '03' or " +
			 "				(COD_TIPO_PROVVEDIMENTO = '02' and EVENTO.cod_esito = '0601')) " +
			 "				AND (FLAG_DOCUMENTO_REGISTRATO <> 'A' or " +
			 "					FLAG_DOCUMENTO_REGISTRATO IS NULL ) " +
			 "				AND NOT (FAS_SIU_ID_FASCICOLO_SIUS IS NULL) " + 
			 "			GROUP BY   FAS_SIU_ID_FASCICOLO_SIUS )  ) ";
    
    String lSqlUnion = lSql + " UNION " + lSql2;
    lSqlUnion += lSqlOrder;
    setStatement(lSqlUnion);
  }

  protected String getSqlQuery(Object aValue)throws DAOException
  {
    String lStatement = new String("");
    lStatement += "SELECT DISTINCT " +
    "  FASCICOLO_SIUS.ID_FASCICOLO_SIUS, FASCICOLO_SIUS.CHIAVE_ANNO CHIAVE_ANNO, FASCICOLO_SIUS.CHIAVE_PROGR CHIAVE_PROGR, FASCICOLO_SIUS.COD_STATO_FASCICOLO" +
    ", SOGGETTO.ID_SOGGETTO, SOGGETTO.COGNOME COGNOME, SOGGETTO.NOME NOME " +
    ", UDIENZA.DATA_UDIENZA ,UDIENZA.ID_UDIENZA, UDIENZA.NUM_COLLEGIO" +
    ", CG.RV_MEANING as OGGETTO_PROCEDIMENTO, GENERALE_PROCEDIMENTO.SEZIONE,GENERALE_PROCEDIMENTO.COD_OGGETTO_PROCEDIMENTO " +
    ", UDIENZA_PROCEDIMENTO.FLAG_RINVIATA" +
    ", EVENTO.COD_ESITO COD_ESITO, DESC_ESITO_PROVVEDIMENTO.RV_MEANING DESC_ESITO_PROVVEDIMENTO " +
    ", EVENTO.COD_MOTIVO, DESC_MOTIVO_PROVVEDIMENTO.RV_MEANING DESC_MOTIVO_PROVVEDIMENTO " +
    ", DESC_FLAG_UDIENZA.RV_MEANING || ' ' || UD_RINVIATA.DATA_RINVIO  DESC_STATO_UDIENZA "+
    ", DESC_POSIZIONE_GIURIDICASIUS.RV_MEANING DESC_POSIZIONE_GIURIDICASIUS "+
    ", DESC_POSIZIONE_GIURIDICA.RV_MEANING DESC_POSIZIONE_GIURIDICA " +
    ", ID_EVENTO,MAGISTRATO.COGNOME COGNOMEMAG,MAGISTRATO.NOME NOMEMAG,MAGISTRATO.COD_MAGISTRATO COD_MAGISTRATO " +
    ", ESPERTO.COGNOME COGNOMEESP,ESPERTO.NOME NOMEESP,ESPERTO.ID_ESPERTO  " +
    " FROM " +
    " UDIENZA" +
//    " INNER JOIN UDIENZA_PROCEDIMENTO ON UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA = UDIENZA.ID_UDIENZA" +
	" INNER JOIN" + 
	" (SELECT * FROM udienza_procedimento WHERE id_udienza_procedimento IN" +
	" (SELECT MAX(id_udienza_procedimento)" + 
	" FROM udienza_procedimento GROUP BY udi_id_udienza, gen_prid_generale_procedimento))" +
	" UDIENZA_PROCEDIMENTO" +
	" ON UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA = UDIENZA.ID_UDIENZA" + 
    " INNER JOIN GENERALE_PROCEDIMENTO ON GENERALE_PROCEDIMENTO.ID_GENERALE_PROCEDIMENTO = UDIENZA_PROCEDIMENTO.GEN_PRID_GENERALE_PROCEDIMENTO" +
    " INNER JOIN CG_REF_CODES CG ON CG.RV_LOW_VALUE = GENERALE_PROCEDIMENTO.COD_OGGETTO_PROCEDIMENTO AND CG.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'  " +
    " INNER JOIN FASCICOLO_SIUS ON FASCICOLO_SIUS.ID_FASCICOLO_SIUS = GENERALE_PROCEDIMENTO.FAS_SIU_ID_FASCICOLO_SIUS" +
    " INNER JOIN SOGGETTO ON SOGGETTO.ID_SOGGETTO = FASCICOLO_SIUS.SOG_ID_SOGGETTO " +
    " INNER JOIN EVENTO ON  EVENTO.FAS_SIU_ID_FASCICOLO_SIUS = FASCICOLO_SIUS.ID_FASCICOLO_SIUS  " +
    " AND (EVENTO.DATA_INSERIMENTO,FASCICOLO_SIUS.ID_FASCICOLO_SIUS) in (  " +
      " SELECT " +
      " MAX(DATA_INSERIMENTO)," +
      " FAS_SIU_ID_FASCICOLO_SIUS" +
      " FROM EVENTO" +
      " WHERE" +
      " (COD_TIPO_PROVVEDIMENTO = '03' or (COD_TIPO_PROVVEDIMENTO = '02' and EVENTO.cod_esito = '0601'))" +
      " AND (FLAG_DOCUMENTO_REGISTRATO <> 'A' or FLAG_DOCUMENTO_REGISTRATO IS NULL ) " +
      " AND NOT (FAS_SIU_ID_FASCICOLO_SIUS IS NULL)" +
      " GROUP BY   FAS_SIU_ID_FASCICOLO_SIUS" +
      " ) " +
    " LEFT OUTER JOIN CG_REF_CODES DESC_FLAG_UDIENZA ON UDIENZA_PROCEDIMENTO.FLAG_RINVIATA = DESC_FLAG_UDIENZA.RV_LOW_VALUE AND DESC_FLAG_UDIENZA.RV_DOMAIN ='STATO_FLAG_RINVIATA' "+
    " LEFT OUTER JOIN CG_REF_CODES DESC_ESITO_PROVVEDIMENTO ON EVENTO.COD_ESITO = DESC_ESITO_PROVVEDIMENTO.RV_LOW_VALUE AND DESC_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO'   " +
    " LEFT OUTER JOIN CG_REF_CODES DESC_MOTIVO_PROVVEDIMENTO ON EVENTO.COD_MOTIVO = DESC_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE AND DESC_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO'   "+
 
    /* Sostituita dal pezzo sotto  Michele 13-11-2008
    " LEFT OUTER JOIN POSIZIONE_GIURIDICA ON POSIZIONE_GIURIDICA.FAS_SIE_ID_FASCICOLO_SIEP =  FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP "+
    " and POSIZIONE_GIURIDICA.data_fine is null "+
     // correzione  per evitare di replicare occorrenze con per lo stesso fascicolo su + posizioni giuridiche Enzo 16-06-2008
    " and POSIZIONE_GIURIDICA.data_inserimento = (SELECT MAX  (data_inserimento) from posizione_giuridica where POSIZIONE_GIURIDICA.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP) "+
    " LEFT OUTER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICA ON DESC_POSIZIONE_GIURIDICA.RV_LOW_VALUE = POSIZIONE_GIURIDICA.COD_POSIZIONE_GIURIDICA " +
 */
    // Sostituzione Michele 01-04-2011
    " LEFT OUTER JOIN (select pg.FAS_SIE_ID_FASCICOLO_SIEP,  pg.cod_posizione_giuridica, pg.DATA_INSERIMENTO from posizione_giuridica pg " + 
    " inner join ( SELECT  FAS_SIE_ID_FASCICOLO_SIEP, MAX(data_inserimento) fsdatamax FROM POSIZIONE_GIURIDICA " + 
    " WHERE data_fine IS NULL GROUP BY FAS_SIE_ID_FASCICOLO_SIEP ) fsmax " +
    " on pg.FAS_SIE_ID_FASCICOLO_SIEP = fsmax.FAS_SIE_ID_FASCICOLO_SIEP and pg.DATA_INSERIMENTO = fsmax.fsdatamax) " +
    " POSIZIONE_GIURIDICA_MAX " +
    " ON POSIZIONE_GIURIDICA_MAX.FAS_SIE_ID_FASCICOLO_SIEP =  FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP " +   
    " LEFT OUTER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICA ON DESC_POSIZIONE_GIURIDICA.RV_LOW_VALUE = POSIZIONE_GIURIDICA_MAX.COD_POSIZIONE_GIURIDICA " +
   // Fine sostituzione
    " AND DESC_POSIZIONE_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' "+
    " LEFT OUTER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICASIUS ON DESC_POSIZIONE_GIURIDICASIUS.RV_LOW_VALUE = GENERALE_PROCEDIMENTO.COD_POSIZIONE_GIURIDICA AND DESC_POSIZIONE_GIURIDICASIUS.RV_DOMAIN = 'POSIZIONE_GIURIDICA' "+
    " LEFT OUTER JOIN "+
    " ( SELECT DISTINCT UDIENZA.NUM_COLLEGIO NUM_COLLEGIO_RINVIO,  to_char(UD2.DATA_UDIENZA, 'dd-mm-yyyy')  ||' Collegio n°'||UD2.NUM_COLLEGIO DATA_RINVIO, UDIENZA_PROCEDIMENTO.ID_UDIENZA_PROCEDIMENTO, UDIENZA_PROCEDIMENTO.GEN_PRID_GENERALE_PROCEDIMENTO "+
    "   FROM UDIENZA "+
    "   INNER JOIN UDIENZA_PROCEDIMENTO ON UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA = UDIENZA.ID_UDIENZA  " +
    "   INNER JOIN UDIENZA UD2 ON UD2.ID_UDIENZA = UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA_RINVIO ";
   
    
    if( aValue instanceof java.math.BigDecimal )
      lStatement += setCondizioniByKey((BigDecimal)aValue) + ") ";
    else if ( aValue instanceof java.util.Date )
      lStatement += setCondizioniByDate((Date)aValue) + ") ";
    else
      throw new DAOException("Il valore passato non è del tipo : BigDecimal ( IDUdienza ) o Date ( DataUdienza ).");
    
    lStatement +=" UD_RINVIATA ON UDIENZA_PROCEDIMENTO.ID_UDIENZA_PROCEDIMENTO= UD_RINVIATA.ID_UDIENZA_PROCEDIMENTO AND GENERALE_PROCEDIMENTO.ID_GENERALE_PROCEDIMENTO = UD_RINVIATA.GEN_PRID_GENERALE_PROCEDIMENTO AND UDIENZA_PROCEDIMENTO.FLAG_RINVIATA = 'R' "+
    " LEFT OUTER JOIN MAGISTRATO_RELATORE ON MAGISTRATO_RELATORE.FAS_SIU_ID_FASCICOLO_SIUS=  FASCICOLO_SIUS.ID_FASCICOLO_SIUS AND MAGISTRATO_RELATORE.DATA_FINE IS NULL"+
    " LEFT OUTER JOIN MAGISTRATO ON MAGISTRATO_RELATORE.MAG_COD_MAGISTRATO=MAGISTRATO.COD_MAGISTRATO" +
    " LEFT OUTER JOIN ESPERTO ON MAGISTRATO_RELATORE.ESP_ID_ESPERTO=ESPERTO.ID_ESPERTO";
    
    return lStatement;
  }
  protected String getSqlQuery2(Object aValue)throws DAOException
  {
    String lStatement = new String("");
    lStatement += "SELECT DISTINCT " +
    "  FASCICOLO_SIUS.ID_FASCICOLO_SIUS, FASCICOLO_SIUS.CHIAVE_ANNO CHIAVE_ANNO, FASCICOLO_SIUS.CHIAVE_PROGR CHIAVE_PROGR, FASCICOLO_SIUS.COD_STATO_FASCICOLO" +
    ", SOGGETTO.ID_SOGGETTO, SOGGETTO.COGNOME COGNOME, SOGGETTO.NOME NOME " +
    ", UDIENZA.DATA_UDIENZA ,UDIENZA.ID_UDIENZA, UDIENZA.NUM_COLLEGIO" +
    ", CG.RV_MEANING as OGGETTO_PROCEDIMENTO, GENERALE_PROCEDIMENTO.SEZIONE,GENERALE_PROCEDIMENTO.COD_OGGETTO_PROCEDIMENTO " +
    ", UDIENZA_PROCEDIMENTO.FLAG_RINVIATA" +
    ", '' COD_ESITO , '' DESC_ESITO_PROVVEDIMENTO " +
    ", '' COD_MOTIVO, '' DESC_MOTIVO_PROVVEDIMENTO " +
    ", DESC_FLAG_UDIENZA.RV_MEANING || ' ' || UD_RINVIATA.DATA_RINVIO  DESC_STATO_UDIENZA "+
    ", DESC_POSIZIONE_GIURIDICASIUS.RV_MEANING DESC_POSIZIONE_GIURIDICASIUS "+
    ", DESC_POSIZIONE_GIURIDICA.RV_MEANING DESC_POSIZIONE_GIURIDICA " +
    ", null ID_EVENTO,MAGISTRATO.COGNOME COGNOMEMAG,MAGISTRATO.NOME NOMEMAG,MAGISTRATO.COD_MAGISTRATO COD_MAGISTRATO " +
    ", ESPERTO.COGNOME COGNOMEESP,ESPERTO.NOME NOMEESP,ESPERTO.ID_ESPERTO  " +
    " FROM " +
    " UDIENZA" +
//    " INNER JOIN UDIENZA_PROCEDIMENTO ON UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA = UDIENZA.ID_UDIENZA" +
	" INNER JOIN" + 
	" (SELECT * FROM udienza_procedimento WHERE id_udienza_procedimento IN" +
	" (SELECT MAX(id_udienza_procedimento)" + 
	" FROM udienza_procedimento GROUP BY udi_id_udienza, gen_prid_generale_procedimento))" +
	" UDIENZA_PROCEDIMENTO" +
	" ON UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA = UDIENZA.ID_UDIENZA" + 
    " INNER JOIN GENERALE_PROCEDIMENTO ON GENERALE_PROCEDIMENTO.ID_GENERALE_PROCEDIMENTO = UDIENZA_PROCEDIMENTO.GEN_PRID_GENERALE_PROCEDIMENTO" +
    " INNER JOIN CG_REF_CODES CG ON CG.RV_LOW_VALUE = GENERALE_PROCEDIMENTO.COD_OGGETTO_PROCEDIMENTO AND CG.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'  " +
    " INNER JOIN FASCICOLO_SIUS ON FASCICOLO_SIUS.ID_FASCICOLO_SIUS = GENERALE_PROCEDIMENTO.FAS_SIU_ID_FASCICOLO_SIUS" +
    " INNER JOIN SOGGETTO ON SOGGETTO.ID_SOGGETTO = FASCICOLO_SIUS.SOG_ID_SOGGETTO " +
    " LEFT OUTER JOIN CG_REF_CODES DESC_FLAG_UDIENZA ON UDIENZA_PROCEDIMENTO.FLAG_RINVIATA = DESC_FLAG_UDIENZA.RV_LOW_VALUE AND DESC_FLAG_UDIENZA.RV_DOMAIN ='STATO_FLAG_RINVIATA' "+
    
    /* Sostituita dal pezzo sotto  Michele 13-11-2008
    " LEFT OUTER JOIN POSIZIONE_GIURIDICA ON POSIZIONE_GIURIDICA.FAS_SIE_ID_FASCICOLO_SIEP =  FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP "+
    " and POSIZIONE_GIURIDICA.data_fine is null "+
     // correzione  per evitare di replicare occorrenze con per lo stesso fascicolo su + posizioni giuridiche Enzo 16-06-2008
    " and POSIZIONE_GIURIDICA.data_inserimento = (SELECT MAX  (data_inserimento) from posizione_giuridica where POSIZIONE_GIURIDICA.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP) "+
    " LEFT OUTER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICA ON DESC_POSIZIONE_GIURIDICA.RV_LOW_VALUE = POSIZIONE_GIURIDICA.COD_POSIZIONE_GIURIDICA " +
 */
    // Sostituzione Michele 01-04-2011
    " LEFT OUTER JOIN (select pg.FAS_SIE_ID_FASCICOLO_SIEP,  pg.cod_posizione_giuridica, pg.DATA_INSERIMENTO from posizione_giuridica pg " + 
    " inner join ( SELECT  FAS_SIE_ID_FASCICOLO_SIEP, MAX(data_inserimento) fsdatamax FROM POSIZIONE_GIURIDICA " + 
    " WHERE data_fine IS NULL GROUP BY FAS_SIE_ID_FASCICOLO_SIEP ) fsmax " +
    " on pg.FAS_SIE_ID_FASCICOLO_SIEP = fsmax.FAS_SIE_ID_FASCICOLO_SIEP and pg.DATA_INSERIMENTO = fsmax.fsdatamax) " +
    " POSIZIONE_GIURIDICA_MAX " +
    " ON POSIZIONE_GIURIDICA_MAX.FAS_SIE_ID_FASCICOLO_SIEP =  FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP " +   
    " LEFT OUTER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICA ON DESC_POSIZIONE_GIURIDICA.RV_LOW_VALUE = POSIZIONE_GIURIDICA_MAX.COD_POSIZIONE_GIURIDICA " +
   // Fine sostituzione
    " AND DESC_POSIZIONE_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' "+
    " LEFT OUTER JOIN CG_REF_CODES DESC_POSIZIONE_GIURIDICASIUS ON DESC_POSIZIONE_GIURIDICASIUS.RV_LOW_VALUE = GENERALE_PROCEDIMENTO.COD_POSIZIONE_GIURIDICA AND DESC_POSIZIONE_GIURIDICASIUS.RV_DOMAIN = 'POSIZIONE_GIURIDICA' "+
    " LEFT OUTER JOIN "+
    " ( SELECT DISTINCT UDIENZA.NUM_COLLEGIO NUM_COLLEGIO_RINVIO,  to_char(UD2.DATA_UDIENZA, 'dd-mm-yyyy')  ||' Collegio n°'||UD2.NUM_COLLEGIO DATA_RINVIO, UDIENZA_PROCEDIMENTO.ID_UDIENZA_PROCEDIMENTO, UDIENZA_PROCEDIMENTO.GEN_PRID_GENERALE_PROCEDIMENTO "+
    "   FROM UDIENZA "+
    "   INNER JOIN UDIENZA_PROCEDIMENTO ON UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA = UDIENZA.ID_UDIENZA  " +
    "   INNER JOIN UDIENZA UD2 ON UD2.ID_UDIENZA = UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA_RINVIO ";
   
    
    if( aValue instanceof java.math.BigDecimal )
      lStatement += setCondizioniByKey((BigDecimal)aValue) + ") ";
    else if ( aValue instanceof java.util.Date )
      lStatement += setCondizioniByDate((Date)aValue) + ") ";
    else
      throw new DAOException("Il valore passato non è del tipo : BigDecimal ( IDUdienza ) o Date ( DataUdienza ).");
    
    lStatement +=" UD_RINVIATA ON UDIENZA_PROCEDIMENTO.ID_UDIENZA_PROCEDIMENTO= UD_RINVIATA.ID_UDIENZA_PROCEDIMENTO AND GENERALE_PROCEDIMENTO.ID_GENERALE_PROCEDIMENTO = UD_RINVIATA.GEN_PRID_GENERALE_PROCEDIMENTO AND UDIENZA_PROCEDIMENTO.FLAG_RINVIATA = 'R' "+
    " LEFT OUTER JOIN MAGISTRATO_RELATORE ON MAGISTRATO_RELATORE.FAS_SIU_ID_FASCICOLO_SIUS=  FASCICOLO_SIUS.ID_FASCICOLO_SIUS AND MAGISTRATO_RELATORE.DATA_FINE IS NULL"+
    " LEFT OUTER JOIN MAGISTRATO ON MAGISTRATO_RELATORE.MAG_COD_MAGISTRATO=MAGISTRATO.COD_MAGISTRATO" +
    " LEFT OUTER JOIN ESPERTO ON MAGISTRATO_RELATORE.ESP_ID_ESPERTO=ESPERTO.ID_ESPERTO";
    
    return lStatement;
  }

  
  //
  // METODO GETMODEL()
  //
  /**
   * Metodo che recupera i dati dal resulset e ne popola il model.
   * <p>
   * @return istanza model ProcedimentixUdienzaModel
   * @throws DAOException propaga errore di eccezione.
   */
  public GenericModel getModel() throws DAOException
  {
    ProcedimentixUdienzaModel aModel = new  ProcedimentixUdienzaModel();

    aModel.setIdFasSIUS(getBigDecimal("ID_FASCICOLO_SIUS") );
    aModel.setChiaveAnnoFasSIUS(getBigDecimal("CHIAVE_ANNO") );
    aModel.setChiaveProgrFasSIUS(getBigDecimal("CHIAVE_PROGR") );
    aModel.setCodStatoFasSIUS(getString("COD_STATO_FASCICOLO"));
    aModel.setDescrStatoFasSIUS(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getStatoFascicolo(), aModel.getCodStatoFasSIUS()));
    aModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
    aModel.setCognomeSog(getString("COGNOME") );
    aModel.setNomeSog(getString("NOME"));
    aModel.setDataNascitaSog(getDate("DATA_NASCITA") );
    aModel.setDescrComuneNascitaSog(getString("LUOGO_NASCITA") );
    aModel.setDescrComuneNascitaEsteroSog(getString("LUOGO_NASCITA_ESTERO") );
    aModel.setDescrOggettoProcedimento(getString("OGGETTO_PROCEDIMENTO") );
    aModel.setSezioneProcedimento(getString("SEZIONE") );

    return aModel;
  }


  /**
   * Metodo che recupera i dati dal resulset e ne popola il model
   * completo dei dati afferenti all'evento.
   * <p>
   * @return istanza model ProcedimentixUdienzaModel
   * @throws DAOException propaga errore di eccezione.
   */
  public GenericModel getModelEvento()
  throws DAOException
  {
    ProcedimentixUdienzaModel aModel = new  ProcedimentixUdienzaModel();

    aModel.setIdFasSIUS(getBigDecimal("ID_FASCICOLO_SIUS") );
    aModel.setChiaveAnnoFasSIUS(getBigDecimal("CHIAVE_ANNO") );
    aModel.setChiaveProgrFasSIUS(getBigDecimal("CHIAVE_PROGR") );
    aModel.setCodStatoFasSIUS(getString ("COD_STATO_FASCICOLO"));
    aModel.setDescrStatoFasSIUS(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getStatoFascicolo(), aModel.getCodStatoFasSIUS()));
    aModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
    aModel.setCognomeSog(getString("COGNOME") );
    aModel.setNomeSog(getString("NOME"));
    aModel.setIdUdienza(getBigDecimal("ID_UDIENZA"));
    aModel.setNumCollegio(getInteger("NUM_COLLEGIO") );
    aModel.setDescrOggettoProcedimento(getString("OGGETTO_PROCEDIMENTO") );
    aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO") );
    aModel.setSezioneProcedimento(getString("SEZIONE") );
    aModel.setFlagRinviata(getString("FLAG_RINVIATA") );
    aModel.setMotivoProvvedimento(getString("DESC_MOTIVO_PROVVEDIMENTO") );
    aModel.setEsitoProvvedimento(getString("DESC_ESITO_PROVVEDIMENTO") );
    aModel.setDescStatoUdienza(getString("DESC_STATO_UDIENZA") );
    aModel.setDescPosizioneGiuridicaSius(getString("DESC_POSIZIONE_GIURIDICASIUS") );
    aModel.setDescPosizioneGiuridica(getString("DESC_POSIZIONE_GIURIDICA") );
    aModel.setIdEvento(getBigDecimal("ID_EVENTO") );
    aModel.setCodEsito(getString("COD_ESITO") );
    aModel.setCognomeMagistrato(getString("COGNOMEMAG") );
    aModel.setNomeMagistrato(getString("NOMEMAG") );
    aModel.setCodMagistrato(getString("COD_MAGISTRATO") );
    aModel.setIdEsperto(getBigDecimal("ID_ESPERTO"));

    if ( aModel.getCodMagistrato() == null || aModel.getCodMagistrato().trim().length() < 1)
    {
      aModel.setNomeMagistrato(getString("NOMEESP") );
      aModel.setCognomeMagistrato(getString("COGNOMEESP") );
    }

    return aModel;
  }

/*
  public String  setCondizione(ProcedimentixUdienzaModel aModel)
  {
   String lCondizioni = new String();

   boolean lInserito = false;
   return lCondizioni;
  }
*/

  /**
   * Metodo che compone, imposta e ritorna la condizione di filtro per l'id dell'udienza.
   * <p>
   * @param aKey id dell'udienza.
   * @return ritorna la condizione di filtro.
   */
  public String setCondizioniByKey(BigDecimal aKey)
  {
   return " WHERE UDIENZA.ID_UDIENZA = " + aKey;
  }

  /**
   * Metodo che compone, imposta e ritorna la condizione di filtro per la data dell'udienza.
   * <p>
   * @param aDate Data dell'udienza.
   * @return ritorna la condizione di filtro.
   */
  public String setCondizioniByDate(Date aDate)
  {
   return " WHERE UDIENZA.DATA_UDIENZA = TO_DATE("+ DateUtils.getDateToString(aDate,"yyyyMMdd") +",'YYYYMMDD')";
  }


  /**
   * La funzione ritorna il Numero di Procedimenti assegnati ad una udienza.
   * <p>
   * @param aIdUdienza id dell'udienza.
   * @return BigDecimal Numero Procedimenti.
   * @throws DAOException propga errore di eccezione.
   */
  public BigDecimal getNumProcedimentiXUdienza(BigDecimal aIdUdienza)
  throws DAOException
  {
    BigDecimal lCount = new BigDecimal(0);

    // Preparazione dello statement
    // 12/04/2007 Aggiunto controllo sui procedimenti unificati.
    /*String lStatement ="SELECT COUNT(*) NUM FROM UDIENZA_PROCEDIMENTO";
    lStatement += " WHERE UDI_ID_UDIENZA = " + aIdUdienza;
    lStatement += " AND  udienza_procedimento.flag_rinviata not in ('M','A')"; */
    String lStatement ="SELECT COUNT(*) NUM FROM UDIENZA_PROCEDIMENTO, fascicolo_sius, generale_procedimento";
    lStatement += " WHERE UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA = " + aIdUdienza;
    lStatement += " and fascicolo_sius.id_fascicolo_sius = generale_procedimento.fas_siu_id_fascicolo_sius ";
    lStatement += " and generale_procedimento.id_generale_procedimento = udienza_procedimento.gen_prid_generale_procedimento ";
    lStatement += " and fascicolo_sius.cod_stato_fascicolo <> '05' ";
    lStatement += " AND  udienza_procedimento.flag_rinviata not in ('M','A')";
    setStatement(lStatement);

    // Attivazione della query e lettura del risultato
    start();
    next();
    lCount = getBigDecimal("NUM");
    stop();

    return lCount;
  }

  /**
   * La funzione ritorna il Numero di Procedimenti assegnati ad una udienza da "rinvio".
   * <p>
   * @param aIdUdienza id dell'udienza.
   * @return BigDecimal Numero Procedimenti.
   * @throws DAOException propaga errore di eccezione.
   */
  public BigDecimal getNumProcDaRinvioXUdienza(BigDecimal aIdUdienza)
  throws DAOException
  {
    BigDecimal lCount = new BigDecimal(0);

    // Preparazione dello statement
    // 12/04/2007 Aggiunto controllo sui procedimenti unificati.
    /*String lStatement ="SELECT COUNT(*) NUM FROM UDIENZA_PROCEDIMENTO";
    lStatement += " WHERE UDI_ID_UDIENZA = " + aIdUdienza;
    lStatement += " AND  udienza_procedimento.flag_rinviata = 'S' "; */
    String lStatement ="SELECT COUNT(*) NUM FROM UDIENZA_PROCEDIMENTO, fascicolo_sius, generale_procedimento";
    lStatement += " WHERE UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA = " + aIdUdienza;
    lStatement += " and fascicolo_sius.id_fascicolo_sius = generale_procedimento.fas_siu_id_fascicolo_sius ";
    lStatement += " and generale_procedimento.id_generale_procedimento = udienza_procedimento.gen_prid_generale_procedimento ";
    lStatement += " and fascicolo_sius.cod_stato_fascicolo <> '05' ";
    lStatement += " AND  udienza_procedimento.flag_rinviata = 'S' ";
    setStatement(lStatement);

    // Attivazione della query e lettura del risultato
    start();
    next();
    lCount = getBigDecimal("NUM");
    stop();

    return lCount;
  }
}