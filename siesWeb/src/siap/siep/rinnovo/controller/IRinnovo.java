package siap.siep.rinnovo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RinnovoController
 * </p>
 * <p>
 * Description: Classe Controller per Rinnovo
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
public interface IRinnovo {

	public RinnovoModel ExInserisciRinnovoVerbale(RinnovoModel aRinnovo, VerbaleModel aVerbale)
			throws F3BException;

	public List ExRicercaRinnovoIdNotifica(BigDecimal aIdNotifica) throws F3BException;

	public Vector ExRicercaRinnovoIdNotificaCodTipoRinnovo(BigDecimal aIdNotifica, String[] aTipoRinnovo)
			throws F3BException;

	public RinnovoModel ExRicercaRinnovoByKey(BigDecimal aKey) throws F3BException;

	public RinnovoModel ExRicercaRinnovoByKeyEvento(BigDecimal aKey) throws F3BException;
	
	// Ticket#202106220110
	public RinnovoModel ExRicercaUltimoRinnovoByKeyEvento (BigDecimal aKey) throws F3BException;
	
	public RinnovoModel ExModificaRinnovo(RinnovoModel aRinnovo) throws F3BException;

	public void ExCancellaRinnovo(RinnovoModel aRinnovo) throws F3BException;

	public RinnovoModel ExUpdateValidaRinnovo(FascicoloSiepModel aFasc, RinnovoModel aRinnovo)
			throws F3BException;

	public RinnovoModel ExUpdateDocument(RinnovoModel aRinnovo) throws F3BException;

	public RinnovoModel ExInserisciRinnovo(RinnovoModel aRinnovo) throws F3BException;

	public RinnovoModel ExUpdateValidaRinnovazioneNotifiche(FascicoloSiepModel aFasc, RinnovoModel aRinnovo)
			throws F3BException;

	public Vector ExInserisciRinnovo(Vector aRinnovo) throws F3BException;

	public RinnovoModel ExUpdateValidaRich8Bis(FascicoloSiepModel aFasc, Vector aRinnovo) throws F3BException;

	public RinnovoModel ExUpdateValidaSolleciti(FascicoloSiepModel aFasc, RinnovoModel aRinnovo)
			throws F3BException;

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 * 
	 * @param aProvvedimento
	 * @return Array con il Documento recuperato dal DB
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExGetDocumento(RinnovoModel aRinnovo) throws F3BException;

	// MEV_2023-33
	public Vector ExRicercaRinnovoIdNotificaCodTipoRinnovoStato(BigDecimal aIdNotifica, String[] aTipoRinnovo, String aStato)
			throws F3BException;
	public void ExCancellaRinnovoPP (RinnovoModel aRinnovo) throws F3BException;
	public RinnovoModel ExUpdateValidaRinnovoPP (FascicoloSiepModel aFasc, RinnovoModel aRinnovo)
			throws F3BException;
	 public RinnovoModel ExUpdateValidaRichiestaComma5 (FascicoloSiepModel aFasc, RinnovoModel aRinnovo)
       throws F3BException;
	 public RinnovoModel ExUpdateValidaRinnovazioneNotifichePP (FascicoloSiepModel aFasc, RinnovoModel aRinnovo)
	     throws F3BException;
	// MEV_2023-33
	
}