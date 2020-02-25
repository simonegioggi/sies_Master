package siap.siepe.richiesta.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.siepe.richiesta.model.RichiestaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IRichiesta
 * </p>
 * <p>
 * Description: Classe Interfaccia per la Richiesta
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
public interface IRichiesta {

	public RichiestaModel ExInserisciRichiesta(RichiestaModel aRichiesta) throws F3BException;

	public Vector ExRicercaRichiesta(RichiestaModel aRichiesta) throws F3BException;

	public RichiestaModel ExRicercaRichiestaByKey(BigDecimal aKey) throws F3BException;

	public RichiestaModel ExModificaRichiesta(RichiestaModel aRichiesta) throws F3BException;

	public void ExCancellaRichiesta(BigDecimal aKey) throws F3BException;

	public void ExAggiornaValidazioneRichiesta(RichiestaModel aRichiesta) throws F3BException;

	public ByteArrayOutputStream ExGetDocumento(RichiestaModel aRichiesta) throws F3BException;

	public RichiestaModel ExUpdateDocument(RichiestaModel aRichiesta) throws F3BException;

	public ByteArrayOutputStream ExGetDocBlob(RichiestaModel aRichiesta) throws F3BException;

}