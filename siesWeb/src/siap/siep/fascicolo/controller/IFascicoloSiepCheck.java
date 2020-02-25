package siap.siep.fascicolo.controller;

import java.util.Vector;

import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IFascicoloSiepCheck {

	public Vector ExContaFascicoli(String aChiaveUfficio) throws F3BException;

	public Vector ExContaFascicoliIscrittiSIEP(String aChiaveUfficio) throws F3BException;

	public Vector ExContaFascicoliIscrittiRES(String aChiaveUfficio) throws F3BException;

}