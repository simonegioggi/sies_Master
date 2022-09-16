<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.archiviazione.model.ArchiviazioneModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="evento"              scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="archiviazione"       scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>

<% 
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  ArchiviazioneModel lArcMod = archiviazione;

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

//   if(lArcMod == null)
//     lArcMod = new ArchiviazioneModel();
  
  boolean NLP = false;
  if(evento.getCodMotivo().equals("1773") || evento.getCodMotivo().equals("1774") || 
	 evento.getCodMotivo().equals("1775") || evento.getCodMotivo().equals("1776") ||
	 evento.getCodMotivo().equals("1777") || evento.getCodMotivo().equals("1778") ||
	 evento.getCodMotivo().equals("1779") || evento.getCodMotivo().equals("1780") ||
	 evento.getCodMotivo().equals("1781") || evento.getCodMotivo().equals("1782") ||
	 evento.getCodMotivo().equals("1783") || evento.getCodMotivo().equals("1784") ||
	 evento.getCodMotivo().equals("1875") )
  {
	  NLP = true;
  }
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Archiviazione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Definizione Procedimento - Provvedimento Altra Autorità</font>
    </td>
<%
    if (evento.getFlagDocumentoRegistrato() != null)
	      if (evento.getFlagDocumentoRegistrato().compareTo("N")==0)
	      {
	%>
		     <td class="LBG">
		     <%if(NLP)
		       {	 %>
		      		<a  href="/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActUploadNonLuogoAProvvedere&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.archiviazione.action.ActLoadDettaglioProvvAltraAutorita&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
		    			<img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
		    		</a>
		    <% }
		       else
		       { %>  
		        	<a  href="/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActUploadArchiviazione&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.archiviazione.action.ActLoadDettaglioProvvAltraAutorita&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
		   				<img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
		   			</a>
		    <% }  %>    
		     </td>

		<!-- BOTTONE DI STAMPA -->

			<td class="LBG">
		     <%if(NLP)
		       {	 %>
			   		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			     	<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActStampaNonLuogoAProvvedere&IdEvento="+evento.getIdEvento()%>"/>
			   		</jsp:include>
		    <% }
		       else
		       { %>  
			   		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			     	<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActStampaArchiviazione&IdEvento="+evento.getIdEvento()%>"/>
			   		</jsp:include>
		    <% }  %>    
		   	</td>	
	<%
	      }
    if (evento.getFlagDocumentoRegistrato() == null)
    {
    		if(NLP)
    		{	
%>
			     <td class="LBG">
			      	<a  href="/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActUploadNonLuogoAProvvedere&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.archiviazione.action.ActLoadDettaglioProvvAltraAutorita&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
			        	<img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
			      	</a>
			     </td>
		     	 <!-- BOTTONE DI STAMPA -->
				 <td class="LBG">
			   		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			     	<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActStampaNonLuogoAProvvedere&IdEvento="+evento.getIdEvento()%>"/>
			   		</jsp:include>
			   	 </td>
		<%	}
    		else
    		{	%>
				 <td class="LBG">
			      	<a  href="/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActUploadArchiviazione&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.archiviazione.action.ActLoadDettaglioProvvAltraAutorita&IdEvento=<%=evento.getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
			        	<img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
			      	</a>
			     </td>
		     	 <!-- BOTTONE DI STAMPA -->
				 <td class="LBG">
			   		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			     	<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActStampaArchiviazione&IdEvento="+evento.getIdEvento()%>"/>
			   		</jsp:include>
			   	 </td>    			   	 	
<%
    		}
    }
%>
      </tr>
    </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=3>
        <font class="campo">
<%
          if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
          {
%>
            DETENUTO PER ALTRA CAUSA
<%
          }
          else
          {
%>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
          }
%>
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
%>
<tr>
<%
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
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
          }
        }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}
    else
    {
%>
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
      }
    }
  }
%>
      </tr>
<%
       if(lArcMod.getCodProvvedimento()!= null)
        {
%>
         <tr>
          <td class="l">Provvedimento emessa da</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrProvvedimento())%></font>
          </td>
         </tr>
<%
        }


        if(lArcMod.getDataRicezione()!= null)
        {
%>
         <tr>
          <td class="l">Data Ricezione provvedimento</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataRicezione(), "dd-MM-yyyy") )%></font>
          </td>
         </tr>
<%
        }

       if(lArcMod.getDataEmissione()!= null)
        {
%>
         <tr>
          <td class="l">Data Emissione provvedimento</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataEmissione(), "dd-MM-yyyy") )%></font>
          </td>
         </tr>
<%
        }

      if(lArcMod.getNumProvvedimento()!= null)
      {
%>
       <tr>
        <td class="l">Anno /Numero Provvedimento</td>
        <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(lArcMod.getAnnoProvvedimento())%> /</font>
        <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getNumProvvedimento())%></font>
        </td>
       </tr>
<%
      }

       if(lArcMod.getCodTipoProvvedimentoArc() != null)
        {
%>
         <tr>
           <td class="l">Tipo Provvedimento</td>
           <td class="L">
             <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrTipoProvvedimentoArc())%></font>
           </td>
         </tr>
<%
       }

	if (lArcMod.getCodTipoAutoritaEmittente() != null && !lArcMod.getCodTipoAutoritaEmittente().equals("-")
	    // Ticket#202209120114 - in assnza della sede non stampava nulla nemmeno Presidente delle Repubblica (privo di sede)
			/*&& lArcMod.getCodLuogoEmittente() != null && !lArcMod.getCodLuogoEmittente().equals("-") */) {
		// MEV_66: aggiunto campo in visualizzazione = tdsm + udsm + uepe
		String descrTipoUfficio = lArcMod.getDescrTipoAutoritaEmittente();
		if ("UDSM".equals(lArcMod.getCodTipoAutoritaEmittente()))
			descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
%>
		<tr>
			<td class="l">Autorità emittente</td>
         	<td class="L">
          		<font class="campo"><%=StringUtils.toStringJSP(descrTipoUfficio)%></font>
          		<% // Ticket#202209120114 - di [sede] visualizzato solo se presente %> 
          		<% if (lArcMod.getCodLuogoEmittente() != null && !lArcMod.getCodLuogoEmittente().equals("-") ) { %>
          		&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrLuogoEmittente())%></font>
          		<% } %>
         	</td>
       	</tr>

<%
      }

       if(lArcMod.getDataDefinizione()!= null)
        {
%>
         <tr>
          <td class="l">Data Definizione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataDefinizione(), "dd-MM-yyyy") )%></font>
          </td>
         </tr>
<%
       }

       if(lArcMod.getCodOggettoDefinizione()!= null)
       {
%>
         <tr>
          <td class="l">Oggetto Definizione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrOggettoDefinizione())%></font>
          </td>
         </tr>
<%
       }

       if(magistrato != null)
       {
%>
         <tr>
          <td class="l">Magistrato
          <td class="L">
              <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
              <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
          </td>
         </tr>
<%
       }

			 if(lArcMod.getNote() != null)
   		 {
%>
         <tr>
          <td class="l">Note</td>
           <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getNote())%></font>
           </td>
         </tr>
<%
    }
%>
</table>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        	<tr>
          		<td class="L">
            		<input  class=bottone  type="submit" value="Conferma">
            	<%	if(NLP)
            		{%>
            				<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActUploadNonLuogoAProvvedere">
            	<%	}
            		else
            		{%>
            				<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActUploadArchiviazione">
            	<%	} %>	
            		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= evento.getIdEvento() %>">
            		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.archiviazione.action.ActLoadDettaglioProvvAltraAutorita">
          		</td>
        	</tr>
      </table>
	</form>
  </div>
 <br>
 <br>


</body>
</html>