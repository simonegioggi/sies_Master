package siap.siep.statis.dao;

import java.sql.Connection;

import f3b.dao.StoreProcedureDAO;

/**
 * <p>Title: StatisStoreProcedureDAO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 */
public class StatisStoreProcedureDAO
extends StoreProcedureDAO
{
  public StatisStoreProcedureDAO(Connection lConn)
  {
		super(lConn);	
  }

  /**
   * Chiama la stored procedure ISPETTORATO.stat_provvedimenti
   */  
  public void setStatProvvedimentiStoreProcedure () {
	  
		setStoreProcedure("ISPETTORATO.stat_provvedimenti");

		//Settare  i campi di Input e di output della Store Procedure
		this.setArgInput("COD_UFFICIO_INSERIMENTO", STRING);
		this.setArgInputPosition("COD_UFFICIO_INSERIMENTO",1);
		
		// NGG - Inseriti altri 3 parametri ufficio accorpato
		
		this.setArgInput("COD_UFFICIO_ACCORPATO1", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO1",2);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO2", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO2",3);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO3", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO3",4);
		
		// END NGG

		this.setArgInput("DATA_VERIFICA", STRING);
		this.setArgInputPosition("DATA_VERIFICA",5);

		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
  }

  public void setAttivitaMagistratiStoreProcedure () {
	  
		setStoreProcedure("ISPETTORATO.Attivita_Magistrati");

		//Settare  i campi Di Input e di output della Store Procedure
		this.setArgInput("DATA_INIZIO", STRING);
		this.setArgInputPosition("DATA_INIZIO",1);
		
		this.setArgInput("DATA_FINE", STRING);
		this.setArgInputPosition("DATA_FINE",2);
		
		this.setArgInput("COD_UFFICIO_INSERIMENTO", STRING);
		this.setArgInputPosition("COD_UFFICIO_INSERIMENTO",3);
		// NGG - Inseriti altri 3 parametri ufficio accorpato
		/*
		this.setArgInput("COD_UFFICIO_ACCORPATO1", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO1",4);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO2", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO2",5);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO3", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO3",6);		
		*/
		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
  }

  public void setTempiIscrizioneStoreProcedure () {
	  
		setStoreProcedure("ISPETTORATO.tempi_iscrizione");

		//Settare  i campi Di Input e di output della Store Procedure
		this.setArgInput("DATA_INIZIO", STRING);
		this.setArgInputPosition("DATA_INIZIO",1);
		
		this.setArgInput("DATA_FINE", STRING);
		this.setArgInputPosition("DATA_FINE",2);
		
		this.setArgInput("COD_UFFICIO_INSERIMENTO", STRING);
		this.setArgInputPosition("COD_UFFICIO_INSERIMENTO",3);
		
		// NGG - Inseriti altri 3 parametri ufficio accorpato
/*		
		this.setArgInput("COD_UFFICIO_ACCORPATO1", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO1",4);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO2", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO2",5);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO3", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO3",6);
*/
		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
  }

  public void setTempiEmissioneStoreProcedure () {
	  
		setStoreProcedure("ISPETTORATO.tempi_emissione");

		//Settare  i campi Di Input e di output della Store Procedure
		this.setArgInput("DATA_INIZIO", STRING);
		this.setArgInputPosition("DATA_INIZIO",1);
		
		this.setArgInput("DATA_FINE", STRING);
		this.setArgInputPosition("DATA_FINE",2);
		
		this.setArgInput("COD_UFFICIO_INSERIMENTO", STRING);
		this.setArgInputPosition("COD_UFFICIO_INSERIMENTO",3);
		
		// NGG - Inseriti altri 3 parametri ufficio accorpato
		
		this.setArgInput("COD_UFFICIO_ACCORPATO1", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO1",4);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO2", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO2",5);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO3", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO3",6);

		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
  } 
  
  // MEV 27
  public void setRiepilogoIscrizioniCPPStoreProcedure () {
	  
		setStoreProcedure("ISPETTORATO_CPP.riepilogo_iscrizioni_CPP");

		//Settare  i campi Di Input e di output della Store Procedure
		this.setArgInput("DATA_INIZIO", STRING);
		this.setArgInputPosition("DATA_INIZIO",1);
		
		this.setArgInput("DATA_FINE", STRING);
		this.setArgInputPosition("DATA_FINE",2);
		
		this.setArgInput("COD_UFFICIO_INSERIMENTO", STRING);
		this.setArgInputPosition("COD_UFFICIO_INSERIMENTO",3);
		
		// NGG - Inseriti altri 3 parametri ufficio accorpato
/*		
		this.setArgInput("COD_UFFICIO_ACCORPATO1", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO1",4);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO2", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO2",5);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO3", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO3",6);
*/
		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
}
  
  /**
   * Chiama la stored procedure ISPETTORATO_CPP.stat_provvedimenti_CPP
   */  
  public void setStatProvvedimenti_CPP_StoreProcedure () {
	  
		setStoreProcedure("ISPETTORATO_CPP.stat_provvedimenti_CPP");

		//Settare  i campi di Input e di output della Store Procedure
		this.setArgInput("COD_UFFICIO_INSERIMENTO", STRING);
		this.setArgInputPosition("COD_UFFICIO_INSERIMENTO",1);
		
		// NGG - Inseriti altri 3 parametri ufficio accorpato
		
		this.setArgInput("COD_UFFICIO_ACCORPATO1", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO1",2);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO2", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO2",3);
		
		this.setArgInput("COD_UFFICIO_ACCORPATO3", STRING);
		this.setArgInputPosition("COD_UFFICIO_ACCORPATO3",4);
		
		// END NGG

		this.setArgInput("DATA_VERIFICA", STRING);
		this.setArgInputPosition("DATA_VERIFICA",5);

		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
  }

  //	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	
  //-------	-----------	-------------	----------------	---------
  
  public void setCodUfficioInserimento(String aValore ) 	 
  	{
	  setString("COD_UFFICIO_INSERIMENTO", aValore); 
  	}

   public void setDataVerifica(String aValore ) 	 
  	{ 
	  setString("DATA_VERIFICA", aValore); 
  	}
   public void setDataInizio(String aValore ) 	 
  	{ 
	  setString("DATA_INIZIO", aValore); 
  	}
   public void setDataFine(String aValore ) 	 
  	{ 
	  setString("DATA_FINE", aValore); 
  	}
   // NGG
   public void setCodUfficioAccorpato_1(String aValore ) 	 
 	{
	  setString("COD_UFFICIO_ACCORPATO1", aValore); 
 	}
   public void setCodUfficioAccorpato_2(String aValore ) 	 
	{
	  setString("COD_UFFICIO_ACCORPATO2", aValore); 
	}
   public void setCodUfficioAccorpato_3(String aValore ) 	 
	{
	  setString("COD_UFFICIO_ACCORPATO3", aValore); 
	}
 
  /*
  public String  getReturn() throws DAOException 
  { 
	  return getOutString("RETURN"); 
  }
  */
}