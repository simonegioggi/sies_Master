package siap.sico.security.controller;

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import f3b.security.model.FunctionModel;
import f3b.security.model.ProfileModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISecurity {

	public UtenteModel ExLogin(UtenteModel aUtente, UfficioModel aUfficio) throws F3BException;

	public UtenteModel ExLogin(UtenteModel aUtente) throws F3BException;

	public void ExLogout(UtenteModel aUtente) throws F3BException;

	public FunctionModel ExLoadFunzioniMenu(ProfileModel aProfiloUtente, FunctionModel aFunzionePadre)
			throws F3BException;

	public FunctionModel getFunzioneByAzioneCodProfilo(String aNomeAzione, BigDecimal aCodProfilo)
			throws F3BException;

	public ArrayList getFunFiglieByCodProfilo(BigDecimal aCodProfilo, BigDecimal aIdFunzPadre)
			throws F3BException;

	public ArrayList getFunFiglieByCodProfiloTipoVis(BigDecimal aCodProfilo, BigDecimal aIdFunzPadre,
			String aTipoVisualizzazione) throws F3BException;

	public UtenteModel getUtenteValido(UtenteModel aUtente, UfficioModel aUfficio) throws F3BException;

	public UtenteModel getUtenteValido(UtenteModel aUtente) throws F3BException;

	public UfficioModel getUfficioUtente(UtenteModel aUtente) throws F3BException;

	/**
	 * Affettua il caricamento delle voci del menù di scelta rapida
	 * 
	 * @param aProfiloUtente
	 * @param aFunzionePadre
	 * @return FunctionModel caricato con la lista della funzioni
	 * @throws F3BException
	 */
	public FunctionModel ExLoadFunzioniMenuSceltaRapida(ProfileModel aProfiloUtente,
			FunctionModel aFunzionePadre) throws F3BException;

}