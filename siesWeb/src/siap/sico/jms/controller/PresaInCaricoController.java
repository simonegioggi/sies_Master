package siap.sico.jms.controller;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.jms.ICostantiJMS;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.PulisciEventoStoreProcedureDAO; // STUB 11/04/2005
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel; // STUB 21/03/2005
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiusDAO;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scambiosanzione.dao.ScambioSanzioneDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.sentenza.dao.SentenzaDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siepe.assistentesociale.dao.AssistenteSocialeDAO;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import siap.siepe.attivita.dao.AttivitaDAO;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.dao.FascicoloSiepeDAO;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.relazione.dao.RelazioneDAO;
import siap.siepe.relazione.model.RelazioneModel;
import siap.siepe.richiesta.dao.RichiestaDAO;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.sius.avvocato.dao.AvvocatoDAO;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusDAO;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.dao.DepositoSentenzaDAO;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.impugnazione.dao.ImpugnazioneDAO;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.motivazionedecreto.dao.MotivazioneDecretoDAO;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.permesso.dao.EventoPermessoLicenzaDAO;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import siap.sius.prescrizione.dao.PrescrizioneDAO;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.rifasiep.dao.RiferimentoFascicoloSiepDAO;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneDAO;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.F3BException;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: PresaInCaricoController
 * </p>
 * <p>
 * Description: Presa in carico di un'ordinanza proveniente da altra BDI
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 */
/*
 * Luigi 28-06-2006 Questo Controller sostituira' l'analogo in siap.sius.jms.controller. In fase di test
 * continuano a sussistere entrambi.
 */
@SuppressWarnings("rawtypes")
public class PresaInCaricoController extends SiapController implements IPresaInCarico {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public MessaggioModel ExPresaInCaricoOrdinanza(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt)
			throws F3BException {
		Connection lConn = null;
//		FascicoloSiepeModel lFasSiepe = null;
		try {
			lConn = getDBTransaction();

//			ParserMessage lPars;
			if (aMessaggio.getTreeModel() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			aMessaggio = ExPresaInCaricoOrdinanza(aMessaggio, aMisAlt, lConn);
			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			throw new F3BException(this.getClass().getPackage().getName() + ".ExPresaInCaricoOrdinanza: " + e);
		} finally {
			cleanup(lConn);
		}

		return aMessaggio;
	}

	/*
	 * -- Luigi 18-06-2006 Ho spostato lo scarico delle NOTIFICHE dopo lo scarico degli AVVOCATI e nello
	 * ExScaricaAvvocatiFascicoloSIUS() eliminata la DELETE.
	 */
	public MessaggioModel ExPresaInCaricoOrdinanza(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt,
			Connection aConn) throws Exception {
		ParserMessage lPars;
		if (aMessaggio.getTreeModel() != null)
			lPars = new ParserMessage(aMessaggio.getTreeModel());
		else
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire il Messaggio Contenuto incorretto!");

		// COntrollo se le BDI sono diverse
		if (aMessaggio.getCodBdiDestinataria().trim().compareTo(aMessaggio.getCodBdiMittente().trim()) != 0) {
			// Prelievo del Soggetto dal messaggio e suo inserimento nel DB
			ExScaricaSoggetto(lPars, aConn);

			// Prelievo della Sentenza e suo inserimento nel DB
			aMessaggio = ExScaricaSentenza(lPars, aConn, aMessaggio);

			// 11/10/2010 Prelievo del Soggetto SIEP.
			aMessaggio = ExScaricaSoggettoSIEP(lPars, aConn, aMessaggio);

			// Prelievo del Fascicolo SIEP e suo Inserimento nel DB
//			FascicoloSiepModel lFasSiepInviato = new FascicoloSiepModel();
			/*lFasSiepInviato = */ExScaricaFascicoloSiep(lPars, aConn, aMessaggio);

			// Prelievo del Fascicolo SIUS e suo Inserimento nel DB
			/*FascicoloSiusModel lFasSiu = */ExScaricaFascicoloSius(lPars, aConn, aMessaggio);

			// GENERALE PROCEDIMENTO
			// Prelievo del GENERALE PROCEDIMENTO e suo Inserimento nel DB
			/*GeneraleProcedimentoModel lGenPro = */ExScaricaGeneraleProcedimento(lPars, aConn, aMessaggio);

			// RESIDENZA
			/*ResidenzaAssociataModel lResAssociata = */ExScaricaResidenza(lPars, aConn, aMessaggio);

			// RESIDENZA_FASCICOLO_SIUS
			/*lResAssociata = */ExScaricaResidenzaSius(lPars, aConn, aMessaggio);

			// EVENTO
			/*EventoNotificaModel lEvento = */ExScaricaEvento(lPars, aConn, aMessaggio);

			// NOTE AGGIUNTIVE
			ExScaricaNoteAggiuntive(lPars, aConn, aMessaggio);

			// STUB 12/04/2005 DOCUMENTO ALLEGATO
			ExScaricaDocumentoAllegato(lPars, aConn, aMessaggio);

			// STUB 15/04/2005 LUOGO DETENZIONE.
			ExScaricaLuogoDetenzione(lPars, aConn, aMessaggio);

			// STUB 15/04/2005 RIFERIMENTI FASCICOLI SIEP.
			ExScaricaRiferimentiFascicoloSIEP(lPars, aConn, aMessaggio);

			// MISURA ALTERNATIVA
			ExScaricaMisuraAlternativa(lPars, aConn, aMessaggio);

			// STUB 07/04/2005 AVVOCATI SIUS.
			ExScaricaAvvocatiSIUS(lPars, aConn, aMessaggio);

			// NOTIFICHE - AUTORITA ESTERNE
			ExScaricaNotifiche(lPars, aConn, aMessaggio);

			// DEPOSITO ORDINANZA PC
			ExScaricaDepositoOrdinanza(lPars, aConn, aMessaggio);

			// LICENZA LIBANTICIPATA
			ExScaricaLiberazioneAnticipata(lPars, aConn, aMessaggio);

			// PRESCRIZIONI
			ExScaricaPrescrizioni(lPars, aConn, aMessaggio);

			// IMPUGNAZIONE
			ExScaricaImpugnazione(lPars, aConn, aMessaggio);

			// TENORI
			ExScaricaTenori(lPars, aConn, aMessaggio);

			// *** AGGIORNA LA POSIZIONE GIURIDICA IN BASE ALLA MISURA ALTERNATIVA ***
			// Posizione Giuridica
			ExAggiornaPosizioneGiuridica(lPars, aConn, aMessaggio, aMisAlt);

			// *** AGGIORNA LO STATO PROCEDIMENTO IN BASE ALLA MISURA ALTERNATIVA ***
			ExAggiornaStatoProcedimento(lPars, aConn, aMessaggio, aMisAlt);

			// 25/02/2008 ESECUZIONE SANZIONE SOSTITUTIVA.
			ExScaricaEsecSanzSostitutiva(lPars, aConn, aMessaggio);

			// 25/02/2008 PERIODO ALTRA SANZIONE.
			ExScaricaPeriodiAltraSanzione(lPars, aConn, aMessaggio);

			// 25/02/2008 SCAMBIO SANZIONE.
			ExScaricaScambioSanzione(lPars, aConn, aMessaggio);

			// 17/02/2015 MISURE SICUREZZA.
			ExScaricaMisureSicurezza(lPars, aConn, aMessaggio);
		}
		return aMessaggio;

	}

	/**
	 * Presa in carico del Decreto
	 * 
	 * @param aMessaggio
	 *            Messaggio arrivato via JMS
	 * @param aMisAlt
	 * @return MessaggioModel
	 * @throws F3BException
	 */

	public MessaggioModel ExPresaInCaricoDecreto(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt)
			throws F3BException {
		Connection lConn = null;
//		FascicoloSiepeModel lFasSiepe = null;
		try {
			lConn = getDBTransaction();

//			ParserMessage lPars;
			if (aMessaggio.getTreeModel() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			aMessaggio = ExPresaInCaricoDecreto(aMessaggio, aMisAlt, lConn);
			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;

		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			throw new F3BException(this.getClass().getPackage().getName() + ".ExPresaInCaricoDecreto: " + e);
		} finally {
			cleanup(lConn);
		}
		return aMessaggio;
	}

	/**
	 * Presa in carico del Decreto
	 * 
	 * @param aMessaggio
	 *            Messaggio arrivato via JMS
	 * @param aMisAlt
	 * @param aConn
	 * @return MessaggioModel
	 * @throws F3BException
	 */
	public MessaggioModel ExPresaInCaricoDecreto(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt,
			Connection aConn) throws Exception {

		ParserMessage lPars;
		if (aMessaggio.getTreeModel() != null)
			lPars = new ParserMessage(aMessaggio.getTreeModel());
		else
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire il Messaggio Contenuto incorretto!");

		if (aMessaggio.getCodBdiDestinataria().trim().compareTo(aMessaggio.getCodBdiMittente().trim()) != 0) {
			// Prelievo del Soggetto dal messaggio e suo inserimento nel DB
			ExScaricaSoggetto(lPars, aConn);

			// Prelievo della Sentenza e suo inserimento nel DB
			aMessaggio = ExScaricaSentenza(lPars, aConn, aMessaggio);

			// 11/10/2010 Prelievo del Soggetto SIEP.
			aMessaggio = ExScaricaSoggettoSIEP(lPars, aConn, aMessaggio);

			// Prelievo del Fascicolo SIEP e suo Inserimento nel DB
//			FascicoloSiepModel lFasSiepInviato = new FascicoloSiepModel();
			/*lFasSiepInviato = */ExScaricaFascicoloSiep(lPars, aConn, aMessaggio);

			// Prelievo del Fascicolo SIUS e suo Inserimento nel DB
			/*FascicoloSiusModel lFasSiu = */ExScaricaFascicoloSius(lPars, aConn, aMessaggio);

			// Prelievo del GENERALE PROCEDIMENTO e suo Inserimento nel DB
			/*GeneraleProcedimentoModel lGenPro = */ExScaricaGeneraleProcedimento(lPars, aConn, aMessaggio);

			// RESIDENZA
			/*ResidenzaAssociataModel lResAssociata = */ExScaricaResidenza(lPars, aConn, aMessaggio);

			// RESIDENZA_FASCICOLO_SIUS
			/*lResAssociata = */ExScaricaResidenzaSius(lPars, aConn, aMessaggio);

			// EVENTO
			/*EventoNotificaModel lEvento = */ExScaricaEvento(lPars, aConn, aMessaggio);

			// NOTE AGGIUNTIVE
			ExScaricaNoteAggiuntive(lPars, aConn, aMessaggio);

			// STUB 12/04/2005 DOCUMENTO ALLEGATO
			ExScaricaDocumentoAllegato(lPars, aConn, aMessaggio);

			// STUB 15/04/2005 LUOGO DETENZIONE.
			ExScaricaLuogoDetenzione(lPars, aConn, aMessaggio);

			// STUB 15/04/2005 RIFERIMENTI FASCICOLI SIEP.
			ExScaricaRiferimentiFascicoloSIEP(lPars, aConn, aMessaggio);

			// MISURA ALTERNATIVA
			ExScaricaMisuraAlternativa(lPars, aConn, aMessaggio);

			// STUB 07/04/2005 AVVOCATI SIUS.
			ExScaricaAvvocatiSIUS(lPars, aConn, aMessaggio);

			// NOTIFICHE - AUTORITA ESTERNE
			ExScaricaNotifiche(lPars, aConn, aMessaggio);

			// Deposito Decreto
			ExScaricaDepositoDecreto(lPars, aConn, aMessaggio);

			// LICENZA LIBANTICIPATA
			ExScaricaLiberazioneAnticipata(lPars, aConn, aMessaggio);

			// Motivazioni Decreto
			ExScaricaMotivazioniDecreto(lPars, aConn, aMessaggio);

			// PRESCRIZIONI
			ExScaricaPrescrizioni(lPars, aConn, aMessaggio);

			// IMPUGNAZIONE
			ExScaricaImpugnazione(lPars, aConn, aMessaggio);

			// TENORI
			ExScaricaTenori(lPars, aConn, aMessaggio);

			// *** AGGIORNA LA POSIZIONE GIURIDICA IN BASE ALLA MISURA ALTERNATIVA ***
			// Posizione Giuridica
			ExAggiornaPosizioneGiuridica(lPars, aConn, aMessaggio, aMisAlt);

			// *** AGGIORNA LO STATO PROCEDIMENTO IN BASE ALLA MISURA ALTERNATIVA ***
			ExAggiornaStatoProcedimento(lPars, aConn, aMessaggio, aMisAlt);

			// 17/02/2015 MISURE SICUREZZA.
			ExScaricaMisureSicurezza(lPars, aConn, aMessaggio);
		}
		return aMessaggio;
	}

	/**
	 * <p>
	 * Title: PresaInCaricoAttivita
	 * </p>
	 * <p>
	 * Description: Presa in carico di una attivita UEPE proveniente da altra BDI
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2007
	 * </p>
	 */

	public MessaggioModel ExPresaInCaricoAttivita(MessaggioModel aMessaggio) throws F3BException {
		Connection lConn = null;
//		FascicoloSiepeEstesoModel lFasSiepeEsteso = null;
		try {
			lConn = getDBTransaction();

//			ParserMessage lPars;
			if (aMessaggio.getTreeModel() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			aMessaggio = ExPresaInCaricoAttivita(aMessaggio, lConn);
			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;

		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			throw new F3BException(this.getClass().getPackage().getName() + ".ExPresaInCaricoAttivita: " + e);
		} finally {
			cleanup(lConn);
		}
		return aMessaggio;
	}

	public MessaggioModel ExPresaInCaricoAttivita(MessaggioModel aMessaggio, Connection aConn)
			throws Exception {
		ParserMessage lPars;
		if (aMessaggio.getTreeModel() != null)
			lPars = new ParserMessage(aMessaggio.getTreeModel());
		else
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire il Messaggio Contenuto incorretto!");

		// Controllo se le BDI sono diverse
		if (aMessaggio.getCodBdiDestinataria().trim().compareTo(aMessaggio.getCodBdiMittente().trim()) != 0) {
			// Prelievo del Fascicolo SIEPE dal messaggio e suo inserimento nel DB.
			ExScaricaFascicoloSiepe(lPars, aConn, aMessaggio);

			// Prelievo dell'Assistente Sociale ricevuto e suo inserimento nel DB.
			ExScaricaAssistenteSociale(lPars, aConn, aMessaggio);

			// Prelievo dell'Attivita riferita al fascicolo SIEPE ricevuto e suo inserimento nel DB.
			ExScaricaAttivitaSiepe(lPars, aConn, aMessaggio);
		}
		return aMessaggio;
	}

	/**
	 * Metodo che esegue la presa in carico della Richiesta Siepe.
	 * <p>
	 * @ param aMessaggio Istanza della classe MessaggioModel.
	 * 
	 * @exception F3BException
	 *                propaga l'errore di eccezione.
	 */
	public MessaggioModel ExPresaInCaricoRichiestaSiepe(MessaggioModel aMessaggio) throws F3BException {
		Connection lConn = null;

		try {
			lConn = getDBConnection();

			if (aMessaggio.getTreeModel() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			/*
			 * ReportGenerator lRep = new ReportGenerator(); lRep.parseTreeXML( aMessaggio.getTreeModel() );
			 */

			aMessaggio = ExPresaInCaricoRichiestaSiepe(aMessaggio, lConn);
			commit(lConn);
		} catch (F3BException fex) {
			rollback(lConn);
			throw fex;
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(this.getClass().getPackage().getName()
					+ ".ExPresaInCaricoRichiestaSiepe: " + ex);
		} finally {
			cleanup(lConn);
		}

		return aMessaggio;
	}

	/**
	 * Metodo che si occupa di eseguire la presa in carico della Richiesta SIEPE/UEPE.
	 * <p>
	 * 
	 * @param aMessaggio
	 *            Oggetto MessaggioModel come parametro.
	 * @param aConn
	 *            Oggetto della connessione al DBase come parametro.
	 * @return il MessaggioModel opportunamente valorizzato.
	 * @throws Exception
	 *             propaga l'errore di eccezione.
	 */
	public MessaggioModel ExPresaInCaricoRichiestaSiepe(MessaggioModel aMessaggio, Connection aConn)
			throws Exception {
		// ParserMessage lPars; // Si puo' eliminare ?
		ParserMessage lPars = null;
		if (aMessaggio.getTreeModel() != null)
			lPars = new ParserMessage(aMessaggio.getTreeModel());
		else
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire il messaggio. Contenuto incorretto!");

		// Controllo se le BDI sono diverse
		if (aMessaggio.getCodBdiDestinataria().trim().compareTo(aMessaggio.getCodBdiMittente().trim()) != 0) {
			// Prelievo del Fascicolo SIEPE dal messaggio ed il suo inserimento nel DB.
			ExScaricaFascicoloSiepe(lPars, aConn, aMessaggio);

			// Prelievo della Richiesta SIEPE dal messaggio ed il relativo inserimento in DB
			ExScaricaRichiestaSiepe(lPars, aConn, aMessaggio);
		}

		return aMessaggio;
	}

	public MessaggioModel ExPresaInCaricoRicorso(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt)
			throws F3BException {
		Connection lConn = null;

		FascicoloSiepDAO lFasSiepDao = null; // STUB 11/12/2003 Prima di Fascicolo SIUS
		SentenzaDAO lSentenzaDao = null; // STUB 11/12/2003 Prima di Fascicolo SIUS
		SoggettoDAO lSogDao = null;
		EventoDAO lEveDao = null; // Dopo FascicoloSius
		NotificaDAO lNotDao = null; // Dopo Evento/Autorita Esterna
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null; // Dopo Evento
		FascicoloSiusDAO lFasSiusDao = null;
		GeneraleProcedimentoDAO lGenProDao = null; // Dopo FascicoloSius
		MisuraAlternativaDAO lMisDao = null;
		DepositoDecretoDAO lDepDecrDao = null; // Dopo GeneraleProcedimento/Evento
		DepositoOrdinanzaPcDAO lOrdDao = null; // Dopo GeneraleProcedimento/Evento
		PrescrizioneDAO lPresrDao = null;
		ImpugnazioneDAO lImpDao = null;
		TenoreDAO lTenDao = null;
		ResidenzaDAO lResDao = null;
		MotivazioneDecretoDAO lMotDecrDao = null;
		ResidenzaFascicoloSiusDAO lResFasSiuDao = null;
		AvvocatoDAO lAvvDao = null;
		AvvocatoFascicoloSiusDAO lAvvFasSiuDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		DocumentoAllegatoDAO lDocAllDAO = null; // STUB 12/04/2005
		LuogoDetenzioneDAO lLuoDetDAO = null; // STUB 15/04/2005
		RiferimentoFascicoloSiepDAO lRifasiepDAO = null; // STUB 15/04/2005

		try {
			ParserMessage lPars;
			if (aMessaggio.getTreeModel() != null)
				lPars = new ParserMessage(aMessaggio.getTreeModel());
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			if (aMessaggio.getCodBdiDestinataria().trim().compareTo(aMessaggio.getCodBdiMittente().trim()) != 0) {

				lConn = getDBTransaction();

				// Controllo sull'appartenenza del FascicoloSiep all'Ufficio destinatario
				// ***
				// ***

				try {
					if (lPars.getFascicoloGPSius() != null
							&& lPars.getFascicoloGPSius().getFascicoloSiusModel() != null
							&& lPars.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto() != null) {
						lSogDao = new SoggettoDAO(lConn);
						lSogDao.setDAOFromModel(lPars.getFascicoloGPSius().getFascicoloSiusModel()
								.getSoggetto());
						lSogDao.setWithoutSequence(true);
						lSogDao.insert();
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Soggetto perche' non ricevuto nelle Presa in Carico! ");
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Soggetto gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Soggetto! ");
				}

				// STUB 11/12/2003 SENTENZA
				try {
					SentenzaModel lSentenza = lPars.getSentenza();
					if (lSentenza != null && lSentenza.getIdSentenza() != null) {
						lSentenzaDao = new SentenzaDAO(lConn);
						lSentenzaDao.setDAOFromModel(lSentenza);
						lSentenzaDao.setIdSentenza(lSentenza.getIdSentenza());
						lSentenzaDao.setWithoutSequence(true);
						lSentenzaDao.insert();
					} else
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("SEntenza Assente !!!");
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Sentenza gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire la Sentenza! ");
				}

				// STUB 11/12/2003 FASCICOLO SIEP
				FascicoloSiepModel lFasSiepInviato = new FascicoloSiepModel();
				try {
					lFasSiepInviato = lPars.getFascicolo();
					if (lFasSiepInviato != null) {
						lFasSiepDao = new FascicoloSiepDAO(lConn);

						lFasSiepDao.setDAOFromModel(lFasSiepInviato);
						lFasSiepDao.setWithoutSequence(true);
						lFasSiepDao.insert();
					} else
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Fascicolo SIEP Assente !!!");

				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// STUB 18/02/2004 Se si viola la PK occorre verificare che i Fas. SIEP abbiano lo
						// stesso ANNO/PROGR/UFFICIO
						// Si prosegue solo se la violazione di unique constraint riguarda la primary key.
						// Nell'altro caso (fascicoli SIEP con lo stesso ANNO/PROGR/UFFICIO ma diversi ID,
						if (ex.getMessage().indexOf("FAS_SIE_PK") > 0) {
							// STUB 18/02/2004 Controllo del Fascicolo SIEP.
							// Lo Metto in sessione solo dopo aver verificato che non ne esiste un altro con
							// stessi ANNO/PROGR/UFFICIO.
							/*
							 * FascicoloSiepController lCtrl = new FascicoloSiepController();
							 * FascicoloSiepModel lFasSiepPresente = new FascicoloSiepModel();
							 * 
							 * // Imposto la chiave Anno/Progressivo/Ufficio per la ricerca.
							 * lFasSiepPresente.setChiaveAnno(lFasSiepInviato.getChiaveAnno());
							 * lFasSiepPresente.setChiaveProgr(lFasSiepInviato.getChiaveProgr());
							 * lFasSiepPresente.setChiaveUfficio(lFasSiepInviato.getChiaveUfficio());
							 * 
							 * Vector lVect = lCtrl.ExRicercaFascicoloSiep(lFasSiepPresente);
							 * 
							 * if ( lVect.size() > 0 ) {
							 */
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Fascicolo Siep gia' presente...");
							aMessaggio.setCodEsito("00001");
							/*
							 * } else throw new F3BException(F3BException.USER_MESSAGE,
							 * "Impossibile inserire il Fascicolo Siep! Presente Un fascicolo SIEP con stesso ID ma ANNO/PROGR/UFFICIO diversi!"
							 * );
							 */}
						// STUB 18/03/2004 Se scatta la costraint FAS_ANN_UFF_PRO_FK_I controllo la coppia di
						// ID.
						else if (ex.getMessage().indexOf("FAS_ANN_UFF_PRO_FK_I") > 0) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Fascicolo Siep gia' presente...");
							aMessaggio.setCodEsito("00001");
							// Controllo del Fascicolo SIEP. Lo Metto in sessione solo dopo aver verificato
							// che non ne esiste un altro con stesso ID.
							/*
							 * GDV --- Query inutile e causa di un outofMemory FascicoloSiepController lCtrl =
							 * new FascicoloSiepController(); FascicoloSiepModel lFasSiepPresente = new
							 * FascicoloSiepModel();
							 * 
							 * // Imposto la chiave Primaria per la ricerca.
							 * lFasSiepPresente.setIdFascicoloSiep(lFasSiepInviato.getIdFascicoloSiep());
							 * 
							 * Vector lVect = lCtrl.ExRicercaFascicoloSiep(lFasSiepPresente);
							 * 
							 * if ( lVect.size() > 0 ) { aMessaggio.setCodEsito("00001"); } else throw new
							 * F3BException(F3BException.USER_MESSAGE,
							 * "Impossibile inserire il Fascicolo Siep! Presente Un fascicolo SIEP con stesso ID ma ANNO/PROGR/UFFICIO diversi!"
							 * );
							 */}
					}
				}

				// FASCICOLO SIUS
				try {
					lFasSiusDao = new FascicoloSiusDAO(lConn);

					FascicoloSiusModel lFasSiu = lPars.getFascicoloGPSius().getFascicoloSiusModel();
					lFasSiusDao.setDAOFromModel(lFasSiu);
					lFasSiusDao.setWithoutSequence(true);
					lFasSiusDao.insert();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED || ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Fascicolo Sius gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Fascicolo Sius! ");
				}

				// GENERALE PROCEDIMENTO
				try {
					lGenProDao = new GeneraleProcedimentoDAO(lConn);

					GeneraleProcedimentoModel lGenPro = lPars.getFascicoloGPSius()
							.getGeneraleProcedimentoModel();
					lGenProDao.setDAOFromModel(lGenPro);
					lGenProDao.setWithoutSequence(true);
					lGenProDao.insert();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Generale Procedimento gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Generale Procedimento! ");
				}

				// RESIDENZA SIUS
				try {
					ResidenzaAssociataModel lResAssociata = lPars.getResidenzaAssociata();

					if (lResAssociata != null && lResAssociata.getResidenza() != null) {
						lResDao = new ResidenzaDAO(lConn);

						lResDao.setDAOFromModel(lResAssociata.getResidenza());
						lResDao.setWithoutSequence(true);
						lResDao.insert();
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Residenza gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire la Residenza! ");
				}

				// RESIDENZA_FASCICOLO_SIUS
				try {
					ResidenzaAssociataModel lResAssociata = lPars.getResidenzaAssociata();

					if (lResAssociata != null && lResAssociata.getResidenzaFascicoloSius() != null) {
						lResFasSiuDao = new ResidenzaFascicoloSiusDAO(lConn);

						// STUB 13/04/2005 Cancellazione prima del nuovo inserimento.
						lResFasSiuDao.setCondizioneDelete(lResAssociata.getResidenza().getIdResidenza(),
								lResAssociata.getResidenzaFascicoloSius().getFasSiuIdFascicoloSius());
						lResFasSiuDao.delete();
						lResFasSiuDao.stop();

						lResFasSiuDao.setDAOFromModel(lResAssociata.getResidenzaFascicoloSius());
						lResFasSiuDao.setWithoutSequence(true);
						lResFasSiuDao.insert();
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Associazione Residenza Fascicolo Sius gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire l'Associazione Residenza Fascicolo Sius! ");
				}

				// STUB 11/04/2005 Se l'Evento e' di un'altra BDI si cancella!
				EventoModel lEveOld = lPars.getEvento().getEvento();
				PulisciEventoStoreProcedureDAO lProcEve = null;
				if (!aMessaggio.getCodBdiDestinataria().equals(aMessaggio.getCodBdiMittente())
						&& lEveOld.getIdEvento().compareTo(null) != 0) {
					lProcEve = new PulisciEventoStoreProcedureDAO(lConn);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Partita Store Procerdure PULISCI EVENTO per EVENTO = "
							+ lEveOld.toString());
					lProcEve.setIdEvento(lEveOld.getIdEvento());
					lProcEve.execute();

					if (!lProcEve.getReturn().equals("0000")) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("ERRORE DURANTE LA STORE PROCEDURE Pulisci_Evento...");
						throw new DAOException(
								"Errore durante la chiamata alla Store Porcedure Pulisci_Evento");
					}
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Pulisci_Evento per EVENTO IdEvento = " + lEveOld.getIdEvento());
				}

				// EVENTO
				try {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("********* Evento = " + lPars.getEvento());

					lEveDao = new EventoDAO(lConn);

					// STUB 01/12/2004 Caricamento BLOB del documento.
					EventoNotificaModel lEve = lPars.getEvento();
					lEveDao.setDAOFromModel(lEve.getEvento());

					if (lEve.getEvento().getDocPerTrasferimento() != null
							&& lEve.getEvento().getDocPerTrasferimento().length > 1) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Controllo BBBLOBBB Out"
								+ lEve.getEvento().getDocPerTrasferimento().length);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Settato BLOBBBB");
						lEveDao.setDocBlob(new ByteArrayInputStream(lEve.getEvento().getDocPerTrasferimento()));
					}

					lEveDao.setWithoutSequence(true);
					lEveDao.insert();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Evento gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l'Evento! ");
				}

				// NOTIFICHE - AUTORITA ESTERNE
				NotificaModel[] lNotifiche = lPars.getEvento().getNotifiche();

				lAutDao = new AutoritaEsternaDAO(lConn);
				lNotDao = new NotificaDAO(lConn);
				lCampoNotaDao = new CampoNotaDAO(lConn);

				int count = 0;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Presenti " + lNotifiche.length + " notifiche");

				while (count < lNotifiche.length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Notifica[" + count + "] = " + lNotifiche[count]);

					if (lNotifiche[count] != null) {
						try {
							// AUTORITA ESTERNA
							if (lNotifiche[count].getAutoritaEsterna() != null) {
								lAutDao.setDAOFromModel(lNotifiche[count].getAutoritaEsterna());
								lAutDao.setWithoutSequence(true);
								lAutDao.insert();
								lAutDao.stop();
							}
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.info("Autorita Esterna gia' presente...");
								aMessaggio.setCodEsito("00001");
							} else
								throw new F3BException(F3BException.USER_MESSAGE,
										"Impossibile inserire la Notifica! ");
						}

						// AVVOCATO SIUS
						if (lNotifiche[count].getAvvIdAvvocatoFascicoloSius() != null
								&& lNotifiche[count].getAvvSius() != null) {
							try {
								lAvvDao = new AvvocatoDAO(lConn);

								lAvvDao.setDAOFromModel(lNotifiche[count].getAvvSius().getAvvocato());
								lAvvDao.setWithoutSequence(true);
								lAvvDao.insert();
								lAvvDao.stop();
							} catch (DAOException ex) {
								if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di LogF3B.getLogger()
									siesLogger.info("Avvocato gia' presente...");
									aMessaggio.setCodEsito("00001");
								} else
									throw new F3BException(F3BException.USER_MESSAGE,
											"Impossibile inserire l'Avvocato! ");
							}
							// }

							// AVVOCATO FASCICOLO SIUS
							// if (lNotifiche[count].getAvvIdAvvocatoFascicoloSius() != null)
							// {
							try {
								lAvvFasSiuDao = new AvvocatoFascicoloSiusDAO(lConn);

								// STUB 13/04/2005 Cancellazione prima del nuovo inserimento.
								lAvvFasSiuDao.setCondizioneUpdate(lNotifiche[count].getAvvSius()
										.getAvvocatoFascicoloSiusModel().getIdAvvocatoFascicoloSius());
								lAvvFasSiuDao.delete();
								lAvvFasSiuDao.stop();

								lAvvFasSiuDao.setDAOFromModel(lNotifiche[count].getAvvSius()
										.getAvvocatoFascicoloSiusModel());
								lAvvFasSiuDao.setWithoutSequence(true);
								lAvvFasSiuDao.insert();
								lAvvFasSiuDao.stop();
							} catch (DAOException ex) {
								if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di LogF3B.getLogger()
									siesLogger.info("Avvocato Fascicolo Sius gia' presente...");
									aMessaggio.setCodEsito("00001");
								} else
									throw new F3BException(F3BException.USER_MESSAGE,
											"Impossibile inserire l'Avvocato Fascicolo Sius! ");
							}
						}

						// NOTIFICA
						try {
							lNotDao.setDAOFromModel(lNotifiche[count]);
							lNotDao.setWithoutSequence(true);
							lNotDao.insert();
							lNotDao.stop();
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.info("Notifica gia' presente...");
								aMessaggio.setCodEsito("00001");
							} else
								throw new F3BException(F3BException.USER_MESSAGE,
										"Impossibile inserire la Notifica! ");
						}
					}

					count++;
				}

				// NOTE AGGIUNTIVE
				try {
					// Inserimento delle eventuali note aggiuntive.
					CampoNotaModel[] lNote = lPars.getEvento().getCampoNote();
					if (lNote != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
								+ lNote.length);

						count = 0;
						while (count < lNote.length) {
							lCampoNotaDao.setDAOFromModel(lNote[count]);
							lCampoNotaDao.setWithoutSequence(true);
							lCampoNotaDao.insert();
							lCampoNotaDao.stop();

							count++;
						}
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Note gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire le Note! ");
				}

				// STUB 12/04/2005 DOCUMENTO ALLEGATO
				try {
					lDocAllDAO = new DocumentoAllegatoDAO(lConn);

					DocumentoAllegatoModel lDocAll = lPars.getDocumentoAllegato();

					if (lDocAll != null) {
						lDocAllDAO.setDAOFromModel(lDocAll);

						// STUB 13/04/2005 Caricamento BLOB del documento.
						if (lDocAll.getDocPerTrasferimento() != null
								&& lDocAll.getDocPerTrasferimento().length > 1) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Settato BLOBBBB");
							lDocAllDAO.setDocBlob(new ByteArrayInputStream(lDocAll.getDocPerTrasferimento()));
						}
						lDocAllDAO.setWithoutSequence(true);
						lDocAllDAO.insert();
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Documento Allegato gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Documento Allegato! ");
				}

				// STUB 15/04/2005 LUOGO DETENZIONE.
				try {
					lLuoDetDAO = new LuogoDetenzioneDAO(lConn);

					LuogoDetenzioneModel lLuoDet = lPars.getLuogoDetenzione();

					if (lLuoDet != null) {
						lLuoDetDAO.setDAOFromModel(lLuoDet);
						lLuoDetDAO.setWithoutSequence(true);
						lLuoDetDAO.insert();
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Luogo Detenzione gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Luogo Detenzione! ");
				}

				// STUB 15/04/2005 RIFERIMENTI FASCICOLI SIEP.
				List mRifasiep = lPars.getRifasiep();
				if ((mRifasiep != null) && (!mRifasiep.isEmpty())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("size di Riferimenti Fascicoli SIEP : " + mRifasiep.size());
					for (int i = 0; i < mRifasiep.size(); i++) {
						try {
							lRifasiepDAO = new RiferimentoFascicoloSiepDAO(lConn);
							lRifasiepDAO.setDAOFromModel((RiferimentoFascicoloSiepModel) mRifasiep.get(i));
							lRifasiepDAO.setWithoutSequence(true);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("lRifasiepDAO.insert() N.ro : " + (i + 1));
							lRifasiepDAO.insert();
							lRifasiepDAO.stop();
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.info("Riferimento Fascicolo SIEP gia' presente...");
								aMessaggio.setCodEsito("00001");
							} else
								throw new F3BException(F3BException.USER_MESSAGE,
										"Impossibile inserire il Riferimento Fascicolo SIEP! ");
						}
					}
				}

				// MISURA ALTERNATIVA
				try {
					lMisDao = new MisuraAlternativaDAO(lConn);

					MisuraAlternativaModel lMisura = lPars.getMisuraAlternativa();

					if (lMisura != null) {
						// Aggiorna le Note inserite durante la presa in carico
						// lMisura.setNote(aMisAlt.getNote());

						lMisDao.setDAOFromModel(lMisura);
						lMisDao.setWithoutSequence(true);
						lMisDao.insert();
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Misura Alternativa gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire la misura alternativa! ");
				}

				// DEPOSITO DECRETO
				try {
					DepositoDecretoModel lDepDecr = lPars.getDepositoDecreto();

					if (lDepDecr != null) {
						lDepDecrDao = new DepositoDecretoDAO(lConn);

						lDepDecrDao.setDAOFromModel(lDepDecr);
						lDepDecrDao.setWithoutSequence(true);
						lDepDecrDao.insert();
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Deposito Decreto gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Deposito Decreto! ");
				}

				// MOTIVAZIONE DECRETO
				if (lPars.getMotivazioniDecreto() != null) {
					List mMotivazioniDecreto = lPars.getMotivazioniDecreto();

					if (!mMotivazioniDecreto.isEmpty()) {
						lMotDecrDao = new MotivazioneDecretoDAO(lConn);

						for (int i = 0; i < mMotivazioniDecreto.size(); i++) {
							try {
								lMotDecrDao.setDAOFromModel((MotivazioneDecretoModel) mMotivazioniDecreto
										.get(i));
								lMotDecrDao.setWithoutSequence(true);
								lMotDecrDao.insert();
							} catch (DAOException ex) {
								if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di LogF3B.getLogger()
									siesLogger.info("Motivazione Decreto gia' presente...");
									aMessaggio.setCodEsito("00001");
								} else
									throw new F3BException(F3BException.USER_MESSAGE,
											"Impossibile inserire la Motivazione Decreto! -" + i + "- "
													+ mMotivazioniDecreto.get(i));
							}
						}
					}
				}

				// DEPOSITO ORDINANZA PC
				try {
					DepositoOrdinanzaPcModel lDepOrdPc = lPars.getDepositoOrdinanzaPc();

					if (lDepOrdPc != null) {
						lOrdDao = new DepositoOrdinanzaPcDAO(lConn);

						lOrdDao.setDAOFromModel(lDepOrdPc);
						lOrdDao.setWithoutSequence(true);
						lOrdDao.insert();
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Deposito Ordinanza gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Deposito Ordinanza! ");
				}

				// PRESCRIZIONI
				List mPrescrizioni = lPars.getPrescrizioni();

				if ((mPrescrizioni != null) && (!mPrescrizioni.isEmpty())) {
					lPresrDao = new PrescrizioneDAO(lConn);

					for (int i = 0; i < mPrescrizioni.size(); i++) {
						try {
							lPresrDao.setDAOFromModel((PrescrizioneModel) mPrescrizioni.get(i));
							lPresrDao.setWithoutSequence(true);
							lPresrDao.insert();
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.info("Prescrizione gia' presente...");
								aMessaggio.setCodEsito("00001");
							} else
								throw new F3BException(F3BException.USER_MESSAGE,
										"Impossibile inserire la Prescrizione! ");
						}
					}
				}

				// IMPUGNAZIONE
				ImpugnazioneModel lImp = lPars.getImpugnazione();

				if (lImp != null) {
					try {
						lImpDao = new ImpugnazioneDAO(lConn);

						lImpDao.setDAOFromModel(lImp);
						lImpDao.setWithoutSequence(true);
						lImpDao.insert();
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Impugnazione gia' presente...");
							aMessaggio.setCodEsito("00001");
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire l'Impugnazione! ");
					}
				}

				// TENORI
				List mTenori = lPars.getTenori();

				if (mTenori != null && !mTenori.isEmpty()) {
					lTenDao = new TenoreDAO(lConn);

					for (int i = 0; i < mTenori.size(); i++) {
						try {
							lTenDao.setDAOFromModel((TenoreModel) mTenori.get(i));
							lTenDao.setWithoutSequence(true);
							lTenDao.insert();
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.info("Tenore gia' presente...");
								aMessaggio.setCodEsito("00001");
							} else
								throw new F3BException(F3BException.USER_MESSAGE,
										"Impossibile inserire il Tenore! ");
						}
					}
				}

				// *** AGGIORNA LA POSIZIONE GIURIDICA IN BASE ALLA MISURA ALTERNATIVA ***
				// Posizione Giuridica
				EventoModel lEve = lPars.getEvento().getEvento();
				lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
				lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lEve.getFasSieIdFascicoloSiep());
				/*PosizioneGiuridicaModel lPosizione = (PosizioneGiuridicaModel) */lPosSqlDao.getModelByKey();

				// Misura Alternativa
				MisuraAlternativaModel lMisura = lPars.getMisuraAlternativa();

				// *** AGGIORNA LO STATO PROCEDIMENTO IN BASE ALLA MISURA ALTERNATIVA ***
				if (lMisura != null && lEve != null) {
					String lStatoProcedimento = null;
					String lNatura = lMisura.getCodNaturaDecisione();
					String lTipoMisura = lMisura.getCodTipoMisura();

					if (lNatura != null && !lNatura.equals("") && lTipoMisura != null
							&& !lTipoMisura.equals("")) {
						// CONC.AFFIDAMENTO
						if (lNatura.equals("CO")
								&& (lTipoMisura.equals("0001") || lTipoMisura.equals("0002") || lTipoMisura
										.equals("0003"))) {
							lStatoProcedimento = "0022";
						}
						// CONC.DET.DOM.
						if (lNatura.equals("CO")
								&& (lTipoMisura.equals("0005") || lTipoMisura.equals("0010") || lTipoMisura
										.equals("0013"))) {
							lStatoProcedimento = "0026";
						}
						// CONC.SEMILIBERTA'
						if (lNatura.equals("CO") && (lTipoMisura.equals("0004"))) {
							lStatoProcedimento = "0030";
						}
						// RIPRISTINO AFFIDAMENTO
						if (lNatura.equals("RG")
								&& (lTipoMisura.equals("0014") || lTipoMisura.equals("0015") || lTipoMisura
										.equals("0086"))) {
							lStatoProcedimento = "0041";
						}
						// RIPRISTINO DET.DOM.
						if (lNatura.equals("RG")
								&& (lTipoMisura.equals("0016") || lTipoMisura.equals("0087") || lTipoMisura
										.equals("0089"))) {
							lStatoProcedimento = "0042";
						}
						// RIPRISTINO SEMILIBERTA'
						if (lNatura.equals("RG") && (lTipoMisura.equals("0091"))) {
							lStatoProcedimento = "0043";
						}
						// REVOCA AFFIDAMENTO
						if (lNatura.equals("RE")
								&& (lTipoMisura.equals("0014") || lTipoMisura.equals("0015") || lTipoMisura
										.equals("0086"))) {
							lStatoProcedimento = "0049";
						}
						// REVOCA DET.DOM.
						if (lNatura.equals("RE")
								&& (lTipoMisura.equals("0016") || lTipoMisura.equals("0087") || lTipoMisura
										.equals("0089"))) {
							lStatoProcedimento = "0050";
						}
						// REVOCA SEMILIBERTA'
						if (lNatura.equals("RE") && (lTipoMisura.equals("0091"))) {
							lStatoProcedimento = "0051";
						}
						// REVOCA MISURA ALTERNATIVA
						if (lNatura.equals("RE") && (lTipoMisura.equals("0000"))) {
							lStatoProcedimento = "0052";
						}

					}

					if (lStatoProcedimento != null) {
						lStatoDao = new StatoProcedimentoDAO(lConn);
						// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
						lStatoDao.setCondizioneByIdFascicolo(lEve.getFasSieIdFascicoloSiep());
						lStatoDao.delete();

						// - Inserisci STATO PROCEDIMENTO
						StatoProcedimentoModel lStaProMod = new StatoProcedimentoModel();

						lStaProMod.setProgressivo(new BigDecimal(1));
						lStaProMod.setCodStatoProcedimento(lStatoProcedimento);
						lStaProMod.setData(lEve.getDataEmissione());

						lStaProMod.setFasSieIdFascicoloSiep(lEve.getFasSieIdFascicoloSiep());
						lStaProMod.setCodOperatoreInserimento(aMisAlt.getCodOperatoreAggiornamento());
						lStaProMod.setDataInserimento(aMisAlt.getDataAggiornamento());
						lStaProMod.setCodUfficioInserimento(aMisAlt.getCodUfficioAggiornamento());

						lStatoDao.setDAOFromModel(lStaProMod);
						lStatoDao.insert();
					}
				}

				commit(lConn);
			}
		} catch (Exception sqe) {
			rollback(lConn);

			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("SQLException: " + sqe);
			throw new F3BException(" .ExPresaInCaricoRicorso: " + sqe);
		} finally {
			cleanup(lSogDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lFasSiepDao);
			cleanup(lCampoNotaDao);
			cleanup(lFasSiusDao);
			cleanup(lGenProDao);
			cleanup(lMisDao);
			cleanup(lOrdDao);
			cleanup(lPresrDao);
			cleanup(lImpDao);
			cleanup(lTenDao);
			cleanup(lResDao);
			cleanup(lResFasSiuDao);
			cleanup(lAvvDao);
			cleanup(lAvvFasSiuDao);
			cleanup(lPosSqlDao);
			cleanup(lDepDecrDao);
			cleanup(lMotDecrDao);
			cleanup(lPosDao);
			cleanup(lStatoDao);
			cleanup(lSentenzaDao);
			cleanup(lDocAllDAO); // STUB 13/04/2005.
			cleanup(lLuoDetDAO); // STUB 15/04/2005.
			cleanup(lRifasiepDAO); // STUB 15/04/2005.

			cleanup(lConn);
		}

		return aMessaggio;
	}

	public MessaggioModel ExPresaInCaricoProvvedimento(MessaggioModel aMessaggio, Connection aConn)
			throws Exception {

		ParserMessage lPars;
		if (aMessaggio.getTreeModel() != null)
			lPars = new ParserMessage(aMessaggio.getTreeModel());
		else
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire il Messaggio Contenuto incorretto!");

		if (aMessaggio.getCodBdiDestinataria().trim().compareTo(aMessaggio.getCodBdiMittente().trim()) != 0) {
			// Prelievo del Soggetto dal messaggio e suo inserimento nel DB
			ExScaricaSoggetto(lPars, aConn);

			// Prelievo della Sentenza e suo inserimento nel DB
			aMessaggio = ExScaricaSentenza(lPars, aConn, aMessaggio);

			// 11/10/2010 Prelievo del Soggetto SIEP.
			aMessaggio = ExScaricaSoggettoSIEP(lPars, aConn, aMessaggio);

			// Prelievo del Fascicolo SIEP e suo Inserimento nel DB
//			FascicoloSiepModel lFasSiepInviato = new FascicoloSiepModel();
			/*lFasSiepInviato = */ExScaricaFascicoloSiep(lPars, aConn, aMessaggio);

			// EVENTO
			/*EventoNotificaModel lEvento = */ExScaricaEvento(lPars, aConn, aMessaggio);

			// LUOGO DETENZIONE.
			// ExScaricaLuogoDetenzione(lPars, aConn, aMessaggio);

			// RIFERIMENTI FASCICOLI SIEP.
			// ExScaricaRiferimentiFascicoloSIEP(lPars, aConn, aMessaggio);

			// MISURA ALTERNATIVA
			// ExScaricaMisuraAlternativa(lPars, aConn, aMessaggio);

			// NOTIFICHE - AUTORITA ESTERNE
			// ExScaricaNotifiche(lPars, aConn, aMessaggio);

			// *** AGGIORNA LA POSIZIONE GIURIDICA IN BASE ALLA MISURA ALTERNATIVA ***
			// Posizione Giuridica
			// ExAggiornaPosizioneGiuridica(lPars, aConn, aMessaggio, aMisAlt);

			// *** AGGIORNA LO STATO PROCEDIMENTO IN BASE ALLA MISURA ALTERNATIVA ***
			// ExAggiornaStatoProcedimento(lPars, aConn, aMessaggio, aMisAlt);
		}
		return aMessaggio;
	}

	public MessaggioModel ExPresaInCaricoRichiestaRelazione(MessaggioModel aMessaggio, Connection aConn)
			throws Exception {

		ParserMessage lPars;
		if (aMessaggio.getTreeModel() != null)
			lPars = new ParserMessage(aMessaggio.getTreeModel());
		else
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire il Messaggio Contenuto incorretto!");

		if (aMessaggio.getCodBdiDestinataria().trim().compareTo(aMessaggio.getCodBdiMittente().trim()) != 0) {
			// Prelievo del Soggetto dal messaggio e suo inserimento nel DB
			ExScaricaSoggetto(lPars, aConn);

			// Prelievo della Sentenza e suo inserimento nel DB
			aMessaggio = ExScaricaSentenza(lPars, aConn, aMessaggio);

			// Prelievo del Fascicolo SIEP e suo Inserimento nel DB
//			FascicoloSiepModel lFasSiepInviato = new FascicoloSiepModel();
			/*lFasSiepInviato = */ExScaricaFascicoloSiep(lPars, aConn, aMessaggio);

			// Prelievo del Fascicolo SIUS e suo Inserimento nel DB
			/*FascicoloSiusModel lFasSiu = */ExScaricaFascicoloSius(lPars, aConn, aMessaggio);

			// Prelievo del GENERALE PROCEDIMENTO e suo Inserimento nel DB
			/*GeneraleProcedimentoModel lGenPro = */ExScaricaGeneraleProcedimento(lPars, aConn, aMessaggio);

			// RESIDENZA
			/*ResidenzaAssociataModel lResAssociata = */ExScaricaResidenza(lPars, aConn, aMessaggio);

			// RESIDENZA_FASCICOLO_SIUS
			/*lResAssociata = */ExScaricaResidenzaSius(lPars, aConn, aMessaggio);

			// EVENTO
			/*EventoNotificaModel lEvento = */ExScaricaEvento(lPars, aConn, aMessaggio);

			// LUOGO DETENZIONE.
			ExScaricaLuogoDetenzione(lPars, aConn, aMessaggio);

		}
		return aMessaggio;
	}

	// / LUIGI 28-06-2006
	private void ExScaricaSoggetto(ParserMessage aParseMess, Connection aConn) throws F3BException {
		SoggettoDAO lSogDao = null;
		try {
			if (aParseMess.getSoggetto() != null) {

				lSogDao = new SoggettoDAO(aConn);
				lSogDao.setDAOFromModel(aParseMess.getSoggetto());
				lSogDao.setWithoutSequence(true);
				lSogDao.insert();
				lSogDao.stop();
			} else if (aParseMess.getFascicoloGPSius() != null
					&& aParseMess.getFascicoloGPSius().getFascicoloSiusModel() != null
					&& aParseMess.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto() != null) {
				lSogDao = new SoggettoDAO(aConn);
				lSogDao.setDAOFromModel(aParseMess.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto());
				lSogDao.setWithoutSequence(true);
				lSogDao.insert();
				lSogDao.stop();
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Soggetto perche' non ricevuto nelle Presa in Carico! ");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Soggetto gia' presente...");
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire il Soggetto! ");
		} finally {
			cleanup(lSogDao);
		}
	}

	// Scarica il soggetto dal FascicoloSiepeEsteso.
	private void ExScaricaSoggetto(FascicoloSiepeEstesoModel aFasSiepeEsteso, Connection aConn)
			throws F3BException {
		SoggettoDAO lSogDao = null;
		try {
			if (aFasSiepeEsteso.getSoggetto() != null) {
				lSogDao = new SoggettoDAO(aConn);
				lSogDao.setDAOFromModel(aFasSiepeEsteso.getSoggetto());
				lSogDao.setWithoutSequence(true);
				lSogDao.insert();
				lSogDao.stop();
			} else if (aFasSiepeEsteso.getFascicoloSius() != null
					&& aFasSiepeEsteso.getFascicoloSius().getFascicoloSiusModel() != null
					&& aFasSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getSoggetto() != null) {
				lSogDao = new SoggettoDAO(aConn);
				lSogDao.setDAOFromModel(aFasSiepeEsteso.getFascicoloSius().getFascicoloSiusModel()
						.getSoggetto());
				lSogDao.setWithoutSequence(true);
				lSogDao.insert();
				lSogDao.stop();
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Soggetto perche' non ricevuto nella Presa in Carico! ");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Soggetto gia' presente...");
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire il Soggetto! ");
		} finally {
			cleanup(lSogDao);
		}
	}

	private MessaggioModel ExScaricaSentenza(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		SentenzaDAO lSentenzaDao = null;
		try {
			SentenzaModel lSentenza = aParseMess.getSentenza();
			if (lSentenza != null && lSentenza.getIdSentenza() != null) {
				lSentenzaDao = new SentenzaDAO(aConn);
				lSentenzaDao.setDAOFromModel(lSentenza);
				lSentenzaDao.setIdSentenza(lSentenza.getIdSentenza());
				lSentenzaDao.setWithoutSequence(true);
				lSentenzaDao.insert();
				lSentenzaDao.stop();
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Sentenza Assente!!!");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Sentenza gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Sentenza! ");
		} finally {
			cleanup(lSentenzaDao);
		}
		return aMessaggio;
	}

	// Scarica la sentenza dal FascicoloSiepeEsteso.
	private void ExScaricaSentenza(FascicoloSiepeEstesoModel aFasSiepeEsteso, Connection aConn)
			throws F3BException {
		SentenzaDAO lSentenzaDao = null;
		try {
			if (aFasSiepeEsteso.getFascicoloSiep().getSentenza() != null
					&& aFasSiepeEsteso.getFascicoloSiep().getSentenza().getIdSentenza() != null) {
				lSentenzaDao = new SentenzaDAO(aConn);
				lSentenzaDao.setDAOFromModel(aFasSiepeEsteso.getFascicoloSiep().getSentenza());
				lSentenzaDao.setIdSentenza(aFasSiepeEsteso.getFascicoloSiep().getSentenza().getIdSentenza());
				lSentenzaDao.setWithoutSequence(true);
				lSentenzaDao.insert();
				lSentenzaDao.stop();
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Sentenza Assente!!!");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Sentenza gia' presente...");
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Sentenza! ");
		} finally {
			cleanup(lSentenzaDao);
		}
	}

	private FascicoloSiepModel ExScaricaFascicoloSiep(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		FascicoloSiepDAO lFasSiepDao = null;
		FascicoloSiepModel lFasSiepInviato = null;
		try {
			lFasSiepInviato = aParseMess.getFascicolo();
			if (lFasSiepInviato != null) {

				lFasSiepDao = new FascicoloSiepDAO(aConn);
				lFasSiepDao.setDAOFromModel(lFasSiepInviato);
				lFasSiepDao.setWithoutSequence(true);
				lFasSiepDao.insert();
				lFasSiepDao.stop();
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fascicolo SIEP Assente !!!");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				if (ex.getMessage().indexOf("FAS_SIE_PK") > 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Fascicolo SIEP gia' presente...");
					aMessaggio.setCodEsito("00001");
				}
				// STUB 18/03/2004 Se scatta la costraint FAS_ANN_UFF_PRO_FK_I controllo la coppia di ID.
				else if (ex.getMessage().indexOf("FAS_ANN_UFF_PRO_FK_I") > 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Fascicolo SIEP gia' presente...");
					aMessaggio.setCodEsito("00001");
				}
			}
		} finally {
			cleanup(lFasSiepDao);
		}
		return lFasSiepInviato;
	}

	private void ExScaricaFascicoloSiep(FascicoloSiepeEstesoModel aFasEsteso, Connection aConn)
			throws F3BException {
		FascicoloSiepDAO lFasSiepDao = null;
		try {
			if (aFasEsteso.getFascicoloSiep().getIdFascicoloSiep() != null) {
				lFasSiepDao = new FascicoloSiepDAO(aConn);
				lFasSiepDao.setDAOFromModel(aFasEsteso.getFascicoloSiep());
				lFasSiepDao.setWithoutSequence(true);
				lFasSiepDao.insert();
				lFasSiepDao.stop();
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fascicolo SIEP Assente !!!");
		}

		catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				if (ex.getMessage().indexOf("FAS_SIE_PK") > 0)
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Fascicolo SIEP gia' presente...");

				// Se scatta la costraint FAS_ANN_UFF_PRO_FK_I controllo la coppia di ID.
				else if (ex.getMessage().indexOf("FAS_ANN_UFF_PRO_FK_I") > 0)
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Fascicolo SIEP gia' presente...");
			}
		} finally {
			cleanup(lFasSiepDao);
		}
	}

	private FascicoloSiusModel ExScaricaFascicoloSius(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		FascicoloSiusDAO lFasSiusDao = null;
		FascicoloSiusModel lFasSius = null;
		try {
			if (aParseMess.getFascicoloGPSius() != null
					&& aParseMess.getFascicoloGPSius().getFascicoloSiusModel() != null
					&& aParseMess.getFascicoloGPSius().getFascicoloSiusModel().getIdFascicoloSius() != null) {
				lFasSius = aParseMess.getFascicoloGPSius().getFascicoloSiusModel();

				lFasSiusDao = new FascicoloSiusDAO(aConn);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fascicolo SIUS " + lFasSius);

				lFasSiusDao.setDAOFromModel(lFasSius);
				lFasSiusDao.setWithoutSequence(true);
				lFasSiusDao.insert();
				lFasSiusDao.stop();
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fascicolo SIUS Assente !!!");

		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED || ex.INTEGRITY_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fascicolo SIUS gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire il Fascicolo Sius! ");
		} finally {
			cleanup(lFasSiusDao);
		}

		return lFasSius;
	}

	private void ExScaricaFascicoloSius(FascicoloSiepeEstesoModel aFasEsteso, Connection aConn)
			throws F3BException {
		FascicoloSiusDAO lFasSiusDao = null;
		try {
			if (aFasEsteso.getFascicoloSius() != null
					&& aFasEsteso.getFascicoloSius().getFascicoloSiusModel() != null
					&& aFasEsteso.getFascicoloSius().getFascicoloSiusModel().getIdFascicoloSius() != null) {
				lFasSiusDao = new FascicoloSiusDAO(aConn);

				lFasSiusDao.setDAOFromModel(aFasEsteso.getFascicoloSius().getFascicoloSiusModel());
				lFasSiusDao.setWithoutSequence(true);
				lFasSiusDao.insert();
				lFasSiusDao.stop();
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fascicolo SIUS Assente !!!");

		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED || ex.INTEGRITY_CONSTRAINT_VIOLATED)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fascicolo SIUS gia' presente...");
			else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire il Fascicolo Sius! ");
		} finally {
			cleanup(lFasSiusDao);
		}
	}

	private GeneraleProcedimentoModel ExScaricaGeneraleProcedimento(ParserMessage aParseMess,
			Connection aConn, MessaggioModel aMessaggio) throws F3BException {
		GeneraleProcedimentoDAO lGenProDao = null;
		GeneraleProcedimentoModel lGenPro = null;

		try {
			lGenProDao = new GeneraleProcedimentoDAO(aConn);

			if (aParseMess.getFascicoloGPSius() != null
					&& aParseMess.getFascicoloGPSius().getGeneraleProcedimentoModel() != null) {
				lGenPro = aParseMess.getFascicoloGPSius().getGeneraleProcedimentoModel();

				lGenProDao.setDAOFromModel(lGenPro);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("GENERALE---PROC----->" + lGenPro);
				lGenProDao.setWithoutSequence(true);
				lGenProDao.insert();
				lGenProDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Generale Procedimento gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Generale Procedimento! ");
		} finally {
			cleanup(lGenProDao);
		}
		return lGenPro;
	}

	private void ExScaricaGeneraleProcedimento(FascicoloSiepeEstesoModel aFasEsteso, Connection aConn)
			throws F3BException {
		GeneraleProcedimentoDAO lGenProDao = null;

		try {
			lGenProDao = new GeneraleProcedimentoDAO(aConn);

			if (aFasEsteso.getFascicoloSius() != null
					&& aFasEsteso.getFascicoloSius().getGeneraleProcedimentoModel() != null) {
				lGenProDao.setDAOFromModel(aFasEsteso.getFascicoloSius().getGeneraleProcedimentoModel());
				lGenProDao.setWithoutSequence(true);
				lGenProDao.insert();
				lGenProDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Generale Procedimento gia' presente...");
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Generale Procedimento! ");
		} finally {
			cleanup(lGenProDao);
		}
	}

	private void ExScaricaFascicoloSiepe(ParserMessage aParseMess, Connection aConn, MessaggioModel aMessaggio)
			throws F3BException, Exception {
		FascicoloSiepeEstesoModel lFasSiepeEstesoInviato = null;
		try {
			lFasSiepeEstesoInviato = aParseMess.getFascicoloSiepeEsteso();
			if (lFasSiepeEstesoInviato != null) {
				// Scarica SOGGETTO
				if (lFasSiepeEstesoInviato.getSoggetto() != null)
					ExScaricaSoggetto(lFasSiepeEstesoInviato, aConn);

				// Scarico della SENTENZA.
				if (lFasSiepeEstesoInviato.getFascicoloSiep() != null
						&& lFasSiepeEstesoInviato.getFascicoloSiep().getSentenza() != null)
					ExScaricaSentenza(lFasSiepeEstesoInviato, aConn);

				// Scarico del Fascicolo SIEP.
				if (lFasSiepeEstesoInviato.getFascicoloSiep() != null)
					ExScaricaFascicoloSiep(lFasSiepeEstesoInviato, aConn);

				// Scarico del Fascicolo SIUS.
				if (lFasSiepeEstesoInviato.getFascicoloSius() != null)
					ExScaricaFascicoloSius(lFasSiepeEstesoInviato, aConn);

				// Scarico del GENERALE PROCEDIMENTO.
				if (lFasSiepeEstesoInviato.getFascicoloSius() != null)
					ExScaricaGeneraleProcedimento(lFasSiepeEstesoInviato, aConn);

				// Scarico dell'EVENTO.
				if (lFasSiepeEstesoInviato.getEvento() != null)
					ExScaricaEvento(lFasSiepeEstesoInviato, aMessaggio, aConn);

				// Scarica Deposito Decreto dal ParserMessage
				if (aParseMess.getDepositoDecreto() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(">>>> Si inserisce il deposito decreto !!!");
					ExScaricaDepositoDecreto(aParseMess, aConn, aMessaggio);
				}

				// Scarico dei TENORI.
				if (lFasSiepeEstesoInviato.getFascicoloSius() != null)
					ExScaricaTenori(lFasSiepeEstesoInviato, aConn);

				// Scarica FascicoloSiepe
				if (lFasSiepeEstesoInviato.getFascicoloSiepe() != null)
					ExScaricaFascicoloSiepe(lFasSiepeEstesoInviato, aConn);
			}
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "");
			ex.printStackTrace();
			throw ex;
		}
	}

	private void ExScaricaFascicoloSiepe(FascicoloSiepeEstesoModel aFasEsteso, Connection aConn)
			throws F3BException {
		FascicoloSiepeDAO lFasSiepeDao = null;

		try {
			lFasSiepeDao = new FascicoloSiepeDAO(aConn);
			lFasSiepeDao.setDAOFromModel(aFasEsteso.getFascicoloSiepe());
			lFasSiepeDao.setWithoutSequence(true);
			lFasSiepeDao.insert();
			lFasSiepeDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Fascicolo SIEPE Inserito");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				if (ex.getMessage().indexOf("FAS_SIEPE_PK") > 0)
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Fascicolo SIEPE gia' presente...");

				// Scatta la costraint FAS_SIEPE_ANN_UFF_PRO_FK_I.
				else if (ex.getMessage().indexOf("FAS_SIEPE_ANN_UFF_PRO_FK_I") > 0)
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Fascicolo SIEPE gia' presente...");

			}
		} finally {
			cleanup(lFasSiepeDao);
		}
	}

	/**
	 * Metodo che si occupa di recuperare la richiesta ricevuta e inserire la stessa nel Dbase
	 * <p>
	 * 
	 * @param aParseMess
	 *            TreeModel contente i dati opportuni per il recupero della richiesta.
	 * @param aConn
	 *            Connessione al Dbase.
	 * @param aMessaggio
	 *            Oggetto della classe Messaggio passato per reference.
	 * @return Ritorna oggetto di RichiestaModel inizializzato e opportunamente valorizzato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	private RichiestaModel ExScaricaRichiestaSiepe(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		RichiestaDAO lRichDao = null;
		RichiestaModel lRichiestaInviata = null;

		try {
			// Recupera dal TreeModel l'oggetto Richiesta.
			lRichiestaInviata = aParseMess.getRichiesta();
			// Se esiste una richiesta nel TreeModel l'inserisce nella tabella Richiesta
			// con lo stesso id-seq della BDI mittente.
			if (lRichiestaInviata != null) {
				lRichDao = new RichiestaDAO(aConn);
				lRichDao.setDAOFromModel(lRichiestaInviata);

				// Recupero del BLOB trasmesso
				if (lRichiestaInviata.getDocPerTrasferimento() != null
						&& lRichiestaInviata.getDocPerTrasferimento().length > 1)
					lRichDao.setDocBlob(new ByteArrayInputStream(lRichiestaInviata.getDocPerTrasferimento()));

				lRichDao.setWithoutSequence(true); // Inserisce senza chiedere l'id sequence.
				lRichDao.insert();
				lRichDao.stop();
				// Recupera le Relazioni e le inserisce nel dbase.
				ExScaricaRelazioni(lRichiestaInviata, aConn);
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Richiesta SIEPE Assente !!!");
		} catch (DAOException ex) {
			// Se la Richiesta ricevuta gia' esiste in archivio, viene segnalato nel
			// file di log e impostato un codice esito pari a 00001.
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				if (ex.getMessage().indexOf("RICHIESTA_PK") > 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Richiesta gia' presente...");
					aMessaggio.setCodEsito("00001");
				}
			}
		} finally {
			cleanup(lRichDao);
		}

		return lRichiestaInviata; // Ritorna i dati di Richiesta recuperati dal TreeModel.
	}

	private AttivitaModel ExScaricaAttivitaSiepe(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		AttivitaDAO lAttDao = null;
		AttivitaModel lAttivitaInviata = null;
		try {
			lAttivitaInviata = aParseMess.getAttivita();
			if (lAttivitaInviata != null) {
				lAttDao = new AttivitaDAO(aConn);
				lAttDao.setDAOFromModel(lAttivitaInviata);

				// Recupero del BLOB trasmesso
				if (lAttivitaInviata.getDocPerTrasferimento() != null
						&& lAttivitaInviata.getDocPerTrasferimento().length > 1)
					lAttDao.setDocBlob(new ByteArrayInputStream(lAttivitaInviata.getDocPerTrasferimento()));

				lAttDao.setWithoutSequence(true);
				lAttDao.insert();
				lAttDao.stop();
				// preleva le relazioni ad essa riferite.
				ExScaricaRelazioni(lAttivitaInviata, aConn);
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Attivita SIEPE Assente !!!");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				if (ex.getMessage().indexOf("ATTIVITA_PK") > 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Attivita gia' presente...");
					aMessaggio.setCodEsito("00001");
				}
			}
		} finally {
			cleanup(lAttDao);
		}

		return lAttivitaInviata;
	}

	/**
	 * Metodo che si occupa di recuperare e scrivere le Relazioni afferenti ad un'attivita' o relazione.
	 * <p>
	 * 
	 * @param aModel
	 *            Model GenericModel, vengono accettate istanze di AttivitaModel o RichiestaModel.
	 * @param aConn
	 *            Connessione al dbase.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	private void ExScaricaRelazioni(GenericModel aModel, Connection aConn) throws F3BException {
		RelazioneDAO lRelDao = null;
		List mRelazioni = null;

		// Esegue controllo istanza di aModel ed il relativo popolamento
		// dell'oggetto mRelazioni.
		if (aModel instanceof AttivitaModel)
			mRelazioni = Arrays.asList(((AttivitaModel) aModel).getRelazioni());
		else if (aModel instanceof RichiestaModel)
			mRelazioni = Arrays.asList(((RichiestaModel) aModel).getRelazioni());
		else
			throw new F3BException(F3BException.SYSTEM_ERROR,
					"Il model dati passato non e' del tipo AttivitaModel o RichiestaModel.");

		// Se esistono relazioni nell'insieme, inserisce le stesse nel Dbase.
		if (mRelazioni != null && !mRelazioni.isEmpty()) {
			lRelDao = new RelazioneDAO(aConn);
			RelazioneModel lRelazione = null;

			for (int i = 0; i < mRelazioni.size(); i++) {
				try {
					// Relazione corrente nella lista
					lRelazione = (RelazioneModel) mRelazioni.get(i);

					lRelDao.setDAOFromModel(lRelazione);

					// Recupero del BLOB trasmesso
					if (lRelazione.getDocPerTrasferimento() != null
							&& lRelazione.getDocPerTrasferimento().length > 1)
						lRelDao.setDocBlob(new ByteArrayInputStream(lRelazione.getDocPerTrasferimento()));

					lRelDao.setWithoutSequence(true);

					lRelDao.insert();
					lRelDao.stop();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Relazione gia' presente...");
					else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire la Relazione ");
				}
			}
		}
	}

	/*
	 * 
	 * private void ExScaricaRelazioni(AttivitaModel aAttivita, Connection aConn ) throws F3BException {
	 * RelazioneDAO lRelDao = null;
	 * 
	 * List mRelazioni = Arrays.asList(aAttivita.getRelazioni());
	 * 
	 * if (mRelazioni != null && !mRelazioni.isEmpty()) { lRelDao = new RelazioneDAO(aConn);
	 * 
	 * for (int i = 0; i < mRelazioni.size(); i++) { try { lRelDao.setDAOFromModel( (RelazioneModel)
	 * mRelazioni.get(i)); lRelDao.setWithoutSequence(true); lRelDao.insert(); } catch (DAOException ex) { if
	 * (// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() (ex.UNIQUE_CONSTRAINT_VIOLATED) siesLogger.info("Relazione gia' presente..."); else
	 * throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Relazione "); } } } }
	 */
	private AssistenteSocialeModel ExScaricaAssistenteSociale(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		AssistenteSocialeDAO lAssDao = null;
		AssistenteSocialeModel lAssistenteSocialeInviato = null;
		try {
			lAssistenteSocialeInviato = aParseMess.getAssistenteSociale();
			if (lAssistenteSocialeInviato != null) {
				lAssDao = new AssistenteSocialeDAO(aConn);
				lAssDao.setDAOFromModel(lAssistenteSocialeInviato);
				lAssDao.setWithoutSequence(true);
				lAssDao.insert();
				lAssDao.stop();
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Assistente Sociale Assente !!!");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				if (ex.getMessage().indexOf("ASS_SOC_PK") > 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Assistente sociale gia' presente...");
					aMessaggio.setCodEsito("00001");
				}
			}
		} finally {
			cleanup(lAssDao);
		}

		return lAssistenteSocialeInviato;
	}

	private ResidenzaAssociataModel ExScaricaResidenza(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		ResidenzaDAO lResDao = null;
		ResidenzaAssociataModel lResAssociata = null;
		try {
			lResAssociata = aParseMess.getResidenzaAssociata();

			if (lResAssociata != null && lResAssociata.getResidenza() != null) {
				// 22/05/2008 Caso particolare di disallineamento ID_SOGGETTO con la Residenza.
				FascicoloSiusModel lFasSius = aParseMess.getFascicoloGPSius().getFascicoloSiusModel();
				if (lFasSius.getSogIdSoggetto().compareTo(lResAssociata.getResidenza().getSogIdSoggetto()) == 0) {
					lResDao = new ResidenzaDAO(aConn);

					lResDao.setDAOFromModel(lResAssociata.getResidenza());
					lResDao.setWithoutSequence(true);
					lResDao.insert();
					lResDao.stop();
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Residenza non valida per disallineamento ID_SOGGETTO ...");
					aMessaggio.setCodEsito("00001");
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Residenza gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Residenza! ");
		} finally {
			cleanup(lResDao);
		}

		return lResAssociata;
	}

	private ResidenzaAssociataModel ExScaricaResidenzaSius(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		ResidenzaFascicoloSiusDAO lResFasSiuDao = null;
		ResidenzaAssociataModel lResAssociata = null;
		try {
			lResAssociata = aParseMess.getResidenzaAssociata();

			if (lResAssociata != null && lResAssociata.getResidenzaFascicoloSius() != null) {
				lResFasSiuDao = new ResidenzaFascicoloSiusDAO(aConn);

				// STUB 13/04/2005 Cancellazione prima del nuovo inserimento.
				lResFasSiuDao.setCondizioneDelete(lResAssociata.getResidenza().getIdResidenza(),
						lResAssociata.getResidenzaFascicoloSius().getFasSiuIdFascicoloSius());
				lResFasSiuDao.delete();
				lResFasSiuDao.stop();

				lResFasSiuDao.setDAOFromModel(lResAssociata.getResidenzaFascicoloSius());
				lResFasSiuDao.setWithoutSequence(true);
				lResFasSiuDao.insert();
				lResFasSiuDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Associazione Residenza Fascicolo SIUS gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire l'Associazione Residenza Fascicolo Sius! ");
		} finally {
			cleanup(lResFasSiuDao);
		}

		return lResAssociata;
	}

	private EventoNotificaModel ExScaricaEvento(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		EventoNotificaModel lEve = null;
		EventoDAO lEveDao = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("********* Evento = " + aParseMess.getEvento());

			lEveDao = new EventoDAO(aConn);

			// Caricamento BLOB del documento.
			lEve = aParseMess.getEvento();

			if (lEve != null) {
				// In caso di trasferimento Provvedimento invio solo un evento; in tal caso non puo' essere
				// correlato.
				if (aMessaggio.getCodTipoOperazione().compareTo("00017") == 0)
					lEve.getEvento().setEveIdEvento(null);

				lEveDao.setDAOFromModel(lEve.getEvento());

				if (lEve.getEvento().getDocPerTrasferimento() != null
						&& lEve.getEvento().getDocPerTrasferimento().length > 1) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Controllo BBBLOBBB Out"
							+ lEve.getEvento().getDocPerTrasferimento().length);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Settato BLOBBBB");
					lEveDao.setDocBlob(new ByteArrayInputStream(lEve.getEvento().getDocPerTrasferimento()));
				}
				lEveDao.setWithoutSequence(true);
				lEveDao.insert();
				lEveDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Evento gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l'Evento! ");
		} finally {
			cleanup(lEveDao);
		}

		return lEve;
	}

	private void ExScaricaEvento(FascicoloSiepeEstesoModel aFasEsteso, MessaggioModel aMessaggio,
			Connection aConn) throws F3BException {
		EventoDAO lEveDao = null;
		try {
			EventoModel lEve = null;
			lEveDao = new EventoDAO(aConn);

			// Caricamento BLOB del documento.
			lEve = aFasEsteso.getEvento();

			if (lEve != null) {
				// In caso di trasferimento Attivita' invio solo un evento; in tal caso non puo' essere
				// correlato.
				if (aMessaggio.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ATTIVITA) == 0)
					lEve.setEveIdEvento(null);

				lEveDao.setDAOFromModel(lEve);

				if (lEve.getDocPerTrasferimento() != null && lEve.getDocPerTrasferimento().length > 1) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Controllo BBBLOBBB Out" + lEve.getDocPerTrasferimento().length);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Settato BLOBBBB");
					lEveDao.setDocBlob(new ByteArrayInputStream(lEve.getDocPerTrasferimento()));
				}
				lEveDao.setWithoutSequence(true);
				lEveDao.insert();
				lEveDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Evento gia' presente...");
			else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l'Evento! ");
		} finally {
			cleanup(lEveDao);
		}
	}

	private void ExScaricaNotifiche(ParserMessage aParseMess, Connection aConn, MessaggioModel aMessaggio)
			throws F3BException {
		AutoritaEsternaDAO lAutDao = null;
		NotificaDAO lNotDao = null;
//		CampoNotaDAO lCampoNotaDao = null;
//		AvvocatoDAO lAvvDao = null;
//		AvvocatoFascicoloSiusDAO lAvvFasSiuDao = null;

		if (aParseMess.getEvento() != null && aParseMess.getEvento().getNotifiche() != null) {
			// NOTIFICHE - AUTORITA ESTERNE
			NotificaModel[] lNotifiche = aParseMess.getEvento().getNotifiche();

			lAutDao = new AutoritaEsternaDAO(aConn);
			lNotDao = new NotificaDAO(aConn);

			int count = 0;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Presenti " + lNotifiche.length + " notifiche");

			while (count < lNotifiche.length) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Notifica[" + count + "] = " + lNotifiche[count]);

				if (lNotifiche[count] != null) {
					try {
						// AUTORITA ESTERNA
						if (lNotifiche[count].getAutoritaEsterna() != null) {
							lAutDao.setDAOFromModel(lNotifiche[count].getAutoritaEsterna());
							lAutDao.setWithoutSequence(true);
							lAutDao.insert();
							lAutDao.stop();
						}
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Autorita Esterna gia' presente...");
							aMessaggio.setCodEsito("00001");
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire la Notifica! ");
					}

					// AVVOCATO SIUS
					if (lNotifiche[count].getAvvIdAvvocatoFascicoloSius() != null
							&& lNotifiche[count].getAvvSius() != null) {
						// AVVOCATO
						ExInserisciAvvocato(lNotifiche[count].getAvvSius().getAvvocato(), aConn, aMessaggio);

						// AVVOCATO FASCICOLO SIUS
						ExInseisciAvvocatoFascicoloSIUS(lNotifiche[count].getAvvSius()
								.getAvvocatoFascicoloSiusModel(), aConn, aMessaggio);
					}

					// NOTIFICA
					try {
						lNotDao.setDAOFromModel(lNotifiche[count]);
						lNotDao.setWithoutSequence(true);
						lNotDao.insert();
						lNotDao.stop();
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Notifica gia' presente...");
							aMessaggio.setCodEsito("00001");
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire la Notifica! ");
					}
				}

				count++;
			}
		}
	}

	private void ExScaricaNoteAggiuntive(ParserMessage aParseMess, Connection aConn, MessaggioModel aMessaggio)
			throws F3BException {
		CampoNotaDAO lCampoNotaDao = null;

		// NOTE AGGIUNTIVE
		try {
			if (aParseMess.getEvento() != null && aParseMess.getEvento().getCampoNote() != null) {
				lCampoNotaDao = new CampoNotaDAO(aConn);
				// Inserimento delle eventuali note aggiuntive.
				CampoNotaModel[] lNote = aParseMess.getEvento().getCampoNote();
				if (lNote != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
							+ lNote.length);

					int count = 0;
					while (count < lNote.length) {
						lCampoNotaDao.setDAOFromModel(lNote[count]);
						lCampoNotaDao.setWithoutSequence(true);
						lCampoNotaDao.insert();
						lCampoNotaDao.stop();

						count++;
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Note gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire le Note! ");
		} finally {
			cleanup(lCampoNotaDao);
		}

	}

	private void ExScaricaDocumentoAllegato(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		DocumentoAllegatoDAO lDocAllDAO = null;

		try {
			lDocAllDAO = new DocumentoAllegatoDAO(aConn);

			DocumentoAllegatoModel lDocAll = aParseMess.getDocumentoAllegato();

			if (lDocAll != null) {
				lDocAllDAO.setDAOFromModel(lDocAll);

				// STUB 13/04/2005 Caricamento BLOB del documento.
				if (lDocAll.getDocPerTrasferimento() != null && lDocAll.getDocPerTrasferimento().length > 1) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Settato BLOBBBB");
					lDocAllDAO.setDocBlob(new ByteArrayInputStream(lDocAll.getDocPerTrasferimento()));
				}
				lDocAllDAO.setWithoutSequence(true);
				lDocAllDAO.insert();
				lDocAllDAO.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Documento Allegato gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Documento Allegato! ");
		} finally {
			cleanup(lDocAllDAO);
		}

	}

	private void ExScaricaLuogoDetenzione(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		LuogoDetenzioneDAO lLuoDetDAO = null;

		try {
			lLuoDetDAO = new LuogoDetenzioneDAO(aConn);

			LuogoDetenzioneModel lLuoDet = aParseMess.getLuogoDetenzione();

			if (lLuoDet != null) {
				lLuoDetDAO.setDAOFromModel(lLuoDet);
				lLuoDetDAO.setWithoutSequence(true);
				lLuoDetDAO.insert();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Luogo Detenzione gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Luogo Detenzione! ");
		} finally {
			cleanup(lLuoDetDAO);
		}
	}

	private void ExScaricaRiferimentiFascicoloSIEP(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		RiferimentoFascicoloSiepDAO lRifasiepDAO = null;

		List mRifasiep = aParseMess.getRifasiep();
		if ((mRifasiep != null) && (!mRifasiep.isEmpty())) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("size di Riferimenti Fascicoli SIEP : " + mRifasiep.size());
			for (int i = 0; i < mRifasiep.size(); i++) {
				try {
					lRifasiepDAO = new RiferimentoFascicoloSiepDAO(aConn);
					lRifasiepDAO.setDAOFromModel((RiferimentoFascicoloSiepModel) mRifasiep.get(i));
					lRifasiepDAO.setWithoutSequence(true);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("lRifasiepDAO.insert() N.ro : " + (i + 1));
					lRifasiepDAO.insert();
					lRifasiepDAO.stop();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Riferimento Fascicolo SIEP gia' presente...");
						aMessaggio.setCodEsito("00001");
					}
					// Nel caso in cui il Riferimento e' ad un titolo esecutivo non presente nella BDI.
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Riferimento Fascicolo SIEP non Referenziabile...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Riferimento Fascicolo SIEP! ");
				} finally {
					cleanup(lRifasiepDAO);
				}
			}
		}
	}

	private void ExScaricaMisuraAlternativa(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		MisuraAlternativaDAO lMisDao = null;
		try {
			lMisDao = new MisuraAlternativaDAO(aConn);

			MisuraAlternativaModel lMisura = aParseMess.getMisuraAlternativa();

			if (lMisura != null) {
				// Aggiorna le Note inserite durante la presa in carico
				// lMisura.setNote(aMisAlt.getNote());

				lMisDao.setDAOFromModel(lMisura);
				lMisDao.setWithoutSequence(true);
				lMisDao.insert();
				lMisDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Misura Alternativa gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire la misura alternativa! ");
		} finally {
			cleanup(lMisDao);
		}
	}

	private void ExScaricaAvvocatiSIUS(ParserMessage aParseMess, Connection aConn, MessaggioModel aMessaggio)
			throws F3BException {
		List lListaAvvSius = aParseMess.getAvvSius();
		if ((lListaAvvSius != null) && (!lListaAvvSius.isEmpty())) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("size di List Avvocato SIUS : " + lListaAvvSius.size());
			for (int i = 0; i < lListaAvvSius.size(); i++) {
				AvvocatoSiusModel lAvvocatoSius = (AvvocatoSiusModel) lListaAvvSius.get(i);
				if (lAvvocatoSius != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Inserimento Avvocato SIUS : " + (i + 1));
					ExInserisciAvvocato(lAvvocatoSius.getAvvocato(), aConn, aMessaggio);
					ExInseisciAvvocatoFascicoloSIUS(lAvvocatoSius.getAvvocatoFascicoloSiusModel(), aConn,
							aMessaggio);
				}
			}
		}
	}

	private void ExInserisciAvvocato(AvvocatoModel aAvvocato, Connection aConn, MessaggioModel aMessaggio)
			throws F3BException {
		if (aAvvocato != null) {
			AvvocatoDAO lAvvDao = null;
			try {
				lAvvDao = new AvvocatoDAO(aConn);
				lAvvDao.setDAOFromModel(aAvvocato);
				lAvvDao.setWithoutSequence(true);
				lAvvDao.insert();
				lAvvDao.stop();
			} catch (DAOException ex) {
				if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Avvocato SIUS gia' presente...");
					aMessaggio.setCodEsito("00001");
				} else
					throw new F3BException(F3BException.USER_MESSAGE,
							"Impossibile inserire l' Avvocato SIUS! ");
			} finally {
				cleanup(lAvvDao);
			}
		}

	}

//	private void ExScaricaAvvocatiFascicoloSIUS(ParserMessage aParseMess, Connection aConn,
//			MessaggioModel aMessaggio) throws F3BException {
//
//		List mAvvFasSius = aParseMess.getAvvSius();
//		if ((mAvvFasSius != null) && (!mAvvFasSius.isEmpty())) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.info("size di List Avvocato Fascicolo SIUS : " + mAvvFasSius.size());
//			for (int i = 0; i < mAvvFasSius.size(); i++) {
//				AvvocatoSiusModel lAvvocatoSius = (AvvocatoSiusModel) mAvvFasSius.get(i);
//				ExInseisciAvvocatoFascicoloSIUS(lAvvocatoSius.getAvvocatoFascicoloSiusModel(), aConn,
//						aMessaggio);
//			}
//		}
//	}

	private void ExInseisciAvvocatoFascicoloSIUS(AvvocatoFascicoloSiusModel aAvvocato, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		if (aAvvocato != null) {
			AvvocatoFascicoloSiusDAO lAvvFasSiuDao = null;

			try {
				lAvvFasSiuDao = new AvvocatoFascicoloSiusDAO(aConn);

				/*
				 * Luigi 18-06-2006 lAvvFasSiuDao.setCondizioneUpdate(aAvvocato.getIdAvvocatoFascicoloSius());
				 * lAvvFasSiuDao.delete(); lAvvFasSiuDao.stop();
				 */
				lAvvFasSiuDao.setDAOFromModel(aAvvocato);
				lAvvFasSiuDao.setWithoutSequence(true);
				lAvvFasSiuDao.insert();
				lAvvFasSiuDao.stop();
			} catch (DAOException ex) {
				if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Avvocato Fascicolo Sius gia' presente...");
					aMessaggio.setCodEsito("00001");
				} else
					throw new F3BException(F3BException.USER_MESSAGE,
							"Impossibile inserire l'Avvocato Fascicolo Sius! ");
			} finally {
				cleanup(lAvvFasSiuDao);
			}
		}
	}

	private void ExScaricaDepositoOrdinanza(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {

		DepositoOrdinanzaPcDAO lOrdDao = null;
		try {
			DepositoOrdinanzaPcModel lDepOrdPc = aParseMess.getDepositoOrdinanzaPc();

			if (lDepOrdPc != null) {
				lOrdDao = new DepositoOrdinanzaPcDAO(aConn);

				lOrdDao.setDAOFromModel(lDepOrdPc);
				lOrdDao.setWithoutSequence(true);
				lOrdDao.insert();
				lOrdDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Deposito Ordinanza gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Deposito Ordinanza! ");
		} finally {
			cleanup(lOrdDao);
		}
	}

	/**
	 * Metodo che esegue lo scarico e la scrittura dei dati del DepositoDecreto
	 * <p>
	 * 
	 * @param aParseMess
	 *            ParseMessage
	 * @param aConn
	 *            Connessione al Dbase.
	 * @param aMessaggio
	 *            Message
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 */
	private void ExScaricaDepositoDecreto(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		DepositoDecretoDAO lDepDecrDao = null;
		try {
			DepositoDecretoModel lDepDecr = aParseMess.getDepositoDecreto();

			if (lDepDecr != null) {
				lDepDecrDao = new DepositoDecretoDAO(aConn);

				lDepDecrDao.setDAOFromModel(lDepDecr);
				lDepDecrDao.setWithoutSequence(true);
				lDepDecrDao.insert();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Deposito Decreto gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Deposito Decreto! ");
		} finally {
			cleanup(lDepDecrDao);
		}
	}

	private void ExScaricaMotivazioniDecreto(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		MotivazioneDecretoDAO lMotDecrDao = null;

		if (aParseMess.getMotivazioniDecreto() != null) {
			List mMotivazioniDecreto = aParseMess.getMotivazioniDecreto();

			if (!mMotivazioniDecreto.isEmpty()) {
				lMotDecrDao = new MotivazioneDecretoDAO(aConn);

				for (int i = 0; i < mMotivazioniDecreto.size(); i++) {
					try {
						lMotDecrDao.setDAOFromModel((MotivazioneDecretoModel) mMotivazioniDecreto.get(i));
						lMotDecrDao.setWithoutSequence(true);
						lMotDecrDao.insert();
						lMotDecrDao.stop();
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Motivazione Decreto gia' presente...");
							aMessaggio.setCodEsito("00001");
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire la Motivazione Decreto! -" + i + "- "
											+ mMotivazioniDecreto.get(i));
					}
				}
				cleanup(lMotDecrDao);
			}
		}
		return;
	}

	private void ExScaricaLiberazioneAnticipata(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		LicenzaLibanticipataDAO lLibAntDAO = null;
		PeriodoLibanticipataDAO lPerAntDAO = null;
		EventoPermessoLicenzaDAO lEvePermLicDAO = null;

		// if ( aParseMess.getEvento() != null && aParseMess.getEvento().getEvento() != null && (
		// aParseMess.getEvento().getEvento().getCodMotivo().equals("0076") ||
		// aParseMess.getEvento().getEvento().getCodMotivo().equals("2130")))
		// 2008-06-23
		// Eseguita modifica concordata per eliminazione delle condizioni di filtro sul CodMotivo
		// Precedentemente fissato solo per 0076 e 2130.
		if (aParseMess.getEvento() != null && aParseMess.getEvento().getEvento() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Inserimento dati Licenze/Permessi.");

			// LICENZA LIBANTICIPATA Rielaborate il 21/03/2005
			List mLicenze = aParseMess.getLicenze();

			if ((mLicenze != null) && (!mLicenze.isEmpty())) {
				for (int i = 0; i < mLicenze.size(); i++) {
					try {
						LicenzaPeriodiLibAnticipataModel lLicenzaPeriodi = (LicenzaPeriodiLibAnticipataModel) mLicenze
								.get(i);
						LicenzaLibAnticipataModel lLicenza = lLicenzaPeriodi.getLicenza();
						PeriodoLibAnticipataModel lPeriodoModel = null;
						EventoPermessoLicenzaModel lEvePermLic = null;

						if (lLicenza != null) {
							lLibAntDAO = new LicenzaLibanticipataDAO(aConn);

							lLicenza.setFasSieIdFascicoloSiep(aParseMess.getEvento().getEvento()
									.getFasSieIdFascicoloSiep());
							lLicenza.setCodUfficioEmittente(aParseMess.getEvento().getEvento()
									.getCodUfficioEmittente());
							lLicenza.setCodLuogoEmittente(aParseMess.getEvento().getEvento()
									.getCodLuogoEmittente());
							if (aParseMess.getFascicoloGPSius().getFascicoloSiusModel() != null) {
								lLicenza.setAnnoSius(aParseMess.getFascicoloGPSius().getFascicoloSiusModel()
										.getChiaveAnno());
								lLicenza.setNumeroSius(StringUtils.toStringJSP(aParseMess
										.getFascicoloGPSius().getFascicoloSiusModel().getChiaveProgr()));
							}
							lLibAntDAO.setDAOFromModel(lLicenza);
							lLibAntDAO.setWithoutSequence(true);
							lLibAntDAO.insert();
							lLibAntDAO.stop();

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Inserimento dati Periodi ");
							if (lLicenzaPeriodi.getPeriodi() != null
									&& lLicenzaPeriodi.getPeriodi().length > 0) {
								PeriodoLibAnticipataModel[] lPeriodi = lLicenzaPeriodi.getPeriodi();
								if (lPeriodi != null && lPeriodi.length > 0) {
									for (int j = 0; j < lPeriodi.length; j++) {
										lPeriodoModel = (PeriodoLibAnticipataModel) lPeriodi[j];
										if (lPeriodoModel != null
												&& lPeriodoModel.getIdPeriodoLibanticipata() != null) {
											lPerAntDAO = new PeriodoLibanticipataDAO(aConn);
											lPerAntDAO.setDAOFromModel(lPeriodoModel);
											lPerAntDAO.setWithoutSequence(true);
											lPerAntDAO.insert();
											lPerAntDAO.stop();
										}
									}
								}
							}
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Inserimento dati EventoPermessoLicenza ");

							if (lLicenzaPeriodi.getEventiPermLic() != null
									&& lLicenzaPeriodi.getEventiPermLic().length > 0) {
								EventoPermessoLicenzaModel[] lEventiPermLic = lLicenzaPeriodi
										.getEventiPermLic();
								if (lEventiPermLic != null && lEventiPermLic.length > 0) {
									for (int j = 0; j < lEventiPermLic.length; j++) {
										lEvePermLic = (EventoPermessoLicenzaModel) lEventiPermLic[j];
										if (lEvePermLic != null
												&& lEvePermLic.getIdEventoPermessoLicenza() != null) {
											lEvePermLicDAO = new EventoPermessoLicenzaDAO(aConn);
											lEvePermLicDAO.setDAOFromModel(lEvePermLic);
											lEvePermLicDAO.setWithoutSequence(true);
											lEvePermLicDAO.insert();
											lEvePermLicDAO.stop();
										}
									}
								}
							}

						}// end if
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Licenza Anticipata gia' presente...");
							aMessaggio.setCodEsito("00001");
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire la Licenza Anticipata! ");
					}
				}
			}// end if
		}
	}

	private void ExScaricaPrescrizioni(ParserMessage aParseMess, Connection aConn, MessaggioModel aMessaggio)
			throws F3BException {
		PrescrizioneDAO lPresrDao = null;
		List mPrescrizioni = aParseMess.getPrescrizioni();

		if ((mPrescrizioni != null) && (!mPrescrizioni.isEmpty())) {
			lPresrDao = new PrescrizioneDAO(aConn);

			for (int i = 0; i < mPrescrizioni.size(); i++) {
				try {
					lPresrDao.setDAOFromModel((PrescrizioneModel) mPrescrizioni.get(i));
					lPresrDao.setWithoutSequence(true);
					lPresrDao.insert();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Prescrizione gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire la Prescrizione! ");
				}
			}
		}
	}

	private void ExScaricaImpugnazione(ParserMessage aParseMess, Connection aConn, MessaggioModel aMessaggio)
			throws F3BException {
		ImpugnazioneDAO lImpDao = null;
		ImpugnazioneModel lImp = aParseMess.getImpugnazione();

		if (lImp != null) {
			try {
				lImpDao = new ImpugnazioneDAO(aConn);

				lImpDao.setDAOFromModel(lImp);
				lImpDao.setWithoutSequence(true);
				lImpDao.insert();
				lImpDao.stop();
			} catch (DAOException ex) {
				if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Impugnazione gia' presente...");
					aMessaggio.setCodEsito("00001");
				} else
					throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l'Impugnazione! ");
			} finally {
				cleanup(lImpDao);
			}

		}
	}

	private void ExScaricaTenori(ParserMessage aParseMess, Connection aConn, MessaggioModel aMessaggio)
			throws F3BException {
		TenoreDAO lTenDao = null;

		List mTenori = aParseMess.getTenori();

		if (mTenori != null && !mTenori.isEmpty()) {
			lTenDao = new TenoreDAO(aConn);

			for (int i = 0; i < mTenori.size(); i++) {
				try {
					lTenDao.setDAOFromModel((TenoreModel) mTenori.get(i));
					lTenDao.setWithoutSequence(true);
					lTenDao.insert();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Tenore gia' presente...");
						aMessaggio.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire il Tenore! ");
				}
			}
		}
	}

	private void ExScaricaTenori(FascicoloSiepeEstesoModel aFasEsteso, Connection aConn) throws F3BException {
		TenoreDAO lTenDao = null;

		List mTenori = Arrays.asList(aFasEsteso.getFascicoloSius().getTenori());

		if (mTenori != null && !mTenori.isEmpty()) {
			lTenDao = new TenoreDAO(aConn);

			for (int i = 0; i < mTenori.size(); i++) {
				try {
					TenoreModel lTenoreModel = new TenoreModel((TenoreModel) mTenori.get(i));

					lTenDao.setDAOFromModel(lTenoreModel);
					lTenDao.setWithoutSequence(true);
					lTenDao.insert();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Tenore gia' presente...");
					else
						throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire il Tenore! ");
				}
			}
		}
	}

	// 25/02/2008 ESECUZIONE SANZIONE SOSTITUTIVA
	private void ExScaricaEsecSanzSostitutiva(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		EsecuzioneSanzioneSostitutivaDAO lESSDao = null;
		try {
			lESSDao = new EsecuzioneSanzioneSostitutivaDAO(aConn);

			// 21/05/2008 Controlli x evitare NullPointer in Presa in Carico Ordinanza.
			if (aParseMess.getFascicoloGPTPSius() != null
					&& aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento() != null
					&& aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento().getESS() != null) {
				EsecuzioneSanzioneSostitutivaModel lESSModel = aParseMess.getFascicoloGPTPSius()
						.getDatiSiusPerTrasferimento().getESS();

				if (lESSModel != null && lESSModel.getIdEsecuzioneSanzioneSost() != null) {
					lESSDao.setDAOFromModel(lESSModel);
					lESSDao.setWithoutSequence(true);
					lESSDao.insert();
					lESSDao.stop();
				}
			}
			cleanup(lESSDao); // 21/05/2008
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Esecuzione Sanzione Sostitutiva gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire la Esec. Sanz. Sostitutiva! ");
		} finally {
			cleanup(lESSDao);
		}
	}

	// 25/02/2008 PERIODO ALTRA SANZIONE
	private void ExScaricaPeriodiAltraSanzione(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		PeriodoAltraSanzioneDAO lPerAltSanDao = null;
		// 21/05/2008 Controlli x evitare NullPointer in Presa in Carico Ordinanza.
		if (aParseMess.getFascicoloGPTPSius() != null
				&& aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento() != null
				&& aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento().getPAS() != null) {
			List mPeriodiAltreSanzioni = aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento()
					.getPAS();

			if ((mPeriodiAltreSanzioni != null) && (!mPeriodiAltreSanzioni.isEmpty())) {
				lPerAltSanDao = new PeriodoAltraSanzioneDAO(aConn);

				for (int i = 0; i < mPeriodiAltreSanzioni.size(); i++) {
					try {
						lPerAltSanDao.setDAOFromModel((PeriodoAltraSanzioneModel) mPeriodiAltreSanzioni
								.get(i));
						lPerAltSanDao.setWithoutSequence(true);
						lPerAltSanDao.insert();
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Periodo Altra Sanzione gia' presente...");
							aMessaggio.setCodEsito("00001");
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire il Periodo Altra Sanzione ! ");
					}
				}
			}
		}
		cleanup(lPerAltSanDao); // 21/05/2008
	}

	// 25/02/2008 SCAMBIO SANZIONE
	private void ExScaricaScambioSanzione(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		ScambioSanzioneDAO lSSDao = null;

		// 21/05/2008 Controlli x evitare NullPointer in Presa in Carico Ordinanza.
		if (aParseMess.getFascicoloGPTPSius() != null
				&& aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento() != null
				&& aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento().getSS() != null) {
			try {
				lSSDao = new ScambioSanzioneDAO(aConn);

				ScambioSanzioneModel lSSModel = aParseMess.getFascicoloGPTPSius()
						.getDatiSiusPerTrasferimento().getSS();

				if (lSSModel != null && lSSModel.getIdScambioSanzione() != null) {
					lSSDao.setDAOFromModel(lSSModel);
					lSSDao.setWithoutSequence(true);
					lSSDao.insert();
					lSSDao.stop();
				}
			} catch (DAOException ex) {
				if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Scambio Sanzione gia' presente...");
					aMessaggio.setCodEsito("00001");
				} else
					throw new F3BException(F3BException.USER_MESSAGE,
							"Impossibile inserire lo Scambio Sanzione ! ");
			} finally {
				cleanup(lSSDao);
			}
		}
		cleanup(lSSDao); // 21/05/2008
	}

	private void ExAggiornaPosizioneGiuridica(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt) throws Exception {
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaModel lPosizione = null;
		EventoModel lEve = null;

		// Posizione Giuridica
		if (aParseMess.getEvento() != null && aParseMess.getEvento().getEvento() != null) {
			lEve = aParseMess.getEvento().getEvento();

			lPosSqlDao = new PosizioneGiuridicaSqlDAO(aConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lEve.getFasSieIdFascicoloSiep());
			lPosizione = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
		}

		// Misura Alternativa
		// 16/06/2008 Segnalazione anomalia NullPoinerException su "PresaInCarico Ordinanza" (planning ID
		// 29743695).
		// MisuraAlternativaModel lMisura = aParseMess.getMisuraAlternativa();
		MisuraAlternativaModel lMisura = null;
		if (aParseMess.getMisuraAlternativa() != null)
			lMisura = aParseMess.getMisuraAlternativa();

		// Il codice della Posizione Giurica viene aggiornato
		// in base al codice della Misura Alternativa
		if (lMisura != null && lPosizione != null) {
			String lCodPosizione = lPosizione.getCodPosizioneGiuridica();
			String lNatura = lMisura.getCodNaturaDecisione();
			String lTipoMisura = lMisura.getCodTipoMisura();

			// se la DATA_SCARCERAZIONE e' presente...
			if (lMisura.getDataScarcerazione() != null && lNatura != null && !lNatura.equals("")
					&& lTipoMisura != null && !lTipoMisura.equals("") && lCodPosizione != null
					&& !lCodPosizione.equals("")) {
				boolean lCambiaPosizione = false;
				// CONC.AFFIDAMENTO IN PROVA
				if (lNatura.equals("CO")
						&& (lTipoMisura.equals("0001") || lTipoMisura.equals("0002") || lTipoMisura
								.equals("0003"))) {
					if (lCodPosizione.equals("03") || lCodPosizione.equals("14")) {
						lCodPosizione = "13";
						lCambiaPosizione = true;
					}
				}

				// CONC.DET.DOM.
				else if (lNatura.equals("CO")
						&& (lTipoMisura.equals("0005") || lTipoMisura.equals("0010") || lTipoMisura
								.equals("0013"))) {
					if (lCodPosizione.equals("03") || lCodPosizione.equals("14")) {
						lCodPosizione = "12";
						lCambiaPosizione = true;
					}
				}

				// RIPRISTINO MISURA ALTERNATIVA
				else if (lNatura.equals("RG")
						&& (lTipoMisura.equals("0123") || lTipoMisura.equals("0124")
								|| lTipoMisura.equals("0125") || lNatura.equals("0126")
								|| lNatura.equals("0127") || lNatura.equals("0128") || lNatura.equals("0129"))) {
					if (lCodPosizione.equals("03") || lCodPosizione.equals("14")) {
						lCodPosizione = "12";
						lCambiaPosizione = true;
					}
				}

				// SOSPENSIONE PROVVISORIA
				else if (lNatura.equals("SP")
						&& (lTipoMisura.equals("2145") || lTipoMisura.equals("2146")
								|| lTipoMisura.equals("2147") || lTipoMisura.equals("2148")
								|| lTipoMisura.equals("2149") || lTipoMisura.equals("2150") || lTipoMisura
									.equals("2151"))) {
					if (lCodPosizione.equals("12") || lCodPosizione.equals("13")
							|| lCodPosizione.equals("14")) {
						lCodPosizione = "03";
						lCambiaPosizione = true;
					}
				}

				// ...e una delle condizioni si verifica, storicizza la Posizione Giuridica
				if (lCambiaPosizione) {
					// CHIUDE POSIZIONE_GIURIDICA VECCHIA
					// ( suppone la presenza di una posizione giuridica,
					// se non presente la prima inserirla? )
					lPosDao = new PosizioneGiuridicaDAO(aConn);

					lPosDao.setDataFine(lMisura.getDataScarcerazione());
					lPosDao.setDataAggiornamento(aMisAlt.getDataAggiornamento());
					lPosDao.setCodUfficioAggiornamento(aMisAlt.getCodUfficioAggiornamento());
					lPosDao.setCodOperatoreAggiornamento(aMisAlt.getCodOperatoreAggiornamento());
					lPosDao.setCondizioneUpdate(lPosizione.getIdPosizioneGiuridica());

					lPosDao.update();
					lPosDao.stop();

					// INSERISCE LA NUOVA POSIZIONE GIURIDICA
					lPosDao.setCodPosizioneGiuridica(lCodPosizione);
					lPosDao.setCodPosizioneProcessuale("-");
					lPosDao.setCodUfficioInserimento(aMisAlt.getCodUfficioAggiornamento());
					lPosDao.setCodOperatoreInserimento(aMisAlt.getCodOperatoreAggiornamento());
					lPosDao.setDataInserimento(aMisAlt.getDataAggiornamento());
					lPosDao.setDataInizio(lMisura.getDataScarcerazione());
					lPosDao.setFasSieIdFascicoloSiep(lPosizione.getFasSieIdFascicoloSiep());

					lPosDao.insert();
					lPosDao.stop();
				}
			}
		}
		cleanup(lPosDao); // 21/05/2008
		cleanup(lPosSqlDao); // 21/05/2008
	}

	private void ExAggiornaStatoProcedimento(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt) throws Exception {
		StatoProcedimentoDAO lStatoDao = null;

		MisuraAlternativaModel lMisura = aParseMess.getMisuraAlternativa();
		EventoModel lEve = null;

		// *** AGGIORNA LO STATO PROCEDIMENTO IN BASE ALLA MISURA ALTERNATIVA ***
		if (lMisura != null && aParseMess.getEvento() != null && aParseMess.getEvento().getEvento() != null) {
			lEve = aParseMess.getEvento().getEvento();

			String lStatoProcedimento = null;
			String lNatura = lMisura.getCodNaturaDecisione();
			String lTipoMisura = lMisura.getCodTipoMisura();

			if (lNatura != null && !lNatura.equals("") && lTipoMisura != null && !lTipoMisura.equals("")) {
				// CONC.AFFIDAMENTO
				if (lNatura.equals("CO")
						&& (lTipoMisura.equals("0001") || lTipoMisura.equals("0002") || lTipoMisura
								.equals("0003"))) {
					lStatoProcedimento = "0022";
				}
				// CONC.DET.DOM.
				else if (lNatura.equals("CO")
						&& (lTipoMisura.equals("0005") || lTipoMisura.equals("0010") || lTipoMisura
								.equals("0013"))) {
					lStatoProcedimento = "0026";
				}
				// CONC.SEMILIBERTA'
				else if (lNatura.equals("CO") && (lTipoMisura.equals("0004"))) {
					lStatoProcedimento = "0030";
				}
				// RIPRISTINO AFFIDAMENTO
				else if (lNatura.equals("RG")
						&& (lTipoMisura.equals("0014") || lTipoMisura.equals("0015") || lTipoMisura
								.equals("0086"))) {
					lStatoProcedimento = "0041";
				}
				// RIPRISTINO DET.DOM.
				else if (lNatura.equals("RG")
						&& (lTipoMisura.equals("0016") || lTipoMisura.equals("0087") || lTipoMisura
								.equals("0089"))) {
					lStatoProcedimento = "0042";
				}
				// RIPRISTINO SEMILIBERTA'
				else if (lNatura.equals("RG") && (lTipoMisura.equals("0091"))) {
					lStatoProcedimento = "0043";
				}
				// REVOCA AFFIDAMENTO
				else if (lNatura.equals("RE")
						&& (lTipoMisura.equals("0014") || lTipoMisura.equals("0015") || lTipoMisura
								.equals("0086"))) {
					lStatoProcedimento = "0049";
				}
				// REVOCA DET.DOM.
				else if (lNatura.equals("RE")
						&& (lTipoMisura.equals("0016") || lTipoMisura.equals("0087") || lTipoMisura
								.equals("0089"))) {
					lStatoProcedimento = "0050";
				}
				// REVOCA SEMILIBERTA'
				else if (lNatura.equals("RE") && (lTipoMisura.equals("0091"))) {
					lStatoProcedimento = "0051";
				}
				// REVOCA MISURA ALTERNATIVA
				else if (lNatura.equals("RE") && (lTipoMisura.equals("0000"))) {
					lStatoProcedimento = "0052";
				}
				// SOSPENSIONE
				else if (lNatura.equals("SP")
						&& (lTipoMisura.equals("2145") || lTipoMisura.equals("2146")
								|| lTipoMisura.equals("2147") || lTipoMisura.equals("2148")
								|| lTipoMisura.equals("2149") || lTipoMisura.equals("2150") || lTipoMisura
									.equals("2151"))) {
					lStatoProcedimento = "0033";
				}

			} // endif Natura

			if (lStatoProcedimento != null) {
				lStatoDao = new StatoProcedimentoDAO(aConn);
				// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
				lStatoDao.setCondizioneByIdFascicolo(lEve.getFasSieIdFascicoloSiep());
				lStatoDao.delete();

				// - Inserisci STATO PROCEDIMENTO
				StatoProcedimentoModel lStaProMod = new StatoProcedimentoModel();

				lStaProMod.setProgressivo(new BigDecimal(1));
				lStaProMod.setCodStatoProcedimento(lStatoProcedimento);
				lStaProMod.setData(lEve.getDataEmissione());

				lStaProMod.setFasSieIdFascicoloSiep(lEve.getFasSieIdFascicoloSiep());
				lStaProMod.setCodOperatoreInserimento(aMisAlt.getCodOperatoreAggiornamento());
				lStaProMod.setDataInserimento(aMisAlt.getDataAggiornamento());
				lStaProMod.setCodUfficioInserimento(aMisAlt.getCodUfficioAggiornamento());

				lStatoDao.setDAOFromModel(lStaProMod);
				lStatoDao.insert();
			}
		} // endif Misura
	}

	// 11/10/2010 Scarico Soggetto legato al Fascicolo SIEP da inserire nei dati
	private MessaggioModel ExScaricaSoggettoSIEP(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		SoggettoDAO lSoggettoDao = null;
		try {
			FascicoloSiepModel lFasSiepInviato = aParseMess.getFascicolo();
			if (lFasSiepInviato != null && lFasSiepInviato.getSoggetto() != null
					&& lFasSiepInviato.getSoggetto().getIdSoggetto() != null) {
				SoggettoModel lSoggetto = lFasSiepInviato.getSoggetto();
				lSoggettoDao = new SoggettoDAO(aConn);
				lSoggettoDao.setDAOFromModel(lSoggetto);
				lSoggettoDao.setIdSoggetto(lSoggetto.getIdSoggetto());
				lSoggettoDao.setWithoutSequence(true);
				lSoggettoDao.insert();
				lSoggettoDao.stop();
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Soggetto SIEP Assente!!!");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Soggetto SIEP gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire il Soggetto SIEP! ");
		} finally {
			cleanup(lSoggettoDao);
		}
		return aMessaggio;
	}

	// 17/02/2015 MISURE SICUREZZA
	private void ExScaricaMisureSicurezza(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {
		MisuraSicurezzaDAO lMisSicDao = null;
		if (aParseMess.getFascicoloGPTPSius() != null
				&& aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento() != null
				&& aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento().getMSA() != null) {
			List mMisureSicurezza = aParseMess.getFascicoloGPTPSius().getDatiSiusPerTrasferimento().getMSA();

			if ((mMisureSicurezza != null) && (!mMisureSicurezza.isEmpty())) {
				lMisSicDao = new MisuraSicurezzaDAO(aConn);

				for (int i = 0; i < mMisureSicurezza.size(); i++) {
					try {
						lMisSicDao.setDAOFromModel((MisuraSicurezzaModel) mMisureSicurezza.get(i));
						lMisSicDao.setWithoutSequence(true);
						lMisSicDao.insert();
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Misura di Sicurezza gia' presente...");
							aMessaggio.setCodEsito("00001");
						} else
							throw new F3BException(F3BException.USER_MESSAGE,
									"Impossibile inserire la Misura di Sicurezza ! ");
					}
				}
			}
		}
		cleanup(lMisSicDao);
	}

	public MessaggioModel ExPresaInCaricoSentenza(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt)
			throws F3BException {
		Connection lConn = null;
//		FascicoloSiepeModel lFasSiepe = null;
		try {
			lConn = getDBTransaction();

//			ParserMessage lPars;
			if (aMessaggio.getTreeModel() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			aMessaggio = ExPresaInCaricoOrdinanza(aMessaggio, aMisAlt, lConn);
			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;

		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			throw new F3BException(this.getClass().getPackage().getName() + ".ExPresaInCaricoOrdinanza: " + e);
		} finally {
			cleanup(lConn);
		}

		return aMessaggio;
	}

	public MessaggioModel ExPresaInCaricoSentenza(MessaggioModel aMessaggio, MisuraAlternativaModel aMisAlt,
			Connection aConn) throws Exception {
		ParserMessage lPars;
		if (aMessaggio.getTreeModel() != null)
			lPars = new ParserMessage(aMessaggio.getTreeModel());
		else
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire il Messaggio Contenuto incorretto!");

		// Controllo se le BDI sono diverse
		if (aMessaggio.getCodBdiDestinataria().trim().compareTo(aMessaggio.getCodBdiMittente().trim()) != 0) {
			// Prelievo del Soggetto dal messaggio e suo inserimento nel DB
			ExScaricaSoggetto(lPars, aConn);

			// Prelievo della Sentenza e suo inserimento nel DB
			aMessaggio = ExScaricaSentenza(lPars, aConn, aMessaggio);

			// 11/10/2010 Prelievo del Soggetto SIEP.
			aMessaggio = ExScaricaSoggettoSIEP(lPars, aConn, aMessaggio);

			// Prelievo del Fascicolo SIEP e suo Inserimento nel DB
//			FascicoloSiepModel lFasSiepInviato = new FascicoloSiepModel();
			/*lFasSiepInviato = */ExScaricaFascicoloSiep(lPars, aConn, aMessaggio);

			// Prelievo del Fascicolo SIUS e suo Inserimento nel DB
			/*FascicoloSiusModel lFasSiu = */ExScaricaFascicoloSius(lPars, aConn, aMessaggio);

			// GENERALE PROCEDIMENTO
			// Prelievo del GENERALE PROCEDIMENTO e suo Inserimento nel DB
			/*GeneraleProcedimentoModel lGenPro = */ExScaricaGeneraleProcedimento(lPars, aConn, aMessaggio);

			// RESIDENZA
			/*ResidenzaAssociataModel lResAssociata = */ExScaricaResidenza(lPars, aConn, aMessaggio);

			// RESIDENZA_FASCICOLO_SIUS
			/*lResAssociata = */ExScaricaResidenzaSius(lPars, aConn, aMessaggio);

			// EVENTO
			/*EventoNotificaModel lEvento = */ExScaricaEvento(lPars, aConn, aMessaggio);

			// NOTE AGGIUNTIVE
			ExScaricaNoteAggiuntive(lPars, aConn, aMessaggio);

			// STUB 12/04/2005 DOCUMENTO ALLEGATO
			ExScaricaDocumentoAllegato(lPars, aConn, aMessaggio);

			// STUB 15/04/2005 LUOGO DETENZIONE.
			ExScaricaLuogoDetenzione(lPars, aConn, aMessaggio);

			// STUB 15/04/2005 RIFERIMENTI FASCICOLI SIEP.
			ExScaricaRiferimentiFascicoloSIEP(lPars, aConn, aMessaggio);

			// MISURA ALTERNATIVA
			ExScaricaMisuraAlternativa(lPars, aConn, aMessaggio);

			// STUB 07/04/2005 AVVOCATI SIUS.
			ExScaricaAvvocatiSIUS(lPars, aConn, aMessaggio);

			// NOTIFICHE - AUTORITA ESTERNE
			ExScaricaNotifiche(lPars, aConn, aMessaggio);

			// DEPOSITO SENTENZA
			ExScaricaDepositoSentenza(lPars, aConn, aMessaggio);

			// LICENZA LIBANTICIPATA
			ExScaricaLiberazioneAnticipata(lPars, aConn, aMessaggio);

			// PRESCRIZIONI
			ExScaricaPrescrizioni(lPars, aConn, aMessaggio);

			// IMPUGNAZIONE
			ExScaricaImpugnazione(lPars, aConn, aMessaggio);

			// TENORI
			ExScaricaTenori(lPars, aConn, aMessaggio);

			// *** AGGIORNA LA POSIZIONE GIURIDICA IN BASE ALLA MISURA ALTERNATIVA ***
			// Posizione Giuridica
			ExAggiornaPosizioneGiuridica(lPars, aConn, aMessaggio, aMisAlt);

			// *** AGGIORNA LO STATO PROCEDIMENTO IN BASE ALLA MISURA ALTERNATIVA ***
			ExAggiornaStatoProcedimento(lPars, aConn, aMessaggio, aMisAlt);

			// 25/02/2008 ESECUZIONE SANZIONE SOSTITUTIVA.
			ExScaricaEsecSanzSostitutiva(lPars, aConn, aMessaggio);

			// 25/02/2008 PERIODO ALTRA SANZIONE.
			ExScaricaPeriodiAltraSanzione(lPars, aConn, aMessaggio);

			// 25/02/2008 SCAMBIO SANZIONE.
			ExScaricaScambioSanzione(lPars, aConn, aMessaggio);
		}
		return aMessaggio;

	}

	private void ExScaricaDepositoSentenza(ParserMessage aParseMess, Connection aConn,
			MessaggioModel aMessaggio) throws F3BException {

		DepositoSentenzaDAO lSenDao = null;
		try {
			DepositoSentenzaModel lDeoSen = aParseMess.getDepositoSentenza();

			if (lDeoSen != null) {
				lSenDao = new DepositoSentenzaDAO(aConn);

				lSenDao.setDAOFromModel(lDeoSen);
				lSenDao.setWithoutSequence(true);
				lSenDao.insert();
				lSenDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Deposito Sentenza gia' presente...");
				aMessaggio.setCodEsito("00001");
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Deposito Ordinanza! ");
		} finally {
			cleanup(lSenDao);
		}
	}

}