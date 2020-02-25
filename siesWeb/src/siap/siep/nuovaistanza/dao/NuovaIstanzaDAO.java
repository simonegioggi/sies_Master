package siap.siep.nuovaistanza.dao;

/**
* <p>Title: NuovaIstanzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella NuovaIstanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class NuovaIstanzaDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public NuovaIstanzaDAO (Connection con) {
    super(con);
    setTable("NUOVA_ISTANZA");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_NUOVA_ISTANZA","NUO_IST_SEQ");
    setFieldKey("ID_NUOVA_ISTANZA", BIG_DECIMAL);
    setField("COD_CONTENUTO"                , STRING);
    setField("DATA_ISTANZA"                 , DATE);
    setField("NOTE"                         , STRING);
    setField("FLAG_PRESDEP"                 , STRING);
    setField("SOGG_PRESENTANTE"             , STRING);
    setField("SOGG_PRESENTANTE_IDENTIFICATO", STRING);
    setField("AVV_ID_AVVOCATO_PRESENTANTE"  , BIG_DECIMAL);
    setField("COD_AUTORITA_MITTENTE"	      , STRING);
    setField("COD_SEDE_MITTENTE"            , STRING);
    setField("DESCR_MITTENTE"		            , STRING);
    setField("AVV_ID_AVVOCATO"              , BIG_DECIMAL);
    setField("COD_ESITO"                    , STRING);
    setField("ANNO_REGISTRO"                , BIG_DECIMAL);
    setField("PROGR_REGISTRO"               , BIG_DECIMAL);
    setField("COD_TIPO_UFFICIO_DESTINATARIO", STRING);
    setField("COD_LUOGO_DESTINATARIO"       , STRING);
    setField("COD_UFFICIO_DESTINATARIO"     , STRING);
    setField("COD_STATO_ISTANZA"            , STRING);
    setField("COD_OPERATORE_INSERIMENTO"    , STRING);
    setField("DATA_INSERIMENTO"             , DATE);
    setField("COD_UFFICIO_INSERIMENTO"      , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO"  , STRING);
    setField("DATA_AGGIORNAMENTO"           , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"    , STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP"    , BIG_DECIMAL);
    setField("EVE_ID_EVENTO"                , BIG_DECIMAL);
    setField("DATA_NOTIFICA_AVVOCATO"       , DATE);
    setField("TIPO_AVVOCATO"                , STRING);
    setField("DATA_INOLTRO_PM"				, DATE);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdNuovaIstanza()               throws DAOException  { return getBigDecimal ("ID_NUOVA_ISTANZA"             ); } 
  public  String      getCodContenuto()                 throws DAOException  { return getString     ("COD_CONTENUTO"                ); } 
  public  Date        getDataIstanza()                  throws DAOException  { return getDate       ("DATA_ISTANZA"                 ); } 
  public  String      getNote()                         throws DAOException  { return getString     ("NOTE"                         ); } 
  public  String      getFlagPresdep()                  throws DAOException  { return getString     ("FLAG_PRESDEP"                 ); } 
  public  String      getSoggPresentante()              throws DAOException  { return getString     ("SOGG_PRESENTANTE"             ); } 
  public  String      getSoggPresentanteIdentificato()  throws DAOException  { return getString     ("SOGG_PRESENTANTE_IDENTIFICATO"); } 
  public  BigDecimal  getAvvIdAvvocatoPresentante()     throws DAOException  { return getBigDecimal ("AVV_ID_AVVOCATO_PRESENTANTE"  ); } 
  public  String      getCodAutoritaMittente()          throws DAOException  { return getString     ("COD_AUTORITA_MITTENTE"        ); } 
  public  String      getCodSedeMittente()              throws DAOException  { return getString     ("COD_SEDE_MITTENTE"            ); } 
  public  String      getDescrMittente()              	throws DAOException  { return getString     ("DESCR_MITTENTE"            		); } 
  public  BigDecimal  getAvvIdAvvocato()                throws DAOException  { return getBigDecimal ("AVV_ID_AVVOCATO"              ); } 
  public  String      getCodEsito()                     throws DAOException  { return getString     ("COD_ESITO"                    ); } 
  public  BigDecimal  getAnnoRegistro()                 throws DAOException  { return getBigDecimal ("ANNO_REGISTRO"                ); } 
  public  BigDecimal  getProgrRegistro()                throws DAOException  { return getBigDecimal ("PROGR_REGISTRO"               ); } 
  public  String      getCodTipoUfficioDestinatario()   throws DAOException  { return getString     ("COD_TIPO_UFFICIO_DESTINATARIO"); } 
  public  String      getCodLuogoDestinatario()         throws DAOException  { return getString     ("COD_LUOGO_DESTINATARIO"       ); } 
  public  String      getCodUfficioDestinatario()       throws DAOException  { return getString     ("COD_UFFICIO_DESTINATARIO"     ); } 
  public  String      getCodStatoIstanza()              throws DAOException  { return getString     ("COD_STATO_ISTANZA"            ); } 
  public  String      getCodOperatoreInserimento()      throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"    ); } 
  public  Date        getDataInserimento()              throws DAOException  { return getDate       ("DATA_INSERIMENTO"             ); } 
  public  String      getCodUfficioInserimento()        throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"      ); } 
  public  String      getCodOperatoreAggiornamento()    throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"  ); } 
  public  Date        getDataAggiornamento()            throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"           ); } 
  public  String      getCodUfficioAggiornamento()      throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"    ); } 
  public  BigDecimal  getFasSieIdFascicoloSiep()        throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"    ); } 
  public  BigDecimal  getEveIdEvento()                  throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"                ); } 

  public  Date getDataNotificaAvvocato()                 throws DAOException  { return getDate ("DATA_NOTIFICA_AVVOCATO"                ); } 
  public  String  getTipoAvvocato()                      throws DAOException  { return getString ("TIPO_AVVOCATO"                ); } 

  public  Date getDataInoltroPM()						throws DAOException  { return getDate ("DATA_INOLTRO_PM"                ); } 
  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdNuovaIstanza               (BigDecimal  aValore )   { setBigDecimal ("ID_NUOVA_ISTANZA"             , aValore); } 
  public void  setCodContenuto                 (String      aValore )   { setString     ("COD_CONTENUTO"                , aValore); } 
  public void  setDataIstanza                  (Date        aValore )   { setDate       ("DATA_ISTANZA"                 , aValore); } 
  public void  setNote                         (String      aValore )   { setString     ("NOTE"                         , aValore); } 
  public void  setFlagPresdep                  (String      aValore )   { setString     ("FLAG_PRESDEP"                 , aValore); } 
  public void  setSoggPresentante              (String      aValore )   { setString     ("SOGG_PRESENTANTE"             , aValore); } 
  public void  setSoggPresentanteIdentificato  (String      aValore )   { setString     ("SOGG_PRESENTANTE_IDENTIFICATO", aValore); } 
  public void  setAvvIdAvvocatoPresentante     (BigDecimal  aValore )   { setBigDecimal ("AVV_ID_AVVOCATO_PRESENTANTE"  , aValore); } 
  public void  setCodAutoritaMittente          (String      aValore )   { setString     ("COD_AUTORITA_MITTENTE"        , aValore); } 
  public void  setCodSedeMittente              (String      aValore )   { setString     ("COD_SEDE_MITTENTE"            , aValore); } 
  public void  setDescrMittente		             (String      aValore )   { setString     ("DESCR_MITTENTE"            , aValore); } 
  public void  setAvvIdAvvocato                (BigDecimal  aValore )   { setBigDecimal ("AVV_ID_AVVOCATO"              , aValore); } 
  public void  setCodEsito                     (String      aValore )   { setString     ("COD_ESITO"                    , aValore); } 
  public void  setAnnoRegistro                 (BigDecimal  aValore )   { setBigDecimal ("ANNO_REGISTRO"                , aValore); } 
  public void  setProgrRegistro                (BigDecimal  aValore )   { setBigDecimal ("PROGR_REGISTRO"               , aValore); } 
  public void  setCodTipoUfficioDestinatario   (String      aValore )   { setString     ("COD_TIPO_UFFICIO_DESTINATARIO", aValore); } 
  public void  setCodLuogoDestinatario         (String      aValore )   { setString     ("COD_LUOGO_DESTINATARIO"       , aValore); } 
  public void  setCodUfficioDestinatario       (String      aValore )   { setString     ("COD_UFFICIO_DESTINATARIO"     , aValore); } 
  public void  setCodStatoIstanza              (String      aValore )   { setString     ("COD_STATO_ISTANZA"            , aValore); } 
  public void  setCodOperatoreInserimento      (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"    , aValore); } 
  public void  setDataInserimento              (Date        aValore )   { setDate       ("DATA_INSERIMENTO"             , aValore); } 
  public void  setCodUfficioInserimento        (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"      , aValore); } 
  public void  setCodOperatoreAggiornamento    (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"  , aValore); } 
  public void  setDataAggiornamento            (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"           , aValore); } 
  public void  setCodUfficioAggiornamento      (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"    , aValore); } 
  public void  setFasSieIdFascicoloSiep        (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"    , aValore); } 
  public void  setEveIdEvento                  (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"                , aValore); } 
  public void  setDataNotificaAvvocato            (Date        aValore )   { setDate       ("DATA_NOTIFICA_AVVOCATO"           , aValore); } 
  public void  setTipoAvvocato      (String      aValore )   { setString     ("TIPO_AVVOCATO"    , aValore); } 

  public void  setDataInoltroPM            (Date        aValore )   { setDate       ("DATA_INOLTRO_PM"           , aValore); } 

  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new NuovaIstanzaModel(  
      getIdNuovaIstanza() , 
      getCodContenuto() , 
      "",
      getDataIstanza() , 
      getNote() , 
      getFlagPresdep() , 
      getSoggPresentante() , 
      getSoggPresentanteIdentificato() , 
      getAvvIdAvvocatoPresentante() , 
      "",
      getCodAutoritaMittente() , 
      "",
      getCodSedeMittente() , 
      "",
 			getDescrMittente() , 
      getAvvIdAvvocato() , 
      getCodEsito() , 
      "",
      getAnnoRegistro() , 
      getProgrRegistro() , 
      getCodTipoUfficioDestinatario() , 
      "",
      getCodLuogoDestinatario() , 
      "",
      getCodUfficioDestinatario() , 
      "",
      getCodStatoIstanza() , 
      "",
      getCodOperatoreInserimento() , 
      getDataInserimento() ,
      DateUtils.getDateToString(getDataInserimento(),"HH:mm:ss"),
      getCodUfficioInserimento() , 
      "",
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento() , 
      "",
      getFasSieIdFascicoloSiep() , 
      getEveIdEvento() ,
      getDataNotificaAvvocato(),
      getTipoAvvocato(),
      getDataInoltroPM()      
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(NuovaIstanzaModel aModel) throws DAOException {
    setIdNuovaIstanza               ( aModel.getIdNuovaIstanza()              );  
    setCodContenuto                 ( aModel.getCodContenuto()                );  
    setDataIstanza                  ( aModel.getDataIstanza()                 );  
    setNote                         ( aModel.getNote()                        );  
    setFlagPresdep                  ( aModel.getFlagPresdep()                 );  
    setSoggPresentante              ( aModel.getSoggPresentante()             );  
    setSoggPresentanteIdentificato  ( aModel.getSoggPresentanteIdentificato() );  
    setAvvIdAvvocatoPresentante     ( aModel.getAvvIdAvvocatoPresentante()    );  
    setCodAutoritaMittente					( aModel.getCodAutoritaMittente()         );  
    setCodSedeMittente              ( aModel.getCodSedeMittente()             );  
    setDescrMittente              	( aModel.getDescrMittente()		            );  
    setAvvIdAvvocato                ( aModel.getAvvIdAvvocato()               );  
    setCodEsito                     ( aModel.getCodEsito()                    );  
    setAnnoRegistro                 ( aModel.getAnnoRegistro()                );  
    setProgrRegistro                ( aModel.getProgrRegistro()               );  
    setCodTipoUfficioDestinatario   ( aModel.getCodTipoUfficioDestinatario()  );  
    setCodLuogoDestinatario         ( aModel.getCodLuogoDestinatario()        );  
    setCodUfficioDestinatario       ( aModel.getCodUfficioDestinatario()      );  
    setCodStatoIstanza              ( aModel.getCodStatoIstanza()             );  
    setCodOperatoreInserimento      ( aModel.getCodOperatoreInserimento()     );  
    setDataInserimento              ( aModel.getDataInserimento()             );  
    setCodUfficioInserimento        ( aModel.getCodUfficioInserimento()       );  
    setCodOperatoreAggiornamento    ( aModel.getCodOperatoreAggiornamento()   );  
    setDataAggiornamento            ( aModel.getDataAggiornamento()           );  
    setCodUfficioAggiornamento      ( aModel.getCodUfficioAggiornamento()     );  
    setFasSieIdFascicoloSiep        ( aModel.getFasSieIdFascicoloSiep()       );  
    setEveIdEvento                  ( aModel.getEveIdEvento()                 );  
    setDataNotificaAvvocato         ( aModel.getDataNotificaAvvocato()        );  
    setTipoAvvocato      						( aModel.getTipoAvvocato()								);  
    setDataInoltroPM         				( aModel.getDataInoltroPM()     		   		);  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(NuovaIstanzaModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdNuovaIstanza() != null ) { 
      lCondizioni += " and ID_NUOVA_ISTANZA = " + aModel.getIdNuovaIstanza() + ""; 
    } 
    if (aModel.getCodContenuto() != null && aModel.getCodContenuto().length() > 0) { 
      lCondizioni += " and COD_CONTENUTO = '" + aModel.getCodContenuto() + "' "; 
    } 
    if (aModel.getDataIstanza() != null ) { 
      lCondizioni += " and to_char(DATA_ISTANZA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataIstanza(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    if (aModel.getFlagPresdep() != null && aModel.getFlagPresdep().length() > 0) { 
      lCondizioni += " and FLAG_PRESDEP = '" + aModel.getFlagPresdep() + "' "; 
    } 
    if (aModel.getSoggPresentante() != null && aModel.getSoggPresentante().length() > 0) { 
      lCondizioni += " and SOGG_PRESENTANTE = '" + aModel.getSoggPresentante() + "' "; 
    } 
    if (aModel.getSoggPresentanteIdentificato() != null && aModel.getSoggPresentanteIdentificato().length() > 0) { 
      lCondizioni += " and SOGG_PRESENTANTE_IDENTIFICATO = '" + aModel.getSoggPresentanteIdentificato() + "' "; 
    } 
    if (aModel.getAvvIdAvvocatoPresentante() != null ) { 
      lCondizioni += " and AVV_ID_AVVOCATO_PRESENTANTE = " + aModel.getAvvIdAvvocatoPresentante() + ""; 
    } 
    if (aModel.getCodAutoritaMittente() != null && aModel.getCodAutoritaMittente().length() > 0) { 
      lCondizioni += " and COD_AUTORITA_MITTENTE = '" + aModel.getCodAutoritaMittente() + "' "; 
    } 
    if (aModel.getCodSedeMittente() != null && aModel.getCodSedeMittente().length() > 0) { 
      lCondizioni += " and COD_SEDE_MITTENTE = '" + aModel.getCodSedeMittente() + "' "; 
    } 
    if (aModel.getDescrMittente() != null && aModel.getDescrMittente().length() > 0) { 
      lCondizioni += " and DESCR_MITTENTE = '" + aModel.getDescrMittente() + "' "; 
    } 
    if (aModel.getAvvIdAvvocato() != null ) { 
      lCondizioni += " and AVV_ID_AVVOCATO = " + aModel.getAvvIdAvvocato() + ""; 
    } 
    if (aModel.getCodEsito() != null && aModel.getCodEsito().length() > 0) { 
      lCondizioni += " and COD_ESITO = '" + aModel.getCodEsito() + "' "; 
    } 
    if (aModel.getAnnoRegistro() != null ) { 
      lCondizioni += " and ANNO_REGISTRO = " + aModel.getAnnoRegistro() + ""; 
    } 
    if (aModel.getProgrRegistro() != null ) { 
      lCondizioni += " and PROGR_REGISTRO = " + aModel.getProgrRegistro() + ""; 
    } 
    if (aModel.getCodTipoUfficioDestinatario() != null && aModel.getCodTipoUfficioDestinatario().length() > 0) { 
      lCondizioni += " and COD_TIPO_UFFICIO_DESTINATARIO = '" + aModel.getCodTipoUfficioDestinatario() + "' "; 
    } 
    if (aModel.getCodLuogoDestinatario() != null && aModel.getCodLuogoDestinatario().length() > 0) { 
      lCondizioni += " and COD_LUOGO_DESTINATARIO = '" + aModel.getCodLuogoDestinatario() + "' "; 
    } 
    if (aModel.getCodUfficioDestinatario() != null && aModel.getCodUfficioDestinatario().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_DESTINATARIO = '" + aModel.getCodUfficioDestinatario() + "' "; 
    } 
    if (aModel.getCodStatoIstanza() != null && aModel.getCodStatoIstanza().length() > 0) { 
      lCondizioni += " and COD_STATO_ISTANZA = '" + aModel.getCodStatoIstanza() + "' "; 
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
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdNuovaIstanza) {
    String lCondizioni = new String();

    lCondizioni += " and ID_NUOVA_ISTANZA = " + aIdNuovaIstanza;
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
