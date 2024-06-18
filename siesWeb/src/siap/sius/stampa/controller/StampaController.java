package siap.sius.stampa.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.util.xml.TreeModel;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.cssa.dao.CSSASqlDAO;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.stampa.controller.SIAPStampaController;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.autoritaesterna.dao.AutoritaEsternaSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepPerEventoSqlDAO;
import siap.siep.avvocato.dao.AvvocatoSiepxStampaSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.beneficio.dao.BeneficioSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.dao.MisuraCautelareSqlDAO;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.notifica.dao.EveNotificaSqlDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaFasSiusEveModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.notifica.model.RicercaNotificheSiusModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaSqlDAO;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.curatore.dao.CuratoreSqlDAO;
import siap.sige.curatore.model.CuratoreModel;
import siap.sius.SIUSException;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.cancassfascsius.controller.ICancAssFascSius;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.curatore.dao.CuratoreSiusDAO;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.UfficioTDSConcessoRiduzioneModel;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.dao.DepositoSentenzaSqlDAO;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esecuzionemisuraalternativa.dao.EsecuzioneMisuraAlternativaSqlDAO;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaSqlDAO;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaSqlDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.esperto.dao.EspertoSqlDAO;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusSqlDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.magistratorelatore.dao.MagistratoRelatoreSqlDAO;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.motivazionedecreto.dao.MotivazioneDecretoSqlDAO;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.permesso.dao.PermessoSqlDAO;
import siap.sius.permesso.model.CriteriRicercaProvPermessiLicenzeModel;
import siap.sius.permesso.model.ProvvedimentoPermessoLicenzaModel;
import siap.sius.permesso.model.TotaliPermessiLicenzeModel;
import siap.sius.prescrizione.dao.PrescrizioneSqlDAO;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.produzioneatti.model.FiltroPareriModel;
import siap.sius.produzioneatti.model.ParereModel;
import siap.sius.provvedimento.action.ICostantiProvvedimento;
import siap.sius.remissionedebito.controller.IRichiestaRemissione;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;
import siap.sius.rifasiep.dao.RiferimentoFascicoloSiepSqlDAO;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneSqlDAO;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.dao.UdienzaSqlDAO;
import siap.sius.udienza.model.UdienzaMagistratoRelModel;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Description: Classe Controller per Stampe SIUS Classe centralizzata, pubblica una serie di metodi che
 * gestiscono il prelievo dei dati per la generazione base delle pagine XML per la gestione delle stampe in
 * ambito SIUS
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StampaController extends SIAPStampaController implements IStampaSius, ICostantiProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Genera il ByteArrayOutputStream per la stampa dell' Emissione Ordinanza.
	 *
	 * @param EventoModel
	 *            Evento Model.
	 * @return ByteArrayOutputStream.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaEmissioneOrdinanza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {

		return ExPreStampaEmissioneOrdinanza(lEvento, aCodUff, aUtenteModel, null);
	}

	/*
	 * Questa funzione con un parametro in più (DocumentoAllegatoModel) rispetto alla versione public è stata
	 * prodotta per gestire il caso della stampa del Foglio Complementare; in questo caso infatti occorre
	 * passare il Documento Allegato. Negli altri casi, ovvero quando questa viene richiamata dalla versione
	 * public il nuovo parametro viene valorizzato a null.
	 */
	private ByteArrayOutputStream ExPreStampaEmissioneOrdinanza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel, DocumentoAllegatoModel aDocAll) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeFasSIEP = null;
		TreeModel lTreeGenProc = null;
		TreeModel lTreeSentenza = null;
		// TreeModel lTreeEsecuzioneMA = null;
		TreeModel lTreeOrdinanza = null;
		// TreeModel dell'eventuale Ordinanza di Riferimento (da revocare)
		TreeModel lTreeOrdinanzaRiferimento = null;
		TreeModel lTreeMisAlt = null;
		TreeModel lTreeMisSic = null;
		// 21/01/2007 Fascicolo SIUS Origine + eventuale EMA.
		TreeModel lTreeFasOri = null;
		TreeModel lTreeEventoNotifiche = null;

		Connection lConn = null;

		FascicoloGPModel lFasGP = null;
		FascicoloGPModel lFasGPMisAlt = null;
		FascicoloGPModel lFasGPMisSic = null;

		// Report generator per la costruzione del report
		ReportGenerator lReport = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaEmissioneOrdinanza : inizio");
		try {
			if (lEvento == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Evento inesistente : ");

			// connessione al Db
			lConn = getDBConnection();

			// ricerca FascicoloSIUSGPmodel
			lFasGP = getFascicoloGPSius(lEvento.getFasSiuIdFascicoloSius(), lConn);

			if (lFasGP == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo inesistente : " + lEvento.getFasSiuIdFascicoloSius());

			// creazione delle varie foglie componenti del documento TreeModel
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));

			// Fascicolo SIUS
			lTreeFasSIUS = prelevaDatiFascicoloSius(lFasGP, lEvento.getIdEvento(), aDocAll, lConn);

			// 23/05/2006 Ricerca idfascicolo sius per misura alternativa solo per S22
			if (lFasGP.getGeneraleProcedimentoModel() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S22") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisAlt = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGP.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGP.getGeneraleProcedimentoModel().getProgrS1(), aCodUff, lConn);
			}

			// Generale Procedimento
			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			// 23/05/2006 Aggregazione lTreeMisAlt.
			if (lFasGPMisAlt != null && lFasGPMisAlt.getFascicoloSiusModel() != null)
				lTreeMisAlt = prelevaDatiEsecuzioneMA(
						lFasGPMisAlt.getFascicoloSiusModel().getIdFascicoloSius(), lConn);

			// 23/05/2006 Ricerca idfascicolo sius per misura alternativa solo per S09
			if (lFasGP.getGeneraleProcedimentoModel() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S09") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisSic = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGP.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGP.getGeneraleProcedimentoModel().getProgrS1(), aCodUff, lConn);
			}

			// Generale Procedimento
			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			// 23/05/2006 Aggregazione lTreeMisSic.
			if (lFasGPMisSic != null && lFasGPMisSic.getFascicoloSiusModel() != null)
				lTreeMisSic = prelevaDatiEsecuzioneMS(
						lFasGPMisSic.getFascicoloSiusModel().getIdFascicoloSius(), lConn);

			// 22/01/2007 Fascicolo SIUS Origine.
			if (lFasGP.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
				// 20191025 [SG]: aggiunto codice per estrarre MS modificata
				boolean test = false;
				if (lFasGP.getGeneraleProcedimentoModel() != null
						&& lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento() != null
						&& "C029".equals(lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento()))
					test = true;
				lTreeFasOri = prelevaDatiFascicoloSiusOrigine(
						lFasGP.getFascicoloSiusModel().getIdFascicoloSiusOrigine(), lConn, test);
			}

			// Ordinanza
			lTreeOrdinanza = prelevaDatiDepositoOrdinanzaPC(lEvento.getIdEvento(), lConn);

			// Nel caso sia valorizzato l'ID_EVE_ID_EVENTO si risale all'Ordinanza relativa
			// Dati di Deposito Ordinanza da Revocare
			if (lTreeOrdinanza != null && lEvento.getEveIdEvento() != null) {
				// Se il Decreto ha un decreto di Riferimento
				lTreeOrdinanzaRiferimento = prelevaDatiDepositoOrdinanzaPC(lEvento.getEveIdEvento(), lConn);
				if (lTreeOrdinanzaRiferimento != null) {
					lTreeOrdinanza.add(lTreeOrdinanzaRiferimento);
					lTreeOrdinanza.add(prelevaDatiFascicoloSiusByIdEvento(lEvento.getEveIdEvento(), lConn));
				}
			}

			// MEV_66: aggiunto calcolo raggiungimento 25° anno di eta'
			if (lFasGP.getGeneraleProcedimentoModel() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento() != null
					&& lFasGP.getFascicoloSiusModel().getSoggetto().getDataNascita() != null
					&& ("U121".equals(lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
							|| "U122"
									.equals(lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
							|| "U123".equals(
									lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento()))) {
				Date venticinqeAnni = DateUtils.moveDateTo(
						lFasGP.getFascicoloSiusModel().getSoggetto().getDataNascita(), Calendar.YEAR, 25);
				if (lTreeFasSIUS != null) {
					int count = lTreeFasSIUS.getChildCount();
					for (int i = 0; i < count; i++) {
						TreeModel lTreeChild = (TreeModel) lTreeFasSIUS.getChildAt(i);
						Object o = lTreeChild.getModel();
						if (o instanceof SoggettoModel) {
							SoggettoModel sm = (SoggettoModel) o;
							sm.setDataNascitaPresuntaCalc(venticinqeAnni);
							break;
						}
					}
				}
			}
			// FINE MEV_66

			lTreeFasSIEP = prelevaDatiFascicoloSiep(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lTreeSentenza = prelevaDatiSentenza(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			// 23/05/2006 lTreeEsecuzioneMA = prelevaDatiEsecuzioneMA(lEvento.getFasSiuIdFascicoloSius(),
			// lConn);

			lTreeEventoNotifiche = prelevaDatiEventoNotifiche(lEvento.getIdEvento(), lConn);

			// Recupera dati Periodo Altra Sanzione attraverso l'evento.
			prelevaDatiPeriodoAltraSanzioneByEvento(lEvento.getIdEvento(), lTreeEventoNotifiche, lConn);

			// COSTRUZIONE DEL DOCUMENTO
			lTreeGenProc.add(lTreeMisAlt);
			lTreeGenProc.add(lTreeMisSic);
			// 23/05/2006 lTreeGenProc.add(lTreeEsecuzioneMA);
			lTreeFasSIUS.add(lTreeOrdinanza);
			// 23/01/2007 Aggiunto il Fascicolo SIUS Origine con indentata l'eventuale EMA ;
			if (lTreeFasOri != null)
				lTreeFasSIUS.add(lTreeFasOri);

			lTreeFasSIUS.add(lTreeEventoNotifiche);

			lRoot.add(lTreeFasSIUS);
			lRoot.add(lTreeGenProc);
			lRoot.add(lTreeFasSIEP);
			lRoot.add(lTreeSentenza);

			// CREAZIONE DEL TEMPLATE
			// Ricavo nome del template
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lEvento.getTemIdTemplate());
			// lReport = new ReportGenerator();
			lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);

			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("generate document eseguito");
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaEmissioneOrdinanza : fine");
		return lByteArrayOut;
	}

	/*
	 * Questa funzione è stata prodotta per gestire il caso della stampa del Foglio Complementare Nsc; occorre
	 * passare il Documento Allegato.
	 */
	private ByteArrayOutputStream ExPreStampaFoglioComplementareNsc(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel, DocumentoAllegatoModel aDocAll) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;

		Connection lConn = null;

		// Report generator per la costruzione del report
		ReportGenerator lReport = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaEmissioneOrdinanza : inizio");
		try {
			if (lEvento == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Evento inesistente : ");

			// connessione al Db
			lConn = getDBConnection();

			// creazione delle varie foglie componenti del documento TreeModel
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));

			// CREAZIONE DEL TEMPLATE
			// Ricavo nome del template
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lEvento.getTemIdTemplate());
			// lReport = new ReportGenerator();
			lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);

			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("generate document eseguito");
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaEmissioneOrdinanza : fine");
		return lByteArrayOut;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa di alcuni tipi di Ordinanza: Ordinanza di Rinvio Udienza,
	 * Generazione Modelli.
	 *
	 * @param FascicoloGPModel
	 * @param EventoModel
	 *            Evento;
	 * @param UtenteModel
	 *            aUtenteModel.
	 * @return ByteArrayOutputStream.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaDocumentoOrdinanza(FascicoloGPModel aFasc,
			EventoNotificaModel aEvento, UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaDocumentoOrdinanza : inizio");

		try {
			aEvento.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());

			TreeModel lTree = prelevaDatiDocumentoOrdinanza(aEvento, aFasc,
					aUtenteModel.getUfficioUtente().getCodUfficio());

			ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Chiave = " + aEvento.getNomeTemplate());

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Exception: " + e);
			throw e;
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaDocumentoOrdinanza : fine");
		return lByteArrayOut;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa dell' Emissione Decreto.
	 *
	 * @param aIdFasSius
	 *            l'id del fascicolo SIUS.
	 * @return ByteArrayOutputStream.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaEmissioneDecreto(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {

		return ExPreStampaEmissioneDecreto(lEvento, aCodUff, aUtenteModel, null);
	}

	/*
	 * Questa funzione con un parametro in più (DocumentoAllegatoModel) rispetto alla versione public è stata
	 * prodotta per gestire il caso della stampa del Foglio Complementare; in questo caso infatti occorre
	 * passare il Documento Allegato. Negli altri casi, ovvero quando questa viene richiamata dalla versione
	 * public il nuovo parametro viene valorizzato a null.
	 */
	private ByteArrayOutputStream ExPreStampaEmissioneDecreto(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel, DocumentoAllegatoModel aDocAll) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;
		TreeModel lTreeDecreto = null;
		TreeModel lTreeDecretoRiferimento = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeFasSIEP = null;
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeGenProc = null;
		TreeModel lTreeMisAlt = null;
		TreeModel lTreeMisSic = null;
		TreeModel lTreeSentenza = null;
		// TreeModel lTreeEsecuzioneMA = null;

		// TreeModel lTreeResidenza = null;
		TreeModel lTreeEventoNotifiche = null;

		// 21/01/2007 Fascicolo SIUS Origine + eventuale EMA.
		TreeModel lTreeFasOri = null;

		Connection lConn = null;

		FascicoloGPModel lFasGP = null;
		FascicoloGPModel lFasGPMisAlt = null;
		FascicoloGPModel lFasGPMisSic = null;
		// TemplateModel lTemplate = null; non più usato

		// Report generator per la costruzione del report
		ReportGenerator lReport = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaEmissioneDecreto : inizio");
		try {
			// connessione al Db
			lConn = getDBConnection();

			// ricerca FascicoloSIUSGPmodel
			lFasGP = getFascicoloGPSius(lEvento.getFasSiuIdFascicoloSius(), lConn);

			if (lFasGP == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo inesistente : " + lEvento.getFasSiuIdFascicoloSius());

			lTreeFasSIUS = prelevaDatiFascicoloSius(lFasGP, lEvento.getIdEvento(), aDocAll, lConn);

			// ricerca Template di stampa
			// lTemplate = getTemplateByIdTemplate(lEvento.getTemIdTemplate(), lConn);

			// Ricerca idfascicolo sius per misura alternativa solo per S22
			if (lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S22") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisAlt = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGP.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGP.getGeneraleProcedimentoModel().getProgrS1(), aCodUff, lConn);
			}
			// Ricerca idfascicolo sius per misura sicurezza solo per S09
			if (lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S09") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisSic = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGP.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGP.getGeneraleProcedimentoModel().getProgrS1(), aCodUff, lConn);
			}

			// creazione delle varie foglie componenti del documento TreeModel
			// lTreeUdienza = prelevaDatiUdienza(lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza(),
			// lConn); LUIGI 15-11-04
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));

			// lTreeFasSIUS = new TreeModel(lFasGP.getFascicoloSiusModel());

			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			if (lFasGPMisAlt != null && lFasGPMisAlt.getFascicoloSiusModel() != null)
				lTreeMisAlt = prelevaDatiEsecuzioneMA(
						lFasGPMisAlt.getFascicoloSiusModel().getIdFascicoloSius(), lConn);
			if (lFasGPMisSic != null && lFasGPMisSic.getFascicoloSiusModel() != null)
				lTreeMisSic = prelevaDatiEsecuzioneMS(
						lFasGPMisSic.getFascicoloSiusModel().getIdFascicoloSius(), lConn);

			// lTreeMagRelatore = prelevaDatiMagistratoRelatore(lEvento.getFasSiuIdFascicoloSius(), lConn);
			// LUIGI 15-11-04
			lTreeSoggetto = prelevaDatiSoggetto(lFasGP.getFascicoloSiusModel().getSogIdSoggetto(),
					lEvento.getFasSiuIdFascicoloSius(), lConn);

			// MEV_66: aggiunto calcolo raggiungimento 25° anno di eta'
			// "prelevaDatiSoggetto" è ridondante poichè lo calcola già "prelevaDatiFascicoloSius"
			if (lFasGP.getGeneraleProcedimentoModel() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento() != null
					&& lFasGP.getFascicoloSiusModel().getSoggetto().getDataNascita() != null
					&& ("U121".equals(lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
							|| "U122"
									.equals(lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
							|| "U123".equals(
									lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento()))) {
				Date venticinqeAnni = DateUtils.moveDateTo(
						lFasGP.getFascicoloSiusModel().getSoggetto().getDataNascita(), Calendar.YEAR, 25);
				SoggettoModel sm = (SoggettoModel) lTreeSoggetto.getModel();
				sm.setDataNascitaPresuntaCalc(venticinqeAnni);
			}
			// FINE MEV_66

			// lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSius(lEvento.getFasSiuIdFascicoloSius(),
			// lConn);

			lTreeFasSIEP = prelevaDatiFascicoloSiep(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lTreeSentenza = prelevaDatiSentenza(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lTreeEventoNotifiche = prelevaDatiEventoNotifiche(lEvento.getIdEvento(), lConn);

			// Recupera dati Periodo Altra Sanzione attraverso l'evento.
			prelevaDatiPeriodoAltraSanzioneByEvento(lEvento.getIdEvento(), lTreeEventoNotifiche, lConn);

			// Dati di Deposito Decreto.
			lTreeDecreto = prelevaDatiDepositoDecreto(lEvento.getIdEvento(), lConn);
			// Dati di Deposito Decreto di Riferimento (da Revocare)
			if (lTreeDecreto != null && lEvento.getEveIdEvento() != null) {
				// Se il Decreto ha un decreto di Riferimento
				lTreeDecretoRiferimento = prelevaDatiDepositoDecreto(lEvento.getEveIdEvento(), lConn);
				if (lTreeDecretoRiferimento != null) {
					lTreeDecreto.add(lTreeDecretoRiferimento);
					lTreeDecreto.add(prelevaDatiFascicoloSiusByIdEvento(lEvento.getEveIdEvento(), lConn));
				}
			}

			// MEV_2023-35: aggiunta gestione procura esecuzione per U136 (Licenza - pene sostitutive -
			// Inosservanza prescrizioni)
			if (lFasGP.getGeneraleProcedimentoModel() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento() != null
					&& "U136".equals(lFasGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento())) {
				DepositoDecretoModel ddm = (DepositoDecretoModel) lTreeDecreto.getModel();
				if (ddm != null && Utils.isPresent(ddm.getDescrProcuraEsecuzione())) {
					String descrProcuraEsecuzione = ddm.getDescrProcuraEsecuzione();
					if (ddm.getProcuraEsecuzione() != null
							&& ddm.getProcuraEsecuzione().getDescrTipoUfficio() != null)
						ddm.setDescrProcuraEsecuzione(ddm.getProcuraEsecuzione().getDescrTipoUfficio() + " "
								+ descrProcuraEsecuzione);
				}
			}
			// FINE MEV_2023-35

			// 22/01/2007 Fascicolo SIUS Origine.
			if (lFasGP.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null)
				lTreeFasOri = prelevaDatiFascicoloSiusOrigine(
						lFasGP.getFascicoloSiusModel().getIdFascicoloSiusOrigine(), lConn, false);

			// COSTRUZIONE DEL DOCUMENTO
			lTreeGenProc.add(lTreeMisAlt);
			lTreeGenProc.add(lTreeMisSic);
			lTreeFasSIUS.add(lTreeGenProc);
			lTreeFasSIUS.add(lTreeDecreto);
			// 23/01/2007 Aggiunto il Fascicolo SIUS Origine con indentata l'eventuale EMA ;
			if (lTreeFasOri != null)
				lTreeFasSIUS.add(lTreeFasOri);

			lTreeFasSIUS.add(lTreeEventoNotifiche);

			// Aggiunge i dati alla root
			lRoot.add(lTreeSoggetto);
			lRoot.add(lTreeFasSIUS);
			lRoot.add(lTreeFasSIEP);
			lRoot.add(lTreeSentenza);

			// CREAZIONE DEL TEMPLATE
			// Ricava nome del template
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lEvento.getTemIdTemplate());
			lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("generate document eseguito");
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaEmissioneDecreto : fine");
		return lByteArrayOut;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa del Verbale Udienza.
	 *
	 * @param aIdFasSius
	 *            l'id del fascicolo SIUS.
	 * @return ByteArrayOutputStream.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */

	public ByteArrayOutputStream ExPreStampaVerbaleUdienza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeUdienza = null;
		TreeModel lTreeGenProc = null;
		TreeModel lTreeMagRelatore = null;
		TreeModel lTreeLuogoDetenzione = null;

		Connection lConn = null;

		FascicoloGPModel lFasGP = null;
		// TemplateModel lTemplate = null;
		Vector lAvvocati = new Vector();
		Vector lTenori = new Vector();

		// Report generator per la costruzione del report
		ReportGenerator lReport = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaVerbaleUdienza : inizio");

		try {
			// connessione al Db
			lConn = getDBConnection();

			// ricerca FascicoloSIUSGPmodel
			lFasGP = getFascicoloGPSius(lEvento.getFasSiuIdFascicoloSius(), lConn);

			if (lFasGP == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo inesistente : " + lEvento.getFasSiuIdFascicoloSius());

			// ricerca Template di stampa
			// lTemplate = getTemplateByCodMotivo(lEvento.getCodMotivo(), lConn);

			// creazione delle varie foglie componenti del documento TreeModel
			lTreeUdienza = prelevaDatiUdienza(lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza(), lConn);
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));
			lTreeFasSIUS = new TreeModel(lFasGP.getFascicoloSiusModel());
			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			lTreeMagRelatore = prelevaDatiMagistratoRelatore(lEvento.getFasSiuIdFascicoloSius(), lConn);
			lTreeSoggetto = prelevaDatiSoggetto(lFasGP.getFascicoloSiusModel().getSogIdSoggetto(),
					lEvento.getFasSiuIdFascicoloSius(), lConn);
			lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSius(lEvento.getFasSiuIdFascicoloSius(), lConn);

			// COSTRUZIONE DEL DOCUMENTO
			lRoot.add(lTreeSoggetto);
			lTreeFasSIUS.add(lTreeGenProc);
			lTreeFasSIUS.add(lTreeUdienza);
			lTreeFasSIUS.add(lTreeMagRelatore);
			lTreeFasSIUS.add(lTreeLuogoDetenzione);

			// Tenori
			lTenori = getTenoriByGenProc(lFasGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(),
					lConn);
			if (lTenori.size() != 0) {
				Iterator lItxTen = lTenori.iterator();
				while (lItxTen.hasNext())
					lTreeGenProc.add(new TreeModel((TenoreModel) lItxTen.next()));
			}

			// Avvocati
			lAvvocati = getAvvocatiFascicolo(lEvento.getFasSiuIdFascicoloSius(), lConn);
			if (lAvvocati.size() != 0) {
				Iterator lItx = lAvvocati.iterator();
				while (lItx.hasNext())
					lTreeFasSIUS.add(new TreeModel(((AvvocatoSiusModel) lItx.next()).getAvvocato()));
			}
			// Aggiunge il fascicolo alla root
			lRoot.add(lTreeFasSIUS);

			// CREAZIONE DEL TEMPLATE
			// lReport = new ReportGenerator();
			lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lEvento.getTemIdTemplate());
			// String lNomeTemplate = lTemplate.getPathRicerca() + lTemplate.getNomeTemplate();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaVerbaleUdienza : fine");
		return lByteArrayOut;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa dell'Allegato dell'Ordinanza, Decreto o Sentenza.
	 *
	 * @param aIdFascicoloSius
	 *            Id del Procedimento SIUS.
	 * @param aDAMod
	 *            model del Documento Allegato.
	 * @return ByteArrayOutputStream.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaAllegato(BigDecimal aIdFascicoloSius,
			DocumentoAllegatoModel aDAMod, String aCodUff, UtenteModel aUtenteModel) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeGenProc = null;
		TreeModel lTreeEvento = null;
		TreeModel lTreeOrdinanza = null;
		TreeModel lTreeDecreto = null;
		TreeModel lTreeDepositoSentenza = null;
		// TreeModel lTreeNotifiche = null;
		TreeModel lTreeSentenza = null;
		TreeModel lTreeMisSic = null;

		// TemplateModel lTemplate = null;

		Connection lConn = null;

		// Oggetti Model Utilizzati.
		FascicoloGPModel lFasGP = null;
		FascicoloGPModel lFasGPMisSic = null;

		// Report generator per la costruzione del report
		ReportGenerator lReport = null;
		lTreeGenProc = new TreeModel();

		try {
			// connessione al Db
			lConn = getDBConnection();

			// ricerca FascicoloSIUSGPmodel
			lFasGP = getFascicoloGPSius(aIdFascicoloSius, lConn);

			if (lFasGP == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo inesistente : " + aIdFascicoloSius);

			// Ricerca Template di stampa
			// lTemplate = getTemplateByIdTemplate(aDAMod.getTemIdTemplate(), lConn);

			// Ricerca dei dati di Evento-Notifiche
			lTreeEvento = prelevaDatiEventoNotifiche(aDAMod.getEveIdEvento(), lConn);

			// Ricerca dei dati di DepositoOrdinanza, DepositoDecreto o DepositoSentenza
			// a seconda del tipo Doc. Allegato.
			if (aDAMod.getCodTipoDocumento().compareTo("02") == 0) {
				lTreeOrdinanza = prelevaDatiDepositoOrdinanzaPC(aDAMod.getEveIdEvento(), lConn);
			}
			// Dati di Deposito Decreto.
			if (aDAMod.getCodTipoDocumento().compareTo("03") == 0) {
				lTreeDecreto = prelevaDatiDepositoDecreto(aDAMod.getEveIdEvento(), lConn);
			}
			// Dati di Deposito Sentenza
			if (aDAMod.getCodTipoDocumento().compareTo("01") == 0) {
				lTreeDepositoSentenza = prelevaDatiDepositoSentenza(aDAMod.getEveIdEvento(), lConn);
			}

			// creazione delle varie foglie componenti del documento TreeModel
			// lTreeUdienza = prelevaDatiUdienza(lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza(),
			// lConn);
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));
			lTreeFasSIUS = new TreeModel(lFasGP.getFascicoloSiusModel());

			// 19/05/2011 Ricerca idfascicolo sius per misura sicurezza solo per S09
			if (lFasGP.getGeneraleProcedimentoModel() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S09") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisSic = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGP.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGP.getGeneraleProcedimentoModel().getProgrS1(), aCodUff, lConn);
			}
			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			if (lFasGPMisSic != null && lFasGPMisSic.getFascicoloSiusModel() != null)
				lTreeMisSic = prelevaDatiEsecuzioneMS(
						lFasGPMisSic.getFascicoloSiusModel().getIdFascicoloSius(), lConn);

			// lTreeSoggetto = new TreeModel(lFasGP.getFascicoloSiusModel().getSoggetto());
			lTreeSoggetto = prelevaDatiSoggetto(lFasGP.getFascicoloSiusModel().getSogIdSoggetto(),
					lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), lConn);

			// Inseriti avvocati e magistrato legati al fascicolo
			// IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
			// Riempi l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSius.TREE_FASCICOLOSIEP };
			// Crea il TreeModel con i dati che occorrono
			lRoot = ExAggiungiDatiStampa(lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), aTipoDati,
					lRoot);

			// Titoli Esecutivi Referenziati (Fascicolo SIEP e Sentenza).
			// Enzo 19/01/2005
			lRoot = prelevaDatiTitoliEsecutiviReferenziati(lRoot,
					lFasGP.getFascicoloSiusModel().getIdFascicoloSius(),
					lFasGP.getFascicoloSiusModel().getNumeroFascicoliUnificati(), lConn);

			// Riferimento Fascicolo SIEP // STUB 14/10/2004
			lRoot = prelevaDatiRifasiep(lRoot, lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), lConn);

			// Dati della sentenza
			lTreeSentenza = prelevaDatiSentenza(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lRoot.add(lTreeSentenza);

			// Costruzione del documento
			lRoot.add(lTreeSoggetto);
			lTreeFasSIUS.add(lTreeSoggetto); // STUB 18/10/2004
			lTreeFasSIUS.add(lTreeGenProc);

			lTreeGenProc.add(lTreeMisSic);
			// Dal tipo Documento Allegato decido tra Ordinanza/Decreto/Sentenza
			if (aDAMod.getCodTipoDocumento().compareTo("02") == 0)
				lTreeFasSIUS.add(lTreeOrdinanza);
			if (aDAMod.getCodTipoDocumento().compareTo("03") == 0)
				lTreeFasSIUS.add(lTreeDecreto);
			if (aDAMod.getCodTipoDocumento().compareTo("01") == 0)
				lTreeFasSIUS.add(lTreeDepositoSentenza);

			lTreeFasSIUS.add(lTreeEvento);
			lRoot.add(lTreeFasSIUS);

			// lReport = new ReportGenerator();
			lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
			// 20231218 [SG]: allineato al resto del codice; altrimenti non funziona in locale
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aDAMod.getTemIdTemplate());
			// String lNomeTemplate = lTemplate.getPathRicerca() + lTemplate.getNomeTemplate();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
			// lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("StampaController.ExPreStampaAllegato Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("StampaController.ExPreStampaAllegato : fine");
		return lByteArrayOut;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa del Fissazione Udienza.
	 *
	 * @param aIdFasSius
	 *            l'id del fascicolo SIUS.
	 * @return ByteArrayOutputStream. ritorna oggetto ByteArray stampa generata.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaFissazioneUdienza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ExPreStampaFissazioneUdienza : inizio");

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;
		TreeModel lTreeUtente = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeFasSIEP = null;
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeUdienza = null;
		TreeModel lTreeGenProc = null;
		TreeModel lTreeMagRelatore = null;
		TreeModel lTreeLuogoDetenzione = null;
		TreeModel lTreeSentenza = null;
		TreeModel lTreeEventoNotifiche = null;
		TreeModel lTreeDecreto = null;

		Connection lConn = null;
		FascicoloGPModel lFasGP = null;
		Vector lAvvocati = new Vector();
		BigDecimal lIdUdienza = null;
		// Report generator per la costruzione del report
		ReportGenerator lReport = null;

		try {
			// connessione al Db
			lConn = getDBConnection();

			// ricerca FascicoloSIUSGPmodel
			lFasGP = getFascicoloGPSius(lEvento.getFasSiuIdFascicoloSius(), lConn);

			if (lFasGP == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo inesistente : " + lEvento.getFasSiuIdFascicoloSius());

			// Si cerca l'udienza attraverso l'UDIENZA_PROCEDIMENTO
			lIdUdienza = cercaIdUdienzaByTdEvento(lEvento.getIdEvento());
			if (lIdUdienza == null)
				lIdUdienza = lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza();

			// creazione delle varie foglie componenti del documento TreeModel
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));
			lTreeUtente = new TreeModel(aUtenteModel); // dati dell'utente connesso
			lTreeFasSIUS = new TreeModel(lFasGP.getFascicoloSiusModel());
			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			lTreeUdienza = prelevaDatiUdienza(lIdUdienza, lConn);
			lTreeMagRelatore = prelevaDatiMagistratoRelatore(lEvento.getFasSiuIdFascicoloSius(), lConn);
			lTreeSoggetto = prelevaDatiSoggetto(lFasGP.getFascicoloSiusModel().getSogIdSoggetto(),
					lEvento.getFasSiuIdFascicoloSius(), lConn);
			lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSius(lEvento.getFasSiuIdFascicoloSius(), lConn);
			lTreeFasSIEP = prelevaDatiFascicoloSiep(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lTreeSentenza = prelevaDatiSentenza(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lTreeEventoNotifiche = prelevaDatiEventoNotifiche(lEvento.getIdEvento(), lConn);
			lTreeDecreto = prelevaDatiDepositoDecreto(lEvento.getIdEvento(), lConn);

			// Avvocati
			lAvvocati = getAvvocatiFascicolo(lEvento.getFasSiuIdFascicoloSius(), lConn);
			if (lAvvocati.size() != 0) {
				Iterator lItx = lAvvocati.iterator();
				while (lItx.hasNext())
					lTreeFasSIUS.add(new TreeModel(((AvvocatoSiusModel) lItx.next()).getAvvocato()));
			}

			// COSTRUZIONE DEL DOCUMENTO
			lTreeFasSIUS.add(lTreeGenProc);
			lTreeFasSIUS.add(lTreeUdienza);
			lTreeFasSIUS.add(lTreeMagRelatore);
			lTreeFasSIUS.add(lTreeLuogoDetenzione);
			lTreeFasSIUS.add(lTreeEventoNotifiche);
			lTreeFasSIUS.add(lTreeDecreto);

			// Aggiunge i dati alla root
			lRoot.add(lTreeUtente);
			lRoot.add(lTreeSoggetto);
			lRoot.add(lTreeFasSIUS);
			lRoot.add(lTreeFasSIEP);
			lRoot.add(lTreeSentenza);

			// CREAZIONE DEL TEMPLATE
			// Ricavo nome del template
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lEvento.getTemIdTemplate());
			lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);

			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
		} catch (F3BException fE) {
			throw fE;
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("ExPreStampaFissazioneUdienza: " + e.toString());
		} finally {
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ExPreStampaFissazioneUdienza : fine");
		return lByteArrayOut;
	}

	/**
	 * Classe per il prelievo dati del Fascicolo al fine di visualizzarli.
	 *
	 * @param aIdFasSius
	 *            l'id del Fascicolo SIUS.
	 * @param aTipoDati
	 *            Quali dati recuperare.
	 * @return dati in formato TreeModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public TreeModel ExPrelevaDatiVideo(BigDecimal aIdFasSius, int[] aTipoDati) throws F3BException {

		Connection lConn = null;
		TreeModel lTreeDati = null;
		try {
			lConn = getDBConnection(); // connessione al Db
			lTreeDati = new TreeModel();
			lTreeDati = iperPrelevaDati(aIdFasSius, aTipoDati, lTreeDati, lConn);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		return lTreeDati;
	}

	/**
	 * Classe Public per il prelievo dati del Fascicolo al fine di creare un report.
	 *
	 * @param aIdFasSius
	 *            l'id del Fascicolo SIUS.
	 * @param aTipoDati
	 *            Quali dati recuperare.
	 * @param aCodiceUfficio
	 *            codice ufficio utente connesso.
	 * @return dati in formato TreeModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public TreeModel ExPrelevaDatiStampa(BigDecimal aIdFasSius, int[] aTipoDati, String aCodiceUfficio)
			throws F3BException {

		Connection lConn = null;
		TreeModel lTreeDati = null;
		try {
			lConn = getDBConnection(); // connessione al Db
			lTreeDati = new TreeModel(CreateRoot(aCodiceUfficio, lConn));
			lTreeDati = iperPrelevaDati(aIdFasSius, aTipoDati, lTreeDati, lConn);
			lTreeDati = prelevaDati(aIdFasSius, aTipoDati, lTreeDati, lConn);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		return lTreeDati;
	}

	/**
	 * Classe Public per il aggiungere dati ad un treeModel esistente.
	 *
	 * @param aIdFasSius
	 *            l'id del Fascicolo SIUS.
	 * @param aTipoDati
	 *            Quali dati recuperare.
	 * @param lTreeDati
	 *            TreeModel da ampliare
	 * @return dati in formato TreeModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public TreeModel ExAggiungiDatiStampa(BigDecimal aIdFasSius, int[] aTipoDati, TreeModel lTreeDati)
			throws F3BException {

		Connection lConn = null;
		try {
			lConn = getDBConnection(); // connessione al Db
			lTreeDati = iperPrelevaDati(aIdFasSius, aTipoDati, lTreeDati, lConn);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		return lTreeDati;
	}

	public ByteArrayOutputStream ExPreStampaModelliAttiIstruttori(BigDecimal aIdFascicoloSius, String aCodUff,
			UtenteModel aUtente, String lTemIdTemplate) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeFasSIEP = null;
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeUdienza = null;
		TreeModel lTreeGenProc = null;
		TreeModel lTreeMagRelatore = null;
		TreeModel lTreeLuogoDetenzione = null;
		TreeModel lTreeSentenza = null;

		Connection lConn = null;

		FascicoloGPModel lFasGP = null;

		Vector lAvvocati = new Vector();
		Vector lTenori = new Vector();

		// Report generator per la costruzione del report
		ReportGenerator lReport = null;

		try {
			// connessione al Db
			lConn = getDBConnection();

			// ricerca FascicoloSIUSGPmodel
			lFasGP = getFascicoloGPSius(aIdFascicoloSius, lConn);

			if (lFasGP == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo inesistente : " + aIdFascicoloSius);

			// creazione delle varie foglie componenti del documento TreeModel
			lTreeUdienza = prelevaDatiUdienza(lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza(), lConn);
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));
			lTreeFasSIUS = new TreeModel(lFasGP.getFascicoloSiusModel());
			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			lTreeMagRelatore = prelevaDatiMagistratoRelatore(aIdFascicoloSius, lConn);
			lTreeSoggetto = prelevaDatiSoggetto(lFasGP.getFascicoloSiusModel().getSogIdSoggetto(),
					aIdFascicoloSius, lConn);
			lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSius(aIdFascicoloSius, lConn);
			lTreeFasSIEP = prelevaDatiFascicoloSiep(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lTreeSentenza = prelevaDatiSentenza(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);

			// Avvocati
			lAvvocati = getAvvocatiFascicolo(aIdFascicoloSius, lConn);
			if (lAvvocati.size() != 0) {
				Iterator lItx = lAvvocati.iterator();
				while (lItx.hasNext())
					lTreeFasSIUS.add(new TreeModel(((AvvocatoSiusModel) lItx.next()).getAvvocato()));
			}

			// Tenori
			lTenori = getTenoriByGenProc(lFasGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(),
					lConn);
			if (lTenori.size() != 0) {
				Iterator lItxTen = lTenori.iterator();
				while (lItxTen.hasNext())
					lTreeGenProc.add(new TreeModel((TenoreModel) lItxTen.next()));
			}

			// COSTRUZIONE DEL DOCUMENTO
			lTreeFasSIUS.add(lTreeGenProc);
			lTreeFasSIUS.add(lTreeUdienza);
			lTreeFasSIUS.add(lTreeMagRelatore);
			lTreeFasSIUS.add(lTreeLuogoDetenzione);

			// Aggiunge i dati alla root
			lRoot.add(new TreeModel(aUtente));
			lRoot.add(lTreeSoggetto);
			lRoot.add(lTreeFasSIUS);
			lRoot.add(lTreeFasSIEP);

			// Titoli Esecutivi Referenziati (Fascicolo SIEP e Sentenza).
			// Enzo 19/01/2005
			lRoot = prelevaDatiTitoliEsecutiviReferenziati(lRoot,
					lFasGP.getFascicoloSiusModel().getIdFascicoloSius(),
					lFasGP.getFascicoloSiusModel().getNumeroFascicoliUnificati(), lConn);

			// Riferimento Fascicolo SIEP.
			lRoot = prelevaDatiRifasiep(lRoot, lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), lConn); // STUB
																											// 14/10/2004

			lRoot.add(lTreeSentenza);

			// CREAZIONE DEL TEMPLATE
			// Ricavo nome del template
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lTemIdTemplate);
			// lReport = new ReportGenerator();
			lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaModelliAttiIstruttori : fine");
		return lByteArrayOut;
	}

	public ByteArrayOutputStream ExPreStampaAvvocato(BigDecimal aIdAvvocato, BigDecimal aIdFascicoloSius,
			String aCodUff, String lTemIdTemplate, UtenteModel aUtenteModel) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeFasSIEP = null;
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeUdienza = null;
		TreeModel lTreeGenProc = null;
		TreeModel lTreeMagRelatore = null;
		TreeModel lTreeLuogoDetenzione = null;
		TreeModel lTreeSentenza = null;

		Connection lConn = null;
		FascicoloGPModel lFasGP = null;
		Vector lAvvocati = new Vector();
		Vector lTenori = new Vector();

		// Report generator per la costruzione del report
		ReportGenerator lReport = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaAvvocato : inizio");
		try {
			// connessione al Db
			lConn = getDBConnection();

			// ricerca FascicoloSIUSGPmodel
			lFasGP = getFascicoloGPSius(aIdFascicoloSius, lConn);

			if (lFasGP == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo inesistente : " + aIdFascicoloSius);

			// creazione delle varie foglie componenti del documento TreeModel
			lTreeUdienza = prelevaDatiUdienza(lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza(), lConn);
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));
			lTreeFasSIUS = new TreeModel(lFasGP.getFascicoloSiusModel());
			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			lTreeMagRelatore = prelevaDatiMagistratoRelatore(aIdFascicoloSius, lConn);
			lTreeSoggetto = prelevaDatiSoggetto(lFasGP.getFascicoloSiusModel().getSogIdSoggetto(),
					aIdFascicoloSius, lConn);
			lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSius(aIdFascicoloSius, lConn);
			lTreeFasSIEP = prelevaDatiFascicoloSiep(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);

			// Avvocati
			lAvvocati = getAvvocatiFascicolo(aIdFascicoloSius, lConn);
			if (lAvvocati.size() != 0) {
				Iterator lItx = lAvvocati.iterator();
				while (lItx.hasNext()) {
					AvvocatoSiusModel lAvvocato = (AvvocatoSiusModel) lItx.next();
					if (lAvvocato.getAvvocato().getIdAvvocato().compareTo(aIdAvvocato) == 0) {
						lTreeFasSIUS.add(new TreeModel(lAvvocato.getAvvocato()));
					}
				}
			}

			// Tenori
			lTenori = getTenoriByGenProc(lFasGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(),
					lConn);
			if (lTenori.size() != 0) {
				Iterator lItxTen = lTenori.iterator();
				while (lItxTen.hasNext())
					lTreeGenProc.add(new TreeModel((TenoreModel) lItxTen.next()));
			}

			// Preleva i dati dei fascicoli unificati
			Vector lVectFas = null;
			if (lFasGP.getFascicoloSiusModel() != null
					&& lFasGP.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null
					&& lFasGP.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0) {
				FascicoloSiusModel lFasRicModel = new FascicoloSiusModel();
				lFasRicModel.setFasSiuIdFascicoloSius(aIdFascicoloSius);
				IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
				lVectFas = lCtrl.ExRicercaElencoFascicoliUnificati(lFasRicModel);
				Iterator itx4 = lVectFas.iterator();
				while (itx4.hasNext()) {
					FascicoloSiusModel lFasUnificati = (FascicoloSiusModel) itx4.next();
					lTreeFasSIUS.add(new TreeModel(lFasUnificati));
				}
			}

			// COSTRUZIONE DEL DOCUMENTO
			lTreeFasSIUS.add(lTreeGenProc);
			lTreeFasSIUS.add(lTreeUdienza);
			lTreeFasSIUS.add(lTreeMagRelatore);
			lTreeFasSIUS.add(lTreeLuogoDetenzione);

			// Aggiunge i dati alla root
			lRoot.add(lTreeSoggetto);
			lRoot.add(lTreeFasSIUS);
			lRoot.add(lTreeFasSIEP);
			lRoot.add(lTreeSentenza);

			// CREAZIONE DEL TEMPLATE
			// Ricavo nome del template
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lTemIdTemplate);
			// lReport = new ReportGenerator();

			lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("######## NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaAvvocato : fine");
		return lByteArrayOut;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa dell' Impugnazione.
	 *
	 * @param aIdImpugnazione
	 *            (id dell'impugnazione).
	 * @param aIdEvento
	 *            (id dell'evento).
	 * @param aIdFascicoloSius
	 *            (id del Fascicolo).
	 * @return ByteArrayOutputStream.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaImpugnazione(BigDecimal lIdImpugnazione, BigDecimal lIdEvento,
			BigDecimal lIdFascicolo, String lIdTemplate, String aCodUff, UtenteModel aUtenteModel)
			throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;
		TreeModel lTreeImpugnazione = null;
		TreeModel lTreeDecreto = null;
		TreeModel lTreeOrdinanza = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeFasSIEP = null;
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeUdienza = null;
		TreeModel lTreeGenProc = null;
		TreeModel lTreeMisAlt = null;
		TreeModel lTreeMisSic = null;
		TreeModel lTreeMagRelatore = null;
		TreeModel lTreeSentenza = null;
		TreeModel lTreeLuogoDetenzione = null;
		TreeModel lTreeEventoNotifiche = null;

		Connection lConn = null;
		FascicoloGPModel lFasGP = null;
		FascicoloGPModel lFasGPMisAlt = null;
		FascicoloGPModel lFasGPMisSic = null;
		Vector lAvvocati = new Vector();
		Vector lTenori = new Vector();

		// Report generator per la costruzione del report
		ReportGenerator lReport = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaImpugnazione : inizio");
		try {

			// connessione al Db
			lConn = getDBConnection();

			// Preleva l'ImpugnazioneModel
			ImpugnazioneModel lImpModel = null;
			IImpugnazione lImpCtrl = SIUSLookupRemote.getImpugnazioneRemote();
			lImpModel = lImpCtrl.ExRicercaImpugnazioneByKey(lIdImpugnazione);

			// ricerca FascicoloSIUSGPmodel
			lFasGP = getFascicoloGPSius(lIdFascicolo, lConn);

			if (lFasGP == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo inesistente : " + lIdFascicolo);

			// Ricerca idfascicolo sius per misura alternativa solo per S22
			if (lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S22") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisAlt = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGP.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGP.getGeneraleProcedimentoModel().getProgrS1(), aCodUff, lConn);
			}
			// Ricerca idfascicolo sius per misura sicurezza solo per S09
			if (lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S09") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisSic = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGP.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGP.getGeneraleProcedimentoModel().getProgrS1(), aCodUff, lConn);
			}

			// creazione delle varie foglie componenti del documento TreeModel
			lTreeUdienza = prelevaDatiUdienza(lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza(), lConn);
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));
			lTreeImpugnazione = new TreeModel(lImpModel);

			// 18/06/2008 Col prelevaDatiFascicoloSius integro alche le ESS.
			// lTreeFasSIUS = new TreeModel(lFasGP.getFascicoloSiusModel());
			// Costruzione albero Fascicolo SIUS
			lTreeFasSIUS = prelevaDatiFascicoloSius(lFasGP, lIdEvento, lConn);

			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			if (lFasGPMisAlt != null && lFasGPMisAlt.getFascicoloSiusModel() != null)
				lTreeMisAlt = prelevaDatiEsecuzioneMA(
						lFasGPMisAlt.getFascicoloSiusModel().getIdFascicoloSius(), lConn);
			if (lFasGPMisSic != null && lFasGPMisSic.getFascicoloSiusModel() != null)
				lTreeMisSic = prelevaDatiEsecuzioneMS(
						lFasGPMisSic.getFascicoloSiusModel().getIdFascicoloSius(), lConn);
			lTreeMagRelatore = prelevaDatiMagistratoRelatore(lIdFascicolo, lConn);
			lTreeSoggetto = prelevaDatiSoggetto(lFasGP.getFascicoloSiusModel().getSogIdSoggetto(),
					lIdFascicolo, lConn);
			lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSius(lIdFascicolo, lConn);
			lTreeFasSIEP = prelevaDatiFascicoloSiep(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lTreeSentenza = prelevaDatiSentenza(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lTreeEventoNotifiche = prelevaDatiEventoNotifiche(lIdEvento, lConn);

			if (lImpModel.getDepDecIdDepositoDecreto() != null) {
				// Dati di Deposito Decreto.
				lTreeDecreto = prelevaDatiDepositoDecreto(lIdEvento, lConn);
			} else if (lImpModel.getDepOpidDepositoOrdinanzaPc() != null) {
				// Dati di Deposito Ordinanza.
				lTreeOrdinanza = prelevaDatiDepositoOrdinanzaPC(lIdEvento, lConn);
			}

			// Avvocati
			lAvvocati = getAvvocatiFascicolo(lIdFascicolo, lConn);
			if (lAvvocati.size() != 0) {
				Iterator lItx = lAvvocati.iterator();
				while (lItx.hasNext())
					lTreeFasSIUS.add(new TreeModel(((AvvocatoSiusModel) lItx.next()).getAvvocato()));
			}

			// Tenori
			if (lIdEvento != null) {
				// Lettura del Deposito Decreto.
				IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
				DepositoDecretoModel llDepMod = lCtrl.ExRicercaDepositoDecretoByIdEvento(lIdEvento);
				if (llDepMod != null && llDepMod.getIdDepositoDecreto() != null) {
					ITenore lCtrlTen = SIUSLookupRemote.getTenoreRemote();
					lTenori = lCtrlTen.ExRicercaTenoreByDecreto(llDepMod.getIdDepositoDecreto());
					if (lTenori.size() != 0) {
						Iterator lItxTen = lTenori.iterator();
						while (lItxTen.hasNext()) {
							lTreeFasSIUS.add(new TreeModel((TenoreModel) lItxTen.next()));
						}
					}
				}
				// Foglio Complementare
				IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
				DocumentoAllegatoModel lDocAll = null;
				lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento, "06");
				if (lDocAll != null && lDocAll.getIdDocumentoAllegato() != null) {
					lTreeFasSIUS.add(new TreeModel(lDocAll));
				}
			}

			// COSTRUZIONE DEL DOCUMENTO
			lTreeGenProc.add(lTreeMisAlt);
			lTreeGenProc.add(lTreeMisSic);
			lTreeFasSIUS.add(lTreeGenProc);
			if (lImpModel.getDepDecIdDepositoDecreto() != null)
				lTreeFasSIUS.add(lTreeDecreto);
			else if (lImpModel.getDepOpidDepositoOrdinanzaPc() != null)
				lTreeFasSIUS.add(lTreeOrdinanza);
			lTreeFasSIUS.add(lTreeImpugnazione);
			lTreeFasSIUS.add(lTreeUdienza);
			lTreeFasSIUS.add(lTreeMagRelatore);
			lTreeFasSIUS.add(lTreeLuogoDetenzione);
			lTreeFasSIUS.add(lTreeEventoNotifiche);

			// Aggiunge i dati alla root

			lRoot.add(lTreeSoggetto);
			lRoot.add(lTreeFasSIUS);
			lRoot.add(lTreeFasSIEP);

			// Titoli Esecutivi Referenziati (Fascicolo SIEP e Sentenza).
			// Enzo 19/01/2005
			lRoot = prelevaDatiTitoliEsecutiviReferenziati(lRoot,
					lFasGP.getFascicoloSiusModel().getIdFascicoloSius(),
					lFasGP.getFascicoloSiusModel().getNumeroFascicoliUnificati(), lConn);

			// Riferimento Fascicolo SIEP.
			lRoot = prelevaDatiRifasiep(lRoot, lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), lConn); // STUB
																											// 14/10/2004

			lRoot.add(lTreeSentenza);

			// CREAZIONE DEL TEMPLATE
			// Ricavo nome del template
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);
			// lReport = new ReportGenerator();
			lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
			// Sostituito
			// String lNomeTemplate = lTemplate.getPathRicerca() + lTemplate.getNomeTemplate();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("generate document eseguito");
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaImpugnazione : fine");
		return lByteArrayOut;
	}

	/**
	 * 04/01/2005 - Genera il ByteArrayOutputStream per la stampa dell' Elenco procedimenti del soggetto.
	 *
	 * @param lSoggetto
	 *            (model del Soggetto).
	 * @param lFascicoliGPModel
	 *            (Vettore di FascicoloGPModel).
	 * @param aIdDocumento
	 *            (id del Documento).
	 * @param aStampa
	 *            (Model di Stampa).
	 * @return ByteArrayOutputStream.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaProcedimentiDelSoggetto(SoggettoModel lSoggetto,
			Vector lFascicoliGPModel, String aIdDocumento, XModel aStampa, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {

		// Connessione al DB per il prelievo dei dati.
		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot; // radice dell'albero generale del documento
		TreeModel lTreeSoggetto;
		TreeModel lTreeFasSIUS;

		TreeModel lTreeResidenza;
		TreeModel lTreeDomicilio;
		TreeModel lTreeGenProc;
		TreeModel lTreeUdienza;
		TreeModel lTreeMagRelatore;
		TreeModel lTreeLuogoDetenzione;
		TreeModel lTreeSentenza;

		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione del documento
			// lTreeRoot = new TreeModel(aStampa);
			lTreeRoot = new TreeModel(CreateRoot(aCodUff, lConn));

			// lTreeSoggetto = new TreeModel(lSoggetto);
			lTreeSoggetto = prelevaDatiSoggetto(lSoggetto.getIdSoggetto(),
					((FascicoloGPModel) (lFascicoliGPModel.elementAt(0))).getFascicoloSiusModel()
							.getIdFascicoloSius(),
					lConn);

			// COSTRUZIONE DEL DOCUMENTO
			lTreeRoot.add(lTreeSoggetto);

			Iterator lItx = lFascicoliGPModel.iterator();
			while (lItx.hasNext()) {
				FascicoloGPModel lFascicolo = (FascicoloGPModel) lItx.next();

				// Rilettura del FascicoloGPModel;
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFascicolo = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFascicolo.getFascicoloSiusModel().getChiaveAnno(),
						lFascicolo.getFascicoloSiusModel().getChiaveProgr(),
						lFascicolo.getFascicoloSiusModel().getChiaveUfficio(), lConn);

				lTreeFasSIUS = new TreeModel(lFascicolo.getFascicoloSiusModel());
				lTreeGenProc = new TreeModel(lFascicolo.getGeneraleProcedimentoModel());

				// Tenori
				Vector lTenori = getTenoriByGenProc(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), lConn);
				if (lTenori.size() != 0) {
					Iterator lItxTen = lTenori.iterator();
					while (lItxTen.hasNext())
						lTreeGenProc.add(new TreeModel((TenoreModel) lItxTen.next()));
				}

				lTreeResidenza = prelevaDatiResidenzaDomicilio(
						lFascicolo.getFascicoloSiusModel().getIdFascicoloSius(), 'R', lConn);
				lTreeDomicilio = prelevaDatiResidenzaDomicilio(
						lFascicolo.getFascicoloSiusModel().getIdFascicoloSius(), 'D', lConn);
				lTreeUdienza = prelevaDatiUdienza(lFascicolo.getGeneraleProcedimentoModel().getUdiIdUdienza(),
						lConn);
				lTreeMagRelatore = prelevaDatiMagistratoRelatore(
						lFascicolo.getFascicoloSiusModel().getIdFascicoloSius(), lConn);
				lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSius(
						lFascicolo.getFascicoloSiusModel().getIdFascicoloSius(), lConn);

				// Avvocati
				Vector lAvvocati = getAvvocatiFascicolo(
						lFascicolo.getFascicoloSiusModel().getIdFascicoloSius(), lConn);
				if (lAvvocati.size() != 0) {
					Iterator lItxAvv = lAvvocati.iterator();
					while (lItxAvv.hasNext())
						lTreeFasSIUS.add(new TreeModel(((AvvocatoSiusModel) lItxAvv.next()).getAvvocato()));
				}

				TreeModel lTreeFasSIEP = null;
				lTreeFasSIEP = prelevaDatiFascicoloSiepOnly(
						lFascicolo.getFascicoloSiusModel().getFasSieIdFascicoloSiep(), lConn);
				lTreeSentenza = prelevaDatiSentenza(
						lFascicolo.getFascicoloSiusModel().getFasSieIdFascicoloSiep(), lConn);

				// COSTRUZIONE DELL'ALBERATURA DEL FASCICOLO SIUS.
				lTreeFasSIUS.add(lTreeGenProc);
				lTreeFasSIUS.add(lTreeResidenza);
				lTreeFasSIUS.add(lTreeDomicilio);
				lTreeFasSIUS.add(lTreeUdienza);
				lTreeFasSIUS.add(lTreeMagRelatore);
				lTreeFasSIUS.add(lTreeLuogoDetenzione);
				lTreeFasSIUS.add(lTreeFasSIEP);
				lTreeFasSIUS.add(lTreeSentenza);
				// Riferimento Fascicolo SIEP.
				// lTreeFasSIUS = prelevaDatiRifasiep (lTreeFasSIUS,
				// lFascicolo.getFascicoloSiusModel().getIdFascicoloSius(), lConn); // STUB 14/10/2004

				// Dati degli eventi (di tipo Provvedimento) per il FASCICOLO SIUS.
				lEveDao = new EventoSqlDAO(lConn);
				lEveDao.ricercaEventoByFascicoloSius(lFascicolo.getFascicoloSiusModel().getIdFascicoloSius(),
						COD_EVENTO_PROVVEDIMENTO);
				Vector lEventi = new Vector(lEveDao.getModels());
				Iterator itx = lEventi.iterator();
				while (itx.hasNext()) {
					EventoModel lEvento = (EventoModel) itx.next();

					if (lEvento != null)
						lTreeFasSIUS.add(prelevaDatiEvento(lEvento, lConn));
				}
				// Aggiunge il fascicolo SIUS alla root.
				lTreeRoot.add(lTreeFasSIUS);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.ExPreStampaProcedimentiDelSoggetto : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIUSException("StampaController.ExPreStampaProcedimentiDelSoggetto : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdDocumento);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * 20/06/2005 - Genera il ByteArrayOutputStream per la stampa delle Richieste Atti.
	 *
	 * @param aEvento
	 *            (model dell' Evento),
	 * @param aCodUfficio
	 *            (codice Ufficio).
	 * @param lUtenteModel
	 *            (Model di UtenteModel).
	 * @return ByteArrayOutputStream.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaRichiestaAtti(EventoModel aEvento, String aCodUff,
			UtenteModel aUtente) throws F3BException {

		// Connessione al DB per il prelievo dei dati.
		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot = null; // radice dell'albero generale del documento
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeFasSiep = null;
		TreeModel lTreeEvento = null;

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione del documento
			lTreeRoot = new TreeModel(CreateRoot(aCodUff, lConn));
			// ricerca FascicoloSIUSGPmodel
			FascicoloGPModel lFasGP = getFascicoloGPSius(aEvento.getFasSiuIdFascicoloSius(), lConn);
			if (lFasGP != null) {
				// Costruzione albero Fascicolo SIUS
				lTreeFasSIUS = prelevaDatiFascicoloSius(lFasGP, aEvento.getIdEvento(), lConn);

				// Inserisce il GPmodel sotto il fasccicolo.
				if (lFasGP.getGeneraleProcedimentoModel() != null)
					lTreeFasSIUS.add(new TreeModel(lFasGP.getGeneraleProcedimentoModel()));

				// Costruzione albero Fascicolo SIEP
				if (lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
					lTreeFasSiep = prelevaDatiFascicoloSiep(
							lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(), lConn);
				}
			}

			// Costruzione albero Evento
			lTreeEvento = prelevaDatiEvento(aEvento, lConn);

			// Dati di Deposito Decreto. New *** 20/05/2011
			// lTreeDecreto = prelevaDatiDepositoDecreto(aEvento.getEveIdEvento(),lConn);

			// COSTRUZIONE DEL DOCUMENTO
			lTreeRoot.add(new TreeModel(aUtente));
			// lTreeFasSIUS.add(lTreeDecreto);
			lTreeRoot.add(lTreeFasSIUS);

			// STUB 15/10/2004 Aggiunto ICostantiStampaSius.TREE_RIF_FAS_SIEP.
			int[] aTipoDatiSog = { ICostantiStampaSius.TREE_SOGGETTO, ICostantiStampaSius.TREE_RIF_FAS_SIEP };
			lTreeRoot = ExAggiungiDatiStampa(aEvento.getFasSiuIdFascicoloSius(), aTipoDatiSog, lTreeRoot);
			lTreeRoot.add(lTreeEvento);
			lTreeRoot.add(lTreeFasSiep);

		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIUSException("StampaController.ExPreStampaProcedimentiDelSoggetto : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// generazione del documento
		// ReportGenerator lReport = new ReportGenerator();
		ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());
		// Il nome del template è stato passato
		String lNomeTemplate = aEvento.getTemIdTemplate();

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * 20/06/2005 - Genera il ByteArrayOutputStream per la stampa dell'Elenco Richieste Parere.
	 *
	 * @param aParere
	 *            (ParereModel)
	 * @param aListaRichieste
	 *            (Vector di ParereModel),
	 * @param lUtenteModel
	 *            (Model di UtenteModel).
	 * @return ByteArrayOutputStream.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaPareri(ParereModel aParere, Vector aListaRichieste,
			UtenteModel aUtente) throws F3BException {

		// Connessione al DB per il prelievo dei dati.
		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot = null; // radice dell'albero generale del documento

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione del documento
			XModel lBase = CreateRoot(aParere.getCodUfficioEmittente(), lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			FiltroPareriModel lXParere = new FiltroPareriModel(lBase, aParere.getDataEmissione(),
					aParere.getDataEmissione2(), aParere.getDescrOggettoProcedimento(),
					aParere.getDescrMotivo());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root Xparere " + lXParere);

			// lTreeRoot = new TreeModel(lXParere);
			// STUB: devo fare così perche pare che il parsing del TreeModel non ispeziona gli ancestor. Da
			// approfondire ! Luigi
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(aUtente));
			lTreeRoot.add(new TreeModel(lXParere));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("data di elaborazione " + lXParere.getDataElaborazione());

			// Iterazione della lista di Richiesta Parere
			Iterator lItx = aListaRichieste.iterator();
			ParereModel lParMod = null;
			while (lItx.hasNext()) {
				lParMod = new ParereModel((ParereModel) lItx.next());
				TreeModel lTreeParere = new TreeModel(lParMod);
				lTreeRoot.add(lTreeParere);
			}

		} catch (Exception lEx) {
			throw new SIUSException("StampaController.ExPreStampaPareri : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// generazione del documento con nome template fisso
		ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIUS_RP_005");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa dell'Elenco di Notifiche o Comunicazioni X Fascicolo
	 * SIUS.
	 *
	 * @param aFiltroNotifica
	 *            (RicercaNotificheSiusModel)
	 * @param aListaNotifiche
	 *            (Vector di NotificaFasSiusEveModel),
	 * @param lUtenteModel
	 *            (Model di UtenteModel).
	 * @return ByteArrayOutputStream.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaNotificheSius(RicercaNotificheSiusModel aFiltroNotifica,
			Vector aListaNotifiche, UtenteModel aUtente) throws F3BException {

		// Connessione al DB per il prelievo dei dati.
		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot = null; // radice dell'albero generale del documento
		String lIdTemplate;

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione dell'Ufficio documento
			XModel lBase = CreateRoot(aFiltroNotifica.getCodUfficioInserimento(), lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			// Intestazione del documento XML
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(aUtente));
			lTreeRoot.add(new TreeModel(aFiltroNotifica));

			// Iterazione della lista di Notifiche
			Iterator lItx = aListaNotifiche.iterator();
			NotificaFasSiusEveModel lNotifica = null;
			while (lItx.hasNext()) {
				lNotifica = new NotificaFasSiusEveModel((NotificaFasSiusEveModel) lItx.next());
				TreeModel lTreeNotifica = new TreeModel(lNotifica);
				// Viene aggiunto il ramo relativo all'evento
				if (lNotifica.getEvento() != null)
					lTreeNotifica.add(new TreeModel(lNotifica.getEvento()));
				// Viene aggiunto il ramo relativo al Fascicolo SIUS
				if (lNotifica.getFascicoloSius() != null)
					lTreeNotifica.add(new TreeModel(lNotifica.getFascicoloSius()));
				// Viene aggiunto il ramo relativo al Soggetto
				if (lNotifica.getFascicoloSius() != null
						&& lNotifica.getFascicoloSius().getSoggetto() != null)
					lTreeNotifica.add(
							prelevaDatiSoggetto(lNotifica.getFascicoloSius().getSoggetto().getIdSoggetto(),
									lNotifica.getFascicoloSius().getIdFascicoloSius(), lConn));
				// Viene aggiunto il ramo relativo al destinatario
				lTreeNotifica = aggiungiDestinatario(lTreeNotifica, lNotifica);

				lTreeRoot.add(lTreeNotifica);
			}
		} catch (Exception lEx) {
			throw new SIUSException("StampaController.ExPreStampaNotificheSius : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// Viene istanziato il Report Generator
		ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());

		// I template disponibili sono 2: uno per le Notifiche, l'altro per le Comunicazioni.
		if (aFiltroNotifica.getCodTipoNotifica().equalsIgnoreCase("N"))
			lIdTemplate = "SIUS_ST_005";
		else
			lIdTemplate = "SIUS_ST_006";

		// Si ricava il Nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

		return lByteArrayOut;
	}

	/*
	 * La funzione esplora il Model Notifica per individuare il Destinatario. Per il destinatario trovato
	 * viene creato un nuovo TreeModel ed aggiunto al TreeModel passato.
	 *
	 * @param aBranch : TreeModel a cui aggiungere il destinatario,
	 *
	 * @param aNotifica: NotificaModel da esplorare per individuare il destinatario;
	 *
	 * @return aBranch.
	 */
	private TreeModel aggiungiDestinatario(TreeModel aBranch, NotificaFasSiusEveModel aNotifica) {

		// UFFICIO
		if (aNotifica.getUfficio() != null)
			aBranch.add(new TreeModel(aNotifica.getUfficio()));
		// Autorità Esterna
		else if (aNotifica.getAutoritaEsterna() != null)
			aBranch.add(new TreeModel(aNotifica.getAutoritaEsterna()));
		// CSSA
		else if (aNotifica.getCSSA() != null)
			aBranch.add(new TreeModel(aNotifica.getCSSA()));
		// Istituto di detenzione
		else if (aNotifica.getIstitutoDetenzione() != null)
			aBranch.add(new TreeModel(aNotifica.getIstitutoDetenzione()));
		// Avvocato SIUS
		else if (aNotifica.getAvvSius() != null)
			aBranch.add(new TreeModel(aNotifica.getAvvSius()));
		// Avvocato SIEP
		else if (aNotifica.getAvvSiep() != null)
			aBranch.add(new TreeModel(aNotifica.getAvvSiep()));

		return aBranch;
	}

	// Metodi private di preleva dati.
	/**
	 * La funzione costruisce il TreeModel relativo al Fascicolo SIUS I dati aggregati come foglie sono:
	 * FOGLIO COMPLEMENTARE con cod 06; TENORI legati al Generale Procedimento attualmente attivi; AVVOCATI
	 * SIUS; UDIENZA; MAGISTRATO RELATORE; SOGGETTO; LUOGO DETENZIONE; RIFERIMENTI FASCICOLI SIEP.
	 *
	 * @param FascicoloGPModel
	 *            ,
	 * @param idEvento
	 *            ,
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati del Fascicolo SIUS come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiFascicoloSius(FascicoloGPModel lFasGP, BigDecimal aIdEvento,
			Connection aConn) throws F3BException {

		return prelevaDatiFascicoloSius(lFasGP, aIdEvento, null, aConn);
	}

	private TreeModel prelevaDatiFascicoloSius(FascicoloGPModel lFasGP, BigDecimal aIdEvento,
			DocumentoAllegatoModel aDocAll, Connection aConn) throws F3BException {

		TreeModel lTreeFasSIUS = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;

		try {
			if (lFasGP.getFascicoloSiusModel() != null) {
				lTreeFasSIUS = new TreeModel(lFasGP.getFascicoloSiusModel());

				if (aDocAll == null) {
					// Ricerca Foglio Complementare per ID Evento
					IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
					DocumentoAllegatoModel lDocAll = null;
					lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(aIdEvento, "06");
					if (lDocAll != null && lDocAll.getIdDocumentoAllegato() != null)
						lTreeFasSIUS.add(new TreeModel(lDocAll));
				} else
					lTreeFasSIUS.add(new TreeModel(aDocAll));

				// Ricerca Tenori per ID Generale Procedimento
				lTenDao = new TenoreSqlDAO(aConn);
				lTenDao.ricercaTenoreByGeneraleProc(
						lFasGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lTenori = new Vector(lTenDao.getModels());
				if (lTenori.size() != 0) {
					Iterator lItxTen = lTenori.iterator();
					while (lItxTen.hasNext()) {
						lTreeFasSIUS.add(new TreeModel((TenoreModel) lItxTen.next()));
					}
				}

				// Ricerca Avvocati per ID Fascicolo SIUS
				Vector lAvvocati = getAvvocatiFascicolo(lFasGP.getFascicoloSiusModel().getIdFascicoloSius(),
						aConn);
				if (lAvvocati.size() != 0) {
					Iterator lItx = lAvvocati.iterator();
					while (lItx.hasNext())
						lTreeFasSIUS.add(new TreeModel(((AvvocatoSiusModel) lItx.next()).getAvvocato()));
				}
				// Ricerca Udienza
				if (lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza() != null) {
					lTreeFasSIUS.add(prelevaDatiUdienza(
							lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza(), aConn));
				}
				// Ricerca Magistrato relatore per ID Fascicolo SIUS
				lTreeFasSIUS.add(prelevaDatiMagistratoRelatore(
						lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), aConn));

				// Soggetto per SIUS
				lTreeFasSIUS.add(prelevaDatiSoggetto(lFasGP.getFascicoloSiusModel().getSogIdSoggetto(),
						lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), aConn));

				// Ricerca Luogo Detenzione per ID fascicolo SIUS
				lTreeFasSIUS.add(prelevaDatiLuogoDetenzioneSius(
						lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), aConn));

				// Titoli Esecutivi Referenziati (Fascicolo SIEP e Sentenza).
				// Enzo 19/01/2005
				lTreeFasSIUS = prelevaDatiTitoliEsecutiviReferenziati(lTreeFasSIUS,
						lFasGP.getFascicoloSiusModel().getIdFascicoloSius(),
						lFasGP.getFascicoloSiusModel().getNumeroFascicoliUnificati(), aConn);

				// Riferimento Fascicolo SIEP.
				lTreeFasSIUS = prelevaDatiRifasiep(lTreeFasSIUS,
						lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), aConn);

				// Esecuzione Sanzione Sostitutiva
				lTreeFasSIUS = prelevaEsecuzioneSanzioneSostitutiva(lTreeFasSIUS,
						lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), aConn);

				// Richiesta Conversione
				lTreeFasSIUS = prelevaRichiesteConversione(lTreeFasSIUS,
						lFasGP.getFascicoloSiusModel().getIdFascicoloSius());

				// Richiesta Remissione
				lTreeFasSIUS = prelevaRichiesteRemissione(lTreeFasSIUS,
						lFasGP.getFascicoloSiusModel().getIdFascicoloSius());

				// Misure Sicurezza
				lTreeFasSIUS = prelevaMisureSicurezza(lTreeFasSIUS,
						lFasGP.getFascicoloSiusModel().getIdFascicoloSius());

			} // endif (lFasGP.getFascicoloSiusModel() != null)
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException(e);
		} finally {
			cleanup(lTenDao);
		}
		return lTreeFasSIUS;
	}

	/**
	 * La funzione costruisce il TreeModel relativo al Fascicolo SIUS. Richiama la funzione
	 * prelevaDatiFascicoloSius dopo aver ricavato FascicoloGPModel a partire dall'Id Evento.
	 *
	 * @param IdEvento
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati del Fascicolo SIUS come TreeModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiFascicoloSiusByIdEvento(BigDecimal aIdEvento, Connection aConn)
			throws F3BException {

		TreeModel lTreeFasSIUS = null;
		FascicoloGPModel lFasGP = null;
		EventoModel lEvento = null;
		EventoSqlDAO lEveSqlDao = null;
		try {
			// Cerca l'Evento by key
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();
			if (lEvento != null) {
				// ricerca FascicoloSIUSGPmodel
				lFasGP = getFascicoloGPSius(lEvento.getFasSiuIdFascicoloSius(), aConn);

				if (lFasGP != null)
					lTreeFasSIUS = prelevaDatiFascicoloSius(lFasGP, aIdEvento, aConn);
			}
		} catch (Exception e) {
			throw new F3BException(e);
		} finally {
			cleanup(lEveSqlDao);
		}
		return lTreeFasSIUS;
	}

	/**
	 * La funzione costruisce il TreeModel relativo al solo Fascicolo SIUS Origine e alla sua eventuale EMA.
	 *
	 * @param aIdFascicoloOrigine
	 * @param aConn
	 *            connessione al dbase.
	 * @param test
	 * @return dati del Fascicolo SIUS Origine come TreeModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiFascicoloSiusOrigine(BigDecimal aIdFascicoloOrigine, Connection aConn,
			boolean test) throws F3BException {

		FascicoloGPModel lFasGPOri = null;
		FascicoloGPModel lFasGPMisAlt = null;
		FascicoloGPModel lFasGPMisSic = null;
		FascicoloGPModel lFasGPSanSos = null; // 19/02/2008
		FascicoloGPSqlDAO lFasGPSqlDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null; // 01/02/2008
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null; // 01/02/2008
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeEMA = null;
		TreeModel lTreeEMS = null;
		TreeModel lTreeESS = null; // 19/02/2008
		try {
			// Recupero Fascicolo SIUS.
			lFasGPSqlDao = new FascicoloGPSqlDAO(aConn);
			lFasGPSqlDao.ricercaFascicoloByKey(aIdFascicoloOrigine);
			lFasGPOri = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();

			lTreeFasSIUS = new TreeModel(lFasGPOri.getFascicoloSiusModel());

			// Ricerca idfascicolo sius per misura alternativa solo per S22
			if (lFasGPOri.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S22") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisAlt = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGPOri.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGPOri.getGeneraleProcedimentoModel().getProgrS1(),
						lFasGPOri.getFascicoloSiusModel().getChiaveUfficio(), aConn);
				if (lFasGPMisAlt != null && lFasGPMisAlt.getFascicoloSiusModel() != null) {
					lTreeEMA = prelevaDatiEsecuzioneMA(
							lFasGPMisAlt.getFascicoloSiusModel().getIdFascicoloSius(), aConn);
					lTreeFasSIUS.add(lTreeEMA);
				}
			}
			// Ricerca idfascicolo sius per misura sicurezza solo per S09
			if (lFasGPOri.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S09") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisSic = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGPOri.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGPOri.getGeneraleProcedimentoModel().getProgrS1(),
						lFasGPOri.getFascicoloSiusModel().getChiaveUfficio(), aConn);

				if (lFasGPMisSic != null && lFasGPMisSic.getFascicoloSiusModel() != null) {
					lTreeEMS = prelevaDatiEsecuzioneMS(
							lFasGPMisSic.getFascicoloSiusModel().getIdFascicoloSius(), aConn);
					lTreeFasSIUS.add(lTreeEMS);
				}
			}

			// 19/02/2008 Ricerca idfascicolo sius per sanzione sostitutiva solo per S12
			if (lFasGPOri.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S12") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPSanSos = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGPOri.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGPOri.getGeneraleProcedimentoModel().getProgrS1(),
						lFasGPOri.getFascicoloSiusModel().getChiaveUfficio(), aConn);

				if (lFasGPSanSos != null && lFasGPSanSos.getFascicoloSiusModel() != null) {
					lTreeESS = prelevaDatiEsecuzioneSS(
							lFasGPSanSos.getFascicoloSiusModel().getIdFascicoloSius(), aConn);
					lTreeFasSIUS.add(lTreeESS);
				}
			}

			// 20191025 [SG]: aggiunto codice per estrarre MS modificata
			if (test)
				// Misure Sicurezza
				lTreeFasSIUS = prelevaMisureSicurezza(lTreeFasSIUS, aIdFascicoloOrigine);

			// 01/02/2008 Si aggrega il provvedimento riferito al Fascicolo SIUS Origine.
			lDepDecSqlDao = new DepositoDecretoSqlDAO(aConn);
			lDepDecSqlDao.ricercaDepositoDecretoByIdGenProc(
					lFasGPOri.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			DepositoDecretoModel lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();
			if (lDepDecMod != null && lDepDecMod.getCodTipoDecreto().compareTo("01") != 0
					&& lDepDecMod.getCodTipoDecreto().compareTo("02") != 0) {
				lTreeFasSIUS.add(new TreeModel(lDepDecMod));
			}

			lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(aConn);
			lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByGenProcedimento(
					lFasGPOri.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			DepositoOrdinanzaPcModel lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();
			if (lDepOrdMod != null && lDepOrdMod.getCodTipoOrdinanza().compareTo("RU") != 0)
				lTreeFasSIUS.add(new TreeModel(lDepOrdMod));
		} catch (Exception e) {
			throw new F3BException(e);
		} finally {
			cleanup(lFasGPSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lDepOrdSqlDao);
		}
		return lTreeFasSIUS;
	}

	/**
	 * Esegue il prelievo dati del Soggetto.
	 *
	 * @param aIdSoggetto
	 *            l'id del soggetto, prelevato dal fascicolo SIUS.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati del soggetto come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiSoggetto(BigDecimal aIdSoggetto, BigDecimal lIdFasSius, Connection aConn)
			throws F3BException {

		TreeModel lTreeSoggetto = null; // new TreeModel();
		TreeModel lTreeResidenza = null; // new TreeModel();
		TreeModel lTreeDomicilio = null; // new TreeModel();
		SoggettoSqlDAO lSogSqlDao = null;

		try {
			lSogSqlDao = new SoggettoSqlDAO(aConn);
			lSogSqlDao.ricercaSoggettoByKey(aIdSoggetto);
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
			lTreeSoggetto = new TreeModel(lSogModel);
			lTreeResidenza = prelevaDatiResidenzaDomicilio(lIdFasSius, 'R', aConn);
			lTreeDomicilio = prelevaDatiResidenzaDomicilio(lIdFasSius, 'D', aConn);

			// Mette Residenza e domicilio in soggetto
			lTreeSoggetto.add(lTreeResidenza);
			lTreeSoggetto.add(lTreeDomicilio);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Dati prelevati nel metodo prelevaDatiSoggetto : " + lSogModel);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiSoggetto : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiSoggetto : " + sqlEx);
		} finally {
			cleanup(lSogSqlDao);
		}

		return lTreeSoggetto;
	}

	/**
	 * Esegue il prelievo dati Residenza e Domicilio del Soggetto.
	 *
	 * @param aIdFasSius
	 *            l'id del fascicolo sius.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della residenza del soggetto di un fascicolo Sius come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiResidenzaDomicilio(BigDecimal aIdFasSius, char lTipo, Connection aConn)
			throws F3BException {

		TreeModel lTreeResidenza = null; // new TreeModel();
		ResidenzaSqlDAO lResSqlDao = null;

		try {
			lResSqlDao = new ResidenzaSqlDAO(aConn);
			ResidenzaModel lResMod = new ResidenzaModel();
			lResSqlDao.ricercaResidenzaUltimaByProcedimentoSius(aIdFasSius, lTipo);
			lResMod = (ResidenzaModel) lResSqlDao.getModelByKey();
			if (lResMod != null) {
				lTreeResidenza = new TreeModel(lResMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Dati prelevati nel metodo prelevaDatiResidenza : " + lResMod);
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Nessun dato prelevato nel metodo prelevaDatiResidenza ");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiResidenza : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiResidenza : " + sqlEx);
		} finally {
			cleanup(lResSqlDao);
		}

		return lTreeResidenza;
	}

	/**
	 * Esegue il prelievo dati dell'Udienza.
	 *
	 * @param aIdUdienza
	 *            l'id dell'Udienza.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati dell'udienza come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiUdienza(BigDecimal aIdUdienza, Connection aConn) throws F3BException {

		TreeModel lTreeUdienza = null; // new TreeModel();
		UdienzaSqlDAO lUdiDao = null;

		try {
			lUdiDao = new UdienzaSqlDAO(aConn);
			lUdiDao.ricercaDattaglioUdienzaByKey(aIdUdienza);
			UdienzaModel lUdiMod = (UdienzaModel) lUdiDao.getModelByKey();
			if (lUdiMod != null)
				lTreeUdienza = new TreeModel(lUdiMod);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Dati prelevati nel metodo prelevaDatiUdienza : " + lUdiMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiUdienza : Non posso leggere : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiUdienza : Non posso leggere : " + sqlEx);
		} finally {
			cleanup(lUdiDao);
		}

		return lTreeUdienza;
	}

	/**
	 * Esegue il prelievo dati della struttura Evento - Notifiche.
	 *
	 * @param aIdEvento
	 *            l'id dell'Evento.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati dell' Evento-Notifiche come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiEventoNotifiche(BigDecimal aIdEvento, Connection aConn) throws F3BException {

		TreeModel lTreeEvento = null;
		EventoSqlDAO lEveDao = null;
		NotificaSqlDAO lNotDao = null;

		try {
			lEveDao = new EventoSqlDAO(aConn);
			lEveDao.ricercaEventoByKey(aIdEvento);
			EventoModel lEveMod = (EventoModel) lEveDao.getModelByKey();

			if (lEveMod != null) {
				lTreeEvento = new TreeModel(lEveMod);

				Vector lNotifiche = null;

				// Caricamento notifiche.
				lNotDao = new NotificaSqlDAO(aConn);
				lNotDao.ricercaNotificaByEvento(aIdEvento);
				lNotifiche = new Vector(lNotDao.getModels());

				// Inserimento di eventuali destinatari: Autorita Esterna,Ufficio, Avvocato Siep, Avvocato
				// Sius, CSSA.
				lNotifiche = getDestinatari(lNotifiche, lEveMod.getFasSiuIdFascicoloSius(), aConn);

				lTreeEvento = prelevaDatiNotificheDestinatari(lNotifiche, lTreeEvento, aConn);
				lTreeEvento = prelevaDatiCampoNota(lTreeEvento, aIdEvento, aConn);
				lTreeEvento = prelevaDatiMotivazioniDecreto(lTreeEvento, aIdEvento, aConn);

			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiEventoNotifiche : " + daoEx);
		} catch (SQLException sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiEventoNotifiche : " + sqlEx);
		} catch (Exception Ex) {
			throw new SIUSException("StampaController.prelevaDatiEventoNotifiche : " + Ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
		}

		return lTreeEvento;
	}

	/**
	 * Esegue il prelievo dati del Magistrato Relatore.
	 *
	 * @param aIdFasSius
	 *            l'id del Fascicolo SIUS.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati del Magistrato come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiMagistratoRelatore(BigDecimal aIdFasSius, Connection aConn)
			throws F3BException {

		TreeModel lTreeMagistrato = null; // new TreeModel();

		MagistratoRelatoreSqlDAO lMagRelSqlDao = null;
		MagistratoRelatoreModel lMagRelMod = null;

		EspertoSqlDAO lEspDao = null;
		EspertoModel lEspMod = null;

		MagistratoSqlDAO lMagDao = null;
		MagistratoModel lMagMod = null;

		try {

			// Magistrato relatore
			lMagRelSqlDao = new MagistratoRelatoreSqlDAO(aConn);
			lMagRelSqlDao.ricercaMagistratoRelatoreCorrenteByFascicolo(aIdFasSius);
			lMagRelMod = (MagistratoRelatoreModel) lMagRelSqlDao.getModelByKey();

			if (lMagRelMod != null) {
				// Magistrato
				if (lMagRelMod.getMagCodMagistrato() != null) {
					lMagDao = new MagistratoSqlDAO(aConn);
					lMagDao.ricercaMagistratoByCod(lMagRelMod.getMagCodMagistrato());
					lMagMod = (MagistratoModel) lMagDao.getModelByKey();
				}

				// Esperto
				if (lMagRelMod.getEspIdEsperto() != null) {
					lEspDao = new EspertoSqlDAO(aConn);
					lEspDao.ricercaEspertoByKey(lMagRelMod.getEspIdEsperto());
					lEspMod = (EspertoModel) lEspDao.getModelByKey();
				}
			}

			lTreeMagistrato = new TreeModel(lMagRelMod);

			if (lMagMod != null) // Magistrato
				lTreeMagistrato.add(new TreeModel(lMagMod));

			if (lEspMod != null) // Esperto
				lTreeMagistrato.add(new TreeModel(lEspMod));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Dati prelevati nel metodo prelevaDatiMagistratoRelatore : " + lMagRelMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"StampaController.prelevaDatiMagistratoRelatore : Non posso leggere : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException(
					"StampaController.prelevaDatiMagistratoRelatore : Non posso leggere : " + sqlEx);
		} finally {
			cleanup(lMagRelSqlDao);
			cleanup(lMagDao);
			cleanup(lEspDao);
		}
		return lTreeMagistrato;
	}

	/**
	 * Esegue il prelievo dati degli avvocati
	 *
	 * @param aIdFasSius
	 *            l'id del Fascicolo SIUS.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati degli avvocati come TreeModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	private Vector getAvvocatiFascicolo(BigDecimal aIdFasSius, Connection aConn) throws F3BException {

		AvvocatoFascicoloSiusSqlDAO lAvvSqlDao = null;
		Vector lAvvMod = new Vector();

		try {

			lAvvSqlDao = new AvvocatoFascicoloSiusSqlDAO(aConn);
			lAvvSqlDao.ricercaAvvocatiByFascicolo(aIdFasSius);

			lAvvMod = new Vector(lAvvSqlDao.getModels());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"##### Numero di Avvocati prelevati nel metodo prelevaDatiAvvocato : " + lAvvMod.size());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiAvvocato : Non posso leggere : " + daoEx);
		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIUSException("StampaController.prelevaDatiAvvocato : Non posso leggere : " + lEx);
		} finally {
			cleanup(lAvvSqlDao);
		}
		return lAvvMod;
	}

	/**
	 * Esegue la ricerca del Fascicolo-GeneraleProcedimentoSIUS.
	 *
	 * @param aIdFasSius
	 *            l'id del Fascicolo SIUS.
	 * @param aConn
	 *            connessione al dbase.
	 * @return FascicoloGPModel
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private FascicoloGPModel getFascicoloGPSius(BigDecimal aIdFasSius, Connection aConn) throws F3BException {

		FascicoloGPSqlDAO lFasGPSqlDao = null;
		FascicoloGPModel lFasGPModel = null;
		SoggettoSqlDAO lSogSqlDao = null;

		try {
			lFasGPSqlDao = new FascicoloGPSqlDAO(aConn);
			lSogSqlDao = new SoggettoSqlDAO(aConn);
			lFasGPSqlDao.ricercaFascicoloByKey(aIdFasSius);
			lFasGPModel = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("###### Dati prelevati da lFasGPModel " + lFasGPModel);

			// STUB 18/10/2004 - Cerca il soggetto associato al fascicolo
			SoggettoModel lSoggetto = null;
			lSogSqlDao = new SoggettoSqlDAO(aConn);
			lSogSqlDao.ricercaSoggettoByKey(lFasGPModel.getFascicoloSiusModel().getSogIdSoggetto());

			lSoggetto = new SoggettoModel((SoggettoModel) lSogSqlDao.getModelByKey());
			if (lSoggetto != null)
				lFasGPModel.getFascicoloSiusModel().setSoggetto(lSoggetto);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.getFascicoloGPSius : Non posso leggere : " + daoEx);
		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIUSException("StampaController.getFascicoloGPSius : Non posso leggere : " + lEx);
		} finally {
			cleanup(lFasGPSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSogSqlDao);
		}
		return lFasGPModel;
	}

	/**
	 * Ricerca Ufficio by Key
	 *
	 * @param aCodiceUfficio
	 * @return lUffMod
	 * @throws F3BException
	 */
	private XModel CreateRoot(String aCodiceUfficio, Connection aConn) throws F3BException {

		UfficioSqlDAO lUDao = null;
		UfficioModel lUffMod = null;
		UfficioModel lUffCAP = null;
		XModel lXMod = null;

		try {
			lUDao = new UfficioSqlDAO(aConn);

			lUDao.selUfficioByCod(aCodiceUfficio);
			lUffMod = (UfficioModel) lUDao.getModelByKey();

			if (lUffMod == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Ufficio inesistente");

			// Viene istanziato l' XLM
			lXMod = new XModel();

			lXMod.setTipoUfficioT1(lUffMod.getDescrTipoUfficio().toUpperCase());
			lXMod.setUfficio(lUffMod.getDescrComune().toUpperCase());
			lXMod.setIndirizzo(lUffMod.getIndirizzo());
			lXMod.setCap(lUffMod.getCap());
			lXMod.setFax(lUffMod.getFax());
			lXMod.setTelefono(lUffMod.getTelefono());
			lXMod.setEMail(lUffMod.getEMail());
			lXMod.setDataElaborazione(DateUtils.getSysDate());

			if (lUffMod.getCodDistretto() != null) {
				lUDao.selUfficioByCod(lUffMod.getCodDistretto());
				lUffCAP = (UfficioModel) lUDao.getModelByKey();
			}
			if (lUffCAP.getDescrComune() != null)
				lXMod.setUfficioCAP(lUffCAP.getDescrComune().toUpperCase());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.CreateRoot : Non posso leggere : " + daoEx);
		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIUSException("StampaController.CreateRoot : Non posso leggere : " + lEx);
		} finally {
			cleanup(lUDao);
		}

		return lXMod;
	}

	// private TemplateModel getTemplateByIdTemplate(String aIdTemplate, Connection aConn) throws F3BException
	// {
	//
	// TemplateSqlDAO lTemplateSqlDao = null;
	// TemplateModel lTemplateMod = null;
	//
	// try {
	// lTemplateSqlDao = new TemplateSqlDAO(aConn);
	// lTemplateSqlDao.ricercaTemplateByKey(aIdTemplate);
	// lTemplateMod = (TemplateModel) lTemplateSqlDao.getModelByKey();
	//
	// if (lTemplateMod == null)
	// throw new F3BException(F3BException.USER_MESSAGE,
	// "Template non trovato per l'IdTemplate richiesto " + aIdTemplate);
	// } catch (DAOException daoEx) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("DAOException: " + daoEx);
	// throw new F3BException("StampaController.getTemplateByIdTemplate: " + daoEx);
	// } catch (Exception lEx) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Exception: " + lEx);
	// throw new F3BException("StampaController.getTemplateByIdTemplate: " + lEx);
	// } finally {
	// cleanup(lTemplateSqlDao);
	// }
	//
	// return lTemplateMod;
	// }

	/**
	 * Esegue la ricerca di tenori per l'id del Generale procedimento.
	 *
	 * @param aKey
	 *            chaive id del generale procedimento.
	 * @return l'insime dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	private Vector getTenoriByGenProc(BigDecimal aKey, Connection aConn) throws F3BException {

		TenoreSqlDAO lTenDao = null;
		Vector lTenoriMod = new Vector();

		try {
			lTenDao = new TenoreSqlDAO(aConn);
			lTenDao.ricercaTenoreByGeneraleProc(aKey);

			lTenoriMod = new Vector(lTenDao.getModels());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Dati prelevati nel metodo ricercaTenoreByGeneraleProc : " + lTenoriMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"StampaController.ricercaTenoreByGeneraleProc : Non posso leggere : " + daoEx);
		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIUSException(
					"StampaController.ricercaTenoreByGeneraleProc : Non posso leggere : " + lEx);
		} finally {
			cleanup(lTenDao);
		}
		return lTenoriMod;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa del Luogo detenzione Corrente
	 *
	 * @param aKey
	 * @return dati del soggetto come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiLuogoDetenzioneSius(BigDecimal aIdFasSIUS, Connection aConn)
			throws F3BException {

		TreeModel lTreeLuogoDetenzione = null; // new TreeModel();
		LuogoDetenzioneSqlDAO lLuoDao = null;
		LuogoDetenzioneModel lLuoMod;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstModel = null;

		try {
			lLuoDao = new LuogoDetenzioneSqlDAO(aConn);
			lLuoDao.ricercaLuogoDetenzioneCorrenteByFascicoloSius(aIdFasSIUS);
			lLuoMod = (LuogoDetenzioneModel) lLuoDao.getModelByKey();
			if (lLuoMod != null) {
				if (lLuoMod.getIstDetIdIstitutoDetenzione() != null) {
					lIstDao = new IstitutoDetenzioneSqlDAO(aConn);
					lIstDao.ricercaIstitutoDetenzioneByKey(lLuoMod.getIstDetIdIstitutoDetenzione());
					lIstModel = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lLuoMod.setIstitutoDetenzione(lIstModel);
					lLuoMod.setDescrTipoIstituto(lLuoMod.getIstitutoDetenzione().getDescrTipoIstituto()
							+ " di " + lLuoMod.getIstitutoDetenzione().getDescrComune() + " - "
							+ lLuoMod.getIstitutoDetenzione().getIndirizzo());
					lLuoMod.setDescrLuogo(lLuoMod.getIstitutoDetenzione().getDescrComune());
				} else {
					lLuoMod.setDescrTipoIstituto(lLuoMod.getAltroLuogo());
				}
				lTreeLuogoDetenzione = new TreeModel(lLuoMod);
				lTreeLuogoDetenzione.add(new TreeModel(lIstModel));

			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Dati prelevati nel metodo prelevaDatiLuogoDetenzione : " + lLuoMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiLuogoDetenzione : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiLuogoDetenzione : " + sqlEx);
		} finally {
			cleanup(lLuoDao);
			cleanup(lIstDao);
		}

		return lTreeLuogoDetenzione;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa del Istituto detenzione
	 *
	 * @param aKey
	 *            Chiave del istituto detenzione
	 * @return dati del soggetto come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiIstitutoDetenzione(String aKey, Connection aConn) throws F3BException {

		TreeModel lTreeLuogoDetenzione = null; // new TreeModel();
		LuogoDetenzioneSqlDAO lLuoDao = null;
		LuogoDetenzioneModel lLuoMod;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstModel = null;

		try {
			lLuoDao = new LuogoDetenzioneSqlDAO(aConn);
			lLuoMod = new LuogoDetenzioneModel();
			if (aKey != null) {
				lIstDao = new IstitutoDetenzioneSqlDAO(aConn);
				lIstDao.ricercaIstitutoDetenzioneByKey(aKey);
				lIstModel = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
				lLuoMod.setIstitutoDetenzione(lIstModel);
				lLuoMod.setDescrTipoIstituto(lLuoMod.getIstitutoDetenzione().getDescrTipoIstituto() + " di "
						+ lLuoMod.getIstitutoDetenzione().getDescrComune() + " - "
						+ lLuoMod.getIstitutoDetenzione().getIndirizzo());
				lLuoMod.setDescrLuogo(lLuoMod.getIstitutoDetenzione().getDescrComune());

				lTreeLuogoDetenzione = new TreeModel(lLuoMod);
				lTreeLuogoDetenzione.add(new TreeModel(lIstModel));
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Dati prelevati nel metodo prelevaDatiLuogoDetenzione : " + lLuoMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiLuogoDetenzione : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiLuogoDetenzione : " + sqlEx);
		} finally {
			cleanup(lLuoDao);
			cleanup(lIstDao);
		}

		return lTreeLuogoDetenzione;
	}

	/**
	 * Esegue il prelievo dati esclusivamente del FascicoloSIUS senza ulteriori dati.
	 *
	 * @param lKeyFascicolo
	 *            chiave fascicolo SIEP.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati dell FascicoloSIEP come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiFascicoloSiepOnly(BigDecimal lKeyFascicolo, Connection aConn)
			throws F3BException {

		TreeModel lTreeFasMod = new TreeModel();

		FascicoloSiepSqlDAO lFasDao = null;

		if (lKeyFascicolo != null) {
			try {
				// Fascicolo
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### FASCICOLO SIEP");
				lFasDao = new FascicoloSiepSqlDAO(aConn);
				lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
				FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();
				lTreeFasMod = new TreeModel(lFasModel);
			} catch (DAOException daoEx) {
				daoEx.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DAOException: " + daoEx);
				throw new F3BException("StampaController.prelevaDatiFascicoloSiepOnly: " + daoEx);
			} catch (Exception sqe) {
				sqe.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Exception: " + sqe);
				throw new F3BException(
						"StampaController.prelevaDatiFascicoloSiepOnly: " + "Eccezione Generica: " + sqe);
			} finally {
				cleanup(lFasDao);
			}
		}
		return lTreeFasMod;
	}

	/**
	 * Esegue il prelievo dati del FascicoloSIEP.
	 *
	 * @param lKeyFascicolo
	 *            Chiave del Fascicolo SIEP.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati dell FascicoloSIEP come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	/*
	 * E' stata operata la seguente modifica: la classe diventa specializzata della classe
	 * SIAPStampaController in modo da poter chiamare in questo metodo la funzione
	 * appendTableToFascicoloSiep(...) che aggiunge al FascicoloSiep una serie di dati ad esso collegati; la
	 * ricerca di questi dati è stata quindi eliminata da questa funzione. Luigi 29-8-2007
	 */
	private TreeModel prelevaDatiFascicoloSiep(BigDecimal lKeyFascicolo, Connection aConn)
			throws F3BException {

		TreeModel lTreeFasMod = new TreeModel();

		Connection lConn = null;

		SoggettoModel lSoggetto = null;

		SoggettoSqlDAO lSogSqlDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		MisuraCautelareSqlDAO lMisDao = null;
		ReatoSqlDAO lReaDao = null;
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;
		PenaComplessivaSqlDAO lPenDao = null;
		PenaAccessoriaSqlDAO lPenAccDao = null;
		PosizioneGiuridicaSqlDAO lPosGiuDao = null;
		BeneficioSqlDAO lBenDao = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		MisuraSicurezzaSqlDAO lMisSicDao = null;
		PenaResiduaSqlDAO lPenaResDao = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Preleva dati fascicolo SIEP " + lKeyFascicolo);
		if (lKeyFascicolo != null) {
			try {
				// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
				if (aConn != null)
					lConn = aConn;
				else
					lConn = getDBConnection();

				// Fascicolo
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### FASCICOLO");
				lFasDao = new FascicoloSiepSqlDAO(lConn);
				lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
				FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();

				// Cerca il soggetto associato al fascicolo
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(lFasModel.getSogIdSoggetto());

				lSoggetto = new SoggettoModel((SoggettoModel) lSogSqlDao.getModelByKey());
				TreeModel lTreeSoggettoSiep = null;
				if (lSoggetto != null) {
					lFasModel.setSoggetto(lSoggetto);
					lTreeSoggettoSiep = new TreeModel(lSoggetto);
				}

				// Pena Residua
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Pena Residua");
				lPenaResDao = new PenaResiduaSqlDAO(lConn);
				// lPenaResDao.ricercaPenaResiduaForStampa(lKeyFascicolo);
				// ** Per la stampa viene considerata la Pena Residua corrente
				// ** associata al Fascicolo Siep
				lPenaResDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lKeyFascicolo);

				PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenaResDao.getModelByKey();
				// Stringa Arresto - REclusione
				if (lPenResMod != null) {
					lPenResMod.calcolaStringaReclusione();
					lPenResMod.calcolaStringaArresto();
				}

				// Crea TreeModel Fascicolo Siep
				lTreeFasMod = new TreeModel(lFasModel);
				lTreeFasMod.add(lTreeSoggettoSiep);

				// STUB 22/06/2006 Aggiunta sentenza per RichiestaAtti.
				TreeModel lTreeSentenza = prelevaDatiSentenza(lFasModel.getIdFascicoloSiep(), lConn);
				lTreeFasMod.add(lTreeSentenza);

				// Avvocati
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Avvocati");
				lAvvDao = new AvvocatoSiepxStampaSqlDAO(lConn);
				lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);
				Vector lAvvocati = new Vector(lAvvDao.getModels());

				// Add Avvocati per Fascicolo
				if (lAvvocati != null) {
					Iterator lItx = lAvvocati.iterator();
					while (lItx.hasNext()) {
						AvvocatoSiepModel lAvvModel = (AvvocatoSiepModel) lItx.next();
						TreeModel lTreeAvvMod = new TreeModel(lAvvModel.getAvvocato());
						lTreeFasMod.add(lTreeAvvMod);
						lTreeAvvMod.add(new TreeModel(lAvvModel.getAvvocatoFascicoloSiepModel()));
					}
				}

				// Aggiunge nodi di evento e notifica
				Vector lEventoMod = new Vector();
				EventoSqlDAO lEveDao = null;
				lEveDao = new EventoSqlDAO(aConn);
				lEveDao.ricercaEventoByIdFascicoloSiep(lKeyFascicolo);

				lEventoMod = new Vector(lEveDao.getModels());
				if (lEventoMod.size() != 0) {
					Iterator lItx = lEventoMod.iterator();
					while (lItx.hasNext()) {
						lTreeFasMod.add(new TreeModel((EventoModel) lItx.next()));
					}
				}

				// Metodo per aggiungere tutte le entità del Fascicolo SIEP Luigi 28-08-2007
				appendTableToFascicoloSiep(lConn, lFasModel.getIdFascicoloSiep(), lTreeFasMod, null);
			} catch (DAOException daoEx) {
				daoEx.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DAOException: " + daoEx);
				throw new F3BException("StampaController.prelevaDatiFascicoloSiep: " + daoEx);
			} catch (Exception sqe) {
				sqe.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Exception: " + sqe);
				throw new F3BException(
						"StampaController.prelevaDatiFascicoloSiep: Eccezione Generica: " + sqe);
			} finally {
				cleanup(lFasDao);
				cleanup(lSogSqlDao);
				cleanup(lReaDao);
				cleanup(lAltCauDao);
				cleanup(lAvvDao);
				cleanup(lBenDao);
				cleanup(lMisDao);
				cleanup(lPenDao);
				cleanup(lLuoDao);
				cleanup(lPosGiuDao);
				cleanup(lPenAccDao);
				cleanup(lMisSicDao);
				cleanup(lPenaResDao);

				// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
				if (aConn == null)
					cleanup(lConn);
			}
		}
		return lTreeFasMod;
	}

	/**
	 * Esegue il prelievo dati della Sentenza.
	 *
	 * @param aIdFascicoloSIEP
	 *            id del FascicoloSIEP.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della sentenza come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiSentenza(BigDecimal aIdFascicoloSIEP, Connection aConn) throws F3BException {

		TreeModel lTreeSenMod = new TreeModel();

		FascicoloSiepSqlDAO lFasDao = null;
		SentenzaSqlDAO lSenDao = null;

		if (aIdFascicoloSIEP != null) {
			try {
				// Fascicolo
				lFasDao = new FascicoloSiepSqlDAO(aConn);
				lFasDao.ricercaFascicoloByKey(aIdFascicoloSIEP);
				FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();

				// Sentenza
				lSenDao = new SentenzaSqlDAO(aConn);
				lSenDao.ricercaSentenzaBykey(lFasModel.getSenIdSentenza());
				SentenzaModel lSenModel = (SentenzaModel) lSenDao.getModelByKey();

				lTreeSenMod = new TreeModel(lSenModel);
			} catch (DAOException daoEx) {
				daoEx.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DAOException: " + daoEx);
				throw new F3BException("StampaController.prelevaDatiSentenza: " + daoEx);
			} catch (Exception sqe) {
				sqe.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Exception: " + sqe);
				throw new F3BException("StampaController.prelevaDatiSentenza: Eccezione Generica: " + sqe);
			} finally {
				cleanup(lSenDao);
				// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
				cleanup(lFasDao);
			}
		}
		return lTreeSenMod;
	}

	/**
	 * Esegue il prelievo dati DepositoOrdinanzaPC.
	 *
	 * @param aIdEvento
	 *            id del Evento.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della Ordinanza come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiDepositoOrdinanzaPC(BigDecimal aIdEvento, Connection aConn)
			throws F3BException {

		TreeModel lTreeOrdMod = new TreeModel();
		DepositoOrdinanzaPcModel lDepMod;

		try {
			// Si preferisce chiamare il Controller
			IDepositoOrdinanzaPc lDepCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lDepMod = lDepCtrl.ExRicercaDepositoOrdinanzaPcByEvento(aIdEvento);

			if (lDepMod != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("####Dati del Model Deposito Ord : " + lDepMod);
				lDepMod.setAnnoDataCameraConsiglio(
						DateUtils.getDateToString(lDepMod.getDataCameraConsiglio(), "yyyy"));
				lDepMod.setGiornoDataCameraConsiglio(
						DateUtils.getDateToString(lDepMod.getDataCameraConsiglio(), "dd"));
				lDepMod.setMeseDataCameraConsiglio(
						DateUtils.getDateToString(lDepMod.getDataCameraConsiglio(), "MMMM"));
				// Calcolo dei giorni concessi in semestri. Luigi 4-10-2005
				if (lDepMod.getNumGiorniLibanticipata() != null) {
					int lNumgiorni = lDepMod.getNumGiorniLibanticipata().intValue();
					int lNumSemestri = lNumgiorni / 45;
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("####Numero semestri per Liberazione Anticipata: " + lNumSemestri);
					lDepMod.setNumSemestri("" + lNumSemestri);
				}

				if (lDepMod.getCodTipoOrdinanza().equals("06") && lDepMod.getAutoritaVigilante() != null) {
					UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(lDepMod.getAutoritaVigilante());
					if (lUff != null) {
						lDepMod.setAutoritaVigilante(
								lUff.getDescrTipoUfficio() + " di " + lUff.getDescrComune());
					}
				}

				lTreeOrdMod = new TreeModel(lDepMod);

				// Ufficio competente memorizzato come Codice Ufficio TDS Concesso Riduzione.
				if (lDepMod.getCodUffTdsConcessoRiduzione() != null
						&& lDepMod.getCodUffTdsConcessoRiduzione().trim().length() > 1) {
					UfficioModel lUff = UfficioUtils
							.getUfficioByCodUfficio(lDepMod.getCodUffTdsConcessoRiduzione());
					UfficioTDSConcessoRiduzioneModel lUffTDSConcRid = new UfficioTDSConcessoRiduzioneModel(
							lUff);
					TreeModel lTreeUffTDSConcessoRiduzione = new TreeModel(lUffTDSConcRid);
					lTreeOrdMod.add(lTreeUffTDSConcessoRiduzione);
				}

				// Ufficio competente memorizzato come Magistrato Competente.
				if (lDepMod.getCodUfficioMagistratoComp() != null
						&& lDepMod.getCodUfficioMagistratoComp().trim().length() > 1) {
					UfficioModel lUff = UfficioUtils
							.getUfficioByCodUfficio(lDepMod.getCodUfficioMagistratoComp());
					TreeModel lTreeUfficio = new TreeModel(lUff);
					lTreeOrdMod.add(lTreeUfficio);
					// desUfficioCompetente = lUff.getDescrTipoUfficio() + " di " + lUff.getDescrComune();
				}

				// Prescrizioni
				addPrescrizioni(aIdEvento, aConn, lTreeOrdMod);

				// Liberazione Anticipata o Licenza o Reclamo Licenza
				// DL 92/2014 anche Violazione Cedu
				if (lDepMod.getCodTipoOrdinanza() != null && ((lDepMod.getCodTipoOrdinanza()
						.compareTo(ICostantiDepositoOrdinanzaPc.LIBERAZIONE_ANTICIPATA) == 0)
						|| (lDepMod.getCodTipoOrdinanza()
								.compareTo(ICostantiDepositoOrdinanzaPc.LICENZA) == 0)
						|| (lDepMod.getCodTipoOrdinanza()
								.compareTo(ICostantiDepositoOrdinanzaPc.RECLAMO_LICENZA) == 0)
						|| (lDepMod.getCodTipoOrdinanza()
								.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) == 0)))
					addLicenzaPeriodiLibAnt(aIdEvento, lTreeOrdMod);

				// Tenori dell'ordinanza
				ITenore lCtrlTen = SIUSLookupRemote.getTenoreRemote();
				Vector lTenori = lCtrlTen
						.ExRicercaTenoreByOrdinanzaOrderByPeso(lDepMod.getIdDepositoOrdinanzaPc());
				if (lTenori.size() != 0) {
					Iterator lItxTen = lTenori.iterator();
					while (lItxTen.hasNext()) {
						lTreeOrdMod.add(new TreeModel((TenoreModel) lItxTen.next()));
					}
				}
			}

			// Esecuzione Misura di Sicurezza,per id depositoOrdinanzaPC
			lTreeOrdMod.add(prelevaDatiEsecuzioneMSByIdOrdinanza(lDepMod.getIdDepositoOrdinanzaPc(), aConn));
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException(
					"StampaController.prelevaDatiDepositoOrdinanzaPC: Eccezione Generica: " + e);
		}

		return lTreeOrdMod;
	}

	/**
	 * Esegue il prelievo dati Prescrizioni.
	 *
	 * @param aIdEvento
	 *            id del Evento a cui sono legate le Prescrizioni.
	 * @param aConn
	 *            connessione al dbase.
	 * @param aTree
	 *            :TreeModel TreeModel a cui aggiungere i dati se trovati.
	 * @return void.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	private void addPrescrizioni(BigDecimal aIdEvento, Connection aConn, TreeModel aTree)
			throws F3BException {

		PrescrizioneSqlDAO lPreDao = null;
		Vector lPrescrizioni = null;

		try {
			lPreDao = new PrescrizioneSqlDAO(aConn);
			lPreDao.ricercaPrescrizioneByIdEve(aIdEvento);
			lPrescrizioni = new Vector(lPreDao.getModels());

			// Controllo per parse Prescrizioni

			// cerca l'Evento per prelevare l'ID del fascicolo SIUS
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoModel lEvento = lCtrlEve.ExRicercaEventoByKey(aIdEvento);

			if (lEvento != null) {
				// Cerca in Generale Procediemento per prelevare il Codice Oggetto Procedimento
				IGeneraleProcedimento lCtrlGenProc = SIUSLookupRemote.getGeneraleProcedimentoRemote();
				GeneraleProcedimentoModel lGenProcMod = lCtrlGenProc
						.ExRicercaGeneraleProcedimentoByFascicolo(lEvento.getFasSiuIdFascicoloSius());

				// Controllo per Sanzioni Sostitutive
				if (lGenProcMod != null && "U059".equals(lGenProcMod.getCodOggettoProcedimento())) {

					// Chiama la funzione di decodifica
					List parsePrescrizioni = DecodificheUtils.parsePrescrizioni(null, lPrescrizioni);

					lPrescrizioni = new Vector(parsePrescrizioni);
				}
			}

			Iterator lItx = lPrescrizioni.iterator();
			TreeModel lTreePreMod = null;

			while (lItx.hasNext()) {
				lTreePreMod = new TreeModel((PrescrizioneModel) lItx.next());
				aTree.add(lTreePreMod);
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("StampaController.addPrescrizioni: " + daoEx);
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("StampaController.addPrescrizioni: Eccezione Generica: " + e);
		} finally {
			cleanup(lPreDao);
		}

	}

	/**
	 * Esegue il prelievo dati Licenze e Periodi di Libertà Anticipata.
	 *
	 * @param aIdEvento
	 *            id del Evento.
	 * @param aTree
	 *            :TreeModel TreeModel a cui aggiungere i dati se trovati.
	 * @return void.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	private void addLicenzaPeriodiLibAnt(BigDecimal aIdEvento, TreeModel aTree) throws F3BException {

		TreeModel lTreeLicenza = null;
		// Licenze e Periodi di Liberazione Anticipata
		ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector lLicenzePeriodi = lCtrlDep.ExRicercaLicenzeLibanticipataByEve(aIdEvento);

		Iterator itx = lLicenzePeriodi.iterator();
		while (itx.hasNext()) {
			LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx.next();
			lTreeLicenza = new TreeModel(lLicPer.getLicenza());
			if (lLicPer != null && lLicPer.getPeriodi() != null) {
				PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
				for (int i = 0; i < lPeriodi.length; i++) {
					TreeModel lTreePeriodo = new TreeModel(lPeriodi[i]);
					lTreeLicenza.add(lTreePeriodo);
				}
			}
			if (lTreeLicenza != null)
				aTree.add(lTreeLicenza);
		}
	}

	/**
	 * Esegue il prelievo dati DepositoDecreto.
	 *
	 * @param aIdEvento
	 *            id del Evento.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della sentenza come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiDepositoDecreto(BigDecimal aIdEvento, Connection aConn) throws F3BException {

		TreeModel lTreeDepMod = new TreeModel();
		TreeModel lTreeIstDet = new TreeModel();

		DepositoDecretoSqlDAO lDepDao = null;
		DepositoDecretoModel lDepMod;
		Vector lMotivazioneDecreti = new Vector();
		MotivazioneDecretoSqlDAO lMotSqlDao = null;
		Vector lVector = null;

		try {
			// Si preferisce chiamare il Controller
			IDepositoDecreto lDepCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
			lDepMod = lDepCtrl.ExRicercaDepositoDecretoByIdEvento(aIdEvento);

			if (lDepMod != null) {
				lTreeDepMod = new TreeModel(lDepMod);

				// Motivazioni
				lMotSqlDao = new MotivazioneDecretoSqlDAO(aConn);
				lMotSqlDao.ricercaMotivazioneDecretoInammissibilitaByDepDec(lDepMod.getIdDepositoDecreto());
				lMotivazioneDecreti = new Vector(lMotSqlDao.getModels());

				if (lMotivazioneDecreti.size() == 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("##### Motivazioni assenti");
				} else {
					Iterator lItx = lMotivazioneDecreti.iterator();
					TreeModel lTreePreMod = null;
					while (lItx.hasNext()) {
						lTreePreMod = new TreeModel((MotivazioneDecretoModel) lItx.next());
						lTreeDepMod.add(lTreePreMod);
					}
				}

				// Prescrizioni
				addPrescrizioni(aIdEvento, aConn, lTreeDepMod);

				// Liberazione Anticipata o Licenza (Decreto Permesso)
				// Per semplicità si cercano sempre. Luigi 5-5-2005
				addLicenzaPeriodiLibAnt(aIdEvento, lTreeDepMod);

				if (lDepMod.getIstDetIdIstitutoDetenzione() != null
						&& lDepMod.getIstDetIdIstitutoDetenzione().trim().length() > 0) {
					lTreeIstDet = prelevaDatiIstitutoDetenzione(lDepMod.getIstDetIdIstitutoDetenzione(),
							aConn);
					lTreeDepMod.add(lTreeIstDet);
				}

				// Tenori del deposito decreto
				ITenore lTenore = SIUSLookupRemote.getTenoreRemote();
				lVector = lTenore.ExRicercaTenoreByDecretoOrderByPeso(lDepMod.getIdDepositoDecreto(), aConn);
				if (lVector.size() != 0) {
					Iterator lItx = lVector.iterator();
					while (lItx.hasNext())
						lTreeDepMod.add(new TreeModel(((TenoreModel) lItx.next())));
				}
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiDepositoDecreto: " + daoEx);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("StampaController.prelevaDatiDepositoDecreto: Eccezione Generica: " + sqe);
		} finally {
			cleanup(lDepDao);
			cleanup(lMotSqlDao);
		}

		return lTreeDepMod;
	}

	private Vector getDestinatari(Vector aNotifiche, BigDecimal lIdFasSius, Connection aConn)
			throws F3BException {

		Vector retNotifiche = new Vector();

		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;
		CSSASqlDAO lCSSASqlDao = null;
		AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = null;
		AvvocatoFascicoloSiusSqlDAO lAvvSiusDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		CuratoreSqlDAO lCurSqlDao = null; // 25/05/2011
		CuratoreSiusDAO lCurSiusDao = null; // 25/05/2011

		try {
			Iterator itx = aNotifiche.iterator();
			while (itx.hasNext()) {
				NotificaModel lNotifica = (NotificaModel) itx.next();

				// Autorita Esterne
				if (lNotifica.getAutEstIdAutoritaEsterna() != null) {
					lAutoritaSqlDao = new AutoritaEsternaSqlDAO(aConn);
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(lNotifica.getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lNotifica.setAutoritaEsterna(lAutorita);
				}
				// Preleva gli uffici
				if (lNotifica.getUffCodUfficio() != null
						&& lNotifica.getUffCodUfficio().compareTo("-") != 0) {
					lUffSqlDao = new UfficioSqlDAO(aConn);
					lUffSqlDao.selUfficioByCod(lNotifica.getUffCodUfficio());
					UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lNotifica.setUfficio(lUffMod);
				}
				// Preleva CSSA
				if (lNotifica.getCssIdCssa() != null) {
					lCSSASqlDao = new CSSASqlDAO(aConn);
					lCSSASqlDao.selCSSAByKey(lNotifica.getCssIdCssa());
					CSSAModel lCSSAMod = (CSSAModel) lCSSASqlDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lNotifica.setCSSA(lCSSAMod);
				}

				// Preleva gli avvocati SIEP
				if (lNotifica.getAvvIdAvvocatoFascicoloSiep() != null) {
					lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(aConn);

					lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(lNotifica.getAvvIdAvvocatoFascicoloSiep());
					lNotifica.setAvvSiep((AvvocatoSiepModel) lAvvDao.getModelByKey());
				}

				// Preleva gli avvocati SIUS
				if (lNotifica.getAvvIdAvvocatoFascicoloSius() != null) {
					lAvvSiusDao = new AvvocatoFascicoloSiusSqlDAO(aConn);
					lAvvSiusDao
							.ricercaAvvocatoByKeyAvvocatoFasSius(lNotifica.getAvvIdAvvocatoFascicoloSius());
					lNotifica.setAvvSius((AvvocatoSiusModel) lAvvSiusDao.getModelByKey());
				}

				// 25/05/2011 Preleva curatore/tutore SIUS
				if (lNotifica.getCurIdCuratore() != null) {
					CuratoreSiusModel lCuratore = null;
					lCurSiusDao = new CuratoreSiusDAO(aConn);
					lCurSiusDao.setCondizioneAttivo(lIdFasSius);
					lCuratore = (CuratoreSiusModel) lCurSiusDao.getModelByKey();
					if (lCuratore != null && lIdFasSius != null) {
						lCuratore.setDescrTipo(DecodificheUtils.getDescbyCode(
								DecodificheManager.getInstance().getTipoCuratore(), lCuratore.getFlagTipo()));
						lCurSqlDao = new CuratoreSqlDAO(aConn);
						lCurSqlDao.ricercaCuratoreByKey(lNotifica.getCurIdCuratore());
						CuratoreModel lCurMod = (CuratoreModel) lCurSqlDao.getModelByKey();

						lCuratore.setCuratore(lCurMod);
					}

					// Aggiunge il Curatore / Tutore al model di Notifica
					lNotifica.setCurSius(lCuratore);
				}

				// tipo istituto
				if (lNotifica.getIstDetIdIstitutoDetenzione() != null
						&& !lNotifica.getIstDetIdIstitutoDetenzione().equals("")) {
					lIstDao = new IstitutoDetenzioneSqlDAO(aConn);
					lIstDao.ricercaIstitutoDetenzioneByKey(lNotifica.getIstDetIdIstitutoDetenzione());
					IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lNotifica.setIstitutoDetenzione(lIstituto);
					lIstDao.stop();
				}

				retNotifiche.add(lNotifica);
			} // endwhile
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("StampaController.getDestinatari: " + daoEx);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("StampaController.getDestinatari: Eccezione Generica: " + sqe);
		} finally {
			cleanup(lAutoritaSqlDao);
			cleanup(lUffSqlDao);
			cleanup(lCSSASqlDao);
			cleanup(lAvvDao);
			cleanup(lAvvSiusDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lIstDao);
			cleanup(lCurSqlDao);
			cleanup(lCurSiusDao);
		}

		return retNotifiche;
	}

	private TreeModel iperPrelevaDati(BigDecimal aFasSIUS, int[] aTipoDati, TreeModel lTreeDati,
			Connection lConn) throws F3BException {

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lTreeResidenza = null;
		TreeModel lTreeDomicilio = null;
		TreeModel lTreeSentenza = null;
		TreeModel lTreeUdienza = null;
		TreeModel lTreeFascicoloSIEP = null;
		TreeModel lTreeLuogoDetenzione = null;
		TreeModel lTreeMagistrato = null;
		TreeModel lTreeRichiesteIstruttorie = null;
		TreeModel lTreeEsecuzioneMA = null;
		TreeModel lTreeEsecuzioneMS = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeGenProc = null;

		FascicoloGPModel lFasGP = null;

		Vector lAvvocati = new Vector();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("iperPrelevaDati : inizio");

		try {

			for (int i = 0; i < aTipoDati.length; i++) {
				// creazione delle varie foglie componenti del documento TreeModel
				switch (aTipoDati[i]) {
				case ICostantiStampaSius.TREE_SOGGETTO:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeResidenza = prelevaDatiSoggetto(lFasGP.getFascicoloSiusModel().getSogIdSoggetto(),
							aFasSIUS, lConn);
					lTreeDati.add(lTreeResidenza);
					break;

				case ICostantiStampaSius.TREE_RESIDENZA:
					lTreeResidenza = prelevaDatiResidenzaDomicilio(aFasSIUS, 'R', lConn);
					lTreeDati.add(lTreeResidenza);
					break;

				case ICostantiStampaSius.TREE_DOMICILIO:
					lTreeDomicilio = prelevaDatiResidenzaDomicilio(aFasSIUS, 'D', lConn);
					lTreeDati.add(lTreeDomicilio);
					break;

				case ICostantiStampaSius.TREE_FASCICOLOSIEP:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeFascicoloSIEP = prelevaDatiFascicoloSiepOnly(
							lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(), lConn);
					lTreeDati.add(lTreeFascicoloSIEP);
					break;

				// STUB Enzo 13/01/2006
				case ICostantiStampaSius.TREE_FASCICOLOSIEP_ALL:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeFascicoloSIEP = prelevaDatiFascicoloSiep(
							lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(), lConn);
					lTreeDati.add(lTreeFascicoloSIEP);
					break;

				// Titoli Esecutivi Referenziati (Fascicolo SIEP e Sentenza).
				// Enzo 19/01/2005
				case ICostantiStampaSius.TREE_TIT_ESE_REF:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeDati = prelevaDatiTitoliEsecutiviReferenziati(lTreeDati,
							lFasGP.getFascicoloSiusModel().getIdFascicoloSius(),
							lFasGP.getFascicoloSiusModel().getNumeroFascicoliUnificati(), lConn);
					break;

				// STUB 14/10/2004
				case ICostantiStampaSius.TREE_RIF_FAS_SIEP:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeDati = prelevaDatiRifasiep(lTreeDati,
							lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), lConn);
					break;

				case ICostantiStampaSius.TREE_SENTENZA:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeSentenza = prelevaDatiSentenza(
							lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(), lConn);
					lTreeDati.add(lTreeSentenza);
					break;

				case ICostantiStampaSius.TREE_ESECUZIONEMISURAALTERNATIVA:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeEsecuzioneMA = prelevaDatiEsecuzioneMA(
							lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), lConn);
					lTreeDati.add(lTreeEsecuzioneMA);
					break;

				case ICostantiStampaSius.TREE_ESECUZIONEMISURASICUREZZA:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeEsecuzioneMS = prelevaDatiEsecuzioneMS(
							lFasGP.getFascicoloSiusModel().getIdFascicoloSius(), lConn);
					lTreeDati.add(lTreeEsecuzioneMS);
					break;

				case ICostantiStampaSius.TREE_UDIENZA:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeUdienza = prelevaDatiUdienza(lFasGP.getGeneraleProcedimentoModel().getUdiIdUdienza(),
							lConn);
					lTreeDati.add(lTreeUdienza);
					break;

				case ICostantiStampaSius.TREE_AVVOCATO:
					lAvvocati = getAvvocatiFascicolo(aFasSIUS, lConn);
					if (lAvvocati != null) {
						if (lAvvocati.size() != 0) {
							Iterator lItx = lAvvocati.iterator();
							while (lItx.hasNext()) {
								lTreeDati.add(new TreeModel(((AvvocatoSiusModel) lItx.next()).getAvvocato()));
							}
						}
					}
					break;

				case ICostantiStampaSius.TREE_AVVOCATOSIUS:
					lAvvocati = getAvvocatiFascicolo(aFasSIUS, lConn);
					if (lAvvocati != null) {
						if (lAvvocati.size() != 0) {
							Iterator lItx = lAvvocati.iterator();
							while (lItx.hasNext()) {
								lTreeDati.add(new TreeModel(((AvvocatoSiusModel) lItx.next())));
							}
						}
					}
					break;

				case ICostantiStampaSius.TREE_MAGISTRATO:
					lTreeMagistrato = prelevaDatiMagistratoRelatore(aFasSIUS, lConn);
					lTreeDati.add(lTreeMagistrato);
					break;

				case ICostantiStampaSius.TREE_LUOGODET:
					lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSius(aFasSIUS, lConn);
					lTreeDati.add(lTreeLuogoDetenzione);
					break;

				case ICostantiStampaSius.TREEs_RICHIESTE_ISTRUTTORIE:
					lTreeRichiesteIstruttorie = new TreeModel();
					lTreeRichiesteIstruttorie = prelevaDatiNotificheByFascicoloSius(aFasSIUS, "05",
							lTreeRichiesteIstruttorie, lConn);
					lTreeDati.add(lTreeRichiesteIstruttorie);
					break;

				case ICostantiStampaSius.TREEs_PROVVEDIMENTI:
					lTreeDati = prelevaDatiEventoByFascicoloSius(aFasSIUS, COD_EVENTO_PROVVEDIMENTO,
							lTreeDati, lConn);
					break;

				case ICostantiStampaSius.TREEs_PROVVEDIMENTI_ALTRI:
					lTreeDati = prelevaDatiAltroEventoByFascicoloSius(aFasSIUS, COD_EVENTO_PROVVEDIMENTO,
							lTreeDati, lConn);
					break;

				case ICostantiStampaSius.TREE_FASCICOLOSIUS:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeFasSIUS = new TreeModel(lFasGP.getFascicoloSiusModel());
					aTipoDati[i] = 0;
					lTreeDati.add(iperPrelevaDati(lFasGP.getFascicoloSiusModel().getIdFascicoloSius(),
							aTipoDati, lTreeFasSIUS, lConn));
					break;

				case ICostantiStampaSius.TREE_GENERALEPROCEDIMENTO:
					if (lFasGP == null)
						lFasGP = getFascicoloGPSius(aFasSIUS, lConn);
					lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
					lTreeDati.add(lTreeGenProc);
					break;

				}
				aTipoDati[i] = 0;
			}
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreDatiFascicolo : fine");
		return lTreeDati;
	}

	private TreeModel prelevaDatiNotificheDestinatari(Vector lNotifiche, TreeModel aTree, Connection aConn)
			throws F3BException {

		NotificaModel lNotMod = null;
		TreeModel lTreeNotMod = null;
		TreeModel lTreeAutMod = null;
		TreeModel lTreeUffMod = null;
		TreeModel lTreeCSSAMod = null;
		TreeModel lTreeAvvSiepMod = null;
		TreeModel lTreeAvvSiusMod = null;
		TreeModel lTreeIstitutoDetenzione = null;

		try {
			// Creazione treemodel degli eventuali destinatari: Autorita Esterna,Ufficio, Avvocato Siep,
			// Avvocato Sius, CSSA.

			Iterator lItx = lNotifiche.iterator();

			while (lItx.hasNext()) {
				lNotMod = (NotificaModel) lItx.next();
				lTreeNotMod = new TreeModel(lNotMod);

				// Tutti gli attributi di NotificaModel con dignità di model vengono esplosi in XML.

				// Autorità Esterna.
				if (lNotMod.getAutoritaEsterna() != null) {
					lTreeAutMod = new TreeModel(lNotMod.getAutoritaEsterna());
					lTreeNotMod.add(lTreeAutMod);
				}

				// Ufficio.
				if (lNotMod.getUfficio() != null) {
					lTreeUffMod = new TreeModel(lNotMod.getUfficio());
					lTreeNotMod.add(lTreeUffMod);
				}

				// CSSA.
				if (lNotMod.getCSSA() != null) {
					lTreeCSSAMod = new TreeModel(lNotMod.getCSSA());
					lTreeNotMod.add(lTreeCSSAMod);
				}

				// Avvocato SIEP.
				if (lNotMod.getAvvSiep() != null) {
					lTreeAvvSiepMod = new TreeModel(lNotMod.getAvvSiep().getAvvocato());
					lTreeNotMod.add(lTreeAvvSiepMod);
				}

				// Avvocato SIUS.
				if (lNotMod.getAvvSius() != null) {
					lTreeAvvSiusMod = new TreeModel(lNotMod.getAvvSius().getAvvocato());
					lTreeNotMod.add(lTreeAvvSiusMod);
				}

				// 25/05/2011 Curatore SIUS.
				if (lNotMod.getCurIdCuratore() != null) {
					TreeModel lTreeCurMod = new TreeModel(lNotMod.getCurSius().getCuratore());
					TreeModel lTreeCurSiusMod = new TreeModel(lNotMod.getCurSius());
					lTreeCurSiusMod.add(lTreeCurMod);
					lTreeNotMod.add(lTreeCurSiusMod);
				}

				// Luogo Detenzione.
				if (lNotMod.getIstDetIdIstitutoDetenzione() != null) {
					lTreeIstitutoDetenzione = new TreeModel(lNotMod.getIstitutoDetenzione());
					lTreeNotMod.add(lTreeIstitutoDetenzione);
				}
				aTree.add(lTreeNotMod);
			} // endwhile
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException(
					"StampaController.prelevaDatiNotificheDestinatari: Eccezione Generica: " + sqe);
		}

		return aTree;
	}

	/*
	 * La funzione estrae dalla tabella CAMPO_NOTA i record collegati all'Evento e li aggiunge al TreeModel
	 * passato.
	 */
	private TreeModel prelevaDatiCampoNota(TreeModel aTree, BigDecimal aEventoKey, Connection aConn)
			throws Exception {

		// CampoNote
		CampoNotaSqlDAO lCampoNotaSqlDao = null;
		Vector lCampiNote = null;
		CampoNotaModel lCampoNota = null;
		TreeModel LTreeNota = null;

		try {
			// Ricerca Campi note collegate all'evento
			lCampoNotaSqlDao = new CampoNotaSqlDAO(aConn);
			lCampoNotaSqlDao.ricercaCampoNotaByKeyEvento(aEventoKey);
			lCampiNote = new Vector(lCampoNotaSqlDao.getModels());
			Iterator lItx = lCampiNote.iterator();
			while (lItx.hasNext()) {
				lCampoNota = (CampoNotaModel) lItx.next();
				LTreeNota = new TreeModel(lCampoNota);
				aTree.add(LTreeNota);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cleanup(lCampoNotaSqlDao);
		}
		return aTree;
	}

	/*
	 * La funzione estrae dalla tabella MOTIVAZIONI_DECRETO i record collegati all'Evento e li aggiunge al
	 * TreeModel passato.
	 */
	private TreeModel prelevaDatiMotivazioniDecreto(TreeModel aTree, BigDecimal aEventoKey, Connection aConn)
			throws Exception {

		// Motivazioni
		MotivazioneDecretoSqlDAO lMotSqlDao = new MotivazioneDecretoSqlDAO(aConn);
		Vector lMotivazioneDecreti = null;
		TreeModel lTreeMotivi = null;

		try {
			// Motivazioni
			lMotSqlDao.ricercaMotivazioneDecretoInammissibilitaByEve(aEventoKey);
			lMotivazioneDecreti = new Vector(lMotSqlDao.getModels());

			if (lMotivazioneDecreti.size() != 0) {
				Iterator lItx = lMotivazioneDecreti.iterator();
				while (lItx.hasNext()) {
					lTreeMotivi = new TreeModel((MotivazioneDecretoModel) lItx.next());
					aTree.add(lTreeMotivi);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cleanup(lMotSqlDao);
		}
		return aTree;
	}

	/**
	 * Metodo che recupera i dati di Periodo altra Sanzione afferenti all' ID Evento e li aggiunge. al
	 * TreeModel passato come parametro in modalità by reference.
	 *
	 * @param aIDEvento
	 *            ID dell' Evento corrente.
	 * @param aTree
	 *            TreeModel in cui inserire il dato, ossia l'evento.
	 * @param aConn
	 *            Connessione al dbase.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	private void prelevaDatiPeriodoAltraSanzioneByEvento(BigDecimal aIDEvento, TreeModel aTree,
			Connection aConn) throws F3BException {

		// Periodo Altra Sanzione.
		PeriodoAltraSanzioneSqlDAO lPeriodoAltraSanzSqlDao = new PeriodoAltraSanzioneSqlDAO(aConn);
		TreeModel lTreePeriodoAltraSanz = null;

		try {
			if (aIDEvento != null) {
				// Periodo Altra Sanzione.
				lPeriodoAltraSanzSqlDao.ricercaSanzioneSostitutivaByIdEvento(aIDEvento);
				lTreePeriodoAltraSanz = new TreeModel(lPeriodoAltraSanzSqlDao.getModelByKey());

				if (lTreePeriodoAltraSanz != null)
					aTree.add(lTreePeriodoAltraSanz);
			}
		} catch (Exception e) {
			throw new F3BException(
					"StampaController.prelevaDatiPeriodoAltraSanzione: Eccezione Generica: " + e);
		} finally {
			cleanup(lPeriodoAltraSanzSqlDao);
		}
	}

	/**
	 * Esegue il prelievo dati della struttura Evento - Notifiche.
	 *
	 * @param aFascKey
	 *            l'id del fascicolo.
	 * @param aTipoEvento
	 *            tipo evento da trovare.
	 * @param lConn
	 *            connessione al dbase.
	 * @return dati dell' Evento-Notifiche come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiNotificheByFascicoloSius(BigDecimal aFascKey, String aTipoEvento,
			TreeModel aTree, Connection aConn) throws F3BException {

		TreeModel lTreeNotifiche = aTree;
		Vector lNotifiche = null;
		EveNotificaSqlDAO lNotDao = null;

		try {
			lNotDao = new EveNotificaSqlDAO(aConn);
			lNotDao.ricercaEveNotificaByFascicoloSius(aFascKey, aTipoEvento);
			lNotifiche = new Vector(lNotDao.getModels());
			lTreeNotifiche = prelevaDatiNotificheDestinatari(lNotifiche, lTreeNotifiche, aConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiNotificheByFascicoloSius : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiNotificheByFascicoloSius : " + sqlEx);
		} finally {
			cleanup(lNotDao);
		}

		return lTreeNotifiche;
	}

	/**
	 * Esegue il prelievo dati della struttura Evento - Notifiche.
	 *
	 * @param aFascKey
	 *            l'id del fascicolo.
	 * @param aTipoEvento
	 *            tipo evento da trovare.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati dell' Evento-Notifiche come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiEventoByFascicoloSius(BigDecimal aFascKey, String aTipoEvento,
			TreeModel aTree, Connection aConn) throws F3BException {

		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lEveDao = new EventoSqlDAO(aConn);
			lEveDao.ricercaEventoByFascicoloSius(aFascKey, aTipoEvento);
			lEventi = new Vector(lEveDao.getModels());
			Iterator itx = lEventi.iterator();
			while (itx.hasNext()) {
				EventoModel lProv = (EventoModel) itx.next();
				aTree.add(new TreeModel(lProv));
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiEventoByFascicoloSius : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiEventoByFascicoloSius : " + sqlEx);
		} finally {
			cleanup(lEveDao);
		}

		return aTree;
	}

	/**
	 * Restituisce il TreeModel dell'Evento.
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public TreeModel ExPrelevaDatiEvento(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		TreeModel lTreeEvento = null;
		try {
			lConn = getDBConnection(); // connessione al Db
			lTreeEvento = prelevaDatiEvento(aEvento, lConn);
		} catch (F3BException e) {
			throw e;
		} catch (Exception ex) {
			throw (new F3BException(ex));
		} finally {
			cleanup(lConn);
		}
		return lTreeEvento;
	}

	/**
	 * Esegue il prelievo dati della struttura Evento - Notifiche - Provvedimenti.
	 *
	 * @param aEveKey
	 *            l'id dell'Evento.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati dell' Evento-Notifiche-Provvedimenti come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	// STUB 09/02/2005 Vincenzo.
	private TreeModel prelevaDatiEvento(EventoModel aEvento, Connection aConn) throws F3BException {

		TreeModel lTreeEvento = null;
		TreeModel lTreeOrdinanza = null;
		TreeModel lTreeDecreto = null;

		NotificaSqlDAO lNotDao = null;

		try {
			lTreeEvento = new TreeModel(aEvento);

			Vector lNotifiche = null;

			// Caricamento notifiche.
			lNotDao = new NotificaSqlDAO(aConn);
			lNotDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			lNotifiche = new Vector(lNotDao.getModels());

			// Inserimento di eventuali destinatari: Autorita Esterna,Ufficio, Avvocato Siep, Avvocato Sius,
			// CSSA.
			lNotifiche = getDestinatari(lNotifiche, aEvento.getFasSiuIdFascicoloSius(), aConn);

			lTreeEvento = prelevaDatiNotificheDestinatari(lNotifiche, lTreeEvento, aConn);
			lTreeEvento = prelevaDatiCampoNota(lTreeEvento, aEvento.getIdEvento(), aConn);
			lTreeEvento = prelevaDatiMotivazioniDecreto(lTreeEvento, aEvento.getIdEvento(), aConn);

			// Ricerca dei dati di DepositoOrdinanza o DepositoDecreto a seconda del tipo Evento.
			if (aEvento.getCodTipoProvvedimento().compareTo("03") == 0) {
				lTreeOrdinanza = prelevaDatiDepositoOrdinanzaPC(aEvento.getIdEvento(), aConn);
				lTreeEvento.add(lTreeOrdinanza);
			}
			// Dati di Deposito Decreto.
			if (aEvento.getCodTipoProvvedimento().compareTo("02") == 0) {
				lTreeDecreto = prelevaDatiDepositoDecreto(aEvento.getIdEvento(), aConn);
				lTreeEvento.add(lTreeDecreto);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiEvento : " + daoEx);
		} catch (SQLException sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiEvento : " + sqlEx);
		} catch (Exception Ex) {
			throw new SIUSException("StampaController.prelevaDatiEvento : " + Ex);
		} finally {
			cleanup(lNotDao);
		}

		return lTreeEvento;
	}

	/**
	 * Esegue il prelievo dati della struttura Evento - Notifiche.
	 *
	 * @param aFascKey
	 *            l'id del fascicolo.
	 * @param aTipoEvento
	 *            tipo evento da trovare.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati dell' Evento-Notifiche come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiAltroEventoByFascicoloSius(BigDecimal aFascKey, String aTipoEvento,
			TreeModel aTree, Connection aConn) throws F3BException {

		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lEveDao = new EventoSqlDAO(aConn);
			lEveDao.ricercaAltroEventoByFascicoloSius(aFascKey, aTipoEvento);
			lEventi = new Vector(lEveDao.getModels());
			Iterator itx = lEventi.iterator();
			while (itx.hasNext()) {
				EventoModel lProv = (EventoModel) itx.next();
				aTree.add(new TreeModel(lProv));
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaDatiAltroEventoByFascicoloSius : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException("StampaController.prelevaDatiAltroEventoByFascicoloSius : " + sqlEx);
		} finally {
			cleanup(lEveDao);
		}

		return aTree;
	}

	/**
	 * Preleva dati dai dao ed li organizza gerarchicamente.
	 *
	 * @param aIdFascicoloSius
	 * @param StampaModel
	 * @throws F3BException
	 */
	private TreeModel prelevaDati(BigDecimal aIdFascicoloSius, int[] aTipoDati, TreeModel lTreeDati,
			Connection lConn) throws F3BException {

		TreeModel lTreeRoot = lTreeDati;

		// SoggettoSqlDAO lSogSqlDao = null;
		FascicoloGPSqlDAO lFasGPSqlDao = null;
		// ResidenzaSqlDAO lResSqlDao = null;
		// PosizioneGiuridicaSqlDAO lPosGiuSqlDao = null;
		Vector lTenori = new Vector();
		TreeModel lTreeGenProc = null;

		try {
			// Fascicolo Generale Procedimento.
			lFasGPSqlDao = new FascicoloGPSqlDAO(lConn);
			lFasGPSqlDao.ricercaFascicoloByKey(aIdFascicoloSius);

			FascicoloGPModel lFasGPModel = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();
			if (lFasGPModel == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Errore: Procedimento SIUS non trovato");
			// /// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// /siesLogger.debug("Dati prelevati da lFasGPModel " + lFasGPModel );

			// Estrazione del FascicoloSius dal Model GP.
			FascicoloSiusModel lFasSiusModel = lFasGPModel.getFascicoloSiusModel();
			if (lFasSiusModel == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Errore: Fascicolo SIUS non trovato");
			// /// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// /siesLogger.debug("Dati prelevati da lFasSiusModel " + lFasSiusModel );

			// Estrazione del Generale procedimento model.
			GeneraleProcedimentoModel lGPModel = lFasGPModel.getGeneraleProcedimentoModel();
			if (lGPModel == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Errore: Generale Procedimento non trovato");
			// /// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// /siesLogger.debug("Dati prelevati da lGPModel " + lGPModel );

			lTreeGenProc = new TreeModel(lGPModel);

			// Tenori
			lTenori = getTenoriByGenProc(lGPModel.getIdGeneraleProcedimento(), lConn);
			if (lTenori.size() != 0) {
				Iterator lItxTen = lTenori.iterator();
				while (lItxTen.hasNext())
					lTreeGenProc.add(new TreeModel((TenoreModel) lItxTen.next()));
			}

			// Creazione dei tree Model figli di root.
			TreeModel lTreeFasSiusModel = new TreeModel(lFasSiusModel);

			// Remissioni Debito (agganciate al fascicolo sius)

			// Richiesta Remissione
			lTreeFasSiusModel = prelevaRichiesteRemissione(lTreeFasSiusModel,
					lFasGPModel.getFascicoloSiusModel().getIdFascicoloSius());

			// Misure Sicurezza (agganciate al fascicolo sius)

			// Richiesta Remissione
			lTreeFasSiusModel = prelevaMisureSicurezza(lTreeFasSiusModel,
					lFasGPModel.getFascicoloSiusModel().getIdFascicoloSius());

			// Si Mette in gerarchia il figlio di Fascicolo Sius
			// ovvero Generale Procedimento.
			if (lGPModel != null)
				lTreeFasSiusModel.add(lTreeGenProc);

			// Ricerca eventuale Cancelleria Assegnataria
			ICancAssFascSius lCancAssFascCtrl = SIUSLookupRemote.getCancAssFascSiusRemote();
			CancelleriaAssegnatariaModel lCancAssAttiva = lCancAssFascCtrl
					.ExRicercaCancAssFascSiusAttiva(aIdFascicoloSius);

			// Se esiste una Cancelleria Assegnataria per il fascicolo viene aggiunto il TreeModel
			if (lCancAssAttiva != null) {
				TreeModel lTreeCancelleriaAssegnataria = new TreeModel(
						new CancelleriaAssegnatariaModel(lCancAssAttiva));
				lTreeFasSiusModel.add(lTreeCancelleriaAssegnataria);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancelleria Assegnataria Definita");
			}

			// Colpo Finale :))
			lTreeRoot.add(lTreeFasSiusModel);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"FascicoloSiusController.ExStampaProcedimento: Non posso leggere : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException(
					"FascicoloSiusController.ExStampaProcedimanto: Non posso leggere : " + sqlEx);
		} finally {
			cleanup(lFasGPSqlDao);
		}

		return lTreeRoot;
	}

	/**
	 * Esegue il prelievo dati della Esecuzione Misura Alternativa.
	 *
	 * @param aIdFascicoloSIUS
	 *            id del FascicoloSIUS.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della sentenza come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiEsecuzioneMA(BigDecimal aKey, Connection aConn) throws F3BException {

		TreeModel lTreeEMCMod = new TreeModel();

		EsecuzioneMisuraAlternativaSqlDAO lEseDao = null;
		EsecuzioneMisuraAlternativaModel lEseMod;

		try {
			if (aKey != null) {
				lEseDao = new EsecuzioneMisuraAlternativaSqlDAO(aConn);
				lEseDao.ricercaEsecuzioneMisuraAlternativaByIdFascicolo(aKey);
				lEseMod = (EsecuzioneMisuraAlternativaModel) lEseDao.getModelByKey();
				lTreeEMCMod = new TreeModel(lEseMod);
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiEsecuzioneMA: " + daoEx);
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("StampaController.prelevaDatiEsecuzioneMA: Eccezione Generica: " + e);
		} finally {
			cleanup(lEseDao);
		}

		return lTreeEMCMod;
	}

	/**
	 * Esegue il prelievo dati della Esecuzione Misura Sicurezza.
	 *
	 * @param aIdFascicoloSIUS
	 *            id del FascicoloSIUS.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della sentenza come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiEsecuzioneMS(BigDecimal aKey, Connection aConn) throws F3BException {

		TreeModel lTreeEMCMod = new TreeModel();

		EsecuzioneMisuraSicurezzaSqlDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod;

		try {
			if (aKey != null) {
				lEseDao = new EsecuzioneMisuraSicurezzaSqlDAO(aConn);
				lEseDao.ricercaEsecuzioneMisuraSicurezzaByIdFascicolo(aKey);
				lEseMod = (EsecuzioneMisuraSicurezzaModel) lEseDao.getModelByKey();
				lTreeEMCMod = new TreeModel(lEseMod);
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiEsecuzioneMS: " + daoEx);
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("StampaController.prelevaDatiEsecuzioneMS: Eccezione Generica: " + e);
		} finally {
			cleanup(lEseDao);
		}

		return lTreeEMCMod;
	}

	/**
	 * Esegue il prelievo dati della Esecuzione Misura Sicurezza, attraverso l'id del DepositoOrdinanzaPC
	 *
	 * @param aKey
	 *            id Ordinanza.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della sentenza come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiEsecuzioneMSByIdOrdinanza(BigDecimal aKey, Connection aConn)
			throws F3BException {

		TreeModel lTreeEMCMod = new TreeModel();

		EsecuzioneMisuraSicurezzaSqlDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod;

		try {
			if (aKey != null) {
				lEseDao = new EsecuzioneMisuraSicurezzaSqlDAO(aConn);
				// lEseDao.ricercaEsecuzioneMisuraSicurezzaByIdFascicolo(aKey);
				lEseDao.ricercaEsecuzioneMisuraSicurezzaByIdOrdinanza(aKey);
				lEseMod = (EsecuzioneMisuraSicurezzaModel) lEseDao.getModelByKey();
				lTreeEMCMod = new TreeModel(lEseMod);
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiEsecuzioneMSByIdOrdinanza : " + daoEx);
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException(
					"StampaController.prelevaDatiEsecuzioneMSByIdOrdinanza : Eccezione Generica: " + e);
		} finally {
			cleanup(lEseDao);
		}

		return lTreeEMCMod;
	}

	/**
	 * Esegue il prelievo dati della Esecuzione Sanzione Sostitutiva.
	 *
	 * @param aIdFascicoloSIUS
	 *            id del FascicoloSIUS.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della sentenza come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiEsecuzioneSS(BigDecimal aKey, Connection aConn) throws F3BException {

		TreeModel lTreeESSMod = new TreeModel();

		EsecuzioneSanzioneSostitutivaSqlDAO lESSDao = null;
		PeriodoAltraSanzioneSqlDAO lPASqlDao = null;

		EsecuzioneSanzioneSostitutivaModel lESSMod;
		Vector lListaPAS = new Vector();

		try {
			if (aKey != null) {
				lESSDao = new EsecuzioneSanzioneSostitutivaSqlDAO(aConn);
				lESSDao.ricercaEsecuzioneSanzioneSostitutivaByIdFascicolo(aKey);
				lESSMod = (EsecuzioneSanzioneSostitutivaModel) lESSDao.getModelByKey();
				lTreeESSMod = new TreeModel(lESSMod);

				if (lESSMod != null) {
					// Controlla che ci sia l'ID del Fascicolo padre
					if (lESSMod.getGenPridGeneraleProcedimento() != null
							&& !"".equals(lESSMod.getGenPridGeneraleProcedimento().toString())) {
						// ricerca i Periodi Altra Sanzione legati All'ESECUZIONE SANZIONE SOSTITUTIVA
						lPASqlDao = new PeriodoAltraSanzioneSqlDAO(aConn);
						lPASqlDao.ricercaSanzioneSostitutivaByIdFascicolo(aKey, "DESC");
						lListaPAS = new Vector(lPASqlDao.getModels());

						if (lListaPAS.size() != 0) {
							Iterator lItxPAS = lListaPAS.iterator();
							while (lItxPAS.hasNext()) {
								// Aggiunge al TreeModel dell'ESECUZIONE ESECUZIONE SANZIONE SOSTITUTIVA i
								// PERIODI ALTRA SANZIONE
								lTreeESSMod.add(new TreeModel((PeriodoAltraSanzioneModel) lItxPAS.next()));
							}
						}
					}
				}
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiEsecuzioneSS: " + daoEx);
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("StampaController.prelevaDatiEsecuzioneSS: Eccezione Generica: " + e);
		} finally {
			cleanup(lESSDao);
			cleanup(lPASqlDao);
		}

		return lTreeESSMod;
	}

	/**
	 * STUB 11/10/2004 Si prelevano i dati di Riferimento Fascicolo Siep.
	 *
	 * @param aIdFascicoloSIUS
	 *            id del FascicoloSIUS.
	 * @param aConn
	 *            connessione al dbase.
	 * @return lTreeRFSMod dati dei Riferimento Fascicolo SIEP.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiRifasiep(TreeModel aTreeModel, BigDecimal aIdFascicoloSIUS, Connection aConn)
			throws F3BException {

		if (aTreeModel != null) {
			RiferimentoFascicoloSiepModel lRFSMod = new RiferimentoFascicoloSiepModel();
			lRFSMod.setFasSiuIdFascicoloSius(aIdFascicoloSIUS);
			RiferimentoFascicoloSiepSqlDAO lRifSqlDao = null;

			try {
				if (aIdFascicoloSIUS != null) {
					// Caricamento Riferimenti Fascicolo SIEP.
					lRifSqlDao = new RiferimentoFascicoloSiepSqlDAO(aConn);
					lRifSqlDao.ricercaRiferimentoFascicoloSiep(lRFSMod);
					Vector lRiFaSiep = new Vector(lRifSqlDao.getModels());

					if (lRiFaSiep.size() != 0) {
						Iterator lItx = lRiFaSiep.iterator();
						while (lItx.hasNext()) {
							RiferimentoFascicoloSiepModel lModel = ((RiferimentoFascicoloSiepModel) lItx
									.next());
							aTreeModel.add(new TreeModel(lModel));
						}
					}
				}
			} catch (DAOException daoEx) {
				daoEx.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DAOException: " + daoEx);
				throw new F3BException("StampaController.prelevaDatiRifasiep: " + daoEx);
			} catch (Exception sqe) {
				sqe.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Exception: " + sqe);
				throw new F3BException("StampaController.prelevaDatiRifasiep: Eccezione Generica: " + sqe);
			} finally {
				cleanup(lRifSqlDao);
			}
		} else
			throw new F3BException("StampaController.prelevaDatiRifasiep: TreeModel Fascicolo SIUS null !");
		return aTreeModel;
	}

	/**
	 * Preleva Dati (Fascicolo SIEP e Sentenza) dei Titoli Esecutivi referenziati a partire da un procedimento
	 * SIUS.
	 *
	 * @param aIdFascicoloSius
	 * @param aNumFascUnificati
	 * @param aConn
	 * @return aTreeModel
	 * @throws F3BException
	 */
	private TreeModel prelevaDatiTitoliEsecutiviReferenziati(TreeModel aTreeModel,
			BigDecimal aIdFascicoloSius, BigDecimal aNumFascUnificati, Connection aConn) throws F3BException {

		if (aTreeModel != null) {
			FascicoloSiepSqlDAO lFSiepSqlDao = null;
			SentenzaSqlDAO lSenSqlDao = null;
			PenaComplessivaSqlDAO lPenComDao = null;
			PenaResiduaSqlDAO lPenaResDao = null;

			try {
				lFSiepSqlDao = new FascicoloSiepSqlDAO(aConn);
				lSenSqlDao = new SentenzaSqlDAO(aConn);
				lFSiepSqlDao.ricercaFascicoliReferenziatiDaSIUS(aIdFascicoloSius, aNumFascUnificati);
				lFSiepSqlDao.start();

				FascicoloSiepModel lFSiepMod = new FascicoloSiepModel();
				// SentenzaModel lSenMod = new SentenzaModel();

				while (lFSiepSqlDao.next()) {
					// Estrazione Fascicolo SIEP.
					lFSiepMod = new FascicoloSiepModel((FascicoloSiepModel) lFSiepSqlDao.getModel());

					// Add del Fascicolo al TreeModel.
					aTreeModel.add(new TreeModel(lFSiepMod));

					// Estrazione Sentenza.
					lSenSqlDao = new SentenzaSqlDAO(aConn);
					lSenSqlDao.ricercaSentenzaBykey(lFSiepMod.getSenIdSentenza());
					SentenzaModel lSenModel = (SentenzaModel) lSenSqlDao.getModelByKey();
					// Add della Sentenza al TreeModel.
					aTreeModel.add(new TreeModel(lSenModel));

					// STUB 01/02/2005 Aggiunta di Pena Complessiva e Pena Residua.
					// Pena Complessiva
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("##### Pena Complessiva");
					lPenComDao = new PenaComplessivaSqlDAO(aConn);
					lPenComDao.ricercaPenaComplessivaByIdFascicolo(lFSiepMod.getIdFascicoloSiep());
					PenaComplessivaModel lPenComMod = (PenaComplessivaModel) lPenComDao.getModelByKey();
					// Stringa Arresto - Reclusione
					if (lPenComMod != null) {
						lPenComMod.calcolaStringaReclusione();
						lPenComMod.calcolaStringaArresto();
						// Add della Pena Complessiva al TreeModel.
						aTreeModel.add(new TreeModel(lPenComMod));
					}

					// Pena Residua
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("##### Pena Residua");
					lPenaResDao = new PenaResiduaSqlDAO(aConn);
					lPenaResDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lFSiepMod.getIdFascicoloSiep());

					PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenaResDao.getModelByKey();
					// Stringa Arresto - Reclusione
					if (lPenResMod != null) {
						lPenResMod.calcolaStringaReclusione();
						lPenResMod.calcolaStringaArresto();
						// Add della Pena Residua al TreeModel.
						aTreeModel.add(new TreeModel(lPenResMod));
					}
				}
				lFSiepSqlDao.stop();
			} catch (DAOException daoEx) {
				daoEx.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DAOException: " + daoEx);
				throw new F3BException("StampaController.prelevaTitoliEsecutiviReferenziati: " + daoEx);
			} catch (Exception sqe) {
				sqe.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Exception: " + sqe);
				throw new F3BException(
						"StampaController.prelevaTitoliEsecutiviReferenziati: Eccezione Generica: " + sqe);
			} finally {
				cleanup(lFSiepSqlDao);
				cleanup(lSenSqlDao);
				// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
				cleanup(lPenComDao);
				cleanup(lPenaResDao);
			}
		} else
			throw new F3BException(
					"StampaController.prelevaTitoliEsecutiviReferenziati: TreeModel Fascicolo SIUS null !");
		return aTreeModel;
	}

	/**
	 * La funzione utilizza il Controller di Udienza_procedimento per trovare l'id Udienza a partire dall'Id
	 * Evento. Se non trovato la funzione restituisce il valore null.
	 *
	 * @param aIdEvento
	 * @return lIdUdienza
	 * @throws BException
	 */

	private BigDecimal cercaIdUdienzaByTdEvento(BigDecimal aIdEvento) throws F3BException {

		BigDecimal lIdUdienza = null;
		UdienzaProcedimentoModel lUdienzaProcedimento = null;

		try {
			// Chiama il controller Udienza_Procedimento per risalire all'Udienza
			IUdienzaProcedimento lUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
			lUdienzaProcedimento = lUdiProCtrl.ExRicercaUdienzaProcedimentoByEve(aIdEvento);

			if (lUdienzaProcedimento != null)
				lIdUdienza = lUdienzaProcedimento.getUdiIdUdienza();
		} catch (Exception e) {
			throw new F3BException(getClass().getName() + ".cercaIdUdienzaByTdEvento: " + e);
		}

		return lIdUdienza;
	}

	private TreeModel prelevaDatiDocumentoOrdinanza(EventoNotificaModel aEvento, FascicoloGPModel lFasModel,
			String aCodUfficio) throws F3BException {

		TreeModel lTree = new TreeModel();

		FascicoloGPSqlDAO lFasDao = null;
		UdienzaSqlDAO lUdiDao = null;
		DepositoOrdinanzaPcSqlDAO lOrdDao = null;
		PrescrizioneSqlDAO lPreDao = null;
		TenoreSqlDAO lTenDao = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;

		Vector lTenoriGenPro = new Vector();

		Connection lConn = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("prelevaDatiDocumentoOrdinanza : inizio");
			lConn = getDBConnection();

			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			// STUB 20030926 : Patch Temporanea, quando non esiste un
			// fascicolo SIEP per un fascicolo SIUS evita di tirare giù tutti dati di
			// SIEP. per tanto crea solo la documentRoot.
			if (aEvento.getEvento().getFasSieIdFascicoloSiep() != null)
				lTree = lStampa.prelevaDatiEventoSiep(aEvento);
			else
				lTree = new TreeModel(createRootDocumentoOrdinanza(aEvento, lConn, aCodUfficio));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("prelevaDatiDocumentoOrdinanza Dopo prelevaDatiEventoSiep");

			// lKeyFascicolo = aEvento.getEvento().getFasSiuIdFascicoloSius();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("***** Presi Eventi *****");
			TreeModel lTreeFasMod = new TreeModel(lFasModel.getFascicoloSiusModel());
			TreeModel lTreeGenMod = new TreeModel(lFasModel.getGeneraleProcedimentoModel());

			// Si cerca l'udienza attraverso l'UDIENZA_PROCEDIMENTO
			BigDecimal lIdUdienza = cercaIdUdienzaByTdEvento(aEvento.getEvento().getIdEvento());
			if (lIdUdienza == null)
				lIdUdienza = lFasModel.getGeneraleProcedimentoModel().getUdiIdUdienza();
			// Udienza.
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaUdienzaByKey(lIdUdienza);
			// lUdiDao.ricercaUdienzaByDate(DateUtils.getDateToString(lFasModel.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"yyyyMMdd"),lFasModel.getGeneraleProcedimentoModel().getCodUfficioInserimento());
			UdienzaModel lUdienza = (UdienzaModel) lUdiDao.getModelByKey();

			if (lUdienza != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("####Dati del Model Udienza : " + lUdienza);

				TreeModel lTreeUdiMod = new TreeModel(lUdienza);
				lTreeFasMod.add(lTreeUdiMod);
			}

			lOrdDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lOrdDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aEvento.getEvento().getIdEvento());
			// lOrdDao.ricercaDepositoOrdinanzaPcByGenProcedimento(lFasModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("######Preleva dati Dep Ordinanza : ");
			DepositoOrdinanzaPcModel lOrd = (DepositoOrdinanzaPcModel) lOrdDao.getModelByKey();
			TreeModel lTreeOrdMod = null; // STUB 31/01/2005

			if (lOrd != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("####Dati del Model Deposito Ord : " + lOrd);
				lOrd.setAnnoDataCameraConsiglio(
						DateUtils.getDateToString(lOrd.getDataCameraConsiglio(), "yyyy"));
				lOrd.setGiornoDataCameraConsiglio(
						DateUtils.getDateToString(lOrd.getDataCameraConsiglio(), "dd"));
				lOrd.setMeseDataCameraConsiglio(
						DateUtils.getDateToString(lOrd.getDataCameraConsiglio(), "MMMM"));
				lTreeOrdMod = new TreeModel(lOrd);

				lPreDao = new PrescrizioneSqlDAO(lConn);
				/*
				 * Le prescrizioni sono collegate all'evento e non più al deposito ordinanza. Luigi 12-12-2003
				 */
				lPreDao.ricercaPrescrizioneByIdEve(aEvento.getEvento().getIdEvento());
				// lPreDao.ricercaPrescrizioneByDepOrdinanzaPc(lOrd.getIdDepositoOrdinanzaPc());
				Vector lPrescrizioni = new Vector(lPreDao.getModels());
				Iterator lItx = lPrescrizioni.iterator();
				TreeModel lTreePreMod = null;

				while (lItx.hasNext()) {
					lTreePreMod = new TreeModel((PrescrizioneModel) lItx.next());
					lTreeOrdMod.add(lTreePreMod);
				}
				// STUB 31/01/2005 lTreeFasMod.add(lTreeOrdMod);
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Deposito Ordinanza mancante !!!!");

			if (lOrd != null) {
				lTenDao = new TenoreSqlDAO(lConn);
				// Ricerca Tenori x ID DepositoOrdinanza Luigi 5-12-2003
				lTenDao.ricercaTenoriByOrdinanzaOrderByPeso(lOrd.getIdDepositoOrdinanzaPc());
				// lTenDao.ricercaTenoriByGeneraleProcOrderByPeso(
				// lFasModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() );
				Vector lTenori = new Vector(lTenDao.getModels());
				Iterator lItxTen = lTenori.iterator();
				TreeModel lTreeTenMod = null;

				while (lItxTen.hasNext()) {
					lTreeTenMod = new TreeModel((TenoreModel) lItxTen.next());
					// STUB 31/01/2005 sostituzione di TreeModel
					// lTreeFasMod.add(lTreeTenMod);
					lTreeOrdMod.add(lTreeTenMod);
				}
				lTreeFasMod.add(lTreeOrdMod); // STUB 31/01/2005
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("I Tenori non possono essere cercati perchè Ordinanza mancante !!!!");

			// Tenori
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoreByGeneraleProc(
					lFasModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenoriGenPro = new Vector(lTenDao.getModels());
			if (lTenoriGenPro.size() != 0) {
				Iterator lItxTen = lTenoriGenPro.iterator();
				while (lItxTen.hasNext())
					lTreeGenMod.add(new TreeModel((TenoreModel) lItxTen.next()));
			}

			// Preleva altri dati del fascicolo
			// Riempi l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSius.TREE_SOGGETTO, ICostantiStampaSius.TREE_MAGISTRATO,
					ICostantiStampaSius.TREE_LUOGODET, ICostantiStampaSius.TREE_TIT_ESE_REF // STUB 19/01/2005
					, ICostantiStampaSius.TREE_RIF_FAS_SIEP }; // STUB 14/10/2004
			// Crea il TreeModel con i dati che occorrono
			lTreeFasMod = ExAggiungiDatiStampa(lFasModel.getFascicoloSiusModel().getIdFascicoloSius(),
					aTipoDati, lTreeFasMod);

			lTree.add(lTreeFasMod);
			lTree.add(lTreeGenMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("prelevaDatiDocumentoOrdinanza: Non posso leggere : " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("prelevaDatiDocumentoOrdinanza: Non posso leggere  : " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lUdiDao);
			cleanup(lOrdDao);
			cleanup(lPreDao);
			cleanup(lLuoDao);
			cleanup(lTenDao);
			cleanup(lSogSqlDao);
			cleanup(lResSqlDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("prelevaDatiDocumentoOrdinanza : fine");
		return lTree;
	}

	/**
	 * Crea la root del Documento di Stampa Modello Ordinanza Questa funzione fa uso della CreateRoot() per
	 * creare l'intestazione standard del documento di stampa. Poi però sull modello standard creato (XModel)
	 * effettua delle modifiche riguardo la descrizione dell'Ufficio.
	 *
	 * @param aEveModel
	 * @param Connection
	 *            aConn;
	 * @param String
	 *            aCodUfficio.
	 * @return lStampa
	 */
	private XModel createRootDocumentoOrdinanza(EventoNotificaModel aEveModel, Connection aConn,
			String aCodUfficio) throws F3BException {

		XModel lStampa = CreateRoot(aCodUfficio, aConn);

		String descrTipoUff = aEveModel.getEvento().getDescrUfficioEmittente().toUpperCase();
		lStampa.setUfficio(aEveModel.getEvento().getDescrLuogoEmittente().toUpperCase());
		lStampa.setTipoUfficio(descrTipoUff);

		if (descrTipoUff != null) {
			if (descrTipoUff.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
			}
			// STUB 01/02/2005 Patch x Valorizzare TipoUfficioT1.
			else
				lStampa.setTipoUfficioT1(descrTipoUff);
		}
		return lStampa;
	}

	// /////////////////////////// Luigi 24-3-06
	/**
	 * Genera il ByteArrayOutputStream per la stampa dell'Elenco del Numero di procedimenti fissati ai vari
	 * Magistrati Relatori per Udienze in un certo intervallo di Date..
	 *
	 * @param UdienzaModel
	 *            .
	 * @return ByteArrayOutputStream.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaUdienzeMagistratiProcedimenti(UdienzaModel aUdienza,
			XModel aStampaMod, String aIdTemplate) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaUdienzeMagistratiProcedimenti : inizio");
		// Costruzione del TreeModel
		TreeModel lTree = prelevaDatiUdienzeMagistratiProcedimenti(aUdienza, aStampaMod);

		// Individuazione del file Template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		// Generazione del Report XML
		ReportGenerator lReport = new ReportGenerator(aUdienza.getCodUfficioAppartenenza());
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaUdienzeMagistratiProcedimenti : fine");
		return lByteArrayOut;
	}

	private TreeModel prelevaDatiUdienzeMagistratiProcedimenti(UdienzaModel aUdienza, XModel aStampaMod)
			throws F3BException {

		// Intestazione del documento
		TreeModel lTreeRoot = new TreeModel(aStampaMod);

		IUdienzaProcedimento CtrlFasGP = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		Collection lLista = CtrlFasGP.ExRicercaUdienzeMagistratiProcedimentiByDate(aUdienza);

		if (lLista != null) {
			Iterator lItx = lLista.iterator();
			while (lItx.hasNext()) {
				UdienzaMagistratoRelModel lUdiMag = (UdienzaMagistratoRelModel) lItx.next();
				// UdienzaModel lUdienza = new UdienzaModel(( UdienzaModel)lUdiMag) ;
				TreeModel lTreeUdienza = new TreeModel(lUdiMag);
				int lNumMagistrati = lUdiMag.getNumMagistrati();
				for (int i = 0; i < lNumMagistrati; i++) {
					// TreeModel lTreeMagistrato = new TreeModel(lUdiMag.getMagistrato(i));
					lTreeUdienza.add(new TreeModel(lUdiMag.getMagistrato(i)));
				}
				lTreeRoot.add(lTreeUdienza);
			}
		}
		return lTreeRoot;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa dell'Elenco di Procedimenti SIUS per estremi
	 * Provvedimenti
	 *
	 * @param aFiltroRicerca
	 *            (RicercaOrdinanzaModel)
	 * @param aElenco
	 *            (Vector di EveFasGepSogProvModel),
	 * @param lUtenteModel
	 *            (Model di UtenteModel).
	 * @return ByteArrayOutputStream.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaProcSiusXProv(RicercaOrdinanzaModel aFiltroRicerca,
			Vector aElenco, UtenteModel aUtente) throws F3BException {

		// Connessione al DB per il prelievo dei dati.
		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot = null; // radice dell'albero generale del documento
		String lIdTemplate;

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione dell'Ufficio documento
			XModel lBase = CreateRoot(aUtente.getUfficioUtente().getCodUfficio(), lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			// Intestazione del documento XML
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(aUtente));
			lTreeRoot.add(new TreeModel(aFiltroRicerca));

			// Iterazione della lista di Notifiche
			Iterator lItx = aElenco.iterator();
			EveFasGepSogProvModel lProcCorr = null;

			IImpugnazione lImpCtrl = SIUSLookupRemote.getImpugnazioneRemote();
			while (lItx.hasNext()) {
				lProcCorr = (EveFasGepSogProvModel) lItx.next();

				TreeModel lTreeProcedimento = new TreeModel(lProcCorr);

				// ramo relativo all'evento
				if (lProcCorr.getEvento() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getEvento()));
				// ramo relativo al Fascicolo SIUS
				if (lProcCorr.getFascicoloSius() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getFascicoloSius()));
				// Ramo Ordinanza
				if (lProcCorr.getDepositoOrdinanzaPc() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getDepositoOrdinanzaPc()));
				// Ramo Sentenza
				if (lProcCorr.getDepositoSentenza() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getDepositoSentenza()));
				// Decreto
				if (lProcCorr.getDepositoDecreto() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getDepositoDecreto()));
				// Impugnazione
				if (lProcCorr.getImpugnazione() != null) {
					// Preleva l'ImpugnazioneModel
					ImpugnazioneModel lImpModel = null;
					lImpModel = lImpCtrl
							.ExRicercaImpugnazioneByKey(lProcCorr.getImpugnazione().getIdImpugnazione());
					lTreeProcedimento.add(new TreeModel(lImpModel));
				}
				// DocumentoAllegato (Foglio Complementare)
				if (lProcCorr.getDocumentoAllegato() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getDocumentoAllegato()));

				// Viene aggiunto il ramo relativo al Soggetto
				if (lProcCorr.getSoggetto() != null)
					if (lProcCorr.getFascicoloSius() != null)
						lTreeProcedimento.add(prelevaDatiSoggetto(lProcCorr.getSoggetto().getIdSoggetto(),
								lProcCorr.getFascicoloSius().getIdFascicoloSius(), lConn));
					else
						lTreeProcedimento.add(new TreeModel(lProcCorr.getSoggetto()));

				lTreeRoot.add(lTreeProcedimento);
			}
		} catch (Exception lEx) {
			throw new SIUSException("StampaController.ExPreStampaProcSiusXProv : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// Viene istanziato il Report Generator
		ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());

		lIdTemplate = "";

		if (aFiltroRicerca.isRicercaXOrdinanza())
			lIdTemplate = "SIUS_ST_007";
		else if (aFiltroRicerca.isRicercaXDecreto())
			lIdTemplate = "SIUS_ST_008";
		else if (aFiltroRicerca.isRicercaXImpugnazioneRicorso())
			lIdTemplate = "SIUS_ST_009";
		else if (aFiltroRicerca.isRicercaXFoglioComplementare())
			lIdTemplate = "SIUS_ST_010";
		else if (aFiltroRicerca.isRicercaXSentenza())
			lIdTemplate = "SIUS_ST_012";

		// Si ricava il Nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * Esegue il prelievo dati dell' Esecuzione Sanzione Sostitutiva
	 *
	 * @param aTreeFasSius
	 *            Tre model del Fascicolo SIUS.
	 * @param aIdGenProc
	 *            l'ID del Generale Procedimento
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati dell'esecuzione come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaEsecuzioneSanzioneSostitutiva(TreeModel aTreeFasSius,
			BigDecimal aIdFascicoloSIUS, Connection aConn) throws F3BException {

		TreeModel lTreeESS = null; // new TreeModel();

		Vector lListaPAS = new Vector();

		EsecuzioneSanzioneSostitutivaSqlDAO lESSqlDao = null;
		PeriodoAltraSanzioneSqlDAO lPASqlDao = null;

		try {
			lESSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(aConn);
			lESSqlDao.ricercaEsecuzioneSanzioneSostitutivaByIdFascicoloFiglio(aIdFascicoloSIUS);

			EsecuzioneSanzioneSostitutivaModel lESSModel = (EsecuzioneSanzioneSostitutivaModel) lESSqlDao
					.getModelByKey();
			lTreeESS = new TreeModel(lESSModel);

			if (lESSModel != null) {
				// aTreeFasSius.add(new TreeModel(lESSModel));
				aTreeFasSius.add(lTreeESS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"##### Dati ESECUZINE SANZIONE SOSTITUTIVA prelevati nel metodo prelevaEsecuzioneSanzioneSostitutiva : "
								+ lESSModel);

				// Controlla che ci sia l'ID del Fascicolo padre
				if (lESSModel.getGenPridGeneraleProcedimento() != null
						&& !"".equals(lESSModel.getGenPridGeneraleProcedimento().toString())) {
					// ricerca i Periodi Altra Sanzione legati All'ESECUZIONE SANZIONE SOSTITUTIVA
					lPASqlDao = new PeriodoAltraSanzioneSqlDAO(aConn);
					lPASqlDao.ricercaSanzioneSostitutivaByIdFascicolo(
							lESSModel.getGenPridGeneraleProcedimento());
					lListaPAS = new Vector(lPASqlDao.getModels());

					if (lListaPAS.size() != 0) {
						Iterator lItxPAS = lListaPAS.iterator();
						while (lItxPAS.hasNext()) {
							// Aggiunge al TreeModel dell'ESECUZIONE ESECUZIONE SANZIONE SOSTITUTIVA i periodi
							// altra sanzione
							lTreeESS.add(new TreeModel((PeriodoAltraSanzioneModel) lItxPAS.next()));
						}
					}
				}
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### metodo prelevaEsecuzioneSanzioneSostitutiva : Dati non presenti");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("StampaController.prelevaEsecuzioneSanzioneSostitutiva : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIUSException("StampaController.prelevaEsecuzioneSanzioneSostitutiva : " + sqlEx);
		} finally {
			cleanup(lESSqlDao);
			cleanup(lPASqlDao);
		}
		return aTreeFasSius;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa del Foglio Complementare. Viene ricercato l'Evento
	 * collegato al DocumentoAllegato passato; se si tratta di un'ordinanza viene richiamata la funzione di
	 * PreStampa dell'Ordinanza, se si tratta di un Decreto quella del Decreto. La funzione fissa anche il
	 * template da usare.
	 *
	 * @param aDocAll
	 * @param aCodUff
	 * @param aUtenteModel
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExPreStampaFoglioComplementare(DocumentoAllegatoModel aDocAll,
			String aCodUff, UtenteModel aUtenteModel) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Preleva l'Evento
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEvento = lCtrlEve.ExRicercaEventoByKey(aDocAll.getEveIdEvento());

		// MERGE v10: aggiunto controllo per diversificare le stampe
		boolean tipologia = aUtenteModel.getUserProfile().isSiep();
		// Viene definito il Template da usare nella stampa
		if (tipologia)
			lEvento.setTemIdTemplate("SIEP_ST_002");
		else
			lEvento.setTemIdTemplate("SIUS_ST_003");

		// Generazione della stampa
		// MERGE v10: aggiunta sentenza
		if (lEvento.getCodTipoProvvedimento().equals("01"))
			lByteArrayOut = ExPreStampaEmissioneSentenza(lEvento, aCodUff, aUtenteModel, aDocAll);
		else if (lEvento.getCodTipoProvvedimento().equals("02"))
			lByteArrayOut = ExPreStampaEmissioneDecreto(lEvento, aCodUff, aUtenteModel, aDocAll);
		else if (lEvento.getCodTipoProvvedimento().equals("03"))
			lByteArrayOut = ExPreStampaEmissioneOrdinanza(lEvento, aCodUff, aUtenteModel, aDocAll);
		else if (lEvento.getCodTipoProvvedimento().equals("04"))
			lByteArrayOut = ExPreStampaFoglioComplementareNsc(lEvento, aCodUff, aUtenteModel, aDocAll);
		else
			throw new F3BException(F3BException.USER_MESSAGE, "Tipo di Provvedimento non previsto");

		return lByteArrayOut;
	}

	public ByteArrayOutputStream ExPreStampaProvvedimentiPermessiLicenza(
			CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca, UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		ByteArrayOutputStream lByteArrayOut = null;
		TreeModel lTreeRoot = null;
		PermessoSqlDAO lPermSqlDao = null;
		ProvvedimentoPermessoLicenzaModel lModel = null;

		try {
			lConn = getDBConnection();
			// Intestazione dell'Ufficio documento.
			XModel lBase = CreateRoot(aUtente.getUfficioUtente().getCodUfficio(), lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>Root Base : " + lBase);

			// Intestazione del documento XML
			lTreeRoot = new TreeModel(lBase);

			lTreeRoot.add(new TreeModel(aUtente));
			lTreeRoot.add(new TreeModel(aCriteriRicerca));

			lPermSqlDao = new PermessoSqlDAO(lConn);

			// Si Ricavano i Totali e li si inseriscono nel treemodel
			TotaliPermessiLicenzeModel lTotali = new TotaliPermessiLicenzeModel();

			StringTokenizer lStrToken = new StringTokenizer(aCriteriRicerca.getCodMotivo(), ",");
			while (lStrToken.hasMoreTokens()) {
				String lCodMotivo = lStrToken.nextToken();
				int lNum = lPermSqlDao.getNumProvvedimentiPermessiLicenze(
						aCriteriRicerca.getDataDepositoIniziale(), aCriteriRicerca.getDataDepositoFinale(),
						lCodMotivo, aCriteriRicerca.getCodUfficio());
				if (lCodMotivo.equals("2020"))
					lTotali.setNumPP(lNum);
				else if (lCodMotivo.equals("2021"))
					lTotali.setNumPN(lNum);
				else if (lCodMotivo.equals("2025"))
					lTotali.setNumLC(lNum);
			}

			lTreeRoot.add(new TreeModel(lTotali));

			// Si recupera l'elenco.
			lPermSqlDao.ricercaProvvedimentiPermessiLicenze(aCriteriRicerca.getDataDepositoIniziale(),
					aCriteriRicerca.getDataDepositoFinale(), aCriteriRicerca.getCodMotivo(),
					aCriteriRicerca.getCodUfficio());
			lPermSqlDao.start();

			while (lPermSqlDao.next()) {
				lModel = (ProvvedimentoPermessoLicenzaModel) lPermSqlDao
						.getProvvedimentoPermessoLicenzaModel();

				TreeModel lTreeProvv = new TreeModel(lModel);

				// Ramo afferente al Fascicolo SIUS.
				if (lModel.getFascicolo() != null)
					lTreeProvv.add(new TreeModel(lModel.getFascicolo()));

				// Ramo afferente all'evento.
				if (lModel.getEvento() != null)
					lTreeProvv.add(new TreeModel(lModel.getEvento()));

				// Ramo afferente al Deposito Decreto.
				if (lModel.getDepositoDecreto() != null)
					lTreeProvv.add(new TreeModel(lModel.getDepositoDecreto()));

				// Ramo afferente alla licenza.
				if (lModel.getLicenza() != null)
					lTreeProvv.add(new TreeModel(lModel.getLicenza()));

				// Ramo afferente al Soggetto.
				if (lModel.getSoggetto() != null)
					lTreeProvv.add(new TreeModel(lModel.getSoggetto()));

				// Ramo afferente all'Istituto di Detenzione.
				if (lModel.getIstitutoDetenzione() != null)
					lTreeProvv.add(new TreeModel(lModel.getIstitutoDetenzione()));

				lTreeRoot.add(lTreeProvv);
			}

			lPermSqlDao.stop();

			// Viene istanziato il Report Generator
			ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());

			String lIdTemplate = "SIUS_ST_011";

			// Si ricava il Nome del template
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(">> NOME TEMPLATE : " + lNomeTemplate);

			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);
		} catch (Exception ex) {
			throw new SIUSException("StampaController.ExPreStampaProvvedimentiPermessiLicenze : " + ex);
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Effettua una ricerca in RICHIESTA_CONVERSIONE dei record legati al Fascicolo SIUS e li aggiunge al
	 * TreModel passato come parametro.
	 *
	 * @param aTreeFasSius
	 * @param aIdFascicoloSIUS
	 * @return
	 * @throws F3BException
	 */
	private TreeModel prelevaRichiesteConversione(TreeModel aTreeFasSius, BigDecimal aIdFascicoloSIUS)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug("##### Ricerca Richiesta Conversione per Fascicolo SIUS con ID : " + aIdFascicoloSIUS);

		if (aIdFascicoloSIUS != null) {
			IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
			RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
			lRicMod.setFasSiuIdFascicoloSius(aIdFascicoloSIUS);
			Vector lRichiestaConversioni = lCtrlRic.ExRicercaRichiestaConversione(lRicMod);

			if (lRichiestaConversioni != null && lRichiestaConversioni.size() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("##### Num. Richieste Conversione trovate : " + lRichiestaConversioni.size());

				Iterator lItxRic = lRichiestaConversioni.iterator();
				while (lItxRic.hasNext())
					aTreeFasSius.add(new TreeModel(((RichiestaConversioneModel) lItxRic.next())));
			}
		}

		return aTreeFasSius;
	}

	/**
	 * Effettua una ricerca in MISURA_SICUREZZA dei record legati al Fascicolo SIUS e li aggiunge al TreeModel
	 * passato come parametro.
	 *
	 * @param aTreeFasSius
	 * @param aIdFascicoloSIUS
	 * @return
	 * @throws F3BException
	 */
	private TreeModel prelevaMisureSicurezza(TreeModel aTreeFasSius, BigDecimal aIdFascicoloSIUS)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("##### Ricerca Misure Sicurezza per Fascicolo SIUS con ID : " + aIdFascicoloSIUS);

		if (aIdFascicoloSIUS != null) {
			IMisuraSicurezza lCtrlMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
			MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel();
			lMisMod.setFasSiuIdFascicoloSius(aIdFascicoloSIUS);
			Vector lMisureSicurezza = lCtrlMis.ExRicercaMisuraSicurezza(lMisMod);

			if (lMisureSicurezza != null && lMisureSicurezza.size() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Num. Misure Sicurezza trovate : " + lMisureSicurezza.size());

				Iterator lItxMis = lMisureSicurezza.iterator();
				while (lItxMis.hasNext())
					aTreeFasSius.add(new TreeModel(((MisuraSicurezzaModel) lItxMis.next())));
			}
		}

		return aTreeFasSius;
	}

	/**
	 * Effettua una ricerca in RICHIESTA_REMISSIONE dei record legati al Fascicolo SIUS e li aggiunge al
	 * TreModel passato come parametro.
	 *
	 * @param aTreeFasSius
	 * @param aIdFascicoloSIUS
	 * @return
	 * @throws F3BException
	 */
	private TreeModel prelevaRichiesteRemissione(TreeModel aTreeFasSius, BigDecimal aIdFascicoloSIUS)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug("##### Ricerca Richiesta Remissione per Fascicolo SIUS con ID : " + aIdFascicoloSIUS);

		if (aIdFascicoloSIUS != null) {
			IRichiestaRemissione lCtrlRic = SIUSLookupRemote.getRichiestaRemissioneRemote();
			RichiestaRemissioneModel lRicMod = new RichiestaRemissioneModel();
			lRicMod.setFasSiuIdFascicoloSius(aIdFascicoloSIUS);
			Vector lRichiestaRemissioni = lCtrlRic.ExRicercaRichiestaRemissione(lRicMod);

			if (lRichiestaRemissioni != null && lRichiestaRemissioni.size() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Num. Richieste Remissione trovate : " + lRichiestaRemissioni.size());

				Iterator lItxRic = lRichiestaRemissioni.iterator();
				while (lItxRic.hasNext())
					aTreeFasSius.add(new TreeModel(((RichiestaRemissioneModel) lItxRic.next())));
			}
		}

		return aTreeFasSius;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa dell' Emissione Sentenza.
	 *
	 * @param lEvento
	 *            Evento Model.
	 * @param aCodUff
	 * @param aUtenteModel
	 * @return ByteArrayOutputStream.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaEmissioneSentenza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {

		return ExPreStampaEmissioneSentenza(lEvento, aCodUff, aUtenteModel, null);
	}

	/*
	 * Questa funzione con un parametro in più (DocumentoAllegatoModel) rispetto alla versione public è stata
	 * prodotta per gestire il caso della stampa del Foglio Complementare; in questo caso infatti occorre
	 * passare il Documento Allegato. Negli altri casi, ovvero quando questa viene richiamata dalla versione
	 * public il nuovo parametro viene valorizzato a null.
	 */
	private ByteArrayOutputStream ExPreStampaEmissioneSentenza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel, DocumentoAllegatoModel aDocAll) throws F3BException {

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lRoot = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeFasSIEP = null;
		TreeModel lTreeGenProc = null;
		TreeModel lTreeSentenza = null;
		TreeModel lTreeSentenzaSius = null;
		// TreeModel dell'eventuale Ordinanza di Riferimento (da revocare)
		// TreeModel lTreeOrdinanzaRiferimento = null;
		// TreeModel dell'eventuale Sentenza di Riferimento (da revocare)
		TreeModel lTreeSentenzaRiferimento = null;
		TreeModel lTreeMisAlt = null;
		TreeModel lTreeMisSic = null;
		// 21/01/2007 Fascicolo SIUS Origine + eventuale EMA.
		TreeModel lTreeFasOri = null;

		TreeModel lTreeEventoNotifiche = null;

		Connection lConn = null;
		FascicoloGPModel lFasGP = null;
		FascicoloGPModel lFasGPMisAlt = null;
		FascicoloGPModel lFasGPMisSic = null;

		// Report generator per la costruzione del report
		ReportGenerator lReport = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaEmissioneSentenza : inizio");
		try {
			if (lEvento == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Evento inesistente : ");

			// connessione al Db
			lConn = getDBConnection();

			// ricerca FascicoloSIUSGPmodel
			lFasGP = getFascicoloGPSius(lEvento.getFasSiuIdFascicoloSius(), lConn);

			if (lFasGP == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo inesistente : " + lEvento.getFasSiuIdFascicoloSius());

			// creazione delle varie foglie componenti del documento TreeModel
			lRoot = new TreeModel(CreateRoot(aCodUff, lConn));

			// Fascicolo SIUS
			lTreeFasSIUS = prelevaDatiFascicoloSius(lFasGP, lEvento.getIdEvento(), aDocAll, lConn);

			// 23/05/2006 Ricerca idfascicolo sius per misura alternativa solo per S22
			if (lFasGP.getGeneraleProcedimentoModel() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S22") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisAlt = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGP.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGP.getGeneraleProcedimentoModel().getProgrS1(), aCodUff, lConn);
			}

			// Generale Procedimento
			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			// 23/05/2006 Aggregazione lTreeMisAlt.
			if (lFasGPMisAlt != null && lFasGPMisAlt.getFascicoloSiusModel() != null) {
				lTreeMisAlt = prelevaDatiEsecuzioneMA(
						lFasGPMisAlt.getFascicoloSiusModel().getIdFascicoloSius(), lConn);
			}

			// 23/05/2006 Ricerca idfascicolo sius per misura alternativa solo per S09
			if (lFasGP.getGeneraleProcedimentoModel() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro() != null
					&& lFasGP.getGeneraleProcedimentoModel().getCodTipoRegistro().compareTo("S09") == 0) {
				IFascicoloSius CtrlFasGP = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMisSic = CtrlFasGP.ExRicercaFascicoloByAnnoProgrCodUfficioFast(
						lFasGP.getGeneraleProcedimentoModel().getAnnoS1(),
						lFasGP.getGeneraleProcedimentoModel().getProgrS1(), aCodUff, lConn);
			}

			// Generale Procedimento
			lTreeGenProc = new TreeModel(lFasGP.getGeneraleProcedimentoModel());
			// 23/05/2006 Aggregazione lTreeMisSic.
			if (lFasGPMisSic != null && lFasGPMisSic.getFascicoloSiusModel() != null)
				lTreeMisSic = prelevaDatiEsecuzioneMS(
						lFasGPMisSic.getFascicoloSiusModel().getIdFascicoloSius(), lConn);

			// 22/01/2007 Fascicolo SIUS Origine.
			if (lFasGP.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null)
				lTreeFasOri = prelevaDatiFascicoloSiusOrigine(
						lFasGP.getFascicoloSiusModel().getIdFascicoloSiusOrigine(), lConn, false);

			// Sentenza
			lTreeSentenzaSius = prelevaDatiDepositoSentenza(lEvento.getIdEvento(), lConn);

			// Nel caso sia valorizzato l'ID_EVE_ID_EVENTO si risale alla Sentenza relativa
			// Dati di Deposito Sentenza da Revocare
			if (lTreeSentenzaSius != null && lEvento.getEveIdEvento() != null) {
				// Se la Sentenza ha una Sentenza di Riferimento
				lTreeSentenzaRiferimento = prelevaDatiDepositoSentenza(lEvento.getEveIdEvento(), lConn);
				if (lTreeSentenzaRiferimento != null) {
					lTreeSentenzaSius.add(lTreeSentenzaRiferimento);
					lTreeSentenzaSius
							.add(prelevaDatiFascicoloSiusByIdEvento(lEvento.getEveIdEvento(), lConn));
				}
			}

			lTreeFasSIEP = prelevaDatiFascicoloSiep(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);
			lTreeSentenza = prelevaDatiSentenza(lFasGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep(),
					lConn);

			lTreeEventoNotifiche = prelevaDatiEventoNotifiche(lEvento.getIdEvento(), lConn);

			// Recupera dati Periodo Altra Sanzione attraverso l'evento.
			prelevaDatiPeriodoAltraSanzioneByEvento(lEvento.getIdEvento(), lTreeEventoNotifiche, lConn);

			// COSTRUZIONE DEL DOCUMENTO
			lTreeGenProc.add(lTreeMisAlt);
			lTreeGenProc.add(lTreeMisSic);
			lTreeFasSIUS.add(lTreeSentenzaSius);
			// 23/01/2007 Aggiunto il Fascicolo SIUS Origine con indentata l'eventuale EMA ;
			if (lTreeFasOri != null)
				lTreeFasSIUS.add(lTreeFasOri);

			lTreeFasSIUS.add(lTreeEventoNotifiche);

			lRoot.add(lTreeFasSIUS);
			lRoot.add(lTreeGenProc);
			lRoot.add(lTreeFasSIEP);
			lRoot.add(lTreeSentenza);

			// CREAZIONE DEL TEMPLATE
			// Ricavo nome del template
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lEvento.getTemIdTemplate());
			lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);

			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lRoot, lNomeTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("generate document eseguito");
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaEmissioneSentenza : fine");
		return lByteArrayOut;
	}

	/**
	 * Esegue il prelievo dati DepositoSentenza.
	 *
	 * @param aIdEvento
	 *            id del Evento.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della Sentenza come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiDepositoSentenza(BigDecimal aIdEvento, Connection aConn)
			throws F3BException {

		TreeModel lTreeSenMod = new TreeModel();
		DepositoSentenzaModel lDepMod;

		try {
			// Si preferisce chiamare il Controller
			IDepositoSentenza lDepCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();
			lDepMod = lDepCtrl.ExRicercaDepositoSentenzaByEvento(aIdEvento);

			if (lDepMod != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("####Dati del Model Deposito Sentenza : " + lDepMod);

				lDepMod.setAnnoDataEmissione(DateUtils.getDateToString(lDepMod.getDataEmissione(), "yyyy"));
				lDepMod.setGiornoDataEmissione(DateUtils.getDateToString(lDepMod.getDataEmissione(), "dd"));
				lDepMod.setMeseDataEmissione(DateUtils.getDateToString(lDepMod.getDataEmissione(), "MMMM"));

				lTreeSenMod = new TreeModel(lDepMod);

				// Tenori della Sentenza
				ITenore lCtrlTen = SIUSLookupRemote.getTenoreRemote();
				Vector lTenori = lCtrlTen
						.ExRicercaTenoreBySentenzaOrderByPeso(lDepMod.getIdDepositoSentenza());
				if (lTenori.size() != 0) {
					Iterator lItxTen = lTenori.iterator();
					while (lItxTen.hasNext()) {
						lTreeSenMod.add(new TreeModel((TenoreModel) lItxTen.next()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("StampaController.prelevaDatiDepositoSentenza: Eccezione Generica: " + e);
		}

		return lTreeSenMod;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa di alcuni tipi di Sentenza: Ordinanza di Rinvio Udienza,
	 * Generazione Modelli.
	 *
	 * @param FascicoloGPModel
	 * @param EventoModel
	 *            Evento;
	 * @param UtenteModel
	 *            aUtenteModel.
	 * @return ByteArrayOutputStream.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaDocumentoSentenza(FascicoloGPModel aFasc,
			EventoNotificaModel aEvento, UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaDocumentoSentenza : inizio");
		try {
			aEvento.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());

			TreeModel lTree = prelevaDatiDocumentoSentenza(aEvento, aFasc,
					aUtenteModel.getUfficioUtente().getCodUfficio());

			ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Chiave = " + aEvento.getNomeTemplate());

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Exception: " + e);
			throw e;
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExPreStampaDocumentoSentenza : fine");
		return lByteArrayOut;
	}

	private TreeModel prelevaDatiDocumentoSentenza(EventoNotificaModel aEvento, FascicoloGPModel lFasModel,
			String aCodUfficio) throws F3BException {

		TreeModel lTree = new TreeModel();

		FascicoloGPSqlDAO lFasDao = null;
		UdienzaSqlDAO lUdiDao = null;
		DepositoSentenzaSqlDAO lSenDao = null;
		TenoreSqlDAO lTenDao = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;

		Vector lTenoriGenPro = new Vector();

		Connection lConn = null;
		// BigDecimal lKeyFascicolo = new BigDecimal(0);

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("prelevaDatiDocumentoSentenza : inizio");
			lConn = getDBConnection();

			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			// STUB 20030926 : Patch Temporanea, quando non esiste un
			// fascicolo SIEP per un fascicolo SIUS evita di tirare giù tutti dati di
			// SIEP. per tanto crea solo la documentRoot.
			if (aEvento.getEvento().getFasSieIdFascicoloSiep() != null)
				lTree = lStampa.prelevaDatiEventoSiep(aEvento);
			else
				lTree = new TreeModel(createRootDocumentoSentenza(aEvento, lConn, aCodUfficio));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("prelevaDatiDocumentoSentenza Dopo prelevaDatiEventoSiep");

			// lKeyFascicolo = aEvento.getEvento().getFasSiuIdFascicoloSius();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("***** Presi Eventi *****");
			TreeModel lTreeFasMod = new TreeModel(lFasModel.getFascicoloSiusModel());
			TreeModel lTreeGenMod = new TreeModel(lFasModel.getGeneraleProcedimentoModel());

			// Si cerca l'udienza attraverso l'UDIENZA_PROCEDIMENTO
			BigDecimal lIdUdienza = cercaIdUdienzaByTdEvento(aEvento.getEvento().getIdEvento());
			if (lIdUdienza == null)
				lIdUdienza = lFasModel.getGeneraleProcedimentoModel().getUdiIdUdienza();
			// Udienza.
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaUdienzaByKey(lIdUdienza);
			UdienzaModel lUdienza = (UdienzaModel) lUdiDao.getModelByKey();

			if (lUdienza != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("####Dati del Model Udienza : " + lUdienza);

				TreeModel lTreeUdiMod = new TreeModel(lUdienza);
				lTreeFasMod.add(lTreeUdiMod);
			}

			lSenDao = new DepositoSentenzaSqlDAO(lConn);

			if (aEvento.getEvento().getIdEvento() != null) {
				lSenDao.ricercaDepositoSentenzaByIdEveGenerato(aEvento.getEvento().getIdEvento());
			} else if (lFasModel.getGeneraleProcedimentoModel() != null) {
				lSenDao.ricercaDepositoSentenzaByIdGenProcedimento(
						lFasModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("######Preleva dati Dep Sentenza : ");
			DepositoSentenzaModel lSen = (DepositoSentenzaModel) lSenDao.getModelByKey();
			TreeModel lTreeSenMod = null;

			if (lSen != null) {
				lSen.setAnnoDataEmissione(DateUtils.getDateToString(lSen.getDataEmissione(), "yyyy"));
				lSen.setGiornoDataEmissione(DateUtils.getDateToString(lSen.getDataEmissione(), "dd"));
				lSen.setMeseDataEmissione(DateUtils.getDateToString(lSen.getDataEmissione(), "MMMM"));
				lTreeSenMod = new TreeModel(lSen);
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Deposito Sentenza mancante !!!!");
			}

			if (lSen != null) {
				lTenDao = new TenoreSqlDAO(lConn);
				// Ricerca Tenori x ID DepositoSentenza
				lTenDao.ricercaTenoriBySentenzaOrderByPeso(lSen.getIdDepositoSentenza());
				Vector lTenori = new Vector(lTenDao.getModels());
				Iterator lItxTen = lTenori.iterator();
				TreeModel lTreeTenMod = null;

				while (lItxTen.hasNext()) {
					lTreeTenMod = new TreeModel((TenoreModel) lItxTen.next());
					lTreeSenMod.add(lTreeTenMod);
				}

				lTreeFasMod.add(lTreeSenMod);
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("I Tenori non possono essere cercati perchè Sentenza mancante !!!!");

			// Tenori
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoreByGeneraleProc(
					lFasModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenoriGenPro = new Vector(lTenDao.getModels());
			if (lTenoriGenPro.size() != 0) {
				Iterator lItxTen = lTenoriGenPro.iterator();
				while (lItxTen.hasNext())
					lTreeGenMod.add(new TreeModel((TenoreModel) lItxTen.next()));
			}

			// Preleva altri dati del fascicolo
			// Riempi l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSius.TREE_SOGGETTO, ICostantiStampaSius.TREE_MAGISTRATO,
					ICostantiStampaSius.TREE_LUOGODET, ICostantiStampaSius.TREE_TIT_ESE_REF,
					ICostantiStampaSius.TREE_RIF_FAS_SIEP };
			// Crea il TreeModel con i dati che occorrono
			lTreeFasMod = ExAggiungiDatiStampa(lFasModel.getFascicoloSiusModel().getIdFascicoloSius(),
					aTipoDati, lTreeFasMod);

			lTree.add(lTreeFasMod);
			lTree.add(lTreeGenMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("prelevaDatiDocumentoSentenza: Non posso leggere : " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("prelevaDatiDocumentoSentenza: Non posso leggere  : " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lUdiDao);
			cleanup(lSenDao);
			cleanup(lLuoDao);
			cleanup(lTenDao);
			cleanup(lSogSqlDao);
			cleanup(lResSqlDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("prelevaDatiDocumentoSentenza : fine");
		return lTree;
	}

	/**
	 * Crea la root del Documento di Stampa Modello Sentenza Questa funzione fa uso della CreateRoot() per
	 * creare l'intestazione standard del documento di stampa. Poi però sull modello standard creato (XModel)
	 * effettua delle modifiche riguardo la descrizione dell'Ufficio.
	 *
	 * @param aEveModel
	 * @param Connection
	 *            aConn;
	 * @param String
	 *            aCodUfficio.
	 * @return lStampa
	 */
	private XModel createRootDocumentoSentenza(EventoNotificaModel aEveModel, Connection aConn,
			String aCodUfficio) throws F3BException {

		XModel lStampa = CreateRoot(aCodUfficio, aConn);

		String descrTipoUff = aEveModel.getEvento().getDescrUfficioEmittente().toUpperCase();
		lStampa.setUfficio(aEveModel.getEvento().getDescrLuogoEmittente().toUpperCase());
		lStampa.setTipoUfficio(descrTipoUff);

		if (descrTipoUff != null) {
			if (descrTipoUff.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
			}
			// STUB 01/02/2005 Patch x Valorizzare TipoUfficioT1.
			else
				lStampa.setTipoUfficioT1(descrTipoUff);
		}
		return lStampa;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 *
	 * @param aFiltroRicerca
	 * @param aElenco
	 * @param aUtente
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExPreStampaProcSiusXProv(RicercaProvvedimentoModel aFiltroRicerca,
			Vector aElenco, UtenteModel aUtente) throws F3BException {

		// Connessione al DB per il prelievo dei dati.
		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot = null; // radice dell'albero generale del documento
		String lIdTemplate;

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione dell'Ufficio documento
			XModel lBase = CreateRoot(aUtente.getUfficioUtente().getCodUfficio(), lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			// Intestazione del documento XML
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(aUtente));
			lTreeRoot.add(new TreeModel(aFiltroRicerca));

			// Iterazione della lista di Notifiche
			Iterator lItx = aElenco.iterator();
			EveFasGepSogProvModel lProcCorr = null;

			IImpugnazione lImpCtrl = SIUSLookupRemote.getImpugnazioneRemote();
			while (lItx.hasNext()) {
				lProcCorr = (EveFasGepSogProvModel) lItx.next();

				TreeModel lTreeProcedimento = new TreeModel(lProcCorr);

				// ramo relativo all'evento
				if (lProcCorr.getEvento() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getEvento()));
				// ramo relativo al Fascicolo SIUS
				if (lProcCorr.getFascicoloSius() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getFascicoloSius()));
				// Ramo Ordinanza
				if (lProcCorr.getDepositoOrdinanzaPc() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getDepositoOrdinanzaPc()));
				// Ramo Sentenza
				if (lProcCorr.getDepositoSentenza() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getDepositoSentenza()));
				// Decreto
				if (lProcCorr.getDepositoDecreto() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getDepositoDecreto()));
				// Impugnazione
				if (lProcCorr.getImpugnazione() != null) {
					// Preleva l'ImpugnazioneModel
					ImpugnazioneModel lImpModel = null;
					lImpModel = lImpCtrl
							.ExRicercaImpugnazioneByKey(lProcCorr.getImpugnazione().getIdImpugnazione());
					lTreeProcedimento.add(new TreeModel(lImpModel));
				}
				// DocumentoAllegato (Foglio Complementare)
				if (lProcCorr.getDocumentoAllegato() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getDocumentoAllegato()));

				// Viene aggiunto il ramo relativo al Soggetto
				if (lProcCorr.getSoggetto() != null)
					if (lProcCorr.getFascicoloSius() != null)
						lTreeProcedimento.add(prelevaDatiSoggetto(lProcCorr.getSoggetto().getIdSoggetto(),
								lProcCorr.getFascicoloSius().getIdFascicoloSius(), lConn));
					else
						lTreeProcedimento.add(new TreeModel(lProcCorr.getSoggetto()));

				lTreeRoot.add(lTreeProcedimento);
			}
		} catch (Exception lEx) {
			throw new SIUSException("StampaController.ExPreStampaProcSiusXProv : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// Viene istanziato il Report Generator
		ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());

		lIdTemplate = "";

		if (aFiltroRicerca.isRicercaXProvvedimento())
			lIdTemplate = "SIUS_ST_007";
		else if (aFiltroRicerca.isRicercaXDecreto())
			lIdTemplate = "SIUS_ST_008";
		else if (aFiltroRicerca.isRicercaXImpugnazioneRicorso())
			lIdTemplate = "SIUS_ST_009";
		else if (aFiltroRicerca.isRicercaXFoglioComplementare())
			lIdTemplate = "SIUS_ST_010";
		else if (aFiltroRicerca.isRicercaXSentenza())
			lIdTemplate = "SIUS_ST_012";

		// Si ricava il Nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 */
	public Vector ExRicercaCopertineFascicoliSius(FascicoloSiusModel fsm, int pagine) throws F3BException {

		FascicoloSiusSqlDAO fssdao = null;
		Connection c = null;
		Vector fascicoli = new Vector();

		try {
			c = getDBConnection();
			fssdao = new FascicoloSiusSqlDAO(c);
			fssdao.ricercaIdFascicoli(fsm, pagine);
			fssdao.start();
			while (fssdao.next())
				fascicoli.add(fssdao.getCopertineFascicoliSiusModel());
			fssdao.stop();
			if (fascicoli.size() == 0)
				throw new F3BException("Nessun procedimento nell'intervallo impostato!");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.ExRicercaCopertineFascicoliSius: " + daoEx);
		} finally {
			cleanup(fssdao);
			cleanup(c);
		}
		// valore di ritorno
		return fascicoli;
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'. Questo metodo genera il documento per la
	 * stampa delle copertine
	 *
	 * @param aFasfsmc
	 * @param um
	 * @param pagine
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaCopertineFascicoliSius(FascicoloSiusModel fsm, UtenteModel um,
			int pagine) throws F3BException {

		TreeModel tm = prelevaDatiStampaCopertineFascicoliSius(fsm, um, pagine);
		ReportGenerator rg = new ReportGenerator(um.getUfficioUtente().getCodUfficio());
		String nomeTemplate = TemplateManager.getInstance().getTemplateName("SIUS_ST_013");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("NOME TEMPLATE >>>" + nomeTemplate);
		ByteArrayOutputStream baos = (ByteArrayOutputStream) rg.generateDocument(tm, nomeTemplate);

		// stampa copertine
		return baos;
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'. Questo metodo genera il documento per la
	 * stampa delle copertine
	 *
	 * @param fsm
	 * @param um
	 * @param pagine
	 * @return TreeModel
	 * @throws F3BException
	 */
	private TreeModel prelevaDatiStampaCopertineFascicoliSius(FascicoloSiusModel fsm, UtenteModel um,
			int pagine) throws F3BException {

		FascicoloSiusSqlDAO fssdao = null;
		Connection c = null;
		TreeModel tm = null;

		try {
			tm = new TreeModel(createRootFascicolo(fsm, um));
			c = getDBConnection();
			fssdao = new FascicoloSiusSqlDAO(c);
			fssdao.ricercaIdFascicoli(fsm, pagine);
			fssdao.start();
			Vector fascicoli = new Vector();
			while (fssdao.next())
				fascicoli.add(fssdao.getCopertineFascicoliSiusModel());
			fssdao.stop();
			Iterator i = fascicoli.iterator();
			if (fascicoli.size() == 0)
				throw new F3BException("Nessun procedimento per i criteri di ricerca impostati!");
			while (i.hasNext()) {
				FascicoloSiusModel fsm2 = (FascicoloSiusModel) i.next();
				int[] aTipoDati = { ICostantiStampaSius.TREE_SOGGETTO,
						ICostantiStampaSius.TREE_FASCICOLOSIEP_ALL, ICostantiStampaSius.TREE_SENTENZA,
						ICostantiStampaSius.TREE_AVVOCATO, ICostantiStampaSius.TREE_LUOGODET,
						ICostantiStampaSius.TREE_MAGISTRATO, ICostantiStampaSius.TREEs_RICHIESTE_ISTRUTTORIE,
						ICostantiStampaSius.TREEs_PROVVEDIMENTI,
						ICostantiStampaSius.TREEs_PROVVEDIMENTI_ALTRI, ICostantiStampaSius.TREE_TIT_ESE_REF,
						ICostantiStampaSius.TREE_RIF_FAS_SIEP,
						ICostantiStampaSius.TREE_ESECUZIONEMISURAALTERNATIVA,
						ICostantiStampaSius.TREE_UDIENZA };
				TreeModel tm2 = ExPrelevaDatiStampa(fsm2.getIdFascicoloSius(), aTipoDati,
						um.getUfficioUtente().getCodUfficio());
				tm.add(tm2);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiStampaCopertineFascicoliSius: " + daoEx);
		} finally {
			cleanup(fssdao);
			cleanup(c);
		}
		// stampa copertine
		return tm;
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'. Questo metodo genera il documento per la
	 * stampa delle copertine
	 *
	 * @param fsm
	 * @param um
	 * @return XModel
	 */
	public XModel createRootFascicolo(FascicoloSiusModel fsm, UtenteModel um) throws F3BException {

		XModel lStampa = new XModel();

		String descrTipoUfficio = fsm.getDescrTipoUfficio().toUpperCase();
		lStampa.setUfficio(fsm.getDescrComuneUfficio().toUpperCase());
		lStampa.setTipoUfficio(descrTipoUfficio.toUpperCase());
		lStampa.setDataElaborazione(DateUtils.getSysDate());

		if (um != null && um.getUfficioUtente() != null) {
			UfficioModel ufm = um.getUfficioUtente();
			lStampa.setCap(ufm.getCap());
			lStampa.setFax(ufm.getFax());
			lStampa.setIndirizzo(ufm.getIndirizzo());
			lStampa.setTelefono(ufm.getTelefono());
		}

		if (descrTipoUfficio != null) {
			if (descrTipoUfficio.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUfficio.substring(0, descrTipoUfficio.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUfficio.substring(descrTipoUfficio.indexOf("PRESSO")));
			}
		}

		if (descrTipoUfficio.indexOf("GENERALE") > 0)
			lStampa.setFirmatario("Il Procuratore Generale");
		else
			lStampa.setFirmatario("Il Pubblico Ministero");

		// valore di ritorno
		return lStampa;
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 */
	public BigDecimal ExContaCopertineFascicoliSius(FascicoloSiusModel fsm) throws F3BException {

		// connessione
		Connection c = null;
		FascicoloSiusSqlDAO fssdao = null;
		BigDecimal bd = new BigDecimal(0);

		try {
			c = getDBConnection();
			fssdao = new FascicoloSiusSqlDAO(c);
			fssdao.ricercaIdFascicoli(fsm, 0);
			bd = fssdao.getNumRowsSelected();
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(fssdao);
			cleanup(c);
		}
		return bd;
	}

}