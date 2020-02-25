package siap.sige.udienza.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;

/**
 * <p>
 * Title: UdienzaSigeRuoloController
 * </p>
 * <p>
 * Description: Classe Controller per UdienzaSigeRuolo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
* @version 1.0
*/
@SuppressWarnings("rawtypes")
public interface IUdienzaSigeRuolo extends IUdienzaSige {

	public Vector ExRicercaUdienzaSigePerRuolo(UdienzaSigeModel aUdienzaSige, String aTipoRito) throws F3BException;

	public Vector ExRicercaUdienza(UdienzaSigeModel aUdienzaSige, int aNumOccorrenze) throws F3BException;

	public UdienzaProcedimentoSigeModel ExInserisciFissazioneUdienza(
			UdienzaProcedimentoSigeModel aNuovaUdienzaProc, FascicoloSigeModel aFasSige,
			EventoNotificaModel aEve, ProvvedimentoSigeModel lProvvedimento, Vector lTenori,
			UdienzaProcedimentoSigeModel aVecchiaUdienzaProc) throws F3BException;

	public UdienzaProcedimentoSigeModel ExModificaFissazioneUdienza(
			UdienzaProcedimentoSigeModel aNuovaUdienzaProc, FascicoloSigeModel aFasSige,
			EventoNotificaModel aEve, ProvvedimentoSigeModel lProvvedimento, Vector lTenori,
			UdienzaProcedimentoSigeModel aVecchiaUdienzaProc) throws F3BException;

	public ByteArrayOutputStream ExStampaFissazioneUdienza(BigDecimal aIdFascicolo, EventoModel lEvento,
			String aCodUff, UtenteModel aUtenteModel) throws F3BException;

	public void ExInserisciFissazioneUdienza(FascicoloSigeModel aFasSige, EventoNotificaModel aEve,
			ProvvedimentoSigeModel lProvvedimento, Vector lTenori) throws F3BException;

	// 20170913: [SG] aggiunta query
	public Vector<Object> cercaCollegi(Date dataUdienza, String codUfficioAppartenenza, String codMagis,
			BigDecimal idSezione, String tipoRito, String idCollegio) throws F3BException;
	
}
