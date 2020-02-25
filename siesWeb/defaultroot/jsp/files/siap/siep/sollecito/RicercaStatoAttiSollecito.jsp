<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.sico.cssa.model.CSSAModel"%>

<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>

<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%@ page import="siap.siep.sollecito.action.ICostantiSollecito"%>
<%@ page import="siap.siep.sollecito.action.ActRicercaStatoAttiSollecito"%>

<jsp:useBean id="atti" scope="request" class="java.util.Vector"/>
<html>
<head>
	<title>[S.I.E.S.] - Ricerca Stato Atti Sollecito</title>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	
	<script language="JavaScript" src="/html/conferma.js"></script>
  	<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
	
<%
int i = 0;
%>
	
	<script language="JavaScript">

  	var lung;   // numero di campi d'aggiornamento.

  	function inizializza()
  	{
    if (typeof(document.ListaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.length) == "undefined")
      lung = 0;
    else
      lung = document.ListaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.length;
   	//alert("lung : " +lung);
  	return;
  	}

  	function aggiornaID(i, ind, data)
  	{
    if (lung == 0)
    {
      document.ListaAtti.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>.value = ind;
    }
    else
    {
      document.ListaAtti.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>[i].value = ind;
    }
    data_invio[i] = data;
  	}

  	function ControlliDate(DataIni,data_to_verify)
  	{
    var ritorno = true;

    //alert("data1: " + DataIni);
    //alert("data2: " + data_to_verify);

    if (data_to_verify.length > 2 && ControllaData(DataIni))
    {
      if (!ControllaData(data_to_verify))
      {
        alert('Data scorretta: '+ data_to_verify);
        ritorno = false;
      }
      else if (! CompareDate(DataIni, data_to_verify))
      {
          alert('Data di Restituzione -' +data_to_verify  +'- non può precedere quella di richiesta -' + DataIni + '-.');
          ritorno = false;
      }
    }
    return ritorno;
  	}
	function  Verify()
  	{
    var ritorno = true;
    var data_ricezione;
    var i;

    if (lung == 0)
    {
      data_ricezione = document.ListaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.ListaAtti.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.ListaAtti.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.value;
      ritorno = ControlliDate(data_invio[0],data_ricezione);
    }
    else
    {
      for(i = 0; ritorno &&(i < lung); i++)
      {
        data_ricezione = document.ListaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[i].value+'/'+document.ListaAtti.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[i].value+'/'+document.ListaAtti.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[i].value;
        ritorno = ControlliDate(data_invio[i],data_ricezione);
      }
    }
    return ritorno;
  	}
 	</script>

</head>

<body class="corpo">
<div>
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
		<td class="LBG"><font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Ricerca Stato Atti/Sollecito</font></td>
	</tr>
</table>


<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />
</div>
<jsp:include page="/jsp/files/siap/siep/sollecito/paginazioneRicercaSollecito.jsp" />
<div>
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
<table width="100%">
	<tr>
      <td class="int" width="30%">Tipo atto istruttorio richiesto</td>
      <td class="int" width="30%">Destinatario</td>
      <td class="int" width="10%">Data richiesta</td>
      <td class="int" width="23%">Data restituzione</td>
	  <td class="int" width="7%">Azioni</td>
    </tr>
	<% EventoNotificaModel atto=new EventoNotificaModel();
			for (int y=0;y<atti.size();y++)
			{
   				atto = (EventoNotificaModel)atti.get(y);
				NotificaModel notifica=new NotificaModel();
				for (int x=0; x<(atto.getNotifiche()).length; x++)
				{
					notifica = atto.getNotifiche()[x];
					if (notifica!=null) {%>
		
	<tr>
		<td class="l"><%=atto.getEvento().getDescrMotivo()%></td>
						<%	if (notifica.getAutoritaEsterna()!=null) {
								if (notifica.getAvvSiep()!=null  && notifica.getAvvSiep().getAvvocato()!=null) {%>
		<td class="c"><%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;<%=notifica.getAutoritaEsterna().getDescrSede()%><br><%=notifica.getAvvSiep().getAvvocato().getNome()%>&nbsp;<%=notifica.getAvvSiep().getAvvocato().getCognome()%>&nbsp;DEL FORO DI&nbsp;<%=notifica.getAvvSiep().getAvvocato().getForo()%></td>			
								<% }
								else {%>
		<td class="c"><%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;<%=notifica.getAutoritaEsterna().getDescrSede()%></td>
								<%}
							} 
							if (notifica.getUfficio()!=null) {%>
		<td class="c"><%=notifica.getUfficio().getDescrTipoUfficio()%>&nbsp;DI&nbsp;<%=notifica.getUfficio().getDescrComune()%></td>
							<% }
							if (notifica.getCSSA()!=null) {%>
		<td class="c"><%=notifica.getCSSA().getTipo()%>&nbsp;DI&nbsp;<%=notifica.getCSSA().getComune()%></td>
							<% } 
							if (notifica.getAutoritaEsterna()==null && notifica.getUfficio()==null && notifica.getCSSA()==null) {%>
		<td class="c">-</td>
							<% }%>
		<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(atto.getEvento().getDataEmissione(),"dd-MM-yyyy"),"-")%></td>
		<td class="c">
					<input onchange="Javascript:aggiornaID('<%=i%>','<%="" + notifica.getIdNotifica()%>','<%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%>');"
						value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"dd"))%>" type="text" maxlength="2" size="2"
						name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" onBlur="javascript:value=FillDM(value);">
        			-
        			<input onchange="Javascript:aggiornaID('<%=i%>','<%="" + notifica.getIdNotifica()%>','<%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%>');"
        				value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"MM"))%>" type="text" maxlength="2" size="2"
        				name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>" onBlur="javascript:value=FillDM(value);">
        			-
        			<input onchange="Javascript:aggiornaID('<%=i%>','<%="" + notifica.getIdNotifica()%>','<%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%>');"
        				value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"yyyy"))%>" type="text" maxlength="4" size="4"
        				name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>">
					<%
      // endif modalita
      String isBlob = "SI"; if(notifica.getFlagDocRegistrato() == null){isBlob="NO";}
      String isSolleciti = "SI"; // if(atto.getSollecito() = null){isAllegato="NO";}
%>
		<input  value="" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>" type = "HIDDEN">
		</td>
    <td class="l" width="7%">
        <jsp:include page="<%=ICostantiSollecito.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=atto.getEvento().getIdEvento()%>" />
          <jsp:param name="IdNotifica" value="<%=notifica.getIdNotifica()%>" />
          <jsp:param name="Solleciti" value="<%=isSolleciti%>" />
          <jsp:param name="Stampa" value="<%=isBlob%>" />
    </jsp:include>
       </td>
	</tr>
			<%	} i++;
		} 
	}%>
</table>
<table>
	<tr>
 		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sollecito.action.ActAggiornaDateNotifica" >
        <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
		</td>
	</tr>
</table>
</form>    
</div>

</body>
	<script language="JavaScript">
    var data_invio = new Array(<%=i%>); // array contenente le date di invio necessarie per il controllo
    for (i=0; i < <%=i%>; i++)
      data_invio[i] = "";
  </script>
</html>