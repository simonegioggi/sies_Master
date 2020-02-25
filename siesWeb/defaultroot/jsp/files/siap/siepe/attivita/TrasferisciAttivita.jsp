<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>
<%@ page import="siap.siepe.attivita.model.AttivitaModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="attivita" scope="request" class="siap.siepe.attivita.model.AttivitaModel"/>
<jsp:useBean id="ufficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="TrasferisciAttivita">

 <table cellspacing="2" cellpadding="2" style="width: 90%;">
 <tr>
       <td class="l">Ufficio di Destinazione</td>
       <td class="l"><font class="campo"><%=ufficioDestinatario.getDescrTipoUfficio() + " " +  ufficioDestinatario.getDescrComune()%></font></td>
   </tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.attivita.action.ActTrasferisciAttivita" >
  <input type="HIDDEN" name="<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>" value="<%=attivita.getIdAttivita()%>" >
  <input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=ufficioDestinatario.getCodUfficio()%>" >

  </form>