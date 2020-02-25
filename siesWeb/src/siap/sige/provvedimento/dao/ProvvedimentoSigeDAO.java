package siap.sige.provvedimento.dao;

/**
* <p>Title: ProvvedimentoSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ProvvedimentoSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

public class ProvvedimentoSigeDAO extends TableDAO 
{
	public ProvvedimentoSigeDAO (Connection con) 
	{
		 super(con);
		 setTable("PROVVEDIMENTO_SIGE");

		 //Settare la Sequence e i campi chiave
		 setSequenceField("ID_PROVVEDIMENTO_SIGE", "PROV_SIGE_SEQ");
		 setFieldKey("ID_PROVVEDIMENTO_SIGE", BIG_DECIMAL);

		 setField("ID_PROVVEDIMENTO_SIGE", BIG_DECIMAL);
		 setField("UDI_ID_UDIENZA_SIGE", BIG_DECIMAL);
		 setField("FAS_ID_FASCICOLO_SIGE", BIG_DECIMAL);
		 setField("ID_EVENTO_GENERATO", BIG_DECIMAL);
		 setField("CHIAVE_ANNO", BIG_DECIMAL);
		 setField("CHIAVE_PROGR", BIG_DECIMAL);
		 setField("CHIAVE_UFFICIO", STRING);
		 setField("DATA_EMISSIONE", DATE);
		 setField("DATA_DEPOSITO", DATE);
		 setField("COD_TIPO_PROVVEDIMENTO", STRING);
		 setField("COD_OPERATORE_INSERIMENTO", STRING);
		 setField("COD_UFFICIO_INSERIMENTO", STRING);
		 setField("DATA_INSERIMENTO", DATE);
		 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		 setField("DATA_AGGIORNAMENTO", DATE);
		 setField("DEFINITORIO", STRING);
		 setField("FLAG_ORDINE_TRADUZIONE", STRING);
		 setField("LUOGO_SVOLGIMENTO", STRING);
		 setField("COL_ID_COLLEGIO", BIG_DECIMAL);
		 setField("COD_TIPO_PROVVEDIMENTO_SIGE", STRING);
		 setField("COD_UFFICIO_DESTINATARIO", STRING);	// 08/01/2010
		 setField("COD_UFFICIO_DESTINATARIO", STRING);	
		 setField("COD_UFFICIO_DEST_CASS", STRING);
		 setField("NOTE", STRING);											// 08/01/2010
		 setField("PROVV_ID_PROVVEDIMENTO_SIGE", BIG_DECIMAL); // 21-10-2010

	}


  	//
  	// METODI GET()
  	//
    public BigDecimal		getIdProvvedimentoSige() 		throws DAOException	 { return getBigDecimal("ID_PROVVEDIMENTO_SIGE"); } 
    public BigDecimal 		getFasIdFascicoloSige() 		throws DAOException	 { return getBigDecimal("FAS_ID_FASCICOLO_SIGE"); } 
    public BigDecimal 		getIdEventoGenerato() 		throws DAOException	 { return getBigDecimal("ID_EVENTO_GENERATO"); }
    public BigDecimal 		getUdiIdUdienzaSige() 		throws DAOException	 { return getBigDecimal("UDI_ID_UDIENZA_SIGE"); }     
    public BigDecimal 		getChiaveAnno() 		throws DAOException	 { return getBigDecimal("CHIAVE_ANNO"); } 
    public BigDecimal 		getChiaveProgr() 		throws DAOException	 { return getBigDecimal("CHIAVE_PROGR"); } 
    public String			getChiaveUfficio() 		throws DAOException	 { return getString("CHIAVE_UFFICIO"); } 
    public Date 		 	getDataEmissione() 		throws DAOException	 { return getDate("DATA_EMISSIONE"); } 
    public Date 		 	getDataDeposito() 		throws DAOException	 { return getDate("DATA_DEPOSITO"); } 
    public String 			getCodTipoProvvedimento() 		throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO"); } 
    public String 			getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
    public String 			getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
    public Date 			getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
    public String 			getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
    public String 			getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
    public Date 			getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
    public String 			getDefinitorio() 		throws DAOException	 { return getString("DEFINITORIO"); } 
    public String 			getFlagOrdineTraduzione() 		throws DAOException	 { return getString("FLAG_ORDINE_TRADUZIONE"); } 
    public String 			getLuogoSvolgimento() 		throws DAOException	 { return getString("LUOGO_SVOLGIMENTO"); } 
    public BigDecimal 		getColIdCollegio() 		throws DAOException	 { return getBigDecimal("COL_ID_COLLEGIO"); } 
    public String 			getCodTipoProvvedimentoSige() 		throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO_SIGE"); } 
    public String 			getCodUfficioDestinatario() 		throws DAOException	 { return getString("COD_UFFICIO_DESTINATARIO"); }	// 08/01/2010 
    public String 			getCodUffCompCorteSuprema() 		throws DAOException	 { return getString("COD_UFFICIO_DEST_CASS"); }
    public String 			getNote() 		throws DAOException	 { return getString("NOTE"); }	// 08/01/2010 
    public BigDecimal		getProvvIdProvvedimentoSige() 		throws DAOException	 { return getBigDecimal("PROVV_ID_PROVVEDIMENTO_SIGE"); } 
   
    //
    // METODI SET()
    //
  	public void  	 setIdProvvedimentoSige(BigDecimal aValore ) 	{ setBigDecimal("ID_PROVVEDIMENTO_SIGE", aValore); } 
  	public void  	 setFasIdFascicoloSige(BigDecimal aValore ) 	{ setBigDecimal("FAS_ID_FASCICOLO_SIGE", aValore); } 
  	public void  	 setIdEventoGenerato(BigDecimal aValore ) 		{ setBigDecimal("ID_EVENTO_GENERATO", aValore); } 
  	public void  	 setUdiIdUdienzaSige(BigDecimal aValore ) 		{ setBigDecimal("UDI_ID_UDIENZA_SIGE", aValore); }  	
  	public void  	 setChiaveAnno(BigDecimal aValore )						{ setBigDecimal("CHIAVE_ANNO", aValore); } 
  	public void  	 setChiaveProgr(BigDecimal aValore ) 			 		{ setBigDecimal("CHIAVE_PROGR", aValore); } 
  	public void  	 setChiaveUfficio(String aValore ) 			 			{ setString("CHIAVE_UFFICIO", aValore); } 
  	public void  	 setDataEmissione(Date aValore ) 			 				{ setDate("DATA_EMISSIONE", aValore); } 
  	public void  	 setDataDeposito(Date aValore ) 			 				{ setDate("DATA_DEPOSITO", aValore); } 
  	public void  	 setCodTipoProvvedimento(String aValore ) 		{ setString("COD_TIPO_PROVVEDIMENTO", aValore); } 
  	public void  	 setCodOperatoreInserimento(String aValore ) 	{ setString("COD_OPERATORE_INSERIMENTO", aValore); } 
  	public void  	 setCodUfficioInserimento(String aValore ) 		{ setString("COD_UFFICIO_INSERIMENTO", aValore); } 
  	public void  	 setDataInserimento(Date aValore ) 			 	 		{ setDate("DATA_INSERIMENTO", aValore); } 
  	public void  	 setCodOperatoreAggiornamento(String aValore ){ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  	public void  	 setCodUfficioAggiornamento(String aValore ) 	{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
  	public void  	 setDataAggiornamento(Date aValore ) 			 		{ setDate("DATA_AGGIORNAMENTO", aValore); } 
  	public void  	 setDefinitorio(String aValore ) 			 	 			{ setString("DEFINITORIO", aValore); } 
  	public void  	 setFlagOrdineTraduzione(String aValore )			{ setString("FLAG_ORDINE_TRADUZIONE", aValore); } 
  	public void  	 setLuogoSvolgimento(String aValore )					{ setString("LUOGO_SVOLGIMENTO", aValore); } 
  	public void  	 setColIdCollegio(BigDecimal aValore ) 			 	{ setBigDecimal("COL_ID_COLLEGIO", aValore); } 
  	public void  	 setCodTipoProvvedimentoSige(String aValore )	{ setString("COD_TIPO_PROVVEDIMENTO_SIGE", aValore); } 
  	public void  	 setCodUfficioDestinatario(String aValore )		{ setString("COD_UFFICIO_DESTINATARIO", aValore); } // 08/01/2010
  	public void  	 setCodUffCompCorteSuprema(String aValore )		{ setString("COD_UFFICIO_DEST_CASS", aValore); }  	
  	public void  	 setNote(String aValore )											{ setString("NOTE", aValore); } // 08/01/2010
  	public void  	 setProvvIdProvvedimentoSige(BigDecimal aValore ) 	{ setBigDecimal("PROVV_ID_PROVVEDIMENTO_SIGE", aValore); } 
  	
  	public GenericModel getModel() throws DAOException
  	{ 
 				 return new ProvvedimentoSigeModel(  
								 getIdProvvedimentoSige() , 
								 getFasIdFascicoloSige() , 
								 getIdEventoGenerato() , 
								 getUdiIdUdienzaSige(),
								 getChiaveAnno() , 
								 getChiaveProgr() , 
								 getChiaveUfficio() , 
								 getDataEmissione() , 
								 getDataDeposito() , 
								 getCodTipoProvvedimento() , 
								 "",
								 getCodOperatoreInserimento() , 
								 getCodUfficioInserimento() , 
								 "",
								 getDataInserimento() , 
								 getCodOperatoreAggiornamento() , 
								 getCodUfficioAggiornamento() , 
								 "",
								 getDataAggiornamento(),
								 getDefinitorio(),
								 getFlagOrdineTraduzione(),
								 getLuogoSvolgimento(),
								 getColIdCollegio(),
								 getCodTipoProvvedimentoSige(), 
								 "",
								 getCodUfficioDestinatario(),
								 getCodUffCompCorteSuprema(),
								 "",
								 "",
								 getNote(),
								 getProvvIdProvvedimentoSige() 
								);
		}

	 public void setDAOFromModel(ProvvedimentoSigeModel aModel) throws DAOException
  		{
				 setIdProvvedimentoSige( aModel.getIdProvvedimentoSige() );  
				 setFasIdFascicoloSige( aModel.getFasIdFascicoloSige() );  
				 setIdEventoGenerato( aModel.getIdEventoGenerato() );  
				 setUdiIdUdienzaSige( aModel.getUdiIdUdienzaSige() );  
				 setChiaveAnno( aModel.getChiaveAnno() );  
				 setChiaveProgr( aModel.getChiaveProgr() );  
				 setChiaveUfficio( aModel.getChiaveUfficio() );  
				 setDataEmissione( aModel.getDataEmissione() );  
				 setDataDeposito( aModel.getDataDeposito() );  
				 setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );  
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
				 setDataInserimento( aModel.getDataInserimento() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setDefinitorio( aModel.getDefinitorio() );  
				 setFlagOrdineTraduzione( aModel.getFlagOrdineTraduzione() );  
				 setLuogoSvolgimento( aModel.getLuogoSvolgimento() );  
				 setColIdCollegio(aModel.getColIdCollegio());
				 setCodTipoProvvedimentoSige( aModel.getCodTipoProvvedimentoSige() );  
				 setCodUfficioDestinatario( aModel.getCodUfficioDestinatario() );
				 setCodUffCompCorteSuprema(aModel.getCodUffCompCorteSuprema());
				 setNote(aModel.getNote());
				 setProvvIdProvvedimentoSige( aModel.getProvvIdProvvedimentoSige() );  
		}
	 
	 
	 public void setDAOFromModelForUpdate(ProvvedimentoSigeModel aModel) throws DAOException
	{
				 setDataEmissione( aModel.getDataEmissione() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				// setFlagOrdineTraduzione( aModel.getFlagOrdineTraduzione() );  
				 setLuogoSvolgimento( aModel.getLuogoSvolgimento() );  
				 setColIdCollegio(aModel.getColIdCollegio());
				 setCodUfficioDestinatario( aModel.getCodUfficioDestinatario() );
				 setCodUffCompCorteSuprema( aModel.getCodUffCompCorteSuprema());				 
				 setUdiIdUdienzaSige( aModel.getUdiIdUdienzaSige());			 
				 setNote(aModel.getNote());
				 if (aModel.getIdEventoGenerato() != null && aModel.getCodTipoProvvedimentoSige() != null && aModel.getCodTipoProvvedimentoSige().compareTo("10")==0)
					 setIdEventoGenerato(aModel.getIdEventoGenerato());  // Per Ordinzanza di Sospensione
				 selCondizioneByKey(aModel.getIdProvvedimentoSige());
	} 
	 
	 
	 
	 
/**
 * 
 * @param aModel
 */
	public void selCondizione(ProvvedimentoSigeModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
		 
		 if(aModel.getFasIdFascicoloSige() != null)
		 {
			 lCondizioni += " FAS_ID_FASCICOLO_SIGE = " + aModel.getFasIdFascicoloSige();
			 lInserito = true; 
		 }
		 if(aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0)
		 {
			 if (lInserito)
				 lCondizioni += " AND ";			 
			 lCondizioni += " COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' ";
			 lInserito = true; 
		 }
 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}

  public void selCondizioneByKey(BigDecimal aIdProvvedimento)
  { 
  	setCondition(" ID_PROVVEDIMENTO_SIGE = " + aIdProvvedimento);
  }
  
  public void selCondizioneByProvvId(BigDecimal aProvvIdProvvedimento)
  { 
  	setCondition(" PROVV_ID_PROVVEDIMENTO_SIGE = " + aProvvIdProvvedimento);
  }

  public void selCondizioneByIdEvento(BigDecimal aIdEvento)
  {
  	setCondition(" ID_EVENTO_GENERATO = " + aIdEvento);
  }
  
  
  public void selCondizioneProvDefinitorioByFasc(BigDecimal aIdFascSige)
  {
  	setCondition(" FAS_ID_FASCICOLO_SIGE = " + aIdFascSige + " AND DEFINITORIO = 'S'");
  }
  
  public void selCondizioneByIdTenore(BigDecimal aIdTenoreSige)
  {
  	setCondition(" ID_PROVVEDIMENTO_SIGE = (SELECT PROV_ID_PROVVEDIMENTO_SIGE FROM TENORE_SIGE WHERE ID_TENORE_SIGE = " + aIdTenoreSige + ")");
  }
}
