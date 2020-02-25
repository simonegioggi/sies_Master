package siap.sius.esperto.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import siap.sius.esperto.model.EspertoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IEsperto
 * </p>
 * <p>
 * Description: Classe Interfaccia Esperto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IEsperto {

	public EspertoModel ExInserisciEsperto(EspertoModel aEsperto) throws F3BException;

	public Vector ExRicercaEsperto(EspertoModel aEsperto) throws F3BException;

	public EspertoModel ExRicercaEspertoByKey(BigDecimal aKey) throws F3BException;

	public EspertoModel ExModificaEsperto(EspertoModel aEsperto) throws F3BException;

	public void ExCancellaEsperto(BigDecimal IdEsperto) throws F3BException;

	public Vector ExElencoCbxEspertiByCodUfficio(String aCodUfficio) throws F3BException;

	public Vector ExRicercaEspertoByCodUfficio(String aCodUfficio) throws F3BException;

	public int ExGetNumRicercaEsperto(EspertoModel aEsperto) throws F3BException;

	// MERGE v10: aggiunto metodo di ricerca
	public Collection ExElencoCbxEspertiByCodAndTipoUff(String aCodUfficio, String aCodTipoUfficio) throws F3BException;

}