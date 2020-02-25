package siap.sico.profilo.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.profilo.model.ProfiloModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ProfiloController
 * </p>
 * <p>
 * Description: Classe Controller per Profilo
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
public interface IProfilo {

	public ProfiloModel ExInserisciProfilo(ProfiloModel aProfilo) throws F3BException;

	public Vector ExRicercaProfilo(ProfiloModel aProfilo) throws F3BException;

	public ProfiloModel ExRicercaProfiloByKey(BigDecimal aKey) throws F3BException;

	public ProfiloModel ExModificaProfilo(ProfiloModel aProfilo) throws F3BException;

	public void ExCancellaProfilo(ProfiloModel aProfilo) throws F3BException;

	public Vector ExRicercaListaProfili() throws F3BException;

	public ProfiloModel ExRicercaProfiloByCodUtente(String aCodUtente) throws F3BException;

	/**
	 * Definizione del metodo di recupero elenco profili per il codice uffcio. tale metodo è implementato
	 * nella corrsipondente calsse controller <code>ProfiloController</code>
	 * <p>
	 * 
	 * @param aCodTipoUffcio
	 *            String Codice tipo uffcio
	 * @throws F3BException
	 *             propaga l'errore di eccezione
	 * @return Vector Insieme di occorrenze.
	 */
	public Vector ExRicercaProfiliByCodTipoUfficio(String aCodTipoUffcio) throws F3BException;

}