package siap.sige.udienzaparti.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PartiUdienzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ANAGRAFICA_PARTI_UDIENZA</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class PartiUdienzaDAO extends SIAPTableDAO
{
  public PartiUdienzaDAO (Connection con)
  {
	    super(con);
	    setTable("ANAGRAFICA_PARTI_UDIENZA");
	
	    //Settare la Sequence e i campi chiave
	    setSequenceField("ID_SOGGETTO", "SEQ_PARTI_UDIENZA");
	    setFieldKey("ID_SOGGETTO", BIG_DECIMAL);
	
	    setField("ID_SOGGETTO", BIG_DECIMAL);
	    setField("COD_TIPO_PART", STRING);
	    setField("COD_PARTE", STRING);
	    setField("COD_FISCALE", STRING);
	    setField("COGNOME", STRING);
	    setField("NOME", STRING);
	    setField("DENOMINAZIONE", STRING);
	    setField("DATA_NASCITA", DATE);
	    setField("COD_COMUNE_NASCITA", STRING);
	    setField("COD_PROVINCIA_NASCITA", STRING);
	    setField("COD_STATO_NASCITA", STRING);
	    setField("DESC_COMUNE_NASCITA_ESTERO", STRING);
	    setField("SESSO", STRING);
	    setField("RAG_SOCIALE", STRING);
	    setField("COD_PROVINCIA", STRING);
	    setField("IND_SEDE_LEGALE", STRING);
	    setField("IND_SEDE_OPERATIVA", STRING);
	    setField("FLG_CONV_UDIENZA", STRING);
	    setField("COD_OPERATORE_INSERIMENTO", STRING);
	    setField("DATA_INSERIMENTO", DATE);
	    setField("COD_UFFICIO_INSERIMENTO", STRING);
	    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
	    setField("DATA_AGGIORNAMENTO", DATE);
	    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
	    setField("COD_FISCALE_RAP", STRING);
	  }
	
	  //
	  // METODI GET()
	  //
	  public BigDecimal   getIdSoggetto() 			     throws DAOException  { return getBigDecimal("ID_SOGGETTO"); }
	  public String 	  getCodTipoPart() 		         throws DAOException  { return getString("COD_TIPO_PART"); }
	  public String 	  getCodParte()  			     throws DAOException  { return getString("COD_PARTE"); }
	  public String 	  getCodFiscale() 		         throws DAOException  { return getString("COD_FISCALE"); }
	  public String 	  getCognome() 			         throws DAOException  { return getString("COGNOME"); }
	  public String 	  getNome() 			         throws DAOException  { return getString("NOME"); }
	  public String 	  getDenominazione()		     throws DAOException  { return getString("DENOMINAZIONE"); }
	  public Date 	      getDataNascita() 			     throws DAOException  { return getDate("DATA_NASCITA"); }
	  public String 	  getCodComuneNascita()		     throws DAOException  { return getString("COD_COMUNE_NASCITA"); }
	  public String       getCodProvinciaNascita() 	     throws DAOException  { return getString("COD_PROVINCIA_NASCITA"); }
	  public String 	  getCodStatoNascita() 		     throws DAOException  { return getString("COD_STATO_NASCITA"); }
	  public String 	  getDescComuneNascitaEstero()   throws DAOException  { return getString("DESC_COMUNE_NASCITA_ESTERO"); }
	  public String 	  getSesso() 					 throws DAOException  { return getString("SESSO"); }
	  public String 	  getRagSociale()				 throws DAOException  { return getString("RAG_SOCIALE"); }
	  public String 	  getCodProvincia()			     throws DAOException  { return getString("COD_PROVINCIA"); }
	  public String 	  getIndSedeLegale() 			 throws DAOException  { return getString("IND_SEDE_LEGALE"); }
	  public String 	  getIndSedeOperativa() 		 throws DAOException  { return getString("IND_SEDE_OPERATIVA"); }
	  public String 	  getFlagConvUdienza() 		     throws DAOException  { return getString("FLG_CONV_UDIENZA"); }
	  public String 	  getCodOperatoreInserimento()   throws DAOException  { return getString("COD_OPERATORE_INSERIMENTO"); }
	  public Date 	      getDataInserimento()		     throws DAOException  { return getDate("DATA_INSERIMENTO"); }
	  public String 	  getCodUfficioInserimento() 	 throws DAOException  { return getString("COD_UFFICIO_INSERIMENTO"); }
	  public String 	  getCodOperatoreAggiornamento() throws DAOException  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
	  public Date 	      getDataAggiornamento()       	 throws DAOException  { return getDate("DATA_AGGIORNAMENTO"); }
	  public String 	  getCodUfficioAggiornamento()   throws DAOException  { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
	  public String 	  getCodFiscaleRap() 		     throws DAOException  { return getString("COD_FISCALE_RAP"); }
	  
	  //
	  // METODI SET()
	  //
	  public void     setIdSoggetto(BigDecimal aValore) 		   { setBigDecimal("ID_SOGGETTO", aValore); }
	  public void 	  setCodTipoPart(String aValore) 		       { setString("COD_TIPO_PART", aValore); }
	  public void 	  setCodParte(String aValore)  			       { setString("COD_PARTE", aValore); }
	  public void 	  setCodFiscale(String aValore) 		       { setString("COD_FISCALE", aValore); }
	  public void 	  setCognome(String aValore) 			       { setString("COGNOME", aValore); }
	  public void 	  setNome(String aValore) 			           { setString("NOME", aValore); }
	  public void 	  setDenominazione(String aValore)		       { setString("DENOMINAZIONE", aValore); }
	  public void 	  setDataNascita(Date aValore) 			       { setDate("DATA_NASCITA", aValore); }
	  public void 	  setCodComuneNascita(String aValore)		   { setString("COD_COMUNE_NASCITA", aValore); }
	  public void     setCodProvinciaNascita(String aValore ) 	   { setString("COD_PROVINCIA_NASCITA", aValore); }
	  public void 	  setCodStatoNascita(String aValore) 		   { setString("COD_STATO_NASCITA", aValore); }
	  public void 	  setDescComuneNascitaEstero(String aValore)   { setString("DESC_COMUNE_NASCITA_ESTERO", aValore); }
	  public void 	  setSesso(String aValore) 					   { setString("SESSO", aValore); }
	  public void 	  setRagSociale(String aValore)				   { setString("RAG_SOCIALE", aValore); }
	  public void 	  setCodProvincia(String aValore)			   { setString("COD_PROVINCIA", aValore); }
	  public void 	  setIndSedeLegale(String aValore) 			   { setString("IND_SEDE_LEGALE", aValore); }
	  public void 	  setIndSedeOperativa(String aValore) 		   { setString("IND_SEDE_OPERATIVA", aValore); }
	  public void 	  setFlagConvUdienza(String aValore) 		   { setString("FLG_CONV_UDIENZA", aValore); }
	  public void 	  setCodOperatoreInserimento(String aValore)   { setString("COD_OPERATORE_INSERIMENTO", aValore); }
	  public void 	  setDataInserimento(Date aValore)		       { setDate("DATA_INSERIMENTO", aValore); }
	  public void 	  setCodUfficioInserimento(String aValore) 	   { setString("COD_UFFICIO_INSERIMENTO", aValore); }
	  public void 	  setCodOperatoreAggiornamento(String aValore) { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
	  public void 	  setDataAggiornamento(Date aValore)       	   { setDate("DATA_AGGIORNAMENTO", aValore); }
	  public void 	  setCodUfficioAggiornamento(String aValore)   { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
	  public void 	  setCodFiscaleRap(String aValore) 		       { setString("COD_FISCALE_RAP", aValore); }
	  
	  public GenericModel getModel()
	  throws DAOException
	  {
	    return new AnagraficaPartiUdienzaModel(
	    		  getIdSoggetto(),
	    		  getCodTipoPart(),
	    		  getCodParte(),
	    		  getCodFiscale(),
	    		  getCognome(),
	    		  getNome(),
	    		  getDenominazione(),
	    		  getDataNascita(),
	    		  getCodComuneNascita(),
	    		  getCodStatoNascita(),
	    		  "",
	    		  getDescComuneNascitaEstero(),
	    		  getCodProvinciaNascita() ,
	    		  "",
	    		  getSesso(),
	    		  getRagSociale(),
	    		  getCodProvincia(),
	    		  getIndSedeLegale(),
	    		  getIndSedeOperativa(),
	    		  getFlagConvUdienza(),
	    		  getCodOperatoreInserimento(),
	    		  getDataInserimento(),
	    		  getCodUfficioInserimento(),
	    		  getCodOperatoreAggiornamento(),
	    		  getDataAggiornamento(),
	    		  getCodUfficioAggiornamento(),
	    		  getCodFiscaleRap()
	    		);
	  }

	  public void setDAOFromModel(AnagraficaPartiUdienzaModel aModel)
			  throws DAOException
	  {
		  setIdSoggetto( aModel.getIdSoggetto() );
		  setCodTipoPart( aModel.getCodTipoPart() );
		  setCodParte( aModel.getCodParte() );
		  setCodFiscale( aModel.getCodFiscale() );
		  setCognome( aModel.getCognome() );
		  setNome( aModel.getNome() );
		  setDenominazione( aModel.getDenominazione() );
		  setDataNascita( aModel.getDataNascita() );
		  setCodComuneNascita( aModel.getCodComuneNascita() );
		  setCodProvinciaNascita( aModel.getCodProvinciaNascita() );
		  setCodStatoNascita( aModel.getCodStatoNascita() );
		  setDescComuneNascitaEstero( aModel.getDescComuneNascitaEstero() );
		  setSesso( aModel.getSesso() );
		  setRagSociale( aModel.getRagSociale() );
		  setCodProvincia( aModel.getCodProvincia() );
		  setIndSedeLegale( aModel.getIndSedeLegale() );
		  setIndSedeOperativa( aModel.getIndSedeOperativa() );
		  setFlagConvUdienza( aModel.getFlagConvUdienza() );
		  setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
		  setDataInserimento( aModel.getDataInserimento() );
		  setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
		  setCodFiscaleRap( aModel.getCodFiscaleRap() );
	  }

	  public void setDAOFromModelForUpdate(AnagraficaPartiUdienzaModel aModel)
			  throws DAOException
	  {
		  setCodFiscale( aModel.getCodFiscale() );
		  setCognome( aModel.getCognome() );
		  setNome( aModel.getNome() );
		  setDenominazione( aModel.getDenominazione() );
		  setDataNascita( aModel.getDataNascita() );
		  setCodComuneNascita( aModel.getCodComuneNascita() );
		  setCodProvinciaNascita( aModel.getCodProvinciaNascita() );
		  setCodStatoNascita( aModel.getCodStatoNascita() );
		  setDescComuneNascitaEstero( aModel.getDescComuneNascitaEstero() );
		  setSesso( aModel.getSesso() );
		  setRagSociale( aModel.getRagSociale() );
		  setCodProvincia( aModel.getCodProvincia() );
		  setIndSedeLegale( aModel.getIndSedeLegale() );
		  setIndSedeOperativa( aModel.getIndSedeOperativa() );
		  setFlagConvUdienza( aModel.getFlagConvUdienza() );
		  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
		  setDataAggiornamento( aModel.getDataAggiornamento() );
		  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		  setCodFiscaleRap( aModel.getCodFiscaleRap() );
		  
		  setCondizioneUpdate(aModel.getIdSoggetto());
	  }

	  public void setCondizioneUpdate(BigDecimal IdSoggetto)
	  {
		  setCondition(" ID_SOGGETTO = " + IdSoggetto ); 
	  }

	  public void selCondizioneDeleteByKey(BigDecimal IdSoggetto)
	  {
		  setCondition(" ID_SOGGETTO = " + IdSoggetto ); 
	  }

}