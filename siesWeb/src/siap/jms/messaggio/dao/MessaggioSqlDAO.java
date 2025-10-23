package siap.jms.messaggio.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.dao.SIAPSqlDAO;
import siap.jms.ICostantiJMS;
import siap.jms.messaggio.model.MessaggioModel;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siepe.ricezioneatti.model.CruscottoModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: MessaggioSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Messaggio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class MessaggioSqlDAO extends SIAPSqlDAO {


	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);
		
	public MessaggioSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaMessaggio(MessaggioModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioPerJmsId(String JmsID) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO IN ('02','04') AND JMS_CORRELATION_ID_MESSAGE = '" + JmsID + "'";
		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioPerCorrreBDIId(String CorrID, String BdiMittente) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO ='04' AND COD_BDI_MITTENTE = '" + BdiMittente
				+ "' AND JMS_CORRELATION_ID_MESSAGE = '" + CorrID + "'" + " AND COD_ESITO = '00100'";
		lSql += setOrder();
		setStatement(lSql);
	}

	// Ricerca per Tipo e JmsID
	public void ricercaMessaggioPerJmsIdOnly(String JmsID) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " AND JMS_CORRELATION_ID_MESSAGE = '" + JmsID + "'";

		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioEsitoRicercaPerJmsId(String JmsID) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '04' AND JMS_CORRELATION_ID_MESSAGE = '" + JmsID + "'";
		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioRichiestaPerUfficio(String aUfficio) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '" + aUfficio
				+ "' AND FLAG_VISTO='N' ";
		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioEsitoRicerca(String aUfficio, String aTipoOperazione, int aPage)
			throws DAOException {

		String lCond = " AND COD_TIPO_MESSAGGIO = '03' AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'";
		lCond += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'  AND FLAG_VISTO='N' ";

		String lSql = getSqlQueryPerRicerca(aPage, lCond);

		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioRichiestaPerUfficioTipoOperazione(String aUfficio, String aTipoOperazione)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '" + aUfficio + "'";
		lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'  AND FLAG_VISTO='N' ";

		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioRichiestaPerUfficiTipoOperazione(String aUfficioRicevente,
			String aUfficioMittente, String aTipoOperazione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '" + aUfficioRicevente + "'";
		lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficioMittente + "'";
		lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'  AND FLAG_VISTO='N' ";

		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggiRichiestaPerUffici(String aUfficioRicevente, String aUfficioMittente)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '" + aUfficioRicevente + "'";
		lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficioMittente + "'";
		lSql += " AND FLAG_VISTO='N' ";

		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggiRichiestaPerUfficioTipoOper(String aUfficioMittente, String aTipoOperazione)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01'";
		lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficioMittente + "'";
		lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";
		lSql += " AND FLAG_VISTO='N' ";

		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggiRichiestaPerDateUffici(String aUfficioRicevente, String aUfficioMittente,
			Date aDataInizioTrasmissione, Date aDataFineTrasmissione, String aIncludeInCarico)
			throws DAOException {
		String lSql = getSqlQuery(false);

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '" + aUfficioRicevente + "'";
		if (!aUfficioMittente.equals("-"))
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficioMittente + "'";
		if (!aDataInizioTrasmissione.equals(null))
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >='"
					+ DateUtils.getDateToString(aDataInizioTrasmissione, "yyyyMMdd") + "'";
		if (!aDataFineTrasmissione.equals(null))
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <='"
					+ DateUtils.getDateToString(aDataFineTrasmissione, "yyyyMMdd") + "'";

		// STUB 15/03/2005 Se si e' inclusi gli atti presi in carico, va esclusa la condizione sul FLAG_VISTO.
		if (aIncludeInCarico.compareTo("S") != 0)
			lSql += " AND FLAG_VISTO='N' ";

		lSql += setOrder();
		setStatement(lSql);
	}

	// Aggiunto x UEPE
	public void ricercaMessaggiRichiestaPerTipoeDate(String aUfficioRicevente, String aTipoOperazione,
			Date aDataInizioTrasmissione, Date aDataFineTrasmissione, String aFlagVisto) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '" + aUfficioRicevente + "'";
		if (!aTipoOperazione.equals("-"))
			lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";
		if (!aDataInizioTrasmissione.equals(null))
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >='"
					+ DateUtils.getDateToString(aDataInizioTrasmissione, "yyyyMMdd") + "'";
		if (!aDataFineTrasmissione.equals(null))
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <='"
					+ DateUtils.getDateToString(aDataFineTrasmissione, "yyyyMMdd") + "'";

		// 15/10/2006 - Solo i messaggi con lo stato Ricezione impostato.
		if ((aFlagVisto.compareTo("S") == 0) || (aFlagVisto.compareTo("V") == 0)
				|| (aFlagVisto.compareTo("R") == 0) || (aFlagVisto.compareTo("N") == 0))
			lSql += " AND FLAG_VISTO='" + aFlagVisto + "' ";

		lSql += setOrder();
		setStatement(lSql);
	}

	// Aggiunto x UEPE
	public void ricercaMessaggiRichiestaPerSoggetto(String aUfficioRicevente, String aCognomeSoggetto,
			String aNomeSoggetto, String aCodComuneNascita, String aCodStatoNascita, Date aDataNascita,
			String aTipoOperazione, String aFlagVisto) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '" + aUfficioRicevente + "'";
		if (!(aCognomeSoggetto.equals("")))
			lSql += " AND COGNOME_SOGGETTO like '" + StringUtils.convertSqlString(aCognomeSoggetto) + "%'";
		if (!(aNomeSoggetto.equals("")))
			lSql += " AND NOME_SOGGETTO like '" + StringUtils.convertSqlString(aNomeSoggetto) + "%'";
		if (!(aCodComuneNascita.equals("")))
			lSql += " AND COD_COMUNE_NASCITA = '" + StringUtils.convertSqlString(aCodComuneNascita) + "'";
		;
		if (!(aCodStatoNascita.equals("-")))
			lSql += " AND COD_STATO_NASCITA = '" + StringUtils.convertSqlString(aCodStatoNascita) + "'";
		;
		if (aDataNascita != null)
			lSql += " AND TO_CHAR(DATA_NASCITA,'YYYYMMDD') >='"
					+ DateUtils.getDateToString(aDataNascita, "yyyyMMdd") + "'";
		if (!aTipoOperazione.equals("-"))
			lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";

		// 15/10/2006 - Solo i messaggi con lo stato Ricezione impostato.
		if ((aFlagVisto.compareTo("S") == 0) || (aFlagVisto.compareTo("V") == 0)
				|| (aFlagVisto.compareTo("R") == 0) || (aFlagVisto.compareTo("N") == 0))
			lSql += " AND FLAG_VISTO='" + aFlagVisto + "' ";

		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioRichiestaSpediti(String aUfficio) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'";
		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioRichiestaSpediti(String aUfficio, String aTipoOperazione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'";
		lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";
		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioRichiestaConFiltri(String aUfficio, String aUtente, String aEsito,
			String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' ";
		if (aUfficio != null)
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'";
		if (aUtente != null)
			lSql += " AND CODICE_UTENTE_MITTENTE = '" + aUtente + "'";
		if (!(aTipoOperazione == null || aTipoOperazione.compareTo("-") == 0))
			lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";
		if (dataRicercaInizio != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(dataRicercaInizio, "yyyyMMdd") + "'";
		if (dataRicercaFine != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(dataRicercaFine, "yyyyMMdd") + "'";

		// Si escludono i messaggi di trasmissione x competenza delle Misure Sicurezza che hanno una gestione
		// a parte
		lSql += " AND COD_TIPO_OPERAZIONE not in ('00071','00072','00073','00074') ";

		lSql += " order by COD_UFFICIO_MITTENTE, DATA_INVIO DESC ";
		// lSql += setOrder();
		setStatement(lSql);
	}

  public void RicercaMessaggiRicevutiPaged(MessaggioModel aMessaggio, Vector <String> aListaTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine, int aPage) throws DAOException
  {
	  //String lSql = getSqlQuery();
	  String lSql = new String("");
	  String lPaginedStatement=new String("");
	  
	  lSql = getPagedSqlQuery(aPage);
	  lSql += " " + setCondizioneMessaggiRicevuti(aMessaggio, aListaTipoOperazione);
	  lSql += " " + setCondizioneDataInvio(dataRicercaInizio, dataRicercaFine);
	  lSql += setOrder();
	  
	  // Se apage = 0 , la Query serve per il totale
	   if(aPage==0)
	   {
		   setStatement(lSql);
	   }
	   else
	   {
		   lPaginedStatement="SELECT * FROM (SELECT INNER.* , Rownum rn FROM ("+lSql+"  ) INNER ) WHERE rn between  "+((aPage-1)*IWebConstants.RESULT_PER_PAGE+1)+ " AND "+ (aPage)*IWebConstants.RESULT_PER_PAGE;
		   setStatement(lPaginedStatement);
	   }

  }
  
  public String setCondizioneMessaggiRicevuti(MessaggioModel aModel, Vector <String> aListaTipoOperazione)
  {
	  String lCondizioni = new String();
    
	  if (aModel.getCodTipoMessaggio() != null && aModel.getCodTipoMessaggio().trim().length() > 0)
		  lCondizioni += " AND COD_TIPO_MESSAGGIO = '" + aModel.getCodTipoMessaggio() + "' ";
	  
	  if (aModel.getFlagVisto() != null && aModel.getFlagVisto().trim().length() > 0)
	       lCondizioni += " AND FLAG_VISTO = '" + aModel.getFlagVisto() + "' ";
	  
      if (aModel.getCodUfficioDestinatario() != null && aModel.getCodUfficioDestinatario().trim().length() > 0)
        lCondizioni += " AND COD_UFFICIO_DESTINATARIO = '" + aModel.getCodUfficioDestinatario() + "' ";
      
      if (aModel.getCodUfficioMittente() != null && aModel.getCodUfficioMittente().trim().length() > 0)
        lCondizioni += " AND COD_UFFICIO_MITTENTE = '" + aModel.getCodUfficioMittente() + "' ";
      
      /* MEV_2025-48 – Atti pervenuti per competenza al cumulo */
      /*               Si aggiunge ricerca anche per ANNO e NUMERO fascicolo trasmesso */
      if (aModel.getChiaveAnnoSiep() != null)
        lCondizioni += " AND CHIAVE_ANNO_SIEP =" + aModel.getChiaveAnnoSiep();

      if (aModel.getChiaveProgrSiep()!= null)
        lCondizioni += " AND CHIAVE_PROGR_SIEP =" + aModel.getChiaveProgrSiep();

      if (aModel.getChiaveAnnoFasCumulante() != null )
        lCondizioni += " AND CHIAVE_ANNO_FAS_CUMULANTE =" + aModel.getChiaveAnnoFasCumulante();

      if (aModel.getChiaveProgrFasCumulante()!= null)
        lCondizioni += " AND CHIAVE_PROGR_FAS_CUMULANTE =" + aModel.getChiaveProgrFasCumulante();
      
      if (aModel.getCognomeSoggetto()!= null && aModel.getCognomeSoggetto().trim().length() > 0)
          lCondizioni += " AND UPPER(COGNOME_SOGGETTO) = '" + aModel.getCognomeSoggetto().toUpperCase()+"' ";

      if (aModel.getNomeSoggetto()!= null && aModel.getNomeSoggetto().trim().length() > 0)
          lCondizioni += " AND UPPER(NOME_SOGGETTO) = '" + aModel.getNomeSoggetto().toUpperCase()+"' ";
      
//      if (aModel.getChiaveAnnoFasCumulante() != null && aModel.getChiaveProgrFasCumulante()!= null)
//      {
//        lCondizioni += " AND CHIAVE_ANNO_FAS_CUMULANTE =" + aModel.getChiaveAnnoFasCumulante();
//        lCondizioni += " AND CHIAVE_PROGR_FAS_CUMULANTE =" + aModel.getChiaveProgrFasCumulante();
//      }
      /* MEV_2025-48 – FINE */
      
      if (aModel.getChiaveUfficioFasCumulante() != null && !"".equals(aModel.getChiaveUfficioFasCumulante()))
    	  lCondizioni += " AND CHIAVE_UFFICIO_FAS_CUMULANTE = '" + aModel.getChiaveUfficioFasCumulante()+"'";
      
      if (aListaTipoOperazione!=null && aListaTipoOperazione.size()>0)
      {
    	  lCondizioni += " AND COD_TIPO_OPERAZIONE in (";
          for (int i=0;i<aListaTipoOperazione.size();i++) 
          {
        	  lCondizioni += " '"+aListaTipoOperazione.elementAt(i)+"'";
        	  if (i<aListaTipoOperazione.size()-1)
        		  lCondizioni += ",";
          }
          
          lCondizioni += " ) ";      
      }      

      return lCondizioni;
  }
	/**
	 * Ricerca i Messaggi di tipo '01 - Richiesta'. La ricerca viene effettuata utilizzando i criteri passati
	 * in input.
	 * 
	 * @param aCodTipoOper
	 *            - TIPO_OPERAZIONE
	 * @param aUfficio
	 *            - NON UTILIZZATO
	 * @param aUtente
	 *            - COD_UTENTE_MITTENTE
	 * @param aEsito
	 *            - NON UTILIZZATO
	 * @param aCodUffDest
	 * @param aAnnoSiep
	 * @param aProgrSiep
	 * @param dataRicercaInizio
	 *            - DATA_INVIO
	 * @param dataRicercaFine
	 *            - DATA_INVIO
	 * @throws DAOException
	 */
	public void ricercaMessaggioConFiltri(String aCodTipoOper, String aUfficio, String aUtente,
			String aEsito, String aCodUffDest, String aAnnoSiep, String aProgrSiep, Date dataRicercaInizio,
			Date dataRicercaFine) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' ";
		lSql += " AND COD_TIPO_OPERAZIONE = '" + aCodTipoOper + "'";

		if (aCodUffDest != null && aCodUffDest != "-")
			lSql += " AND COD_UFFICIO_DESTINATARIO = '" + aCodUffDest + "'";
		if (aAnnoSiep.length() > 1)
			lSql += " AND CHIAVE_ANNO_SIEP = '" + aAnnoSiep + "'";
		if (aProgrSiep.length() > 0)
			lSql += " AND CHIAVE_PROGR_SIEP = '" + aProgrSiep + "'";
		if (aUfficio != null)
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'";
		if (aUtente != null)
			lSql += " AND CODICE_UTENTE_MITTENTE = '" + aUtente + "'";
		if (dataRicercaInizio != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(dataRicercaInizio, "yyyyMMdd") + "'";
		if (dataRicercaFine != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(dataRicercaFine, "yyyyMMdd") + "'";

		lSql += " order by COD_UFFICIO_MITTENTE, DATA_INVIO DESC ";
		// lSql += setOrder();
		setStatement(lSql);
	}

	/**
	 * Recupera il numero di record restituiti dalla ricerca. Necessario per la gestione della ricerca
	 * paginata
	 * 
	 * @param aDeliveryMode
	 * @param aCodTipoMessaggio
	 * @param aCodTipoOper
	 * @param aCodEsito
	 * @param sFlagVisto
	 * @param aChiaveAnnoSiep
	 * @param aChiaveProgrSiep
	 * @param aChiaveUfficioSiep
	 * @param aCodUfficioMitt
	 * @param aCodUtenteMitt
	 * @param aCodUffDest
	 * @param aDataRicercaInizio
	 * @param aDataRicercaFine
	 * 
	 * @throws DAOException
	 */
	public void getCountRicercaMessaggioRicercaConFiltri(
			String aDeliveryMode,
			String aCodTipoMessaggio,
			Vector<String> aListaTipoOperazione
			, Vector<String> aListaEsiti, String sFlagVisto, BigDecimal aChiaveAnnoSiep,
			BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep, String aCodUfficioMitt,
			String aCodUtenteMitt, String aCodUffDest, Date aDataRicercaInizio, Date aDataRicercaFine
      , String aCognome, String aNome      
      )
			throws DAOException {

		String lSql = "SELECT COUNT(*) HowManyRecords FROM MESSAGGIO WHERE 1=1 ";

		if (aDeliveryMode != null)
			lSql += " AND DELIVERY_MODE = '" + aDeliveryMode + "' ";
		if (aCodTipoMessaggio != null)
			lSql += " AND COD_TIPO_MESSAGGIO = '" + aCodTipoMessaggio + "' ";
		// if (aCodTipoOper!=null) lSql += " AND COD_TIPO_OPERAZIONE = '" +aCodTipoOper+ "'";
		if (aListaTipoOperazione != null && aListaTipoOperazione.size() > 0) {
			lSql += " AND COD_TIPO_OPERAZIONE in (";
			for (int i = 0; i < aListaTipoOperazione.size(); i++) {
				lSql += " '" + aListaTipoOperazione.elementAt(i) + "'";
				if (i < aListaTipoOperazione.size() - 1)
					lSql += ",";
			}
			lSql += " ) ";
		}

		// if (aCodEsito != null) lSql += " AND COD_ESITO = '" + aCodEsito + "'";
		if (aListaEsiti != null && aListaEsiti.size() > 0) {
			lSql += " AND COD_ESITO in (";
			for (int i = 0; i < aListaEsiti.size(); i++) {
				lSql += " '" + aListaEsiti.elementAt(i) + "'";
				if (i < aListaEsiti.size() - 1)
					lSql += ",";
			}
			lSql += " ) ";
		}

		if (sFlagVisto != null)
			lSql += " AND FLAG_VISTO = '" + sFlagVisto + "'";
		if (aChiaveAnnoSiep != null)
			lSql += " AND MESSAGGIO.CHIAVE_ANNO_SIEP = '" + aChiaveAnnoSiep + "'";
		if (aChiaveProgrSiep != null)
			lSql += " AND MESSAGGIO.CHIAVE_PROGR_SIEP = '" + aChiaveProgrSiep + "'";
		if (aChiaveUfficioSiep != null)
			lSql += " AND MESSAGGIO.CHIAVE_UFFICIO_SIEP = '" + aChiaveUfficioSiep + "'";

		if (aCodUfficioMitt != null)
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aCodUfficioMitt + "'";
		if (aCodUffDest != null && aCodUffDest != "-")
			lSql += " AND COD_UFFICIO_DESTINATARIO = '" + aCodUffDest + "'";
		if (aCodUtenteMitt != null)
			lSql += " AND CODICE_UTENTE_MITTENTE = '" + aCodUtenteMitt + "'";
		if (aDataRicercaInizio != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aDataRicercaInizio, "yyyyMMdd") + "'";
		if (aDataRicercaFine != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aDataRicercaFine, "yyyyMMdd") + "'";
          
    if (aCognome != null)   lSql += " AND UPPER(COGNOME_SOGGETTO) = '" + aCognome.toUpperCase() + "'";
    if (aNome != null)      lSql += " AND UPPER(NOME_SOGGETTO) = '" + aNome.toUpperCase() + "'";

		setStatement(lSql);
	}


 public void getCountRicercaMessaggioRicercaConFiltri( String aDeliveryMode
          , Vector <String> aListaTipoMessaggio
          , Vector <String> aListaTipoOperazione
          , Vector <String> aListaEsiti
          , String sFlagVisto
          , BigDecimal aChiaveAnnoSiep
          , BigDecimal aChiaveProgrSiep
          , String aChiaveUfficioSiep
          , String aCodUfficioMitt
          , String aCodUtenteMitt
          , String aCodUffDest
          , Date aDataRicercaInizio
          , Date aDataRicercaFine
          , String aCognome
          , String aNome ) throws DAOException
  {

	    String lSql ="SELECT COUNT(*) HowManyRecords FROM MESSAGGIO WHERE 1=1 ";
	    
	    if (aDeliveryMode!=null)         lSql += " AND DELIVERY_MODE = '"+aDeliveryMode+"' ";    

	    if (aListaTipoMessaggio!=null && aListaTipoMessaggio.size()>0){
	      lSql += " AND COD_TIPO_MESSAGGIO in (";
	      for (int i=0;i<aListaTipoMessaggio.size();i++) {
	        lSql += " '"+aListaTipoMessaggio.elementAt(i)+"'";
	        if (i<aListaTipoMessaggio.size()-1)
	          lSql += ",";
	      }      
	      lSql += " ) ";      
	    }

	    if (aListaTipoOperazione!=null && aListaTipoOperazione.size()>0){
	      lSql += " AND COD_TIPO_OPERAZIONE in (";
	      for (int i=0;i<aListaTipoOperazione.size();i++) {
	        lSql += " '"+aListaTipoOperazione.elementAt(i)+"'";
	        if (i<aListaTipoOperazione.size()-1)
	          lSql += ",";
	      }      
	      lSql += " ) ";      
	    }
	    
	    if (aListaEsiti!=null && aListaEsiti.size()>0){
	      lSql += " AND COD_ESITO in (";
	      for (int i=0;i<aListaEsiti.size();i++) {
	        lSql += " '"+aListaEsiti.elementAt(i)+"'";
	        if (i<aListaEsiti.size()-1)
	          lSql += ",";
	      }      
	      lSql += " ) ";      
	    }
	    
	    if (sFlagVisto != null)          lSql += " AND FLAG_VISTO = '" + sFlagVisto + "'";
	    if (aChiaveAnnoSiep != null)     lSql += " AND MESSAGGIO.CHIAVE_ANNO_SIEP = '" + aChiaveAnnoSiep + "'";
	    if (aChiaveProgrSiep != null)    lSql += " AND MESSAGGIO.CHIAVE_PROGR_SIEP = '" + aChiaveProgrSiep + "'";  
	    if (aChiaveUfficioSiep != null)  lSql += " AND MESSAGGIO.CHIAVE_UFFICIO_SIEP = '" + aChiaveUfficioSiep + "'";  
	    
	    if (aCodUfficioMitt != null)     lSql += " AND COD_UFFICIO_MITTENTE = '" + aCodUfficioMitt + "'";    
	    if (aCodUffDest != null && aCodUffDest != "-")
	                                     lSql += " AND COD_UFFICIO_DESTINATARIO = '" + aCodUffDest + "'";
	    if (aCodUtenteMitt != null)      lSql += " AND CODICE_UTENTE_MITTENTE = '" + aCodUtenteMitt + "'";
	    if (aDataRicercaInizio != null ) lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '" + DateUtils.getDateToString(aDataRicercaInizio, "yyyyMMdd" )+"'" ;
	    if (aDataRicercaFine != null )   lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '" + DateUtils.getDateToString(aDataRicercaFine, "yyyyMMdd" )+"'" ;

	    if (aCognome != null)   lSql += " AND UPPER(COGNOME_SOGGETTO) = '" + aCognome.toUpperCase() + "'";
	    if (aNome != null)      lSql += " AND UPPER(NOME_SOGGETTO) = '" + aNome.toUpperCase() + "'";

	    setStatement(lSql);
  }

	/**
	 * 
	 * Effettua la ricerca dei Messaggio di tipo Ricerca 01 applicando i filtri impostati in maschera.
	 * 
	 * @param aDeliveryMode
	 * @param aCodTipoMessaggio
	 * @param aCodTipoOper
	 * @param aCodEsito
	 * @param sFlagVisto
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param aCodUfficioMitt
	 * @param aCodUtenteMitt
	 * @param aCodUffDest
	 * @param aDataRicercaInizio
	 * @param aDataRicercaFine
	 * @param aPage
	 *            - se > 0 vengono recuperati solo i dati per la pagina indicata
	 * 
	 * @throws DAOException
	 * 
	 * @since NOV/2013
	 */
	public void ricercaMessaggioRicercaConFiltri (	String aDeliveryMode,
													String aCodTipoMessaggio,
													// , String aCodTipoOper
													Vector<String> aListaTipoOperazione,
													// , String aCodEsito
													Vector<String> aListaEsiti, 
													String sFlagVisto, 
													BigDecimal aChiaveAnnoSiep,
													BigDecimal aChiaveProgrSiep, 
													String aChiaveUfficioSiep, 
													String aCodUfficioMitt,
													String aCodUtenteMitt, 
													String aCodUffDest, 
													Date aDataRicercaInizio, 
													Date aDataRicercaFine,
													String aCognome, 
													String aNome,
													BigDecimal aIdMessSollecitato, 
													BigDecimal aChiaveAnnoFasCumulante,
													BigDecimal aChiaveProgrFasCumulante,
													String aChiaveUfficioFasCumulante,   
      int aPage) 
      throws DAOException 
  {
		String lSql = getSqlQueryMS();

		if (aDeliveryMode != null)
			lSql += " AND DELIVERY_MODE = '" + aDeliveryMode + "' ";
		if (aCodTipoMessaggio != null)
			lSql += " AND COD_TIPO_MESSAGGIO = '" + aCodTipoMessaggio + "' ";
		// if (aCodTipoOper!=null)
		// lSql += " AND COD_TIPO_OPERAZIONE = '" +aCodTipoOper+ "'";

		if (aListaTipoOperazione != null && aListaTipoOperazione.size() > 0) {
			lSql += " AND COD_TIPO_OPERAZIONE in (";
			for (int i = 0; i < aListaTipoOperazione.size(); i++) {
				lSql += " '" + aListaTipoOperazione.elementAt(i) + "'";
				if (i < aListaTipoOperazione.size() - 1)
					lSql += ",";
			}
			lSql += " ) ";
		}

		// if (aCodEsito != null)
		// lSql += " AND COD_ESITO = '" + aCodEsito + "'";

		if (aListaEsiti != null && aListaEsiti.size() > 0) {
			lSql += " AND COD_ESITO in (";
			for (int i = 0; i < aListaEsiti.size(); i++) {
				lSql += " '" + aListaEsiti.elementAt(i) + "'";
				if (i < aListaEsiti.size() - 1)
					lSql += ",";
			}
			lSql += " ) ";
		}

		if (sFlagVisto != null)
			lSql += " AND FLAG_VISTO = '" + sFlagVisto + "'";
		if (aChiaveAnnoSiep != null)
			lSql += " AND MESSAGGIO.CHIAVE_ANNO_SIEP = '" + aChiaveAnnoSiep + "'";
		if (aChiaveProgrSiep != null)
			lSql += " AND MESSAGGIO.CHIAVE_PROGR_SIEP = '" + aChiaveProgrSiep + "'";
		if (aCodUfficioMitt != null)
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aCodUfficioMitt + "'";
		if (aChiaveUfficioSiep != null)
			lSql += " AND MESSAGGIO.CHIAVE_UFFICIO_SIEP = '" + aChiaveUfficioSiep + "'";
		if (aCodUffDest != null && aCodUffDest != "-")
			lSql += " AND COD_UFFICIO_DESTINATARIO = '" + aCodUffDest + "'";
		if (aCodUtenteMitt != null)
			lSql += " AND CODICE_UTENTE_MITTENTE = '" + aCodUtenteMitt + "'";
		if (aDataRicercaInizio != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aDataRicercaInizio, "yyyyMMdd") + "'";
		if (aDataRicercaFine != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aDataRicercaFine, "yyyyMMdd") + "'";

    if (aCognome != null)
      lSql += " AND UPPER(COGNOME_SOGGETTO) = '" + aCognome.toUpperCase() + "'";
    if (aNome != null)
      lSql += " AND UPPER(NOME_SOGGETTO) = '" + aNome.toUpperCase() + "'";

          
		if (aIdMessSollecitato != null)
			lSql += " AND ID_MESSAGGIO_SOLLECITATO = '" + aIdMessSollecitato.toString() + "'";

    if (aChiaveAnnoFasCumulante != null)
    	lSql += " AND MESSAGGIO.CHIAVE_ANNO_FAS_CUMULANTE = '" + aChiaveAnnoFasCumulante + "'";
    if (aChiaveProgrFasCumulante != null)
    	lSql += " AND MESSAGGIO.CHIAVE_PROGR_FAS_CUMULANTE = '" + aChiaveProgrFasCumulante + "'";    
    if (aChiaveUfficioFasCumulante != null)
        lSql += " AND MESSAGGIO.CHIAVE_UFFICIO_FAS_CUMULANTE = '" + aChiaveUfficioFasCumulante + "'";   
    
		// lSql += " order by ID_MESSAGGIO, DATA_INVIO DESC " ; // dal piu' recente
		lSql += " order by DATA_INVIO DESC "; // dal piu' recente
		// lSql += setOrder();

		// Se la ricerca e' paginata estraggo solo i risultati da visualizzare sulla
		// pagina indicata
		if (aPage > 0) {
			lSql = " SELECT * FROM (SELECT INNER.* , Rownum rn FROM ( " + lSql
					+ " ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		}
    setStatement(lSql);
  }		

  /**
   * 
   * Effettua la ricerca dei Messaggi in base ai parametri Liste di Tipo Messaggio, Tipo Operazione ed Esiti, applicando i filtri 
   * impostati in maschera. 
   * 
   * @param aDeliveryMode
   * @param aListaTipoMessaggio
   * @param aListaTipoOperazione
   * @param aListaEsiti
   * @param sFlagVisto
   * @param aChiaveAnno
   * @param aChiaveProgr
   * @param aCodUfficioMitt
   * @param aCodUtenteMitt
   * @param aCodUffDest
   * @param aDataRicercaInizio
   * @param aDataRicercaFine
   * @param aPage - se > 0 vengono recuperati solo i dati per la pagina indicata
   * 
   * @throws DAOException
   * 
   * @since JUL/2017
   */
  public void ricercaMessaggioRicercaConFiltri(String aDeliveryMode
                                             , Vector <String> aListaTipoMessaggio
                                             , Vector <String> aListaTipoOperazione
                                             , Vector <String> aListaEsiti
                                             , String sFlagVisto
                                             , BigDecimal aChiaveAnnoSiep
                                             , BigDecimal aChiaveProgrSiep
                                             , String aChiaveUfficioSiep
                                             , String aCodUfficioMitt
                                             , String aCodUtenteMitt
                                             , String aCodUffDest
                                             , Date aDataRicercaInizio
                                             , Date aDataRicercaFine
                                             , String aCognome
                                             , String aNome
                                             , BigDecimal aIdMessSollecitato
                                             , BigDecimal aChiaveAnnoFasCumulante
                                             , BigDecimal aChiaveProgrFasCumulante
                                             , String aChiaveUfficioFasCumulante
                                             , int  aPage) throws DAOException
  {
    String lSql = getSqlQueryMS();
    
    if (aDeliveryMode!=null)
      lSql += " AND DELIVERY_MODE = '"+aDeliveryMode+"' ";   

    if (aListaTipoMessaggio!=null && aListaTipoMessaggio.size()>0){
        lSql += " AND COD_TIPO_MESSAGGIO in (";
        for (int i=0;i<aListaTipoMessaggio.size();i++) {
          lSql += " '"+aListaTipoMessaggio.elementAt(i)+"'";
          if (i<aListaTipoMessaggio.size()-1)
            lSql += ",";
        }      
        lSql += " ) ";      
      }   

    if (aListaTipoOperazione!=null && aListaTipoOperazione.size()>0){
      lSql += " AND COD_TIPO_OPERAZIONE in (";
      for (int i=0;i<aListaTipoOperazione.size();i++) {
        lSql += " '"+aListaTipoOperazione.elementAt(i)+"'";
        if (i<aListaTipoOperazione.size()-1)
          lSql += ",";
      }      
      lSql += " ) ";      
    }   
    
    if (aListaEsiti!=null && aListaEsiti.size()>0){
      lSql += " AND COD_ESITO in (";
      for (int i=0;i<aListaEsiti.size();i++) {
        lSql += " '"+aListaEsiti.elementAt(i)+"'";
        if (i<aListaEsiti.size()-1)
          lSql += ",";
      }      
      lSql += " ) ";      
    }
      
    if (sFlagVisto != null)
      lSql += " AND FLAG_VISTO = '" + sFlagVisto + "'";
    if (aChiaveAnnoSiep != null)
      lSql += " AND MESSAGGIO.CHIAVE_ANNO_SIEP = '" + aChiaveAnnoSiep + "'";
    if (aChiaveProgrSiep != null)
      lSql += " AND MESSAGGIO.CHIAVE_PROGR_SIEP = '" + aChiaveProgrSiep + "'";    
    if (aCodUfficioMitt != null)
      lSql += " AND COD_UFFICIO_MITTENTE = '" + aCodUfficioMitt + "'";   
    if (aChiaveUfficioSiep != null)
      lSql += " AND MESSAGGIO.CHIAVE_UFFICIO_SIEP = '" + aChiaveUfficioSiep + "'"; 
    if (aCodUffDest != null && aCodUffDest != "-")
      lSql += " AND COD_UFFICIO_DESTINATARIO = '" + aCodUffDest + "'";
    if (aCodUtenteMitt != null)
      lSql += " AND CODICE_UTENTE_MITTENTE = '" + aCodUtenteMitt + "'";
    if (aDataRicercaInizio != null )
      lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '" + DateUtils.getDateToString(aDataRicercaInizio, "yyyyMMdd" )+"'" ;
    if (aDataRicercaFine != null )
      lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '" + DateUtils.getDateToString(aDataRicercaFine, "yyyyMMdd" )+"'" ;

    if (aCognome != null)
      lSql += " AND UPPER(COGNOME_SOGGETTO) = '" + aCognome.toUpperCase() + "'";
    if (aNome != null)
      lSql += " AND UPPER(NOME_SOGGETTO) = '" + aNome.toUpperCase() + "'";
    
    
    if (aIdMessSollecitato != null )
      lSql += " AND ID_MESSAGGIO_SOLLECITATO = '" + aIdMessSollecitato.toString() +"'" ;
    
    
    if (aChiaveAnnoFasCumulante != null)
    	lSql += " AND MESSAGGIO.CHIAVE_ANNO_FAS_CUMULANTE = '" + aChiaveAnnoFasCumulante + "'";
    if (aChiaveProgrFasCumulante != null)
    	lSql += " AND MESSAGGIO.CHIAVE_PROGR_FAS_CUMULANTE = '" + aChiaveProgrFasCumulante + "'";    
    if (aChiaveUfficioFasCumulante != null)
        lSql += " AND MESSAGGIO.CHIAVE_UFFICIO_FAS_CUMULANTE = '" + aChiaveUfficioFasCumulante + "'";   
    
    //lSql += " order by ID_MESSAGGIO, DATA_INVIO DESC " ; // dal piÃ¹ recente
    lSql += " order by DATA_INVIO DESC " ; // dal piÃ¹ recente
    //lSql += setOrder();
    
    // Se la ricerca Ã¨ paginata estraggo solo i risultati da visualizzare sulla pagina indicata
    if (aPage>0){
      lSql = " SELECT * FROM (SELECT INNER.* , Rownum rn FROM ( "+lSql+" ) INNER ) WHERE rn between  "+((aPage-1)*IWebConstants.RESULT_PER_PAGE+1)+ " AND "+ (aPage)*IWebConstants.RESULT_PER_PAGE;
    }
    
    setStatement(lSql);
  }

	/**
	 * 
	 * @param aAnnoSiep
	 * @param aProgrSiep
	 * @param aUfficioSiep
	 * @param aUfficioMittente
	 * @param aUfficioDestinatario
	 * @param aUtente
	 * @param aEsito
	 * @param aTipoOperazione
	 * @param dataRicercaInizio
	 * @param dataRicercaFine
	 * @param aPage
	 * @throws DAOException
	 */
	public void ricercaMessaggioEsitoRicercaFascConFiltri(String aAnnoSiep, String aProgrSiep,
			String aUfficioSiep, String aUfficioMittente, String aUfficioDestinatario, String aUtente,
			String aEsito, String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine, int aPage)
			throws DAOException {
		String lCond = "";
		lCond += " AND COD_TIPO_MESSAGGIO = '03' ";
		if (aAnnoSiep != null && aAnnoSiep.length() > 1)
			lCond += " AND CHIAVE_ANNO_SIEP = '" + aAnnoSiep + "'";
		if (aProgrSiep != null && aProgrSiep.length() > 0)
			lCond += " AND CHIAVE_PROGR_SIEP = '" + aProgrSiep + "'";
		if (aUfficioSiep != null)
			lCond += " AND CHIAVE_UFFICIO_SIEP = '" + aUfficioSiep + "'";
		if (aUfficioMittente != null)
			lCond += " AND COD_UFFICIO_MITTENTE = '" + aUfficioMittente + "'";
		if (aUfficioDestinatario != null && aUfficioDestinatario.length() > 1)
			lCond += " AND COD_UFFICIO_DESTINATARIO = '" + aUfficioDestinatario + "'";
		if (aUtente != null)
			lCond += " AND CODICE_UTENTE_MITTENTE = '" + aUtente + "'";
		if (!(aTipoOperazione == null || aTipoOperazione.compareTo("-") == 0))
			lCond += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";
		if (dataRicercaInizio != null)
			lCond += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(dataRicercaInizio, "yyyyMMdd") + "'";
		if (dataRicercaFine != null)
			lCond += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(dataRicercaFine, "yyyyMMdd") + "'";

		// lCond += " order by COD_UFFICIO_MITTENTE, DATA_INVIO DESC " ;
		// lSql += setOrder();

		String lSql = getSqlQueryPerRicerca(aPage, lCond);

		setStatement(lSql);

	}

	public void ricercaMessaggioEsitoPerUfficio(String aUfficio) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '02' AND COD_UFFICIO_DESTINATARIO = '" + aUfficio + "'";
		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioRichiestaRicercaSoggettoPerUfficio(String aUfficio, String aUtente,
			String aTipoOperazione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '03' AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'"
				+ " AND CODICE_UTENTE_MITTENTE = '" + aUtente + "' AND COD_TIPO_OPERAZIONE = '"
				+ aTipoOperazione + "'";

		lSql += setOrder();

		setStatement(lSql);
	}

	public void ricercaMessaggioRichiestaRicercaSoggettoPerUfficioPaged(String aUfficio, String aUtente,
			String aTipoOperazione, int aPage) throws DAOException {
		String lSql = getSqlQuery();
		String lPaginedStatement = new String("");

		lSql += " AND COD_TIPO_MESSAGGIO = '03' AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'"
				+ " AND CODICE_UTENTE_MITTENTE = '" + aUtente + "' AND COD_TIPO_OPERAZIONE = '"
				+ aTipoOperazione + "'";

		lSql += setOrder();
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE_ESITO + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE_ESITO;

		setStatement(lPaginedStatement);
	}

	public void getCountMessaggioRichiestaRicercaSoggettoPerUfficioPaged(String aUfficio, String aUtente,
			String aTipoOperazione) throws DAOException

	{
		String lStatement = getSqlCountMessaggi();
		lStatement += " WHERE COD_TIPO_MESSAGGIO = '03' AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'"
				+ " AND CODICE_UTENTE_MITTENTE = '" + aUtente + "' AND COD_TIPO_OPERAZIONE = '"
				+ aTipoOperazione + "'";
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	/*
	 * Da ottimizzare 20070614
	 */
	public void ricercaMessaggioRichiestaPerTipoOperazioneFascicolo(String aUfficioRicevente,
			String aUfficioMittente, String aTipoOperazione, BigDecimal aAnnoFascicolo,
			BigDecimal aProgrFascicolo, String aIncludeInCarico) throws DAOException {
		String lSql = getSqlQuery(false);

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '" + aUfficioRicevente + "'";
		if (!aUfficioMittente.equals("-")) // 26/06/2006
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficioMittente + "'";
		if (aTipoOperazione != null && // 2007-06-25 Patch Migliorabile
				(aTipoOperazione.trim().length() == 5 && aTipoOperazione.compareTo("SIEPE") != 0)) // inserito
																									// controllo
																									// su
																									// "SIEPE"
			lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'"; // poiche' la variabile
																			// TipoOperazione
																			// viene utilizzata anche come
																			// flag
		// STUB 15/03/2005 Se si e' inclusi gli atti presi in carico, va esclusa la condizione sul FLAG_VISTO.
		if (aIncludeInCarico.compareTo("S") != 0)
			lSql += "  AND FLAG_VISTO='N' ";

		// Trasferimento ISTANZA, PROVVEDIMENTO, RIF_FAS_SIUS.
		if (aTipoOperazione != null
				&& (aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_ISTANZA) == 0
						|| aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO) == 0
						|| aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RIF_FAS_SIUS) == 0 || aTipoOperazione
						.compareTo("SIEP") == 0)) {
			if (aAnnoFascicolo != null)
				lSql += " AND CHIAVE_ANNO_SIEP = '" + aAnnoFascicolo + "'";
			if (aProgrFascicolo != null)
				lSql += " AND CHIAVE_PROGR_SIEP = '" + aProgrFascicolo + "'";
		}
		// Trasferimento DECRETO, ORDINANZA, RICORSO, SENTENZA.
		else if (aTipoOperazione != null
				&& (aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_DECRETO) == 0
						|| aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_ORDINANZA) == 0
						|| aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RICORSO) == 0
						|| aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_SENTENZA) == 0 || aTipoOperazione
						.compareTo("SIUS") == 0)) {
			if (aAnnoFascicolo != null)
				lSql += " AND CHIAVE_ANNO_SIUS = '" + aAnnoFascicolo + "'";
			if (aProgrFascicolo != null)
				lSql += " AND CHIAVE_PROGR_SIUS = '" + aProgrFascicolo + "'";
		}

		// Trasferimento ATTIVITA, RICHIESTA_UEPE, RELAZIONE_UEPE, RICHIESTA_RELAZIONE.
		else if (aTipoOperazione != null
				&& (aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_ATTIVITA) == 0
						|| aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE) == 0
						|| aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RELAZIONE_UEPE) == 0
						|| aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_RELAZIONE) == 0 || aTipoOperazione
						.compareTo("SIEPE") == 0)) {
			if (aAnnoFascicolo != null)
				lSql += " AND CHIAVE_ANNO_SIEPE = '" + aAnnoFascicolo + "'";
			if (aProgrFascicolo != null)
				lSql += " AND CHIAVE_PROGR_SIEPE = '" + aProgrFascicolo + "'";
		}

		lSql += setOrder();
		setStatement(lSql);
	}

	/*
	 * 2007-06-14 - Metodi otiimizzati per la risoluzione del problema della inclusione public void
	 * ricercaMessaggioRichiestaPerTipoOperazioneFascicolo( String aUfficioRicevente, String aUfficioMittente,
	 * String aTipoOperazione, BigDecimal aAnnoFascicolo, BigDecimal aProgrFascicolo, String aIncludeInCarico
	 * ) throws DAOException {
	 * 
	 * ricercaMessaggioRichiestaPerTipoOperazioneFascicolo( aUfficioRicevente, aUfficioMittente,
	 * aTipoOperazione, aAnnoFascicolo, aProgrFascicolo, aIncludeInCarico, null );
	 * 
	 * }
	 * 
	 * public void ricercaMessaggioRichiestaPerTipoOperazioneFascicolo( String aUfficioRicevente, String
	 * aUfficioMittente, String aTipoOperazione, BigDecimal aAnnoFascicolo, BigDecimal aProgrFascicolo, String
	 * aIncludeInCarico, String aFlagVisto ) throws DAOException {
	 * 
	 * String lSql = getSqlQuery();
	 * 
	 * lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '" + aUfficioRicevente + "'";
	 * 
	 * if (!aUfficioMittente.equals("-")) // 26/06/2006 lSql += " AND COD_UFFICIO_MITTENTE = '" +
	 * aUfficioMittente + "'"; if (aTipoOperazione != null && (aTipoOperazione.trim().length() == 5 ) ) lSql
	 * += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";
	 * 
	 * // STUB 15/03/2005 Se si e' inclusi gli atti presi in carico, va esclusa la condizione sul FLAG_VISTO.
	 * // 20070614 - Riadattato per impostare dati di flagVisto, per filtro su occorrenze if(aIncludeInCarico
	 * != null && aIncludeInCarico.compareTo("S")!=0) lSql += " AND FLAG_VISTO='N' "; else if( aFlagVisto !=
	 * null && aFlagVisto.compareTo("-")!=0 ) lSql += " AND FLAG_VISTO='" + aFlagVisto + "' ";
	 * 
	 * // Trasferimento ISTANZA, PROVVEDIMENTO, RIF_FAS_SIUS. if (aTipoOperazione != null &&
	 * (aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_ISTANZA)==0 ||
	 * aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO)==0 ||
	 * aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RIF_FAS_SIUS)==0 ||
	 * aTipoOperazione.compareTo("SIEP")==0 ) ) { if (aAnnoFascicolo != null) lSql +=
	 * " AND CHIAVE_ANNO_SIEP = '" + aAnnoFascicolo + "'"; if (aProgrFascicolo != null) lSql +=
	 * " AND CHIAVE_PROGR_SIEP = '" + aProgrFascicolo + "'"; } // Trasferimento DECRETO, ORDINANZA, RICORSO.
	 * else if (aTipoOperazione != null && (aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_DECRETO)==0
	 * || aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_ORDINANZA)==0 ||
	 * aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RICORSO)==0 ||
	 * aTipoOperazione.compareTo("SIUS")==0 ) ) { if (aAnnoFascicolo != null) lSql +=
	 * " AND CHIAVE_ANNO_SIUS = '" + aAnnoFascicolo + "'"; if (aProgrFascicolo != null) lSql +=
	 * " AND CHIAVE_PROGR_SIUS = '" + aProgrFascicolo + "'"; }
	 * 
	 * // Trasferimento ATTIVITA, RICHIESTA_UEPE, RELAZIONE_UEPE, RICHIESTA_RELAZIONE. else if
	 * (aTipoOperazione != null && (aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_ATTIVITA)==0 ||
	 * aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE)==0 ||
	 * aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RELAZIONE_UEPE)==0 ||
	 * aTipoOperazione.compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_RELAZIONE)==0 ||
	 * aTipoOperazione.compareTo("SIEPE")==0 ) ) { if (aAnnoFascicolo != null) lSql +=
	 * " AND CHIAVE_ANNO_SIEPE = '" + aAnnoFascicolo + "'"; if (aProgrFascicolo != null) lSql +=
	 * " AND CHIAVE_PROGR_SIEPE = '" + aProgrFascicolo + "'"; }
	 * 
	 * lSql += setOrder(); setStatement(lSql); }
	 */

	/**
	 * Esegue laricerca dei messaggi ricevuti, filtrati per il fascicolo etipo operazione e flag di stato.
	 * <p>
	 * 
	 * @param aMessaggio
	 * @param aIncludeInCarico
	 * @throws DAOException
	 */
	public void ricercaMessaggioRichiestaPerTipoOperazioneFascicolo(MessaggioModel aMessaggio,
			String aIncludeInCarico) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_MESSAGGIO = '01' AND COD_UFFICIO_DESTINATARIO = '"
				+ aMessaggio.getCodUfficioDestinatario() + "'";

		if (!aMessaggio.getCodUfficioMittente().equals("-") && !aMessaggio.getCodUfficioMittente().equals("")) // 26/06/2006
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aMessaggio.getCodUfficioMittente() + "'";

		if (aMessaggio.getCodTipoOperazione() != null && // 2007-06-25 Patch Migliorabile
				aMessaggio.getCodTipoOperazione().trim().length() == 5 && // inserito controllo su "SIEPE"
				aMessaggio.getCodTipoOperazione().compareTo("SIEPE") != 0) // poiche' la variabile
																			// TipoOperazione viene utilizzata
																			// anche come flag
			lSql += " AND COD_TIPO_OPERAZIONE = '" + aMessaggio.getCodTipoOperazione() + "'";

		// STUB 15/03/2005 Se si e' inclusi gli atti presi in carico, va esclusa la condizione sul FLAG_VISTO.
		// 20070614 - Riadattato per impostare dati di flagVisto, per filtro su occorrenze
		if (aIncludeInCarico != null && aIncludeInCarico.compareTo("S") != 0)
			lSql += " AND FLAG_VISTO='N' ";
		else if (aMessaggio.getFlagVisto() != null && aMessaggio.getFlagVisto().compareTo("-") != 0)
			lSql += " AND FLAG_VISTO='" + aMessaggio.getFlagVisto() + "' ";

		// Trasferimento ISTANZA, PROVVEDIMENTO, RIF_FAS_SIUS.
		if (aMessaggio.getCodTipoOperazione() != null
				&& (aMessaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ISTANZA) == 0
						|| aMessaggio.getCodTipoOperazione().compareTo(
								ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO) == 0
						|| aMessaggio.getCodTipoOperazione().compareTo(
								ICostantiJMS.TRASFERIMENTO_RIF_FAS_SIUS) == 0 || aMessaggio
						.getCodTipoOperazione().compareTo("SIEP") == 0)) {

			if (aMessaggio.getChiaveAnnoSiep() != null)
				lSql += " AND CHIAVE_ANNO_SIEP = '" + aMessaggio.getChiaveAnnoSiep() + "'";

			if (aMessaggio.getChiaveProgrSiep() != null)
				lSql += " AND CHIAVE_PROGR_SIEP = '" + aMessaggio.getChiaveProgrSiep() + "'";
		}
		// Trasferimento DECRETO, ORDINANZA, RICORSO.
		else if (aMessaggio.getCodTipoOperazione() != null
				&& (aMessaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_DECRETO) == 0
						|| aMessaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ORDINANZA) == 0
						|| aMessaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_RICORSO) == 0
						|| aMessaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_SENTENZA) == 0 || aMessaggio
						.getCodTipoOperazione().compareTo("SIUS") == 0)) {
			if (aMessaggio.getChiaveAnnoSius() != null)
				lSql += " AND CHIAVE_ANNO_SIUS = '" + aMessaggio.getChiaveAnnoSius() + "'";

			if (aMessaggio.getChiaveProgrSius() != null)
				lSql += " AND CHIAVE_PROGR_SIUS = '" + aMessaggio.getChiaveProgrSius() + "'";
		}
		// Trasferimento ATTIVITA, RICHIESTA_UEPE, RELAZIONE_UEPE, RICHIESTA_RELAZIONE.
		else if (aMessaggio.getCodTipoOperazione() != null
				&& (aMessaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ATTIVITA) == 0
						|| aMessaggio.getCodTipoOperazione().compareTo(
								ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE) == 0
						|| aMessaggio.getCodTipoOperazione().compareTo(
								ICostantiJMS.TRASFERIMENTO_RELAZIONE_UEPE) == 0
						|| aMessaggio.getCodTipoOperazione().compareTo(
								ICostantiJMS.TRASFERIMENTO_RICHIESTA_RELAZIONE) == 0 || aMessaggio
						.getCodTipoOperazione().compareTo("SIEPE") == 0)) {
			if (aMessaggio.getChiaveAnnoSiepe() != null)
				lSql += " AND CHIAVE_ANNO_SIEPE = '" + aMessaggio.getChiaveAnnoSiepe() + "'";

			if (aMessaggio.getChiaveProgrSiepe() != null)
				lSql += " AND CHIAVE_PROGR_SIEPE = '" + aMessaggio.getChiaveProgrSiepe() + "'";
		}

		lSql += setOrder();
		setStatement(lSql);
	}

	public void ricercaMessaggioByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		lSql += setOrder();
		setStatement(lSql);
	}

  public void ricercaMessaggioByIdRichiesta( String aTipoMes, String aTipoOperazione, String aCodUffMittenete, BigDecimal aIdRichiesta) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " AND COD_TIPO_MESSAGGIO = '" +aTipoMes+ "'";
    lSql += " AND COD_TIPO_OPERAZIONE = '" +aTipoOperazione+ "'";
  //  lSql += " AND DELIVERY_MODE = '" +adeliveryMod+ "'";
    
    lSql += " AND ID_RICHIESTA = "+aIdRichiesta;
    
    if (aCodUffMittenete != null)
      lSql += " AND COD_UFFICIO_MITTENTE = '" + aCodUffMittenete + "'";
 
  //  lSql += " order by COD_UFFICIO_MITTENTE, DATA_INVIO DESC " ;
    
    
    lSql += setOrder();
    setStatement(lSql);
  }
  
  public void RicercaSollecitiMessaggioRichiestaAtti( String aTipoMes, String aTipoOperazione, String aIdMesSollecitato) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " AND COD_TIPO_MESSAGGIO = '" +aTipoMes+ "'";
    lSql += " AND COD_TIPO_OPERAZIONE = '" +aTipoOperazione+ "'";
  //  lSql += " AND DELIVERY_MODE = '" +adeliveryMod+ "'";
    
    lSql += " AND ID_MESSAGGIO_SOLLECITATO = '"+aIdMesSollecitato+"'";
 
  //  lSql += " order by COD_UFFICIO_MITTENTE, DATA_INVIO DESC " ;
    
    
    lSql += setOrder();
    setStatement(lSql);
  }
  
  public void RicercaSolleciti( String aTipoOperazione, String aIdMesSollecitato) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " AND COD_TIPO_OPERAZIONE = '" +aTipoOperazione+ "'";
    lSql += " AND ID_MESSAGGIO_SOLLECITATO = '"+aIdMesSollecitato+"'";
    
    lSql += setOrder();
    setStatement(lSql);
  }
  
	protected String getSqlQuery(boolean aFlagBlob) {

		String lStatement = new String("");

		lStatement += " SELECT  "
				+ " ID_MESSAGGIO,  "
				+ " COD_TIPO_MESSAGGIO, tipoMess.DESCRIZIONE DescrTipoMessaggio, "
				+ " COD_TIPO_OPERAZIONE,  tipOperazione.DESCRIZIONE DescrTipoOperazione,"
				+ " COD_UFFICIO_MITTENTE,  "
				+ " COD_BDI_MITTENTE,  mit.DESCRIZIONE bdiMit,"
				+ " COD_UFFICIO_DESTINATARIO, comuffdest.DESCRIZIONE destSede,tipuffdest.RV_MEANING tipodest,"
				+ " comuff.DESCRIZIONE sede, tipuff.RV_MEANING tipouff,"
				+ " COD_BDI_DESTINATARIA,  dest.descrizione bdiDest," + " DATA_INVIO,  " + " DATA_ESITO,  "
				+ " FLAG_VISTO,  " + " CODICE_UTENTE_MITTENTE,  "
				+ " COD_ESITO, ESITO.DESCRIZIONE DESCR_ESITO ,"
				+ " JMS_ID_MESSAGE, JMS_CORRELATION_ID_MESSAGE, "
				+ (aFlagBlob ? " BLOB_ESITO, " : "")
				+ " CHIAVE_ANNO_SIEP, "
				+ " CHIAVE_PROGR_SIEP, "
				+
				// =======================
				// new d.f. since NOV/2013 per gestire l'inoltro ad altro ufficio
				" CHIAVE_UFFICIO_SIEP, "
				+ " tipo_ufficio_fasc_siep.RV_MEANING descr_ufficio_siep, "
				+ " comune_fasc_siep.DESCRIZIONE sede_ufficio_siep, "
				+ " DELIVERY_MODE, "
				+ " COD_UFFICIO_INOLTRO, tipUffInoltro.RV_MEANING descrUffInoltro, comUffInoltro.DESCRIZIONE descrSedeUfficioInoltro,"
				+ " COD_BDI_INOLTRO, "
				+ " COD_UFFICIO_REPLY_TO, tipuffReplyTo.RV_MEANING descrUffReplyTo, comuffReplyTo.DESCRIZIONE descrSedeUfficioReplyTo,"
				+ " COD_BDI_REPLY_TO, "
				+ " JMS_CORRELATION_REPLY_TO, "
				+ " ID_MESSAGGIO_SOLLECITATO, "
				+ " ID_RICHIESTA, " 
				//
				// ======================
				+ " CHIAVE_ANNO_SIUS, "
				+ " CHIAVE_PROGR_SIUS, "
				+ " CHIAVE_ANNO_FAS_CUMULANTE, "
				+ " CHIAVE_PROGR_FAS_CUMULANTE, "      
        + " CHIAVE_UFFICIO_FAS_CUMULANTE, " 
        + " tipo_ufficio_fasc_siep_cum.RV_MEANING descr_ufficio_siep_cum, "
        + " comune_fasc_siep_cum.DESCRIZIONE sede_ufficio_siep_cum, "
				+ " NOTE, "
				+ " CHIAVE_ANNO_SIEPE, CHIAVE_PROGR_SIEPE, COGNOME_SOGGETTO, NOME_SOGGETTO, DATA_NASCITA, COD_STATO_NASCITA, COD_COMUNE_NASCITA "
				+ " FROM MESSAGGIO,"
				+ " JMS_CODE mit,JMS_CODE dest, JMS_CODE ESITO,ufficio uff, comune comuff,cg_ref_codes tipuff, "
				+ " ufficio uffdest,comune comuffdest, cg_ref_codes tipuffdest, JMS_CODE tipoMess,JMS_CODE tipOperazione "
				// Aggiunte join per recuperare i dati descrittivi dell'ufficio siep: CHIAVE_UFFICIO_SIEP
				+ " , UFFICIO ufficio_fasc_siep, COMUNE comune_fasc_siep ,CG_REF_CODES tipo_ufficio_fasc_siep "
        + " , UFFICIO ufficio_fasc_siep_cum, COMUNE comune_fasc_siep_cum ,CG_REF_CODES tipo_ufficio_fasc_siep_cum "
				+ " , UFFICIO uffInoltro, COMUNE comUffInoltro, CG_REF_CODES tipUffInoltro "
				+ " , UFFICIO uffReplyTo, COMUNE comuffReplyTo, CG_REF_CODES tipuffReplyTo "
				+ " WHERE "
				+ " COD_TIPO_MESSAGGIO = tipoMess.CODICE and tipoMess.dominio = 'TIPO_MESSAGGIO'  "
				+ " and COD_TIPO_OPERAZIONE = tipOperazione.CODICE and tipOperazione.dominio = 'TIPO_OPERAZIONE'  "
				+ " and COD_BDI_MITTENTE = mit.CODICE and mit.dominio = 'BDI'  "
				+ " and COD_BDI_DESTINATARIA = dest.CODICE and dest.dominio = 'BDI'"
				+ " and ESITO.CODICE = COD_ESITO AND ESITO.dominio = 'CODICE_ESITO'"
				+ " and uff.COD_UFFICIO = COD_UFFICIO_MITTENTE"
				+ " and comuff.COD_COMUNE = uff.COD_COMUNE"
				+ " and uff.COD_TIPO_UFFICIO =tipuff.RV_LOW_VALUE"
				+ " and tipuff.RV_DOMAIN ='TIPO_UFFICIO'"
				+ " and uffdest.COD_UFFICIO = COD_UFFICIO_DESTINATARIO"
				+ " and comuffdest.COD_COMUNE = uffdest.COD_COMUNE"
				+ " and uffdest.COD_TIPO_UFFICIO =tipuffdest.RV_LOW_VALUE"
				+ " and tipuffdest.RV_DOMAIN ='TIPO_UFFICIO'"
				+
				// Aggiunte join per recuperare i dati descrittivo dell'ufficio siep: CHIAVE_UFFICIO_SIEP
				" AND ufficio_fasc_siep.COD_UFFICIO = nvl(MESSAGGIO.CHIAVE_UFFICIO_SIEP, '-' ) "

				+ // n.b. CHIAVE_UFFICIO_SIEP potrebbe essere null
				" AND ufficio_fasc_siep.COD_COMUNE = comune_fasc_siep.COD_COMUNE "
				+ " AND ufficio_fasc_siep.COD_TIPO_UFFICIO = tipo_ufficio_fasc_siep.RV_LOW_VALUE "
				+ " AND tipo_ufficio_fasc_siep.RV_DOMAIN ='TIPO_UFFICIO' "				
        
        //
        + " AND ufficio_fasc_siep_cum.COD_UFFICIO = nvl(MESSAGGIO.CHIAVE_UFFICIO_FAS_CUMULANTE, '-' ) "  //n.b. CHIAVE_UFFICIO_FAS_CUMULANTE potrebbe essere null
        + " AND ufficio_fasc_siep_cum.COD_COMUNE = comune_fasc_siep_cum.COD_COMUNE " 
        + " AND ufficio_fasc_siep_cum.COD_TIPO_UFFICIO = tipo_ufficio_fasc_siep_cum.RV_LOW_VALUE " 
        + " AND tipo_ufficio_fasc_siep_cum.RV_DOMAIN ='TIPO_UFFICIO' "         
        
				// ============ UFFICIO/BDI INOLTRO =========================
				+ " AND uffInoltro.COD_UFFICIO = nvl(MESSAGGIO.COD_UFFICIO_INOLTRO, '-' ) "
				+ " AND uffInoltro.COD_COMUNE = comUffInoltro.COD_COMUNE "
				+ " AND uffInoltro.COD_TIPO_UFFICIO = tipUffInoltro.RV_LOW_VALUE "
				+ " AND tipUffInoltro.RV_DOMAIN ='TIPO_UFFICIO' "
				+
				// ============ UFFICIO/BDI REPLY TO =========================
				" AND uffReplyTo.COD_UFFICIO = nvl(MESSAGGIO.COD_UFFICIO_REPLY_TO, '-' ) "
				+ " AND uffReplyTo.COD_COMUNE = comuffReplyTo.COD_COMUNE "
				+ " AND uffReplyTo.COD_TIPO_UFFICIO = tipuffReplyTo.RV_LOW_VALUE "
				+ " AND tipuffReplyTo.RV_DOMAIN ='TIPO_UFFICIO' ";

		return lStatement;

	}

	/**
	 * Query join con a tabella FASC_MS_TO_FASC_SIEP per recuperare anche le
	 * 
	 * @return
	 */
	protected String getSqlQueryMS() {

		String lStatement = new String("");
		lStatement = "SELECT  ID_MESSAGGIO, "
				+ " COD_TIPO_MESSAGGIO, tipoMess.DESCRIZIONE DescrTipoMessaggio, "
				+ " COD_TIPO_OPERAZIONE,  tipOperazione.DESCRIZIONE DescrTipoOperazione, "
				+ " COD_UFFICIO_MITTENTE,  "
				+ " COD_BDI_MITTENTE,  bdiMitt.DESCRIZIONE bdiMit, "
				+ " COD_UFFICIO_DESTINATARIO, comuffdest.DESCRIZIONE destSede, tipuffdest.RV_MEANING tipodest, "
				+ " comuffMitt.DESCRIZIONE sede, tipuffMitt.RV_MEANING tipouff, "
				+ " COD_BDI_DESTINATARIA, bdiDest.descrizione bdiDest, "
				+ " DATA_INVIO, DATA_ESITO, "
				+ " FLAG_VISTO, "
				+ " CODICE_UTENTE_MITTENTE, "
				+ " COD_ESITO, esito.DESCRIZIONE DESCR_ESITO , "
				+ " JMS_ID_MESSAGE, JMS_CORRELATION_ID_MESSAGE, "
				+ " MESSAGGIO.CHIAVE_ANNO_SIEP, "
				+ " MESSAGGIO.CHIAVE_PROGR_SIEP, "
				+ " BLOB_ESITO, "
				+
				// =======================
				" MESSAGGIO.CHIAVE_UFFICIO_SIEP, "
				+ // new d.f. since NOV/2013 per gestire l'inoltro ad altro ufficio
				" tipo_ufficio_fasc_siep.RV_MEANING descr_ufficio_siep, "
				+ // new d.f. since NOV/2013 per gestire l'inoltro ad altro ufficio
				" comune_fasc_siep.DESCRIZIONE sede_ufficio_siep, "
				+ // new d.f. since NOV/2013 per gestire l'inoltro ad altro ufficio
				// ======================
				" CHIAVE_ANNO_SIUS, "
				+ " CHIAVE_PROGR_SIUS, "
				+ " CHIAVE_ANNO_FAS_CUMULANTE, "
				+ " CHIAVE_PROGR_FAS_CUMULANTE, "
				+ " NOTE, "
				+ " CHIAVE_ANNO_SIEPE, CHIAVE_PROGR_SIEPE, "
				+ " COGNOME_SOGGETTO, NOME_SOGGETTO, DATA_NASCITA, COD_STATO_NASCITA, COD_COMUNE_NASCITA, "
				+
				// ==============================
				" DELIVERY_MODE, "
				+ " COD_UFFICIO_INOLTRO, tipUffInoltro.RV_MEANING descrUffInoltro, comUffInoltro.DESCRIZIONE descrSedeUfficioInoltro,"
				+ " COD_BDI_INOLTRO, "
				+ " COD_UFFICIO_REPLY_TO, tipuffReplyTo.RV_MEANING descrUffReplyTo, comuffReplyTo.DESCRIZIONE descrSedeUfficioReplyTo,"
				+ " COD_BDI_REPLY_TO, "
				+ " JMS_CORRELATION_REPLY_TO, "
				+ " ID_MESSAGGIO_SOLLECITATO, "
				+ " ID_RICHIESTA, " 
				// " --==============================" +
				+ " FASC_MS_TO_FASC_SIEP.ID_FASC_MS_TO_FASC_SIEP, "
				+ " FASC_MS_TO_FASC_SIEP.COD_TIPO_RELAZIONE_MS, "
				+	" FASC_MS_TO_FASC_SIEP.FAS_SIE_ID_FASCICOLO_SIEP as FAS_SIE_ID_FASCICOLO_SIEP_X, "
				+ " FASC_MS_TO_FASC_SIEP.CHIAVE_ANNO_SIEP as CHIAVE_ANNO_SIEP_X, "
				+ " FASC_MS_TO_FASC_SIEP.CHIAVE_PROGR_SIEP as CHIAVE_PROGR_SIEP_X, "
				+ " FASC_MS_TO_FASC_SIEP.CHIAVE_UFFICIO_SIEP as CHIAVE_UFFICIO_SIEP_X, "
				+	" FASC_MS_TO_FASC_SIEP.FAS_SIE_ID_FASCICOLO_COLLEGATO, "
				+ " FASC_MS_TO_FASC_SIEP.CHIAVE_ANNO_SIEP_COLLEGATO, "
				+ " FASC_MS_TO_FASC_SIEP.CHIAVE_PROGR_SIEP_COLLEGATO, "
				+ " FASC_MS_TO_FASC_SIEP.CHIAVE_UFFICIO_SIEP_COLLEGATO "
				// Ticket#20220111018 - ottimizzazione recupero solleciti: si cerca subito se presenti solleciti
				// sul messaggio (count). Le funzioni di ricerca ciclano sul risultato della ricerca (vettore)
				// per recuperare eventuali solleciti. In questo modo già sanno se presenti ed si evitano 
				// query inutili.
				// Si ricercano tra i messaggi ricevuti (DELIVERY_MODE = '00002'), di tipo richiesta (COD_TIPO_MESSAGGIO = '01')
				// di qualsiazi tipo (COD_TIPO_OPERAZIONE). Non si entra nello specifico del tipo di sollecito
				// ne dell'ufficio. Sarà la successiva query a mettere dei filtri più stringenti.
                + " , (select COUNT(*) "
                + "      from MESSAGGIO solleciti "
                + "     where 1=1 "
				+ "       AND solleciti.DELIVERY_MODE = '00002' "
				+ "       AND solleciti.COD_TIPO_MESSAGGIO = '01'  "
				+ "       AND solleciti.COD_TIPO_OPERAZIONE in ( '00073', '00076','00068')  "
					//+ " AND solleciti.COD_UFFICIO_DESTINATARIO = '00127202101'    "  // in questo metodo non lo conosco s cui ometto il filtro
				+ "       AND (   solleciti.ID_MESSAGGIO_SOLLECITATO = MESSAGGIO.JMS_CORRELATION_ID_MESSAGE "
				+ "            OR solleciti.ID_MESSAGGIO_SOLLECITATO = MESSAGGIO.JMS_CORRELATION_REPLY_TO "
				+ "           ) "
				+ "   ) contaSolleciti "
			    // Ticket#20220111018 - ottimizzazione				
				+ " FROM MESSAGGIO LEFT OUTER JOIN FASC_MS_TO_FASC_SIEP ON (    MESSAGGIO.ID_MESSAGGIO     = FASC_MS_TO_FASC_SIEP.MES_ID_MESSAGGIO) "
				+
				// " FROM MESSAGGIO LEFT OUTER JOIN FASC_MS_TO_FASC_SIEP ON (    MESSAGGIO.CHIAVE_ANNO_SIEP     = FASC_MS_TO_FASC_SIEP.CHIAVE_ANNO_SIEP "
				// +
				// "  AND MESSAGGIO.CHIAVE_PROGR_SIEP    = FASC_MS_TO_FASC_SIEP.CHIAVE_PROGR_SIEP " +
				// "  AND MESSAGGIO.CHIAVE_UFFICIO_SIEP  = FASC_MS_TO_FASC_SIEP.CHIAVE_UFFICIO_SIEP" +
				// "  AND MESSAGGIO.COD_UFFICIO_DESTINATARIO = FASC_MS_TO_FASC_SIEP.CHIAVE_UFFICIO_CLASSE_IV) "
				// +
				" , JMS_CODE tipoMess, JMS_CODE tipOperazione, JMS_CODE ESITO "
				+ " , JMS_CODE bdiMitt, UFFICIO uffMitt, COMUNE comuffMitt, CG_REF_CODES tipuffMitt "
				+ " , JMS_CODE bdiDest, UFFICIO uffdest, COMUNE comuffdest, CG_REF_CODES tipuffdest "
				+ " , UFFICIO uffInoltro, COMUNE comUffInoltro, CG_REF_CODES tipUffInoltro "
				+ " , UFFICIO uffReplyTo, COMUNE comuffReplyTo, CG_REF_CODES tipuffReplyTo "
				+ " , UFFICIO ufficio_fasc_siep, COMUNE comune_fasc_siep ,CG_REF_CODES tipo_ufficio_fasc_siep "
				+ " WHERE COD_TIPO_MESSAGGIO = tipoMess.CODICE and tipoMess.dominio = 'TIPO_MESSAGGIO'  "
				+ " AND COD_TIPO_OPERAZIONE = tipOperazione.CODICE and tipOperazione.dominio = 'TIPO_OPERAZIONE'  "
				+ " AND COD_ESITO = esito.CODICE AND esito.DOMINIO = 'CODICE_ESITO'"
				+
				// ============ BDI/UFFICIO MITTENTE ==========================
				" AND COD_BDI_MITTENTE = bdiMitt.CODICE and bdiMitt.dominio = 'BDI'  "
				+ " AND COD_UFFICIO_MITTENTE = uffMitt.COD_UFFICIO"
				+ " AND uffMitt.COD_COMUNE = comuffMitt.COD_COMUNE"
				+ " AND uffMitt.COD_TIPO_UFFICIO = tipuffMitt.RV_LOW_VALUE"
				+ " AND tipuffMitt.RV_DOMAIN ='TIPO_UFFICIO'"
				+
				// ============ BDI/UFFICIO DESTINATARIO ======================
				" AND COD_BDI_DESTINATARIA = bdiDest.CODICE and bdiDest.dominio = 'BDI'  "
				+ " AND COD_UFFICIO_DESTINATARIO = uffdest.COD_UFFICIO "
				+ " AND comuffdest.COD_COMUNE = uffdest.COD_COMUNE"
				+ " AND uffdest.COD_TIPO_UFFICIO = tipuffdest.RV_LOW_VALUE "
				+ " AND tipuffdest.RV_DOMAIN ='TIPO_UFFICIO' "
				+
				// ============ UFFICIO FASCIOLO SIEP =========================
				" AND ufficio_fasc_siep.COD_UFFICIO = nvl(MESSAGGIO.CHIAVE_UFFICIO_SIEP, '-' ) "
				+ " AND ufficio_fasc_siep.COD_COMUNE = comune_fasc_siep.COD_COMUNE "
				+ " AND ufficio_fasc_siep.COD_TIPO_UFFICIO = tipo_ufficio_fasc_siep.RV_LOW_VALUE "
				+ " AND tipo_ufficio_fasc_siep.RV_DOMAIN ='TIPO_UFFICIO' "
				+
				// ============ UFFICIO/BDI INOLTRO =========================
				" AND uffInoltro.COD_UFFICIO = nvl(MESSAGGIO.COD_UFFICIO_INOLTRO, '-' ) "
				+ " AND uffInoltro.COD_COMUNE = comUffInoltro.COD_COMUNE "
				+ " AND uffInoltro.COD_TIPO_UFFICIO = tipUffInoltro.RV_LOW_VALUE "
				+ " AND tipUffInoltro.RV_DOMAIN ='TIPO_UFFICIO' "
				+
				// ============ UFFICIO/BDI REPLY TO =========================
				" AND uffReplyTo.COD_UFFICIO = nvl(MESSAGGIO.COD_UFFICIO_REPLY_TO, '-' ) "
				+ " AND uffReplyTo.COD_COMUNE = comuffReplyTo.COD_COMUNE "
				+ " AND uffReplyTo.COD_TIPO_UFFICIO = tipuffReplyTo.RV_LOW_VALUE "
				+ " AND tipuffReplyTo.RV_DOMAIN ='TIPO_UFFICIO' ";

		return lStatement;
	}

	protected String getSqlQuery() {
		return getSqlQuery(true);
		/*
		 * String lStatement = new String("");
		 * 
		 * lStatement += " SELECT  " + " ID_MESSAGGIO,  " +
		 * " COD_TIPO_MESSAGGIO, tipoMess.DESCRIZIONE DescrTipoMessaggio, " +
		 * " COD_TIPO_OPERAZIONE,  tipOperazione.DESCRIZIONE DescrTipoOperazione," +
		 * " COD_UFFICIO_MITTENTE,  " + " COD_BDI_MITTENTE,  mit.DESCRIZIONE bdiMit," +
		 * " COD_UFFICIO_DESTINATARIO, comuffdest.DESCRIZIONE destSede,tipuffdest.RV_MEANING tipodest," +
		 * " comuff.DESCRIZIONE sede, tipuff.RV_MEANING tipouff," +
		 * " COD_BDI_DESTINATARIA,  dest.descrizione bdiDest," + " DATA_INVIO,  " + " DATA_ESITO,  " +
		 * " FLAG_VISTO,  " + " CODICE_UTENTE_MITTENTE,  " + " COD_ESITO, ESITO.DESCRIZIONE DESCR_ESITO ," +
		 * " JMS_ID_MESSAGE, JMS_CORRELATION_ID_MESSAGE, " + " BLOB_ESITO, " + " CHIAVE_ANNO_SIEP, " +
		 * " CHIAVE_PROGR_SIEP, " + " CHIAVE_ANNO_SIUS, " + " CHIAVE_PROGR_SIUS, " +
		 * " CHIAVE_ANNO_FAS_CUMULANTE, " + " CHIAVE_PROGR_FAS_CUMULANTE, " + " NOTE, " +
		 * " CHIAVE_ANNO_SIEPE, CHIAVE_PROGR_SIEPE, COGNOME_SOGGETTO, NOME_SOGGETTO, DATA_NASCITA, COD_STATO_NASCITA, COD_COMUNE_NASCITA "
		 * + " FROM MESSAGGIO," +
		 * " JMS_CODE mit,JMS_CODE dest, JMS_CODE ESITO,ufficio uff, comune comuff,cg_ref_codes tipuff," +
		 * " ufficio uffdest,comune comuffdest, cg_ref_codes tipuffdest, JMS_CODE tipoMess,JMS_CODE tipOperazione"
		 * + " where " + " COD_TIPO_MESSAGGIO = tipoMess.CODICE and tipoMess.dominio = 'TIPO_MESSAGGIO'  " +
		 * " and COD_TIPO_OPERAZIONE = tipOperazione.CODICE and tipOperazione.dominio = 'TIPO_OPERAZIONE'  " +
		 * " and COD_BDI_MITTENTE = mit.CODICE and mit.dominio = 'BDI'  " +
		 * " and COD_BDI_DESTINATARIA = dest.CODICE and dest.dominio = 'BDI'" +
		 * " and ESITO.CODICE = COD_ESITO AND ESITO.dominio = 'CODICE_ESITO'" +
		 * " and uff.COD_UFFICIO = COD_UFFICIO_MITTENTE" + " and comuff.COD_COMUNE = uff.COD_COMUNE" +
		 * " and uff.COD_TIPO_UFFICIO =tipuff.RV_LOW_VALUE" + " and tipuff.RV_DOMAIN ='TIPO_UFFICIO'" +
		 * " and uffdest.COD_UFFICIO = COD_UFFICIO_DESTINATARIO" +
		 * " and comuffdest.COD_COMUNE = uffdest.COD_COMUNE" +
		 * " and uffdest.COD_TIPO_UFFICIO =tipuffdest.RV_LOW_VALUE" +
		 * " and tipuffdest.RV_DOMAIN ='TIPO_UFFICIO'";
		 * 
		 * return lStatement;
		 */
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		MessaggioModel aModel = new MessaggioModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdMessaggio(getBigDecimal("ID_MESSAGGIO"));
		aModel.setCodTipoMessaggio(getString("COD_TIPO_MESSAGGIO"));

		aModel.setDescrTipoMessaggio(getString("DescrTipoMessaggio"));
		aModel.setDescrTipoOperazione(getString("DescrTipoOperazione"));
		aModel.setCodTipoOperazione(getString("COD_TIPO_OPERAZIONE"));
		aModel.setCodUfficioMittente(getString("COD_UFFICIO_MITTENTE"));
		aModel.setCodBdiMittente(getString("COD_BDI_MITTENTE"));
		aModel.setDescrBdiMittente(getString("BDIMIT"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setDescrUfficioMittente(getString("tipouff"));
		aModel.setDescrSedeUfficioMittente(getString("sede"));
		aModel.setCodBdiDestinataria(getString("COD_BDI_DESTINATARIA"));
		aModel.setDescrBdiDestinataria(getString("BDIDEST"));
		aModel.setDataInvio(getDate("DATA_INVIO"));
		aModel.setDataEsito(getDate("DATA_ESITO"));
		aModel.setCodiceUtenteMittente(getString("CODICE_UTENTE_MITTENTE"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setDescrUfficioDestinatario(getString("tipodest"));
		aModel.setCodBdiDestinataria(getString("COD_BDI_DESTINATARIA"));
		aModel.setDescrBdiDestinataria(getString("bdiDest"));
		aModel.setDescrSedeUfficioDestinatario(getString("destSede"));
		aModel.setDataInvio(getDate("DATA_INVIO"));
		aModel.setDataEsito(getDate("DATA_ESITO"));
		aModel.setCodiceUtenteMittente(getString("CODICE_UTENTE_MITTENTE"));
		aModel.setJmsIdMessaggio(getString("JMS_ID_MESSAGE"));
		aModel.setJmsCorrelationIdMessage(getString("JMS_CORRELATION_ID_MESSAGE"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setFlagVisto(getString("FLAG_VISTO"));
		aModel.setDescrEsito(getString("DESCR_ESITO"));

		if (findColumn("BLOB_ESITO"))
			aModel.setBlobOut(getBlob("BLOB_ESITO"));

		aModel.setChiaveAnnoSiep(getBigDecimal("CHIAVE_ANNO_SIEP"));
		aModel.setChiaveProgrSiep(getBigDecimal("CHIAVE_PROGR_SIEP"));
		aModel.setChiaveUfficioSiep(getString("CHIAVE_UFFICIO_SIEP")); // new d.f. NOV/2013 per gestire
																		// l'inoltro ad altro ufficio
		aModel.setDescrUfficioSiep(getString("descr_ufficio_siep")); // new d.f. NOV/2013 per gestire
																		// l'inoltro ad altro ufficio
		aModel.setDescrSedeUfficioSiep(getString("sede_ufficio_siep")); // new d.f. NOV/2013 per gestire
																		// l'inoltro ad altro ufficio

		aModel.setChiaveAnnoSius(getBigDecimal("CHIAVE_ANNO_SIUS"));
		aModel.setChiaveProgrSius(getBigDecimal("CHIAVE_PROGR_SIUS"));
		// UEPE
		aModel.setChiaveAnnoSiepe(getBigDecimal("CHIAVE_ANNO_SIEPE"));
		aModel.setChiaveProgrSiepe(getBigDecimal("CHIAVE_PROGR_SIEPE"));
		aModel.setCognomeSoggetto(getString("COGNOME_SOGGETTO"));
		aModel.setNomeSoggetto(getString("NOME_SOGGETTO"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		// SIEP - Trasmissione per Competenza
		aModel.setChiaveAnnoFasCumulante(getBigDecimal("CHIAVE_ANNO_FAS_CUMULANTE"));
		aModel.setChiaveProgrFasCumulante(getBigDecimal("CHIAVE_PROGR_FAS_CUMULANTE"));
    
    if( findColumn("CHIAVE_UFFICIO_FAS_CUMULANTE") ) {
      aModel.setChiaveUfficioFasCumulante (getString("CHIAVE_UFFICIO_FAS_CUMULANTE")); // new d.f. MEV26 Cumulo   
      aModel.setDescrUfficioFasCumulante  (getString("descr_ufficio_siep_cum")); // new d.f. MEV26 Cumulo       
      aModel.setDescrSedeUfficioFasCumulante (getString("sede_ufficio_siep_cum")); // new d.f. MEV26 Cumulo      
    }
    
		aModel.setNote(getString("NOTE"));
    
    // MEV26 Cumulo   
    if( findColumn("ID_RICHIESTA") )
      aModel.setIdRichiesta(getBigDecimal("ID_RICHIESTA"));
    //FINE MEV26
    
    //==========================================================================
    aModel.setDeliveryMode            (getString("DELIVERY_MODE")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    
    
    aModel.setCodUfficioInoltro       (getString("COD_UFFICIO_INOLTRO")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    
    aModel.setDescrUfficioInoltro     (getString("descrUffInoltro")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    
    aModel.setDescrSedeUfficioInoltro (getString("descrSedeUfficioInoltro")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    
    aModel.setCodBdiInoltro           (getString("COD_BDI_INOLTRO")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    

    aModel.setCodUfficioReplyTo       (getString("COD_UFFICIO_REPLY_TO")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    
    aModel.setDescrUfficioReplyTo     (getString("descrUffReplyTo")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    
    aModel.setDescrSedeUfficioReplyTo (getString("descrSedeUfficioReplyTo")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    
    aModel.setCodBdiReplyTo           (getString("COD_BDI_REPLY_TO")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    

    aModel.setJmsCorrelationReplyTo   (getString("JMS_CORRELATION_REPLY_TO")); // new d.f. NOV/2013 per gestire l'inoltro ad altro ufficio    
    aModel.setIdMessaggioSollecitato  (getString("ID_MESSAGGIO_SOLLECITATO")); // new d.f. NOV/2013 per gestire il sollecito   

    //==========================================================================

		if (findColumn("ID_FASC_MS_TO_FASC_SIEP")) {
			if (getBigDecimal("ID_FASC_MS_TO_FASC_SIEP") != null) {
				FascMsToFascSiepModel aModelFas = new FascMsToFascSiepModel();

				aModelFas.setCodTipoRelazioneMS(getString("COD_TIPO_RELAZIONE_MS"));

				aModelFas.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP_X"));
				aModelFas.setChiaveAnnoSiep(getBigDecimal("CHIAVE_ANNO_SIEP_X"));
				aModelFas.setChiaveProgrSiep(getBigDecimal("CHIAVE_PROGR_SIEP_X"));
				aModelFas.setChiaveUfficioSiep(getString("CHIAVE_UFFICIO_SIEP_X"));

				aModelFas.setFasSieIdFascicoloCollegato(getBigDecimal("FAS_SIE_ID_FASCICOLO_COLLEGATO"));
				aModelFas.setChiaveAnnoSiepCollegato(getBigDecimal("CHIAVE_ANNO_SIEP_COLLEGATO"));
				aModelFas.setChiaveProgrSiepCollegato(getBigDecimal("CHIAVE_PROGR_SIEP_COLLEGATO"));
				aModelFas.setChiaveUfficioSiepCollegato(getString("CHIAVE_UFFICIO_SIEP_COLLEGATO"));

				aModel.setFascMsToFascSiepModel(aModelFas);
			}
		}

		// Ticket#20220111018 - Aggiunta conteggio solleciti
		if (findColumn("contaSolleciti"))
			aModel.setContaSolleciti(getBigDecimal("contaSolleciti"));
		// Ticket#20220111018 - FINE
		
		// Settaggio del TreeModel
		ByteArrayOutputStream lStr = new ByteArrayOutputStream();
		lStr = aModel.getBlobOut();

		if (lStr != null) {
			if (lStr.size() > 0) {
				byte[] lBuffer = new byte[lStr.size()];
				lBuffer = lStr.toByteArray();
				ByteArrayInputStream lBufInput = new ByteArrayInputStream(lBuffer);

				try {
					ObjectInputStream ois = new ObjectInputStream(lBufInput);
					TreeModel lTree = (TreeModel) ois.readObject();
					ois.close();
					aModel.setTreeModel(lTree);
				// INIZIO INTERVENTO PER SEGNALAZIONE m_dg.DOG07.06-08-2018.0025591.U per versione sies 11.3 (introduco il catch per InvalidClassException)
				} catch (InvalidClassException exC) {
					aModel.setIsErroreParser(true);
					
					siesLogger.error(
							"Eccezione MessaggioSqlDAO.getModel: InvalidClassException ");
					aModel.setCodEsito(ICostantiJMS.ERRORE_DEPLOY);
					siesLogger.error("STAMPO ECCEZIONE: " + exC.getMessage(), exC);
				}
				// FINE
			    catch (Exception ex) {
					aModel.setIsErroreParser(true);
	
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.error(
							"Eccezione MessaggioSqlDAO.getModel: Errore nella lettura del BLOB!");
					// NUOVA INFRASTRUTTURA: aggiunta stampa eccezione
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.error("STAMPO ECCEZIONE: " + ex.getMessage(), ex);
			   }
			}

		}
		return aModel;
	}

	// public GenericModel getModelMS() throws DAOException
	// {
	// GenericModel gm = getModel();
	//
	// return gm;
	// }

	public String setCondizione(MessaggioModel aModel) {

		String lCondizioni = new String();
		// boolean lInserito = false;

		if (aModel.getCodTipoMessaggio() != null && aModel.getCodTipoMessaggio().trim().length() > 0)
			lCondizioni += " AND COD_TIPO_MESSAGGIO = '" + aModel.getCodTipoMessaggio() + "' ";
		if (aModel.getCodTipoOperazione() != null && aModel.getCodTipoOperazione().trim().length() > 0)
			lCondizioni += " AND COD_TIPO_OPERAZIONE = '" + aModel.getCodTipoOperazione() + "' ";
		if (aModel.getCodEsito() != null && aModel.getCodEsito().trim().length() > 0)
			lCondizioni += " AND COD_ESITO = '" + aModel.getCodEsito() + "' ";
		if (aModel.getCodUfficioDestinatario() != null
				&& aModel.getCodUfficioDestinatario().trim().length() > 0)
			lCondizioni += " AND COD_UFFICIO_DESTINATARIO = '" + aModel.getCodUfficioDestinatario() + "' ";
		if (aModel.getCodUfficioMittente() != null && aModel.getCodUfficioMittente().trim().length() > 0)
			lCondizioni += " AND COD_UFFICIO_MITTENTE = '" + aModel.getCodUfficioMittente() + "' ";
		if (aModel.getChiaveAnnoSiep() != null && aModel.getChiaveProgrSiep() != null) {
			lCondizioni += " AND CHIAVE_ANNO_SIEP =" + aModel.getChiaveAnnoSiep();
			lCondizioni += " AND CHIAVE_PROGR_SIEP =" + aModel.getChiaveProgrSiep();
		}
    if (aModel.getChiaveUfficioSiep() != null && !"".equals(aModel.getChiaveUfficioSiep()))
    {
      lCondizioni += " AND CHIAVE_UFFICIO_SIEP = '" + aModel.getChiaveUfficioSiep()+"'";
    }    
		if (aModel.getChiaveAnnoSius() != null && aModel.getChiaveProgrSius() != null) {
			lCondizioni += " AND CHIAVE_ANNO_SIUS =" + aModel.getChiaveAnnoSius();
			lCondizioni += " AND CHIAVE_PROGR_SIUS =" + aModel.getChiaveProgrSius();
		}
		if (aModel.getFlagVisto() != null && aModel.getFlagVisto().trim().length() > 0)
			lCondizioni += " AND FLAG_VISTO = '" + aModel.getFlagVisto() + "' ";
		if (aModel.getDataInvio() != null)
			lCondizioni += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') = '"
					+ DateUtils.getDateToString(aModel.getDataInvio(), "yyyyMMdd") + "'";
    
    if (aModel.getChiaveAnnoFasCumulante() != null && aModel.getChiaveProgrFasCumulante()!= null)
    {
      lCondizioni += " AND CHIAVE_ANNO_FAS_CUMULANTE =" + aModel.getChiaveAnnoFasCumulante();
      lCondizioni += " AND CHIAVE_PROGR_FAS_CUMULANTE =" + aModel.getChiaveProgrFasCumulante();
    }
    if (aModel.getChiaveUfficioFasCumulante() != null && !"".equals(aModel.getChiaveUfficioFasCumulante()))
    {
      lCondizioni += " AND CHIAVE_UFFICIO_FAS_CUMULANTE = '" + aModel.getChiaveUfficioFasCumulante()+"'";
    }
    
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_MESSAGGIO = " + aKey;
	}
  
  public String setCondizioneDataInvio(Date aDaIni, Date aDaFine)
  {
	  String lCondizioni = new String();

	  /* MEV_2025-10 – Requisito H */
	  // Le date possono essere anche null
	  //lCondizioni += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '" + DateUtils.getDateToString(aDaIni, "yyyyMMdd" )+"'" ;
	  //lCondizioni += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '" + DateUtils.getDateToString(aDaFine, "yyyyMMdd" )+"'" ;
	  
	  if (aDaIni!=null)
		  lCondizioni += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '" + DateUtils.getDateToString(aDaIni, "yyyyMMdd" )+"'" ;
	  
	  if (aDaFine!=null)
		  lCondizioni += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '" + DateUtils.getDateToString(aDaFine, "yyyyMMdd" )+"'" ;
	  /* FINE - MEV_2025-10 – Requisito H */
	  
	  return lCondizioni;
  }

	private String setOrder() {
		return " ORDER BY DATA_INVIO DESC";
	}

	public void cancellaEsitoNonSpedito(String aCorrelationID, String aBDIMittente) {
		String lSql = "";
		lSql += " DELETE FROM MESSAGGIO WHERE COD_TIPO_MESSAGGIO ='04' AND COD_BDI_MITTENTE = '"
				+ aBDIMittente + "' AND JMS_CORRELATION_ID_MESSAGE = '" + aCorrelationID + "'"
				+ " AND COD_ESITO = '00100'";
		setStatement(lSql);
	}

	/**
	 * 
	 * @param aPage
	 * @param lCond
	 * @return
	 */
	protected String getSqlQueryPerRicerca(int aPage, String lCond) {
		String lStatement = new String("");

		lStatement += "SELECT * FROM ( "
				+ " SELECT  "
				+ " ID_MESSAGGIO,  "
				+ " COD_TIPO_MESSAGGIO,DescrTipoMessaggio, "
				+ " COD_TIPO_OPERAZIONE,  DescrTipoOperazione,"
				+ " COD_UFFICIO_MITTENTE,  "
				+ " COD_BDI_MITTENTE,  bdiMit,"
				+ " COD_UFFICIO_DESTINATARIO, destSede,tipodest,"
				+ "  sede,  tipouff,"
				+ " COD_BDI_DESTINATARIA,   bdiDest,"
				+ " DATA_INVIO,   DATA_ESITO,  FLAG_VISTO,  "
				+ " CODICE_UTENTE_MITTENTE,  "
				+ " COD_ESITO, DESCR_ESITO ,"
				+ " JMS_ID_MESSAGE, JMS_CORRELATION_ID_MESSAGE, "
				+ " BLOB_ESITO, "
				+ " CHIAVE_ANNO_SIEP, "
				+ " CHIAVE_PROGR_SIEP, "
				+
				// =============================
				// ADD Misure Sicurezza
				" CHIAVE_UFFICIO_SIEP,  descr_ufficio_siep, sede_ufficio_siep, "
				+ " DELIVERY_MODE, "
				+ " COD_UFFICIO_INOLTRO,  descrUffInoltro,  descrSedeUfficioInoltro,"
				+ " COD_BDI_INOLTRO, "
				+ " COD_UFFICIO_REPLY_TO, descrUffReplyTo,  descrSedeUfficioReplyTo,"
				+ " COD_BDI_REPLY_TO, "
				+ " JMS_CORRELATION_REPLY_TO, "
				+ " ID_MESSAGGIO_SOLLECITATO, "
				+
				// ==============================
				" CHIAVE_ANNO_SIUS, "
				+ " CHIAVE_PROGR_SIUS, "
				+ " CHIAVE_ANNO_FAS_CUMULANTE, "
				+ " CHIAVE_PROGR_FAS_CUMULANTE, "
        + " CHIAVE_UFFICIO_FAS_CUMULANTE,  descr_ufficio_siep_cum, sede_ufficio_siep_cum, "        
				+ " NOTE, "
				+ " CHIAVE_ANNO_SIEPE, CHIAVE_PROGR_SIEPE, COGNOME_SOGGETTO, NOME_SOGGETTO, DATA_NASCITA, COD_STATO_NASCITA, COD_COMUNE_NASCITA "
				+ ", Rownum rn FROM ( " + getSqlQuery() + " "
				+ lCond
				+
				// ", Rownum rn FROM ( " + getSqlQuery(false) + " " + lCond +
				" ORDER BY DATA_INVIO DESC) inner ) WHERE rn BETWEEN "
				+ ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND " + (aPage)
				* IWebConstants.RESULT_PER_PAGE;

		return lStatement;
	}

	public void getCountMesaggiPerRicerca(String aUfficio, String aTipoOperazione) throws DAOException {
		String lSql = getSqlCountMessaggi();

		lSql += " where COD_TIPO_MESSAGGIO = '03' AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'";
		lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'  AND FLAG_VISTO='N' ";

		setStatement(lSql);
	}

	protected String getSqlCountMessaggi() throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords " + " FROM MESSAGGIO ";

		return lStatement;
	}

	public void getCountMessaggiPerRicercaFascAltreBDI(String aAnnoSiep, String aProgrSiep, String aUfficio,
			String aUfficioDestinatario, String aUtente, String aEsito, String aTipoOperazione,
			Date dataRicercaInizio, Date dataRicercaFine) throws DAOException {
		String lSql = getSqlCountMessaggi();

		lSql += " where COD_TIPO_MESSAGGIO = '03' ";
		if (aAnnoSiep != null && aAnnoSiep.length() > 1)
			lSql += " AND CHIAVE_ANNO_SIEP = '" + aAnnoSiep + "'";
		if (aProgrSiep != null && aProgrSiep.length() > 0)
			lSql += " AND CHIAVE_PROGR_SIEP = '" + aProgrSiep + "'";
		if (aUfficio != null)
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'";
		if (aUfficioDestinatario != null && aUfficioDestinatario.length() > 1)
			lSql += " AND COD_UFFICIO_DESTINATARIO = '" + aUfficioDestinatario + "'";
		if (aUtente != null)
			lSql += " AND CODICE_UTENTE_MITTENTE = '" + aUtente + "'";
		if (!(aTipoOperazione == null || aTipoOperazione.compareTo("-") == 0))
			lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";
		if (dataRicercaInizio != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(dataRicercaInizio, "yyyyMMdd") + "'";
		if (dataRicercaFine != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(dataRicercaFine, "yyyyMMdd") + "'";

		lSql += " order by COD_UFFICIO_MITTENTE, DATA_INVIO DESC ";

		setStatement(lSql);
	}

	// 08/06/2005 Conteggio BDI configurate.
	public void getCountDestinationBDI() throws DAOException {
		String lSql = "SELECT COUNT(*) HowManyRecords " + " FROM JMS_CODE WHERE DOMINIO = 'CONN_JMS_STRING'";
		setStatement(lSql);
	}

	public void getCountMessaggiPerRicercaSoggAltreBDI(String aUfficio, String aUtente,
			String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine) throws DAOException {
		String lSql = getSqlCountMessaggi();

		lSql += " where COD_TIPO_MESSAGGIO = '03' ";
		if (aUfficio != null)
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'";
		if (aUtente != null)
			lSql += " AND CODICE_UTENTE_MITTENTE = '" + aUtente + "'";
		if (!(aTipoOperazione == null || aTipoOperazione.compareTo("-") == 0))
			lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";
		if (dataRicercaInizio != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(dataRicercaInizio, "yyyyMMdd") + "'";
		if (dataRicercaFine != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(dataRicercaFine, "yyyyMMdd") + "'";

		lSql += " order by COD_UFFICIO_MITTENTE, DATA_INVIO DESC ";

		setStatement(lSql);
	}

	public void ricercaMessaggioRichiestaRicercaSoggettoPerUfficioPaged(String aUfficio, String aUtente,
			String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine, int aPage)
			throws DAOException {
		String lSql = getSqlQuery();
		String lPaginedStatement = new String("");

		lSql += " AND COD_TIPO_MESSAGGIO = '03' ";
		if (aUfficio != null)
			lSql += " AND COD_UFFICIO_MITTENTE = '" + aUfficio + "'";
		if (!(aTipoOperazione == null || aTipoOperazione.compareTo("-") == 0))
			lSql += " AND COD_TIPO_OPERAZIONE = '" + aTipoOperazione + "'";
		if (aUtente != null)
			lSql += " AND CODICE_UTENTE_MITTENTE = '" + aUtente + "'";
		if (dataRicercaInizio != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(dataRicercaInizio, "yyyyMMdd") + "'";
		if (dataRicercaFine != null)
			lSql += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(dataRicercaFine, "yyyyMMdd") + "'";

		lSql += setOrder();
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE_ESITO + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE_ESITO;

		setStatement(lPaginedStatement);
	}

	/**
	 * RIcerca un messaggio uguale a quello che si cerca d'inserire.
	 * 
	 * @param aMessage
	 */
	public void ricercaMessaggioUgualeNonSpedito(MessaggioModel aMessage) {
		String lStatement = "";

		lStatement += " SELECT  " + " ID_MESSAGGIO  " + " FROM MESSAGGIO " + " where COD_TIPO_MESSAGGIO = '"
				+ aMessage.getCodTipoMessaggio() + "' " + " and  " + " COD_TIPO_OPERAZIONE= '"
				+ aMessage.getCodTipoOperazione() + "'" + " and COD_UFFICIO_MITTENTE= '"
				+ aMessage.getCodUfficioMittente() + "' and   " + " COD_BDI_MITTENTE= '"
				+ aMessage.getCodBdiMittente() + "' and  " + " COD_UFFICIO_DESTINATARIO= '"
				+ aMessage.getCodUfficioDestinatario() + "' and " + " COD_BDI_DESTINATARIA= '"
				+ aMessage.getCodBdiDestinataria() + "'";
		if (aMessage.getDataInvio() != null)
			lStatement += " AND TO_CHAR(DATA_INVIO,'YYYYMMDD') = '"
					+ DateUtils.getDateToString(aMessage.getDataInvio(), "yyyyMMdd") + "'";

		if (aMessage.getDataEsito() != null)
			lStatement += " AND TO_CHAR(DATA_ESITO,'YYYYMMDD') = '"
					+ DateUtils.getDateToString(aMessage.getDataEsito(), "yyyyMMdd") + "'";

		if (aMessage.getJmsIdMessaggio() != null && (aMessage.getJmsIdMessaggio() != ""))
			lStatement += " and JMS_ID_MESSAGE= '" + aMessage.getJmsIdMessaggio() + "'";

		if (aMessage.getJmsCorrelationIdMessage() != null)
			lStatement += " and  JMS_CORRELATION_ID_MESSAGE= '" + aMessage.getJmsCorrelationIdMessage() + "'";

		lStatement += " and FLAG_VISTO= '" + aMessage.getFlagVisto() + "' ";
		lStatement += " and COD_ESITO= '" + aMessage.getCodEsito() + "' ";

		setStatement(lStatement);
	}

	public void ricercaXCruscotto(String aCodUfficio, String aOrd) throws DAOException {
		// Calcolo data "90 giorni prima"
		Date lDataConfronto = DateUtils.getSysDate();
		for (int x = 0; x < 90; x++)
			lDataConfronto = DateUtils.getDayBefore(lDataConfronto);

		String lSql = "SELECT TRUNC(DATA_INVIO) DATA_INVIO, FLAG_VISTO, COUNT(*) NUM from MESSAGGIO where MESSAGGIO.COD_UFFICIO_DESTINATARIO = '";
		lSql += aCodUfficio;
		lSql += "' AND COD_TIPO_MESSAGGIO = '01' ";
		lSql += " AND TRUNC(DATA_INVIO) > TO_DATE ('" + DateUtils.getDateToString(lDataConfronto, "ddMMyyyy")
				+ "', 'DDMMYYYY')";
		lSql += " GROUP BY TRUNC(DATA_INVIO), FLAG_VISTO";

		if (aOrd.equalsIgnoreCase("ASC"))
			lSql += " ORDER BY DATA_INVIO ASC";
		else
			lSql += " ORDER BY DATA_INVIO DESC";

		setStatement(lSql);
	}

	public CruscottoModel getCruscottoModel() throws DAOException {
		// Creazione del CruscottoModel
		CruscottoModel lCruscMod = new CruscottoModel();

		// Lettura dei dati
		lCruscMod.setDataRicezione(getDate("DATA_INVIO"));
		lCruscMod.setMessage(getString("FLAG_VISTO"));
		lCruscMod.setINumAttiRicevuti(getBigDecimal("NUM"));

		return lCruscMod;
	}

	/**
	 * Imposta la query per la ricerca dei messaggi ricevuti (02 - Esito) da un certo ufficio in relazione al
	 * messaggio inviato
	 * 
	 * @param aUfficio
	 *            - Ufficio che ha inviato la richiesta e destinatario delle risposte
	 * @param aIdMessaggio
	 *            - Id del messaggio di Richiesta (inviato)
	 * @throws DAOException
	 */
	public void ricercaEsitiRicevutiByIdMessaggi(String aIdMessaggio /* , String aUfficio */)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND JMS_CORRELATION_ID_MESSAGE = '" + aIdMessaggio + "'"
				+ " AND COD_TIPO_MESSAGGIO = '02' " +
				// " AND COD_UFFICIO_DESTINATARIO = '" + aUfficio + "'" +
				// " AND DELIVERY_MODE = '00002'"
				" ORDER BY ID_MESSAGGIO ";

		setStatement(lSql);
	}

   public void ricercaMessaggioNelPeriodoPaged(MessaggioModel aModel, Date aDataInizio, Date aDataFine, int aPage) throws DAOException
   {

	   String lSql = new String("");
	   String lPaginedStatement=new String("");

	   lSql = getPagedSqlQuery(aPage);
	   lSql += " " + setCondizione(aModel);
	   lSql += " " + setCondizioneDataInvio(aDataInizio, aDataFine);
	   lSql += setOrder();
	   
	   // Se apage = 0 , la Query serve per il totale
	   if(aPage==0)
	   {
		   setStatement(lSql);
	   }
	   else
	   {
		   lPaginedStatement="SELECT * FROM (SELECT INNER.* , Rownum rn FROM ("+lSql+"  ) INNER ) WHERE rn between  "+((aPage-1)*IWebConstants.RESULT_PER_PAGE+1)+ " AND "+ (aPage)*IWebConstants.RESULT_PER_PAGE;
		   setStatement(lPaginedStatement);
	   }
	   
   }
   
   protected String getPagedSqlQuery(int aPage) 
   {
	    String lStatement = new String("");

	    if (aPage > 0) 
	    {
	      lStatement += " SELECT  " +
	      " ID_MESSAGGIO,  " +
	      " COD_TIPO_MESSAGGIO, tipoMess.DESCRIZIONE DescrTipoMessaggio, " +
	      " COD_TIPO_OPERAZIONE,  tipOperazione.DESCRIZIONE DescrTipoOperazione," +
	      " COD_UFFICIO_MITTENTE,  " +
	      " COD_BDI_MITTENTE,  mit.DESCRIZIONE bdiMit," +
	      " COD_UFFICIO_DESTINATARIO, comuffdest.DESCRIZIONE destSede,tipuffdest.RV_MEANING tipodest," +
	      " comuff.DESCRIZIONE sede, tipuff.RV_MEANING tipouff," +
	      " COD_BDI_DESTINATARIA,  dest.descrizione bdiDest," +
	      " DATA_INVIO,  " +
	      " DATA_ESITO,  " +
	      " FLAG_VISTO,  " +
	      " CODICE_UTENTE_MITTENTE,  " +
	      " COD_ESITO, ESITO.DESCRIZIONE DESCR_ESITO ," +
	      " JMS_ID_MESSAGE, JMS_CORRELATION_ID_MESSAGE, " +    
	      " BLOB_ESITO, "  +
	      " CHIAVE_ANNO_SIEP, " +
	      " CHIAVE_PROGR_SIEP, " +
	      //=======================
	      // new d.f. since NOV/2013 per gestire l'inoltro ad altro ufficio
	      " CHIAVE_UFFICIO_SIEP, " + 
	      " tipo_ufficio_fasc_siep.RV_MEANING descr_ufficio_siep, " + 
	      " comune_fasc_siep.DESCRIZIONE sede_ufficio_siep, " +    
	      " DELIVERY_MODE, " +
	      " COD_UFFICIO_INOLTRO, tipUffInoltro.RV_MEANING descrUffInoltro, comUffInoltro.DESCRIZIONE descrSedeUfficioInoltro," +
	      " COD_BDI_INOLTRO, " +
	      " COD_UFFICIO_REPLY_TO, tipuffReplyTo.RV_MEANING descrUffReplyTo, comuffReplyTo.DESCRIZIONE descrSedeUfficioReplyTo," +
	      " COD_BDI_REPLY_TO, " +
	      " JMS_CORRELATION_REPLY_TO, " +
	      " ID_MESSAGGIO_SOLLECITATO, " +      
	      //
	      " ID_RICHIESTA, " +
	      //======================
	      " CHIAVE_ANNO_SIUS, " +
	      " CHIAVE_PROGR_SIUS, " +
	      " CHIAVE_ANNO_FAS_CUMULANTE, " +
	      " CHIAVE_PROGR_FAS_CUMULANTE, " +
	      " CHIAVE_UFFICIO_FAS_CUMULANTE, " +    
	      " tipo_ufficio_fasc_siep_cum.RV_MEANING descr_ufficio_siep_cum, " + 
	      " comune_fasc_siep_cum.DESCRIZIONE sede_ufficio_siep_cum, " +   
	      " NOTE, " +
	      " CHIAVE_ANNO_SIEPE, CHIAVE_PROGR_SIEPE, COGNOME_SOGGETTO, NOME_SOGGETTO, DATA_NASCITA, COD_STATO_NASCITA, COD_COMUNE_NASCITA "; 
	    }
	    else
	    {
	    	lStatement += " Select count(*) HowManyRecords ";         
	    }
	    
	    
	    lStatement += " FROM MESSAGGIO," +
	      " JMS_CODE mit,JMS_CODE dest, JMS_CODE ESITO,ufficio uff, comune comuff,cg_ref_codes tipuff, " +
	      " ufficio uffdest,comune comuffdest, cg_ref_codes tipuffdest, JMS_CODE tipoMess,JMS_CODE tipOperazione " +
	      // Aggiunte join per recuperare i dati descrittivi dell'ufficio siep: CHIAVE_UFFICIO_SIEP
	      " , UFFICIO ufficio_fasc_siep, COMUNE comune_fasc_siep ,CG_REF_CODES tipo_ufficio_fasc_siep " +
	      " , UFFICIO ufficio_fasc_siep_cum, COMUNE comune_fasc_siep_cum ,CG_REF_CODES tipo_ufficio_fasc_siep_cum " +      
	      " , UFFICIO uffInoltro, COMUNE comUffInoltro, CG_REF_CODES tipUffInoltro " +
	      " , UFFICIO uffReplyTo, COMUNE comuffReplyTo, CG_REF_CODES tipuffReplyTo " +
	    " WHERE " +
	      " COD_TIPO_MESSAGGIO = tipoMess.CODICE and tipoMess.dominio = 'TIPO_MESSAGGIO'  " +
	      " and COD_TIPO_OPERAZIONE = tipOperazione.CODICE and tipOperazione.dominio = 'TIPO_OPERAZIONE'  " +
	      " and COD_BDI_MITTENTE = mit.CODICE and mit.dominio = 'BDI'  " +
	      " and COD_BDI_DESTINATARIA = dest.CODICE and dest.dominio = 'BDI'" +
	      " and ESITO.CODICE = COD_ESITO AND ESITO.dominio = 'CODICE_ESITO'" +
	      " and uff.COD_UFFICIO = COD_UFFICIO_MITTENTE" +
	      " and comuff.COD_COMUNE = uff.COD_COMUNE" +
	      " and uff.COD_TIPO_UFFICIO =tipuff.RV_LOW_VALUE" +
	      " and tipuff.RV_DOMAIN ='TIPO_UFFICIO'" +
	      " and uffdest.COD_UFFICIO = COD_UFFICIO_DESTINATARIO" +
	      " and comuffdest.COD_COMUNE = uffdest.COD_COMUNE" +
	      " and uffdest.COD_TIPO_UFFICIO =tipuffdest.RV_LOW_VALUE" +
	      " and tipuffdest.RV_DOMAIN ='TIPO_UFFICIO'" +
	      // Aggiunte join per recuperare i dati descrittivo dell'ufficio siep: CHIAVE_UFFICIO_SIEP
	      " AND ufficio_fasc_siep.COD_UFFICIO = nvl(MESSAGGIO.CHIAVE_UFFICIO_SIEP, '-' ) " + //n.b. CHIAVE_UFFICIO_SIEP potrebbe essere null
	      " AND ufficio_fasc_siep.COD_COMUNE = comune_fasc_siep.COD_COMUNE " +
	      " AND ufficio_fasc_siep.COD_TIPO_UFFICIO = tipo_ufficio_fasc_siep.RV_LOW_VALUE " +
	      " AND tipo_ufficio_fasc_siep.RV_DOMAIN ='TIPO_UFFICIO' "  +
	      //
	      " AND ufficio_fasc_siep_cum.COD_UFFICIO = nvl(MESSAGGIO.CHIAVE_UFFICIO_FAS_CUMULANTE, '-' ) " + //n.b. CHIAVE_UFFICIO_FAS_CUMULANTE potrebbe essere null
	      " AND ufficio_fasc_siep_cum.COD_COMUNE = comune_fasc_siep_cum.COD_COMUNE " +
	      " AND ufficio_fasc_siep_cum.COD_TIPO_UFFICIO = tipo_ufficio_fasc_siep_cum.RV_LOW_VALUE " +
	      " AND tipo_ufficio_fasc_siep_cum.RV_DOMAIN ='TIPO_UFFICIO' "  +
	      //============ UFFICIO/BDI INOLTRO =========================
	      " AND uffInoltro.COD_UFFICIO = nvl(MESSAGGIO.COD_UFFICIO_INOLTRO, '-' ) " + 
	      " AND uffInoltro.COD_COMUNE = comUffInoltro.COD_COMUNE " + 
	      " AND uffInoltro.COD_TIPO_UFFICIO = tipUffInoltro.RV_LOW_VALUE " +
	      " AND tipUffInoltro.RV_DOMAIN ='TIPO_UFFICIO' " +
	      //============ UFFICIO/BDI REPLY TO =========================
	      " AND uffReplyTo.COD_UFFICIO = nvl(MESSAGGIO.COD_UFFICIO_REPLY_TO, '-' ) " + 
	      " AND uffReplyTo.COD_COMUNE = comuffReplyTo.COD_COMUNE " + 
	      " AND uffReplyTo.COD_TIPO_UFFICIO = tipuffReplyTo.RV_LOW_VALUE " +
	      " AND tipuffReplyTo.RV_DOMAIN ='TIPO_UFFICIO' " ; 

	    return lStatement;

	  }
}