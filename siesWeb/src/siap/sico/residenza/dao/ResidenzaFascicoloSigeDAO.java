package siap.sico.residenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.residenza.model.ResidenzaFascicoloSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ResidenzaFascicoloSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ResidenzaFascicoloSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

public class ResidenzaFascicoloSigeDAO extends TableDAO
{
	public ResidenzaFascicoloSigeDAO (Connection con)
	{
    super(con);

    setTable("RESIDENZA_FASCICOLO_SIGE");

    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("RES_ID_RESIDENZA", BIG_DECIMAL);
    setField("FAS_SIGE_ID_FASCICOLO_SIGE", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

  public Date 		getDataInizioValidita() 		throws DAOException	  { return getDate("DATA_INIZIO_VALIDITA"); }
  public Date 		getDataFineValidita() 			throws DAOException	  { return getDate("DATA_FINE_VALIDITA"); }
  public BigDecimal getResIdResidenza() 			throws DAOException	  { return getBigDecimal("RES_ID_RESIDENZA"); }
  public BigDecimal getFasSigeIdFascicoloSige() 	throws DAOException	  { return getBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE"); }

  //
  // METODI SET()
  //

  public void setDataInizioValidita(Date aValore) 			    { setDate("DATA_INIZIO_VALIDITA", aValore); }
  public void setDataFineValidita(Date aValore) 			    { setDate("DATA_FINE_VALIDITA", aValore); }
  public void setResIdResidenza(BigDecimal aValore) 			{ setBigDecimal("RES_ID_RESIDENZA", aValore); }
  public void setFasSigeIdFascicoloSige(BigDecimal aValore) 	{ setBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE", aValore); }


  public void setDAOFromModel(ResidenzaFascicoloSigeModel aModel) throws DAOException
  {
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setResIdResidenza( aModel.getResIdResidenza() );
    setFasSigeIdFascicoloSige( aModel.getFasSigeIdFascicoloSige());
	}

  public void setDAOFromModelForUpdate(ResidenzaFascicoloSigeModel aModel) throws DAOException
  {
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setResIdResidenza( aModel.getResIdResidenza() );
    setFasSigeIdFascicoloSige( aModel.getFasSigeIdFascicoloSige() );

    //setCondizioneUpdate(aModel.getIdResidenzaFascicoloSige());
  }

  public void setDAOFromModelForUpdateIdResidenza(ResidenzaFascicoloSigeModel aModel) throws DAOException
 {
   setDataInizioValidita( aModel.getDataInizioValidita() );
   setDataFineValidita( aModel.getDataFineValidita() );
   setResIdResidenza( aModel.getResIdResidenza() );
   setFasSigeIdFascicoloSige( aModel.getFasSigeIdFascicoloSige() );

   setCondizioneUpdate(aModel.getResIdResidenza());
 }

  public void setDAOFromModelForUpdateIdResidenza(BigDecimal aIdResidenza) throws DAOException
 {
   setResIdResidenza( aIdResidenza );
   setCondizioneUpdate( aIdResidenza );
 }

  public void setCondizione(ResidenzaFascicoloSigeModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneDelete(BigDecimal aIdResidenza, BigDecimal aIdFascicolo)
  {
    setCondition(" DATA_INIZIO_VALIDITA=(select max(DATA_INIZIO_VALIDITA) from RESIDENZA_FASCICOLO_SIGE where RES_ID_RESIDENZA=" + aIdResidenza + " and  FAS_SIGE_ID_FASCICOLO_SIGE= " + aIdFascicolo + ") and RES_ID_RESIDENZA=" + aIdResidenza + " and  FAS_SIGE_ID_FASCICOLO_SIGE= " + aIdFascicolo );
  }

	public void setCondizioneDeletePerFascicoloSige( BigDecimal aIdFascicolo)
  {
    setCondition(" FAS_SIGE_ID_FASCICOLO_SIGE= " + aIdFascicolo );
  }

  public void setCondizioneUpdate(BigDecimal aIdFascicolo, char aCodTipoResidenza)
  {
    String aStatement = " RES_ID_RESIDENZA =";
    aStatement += " (select RES_ID_RESIDENZA ";
    aStatement += " FROM RESIDENZA_FASCICOLO_SIGE INNER JOIN RESIDENZA ON ID_RESIDENZA=RES_ID_RESIDENZA ";
    aStatement += " WHERE COD_TIPO_RESIDENZA = '" + aCodTipoResidenza + "'";
    aStatement += " AND FAS_SIGE_ID_FASCICOLO_SIGE = "+ aIdFascicolo;
    aStatement += " AND DATA_INIZIO_VALIDITA = ";
    aStatement += " (select MAX(RESIDENZA_FASCICOLO_SIGE.DATA_INIZIO_VALIDITA) ";
    aStatement += " FROM RESIDENZA_FASCICOLO_SIGE INNER JOIN RESIDENZA ON ID_RESIDENZA=RES_ID_RESIDENZA ";
    aStatement += " WHERE COD_TIPO_RESIDENZA = '" + aCodTipoResidenza + "'";
    aStatement += " AND FAS_SIGE_ID_FASCICOLO_SIGE = "+ aIdFascicolo + "))";
    aStatement += " AND FAS_SIGE_ID_FASCICOLO_SIGE = " + aIdFascicolo;
    aStatement += " AND DATA_INIZIO_VALIDITA = ";
    aStatement += "  (select MAX(RESIDENZA_FASCICOLO_SIGE.DATA_INIZIO_VALIDITA) ";
    aStatement += "  FROM RESIDENZA_FASCICOLO_SIGE INNER JOIN RESIDENZA ON ID_RESIDENZA=RES_ID_RESIDENZA ";
    aStatement += "  WHERE COD_TIPO_RESIDENZA = '" + aCodTipoResidenza + "'";
    aStatement += "  AND FAS_SIGE_ID_FASCICOLO_SIGE =  " + aIdFascicolo + ")";
    setCondition(aStatement );

  }

	public void setCondizioneResFascCorrente(BigDecimal aIdFascicolo, String aCodTipoRes)
  {
    String lCondizione = " FAS_SIGE_ID_FASCICOLO_SIGE = "+aIdFascicolo;
    lCondizione += " AND DATA_FINE_VALIDITA IS NULL";
    lCondizione += " AND RES_ID_RESIDENZA IN (SELECT ID_RESIDENZA FROM RESIDENZA WHERE COD_TIPO_RESIDENZA  = '" + aCodTipoRes +"')";

    setCondition(lCondizione);
  }

  public void setCondizioneFasicoloResidenza(BigDecimal aIdFascicolo, BigDecimal aIdResidenza)
  {
    String lCondizione = " FAS_SIGE_ID_FASCICOLO_SIGE = "+ aIdFascicolo;
    lCondizione += " AND RES_ID_RESIDENZA = " + aIdResidenza;

    setCondition(lCondizione);
  }





	public GenericModel getModel() throws DAOException
  {
    return new ResidenzaFascicoloSigeModel(
                                            getDataInizioValidita() ,
                                            getDataFineValidita() ,
                                            getResIdResidenza() ,
                                            getFasSigeIdFascicoloSige()
                                          );
	}

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" RES_ID_RESIDENZA = " + key );
  }

	
	public void setCondizioneUpdateSoggetto(ResidenzaFascicoloSigeModel aModel)
	{
	    setCondition(" RES_ID_RESIDENZA = " + aModel.getResIdResidenza() + " AND FAS_SIGE_ID_FASCICOLO_SIGE = " + aModel.getFasSigeIdFascicoloSige());
	}
	
	
}