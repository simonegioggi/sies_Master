package siap.siep.penacomplessiva.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;

/**
 * Title: PenaComplessivaController
 * Description: Classe Controller per PenaComplessiva
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IPenaComplessiva {

	public PenaComplessivaModel ExInserisciPenaComplessiva(PenaComplessivaModel aPenaComplessiva)
			throws F3BException;

	public PenaComplessivaModel ExInserisciPenaCompSanzioneSostContinuazioni(
			PenaComplessivaModel aPenaComplessiva, SanzioneSostitutivaModel aSanzioneSostitutiva,
			List aContinuazioni) throws F3BException;

	public void ExInserisciUlterioriContinuazioni(List aContinuazioni) throws F3BException;

	public Vector ExRicercaPenaComplessiva(PenaComplessivaModel aPenaComplessiva) throws F3BException;

	public Vector ExRicercaPenaComplessivaNoError(PenaComplessivaModel aPenaComplessiva) throws F3BException;

	public PenaComplessivaModel ExRicercaPenaComplessivaByKey(BigDecimal aKey) throws F3BException;

	public PenaComplessivaSanzioneSostitutivaModel ExRicercaPenaComplessivaSanzioneSostitutivaByKey(
			BigDecimal aKey) throws F3BException;

	public PenaComplessivaSanzioneSostitutivaModel ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(
			BigDecimal aIdFascicoloSiep) throws F3BException;

	public DettaglioPenaComplessivaModel ExRicercaPenaCompSanzioneSostContinuazioniByKey(
			BigDecimal aIdPenaComplessiva) throws F3BException;

	public DettaglioPenaComplessivaModel ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException;

	public PenaComplessivaModel ExModificaPenaComplessiva(PenaComplessivaModel aPenaComplessiva)
			throws F3BException;

	public PenaComplessivaSanzioneSostitutivaModel ExModificaPenaComplessivaSanzioneSostitutiva(
			PenaComplessivaModel aPenaComplessiva, SanzioneSostitutivaModel aSanzioneSostitutiva,
			boolean aflagSanzioneSostitutiva) throws F3BException;

	public void ExCancellaPenaComplessiva(PenaComplessivaModel aPenaComplessiva) throws F3BException;

	public PenaComplessivaModel ExRicercaPenaComplessivaByIdFascicolo(BigDecimal aKey) throws F3BException;

	public void ExCancellaPenaComplessivaSanzioneSostitutivaContinuazioni(
			PenaComplessivaModel aPenaComplessiva) throws F3BException;

	public String ExInserisciPenaComplessivaWithoutSequence(
			PenaComplessivaSanzioneSostitutivaModel aPenaComplessiva, Connection lConn) throws F3BException;

	public PenaComplessivaModel ExInserisciPenaCompSige(PenaComplessivaModel aPenaComplessiva,
			SanzioneSostitutivaModel aSanzioneSostitutiva, List aContinuazioni, BigDecimal aIdFasSigeSen)
			throws F3BException;

	public DettaglioPenaComplessivaModel ExRicercaPenaComplessivaCompletaByIdSIGE(BigDecimal aKey)
			throws F3BException;

	public void ExCancellaPenaComplessivaSige(PenaComplessivaModel aPenaComplessiva) throws F3BException;

	/**
	 * Aggiunto metodo di ricerca per la Gestione Pene Sostitutive: Semilibertà/Detenzione Domiciliare
	 * 
	 * @author 	sgioggi
	 * @since	MEV_2023-33
	 * @param 	idFascicoloSiep
	 * @param 	highValue 
	 * @return 	PenaComplessivaSanzioneSostitutivaModel
	 * @throws	F3BException
	 */
	public PenaComplessivaSanzioneSostitutivaModel ExRicercaPenaComplessivaPenaSostitutivaByIdFascicoloSiep(
			BigDecimal idFascicoloSiep, String highValue) throws F3BException;

}