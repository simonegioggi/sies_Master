package siap.siep.statis.dao;

/**
* <p>Title: StatoFascicoloResDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella StatoFascicoloRes</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import siap.siep.statis.model.StatoFascicoloResModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

public class StatoFascicoloResDAO extends TableDAO 
{
  public StatoFascicoloResDAO (Connection con) 
  {
    super(con);
    
    setTable("STATO_FASCICOLO_RES");
    
    //Settare la Sequence e i campi chiave
    
    setField("COD_STATO_FASCICOLO", INT);
    setField("DESCRIZIONE", STRING);

    setField("ORDINAMENTO", INT);
    setField("TIPO_CAMPO", STRING);
    setField("T1", INT);
    setField("T2", INT);
  }


  //
  // METODI GET()
  //
  public Integer getCodStatoFascicolo() throws DAOException  { return getInteger ("COD_STATO_FASCICOLO"); } 
  public String  getDescrizione()       throws DAOException  { return getString  ("DESCRIZIONE"); } 

  public Integer getOrdinamento()       throws DAOException  { return getInteger ("ORDINAMENTO"); } 
  public String  getTipoCampo()         throws DAOException  { return getString  ("TIPO_CAMPO"); } 
  public Integer getT1()                throws DAOException  { return getInteger ("T1"); } 
  public Integer getT2()                throws DAOException  { return getInteger ("T2"); } 

  //
  // METODI SET()
  //
  public void setCodStatoFascicolo (Integer aValore )   { setInteger("COD_STATO_FASCICOLO", aValore); } 
  public void setDescrizione       (String  aValore )   { setString("DESCRIZIONE", aValore); } 

  public void setOrdinamento       (Integer aValore )   { setInteger("ORDINAMENTO", aValore); } 
  public void setTipoCampo         (String  aValore )   { setString("TIPO_CAMPO", aValore); } 
  public void setT1                (Integer aValore )   { setInteger("T1", aValore); } 
  public void setT2                (Integer aValore )   { setInteger("T2", aValore); } 

  public GenericModel getModel() throws DAOException
  { 
    return new StatoFascicoloResModel( getCodStatoFascicolo() , 
                                       getDescrizione(),
                                       getOrdinamento(),
                                       getTipoCampo(),
                                       getT1(),
                                       getT2()
    );
  }


  public void setDAOFromModel (StatoFascicoloResModel aModel) throws DAOException
  {
     setCodStatoFascicolo ( aModel.getCodStatoFascicolo() );  
     setDescrizione       ( aModel.getDescrizione() );  

     setOrdinamento       ( aModel.getOrdinamento());  
     setTipoCampo         ( aModel.getTipoCampo() );  
     setT1                ( aModel.getT1());  
     setT2                ( aModel.getT2());  
  }


  public void selCondizione(StatoFascicoloResModel aModel)
  {
    String lCondizioni = new String(); 
    
    boolean lInserito = false; 
    if ( lInserito ) setCondition(lCondizioni); 
  }

  public void setOrdinamento(String order)
  {
    super.setOrder(order);
  }
  
  public void selCondizioneUpdate(String key)
  {}
}
