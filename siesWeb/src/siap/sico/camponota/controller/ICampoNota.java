package siap.sico.camponota.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ICampoNota {

	public CampoNotaModel ExRicercaCampoNotaByKey(BigDecimal aKey) throws F3BException;

	public CampoNotaModel ExRicercaCampoNotaByIdEvento(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaVectCampoNotaByIdEvento(BigDecimal aKey) throws F3BException;

}