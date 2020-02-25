package siap.siep.misuracautelare.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MisuraCautelareController
 * </p>
 * <p>
 * Description: Classe Controller per MisuraCautelare
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
public interface IMisuraCautelare {

	public MisuraCautelareModel ExInserisciMisuraCautelare(MisuraCautelareModel aMisuraCautelare)
			throws F3BException;

	public MisuraCautelareModel ExInserisciMisuraCautelare(Vector aMisuraCautelare) throws F3BException;

	public MisuraCautelareModel ExInserisciMisuraCautelareByBdmc(Vector aMisuraCautelare, Vector aMisCautBdmc)
			throws F3BException;

	public Vector ExRicercaMisuraCautelare(MisuraCautelareModel aMisuraCautelare) throws F3BException;

	public Vector ExRicercaMisureCautelariByIdFascicolo(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaMisureCautelariByIdFascicoloNoDataNull(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaMisureCautelariByIdFascicoloSiDataNull(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaMisureCautelariByIdFascicoloSoloDataInizio(BigDecimal aKey) throws F3BException;

	public MisuraCautelareModel ExRicercaMisuraCautelareByKey(BigDecimal aKey) throws F3BException;

	public MisuraCautelareModel ExModificaMisuraCautelare(MisuraCautelareModel aMisuraCautelare)
			throws F3BException;

	public MisuraCautelareModel ExModificaMisuraCautelareModificaPosizioneGiuridica(
			MisuraCautelareModel aMisuraCautelare, PosizioneGiuridicaModel aPosizioneGiuridica)
			throws F3BException;

	public void ExCancellaMisuraCautelare(MisuraCautelareModel aMisuraCautelare) throws F3BException;

	public MisuraCautelareModel ExInserisciMisuraCautelareInserisciPosizioneGiuridica(
			Vector aMisuraCautelare, PosizioneGiuridicaModel aPosizioneGiuridica) throws F3BException;

	public MisuraCautelareModel ExRicercaMisuraCautelareSenzaDataFineByIdFascicolo(BigDecimal aKey)
			throws F3BException;

	public String ExInserisciMisuraCautelareWithoutSequence(ArrayList aMisuraCautelare, Connection lConn)
			throws F3BException;

}