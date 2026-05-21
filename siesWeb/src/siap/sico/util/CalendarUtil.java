package siap.sico.util;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.sico.calendar.model.CalendarModel;

/**
 * CalendarController - Classe Controller per il calcolo delle date
 *
 * @version 1.0
 */
public class CalendarUtil {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /*****************************************************************************
	 * Calcola il quantum (aModel.mGG , mMM, mAA) a partire da data inizio e data fine del model passato in
	 * input.
   * 
	 * @param aModel
	 *            - Model con data inizio e data fine valorizzati
	 * @param lEscludiDiesaquo
	 *            - indica se escludere il giorno inizio nel calcolo del quantum. Se true il giorno inizio non
	 *            viene conteggiato. Se false, viene conteggiatao nel quantum anche il gg inizio.<br>
   *        ES: data inizio 10/04/2006, data fine 15/04/2006<br>
   *            lEscludiDiesaquo = true,  quantum = 5gg (11,12,13,14,15)<br>
   *            lEscludiDiesaquo = false, quantum = 6gg (10,11,12,13,14,15)<br>
   * @return Model con i <b>soli</b> quantum (gg,mm,aaaa) valorizzati (data inizio e data fine a null)
	 */
	public CalendarModel CalcolaNumGiorniMesiAnni(CalendarModel aModel, boolean lEscludiDiesaquo) {

		if (lEscludiDiesaquo) {
      return contagiorni_escludidiesaquo(aModel);
		} else
      return contagiorniclassic(aModel);
  }

  /*****************************************************************************
	 * Valorizza aModel.mGG , mMM, mAA a partire da data inizio e data fine conteggiando nel quantum anche il
	 * giorno di inizio.
   * 
   * Come il metodo CalcolaNumGiorniMesiAnni con lEscludiDiesaquo = false
   * 
	 * @param aModel
	 *            - Model con data inizio e data fine valorizzati
   * @return Model con i <b>soli</b> quantum (gg,mm,aaaa) valorizzati (data inizio e data fine a null)
	 */
	public CalendarModel CalcolaNumGiorniMesiAnni(CalendarModel aModel) {

    return contagiorniclassic(aModel);
  }

  /*****************************************************************************
	 * METODO PRIVATO : Valorizza aModel.mGG , mMM, mAA a partire da data inizio e data fine (considera nel
	 * quantum anche il gg iniziale) (USE CASE SIEP-UC-014-LV-AA) Il quantum restituito non è necessariamente
	 * normalizzato vale a dire che può valere 30 giorni, 3 mesi e 2anni opure anche 31 giorni 2mesi e un anno
   *   
	 */
	private CalendarModel contagiorniclassic(CalendarModel aModel) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("contagiorniclassic : " + aModel);

    CalendarModel lModRet = new CalendarModel();

    //** Modifica per prevenire NullPointerException nel caso di model con date a null
    //** 13/04/2004 DL
    if(aModel == null || (aModel.getDataInizio() == null || aModel.getDataFine() == null))
      return lModRet;

    int ggI=Integer.parseInt(DateUtils.getDayToString(aModel.getDataInizio()));
    int mmI=Integer.parseInt(DateUtils.getMonthToString(aModel.getDataInizio()));
    int aaI=Integer.parseInt(DateUtils.getYearToString(aModel.getDataInizio()));

    int ggF=Integer.parseInt(DateUtils.getDayToString(aModel.getDataFine()));
    int mmF=Integer.parseInt(DateUtils.getMonthToString(aModel.getDataFine()));
    int aaF=Integer.parseInt(DateUtils.getYearToString(aModel.getDataFine()));

    //==========================================================================
    // Due casi
    // - ggI>ggF   
    // - ggI<=ggF
    //==========================================================================
		if (ggI > ggF) {
			// Calcolo il numero di giorni ad arrivare alla
			// fine del mese di inizio (+1 dies a quo) + i giorni ad arrivare al giorno fine
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
			// posto di LogF3B.getLogger()
       siesLogger.debug("ggI>ggF");

			int lNrGiorni = Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI)))
					- ggI + ggF + 1; // +1 per considerare anche il gg iniziale
       lModRet.setNumGiorni(lNrGiorni);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
       siesLogger.debug("NumGiorni 1 : "+lModRet.getNumGiorni());

       // Stesso mese e giorno inizio = giorno fine +1 considero il quantum
       // espresso solo in anni
			if (lModRet.getNumGiorni() == Integer
					.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) && mmI == mmF
					&& aaI != aaF) {
				// vera se ggF=ggI-1
          // es 15/04/2001 14/04/2002 ==> quantum (0,0,1) lNrGiorni=30
          // es 15/03/2001 14/03/2002 ==> quantum (0,0,1) lNrGiorni=31
          // es 15/02/2001 14/02/2002 ==> quantum (0,0,1) lNrGiorni=28
          // es 15/02/2000 14/02/2001 ==> quantum (0,0,1) lNrGiorni=29
          lModRet.setNumGiorni(0);
          lModRet.setNumMesi(0);
          lModRet.setNumAnni(aaF-aaI);
			} else {
				// =====================================================================
         // lNrGiorni può essere compreso tra 2 e 31 ma non viene normalizzato.
         // Esistono due casi particolari in cui invece di restituire il lNrGiorni 
         // calcolato questo viene convertito in 1 mese e 0 giorni. Ciò accade
         // nel caso di mese inizio sia febbraio e ggI=ggF+1
						// es: data inizio 26/02/2001, data fine 25/04/2002 lNrGiorni = 28 convetiti in 0
						// giorni e 1 mese
						// es: data inizio 15/02/2000, data fine 14/07/2001 lNrGiorni = 29 (bisestile)
						// convetiti in 0 giorni e 1 mese
         //=====================================================================
         // Nel caso il mese di inizio sia febbraio lNrGiorni calcolati può essere
         // al più 28 o 29, verifico se è possibile in questi casi convertire il
         // giorni in un mese intero. (questo e vero quando ggI=ggF+1)
         // Questo è vero se lNrGiorni = num giorni mese inizio quindi
         // 28 giorni per anni non bisestili e 29 per quelli bisestili, in
         // questi casi azzero il quantum di giorni (lNrGiorni) e calcolo
         // il quantum di mesi come differenza tra i mesi (senza cioè correggere
         // il mese di inizio)
         // Attenzione al caso di lNrGiorni = 28 ma anno non bisestile, in questo 
         // caso ggI=ggF+2 e il quantum va calcolato con giorni=28 e correggendo 
         // il mese di inizio 
				if (mmI == 2 && lNrGiorni >= 28) {
					// Se Febbraio e nrgiorni >= 28 (n.b. può valere solo 28 o 29)
					if (lNrGiorni == 29 && Integer
							.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) == 29) {
              lNrGiorni = 0;
              lModRet.setNumGiorni(lNrGiorni);
					} else if (lNrGiorni == 28 && Integer
							.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) == 28) {
              lNrGiorni = 0; 
              lModRet.setNumGiorni(lNrGiorni);
					} else if (lNrGiorni == 28 && Integer
							.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) == 29) {
						// ggI=ggF+2 es data inizio 15/02/2000, data fine 13/07/2001
             mmI++;  // lascio i giorni invariati, ma devo correggere il mese inizio
           }
           
//           if(   lNrGiorni > 28 // 29 solo se feb bisestile e ggI=ggF+1, es 26/02/2000 25/08/2002 
//              && Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI,mmI))) == 29)
//           {
//             lNrGiorni = lNrGiorni - 29; // cioè 0
//             lModRet.setNumGiorni(lNrGiorni);
//           }
//           // è false la prima if per cui può essere solo 28, se anno non bisestile
//           // allora vuol dire ancora ggI=ggF+1, es 26/02/2001 25/02/2002 
//           // n.b. potrebbe essere 28 di un bisestile, in questo caso ggI=ggF+2
//           //      vale a dire è ggF=ggI-1
//           if(   lNrGiorni >= 28 // solo 28 
//              && Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI,mmI))) != 29)
//           {
//             lNrGiorni = lNrGiorni - 28; // 0
//             lModRet.setNumGiorni(lNrGiorni);
//           }
           
					if (mmI > mmF) {
             lModRet.setNumMesi(12-mmI+mmF);
             lModRet.setNumAnni(aaF-(aaI+1));

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
             siesLogger.debug("lModRet 1 : "+lModRet);
					} else {
             lModRet.setNumMesi(mmF-mmI);
             lModRet.setNumAnni(aaF-aaI);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
             siesLogger.debug("lModRet 3 : "+lModRet);
           }
				} else {
           mmI++;
					if (mmI > mmF) {
             lModRet.setNumMesi(12-mmI+mmF);
             lModRet.setNumAnni(aaF-(aaI+1));
					} else {
             lModRet.setNumMesi(mmF-mmI);
             lModRet.setNumAnni(aaF-aaI);
           }
         }
       }
		} else { // ggI<=ggF
      lModRet.setNumGiorni(ggF-ggI+1); // +1 per considerare anche il gg iniziale

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
      siesLogger.debug("ggI<=ggF Qgg= "+lModRet.getNumGiorni());

			if (mmI <= mmF) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
        siesLogger.debug("mmI <= mmF");
        lModRet.setNumMesi(mmF-mmI);
        
				if (Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) == lModRet
						.getNumGiorni()) {
					// Il numero di GG calcolati coincide con il numero di gg del mese di partenza.
					// In questo caso considero come se avessi scontato un mese intero, per cui porto i
					// giorni a 0 e sommo 1 al mese
					// n.b. questa situazione è vera in genere se ggI=01 e ggF=ultimo del mese 
					// di inizio ma anche se ggI>01 
					// es 01/02/2003 28/03/2004 Qgg=28=giorni di febbraio 2003
					// es 02/02/2003 29/03/2004 Qgg=28=giorni di febbraio 2003
					// es 03/02/2003 30/03/2004 Qgg=28=giorni di febbraio 2003
					// es 04/02/2003 31/03/2004 Qgg=28=giorni di febbraio 2003
					// n.b. i gg calcolati potrebbero anche essere >utimo giorno del mese
					// es. 01/04/2001 31/05/2001 gg calcolato=31>30 (ultimo di aprile)
					// in questo caso la procedura restituisce un quantum di 31gg e 1mese
					// che normalizzato porterebbe a 1gg e 2mesi
					if (lModRet.getNumMesi() == 11) {
              lModRet.setNumGiorni(0);
              lModRet.setNumMesi(0);
              lModRet.setNumAnni(aaF+1-aaI);
					} else { // sono finito sul primo del mese di arrivo ma ho scalato 1 per il dies a quo
              lModRet.setNumGiorni(0);
              lModRet.setNumMesi(lModRet.getNumMesi()+1);
              lModRet.setNumAnni(aaF-aaI);
          }
				} else
          lModRet.setNumAnni(aaF-aaI);
			} else {
				// mmI > mmF
				if (Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) == lModRet
						.getNumGiorni()) {
					// Il numero di GG calcolati coincide con il numero di gg del mese di partenza.
					// Vero solo se il ggI=01 e ggF=ultimo del mese di inizio.
					// In questo caso considero come se avessi scontato un mese intero, per
					// cui porto i giorni a 0 e sommo 1 al mese
					// n.b. i gg calcolati potrebbero anche essere >utimo giorno del mese
					//      es. 01/04/2001 31/05/2001 gg calcolato=31>30 (ultimo di aprile)
					//      in questo caso la procedura restituisce un quantum di 31gg e 1mese
					//      che normalizzato porterebbe a 1gg e 2mesi
          lModRet.setNumGiorni(0);
          lModRet.setNumMesi(12-mmI+mmF+1);
				} else {
          lModRet.setNumMesi(12-mmI+mmF);
        }
        lModRet.setNumAnni(aaF-(aaI+1));
      }
     }
    
    // Modifica del 28/09/2006 per limitare i casi in cui il calcolo inverso
    // della fornisce risultati diversi del calcolo diretto 
    if(lModRet.getNumGiorni()==31){
      lModRet.setNumGiorni(30);
    }
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("lModRet : "+lModRet);

    return lModRet;
  }

  /**
	 * METODO PRIVATO : Valorizza aModel.mGG , mMM, mAA a partire da data inizio e data fine (USE CASE
	 * SIEP-UC-014-LV-AA) escludendo il dies a quo!!! Identico al contagiorniclassic ma non aggiunge 1 al
	 * lNrGiorni
   * 
	 * Il calcoli non viene eseguito come differenza in giorni tra due oggetti date, quindi non calcola
	 * l'effettiva differenza di giorni tra due date
   */
	private CalendarModel contagiorni_escludidiesaquo(CalendarModel aModel) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("contagiorni_escludidiesaquo Model input : "+aModel);

    CalendarModel lModRet = new CalendarModel();

    //** Modifica per prevenire NullPointerException nel caso di model con date a null
    //** 13/04/2004 DL
    if(aModel == null || (aModel.getDataInizio() == null || aModel.getDataFine() == null))
      return lModRet;

    int ggI=Integer.parseInt(DateUtils.getDayToString(aModel.getDataInizio()));
    int mmI=Integer.parseInt(DateUtils.getMonthToString(aModel.getDataInizio()));
    int aaI=Integer.parseInt(DateUtils.getYearToString(aModel.getDataInizio()));

    int ggF=Integer.parseInt(DateUtils.getDayToString(aModel.getDataFine()));
    int mmF=Integer.parseInt(DateUtils.getMonthToString(aModel.getDataFine()));
    int aaF=Integer.parseInt(DateUtils.getYearToString(aModel.getDataFine()));

		if (ggI > ggF) {
			int lNrGiorni = Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI)))
					- ggI + ggF;
       lModRet.setNumGiorni(lNrGiorni);

			if (lModRet.getNumGiorni() == Integer
					.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) && mmI == mmF
					&& aaI != aaF) {
          lModRet.setNumGiorni(0);
          lModRet.setNumMesi(0);
          lModRet.setNumAnni(aaF-aaI);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
          siesLogger.debug("lModRet 1 : "+lModRet);
			} else {
				if (mmI == 2 && lNrGiorni >= 28) { // Se Febbraio e nrgiorni >= 28
					if (lNrGiorni > 28 && Integer
							.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) == 29) {
              lNrGiorni = lNrGiorni - 29;
              lModRet.setNumGiorni(lNrGiorni);
            }
					if (lNrGiorni >= 28 && Integer
							.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) != 29) {
              lNrGiorni = lNrGiorni - 28;
              lModRet.setNumGiorni(lNrGiorni);
            }
					if (mmI > mmF) {
              lModRet.setNumMesi(12-mmI+mmF);
              lModRet.setNumAnni(aaF-(aaI+1));

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
              siesLogger.debug("lModRet 1 : "+lModRet);
					} else {
              lModRet.setNumMesi(mmF-mmI);
              lModRet.setNumAnni(aaF-aaI);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
              siesLogger.debug("lModRet 3 : "+lModRet);
            }
				} else {
            mmI++;
					if (mmI > mmF) {
              lModRet.setNumMesi(12-mmI+mmF);
              lModRet.setNumAnni(aaF-(aaI+1));

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
              siesLogger.debug("lModRet 4 : "+lModRet);
					} else {
              lModRet.setNumMesi(mmF-mmI);
              lModRet.setNumAnni(aaF-aaI);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
              siesLogger.debug("lModRet 5 : "+lModRet);
            }
          }
       }
		} else {
      lModRet.setNumGiorni(ggF-ggI);
			if (mmI <= mmF) {
        lModRet.setNumMesi(mmF-mmI);
				if (Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) == lModRet
						.getNumGiorni()) {
					if (lModRet.getNumMesi() == 11) {
            lModRet.setNumGiorni(0);
            lModRet.setNumMesi(0);
            lModRet.setNumAnni(aaF+1-aaI);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
            siesLogger.debug("lModRet 6 : "+lModRet);
					} else {
            lModRet.setNumGiorni(0);
            lModRet.setNumMesi(lModRet.getNumMesi()+1);
            lModRet.setNumAnni(aaF-aaI);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
            siesLogger.debug("lModRet 7 : "+lModRet);
          }
				} else {
          lModRet.setNumAnni(aaF-aaI);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
          siesLogger.debug("lModRet 8 : "+lModRet);
        }
			} else {
				if (Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(aaI, mmI))) == lModRet
						.getNumGiorni()) {
          lModRet.setNumGiorni(0);
          lModRet.setNumMesi(12-mmI+mmF+1);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
          siesLogger.debug("lModRet 9 : "+lModRet);
				} else {
          lModRet.setNumMesi(12-mmI+mmF);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
          siesLogger.debug("lModRet 10 : "+lModRet);
        }

        lModRet.setNumAnni(aaF-(aaI+1));

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
        siesLogger.debug("lModRet 11 : "+lModRet);
      }
    }

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug("lModRet restituito : "+lModRet);

    return lModRet;
  }

  /*****************************************************************************
	 * Restituisce un nuovo CalendarModel ottenuto sommando (mGG,mMM,mAA) del aModelA con (mGG,mMM,mAA) del
	 * aModelB. I dati vengono restituiti normalizzati (ricalcolaGAM)<br>
	 * <br>
	 * n.b. Il calendar restituito contiene valorizzati i <b>soli</b> campi GG,MM,AA tutti gli altri campi
	 * sono valorizzati ai valori di default del CalendarModel (null, e 0)
	 */
	public CalendarModel sommaGiorni(CalendarModel aModelA, CalendarModel aModelB) {

    CalendarModel lModRet = new CalendarModel();
    
    lModRet.setNumAnni   (aModelA.getNumAnni()+aModelB.getNumAnni());
    lModRet.setNumMesi   (aModelA.getNumMesi()+aModelB.getNumMesi());
    lModRet.setNumGiorni (aModelA.getNumGiorni()+aModelB.getNumGiorni());
    
    return ricalcolaGAM(lModRet);
  }

  /**
	 * Valorizza mGG , mMM, mAA del aModel, sommando a questi i gg,mm,aa passati a parametro e i giorni di
	 * differenza tra datainizio e datafine.
   */
	public CalendarModel sommaGiornieIntervalloConRiporto(CalendarModel aModel, int gg, int mm, int aa) {

    int ggOld=aModel.getNumGiorni();
    int mmOld=aModel.getNumMesi();
    int aaOld=aModel.getNumAnni();
    CalendarModel lModRet=new CalendarModel(aModel);
    lModRet=CalcolaNumGiorniMesiAnni(lModRet);
    lModRet.setNumAnni(lModRet.getNumAnni()+aaOld+aa);
    lModRet.setNumMesi(lModRet.getNumMesi()+mmOld+mm);
    lModRet.setNumGiorni(lModRet.getNumGiorni()+ggOld+gg);
    return ricalcolaGAM(lModRet);
  }

  /**
	 * Valorizza mGG , mMM, mAA del aModel, sommando soltanto i gg,mm,aa passati a parametro e i giorni di
	 * differenza tra datainizio e datafine.
   */
	public CalendarModel sommaGiornieIntervallo(CalendarModel aModel, int gg, int mm, int aa) {

    CalendarModel lModRet=new CalendarModel(aModel);
    lModRet=CalcolaNumGiorniMesiAnni(lModRet);
    lModRet=ricalcolaGAM(lModRet);
    lModRet.setNumAnni(lModRet.getNumAnni()+aa);
    lModRet.setNumMesi(lModRet.getNumMesi()+mm);
    lModRet.setNumGiorni(lModRet.getNumGiorni()+gg);
    return ricalcolaGAM(lModRet);
  }

  /**
	 * Valorizza mGG , mMM, mAA del aModel, sommando a questi i gg,mm,aa passati a parametro.
   */
	public CalendarModel sommaGiorni(CalendarModel aModel, int gg, int mm, int aa) {
    int ggOld=aModel.getNumGiorni();
    int mmOld=aModel.getNumMesi();
    int aaOld=aModel.getNumAnni();
    CalendarModel lModRet=new CalendarModel();
    lModRet.setNumAnni(aaOld+aa);
    lModRet.setNumMesi(mmOld+mm);
    lModRet.setNumGiorni(ggOld+gg);
    return ricalcolaGAM(lModRet);
  }
  
  /*****************************************************************************
	 * Restituisce un Calendar ottenuto dalla differenza: (aModelA-aModelB) n.b. il metodo effettua la sola
	 * differenza dei quantum (GG, MM, AA), non opera sugli altri campi dei model in input (valute e sulle
	 * date)<br>
	 * <b>Attenzione: non utilizzare questo metodo se aModelA&lt;aModelB</b> infatti dati vengono restituiti
	 * con valori negativi ma non sempre in una forma corretta es: <br>
	 * aModelA = (10gg, 2mm, 2aa) e aModelB = (15gg, 2mm, 2aa) <br>
	 * Il model restituito è nella forma (25gg, 11mm, -1aa ) e non (-5gg, 0mm, 0aa) le due forme sono
	 * equivalenti ma la prima non è bella a vedersi e non esistono metodi per convertirla. ricalcolaGAM
	 * infatti non agisce su quantum negativi Per come vengono effettuati i calcoli, se aModelA&lt;aModelB,
	 * giorni e mesi risultano sempre positivi mentre è sempre l'anno che diviene negativo. Per cui si ottiene
	 * un quantum accettabile <b>SOLO</b> se i quantum in input sono espressi in anni: es aModelA=(0,0,3),
	 * aModelA=(0,0,5) differenza calcolata = (0,0,-2)
	 */
	public CalendarModel sottraiGiorni(CalendarModel aModelA, CalendarModel aModelB) {

    CalendarModel lModRet=new CalendarModel();
    
    int ggA=aModelA.getNumGiorni();
    int mmA=aModelA.getNumMesi();
    int aaA=aModelA.getNumAnni();
    
    int ggB=aModelB.getNumGiorni();
    int mmB=aModelB.getNumMesi();
    int aaB=aModelB.getNumAnni();
    
    if (ggA<ggB) {
        lModRet.setNumGiorni(ggA+30-ggB);
        mmB++;
    }  else
        lModRet.setNumGiorni(ggA-ggB);

    if (mmA<mmB) {
        lModRet.setNumMesi(mmA+12-mmB);
        aaB++;
    }else
        lModRet.setNumMesi(mmA-mmB);

     lModRet.setNumAnni(aaA-aaB);

    return lModRet;
  }

  /*****************************************************************************
	 * Ricalcola mGG , mMM, mAA di aModel riportando i giorni <30 e i mesi<12 (si presuppone che il model di
	 * input possa trovarsi in una situazione tipo 59 giorni , 21 mesi, 2 anni. In output viene prodotto :
	 * 29gg, 10mm,3anni) I conteggi sono effettuati usando gli algoritmi di calcolo della pena (USE CASE
	 * SIEP-UC-015-LV-AA) <b>Attenzione!! Non opera su quantum negativi. Per normalizzare un quantum negativo
	 * è necessario passare i dati combiati di segno</b>
   ****************************************************************************/
	public CalendarModel ricalcolaGAM(CalendarModel aModel) {

    CalendarModel lModRet=new CalendarModel(aModel);
    
    int gg = aModel.getNumGiorni();
    int mm = aModel.getNumMesi();
    int aa = aModel.getNumAnni();
    
		if (gg > 30) {
      int tmp = gg/30 ;
      mm+=tmp;
      gg-=tmp*30;
    }
    
		if (gg == 30) {
      mm++;
      gg=0;
    }
    
		if (mm > 12) {
      int tmp=mm/12;
      aa+=tmp;
      mm-=tmp*12;
    }
    
		if (mm == 12) {
      aa++;
      mm=0;
    }
    
    lModRet.setNumAnni(aa);
    lModRet.setNumMesi(mm);
    lModRet.setNumGiorni(gg);
    
    return lModRet;
  }

 /******************************************************************************
	 * Valorizza mGG , mMM, mAA del aModelA, sommando a questi mGG , mMM, mAA del aModelB. Vengono sommati
	 * inoltre anche Ammende e Multe Il risultato viene restituito normalizzato (ricalcolaGAM) I quantum
	 * devono essere positvi.
	 */
	public CalendarModel sommaGiornieValute(CalendarModel aModelA, CalendarModel aModelB) {

    CalendarModel lModRet=new CalendarModel();
    
    int lTotGiorniA = getTotGiorni(aModelA);
    int lTotGiorniB = getTotGiorni(aModelB);
    int lTotGiorni = lTotGiorniA + lTotGiorniB;
    
    lModRet.setImportoAmmenda (aModelA.getImportoAmmenda()+aModelB.getImportoAmmenda());
    lModRet.setImportoMulta   (aModelA.getImportoMulta()+aModelB.getImportoMulta());
    
    // Se i quantum in input sono <0 potrei ottenere un risultato con quantum
    // negativi. Per normalizzare i dati i quantum devono essere positivi
    if (lTotGiorni<0){
      CalendarModel lAppoModel = new CalendarModel();
      lAppoModel.setNumGiorni(-1*lTotGiorni);
      lAppoModel = ricalcolaGAM(lAppoModel);
      lModRet.setNumAnni   (-lAppoModel.getNumAnni());
      lModRet.setNumMesi   (-lAppoModel.getNumMesi());
      lModRet.setNumGiorni (-lAppoModel.getNumGiorni());
		} else {
      CalendarModel lAppoModel = new CalendarModel();
      lAppoModel.setNumGiorni(lTotGiorni);
      lAppoModel = ricalcolaGAM(lAppoModel);
      lModRet.setNumAnni   (lAppoModel.getNumAnni());
      lModRet.setNumMesi   (lAppoModel.getNumMesi());
      lModRet.setNumGiorni (lAppoModel.getNumGiorni());
    }
    
    return lModRet;
  }

 /******************************************************************************
	 * Valorizza mGG , mMM, mAA , sottranedo a quelli del aModelA mGG , mMM, mAA del aModelB.<br>
  *  Inoltre sottrae anche le valute (Multa, Ammenda)<br>
  *  Non utilizza DataInizio e DataFine del calendar <br>
	 * n.b Se aModelA&lt;aModelB allora mGG , mMM, mAA risultano negativi (tutti) e ErrorMsg="Swapped" Questo
	 * metodo si differenzia dal sottraiGiorni per il fatto che il quantum viene sempre restituito
	 * <b>normalizzato</b>, anche se la differenza porta a un quantum negativo. I risultati ottenutosono del
	 * tutto equivalenti. Es: aModelA = (10gg, 2mm, 2aa) < aModelB = (15gg, 2mm, 2aa) La sottraiGiorni
	 * restituisce (25gg, 11mm, -1aa ) mentre questo metodo restituisce (-5gg, 0mm, 0aa)
  *  
	 */
	public CalendarModel sottraiGiornieValute(CalendarModel aModelA, CalendarModel aModelB) {

    CalendarModel lModRet=new CalendarModel();
    
    int ggA=aModelA.getNumGiorni();
    int mmA=aModelA.getNumMesi();
    int aaA=aModelA.getNumAnni();
    
    int ggB=aModelB.getNumGiorni();
    int mmB=aModelB.getNumMesi();
    int aaB=aModelB.getNumAnni();
    
		if (!isGreater(aModelA, aModelB)) { // inverto i model
       ggA=aModelB.getNumGiorni();
       mmA=aModelB.getNumMesi();
       aaA=aModelB.getNumAnni();
       ggB=aModelA.getNumGiorni();
       mmB=aModelA.getNumMesi();
       aaB=aModelA.getNumAnni();
       lModRet.setErrorMsg("Swapped");
     }
    
    if (ggA<ggB) {
        lModRet.setNumGiorni(ggA+30-ggB);
        mmB++;
     }  else
        lModRet.setNumGiorni(ggA-ggB);

		if (mmA < mmB) {
        lModRet.setNumMesi(mmA+12-mmB);
        aaB++;
     }else
        lModRet.setNumMesi(mmA-mmB);

     lModRet.setNumAnni(aaA-aaB);
     
     lModRet.setImportoAmmenda(aModelA.getImportoAmmenda()-aModelB.getImportoAmmenda());
     lModRet.setImportoMulta(aModelA.getImportoMulta()-aModelB.getImportoMulta());
     
		if (lModRet.getErrorMsg().equals("Swapped")) {
       lModRet.setNumAnni(-lModRet.getNumAnni());
       lModRet.setNumMesi(-lModRet.getNumMesi());
       lModRet.setNumGiorni(-lModRet.getNumGiorni());
     }
    return lModRet;
  }
  
  /*****************************************************************************
	 * Effettua la differenza sia sui quantum che sugli importi tra aModelA e aModelB. (aModelA-aModelB) I
	 * conteggi sono effettuati usando gli algoritmi di calcolo della pena (USE CASE SIEP-UC-015-LV-AA)
	 *
	 * Se il quantum aModelA è minore del quantum di aModelB, il quantum ottenuto sarebbe negativo ma
	 * risultato viene restituito in valore assoluto (cioè aModelB-aModelA) con ErrorMsg="Swapped" (gli
	 * importi sono invece corretti)
	 *
   * @param aModelA
   * @param aModelB
   * @return
   ****************************************************************************/
	public CalendarModel BeneficisottraiGiornieValute(CalendarModel aModelA, CalendarModel aModelB) {

    CalendarModel lModRet = new CalendarModel();
    CalendarModel tmpA    = new CalendarModel();
    CalendarModel tmpB    = new CalendarModel();
    
    lModRet.setErrorMsg("");
    int ggA;
    int mmA;
    int aaA;
    int ggB;
    int mmB;
    int aaB;
    
		if (isZero(aModelA) && isZero(aModelB)) {
      lModRet.setImportoAmmenda (aModelA.getImportoAmmenda()-aModelB.getImportoAmmenda());
      lModRet.setImportoMulta   (aModelA.getImportoMulta()-aModelB.getImportoMulta());
      return lModRet;
    }

		if (isZero(aModelA)) {
      lModRet=new CalendarModel(aModelB);
      lModRet.setImportoAmmenda (aModelA.getImportoAmmenda()-aModelB.getImportoAmmenda());
      lModRet.setImportoMulta   (aModelA.getImportoMulta()-aModelB.getImportoMulta());
      lModRet.setErrorMsg("Swapped");
      return lModRet;
    }
    
		if (isZero(aModelB)) {
      lModRet=new CalendarModel(aModelA);
      lModRet.setImportoAmmenda(aModelA.getImportoAmmenda()-aModelB.getImportoAmmenda());
      lModRet.setImportoMulta(aModelA.getImportoMulta()-aModelB.getImportoMulta());
      return lModRet;
    }
    
       ggA = aModelA.getNumGiorni();
       mmA = aModelA.getNumMesi();
       aaA = aModelA.getNumAnni();
       ggB = aModelB.getNumGiorni();
       mmB = aModelB.getNumMesi();
       aaB = aModelB.getNumAnni();

      // normalizzo A in modo che gg>0, mm>0 se possibile
		if (ggA == 0 && mmA == 0) { // es:(aa,mm,gg)=(2,0,0) >> (1,11,30)
        ggA=30;
        mmA=11;
        aaA--;
      }
		if (ggA == 0 && mmA > 0) { // es:(aa,mm,gg)=(2,3,0) >> (2,2,30)
        ggA=30;
        mmA--;
      }
		if (ggA > 0 && mmA == 0 && aaA > 0) { // es:(aa,mm,gg)=(2,0,11) >> (1,11,41)
        ggA=ggA+30;
        mmA=11;
        aaA--;
      }
      
      // normalizzo B come A
		if (ggB == 0 && mmB == 0) {
        ggB=30;
        mmB=11;
        aaB--;
      }
		if (ggB == 0 && mmB > 0) {
        ggB=30;
        mmB--;
      }
		if (ggB > 0 && mmB == 0 && aaB > 0) {
        ggB=ggB+30;
        mmB=11;
        aaB--;
      }
     
     tmpA.setNumAnni(aaA);
     tmpA.setNumMesi(mmA);
     tmpA.setNumGiorni(ggA);
     tmpA.setImportoAmmenda(aModelA.getImportoAmmenda());
     tmpA.setImportoMulta(aModelA.getImportoMulta());
     
     tmpB.setNumAnni(aaB);
     tmpB.setNumMesi(mmB);
     tmpB.setNumGiorni(ggB);
     tmpB.setImportoAmmenda(aModelB.getImportoAmmenda());
     tmpB.setImportoMulta(aModelB.getImportoMulta());
     
		if (isGreater(tmpA, tmpB)) {
       lModRet = sottraiGiorni(tmpA, tmpB);
       lModRet.setErrorMsg("Normal");
		} else {
       lModRet = sottraiGiorni(tmpB, tmpA);
       lModRet.setErrorMsg("Swapped");
     }
    //}
     lModRet.setImportoAmmenda(tmpA.getImportoAmmenda()-tmpB.getImportoAmmenda());
     lModRet.setImportoMulta(tmpA.getImportoMulta()-tmpB.getImportoMulta());
    return lModRet;
  }

  /*****************************************************************************
	 * Verifica se il periodo (AA,MM,GG) del primo Calendar è STRETTAMENTE maggiore del periodo del
	 * secondo<br>
   * Non effettua controlli sugli altri campi.
	 *
   * @param aModelA
   * @param aModelB
   * @return true se aModelA>aModelB, false se aModelA<=aModelB 
	 */
	public boolean isGreater(CalendarModel aModelA, CalendarModel aModelB) {

    int ggA=aModelA.getNumGiorni();
    int mmA=aModelA.getNumMesi();
    int aaA=aModelA.getNumAnni();
    
    int ggB=aModelB.getNumGiorni();
    int mmB=aModelB.getNumMesi();
    int aaB=aModelB.getNumAnni();
    
    int lTotGiorniA = ggA + (mmA*30) + (aaA*12*30); 
    int lTotGiorniB = ggB + (mmB*30) + (aaB*12*30);
    if (lTotGiorniA>lTotGiorniB){
      return true;
		} else {
      return false;
    }
  }

  /*****************************************************************************
	 * Restituisce true se giorni, mesi e anni del Calendar sono tutti nulli Non effettua controlli sugli
	 * altri campi.
	 *
   * @param aModelA
   * @return 
	 */
	public boolean isZero(CalendarModel aModelA) {

    return (aModelA.getNumGiorni()==0 && aModelA.getNumMesi()==0 && aModelA.getNumAnni()==0);
  }

	public CalendarModel toJsp(CalendarModel aModelA) {

		if (aModelA.getNumGiorni() == 30) {
      aModelA.setNumGiorni(0);
      aModelA.setNumMesi(aModelA.getNumMesi()+1);
    }
		if (aModelA.getNumMesi() == 12) {
      aModelA.setNumMesi(0);
      aModelA.setNumAnni(aModelA.getNumAnni()+1);
    }

    return aModelA;
  }

  /*****************************************************************************
	 * Verifica se GG. MM e AA sono tutti >=0 Non effettua controlli sugli altri campi.
	 *
   * @param aModel
   * @return
	 */
	public boolean isPositiveTime(CalendarModel aModel) {

    return (aModel.getNumAnni()>=0 && aModel.getNumMesi()>=0 && aModel.getNumGiorni()>=0);
  }
  
  /**
	 * Metodo che effettua la differenza tra quantum passati in input con i due CalendaModel: aModelA-aModelB.
	 * Il risultato viene restituito su un terzo calendar. I model in input NON vengono modificati, il
	 * risultato può essere negativo es (-5 giorni, -1 mese, -1 anno) Questo metodo opera solo sui quantum. I
	 * Calendar passati in input possono avere qualsiasi segno. Il risultato è normalizzato
	 *
   * @param aModelA
   * @param aModelB
   * @return
   */
	public CalendarModel sottraiGiorniNew(CalendarModel aModelA, CalendarModel aModelB) {

    CalendarModel lModRet=new CalendarModel();
    
    int ggA=aModelA.getNumGiorni();
    int mmA=aModelA.getNumMesi();
    int aaA=aModelA.getNumAnni();
    
    int ggB=aModelB.getNumGiorni();
    int mmB=aModelB.getNumMesi();
    int aaB=aModelB.getNumAnni();

    int lTotGiorniA = ggA + (mmA*30) + (aaA*12*30); 
    int lTotGiorniB = ggB + (mmB*30) + (aaB*12*30);
    int lDiff = lTotGiorniA - lTotGiorniB;

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
//    siesLogger.debug("lTotGiorniA = "+lTotGiorniA);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
//    siesLogger.debug("lTotGiorniB = "+lTotGiorniB);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
//    siesLogger.debug("lDiff = "+lDiff);
    
    if (lDiff<0){
      // La funzione di normalizzazione non lavora su quantum negativi per
      // cui devo combiare di segno prima di normalizzare e dopo la normalizzazione
      lModRet.setNumGiorni(-1*lDiff);
      lModRet = ricalcolaGAM(lModRet);
      lModRet.setNumAnni   (-lModRet.getNumAnni());
      lModRet.setNumMesi   (-lModRet.getNumMesi());
      lModRet.setNumGiorni (-lModRet.getNumGiorni());
		} else {
      lModRet.setNumGiorni(lDiff);
      lModRet = ricalcolaGAM(lModRet);
    } 

    return lModRet;
  }  


  /**
	 * Metodo che effettua la differenza tra quantum passati in input con i due CalendaModel: aModelA-aModelB.
	 * Il risultato viene restituito su un terzo calendar. I model in input NON vengono modificati, il
	 * risultato può essere negativo es (-5 giorni, -1 mese, -1 anno) Questo metodo NON opera solo sui
	 * quantum. I Calendar passati in input possono avere qualsiasi segno. Il risultato è normalizzato
	 *
   * @param aModelA
   * @param aModelB
   * @return
   */
	public CalendarModel sottraiGiorniValuteNew(CalendarModel aModelA, CalendarModel aModelB) {

    CalendarModel lModRet = new CalendarModel();
    
    lModRet = sottraiGiorniNew (aModelA,aModelB);
    
    lModRet.setImportoMulta   (aModelA.getImportoMulta()   - aModelB.getImportoMulta());
    lModRet.setImportoAmmenda (aModelA.getImportoAmmenda() - aModelB.getImportoAmmenda());

    return lModRet;
  }
  
  /**
	 * Ritorna il quantum in giorni applicando la seguente conversione: 1 mese = 30 gg 1 anno = 12 mesi x 30
	 * gg = 360gg
	 *
   * @param aModelA
   * @return
   */
  public static int getTotGiorni (CalendarModel aModelA){
    int ggA=aModelA.getNumGiorni();
    int mmA=aModelA.getNumMesi();
    int aaA=aModelA.getNumAnni();
    
    int lTotGiorniA = ggA + (mmA*30) + (aaA*12*30); 
   
    return lTotGiorniA;
  }

  /**
   * Restituisce un calendar con i quantum in valore assoluto 
	 *
   * @param aCalModel
   * @return
   */
  public CalendarModel abs(CalendarModel aCalModel) {
    CalendarModel lCalModel = new CalendarModel();
    
    lCalModel.setNumAnni   (Math.abs (aCalModel.getNumAnni()));
    lCalModel.setNumMesi   (Math.abs (aCalModel.getNumMesi()));
    lCalModel.setNumGiorni (Math.abs (aCalModel.getNumGiorni()));
    
    return lCalModel;
  }
}