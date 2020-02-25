package siap.siep.circostanza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.circostanza.model.CircostanzaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: CircostanzaController
 * </p>
 * <p>
 * Description: Classe Controller per Circostanza
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
public interface ICircostanza {

	public CircostanzaModel ExInserisciCircostanza(CircostanzaModel aCircostanza) throws F3BException;

	public void ExInserisciCircostanze(Vector aCircostanze, boolean aggiornamento, String flagGiudizio,
			String flagSentenza, String codBil, String noteBil, BigDecimal aIdFascicoloSiep,
			BigDecimal aIdFascicoloSentenzaSige) throws F3BException;

	// public void ExInserisciCircostanze(Vector aCircostanze)
	// throws F3BException;

	// public void ExInserisciCircostanzeModificaSentenza(Vector aCircostanze, SentenzaModel aSentenza)
	// throws F3BException;

	// public CircostanzaModel ExModificaCircostanza(CircostanzaModel aCircostanza)
	/*
	 * public CircostanzaModel ExModificaCircostanza(CircostanzaModel aCircostanza, boolean flagAgg, String
	 * flagGiudizio, String flagSentenza, String codBil, String noteBil, BigDecimal idFascicolo) throws
	 * F3BException;
	 */

	public CircostanzaModel ExModificaCircostanza(CircostanzaModel aCircostanza, boolean flagAgg,
			String flagGiudizio, String flagSentenza, String codBil, String noteBil,
			BigDecimal idFascicoloSiep, BigDecimal aIdFascicoloSentenzaSige) throws F3BException;

	// public CircostanzaModel ExModificaCircostanzaModificaSentenza(CircostanzaModel aCircostanza,
	// SentenzaModel aSentenza)
	// throws F3BException;

	public void ExCancellaCircostanza(CircostanzaModel aCircostanza) throws F3BException;

	public Vector ExRicercaCircostanza(CircostanzaModel aCircostanza) throws F3BException;

	public Vector ExRicercaCircostanzaNoErr(CircostanzaModel aCircostanza) throws F3BException;

	public CircostanzaModel ExRicercaCircostanzaByKey(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaCircostanzaByIdFascicolo(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaCircostanzaDescByIdFascicolo(BigDecimal aKey) throws F3BException;

	public String ExInserisciCircostanzaWithoutSequence(ArrayList aCircostanze, Connection lConn)
			throws F3BException;

}