<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>

<jsp:useBean id="scambiosanzione" scope="request" class="siap.siep.scambiosanzione.model.ScambioSanzioneModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="esito" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="lPenComSanSost"      scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>


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
    
    PenaComplessivaSanzioneSostitutivaModel lPenaComplessSSMod = lPenComSanSost;
//     if(lPenaComplessSSMod == null)
//     	lPenaComplessSSMod = new PenaComplessivaSanzioneSostitutivaModel();     
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>"> 
  <script language="JavaScript">
  var desktop;  
  	function ListaDocumentiSius(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scambiosanzione.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }
    
  </script> 
</head>

<body class="corpo">
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciAnnotazioneProvvedimento">
	<table>
	    <tr>
		    <td class="LBG">
		    	<a href="Javascript:window.print();">
		    		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
		    	</a>
		    </td>
		    <td class="LBG">
		    	<font class="label">Funzione :</font>&nbsp;&nbsp;
		    	<font class="campo">Dettaglio Annotazione Provvedimento Sanzione Sostitutiva</font>
			</td>
		</tr>
	</table>
	<br>
		<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	<br>    

<% //======================= BLOCCO POSIZIONE GIURIDICA ========================= %>
	<table>
		<tr>
			<td class="l">Posizione Giuridica </td>
			<td class="L" colspan=5>
				<font class="campo">
<%     			if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       			{%>
              		DETENUTO PER ALTRA CAUSA
<%    			}
				else
			    {%>
					<%=lPosizione.getDescrPosizioneGiuridica()%>
			<%  }%>
				</font>
			</td>
		</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
			if( lAltraCausa.getIstitutoDetenzione()!= null)
			{
%>
	           <tr>
	             <td class="l">Detenuto presso </td>
	             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
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
	        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
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
        }%>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
   	<%--  input type="HIDDEN" title="Codice Posizione"
   		value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" 
   		type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  
   		maxlength="6" size="6" --%>

<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
             </tr>
<%
          }
        }
%>
<%
if(lPenaComplessSSMod!=null)
{
SanzioneSostitutivaModel lSanSos = lPenaComplessSSMod.getSanzioneSostitutiva();
if(lSanSos != null && lSanSos.getIdSanzioneSostitutiva() != null)
{
%>

<tr>
<td class="L"><font class="label">Sanzione Sostitutiva applicata: </font></td>
<td class="L" colspan="5">
<%
if((lSanSos.getNumAnni()!=null && lSanSos.getNumAnni().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumMesi()!=null && lSanSos.getNumMesi().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumGiorni()!=null && lSanSos.getNumGiorni().compareTo(new BigDecimal(0))!=0))
{
%>

<font class="campo"><%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>&nbsp;</font>
<font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;</font>
<font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;</font>
<font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%></font>

<%
}

if(lSanSos.getSanzionePecuniariaMulta() != null && lSanSos.getSanzionePecuniariaMulta().intValue() != 0)
{
%>
<font class="label"> Sanz.Pec. Multa&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%>&nbsp;</font>&euro;
<br>
<%
}

if(lSanSos.getSanzionePecuniariaAmmenda() != null && lSanSos.getSanzionePecuniariaAmmenda().intValue() != 0)
{
%>
<font class="label"> Sanz.Pec. Ammenda&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%>&nbsp;</font>&euro;
<%
}
%>
</td>
</tr>
<%
}
}  
%>
<%
  if(penaresidua != null && penaresidua.getFlagSanzioneSostitutiva()!=null)
  {
%>
     <tr>
      <td class="l">Sanzione sostitutiva da espiare:</td>  
      <td class="L">
           <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
				 if(   (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
            || (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)
           )
         {
%>
          <font class="label"> Sanz.Pec.&nbsp;</font>
          <% if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) { %>
          <font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
          <% } %>
          <% if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) { %>
          <font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
          <% } %>
<%
         }
%> 

      </td>
    </tr>

<%}      
%>
<% //======================= FINE BLOCCO POSIZIONE GIURIDICA ========================= %>	

    
    <tr>		
		<td class="l">Data Ricezione</td>
		<td class="l" colspan="5">
			<font class="campo">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataAggiornamento(),"dd-MM-yyyy"))%>
			&nbsp;</font>
		</td>
	</tr>
    
    <tr>
    	<td class="l">Tipo Provvedimento</td>
    	<td class="l" colspan="5">
			<font class="campo">
    			<%=StringUtils.toStringJSP(scambiosanzione.getDescrTipoDecisione()) %>   
    		&nbsp;</font> 		
    	</td>
    </tr>
    <tr>
      <td class="l">Anno/Numero SIUS</td>
      <td class="l" colspan="5">
			<font class="campo">
	        	<%=StringUtils.toStringJSP(scambiosanzione.getChiaveAnnoFascicoloSius())%>
	       	 	/
	        	<%=StringUtils.toStringJSP(scambiosanzione.getChiaveProgrFascicoloSius())%>
	        &nbsp;</font>
      </td>
     </tr>
     <tr> 
      <td class="l"> Anno/Numero Provvedimento</td>
      <td class="l" colspan="5">
			<font class="campo">
        		<%=StringUtils.toStringJSP(scambiosanzione.getAnnoRegistro())%>
        		/
        		<%=StringUtils.toStringJSP(scambiosanzione.getNumeroRegistro())%>
        	&nbsp;</font>
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente</td>
      <td class="l" colspan="5">
			<font class="campo">
	          <%=StringUtils.toStringJSP(scambiosanzione.getDescrUfficioEmittente())%>
				&nbsp;di&nbsp;
	          <%=StringUtils.toStringJSP(scambiosanzione.getComuneUfficioEmittente())%>
	        &nbsp;</font>
      </td>
    </tr>
    <tr>
		<td class="l">Data Emissione Provvedimento </td>
		<td class="l" colspan="5">
			<font class="campo">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataEmissione(),"dd-MM-yyyy"))%>
			&nbsp;</font>
		</td>
	</tr>
    <tr>
      <td class="l">Oggetto Provvedimento</td>
      <td class="l" colspan="5">
			<font class="campo">
	        	<%=StringUtils.toStringJSP(scambiosanzione.getDescrTipoSanzione())%>
	        &nbsp;</font>
      </td>
    </tr>
    <tr>
      <td class="l">Esito</td>
      <td class="l" colspan="5">
			<font class="campo">
	        	<%=StringUtils.toStringJSP(scambiosanzione.getDescrNaturaSanzione())%>
	        &nbsp;</font>
      </td>
    </tr>
    <tr>
      <td  class="l">Note</td>
      <td class="l" colspan="5">
			<font class="campo">
	        	<%=StringUtils.toStringJSP(scambiosanzione.getNote())%>
	        &nbsp;</font>
      </td>
    </tr>
    <tr>
    	<td class="l">Ufficio Competente</td>
      	<td class="l" colspan="5">
			<font class="campo">
				<%=StringUtils.toStringJSP(scambiosanzione.getDescrUfficioSorveglianza())%>
	      		di
	          	<%=StringUtils.toStringJSP(scambiosanzione.getComuneUfficioSorveglianza())%>
          	&nbsp;</font>
      </td>
    </tr>
<%if(scambiosanzione.getCodNaturaSanzione().equals("IR") ||
	 scambiosanzione.getCodNaturaSanzione().equals("IT"))
{%>
    
 <tr>
<td class="lNoBord" colspan="2">
<FORM method="POST" name="Trasmissione Atti per Esecuzione" action="<%=IWebConstants.PG_MAIN%>">
   <input type="hidden" value="siap.siep.sanzionesostitutiva.action.ActLoadInserisciTrasmissioneAttiEsecuzione" name="<%=IWebConstants.ACTION_FIELD %>">
   <input type="hidden" value="S" name="lAnnotazione">
   <input type="hidden" value="<%=scambiosanzione.getComuneUfficioSorveglianza()%>" name="lSedeUfficio">
      <br><INPUT class="bottone" type="submit" name="T" value="Trasmissione Atti per Esecuzione">
  </FORM>
</td>
</tr>   
    
<%}%>    
</table>

	
</FORM>
</body>
</html>