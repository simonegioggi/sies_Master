package siap.siep.penacumulo.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiPenaCumulo</p>
* <p>Description: Classe di costanti di PenaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiPenaCumulo
{
		 public static final String CAMPO_ID_PENA_CUMULO = "IdPenaCumulo";
		 public static final String CAMPO_COD_TIPO_PENA_DETENTIVA = "CodTipoPenaDetentiva";
		 public static final String CAMPO_NUM_ANNI_RECLUSIONE = "NumAnniReclusione";
		 public static final String CAMPO_NUM_MESI_RECLUSIONE = "NumMesiReclusione";
		 public static final String CAMPO_NUM_GIORNI_RECLUSIONE = "NumGiorniReclusione";
		 public static final String CAMPO_IMPORTO_MULTA = "ImportoMulta";
		 public static final String CAMPO_NUM_ANNI_ARRESTO = "NumAnniArresto";
		 public static final String CAMPO_NUM_MESI_ARRESTO = "NumMesiArresto";
		 public static final String CAMPO_NUM_GIORNI_ARRESTO = "NumGiorniArresto";
		 public static final String CAMPO_IMPORTO_AMMENDA = "ImportoAmmenda";
		 public static final String CAMPO_GIORNO_DATA_DECORRENZA_PENA = "GiornoDataDecorrenzaPena";
		 public static final String CAMPO_MESE_DATA_DECORRENZA_PENA = "MeseDataDecorrenzaPena";
		 public static final String CAMPO_ANNO_DATA_DECORRENZA_PENA = "AnnoDataDecorrenzaPena";
		 public static final String CAMPO_MOTIVAZIONI = "Motivazioni";
		 public static final String CAMPO_NUM_ANNI_RECLUSIONE_SOSP = "NumAnniReclusioneSosp";
		 public static final String CAMPO_NUM_MESI_RECLUSIONE_SOSP = "NumMesiReclusioneSosp";
		 public static final String CAMPO_NUM_GIORNI_RECLUSIONE_SOSP = "NumGiorniReclusioneSosp";
		 public static final String CAMPO_NUM_ANNI_ARRESTO_SOSP = "NumAnniArrestoSosp";
		 public static final String CAMPO_NUM_MESI_ARRESTO_SOSP = "NumMesiArrestoSosp";
		 public static final String CAMPO_NUM_GIORNI_ARRESTO_SOSP = "NumGiorniArrestoSosp";
		 public static final String CAMPO_ESTREMI_ORDINANZA = "EstremiOrdinanza";
		 public static final String CAMPO_FLAG_ERGASTOLO = "FlagErgastolo";
		 public static final String CAMPO_NOTE = "Note";
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
		 public static final String CAMPO_CUM_ID_CUMULO = "CumIdCumulo";

     public static final String CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO = "NumAnniIsolamentoDiurno";
     public static final String CAMPO_NUM_MESI_ISOLAMENTO_DIURNO = "NumMesiIsolamentoDiurno";
     public static final String CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO = "NumGiorniIsolamentoDiurno";

     public static final String CAMPO_MISURA_SICUREZZA = "MisuraSicurezza";
     public static final String CAMPO_PENA_ACCESSORIA = "PenaAccessoria";
     public static final String CAMPO_NUM_GIORNI_LIB_ANTICIPATA = "NumGiorniLibAnticipata";
// 20/05/2014	- Nuova L.A. - decreto 2013/146	- Liberazione Anticipata diventa:
//		Liberazione Anticipata (L.A.), L.A. Speciale , Integrazione L.A.				     
public static final String CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA = "NumGiornilibAnt";
public static final String CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_SPE = "NumGiornilibAntSpe";
public static final String CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_INT = "NumGiornilibAntInt";     
public static final String CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92 = "GiorniRisarcimentoDanni";


		 public static final String PG_LOAD_RICERCAPENACUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/penacumulo/LoadRicercaPenaCumulo.jsp";
		 public static final String PG_LOAD_DETTAGLIOPENACUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/penacumulo/LoadRicercaPenaCumulo.jsp";
		 public static final String PG_RICERCAPENACUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/penacumulo/RicercaPenaCumulo.jsp";
		 public static final String PG_LOAD_INSERISCIPENACUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/penacumulo/LoadInserisciPenaCumulo.jsp";
}