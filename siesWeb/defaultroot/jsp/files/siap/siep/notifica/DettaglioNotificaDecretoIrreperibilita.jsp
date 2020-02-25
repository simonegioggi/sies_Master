<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%@ page import="siap.sico.evento.model.EventoNotificaModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica" %>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione" %>

<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"  />
<jsp:useBean id="notifica" scope="request" class="java.util.Vector"  />
<jsp:useBean id="autoritaEsternaDelegata" scope="request" class="java.lang.String"  />

<%
	List lNot = new ArrayList(notifica);
	EventoNotificaModel lEveMod = new EventoNotificaModel(eventonotifica);
%>
<html>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

	<head>
		<title>[S.I.E.S.] - Registrazione Notifica Decreto di Irreperibilità </title>
		
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
						
			  for(var x=0;x<lLungN;x++)
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

		    document.DettaglioNotifica.flag.value=cFlag;
    		
    		if (cFlag>0)
    		{
       		document.DettaglioNotifica.submit();
    		} 
    		else
    		{
      		alert ("Selezionare almeno un elemento");
    		}
			}
		</script>
		<script language="JavaScript">
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
		   	}
		    
		    if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked)
		    {
		    	document.DettaglioNotifica.flag.value=1;
		    } 
		    else
		    {
		    	document.DettaglioNotifica.flag.value=0;
				}
		   	
		   	if (document.DettaglioNotifica.flag.value=="0")
		   	{ 
		   		alert ("Selezionare almeno un elemento");
		   	} 
		   	else
		   	{
		     	document.DettaglioNotifica.submit();
		    }
			}

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
		      
		      // Sbianca i campi delle notifiche non selezionate
		      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[id].value='';
		      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[id].value='';
		      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[id].value='';
		
		      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[id].value='-';
		      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[id].value='';
		      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>[id].value='';
		
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
		
		      // Sbianca i campi delle notifiche non selezionate
		      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value='';
		      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value='';
		      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.value='';  
		      
		      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value='-';
		      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value='';
		      document.DettaglioNotifica.<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>.value='';
		
		      var node = document.getElementById('divAvv');
		      node.style.display='none';
	    	}
			}
	
			function ControlloCheck()
			{
	  		var check=document.getElementById('<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>');
	  		if (check != null)
	  		{
	  			if (document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.length>1)
	  			{
	    			for(var totcheck=0;totcheck<document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.length;totcheck++)
	    			{
	      			if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[totcheck].checked )
	      			{
	        			document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[totcheck].disabled=false;
	        			document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[totcheck].disabled=false;
	        			document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[totcheck].disabled=false;
	        			//document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>[totcheck].disabled=false;
	      			}
	    			}
	  			}
	  			else if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked )
	      	{
	        	document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
	        	document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
	        	document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
	        	//document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>.disabled=false;
	        }
	  		}
			}
	</script>
	</head>
<%  
		//commento int lLungNot = lEveMod.getNotifiche().length;
		int lLungNot = lNot.size();
	  boolean vedosubmit=false;
	  int contaabilita = 0;
	  int totabilita = 0;
	  int PosAvvocato = 0;
%>

<body class="corpo"  onLoad="javascript:ControlloCheck()">

  <form name="DettaglioNotifica" method="POST" action="/jsp/Main.jsp">
	<table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%  
       		 int conta=0;
           for(int i=0;i<lLungNot;i++)
           {
              NotificaModel lNotMod = new NotificaModel();
              lNotMod = (NotificaModel)lNot.get(i);
              //if(lEveMod.getNotifiche()[i].getDataAvvenutaNotifica() == null)
              if(lNotMod != null && lNotMod.getDataAvvenutaNotifica() == null)
              {
                conta+=1;
              }
            }

            if(conta==0)
            { 
%>
            	<font class="campo">Dettaglio Registrazione Notifica Decreto di Irreperibilità </font>
<%
            }
            else
            {
%>
             	<font class="campo">Inserimento Registrazione Notifica Decreto di Irreperibilità </font>
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
<%  
			for(int i=0;i<lLungNot;i++)
      {
        NotificaModel lNotMod = new NotificaModel();
        lNotMod = (NotificaModel)lNot.get(i);
        if(lNotMod != null && lNotMod.getDataAvvenutaNotifica() == null)
        {
          contaabilita++;
        }
      }

			for(int i=0; i<lLungNot; i++)
      {
%>
	      <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEveMod.getNotifiche()[i].getEveIdEvento()%>">
<%
        //Controllo se Esiste la Data di Avvenuta Notifica
        NotificaModel lNotMod = new NotificaModel();
        lNotMod = (NotificaModel)lNot.get(i);
        if(lNotMod != null && lNotMod.getDataAvvenutaNotifica() != null)
        { 
%>
					<tr>
					<td class="l">Autorità delegata alla notifica</td>				
<%
						if(lNotMod.getSogIdSoggetto() !=null)
						{
			        if(fascicolo.getSoggetto() != null)
			        {
%>
								<td class="l"><font class="campo"><%=fascicolo.getSoggetto().getCognome()%></font>&nbsp;<font class="campo"><%=fascicolo.getSoggetto().getNome()%></font></td>
								</tr>
<%
			        }
			     }
				   else if(lNotMod.getUfficio()!= null)
				   {
%>
 						 <td class="l"><font class="campo"><%=lNotMod.getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=lNotMod.getUfficio().getDescrComune()%></font></td>
	 					 </tr>
<%    
					 }
				   else if(lNotMod.getAutoritaEsterna()!= null)
				   {
%>
			       <td class="l"><font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp; di &nbsp; <font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrSede()%></font></td>
			       </tr>
<%
				   }

           if(lNotMod.getAvvIdAvvocatoFascicoloSiep() != null)
           {
             if(lNotMod.getAvvSiep()!= null)
             {
%>
								<tr>
						     <td class="l">Notifica al difensore</td>
                 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getCognome() +" "+StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getNome()))%></font>&nbsp;
                     Foro di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getForo())%></font>&nbsp;
                 </td>
               </tr>
               <tr>
	               <td class="l">Tipo Difensore</td>
	               <td class="l">
	               	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getDescrTipo())%></font>&nbsp;
	               </td>
               </tr>
						      </td>
								</tr>
<%
              //    PosAvvocato++;
            //  }
            }
           }
%>
            <tr>
              <td class="l">Data Notifica</td>
              <td class="l"><font class="campo"><%=DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(),"dd/MM/yyyy")%></font></td>
      
             </tr>
<%
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
           if(contaabilita >1) //Esiste piu' di una check box
           {
             if(lNotMod.getAvvIdAvvocatoFascicoloSiep()!=null)
             {
%>			       
               <tr>
			           <td class="l">Destinatario</td>			
<%
							     if(lNotMod.getSogIdSoggetto() !=null)
							     {
							        if(fascicolo.getSoggetto() != null)
							        {
%>
								         <td class="l"><font class="campo"><%=fascicolo.getSoggetto().getCognome()%></font>&nbsp;<font class="campo"><%=fascicolo.getSoggetto().getNome()%></font></td>
								         </tr>
<%
							        }
							     }
			   					 else if(lNotMod.getUfficio()!= null)
			        		 {
%>
						          <td class="l"><font class="campo"><%=lNotMod.getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=lNotMod.getUfficio().getDescrComune()%></font></td>
						          </tr>
<%    
									 }
			  					 else if(lNotMod.getAutoritaEsterna()!= null)
			       			 {
%>
						        <td class="l"><font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp; di &nbsp; <font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrSede()%></font></td>
						        </tr>
<%
			  			      }
			   
				            if(lNotMod.getAvvSiep() != null)
				           {
				            //if( lEveMod.getAvvocati()[PosAvvocato] !=null)
				            // {%>
				               <tr>
				                    <td class="l">Avvocato</td>
				                     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getCognome() +" "+StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getNome()))%></font>&nbsp;
				                     Foro di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getForo())%></font>&nbsp;
				                  </td>
				               </tr>
				               <tr>
				                 <td class="l">Tipo Difensore</td>
				                 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getDescrTipo())%></font>&nbsp;</td>
							           <td class="l"><input type="checkbox" onclick="Javascript:Abilita('<%=totabilita%>');" name="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>" value="<%=lNotMod.getIdNotifica()%>" ></td>
							           <td class="l">Data Notifica</td>
							           <td class="l">
							            	<font class="campo">
									            <input type="text" disabled  name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
									            -
									            <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
									            -
									            <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>"   maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
								            </font>
								          </td>
				               </tr>
<%
				              //    PosAvvocato++;
				             // }
				            }
          				}
%>
		           <tr>
		             <td class="l">Autorità che ha effettuato la notifica</td>
					       <td class="l">
            			 <select disabled Title="Autorità che ha effettuato la notifica"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
             				 <%=autoritaEsternaDelegata%>
             			 </select>
					       </td>
	            </tr>
   						<tr>
		             <td class="l">Sede</td>
     						 <td class="L">
        				   	 <input disabled title="Sede Autorità che ha effettuato la notifica" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>"  maxlength="35" size="35">
									   <div id="divAvv<%=totabilita %>" style="display:none; width:100%;">
    							     <a href="Javascript:ListaComuni('DettaglioNotifica','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=totabilita%>]');">
          				 	     <img src="/images/filefolder.gif" border=0>
        					     </a>
        					 </div>
      					 </td>
 		     	       <td class="l">Indirizzo</td>
	 				       <td class="L" colspan="2">
					         <TEXTAREA disabled title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>" cols=30 ></TEXTAREA>
					       </td>
		           </tr>
<% 
							totabilita++;
            }
            else //Esiste  una check box
            {
              vedosubmit = true;
              if(lNotMod.getAvvIdAvvocatoFascicoloSiep()!=null)
              {
               if(lNotMod.getAvvSiep() !=null)
               {
%>
                 <tr>
    			       	 <td class="l">Destinatario</td>  			
<%
    			     if(lNotMod.getSogIdSoggetto() !=null)
    			     {
    			        if(fascicolo.getSoggetto() != null)
    			        {
%>
    			         <td class="l"><font class="campo"><%=fascicolo.getSoggetto().getCognome()%></font>&nbsp;<font class="campo"><%=fascicolo.getSoggetto().getNome()%></font></td>
    			         </tr>
<%
    			        }
    			     }
    			   	else if(lNotMod.getUfficio()!= null)
    			    {
%>
    			          <td class="l"><font class="campo"><%=lNotMod.getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=lNotMod.getUfficio().getDescrComune()%></font></td>
    			          </tr>
<%    
							}
    			  	else if(lNotMod.getAutoritaEsterna()!= null)
    			    {
%>
    			        <td class="l"><font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp; di &nbsp; <font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrSede()%></font></td>
    			        </tr>
<%
    			    }
%>
                 <tr>
                    <td class="l">Avvocato</td>
                     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getCognome() +" "+StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getNome()))%></font>&nbsp;
                     Foro di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getForo())%></font>&nbsp;
                  </td>
                 </tr>
                 <tr>
                   <td class="l">Tipo Difensore</td>
                   <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getDescrTipo())%></font>&nbsp;</td>
			             <td class="l"><input type="checkbox" onclick="Javascript:AbilitaUno();" name="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>" value="<%=lNotMod.getIdNotifica()%>" ></td>
			             <td class="l">Data Notifica</td>
			             <td class="l">
			             	 <font class="campo">
			                 <input type="text" disabled  name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
			            		 -
			            		 <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
			            		 -
			            		 <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>"   maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			            	 </font>
			             </td>
                 </tr>
<%
               //   PosAvvocato++;
               // }
             }
           }
%>
	         <tr>
             <td class="l">Autorità che ha effettuato la notifica</td>
			       <td class="l">
          			 <select disabled Title="Autorità che ha effettuato la notifica"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
           				 <%=autoritaEsternaDelegata%>
           			 </select>
			       </td>
           </tr>
 						<tr>
             <td class="l">Sede</td>
   						 <td class="L">
      				   	 <input disabled title="Sede Autorità che ha effettuato la notifica" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>"  maxlength="35" size="35">
							   <div id="divAvv" style="display:none; width:100%;">
  							     <a href="Javascript:ListaComuni('DettaglioNotifica','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
        				 	     <img src="/images/filefolder.gif" border=0>
      					     </a>
      					 </div>
    					 </td>
	     	       <td class="l">Indirizzo</td>
				       <td class="L" colspan="2">
			         <TEXTAREA disabled title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>" cols=30 ></TEXTAREA>
			       </td>
           </tr>			           
<%
          }
        }
%>
        <tr>
        	<td>&nbsp;</td>
        </tr>
<%
   		} /*---------FINE FOR */ 

   		if(vedosubmit)
   		{
   			if(contaabilita > 1)
   			{
%>
			     <tr>
			       <td>
			         <input type="hidden" name="flag" value="">
			         <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActAggiornaRegistrazioneNotificaDecrIrreperibilita">
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
			        <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActAggiornaRegistrazioneNotificaDecrIrreperibilita">
			
			       <input class="bottone" type="button" name="INSERISCI" value="Conferma" onClick="javascript:VerificaUno()">
			      </td>
			    </tr>
<%
				}
		}
%>
  </table>
  </form>
</body>
</html>