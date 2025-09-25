package siap.siep.sospensione.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.verbale.model.VerbaleModel;

/**
 * <p>
 * Title: SospensioneController
 * </p>
 * <p>
 * Description: Classe Controller per Sospensione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ISospensione {

	public SospensioneModel ExInserisciSospensione(SospensioneModel aSospensione) throws F3BException;

	public String ExInserisciSospensioneWithoutSequence(SospensioneModel aSospensioneModel, Connection lConn)
			throws F3BException;

	public Vector ExRicercaSospensione(SospensioneModel aSospensione) throws F3BException;

	public SospensioneModel ExRicercaSospensioneByKey(BigDecimal aKey) throws F3BException;

	public SospensioneModel ExRicercaSospensioneByIdPenaResidua(BigDecimal aIdPenaResidua)
			throws F3BException;

	public SospensioneModel ExModificaSospensione(SospensioneModel aSospensione) throws F3BException;

	public void ExCancellaSospensione(SospensioneModel aSospensione) throws F3BException;

	public Vector<SospensioneModel> ExRicercaSospensioneByIdFascicoloSiep(BigDecimal aKeyFascicolo)
			throws F3BException;

	/**
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaEventoNotificaSosp(EventoNotificaModel aEvento)
			throws F3BException;

	/**
	 * ExInserisciEventoNotificaVerbale
	 * 
	 * @param aEventoNot
	 * @param aPena
	 * @param aEvento
	 * @param aVerbale
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciEventoNotificaVerbale(EventoNotificaModel aEventoNot,
			PenaResiduaModel aPena, EventoModel aEvento, VerbaleModel aVerbale) throws F3BException;

	public EventoModel ExUpdateValidaSospensione(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aPosGiu, String aNomProv, String aStatoProc) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoSospensioni(EventoNotificaModel aEvento,
			UtenteModel aUtente) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoDifferimento(EventoNotificaModel aEvento,
			UtenteModel aUtente) throws F3BException;

	/**
	 * Effettua l'aggiornamento del Provvedimento (blob) e la validazione del Differimento. Vale a dire del
	 * DECRETO_ORDINANZA_SIEP, EVENTO(sius), PENA_RESIDUA, EVENTO (siep)..... aggiorna la posizione giuridica
	 * aggiorna lo stato del provvedimento
	 * 
	 * @param aEvento
	 *            - EventoModel contenente i soli dati da aggiornare (idEvento,blob,dataAgg,uffAgg,operAgg)
	 * @param aFascicolo
	 * @param aPosGiu
	 * @param aNomProv
	 * @param aStatoProc
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaDifferimento(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aPosGiu, String aNomProv, String aStatoProc) throws F3BException;

	/**
	 * Valida l'evento, la pena residua, il decreto ordinanza siep legati a una interruzione. Se codMotivo =
	 * in (0266,0267,0268,0269) aggiorna anche la posizione giuridica Se codMotivo 0268 cambia anche lo stato
	 * del procedimento in 0137 (Disposta consegna temporanea all'estero del condannato)
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @param aPosGiu
	 * @param aPenaResidua
	 * @param aDecreto
	 */
	public EventoModel ExUpdateValidaInterruzione(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			PosizioneGiuridicaModel aPosGiu, PenaResiduaModel aPenaResidua,
			DecretoOrdinanzaSiepModel aDecreto) throws F3BException;

	/**
	 * Effettua la validazione e dei provvedimenti collegati a una <b>Interruzione</b><br> - Valida
	 * l'evento<br> - Valida la pena residua<br> - Valida il decreto ordinanza siep<br> - Effettua
	 * l'aggiornamento dello Stato del Procedimento, dello Scadenzario Vane Ricerche, del Nome
	 * Provvedimento<br><br>
	 *
	 * @param aEvento @param aFascicolo @param aNomProv - nome provvedimento @param aStatoProc - stato
	 * procedimento @return @throws
	 */
	public EventoModel ExUpdateValidaSospensioneOE(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aNomProv, String aStatoProc) throws F3BException;

	/**
	 * Effettua la <b>SVALIDAZIONE</b> dell'evento e della pena residua. Cancellando le eventuali vecchie
	 * annotazioni (Quali?) e sostituendole con quelle passare in input.
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExModificaEventoInserisciNotificaSosp(EventoNotificaModel aEvento) throws F3BException;

	public EventoModel ExUpdateValidaDecretoSospensione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExInserisciEventoDecretoOrdinanzaSiep(DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep,
			EventoNotificaModel aEventoNot, CalcoloPenaModel aCalcoloPenaMod) throws F3BException;

	public EventoModel ExUpdateValidaSospensioneEsecPenaDispPm(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

	public EventoModel ExUpdateValidaSospensioneDecisioniSorveglianza(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

	/**
	 * Effettua l'inserimento del Provvedimento conseguente un Differimento. Eventualmente calcola la pena
	 * residua e la sospensione.
	 * 
	 * @param aTipoDifferimento
	 *            - indica il tipo di Differimento che si sta registrando
	 * @param aEventoNotifica
	 *            - model contenente i dati dell'evento da inserire e delle relative notifiche. Il campo
	 *            mEveIdEvento dell'EventoModel deve essere valorizzato con l'id dell'Evento legato al
	 *            provvedimento della Sorveglianza.
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciEventoDifferimento(String aTipoDifferimento,
			EventoNotificaModel aEventoNotifica, CalcoloPenaModel aCalcoloPenaMod) throws F3BException;

	public EventoModel ExUpdateValidaDifferimentoNew(String aTipoProvvedimento, EventoModel aEvento,
			MisuraAlternativaModel aMisAlt, BigDecimal aFascicoloKey, String aPosGiu, String aNomProv,
			String aStatoProc) throws F3BException;

	public EventoModel ExUpdateEspulsione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateRinunciaOpEspulsione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExInserisciEventoNotVerbaleSospPena(EventoModel aEveVer, VerbaleModel aVerMod,
			SospensioneModel aSosp, PenaResiduaModel aPenMod, EventoNotificaModel aEveNot)
			throws F3BException;

	public EventoModel ExUpdateValidaEspulsione(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aPosGiu, String aNomProv, String aStatoProc) throws F3BException;

	// MEV_9-SIEP
	public EventoModel ExUpdateValidaSospensioneDecisioniSorveglianza678(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;
}