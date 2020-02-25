<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="nuovaistanza" scope="request" class="siap.siep.nuovaistanza.model.NuovaIstanzaModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String"/>

<jsp:useBean id="magistrato" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="evento"     scope="request" class="siap.sico.evento.model.EventoModel"/>
<%
    String lAnnullato = "N";
    if(nuovaistanza != null && nuovaistanza.getCodStatoIstanza()!= null && nuovaistanza.getCodStatoIstanza().equals("08"))
     {
       lAnnullato = "S";
     }
%>
<html>
<head>

  <title> Dettaglio Inoltro Istanza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
// Richiamo della finestra di pop-up per inserire motivazione Annullamento
function conferma(a_action, idIstanza, eventodacanc, tipo)
 {
  if (window.confirm('Confermi la cancellazione ?'))
  {
    if (tipo=="A")
    {
       var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&IdEvento=" + eventodacanc +"&IdIstanza=" + idIstanza+ "&TipoOp=" + tipo +"&TipoProvvedimento=Inoltro", "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=450,height=200");
         window.parent.close();
    }else
    {
      str = "/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActCancellaInoltroNuovaIstanza&IdEvento="+eventodacanc+"&IdIstanza="+idIstanza+"&TipoOp="+tipo+"&TipoProvvedimento=Inoltro";
               window.location.href=str;
    }
  }
 }

</script>
</head>
<body class="corpo">
<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio inoltro Istanza al Pubblico Ministero</font>
     </td>
      <%
      // Visualizzo il tasto di stampa se l'evento non è validato
      if (evento.getFlagDocumentoRegistrato()==null
       ||(evento.getFlagDocumentoRegistrato()!=null
       && evento.getFlagDocumentoRegistrato().equals("N") ) )
      {
      %>
      <!-- BOTTONE DI STAMPA -->
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActStampaInoltroIstanza&IdEvento="+evento.getIdEvento()%>"/>
      </jsp:include>
      <%}%>

<%
    if (evento.getFlagDocumentoRegistrato() == null ||
        (evento.getFlagDocumentoRegistrato() != null && 
        		evento.getFlagDocumentoRegistrato().compareTo("N")==0))
    {%>
    <td class="LBG">
     	<a href="/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActUploadInoltroIstanza&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza&<%=ICostantiEvento.CAMPO_VALIDA%>=S&TipoVis=Inoltro">
      	<img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
     	</a>
    </td>

     <td class="LBG">
      <a href="Javascript:conferma('siap.siep.nuovaistanza.action.ActLoadCancellaInoltroNuovaIstanza','<%=nuovaistanza.getIdNuovaIstanza()%>','<%=evento.getIdEvento()%>','C');">
     	<img src="/images/delete24.gif" width="24" height="24" alt="Cancella" border="0">
      </a>
     </td>
<%
      }else {
    	  if (evento.getFlagDocumentoRegistrato().compareTo("A")!=0){
%>
     <td class="LBG">
      <a href="Javascript:conferma('siap.siep.nuovaistanza.action.ActLoadCancellaInoltroNuovaIstanza','<%=nuovaistanza.getIdNuovaIstanza()%>','<%=evento.getIdEvento()%>','A');">
     	<img src="/images/delete24.gif" width="24" height="24" alt="Cancella" border="0">
      </a>
     </td>

<%
    	  }
      }
	    if( request.getParameter("AzioneChiamante")==null ||
	    		request.getParameter("AzioneChiamante").equals("")){
%>
          <td class="LBG"> 
          <a href="javascript:history.back()">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
        <%} %>
    </tr>
  </table>
</FORM>

   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<table >
<%
//ISTANZA Pervenuta
if(nuovaistanza != null && "P".equals(nuovaistanza.getFlagPresdep())) 
{
%>
  <tr>
    <td class="titolo">Dati Dell'Istanza</td>
    <td class="titolo" align="center">Pervenuta</td>                          
   </tr>  
	<tr> 
		<td class="l">Data Atto</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataIstanza(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Autorità Mittente</td>
		<td class="l"><font class="campo"><%=nuovaistanza.getDescrAutoritaMittente()%></font> di <font class="campo"><%=nuovaistanza.getDescrSedeMittente()%></font></td>
	</tr>

<%}else if(nuovaistanza != null && "D".equals(nuovaistanza.getFlagPresdep()))  
{%>
  <tr>
    <td class="titolo">Dati Dell'Istanza</td>
    <td class="titolo" align="center">Depositata</td>                          
   </tr>  
  <tr>
    <td class="l">Depositata in data</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataIstanza(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Soggetto Presentante</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getSoggPresentanteIdentificato()) %></font>&nbsp;
    	Presentata da Avvocato
<%
  		if(nuovaistanza.getAvvocatoPresentante() != null)
  		{%>
  			<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
    		<font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getAvvocatoPresentante().getNome())%>&nbsp;<%=StringUtils.toStringJSP(nuovaistanza.getAvvocatoPresentante().getCognome()) %></font>&nbsp;
		<%}else{%>
   	&nbsp;
		<%}%>
   	</td>
  </tr>  
<%}%>
<%
		String NomeAvvocato="-", Foro="-",TipoDifensore="-",  Nominato="";
		if(nuovaistanza.getAvvocato()!=null)
		{
			NomeAvvocato = nuovaistanza.getAvvocato().getCognome()+" " +nuovaistanza.getAvvocato().getNome(); 
			Foro=nuovaistanza.getAvvocato().getForo();
			TipoDifensore=nuovaistanza.getAvvocato().getDescrTipo();
			Nominato = StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataIstanza(),"dd-MM-yyyy"));
		}
%>
	<tr><td class="l">Avvocato</td>	<td class="l"><font class="campo"><%=NomeAvvocato%></font> </td></tr>
	<tr><td class="l">Foro di Competenza</td><td class="l"><font class="campo"><%=Foro%></font></td></tr>
	<tr><td class="l">Tipo Difensore</td><td class="l"><font class="campo"><%=TipoDifensore%></font></td></tr>
	<tr><td class="l">Nominato in Data</td><td class="l"><font class="campo"><%=Nominato%>&nbsp;</font></td>
	</tr>
	<tr><td class="l">Oggetto Istanza</td>
			<td class="l"><font class="campo"><%=nuovaistanza.getDescrContenuto()%></font></td>
	</tr>
	<tr><td class="l">Note</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getNote())%>&nbsp;</font></td>
	</tr>

	<tr>
    	<td class="l">Data Inoltro al Pubblico Ministero&nbsp;</td>
    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataInoltroPM(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
   	</tr>
    <tr>  
      	<td class="l">Magistrato</td>
      	<td class="l">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
       </td>
    </tr>
</table>

 <div align=left style="visibility:hidden" id="upld">
         <FORM name="comandi" enctype="multipart/form-data" method="post">
             <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
             <tr><td class="L">
                 <input  class=bottone  type="submit" value="Conferma">
                 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.nuovaistanza.action.ActUploadInoltroIstanza">
                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= evento.getIdEvento() %>">
                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza&TipoVis=Inoltro">
              </td> </tr>
            </table>

          </FORM>
      </div>
    <br>

</body>
</html>