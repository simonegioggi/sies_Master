package siap.sius.util;

import it.eng.giustizia.avvocatura.controller.IAvvisiSius;
import it.eng.giustizia.avvocatura.controller.IAvvocaturaSius;
//import siap.sius.presaincarico.controller.IPresaInCarico;  sostituita
import siap.sico.jms.controller.IPresaInCarico;
import siap.sico.note.controller.INote; // 06-09-2005
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.cancassfascsius.controller.ICancAssFascSius;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.curatore.controller.ICuratoreSius;
import siap.sius.decretounificazione.controller.IDecretoUnificazione;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS; // 22/04/2011
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS; // 30/07/2007
//import siap.sius.generaleprocedimento.controller.GeneraleProcedimentoController;
import siap.sius.esperto.controller.IEsperto;
//Inserire gli import in testa a file
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSiusUDS; // 08/08/2007
//import siap.sius.udienza.controller.UdienzaController;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.impugnazione.controller.IImpugnazione;
//import siap.sius.jms.controller.IPresaInCaricoJMS;
import siap.sius.jms.controller.ITrasmissioneJMS;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.motivazionedecreto.controller.IMotivazioneDecreto;
//import siap.sius.esecuzionemisuraalternativa.controller.EsecuzioneMisuraAlternativaController;
import siap.sius.permesso.controller.IEventoPermessoLicenza;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.posizionematerialefascsius.controller.IPosizioneMaterialeFascSius;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.remissionedebito.controller.IRichiestaRemissione;
//import siap.sius.tenore.controller.TenoreController;
import siap.sius.richiestaatti.controller.IRichiestaAtti;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasius.controller.IRiferimentoFascicoloSius;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.stralcio.controller.IStralcio;
//import siap.sius.udienzaprocedimento.controller.UdienzaProcedimentoController;
import siap.sius.tenore.controller.ITenore;
import siap.sius.titoloesecutivo.controller.ITitoloEsecutivo; // 12-08-2005
import siap.sius.trasmissioneatti.controller.ITrasmissioneAtti;
//import siap.sius.fascicolo.controller.FascicoloSiusController;
import siap.sius.udienza.controller.IUdienza;
//import siap.sius.esperto.controller.EspertoController;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.ulterioreistanza.controller.IUlterioreIstanza;
import siap.sius.unificazione.controller.IUnificazione;
import f3b.util.F3BException;
import f3b.util.LookupClass;

public class SIUSLookupRemote extends LookupClass {

	/**
	 * Ritorna l'interfaccia del controller Avvocato.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IAvvocato getAvvocatoRemote() throws F3BException {
		Object lRef;
		IAvvocato lRemote;
		lRef = lookup("siap.sius.avvocato.controller.AvvocatoController");
		lRemote = (IAvvocato) lRef;

		return lRemote;
	}

	/*
	 * public static IEsecuzioneMisuraAlternativa getEsecuzioneMisuraAlternativaRemote() throws F3BException {
	 * Object lRef; IEsecuzioneMisuraAlternativa lRemote; lRef =
	 * lookup("siap.sius.esecuzionemisuraalternativa.controller.EsecuzioneMisuraAlternativaController");
	 * lRemote = (IEsecuzioneMisuraAlternativa)lRef; return lRemote; }
	 */

	/**
	 * Ritorna l'interfaccia del controller FascicoloSius.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IFascicoloSius getFascicoloSiusRemote() throws F3BException {
		Object lRef;
		IFascicoloSius lRemote;
		lRef = lookup("siap.sius.fascicolo.controller.FascicoloSiusController");
		lRemote = (IFascicoloSius) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller Udienza.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IUdienza getUdienzaRemote() throws F3BException {
		Object lRef;
		IUdienza lRemote;
		lRef = lookup("siap.sius.udienza.controller.UdienzaController");
		lRemote = (IUdienza) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller GeneraleProcedimento.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IGeneraleProcedimento getGeneraleProcedimentoRemote() throws F3BException {
		Object lRef;
		IGeneraleProcedimento lRemote;
		lRef = lookup("siap.sius.generaleprocedimento.controller.GeneraleProcedimentoController");
		lRemote = (IGeneraleProcedimento) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller Esperto.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IEsperto getEspertoRemote() throws F3BException {
		Object lRef;
		IEsperto lRemote;
		lRef = lookup("siap.sius.esperto.controller.EspertoController");
		lRemote = (IEsperto) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller UdienzaProcedimento.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IUdienzaProcedimento getUdienzaProcedimentoRemote() throws F3BException {
		Object lRef;
		IUdienzaProcedimento lRemote;
		lRef = lookup("siap.sius.udienzaprocedimento.controller.UdienzaProcedimentoController");
		lRemote = (IUdienzaProcedimento) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller RichiestaAtti.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static ITenore getTenoreRemote() throws F3BException {
		Object lRef;
		ITenore lRemote;
		lRef = lookup("siap.sius.tenore.controller.TenoreController");
		lRemote = (ITenore) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller RichiestaAtti.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IRichiestaAtti getRichiestaAttiRemote() throws F3BException {
		Object lRef;
		IRichiestaAtti lRemote;
		lRef = lookup("siap.sius.richiestaatti.controller.RichiestaAttiController");
		lRemote = (IRichiestaAtti) lRef;
		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller TrasmissioneAtti.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static ITrasmissioneAtti getTrasmissioneAttiRemote() throws F3BException {
		Object lRef;
		ITrasmissioneAtti lRemote;
		lRef = lookup("siap.sius.trasmissioneatti.controller.TrasmissioneAttiController");
		lRemote = (ITrasmissioneAtti) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller DepositoOrdinanza.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IDepositoOrdinanzaPc getDepositoOrdinanzaPcRemote() throws F3BException {
		Object lRef;
		IDepositoOrdinanzaPc lRemote;
		lRef = lookup("siap.sius.depositoordinanzapc.controller.DepositoOrdinanzaPcController");
		lRemote = (IDepositoOrdinanzaPc) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller Prescrizione.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IPrescrizione getPrescrizioneRemote() throws F3BException {
		Object lRef;
		IPrescrizione lRemote;
		lRef = lookup("siap.sius.prescrizione.controller.PrescrizioneController");
		lRemote = (IPrescrizione) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller DepositoDecreto.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IDepositoDecreto getDepositoDecretoRemote() throws F3BException {
		Object lRef;
		IDepositoDecreto lRemote;
		lRef = lookup("siap.sius.depositodecreto.controller.DepositoDecretoController");
		lRemote = (IDepositoDecreto) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller DecretoUnificazione.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IDecretoUnificazione getDecretoUnificazioneRemote() throws F3BException {
		Object lRef;
		IDecretoUnificazione lRemote;
		lRef = lookup("siap.sius.decretounificazione.controller.DecretoUnificazioneController");
		lRemote = (IDecretoUnificazione) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller Scadenzario Sius.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IScadenzarioSius getScadenzarioRemote() throws F3BException {
		Object lRef;
		IScadenzarioSius lRemote;
		lRef = lookup("siap.sius.scadenzario.controller.ScadenzarioSiusController");
		lRemote = (IScadenzarioSius) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller Impugnazione.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IImpugnazione getImpugnazioneRemote() throws F3BException {
		Object lRef;
		IImpugnazione lRemote;
		lRef = lookup("siap.sius.impugnazione.controller.ImpugnazioneController");
		lRemote = (IImpugnazione) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller MagistratoRelatore.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IMagistratoRelatore getMagistratoRelatoreRemote() throws F3BException {
		Object lRef;
		IMagistratoRelatore lRemote;
		lRef = lookup("siap.sius.magistratorelatore.controller.MagistratoRelatoreController");
		lRemote = (IMagistratoRelatore) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller MotivazioneDecreto.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IMotivazioneDecreto getMotivazioneDecretoRemote() throws F3BException {
		Object lRef;
		IMotivazioneDecreto lRemote;
		lRef = lookup("siap.sius.motivazionedecreto.controller.MotivazioneDecretoController");
		lRemote = (IMotivazioneDecreto) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller DocumentoAllegato.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IDocumentoAllegato getDocumentoAllegatoRemote() throws F3BException {
		Object lRef;
		IDocumentoAllegato lRemote;
		lRef = lookup("siap.sius.documentoallegato.controller.DocumentoAllegatoController");
		lRemote = (IDocumentoAllegato) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller Stampa.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IStampaSius getStampaRemote() throws F3BException {
		Object lRef;
		IStampaSius lRemote;
		lRef = lookup("siap.sius.stampa.controller.StampaController");
		lRemote = (IStampaSius) lRef;

		return lRemote;
	}

	public static ITrasmissioneJMS getTrasmissioneJMS() throws F3BException {
		Object lRef;

		ITrasmissioneJMS lRemote;
		lRef = lookup("siap.sius.jms.controller.TrasmissioneJMSController");
		lRemote = (ITrasmissioneJMS) lRef;

		return lRemote;
	}

	// 13/12/2007 Cambiato il riferimento da SIUS.PresaInCaricoJMSController a SICO.PresaInCaricoController.
	/*
	 * public static IPresaInCaricoJMS getPresaInCarico() throws F3BException { Object lRef;
	 * 
	 * IPresaInCaricoJMS lRemote; lRef = lookup("siap.sius.jms.controller.PresaInCaricoJMSController");
	 * lRemote = (IPresaInCaricoJMS)lRef;
	 * 
	 * return lRemote; }
	 */
	/**
	 * Ritorna l'interfaccia dell' Esecuzione Misura Alternativa.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IEsecuzioneMA getEsecuzioneMARemote() throws F3BException {
		Object lRef;
		IEsecuzioneMA lRemote;
		lRef = lookup("siap.sius.esecuzionemisuraalternativa.controller.EsecuzioneMAController");
		lRemote = (IEsecuzioneMA) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia dell Riferimento Fascicolo Sius.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IRiferimentoFascicoloSius getRiferimentoFascicoloSiusRemote() throws F3BException {
		Object lRef;
		IRiferimentoFascicoloSius lRemote;
		lRef = lookup("siap.sius.rifasius.controller.RiferimentoFascicoloSiusController");
		lRemote = (IRiferimentoFascicoloSius) lRef;
		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia dell Riferimento Fascicolo Siep.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IRiferimentoFascicoloSiep getRiferimentoFascicoloSiepRemote() throws F3BException {
		Object lRef;
		IRiferimentoFascicoloSiep lRemote;
		lRef = lookup("siap.sius.rifasiep.controller.RiferimentoFascicoloSiepController");
		lRemote = (IRiferimentoFascicoloSiep) lRef;
		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia di Permesso.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IPermesso getPermessoRemote() throws F3BException {
		Object lRef;
		IPermesso lRemote;
		lRef = lookup("siap.sius.permesso.controller.PermessoController");
		lRemote = (IPermesso) lRef;
		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller Unificazione.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IUnificazione getUnificazioneRemote() throws F3BException {
		Object lRef;
		IUnificazione lRemote;
		lRef = lookup("siap.sius.unificazione.controller.UnificazioneController");
		lRemote = (IUnificazione) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller TitoloEsecutivo.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static ITitoloEsecutivo getTitoloEsecutivoRemote() throws F3BException {
		Object lRef;
		ITitoloEsecutivo lRemote;
		lRef = lookup("siap.sius.titoloesecutivo.controller.TitoloEsecutivoController");
		lRemote = (ITitoloEsecutivo) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller Note.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static INote getNoteRemote() throws F3BException {
		Object lRef;
		INote lRemote;
		lRef = lookup("siap.sico.note.controller.NoteController");
		lRemote = (INote) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller PosizioneMaterialeFascSius.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IPosizioneMaterialeFascSius getPosizioneMaterialeFascSiusRemote() throws F3BException {
		Object lRef;
		IPosizioneMaterialeFascSius lRemote;
		lRef = lookup("siap.sius.posizionematerialefascsius.controller.PosizioneMaterialeFascSiusController");
		lRemote = (IPosizioneMaterialeFascSius) lRef;
		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller PresaInCarico.
	 * <p>
	 * NOTA: questo utilizza il Controller alla Presa in Carico di SIEPE. Superati i test potrà sostituire
	 * getPresaInCarico() ed i due controller saranno unificati.
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IPresaInCarico getPresaInCaricoSIEPE() throws F3BException {
		Object lRef;

		IPresaInCarico lRemote;
		lRef = lookup("siap.sius.presaincarico.controller.PresaInCaricoController");
		lRemote = (IPresaInCarico) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller UlterioreIstanza.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IUlterioreIstanza getUlterioreIstanzaRemote() throws F3BException {
		Object lRef;

		IUlterioreIstanza lRemote;
		lRef = lookup("siap.sius.ulterioreistanza.controller.UlterioreIstanzaController");
		lRemote = (IUlterioreIstanza) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller Stralcio.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IStralcio getStralcioRemote() throws F3BException {
		Object lRef;

		IStralcio lRemote;
		lRef = lookup("siap.sius.stralcio.controller.StralcioController");
		lRemote = (IStralcio) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller della CancelleriaAssegnataria.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static ICancelleriaAssegnataria getCancelleriaAssegnatariaRemote() throws F3BException {
		Object lRef;

		ICancelleriaAssegnataria lRemote;
		lRef = lookup("siap.sius.cancelleriaassegnataria.controller.CancelleriaAssegnatariaController");
		lRemote = (ICancelleriaAssegnataria) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller della CancAssFascSius.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static ICancAssFascSius getCancAssFascSiusRemote() throws F3BException {
		Object lRef;

		ICancAssFascSius lRemote;
		lRef = lookup("siap.sius.cancassfascsius.controller.CancAssFascSiusController");
		lRemote = (ICancAssFascSius) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller della StatisticheSius.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IStatisticheSius getStatisticheSiusRemote() throws F3BException {
		Object lRef;

		IStatisticheSius lRemote;
		lRef = lookup("siap.sius.statistiche.controller.StatisticheSiusController");
		lRemote = (IStatisticheSius) lRef;

		return lRemote;
	}

	/*****************************************************************************
	 * Istanzia e restituisce l'interfaccia del controller PeriodoAltraSanzioneController
	 * 
	 * @return Un'istanza dell'interfaccia del controller PeriodoAltraSanzioneController
	 * @throws F3BException
	 ****************************************************************************/
	public static IPeriodoAltraSanzione getPeriodoAltraSanzioneRemote() throws F3BException {
		Object lRef;
		IPeriodoAltraSanzione lRemote;
		lRef = lookup("siap.sius.sanzionesostitutiva.controller.PeriodoAltraSanzioneController");
		lRemote = (IPeriodoAltraSanzione) lRef;
		return lRemote;
	}

	/*****************************************************************************
	 * Istanzia e restituisce l'interfaccia del controller PeriodoAltraMisuraController
	 * 
	 * @return Un'istanza dell'interfaccia del controller PeriodoAltraMisuraController
	 * @throws F3BException
	 ****************************************************************************/
	public static IPeriodoAltraMisura getPeriodoAltraMisuraRemote() throws F3BException {
		Object lRef;
		IPeriodoAltraMisura lRemote;
		lRef = lookup("siap.sius.misurasicurezza.controller.PeriodoAltraMisuraController");
		lRemote = (IPeriodoAltraMisura) lRef;
		return lRemote;
	}

	/**
	 * STUB 30/07/2007 E.S.S. Ritorna l'interfaccia dell' Esecuzione Sanzione Sostitutiva.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IEsecuzioneSS getEsecuzioneSSRemote() throws F3BException {
		Object lRef;
		IEsecuzioneSS lRemote;
		lRef = lookup("siap.sius.esecuzionesanzionesostitutiva.controller.EsecuzioneSSController");
		lRemote = (IEsecuzioneSS) lRef;
		return lRemote;
	}

	/**
	 * STUB 22/04/2011 E.M.S. Ritorna l'interfaccia dell' Esecuzione Misura Sicurezza.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IEsecuzioneMS getEsecuzioneMSRemote() throws F3BException {
		Object lRef;
		IEsecuzioneMS lRemote;
		lRef = lookup("siap.sius.esecuzionemisurasicurezza.controller.EsecuzioneMSController");
		lRemote = (IEsecuzioneMS) lRef;
		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller FascicoloSius.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IFascicoloSiusUDS getFascicoloSiusUDSRemote() throws F3BException {
		Object lRef;
		IFascicoloSiusUDS lRemote;
		lRef = lookup("siap.sius.fascicolo.controller.FascicoloSiusUDSController");
		lRemote = (IFascicoloSiusUDS) lRef;

		return lRemote;
	}

	public static ICollaboratore getCollaboratoreRemote() throws F3BException {
		Object lRef;
		ICollaboratore lRemote;
		lRef = lookup("siap.sius.collaboratore.controller.CollaboratoreController");
		lRemote = (ICollaboratore) lRef;
		return lRemote;
	}

	/**
	 * Istanzia e restituisce l'interfaccia del controller EventoPermessoLicenza.
	 * <p>
	 * 
	 * @return Un'istanza dell'interfaccia del controller EventoPermessoLicenza
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public static IEventoPermessoLicenza getEventoPermessoLicenzaRemote() throws F3BException {
		Object lRef;
		IEventoPermessoLicenza lRemote;
		lRef = lookup("siap.sius.permesso.controller.EventoPermessoLicenzaController");
		lRemote = (IEventoPermessoLicenza) lRef;

		return lRemote;
	}

	/*****************************************************************************
	 * Istanzia e restituisce l'interfaccia del controller RichiestaRemissioneController
	 * 
	 * @return Un'istanza dell'interfaccia del controller RichiestaRemissioneController
	 * @throws F3BException
	 ****************************************************************************/
	public static IRichiestaRemissione getRichiestaRemissioneRemote() throws F3BException {
		Object lRef;
		IRichiestaRemissione lRemote;
		lRef = lookup("siap.sius.remissionedebito.controller.RichiestaRemissioneController");
		lRemote = (IRichiestaRemissione) lRef;

		return lRemote;
	}

	/*****************************************************************************
	 * Istanzia e restituisce l'interfaccia del controller CuraToreSiusController
	 * 
	 * @return Un'istanza dell'interfaccia del controller CuraToreSiusController
	 * @throws F3BException
	 ****************************************************************************/
	public static ICuratoreSius getCuratoreSiusRemote() throws F3BException {
		Object lRef;
		ICuratoreSius lRemote;
		lRef = lookup("siap.sius.curatore.controller.CuratoreSiusController");
		lRemote = (ICuratoreSius) lRef;

		return lRemote;
	}

	/**
	 * Ritorna l'interfaccia del controller DepositoSentenza.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IDepositoSentenza getDepositoSentenzaRemote() throws F3BException {
		Object lRef;
		IDepositoSentenza lRemote;
		lRef = lookup("siap.sius.depositosentenza.controller.DepositoSentenzaController");
		lRemote = (IDepositoSentenza) lRef;

		return lRemote;
	}

	/**
	 * MEV AVVOCATURA: aggiunta interfaccia
	 * Ritorna l'interfaccia del controller AvvocaturaSiusController.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IAvvocaturaSius getAvvocaturaSiusRemote() throws F3BException {

		Object lRef;
		IAvvocaturaSius ias;
		lRef = lookup("it.eng.giustizia.avvocatura.controller.AvvocaturaSiusController");
		ias = (IAvvocaturaSius) lRef;

		// valore di ritorno
		return ias;
	}
	
	
	/**
	 * MEV AVVOCATURA: aggiunta interfaccia
	 * Ritorna l'interfaccia del controller AvvisiSiusController.
	 * <p>
	 * 
	 * @return l'interfaccia del relativo controller.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static IAvvisiSius getAvvisiSiusRemote() throws F3BException {

		Object lRef;
		IAvvisiSius ias;
		lRef = lookup("it.eng.giustizia.avvocatura.controller.AvvisiSiusController");
		ias = (IAvvisiSius) lRef;

		// valore di ritorno
		return ias;
	}

}