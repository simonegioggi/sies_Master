package siap.siep.ordineesecuzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.motivoevento.model.MotivoEventoModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.rinnovo.model.RinnovoModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IOrdineEsecuzione {

	public EventoNotificaModel ExInserisciOrdineEsecuzioneAltraCausa(EventoNotificaModel aEvento,
			FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public EventoNotificaModel ExInserisciOENotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua) throws F3BException;

	public EventoNotificaModel ExInserisciAnnNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, String IdEve) throws F3BException;

	public EventoNotificaModel ExInserisciOModificaOENotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua) throws F3BException;

	public EventoNotificaModel ExInserisciOModificaLSNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua) throws F3BException;

	public EventoNotificaModel ExInserisciOModificaLAlfNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua) throws F3BException;

	public EventoNotificaModel ExInserisciOModificaRevocaLSNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MotivoEventoModel aMotEve) throws F3BException;

	public EventoNotificaModel ExInserisciOModificaRevocaLAlfNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MotivoEventoModel aMotEve) throws F3BException;

	public Vector ExRicercaOEScadenzarioEventoByFascicolo(BigDecimal aKeyFascicolo) throws F3BException;

	public Vector ExAggiornaAvvenutaNotifica(NotificaModel[] IdNotifiche, BigDecimal aKeyFascicolo,
			boolean isIrreperibilita) throws F3BException;

	public Vector ExAggiornaNotifichePosizioneGiuridicaLuogoDetenzioneVerbaleArresto(
			NotificaModel[] IdNotifiche, PosizioneGiuridicaModel aPosMod, LuogoDetenzioneModel aLuoDetMod)
			throws F3BException;

	public EventoModel ExAggiornaEventoStatoEsecuzione(List aEventi) throws F3BException;

	public Vector ExRicercaTuttiEventiByFascicolo(BigDecimal aKeyFascicolo) throws F3BException;

	public Vector ExRicercaEventiPerCodiceTipoEventoByFascicolo(BigDecimal aKeyFascicolo) throws F3BException;

	public EventoModel ExUpdateValidaOE(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaOESanSos(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaLS(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaLSSanSos(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaRS(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaRED(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoNotificaModel ExInserisciOModificaEventoNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua) throws F3BException;

	public EventoModel ExUpdateValidaOrdineEsecuzioneRidetPena(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

	public EventoModel ExRicercaEventoNonRegistratoByFascicoloSiep(BigDecimal aKey, String aTipEve,
			String aTipProv) throws F3BException;

	public EventoModel ExRicercaEventoOERDNonRegistratoByFascicoloSiep(BigDecimal aKey, String aTipEve,
			String aTipProv) throws F3BException;

	public boolean ExEsisteOrdineEsecuzioneByFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException;

	/**
	 * Metodo per l'annullamento logico di eventi validati
	 * 
	 * @param aEvento
	 *            - evento da annullare
	 * @param aCampoNota
	 *            - Model campo nota contenete le motivazioni dell'annullamento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExAggiornaEventoInserisciCampoNota(EventoModel aEvento, CampoNotaModel aCampoNota)
			throws F3BException;

	public EventoModel ExCancellaEventoConStoreProcedure(EventoModel aEvento) throws F3BException;

	public CampoNotaModel ExRicercaEventoCampoNotaByIdEvento(BigDecimal aKey) throws F3BException;

	public BigDecimal ExGetCountEventi(BigDecimal aKeyFascicolo) throws F3BException;

	public Vector ExRicercaTuttiEventiByFascicoloPaged(BigDecimal aKeyFascicolo, int aPage)
			throws F3BException;

	public EventoNotificaModel ExInserisciOModificaMANotificaRevocaLS(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MisuraAlternativaModel aMisura, MotivoEventoModel aMotEve)
			throws F3BException;

	public EventoNotificaModel ExInserisciOModificaMANotificaRevocaLAlf(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MisuraAlternativaModel aMisura, MotivoEventoModel aMotEve)
			throws F3BException;

	public Date calcolaDataMaggiore(NotificaModel[] aNotifiche);

	public Date calcolaPeriodoFeriale(Connection aConn, Date aDataMaggiore, String aCodUfficioUtente)
			throws F3BException;

	public String calcolaCodStatoNotifica(List aNotifiche, Date aDataAvvenutaNotifica, RinnovoModel aRinnovo);

	/**
	 * Effettua l'inserimento dell'Ordine di esecuzione associato alla rideterminazione pena altro
	 * 
	 * @param lEveNotModel
	 * @return
	 * @throws F3BException
	 * @since 4.0
	 */
	public EventoModel ExInserisciOERidetPenaAltro(EventoNotificaModel aEveNotModel) throws F3BException;

	/**
	 * Metodo per la validazione dell'ordine di esecuzione per Rideterminazione Pena Altro. Effettua la
	 * contestuale validazione del decreto di computo se non già validato.
	 * 
	 * @param aEvento
	 *            - OE da validare
	 * @param aFascicolo
	 *            - Fascicolo Siep Model
	 * @return
	 * @throws F3BException
	 * @since 4.0
	 */
	public EventoModel ExUpdateValidaOERidetPenaAltro(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	/**
	 * Metod per l'inserimento della variazione decorrenza scadenza Questa Causa
	 * 
	 * @param aEveNotMod
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciVariazioneDecorrenzaScadenzaQC(EventoNotificaModel aEveNotMod,
			PenaResiduaModel aPenaResidua) throws F3BException;

	public EventoModel ExUpdateValidaVariazioneDecScadQC(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	// AMBROS DECRETO LEGGE GIUGNO 2013
	public EventoNotificaModel ExInserisciOModificaLegge78del2013(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua) throws F3BException;

	// AMBROS 01/2014 - Misure di Sicurezza SIEP -
	// 06-03-2015 cambiati gli ultimi 2 parametri (da bigDecimal a list, e da Model a Vector)
	public EventoNotificaModel ExInserisciOENotificaMisSic(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, List aMisureOld, Vector aMisureNewSius) throws F3BException;

	/* 
	 * ISSUE MEV : aggiunto metodo di inserimento OE differimento con notifiche
	 * Numero MEV : 39
	 * Autore    : Gioggi
	 * Data      : 07/mar/2017
	 * Branch    : MEV_39
	 */
	public EventoNotificaModel ExInserisciOLDifferimentoConNotifiche(EventoNotificaModel lEve,
			PenaResiduaModel lPenaRes, MisuraAlternativaModel mam) throws F3BException;

	public EventoNotificaModel ExModificaOLDifferimentoConNotifiche(EventoNotificaModel lEve,
			PenaResiduaModel lPenaRes, MisuraAlternativaModel mam) throws F3BException;
	//***** FINE INTERVENTO MEV_39 *****//

}