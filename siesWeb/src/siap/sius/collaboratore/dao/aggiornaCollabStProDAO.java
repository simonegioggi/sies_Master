package siap.sius.collaboratore.dao;

import java.sql.Connection;

import siap.sius.collaboratore.model.CollaboratoreModel;
import f3b.dao.StoreProcedureDAO;
import f3b.util.DateUtils;
import f3b.util.StringUtils;



/**
 * <p>Title: aggiornaCollabStProcDAO</p>
 * <p>Description:</p>
 * <p> La classe costituisce l'interfaccia alla funzione PlSql aggiornaCollab():
 * <p> Schema : COLLA
 * <p>Nome del package : COLL
 * <p>  procedure aggiornaCollab 
 * <p>  ( par_id IN   number,
 * <p>    par_data_iniziale IN   VARCHAR2,
 * <p>	  par_data_finale  IN   VARCHAR2,
 * <p>    par_utente IN varchar2,
 * <p>	  par_ufficio_utente IN varchar2)
 * <p> La funzione effettua l'aggiornamento del record Collaboratore individuato dall'ID par_id.
 * <p> I dati da aggiornare sono data_iniziale e data_finale.
 * <p>Copyright: Copyright (c) 2008</p>
 */
public class aggiornaCollabStProDAO extends StoreProcedureDAO
{
  public aggiornaCollabStProDAO(Connection lConn, CollaboratoreModel aCollaboratore)
  {
    super(lConn);
    
    // Nome della Stored Procedure 
    setStoreProcedure("COLLA.COLL.aggiornaCollab");
   
    // Definizione degli argomenti 
	
	setArgInput("ID", BIG_DECIMAL);
	setArgInputPosition("ID",1);

	setArgInput("DATA_INIZIALE", STRING);
	setArgInputPosition("DATA_INIZIALE",2);

	setArgInput("DATA_FINALE", STRING);
	setArgInputPosition("DATA_FINALE",3);
	
	setArgInput("COD_UTENTE", STRING);
	setArgInputPosition("COD_UTENTE",4);
	
	setArgInput("COD_UFFICIO_UTENTE", STRING);
	setArgInputPosition("COD_UFFICIO_UTENTE",5);
    
    //  Valorizzazione dei parametri di Input
	
	setBigDecimal("ID", aCollaboratore.getIdCollaboratore()); 
	setString("DATA_INIZIALE", StringUtils.toStringJSP(DateUtils.getDateToString(aCollaboratore.getDataInizio(),"dd/MM/yyyy"), "")); 
	setString("DATA_FINALE", StringUtils.toStringJSP(DateUtils.getDateToString(aCollaboratore.getDataFine(),"dd/MM/yyyy"), "")); 
	setString("COD_UTENTE", aCollaboratore.getCodOperatoreAggiornamento()); 
	setString("COD_UFFICIO_UTENTE", aCollaboratore.getCodUfficioAggiornamento()); 

  }
}