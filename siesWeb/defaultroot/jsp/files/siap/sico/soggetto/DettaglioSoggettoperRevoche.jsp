<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<%
  SoggettoModel soggetto = fascicolo.getSoggetto();
  String lDataNascita = "DataNascita";
  String DataNascita = DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy");
%>

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto : </font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>&<%=ICostantiSoggetto.CAMPO_COGNOME%>=<%=soggetto.getCognome()%>&<%=ICostantiSoggetto.CAMPO_NOME%>=<%=soggetto.getNome()%>&<%=lDataNascita%>=<%=DataNascita%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>
      </td>
    </tr>
    
   <tr>
      <td class="L" width=100%>    
<%
        if (soggetto.getSesso().compareTo("F")==0)
        {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }
        else
        {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }

if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {%>
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%}else
   {%>
      <font class="campo">***</font>&nbsp;
<%}}else{%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%}%>
      <font class="label">in : </font>
      <font class="campo">

 <%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
       <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
<%
      }
%>

      </font>
     </td>
    </tr>
  </table>