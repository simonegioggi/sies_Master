package siap.siep.calcolopena.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * CalcoloPenaDL92Model - Classe Model per la calcolatrice
 * 
 * @since MEV_2024-092
 */
public class CalcoloPenaDL92Model extends GenericModel {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1102882424352691224L;

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;
	
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;

	private BigDecimal mNumAnniPresofferto;
	private BigDecimal mNumMesiPresofferto;
	private BigDecimal mNumGiorniPresofferto;

	private String mPosizioneGiuridica;
	private Date mDataInizioPena;
	
	// Dati calcolati
	private SemestreDL92Model mSemestrePresofferto = null;
	private Vector<SemestreDL92Model> mListaSemetri = new Vector<>();
	
	// Totali LA
	private BigDecimal mTotLAMaturabili;
	private BigDecimal mTotLAApplicabili;
	private BigDecimal mTotLAFungibili;

	// Fine pena iniziale
	private Date mDataScarcerazioneNoLA; 
  // Date effettiva scarcerazione con sole LA applicabili
	private Date mDataScarcerazioneLAFung; 
  // Date scarcerazione teorica applicando tutte le LA maturate
	private Date mDataScarcerazioneLANoFung; 
	// ?? data fine pena dal Penultimo semestre ( a che serve????)
	private Date mDataScarcerazionePenultimoSemestre;

	// Indica se il semestre con progressivo x è considerato valido ai fini della concessione delle LA
	private Hashtable <String, String> mListaIsCompresa = new Hashtable <String, String>();

  /**
   * 
   */
	public CalcoloPenaDL92Model() {
	}
	
	// Metodi Getter
	public BigDecimal getNumAnniReclusione() {
		return mNumAnniReclusione;
	}

	public BigDecimal getNumMesiReclusione() {
		return mNumMesiReclusione;
	}

	public BigDecimal getNumGiorniReclusione() {
		return mNumGiorniReclusione;
	}

	public BigDecimal getImportoMulta() {
		return mImportoMulta;
	}

	public BigDecimal getNumAnniArresto() {
		return mNumAnniArresto;
	}

	public BigDecimal getNumMesiArresto() {
		return mNumMesiArresto;
	}

	public BigDecimal getNumGiorniArresto() {
		return mNumGiorniArresto;
	}

	public BigDecimal getImportoAmmenda() {
		return mImportoAmmenda;
	}

	public BigDecimal getNumAnniPresofferto() {
		return mNumAnniPresofferto;
	}

	public BigDecimal getNumMesiPresofferto() {
		return mNumMesiPresofferto;
	}

	public BigDecimal getNumGiorniPresofferto() {
		return mNumGiorniPresofferto;
	}

	public String getPosizioneGiuridica() {
		return mPosizioneGiuridica;
	}

	public Date getDataInizioPena() {
		return mDataInizioPena;
	}

	public SemestreDL92Model getSemestrePresofferto() {
		return mSemestrePresofferto;
	}

	public Vector<SemestreDL92Model> getListaSemetri() {
		return mListaSemetri;
	}

	public BigDecimal getTotLAMaturabili() {
		return mTotLAMaturabili;
	}

	public BigDecimal getTotLAApplicabili() {
		return mTotLAApplicabili;
	}

	public BigDecimal getTotLAFungibili() {
		return mTotLAFungibili;
	}

	public Date getDataScarcerazioneNoLA() {
		return mDataScarcerazioneNoLA;
	}

	public Date getDataScarcerazioneLAFung() {
		return mDataScarcerazioneLAFung;
	}

	public Date getDataScarcerazioneLANoFung() {
		return mDataScarcerazioneLANoFung;
	}

	public Date getDataScarcerazionePenultimoSemestre() {
		return mDataScarcerazionePenultimoSemestre;
	}
	
	public Hashtable <String, String> getListaIsCompresa () {
		return mListaIsCompresa;
	}	
	
  // Metodi Setter
  public void setNumAnniReclusione(BigDecimal mNumAnniReclusione) {
    this.mNumAnniReclusione = mNumAnniReclusione;
  }
  public void setNumMesiReclusione(BigDecimal mNumMesiReclusione) {
    this.mNumMesiReclusione = mNumMesiReclusione;
  }
  public void setNumGiorniReclusione(BigDecimal mNumGiorniReclusione) {
    this.mNumGiorniReclusione = mNumGiorniReclusione;
  }
	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}
  public void setNumAnniArresto(BigDecimal mNumAnniArresto) {
    this.mNumAnniArresto = mNumAnniArresto;
  }
  public void setNumMesiArresto(BigDecimal mNumMesiArresto) {
    this.mNumMesiArresto = mNumMesiArresto;
  }
  public void setNumGiorniArresto(BigDecimal mNumGiorniArresto) {
    this.mNumGiorniArresto = mNumGiorniArresto;
  }
	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}
    
  public void setNumAnniPresofferto(BigDecimal mNumAnniPresofferto) {
    this.mNumAnniPresofferto = mNumAnniPresofferto;
  }
  public void setNumMesiPresofferto(BigDecimal mNumMesiPresofferto) {
    this.mNumMesiPresofferto = mNumMesiPresofferto;
  }
  public void setNumGiorniPresofferto(BigDecimal mNumGiorniPresofferto) {
    this.mNumGiorniPresofferto = mNumGiorniPresofferto;
  }
  
  public void setPosizioneGiuridica (String mPosizioneGiuridica) { 
  	this.mPosizioneGiuridica = mPosizioneGiuridica;  
  }
  
  public void setDataInizioPena (Date mDataInizioPena) { 
  	this.mDataInizioPena = mDataInizioPena;  
  }
  
  public void setSemestrePresofferto (SemestreDL92Model mSemestrePresofferto) {
  	this.mSemestrePresofferto = mSemestrePresofferto; 
  }
  
  public void setListaSemetri (Vector<SemestreDL92Model> mListaSemetri) {
    this.mListaSemetri = mListaSemetri;
  }

	public void setTotLAMaturabili(BigDecimal mTotLAMaturabili) {
		this.mTotLAMaturabili = mTotLAMaturabili;
	}

	public void setTotLAApplicabili(BigDecimal mTotLAApplicabili) {
		this.mTotLAApplicabili = mTotLAApplicabili;
	}

	public void setTotLAFungibili(BigDecimal mTotLAFungibili) {
		this.mTotLAFungibili = mTotLAFungibili;
	}
	
	public void setDataScarcerazioneNoLA (Date mDataScarcerazioneNoLA) {
		this.mDataScarcerazioneNoLA = mDataScarcerazioneNoLA;
	}

	public void setDataScarcerazioneLAFung (Date mDataScarcerazioneLAFung) {
		this.mDataScarcerazioneLAFung = mDataScarcerazioneLAFung;
	}

	public void setDataScarcerazioneLANoFung (Date mDataScarcerazioneLANoFung) {
		this.mDataScarcerazioneLANoFung = mDataScarcerazioneLANoFung;
	} 
	
	public void setDataScarcerazionePenultimoSemestre (Date mDataScarcerazionePenultimoSemestre) {
		this.mDataScarcerazionePenultimoSemestre = mDataScarcerazionePenultimoSemestre;
	}
	
	public void setListaIsCompresa (Hashtable <String, String> mListaIsCompresa)   {
		this.mListaIsCompresa = mListaIsCompresa;
	}	
	
  /**
	 * calcolaPenaVirtuale
   */
	@SuppressWarnings("unchecked")
  public void calcolaPenaVirtuale() throws Exception {
  	CalendarUtil lCalUtil = new CalendarUtil();
		
		CalendarModel lCalModPresofferto = new CalendarModel();
		lCalModPresofferto.setNumAnni   (this.mNumAnniPresofferto);
		lCalModPresofferto.setNumMesi   (this.mNumMesiPresofferto);
		lCalModPresofferto.setNumGiorni (this.mNumGiorniPresofferto);
		siesLogger.debug("Presofferto: "+lCalModPresofferto);
		
		CalendarModel lCalModTot = new CalendarModel();
		// lCalModTot = lCalUtil.sommaGiornieValute(lCalModRec, lCalModArr);
		lCalModTot = getTotaleDaEseguire();
		siesLogger.debug("Totale: "+lCalModTot);

		// Pena da eseguire al netto del presofferto
		//lCalModTot = lCalUtil.sottraiGiornieValute(lCalModTot, lCalModPresofferto);
		siesLogger.debug("Totale al netto del presofferto: "+lCalModTot);	
		
		// Calendar che rappresenta il Semestre
		CalendarModel lCalendarSemestre = new CalendarModel();
		lCalendarSemestre.setNumMesi(new BigDecimal(6));
		
	  // Calendar che rappresenta i 45 gg di LA
		CalendarModel lCalendarLA_Compreso = new CalendarModel();
		lCalendarLA_Compreso.setNumGiorni(new BigDecimal(45));
		lCalendarLA_Compreso = lCalUtil.ricalcolaGAM(lCalendarLA_Compreso);
		
		CalendarModel lCalendarLA_Escluso = new CalendarModel();
		lCalendarLA_Escluso.setNumGiorni(new BigDecimal(0));
		lCalendarLA_Escluso = lCalUtil.ricalcolaGAM(lCalendarLA_Escluso);
		
		// Il presofferto posso calcolarlo fuori ciclo sia per Detenuto che libero
		int lGiorniResiduiPresofferto = 0;
		mSemestrePresofferto = new SemestreDL92Model ();
		mSemestrePresofferto.setIsPresofferto("S");
		mSemestrePresofferto.setProgressivo(new BigDecimal(0));
		if (!lCalUtil.isZero(lCalModPresofferto)) {
			// Devo calcolare i semetri Utili, e le LA totali
			siesLogger.debug("Calcolo Semestri utili Presofferto e LA maturate");	
			int lTotGiorni = CalendarUtil.getTotGiorni(lCalModPresofferto);
			int lSemestriUtiliPresofferto = lTotGiorni / 180;
			siesLogger.debug("Semestri Utili = "+lSemestriUtiliPresofferto);
			int lTotLAPresofferto = lSemestriUtiliPresofferto * 45;
			siesLogger.debug("LA Maturate = "+lTotLAPresofferto);
			
			// Calcolo i quantum residui dopo aver scalato dalla pena totale i giorni di LA maturati in
			// presofferto
			CalendarModel lQuantumLAMaturati = new CalendarModel();
			lQuantumLAMaturati.setNumGiorni(new BigDecimal(lTotLAPresofferto));
			lQuantumLAMaturati = lCalUtil.ricalcolaGAM (lQuantumLAMaturati);
			lCalModTot = lCalUtil.sottraiGiornieValute (lCalModTot, lQuantumLAMaturati);
			siesLogger.debug("Residuo dopo aver scalato le LA maturate in presofferto  = "+lCalModTot);
			
			// 
			// Valorizzo i giorni di presofferto non calcolati come utili per la matutrazione del sementre ma
			// che devono
			// essere scalati dal primo calcolo sui semestri di pena
			lGiorniResiduiPresofferto = lTotGiorni - (lSemestriUtiliPresofferto * 180);
			siesLogger.debug("Giorni residui presofferto = "+lGiorniResiduiPresofferto);
			
			mSemestrePresofferto.setNumSemestriMaturati(new BigDecimal(lSemestriUtiliPresofferto));			
			mSemestrePresofferto.setLAApplicate      (new BigDecimal(lTotLAPresofferto));
			mSemestrePresofferto.setResiduoNumAnni   (new BigDecimal (lCalModTot.getNumAnni()));
			mSemestrePresofferto.setResiduoNumMesi   (new BigDecimal (lCalModTot.getNumMesi()));
			mSemestrePresofferto.setResiduoNumGiorni (new BigDecimal (lCalModTot.getNumGiorni()));
			mSemestrePresofferto.setGiorniResiduiPresofferto(new BigDecimal (lGiorniResiduiPresofferto));
		} else {
			mSemestrePresofferto.setNumSemestriMaturati (new BigDecimal (0));
			mSemestrePresofferto.setLAApplicate      (new BigDecimal (0));
			mSemestrePresofferto.setResiduoNumAnni   (new BigDecimal (lCalModTot.getNumAnni()));
			mSemestrePresofferto.setResiduoNumMesi   (new BigDecimal (lCalModTot.getNumMesi()));
			mSemestrePresofferto.setResiduoNumGiorni (new BigDecimal (lCalModTot.getNumGiorni()));
			mSemestrePresofferto.setGiorniResiduiPresofferto (new BigDecimal (0));  	
		}
		
  	// Libero
  	if ("L".equals(mPosizioneGiuridica)) {
  		siesLogger.debug("Calcolo Pena Virtuale: LIBERO");	
  		
  		// Calcolo fuori ciclo per l'eventuale residuo di presofferto
  		if (!lCalUtil.isZero(lCalModPresofferto)) {  			
				// Teoricamente devo riaggiungere al totale i giorni di presofferto in eccesso dei quantum di
				// pena
  			lCalModTot = lCalUtil.sommaGiorni(lCalModTot,lGiorniResiduiPresofferto,0,0);
  			siesLogger.debug("Nuovo totale = "+lCalModTot);
  		}
  		
  		boolean bContinua = true;
  		int lProgSemestre = 0;
  		while (bContinua) {
  			// Verifico se c'è capienza per un semestre ovvero per maturare i 45gg di LA
  			CalendarModel lResiduo = lCalUtil.sottraiGiornieValute(lCalModTot, lCalendarSemestre);
				// lLaApplicate = default 45 per semestre tranne eventualmente sull'utimo in mancanza di
				// capienza
  			//
  			BigDecimal lLaApplicate = new BigDecimal(45);
  			CalendarModel lCalendarLA = lCalendarLA_Compreso;
  			String lIsCompreso = "S";
  			String idSemestre = "prgSemestre_"+(lProgSemestre+1);
  			
  			if ( mListaIsCompresa.get(idSemestre)!=null && "N".equals(mListaIsCompresa.get(idSemestre)) ) {
  				lCalendarLA = lCalendarLA_Escluso;
  			  lLaApplicate = new BigDecimal(0);
  			  lIsCompreso = "N";
  			}  			
  			
  			if (lCalUtil.isPositiveTime(lResiduo) ) {
  				siesLogger.debug(lProgSemestre+") Ho capienza per un altro semestre"); 
	  			// Ho capienza per un altro semestre sotraggo
	  			// Calcolo la nuova pena residua (lCalModTot=lCalModTot-lCalendarSemestre-lCalendarLA)
	  			lCalModTot = lCalUtil.sottraiGiornieValute(lCalModTot, lCalendarSemestre);
	  			siesLogger.debug("Dopo sottrazione Semestre: "+lCalModTot);
	  			
					// verifico se posso sottrerre tutti i 45 gg dal residuo ovvero se posso usufruire di
					// tutte le LA maturate
	  			lResiduo = lCalUtil.sottraiGiornieValute(lCalModTot, lCalendarLA);
	  			if (lCalUtil.isPositiveTime (lResiduo) ) {
	  				// Posso scalare tutte le LA maturate
	  				siesLogger.debug(lProgSemestre+") Posso scalare tutte le LA maturate"); 
	  				lCalModTot = lCalUtil.sottraiGiornieValute (lCalModTot, lCalendarLA);
	  				siesLogger.debug("Dopo sottrazione $% LA: "+lCalModTot);
					} else {
	  				siesLogger.debug(lProgSemestre+")  Posso scalare solo una parte delle LA maturate"); 
	  				// Posso scalare solo una parte delle LA. lCalModTot va a zero
						lLaApplicate = new BigDecimal(CalendarUtil.getTotGiorni(lCalModTot));
	  				lCalModTot = new CalendarModel(); // azzero il model
	  			}
				} else {
					siesLogger.debug(
							lProgSemestre + ") Non ho altri semestri utili per il calcolo delle LA. Esco");
  				// Non ho altri semestri utili per il calcolo delle LA. Esco
  				break; 
  			}
  			
  			lProgSemestre++;
				SemestreDL92Model lSemetreUtile = new SemestreDL92Model();
				lSemetreUtile.setProgressivo         (new BigDecimal (lProgSemestre));
				lSemetreUtile.setNumSemestriMaturati (new BigDecimal (1));
				lSemetreUtile.setResiduoNumAnni      (new BigDecimal (lCalModTot.getNumAnni()));
				lSemetreUtile.setResiduoNumMesi      (new BigDecimal (lCalModTot.getNumMesi()));
				lSemetreUtile.setResiduoNumGiorni    (new BigDecimal (lCalModTot.getNumGiorni()));
				lSemetreUtile.setLAApplicate         (lLaApplicate);
				lSemetreUtile.setIsCompreso          (lIsCompreso);
				
				mListaSemetri.add(lSemetreUtile);
  		}
		} else if ("D".equals(mPosizioneGiuridica)) {
  		siesLogger.debug("Calcolo Pena Virtuale: DETENUTO");  		
			siesLogger.debug(
					"Data Inizio Pena " + DateUtils.getDateToString(this.mDataInizioPena, "dd/MM/yyyy"));
  		// Per il detenuto vengono calcolate decorrenza e scadenza dei semetri
  		
			// Calcolo il fine pena applicando tutti i quantum
			PenaResiduaModel lPenaIniziale = new PenaResiduaModel();
			// n.b. non posso usare i quantum sul presofferto perchè già decurtati delle LA. 
			// Devo usare il totale iniziale e poi anticipare il fine pena 
			lPenaIniziale.setQuantumReclusione (this.getTotaleDaEseguire()); 
			
			ICalcoloPena lCalPenCtrl = SIEPLookupRemote.getCalcoloPenaRemote();
			Vector<Date> lDateFine = lCalPenCtrl.exCalcolaDataFinePena(this.mDataInizioPena, lPenaIniziale,
					true);
			Date lDataFinePena = lDateFine.elementAt(0);
			siesLogger.debug("Data Fine senza LA = " + DateUtils.getDateToString(lDataFinePena, "dd/MM/yyyy"));
			
			// Data senza applicare LA (nemmeno quelle del presofferto)
			mDataScarcerazioneNoLA = lDataFinePena;
			
  		// Gestione Presofferto
  		if (!lCalUtil.isZero (lCalModPresofferto)) { 
  			// Se maturate LA devo anticipare il fine pena
  			if (mSemestrePresofferto.getLAApplicate().intValue()>0) {
					siesLogger.debug("Presenti " + mSemestrePresofferto.getLAApplicate().intValue()
							+ " LA maturate in presofferto anticipo subito il fine pena.");
					lDataFinePena = DateUtils.moveDateTo(lDataFinePena, Calendar.DAY_OF_MONTH,
							-mSemestrePresofferto.getLAApplicate().intValue());
					siesLogger.debug("Nuova Data Fine con LA presofferto = "
							+ DateUtils.getDateToString(lDataFinePena, "dd/MM/yyyy"));
  			}
  		}
  		mSemestrePresofferto.setNuovaDataScadenzaPena(lDataFinePena);
  		
  		boolean bContinua = true;
  		int lProgSemestre = 0;
  		Date lDataMaturazioneSemestre = this.mDataInizioPena;
  		Date lNuovaDataFinePena = null;
  		
  		while (bContinua) {
  			// Data Maturazione Semestre
  			lDataMaturazioneSemestre = DateUtils.moveDateTo (lDataMaturazioneSemestre, Calendar.MONTH, 6);
				siesLogger.debug(lProgSemestre + ") lDataMaturazioneSemestre = "
						+ DateUtils.getDateToString(lDataMaturazioneSemestre, "dd/MM/yyyy"));

				// Se del presofferto avanzavano giorni, anticipo la data di maturazione del primo semestre
				// dei giorni
  			// che erano avanzati
  			if (lGiorniResiduiPresofferto > 0) {
					siesLogger.debug("Presenti " + lGiorniResiduiPresofferto
							+ " giorni di presofferto residui. anticipo la data di maturazione del primo semestre...");
					lDataMaturazioneSemestre = DateUtils.moveDateTo(lDataMaturazioneSemestre,
							Calendar.DAY_OF_MONTH, -lGiorniResiduiPresofferto);
					siesLogger.debug(lProgSemestre + ") Nuova lDataMaturazioneSemestre = "
							+ DateUtils.getDateToString(lDataMaturazioneSemestre, "dd/MM/yyyy"));
  				lGiorniResiduiPresofferto = 0; //Lo azzero per i successivi semestri
  			}
  			
  			// Memorizzo la data scarcerazione del precedente semestre  			
  			mDataScarcerazionePenultimoSemestre = lDataFinePena;  /// ????????????????????
  			
  			// Verifico se la data maturazione cade nel fine pena
  			if (DateUtils.isGreater (lDataMaturazioneSemestre, lDataFinePena)) {
					siesLogger.debug(
							lProgSemestre + ") Non ho altri semestri utili per il calcolo delle LA. Esco");
  				break;
  			}
  			
  			// Nuova gestione dei periodi (ESCLUSO/COMPRESO)
  			BigDecimal lLaApplicate = new BigDecimal(45); //default
  			String lIsCompreso = "S";
  			String idSemestre = "prgSemestre_"+(lProgSemestre+1);
  			
  			if ( mListaIsCompresa.get(idSemestre)!=null && "N".equals(mListaIsCompresa.get(idSemestre)) ) {
  			  lLaApplicate = new BigDecimal(0);
  			  lIsCompreso = "N";
  			}  			

  			
  			// Nuovo fine pena sottraendo i 45 gg maturati
  			lNuovaDataFinePena = DateUtils.moveDateTo (lDataFinePena, Calendar.DAY_OF_MONTH, -lLaApplicate.intValue());
				siesLogger.debug(lProgSemestre + ") lNuovaDataFinePena = "
						+ DateUtils.getDateToString(lNuovaDataFinePena, "dd/MM/yyyy"));
	  		
  			// Fine pena applicando tutte le LA maturate 
  			mDataScarcerazioneLANoFung = lNuovaDataFinePena;
  			
  			// Verifico se posso applicare Tutti i 45 gg di LA maturati
  			//BigDecimal lLaApplicate = new BigDecimal(45); //default
  			if (DateUtils.isGreater (lDataMaturazioneSemestre, lNuovaDataFinePena)) {
  				// Non posso sottrarre tutti i 45 gg ma solo una parte
					siesLogger.debug(lProgSemestre
							+ ") non posso applicare tutte le LA, non ho capienza sul fne pena ");
					// Posso applicar solo le LA che vanno dal giorno di maturazione del semetre al precedente
					// fine pena
					lLaApplicate = new BigDecimal(
							DateUtils.getIntervallo(lDataMaturazioneSemestre, lDataFinePena));
  				// scarcerato il giorno di maturazione
  				lNuovaDataFinePena = lDataMaturazioneSemestre;
				} else {
  				lDataFinePena = lNuovaDataFinePena;
  			}
  			siesLogger.debug(lProgSemestre+") lLaApplicate = "+lLaApplicate);
  			
  			// Fine pena effettivo applicando solo le LA dove c'è capienza 
  			mDataScarcerazioneLAFung = lNuovaDataFinePena;
  			
  			/*
				 * // Calcolo i nuovi quantum = quantum precedenti - 6 mesi - 45 gg lCalModTot =
				 * lCalUtil.sottraiGiornieValute (lCalModTot, lCalendarSemestre); lCalModTot =
				 * lCalUtil.sottraiGiornieValute (lCalModTot, lCalendarLA); if
				 * (!lCalUtil.isPositiveTime(lCalModTot) ) lCalModTot = new CalendarModel(); // azzero
  			*/
  			
  			// seconda opzione. Calcoli quantum cone diff tra nuova data fine e data maturazione +1g
  			//CalendarModel lCalPenaDaEspiare = new CalendarModel();
				lCalModTot.setDataInizio(
						DateUtils.moveDateTo(lDataMaturazioneSemestre, Calendar.DAY_OF_MONTH, +1));
  			lCalModTot.setDataFine   (lNuovaDataFinePena);
  			lCalModTot = lCalUtil.CalcolaNumGiorniMesiAnni(lCalModTot, false);
  			if (!lCalUtil.isPositiveTime(lCalModTot) ) 
  				lCalModTot = new CalendarModel(); // azzero
  			
  			siesLogger.debug(lProgSemestre+") nuovo residuo = "+lCalModTot); 
  			
  			// Aggiungo il semestre
  			lProgSemestre++;
				SemestreDL92Model lSemetreUtile = new SemestreDL92Model();
				lSemetreUtile.setProgressivo         (new BigDecimal (lProgSemestre));
				lSemetreUtile.setNumSemestriMaturati (new BigDecimal (1));
				lSemetreUtile.setResiduoNumAnni      (new BigDecimal (lCalModTot.getNumAnni()));
				lSemetreUtile.setResiduoNumMesi      (new BigDecimal (lCalModTot.getNumMesi()));
				lSemetreUtile.setResiduoNumGiorni    (new BigDecimal (lCalModTot.getNumGiorni()));
				lSemetreUtile.setLAApplicate         (lLaApplicate);
				lSemetreUtile.setIsCompreso          (lIsCompreso);
				
				lSemetreUtile.setDataMaturazioneLA     (lDataMaturazioneSemestre);
				lSemetreUtile.setNuovaDataScadenzaPena (lNuovaDataFinePena);				
				
				mListaSemetri.add(lSemetreUtile);

  		}
  	}
  	
    siesLogger.debug("Totale semestri calcolati: "+mListaSemetri.size());  
  }
  

  /**
   * Somma reclusione e arresti e restituisce un calendar con la pena totale
	 *
   * @return
   */
  public CalendarModel getTotaleDaEseguire () {
  	CalendarUtil lCalUtil = new CalendarUtil();
  	
  	// Reclusione
		CalendarModel lCalModRec = new CalendarModel();
		lCalModRec.setNumAnni   (this.mNumAnniReclusione);
		lCalModRec.setNumMesi   (this.mNumMesiReclusione);
		lCalModRec.setNumGiorni (this.mNumGiorniReclusione);
		
		//siesLogger.debug("Reclusione: "+lCalModRec);
		
	  // Arresti
		CalendarModel lCalModArr = new CalendarModel();
		lCalModArr.setNumAnni   (this.mNumAnniArresto);
		lCalModArr.setNumMesi   (this.mNumMesiArresto);
		lCalModArr.setNumGiorni (this.mNumGiorniArresto);
		
		//siesLogger.debug("Arresto: "+lCalModArr);
		
		CalendarModel lCalModTot = new CalendarModel();
		lCalModTot = lCalUtil.sommaGiornieValute(lCalModRec, lCalModArr);
		//siesLogger.debug("Totale: "+lCalModTot);
		return lCalModTot;
  }
  
  //===================================================================================
  // Metodi get che calcolano i dati da visualizzare. 
  // Comodi nel caso delle stampe per evitare di fare i calcoli nei template 
  //===================================================================================
  
  /**
   * Ritorna il totale della LA maturate come prodotto del numero di semestri utili per 45gg
   * Nota solo le LA sulla solo pena da sconare quindi non conteggiano le LA sul presofferto
   * @return
   */
  public BigDecimal getLAMaturate () {
  	int lTotLAMaturata = 0;
  	
  	//if (mSemestrePresofferto!=null && mSemestrePresofferto.getLAApplicate()!=null)
  	//	lTotLAMaturata = mSemestrePresofferto.getLAApplicate().intValue();
  	
  	// lTotLAMaturata += mListaSemetri.size()*45;
  	lTotLAMaturata +=getSemestriUtiliPenaScontata().intValue()*45;
  	
  	return new BigDecimal(lTotLAMaturata);
  }
  
  /**
	 * Ritorna il totale della LA applicata come la somma delle LA applicate sui singoli semestri L'ultimo
	 * potrebbe essere < 45gg
	 *
   * @return
   */
  public BigDecimal getLAApplicate () {
  	BigDecimal lTotLAApplicata = new BigDecimal(0);

  	if (mSemestrePresofferto!=null && mSemestrePresofferto.getLAApplicate()!=null)
  		lTotLAApplicata = lTotLAApplicata.add(mSemestrePresofferto.getLAApplicate());
  	
  	for (int i = 0; i<mListaSemetri.size(); i++) {
  		SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);
  		lTotLAApplicata = lTotLAApplicata.add (lSemestreUtile.getLAApplicate());
  	}
  	
  	return lTotLAApplicata;
  }
  
  public BigDecimal getLANonConcesse () {
  	BigDecimal lTotLANonConcesse = new BigDecimal(0);

  	//if (mSemestrePresofferto!=null && mSemestrePresofferto.getLAApplicate()!=null)
  	//	lTotLAApplicata = lTotLAApplicata.add(mSemestrePresofferto.getLAApplicate());
  	
  	for (int i = 0; i<mListaSemetri.size(); i++) {
  		SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);
  		if ("N".equals(lSemestreUtile.getIsCompreso()))
  			lTotLANonConcesse = lTotLANonConcesse.add (new BigDecimal(45));
  	}
  	
  	return lTotLANonConcesse;
  }
  
  /**
   * Ritorna le LA non fruibili (fungibili) come differenza tra quelle maturate e quelle applicate
   * escludendo comunque i semestri non concessi
	 *
   * @return
   */
  public BigDecimal getLAFungibili () {
  	BigDecimal lTotLAFungibile = null;

  	// lTotLAFungibile= getLAMaturate().subtract(getLAApplicate());
  	lTotLAFungibile= getLAMaturate().subtract(getLAApplicate());
  	
  	// Le LA non concesso non sono ovviamente fungibili
  	//lTotLAFungibile = lTotLAFungibile.subtract(getLANonConcesse());
  	
  	return lTotLAFungibile;
  }
  
  /**
	 * getNumGiorniLAMaturataInPenaresidua
   * 
	 * @return BigDecimal
   */
  public BigDecimal getNumGiorniLAMaturataInPenaresidua () {

		int numGiorniLAMaturataInPenaresidua = this.getLAApplicate().intValue()
				- getSemestrePresofferto().getLAApplicate().intValue();
  	
  	return new BigDecimal(numGiorniLAMaturataInPenaresidua);
  }
  
  /**
   * Semestri su cui computare le LA calcolati sulla pena residua al netto del Presofferto, quindi escluso il presoffeto.
   * @return
   */
  public BigDecimal getSemestriUtili () {

  	int semestriUtili = 0; //getListaSemetri().size();
  	
  	Vector <SemestreDL92Model> listaSemestri = getListaSemetri();
  	for (int i = 0 ; i< listaSemestri.size(); i++) {  		
  		if ("S".equals(listaSemestri.elementAt(i).getIsCompreso()))
  			semestriUtili++;
  	}  	
  	
  	return new BigDecimal(semestriUtili);
  }
  
  /**
   * Ritorna il totale dei semestri utili, quelli del presofferto più quelli delle detentiva
   * a meno dei semestri scartati
	 * @return BigDecimal
   */
  public BigDecimal getSemestriUtiliPenaScontata () {
  	
	  	int semestriUtili = this.getSemestrePresofferto().getNumSemestriMaturati().intValue();
		//		+ this.getListaSemetri().size();
	  	
		Vector <SemestreDL92Model> listaSemestri = getListaSemetri();
		for (int i = 0 ; i< listaSemestri.size(); i++) {  		
			if ("S".equals(listaSemestri.elementAt(i).getIsCompreso()))
				semestriUtili++;
		}
  	
  	return new BigDecimal(semestriUtili);
  }
  
  // Totale da espiare: Reclusione + arresto
  public BigDecimal getNumAnniDaEspiare () {
  	return new BigDecimal(this.getTotaleDaEseguire().getNumAnni());
  }
  public BigDecimal getNumMesiDaEspiare () {
  	return new BigDecimal(this.getTotaleDaEseguire().getNumMesi());
  }
  public BigDecimal getNumGiorniDaEspiare () {
  	return new BigDecimal(this.getTotaleDaEseguire().getNumGiorni());
  }
  
  // Tena Ipotetica
  public BigDecimal getNumAnniPenaIpotetica () {
  	return new BigDecimal(this.getPenaIpotetica().getNumAnni());
  }
  public BigDecimal getNumMesiPenaIpotetica () {
  	return new BigDecimal(this.getPenaIpotetica().getNumMesi());
  }
  public BigDecimal getNumGiorniPenaIpotetica () {
  	return new BigDecimal(this.getPenaIpotetica().getNumGiorni());
  }
  
  /**
	 * Restituisce il calendar ottentuto dalla Pena al netto del presofferto alla quale vanno sotrratti i GG
	 * di LA applicati
	 *
	 * @return CalendarModel
   */
  public CalendarModel getPenaIpotetica() {
  	CalendarUtil lCalUtil = new CalendarUtil(); 
  	CalendarModel lPenaIpotetica = new CalendarModel();
  	CalendarModel lLAApplicate = new CalendarModel();
  	lLAApplicate.setNumGiorni(getLAApplicate());
  	lLAApplicate = lCalUtil.ricalcolaGAM(lLAApplicate); // normalizzo
  	
  	lPenaIpotetica = lCalUtil.sottraiGiornieValute (getTotaleDaEseguire(), lLAApplicate);
  	
  	return lPenaIpotetica;
  }
  
  // Metodo per il dump del contenuto del model
	public void stampaCalcolo() {
		// Implementare il Dump
		siesLogger.debug("=============================================================");
		siesLogger.debug("== ==");
		siesLogger.debug("=============================================================");
		siesLogger.debug("== Posizione Giuridica = "+this.mPosizioneGiuridica);
		siesLogger.debug(
				"== Data inizio pena = " + DateUtils.getDateToString(this.mDataInizioPena, "dd/MM/yyyy"));
		
		siesLogger.debug("== | Semestre Utile | LA APPLICATA |        Residuo Pena          |");
		for (int i = 0; i<mListaSemetri.size(); i++) {
			SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);
			
			String rigo = "== |" + String.format("%-16s", "       " + lSemestreUtile.getProgressivo() + ")")
					+ "|" + String.format("%-14s", "       " + lSemestreUtile.getLAApplicate() + "") + "|"
          +String.format("%2s",lSemestreUtile.getResiduoNumAnni())+" anni - "
          +String.format("%2s",lSemestreUtile.getResiduoNumMesi())+" mesi - "
          +String.format("%2s",lSemestreUtile.getResiduoNumGiorni())+" giorni |";
			
			if (lSemestreUtile.getDataMaturazioneLA()!=null) {
				rigo += " " + DateUtils.getDateToString(lSemestreUtile.getDataMaturazioneLA(), "dd/MM/yyyy")
						+ " | "
						+ DateUtils.getDateToString(lSemestreUtile.getNuovaDataScadenzaPena(), "dd/MM/yyyy")
						+ " |";
			}
			
			siesLogger.debug (rigo);
		}
	}

}