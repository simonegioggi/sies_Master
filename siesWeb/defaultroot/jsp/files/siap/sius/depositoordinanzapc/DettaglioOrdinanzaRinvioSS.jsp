<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<table cellspacing="4" cellpadding="4">
   <tr>
  	<td class="l">Ulteriore descrizione della decisione </td>
  	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
   </tr>
   
   <tr><td><br></td></tr>
 
 	<!-- [ Che sta a fare questa Roba ? Chiedere a Umberto ] -->
    <tr>
        <td class="Titolo" colspan=6> Concessione Rinvio Esecuzione della Pena <td>
    </tr>
    <!--  <tr> <td> <br></td></tr> -->
     <tr>
      <td class="l">Data Inizio Periodo </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(),"dd/MM/yyyy"), "-")%></font></td>
    </tr>

     <tr>
      <td class="l">Data Termine Periodo </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(),"dd/MM/yyyy"), "-")%></font></td>
    </tr>
   <tr>
      <td class="l">Durata sospensione  (AA-MM-GG)</td>
      <td class="l">
      <font class="campo">
         <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getSospensioneAASS())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getSospensioneMMSS())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getSospensioneGGSS())%>
      </font>
       </td>
    </tr>
    <tr> <td> <br></td></tr>

</table>

