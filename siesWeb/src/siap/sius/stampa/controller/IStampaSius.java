package siap.sius.stampa.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.notifica.model.RicercaNotificheSiusModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.permesso.model.CriteriRicercaProvPermessiLicenzeModel;
import siap.sius.produzioneatti.model.ParereModel;
import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;
import siap.sius.udienza.model.UdienzaModel;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

@SuppressWarnings("rawtypes")
public interface IStampaSius {
	public ByteArrayOutputStream ExPreStampaModelliAttiIstruttori(BigDecimal aIdFascicoloSius, String aCodUff,
			UtenteModel aUtente, String lTemIdTemplate) throws F3BException;

	public ByteArrayOutputStream ExPreStampaVerbaleUdienza(EventoModel aEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExPreStampaFissazioneUdienza(EventoModel aEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExPreStampaAllegato(BigDecimal aIdFascicoloSius,
			DocumentoAllegatoModel aDAMod, String aCodUff, UtenteModel aUtenteModel) throws F3BException;

	// /public TreeModel ExTreeMagistratoRelatore(BigDecimal aIdFascicoloSius)
	// /throws F3BException;

	public TreeModel ExPrelevaDatiVideo(BigDecimal aFasSIUS, int[] aTipoDati) throws F3BException;

	public TreeModel ExPrelevaDatiStampa(BigDecimal aFasSIUS, int[] aTipoDati, String aCodiceUfficio)
			throws F3BException;

	public ByteArrayOutputStream ExPreStampaEmissioneDecreto(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExPreStampaEmissioneOrdinanza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public TreeModel ExAggiungiDatiStampa(BigDecimal aIdFasSius, int[] aTipoDati, TreeModel lTreeDati)
			throws F3BException;

	public ByteArrayOutputStream ExPreStampaAvvocato(BigDecimal aIdAvvocato, BigDecimal aIdFascicoloSius,
			String aCodUff, String lTemIdTemplate, UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExPreStampaImpugnazione(BigDecimal aIdImpugnazione, BigDecimal aIdEvento,
			BigDecimal aIdFascicoloSius, String lTemIdTemplate, String aCodUff, UtenteModel aUtenteModel)
			throws F3BException;

	public ByteArrayOutputStream ExPreStampaProcedimentiDelSoggetto(SoggettoModel lSoggetto,
			Vector lFascicoliGPModel, String aIdDocumento, XModel aStampa, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExPreStampaDocumentoOrdinanza(FascicoloGPModel aFasc,
			EventoNotificaModel aEvento, UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExPreStampaRichiestaAtti(EventoModel aEvento, String aCodUff,
			UtenteModel lUtenteMod) throws F3BException;

	public ByteArrayOutputStream ExPreStampaPareri(ParereModel aParere, Vector aRicerche, UtenteModel aUtente)
			throws F3BException;

	public ByteArrayOutputStream ExPreStampaUdienzeMagistratiProcedimenti(UdienzaModel aUdienza,
			XModel aStampaMod, String aIdTemplate) throws F3BException;

	public TreeModel ExPrelevaDatiEvento(EventoModel aEvento) throws F3BException;

	public ByteArrayOutputStream ExPreStampaNotificheSius(RicercaNotificheSiusModel aFiltroNotifica,
			Vector aListaNotifiche, UtenteModel aUtente) throws F3BException;

	public ByteArrayOutputStream ExPreStampaProcSiusXProv(RicercaOrdinanzaModel aFiltroRicerca,
			Vector aElenco, UtenteModel aUtente) throws F3BException;

	public ByteArrayOutputStream ExPreStampaFoglioComplementare(DocumentoAllegatoModel aDocAll,
			String aCodUff, UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExPreStampaProvvedimentiPermessiLicenza(
			CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca, UtenteModel aUtente) throws F3BException;

	public ByteArrayOutputStream ExPreStampaEmissioneSentenza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExPreStampaDocumentoSentenza(FascicoloGPModel aFasc,
			EventoNotificaModel aEvento, UtenteModel aUtenteModel) throws F3BException;

	// MEV10-s3: aggiunto metodo
	public ByteArrayOutputStream ExPreStampaProcSiusXProv(RicercaProvvedimentoModel mRicercaModel,
			Vector mVect, UtenteModel lUtenteMod) throws F3BException;

	// MEV_65: aggiunto metodo per gestire nuova funzionalita'
	public Vector ExRicercaCopertineFascicoliSius(FascicoloSiusModel fsm, int pagine) throws F3BException;

	// MEV_65: aggiunta action per gestire nuova funzionalita'
	public ByteArrayOutputStream ExStampaCopertineFascicoliSius(FascicoloSiusModel fsm, UtenteModel um,
			int pagina) throws F3BException;

	// MEV_65: aggiunta action per gestire nuova funzionalita'
	public BigDecimal ExContaCopertineFascicoliSius(FascicoloSiusModel fsm) throws F3BException;

}