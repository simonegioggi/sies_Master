<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.posizionemateriale.model.PosizioneMaterialeModel"%>
<%@ page import="siap.siep.posizionemateriale.action.ICostantiPosizioneMateriale"%>

<jsp:useBean id="elenco" scope="request" class="java.util.Vector"/>
<jsp:useBean id="descUfficioRicerca" scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroTipoRicerca" scope="request" class="java.lang.String"/>

<%
	String lDesrFiltroTipoRicerca = "";
	if( "0".equals(filtroTipoRicerca) )
	{
	  lDesrFiltroTipoRicerca = "Visualizza Solo Posizioni Materiali Valide"; 
	}
	else if( "1".equals(filtroTipoRicerca) )
	{
	  lDesrFiltroTipoRicerca = "Visualizza Solo Posizioni Materiali Non Valide";
	}
	else
	{
	  lDesrFiltroTipoRicerca = "Visualizza Tutte le Posizioni Materiali";
	}
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Posizioni Materiale</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font> <font class=campo> Ricerca Posizione Materiale</font> </td>
      </tr>
    </table>
    <br>
    <table cellspacing=2 cellpadding=2>
      <tr>
      	<td class="lVerdeNB"><%= descUfficioRicerca %></td>
      </tr>
      <tr><td>&nbsp;</td><tr>
      <tr>
        <td class="lVerdeNB">Parametri di ricerca: <%= lDesrFiltroTipoRicerca %></td>
      </tr>
    </table>
    <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  <table>
    <tr>
      <td class="int" width=10%>Codice</td>
      <td class="int" width=30%>Descrizione</td>
      <td class="int" width=30%>Data Fine Validità</td>
      <td class="int" width=10%>Azioni</td>
    </tr>
<%
  Iterator itx = elenco.iterator();
  while ( itx.hasNext())
  {
    PosizioneMaterialeModel lPosMat = (PosizioneMaterialeModel)itx.next();
%>
    <tr>
      <td class=c><%=lPosMat.getCodPosizioneMateriale()%></td>
      <td class=l><%=lPosMat.getDescPosizioneMateriale()%></td>
      <td class=c><%=StringUtils.toStringJSP( DateUtils.getDateToString(lPosMat.getDataFineValidita(), "dd-MM-yyyy" ) )%>&nbsp;</td>

      <td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiPosizioneMateriale.CAMPO_COD_POSIZIONE_MATERIALE%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=lPosMat.getCodPosizioneMateriale()%>" />
           <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiPosizioneMateriale.CAMPO_COD_UFFICIO%>"/>
           <jsp:param name="ValoreIdEntitaProvv" value="<%=lPosMat.getCodUfficio()%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  <br>
  </body>
</html>