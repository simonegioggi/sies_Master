package siap.sico.security;

import java.math.BigDecimal;

public interface ICostantiFunzioni
{
  public static final BigDecimal RADICE = new BigDecimal(0);
  public static final BigDecimal RADICE_SIGE = new BigDecimal(888);

  //CODICE TIPO VISUALIZZAZIONE
  public static final String FUNZIONE_MENU    = "ME";
  public static final String FUNZIONE_BOTTONE = "BO";
  public static final String FUNZIONE_COMBO   = "CO";
  public static final String FUNZIONE_LINK    = "LI";
  public static final String FUNZIONE_MSR     = "MR"; // Menu Scelta Rapida

  //CODICE TIPO FUNZIONE
  public static final String TIPO_INSERIMENTO        = "I";
  public static final String TIPO_DETTAGLIO          = "D";
  public static final String TIPO_MODIFICA           = "M";
  public static final String TIPO_STAMPA             = "S";
  public static final String TIPO_CANCELLA           = "C";
  public static final String TIPO_TRASFERIMENTO      = "T";
  public static final String TIPO_INSERIMENTO_COPIA  = "X";
  public static final String TIPO_RICERCA            = "R";
  public static final String TIPO_VALIDAZIONE        = "V";
  public static final String TIPO_RIAPERTURA         = "O"; // Riapertura del documento equivale ad una svalidazione
  public static final String TIPO_MODIFICA_DIFENSORE = "Y"; // Modifica del Difensore nella funzionalità di Gestione delle Parti (Offese/Civili)
  public static final String TIPO_LOAD_MODIFICA		 = "L";
  public static final String TIPO_ALLEGARE_DOC       = "A";
  
}