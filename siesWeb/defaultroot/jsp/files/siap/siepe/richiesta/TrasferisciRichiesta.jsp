<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siepe.richiesta.action.ICostantiRichiesta"%>
<%@ page import="siap.siepe.richiesta.model.RichiestaModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="richiesta" scope="request" class="siap.siepe.richiesta.model.RichiestaModel"/>
<!--<jsp:useBean id="ufficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>-->

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="TrasferisciRichiesta">
	<table cellspacing="2" cellpadding="2" style="width: 90%;">
		<tr>
 		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    	<%--
		   	<td class="l">Ufficio di Destinazione</td>
       		<td class="l"><font class="campo"><%=ufficioDestinatario.getDescrTipoUfficio() + " " +  ufficioDestinatario.getDescrComune()%></font></td>
		--%>
		</tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.richiesta.action.ActTrasferisciRichiesta" >
  <input type="HIDDEN" name="<%=ICostantiRichiesta.CAMPO_ID_RICHIESTA%>" value="<%=richiesta.getIdRichiesta()%>" >
  <input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=richiesta.getCodUfficioDestinatario()%>" >
</form>