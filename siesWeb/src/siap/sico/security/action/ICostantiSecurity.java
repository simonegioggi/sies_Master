package siap.sico.security.action;

import siap.sico.web.ISICOCostantiWeb;

public interface ICostantiSecurity extends ISICOCostantiWeb
{
  // Nome campi della form.
  public static final String CAMPO_USER_ID      = "userid";
  public static final String CAMPO_PASSWORD     = "password";
  public static final String CAMPO_TIPO_UFFICIO = "tipoUfficio";
  public static final String CAMPO_COMUNE       = "comune";

  public static final String CAMPO_ID_FUNZIONE  = "IdFunzione";
  public static final String CAMPO_ID_ENTITA    = "CampoIdEntita";
  public static final String VALORE_ID_ENTITA   = "ValoreIdEntita";
  public static final String CAMPO_ID_ENTITAPP    = "CampoIdEntitaPP";
  public static final String VALORE_ID_ENTITAPP   = "ValoreIdEntitaPP";

  public static final String CAMPO_ID_ENTITA_PROVV    = "CampoIdEntitaProvv";
  public static final String VALORE_ID_ENTITA_PROVV   = "ValoreIdEntitaProvv";

  public static final String FUN_RADICE_MENU_VRT = "FunRadiceMenuVrt";
  public static final String FUN_RADICE_MENU_ORZ = "FunRadiceMenuOrz";
  public static final String FUN_RADICE_CONTESTO = "FunRadiceContesto";
  public static final String FUN_RADICE_MENU_SR  = "FunRadiceMenuSceltaRapida";
  public static final String FUN_ANTENATE        = "FunAntenate";
  public static final String FUN_FIGLIE          = "FunFiglie";
  public static final String FUNZIONE            = "Funzione";

  // utilizzati per buttos_avvocato

  public static final String CAMPO_NOME      = "nome";
  public static final String CAMPO_COGNOME   = "cognome";
  public static final String CAMPO_FORO = "foro";
  public static final String VALORE_NOME      = "valorenome";
  public static final String VALORE_COGNOME   = "valorecognome";
  public static final String VALORE_FORO = "valoreforo";

  //utilizzati per buttos_more_parameters
  public static final String CAMPO_ENTITA_CHIAVE_QUATTRO = "ChiaveQuattro";
  public static final String CAMPO_VALORE_CHIAVE_QUATTRO = "ValoreQuattro";
  
  // intervento per 11.2.1
  public static final String CAMPO_ENTITA_CHIAVE_MAGIS = "ChiaveMagis";
  public static final String CAMPO_VALORE_CHIAVE_MAGIS = "ValoreMagis";  
  
  public static final String SESSION_UTENTE_CONNESSO = "UtenteConnesso";
  public static final String PG_CHANGE_PASSWORD = ROOT_DIR +"files/ChangeUserPassword.jsp";
}