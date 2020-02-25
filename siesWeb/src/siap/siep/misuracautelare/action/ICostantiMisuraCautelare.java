package siap.siep.misuracautelare.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiMisuraCautelare</p>
* <p>Description: Classe di costanti di MisuraCautelare</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiMisuraCautelare
{
		 public static final String CAMPO_ID_MISURA_CAUTELARE = "IdMisuraCautelare";
		 public static final String CAMPO_COD_TIPO_MISURA = "CodTipoMisura";
		 public static final String CAMPO_COD_TIPO_MISURA_DETENTIVA = "CodTipoMisuraDetentiva";		 
		 public static final String CAMPO_COD_TIPO_MISURA_NON_DETENTIVA = "CodTipoMisuraNonDetentive";
		 public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
		 public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
		 public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
		 public static final String CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA = "GiornoDataEmissioneOrdinanza";
		 public static final String CAMPO_MESE_DATA_EMISSIONE_ORDINANZA = "MeseDataEmissioneOrdinanza";
		 public static final String CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA = "AnnoDataEmissioneOrdinanza";

		 public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
		 public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
		 public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
		 public static final String CAMPO_NUM_ANNI = "NumAnni";
		 public static final String CAMPO_NUM_MESI = "NumMesi";
		 public static final String CAMPO_NUM_GIORNI = "NumGiorni";
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
		 public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
		 public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
		 //nuovi
		 public static final String CAMPO_COD_MOTIVO_NON_COMPUTABILE = "CodMotivoNonComputabile";
		 //modifica relativa al tipo istituto
		 public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione";
		 public static final String COD_ID_IST_DETENZIONE = "CodiceIstDetIdIstitutoDetenzione";
		 public static final String CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE_COMUNE = "Comune";
		 // public static final String CAMPO_COD_TIPO_ISTITUTO_DETENZIONE = "CodTipoIstitutoDetenzione";
		 public static final String CAMPO_ALTRO_LUOGO_DETENZIONE = "AltroLuogoDetenzione";
		 // public static final String CAMPO_COD_LUOGO_DETENZIONE = "CodLuogoDetenzione";
		 public static final String CAMPO_NUM_RIFER = "NumRifer";
		 public static final String CAMPO_COD_TIPO_UFFICIO_RIFER = "CodTipoUfficioRifer";
		 public static final String CAMPO_COD_LUOGO_UFFICIO_RIFER = "CodLuogoUfficioRifer";
		 public static final String COD_LUOGO_COMUNE_UFF_RIFER = "CodiceLuogoComuneUfficioRifer";
		 public static final String CAMPO_COD_SEDE_UFFICIO_PM = "CodSedeUfficioPM";
		 public static final String COMUNE_COD_SEDE_UFFICIO_PM = "ComuneCodSedeUfficioPM";
		 public static final String CAMPO_GIORNO_DATA_FUNGIBILITA = "GiornoDataFungibilita";
		 public static final String CAMPO_MESE_DATA_FUNGIBILITA = "MeseDataFungibilita";
		 public static final String CAMPO_ANNO_DATA_FUNGIBILITA = "AnnoDataFungibilita";
		 public static final String CAMPO_NOTE = "Note";
		 //misure cautelari computabili e non computabili 
		 public static final String ANNO_FASC_BDMC = "AnnoFascBdmc"; 
		 public static final String NUME_FASC_BDMC = "NumeFascBdmc";
		 public static final String ANNO_RGNR = "AnnoRgnr";
		 public static final String NUMERO_RGNR = "NumeroRgnr";
		 public static final String ANNO_REG_GEN = "AnnoRegGen";
		 public static final String NUMERO_REG_GEN = "NumeroRegGen";
		 public static final String TIPO_UFFICIO_REG_GEN = "TipoUfficioRegGen";
		 public static final String AUTORITA_EMITTENTE = "AutoritaEmittente";
		 public static final String	AUTORITA_EMITTENTE_LUOGO = "AutoritaEmittenteLuogo";
		 public static final String	AUTORITA_COMPETENTE = "AutoritaCompetente";
		 public static final String	AUTORITA_COMPETENTE_SEDE = "AutoritaCompetenteSede";
		 public static final String	AUTORITA_COMPETENTE_INDIRIZZO = "AutoritaCompetenteIndirizzo";
		 public static final String	ANNO_RIFER = "AnnoRifer";
		 //public static final String INDIRIZZO = "Indirizzo";
		 public static final String CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE = "CodTipoUfficioMisuraCautelare";
		 public static final String CAMPO_COD_TIPO_MISURA_GIORNI_TOT = "CodTipoMisuraGiorniTot";
		 public static final String CAMPO_COD_TIPO_MISURA_GIORNI = "CodTipoMisuraGiorni";
		 public static final String CAMPO_COD_TIPO_MISURA_MESI = "CodTipoMisuraMesi";
		 public static final String CAMPO_COD_TIPO_MISURA_ANNI = "CodTipoMisuraAnni";
		 
		 public static final String CAMPO_COD_LUOGO_DI_ESPIAZIONE = "CodLuogoDiEspiazione";
		 // public static final String	AUTORITA_COMPETENTE_PER_TERRITORIO = "AutoritaCompetentePerTerritorio";
		 public static final String CAMPO_COD_SEDE_UFFICIO_PER_TERRITORIO = "CodSedeUfficioPerTerritorio";
		 public static final String ANNO_SIEP = "AnnoSiep";
		 public static final String NUMERO_SIEP = "NumeroSiep";
		 public static final String CAMPO_COD_SEDE_PROVVEDIMENTO_FUNGIBILITA = "CodSedeUfficioProvvedimentodIFungibilita";
	
	
		
		 					
		 //public static final String ESPIAZIONE_PENA_ISTITUTO_DETENZIONE = "EspiazionePenaIstitutoDetenzione";
		 //public static final String ESPIAZIONE_PENA_ALTRO_LUOGO = "EspiazionePenaAltroLuogo";
		 public static final String ESPIAZIONE_PENA_ISTITUTO_DETENZIONE_ALTRO_LUOGO = "EspiazionePenaIstDeteAltroLuogo";

		 public static final String CAMPO_ANNO_FASC_BDMC = "AnnoFascBdmc"; 
		 public static final String CAMPO_NUME_FASC_BDMC = "NumeFascBdmc"; 
		 public static final String CAMPO_ANNO_RGNR = "AnnoRgnr"; 
		 public static final String CAMPO_NUMERO_RGNR = "NumeroRgnr"; 
		 public static final String CAMPO_ANNO_REG_GEN = "AnnoRegGen"; 
		 public static final String CAMPO_NUMERO_REG_GEN = "NumeroRegGen"; 
		 public static final String CAMPO_TIPO_UFFICIO_REG_GEN = "TipoUfficioRegGen"; 
		 public static final String CAMPO_AUTORITA_EMITTENTE = "AutoritaEmittente"; 
		 public static final String CAMPO_AUTORITA_EMITTENTE_LUOGO = "AutoritaEmittenteLuogo"; 
		 public static final String CAMPO_AUTORITA_COMPETENTE = "AutoritaCompetente"; 
		 public static final String CAMPO_AUTORITA_COMPETENTE_SEDE = "AutoritaCompetenteSede"; 
		 public static final String CAMPO_AUTORITA_COMPETENTE_INDIRIZZO = "AutoritaCompetenteIndirizzo"; 
		 public static final String CAMPO_ANNO_RIFER = "AnnoRifer"; 
		 public static final String CAMPO_CODICE_UFFICIO_PM_SEDE = "CodiceUfficioPmSede"; 

		 public static final String PG_LOAD_RICERCAMISURACAUTELARE	= IWebConstants.ROOT_DIR + "files/siap/siep/misuracautelare/LoadRicercaMisuraCautelare.jsp";
		 public static final String PG_LOAD_DETTAGLIOMISURACAUTELARE	= IWebConstants.ROOT_DIR + "files/siap/siep/misuracautelare/DettaglioMisuraCautelare.jsp";
		 public static final String PG_RICERCAMISURACAUTELARE	= IWebConstants.ROOT_DIR + "files/siap/siep/misuracautelare/RicercaMisuraCautelare.jsp";
		 public static final String PG_LOAD_INSERISCIMISURACAUTELARE	= IWebConstants.ROOT_DIR + "files/siap/siep/misuracautelare/LoadInserisciMisuraCautelare.jsp";
		 public static final String PG_LOAD_MODIFICAMISURACAUTELARE	= IWebConstants.ROOT_DIR + "files/siap/siep/misuracautelare/LoadModificaMisuraCautelare.jsp";
		 public static final String PG_LOAD_INSERISCIMISURACAUTELARECESSATE	= IWebConstants.ROOT_DIR + "files/siap/siep/misuracautelare/LoadInserisciMisuraCautelareCessate.jsp";
		 public static final String PG_LOAD_INSERISCIMISURACAUTELARECESSATECOMPUTABILI	= IWebConstants.ROOT_DIR + "files/siap/siep/misuracautelare/LoadInserisciMisuraCautelareCessateComputabili.jsp";
		 public static final String PG_LOAD_INSERISCIMISURACAUTELARECESSATENONCOMPUTABILI	= IWebConstants.ROOT_DIR + "files/siap/siep/misuracautelare/LoadInserisciMisuraCautelareCessateNonComputabili.jsp";
		 public static final String PG_GRIGLIA_MISURE_CAUTELARI = IWebConstants.ROOT_DIR + "files/siap/siep/misuracautelare/LoadGrigliaBottoniMisureCautelari.jsp";

}