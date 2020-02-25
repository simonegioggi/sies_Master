package siap.sico.soggettodattilo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: SoggettoDattiloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class SoggettoDattiloSqlDAO extends SqlDAO 
{

	public SoggettoDattiloSqlDAO (Connection con) 
			{
			 super(con);
			}

 //
  // METODO RICERCA()
  //

  public void ricercaSoggettoDattilo( SoggettoDattiloModel  aModel)	 throws DAOException
		{
			String lSql = getSqlQuery();
			lSql += " " + setCondizione(aModel);
			setStatement(lSql);
		}

  public void ricercaSoggettoDattiloByKey( BigDecimal aKey)	 throws DAOException
		{
			String lSql = getSqlQuery();
			lSql += " " + setCondizioniByKey(aKey);
			setStatement(lSql);
		}

  protected String getSqlQuery()
		{
	  		String lStatement = new String("");

			lStatement += " SELECT " +
				 "ID_DATTILO, "+  
				 "COD_SOGGETTO, "+  
				 "DOC_TIPO, "+  
				 "DOC_NOME, "+  
				 "DATA_INSERIMENTO, "+  
				 "COD_OPERATORE_INSERIMENTO, "+  
				 "COD_UFFICIO_INSERIMENTO, "+  
				 "DOC_BLOB "; 
			lStatement += " FROM SOGGETTO_DATTILO";
			lStatement += " WHERE 1=1 ";
			return lStatement;
		}

 //
  // METODO GETMODEL()
  //

	 public GenericModel  	 getModel() throws DAOException
  		{ 
				 SoggettoDattiloModel aModel = new  SoggettoDattiloModel(); 

				 aModel.setIdDattilo(getBigDecimal("ID_DATTILO") ); 
				 aModel.setCodSoggetto(getBigDecimal("COD_SOGGETTO") ); 
				 aModel.setDocTipo(getString("DOC_TIPO") ); 
				 aModel.setDocNome(getString("DOC_NOME") ); 
				 aModel.setDataInserimento(getDate("DATA_INSERIMENTO") ); 
				 aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") ); 
				 aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") ); 

				 return aModel;
		}


	 public String  setCondizione(SoggettoDattiloModel aModel)
		{
			String lCondizioni = new String();

			if (aModel.getCodSoggetto() != null)
			{
				lCondizioni += " AND COD_SOGGETTO=" + aModel.getCodSoggetto();
			}
		 
			return lCondizioni; 
 		}


	 public String setCondizioniByKey(BigDecimal aKey)
		{
		 	return " AND ID_DATTILO = " + aKey;
		}

}
