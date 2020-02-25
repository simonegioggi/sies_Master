package siap.sius.misurasicurezza.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PeriodoAltraMisuraController
 * </p>
 * <p>
 * Description: Classe Controller per PeriodoAltraMisura
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
public interface IPeriodoAltraMisura {

	public PeriodoAltraMisuraModel ExInserisciPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod)
			throws F3BException;

	/*****************************************************************************
	 * 2 modifica Periodo Altra Misura
	 ****************************************************************************/

	public PeriodoAltraMisuraModel ExModificaPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod)
			throws F3BException;

	/*****************************************************************************
	 * 3 modifica Periodo Altra Misura + Ems Inserimento Evento + scadenzario
	 ****************************************************************************/
	public PeriodoAltraMisuraModel ExModificaPeriodoAltraMisura(EventoModel aEveMod,
			ScadenzarioSiusModel lScaMod, PeriodoAltraMisuraModel lPerMod,
			EsecuzioneMisuraSicurezzaModel lEmsMod) throws F3BException;

	/*****************************************************************************
	 * 4 cancella Periodo Altra Misura (inizio misura) Modifica Ems cancella Evento + scadenzario
	 ****************************************************************************/
	public void ExCancellaPeriodoAltraMisura(ScadenzarioSiusModel lScaMod, PeriodoAltraMisuraModel lPerMod,
			EsecuzioneMisuraSicurezzaModel lEmsMod) throws F3BException;

	/*****************************************************************************
	 * 5 Inserimento Periodo Altra Misura e Modifica EMS inserimento scadenzario eventuale modifica altro
	 * scadenzario
	 *****************************************************************************/
	public PeriodoAltraMisuraModel ExInserisciPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod,
			ScadenzarioSiusModel lScaMod1, EsecuzioneMisuraSicurezzaModel lEmsMod,
			ScadenzarioSiusModel lScaMod2) throws F3BException;

	/*****************************************************************************
	 * 5bis Inserimento Periodo Altra Misura e Modifica EMS inserimento scadenzario eventuale modifica altro
	 * scadenzario
	 *****************************************************************************/
	public PeriodoAltraMisuraModel ExInserisciPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod,
			EsecuzioneMisuraSicurezzaModel lEmsMod, ScadenzarioSiusModel lScaMod1,
			ScadenzarioSiusModel lScaMod2) throws F3BException;

	/*****************************************************************************
	 * 6 modifica Periodo Altra Misura + Ems modifica scadenzario eventuale modifica altro scadenzario
	 ****************************************************************************/
	public PeriodoAltraMisuraModel ExModificaPeriodoAltraMisura(ScadenzarioSiusModel lScaMod1,
			ScadenzarioSiusModel lScaMod2, PeriodoAltraMisuraModel lPerMod,
			EsecuzioneMisuraSicurezzaModel lEmsMod) throws F3BException;

	/*****************************************************************************
	 * 7 cancella Periodo Altra Misura (ripresa misura) Modifica Ems eventuale cancella scadenzario eventuale
	 * modifiche di altri 2 scadenzari
	 ****************************************************************************/
	public void ExCancellaPeriodoAltraMisura(ScadenzarioSiusModel lScaMod1, ScadenzarioSiusModel lScaMod2,
			ScadenzarioSiusModel lScaMod3, BigDecimal idPerMod, EsecuzioneMisuraSicurezzaModel lEmsMod)
			throws F3BException;

	/****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ***************************************************************************/
	public PeriodoAltraMisuraModel ExRicercaPeriodoAltraMisuraById(BigDecimal aIdPeriodoAltraMisura)
			throws F3BException;

	/****************************************************************************
	 * Effettua la ricerca per chiave Id Fascicolo
	 * 
	 * @param akey
	 *            valore della chiave del Fascicolo
	 * @return una lista di tutti il model con i dati trovati
	 * @throws F3BException
	 ***************************************************************************/
	public List ExRicercaMisuraSicurezzaByIdFascicolo(BigDecimal aKey) throws F3BException;

	public List ExRicercaMisuraSicurezzaByIdSiep(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaPeriodoAltraMisura(PeriodoAltraMisuraModel lPerMod) throws F3BException;

	public PeriodoAltraMisuraModel ExRicercaMisuraSicurezzaByIdEvento(BigDecimal aKey) throws F3BException;

	public PeriodoAltraMisuraModel ExModificaDateInizioMisuraSicurezza(PeriodoAltraMisuraModel lPerMod)
			throws F3BException;

	public Vector ExRicercaProvvedimentoEventoByFascicoloSiep(BigDecimal aFascSiepKey) throws F3BException;

}