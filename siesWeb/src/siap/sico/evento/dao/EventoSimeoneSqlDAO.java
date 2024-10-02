package siap.sico.evento.dao;

/**
 * <p>Title: EventoSimeoneSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoFascicoloModel;
import siap.sico.evento.model.EventoFascicoloStatoModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class EventoSimeoneSqlDAO extends SIAPSqlDAO {
	public EventoSimeoneSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaEventoByFascicoloSiepDesc(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	// ricerca usata nel ActionSiap nel metodo isEventoValidato---Dario
	public void ricercaEventoByFascicoloSiepDescUfficioConnesso(BigDecimal aIdFascicolo, String aCodUfficio)
			throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND COD_UFFICIO_INSERIMENTO = " + aCodUfficio;
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaProvvedimentiNonValidatiPaged(EventoModel aModel, int aPage) throws DAOException {
		String lPaginedStatement = new String("");

		String lStatement = "SELECT * FROM V_PROVVEDIMENTI WHERE ";

		lStatement += " (FLAG_DOCUMENTO_REGISTRATO ='N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND CHIAVE_UFFICIO ='" + aModel.getCodUfficioInserimento() + "'";
		lStatement += "  AND COD_TIPO_EVENTO IN ('01') AND COD_TIPO_PROVVEDIMENTO NOT IN ('02','03')";
		lStatement += " " + setOrderIdFascicoloIdEvento();

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void ricercaProvvedimentiOmesseNotifiche(EventoModel aModel, String aCodUfficioUtenteConnesso)
			throws DAOException {
		/*
		 * String lPaginedStatement=new String("");
		 * 
		 * String lStatement = "SELECT * FROM V_PROVVEDIMENTI WHERE ";
		 * 
		 * lStatement += " CHIAVE_UFFICIO ='"+aModel.getCodUfficioInserimento()+"'"; lStatement +=
		 * " AND COD_MOTIVO IN ('0061','0062','0063') AND COD_TIPO_PROVVEDIMENTO NOT IN ('02','03')";
		 * lStatement += " AND (SELECT EVE_ID_EVENTO FROM NOTIFICA WHERE DATA_AVVENUTA_NOTIFICA IS NULL";
		 * lStatement +=
		 * " AND EVE_ID_EVENTO = V_PROVVEDIMENTI.ID_EVENTO AND (COD_TIPO_NOTIFICA ='E' OR COD_TIPO_NOTIFICA ='N')"
		 * ; lStatement += " AND rownum=1) = V_PROVVEDIMENTI.ID_EVENTO ";
		 * 
		 * lStatement += " " + setOrderIdFascicoloIdEvento();
		 * 
		 * lPaginedStatement="SELECT * FROM (SELECT INNER.* , Rownum rn FROM ("+lStatement+
		 * "  ) INNER ) WHERE rn between  "+((aPage-1)*IWebConstants.RESULT_PER_PAGE+1)+ " AND "+
		 * (aPage)*IWebConstants.RESULT_PER_PAGE;
		 * 
		 * setStatement(lPaginedStatement);
		 */
		String lStatement = " SELECT";
		lStatement += " DISTINCT(FASCICOLO_SIEP.ID_FASCICOLO_SIEP),";
		lStatement += " FASCICOLO_SIEP.CHIAVE_ANNO,";
		lStatement += " FASCICOLO_SIEP.CHIAVE_PROGR,";
		lStatement += " SOGGETTO.NOME,";
		lStatement += " SOGGETTO.COGNOME";
		lStatement += " FROM EVENTO, NOTIFICA,";
		lStatement += " FASCICOLO_SIEP, SOGGETTO";
		lStatement += " WHERE EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP";
		lStatement += " AND NOTIFICA.EVE_ID_EVENTO = EVENTO.ID_EVENTO";
		lStatement += " AND FASCICOLO_SIEP.SOG_ID_SOGGETTO = SOGGETTO.ID_SOGGETTO";
		lStatement += " AND FASCICOLO_SIEP.CHIAVE_UFFICIO = '" + aCodUfficioUtenteConnesso + "'";
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '06'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('0061', '0062', '0063', '0104', '0105','0117')";
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL OR EVENTO.FLAG_DOCUMENTO_REGISTRATO != 'A')";
		lStatement += " AND ( (NOTIFICA.COD_TIPO_NOTIFICA = 'E') OR (NOTIFICA.COD_TIPO_NOTIFICA = 'N' AND NOTIFICA.AVV_ID_AVVOCATO_FASCICOLO_SIEP IS NOT NULL) )";
		lStatement += " AND NOTIFICA.DATA_AVVENUTA_NOTIFICA IS NULL ";

		lStatement += " ORDER BY FASCICOLO_SIEP.CHIAVE_ANNO, FASCICOLO_SIEP.CHIAVE_PROGR";

		setStatement(lStatement);
	}

	public void getCountProvvedimentiOmesseNotifiche(EventoModel aModel, String aCodUfficioUtenteConnesso)
			throws DAOException {
		String lStatement = " SELECT count(DISTINCT(FASCICOLO_SIEP.ID_FASCICOLO_SIEP)) HowManyRecords";
		lStatement += " FROM EVENTO, NOTIFICA, FASCICOLO_SIEP";
		lStatement += " WHERE FASCICOLO_SIEP.chiave_ufficio = '" + aCodUfficioUtenteConnesso + "'";
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '06'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('0061', '0062', '0063', '0104', '0105', '0117')";
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL OR EVENTO.FLAG_DOCUMENTO_REGISTRATO != 'A')";
		lStatement += " AND ( (NOTIFICA.COD_TIPO_NOTIFICA = 'E') OR (NOTIFICA.COD_TIPO_NOTIFICA = 'N' AND NOTIFICA.AVV_ID_AVVOCATO_FASCICOLO_SIEP IS NOT NULL) )";
		lStatement += " AND NOTIFICA.DATA_AVVENUTA_NOTIFICA IS NULL ";
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP";
		lStatement += " AND EVENTO.ID_EVENTO = NOTIFICA.EVE_ID_EVENTO";

		setStatement(lStatement);
	}

	public void ricercaEventoSimeoneByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = " SELECT";
		lStatement += " DISTINCT(EVENTO.ID_EVENTO),";
		lStatement += " EVENTO.DATA_EMISSIONE,";
		lStatement += " EVENTO.COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";
		lStatement += " EVENTO.COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " EVENTO.COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFF_EMITTENTE,";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI,";
		// modifica 17-01-2008 dario
		lStatement += " DATA_ESPULSIONE_SANZ_SOST ";

		lStatement += " FROM EVENTO, NOTIFICA,";
		lStatement += " FASCICOLO_SIEP FAS, CG_REF_CODES CODEVE, CG_REF_CODES CODTIPPRO, CG_REF_CODES CODMOV, CG_REF_CODES UFF_TIPO_EMI, COMUNE LUOEMI, UFFICIO UFF_EMI";

		// NUOVA INFRASTRUTTURA: uso l'alias (e non il nome della tabella) per recuperare il campo
		// "ID_FASCICOLO_SIEP" (2 occorrenze)
		lStatement += " WHERE FAS.ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '06'";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('0061', '0062', '0063', '0104', '0105','0117')";
		lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL OR EVENTO.FLAG_DOCUMENTO_REGISTRATO != 'A')";
		lStatement += " AND ( (NOTIFICA.COD_TIPO_NOTIFICA = 'E') OR (NOTIFICA.COD_TIPO_NOTIFICA = 'N' AND NOTIFICA.AVV_ID_AVVOCATO_FASCICOLO_SIEP IS NOT NULL) )";
		lStatement += " AND NOTIFICA.DATA_AVVENUTA_NOTIFICA IS NULL ";
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND NOTIFICA.EVE_ID_EVENTO = EVENTO.ID_EVENTO";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO'";
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'";
		lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE";

		setStatement(lStatement);
	}

	/**
	 * Ricerca eventi
	 * 
	 * @param aKey
	 * @param aTipoEvento
	 * @throws DAOException
	 */
	public void ricercaEventoByFascicoloTipEveTipProvAsc(BigDecimal aKey, String[] aTipoEvento,
			String[] aTipoProv) throws DAOException {
		String lStatement = getSqlQueryOggettoProvv();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND FAS_SIU_ID_FASCICOLO_SIUS is  null ";

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += condizioneTipoEvento(aTipoEvento);
			}
		}

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoIn(aTipoProv);
			}
		}
		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";

		// STUB 24/02/2005 lStatement += setOrderEventoAsc();
		lStatement += setOrderCodUfficioEventoAsc();

		setStatement(lStatement);
	}

	public void ricercaEventoByFascicoloTipProvDesc(BigDecimal aKey, String[] aTipoProv) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO != 'A'";

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoIn(aTipoProv);
			}
		}
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaOrdineEsecuzioneSoloEventiVisualizzazioneByIdFascicoloPaged(BigDecimal aIdFascicolo,
			int aPage) throws DAOException {
		String lStatement = getSqlQuery();
		String lPaginedStatement = new String("");

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
	    lStatement += " AND COD_MOTIVO NOT IN ('0670') "; // MEV70 Esclusione Eventi con CodMotivo="0670"
		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";

		lStatement += setOrderEventoAsc();
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

   /**
    * 
    * @param aIdFascicolo
    * @param aPage
    * @param resultPerPage
    * @throws DAOException
    */
   public void ricercaEventiPerStatoEsecuzioneCumuloByIdFascicoloPaged (BigDecimal aIdFascicolo, int aPage, int resultPerPage) throws DAOException
   {
     String lStatement = getSqlQuery();
     String lPaginedStatement=new String("");

     lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
     lStatement += " AND COD_MOTIVO NOT IN ('0670') "; // MEV70 Esclusione Eventi con CodMotivo="0670"
     lStatement += " AND FLAG_VIDEO_SIEP = 'S'";
     lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";

     lStatement += setOrderEventoAsc();
     
     if (aPage>0) {
       lPaginedStatement="SELECT * FROM (SELECT INNER.* , Rownum rn FROM ("+lStatement+"  ) INNER ) WHERE rn between  "+((aPage-1)*resultPerPage+1)+ " AND "+ (aPage)*resultPerPage;
       setStatement (lPaginedStatement);
     }
     else {
       setStatement (lStatement);       
     }
   } 
  
  
  
	public void getCountSoloEventiVisualizzazione(BigDecimal aFascicoloId) throws DAOException

	{
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM EVENTO";
		lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP =" + aFascicoloId;
	    lStatement += " AND COD_MOTIVO NOT IN ('0670') "; // MEV70 Esclusione Eventi con CodMotivo="0670"
		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";
		lStatement += " " + setOrderEventoAsc();

		setStatement(lStatement);
	}

	public void getCountEventiNonValidati(EventoModel aModel) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM V_PROVVEDIMENTI WHERE ";
		lStatement += " (FLAG_DOCUMENTO_REGISTRATO ='N' OR FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		lStatement += " AND CHIAVE_UFFICIO ='" + aModel.getCodUfficioInserimento() + "'";
		lStatement += " AND CHIAVE_UFFICIO ='" + aModel.getCodUfficioInserimento() + "'";
		lStatement += "  AND COD_TIPO_EVENTO IN ('01') AND COD_TIPO_PROVVEDIMENTO NOT IN ('02','03')";
		lStatement += " " + setOrderIdFascicoloIdEvento();
		setStatement(lStatement);
	}

	public void ricercaEventoByFascicoloTipEveTipProvDesc(BigDecimal aKey, String[] aTipoEvento,
			String[] aTipoProv) throws DAOException {
		String lStatement = getSqlQueryOggettoProvv();
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		if (aTipoEvento != null) {
			if (aTipoEvento.length > 0) {
				lStatement += condizioneTipoEvento(aTipoEvento);
			}
		}

		if (aTipoProv != null) {
			if (aTipoProv.length > 0) {
				lStatement += condizioneTipoProvvedimentoIn(aTipoProv);
			}
		}
		lStatement += " AND FLAG_VIDEO_SIEP = 'S'";
		lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lStatement += setOrderEventoAsc();

		setStatement(lStatement);
	}

	public void ricercaEventoByFascicoloTipEveTipProvCodMotivo(BigDecimal aKey, String aTipoEvento,
			String aTipoProv, String aMotivo) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND COD_TIPO_EVENTO = '" + aTipoEvento + "'";
		lStatement += " AND COD_TIPO_PROVVEDIMENTO = '" + aTipoProv + "'";
		lStatement += " AND COD_MOTIVO = '" + aMotivo + "'";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	public void ricercaEventoByEveIdEventoTipEveTipProvCodMotivo(BigDecimal aKey, String aTipoEvento,
			String aTipoProv, String aMotivo, String flagDocRegistrato) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " AND COD_TIPO_EVENTO =  '" + aTipoEvento + "'";
		lStatement += " AND COD_TIPO_PROVVEDIMENTO =  '" + aTipoProv + "'";
		lStatement += " AND COD_MOTIVO = '" + aMotivo + "'";
		lStatement += " AND EVE_ID_EVENTO = " + aKey;
		
		// MEV_2019-09
		if ("S".equals(flagDocRegistrato) )
			lStatement += " AND FLAG_DOCUMENTO_REGISTRATO = 'S' ";		
		
		lStatement += setOrderEventoDesc();

		setStatement(lStatement);
	}

	protected String getSqlQuery() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO, ";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";
		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFF_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		lStatement += " DOC_BLOB, ";
		lStatement += " COD_OPERATORE_INSERIMENTO, ";
		lStatement += " DATA_INSERIMENTO,";
		lStatement += " COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " DATA_AGGIORNAMENTO,";
		lStatement += " COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  ";
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP, ";
		lStatement += " EVE_ID_EVENTO, ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST ";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

		return lStatement;
	}

	protected String getSqlQueryOggettoProvv() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO,";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";
		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE,";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFF_EMITTENTE,";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE,";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE,";
		lStatement += " DATA_EMISSIONE,";
		lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO,";
		lStatement += " DATA_TRASMISSIONE_ATTI,";
		lStatement += " DATA_RICEZIONE_ATTI,";
		lStatement += " COD_UFFICIO_DESTINATARIO,";
		lStatement += " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO,";
		lStatement += " PROGR_PROTOCOLLO,";
		lStatement += " DOC_BLOB,";
		lStatement += " COD_OPERATORE_INSERIMENTO,";
		lStatement += " DATA_INSERIMENTO,";
		lStatement += " COD_UFFICIO_INSERIMENTO,";
		lStatement += " COD_OPERATORE_AGGIORNAMENTO,";
		lStatement += " DATA_AGGIORNAMENTO,";
		lStatement += " COD_UFFICIO_AGGIORNAMENTO,";
		lStatement += " FAS_SIE_ID_FASCICOLO_SIEP,";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS,";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO,";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO,";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO,";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,";
		lStatement += " FLAG_STAMPA_SIEP,";
		lStatement += " FLAG_STAMPA_SIUS,";
		lStatement += " FLAG_VIDEO_SIEP,";
		lStatement += " FLAG_VIDEO_SIUS,";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP,";
		lStatement += " EVE_ID_EVENTO,";
		lStatement += " EVE_ID_EVENTO_REVOCA,";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE,";
		lStatement += " PEN_ID_PENA_RESIDUA,";
		lStatement += " DATA_ESPULSIONE_SANZ_SOST,";
		lStatement += " CODOGGETTO.RV_MEANING  DESCR_PROV";
		lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES,CG_REF_CODES CODOGGETTO";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";
		lStatement += " AND CODOGGETTO.RV_LOW_VALUE = CODMOV.RV_HIGH_VALUE AND CODOGGETTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";

		return lStatement;
	}

	protected String getSqlQueryEventoSimeoneFascicolo() throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ID_EVENTO,";
		lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
		lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
		// GDV
		lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";

		lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
		lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
		lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
		// MEV10-s3: aggiunto campo in estrazione
		lStatement += " UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFF_EMITTENTE,";
		lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
		lStatement += " DATA_EMISSIONE, ";
		lStatement += " EVENTO.COD_ESITO, CODESI.RV_MEANING COD_ESI,";
		lStatement += " FLAG_PIU_MENO, ";
		lStatement += " DATA_TRASMISSIONE_ATTI, ";
		lStatement += " DATA_RICEZIONE_ATTI, ";
		lStatement += " COD_UFFICIO_DESTINATARIO, ";
		lStatement += " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
		lStatement += " ANNO_PROTOCOLLO, ";
		lStatement += " PROGR_PROTOCOLLO, ";
		// lStatement += " DOC_BLOB, ";
		lStatement += " EVENTO.COD_OPERATORE_INSERIMENTO, ";
		lStatement += " EVENTO.DATA_INSERIMENTO,";
		lStatement += " EVENTO.COD_UFFICIO_INSERIMENTO,";
		lStatement += " EVENTO.COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " EVENTO.DATA_AGGIORNAMENTO,";
		lStatement += " EVENTO.COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " EVENTO.FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += " EVENTO.FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
		lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
		lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
		lStatement += " TEM_ID_TEMPLATE,  "; // Add By Paolo
		lStatement += " FLAG_STAMPA_SIEP,  ";
		lStatement += " FLAG_STAMPA_SIUS,  ";
		lStatement += " FLAG_VIDEO_SIEP,  ";
		lStatement += " FLAG_VIDEO_SIUS,  ";
		lStatement += " DEC_ID_DECRETO_ORDINANZA_SIEP, ";
		lStatement += " PEN_ACC_ID_PENA_ACCESSORIA, "; // STUB 09/02/2006
		lStatement += " EVENTO.EVE_ID_EVENTO,  ";
		lStatement += " EVE_ID_EVENTO_REVOCA, ";
		lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
		lStatement += " PEN_ID_PENA_RESIDUA, ";
		lStatement += " fasc.CHIAVE_ANNO, ";
		lStatement += " fasc.CHIAVE_PROGR, ";
		// modifica 17-01-2008 dario
		lStatement += " DATA_ESPULSIONE_SANZ_SOST ";

		lStatement += " FROM EVENTO, FASCICOLO_SIEP fasc, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
		lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES,";
		lStatement += " NOTIFICA";
		lStatement += " WHERE";
		lStatement += " EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = fasc.ID_FASCICOLO_SIEP AND";
		lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
		lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		// STUB 07/10/2005 lStatement +=
		// " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND";
		// //****
		lStatement += " ((nvl(EVENTO.COD_MOTIVO, '-') = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') OR";
		lStatement += " (EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO')) AND";
		lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
		lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
		lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";
		lStatement += " AND EVENTO.ID_EVENTO = NOTIFICA.EVE_ID_EVENTO";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		EventoModel aModel = new EventoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		aModel.setDescrTipoEvento(getString("COD_EVE"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento(getString("COD_PRO"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString("COD_MOV"));
		aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		aModel.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE"));
		// MEV10-s3: aggiunto campo in estrazione
		aModel.setCodTipoUfficioEmittente(getString("COD_TIPO_UFF_EMITTENTE"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("LUO_EMI"));
		aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
		aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("COD_ESI"));
		aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
		aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		aModel.setDescrLuogoDestinatario(getString("LUO_DES"));
		aModel.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
		aModel.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO"));
		aModel.setFasSiuIdFascicoloSiusDest(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST"));
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
		aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
		aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setDecIdDecretoOrdinanzaSiep(getBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP"));
		aModel.setDescrProvvedimento(getString("DESCR_PROV"));
		aModel.setEveIdEventoRevoca(getBigDecimal("EVE_ID_EVENTO_REVOCA"));
		aModel.setAnnIdAnnotazioneManuale(getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE"));
		aModel.setPenIdPenaResidua(getBigDecimal("PEN_ID_PENA_RESIDUA"));
		aModel.setDataEspulsioneSanzSost(getDate("DATA_ESPULSIONE_SANZ_SOST"));

		return aModel;
	}

	public GenericModel getModelViewProvvedimenti() throws DAOException {
		EventoFascicoloModel lEveFasc = new EventoFascicoloModel();
		EventoModel aModel = new EventoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));

		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		SoggettoModel lSoggetto = new SoggettoModel();
		SentenzaModel lSentenza = new SentenzaModel();

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setSogIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lFascicolo.setSenIdSentenza(getBigDecimal("ID_SENTENZA"));
		lFascicolo.setFlagValidato(getString("FLAG_VALIDATO"));
		lFascicolo.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lFascicolo.setDescrStatoProcedimento(getString("STATO_PROCEDIMENTO"));
		lFascicolo.setCodDistretto(getString("COD_DISTRETTO"));
		lFascicolo.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lFascicolo.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("LUOGO_NASCITA"));

		lSentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lSentenza.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		lSentenza.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
		lSentenza.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));

		lFascicolo.setSoggetto(lSoggetto);
		lFascicolo.setSentenza(lSentenza);
		lEveFasc.setFascicoloSiep(lFascicolo);
		return lEveFasc;
	}

	public GenericModel getModelViewFascicolo() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		SoggettoModel lSoggetto = new SoggettoModel();
		SentenzaModel lSentenza = new SentenzaModel();

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setSogIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lFascicolo.setSenIdSentenza(getBigDecimal("ID_SENTENZA"));
		lFascicolo.setFlagValidato(getString("FLAG_VALIDATO"));
		lFascicolo.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lFascicolo.setDescrStatoProcedimento(getString("STATO_PROCEDIMENTO"));
		lFascicolo.setCodDistretto(getString("COD_DISTRETTO"));
		lFascicolo.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lFascicolo.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));

		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("LUOGO_NASCITA"));

		lSentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lSentenza.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		lSentenza.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
		lSentenza.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));

		lFascicolo.setSoggetto(lSoggetto);
		lFascicolo.setSentenza(lSentenza);

		return lFascicolo;
	}

	public GenericModel getModelEventoProvvedimentiNonValidati() throws DAOException {
		EventoModel aModel = new EventoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString("DESCR_COD_MOTIVO"));

		aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));

		return aModel;
	}

	public GenericModel getModelEvento() throws DAOException {
		EventoModel aModel = new EventoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		aModel.setDescrTipoEvento(getString("COD_EVE"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento(getString("COD_PRO"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString("COD_MOV"));
		aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		aModel.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("LUO_EMI"));
		// MEV10-s3: aggiunto campo in estrazione
		aModel.setCodTipoUfficioEmittente(getString("COD_TIPO_UFF_EMITTENTE"));
		aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
		aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("COD_ESI"));
		aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
		aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		aModel.setDescrLuogoDestinatario(getString("LUO_DES"));
		aModel.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
		aModel.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO"));
		aModel.setFasSiuIdFascicoloSiusDest(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST"));
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
		aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
		aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
		aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setDecIdDecretoOrdinanzaSiep(getBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP"));
		aModel.setEveIdEventoRevoca(getBigDecimal("EVE_ID_EVENTO_REVOCA"));
		aModel.setAnnIdAnnotazioneManuale(getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE"));
		aModel.setPenIdPenaResidua(getBigDecimal("PEN_ID_PENA_RESIDUA"));

		return aModel;
	}

	public GenericModel getModelEventoSimeoneFascicolo() throws DAOException {
		FascicoloSiepModel aFasModel = new FascicoloSiepModel();
		SoggettoModel lSogMod = new SoggettoModel();

		aFasModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		aFasModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aFasModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));

		lSogMod.setNome(getString("NOME"));
		lSogMod.setCognome(getString("COGNOME"));

		aFasModel.setSoggetto(lSogMod);

		return aFasModel;
	}

	public GenericModel getModelEventoSimeoneON() throws DAOException {
		EventoNotificaModel lEveNotModel = new EventoNotificaModel();
		EventoModel lEveMod = new EventoModel();

		lEveMod.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEveMod.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		lEveMod.setDescrTipoEvento(getString("COD_EVE"));
		lEveMod.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEveMod.setDescrTipoProvvedimento(getString("COD_PRO"));
		lEveMod.setCodMotivo(getString("COD_MOTIVO"));
		lEveMod.setDescrMotivo(getString("COD_MOV"));
		lEveMod.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		lEveMod.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE"));
		lEveMod.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lEveMod.setDescrLuogoEmittente(getString("LUO_EMI"));
		// MEV10-s3: aggiunto campo in estrazione
		lEveMod.setCodTipoUfficioEmittente(getString("COD_TIPO_UFF_EMITTENTE"));
		lEveMod.setDataEmissione(getDate("DATA_EMISSIONE"));

		lEveNotModel.setEvento(lEveMod);

		return lEveNotModel;
	}

	/*
	 * public GenericModel getModelEventoSimeoneFascicolo() throws DAOException { EventoModel aModel = new
	 * EventoModel();
	 * 
	 * // Inserire le opportune set delle descrizioni! aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
	 * aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO")); aModel.setDescrTipoEvento(getString("COD_EVE"));
	 * aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
	 * aModel.setDescrTipoProvvedimento(getString("COD_PRO")); aModel.setCodMotivo(getString("COD_MOTIVO"));
	 * aModel.setDescrMotivo(getString("COD_MOV"));
	 * aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
	 * aModel.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE"));
	 * aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
	 * aModel.setDescrLuogoEmittente(getString("LUO_EMI"));
	 * aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
	 * aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
	 * aModel.setDataEmissione(getDate("DATA_EMISSIONE")); aModel.setCodEsito(getString("COD_ESITO"));
	 * aModel.setDescrEsito(getString("COD_ESI")); aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
	 * aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
	 * aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
	 * aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
	 * aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
	 * aModel.setDescrLuogoDestinatario(getString("LUO_DES"));
	 * aModel.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
	 * aModel.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
	 * aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
	 * aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
	 * aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
	 * aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
	 * aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
	 * aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
	 * aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
	 * aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
	 * aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
	 * aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
	 * aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO"));
	 * aModel.setFasSiuIdFascicoloSiusDest(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST"));
	 * aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO")); // Add 20030713 By paolo
	 * aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
	 * aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
	 * aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
	 * aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
	 * aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
	 * aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
	 * 
	 * aModel.setDecIdDecretoOrdinanzaSiep(getBigDecimal("DEC_ID_DECRETO_ORDINANZA_SIEP") );
	 * aModel.setPenAccIdPenaAccessoria(getBigDecimal("PEN_ACC_ID_PENA_ACCESSORIA") ); // STUB 09/02/2006
	 * aModel.setLegge(getString("COD_ABBR"));
	 * aModel.setEveIdEventoRevoca(getBigDecimal("EVE_ID_EVENTO_REVOCA"));
	 * aModel.setAnnIdAnnotazioneManuale(getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE"));
	 * aModel.setPenIdPenaResidua(getBigDecimal("PEN_ID_PENA_RESIDUA"));
	 * 
	 * aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
	 * aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
	 * 
	 * return aModel; }
	 */

	private String setOrderEventoDesc() {
		String lCondizioni = " ORDER BY DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC, ID_EVENTO DESC";
		return lCondizioni;
	}

	private String setOrderEventoAsc() {
		String lCondizioni = " ORDER BY EVENTO.DATA_EMISSIONE, EVENTO.DATA_INSERIMENTO, EVENTO.ID_EVENTO ";
		return lCondizioni;
	}

	// STUB 24/02/2005
	private String setOrderCodUfficioEventoAsc() {
		String lCondizioni = " ORDER BY EVENTO.COD_UFFICIO_EMITTENTE, DATA_EMISSIONE, DATA_INSERIMENTO, ID_EVENTO ";
		return lCondizioni;
	}

	// private String setOrderIdEvento() {
	// String lCondizioni = " ORDER BY ID_EVENTO DESC";
	// return lCondizioni;
	// }

	private String setOrderIdFascicoloIdEvento() {
		String lCondizioni = " ORDER BY ID_FASCICOLO_SIEP,ID_EVENTO DESC";
		return lCondizioni;
	}

	private String condizioneTipoEvento(String[] aTipoEvento) {
		String lCondizioni = " AND COD_TIPO_EVENTO IN (";
		for (int i = 0; i < aTipoEvento.length; i++) {
			lCondizioni += "'" + aTipoEvento[i] + "'";
			if (aTipoEvento.length > 1 && i < aTipoEvento.length - 1)
				lCondizioni += ",";
		}

		lCondizioni += ")";

		return lCondizioni;
	}

	private String condizioneTipoProvvedimentoIn(String[] aTipoProv) {
		String lCondizioni = " AND COD_TIPO_PROVVEDIMENTO IN (";
		for (int i = 0; i < aTipoProv.length; i++) {
			lCondizioni += "'" + aTipoProv[i] + "'";
			if (aTipoProv.length > 1 && i < aTipoProv.length - 1)
				lCondizioni += ",";
		}

		lCondizioni += ")";

		return lCondizioni;
	}

	public ByteArrayOutputStream getBlob() throws Exception {
		return getBlob("DOC_BLOB");
	}

	public void ricercatrasmessiL78del2013Xreport(ScadenzarioModel aModel, String acoduffcoll,
			Boolean Attivi, Boolean nostato) throws DAOException {
		String lSql = new String("");
		lSql = getSqlQueryL78del2013(acoduffcoll);
		lSql += " " + setCondizioniL78del2013(aModel, Attivi, nostato);
		lSql += " ORDER BY 6,7";

		setStatement(lSql);
	}

	public void ricercaEventoL78del2013(ScadenzarioModel aModel, Boolean Attivi, String acoduffcoll,
			Boolean nostato) throws DAOException {
		String lStatement = new String("");

		lStatement = "SELECT COUNT(*) HowManyRecords FROM (";
		lStatement += getSqlQueryL78del2013(acoduffcoll);
		lStatement += " " + setCondizioniL78del2013(aModel, Attivi, nostato) + ")";

		setStatement(lStatement);

	}

	public void ricercaEventoL78del2013Completa(ScadenzarioModel aModel, int aPage, Boolean Attivi,
			String acoduffcoll, Boolean nostato) throws DAOException {
		String lSql = new String("");
		lSql = getSqlQueryL78del2013(acoduffcoll);
		lSql += " " + setCondizioniL78del2013(aModel, Attivi, nostato);
		lSql += " ORDER BY 6,7";

		String lPaginedStatement = new String("");
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	protected String getSqlQueryL78del2013(String aCodUfficioUtenteConnesso) throws DAOException {
		String lStatement = new String("");

		lStatement = "SELECT DISTINCT(EVENTO.ID_EVENTO), EVENTO.COD_MOTIVO,";
		lStatement += " EVENTO.DATA_EMISSIONE, CODMOV.RV_MEANING MOTIVO, CODTIPPRO.RV_MEANING PROVVEDIMENTO,";
		lStatement += " FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR,FAS.ID_FASCICOLO_SIEP,";
		lStatement += " CODPRO.RV_MEANING STATO_DEL_PROCEDIMENTO,";
		lStatement += " SOG.ID_SOGGETTO, SOG.COGNOME, SOG.NOME, SOG.DATA_NASCITA, TIPCOM.DESCRIZIONE COMUNE_NASCITA ";
		lStatement += " FROM EVENTO, SOGGETTO SOG, STATO_PROCEDIMENTO PROC, FASCICOLO_SIEP FAS,";
		lStatement += " CG_REF_CODES CODPRO,";
		lStatement += " CG_REF_CODES CODEVE, CG_REF_CODES CODTIPPRO, CG_REF_CODES CODMOV, COMUNE TIPCOM";
		lStatement += " WHERE";
		lStatement += " EVENTO.COD_TIPO_EVENTO = '01'";
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO IN ('06', '12')";
		lStatement += " AND EVENTO.COD_MOTIVO IN ('1022', '1023', '1024','5522','5523','5524')";
		lStatement += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S'";
		lStatement += " AND EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO'";
		lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'";
		lStatement += " AND PROC.COD_STATO_PROCEDIMENTO = CODPRO.RV_LOW_VALUE  AND CODPRO.RV_ABBREVIATION = 'In esecuzione' AND CODPRO.RV_DOMAIN = 'STATO_PROCEDIMENTO'";
		lStatement += " AND FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
		lStatement += " AND FAS.COD_UFFICIO_INSERIMENTO = '" + aCodUfficioUtenteConnesso + "'";
		lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND PROC.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP";
		lStatement += " AND TIPCOM.COD_COMUNE = SOG.COD_COMUNE_NASCITA";

		return lStatement;
	}

	private String setCondizioniL78del2013(ScadenzarioModel aModel, Boolean attivi, Boolean nostato) {

		String lCondizioni = new String();

		String ldata1 = new String();
		String ldata2 = new String();
		ldata1 = "";
		ldata2 = "";

		if (aModel.getCodiciStatoNotifica() != null) {
			String[] aMotivo = aModel.getCodiciStatoNotifica();
			if (aMotivo.length > 0) {

				lCondizioni += " AND COD_MOTIVO IN (";
				for (int i = 0; i < aMotivo.length; i++) {
					if (aMotivo[i].equals("B"))
						lCondizioni += "'" + 1022 + "'";
					if (aMotivo[i].equals("Q"))
						lCondizioni += "'" + 1023 + "'";
					if (aMotivo[i].equals("O"))
						lCondizioni += "'" + 1024 + "'";
					if (aMotivo[i].equals("A"))
						lCondizioni += "'" + 5524 + "'";
					if (aMotivo[i].equals("P"))
						lCondizioni += "'" + 5522 + "'";
					if (aMotivo[i].equals("C"))
						lCondizioni += "'" + 5523 + "'";

					if (aMotivo.length > 1 && i < aMotivo.length - 1) {
						lCondizioni += ",";
					}
				}
				lCondizioni += ")";
			}
		}

		if (aModel.getDataEmissioneIniziale() != null)
			ldata1 = DateUtils.getDateToString(aModel.getDataEmissioneIniziale(), "dd/MM/yyyy");
		if (aModel.getDataEmissioneFinale() != null)
			ldata2 = DateUtils.getDateToString(aModel.getDataEmissioneFinale(), "dd/MM/yyyy");

		if (!ldata1.equals("") && !ldata2.equals(""))
			lCondizioni += " AND DATA_EMISSIONE BETWEEN TO_DATE('" + ldata1 + "','DD/MM/YYYY') AND TO_DATE('"
					+ ldata2 + "','DD/MM/YYYY')";

		if (!ldata1.equals("") && ldata2.equals(""))
			lCondizioni += " AND DATA_EMISSIONE >= TO_DATE('" + ldata1 + "','DD/MM/YYYY')";

		if (ldata1.equals("") && !ldata2.equals(""))
			lCondizioni += " AND DATA_EMISSIONE <= TO_DATE('" + ldata2 + "','DD/MM/YYYY')";

		if ((aModel.getChiaveAnnoIniziale() != null) && (aModel.getChiaveAnnoIniziale().intValue() >= 0)
				&& (aModel.getChiaveProgrIniziale() != null)
				&& (aModel.getChiaveProgrIniziale().intValue() >= 0)) {
			lCondizioni += " AND ( (FAS.CHIAVE_ANNO > " + aModel.getChiaveAnnoIniziale() + ")";
			lCondizioni += " OR (FAS.CHIAVE_ANNO = " + aModel.getChiaveAnnoIniziale()
					+ " AND FAS.CHIAVE_PROGR >= " + aModel.getChiaveProgrIniziale() + "))";
		}

		if ((aModel.getChiaveAnnoFinale() != null) && (aModel.getChiaveAnnoFinale().intValue() >= 0)
				&& (aModel.getChiaveProgrFinale() != null) && (aModel.getChiaveProgrFinale().intValue() >= 0)) {

			if ((aModel.getChiaveAnnoIniziale() == null) || (aModel.getChiaveAnnoIniziale().intValue() <= 0)
					&& (aModel.getChiaveProgrIniziale() == null)
					|| (aModel.getChiaveProgrIniziale().intValue() <= 0)) {
				lCondizioni += " AND ( (FAS.CHIAVE_ANNO < " + aModel.getChiaveAnnoFinale() + ")";
				lCondizioni += " OR (FAS.CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale()
						+ " AND FAS.CHIAVE_PROGR <= " + aModel.getChiaveProgrFinale() + "))";
			}

			lCondizioni += " AND ( (FAS.CHIAVE_ANNO < " + aModel.getChiaveAnnoFinale() + ")";
			lCondizioni += " OR (FAS.CHIAVE_ANNO = " + aModel.getChiaveAnnoFinale()
					+ " AND FAS.CHIAVE_PROGR <= " + aModel.getChiaveProgrFinale() + "))";
		}

		if (attivi && !nostato) {
			lCondizioni += " AND PROC.COD_STATO_PROCEDIMENTO IN ('0507','0508','0509')";
			lCondizioni += " AND PROC.PROGRESSIVO = 1";
		}
		if (!attivi && nostato) {
			lCondizioni += " AND PROC.COD_STATO_PROCEDIMENTO NOT IN ('0507','0508','0509')";
			lCondizioni += " AND PROC.PROGRESSIVO = 1";
		}
		if ((!attivi && !nostato) || (attivi && nostato)) {
			lCondizioni += " AND PROC.PROGRESSIVO = 1";
		}

		return lCondizioni;

	} // chiude setCondizioniL78del2013

	public GenericModel getModelL78del2013() throws DAOException {
		EventoFascicoloStatoModel lEveFasc = new EventoFascicoloStatoModel();
		EventoModel aModel = new EventoModel();
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		SoggettoModel lSoggetto = new SoggettoModel();
		StatoProcedimentoModel lSta = new StatoProcedimentoModel();

		aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setDescrMotivo(getString("MOTIVO"));
		aModel.setDescrProvvedimento(getString("PROVVEDIMENTO"));

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("COMUNE_NASCITA"));

		lSta.setDescrStatoProcedimento(getString("STATO_DEL_PROCEDIMENTO"));

		lFascicolo.setSoggetto(lSoggetto);
		lEveFasc.setEvento(aModel);
		lEveFasc.setStatoProcedimento(lSta);

		lEveFasc.setFascicoloSiep(lFascicolo);

		return lEveFasc;
	}

}