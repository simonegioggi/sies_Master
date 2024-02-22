package siap.siep.pagoPA.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;

/**
 * MEV_2023-13 
 * Title: ICivilmenteObbligato 
 * Description: Interfaccia per la gestione del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public interface ICivilmenteObbligato {

	CivilmenteObbligatoModel ExInserisciCivilmenteObbligato(CivilmenteObbligatoModel com) throws F3BException;

	public void ExCancellaCivilmenteObbligato(BigDecimal idCivilmenteObbligato) throws F3BException;

	public CivilmenteObbligatoModel ExRicercaCivilmenteObbligatoByKey(BigDecimal idCivilmenteObbligato)
			throws F3BException;

	public void ExModificaCivilmenteObbligato(CivilmenteObbligatoModel com) throws F3BException;

	public Vector<CivilmenteObbligatoModel> ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(
			BigDecimal fasSieIdFascicolSiep) throws F3BException;

}