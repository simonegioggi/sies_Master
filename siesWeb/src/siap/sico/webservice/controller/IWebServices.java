package siap.sico.webservice.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIOPERAZIONEDocument;
import it.mig.sies.type.ESITODocument;
import it.mig.sies.type.esecuzione.DATIRISPOSTAESECUZIONEDocument;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.webservice.model.DatiNscToSiesModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: ResidenzaController
 * </p>
 * <p>
 * Description: Classe Controller per Residenza
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
public interface IWebServices {

	// MEV 16: aggiunti parametri di passaggio per inserire solo sentenza e cumulo, NO sogg e fasc
	// per gestione fascicolo coinvolto nel cumulo
	// MEV 16 CUMULO: aggiunto parametro di passaggio per gestione dei titoli da associare al cumulo
	public void ExInserisciFascicoloDaNsc(DatiNscToSiesModel aDatiNscToSiesModel, String aWriteSentenza,
			boolean isForCumulo, String idFascicoloSiep, String lCodOperatoreIns, String lCodUfficioIns,
			String idIstruttoriaCumulo) throws F3BException;

	// MEV 16: aggiunto parametri di passaggio per differenziare collegato al cumulo
	public Vector ExRicercaSoggettoWebServices(DatiNscToSiesModel aDatiNscToSiesModel, boolean isForCumulo,
			String idIstruttoriaCumulo) throws F3BException;

	public ComuneModel ExRicercaProvinciaSedeGiudiziaria(String aCodIstatComuneNascita) throws F3BException;

	public UfficioModel ExRicercaCodiceUfficio(String aCodTipoUfficio, String aCodComune) throws F3BException;

	public Vector ExRicercaReatiInContinuazione(long aFascicoloSIEP) throws F3BException;

	public Vector ExRicercaTitoloEsecutivoTrasferito(Date dataRicercaInizio, Date dataRicercaFine,
			String lDestinazione, String lStatoFascicolo, String lCodUfficioUtenteConnesso, int aPage)
			throws F3BException;

	public BigDecimal ExGetCountRicercaTitoloEsecutivoTrasferito(Date dataRicercaInizio, Date dataRicercaFine,
			String lDestinazione, String lStatoFascicolo, String lCodUfficioUtenteConnesso)
			throws F3BException;

	// MEV 16 CUMULO: per il cumulo eseguo un'altra count
	public BigDecimal ExGetCountCercaFascSogg(BigDecimal lKeyProvvNsc, BigDecimal lKeySoggNsc,
			boolean isForCumulo) throws F3BException;

	public void ExScritturaChiaviNsc(CHIAVIDocument.CHIAVI adatiChiavi,
			DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione, ESITODocument.ESITO adatiEsito,
			BigDecimal lAnnoFascicolo, BigDecimal lNumeroFascicolo, UtenteModel lUteMod) throws F3BException;

	public void ExCancellazioneChiaviNsc(CHIAVIDocument.CHIAVI adatiChiavi,
			DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione, ESITODocument.ESITO adatiEsito,
			BigDecimal lAnnoFascicolo, BigDecimal lNumeroFascicolo, UtenteModel lUteMod) throws F3BException;

	public FascicoloSiepModel ExPrelevaAnnoNumeroFas(BigDecimal lKeyProvvNsc, BigDecimal lKeySoggNsc)
			throws F3BException;

	public void ExScritturaChiaviNscUDS(it.mig.sies.type.esecuzione.CHIAVIDocument.CHIAVI adatiChiavi,
			it.mig.sies.type.esecuzione.DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione,
			it.mig.sies.type.esecuzione.ESITODocument.ESITO adatiEsito, BigDecimal lAnnoFascicolo,
			BigDecimal lNumeroFascicolo, UtenteModel lUteMod,
			DATIRISPOSTAESECUZIONEDocument.DATIRISPOSTAESECUZIONE aDatiRispostaEsecuzione)
			throws F3BException;

}