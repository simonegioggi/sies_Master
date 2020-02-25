package siap.siep.verbale.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.evento.model.EventoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: VerbaleSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Verbale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class EventoVerbaleSqlDAO extends SqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	 public EventoVerbaleSqlDAO (Connection con)
			{
			 super(con);
			}


 //
  // METODO RICERCA()
  //


  public void ricercaEventoVerbale(BigDecimal aKeyFasc)	 throws DAOException
		{
		 String lSql = getSqlQuery();

		 lSql += " " + setCondizioneEventoVerbale(aKeyFasc);
			 setStatement(lSql);
		}

  // AMBROSINO 07-02-2011  
  
  public void ricercaEventoOE(BigDecimal aKeyFasc)	 throws DAOException
	{
	  	
		String lSql = getSqlQuery();
		lSql += " " + setCondizioneEventoOE(aKeyFasc);
		setStatement(lSql);
	}
  
// END AMBROSINO
  
  public void ricercaEventoOELS(BigDecimal aKeyFasc)	 throws DAOException
	{
		 String lSql = getSqlQuery();

	     lSql += " " + setCondizioneOELS(aKeyFasc);
		 setStatement(lSql);
	}  

  public void ricercaEventoOELSRSCUM(BigDecimal aKeyFasc)	 throws DAOException
	{
		 String lSql = getSqlQuery();

	     lSql += " " + setCondizioneOELSRSCUM(aKeyFasc);
		 setStatement(lSql);
	}  


   protected String getSqlQuery()	 throws DAOException
  {
    String lStatement = new String("");

    lStatement +=  "SELECT ID_EVENTO, ";
    lStatement +=  " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
    lStatement +=  " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
    lStatement +=  " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
    lStatement +=  " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
    lStatement +=  " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
    lStatement +=  " NOME_SOGGETTO_PRESENTANTE, ";
    lStatement +=  " COGNOME_SOGGETTO_PRESENTANTE, ";
    lStatement +=  " DATA_EMISSIONE, ";
    lStatement +=  " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
    lStatement +=  " FLAG_PIU_MENO, ";
    lStatement +=  " DATA_TRASMISSIONE_ATTI, ";
    lStatement +=  " DATA_RICEZIONE_ATTI, ";
    lStatement +=  " COD_UFFICIO_DESTINATARIO, ";
    lStatement +=  " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
    lStatement +=  " ANNO_PROTOCOLLO, ";
    lStatement +=  " PROGR_PROTOCOLLO, ";
    lStatement +=  " DOC_BLOB, ";
    lStatement +=  " COD_OPERATORE_INSERIMENTO, ";
    lStatement +=  " DATA_INSERIMENTO,";
    lStatement +=  " COD_UFFICIO_INSERIMENTO,";
    lStatement +=  " COD_OPERATORE_AGGIORNAMENTO, ";
    lStatement +=  " DATA_AGGIORNAMENTO,";
    lStatement +=  " COD_UFFICIO_AGGIORNAMENTO, ";
    lStatement +=  " FAS_SIE_ID_FASCICOLO_SIEP, ";
    lStatement +=  " FAS_SIU_ID_FASCICOLO_SIUS, ";
    lStatement +=  " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
    lStatement +=  " FLAG_DOCUMENTO_REGISTRATO, ";
    lStatement +=  " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
    lStatement +=  " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
    lStatement +=  " TEM_ID_TEMPLATE,  "; // Add By Paolo
    lStatement +=  " FLAG_STAMPA_SIEP,  ";
    lStatement +=  " FLAG_STAMPA_SIUS,  ";
    lStatement +=  " FLAG_VIDEO_SIEP,  ";
    lStatement +=  " FLAG_VIDEO_SIUS  ";
    lStatement +=	 " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
    lStatement +=  " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
    lStatement +=  " WHERE";
    lStatement +=  " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
    lStatement +=  " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
    lStatement +=  " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND";
    lStatement +=  " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
    lStatement +=  " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
    lStatement +=  " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
    lStatement +=  " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
    lStatement +=  " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
    lStatement +=  " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO' ";


    return lStatement;
  }

 //
  // METODO GETMODEL()
  //

  public GenericModel getModel() throws DAOException
  {
    EventoModel aModel = new  EventoModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdEvento(getBigDecimal("ID_EVENTO") );
    aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO") );
    aModel.setDescrTipoEvento(getString("COD_EVE") );
    aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO") );
    aModel.setDescrTipoProvvedimento(getString("COD_PRO") );
    aModel.setCodMotivo(getString("COD_MOTIVO") );
    aModel.setDescrMotivo(getString("COD_MOV") );
    aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE") );
    aModel.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE") );
    aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE") );
    aModel.setDescrLuogoEmittente(getString("LUO_EMI") );
    aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE") );
    aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE") );
    aModel.setDataEmissione(getDate("DATA_EMISSIONE") );
    aModel.setCodEsito(getString("COD_ESITO") );
    aModel.setDescrEsito(getString("COD_ESI") );
    aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO") );
    aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI") );
    aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI") );
    aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO") );
    aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO") );
    aModel.setDescrLuogoDestinatario(getString("LUO_DES") );
    aModel.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO") );
    aModel.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP") );
    aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS") );
    aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO") );
    aModel.setCodMagistrato(getString("COD_MAGISTRATO") );
    aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO") );
    aModel.setFasSiuIdFascicoloSiusDest(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST"));
    aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
    // Add 20030713 By paolo
    aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
    aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
    aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
    aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
    aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
    aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));

    return aModel;
  }

  public String setCondizioneEventoVerbale(BigDecimal aKeyFasc)
	{
		 String lCondizioni = new String();
  
     lCondizioni +=  " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFasc;
     
//** REWORK 30/09/2004 DL -- NON DEVE PIU' NECESSARIAMENTE ESSERE UN OE
     //lCondizioni +=  " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'RS%' " ;
     //lCondizioni +=  " OR COD_MOTIVO IN ('0014','0015','0086','0196','0277')) ";

     lCondizioni +=  " AND FLAG_DOCUMENTO_REGISTRATO = 'S' ";
     lCondizioni +=  " ORDER BY DATA_INSERIMENTO DESC ";

 		 return lCondizioni;
  }

	public String  setCondizioneOELS(BigDecimal aKeyFasc)
	{
	  String lCondizioni = new String();
    
    lCondizioni +=  " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFasc;
    lCondizioni +=  " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'LS%') " ;
    lCondizioni +=  " AND FLAG_DOCUMENTO_REGISTRATO = 'S' ";
    lCondizioni +=  " ORDER BY DATA_INSERIMENTO DESC ";
         
 		return lCondizioni;
 	}
	 
	public String  setCondizioneOELSRSCUM(BigDecimal aKeyFasc)
	{
		 String lCondizioni = new String();
		 lCondizioni +=  " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFasc;
		 lCondizioni +=  " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'LS%'OR CODMOV.RV_HIGH_VALUE LIKE 'RS%' OR CODMOV.RV_HIGH_VALUE = 'CUMULO') " ;
		 lCondizioni +=  " AND FLAG_DOCUMENTO_REGISTRATO = 'S' ";
      	 lCondizioni +=  " ORDER BY DATA_INSERIMENTO DESC ";
      
		 return lCondizioni;
		}	 

	  // AMBROSINO 07-02-2011	
	
	  public String setCondizioneEventoOE(BigDecimal aKeyFasc)
		{
		//  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		//  siesLogger.debug("-- --- AMBROSINO --- -- setCondizioneEventoOE  -- ");
//Ambrosino 06-12-2011 - Ricerca degli eventi OE che devono essere legati con VVR
//Viene sostituita  --> lCondizioni +=  " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%'  OR  COD_TIPO_PROVVEDIMENTO IN ('04','06'))" ;--		 
// con la nuova condizione --> lCondizioni +=  "AND CODMOV.RV_ALT2_VALUE = 'CREA_VVR' " -- ;
		  
// E' stata modificata la tabella Cg_REF_CODE con l'aggiunta della chiave LEGA_VVR per tutti i codici interessati
//	Al Verbale Vane Ricerche ;  Se viene Inserito o modificato uno di questi codici, la stessa cosa 
// deve essere fatta nella procedura : "AGGIORNA_VVR" 			 
			 
			 String lCondizioni = new String();
			 lCondizioni +=  " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFasc;
//	     lCondizioni +=  " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%'  OR  COD_TIPO_PROVVEDIMENTO IN ('04','06'))" ;
			 
			 lCondizioni +=  " AND CODMOV.RV_ALT2_VALUE = 'LEGA-VVR' ";
			 lCondizioni +=  " AND FLAG_DOCUMENTO_REGISTRATO = 'S' ";
			 lCondizioni +=  " ORDER BY DATA_INSERIMENTO DESC ";

	 		 return lCondizioni;
	  }
	
}