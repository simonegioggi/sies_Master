package siap.siep.alias.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.alias.model.AliasModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AliasDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Alias</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AliasDAO extends TableDAO
{
	public AliasDAO (Connection con)
	{
			 super(con);
			 setTable("ALIAS");

			 //Settare la Sequence e i campi chiave

			 setField("ID_ALIAS", BIG_DECIMAL);
			 setField("COGNOME", STRING);
			 setField("NOME", STRING);
			 setField("PATERNITA", STRING);
			 setField("COD_FISCALE", STRING);
			 setField("COD_CS", STRING);
			 setField("COD_AFIS", STRING);
			 setField("ATTO_NASCITA", STRING);
			 setField("SESSO", STRING);
			 setField("COD_COMUNE_NASCITA", STRING);
			 setField("COD_PROVINCIA_NASCITA", STRING);
       setField("DESC_COMUNE_NASCITA_ESTERO", STRING);
			 setField("COD_STATO_NASCITA", STRING);
			 setField("DATA_NASCITA", DATE);
			 setField("NOTE", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("SOG_ID_SOGGETTO", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdAlias() 		throws DAOException	 { return getBigDecimal("ID_ALIAS"); }
			public String 				 getCognome() 		throws DAOException	 { return getString("COGNOME"); }
			public String 				 getNome() 		throws DAOException	 { return getString("NOME"); }
			public String 				 getPaternita() 		throws DAOException	 { return getString("PATERNITA"); }
			public String 				 getCodFiscale() 		throws DAOException	 { return getString("COD_FISCALE"); }
			public String 				 getCodCs() 		throws DAOException	 { return getString("COD_CS"); }
			public String 				 getCodAfis() 		throws DAOException	 { return getString("COD_AFIS"); }
			public String 				 getAttoNascita() 		throws DAOException	 { return getString("ATTO_NASCITA"); }
			public String 				 getSesso() 		throws DAOException	 { return getString("SESSO"); }
			public String 				 getCodComuneNascita() 		throws DAOException	 { return getString("COD_COMUNE_NASCITA"); }
			public String 				 getCodProvinciaNascita() 		throws DAOException	 { return getString("COD_PROVINCIA_NASCITA"); }
			public String 				 getCodStatoNascita() 		throws DAOException	 { return getString("COD_STATO_NASCITA"); }
			public Date 					 getDataNascita() 		throws DAOException	 { return getDate("DATA_NASCITA"); }
			public String 				 getNote() 		throws DAOException	 { return getString("NOTE"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public BigDecimal 		 getSogIdSoggetto() 		throws DAOException	 { return getBigDecimal("SOG_ID_SOGGETTO"); }
			public String 				 getDescComuneNascitaEstero() 		throws DAOException	 { return getString("DESC_COMUNE_NASCITA_ESTERO"); }


  //
  // METODI SET()
  //

			public void  	 setIdAlias(BigDecimal aValore ) 			 { setBigDecimal("ID_ALIAS", aValore); }
			public void  	 setCognome(String aValore ) 			 { setString("COGNOME", aValore); }
			public void  	 setNome(String aValore ) 			 { setString("NOME", aValore); }
			public void  	 setPaternita(String aValore ) 			 { setString("PATERNITA", aValore); }
			public void  	 setCodFiscale(String aValore ) 			 { setString("COD_FISCALE", aValore); }
			public void  	 setCodCs(String aValore ) 			 { setString("COD_CS", aValore); }
			public void  	 setCodAfis(String aValore ) 			 { setString("COD_AFIS", aValore); }
			public void  	 setAttoNascita(String aValore ) 			 { setString("ATTO_NASCITA", aValore); }
			public void  	 setSesso(String aValore ) 			 { setString("SESSO", aValore); }
			public void  	 setCodComuneNascita(String aValore ) 			 { setString("COD_COMUNE_NASCITA", aValore); }
			public void  	 setCodProvinciaNascita(String aValore ) 			 { setString("COD_PROVINCIA_NASCITA", aValore); }
			public void  	 setCodStatoNascita(String aValore ) 			 { setString("COD_STATO_NASCITA", aValore); }
			public void  	 setDataNascita(Date aValore ) 			 { setDate("DATA_NASCITA", aValore); }
			public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setSogIdSoggetto(BigDecimal aValore ) 			 { setBigDecimal("SOG_ID_SOGGETTO", aValore); }
		  public void  	 setDescComuneNascitaEstero(String aValore ) 			 { setString("DESC_COMUNE_NASCITA_ESTERO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new AliasModel(
								 getIdAlias() ,
								 getCognome() ,
								 getNome() ,
								 getPaternita() ,
								 getCodFiscale() ,
								 "",
								 getCodCs() ,
								 "",
								 getCodAfis() ,
								 "",
								 getAttoNascita() ,
								 getSesso() ,
								 getCodComuneNascita() ,
								 "",
								 getCodProvinciaNascita() ,
								 "",
								 getCodStatoNascita() ,
								 "",
								 getDataNascita() ,
								 getNote() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getSogIdSoggetto(),
                 getDescComuneNascitaEstero()
								);
		}


	 public void 	 setDAOFromModel(AliasModel aModel) throws DAOException
  		{
				 setIdAlias( aModel.getIdAlias() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setPaternita( aModel.getPaternita() );
				 setCodFiscale( aModel.getCodFiscale() );
				 setCodCs( aModel.getCodCs() );
				 setCodAfis( aModel.getCodAfis() );
				 setAttoNascita( aModel.getAttoNascita() );
				 setSesso( aModel.getSesso() );
				 setCodComuneNascita( aModel.getCodComuneNascita() );
				 setCodProvinciaNascita( aModel.getCodProvinciaNascita() );
				 setCodStatoNascita( aModel.getCodStatoNascita() );
				 setDataNascita( aModel.getDataNascita() );
				 setNote( aModel.getNote() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setSogIdSoggetto( aModel.getSogIdSoggetto() );
         setDescComuneNascitaEstero(aModel.getDescComuneNascitaEstero());
		}


	 public void 	 setDAOFromModelForUpdate(AliasModel aModel) throws DAOException
  		{
				 setIdAlias( aModel.getIdAlias() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setPaternita( aModel.getPaternita() );
				 setCodFiscale( aModel.getCodFiscale() );
				 setCodCs( aModel.getCodCs() );
				 setCodAfis( aModel.getCodAfis() );
				 setAttoNascita( aModel.getAttoNascita() );
				 setSesso( aModel.getSesso() );
				 setCodComuneNascita( aModel.getCodComuneNascita() );
				 setCodProvinciaNascita( aModel.getCodProvinciaNascita() );
				 setCodStatoNascita( aModel.getCodStatoNascita() );
				 setDataNascita( aModel.getDataNascita() );
				 setNote( aModel.getNote() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setSogIdSoggetto( aModel.getSogIdSoggetto() );
	       setDescComuneNascitaEstero(aModel.getDescComuneNascitaEstero());

         setCondizioneUpdate(aModel.getIdAlias());
  	}


	public void setCondizione(AliasModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_ALIAS = " + key );
		 }

}
