package siap.siep.verbale.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.verbale.model.VerbaleModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: VerbaleDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Verbale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class VerbaleDAO extends TableDAO
{
	public VerbaleDAO (Connection con)
	{
             super(con);
             setTable("VERBALE");

             //Settare la Sequence e i campi chiave
             setSequenceField("ID_VERBALE", "VER_SEQ");
             setFieldKey("ID_VERBALE", BIG_DECIMAL);

             setField("ID_VERBALE", BIG_DECIMAL);
             setField("COD_TIPO_VERBALE", STRING);
             setField("DATA_EMISSIONE", DATE);
             setField("DATA_PERVENIMENTO", DATE);
             setField("COD_TIPO_UFFICIO_FIRMATARIO", STRING);
             setField("COD_LUOGO_UFFICIO_FIRMATARIO", STRING);
             setField("NOTE", STRING);
             setField("COD_OPERATORE_INSERIMENTO", STRING);
             setField("DATA_INSERIMENTO", DATE);
             setField("COD_UFFICIO_INSERIMENTO", STRING);
             setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
             setField("DATA_AGGIORNAMENTO", DATE);
             setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
             setField("EVE_ID_EVENTO", BIG_DECIMAL);
             setField("CSS_ID_CSSA", BIG_DECIMAL);
             setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
             setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
             setField("NUMERO_PROTOCOLLO", STRING);
             setField("NUM_ANNI_ESPULSIONE", BIG_DECIMAL);
             setField("NUM_MESI_ESPULSIONE", BIG_DECIMAL);
             setField("NUM_GIORNI_ESPULSIONE", BIG_DECIMAL);             
	}


  //
  // METODI GET()
  //

			public BigDecimal 	 getIdVerbale() 		throws DAOException	 { return getBigDecimal("ID_VERBALE"); }
			public String 		 getCodTipoVerbale() 		throws DAOException	 { return getString("COD_TIPO_VERBALE"); }
			public Date 		 getDataEmissione() 		throws DAOException	 { return getDate("DATA_EMISSIONE"); }
			public Date 		 getDataPervenimento() 		throws DAOException	 { return getDate("DATA_PERVENIMENTO"); }
			public String 		 getCodTipoUfficioFirmatario() 	throws DAOException	 { return getString("COD_TIPO_UFFICIO_FIRMATARIO"); }
			public String 		 getCodLuogoUfficioFirmatario() throws DAOException	 { return getString("COD_LUOGO_UFFICIO_FIRMATARIO"); }
			public String 		 getNote() 		        throws DAOException	 { return getString("NOTE"); }
			public String 		 getCodOperatoreInserimento() 	throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 		 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 		 getCodUfficioInserimento() 	throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 		 getCodOperatoreAggiornamento() throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 		 getDataAggiornamento() 	throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 		 getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public BigDecimal 	 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
			public BigDecimal 	 getCssIdCssa() 		throws DAOException	 { return getBigDecimal("CSS_ID_CSSA"); }
			public String 		 getIstDetIdIstitutoDetenzione() throws DAOException	 { return getString("IST_DET_ID_ISTITUTO_DETENZIONE"); }
			public BigDecimal 	 getFasSiuIdFascicoloSius() 	throws DAOException	 { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }
			public String        getNumeroProtocollo() throws DAOException {return getString("NUMERO_PROTOCOLLO");}

			public BigDecimal 	 getNumAnniEspulsione() 	throws DAOException	 { return getBigDecimal("NUM_ANNI_ESPULSIONE"); }
			public BigDecimal 	 getNumMesiEspulsione() 	throws DAOException	 { return getBigDecimal("NUM_MESI_ESPULSIONE"); }
			public BigDecimal 	 getNumGiorniEspulsione() 	throws DAOException	 { return getBigDecimal("NUM_GIORNI_ESPULSIONE"); }

			
  //
  // METODI SET()
  //

			public void  	 setIdVerbale(BigDecimal aValore ) 			 { setBigDecimal("ID_VERBALE", aValore); }
			public void  	 setCodTipoVerbale(String aValore ) 			 { setString("COD_TIPO_VERBALE", aValore); }
			public void  	 setDataEmissione(Date aValore ) 			 { setDate("DATA_EMISSIONE", aValore); }
			public void  	 setDataPervenimento(Date aValore ) 			 { setDate("DATA_PERVENIMENTO", aValore); }
			public void  	 setCodTipoUfficioFirmatario(String aValore ) 		 { setString("COD_TIPO_UFFICIO_FIRMATARIO", aValore); }
			public void  	 setCodLuogoUfficioFirmatario(String aValore ) 		 { setString("COD_LUOGO_UFFICIO_FIRMATARIO", aValore); }
			public void  	 setNote(String aValore ) 			         { setString("NOTE", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 		 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 		 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 		 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 		 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }
			public void  	 setCssIdCssa (BigDecimal aValore ) 			 { setBigDecimal("CSS_ID_CSSA", aValore); }
			public void  	 setIstDetIdIstitutoDetenzione (String aValore ) 	 { setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); }
			public void  	 setFasSiuIdFascicoloSius (BigDecimal aValore ) 	 { setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }
			public void      setNumeroProtocollo(String aValore)  {setString("NUMERO_PROTOCOLLO",aValore);}
			public void 	 setNumAnniEspulsione(BigDecimal aValore) 	 	 { setBigDecimal("NUM_ANNI_ESPULSIONE",aValore); }
			public void 	 setNumMesiEspulsione(BigDecimal aValore) 	 	 { setBigDecimal("NUM_MESI_ESPULSIONE",aValore); }
			public void 	 setNumGiorniEspulsione(BigDecimal aValore) 	 { setBigDecimal("NUM_GIORNI_ESPULSIONE",aValore); }

			

			public GenericModel getModel() throws DAOException
  			 {
 				 return new VerbaleModel(
								 getIdVerbale() ,
								 getCodTipoVerbale() ,
								 "",
								 getDataEmissione() ,
								 getDataPervenimento() ,
								 getCodTipoUfficioFirmatario() ,
								 "",
								 getCodLuogoUfficioFirmatario() ,
								 "",
								 getNote() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getEveIdEvento() ,
								 getCssIdCssa(),
								 getIstDetIdIstitutoDetenzione(),
								 getFasSiuIdFascicoloSius(),
								 getNumeroProtocollo(),
								 getNumAnniEspulsione(),
								 getNumMesiEspulsione(),
								 getNumGiorniEspulsione()
								);
		}


	 public void 	 setDAOFromModel(VerbaleModel aModel) throws DAOException
  		{
				 setIdVerbale( aModel.getIdVerbale() );
				 setCodTipoVerbale( aModel.getCodTipoVerbale() );
				 setDataEmissione( aModel.getDataEmissione() );
				 setDataPervenimento( aModel.getDataPervenimento() );
				 setCodTipoUfficioFirmatario( aModel.getCodTipoUfficioFirmatario() );
				 setCodLuogoUfficioFirmatario( aModel.getCodLuogoUfficioFirmatario() );
				 setNote( aModel.getNote() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 setCssIdCssa ( aModel.getCssIdCssa() );
				 setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
				 setFasSiuIdFascicoloSius (aModel.getFasSiuIdFascicoloSius());
				 setNumeroProtocollo(aModel.getNumeroProtocollo());
				 setNumAnniEspulsione(aModel.getNumAnniEspulsione());
				 setNumMesiEspulsione(aModel.getNumMesiEspulsione());
				 setNumGiorniEspulsione(aModel.getNumGiorniEspulsione()); 

  		}


	 public void 	 setDAOFromModelForUpdate(VerbaleModel aModel) throws DAOException
  		{
				 setIdVerbale( aModel.getIdVerbale() );
				 setCodTipoVerbale( aModel.getCodTipoVerbale() );
				 setDataEmissione( aModel.getDataEmissione() );
				 setDataPervenimento( aModel.getDataPervenimento() );
				 setCodTipoUfficioFirmatario( aModel.getCodTipoUfficioFirmatario() );
				 setCodLuogoUfficioFirmatario( aModel.getCodLuogoUfficioFirmatario() );
				 setNote( aModel.getNote() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 setCondizioneUpdate(aModel.getIdVerbale());
				 setCssIdCssa ( aModel.getCssIdCssa() );
				 setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
				 setFasSiuIdFascicoloSius (aModel.getFasSiuIdFascicoloSius());
				 setNumeroProtocollo(aModel.getNumeroProtocollo());
				 setNumAnniEspulsione(aModel.getNumAnniEspulsione());
				 setNumMesiEspulsione(aModel.getNumMesiEspulsione());
				 setNumGiorniEspulsione(aModel.getNumGiorniEspulsione()); 
		}


    public void setCondizione(VerbaleModel aModel) {
      String lCondizioni = new String();

      boolean lInserito = false;
      if (lInserito)
        setCondition(lCondizioni);
    }


     public void setCondizioneUpdate(BigDecimal key) {
       setCondition(" ID_VERBALE = " + key);
     }

     public void setCondizioneUpdateEveIdEvento(BigDecimal key) {
         setCondition(" EVE_ID_EVENTO = " + key);
       }

}
