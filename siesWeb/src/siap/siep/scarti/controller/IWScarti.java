package siap.siep.scarti.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.scarti.model.WScartiModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: WScartiController
 * </p>
 * <p>
 * Description: Classe Controller per WScarti
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
public interface IWScarti {

	public WScartiModel ExInserisciWScarti(WScartiModel aWScarti) throws F3BException;

	public Vector ExRicercaWScarti(WScartiModel aWScarti) throws F3BException;

	public WScartiModel ExRicercaWScartiByKey(BigDecimal aKey) throws F3BException;

	public WScartiModel ExModificaWScarti(WScartiModel aWScarti) throws F3BException;

	public void ExCancellaWScarti(WScartiModel aWScarti) throws F3BException;

	public Vector ExRicercaScartiPaged(WScartiModel aScarti, int aPage) throws F3BException;

	public BigDecimal ExGetCountScarti(WScartiModel aScarti) throws F3BException;

}