<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel" />


<jsp:useBean id="residenze" scope="request" class="java.util.Vector" />

<%
  boolean lFlagPopUp = false;
  if( (request.getParameter("PopUp") != null)&& (request.getParameter("PopUp").equals("Y")) )
  {
    lFlagPopUp = true;
  }
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Domicilio</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
<%
    if ( lFlagPopUp )
    {
%>
    <script language="JavaScript">
      function insertIT(id, indirizzo, cap, comune, comestero, stato)
      {
         window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>.value=id;
        if(indirizzo!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_INDIRIZZO%>.value=indirizzo;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_INDIRIZZO%>.value="";

        if(cap!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_CAP%>.value=cap;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_CAP%>.value="";

       if (comune!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>.value=comune;
       else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>.value="";

       if (stato!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_COD_STATO%>.value=stato;
       else
         window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_COD_STATO%>.value="";

       if (comestero!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.value=comestero;
       else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.value="";

        window.parent.close();

        window.parent.close();
      }
  	</script>
<%
    }
%>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Elenco Domicilio</font>
      </td>
    </tr>
  </table>
  <br>
<%
    if (!lFlagPopUp)
    {
%>
   		<jsp:include page="/jsp/files/siap/sico/soggetto/DettaglioSoggettoAssociato.jsp"/>
<%
    }
%>
  <br>
<%
  BigDecimal lIdSoggetto = ((ResidenzaModel)residenze.get(0)).getSogIdSoggetto();
%>
  <table>
    <tr>
      <td class="int">Indirizzo</td>
      <td class="int">CAP</td>
      <td class="int">Luogo</td>
      <td class="int">Comune Estero</td>
      <td class="int">Stato</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
  Iterator itx = residenze.iterator();
  while ( itx.hasNext())
  {
    ResidenzaModel residenza = (ResidenzaModel)itx.next();
%>

    <tr>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getIndirizzo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getCap(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescrComune(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescComuneEstero(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescrStato(),"-")%></td>
      <td class="c">
<%
        if(!lFlagPopUp)
        {
           String modificabile = "";
           //if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(soggetto.getCodUfficioInserimento()))
           if (UtenteConnesso.getUfficioUtente().isUfficioDiCompetenza(soggetto.getCodUfficioInserimento()))
              {modificabile = "SI";}
           else
              {modificabile = "NO";}
%>
          <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>" />
             <jsp:param name="ValoreIdEntita" value="<%=residenza.getIdResidenza()%>" />
             <jsp:param name="Modificabile" value="<%=modificabile%>" />
          </jsp:include>

        <%
        }
        else
        {
%>
          <a href="Javascript:insertIT('<%=residenza.getIdResidenza()%>','<%=StringUtils.cStrForJS(residenza.getIndirizzo())%>','<%=StringUtils.cStrForJS(residenza.getCap())%>','<%=StringUtils.cStrForJS(residenza.getDescrComune())%>','<%=StringUtils.cStrForJS(residenza.getDescComuneEstero())%>','<%=StringUtils.cStrForJS(residenza.getCodStato())%>');">
            <img align="middle" src="/images/fileselected.gif" border=0>
          </a>
<%
        }
%>
      </td>
    </tr>
<%
  }
%>
    </table>
  </form>
  </body>
</html>