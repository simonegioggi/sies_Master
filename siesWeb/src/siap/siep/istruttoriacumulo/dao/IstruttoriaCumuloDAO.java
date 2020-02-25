package siap.siep.istruttoriacumulo.dao;

/**
* <p>Title: IstruttoriaCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import f3b.util.DateUtils;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;

public class IstruttoriaCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public IstruttoriaCumuloDAO (Connection con) {
    super(con);
    setTable("ISTRUTTORIA_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_ISTRUTTORIA_CUMULO","ISTR_CUM_SEQ");

    //setField("ID_ISTRUTTORIA_CUMULO", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP"  , BIG_DECIMAL);
    setField("EVE_ID_EVENTO_ISTR"         , BIG_DECIMAL);
    setField("EVE_ID_EVENTO_PROV"         , BIG_DECIMAL);
    setField("DATA_APERTURA"              , DATE);
    setField("DATA_CHIUSURA"              , DATE);
    setField("ANNO_PROTOCOLLO"            , BIG_DECIMAL);
    setField("NUM_PROTOCOLLO"             , BIG_DECIMAL);
    setField("CHIAVE_UFFICIO"             , STRING);
    setField("NOTE"                       , STRING);
    setField("FLAG_STATO"                 , STRING);
    setField("ORDINAMENTO_TITOLI"         , STRING);
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
  public  BigDecimal  getIdIstruttoriaCumulo()        throws DAOException  { return getBigDecimal ("ID_ISTRUTTORIA_CUMULO"      ); } 
  public  BigDecimal  getFasSieIdFascicoloSiep()      throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  ); } 
  public  BigDecimal  getEveIdEventoIstr()            throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO_ISTR"         ); } 
  public  BigDecimal  getEveIdEventoProv()            throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO_PROV"         ); } 
  public  Date        getDataApertura()               throws DAOException  { return getDate       ("DATA_APERTURA"              ); } 
  public  Date        getDataChiusura()               throws DAOException  { return getDate       ("DATA_CHIUSURA"              ); } 
  public  BigDecimal  getAnnoProtocollo()             throws DAOException  { return getBigDecimal ("ANNO_PROTOCOLLO"            ); } 
  public  BigDecimal  getNumProtocollo()              throws DAOException  { return getBigDecimal ("NUM_PROTOCOLLO"             ); }
  public  String      getChiaveUfficio()              throws DAOException  { return getString     ("CHIAVE_UFFICIO"             ); }
  public  String      getNote()                       throws DAOException  { return getString     ("NOTE"                       ); } 
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                 ); } 
  public  String      getOrdinamentoTitoli()          throws DAOException  { return getString     ("ORDINAMENTO_TITOLI"         ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdIstruttoriaCumulo        (BigDecimal  aValore )   { setBigDecimal ("ID_ISTRUTTORIA_CUMULO"      , aValore); } 
  public void  setFasSieIdFascicoloSiep      (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  , aValore); } 
  public void  setEveIdEventoIstr            (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO_ISTR"         , aValore); } 
  public void  setEveIdEventoProv            (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO_PROV"         , aValore); } 
  public void  setDataApertura               (Date        aValore )   { setDate       ("DATA_APERTURA"              , aValore); } 
  public void  setDataChiusura               (Date        aValore )   { setDate       ("DATA_CHIUSURA"              , aValore); } 
  public void  setAnnoProtocollo             (BigDecimal  aValore )   { setBigDecimal ("ANNO_PROTOCOLLO"            , aValore); } 
  public void  setNumProtocollo              (BigDecimal  aValore )   { setBigDecimal ("NUM_PROTOCOLLO"             , aValore); }
  public void  setChiaveUfficio              (String      aValore )   { setString     ("CHIAVE_UFFICIO"             , aValore); }
  public void  setNote                       (String      aValore )   { setString     ("NOTE"                       , aValore); } 
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                 , aValore); } 
  public void  setOrdinamentoTitoli          (String      aValore )   { setString     ("ORDINAMENTO_TITOLI"         , aValore); } 
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
    return new IstruttoriaCumuloModel(  
      getIdIstruttoriaCumulo() , 
      getFasSieIdFascicoloSiep() , 
      getEveIdEventoIstr() , 
      getEveIdEventoProv() , 
      getDataApertura() , 
      getDataChiusura() ,
      getAnnoProtocollo(),
      getNumProtocollo(),
      getChiaveUfficio(),
      getNote() , 
      getFlagStato() , 
      getOrdinamentoTitoli(),
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
  public void setDAOFromModel(IstruttoriaCumuloModel aModel) throws DAOException {
    setIdIstruttoriaCumulo        ( aModel.getIdIstruttoriaCumulo()       );  
    setFasSieIdFascicoloSiep      ( aModel.getFasSieIdFascicoloSiep()     );  
    setEveIdEventoIstr            ( aModel.getEveIdEventoIstr()           );  
    setEveIdEventoProv            ( aModel.getEveIdEventoProv()           );  
    setDataApertura               ( aModel.getDataApertura()              );  
    setDataChiusura               ( aModel.getDataChiusura()              );
    setAnnoProtocollo             ( aModel.getAnnoProtocollo()            );
    setNumProtocollo              ( aModel.getNumProtocollo()             );
    setChiaveUfficio              ( aModel.getChiaveUfficio()             );
    setNote                       ( aModel.getNote()                      );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setOrdinamentoTitoli          ( aModel.getOrdinamentoTitoli()         );  
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
  public void setCondizioni(IstruttoriaCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdIstruttoriaCumulo() != null ) { 
      lCondizioni += " and ID_ISTRUTTORIA_CUMULO = " + aModel.getIdIstruttoriaCumulo() + ""; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    if (aModel.getEveIdEventoIstr() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO_ISTR = " + aModel.getEveIdEventoIstr() + ""; 
    } 
    if (aModel.getEveIdEventoProv() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO_PROV = " + aModel.getEveIdEventoProv() + ""; 
    } 
    if (aModel.getDataApertura() != null ) { 
      lCondizioni += " and to_char(DATA_APERTURA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataApertura(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataChiusura() != null ) { 
      lCondizioni += " and to_char(DATA_CHIUSURA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataChiusura(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getAnnoProtocollo() != null ) { 
      lCondizioni += " and ANNO_PROTOCOLLO = " + aModel.getAnnoProtocollo() + ""; 
    } 
    if (aModel.getNumProtocollo() != null ) { 
      lCondizioni += " and NUM_PROTOCOLLO = " + aModel.getNumProtocollo() + ""; 
    } 
    if (aModel.getChiaveUfficio() != null && aModel.getChiaveUfficio().length() > 0) { 
      lCondizioni += " and CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getOrdinamentoTitoli() != null && aModel.getOrdinamentoTitoli().length() > 0) { 
      lCondizioni += " and ORDINAMENTO_TITOLI = '" + aModel.getOrdinamentoTitoli() + "' "; 
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
    if (aModel.getAnnoProtocolloIniziale() != null ) { 
        if (aModel.getNumProtocolloIniziale() == null ) {
        	lCondizioni += " and ANNO_PROTOCOLLO >= " + aModel.getAnnoProtocolloIniziale() + ""; 
        } else {
    	  lCondizioni += " and ((ANNO_PROTOCOLLO  = " + aModel.getAnnoProtocolloIniziale() + " and " +
    	  						 "NUM_PROTOCOLLO >= " + aModel.getNumProtocolloIniziale() + ") " +
    	  				    " or ANNO_PROTOCOLLO  > " + aModel.getAnnoProtocolloIniziale() +" )"; 
        } 
    }
    if (aModel.getAnnoProtocolloFinale() != null ) { 
        if (aModel.getNumProtocolloFinale() == null ) {
        	lCondizioni += " and ANNO_PROTOCOLLO <= " + aModel.getAnnoProtocolloFinale() + ""; 
        } else {
    	  lCondizioni += " and ((ANNO_PROTOCOLLO  = " + aModel.getAnnoProtocolloFinale() + " and " +
    	  						 "NUM_PROTOCOLLO <= " + aModel.getNumProtocolloFinale() + ") " +
    	  				    " or ANNO_PROTOCOLLO  < " + aModel.getAnnoProtocolloFinale() +" )"; 
      }
    }
    if (aModel.getDataIscrizioneIniziale() != null ) {
        lCondizioni += " and DATA_INSERIMENTO >= TO_DATE ('" + DateUtils.getDateToString(aModel.getDataIscrizioneIniziale(),"ddMMyyyy") +"', 'DDMMYYYY')"; 
    } 
    if (aModel.getDataIscrizioneFinale() != null ) { 
        lCondizioni += " and DATA_INSERIMENTO <= TO_DATE ('" + DateUtils.getDateToString(aModel.getDataIscrizioneFinale(),"ddMMyyyy") +"', 'DDMMYYYY')"; 
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
  public void selCondizioneUpdate( BigDecimal aIdIstruttoriaCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoriaCumulo;
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
    String orderBy = " DATA_APERTURA ASC "; 
    //======================================================================= 
    // Lasciare orderBy="" se non si vuole scegliere un ordinamento, 
    // altrimenti elencare i campi separati da virgola 
    // n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente 
    //======================================================================= 

    setOrder(orderBy); 
  } 

}
