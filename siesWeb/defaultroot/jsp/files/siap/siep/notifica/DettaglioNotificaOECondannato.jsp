<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<html>

<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<%--  <jsp:useBean id="scadenzario" scope="request" class="siap.siep.scadenzario.model.ScadenzarioModel"  /> --%>
<jsp:useBean id="notifica" scope="request" class="siap.siep.notifica.model.NotificaModel" />
<jsp:useBean id="autoritaEsternaDelegata" scope="request" class="java.lang.String"  />

<%
      NotificaModel lNotMod = new NotificaModel(notifica);
%>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<head>
<title>[S.I.E.S.] - Registrazione Data Notifica OE a Condannato</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<!--<script language="JavaScript" src="/html/conferma.js"></script>-->
<script language="JavaScript">
  var desktop;
  function ListaComuni(a_formname, a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

	function VerificaUno()
	{
	  if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value.length==1)
	    document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value;
	  if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value.length==1)
	    document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value;

	  var d1=document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.value;

    if (! ControllaData(d1))
    {
      alert('Data di Notifica non valida');
      return false;
    }

	   var data_emissione  =  document.DettaglioNotifica.dataemissione.value;
	   year=data_emissione.substring(0,4);
	   month=data_emissione.substring(5,7);
	   day=data_emissione.substring(8,10);
	   var MiadataEM = new Date(year, month-1, day);

	   day=d1.substring(0,2);
	   month=d1.substring(3,5);
	   year=d1.substring(6,10);
	   var MiadataNOT = new Date(year, month-1, day);

     if (MiadataNOT<MiadataEM)
	   {
	     alert('Data Notifica deve essere maggiore o uguale alla data di emissione del provvedimento');
	     return false;
	   }

     document.DettaglioNotifica.submit();
	}
</script>
</head>
<%
	boolean vedosubmit = false;
%>

<body class="corpo" >

  <form name="DettaglioNotifica" method="POST" action="/jsp/Main.jsp">

	<table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%
	        if(lNotMod.getDataAvvenutaNotifica() != null)
	        { 
%>
          	<font class="campo">Dettaglio Avvenuta Notifica al Condannato</font>
<%
          }
	        else
          {
%>
            <font class="campo">Inserimento Avvenuta Notifica al Condannato</font>
<%
					}
%>
      </td>
      <td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
      </td>
     </tr>
   </table>
 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<%
	if( eventonotifica.getEvento() != null && eventonotifica.getEvento().getIdEvento() != null )
	{
%>
	  <table cellspacing=0 cellpadding=0 width=95%>
	    <tr>
	    <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
	      <td class="L">
	        <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%>&nbsp;<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%> emesso in data <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
	      </td>
	    </tr>
	  </table>  
		<br>
<%
	}
%>
    <table cellspacing=2 cellpadding=2>
       <input type="hidden" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>" value="<%=lNotMod.getIdNotifica()%>">
       <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lNotMod.getEveIdEvento()%>">
<%
       //Controllo se Esiste la Data di Avvenuta Notifica
       if(lNotMod.getDataAvvenutaNotifica() != null)
       {
%>
				<tr>
					<td class="l">Data Notifica</td>
				 	<td class="l"><font class="campo"><%=DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(),"dd/MM/yyyy")%></font></td>
				</tr>
				<tr>
        
        	<td class="l">Autorità delegata alla notifica</td>
<%
          if(lNotMod.getAutoritaEsterna()!= null)
          {
%>
	          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font></td>
<%
          }
          else if(lNotMod.getIstitutoDetenzione()!= null)
          {
%>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrComune())%></font></td>
<%
          }

					if(lNotMod.getAutoritaEsternaDelegata() != null)
          {
%>
						<tr>
							<td class="l">Autorità che ha effettuato la notifica</td>
		       		<td class="l">
	          		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrTipoAutorita())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrSede())%></font>&nbsp;
			       	</td>
            </tr>							
<%                
          }
					
					if( 	 lNotMod.getAutoritaEsternaDelegata() != null
					    && lNotMod.getAutoritaEsternaDelegata().getDescrizione() != null
					    && !"".equals( lNotMod.getAutoritaEsternaDelegata().getDescrizione().trim()) )
          {
%>
						<tr>
							<td class="l">Indirizzo</td>
		       		<td class="l">
	          		<font class="campo"><%=StringUtils.toStringJSP(StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrizione()))%></font>
			       	</td>
	          </tr>							
<%                
          }
				}
        else //Non Esiste la Data di Avvenuta Notifica
        {
            vedosubmit = true;
%>
            <tr>
              <td class="l">Data Notifica</td>
              <td class="l">
              	<font class="campo">
			            <input type="text"   name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
			            -
			            <input type="text"  name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
			            -
			            <input type="text"  name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>"   maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		            </font>
		          </td>
          </tr>
          <tr>
            <td class="l">Autorità delegata alla notifica</td>
<%
						if(lNotMod.getAutoritaEsterna()!= null)
						{
%>
              <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font></td>
<%
            }
						else if(lNotMod.getIstitutoDetenzione()!= null)
            {
%>
              <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrComune())%></font></td>
<%
            }
%>
       			</tr>
       			<tr>
	            <td class="l">Autorità che ha effettuato la notifica</td>
				      <td class="l">
	         	    <select Title="Autorita Esterna Delegata"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
		       			  <%=autoritaEsternaDelegata%>
           			</select>
				      </td>
    				</tr>
   					<tr>
	            <td class="l">Sede</td>
	  					<td class="L">
	  				    <input title="Sede Autorita Esterna Delegata" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>"  maxlength="35" size="35">
	  					  <a href="Javascript:ListaComuni('DettaglioNotifica','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
	      			 	  <img src="/images/filefolder.gif" border=0>
	      			  </a>
	    				</td>
				      <td class="l">Indirizzo</td>
				      <td class="L">
				        <TEXTAREA title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>" cols=30 ></TEXTAREA>
				      </td>
	           </tr>
  				</table>
<%
				}	
          
  if(vedosubmit)
	{
%>
    <table cellspacing=2 cellpadding=2 width=95%>
     <tr>
      <td>
				<br>
        	<input type="hidden" name="flag" value="">
        	<input type="hidden" name="dataemissione" value="<%=eventonotifica.getEvento().getDataEmissione()%>">
        	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActAggiornaAvvenutaNotificaCondannato">
       		<input class="bottone" type="button" name="INSERISCI" value="Conferma" onClick="javascript:VerificaUno()">
      </td>
    </tr>
  </table>
<%
	}
%>
  </form>
</body>
</html>