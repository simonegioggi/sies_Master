package siap.siep.motivoevento.controller;

import java.math.BigDecimal;

import siap.siep.motivoevento.model.MotivoEventoModel;
import f3b.util.F3BException;




/**
* <p>Title: MotivoEventoController</p>
* <p>Description: Classe Controller per MotivoEvento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface IMotivoEvento
{
public MotivoEventoModel ExRicercaMotivoEventoByEveIdEvento (BigDecimal aKey)
 		        throws F3BException;

}
