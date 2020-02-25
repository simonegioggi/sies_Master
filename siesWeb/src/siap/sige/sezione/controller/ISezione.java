package siap.sige.sezione.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import siap.sige.sezione.model.SezioneModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ICuratore
 * </p>
 * <p>
 * Description: Classe Interfaccia Sezione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ISezione {

	public SezioneModel ExInserisciSezione(SezioneModel aSezione) throws F3BException;

	public Vector ExRicercaSezione(SezioneModel aSezione) throws F3BException;

	public SezioneModel ExRicercaSezioneByKey(BigDecimal aKey) throws F3BException;

	public SezioneModel ExModificaSezione(SezioneModel aSezione) throws F3BException;

	public void ExCancellaSezione(BigDecimal IdSezione) throws F3BException;

	public Collection ExElencoCbxSezioniByCodUfficio(String aCodUfficio) throws F3BException;

	public Vector ExRicercaSezioneByCodUfficio(String aCodUfficio) throws F3BException;

	public int ExGetNumRicercaSezione(SezioneModel aSezione) throws F3BException;

	// 20171013: [EC] aggiungo metodo per recuperare le sezioni per codice magistrato ed ufficio appartenenza
	public Collection getElencoSezioniModificabiliByCodUfficio(String codiceMagistrato,
			String codUfficioUtenteConnesso) throws F3BException;

}