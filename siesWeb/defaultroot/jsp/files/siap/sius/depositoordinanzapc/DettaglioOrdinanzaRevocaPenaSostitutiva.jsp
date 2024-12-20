<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel"%>


<%@ page import="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"%>
<%@ page import="siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel"%>

<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>

<jsp:useBean id="esecSansSostModel" scope="request" class="siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel" />
<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<%
DepositoOrdinanzaPcModel lDepositoOrd = datiOrdinanza.getOrdinanza();
%>

<table cellspacing="2" cellpadding="2" width="90%" >
  <tr>
    <td class="L">
      <font class="label">Eventuale Motivazione</font>
    </td>
    <td class="L">
      <font class="campo"> <%=StringUtils.toStringJSP(lDepositoOrd.getUlterioreDescrizione(),"&nbsp;") %> </font>&nbsp;&nbsp;
    </td>    
  </tr>
</table>

<br>

<%
if (esecSansSostModel.getIdEsecuzioneSanzioneSost()!=null)
{
  
%>
    <table cellspacing="2" cellpadding="2" width="90%" >
      <tr>
        <td class="L">
          <font class="label">Quantum pena revocata</font>
        </td>
        <td class="L">
          <font class="label">&nbsp;Anni: </font>
          <font class="campo"> <%=StringUtils.toStringJSP(lDepositoOrd.getNumAnniArrestoRev()) %> </font>&nbsp;&nbsp;
          <font class="label">&nbsp;Mesi: </font>
          <font class="campo"> <%=StringUtils.toStringJSP(lDepositoOrd.getNumMesiArrestoRev()) %> </font>&nbsp;&nbsp;
          <font class="label">&nbsp;Giorni: </font>
          <font class="campo"> <%=StringUtils.toStringJSP(lDepositoOrd.getNumGiorniArrestoRev()) %> </font>&nbsp;&nbsp;
        </td>
      </tr>
    
      <tr>
        <td class="L">
          <font class="label">Data Decorrenza Revoca</font>
        </td>  
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lDepositoOrd.getDataDecorrenza(),"dd/MM/yyyy"),"&nbsp;")%></font>
        </td>
      </tr>
    
      <tr>
        <td class="L">  
          <font class="label" >Pena Sostitutiva piu' grave</font>&nbsp;
        </td>  
        <td class="L">          
          <font class="campo"> <%=StringUtils.toStringJSP(esecSansSostModel.getDescrTipoSanzione()) %> </font>&nbsp;&nbsp;
        </td>
      </tr> 

      <tr>
        <td class="L">
          <font class="label">Rideterminazione Quantum Pena Da Espiare</font>&nbsp;&nbsp;
        </td>
        <td class="L">
          <font class="label">&nbsp;Anni: </font>
          <font class="campo"> <%=StringUtils.toStringJSP(esecSansSostModel.getNumAnniSanzione()) %> </font>&nbsp;&nbsp;
          <font class="label">&nbsp;Mesi: </font>
          <font class="campo"> <%=StringUtils.toStringJSP(esecSansSostModel.getNumMesiSanzione()) %> </font>&nbsp;&nbsp;
          <font class="label">&nbsp;Giorni: </font>
          <font class="campo"> <%=StringUtils.toStringJSP(esecSansSostModel.getNumGiorniSanzione()) %> </font>&nbsp;&nbsp;
        </td>
      </tr>
    </table>
<% } %>    
<br>