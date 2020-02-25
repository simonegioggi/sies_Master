package siap.sius.avvocato.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: AvvocatoFascicoloSiusDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AvvocatoFascicoloSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AvvocatoFascicoloSiusDAO extends SIAPTableDAO
{
	public AvvocatoFascicoloSiusDAO (Connection con)
	{
    super(con);
    setTable("AVVOCATO_FASCICOLO_SIUS");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_AVVOCATO_FASCICOLO_SIUS", "AVV_FAS_SIU_SEQ");

    setField("ID_AVVOCATO_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("COD_TIPO_AVVOCATO", STRING);
    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("AVV_ID_AVVOCATO", BIG_DECIMAL);
    setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("COD_MOTIVO_DESIGNAZIONE", STRING);
    setField("COD_TIPO_AUTORITA", STRING);
    setField("SEDE_TIPO_AUTORITA", STRING);
    setField("INDIRIZZO_TIPO_AUTORITA", STRING);
    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
    setField("COD_TIPO_AUTORITA_DIF", STRING);
    setField("SEDE_TIPO_AUTORITA_DIF", STRING);
    setField("NOTE", STRING);
	}

  //
  // METODI GET()
  //

  public BigDecimal 		 getIdAvvocatoFascicoloSius() 	throws DAOException	{ return getBigDecimal("ID_AVVOCATO_FASCICOLO_SIUS"); }
  public String 				 getCodTipoAvvocato() 		      throws DAOException	{ return getString("COD_TIPO_AVVOCATO"); }
  public Date 					 getDataInizioValidita() 		    throws DAOException	{ return getDate("DATA_INIZIO_VALIDITA"); }
  public Date 					 getDataFineValidita() 		      throws DAOException	{ return getDate("DATA_FINE_VALIDITA"); }
  public String 				 getCodOperatoreInserimento() 	throws DAOException	{ return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 					 getDataInserimento() 		      throws DAOException	{ return getDate("DATA_INSERIMENTO"); }
  public String 				 getCodUfficioInserimento() 		throws DAOException	{ return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 				 getCodOperatoreAggiornamento() throws DAOException	{ return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 					 getDataAggiornamento() 		    throws DAOException	{ return getDate("DATA_AGGIORNAMENTO"); }
  public String 				 getCodUfficioAggiornamento() 	throws DAOException	{ return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 		 getFasSiuIdFascicoloSius() 		throws DAOException	{ return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }
  public BigDecimal 		 getAvvIdAvvocato() 		        throws DAOException	{ return getBigDecimal("AVV_ID_AVVOCATO"); }
  public String 				 getCodTipoAutorita() 	        throws DAOException	{ return getString("COD_TIPO_AUTORITA"); }
  public String 				 getSedeTipoAutorita() 	        throws DAOException	{ return getString("SEDE_TIPO_AUTORITA"); }
  public String 				 getCodTipoAutoritaDif() 	      throws DAOException	{ return getString("COD_TIPO_AUTORITA_DIF"); }
  public String 				 getSedeTipoAutoritaDif() 	    throws DAOException	{ return getString("SEDE_TIPO_AUTORITA_DIF"); }
  public String          getCodMotivoDesignazione()     throws DAOException	{ return getString("COD_MOTIVO_DESIGNAZIONE"); }
  public String          getIstDetIdIstitutoDetenzione() throws DAOException { return getString("IST_DET_ID_ISTITUTO_DETENZIONE"); }
  public String          getIndirizzoTipoAutorita()     throws DAOException { return getString("INDIRIZZO_TIPO_AUTORITA"); }
  public String          getNote()                      throws DAOException { return getString("NOTE"); }
  //
  // METODI SET()
  //

  public void  	 setIdAvvocatoFascicoloSius(BigDecimal aValore )    { setBigDecimal("ID_AVVOCATO_FASCICOLO_SIUS", aValore); }
  public void  	 setCodTipoAvvocato(String aValore ) 			          { setString("COD_TIPO_AVVOCATO", aValore); }
  public void  	 setDataInizioValidita(Date aValore ) 			        { setDate("DATA_INIZIO_VALIDITA", aValore); }
  public void  	 setDataFineValidita(Date aValore ) 			          { setDate("DATA_FINE_VALIDITA", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 			  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			            { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 			    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 			{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			          { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 			  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setFasSiuIdFascicoloSius(BigDecimal aValore ) 			{ setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }
  public void  	 setAvvIdAvvocato(BigDecimal aValore ) 			        { setBigDecimal("AVV_ID_AVVOCATO", aValore); }
  public void  	 setCodTipoAutorita(String aValore ) 			      { setString("COD_TIPO_AUTORITA", aValore); }
  public void  	 setSedeTipoAutorita(String aValore ) 			      { setString("SEDE_TIPO_AUTORITA", aValore); }
  public void  	 setCodTipoAutoritaDif(String aValore ) 			      { setString("COD_TIPO_AUTORITA_DIF", aValore); }
  public void  	 setSedeTipoAutoritaDif(String aValore ) 			      { setString("SEDE_TIPO_AUTORITA_DIF", aValore); }
  public void    setCodMotivoDesignazione(String aValore )          { setString("COD_MOTIVO_DESIGNAZIONE", aValore); }
  public void    setIstDetIdIstitutoDetenzione(String aValore)            { setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); }
  public void    setIndirizzoTipoAutorita(String aValore)                 { setString("INDIRIZZO_TIPO_AUTORITA", aValore); }
  public void    setNote(String aValore)                            { setString("NOTE", aValore); }

	public GenericModel getModel() throws DAOException
  {
    return new AvvocatoFascicoloSiusModel(
                    getIdAvvocatoFascicoloSius() ,
								    getCodTipoAvvocato() ,
								    "",
								    getDataInizioValidita() ,
								    getDataFineValidita() ,
								    getCodOperatoreInserimento() ,
								    getDataInserimento() ,
								    getCodUfficioInserimento() ,
								    "",
								    getCodOperatoreAggiornamento() ,
								    getDataAggiornamento() ,
								    getCodUfficioAggiornamento() ,
								    "",
								    getAvvIdAvvocato() ,
                    getFasSiuIdFascicoloSius() ,
                    getCodMotivoDesignazione() ,
                    getCodTipoAutorita() ,
                    getSedeTipoAutorita() ,
                    getIndirizzoTipoAutorita() ,
                    getIstDetIdIstitutoDetenzione() ,
                    getNote() ,
                    getCodTipoAutoritaDif() ,
                    getSedeTipoAutoritaDif() ,
                    "",
                    "",
                    "",
                    "",
                    ""
								  );
		}

    public void setDAOFromModel(AvvocatoFascicoloSiusModel aModel)
    throws DAOException
    {
      setIdAvvocatoFascicoloSius( aModel.getIdAvvocatoFascicoloSius() );
      setCodTipoAvvocato( aModel.getCodTipoAvvocato() );
      setDataInizioValidita( aModel.getDataInizioValidita() );
      setDataFineValidita( aModel.getDataFineValidita() );
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
      setDataInserimento( aModel.getDataInserimento() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
      setAvvIdAvvocato( aModel.getAvvIdAvvocato() );
      setCodTipoAutorita( aModel.getCodTipoAutorita() );
      setSedeTipoAutorita( aModel.getSedeAutorita() );
      setCodTipoAutoritaDif( aModel.getCodTipoAutoritaDif() );
      setSedeTipoAutoritaDif( aModel.getSedeAutoritaDif() );
      setCodMotivoDesignazione( aModel.getCodMotivoDesignazione() );
      setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione() );
      setIndirizzoTipoAutorita( aModel.getIndirizzoTipoAutorita() );
      setNote( aModel.getNote() );
		}

    public void setDAOFromModelForUpdate(AvvocatoFascicoloSiusModel aModel)
    throws DAOException
    {
      setIdAvvocatoFascicoloSius( aModel.getIdAvvocatoFascicoloSius() );
      setCodTipoAvvocato( aModel.getCodTipoAvvocato() );
      setDataInizioValidita( aModel.getDataInizioValidita() );
      setDataFineValidita( aModel.getDataFineValidita() );
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
      setDataInserimento( aModel.getDataInserimento() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
      setAvvIdAvvocato( aModel.getAvvIdAvvocato() );
      setCodTipoAutorita( aModel.getCodTipoAutorita() );
      setSedeTipoAutorita( aModel.getSedeAutorita() );
      setCodTipoAutoritaDif( aModel.getCodTipoAutoritaDif() );
      setSedeTipoAutoritaDif( aModel.getSedeAutoritaDif() );
      setCodMotivoDesignazione( aModel.getCodMotivoDesignazione() );
      setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione() );
      setIndirizzoTipoAutorita( aModel.getIndirizzoTipoAutorita() );
		  setNote( aModel.getNote() );
    }



  // 02/11/2006 MAC x Deassegnazione e Sostituzione Avvocati SIUS.
  public void setDAOFromModelForUpdateIdAvvocatoIdFascicolo(AvvocatoFascicoloSiusModel aModel) throws DAOException
  {
    setDataFineValidita(aModel.getDataFineValidita());
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());

    selCondizioneUpdateAvvIdAvvFasIdFas(aModel.getAvvIdAvvocato(), aModel.getFasSiuIdFascicoloSius());
  }

  public void selCondizioneUpdateAvvIdAvvFasIdFas(BigDecimal IdAvv, BigDecimal IdFas)
  {
    setCondition(" AVV_ID_AVVOCATO = " + IdAvv +" AND FAS_SIU_ID_FASCICOLO_SIUS = " + IdFas );
  }

  public void setCondizioneUpdateFascSius(BigDecimal key)
  {
      setCondition(" ID_AVVOCATO_FASCICOLO_SIUS = " + key );
  }


  public void setCondizione(AvvocatoFascicoloSiusModel aModel)
  {
   String lCondizioni = new String();

   boolean lInserito = false;
   if ( lInserito ) setCondition(lCondizioni);
   }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_AVVOCATO_FASCICOLO_SIUS = " + key );
  }
}

