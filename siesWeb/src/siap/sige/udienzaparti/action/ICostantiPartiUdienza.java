package siap.sige.udienzaparti.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiPartiUdienza</p>
* <p>Description: Classe costanti delle Parti di una Udienza</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public interface ICostantiPartiUdienza
{
	 public static final String CAMPO_COD_TIPO_PART = "codTipoParte";
	 public static final String CAMPO_ID_SOGGETTO = "IdSoggetto";
	 public static final String CAMPO_COGNOME = "Cognome";
	 public static final String CAMPO_NOME = "Nome";
	 public static final String CAMPO_SESSO = "Sesso";
	 public static final String CAMPO_GIORNO_DATA_NASCITA = "GiornoDataNascita";
	 public static final String CAMPO_MESE_DATA_NASCITA = "MeseDataNascita";
	 public static final String CAMPO_ANNO_DATA_NASCITA = "AnnoDataNascita";
	 public static final String CAMPO_COD_COMUNE_NASCITA = "CodComuneNascita";
	 public static final String CAMPO_COD_STATO_NASCITA = "CodStatoNascita";
	 public static final String CAMPO_DESC_COMUNE_NASCITA_ESTERO = "DescComuneNascitaEstero";
	 public static final String CAMPO_COD_FISCALE = "CodFiscale";
	 public static final String CAMPO_ID_RESIDENZA = "IdResidenza";
	 public static final String CAMPO_INDIRIZZO = "Indirizzo";
	 public static final String CAMPO_COD_COMUNE_RESIDENZA = "CodComuneResidenza";
	 public static final String CAMPO_CAP_RESIDENZA = "CapResidenza";
	 public static final String CAMPO_DESC_COMUNE_ESTERO_RESIDENZA = "DescComuneEsteroResidenza";
	 public static final String CAMPO_COD_STATO_RESIDENZA = "CodStatoResidenza";
	 public static final String CAMPO_CONVOCAZIONE_UDIENZA = "ConvocazioneUdienza";
	 public static final String CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE = "FlagDomicilioPressoDifensore";
	 public static final String CAMPO_COD_AVVOCATO = "CodAvvocato";
	 public static final String CAMPO_NOTE = "Note";
	 public static final String CAMPO_SEDE = "Sede";
	 public static final String CAMPO_COD_DESTINATARIO = "CodDestinatario";
	 public static final String CAMPO_DENOMINAZIONE = "Denominazione";
	 public static final String CAMPO_RAG_SOCIALE = "RagSociale";
	 public static final String CAMPO_COD_PROVINCIA = "CodProvincia";
	 public static final String CAMPO_IND_SEDE_LEGALE = "IndSedeLegale";
	 public static final String CAMPO_IND_SEDE_OPERATIVA = "IndSedeOperativa";
	 public static final String CAMPO_COD_FISCALE_RAP = "CodFiscaleRap";
	 public static final String CAMPO_COD_IST_DETENZIONE = "CodIstDetenzione";
	 public static final String CAMPO_COD_LUOGO_DETENZIONE = "CodLuogoDetenzione";
	 public static final String CAMPO_INDIRIZZO_DETENZIONE = "IndirizzoDetenzione";
	 public static final String CAMPO_ID_AVVOCATO_PARTE_UDIENZA = "IdAvvocatoParteUdienza";
	 public static final String CAMPO_FLAG_SNT = "FlagSNT";
	 public static final String CAMPO_ID_EVENTO_UDIENZA = "IdEventoUdienza";
	 
	 public static final String CAMPO_ID_ENTITA_1    = "CampoIdEntita1";
	 public static final String VALORE_ID_ENTITA_1   = "ValoreIdEntita1";

	 public static final String CAMPO_ID_ENTITA_2    = "CampoIdEntita2";
	 public static final String VALORE_ID_ENTITA_2   = "ValoreIdEntita2";

	 public static final String CAMPO_ID_ENTITA_3    = "CampoIdEntita3";
	 public static final String VALORE_ID_ENTITA_3   = "ValoreIdEntita3";

	 public static final String CAMPO_ID_ENTITA_4    = "CampoIdEntita4";
	 public static final String VALORE_ID_ENTITA_4   = "ValoreIdEntita4";

	 public static final String CAMPO_AZIONE_CHIAMANTE = "AzioneChiamante";
	 
	 public static final String PG_LOAD_VISUALSUMMARY_PARTI_UDIENZA  = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/DettaglioPartiUdienzaSummary.jsp";
	 public static final String PG_LOAD_VISUALIZZA_PARTI_UDIENZA     = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/DettaglioPartiUdienza.jsp";
	 public static final String PG_LOAD_INSERISCI_PARTE_UDIENZA      = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/LoadInserisciParteUdienza.jsp";
	 public static final String PG_LOAD_MODIFICA_PARTE_UDIENZA       = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/LoadModificaParteUdienza.jsp";
	 public static final String PG_LOAD_MODIFICA_DIFENSORE_PARTE     = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/LoadModificaDifensore.jsp";
	 
	 public static final String RADIO_COD_PARTE = "CodParte";
	 public static final String JS_PARTE_UDIENZA = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/JsParteUdienza.js";	 
	 
	 public static final String DIV_PERSONA_FISICA = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/DivPersonaFisica.jsp";
	 public static final String DIV_PERSONA_GIURIDICA = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/DivPersonaGiuridica.jsp";
	 
	 public static final String CONTROLLI_PARTE_UDIENZA = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/ControlliParteUdienza.jsp";
	 
	 public static final String PG_TOOLBAR_HEADER_PARTI = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/toolbar_header_parti.jsp";
	 
	 public static final String PG_BUTTONS_MORE_PARAMETERS_PARTI  = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/buttons_more_parameters_parti.jsp";
	  
     public static final String PG_ASSEGNA_INSERISCI_AVVOCATO = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/AssegnazioneElencoAvvocati.jsp";
     public static final String PG_LOAD_INSERISCIAVVOCATO	  = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/LoadInserisciAvvocato.jsp";
     public static final String PG_INSERISCI_DIFENSORE	      = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/LoadInserisciDifensore.jsp";
     public static final String PG_DETTAGLIO_AVVOCATO         = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/DettaglioAvvocato.jsp";
     public static final String PG_DETTAGLIO_AVVOCATO_PARTE   = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/DettaglioAvvocatoParte.jsp";
     public static final String PG_LOAD_SINTESIPARTEUDIENZA	  = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/SintesiParteUdienza.jsp";
     public static final String PG_DETTAGLIO_PARTEUDIENZA	  = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/DettaglioParteUdienza.jsp";
     public static final String PG_LOAD_DESTINATARI           = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/Destinatari.jsp";
     public static final String PG_SOSTITUZIONE_AVVOCATO      = IWebConstants.ROOT_DIR + "files/siap/sige/udienzaparti/SostituzioneDifensore.jsp";
     
}