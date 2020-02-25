package it.eng.giustizia.avvocatura.dao;

import java.sql.Connection;
import java.util.Date;

import f3b.dao.StoreProcedureDAO;
import f3b.util.DateUtils;

/**
 * <p>Title: DettaglioProcedimentoSiusStoreProcedureDAO</p>
 * <p>Description: Interfaccia  alla Stored Procedure Package AVVOCATURA_SIUS</p>
 * <p>Copyright: Copyright (c) 2005</p>
 */
public class DettaglioProcedimentoSiusStoreProcedureDAO extends StoreProcedureDAO {
  
	public DettaglioProcedimentoSiusStoreProcedureDAO(Connection lConn){
	  super(lConn);	
  }

  /**
   * Chiama la stored procedure ISPETTORATO_SIUS.stato_oggetti_sius
   */  	
	public void setStatoOggettiSius () {  
		setStoreProcedure("ISPETTORATO_SIUS.stato_oggetti_sius");
	
		//Settare  i campi Di Input e di output della Store Procedure
		this.setArgInput("COD_UFFICIO", STRING);
		this.setArgInputPosition("COD_UFFICIO",1);
		
		this.setArgInput("DATA_INIZIO", STRING);
		this.setArgInputPosition("DATA_INIZIO",2);
		
		this.setArgInput("DATA_FINE", STRING);
		this.setArgInputPosition("DATA_FINE",3);
		
		this.setArgInput("COD_CANCELLERIA", STRING);
		this.setArgInputPosition("COD_CANCELLERIA",4);
		
		this.setArgInput("FILTRO_COLLAB", STRING);
		this.setArgInputPosition("FILTRO_COLLAB",5);
		
		this.setArgInput("POSIZIONE_GIURIDICA", STRING);
		this.setArgInputPosition("POSIZIONE_GIURIDICA",6);
	
		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
	 }
 
   
  	public void setCodUfficio(String aValore ) {
	  setString("COD_UFFICIO", aValore); 
  	}
  	
  	public void setTipoUfficio(String aValore ) {
	  setString("TIPO_UFFICIO", aValore); 
  	}
  	
  	public void setDataInizio(Date aValore ) { 
	  setString("DATA_INIZIO", DateUtils.getDateToString(aValore,"dd/MM/yyyy")); 
  	}
  	
  	public void setDataFine(Date aValore ) { 
	  setString("DATA_FINE", DateUtils.getDateToString(aValore,"dd/MM/yyyy")); 
  	}  

  	public void setCodMagistrato(String aValore ) {
	  setString("COD_MAGISTRATO", aValore); 
 	}
 
  	public void setCodCancAssegnataria(String aValore ) {
	  setString("COD_CANCELLERIA", aValore); 
	}

  	public void setFiltroCollab(String aValore ) {
	  setString("FILTRO_COLLAB", aValore); 
	}

  	public void setPosizioneGiuridica(String aValore ) {
	  setString("POSIZIONE_GIURIDICA", aValore); 
	}

   	public void setCodOggetto(String aValore ) {
	  setString("COD_OGGETTO", aValore); 
	}
   	
   	public void setDescOggetto(String aValore ) {
	  setString("DESC_OGGETTO", aValore); 
	}

   	
   	
    /**
     * Chiama la stored procedure ISPETTORATO_SIUS.stato_oggetti_sius_comp_mag
     */  
  	public void setStatoOggettiSiusCompMag () {  
  		setStoreProcedure("ISPETTORATO_SIUS.stato_oggetti_sius_comp_mag");
  	
  		//Settare  i campi Di Input e di output della Store Procedure
  		this.setArgInput("COD_UFFICIO", STRING);
  		this.setArgInputPosition("COD_UFFICIO",1);
  		
  		this.setArgInput("DATA_INIZIO", STRING);
  		this.setArgInputPosition("DATA_INIZIO",2);
  		
  		this.setArgInput("DATA_FINE", STRING);
  		this.setArgInputPosition("DATA_FINE",3);
  		
  		this.setArgInput("COD_CANCELLERIA", STRING);
  		this.setArgInputPosition("COD_CANCELLERIA",4);
  		
  		this.setArgInput("FILTRO_COLLAB", STRING);
  		this.setArgInputPosition("FILTRO_COLLAB",5);
  		
  		this.setArgInput("POSIZIONE_GIURIDICA", STRING);
  		this.setArgInputPosition("POSIZIONE_GIURIDICA",6);
  	
  		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
  	 }

   	
   	
   	
   
   /**
    * Chiama la stored procedure ISPETTORATO_SIUS.creaStatisticaOggetti
    */  
   public void setCreaStatisticaOggetti () {
 	  
 		setStoreProcedure("ISPETTORATO_SIUS.creaStatisticaOggetti");

 		//Settare  i campi Di Input e di output della Store Procedure
 		this.setArgInput("COD_UFFICIO", STRING);
 		this.setArgInputPosition("COD_UFFICIO",1);
 		
 		this.setArgInput("COD_MAGISTRATO", STRING);
 		this.setArgInputPosition("COD_MAGISTRATO",2);

 		this.setArgInput("DATA_INIZIO", STRING);
 		this.setArgInputPosition("DATA_INIZIO",3);
 		
 		this.setArgInput("DATA_FINE", STRING);
 		this.setArgInputPosition("DATA_FINE",4);

 		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
   }
   
   /**
    * Chiama la stored procedure ISPETTORATO_SIUS.creaStatisticaRelatori
    */  
   public void setCreaStatisticaRelatori () {
 	  
 		setStoreProcedure("ISPETTORATO_SIUS.creaStatisticaRelatori");

 		//Settare  i campi Di Input e di output della Store Procedure
 		this.setArgInput("COD_UFFICIO", STRING);
 		this.setArgInputPosition("COD_UFFICIO",1);
 		
 		this.setArgInput("DATA_INIZIO", STRING);
 		this.setArgInputPosition("DATA_INIZIO",2);
 		
 		this.setArgInput("DATA_FINE", STRING);
 		this.setArgInputPosition("DATA_FINE",3);

 		
 		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
   }
   
   
   /**
    * Chiama la stored procedure ISPETTORATO_SIUS.creaStatisticaRelatori
    */  
   public void setCreaStatRelatoriMotOggSel() {
 	  
 		setStoreProcedure("ISPETTORATO_SIUS.creaStatRelatoriMotOggSel");

 		//Settare  i campi Di Input e di output della Store Procedure
 		this.setArgInput("COD_UFFICIO", STRING);
 		this.setArgInputPosition("COD_UFFICIO",1);
 		
 		this.setArgInput("DATA_INIZIO", STRING);
 		this.setArgInputPosition("DATA_INIZIO",2);
 		
 		this.setArgInput("DATA_FINE", STRING);
 		this.setArgInputPosition("DATA_FINE",3);

 		
 		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
   }

   
   /**
    * Chiama la stored procedure ISPETTORATO_SIUS.creaStatisticaRelatori
    */  
   public void setSvuotaConteggioOggetti () {
 	  
 		setStoreProcedure("ISPETTORATO_SIUS.svuotaConteggioOggetti");

 		//Settare  i campi Di Input e di output della Store Procedure
 		this.setArgInput("COD_UFFICIO", STRING);
 		this.setArgInputPosition("COD_UFFICIO",1);
 		
 		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
   }
   
   /**
    * Chiama la stored procedure ISPETTORATO_SIUS.estraiProcedimentiDepositati
    * per l'estrazione dei procedimenti depositati e caricamento della
    * tabella ISP_PROC_INTERVALLI
    */  
   public void setEstraiProcedimentiDepositati () {
 	  
 		setStoreProcedure("ISPETTORATO_SIUS.estraiProcedimentiDepositati");

 		//Settare  i campi Di Input e di output della Store Procedure
 		this.setArgInput("COD_UFFICIO", STRING);
 		this.setArgInputPosition("COD_UFFICIO",1);
 		
 		this.setArgInput("DATA_INIZIO", STRING);
 		this.setArgInputPosition("DATA_INIZIO",2);
 		
 		this.setArgInput("DATA_FINE", STRING);
 		this.setArgInputPosition("DATA_FINE",3);

		this.setArgInput("COD_CANCELLERIA", STRING);
		this.setArgInputPosition("COD_CANCELLERIA",4);

		this.setArgInput("FILTRO_COLLAB", STRING);
		this.setArgInputPosition("FILTRO_COLLAB",5);
		
    this.setArgInput("POSIZIONE_GIURIDICA", STRING);
    this.setArgInputPosition("POSIZIONE_GIURIDICA",6);

 		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
   }
  
   /**
    * Chiama la stored procedure ISPETTORATO_SIUS.creaStatisticaTempi
    * per l'estrazione dei tempi dei procedimenti depositati e caricamento della
    * tabella ISP_CONTEGGIO_TEMPI
    */  
   public void setCreaStatisticaTempi () {
 	  
 		setStoreProcedure("ISPETTORATO_SIUS.creaStatisticaTempi");

 		//Settare  i campi Di Input e di output della Store Procedure
 		this.setArgInput("COD_UFFICIO", STRING);
 		this.setArgInputPosition("COD_UFFICIO",1);
 		
 		this.setArgInput("COD_MAGISTRATO", STRING);
 		this.setArgInputPosition("COD_MAGISTRATO",2);

 		this.setArgInput("DATA_INIZIO", STRING);
 		this.setArgInputPosition("DATA_INIZIO",3);
 		
 		this.setArgInput("DATA_FINE", STRING);
 		this.setArgInputPosition("DATA_FINE",4);

		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
   }
   
   
   /**
    * Chiama la stored procedure ISPETTORATO_SIUS.creaOggettiSelezionati
    * per l'elenco degli oggetti selezionati  ISP_OGGETTI_SELEZIONATI
    */  
   public void setCreaOggettiSelezionati() {
 		setStoreProcedure("ISPETTORATO_SIUS.creaOggettiSelezionati");
 		
 		//Settare  i campi Di Input e di output della Store Procedure 		 		
 		this.setArgInput("COD_OGGETTO", STRING);
 		this.setArgInputPosition("COD_OGGETTO",1);

 		this.setArgInput("COD_UFFICIO", STRING);
 		this.setArgInputPosition("COD_UFFICIO",2);
 		
 		//this.setArgInput("DESC_OGGETTO", STRING);
 		//this.setArgInputPosition("DESC_OGGETTO",2);

 		//this.setArgInput("TIPO_UFFICIO", STRING);
 		//this.setArgInputPosition("TIPO_UFFICIO",3);


		//this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
   }

   
   
  /*
  public String  getReturn() throws DAOException 
  { 
	  return getOutString("RETURN"); 
  }
  */
}