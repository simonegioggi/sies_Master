<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione" %>

<html>

<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="notifica" scope="request" class="java.util.Vector"  />
<jsp:useBean id="autoritaEsternaDelegata" scope="request" class="java.util.ArrayList"  />

<%
	List lNot = new ArrayList(notifica);
	EventoNotificaModel lEveMod = new EventoNotificaModel(eventonotifica);
%>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<head>
<title>[S.I.E.S.] - Modifica Avvenuta Notifica al Difensore Decreto Irreperibilità</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">

  var desktop;
  function ListaComuni(a_formname, a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

	function Verifica()
	{
  	var lLungN = document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.length;

  	for(var x=0; x<lLungN; x++)
		{
      if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[x].value.length==1)
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[x].value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[x].value;
      
      if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[x].value.length==1)
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[x].value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[x].value;

      var d1=document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[x].value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[x].value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[x].value;
      
      if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[x].checked)
      {
        if (! ControllaData(d1))
        {
          alert('Data di Notifica non valida');
          return false;
        }
        
	      if ( 
	       	   (     document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[x].value=='-'
	              && (   trimStringa(document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[x].value) != ''  
	              		&& document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[x].value!='-' ) )
	            ||
	            (    document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[x].value!='-'
	              && ( trimStringa(document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[x].value) == ''
	              		|| document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[x].value=='-' ) )
	          )
	      {
          alert('Indicare autorità delegata valida');
          
          return false;
        }
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

   	
	   	for(var valorizzati=0; valorizzati<document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.length; valorizzati++)
	    {
	      if( document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[valorizzati].checked == false)
	      {
					// Alle notifiche non selezionate vengono aggiornati i campi a "vuoti"
					document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[valorizzati].value='';
					document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[valorizzati].value='';
					document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[valorizzati].value='';
		
		      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[valorizzati].value='-';
					   
		      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[valorizzati].value='';	
	      }
	    }
		}
   	
   	var cFlag=0;

   	for(var tot2=0;tot2<document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.length;tot2++)
    {
      if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[tot2].checked)
      {
      	cFlag++;
      }
    }

    document.DettaglioNotifica.INSERISCI.disabled = true;
    document.DettaglioNotifica.submit();
	}

	function VerificaUno()
	{
   	if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value.length==1)
     	document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value;
   	
   	if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value.length==1)
     document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value;

  	var d1=document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.value;
   	if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked)
   	{
    	if (! ControllaData(d1))
     	{
        alert('Data di Notifica non valida');
        return false;
      }

      if ( 
       	   (     document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value=='-'
              && (   trimStringa(document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value) != ''  
              		&& document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value!='-' ) )
            ||
            (    document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value!='-'
              && ( trimStringa(document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value) == ''
              		|| document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value=='-' ) )
          )
      {
	      alert('Indicare autorità delegata valida');

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
   	}
    
    if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked)
    {
    	document.DettaglioNotifica.flag.value=1;
    } 
    else
    {
    	document.DettaglioNotifica.flag.value=0;
		}

    document.DettaglioNotifica.INSERISCI.disabled = true;
    document.DettaglioNotifica.submit();
	}

<%
		// Conserva i valori precedenti nel caso deselezione chekbox per ripristinarli
%>
		var ggData = new Array();
		var mmData = new Array();
		var aaData = new Array();
		var tipoAut = new Array();
		var sedeAut = new Array();
		var indirizzo = new Array();
<%		
		for(int i=0; i<lNot.size(); i++)
    {
      NotificaModel lNotMod = new NotificaModel();
      lNotMod = (NotificaModel)lNot.get(i);
%>
			ggData[<%=i%>] = '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(), "dd") )%>';
			mmData[<%=i%>] = '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(), "MM") )%>';
			aaData[<%=i%>] = '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(), "yyyy") )%>';
			      	 
			tipoAut[<%=i%>] = '<%=(lNotMod.getAutoritaEsternaDelegata()!= null ) ? lNotMod.getAutoritaEsternaDelegata().getCodTipoAutorita() : "-"%>';
			
			sedeAut[<%=i%>] = '<%=(lNotMod.getAutoritaEsternaDelegata()!= null ) ? lNotMod.getAutoritaEsternaDelegata().getDescrSede() : ""%>';
			indirizzo[<%=i%>] = '<%=( (lNotMod.getAutoritaEsternaDelegata() != null) && (lNotMod.getAutoritaEsternaDelegata().getDescrizione() != null) ) ? lNotMod.getAutoritaEsternaDelegata().getDescrizione(): ""%>';
<%
    }
%>

 	function Abilita(id)
  {
    if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[id].checked)
    {
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[id].disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[id].disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[id].disabled=false;
      //document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>[id].disabled=false;
      
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[id].disabled=false;
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[id].disabled=false;
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>[id].disabled=false;

      var node = document.getElementById('divAvv'+id);
      node.style.display='inline';
    }
    else
    {
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[id].disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[id].disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[id].disabled=true;
      //document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>[id].disabled=true;
      
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[id].disabled=true;
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[id].disabled=true;
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>[id].disabled=true;

			// Ripristina i valori precedenti nel caso di deselezione chekbox
			document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[id].value=ggData[id];
			document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[id].value=mmData[id];
			document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[id].value=aaData[id];

      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[id].value=tipoAut[id];			   
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[id].value=sedeAut[id];
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>[id].value=indirizzo[id];

      var node = document.getElementById('divAvv'+id);
      node.style.display='none';
    }
	}
	
	function AbilitaUno()
  {
    if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked)
    {
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
      //document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>.disabled=false;

      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>.disabled=false;

      var node = document.getElementById('divAvv');
      node.style.display='inline';
    }
    else
    {
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.disabled=true;
      //document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>.disabled=true;
      
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=true;
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=true;
      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>.disabled=true;

			// Ripristina i valori precedenti nel caso di deselezione chekbox
<%
      NotificaModel lNotModUno = new NotificaModel();
      lNotModUno = (NotificaModel)lNot.get(0);
%>
 	    var ggData = '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotModUno.getDataAvvenutaNotifica(), "dd") )%>';
	    var mmData = '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotModUno.getDataAvvenutaNotifica(), "MM") )%>';
	    var aaData = '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotModUno.getDataAvvenutaNotifica(), "yyyy") )%>';
	         	 
    	document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value=ggData;
    	document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value=mmData;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.value=aaData;

	    var tipoAut = '<%=(lNotModUno.getAutoritaEsternaDelegata()!= null ) ? lNotModUno.getAutoritaEsternaDelegata().getCodTipoAutorita() : "-"%>';
    	document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value=tipoAut;
	   
	    var sedeAut = '<%=(lNotModUno.getAutoritaEsternaDelegata()!= null ) ? lNotModUno.getAutoritaEsternaDelegata().getDescrSede() : ""%>';
    	document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value=sedeAut;

	    var indirizzo = '<%=( (lNotModUno.getAutoritaEsternaDelegata() != null) && (lNotModUno.getAutoritaEsternaDelegata().getDescrizione() != null) ) ? lNotModUno.getAutoritaEsternaDelegata().getDescrizione() : ""%>';
    	document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>.value=indirizzo;

      var node = document.getElementById('divAvv');
      node.style.display='none';
    }
	}

<%
	int lLungNot = lNot.size();
	int contaabilita = lLungNot;
  int totabilita = 0;
%>

	function ControlloCheck()
	{
<%
		if(lLungNot == 1)
		{
%>
     	document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked=true;
			AbilitaUno();
<%	  
		}
		else
		{
      for(int i=0; i<lLungNot; i++)
      {
         NotificaModel lNotMod = new NotificaModel();
         lNotMod = (NotificaModel)lNot.get(i);
         if(lNotMod != null && lNotMod.getDataAvvenutaNotifica() != null)
         {
%>
     				document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[<%=i%>].checked=true;
     				Abilita(<%=i%>);
<%	  		  
         }
       }
		}
%>
	}
</script>
</head>
	<body class="corpo"  onLoad="javascript:ControlloCheck()">
  <form name="DettaglioNotifica" method="POST" action="/jsp/Main.jsp">
		<table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
	      <font class="campo">Modifica Avvenuta Notifica al Difensore Decreto Irreperibilità</font>
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
<%
       for(int i=0; i<lLungNot; i++)
       {
          NotificaModel lNotMod = new NotificaModel();
          lNotMod = (NotificaModel)lNot.get(i);
%>
	       <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEveMod.getNotifiche()[i].getEveIdEvento()%>">
	       <input type="hidden" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>" value="<%=lNotMod.getIdNotifica()%>">

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

          if( contaabilita >1 ) //Esiste piu' di una check box
          {
              if(lNotMod.getAvvIdAvvocatoFascicoloSiep()!=null)
              {
                if(lNotMod.getAvvSiep() != null)
                {
%>
                  <tr>
                    <td class="l">Avvocato</td>
                    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getCognome() +" "+StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getNome()))%></font>&nbsp;
                     Foro di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getForo())%></font>&nbsp;
                    </td>
                  </tr>
                  <tr>
                    <td class="l">Tipo Difensore</td>
                    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getDescrTipo())%></font>&nbsp;
                    </td>
				            <td class="l">
				             	<input type="checkbox" onclick="Javascript:Abilita('<%=totabilita%>');" name="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>" value="<%=lNotMod.getIdNotifica()%>" >
				            </td>
				            <td class="l">Data Notifica</td>
				            <td class="l">
				              <font class="campo">
						            <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(), "dd") )%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
						            -
						            <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(), "MM") )%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
						            -
						            <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(), "yyyy") )%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
				            	</font>
				            </td>
                  </tr>
<%
               }
             }
%>
		           <tr>
		             <td class="l">Autorità che ha effettuato la notifica</td>
					       <td class="l">
            			 <select disabled Title="Autorità che ha effettuato la notifica"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
<%
					            Iterator lIter = autoritaEsternaDelegata.iterator();
					            while(lIter.hasNext())
					            {
              					DecodificheModel lDecMod = (DecodificheModel)lIter.next();
%>
                				<option value="<%=lDecMod.getCode()%>" <%=(lNotMod.getAutoritaEsternaDelegata()!= null && lNotMod.getAutoritaEsternaDelegata().getCodTipoAutorita().equals(lDecMod.getCode())) ? "selected" : ""%>/><%=lDecMod.getDescription()%>
<%
					            }
%>
             			 </select>
					       </td>
					     </tr>
					     <tr>				       
		             <td class="l">Sede</td>
     						 <td class="L">
        				   	 <input disabled title="Sede Autorità che ha effettuato la notifica" value="<%= lNotMod.getAutoritaEsternaDelegata()!= null ? StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrSede()) : ""%>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>"  maxlength="35" size="35">
									   <div id="divAvv<%=totabilita %>" style="display:none; width:100%;">
    							     <a href="Javascript:ListaComuni('DettaglioNotifica','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=totabilita%>]');">
          				 	     <img src="/images/filefolder.gif" border=0>
        					     </a>
        					 </div>
      					 </td>
					       <td class="l">Indirizzo</td>
					       <td class="L" colspan="2">
<%
									if( 	 lNotMod.getAutoritaEsternaDelegata() != null
									    && lNotMod.getAutoritaEsternaDelegata().getDescrizione() != null
									    && !"".equals( lNotMod.getAutoritaEsternaDelegata().getDescrizione().trim()) )
				          {
%>
	          				<TEXTAREA disabled title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>" cols=30><%=StringUtils.toStringJSP(StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrizione()))%></TEXTAREA>
<%
				          }
									else
									{
%>
					          <TEXTAREA disabled title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>" cols=30></TEXTAREA>
<%
									}
%>
					       </td>
		           </tr>
<% 
							 totabilita++;
             }
             else //Esiste  una check box
             {
               if(lNotMod.getAvvIdAvvocatoFascicoloSiep()!=null)
               {
                 if(lNotMod.getAvvSiep() !=null)
                 {
%>
	                 <tr>
	                    <td class="l">Avvocato</td>
	                     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getCognome() +" "+StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getNome()))%></font>&nbsp;
	                     Foro di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getForo())%></font>&nbsp;
	                  </td>
	                 </tr>
	                 <tr>
	                 	 <td class="l">Tipo Difensore</td>
	                   <td class="l">
	                   	 <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getDescrTipo())%></font>&nbsp;
	                   </td>
					           <td class="l">
					             <input type="checkbox" onclick="Javascript:AbilitaUno();" name="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>" value="<%=lNotMod.getIdNotifica()%>" >
					           </td>
					           <td class="l">Data Notifica</td>
					           <td class="l">
					           	 <font class="campo">
					           	   <input type="text" disabled  name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(), "dd") )%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
					               -
					               <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(), "MM") )%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
					               -
					               <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(), "yyyy") )%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
					             </font>
					           </td>
	                 </tr>
<%
                 }
               }
%>
			         <tr>
		             <td class="l">Autorità che ha effettuato la notifica</td>
					       <td class="l">
            			 <select disabled Title="Autorità che ha effettuato la notifica"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
<%
					            Iterator lIter = autoritaEsternaDelegata.iterator();
					            while(lIter.hasNext())
					            {
              					DecodificheModel lDecMod = (DecodificheModel)lIter.next();
%>
                				<option value="<%=lDecMod.getCode()%>" <%=(lNotMod.getAutoritaEsternaDelegata()!= null && lNotMod.getAutoritaEsternaDelegata().getCodTipoAutorita().equals(lDecMod.getCode())) ? "selected" : ""%>/><%=lDecMod.getDescription()%>
<%
					            }
%>
             			 </select>
					       </td>
					     </tr>
					     <tr>				       
		             <td class="l">Sede</td>
     						 <td class="L">
        				   	 <input disabled title="Sede Autorità che ha effettuato la notifica" value="<%= lNotMod.getAutoritaEsternaDelegata()!= null ? StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrSede()) : ""%>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>"  maxlength="35" size="35">
									   <div id="divAvv" style="display:none; width:100%;">
    							     <a href="Javascript:ListaComuni('DettaglioNotifica','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
          				 	     <img src="/images/filefolder.gif" border=0>
        					     </a>
        					 </div>
      					 </td>
					       <td class="l">Indirizzo</td>
					       <td class="L" colspan="2">
<%
									if( 	 lNotMod.getAutoritaEsternaDelegata() != null
									    && lNotMod.getAutoritaEsternaDelegata().getDescrizione() != null
									    && !"".equals( lNotMod.getAutoritaEsternaDelegata().getDescrizione().trim()) )
				          {
%>
	          				<TEXTAREA disabled title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>" cols=30><%=StringUtils.toStringJSP(StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrizione()))%></TEXTAREA>
<%
				          }
									else
									{
%>
					          <TEXTAREA disabled title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>" cols=30></TEXTAREA>
<%
									}
%>
					       </td>
		           </tr>			           
<%
          	}
%>
        <tr>
        	<td>&nbsp;</td>
        </tr>
<%
		} /*---------FINE FOR */ 

      if(contaabilita > 1)
      {
%>
		     <tr>
		       <td>
		         <input type="hidden" name="flag" value="">
		         <input type="hidden" name="dataemissione" value="<%=eventonotifica.getEvento().getDataEmissione()%>">
		         <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActAggiornaAvvenutaNotificaDifensoreIrreperibilita">
		         <input class="bottone" type="button" name="INSERISCI" value="Conferma" onClick="javascript:Verifica()">
		       </td>
		     </tr>
<%
      }
      else
      {
%>
		     <tr>
		      <td>
		        <input type="hidden" name="flag" value="">
		        <input type="hidden" name="dataemissione" value="<%=eventonotifica.getEvento().getDataEmissione()%>">
		        <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActAggiornaAvvenutaNotificaDifensoreIrreperibilita">
		       <input class="bottone" type="button" name="INSERISCI" value="Conferma" onClick="javascript:VerificaUno()">
		      </td>
		    </tr>
<%
      }
%>
  </table>
  </form>
</body>
</html>