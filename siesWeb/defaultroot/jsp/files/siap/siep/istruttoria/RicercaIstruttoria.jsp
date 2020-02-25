<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="eventi" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Istruttoria </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
		<script language="JavaScript">
			function conferma(a_action, a_parameter, a_entityname,a_parameter2, a_entityname2, valoreordinamento)
			{
			  var documentoRegistrato = a_entityname2;
			  if (window.confirm('Confermi la cancellazione ?'))
			  {
			    if ( documentoRegistrato == "S" )
			    {
			       var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&" + a_parameter + "=" +a_entityname + "&nextAction=siap.siep.istruttoria.action.ActRicercaRichiesteIstruttoria", "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
			       window.parent.close();
			    }
			    else
			    {
			      str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter + "=" + a_entityname + "&nextAction=siap.siep.istruttoria.action.ActRicercaRichiesteIstruttoria";
			      window.location.href=str;
			    }
			  }
			}
			 
			function chiama(idEvento)
			{
				window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento&IdEvento="+idEvento,"Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
			}
		
			function pr()
			{
			   sign = prompt("Confermi la cancellazione?");
			}	
		</script>

</head>

 <body class="corpo">
	 <table>
	    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	      <td class="LBG">
	      <font class="label">Funzione : Ricerca Istruttoria</font>&nbsp;&nbsp;
	     </td>
	    </tr>
	  </table>
	  <br>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	  <br>
	  <br>
	  <div align=center>
	  <table cellspacing=2 cellpadding=2>
	    <tr>
	      <td class="int">Data Emissione</td>
	      <td class="int">Descrizione Istruttoria</td>
	      <td class="int">Documento<br>Validato</td>
	 	  <td class="int">Azioni</td>
	    </tr>
<%
	  Iterator itx = eventi.iterator();
	  while ( itx.hasNext())
	  {
	    EventoModel lEvento = (EventoModel)itx.next();
%>
	    <tr>
	      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%></td>
	      <td class="l"><%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%></td>
	      <td class="c">&nbsp;
<%
		      if (lEvento.getFlagDocumentoRegistrato()!=null)
		      {
		        if (lEvento.getFlagDocumentoRegistrato().compareTo("S")==0)
		        {
%>
		          <img src="/images/TickRed.gif">
<%
        		}
        		else if(lEvento.getFlagDocumentoRegistrato().compareTo("A")==0)
        		{
%>
	             <a class="cliccabile" href="javascript:chiama('<%=lEvento.getIdEvento()%>');" title="ANNULLAMENTO">
	             	<font class="cRosso">ANNULLATO</font>
	             </a>
<%
         	}
      	}
%>
				</td>
	      <td class=c>	
<%
				String isBlob="SI";
				if(lEvento.getFlagDocumentoRegistrato() == null)
        {
          isBlob = "NO";
        }
        
        //Flag Documento Registrato
        String lDocReg = "N";
        if(  lEvento.getFlagDocumentoRegistrato() != null
          && lEvento.getFlagDocumentoRegistrato().equals("S"))
        {
          lDocReg = "S";
        }
      	
        if(  lEvento.getFlagDocumentoRegistrato() != null
          && lEvento.getFlagDocumentoRegistrato().equals("A"))
        {
          lDocReg = "A";
        }
      	
      	if(  lEvento.getCodOperatoreInserimento() != null
          && lEvento.getCodOperatoreInserimento().startsWith("res-"))
        {
          lDocReg = "M";
        }

 				String modificabile = "";
   			if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))
      	{
   				modificabile = "SI";
   			}
   			else
      	{
   			  modificabile = "NO";
   			}
%>
        <jsp:include page="<%=ICostantiIstruttoria.PG_BUTTONS_RICERCA%>">
          <jsp:param name="CampoIdEntita"  value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEvento.getIdEvento()%>" />
          <jsp:param name="CampoIdEntitaProvv" value="campo" />
          <jsp:param name="ValoreIdEntitaProvv" value="<%=lEvento.getFlagDocumentoRegistrato()%>" />
          <jsp:param name="TipoProvvedimento" value="<%=lEvento.getCodTipoProvvedimento()%>" />
          <jsp:param name="MotivoEvento" value="<%=lEvento.getCodMotivo()%>" />
          <jsp:param name="TipoEvento" value="<%=lEvento.getCodTipoEvento()%>" />
          <jsp:param name="TemIdTemplate" value="<%=lEvento.getTemIdTemplate()%>" />
          <jsp:param name="modalita" value="R" />
          <jsp:param name="docRegistrato" value="<%=lDocReg%>" />
          <jsp:param name="Evento" value="<%=isBlob%>" />
          <jsp:param name="Modificabile" value="<%=modificabile%>" />
        </jsp:include>
      </td>
    </tr>    
<%
 	 	}
%>
    </table>
  </div>
	</body>
</html>