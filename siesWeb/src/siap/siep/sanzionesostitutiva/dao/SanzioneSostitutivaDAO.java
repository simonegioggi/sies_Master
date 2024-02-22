package siap.siep.sanzionesostitutiva.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: SanzioneSostitutivaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella SanzioneSostitutiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class SanzioneSostitutivaDAO extends SIAPTableDAO
{
  public SanzioneSostitutivaDAO (Connection con)
  {
    super(con);

    setTable("SANZIONE_SOSTITUTIVA");

    setSequenceField("ID_SANZIONE_SOSTITUTIVA", "SAN_SOS_SEQ");

    setFieldKey("ID_SANZIONE_SOSTITUTIVA", BIG_DECIMAL);

    setField("ID_SANZIONE_SOSTITUTIVA", BIG_DECIMAL);
    setField("COD_TIPO_SANZIONE", STRING);
    setField("NUM_ANNI", BIG_DECIMAL);
    setField("NUM_MESI", BIG_DECIMAL);
    setField("NUM_GIORNI", BIG_DECIMAL);
    setField("SANZIONE_PECUNIARIA_MULTA", BIG_DECIMAL);
    setField("ANNO_REGISTRO", BIG_DECIMAL);
    setField("NUM_REGISTRO", BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("PEN_COM_ID_PENA_COMPLESSIVA", BIG_DECIMAL);
    setField("SANZIONE_PECUNIARIA_AMMENDA", BIG_DECIMAL);
  }

  //
  // METODI GET()
  //

  public BigDecimal getIdSanzioneSostitutiva() 	     throws DAOException	  { return getBigDecimal("ID_SANZIONE_SOSTITUTIVA"); }
  public String     getCodTipoSanzione() 	     throws DAOException	        { return getString("COD_TIPO_SANZIONE"); }
  public BigDecimal getNumAnni() 		     throws DAOException	              { return getBigDecimal("NUM_ANNI"); }
  public BigDecimal getNumMesi() 		     throws DAOException	              { return getBigDecimal("NUM_MESI"); }
  public BigDecimal getNumGiorni() 		     throws DAOException	            { return getBigDecimal("NUM_GIORNI"); }
  public BigDecimal getSanzionePecuniariaMulta() 	     throws DAOException	    { return getBigDecimal("SANZIONE_PECUNIARIA_MULTA"); }
  public BigDecimal getAnnoRegistro() 		     throws DAOException	        { return getBigDecimal("ANNO_REGISTRO"); }
  public BigDecimal getNumRegistro() 		     throws DAOException	          { return getBigDecimal("NUM_REGISTRO"); }
  public String     getCodOperatoreInserimento()     throws DAOException	  { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	    getDataInserimento() 	     throws DAOException	        { return getDate("DATA_INSERIMENTO"); }
  public String     getCodUfficioInserimento() 	     throws DAOException	  { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String     getCodOperatoreAggiornamento()   throws DAOException	  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	    getDataAggiornamento() 	     throws DAOException	      { return getDate("DATA_AGGIORNAMENTO"); }
  public String     getCodUfficioAggiornamento()     throws DAOException	  { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getPenComIdPenaComplessiva() 		throws DAOException	    { return getBigDecimal("PEN_COM_ID_PENA_COMPLESSIVA"); }
  public BigDecimal getSanzionePecuniariaAmmenda() 	     throws DAOException	    { return getBigDecimal("SANZIONE_PECUNIARIA_AMMENDA"); }

  //
  // METODI SET()
  //

  public void setIdSanzioneSostitutiva(BigDecimal aValore ) 	  { setBigDecimal("ID_SANZIONE_SOSTITUTIVA", aValore); }
  public void setCodTipoSanzione(String aValore ) 		          { setString("COD_TIPO_SANZIONE", aValore); }
  public void setNumAnni(BigDecimal aValore ) 			            { setBigDecimal("NUM_ANNI", aValore); }
  public void setNumMesi(BigDecimal aValore ) 			            { setBigDecimal("NUM_MESI", aValore); }
  public void setNumGiorni(BigDecimal aValore ) 		            { setBigDecimal("NUM_GIORNI", aValore); }
  public void setSanzionePecuniariaMulta(BigDecimal aValore ) 	      { setBigDecimal("SANZIONE_PECUNIARIA_MULTA", aValore); }
  public void setAnnoRegistro(BigDecimal aValore ) 		          { setBigDecimal("ANNO_REGISTRO", aValore); }
  public void setNumRegistro(BigDecimal aValore ) 		          { setBigDecimal("NUM_REGISTRO", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 	    { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 		            { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 	      { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 	  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 		          { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 	    { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setPenComIdPenaComplessiva(BigDecimal aValore ) 	{ setBigDecimal("PEN_COM_ID_PENA_COMPLESSIVA", aValore); }
  public void setSanzionePecuniariaAmmenda(BigDecimal aValore ) 	      { setBigDecimal("SANZIONE_PECUNIARIA_AMMENDA", aValore); }

  public GenericModel getModel() throws DAOException
  {
    return new SanzioneSostitutivaModel( getIdSanzioneSostitutiva() ,
                                         getCodTipoSanzione() ,
                                         "",
                                         getNumAnni() ,
                                         getNumMesi() ,
                                         getNumGiorni() ,
                                         getSanzionePecuniariaMulta() ,
                                         getAnnoRegistro() ,
                                         getNumRegistro() ,
                                         getCodOperatoreInserimento() ,
                                         getDataInserimento() ,
                                         getCodUfficioInserimento() ,
                                         "",
                                         getCodOperatoreAggiornamento() ,
                                         getDataAggiornamento() ,
                                         getCodUfficioAggiornamento() ,
                                         "",
                                         getPenComIdPenaComplessiva() ,
    									 getSanzionePecuniariaAmmenda(),
    									 ""
    									 );
  }

  public void setDAOFromModel(SanzioneSostitutivaModel aModel) throws DAOException
  {
    setIdSanzioneSostitutiva( aModel.getIdSanzioneSostitutiva() );
    setCodTipoSanzione( aModel.getCodTipoSanzione() );
    setNumAnni( aModel.getNumAnni() );
    setNumMesi( aModel.getNumMesi() );
    setNumGiorni( aModel.getNumGiorni() );
    setSanzionePecuniariaMulta( aModel.getSanzionePecuniariaMulta() );
    setAnnoRegistro( aModel.getAnnoRegistro() );
    setNumRegistro( aModel.getNumRegistro() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setPenComIdPenaComplessiva( aModel.getPenComIdPenaComplessiva() );
    setSanzionePecuniariaAmmenda( aModel.getSanzionePecuniariaAmmenda() );
  }

  public void setDAOFromModelForUpdate(SanzioneSostitutivaModel aModel) throws DAOException
  {
    //setIdSanzioneSostitutiva( aModel.getIdSanzioneSostitutiva() );
    setCodTipoSanzione( aModel.getCodTipoSanzione() );
    setNumAnni( aModel.getNumAnni() );
    setNumMesi( aModel.getNumMesi() );
    setNumGiorni( aModel.getNumGiorni() );
    setSanzionePecuniariaMulta( aModel.getSanzionePecuniariaMulta() );
    setAnnoRegistro( aModel.getAnnoRegistro() );
    setNumRegistro( aModel.getNumRegistro() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setSanzionePecuniariaAmmenda( aModel.getSanzionePecuniariaAmmenda() );
    //setPenComIdPenaComplessiva( aModel.getPenComIdPenaComplessiva() );

    setCondizioneUpdate(aModel.getIdSanzioneSostitutiva());
  }

  public void setCondizione(SanzioneSostitutivaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_SANZIONE_SOSTITUTIVA = " + key );
  }

  public void setCondizioneByIdFascicolo(BigDecimal aIdFascicolo)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo);
  }

  public void setCondizioneByIdPenaComplessiva(BigDecimal aIdPenaComplessiva)
  {
    setCondition(" PEN_COM_ID_PENA_COMPLESSIVA = " + aIdPenaComplessiva);
  }
}
