package siap.siep.modulocumulo.dao;

/**
* <p>Title: PosizioneGiuridicaCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella PosizioneGiuridicaCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;

public class PosizioneGiuridicaCumuloSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public PosizioneGiuridicaCumuloSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaPosizioneGiuridicaCumulo ( PosizioneGiuridicaCumuloModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lSql+=" WHERE " + lCondizioni;

    lSql += " "+getOrderBy()+" "; 

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaPosizioneGiuridicaCumuloByKey( BigDecimal aIdPosizioneGiuridicaCum) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND " + setCondizioniByKey( aIdPosizioneGiuridicaCum);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  
  public void ricercaPosizioneGiuridicaCumuloByIdIstruttoria ( BigDecimal aIdIstruttoriaCumulo) throws DAOException {
    // Recupera la select...from... where
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND ISTR_ID_ISTRUTTORIA_CUMULO =  "+aIdIstruttoriaCumulo;
    lSql += " AND TIT_ID_TITOLO_CUMULATO is null ";	// Esclusione delle Posizioni Giuridiche afferenti ai Titoli Cumulati

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  public void ricercaPosizioneGiuridicaCumuloByIdIstruttoriaTitIdTitoloCumulato ( BigDecimal aIdIstruttoriaCumulo, BigDecimal aTitIdTitoloCumulato) throws DAOException {
    // Recupera la select...from... where
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiavi 
    lSql += " AND ISTR_ID_ISTRUTTORIA_CUMULO =  "+aIdIstruttoriaCumulo;
    lSql += " AND TIT_ID_TITOLO_CUMULATO =  "+aTitIdTitoloCumulato;

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  public void ricercaPosizioneGiuridicaCumuloByIdTitoloCumulato ( BigDecimal aTitIdTitoloCumulato) throws DAOException {
	    // Recupera la select...from... where
	    String lSql = getSqlQuery();

	    // Aggiunge le where condition per chiavi 
	    lSql += " AND TIT_ID_TITOLO_CUMULATO =  "+aTitIdTitoloCumulato;

	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
	  }
  
  public void ricercaPosizioniGiuridicheTitoliByIdIstruttoria ( BigDecimal aIdIstruttoriaCumulo) throws DAOException {
    // Recupera la select...from... where
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiavi 
    lSql += " AND ISTR_ID_ISTRUTTORIA_CUMULO =  "+aIdIstruttoriaCumulo;
    lSql += " AND TIT_ID_TITOLO_CUMULATO is not null ";

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_POSIZIONE_GIURIDICA_CUM, "+  
                  "COD_POSIZIONE_GIURIDICA, CG_POS_GIU.RV_MEANING DESC_POS_GIU, "+  
                  "DATA_INIZIO, "+  
                  "IST_DET_ID_ISTITUTO_DETENZIONE, "+  
                  "ALTRO_LUOGO, "+  
                  "CHIAVE_ANNO_FAS_SIUS, "+  
                  "CHIAVE_PROGR_FAS_SIUS, "+  
                  "CHIAVE_UFF_FAS_SIUS, "+  
                  "ANNO_REGISTRO, "+  
                  "NUMERO_REGISTRO, "+  
                  "COD_TIPO_PROVVEDIMENTO, CG_TIPO_PROVV.RV_MEANING DESC_TIPO_PROVV, "+  
                  "DATA_EMISSIONE_PROVV, "+ 
                  "NUM_ANNI_MISURA, NUM_MESI_MISURA, NUM_GIORNI_MISURA, "+  
                  "DATA_FINE_MISURA, "+ 
                  "FLAG_DECISIONE_TRIBUNALE, "+
                  
                  "FLAG_DIFF_DET_DOM, "+
                  "DATA_INIZIO_MISURA, "+
                  
                  "DAT_ID_DATI_FINALI_CUMULO, ISTR_ID_ISTRUTTORIA_CUMULO, TIT_ID_TITOLO_CUMULATO, "+  
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement +=  " FROM POSIZIONE_GIURIDICA_CUMULO, CG_REF_CODES CG_POS_GIU, CG_REF_CODES CG_TIPO_PROVV ";
    lStatement += " WHERE 1=1 ";
    lStatement +=   " AND ( nvl(POSIZIONE_GIURIDICA_CUMULO.COD_POSIZIONE_GIURIDICA,'-') = CG_POS_GIU.RV_LOW_VALUE AND CG_POS_GIU.RV_DOMAIN = 'POSIZIONE_GIURIDICA' ) "; 
    lStatement +=   " AND ( nvl(POSIZIONE_GIURIDICA_CUMULO.COD_TIPO_PROVVEDIMENTO,'-') = CG_TIPO_PROVV.RV_LOW_VALUE AND CG_TIPO_PROVV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) "; 

    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     PosizioneGiuridicaCumuloModel aModel = new  PosizioneGiuridicaCumuloModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdPosizioneGiuridicaCum    ( getBigDecimal ("ID_POSIZIONE_GIURIDICA_CUM"    ) ); 
    aModel.setCodPosizioneGiuridica      ( getString     ("COD_POSIZIONE_GIURIDICA"       ) ); 
    aModel.setDescrPosizioneGiuridica 	 ( getString	 ("DESC_POS_GIU"				  ) );
    aModel.setDataInizio                 ( getDate       ("DATA_INIZIO"                   ) ); 
    aModel.setIstDetIdIstitutoDetenzione ( getString     ("IST_DET_ID_ISTITUTO_DETENZIONE") ); 
    aModel.setAltroLuogo                 ( getString     ("ALTRO_LUOGO"                   ) ); 
    aModel.setChiaveAnnoFasSius          ( getBigDecimal ("CHIAVE_ANNO_FAS_SIUS"          ) ); 
    aModel.setChiaveProgrFasSius         ( getBigDecimal ("CHIAVE_PROGR_FAS_SIUS"         ) ); 
    aModel.setChiaveUffFasSius           ( getString     ("CHIAVE_UFF_FAS_SIUS"           ) ); 
    aModel.setAnnoRegistro               ( getBigDecimal ("ANNO_REGISTRO"                 ) ); 
    aModel.setNumeroRegistro             ( getBigDecimal ("NUMERO_REGISTRO"               ) ); 
    aModel.setCodTipoProvvedimento       ( getString     ("COD_TIPO_PROVVEDIMENTO"        ) ); 
    aModel.setDescrTipoProvvedimento	 ( getString	 ("DESC_TIPO_PROVV"				  ) ); 
    aModel.setDataEmissioneProvv         ( getDate       ("DATA_EMISSIONE_PROVV"          ) ); 
    aModel.setNumAnniMisura              ( getBigDecimal ("NUM_ANNI_MISURA"               ) ); 
    aModel.setNumMesiMisura              ( getBigDecimal ("NUM_MESI_MISURA"               ) ); 
    aModel.setNumGiorniMisura            ( getBigDecimal ("NUM_GIORNI_MISURA"             ) ); 
    aModel.setDataFineMisura             ( getDate       ("DATA_FINE_MISURA"              ) );
    
    aModel.setFlagDecisioneTDS			 ( getString     ("FLAG_DECISIONE_TRIBUNALE"      ) ); 
    aModel.setFlagDifferimentoDetDom	 ( getString	 ("FLAG_DIFF_DET_DOM"	  		  ) );
    aModel.setDataInizioMisura           ( getDate       ("DATA_INIZIO_MISURA"            ) );
    
    aModel.setDatIdDatiFinaliCumulo      ( getBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"     ) );
    aModel.setIstrIdIstruttoriaCumulo    ( getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"    ) ); 
    aModel.setTitIdTitoloCumulato		 ( getBigDecimal ("TIT_ID_TITOLO_CUMULATO"	      ) );
    
    aModel.setCodOperatoreInserimento    ( getString     ("COD_OPERATORE_INSERIMENTO"     ) ); 
    aModel.setDataInserimento            ( getDate       ("DATA_INSERIMENTO"              ) ); 
    aModel.setCodUfficioInserimento      ( getString     ("COD_UFFICIO_INSERIMENTO"       ) ); 
    aModel.setCodOperatoreAggiornamento  ( getString     ("COD_OPERATORE_AGGIORNAMENTO"   ) ); 
    aModel.setDataAggiornamento          ( getDate       ("DATA_AGGIORNAMENTO"            ) ); 
    aModel.setCodUfficioAggiornamento    ( getString     ("COD_UFFICIO_AGGIORNAMENTO"     ) ); 
    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(PosizioneGiuridicaCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdPosizioneGiuridicaCum() != null ) { 
      lCondizioni += " and ID_POSIZIONE_GIURIDICA_CUM = " + aModel.getIdPosizioneGiuridicaCum() + ""; 
    } 
    if (aModel.getCodPosizioneGiuridica() != null && aModel.getCodPosizioneGiuridica().length() > 0) { 
      lCondizioni += " and COD_POSIZIONE_GIURIDICA = '" + aModel.getCodPosizioneGiuridica() + "' "; 
    } 
    if (aModel.getDataInizio() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getIstDetIdIstitutoDetenzione() != null && aModel.getIstDetIdIstitutoDetenzione().length() > 0) { 
      lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione() + "' "; 
    } 
    if (aModel.getAltroLuogo() != null && aModel.getAltroLuogo().length() > 0) { 
      lCondizioni += " and ALTRO_LUOGO = '" + aModel.getAltroLuogo() + "' "; 
    } 
    if (aModel.getChiaveAnnoFasSius() != null ) { 
      lCondizioni += " and CHIAVE_ANNO_FAS_SIUS = " + aModel.getChiaveAnnoFasSius() + ""; 
    } 
    if (aModel.getChiaveProgrFasSius() != null ) { 
      lCondizioni += " and CHIAVE_PROGR_FAS_SIUS = " + aModel.getChiaveProgrFasSius() + ""; 
    } 
    if (aModel.getChiaveUffFasSius() != null && aModel.getChiaveUffFasSius().length() > 0) { 
      lCondizioni += " and CHIAVE_UFF_FAS_SIUS = '" + aModel.getChiaveUffFasSius() + "' "; 
    } 
    if (aModel.getAnnoRegistro() != null ) { 
      lCondizioni += " and ANNO_REGISTRO = " + aModel.getAnnoRegistro() + ""; 
    } 
    if (aModel.getNumeroRegistro() != null ) { 
      lCondizioni += " and NUMERO_REGISTRO = " + aModel.getNumeroRegistro() + ""; 
    } 
    if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' "; 
    }     
    if (aModel.getDataEmissioneProvv() != null ) { 
      lCondizioni += " and to_char(DATA_EMISSIONE_PROVV,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEmissioneProvv(),"dd/MM/yyyy") + "' "; 
    }
    if (aModel.getNumAnniMisura() != null ) { 
      lCondizioni += " and NUM_ANNI_MISURA = " + aModel.getNumAnniMisura() + ""; 
    } 
    if (aModel.getNumMesiMisura() != null ) { 
      lCondizioni += " and NUM_MESI_MISURA = " + aModel.getNumMesiMisura() + ""; 
    } 
    if (aModel.getNumGiorniMisura() != null ) { 
      lCondizioni += " and NUM_GIORNI_MISURA = " + aModel.getNumGiorniMisura() + ""; 
    } 
    if (aModel.getDataFineMisura() != null ) { 
      lCondizioni += " and to_char(DATA_FINE_MISURA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFineMisura(),"dd/MM/yyyy") + "' "; 
    }
    if (aModel.getDatIdDatiFinaliCumulo() != null ) { 
      lCondizioni += " and DAT_ID_DATI_FINALI_CUMULO = " + aModel.getDatIdDatiFinaliCumulo() + ""; 
    }
    if (aModel.getIstrIdIstruttoriaCumulo() != null ) { 
      lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + ""; 
    } 
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    }

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( BigDecimal aIdPosizioneGiuridicaCum  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_POSIZIONE_GIURIDICA_CUM = " + aIdPosizioneGiuridicaCum;

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    return lCondizioni;
  }


  /***************************************************************************** 
   * Metodo per la costruzione della sezione order by 
   * @return 
   ****************************************************************************/ 
  protected String getOrderBy() { 
    String orderBy = new String(""); 
    //orderBy = " ORDER BY "; 
    return orderBy; 
  } 
}
