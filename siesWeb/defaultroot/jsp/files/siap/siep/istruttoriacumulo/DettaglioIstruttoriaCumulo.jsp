<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<table cellspacing="0" cellpadding="0" width="95%" align="center">
  <tr><td width="90%" class="Titolo">Istruttoria</td></tr>

  <tr>
    <td class="L">
      <font class="label">Istruttoria N. </font>
      <font class="campo">
        <a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>" title="Istruttoria">
        <%=IstruttoriaCumulo.getAnnoProtocollo()%>
        /
        <%=IstruttoriaCumulo.getNumProtocollo()%></a>
      </font>
      <font class="label">&nbsp;Del </font>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(IstruttoriaCumulo.getDataApertura(),"dd-MM-yyyy"))%></font>
     
      <% if (IstruttoriaCumulo.getFlagStato().equals("A")) 
        { %>
            <font class="label">&nbsp;Stato Istruttoria:<font>
            <font class="label" style="color: green;">&nbsp;APERTA<font class="label">
      <% } else if (IstruttoriaCumulo.getFlagStato().equals("C")) 
            {%>
              <font class="label" style="color: red;">&nbsp;Chiusa</font><font class="label" >&nbsp;il <font>
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(IstruttoriaCumulo.getDataChiusura(),"dd-MM-yyyy"))%></font>
      <%    } else if (IstruttoriaCumulo.getFlagStato().equals("N")) 
              {%>
                <font class="label" style="color: red;">&nbsp;Annullata</font><font class="label">&nbsp;il</font>
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(IstruttoriaCumulo.getDataChiusura(),"dd-MM-yyyy"))%></font>
                <font class="label">&nbsp;Note</font>      
                <font class="campo"><%=StringUtils.toStringJSP(IstruttoriaCumulo.getNote(),"&nbsp;")%></font>
      <%      } %>
      
      <font class="label">&nbsp;&nbsp;
        <a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>" title="Lista Titoli Coinvolti">
       (Elenco Titoli Coinvolti)
       </a>
       &nbsp;&nbsp;-&nbsp;&nbsp;
       <a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadInserisciDatiFinaliCumulo&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>" title="Dati FInali Cumulo">
       (Dati Finali Cumulo)
       </a>
      </font>
    </td>
  </tr>
</table>  