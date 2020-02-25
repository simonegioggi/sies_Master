package siap.sius.rifasius.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.rifasius.model.RiferimentoFascicoloSiusModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RiferimentoFascicoloSiusDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RiferimentoFascicoloSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RiferimentoFascicoloSiusDAO extends TableDAO
{
  public RiferimentoFascicoloSiusDAO (Connection con)
  {
    super(con);
    setTable("RIFERIMENTO_FASCICOLO_SIUS");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_RIFERIMENTO_FASCICOLO_SIUS", "RIF_SIU_SEQ");
    setFieldKey("ID_RIFERIMENTO_FASCICOLO_SIUS", BIG_DECIMAL);

    setField("ID_RIFERIMENTO_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("ANNO_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("PROGR_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("COD_UFF_FASCICOLO_SIUS", STRING);
    setField("DATA_RICEZIONE", DATE);
    setField("COD_OGGETTO_PROCEDIMENTO", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
  }

  //
  // METODI GET()
  //
  public BigDecimal 		 getIdRiferimentoFascicoloSius()  throws DAOException	 { return getBigDecimal("ID_RIFERIMENTO_FASCICOLO_SIUS"); }
  public BigDecimal 		 getAnnoFascicoloSius() 	  throws DAOException	 { return getBigDecimal("ANNO_FASCICOLO_SIUS"); }
  public BigDecimal 		 getProgrFascicoloSius() 	  throws DAOException	 { return getBigDecimal("PROGR_FASCICOLO_SIUS"); }
  public String			 getCodUffFascicoloSius() 	  throws DAOException	 { return getString("COD_UFF_FASCICOLO_SIUS"); }
  public Date			 getDataRicezione() 	          throws DAOException	 { return getDate("DATA_RICEZIONE"); }
  public String			 getCodOggettoProcedimento()      throws DAOException	 { return getString("COD_OGGETTO_PROCEDIMENTO"); }
  public BigDecimal 		 getFasSieIdFascicoloSiep()       throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }

  //
  // METODI SET()
  //
  public void  	 setIdRiferimentoFascicoloSius(BigDecimal aValore ) 	 { setBigDecimal("ID_RIFERIMENTO_FASCICOLO_SIUS", aValore); }
  public void  	 setAnnoFascicoloSius(BigDecimal aValore ) 		 { setBigDecimal("ANNO_FASCICOLO_SIUS", aValore); }
  public void  	 setProgrFascicoloSius(BigDecimal aValore ) 		 { setBigDecimal("PROGR_FASCICOLO_SIUS", aValore); }
  public void  	 setCodUffFascicoloSius(String aValore ) 		 { setString("COD_UFF_FASCICOLO_SIUS", aValore); }
  public void  	 setDataRicezione(Date aValore ) 			 { setDate("DATA_RICEZIONE", aValore); }
  public void  	 setCodOggettoProcedimento(String aValore ) 		 { setString("COD_OGGETTO_PROCEDIMENTO", aValore); }
  public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 		 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }


  public GenericModel getModel() throws DAOException
  {
  return new RiferimentoFascicoloSiusModel(
                getIdRiferimentoFascicoloSius() ,
                getAnnoFascicoloSius() ,
                getProgrFascicoloSius() ,
                getCodUffFascicoloSius() ,
                "",
                getDataRicezione() ,
                getCodOggettoProcedimento() ,
                "",
                getFasSieIdFascicoloSiep()
                );
  }

  public void setDAOFromModel(RiferimentoFascicoloSiusModel aModel) throws DAOException
  {
    setIdRiferimentoFascicoloSius( aModel.getIdRiferimentoFascicoloSius() );
    setAnnoFascicoloSius( aModel.getAnnoFascicoloSius() );
    setProgrFascicoloSius( aModel.getProgrFascicoloSius() );
    setCodUffFascicoloSius( aModel.getCodUffFascicoloSius() );
    setDataRicezione( aModel.getDataRicezione() );
    setCodOggettoProcedimento( aModel.getCodOggettoProcedimento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
  }

  public void setDAOFromModelForUpdate(RiferimentoFascicoloSiusModel aModel) throws DAOException
  {
    setIdRiferimentoFascicoloSius( aModel.getIdRiferimentoFascicoloSius() );
    setAnnoFascicoloSius( aModel.getAnnoFascicoloSius() );
    setProgrFascicoloSius( aModel.getProgrFascicoloSius() );
    setCodUffFascicoloSius( aModel.getCodUffFascicoloSius() );
    setDataRicezione( aModel.getDataRicezione() );
    setCodOggettoProcedimento( aModel.getCodOggettoProcedimento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setCondizioneUpdate(aModel.getIdRiferimentoFascicoloSius());
  }

  public void setCondizione(RiferimentoFascicoloSiusModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_RIFERIMENTO_FASCICOLO_SIUS = " + key );
  }
}
