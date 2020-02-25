package siap.sige.magistratosezione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.magistratosezione.model.MagistratoSezioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: MagistratoSezioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella MagistratoSezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
*/

public class MagistratoSezioneDAO extends SIAPTableDAO
{
  public MagistratoSezioneDAO (Connection con)
  {
    super(con);
    setTable("MAGISTRATO_SEZIONE");

    setField("MAG_COD_MAGISTRATO", STRING);
    setField("SEZ_ID_SEZIONE", BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("DATA_INIZIO_ASS", DATE);
    setField("DATA_FINE_ASS", DATE);
    setField("FLG_VALIDO_SN", STRING);
  }

  //
  // METODI GET()
  //
  public String      getMagCodMagistrato() 	          throws DAOException	 { return getString("MAG_COD_MAGISTRATO"); }
  public BigDecimal  getSezIdSezione()                throws DAOException  { return getBigDecimal("SEZ_ID_SEZIONE"); }
  public String      getCodOperatoreInserimento() 	  throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	     getDataInserimento() 		        throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String      getCodUfficioInserimento() 	    throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String      getCodOperatoreAggiornamento()   throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	     getDataAggiornamento() 	        throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String      getCodUfficioAggiornamento() 	  throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public Date 	     getDataInizioAssegnazione() 	    throws DAOException	 { return getDate("DATA_INIZIO_ASS"); }
  public Date 	     getDataFineAssegnazione() 	        throws DAOException	 { return getDate("DATA_FINE_ASS"); }
  public String      getFlgValidoSN() 	                throws DAOException	 { return getString("FLG_VALIDO_SN"); }
  
  //
  // METODI SET()
  //
  public void  	 setMagCodMagistrato(String aValore ) 	        { setString("MAG_COD_MAGISTRATO", aValore); }
  public void    setSezIdSezione(BigDecimal aValore )           { setBigDecimal("SEZ_ID_SEZIONE", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore )    { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 	            { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore )      { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore )  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 		    { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 	{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }  
  public void  	 setDataInizioAssegnazione(Date aValore ) 		{ setDate("DATA_INIZIO_ASS", aValore); }
  public void  	 setDataFineAssegnazione(Date aValore ) 		{ setDate("DATA_FINE_ASS", aValore); }
  public void    setFlgValidoSN(String aValore) 	            { setString("FLG_VALIDO_SN", aValore); }  
 
  public GenericModel getModel() throws DAOException
  {
    MagistratoSezioneModel lModel = new MagistratoSezioneModel();
    lModel.setMagCodMagistrato(getMagCodMagistrato());
    lModel.setSezIdSezione( getSezIdSezione() );
    lModel.setCodOperatoreInserimento(getCodOperatoreInserimento());
    lModel.setDataInserimento(getDataInserimento());
    lModel.setCodUfficioInserimento(getCodUfficioInserimento());
    lModel.setCodOperatoreAggiornamento(getCodOperatoreAggiornamento());
    lModel.setDataAggiornamento(getDataAggiornamento());
    lModel.setCodUfficioAggiornamento(getCodUfficioAggiornamento());   
    lModel.setDataInizioAssegnazione(getDataInizioAssegnazione());
    lModel.setDataFineAssegnazione(getDataFineAssegnazione());
    lModel.setFlagValidoSN(getFlgValidoSN());
    
    
    return lModel;  
  }
  
  public void setDAOFromModel(MagistratoSezioneModel aModel) throws DAOException
  {
    setMagCodMagistrato( aModel.getMagCodMagistrato() );
    setSezIdSezione(aModel.getSezIdSezione());
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );    
    setDataInizioAssegnazione(aModel.getDataInizioAssegnazione());
    setDataFineAssegnazione(aModel.getDataFineAssegnazione());
    //FlagValidoSN se impostato a 'S' indica ULTIMA SEZIONE INSERITA ALTRIMENTI 'N'
    setFlgValidoSN("S");
  }

  public void setDAOFromModelForUpdate(MagistratoSezioneModel aModel) throws DAOException
  {
    //setSezIdSezione(aModel.getSezIdSezione());
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() ); 
    setCondizioneUpdate(aModel.getMagCodMagistrato());
    //in update la DataInizioAssegnazione non viene aggiornata 
    //perchè una volta inserita non può essere modificata
    //setDataInizioAssegnazione(aModel.getDataInizioAssegnazione());//    
    setDataFineAssegnazione(aModel.getDataFineAssegnazione());
  }

  public void setDAOFromModelForUpdateFlgValidoSN(MagistratoSezioneModel aModel) throws DAOException
  {
	//update FlgValidoSN='S' a 'N'
    //setSezIdSezione(aModel.getSezIdSezione());
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );     
    setCondizioneUpdateMultiSezioneFlgValidoSN(aModel.getMagCodMagistrato(),"S");
    setFlgValidoSN("N");
    //in update la DataInizioAssegnazione non viene aggiornata 
    //perchè una volta inserita non può essere modificata
    //setDataInizioAssegnazione(aModel.getDataInizioAssegnazione());
    setDataFineAssegnazione(DateUtils.getSysDate());
  }
  
  public void setCondizione(MagistratoSezioneModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(String key)
  {
    setCondition(" MAG_COD_MAGISTRATO = '" + key + "'");
  }

  public void setCondizioneUpdateMultiSezioneFlgValidoSN(String key, String flgValido)
  {
    setCondition(" MAG_COD_MAGISTRATO = '" + key + "'" + " AND " +" (FLG_VALIDO_SN = '" + flgValido + "' OR FLG_VALIDO_SN IS NULL) ");
  }
  
  public void setCondizioneByCodMag(String key)
  {
    setCondition(" MAG_COD_MAGISTRATO = '" + key + "'");
  }

  //[EC] passo anche id SEZ_ID_SEZIONE per individuare la sezione da modificare
  public void setCondizioneForUpdateBySezioni(
			MagistratoSezioneModel[] magistratoSezioneModels) {
	
	  if (mStrConditions != null && !mStrConditions.equals(""))
		  mStrConditions += " AND ";

	  String lStatement = "  SEZ_ID_SEZIONE IN (";
	  String sezIdSezione = "";
	  BigDecimal mSezIdSezione  = null;
	  for (int i = 0; i < magistratoSezioneModels.length; i++) {
			 mSezIdSezione  =  (BigDecimal) magistratoSezioneModels[i].getSezIdSezione();
			
			 if(i != 0 && i < magistratoSezioneModels.length){
				 sezIdSezione +=  ",";
			 } 				
			 sezIdSezione = sezIdSezione + mSezIdSezione.toString();
	  }
	  lStatement += sezIdSezione + ")";
	  mStrConditions += lStatement;
	
  }

  /*
  public void setCondizioneUpdate(String key,String aCodUff)
  {
    setCondition(" COD_MAGISTRATO = '" + key +  "' AND COD_UFFICIO_APPARTENENZA = '" + aCodUff + "'");
  }
  */
}