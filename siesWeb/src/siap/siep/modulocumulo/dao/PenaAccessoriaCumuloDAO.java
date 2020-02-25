package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PenaAccessoriaCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PenaAccessoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class PenaAccessoriaCumuloDAO extends SIAPTableDAO
{
	public PenaAccessoriaCumuloDAO (Connection con)
	{
    super(con);
    setTable("PENA_ACCESSORIA_CUMULO");

    setSequenceField("ID_PENA_ACCESSORIA_CUMULO", "PEN_ACC_CUM_SEQ");

    setFieldKey("ID_PENA_ACCESSORIA_CUMULO", BIG_DECIMAL);

    setField("COD_TIPO_PENA_ACCESSORIA", STRING);
    setField("DESCR_ALTRE_PA", STRING);
    setField("COD_TIPO_DURATA", STRING);
    setField("NUM_ANNI", BIG_DECIMAL);
    setField("NUM_MESI", BIG_DECIMAL);
    setField("NUM_GIORNI", BIG_DECIMAL);
        
    setField("NOTE", STRING);
    setField("BEN_ID_BENEFICIO_ORIG", BIG_DECIMAL);
    setField("BEN_ID_BENEFICIO_CUMULO", BIG_DECIMAL);
    
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);

 /*   setField("ANNO_ORDINANZA_GE", BIG_DECIMAL);
    setField("NUMERO_ORDINANZA_GE", BIG_DECIMAL);
    setField("DATA_ORDINANZA_GE", DATE);
    setField("COD_TIPO_UFFICIO_ORDINANZA_GE", STRING);
    setField("COD_LUOGO_UFFICIO_ORDINANZA_GE", STRING);
 */   
    setField("TIT_ID_TITOLO_CUMULATO"     , BIG_DECIMAL);
    setField("FLAG_STATO"                 , STRING);
    setField("MOTIVO_MODIFICA"            , STRING);
    setField("ID_PENA_ACCESSORIA_ORIGINE", BIG_DECIMAL);
    setField("FLAG_DATI_FINALI"              , STRING);

	}

  //
  // METODI GET()
  //
  public BigDecimal getIdPenaAccessoriaCumulo() 	throws DAOException	  			{ return getBigDecimal("ID_PENA_ACCESSORIA_CUMULO"); }
  public String 	getCodTipoPenaAccessoria() 		throws DAOException	  			{ return getString("COD_TIPO_PENA_ACCESSORIA"); }
  public String     getDescrTipoPenaAccessoria()    throws DAOException   			{ return getString("DESCR_TIPO_PENA_ACCESSORIA"); }
  public String 	getDurata() 					throws DAOException        		{ return getString("COD_TIPO_DURATA"); }
  public String 	getDescrDurata() 				throws DAOException        		{ return getString("DESCR_DURATA"); }
  public BigDecimal getNumAnni() 					throws DAOException	                { return getBigDecimal("NUM_ANNI"); }
  public BigDecimal getNumMesi() 					throws DAOException	                { return getBigDecimal("NUM_MESI"); }
  public BigDecimal getNumGiorni() 					throws DAOException	              	{ return getBigDecimal("NUM_GIORNI"); }
  public String 	getDescrAltrePA() 				throws DAOException	                { return getString("DESCR_ALTRE_PA"); }  

  public String 	getNote() 						throws DAOException	        { return getString("NOTE"); }
  public BigDecimal getBenIdBeneficioOrig()			throws DAOException			{ return getBigDecimal("BEN_ID_BENEFICIO_ORIG"); }
  public BigDecimal getBenIdBeneficioCumulo()		throws DAOException			{ return getBigDecimal("BEN_ID_BENEFICIO_CUMULO"); }
  
  public String 	getCodOperatoreInserimento() 		throws DAOException		{ return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 		getDataInserimento() 				throws DAOException		{ return getDate("DATA_INSERIMENTO"); }
  public String 	getCodUfficioInserimento() 			throws DAOException		{ return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 	getCodOperatoreAggiornamento() 		throws DAOException		{ return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 		getDataAggiornamento() 				throws DAOException		{ return getDate("DATA_AGGIORNAMENTO"); }
  public String 	getCodUfficioAggiornamento() 		throws DAOException		{ return getString("COD_UFFICIO_AGGIORNAMENTO"); }
 
/*  public BigDecimal getAnnoOrdinanzaGE() 				throws DAOException	    { return getBigDecimal("ANNO_ORDINANZA_GE"); }
  public BigDecimal getNumeroOrdinanzaGE() 				throws DAOException     { return getBigDecimal("NUMERO_ORDINANZA_GE"); }
  public Date 		getDataOrdinanzaGE() 	      		throws DAOException	    { return getDate("DATA_ORDINANZA_GE"); }
  public String 	getCodTipoUfficioOrdinanzaGE()  	throws DAOException	    { return getString("COD_TIPO_UFFICIO_ORDINANZA_GE"); }
  public String 	getDescrTipoUfficioOrdinanzaGE()  	throws DAOException	    { return getString("DESCR_TIPO_UFFICIO_ORDINANZA_GE"); }
  public String 	getCodLuogoUfficioOrdinanzaGE() 	throws DAOException	    { return getString("COD_LUOGO_UFFICIO_ORDINANZA_GE"); }
  public String 	getDescrLuogoUfficioOrdinanzaGE()  	throws DAOException	    { return getString("DESCR_LUOGO_UFFICIO_ORDINANZA_GE"); }
*/
  public BigDecimal getTitIdTitoloCumulato() 			throws DAOException     { return getBigDecimal("TIT_ID_TITOLO_CUMULATO"); }
  public String		getMotivoModifica()					throws DAOException     { return getString("MOTIVO_MODIFICA"); }	
  public String		getFlagStato()						throws DAOException     { return getString("FLAG_STATO"); }	
  public BigDecimal getIdPenaAccessoriaOrigine() 		throws DAOException    	{ return getBigDecimal("ID_PENA_ACCESSORIA_ORIGINE"); }
  public  String    getFlagDatiFinali()             throws DAOException  { return getString     ("FLAG_DATI_FINALI"              ); } 
 
  //
  // METODI SET()
  //
  public void setIdPenaAccessoriaCumulo(BigDecimal aValore ) 			{ setBigDecimal("ID_PENA_ACCESSORIA_CUMULO", aValore); }
  public void setCodTipoPenaAccessoria(String aValore ) 			    { setString("COD_TIPO_PENA_ACCESSORIA", aValore); }
  public void setDescrTipoPenaAccessoria(String aValore ) 			    { setString("DESCR_TIPO_PENA_ACCESSORIA", aValore); }
  public void setDurata(String aValore ) 			                    { setString("COD_TIPO_DURATA", aValore); }
  public void setDescrDurata(String aValore ) 			                { setString("DESCR_DURATA", aValore); }
  public void setNumAnni(BigDecimal aValore ) 			                { setBigDecimal("NUM_ANNI", aValore); }
  public void setNumMesi(BigDecimal aValore ) 			                { setBigDecimal("NUM_MESI", aValore); }
  public void setNumGiorni(BigDecimal aValore ) 			            { setBigDecimal("NUM_GIORNI", aValore); }
  public void setDescrAltrePA(String aValore ) 		                  	{ setString("DESCR_ALTRE_PA", aValore); }    
  public void setNote(String aValore ) 			                        { setString("NOTE", aValore); }
  public void setBenIdBeneficioOrig(BigDecimal aValore)					{ setBigDecimal("BEN_ID_BENEFICIO_ORIG", aValore); }
  public void setBenIdBeneficioCumulo(BigDecimal aValore)				{ setBigDecimal("BEN_ID_BENEFICIO_CUMULO", aValore); }
  
  public void setCodOperatoreInserimento(String aValore ) 			    { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			            { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 			    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 			{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			            { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 			    { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }

/*  public void setAnnoOrdinanzaGE(BigDecimal aValore ) 			    { setBigDecimal("ANNO_ORDINANZA_GE", aValore); }
  public void setNumeroOrdinanzaGE(BigDecimal aValore )		        { setBigDecimal("NUMERO_ORDINANZA_GE", aValore); }
  public void setDataOrdinanzaGE(Date aValore ) 			        { setDate("DATA_ORDINANZA_GE", aValore); }
  public void setCodTipoUfficioOrdinanza_GE(String aValore ) 		{ setString("COD_TIPO_UFFICIO_ORDINANZA_GE", aValore); }
  public void setCodLuogoUfficioOrdinanza_GE(String aValore ) 		{ setString("COD_LUOGO_UFFICIO_ORDINANZA_GE", aValore); }
*/
  public void setTitIdTitoloCumulato(BigDecimal aValore ) 	        {  setBigDecimal("TIT_ID_TITOLO_CUMULATO", aValore); }
  public void setMotivoModifica(String aValore)			         	{  setString("MOTIVO_MODIFICA", aValore); }	
  public void setFlagStato(String aValore)				         	{  setString("FLAG_STATO", aValore ); }	
  public void setIdPenaAccessoriaOrigine(BigDecimal aValore ) 	 	{ setBigDecimal("ID_PENA_ACCESSORIA_ORIGINE", aValore); }
  public void  setFlagDatiFinali             (String      aValore )   { setString     ("FLAG_DATI_FINALI"              , aValore); } 

  
	public GenericModel getModel() throws DAOException
  {
    return new PenaAccessoriaCumuloModel(
                                    getIdPenaAccessoriaCumulo() ,
                                    getCodTipoPenaAccessoria() ,
                                    "",
                                    getDescrAltrePA(),
                                    
                                    getDurata() ,
                                    "",
                                    getNumAnni() ,
                                    getNumMesi() ,
                                    getNumGiorni() ,
                             /*       
                                    getAnnoOrdinanzaGE() ,
                                    getNumeroOrdinanzaGE() ,
                                    getDataOrdinanzaGE() ,
                                    getCodTipoUfficioOrdinanzaGE() ,
                                    "",
                                    getCodLuogoUfficioOrdinanzaGE(),
                                    "",
                               */     
                                    getNote(),
                                    getBenIdBeneficioOrig(),
                                    getBenIdBeneficioCumulo(),
                                    
                                    getFlagStato(),
                                    getMotivoModifica(),
                                    getTitIdTitoloCumulato(),
                                    getIdPenaAccessoriaOrigine(),
                                    getFlagDatiFinali(),
                                    
                                    getCodOperatoreInserimento() ,
                                    getDataInserimento() ,
                                    getCodUfficioInserimento() ,
                                    getCodOperatoreAggiornamento() ,
                                    getDataAggiornamento() ,
                                    getCodUfficioAggiornamento()
                                    
                                    );
  }

  public void 	 setDAOFromModel(PenaAccessoriaCumuloModel aModel) throws DAOException
  {
    setIdPenaAccessoriaCumulo( aModel.getIdPenaAccessoriaCumulo() );
    setCodTipoPenaAccessoria( aModel.getCodTipoPenaAccessoria() );
    setDescrTipoPenaAccessoria(aModel.getDescrTipoPenaAccessoria() );
    setDurata( aModel.getDurata() );
   // ""
    setNumAnni( aModel.getNumAnni() );
    setNumMesi( aModel.getNumMesi() );
    setNumGiorni( aModel.getNumGiorni() );
    setDescrAltrePA( aModel.getDescrAltrePA() );
    setNote( aModel.getNote() );
    setBenIdBeneficioOrig( aModel.getBenIdBeneficioOrig() );
    setBenIdBeneficioCumulo( aModel.getBenIdBeneficioCumulo() );
    
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

   // setAnnoOrdinanzaGE( aModel.getAnnoOrdinanzaGE() );
   // setNumeroOrdinanzaGE( aModel.getNumeroOrdinanzaGE() );
   // setDataOrdinanzaGE( aModel.getDataOrdinanzaGE() );
   // setCodTipoUfficioOrdinanza_GE( aModel.getCodTipoUfficioOrdinanzaGE() );
   // ""
   // setCodLuogoUfficioOrdinanza_GE( aModel.getCodLuogoUfficioOrdinanzaGE() );
   // ""
    
    setMotivoModifica(aModel.getMotivoModifica() );
    setFlagStato(aModel.getFlagStato() );
    setTitIdTitoloCumulato(aModel.getTitIdTitoloCumulato() );
    setIdPenaAccessoriaOrigine( aModel.getIdPenaAccessoriaOrigine() );
    setFlagDatiFinali         ( aModel.getFlagDatiFinali()); 
    
  }

  public void setDAOFromModelForUpdate(PenaAccessoriaCumuloModel aModel) throws DAOException
  {
    setIdPenaAccessoriaCumulo( aModel.getIdPenaAccessoriaCumulo() );
    setCodTipoPenaAccessoria( aModel.getCodTipoPenaAccessoria() );
    setDurata( aModel.getDurata() );
    setNumAnni( aModel.getNumAnni() );
    setNumMesi( aModel.getNumMesi() );
    setNumGiorni( aModel.getNumGiorni() );
    setDescrAltrePA( aModel.getDescrAltrePA() );   
    setNote( aModel.getNote() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

 /*   setAnnoOrdinanzaGE( aModel.getAnnoOrdinanzaGE() );
    setNumeroOrdinanzaGE( aModel.getNumeroOrdinanzaGE() );
    setDataOrdinanzaGE( aModel.getDataOrdinanzaGE() );
    setCodTipoUfficioOrdinanza_GE( aModel.getCodTipoUfficioOrdinanzaGE() );
    setCodLuogoUfficioOrdinanza_GE( aModel.getCodLuogoUfficioOrdinanzaGE() );
*/
    setBenIdBeneficioCumulo(aModel.getBenIdBeneficioCumulo());
    setMotivoModifica(aModel.getMotivoModifica() );
    setFlagStato(aModel.getFlagStato() );
    setTitIdTitoloCumulato(aModel.getTitIdTitoloCumulato() );
    setIdPenaAccessoriaOrigine( aModel.getIdPenaAccessoriaOrigine() );

  }

    public void setDAOFromModelForUpdateOrdinanzaPA(PenaAccessoriaModel aModel) throws DAOException
    {
      if(aModel.getDescrAltrePA().trim() !="" )
        setDescrAltrePA( aModel.getDescrAltrePA()) ;

 /*     if(aModel.getFlagCondonata().compareTo("-")!=0)
      {
        setFlagCondonata( aModel.getFlagCondonata() );
        setDataOrdinanzaPA( aModel.getDataOrdinanzaPA() );
        setAnnoOrdinanzaPA( aModel.getAnnoOrdinanzaPA() );
        setNumeroOrdinanzaPA( aModel.getNumeroOrdinanzaPA() );
        setCodTipoUfficioOrdinanzaPA( aModel.getCodTipoUfficioOrdinanzaPA() );
        setCodLuogoUfficioOrdinanzaPA( aModel.getCodLuogoUfficioOrdinanzaPA() );
        setCodFonteGE(aModel.getCodFonteGE());
        setAnnoFonteGE(aModel.getAnnoFonteGE());
        setNumeroFonteGE(aModel.getNumeroFonteGE());
        setArticoloGE(aModel.getArticoloGE());
        setCodSottonumerazioneGE(aModel.getCodSottonumerazioneGE());
        setCommaGE(aModel.getCommaGE());
        setLetteraGE(aModel.getLetteraGE());
        setNumeroGE(aModel.getNumeroGE());
      }
*/      

/*      if(aModel.getCodNuovoTipoPenaAccessoria().compareTo("-")!=0)
      {
        setCodNuovoTipoPenaAccessoria( aModel.getCodNuovoTipoPenaAccessoria() );
        //setDurata( aModel.getDurata() );
        //setNumAnni( aModel.getNumAnni() );
        //setNumMesi( aModel.getNumMesi() );
        //setNumGiorni( aModel.getNumGiorni() );

        if ((aModel.getFlagCondonata().compareTo("S")==0)  ||
            (aModel.getFlagCondonata().compareTo("T")==0) )
        {
          setDataFineValidita(aModel.getDataAggiornamento());
        }
      }
*/
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

    }

    public void setDAOFromModelForUpdateCodNuovoTipoPA(PenaAccessoriaModel aModel) throws DAOException
    {
    //  setCodNuovoTipoPenaAccessoria( aModel.getCodNuovoTipoPenaAccessoria() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    }

	public void selCondizione(PenaAccessoriaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }
	
	public void selCondizioneUpdateBenIdBeneficioFascSiep(BigDecimal keyBen,BigDecimal keyFasc)
 	{
      String lCondizioni = " BEN_ID_BENEFICIO = " + keyBen;
      lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + keyFasc;
      setCondition(lCondizioni);
    }
	
	public void selCondizioneUpdateBenIdBeneficioCumTitoloCum(BigDecimal keyBen,BigDecimal keyTitolo)
 	{
      String lCondizioni = " BEN_ID_BENEFICIO_CUMULO = " + keyBen;
      lCondizioni += " AND TIT_ID_TITOLO_CUMULATO = " + keyTitolo;
      setCondition(lCondizioni);
    }
	
	public void selCondizioneUpdate(BigDecimal key)
 	{
      String lCondizioni = " ID_PENA_ACCESSORIA_CUMULO = " + key;

     setCondition(lCondizioni);
    }
	
  public void selCondizioneUpdateByIdTitolo( BigDecimal aIdTitolo)
  {
     String lCondizioni = " TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

     setCondition(lCondizioni);
  }	
	
	public void selCondizioneFascSiep(BigDecimal key)
 	{
      String lCondizioni = " FAS_SIE_ID_FASCICOLO_SIEP = " + key;

     setCondition(lCondizioni);
    }

	
}
