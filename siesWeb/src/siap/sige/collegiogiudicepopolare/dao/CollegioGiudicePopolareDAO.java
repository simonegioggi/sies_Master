package siap.sige.collegiogiudicepopolare.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.collegiogiudicepopolare.model.CollegioGiudicePopolareModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: CollegioGiudicePopolareDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CollegioGiudicePopolareModel</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
*/
public class CollegioGiudicePopolareDAO extends SIAPTableDAO
{
  public CollegioGiudicePopolareDAO(Connection con)
  {
    super(con);
    setTable("COLLEGIO_GIUDICE_POPOLARE");
    
    setField("COL_ID_COLLEGIO", BIG_DECIMAL);
    setField("GIU_POP_ID_GIUDICE_POPOLARE", BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
  }

  //
  // METODI GET()
  //
  public BigDecimal getColIdCollegio() 	         throws DAOException  { return getBigDecimal("COL_ID_COLLEGIO"); }
  public BigDecimal getGiuPopIdGiudicePopolare() throws DAOException  { return getBigDecimal("GIU_POP_ID_GIUDICE_POPOLARE"); }
  public String     getCodOperatoreInserimento() throws DAOException	{ return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	    getDataInserimento() 		     throws DAOException	{ return getDate("DATA_INSERIMENTO"); }
  public String     getCodUfficioInserimento() 	 throws DAOException	{ return getString("COD_UFFICIO_INSERIMENTO"); }

  //
  // METODI SET()
  //
  public void setColIdCollegio(BigDecimal aValore) 	         { setBigDecimal("COL_ID_COLLEGIO", aValore); }
  public void setGiuPopIdGiudicePopolare(BigDecimal aValore) { setBigDecimal("GIU_POP_ID_GIUDICE_POPOLARE", aValore); }
  public void setCodOperatoreInserimento(String aValore)     { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore) 	             { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore)       { setString("COD_UFFICIO_INSERIMENTO", aValore); }

  public GenericModel getModel() throws DAOException
  {
    CollegioGiudicePopolareModel lModel = new CollegioGiudicePopolareModel();
    lModel.setColIdCollegio(getColIdCollegio());
    lModel.setGiuPopIdGiudicePopolare( getGiuPopIdGiudicePopolare() );
    lModel.setCodOperatoreInserimento(getCodOperatoreInserimento());
    lModel.setDataInserimento(getDataInserimento());
    lModel.setCodUfficioInserimento(getCodUfficioInserimento());
    return lModel;  
  }
  
  public void setDAOFromModel(CollegioGiudicePopolareModel aModel) throws DAOException
  {
    setColIdCollegio( aModel.getColIdCollegio() );
    setGiuPopIdGiudicePopolare(aModel.getGiuPopIdGiudicePopolare());
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
  }

  /*
  public void setDAOFromModelForUpdate(CollegioGiudicePopolareModel aModel) throws DAOException
  {
    setGiuPopIdGiudicePopolare(aModel.getGiuPopIdGiudicePopolare());
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCondizioneUpdate(aModel.getMagCodMagistrato());
  }
  */
  public void setCondizione(CollegioGiudicePopolareModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(String key)
  {
    setCondition(" COL_ID_COLLEGIO = '" + key + "'");
  }
  
  public void setCondizioneByIdCol(BigDecimal aId)
  {
    setCondition(" COL_ID_COLLEGIO = " + aId.toString() );
  }

  public void setCondizioneIdGiudicePopolare(String key)
  {
    setCondition(" GIU_POP_ID_GIUDICE_POPOLARE = '" + key + "'");
  }
}