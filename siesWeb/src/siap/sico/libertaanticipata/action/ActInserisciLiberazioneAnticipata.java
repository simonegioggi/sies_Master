package siap.sico.libertaanticipata.action;

/**
* <p>Title: ActInserisciLiberazioneAnticipata</p>
* <p>Description: Classe Action per l'inserimento dei periodi di liberazione anticipata</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciLiberazioneAnticipata extends ActionSiap
                                               implements ICostantiLibertaAnticipata,
                                                          ICostantiDepositoOrdinanzaPc

{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  private class PeriodoClass
  {
	    Date mDataIni = null;
	    Date mDataFine = null;
  }

  private PeriodoClass[] mPeriodi = null;
  private int mInd = 0;
  private String mTipoConcessione;    		/* modalità di scelta dei periodi concessi. (S/C) */
//
  private PeriodoClass[] mPeriodi_spe = null;
  private int mInd_spe = 0;
  private String mTipoConcessione_spe;    	/* modalità di scelta dei periodi concessi. (S/C)  L.A. SPECIALE */
//  
  private PeriodoClass[] mPeriodi_int = null;
  private int mInd_int = 0;
  private String mTipoConcessione_int;    	/* modalità di scelta dei periodi concessi. (S/C)  L.A. INTEGRAZIONE */
//  
  // Data odierna
  protected Date mOggi = DateUtils.getSysDate();
  private BigDecimal lIdFascicolo = null;
  
  private String[] lCodOggetti = new String[3];
  private int indarr = 0;

  /**
  * Azione di Inserimento del Liberazione Anticipata
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws F3BException
  */
 
  public String processRequest()	throws Exception
  {
	  	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  	siesLogger.debug("-------------> - Inizio  processRequest() di ActInserisciLiberazioneAnticipata - " );
	    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
	    lIdFascicolo = lFascMod.getIdFascicoloSiep();
// Controllo Posizione Giuridica	    
	      IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
	      PosizioneGiuridicaModel lPosizione  = lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

	      if (lPosizione == null)
	        throw new SIEPException(SIEPException.USER_MESSAGE, "Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");
//	
		 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 siesLogger.debug("-------------> - INSERIMENTO L.A. NORMALE " );
	 
//-------------------------------------------------------------
//>>>>>>>>>>>>>>>>> 	L.A. NORMALE		<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//	 

    LicenzaPeriodiLibAnticipataModel[] lLicenze = null;

    String[] lChecks = null;

    int numCheck = 0;
    int numCheckConcessi = 0;

    boolean lConcessi = false;
    boolean lPeriodo = false;
    boolean lRigettati = false;
    boolean lInammissibili = false;
    boolean lNLP = false;
    
    String lCodEsistoTenore = "0020";
    String lCodOggettoTenore = "";
    
//	Prendo subito gli eventuali giorni di L.A.

	   int SommatotLA=0;

	    if(getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA) != null)
		{	
			if(getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue() > 0 ) 
			{
			       SommatotLA=getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue();
			}
		}	
		if(getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA) != null)
		{	
				if(getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA).intValue() > 0 )
				{
					SommatotLA=getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA).intValue();
				}
		}

	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("--> Totale Giorni di L.A. Normale = " + SommatotLA);	
		
    mTipoConcessione = this.getRequestStringParameter(CAMPO_RADIO_TIPO_CONCESSIONE);		// S semestri - C periodo Unico 

    // Periodi Per Semestre
    if (isRequestChecked(CAMPO_CHECK_CONCESSI))
    {
    	// Numero chek periodi da 45 gg ( se CAMPO_RADIO_TIPO_CONCESSIONE = S)
	      lChecks = getRequestStringParameters(CAMPO_CHECK_CONCESSI);
	      numCheck += lChecks.length;
	      lConcessi = true;
	      numCheckConcessi = numCheck;
	  //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  //  siesLogger.debug("--> LA Normale - CHECK_CONCESSI semestri - numCheckConcessi = " + numCheckConcessi);
    }
    
    // Unico Periodo
    if (isRequestChecked(CAMPO_CHECK_PERIODO))
    {
    	// check per inserimento periodo in periodo Unico ( se CAMPO_RADIO_TIPO_CONCESSIONE = C)
	      numCheck++;
	      lPeriodo = true;
    }
    
    // Periodi Rigettati
    if (isRequestChecked(CAMPO_CHECK_RIGETTATI))
    {
	      numCheck++;
	      lRigettati = true;
    }
    
    // Inammissibili
    if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI))
    {
	      numCheck++;
	      lInammissibili = true;
    }
    
    // NLP/NDP
    if (isRequestChecked(CAMPO_CHECK_NLP))
    {
	      numCheck++;
	      lNLP = true;
    }

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Numero chek Totali di L.A. Normale = " + numCheck);
   //==========================================================================
   // Se sono presenti periodi concessi/non concessi, li recupero dalla form e
   // li inserisco.
   // n.b. per come è strutturata la form di inserimento è possibile inserire
   //      contemporaneamente sia periodi concessi che non concessi (rigettati,
   //      inammissibili, NLP). Per i concessi è possibile specificare solo una 
   //      tipologia, o per semestri o periodo unico 
   //==========================================================================
   if (numCheck > 0)
   {
	   	  lCodOggettoTenore = "2130";
	   	  
	      lLicenze = new LicenzaPeriodiLibAnticipataModel[numCheck];
	      int i = 0;
	
	      // Recupero tutti i periodi specificati nella form (data inizio - data fine)
	      mPeriodi = leggiDate();
	
	// Se sono presenti periodi concessi per semestre creo un model per ogni 
	      if (lConcessi)		// semestre
	      {
		        while(i < numCheckConcessi )
		        {
			          lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
			          lLicenze[i].setLicenza(generaLicenza("C"));
			          lLicenze[i].getLicenza().setFlagScorta(mTipoConcessione);		// S per i semestri
			          setPeriodiInLicenze(lLicenze[i]);
			          i++;
		        }
	      }
	
	      // Se sono presenti periodi concessi per periodo unico creo un model con i periodi 
	      if (lPeriodo)
	      {
		        lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
		        lLicenze[i].setLicenza(generaLicenza("C"));
		        
		        if(SommatotLA > 0)
					lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLA));
		        
		        lLicenze[i].getLicenza().setFlagScorta(mTipoConcessione);		// C per periodo unico
		        setPeriodiInLicenze(lLicenze[i]);
		        i++;
	      }
	
	      if (lRigettati)
	      {
		        lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
		        lLicenze[i].setLicenza(generaLicenza("R"));
		        setPeriodiInLicenze(lLicenze[i]);
		        i++;
	      }
	      
	      if (lInammissibili)
	      {
		        lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
		        lLicenze[i].setLicenza(generaLicenza("I"));
		        setPeriodiInLicenze(lLicenze[i]);
		        i++;
	      }
	      
	      if (lNLP)
	      {
		        lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
		        lLicenze[i].setLicenza(generaLicenza("N"));
		        setPeriodiInLicenze(lLicenze[i]);
		        i++;
	      }
	
	      // Gestione dell'Esito Tenore:
	      // viene considerato prevalente (e quindi inserito)
	      // l'esito secondo questa scala di priorità
	      // " concede > rigetta > inammissibile > nlp "

	      if( lNLP )
	      {
	        lCodEsistoTenore = "0004"; // Dichiara N.D.P./ N.L.P. [RV_DOMAIN='ESITO_PROVVEDIMENTO']
	      }
	      
	      if( lInammissibili )
	      {
	        lCodEsistoTenore = "0021"; // Dichiara Inammissibile [RV_DOMAIN='ESITO_PROVVEDIMENTO']
	      }
	      
	      if( lRigettati )
	      {
	        lCodEsistoTenore = "0022"; // Rigetta [RV_DOMAIN='ESITO_PROVVEDIMENTO']
	      }
	      
	      if( lConcessi || lPeriodo )
	      {
	        lCodEsistoTenore = "0020"; // Concede [RV_DOMAIN='ESITO_PROVVEDIMENTO']
	      }
	
    }	// chiude  if (numCheck > 0)

   // Se presente Esito L.A. carico ilCodiceOggetto 
	  lCodOggetti[indarr] = lCodOggettoTenore;
	  indarr ++;
    	
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug(" L.A. Normale - lCodOggettoTenore  = " + lCodOggettoTenore);	  
   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug(" L.A. Normale - lCodEsistoTenore  = " + lCodEsistoTenore);
   
//
//	FINE Gestione L.A. NORMALE
// 
   
	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.debug("-------------> - INSERIMENTO L.A. SPECIALE " );
	 
//-------------------------------------------------------------
//>>>>>>>>>>>>>>>>> 	L.A. SPECIALE		<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//	 

  LicenzaPeriodiLibAnticipataModel[] lLicenze_spe = null;

  String[] lChecks_spe = null;

  int numCheck_spe = 0;
  int numCheckConcessi_spe = 0;

  boolean lConcessi_spe = false;
  boolean lPeriodo_spe = false;
  boolean lRigettati_spe = false;
  boolean lInammissibili_spe = false;
  boolean lNLP_spe = false;
  
  String lCodEsistoTenore_spe = "0020";
  String lCodOggettoTenore_spe ="";
  
//	Prendo subito gli eventuali giorni di L.A. Speciale
	   int SommatotLS=0;

	    if(getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE) != null)
		{	
			if(getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE).intValue() > 0 ) 
			{
			       SommatotLS=getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE).intValue();
			}
		}	
		if(getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE) != null)
		{	
				if(getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE).intValue() > 0 )
				{
					SommatotLS=getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE).intValue();
				}
		}

	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("--> Totale Giorni di L.A. Speciale = " + SommatotLS);	
  mTipoConcessione_spe = this.getRequestStringParameter(CAMPO_RADIO_TIPO_CONCESSIONE_SPE);		// S semestri - C periodo Unico 

  // Periodi di LA Speciale Per Semestre
  if (isRequestChecked(CAMPO_CHECK_CONCESSI_SPE))
  {
  	// Numero chek periodi da 75 gg ( se CAMPO_RADIO_TIPO_CONCESSIONE_SPE = S)
	      lChecks_spe = getRequestStringParameters(CAMPO_CHECK_CONCESSI_SPE);
	      numCheck_spe += lChecks_spe.length;
	      lConcessi_spe = true;
	      numCheckConcessi_spe = numCheck_spe;
	  //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  //  siesLogger.debug("--> LA SPECIALE - CHECK_CONCESSI semestri - numCheckConcessi_spe = " + numCheckConcessi_spe);
  }
  
  // Unico Periodo di LA Speciale
  if (isRequestChecked(CAMPO_CHECK_PERIODO_SPE))
  {
  	// check per inserimento periodo in periodo Unico ( se CAMPO_RADIO_TIPO_CONCESSIONE_SPE = C)
	      numCheck_spe++;
	      lPeriodo_spe = true;
  }
  
  // Periodi Rigettati di LA Speciale
  if (isRequestChecked(CAMPO_CHECK_RIGETTATI_SPE))
  {
	      numCheck_spe++;
	      lRigettati_spe = true;
  }
  
  // Periodi Inammissibili di LA Speciale
  if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI_SPE))
  {
	      numCheck_spe++;
	      lInammissibili_spe = true;
  }
  
  // Periodi NLP/NDP di LA Speciale
  if (isRequestChecked(CAMPO_CHECK_NLP_SPE))
  {
	      numCheck_spe++;
	      lNLP_spe = true;
  }

  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("L.A. Speciale - Numero chek = " + numCheck_spe);
 //==========================================================================
 // Se sono presenti periodi concessi/non concessi, li recupero dalla form e
 // li inserisco.
 // n.b. per come è strutturata la form di inserimento è possibile inserire
 //      contemporaneamente sia periodi concessi che non concessi (rigettati,
 //      inammissibili, NLP). Per i concessi è possibile specificare solo una 
 //      tipologia, o per semestri o periodo unico 
 //==========================================================================
 if (numCheck_spe > 0)
 {
	 	lCodOggettoTenore_spe = "2131";
	      lLicenze_spe = new LicenzaPeriodiLibAnticipataModel[numCheck_spe];
	      int i = 0;
	
	      // Recupero tutti i periodi di L.A.Speciale specificati nella form (data inizio - data fine)
	      mPeriodi_spe = leggiDate_spe();
	
	      // Se sono presenti periodi di LA Spec concessi per semestre, creo un model per ogni
	      if (lConcessi_spe)		      // Semestre
	      {
		        while(i < numCheckConcessi_spe )
		        {
		        	lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
		        	lLicenze_spe[i].setLicenza(generaLicenza_spe("C"));
		        	lLicenze_spe[i].getLicenza().setFlagScorta(mTipoConcessione_spe);		// S per i semestri
			        setPeriodiInLicenze_spe(lLicenze_spe[i]);
			        i++;
		        }
	      }
	
	      // Se sono presenti periodi di LA Spec concessi per periodo unico creo un model con i periodi 
	      if (lPeriodo_spe)
	      {
	    	  lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
	    	  lLicenze_spe[i].setLicenza(generaLicenza_spe("C"));
		        
		      //  lLicenze[i].getLicenza().setNumeroGiorni(this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE));
		        if(SommatotLS > 0)
		        	lLicenze_spe[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLS));
		        
		        lLicenze_spe[i].getLicenza().setFlagScorta(mTipoConcessione_spe);		// C per periodo unico
		        setPeriodiInLicenze_spe(lLicenze_spe[i]);
		        i++;
	      }
	
	      if (lRigettati_spe)
	      {
	    	  lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
	    	  lLicenze_spe[i].setLicenza(generaLicenza_spe("R"));
		      setPeriodiInLicenze_spe(lLicenze_spe[i]);
		      i++;
	      }
	      
	      if (lInammissibili_spe)
	      {
	    	  lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
	    	  lLicenze_spe[i].setLicenza(generaLicenza_spe("I"));
		      setPeriodiInLicenze_spe(lLicenze_spe[i]);
		      i++;
	      }
	      
	      if (lNLP_spe)
	      {
	    	  lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
	    	  lLicenze_spe[i].setLicenza(generaLicenza_spe("N"));
		      setPeriodiInLicenze_spe(lLicenze_spe[i]);
		      i++;
	      }
	
	      // Gestione dell'Esito Tenore:
	      // viene considerato prevalente (e quindi inserito)
	      // l'esito secondo questa scala di priorità
	      // " concede > rigetta > inammissibile > nlp "

	      if( lNLP_spe )
	      {
	        lCodEsistoTenore_spe = "0004"; // Dichiara N.D.P./ N.L.P. [RV_DOMAIN='ESITO_PROVVEDIMENTO']
	      }
	      
	      if( lInammissibili_spe )
	      {
	        lCodEsistoTenore_spe = "0021"; // Dichiara Inammissibile [RV_DOMAIN='ESITO_PROVVEDIMENTO']
	      }
	      
	      if( lRigettati_spe )
	      {
	        lCodEsistoTenore_spe = "0022"; // Rigetta [RV_DOMAIN='ESITO_PROVVEDIMENTO']
	      }
	      
	      if( lConcessi_spe || lPeriodo_spe )
	      {
	        lCodEsistoTenore_spe = "0020"; // Concede [RV_DOMAIN='ESITO_PROVVEDIMENTO']
	      }
	
  }	// chiude  if (numCheck_spe > 0)
 
 	// Se presente Esito L.A. carico ilCodiceOggetto 
	  lCodOggetti[indarr] = lCodOggettoTenore_spe;
	  indarr ++;
 
  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug(" L.A. Speciale - lCodOggettoTenore_spe  = " + lCodOggettoTenore_spe);
 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 siesLogger.debug(" L.A. Speciale - lCodEsistoTenore  = " + lCodEsistoTenore_spe);
 
//
//	FINE Gestione L.A. SPECIALE
// 

 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 siesLogger.debug("-------------> - INSERIMENTO L.A. INTEGRAZIONE " );
 
//-------------------------------------------------------------
//>>>>>>>>>>>>>>>>> 	L.A. INTEGRAZIONE		<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// 

LicenzaPeriodiLibAnticipataModel[] lLicenze_int = null;

String[] lChecks_int = null;

int numCheck_int = 0;
int numCheckConcessi_int = 0;

boolean lConcessi_int = false;
boolean lPeriodo_int = false;
boolean lRigettati_int = false;
boolean lInammissibili_int = false;
boolean lNLP_int = false;

String lCodEsistoTenore_int = "0020";
String lCodOggettoTenore_int ="";

	//Prendo subito gli eventuali giorni di L.A. INTEGRAZIONE
   int SommatotLI=0;

    if(getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT) != null)
	{	
		if(getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT).intValue() > 0 ) 
		{
		       SommatotLI=getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT).intValue();
		}
	}	
	if(getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT) != null)
	{	
			if(getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT).intValue() > 0 )
			{
				SommatotLI=getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT).intValue();
			}
	}

  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("--> Totale Giorni di L.A. Integrazione = " + SommatotLI);	
mTipoConcessione_int = this.getRequestStringParameter(CAMPO_RADIO_TIPO_CONCESSIONE_INT);		// S semestri - C periodo Unico 

// Periodi Per Semestre
if (isRequestChecked(CAMPO_CHECK_CONCESSI_INT))
{
	// Numero chek periodi da 30 gg ( se CAMPO_RADIO_TIPO_CONCESSIONE_INT = S)
      lChecks_int = getRequestStringParameters(CAMPO_CHECK_CONCESSI_INT);
      numCheck_int += lChecks_int.length;
      lConcessi_int = true;
      numCheckConcessi_int = numCheck_int;
//  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//  siesLogger.debug("--> LA INT - CHECK_CONCESSI semestri - numCheckConcessi_int = " + numCheckConcessi_int);
}

// Unico Periodo
if (isRequestChecked(CAMPO_CHECK_PERIODO_INT))
{
	// check per inserimento periodo in periodo Unico ( se CAMPO_RADIO_TIPO_CONCESSIONE_INT = C)
      numCheck_int ++;
      lPeriodo_int = true;
}

// Periodi Rigettati
if (isRequestChecked(CAMPO_CHECK_RIGETTATI_INT))
{
      numCheck_int ++;
      lRigettati_int = true;
}

// Inammissibili
if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI_INT))
{
      numCheck_int ++;
      lInammissibili_int = true;
}

// NLP/NDP
if (isRequestChecked(CAMPO_CHECK_NLP_INT))
{
      numCheck_int ++;
      lNLP_int = true;
}

// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
siesLogger.debug(" L.A. Integraz - Numero chek = " + numCheck_int);
//==========================================================================
// Se sono presenti periodi concessi/non concessi, li recupero dalla form e
// li inserisco.
// n.b. per come è strutturata la form di inserimento è possibile inserire
//      contemporaneamente sia periodi concessi che non concessi (rigettati,
//      inammissibili, NLP). Per i concessi è possibile specificare solo una 
//      tipologia, o per semestri o periodo unico 
//==========================================================================
if (numCheck_int > 0)
{
		lCodOggettoTenore_int = "2132";
      lLicenze_int = new LicenzaPeriodiLibAnticipataModel[numCheck_int];
      int i = 0;

      // Recupero tutti i periodi specificati nella form (data inizio - data fine)
      mPeriodi_int = leggiDate_int();

      // Se sono presenti periodi concessi per semestre creo un model per ogni
      if (lConcessi_int)	      // Semestre
      {
	        while(i < numCheckConcessi_int )
	        {
		          lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
		          lLicenze_int[i].setLicenza(generaLicenza_int("C"));
		          lLicenze_int[i].getLicenza().setFlagScorta(mTipoConcessione);		// S per i semestri
		          setPeriodiInLicenze_int(lLicenze_int[i]);
		          i++;
	        }
      }

      // Se sono presenti periodi concessi per periodo unico creo un model con i periodi 
      if (lPeriodo_int)
      {
	        lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
	        lLicenze_int[i].setLicenza(generaLicenza_int("C"));
	        
	        if(SommatotLI > 0)
	        	lLicenze_int[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLI));
	        
	        lLicenze_int[i].getLicenza().setFlagScorta(mTipoConcessione_int);		// C per periodo unico
	        setPeriodiInLicenze_int(lLicenze_int[i]);
	        i++;
      }

      if (lRigettati_int)
      {
	        lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
	        lLicenze_int[i].setLicenza(generaLicenza_int("R"));
	        setPeriodiInLicenze_int(lLicenze_int[i]);
	        i++;
      }
      
      if (lInammissibili_int)
      {
	        lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
	        lLicenze_int[i].setLicenza(generaLicenza_int("I"));
	        setPeriodiInLicenze_int(lLicenze_int[i]);
	        i++;
      }
      
      if (lNLP_int)
      {
	        lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
	        lLicenze_int[i].setLicenza(generaLicenza_int("N"));
	        setPeriodiInLicenze_int(lLicenze_int[i]);
	        i++;
      }

      // Gestione dell'Esito Tenore:
      // viene considerato prevalente (e quindi inserito)
      // l'esito secondo questa scala di priorità
      // " concede > rigetta > inammissibile > nlp "

      if( lNLP_int )
      {
        lCodEsistoTenore_int = "0004"; // Dichiara N.D.P./ N.L.P. [RV_DOMAIN='ESITO_PROVVEDIMENTO']
      }
      
      if( lInammissibili_int )
      {
        lCodEsistoTenore_int = "0021"; // Dichiara Inammissibile [RV_DOMAIN='ESITO_PROVVEDIMENTO']
      }
      
      if( lRigettati_int )
      {
        lCodEsistoTenore_int = "0022"; // Rigetta [RV_DOMAIN='ESITO_PROVVEDIMENTO']
      }
      
      if( lConcessi_int || lPeriodo_int )
      {
        lCodEsistoTenore_int = "0020"; // Concede [RV_DOMAIN='ESITO_PROVVEDIMENTO']
      }

}	// chiude  if (numCheck_int > 0)

// Se presente Esito L.A. carico ilCodiceOggetto 
	  lCodOggetti[indarr] = lCodOggettoTenore_int;

 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 siesLogger.debug(" L.A. Integrazione - lCodOggettoTenore_int  = " + lCodOggettoTenore_int);
// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
siesLogger.debug(" L.A. Integrazione - lCodEsistoTenore  = " + lCodEsistoTenore_int);

//
//FINE Gestione L.A. INTEGRAZIONE
//   
   
	/*
   aOrdEveTenGP.getOrdinanza().setNumGiorniLibanticipata(getRequestBigDecimalParameter(super.CAMPO_NUM_GIORNI_LIBANTICIPATA));
   // inserimento
   IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
   lModRet = (IDepOrdCtrl.ExInserisciOrdinanzaLibAnt(aOrdEveTenGP,lLicenze));
*/

//---------------------------------------------------------------------------------------------------
//SOMMA TOTALE GIORNI 	   
//	   
//10-03-2014 ---> Nuova ordinanza L.A. dopo DECRETO 146/2013					//
//				Da oggi in poi in "aOrdEveTenGP" va messo il numero di giorni totali di LA ( LA + LS + LI)
//
//		SommatotLA =  eventuale Num. gg. Totali di L.A. NORMALE
//	 	SommatotLS =  eventuale Num. gg. Totali di L.A. SPECIALE
//	 	SommatotLI =  eventuale Num. gg. Totali di L.A. INTEGRAZIONE	   
	   
	   int Sommatot=0;
	   Sommatot = SommatotLA + SommatotLS + SommatotLI;

	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(" Somma totale Giorni L.A. (L.A.+L.S.+L.I.)  = " + Sommatot);

	   // Preparo i dati per Evento ORDINANZA Tenore
   OrdinanzaEventoTenoriModel lOrdEveTenMod = new OrdinanzaEventoTenoriModel();
   lOrdEveTenMod.setEvento(generaEvento(lPosizione));
   lOrdEveTenMod.setOrdinanza(generaOrdinanza(Sommatot));
   lOrdEveTenMod.setTenori(generaTenori(lCodEsistoTenore, lCodEsistoTenore_spe, lCodEsistoTenore_int));
   
   // Nel caso di solo Rigetto, Inammissibilità o NLP l'ordinanza iscritta 
   // SIEP viene direttamente validata in quanto non è possibile emettere 
   // ulteriori provvedimenti.
   // n.b. come cas isLibero() (vedi generaEvento())
   if ( ( lCodEsistoTenore.equals("0004") || lCodEsistoTenore.equals("0021") || lCodEsistoTenore.equals("0022") ) &&
		( lCodEsistoTenore_spe.equals("0004") || lCodEsistoTenore_spe.equals("0021") || lCodEsistoTenore_spe.equals("0022") ) &&
		( lCodEsistoTenore_int.equals("0004") || lCodEsistoTenore_int.equals("0021") || lCodEsistoTenore_int.equals("0022") ) 
      )
   {
 	  lOrdEveTenMod.getEvento().setFlagDocumentoRegistrato("S");
   }   

   ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
   
 //  lLicenze = lCtrlLib.ExInserisciLicenzeLibanticipata(lLicenze, lOrdEveTenMod);
   EventoModel lRetEve = lCtrlLib.ExInserisciLicenzeLibanticipataSIEP(lOrdEveTenMod, lLicenze, lLicenze_spe, lLicenze_int );

    //==========================================================================
    // Recupero l'id evento da passare alla Action di caricamente del dettaglio
    //==========================================================================
    BigDecimal lIdEvento = null;

    if(lRetEve == null )
    	throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Periodo Trovato dopo Inserimento L.A. Impossibile procedere.");
    else 
   		lIdEvento = lRetEve.getIdEvento();
    
    if(lIdEvento == null)
    	throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Periodo Trovato dopo Inserimento L.A. Impossibile procedere.");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(" Id Evento Inserito  = " + lIdEvento);
    //Prepara la pagina di destinazione
    String lPage = "";

    //setRequestAttribute("LicenzePeriodi", new Vector(Arrays.asList(lLicenze)));
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.libertaanticipata.action.ActDettaglioLiberazioneAnticipata&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lIdEvento.toString();

    return lPage;
    
  }	// CHIUDE processRequest() 
  
// 	-	-	-	-	 END processRequest()	-	-	-	 
  
//>>>>>>>>>>>>>>>>>  METODI per gestione L.A. NORMALE		<<<<<<<<<<<<<<<<<<<<<<<<<<<<

  /*****************************************************************************
   * Lettura di tutti i periodi di date valorizzati nella form di input.
   * Attenzione!! Il metodo non è in grado di capire a quale DIV appartengano
   * i periodi (concessi/rigettati), mischia tutto. 
   * @return
   * @throws F3BException
   ************************************************************************** */
  private PeriodoClass[] leggiDate()
    throws F3BException
  {
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.debug(" L.A. Normale - leggiDate Inizio");
	    PeriodoClass[] lPeriodi = null;
	    Date[] lDateInizio = null;
	    Date[] lDateFine = null;
	
	    lDateInizio = getRequestDateParameters(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO);
	    lDateFine = getRequestDateParameters(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE);
	    
	    // ???
	    int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
	
	    lPeriodi = new PeriodoClass[lNumDate];
	
	    for (int i = 0; i <lDateFine.length; i++)
	    {
	      lPeriodi[i] = new PeriodoClass();
	      lPeriodi[i].mDataIni = lDateInizio[i];
	      lPeriodi[i].mDataFine = lDateFine[i];
	    }
	
	    return lPeriodi;
   }

  /**
   * Carica un Model LicenzaLibAnticipataModel
   *
   * @param aFlagConcesso
   * @return
   * @throws F3BException
   */
   private LicenzaLibAnticipataModel generaLicenza(String aFlagConcesso)
     throws F3BException
   {
	   //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   //siesLogger.debug(" L.A. Normale - generaLicenza  Inizio");
	     LicenzaLibAnticipataModel lLicenza;
	     lLicenza = new LicenzaLibAnticipataModel();
	
	     lLicenza.setCodTipoLicenza("LA");
	     lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
	     lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	     lLicenza.setDataInserimento(mOggi);
	     lLicenza.setFlagConcesso(aFlagConcesso);
	     
	     if(aFlagConcesso.compareTo("C") == 0)
	    	 lLicenza.setNumeroGiorni(new BigDecimal(45));
	     
	     lLicenza.setFasSieIdFascicoloSiep(lIdFascicolo);
	     lLicenza.setDescrStatoPermesso("LA");	 // L.A. NORMALE  

/*
     if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
         lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
     else
*/

	     lLicenza.setAnnoSius        (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS));
	     lLicenza.setNumeroSius      (getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS));
	     lLicenza.setAnnoOrdinanza   (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_ORDINANZA));
	     lLicenza.setNumeroOrdinanza (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORDINANZA));
	
	     String lCodUDS = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
	     /* ** */lLicenza.setDescrUfficioEmittente(lCodUDS);
	     String lCodComune = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE);
	     if( lCodComune != null && !lCodComune.equals("") && !lCodComune.equals("-"))
	       lCodUDS = getCodUfficioByCodTipoUfficioDescrComune(lCodUDS, lCodComune);
	
	     lLicenza.setCodUfficioEmittente(lCodUDS);
	
	     ComuneModel lComune = getCodComuneByDescr(getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE));
	     lLicenza.setCodLuogoEmittente(lComune.getCodComune());
	
	     lLicenza.setDataEmissioneOrdinanza(getRequestDateParameter( ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA,
	                                                                 ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA,
	                                                                 ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA)
	                                                                );

	   //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   //siesLogger.debug(" L.A. Normale - generaLicenza  Fine");

	     return lLicenza;
   }

   /**
    * Carica sull'opportuna Licenza i propri periodi
    * @param aLicenza
    * @throws F3BException
    */
   private void setPeriodiInLicenze(LicenzaPeriodiLibAnticipataModel aLicenza)
     throws F3BException
   {
	//  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	//  siesLogger.debug(" L.A. Normale - setPeriodiInLicenze - Inizio " );
	     // Conteggio dei periodi valorizzati
	     int num = 0;
	     
	     for (int j = 0, k = mInd; j < 10;  j++, k++)
	    	 if ( (mPeriodi[k].mDataIni != null) && (mPeriodi[k].mDataFine != null))
	    		 num++;
	     
	     if (num > 0)
	    	 aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);
	
	     // Generazione dei Periodi di Libertà Anticipa
	     for (int k = 0, j = 0; k < 10; k++,mInd++, j++)
	     {
			 if ( (mPeriodi[mInd].mDataIni != null) && (mPeriodi[mInd].mDataFine != null))
			 {
			     aLicenza.getPeriodi()[j] = generaPeriodoLibAnticipa(aLicenza.getLicenza().getFlagConcesso());
			 }
			// else
			//	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//	 siesLogger.debug("setPeriodiInLicenze - data nulla ");
	     }
	     
	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 //  siesLogger.debug(" setPeriodiInLicenze - fine " );
   }

   /****************************************************************************
    *
    * @param aFlagConcesso
    * @return
    * @throws F3BException
    ************************************************************************* */
   private PeriodoLibAnticipataModel generaPeriodoLibAnticipa(String aFlagConcesso)
     throws F3BException
   {
	//  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	//  siesLogger.debug(" L.A. Normale - generaPeriodoLibAnticipa - Inizio " );
     PeriodoLibAnticipataModel lPeriodoLib = null;

     lPeriodoLib = new PeriodoLibAnticipataModel();
     lPeriodoLib.setDataInizio(mPeriodi[mInd].mDataIni);
     lPeriodoLib.setDataFine(mPeriodi[mInd].mDataFine);
     lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
     lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
     lPeriodoLib.setDataInserimento(mOggi);
     lPeriodoLib.setFlagConcesso(aFlagConcesso);

 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 //  siesLogger.debug(" L.A. Normale - generaPeriodoLibAnticipa - Fine " );

     return lPeriodoLib;
   }

 //>>>>>>>>>>>>>>>>>  METODI per gestione L.A. SPECIALE		<<<<<<<<<<<<<<<<<<<<<<<<<<<<
   
   /*****************************************************************************
    * Lettura di tutti i periodi di date valorizzati nella form di input.
    * Attenzione!! Il metodo non è in grado di capire a quale DIV appartengano
    * i periodi (concessi/rigettati), mischia tutto. 
    * @return
    * @throws F3BException
    ************************************************************************** */
   private PeriodoClass[] leggiDate_spe()	throws F3BException
   {
     
	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 //  siesLogger.debug(" leggiDate_spe  - Inizio " );
	   
	     PeriodoClass[] lPeriodi_spe = null;
	     Date[] lDateInizio = null;
	     Date[] lDateFine = null;
	
	     lDateInizio = getRequestDateParameters(CAMPO_ANNO_DATA_INIZIO_SPE, CAMPO_MESE_DATA_INIZIO_SPE, CAMPO_GIORNO_DATA_INIZIO_SPE);
	     lDateFine = getRequestDateParameters(CAMPO_ANNO_DATA_FINE_SPE, CAMPO_MESE_DATA_FINE_SPE, CAMPO_GIORNO_DATA_FINE_SPE);
	     
	     // ???
	     int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
	
	     lPeriodi_spe = new PeriodoClass[lNumDate];
	
	     for (int i = 0; i <lDateFine.length; i++)
	     {
	    	 lPeriodi_spe[i] = new PeriodoClass();
	    	 lPeriodi_spe[i].mDataIni = lDateInizio[i];
	    	 lPeriodi_spe[i].mDataFine = lDateFine[i];
	     }
	
	     return lPeriodi_spe;
	     
    }	// END private PeriodoClass[] leggiDate_spe()

   /**
    * Carica un Model LicenzaLibAnticipataModel
    *
    * @param aFlagConcesso
    * @return
    * @throws F3BException
    */
    private LicenzaLibAnticipataModel generaLicenza_spe(String aFlagConcesso)      throws F3BException
    {
    	
    	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	 //  siesLogger.debug(" generaLicenza_spe  - Inizio " );
    	
      LicenzaLibAnticipataModel lLicenza;
      lLicenza = new LicenzaLibAnticipataModel();

      lLicenza.setCodTipoLicenza("LA");
      lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
      lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
      lLicenza.setDataInserimento(mOggi);
      lLicenza.setFlagConcesso(aFlagConcesso);
      
      if(aFlagConcesso.compareTo("C") == 0)
    	  lLicenza.setNumeroGiorni(new BigDecimal(75));
      
      lLicenza.setFasSieIdFascicoloSiep(lIdFascicolo);
      lLicenza.setDescrStatoPermesso("LS");	  // L.A. SPECIALE 
 /*
      if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
          lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
      else
 */

      lLicenza.setAnnoSius        (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS));
      lLicenza.setNumeroSius      (getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS));
      lLicenza.setAnnoOrdinanza   (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_ORDINANZA));
      lLicenza.setNumeroOrdinanza (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORDINANZA));

      String lCodUDS = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
      /* ** */lLicenza.setDescrUfficioEmittente(lCodUDS);
      String lCodComune = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE);
     
      if( lCodComune != null && !lCodComune.equals("") && !lCodComune.equals("-"))
        lCodUDS = getCodUfficioByCodTipoUfficioDescrComune(lCodUDS, lCodComune);

      lLicenza.setCodUfficioEmittente(lCodUDS);

      ComuneModel lComune = getCodComuneByDescr(getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE));
      lLicenza.setCodLuogoEmittente(lComune.getCodComune());

      lLicenza.setDataEmissioneOrdinanza(getRequestDateParameter( ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA,
                                                                  ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA,
                                                                  ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA)
                                                                 );


      return lLicenza;
      
    } // CHIUDE private LicenzaLibAnticipataModel generaLicenza_spe...

    /**
     * Carica sull'opportuna Licenza i propri periodi
     * @param aLicenza
     * @throws F3BException
     */
    private void setPeriodiInLicenze_spe(LicenzaPeriodiLibAnticipataModel aLicenza)	      throws F3BException
    {
   	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   	 //  siesLogger.debug(" setPeriodiInLicenze_spe  - Inizio " );
    	
	      // Conteggio dei periodi valorizzati
	      int num = 0;
	      
	      for (int j = 0, k = mInd_spe; j < 10;  j++, k++)
	    	  if ( (mPeriodi_spe[k].mDataIni != null) && (mPeriodi_spe[k].mDataFine != null))
	    		  num++;
	      
	      if (num > 0)
	    	  aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);
	
	      // Generazione dei Periodi di Libertà Anticipa
	      for (int k = 0, j = 0; k < 10; k++,mInd_spe++, j++)
	      {
		        if ( (mPeriodi_spe[mInd_spe].mDataIni != null) && (mPeriodi_spe[mInd_spe].mDataFine != null))
		        {
		          aLicenza.getPeriodi()[j] = generaPeriodoLibAnticipa_spe(aLicenza.getLicenza().getFlagConcesso());
		        }
		     //   else
		     //     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		     //     siesLogger.debug("setPeriodiInLicenze_spe - data nulla ");
	      }

    } // CHIUDE private void setPeriodiInLicenze_spe ... 

    /****************************************************************************
     *
     * @param aFlagConcesso
     * @return
     * @throws F3BException
     ************************************************************************* */
    private PeriodoLibAnticipataModel generaPeriodoLibAnticipa_spe(String aFlagConcesso)      throws F3BException
    {
      	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      	 //  siesLogger.debug(" generaPeriodoLibAnticipa_spe  - Inizio " );  	
      
	    	PeriodoLibAnticipataModel lPeriodoLib = null;
	      lPeriodoLib = new PeriodoLibAnticipataModel();
	      
	      lPeriodoLib.setDataInizio(mPeriodi_spe[mInd_spe].mDataIni);
	      lPeriodoLib.setDataFine(mPeriodi_spe[mInd_spe].mDataFine);
	      lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
	      lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	      lPeriodoLib.setDataInserimento(mOggi);
	      lPeriodoLib.setFlagConcesso(aFlagConcesso);

	      return lPeriodoLib;
    }

  //>>>>>>>>>>>>>>>>>  METODI per gestione L.A. INTEGRAZIONE		<<<<<<<<<<<<<<<<<<<<<<<<<<<<   
    
    /*****************************************************************************
     * Lettura di tutti i periodi di date valorizzati nella form di input.
     * Attenzione!! Il metodo non è in grado di capire a quale DIV appartengano
     * i periodi (concessi/rigettati), mischia tutto. 
     * @return
     * @throws F3BException
     ************************************************************************** */
    private PeriodoClass[] leggiDate_int()	throws F3BException
    {
      
 	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 	 //  siesLogger.debug(" leggiDate_int  - Inizio " );
 	   
 	     PeriodoClass[] lPeriodi_int = null;
 	     Date[] lDateInizio = null;
 	     Date[] lDateFine = null;
 	
 	     lDateInizio = getRequestDateParameters(CAMPO_ANNO_DATA_INIZIO_INT, CAMPO_MESE_DATA_INIZIO_INT, CAMPO_GIORNO_DATA_INIZIO_INT);
 	     lDateFine = getRequestDateParameters(CAMPO_ANNO_DATA_FINE_INT, CAMPO_MESE_DATA_FINE_INT, CAMPO_GIORNO_DATA_FINE_INT);
 	     
 	     // ???
 	     int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
 	
 	     lPeriodi_int = new PeriodoClass[lNumDate];
 	
 	     for (int i = 0; i <lDateFine.length; i++)
 	     {
 	    	lPeriodi_int[i] = new PeriodoClass();
 	    	lPeriodi_int[i].mDataIni = lDateInizio[i];
 	    	lPeriodi_int[i].mDataFine = lDateFine[i];
 	     }
 	
 	     return lPeriodi_int;
 	     
     }	// END private PeriodoClass[] leggiDate_int()

    /**
     * Carica un Model LicenzaLibAnticipataModel
     *
     * @param aFlagConcesso
     * @return
     * @throws F3BException
     */
     private LicenzaLibAnticipataModel generaLicenza_int(String aFlagConcesso)      throws F3BException
     {
     	
     	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     	 //  siesLogger.debug(" generaLicenza_int  - Inizio " );
     	
       LicenzaLibAnticipataModel lLicenza;
       lLicenza = new LicenzaLibAnticipataModel();

       lLicenza.setCodTipoLicenza("LA");
       lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
       lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
       lLicenza.setDataInserimento(mOggi);
       lLicenza.setFlagConcesso(aFlagConcesso);
       
       if(aFlagConcesso.compareTo("C") == 0)
    	   lLicenza.setNumeroGiorni(new BigDecimal(30));
       
       lLicenza.setFasSieIdFascicoloSiep(lIdFascicolo);
       lLicenza.setDescrStatoPermesso("LI");	   // L.A. INTEGRAZIONE 
  /*
       if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
           lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
       else
  */

       lLicenza.setAnnoSius        (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS));
       lLicenza.setNumeroSius      (getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS));
       lLicenza.setAnnoOrdinanza   (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_ORDINANZA));
       lLicenza.setNumeroOrdinanza (getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORDINANZA));

       String lCodUDS = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
       /* ** */lLicenza.setDescrUfficioEmittente(lCodUDS);
       String lCodComune = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE);
      
       if( lCodComune != null && !lCodComune.equals("") && !lCodComune.equals("-"))
         lCodUDS = getCodUfficioByCodTipoUfficioDescrComune(lCodUDS, lCodComune);

       lLicenza.setCodUfficioEmittente(lCodUDS);

       ComuneModel lComune = getCodComuneByDescr(getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE));
       lLicenza.setCodLuogoEmittente(lComune.getCodComune());

       lLicenza.setDataEmissioneOrdinanza(getRequestDateParameter( ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA,
                                                                   ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA,
                                                                   ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA)
                                                                  );

       return lLicenza;
       
     } // CHIUDE private LicenzaLibAnticipataModel generaLicenza_int...

     /**
      * Carica sull'opportuna Licenza i propri periodi
      * @param aLicenza
      * @throws F3BException
      */
     private void setPeriodiInLicenze_int(LicenzaPeriodiLibAnticipataModel aLicenza)	      throws F3BException
     {
    	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	 //  siesLogger.debug(" setPeriodiInLicenze_int  - Inizio " );
     	
 	      // Conteggio dei periodi valorizzati
 	      int num = 0;
 	      
 	      for (int j = 0, k = mInd_int; j < 10;  j++, k++)
 	    	  if ( (mPeriodi_int[k].mDataIni != null) && (mPeriodi_int[k].mDataFine != null))
 	    		  num++;
 	      
 	      if (num > 0)
 	    	  aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);
 	
 	      // Generazione dei Periodi di Libertà Anticipa
 	      for (int k = 0, j = 0; k < 10; k++,mInd_int++, j++)
 	      {
 		        if ( (mPeriodi_int[mInd_int].mDataIni != null) && (mPeriodi_int[mInd_int].mDataFine != null))
 		        {
 		        	aLicenza.getPeriodi()[j] = generaPeriodoLibAnticipa_int(aLicenza.getLicenza().getFlagConcesso());
 		        }
 		       // else
 		       //   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 		       //   siesLogger.debug("setPeriodiInLicenze_int - data nulla ");
 	      }

     } // CHIUDE private void setPeriodiInLicenze_int ... 

     /****************************************************************************
      *
      * @param aFlagConcesso
      * @return
      * @throws F3BException
      ************************************************************************* */
     private PeriodoLibAnticipataModel generaPeriodoLibAnticipa_int(String aFlagConcesso)      throws F3BException
     {
       	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       	 //  siesLogger.debug(" setPeriodiInLicenze_int  - Inizio " );  	
       
 	    	PeriodoLibAnticipataModel lPeriodoLib = null;
 	      lPeriodoLib = new PeriodoLibAnticipataModel();
 	      
 	      lPeriodoLib.setDataInizio(mPeriodi_int[mInd_int].mDataIni);
 	      lPeriodoLib.setDataFine(mPeriodi_int[mInd_int].mDataFine);
 	      lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
 	      lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
 	      lPeriodoLib.setDataInserimento(mOggi);
 	      lPeriodoLib.setFlagConcesso(aFlagConcesso);

 	      return lPeriodoLib;
     }
   
  //>>>>>>>>>>>>>>>>>  METODI per gestione comue di TENORE, EVENTO, ORDINANZA 	<<<<<<<<<<<<<<<<<<<<<<<<<<<<     
    
   /****************************************************************************
    * N.B. In particolare il metodo ritorna un array con un solo elemento
    *
    * @return
    * @throws F3BException
    ************************************************************************* */
   
     private TenoreModel[] generaTenori(String aCodEsistoTenore, String aCodEsistoTenore_spe, String aCodEsistoTenore_int ) throws F3BException
    {
    	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	   siesLogger.debug(" generaTenori - Inizio " );  	
     
 // Vediamo quanti sono gli oggetti validi (Cioè Cod_Ogg diverso da spazio);   	
    	int lung = lCodOggetti.length;
    	int lSizeArray = 0;
        if (lung > 0)
        {
	          for (int i = 0; i < lung; i++)
	          {
	        	  if(lCodOggetti[i].compareTo("") != 0 )
	        	  {
	        		  lSizeArray ++;
	        	  }
	          }
        }
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug(" Oggetti iniziali = "+lung+ " - Oggetti Validi = "+lSizeArray);  
// 
// Gli oggetti Validi vengono spostati in un array dimensionato correttamente con i codici validi        
        String[] lCodOggettiVal = new String[lSizeArray];

        for (int i = 0, c = 0; i < lung; i++)
        {
        	  if(lCodOggetti[i].compareTo("") != 0 )
	          {
        		  lCodOggettiVal[c] = lCodOggetti[i];
        		  c++;
	          }
	     }
//
        String lCodUDS = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
        
    	TenoreModel[] lTenori = null;
    	lTenori = new TenoreModel[lSizeArray];
        for (int i = 0; i < lSizeArray; i++)
        {
		      TenoreModel lTenModel =  new TenoreModel();
		      if(lCodOggettiVal[i].equals("2130") )
		      {	
			      if( lCodUDS != null && !lCodUDS.equals("") && !lCodUDS.equals("-")
			          && lCodUDS.equals("UDS")) // Ufficio di Sorveglianza/Magistrato di Sorveglianza
			      {
	
			    		lTenModel.setCodOggettoTenore("2130");  // 2130 = Concessione Liberazione Anticipata
		    	  }
		    	  else
		    	  {
		    		    lTenModel.setCodOggettoTenore("0076");  // 0076 = Concessione Liberazione Anticipata
		    	  }
		      }
		      else
		      {
		    	  lTenModel.setCodOggettoTenore(lCodOggettiVal[i]);
		      }
		    	  
		      if(lCodOggettiVal[i].equals("2130"))
		    	  lTenModel.setCodEsitoTenore(aCodEsistoTenore);
		      else if(lCodOggettiVal[i].equals("2131"))
		    	  lTenModel.setCodEsitoTenore(aCodEsistoTenore_spe);
		      else if(lCodOggettiVal[i].equals("2132"))
		    	  lTenModel.setCodEsitoTenore(aCodEsistoTenore_int);
		
		      lTenModel.setProgrTenore( new BigDecimal(i+1) );
		      lTenModel.setData(getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
		                                                 ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
		                                                 ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE) );
		/*
		
		      lTenModel.setData( getRequestDateParameter( ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA,
		                                                  ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA,
		                                                  ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA) );
		*/
		
		      lTenModel.setCodMagistrato( "-" );
		      lTenModel.setCodDettaglioOggetto( "-" );
		      //lTenModel.setDescrOggettoTenore( lDescOggetti[i]);
		      lTenModel.setCodUfficioInserimento( getCodUfficioUtenteConnesso() ); //Codice dell'ufficio dell'operatore che inserisce
		      lTenModel.setCodOperatoreInserimento( getCodUtenteConnesso() );      //Codice dell'operatore che inserisce
		      lTenModel.setDataInserimento( mOggi );
		      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		      siesLogger.debug(" TENOREGeneraTo di "+i+" = "+lTenModel);  
		      lTenori[i] = lTenModel;
        }

        return lTenori;
    }

    /***************************************************************************
     * Genera il singolo evento (Ordinanza)
     * @return
     * @throws F3BException
     ************************************************************************ */
   
    private EventoModel generaEvento(PosizioneGiuridicaModel lPosizione)	      throws F3BException
    {
    	 //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	 //  siesLogger.debug(" generaEvento  - Inizio " );  //Prepara Model Evento.
      
      EventoModel lEvento = new EventoModel();

      lEvento.setCodTipoEvento("01"); // 01 = Provvedimento.
      lEvento.setCodTipoProvvedimento("03");  // 03 = Ordinanza.

      String lCodUDS = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
      String lCodComune = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE);

      if( lCodUDS != null && !lCodUDS.equals("") && !lCodUDS.equals("-")
          && lCodUDS.equals("UDS")) // Ufficio di Sorveglianza/Magistrato di Sorveglianza
      {
        lEvento.setCodMotivo("2130");  // 2130 = Concessione Liberazione Anticipata
      }
      else
      {
        lEvento.setCodMotivo("0076");  // 0076 = Concessione Liberazione Anticipata
      }

      if( lCodComune != null && !lCodComune.equals("") && !lCodComune.equals("-"))
        lCodUDS = getCodUfficioByCodTipoUfficioDescrComune(lCodUDS, lCodComune);

      lEvento.setCodUfficioEmittente(lCodUDS);

      ComuneModel lComune = getCodComuneByDescr(getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE));
      lEvento.setCodLuogoEmittente(lComune.getCodComune());

      lEvento.setCodEsito("0020"); // [ESITO_PROVVEDIMENTO: 0020 = Concede]

/*
      Date lDataEmissione = getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
                                                     ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
                                                     ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
*/

      Date lDataEmissione = getRequestDateParameter( ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA,
                                                     ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA,
                                                     ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA);

      lEvento.setDataEmissione( lDataEmissione );
      lEvento.setDataTrasmissioneAtti( lDataEmissione );

      lEvento.setFasSieIdFascicoloSiep( lIdFascicolo );
      //lEvento.setFasSiuIdFascicoloSius( mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() );

      /*25-08-2005 -- dario -- luciana -- altrimenti risulta l'uffico della procura
       lEvento.setCodUfficioInserimento( getCodUfficioUtenteConnesso() );
       */
      //lEvento.setCodUfficioInserimento(lCodUDS);
      // Ripristinato valore per problema della cancellazione 11-08-2005 DL
      lEvento.setCodUfficioInserimento( getCodUfficioUtenteConnesso() );
      lEvento.setCodOperatoreInserimento( getCodUtenteConnesso() );
      lEvento.setDataInserimento( mOggi );

      lEvento.setCodLuogoDestinatario("-");
      lEvento.setCodTipoUfficioDestinatario("-");
      lEvento.setCodUfficioDestinatario("-");
      lEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
      lEvento.setFlagStampaSiep("S");
      lEvento.setFlagVideoSiep("S");

//      if(lPosizione.isLibero())
//      {
//        lEvento.setFlagDocumentoRegistrato("S");
//      }

      return lEvento;
    }

    /***************************************************************************
     *
     * @return
     * @throws F3BException
     ************************************************************************ */
    private DepositoOrdinanzaPcModel generaOrdinanza(int aSommaGiornitot)
      throws F3BException
    {
      // Prepara il model DepositoOrdinanza.
      DepositoOrdinanzaPcModel lDepOrdModel = new DepositoOrdinanzaPcModel();


      Date lDataEmissione = getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
                                                     ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
                                                     ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

/*
      Date lDataEmissione = getRequestDateParameter( ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA,
                                                     ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA,
                                                     ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA);

*/
      lDepOrdModel.setDataCameraConsiglio(lDataEmissione);
      lDepOrdModel.setCodUfficioMagistratoComp( "-" );
      lDepOrdModel.setLuogoSvolgimentoProva( "-" );
      lDepOrdModel.setServizioTerapeuticoComp( "-" );
      
   //   lDepOrdModel.setNumGiorniLibanticipata(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBANTICIPATA));
      lDepOrdModel.setNumGiorniLibanticipata(new BigDecimal(aSommaGiornitot));
      lDepOrdModel.setCodUffTdsConcessoRiduzione( "-" );

      lDepOrdModel.setCodTipoOrdinanza( "LA" );

      lDepOrdModel.setCodMagistrato( "-" );
      lDepOrdModel.setCodNaturaProvvedimento( "-" );

      //lDepOrdModel.setGenPridGeneraleProcedimento( mIdGenProc );

      lDepOrdModel.setCodOperatoreInserimento( getCodUtenteConnesso() );
      lDepOrdModel.setCodUfficioInserimento( getCodUfficioUtenteConnesso() );
      lDepOrdModel.setDataInserimento( mOggi );

      lDepOrdModel.setDataDeposito(lDataEmissione);

      return lDepOrdModel;
    }
}