<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep" %>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="evento"            scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="eventoGiudice"     scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="decretoordinanza"   scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"       scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="VecMisureSic"		scope="request" class="java.util.Vector"/>
<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  
  MisuraSicurezzaModel MisuraMod = null;
  List listaMisure =(List) request.getAttribute("listaMisureSic");
%>

<!-- 		LoadDettaglioAnnotazioneDecisioneGiudiceCassazione		 -->
<html>
<head>
  <title>[S.I.E.S.] - Gestione Annotazione Provvedimento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  
  <script language="JavaScript1.2">
  function over_effect(e,state)
  {
		if (document.all)
	      	source4=event.srcElement
	    else if (document.getElementById)
	       	source4=e.target
	    if (source4.className=="menulines")
	      	source4.style.borderStyle=state
	    else
	    {
		    while(source4.tagName!="TABLE")
		    {
		       	source4=document.getElementById? source4.parentNode : source4.parentElement
		       	if (source4.className=="menulines")
		       		source4.style.borderStyle=state
		    }
	    }
  }
  </script>
  
  <style>
		.menulines
      	{
	        border:2.5px solid #BEC6FC;
	        text-align : center;
	        font-family: 'Tahoma';
	        color : Navy;
	        font-size : 13px;
	        text-decoration : none;
	        height:100%;
	        font-weight : normal;
      	}

      	.menulines a
      	{
	        text-align : center;
	        text-decoration:none;
	        color:black;
	        font-family: 'Tahoma';
	        color : Navy;
	        font-size : 13px;
	        width:100%;
	        height:100%;
      	}
  </style>
  
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione : </font>
      &nbsp;&nbsp;
      <font class="campo">Dettaglio Annotazione decisione del Giudice/Cassazione/Riesame</font>
    </td>
<%
    if (evento.getFlagDocumentoRegistrato() == null || 
    	evento.getFlagDocumentoRegistrato().compareTo("N")==0 )
    {
%>
     <td class="LBG">
      <a  href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadAnnotazioneDecisioneGiudiceCassazione&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAnnotazioneDecisioneGiudiceCassazione&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
        <img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
      </a>
     </td>
<%
    }
%>
  </tr>
    </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <table width = "80%">
    <tr>
      <td class="l" width = "25%">Posizione Giuridica </td>
      <td class="L" colspan=3>
        <font class="campo">
<%     if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>        
       		DETENUTO PER ALTRA CAUSA
<%     }
       else
       {	%>
        	<%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
<%     }%>

        </font>
      </td>
    </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>

                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>

             </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>

                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>

            </td>
          </tr>
<%
        }

    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
           </tr>
<%
          }
        }
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>       
         <tr>
          <td class="l">Reclusione</td>
          <td class="l" >
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
<%
          if(penaresidua.getImportoMulta() != null && penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
          {
%>
            <td class="l">Multa</td>
            <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          }%></tr><% 
        }
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}
    else
    {
%>   <tr>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
<%
      if(penaresidua.getImportoAmmenda() != null && penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
%>
        <td class="l">Ammenda</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }%>   </tr><% 
    }
  }
%>
</table>
<!--   // Misura di Sicurezza   -->
<%	
	if(VecMisureSic!=null && VecMisureSic.size() > 0)
	{ %>
		<table width = "80%">
<% 		Iterator Itx3 = VecMisureSic.iterator();
		while (Itx3.hasNext() )
		{
			MisuraMod = (MisuraSicurezzaModel) Itx3.next();
%>
		    <tr>
		    	<td class=L width = "25%">Misura di Sicurezza da espiare</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getDescrTipo())%>&nbsp;</font></td>
		    </tr>
		    <tr>
		    	<td class=L width = "25%">&nbsp;  Durata  Misura  </td>  	
		      	<td class=L>
		      		<font class="label"> Anni </font>&nbsp;
		      		<font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getNumAnni(),"0")%>&nbsp;&nbsp;&nbsp;</font>
		      		<font class="label"> Mesi </font>&nbsp;
		      		<font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getNumMesi(),"0")%>&nbsp;&nbsp;&nbsp;</font>
		      		<font class="label"> Giorni </font>&nbsp;
		      		<font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getNumGiorni(),"0")%>&nbsp;&nbsp;&nbsp;</font>
				</td>	
		    </tr>
<%			if(MisuraMod.getLuogoEsecuzioneMisura()!=null && MisuraMod.getLuogoEsecuzioneMisura().compareTo("")!=0)
			{ %>
		    	<tr>
		    		<td class=L width = "25%">&nbsp;  Luogo Esecuzione Misura </td>
		    		<td class=L><font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getLuogoEsecuzioneMisura())%>&nbsp;</font></td>
		    	</tr>						    
<%			}
		} %>
		</table>		      		
<% 	}
	else if(listaMisure!=null && listaMisure.size() > 0)
	{  %>
		<table width = "80%">
<% 		Iterator Itx4 = listaMisure.iterator();	%>
<%		while (Itx4.hasNext() )
		{
			MisuraMod = (MisuraSicurezzaModel) Itx4.next();
%>
		    <tr>
		    	<td class=L width = "25%">Misura di Sicurezza da espiare</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getDescrTipo())%>&nbsp;</font></td>
		    </tr>  	
		    <tr>
		    	<td class=L width = "25%">&nbsp;  Durata  Misura  </td>  	
		      	<td class=L>
		      		<font class="label"> Anni </font>&nbsp;
		      		<font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getNumAnni(),"0")%>&nbsp;&nbsp;&nbsp;</font>
		      		<font class="label"> Mesi </font>&nbsp;
		      		<font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getNumMesi(),"0")%>&nbsp;&nbsp;&nbsp;</font>
		      		<font class="label"> Giorni </font>&nbsp;
		      		<font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getNumGiorni(),"0")%>&nbsp;&nbsp;&nbsp;</font>
				</td>		
		    </tr>
<%			if(MisuraMod.getLuogoEsecuzioneMisura()!=null && MisuraMod.getLuogoEsecuzioneMisura().compareTo("")!=0)
			{ %>
		    	<tr>
		    		<td class=L width = "25%">&nbsp;  Luogo Esecuzione Misura </td>
		    		<td class=L><font class="campo"><%=StringUtils.toStringJSP(MisuraMod.getLuogoEsecuzioneMisura())%>&nbsp;</font></td>
		    	</tr>		    
<%			}
		} %>
		</table>		      		
<%	}	 %>	
<br>  
<table width="80%">
<!-- 	Provvedimento Annotazione SIEP		 --> 
	<tr>
    	<td colspan=2 class="titolo">Dati Provvedimento Annotazione Decisione SIEP</td>
  	</tr>
    <tr>
    	<td class="l">Data Ricezione </td>
        <td class="L">
           	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataRicezioneAtti(), "dd-MM-yyyy") )%></font>
        </td>
    </tr>    
    <tr>
    	<td class="l">Data Annotazione </td>
        <td class="L">
           	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(), "dd-MM-yyyy") )%></font>
        </td>
    </tr>
	<tr>
    <tr>
       	<td class="l">Tipo Provvedimento</td>
       	<td class="L">
       	<font class="campo"><%=StringUtils.toStringJSP(evento.getDescrTipoProvvedimento())%>&nbsp;<%=StringUtils.toStringJSP(evento.getDescrMotivo())%>
       	</font>
       	</td>
    </tr>
   <tr>
       <td class="l">Autorità Emittente</td>
       <td class="l">
           <font class="campo"> <%=StringUtils.toStringJSP(evento.getDescrUfficioEmittente())%></font>&nbsp; di
           <font class="campo"> <%=StringUtils.toStringJSP(evento.getDescrLuogoEmittente())%></font>
       </td>
   </tr>
	<tr><td>&nbsp;</td></tr>
<!-- 	Provvedimento Giudice Cassazione/Riesame		 --> 
	<tr>
    	<td colspan=2 class="titolo">Dati Ordinanza/Decreto Giudice/Cassazione</td>
  	</tr>    
    <tr>
    	<td class="l">Data Emissione provvedimento</td>
        <td class="L">
           	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoGiudice.getDataEmissione(), "dd-MM-yyyy") )%></font>
        </td>
    </tr>

    <tr>
       	<td class="l">Tipo Provvedimento</td>
       	<td class="L">
           	<font class="campo"><%=StringUtils.toStringJSP(eventoGiudice.getDescrTipoProvvedimento() )%></font>
       	</td>
    </tr>
    
<%  if(eventoGiudice != null && eventoGiudice.getProgrProtocollo() != null )
    { %>    
	<tr>
       	<td class="l">Anno/Numero Provvedimento</td>
       	<td class="L">
           	<font class="campo"><%=StringUtils.toStringJSP(eventoGiudice.getAnnoProtocollo() )%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(eventoGiudice.getProgrProtocollo() )%></font>
       	</td>
    </tr>
<%	} %>  
  
    <!-- 		ANNO E NUMERO R.G.N.R.	- 11/2015 Eliminato su segnalazione di M.T.		 -->
<%  //if(decretoordinanza != null && decretoordinanza.getAnnoRegistro() != null )
    //{ %>
	<!--  tr>
       	<td class="l">Anno/Numero R.G.N.R. </td>
       	<td class="L">  
	        <font class="campo">< %=StringUtils.toStringJSP(decretoordinanza.getAnnoRegistro())%>&nbsp;/&nbsp;< %=StringUtils.toStringJSP(decretoordinanza.getNumRegistro())%></font>
      </td>
    </tr -->	        
<%  //}%>

    <!-- 		ANNO E NUMERO E TIPO REG. GEN.			 -->
<%  if(decretoordinanza != null && decretoordinanza.getAnnoRegGen() != null && 
		decretoordinanza.getTipoRegGen() != null && 
		!decretoordinanza.getTipoRegGen().equals("-") )
    { %>
		<tr>
       		<td class="l">Anno/Numero e Tipo Reg. Gen. </td>
       		<td class="L">  
	        	<font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getAnnoRegGen())%>&nbsp;
	        	/&nbsp;<%=StringUtils.toStringJSP(decretoordinanza.getNumeroRegGen())%>&nbsp;
	        	<%=StringUtils.toStringJSP(decretoordinanza.getTipoRegGen()) %>
	        	</font>
      		</td>
   		</tr>
<%	} %>   
   <tr>
       <td class="l">Autorità Emittente</td>
       <td class="l">
           <font class="campo"> <%=StringUtils.toStringJSP(eventoGiudice.getDescrUfficioEmittente())%></font>&nbsp; di
           <font class="campo"> <%=StringUtils.toStringJSP(eventoGiudice.getDescrLuogoEmittente())%></font>
       </td>
   </tr>
   <tr>
      <td class="l">Tipologia Decisione</td>
       <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(evento.getDescrMotivo())%></font>
       </td>
   </tr>
   <tr>
      <td class="l">Oggetto Decisione</td>
       <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(evento.getDescrMotivo())%></font>
       </td>
   </tr>   
<%
   if(eventoGiudice != null && eventoGiudice.getCodEsito()!= null )
   {
%>
       <tr>
         <td class="l">Esito </td>
         <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(eventoGiudice.getDescrEsito())%></font>
         </td>
      </tr>
<%
   }

   if(decretoordinanza != null && decretoordinanza.getNote()!= null)
   {
%>
         <tr>
          <td class="l">Note</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getNote())%></font>
          </td>
         </tr>
<%
   }
%>
</table>
 <br>

	<!-- Menù di prosecuzione Esecuzione misura - Solo dopo Validazione Provvedimento -->	
<% 	if((evento.getFlagDocumentoRegistrato()!=null) &&
	   (evento.getFlagDocumentoRegistrato().compareTo("S")==0) )
	{
%>

	<table cellpadding="5" cellspacing="5" width="32%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
		<br>
		<tr>
			<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciComunicazionePolizia">Comunicazione 
			</a>
			</td>		
			<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciOrdinediConsegna">Ordine di Consegna 
			</a>
			</td>
			<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciOEInternamento">Ordine Esecuzione per Internamento
			</a>
			</td>
			<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciOrdineLiberazione">Ordine di Liberazione
			</a>
			</td>
			<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciRichiestaDAP">Richiesta al D.A.P.
			</a>
			</td>
		</tr>
		<tr>	
			<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciArchiviazionePerProvvGiudiceCassazione">Archiviazione per Provvedimento del Giudice
			</a>
			</td>      
    	</tr>
   	</table>
<%	} %> 

  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadAnnotazioneDecisioneGiudiceCassazione">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= evento.getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActLoadDettaglioAnnotazioneDecisioneGiudiceCassazione">
          </td>
        </tr>
      </table>
</form>
</div>
<br>
<br>
</body>
</html>