package siap.siep.util;

import f3b.util.F3BException;
import f3b.util.LookupClass;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.siep.agdgfascicolosiep.controller.IAgdgFascicoloSiep;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altrigradigiudizio.controller.IAltriGradiGiudizio;
import siap.siep.annotazioneesitotrasmissione.controller.IAnnotazioneEsitoTrasmissione;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IIndulto;
import siap.siep.annotazionemanuale.controller.IUltimaAnnotazioneManuale;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.controller.ICalcoloPenaF5;
import siap.siep.certificatostatoesecuzione.controller.ICertificatoStatoEsec;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.continuazione.controller.IContinuazione;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiepCheck;
import siap.siep.fascicolo.controller.IFascicoloSiepStampa;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istruttoria.controller.IIstruttoria;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IStampaCumulo;
import siap.siep.jms.controller.IEsitoRicercaJMS;
import siap.siep.jms.controller.IPresaInCaricoJMS;
import siap.siep.jms.controller.IRicercaJMS;
import siap.siep.jms.controller.ITrasmissioneJMS;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IContinuazioneCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.controller.IMisuraCautelareCumulo;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.controller.IPosizioneGiuridicaCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ISoggettoCumulato;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.motivoevento.controller.IMotivoEvento;
import siap.siep.notifica.controller.INotifica;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzioneAlfano;
import siap.siep.ordinescarcerazione.controller.IOrdineScarcerazione;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.controller.IErroriSiesPagopa;
import siap.siep.pagoPaBatch.controller.IBatchPagopa;
import siap.siep.pagoPaBatch.controller.IInvocazionePagopa;
import siap.siep.parametro.controller.IParametro;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapresunta.controller.IPenaPresunta;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penasospesa.controller.IPenaSospesa;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.controller.IPosizioneGiuridicaLuogoDetenzione;
import siap.siep.posizionemateriale.controller.IPosizioneMateriale;
import siap.siep.posizionematerialefasc.controller.IPosizioneMaterialeFasc;
import siap.siep.provvedimentopm.controller.IProvvedimento;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.reato.controller.IReato;
import siap.siep.refertoscarcerazione.controller.IRefertoScarcerazione;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.riepilogoprovvedimento.controller.IRiepilogoProvvedimento;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.controller.IRinnovoStampa;
import siap.siep.ripristino.controller.IRipristino;
import siap.siep.risultatoricerca.controller.IRisultatoRicerca;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.scarti.controller.IWScarti;
import siap.siep.sedegiudiziaria.controller.ISedeGiudiziaria;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sollecito.controller.ISollecito;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sospensione.controller.IInterruzione;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.stampadocumenti.controller.IStampaDocumenti;
import siap.siep.statistiche.controller.IStatisticheSiep;
import siap.siep.statoprocedimento.controller.IStatoProcedimento;
import siap.siep.storicoavvocato.controller.IStoricoAvvocato;
import siap.siep.tipoeventibdmc.controller.ITipoEventiBdmc;
import siap.siep.tipologiaorario.controller.ITipologiaOrario;
import siap.siep.ulterioresanzionecumulo.controller.IUlterioreSanzioneCumulo;
import siap.siep.verbale.controller.IVerbale;

public class SIEPLookupRemote extends LookupClass {

	public static IAvvocato getAvvocatoRemote() throws F3BException {
		Object lRef;
		IAvvocato lRemote;

		lRef = lookup("siap.siep.avvocato.controller.AvvocatoController");
		lRemote = (IAvvocato) lRef;

		return lRemote;
	}

	public static IFascicoloSiep getFascicoloSiepRemote() throws F3BException {
		Object lRef;
		IFascicoloSiep lRemote;

		lRef = lookup("siap.siep.fascicolo.controller.FascicoloSiepController");
		lRemote = (IFascicoloSiep) lRef;

		return lRemote;
	}

	public static ISentenza getSentenzaRemote() throws F3BException {
		Object lRef;
		ISentenza lRemote;

		lRef = lookup("siap.siep.sentenza.controller.SentenzaController");
		lRemote = (ISentenza) lRef;

		return lRemote;
	}

	public static IPosizioneGiuridica getPosizioneGiuridicaRemote() throws F3BException {
		Object lRef;
		IPosizioneGiuridica lRemote;

		lRef = lookup("siap.siep.posizione.controller.PosizioneGiuridicaController");
		lRemote = (IPosizioneGiuridica) lRef;

		return lRemote;
	}

	public static IReato getReatoRemote() throws F3BException {
		Object lRef;
		IReato lRemote;

		lRef = lookup("siap.siep.reato.controller.ReatoController");
		lRemote = (IReato) lRef;

		return lRemote;
	}

	public static IRichiesta getRichiestaRemote() throws F3BException {
		Object lRef;
		IRichiesta lRemote;

		lRef = lookup("siap.siep.richiesta.controller.RichiestaController");
		lRemote = (IRichiesta) lRef;

		return lRemote;
	}

	public static IProvvedimento getProvvedimentoRemote() throws F3BException {
		Object lRef;
		IProvvedimento lRemote;

		lRef = lookup("siap.siep.provvedimentopm.controller.ProvvedimentoController");
		lRemote = (IProvvedimento) lRef;

		return lRemote;
	}

	public static IBeneficio getBeneficioRemote() throws F3BException {
		Object lRef;
		IBeneficio lRemote;

		lRef = lookup("siap.siep.beneficio.controller.BeneficioController");
		lRemote = (IBeneficio) lRef;

		return lRemote;
	}

	public static IPenaAccessoria getPenaAccessoriaRemote() throws F3BException {
		Object lRef;
		IPenaAccessoria lRemote;

		lRef = lookup("siap.siep.penaaccessoria.controller.PenaAccessoriaController");
		lRemote = (IPenaAccessoria) lRef;

		return lRemote;
	}

	public static ISentenzaRiunita getSentenzaRiunitaRemote() throws F3BException {
		Object lRef;
		ISentenzaRiunita lRemote;

		lRef = lookup("siap.siep.sentenzariunita.controller.SentenzaRiunitaController");
		lRemote = (ISentenzaRiunita) lRef;

		return lRemote;
	}

	public static IMisuraCautelare getMisuraCautelareRemote() throws F3BException {
		Object lRef;
		IMisuraCautelare lRemote;

		lRef = lookup("siap.siep.misuracautelare.controller.MisuraCautelareController");
		lRemote = (IMisuraCautelare) lRef;

		return lRemote;
	}

	/*
	 * public static ICircostanzaReato getCircostanzaReatoRemote() throws F3BException { Object lRef;
	 * ICircostanzaReato lRemote;
	 *
	 * lRef = lookup("siap.siep.circostanzareato.controller.CircostanzaReatoController"); lRemote =
	 * (ICircostanzaReato)lRef;
	 *
	 * return lRemote; }
	 */

	public static IPenaComplessiva getPenaComplessivaRemote() throws F3BException {
		Object lRef;
		IPenaComplessiva lRemote;

		lRef = lookup("siap.siep.penacomplessiva.controller.PenaComplessivaController");
		lRemote = (IPenaComplessiva) lRef;

		return lRemote;
	}

	public static IIstanza getIstanzaRemote() throws F3BException {
		Object lRef;
		IIstanza lRemote;

		lRef = lookup("siap.siep.istanza.controller.IstanzaController");
		lRemote = (IIstanza) lRef;

		return lRemote;
	}

	public static IIstruttoria getIstruttoriaRemote() throws F3BException {
		Object lRef;
		IIstruttoria lRemote;

		lRef = lookup("siap.siep.istruttoria.controller.IstruttoriaController");
		lRemote = (IIstruttoria) lRef;

		return lRemote;
	}

	public static INotifica getNotificaRemote() throws F3BException {
		Object lRef;
		INotifica lRemote;

		lRef = lookup("siap.siep.notifica.controller.NotificaController");
		lRemote = (INotifica) lRef;

		return lRemote;
	}

	public static ICircostanza getCircostanzaRemote() throws F3BException {
		Object lRef;
		ICircostanza lRemote;

		lRef = lookup("siap.siep.circostanza.controller.CircostanzaController");
		lRemote = (ICircostanza) lRef;

		return lRemote;
	}

	public static ILuogoDetenzione getLuogoDetenzioneRemote() throws F3BException {
		Object lRef;
		ILuogoDetenzione lRemote;

		lRef = lookup("siap.siep.luogodetenzione.controller.LuogoDetenzioneController");
		lRemote = (ILuogoDetenzione) lRef;

		return lRemote;
	}

	public static IMisuraSicurezza getMisuraSicurezzaRemote() throws F3BException {
		Object lRef;
		IMisuraSicurezza lRemote;
		lRef = lookup("siap.siep.misurasicurezza.controller.MisuraSicurezzaController");
		lRemote = (IMisuraSicurezza) lRef;
		return lRemote;
	}

	public static IScadenzario getScadenzarioRemote() throws F3BException {
		Object lRef;
		IScadenzario lRemote;
		lRef = lookup("siap.siep.scadenzario.controller.ScadenzarioController");
		lRemote = (IScadenzario) lRef;
		return lRemote;
	}

	public static IPenaResidua getPenaResiduaRemote() throws F3BException {
		Object lRef;
		IPenaResidua lRemote;
		lRef = lookup("siap.siep.penaresidua.controller.PenaResiduaController");
		lRemote = (IPenaResidua) lRef;
		return lRemote;
	}

	public static ISedeGiudiziaria getSedeGiudiziariaRemote() throws F3BException {
		Object lRef;
		ISedeGiudiziaria lRemote;
		lRef = lookup("siap.siep.sedegiudiziaria.controller.SedeGiudiziariaController");
		lRemote = (ISedeGiudiziaria) lRef;
		return lRemote;
	}

	public static IOrdineEsecuzione getOrdineEsecuzioneRemote() throws F3BException {
		Object lRef;
		IOrdineEsecuzione lRemote;
		lRef = lookup("siap.siep.ordineesecuzione.controller.OrdineEsecuzioneController");
		lRemote = (IOrdineEsecuzione) lRef;
		return lRemote;
	}

	public static IContinuazione getContinuazioneRemote() throws F3BException {
		Object lRef;
		IContinuazione lRemote;
		lRef = lookup("siap.siep.continuazione.controller.ContinuazioneController");
		lRemote = (IContinuazione) lRef;
		return lRemote;
	}

	public static IPenaPresunta getPenaPresuntaRemote() throws F3BException {
		Object lRef;
		IPenaPresunta lRemote;
		lRef = lookup("siap.siep.penapresunta.controller.PenaPresuntaController");
		lRemote = (IPenaPresunta) lRef;
		return lRemote;
	}

	public static IParametro getParametroRemote() throws F3BException {
		Object lRef;
		IParametro lRemote;
		lRef = lookup("siap.siep.parametro.controller.ParametroController");
		lRemote = (IParametro) lRef;
		return lRemote;
	}

	public static ICalcoloPena getCalcoloPenaRemote() throws F3BException {
		Object lRef;
		ICalcoloPena lRemote;
		lRef = lookup("siap.siep.calcolopena.controller.CalcoloPenaController");
		lRemote = (ICalcoloPena) lRef;
		return lRemote;
	}

	public static IVerbale getVerbaleRemote() throws F3BException {
		Object lRef;
		IVerbale lRemote;
		lRef = lookup("siap.siep.verbale.controller.VerbaleController");
		lRemote = (IVerbale) lRef;
		return lRemote;
	}

	public static IAltraCausa getAltraCausa() throws F3BException {
		Object lRef;
		IAltraCausa lRemote;
		lRef = lookup("siap.siep.altracausa.controller.AltraCausaController");
		lRemote = (IAltraCausa) lRef;
		return lRemote;
	}

	public static IStatoProcedimento getStatoProcedimentoRemote() throws F3BException {
		Object lRef;
		IStatoProcedimento lRemote;
		lRef = lookup("siap.siep.statoprocedimento.controller.StatoProcedimentoController");
		lRemote = (IStatoProcedimento) lRef;
		return lRemote;
	}

	public static IAnnotazioneManuale getAnnotazioneManualeRemote() throws F3BException {
		Object lRef;
		IAnnotazioneManuale lRemote;
		lRef = lookup("siap.siep.annotazionemanuale.controller.AnnotazioneManualeController");
		lRemote = (IAnnotazioneManuale) lRef;
		return lRemote;
	}

	public static IPresaInCaricoJMS getPresaInCarico() throws F3BException {
		Object lRef;
		IPresaInCaricoJMS lRemote;
		lRef = lookup("siap.siep.jms.controller.PresaInCaricoJMSController");
		lRemote = (IPresaInCaricoJMS) lRef;
		return lRemote;
	}

	public static ITrasmissioneJMS getTrasmissioneJMS() throws F3BException {
		Object lRef;
		ITrasmissioneJMS lRemote;
		lRef = lookup("siap.siep.jms.controller.TrasmissioneJMSController");
		lRemote = (ITrasmissioneJMS) lRef;
		return lRemote;
	}

	public static IFungibilita getFungibilitaRemote() throws F3BException {
		Object lRef;
		IFungibilita lRemote;

		lRef = lookup("siap.siep.fungibilita.controller.FungibilitaController");
		lRemote = (IFungibilita) lRef;

		return lRemote;
	}

	public static IIstitutoDetenzione getIstitutoDetenzioneRemote() throws F3BException {
		Object lRef;
		IIstitutoDetenzione lRemote;
		lRef = lookup("siap.siep.istitutodetenzione.controller.IstitutoDetenzioneController");
		lRemote = (IIstitutoDetenzione) lRef;
		return lRemote;
	}

	public static IRicercaJMS getRicercaJMS() throws F3BException {
		Object lRef;
		IRicercaJMS lRemote;
		lRef = lookup("siap.siep.jms.controller.RicercaJMSController");
		lRemote = (IRicercaJMS) lRef;
		return lRemote;
	}

	public static ICumulo getCumuloRemote() throws F3BException {
		Object lRef;
		ICumulo lRemote;
		lRef = lookup("siap.siep.cumulo.controller.CumuloController");
		lRemote = (ICumulo) lRef;
		return lRemote;
	}

	public static IUltimaAnnotazioneManuale getUltimaAnnotazioneManuale() throws F3BException {
		Object lRef;
		IUltimaAnnotazioneManuale lRemote;
		lRef = lookup("siap.siep.annotazionemanuale.controller.UltimaAnnotazioneManualeController");
		lRemote = (IUltimaAnnotazioneManuale) lRef;
		return lRemote;
	}

	public static IOrdineScarcerazione getOrdineScarcerazione() throws F3BException {
		Object lRef;

		IOrdineScarcerazione lRemote;
		lRef = lookup("siap.siep.ordinescarcerazione.controller.OrdineScarcerazioneController");
		lRemote = (IOrdineScarcerazione) lRef;
		return lRemote;
	}

	public static IEsitoRicercaJMS getEsitoRicercaJMS() throws F3BException {
		Object lRef;

		IEsitoRicercaJMS lRemote;
		lRef = lookup("siap.siep.jms.controller.EsitoRicercaJMSController");
		lRemote = (IEsitoRicercaJMS) lRef;
		return lRemote;
	}

	public static IRefertoScarcerazione getRefertoScarcerazioneRemote() throws F3BException {
		Object lRef;

		IRefertoScarcerazione lRemote;
		lRef = lookup("siap.siep.refertoscarcerazione.controller.RefertoScarcerazioneController");
		lRemote = (IRefertoScarcerazione) lRef;
		return lRemote;
	}

	public static ISospensione getSospensioneRemote() throws F3BException {
		Object lRef;

		ISospensione lRemote;
		lRef = lookup("siap.siep.sospensione.controller.SospensioneController");
		lRemote = (ISospensione) lRef;

		return lRemote;
	}

	public static IDecretoOrdinanzaSiep getDecretoOrdinanzaSiepRemote() throws F3BException {
		Object lRef;

		IDecretoOrdinanzaSiep lRemote;
		lRef = lookup("siap.siep.decretoordinanza.controller.DecretoOrdinanzaSiepController");
		lRemote = (IDecretoOrdinanzaSiep) lRef;

		return lRemote;
	}

	public static IStoricoAvvocato getStoricoAvvocatoRemote() throws F3BException {
		Object lRef;
		IStoricoAvvocato lRemote;
		lRef = lookup("siap.siep.storicoavvocato.controller.StoricoAvvocatoController");
		lRemote = (IStoricoAvvocato) lRef;
		return lRemote;
	}

	public static ISollecito getSollecitoRemote() throws F3BException {
		Object lRef;
		ISollecito lRemote;
		lRef = lookup("siap.siep.sollecito.controller.SollecitoController");
		lRemote = (ISollecito) lRef;
		return lRemote;
	}

	public static IRinnovo getRinnovoRemote() throws F3BException {
		Object lRef;
		IRinnovo lRemote;
		lRef = lookup("siap.siep.rinnovo.controller.RinnovoController");
		lRemote = (IRinnovo) lRef;
		return lRemote;
	}

	public static IRinnovoStampa getRinnovoStampaRemote() throws F3BException {
		Object lRef;
		IRinnovoStampa lRemote;
		lRef = lookup("siap.siep.rinnovo.controller.RinnovoStampaController");
		lRemote = (IRinnovoStampa) lRef;
		return lRemote;
	}

	public static IPosizioneGiuridicaLuogoDetenzione getPosizioneGiuridicaLuogoDetenzioneRemote()
			throws F3BException {
		Object lRef;
		IPosizioneGiuridicaLuogoDetenzione lRemote;
		lRef = lookup("siap.siep.posizione.controller.PosizioneGiuridicaLuogoDetenzioneController");
		lRemote = (IPosizioneGiuridicaLuogoDetenzione) lRef;
		return lRemote;
	}

	public static IUlterioreSanzioneCumulo getUlterioreSanzioneCumuloRemote() throws F3BException {
		Object lRef;
		IUlterioreSanzioneCumulo lRemote;
		lRef = lookup("siap.siep.ulterioresanzionecumulo.controller.UlterioreSanzioneCumuloController");
		lRemote = (IUlterioreSanzioneCumulo) lRef;
		return lRemote;
	}

	public static IPenaCumulo getPenaCumuloRemote() throws F3BException {
		Object lRef;
		IPenaCumulo lRemote;
		lRef = lookup("siap.siep.penacumulo.controller.PenaCumuloController");
		lRemote = (IPenaCumulo) lRef;
		return lRemote;
	}

	public static IFascicoloSiepStampa getFascicoloSiepStampaRemote() throws F3BException {
		Object lRef;
		IFascicoloSiepStampa lRemote;

		lRef = lookup("siap.siep.fascicolo.controller.FascicoloSiepStampaController");
		lRemote = (IFascicoloSiepStampa) lRef;

		return lRemote;
	}

	public static IRiepilogoProvvedimento getRiepilogoProvvedimentoRemote() throws F3BException {
		Object lRef;
		IRiepilogoProvvedimento lRemote;

		lRef = lookup("siap.siep.riepilogoprovvedimento.controller.RiepilogoProvvedimentoController");
		lRemote = (IRiepilogoProvvedimento) lRef;

		return lRemote;
	}

	public static IRipristino getRipristinoRemote() throws F3BException {
		Object lRef;
		IRipristino lRemote;

		lRef = lookup("siap.siep.ripristino.controller.RipristinoController");
		lRemote = (IRipristino) lRef;

		return lRemote;
	}

	public static IInterruzione getInterruzioneRemote() throws F3BException {
		Object lRef;
		IInterruzione lRemote;

		lRef = lookup("siap.siep.sospensione.controller.InterruzioneController");
		lRemote = (IInterruzione) lRef;

		return lRemote;
	}

	public static IWScarti getWScartiRemote() throws F3BException {
		Object lRef;
		IWScarti lRemote;

		lRef = lookup("siap.siep.scarti.controller.WScartiController");
		lRemote = (IWScarti) lRef;

		return lRemote;
	}

	public static IArchiviazione getArchiviazioneRemote() throws F3BException {
		Object lRef;
		IArchiviazione lRemote;

		lRef = lookup("siap.siep.archiviazione.controller.ArchiviazioneController");
		lRemote = (IArchiviazione) lRef;

		return lRemote;
	}

	public static IPosizioneMateriale getPosizioneMaterialeRemote() throws F3BException {
		Object lRef;
		IPosizioneMateriale lRemote;

		lRef = lookup("siap.siep.posizionemateriale.controller.PosizioneMaterialeController");
		lRemote = (IPosizioneMateriale) lRef;

		return lRemote;
	}

	public static IPosizioneMaterialeFasc getPosizioneMaterialeFascRemote() throws F3BException {
		Object lRef;
		IPosizioneMaterialeFasc lRemote;
		lRef = lookup("siap.siep.posizionematerialefasc.controller.PosizioneMaterialeFascController");
		lRemote = (IPosizioneMaterialeFasc) lRef;
		return lRemote;
	}

	public static IMotivoEvento getMotivoEventoRemote() throws F3BException {
		Object lRef;
		IMotivoEvento lRemote;
		lRef = lookup("siap.siep.motivoevento.controller.MotivoEventoController");
		lRemote = (IMotivoEvento) lRef;
		return lRemote;
	}

	public static IRisultatoRicerca getRisultatoRicercaRemote() throws F3BException {
		Object lRef;
		IRisultatoRicerca lRemote;
		lRef = lookup("siap.siep.risultatoricerca.controller.RisultatoRicercaController");
		lRemote = (IRisultatoRicerca) lRef;
		return lRemote;
	}

	public static IIndulto getIndultoRemote() throws F3BException {
		Object lRef;
		IIndulto lRemote;
		lRef = lookup("siap.siep.annotazionemanuale.controller.IndultoController");
		lRemote = (IIndulto) lRef;
		return lRemote;
	}

	public static ICalcoloPenaF5 getCalcoloPenaF5() throws F3BException {
		Object lRef;
		ICalcoloPenaF5 lRemote;

		lRef = lookup("siap.siep.calcolopena.controller.CalcoloPenaControllerF5");
		lRemote = (ICalcoloPenaF5) lRef;

		return lRemote;
	}

	public static IFascicoloSiepCheck getFascicoloSiepCheckRemote() throws F3BException {
		Object lRef;
		IFascicoloSiepCheck lRemote;

		lRef = lookup("siap.siep.fascicolo.controller.FascicoloSiepCheckController");
		lRemote = (IFascicoloSiepCheck) lRef;

		return lRemote;
	}

	/*****************************************************************************
	 * Istanzia e restituisce l'interfaccia del controller CertificatoStatoEsecController
	 *
	 * @return Un'istanza dell'interfaccia del controller CertificatoStatoEsecController
	 * @throws F3BException
	 ****************************************************************************/
	public static ICertificatoStatoEsec getCertificatoStatoEsecRemote() throws F3BException {
		Object lRef;
		ICertificatoStatoEsec lRemote;
		lRef = lookup("siap.siep.certificatostatoesecuzione.controller.CertificatoStatoEsecController");
		lRemote = (ICertificatoStatoEsec) lRef;
		return lRemote;
	}

	/*****************************************************************************
	 * Istanzia e restituisce l'interfaccia del controller ScambioSanzioneController
	 *
	 * @return Un'istanza dell'interfaccia del controller ScambioSanzioneController
	 * @throws F3BException
	 *             blablablablablabla
	 ****************************************************************************/
	public static IScambioSanzione getScambioSanzionRemote() throws F3BException {
		Object lRef;
		IScambioSanzione lRemote;
		lRef = lookup("siap.siep.scambiosanzione.controller.ScambioSanzioneController");
		lRemote = (IScambioSanzione) lRef;
		return lRemote;

	}

	public static IMisuraCautelareBdmc getMisuraCautelareBdmcRemote() throws F3BException {
		Object lRef;
		IMisuraCautelareBdmc lRemote;
		lRef = lookup("siap.siep.misuracautelarebdmc.controller.MisuraCautelareBdmcController");
		lRemote = (IMisuraCautelareBdmc) lRef;
		return lRemote;
	}

	/*****************************************************************************
	 * Istanzia e restituisce l'interfaccia del controller IstruttoriaCumuloController
	 *
	 * @return Un'istanza dell'interfaccia del controller IstruttoriaCumuloController
	 * @throws F3BException
	 ****************************************************************************/
	public static IIstruttoriaCumulo getIstruttoriaCumuloRemote() throws F3BException {
		Object lRef;
		IIstruttoriaCumulo lRemote;
		lRef = lookup("siap.siep.istruttoriacumulo.controller.IstruttoriaCumuloController");
		lRemote = (IIstruttoriaCumulo) lRef;
		return lRemote;
	}

	public static ISanzioneSostitutiva getSanzioneSostitutivaRemote() throws F3BException {
		Object lRef;
		ISanzioneSostitutiva lRemote;
		lRef = lookup("siap.siep.sanzionesostitutiva.controller.SanzioneSostitutivaController");
		lRemote = (ISanzioneSostitutiva) lRef;
		return lRemote;
	}

	public static ITipoEventiBdmc getTipoEventiBdmcRemote() throws F3BException {
		Object lRef;
		ITipoEventiBdmc lRemote;
		lRef = lookup("siap.siep.tipoeventibdmc.controller.TipoEventiBdmcController");
		lRemote = (ITipoEventiBdmc) lRef;
		return lRemote;
	}

	public static ITipologiaOrario getTipologiaOrarioRemote() throws F3BException {
		Object lRef;
		ITipologiaOrario lRemote;
		lRef = lookup("siap.siep.tipologiaorario.controller.TipologiaOrarioController");
		lRemote = (ITipologiaOrario) lRef;
		return lRemote;
	}

	/*****************************************************************************
	 * Istanzia e restituisce l'interfaccia del controller RichiestaConversioneController
	 *
	 * @return Un'istanza dell'interfaccia del controller RichiestaConversioneController
	 * @throws F3BException
	 ****************************************************************************/
	public static IRichiestaConversione getRichiestaConversioneRemote() throws F3BException {
		Object lRef;
		IRichiestaConversione lRemote;
		lRef = lookup("siap.siep.penapecuniaria.controller.RichiestaConversioneController");
		lRemote = (IRichiestaConversione) lRef;

		return lRemote;
	}

	public static ICompetenza getCompetenzaRemote() throws F3BException {
		Object lRef;
		ICompetenza lRemote;
		lRef = lookup("siap.siep.competenza.controller.CompetenzaController");
		lRemote = (ICompetenza) lRef;
		return lRemote;
	}

	public static IMisuraSicurezzaCumulo getMisuraSicurezzaCumuloRemote() throws F3BException {
		Object lRef;
		IMisuraSicurezzaCumulo lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.MisuraSicurezzaCumuloController");
		lRemote = (IMisuraSicurezzaCumulo) lRef;
		return lRemote;
	}

	public static IReatoCumulo getReatoCumuloRemote() throws F3BException {
		Object lRef;
		IReatoCumulo lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.ReatoCumuloController");
		lRemote = (IReatoCumulo) lRef;
		return lRemote;
	}

	public static IPenaComplessivaCumulo getPenaComplessivaCumuloRemote() throws F3BException {
		Object lRef;
		IPenaComplessivaCumulo lRemote;

		lRef = lookup("siap.siep.modulocumulo.controller.PenaComplessivaCumuloController");
		lRemote = (IPenaComplessivaCumulo) lRef;

		return lRemote;
	}

	public static IContinuazioneCumulo getContinuazioneCumuloRemote() throws F3BException {
		Object lRef;
		IContinuazioneCumulo lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.ContinuazioneCumuloController");
		lRemote = (IContinuazioneCumulo) lRef;
		return lRemote;
	}

	public static INuovaIstanza getNuovaIstanzaRemote() throws F3BException {
		Object lRef;
		INuovaIstanza lRemote;
		lRef = lookup("siap.siep.nuovaistanza.controller.NuovaIstanzaController");
		lRemote = (INuovaIstanza) lRef;
		return lRemote;
	}

	public static IAltriGradiGiudizio getAltriGradiGiudizioRemote() throws F3BException {
		Object lRef;
		IAltriGradiGiudizio lRemote;
		lRef = lookup("siap.siep.altrigradigiudizio.controller.AltriGradiGiudizioController");
		lRemote = (IAltriGradiGiudizio) lRef;
		return lRemote;
	}

	public static IAgdgFascicoloSiep getAgdgFascicoloSiepRemote() throws F3BException {
		Object lRef;
		IAgdgFascicoloSiep lRemote;
		lRef = lookup("siap.siep.agdgfascicolosiep.controller.AgdgFascicoloSiepController");
		lRemote = (IAgdgFascicoloSiep) lRef;
		return lRemote;
	}

	public static IOrdineEsecuzioneAlfano getOrdineEsecuzioneAlfanoRemote() throws F3BException {
		Object lRef;
		IOrdineEsecuzioneAlfano lRemote;
		lRef = lookup("siap.siep.ordineesecuzione.controller.OrdineEsecuzioneAlfanoController");
		lRemote = (IOrdineEsecuzioneAlfano) lRef;
		return lRemote;
	}

	public static IPenaSospesa getPenaSospesaRemote() throws F3BException {
		Object lRef;
		IPenaSospesa lRemote;
		lRef = lookup("siap.siep.penasospesa.controller.PenaSospesaController");
		lRemote = (IPenaSospesa) lRef;
		return lRemote;
	}

	public static IMisuraAlternativa getMisuraAlternativaRemote() throws F3BException {
		Object lRef;
		IMisuraAlternativa lRemote;
		lRef = lookup("siap.sico.misuraalternativa.controller.MisuraAlternativaController");
		lRemote = (IMisuraAlternativa) lRef;
		return lRemote;
	}

	/*****************************************************************************
	 * Istanzia e restituisce l'interfaccia del controller AnnotazioneEsitoTrasmissioneController
	 *
	 * @return Un'istanza dell'interfaccia del controller AnnotazioneEsitoTrasmissioneController
	 * @throws F3BException
	 ****************************************************************************/
	public static IAnnotazioneEsitoTrasmissione getAnnotazioneEsitoTrasmissioneRemote() throws F3BException {
		Object lRef;
		IAnnotazioneEsitoTrasmissione lRemote;
		lRef = lookup(
				"siap.siep.annotazioneesitotrasmissione.controller.AnnotazioneEsitoTrasmissioneController");
		lRemote = (IAnnotazioneEsitoTrasmissione) lRef;
		return lRemote;
	}

	public static ISollecitoEsitoTrasmissione getSollecitoEsitoTrasmissioneRemote() throws F3BException {
		Object lRef;
		ISollecitoEsitoTrasmissione lRemote;
		lRef = lookup("siap.siep.sollecitoesitotrasmissione.controller.SollecitoEsitoTrasmissioneController");
		lRemote = (ISollecitoEsitoTrasmissione) lRef;
		return lRemote;
	}

	public static IStatisticheSiep getStatisticheSiepRemote() throws F3BException {
		Object lRef;
		IStatisticheSiep lRemote;
		lRef = lookup("siap.siep.statistiche.controller.StatisticheSiepController");
		lRemote = (IStatisticheSiep) lRef;
		return lRemote;
	}

	public static IStampaDocumenti getStampaDocumentiRemote() throws F3BException {
		Object lRef;
		IStampaDocumenti lRemote;
		lRef = lookup("siap.siep.stampadocumenti.controller.StampaDocumentiController");
		lRemote = (IStampaDocumenti) lRef;
		return lRemote;
	}

	public static ITitoloCumulato getTitoloCumulatoRemote() throws F3BException {
		Object lRef;
		ITitoloCumulato lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.TitoloCumulatoController");
		lRemote = (ITitoloCumulato) lRef;
		return lRemote;
	}

	public static IModuloCumulo getModuloCumuloRemote() throws F3BException {
		Object lRef;
		IModuloCumulo lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.ModuloCumuloController");
		lRemote = (IModuloCumulo) lRef;
		return lRemote;
	}

	public static ISoggettoCumulato getSoggettoCumuloRemote() throws F3BException {
		Object lRef;
		ISoggettoCumulato lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.SoggettoCumulatoController");
		lRemote = (ISoggettoCumulato) lRef;
		return lRemote;
	}

	public static IPenaAccessoriaCumulo getPenaAccessoriaCumuloRemote() throws F3BException {
		Object lRef;
		IPenaAccessoriaCumulo lRemote;

		lRef = lookup("siap.siep.modulocumulo.controller.PenaAccessoriaCumuloController");
		lRemote = (IPenaAccessoriaCumulo) lRef;

		return lRemote;
	}

	public static IDatiFinaliCumulo getDatiFinaliCumuloRemote() throws F3BException {
		Object lRef;
		IDatiFinaliCumulo lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.DatiFinaliCumuloController");
		lRemote = (IDatiFinaliCumulo) lRef;
		return lRemote;
	}

	public static IMisuraCautelareCumulo getMisuraCautelareCumuloRemote() throws F3BException {
		Object lRef;
		IMisuraCautelareCumulo lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.MisuraCautelareCumuloController");
		lRemote = (IMisuraCautelareCumulo) lRef;
		return lRemote;
	}

	public static IBeneficioCumulo getBeneficioCumuloRemote() throws F3BException {
		Object lRef;
		IBeneficioCumulo lRemote;

		lRef = lookup("siap.siep.modulocumulo.controller.BeneficioCumuloController");
		lRemote = (IBeneficioCumulo) lRef;

		return lRemote;
	}

	public static IComputiCumulo getComputiCumuloRemote() throws F3BException {
		Object lRef;
		IComputiCumulo lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.ComputiCumuloController");
		lRemote = (IComputiCumulo) lRef;
		return lRemote;
	}

	public static IStampaCumulo getStampaCumuloRemote() throws F3BException {
		Object lRef;
		IStampaCumulo lRemote;

		lRef = lookup("siap.siep.istruttoriacumulo.controller.StampaCumuloController");
		lRemote = (IStampaCumulo) lRef;

		return lRemote;
	}

	public static IPosizioneGiuridicaCumulo getPosizioneGiuridicaCumuloRemote() throws F3BException {
		Object lRef;
		IPosizioneGiuridicaCumulo lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.PosizioneGiuridicaCumuloController");
		lRemote = (IPosizioneGiuridicaCumulo) lRef;
		return lRemote;
	}

	public static ICircostanzaCumulo getCircostanzaCumuloRemote() throws F3BException {
		Object lRef;
		ICircostanzaCumulo lRemote;

		lRef = lookup("siap.siep.modulocumulo.controller.CircostanzaCumuloController");
		lRemote = (ICircostanzaCumulo) lRef;

		return lRemote;
	}

	public static IStatoEsecTitoloCumulato getStatoEsecTitoloCumulatoRemote() throws F3BException {
		Object lRef;
		IStatoEsecTitoloCumulato lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.StatoEsecTitoloCumulatoController");
		lRemote = (IStatoEsecTitoloCumulato) lRef;
		return lRemote;
	}

	public static IRichiestePmInCumulo getRichiestePmInCumuloRemote() throws F3BException {
		Object lRef;
		IRichiestePmInCumulo lRemote;
		lRef = lookup("siap.siep.modulocumulo.controller.RichiestePmInCumuloController");
		lRemote = (IRichiestePmInCumulo) lRef;
		return lRemote;
	}

	/*
	 * ISSUE MEV : aggiunte interfacce per MEV PagoPA 
	 * Numero MEV : 2023-13 
	 * Autore : sgioggi 
	 * Data : 17 mar 2023
	 * Branch : MEV_2023-13
	 */
	public static ICivilmenteObbligato getCivilmenteObbligatoRemote() throws F3BException {

		Object lRef;
		ICivilmenteObbligato lRemote;
		lRef = lookup("siap.siep.pagoPA.controller.CivilmenteObbligatoController");
		lRemote = (ICivilmenteObbligato) lRef;
		return lRemote;
	}

	public static IRateizzazionePP getRateizzazionePPRemote() throws F3BException {
		Object lRef;
		IRateizzazionePP lRemote;

		lRef = lookup("siap.siep.rateizzazionepp.controller.RateizzazionePPController");
		lRemote = (IRateizzazionePP) lRef;

		return lRemote;
	}

	public static IBollettinoPagopa getBollettinoPagopaRemote() throws F3BException {
		Object lRef;
		IBollettinoPagopa lRemote;

		lRef = lookup("siap.siep.pagoPA.controller.BollettinoPagopaController");
		lRemote = (IBollettinoPagopa) lRef;

		return lRemote;
	}
	
   public static IBatchPagopa getBatchPagopaPagopaRemote() throws F3BException {
        Object lRef;
        IBatchPagopa lRemote;

        lRef = lookup("siap.siep.pagoPaBatch.controller.BatchPagopaController");
        lRemote = (IBatchPagopa) lRef;

        return lRemote;
    }
	// ***** FINE INTERVENTO MEV_2023-13 *****//

   // MEV_2023-33
   public static IInvocazionePagopa getInvocazionePagopaRemote() throws F3BException {
       Object lRef;
       IInvocazionePagopa lRemote;

       lRef = lookup("siap.siep.pagoPaBatch.controller.InvocazionePagopaController");
       lRemote = (IInvocazionePagopa) lRef;

       return lRemote;
   }
   
   public static IErroriSiesPagopa getErroriSiesPagopaRemote() throws F3BException {
     Object lRef;
     IErroriSiesPagopa lRemote;

     lRef = lookup("siap.siep.pagoPA.controller.ErroriSiesPagopaController");
     lRemote = (IErroriSiesPagopa) lRef;

     return lRemote;
 }   
}