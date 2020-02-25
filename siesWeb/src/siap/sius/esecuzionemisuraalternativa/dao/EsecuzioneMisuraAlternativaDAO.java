package siap.sius.esecuzionemisuraalternativa.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: EsecuzioneMisuraAlternativaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella EsecuzioneMisuraAlternativa</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class EsecuzioneMisuraAlternativaDAO extends TableDAO
{
        public EsecuzioneMisuraAlternativaDAO (Connection con)
        {
          super(con);
          setTable("ESECUZIONE_MISURA_ALTERNATIVA");

          this.setSequenceField("ID_ESECUZIONE_MISURA_ALTERNATI","ESE_MIS_ALT_SEQ");
          this.setFieldKey("ID_ESECUZIONE_MISURA_ALTERNATI", BIG_DECIMAL);

          setField("ID_ESECUZIONE_MISURA_ALTERNATI", BIG_DECIMAL);
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
          setField("DATA_DECLARATORIA_EP", DATE);
          setField("DATA_TX_ATTI_EST_PENA", DATE);
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
        }


  //
  // METODI GET()
  //

    public BigDecimal 		 getIdEsecuzioneMisuraAlternati() 		throws DAOException	 { return getBigDecimal("ID_ESECUZIONE_MISURA_ALTERNATI"); }
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
    public Date 					 getDataDeclaratoriaEp() 		throws DAOException	 { return getDate("DATA_DECLARATORIA_EP"); }
    public Date 					 getDataTxAttiEstPena() 		throws DAOException	 { return getDate("DATA_TX_ATTI_EST_PENA"); }
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


  //
  // METODI SET()
  //

    public void  	 setIdEsecuzioneMisuraAlternati(BigDecimal aValore ) 			 { setBigDecimal("ID_ESECUZIONE_MISURA_ALTERNATI", aValore); }
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
    public void  	 setDataDeclaratoriaEp(Date aValore ) 			 { setDate("DATA_DECLARATORIA_EP", aValore); }
    public void  	 setDataTxAttiEstPena(Date aValore ) 			 { setDate("DATA_TX_ATTI_EST_PENA", aValore); }
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


    public GenericModel getModel() throws DAOException
    {
      return new EsecuzioneMisuraAlternativaModel(
                     getIdEsecuzioneMisuraAlternati() ,
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
                     getDataDeclaratoriaEp() ,
                     getDataTxAttiEstPena() ,
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
                     getLuogoEsecuzioneMisura()
                    );
    }

    public void setDAOFromModel(EsecuzioneMisuraAlternativaModel aModel) throws DAOException
    {
      setIdEsecuzioneMisuraAlternati( aModel.getIdEsecuzioneMisuraAlternati() );
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
      setDataDeclaratoriaEp( aModel.getDataDeclaratoriaEp() );
      setDataTxAttiEstPena( aModel.getDataTxAttiEstPena() );
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
    }


    public void setDAOFromModelForUpdate(EsecuzioneMisuraAlternativaModel aModel) throws DAOException
    {
      setIdEsecuzioneMisuraAlternati( aModel.getIdEsecuzioneMisuraAlternati() );
      setDataInizioMisura( aModel.getDataInizioMisura() );
      setDataTermineIniziale( aModel.getDataTermineIniziale() );
      setDataTermineAttuale( aModel.getDataTermineAttuale() );
      setLuogoEsecuzioneMisura( aModel.getLuogoEsecuzioneMisura() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

      setCondizioneUpdate(aModel.getIdEsecuzioneMisuraAlternati());
    }

    public void setCondizione(EsecuzioneMisuraAlternativaModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;
      if ( lInserito ) setCondition(lCondizioni);
    }


    public void setCondizioneUpdate(BigDecimal key)
    {
      setCondition(" ID_ESECUZIONE_MISURA_ALTERNATI = " + key );
    }

    public void setCondizioneDeleteByGP(BigDecimal key)
    {
      setCondition(" GEN_PRID_GENERALE_PROCEDIMENTO = " + key );
    }

    public void setDAOFromModelForUpdatebyFascicolo(EsecuzioneMisuraAlternativaModel aModel) throws DAOException
    {
      setIdEsecuzioneMisuraAlternati( aModel.getIdEsecuzioneMisuraAlternati() );
      setAnnoS07( aModel.getAnnoS07() );
      setProgrS07( aModel.getProgrS07() );
      setCodTipoMisura( aModel.getCodTipoMisura() );
      setDataOrdinanza( aModel.getDataOrdinanza() );
      setCodAutoritaEmittOrd( aModel.getCodAutoritaEmittOrd() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

      setCondizioneUpdate(aModel.getIdEsecuzioneMisuraAlternati());
    }

}
