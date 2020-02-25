package siap.sico.avvocato.dao;

/**
* <p>Title: AvvocatoDAO</p>
* <p>Description: Classe DAO che rrapèpresenta la tabella Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.avvocato.model.AvvocatoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

public class AvvocatoDAO extends SIAPTableDAO
{
  public AvvocatoDAO (Connection con)
  {
    super(con);
    
    setTable("AVVOCATO");
    
    setSequenceField("ID_AVVOCATO", "AVV_SEQ");
    
    setField("ID_AVVOCATO", BIG_DECIMAL);
    setField("COGNOME", STRING);
    setField("NOME", STRING);
    setField("FORO", STRING);
    setField("INDIRIZZO", STRING);
    setField("TELEFONO", STRING);
    setField("FAX", STRING);
    setField("E_MAIL", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("COD_LUOGO_NASCITA", STRING);
    setField("COD_COMUNE_RESIDENZA", STRING);
    setField("DATA_NASCITA", DATE);
    setField("DATA_SOSPESO_FINO_AL", DATE);
    setField("DATA_RADIATO_DAL", DATE);
    setField("COD_NON_ATTIVITA", STRING);
    setField("COD_UFFICIO_APPARTENENZA", STRING);
    setField("NOTE", STRING);
    setField("FLAG_CANCELLATO", STRING);
    setField("COD_FISCALE", STRING);
    setField("PROVINCIA", STRING);
    setField("CAP", STRING);
    setField("FLAG_VISUALIZZA", BIG_DECIMAL);
    setField("ID_AVVOCATO_STANDARD", BIG_DECIMAL);
    

   }

  //
  // METODI GET()
  //
  
  public BigDecimal  getIdAvvocato()          throws DAOException  { return getBigDecimal("ID_AVVOCATO"); }
  public String      getCognome()             throws DAOException  { return getString("COGNOME"); }
  public String      getNome()              throws DAOException  { return getString("NOME"); }
  public String      getForo()              throws DAOException  { return getString("FORO"); }
  public String      getIndirizzo()           throws DAOException  { return getString("INDIRIZZO"); }
  public String      getTelefono()            throws DAOException  { return getString("TELEFONO"); }
  public String      getFax()               throws DAOException  { return getString("FAX"); }
  public String      getEMail()             throws DAOException  { return getString("E_MAIL"); }
  public String      getCodOperatoreInserimento()     throws DAOException  { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date        getDataInserimento()         throws DAOException  { return getDate("DATA_INSERIMENTO"); }
  public String      getCodOperatoreAggiornamento()   throws DAOException  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date        getDataAggiornamento()       throws DAOException  { return getDate("DATA_AGGIORNAMENTO"); }
  public String      getCodUfficioInserimento()     throws DAOException  { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String      getCodUfficioAggiornamento()     throws DAOException  { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public String      getCodLuogoNascita()         throws DAOException  { return getString("COD_LUOGO_NASCITA"); }
  public String      getCodComuneResidenza()      throws DAOException  { return getString("COD_COMUNE_RESIDENZA"); }
  public Date        getDataNascita()           throws DAOException  { return getDate("DATA_NASCITA"); }
  public Date        getDataSospensione()         throws DAOException  { return getDate("DATA_SOSPESO_FINO_AL"); }
  public Date        getDataRadiazione()        throws DAOException  { return getDate("DATA_RADIATO_DAL"); }
  public String      getCodNonAttivita ()         throws DAOException  { return getString("COD_NON_ATTIVITA"); }
  public String      getCodUffAppartenenza()      throws DAOException  { return getString("COD_UFFICIO_APPARTENENZA"); }
  public String      getNote()              throws DAOException  { return getString("NOTE"); }
  public String      getFlagCancellato()        throws DAOException  { return getString("FLAG_CANCELLATO"); }
  public String      getCodiceFiscale()         throws DAOException  { return getString("COD_FISCALE"); }
  public String      getProvincia()           throws DAOException  { return getString("PROVINCIA"); }
  public String      getCap()             throws DAOException  { return getString("CAP"); }
  public BigDecimal  getFlagVisualizza()        throws DAOException  { return getBigDecimal("FLAG_VISUALIZZA"); }
  public BigDecimal  getIdAvvocatoStandard()      throws DAOException  { return getBigDecimal("ID_AVVOCATO_STANDARD"); }

  //
  // METODI SET()
  //
  public void setIdAvvocato(BigDecimal aValore)       {setBigDecimal("ID_AVVOCATO", aValore);}
  public void setCognome(String aValore)            {setString("COGNOME", aValore);}
  public void setNome(String aValore)             {setString("NOME", aValore);}
  public void setForo(String aValore)             {setString("FORO", aValore);}
  public void setIndirizzo(String aValore)          {setString("INDIRIZZO", aValore);}
  public void setTelefono(String aValore)           {setString("TELEFONO", aValore);}
  public void setFax(String aValore)                  {setString("FAX", aValore);}
  public void setEMail(String aValore)            {setString("E_MAIL", aValore);}
  public void setCodOperatoreInserimento(String aValore)  {setString("COD_OPERATORE_INSERIMENTO", aValore);}
  public void setDataInserimento(Date aValore)      {setDate("DATA_INSERIMENTO", aValore);}
  public void setCodOperatoreAggiornamento(String aValore) {setString("COD_OPERATORE_AGGIORNAMENTO", aValore);}
  public void setDataAggiornamento(Date aValore)      {setDate("DATA_AGGIORNAMENTO", aValore);}
  public void setCodUfficioInserimento(String aValore)  {setString("COD_UFFICIO_INSERIMENTO", aValore);}
  public void setCodUfficioAggiornamento(String aValore)  {setString("COD_UFFICIO_AGGIORNAMENTO", aValore);}
  public void setCodLuogoNascita(String aValore)      {setString("COD_LUOGO_NASCITA", aValore);}
  public void setCodComuneResidenza(String aValore)     {setString("COD_COMUNE_RESIDENZA", aValore);}
  public void setDataNascita(Date aValore)        {setDate("DATA_NASCITA", aValore);}
  public void setDataSospensione(Date aValore)      {setDate("DATA_SOSPESO_FINO_AL", aValore);}
  public void setDataRadiazione(Date aValore)       {setDate("DATA_RADIATO_DAL", aValore);}
  public void setCodNonAttivita(String aValore)       {setString("COD_NON_ATTIVITA", aValore);}
  public void setCodUffAppartenenza(String aValore)     {setString("COD_UFFICIO_APPARTENENZA", aValore);}
  public void setNote(String aValore)           {setString("NOTE", aValore);}
  public void setFlagCancellato(String aValore)       {setString("FLAG_CANCELLATO", aValore);}
  public void setCodiceFiscale(String aValore)      {setString("COD_FISCALE", aValore);}
  public void setProvincia(String aValore)        {setString("PROVINCIA", aValore);}
  public void setCap(String aValore)            {setString("CAP", aValore);}
  public void setFlagVisualizza(BigDecimal aValore)     {setBigDecimal("FLAG_VISUALIZZA", aValore);}
  public void setIdAvvocatoStandard(BigDecimal aValore)   {setBigDecimal("ID_AVVOCATO_STANDARD", aValore);}


  public GenericModel getModel() throws DAOException
  {
    return new AvvocatoModel(
        getIdAvvocato(),
        getCognome(), 
        getNome(),
        getForo(),
        getIndirizzo(),
        getTelefono(),
        getFax(),
        getEMail(),
        getCodLuogoNascita(),
        getCodComuneResidenza(),
        "", "",
        getDataNascita(),
        getCodOperatoreInserimento(),
        getDataInserimento(),
        getCodOperatoreAggiornamento(),
        getDataAggiornamento(),
        "",
        getCodUfficioInserimento(),
        getCodUfficioAggiornamento(),
        this.getDataSospensione(),
        this.getDataRadiazione(),
        this.getCodNonAttivita(),
        this.getCodUffAppartenenza(),
        getNote(),
        getFlagCancellato(),
        "",
        this.getCodiceFiscale(),
        this.getProvincia(),
        this.getCap(),
        this.getFlagVisualizza(),
        this.getIdAvvocatoStandard()
        );
  }


  public void setDAOFromModel(AvvocatoModel aModel) throws DAOException
  {
     setIdAvvocato( aModel.getIdAvvocato() );
     setCognome( aModel.getCognome() );
     setNome( aModel.getNome() );
     setForo( aModel.getForo() );
     setIndirizzo( aModel.getIndirizzo() );
     setTelefono( aModel.getTelefono() );
     setFax( aModel.getFax() );
     setEMail( aModel.getEMail() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setCodLuogoNascita(aModel.getCodLuogoNascita() );
     setCodComuneResidenza(aModel.getCodComuneResidenza() );
     setDataNascita(aModel.getDataNascita() );
     setDataSospensione(aModel.getDataSospensione() );
     setDataRadiazione(aModel.getDataRadiazione() );
     setCodNonAttivita (aModel.getCodNonAttivita() );
     setCodUffAppartenenza(aModel.getCodUffAppartenenza() );
     setNote(aModel.getNote());
     setFlagCancellato(aModel.getFlagCancellato());
     setCodiceFiscale(aModel.getCodiceFiscale());
     setProvincia(aModel.getProvincia());
     setCap(aModel.getCap());
     setFlagVisualizza(aModel.getFlagVisualizza());
     setIdAvvocatoStandard(aModel.getIdAvvocatoStandard());  
     
     
  }

  public void setDAOFromModelForUpdate(AvvocatoModel aModel) throws DAOException
  {
    setCognome( aModel.getCognome() );
    setNome( aModel.getNome() );
    setForo( aModel.getForo() );
    setIndirizzo( aModel.getIndirizzo() );
    setTelefono( aModel.getTelefono() );
    setFax( aModel.getFax() );
    setEMail( aModel.getEMail() );
    setCodComuneResidenza(aModel.getCodComuneResidenza() );
    setCodLuogoNascita(aModel.getCodLuogoNascita() );
    setDataNascita(aModel.getDataNascita() );

    setDataSospensione(aModel.getDataSospensione() );
    setDataRadiazione(aModel.getDataRadiazione() );
    setCodNonAttivita (aModel.getCodNonAttivita() );

    setNote(aModel.getNote());
    setFlagCancellato(aModel.getFlagCancellato());
    setCodUffAppartenenza(aModel.getCodUffAppartenenza() );

    setCodiceFiscale( aModel.getCodiceFiscale() );
    setProvincia( aModel.getProvincia() );
    setCap( aModel.getCap() );

    setFlagVisualizza( aModel.getFlagVisualizza() );          

    
    setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento         ( aModel.getDataAggiornamento() );
    
    selCondizioneUpdate(aModel.getIdAvvocato());
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_AVVOCATO = " + key );
  }


  public void     setCondizione(AvvocatoModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if (aModel.getIdAvvocato().doubleValue() != 0 )
    {
      if (lInserito)
        lCondizioni +=" AND ID_AVVOCATO= "+ aModel.getIdAvvocato();
      else
      {
        lCondizioni=" ID_AVVOCATO= "+ aModel.getIdAvvocato();
        lInserito=true;
      }
    }

    if ( lInserito ) setCondition(lCondizioni);
  }


  public void selCondizioneUpdate(BigDecimal key) {
    setCondition(" ID_AVVOCATO = " + key);
  }

}
