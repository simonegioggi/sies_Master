<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.alias.model.AliasModel"%>

<jsp:useBean id="aliasvect" scope="request" class="java.util.Vector" />



<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Alias</title>

  </head>

  <body class="corpo">
      <table width= "100%">
          <tr><td class="Titolo"> Alias </td></tr>
              <%
              Iterator itx = aliasvect.iterator();
              while ( itx.hasNext())
              {
                  AliasModel lAliasMod = (AliasModel)itx.next();
              %>
                   <tr>
                       <td class="l">Cognome e Nome:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getCognome())%>&nbsp; <%=StringUtils.toStringJSP(lAliasMod.getNome())%></font>
                       &nbsp; Data di Nascita:&nbsp;<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAliasMod.getDataNascita(),"dd-MM-yyyy"))%></font>
                       &nbsp;Sesso: &nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getSesso())%></font>
                       </td>
                   </tr>
                   <tr>
                       <td class="l">

                       <%if(lAliasMod.getDescComuneNascitaEstero()!= null && lAliasMod.getDescComuneNascitaEstero()!="" && !lAliasMod.getDescComuneNascitaEstero().equals("-"))
                       {%>
                           <font class="label">Comune Di Nascita Estero:</font>&nbsp;
                           <font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font>
                       <%
                       }
                       else
                       {%>
                           Comune di Nascita:&nbsp;<font class="campo">
                           <%=StringUtils.toStringJSP(lAliasMod.getDescrComuneNascita() )%>
                           <%
                           if( (lAliasMod.getDescrComuneNascita() != null)
                             && (!(lAliasMod.getDescrComuneNascita().equals("")))
                             && (!(lAliasMod.getDescrComuneNascita().equals("-"))) )
                           {%>
                            (<%=lAliasMod.getCodProvinciaNascita()%>)
                           <%
                           }
                       }%>
                        &nbsp;
                       </font>
                       <%if((lAliasMod.getDescrStatoNascita()!=null)&&(lAliasMod.getDescrStatoNascita()!=null))
                       {%>
	                       <font  class="label">Stato Nascita:</font>&nbsp;
                           <font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getDescrStatoNascita())%>&nbsp;</font>
                       <%}%>
                       </td>
                   </tr>

                   <% if ((lAliasMod.getPaternita()!=null &&lAliasMod.getPaternita()!="")||
                         (lAliasMod.getCodCs()!=null &&lAliasMod.getCodCs()!="")||
                         (lAliasMod.getAttoNascita()!=null &&lAliasMod.getAttoNascita()!=""))
                   {%>
                       <tr><td class="l">
                           <% if (lAliasMod.getPaternita()!=null &&lAliasMod.getPaternita()!="")
                           {%>
                               Paternità:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getPaternita())%></font>&nbsp;Codice Fiscale:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getCodFiscale())%></font>
                           <%}
                           if (lAliasMod.getCodCs()!=null &&lAliasMod.getCodCs()!="")
                           {%>
                               &nbsp;Cod CS:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getCodCs())%></font>
                           <%}
                           if (lAliasMod.getAttoNascita()!=null &&lAliasMod.getAttoNascita()!="")
                           {%>
                               &nbsp; Atto di Nascita:&nbsp;<font class="campo"> <%=StringUtils.toStringJSP(lAliasMod.getAttoNascita())%></font>
                           <%}
                           if(lAliasMod.getCodAfis()!=null && lAliasMod.getCodAfis()!="")
                           {%>
                               Cod AFIS:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getCodAfis())%></font>
                           <%}%>
                           </td>
                       </tr>
                   <%}%>
                   <tr>
                       <td>&nbsp;</td>
                   </tr>
              <%
              }
              %>

      </table>
      <table width=100%>
          <tr>
              <td valign=bottom class=c><input style=bottone value="Chiudi" type=button onclick="Javascript:window.close()"></td>
          </tr>
      </table>
  </body>
</html>
