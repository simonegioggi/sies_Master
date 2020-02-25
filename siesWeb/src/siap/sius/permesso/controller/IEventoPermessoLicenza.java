package siap.sius.permesso.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EventoPermessoLicenzaController
 * </p>
 * <p>
 * Description: Classe Controller per EventoPermessoLicenza
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
public interface IEventoPermessoLicenza {

	public EventoPermessoLicenzaModel ExInserisciEventoPermessoLicenza(
			EventoPermessoLicenzaModel aEventoPermessoLicenza) throws F3BException;

	/*
	 * public Vector ExRicercaEventoPermessoLicenza (EventoPermessoLicenzaModel aEventoPermessoLicenza )
	 * throws F3BException;
	 */
	public Vector ExRicercaEventoPermessoLicenzaByKeyLicLib(BigDecimal aKey) throws F3BException;

	public EventoPermessoLicenzaModel ExRicercaEventoPermessoLicenzaByKey(BigDecimal aKey)
			throws F3BException;

	public EventoPermessoLicenzaModel ExModificaEventoPermessoLicenza(
			EventoPermessoLicenzaModel aEventoPermessoLicenza) throws F3BException;

	public void ExCancellaEventoPermessoLicenza(EventoPermessoLicenzaModel aEventoPermessoLicenza)
			throws F3BException;

}