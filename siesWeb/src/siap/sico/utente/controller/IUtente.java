package siap.sico.utente.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.utente.model.UtenteModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: UtenteController
 * </p>
 * <p>
 * Description: Classe Controller per Utente
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
public interface IUtente {

	public UtenteModel ExInserisciUtente(UtenteModel aUtente) throws F3BException;

	public Vector ExRicercaUtente(UtenteModel aUtente) throws F3BException;

	public Vector ExListaUtentiAttivi(int aPage) throws F3BException;

	public Vector ExListaUtentiAttiviFromView(int aPage, String aDistretto) throws F3BException;

	public Vector ExListaUtentiAttiviFromViewPerUfficio(int aPage, String aUfficio) throws F3BException;

	public Vector ExListaUtentiNonAttiviFromView(int aPage, String aDistretto) throws F3BException;

	public Vector ExListaUtentiNonAttiviFromViewPerUfficio(int aPage, String aUfficio) throws F3BException;

	public Vector ExListaUtentiNonAttivi(int aPage) throws F3BException;

	public UtenteModel ExRicercaUtenteByKey(String aKey) throws F3BException;

	public UtenteModel ExModificaUtente(UtenteModel aUtente) throws F3BException;

	public void ExCancellaUtente(UtenteModel aUtente) throws F3BException;

	public void ExModificaPassword(UtenteModel aUtente) throws F3BException;

	public Vector ExRicercaUtentiPerUfficio(String codUfficio, String codDistretto, String cognome,
			String nome, String codUt) throws F3BException;

	public Vector ExRicercaUtentePerUfficioCognomeNome(String codUfficio, String cognome, String nome)
			throws F3BException;

	public BigDecimal ExGetCountUtentiNonAttivi(String aDistretto) throws F3BException;

	public BigDecimal ExGetCountUtentiNonAttiviPerUfficio(String aUfficio) throws F3BException;

	public BigDecimal ExGetCountUtentiAttivi(String aDistretto) throws F3BException;

	public BigDecimal ExGetCountUtentiAttiviPerUfficio(String Ufficio) throws F3BException;

	public Vector ExRicercaUtentiAttiviPerUfficio(String codUfficio, String aCognome) throws F3BException;

	public UtenteModel ExModificaUtenteDatiAccessoNSC(UtenteModel aUtente) throws F3BException;

	public UtenteModel ExResetDatiAccessoNSC(UtenteModel aUtente) throws F3BException;

}