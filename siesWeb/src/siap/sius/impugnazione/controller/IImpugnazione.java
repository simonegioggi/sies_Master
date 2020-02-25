package siap.sius.impugnazione.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.utente.model.UtenteModel;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ImpugnazioneController
 * </p>
 * <p>
 * Description: Classe Controller per Impugnazione
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
public interface IImpugnazione {

	public ImpugnazioneModel ExInserisciImpugnazione(ImpugnazioneModel aImpugnazione, BigDecimal aIdEvento,
			BigDecimal aIdFascicolo, String aTipo, Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato)
			throws F3BException;

	public Vector ExRicercaImpugnazione(ImpugnazioneModel aImpugnazione) throws F3BException;

	public ImpugnazioneModel ExRicercaImpugnazioneByKey(BigDecimal aKey) throws F3BException;

	public ImpugnazioneModel ExModificaImpugnazione(ImpugnazioneModel aImpugnazione) throws F3BException;

	public void ExCancellaImpugnazione(ImpugnazioneModel aImpugnazione) throws F3BException;

	public ImpugnazioneModel ExRicercaImpugnazioneByIdEventoTipoProvv(BigDecimal aIdEvento, String aTipo,
			String[] aTipoImpugnazione, String aFlagAnnullate) throws F3BException;

	public boolean ExVerificaImpugnazione(BigDecimal aFascKey, String aTipoEvento) throws F3BException;

	public Vector<ImpugnazioneModel> ExRicercaImpugnazioni(BigDecimal aFascKey, String aTipoEvento,
			String[] aTipoImpugnazione, String aFlagAnnullate) throws F3BException;

	public ByteArrayOutputStream ExStampaImpugnazione(BigDecimal aImpKey, BigDecimal aEveKey,
			BigDecimal aFasKey, String lIdTemplate, String aUfficioUtenteConnesso, UtenteModel aUtenteModel)
			throws F3BException;

	public String ExRicercaDataRicorso(BigDecimal aIdEvento, String aTipo) throws F3BException;

	public void ExAnnullaImpugnazione(ImpugnazioneModel aImpugnazione, BigDecimal aIdEvento)
			throws F3BException;

	public Vector ExRicercaImpugnazioniAnnullateByProv(BigDecimal aIdProv, String aTipoProv)
			throws F3BException;

	public Vector<ImpugnazioneModel> ExRicercaImpugnazioniDelProvvedimento(BigDecimal aIdProv,
			String aTipoProv, String[] aTipoImpugnazione, String aFlagAnnullate) throws F3BException;

	public String ExRicercaDateRicorsi(BigDecimal aIdEvento, String aTipo) throws F3BException;

	public Vector<ImpugnazioneModel> ExRicercaImpugnazioniByIdEventoTipoProvvTipoImpFlagAnn(
			BigDecimal aIdEvento, String aTipo, String[] aTipoImpugnazione, String aFlagAnnullate)
			throws F3BException;

	public ImpugnazioneModel ExInserisciOpposizione(ImpugnazioneModel aImpugnazione, BigDecimal aIdEvento,
			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato) throws F3BException;

}