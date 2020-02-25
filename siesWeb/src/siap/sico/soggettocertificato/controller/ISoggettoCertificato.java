package siap.sico.soggettocertificato.controller;

/**
* <p>Title: SoggettoCertificatoController</p>
* <p>Description: Classe Controller per SoggettoCertificato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISoggettoCertificato {

	public SoggettoCertificatoModel ExInserisciSoggettoCertificato(
			SoggettoCertificatoModel aSoggettoCertificato) throws F3BException;

	public Vector ExRicercaSoggettoCertificato(SoggettoCertificatoModel aSoggettoCertificato)
			throws F3BException;

	public void ExModificaSoggettoCertificato(SoggettoCertificatoModel aSoggettoCertificato)
			throws F3BException;

	public void ExCancellaSoggettoCertificato(SoggettoCertificatoModel aSoggettoCertificato)
			throws F3BException;

	public BigDecimal ExGetCountSoggettoCertificato(SoggettoCertificatoModel aSoggettoCertificato)
			throws F3BException;

	public SoggettoCertificatoModel ExRicercaSoggettoCertificatoById(BigDecimal aIdSoggettoCertificato)
			throws F3BException;

	public Vector ExRicercaSoggettoCertificatoPaged(SoggettoCertificatoModel aSoggettoCertificato, int aPage)
			throws F3BException;

	public ByteArrayOutputStream ExGetDocumento(SoggettoCertificatoModel aCertificato) throws F3BException;

}