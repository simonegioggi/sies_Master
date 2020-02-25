<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.siep.alias.model.AliasModel"%>
<jsp:useBean id="aliasvect" scope="request" class="java.util.Vector"/>
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Alias </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - RicercaSoggetto</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Alias</font></td>
   </tr>
  </table>
  <table>
<br>
<tr>
   <td class="l"><font class="label">Cognome e Nome: </font>&nbsp;
     <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a> &nbsp;<font class="label">Sesso: </font>&nbsp;&nbsp;<font class="campo"><%=soggetto.getSesso()%>&nbsp;</font>
  &nbsp;<font  class="label">Data di nascita:</font>&nbsp;<font class="campo">
<%
        if(soggetto.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggetto.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggetto.getAnnoNascita())%>&nbsp;
<%
        }
%>
        </font>
        <font class="label">Presunta:</font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(soggetto.getDataNascitaPresunta())%></font>
      </td>
    </tr>
    <tr> <td class="l">
    <%        if( (soggetto.getDescrComuneNascita() != null)
              && (!(soggetto.getDescrComuneNascita().equals("")))
              && (!(soggetto.getDescrComuneNascita().equals("-"))) )
          {%>
     <font  class="label">Comune Nascita:</font>&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita() )%>
            (<%=soggetto.getCodProvinciaNascita()%>)
         &nbsp;
        </font>
        <%}%>
    <%if (soggetto.getDescComuneNascitaEstero()!=null &&soggetto.getDescComuneNascitaEstero()!="")
      {%>
      <font class="label">Comune Di Nascita Estero:</font>&nbsp;
     <font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font>
    <%}%>

	 <font class="label"> Nazionalità:</font>&nbsp;
       <font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrNazionalita())%>&nbsp;</font>
      <font  class="label">Stato Nascita:</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>&nbsp;</font>
    </td></tr>

     <%if ((soggetto.getPaternita()!=null && soggetto.getPaternita()!="")||
     (soggetto.getCognomeMadre()!=null &&soggetto.getCognomeMadre()!="")||
      (soggetto.getAttoNascita()!=null &&soggetto.getAttoNascita()!="")||
       (soggetto.getCodFiscale()!=null &&soggetto.getCodFiscale()!=""))
      {%>
    <tr>
     <%if (soggetto.getPaternita()!=null &&soggetto.getPaternita()!="")
      {%>
      <td class="l"><font class="label">Paternita:</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getPaternita())%>&nbsp;</font>
    <%} if (soggetto.getCognomeMadre()!=null &&soggetto.getCognomeMadre()!="")
      {%>
      <font class="label">Nome Madre:</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getNomeMadre())%>
      <%=StringUtils.toStringJSP(soggetto.getCognomeMadre())%>&nbsp;</font>&nbsp;
      <%} if (soggetto.getCodFiscale()!=null &&soggetto.getCodFiscale()!="")
      {%>
      <font class="label">Codice Fiscale:</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodFiscale())%>&nbsp;</font>
      <%} if (soggetto.getAttoNascita()!=null &&soggetto.getAttoNascita()!="")
      {%><font class="label">Atto Nascita:</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAttoNascita()) %>&nbsp;</font></td>
   <%}%>
      </tr>
    <%}%><%if ((soggetto.getCodCs()!=null && soggetto.getCodCs()!="")||
     (soggetto.getCodAfis()!=null &&soggetto.getCodAfis()!=""))
      {%>
    <tr><% if (soggetto.getCodCs()!=null &&soggetto.getCodCs()!="")
      {%>    <td class="l"><font class="label">Codice CS:</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodCs())%>&nbsp;</font>
      <%} if (soggetto.getCodAfis()!=null &&soggetto.getCodAfis()!="")
      {%> <font class="label">Codice AFIS:</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis() )%>&nbsp;</font></td>
       <%}%>
    </tr>
     <%}%>
<tr>
  <td>&nbsp;
  </td>
</tr>
</table>

<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA_ESITO%>"></jsp:include>
<br>
<table width="80%">
<tr><td class="Titolo" colspan=6> Alias </td></tr>
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
     </tr>
       <tr>
    <td class="l">

   <%if(lAliasMod.getDescComuneNascitaEstero()!= null && lAliasMod.getDescComuneNascitaEstero()!="" && !lAliasMod.getDescComuneNascitaEstero().equals("-"))
    {%>
    <font class="label">Comune Di Nascita Estero:</font>&nbsp;
    <font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font>
 <% }
    else
   {%>
       Comune di Nascita:&nbsp;            <font class="campo">
          <%=StringUtils.toStringJSP(lAliasMod.getDescrComuneNascita() )%>
<%        if( (lAliasMod.getDescrComuneNascita() != null)
              && (!(lAliasMod.getDescrComuneNascita().equals("")))
              && (!(lAliasMod.getDescrComuneNascita().equals("-"))) )
          {%>
            (<%=lAliasMod.getCodProvinciaNascita()%>)
<%        }
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
    <tr>
    <% if (lAliasMod.getPaternita()!=null &&lAliasMod.getPaternita()!="")
     {%> <td class="l">Paternità:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getPaternita())%></font>&nbsp;Codice Fiscale:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getCodFiscale())%></font></td>
     <%} if (lAliasMod.getCodCs()!=null &&lAliasMod.getCodCs()!="")
     {%>
     <td class="l">&nbsp;Cod CS:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getCodCs())%></font></td>
     <%} if (lAliasMod.getAttoNascita()!=null &&lAliasMod.getAttoNascita()!="")
     {%><td class="l">&nbsp; Atto di Nascita:&nbsp;<font class="campo"> <%=StringUtils.toStringJSP(lAliasMod.getAttoNascita())%></font></td>
     <%}if(lAliasMod.getCodAfis()!=null && lAliasMod.getCodAfis()!="")
    {%>
       <td class="l">Cod AFIS:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAliasMod.getCodAfis())%></font>&nbsp;</td>
   <%}%>
    </tr>
    <%}%>
    <tr>
      <td>&nbsp;
      </td>
    </tr>
<%
  }
%>
  </table>
  </form>
  </body>
</html>