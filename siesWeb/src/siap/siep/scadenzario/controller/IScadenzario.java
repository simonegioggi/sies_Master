package siap.siep.scadenzario.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.scadenzario.model.ScadenzarioModel;

/**
 * <p>
 * Title: IScadenzario
 * </p>
 * <p>
 * Description: Classe interfaccia controller per Scadenzario
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
public interface IScadenzario {

	public ScadenzarioModel ExInserisciScadenzario(ScadenzarioModel aScadenzario) throws F3BException;

	// MEV_39: aggiunto parametro di passaggio
	public Vector ExRicercaScadenzario(ScadenzarioModel aScadenzario, String tipoRicerca) throws F3BException;

	public ScadenzarioModel ExRicercaScadenzarioByKey(BigDecimal aKey) throws F3BException;

	public ScadenzarioModel ExModificaScadenzario(ScadenzarioModel aScadenzario) throws F3BException;

	public void ExCancellaScadenzario(ScadenzarioModel aScadenzario) throws F3BException;

	public void ExCancellaScadenzarioSimeone(ScadenzarioModel aScadenzario) throws F3BException;

	public List ExScadenzarioByIdFascicolo(BigDecimal aKey) throws F3BException;

	public ScadenzarioModel ExScadenzarioCorrenteByIdFascicoloTipoScadenzario(BigDecimal aIdFascicolo,
			String aCodTipoScadenzario) throws F3BException;

	/**
	 * Aggiorna lo scadenzario FINE PENA più recente, se presente, altrimenti lo inserisce
	 *
	 * @param aScadenzario
	 * @return
	 * @throws F3BException
	 */
	public ScadenzarioModel ExRicercaInserisciAggScadenzarioIdFascicoloCorrente(ScadenzarioModel aScadenzario)
			throws F3BException;

	public Vector ExRicercaScadenzarioVerbaleArresto(ScadenzarioModel aScadenzario) throws F3BException;

	public Vector ExRicercaScadenzarioPaged(ScadenzarioModel aScadenzario, int aPage) throws F3BException;

	// AMBROSINO 04-02-2011 Vers 5.1 - Su segnalazione di Marchese Aggiungo Data
	// VVR alla ricerca Scadenzario
	public Vector ExRicercaScadenzarioVVRPaged(ScadenzarioModel aScadenzario, int aPage) throws F3BException;

	public BigDecimal ExGetCountScadenzarioVVRPaged(ScadenzarioModel aScadenzario, int aPage)
			throws F3BException;

	// END AMBROSINO
	public BigDecimal ExGetCountScadenzari(ScadenzarioModel aScadenzario) throws F3BException;

	public List ExRicercaScadenzarioSimeonePaged(ScadenzarioModel aScadenzario, int aPage)
			throws F3BException;

	public BigDecimal ExGetCountScadenzarioSimeone(ScadenzarioModel aScadenzario) throws F3BException;

	public ScadenzarioModel ExRicercaScadenzarioCorrenteByIdFascicoloIdNotifica(BigDecimal aKey,
			BigDecimal aKeyNot, String aTipSca) throws F3BException;

	// AMBROSINO Decreto Legge 78/2013
	public BigDecimal ExGetCountTrasmessiL78del2013(ScadenzarioModel aScadenzario, Boolean Attivi,
			String acoduffcoll, Boolean nostato) throws F3BException;

	public List ExRicercaTrasmessiL78del2013Paged(ScadenzarioModel aScadenzario, int aPage, Boolean Attivi,
			String acoduffcoll, Boolean nostato) throws F3BException;

	// 27/03/2015 Scadenzario Fine pena Mis Sic -->
	// public BigDecimal ExGetCountScadenzariMisSic(ScadenzarioModel aScadenzario) throws F3BException;

	public Vector ExRicercaScadenzarioPagedMisSic(ScadenzarioModel aScadenzario, int aPage)
			throws F3BException;

	// MEV_39: aggiunti nuovo metodi di ricerca
	public ScadenzarioModel ExRicercaScadenzarioCSMSByKey(BigDecimal idScadenzario, String s)
			throws F3BException;

	public BigDecimal ExGetCountScadenzariDifferimentoMS(ScadenzarioModel lScaMod) throws F3BException;

	public Vector ExRicercaScadenzarioDifferimentoMSPaged(ScadenzarioModel lScaMod, int i)
			throws F3BException;

	// 20191121 [SG]: aggiunto metodo
	public BigDecimal ExGetCountRicercaScadenzarioPagedMisSic(ScadenzarioModel sm) throws F3BException;

	//MEV_2023-33
	public BigDecimal ExGetCountScadenzariPP(ScadenzarioModel aScadenzario) throws F3BException;
	public Vector ExRicercaScadenzarioPagedPP(ScadenzarioModel aScadenzario, int aPage) throws F3BException;
	
	// MEV_2026-1 - Funzione di test per switch dello scadenzario fine pena su tabella PENA_RESIDUA invece di SCADENZARIO_SIEP
	public BigDecimal ExGetCountScadenzariFinePena (ScadenzarioModel aScadenzario) throws F3BException;
	
}