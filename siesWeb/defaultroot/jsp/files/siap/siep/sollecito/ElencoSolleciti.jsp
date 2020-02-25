<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato" %>
<%@ page import="siap.sius.documentoallegato.model.DocumentoAllegatoModel" %>

<jsp:useBean id="allegati"        scope="request" class="java.util.Vector"/>
<jsp:useBean id="eventoNotifica"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<%
BigDecimal idNotifica = (BigDecimal) request.getAttribute("idNotifica");
%>
 <script language="JavaScript">
    function stampa()
    {
       var  link = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sollecito.action.ActInserisciSollecito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventoNotifica.getEvento().getIdEvento()%>";
       window.location.href=link;
       return link;
    }
 </script>

<html>
<head>
  <title>[S.I.E.S.] - Elenco Solleciti</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <table>
    <tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Solleciti</font>
      	</td>
        <td class="LBG">
          <a href="Javascript:stampa()" onclick="javascript:lookUpload();">
            <img  align="middle" src="/images/print24.gif" alt="Stampa Sollecito" width="24" height="24" border="0">
          </a>
        </td>
    </tr>
   </table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />
<br>
  <table cellspacing="2" cellpadding="2">
    <tr>
      	<td class="l">Tipo Atto</td>
	  	<td colspan="2">
        <font class="campo"><%=eventoNotifica.getEvento().getDescrMotivo()%></font>
      	</td>
	</tr>
	<tr>
	  	<td class="l">Data Richiesta</td>
      	<td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoNotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      	</td>
    </tr>
	<tr>
		<td class="titolo" colspan="3">Notifica</td>
	</tr> 
  <%
    int lSizeNotifiche = eventoNotifica.getNotifiche().length;
    for(int count = 0;count < lSizeNotifiche; count++)
    {
          if (eventoNotifica.getNotifiche()[count].getIdNotifica().compareTo(idNotifica)==0)
            {
            %>
              <tr>
				<td class="L">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getUfficio().getDescrTipoUfficio()%></font>&nbsp;
                </td>
                <td class="l">Luogo </td>
                <td class="L">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getUfficio().getDescrComune()%></font>&nbsp;
                </td>
              </tr>
            <%

            }else if (eventoNotifica.getNotifiche()[count].getAutoritaEsterna() != null )
                    {
                    %>
                      <tr>
                        <td class="L">
                          <font class="campo"><%=eventoNotifica.getNotifiche()[count].getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp;
                        </td>
                        <td class="l">Luogo </td>
                        <td class="L">
                          <font class="campo"><%=eventoNotifica.getNotifiche()[count].getAutoritaEsterna().getDescrSede()%></font>&nbsp;
                        </td>
                      </tr>
                    <%
                    }else if (eventoNotifica.getNotifiche()[count].getCSSA() != null )
                            {
                            %>
                              <tr>
                                <td class="L">
                                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getCSSA().getTipo()%></font>&nbsp;
                                </td>
                                <td class="l">Luogo </td>
                                <td class="L">
                                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getCSSA().getComune()%></font>&nbsp;
                                </td>
                              </tr>
                            <%
                            }
%>
   </table>
<br>
   <table>
     <tr>
	  <td class="LBG">Stampa Sollecito</td>
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sollecito.action.ActStampaSollecito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventoNotifica.getEvento().getIdEvento()%>" onclick="javascript:lookUpload();">
          <img  align="middle" src="/images/print24.gif" alt="Stampa Documento Istruttorio" width="24" height="24" border="0">
        </a>
      </td>
     </tr>
     </table>
<% }%>


<%
  if ( allegati.size() == 0 )
  {
%>
	<table>
    	<tr>
        	<td class="LBG">
          		<font class="label"> Non ci sono solleciti per questo atto. </font>
        	</td>
		</tr>
	</table>
<%
  } else
  {
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaDocumentiAllegati">
  <table width="90%">
  <div align=center>
    <tr>
      <td class="int" width=30%>Data emissione</td>
      <td class="int" width=50%>Tipo documento</td>
      <td class="int" width=20%>Azioni</td>
    </tr>
  </div>
<%
    Iterator itx = allegati.iterator();
    while ( itx.hasNext())
    {
      DocumentoAllegatoModel lDoc = (DocumentoAllegatoModel)itx.next();
%>
    <tr>
      <td class="l">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lDoc.getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      <td class="l" ><%=StringUtils.toStringJSP(lDoc.getDescrTipoDocumento(),"-")%></td>
<%    String isBlob = "SI";   // flag BLOB pieno
      if (lDoc.getFlagDocumentoRegistrato()!=null)
      {
        if (lDoc.getFlagDocumentoRegistrato().compareTo("S")==0)
        {
%>
<%       }
      }
      else
      {
        isBlob="NO";
      }
%>
       <td class="l" >
        <jsp:include page="<%=ICostantiDocumentoAllegato.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lDoc.getIdDocumentoAllegato()%>" />
          <jsp:param name="CodTipoProvvedimento" value="<%=lDoc.getCodTipoDocumento()%>" />
          <jsp:param name="Stampa" value="<%=isBlob%>" />
        </jsp:include>
       </td>
    </tr>
  <%
   } // endwhile
  %>
  </table>
  </FORM>
<%
  }  // endif provvedimenti.size()
%>

</body>
</html>