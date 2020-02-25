package siap.sico.log.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.log.model.LogAttivitaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

/**
* <p>Title: LogAttivitaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella LogAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class LogAttivitaSqlDAO extends SqlDAO
{
	 public LogAttivitaSqlDAO (Connection con)
			{
			 super(con);
			}


 //
  // METODO RICERCA()
  //


  public void ricercaLogAttivita( LogAttivitaModel  aModel,Date aDataInizio, Date aDataFine,String FiltroUtenteConnesso)	 throws DAOException
		{
			 String lSql = getSqlQuery();

		 lSql += " " + setCondizione(aModel, aDataInizio, aDataFine,FiltroUtenteConnesso);
                  lSql += " ORDER BY DATA DESC";
			 setStatement(lSql);
		}


  public void ricercaLogAttivitaByKey( BigDecimal aKey)	 throws DAOException
		{
			 String lSql = getSqlQuery();

		 lSql += " " + setCondizioniByKey(aKey);
			 setStatement(lSql);
		}


  protected String getSqlQuery()
		{			 String lStatement = new String("");

			 lStatement += " SELECT " +
				 "RECORD, "+
				 "COD_OPERATORE, "+
				 "DATA, "+
				 "IP_UTENTE, "+
				 "AZIONE_CONTESTO_JAVA, "+
				 "UTENTE.NOME NOME, "+
				 "UTENTE.COGNOME COGNOME ";
			 lStatement += " FROM LOG_ATTIVITA, UTENTE";
			 lStatement += " WHERE COD_OPERATORE = UTENTE.COD_UTENTE";
			 return lStatement;		}


 //
  // METODO GETMODEL()
  //


	 public GenericModel  	 getModel() throws DAOException
  		{
				 LogAttivitaModel aModel = new  LogAttivitaModel();

//Inserire le opportune set delle descrizioni!
				 aModel.setRecord(getString("RECORD") );
				 aModel.setCodOperatore(getString("COD_OPERATORE") );
				 aModel.setData(getDate("DATA") );
				 aModel.setIpUtente(getString("IP_UTENTE") );
				 aModel.setAzioneContestoJava(getString("AZIONE_CONTESTO_JAVA") );
				 aModel.setCognome(getString("COGNOME"));
				 aModel.setNome(getString("NOME"));
				 return aModel;
		}


	 public String  setCondizione(LogAttivitaModel aModel,Date aDataInizio, Date aDataFine,String FiltroUtenteConnesso)
		{
		 String lCondizioni = new String();

		 //boolean lInserito = false;
                 if (aModel.getCodOperatore()!=null)
                 {
                   if (!aModel.getCodOperatore().trim().equals(""))
                   {
                     lCondizioni += " AND UPPER(COD_OPERATORE)='" + StringUtils.convertSqlString(aModel.getCodOperatore()).toUpperCase()+"' ";
         //            lInserito = true;
                   }
                 }
                 if (aModel.getIpUtente()!=null)
                 {
                   if (!aModel.getIpUtente().trim().equals(""))
                   {
         //            if (lInserito) {
                       lCondizioni += " AND IP_UTENTE='" + StringUtils.convertSqlString(aModel.getIpUtente()) + "'";
         //            }
         //            else {
         //              lCondizioni += " AND IP_UTENTE='" + StringUtils.convertSqlString(aModel.getIpUtente()) + "'";
         //              lInserito = true;
         //            }
                   }
                 }
                 if (aDataInizio!=null)
                 {
         //          if (lInserito)
         //          {
			 			lCondizioni+=" AND DATA>=TO_DATE('"+DateUtils.getDateToString(aDataInizio,"dd/MM/yyyy HH:mm:ss")+"','DD/MM/YYYY HH24:MI:SS')";
                     //}
         //          else
         //          {
         //            lCondizioni+=" AND DATA>=TO_DATE('"+DateUtils.getDateToString(aDataInizio,"dd/MM/yyyy HH:mm:ss")+"','DD/MM/YYYY HH24:MI:SS')";
         //            lInserito=true;
         //          }
                 }
                 if (aDataFine!=null)
                 {
         //          if (lInserito)
         //          {
					   lCondizioni+=" AND DATA<TO_DATE('"+DateUtils.getDateToString(DateUtils.moveDateTo(aDataFine,java.util.GregorianCalendar.DAY_OF_WEEK,1),"dd/MM/yyyy HH:mm:ss")+"','DD/MM/YYYY HH24:MI:SS')";
                      //}
         //          else
         //          {
         //            lCondizioni+=" AND DATA<TO_DATE('"+DateUtils.getDateToString(DateUtils.moveDateTo(aDataFine,java.util.GregorianCalendar.DAY_OF_WEEK,1),"dd/MM/yyyy HH:mm:ss")+"','DD/MM/YYYY HH24:MI:SS')";
         //            lInserito=true;
         //          }
                 }
                 if (!FiltroUtenteConnesso.equals(""))
                 {
         //          if (lInserito)
         //          {
					   lCondizioni+=" AND COD_OPERATORE <> '" + FiltroUtenteConnesso+"' ";
                      //}
         //          else
         //          {
         //            lCondizioni+=" AND COD_OPERATORE <> '" + FiltroUtenteConnesso+"' ";
         //            lInserito=true;
         //          }
                 }
                 if (!aModel.getNome().equals(""))
				 {
					lCondizioni+=" AND UPPER(NOME) = '" + StringUtils.convertSqlString(aModel.getNome()).toUpperCase() +"' ";
                 }
                 if (!aModel.getCognome().equals(""))
				         {
				 	lCondizioni+=" AND UPPER(COGNOME) = '" + StringUtils.convertSqlString(aModel.getCognome()).toUpperCase() +"' ";
                 }
                 if (aModel.getRecord()!=null)
                 {
                   if (!aModel.getRecord().trim().equals(""))
                   {
         //           if (lInserito) {
                       lCondizioni += " AND UPPER(RECORD) LIKE '%" + StringUtils.convertSqlString(aModel.getRecord()).toUpperCase() + "%'";
         //            }
         //            else {
         //              lCondizioni += " AND UPPER(RECORD) LIKE '%" + StringUtils.convertSqlString(aModel.getRecord()).toUpperCase() + "%'";
         //              lInserito = true;
         //            }
                   }
                 }

 		 return lCondizioni;
 		}


	 public String setCondizioniByKey(BigDecimal aKey)
		{
		 return " AND ID_LOG_ATTIVITA = " + aKey;
		}
}
