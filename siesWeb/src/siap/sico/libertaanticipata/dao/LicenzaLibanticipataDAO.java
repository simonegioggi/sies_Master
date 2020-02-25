package siap.sico.libertaanticipata.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: LicenzaLibanticipataDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella LicenzaLibanticipata</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class LicenzaLibanticipataDAO extends TableDAO
{
  public LicenzaLibanticipataDAO(Connection con)
  {
     super(con);
     setTable("LICENZA_LIBANTICIPATA");

     //Settare la Sequence e i campi chiave
     setSequenceField("ID_LICENZA_LIBANTICIPATA","LIC_LIB_SEQ");
     setFieldKey("ID_LICENZA_LIBANTICIPATA", BIG_DECIMAL);

     setField("ID_LICENZA_LIBANTICIPATA", BIG_DECIMAL);
     setField("COD_TIPO_LICENZA", STRING);
     setField("NUMERO_GIORNI", BIG_DECIMAL);
     setField("DATA_INIZIO", DATE);
     setField("ORA_INIZIO", STRING);
     setField("DATA_FINE", DATE);
     setField("ORA_FINE", STRING);
     setField("LUOGO_SVOLGIMENTO_PROVA", STRING);
     setField("DATA_DETENZ_RIF_DA", DATE);
     setField("DATA_DETENZ_RIF_A", DATE);
     setField("FLAG_INFRAZIONE_OBBLIGHI", STRING);
     setField("DATA_INFRAZIONE_OBBLIGHI", DATE);
     setField("DESCR_INFRAZIONE_OBBLIGHI", STRING);
     setField("FLAG_SCOMPUTO", STRING);
     setField("COD_OPERATORE_INSERIMENTO", STRING);
     setField("DATA_INSERIMENTO", DATE);
     setField("COD_UFFICIO_INSERIMENTO", STRING);
     setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
     setField("DATA_AGGIORNAMENTO", DATE);
     setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
     setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
     setField("EVE_ID_EVENTO", BIG_DECIMAL);
     setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
     setField("FLAG_CONCESSO", STRING);
     setField("FLAG_ELABORATO", STRING);
     setField("FLAG_SCORTA", STRING);
     setField("COD_STATO_PERMESSO", STRING);
     setField("DESCR_STATO_PERMESSO", STRING);
     setField("NUMERO_ORE", BIG_DECIMAL);
     setField("ANNO_SIUS", BIG_DECIMAL);
     setField("NUMERO_SIUS", STRING);
     setField("ANNO_ORDINANZA", BIG_DECIMAL);
     setField("NUMERO_ORDINANZA", BIG_DECIMAL);
     setField("COD_UFFICIO_EMITTENTE", STRING);
     setField("COD_LUOGO_EMITTENTE", STRING);
     setField("DATA_EMISSIONE_ORDINANZA", DATE);

     setField("GIORNI_SCOMPUTATI", STRING);
     setField("NUMERO_ORE_NO_FRUITE", BIG_DECIMAL);
     setField("NUMERO_GIORNI_NO_FRUITI", BIG_DECIMAL);

     setField("COD_ESITO", STRING);
     setField("DATA_ANNOTAZIONE_ESITO", DATE);
     setField("ANNOTAZIONE", STRING);
     
     setField("NUMERO_MESI", BIG_DECIMAL);
     setField("SOMMA_RISARC_DANNI", BIG_DECIMAL);
  }


  //
  // METODI GET()
  //

  public BigDecimal 	getIdLicenzaLibanticipata()      throws DAOException	 { return getBigDecimal("ID_LICENZA_LIBANTICIPATA"); }
  public String 	    getCodTipoLicenza() 	         throws DAOException	 { return getString("COD_TIPO_LICENZA"); }
  public BigDecimal 	getNumeroGiorni() 	             throws DAOException	 { return getBigDecimal("NUMERO_GIORNI"); }
  public Date 		    getDataInizio() 	             throws DAOException	 { return getDate("DATA_INIZIO"); }
  public String         getOraInizio() 		             throws DAOException	 { return getString("ORA_INIZIO"); }
  public Date 		    getDataFine() 		             throws DAOException	 { return getDate("DATA_FINE"); }
  public String 	    getOraFine() 		             throws DAOException	 { return getString("ORA_FINE"); }
  public String 	    getLuogoSvolgimentoProva() 	     throws DAOException	 { return getString("LUOGO_SVOLGIMENTO_PROVA"); }
  public Date 		    getDataDetenzRifDa() 		     throws DAOException	 { return getDate("DATA_DETENZ_RIF_DA"); }
  public Date 		    getDataDetenzRifA() 		     throws DAOException	 { return getDate("DATA_DETENZ_RIF_A"); }
  public String 	    getFlagInfrazioneObblighi() 	 throws DAOException	 { return getString("FLAG_INFRAZIONE_OBBLIGHI"); }
  public Date 		    getDataInfrazioneObblighi() 	 throws DAOException	 { return getDate("DATA_INFRAZIONE_OBBLIGHI"); }
  public String 	    getDescrInfrazioneObblighi() 	 throws DAOException	 { return getString("DESCR_INFRAZIONE_OBBLIGHI"); }
  public String 	    getFlagScomputo() 		         throws DAOException	 { return getString("FLAG_SCOMPUTO"); }
  public String 	    getCodOperatoreInserimento() 	 throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 		    getDataInserimento() 		     throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 	    getCodUfficioInserimento() 	     throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 	    getCodOperatoreAggiornamento() 	 throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 		    getDataAggiornamento() 		     throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 	    getCodUfficioAggiornamento() 	 throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 	getFasSiuIdFascicoloSius() 	     throws DAOException	 { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }
  public BigDecimal 	getEveIdEvento() 	             throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
  public BigDecimal 	getFasSieIdFascicoloSiep() 	     throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public String 	    getFlagConcesso() 		         throws DAOException	 { return getString("FLAG_CONCESSO"); }
  public String 	    getFlagElaborato() 		         throws DAOException	 { return getString("FLAG_ELABORATO"); }
  public String 	    getFlagScorta() 		         throws DAOException	 { return getString("FLAG_SCORTA"); }
  public String 	    getCodStatoPermesso() 		     throws DAOException	 { return getString("COD_STATO_PERMESSO"); }
  public String 	    getDescrStatoPermesso() 	     throws DAOException	 { return getString("DESCR_STATO_PERMESSO"); }
  public BigDecimal     getNumeroOre() 		             throws DAOException	 { return getBigDecimal("NUMERO_ORE"); }
  public BigDecimal     getAnnoSius() 		             throws DAOException	 { return getBigDecimal("ANNO_SIUS"); }
  public String 		getNumeroSius() 		         throws DAOException	 { return getString("NUMERO_SIUS"); }
  public BigDecimal     getAnnoOrdinanza() 		         throws DAOException	 { return getBigDecimal("ANNO_ORDINANZA"); }
  public BigDecimal     getNumeroOrdinanza() 		     throws DAOException	 { return getBigDecimal("NUMERO_ORDINANZA"); }
  public String 		getCodUfficioEmittente() 		 throws DAOException	 { return getString("COD_UFFICIO_EMITTENTE"); }
  public String 		getCodLuogoEmittente() 		     throws DAOException	 { return getString("COD_LUOGO_EMITTENTE"); }
  // MEV10-s3: aggiunto recupero di proprietà
  public String 		getCodTipoUfficioEmittente()	 throws DAOException	 { return getString("COD_TIPO_UFFICIO_EMITTENTE"); }
  public Date 			getDataEmissioneOrdinanza() 	 throws DAOException	 { return getDate("DATA_EMISSIONE_ORDINANZA"); }

  public String         getGiorniScomputati()            throws DAOException     { return getString("GIORNI_SCOMPUTATI"); }
  public BigDecimal     getNumeroGiorniNoFruiti()        throws DAOException     { return getBigDecimal("NUMERO_GIORNI_NO_FRUITI"); }
  public BigDecimal     getNumeroOreNoFruite()           throws DAOException     { return getBigDecimal("NUMERO_ORE_NO_FRUITE"); }

  public String         getCodEsito()                    throws DAOException     { return getString("COD_ESITO"); }
  public Date           getDataAnnotazioneEsito()        throws DAOException     { return getDate("DATA_ANNOTAZIONE_ESITO"); }
  public String         getAnnotazione()                 throws DAOException     { return getString("ANNOTAZIONE"); }
  
  public BigDecimal 	getNumeroMesi() 	             throws DAOException   { return getBigDecimal("NUMERO_MESI"); }
  public BigDecimal   getSommaRisarcDanni()  throws DAOException   { return getBigDecimal("SOMMA_RISARC_DANNI"); }
  
  //
  // METODI SET()
  //

  public void setIdLicenzaLibanticipata(BigDecimal aValore )    { setBigDecimal("ID_LICENZA_LIBANTICIPATA", aValore); }
  public void setCodTipoLicenza(String aValore ) 		        { setString("COD_TIPO_LICENZA", aValore); }
  public void setNumeroGiorni(BigDecimal aValore ) 		        { setBigDecimal("NUMERO_GIORNI", aValore); }
  public void setDataInizio(Date aValore ) 			            { setDate("DATA_INIZIO", aValore); }
  public void setOraInizio(String aValore ) 			        { setString("ORA_INIZIO", aValore); }
  public void setDataFine(Date aValore ) 			            { setDate("DATA_FINE", aValore); }
  public void setOraFine(String aValore ) 			            { setString("ORA_FINE", aValore); }
  public void setLuogoSvolgimentoProva(String aValore ) 	    { setString("LUOGO_SVOLGIMENTO_PROVA", aValore); }
  public void setDataDetenzRifDa(Date aValore ) 		        { setDate("DATA_DETENZ_RIF_DA", aValore); }
  public void setDataDetenzRifA(Date aValore ) 		            { setDate("DATA_DETENZ_RIF_A", aValore); }
  public void setFlagInfrazioneObblighi(String aValore ) 	    { setString("FLAG_INFRAZIONE_OBBLIGHI", aValore); }
  public void setDataInfrazioneObblighi(Date aValore ) 	        { setDate("DATA_INFRAZIONE_OBBLIGHI", aValore); }
  public void setDescrInfrazioneObblighi(String aValore ) 	    { setString("DESCR_INFRAZIONE_OBBLIGHI", aValore); }
  public void setFlagScomputo(String aValore ) 		            { setString("FLAG_SCOMPUTO", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 	    { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 		        { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 	    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 		        { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 	    { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setFasSiuIdFascicoloSius(BigDecimal aValore ) 	{ setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }
  public void setEveIdEvento(BigDecimal aValore ) 		        { setBigDecimal("EVE_ID_EVENTO", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore ) 	{ setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setFlagConcesso(String aValore ) 					{ setString("FLAG_CONCESSO", aValore); }
  public void setFlagElaborato(String aValore ) 				{ setString("FLAG_ELABORATO", aValore); }
  public void setFlagScorta(String aValore ) 					{ setString("FLAG_SCORTA", aValore); }
  public void setCodStatoPermesso(String aValore ) 				{ setString("COD_STATO_PERMESSO", aValore); }
  public void setDescrStatoPermesso(String aValore ) 			{ setString("DESCR_STATO_PERMESSO", aValore); }
  public void setNumeroOre(BigDecimal aValore ) 				{ setBigDecimal("NUMERO_ORE", aValore); }
  public void setAnnoSius(BigDecimal aValore ) 			 		{ setBigDecimal("ANNO_SIUS", aValore); }
  public void setNumeroSius(String aValore ) 			 		{ setString("NUMERO_SIUS", aValore); }
  public void setAnnoOrdinanza(BigDecimal aValore ) 			{ setBigDecimal("ANNO_ORDINANZA", aValore); }
  public void setNumeroOrdinanza(BigDecimal aValore ) 			{ setBigDecimal("NUMERO_ORDINANZA", aValore); }
  public void setNumeroMesi(BigDecimal aValore ) 		        { setBigDecimal("NUMERO_MESI", aValore); }
  public void setSommaRisarcDanni(BigDecimal aValore )      { setBigDecimal("SOMMA_RISARC_DANNI", aValore); }
  
  public void setCodUfficioEmittente(String aValore )
  {
    if(aValore == null || aValore.equals(""))
      aValore = "-";

    setString("COD_UFFICIO_EMITTENTE", aValore);
  }
  public void setCodLuogoEmittente(String aValore )
  {
     if(aValore == null || aValore.equals(""))
      aValore = "-";

   setString("COD_LUOGO_EMITTENTE", aValore);
  }

  // MEV10-s3: aggiunta impostazione di proprietà
  public void setCodTipoUfficioEmittente(String aValore )
  {
    if(aValore == null || aValore.equals(""))
      aValore = "-";

    setString("COD_TIPO_UFFICIO_EMITTENTE", aValore);
  }
  
  public void setDataEmissioneOrdinanza(Date aValore)     { setDate("DATA_EMISSIONE_ORDINANZA", aValore); }

  public void setGiorniScomputati(String aValore)         { setString("GIORNI_SCOMPUTATI",aValore); }
  public void setNumeroGiorniNoFruiti(BigDecimal aValore) { setBigDecimal("NUMERO_GIORNI_NO_FRUITI", aValore); }
  public void setNumeroOreNoFruite(BigDecimal aValore)    { setBigDecimal("NUMERO_ORE_NO_FRUITE", aValore); }

  public void setCodEsito(String aValore)                 { setString("COD_ESITO",aValore); }
  public void setDataAnnotazioneEsito(Date aValore)       { setDate("DATA_ANNOTAZIONE_ESITO",aValore); }
  public void setAnnotazione(String aValore)              { setString("ANNOTAZIONE",aValore); }


  public GenericModel getModel() throws DAOException
  {
    return new LicenzaLibAnticipataModel( getIdLicenzaLibanticipata() ,
                                          getCodTipoLicenza() ,
                                          "",
                                          getNumeroGiorni() ,
                                          getDataInizio() ,
                                          getOraInizio(),
                                          getDataFine(),
                                          getOraFine(),
                                          getLuogoSvolgimentoProva(),
                                          getDataDetenzRifDa(),
                                          getDataDetenzRifA(),
                                          getFlagInfrazioneObblighi(),
                                          getDataInfrazioneObblighi(),
                                          getDescrInfrazioneObblighi(),
                                          getFlagScomputo(),
                                          getCodOperatoreInserimento(),
                                          getDataInserimento(),
                                          getCodUfficioInserimento(),
                                          "",
                                          getCodOperatoreAggiornamento(),
                                          getDataAggiornamento(),
                                          getCodUfficioAggiornamento(),
                                          "",
                                          getFasSiuIdFascicoloSius(),
                                          getEveIdEvento(),
                                          getFasSieIdFascicoloSiep(),
                                          getFlagConcesso(),
                                          getFlagElaborato(),
                                          getFlagScorta() ,
                                          getCodStatoPermesso() ,
                                          getDescrStatoPermesso() ,
                                          getNumeroOre() ,
                                          getAnnoSius() ,
                                          getNumeroSius() ,
                                          getAnnoOrdinanza() ,
                                          getNumeroOrdinanza() ,
                                          getCodUfficioEmittente() ,
                                          "",
                                          getCodLuogoEmittente() ,
                                          "",
                                          // MEV10-s3: aggiunta variabile
                                      	  getCodTipoUfficioEmittente(),
                                          getDataEmissioneOrdinanza(),
                                          getGiorniScomputati(),
                                          getCodEsito(),
                                          "",
                                          getDataAnnotazioneEsito(),
                                          getAnnotazione(),
                                          getNumeroGiorniNoFruiti(),
                                          getNumeroOreNoFruite(),
                                          getNumeroMesi(),
                                          getSommaRisarcDanni()
                                        );
  }


  public void setDAOFromModel(LicenzaLibAnticipataModel aModel) throws DAOException
  {
     setIdLicenzaLibanticipata( aModel.getIdLicenzaLibanticipata() );
     setCodTipoLicenza( aModel.getCodTipoLicenza() );
     setNumeroGiorni( aModel.getNumeroGiorni() );
     setDataInizio( aModel.getDataInizio() );
     setOraInizio( aModel.getOraInizio() );
     setDataFine( aModel.getDataFine() );
     setOraFine( aModel.getOraFine() );
     setLuogoSvolgimentoProva( aModel.getLuogoSvolgimentoProva() );
     setDataDetenzRifDa( aModel.getDataDetenzRifDa() );
     setDataDetenzRifA( aModel.getDataDetenzRifA() );
     setFlagInfrazioneObblighi( aModel.getFlagInfrazioneObblighi() );
     setDataInfrazioneObblighi( aModel.getDataInfrazioneObblighi() );
     setDescrInfrazioneObblighi( aModel.getDescrInfrazioneObblighi() );
     setFlagScomputo( aModel.getFlagScomputo() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
     setEveIdEvento( aModel.getEveIdEvento() );
     setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
     setFlagConcesso( aModel.getFlagConcesso() );
     setFlagElaborato( aModel.getFlagElaborato() );
     setFlagScorta( aModel.getFlagScorta() );
     setCodStatoPermesso( aModel.getCodStatoPermesso() );
     setDescrStatoPermesso( aModel.getDescrStatoPermesso() );
     setNumeroOre( aModel.getNumeroOre() );
     setAnnoSius( aModel.getAnnoSius() );
     setNumeroSius( aModel.getNumeroSius() );
     setAnnoOrdinanza( aModel.getAnnoOrdinanza() );
     setNumeroOrdinanza( aModel.getNumeroOrdinanza() );
     setCodUfficioEmittente( aModel.getCodUfficioEmittente() );
     setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
     // MEV10-s3: aggiunta impostazione di proprietà
     setCodTipoUfficioEmittente( aModel.getCodTipoUfficioEmittente() );
     setDataEmissioneOrdinanza( aModel.getDataEmissioneOrdinanza() );

     setGiorniScomputati(aModel.getGiorniScomputati());
     setNumeroGiorniNoFruiti(aModel.getNumeroGiorniNoFruiti());
     setNumeroOreNoFruite(aModel.getNumeroOreNoFruite());
     setCodEsito(aModel.getCodEsito());
     setDataAnnotazioneEsito(aModel.getDataAnnotazioneEsito());
     setAnnotazione(aModel.getAnnotazione());
     
     setNumeroMesi( aModel.getNumeroMesi() );
     setSommaRisarcDanni (aModel.getSommaRisarcDanni());
  }

  public void setDAOFromModelForUpdate(LicenzaLibAnticipataModel aModel) throws DAOException
  {
     setIdLicenzaLibanticipata( aModel.getIdLicenzaLibanticipata() );
     setCodTipoLicenza( aModel.getCodTipoLicenza() );
     setNumeroGiorni( aModel.getNumeroGiorni() );
     setDataInizio( aModel.getDataInizio() );
     setOraInizio( aModel.getOraInizio() );
     setDataFine( aModel.getDataFine() );
     setOraFine( aModel.getOraFine() );
     setLuogoSvolgimentoProva( aModel.getLuogoSvolgimentoProva() );
     setDataDetenzRifDa( aModel.getDataDetenzRifDa() );
     setDataDetenzRifA( aModel.getDataDetenzRifA() );
     setFlagInfrazioneObblighi( aModel.getFlagInfrazioneObblighi() );
     setDataInfrazioneObblighi( aModel.getDataInfrazioneObblighi() );
     setDescrInfrazioneObblighi( aModel.getDescrInfrazioneObblighi() );
     setFlagScomputo( aModel.getFlagScomputo() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
     setEveIdEvento( aModel.getEveIdEvento() );
     setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
     setFlagConcesso( aModel.getFlagConcesso() );
     setFlagElaborato( aModel.getFlagElaborato() );
     setFlagScorta( aModel.getFlagScorta() );
     setCodStatoPermesso( aModel.getCodStatoPermesso() );
     setDescrStatoPermesso( aModel.getDescrStatoPermesso() );
     setNumeroOre( aModel.getNumeroOre() );
     setAnnoSius( aModel.getAnnoSius() );
     setNumeroSius( aModel.getNumeroSius() );
     setAnnoOrdinanza( aModel.getAnnoOrdinanza() );
     setNumeroOrdinanza( aModel.getNumeroOrdinanza() );
     setCodUfficioEmittente( aModel.getCodUfficioEmittente() );
     setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
     // MEV10-s3: aggiunta impostazione di proprietà
     setCodTipoUfficioEmittente( aModel.getCodTipoUfficioEmittente() );
     setDataEmissioneOrdinanza( aModel.getDataEmissioneOrdinanza() );

     setGiorniScomputati(aModel.getGiorniScomputati());
     setNumeroGiorniNoFruiti(aModel.getNumeroGiorniNoFruiti());
     setNumeroOreNoFruite(aModel.getNumeroOreNoFruite());
     setCodEsito(aModel.getCodEsito());
     setDataAnnotazioneEsito(aModel.getDataAnnotazioneEsito());
     setAnnotazione(aModel.getAnnotazione());

     setCondizioneUpdate(aModel.getIdLicenzaLibanticipata());
     
     setNumeroMesi( aModel.getNumeroMesi() );
     setSommaRisarcDanni (aModel.getSommaRisarcDanni());
  }

  public void setCondizione(LicenzaLibAnticipataModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito )
      setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_LICENZA_LIBANTICIPATA = " + key );
  }

  public void setCondizioneIdFascicolo(BigDecimal aIdFascicoloSiep)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep );
  }

  public void setCondizioneIdEvento(BigDecimal aIdEvento)
  {
    setCondition(" EVE_ID_EVENTO = " + aIdEvento );
  }
  // 18/03/2008
  public void selCondizioneUpdateXIdFascicoloSius(BigDecimal key)
  {
    setCondition(" FAS_SIU_ID_FASCICOLO_SIUS = " + key);
  }
  // 18/03/2008 Update del FAS_SIE_ID_FASCICOLO_SIEP.
  public void setDAOFromModelForUpdateIdFascicoloSiep(LicenzaLibAnticipataModel aModel) throws DAOException
  {
    setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );

    selCondizioneUpdateXIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
  }

  // 29/06/2011 Risolto errore indotto dalla Modifica x Visibilità Stato di Esecuzione: 
  // Update LicenzaLibAnticipata va aggiornata per FAS_SIE_ID_FASCICOLO_SIEP.
  public void setDAOFromOrdinanzaForUpdate(DepositoOrdinanzaPcModel aModel, BigDecimal aIdFascicoloSiep) throws DAOException
  {
     setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
     setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
     setDataAggiornamento(aModel.getDataAggiornamento());
     setAnnoOrdinanza(aModel.getAnnoS3());
     setNumeroOrdinanza(aModel.getNumS3());
     setFasSieIdFascicoloSiep(aIdFascicoloSiep);
     setCondition(" EVE_ID_EVENTO = " + aModel.getIdEventoGenerato());
  }
  
  // 25/07/2014 DL 146/2013 - Revoca L.A.
  // Update LicenzaLibAnticipata va aggiornata per FAS_SIE_ID_FASCICOLO_SIEP.
  public void setDAOFromDecretoForUpdate(DepositoDecretoModel aModel, BigDecimal aIdFascicoloSiep) throws DAOException
  {
     setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
     setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
     setDataAggiornamento(aModel.getDataAggiornamento());
     setAnnoOrdinanza(aModel.getAnnoS72());
     setNumeroOrdinanza(aModel.getNumS72());
     setFasSieIdFascicoloSiep(aIdFascicoloSiep);
     setAnnotazione("DECRETO");
     setCondition(" EVE_ID_EVENTO = " + aModel.getIdEventoGenerato());
  }



}
