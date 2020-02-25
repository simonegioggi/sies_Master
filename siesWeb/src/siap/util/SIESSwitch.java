package siap.util;

import f3b.util.F3BException;
import f3b.util.F3BProperties;

/**
 *
 * <p>Title: SIESSwitch</p>
 * <p>Description: Classe che mantiene le variabilie per sapere se
 * i sottosistemi in via di sviluppo sono da vedere o meno.</p>
 */
public class SIESSwitch
{

  private static SIESSwitch mSIESSwitch = null;
  private static String mRegeSiesOn = "OFF";
  private static String mObscureFunction = "OFF";
  private static int mObscureFirst = 0;
  private static int mObscureLast = 0;
  private static String mReworkDettaglio = "OFF";
  private static String mReworkStatoEsecuzioneOn = "OFF";


  protected SIESSwitch()
  {
  }

  public static SIESSwitch getInstance()
  {
    if (mSIESSwitch == null)
      init();

    return mSIESSwitch;
  }

  public static String getRegeSies()
  {
    if (mSIESSwitch == null)
      init();

    return mRegeSiesOn;
  }

  public static String getObscureFunction()
  {
    if (mSIESSwitch == null)
      init();

    return mObscureFunction;
  }

  public static int getObscureFirst()
  {
    if (mSIESSwitch == null)
      init();

    return mObscureFirst;
  }

  public static int getObscureLast()
  {
    if (mSIESSwitch == null)
      init();

    return mObscureLast;
  }

  /**
   * Restituisce true se il Rework Dettaglio è attivo.
   * @return
   */
  public static boolean isReworkDettaglio()
  {
    if (mSIESSwitch == null)
      init();

    if (mReworkDettaglio != null && mReworkDettaglio.equals("ON"))
      return true;

    return false;
  }  
  /**
   * Restituisce true se il sottosistema RegeSies è attivo.
   * @return
   */
  public static boolean isRegeSiesOn()
  {
    if (mSIESSwitch == null)
      init();

    if (mRegeSiesOn != null && mRegeSiesOn.equals("ON"))
      return true;

    return false;
  }
  
  
  /**
   * Restituisce true se il sottosistema RegeSies è attivo.
   * @return
   */
  public static boolean isReworkStatoEsecuzioneOn()
  {
    if (mSIESSwitch == null)
      init();

    if (mReworkStatoEsecuzioneOn != null && mReworkStatoEsecuzioneOn.equals("ON"))
      return true;

    return false;
  }
  

  /**
   * Restituisce true se devono essere oscurate delle funzioni
   * @return
   */
  public static boolean isObscureFunctionOn()
  {
    if (mSIESSwitch == null)
      init();

    if (mObscureFunction != null && mObscureFunction.equals("ON"))
      return true;

    return false;
  }

  /**
   * Inizializzazione delle variabili statiche della classe
   */
  private static void init()
  {
    if (mSIESSwitch == null)
    {
      mSIESSwitch = new SIESSwitch();
      try
      {
        mRegeSiesOn = F3BProperties.getProperty("RegeSies");
      }
      catch (F3BException fEx)
      {
        //Nel caso non riesco a leggere il file o non c'e' la variabile di default
        //si metter il sottoasistema a Off.
        mRegeSiesOn = "OFF";
      }
      try
      {
        mObscureFunction = F3BProperties.getProperty("ObscureFunction");
      }
      catch (F3BException fEx)
      {
        mObscureFunction = "OFF";
      }
      try
      {
        mObscureFirst = F3BProperties.getIntProperty("ObscureFirst");
        mObscureLast = F3BProperties.getIntProperty("ObscureLast");
      }
      catch (F3BException fEx)
      {
        mObscureFirst = 0;
        mObscureLast = 0;
      }
       try
      {
        mReworkDettaglio = F3BProperties.getProperty("ReworkDettaglioProvvedimento");
      }
      catch (F3BException fEx)
      {
        //Nel caso non riesco a leggere il file o non c'e' la variabile di default
        //si metter il sottoasistema a Off.
        mReworkDettaglio = "OFF";
      }
      try
      {
        mReworkStatoEsecuzioneOn = F3BProperties.getProperty("ReworkStatoEsecuzione");
      }
      catch (F3BException fEx)
      {
        //Nel caso non riesco a leggere il file o non c'e' la variabile di default
        //si metter il sottoasistema a Off.
        mReworkStatoEsecuzioneOn = "OFF";
      }
      
      
      
    }
  }
}