<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza" %>
<%@ page import="siap.regesies.regecircostanza.model.RegeCircostanzaModel" %>
<%@ page import="siap.regesies.regereato.model.RegeReatoCircostanzaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>


<jsp:useBean id="provvedimento" scope="request" class="siap.regesies.regesentenza.model.ProvvedimentoModel"/>


<html>
<%
      Vector lCircostanze = provvedimento.getCircostanze();
      if(lCircostanze != null && lCircostanze.size() != 0)
      {%>
        <table cellspacing=1 cellpadding=1 width="50%">

           <tr>
      <td class="LBGISIV" width="10%"><font class="campoLow">Progr.</font>  </td>
      <td class="LBGISIV" width="90%"><font class="campoLow">Circostanza </font></td>
 </tr>
        <%
        Iterator lIterCircostanze = lCircostanze.iterator();
        while(lIterCircostanze.hasNext())
        {
          RegeCircostanzaModel lCircostanza = (RegeCircostanzaModel)lIterCircostanze.next();
          boolean lFlagAnnoNumero = false;
          if( lCircostanza.getAnnoFonte() != 0
              && lCircostanza.getNumeroFonte() != null
              && !lCircostanza.getNumeroFonte().equals("") )
          {
            lFlagAnnoNumero = true;
          }
%>
            <tr>
            <td class="c"><%=lCircostanza.getProgrCircostanza()%></td>
             <td class="l">      <font class="campo">
<%
                  //REATO
                  if(lFlagAnnoNumero)
                  {
                    if(lCircostanza.getDescrFonte() != null && !lCircostanza.getDescrFonte().equals("") && !lCircostanza.getDescrFonte().equals("-"))
                      out.println(lCircostanza.getDescrFonte()+" ");
                    if(lCircostanza.getAnnoFonte() != 0)
                      out.println(lCircostanza.getAnnoFonte());
                    if(lCircostanza.getNumeroFonte() != null && !lCircostanza.getNumeroFonte().equals(""))
                      out.println("/"+lCircostanza.getNumeroFonte());
                  }

                  if(lCircostanza.getArticolo() != null && !lCircostanza.getArticolo().equals(""))
                    out.println("art."+lCircostanza.getArticolo());
                  if(lCircostanza.getDescrSottonumerazione() != null && !lCircostanza.getDescrSottonumerazione().equals("") && !lCircostanza.getDescrSottonumerazione().equals("-"))
                    out.println(" "+lCircostanza.getDescrSottonumerazione());

                  if(!lFlagAnnoNumero)
                  {
                    if(lCircostanza.getDescrFonte() != null && !lCircostanza.getDescrFonte().equals("") && !lCircostanza.getDescrFonte().equals("-"))
                      out.println(lCircostanza.getDescrFonte());
                  }

                  if(lCircostanza.getComma() != null && !lCircostanza.getComma().equals(""))
                    out.println(" c. "+lCircostanza.getComma());
                  if(lCircostanza.getLettera() != null && !lCircostanza.getLettera().equals(""))
                    out.println(" l. "+lCircostanza.getLettera());
                  if(lCircostanza.getNumero() != null && !lCircostanza.getNumero().equals(""))
                    out.println(" n. "+lCircostanza.getNumero());
%>
                </font>
              </td>

            </tr>
<%        }
      }
else
      {%>
       <table cellspacing=1 cellpadding=1 width="50%" >
       <tr>
        <td class="l">
       <font class="cGrigio">Nessuna Circostanza per il provvedimento.</font>
      </td>
    </tr>
 <%}%>        </table>
</html>