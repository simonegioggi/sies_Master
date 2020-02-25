package siap.sius.udienzaprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import f3b.dao.DAOException;
//import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: UdienzaProcedimentoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class UdienzaProcedimentoDAO extends SIAPTableDAO
{
	public UdienzaProcedimentoDAO (Connection con)
	{
          super(con);
          setTable("UDIENZA_PROCEDIMENTO");
          setFieldKey("ID_UDIENZA_PROCEDIMENTO",BIG_DECIMAL);

          setSequenceField("ID_UDIENZA_PROCEDIMENTO","UDI_PRO_SEQ");

          setField("ID_UDIENZA_PROCEDIMENTO", BIG_DECIMAL);
          setField("FLAG_RINVIATA", STRING);
          setField("COD_OPERATORE_INSERIMENTO", STRING);
          setField("DATA_INSERIMENTO", DATE);
          setField("COD_UFFICIO_INSERIMENTO", STRING);
          setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
          setField("DATA_AGGIORNAMENTO", DATE);
          setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
          setField("GEN_PRID_GENERALE_PROCEDIMENTO", BIG_DECIMAL);
          setField("UDI_ID_UDIENZA", BIG_DECIMAL);
          setField("UDI_ID_UDIENZA_RINVIO", BIG_DECIMAL);
          setField("EVE_ID_EVENTO", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

        public BigDecimal 	getIdUdienzaProcedimento() 	throws DAOException	 { return getBigDecimal("ID_UDIENZA_PROCEDIMENTO"); }
        public String 		getFlagRinviata() 		throws DAOException	 { return getString("FLAG_RINVIATA"); }
        public String 		getCodOperatoreInserimento() 	throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
        public Date 		getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
        public String 		getCodUfficioInserimento() 	throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
        public String 		getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
        public Date 		getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
        public String 		getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
        public BigDecimal 	getGenPridGeneraleProcedimento() 	throws DAOException	 { return getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"); }
        public BigDecimal 	getUdiIdUdienza() 		throws DAOException	 { return getBigDecimal("UDI_ID_UDIENZA"); }
        public BigDecimal 	getUdiIdUdienzaRinvio() 	throws DAOException	 { return getBigDecimal("UDI_ID_UDIENZA_RINVIO"); }
        public BigDecimal 	getEveIdEvento() 	        throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }


  //
  // METODI SET()
  //

        public void  	 setIdUdienzaProcedimento(BigDecimal aValore ) 	  { setBigDecimal("ID_UDIENZA_PROCEDIMENTO", aValore); }
        public void  	 setFlagRinviata(String aValore ) 		  { setString("FLAG_RINVIATA", aValore); }
        public void  	 setCodOperatoreInserimento(String aValore ) 	  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
        public void  	 setDataInserimento(Date aValore ) 		  { setDate("DATA_INSERIMENTO", aValore); }
        public void  	 setCodUfficioInserimento(String aValore ) 	  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
        public void  	 setCodOperatoreAggiornamento(String aValore ) 	  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
        public void  	 setDataAggiornamento(Date aValore ) 		  { setDate("DATA_AGGIORNAMENTO", aValore); }
        public void  	 setCodUfficioAggiornamento(String aValore ) 	  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
        public void  	 setGenPridGeneraleProcedimento(BigDecimal aValore ) 	{ setBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO", aValore); }
        public void  	 setUdiIdUdienza(BigDecimal aValore ) 			{ setBigDecimal("UDI_ID_UDIENZA", aValore); }
        public void  	 setUdiIdUdienzaRinvio(BigDecimal aValore ) 		{ setBigDecimal("UDI_ID_UDIENZA_RINVIO", aValore); }
        public void  	 setEveIdEvento(BigDecimal aValore ) 		{ setBigDecimal("EVE_ID_EVENTO", aValore); }


	public GenericModel getModel() throws DAOException
        {
               return new UdienzaProcedimentoModel(
                                               getIdUdienzaProcedimento() ,
                                               getFlagRinviata() ,
                                               getCodOperatoreInserimento() ,
                                               getDataInserimento() ,
                                               getCodUfficioInserimento() ,
                                               "",
                                               getCodOperatoreAggiornamento() ,
                                               getDataAggiornamento() ,
                                               getCodUfficioAggiornamento() ,
                                               "",
                                               getGenPridGeneraleProcedimento() ,
                                               getUdiIdUdienza(),
                                               getUdiIdUdienzaRinvio(),
                                               getEveIdEvento()
                                              );
        }


	 public void 	 setDAOFromModel(UdienzaProcedimentoModel aModel) throws DAOException
         {
                   setIdUdienzaProcedimento( aModel.getIdUdienzaProcedimento() );
                   setFlagRinviata( aModel.getFlagRinviata() );
                   setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
                   setDataInserimento( aModel.getDataInserimento() );
                   setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
                   setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
                   setDataAggiornamento( aModel.getDataAggiornamento() );
                   setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
                   setGenPridGeneraleProcedimento( aModel.getGenPridGeneraleProcedimento() );
                   setUdiIdUdienza( aModel.getUdiIdUdienza() );
                   setUdiIdUdienzaRinvio( aModel.getUdiIdUdienzaRinvio() );
                   setEveIdEvento( aModel.getEveIdEvento() );
        }


 public void 	 setDAOFromModelForUpdate(UdienzaProcedimentoModel aModel) throws DAOException
 {
           setIdUdienzaProcedimento( aModel.getIdUdienzaProcedimento() );
           setFlagRinviata( aModel.getFlagRinviata() );
           setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
           setDataAggiornamento( aModel.getDataAggiornamento() );
           setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
           setGenPridGeneraleProcedimento( aModel.getGenPridGeneraleProcedimento() );
           setUdiIdUdienza( aModel.getUdiIdUdienza() );
           setUdiIdUdienzaRinvio( aModel.getUdiIdUdienzaRinvio() );
           setEveIdEvento( aModel.getEveIdEvento() );

         setCondizioneUpdate(aModel.getIdUdienzaProcedimento());
  }

  public void 	 setDAOFromModelForUpdateFissazione(UdienzaProcedimentoModel aModel) throws DAOException
  {
        setFlagRinviata( aModel.getFlagRinviata() );
        setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
        setDataAggiornamento( aModel.getDataAggiornamento() );
        setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
        if (aModel.getUdiIdUdienzaRinvio() != null)
          setUdiIdUdienzaRinvio( aModel.getUdiIdUdienzaRinvio() );

      setCondizioneUpdate(aModel.getIdUdienzaProcedimento());
   }





  public void setCondizione(UdienzaProcedimentoModel aModel)
 {
   String lCondizioni = new String();

   boolean lInserito = false;
   if ( lInserito ) setCondition(lCondizioni);
 }


  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_UDIENZA_PROCEDIMENTO = " + key );
  }

  public void setDAOFromModelForUpdateFlagInviata(UdienzaProcedimentoModel aModel) throws DAOException
  {
    setFlagRinviata( aModel.getFlagRinviata() );
    setCondizioneUpdateFlagInviata(aModel);
  }

  public void setCondizioneUpdateFlagInviata(UdienzaProcedimentoModel aModel)
  {
    setCondition(" GEN_PRID_GENERALE_PROCEDIMENTO = " + aModel.getGenPridGeneraleProcedimento() +" AND UDI_ID_UDIENZA = " + aModel.getUdiIdUdienza() );
  }

  public void setCondizioneUpdateIdUdienzaProcedimento(UdienzaProcedimentoModel aModel)
  {
    setCondition(" ID_UDIENZA_PROCEDIMENTO = " + aModel.getIdUdienzaProcedimento() );
  }
  
  /**
   * Imposta filtro di condizione per id evento.
   * <p>
   * @param aKey Id evento.
   */
  public void setCondizioneByIdEvento( BigDecimal aKey )
  {
    setCondition(" EVE_ID_EVENTO = " + aKey );
  }
}