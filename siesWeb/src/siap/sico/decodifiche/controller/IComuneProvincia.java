package siap.sico.decodifiche.controller;

import java.util.Vector;

import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IComuneProvincia {

	public Vector ExListaProv() throws F3BException;

	public Vector ExListaProvPerTipoUfficio(String TipoUfficio) throws F3BException;

}