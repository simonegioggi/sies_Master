package siap.sius.esecuzionemisurasicurezza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: EsecuzioneMisuraSicurezzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella EsecuzioneMisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class EsecuzioneMisuraSicurezzaDAO extends TableDAO
{
        public EsecuzioneMisuraSicurezzaDAO (Connection con)
        {
          super(con);
          setTable("ESECUZIONE_MISURA_SICUREZZA");

          this.setSequenceField("ID_ESECUZIONE_MISURA_SICUREZZA","ESE_MIS_SIC_SEQ");
          this.setFieldKey("ID_ESECUZIONE_MISURA_SICUREZZA", BIG_DECIMAL);

          setField("ID_ESECUZIONE_MISURA_SICUREZZA", BIG_DECIMAL);
          setField("ANNO_S07", BIG_DECIMAL);
          setField("PROGR_S07", BIG_DECIMAL);
          setField("DATA_ORDINANZA", DATE);
          setField("COD_AUTORITA_EMITT_ORD", STRING);
          setField("COD_TIPO_AUTORITA_EMITT_ORD", STRING);
          setField("COD_LUOGO_AUTORITA_EMITT_ORD", STRING);
          setField("COD_TIPO_MISURA", STRING);
          setField("DATA_INIZIO_MISURA", DATE);
          setField("DATA_TERMINE_INIZIALE", DATE);
          setField("DATA_TERMINE_ATTUALE", DATE);
          setField("DATA_DECLARATORIA_EMS", DATE);
          setField("DEP_DEC_ID_DEPOSITO_DECRETO", BIG_DECIMAL);
          setField("DEP_OPID_DEPOSITO_ORDINANZA_PC", BIG_DECIMAL);
          setField("NOTE", STRING);
          setField("COD_OPERATORE_INSERIMENTO", STRING);
          setField("DATA_INSERIMENTO", DATE);
          setField("COD_UFFICIO_INSERIMENTO", STRING);
          setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
          setField("DATA_AGGIORNAMENTO", DATE);
          setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
          setField("GEN_PRID_GENERALE_PROCEDIMENTO", BIG_DECIMAL);
          setField("LUOGO_ESECUZIONE_MISURA", STRING);
          setField("NUM_GIORNI_MISURA", BIG_DECIMAL);
          setField("NUM_MESI_MISURA", BIG_DECIMAL);
          setField("NUM_ANNI_MISURA", BIG_DECIMAL);
        }


  //
  // METODI GET()
  //

    public BigDecimal 		 getIdEsecuzioneMisuraSicurezza() 		throws DAOException	 { return getBigDecimal("ID_ESECUZIONE_MISURA_SICUREZZA"); }
    public BigDecimal 		 getAnnoS07() 		throws DAOException	 { return getBigDecimal("ANNO_S07"); }
    public BigDecimal 		 getProgrS07() 		throws DAOException	 { return getBigDecimal("PROGR_S07"); }
    public Date 					 getDataOrdinanza() 		throws DAOException	 { return getDate("DATA_ORDINANZA"); }
    public String 				 getCodAutoritaEmittOrd() 		throws DAOException	 { return getString("COD_AUTORITA_EMITT_ORD"); }
    public String 				 getCodTipoAutoritaEmittOrd() 		throws DAOException	 { return getString("COD_TIPO_AUTORITA_EMITT_ORD"); }
    public String 				 getCodLuogoAutoritaEmittOrd() 		throws DAOException	 { return getString("COD_LUOGO_AUTORITA_EMITT_ORD"); }
    public String 				 getCodTipoMisura() 		throws DAOException	 { return getString("COD_TIPO_MISURA"); }
    public Date 					 getDataInizioMisura() 		throws DAOException	 { return getDate("DATA_INIZIO_MISURA"); }
    public Date 					 getDataTermineIniziale() 		throws DAOException	 { return getDate("DATA_TERMINE_INIZIALE"); }
    public Date 					 getDataTermineAttuale() 		throws DAOException	 { return getDate("DATA_TERMINE_ATTUALE"); }
    public Date 					 getDataDeclaratoriaEMS() 		throws DAOException	 { return getDate("DATA_DECLARATORIA_EMS"); }
    public BigDecimal 		 getDepDecIdDepositoDecreto() 		throws DAOException	 { return getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO"); }
    public BigDecimal 		 getDepOpidDepositoOrdinanzaPc() 		throws DAOException	 { return getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC"); }
    public String 				 getNote() 		throws DAOException	 { return getString("NOTE"); }
    public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
    public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
    public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
    public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
    public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
    public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
    public BigDecimal 		 getGenPridGeneraleProcedimento() 		throws DAOException	 { return getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"); }
    public String 				 getLuogoEsecuzioneMisura() 		throws DAOException	 { return getString("LUOGO_ESECUZIONE_MISURA"); }
    public BigDecimal 		 getNumGiorniMisura() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_MISURA"); }
    public BigDecimal 		 getNumMesiMisura() 		throws DAOException	 { return getBigDecimal("NUM_MESI_MISURA"); }
    public BigDecimal 		 getNumAnniMisura() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_MISURA"); }


  //
  // METODI SET()
  //

    public void  	 setIdEsecuzioneMisuraSicurezza(BigDecimal aValore ) 			 { setBigDecimal("ID_ESECUZIONE_MISURA_SICUREZZA", aValore); }
    public void  	 setAnnoS07(BigDecimal aValore ) 			 { setBigDecimal("ANNO_S07", aValore); }
    public void  	 setProgrS07(BigDecimal aValore ) 			 { setBigDecimal("PROGR_S07", aValore); }
    public void  	 setDataOrdinanza(Date aValore ) 			 { setDate("DATA_ORDINANZA", aValore); }
    public void  	 setCodAutoritaEmittOrd(String aValore ) 			 { setString("COD_AUTORITA_EMITT_ORD", aValore); }
    public void  	 setCodTipoAutoritaEmittOrd(String aValore ) 			 { setString("COD_TIPO_AUTORITA_EMITT_ORD", aValore); }
    public void  	 setCodLuogoAutoritaEmittOrd(String aValore ) 			 { setString("COD_LUOGO_AUTORITA_EMITT_ORD", aValore); }
    public void  	 setCodTipoMisura(String aValore ) 			 { setString("COD_TIPO_MISURA", aValore); }
    public void  	 setDataInizioMisura(Date aValore ) 			 { setDate("DATA_INIZIO_MISURA", aValore); }
    public void  	 setDataTermineIniziale(Date aValore ) 			 { setDate("DATA_TERMINE_INIZIALE", aValore); }
    public void  	 setDataTermineAttuale(Date aValore ) 			 { setDate("DATA_TERMINE_ATTUALE", aValore); }
    public void  	 setDataDeclaratoriaEMS(Date aValore ) 			 { setDate("DATA_DECLARATORIA_EMS", aValore); }
    public void  	 setDepDecIdDepositoDecreto(BigDecimal aValore ) 			 { setBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO", aValore); }
    public void  	 setDepOpidDepositoOrdinanzaPc(BigDecimal aValore ) 			 { setBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC", aValore); }
    public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
    public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
    public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
    public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
    public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
    public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
    public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
    public void  	 setGenPridGeneraleProcedimento(BigDecimal aValore ) 			 { setBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO", aValore); }
    public void  	 setLuogoEsecuzioneMisura(String aValore ) 			 { setString("LUOGO_ESECUZIONE_MISURA", aValore); }
    public void  	 setNumGiorniMisura(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_MISURA", aValore); }
    public void  	 setNumMesiMisura(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_MISURA", aValore); }
    public void  	 setNumAnniMisura(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_MISURA", aValore); }


    public GenericModel getModel() throws DAOException
    {
      return new EsecuzioneMisuraSicurezzaModel(
                     getIdEsecuzioneMisuraSicurezza() ,
                     getAnnoS07() ,
                     getProgrS07() ,
                     getDataOrdinanza() ,
                     getCodAutoritaEmittOrd() ,
                     "",
                     getCodTipoAutoritaEmittOrd() ,
                     "",
                     getCodLuogoAutoritaEmittOrd() ,
                     "",
                     getCodTipoMisura() ,
                     "",
                     getDataInizioMisura() ,
                     getDataTermineIniziale() ,
                     getDataTermineAttuale() ,
                     getDataDeclaratoriaEMS() ,
                     getDepDecIdDepositoDecreto() ,
                     getDepOpidDepositoOrdinanzaPc() ,
                     getNote() ,
                     getCodOperatoreInserimento() ,
                     getDataInserimento() ,
                     getCodUfficioInserimento() ,
                     "",
                     getCodOperatoreAggiornamento() ,
                     getDataAggiornamento() ,
                     getCodUfficioAggiornamento() ,
                     "",
                     getGenPridGeneraleProcedimento(),
                     getLuogoEsecuzioneMisura(),
                     getNumGiorniMisura(),
                     getNumMesiMisura(),
                     getNumAnniMisura()
                    );
    }

    public void setDAOFromModel(EsecuzioneMisuraSicurezzaModel aModel) throws DAOException
    {
      setIdEsecuzioneMisuraSicurezza( aModel.getIdEsecuzioneMisuraSicurezza() );
      setAnnoS07( aModel.getAnnoS07() );
      setProgrS07( aModel.getProgrS07() );
      setDataOrdinanza( aModel.getDataOrdinanza() );
      setCodAutoritaEmittOrd( aModel.getCodAutoritaEmittOrd() );
      setCodTipoAutoritaEmittOrd( aModel.getCodTipoAutoritaEmittOrd() );
      setCodLuogoAutoritaEmittOrd( aModel.getCodLuogoAutoritaEmittOrd() );
      setCodTipoMisura( aModel.getCodTipoMisura() );
      setDataInizioMisura( aModel.getDataInizioMisura() );
      setDataTermineIniziale( aModel.getDataTermineIniziale() );
      setDataTermineAttuale( aModel.getDataTermineAttuale() );
      setDataDeclaratoriaEMS( aModel.getDataDeclaratoriaEMS() );
      setDepDecIdDepositoDecreto( aModel.getDepDecIdDepositoDecreto() );
      setDepOpidDepositoOrdinanzaPc( aModel.getDepOpidDepositoOrdinanzaPc() );
      setNote( aModel.getNote() );
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
      setDataInserimento( aModel.getDataInserimento() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setGenPridGeneraleProcedimento( aModel.getGenPridGeneraleProcedimento() );
      setLuogoEsecuzioneMisura( aModel.getLuogoEsecuzioneMisura() );
      setNumGiorniMisura( aModel.getNumGiorniMisura() );
      setNumMesiMisura( aModel.getNumMesiMisura() );
      setNumAnniMisura( aModel.getNumAnniMisura() );
    }


    public void setDAOFromModelForUpdate(EsecuzioneMisuraSicurezzaModel aModel) throws DAOException
    {
      setIdEsecuzioneMisuraSicurezza( aModel.getIdEsecuzioneMisuraSicurezza() );
      setDataInizioMisura( aModel.getDataInizioMisura() );
      setDataTermineIniziale( aModel.getDataTermineIniziale() );
      setDataTermineAttuale( aModel.getDataTermineAttuale() );
      setLuogoEsecuzioneMisura( aModel.getLuogoEsecuzioneMisura() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setNumAnniMisura(aModel.getNumAnniMisura());
      setNumMesiMisura(aModel.getNumMesiMisura());
      setNumGiorniMisura(aModel.getNumGiorniMisura());
      
      setCondizioneUpdate(aModel.getIdEsecuzioneMisuraSicurezza());
    }

    public void setCondizioneUpdate(BigDecimal key)
    {
      setCondition(" ID_ESECUZIONE_MISURA_SICUREZZA = " + key );
    }

    public void setCondizioneDeleteByGP(BigDecimal key)
    {
      setCondition(" GEN_PRID_GENERALE_PROCEDIMENTO = " + key );
    }
    
    public void setCondizioneDeleteByIdOrdinanza(BigDecimal key)
    {
      setCondition(" DEP_OPID_DEPOSITO_ORDINANZA_PC = " + key );
    }

    public void setDAOFromModelForUpdatebyFascicolo(EsecuzioneMisuraSicurezzaModel aModel) throws DAOException
    {
      setIdEsecuzioneMisuraSicurezza( aModel.getIdEsecuzioneMisuraSicurezza() );
      setAnnoS07( aModel.getAnnoS07() );
      setProgrS07( aModel.getProgrS07() );
      setCodTipoMisura( aModel.getCodTipoMisura() );
      setDataOrdinanza( aModel.getDataOrdinanza() );
      setCodAutoritaEmittOrd( aModel.getCodAutoritaEmittOrd() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

      setCondizioneUpdate(aModel.getIdEsecuzioneMisuraSicurezza());
    }

}
