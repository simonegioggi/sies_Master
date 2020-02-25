package siap.siepe.attivita.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.siepe.attivita.model.AttivitaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AttivitaController
 * </p>
 * <p>
 * Description: Classe Controller per Attivita
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
public interface IAttivita {

	public AttivitaModel ExInserisciAttivita(AttivitaModel aAttivita) throws F3BException;

	public AttivitaModel[] ExInserisciAttivita(AttivitaModel[] aListaAttivita, Connection aConn)
			throws Exception;

	public Vector ExRicercaAttivita(AttivitaModel aAttivita) throws F3BException;

	public AttivitaModel ExRicercaAttivitaByKey(BigDecimal aKey) throws F3BException;

	public AttivitaModel ExModificaAttivita(AttivitaModel aAttivita) throws F3BException;

	public void ExCancellaAttivita(BigDecimal aIdAttivita) throws F3BException;

	public ByteArrayOutputStream ExGetDocumento(AttivitaModel aAttivita) throws F3BException;

	public AttivitaModel ExUpdateDocument(AttivitaModel aAttivita) throws F3BException;

	public AttivitaModel ExChiusuraAttivita(AttivitaModel aAttivita) throws F3BException;

	public ByteArrayOutputStream ExGetDocBlob(AttivitaModel aAttivita) throws F3BException;

	public void ExAggiornaValidazioneAttivita(AttivitaModel aAttivita) throws F3BException;

}