package siap.siep.nuovaistanza.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: NuovaIstanzaController
 * </p>
 * <p>
 * Description: Classe Controller per NuovaIstanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Agile S.r.l.
 * </p>
 * 
 * @version 5.0
 */
@SuppressWarnings("rawtypes")
public interface INuovaIstanza {

	public NuovaIstanzaModel ExInserisciNuovaIstanza(EventoModel aEveMod, NuovaIstanzaModel aNuovaIstanza,
			SentenzaModel aSenMod, SoggettoModel aSogMod, FascicoloSiepModel aFascMod) throws F3BException;

	public Vector<NuovaIstanzaModel> ExRicercaNuovaIstanza(NuovaIstanzaModel aNuovaIstanza)
			throws F3BException;

	public void ExModificaNuovaIstanza(NuovaIstanzaModel aNuovaIstanza) throws F3BException;

	public void ExAnnulamentoIstanza(NuovaIstanzaModel aIstMod, CampoNotaModel aCampoNota)
			throws F3BException;

	public BigDecimal ExGetCountNuovaIstanza(NuovaIstanzaModel aNuovaIstanza) throws F3BException;

	public BigDecimal ExGetCountNuovaIstanzaByAnnoProgr(NuovaIstanzaModel aNuovaIstanza, int annoIni,
			int progrIni, int annoFine, int progrFine) throws F3BException;

	public BigDecimal ExGetCountNuovaIstanzaBySoggetto(NuovaIstanzaModel aNuovaIstanza, SoggettoModel aSogMod)
			throws F3BException;

	public NuovaIstanzaModel ExRicercaNuovaIstanzaById(BigDecimal aIdNuovaIstanza) throws F3BException;

	public Vector<NuovaIstanzaModel> ExRicercaNuovaIstanzaPaged(NuovaIstanzaModel aNuovaIstanza, int aPage)
			throws F3BException;

	public Vector<NuovaIstanzaModel> ExRicercaNuoveIstanzeByAnnoProgrPaged(NuovaIstanzaModel aNuovaIstanza,
			int annoIni, int progrIni, int annoFine, int progrFine, int aPage) throws F3BException;

	public Vector<NuovaIstanzaModel> ExRicercaNuoveIstanzeBySoggettoPaged(NuovaIstanzaModel aNuovaIstanza,
			SoggettoModel aSogMod, int aPage) throws F3BException;

	public Collection<NuovaIstanzaModel> ExRicercaNuovaIstanzaByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException;

	public Collection<NuovaIstanzaModel> ExRicercaNuovaIstanzaNonAnnullataByIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException;

	public NuovaIstanzaModel ExRicercaNuovaIstanzaByEveIdEvento(BigDecimal aEveIdEvento) throws F3BException;

	public ByteArrayOutputStream ExStampaTrasmissioneNuovaIstanza(EventoNotificaModel aEvento,
			UtenteModel aUtente, String tipologia) throws F3BException;

	public ByteArrayOutputStream ExStampaRicevutaNuovaIstanza(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException;

	// 20180110: [SG] aggiunto parametro di passaggio x gestione NOTIFICHE: prendo solo l'ultima
	public String ExInserisciNuovaIstanzaWithoutSequence(ArrayList aNuovaIstanza, Connection lConn)
			throws F3BException;

	public NuovaIstanzaModel ExInoltraNuovaIstanza(EventoModel aEveMod, NuovaIstanzaModel aNuovaIstanza)
			throws F3BException;

	public EventoModel ExUpdateValidaInoltroNuovaIstanza(EventoModel aEvento) throws F3BException;

	public EventoNotificaModel ExDisposizioneNuovaIstanza(NuovaIstanzaModel aNuovaIstanza,
			EventoNotificaModel lEve) throws F3BException;

	public String ExRicercaFlagValNuovaIstanzaPaged(BigDecimal aIdEventoNuovaIstanza, int aPage)
			throws F3BException;

	public FascicoloSiepModel ExConvertiNuovaIstanza(SoggettoModel aSogMod, FascicoloSiepModel aFascMod,
			FascicoloSiepModel aRIMod, EventoModel aEveMod, NuovaIstanzaModel aNuoMod) throws F3BException;

	public FascicoloSiepModel ExAssociaNuovaIstanza(BigDecimal aIdFascSiep, FascicoloSiepModel aRIMod,
			EventoModel aEveMod, NuovaIstanzaModel aNuoMod) throws F3BException;

	public FascicoloSiepModel ExAnnullaAssociaNuovaIstanza(FascicoloSiepModel aRIMod) throws F3BException;

	public void ExModificaStatoNuovaIstanza(NuovaIstanzaModel aNuovaIstanza) throws F3BException;

	public String ExRicercaFlagValNuovaIstanza(BigDecimal aIdEventoNuovaIstanza, String Tipo)
			throws F3BException;

	public void ExAnnullamentoInoltroIstanza(BigDecimal aIdEventoNuovaIstanza, BigDecimal aIdIstanza,
			CampoNotaModel aCampoNota, String tipoOp) throws F3BException;

	public void ExAnnullamentoDisposizioneIstanza(BigDecimal aIdEventoNuovaIstanza, BigDecimal aIdIstanza,
			CampoNotaModel aCampoNota, String tipoOp) throws F3BException;

}