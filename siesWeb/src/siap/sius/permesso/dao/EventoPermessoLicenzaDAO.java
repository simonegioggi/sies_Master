package siap.sius.permesso.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: EventoPermessoLicenzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella EventoPermessoLicenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class EventoPermessoLicenzaDAO extends TableDAO
{
  public EventoPermessoLicenzaDAO (Connection con)
  {
    super(con);
		setTable("EVENTO_PERMESSO_LICENZA");

		//Settare la Sequence e i campi chiave
    setSequenceField("ID_EVENTO_PERMESSO_LICENZA","EVE_PER_LIC_SEQ");
    setFieldKey("ID_EVENTO_PERMESSO_LICENZA", BIG_DECIMAL);
    
		setField("ID_EVENTO_PERMESSO_LICENZA", BIG_DECIMAL);
		setField("COD_TIPO_EVENTO", STRING);
    setField("DATA_SEGNALAZIONE", DATE);
    setField("DESCR_EVENTO", STRING);
    setField("MITTENTE_SEGNALAZIONE", STRING);
		setField("COD_TIPO_CONSEGUENZA", STRING);
    setField("DESCR_CONSEGUENZE", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);			 
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("LIC_ID_LICENZA_LIBANTICIPATA", BIG_DECIMAL);   
	}

  //
  // METODI GET()
  //
  public BigDecimal getIdEventoPermessoLicenza()    throws DAOException { return getBigDecimal("ID_EVENTO_PERMESSO_LICENZA"); }  
  public String     getCodTipoEvento()              throws DAOException { return getString("COD_TIPO_EVENTO"); }
  public Date       getDataSegnalazione()           throws DAOException { return getDate("DATA_SEGNALAZIONE"); }
  public String     getDescrEvento()                throws DAOException { return getString("DESCR_EVENTO"); }
  public String     getMittenteSegnalazione()       throws DAOException { return getString("MITTENTE_SEGNALAZIONE"); }
  public String     getCodTipoConseguenza()         throws DAOException { return getString("COD_TIPO_CONSEGUENZA"); }
  public String     getDescrConseguenze()           throws DAOException { return getString("DESCR_CONSEGUENZE"); }
  public String     getCodOperatoreInserimento()    throws DAOException { return getString("COD_OPERATORE_INSERIMENTO"); }
  public String     getCodUfficioInserimento()      throws DAOException { return getString("COD_UFFICIO_INSERIMENTO"); }
  public Date       getDataInserimento()            throws DAOException { return getDate("DATA_INSERIMENTO"); }
  public String     getCodOperatoreAggiornamento()  throws DAOException { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public String     getCodUfficioAggiornamento()    throws DAOException { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public Date       getDataAggiornamento()          throws DAOException { return getDate("DATA_AGGIORNAMENTO"); }
  public BigDecimal getLicIdLicenzaLibAnticipata()  throws DAOException { return getBigDecimal("LIC_ID_LICENZA_LIBANTICIPATA"); }

  //
  // METODI SET()
  //
  public void setIdEventoPermessoLicenza(BigDecimal aValore)    { setBigDecimal("ID_EVENTO_PERMESSO_LICENZA", aValore); }  
  public void setCodTipoEvento(String aValore)                  { setString("COD_TIPO_EVENTO", aValore); }
  public void setDataSegnalazione(Date aValore)                 { setDate("DATA_SEGNALAZIONE", aValore); }
  public void setDescrEvento(String aValore)                    { setString("DESCR_EVENTO", aValore); }
  public void setMittenteSegnalazione(String aValore)           { setString("MITTENTE_SEGNALAZIONE", aValore); }
  public void setCodTipoConseguenza(String aValore)             { setString("COD_TIPO_CONSEGUENZA", aValore); }
  public void setDescrConseguenze(String aValore)               { setString("DESCR_CONSEGUENZE", aValore); }
  public void setCodOperatoreInserimento(String aValore)        { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore)          { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore)                  { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore)      { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore)        { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore)                { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setLicIdLicenzaLibAnticipata(BigDecimal aValore)  { setBigDecimal("LIC_ID_LICENZA_LIBANTICIPATA", aValore); }

  public GenericModel getModel() throws DAOException
  {
    EventoPermessoLicenzaModel lEvPermLicMod = new EventoPermessoLicenzaModel();
    
    lEvPermLicMod.setIdEventoPermessoLicenza(getIdEventoPermessoLicenza());
    lEvPermLicMod.setCodTipoEvento(getCodTipoEvento());
    lEvPermLicMod.setDataSegnalazione(getDataSegnalazione());
    lEvPermLicMod.setMittenteSegnalazione(getMittenteSegnalazione());
    lEvPermLicMod.setDescrEvento(getDescrEvento());
    lEvPermLicMod.setCodTipoConseguenza(getCodTipoConseguenza());
    lEvPermLicMod.setDescrConseguenze(getDescrConseguenze());
    lEvPermLicMod.setCodOperatoreInserimento(getCodOperatoreInserimento());
    lEvPermLicMod.setCodUfficioInserimento(getCodUfficioInserimento());
    lEvPermLicMod.setDataInserimento(getDataInserimento());
    lEvPermLicMod.setCodOperatoreAggiornamento(getCodOperatoreAggiornamento());
    lEvPermLicMod.setCodUfficioInserimento(getCodUfficioInserimento());
    lEvPermLicMod.setDataAggiornamento(getDataAggiornamento());
    lEvPermLicMod.setLicIdLicenzaLibAnticipata(getLicIdLicenzaLibAnticipata());
    
    return lEvPermLicMod;
  }
    
	public void setDAOFromModel(EventoPermessoLicenzaModel aModel) throws DAOException
	{
    setIdEventoPermessoLicenza(aModel.getIdEventoPermessoLicenza());  
    setCodTipoEvento(aModel.getCodTipoEvento());
    setDataSegnalazione(aModel.getDataSegnalazione());
    setDescrEvento(aModel.getDescrEvento());
    setMittenteSegnalazione(aModel.getMittenteSegnalazione());
    setCodTipoConseguenza(aModel.getCodTipoConseguenza());
    setDescrConseguenze(aModel.getDescrConseguenze());
    setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
    setCodUfficioInserimento(aModel.getCodUfficioInserimento());
    setDataInserimento(aModel.getDataInserimento()); 
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento()); 
    setLicIdLicenzaLibAnticipata(aModel.getLicIdLicenzaLibAnticipata());
  }
 
	public void setDAOFromModelForUpdate(EventoPermessoLicenzaModel aModel) throws DAOException
  {
    setIdEventoPermessoLicenza(aModel.getIdEventoPermessoLicenza());  
    setCodTipoEvento(aModel.getCodTipoEvento());
    setDataSegnalazione(aModel.getDataSegnalazione());
    setDescrEvento(aModel.getDescrEvento());
    setMittenteSegnalazione(aModel.getMittenteSegnalazione());
    setCodTipoConseguenza(aModel.getCodTipoConseguenza());
    setDescrConseguenze(aModel.getDescrConseguenze());
    setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
    setCodUfficioInserimento(aModel.getCodUfficioInserimento());
    setDataInserimento(aModel.getDataInserimento()); 
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento()); 
    setLicIdLicenzaLibAnticipata(aModel.getLicIdLicenzaLibAnticipata());

    setCondizioneUpdate(aModel.getIdEventoPermessoLicenza());
	}
/*
	public void setCondizione(EventoPermessoLicenzaModel aModel)
	{
	   String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
	}
*/

	public void setCondizioneUpdate(BigDecimal aKey)
	{
	  setCondition(" ID_EVENTO_PERMESSO_LICENZA = '" + aKey + "'" );
	}
  
  public void setCondizioneByIdLic( BigDecimal aKey )
  {
    setCondition(" LIC_ID_LICENZA_LIBANTICIPATA = '" + aKey + "'" );
  }
  
}
