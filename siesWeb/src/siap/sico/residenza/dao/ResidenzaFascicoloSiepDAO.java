package siap.sico.residenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ResidenzaFascicoloSiepDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ResidenzaFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ResidenzaFascicoloSiepDAO extends TableDAO
{
	public ResidenzaFascicoloSiepDAO (Connection con)
	{
    super(con);

    setTable("RESIDENZA_FASCICOLO_SIEP");

    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("RES_ID_RESIDENZA", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

  public Date 			getDataInizioValidita() 		throws DAOException	    { return getDate("DATA_INIZIO_VALIDITA"); }
  public Date 			getDataFineValidita() 		throws DAOException	      { return getDate("DATA_FINE_VALIDITA"); }
  public BigDecimal getResIdResidenza() 		throws DAOException	        { return getBigDecimal("RES_ID_RESIDENZA"); }
  public BigDecimal getFasSieIdFascicoloSiep() 		throws DAOException	  { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }

  //
  // METODI SET()
  //

  public void setDataInizioValidita(Date aValore) 			    { setDate("DATA_INIZIO_VALIDITA", aValore); }
  public void setDataFineValidita(Date aValore) 			      { setDate("DATA_FINE_VALIDITA", aValore); }
  public void setResIdResidenza(BigDecimal aValore) 			  { setBigDecimal("RES_ID_RESIDENZA", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore) 	{ setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }

	public GenericModel getModel() throws DAOException
  {
    return new ResidenzaFascicoloSiepModel(
                                            getDataInizioValidita() ,
                                            getDataFineValidita() ,
                                            getResIdResidenza() ,
                                            getFasSieIdFascicoloSiep()
                                          );
	}

  public void setDAOFromModel(ResidenzaFascicoloSiepModel aModel) throws DAOException
  {
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setResIdResidenza( aModel.getResIdResidenza() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
	}

  public void setDAOFromModelForUpdate(ResidenzaFascicoloSiepModel aModel) throws DAOException
  {
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setResIdResidenza( aModel.getResIdResidenza() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );

    //setCondizioneUpdate(aModel.getIdResidenzaFascicoloSiep());
  }

	public void setCondizione(ResidenzaFascicoloSiepModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneResFascCorrente(BigDecimal aIdFascicolo, BigDecimal aIdResidenza)
  {
    String lCondizione = " FAS_SIE_ID_FASCICOLO_SIEP = "+aIdFascicolo;
    lCondizione += " AND RES_ID_RESIDENZA = "+aIdResidenza;
    lCondizione += " AND DATA_FINE_VALIDITA IS NULL";

    setCondition(lCondizione);
  }
	
	public void setCondizioneIdFascicoloSiep(BigDecimal aIdFascicolo)
	  {
	    String lCondizione = " FAS_SIE_ID_FASCICOLO_SIEP = "+aIdFascicolo;
	    lCondizione += " AND DATA_FINE_VALIDITA IS NULL";
	    setCondition(lCondizione);
	  }
	
	

/*
	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_RESIDENZA_FASCICOLO_SIEP = " + key );
  }
*/
}
