package siap.sige.collegioesperto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.collegioesperto.model.CollegioEspertoModel;
import siap.sige.collegiogiudicepopolare.model.CollegioGiudicePopolareModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: CollegioGiudicePopolareDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CollegioGiudicePopolareModel</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
*/
public class CollegioEspertoDAO extends SIAPTableDAO
{
  public CollegioEspertoDAO(Connection con)
  {
    super(con);
    setTable("COLLEGIO_ESPERTO");
    
    setField("COL_ID_COLLEGIO", BIG_DECIMAL);
    setField("ESP_ID_ESPERTO", BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
  }

  //
  // METODI GET()
  //
  public BigDecimal getColIdCollegio() 	         throws DAOException  { return getBigDecimal("COL_ID_COLLEGIO"); }
  public BigDecimal getEspIdEsperto()            throws DAOException  { return getBigDecimal("ESP_ID_ESPERTO"); }
  public String     getCodOperatoreInserimento() throws DAOException	{ return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	    getDataInserimento() 		     throws DAOException	{ return getDate("DATA_INSERIMENTO"); }
  public String     getCodUfficioInserimento() 	 throws DAOException	{ return getString("COD_UFFICIO_INSERIMENTO"); }

  //
  // METODI SET()
  //
  public void setColIdCollegio(BigDecimal aValore) 	         { setBigDecimal("COL_ID_COLLEGIO", aValore); }
  public void setEspIdEsperto(BigDecimal aValore)            { setBigDecimal("ESP_ID_ESPERTO", aValore); }
  public void setCodOperatoreInserimento(String aValore)     { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore) 	             { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore)       { setString("COD_UFFICIO_INSERIMENTO", aValore); }

  public GenericModel getModel() throws DAOException
  {
    CollegioEspertoModel lModel = new CollegioEspertoModel();
    lModel.setColIdCollegio(getColIdCollegio());
    lModel.setEspIdEsperto( getEspIdEsperto() );
    lModel.setCodOperatoreInserimento(getCodOperatoreInserimento());
    lModel.setDataInserimento(getDataInserimento());
    lModel.setCodUfficioInserimento(getCodUfficioInserimento());
    return lModel;  
  }
  
  public void setDAOFromModel(CollegioEspertoModel aModel) throws DAOException
  {
    setColIdCollegio( aModel.getColIdCollegio() );
    setEspIdEsperto(aModel.getEspIdEsperto());
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
    setCondition(" COL_ID_COLLEGIO = " + key );
  }
  
  public void setCondizioneByIdCol(BigDecimal aId)
  {
    setCondition(" COL_ID_COLLEGIO = " + aId.toString() );
  }

  public void setCondizioneIdEsperto(String key)
  {
    setCondition(" ESP_ID_ESPERTO = " + key );
  }
}