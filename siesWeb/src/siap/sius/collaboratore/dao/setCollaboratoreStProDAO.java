package siap.sius.collaboratore.dao;

import java.sql.Connection;

import siap.sius.collaboratore.model.CollaboratoreModel;
import f3b.dao.StoreProcedureDAO;
import f3b.util.DateUtils;
import f3b.util.StringUtils;



/**
 * <p>Title: setCollaboratoreStProcDAO</p>
 * <p>Description:</p>
 * <p> La classe costituisce l'interfaccia alla funzione PlSql setCollaboratore():
 * <p> Schema : COLLA
 * <p>Nome del package : COLL
 * <p>  procedure setCollaboratore ( par_ufficio IN VARCHAR2, par_data_iniziale IN VARCHAR2, par_data_finale IN VARCHAR2,
 *       par_id IN  number, par_utente IN VARCHAR2, par_ufficio_utente IN VARCHAR2).
 * <p> La funzione effettua l'inserimento del record Collaboratore nell'apposita tabella.
 * <p>Copyright: Copyright (c) 2007</p>
 */
public class setCollaboratoreStProDAO extends StoreProcedureDAO
{

  public setCollaboratoreStProDAO(Connection lConn, CollaboratoreModel aCollaboratore)
  {
    super(lConn);
    
    // Nome della Stored Procedure 
    setStoreProcedure("COLLA.COLL.setCollaboratore");
   
    // Definizione degli argomenti 
	
    setArgInput("COD_UFFICIO", STRING);
	setArgInputPosition("COD_UFFICIO",1);

	setArgInput("DATA_INIZIALE", STRING);
	setArgInputPosition("DATA_INIZIALE",2);

	setArgInput("DATA_FINALE", STRING);
	setArgInputPosition("DATA_FINALE",3);
	
	setArgInput("ID", BIG_DECIMAL);
	setArgInputPosition("ID",4);
	
	setArgInput("COD_UTENTE", STRING);
	setArgInputPosition("COD_UTENTE",5);
	
	setArgInput("COD_UFFICIO_UTENTE", STRING);
	setArgInputPosition("COD_UFFICIO_UTENTE",6);
    
    //  Valorizzazione dei parametri di Input
	
	setString("COD_UFFICIO", aCollaboratore.getCodUfficio()); 
	setBigDecimal("ID", aCollaboratore.getIdFascicoloSius()); 
	setString("DATA_INIZIALE", StringUtils.toStringJSP(DateUtils.getDateToString(aCollaboratore.getDataInizio(),"dd/MM/yyyy"), "")); 
	setString("DATA_FINALE", StringUtils.toStringJSP(DateUtils.getDateToString(aCollaboratore.getDataFine(),"dd/MM/yyyy"), "")); 
	setString("COD_UTENTE", aCollaboratore.getCodOperatoreInserimento()); 
	setString("COD_UFFICIO_UTENTE", aCollaboratore.getCodUfficioInserimento()); 

  }
}