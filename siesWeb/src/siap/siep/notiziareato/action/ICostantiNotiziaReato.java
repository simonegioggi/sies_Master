package siap.siep.notiziareato.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiNotiziaReato</p>
* <p>Description: Classe di costanti di Notizia di Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiNotiziaReato
{
		 public static final String CAMPO_ID_NOTIZIA_REATO = "IdNotiziaReato";
		 public static final String CAMPO_PROGR_NOTIZIA = "ProgrNotizia";
		 public static final String CAMPO_GIORNO_DATA_PERVENIMENTO = "GiornoDataPervenimento";
		 public static final String CAMPO_MESE_DATA_PERVENIMENTO = "MeseDataPervenimento";
		 public static final String CAMPO_ANNO_DATA_PERVENIMENTO = "AnnoDataPervenimento";
		 public static final String CAMPO_ACQUISIZIONE_DIRETTA = "AcquisizioneDiretta";
		 public static final String CAMPO_GIORNO_DATA_FATTO = "GiornoDataFatto";
		 public static final String CAMPO_MESE_DATA_FATTO = "MeseDataFatto";
		 public static final String CAMPO_ANNO_DATA_FATTO = "AnnoDataFatto";
		 public static final String CAMPO_COD_FONTE = "CodFonte";
		 public static final String CAMPO_TIPO_FONTE = "TipoFonte";
		 public static final String CAMPO_COD_COMUNE_FONTE = "CodComuneFonte";
		 public static final String CAMPO_NUM_REG_AUTORITA = "NumRegAutorita";
		 public static final String CAMPO_LUOGO_PROVENIENZA = "LuogoProvenienza";
		 public static final String CAMPO_GIORNO_DATA_ACQUISIZIONE = "GiornoDataAcquisizione";
		 public static final String CAMPO_MESE_DATA_ACQUISIZIONE = "MeseDataAcquisizione";
		 public static final String CAMPO_ANNO_DATA_ACQUISIZIONE = "AnnoDataAcquisizione";
		 public static final String CAMPO_NUMERO_RICEVUTA = "NumeroRicevuta";
		 public static final String CAMPO_DESCRIZIONE_FONTE = "DescrizioneFonte";
     public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP= "FasSieIdFascicoloSiep";
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
		 public static final String PG_LOAD_RICERCANOTIZIAREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/notiziareato/LoadRicercaNotiziaReato.jsp";
		 public static final String PG_LOAD_DETTAGLIONOTIZIAREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/notiziareato/DettaglioNotiziaReato.jsp";
		 public static final String PG_RICERCANOTIZIAREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/notiziareato/RicercaNotiziaReato.jsp";
		 public static final String PG_LOAD_INSERISCINOTIZIAREATO	= IWebConstants.ROOT_DIR + "files/siap/siep/notiziareato/LoadInserisciNotiziaReato.jsp";
		 //modifica integrazione REGE-SIES		
		 public static final String CAMPO_FLAG_ARRESTATO = "FlagArrestato";
		 public static final String CAMPO_FLAG_FOTOSEGNALATO = "FlagFotosegnalato";
		 public static final String CAMPO_GIORNO_DATA_ARRESTO = "GiornoDataArresto";
		 public static final String CAMPO_MESE_DATA_ARRESTO = "MeseDataArresto";
		 public static final String CAMPO_ANNO_DATA_ARRESTO = "AnnoDataArresto";
		 public static final String CAMPO_GIORNO_DATA_FERMO = "GiornoDataFermo";
		 public static final String CAMPO_MESE_DATA_FERMO = "MeseDataFermo";
		 public static final String CAMPO_ANNO_DATA_FERMO = "AnnoDataFermo";
		 public static final String CAMPO_GIORNO_DATA_FOTO = "GiornoDataFoto";
		 public static final String CAMPO_MESE_DATA_FOTO = "MeseDataFoto";
		 public static final String CAMPO_ANNO_DATA_FOTO = "AnnoDataFoto";
		 public static final String CAMPO_COD_COMUNE_FOTO = "CodComuneFoto";
		 public static final String CAMPO_AUTORITA_FOTO = "CodAutoritaFoto";
}