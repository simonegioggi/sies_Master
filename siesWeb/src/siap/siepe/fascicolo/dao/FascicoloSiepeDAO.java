package siap.siepe.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: FascicoloSiepeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella FascicoloSiepe</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class FascicoloSiepeDAO extends TableDAO
{
	public FascicoloSiepeDAO (Connection con)
	{
        super(con);
        setTable("FASCICOLO_SIEPE");

        //Settare la Sequence e i campi chiave
        setSequenceField("ID_FASCICOLO_SIEPE", "FAS_SIEPE_SEQ");

        setField("ID_FASCICOLO_SIEPE", BIG_DECIMAL);
        setField("CHIAVE_ANNO", BIG_DECIMAL);
        setField("CHIAVE_PROGR", BIG_DECIMAL);
        setField("CHIAVE_UFFICIO", STRING);
        setField("NUM_UEPE", BIG_DECIMAL);
        setField("ANNO_UEPE", BIG_DECIMAL);
        setField("PROGR_UEPE", BIG_DECIMAL);
        setField("COD_STATO_FASCICOLO", STRING);
        setField("COD_OPERATORE_INSERIMENTO", STRING);
        setField("DATA_ISCRIZIONE", DATE);
        setField("DATA_INSERIMENTO", DATE);
        setField("COD_UFFICIO_INSERIMENTO", STRING);
        setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
        setField("DATA_AGGIORNAMENTO", DATE);
        setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
        setField("SOG_ID_SOGGETTO", BIG_DECIMAL);
        setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
        setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
        setField("COD_INCARICO", STRING);
        setField("NOTE", STRING);
        setField("COD_UFFICIO_MITTENTE", STRING);
        setField("EVE_ID_EVENTO", BIG_DECIMAL);
        setField("TIPO_DEFINIZIONE", STRING);
        setField("DATA_DEFINIZIONE", DATE);
        setField("DESCR_DEFINIZIONE", STRING);
	  }

  //
  // METODI GET()
  //
  public BigDecimal   getIdFascicoloSiepe() 		      throws DAOException	 { return getBigDecimal("ID_FASCICOLO_SIEPE"); }
  public BigDecimal   getChiaveAnno() 		            throws DAOException	 { return getBigDecimal("CHIAVE_ANNO"); }
  public BigDecimal   getChiaveProgr() 		            throws DAOException	 { return getBigDecimal("CHIAVE_PROGR"); }
  public String 		  getChiaveUfficio() 		          throws DAOException	 { return getString("CHIAVE_UFFICIO"); }
  public BigDecimal 	getNumUepe() 		                throws DAOException	 { return getBigDecimal("NUM_UEPE"); }
  public BigDecimal 	getAnnoUepe() 		              throws DAOException	 { return getBigDecimal("ANNO_UEPE"); }
  public BigDecimal   getProgrUepe() 		              throws DAOException	 { return getBigDecimal("PROGR_UEPE"); }
  public String 		  getCodStatoFascicolo() 		      throws DAOException	 { return getString("COD_STATO_FASCICOLO"); }
  public String 		  getCodOperatoreInserimento() 	  throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			  getDataIscrizione() 		        throws DAOException	 { return getDate("DATA_ISCRIZIONE"); }
  public Date 			  getDataInserimento() 		        throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 		  getCodUfficioInserimento() 	    throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		  getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 			  getDataAggiornamento() 		      throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 		  getCodUfficioAggiornamento() 	  throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal   getSogIdSoggetto() 		          throws DAOException	 { return getBigDecimal("SOG_ID_SOGGETTO"); }
  public BigDecimal   getFasSieIdFascicoloSiep() 	    throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public BigDecimal   getFasSiuIdFascicoloSius() 	    throws DAOException	 { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }
  public String 		  getCodIncarico() 		            throws DAOException	 { return getString("COD_INCARICO"); }
  public String 		  getNote() 		                  throws DAOException	 { return getString("NOTE"); }
  public String 		  getCodUfficioMittente() 		    throws DAOException	 { return getString("COD_UFFICIO_MITTENTE"); }
  public BigDecimal 	getEveIdEvento() 		            throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
  public String       getTipoDefinizione()            throws DAOException  { return getString("TIPO_DEFINIZIONE"); }
  public Date         getDataDefinizione()            throws DAOException  { return getDate("DATA_DEFINIZIONE"); }
  public String       getDescrDefinizione()           throws DAOException  { return getString("DESCR_DEFINIZIONE"); }

  //
  // METODI SET()
  //
  public void  	 setIdFascicoloSiepe(BigDecimal aValore ) 			 { setBigDecimal("ID_FASCICOLO_SIEPE", aValore); }
  public void  	 setChiaveAnno(BigDecimal aValore ) 			       { setBigDecimal("CHIAVE_ANNO", aValore); }
  public void  	 setChiaveProgr(BigDecimal aValore ) 			       { setBigDecimal("CHIAVE_PROGR", aValore); }
  public void  	 setChiaveUfficio(String aValore ) 			         { setString("CHIAVE_UFFICIO", aValore); }
  public void  	 setNumUepe(BigDecimal aValore ) 			           { setBigDecimal("NUM_UEPE", aValore); }
  public void  	 setAnnoUepe(BigDecimal aValore ) 			         { setBigDecimal("ANNO_UEPE", aValore); }
  public void  	 setProgrUepe(BigDecimal aValore ) 			         { setBigDecimal("PROGR_UEPE", aValore); }
  public void  	 setCodStatoFascicolo(String aValore ) 			     { setString("COD_STATO_FASCICOLO", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 		 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataIscrizione(Date aValore ) 			         { setDate("DATA_ISCRIZIONE", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			         { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 		   { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore )   { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			       { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 		 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setSogIdSoggetto(BigDecimal aValore ) 			     { setBigDecimal("SOG_ID_SOGGETTO", aValore); }
  public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 	 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void  	 setFasSiuIdFascicoloSius(BigDecimal aValore ) 	 { setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }
  public void  	 setCodIncarico(String aValore ) 			           { setString("COD_INCARICO", aValore); }
  public void  	 setNote(String aValore ) 			                 { setString("NOTE", aValore); }
  public void  	 setCodUfficioMittente(String aValore )          { setString("COD_UFFICIO_MITTENTE", aValore); }
  public void  	 setEveIdEvento(BigDecimal aValore ) 		         { setBigDecimal("EVE_ID_EVENTO", aValore); }
  public void    setTipoDefinizione(String aValore)              { setString("TIPO_DEFINIZIONE", aValore); }
  public void    setDataDefinizione(Date aValore)                { setDate("DATA_DEFINIZIONE",aValore); }
  public void    setDescrDefinizione(String aValore)             { setString("DESCR_DEFINIZIONE",aValore); }


  public GenericModel getModel() throws DAOException
  {
    return new FascicoloSiepeModel(
        getIdFascicoloSiepe() ,
        getChiaveAnno() ,
        getChiaveProgr() ,
        getChiaveUfficio() ,
        getNumUepe() ,
        getAnnoUepe() ,
        getProgrUepe() ,
        getCodStatoFascicolo() ,
        "",
        getCodOperatoreInserimento() ,
        getDataIscrizione() ,
        getDataInserimento() ,
        getCodUfficioInserimento() ,
        "",
        getCodOperatoreAggiornamento() ,
        getDataAggiornamento() ,
        getCodUfficioAggiornamento() ,
        "",
        getSogIdSoggetto() ,
        getFasSieIdFascicoloSiep() ,
        getFasSiuIdFascicoloSius() ,
        getCodIncarico() ,
        "",
        getNote(),
        getCodUfficioMittente() ,
        "",
        getEveIdEvento(),
        getTipoDefinizione(),
        "",
        getDataDefinizione(),
        getDescrDefinizione());
    }


    public void setDAOFromModel(FascicoloSiepeModel aModel) throws DAOException
    {
      setIdFascicoloSiepe( aModel.getIdFascicoloSiepe() );
      setChiaveAnno( aModel.getChiaveAnno() );
      setChiaveProgr( aModel.getChiaveProgr() );
      setChiaveUfficio( aModel.getChiaveUfficio() );
      setNumUepe( aModel.getNumUepe() );
      setAnnoUepe( aModel.getAnnoUepe() );
      setProgrUepe( aModel.getProgrUepe() );
      setCodStatoFascicolo( aModel.getCodStatoFascicolo() );
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
      setDataIscrizione( aModel.getDataIscrizione() );
      setDataInserimento( aModel.getDataInserimento() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setSogIdSoggetto( aModel.getSogIdSoggetto() );
      setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
      setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
      setCodIncarico( aModel.getCodIncarico() );
      setNote( aModel.getNote() );
      setCodUfficioMittente( aModel.getCodUfficioMittente() );
      setEveIdEvento( aModel.getEveIdEvento() );
      setTipoDefinizione(aModel.getTipoDefinizione());  
      setDataDefinizione(aModel.getDataDefinizione());
      setDescrDefinizione(aModel.getDescrDefinizione());
    }

    public void setDAOFromModelForUpdate(FascicoloSiepeModel aModel) throws DAOException
    {
      //setIdFascicoloSiepe( aModel.getIdFascicoloSiepe() );
      //setChiaveAnno( aModel.getChiaveAnno() );
      //setChiaveProgr( aModel.getChiaveProgr() );
      //setChiaveUfficio( aModel.getChiaveUfficio() );
      setNumUepe( aModel.getNumUepe() );
      setAnnoUepe( aModel.getAnnoUepe() );
      setProgrUepe( aModel.getProgrUepe() );
      //setCodStatoFascicolo( aModel.getCodStatoFascicolo() );
      //setDataIscrizione( aModel.getDataIscrizione() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      //setSogIdSoggetto( aModel.getSogIdSoggetto() );
      //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
      //setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
      //setCodIncarico( aModel.getCodIncarico() );
      setNote( aModel.getNote() );
      //setCodUfficioMittente( aModel.getCodUfficioMittente() );
      //setEveIdEvento( aModel.getEveIdEvento() );
      setCondizioneUpdate(aModel.getIdFascicoloSiepe());
    }

    public void setCondizione(FascicoloSiepeModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;
      if ( lInserito ) setCondition(lCondizioni);
    }

    public void setCondizioneUpdate(BigDecimal key)
    {
      setCondition(" ID_FASCICOLO_SIEPE = " + key );
    }
}