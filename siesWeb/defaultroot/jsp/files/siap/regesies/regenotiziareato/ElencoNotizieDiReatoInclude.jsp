<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza" %>
<%@ page import="siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>


<jsp:useBean id="provvedimento" scope="request" class="siap.regesies.regesentenza.model.ProvvedimentoModel"/>


<html>
<%    Vector lNotizie = provvedimento.getNotizieDiReato();
      if(lNotizie != null && lNotizie.size() != 0)
      {%>
        <table cellspacing=1 cellpadding=1   width="50%">
        <tr>
      <td class="LBGISIV" width="10%"><font class="campoLow">Progr.</font>  </td>
      <td class="LBGISIV" width="70%"><font class="campoLow">Fonte </font></td>
      <td class="LBGISIV" width="20%"><font class="campoLow">Data </font> </td>
 </tr>
        <%Iterator lIterNot = lNotizie.iterator();
        while(lIterNot.hasNext())
        {
          RegeNotiziaReatoModel lNotizia = (RegeNotiziaReatoModel)lIterNot.next();
%>        <tr>
          <td class="c" width="10%">
             <font  class="label"><%=lNotizia.getProgrNotizia()%></font>
            </td>
              <td class="l">
                <%
               if(lNotizia.getDescrizioneFonte() != null && !lNotizia.getDescrizioneFonte().equals("") && !lNotizia.getDescrizioneFonte().equals("-"))
                 {%>
                  <font class="campo">
                      <%=lNotizia.getDescrizioneFonte()%> </font><font class="label"> di </font>
                    <font class="campo">  <%=lNotizia.getDescrComuneFonte()%>
                  </font>
               <%}
               else{
                   if(lNotizia.getDescrComuneFonte()!=null && !lNotizia.getDescrComuneFonte().equals("-"))
                   {
                   %> <font class="campo"><%=lNotizia.getDescrComuneFonte()%></font>

                   <%
                   }else{%>-<%}}%>
               </td>
               <td class="l"><%
               if(lNotizia.getDataPervenimento() !=null )
                 {%><font class="label">&nbsp;&nbsp;</font>
                  <font class="campo">
                      <%=DateUtils.getDateToString(lNotizia.getDataPervenimento(),"dd-MM-yyyy")%>
                 </font>
                 <%}%>
              </td>
            </tr>
<%    }
      }
      else
      {%>
       <table cellspacing=0 cellpadding=0 width="50%">
       <tr>
        <td class="l">
           <font class="lGrigio">Nessuna Notizie di Reato per il provvedimento.</font>
        </td>
    </tr>
 <%}%>
 </table>
</html>