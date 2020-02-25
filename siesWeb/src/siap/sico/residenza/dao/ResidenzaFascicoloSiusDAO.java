package siap.sico.residenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ResidenzaFascicoloSiusDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ResidenzaFascicoloSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ResidenzaFascicoloSiusDAO extends TableDAO
{
	public ResidenzaFascicoloSiusDAO (Connection con)
	{
    super(con);

    setTable("RESIDENZA_FASCICOLO_SIUS");

    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("RES_ID_RESIDENZA", BIG_DECIMAL);
    setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

  public Date 			getDataInizioValidita() 		throws DAOException	    { return getDate("DATA_INIZIO_VALIDITA"); }
  public Date 			getDataFineValidita() 		throws DAOException	      { return getDate("DATA_FINE_VALIDITA"); }
  public BigDecimal getResIdResidenza() 		throws DAOException	        { return getBigDecimal("RES_ID_RESIDENZA"); }
  public BigDecimal getFasSiuIdFascicoloSius() 		throws DAOException	  { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }

  //
  // METODI SET()
  //

  public void setDataInizioValidita(Date aValore) 			    { setDate("DATA_INIZIO_VALIDITA", aValore); }
  public void setDataFineValidita(Date aValore) 			      { setDate("DATA_FINE_VALIDITA", aValore); }
  public void setResIdResidenza(BigDecimal aValore) 			  { setBigDecimal("RES_ID_RESIDENZA", aValore); }
  public void setFasSiuIdFascicoloSius(BigDecimal aValore) 	{ setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }

/*
	public GenericModel getModel() throws DAOException
  {
    return new ResidenzaFascicoloSiusModel(
                                            getDataInizioValidita() ,
                                            getDataFineValidita() ,
                                            getResIdResidenza() ,
                                            getFasSieIdFascicoloSius()
                                          );
	}
*/

  public void setDAOFromModel(ResidenzaFascicoloSiusModel aModel) throws DAOException
  {
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setResIdResidenza( aModel.getResIdResidenza() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius());
	}

  public void setDAOFromModelForUpdate(ResidenzaFascicoloSiusModel aModel) throws DAOException
  {
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setResIdResidenza( aModel.getResIdResidenza() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );

    //setCondizioneUpdate(aModel.getIdResidenzaFascicoloSius());
  }

  public void setDAOFromModelForUpdateIdResidenza(ResidenzaFascicoloSiusModel aModel) throws DAOException
 {
   setDataInizioValidita( aModel.getDataInizioValidita() );
   setDataFineValidita( aModel.getDataFineValidita() );
   setResIdResidenza( aModel.getResIdResidenza() );
   setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );

   setCondizioneUpdate(aModel.getResIdResidenza());
 }

  public void setDAOFromModelForUpdateIdResidenza(BigDecimal aIdResidenza) throws DAOException
 {
   setResIdResidenza( aIdResidenza );

   setCondizioneUpdate( aIdResidenza );
 }

	public void setCondizione(ResidenzaFascicoloSiusModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneDelete(BigDecimal aIdResidenza, BigDecimal aIdFascicolo)
  {
    setCondition(" DATA_INIZIO_VALIDITA=(select max(DATA_INIZIO_VALIDITA) from RESIDENZA_FASCICOLO_SIUS where RES_ID_RESIDENZA=" + aIdResidenza + " and  FAS_SIU_ID_FASCICOLO_SIUS= " + aIdFascicolo + ") and RES_ID_RESIDENZA=" + aIdResidenza + " and  FAS_SIU_ID_FASCICOLO_SIUS= " + aIdFascicolo );
  }

	public void setCondizioneDeletePerFascicoloSius( BigDecimal aIdFascicolo)
  {
    setCondition(" FAS_SIU_ID_FASCICOLO_SIUS= " + aIdFascicolo );
  }

	public void setCondizioneUpdate(BigDecimal aIdFascicolo, char aCodTipoResidenza)
  {
    String aStatement = " RES_ID_RESIDENZA =";
    aStatement += "  (select RES_ID_RESIDENZA ";
    aStatement += "  FROM RESIDENZA_FASCICOLO_SIUS INNER JOIN RESIDENZA ON ID_RESIDENZA=RES_ID_RESIDENZA ";
    aStatement += "  WHERE COD_TIPO_RESIDENZA = '" + aCodTipoResidenza + "'";
    aStatement += "  AND FAS_SIU_ID_FASCICOLO_SIUS = "+ aIdFascicolo;
    aStatement += "  AND DATA_INIZIO_VALIDITA = ";
    aStatement += "  (select MAX(RESIDENZA_FASCICOLO_SIUS.DATA_INIZIO_VALIDITA) ";
    aStatement += "  FROM RESIDENZA_FASCICOLO_SIUS INNER JOIN RESIDENZA ON ID_RESIDENZA=RES_ID_RESIDENZA ";
    aStatement += "  WHERE COD_TIPO_RESIDENZA = '" + aCodTipoResidenza + "'";
    aStatement += "  AND FAS_SIU_ID_FASCICOLO_SIUS = "+ aIdFascicolo + "))";
    aStatement += " AND FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicolo;
    aStatement += " AND DATA_INIZIO_VALIDITA = ";
    aStatement += "  (select MAX(RESIDENZA_FASCICOLO_SIUS.DATA_INIZIO_VALIDITA) ";
    aStatement += "  FROM RESIDENZA_FASCICOLO_SIUS INNER JOIN RESIDENZA ON ID_RESIDENZA=RES_ID_RESIDENZA ";
    aStatement += "  WHERE COD_TIPO_RESIDENZA = '" + aCodTipoResidenza + "'";
    aStatement += "  AND FAS_SIU_ID_FASCICOLO_SIUS =  " + aIdFascicolo + ")";
    setCondition(aStatement );

  }

	public void setCondizioneResFascCorrente(BigDecimal aIdFascicolo, String aCodTipoRes)
  {
    String lCondizione = " FAS_SIU_ID_FASCICOLO_SIUS = "+aIdFascicolo;
    lCondizione += " AND DATA_FINE_VALIDITA IS NULL";
    lCondizione += " AND RES_ID_RESIDENZA IN (SELECT ID_RESIDENZA FROM RESIDENZA WHERE COD_TIPO_RESIDENZA  = '" + aCodTipoRes +"')";

    setCondition(lCondizione);
  }

  public void setCondizioneFasicoloResidenza(BigDecimal aIdFascicolo, BigDecimal aIdResidenza)
  {
    String lCondizione = " FAS_SIU_ID_FASCICOLO_SIUS = "+ aIdFascicolo;
    lCondizione += " AND RES_ID_RESIDENZA = " + aIdResidenza;

    setCondition(lCondizione);
  }





	public GenericModel getModel() throws DAOException
  {
    return new ResidenzaFascicoloSiusModel(
                                            getDataInizioValidita() ,
                                            getDataFineValidita() ,
                                            getResIdResidenza() ,
                                            getFasSiuIdFascicoloSius()
                                          );
	}

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" RES_ID_RESIDENZA = " + key );
  }

}