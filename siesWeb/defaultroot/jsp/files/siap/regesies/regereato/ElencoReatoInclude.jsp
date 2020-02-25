<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza" %>
<%@ page import="siap.regesies.regereato.model.RegeReatoModel" %>
<%@ page import="siap.regesies.regereato.model.RegeReatoCircostanzaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>


<jsp:useBean id="provvedimento" scope="request" class="siap.regesies.regesentenza.model.ProvvedimentoModel"/>



<html>
<table width="50%">
    <%
    Vector reati = provvedimento.getReati();
    if(reati!=null && reati.size()>0)
    {
    %>
 <tr>
      <td class="LBGISIV" width="10%"><font class="campoLow">Progr.</font>  </td>
      <td class="LBGISIV" width="70%"><font class="campoLow">Reato </font></td>
      <td class="LBGISIV" width="20%"><font class="campoLow">Nota Qgf</font> </td>
 </tr>
   <% Iterator lIterReati = reati.iterator();
   while(lIterReati.hasNext())
      {
          RegeReatoCircostanzaModel lReatoCircostanza = (RegeReatoCircostanzaModel)lIterReati.next();
          RegeReatoModel lReato = lReatoCircostanza.getReato();
          RegeReatoModel[] lCircostanze = lReatoCircostanza.getCircostanze();

          boolean lFlagAnnoNumero = false;
          if( lReato.getAnnoFonte() != 0
              &&  lReato.getNumeroFonte() != null &&
              !lReato.getNumeroFonte().equals(""))
          {
            lFlagAnnoNumero = true;
          }%>

            <tr>
              <td class="c" width="10%">
<%                 //REATO
                  if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
                  {%>
                    <font class="label">
<%                     out.println("" + lReato.getProgrNumeroManuale()+" ");
%>                  </font>
<%                }
                  else
                  {
                      out.println("" + lReato.getProgrReato()+" ");
                  }
                  %></font>
                  </td><td class="l">
          <font class="campo"><%

                  if(lFlagAnnoNumero)
                  {
                    if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                      out.println(lReato.getDescrFonte()+" ");
                    if(lReato.getAnnoFonte() != 0)
                      out.println(lReato.getAnnoFonte());
                    if(lReato.getNumeroFonte()!= null&&
                    !lReato.getNumeroFonte().equals(""))
                      out.println("/"+lReato.getNumeroFonte());
                  }

                  if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
                    out.println("art."+lReato.getArticolo());
                  if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
                    out.println(" "+lReato.getDescrSottonumerazione());

                  if(!lFlagAnnoNumero)
                  {
                    if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                      out.println(lReato.getDescrFonte());
                  }

                  if(lReato.getComma() != null && !lReato.getComma().equals(""))
                    out.println(" c. "+lReato.getComma());
                  if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
                    out.println(" l. "+lReato.getLettera());
                  if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
                    out.println(" n. "+lReato.getNumero());



                  //CIRCOSTANZE
                  if(lCircostanze != null)
                  {
                    RegeReatoModel lCirc = null;
                    for(int i=0; i<lCircostanze.length; i++)
                    {
                      lCirc = lCircostanze[i];
%>
                      ,
<%
                      boolean lFlagAnnoNumeroCirc = false;
                      if( lCirc.getAnnoFonte() != 0
                          && lCirc.getNumeroFonte() != null
                          && !lCirc.getNumeroFonte().equals("") )
                      {
                        lFlagAnnoNumeroCirc = true;
                      }
                      if(lFlagAnnoNumeroCirc)
                      {
                        if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                          out.println(lCirc.getDescrFonte()+" ");
                        if(lCirc.getAnnoFonte() != 0)
                          out.println(lCirc.getAnnoFonte());
                        if(lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
                          out.println("/"+lCirc.getNumeroFonte());
                      }

                      if(lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
                        out.println("art."+lCirc.getArticolo());
                      if(lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("") && !lCirc.getDescrSottonumerazione().equals("-"))
                        out.println(" "+lCirc.getDescrSottonumerazione());

                      if(!lFlagAnnoNumeroCirc)
                      {
                        if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                          out.println(lCirc.getDescrFonte());
                      }

                      if(lCirc.getComma() != null && !lCirc.getComma().equals(""))
                        out.println(" c. "+lCirc.getComma());
                      if(lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
                        out.println(" l. "+lCirc.getLettera());
                      if(lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
                        out.println(" n. "+lCirc.getNumero());
                    }
                  }
%>
                </font>
<%


 				if(lReato.getNote() != null && !lReato.getNote().equals(""))
        {
%>
        			<font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
<%
        }

    if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals(""))
    {
%>
        <font class="label">Luogo</font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
<%
    }
%>

              </td>
          <%
                  if(lReato.getNotaQgf()!= null && !lReato.getNotaQgf().equals(""))
                  {%>
                    <td class="L"> <font class="campo"> <%=lReato.getNotaQgf()%> </td>
                  <%}
                  else
                  {%><td></td>
                 <% }%>
            </tr>
<%
        }
    }
else
      {%>
       <tr>
        <td class="l">
       <font class="cGrigio">Nessun Reato per il provvedimento.</font>
      </td>
    </tr>
 <%}%>
        </table>
</html>