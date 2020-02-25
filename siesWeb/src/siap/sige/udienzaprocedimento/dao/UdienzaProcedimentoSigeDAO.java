package siap.sige.udienzaprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import f3b.dao.DAOException;
//import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: UdienzaProcedimentoSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella UdienzaProcedimentoSige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class UdienzaProcedimentoSigeDAO extends SIAPTableDAO
{
	public UdienzaProcedimentoSigeDAO (Connection con)
	{
    super(con);
    setTable("UDIENZA_PROCEDIMENTO_SIGE");
    setFieldKey("ID_UDIENZA_PROCEDIMENTO_SIGE",BIG_DECIMAL);
  
    setSequenceField("ID_UDIENZA_PROCEDIMENTO_SIGE","UDI_PRO_SIG_SEQ");
  
    setField("ID_UDIENZA_PROCEDIMENTO_SIGE", BIG_DECIMAL);
    setField("FLAG_RINVIATA", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_ID_FASCICOLO_SIGE", BIG_DECIMAL);
    setField("UDI_ID_UDIENZA_SIGE", BIG_DECIMAL);
    setField("UDI_ID_UDIENZA_RINVIO", BIG_DECIMAL);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
	}

  //
  // METODI GET()
  //
  public BigDecimal getIdUdienzaProcedimentoSige() throws DAOException	 { 
    return getBigDecimal("ID_UDIENZA_PROCEDIMENTO_SIGE"); }
  public String 		getFlagRinviata() 		throws DAOException	 { 
    return getString("FLAG_RINVIATA"); }
  public String 		getCodOperatoreInserimento() 	throws DAOException	 { 
    return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			getDataInserimento() 		throws DAOException	 { 
    return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 	throws DAOException	 { 
    return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		getCodOperatoreAggiornamento() 	throws DAOException	 { 
    return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 			getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 		getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getFasIdFascicoloSige() 	throws DAOException	 { 
    return getBigDecimal("FAS_ID_FASCICOLO_SIGE"); }
  public BigDecimal getUdiIdUdienzaSige() 		throws DAOException	 { 
    return getBigDecimal("UDI_ID_UDIENZA_SIGE"); }
  public BigDecimal getUdiIdUdienzaRinvio() 	throws DAOException	 { 
    return getBigDecimal("UDI_ID_UDIENZA_RINVIO"); }
  public BigDecimal getEveIdEvento() 	        throws DAOException	 { 
    return getBigDecimal("EVE_ID_EVENTO"); }

  //
  // METODI SET()
  //
  public void  	 setIdUdienzaProcedimentoSige(BigDecimal aValore ) 	  { setBigDecimal("ID_UDIENZA_PROCEDIMENTO_SIGE", aValore); }
  public void  	 setFlagRinviata(String aValore ) 		  { setString("FLAG_RINVIATA", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 	  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 		  { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 	  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 	  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 		  { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 	  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setFasIdFascicoloSige(BigDecimal aValore ) 	{ setBigDecimal("FAS_ID_FASCICOLO_SIGE", aValore); }
  public void  	 setUdiIdUdienzaSige(BigDecimal aValore ) 			{ setBigDecimal("UDI_ID_UDIENZA_SIGE", aValore); }
  public void  	 setUdiIdUdienzaRinvio(BigDecimal aValore ) 		{ setBigDecimal("UDI_ID_UDIENZA_RINVIO", aValore); }
  public void  	 setEveIdEvento(BigDecimal aValore ) 		{ setBigDecimal("EVE_ID_EVENTO", aValore); }

	public GenericModel getModel() throws DAOException
        {
               return new UdienzaProcedimentoSigeModel(
                   getIdUdienzaProcedimentoSige() ,
                   getFlagRinviata() ,
                   getCodOperatoreInserimento() ,
                   getDataInserimento() ,
                   getCodUfficioInserimento() ,
                   "",
                   getCodOperatoreAggiornamento() ,
                   getDataAggiornamento() ,
                   getCodUfficioAggiornamento() ,
                   "",
                   getFasIdFascicoloSige() ,
                   getUdiIdUdienzaSige(),
                   null,
                   getUdiIdUdienzaRinvio(),
                   getEveIdEvento() );
        }


	 public void 	 setDAOFromModel(UdienzaProcedimentoSigeModel aModel) throws DAOException
   {
     setIdUdienzaProcedimentoSige( aModel.getIdUdienzaProcedimentoSige() );
     setFlagRinviata( aModel.getFlagRinviata() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setFasIdFascicoloSige( aModel.getFasIdFascicoloSige() );
     setUdiIdUdienzaSige( aModel.getUdiIdUdienzaSige() );
     setUdiIdUdienzaRinvio( aModel.getUdiIdUdienzaRinvio() );
     setEveIdEvento( aModel.getEveIdEvento() );
  }


 public void 	 setDAOFromModelForUpdate(UdienzaProcedimentoSigeModel aModel) throws DAOException
 {
         setIdUdienzaProcedimentoSige( aModel.getIdUdienzaProcedimentoSige() );
         setFlagRinviata( aModel.getFlagRinviata() );
         setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
         setDataAggiornamento( aModel.getDataAggiornamento() );
         setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
         setFasIdFascicoloSige( aModel.getFasIdFascicoloSige() );
         setUdiIdUdienzaSige( aModel.getUdiIdUdienzaSige() );
         setUdiIdUdienzaRinvio( aModel.getUdiIdUdienzaRinvio() );
         setEveIdEvento( aModel.getEveIdEvento() );

         setCondizioneUpdate(aModel.getIdUdienzaProcedimentoSige());
  }

  public void setDAOFromModelForUpdateFissazione(UdienzaProcedimentoSigeModel aModel) throws DAOException
  {
        setFlagRinviata( aModel.getFlagRinviata() );
        setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
        setDataAggiornamento( aModel.getDataAggiornamento() );
        setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
        if (aModel.getUdiIdUdienzaRinvio() != null)
          setUdiIdUdienzaRinvio( aModel.getUdiIdUdienzaRinvio() );

      setCondizioneUpdate(aModel.getIdUdienzaProcedimentoSige() );
   }

  public void setCondizione(UdienzaProcedimentoSigeModel aModel)
 {
   String lCondizioni = new String();

   boolean lInserito = false;
   if ( lInserito ) setCondition(lCondizioni);
 }


  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_UDIENZA_PROCEDIMENTO_SIGE = " + key );
  }

  public void setDAOFromModelForUpdateFlagInviata(UdienzaProcedimentoSigeModel aModel) throws DAOException
  {
    setFlagRinviata( aModel.getFlagRinviata() );
    setCondizioneUpdateFlagInviata(aModel);
  }

  public void setCondizioneUpdateFlagInviata(UdienzaProcedimentoSigeModel aModel)
  {
    setCondition(" FAS_ID_FASCICOLO_SIGE = " + aModel.getFasIdFascicoloSige() +" AND UDI_ID_UDIENZA_SIGE = " + aModel.getUdiIdUdienzaSige() );
  }

  public void setCondizioneUpdateIdUdienzaProcedimentoSige(UdienzaProcedimentoSigeModel aModel)
  {
    setCondition(" ID_UDIENZA_PROCEDIMENTO_SIGE = " + aModel.getIdUdienzaProcedimentoSige() );
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