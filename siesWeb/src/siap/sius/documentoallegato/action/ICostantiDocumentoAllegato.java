package siap.sius.documentoallegato.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiDocumentoAllegato</p>
* <p>Description: Classe di costanti di DocumentoAllegato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiDocumentoAllegato
{
		 public static final String CAMPO_ID_DOCUMENTO_ALLEGATO = "IdDocumentoAllegato";
		 public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
		 public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
		 public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";
		 public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
		 public static final String CAMPO_NUMERO_PROGRESSIVO = "NumeroProgressivo";
		 public static final String CAMPO_FLAG_DOCUMENTO_REGISTRATO = "FlagDocumentoRegistrato";
		 public static final String CAMPO_DOC_BLOB = "DocBlob";
         public static final String CAMPO_BLOB = "CampoBlob";
         public static final String CAMPO_VALIDA = "CampoValida";
         public static final String CAMPO_AZIONE_DETTAGLIO = "AzioneDettaglio";
		 public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
		 public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
		 public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
		 public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
		 public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
		 public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
		 public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
		 public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
		 public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
		 public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
		 public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
		 public static final String CAMPO_TEM_ID_TEMPLATE = "TemIdTemplate";
		 public static final String PG_LOAD_RICERCADOCUMENTOALLEGATO	= IWebConstants.ROOT_DIR + "files/siap/sius/documentoallegato/LoadRicercaDocumentoAllegato.jsp";
		 public static final String PG_LOAD_DETTAGLIODOCUMENTOALLEGATO	= IWebConstants.ROOT_DIR + "files/siap/sius/documentoallegato/LoadRicercaDocumentoAllegato.jsp";
		 public static final String PG_RICERCADOCUMENTOALLEGATO	= IWebConstants.ROOT_DIR + "files/siap/sius/documentoallegato/ElencoDocumentiAllegati.jsp";
		 public static final String PG_LOAD_INSERISCIDOCUMENTOALLEGATO	= IWebConstants.ROOT_DIR + "files/siap/sius/documentoallegato/LoadInserisciDocumentoAllegato.jsp";
         public static final String PG_BUTTONS  = IWebConstants.ROOT_DIR + "files/siap/sius/documentoallegato/buttonsDocumentoAllegato.jsp";
         public static final String PG_ELENCODOCUMENTI = IWebConstants.ROOT_DIR + "files/siap/sius/documentoallegato/ElencoDocumentiAllegati.jsp";
         public static final String CAMPO_FLAG_FOGLIO_TRASMESSO = "FlagFoglioTrasmesso";
         public static final String CAMPO_FLAG_MOTIVO_NON_INVIO = "FlagMotivoNonInvio";

}