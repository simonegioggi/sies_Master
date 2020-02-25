package siap.siep.alias.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.alias.model.AliasModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AliasController
 * </p>
 * <p>
 * Description: Classe Controller per Alias
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
public interface IAlias {

	public AliasModel ExInserisciAlias(AliasModel aAlias) throws F3BException;

	public Vector ExRicercaAlias(AliasModel aAlias) throws F3BException;

	public AliasModel ExRicercaAliasByKey(BigDecimal aKey) throws F3BException;

	public AliasModel ExModificaAlias(AliasModel aAlias) throws F3BException;

	public void ExCancellaAlias(AliasModel aAlias) throws F3BException;

	public Vector ExRicercaAliasByIdSoggetto(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaAliasByIdSoggettoPaged(BigDecimal aKey, int aPage) throws F3BException;

	public BigDecimal ExGetCountAliasByIdSoggetto(BigDecimal aKey) throws F3BException;

}