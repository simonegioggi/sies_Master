package siap.siep.sentenzariunita.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: SentenzaRiunitaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella SentenzaRiunita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class SentenzaRiunitaSqlDAO extends SIAPSqlDAO
{
	 public SentenzaRiunitaSqlDAO (Connection con)
			{
			 super(con);
			}


 //
	// METODO RICERCA()
	//


	public void ricercaSentenzaRiunita( SentenzaRiunitaModel  aModel)	 throws DAOException
		{
			 String lSql = getSqlQuery();

			 lSql += " " + setCondizione(aModel);
			 lSql += setOrder();
			 setStatement(lSql);
		}


	public void ricercaSentenzaRiunitaByKey( BigDecimal aKey)	 throws DAOException
		{
			 String lSql = getSqlQuery();

		 lSql += " " + setCondizioniByKey(aKey);

			 setStatement(lSql);
		}


	protected String getSqlQuery()
		{			 String lStatement = new String("");

			 lStatement += " SELECT " +
				 "ID_SENTENZA_RIUNITA, "+
				 "DATA_SENTENZA, "+
				 "ANNO_SENTENZA, "+
				 "NUMERO_SENTENZA, "+
				 "COD_TIPO_AUTORITA_EMITTENTE, "+
				 "COD_SEDE_NOTIZIA_REATO, "+
		  "LUOGO_REATO.DESCRIZIONE DESCR_LUOGO_REATO, "+
				 "TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, "+
				 "COD_AUTORITA_EMITTENTE, "+
				 "COD_LUOGO_EMITTENTE, "+
         "LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, "+
				 "SEZIONE_AUTORITA_EMITTENTE, "+
				 "ANNO_REGE_PM, "+
				 "NUMERO_REGE_PM, "+
				 "ANNO_REGE_GIP, "+
				 "NUMERO_REGE_GIP, "+
				 "ANNO_REGE_DIB, "+
				 "NUMERO_REGE_DIB, "+
				 "ANNO_REGE_CAS, "+
				 "NUMERO_REGE_CAS, "+
				 "COD_OPERATORE_INSERIMENTO, "+
				 "DATA_INSERIMENTO, "+
				 "COD_UFFICIO_INSERIMENTO, "+
				 "COD_OPERATORE_AGGIORNAMENTO, "+
				 "DATA_AGGIORNAMENTO, "+
				 "COD_UFFICIO_AGGIORNAMENTO, "+
				 "SEN_ID_SENTENZA ";
			 lStatement += " FROM SENTENZA_RIUNITA, cg_ref_codes TIPO_AUTORITA_EMITTENTE,comune LUOGO_EMITTENTE,comune LUOGO_REATO ";
			 lStatement += " WHERE ";
			 lStatement += " (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE)";
			 lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE)";
			 lStatement += " AND (LUOGO_REATO.COD_COMUNE = COD_SEDE_NOTIZIA_REATO)";

			 return lStatement;
		 }


 //
	// METODO GETMODEL()
	//


	 public GenericModel  	 getModel() throws DAOException
			{
				 SentenzaRiunitaModel aModel = new  SentenzaRiunitaModel();

//Inserire le opportune set delle descrizioni!
				 aModel.setIdSentenzaRiunita(getBigDecimal("ID_SENTENZA_RIUNITA") );
				 aModel.setDataSentenza(getDate("DATA_SENTENZA") );
				 aModel.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA") );
				 aModel.setNumeroSentenza(getString("NUMERO_SENTENZA") );
				 aModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE") );
				 aModel.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE") );
				 aModel.setCodAutoritaEmittente(getString("COD_AUTORITA_EMITTENTE") );
				 //aModel.setDescrAutoritaEmittente(getString("") );
				 aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE") );
				 aModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE") );
				 aModel.setSezioneAutoritaEmittente(getString("SEZIONE_AUTORITA_EMITTENTE") );
				 aModel.setAnnoRegePm(getBigDecimal("ANNO_REGE_PM") );
				 aModel.setNumeroRegePm(getString("NUMERO_REGE_PM") );
				 aModel.setAnnoRegeGip(getBigDecimal("ANNO_REGE_GIP") );
				 aModel.setNumeroRegeGip(getString("NUMERO_REGE_GIP") );
				 aModel.setAnnoRegeDib(getBigDecimal("ANNO_REGE_DIB") );
				 aModel.setNumeroRegeDib(getString("NUMERO_REGE_DIB") );
				 aModel.setAnnoRegeCas(getBigDecimal("ANNO_REGE_CAS") );
				 aModel.setNumeroRegeCas(getString("NUMERO_REGE_CAS") );
				 aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
				 aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
				 aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
				 //aModel.setDescrUfficioInserimento(getString("") );
				 aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
				 aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
				 aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
				 //aModel.setDescrUfficioAggiornamento(getString("") );
				 aModel.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA") );
				 aModel.setCodSedeNotiziaReato(getString("COD_SEDE_NOTIZIA_REATO") );
				 aModel.setDescrSedeNotiziaReato(getString("DESCR_LUOGO_REATO") );
				 return aModel;
		}


	 public String  setCondizione(SentenzaRiunitaModel aModel)
		{
		  String lCondizioni = " AND SEN_ID_SENTENZA = " + aModel.getSenIdSentenza();

			return lCondizioni;
		 }


	 public String setCondizioniByKey(BigDecimal aKey)
		{
		 return " AND ID_SENTENZA_RIUNITA = " + aKey ;
		}

	  public String setOrder()
		{
		 return " ORDER BY DATA_SENTENZA DESC " ;
		}

}
