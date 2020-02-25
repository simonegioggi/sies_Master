package siap.sius.esecuzionesanzionesostitutiva.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: EsecuzioneSanzioneSostitutivaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella EsecuzioneSanzioneSostitutiva</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class EsecuzioneSanzioneSostitutivaDAO extends TableDAO
{
        public EsecuzioneSanzioneSostitutivaDAO (Connection con)
        {
          super(con);
          setTable("ESECUZIONE_SANZIONE_SOST");

          this.setSequenceField("ID_ESECUZIONE_SANZIONE_SOST","ESE_SAN_SOS_SEQ");
          this.setFieldKey("ID_ESECUZIONE_SANZIONE_SOST", BIG_DECIMAL);

          setField("ID_ESECUZIONE_SANZIONE_SOST", BIG_DECIMAL);
          setField("ANNO_S07", BIG_DECIMAL);
          setField("PROGR_S07", BIG_DECIMAL);
          setField("DATA_ORDINANZA", DATE);
          setField("COD_AUTORITA_EMITT_ORD", STRING);
          setField("COD_TIPO_AUTORITA_EMITT_ORD", STRING);
          setField("COD_LUOGO_AUTORITA_EMITT_ORD", STRING);
          setField("COD_TIPO_SANZIONE", STRING);
          setField("DATA_INIZIO_SANZIONE", DATE);
          setField("DATA_TERMINE_INIZIALE", DATE);
          setField("DATA_TERMINE_ATTUALE", DATE);
          setField("DATA_DECLARATORIA_ESS", DATE);
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
          setField("LUOGO_ESECUZIONE_SANZIONE", STRING);
          setField("NUM_GIORNI_SANZIONE", BIG_DECIMAL);
          setField("NUM_MESI_SANZIONE", BIG_DECIMAL);
          setField("NUM_ANNI_SANZIONE", BIG_DECIMAL);
        }


  //
  // METODI GET()
  //

    public BigDecimal 		 getIdEsecuzioneSanzioneSost() 		throws DAOException	 { return getBigDecimal("ID_ESECUZIONE_SANZIONE_SOST"); }
    public BigDecimal 		 getAnnoS07() 		throws DAOException	 { return getBigDecimal("ANNO_S07"); }
    public BigDecimal 		 getProgrS07() 		throws DAOException	 { return getBigDecimal("PROGR_S07"); }
    public Date 					 getDataOrdinanza() 		throws DAOException	 { return getDate("DATA_ORDINANZA"); }
    public String 				 getCodAutoritaEmittOrd() 		throws DAOException	 { return getString("COD_AUTORITA_EMITT_ORD"); }
    public String 				 getCodTipoAutoritaEmittOrd() 		throws DAOException	 { return getString("COD_TIPO_AUTORITA_EMITT_ORD"); }
    public String 				 getCodLuogoAutoritaEmittOrd() 		throws DAOException	 { return getString("COD_LUOGO_AUTORITA_EMITT_ORD"); }
    public String 				 getCodTipoSanzione() 		throws DAOException	 { return getString("COD_TIPO_SANZIONE"); }
    public Date 					 getDataInizioSanzione() 		throws DAOException	 { return getDate("DATA_INIZIO_SANZIONE"); }
    public Date 					 getDataTermineIniziale() 		throws DAOException	 { return getDate("DATA_TERMINE_INIZIALE"); }
    public Date 					 getDataTermineAttuale() 		throws DAOException	 { return getDate("DATA_TERMINE_ATTUALE"); }
    public Date 					 getDataDeclaratoriaESS() 		throws DAOException	 { return getDate("DATA_DECLARATORIA_ESS"); }
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
    public String 				 getLuogoEsecuzioneSanzione() 		throws DAOException	 { return getString("LUOGO_ESECUZIONE_SANZIONE"); }
    public BigDecimal 		 getNumGiorniSanzione() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_SANZIONE"); }
    public BigDecimal 		 getNumMesiSanzione() 		throws DAOException	 { return getBigDecimal("NUM_MESI_SANZIONE"); }
    public BigDecimal 		 getNumAnniSanzione() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_SANZIONE"); }


  //
  // METODI SET()
  //

    public void  	 setIdEsecuzioneSanzioneSost(BigDecimal aValore ) 			 { setBigDecimal("ID_ESECUZIONE_SANZIONE_SOST", aValore); }
    public void  	 setAnnoS07(BigDecimal aValore ) 			 { setBigDecimal("ANNO_S07", aValore); }
    public void  	 setProgrS07(BigDecimal aValore ) 			 { setBigDecimal("PROGR_S07", aValore); }
    public void  	 setDataOrdinanza(Date aValore ) 			 { setDate("DATA_ORDINANZA", aValore); }
    public void  	 setCodAutoritaEmittOrd(String aValore ) 			 { setString("COD_AUTORITA_EMITT_ORD", aValore); }
    public void  	 setCodTipoAutoritaEmittOrd(String aValore ) 			 { setString("COD_TIPO_AUTORITA_EMITT_ORD", aValore); }
    public void  	 setCodLuogoAutoritaEmittOrd(String aValore ) 			 { setString("COD_LUOGO_AUTORITA_EMITT_ORD", aValore); }
    public void  	 setCodTipoSanzione(String aValore ) 			 { setString("COD_TIPO_SANZIONE", aValore); }
    public void  	 setDataInizioSanzione(Date aValore ) 			 { setDate("DATA_INIZIO_SANZIONE", aValore); }
    public void  	 setDataTermineIniziale(Date aValore ) 			 { setDate("DATA_TERMINE_INIZIALE", aValore); }
    public void  	 setDataTermineAttuale(Date aValore ) 			 { setDate("DATA_TERMINE_ATTUALE", aValore); }
    public void  	 setDataDeclaratoriaESS(Date aValore ) 			 { setDate("DATA_DECLARATORIA_ESS", aValore); }
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
    public void  	 setLuogoEsecuzioneSanzione(String aValore ) 			 { setString("LUOGO_ESECUZIONE_SANZIONE", aValore); }
    public void  	 setNumGiorniSanzione(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_SANZIONE", aValore); }
    public void  	 setNumMesiSanzione(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_SANZIONE", aValore); }
    public void  	 setNumAnniSanzione(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_SANZIONE", aValore); }


    public GenericModel getModel() throws DAOException
    {
      return new EsecuzioneSanzioneSostitutivaModel(
                     getIdEsecuzioneSanzioneSost() ,
                     getAnnoS07() ,
                     getProgrS07() ,
                     getDataOrdinanza() ,
                     getCodAutoritaEmittOrd() ,
                     "",
                     getCodTipoAutoritaEmittOrd() ,
                     "",
                     getCodLuogoAutoritaEmittOrd() ,
                     "",
                     getCodTipoSanzione() ,
                     "",
                     getDataInizioSanzione() ,
                     getDataTermineIniziale() ,
                     getDataTermineAttuale() ,
                     getDataDeclaratoriaESS() ,
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
                     getLuogoEsecuzioneSanzione(),
                     getNumGiorniSanzione(),
                     getNumMesiSanzione(),
                     getNumAnniSanzione()
                    );
    }

    public void setDAOFromModel(EsecuzioneSanzioneSostitutivaModel aModel) throws DAOException
    {
      setIdEsecuzioneSanzioneSost( aModel.getIdEsecuzioneSanzioneSost() );
      setAnnoS07( aModel.getAnnoS07() );
      setProgrS07( aModel.getProgrS07() );
      setDataOrdinanza( aModel.getDataOrdinanza() );
      setCodAutoritaEmittOrd( aModel.getCodAutoritaEmittOrd() );
      setCodTipoAutoritaEmittOrd( aModel.getCodTipoAutoritaEmittOrd() );
      setCodLuogoAutoritaEmittOrd( aModel.getCodLuogoAutoritaEmittOrd() );
      setCodTipoSanzione( aModel.getCodTipoSanzione() );
      setDataInizioSanzione( aModel.getDataInizioSanzione() );
      setDataTermineIniziale( aModel.getDataTermineIniziale() );
      setDataTermineAttuale( aModel.getDataTermineAttuale() );
      setDataDeclaratoriaESS( aModel.getDataDeclaratoriaESS() );
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
      setLuogoEsecuzioneSanzione( aModel.getLuogoEsecuzioneSanzione() );
      setNumGiorniSanzione( aModel.getNumGiorniSanzione() );
      setNumMesiSanzione( aModel.getNumMesiSanzione() );
      setNumAnniSanzione( aModel.getNumAnniSanzione() );
    }


    public void setDAOFromModelForUpdate(EsecuzioneSanzioneSostitutivaModel aModel) throws DAOException
    {
      setIdEsecuzioneSanzioneSost( aModel.getIdEsecuzioneSanzioneSost() );
      setDataInizioSanzione( aModel.getDataInizioSanzione() );
      setDataTermineIniziale( aModel.getDataTermineIniziale() );
      setDataTermineAttuale( aModel.getDataTermineAttuale() );
      setLuogoEsecuzioneSanzione( aModel.getLuogoEsecuzioneSanzione() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setNumAnniSanzione(aModel.getNumAnniSanzione());
      setNumMesiSanzione(aModel.getNumMesiSanzione());
      setNumGiorniSanzione(aModel.getNumGiorniSanzione());
      
      setCondizioneUpdate(aModel.getIdEsecuzioneSanzioneSost());
    }

    public void setCondizioneUpdate(BigDecimal key)
    {
      setCondition(" ID_ESECUZIONE_SANZIONE_SOST = " + key );
    }

    public void setCondizioneDeleteByGP(BigDecimal key)
    {
      setCondition(" GEN_PRID_GENERALE_PROCEDIMENTO = " + key );
    }

    public void setDAOFromModelForUpdatebyFascicolo(EsecuzioneSanzioneSostitutivaModel aModel) throws DAOException
    {
      setIdEsecuzioneSanzioneSost( aModel.getIdEsecuzioneSanzioneSost() );
      setAnnoS07( aModel.getAnnoS07() );
      setProgrS07( aModel.getProgrS07() );
      setCodTipoSanzione( aModel.getCodTipoSanzione() );
      setDataOrdinanza( aModel.getDataOrdinanza() );
      setCodAutoritaEmittOrd( aModel.getCodAutoritaEmittOrd() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

      setCondizioneUpdate(aModel.getIdEsecuzioneSanzioneSost());
    }

}
