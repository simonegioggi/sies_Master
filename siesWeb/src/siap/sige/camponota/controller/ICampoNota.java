package siap.sige.camponota.controller;

import java.math.BigDecimal;

import siap.sico.camponota.model.CampoNotaModel;
import f3b.util.F3BException;

public interface ICampoNota {
    public void ExAggiornaNoteByIdEvento (CampoNotaModel[] note, BigDecimal idEvento) throws F3BException;
}
