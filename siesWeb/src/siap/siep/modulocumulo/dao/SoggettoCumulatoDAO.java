package siap.siep.modulocumulo.dao;

/**
* <p>Title: SoggettoCumulatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella SoggettoCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

import siap.siep.modulocumulo.model.SoggettoCumulatoModel;


public class SoggettoCumulatoDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public SoggettoCumulatoDAO (Connection con) {
    super(con);
    setTable("SOGGETTO_CUMULATO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_SOGGETTO_CUMULATO","SOGGETTO_CUMULATO_SEQ");

    //setField("ID_SOGGETTO_CUMULATO", BIG_DECIMAL);
    setField("COGNOME"                    , STRING);
    setField("NOME"                       , STRING);
    setField("SESSO"                      , STRING);
    setField("DATA_NASCITA"               , DATE);
    setField("DATA_NASCITA_PRESUNTA"      , STRING);
    setField("ANNO_NASCITA"               , BIG_DECIMAL);
    setField("MESE_NASCITA"               , BIG_DECIMAL);
    setField("COD_COMUNE_NASCITA"         , STRING);
    setField("COD_PROVINCIA_NASCITA"      , STRING);
    setField("COD_STATO_NASCITA"          , STRING);
    setField("DESC_COMUNE_NASCITA_ESTERO" , STRING);
    setField("NAZIONALITA"                , STRING);
    setField("PATERNITA"                  , STRING);
    setField("COGNOME_MADRE"              , STRING);
    setField("NOME_MADRE"                 , STRING);
    setField("COD_FISCALE"                , STRING);
    setField("ATTO_NASCITA"               , STRING);
    setField("COD_AFIS"                   , STRING);
    setField("COD_COMUNE_CASELLARIO"      , STRING);
    setField("NOTE"                       , STRING);
    setField("KEY_SOGG_NSC"               , BIG_DECIMAL);
    
    setField("TIT_ID_TITOLO_CUMULATO"     , BIG_DECIMAL);
    setField("FLAG_STATO"                 , STRING);
    setField("MOTIVO_MODIFICA"            , STRING);
    setField("ID_SOGGETTO_ORIGINE"        , BIG_DECIMAL);

    setField("COD_OPERATORE_INSERIMENTO"  , STRING);
    setField("DATA_INSERIMENTO"           , DATE);
    setField("COD_UFFICIO_INSERIMENTO"    , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO"         , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"  , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdSoggettoCumulato()         throws DAOException  { return getBigDecimal ("ID_SOGGETTO_CUMULATO"       ); } 
  public  String      getCognome()                    throws DAOException  { return getString     ("COGNOME"                    ); } 
  public  String      getNome()                       throws DAOException  { return getString     ("NOME"                       ); } 
  public  String      getSesso()                      throws DAOException  { return getString     ("SESSO"                      ); } 
  public  Date        getDataNascita()                throws DAOException  { return getDate       ("DATA_NASCITA"               ); } 
  public  String      getDataNascitaPresunta()        throws DAOException  { return getString     ("DATA_NASCITA_PRESUNTA"      ); } 
  public  BigDecimal  getAnnoNascita()                throws DAOException  { return getBigDecimal ("ANNO_NASCITA"               ); } 
  public  BigDecimal  getMeseNascita()                throws DAOException  { return getBigDecimal ("MESE_NASCITA"               ); } 
  public  String      getCodComuneNascita()           throws DAOException  { return getString     ("COD_COMUNE_NASCITA"         ); } 
  public  String      getCodProvinciaNascita()        throws DAOException  { return getString     ("COD_PROVINCIA_NASCITA"      ); } 
  public  String      getCodStatoNascita()            throws DAOException  { return getString     ("COD_STATO_NASCITA"          ); } 
  public  String      getDescComuneNascitaEstero()    throws DAOException  { return getString     ("DESC_COMUNE_NASCITA_ESTERO" ); } 
  public  String      getNazionalita()                throws DAOException  { return getString     ("NAZIONALITA"                ); } 
  public  String      getPaternita()                  throws DAOException  { return getString     ("PATERNITA"                  ); } 
  public  String      getCognomeMadre()               throws DAOException  { return getString     ("COGNOME_MADRE"              ); } 
  public  String      getNomeMadre()                  throws DAOException  { return getString     ("NOME_MADRE"                 ); } 
  public  String      getCodFiscale()                 throws DAOException  { return getString     ("COD_FISCALE"                ); } 
  public  String      getAttoNascita()                throws DAOException  { return getString     ("ATTO_NASCITA"               ); } 
  public  String      getCodAfis()                    throws DAOException  { return getString     ("COD_AFIS"                   ); } 
  public  String      getCodComuneCasellario()        throws DAOException  { return getString     ("COD_COMUNE_CASELLARIO"      ); } 
  public  String      getNote()                       throws DAOException  { return getString     ("NOTE"                       ); } 
  public  BigDecimal  getKeySoggNsc()                 throws DAOException  { return getBigDecimal ("KEY_SOGG_NSC"               ); } 
  
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ); } 
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                 ); } 
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"            ); } 
  public  BigDecimal  getIdSoggettoOrigine()          throws DAOException  { return getBigDecimal ("ID_SOGGETTO_ORIGINE"    ); } 

  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdSoggettoCumulato         (BigDecimal  aValore )   { setBigDecimal ("ID_SOGGETTO_CUMULATO"       , aValore); } 
  public void  setCognome                    (String      aValore )   { setString     ("COGNOME"                    , aValore); } 
  public void  setNome                       (String      aValore )   { setString     ("NOME"                       , aValore); } 
  public void  setSesso                      (String      aValore )   { setString     ("SESSO"                      , aValore); } 
  public void  setDataNascita                (Date        aValore )   { setDate       ("DATA_NASCITA"               , aValore); } 
  public void  setDataNascitaPresunta        (String      aValore )   { setString     ("DATA_NASCITA_PRESUNTA"      , aValore); } 
  public void  setAnnoNascita                (BigDecimal  aValore )   { setBigDecimal ("ANNO_NASCITA"               , aValore); } 
  public void  setMeseNascita                (BigDecimal  aValore )   { setBigDecimal ("MESE_NASCITA"               , aValore); } 
  public void  setCodComuneNascita           (String      aValore )   { setString     ("COD_COMUNE_NASCITA"         , aValore); } 
  public void  setCodProvinciaNascita        (String      aValore )   { setString     ("COD_PROVINCIA_NASCITA"      , aValore); } 
  public void  setCodStatoNascita            (String      aValore )   { setString     ("COD_STATO_NASCITA"          , aValore); } 
  public void  setDescComuneNascitaEstero    (String      aValore )   { setString     ("DESC_COMUNE_NASCITA_ESTERO" , aValore); } 
  public void  setNazionalita                (String      aValore )   { setString     ("NAZIONALITA"                , aValore); } 
  public void  setPaternita                  (String      aValore )   { setString     ("PATERNITA"                  , aValore); } 
  public void  setCognomeMadre               (String      aValore )   { setString     ("COGNOME_MADRE"              , aValore); } 
  public void  setNomeMadre                  (String      aValore )   { setString     ("NOME_MADRE"                 , aValore); } 
  public void  setCodFiscale                 (String      aValore )   { setString     ("COD_FISCALE"                , aValore); } 
  public void  setAttoNascita                (String      aValore )   { setString     ("ATTO_NASCITA"               , aValore); } 
  public void  setCodAfis                    (String      aValore )   { setString     ("COD_AFIS"                   , aValore); } 
  public void  setCodComuneCasellario        (String      aValore )   { setString     ("COD_COMUNE_CASELLARIO"      , aValore); } 
  public void  setNote                       (String      aValore )   { setString     ("NOTE"                       , aValore); } 
  public void  setKeySoggNsc                 (BigDecimal  aValore )   { setBigDecimal ("KEY_SOGG_NSC"               , aValore); } 
  
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"     , aValore); } 
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                 , aValore); } 
  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"            , aValore); } 
  public void  setIdSoggettoOrigine          (BigDecimal  aValore )   { setBigDecimal ("ID_SOGGETTO_ORIGINE"        , aValore); } 

  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new SoggettoCumulatoModel(  
      getIdSoggettoCumulato() , 
      getCognome() , 
      getNome() , 
      getSesso() , 
      getDataNascita() , 
      getDataNascitaPresunta() , 
      getAnnoNascita() , 
      getMeseNascita() , 
      getCodComuneNascita() , 
 "",
      getCodProvinciaNascita() , 
 "",
      getCodStatoNascita() , 
 "",
      getDescComuneNascitaEstero() , 
      getNazionalita() , 
 "",     
      getPaternita() , 
      getCognomeMadre() , 
      getNomeMadre() , 
      getCodFiscale() , 
      getAttoNascita() , 
      getCodAfis() , 
      getCodComuneCasellario() , 
 "",
      getNote() , 
      getKeySoggNsc() , 
      
      getTitIdTitoloCumulato() , 
      getFlagStato() , 
      getMotivoModifica() , 
      getIdSoggettoOrigine() ,
      
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento()      
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(SoggettoCumulatoModel aModel) throws DAOException {
    setIdSoggettoCumulato         ( aModel.getIdSoggettoCumulato()        );  
    setCognome                    ( aModel.getCognome()                   );  
    setNome                       ( aModel.getNome()                      );  
    setSesso                      ( aModel.getSesso()                     );  
    setDataNascita                ( aModel.getDataNascita()               );  
    setDataNascitaPresunta        ( aModel.getDataNascitaPresunta()       );  
    setAnnoNascita                ( aModel.getAnnoNascita()               );  
    setMeseNascita                ( aModel.getMeseNascita()               );  
    setCodComuneNascita           ( aModel.getCodComuneNascita()          );  
    setCodProvinciaNascita        ( aModel.getCodProvinciaNascita()       );  
    setCodStatoNascita            ( aModel.getCodStatoNascita()           );  
    setDescComuneNascitaEstero    ( aModel.getDescComuneNascitaEstero()   );  
    setNazionalita                ( aModel.getNazionalita()               );  
    setPaternita                  ( aModel.getPaternita()                 );  
    setCognomeMadre               ( aModel.getCognomeMadre()              );  
    setNomeMadre                  ( aModel.getNomeMadre()                 );  
    setCodFiscale                 ( aModel.getCodFiscale()                );  
    setAttoNascita                ( aModel.getAttoNascita()               );  
    setCodAfis                    ( aModel.getCodAfis()                   );  
    setCodComuneCasellario        ( aModel.getCodComuneCasellario()       );  
    setNote                       ( aModel.getNote()                      );  
    setKeySoggNsc                 ( aModel.getKeySoggNsc()                );  
    
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setIdSoggettoOrigine          ( aModel.getIdSoggettoOrigine()         );  

    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(SoggettoCumulatoModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdSoggettoCumulato() != null ) { 
      lCondizioni += " and ID_SOGGETTO_CUMULATO = " + aModel.getIdSoggettoCumulato() + ""; 
    } 
    if (aModel.getCognome() != null && aModel.getCognome().length() > 0) { 
      lCondizioni += " and COGNOME = '" + aModel.getCognome() + "' "; 
    } 
    if (aModel.getNome() != null && aModel.getNome().length() > 0) { 
      lCondizioni += " and NOME = '" + aModel.getNome() + "' "; 
    } 
    if (aModel.getSesso() != null && aModel.getSesso().length() > 0) { 
      lCondizioni += " and SESSO = '" + aModel.getSesso() + "' "; 
    } 
    if (aModel.getDataNascita() != null ) { 
      lCondizioni += " and to_char(DATA_NASCITA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataNascita(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataNascitaPresunta() != null && aModel.getDataNascitaPresunta().length() > 0) { 
      lCondizioni += " and DATA_NASCITA_PRESUNTA = '" + aModel.getDataNascitaPresunta() + "' "; 
    } 
    if (aModel.getAnnoNascita() != null ) { 
      lCondizioni += " and ANNO_NASCITA = " + aModel.getAnnoNascita() + ""; 
    } 
    if (aModel.getMeseNascita() != null ) { 
      lCondizioni += " and MESE_NASCITA = " + aModel.getMeseNascita() + ""; 
    } 
    if (aModel.getCodComuneNascita() != null && aModel.getCodComuneNascita().length() > 0) { 
      lCondizioni += " and COD_COMUNE_NASCITA = '" + aModel.getCodComuneNascita() + "' "; 
    } 
    if (aModel.getCodProvinciaNascita() != null && aModel.getCodProvinciaNascita().length() > 0) { 
      lCondizioni += " and COD_PROVINCIA_NASCITA = '" + aModel.getCodProvinciaNascita() + "' "; 
    } 
    if (aModel.getCodStatoNascita() != null && aModel.getCodStatoNascita().length() > 0) { 
      lCondizioni += " and COD_STATO_NASCITA = '" + aModel.getCodStatoNascita() + "' "; 
    } 
    if (aModel.getDescComuneNascitaEstero() != null && aModel.getDescComuneNascitaEstero().length() > 0) { 
      lCondizioni += " and DESC_COMUNE_NASCITA_ESTERO = '" + aModel.getDescComuneNascitaEstero() + "' "; 
    } 
    if (aModel.getNazionalita() != null && aModel.getNazionalita().length() > 0) { 
      lCondizioni += " and NAZIONALITA = '" + aModel.getNazionalita() + "' "; 
    } 
    if (aModel.getPaternita() != null && aModel.getPaternita().length() > 0) { 
      lCondizioni += " and PATERNITA = '" + aModel.getPaternita() + "' "; 
    } 
    if (aModel.getCognomeMadre() != null && aModel.getCognomeMadre().length() > 0) { 
      lCondizioni += " and COGNOME_MADRE = '" + aModel.getCognomeMadre() + "' "; 
    } 
    if (aModel.getNomeMadre() != null && aModel.getNomeMadre().length() > 0) { 
      lCondizioni += " and NOME_MADRE = '" + aModel.getNomeMadre() + "' "; 
    } 
    if (aModel.getCodFiscale() != null && aModel.getCodFiscale().length() > 0) { 
      lCondizioni += " and COD_FISCALE = '" + aModel.getCodFiscale() + "' "; 
    } 
    if (aModel.getAttoNascita() != null && aModel.getAttoNascita().length() > 0) { 
      lCondizioni += " and ATTO_NASCITA = '" + aModel.getAttoNascita() + "' "; 
    } 
    if (aModel.getCodAfis() != null && aModel.getCodAfis().length() > 0) { 
      lCondizioni += " and COD_AFIS = '" + aModel.getCodAfis() + "' "; 
    } 
    if (aModel.getCodComuneCasellario() != null && aModel.getCodComuneCasellario().length() > 0) { 
      lCondizioni += " and COD_COMUNE_CASELLARIO = '" + aModel.getCodComuneCasellario() + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    if (aModel.getKeySoggNsc() != null ) { 
      lCondizioni += " and KEY_SOGG_NSC = " + aModel.getKeySoggNsc() + ""; 
    } 
    
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getIdSoggettoOrigine() != null ) { 
      lCondizioni += " and ID_SOGGETTO_ORIGINE = " + aModel.getIdSoggettoOrigine() + ""; 
    }     
    
    if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' "; 
    } 
    if (aModel.getDataInserimento() != null ) { 
      lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInserimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' "; 
    } 
    if (aModel.getCodOperatoreAggiornamento() != null && aModel.getCodOperatoreAggiornamento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento() + "' "; 
    } 
    if (aModel.getDataAggiornamento() != null ) { 
      lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataAggiornamento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' "; 
    } 

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni); 
  }


  /***************************************************************************** 
   * Imposta la condizione di where per l'operazione di update puntuale 
   * si entra sempre in chiave 
   * @param key 
   ****************************************************************************/ 
  public void selCondizioneUpdate( BigDecimal aIdSoggettoCumulato) {
    String lCondizioni = new String();

    lCondizioni += " and ID_SOGGETTO_CUMULATO = " + aIdSoggettoCumulato;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }
  
  public void selCondizioneUpdateByIdTitolo( BigDecimal aIdTitolo) {
    String lCondizioni = new String();

    lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }


  /***************************************************************************** 
   * Imposta la condizione di order by per la ricerca  
   *  
   *****************************************************************************/ 
  public void setOrderBy() { 
    String orderBy = ""; 
    //======================================================================= 
    // Lasciare orderBy="" se non si vuole scegliere un ordinamento, 
    // altrimenti elencare i campi separati da virgola 
    // n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente 
    //======================================================================= 

    setOrder(orderBy); 
  } 

}
