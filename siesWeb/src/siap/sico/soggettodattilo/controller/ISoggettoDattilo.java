package siap.sico.soggettodattilo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SoggettoDattiloController
 * </p>
 * <p>
 * Description: Classe Controller per SoggettoDattilo
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
public interface ISoggettoDattilo {

	public SoggettoDattiloModel ExInserisciSoggettoDattilo(SoggettoDattiloModel aSoggettoDattilo)
			throws F3BException;

	public Vector ExRicercaSoggettoDattilo(SoggettoDattiloModel aSoggettoDattilo) throws F3BException;

	public SoggettoDattiloModel ExRicercaSoggettoDattiloByKey(BigDecimal aKey) throws F3BException;

	public SoggettoDattiloModel ExModificaSoggettoDattilo(SoggettoDattiloModel aSoggettoDattilo)
			throws F3BException;

	public void ExCancellaSoggettoDattilo(SoggettoDattiloModel aSoggettoDattilo) throws F3BException;

	public SoggettoDattiloModel ExUpdateDocument(SoggettoDattiloModel aSoggettoDattilo) throws F3BException;

	public ByteArrayOutputStream ExGetDocumento(SoggettoDattiloModel aSoggettoDattilo) throws F3BException;

}