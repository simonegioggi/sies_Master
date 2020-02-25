package siap.siepe.espertoattivita.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siepe.espertoattivita.model.EspertoAttivitaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EspertoAttivitaController
 * </p>
 * <p>
 * Description: Classe Controller per EspertoAttivita
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
public interface IEspertoAttivita {

	public EspertoAttivitaModel ExInserisciEspertoAttivita(EspertoAttivitaModel aEspertoAttivita)
			throws F3BException;

	public Vector ExRicercaEspertiAttiviXAttivita(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaEspertiXAttivita(BigDecimal aKey) throws F3BException;

	public EspertoAttivitaModel ExModificaEspertoAttivita(EspertoAttivitaModel aEspertoAttivita)
			throws F3BException;

	public EspertoAttivitaModel ExChiudiEspertoAttivita(EspertoAttivitaModel aEspertoAttivita)
			throws F3BException;

}