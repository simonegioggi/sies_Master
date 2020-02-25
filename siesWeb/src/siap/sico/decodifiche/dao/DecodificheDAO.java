package siap.sico.decodifiche.dao;

import java.sql.Connection;

import siap.dao.SIAPTableDAO;
import siap.sico.decodifiche.model.DecodificheModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;


public class DecodificheDAO extends SIAPTableDAO
{
  public DecodificheDAO (Connection con)
  {
    super(con);

    setTable("CG_REF_CODES");

    setField("RV_LOW_VALUE", STRING);    // Codice
    setField("RV_MEANING", STRING);      // Descrizione
    setField("RV_DOMAIN", STRING);       // Contesto
    setField("RV_ABBREVIATION", STRING); // Filtro
    setField("RV_HIGH_VALUE", STRING);   // Filtro
    setField("RV_ALT2_VALUE", STRING);   // Filtro 2
    setField("RV_ALT3_VALUE", STRING);   // Filtro 3
    setField("RV_ALT4_VALUE", STRING);   // Filtro 4
    setField("RV_ALT5_VALUE", STRING);   // Filtro 5

  }

  //
  // METODI GET()
  //
  public String getCodice()            throws DAOException { return getString("RV_LOW_VALUE"); }
  public String getDescrizione()       throws DAOException { return getString("RV_MEANING"); }
  public String getContesto() 	       throws DAOException { return getString("RV_DOMAIN"); }
  public String getFiltro() 	         throws DAOException { return getString("RV_ABBREVIATION"); }
  public String getCodiceAlternativo() throws DAOException { return getString("RV_HIGH_VALUE"); }
  public String getCodiceAlt2()				 throws DAOException { return getString("RV_ALT2_VALUE"); }
  public String getCodiceAlt3()				 throws DAOException { return getString("RV_ALT3_VALUE"); }
  public String getCodiceAlt4()				 throws DAOException { return getString("RV_ALT4_VALUE"); }
  public String getCodiceAlt5()				 throws DAOException { return getString("RV_ALT5_VALUE"); }

  //
  // METODI SET()
  //
  public void setCodice(String aValore) 	         { setString("RV_LOW_VALUE", aValore); }
  public void setDescrizione(String aValore)       { setString("RV_MEANING", aValore); }
  public void setContesto(String aValore) 	       { setString("RV_DOMAIN", aValore); }
  public void setFiltro(String aValore) 	         { setString("RV_ABBREVIATION", aValore); }
  public void setCodiceAlternativo(String aValore) { setString("RV_HIGH_VALUE", aValore); }
  public void setCodiceAlt2(String aValore) 			 { setString("RV_ALT2_VALUE", aValore); }
  public void setCodiceAlt3(String aValore) 			 { setString("RV_ALT3_VALUE", aValore); }
  public void setCodiceAlt4(String aValore) 			 { setString("RV_ALT4_VALUE", aValore); }
  public void setCodiceAlt5(String aValore) 			 { setString("RV_ALT5_VALUE", aValore); }

  //
  // GET MODEL
  //
  public GenericModel getModel() throws DAOException
  {
    return new DecodificheModel( getCodice() ,
                                 getDescrizione() ,
                                 getContesto(),
                                 getFiltro(),
                                 getCodiceAlternativo(),
                                 getCodiceAlt2(),
                                 getCodiceAlt3(),
                                 getCodiceAlt4(),
                                 getCodiceAlt5()  );
  }

  public void setCondizioni( DecodificheModel aModel )
  {
    String lCondizione = new String();

    boolean inserito = false;

    if (!(aModel.getContesto().equals("")) )
    {
      lCondizione = " RV_DOMAIN = '" + aModel.getContesto() + "'";
      inserito = true;
    }

    if (!(aModel.getCode().equals("")) )
    {
      if (inserito)
      {
        lCondizione +=" AND RV_LOW_VALUE = '"+ aModel.getCode() + "'";
        inserito=true;
      }
      else
        lCondizione =" RV_LOW_VALUE = '"+ aModel.getCode() + "'";
    }

    if (!(aModel.getCodiceAlternativo().equals("")) )
    {
      if (inserito)
      {
        lCondizione +=" AND RV_HIGH_VALUE = '"+ aModel.getCodiceAlternativo() + "'";
        inserito=true;
      }
      else
        lCondizione =" RV_HIGH_VALUE = '"+ aModel.getCodiceAlternativo() + "'";
    }

    if (!(aModel.getFiltro().equals("")) )
    {
      if (inserito)
      {
        lCondizione +=" AND RV_ABBREVIATION = '"+ aModel.getFiltro() + "'";
        inserito=true;
      }
      else
        lCondizione =" RV_ABBREVIATION = '"+ aModel.getFiltro() + "'";
    }

    if (!(aModel.getDescription().equals("")) )
    {
      if (inserito)
      {
        lCondizione += " AND  RV_MEANING = '"+ aModel.getDescription() + "'";
        inserito=true;
      }
      else
        lCondizione = " RV_MEANING = '"+ aModel.getDescription() + "'";
    }
    
    if (!(aModel.getCodiceAlt2().equals("")) )
    {
      if (inserito)
      {
        lCondizione += " AND  RV_ALT2_VALUE = '"+ aModel.getCodiceAlt2() + "'";
        inserito=true;
      }
      else
        lCondizione = " RV_ALT2_VALUE = '"+ aModel.getCodiceAlt2() + "'";
    }
    
    if (!(aModel.getCodiceAlt5().equals("")) )
    {
      if (inserito)
      {
        lCondizione += " AND  RV_ALT5_VALUE = '"+ aModel.getCodiceAlt5() + "'";
        inserito=true;
      }
      else
        lCondizione = " RV_ALT5_VALUE = '"+ aModel.getCodiceAlt5() + "'";
    }
    
    //MEV26 - Cumulo INIZIO
    if (!(aModel.getCodiceAlt4().equals("")) )
    {
      if (inserito)
      {
        lCondizione += " AND  RV_ALT4_VALUE = '"+ aModel.getCodiceAlt4() + "'";
        inserito=true;
      }
      else
        lCondizione = " RV_ALT4_VALUE = '"+ aModel.getCodiceAlt4() + "'";
    }
    //MEV26 - Cumulo FINE
    
    

    setCondition( lCondizione );

    setOrder("RV_MEANING");
  }

  public void setCondizioneContestoFiltroNull(String aDomain)
  {
    setCondition(" RV_DOMAIN = '"+aDomain+"' AND RV_ABBREVIATION IS NULL" );
  }

  public void setCondizioneContestoFiltroNullOrRvAbbreviation(String aDomain, String aRvAbbreviation)
  {
    setCondition(" RV_DOMAIN = '"+aDomain+"' AND RV_ABBREVIATION IS NULL OR RV_ABBREVIATION = '"+aRvAbbreviation+"'" );
  }

  public void setCondizioneContestoRwLowValue(DecodificheModel aModel)
  {
    setCondition(" RV_DOMAIN = '"+aModel.getContesto()+"'");
    setOrdinamentoPerCodice();
  }


  public void setOrdinamentoPerCodice()
  {
    setOrder("RV_LOW_VALUE");
  }

  public void setOrdinamentoPerCodiceAlternativo()
  {
    setOrder("RV_HIGH_VALUE");
  }

  public void setOrdinamentoPerDescrizione()
  {
    setOrder("RV_MEANING");
  }

  public void  setCondizioneByHighValue(String aCode)
  {
    setCondition(" RV_HIGH_VALUE = '"+aCode+"' " );
  }

  public void  setCondizioneByLowValue(String aCode)
  {
    setCondition(" RV_LOW_VALUE = '"+aCode+"' " );
  }

  // 08/04/2011
  public void setCondizioneContestoRwLowValue(String aContesto, String aLowValue)
  {
    setCondition(" RV_DOMAIN = '"+aContesto+"' AND RV_LOW_VALUE = '"+aLowValue+"' " );
  }
  
  /**
   * La funzione setta le condizioni per la ricerca della decodifica  
   * degli Oggetti estratti dalle funzioni statistiche SIUS in una delle 
   * tabelle di estrazione.
   * Il nome della tabella di estrazione viene passato come parametro. 
   * .
   */
  public void  setRicercaOggettiStatisticaSius(String aNomeTabellaEstrazione)throws  DAOException
  {
	String lCondizioni ;
	
    lCondizioni = "RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND RV_LOW_VALUE in " +
    " (select distinct COD_OGGETTO_TENORE from " +
    aNomeTabellaEstrazione + ") ";
   
    setCondition(lCondizioni);
    setOrder("RV_MEANING");
  }
  
  /**
   * La funzione setta le condizioni per la ricerca della decodifica  
   * degli Oggetti estratti dalle funzioni statistiche SIUS in una delle 
   * tabelle di estrazione.
   * Il nome della tabella di estrazione viene passato come parametro. 
   * Il codice dell'ufficio viene passato come parametro
   */
  
  public void  setRicercaOggettiStatisticaSius(String aNomeTabellaEstrazione, String aCodUfficio)throws  DAOException
  {
	String lCondizioni ;
	
    lCondizioni = "RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND RV_LOW_VALUE in " +
    " (select distinct COD_OGGETTO_TENORE from " +
    aNomeTabellaEstrazione + " where FAS_SIU_CHIAVE_UFFICIO = '" + aCodUfficio + "') ";
   
    setCondition(lCondizioni);
    setOrder("RV_MEANING");
  }
  /**
   * Setta la condizione per la ricerca dei tipi di uffici del Giudice dell'Esecuzione (SIGE).
   * Per il momento i codici sono cablati in questa funzione ma andranno poi codificati in una tabella statica (CG_REF_CODES).
   * @throws DAOException
   */
  
  public void  setRicercaTipoUfficioSIGE()throws  DAOException
  {
	String lCondizioni ;
	
    lCondizioni = "RV_DOMAIN = 'TIPO_UFFICIO' AND RV_LOW_VALUE in"  +
    "('CAP', 'CAPSM', 'CAS', 'CASAP', 'GIP', 'GIPM', 'TRIBSD', 'DIB', 'DIBM','GUPM') " ;
   
    setCondition(lCondizioni);
    setOrder("RV_MEANING");
  }
  
  public void  setRicercaTipoUfficioSIGEAccorpato()throws  DAOException
  {
	String lCondizioni ;
	
    lCondizioni = "RV_DOMAIN = 'TIPO_UFFICIO' AND RV_LOW_VALUE in ( "  +
    " select distinct T.COD_TIPO_UFFICIO" +
    " from UFFICIO T" +
    " where T.COD_TIPO_UFFICIO in ('CAP', 'CAPSM', 'CAS', 'CASAP', 'GIP', 'GIPM', 'TRIBSD', 'DIB', 'DIBM', 'GUPM')" +
    " and NVL(T.FLAG_ACCORP, 'N') != 'S'" +
    " ) " ;
   
    setCondition(lCondizioni);
    setOrder("RV_MEANING");
  }
  
  public void  setRicercaTipoAutorita ()throws  DAOException {
	  String lCondizioni = " RV_DOMAIN = 'TIPO_AUTORITA' AND " +
	                     " (RV_HIGH_VALUE is NULL or RV_HIGH_VALUE != 'AUTORITA_MINORENNI') ";
	
      setCondition(lCondizioni);
      setOrder("RV_MEANING");
  }
}
