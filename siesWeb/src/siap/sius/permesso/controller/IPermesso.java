package siap.sius.permesso.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.permesso.model.CriteriRicercaProvPermessiLicenzeModel;
import siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel;
import siap.sius.permesso.model.TotaliPermessiLicenzeModel;

/**
 * IPermesso - Classe di Interfaccia per Controller di Permesso
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IPermesso {

	public Vector ExRicercaPermessiBySoggettoPagina(SoggettoModel aSogModel, String lCodUfficioUtenteConnesso,
			String lCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodPermesso,
			Date dataDalInCanc, Date dataAlInCanc, int aPageNum) throws F3BException;

	public BigDecimal ExGetNumRicercaPermessiBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodUffOTrib, String lCodDistretto,
			String lIncludeRigettati, String lCodPermesso, Date dataDalInCanc, Date dataAlInCanc)
			throws F3BException;

	public Vector ExRicercaPermessiDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodPermesso,
			Date dataDalInCanc, Date dataAlInCanc) throws F3BException;

	public Vector ExRicercaLicenzeBySoggettoPagina(SoggettoModel aSogModel, String lCodUfficioUtenteConnesso,
			String lCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodLicenza,
			Date dataDalInCanc, Date dataAlInCanc, int aPageNum) throws F3BException;

	public BigDecimal ExGetNumRicercaLicenzeBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodUffOTrib, String lCodDistretto,
			String lIncludeRigettati, String lCodLicenza, Date dataDalInCanc, Date dataAlInCanc)
			throws F3BException;

	public Vector ExRicercaLicenzeDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodLicenza,
			Date dataDalInCanc, Date dataAlInCanc) throws F3BException;

	public DepositoDecretoMotivazioniLicenzaModel ExRicercaPermessoDepositato(BigDecimal aIDFasSius)
			throws F3BException;

	public DepositoDecretoMotivazioniLicenzaModel ExRicercaLicenzaDepositata(BigDecimal aIDFasSius)
			throws F3BException;

	public DepositoDecretoMotivazioniLicenzaModel ExRicercaPermessoLicenzaDepositati(BigDecimal aIDLicLibAnt)
			throws F3BException;

	public LicenzaLibAnticipataModel ExRicercaTipoPermessoLicenzaDepositata(BigDecimal aIDFascicoloSius)
			throws F3BException;

	// MEV_2025-48: paginata la ricerca
	public Collection ExRicercaProvvedimentiPermessiLicenze(
			CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca, int page) throws F3BException;

	public int ExGetNumProvvedimentiPermessiLicenze(CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca)
			throws F3BException;

	public TotaliPermessiLicenzeModel ExGetTotProvvedimentiPermessiLicenze(
			CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca) throws F3BException;

	public ByteArrayOutputStream ExStampaProvvedimentiPermessiLicenze(
			CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca, UtenteModel aUtente) throws F3BException;

}