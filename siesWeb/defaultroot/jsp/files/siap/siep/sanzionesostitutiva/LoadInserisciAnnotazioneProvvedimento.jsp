<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%> 
<%@ page import="java.util.Arrays"%> 

<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="esito" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="lPenComSanSost"      scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>


<%
	//String filtroEsito[] = (String[])request.getAttribute("filtroEsito");

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
  
	//Preparo le stringhe per il campo ESITO
	/*
	String strEsito ="";
    Iterator itxEs = esito.iterator();
	Arrays.sort( filtroEsito );	
	while(itxEs.hasNext())
	{
		DecodificheModel lDecMod = (DecodificheModel)itxEs.next();
	
		if( Arrays.binarySearch( filtroEsito, lDecMod.getCode() ) > -1 ){
			strEsito += lDecMod.getCodiceAlternativo() +";";
	        strEsito += lDecMod.getFiltro()+";";
	        strEsito += lDecMod.getDescription()+"#";
		}
	}
	*/
	
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  var desktop;  
  	function ListaDocumentiSius(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scambiosanzione.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&&<%=ICostantiScambioSanzione.CAMPO_NATURA_SS%>=<%=ICostantiScambioSanzione.ANNOTAZIONE%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }
    
    function Verify(){
    
    	if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiSanzioneSostitutiva.CAMPO_ID_SANZIONE_SOSTITUTIVA%>.value==''){
			alert('Selezionare un Provvedimento di Sorveglianza!');			 
			return false;
		}						
    
    }
    
  </script>
  <style>
	.readonly{
		background-color: transparent;
		border: solid 1px #8FBFC5;
		text-transform : uppercase;
		color : Blue;
		font-family: 'Tahoma';
		font-size: 12px;
		font-weight: bold;
		text-align:left;	
		padding: 1px 1px 1px 1px;	
		user-modify: read-only;		
	}
  </style>
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
		    	<font class="campo">Annotazione Provvedimento Sanzione Sostitutiva</font>
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
   	<%-- input type="HIDDEN" title="Codice Posizione" 
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
</table>

<br>
<table width=90%>
   <tr>
      <td class="Titolo" colspan='8'> Dati Provvedimento di Sorveglianza </td>
   </tr>
   <tr>
      <td class="l" colspan="4">
        <a href="Javascript:ListaDocumentiSius('LoadInserisciAnnotazioneProvvedimento');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    
    <tr>		
		<td class="l">Data Ricezione</td>
		<td class="l" colspan="3">
			<font class="campo">
				<input class="readonly" readonly title = "Giorno Data Ricezione" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_RICEZIONE %>"  <%=IWebConstants.UTIL_DATA%>> -
				<input class="readonly" readonly title = "Mese Data Ricezione" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_RICEZIONE %>"  <%=IWebConstants.UTIL_DATA%>> -
				<input class="readonly" readonly title = "Anno Data Ricezione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_RICEZIONE %>"  <%=IWebConstants.UTIL_DATA_ANNO%>>
			</font>
		</td>
	</tr>
    
    <tr>
    	<td class="l">Tipo Provvedimento</td>
    	<td>
    		<input class="readonly" readonly Title="Tipo Provvedimento" name="<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_PROVVEDIMENTO%>">    		
    	</td>
    </tr>
    <tr>
      <td class="l">Anno /Numero SIUS</td>
      <td class="l">
        <input class="readonly" readonly  Title="Anno Fascicolo Sius" name="<%=ICostantiSanzioneSostitutiva.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4">
        /
        <input class="readonly" readonly  Title="Numero Sius" name="<%=ICostantiSanzioneSostitutiva.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6">
      </td>
      <td class="l"> Anno / Numero Provvedimento</td>
      <td class="l">
        <input class="readonly" readonly  Title="Anno Provvedimento" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_PROVVEDIMENTO%>" type="text" size="4" maxlength="4">
        /
        <input class="readonly" readonly  Title="Numero Provvedimento" name="<%=ICostantiSanzioneSostitutiva.CAMPO_NUMERO_PROVVEDIMENTO%>" type="text" size="6" maxlength="6">
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente</td>
      <td class="l" colspan="3">
              <input class="readonly" readonly  Title="Ufficio Emittente" name="<%= ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>" size=50 type="text">
              <input class="readonly" readonly  type="hidden" name="<%=ICostantiSanzioneSostitutiva.CAMPO_COD_UFFICIO_SORVEGLIANZA%>" value="UDS">
       </td>
    </tr>
    <tr>
      <td class="l">Sede Ufficio Emittente</td>
      <td class="l" colspan="3">
        <font class="campo">
          <input class="readonly" readonly  Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT %>" size=35 type="text">
        </font>
      </td>
    </tr>
    <tr>
		<td class="l">Data Emissione Provvedimento </td>
		<td class="l" colspan="3">
			<font class="campo">
				<input class="readonly" readonly  title = "Giorno Data Emissione Provvedimento" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE %>"> -
				<input class="readonly" readonly  title = "Mese Data Emissione Provvedimento" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE %>"> -
				<input class="readonly" readonly  title = "Anno Data Emissione Provvedimento" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE %>">
			</font>
		</td>
	</tr>
    <tr>
      <td class="l">Oggetto Provvedimento</td>
      <td class="L" colspan="3">
        <input class="readonly" readonly Title="Oggetto Provvedimento" name="<%=ICostantiSanzioneSostitutiva.CAMPO_OGGETTO_PROVVEDIMENTO%>" size=80 type="text">
      </td>
    </tr>
    <tr>
      <td class="l">Esito</td>
      <td class="L" colspan="3">
        <input class="readonly" readonly Title="Esito" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ESITO%>" size=80 type="text">
      </td>
    </tr>
    <tr>
      <td  class="l">Note</td>
      <td  class="L" colspan="3">
        <textarea class="readonly" readonly Title="Note" name="<%= ICostantiSanzioneSostitutiva.CAMPO_NOTE %>" cols=80 rows=2></textarea>
      </td>
    </tr>
    <tr>
    	<td class="l">Ufficio Competente</td>
      <td  class="L" colspan="3">
			<input class="readonly" readonly  Title="Ufficio Competente" name="<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_COMPETENTE%>" size=50 type="text">          		
      		di:
          	<input class="readonly" readonly  Title="Luogo Ufficio Competente" name="<%= ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO_COMPETENTE %>" size=35 type="text">			
      </td>
    </tr>
</table>

<input type=hidden name="<%=ICostantiSanzioneSostitutiva.CAMPO_ID_SANZIONE_SOSTITUTIVA%>">
<input type=hidden name="<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>">


<table width="100%">
	<tr>
	<td class="lNoBord" colspan="2">
	      <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
	      <input type="HIDDEN" name="Action" value="siap.siep.sanzionesostitutiva.action.ActInserisciAnnotazioneProvvedimento">
	    </td>
	</tr>
</table>

	<script language="JavaScript" type="text/javascript">
    		var frmvalidator  = new Validator("LoadInserisciAnnotazioneProvvedimento");
    		frmvalidator.setAddnlValidationFunction("Verify");
    </script>

</FORM>
</body>
</html>