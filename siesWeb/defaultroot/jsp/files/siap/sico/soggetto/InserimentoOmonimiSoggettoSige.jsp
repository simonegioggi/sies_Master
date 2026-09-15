<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sico.soggetto.model.SoggettoFascicoliSigeModel"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeSentenzaModel" %>

<jsp:useBean id="SoggOmonimi" 	scope="request" class="java.util.Vector" />
<jsp:useBean id="soggetto" 		scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>

<%
	String lAzione = new String();
	lAzione = "siap.sico.soggetto.action.ActInserisciSoggettoSige"; 
%>
 
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] Inserimento Soggetto - Presenza Omonimie</title>
    <script language="JavaScript" src="/html/conferma.js"></script>

    <script language="JavaScript">
		var node;
		function effettoTree(a)
		{
		  node=document.getElementById("elenco"+a);
		  node.style.display = (node.style.display == "none")? "block" : "none";
		  document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
		  return false;
		
		}
	</script>	

  </head>

  <body class="corpo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class="label"> Funzione :</font> <font class=campo> Inserimento Soggetto - Presenza Omonimie</font></td>
    </tr>
  </table>

  <br>
  <div align=center>
  <table>
    <tr>
      <td class="LBG"> <font class=campo> Soggetti omonimi già presenti in archivio</font></td>
    </tr>
  </table>
  <br>

<%
	String wCol1="180";
	String wCol2="130";
	String wCol3="230";
	String wCol4="130";
	String wCol5="100";
	String wCol6="100";
%>

  <table cellspacing=2 cellpadding=2 width="100%">
    <tr>
      <td class="int" width="<%=wCol1%>">Cognome Nome</td>
      <td class="int" width="<%=wCol2%>">Sesso</td>
      <td class="int" width="<%=wCol3%>">Data Nascita</td>
      <td class="int" width="<%=wCol4%>">Luogo Nascita</td>
      <td class="int" width="<%=wCol5%>">Paternità</td>
      <td class="int" width="<%=wCol6%>">Proc.</td>
    </tr>
  </table>   

<%
  int jPA =0;
  Vector lFascicoli = new Vector();
  Iterator itx = SoggOmonimi.iterator();
  int numOmonimi = SoggOmonimi.size();
  int contatore = 0;
  while ( itx.hasNext())
  {
	  SoggettoFascicoliSigeModel soggFasSige = (SoggettoFascicoliSigeModel)itx.next();
	  SoggettoModel soggOmonimo = soggFasSige.getSoggetto();
	  contatore += 1;
%>
   <table cellspacing=2 cellpadding=2 width="100%">
	<tr>
    	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      	<td class="C" width="<%=wCol1%>"><%=soggOmonimo.getCognome()%>&nbsp;<%=soggOmonimo.getNome()%></td>
      	<td class="C" width="<%=wCol2%>"><%=soggOmonimo.getSesso()%></td>
      	<td class="C" width="<%=wCol3%>">&nbsp;
      	<font class="campo">
<%
        if(soggOmonimo.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggOmonimo.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggOmonimo.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggOmonimo.getAnnoNascita())%>&nbsp;

<%
        }
%>
        </font>
<%
       if(soggOmonimo.getDataNascitaPresunta() != null && soggOmonimo.getDataNascitaPresunta().equals("S"))
        {
%>
          <font class="campo"> (Data Presunta)</font>
<%      }

%>

      </td>

      <%if (soggOmonimo.getDescrComuneNascita().compareTo("-")==0)
        {%>
         <%if (soggOmonimo.getDescComuneNascitaEstero().compareTo("------------------------------")==0 || soggOmonimo.getDescComuneNascitaEstero().equals("") )
        {%>
        <td class="C" width="<%=wCol4%>">
          -
        </td>
         <%}else{%>
           <td class="C" width="<%=wCol4%>">
          <%=StringUtils.pulisciCampo(soggOmonimo.getDescComuneNascitaEstero(),soggOmonimo.getDescrStatoNascita().toUpperCase())%>  (<%=soggOmonimo.getDescrStatoNascita().toUpperCase()%>)&nbsp;
        </td>

          <%}%>
      <%}else
        {%>
        <td class="C" width="<%=wCol4%>"><%=soggOmonimo.getDescrComuneNascita()%> (<%=soggOmonimo.getCodProvinciaNascita()%>)&nbsp;</td>
      <%}%>
      <td class="C" width="<%=wCol5%>">
      <%if(soggOmonimo.getPaternita() != null && !soggOmonimo.getPaternita().equals("")){ %>
	      <%=StringUtils.toStringJSP(soggOmonimo.getPaternita())%>
	  <%} else { %>
	  	  &nbsp;
	  <%} %>
	  </td>
      <td class="C" width="<%=wCol6%>">
      <% if(soggFasSige.getFascicoli() != null &&  soggFasSige.getFascicoli().length > 0)
     	 { %>
      		<%= soggFasSige.getFascicoli().length %> <a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Procedimenti Sige" ></a>
	  <% } else {%> 
	  		&nbsp;
	  <% } %> 
      </td>

    </tr>

<%
	//Caricamento Procediemnti Sige.
	if(soggFasSige.getFascicoli() != null &&  soggFasSige.getFascicoli().length > 0)
	{
		for(int j=0; j < soggFasSige.getFascicoli().length; j++)
		{
			FascicoloSigeSentenzaModel lFasMod = new FascicoloSigeSentenzaModel(soggFasSige.getFascicoli()[j]);
			lFascicoli.add(lFasMod);
		}
%>
 		</table>
   		<div id="elenco<%=jPA%>" style="display:none; width:100%;">
     		<%@include file="/jsp/files/siap/sico/soggetto/ListaProcedimentiSige.jspf" %>
   		</div>
<%
		lFascicoli.clear();
   		jPA++;
	} else {
		if(contatore == numOmonimi){
	%>
 			</table>
<%  
		}
	}

  } // end while
%>

</div>
  <br>

  <table>
	<tr>
		<td>
 			<font class="cVerde">Per confermare l'inserimento del soggetto, clickare sul pulsante "Conferma"</font>
		</td>
	</tr>    
  </table>
  
  <form  method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciOmonimoSoggetto">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  	
  	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_COGNOME %>" value="<%=soggetto.getCognome()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_NOME %>" value="<%=soggetto.getNome()%>">  	
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_SESSO %>" value="<%=soggetto.getSesso()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>" value="<%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" value="<%=StringUtils.toStringJSP(soggetto.getMeseNascita())%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA %>" value="<%=DateUtils.getDateToString(soggetto.getDataNascita(), "dd")%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA %>" value="<%=soggetto.getDataNascitaPresunta()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>" value="<%=soggetto.getCodComuneNascita()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_COD_PROVINCIA_NASCITA %>" value="<%=soggetto.getCodProvinciaNascita()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_COD_COMUNE_CASELLARIO %>" value="<%=soggetto.getCodComuneCasellario()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_COD_STATO_NASCITA %>" value="<%=soggetto.getCodStatoNascita()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>" value="<%=soggetto.getDescComuneNascitaEstero()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_NAZIONALITA %>" value="<%=soggetto.getNazionalita()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_PATERNITA %>" value="<%=soggetto.getPaternita()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE %>" value="<%=soggetto.getCognomeMadre()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_NOME_MADRE %>" value="<%=soggetto.getNomeMadre()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>" value="<%=soggetto.getAttoNascita()%>">
	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_NOTE %>" value="<%=soggetto.getNote()%>">
  	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>" value="<%=soggetto.getCodFiscale()%>">
  	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_COD_AFIS %>" value="<%=soggetto.getCodAfis()%>">
  	<%-- 20180124 MEV 57: [EC] I CAMPI HIDDEN PER DATA COMMESSO REATO ED ETA PRESUNTA --%>
  	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO %>" value="<%=DateUtils.getDateToString(soggetto.getDataReatoSius(), "dd")%>">
  	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_COMMESSO_REATO %>" value="<%=DateUtils.getDateToString(soggetto.getDataReatoSius(), "MM")%>">
  	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_COMMESSO_REATO %>" value="<%=DateUtils.getDateToString(soggetto.getDataReatoSius(), "yyyy")%>">
  	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI %>" value="<%=soggetto.getEtaPresuntaAnni()%>">
  	<input type="HIDDEN" name="<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI %>" value="<%=soggetto.getEtaPresuntaMesi()%>">
  	   
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l"><font class="label">Cognome e Nome</font></td>
      <td class="l"><font class="campo"><%=soggetto.getCognome() %>&nbsp;&nbsp;<%=soggetto.getNome() %></font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Sesso</font></td>
      <td class="l"><font class="campo"><%=soggetto.getSesso()%>&nbsp;</font></td>
    </tr>
	<tr>
      <td class="l" width="25%">
        <font  class="label">Data di nascita</font>
      </td>
      <td class="l" width="25%">
        <font class="campo">
<%
        if(soggetto.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggetto.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggetto.getAnnoNascita())%>&nbsp;
<%
        }
%>
        </font>
      </td>
            <td class="l" width="25%">
        <font class="label">Data Presunta</font>
      </td>
      <td class="l" width="25%">
        <font class="campo"><%=StringUtils.toStringJSP(soggetto.getDataNascitaPresunta())%></font>
      </td>
      </tr>
	<tr>
	  <td class="l"><font class="label">Età Presunta</font></td>
		      <td class="l"><font class="campo">
		<% if (soggetto.getEtaPresuntaAnni()!=null){%> 
		      	<%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font> anni
		<% if (soggetto.getEtaPresuntaMesi()!=null){%> 
		      	e <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font> mesi
		<%} %> 
		<%} %>
				&nbsp;
      </td>
      <!-- MEV 57 : SE PRESENTE VISUALIZZARE LA DATA DEL COMMESSO REATO -->
       <td class="l" width="25%">
        <font class="label">Data Commesso Reato</font>
      </td>
      <td class="l" width="25%">
       	<font class="campo">
       	<%
		        if(soggetto.getDataReatoSius() != null)
		        {
		%>
		          <%=DateUtils.getDateToString(soggetto.getDataReatoSius(),"dd-MM-yyyy")%>&nbsp;
		<%
		        }
		        else
		        {
		%>
		          <%="**-"+StringUtils.toStringJSP(soggetto.getDataReatoSius(), "**")+"-"+StringUtils.toStringJSP(soggetto.getDataReatoSius(), "****")%>&nbsp;
		<%
		        }
		%>
       	</font>
   	  </td>
	    	  
    </tr>
    <tr>
      <td class="l"><font  class="label">Comune Nascita</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita() )%>
<%        if( (soggetto.getDescrComuneNascita() != null)
              && (!(soggetto.getDescrComuneNascita().equals("")))
              && (!(soggetto.getDescrComuneNascita().equals("-"))) )
          {
%>
            (<%=soggetto.getCodProvinciaNascita()%>)
<%
          }
%>
          &nbsp;
        </font>
      </td>
    </tr>
	<tr>
	  <td class="l"><font class="label">Stato Cittadinanza</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrNazionalita())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Stato Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Comune Di Nascita Estero</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
    </tr>
	  <tr>
      <td class="l"><font class="label">Paternità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getPaternita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Nome Madre</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNomeMadre())%>
      <%=StringUtils.toStringJSP(soggetto.getCognomeMadre())%>&nbsp;</font></td>
    </tr>
	<tr><td colspan=4 class=l>&nbsp;</td></tr>
    <tr>
      <td class="l"><font class="label">Codice Fiscale</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodFiscale())%>&nbsp;</font></td>
      <td class="l"><font class="label">Atto Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getAttoNascita()) %>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Codice Fascicolo Rosso</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodCs())%>&nbsp;</font></td>
      <td class="l"><font class="label">Codice CUI</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis() )%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font  class="label">Note</font></td>
      <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNote())%>&nbsp;</font></td>
    </tr>

	<tr>
      <td colspan="4">
        <input class=bottone type="submit" value="Conferma">
      </td>
    </tr>

</table>

</form>
  <br>

  </body>
</html>