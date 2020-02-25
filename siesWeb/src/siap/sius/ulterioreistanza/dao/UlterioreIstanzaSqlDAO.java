package siap.sius.ulterioreistanza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: UlterioreIstanzaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella UlterioreIstanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class UlterioreIstanzaSqlDAO extends SqlDAO 
{
  public UlterioreIstanzaSqlDAO (Connection con) 
	{
    super(con);
	}

  //
  // METODO RICERCA()
  //

  /**
   * Metodo che imposta la query per la ricerca.
   * <p>
   * @param aModel Model Ulteriore istanza.
   */
  public void ricercaUlterioreIstanza( UlterioreIstanzaModel  aModel)	 throws DAOException
	{
    String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

  /**
   * Imposta la ricerca di una Ulteriore Istanza, per il proprio ID
   * <p> 
   * @param aKey id di ricerca.
   * @throws DAOException propga errori di eccezione.
   */
  public void ricercaUlterioreIstanzaByKey( BigDecimal aKey)	 throws DAOException
	{
    String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

  /**
   * Metodo che ritorna la select d'interrogazione.
   * <p>
   * @return Ritorna la select sql. 
   */
  protected String getSqlQuery()
	{			 
    String lStatement = new String("");
		
    lStatement += "SELECT " +
				          " ID_ULTERIORE_ISTANZA, " +  
				          " COD_OGGETTO_PROCEDIMENTO, " + 
                  " OGGETTO_PROCEDIMENTO.RV_MEANING AS DESCR_OGGETTO_PROCEDIMENTO, " +
				          " DATA_RICHIESTA, " +  
				          " DATA_ARRIVO_CANCELLERIA, "  +  
				          " COD_TIPO_ATTO, "  +
                  " TIPO_ATTO.RV_MEANING AS DESCR_TIPO_ATTO, " +
				          " COD_TIPO_MITTENTE_ATTO, " + 
                  " MITTENTE_ATTO.RV_MEANING AS DESCR_MITTENTE_ATTO, " +
				          " SEDE_MITTENTE.DESCRIZIONE AS SEDE_MITTENTE, "  +  
				          " DESCR_MITTENTE, " +  
				          " NOTE, " +  
				          " COD_OPERATORE_INSERIMENTO, "  +  
				          " DATA_INSERIMENTO, " +  
				          " COD_UFFICIO_INSERIMENTO, "  +  
				          " COD_OPERATORE_AGGIORNAMENTO, "  +  
				          " DATA_AGGIORNAMENTO, " +  
				          " COD_UFFICIO_AGGIORNAMENTO, "  +  
				          " FAS_SIU_ID_FASCICOLO_SIUS "; 
			 lStatement += " FROM ULTERIORE_ISTANZA ";
       lStatement += " INNER JOIN CG_REF_CODES OGGETTO_PROCEDIMENTO ON  OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' " +
                                                                  " AND OGGETTO_PROCEDIMENTO.RV_LOW_VALUE = ULTERIORE_ISTANZA.COD_OGGETTO_PROCEDIMENTO "+ 
                     " INNER JOIN CG_REF_CODES TIPO_ATTO            ON  TIPO_ATTO.RV_DOMAIN = 'TIPO_ATTO'" +
                                                                  " AND TIPO_ATTO.RV_LOW_VALUE = ULTERIORE_ISTANZA.COD_TIPO_ATTO "+
                     " INNER JOIN CG_REF_CODES MITTENTE_ATTO        ON  MITTENTE_ATTO.RV_DOMAIN = 'MITTENTE_ATTO' " +
                                                                  " AND MITTENTE_ATTO.RV_LOW_VALUE = ULTERIORE_ISTANZA.COD_TIPO_MITTENTE_ATTO " +
                     " INNER JOIN COMUNE SEDE_MITTENTE              ON  SEDE_MITTENTE.COD_COMUNE = ULTERIORE_ISTANZA.SEDE_MITTENTE ";
			 lStatement += " WHERE ";
			 
       return lStatement;		
   }
  
  //
  // METODO GETMODEL()
  //

  /**
   * Metodo che ritorna il model opportunamente valorizzato.
   * <p>
   * @return Ritorna il model.
   */
	public GenericModel  getModel() throws DAOException
  { 
    UlterioreIstanzaModel aModel = new  UlterioreIstanzaModel(); 

	  //Inserire le opportune set delle descrizioni!
		aModel.setIdUlterioreIstanza(getBigDecimal("ID_ULTERIORE_ISTANZA") ); 
		aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO") ); 
		aModel.setDescrOggettoProcedimento(getString("DESCR_OGGETTO_PROCEDIMENTO") );
		aModel.setDataRichiesta(getDate("DATA_RICHIESTA") ); 
		aModel.setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA") ); 
    aModel.setCodTipoAtto(getString("COD_TIPO_ATTO") ); 
    aModel.setDescrTipoAtto(getString("DESCR_TIPO_ATTO") );
    aModel.setCodTipoMittenteAtto(getString("COD_TIPO_MITTENTE_ATTO") ); 
    aModel.setDescrTipoMittenteAtto(getString("DESCR_MITTENTE_ATTO") );
    aModel.setSedeMittente(getString("SEDE_MITTENTE") ); 
    aModel.setDescrMittente(getString("DESCR_MITTENTE") ); 
    aModel.setNote(getString("NOTE") ); 
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") ); 
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") ); 
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") ); 
     //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") ); 
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") ); 
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") ); 
     //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS") ); 
    
    return aModel;
	}

	/**
   * Imposta le condizioni di filtro in funzione dei membri di classe
   * opportunamente valorizzati.
   * <p>
   * @param aModel Model con i dati di filtro.
   * @return Stringa di composzione di Where condition.
	 */
	public String setCondizione(UlterioreIstanzaModel aModel)
	{
	  String lCondizioni = new String(); 
 		
		boolean lInserito = false;
    
    if( aModel.getFasSiuIdFascicoloSius() != null )
    {
      lCondizioni += ( lInserito ? " AND " : "" );
      lCondizioni += " FAS_SIU_ID_FASCICOLO_SIUS = " + aModel.getFasSiuIdFascicoloSius();
      lInserito = true;
    }
    
 		return lCondizioni; 
 	}

	/**
   * Imposta condizioni di filtro per il corrispondente id. 
   * <p>
   * @param aKey id da ricercare.
   * @return Ritorna la stringa di composizione della where condition.
	 */
	public String setCondizioniByKey(BigDecimal aKey)
	{
	  return " ID_ULTERIORE_ISTANZA = " + aKey; 
	}
}