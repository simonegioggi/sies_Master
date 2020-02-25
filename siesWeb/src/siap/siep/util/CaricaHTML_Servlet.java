package siap.siep.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.RemoteScriptingServlet;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.calcolopena.action.ICostantiCalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.penaresidua.model.PenaResiduaModel;

/**
 * Classe servlet che consente le chiamate dirette dalle pagine HTML senza necessità di post e refresh delle
 * pagine. In questa classe vanno dichiarati i metodi che vengono invocati dalle pagina HTML. Tali metodi
 * possono ricevere in input solo dati di tipo String e devono restituire solo dati di tipo String. Se fosse
 * necessario restituire dati più complessi si deve utilizzare una stringa concatenata con separatori. Per
 * utilizzarla dichiarare laservlet nel web.xml ed importare nelle jsp il javascript jsrsClient.js.
 * 
 * @author
 *
 */
public class CaricaHTML_Servlet extends RemoteScriptingServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7829776030532613222L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Calcola i quantum in giorni mesi e anni di un periodo di pena sofferto definito da un intervallo di
	 * date (dal-al). I calcoli vengono effettuati considerando il dies a quo. Il giorno inizio è considerato
	 * come espiato. I quantum vengono restituiti normalizzati (30gg = 1 mese)
	 * 
	 * @param ggInizio
	 * @param mmInizio
	 * @param aaInizio
	 * @param ggFine
	 * @param mmFine
	 * @param aaFine
	 * @return Stringa concatenata nel formato anni~#mesi~#giorni
	 * @throws Exception
	 */
	public static String getQuantumIntervallo(String ggInizio, String mmInizio, String aaInizio,
			String ggFine, String mmFine, String aaFine, String Tipomis) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--XX-- getQuantumIntervallo Start ");
		Date data_inizio = null;
		Date data_fine = null;

		data_inizio = DateUtils.getDate(aaInizio, mmInizio, ggInizio);
		data_fine = DateUtils.getDate(aaFine, mmFine, ggFine);

		// LogF3B.getLogger().debug("dal "+DateUtils.getDateToString(data_inizio,"dd-MM-yyyy"));
		// LogF3B.getLogger().debug("al "+DateUtils.getDateToString(data_fine,"dd-MM-yyyy"));

		CalendarModel lCalPenaEspiata = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();

		lCalPenaEspiata.setDataInizio(data_inizio);
		lCalPenaEspiata.setDataFine(data_fine);

		// Attenzione!! Nel caso dei computi la classe che effettua i calcoli dei
		// quantum è la:
		// siap.siep.calcolopena.action.ActInserisciAnnotazioniManualiComputo
		// che utlizza la seguente chiamata
		// lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata);
		// e non effettua la normalizzazione dei quantum per cui potrebbe

		String diesAquo = "S"; // forzato a S per conteggiare anche la data inizio
		if (diesAquo.equals("S")) {
			lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata, false);
		} else {
			lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata, true);
		}

		// Normalizzo i quantum
		lCalPenaEspiata = lCalUtil.ricalcolaGAM(lCalPenaEspiata);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Quantum = " + lCalPenaEspiata);

		// Creo la stringa di risposta nel formato: anni~#mesi~#giorni
		String lQuantum = "";
		String lSeparatore = "~#";
		lQuantum = lCalPenaEspiata.getNumAnni() + lSeparatore + lCalPenaEspiata.getNumMesi() + lSeparatore
				+ lCalPenaEspiata.getNumGiorni() + lSeparatore + 0;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Stringa di ritorno " + lQuantum);

		// Tipo Misura = 'COMPUTO PERIODO DI MESSA ALLA PROVA', Cod. Misura = 'CL'
		if (Tipomis != null) {
			if ("CL".equals(Tipomis)) {
				// Nel caso della Messa Alla Prova i Quantum calcolati vanno divisi per 3 e arrotondati
				// all'intero più vicino
				// I giorni effettivamente espiati vanno memorizzati nel campo GIORNI- n.b. trattasi dei
				// quantum convertiti in GG e non
				// dei giorni calendariali.
				//
				CalendarModel lCalendarConvertito = MisuraCautelareCumuloModel
						.getQuantumMessaAllaProva(lCalPenaEspiata);

				int nAA = lCalendarConvertito.getNumAnni();
				int nMM = lCalendarConvertito.getNumMesi();
				int nGG = lCalendarConvertito.getNumGiorni();

				int lGiorni = 360 * lCalPenaEspiata.getNumAnni() + 30 * lCalPenaEspiata.getNumMesi()
						+ lCalPenaEspiata.getNumGiorni();

				// Diff + 1 per il dies a quo
				// lMisMod.setGiorni (new BigDecimal (DateUtils.getDaysBetween (lMisMod.getDataInizio(),
				// lMisMod.getDataFine())+1 ) );

				lQuantum = nAA + lSeparatore + nMM + lSeparatore + nGG + lSeparatore + lGiorni;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Servlet - Stringa di ritorno (Cod CL) = " + lQuantum);

			}
		}

		return lQuantum;
	}

	public static String getQuantumIntervallo(String ggInizio, String mmInizio, String aaInizio,
			String ggFine, String mmFine, String aaFine) throws Exception {
		return getQuantumIntervallo(ggInizio, mmInizio, aaInizio, ggFine, mmFine, aaFine, null);

	}

	/**
	 * Calcola la data ifne nota data inizio e quantum
	 * 
	 * @param ggInizio
	 * @param mmInizio
	 * @param aaInizio
	 * @param ggQuantum
	 * @param mmQuantum
	 * @param aaQuantum
	 * @return data fine in formato yyyy~#MM~#dd, oppure ~#~# se data fine non calcolabile
	 * @throws Exception
	 */
	public static String getDataFine(String ggInizio, String mmInizio, String aaInizio, String ggQuantum,
			String mmQuantum, String aaQuantum) throws Exception {
		// LogF3B.getLogger().debug("getDataFine Start ");

		Date data_inizio = null;
		Date data_fine = null;

		PenaResiduaModel lPenaIniziale = new PenaResiduaModel();

		data_inizio = DateUtils.getDate(aaInizio, mmInizio, ggInizio);

		if (!ggQuantum.equals(""))
			lPenaIniziale.setNumGiorniReclusione(new BigDecimal(ggQuantum));
		if (!mmQuantum.equals(""))
			lPenaIniziale.setNumMesiReclusione(new BigDecimal(mmQuantum));
		if (!aaQuantum.equals(""))
			lPenaIniziale.setNumAnniReclusione(new BigDecimal(aaQuantum));

		//
		CalcoloPenaModel lCalcPenaModel = new CalcoloPenaModel();

		lCalcPenaModel.setPenaResiduaManuale(lPenaIniziale);
		lCalcPenaModel.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE);

		PenaResiduaModel lPenaRideterminata = null;
		lPenaRideterminata = lCalcPenaModel.getPenaDaEspiare(data_inizio, null, "all", null);
		// siesLogger.debug("Pena rideterminata: "+lPenaRideterminata);

		data_fine = lPenaRideterminata.getDataFine();

		// Creo la stringa di risposta nel formato: anni~#mesi~#giorni
		String lRisposta = "";
		String lSeparatore = "~#";
		if (data_fine != null) {
			lRisposta = DateUtils.getDateToString(data_fine, "yyyy") + lSeparatore
					+ DateUtils.getDateToString(data_fine, "MM") + lSeparatore
					+ DateUtils.getDateToString(data_fine, "dd");
		} else {
			lRisposta = "~#~#";
		}

		// siesLogger.debug("Stringa di ritorno "+lRisposta);

		return lRisposta;
	}
  
  /**
   * Calcola la data del rinvio differimento pena partendo da una Data_Inizio e sommandoci i 
   * quantum (in giorni mesi e anni ) di un periodo di rinvio Differimento Pena.
   * 
   * Se la data è presente controlla che questa sia valida.
   * I calcoli vengono effettuati considerando il dies a quo. Il giorno inizio è considerato come espiato. 
   * I quantum vengono restituiti normalizzati (30gg = 1 mese)
   * @param ggInizio
   * @param mmInizio
   * @param aaInizio
   * @param ggFine
   * @param mmFine
   * @param aaFine
   * @param ggDiff
   * @param mmDiff
   * @param aaDiff
   * @return Stringa concatenata nel formato anni~#mesi~#giorni
   * @throws Exception
   */
  public static String getDataRinvioDifferimento (String ggInizio,
                                             String mmInizio,
                                             String aaInizio,
                                             String ggFine,
                                             String mmFine,
                                             String aaFine,
                                             String ggDiff, String mmDiff, String aaDiff) throws Exception 
  { 
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.debug("--XX-- getDataRinvioDifferimento Start - data Ini - gg =>"+ggInizio+"< mm=>"+mmInizio+"< aa=>"+aaInizio+"<");
    //siesLogger.debug("--XX-- getDataRinvioDifferimento Start - data fine - gg =>"+ggFine+"< mm=>"+mmFine+"< aa=>"+aaFine+"<");
    //siesLogger.debug("--XX-- getDataRinvioDifferimento Start - Quantum - gg =>"+ggDiff+"< mm=>"+mmDiff+"< aa=>"+aaDiff+"<");
    
    Date data_inizio = null;
    Date data_fine   = null;
    
    data_inizio = DateUtils.getDate(aaInizio,mmInizio,ggInizio);
    data_fine   = DateUtils.getDate(aaFine,mmFine,ggFine);

    //siesLogger.debug("dal "+DateUtils.getDateToString(data_inizio,"dd-MM-yyyy"));
    //siesLogger.debug("al "+DateUtils.getDateToString(data_fine,"dd-MM-yyyy"));
    
    CalendarModel lCalDifferimento = new CalendarModel();
    CalendarUtil  lCalUtil = new CalendarUtil();
    
    String lStringaRitorno = "";
    if(data_fine != null )
    {
   	 // Data_fine != null : DEVO CALCOLARE E RITORNARE I QUANTUM del DIFFERIMENTO : Primo carattere di stringa ritorno = 0
   	 
   	 //siesLogger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   data fine diversa da null ");
   	 lCalDifferimento.setDataInizio(data_inizio);
   	 lCalDifferimento.setDataFine(data_fine);
    
   	 // Attenzione!! Nel caso dei computi la classe che effettua i calcoli dei 
   	 //              quantum è la: 
   	 // siap.siep.calcolopena.action.ActInserisciAnnotazioniManualiComputo
   	 // che utlizza la seguente chiamata
   	 // lCalDifferimento = lCalUtil.CalcolaNumGiorniMesiAnni(lCalDifferimento);
   	 // e non effettua la normalizzazione dei quantum per cui potrebbe

	    String diesAquo = "S"; // forzato a S per conteggiare anche la data inizio
	    if (diesAquo.equals("S")){
	      lCalDifferimento = lCalUtil.CalcolaNumGiorniMesiAnni(lCalDifferimento,false);
	    }
	    else {
	      lCalDifferimento = lCalUtil.CalcolaNumGiorniMesiAnni(lCalDifferimento,true);
	    }
	
	    // Normalizzo i quantum
	    lCalDifferimento = lCalUtil.ricalcolaGAM(lCalDifferimento);
	    
	    //siesLogger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   Quantum = "+lCalDifferimento);
	    
	    // Creo la stringa di risposta nel formato:  anni~#mesi~#giorni
	    String lQuantum="";
	    String lSeparatore = "~#";
	    
	    lQuantum = 0
	   		 		+lSeparatore
	   		 	  +lCalDifferimento.getNumAnni()
	              +lSeparatore
	              +lCalDifferimento.getNumMesi()
	              +lSeparatore
	              +lCalDifferimento.getNumGiorni();	


	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug("Stringa di ritorno di Quantum = "+lQuantum);
	    
	    lStringaRitorno = lQuantum;
    } else {
   	 
   	// Data_fine == null : DEVO CALCOLARE LA DATA FINE RINVIO DIFFERIMENTO; e poi metterla nella stringa di RITORNO (Primo carattere = 1)
   	 
   	 //siesLogger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   data fine Uguale a null ");
       
       CalcoloPenaModel lCalcDiffModel = new CalcoloPenaModel();
       
       PenaResiduaModel lquantumModel = new PenaResiduaModel();
       if ("".equals(aaDiff)) aaDiff = "0";
       if ("".equals(mmDiff)) mmDiff = "0";
       if ("".equals(ggDiff)) ggDiff = "0";
       
       lquantumModel.setNumAnniReclusione(new BigDecimal(aaDiff) );
       lquantumModel.setNumMesiReclusione(new BigDecimal(mmDiff) );
       lquantumModel.setNumGiorniReclusione(new BigDecimal(ggDiff) );

       lCalcDiffModel.setPenaResiduaManuale(lquantumModel);
       lCalcDiffModel.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE);

       PenaResiduaModel FineDifferimentoModel = null;
       FineDifferimentoModel = lCalcDiffModel.getPenaDaEspiare (data_inizio,null,"all",null);
       //FineDifferimentoModel = lCalcDiffModel.getPenaDaEspiare (data_inizio,lDataSistemaPerCalcoli,"all",null);
       
       String SggF = DateUtils.getDayToString(FineDifferimentoModel.getDataFine());
       String SmmF = DateUtils.getMonthToString(FineDifferimentoModel.getDataFine());
       String SaaF = DateUtils.getYearToString(FineDifferimentoModel.getDataFine());
       
       // Creo la stringa di risposta (Data_Fine) nel formato:  anni~#mesi~#giorni
	    String lDataDifferimento="";
	    String lSeparatore = "~#";
	    
	    siesLogger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   mando la stringa DATA_FINE ");
	    lDataDifferimento = 1
	   		 		+lSeparatore
	   		 		+SaaF
	               +lSeparatore
	               +SmmF
	               +lSeparatore
	               +SggF;	

	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug("Stringa di ritorno "+lDataDifferimento);
	    
	    lStringaRitorno = lDataDifferimento;
   	 
    }
    
    
    return lStringaRitorno;
  }
  
  
  

}