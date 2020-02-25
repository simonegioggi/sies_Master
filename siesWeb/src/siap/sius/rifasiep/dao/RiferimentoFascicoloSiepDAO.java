package siap.sius.rifasiep.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RiferimentoFascicoloSiepDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RiferimentoFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RiferimentoFascicoloSiepDAO extends TableDAO
{
  public RiferimentoFascicoloSiepDAO (Connection con)
  {
        super(con);
        setTable("RIFERIMENTO_FASCICOLO_SIEP");

        //Settare la Sequence e i campi chiave
        setSequenceField("ID_RIFERIMENTO_FASCICOLO_SIEP", "RIF_SIE_SEQ");
        setFieldKey("ID_RIFERIMENTO_FASCICOLO_SIEP", BIG_DECIMAL);

        setField("ID_RIFERIMENTO_FASCICOLO_SIEP", BIG_DECIMAL);
        setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
        setField("ANNO_FASCICOLO_SIEP", BIG_DECIMAL);
        setField("PROGR_FASCICOLO_SIEP", BIG_DECIMAL);
        setField("COD_UFF_FASCICOLO_SIEP", STRING);
        setField("COD_TIPO_PROVVEDIMENTO", STRING);
        setField("DATA_PROVVEDIMENTO", DATE);
        setField("ANNO_PROVVEDIMENTO", BIG_DECIMAL);
        setField("NUMERO_PROVVEDIMENTO", STRING);
        setField("COD_TIPO_AUTORITA_EMITTENTE", STRING);
        setField("COD_LUOGO_EMITTENTE", STRING);
        setField("DATA_IRREVOCABILITA", DATE);
        setField("DATA_FINE_VALIDITA", DATE);
        setField("COD_OPERATORE_INSERIMENTO", STRING);
        setField("DATA_INSERIMENTO", DATE);
        setField("COD_UFFICIO_INSERIMENTO", STRING);
        setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
        setField("DATA_AGGIORNAMENTO", DATE);
        setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
        setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
        setField("NOTE", STRING);
        setField("FLAG_MS_SN", STRING);
        setField("FLAG_FAS_SIUS_UNIF_SN", STRING);
  }


  //
  // METODI GET()
  //
  public BigDecimal 	 getIdRiferimentoFascicoloSiep()	throws DAOException	 { return getBigDecimal("ID_RIFERIMENTO_FASCICOLO_SIEP"); }
  public BigDecimal 	 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public BigDecimal 	 getAnnoFascicoloSiep() 			throws DAOException	 { return getBigDecimal("ANNO_FASCICOLO_SIEP"); }
  public BigDecimal 	 getProgrFascicoloSiep() 			throws DAOException	 { return getBigDecimal("PROGR_FASCICOLO_SIEP"); }
  public String 		 getCodUffFascicoloSiep() 			throws DAOException	 { return getString("COD_UFF_FASCICOLO_SIEP"); }
  public String 		 getCodTipoProvvedimento() 			throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO"); }
  public Date 			 getDataProvvedimento() 			throws DAOException	 { return getDate("DATA_PROVVEDIMENTO"); }
  public BigDecimal 	 getAnnoProvvedimento() 			throws DAOException	 { return getBigDecimal("ANNO_PROVVEDIMENTO"); }
  public String			 getNumeroProvvedimento() 			throws DAOException	 { return getString("NUMERO_PROVVEDIMENTO"); }
  public String			 getCodTipoAutoritaEmittente() 		throws DAOException	 { return getString("COD_TIPO_AUTORITA_EMITTENTE"); }
  public String 		 getCodLuogoEmittente() 			throws DAOException	 { return getString("COD_LUOGO_EMITTENTE"); }
  public Date 			 getDataIrrevocabilita() 			throws DAOException	 { return getDate("DATA_IRREVOCABILITA"); }
  public Date 			 getDataFineValidita()   			throws DAOException	 { return getDate("DATA_FINE_VALIDITA"); }
  public String 		 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			 getDataInserimento()    			throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 		 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		 getCodOperatoreAggiornamento()		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 			 getDataAggiornamento() 			throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 		 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 	 getFasSiuIdFascicoloSius() 		throws DAOException	 { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }
  public String 		 getNote() 		                	throws DAOException	 { return getString("NOTE"); }
  public String 		 getFlagMS() 		                throws DAOException	 { return getString("FLAG_MS_SN"); }
  public String 		 getFlagFasSiusUnif() 		        throws DAOException	 { return getString("FLAG_FAS_SIUS_UNIF_SN"); }

  //
  // METODI SET()
  //
  public void  	 setIdRiferimentoFascicoloSiep(BigDecimal aValore )   { setBigDecimal("ID_RIFERIMENTO_FASCICOLO_SIEP", aValore); }
  public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 		  { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void  	 setAnnoFascicoloSiep(BigDecimal aValore ) 			  { setBigDecimal("ANNO_FASCICOLO_SIEP", aValore); }
  public void  	 setProgrFascicoloSiep(BigDecimal aValore ) 		  { setBigDecimal("PROGR_FASCICOLO_SIEP", aValore); }
  public void  	 setCodUffFascicoloSiep(String aValore ) 			  { setString("COD_UFF_FASCICOLO_SIEP", aValore); }
  public void  	 setCodTipoProvvedimento(String aValore ) 			  { setString("COD_TIPO_PROVVEDIMENTO", aValore); }
  public void  	 setDataProvvedimento(Date aValore ) 			      { setDate("DATA_PROVVEDIMENTO", aValore); }
  public void  	 setAnnoProvvedimento(BigDecimal aValore ) 			  { setBigDecimal("ANNO_PROVVEDIMENTO", aValore); }
  public void  	 setNumeroProvvedimento(String aValore ) 			  { setString("NUMERO_PROVVEDIMENTO", aValore); }
  public void  	 setCodTipoAutoritaEmittente(String aValore ) 		  { setString("COD_TIPO_AUTORITA_EMITTENTE", aValore); }
  public void  	 setCodLuogoEmittente(String aValore )   			  { setString("COD_LUOGO_EMITTENTE", aValore); }
  public void  	 setDataIrrevocabilita(Date aValore )    			  { setDate("DATA_IRREVOCABILITA", aValore); }
  public void  	 setDataFineValidita(Date aValore )      			  { setDate("DATA_FINE_VALIDITA", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 		  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore )       			  { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 			  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 		  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore )     			  { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 		  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setFasSiuIdFascicoloSius(BigDecimal aValore ) 		  { setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }
  public void  	 setNote(String aValore )                             { setString("NOTE", aValore); }
  public void  	 setFlagMS(String aValore )                           { setString("FLAG_MS_SN", aValore); }
  public void  	 setFlagFasSiusUnif(String aValore )                  { setString("FLAG_FAS_SIUS_UNIF_SN", aValore); }
  

  public GenericModel getModel() throws DAOException
  {
    return new RiferimentoFascicoloSiepModel
         (getIdRiferimentoFascicoloSiep() ,
          getFasSieIdFascicoloSiep() ,
          getAnnoFascicoloSiep() ,
          getProgrFascicoloSiep() ,
          getCodUffFascicoloSiep() ,
          "",
          getCodTipoProvvedimento() ,
          "",
          getDataProvvedimento() ,
          getAnnoProvvedimento() ,
          getNumeroProvvedimento() ,
          getCodTipoAutoritaEmittente() ,
          "",
          getCodLuogoEmittente() ,
          "",
          getDataIrrevocabilita() ,
          getDataFineValidita() ,
          getCodOperatoreInserimento() ,
          getDataInserimento() ,
          getCodUfficioInserimento() ,
          "",
          getCodOperatoreAggiornamento() ,
          getDataAggiornamento() ,
          getCodUfficioAggiornamento() ,
          "",
          getFasSiuIdFascicoloSius(),
          "",
          "",
          "");
  }


  public void 	 setDAOFromModel(RiferimentoFascicoloSiepModel aModel) throws DAOException
  {
    setIdRiferimentoFascicoloSiep( aModel.getIdRiferimentoFascicoloSiep() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setAnnoFascicoloSiep( aModel.getAnnoFascicoloSiep() );
    setProgrFascicoloSiep( aModel.getProgrFascicoloSiep() );
    setCodUffFascicoloSiep( aModel.getCodUffFascicoloSiep() );
    setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
    setDataProvvedimento( aModel.getDataProvvedimento() );
    setAnnoProvvedimento( aModel.getAnnoProvvedimento() );
    setNumeroProvvedimento( aModel.getNumeroProvvedimento() );
    setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
    setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
    setDataIrrevocabilita( aModel.getDataIrrevocabilita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
    setNote( aModel.getNote() );
    setFlagMS( aModel.getFlagMS() );
    setFlagFasSiusUnif( aModel.getFlagFasSiusUnif() );
  }

  public void setDAOFromModelForUpdate(RiferimentoFascicoloSiepModel aModel) throws DAOException
  {
    setIdRiferimentoFascicoloSiep( aModel.getIdRiferimentoFascicoloSiep() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setAnnoFascicoloSiep( aModel.getAnnoFascicoloSiep() );
    setProgrFascicoloSiep( aModel.getProgrFascicoloSiep() );
    setCodUffFascicoloSiep( aModel.getCodUffFascicoloSiep() );
    setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
    setDataProvvedimento( aModel.getDataProvvedimento() );
    setAnnoProvvedimento( aModel.getAnnoProvvedimento() );
    setNumeroProvvedimento( aModel.getNumeroProvvedimento() );
    setCodTipoAutoritaEmittente( aModel.getCodTipoAutoritaEmittente() );
    setCodLuogoEmittente( aModel.getCodLuogoEmittente() );
    setDataIrrevocabilita( aModel.getDataIrrevocabilita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
    setNote( aModel.getNote() );
    setFlagMS( aModel.getFlagMS() );
    setFlagFasSiusUnif( aModel.getFlagFasSiusUnif() );
    setCondizioneUpdate(aModel.getIdRiferimentoFascicoloSiep());
  }


  public void setCondizione(RiferimentoFascicoloSiepModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_RIFERIMENTO_FASCICOLO_SIEP = " + key );
  }

  public void setCondizioneUpdateByIdFasSius(BigDecimal key)
  {
    setCondition(" FAS_SIU_ID_FASCICOLO_SIUS = " + key );
  }

}