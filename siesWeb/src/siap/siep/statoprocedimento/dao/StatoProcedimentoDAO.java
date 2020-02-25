package siap.siep.statoprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: StatoProcedimentoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella StatoProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class StatoProcedimentoDAO extends TableDAO
{
	public StatoProcedimentoDAO (Connection con)
	{
    super(con);

    setTable("STATO_PROCEDIMENTO");

    //Settare la Sequence e i campi chiave

    setField("PROGRESSIVO", BIG_DECIMAL);
    setField("COD_STATO_PROCEDIMENTO", STRING);
    setField("DATA", DATE);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
	}

  //
  // METODI GET()
  //

  public BigDecimal getProgressivo() 		throws DAOException	            { return getBigDecimal("PROGRESSIVO"); }
  public String 		getCodStatoProcedimento() 		throws DAOException	  { return getString("COD_STATO_PROCEDIMENTO"); }
  public Date 			getData() 		throws DAOException	                  { return getDate("DATA"); }
  public String 		getCodOperatoreInserimento() 		throws DAOException	{ return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			getDataInserimento() 		throws DAOException	        { return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 		throws DAOException	  { return getString("COD_UFFICIO_INSERIMENTO"); }
  public BigDecimal getFasSieIdFascicoloSiep() 		throws DAOException	  { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public BigDecimal getEveIdEvento() 		throws DAOException	            { return getBigDecimal("EVE_ID_EVENTO"); }

  //
  // METODI SET()
  //

  public void setProgressivo(BigDecimal aValore ) 			     { setBigDecimal("PROGRESSIVO", aValore); }
  public void setCodStatoProcedimento(String aValore ) 			 { setString("COD_STATO_PROCEDIMENTO", aValore); }
  public void setData(Date aValore ) 			                   { setDate("DATA", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 	 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			       { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 		 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore )  { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setEveIdEvento(BigDecimal aValore )            { setBigDecimal("EVE_ID_EVENTO", aValore); }

	public GenericModel getModel() throws DAOException
  {
    return new StatoProcedimentoModel(
                                      getProgressivo() ,
                                      getCodStatoProcedimento() ,
                                      "",
                                      getData() ,
                                      getCodOperatoreInserimento() ,
                                      getDataInserimento() ,
                                      getCodUfficioInserimento() ,
                                      "",
                                      getFasSieIdFascicoloSiep(),
                                      getEveIdEvento()
                                     );
  }

  public void setDAOFromModel(StatoProcedimentoModel aModel) throws DAOException
  {
    setProgressivo( aModel.getProgressivo() );
    setCodStatoProcedimento( aModel.getCodStatoProcedimento() );
    setData( aModel.getData() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setEveIdEvento( aModel.getEveIdEvento() );
  }

  public void setDAOFromModelForUpdate(StatoProcedimentoModel aModel) throws DAOException
  {
    setProgressivo( aModel.getProgressivo() );
    setCodStatoProcedimento( aModel.getCodStatoProcedimento() );
    setData( aModel.getData() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setCondizioneUpdate(aModel);
  }

	public void setCondizione(StatoProcedimentoModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneByIdFascicolo(BigDecimal aIdFascicolo)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = "+ aIdFascicolo);
  }

	public void setCondizioneUpdate(StatoProcedimentoModel aModel)
  {
    setCondition(" PROGRESSIVO = " + aModel.getProgressivo() +" AND FAS_SIE_ID_FASCICOLO_SIEP = "+ aModel.getFasSieIdFascicoloSiep());
  }
}
