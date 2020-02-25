<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.regesentenza.model.RegeSentenzaModel"%>
<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.regesies.regesoggetto.action.ICostantiRegeSoggetto"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="provvedimentoRege"  scope="session" class="siap.regesies.regesentenza.model.ProvvedimentoModel"/>
<%
String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
String largh = "8%";
String resto = "92%";
%>

<html>
<%RegeSentenzaModel lSentenza = provvedimentoRege.getRegeSentenza(); %>

<table>
	<tr>
	    <td class="LBGISI" width=<%=largh%> valign="middle" >
		   	<% if(lSentenza.getCodTipoProvvedimento().equals("01")){%>
		      	<font class="campo">Sentenza ReGe</font>
		    <%}else{%>
		       	<font class="campo">Decreto ReGe</font>
		    <%}%>
	 	</td>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
		<%--<td width=<%=resto%>></td></tr><><tr> --%>
		<td>
<table width="100%"><tr><td class="L" colspan=2>
        <font class="label">N. </font>
        <a class="cliccabile" title="Dettaglio Provvedimento" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regesentenza.action.ActDettaglioProvvedimento&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=provvedimentoRege.getRegeSoggetto().getIdFile()%>">
          <%=lSentenza.getAnnoSentenza()%>/<%=lSentenza.getNumeroSentenza()%>
        </a>&nbsp;
       <font  class="label" > del </font>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
   <font class="label" > emessa da </font>
         <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaEmittente())%></font><font class="label" > di </font><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrLuogoEmittente())%></font>&nbsp;
    </td></tr>
 <tr>
   <td class="L" colspan=2>
    <font class="label">Data Arrivo Atto</font>
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"dd-MM-yyyy"),"-")%></font>&nbsp;

      <font class="label">Data Iscrizione</font>
               <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataIscrizione(),"dd-MM-yyyy"),"-")%></font>&nbsp;

      <font class="label">Data Irrevocabilità</font>
         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font>&nbsp;
      <font class="label">Note</font>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font>&nbsp;
      </td>
    </tr>
   </table>
  </td>
  </tr>
</table>
<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
<tr>
  <td class="LBGISI" width=<%=largh%> valign="middle">
     <font class="campo">Soggetto</font>
     </td>
     <td>
     <table width="100%"><tr>
     <td class="L" colspan=2><font class="label">Cognome Nome :</font>
     	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <font class="campo">
        <a class="cliccabile" title="Dettaglio Soggetto" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regesoggetto.action.ActDettaglioRegeSoggetto&<%=ICostantiRegeSoggetto.CAMPO_ID_FILE%>=<%=provvedimentoRege.getRegeSoggetto().getIdFile()%>">
          <%=provvedimentoRege.getRegeSoggetto().getCognome()%>&nbsp;<%=provvedimentoRege.getRegeSoggetto().getNome()%>
        </a>
      </font>
<%    if (provvedimentoRege.getRegeSoggetto().getSesso().compareTo("F")==0)
       {%>  <font class="label">&nbsp; nata il </font>
<%     } else        {
%>          <font class="label">&nbsp; nato il </font>
<%     }%>
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimentoRege.getRegeSoggetto().getDataNascita(),"dd-MM-yyyy"),"-")%></font>
       <font class="label">&nbsp; in  </font>
<%   if (provvedimentoRege.getRegeSoggetto().getDescrComuneNascita().compareTo("-")==0)
       {%>
         <font class="campo"><%=provvedimentoRege.getRegeSoggetto().getDescrStatoNascita()%> </font>
<%     }else{
%>      <font class="campo"><%=provvedimentoRege.getRegeSoggetto().getDescrComuneNascita() + "  ("+provvedimentoRege.getRegeSoggetto().getCodProvinciaNascita()+")" %></font>
<%     }%>
      </td>
    </tr>
    </table>
    </td>
    </tr>
    </table>
  <br>
</html>