package siap.jms;

public interface ICostantiJMS {

	public static final String QUEUE_IN_ARRIVO = "QUEUE_IN_ARRIVO";
	public static final String QUEUE_IN_PARTENZA = "QUEUE_IN_PARTENZA";
	public static final String QUEUE_STAMPA = "QUEUE_STAMPA";

	public static final String POOL_SIZE = "POOL_SIZE";

	public static final String DESTINATARIO = "destinatario";
	public static final String MITTENTE = "mittente";

	public static final String BDI_DESTINATARIA = "BdiDestinataria";
	public static final String BDI_MITTENTE = "BdiMittente";
	public static final String STESSA_BDI = "StessaBDI";

	public static final String UFFICIO_DESTINATARIO = "UfficioDestinatario";
	public static final String UFFICIO_MITTENTE = "UfficioMittente";
	public static final String UTENTE_MITTENTE = "UtenteMittente";

	public static final String COD_BDI_DESTINATARIA = "CodBdiDestinataria";
	public static final String COD_BDI_MITTENTE = "CodBdiMittente";
	public static final String COD_UFFICIO_DESTINATARIO = "CodUfficioDestinatario";
	public static final String COD_UFFICIO_MITTENTE = "CodUfficioMittente";
	public static final String COD_ESITO = "CodEsito";

	public static final String TIPO_MESSAGGIO = "TipoMessaggio";
	public static final String ID_MESSAGGIO = "IDMessaggio";
	public static final String CORRELATION_ID_MESSAGGIO = "CorrelationIDMessaggio";

	public static final String CHIAVE_ANNO_SIEP = "ChiaveAnnoSiep";
	public static final String CHIAVE_PROGR_SIEP = "ChiaveProgrSiep";
	public static final String CHIAVE_UFFICIO_SIEP = "ChiaveUfficioSiep";

	public static final String CHIAVE_PROGR_SIEP_ORIGIN = "ChiaveProgrSiepOrigin";
	public static final String CHIAVE_ANNO_SIUS = "ChiaveAnnoSius";
	public static final String CHIAVE_PROGR_SIUS = "ChiaveProgrSius";

	public static final String CHIAVE_ANNO_SIEPE = "ChiaveAnnoSiepe"; // UEPE
	public static final String CHIAVE_PROGR_SIEPE = "ChiaveProgrSiepe"; // UEPE
	public static final String NOME_SOGGETTO = "NomeSoggetto"; // UEPE
	public static final String COGNOME_SOGGETTO = "CognomeSoggetto"; // UEPE
	public static final String DATA_NASCITA = "DataNascita"; // UEPE
	public static final String COD_STATO_NASCITA = "CodStatoNascita"; // UEPE
	public static final String COD_COMUNE_NASCITA = "CodComuneNascita"; // UEPE
	public static final String CHIAVE_ANNO_FAS_CUMULANTE = "ChiaveAnnoCumulante"; // SIEP - Trasmissione
																					// Competenza
	public static final String CHIAVE_PROGR_FAS_CUMULANTE = "ChiaveProgrCumulante"; // SIEP - Trasmissione
	public static final String CHIAVE_UFFICIO_FAS_CUMULANTE = "ChiaveUfficioCumulante"; // SIEP - Trasmissione
	// mev 39: aggiungo costante per trasmissione della data del cumulo																					// Competenza
	public static final String DATA_EMISSIONE_CUMULO = "DataEmissioneCumulo";
	
	public static final String NOTE = "Note"; // SIEP - Trasmissione Competenza

	public static final String DELIVERY_MODE = "DeliveryMode";
	public static final String COD_UFFICIO_INOLTRO = "CodUfficioInoltro";
	public static final String COD_BDI_INOLTRO = "CodBdiInoltro";
	public static final String COD_UFFICIO_REPLY_TO = "CodUfficioReplyTo";
	public static final String COD_BDI_REPLY_TO = "CodBdiReplyTo";
	public static final String JMS_CORRELATION_REPLY_TO = "JmsCorrelationReplyTo";
	public static final String ID_MESSAGGIO_SOLLECITATO = "IdMessaggioSollecitato";
	public static final String ID_RICHIESTA = "IdRichiesta";

	public static final String TIPO_OPERAZIONE = "TipoOperazione";

	public static final String RICHIESTA = "01";
	public static final String ESITO = "02";
	public static final String RICHIESTA_RICERCA = "03";
	public static final String ESITO_RICERCA = "04";

	public static final String LOCAL = "JMS_LOCAL";
	public static final String ROMA = "ROMA";
	public static final String NAPOLI = "NAPOLI";
	public static final String GENOVA = "GENOVA";
	public static final String TORINO = "TORINO";

	public static final String AVELLINO = "AVELLINO";

	public static final String COD_TUTTE = "00000000000";
	public static final String TUTTE = "TUTTE";

	public static final String LOCAL_MITTENTE = "JMS_LOCAL_MITTENTE";

	public static final String CONTEXT_FACTORY = "JndiInitialContextFactory";
	public static final String JMS_CONNECTION_FACTORY = "JmsQueueConnectionFactory";

	// ****** !NB! CAMBIANDO IL CONTENUTO DI QUESTE COSTANTI
	// ****** RICOMPILARE LE CLASSI CHE LE UTILIZZANO
	// ISTANZA
	public static final String TRASFERIMENTO_ISTANZA = "00001";
	public static final String ESITO_TRASFERIMENTO_ISTANZA = "00002";

	// ORDINANZA
	public static final String TRASFERIMENTO_ORDINANZA = "00003";
	public static final String ESITO_TRASFERIMENTO_ORDINANZA = "00004";

	// DECRETO
	public static final String TRASFERIMENTO_DECRETO = "00005";
	public static final String ESITO_TRASFERIMENTO_DECRETO = "00006";

	// SENTENZA
	public static final String TRASFERIMENTO_SENTENZA = "00007";
	public static final String ESITO_TRASFERIMENTO_SENTENZA = "00008";

	// RIFERIMENTO FASCICOLO SIUS
	public static final String TRASFERIMENTO_RIF_FAS_SIUS = "00013";
	public static final String ESITO_TRASFERIMENTO_RIF_FAS_SIUS = "00014";

	// RICERCA
	public static final String RICERCA_FASCICOLO = "00020";
	public static final String ESITO_RICERCA_FASCICOLO = "00030";
	public static final String RICERCA_FASCICOLO_PER_TRASFERIMENTO = "00040";
	public static final String ESITO_FASCICOLO_PER_TRASFERIMENTO = "00050";
	public static final String RICERCA_SOGGETTO = "00010";
	public static final String ESITO_RICERCA_SOGGETTO = "00011";

	// RICORSO / IMPUGNAZIONE
	public static final String TRASFERIMENTO_RICORSO = "00015";
	public static final String ESITO_TRASFERIMENTO_RICORSO = "00016";

	// ORDINE ESECUZIONE - PROVVEDIMENTO SIEP (ISTANZA)
	public static final String TRASFERIMENTO_PROVVEDIMENTO = "00017";
	public static final String ESITO_TRASFERIMENTO_PROVVEDIMENTO = "00018";

	// ATTIVITA - SIEPE
	public static final String TRASFERIMENTO_ATTIVITA = "00021";
	public static final String ESITO_TRASFERIMENTO_ATTIVITA = "00022";

	// RICHIESTA RELAZIONE CSSA x SIEPE
	public static final String TRASFERIMENTO_RICHIESTA_RELAZIONE = "00023";
	public static final String ESITO_TRASFERIMENTO_RICHIESTA_RELAZIONE = "00024";

	// RICHIESTA CSSA x SIEPE
	public static final String TRASFERIMENTO_RICHIESTA_UEPE = "00025";
	public static final String ESITO_TRASFERIMENTO_RICHIESTA_UEPE = "00026";

	// RELAZIONE UEPEx SIEPE
	public static final String TRASFERIMENTO_RELAZIONE_UEPE = "00027";
	public static final String ESITO_TRASFERIMENTO_RELAZIONE_UEPE = "00028";

	// OPPOSIZIONE/RICORSO
	public static final String TRASFERIMENTO_OPPOSIZIONE_RICORSO = "00029";
	public static final String ESITO_TRASFERIMENTO_OPPOSIZIONE_RICORSO = "00033";

	// RELAZIONE DELLA RICHIESTA UEPE
	public static final String TRASFERIMENTO_RELAZIONE_RICHIESTA_UEPE = "00060";
	public static final String ESITO_TRASFERIMENTO_RELAZIONE_RICHIESTA_UEPE = "00061";

	// RELAZIONE DELL'ATTIVITA UEPE
	public static final String TRASFERIMENTO_RELAZIONE_ATTIVITA = "00062";
	public static final String ESITO_TRASFERIMENTO_RELAZIONE_ATTIVITA = "00063";

	// 18/07/2007 SANZIONE SOSTITUTIVA
	public static final String TRASFERIMENTO_SANZIONE_SOSTITUTIVA = "00064";
	public static final String ESITO_TRASFERIMENTO_SANZIONE_SOSTITUTIVA = "00065";

	// TRASMISSIONE PER COMPETENZA
	public static final String TRASFERIMENTO_COMPETENZA = "00066";
	public static final String ESITO_TRASFERIMENTO_COMPETENZA = "00067";
	public static final String SOLLECITO_TRASFERIMENTO_COMPETENZA = "00068";
	// TRASMISSIONE PER COMPETENZA - SEGUITO ATTI
	public static final String SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA = "00078";
	public static final String ESITO_SEGUITO_ATTI = "00080";

	// TRASMISSIONE ATTI PER CONVERSIONE (Pene pecuniarie) 02/2009
	public static final String TRASFERIMENTO_ATTI_CONVERSIONE = "00069";
	public static final String ESITO_TRASFERIMENTO_ATTI_CONVERSIONE = "00070";

	// TRASMISSIONE PER COMPETENZA MISURE DI SICUREZZA
	public static final String TRASFERIMENTO_COMPETENZA_MS = "00071";
	public static final String ESITO_TRASFERIMENTO_COMPETENZA_MS = "00072";
	public static final String SOLLECITO_TRASFERIMENTO_COMPETENZA_MS = "00073";

	// Restitizione atti iscritti in classe IV (da confermare)
	// public static final String RESTITIZIONE_ATTI_MS = "00074";
	// public static final String ESITO_RESTITIZIONE_ATTI_MS = "00075";
	// new d.f. 09/04/2015 x distinguere la trasmissione x competenza da quella x l'esecuzione
	public static final String TRASFERIMENTO_ESECUZIONE_MS = "00074";

	// n.b. è la RICHIESTA ATTI ad altro ufficio da non confondere con TRASFERIMENTO_COMPETENZA
	// un cui si TRASMETTONO gli atti richiesti
	public static final String RICHIESTA_TRASMISSIONE_ATTI_PER_COMP = "00075";
	public static final String SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP = "00076";
	// RIGETTO RICHIESTA ATTI ad altro ufficio (a seguito di una Richiesta TRASMISSIONE ATTI per COMPETENZA)
	public static final String RIGETTO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP = "00077";
	// RICHIESTA ACCERTAMENTO PERICOLOSITA SOCIALE
	public static final String TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE = "00045";
	public static final String ESITO_TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE = "00046";

	// RICHIESTA TRASMISSIONE ATTI RICHIESTA CESSAZIONE/PROSECUZIONE DELLA MISURA IN CORSO EX ART. 51 BIS
	public static final String RICHIESTA_CESSAZIONE_MISURA = "00031";
	public static final String RICHIESTA_PROSECUZIONE_MISURA = "00032";

	//
	public static final String COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI = "00079"; // Cumulo STEP2

	// ---------------ESITO------------
	public static final String TROVATO = "10000";
	public static final String NON_TROVATO = "10001";
	public static final String NON_SPEDITO = "00100";
	public static final String CANCELLATO = "01000"; // STUB 05/11/2004
	public static final String PRESAINCARICO = "01001"; // UEPE + SIEP TRASFERIMENTO COMPETENZA
	public static final String PRESAVISIONE = "01002"; // UEPE
	public static final String RESTITUITO = "01003"; // UEPE + SIEP TRASFERIMENTO COMPETENZA
	public static final String TRASFERITO = "01004"; // SIEP TRASFERIMENTO COMPETENZA
	public static final String ISCRITTO_CLASSE_IV = "01005"; // SIEP TRASFERIMENTO COMPETENZA
	public static final String TRASMESSO = "01006"; // SIEP TRASFERIMENTO COMPETENZA - RICHIESTA RICEVUTA e
													// TRASMESSA RISPOSTA
	public static final String RIGETTATO = "01007"; // SIEP TRASFERIMENTO COMPETENZA - RICHIESTA RICEVUTA e
													// RIGETTATA

	public static final String ASSORBITO_IN_CUMULO = "01009"; // SIEP TRASFERIMENTO COMPETENZA -
																// Fascicolo/Titolo ASSORBITO IN CUMULO dal
																// Cumulante

	public static final String DESTINAZIONE_NON_RAGGIUNGIBILE = "00100";
	public static final String POSITIVO = "00000";
	public static final String CHIAVE_DUPLICATA = "00001";
	public static final String CONSTRAINT_VIOLATA = "00002"; // STUB 20/04/2005.
	public static final String NULL_NON_CONSENTITO = "01400";
	public static final String ERRORE_GENERICO = "33333";
	//MEV_67
	public static final String ERRORE_CARICAMENTO = "99999";

	// new MS. Nel caso di MS viene sempre salvata in locale la richiesta (01)
	// anche se stessa BDI. Il DELIVERY_MODE_INVIATO serve per distinguere
	// il messaggio inviato da quello ricevuto
	public static final String DELIVERY_MODE_INVIATO = "00001";
	public static final String DELIVERY_MODE_RICEVUTO = "00002";
	public static final String DELIVERY_MODE_INOLTRATO = "00003"; // gli invii x inoltro hanno codice 00003
																	// per distinguerli dagli invii diretti
	// public static final String ESITO_RICERCA_FASCICOLO = "00030";

	// NUOVA INFRASTRUTTURA: porta broker jms
	public static final String PORT = "PORT";
	
	// 28/05/2019 [EC] -  MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	public static final String ERRORE_DEPLOY   = "11111";
	public static final String ESITO_DI_ERRORE = "01110";

}