package siap.siep.storicoavvocato.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: StoricoAvvocatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella StoricoAvvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class StoricoAvvocatoDAO extends TableDAO
{
	public StoricoAvvocatoDAO (Connection con)
	{
			 super(con);
			 setTable("STORICO_AVVOCATO");

			 //Settare la Sequence e i campi chiave
       setSequenceField("ID_STORICO_AVVOCATO", "STO_AVV_SEQ");

       setFieldKey("ID_STORICO_AVVOCATO", BIG_DECIMAL);

			// setField("ID_STORICO_AVVOCATO", BIG_DECIMAL);
			 setField("COGNOME", STRING);
			 setField("NOME", STRING);
			 setField("FORO", STRING);
			 setField("INDIRIZZO", STRING);
			 setField("TELEFONO", STRING);
			 setField("FAX", STRING);
			 setField("E_MAIL", STRING);
			 setField("COD_FISCALE", STRING);
			 setField("FLAG_VISUALIZZA", BIG_DECIMAL);
			 setField("COD_COMUNE_RESIDENZA", STRING);
			 setField("COD_LUOGO_NASCITA", STRING);
			 setField("DATA_NASCITA", DATE);
			 setField("DATA_SOSPESO_FINO_AL", DATE);
			 setField("DATA_RADIATO_DAL", DATE);
			 setField("COD_NON_ATTIVITA", STRING);
			 setField("NOTE", STRING);
			 setField("COD_UFFICIO_APPARTENENZA", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("FLAG_CANCELLATO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("PROVINCIA", STRING);
			 setField("CAP", STRING);
			 setField("ID_AVVOCATO_STANDARD", BIG_DECIMAL);
			 setField("AVV_ID_AVVOCATO", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdStoricoAvvocato() 		throws DAOException	 { return getBigDecimal("ID_STORICO_AVVOCATO"); }
			public String 				 getCognome() 		throws DAOException	 { return getString("COGNOME"); }
			public String 				 getNome() 		throws DAOException	 { return getString("NOME"); }
			public String 				 getForo() 		throws DAOException	 { return getString("FORO"); }
			public String 				 getIndirizzo() 		throws DAOException	 { return getString("INDIRIZZO"); }
			public String 				 getTelefono() 		throws DAOException	 { return getString("TELEFONO"); }
			public String 				 getFax() 		throws DAOException	 { return getString("FAX"); }
			public String 				 getEMail() 		throws DAOException	 { return getString("E_MAIL"); }
			public String 				 getCodiceFiscale() 		throws DAOException	 { return getString("COD_FISCALE"); }
			public BigDecimal 				 getFlagVisualizza() 		throws DAOException	 { return getBigDecimal("FLAG_VISUALIZZA"); }
			public String 				 getCodComuneResidenza() 		throws DAOException	 { return getString("COD_COMUNE_RESIDENZA"); }
			public String 				 getCodLuogoNascita() 		throws DAOException	 { return getString("COD_LUOGO_NASCITA"); }
			public Date 					 getDataNascita() 		throws DAOException	 { return getDate("DATA_NASCITA"); }
			public Date 					 getDataSospesoFinoAl() 		throws DAOException	 { return getDate("DATA_SOSPESO_FINO_AL"); }
			public Date 					 getDataRadiatoDal() 		throws DAOException	 { return getDate("DATA_RADIATO_DAL"); }
			public String 				 getCodNonAttivita() 		throws DAOException	 { return getString("COD_NON_ATTIVITA"); }
			public String 				 getNote() 		throws DAOException	 { return getString("NOTE"); }
			public String 				 getCodUfficioAppartenenza() 		throws DAOException	 { return getString("COD_UFFICIO_APPARTENENZA"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public String 				 getProvincia() 		throws DAOException	 { return getString("PROVINCIA"); }
			public String 				 getCap() 		throws DAOException	 { return getString("CAP"); }
			public BigDecimal 				 getIdAvvocatoStandard() 		throws DAOException	 { return getBigDecimal("ID_AVVOCATO_STANDARD"); }
			public BigDecimal 		 getAvvIdAvvocato() 		throws DAOException	 { return getBigDecimal("AVV_ID_AVVOCATO"); }
      public String 				 getFlagCancellato() 		throws DAOException	 { return getString("FLAG_CANCELLATO"); }



  //
  // METODI SET()
  //

			public void  	 setIdStoricoAvvocato(BigDecimal aValore ) 			 { setBigDecimal("ID_STORICO_AVVOCATO", aValore); }
			public void  	 setCognome(String aValore ) 			 { setString("COGNOME", aValore); }
			public void  	 setNome(String aValore ) 			 { setString("NOME", aValore); }
			public void  	 setForo(String aValore ) 			 { setString("FORO", aValore); }
			public void  	 setIndirizzo(String aValore ) 			 { setString("INDIRIZZO", aValore); }
			public void  	 setTelefono(String aValore ) 			 { setString("TELEFONO", aValore); }
			public void  	 setFax(String aValore ) 			 { setString("FAX", aValore); }
			public void  	 setEMail(String aValore ) 			 { setString("E_MAIL", aValore); }
			public void  	 setCodiceFiscale(String aValore ) 			 { setString("COD_FISCALE", aValore); }
			public void  	 setFlagVisualizza(BigDecimal aValore ) 			 { setBigDecimal("FLAG_VISUALIZZA", aValore); }
			public void  	 setCodComuneResidenza(String aValore ) 			 { setString("COD_COMUNE_RESIDENZA", aValore); }
			public void  	 setCodLuogoNascita(String aValore ) 			 { setString("COD_LUOGO_NASCITA", aValore); }
			public void  	 setDataNascita(Date aValore ) 			 { setDate("DATA_NASCITA", aValore); }
			public void  	 setDataSospesoFinoAl(Date aValore ) 			 { setDate("DATA_SOSPESO_FINO_AL", aValore); }
			public void  	 setDataRadiatoDal(Date aValore ) 			 { setDate("DATA_RADIATO_DAL", aValore); }
			public void  	 setCodNonAttivita(String aValore ) 			 { setString("COD_NON_ATTIVITA", aValore); }
			public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
			public void  	 setCodUfficioAppartenenza(String aValore ) 			 { setString("COD_UFFICIO_APPARTENENZA", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setProvincia(String aValore ) 			 { setString("PROVINCIA", aValore); }
			public void  	 setCap(String aValore ) 			 { setString("CAP", aValore); }
			public void  	 setIdAvvocatoStandard(BigDecimal aValore ) 			 { setBigDecimal("ID_AVVOCATO_STANDARD", aValore); }		
			public void  	 setAvvIdAvvocato(BigDecimal aValore ) 			 { setBigDecimal("AVV_ID_AVVOCATO", aValore); }

      public void		setFlagCancellato(String aValore ) 			 { setString("FLAG_CANCELLATO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new StoricoAvvocatoModel(
								 getIdStoricoAvvocato() ,
								 getCognome() ,
								 getNome() ,
								 getForo() ,
								 getIndirizzo() ,
								 getTelefono() ,
								 getFax() ,
								 getEMail() ,
								 getCodiceFiscale(),
								 getFlagVisualizza(),
								 getCodComuneResidenza() ,
								 "",
								 getCodLuogoNascita() ,
								 "",
								 getDataNascita() ,
								 getDataSospesoFinoAl() ,
								 getDataRadiatoDal() ,
								 getCodNonAttivita() ,
								 "",
								 getNote() ,
								 getCodUfficioAppartenenza() ,
								 "",
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 getProvincia(),
								 getCap(),
								 getIdAvvocatoStandard(),
								 getAvvIdAvvocato(),
                 getFlagCancellato()
								);
		}


	 public void 	 setDAOFromModel(StoricoAvvocatoModel aModel) throws DAOException
  		{
				 setIdStoricoAvvocato( aModel.getIdStoricoAvvocato() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setForo( aModel.getForo() );
				 setIndirizzo( aModel.getIndirizzo() );
				 setTelefono( aModel.getTelefono() );
				 setFax( aModel.getFax() );
				 setEMail( aModel.getEMail() );
				 setCodiceFiscale( aModel.getCodiceFiscale() );
				 setFlagVisualizza( aModel.getFlagVisualizza() );
				 setCodComuneResidenza( aModel.getCodComuneResidenza() );
				 setCodLuogoNascita( aModel.getCodLuogoNascita() );
				 setDataNascita( aModel.getDataNascita() );
				 setDataSospesoFinoAl( aModel.getDataSospesoFinoAl() );
				 setDataRadiatoDal( aModel.getDataRadiatoDal() );
				 setCodNonAttivita( aModel.getCodNonAttivita() );
				 setNote( aModel.getNote() );
				 setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setProvincia( aModel.getProvincia() );
				 setCap( aModel.getCap() );
				 setIdAvvocatoStandard( aModel.getIdAvvocatoStandard() );
				 setAvvIdAvvocato( aModel.getAvvIdAvvocato() );
         setFlagCancellato( aModel.getFlagCancellato() );
		}


	 public void 	 setDAOFromModelForUpdate(StoricoAvvocatoModel aModel) throws DAOException
  		{
				 setIdStoricoAvvocato( aModel.getIdStoricoAvvocato() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setForo( aModel.getForo() );
				 setIndirizzo( aModel.getIndirizzo() );
				 setTelefono( aModel.getTelefono() );
				 setFax( aModel.getFax() );
				 setEMail( aModel.getEMail() );
				 setCodiceFiscale( aModel.getCodiceFiscale() );
				 setFlagVisualizza( aModel.getFlagVisualizza() );
				 setCodComuneResidenza( aModel.getCodComuneResidenza() );
				 setCodLuogoNascita( aModel.getCodLuogoNascita() );
				 setDataNascita( aModel.getDataNascita() );
				 setDataSospesoFinoAl( aModel.getDataSospesoFinoAl() );
				 setDataRadiatoDal( aModel.getDataRadiatoDal() );
				 setCodNonAttivita( aModel.getCodNonAttivita() );
				 setNote( aModel.getNote() );
				 setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setProvincia( aModel.getProvincia() );
				 setCap( aModel.getCap() );
				 setIdAvvocatoStandard( aModel.getIdAvvocatoStandard() );
				 setFlagCancellato( aModel.getFlagCancellato() );
				 setAvvIdAvvocato( aModel.getAvvIdAvvocato() );
		 setCondizioneUpdate(aModel.getIdStoricoAvvocato());
		}


	public void setCondizione(StoricoAvvocatoModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_STORICO_AVVOCATO = " + key );
		 }

}
