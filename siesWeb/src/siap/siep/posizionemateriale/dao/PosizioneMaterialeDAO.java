package siap.siep.posizionemateriale.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PosizioneMaterialeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PosizioneMateriale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

  public class PosizioneMaterialeDAO extends SIAPTableDAO
  {
    public PosizioneMaterialeDAO (Connection con)
    {
      super(con);
      setTable("POSIZIONE_MATERIALE");

      //Settare la Sequence e i campi chiave

      setField("COD_POSIZIONE_MATERIALE", STRING);
      setField("COD_UFFICIO", STRING);
      setField("DESC_POSIZIONE_MATERIALE", STRING);
      setField("COD_OPERATORE_INSERIMENTO", STRING);
      setField("DATA_INSERIMENTO", DATE);
      setField("COD_UFFICIO_INSERIMENTO", STRING);
      setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
      setField("DATA_AGGIORNAMENTO", DATE);
      setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
      setField("DATA_FINE_VALIDITA", DATE);
    }


    //
    // METODI GET()
    //

    public String getCodPosizioneMateriale()   throws DAOException { return getString("COD_POSIZIONE_MATERIALE"); }
    public String getCodUfficio() 	           throws DAOException { return getString("COD_UFFICIO"); }
    public String getDescPosizioneMateriale()  throws DAOException { return getString("DESC_POSIZIONE_MATERIALE"); }
    public String getCodOperatoreInserimento() throws DAOException { return getString("COD_OPERATORE_INSERIMENTO"); }
    public Date   getDataInserimento() 	       throws DAOException   { return getDate("DATA_INSERIMENTO"); }
    public String getCodUfficioInserimento()   throws DAOException   { return getString("COD_UFFICIO_INSERIMENTO"); }
    public String getCodOperatoreAggiornamento() throws DAOException { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
    public Date   getDataAggiornamento() 	     throws DAOException { return getDate("DATA_AGGIORNAMENTO"); }
    public String getCodUfficioAggiornamento() throws DAOException { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
    public Date   getDataFineValidita()        throws DAOException { return getDate("DATA_FINE_VALIDITA"); }

    //
    // METODI SET()
    //

    public void  	 setCodPosizioneMateriale(String aValore ) 			 { setString("COD_POSIZIONE_MATERIALE", aValore); }
    public void  	 setCodUfficio(String aValore ) 			           { setString("COD_UFFICIO", aValore); }
    public void  	 setDescPosizioneMateriale(String aValore ) 		 { setString("DESC_POSIZIONE_MATERIALE", aValore); }
    public void  	 setCodOperatoreInserimento(String aValore ) 		 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
    public void  	 setDataInserimento(Date aValore ) 			         { setDate("DATA_INSERIMENTO", aValore); }
    public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
    public void  	 setCodOperatoreAggiornamento(String aValore ) 	 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
    public void  	 setDataAggiornamento(Date aValore ) 			       { setDate("DATA_AGGIORNAMENTO", aValore); }
    public void  	 setCodUfficioAggiornamento(String aValore ) 		 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
    public void    setDataFineValidita(Date aValore )              { setDate("DATA_FINE_VALIDITA", aValore); }


    public GenericModel getModel() throws DAOException
    {
      return new PosizioneMaterialeModel(
                                          getCodPosizioneMateriale() ,
                                          getCodUfficio() ,
                                          "",
                                          getDescPosizioneMateriale() ,
                                          getCodOperatoreInserimento() ,
                                          getDataInserimento() ,
                                          getCodUfficioInserimento() ,
                                          "",
                                          getCodOperatoreAggiornamento() ,
                                          getDataAggiornamento() ,
                                          getCodUfficioAggiornamento(),
                                          ""
                                        );
    }


    public void setDAOFromModel(PosizioneMaterialeModel aModel) throws DAOException
    {
      setCodPosizioneMateriale( aModel.getCodPosizioneMateriale() );
      setCodUfficio( aModel.getCodUfficio() );
      setDescPosizioneMateriale( aModel.getDescPosizioneMateriale() );
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
      setDataInserimento( aModel.getDataInserimento() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setDataFineValidita( aModel.getDataFineValidita() );
    }


    public void setDAOFromModelForUpdate(PosizioneMaterialeModel aModel) throws DAOException
    {
      setDescPosizioneMateriale( aModel.getDescPosizioneMateriale() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setDataFineValidita( aModel.getDataFineValidita() );
      
      setCondizione(aModel.getCodPosizioneMateriale(), aModel.getCodUfficio());
    }


    public void setCondizione(PosizioneMaterialeModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;
      if ( lInserito ) setCondition(lCondizioni);
    }

    public void setCondizione(String aCodPosizioneMateriale, String aCodUfficio) throws DAOException
    {
      if (aCodPosizioneMateriale == null ||  aCodPosizioneMateriale.trim().length() < 1 || aCodUfficio == null || aCodUfficio.trim().length() < 1 )
        throw new DAOException("errore nelle condizioni di filtro");
      setCondition("COD_POSIZIONE_MATERIALE= '" + aCodPosizioneMateriale + "'" + " AND COD_UFFICIO = '" + aCodUfficio + "'") ;
    }


    public void setCondizioneUpdate(BigDecimal key)
    {
	// setCondition(" ID_POSIZIONE_MATERIALE = " + key );
    }
}
