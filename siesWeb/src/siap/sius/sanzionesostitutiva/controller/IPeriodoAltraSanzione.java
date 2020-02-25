package siap.sius.sanzionesostitutiva.controller;

/**
 * <p>Title: PeriodoAltraSanzioneController</p>
 * <p>Description: Classe Controller per PeriodoAltraSanzione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IPeriodoAltraSanzione {

	public PeriodoAltraSanzioneModel ExInserisciPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod)
			throws F3BException;

	/*****************************************************************************
	 * 2 modifica Periodo Altra Sanzione
	 ****************************************************************************/

	public PeriodoAltraSanzioneModel ExModificaPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod)
			throws F3BException;

	/*****************************************************************************
	 * 3 modifica Periodo Altra Sanzione + Ess Inserimento Evento + scadenzario
	 ****************************************************************************/
	public PeriodoAltraSanzioneModel ExModificaPeriodoAltraSanzione(EventoModel aEveMod,
			ScadenzarioSiusModel lScaMod, PeriodoAltraSanzioneModel lPerMod,
			EsecuzioneSanzioneSostitutivaModel lEssMod) throws F3BException;

	/*****************************************************************************
	 * 4 cancella Periodo Altra Sanzione (inizio sanzione) Modifica Ess cancella Evento + scadenzario
	 ****************************************************************************/
	public void ExCancellaPeriodoAltraSanzione(ScadenzarioSiusModel lScaMod,
			PeriodoAltraSanzioneModel lPerMod, EsecuzioneSanzioneSostitutivaModel lEssMod)
			throws F3BException;

	/*****************************************************************************
	 * 5 Inserimento Periodo Altra Sanzione e Modifica ESS inserimento scadenzario eventuale modifica altro
	 * scadenzario
	 *****************************************************************************/
	public PeriodoAltraSanzioneModel ExInserisciPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod,
			ScadenzarioSiusModel lScaMod1, EsecuzioneSanzioneSostitutivaModel lEssMod,
			ScadenzarioSiusModel lScaMod2) throws F3BException;

	/*****************************************************************************
	 * 5bis Inserimento Periodo Altra Sanzione e Modifica ESS inserimento scadenzario eventuale modifica altro
	 * scadenzario
	 *****************************************************************************/
	public PeriodoAltraSanzioneModel ExInserisciPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod,
			EsecuzioneSanzioneSostitutivaModel lEssMod, ScadenzarioSiusModel lScaMod1,
			ScadenzarioSiusModel lScaMod2) throws F3BException;

	/*****************************************************************************
	 * 6 modifica Periodo Altra Sanzione + Ess modifica scadenzario eventuale modifica altro scadenzario
	 ****************************************************************************/
	public PeriodoAltraSanzioneModel ExModificaPeriodoAltraSanzione(ScadenzarioSiusModel lScaMod1,
			ScadenzarioSiusModel lScaMod2, PeriodoAltraSanzioneModel lPerMod,
			EsecuzioneSanzioneSostitutivaModel lEssMod) throws F3BException;

	/*****************************************************************************
	 * 7 cancella Periodo Altra Sanzione (ripresa sanzione) Modifica Ess eventuale cancella scadenzario
	 * eventuale modifiche di altri 2 scadenzari
	 ****************************************************************************/
	public void ExCancellaPeriodoAltraSanzione(ScadenzarioSiusModel lScaMod1, ScadenzarioSiusModel lScaMod2,
			ScadenzarioSiusModel lScaMod3, BigDecimal idPerMod, EsecuzioneSanzioneSostitutivaModel lEssMod)
			throws F3BException;

	/****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ***************************************************************************/
	public PeriodoAltraSanzioneModel ExRicercaPeriodoAltraSanzioneById(BigDecimal aIdPeriodoAltraSanzione)
			throws F3BException;

	/****************************************************************************
	 * Effettua la ricerca per chiave Id Fascicolo
	 * 
	 * @param akey
	 *            valore della chiave del Fascicolo
	 * @return una lista di tutti il model con i dati trovati
	 * @throws F3BException
	 ***************************************************************************/
	public List ExRicercaSanzioneSostitutivaByIdFascicolo(BigDecimal aKey) throws F3BException;

	public List ExRicercaSanzioneSostitutivaByIdSiep(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaPeriodoAltraSanzione(PeriodoAltraSanzioneModel lPerMod) throws F3BException;

	public PeriodoAltraSanzioneModel ExRicercaSanzioneSostitutivaByIdEvento(BigDecimal aKey)
			throws F3BException;

}