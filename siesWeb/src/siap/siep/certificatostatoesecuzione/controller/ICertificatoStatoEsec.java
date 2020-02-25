package siap.siep.certificatostatoesecuzione.controller;

/**
* <p>Title: CertificatoStatoEsecController</p>
* <p>Description: Classe Controller per CertificatoStatoEsec</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.utente.model.UtenteModel;
import siap.siep.certificatostatoesecuzione.model.CertificatoStatoEsecModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ICertificatoStatoEsec {

	public CertificatoStatoEsecModel ExInserisciCertificatoStatoEsec(
			CertificatoStatoEsecModel aCertificatoStatoEsec) throws F3BException;

	public Vector ExRicercaCertificatoStatoEsec(CertificatoStatoEsecModel aCertificatoStatoEsec)
			throws F3BException;

	public void ExCancellaCertificatoStatoEsec(CertificatoStatoEsecModel aCertificatoStatoEsec)
			throws F3BException;

	public CertificatoStatoEsecModel ExRicercaCertificatoStatoEsecById(BigDecimal aIdCertificatoStatoEsec)
			throws F3BException;

	public ByteArrayOutputStream ExGetDocumento(CertificatoStatoEsecModel aCertificatoStatoEsec)
			throws F3BException;

	public ByteArrayOutputStream ExStampaDocumento(FascicoloSiepModel aFasc, String lIdTemplate,
			UtenteModel aUtente) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoPDF(FascicoloSiepModel aFasc, String lIdTemplate,
			UtenteModel aUtente) throws F3BException;

}