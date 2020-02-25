package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.calcolopena.controller.ICalcoloPenaF5;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * Classe Action che espone i metodi centralizzati per recuperare i dati che concorrono la calcolo della Pena
 * 
 * @author
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCalcoloPenaMain extends ActionSiap implements ICostantiAnnotazioneManuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * ************************************************************************** Questo metodo pilota il
	 * recupero di tutti i dati che concorrono al calcolo della pena per un certo fascicolo in un certo
	 * istante. L'istante viene specificato fornendo l'idEvento. Se viene specificato questo parametro
	 * verranno recuperati i dati utilizzati/utilizabili al momento dell'inserimento dell'evento. Se non viene
	 * fornito l'idEvento vengono recuperati tutti i dati che concorrono al calcolo della pena al momento
	 * della chiamata del metodo. n.b. I dati recuperati sono solo ed esclusivamente legati a eventi VALIDATI
	 * non ANNULLATI non tengono quindi conto di eventuali dati transitori. E' compito del metodo chiamante
	 * aggiungere i dati di sua competenza.
	 * 
	 * I dati vengono recuperati sono: - Pena Iniziale - Pena in sentenza - Pena irrogata in cumulo - Pena
	 * dopo sospensione/interruzione/differimento - Pena dopo revoca MA - Pena dopo cessazione MA (MEV/MAC
	 * 04/2014) - Pena dopo revoca indultino - Pena Residua Manuale - Pena da sospensine RES - Pena da
	 * archiviazione RES - Benefici in sentenza - Misure cautelari stesso reato - Sanzioni Sostitutive in
	 * sentenza (new versione 3.0) - Richieste al GE (incostituzionalità, depenalizzazione, amnistia/indulto)
	 * - Decisioni del GE (incostituzionalità, depenalizzazione, amnistia/indulto) - Computi (Presofferto
	 * altro Reato, Fungibilità Altro reato Misure cautelari, Fungibilità Altro reato Pena Detentiva) -
	 * Rideterminazione Pena Altro - Liberazioni Anticipate
	 * 
	 * I dati vengono recuperati in maniera granulare e restituiti nel CalcoloPenaModel che oltre ad contenere
	 * i singoli dati, espone anche i metodi per elaborarli estraendoli in modo aggregato.
	 * 
	 * @param lFascID
	 *            - Id del fascicolo su cui effettuare il calcolo della pena
	 * @param lIdEvento
	 *            - Id dell'evento rispetto al quale effettuare il calcolo della pena. Se non viene
	 *            specificato l'idEvento il calcolo viene fatto sulla situazione corrente del fascicolo.
	 * @return CalcoloPenaModel - Model contenente tutti i dati che concorrono al calcolo della pena per il
	 *         fascicolo e l'evento in input
	 * @throws F3BException
	 ************************************************************************** */
	public CalcoloPenaModel calcoloPena(BigDecimal aFascID, BigDecimal aIdEvento) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("----------------------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("  INIZIO CALCOLO PENA F5 (" + aFascID + "," + aIdEvento + ")");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("----------------------------------------------------------");
		CalcoloPenaModel lCalcoloPenaMod = null;

		// ==========================================================================
		// Recupero la 'Pena Iniziale' punto di partenza per i successivi calcoli
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero la Pena Iniziale");
		lCalcoloPenaMod = getPenaIniziale(aFascID, aIdEvento);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Pena Iniziale = " + lCalcoloPenaMod);

		// ==========================================================================
		// Se la pena di partenza è quella irrogata in sentenza recupero i Benefici
		// concessi in sentenza e le misure cautelari computabili.
		// E le sanzioni sostitutive
		// ==========================================================================
		if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_SENTENZA) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Iniziale = Pena in Sentenza");
			lCalcoloPenaMod.setBeneficiInSentenza(getBeneficiInSentenza(aFascID));
			lCalcoloPenaMod.setMisureCautelariInSentenza(getMisureCautelari(aFascID));
		}

		// ==========================================================================
		// Verifico se presenti SS in sentenza. In questo caso verifico se è già stato
		// effettuato un calcolo della pena per le SS (sono presenti dati nella
		// tabella SANZIONE_SOST_RESIDUA). Infatti il calcolo delle SS è sempre abFine,
		// si parte sempre dall'ultima SS
		// n.b. nel caso di cumulo la SS non può essere recuperato dalla sentenza del
		// cumulante, non ha senso
		// ==========================================================================
		if (lCalcoloPenaMod.getTipoPenaIniziale() != ICostantiCalcoloPena.PENA_IN_CUMULO) {
			PenaComplessivaModel lPenaInSentenza = null;
			if (lCalcoloPenaMod.getPenaInSentenza() != null) {
				lPenaInSentenza = lCalcoloPenaMod.getPenaInSentenza();
			} else {
				IPenaComplessiva lPenaCompCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
				lPenaInSentenza = lPenaCompCtrl.ExRicercaPenaComplessivaByIdFascicolo(aFascID);
			}

			// MEV_39: aggiunto controllo preventivo per gestione errore
			SanzioneSostitutivaModel lSSinSentenza = null;
			if (lPenaInSentenza != null)
				lSSinSentenza = getSanzioneSostitutiva(lPenaInSentenza.getIdPenaComplessiva());
			if (lSSinSentenza != null && lSSinSentenza.getIdSanzioneSostitutiva() != null) {
				lCalcoloPenaMod.setSanzioneSostitutiva(lSSinSentenza);
				SanzioneSostResiduaModel lSSresiduaModel = getUltimaSSResidua(aFascID);
				lCalcoloPenaMod.setUltimaSanzSostResidua(lSSresiduaModel);
			}
		}

		// ==========================================================================
		// Recupero le Richieste al GE
		// ==========================================================================
		Vector lRichiesteAlGE = getRichiesteAlGE(aFascID, lCalcoloPenaMod.getDataDal(),
				lCalcoloPenaMod.getDataAl());
		for (int i = 0; i < lRichiesteAlGE.size(); i++) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lRichiesteAlGE.elementAt(i);

			if (lAnnMod.getCodTipoAnnotazione().equals("004") // Depenalizzazione
					// MEV 37 - inizio
					|| lAnnMod.getCodTipoAnnotazione().equals("017")) { // Illecito Amministrativo
				// MEV 37 - Fine
				lCalcoloPenaMod.getDepenalizzazioneR().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("013")) { // Incostituzionalità
				lCalcoloPenaMod.getIncostituzionalitaR().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("003")) { // Amnistia
				lCalcoloPenaMod.getAmnistiaR().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("002")) { // Indulto
				lCalcoloPenaMod.getIndultoR().add(lAnnMod);
			}
		}

		// ==========================================================================
		// Recupero le Decisioni del GE
		// ==========================================================================
		Vector lDecisioniDelGE = getDecisioniDelGE(aFascID, lCalcoloPenaMod.getDataDal(),
				lCalcoloPenaMod.getDataAl());
		for (int i = 0; i < lDecisioniDelGE.size(); i++) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lDecisioniDelGE.elementAt(i);

			if (lAnnMod.getCodTipoAnnotazione().equals("004") // Depenalizzazione
					// MEV 37 - inizio
					|| lAnnMod.getCodTipoAnnotazione().equals("017")) { // Illecito Amministrativo
				// MEV 37 - Fine
				lCalcoloPenaMod.getDepenalizzazione().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("013")) { // Incostituzionalità
				lCalcoloPenaMod.getIncostituzionalita().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("003")) { // Amnistia
				lCalcoloPenaMod.getAmnistia().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("002")) { // Indulto
				lCalcoloPenaMod.getIndulto().add(lAnnMod);
			}
		}

		// ==========================================================================
		// Recupero gli indulti RES (richieste e decisioni)
		// ==========================================================================
		Vector lIndultiRES = getIndultiRES(aFascID, lCalcoloPenaMod.getDataDal(), lCalcoloPenaMod.getDataAl());
		for (int i = 0; i < lIndultiRES.size(); i++) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lIndultiRES.elementAt(i);
			lCalcoloPenaMod.getIndulto().add(lAnnMod);
		}

		// ==========================================================================
		// Recupero i Computi
		// ==========================================================================
		Vector lComputi = getComputi(aFascID, lCalcoloPenaMod.getDataDal(), lCalcoloPenaMod.getDataAl());
		for (int i = 0; i < lComputi.size(); i++) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lComputi.elementAt(i);

			if (lAnnMod.getCodTipoAnnotazione().equals("005")) { // Pena Espiata per lo Stesso Titolo
																	// (Presofferto)
				lCalcoloPenaMod.getPresoffertoAltroReato().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("006")) { // Pena Espiata per Altro Titolo
																		// (Fungibilità) Misura Cautelare
				lCalcoloPenaMod.getFungibilitaAltroReatoMC().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("007")) { // Pena Espiata Senza Titolo
																		// (Fungibilità) Pena Detentiva
				lCalcoloPenaMod.getFungibilitaAltroReatoPD().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("014")) { // Altro
				lCalcoloPenaMod.getComputoAltro().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("-")) { // Computo RES
				lCalcoloPenaMod.getComputoRES().add(lAnnMod);
			} else if (lAnnMod.getCodTipoAnnotazione().equals("015")) { // Computo da Revoca Sanzione
																		// Sosttutiva
				lCalcoloPenaMod.getComputoAltro().add(lAnnMod);
			}
		}

		// ==========================================================================
		// Recupero i giorni di Liberazione Anticipata
		// ==========================================================================
		Vector lLibAnticipate = getLibAnticipate(aFascID, lCalcoloPenaMod.getDataDal(),
				lCalcoloPenaMod.getDataAl());
		lCalcoloPenaMod.setLibAnticipate(lLibAnticipate);

		// ==========================================================================
		// Recupero La Pena Già Espiata (tutti i record fine all'evento corrente)
		// ==========================================================================
		// Vector lPeneEspiate = getPeneGiaEspiate(aFascID, lCalcoloPenaMod.getDataDal(),
		// lCalcoloPenaMod.getDataAl());
		Vector lPeneEspiate = getPeneGiaEspiate(aFascID, null, lCalcoloPenaMod.getDataAl());
		lCalcoloPenaMod.setPeneGiaEspiate(lPeneEspiate);

		// ==========================================================================
		// Recupero La Pena Espiata in Eccesso (tutti i record fungibilità fine all'evento corrente)
		// ==========================================================================
		Vector lPeneEspiateInEccesso = getPeneEspiateInEccesso(aFascID, null, lCalcoloPenaMod.getDataAl());
		lCalcoloPenaMod.setPeneEspiateInEccesso(lPeneEspiateInEccesso);

		return lCalcoloPenaMod;
	}

	/**
	 * ************************************************************************** Verifica quale deve essere
	 * il punto di partenza per il calcolo della pena (Pena Inizale). Tale punto varia in funzione dello stato
	 * del fascicolo e del momento del calcolo<br>
	 * <br>
	 * Istanzia il CalcoloPenaModel caricandolo con l'opportuna Pena Iniziale e valorizza il campo
	 * mTipoPenaIniziale per indicare alla chiamante quale è la pena inziale.<br>
	 * <br>
	 * Imposta le date di ricerca in funzione della pena iniziale e dell'evento passato in input.<br>
	 * <br>
	 * 
	 * La 'Pene Iniziale' può essere:<br>
	 * - Pena Irrogata in sentenza<br>
	 * - Pena Irrogata in Cumulo<br>
	 * - Pena Residua a seguito di Interruzine/Differimento/Sospensione<br>
	 * - Pena Residua a seguito Revoca MA<br>
	 * - Pena Residua a seguito Revoca Indultino<br>
	 * - Pena Residua Manuale<br>
	 * 
	 * @param aFascID
	 * @param aIdEvento
	 ************************************************************************** */
	public CalcoloPenaModel getPenaIniziale(BigDecimal aFascID, BigDecimal aIdEvento) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> ===========================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupero la Pena INIZIALE");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> ===========================");

		CalcoloPenaModel lCalcoloPenaMod = new CalcoloPenaModel();

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lCalcoloPenaMod = lCalcPenaF5.exGetPenaIniziale(aFascID, aIdEvento);

		return lCalcoloPenaMod;
	}

	/**
	 * ************************************************************************** Recupero i benefici
	 * concessi/revocati in sentenza. Tali benefici non sono legati ad alcun evento.
	 * 
	 * @param lFascID
	 * @return vettore di BeneficioModel contenente sia i benefici concessi (C) sia i benefici revocati (R).
	 ************************************************************************** */
	public Vector getBeneficiInSentenza(BigDecimal aFascID) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> =======================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupero i Benefici in Sentenza: getBeneficiInSentenza");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> =======================================================");
		// IBeneficio lCtrl= SIEPLookupRemote.getBeneficioRemote();

		BeneficioModel lBenMod = new BeneficioModel();
		lBenMod.setFasSieIdFascicoloSiep(aFascID);
		// lBenMod.setCodNaturaBeneficio("C");
		Vector lBenefici = null;
		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lBenefici = lCalcPenaF5.exGetBenefici(lBenMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lBenefici = " + lBenefici);
		if (lBenefici != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lBenefici.size() = " + lBenefici.size());
			for (int i = 0; i < lBenefici.size(); i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("i = " + i);
				BeneficioModel benMod = (BeneficioModel) lBenefici.elementAt(i);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("benMod = " + benMod);
				if (benMod.getCodNaturaBeneficio() != null && (benMod.getCodNaturaBeneficio().equals("C")
				// || benMod.getCodNaturaBeneficio().equals("R")
						)) {
					// nulla
				} else {
					// scarto il record
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("scarto il record");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("benMod = " + benMod.getCodNaturaBeneficio());
					lBenefici.remove(i);
					i--;
				}
			}
		} else {
			lBenefici = new Vector();
		}

		return lBenefici;
	}

	/**
	 * ************************************************************************** Recupera le Misure Cautelari
	 * computabili stesso reato:<br>
	 * - Arresti (codTipoMisura = "AD" - "Custodia cautelare in Arresti domiciliari") <br>
	 * - Reclusione (codTipoMisura = "CA" - "Custodia cautelare in carcere (Reclusione)" codTipoMisura = "CB"
	 * - "Custodia Cautelare in Regime di Permanenza in Casa codTipoMisura = "CC" - "Custodia Cautelare in
	 * Collocamento in Comunità codTipoMisura = "CD" - "Custodia Cautelare in Misura di Sicurezza Applicata in
	 * via Provvisoria codTipoMisura = "CE" - "Custodia cautelare in camera di sicurezza codTipoMisura = "CL"
	 * - "Computo periodo messa alla prova codTipoMisura = "CM" - "Custodia Cautelare in Regime di Arresti
	 * Domiciliari ex art 89 dpr 309/90
	 * 
	 * @param lFascID
	 * @return Vector di MisuraCautelareModel
	 ************************************************************************** */
	public Vector getMisureCautelari(BigDecimal aFascID) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> =============================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupero le Misure Cautelari Computabili: getMisureCautelari");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> =============================================================");

		MisuraCautelareModel lMisCau = new MisuraCautelareModel();
		lMisCau.setFasSieIdFascicoloSiep(aFascID);
		lMisCau.setFlagComputabile("S");

		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();

		Vector lMisureComputabili = null;
		try {
			lMisureComputabili = lCtrl.ExRicercaMisuraCautelare(lMisCau);
		} catch (F3BException e) {
			// e.printStackTrace();
		}

		if (lMisureComputabili != null) {
			for (int i = 0; i < lMisureComputabili.size(); i++) {
				MisuraCautelareModel misMod = (MisuraCautelareModel) lMisureComputabili.elementAt(i);
				if (misMod.getCodTipoMisura() != null
						&& (misMod.getCodTipoMisura().equals("AD") // Arresti Domiciliari
								|| misMod.getCodTipoMisura().equals("CA") // Custodia cautelare in carcere
								|| misMod.getCodTipoMisura().equals("CB")
								|| misMod.getCodTipoMisura().equals("CC")
								|| misMod.getCodTipoMisura().equals("CD")
								|| misMod.getCodTipoMisura().equals("CE")
								|| misMod.getCodTipoMisura().equals("CL") || misMod.getCodTipoMisura()
								.equals("CM"))) {
					// nulla
				} else {
					// scarto il record
					lMisureComputabili.remove(i);
					i--;
				}
			}
		} else {
			lMisureComputabili = new Vector();
		}

		return lMisureComputabili;
	}

	/**
	 * Recupera l'eventuale Sanzione Sostitutiva disposta in sentenza
	 * 
	 * @param aPenaComplID
	 *            - Id della pena complessiva (n.b. la SS ha un rif solo alla pena complessiva)
	 * @return
	 * @throws F3BException
	 */
	public SanzioneSostitutivaModel getSanzioneSostitutiva(BigDecimal aPenaComplID) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> ================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupero la Sanzione Sostitutiva in Sentenza");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> ================================================");

		SanzioneSostitutivaModel lSanzione = null;

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lSanzione = lCalcPenaF5.exGetSanzioneSostitutiva(aPenaComplID);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lSanzione = " + lSanzione);

		return lSanzione;
	}

	/**
	 * Recupera l'ultima SS Residua Ricalcolata se presente
	 * 
	 * @param aIdFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public SanzioneSostResiduaModel getUltimaSSResidua(BigDecimal aIdFascicoloSiep) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> ================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupero l'ultima Sanzione Sostitutiva Residua ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> ================================================");

		SanzioneSostResiduaModel lSSResModel = null;

		ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		lSSResModel = lSSCtrl.getUltimaSSResidua(aIdFascicoloSiep, "S"); // Validata

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lSSResModel = " + lSSResModel);

		return lSSResModel;
	}

	/**
	 * ************************************************************************** Recupera le richieste al GE
	 * con anticipazione degli effetti (FLAG_APP_PROVVISORIA = A) iscritte nell'intervallo di date
	 * specificato.
	 * 
	 * n.b. poichè una richiesta verrà prima o poi annullata da una decisione bisogna recuperare SOLO quelle
	 * inserite nel periodo, ma non ancora annullate dalla decisione per cui nell'annullamento deve essere
	 * inserita la data, o comunque deve essere creato un link tra la Decisione del GE e la richiesta. In
	 * questo modo sarebbe almeno possibile recuperare la data della decisione.
	 * 
	 * n.b. i dati servono ai fini del calcolo della pena per cui andrebbero recuperate le Richieste collegate
	 * ad Eventi validati inseriti tra le date specificate. Non può far fede la data di inserimento
	 * dell'annotazione in quanto una annotazione può essere inserita ma validata solo successivamente a
	 * seguito dell'emissione di un provvedimento.
	 * 
	 * n.b. E' possibile inserire delle richieste - depenalizzazione (004) - incostituzionalità (013) -
	 * Amnistia (003) - Indulto (002) - (MEV 37 ) Illecito Amministrativo (017)
	 * 
	 * @param lFascID
	 * @param dataDal
	 * @param dataAl
	 * @return Vettore di AnnotazioneManualeModel
	 ************************************************************************** */
	public Vector getRichiesteAlGE(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> =================================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupero le richieste al GE con anticipazione degli effetti: dal "
				+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss") + " al "
				+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> =================================================================================");

		Vector lRichieste = null;

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lRichieste = lCalcPenaF5.exGetRichiesteAlGE(aFascID, aDataDal, aDataAl);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Richieste trovate: " + lRichieste.size());

		return lRichieste;
	}

	/**
	 * ************************************************************************** Recupera le Decisioni del GE
	 * iscritte nell'intervallo di date specificato.
	 * 
	 * - depenalizzazione (004) - incostituzionalità (013) - Amnistia (003) - Indulto (002) - (MEV 37)
	 * Illecito Amministrativo (017)
	 * 
	 * @param lFascID
	 * @param dataDal
	 * @param dataAl
	 * @return Vettore di AnnotazioneManualeModel
	 ************************************************************************** */
	public Vector getDecisioniDelGE(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupero le Decisioni del GE: dal "
				+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss") + " al "
				+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		Vector lDecisioni = null;

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lDecisioni = lCalcPenaF5.exGetDecisioniDelGE(aFascID, aDataDal, aDataAl);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Decisioni trovate: " + lDecisioni.size());

		return lDecisioni;
	}

	/**
	 * ************************************************************************** Recupera gli Indulti 2006
	 * (richieste e decisioni) migrati RES iscritti nell'intervallo di date specificato.
	 *
	 * n.b. Gli indulti RES sono particolari. Infatti i vari uffici hanno inserito i dati in modo non omogeneo
	 * per cui si è decisi di gestire i dati per quanto possibile nel seguente modo:
	 * 
	 * - se Presente solo richiesta con anticipazione --> Computo i quantum - se Presente solo richiesta senza
	 * anticipazione --> Ignoro i quantum - se Presente solo decisione --> Computo i quantum - se Presenti più
	 * richieste (senza decisione) --> Considero solo quelle con anticipazione a partire da quella inserita
	 * per ultima fino a trovarne una con quantum valorizzati e li computo. - se Presente una richiesta e una
	 * decisione --> Provo a prendere per buona SOLO la decisione ignorando la richiesta. Se i quantum della
	 * decisione sono nulli li recupero dalla richiesta. (caso in cui sono stati computati con anticipazione
	 * nella richiesta e nella decisione non sono stati ribaditi in quanto conforme e già computati) - se
	 * Presente più richieste e una sola decisione --> Provo a prendere per buona SOLO la decisione ignorando
	 * le richieste. Se i quantum della decisione sono nulli li recupero dalla richiesta scelta come al punto
	 * 4.
	 * 
	 * - Indulto (002)
	 * 
	 * 
	 * @param lFascID
	 * @param dataDal
	 * @param dataAl
	 * @return Vettore di AnnotazioneManualeModel conteneti solo i dati utili
	 ************************************************************************** */
	public Vector getIndultiRES(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupero Indulti RES: dal "
				+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss") + " al "
				+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		Vector lAnnotazioniRes = null;

		Vector lAnnotComputabili = new Vector();

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lAnnotazioniRes = lCalcPenaF5.exGetIndultiRES(aFascID, aDataDal, aDataAl);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Indulti trovati: " + lAnnotazioniRes.size());

		// ==========================================================================
		// Scelgo quali annotazioni considerare. Per distinguere le decisioni dalle
		// richieste lavoro (Per ora) sul flag_conforme che nel caso delle richieste
		// è sempre - mentre nelle decisioni è <> -
		// ==========================================================================
		for (int i = 0; i < lAnnotazioniRes.size(); i++) {
			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lAnnotazioniRes.elementAt(i);

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("id: "+lAnnMod.getIdAnnotazioneManuale());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("id: " + lAnnMod.toString2());

			if (CalendarUtil.getTotGiorni(lAnnMod.getQuantumReclusione()) > 0
					|| (lAnnMod.getImportoMulta() != null && lAnnMod.getImportoMulta().compareTo(
							new BigDecimal(0)) != 0)
					|| CalendarUtil.getTotGiorni(lAnnMod.getQuantumArresto()) > 0
					|| (lAnnMod.getImportoAmmenda() != null && lAnnMod.getImportoAmmenda().compareTo(
							new BigDecimal(0)) != 0)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" annotazione valorizzata ");
				lAnnotComputabili.add(lAnnMod);
				break; // per ora esco ne considero solo una
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" annotazione nulla ");
			}

		}
		return lAnnotComputabili;
	}

	/**
	 * ************************************************************************** Recupera i computi iscritti
	 * nell'intervallo di date specificato: - Presofferto Altro Reato (005-Pena Espiata per lo Stesso Titolo)
	 * - Fungibilità altro Reato - Misura Cautelare (006-Pena Espiata per Altro Titolo) - Pena Detentiva
	 * (007-Pena Espiata Senza Titolo) - Altro (014-Altro)
	 * 
	 * @param lFascID
	 * @param dataDal
	 * @param dataAl
	 * @return Vettore di AnnotazioneManualeModel
	 ************************************************************************** */
	public Vector getComputi(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupero i computi iscritti: dal "
				+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss") + " al "
				+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		Vector lComputi = null;

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lComputi = lCalcPenaF5.exGetComputi(aFascID, aDataDal, aDataAl);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Computi trovati: " + lComputi.size());

		return lComputi;
	}

	/**
	 * ************************************************************************** Recupera le LA computabili
	 * nel periodo in questione. n.b. le LA computabili sono solo quelle collegate a un evento SIEP, vale: - a
	 * dire a una Comunicazione (12) nel caso in cui la LA sia stata acquisita per in condannato libero - a un
	 * ordine di scarcerazione per rideterminazione pena, nel caso in cui le LA sono state concesse a un
	 * condannato detenuto, per cui sono state utilizzate per anticipare il fine pena - a un cumulo - a una
	 * pena residua manuale (new) - Liberazione Manuale Anticipata ????
	 * 
	 * @param lFascID
	 * @param dataDal
	 * @param dataAl
	 * @return Vettore di LicenzaLibAnticipataModel
	 ************************************************************************** */
	public Vector getLibAnticipate(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupera le LA computabili: dal "
				+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss") + " al "
				+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");

		// LicenzaLibAnticipataModel
		/*
		 * Attenzione a quelle legate all'evento che rappresenta la Pena Iniziale potrebbero esserci problemi
		 * con le date inerimento/aggiornamento.
		 */
		Vector lLibAnticipate = null;

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lLibAnticipate = lCalcPenaF5.exGetLiberazioneAnticipata(aFascID, aDataDal, aDataAl);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("LA trovate: " + lLibAnticipate.size());

		return lLibAnticipate;
	}

	/**
	 * Ritorna l'elenco delle pene espiate. n.b. per pena espiata si intende un periodo di pena compreso tra
	 * una data inizio e una data fine legata a una interruzione/sospensione/differimento. Non viene computato
	 * un eventuale periodo in corso di espiazione. La pena espiata viene registrata sulla tabella SOSPENSIONE
	 * per cui il vettore ritornato è composto da SospensioneModel
	 * 
	 * @param aFascID
	 * @return vettore di SospensioneModel
	 */
	public Vector getPeneGiaEspiate(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupera le pene già espiate: dal "
				+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss") + " al "
				+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		Vector lListaPeneEspiate = new Vector();

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lListaPeneEspiate = lCalcPenaF5.exGetPeneEspiate(aFascID, aDataDal, aDataAl);

		return lListaPeneEspiate;
	}

	/**
	 * Ritorna l'elenco delle pene espiate in eccesso recuperandole dalla tabella Fungibilità.
	 * 
	 * @param aFascID
	 * @return vettore di SospensioneModel
	 */
	public Vector getPeneEspiateInEccesso(BigDecimal aFascID, Date aDataDal, Date aDataAl)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Recupera le Fungibilità: dal "
				+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss") + " al "
				+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		Vector lListaPeneEspiateInEccesso = new Vector();

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
		lListaPeneEspiateInEccesso = lCalcPenaF5.exGetPeneEspiateInEccesso(aFascID, aDataDal, aDataAl);

		return lListaPeneEspiateInEccesso;
	}

	/**
	 * Ritorna la data inizio pena associata a un certo evento
	 * 
	 * @param aFascID
	 *            obbligatorio
	 * @param aIdEvento
	 *            eventuale id dell'evento su cui si sta chiedendo il calcolo della pena
	 * @return la data inizio o null se pena non in decorenza.
	 * @throws F3BException
	 */
	public Date getDataDecorrenzaPena(BigDecimal aFascID, BigDecimal aIdEvento) throws F3BException {
		Date lDataInizioPena = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> getDataDecorrenzaPena: " + aFascID + " - " + aIdEvento);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug(">>> ==============================================================================");
		// ==========================================================================
		// La data inizio pena è sempre la data inizio dell'ultima pena residua
		// validata.
		// Se l'evento passato in input è validato ed è un evento che ha associata
		// una pena residua deve essere utilizzata la data decorrenza della pena
		// associata.
		// Se l'evento è un evento che non ha associata una pena residua, va recuperata
		// l'ultima pena inserita prima dell'evento.
		// Se l'evento NON E' VALIDATO è come se non sia stato passato in input in
		// quanto si sta chiedendo il calcolo sullo stato attuale del fascicolo.
		// L'evento non validato è sicuramente l'ultimo inserito. In questo caso si
		// fa sempre riferimento all'ultima pena validata.
		// Se NON ESISTE una pena validata si opera come nel caso del primo calcolo
		// della pena.
		// ==========================================================================

		ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();

		// ==========================================================================
		// Recupero l'evento e verifico se è validato, in caso contrario è come se
		// non fosse stato passato in input
		// ==========================================================================
		EventoModel lEveMod = null;
		if (aIdEvento != null) {
			IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
			lEveMod = lEventoCtrl.ExRicercaEventoByKey(aIdEvento);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEveMod = " + lEveMod);

		if (lEveMod != null
				&& (lEveMod.getFlagDocumentoRegistrato() == null || lEveMod.getFlagDocumentoRegistrato()
						.equals("N"))) { // Evento non validato, è come se non esistesse ai fini del calcolo
											// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
											// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Evento non validato");
			lEveMod = null;
		}

		// ==========================================================================
		//
		// ==========================================================================
		boolean isPrimoCalcolo = false;
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		if (lEveMod == null) {
			// Recupero la data inizio pena dell'ultima pena residua validata se esiste
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la data inizio pena dell'ultima pena residua validata se esiste");
			PenaResiduaModel lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(aFascID);

			if (lUltimaPenResVal != null) {
				lDataInizioPena = lUltimaPenResVal.getDataInizio();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lUltimaPenResVal = " + lUltimaPenResVal);
			} else {
				// Come Primo calcolo della pena (OK)
				isPrimoCalcolo = true;
			}
		} else {
			// Evento validato. Verifico se esiste una pena associata
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Evento validato. Verifico se esiste una pena associata");
			PenaResiduaModel lPenResAssociata = lPenResCtrl.ExRicercaPenaResiduaByIdEvento(lEveMod
					.getIdEvento());
			if (lPenResAssociata != null) {
				lDataInizioPena = lPenResAssociata.getDataInizio();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena associata trovata: lPenResAssociata = " + lPenResAssociata);
			} else {
				// Recupero l'ultima pena residua validata prima dell'evento se esiste
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("Evento senza pena associata: Recupero l'ultima pena residua validata prima dell'evento se esiste");
				Vector lListaPeneValidate = lCalcPenaF5.getElencoPeneResidueDataInsDesc(aFascID,
						lEveMod.getDataInserimento());
				if (lListaPeneValidate.size() != 0) {
					lDataInizioPena = ((PenaResiduaModel) lListaPeneValidate.elementAt(0)).getDataInizio();
				} else {
					// Evento validato, ma non esiste una pena residua validata a sistema
					// prima del suo inserimento. Evento inserito prima di un calcolo pena
					// In questo caso devo verificare se esistono altri eventi dopo quello
					// corrente perchè in questo caso non posso utilizzare la routine di
					// che viene utilizzata nel primo calcolo della pena perchè la posizione
					// giuridica potrebbe essere cambiata
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.debug("Evento validato, ma non esiste una pena residua validata a sistema prima del suo inserimento.");
					isPrimoCalcolo = false;
				}
			}
		}

		if (isPrimoCalcolo) {
			// Nel primo calcolo della pena viene recuperata la posizione giuridica
			// corrente e se detenuto questa causa viene utilizzata la data inizio
			// della Misura Cautelare, altrimenti se possibile la data fine pena + 1
			// se detenuto altra causa.
			// Se la chiamata a questa funzione avviene dall'F5 (aIdEvento = null)
			// o comunque quando non è ancora stato effettuato alcun calcolo pena,
			// le cose funzionano correttamente. Se invece si sta cercando di visualizzare
			// la situazione della pena residua a un certo istante (stato esecuzione)
			// non è possibile determinare la situazione della posizione giuridica
			// al tempo dell'evento selezionato per cui non è possibile determinare
			// la data inizio
			try {
				lDataInizioPena = this.getDataPrimoCalcolo(aFascID);
			} catch (F3BException e) {
				// non faccio nulla, non utilizzo l'inizio pena
			}
		}

		return lDataInizioPena;
	}

	/**
	 * Ritorna la data inizio da utilizzare nel primo calcolo della pena n.b. il codice è lo stesso della
	 * siap.siep.calcolopena.action.ActLoadCalcoloPena con la differenza che non rilancia le eccezioni quando
	 * non riesce a determinare la data inizio per posizioni giuridiche non congruenti. In questo caso
	 * restituisce semplicemente null.
	 * 
	 * @param aFascID
	 * @return data inizio se possibile, null altrimenti
	 * @throws F3BException
	 */
	public Date getDataPrimoCalcolo(BigDecimal aFascID) throws F3BException {
		Date lDataInizioPena = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("getDataPrimoCalcolo");

		// Recupero le posizioni giuridiche associate al fascicolo
		IPosizioneGiuridica lPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel PGMod = new PosizioneGiuridicaModel();
		List lList = lPG.ExRicercaPosizioneGiuridicaByIdFascicoloNoError(aFascID);

		boolean detenutoQuestaCausa = false;

		if (lList.size() == 0) { // Manca la posizione giuridica
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Manca la posizione giuridica!!");
			throw new F3BException("Inserire Posizione Giuridica !");
		}

		// ==========================================================================
		//
		// ==========================================================================
		PGMod = (PosizioneGiuridicaModel) lList.get(lList.size() - 1);

		if (PGMod.getCodPosizioneGiuridica().equals("01") // Custodia Cautelare per Questa Causa in Regime di
															// Detenzione (PRIMA)
				|| PGMod.getCodPosizioneGiuridica().equals("02") // Custodia Cautelare per Questa Causa in
																	// Regime di Arresti Domiciliari (PRIMA)
				|| PGMod.getCodPosizioneGiuridica().equals("03") // Espiazione Pena in Regime Carcerario
				|| PGMod.getCodPosizioneGiuridica().equals("04") // Arresti Domiciliari ex art. 656/10
		) {
			detenutoQuestaCausa = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("PosGiuCorr = " + PGMod.getCodPosizioneGiuridica());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("detenutoQuestaCausa = " + detenutoQuestaCausa);

		// // Verifico se detenuto per questa causa (01,02,03,04)
		// for (int i=0;i<lList.size();i++)
		// {
		// PGMod=(PosizioneGiuridicaModel)lList.get(i);
		//
		// if( PGMod.getCodPosizioneGiuridica().equals("01") // Custodia Cautelare per Questa Causa in Regime
		// di Detenzione (PRIMA)
		// || PGMod.getCodPosizioneGiuridica().equals("02") // Custodia Cautelare per Questa Causa in Regime
		// di Arresti Domiciliari (PRIMA)
		// || PGMod.getCodPosizioneGiuridica().equals("03") // Espiazione Pena in Regime Carcerario
		// || PGMod.getCodPosizioneGiuridica().equals("04") // Arresti Domiciliari ex art. 656/10
		// )
		// {
		// detenutoQuestaCausa=true;
		// }
		// if(PGMod.getCodPosizioneGiuridica().equals("07") || PGMod.getCodPosizioneGiuridica().equals("10"))
		// libero=true;
		// }

		// ==========================================================================
		// Cerco di recuperare la data inizio da data inizio misure cautelari,
		// se detenuto per questa causa. Data fine pena + 1g, se detenuto altra
		// causa.
		// ==========================================================================
		if (detenutoQuestaCausa) {
			// Verifica se presenti misure cautelari
			MisuraCautelareModel lMisCau = new MisuraCautelareModel();
			IMisuraCautelare lMC = SIEPLookupRemote.getMisuraCautelareRemote();
			Vector lVect = lMC.ExRicercaMisureCautelariByIdFascicolo(aFascID);

			if (lVect.size() == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Rivedere Misure Cautelari o Posizione Giuridica !");
				throw new F3BException("Rivedere Misure Cautelari o Posizione Giuridica !");
			}

			for (int i = 0; i < lVect.size(); i++) {
				lMisCau = (MisuraCautelareModel) lVect.get(i);
				if (lMisCau.getDataFine() == null) { // è ancora detenuto, utilizzo la data inizio delle
														// misure cautelari
														// come data di partenza per il calcolo della pena
														// residua
					lDataInizioPena = lMisCau.getDataInizio();
				}
			}

			if (lDataInizioPena == null) { // Risulta detenuto per questa causa come posizione giuridica, ma
											// ma la data fine misura cautelare risulta valorizzata, quindi in
											// teoria
											// non dovrebbe essere detenuto
											// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
											// siesLogger al posto di LogF3B.getLogger()
				siesLogger
						.debug("Posizione giuridica inconsistente: det questa causa, ma nessuna misura cautelare in esecuzione");
				throw new F3BException(
						"Impossibile effettuare il calcolo della pena . Rivedere Misure Cautelari !");
				// return null;
			}
		} else { // non detenuto per questa causa, verifico se detenuto per altra causa e
					// in questo caso verifico se è possibile utilizzare la data scadenza
					// del periodo di detenzione per altra causa, come data inizio di espiazione
					// della pena corrente
			FascicoloSiepModel lFascMod = new FascicoloSiepModel();
			IFascicoloSiep lFS = SIEPLookupRemote.getFascicoloSiepRemote();
			lFascMod = lFS.ExRicercaFascicoloByKey(aFascID);

			if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
				AltraCausaModel lAcModel = new AltraCausaModel();
				IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
				lAcModel = lAC.ExRicercaAltraCausaByFascicolo(aFascID);

				if (lAcModel.getDataDecorrenza() != null && lAcModel.getDataScadenza() != null) {
					lDataInizioPena = DateUtils.getDayAfter(lAcModel.getDataScadenza());
				} else
					lDataInizioPena = null;
			} else {
				lDataInizioPena = null;
			}

		}

		return lDataInizioPena;
	}

}