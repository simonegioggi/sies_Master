package siap.siep.statis.dao;

/**
* <p>Title: IspProvvedimentiDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella IspProvvedimenti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.statis.model.IspProvvedimentiModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

public class IspProvvedimentiDAO extends TableDAO 
{
  public IspProvvedimentiDAO (Connection con) 
  {
       super(con);
       setTable("ISP_PROVVEDIMENTI");

       //Settare la Sequence e i campi chiave

       setField("ID_FASCICOLO_SIEP", BIG_DECIMAL);
       setField("NRES", BIG_DECIMAL);
       setField("CHIAVE_ANNO", INTEGER);
       setField("CHIAVE_PROGR", BIG_DECIMAL);
       setField("ULT_TIPO_PROVVEDIMENTO", STRING);
       setField("ULT_COD_MOTIVO", STRING);
       setField("PEN_TIPO_PROVVEDIMENTO", STRING);
       setField("PEN_COD_MOTIVO", STRING);
       setField("CONTA", INTEGER);
       setField("COD_UFFICIO", STRING);
       setField("COD_STATO_PROCEDIMENTO", STRING);
       setField("COD_STATO_FASCICOLO_RES", INTEGER);
       setField("COD_POSIZIONE_GIURIDICA", STRING);
       // NGG Statistiche SIEP
       setField("COD_UFFICIO_INSERIMENTO", STRING);
       setField("CHIAVE_PROGR_ORIG", BIG_DECIMAL);
       setField("DESC_UFFICIO_INSERIMENTO", STRING);
  }


  //
  // METODI GET()
  //

  public BigDecimal  getIdFascicoloSiep()       throws DAOException  { return getBigDecimal("ID_FASCICOLO_SIEP"); } 
  public BigDecimal  getNres()                  throws DAOException  { return getBigDecimal("NRES"); } 
  public Integer     getChiaveAnno()            throws DAOException  { return getInteger("CHIAVE_ANNO"); } 
  public BigDecimal  getChiaveProgr()           throws DAOException  { return getBigDecimal("CHIAVE_PROGR"); } 
  public String      getUltTipoProvvedimento()  throws DAOException  { return getString("ULT_TIPO_PROVVEDIMENTO"); } 
  public String      getUltCodMotivo()          throws DAOException  { return getString("ULT_COD_MOTIVO"); } 
  public String      getPenTipoProvvedimento()  throws DAOException  { return getString("PEN_TIPO_PROVVEDIMENTO"); } 
  public String      getPenCodMotivo()          throws DAOException  { return getString("PEN_COD_MOTIVO"); } 
  public Integer     getConta()                 throws DAOException  { return getInteger("CONTA"); } 
  public String      getCodUfficio()            throws DAOException  { return getString("COD_UFFICIO"); } 
  public String      getCodStatoProcedimento()  throws DAOException  { return getString("COD_STATO_PROCEDIMENTO"); } 
  public Integer     getCodStatoFascicoloRes()  throws DAOException  { return getInteger("COD_STATO_FASCICOLO_RES"); } 
  public String      getCodPosizioneGiuridica() throws DAOException  { return getString("COD_POSIZIONE_GIURIDICA"); } 
  // NGG Statistiche SIEP 
  public String     getCodUfficioInserimento()    throws DAOException  { return getString("COD_UFFICIO_INSERIMENTO"); } 
  public BigDecimal   getChiaveProgrOrig()      throws DAOException  { return getBigDecimal("CHIAVE_PROGR_ORIG"); }
  public String     getDescUfficioInserimento()   throws DAOException  { return getString("DESC_UFFICIO_INSERIMENTO"); }  

  //
  // METODI SET()
  //

  public void setIdFascicoloSiep        (BigDecimal aValore )   { setBigDecimal("ID_FASCICOLO_SIEP", aValore); } 
  public void setNres                   (BigDecimal aValore )   { setBigDecimal("NRES", aValore); } 
  public void setChiaveAnno             (Integer    aValore )   { setInteger("CHIAVE_ANNO", aValore); } 
  public void setChiaveProgr            (BigDecimal aValore )   { setBigDecimal("CHIAVE_PROGR", aValore); } 
  public void setUltTipoProvvedimento   (String aValore )       { setString("ULT_TIPO_PROVVEDIMENTO", aValore); } 
  public void setUltCodMotivo           (String aValore )       { setString("ULT_COD_MOTIVO", aValore); } 
  public void setPenTipoProvvedimento   (String aValore )       { setString("PEN_TIPO_PROVVEDIMENTO", aValore); } 
  public void setPenCodMotivo           (String aValore )       { setString("PEN_COD_MOTIVO", aValore); } 
  public void setConta                  (Integer aValore )      { setInteger("CONTA", aValore); } 
  public void setCodUfficio             (String aValore )       { setString("COD_UFFICIO", aValore); } 
  public void setCodStatoProcedimento   (String aValore )       { setString("COD_STATO_PROCEDIMENTO", aValore); } 
  public void setCodStatoFascicoloRes   (Integer aValore )      { setInteger("COD_STATO_FASCICOLO_RES", aValore); } 
  public void setCodPosizioneGiuridica  (String aValore )       { setString("COD_POSIZIONE_GIURIDICA", aValore); } 
  // NGG Statistiche SIEP
  public void   setCodUfficioInserimento(String aValore)  { setString("COD_UFFICIO_INSERIMENTO", aValore); }  
  public void   setChiaveProgrOrig(BigDecimal aValore)    { setBigDecimal("CHIAVE_PROGR_ORIG", aValore); }
  public void   setDescUfficioInserimento(String aValore)   { setString("DESC_UFFICIO_INSERIMENTO", aValore); } 


  public GenericModel getModel() throws DAOException
  { 
     return new IspProvvedimentiModel( getIdFascicoloSiep() , 
                                       getNres() , 
                                       getChiaveAnno() , 
                                       getChiaveProgr() , 
                                       getUltTipoProvvedimento() , 
                                       getUltCodMotivo() , 
                                       getPenTipoProvvedimento() , 
                                       getPenCodMotivo() , 
                                       getConta() , 
                                       getCodUfficio() , 
                                       "",
                                       getCodStatoProcedimento() , 
                                       "",
                                       getCodStatoFascicoloRes() , 
                                       "",
                                       getCodPosizioneGiuridica(),
                                       "",
                                       // NGG Statistiche SIEP      
                                       getCodUfficioInserimento(),
                                       getChiaveProgrOrig(),
                                       getDescUfficioInserimento(),
                                       null
                                      );
  }


  public void setDAOFromModel(IspProvvedimentiModel aModel) throws DAOException
  {
     setIdFascicoloSiep( aModel.getIdFascicoloSiep() );  
     setNres( aModel.getNres() );  
     setChiaveAnno( aModel.getChiaveAnno() );  
     setChiaveProgr( aModel.getChiaveProgr() );  
     setUltTipoProvvedimento( aModel.getUltTipoProvvedimento() );  
     setUltCodMotivo( aModel.getUltCodMotivo() );  
     setPenTipoProvvedimento( aModel.getPenTipoProvvedimento() );  
     setPenCodMotivo( aModel.getPenCodMotivo() );  
     setConta( aModel.getConta() );  
     setCodUfficio( aModel.getCodUfficio() );  
     setCodStatoProcedimento( aModel.getCodStatoProcedimento() );  
     setCodStatoFascicoloRes( aModel.getCodStatoFascicoloRes() );  
     setCodPosizioneGiuridica( aModel.getCodPosizioneGiuridica() );  
     //NGG Statistiche SIEP7
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setChiaveProgrOrig( aModel.getChiaveProgrOrig() );  
     setDescUfficioInserimento( aModel.getDescUfficioInserimento() );
  }


  public void selCondizione(IspProvvedimentiModel aModel)
  {
     String lCondizioni = new String(); 
    
     boolean lInserito = false; 
     if ( lInserito ) setCondition(lCondizioni); 
  }


  public void selCondizioneUpdate(Integer key) {}
  
}   // Chiude Class IspProvvedimentiDAO

