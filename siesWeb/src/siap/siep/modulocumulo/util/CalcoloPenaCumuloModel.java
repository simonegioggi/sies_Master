package siap.siep.modulocumulo.util;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.CalendarUtil;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

import org.apache.log4j.Logger;

/**
 * Classe di utiliti per effettuare il CalcoloPena a livello dei prospetti cumulo
 * 
 * Il modulo prende in considerazione: - Pene Principali - Misure Cautelari - Benefici
 * 
 * - Sanzioni sostitutive
 * 
 * - ALTRO STEP2
 * 
 * Il calcolo 'semplice' prende in considerazione le Pene Principali e le somma, quindi sotrae il totale
 * presofferti e il totale benefici, ottenendo il residuo da espiare.
 * 
 * Vengono calcolati i totali lordi: - Totale Pena Principale - Totale Misure Cautelari - Totale Benefici -
 * Totale Sanzioni Sostitutive
 * 
 * Vengono poi calcolati i totali netti ovvero: - (Tot Pena Principale) - (tot misure cautelari) - (totale
 * benefici)
 * 
 * In presenza di Sanzioni Sostitutive, la pena principale sostituita non va considerata ne sul Lordo ne sul
 * Netto. Va invece riportata la/le Sanzioni Sostitutive.
 * 
 * Caso ancora particolare è la presenza di titoli con Sanzione Sostitutiva in cui sono presenti anche Misure
 * Cautelari e/o Benefici o Espiato. In questo caso bisogna decidere come scalare tali quantità, ovvero se
 * scalarle dalla Sanzione Sostitutiva o dalle pene principali degli altri titolo.
 * 
 * 
 * DA AGGIUNGERE IN STEP 2 PER COMPLETARE IL CALCOLO: -
 * 
 * @author d.fiorletta
 *
 */
public class CalcoloPenaCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9162839566239500385L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private Vector<TitoloCumulatoModel> mListaTitoli;

	// Elenco delle Quantità Lorde
	private Vector<PenaComplessivaCumuloModel> mListaPeneComplessive;
	private Vector<MisuraCautelareCumuloModel> mListaMisureCautelari;
	private Vector<BeneficioCumuloModel> mListaBenefici;
	private Vector<SanzioneSostitutivaCumuloModel> mListaSanzioniSost;

	private Vector<LibAnticipataCumuloModel> mListaLibAnticipate;

	private Vector<StatoEsecTitoloCumulatoModel> mListaProvvedimenti;
	private Vector<RichiestePmInCumuloModel> mListaRichiestePM;

	private Vector<ComputiCumuloModel> mListaComputi;

	//
	private SanzioneSostitutivaCumuloModel mTotSemidetenzione;
	private SanzioneSostitutivaCumuloModel mTotLibertaControllata;

	/**
	 * Costruttore che inizializza le strutture dati
	 */
	public CalcoloPenaCumuloModel() {
		mListaPeneComplessive = new Vector<PenaComplessivaCumuloModel>();
		mListaMisureCautelari = new Vector<MisuraCautelareCumuloModel>();
		mListaBenefici = new Vector<BeneficioCumuloModel>();
		mListaSanzioniSost = new Vector<SanzioneSostitutivaCumuloModel>();
		mListaLibAnticipate = new Vector<LibAnticipataCumuloModel>();

		mListaProvvedimenti = new Vector<StatoEsecTitoloCumulatoModel>();
		mListaRichiestePM = new Vector<RichiestePmInCumuloModel>();

		mListaComputi = new Vector<ComputiCumuloModel>();

		mTotSemidetenzione = null;
		mTotLibertaControllata = null;
	}

	// Metodi GET
	public Vector<TitoloCumulatoModel> getListaTitoli() {
		return mListaTitoli;
	}

	public Vector<PenaComplessivaCumuloModel> getListaPeneComplessive() {
		return mListaPeneComplessive;
	}

	public Vector<MisuraCautelareCumuloModel> getListaMisureCautelari() {
		return mListaMisureCautelari;
	}

	public Vector<BeneficioCumuloModel> getListaBenefici() {
		return mListaBenefici;
	}

	public Vector<SanzioneSostitutivaCumuloModel> getListaSanzioniSost() {
		return mListaSanzioniSost;
	}

	public SanzioneSostitutivaCumuloModel getTotSemidetenzione() {
		return mTotSemidetenzione;
	}

	public SanzioneSostitutivaCumuloModel getTotLibertaControllata() {
		return mTotLibertaControllata;
	}

	public Vector<LibAnticipataCumuloModel> getListaLibAnticipate() {
		return mListaLibAnticipate;
	}

	public Vector<StatoEsecTitoloCumulatoModel> getListaProvvedimenti() {
		return mListaProvvedimenti;
	}

	public Vector<RichiestePmInCumuloModel> getListaRichiestePM() {
		return mListaRichiestePM;
	}

	public Vector<ComputiCumuloModel> getListaComputi() {
		return mListaComputi;
	}

	// Metodi SET
	public void setListaTitoli(Vector<TitoloCumulatoModel> aListaTitoli) {
		this.mListaTitoli = aListaTitoli;
	}

	public void setListaPeneComplessive(Vector<PenaComplessivaCumuloModel> aListaPeneComplessive) {
		this.mListaPeneComplessive = aListaPeneComplessive;
	}

	public void setListaMisureCautelari(Vector<MisuraCautelareCumuloModel> aListaMisureCautelari) {
		this.mListaMisureCautelari = aListaMisureCautelari;
	}

	public void setListaBenefici(Vector<BeneficioCumuloModel> aListaBenefici) {
		this.mListaBenefici = aListaBenefici;
	}

	public void setListaSanzioniSost(Vector<SanzioneSostitutivaCumuloModel> aListaSanzioniSost) {
		this.mListaSanzioniSost = aListaSanzioniSost;
	}

	public void setListaLibAnticipate(Vector<LibAnticipataCumuloModel> aListaLibAnticipate) {
		this.mListaLibAnticipate = aListaLibAnticipate;
	}

	public void setListaProvvedimenti(Vector<StatoEsecTitoloCumulatoModel> aListaProvvedimenti) {
		this.mListaProvvedimenti = aListaProvvedimenti;
	}

	public void setListaRichiestePM(Vector<RichiestePmInCumuloModel> aListaRichiestePM) {
		this.mListaRichiestePM = aListaRichiestePM;
	}

	public void setListaComputi(Vector<ComputiCumuloModel> aListaComputi) {
		this.mListaComputi = aListaComputi;
	}

	// ============================================================================
	// Metodi per aggiungere dati alle strutture
	public void addPenaComplessiva(PenaComplessivaCumuloModel aPenaComplessiva) {
		mListaPeneComplessive.add(aPenaComplessiva);
	}

	public void addMisuraCautelare(MisuraCautelareCumuloModel aMisuraCautelare) {
		mListaMisureCautelari.add(aMisuraCautelare);
	}

	public void addBeneficio(BeneficioCumuloModel aBeneficio) {
		mListaBenefici.add(aBeneficio);
	}

	public void addSanzioneSost(SanzioneSostitutivaCumuloModel aSanzioneSost) {
		mListaSanzioniSost.add(aSanzioneSost);
	}

	public void addLibAnticipata(LibAnticipataCumuloModel aLibAnticipata) {
		mListaLibAnticipate.add(aLibAnticipata);
	}

	public void addProvvedimento(StatoEsecTitoloCumulatoModel aStatoEsecuzione) {
		mListaProvvedimenti.add(aStatoEsecuzione);
	}

	public void addComputi(ComputiCumuloModel aComputiCum) {
		mListaComputi.add(aComputiCum);
	}

	/**
	 * Ritorna la somma delle pena principali al lordo di presofferti e benefici.
	 * 
	 * Le Pene Principali Sostituite non vengono prese in considerazione.
	 * 
	 * 
	 * @return
	 */
	public PenaRideterminataCumuloModel getPenaPrincipaleTotLorda() {
		PenaRideterminataCumuloModel lPenaTotaleLorda = new PenaRideterminataCumuloModel();
		lPenaTotaleLorda.setFlagPenaResiduaCumulo("N");

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusioneTotMod = new CalendarModel();
		CalendarModel lCalArrestiTotMod = new CalendarModel();

		for (int i = 0; i < mListaPeneComplessive.size(); i++) {
			PenaComplessivaCumuloModel lPenaCompl = mListaPeneComplessive.elementAt(i);
			siesLogger.debug("lPenaCompl = " + lPenaCompl.getIdPenaComplessivaCum());

			// se la PC è sostituita non la calcolo
			boolean isSostituita = false;
			Iterator<SanzioneSostitutivaCumuloModel> lIterSS = mListaSanzioniSost.iterator();
			while (lIterSS.hasNext()) {
				SanzioneSostitutivaCumuloModel lSSCumulo = lIterSS.next();
				if (lPenaCompl.getIdPenaComplessivaCum().compareTo(lSSCumulo.getPcIdPenaComplessivaCum()) == 0) {
					siesLogger.debug("Pena Principale Sostituita lSSCumulo = "+ lSSCumulo.getIdSanzioneSostitutivaCum());
					
					siesLogger.debug("Verificoe se revocata.");
// Presente la SS devo verificare se revocata. Cerco nelle richieste		
					if (!lSSCumulo.getIsRevocata()){
					  isSostituita = true;
					  break;
					}
				}
			}

			if (isSostituita)
				continue; // salto la PC nel calcolo

			CalendarModel lReclusioneMulta = new CalendarModel();
			CalendarModel lArrestoAmmenda = new CalendarModel();

			// n.b. se ergastolo i quantum non dovrebbero essere presenti ma solo la 
			//      pecuniaria. Tuttavia si potrebbero voler caricare anche cumuli
			//      di cumuli con la detentiva oltre all'ergastolo.
			
			// Recupero Reclusione e Multa
			//if (!lPenaCompl.isErgastolo()) {
  			lReclusioneMulta.setNumAnni   (lPenaCompl.getNumAnniReclusione());
  			lReclusioneMulta.setNumMesi   (lPenaCompl.getNumMesiReclusione());
  			lReclusioneMulta.setNumGiorni (lPenaCompl.getNumGiorniReclusione());
			//}
			if (lPenaCompl.getImportoMulta() != null)
				lReclusioneMulta.setImportoMulta(lPenaCompl.getImportoMulta().doubleValue());

			// Recupero l'Arresto e Ammenda
			//if (!lPenaCompl.isErgastolo()) {
  			lArrestoAmmenda.setNumAnni   (lPenaCompl.getNumAnniArresto());
  			lArrestoAmmenda.setNumMesi   (lPenaCompl.getNumMesiArresto());
  			lArrestoAmmenda.setNumGiorni (lPenaCompl.getNumGiorniArresto());
			//}
			if (lPenaCompl.getImportoAmmenda() != null)
				lArrestoAmmenda.setImportoAmmenda(lPenaCompl.getImportoAmmenda().doubleValue());

			siesLogger.debug("Reclusione: " + lReclusioneMulta.toString());
			siesLogger.debug("Arresto:    " + lArrestoAmmenda.toString());

			lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod, lReclusioneMulta);
			lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod, lArrestoAmmenda);

		}

		//
		lPenaTotaleLorda.setNumGiorniReclusione(new BigDecimal(lCalReclusioneTotMod.getNumGiorni()));
		lPenaTotaleLorda.setNumMesiReclusione(new BigDecimal(lCalReclusioneTotMod.getNumMesi()));
		lPenaTotaleLorda.setNumAnniReclusione(new BigDecimal(lCalReclusioneTotMod.getNumAnni()));

		lPenaTotaleLorda.setImportoMulta(new BigDecimal(lCalReclusioneTotMod.getImportoMulta()));

		//
		lPenaTotaleLorda.setNumGiorniArresto(new BigDecimal(lCalArrestiTotMod.getNumGiorni()));
		lPenaTotaleLorda.setNumMesiArresto(new BigDecimal(lCalArrestiTotMod.getNumMesi()));
		lPenaTotaleLorda.setNumAnniArresto(new BigDecimal(lCalArrestiTotMod.getNumAnni()));

		lPenaTotaleLorda.setImportoAmmenda(new BigDecimal(lCalArrestiTotMod.getImportoAmmenda()));
		
		siesLogger.debug("lPenaTotaleLorda: " + lPenaTotaleLorda.toString());

		return lPenaTotaleLorda;
	}

	/**
	 * Effettua il calcolo del totale dei Computi
	 * 
	 * @return
	 */
	public ComputiCumuloModel getComputiTotali() {
		ComputiCumuloModel lComputiTot = new ComputiCumuloModel();

		CalendarUtil lCalUtil = new CalendarUtil();
		CalendarModel lCalCompTotMod = new CalendarModel();

		for (int i = 0; i < mListaComputi.size(); i++) {
			ComputiCumuloModel lComputo = mListaComputi.elementAt(i);
			CalendarModel lCalendarComp = new CalendarModel();

			lCalendarComp.setNumAnni(lComputo.getNumAnniReclusione());
			lCalendarComp.setNumMesi(lComputo.getNumMesiReclusione());
			lCalendarComp.setNumGiorni(lComputo.getNumGiorniReclusione());

			lCalCompTotMod = lCalUtil.sommaGiornieValute(lCalCompTotMod, lCalendarComp);
		}

		lComputiTot.setNumAnniReclusione(lCalCompTotMod.getNumAnni() != 0 ? new BigDecimal(lCalCompTotMod
				.getNumAnni()) : null);
		lComputiTot.setNumMesiReclusione(lCalCompTotMod.getNumMesi() != 0 ? new BigDecimal(lCalCompTotMod
				.getNumMesi()) : null);
		lComputiTot.setNumGiorniReclusione(lCalCompTotMod.getNumGiorni() != 0 ? new BigDecimal(lCalCompTotMod
				.getNumGiorni()) : null);

		return lComputiTot;
	}

	/**
	 * Effettua il calcolo del totale presofferti.
	 * 
	 * @return
	 */
	public MisuraCautelareCumuloModel getMisureCautelariTotali() {
		MisuraCautelareCumuloModel lMisureCautaleriTot = new MisuraCautelareCumuloModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalMCTotMod = new CalendarModel();
		
		siesLogger.debug("mListaMisureCautelari.size() = "+mListaMisureCautelari.size());
		
		for (int i = 0; i < mListaMisureCautelari.size(); i++) {
			MisuraCautelareCumuloModel lMisura = mListaMisureCautelari.elementAt(i);
			CalendarModel lCalendarMisura = new CalendarModel();

			lCalendarMisura.setNumAnni(lMisura.getNumAnni());
			lCalendarMisura.setNumMesi(lMisura.getNumMesi());
			lCalendarMisura.setNumGiorni(lMisura.getNumGiorni());

			lCalMCTotMod = lCalUtil.sommaGiornieValute(lCalMCTotMod, lCalendarMisura);
		}

		// Aggiungo le Misure riconosciute con provvedimento
		CalendarModel lComputiProvv = getMisureCautelariTotaliProv();
		lCalMCTotMod = lCalUtil.sommaGiornieValute(lCalMCTotMod, lComputiProvv);

		// Aggiungo l'espiato
		CalendarModel lComputiEspiato = getEspiatoTotale();
		lCalMCTotMod = lCalUtil.sommaGiornieValute(lCalMCTotMod, lComputiEspiato);

		lMisureCautaleriTot.setNumAnni(lCalMCTotMod.getNumAnni() != 0 ? new BigDecimal(lCalMCTotMod
				.getNumAnni()) : null);
		lMisureCautaleriTot.setNumMesi(lCalMCTotMod.getNumMesi() != 0 ? new BigDecimal(lCalMCTotMod
				.getNumMesi()) : null);
		lMisureCautaleriTot.setNumGiorni(lCalMCTotMod.getNumGiorni() != 0 ? new BigDecimal(lCalMCTotMod
				.getNumGiorni()) : null);

		return lMisureCautaleriTot;
	}

	/**
	 * 
	 * @param aTipoPena
	 * @param aFlagConcesso
	 * @return
	 */
	public CalendarModel getMisureCautelariTotaliProv() {
		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lTotSofferto = new CalendarModel();

		Vector<StatoEsecTitoloCumulatoModel> lListaProvvComputo = getProvvComputi();

		for (StatoEsecTitoloCumulatoModel lProvvComputo : lListaProvvComputo) {
			Vector<ComputiCumuloModel> lListaComputi = lProvvComputo.getListaComputi();

			for (ComputiCumuloModel lComputo : lListaComputi) {
				CalendarModel lSofferto = new CalendarModel();

				lSofferto.setNumAnni(lComputo.getNumAnniReclusione());
				lSofferto.setNumMesi(lComputo.getNumMesiReclusione());
				lSofferto.setNumGiorni(lComputo.getNumGiorniReclusione());

				lTotSofferto = lCalUtil.sommaGiornieValute(lTotSofferto, lSofferto);
			}
		}

		return lTotSofferto;
	}

	/**
	 * 
	 * @return
	 */
	public CalendarModel getEspiatoTotale() {
		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lTotSofferto = new CalendarModel();

		Vector<StatoEsecTitoloCumulatoModel> lListaProvvEspiato = getProvvEspiato();

		for (StatoEsecTitoloCumulatoModel lProvvComputo : lListaProvvEspiato) {
			Vector<ComputiCumuloModel> lListaComputi = lProvvComputo.getListaComputi();

			for (ComputiCumuloModel lComputo : lListaComputi) {
				CalendarModel lSofferto = new CalendarModel();

				lSofferto.setNumAnni(lComputo.getNumAnniReclusione());
				lSofferto.setNumMesi(lComputo.getNumMesiReclusione());
				lSofferto.setNumGiorni(lComputo.getNumGiorniReclusione());

				lTotSofferto = lCalUtil.sommaGiornieValute(lTotSofferto, lSofferto);
			}
		}

		return lTotSofferto;
	}

	/**
	 * 
	 * @return
	 */
	public CalendarModel getPagamentoPPTotale() {
		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lTotPagamenti = new CalendarModel();

		Vector<StatoEsecTitoloCumulatoModel> lListaProvvPP = getProvvPagamentoPP();

		for (StatoEsecTitoloCumulatoModel lProvvComputo : lListaProvvPP) {
			Vector<ComputiCumuloModel> lListaComputi = lProvvComputo.getListaComputi();

			for (ComputiCumuloModel lComputo : lListaComputi) {
				CalendarModel lPagamento = new CalendarModel();

				if (lComputo.getImportoMulta() != null)
					lPagamento.setImportoMulta(lComputo.getImportoMulta().doubleValue());
				if (lComputo.getImportoAmmenda() != null)
					lPagamento.setImportoAmmenda(lComputo.getImportoAmmenda().doubleValue());

				lTotPagamenti = lCalUtil.sommaGiornieValute(lTotPagamenti, lPagamento);
			}
		}

		return lTotPagamenti;
	}

	/**
	 * 02/04/2019 MEV70
	 * Recupera il totale dei quantum di Provvedimenti di Rideterminazione Pena PM Altro.
	 * 
	 * @param aTipoPena	(R=Reclusione, A=Arresto)
	 *
	 * @return
	 */
	public CalendarModel getRidetPenaPMAltroTotali(String aTipoPena) {
		CalendarModel lTotRidetPena = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusioneTotMod = new CalendarModel();
		CalendarModel lCalArrestiTotMod = new CalendarModel();

		Vector<StatoEsecTitoloCumulatoModel> lListaRidetPena = getRidetPenaPMAltro();

    siesLogger.debug("==================================================");
		siesLogger.debug("Inizio Calcolo Totali parziali ridet pena altro per aTipoPena = "+aTipoPena);
		siesLogger.debug("  lListaRidetPena.size() = "+lListaRidetPena.size());
    siesLogger.debug("==================================================");
		
		// for (int i = 0; i<lListaBenProvv.size(); i++ ){
		for (StatoEsecTitoloCumulatoModel lProvvRidPen : lListaRidetPena) {
		  siesLogger.debug("Provv:  "+lProvvRidPen.getIdStatoEsecTitoloCumulato()+" - "+lProvvRidPen.getCodTipoProvvedimento()+" - "+lProvvRidPen.getCodMotivo());
			Vector<ComputiCumuloModel> lListaComputi = lProvvRidPen.getListaComputi();

			for (ComputiCumuloModel lComputo : lListaComputi) {
			  siesLogger.debug("lComputo id =  "+lComputo.getIdComputiCumulo()+", segno = "+lComputo.getFlagPiuMeno());
			  
			  CalendarModel lReclusioneMulta = new CalendarModel();
				CalendarModel lArrestoAmmenda = new CalendarModel();

				// Recupero Reclusione e Multa
				lReclusioneMulta.setNumAnni(lComputo.getNumAnniReclusione());
				lReclusioneMulta.setNumMesi(lComputo.getNumMesiReclusione());
				lReclusioneMulta.setNumGiorni(lComputo.getNumGiorniReclusione());
				if (lComputo.getImportoMulta() != null)
					lReclusioneMulta.setImportoMulta(lComputo.getImportoMulta().doubleValue());

				// Recupero l'Arresto e Ammenda
				lArrestoAmmenda.setNumAnni(lComputo.getNumAnniArresto());
				lArrestoAmmenda.setNumMesi(lComputo.getNumMesiArresto());
				lArrestoAmmenda.setNumGiorni(lComputo.getNumGiorniArresto());
				if (lComputo.getImportoAmmenda() != null)
					lArrestoAmmenda.setImportoAmmenda(lComputo.getImportoAmmenda().doubleValue());

				siesLogger.debug("lReclusioneMulta =  "+lReclusioneMulta);
				siesLogger.debug("lArrestoAmmenda =  "+lArrestoAmmenda);
				if ("+".equals(lComputo.getFlagPiuMeno())) {
					lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod, lReclusioneMulta);
					lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod, lArrestoAmmenda);
				}
				if ("-".equals(lComputo.getFlagPiuMeno())) {
					lCalReclusioneTotMod = lCalUtil.sottraiGiornieValute(lCalReclusioneTotMod, lReclusioneMulta);
					lCalArrestiTotMod = lCalUtil.sottraiGiornieValute(lCalArrestiTotMod, lArrestoAmmenda);
				}
        siesLogger.debug("lCalReclusioneTotModParz =  "+lCalReclusioneTotMod);
        siesLogger.debug("lCalArrestiTotModParz =  "+lCalArrestiTotMod);				
			}
		}

		if ("R".equals(aTipoPena))
			lTotRidetPena = lCalReclusioneTotMod;
		else if ("A".equals(aTipoPena))
			lTotRidetPena = lCalArrestiTotMod;

		return lTotRidetPena;
	}

	/**
	 * 04/04/2019 MEV70
	 * Recupera il totale dei quantum di Provvedimenti di Revoca Misura Alternativa.
	 * 
	 * @return
	 */
	public CalendarModel getRevocaMATotali() {
		CalendarModel lTotRevocaMA = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusioneTotMod = new CalendarModel();

		Vector<StatoEsecTitoloCumulatoModel> lListaRevocaMA = getProvvEspiatoRevocaMA();

		for (StatoEsecTitoloCumulatoModel lProvvRevocaMA : lListaRevocaMA) {
			Vector<ComputiCumuloModel> lListaComputi = lProvvRevocaMA.getListaComputi();

			for (ComputiCumuloModel lComputo : lListaComputi) {
				CalendarModel lReclusioneMulta = new CalendarModel();

				// Recupero Reclusione e Multa
				lReclusioneMulta.setNumAnni(lComputo.getNumAnniReclusione());
				lReclusioneMulta.setNumMesi(lComputo.getNumMesiReclusione());
				lReclusioneMulta.setNumGiorni(lComputo.getNumGiorniReclusione());
				if (lComputo.getImportoMulta() != null)
					lReclusioneMulta.setImportoMulta(lComputo.getImportoMulta().doubleValue());

				lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod, lReclusioneMulta);
			}
		}
		lTotRevocaMA = lCalReclusioneTotMod;

		return lTotRevocaMA;
	}

	/**
	 * 16/04/2019 MEV70
	 * Recupera il totale dei quantum di Provvedimenti di Sospensione / Differimento della Pena.
	 * 
	 * @return
	 */
	public CalendarModel getSospDiffTotali() {
		CalendarModel lTotSospDiff = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusioneTotMod = new CalendarModel();

		Vector<StatoEsecTitoloCumulatoModel> lListaSospDiff = getProvvSospDiff();

		for (StatoEsecTitoloCumulatoModel lProvvSospDiff : lListaSospDiff) {
			Vector<ComputiCumuloModel> lListaComputi = lProvvSospDiff.getListaComputi();

			for (ComputiCumuloModel lComputo : lListaComputi) {
				CalendarModel lReclusioneMulta = new CalendarModel();

				// Recupero Reclusione e Multa
				lReclusioneMulta.setNumAnni(lComputo.getNumAnniReclusione());
				lReclusioneMulta.setNumMesi(lComputo.getNumMesiReclusione());
				lReclusioneMulta.setNumGiorni(lComputo.getNumGiorniReclusione());
				if (lComputo.getImportoMulta() != null)
					lReclusioneMulta.setImportoMulta(lComputo.getImportoMulta().doubleValue());

				lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod, lReclusioneMulta);
			}
		}
		lTotSospDiff = lCalReclusioneTotMod;

		return lTotSospDiff;
	}
	
	/**
	 * Ritorna la somma delle pene principali al Netto di presofferti e benefici.
	 * 
	 * @return
	 */
	public PenaRideterminataCumuloModel getPenaPrincipaleTotNetta() {
		PenaRideterminataCumuloModel lPenaTotaleNetta = new PenaRideterminataCumuloModel();
		lPenaTotaleNetta.setFlagPenaResiduaCumulo("S");

		CalendarUtil lCalUtil = new CalendarUtil();

		// ==================================================
		// Recupero la Pena Complessiva Lorda
		// ==================================================
    siesLogger.debug("=======================================================");
		siesLogger.debug("Recupero la Pena Complessiva Lorda");
    siesLogger.debug("=======================================================");
		PenaRideterminataCumuloModel lPenaTotaleLorda = getPenaPrincipaleTotLorda();

		CalendarModel lCalReclusioneTotMod = new CalendarModel();
		CalendarModel lCalArrestiTotMod = new CalendarModel();

		lCalReclusioneTotMod.setNumAnni(lPenaTotaleLorda.getNumAnniReclusione());
		lCalReclusioneTotMod.setNumMesi(lPenaTotaleLorda.getNumMesiReclusione());
		lCalReclusioneTotMod.setNumGiorni(lPenaTotaleLorda.getNumGiorniReclusione());
		if (lPenaTotaleLorda.getImportoMulta() != null)
			lCalReclusioneTotMod.setImportoMulta(lPenaTotaleLorda.getImportoMulta().doubleValue());

		lCalArrestiTotMod.setNumAnni(lPenaTotaleLorda.getNumAnniArresto());
		lCalArrestiTotMod.setNumMesi(lPenaTotaleLorda.getNumMesiArresto());
		lCalArrestiTotMod.setNumGiorni(lPenaTotaleLorda.getNumGiorniArresto());
		if (lPenaTotaleLorda.getImportoAmmenda() != null)
			lCalArrestiTotMod.setImportoAmmenda(lPenaTotaleLorda.getImportoAmmenda().doubleValue());

		
    siesLogger.debug("Totali Parziali Aggiornati ==================");
		siesLogger.debug("lCalReclusioneTotMod = "+lCalReclusioneTotMod);
		siesLogger.debug("lCalArrestiTotMod = "+lCalArrestiTotMod);
		// ======================================================
		// Computo i Benefici
		// ======================================================
    siesLogger.debug("=======================================================");
		siesLogger.debug("Computo i benefici");
    siesLogger.debug("=======================================================");
    siesLogger.debug("getBeneficiTotali(R) = "+getBeneficiTotali("R"));
    siesLogger.debug("getBeneficiTotali(A) = "+getBeneficiTotali("A"));

		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod, getBeneficiTotali("R"));
		lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod, getBeneficiTotali("A"));

    siesLogger.debug("Totali Parziali Aggiornati ==================");
    siesLogger.debug("lCalReclusioneTotMod = "+lCalReclusioneTotMod);
    siesLogger.debug("lCalArrestiTotMod = "+lCalArrestiTotMod);
		/*
		 * // Sommo per prima le revoche 1siesLogger.debug("Sommo per prima le revoche"); lCalReclusioneTotMod
		 * = lCalUtil.sommaGiornieValute (lCalReclusioneTotMod, getBeneficiTotali("R","R")); lCalArrestiTotMod
		 * = lCalUtil.sommaGiornieValute (lCalArrestiTotMod, getBeneficiTotali("A","R"));
		 * 
		 * // Sottraggo i benefici concessi siesLogger.debug("Sottraggo i benefici concessi");
		 * lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew (lCalReclusioneTotMod,
		 * getBeneficiTotali("R","C")); lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew (lCalArrestiTotMod
		 * , getBeneficiTotali("A","C"));
		 */

		// ==========================================================================
		// Sottraggo alla pena totale le MC, prima dalla Reclusione e quindi dagli Arresti
		// ==========================================================================
    siesLogger.debug("=======================================================");
		siesLogger.debug("Sottraggo le Misure Cautelari dalla reclusione");
    siesLogger.debug("=======================================================");
		CalendarModel lMCTotali = this.getMisureCautelariTotali().getQuantumMisura();
		siesLogger.debug("lMCTotali = "+lMCTotali);
		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod, lMCTotali);

		siesLogger.debug("Totali Parziali Aggiornati ==================");
    siesLogger.debug("lCalReclusioneTotMod = "+lCalReclusioneTotMod);
    siesLogger.debug("lCalArrestiTotMod = "+lCalArrestiTotMod);
    
		if (!lCalUtil.isPositiveTime(lCalReclusioneTotMod)) {
			siesLogger.debug("Attenzione Quantum di Reclusione Negativi: " + lCalReclusioneTotMod);

			CalendarModel lCalModApp = new CalendarModel();
			lCalModApp = lCalUtil.abs(lCalReclusioneTotMod);

			//
			lCalModApp = lCalUtil.sottraiGiorniNew(lCalArrestiTotMod, lCalModApp);

			// Aggiorno i quantum di Arresto
			lCalArrestiTotMod.setNumAnni(lCalModApp.getNumAnni());
			lCalArrestiTotMod.setNumMesi(lCalModApp.getNumMesi());
			lCalArrestiTotMod.setNumGiorni(lCalModApp.getNumGiorni());

			// Azzero i quantum di reclusione
			lCalReclusioneTotMod.setNumAnni(0);
			lCalReclusioneTotMod.setNumMesi(0);
			lCalReclusioneTotMod.setNumGiorni(0);
		}
		siesLogger.debug("Totali Parziali Aggiornati ==================");
    siesLogger.debug("lCalReclusioneTotMod = "+lCalReclusioneTotMod);
    siesLogger.debug("lCalArrestiTotMod = "+lCalArrestiTotMod);

		// ===========================================================================
		// Computo le richieste: se >0 si tratta di Richiste di Applicazione Benefici
		// ===========================================================================
    siesLogger.debug("=======================================================");
    siesLogger.debug(" Computo le richieste Tutte: Cocesse e Revocate...");
    siesLogger.debug("=======================================================");
		CalendarModel lRichTotReclusione = getRichiesteTotali("R", null);
		CalendarModel lRichTotArresti = getRichiesteTotali("A", null);
		
		siesLogger.debug("lRichTotReclusione = "+lRichTotReclusione);
		siesLogger.debug("lRichTotArresti = "+lRichTotArresti);
		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod, lRichTotReclusione);
		lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod, lRichTotArresti);
		
		siesLogger.debug("Totali Parziali Aggiornati ==================");
    siesLogger.debug("lCalReclusioneTotMod = "+lCalReclusioneTotMod);
    siesLogger.debug("lCalArrestiTotMod = "+lCalArrestiTotMod);
		
		/*
		 * if (lCalUtil.isPositiveTime (lRichTotReclusione)) lCalReclusioneTotMod =
		 * lCalUtil.sottraiGiorniValuteNew (lCalReclusioneTotMod, lRichTotReclusione); else
		 * lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod, lRichTotReclusione);
		 * 
		 * if (lCalUtil.isPositiveTime (lRichTotArresti)) lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew
		 * (lCalArrestiTotMod , lRichTotArresti); else lCalArrestiTotMod = lCalUtil.sommaGiornieValute
		 * (lCalArrestiTotMod , lRichTotArresti);
		 */

		// ===========================================================================
		// Computo le Annotazioni Pagamento PP
		// ===========================================================================
    siesLogger.debug("=======================================================");
		siesLogger.debug(" Computo le Annotazioni Pagamento PP");
    siesLogger.debug("=======================================================");
		CalendarModel lTotPagamentiPP = getPagamentoPPTotale();
		siesLogger.debug("lTotPagamentiPP = "+lTotPagamentiPP);
		CalendarModel lTotPagamentiPPMulta = new CalendarModel(lTotPagamentiPP);
		lTotPagamentiPPMulta.setImportoAmmenda(0);
		CalendarModel lTotPagamentiPPAmmenda = new CalendarModel(lTotPagamentiPP);
		lTotPagamentiPPAmmenda.setImportoMulta(0);

		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod, lTotPagamentiPPMulta);
		lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod, lTotPagamentiPPAmmenda);
    siesLogger.debug("Totali Parziali Aggiornati ==================");
    siesLogger.debug("lCalReclusioneTotMod = "+lCalReclusioneTotMod);
    siesLogger.debug("lCalArrestiTotMod = "+lCalArrestiTotMod);

		// ===========================================================================
		// MEV70 Computo dei provvedimenti di Rideterminazione Pena Altro
		// ===========================================================================
    siesLogger.debug("=======================================================");
    siesLogger.debug(" MEV70 Computo dei provvedimenti di Rideterminazione Pena Altro");
    siesLogger.debug("=======================================================");
		CalendarModel lTotReclusioneRidetPenaAltro = getRidetPenaPMAltroTotali("R");
    CalendarModel lTotArrestoRidetPenaAltro = getRidetPenaPMAltroTotali("A");
		
		siesLogger.debug("lTotReclusioneRidetPenaAltro = "+lTotReclusioneRidetPenaAltro);
    siesLogger.debug("lTotArrestoRidetPenaAltro = "+lTotArrestoRidetPenaAltro);
//		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew (lCalReclusioneTotMod, lTotReclusioneRidetPenaAltro);
//    lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod, lTotArrestoRidetPenaAltro);
    lCalReclusioneTotMod = lCalUtil.sommaGiornieValute (lCalReclusioneTotMod, lTotReclusioneRidetPenaAltro);
    lCalArrestiTotMod    = lCalUtil.sommaGiornieValute (lCalArrestiTotMod, lTotArrestoRidetPenaAltro);

    siesLogger.debug("Totali Parziali Aggiornati ==================");
    siesLogger.debug("lCalReclusioneTotMod = "+lCalReclusioneTotMod);
    siesLogger.debug("lCalArrestiTotMod = "+lCalArrestiTotMod);
    
		// ===========================================================================
		// MEV70 Computo dei provvedimenti di Revoca Misure Alternative
		// ===========================================================================
    siesLogger.debug("=======================================================");
    siesLogger.debug("MEV70 Computo dei provvedimenti di Revoca Misure Alternative");
    siesLogger.debug("=======================================================");
		CalendarModel lTotRevocaMA = getRevocaMATotali();
		siesLogger.debug("lTotRevocaMA = "+lTotRevocaMA);
		
		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod, lTotRevocaMA);

    siesLogger.debug("Totali Parziali Aggiornati ==================");
    siesLogger.debug("lCalReclusioneTotMod = "+lCalReclusioneTotMod);
    siesLogger.debug("lCalArrestiTotMod = "+lCalArrestiTotMod);
    
		// ===========================================================================
		// MEV70 Computo dei provvedimenti di Sospensione / Differimento della Pena
		// ===========================================================================
    siesLogger.debug("=======================================================");
    siesLogger.debug("MEV70 Computo dei provvedimenti di Sospensione / Differimento della Pena");
    siesLogger.debug("=======================================================");
		CalendarModel lTotSospDiff = getSospDiffTotali();
    siesLogger.debug("lTotSospDiff = "+lTotSospDiff);
		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod, lTotSospDiff);
		
    siesLogger.debug("=======================================================");
    siesLogger.debug("=========== Totali FINALI Aggiornati ==================");
    siesLogger.debug("lCalReclusioneTotMod = "+lCalReclusioneTotMod);
    siesLogger.debug("lCalArrestiTotMod = "+lCalArrestiTotMod);
    siesLogger.debug("=======================================================");

		// ===============================================
		// Restituisco il totale come PenaResiduaModel
		// ===============================================
    if (!lCalUtil.isPositiveTime(lCalReclusioneTotMod)) {
      siesLogger.debug("Attenzione Quantum di Reclusione Negativi: " + lCalReclusioneTotMod);

      CalendarModel lCalModApp = new CalendarModel();
      lCalModApp = lCalUtil.abs(lCalReclusioneTotMod);

      //
      lCalModApp = lCalUtil.sottraiGiorniNew(lCalArrestiTotMod, lCalModApp);

      // Aggiorno i quantum di Arresto
      lCalArrestiTotMod.setNumAnni(lCalModApp.getNumAnni());
      lCalArrestiTotMod.setNumMesi(lCalModApp.getNumMesi());
      lCalArrestiTotMod.setNumGiorni(lCalModApp.getNumGiorni());

      // Azzero i quantum di reclusione
      lCalReclusioneTotMod.setNumAnni(0);
      lCalReclusioneTotMod.setNumMesi(0);
      lCalReclusioneTotMod.setNumGiorni(0);
    }
    
		lPenaTotaleNetta.setNumGiorniReclusione(new BigDecimal(lCalReclusioneTotMod.getNumGiorni()));
		lPenaTotaleNetta.setNumMesiReclusione(new BigDecimal(lCalReclusioneTotMod.getNumMesi()));
		lPenaTotaleNetta.setNumAnniReclusione(new BigDecimal(lCalReclusioneTotMod.getNumAnni()));

		lPenaTotaleNetta.setImportoMulta(new BigDecimal(lCalReclusioneTotMod.getImportoMulta()));

		//
		lPenaTotaleNetta.setNumGiorniArresto(new BigDecimal(lCalArrestiTotMod.getNumGiorni()));
		lPenaTotaleNetta.setNumMesiArresto(new BigDecimal(lCalArrestiTotMod.getNumMesi()));
		lPenaTotaleNetta.setNumAnniArresto(new BigDecimal(lCalArrestiTotMod.getNumAnni()));

		lPenaTotaleNetta.setImportoAmmenda(new BigDecimal(lCalArrestiTotMod.getImportoAmmenda()));

		return lPenaTotaleNetta;
	}

	/***
	 * Ritorna il totale benefici aggregati per titologia (aTipoPena) e tipo computo (concesso/revocato)
	 * 
	 * @param aTipoPena
	 *            : R = reclusione, A = Arresto
	 * @param aFlagConcesso
	 *            : C = Concesso, R = Revocato
	 * @return CalendarModel con i quantum e la pecuniaria
	 */
	public CalendarModel getBeneficiTotali(String aTipoPena, String aFlagConcesso) {
		CalendarModel lTotBenefici = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusioneTotMod = new CalendarModel();
		CalendarModel lCalArrestiTotMod = new CalendarModel();

		for (int i = 0; i < mListaBenefici.size(); i++) {
			BeneficioCumuloModel lBeneficio = mListaBenefici.elementAt(i);

			if (("03".equals(lBeneficio.getCodTipoBeneficio()) // Indulto
					|| "04".equals(lBeneficio.getCodTipoBeneficio()) // Amnistia
					)
					&& (lBeneficio.getCodNaturaBeneficio().equals(aFlagConcesso))) {
				CalendarModel lReclusioneMulta = new CalendarModel();
				CalendarModel lArrestoAmmenda = new CalendarModel();

				// Recupero Reclusione e Multa
				lReclusioneMulta.setNumAnni(lBeneficio.getNumAnniReclusione());
				lReclusioneMulta.setNumMesi(lBeneficio.getNumMesiReclusione());
				lReclusioneMulta.setNumGiorni(lBeneficio.getNumGiorniReclusione());
				if (lBeneficio.getImportoMulta() != null)
					lReclusioneMulta.setImportoMulta(lBeneficio.getImportoMulta().doubleValue());

				// Recupero l'Arresto e Ammenda
				lArrestoAmmenda.setNumAnni(lBeneficio.getNumAnniArresto());
				lArrestoAmmenda.setNumMesi(lBeneficio.getNumMesiArresto());
				lArrestoAmmenda.setNumGiorni(lBeneficio.getNumGiorniArresto());
				if (lBeneficio.getImportoAmmenda() != null)
					lArrestoAmmenda.setImportoAmmenda(lBeneficio.getImportoAmmenda().doubleValue());

				lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod, lReclusioneMulta);
				lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod, lArrestoAmmenda);
			}
		}

		if ("R".equals(aTipoPena))
			lTotBenefici = lCalReclusioneTotMod;
		else if ("A".equals(aTipoPena))
			lTotBenefici = lCalArrestiTotMod;

		return lTotBenefici;
	}

	/**
	 * Recupera il totale dei benefici concessi con provvedimento operando sullo Stato Esecuzione
	 * 
	 * @param aTipoPena
	 * @param aFlagConcesso
	 *            (+/-)
	 * @return
	 */
	public CalendarModel getBeneficiTotaliProvv(String aTipoPena, String aFlagConcesso) {
		CalendarModel lTotBenefici = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusioneTotMod = new CalendarModel();
		CalendarModel lCalArrestiTotMod = new CalendarModel();

		Vector<StatoEsecTitoloCumulatoModel> lListaBenProvv = getProvvBenefici();

		// for (int i = 0; i<lListaBenProvv.size(); i++ ){
		for (StatoEsecTitoloCumulatoModel lProvvBen : lListaBenProvv) {
			Vector<ComputiCumuloModel> lListaComputi = lProvvBen.getListaComputi();

			for (ComputiCumuloModel lComputo : lListaComputi) {
				if (aFlagConcesso.equals(lComputo.getFlagPiuMeno())) {
					CalendarModel lReclusioneMulta = new CalendarModel();
					CalendarModel lArrestoAmmenda = new CalendarModel();

					// Recupero Reclusione e Multa
					lReclusioneMulta.setNumAnni(lComputo.getNumAnniReclusione());
					lReclusioneMulta.setNumMesi(lComputo.getNumMesiReclusione());
					lReclusioneMulta.setNumGiorni(lComputo.getNumGiorniReclusione());
					if (lComputo.getImportoMulta() != null)
						lReclusioneMulta.setImportoMulta(lComputo.getImportoMulta().doubleValue());

					// Recupero l'Arresto e Ammenda
					lArrestoAmmenda.setNumAnni(lComputo.getNumAnniArresto());
					lArrestoAmmenda.setNumMesi(lComputo.getNumMesiArresto());
					lArrestoAmmenda.setNumGiorni(lComputo.getNumGiorniArresto());
					if (lComputo.getImportoAmmenda() != null)
						lArrestoAmmenda.setImportoAmmenda(lComputo.getImportoAmmenda().doubleValue());

					lCalReclusioneTotMod = lCalUtil
							.sommaGiornieValute(lCalReclusioneTotMod, lReclusioneMulta);
					lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod, lArrestoAmmenda);
				}
			}
		}

		if ("R".equals(aTipoPena))
			lTotBenefici = lCalReclusioneTotMod;
		else if ("A".equals(aTipoPena))
			lTotBenefici = lCalArrestiTotMod;

		return lTotBenefici;
	}

	/***
	 * Ritorna il totale benefici aggregati per titologia (aTipoPena) e tipo computo (concesso-revocato) In
	 * sentenza e con Provvedimento
	 * 
	 * @param aTipoPena
	 *            : R = reclusione, A = Arresto
	 * @return CalendarModel con i quantum e la pecuniaria
	 */
	public CalendarModel getBeneficiTotali(String aTipoPena) {
		CalendarModel lTotBenefici = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		// Recupero i benefici in sentenza
		CalendarModel lCalendarConcessi = getBeneficiTotali(aTipoPena, "C");
		CalendarModel lCalendarRevocati = getBeneficiTotali(aTipoPena, "R");

		// Recupero i benefici Con provvedimento
		CalendarModel lCalendarConcessiProvv = getBeneficiTotaliProvv(aTipoPena, "-");
		CalendarModel lCalendarRevocatiProvv = getBeneficiTotaliProvv(aTipoPena, "+");

		CalendarModel lTotBeneficiConcessi = lCalUtil.sommaGiornieValute(lCalendarConcessi,
				lCalendarConcessiProvv);
		CalendarModel lTotBeneficiRevocati = lCalUtil.sommaGiornieValute(lCalendarRevocati,
				lCalendarRevocatiProvv);

		lTotBenefici = lCalUtil.sottraiGiorniValuteNew(lTotBeneficiConcessi, lTotBeneficiRevocati);

		return lTotBenefici;
	}

	/**
	 * Effettua l'aggregazione dei benefici per tipologia (DPR). Considerando concessioni e revoche.
	 * 
	 * @return
	 */
	public Vector<BeneficioCumuloModel> getBeneficiAggregati() {
		Vector<BeneficioCumuloModel> lBeneficiAggregati = new Vector<BeneficioCumuloModel>();

		// Sommo prima i concessi e poi tolgo irrevocati
		for (int i = 0; i < mListaBenefici.size(); i++) {
			BeneficioCumuloModel lBeneficio = mListaBenefici.elementAt(i);

			if (("03".equals(lBeneficio.getCodTipoBeneficio()) // Indulto
					|| "04".equals(lBeneficio.getCodTipoBeneficio()) // Amnistia
					)
					&& (lBeneficio.getCodNaturaBeneficio().equals("C"))) {
				CalendarModel lReclusioneMulta = lBeneficio.getQuantumReclusione();
				CalendarModel lArrestoAmmenda = lBeneficio.getQuantumArresto();

				// Ricerco se già presente negli aggregati lo stesso beneficio cui sommare
				// sotrrarre, altrimento inserisco
				boolean trovato = false;
				for (int j = 0; j < lBeneficiAggregati.size(); j++) {
					BeneficioCumuloModel lBenAggregato = lBeneficiAggregati.elementAt(j);

					if (lBenAggregato.getCodDpr().equals(lBeneficio.getCodDpr())) {

						CalendarModel lReclusioneMultaAgg = lBenAggregato.getQuantumReclusione();
						CalendarModel lArrestoAmmendaAgg = lBenAggregato.getQuantumArresto();

						CalendarUtil lCalUtil = new CalendarUtil();

						lReclusioneMultaAgg = lCalUtil.sommaGiornieValute(lReclusioneMultaAgg,
								lReclusioneMulta);
						lArrestoAmmendaAgg = lCalUtil.sommaGiornieValute(lArrestoAmmendaAgg, lArrestoAmmenda);

						lBenAggregato.setQuantumReclusione(lReclusioneMultaAgg);
						lBenAggregato.setQuantumArresto(lArrestoAmmendaAgg);

						trovato = true;
						break;
					}
				}

				if (!trovato) {
					// Inserisco il beneficio
					BeneficioCumuloModel lBenAggregato = new BeneficioCumuloModel();

					lBenAggregato.setQuantumReclusione(lBeneficio.getQuantumReclusione());
					lBenAggregato.setQuantumArresto(lBeneficio.getQuantumArresto());

					lBenAggregato.setCodTipoBeneficio(lBeneficio.getCodTipoBeneficio());
					lBenAggregato.setDescrTipoBeneficio(lBeneficio.getDescrTipoBeneficio());
					lBenAggregato.setCodDpr(lBeneficio.getCodDpr());
					lBenAggregato.setDescrDpr(lBeneficio.getDescrDpr());

					// lBenAggregato.setDescrNaturaBeneficio(aValore)

					lBeneficiAggregati.add(lBenAggregato);
				}
			}
		}

		// Sottraggo i revocati
		for (int i = 0; i < mListaBenefici.size(); i++) {
			BeneficioCumuloModel lBeneficio = mListaBenefici.elementAt(i);

			if (("03".equals(lBeneficio.getCodTipoBeneficio()) // Indulto
					|| "04".equals(lBeneficio.getCodTipoBeneficio()) // Amnistia
					)
					&& (lBeneficio.getCodNaturaBeneficio().equals("R"))) {
				CalendarModel lReclusioneMulta = lBeneficio.getQuantumReclusione();
				CalendarModel lArrestoAmmenda = lBeneficio.getQuantumArresto();

				// Ricerco se già presente negli aggregati lo stesso beneficio cui sommare
				// sotrrarre, altrimento inserisco
				boolean trovato = false;
				for (int j = 0; j < lBeneficiAggregati.size(); j++) {
					BeneficioCumuloModel lBenAggregato = lBeneficiAggregati.elementAt(j);

					if (lBenAggregato.getCodDpr().equals(lBeneficio.getCodDpr())) {

						CalendarModel lReclusioneMultaAgg = lBenAggregato.getQuantumReclusione();
						CalendarModel lArrestoAmmendaAgg = lBenAggregato.getQuantumArresto();

						CalendarUtil lCalUtil = new CalendarUtil();

						lReclusioneMultaAgg = lCalUtil.sottraiGiornieValute(lReclusioneMultaAgg,
								lReclusioneMulta);
						lArrestoAmmendaAgg = lCalUtil.sottraiGiornieValute(lArrestoAmmendaAgg,
								lArrestoAmmenda);

						lBenAggregato.setQuantumReclusione(lReclusioneMultaAgg);
						lBenAggregato.setQuantumArresto(lArrestoAmmendaAgg);

						trovato = true;
						break;
					}
				}

				if (!trovato) {
					// Inserisco la Revoca n.b. non dovrebbe mai accadere che venga revocato un
					// benefigio non concesso
					siesLogger.warn("Revoca di un beneficio non concesso: " + lBeneficio);
					BeneficioCumuloModel lBenAggregato = new BeneficioCumuloModel(lBeneficio);

					lBeneficiAggregati.add(lBenAggregato);
				}
			}
		}

		return lBeneficiAggregati;
	}

	/**
	 * Calcola il totale LA Concessi o revocati con provvedimenti
	 * 
	 * @param aTipoLA
	 * @param aIdStatEsec
	 *            = id del provvedimento. Se indicato il metodo filtra le sole LA collegate al provvedimento
	 * @return
	 */
	public int getTotaliLA(String aTipoLA, BigDecimal aIdStatEsec) {
		int lTotLA = 0;

		siesLogger.debug("Tipo:" + aTipoLA + ", idStato:" + aIdStatEsec);
		if (mListaLibAnticipate != null) {
			for (LibAnticipataCumuloModel lLibAnt : mListaLibAnticipate) {
				if (aIdStatEsec != null && aIdStatEsec.compareTo(lLibAnt.getStatIdStatoEsecTitoloCum()) != 0) {
					continue;
				}

				if ("LA".equals(lLibAnt.getCodTipoLicenza()) // Solo LA = liberazioni Anticipate
						&& aTipoLA.equals(lLibAnt.getTipoLa()) // Tipologia: LA,LI,LS
						&& lLibAnt.getNumeroGiorni() != null) {
					if ("C".equals(lLibAnt.getFlagConcesso()))
						lTotLA = lTotLA + lLibAnt.getNumeroGiorni().intValue();
					else if ("S".equals(lLibAnt.getFlagConcesso()))
						lTotLA = lTotLA - lLibAnt.getNumeroGiorni().intValue();
				}
			}
		}

		if (aIdStatEsec == null) {
			// Aggiungere le Richieste/Decisioni di Revoca
			// Per ora scarico solo le decisioni non essndo le richiesta con anticipazione
			for (RichiestePmInCumuloModel lRichiesta : mListaRichiestePM) {
				if ("020".equals(lRichiesta.getCodTipoAnnotazione())) {
					siesLogger.debug("Rich Trovata: " + lRichiesta.getIdRichiestePmInCumulo() + "-"
							+ lRichiesta.getDescrTipoAnnotazione() + "- Antic: "
							+ lRichiesta.getFlagAppProvvisoria());
					ProvvedimentoGeSorvCumModel lDecisione = lRichiesta.getDecisioneGeSorvCum();

					if (lDecisione != null) {
						siesLogger.debug("Decisione Trovata: " + lDecisione.getIdProvvedimentoGeSorvCum());
						if ("LA".equals(aTipoLA) && lDecisione.getNumGiorniRevocaLaD() != null)
							lTotLA = lTotLA - lDecisione.getNumGiorniRevocaLaD().intValue();
						else if ("LS".equals(aTipoLA) && lDecisione.getNumGiorniRevocaLsD() != null)
							lTotLA = lTotLA - lDecisione.getNumGiorniRevocaLsD().intValue();
						else if ("LI".equals(aTipoLA) && lDecisione.getNumGiorniRevocaLiD() != null)
							lTotLA = lTotLA - lDecisione.getNumGiorniRevocaLiD().intValue();
					} else if ("A".equals(lRichiesta.getFlagAppProvvisoria())) {
						siesLogger.debug("Richiesta con anticipazione senza decisione");
						if ("LA".equals(aTipoLA) && lRichiesta.getNumGiorniRevocaLA() != null)
							lTotLA = lTotLA - lRichiesta.getNumGiorniRevocaLA().intValue();
						else if ("LS".equals(aTipoLA) && lRichiesta.getNumGiorniRevocaLS() != null)
							lTotLA = lTotLA - lRichiesta.getNumGiorniRevocaLS().intValue();
						else if ("LI".equals(aTipoLA) && lRichiesta.getNumGiorniRevocaLI() != null)
							lTotLA = lTotLA - lRichiesta.getNumGiorniRevocaLI().intValue();
					} else {
						siesLogger.debug("Decisione non presente");
					}
				}
			}
		}

		return lTotLA;
	}

	/**
	 * Ritorna il totale giorni concessi con rimedi Risarcitori
	 * 
	 * @return
	 */
	public int getTotaliRimedi(BigDecimal aIdStatEsec) {
		int lTotLA = 0;

		if (mListaLibAnticipate != null) {
			for (LibAnticipataCumuloModel lLibAnt : mListaLibAnticipate) {

				if (aIdStatEsec != null && aIdStatEsec.compareTo(lLibAnt.getStatIdStatoEsecTitoloCum()) != 0) {
					continue;
				}

				if ("RD".equals(lLibAnt.getCodTipoLicenza()) // Solo RD = Rimedi RIsercitori
						&& lLibAnt.getNumeroGiorni() != null) {
					siesLogger.debug("DL92 = id " + lLibAnt.getIdLibAnticipataCumulo() + ", gg: "
							+ lLibAnt.getNumeroGiorni() + ", Conc = " + lLibAnt.getFlagConcesso());
					if ("C".equals(lLibAnt.getFlagConcesso()))
						lTotLA = lTotLA + lLibAnt.getNumeroGiorni().intValue();
					else if ("S".equals(lLibAnt.getFlagConcesso()))
						lTotLA = lTotLA - lLibAnt.getNumeroGiorni().intValue();
				}
			}
		}

		return lTotLA;
	}
	
	/**
	 * Ritorna il totale degli scomputi permesso. Il valore restituito rappresenta
	 * i GG di scomputo che devono essere SOMMATI al fine pena. Il valore dovrebbe
	 * essere sempre >=0. In presenza di reclami infatti lo scomputo può al più
	 * azzerarsi.
	 * 01-[02/03]-2250 - Esclusione Computo Permesso - COD_TIPO_LICENZA = PP, FLAG_CONCESSO = S 
	 * 01-[02/03]-0039 - Reclamo Avverso Scomputo Periodo Permesso COD_TIPO_LICENZA = EP, FLAG_CONCESSO = C 
	 * @param aIdStatEsec - Se indicato il totale viene effettuato sul singolo provvedimento
	 *                      Se null vengono sommati i dati di tutti i provvedimenti di tutti
	 *                      i titoli in istruttoria
	 * @return
	 */
	public int getTotaliScomputi(BigDecimal aIdStatEsec) {
	  int lTotScomputi = 0;

	  siesLogger.debug("Get scomputi permessi per titolo aIdStatEsec = "+aIdStatEsec);
	  if (mListaLibAnticipate != null) {
	    for (LibAnticipataCumuloModel lLibAnt : mListaLibAnticipate) {

	      if (aIdStatEsec != null && aIdStatEsec.compareTo(lLibAnt.getStatIdStatoEsecTitoloCum()) != 0) {
	        continue;
	      }

	      if ("PP".equals(lLibAnt.getCodTipoLicenza()) && lLibAnt.getNumeroGiorni() != null) 
	      {
	        siesLogger.debug("Scomputo Permessi = id " + lLibAnt.getIdLibAnticipataCumulo() + ", gg: "
	            + lLibAnt.getNumeroGiorni() + ", Conc = " + lLibAnt.getFlagConcesso());
	        //if ("S".equals(lLibAnt.getFlagConcesso()))
	          lTotScomputi = lTotScomputi + lLibAnt.getNumeroGiorni().intValue();
	      }
	      else if ("EP".equals(lLibAnt.getCodTipoLicenza()) && lLibAnt.getNumeroGiorni() != null) 
        {
          siesLogger.debug("Reclamo Scomputo Permessi = id " + lLibAnt.getIdLibAnticipataCumulo() + ", gg: "
              + lLibAnt.getNumeroGiorni() + ", Conc = " + lLibAnt.getFlagConcesso());
          //if ("C".equals(lLibAnt.getFlagConcesso()))
            lTotScomputi = lTotScomputi - lLibAnt.getNumeroGiorni().intValue();
        }	      
	    }
	  }
	  
	  siesLogger.debug("Totale scomputi permessi = "+lTotScomputi);

	  return lTotScomputi;
	}

	 
	/**
	 * Metodo che calcolo il totale delle Sanzioni sostitutive disposte sui vari titoli aggregando quelle
	 * dello stesso tipo. Salva il calcolato nelle variabili di classe mTotSemidetenzione e
	 * mTotLibertaControllata
	 */
	public void calcolaTotaliSanzioniSost() {
		CalendarModel lTotSemidetenzione = new CalendarModel();
		CalendarModel lTotLibertaControllata = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		String lDescrSemidetenzione = "";
		String lDescrLibertaControllata = "";

		Iterator<SanzioneSostitutivaCumuloModel> lIterSS = mListaSanzioniSost.iterator();
		while (lIterSS.hasNext()) {
			SanzioneSostitutivaCumuloModel lSSCumulo = lIterSS.next();

			if (lSSCumulo.getCodTipoSanzione().equals("S")) {
				lTotSemidetenzione = lCalUtil
						.sommaGiornieValute(lTotSemidetenzione, lSSCumulo.getQuantumSS());
				lDescrSemidetenzione = lSSCumulo.getDescrTipoSanzione();
			} else if (lSSCumulo.getCodTipoSanzione().equals("L")) {
				lTotLibertaControllata = lCalUtil.sommaGiornieValute(lTotLibertaControllata,
						lSSCumulo.getQuantumSS());
				lDescrLibertaControllata = lSSCumulo.getDescrTipoSanzione();
			}
		}

		if (!lTotSemidetenzione.isQuantumZero()) {
			mTotSemidetenzione = new SanzioneSostitutivaCumuloModel();
			mTotSemidetenzione.setCodTipoSanzione("S");
			mTotSemidetenzione.setDescrTipoSanzione(lDescrSemidetenzione);
			mTotSemidetenzione.setQuantumSS(lTotSemidetenzione);
			mTotSemidetenzione.setFlagPenaNetta("N");

			mTotSemidetenzione.calcolaStringaSanzione();

		} else
			mTotSemidetenzione = null;

		if (!lTotLibertaControllata.isQuantumZero()) {
			mTotLibertaControllata = new SanzioneSostitutivaCumuloModel();
			mTotLibertaControllata.setCodTipoSanzione("L");
			mTotLibertaControllata.setDescrTipoSanzione(lDescrLibertaControllata);
			mTotLibertaControllata.setQuantumSS(lTotLibertaControllata);

			mTotLibertaControllata.setFlagPenaNetta("N");

			mTotLibertaControllata.calcolaStringaSanzione();
		} else
			mTotLibertaControllata = null;

	}

	/**
	 * 
	 * @param aIdTitolo
	 * @return
	 */
	public TitoloCumulatoModel getTitoloCumulato(BigDecimal aIdTitolo) {
		TitoloCumulatoModel lTitolo = null;
		// mListaTitoli
		if (mListaTitoli != null) {
			for (TitoloCumulatoModel pTitolo : mListaTitoli) {
				if (pTitolo.getIdTitoloCumulato().compareTo(aIdTitolo) == 0) {
					lTitolo = pTitolo;
					break;
				}
			}
		}

		return lTitolo;

	}

	/**
	 * Verifica se preente Ergastolo tra i titoli cumulati e in caso positivo 
	 * Restituisce il model con i dati dell'ergastolo, null se non ergastolo.
	 * 
	 * In Test: se presenti più ergastoli di cui alcuni con e altri senza isolamento
	 * diurno restituisce come tipo ergasto quello con isolamento e se presenti
	 * più ergastoli con isolamento somma i periodi
	 * 
	 * @return
	 */
	public PenaComplessivaCumuloModel getErgastolo() {
		PenaComplessivaCumuloModel lErgastolo = null;

	  CalendarUtil lCalUtil = new CalendarUtil();
		CalendarModel lCalTotIsolamento= new CalendarModel();
		
		String lTipoErgastolo = "";
		String lDescrTipoErgastolo = "";
		
		// FIXME da implementare. Attualmente restituisce solo 
		if (mListaPeneComplessive != null) {
			for (int i = 0; i < mListaPeneComplessive.size(); i++) {
				PenaComplessivaCumuloModel lPena = mListaPeneComplessive.elementAt(i);
				if (   "03".equals(lPena.getCodTipoPenaDetentiva())  // Ergastolo
						|| "04".equals(lPena.getCodTipoPenaDetentiva())) // Ergastolo con isolamento
				{				  
				  if (!"04".equals(lTipoErgastolo)) {// Se già con isolamento lascio
				    lTipoErgastolo = lPena.getCodTipoPenaDetentiva();
				    lDescrTipoErgastolo = lPena.getDescrTipoPenaDetentiva();
				  }
				  
				  // Se con isolamento sommo la durata dell'isolamento
				  if ("04".equals(lPena.getCodTipoPenaDetentiva())){
				    CalendarModel lCalIsolamento= new CalendarModel();
				    
				    lCalIsolamento.setNumAnni   (lPena.getNumAnniIsolamentoDiurno());
				    lCalIsolamento.setNumMesi   (lPena.getNumMesiIsolamentoDiurno());
				    lCalIsolamento.setNumGiorni (lPena.getNumGiorniIsolamentoDiurno());

				    lCalTotIsolamento = lCalUtil.sommaGiornieValute(lCalTotIsolamento, lCalIsolamento);
				  }
					//return lPena;
				}
			}
		}

		if (!"".equals(lTipoErgastolo)) {
		  lErgastolo = new PenaComplessivaCumuloModel();
		  
		  lErgastolo.setCodTipoPenaDetentiva   (lTipoErgastolo);
		  lErgastolo.setDescrTipoPenaDetentiva (lDescrTipoErgastolo);
		  
      lErgastolo.setNumAnniIsolamentoDiurno   (new BigDecimal(lCalTotIsolamento.getNumAnni()));
      lErgastolo.setNumMesiIsolamentoDiurno   (new BigDecimal(lCalTotIsolamento.getNumMesi()));
      lErgastolo.setNumGiorniIsolamentoDiurno (new BigDecimal(lCalTotIsolamento.getNumGiorni()));
		}
		
		return lErgastolo;
	}

	/**
	 * Restituisce i soli provvedimenti di tipo Benefici Amnistia/Indulto/Depenalizzazione
	 * 
	 * @return
	 */
	public Vector<StatoEsecTitoloCumulatoModel> getProvvBenefici() {
		Vector<StatoEsecTitoloCumulatoModel> lListaProvvBenefici = new Vector<StatoEsecTitoloCumulatoModel>();

		if (mListaProvvedimenti != null) {
			// siesLogger.debug(""+mListaProvvedimenti.size());

			for (StatoEsecTitoloCumulatoModel lProvvedimento : mListaProvvedimenti) {
				// siesLogger.debug("["+lProvvedimento.getCodTipoEvento()
				// +" - "+lProvvedimento.getCodTipoProvvedimento()
				// +" - "+lProvvedimento.getCodMotivo()+"]");

				if ("01".equals(lProvvedimento.getCodTipoEvento())
						&& "03".equals(lProvvedimento.getCodTipoProvvedimento())
						&& ("0284".equals(lProvvedimento.getCodMotivo()) // Amnistia/Indulto
								|| "0285".equals(lProvvedimento.getCodMotivo()) // Depenalizzazione
						|| "0286".equals(lProvvedimento.getCodMotivo()) // Incostituzionalità
						)) {
					lListaProvvBenefici.add(lProvvedimento);
				}
			}
		}

		return lListaProvvBenefici;
	}

	/**
	 * Restituisce i provvedimenti di computo: - Presofferto stesso Titolo (01-04-0121) - Presofferto altro
	 * Titolo (01-04-0212) - Fungibilità (01-04-0213)
	 * 
	 * @return
	 */
	public Vector<StatoEsecTitoloCumulatoModel> getProvvComputi() {
		Vector<StatoEsecTitoloCumulatoModel> lListaProvvBenefici = new Vector<StatoEsecTitoloCumulatoModel>();

		if (mListaProvvedimenti != null) {
			for (StatoEsecTitoloCumulatoModel lProvvedimento : mListaProvvedimenti) {
				if ("01".equals(lProvvedimento.getCodTipoEvento())
						&& "04".equals(lProvvedimento.getCodTipoProvvedimento())
						&& ("0121".equals(lProvvedimento.getCodMotivo()) // Presofferto Stesso Reato
								|| "0212".equals(lProvvedimento.getCodMotivo()) // Presofferto Altro Reato
						|| "0213".equals(lProvvedimento.getCodMotivo()) // Fungibilità
						)) {
					siesLogger.debug("id: " + lProvvedimento.getIdStatoEsecTitoloCumulato() + "["
							+ lProvvedimento.getCodTipoEvento() + " - "
							+ lProvvedimento.getCodTipoProvvedimento() + " - "
							+ lProvvedimento.getCodMotivo() + "]");
					lListaProvvBenefici.add(lProvvedimento);
				}
			}
		}

		return lListaProvvBenefici;
	}

	/**
	 * 
	 * @return
	 */
	public Vector<StatoEsecTitoloCumulatoModel> getProvvEspiato() {
		Vector<StatoEsecTitoloCumulatoModel> lListaProvvEspiato = new Vector<StatoEsecTitoloCumulatoModel>();

		if (mListaProvvedimenti != null) {
			for (StatoEsecTitoloCumulatoModel lProvvedimento : mListaProvvedimenti) {
				if ("01".equals(lProvvedimento.getCodTipoEvento())
						&& ("04".equals(lProvvedimento.getCodTipoProvvedimento())
								|| "09".equals(lProvvedimento.getCodTipoProvvedimento())
								|| "12".equals(lProvvedimento.getCodTipoProvvedimento()) || "25"
									.equals(lProvvedimento.getCodTipoProvvedimento()))
						&& StatoEsecuzioneCumuloUtils.isEspiazionePregressaPM(lProvvedimento.getCodMotivo())) {
					siesLogger.debug("id: " + lProvvedimento.getIdStatoEsecTitoloCumulato() + "["
							+ lProvvedimento.getCodTipoEvento() + " - "
							+ lProvvedimento.getCodTipoProvvedimento() + " - "
							+ lProvvedimento.getCodMotivo() + "]");

					lListaProvvEspiato.add(lProvvedimento);
				}
			}
		}

		return lListaProvvEspiato;
	}

	// 04/04/2019  MEV70 Carcerazione presofferta a seguito revoca di Misure Alternative.
	/**
	 * 
	 * @return
	 */
	public Vector<StatoEsecTitoloCumulatoModel> getProvvEspiatoRevocaMA() {
		Vector<StatoEsecTitoloCumulatoModel> lListaProvvEspiatoRevocaMA = new Vector<StatoEsecTitoloCumulatoModel>();

		if (mListaProvvedimenti != null) {
			for (StatoEsecTitoloCumulatoModel lProvvedimento : mListaProvvedimenti) {
				if ("01".equals(lProvvedimento.getCodTipoEvento())
						&& ("02".equals(lProvvedimento.getCodTipoProvvedimento())
						 || "03".equals(lProvvedimento.getCodTipoProvvedimento()) )
						&& StatoEsecuzioneCumuloUtils.isRidPenaRevocaMA(lProvvedimento.getCodMotivo())) {
					siesLogger.debug("id: " + lProvvedimento.getIdStatoEsecTitoloCumulato() + "["
							+ lProvvedimento.getCodTipoEvento() + " - "
							+ lProvvedimento.getCodTipoProvvedimento() + " - "
							+ lProvvedimento.getCodMotivo() + "]");

					lListaProvvEspiatoRevocaMA.add(lProvvedimento);
				}
			}
		}
		return lListaProvvEspiatoRevocaMA;
	}

	// 16/04/2019  MEV70 Periodi di Sospensione / Differimento della Pena.
	/**
	 * 
	 * @return
	 */
	public Vector<StatoEsecTitoloCumulatoModel> getProvvSospDiff() {
		Vector<StatoEsecTitoloCumulatoModel> lListaProvvSospDiff = new Vector<StatoEsecTitoloCumulatoModel>();

		if (mListaProvvedimenti != null) {
			for (StatoEsecTitoloCumulatoModel lProvvedimento : mListaProvvedimenti) {
				if ("01".equals(lProvvedimento.getCodTipoEvento())
						&& ("02".equals(lProvvedimento.getCodTipoProvvedimento())
						 || "03".equals(lProvvedimento.getCodTipoProvvedimento()) )
						&& StatoEsecuzioneCumuloUtils.isSospDiff(lProvvedimento.getCodMotivo())) {
					siesLogger.debug("id: " + lProvvedimento.getIdStatoEsecTitoloCumulato() + "["
							+ lProvvedimento.getCodTipoEvento() + " - "
							+ lProvvedimento.getCodTipoProvvedimento() + " - "
							+ lProvvedimento.getCodMotivo() + "]");

					lListaProvvSospDiff.add(lProvvedimento);
				}
			}
		}
		return lListaProvvSospDiff;
	}
	
	/**
	 * 
	 * @return
	 */
	public Vector<StatoEsecTitoloCumulatoModel> getProvvPagamentoPP() {
		Vector<StatoEsecTitoloCumulatoModel> lListaProvvPP = new Vector<StatoEsecTitoloCumulatoModel>();

		if (mListaProvvedimenti != null) {
			for (StatoEsecTitoloCumulatoModel lProvvedimento : mListaProvvedimenti) {
				EventoModel lEventoRicerca = new EventoModel();
				lEventoRicerca.setCodTipoEvento(lProvvedimento.getCodTipoEvento());
				lEventoRicerca.setCodTipoProvvedimento(lProvvedimento.getCodTipoProvvedimento());
				lEventoRicerca.setCodMotivo(lProvvedimento.getCodMotivo());

				if (StatoEsecuzioneCumuloUtils.isPagamentoPP(lEventoRicerca)) {
					siesLogger.debug("[" + lProvvedimento.getCodTipoEvento() + " - "
							+ lProvvedimento.getCodTipoProvvedimento() + " - "
							+ lProvvedimento.getCodMotivo() + "]");
					lListaProvvPP.add(lProvvedimento);
				}
			}
		}

		return lListaProvvPP;
	}

	/**
	 * Restituisce i provvedimenti di LA e DL92 e Scomputi permesso
	 * @return
	 */
	public Vector<StatoEsecTitoloCumulatoModel> getProvvLADL92() {
		Vector<StatoEsecTitoloCumulatoModel> lListaProvv = new Vector<StatoEsecTitoloCumulatoModel>();
		Vector<LibAnticipataCumuloModel> lListaLATOT = this.getListaLibAnticipate();

		if (mListaProvvedimenti != null) {
			for (StatoEsecTitoloCumulatoModel lProvvedimento : mListaProvvedimenti) {
				EventoModel lEventoRicerca = new EventoModel();
				lEventoRicerca.setCodTipoEvento(lProvvedimento.getCodTipoEvento());
				lEventoRicerca.setCodTipoProvvedimento(lProvvedimento.getCodTipoProvvedimento());
				lEventoRicerca.setCodMotivo(lProvvedimento.getCodMotivo());

				if (StatoEsecuzioneCumuloUtils.isLiberazioneAnticipata(lEventoRicerca)
						|| StatoEsecuzioneCumuloUtils.isRimediRisarcitori(lEventoRicerca)
            || StatoEsecuzioneCumuloUtils.isScomputoPermessi(lEventoRicerca)
						) 
				{
					siesLogger.debug("[" + lProvvedimento.getCodTipoEvento() + " - "
							+ lProvvedimento.getCodTipoProvvedimento() + " - "
							+ lProvvedimento.getCodMotivo() + "]");

					lListaProvv.add(lProvvedimento);

					// Aggiungo le LA al provvedimento
					Vector<LibAnticipataCumuloModel> lListaLAProvv = new Vector<LibAnticipataCumuloModel>();
					for (LibAnticipataCumuloModel lLibAntModel : lListaLATOT) {
						if (lLibAntModel.getStatIdStatoEsecTitoloCum().compareTo(
								lProvvedimento.getIdStatoEsecTitoloCumulato()) == 0)
						{
							lListaLAProvv.add(lLibAntModel);
						}
					}
					lProvvedimento.setListaLiberazioniAnticipate(lListaLAProvv);
				}
			}
		}

		return lListaProvv;
	}

	/**
	 * Restituisce le annotazioni di rideterminazione pena PM altro
	 * @return
	 */
	public Vector<StatoEsecTitoloCumulatoModel> getRidetPenaPMAltro() {
		Vector<StatoEsecTitoloCumulatoModel> lListaRidetPenaPMAltro = new Vector<StatoEsecTitoloCumulatoModel>();

		if (mListaProvvedimenti != null) {
			for (StatoEsecTitoloCumulatoModel lProvvedimento : mListaProvvedimenti) {
				EventoModel lEventoRicerca = new EventoModel();
				lEventoRicerca.setCodTipoEvento(lProvvedimento.getCodTipoEvento());
				lEventoRicerca.setCodTipoProvvedimento(lProvvedimento.getCodTipoProvvedimento());
				lEventoRicerca.setCodMotivo(lProvvedimento.getCodMotivo());

				if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroDufficio(lEventoRicerca.getCodMotivo())	||
					StatoEsecuzioneCumuloUtils.isRidPenaPMAltroAltAut(lEventoRicerca.getCodMotivo()) 	||
					StatoEsecuzioneCumuloUtils.isRidPenaPMAltroGE(lEventoRicerca.getCodMotivo())	 	||
					StatoEsecuzioneCumuloUtils.isRidPenaPMAltroSorv(lEventoRicerca.getCodMotivo())  ) {
					siesLogger.debug("[" + lProvvedimento.getCodTipoEvento() + " - "
							+ lProvvedimento.getCodTipoProvvedimento() + " - "
							+ lProvvedimento.getCodMotivo() + "]");
					lListaRidetPenaPMAltro.add(lProvvedimento);
				}
			}
		}

		return lListaRidetPenaPMAltro;
	}
	
	/**
	 * Restituisce le annotazioni di Revoca di Misura Alternativa 02/04/1990 MEV70
	 * @return
	 */
	public Vector<StatoEsecTitoloCumulatoModel> getRidetPenaPerRevocaMA() {
		Vector<StatoEsecTitoloCumulatoModel> lListaRidetPenaRevocaMA = new Vector<StatoEsecTitoloCumulatoModel>();

		if (mListaProvvedimenti != null) {
			for (StatoEsecTitoloCumulatoModel lProvvedimento : mListaProvvedimenti) {
				EventoModel lEventoRicerca = new EventoModel();
				lEventoRicerca.setCodTipoEvento(lProvvedimento.getCodTipoEvento());
				lEventoRicerca.setCodTipoProvvedimento(lProvvedimento.getCodTipoProvvedimento());
				lEventoRicerca.setCodMotivo(lProvvedimento.getCodMotivo());

				if (StatoEsecuzioneCumuloUtils.isRidPenaRevocaMA(lEventoRicerca.getCodMotivo())	 ) {
					siesLogger.debug("[" + lProvvedimento.getCodTipoEvento() + " - "
							+ lProvvedimento.getCodTipoProvvedimento() + " - "
							+ lProvvedimento.getCodMotivo() + "]");
					lListaRidetPenaRevocaMA.add(lProvvedimento);
				}
			}
		}

		return lListaRidetPenaRevocaMA;
	}
	
	
	/**
	 * Ritorna il totale delle richieste al GE per tipologia di pena: reclusione, Arresto
	 * 
	 * Se presente la decisione viene presa in considerazione la decisione. Se la decisione è di Rigetto o
	 * Inammissibilità, non va computata ne la richiesta con anticipazione nel la decisione FLAG_CONFORME
	 * (R,I)
	 * 
	 * @param aTipoPena
	 *            R = Reclusione A=Arresto
	 * @param aFlagConcRev
	 *            C concessione, R Revoche, null tutte
	 * @return
	 */
	public CalendarModel getRichiesteTotali(String aTipoPena, String aFlagConcRev) {
	  siesLogger.debug("aTipoPena = "+aTipoPena+" - aFlagConcRev = "+aFlagConcRev);
	  siesLogger.debug("mListaRichiestePM.size() = "+mListaRichiestePM.size());
	  
		CalendarModel lTotRichieste = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusioneTotModConc = new CalendarModel();
		CalendarModel lCalArrestiTotModConc = new CalendarModel();

		CalendarModel lCalReclusioneTotModRev = new CalendarModel();
		CalendarModel lCalArrestiTotModRev = new CalendarModel();

		for (int i = 0; i < mListaRichiestePM.size(); i++) {
			RichiestePmInCumuloModel lRichiesta = mListaRichiestePM.elementAt(i);
			
			siesLogger.debug("lRichiesta = "+lRichiesta.getIdRichiestePmInCumulo()
			    +" - "+lRichiesta.getCodTipoAnnotazione()
          +" - "+lRichiesta.getNumAnniReclusioneR()
          +" - "+lRichiesta.getNumMesiReclusioneR()
          +" - "+lRichiesta.getNumGiorniReclusioneR()         
      );
      //siesLogger.debug("lDecisione = "+lRichiesta.getDecisioneGeSorvCum());
			
			ProvvedimentoGeSorvCumModel lDecisione = lRichiesta.getDecisioneGeSorvCum();
			CalendarModel lReclusioneMulta = new CalendarModel();
			CalendarModel lArrestoAmmenda = new CalendarModel();

			if (lDecisione != null) {
			  siesLogger.debug("Presente decisione: prendo i dati della decisione ");
				lReclusioneMulta = lDecisione.getQuantumReclusione();
				if (lDecisione.getImportoMultaD() != null)
					lReclusioneMulta.setImportoMulta(lDecisione.getImportoMultaD().doubleValue());

				lArrestoAmmenda = lDecisione.getQuantumArresto();
				if (lDecisione.getImportoAmmendaD() != null)
					lArrestoAmmenda.setImportoAmmenda(lDecisione.getImportoAmmendaD().doubleValue());
			} else {
			  siesLogger.debug("Decisione assente: prendo i dati della richiesta ");
				lReclusioneMulta = lRichiesta.getQuantumReclusione();
				if (lRichiesta.getImportoMultaR() != null)
					lReclusioneMulta.setImportoMulta(lRichiesta.getImportoMultaR().doubleValue());

				lArrestoAmmenda = lRichiesta.getQuantumArresto();
				if (lRichiesta.getImportoAmmendaR() != null)
					lArrestoAmmenda.setImportoAmmenda(lRichiesta.getImportoAmmendaR().doubleValue());
			}

			siesLogger.debug("lReclusioneMulta da Sommare = "+lReclusioneMulta);
			
			if (!"021".equals(lRichiesta.getCodTipoAnnotazione())
					&& !"023".equals(lRichiesta.getCodTipoAnnotazione())) {
				// Recupero Reclusione e Multa
				// lReclusioneMulta.setNumAnni (lRichiesta.getNumAnniReclusioneR());
				// lReclusioneMulta.setNumMesi (lRichiesta.getNumMesiReclusioneR());
				// lReclusioneMulta.setNumGiorni (lRichiesta.getNumGiorniReclusioneR());
				// if (lRichiesta.getImportoMultaR() != null)
				// lReclusioneMulta.setImportoMulta (lRichiesta.getImportoMultaR().doubleValue());
				//
				// // Recupero l'Arresto e Ammenda
				// lArrestoAmmenda.setNumAnni (lRichiesta.getNumAnniArrestoR());
				// lArrestoAmmenda.setNumMesi (lRichiesta.getNumMesiArrestoR());
				// lArrestoAmmenda.setNumGiorni (lRichiesta.getNumGiorniArrestoR());
				// if (lRichiesta.getImportoAmmendaR() != null)
				// lArrestoAmmenda.setImportoAmmenda (lRichiesta.getImportoAmmendaR().doubleValue());

				lCalReclusioneTotModConc = lCalUtil.sommaGiornieValute(lCalReclusioneTotModConc,
						lReclusioneMulta);
				lCalArrestiTotModConc = lCalUtil.sommaGiornieValute(lCalArrestiTotModConc, lArrestoAmmenda);
			} else {
				// 021 Richieste di Revoca Benefici
				// 023 Richiesta di Revoca Sanzione Sostitutiva - I quantum vanno sommati alle revoche
				// CalendarModel lReclusioneMulta = new CalendarModel();
				// CalendarModel lArrestoAmmenda = new CalendarModel();
				//
				// // Recupero Reclusione e Multa
				// lReclusioneMulta.setNumAnni (lRichiesta.getNumAnniReclusioneR());
				// lReclusioneMulta.setNumMesi (lRichiesta.getNumMesiReclusioneR());
				// lReclusioneMulta.setNumGiorni (lRichiesta.getNumGiorniReclusioneR());
				// if (lRichiesta.getImportoMultaR() != null)
				// lReclusioneMulta.setImportoMulta (lRichiesta.getImportoMultaR().doubleValue());
				//
				// // Recupero l'Arresto e Ammenda
				// lArrestoAmmenda.setNumAnni (lRichiesta.getNumAnniArrestoR());
				// lArrestoAmmenda.setNumMesi (lRichiesta.getNumMesiArrestoR());
				// lArrestoAmmenda.setNumGiorni (lRichiesta.getNumGiorniArrestoR());
				// if (lRichiesta.getImportoAmmendaR() != null)
				// lArrestoAmmenda.setImportoAmmenda (lRichiesta.getImportoAmmendaR().doubleValue());

				lCalReclusioneTotModRev = lCalUtil.sommaGiornieValute(lCalReclusioneTotModRev,
						lReclusioneMulta);
				lCalArrestiTotModRev = lCalUtil.sommaGiornieValute(lCalArrestiTotModRev, lArrestoAmmenda);
			}
		}

		CalendarModel lCalReclusioneTotMod = null;
		CalendarModel lCalArrestiTotMod = null;

		if ("C".equals(aFlagConcRev)) {
			lCalReclusioneTotMod = lCalReclusioneTotModConc;
			lCalArrestiTotMod = lCalArrestiTotModConc;
		} else if ("R".equals(aFlagConcRev)) {
			lCalReclusioneTotMod = lCalReclusioneTotModRev;
			lCalArrestiTotMod = lCalArrestiTotModRev;
		} else {
			lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotModConc,
					lCalReclusioneTotModRev);
			lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotModConc, lCalArrestiTotModRev);
		}

		if ("R".equals(aTipoPena))
			lTotRichieste = lCalReclusioneTotMod;
		else if ("A".equals(aTipoPena))
			lTotRichieste = lCalArrestiTotMod;

		return lTotRichieste;
	}

	/**
	 * 
	 */
	public String toString () {
	  String lString = "";
	  lString+="\n================================================================\n";
	  lString+=" Quantità costituenti il calcolo della pena sul cumulo: \n";
    lString+="================================================================\n";
	  
    lString+="===================\n";
    lString+=" Pene complessive: \n";
    lString+="===================\n";
	  for (PenaComplessivaCumuloModel lPenaCompl: mListaPeneComplessive) {
      String lReclusione = "Reclusione:";
      lReclusione+=" Anni "+(lPenaCompl.getNumAnniReclusione()==null ? "0" : lPenaCompl.getNumAnniReclusione() );
      lReclusione+=" Mesi "+(lPenaCompl.getNumMesiReclusione()==null ? "0" : lPenaCompl.getNumMesiReclusione() );
      lReclusione+=" Giorni "+(lPenaCompl.getNumGiorniReclusione()==null ? "0" : lPenaCompl.getNumGiorniReclusione() );
      lReclusione+=" Multa "+(lPenaCompl.getImportoMulta()==null ? "0" : StringUtils.toEuroFormat(lPenaCompl.getImportoMulta()) );
      
      String lArresto = "Arresto:";
      lArresto+=" Anni "+(lPenaCompl.getNumAnniArresto()==null ? "0" : lPenaCompl.getNumAnniArresto() );
      lArresto+=" Mesi "+(lPenaCompl.getNumMesiArresto()==null ? "0" : lPenaCompl.getNumMesiArresto() );
      lArresto+=" Giorni "+(lPenaCompl.getNumGiorniArresto()==null ? "0" : lPenaCompl.getNumGiorniArresto() );
      lArresto+=" Multa "+(lPenaCompl.getImportoAmmenda()==null ? "0" : StringUtils.toEuroFormat(lPenaCompl.getImportoAmmenda()) );
     
      lString+=lReclusione+" "+lArresto+"\n" ;
	  }
	  
    lString+="===================\n";
    lString+=" Misure Cautelari: \n";
    lString+="===================\n";
    for (MisuraCautelareCumuloModel lMisuraCautel: mListaMisureCautelari) {
      String lReclusione = "Reclusione:";
      lReclusione+=" Anni "+(lMisuraCautel.getNumAnni()==null ? "0" : lMisuraCautel.getNumAnni() );
      lReclusione+=" Mesi "+(lMisuraCautel.getNumMesi()==null ? "0" : lMisuraCautel.getNumMesi() );
      lReclusione+=" Giorni "+(lMisuraCautel.getNumGiorni()==null ? "0" : lMisuraCautel.getNumGiorni() );      
     
      lString+=lMisuraCautel.getCodTipoMisura()+" - dal "+DateUtils.getDateToString(lMisuraCautel.getDataInizio(), "dd/MM/yyyy")
          +" al "+DateUtils.getDateToString(lMisuraCautel.getDataFine(), "dd/MM/yyyy")
          +" Tot "+lReclusione+"\n" ;
    }
	  
    lString+="===================================\n";
    lString+=" Computi Misure Cautelari (provv): \n";
    lString+="===================================\n";
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvComputo = this.getProvvComputi();
    for (StatoEsecTitoloCumulatoModel lProvvComputo: lListaProvvComputo) {
      Vector <ComputiCumuloModel> lListaComputi = lProvvComputo.getListaComputi();
      String lProvv = "["+lProvvComputo.getIdStatoEsecTitoloCumulato()+"-"+lProvvComputo.getCodTipoProvvedimento()+"-"+lProvvComputo.getCodMotivo()+"]";
      
      for (ComputiCumuloModel lComputo: lListaComputi) {
        String lReclusione = "Reclusione:";
        lReclusione+=" Anni "+(lComputo.getNumAnniReclusione()==null ? "0" : lComputo.getNumAnniReclusione() );
        lReclusione+=" Mesi "+(lComputo.getNumMesiReclusione()==null ? "0" : lComputo.getNumMesiReclusione() );
        lReclusione+=" Giorni "+(lComputo.getNumGiorniReclusione()==null ? "0" : lComputo.getNumGiorniReclusione() );      
     
        lString+=lProvv+" "+lComputo.getCodTipoAnnotazione()+" - dal "+DateUtils.getDateToString(lComputo.getDataReclusioneDa(), "dd/MM/yyyy")
            +" al "+DateUtils.getDateToString(lComputo.getDataReclusioneA(), "dd/MM/yyyy")
            +" Tot "+lReclusione+"\n" ;
      }
    }    
	  
    lString+="===================================\n";
    lString+=" Computi Espiato (provv): \n";
    lString+="===================================\n";
    Vector <StatoEsecTitoloCumulatoModel> lListaEspiato = this.getProvvEspiato();
    for (StatoEsecTitoloCumulatoModel lProvvComputo: lListaEspiato) {
      Vector <ComputiCumuloModel> lListaComputi = lProvvComputo.getListaComputi();
      String lProvv = "["+lProvvComputo.getIdStatoEsecTitoloCumulato()+"-"+lProvvComputo.getCodTipoProvvedimento()+"-"+lProvvComputo.getCodMotivo()+"]";
      for (ComputiCumuloModel lComputo: lListaComputi) {
        String lReclusione = "Reclusione:";
        lReclusione+=" Anni "+(lComputo.getNumAnniReclusione()==null ? "0" : lComputo.getNumAnniReclusione() );
        lReclusione+=" Mesi "+(lComputo.getNumMesiReclusione()==null ? "0" : lComputo.getNumMesiReclusione() );
        lReclusione+=" Giorni "+(lComputo.getNumGiorniReclusione()==null ? "0" : lComputo.getNumGiorniReclusione() );      
     
        lString+=lProvv+" "+lComputo.getCodTipoAnnotazione()+" - dal "+DateUtils.getDateToString(lComputo.getDataReclusioneDa(), "dd/MM/yyyy")
            +" al "+DateUtils.getDateToString(lComputo.getDataReclusioneA(), "dd/MM/yyyy")
            +" Tot "+lReclusione+"\n" ;
      }
    }    
    
	  lString+="===========\n";
    lString+=" Benefici: \n";
    lString+="===========\n";
    for (BeneficioCumuloModel lBeneficio: mListaBenefici) {
      String lReclusione = "Reclusione:";
      lReclusione+=" Anni "+(lBeneficio.getNumAnniReclusione()==null ? "0" : lBeneficio.getNumAnniReclusione() );
      lReclusione+=" Mesi "+(lBeneficio.getNumMesiReclusione()==null ? "0" : lBeneficio.getNumMesiReclusione() );
      lReclusione+=" Giorni "+(lBeneficio.getNumGiorniReclusione()==null ? "0" : lBeneficio.getNumGiorniReclusione() );
      lReclusione+=" Multa "+(lBeneficio.getImportoMulta()==null ? "0" : StringUtils.toEuroFormat(lBeneficio.getImportoMulta()) );
      
      String lArresto = "Arresto:";
      lArresto+=" Anni "+(lBeneficio.getNumAnniArresto()==null ? "0" : lBeneficio.getNumAnniArresto() );
      lArresto+=" Mesi "+(lBeneficio.getNumMesiArresto()==null ? "0" : lBeneficio.getNumMesiArresto() );
      lArresto+=" Giorni "+(lBeneficio.getNumGiorniArresto()==null ? "0" : lBeneficio.getNumGiorniArresto() );
      lArresto+=" Multa "+(lBeneficio.getImportoAmmenda()==null ? "0" : StringUtils.toEuroFormat(lBeneficio.getImportoAmmenda()) );
     
      lString+=lBeneficio.getCodNaturaBeneficio()+" "+lReclusione+" "+lArresto+"\n" ;
    }
    
    lString+="===========\n";
    lString+=" Computi : \n";
    lString+="===========\n";
    for (ComputiCumuloModel lComputo: mListaComputi) {
      String lReclusione = "Reclusione:";
      lReclusione+=" Anni "+(lComputo.getNumAnniReclusione()==null ? "0" : lComputo.getNumAnniReclusione() );
      lReclusione+=" Mesi "+(lComputo.getNumMesiReclusione()==null ? "0" : lComputo.getNumMesiReclusione() );
      lReclusione+=" Giorni "+(lComputo.getNumGiorniReclusione()==null ? "0" : lComputo.getNumGiorniReclusione() );
      lReclusione+=" Multa "+(lComputo.getImportoMulta()==null ? "0" : StringUtils.toEuroFormat(lComputo.getImportoMulta()) );
      
      String lArresto = "Arresto:";
      lArresto+=" Anni "+(lComputo.getNumAnniArresto()==null ? "0" : lComputo.getNumAnniArresto() );
      lArresto+=" Mesi "+(lComputo.getNumMesiArresto()==null ? "0" : lComputo.getNumMesiArresto() );
      lArresto+=" Giorni "+(lComputo.getNumGiorniArresto()==null ? "0" : lComputo.getNumGiorniArresto() );
      lArresto+=" Multa "+(lComputo.getImportoAmmenda()==null ? "0" : StringUtils.toEuroFormat(lComputo.getImportoAmmenda()) );
     
      lString+=lComputo.getCodTipoAnnotazione()+" "+lComputo.getFlagPiuMeno()+" "+lReclusione+" "+lArresto+"\n" ;
    }
    
    lString+="================\n";
    lString+=" Richieste PM : \n";
    lString+="================\n";
    for (RichiestePmInCumuloModel lRichiesta: mListaRichiestePM) {
      String lReclusione = "Reclusione:";
      lReclusione+=" Anni "+(lRichiesta.getNumAnniReclusioneR()==null ? "0" : lRichiesta.getNumAnniReclusioneR() );
      lReclusione+=" Mesi "+(lRichiesta.getNumMesiReclusioneR()==null ? "0" : lRichiesta.getNumMesiReclusioneR() );
      lReclusione+=" Giorni "+(lRichiesta.getNumGiorniReclusioneR()==null ? "0" : lRichiesta.getNumGiorniReclusioneR() );
      lReclusione+=" Multa "+(lRichiesta.getImportoMultaR()==null ? "0" : StringUtils.toEuroFormat(lRichiesta.getImportoMultaR()) );
      
      String lArresto = "Arresto:";
      lArresto+=" Anni "+(lRichiesta.getNumAnniArrestoR()==null ? "0" : lRichiesta.getNumAnniArrestoR() );
      lArresto+=" Mesi "+(lRichiesta.getNumMesiArrestoR()==null ? "0" : lRichiesta.getNumMesiArrestoR() );
      lArresto+=" Giorni "+(lRichiesta.getNumGiorniArrestoR()==null ? "0" : lRichiesta.getNumGiorniArrestoR() );
      lArresto+=" Multa "+(lRichiesta.getImportoAmmendaR()==null ? "0" : StringUtils.toEuroFormat(lRichiesta.getImportoAmmendaR()) );
     
      lString+=lRichiesta.getCodTipoRichiesta()+"-"+lRichiesta.getCodTipoAnnotazione()+" "+lRichiesta.getFlagPiuMenoR()+" "+lReclusione+" "+lArresto+"\n" ;
    }
	  
	  return lString;
	}
}