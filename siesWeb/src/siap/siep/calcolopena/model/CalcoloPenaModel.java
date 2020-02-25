package siap.siep.calcolopena.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.CalendarUtil;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.calcolopena.action.ICostantiCalcoloPena;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.CaricaHTML_Servlet;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: CalcoloPenaModel
 * </p>
 * <p>
 * Description: Classe Model contenete tutti i dati (Model) che concorrono al calcolo della pena in un certo
 * istante
 * </p>
 * <p>
 * Espone i metodi per recuperare i dati ed effettuare i calcoli. La sequenza corretta di invocazione dei
 * metodi è: es: CalcoloPenaModel lCalcoloPenaMod = null; ActCalcoloPenaMain lActCalcoloPenaMain = new
 * ActCalcoloPenaMain();
 * 
 * -- Recupero tutti i dati già a aistema (VALIDATI) per il fascicolo ed aventualmente -- per l'evento.
 * lCalcoloPenaMod = lActCalcoloPenaMain.calcoloPena(BigDecimal lFascID, BigDecimal lIdEvento)
 * 
 * -- Aggiungere eventuali altri dati (es Presofferto)
 * lCalcoloPenaMod.getPresoffertoAltroReato().add(lAnnMod);
 * 
 * -- Recupero la pena da espiare (PenaResiduaModel) calcolata a partire dai dati -- a sistema e dagli
 * eventuali dati aggiunti
 * 
 * PenaResiduaModel lPenResMod = lCalPenaMod.getPenaDaEspiare(); FungibilitaModel lFungModel =
 * lCalcoloPenaModel.getFungibilitaCalcolata(); CalendarModel lPenaGiaEspiata =
 * lCalcoloPenaModel.getPenaEspiata();
 *
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CalcoloPenaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5543231268907673781L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	private int mTipoPenaIniziale;

	private Date mDataDal; // Data a partire dalla quale vanno considerati i dati da computare
	private Date mDataAl; // Data fino alla quale vanno considerati i dati da computare

	// Evento di pena iniziale, null se la pena iniziale è la pena in sentenza
	private EventoModel mEventoPenaIniziale;

	// Dati utilizzati quando la pena iniziale è la Pena in Sentenza
	private PenaComplessivaModel mPenaInSentenza;
	private Vector mBeneficiInSentenza; // Benefici concessi/revocati
	private Vector mMisureCautelariInSentenza;
	private SanzioneSostitutivaModel mSanzioneSostitutiva; // Sanzione Sostitutiva in sentenza
	private SanzioneSostResiduaModel mUltimaSanzSostResidua; // Ultima Sanzione Sostitutiva Residua Validata

	// Pene Iniziali se diversa da Pena Irrogata in sentenza
	private PenaResiduaModel mPenaIrrogataInCumulo;
	private PenaResiduaModel mPenaDopoSospensione; // Pena residua associata alla sospensione
	private PenaResiduaModel mPenaDopoRevocaMA; // Pena residua associata alla revoca di un affidamento in
												// prova
	private PenaResiduaModel mPenaDopoRevocaIndultino;
	private PenaResiduaModel mPenaResiduaManuale;
	private PenaResiduaModel mPenaDaArchiviazione;
	private PenaResiduaModel mPenaDaIndulto;
	private PenaResiduaModel mPenaUltimaValidata;
	private PenaResiduaModel mPenaDaRevocaSS;

	// ==================================================
	// Dati da sommare/sottrarre alla Pena Iniziale
	// ==================================================
	// Richieste al GE (AnnotazioneManualeModel) con anticipazione
	private Vector mDepenalizzazioneR;
	private Vector mIncostituzionalitaR;
	private Vector mAmnistiaR;
	private Vector mIndultoR;

	// Decisioni del GE (AnnotazioneManualeModel)
	private Vector mDepenalizzazione;
	private Vector mIncostituzionalita;
	private Vector mAmnistia;
	private Vector mIndulto;

	// Computi (AnnotazioneManualeModel)
	private Vector mPresoffertoAltroReato;
	private Vector mFungibilitaAltroReatoMC; // Misura cautelare
	private Vector mFungibilitaAltroReatoPD; // Pena Detentiva
	private Vector mComputoAltro;
	private Vector mComputoRES; // Computi res di rideterminazione pena

	// Liberazioni Anticipate Concesse/Revocate
	private Vector mLibAnticipate;

	// Scomputi Permessi e Licenze (new v4.0)
	//private Vector mScomputiPermLic;

	// Vettore contenente i periodi già espiati a seguito di interruzioni/sospensioni...
	private Vector mPeneGiaEspiate;

	// Vettore contenente i periodi espiati in eccesso registrati sulla tabella
	// fungibilità
	private Vector mPeneEspiateInEccesso;

	// ============================================================================
	// Campi valorizzati in fase di calcolo della pena dal Metodo getPenaDaEspiare
	// ============================================================================
	// Pen Residua Ricalcolata
	private PenaResiduaModel mPenaResiduaRicalcolata;

	// Fungibilità questo reato Calcolata dalla getPenaDaEspiare
	private FungibilitaModel mFungibilitaCalcolata;

	// Pena espiata calcolata dalla getPenaDaEspiare
	private CalendarModel mPenaEspiata;

	private Date mDataRevocataDal; // Data 'Revocata Dal' per Revoca Indultino e Affidamento in prova

	// COSTRUTTORE DI DEFAULT
	public CalcoloPenaModel() {
		this.mTipoPenaIniziale = ICostantiCalcoloPena.PENA_NON_DEFINITA;

		this.mPenaInSentenza = null;
		this.mBeneficiInSentenza = new Vector();
		this.mMisureCautelariInSentenza = new Vector();

		// Pene Iniziali se diversa da Pena Irrogata in sentenza
		this.mPenaIrrogataInCumulo = null;
		this.mPenaDopoSospensione = null;
		this.mPenaDopoRevocaMA = null;
		this.mPenaDopoRevocaIndultino = null;
		this.mPenaResiduaManuale = null;
		this.mPenaDaArchiviazione = null;
		this.mPenaDaIndulto = null;
		this.mPenaUltimaValidata = null;

		// Richieste al GE (AnnotazioneManualeModel)
		this.mDepenalizzazioneR = new Vector();
		this.mIncostituzionalitaR = new Vector();
		this.mAmnistiaR = new Vector();
		this.mIndultoR = new Vector();

		// Decisioni del GE (AnnotazioneManualeModel)
		this.mDepenalizzazione = new Vector();
		this.mIncostituzionalita = new Vector();
		this.mAmnistia = new Vector();
		this.mIndulto = new Vector();

		// Computi (AnnotazioneManualeModel)
		this.mPresoffertoAltroReato = new Vector();
		this.mFungibilitaAltroReatoMC = new Vector();
		this.mFungibilitaAltroReatoPD = new Vector();
		this.mComputoAltro = new Vector();
		this.mComputoRES = new Vector();

		// Liberazioni anticipate
		this.mLibAnticipate = new Vector();
		// Scomputio Permessi/Licenze
		//this.mScomputiPermLic = new Vector();

		// Pene Già Espiate
		this.mPeneGiaEspiate = new Vector();
		this.mPeneEspiateInEccesso = new Vector();

		// Fungibilità calcolata
		this.mPenaResiduaRicalcolata = null;
		this.mFungibilitaCalcolata = null;
		this.mPenaEspiata = null;

		this.mDataRevocataDal = null;
	}

	// COSTRUTTORE DI COPIA
	public CalcoloPenaModel(CalcoloPenaModel aModel) {
		this.mTipoPenaIniziale = aModel.mTipoPenaIniziale;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public int getTipoPenaIniziale() {
		return mTipoPenaIniziale;
	}

	public Date getDataDal() {
		return mDataDal;
	}

	public Date getDataAl() {
		return mDataAl;
	}

	public EventoModel getEventoPenaIniziale() {
		return mEventoPenaIniziale;
	}

	public PenaComplessivaModel getPenaInSentenza() {
		return mPenaInSentenza;
	}

	public Vector getBeneficiInSentenza() {
		return mBeneficiInSentenza;
	}

	public Vector getMisureCautelariInSentenza() {
		return mMisureCautelariInSentenza;
	}

	public SanzioneSostitutivaModel getSanzioneSostitutiva() {
		return mSanzioneSostitutiva;
	}

	public SanzioneSostResiduaModel getUltimaSanzSostResidua() {
		return mUltimaSanzSostResidua;
	}

	// Pene Iniziali se diversa da Pena Irrogata in sentenza
	public PenaResiduaModel getPenaIrrogataInCumulo() {
		return mPenaIrrogataInCumulo;
	}

	public PenaResiduaModel getPenaDopoSospensione() {
		return mPenaDopoSospensione;
	}

	public PenaResiduaModel getPenaDopoRevocaMA() {
		return mPenaDopoRevocaMA;
	}

	public PenaResiduaModel getPenaDopoRevocaIndultino() {
		return mPenaDopoRevocaIndultino;
	}

	public PenaResiduaModel getPenaResiduaManuale() {
		return mPenaResiduaManuale;
	}

	public PenaResiduaModel getPenaDaArchiviazione() {
		return mPenaDaArchiviazione;
	}

	public PenaResiduaModel getPenaDaIndulto() {
		return mPenaDaIndulto;
	}

	public PenaResiduaModel getPenaUltimaValidata() {
		return mPenaUltimaValidata;
	}

	public PenaResiduaModel getPenaDaRevocaSS() {
		return mPenaDaRevocaSS;
	}

	// Richieste al GE (AnnotazioneManualeModel) con anticipazione
	public Vector getDepenalizzazioneR() {
		return mDepenalizzazioneR;
	}

	public Vector getIncostituzionalitaR() {
		return mIncostituzionalitaR;
	}

	public Vector getAmnistiaR() {
		return mAmnistiaR;
	}

	public Vector getIndultoR() {
		return mIndultoR;
	}

	// Decisioni del GE (AnnotazioneManualeModel)
	public Vector getDepenalizzazione() {
		return mDepenalizzazione;
	}

	public Vector getIncostituzionalita() {
		return mIncostituzionalita;
	}

	public Vector getAmnistia() {
		return mAmnistia;
	}

	public Vector getIndulto() {
		return mIndulto;
	}

	// Computi
	public Vector getPresoffertoAltroReato() {
		return mPresoffertoAltroReato;
	}

	public Vector getFungibilitaAltroReatoMC() {
		return mFungibilitaAltroReatoMC;
	}

	public Vector getFungibilitaAltroReatoPD() {
		return mFungibilitaAltroReatoPD;
	}

	public Vector getComputoAltro() {
		return mComputoAltro;
	}

	public Vector getComputoRES() {
		return mComputoRES;
	}

	public Date getDataRevocataDal() {
		return mDataRevocataDal;
	}

	// Liberazione Anticipata
	public Vector getLibAnticipate() {
		return mLibAnticipate;
	}

	// Scomputi Permessi e Licenze
//	public Vector getScomputiPermLic() {
//		return mScomputiPermLic;
//	}

	public Vector getPeneGiaEspiate() {
		return mPeneGiaEspiate;
	}

	/**
	 * Restituisce la PenaResidua ricalcolata all'ultima chiamata a getPenaDaEspiare()
	 * 
	 * @return pena residua ricalcolata
	 */
	public PenaResiduaModel getPenaResiduaRicalcolata() {
		return mPenaResiduaRicalcolata;
	}

	/**
	 * Restituisce la Fungibilità ricalcolata all'ultima chiamata a getPenaDaEspiare()
	 * 
	 * @return
	 */
	public FungibilitaModel getFungibilitaCalcolata() {
		return mFungibilitaCalcolata;
	}

	/**
	 * Il valore restituito dipende dalla sequenza di operazioni effettuate: 1) dopo la chiamata a
	 * getPenaDaEspiare viene valorizzata con la pena in corso di espiazione. Il quantum restituito è
	 * calcolato tra la aDataInizioPena e la aDataSistema passate alla funzione getPenaDaEspiare(). Se il
	 * secondo parametro non viene passato, viene utilizzata la data di sistema. n.b. Questo quantum tiene
	 * conto SOLO della pena attualmente in espiazione, non della pena globalmente espiata. Se sono presenti
	 * Interruzioni o Sospensioni, i periodi espiati prima di tali eventi non vengono presi in considerazione.
	 * 2) dopo la chiamata alla calcolaPenaDaSospensione() viene valorizzato con in quantum ricalcolato a
	 * seguito della sospensione. Il quantum restituito è comprende non solo la pena effettivamente espiata,
	 * ma anche eventuali giorni di LA concessi al momento della sospensione.
	 * 
	 * @return
	 */
	public CalendarModel getPenaEspiata() {
		return mPenaEspiata;
	}

	public Vector getPeneEspiateInEccesso() {
		return mPeneEspiateInEccesso;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setTipoPenaIniziale(int aValore) {
		mTipoPenaIniziale = aValore;
	}

	public void setDataDal(Date aValore) {
		mDataDal = aValore;
	}

	public void setDataAl(Date aValore) {
		mDataAl = aValore;
	}

	public void setEventoPenaIniziale(EventoModel aValore) {
		mEventoPenaIniziale = aValore;
	}

	public void setPenaInSentenza(PenaComplessivaModel aValore) {
		mPenaInSentenza = aValore;
	}

	public void setBeneficiInSentenza(Vector aValore) {
		mBeneficiInSentenza = aValore;
	}

	public void setMisureCautelariInSentenza(Vector aValore) {
		mMisureCautelariInSentenza = aValore;
	}

	public void setSanzioneSostitutiva(SanzioneSostitutivaModel aValore) {
		mSanzioneSostitutiva = aValore;
	}

	public void setUltimaSanzSostResidua(SanzioneSostResiduaModel aValore) {
		mUltimaSanzSostResidua = aValore;
	}

	// Pene Iniziali se diversa da Pena Irrogata in sentenza
	public void setPenaIrrogataInCumulo(PenaResiduaModel aValore) {
		mPenaIrrogataInCumulo = aValore;
	}

	public void setPenaDopoSospensione(PenaResiduaModel aValore) {
		mPenaDopoSospensione = aValore;
	}

	public void setPenaDopoRevocaMA(PenaResiduaModel aValore) {
		mPenaDopoRevocaMA = aValore;
	}

	public void setPenaDopoRevocaIndultino(PenaResiduaModel aValore) {
		mPenaDopoRevocaIndultino = aValore;
	}

	public void setPenaResiduaManuale(PenaResiduaModel aValore) {
		mPenaResiduaManuale = aValore;
	}

	public void setPenaDaArchiviazione(PenaResiduaModel aValore) {
		mPenaDaArchiviazione = aValore;
	}

	public void setPenaDaIndulto(PenaResiduaModel aValore) {
		mPenaDaIndulto = aValore;
	}

	public void setPenaUltimaValidata(PenaResiduaModel aValore) {
		mPenaUltimaValidata = aValore;
	}

	public void setPenaDaRevocaSS(PenaResiduaModel aValore) {
		mPenaDaRevocaSS = aValore;
	}

	// Richieste al GE (AnnotazioneManualeModel) con anticipazione
	public void setDepenalizzazioneR(Vector aValore) {
		mDepenalizzazioneR = aValore;
	}

	public void setIncostituzionalitaR(Vector aValore) {
		mIncostituzionalitaR = aValore;
	}

	public void setAmnistiaR(Vector aValore) {
		mAmnistiaR = aValore;
	}

	public void setIndultoR(Vector aValore) {
		mIndultoR = aValore;
	}

	// Decisioni del GE (AnnotazioneManualeModel)
	public void setDepenalizzazione(Vector aValore) {
		mDepenalizzazione = aValore;
	}

	public void setIncostituzionalita(Vector aValore) {
		mIncostituzionalita = aValore;
	}

	public void setAmnistia(Vector aValore) {
		mAmnistia = aValore;
	}

	public void setIndulto(Vector aValore) {
		mIndulto = aValore;
	}

	// Computi
	public void setPresoffertoAltroReato(Vector aValore) {
		mPresoffertoAltroReato = aValore;
	}

	public void setFungibilitaAltroReatoMC(Vector aValore) {
		mFungibilitaAltroReatoMC = aValore;
	}

	public void setFungibilitaAltroReatoPD(Vector aValore) {
		mFungibilitaAltroReatoPD = aValore;
	}

	public void setComputoAltro(Vector aValore) {
		mComputoAltro = aValore;
	}

	public void setComputoRES(Vector aValore) {
		mComputoRES = aValore;
	}

	public void setDataRevocataDal(Date aValore) {
		mDataRevocataDal = aValore;
	}

	// Liberazione Anticipata
	public void setLibAnticipate(Vector aValore) {
		mLibAnticipate = aValore;
	}

	// Scomputi Permessi e Licenze
//	public void setScomputiPermLic(Vector aValore) {
//		mScomputiPermLic = aValore;
//	}

	public void setPeneGiaEspiate(Vector aValore) {
		mPeneGiaEspiate = aValore;
	}

	public void setPeneEspiateInEccesso(Vector aValore) {
		mPeneEspiateInEccesso = aValore;
	}

	// Fungibilità Calcolata
	public void setFungibilitaCalcolata(FungibilitaModel aValore) {
		mFungibilitaCalcolata = aValore;
	}

	// Consente di settare la pena espiata
	public void setPenaEspiata(CalendarModel aValore) {
		mPenaEspiata = aValore;
	}

	public void setPenaResiduaRicalcolata(PenaResiduaModel aValore) {
		mPenaResiduaRicalcolata = aValore;
	}

	// ============================================================================
	// METODI GET per estrarre i dati elaborati
	// ============================================================================
	/**
	 * Restituisce la somma di tutti i benefici concessi o revocati In Sentenza per Reclusione e Multa
	 * 
	 * @param aFlagConcessiRevocati
	 *            C = concessi R = revocati
	 * @return CalendarModel contenente i Quantum di benefici di reclusione concessi in sentenza e l'importo
	 *         della Multa
	 */
	public CalendarModel getBeneficiReclusioneInSentenza(String aFlagConcessiRevocati) {
		CalendarModel lBenConc = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mBeneficiInSentenza.size(); i++) {
			BeneficioModel lBenMod = (BeneficioModel) mBeneficiInSentenza.elementAt(i);

			if (aFlagConcessiRevocati != null && !aFlagConcessiRevocati.equals("")
					&& lBenMod.getCodNaturaBeneficio().equals(aFlagConcessiRevocati)) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lBenMod.getNumAnniReclusione());
				lCalMod.setNumMesi(lBenMod.getNumMesiReclusione());
				lCalMod.setNumGiorni(lBenMod.getNumGiorniReclusione());

				if (lBenMod.getImportoMulta() != null)
					lCalMod.setImportoMulta(lBenMod.getImportoMulta().doubleValue());
				else
					lCalMod.setImportoMulta(0);

				lBenConc = lCalUtil.sommaGiornieValute(lBenConc, lCalMod);
			}
		}

		return lBenConc;
	}

	/**
	 * Restituisce la somma di tutti i benefici concessi in sentenza per Arresti e Ammenda
	 * 
	 * @param aFlagConcessiRevocati
	 *            C = concessi R = revocati
	 * @return CalendarModel contenente i Quantum di benefici di Arresti concessi in sentenza e l'importo
	 *         della Ammenda
	 * 
	 */
	public CalendarModel getBeneficiArrestiInSentenza(String aFlagConcessiRevocati) {
		CalendarModel lBenConc = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mBeneficiInSentenza.size(); i++) {
			BeneficioModel lBenMod = (BeneficioModel) mBeneficiInSentenza.elementAt(i);

			if (aFlagConcessiRevocati != null && !aFlagConcessiRevocati.equals("")
					&& lBenMod.getCodNaturaBeneficio().equals(aFlagConcessiRevocati)) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lBenMod.getNumAnniArresto());
				lCalMod.setNumMesi(lBenMod.getNumMesiArresto());
				lCalMod.setNumGiorni(lBenMod.getNumGiorniArresto());

				if (lBenMod.getImportoAmmenda() != null)
					lCalMod.setImportoAmmenda(lBenMod.getImportoAmmenda().doubleValue());
				else
					lCalMod.setImportoAmmenda(0);

				lBenConc = lCalUtil.sommaGiornieValute(lBenConc, lCalMod);
			}
		}

		return lBenConc;
	}

	/**
	 * Restituisce il totale dei Quantum di Reclusione per Misure Cautelari stesso reato iscritte in sentenza
	 * (CodTipoMisura='CA')
	 * 
	 * @return
	 */
	public CalendarModel getMCReclusioneInSentenza() {
		CalendarModel lMisCautRec = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mMisureCautelariInSentenza.size(); i++) {
			MisuraCautelareModel lMisCautMod = (MisuraCautelareModel) mMisureCautelariInSentenza.elementAt(i);

			// CA = Custodia cautelare in carcere (Reclusione)
			if (lMisCautMod.getCodTipoMisura().equals("CA")) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lMisCautMod.getNumAnni());
				lCalMod.setNumMesi(lMisCautMod.getNumMesi());
				lCalMod.setNumGiorni(lMisCautMod.getNumGiorni());

				lMisCautRec = lCalUtil.sommaGiornieValute(lMisCautRec, lCalMod);
			}
		}

		return lMisCautRec;
	}

	/**
	 * Restituisce il totale dei Quantum di Arresti per Misure Cautelari stesso reato iscritte in sentenza
	 * (CodTipoMisura='AD')
	 * 
	 * @return
	 */
	public CalendarModel getMCArrestiInSentenza() {
		CalendarModel lMisCautArr = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mMisureCautelariInSentenza.size(); i++) {
			MisuraCautelareModel lMisCautMod = (MisuraCautelareModel) mMisureCautelariInSentenza.elementAt(i);

			// AD = Custodia cautelare in Arresti domiciliari
			if (lMisCautMod.getCodTipoMisura().equals("AD")) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lMisCautMod.getNumAnni());
				lCalMod.setNumMesi(lMisCautMod.getNumMesi());
				lCalMod.setNumGiorni(lMisCautMod.getNumGiorni());

				lMisCautArr = lCalUtil.sommaGiornieValute(lMisCautArr, lCalMod);
			}
		}

		return lMisCautArr;
	}

	/**
	 * Restituisce il totale dei Quantum di Custodia Cautelare in Regime di Permanenza in Casa stesso reato
	 * iscritte in sentenza (CodTipoMisura='CB')
	 * 
	 * @return
	 */
	public CalendarModel getMCPermanenzaInCasaInSentenza() {
		CalendarModel lMisCautPiC = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mMisureCautelariInSentenza.size(); i++) {
			MisuraCautelareModel lMisCautMod = (MisuraCautelareModel) mMisureCautelariInSentenza.elementAt(i);

			// CB = Custodia Cautelare in Regime di Permanenza in Casa
			if (lMisCautMod.getCodTipoMisura().equals("CB")) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lMisCautMod.getNumAnni());
				lCalMod.setNumMesi(lMisCautMod.getNumMesi());
				lCalMod.setNumGiorni(lMisCautMod.getNumGiorni());

				lMisCautPiC = lCalUtil.sommaGiornieValute(lMisCautPiC, lCalMod);
			}
		}

		return lMisCautPiC;
	}

	/**
	 * Restituisce il totale dei Quantum di Custodia Cautelare in Collocamento in Comunità stesso reato
	 * iscritte in sentenza (CodTipoMisura='CC')
	 * 
	 * @return
	 */
	public CalendarModel getMCCollocamentoInComunitaInSentenza() {
		CalendarModel lMisCautCiC = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mMisureCautelariInSentenza.size(); i++) {
			MisuraCautelareModel lMisCautMod = (MisuraCautelareModel) mMisureCautelariInSentenza.elementAt(i);

			// CC = Custodia Cautelare in Collocamento in Comunità
			if (lMisCautMod.getCodTipoMisura().equals("CC")) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lMisCautMod.getNumAnni());
				lCalMod.setNumMesi(lMisCautMod.getNumMesi());
				lCalMod.setNumGiorni(lMisCautMod.getNumGiorni());

				lMisCautCiC = lCalUtil.sommaGiornieValute(lMisCautCiC, lCalMod);
			}
		}

		return lMisCautCiC;
	}

	/**
	 * Restituisce il totale dei Quantum di Custodia Cautelare in Misura di Sicurezza Applicata in via
	 * Provvisoria stesso reato iscritte in sentenza (CodTipoMisura='CD')
	 * 
	 * @return
	 */
	public CalendarModel getMCMisuraSicurezzaApplicataInSentenza() {
		CalendarModel lMisCautMSA = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mMisureCautelariInSentenza.size(); i++) {
			MisuraCautelareModel lMisCautMod = (MisuraCautelareModel) mMisureCautelariInSentenza.elementAt(i);

			// CD = Custodia Cautelare in Misura di Sicurezza Applicata in via Provvisoria
			if (lMisCautMod.getCodTipoMisura().equals("CD")) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lMisCautMod.getNumAnni());
				lCalMod.setNumMesi(lMisCautMod.getNumMesi());
				lCalMod.setNumGiorni(lMisCautMod.getNumGiorni());

				lMisCautMSA = lCalUtil.sommaGiornieValute(lMisCautMSA, lCalMod);
			}
		}

		return lMisCautMSA;
	}

	/**
	 * Restituisce il totale dei Quantum di Custodia Cautelare in Camera di Sicurezza stesso reato iscritte in
	 * sentenza (CodTipoMisura='CE')
	 * 
	 * @return
	 */
	public CalendarModel getMCCameraSicurezzaInSentenza() {
		CalendarModel lMisCautCS = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mMisureCautelariInSentenza.size(); i++) {
			MisuraCautelareModel lMisCautMod = (MisuraCautelareModel) mMisureCautelariInSentenza.elementAt(i);

			// CE = Custodia cautelare in camera di sicurezza
			if (lMisCautMod.getCodTipoMisura().equals("CE")) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lMisCautMod.getNumAnni());
				lCalMod.setNumMesi(lMisCautMod.getNumMesi());
				lCalMod.setNumGiorni(lMisCautMod.getNumGiorni());

				lMisCautCS = lCalUtil.sommaGiornieValute(lMisCautCS, lCalMod);
			}
		}

		return lMisCautCS;
	}

	/**
	 * Restituisce il totale dei Quantum di Computo periodo messa alla prova stesso reato iscritte in sentenza
	 * (CodTipoMisura='CL')
	 * 
	 * @return
	 */
	public CalendarModel getMCComputoMessoAllaProvaInSentenza() {
		CalendarModel lMisCautMP = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mMisureCautelariInSentenza.size(); i++) {
			MisuraCautelareModel lMisCautMod = (MisuraCautelareModel) mMisureCautelariInSentenza.elementAt(i);

			// CL = Computo periodo messa alla prova
			if (lMisCautMod.getCodTipoMisura().equals("CL")) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lMisCautMod.getNumAnni());
				lCalMod.setNumMesi(lMisCautMod.getNumMesi());
				lCalMod.setNumGiorni(lMisCautMod.getNumGiorni());

				lMisCautMP = lCalUtil.sommaGiornieValute(lMisCautMP, lCalMod);
			}
		}

		return lMisCautMP;
	}

	/**
	 * Restituisce il totale dei Quantum di Custodia Cautelare in Regime di Arresti Domiciliari ex art 89 dpr
	 * 309/90 stesso reato iscritte in sentenza (CodTipoMisura='CM')
	 * 
	 * @return
	 */
	public CalendarModel getMCArrestiDomiciliariInSentenza() {
		CalendarModel lMisCautAD = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mMisureCautelariInSentenza.size(); i++) {
			MisuraCautelareModel lMisCautMod = (MisuraCautelareModel) mMisureCautelariInSentenza.elementAt(i);

			// CM = Custodia Cautelare in Regime di Arresti Domiciliari ex art 89 dpr 309/90
			if (lMisCautMod.getCodTipoMisura().equals("CM")) {
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumAnni(lMisCautMod.getNumAnni());
				lCalMod.setNumMesi(lMisCautMod.getNumMesi());
				lCalMod.setNumGiorni(lMisCautMod.getNumGiorni());

				lMisCautAD = lCalUtil.sommaGiornieValute(lMisCautAD, lCalMod);
			}
		}

		return lMisCautAD;
	}

	/**
	 * Restituisce il totale dei Quantum per Misure Cautelari
	 * 
	 * @return
	 */
	public CalendarModel getMisureCautelariReclusioneInSentenza() {
		CalendarModel lMisCautArr = new CalendarModel();

		// CalendarUtil lCalUtil = new CalendarUtil();

		// for (int i=0; i<mMisureCautelariInSentenza.size(); i++){
		// MisuraCautelareModel lMisCautMod = (MisuraCautelareModel) mMisureCautelariInSentenza.elementAt(i);
		//
		// // AD = Custodia cautelare in Arresti domiciliari
		// if ( lMisCautMod.getCodTipoMisura().equals("AD") ) {
		// CalendarModel lCalMod = new CalendarModel();
		//
		// lCalMod.setNumAnni (lMisCautMod.getNumAnni());
		// lCalMod.setNumMesi (lMisCautMod.getNumMesi());
		// lCalMod.setNumGiorni (lMisCautMod.getNumGiorni());
		//
		//
		// lMisCautArr = lCalUtil.sommaGiornieValute(lMisCautArr,lCalMod);
		// }
		// }

		// inizio calcolo totale custodia cautelare Anni Mesi Giorni
		// dove consideriamo periodi sovrapposti solo per Misure Cautelari
		// "Cessata al momento del passaggio in giudicato computabili"
		Date DItempdata;
		Date DFtempdata;
		// String DItemp = "";
		// String DFtemp = "";
		int sommaColonnaAnni = 0;
		int sommaColonnaMesi = 0;
		int sommaColonnaGiorni = 0;
		Vector vectorPeriodi = new Vector();
		int sommaColonnaAnniModManuale = 0;
		int sommaColonnaMesiModManuale = 0;
		int sommaColonnaGiorniModManuale = 0;

		CalendarModel ctot = new CalendarModel();
		CalendarUtil cu = new CalendarUtil();
		CalendarModel cm = new CalendarModel();
		Iterator itx = mMisureCautelariInSentenza.iterator();
		while (itx.hasNext()) {
			MisuraCautelareModel lMis = (MisuraCautelareModel) itx.next();
			if (lMis.getDataInizio() != null && lMis.getDataFine() != null && lMis.getGiorni() == null
					&& lMis.getFlagModificaManuale() == null && lMis.getFlagComputabile().equals("S")) {
				if (lMis.getCodOperatoreInserimento() == null || (lMis.getCodOperatoreInserimento() != null
						&& lMis.getCodOperatoreInserimento().length() > 2
						&& !lMis.getCodOperatoreInserimento().substring(0, 3).equalsIgnoreCase("RES"))) {
					vectorPeriodi.add(lMis.getDataInizio());
					vectorPeriodi.add(lMis.getDataFine());
				}
			}

			// inizio calcoloGioniMesiAnni A.S. settembre2015
			if (lMis.getDataFine() != null && lMis.getDataInizio() != null) {
				Date datainizio = lMis.getDataInizio();
				String ggInizio = DateUtils.getDayToString(datainizio);
				String mmInizio = DateUtils.getMonthToString(datainizio);
				String aaInizio = DateUtils.getYearToString(datainizio);

				Date datafine = lMis.getDataFine();
				String ggFine = DateUtils.getDayToString(datafine);
				String mmFine = DateUtils.getMonthToString(datafine);
				String aaFine = DateUtils.getYearToString(datafine);

				String calcoloGioniMesiAnni = "";
				try {
					calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
							aaInizio, ggFine, mmFine, aaFine);
				} catch (Exception e) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("Errore in " + getClass().getName() + ": " + e.getMessage());
					e.printStackTrace();
				}
				String sep = "~#";
				String[] aPairs = new String[3];
				aPairs = calcoloGioniMesiAnni.split(sep);

				cm.setNumAnni(Integer.parseInt(aPairs[0]));
				cm.setNumMesi(Integer.parseInt(aPairs[1]));
				cm.setNumGiorni(Integer.parseInt(aPairs[2]));

				// int numGiorniTotale = 0;
				// inizio calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova
				if (lMis.getCodTipoMisura().equalsIgnoreCase("CL") && lMis.getFlagModificaManuale() == null) {
					int giorni = cm.getNumGiorni();
					int mesi = cm.getNumMesi();
					int anni = cm.getNumAnni();

					int numGiorni = anni * 360 + mesi * 30 + giorni;
					// numGiorniTotale = numGiorni;
					// numGiorni = numGiorni/3;

					BigDecimal numGiorniAnniMesiGiorni = new BigDecimal(numGiorni);
					BigDecimal periodiGiorniUnoTre = new BigDecimal(3);
					BigDecimal numGiorniAnniMesiGiorniDivTre = new BigDecimal(0);
					BigDecimal numDiff = new BigDecimal(0);
					BigDecimal uno = new BigDecimal(1);
					numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorni.divide(periodiGiorniUnoTre, 1,
							RoundingMode.HALF_UP);
					// int num = tot.intValue();
					BigDecimal numGiorniAnniMesiGiorniDivTreInteroNegato = new BigDecimal(
							numGiorniAnniMesiGiorniDivTre.intValue()).negate();
					// numDiff = numGiorniAnniMesiGiorniDivTre;
					numDiff = numGiorniAnniMesiGiorniDivTre.add(numGiorniAnniMesiGiorniDivTreInteroNegato);

					// Se il decimale è >= 6 allora arrotondamento per eccesso.
					// Se il decimale è <= 5 allora arrotondamento per difetto.
					BigDecimal zero5 = new BigDecimal("0.5");
					if (numDiff.compareTo(zero5) == 0 || numDiff.compareTo(zero5) == -1) {
						// Se il decimale è <= 5 allora arrotondamento per difetto.
						numGiorniAnniMesiGiorniDivTre = new BigDecimal(
								numGiorniAnniMesiGiorniDivTre.intValue());
						// numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
					} else {
						numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
					}

					numGiorni = numGiorniAnniMesiGiorniDivTre.intValue();
					mesi = 0;
					anni = 0;
					// I conteggi sono effettuati usando gli algoritmi di ricalcolaGAM
					if (numGiorni > 30) {
						int tmp = numGiorni / 30;
						mesi += tmp;
						numGiorni -= tmp * 30;
					}
					if (numGiorni == 30) {
						mesi++;
						numGiorni = 0;
					}
					if (mesi > 12) {
						int tmp = mesi / 12;
						anni += tmp;
						mesi -= tmp * 12;
					}

					if (mesi == 12) {
						anni++;
						mesi = 0;
					}

					/*
					 * if (mesi<12) { anni=0; }
					 */
					cm.setNumAnni(anni);
					cm.setNumMesi(mesi);
					cm.setNumGiorni(numGiorni);
				} else {
					cm.setNumAnni(lMis.getNumAnni());
					cm.setNumMesi(lMis.getNumMesi());
					cm.setNumGiorni(lMis.getNumGiorni());
					// if (lMis.getGiorni() != null)
					// numGiorniTotale = Integer.parseInt(lMis.getGiorni().toString());
					// else
					// numGiorniTotale = Integer.parseInt("0");
				}
				// fine calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova

				if (lMis.getFlagComputabile().equals("S")) {
					// somma giorni/mesi/anni solo per le Misure cautelari migrate dal sistema RES
					if (lMis.getCodOperatoreInserimento() != null
							&& lMis.getCodOperatoreInserimento().length() > 2
							&& lMis.getCodOperatoreInserimento().substring(0, 3).equalsIgnoreCase("RES")) {
						ctot = cu.sommaGiorni(ctot, cm);
					}
				}
			}
			ctot = cu.ricalcolaGAM(ctot);
			// fine calcoloGioniMesiAnni A.S. settembre2015

			if (lMis.getGiorni() != null && lMis.getFlagComputabile().equals("S")) {
				sommaColonnaAnniModManuale = sommaColonnaAnniModManuale
						+ Integer.parseInt(lMis.getNumAnni().toString());
				sommaColonnaMesiModManuale = sommaColonnaMesiModManuale
						+ Integer.parseInt(lMis.getNumMesi().toString());
				sommaColonnaGiorniModManuale = sommaColonnaGiorniModManuale
						+ Integer.parseInt(lMis.getNumGiorni().toString());
			}

		}

		if (vectorPeriodi != null && vectorPeriodi.size() != 0) {
			for (int i = 0; i < vectorPeriodi.size() - 1; i = i + 2) {
				// trova l'elemento minimo
				int jMin = i;
				for (int j = i + 2; j < vectorPeriodi.size(); j = j + 1) {
					if (DateUtils.isLower((Date) vectorPeriodi.get(j), (Date) vectorPeriodi.get(jMin)))// vectorPeriodi[j]<vectorPeriodi[jMin]
						jMin = j;
				}
				// scambia gli elementi con indice i e jMin
				if (i != jMin) {
					// scambia
					// DItemp = (String)vectorPeriodi.get(jMin);
					// DFtemp = (String)vectorPeriodi.get(jMin+1);
					// vectorPeriodi.set(jMin, vectorPeriodi.get(i));
					// vectorPeriodi.set(jMin+1,vectorPeriodi.get(i+1));
					// vectorPeriodi.set(i,DItemp);
					// vectorPeriodi.set(i+1,DFtemp);
					Object DItempOb = new Object();
					Object DFtempOb = new Object();
					DItempOb = vectorPeriodi.get(jMin);
					DFtempOb = vectorPeriodi.get(jMin + 1);
					vectorPeriodi.set(jMin, vectorPeriodi.get(i));
					vectorPeriodi.set(jMin + 1, vectorPeriodi.get(i + 1));
					vectorPeriodi.set(i, DItempOb);
					vectorPeriodi.set(i + 1, DFtempOb);
				}
			}
		}

		// popolo una tabella di sei colonne e n righe quanti sono i periodi
		// nomi colonne: Data-inizio | Data-finale | Anni | Mesi | Giorni | Periodi-continuativi | numero
		// periodo
		// nella cella Periodi-continuativi che hanno uguale numero sono continuativi
		// ==> si deve fare la somma dei giorni mesi anni
		int nRighe = vectorPeriodi.size() / 2;
		int nColonne = 7;
		String[][] matricePeriodi = new String[nRighe][nColonne];
		// popolo Data-inizio | Data-finale |
		int r = 0;
		int c = 0;
		for (int i = 0; i < vectorPeriodi.size() - 1; i = i + 2) {
			matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
			r++;
		}
		r = 0;
		c = 1;
		for (int i = 1; i <= vectorPeriodi.size() - 1; i = i + 2) {
			matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
			r++;
		}

		// popolo le colonne | Anni | Mesi | Giorni | Periodi-continuativi | numero periodo
		r = 0;
		int numPeriodiContinuativi = 0;
		for (int i = 0; i < vectorPeriodi.size() - 1; i = i + 2) {
			DItempdata = (Date) vectorPeriodi.get(i);
			String ggInizio = DateUtils.getDayToString(DItempdata).toString();
			String mmInizio = DateUtils.getMonthToString(DItempdata).toString();
			String aaInizio = DateUtils.getYearToString(DItempdata).toString();

			DFtempdata = (Date) vectorPeriodi.get(i + 1);
			String ggFine = DateUtils.getDayToString(DFtempdata).toString();
			String mmFine = DateUtils.getMonthToString(DFtempdata).toString();
			String aaFine = DateUtils.getYearToString(DFtempdata).toString();

			String calcoloGioniMesiAnni = "";
			try {
				calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio, aaInizio,
						ggFine, mmFine, aaFine);
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Errore in " + getClass().getName() + ": " + e.getMessage());
				e.printStackTrace();
			}
			String sep = "~#";
			String[] aPairs = new String[3];
			aPairs = calcoloGioniMesiAnni.split(sep);
			String numAnni = aPairs[0];
			String numMesi = aPairs[1];
			String numGiorni = aPairs[2];

			boolean periodiContinuativi = false;

			// controllo i periodi se sono continuativi(sovrapposti)
			if (i == 0) {
				periodiContinuativi = true;// non cambia il numero del periodo
			} else {
				Date DIprimoPeriodo = (Date) vectorPeriodi.get(i - 1);
				Date DFsecondoPeriodo = (Date) vectorPeriodi.get(i);
				// System.out.print("isEquals ");
				// System.out.print("isLower ");
				// System.out.print("getIntervallo ");

				if (DateUtils.isEquals(DIprimoPeriodo, DFsecondoPeriodo)
						|| (DateUtils.isLower(DIprimoPeriodo, DFsecondoPeriodo)
								&& DateUtils.getIntervallo(DIprimoPeriodo, DFsecondoPeriodo) == 1)) {
					periodiContinuativi = true;// non cambia il numero del periodo
				} else {
					periodiContinuativi = false;// cambia il numero del periodo
				}
			}

			int cAnni = 2;
			int cMesi = 3;
			int cGiorni = 4;
			int cPeriodiContinuativi = 5;
			int cNumeroPeriodo = 6;
			matricePeriodi[r][cAnni] = numAnni;
			matricePeriodi[r][cMesi] = numMesi;
			matricePeriodi[r][cGiorni] = numGiorni;
			if (periodiContinuativi) {
				// Periodi sono Continuativi
				matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
			} else {
				numPeriodiContinuativi = numPeriodiContinuativi + 1;
				matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
				periodiContinuativi = false;
			}
			matricePeriodi[r][cNumeroPeriodo] = Integer.toString(i);// uguale al numero del periodo: primo
																	// periodo "0" secondo "2" terzo "4"
			r++;
		}

		// somma anni mesi giorni per periodi consecutivi
		String[][] matricePeriodiConsecutivi = new String[nRighe][nColonne];
		boolean matricePeriodiConsecutiviEsiste = false;// non esiste
		// Vector vectorPeriodiTemp = new Vector();
		boolean periodiContinuativi = false;
		int nRigPC = 0;
		// int nColPC = 0;

		String ggInizio = "";
		String mmInizio = "";
		String aaInizio = "";
		String ggFine = "";
		String mmFine = "";
		String aaFine = "";
		// int numeroPeriodo = 0;
		Date data = new Date();
		DItempdata = new Date();
		DFtempdata = new Date();
		for (int i = 0; i < nRighe; i++) {
			if (i == 0) {
				periodiContinuativi = false;
			} else {
				if (matricePeriodi[i - 1][5].equalsIgnoreCase(matricePeriodi[i][5])) {
					periodiContinuativi = true;
				} else {
					periodiContinuativi = false;
					nRigPC = nRigPC + 1;
				}
			}

			if (periodiContinuativi) {
				// SI periodi Consecutivi
				ggInizio = DateUtils.getDayToString(DItempdata);
				mmInizio = DateUtils.getMonthToString(DItempdata);
				aaInizio = DateUtils.getYearToString(DItempdata);
				for (int k = 0; k < vectorPeriodi.size(); k = k + 1) {
					data = (Date) vectorPeriodi.get(k);
					String ggVectorPeriodi = DateUtils.getDayToString(data);
					String mmVectorPeriodi = DateUtils.getMonthToString(data);
					String aaVectorPeriodi = DateUtils.getYearToString(data);
					String ggMatricePeriodi = matricePeriodi[i][1].substring(8, 10);
					String mmMatricePeriodi = matricePeriodi[i][1].substring(5, 7);
					String aaMatricePeriodi = matricePeriodi[i][1].substring(0, 4);
					if ((ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi)
							|| ("0" + ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi))
							&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi)
									|| ("0" + mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi))
							&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
						DFtempdata = data;
						break;
					}

				}

				ggFine = DateUtils.getDayToString(DFtempdata);
				mmFine = DateUtils.getMonthToString(DFtempdata);
				aaFine = DateUtils.getYearToString(DFtempdata);

				String calcoloGioniMesiAnni = "";
				try {
					calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
							aaInizio, ggFine, mmFine, aaFine);
				} catch (Exception e) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("Errore in " + getClass().getName() + ": " + e.getMessage());
					e.printStackTrace();
				}
				String sep = "~#";
				String[] aPairs = new String[3];
				aPairs = calcoloGioniMesiAnni.split(sep);
				String numAnni = aPairs[0];
				String numMesi = aPairs[1];
				String numGiorni = aPairs[2];

				matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][2] = numAnni;
				matricePeriodiConsecutivi[nRigPC][3] = numMesi;
				matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
				matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5];
				matricePeriodiConsecutiviEsiste = true;
			} else {
				// NO periodi Consecutivi
				if (i == 0) {
					DItempdata = (Date) vectorPeriodi.get(i);
				} else {
					for (int k = 0; k < vectorPeriodi.size() - 1; k = k + 1) {
						data = (Date) vectorPeriodi.get(k);

						String ggVectorPeriodi = DateUtils.getDayToString(data);
						String mmVectorPeriodi = DateUtils.getMonthToString(data);
						String aaVectorPeriodi = DateUtils.getYearToString(data);
						String ggMatricePeriodi = matricePeriodi[i][0].substring(8, 10);
						String mmMatricePeriodi = matricePeriodi[i][0].substring(5, 7);
						String aaMatricePeriodi = matricePeriodi[i][0].substring(0, 4);
						if ((ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi)
								|| ("0" + ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi))
								&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi)
										|| ("0" + mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi))
								&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
							DItempdata = data;
							break;
						}
					}
				}
				ggInizio = DateUtils.getDayToString(DItempdata);
				mmInizio = DateUtils.getMonthToString(DItempdata);
				aaInizio = DateUtils.getYearToString(DItempdata);

				if (i == 0) {
					DFtempdata = (Date) vectorPeriodi.get(i + 1);
				} else {
					for (int k = 0; k < vectorPeriodi.size(); k = k + 1) {
						data = (Date) vectorPeriodi.get(k);
						String ggVectorPeriodi = DateUtils.getDayToString(data);
						String mmVectorPeriodi = DateUtils.getMonthToString(data);
						String aaVectorPeriodi = DateUtils.getYearToString(data);
						String ggMatricePeriodi = matricePeriodi[i][1].substring(8, 10);
						String mmMatricePeriodi = matricePeriodi[i][1].substring(5, 7);
						String aaMatricePeriodi = matricePeriodi[i][1].substring(0, 4);
						if ((ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi)
								|| ("0" + ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi))
								&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi)
										|| ("0" + mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi))
								&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
							DFtempdata = data;
							break;
						}

					}
				}
				ggFine = DateUtils.getDayToString(DFtempdata);
				mmFine = DateUtils.getMonthToString(DFtempdata);
				aaFine = DateUtils.getYearToString(DFtempdata);

				String calcoloGioniMesiAnni = "";
				try {
					calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio,
							aaInizio, ggFine, mmFine, aaFine);
				} catch (Exception e) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("Errore in " + getClass().getName() + ": " + e.getMessage());
					e.printStackTrace();
				}
				String sep = "~#";
				String[] aPairs = new String[3];
				aPairs = calcoloGioniMesiAnni.split(sep);
				String numAnni = aPairs[0];
				String numMesi = aPairs[1];
				String numGiorni = aPairs[2];
				matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata, "dd/MM/yyyy");
				matricePeriodiConsecutivi[nRigPC][2] = numAnni;
				matricePeriodiConsecutivi[nRigPC][3] = numMesi;
				matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
				matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5].toString();
				matricePeriodiConsecutiviEsiste = true;
			}
		}

		// somma colonna | Anni | Mesi | Giorni |
		// int sommaColonnaAnni = 0;
		// int sommaColonnaMesi = 0;
		// int sommaColonnaGiorni = 0;
		if (mMisureCautelariInSentenza.size() > 0) {
			if (matricePeriodiConsecutiviEsiste) { // ci sono periodi consecutivi ==> in ogni riga della
													// tabella è stato inserito un periodo consecutivo
				for (int i = 0; i <= nRigPC; i++) {
					if (matricePeriodiConsecutivi[i][2] != null)
						sommaColonnaAnni = sommaColonnaAnni
								+ Integer.parseInt(matricePeriodiConsecutivi[i][2]);
					if (matricePeriodiConsecutivi[i][3] != null)
						sommaColonnaMesi = sommaColonnaMesi
								+ Integer.parseInt(matricePeriodiConsecutivi[i][3]);
					if (matricePeriodiConsecutivi[i][4] != null)
						sommaColonnaGiorni = sommaColonnaGiorni
								+ Integer.parseInt(matricePeriodiConsecutivi[i][4]);
				}
			}

			sommaColonnaAnni = sommaColonnaAnni + sommaColonnaAnniModManuale;
			sommaColonnaMesi = sommaColonnaMesi + sommaColonnaMesiModManuale;
			sommaColonnaGiorni = sommaColonnaGiorni + sommaColonnaGiorniModManuale;

			int giorni = sommaColonnaGiorni;
			while (giorni > 0) {
				giorni = giorni - 30;
				if (giorni >= 0) {
					sommaColonnaMesi = sommaColonnaMesi + 1;
					sommaColonnaGiorni = giorni;
				}
			}
			int mesi = sommaColonnaMesi;
			while (mesi > 0) {
				mesi = mesi - 12;
				if (mesi >= 0) {
					sommaColonnaAnni = sommaColonnaAnni + 1;
					sommaColonnaMesi = mesi;
				}
			}
			// int anni = sommaColonnaAnni;
		}

		// mTotaleAnni = sommaColonnaAnni;
		// mTotaleMesi = sommaColonnaMesi;
		// mTotaleGiorni = sommaColonnaGiorni;
		// fine calcolo totale custodia cautelare Anni Mesi Giorni
		// dove consideriamo periodi sovrapposti solo per Misure Cautelari

		int sommaColonnaAnniMisureCautelariMigrateRES = 0;
		int sommaColonnaMesiMisureCautelariMigrateRES = 0;
		int sommaColonnaGiorniMisureCautelariMigrateRES = 0;
		sommaColonnaAnniMisureCautelariMigrateRES = ctot.getNumAnni();
		sommaColonnaMesiMisureCautelariMigrateRES = ctot.getNumMesi();
		sommaColonnaGiorniMisureCautelariMigrateRES = ctot.getNumGiorni();
		int sommaColonnaAnniMisureCautelariMigrateRES_SIEP = sommaColonnaAnniMisureCautelariMigrateRES
				+ sommaColonnaAnni;
		int sommaColonnaMesiMisureCautelariMigrateRES_SIEP = sommaColonnaMesiMisureCautelariMigrateRES
				+ sommaColonnaMesi;
		int sommaColonnaGiorniMisureCautelariMigrateRES_SIEP = sommaColonnaGiorniMisureCautelariMigrateRES
				+ sommaColonnaGiorni;

		// ctot.setNumAnni(sommaColonnaAnniMisureCautelariMigrateRES_SIEP);
		// ctot.setNumMesi(sommaColonnaMesiMisureCautelariMigrateRES_SIEP);
		// ctot.setNumGiorni(sommaColonnaGiorniMisureCautelariMigrateRES_SIEP);

		lMisCautArr.setNumAnni(sommaColonnaAnniMisureCautelariMigrateRES_SIEP);
		lMisCautArr.setNumMesi(sommaColonnaMesiMisureCautelariMigrateRES_SIEP);
		lMisCautArr.setNumGiorni(sommaColonnaGiorniMisureCautelariMigrateRES_SIEP);
		// lMisCautArr.setNumAnni (sommaColonnaAnni);
		// lMisCautArr.setNumMesi (sommaColonnaMesi);
		// lMisCautArr.setNumGiorni (sommaColonnaGiorni);

		return lMisCautArr;
	}

	/**
	 * Restituisce il totale quantum di <b>Reclusione</b> e <b>Multa</b> per i benefici concessi/revocati. I
	 * dati presi in considerazione sono: Richieste al GE con anticipazione degli effetti - Indulto - Amnistia
	 * - Depenalizzazione - Incostituzionalità Decisioni del GE - Indulto - Amnistia - Depenalizzazione -
	 * Incostituzionalità
	 * 
	 * Il aFlagPiuMeno indica se recuperare le concessioni (-) o le revoche (+)
	 * 
	 * @param aFlagPiuMeno
	 *            ('+':revocati, '-':concessi)
	 * @return
	 */
	public CalendarModel getBeneficiReclusione(String aFlagPiuMeno) {
		CalendarModel lTotBenRecMod = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();
		Iterator itx = null;

		Vector lTotBenefici = new Vector();

		// Concateno i vettori in averne uno solo
		lTotBenefici.addAll(mAmnistiaR);
		lTotBenefici.addAll(mAmnistia);
		lTotBenefici.addAll(mIndultoR);
		lTotBenefici.addAll(mIndulto);
		lTotBenefici.addAll(mDepenalizzazioneR);
		lTotBenefici.addAll(mDepenalizzazione);
		lTotBenefici.addAll(mIncostituzionalitaR);
		lTotBenefici.addAll(mIncostituzionalita);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ");
		if (aFlagPiuMeno.equals("+"))
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Scorro tutti i Benefici REVOCATI e sommo Reclusione e Multa");
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Scorro tutti i Benefici CONCESSI e sommo Reclusione e Multa");

		itx = lTotBenefici.iterator();
		while (itx.hasNext()) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) itx.next();
			if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals(aFlagPiuMeno)) {
				CalendarModel lCalMod = new CalendarModel();
				lCalMod.setNumAnni(lAnnMod.getNumAnniReclusione());
				lCalMod.setNumMesi(lAnnMod.getNumMesiReclusione());
				lCalMod.setNumGiorni(lAnnMod.getNumGiorniReclusione());

				if (lAnnMod.getImportoMulta() != null)
					lCalMod.setImportoMulta(lAnnMod.getImportoMulta().doubleValue());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCalMod = " + lCalMod);

				lTotBenRecMod = lCalUtil.sommaGiornieValute(lTotBenRecMod, lCalMod);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Benefici Reclusione = " + lTotBenRecMod);

		return lTotBenRecMod;
	}

	/**
	 * Restituisce il totale quantum di <b>Arresti</b> e <b>Ammenda</b> per i benefici concessi/revocati. I
	 * dati presi in considerazione sono: Richieste al GE con anticipazione degli effetti - Indulto - Amnistia
	 * - Depenalizzazione - Incostituzionalità Decisioni del GE - Indulto - Amnistia - Depenalizzazione -
	 * Incostituzionalità
	 * 
	 * Il aFlagPiuMeno indica se recuperare le concessioni (-) o le revoche (+)
	 * 
	 * @param aFlagPiuMeno
	 *            ('+':revocati, '-':concessi)
	 * @return
	 */
	public CalendarModel getBeneficiArresti(String aFlagPiuMeno) {
		CalendarModel lTotBenArrMod = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();
		Iterator itx = null;

		Vector lTotBenefici = new Vector();

		// Concateno i vettori in averne uno solo
		lTotBenefici.addAll(mAmnistiaR);
		lTotBenefici.addAll(mAmnistia);
		lTotBenefici.addAll(mIndultoR);
		lTotBenefici.addAll(mIndulto);
		lTotBenefici.addAll(mDepenalizzazioneR);
		lTotBenefici.addAll(mDepenalizzazione);
		lTotBenefici.addAll(mIncostituzionalitaR);
		lTotBenefici.addAll(mIncostituzionalita);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ");
		if (aFlagPiuMeno.equals("+"))
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("   Scorro tutti i Benefici REVOCATI e sommo Arresti e Ammenda");
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("   Scorro tutti i Benefici CONCESSI e sommo Arresti e Ammenda");

		itx = lTotBenefici.iterator();
		while (itx.hasNext()) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) itx.next();
			if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals(aFlagPiuMeno)) {
				CalendarModel lCalMod = new CalendarModel();
				lCalMod.setNumAnni(lAnnMod.getNumAnniArresto());
				lCalMod.setNumMesi(lAnnMod.getNumMesiArresto());
				lCalMod.setNumGiorni(lAnnMod.getNumGiorniArresto());

				if (lAnnMod.getImportoAmmenda() != null)
					lCalMod.setImportoAmmenda(lAnnMod.getImportoAmmenda().doubleValue());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCalMod = " + lCalMod);

				lTotBenArrMod = lCalUtil.sommaGiornieValute(lTotBenArrMod, lCalMod);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Benefici Arresto = " + lTotBenArrMod);

		return lTotBenArrMod;
	}

	/**
	 * Restituisce il totale <b>Quantum di Reclusione</b> e <b>Multa</b> per i <b>Computi</b>:<br>
	 * <br>
	 * - Presofferti altro reato<br>
	 * - Fungibilità altro reato Misure Cautelari<br>
	 * - Fungibilità altro reato Pena Detentiva<br>
	 * 
	 * Il aFlagPiuMeno indica se recuperare le concessioni (-) o le revoche (+)
	 * 
	 * @param aFlagPiuMeno
	 *            ('+':revocati, '-':concessi)
	 * @return CalendarModel con il totale normalizzato n.b. sempre >0
	 */
	public CalendarModel getComputiReclusione(String aFlagPiuMeno) {
		CalendarModel lTotComputiRecMod = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();
		Iterator itx = null;

		Vector lTotComputi = new Vector();

		// Concateno i vettori in averne uno solo
		lTotComputi.addAll(this.mPresoffertoAltroReato);
		lTotComputi.addAll(this.mFungibilitaAltroReatoMC);
		lTotComputi.addAll(this.mFungibilitaAltroReatoPD);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ");
		if (aFlagPiuMeno.equals("+"))
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("   Scorro tutti i Computi REVOCATI e sommo Reclusione e Multa");
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("   Scorro tutti i Computi CONCESSI e sommo Reclusione e Multa");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotComputi.size() = " + lTotComputi.size());

		itx = lTotComputi.iterator();
		while (itx.hasNext()) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) itx.next();

			if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals(aFlagPiuMeno)) {
				CalendarModel lCalMod = new CalendarModel();
				lCalMod.setNumAnni(lAnnMod.getNumAnniReclusione());
				lCalMod.setNumMesi(lAnnMod.getNumMesiReclusione());
				lCalMod.setNumGiorni(lAnnMod.getNumGiorniReclusione());

				if (lAnnMod.getImportoMulta() != null)
					lCalMod.setImportoMulta(lAnnMod.getImportoMulta().doubleValue());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCalMod = " + lCalMod);

				lTotComputiRecMod = lCalUtil.sommaGiornieValute(lTotComputiRecMod, lCalMod);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Computi Reclusione = " + lTotComputiRecMod);

		return lTotComputiRecMod;
	}

	/**
	 * Restituisce il totale <b>Quantum di Arresti</b> e <b>Ammenda</b> per i <b>Computi</b>:<br>
	 * <br>
	 * - Presofferti altro reato<br>
	 * - Fungibilità altro reato Misure Cautelari<br>
	 * - Fungibilità altro reato Pena Detentiva<br>
	 * 
	 * Il aFlagPiuMeno indica se recuperare le concessioni (-) o le revoche (+)
	 * 
	 * @param aFlagPiuMeno
	 *            ('+':revocati, '-':concessi)
	 * @return CalendarModel con il totale normalizzato n.b. sempre >0
	 */
	public CalendarModel getComputiArresti(String aFlagPiuMeno) {
		CalendarModel lTotComputiArrMod = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();

		Iterator itx = null;

		Vector lTotComputi = new Vector();

		// Concateno i vettori in averne uno solo
		lTotComputi.addAll(this.mPresoffertoAltroReato);
		lTotComputi.addAll(this.mFungibilitaAltroReatoMC);
		lTotComputi.addAll(this.mFungibilitaAltroReatoPD);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ");
		if (aFlagPiuMeno.equals("+"))
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("   Scorro tutti i Computi REVOCATI e sommo Arresti e Ammenda");
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("   Scorro tutti i Computi CONCESSI e sommo Arresti e Ammenda");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotComputi.size() = " + lTotComputi.size());

		itx = lTotComputi.iterator();
		while (itx.hasNext()) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) itx.next();

			if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals(aFlagPiuMeno)) {
				CalendarModel lCalMod = new CalendarModel();
				lCalMod.setNumAnni(lAnnMod.getNumAnniArresto());
				lCalMod.setNumMesi(lAnnMod.getNumMesiArresto());
				lCalMod.setNumGiorni(lAnnMod.getNumGiorniArresto());

				if (lAnnMod.getImportoAmmenda() != null)
					lCalMod.setImportoAmmenda(lAnnMod.getImportoAmmenda().doubleValue());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCalMod = " + lCalMod);

				lTotComputiArrMod = lCalUtil.sommaGiornieValute(lTotComputiArrMod, lCalMod);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Computi Arresti = " + lTotComputiArrMod);

		return lTotComputiArrMod;
	}

	/**
	 * Restituisce il totale <b>Quantum di Reclusione</b> e <b>Multa</b> delle <b>Rideterminazioni pena
	 * 'Altro'</b>:<br>
	 * <br>
	 * 
	 * Il aFlagPiuMeno indica se recuperare le concessioni (-) o le revoche (+)
	 *
	 * @param aFlagPiuMeno
	 *            ('+':revocati, '-':concessi)
	 * @return CalendarModel con il totale normalizzato n.b. sempre >0
	 */
	public CalendarModel getAltroReclusione(String aFlagPiuMeno) {
		CalendarModel lTotComputiAltroRecMod = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();
		Iterator itx = null;

		Vector lTotComputiAltro = this.mComputoAltro;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Scorro tutti i Computi 'Altro' e sommo/sottraggo Reclusione e Multa");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotComputiAltro.size() = " + lTotComputiAltro.size());

		itx = lTotComputiAltro.iterator();
		while (itx.hasNext()) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) itx.next();

			if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals(aFlagPiuMeno)) {
				CalendarModel lCalMod = new CalendarModel();
				lCalMod.setNumAnni(lAnnMod.getNumAnniReclusione());
				lCalMod.setNumMesi(lAnnMod.getNumMesiReclusione());
				lCalMod.setNumGiorni(lAnnMod.getNumGiorniReclusione());

				if (lAnnMod.getImportoMulta() != null)
					lCalMod.setImportoMulta(lAnnMod.getImportoMulta().doubleValue());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCalMod = " + lCalMod);

				lTotComputiAltroRecMod = lCalUtil.sommaGiornieValute(lTotComputiAltroRecMod, lCalMod);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Computi Altro Reclusione = " + lTotComputiAltroRecMod);

		return lTotComputiAltroRecMod;
	}

	/**
	 * Restituisce il totale <b>Quantum di Arresti</b> e <b>Ammenda</b> delle <b>Rideterminazioni pena
	 * 'Altro'</b>:<br>
	 * <br>
	 * 
	 * Il aFlagPiuMeno indica se recuperare le concessioni (-) o le revoche (+)
	 * 
	 * @param aFlagPiuMeno
	 *            ('+':revocati, '-':concessi)
	 * @return CalendarModel con il totale normalizzato n.b. sempre >0
	 */
	public CalendarModel getAltroArresti(String aFlagPiuMeno) {
		CalendarModel lTotComputiAltroArrMod = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();
		Iterator itx = null;

		Vector lTotComputiAltro = this.mComputoAltro;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Scorro tutti i Computi 'Altro' e sommo/sottraggo Arresti e Ammenda");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotComputiAltro.size() = " + lTotComputiAltro.size());

		itx = lTotComputiAltro.iterator();
		while (itx.hasNext()) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) itx.next();
			if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals(aFlagPiuMeno)) {
				CalendarModel lCalMod = new CalendarModel();
				lCalMod.setNumAnni(lAnnMod.getNumAnniArresto());
				lCalMod.setNumMesi(lAnnMod.getNumMesiArresto());
				lCalMod.setNumGiorni(lAnnMod.getNumGiorniArresto());

				if (lAnnMod.getImportoAmmenda() != null)
					lCalMod.setImportoAmmenda(lAnnMod.getImportoAmmenda().doubleValue());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCalMod = " + lCalMod);

				lTotComputiAltroArrMod = lCalUtil.sommaGiornieValute(lTotComputiAltroArrMod, lCalMod);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Computi Altro Arresti = " + lTotComputiAltroArrMod);

		return lTotComputiAltroArrMod;
	}

	/**
	 * Restituisce il totale <b>Quantum di Reclusione</b> e <b>Multa</b> dei <b>Computi RES</b>:<br>
	 * <br>
	 * 
	 * Il aFlagPiuMeno indica se recuperare le concessioni (-) o le revoche (+)
	 *
	 * @param aFlagPiuMeno
	 *            ('+':revocati, '-':concessi)
	 * @return CalendarModel con il totale normalizzato n.b. sempre >0
	 */
	public CalendarModel getComputoResReclusione(String aFlagPiuMeno) {
		CalendarModel lTotComputiAltroRecMod = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();
		Iterator itx = null;

		Vector lTotComputiRES = this.mComputoRES;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Scorro tutti i Computi 'RES' e sommo/sottraggo Reclusione e Multa");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotComputiRES.size() = " + lTotComputiRES.size());

		itx = lTotComputiRES.iterator();
		while (itx.hasNext()) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) itx.next();

			if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals(aFlagPiuMeno)) {
				CalendarModel lCalMod = new CalendarModel();
				lCalMod.setNumAnni(lAnnMod.getNumAnniReclusione());
				lCalMod.setNumMesi(lAnnMod.getNumMesiReclusione());
				lCalMod.setNumGiorni(lAnnMod.getNumGiorniReclusione());

				if (lAnnMod.getImportoMulta() != null)
					lCalMod.setImportoMulta(lAnnMod.getImportoMulta().doubleValue());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCalMod = " + lCalMod);

				lTotComputiAltroRecMod = lCalUtil.sommaGiornieValute(lTotComputiAltroRecMod, lCalMod);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Computi RES Reclusione = " + lTotComputiAltroRecMod);

		return lTotComputiAltroRecMod;
	}

	/**
	 * Restituisce il totale <b>Quantum di Arresti</b> e <b>Ammenda</b> dei <b>Computi RES</b>:<br>
	 * <br>
	 * 
	 * Il aFlagPiuMeno indica se recuperare le concessioni (-) o le revoche (+)
	 * 
	 * @param aFlagPiuMeno
	 *            ('+':revocati, '-':concessi)
	 * @return CalendarModel con il totale normalizzato n.b. sempre >0
	 */
	public CalendarModel getComputoResArresti(String aFlagPiuMeno) {
		CalendarModel lTotComputiAltroArrMod = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();
		Iterator itx = null;

		Vector lTotComputiRES = this.mComputoRES;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Scorro tutti i Computi 'RES' e sommo/sottraggo Arresti e Ammenda");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotComputiRES.size() = " + lTotComputiRES.size());

		itx = lTotComputiRES.iterator();
		while (itx.hasNext()) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) itx.next();
			if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals(aFlagPiuMeno)) {
				CalendarModel lCalMod = new CalendarModel();
				lCalMod.setNumAnni(lAnnMod.getNumAnniArresto());
				lCalMod.setNumMesi(lAnnMod.getNumMesiArresto());
				lCalMod.setNumGiorni(lAnnMod.getNumGiorniArresto());

				if (lAnnMod.getImportoAmmenda() != null)
					lCalMod.setImportoAmmenda(lAnnMod.getImportoAmmenda().doubleValue());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCalMod = " + lCalMod);

				lTotComputiAltroArrMod = lCalUtil.sommaGiornieValute(lTotComputiAltroArrMod, lCalMod);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Computi RES Arresti = " + lTotComputiAltroArrMod);

		return lTotComputiAltroArrMod;
	}

	/**
	 * Restituisce il totale Giorni Liberazione Anticipata (LA) indipendentemente dal fatto che siano
	 * elaborate o meno. NON prende in considerazione gli scomputi permesso PP e EP
	 * 
	 * @return
	 */
	public int getLiberazioneAnticipata() {
		int lTotGiorniLibAnt = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lLibAnt.getFlagElaborato() = " + lLibAnt.getIdLicenzaLibanticipata() + " - "
					+ lLibAnt.getFlagElaborato() + " - " + lLibAnt.getFlagConcesso());

			if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") // Revoche RES
					|| lLibAnt.getFlagConcesso().equals("S") // Ridimensionamento LA (S=scomputato)
			)) { // Revoca LA RES viene iscritta su LICENZA_LIBANTICIPATA invece che su AM
					// Ridimensionamento LA (FLAG_CONCESSO="S")
				lTotGiorniLibAnt = lTotGiorniLibAnt - lLibAnt.getNumeroGiorni().intValue();
			} else {
				if (!lLibAnt.getCodTipoLicenza().equals("PP") && !lLibAnt.getCodTipoLicenza().equals("EP")
						&& !lLibAnt.getCodTipoLicenza().equals("RD")) {
					lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
				}
			}
		}

		return lTotGiorniLibAnt;
	}

	// 20/05/2014 Nuova L.A. DL 146/2013

	/**
	 * Restituisce il totale Giorni Liberazione Anticipata (con flag_concesso DIVERSO da "C" , quindi Scomputi
	 * o Revoche) indipendentemente dal fatto che siano elaborate o meno, per il Tipo di L.A. specificata da
	 * parametro
	 * 
	 * @param aTipoLA
	 *            (LA, LS, LI)
	 * @return
	 */
	public int getTipoLiberazioneAnticipata(String aTipoLA) {
		int lTotGiorniLibAnt = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getDescrStatoPermesso() != null) {
				if (lLibAnt.getDescrStatoPermesso().equals(aTipoLA)) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lLibAnt.getFlagElaborato() = " + lLibAnt.getIdLicenzaLibanticipata()
							+ " - " + lLibAnt.getFlagElaborato() + " - " + lLibAnt.getFlagConcesso());
					if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																										// RES
							lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA (S=scomputato)
					{
						// Ridimensionamento LA (FLAG_CONCESSO="S")
						lTotGiorniLibAnt = lTotGiorniLibAnt - lLibAnt.getNumeroGiorni().intValue();
					} else {
						// non devo tenere conto solo degli scomputi!
						if (!lLibAnt.getCodTipoLicenza().equals("PP")
								&& !lLibAnt.getCodTipoLicenza().equals("EP")) {
							lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
						}
					}
				}
			} else {
				if (aTipoLA.equals("LA")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lLibAnt.getFlagElaborato() = " + lLibAnt.getIdLicenzaLibanticipata()
							+ " - " + lLibAnt.getFlagElaborato() + " - " + lLibAnt.getFlagConcesso());
					if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																										// RES
							lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA (S=scomputato)
					{
						// Ridimensionamento LA (FLAG_CONCESSO="S")
						lTotGiorniLibAnt = lTotGiorniLibAnt - lLibAnt.getNumeroGiorni().intValue();
					} else {
						// non devo tenere conto solo degli scomputi!
						if (!lLibAnt.getCodTipoLicenza().equals("PP")
								&& !lLibAnt.getCodTipoLicenza().equals("EP")) {
							lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
						}
					}
				}
			}
		}

		return lTotGiorniLibAnt;
	}

	// END DL 146/2013

	/**
	 * Restituisce il totale Giorni Liberazione Anticipata da concedere, quelli con flag_elaborato a N o null
	 * 
	 * @return
	 */
	public int getLiberazioneAnticipataDaConcedere() {
		int lTotGiorniLibAnt = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("1 lLibAnt.getCodTipoLicenza() = " + lLibAnt.getCodTipoLicenza());

			if (lLibAnt.getFlagElaborato() == null || lLibAnt.getFlagElaborato().equals("N")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("2 lLibAnt.getFlagElaborato() = " + lLibAnt.getFlagElaborato());
				// Anna per Ridim. LA ottobre 2010
				if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") // Revoche RES
																								// di LA
						|| lLibAnt.getFlagConcesso().equals("S") // Ridimensionamento LA
				)) { // Revoca LA RES viene iscritta su LICENZA_LIBANTICIPATA invece che su AM
						// Ridimensionamento LA (FLAG_CONCESSO="S")
					lTotGiorniLibAnt = lTotGiorniLibAnt - lLibAnt.getNumeroGiorni().intValue();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("3 Ridimensionamento:lTotGiorniLibAnt = " + lTotGiorniLibAnt);
				} else {
					// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
					// ANNA ottobre 2010 devo controllare se SCOMPUTO.
					// In caso scomputo sorveglianza si posticipa il fine pena
					// se invece accoglimento di reclamo anticipa il fine pena
					if (!lLibAnt.getCodTipoLicenza().equals("PP") && !lLibAnt.getCodTipoLicenza().equals("EP")
							&& !lLibAnt.getCodTipoLicenza().equals("RD")) {
						lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni da concedere = " + lTotGiorniLibAnt);
		return lTotGiorniLibAnt;
	}

	// ===========================================================================================================
	// 20/05/2014 Nuova L.A. - differenziare i giorni da concedere di L.A. , L.A. Speciale , L.A. Integrazione
	// ===========================================================================================================

	/**
	 * Restituisce il totale Giorni Liberazione Anticipata "Ordinaria" da concedere, quelli con flag_elaborato
	 * a N o null
	 * 
	 * @return
	 */
	public int getLiberazioneAnticipataDaConcedereLA() {
		int lTotGiorniLibAntLA = 0;
		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() == null || lLibAnt.getFlagElaborato().equals("N")) {
				if (lLibAnt.getDescrStatoPermesso() != null) {
					if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LA")) {
						// Anna per Ridim. LA ottobre 2010
						if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																											// RES
																											// di
																											// LA
								lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA
						{
							lTotGiorniLibAntLA = lTotGiorniLibAntLA - lLibAnt.getNumeroGiorni().intValue();
						} else {
							// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
							// ANNA ottobre 2010 devo controllare se SCOMPUTO.
							// In caso scomputo sorveglianza si posticipa il fine pena
							// se invece accoglimento di reclamo anticipa il fine pena
							if (!lLibAnt.getCodTipoLicenza().equals("PP")
									&& !lLibAnt.getCodTipoLicenza().equals("EP")) {
								lTotGiorniLibAntLA = lTotGiorniLibAntLA
										+ lLibAnt.getNumeroGiorni().intValue();
							}
						}
					}
				} else {
					// Anna per Ridim. LA ottobre 2010
					if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																										// RES
																										// di
																										// LA
							lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA
					{
						lTotGiorniLibAntLA = lTotGiorniLibAntLA - lLibAnt.getNumeroGiorni().intValue();
					} else {
						// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
						// ANNA ottobre 2010 devo controllare se SCOMPUTO.
						// In caso scomputo sorveglianza si posticipa il fine pena
						// se invece accoglimento di reclamo anticipa il fine pena
						if (!lLibAnt.getCodTipoLicenza().equals("PP")
								&& !lLibAnt.getCodTipoLicenza().equals("EP")
								&& !lLibAnt.getCodTipoLicenza().equals("RD")) {
							lTotGiorniLibAntLA = lTotGiorniLibAntLA + lLibAnt.getNumeroGiorni().intValue();
						}
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni da concedere LA = " + lTotGiorniLibAntLA);
		return lTotGiorniLibAntLA;
	}

	/**
	 * Restituisce il totale Giorni Liberazione Anticipata Speciale da concedere, quelli con flag_elaborato a
	 * N o null
	 * 
	 * @return
	 */
	public int getLiberazioneAnticipataDaConcedereLS() {
		int lTotGiorniLibAntLS = 0;
		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() == null || lLibAnt.getFlagElaborato().equals("N")) {
				if (lLibAnt.getDescrStatoPermesso() != null) {
					if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LS")) {
						// Anna per Ridim. LA ottobre 2010
						if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																											// RES
																											// di
																											// LA
								lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA
						{
							lTotGiorniLibAntLS = lTotGiorniLibAntLS - lLibAnt.getNumeroGiorni().intValue();
						} else {
							// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
							// ANNA ottobre 2010 devo controllare se SCOMPUTO.
							// In caso scomputo sorveglianza si posticipa il fine pena
							// se invece accoglimento di reclamo anticipa il fine pena
							if (!lLibAnt.getCodTipoLicenza().equals("PP")
									&& !lLibAnt.getCodTipoLicenza().equals("EP")) {
								lTotGiorniLibAntLS = lTotGiorniLibAntLS
										+ lLibAnt.getNumeroGiorni().intValue();
							}
						}
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni da concedere LA SPECIALE = " + lTotGiorniLibAntLS);
		return lTotGiorniLibAntLS;
	}

	/**
	 * Restituisce il totale Giorni Liberazione Anticipata Integrazione da concedere, quelli con
	 * flag_elaborato a N o null
	 * 
	 * @return
	 */
	public int getLiberazioneAnticipataDaConcedereLI() {
		int lTotGiorniLibAntLI = 0;
		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() == null || lLibAnt.getFlagElaborato().equals("N")) {
				if (lLibAnt.getDescrStatoPermesso() != null) {
					if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LI")) {
						// Anna per Ridim. LA ottobre 2010
						if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																											// RES
																											// di
																											// LA
								lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA
						{
							lTotGiorniLibAntLI = lTotGiorniLibAntLI - lLibAnt.getNumeroGiorni().intValue();
						} else {
							// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
							// ANNA ottobre 2010 devo controllare se SCOMPUTO.
							// In caso scomputo sorveglianza si posticipa il fine pena
							// se invece accoglimento di reclamo anticipa il fine pena
							if (!lLibAnt.getCodTipoLicenza().equals("PP")
									&& !lLibAnt.getCodTipoLicenza().equals("EP")) {
								lTotGiorniLibAntLI = lTotGiorniLibAntLI
										+ lLibAnt.getNumeroGiorni().intValue();
							}
						}
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni da concedere LA INTEGRAZIONE = " + lTotGiorniLibAntLI);
		return lTotGiorniLibAntLI;
	}

	// End Nuova L.A.
	// ===========================================================================================================

	public int getScomputiDaConcedere() {
		int lTotGiorniLibAnt = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			if (lLibAnt.getFlagElaborato() == null || lLibAnt.getFlagElaborato().equals("N")) {
				if (lLibAnt.getFlagConcesso() != null && lLibAnt.getFlagConcesso().equals("C")) {
					// devo controllare se SCOMPUTO. In caso scomputo sorveglianza posticipa il fine pena
					// quindi sottratto da LA, se invece accoglimento di reclamo anticipa il fine pena
					// quindi sommato a LA
					if (lLibAnt.getCodTipoLicenza().equals("PP")) {
						lTotGiorniLibAnt = lTotGiorniLibAnt - lLibAnt.getNumeroGiorni().intValue();
					} else {
						if (lLibAnt.getCodTipoLicenza().equals("EP"))
							lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni da concedere = " + lTotGiorniLibAnt);
		return lTotGiorniLibAnt;
	}

	/**
	 * Restituisce il totale Giorni Liberazione Anticipata già concessi, quelli con flag_elaborato <> da N o
	 * null
	 * 
	 * @return
	 */
	public int getLiberazioneAnticipataGiaConcesse() {
		int lTotGiorniLibAnt = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("LA: " + lLibAnt.getIdLicenzaLibanticipata() + " - " + lLibAnt.getFlagElaborato()
					+ " - " + lLibAnt.getNumeroGiorni());

			if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")
					&& !lLibAnt.getFlagElaborato().equals("A")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"OK: " + lLibAnt.getIdLicenzaLibanticipata() + " - " + lLibAnt.getFlagElaborato()
								+ " - " + lLibAnt.getNumeroGiorni() + " - " + lLibAnt.getFlagConcesso());

				if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") // Revoche RES
																								// di LA
						|| lLibAnt.getFlagConcesso().equals("S") // Ridimensionamento LA
				)) { // Revoca LA RES viene iscritta su LICENZA_LIBANTICIPATA invece che su AM
						// Ridimensionamento LA (FLAG_CONCESSO="S")
					lTotGiorniLibAnt = lTotGiorniLibAnt - lLibAnt.getNumeroGiorni().intValue();
				} else {
					// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
					// ANNA ottobre 2010, devo controllare se SCOMPUTO.
					// RICORDA: In caso scomputo sorveglianza si posticipa il fine pena
					// se invece accoglimento di reclamo si anticipa il fine pena

					if (!lLibAnt.getCodTipoLicenza().equals("PP") && !lLibAnt.getCodTipoLicenza().equals("EP")
							&& !lLibAnt.getCodTipoLicenza().equals("RD")) {
						lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni già concessi = " + lTotGiorniLibAnt);
		return lTotGiorniLibAnt;
	}

	// ===========================================================================================================
	// 20/05/2014 Nuova L.A. - differenziare i giorni già concessi di L.A. , L.A. Speciale , L.A. Integrazione
	// ===========================================================================================================

	/**
	 * Restituisce il totale Giorni Liberazione Anticipata "Ordinaria" già concessi, quelli con flag_elaborato
	 * <> da N o null
	 * 
	 * @return
	 */
	public int getLiberazioneAnticipataGiaConcesseLA() {
		int lTotGiorniLibAntLA = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")
					&& !lLibAnt.getFlagElaborato().equals("A")) {
				if (lLibAnt.getDescrStatoPermesso() != null) {
					if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LA")) {
						if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																											// RES
																											// di
																											// LA
								lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA
						{
							lTotGiorniLibAntLA = lTotGiorniLibAntLA - lLibAnt.getNumeroGiorni().intValue();
						} else {
							// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
							// ANNA ottobre 2010, devo controllare se SCOMPUTO.
							// RICORDA: In caso scomputo sorveglianza si posticipa il fine pena
							// se invece accoglimento di reclamo si anticipa il fine pena

							if (!lLibAnt.getCodTipoLicenza().equals("PP")
									&& !lLibAnt.getCodTipoLicenza().equals("EP")) {
								lTotGiorniLibAntLA = lTotGiorniLibAntLA
										+ lLibAnt.getNumeroGiorni().intValue();

							}
						}
					}
				} else {
					if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																										// RES
																										// di
																										// LA
							lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA
					{
						lTotGiorniLibAntLA = lTotGiorniLibAntLA - lLibAnt.getNumeroGiorni().intValue();
					} else {
						// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
						// ANNA ottobre 2010, devo controllare se SCOMPUTO.
						// RICORDA: In caso scomputo sorveglianza si posticipa il fine pena
						// se invece accoglimento di reclamo si anticipa il fine pena

						if (!lLibAnt.getCodTipoLicenza().equals("PP")
								&& !lLibAnt.getCodTipoLicenza().equals("EP")
								&& !lLibAnt.getCodTipoLicenza().equals("RD")) {
							lTotGiorniLibAntLA = lTotGiorniLibAntLA + lLibAnt.getNumeroGiorni().intValue();
						}
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni di L.A. ordinaria già concessi = " + lTotGiorniLibAntLA);
		return lTotGiorniLibAntLA;
	}

	/**
	 * Restituisce il totale Giorni Liberazione Anticipata Speciale già concessi, quelli con flag_elaborato <>
	 * da N o null
	 * 
	 * @return
	 */
	public int getLiberazioneAnticipataGiaConcesseLS() {
		int lTotGiorniLibAntLS = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")
					&& !lLibAnt.getFlagElaborato().equals("A")) {
				if (lLibAnt.getDescrStatoPermesso() != null) {
					if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LS")) {

						if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																											// RES
																											// di
																											// LA
								lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA
						{
							lTotGiorniLibAntLS = lTotGiorniLibAntLS - lLibAnt.getNumeroGiorni().intValue();
						} else {
							// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
							// ANNA ottobre 2010, devo controllare se SCOMPUTO.
							// RICORDA: In caso scomputo sorveglianza si posticipa il fine pena
							// se invece accoglimento di reclamo si anticipa il fine pena

							if (!lLibAnt.getCodTipoLicenza().equals("PP")
									&& !lLibAnt.getCodTipoLicenza().equals("EP")) {
								lTotGiorniLibAntLS = lTotGiorniLibAntLS
										+ lLibAnt.getNumeroGiorni().intValue();
							}
						}
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni di L.A. Speciale già concessi = " + lTotGiorniLibAntLS);
		return lTotGiorniLibAntLS;
	}

	/**
	 * Restituisce il totale Giorni Integrazione Liberazione Anticipata già concessi, quelli con
	 * flag_elaborato <> da N o null
	 * 
	 * @return
	 */
	public int getLiberazioneAnticipataGiaConcesseLI() {
		int lTotGiorniLibAntLI = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")
					&& !lLibAnt.getFlagElaborato().equals("A")) {
				if (lLibAnt.getDescrStatoPermesso() != null) {
					if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LI")) {
						if (lLibAnt.getFlagConcesso() != null && (lLibAnt.getFlagConcesso().equals("R") || // Revoche
																											// RES
																											// di
																											// LA
								lLibAnt.getFlagConcesso().equals("S"))) // Ridimensionamento LA
						{
							lTotGiorniLibAntLI = lTotGiorniLibAntLI - lLibAnt.getNumeroGiorni().intValue();
						} else {
							// lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
							// ANNA ottobre 2010, devo controllare se SCOMPUTO.
							// RICORDA: In caso scomputo sorveglianza si posticipa il fine pena
							// se invece accoglimento di reclamo si anticipa il fine pena

							if (!lLibAnt.getCodTipoLicenza().equals("PP")
									&& !lLibAnt.getCodTipoLicenza().equals("EP")) {
								lTotGiorniLibAntLI = lTotGiorniLibAntLI
										+ lLibAnt.getNumeroGiorni().intValue();
							}
						}
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni di Integrazione L.A. già concessi = " + lTotGiorniLibAntLI);
		return lTotGiorniLibAntLI;
	}

	// End Nuova L.A. -
	// ===========================================================================================================

	public int getScomputiGiaConcessi() {
		int lTotGiorniLibAnt = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")
					&& !lLibAnt.getFlagElaborato().equals("A")) {
				if (lLibAnt.getFlagConcesso() != null && lLibAnt.getFlagConcesso().equals("C")) {
					// ANNA ottobre 2010
					// In caso scomputo sorveglianza posticipa il fine pena
					// quindi sottratto da LA, se invece accoglimento di reclamo anticipa il fine pena
					// quindi sommato a LA
					if (lLibAnt.getCodTipoLicenza().equals("PP"))
						lTotGiorniLibAnt = lTotGiorniLibAnt - lLibAnt.getNumeroGiorni().intValue();
					else {
						if (lLibAnt.getCodTipoLicenza().equals("EP"))
							lTotGiorniLibAnt = lTotGiorniLibAnt + lLibAnt.getNumeroGiorni().intValue();
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni SCOMPUTO già concessi = " + lTotGiorniLibAnt);
		return lTotGiorniLibAnt;
	}

	/**
	 * 
	 * @return
	 * @deprecated non ha senso x gli scomputi distinguere tra LA-LS-LI
	 */
	// 20/05/2014 Nuova L.A. - DL 146/2013

	// Scomputo L.A. ordinaria
	public int getScomputiGiaConcessiLA() {
		int lTotGiorniLibAntLA = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")
					&& !lLibAnt.getFlagElaborato().equals("A")) {
				if (lLibAnt.getFlagConcesso() != null && lLibAnt.getFlagConcesso().equals("C")) {
					// ANNA ottobre 2010
					// In caso scomputo sorveglianza posticipa il fine pena
					// quindi sottratto da LA, se invece accoglimento di reclamo anticipa il fine pena
					// quindi sommato a LA
					if (lLibAnt.getDescrStatoPermesso() != null) {
						if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LA")) {
							if (lLibAnt.getCodTipoLicenza().equals("PP"))
								lTotGiorniLibAntLA = lTotGiorniLibAntLA
										- lLibAnt.getNumeroGiorni().intValue();
							else {
								if (lLibAnt.getCodTipoLicenza().equals("EP"))
									lTotGiorniLibAntLA = lTotGiorniLibAntLA
											+ lLibAnt.getNumeroGiorni().intValue();
							}
						}
					} else {
						if (lLibAnt.getCodTipoLicenza().equals("PP"))
							lTotGiorniLibAntLA = lTotGiorniLibAntLA - lLibAnt.getNumeroGiorni().intValue();
						else {
							if (lLibAnt.getCodTipoLicenza().equals("EP"))
								lTotGiorniLibAntLA = lTotGiorniLibAntLA
										+ lLibAnt.getNumeroGiorni().intValue();
						}
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni SCOMPUTO L.A. ORDINARIA già concessi = " + lTotGiorniLibAntLA);
		return lTotGiorniLibAntLA;
	}

	/**
	 * 
	 * @return
	 * @deprecated non ha senso x gli scomputi distinguere tra LA-LS-LI
	 */
	// Scomputo L.A. SPECIALE
	public int getScomputiGiaConcessiLS() {
		int lTotGiorniLibAntLS = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")
					&& !lLibAnt.getFlagElaborato().equals("A")) {
				if (lLibAnt.getFlagConcesso() != null && lLibAnt.getFlagConcesso().equals("C")) {
					// ANNA ottobre 2010
					// In caso scomputo sorveglianza posticipa il fine pena
					// quindi sottratto da LA, se invece accoglimento di reclamo anticipa il fine pena
					// quindi sommato a LA
					if (lLibAnt.getDescrStatoPermesso() != null) {
						if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LS")) {
							if (lLibAnt.getCodTipoLicenza().equals("PP"))
								lTotGiorniLibAntLS = lTotGiorniLibAntLS
										- lLibAnt.getNumeroGiorni().intValue();
							else {
								if (lLibAnt.getCodTipoLicenza().equals("EP"))
									lTotGiorniLibAntLS = lTotGiorniLibAntLS
											+ lLibAnt.getNumeroGiorni().intValue();
							}
						}
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni SCOMPUTO L.A. SPECIALE già concessi = " + lTotGiorniLibAntLS);
		return lTotGiorniLibAntLS;
	}

	/**
	 * 
	 * @return
	 * @deprecated non ha senso x gli scomputi distinguere tra LA-LS-LI
	 */
	// Scomputo INTEGRAZIONE L.A.
	public int getScomputiGiaConcessiLI() {
		int lTotGiorniLibAntLI = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();
			if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")
					&& !lLibAnt.getFlagElaborato().equals("A")) {
				if (lLibAnt.getFlagConcesso() != null && lLibAnt.getFlagConcesso().equals("C")) {
					// ANNA ottobre 2010
					// In caso scomputo sorveglianza posticipa il fine pena
					// quindi sottratto da LA, se invece accoglimento di reclamo anticipa il fine pena
					// quindi sommato a LA
					if (lLibAnt.getDescrStatoPermesso() != null) {
						if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LI")) {
							if (lLibAnt.getCodTipoLicenza().equals("PP"))
								lTotGiorniLibAntLI = lTotGiorniLibAntLI
										- lLibAnt.getNumeroGiorni().intValue();
							else {
								if (lLibAnt.getCodTipoLicenza().equals("EP"))
									lTotGiorniLibAntLI = lTotGiorniLibAntLI
											+ lLibAnt.getNumeroGiorni().intValue();
							}
						}
					}
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni SCOMPUTO  INTEGRAZIONE L.A.  già concessi = " + lTotGiorniLibAntLI);
		return lTotGiorniLibAntLI;
	}

	// End DL 146/2013

	/**
	 * Restitiusce il numero di Giorni di Ridimensionamento LA da concedere - Anna per Ridim. LA ottobre 2010
	 * 
	 * @return
	 */
	public int getRidimensionamentoLAdaConcedere() {
		int lTotGiorni = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			if (lLibAnt.getFlagElaborato() == null || lLibAnt.getFlagElaborato().equals("N")) {
				if (lLibAnt.getFlagConcesso() != null && lLibAnt.getFlagConcesso().equals("S"))
					lTotGiorni = lTotGiorni + lLibAnt.getNumeroGiorni().intValue();

			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni Ridimensionamento da concedere = " + lTotGiorni);
		return lTotGiorni;
	}

	/**
	 * Restitiusce il numero di Giorni di Ridimensionamento LA concessi - Anna per Ridim. LA ottobre 2010
	 * 
	 * @return
	 */
	public int getRidimensionamentoLAConcesso() {
		int lTotGiorni = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")) {
				if (lLibAnt.getFlagConcesso() != null && lLibAnt.getFlagConcesso().equals("S"))
					lTotGiorni = lTotGiorni + lLibAnt.getNumeroGiorni().intValue();

			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni Ridimensionamento concessi = " + lTotGiorni);
		return lTotGiorni;
	}

	/**
	 * Ritorna il totale di GG di Risarcimento riconosciuti così come previsto dal DL 92 /2014
	 * indipendentemente se già computati o da computare
	 * 
	 * @return
	 * @since 10/2014 DL92/2014
	 */
	public int getRimediRisarcitori() {
		int lTotGiorniRD = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			if (lLibAnt.getCodTipoLicenza().equals("RD") && lLibAnt.getFlagConcesso().equals("C")) {
				if (!"A".equals(lLibAnt.getFlagElaborato())) { // E e S
					lTotGiorniRD = lTotGiorniRD + lLibAnt.getNumeroGiorni().intValue();
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni RD (concessi + da concedere) = " + lTotGiorniRD);
		return lTotGiorniRD;
	}

	/**
	 * Ritorna il totale di GG di Risarcimento riconosciuti così come previsto dal DL 92 /2014 già concessi,
	 * ovvero computato su una pena residua
	 * 
	 * @return
	 * @since 10/2014 DL92/2014
	 */
	public int getRimediRisarcitoriGiaConcessi() {
		int lTotGiorniRD = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			if (lLibAnt.getCodTipoLicenza().equals("RD") && lLibAnt.getFlagConcesso().equals("C")) {
				if (lLibAnt.getFlagElaborato() != null && !lLibAnt.getFlagElaborato().equals("N")
						&& !lLibAnt.getFlagElaborato().equals("A")) { // E e S
					lTotGiorniRD = lTotGiorniRD + lLibAnt.getNumeroGiorni().intValue();
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni RD già concessi = " + lTotGiorniRD);
		return lTotGiorniRD;
	}

	/**
	 * Ritorna il totale di GG di Risarcimento riconosciuti così come previsto dal DL 92 /2014 da concedere,
	 * ovvero NON computato su una pena residua
	 * 
	 * @return
	 * @since 10/2014 DL92/2014
	 */
	public int getRimediRisarcitoriDaConcedere() {
		int lTotGiorniRD = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			if (lLibAnt.getCodTipoLicenza().equals("RD") && lLibAnt.getFlagConcesso().equals("C")) {
				if (lLibAnt.getFlagElaborato() == null || "N".equals(lLibAnt.getFlagElaborato())) { // null o
																									// N
					lTotGiorniRD = lTotGiorniRD + lLibAnt.getNumeroGiorni().intValue();
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("TOT giorni RD da concedere = " + lTotGiorniRD);

		return lTotGiorniRD;
	}

	/**
	 * Restitiusce il numero totale di Giorni scomputati - COD_TIPO_LICENZA = 'PP' per scomputo Permessi -
	 * 
	 * @return
	 */
	public int getTotaleScomputi() {
		int lTotGiorniScomputo = 0;

		Iterator itx = this.mLibAnticipate.iterator();
		while (itx.hasNext()) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) itx.next();

			// if ( lLibAnt.getFlagElaborato()!=null&& !lLibAnt.getFlagElaborato().equals("N")&&
			// !lLibAnt.getFlagElaborato().equals("A"))
			if (lLibAnt.getFlagConcesso() != null) {
				if (lLibAnt.getCodTipoLicenza().equals("PP"))
					lTotGiorniScomputo = lTotGiorniScomputo - lLibAnt.getNumeroGiorni().intValue();
				else if (lLibAnt.getCodTipoLicenza().equals("EP"))
					lTotGiorniScomputo = lTotGiorniScomputo + lLibAnt.getNumeroGiorni().intValue();
			}
		}

		/*
		 * VECCHIA VERSIONE Iterator itx = this.mScomputiPermLic.iterator(); // [FT] - 03/08/2016 - MAC_LOG -
		 * Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug("devo contare gliSCOMPUTI"); while (itx.hasNext()){ LicenzaLibAnticipataModel
		 * lLibAntModScomp = (LicenzaLibAnticipataModel) itx.next();
		 * 
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.debug("lLibAntModScomp.getFlagElaborato() = "+lLibAntModScomp.
		 * getIdLicenzaLibanticipata
		 * ()+" - "+lLibAntModScomp.getFlagElaborato()+" - "+lLibAntModScomp.getFlagConcesso());
		 * lTotGiorniScomputo = lTotGiorniScomputo + lLibAntModScomp.getNumeroGiorni().intValue(); }
		 */

		return lTotGiorniScomputo;
	}

	/**
	 * Restutiusce la somma dei periodi già espiati recuperati dai record Sospensione
	 * 
	 * @return
	 */
	public CalendarModel getQuantumPenaGiaEspiata() {
		CalendarModel lTotGiaEspiataCal = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();
		Iterator itx = null;

		Vector lTotPenaGiaEspiata = this.mPeneGiaEspiate;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotPenaGiaEspiata.size() = " + lTotPenaGiaEspiata.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Scorro tutti i record SOSPENSIONE e sommo i periodi");

		itx = lTotPenaGiaEspiata.iterator();
		while (itx.hasNext()) {
			SospensioneModel lSospMod = (SospensioneModel) itx.next();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("data inizio sospensione = " + lSospMod.getDataInizio());

			CalendarModel lCalMod = new CalendarModel();
			lCalMod.setNumAnni(lSospMod.getNumAnniPenaEspiata());
			lCalMod.setNumMesi(lSospMod.getNumMesiPenaEspiata());
			lCalMod.setNumGiorni(lSospMod.getNumGiorniPenaEspiata());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCalMod = " + lCalMod);

			lTotGiaEspiataCal = lCalUtil.sommaGiornieValute(lTotGiaEspiataCal, lCalMod);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Già Espiato = " + lTotGiaEspiataCal);

		return lTotGiaEspiataCal;
	}

	/**
	 * Restituisce il totale di giorni di LA computati sulle sospensioni
	 * 
	 * @return
	 */
	public int getLAsuPenaEspiata() {
		int lLAgiaEspiata = 0;

		Iterator itx = null;

		Vector lTotPenaGiaEspiata = this.mPeneGiaEspiate;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotPenaGiaEspiata.size() = " + lTotPenaGiaEspiata.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Scorro tutti i record SOSPENSIONE e sommo i periodi");

		itx = lTotPenaGiaEspiata.iterator();
		while (itx.hasNext()) {
			SospensioneModel lSospMod = (SospensioneModel) itx.next();

			lLAgiaEspiata = lLAgiaEspiata + (lSospMod.getNumGiorniLibanticipata()).intValue();

		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale LA Già Espiato = " + lLAgiaEspiata);

		return lLAgiaEspiata;
	}

	// ============================================================================
	// Metodi principali di calcolo della pena
	// ============================================================================

	/**
	 * ************************************************************************** Metodo che effettua i
	 * calcoli della <b>Pena da Espiare</b> in funzione dei dati contenuti nel model. Il metodo:<br>
	 * - somma i quantum <br>
	 * - determina le date di espiazione se presente data decorrenza sulla pena iniziale<br>
	 * - arretra il fine pena se presenti LA da computare<br>
	 * 
	 * @param aDataInizioPena
	 *            Data di decorrenza rispetto alla quale si vuole che vengano calcolate le date di decorrenza
	 *            (fine Reclusione/fine pena)
	 * @param aDataSistema
	 *            data di systema rispetto alla quale si vuole effettuare i calcoli delle fungibilità. Se non
	 *            specificata viene utilizzata la sysdate
	 * @param tipoCalcolo
	 *            indica se vanno computate sul fine pena tutte le LA o solo quelle già concesse. Es. nel caso
	 *            si sta visualizzando il dettaglio della pena in espiazione il parametro va passato a null in
	 *            modo che nel ricalcolo non vengano scalate le LA concesse ma non ancora computate Se si
	 *            vuole rieffettuare un nuovo calcolo della pena il parametro va passato a 'all' in modo che
	 *            vengano portate dentro anche le nuove LA
	 * @return Un model PenaResiduaModel con quantum e importi valorizzati oltre alle date di decorrenza.
	 */
	public PenaResiduaModel getPenaDaEspiare(Date aDataInizioPena, Date aDataSistema, String tipoCalcolo)
			throws Exception {
		return getPenaDaEspiare(aDataInizioPena, aDataSistema, tipoCalcolo, null);
	}

	/**
	 * ************************************************************************** Metodo che effettua i
	 * calcoli della <b>Pena da Espiare</b> in funzione dei dati contenuti nel model. Il metodo:<br>
	 * - somma i quantum <br>
	 * - determina le date di espiazione se presente data decorrenza sulla pena iniziale<br>
	 * - arretra il fine pena se presenti LA da computare<br>
	 * 
	 * DA estendere per gestire il calcolo anche in caso di ergastolo
	 * 
	 * @param aDataInizioPena
	 *            Data di decorrenza rispetto alla quale si vuole che vengano calcolate le date di decorrenza
	 *            (fine Reclusione/fine pena)
	 * @param aDataSistema
	 *            data di systema rispetto alla quale si vuole effettuare i calcoli delle fungibilità. Se non
	 *            specificata viene utilizzata la sysdate
	 * @param tipoCalcolo
	 *            indica se vanno computate sul fine pena tutte le LA o solo quelle già concesse. Es. nel caso
	 *            si sta visualizzando il dettaglio della pena in espiazione il parametro va passato a null in
	 *            modo che nel ricalcolo non vengano scalate le LA concesse ma non ancora computate Se si
	 *            vuole rieffettuare un nuovo calcolo della pena il parametro va passato a 'all' in modo che
	 *            vengano portate dentro anche le nuove LA
	 * @param aDataFineDaForzare
	 *            . Se specificata, viene utilizzata come data fine pena e sostituita a quella calcolata. n.b.
	 *            anche per il calcolo della pena espiata e della fingibilità partirà dalla pena forzata.
	 *            Opzione utilizzata nel caso del'OS fungibilità per poter operare sulle vecchie date
	 *            piuttosto che su quella calcolata dalla nuova procedura.
	 * @return Un model PenaResiduaModel con quantum e importi valorizzati oltre alle date di decorrenza.
	 * 
	 */
	public PenaResiduaModel getPenaDaEspiare(Date aDataInizioPena, Date aDataSistema, String tipoCalcolo,
			Date aDataFineDaForzare) throws Exception {
		PenaResiduaModel lPenaDaEspiare = new PenaResiduaModel();
		this.mFungibilitaCalcolata = null;
		this.mPenaEspiata = null;

		// ==========================================================================
		// n.b. per ora funziona solo sul primo calcolo della pena lanciato direttamente
		// dalle funzioni di computo
		// ==========================================================================
		if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_IN_SENTENZA && mPenaInSentenza != null
				&& mPenaInSentenza.getCodTipoPenaDetentiva() != null
				&& (mPenaInSentenza.getCodTipoPenaDetentiva().equals("03")
						|| mPenaInSentenza.getCodTipoPenaDetentiva().equals("04"))) {
			// Nel caso di ergastolo non devo effettuare calcoli, ma copiare i dati
			// dell'ultima pena validata se esiste

			if (mPenaInSentenza.getCodTipoPenaDetentiva().equals("03")) // Ergastolo
			{
				lPenaDaEspiare.setFlagErgastolo("S");
			} else if (mPenaInSentenza.getCodTipoPenaDetentiva().equals("04")) // Ergastolo con isolamento
			{
				lPenaDaEspiare.setFlagErgastolo("D");
			}

			if (aDataInizioPena != null) {
				Date lDataFinePena = DateUtils.getDate(9999, 12, 31);
				lPenaDaEspiare.setDataInizio(aDataInizioPena);
				lPenaDaEspiare.setDataFine(lDataFinePena); // 31/12/9999
			}

			lPenaDaEspiare.setDataInizioIsolamentoDiurno(mPenaInSentenza.getDataInizioIsolamentoDiurno());
			lPenaDaEspiare.setDataFineIsolamentoDiurno(mPenaInSentenza.getDataFineIsolamentoDiurno());

			lPenaDaEspiare.setNumGiorniIsolamentoDiurno(mPenaInSentenza.getNumGiorniIsolamentoDiurno());
			lPenaDaEspiare.setNumMesiIsolamentoDiurno(mPenaInSentenza.getNumMesiIsolamentoDiurno());
			lPenaDaEspiare.setNumAnniIsolamentoDiurno(mPenaInSentenza.getNumAnniIsolamentoDiurno());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ergastolo!!");
			return lPenaDaEspiare;
		}

		// ==========================================================================
		// Sommo i quantum a partire dalla pena iniziale
		// n.b attenzione ai quantum da sottrarre è alla modalità di sottrazione
		// ==========================================================================
		// Calcolo prima il quantum di Reclusione e quindi il quantum di Arresti.
		// Se il quantum, di reclusione diventa negativo, lo azzero e sottraggo la
		// differenza al quantum di arresti.

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusioneTotMod = new CalendarModel();
		CalendarModel lCalArrestiTotMod = new CalendarModel();
		// CalendarModel lSanzioniSostMod = new CalendarModel();

		Date lDataSistemaPerCalcoli = null;

		if (aDataSistema != null) {
			lDataSistemaPerCalcoli = aDataSistema;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lDataSistemaPerCalcoli = " + lDataSistemaPerCalcoli);
		} else {
			lDataSistemaPerCalcoli = DateUtils.getSysDate();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lDataSistemaPerCalcoli = sysdate = " + lDataSistemaPerCalcoli);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero il quantum di 'Pena Iniziale'");
		// ==========================================================================
		// Recupero i quantum di 'Pena Iniziale' in funzione del tipo di pena iniziale
		// i quantum ottenuti rappresenteranno il punto di partenza per i successivi
		// calcoli
		// - lCalReclusioneTotMod
		// - lCalArrestiTotMod
		// ==========================================================================
		if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_IN_SENTENZA) {
			// MEV_39: aggiungo controllo preventivo
			if (mPenaInSentenza != null) {
				// Recupero la Reclusione
				lCalReclusioneTotMod.setNumAnni(mPenaInSentenza.getNumAnniReclusione());
				lCalReclusioneTotMod.setNumMesi(mPenaInSentenza.getNumMesiReclusione());
				lCalReclusioneTotMod.setNumGiorni(mPenaInSentenza.getNumGiorniReclusione());
				if (mPenaInSentenza.getImportoMulta() != null)
					lCalReclusioneTotMod.setImportoMulta(mPenaInSentenza.getImportoMulta().doubleValue());

				// Recupero l'Arresto
				lCalArrestiTotMod.setNumAnni(mPenaInSentenza.getNumAnniArresto());
				lCalArrestiTotMod.setNumMesi(mPenaInSentenza.getNumMesiArresto());
				lCalArrestiTotMod.setNumGiorni(mPenaInSentenza.getNumGiorniArresto());
				if (mPenaInSentenza.getImportoAmmenda() != null)
					lCalArrestiTotMod.setImportoAmmenda(mPenaInSentenza.getImportoAmmenda().doubleValue());
			}

			// Sottraggo i Benefici Concessi in Sentenza
			lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod,
					this.getBeneficiReclusioneInSentenza("C"));
			lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod,
					this.getBeneficiArrestiInSentenza("C"));
			// Sommo i Benefici Revocati in Sentenza
			lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod,
					this.getBeneficiReclusioneInSentenza("R"));
			lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod,
					this.getBeneficiArrestiInSentenza("R"));

			// Sottraggo le Misure Cautelari.
			// ATTENZIONE!! Le MC per Arresti Domiciliari vengono comunque scalate dalla
			// reclusione

			// lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod,
			// this.getMCReclusioneInSentenza());
			// lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod ,
			// this.getMCArrestiInSentenza());
			lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod,
					this.getMisureCautelariReclusioneInSentenza());
			// lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod,
			// this.getMCArrestiInSentenza());

			// ========================================================================
			// new! Gestione delle sanzioni sostitutive. dalla vers 3.0
			// Devo calcolare la Sanzione Sostitutiva residua, quella da eseguire,
			// partendo da quanto concesso in sentenza scomputando l'eventuale presofferto
			// ========================================================================
			// Carico la SS in sentenza
			// if (mSanzioneSostitutiva!=null && mSanzioneSostitutiva.getIdSanzioneSostitutiva()!=null){
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("presente sanzione sostitutiva in sentenza = "+mSanzioneSostitutiva);
			//
			// lSanzioniSostMod.setNumAnni (mSanzioneSostitutiva.getNumAnni());
			// lSanzioniSostMod.setNumMesi (mSanzioneSostitutiva.getNumMesi());
			// lSanzioniSostMod.setNumGiorni (mSanzioneSostitutiva.getNumGiorni());
			//
			// if (mSanzioneSostitutiva.getSanzionePecuniaria()!=null){
			// lSanzioniSostMod.setImportoMulta (mSanzioneSostitutiva.getSanzionePecuniaria().doubleValue());
			// }
			//
			// // Sottraggo l'eventuale presofferto (MC)
			// if (mSanzioneSostitutiva.getCodTipoSanzione().equals("S")){
			// // Semidetenzione: conversione 1 a 1, 1g di detentiva = 1 g di semidetenzione
			// CalendarModel lMCTotali = lCalUtil.sommaGiornieValute(this.getMCReclusioneInSentenza(),
			// this.getMCArrestiInSentenza());
			// lSanzioniSostMod = lCalUtil.sottraiGiorniValuteNew(lSanzioniSostMod, lMCTotali);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lSanzioniSostMod = "+lSanzioniSostMod);
			// }
			// else if (mSanzioneSostitutiva.getCodTipoSanzione().equals("L")){
			// // Libertà Controllata: conversione 1 a 2, 1g di detentiva = 2g di semidetenzione
			// CalendarModel lMCTotali = lCalUtil.sommaGiornieValute(this.getMCReclusioneInSentenza(),
			// this.getMCArrestiInSentenza());
			// lMCTotali.setNumAnni (2*lMCTotali.getNumAnni());
			// lMCTotali.setNumMesi (2*lMCTotali.getNumMesi());
			// lMCTotali.setNumGiorni (2*lMCTotali.getNumGiorni());
			// lSanzioniSostMod = lCalUtil.sottraiGiorniValuteNew(lSanzioniSostMod, lMCTotali);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lSanzioniSostMod = "+lSanzioniSostMod);
			// }
			// else if (mSanzioneSostitutiva.getCodTipoSanzione().equals("P")){
			// // Pecuniaria: ???? 1gg = 38 euro
			// CalendarModel lMCTotali = lCalUtil.sommaGiornieValute(this.getMCReclusioneInSentenza(),
			// this.getMCArrestiInSentenza());
			// int lTotGG = CalendarUtil.getTotGiorni(lMCTotali);
			// lMCTotali = new CalendarModel();
			// lMCTotali.setImportoMulta(lTotGG*38); // <<--- 38 EURO per ogni giorno di MC sofferto
			// lSanzioniSostMod = lCalUtil.sottraiGiorniValuteNew(lSanzioniSostMod, lMCTotali);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lSanzioniSostMod = "+lSanzioniSostMod);
			// }
			// else {
			// // In tutti gli altri casi non c'è conversione
			// }
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lSanzioniSostMod = "+lSanzioniSostMod);
			// }
		} else if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_IN_CUMULO
				|| mTipoPenaIniziale == ICostantiCalcoloPena.PENA_IN_CUMULO_NEW // MEV26 - Cumulo
		) {
			// Recupero la Reclusione
			lCalReclusioneTotMod.setNumAnni(mPenaIrrogataInCumulo.getNumAnniReclusione());
			lCalReclusioneTotMod.setNumMesi(mPenaIrrogataInCumulo.getNumMesiReclusione());
			lCalReclusioneTotMod.setNumGiorni(mPenaIrrogataInCumulo.getNumGiorniReclusione());
			if (mPenaIrrogataInCumulo.getImportoMulta() != null)
				lCalReclusioneTotMod.setImportoMulta(mPenaIrrogataInCumulo.getImportoMulta().doubleValue());

			// Recupero l'Arresto
			lCalArrestiTotMod.setNumAnni(mPenaIrrogataInCumulo.getNumAnniArresto());
			lCalArrestiTotMod.setNumMesi(mPenaIrrogataInCumulo.getNumMesiArresto());
			lCalArrestiTotMod.setNumGiorni(mPenaIrrogataInCumulo.getNumGiorniArresto());
			if (mPenaIrrogataInCumulo.getImportoAmmenda() != null)
				lCalArrestiTotMod.setImportoAmmenda(mPenaIrrogataInCumulo.getImportoAmmenda().doubleValue());
		} else if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_SOSPENSIONE
				|| mTipoPenaIniziale == ICostantiCalcoloPena.PENA_SOSPENSIONE_RES) {
			// Recupero la Reclusione
			lCalReclusioneTotMod.setNumAnni(mPenaDopoSospensione.getNumAnniReclusione());
			lCalReclusioneTotMod.setNumMesi(mPenaDopoSospensione.getNumMesiReclusione());
			lCalReclusioneTotMod.setNumGiorni(mPenaDopoSospensione.getNumGiorniReclusione());
			if (mPenaDopoSospensione.getImportoMulta() != null)
				lCalReclusioneTotMod.setImportoMulta(mPenaDopoSospensione.getImportoMulta().doubleValue());

			// Recupero l'Arresto
			lCalArrestiTotMod.setNumAnni(mPenaDopoSospensione.getNumAnniArresto());
			lCalArrestiTotMod.setNumMesi(mPenaDopoSospensione.getNumMesiArresto());
			lCalArrestiTotMod.setNumGiorni(mPenaDopoSospensione.getNumGiorniArresto());
			if (mPenaDopoSospensione.getImportoAmmenda() != null)
				lCalArrestiTotMod.setImportoAmmenda(mPenaDopoSospensione.getImportoAmmenda().doubleValue());

		} else if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_REVOCA_MA
				|| mTipoPenaIniziale == ICostantiCalcoloPena.PENA_CESSAZIONE_MA) {
			// Recupero la Reclusione
			lCalReclusioneTotMod.setNumAnni(mPenaDopoRevocaMA.getNumAnniReclusione());
			lCalReclusioneTotMod.setNumMesi(mPenaDopoRevocaMA.getNumMesiReclusione());
			lCalReclusioneTotMod.setNumGiorni(mPenaDopoRevocaMA.getNumGiorniReclusione());
			if (mPenaDopoRevocaMA.getImportoMulta() != null)
				lCalReclusioneTotMod.setImportoMulta(mPenaDopoRevocaMA.getImportoMulta().doubleValue());

			// Recupero l'Arresto
			lCalArrestiTotMod.setNumAnni(mPenaDopoRevocaMA.getNumAnniArresto());
			lCalArrestiTotMod.setNumMesi(mPenaDopoRevocaMA.getNumMesiArresto());
			lCalArrestiTotMod.setNumGiorni(mPenaDopoRevocaMA.getNumGiorniArresto());
			if (mPenaDopoRevocaMA.getImportoAmmenda() != null)
				lCalArrestiTotMod.setImportoAmmenda(mPenaDopoRevocaMA.getImportoAmmenda().doubleValue());

		} else if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_REVOCA_INDULTINO) {
			// Recupero la Reclusione
			lCalReclusioneTotMod.setNumAnni(mPenaDopoRevocaIndultino.getNumAnniReclusione());
			lCalReclusioneTotMod.setNumMesi(mPenaDopoRevocaIndultino.getNumMesiReclusione());
			lCalReclusioneTotMod.setNumGiorni(mPenaDopoRevocaIndultino.getNumGiorniReclusione());
			if (mPenaDopoRevocaIndultino.getImportoMulta() != null)
				lCalReclusioneTotMod
						.setImportoMulta(mPenaDopoRevocaIndultino.getImportoMulta().doubleValue());

			// Recupero l'Arresto
			lCalArrestiTotMod.setNumAnni(mPenaDopoRevocaIndultino.getNumAnniArresto());
			lCalArrestiTotMod.setNumMesi(mPenaDopoRevocaIndultino.getNumMesiArresto());
			lCalArrestiTotMod.setNumGiorni(mPenaDopoRevocaIndultino.getNumGiorniArresto());
			if (mPenaDopoRevocaIndultino.getImportoAmmenda() != null)
				lCalArrestiTotMod
						.setImportoAmmenda(mPenaDopoRevocaIndultino.getImportoAmmenda().doubleValue());

		} else if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_MANUALE
				|| mTipoPenaIniziale == ICostantiCalcoloPena.PENA_MANUALE_RES) {
			// Recupero la Reclusione
			lCalReclusioneTotMod.setNumAnni(mPenaResiduaManuale.getNumAnniReclusione());
			lCalReclusioneTotMod.setNumMesi(mPenaResiduaManuale.getNumMesiReclusione());
			lCalReclusioneTotMod.setNumGiorni(mPenaResiduaManuale.getNumGiorniReclusione());
			if (mPenaResiduaManuale.getImportoMulta() != null)
				lCalReclusioneTotMod.setImportoMulta(mPenaResiduaManuale.getImportoMulta().doubleValue());

			// Recupero l'Arresto
			lCalArrestiTotMod.setNumAnni(mPenaResiduaManuale.getNumAnniArresto());
			lCalArrestiTotMod.setNumMesi(mPenaResiduaManuale.getNumMesiArresto());
			lCalArrestiTotMod.setNumGiorni(mPenaResiduaManuale.getNumGiorniArresto());
			if (mPenaResiduaManuale.getImportoAmmenda() != null)
				lCalArrestiTotMod.setImportoAmmenda(mPenaResiduaManuale.getImportoAmmenda().doubleValue());
		} else if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_DA_INDULTO) {
			// Recupero la Reclusione
			lCalReclusioneTotMod.setNumAnni(mPenaDaIndulto.getNumAnniReclusione());
			lCalReclusioneTotMod.setNumMesi(mPenaDaIndulto.getNumMesiReclusione());
			lCalReclusioneTotMod.setNumGiorni(mPenaDaIndulto.getNumGiorniReclusione());
			if (mPenaDaIndulto.getImportoMulta() != null)
				lCalReclusioneTotMod.setImportoMulta(mPenaDaIndulto.getImportoMulta().doubleValue());

			// Recupero l'Arresto
			lCalArrestiTotMod.setNumAnni(mPenaDaIndulto.getNumAnniArresto());
			lCalArrestiTotMod.setNumMesi(mPenaDaIndulto.getNumMesiArresto());
			lCalArrestiTotMod.setNumGiorni(mPenaDaIndulto.getNumGiorniArresto());
			if (mPenaDaIndulto.getImportoAmmenda() != null)
				lCalArrestiTotMod.setImportoAmmenda(mPenaDaIndulto.getImportoAmmenda().doubleValue());
		} else if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_ARCHIVIATA_RES
				|| mTipoPenaIniziale == ICostantiCalcoloPena.PENA_ARCHIVIATA_SIEP) {
			// Recupero la Reclusione
			lCalReclusioneTotMod.setNumAnni(mPenaDaArchiviazione.getNumAnniReclusione());
			lCalReclusioneTotMod.setNumMesi(mPenaDaArchiviazione.getNumMesiReclusione());
			lCalReclusioneTotMod.setNumGiorni(mPenaDaArchiviazione.getNumGiorniReclusione());
			if (mPenaDaArchiviazione.getImportoMulta() != null)
				lCalReclusioneTotMod.setImportoMulta(mPenaDaArchiviazione.getImportoMulta().doubleValue());

			// Recupero l'Arresto
			lCalArrestiTotMod.setNumAnni(mPenaDaArchiviazione.getNumAnniArresto());
			lCalArrestiTotMod.setNumMesi(mPenaDaArchiviazione.getNumMesiArresto());
			lCalArrestiTotMod.setNumGiorni(mPenaDaArchiviazione.getNumGiorniArresto());
			if (mPenaDaArchiviazione.getImportoAmmenda() != null)
				lCalArrestiTotMod.setImportoAmmenda(mPenaDaArchiviazione.getImportoAmmenda().doubleValue());
		} else if (mTipoPenaIniziale == ICostantiCalcoloPena.PENA_DA_REVOCA_SS) {
			// Recupero la Reclusione
			lCalReclusioneTotMod.setNumAnni(mPenaDaRevocaSS.getNumAnniReclusione());
			lCalReclusioneTotMod.setNumMesi(mPenaDaRevocaSS.getNumMesiReclusione());
			lCalReclusioneTotMod.setNumGiorni(mPenaDaRevocaSS.getNumGiorniReclusione());
			if (mPenaDaRevocaSS.getImportoMulta() != null)
				lCalReclusioneTotMod.setImportoMulta(mPenaDaRevocaSS.getImportoMulta().doubleValue());

			// Recupero l'Arresto
			lCalArrestiTotMod.setNumAnni(mPenaDaRevocaSS.getNumAnniArresto());
			lCalArrestiTotMod.setNumMesi(mPenaDaRevocaSS.getNumMesiArresto());
			lCalArrestiTotMod.setNumGiorni(mPenaDaRevocaSS.getNumGiorniArresto());
			if (mPenaDaRevocaSS.getImportoAmmenda() != null)
				lCalArrestiTotMod.setImportoAmmenda(mPenaDaRevocaSS.getImportoAmmenda().doubleValue());

		}

		// ==========================================================================
		// Computo separatamente il saldo di reclusione e arresto tanto non sono in
		// grado di stabilire l'ordine di inserimento. Calcolo quindi prima tutti i
		// quantum concessi, li detraggo dalla pena iniziale, verifico se la reclusione
		// risulta negativa. In questo caso la azzero e detraggo la differenza dall'arresto.
		// ATTENZIOINE!!! verificare cosa accade se il quantum diventa negativo e
		// continuo a sottrarre (dovrebbe funzionare)COL C... CHE FUNZIONA!!!!!!!!
		// Modificare il calcolo per non scalare da arresti
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Quantum di 'Pena Iniziale'");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Reclusione Pena Iniziale: " + lCalReclusioneTotMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Arresti Pena Iniziale   : " + lCalArrestiTotMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Sommo/sottraggo i computi alla pena iniziale");

		// ==========================================================================
		// n.b Prima sommo eventuali revoche, poi effettuo le sottrazioni per evitare
		// problemi con quantum negativi
		// ==========================================================================
		// Sommo le Revoche Benefici
		lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod,
				this.getBeneficiReclusione("+"));
		lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod, this.getBeneficiArresti("+"));

		// Sommo i Computi Revocati (Presofferto e fungibilità MC/PD)
		lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod,
				this.getComputiReclusione("+"));
		lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod, this.getComputiArresti("+"));

		// Sommo le Revoche Altro
		lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod,
				this.getAltroReclusione("+"));
		lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod, this.getAltroArresti("+"));

		// Sommo i computi RES
		lCalReclusioneTotMod = lCalUtil.sommaGiornieValute(lCalReclusioneTotMod,
				this.getComputoResReclusione("+"));
		lCalArrestiTotMod = lCalUtil.sommaGiornieValute(lCalArrestiTotMod, this.getComputoResArresti("+"));

		// Sottraggo i Benefici CONCESSI (Depenalizzazione-Incostituzionalità, Amnistia/Indulto)
		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod,
				this.getBeneficiReclusione("-"));
		lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod, this.getBeneficiArresti("-"));

		// Sottraggo i Computi Concessi (Presofferto e fungibilità MC/PD)
		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod,
				this.getComputiReclusione("-"));
		lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod, this.getComputiArresti("-"));

		// Sottraggo Altri Computi
		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod,
				this.getAltroReclusione("-"));
		lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod, this.getAltroArresti("-"));

		// Sottraggo Computi RES
		lCalReclusioneTotMod = lCalUtil.sottraiGiorniValuteNew(lCalReclusioneTotMod,
				this.getComputoResReclusione("-"));
		lCalArrestiTotMod = lCalUtil.sottraiGiorniValuteNew(lCalArrestiTotMod,
				this.getComputoResArresti("-"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Reclusione: " + lCalReclusioneTotMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale Arresti   : " + lCalArrestiTotMod);

		// ==========================================================================
		// Verifico se i quantum di Reclusione sono diventati Negativi, in questo
		// caso azzero i quantum di Reclusione e scarico la differenza sugli Arresti
		// ==========================================================================
		if (!lCalUtil.isPositiveTime(lCalReclusioneTotMod)) {
			// Quantum di Reclusione negativi
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
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

		// ==========================================================================
		// Verifico se l'importo della Multa è negativo, in questo caso lo azzero
		// e scarico la differenza sull'ammenda. Se anche l'ammenda risulta negativa
		// azzero anche l'ammenda.
		// ==========================================================================
		if (lCalReclusioneTotMod.getImportoMulta() < 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ImportoMulta negativo: " + lCalReclusioneTotMod.getImportoMulta());

			lCalArrestiTotMod.setImportoAmmenda(
					lCalArrestiTotMod.getImportoAmmenda() + lCalReclusioneTotMod.getImportoMulta());

			lCalReclusioneTotMod.setImportoMulta(0);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nuovo importo Ammenda: " + lCalArrestiTotMod.getImportoAmmenda());
			if (lCalArrestiTotMod.getImportoAmmenda() < 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nuovo importo negativo, lo azzero.");
				lCalArrestiTotMod.setImportoAmmenda(0);
			}
		}

		// ==========================================================================
		// Carico il model di ritorno con quantum e importi calcolati
		// ==========================================================================
		lPenaDaEspiare.setQuantumReclusione(lCalReclusioneTotMod);
		lPenaDaEspiare.setImportoMulta(new BigDecimal(lCalReclusioneTotMod.getImportoMulta()));

		lPenaDaEspiare.setQuantumArresto(lCalArrestiTotMod);
		lPenaDaEspiare.setImportoAmmenda(new BigDecimal(lCalArrestiTotMod.getImportoAmmenda()));

		// ==========================================================================
		// Determino le date di espiazione a partire dai quantum
		// ==========================================================================
		if (aDataInizioPena != null) {
			ICalcoloPena lCalPenCtrl = SIEPLookupRemote.getCalcoloPenaRemote();

			Vector lDateFine = lCalPenCtrl.exCalcolaDataFinePena(aDataInizioPena, lPenaDaEspiare, true);

			lPenaDaEspiare.setDataInizio(aDataInizioPena);

			if (lDateFine.size() == 1) { // solo Reclusione o Arresti: ho quindi solo data fine
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Ho solo una data: " + lDateFine.get(0));

				if (!lCalUtil.isZero(lPenaDaEspiare.getQuantumArresto())) {
					// Solo arresti
					lPenaDaEspiare.setDataInizioArresto(aDataInizioPena);
				}

				lPenaDaEspiare.setDataFine((Date) lDateFine.get(0));
				lPenaDaEspiare.setDataFinePresunta(lPenaDaEspiare.getDataFine());
			} else if (lDateFine.size() == 2) { // Sono presenti sia Reclusione che Arresti
				lPenaDaEspiare.setDataFineReclusione((Date) lDateFine.get(0));
				lPenaDaEspiare
						.setDataInizioArresto(DateUtils.getDayAfter(lPenaDaEspiare.getDataFineReclusione()));
				lPenaDaEspiare.setDataFine((Date) lDateFine.get(1));
				lPenaDaEspiare.setDataFinePresunta(lPenaDaEspiare.getDataFine());
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Attenzione quantum negativi o nulli Impossibile determinare la data fine pena");
				// In caso di quantum negativi (benefici), utilizzo la data fine prevista
				// Se quantum < 0 fine pena = fine pena di partenza
				// Se quantum = 0 fine pena = inizio pena

				if (mPenaUltimaValidata != null) {
					if (CalendarUtil.getTotGiorni(lCalReclusioneTotMod) == 0
							&& CalendarUtil.getTotGiorni(lCalArrestiTotMod) == 0
							&& mPenaUltimaValidata.getDataInizio() != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Quantum nulli, fine pena = inizio pena");
						lPenaDaEspiare.setDataFine(mPenaUltimaValidata.getDataInizio());
						lPenaDaEspiare.setDataFinePresunta(mPenaUltimaValidata.getDataInizio());
					} else if ((CalendarUtil.getTotGiorni(lCalReclusioneTotMod) < 0
							|| CalendarUtil.getTotGiorni(lCalArrestiTotMod) < 0)
							&& mPenaUltimaValidata.getDataFine() != null) { // Quantum <0 ricopio il fine pena
																			// previsto
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Quantum nulli, fine pena = fine pena iniziale");
						lPenaDaEspiare.setDataFine(mPenaUltimaValidata.getDataFinePresunta());
						lPenaDaEspiare.setDataFinePresunta(mPenaUltimaValidata.getDataFine());
					}
				}
			}
		}

		// ==========================================================================
		// Anticipo il fine pena se sono presenti LA da computare
		// n.b. se la data fine Reclusione diventa antecedente l'inizio pena,
		// non vanno valorizzati i campi inizio arresti e fine reclusione, ma
		// solo il fine pena
		// se la data rideterminata diventa < data inizio pena??? in questo caso
		// dovrei avere fungibilità?
		//
		// 24/06/2009
		// - aggiunta gestione delle ridimensionamento LA e scomputo Permessi e licenze
		// n.b.
		// ==========================================================================
		// int lGiorniLA = getLiberazioneAnticipata();
		int lGiorniLA = 0;
		if (tipoCalcolo != null && tipoCalcolo.equals("all")) {
			lGiorniLA = getLiberazioneAnticipata();
		} else {
			lGiorniLA = getLiberazioneAnticipataGiaConcesse();
		}

		// Aggiungo o meglio sottraggo i giorni di scomputo permesso
		// ANNA lGiorniLA = lGiorniLA - this.getTotaleScomputi();
		if (tipoCalcolo != null && tipoCalcolo.equals("all")) {
			lGiorniLA = lGiorniLA + getTotaleScomputi();
		} else {
			lGiorniLA = lGiorniLA + this.getScomputiGiaConcessi();
		}

		// Rimedi Risarcitori DL92
		if (tipoCalcolo != null && tipoCalcolo.equals("all")) {
			lGiorniLA = lGiorniLA + getRimediRisarcitori();
		} else {
			lGiorniLA = lGiorniLA + getRimediRisarcitoriGiaConcessi();
		}

		// if (aDataInizioPena!=null && lGiorniLA>0){
		// if (lPenaDaEspiare.getDataFine()!=null && lGiorniLA>0){
		if (lPenaDaEspiare.getDataFine() != null) {
			// n.b. devo utilizzare la data fine in quanto se i lPenaDaEspiare<=0
			// non ho data di espiazione da anticipare
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Anticipo il fine pena per effetto di " + lGiorniLA
					+ " giorni di anticipazione (LA,PP,PE,RD)");

			// Arretro le data fine reclusione, inizio arresto, fine pena
			if (lPenaDaEspiare.getDataFineReclusione() != null) {
				lPenaDaEspiare.setDataFineReclusione(DateUtils.moveDateTo(
						lPenaDaEspiare.getDataFineReclusione(), Calendar.DAY_OF_MONTH, -lGiorniLA));
			}
			if (lPenaDaEspiare.getDataInizioArresto() != null) {
				lPenaDaEspiare.setDataInizioArresto(DateUtils.moveDateTo(
						lPenaDaEspiare.getDataInizioArresto(), Calendar.DAY_OF_MONTH, -lGiorniLA));
			}
			if (lPenaDaEspiare.getDataFine() != null) {
				lPenaDaEspiare.setDataFine(DateUtils.moveDateTo(lPenaDaEspiare.getDataFine(),
						Calendar.DAY_OF_MONTH, -lGiorniLA));
				lPenaDaEspiare.setDataFinePresunta(lPenaDaEspiare.getDataFine());
			}

			// Azzero le date se per effetto delle LA sono arretrate oltre la data inizio pena
			if (lPenaDaEspiare.getDataFineReclusione() != null
					&& DateUtils.isGreater(aDataInizioPena, lPenaDaEspiare.getDataFineReclusione())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data fine reclusione < data inizio pena per effetto arretramento LA");
				lPenaDaEspiare.setDataFineReclusione(null);
			}

			if (lPenaDaEspiare.getDataInizioArresto() != null
					&& DateUtils.isGreater(aDataInizioPena, lPenaDaEspiare.getDataInizioArresto())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data inizio arresto < data inizio pena per effetto arretramento LA");
				lPenaDaEspiare.setDataInizioArresto(null);
			}

			//
			if (lPenaDaEspiare.getDataFine() != null
					&& DateUtils.isGreater(aDataInizioPena, lPenaDaEspiare.getDataFine())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Attenzione!!! INIZIO pena < fine pena rideterminato per effetto delle LA ");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("In teoria ho un periodo fungibile");
			}
		}

		// ==========================================================================
		// Se è stata specificata la data fine, la forzo in modo da utilizzarla anche
		// per il calcolo della fungibilità e pena espiata
		// ==========================================================================
		if (aDataFineDaForzare != null) {
			lPenaDaEspiare.setDataFine(aDataFineDaForzare);
			lPenaDaEspiare.setDataFinePresunta(lPenaDaEspiare.getDataFine());
		}

		// ==========================================================================
		// Calcolo la pena espiata se ho una data di decorrenza
		// ==========================================================================
		if (aDataInizioPena != null) {
			CalendarModel lPenaGiaEspiata = new CalendarModel();

			if (!DateUtils.isGreater(aDataInizioPena, lDataSistemaPerCalcoli)) {
				// Il calcolo dell'espiato viene fatto solo se la data inizio<=data calcoli
				lPenaGiaEspiata.setDataInizio(aDataInizioPena);
				lPenaGiaEspiata.setDataFine(lDataSistemaPerCalcoli);

				lPenaGiaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lPenaGiaEspiata, false);
				lPenaGiaEspiata = lCalUtil.ricalcolaGAM(lPenaGiaEspiata);

				this.mPenaEspiata = lPenaGiaEspiata;
			}
		}

		// ==========================================================================
		// Valuto la fungibilità:
		// Ho fungibilità se:
		// 1) Quantum rideterminato nullo, ma pena in espiazione: in questo caso la
		// fungibilità coincide con la pena espiata
		// 2) Quantum rideterminato positivo, ma data fine pena calcolata < data
		// di sistema. La fungibilità è rappresentata dal quantum espiato dalla
		// data fine prevista alla data di systema.
		// 3) Quantum rideterminato positivo, ma data fine pena calcolata < data
		// inizio. Questo caso si può determinare per effetto dell'applicazione
		// delle LA che arretrano la data fine.
		// 4) Quantum rideterminato negativo, la fungibilità è data dal quantum
		// negativo (abs) più l'eventuale pena già espiata (se detenuto)
		//
		// ==========================================================================
		FungibilitaModel lFungibilitaModel = new FungibilitaModel();
		CalendarModel lTotRideterminato = new CalendarModel();
		lTotRideterminato = lCalUtil.sommaGiorni(lPenaDaEspiare.getQuantumReclusione(),
				lPenaDaEspiare.getQuantumArresto());

		if (lCalUtil.isZero(lTotRideterminato)) {
			// Quantum Rideterminati NULLI
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena complessiva rideterminata = 0");

			if (aDataInizioPena != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena in espiazione, fungibilità = pena già espiata.");

				CalendarModel lPenaGiaEspiata = new CalendarModel();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lDataSistemaPerCalcoli = " + lDataSistemaPerCalcoli);

				lPenaGiaEspiata.setDataInizio(aDataInizioPena);
				lPenaGiaEspiata.setDataFine(lDataSistemaPerCalcoli);

				lPenaGiaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lPenaGiaEspiata, false); // false =
																								// conteggio
																								// anche il
																								// giorno
																								// inizio pena
																								// come
																								// espiato

				lPenaGiaEspiata = lCalUtil.ricalcolaGAM(lPenaGiaEspiata); // Normalizzo

				lFungibilitaModel.setNumAnni(new BigDecimal(lPenaGiaEspiata.getNumAnni()));
				lFungibilitaModel.setNumMesi(new BigDecimal(lPenaGiaEspiata.getNumMesi()));
				lFungibilitaModel.setNumGiorni(new BigDecimal(lPenaGiaEspiata.getNumGiorni()));

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Reimposto la data fine pena = data inizio");
				// lDataFinePena = lUltimaPenRes.getDataInizio(); //???????? Perchè ???
			}
		} else if (lCalUtil.isPositiveTime(lTotRideterminato) && aDataInizioPena != null
				&& lPenaDaEspiare.getDataFine() != null) {
			// Quantum positivi verifico se il fine pena calcolato < data di systema
			if (DateUtils.isLower(lPenaDaEspiare.getDataFine(), lDataSistemaPerCalcoli)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data fine pena < lDataSistemaPerCalcoli ");

				CalendarModel lPenaEspiataInEccesso = new CalendarModel();
				lPenaEspiataInEccesso.setDataInizio(lPenaDaEspiare.getDataFine());
				lPenaEspiataInEccesso.setDataFine(lDataSistemaPerCalcoli);
				lPenaEspiataInEccesso = lCalUtil.CalcolaNumGiorniMesiAnni(lPenaEspiataInEccesso, true); // true
																										// =
																										// non
																										// considero
																										// il
																										// giorno
																										// fine
																										// pena
																										// previsto
																										// come
																										// da
																										// espiare
				lPenaEspiataInEccesso = lCalUtil.ricalcolaGAM(lPenaEspiataInEccesso); // Normalizzo

				lFungibilitaModel.setNumAnni(new BigDecimal(lPenaEspiataInEccesso.getNumAnni()));
				lFungibilitaModel.setNumMesi(new BigDecimal(lPenaEspiataInEccesso.getNumMesi()));
				lFungibilitaModel.setNumGiorni(new BigDecimal(lPenaEspiataInEccesso.getNumGiorni()));
			}
		} else if (!lCalUtil.isPositiveTime(lTotRideterminato)) {
			// Quantum negativi
			if (aDataInizioPena != null && !DateUtils.isGreater(aDataInizioPena, lDataSistemaPerCalcoli)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Soggetto in espiazione, calcolo la fungibilità come somma dell'espiato e del quantum negativo");
				// Calcolo la pena già espiata
				CalendarModel lPenaGiaEspiata = new CalendarModel();

				lPenaGiaEspiata.setDataInizio(aDataInizioPena);
				lPenaGiaEspiata.setDataFine(lDataSistemaPerCalcoli);
				lPenaGiaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lPenaGiaEspiata, false); // false =
																								// conteggio
																								// anche il
																								// giorno
																								// inizio pena
																								// come
																								// espiato
				lPenaGiaEspiata = lCalUtil.ricalcolaGAM(lPenaGiaEspiata); // Normalizzo

				// Sommo la pena già espiata al quantum negativo rideterminato
				CalendarModel lPenaEspiataInEccesso = new CalendarModel();
				lPenaEspiataInEccesso = lCalUtil.sommaGiorni(lCalUtil.abs(lTotRideterminato),
						lPenaGiaEspiata);
				lFungibilitaModel.setNumAnni(new BigDecimal(lPenaEspiataInEccesso.getNumAnni()));
				lFungibilitaModel.setNumMesi(new BigDecimal(lPenaEspiataInEccesso.getNumMesi()));
				lFungibilitaModel.setNumGiorni(new BigDecimal(lPenaEspiataInEccesso.getNumGiorni()));
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Soggetto non in espiazione, la fungibilità è pari al quantum rideterminato negativo");
				CalendarModel lPenaEspiataInEccesso = lCalUtil.abs(lTotRideterminato);
				lFungibilitaModel.setNumAnni(new BigDecimal(lPenaEspiataInEccesso.getNumAnni()));
				lFungibilitaModel.setNumMesi(new BigDecimal(lPenaEspiataInEccesso.getNumMesi()));
				lFungibilitaModel.setNumGiorni(new BigDecimal(lPenaEspiataInEccesso.getNumGiorni()));
			}
			// ========================================================================
			// Azzero i quantum di pena, non hanno senso qunatum negativi
			// ========================================================================
			// lPenaDaEspiare.setNumAnniReclusione (new BigDecimal(0));
			// lPenaDaEspiare.setNumMesiReclusione (new BigDecimal(0));
			// lPenaDaEspiare.setNumGiorniReclusione (new BigDecimal(0));
			//
			// lPenaDaEspiare.setNumAnniArresto (new BigDecimal(0));
			// lPenaDaEspiare.setNumMesiArresto (new BigDecimal(0));
			// lPenaDaEspiare.setNumGiorniArresto (new BigDecimal(0));
		}

		this.mPenaResiduaRicalcolata = lPenaDaEspiare;
		// Imposto la fungibilità
		this.mFungibilitaCalcolata = lFungibilitaModel;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lPenaDaEspiare = " + lPenaDaEspiare);

		return lPenaDaEspiare;
	}

	/**
	 * Effettua il calcolo della SS da espiare a partire dall'ultima SS a sistema sottraendo o sommando i
	 * quantum specificati in input
	 * 
	 * @param aTotComputi
	 *            calendar model con il totale da DETENTIVA da scomputare sulla SS a sistema
	 * @return
	 */
	public SanzioneSostResiduaModel getSanzioneSostitutivaDaEspiare(CalendarModel aTotComputi) {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("===============================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("    CALCOLO LE SANZIONI SOSTITUTIVE RESIDUE    ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("===============================================");
		SanzioneSostResiduaModel lSSDaEspiare = null;

		CalendarUtil lCalUtil = new CalendarUtil();

		// ==========================================================================
		// new! Gestione delle sanzioni sostitutive. dalla vers 3.0
		// Devo calcolare la Sanzione Sostitutiva residua, quella da eseguire,
		// partendo dall'ultima SS (calcolo abFine) scomputando i quantum passati in
		// input
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Sanzione Sostitutiva in Sentenza = " + mSanzioneSostitutiva);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ultima Sanzione Sostitutiva Residua = " + mUltimaSanzSostResidua);

		if (mSanzioneSostitutiva != null && mSanzioneSostitutiva.getIdSanzioneSostitutiva() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("presente sanzione sostitutiva in sentenza, effettuo i calcoli");

			CalendarModel lSSIniziale = new CalendarModel();
			CalendarModel lSSRideterminata = new CalendarModel();

			CalendarModel lTotDaComputare = null;
			if (aTotComputi != null) {
				lTotDaComputare = new CalendarModel(aTotComputi);
			} else {
				lTotDaComputare = new CalendarModel();
			}

			// ====================================================================
			// Carico la SSiniziale recuperando i dati o dalla SS in sentenza o
			// dall'ultima SS ricalcolata se esite
			// ====================================================================
			if (mUltimaSanzSostResidua != null) {
				// Ultima SS ricalcolata
				lSSIniziale.setNumAnni(mUltimaSanzSostResidua.getNumAnni());
				lSSIniziale.setNumMesi(mUltimaSanzSostResidua.getNumMesi());
				lSSIniziale.setNumGiorni(mUltimaSanzSostResidua.getNumGiorni());
				if (mUltimaSanzSostResidua.getSanzionePecuniariaMulta() != null) {
					lSSIniziale.setImportoMulta(
							mUltimaSanzSostResidua.getSanzionePecuniariaMulta().doubleValue());
				}
				if (mUltimaSanzSostResidua.getSanzionePecuniariaAmmenda() != null) {
					lSSIniziale.setImportoAmmenda(
							mUltimaSanzSostResidua.getSanzionePecuniariaAmmenda().doubleValue());
				}
			} else {
				// SS in sentenza
				lSSIniziale.setNumAnni(mSanzioneSostitutiva.getNumAnni());
				lSSIniziale.setNumMesi(mSanzioneSostitutiva.getNumMesi());
				lSSIniziale.setNumGiorni(mSanzioneSostitutiva.getNumGiorni());
				if (mSanzioneSostitutiva.getSanzionePecuniariaMulta() != null) {
					lSSIniziale
							.setImportoMulta(mSanzioneSostitutiva.getSanzionePecuniariaMulta().doubleValue());
				}
				if (mSanzioneSostitutiva.getSanzionePecuniariaAmmenda() != null) {
					lSSIniziale.setImportoAmmenda(
							mSanzioneSostitutiva.getSanzionePecuniariaAmmenda().doubleValue());
				}
			}

			// Sottraggo il totale computi
			if (mSanzioneSostitutiva.getCodTipoSanzione().equals("S")) {
				// Semidetenzione: conversione 1 a 1, 1g di detentiva = 1 g di semidetenzione
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("Semidetenzione: conversione 1 a 1, 1g di detentiva = 1 g di semidetenzione");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lTotDaComputare = " + lTotDaComputare);
				lSSRideterminata = lCalUtil.sottraiGiorniValuteNew(lSSIniziale, lTotDaComputare);
			} else if (mSanzioneSostitutiva.getCodTipoSanzione().equals("L")) {
				// Libertà Controllata: conversione 1 a 2, 1g di detentiva = 2g di Libertà Controllata
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Libertà Controllata: conversione 1 a 2, 1g di detentiva = 2g di Libertà Controllata");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lTotDaComputare = " + lTotDaComputare);
				lTotDaComputare.setNumAnni(2 * lTotDaComputare.getNumAnni());
				lTotDaComputare.setNumMesi(2 * lTotDaComputare.getNumMesi());
				lTotDaComputare.setNumGiorni(2 * lTotDaComputare.getNumGiorni());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lTotDaComputare convertito = " + lTotDaComputare);

				lSSRideterminata = lCalUtil.sottraiGiorniValuteNew(lSSIniziale, lTotDaComputare);
			}
			// else if (mSanzioneSostitutiva.getCodTipoSanzione().equals("P")){
			// // In tutti gli altri casi non c'è conversione
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("SS di altro tipo non effettuo calcoli");
			// // Pena Pecuniaria:
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Semidetenzione: conversione 1 a 1, 1g di detentiva = 1 g di semidetenzione");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lTotDaComputare = "+lTotDaComputare);
			// lSSRideterminata = lCalUtil.sottraiGiorniValuteNew(lSSIniziale, lTotDaComputare);
			// }
			else {
				// In tutti gli altri casi non c'è conversione
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("SS di altro tipo non effettuo calcoli");
				lSSRideterminata = new CalendarModel(lSSIniziale);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lSSRideterminata = " + lSSRideterminata);

			// =============================================
			// Carico la SS da espiare
			// =============================================
			lSSDaEspiare = new SanzioneSostResiduaModel();

			lSSDaEspiare.setCodTipoSanzione(mSanzioneSostitutiva.getCodTipoSanzione());

			lSSDaEspiare.setNumAnni(new BigDecimal(lSSRideterminata.getNumAnni()));
			lSSDaEspiare.setNumMesi(new BigDecimal(lSSRideterminata.getNumMesi()));
			lSSDaEspiare.setNumGiorni(new BigDecimal(lSSRideterminata.getNumGiorni()));
			lSSDaEspiare.setSanzionePecuniariaMulta(new BigDecimal(lSSRideterminata.getImportoMulta()));
			lSSDaEspiare.setSanzionePecuniariaAmmenda(new BigDecimal(lSSRideterminata.getImportoAmmenda()));
		}

		return lSSDaEspiare;
	}

	/**
	 * Calcola la pena residua da espiare e il quantum di pena espiato in seguito a una
	 * sospensione/interruzione dell'esecuzione della pena
	 * 
	 * @param aPenResMod
	 *            Model contenente la pena residua in decorrenza. Tale model deve contenere i quantum totali e
	 *            le date già anticipate per effetto di eventuali LA. Se il model non viene passato in input
	 *            viene utilizzato il quantum calcolato dall'ultima invocazione del metodo getPenaDaEspiare
	 * @param aDataSosp
	 *            data sospensione/interruzione (obbligatoria)
	 * @return non ritorna nulla, ma valorizza le variabili mPenaResiduaRicalcolata e mPenaEspiata con la pena
	 *         residua dopo la sospensione e la pena espiata. Le due quantità sono recuperabili invocando i
	 *         metodi getPenaResiduaRicalcolata e getPenaEspiata.
	 * @throws Exception
	 */
	public void calcolaPenaDaSospensione(PenaResiduaModel aPenResMod, Date aDataSosp) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("            CALCOLO SOSPENSIONE                 ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("================================================");
		PenaResiduaModel lPenRes = null;
		if (aPenResMod != null) {
			lPenRes = aPenResMod;
		} else if (mPenaResiduaRicalcolata != null) {
			lPenRes = mPenaResiduaRicalcolata;
		} else {
			throw new Exception(
					"Attenzione! Pena in decorrenza non valorizzata. Impossibile calcolare la sospensione.");
		}

		// if (aDataSosp==null){
		// throw new
		// Exception("Attenzione! Data di sospensione/interruzione non specificata. Impossibile calcolare la
		// sospensione.");
		// }

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusione = lPenRes.getQuantumReclusione();
		CalendarModel lCalArresto = lPenRes.getQuantumArresto();

		// Recupero le date di decorrenza della pena
		Date lDataInizio = lPenRes.getDataInizio();
		Date lDataFineReclusione = lPenRes.getDataFineReclusione();
		Date lDataInizioArresto = lPenRes.getDataInizioArresto();
		Date lDataFine = lPenRes.getDataFine();

		BigDecimal lMulta = lPenRes.getImportoMulta();
		BigDecimal lAmmenda = lPenRes.getImportoAmmenda();

		// ==========================================================================
		// Il metodo calcola la pena da espiare (Reclusione+Arresto) come intervallo
		// tra la data di sospensione e la data fine pena prevista (quella anticipata
		// per effetto delle LA).
		// Calcola quindi il quantum di pena espiata come DIFFERENZA tra il quantum
		// totale (Reclusione + Arresto) prima dell'interruzione e il quantum residuo
		// (Reclusione + Arresto) dopo l'interruzione.
		// Il quantum residuo potrebbe essere composto da Reclusione e Arresto.
		// Per il calcolo dei quantum residui fanno fede le date di decorrenza
		// eventualmente anticipate per effetto delle LA
		//
		// Casi possibili:
		// 1) Solo Reclusione o solo Arresto: (data fine-data interruzione)
		// 2) presenti sia reclusione che arresto
		// 2a - Dint>data inizio arresto ==>
		// 2b - Dint<data fine reclusione
		// ==========================================================================
		CalendarModel lCalRecDaEspiare = new CalendarModel(); // inizializza a 0
		CalendarModel lCalArrDaEspiare = new CalendarModel(); // inizializza a 0
		CalendarModel lTotEspiato = new CalendarModel(); // inizializza a 0

		if (lDataInizio != null && aDataSosp != null) {
			// Calcolo i quantum da espiare solo se ho una data di decorrenza e una
			// data di sospensione
			if (lDataFine != null && aDataSosp.after(lDataFine)) {
				// Interruzione successiva alla data fine pena prevista. La pena è
				// considerata interamente espiata.
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("aDataSosp (" + DateUtils.getDateToString(aDataSosp, "dd/MM/yyyy")
						+ ") after lDataFine (" + DateUtils.getDateToString(lDataFine, "dd/MM/yyyy") + ")");
				lCalRecDaEspiare = new CalendarModel();
				lCalArrDaEspiare = new CalendarModel();
			} else if (lDataInizioArresto != null && (aDataSosp.after(lDataInizioArresto)
					|| aDataSosp.compareTo(lDataInizioArresto) == 0)) {
				// Interruzione intervenuta nel periodo di Arresti. La reclusione residua
				// diventa nulla e ricalcolo solo il quantum di Arresti residui
				// n.b. il giorno della sospensione viene considerato espiato, il calcolo
				// parte quindi dal giorno successivo alla sospensione
				// Azzero la Reclusione
				lCalRecDaEspiare = new CalendarModel();

				// Calcolo il residuo Arresti
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("aDataSosp (" + DateUtils.getDateToString(aDataSosp, "dd/MM/yyyy")
						+ ") after or equal lDataInizioArresto ("
						+ DateUtils.getDateToString(lDataInizioArresto, "dd/MM/yyyy") + ")");
				lCalArrDaEspiare.setDataInizio(DateUtils.getDayAfter(aDataSosp));
				lCalArrDaEspiare.setDataFine(lDataFine);

				lCalArrDaEspiare = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrDaEspiare, false); // false =
																								// considero
																								// il gg
																								// successivo
																								// alla
																								// sospensione
				lCalArrDaEspiare = lCalUtil.ricalcolaGAM(lCalArrDaEspiare);
			} else if (lDataFineReclusione != null && aDataSosp.compareTo(lDataFineReclusione) == 0) {
				// Sospensione coincide con la data fine reclusione: poichè il giorno di
				// sospensione è considerato espiato, la reclusione viene azzerata e
				// gli arresti sono tutti ancora da scontare
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("data sospensione coincidente con data fine reclusione");

				// Azzero la Reclusione
				lCalRecDaEspiare = new CalendarModel();
				// Arresto, copio il quantum
				lCalArrDaEspiare = new CalendarModel(lCalArresto);
			} else if (lDataFineReclusione != null && aDataSosp.before(lDataFineReclusione)) {
				// Interruzione nel periodo di reclusione: il quantum di arresto, se presente
				// resta invariato, ricalcolo solo il residuo Reclusione
				// n.b. in questo caso si ricade anche se aDataSosp=
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("aDataSosp (" + DateUtils.getDateToString(aDataSosp, "dd/MM/yyyy")
						+ ") before lDataFineReclusione ("
						+ DateUtils.getDateToString(lDataFineReclusione, "dd/MM/yyyy") + ")");
				lCalRecDaEspiare.setDataInizio(DateUtils.getDayAfter(aDataSosp));
				lCalRecDaEspiare.setDataFine(lDataFineReclusione);
				lCalRecDaEspiare = lCalUtil.CalcolaNumGiorniMesiAnni(lCalRecDaEspiare, false);
				lCalRecDaEspiare = lCalUtil.ricalcolaGAM(lCalRecDaEspiare);

				// Arresto, copio il quantum
				lCalArrDaEspiare = new CalendarModel(lCalArresto);
			} else {
				// Mancano le date intermedie, ho un unico periodo in decorrenza. Potrebbe
				// essere o solo reclusione o solo aresto.
				// n.b. potrei avere sia il quantum di Reclusione che quello di Arresto
				// ma un solo periodo di decorrenza se, per effetto delle LA, la data
				// fine reclusione viene arretrata prima della data inizio pena.
				// In questo caso le date intermedie vengono poste a null e considero
				// come da espiare solo l'arresto.
				if (!lCalUtil.isZero(lCalArresto)) {
					// Ho solo arresti, o anche reclusione ma non in decorrenza perchè
					// manca la data
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Solo Arresti, o comunque mancano date intermedie");
					lCalArrDaEspiare.setDataInizio(DateUtils.getDayAfter(aDataSosp));
					lCalArrDaEspiare.setDataFine(lDataFine);

					lCalArrDaEspiare = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrDaEspiare, false); // false =
																									// considero
																									// il gg
																									// successivo
																									// alla
																									// sospensione
					lCalArrDaEspiare = lCalUtil.ricalcolaGAM(lCalArrDaEspiare);
				} else {
					// Ho solo reclusione
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Solo reclusione");
					lCalRecDaEspiare.setDataInizio(DateUtils.getDayAfter(aDataSosp));
					lCalRecDaEspiare.setDataFine(lDataFine);

					lCalRecDaEspiare = lCalUtil.CalcolaNumGiorniMesiAnni(lCalRecDaEspiare, false);
					lCalRecDaEspiare = lCalUtil.ricalcolaGAM(lCalRecDaEspiare);

				}
			}
			// ========================================================================
			// Calcolo la pena espiata come differenza tra il quantum totale in
			// espiazione e il quantum totale ancora da espiare.
			// ========================================================================
			CalendarModel lTotIniziale = lCalUtil.sommaGiorni(lCalReclusione, lCalArresto);
			CalendarModel lTotDaEspiare = lCalUtil.sommaGiorni(lCalRecDaEspiare, lCalArrDaEspiare);
			lTotEspiato = lCalUtil.sottraiGiorniNew(lTotIniziale, lTotDaEspiare);
		} else {
			// Pena non in decorrenza i quantum da espiare in seguito alla sospensione
			// coincidono con quelli attuali
			// n.b. anche se data sospensione è null(caso di revoca)
			lCalRecDaEspiare = new CalendarModel(lCalReclusione);
			lCalArrDaEspiare = new CalendarModel(lCalArresto);
			lTotEspiato = new CalendarModel(); // 0
		}

		// ==========================================================================
		// Ripulisco il model pena rideterminata e lo valorizzo con i nuovi quantum
		// ==========================================================================
		this.mPenaResiduaRicalcolata = null;
		mPenaResiduaRicalcolata = new PenaResiduaModel();
		mPenaResiduaRicalcolata.setQuantumReclusione(lCalRecDaEspiare);
		mPenaResiduaRicalcolata.setQuantumArresto(lCalArrDaEspiare);
		mPenaResiduaRicalcolata.setImportoMulta(lMulta);
		mPenaResiduaRicalcolata.setImportoAmmenda(lAmmenda);

		this.mPenaEspiata = lTotEspiato;

	}

	/**
	 * Restituisce la data dell'ultima sospensione (se presente)
	 * 
	 * @return data inizio sospensione o null
	 * @throws Exception
	 */
	public Date getDataUltimaSospensione() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerco data inizio ultima sospensione");
		Date lDataUltimaSosp = null;

		if (this.mPeneGiaEspiate.size() > 0) {
			SospensioneModel lSospMod = (SospensioneModel) mPeneGiaEspiate.elementAt(0);
			lDataUltimaSosp = lSospMod.getDataInizio();
		}

		return lDataUltimaSosp;
	}

	/**
	 * Restituisce la pena Virtuale calcolata come differenza tra i quantum di pena residua e le LA ancora da
	 * detrarre. Utilizzata nel caso di pena non in decorrenza (data inizio = null) per conoscere l'effettivo
	 * quantum residuo ai fini dell'applicazione dei benefici. n.b. il metodo non entra nel merito se la pena
	 * sia o meno in decorrenza, ma effettua una semplice sottrazione sui quantum
	 * 
	 * @return CalendarModel contenente i quantum di pena residua totale (reclusione+arresti)
	 * @throws Exception
	 */
	public CalendarModel getPenaVirtuale(PenaResiduaModel aPenaResidua) throws Exception {
		CalendarModel lPenaVirtuale = new CalendarModel();
		PenaResiduaModel lPenaAttuale = null;

		if (aPenaResidua != null) {
			lPenaAttuale = aPenaResidua;
		} else if (this.mPenaResiduaRicalcolata != null) {
			lPenaAttuale = this.mPenaResiduaRicalcolata;
		} else {
			throw new Exception("Impossibile determinare la pena virtuale, pena residua non specificata");
		}

		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lCalReclusione = lPenaAttuale.getQuantumReclusione();
		CalendarModel lCalArresto = lPenaAttuale.getQuantumArresto();

		// Recupero le LA e le tratto come quantum caricandole in un CalendarModel
		CalendarModel lCalLA = new CalendarModel();

		// Anna per scomputi
		// lCalLA.setNumGiorni(this.getLiberazioneAnticipata());
		lCalLA.setNumGiorni(this.getLiberazioneAnticipata() + this.getTotaleScomputi());

		lCalLA = lCalUtil.ricalcolaGAM(lCalLA);

		// ==========================================================================
		// Sottraggo le LA prima dalla reclusione, quindi dall'arresto. Infatti
		// le LA anticipando sia il fine pena che il fine reclusione di fatto vengono
		// scalate prima dalla reclusione e successivamente dall'arresto (vedi
		// sospensione)
		// ==========================================================================
		lCalReclusione = lCalUtil.sottraiGiorniValuteNew(lCalReclusione, lCalLA);
		if (!lCalUtil.isPositiveTime(lCalReclusione)) {
			CalendarModel lCalModApp = new CalendarModel();
			lCalModApp = lCalUtil.abs(lCalReclusione);
			lCalModApp = lCalUtil.sottraiGiorniNew(lCalArresto, lCalModApp);

			// Aggiorno i quantum di Arresto
			lCalArresto.setNumAnni(lCalModApp.getNumAnni());
			lCalArresto.setNumMesi(lCalModApp.getNumMesi());
			lCalArresto.setNumGiorni(lCalModApp.getNumGiorni());

			// Azzero i quantum di reclusione
			lCalReclusione.setNumAnni(0);
			lCalReclusione.setNumMesi(0);
			lCalReclusione.setNumGiorni(0);
		}

		// ==========================================================================
		// Sottraggo i giorni di LA dal Totale
		// ==========================================================================
		// CalendarModel lTotDaEspiare = lCalUtil.sommaGiorni(lCalReclusione, lCalArresto);
		lPenaVirtuale = lCalUtil.sommaGiorni(lCalReclusione, lCalArresto);
		// lPenaVirtuale = lCalUtil.sottraiGiorniNew(lTotDaEspiare, lCalLA);

		return lPenaVirtuale;
	}

	/**
	 * Verifica se la pena rideterminata coincide con le pena iniziale. Questo è vero se non esistono dati da
	 * computare oltre a quelli delle 'pena iniziale'. n.b. non vengono prese in considerazione eventuale LA
	 * inserite dopo la pena iniziale. Il controllo viene quindi effettuato SOLO sui quantum, non sulle date
	 * di decorrenza
	 * 
	 * @return
	 */
	public boolean isPenaIniziale() {
		boolean lIsPenaIniziale = true;
		// Verificare se vero sempre!!
		if (mDepenalizzazioneR.size() > 0 || mIncostituzionalitaR.size() > 0 || mAmnistiaR.size() > 0
				|| mIndultoR.size() > 0 || mDepenalizzazione.size() > 0 || mIncostituzionalita.size() > 0
				|| mAmnistia.size() > 0 || mIndulto.size() > 0 || mPresoffertoAltroReato.size() > 0
				|| mFungibilitaAltroReatoMC.size() > 0 || mFungibilitaAltroReatoPD.size() > 0
				|| mComputoAltro.size() > 0) {
			lIsPenaIniziale = false;
		}

		return lIsPenaIniziale;
	}

	public String toString() {
		String lStr = new String();

		lStr = "CalcoloPenaModel:\n" + "[ mTipoPenaIniziale = " + mTipoPenaIniziale + " ]\n"
				+ "[ mDataDal          = " + mDataDal + " ]\n" + "[ mDataAl           = " + mDataAl + " ]\n";
		if (mEventoPenaIniziale != null && mEventoPenaIniziale.getIdEvento() != null) {
			lStr += "[ mIdEvento                   = " + mEventoPenaIniziale.getIdEvento() + " ]\n"
					+ "[ mCodTipoEvento              = " + mEventoPenaIniziale.getCodTipoEvento() + " ]\n"
					+ "[ mCodTipoProvvedimento       = " + mEventoPenaIniziale.getCodTipoProvvedimento()
					+ " ]\n" + "[ mCodMotivo                  = " + mEventoPenaIniziale.getCodMotivo()
					+ " ]\n";
		}

		return lStr;
	}

}