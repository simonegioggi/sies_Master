package siap.sico.certificato_omonimi_nsc.controller;

/**
* <p>Title: CertificatoOmonimiNscController</p>
* <p>Description: Classe Controller per CertificatoOmonimiNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.certificato_omonimi_nsc.model.CertificatoOmonimiNscModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ICertificatoOmonimiNsc {

	public CertificatoOmonimiNscModel ExInserisciCertificatoOmonimiNsc(
			CertificatoOmonimiNscModel aCertificatoOmonimiNsc) throws F3BException;

	public Vector ExRicercaCertificatoOmonimiNsc(CertificatoOmonimiNscModel aCertificatoOmonimiNsc)
			throws F3BException;

	public void ExModificaCertificatoOmonimiNsc(CertificatoOmonimiNscModel aCertificatoOmonimiNsc)
			throws F3BException;

	public void ExCancellaCertificatoOmonimiNsc(CertificatoOmonimiNscModel aCertificatoOmonimiNsc)
			throws F3BException;

	public void ExCancellaCertificatoOmonimiNscByDate() throws F3BException;

	public BigDecimal ExGetCountCertificatoOmonimiNsc(CertificatoOmonimiNscModel aCertificatoOmonimiNsc)
			throws F3BException;

	public CertificatoOmonimiNscModel ExRicercaCertificatoOmonimiNscById(BigDecimal aIdCertificatoOmonimi)
			throws F3BException;

	public Vector ExRicercaCertificatoOmonimiNscPaged(CertificatoOmonimiNscModel aCertificatoOmonimiNsc,
			int aPage) throws F3BException;

	public ByteArrayOutputStream ExGetDocumento(CertificatoOmonimiNscModel aCertificato) throws F3BException;

}