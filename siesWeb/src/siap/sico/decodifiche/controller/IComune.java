package siap.sico.decodifiche.controller;

import java.util.Collection;
import java.util.Vector;
//import java.rmi.RemoteException;

import f3b.util.F3BException;
import siap.sico.decodifiche.model.ComuneModel;

@SuppressWarnings("rawtypes")
public interface IComune {

	public ComuneModel ExGetCodiceComune(ComuneModel lModel) throws F3BException;

	public ComuneModel ExGetCodiceComuneValidita(ComuneModel lModel) throws F3BException;

	public ComuneModel ExGetDescComune(ComuneModel lModel) throws F3BException;

	public Collection ExGetComuni() throws F3BException;

	public Vector ExGetListaComuni(ComuneModel lModel) throws F3BException;

	public Vector ExGetListaComuniTds() throws F3BException;

	public Vector ExGetListaComuniTdsm() throws F3BException;

	public Vector ExGetListaSediUNEPperDistretto(String aCodDistretto) throws F3BException;

	public boolean ExIsComuneSedeUNEP(String aCodComune) throws F3BException;

	public ComuneModel ExRicercaComuneByKey(String aKey) throws F3BException;

	// MEV_21: ricerco il comune dato il codice catastale
	public ComuneModel ExRicercaComuneByCodCatastale(String ccc) throws F3BException;

}