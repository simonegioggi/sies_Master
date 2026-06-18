<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.web.Action" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaFascicoliModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="sentenze" scope="request" class="java.util.Vector" />
<jsp:useBean id="flagRicercaData" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRegistro" scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="RequestForPaging" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Sentenza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
        <script language="JavaScript">
var node;
function effettoTree(a)
{
  node=document.getElementById("elenco"+a);
  node.style.display = (node.style.display == "none")? "block" : "none";
  document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
  return false;

}

 function fascicolo(id)
 {

  document.elenco.<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>.disabled=true;
  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=false;
  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.value=id;
 }

 function sentenza(id)
 {
	  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=true;
	  document.elenco.<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>.disabled=false;
	  document.elenco.<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>.value=id;
 }

 function nuovo()
 {
	  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=true;
	  document.elenco.<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>.disabled=true;
 }
</script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Titoli Esecutivi</font></td>
  		<jsp:include page="/jsp/files/siap/siep/sentenza/BottoneInserimentoSige.jsp"></jsp:include>
       </tr>
    </table>

    <br>

<%if (!(RequestForPaging.equals("NO"))) {%>
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<%}%>

<br>
<br>
  <table cellpadding=5 cellspacing=5 width="100%">
    <tr>
<%

	String wCol1="180";
	String wCol2="130";
	String wCol3="230";
	String wCol4="130";
	String wCol5="100";
	String wCol6="100";

      if(flagRicercaData.equals("S"))
      {%>
        <td class="int" width="<%=wCol1%>">Anno/Numero</td>
        <td class="int" width="<%=wCol2%>">Tipo Tit.Esecutivo</td>
        <td class="int" width="<%=wCol3%>">Autorità</td>
        <td class="int" width="<%=wCol4%>">Data Tit.Esecutivo</td>
        <td class="int" width="<%=wCol5%>">Altro grado di giudizio</td>
        <td class="int" width="<%=wCol6%>">Sentenza della cassazione</td>
<%
      }
      else
      {
%>
        <td class="int" width="<%=wCol4%>">Data Tit.Esecutivo</td>
        <td class="int" width="<%=wCol1%>">Anno/Numero</td>
        <td class="int" width="<%=wCol2%>">Tipo Tit.Esecutivo</td>
        <td class="int" width="<%=wCol3%>">Autorità</td>
        <td class="int" width="<%=wCol5%>">Altro grado di giudizio</td>
        <td class="int" width="<%=wCol6%>">Sentenza della cassazione</td>
<%
      }
%>
      <td class="int">Azioni</td>
    </tr>
     </table>
<%
	int jPA =0;
	Vector lFascicoli = new Vector();
    Iterator itx = sentenze.iterator();
    while ( itx.hasNext())
    {
        // Paolo Cherubini 12/04/2011 inserisco la ricerca gia usata nella nuova istanza che riporta prima le sentenze
        // e poi i procedimenti collegati commento la if per risultato = 1 e modifico la setRequestAttribute
      //SentenzaModel sentenza = (SentenzaModel)itx.next();
      SentenzaFascicoliModel lSenFasMod= (SentenzaFascicoliModel)itx.next();
      SentenzaModel sentenza = lSenFasMod.getSentenza();
%>
     	<table cellspacing=2 cellpadding=2 width="100%">
      <tr>
<%
      if(flagRicercaData.equals("S"))
      {
%>
               
        <td class="C" width="<%=wCol1%>">
         		<%=StringUtils.toStringJSP(sentenza.getNumeroSentenza()).length()>0
         			? StringUtils.toStringJSP(sentenza.getAnnoSentenza())+"/"+StringUtils.toStringJSP(sentenza.getNumeroSentenza()) : "-"%>
<%
       				if(lSenFasMod.getFascicoli() != null &&  lSenFasMod.getFascicoli().length>0)
     					{
%>
         				<a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Ulteriori Fascicoli" ></a>
<%
       				}
%>       
       	</td>
        <td class="C" width="<%=wCol2%>"> 
          <%=StringUtils.toStringJSP(sentenza.getDescrTipoProvvedimento())%>
        </td>
        <td class="C" width="<%=wCol3%>">
          <%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%>
        </td>
        <td class="C" width="<%=wCol4%>">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd-MM-yyyy"))%>
        </td>

        <td class="C" width="<%=wCol5%>">
<%          if (sentenza.isAltroGiudizio()) {%>
            	<img src="/images/TickRed.gif">
<%          }else{
        	  	if(sentenza.getAnnoProvvRif() == null && sentenza.getNumeroProvvRif() == null){%>
            		&nbsp;
<%         	}else{%>         		
   					<img src="/images/TickRed.gif">
<% 				 }   						
          }%>
        </td>
        <td class="C" width="<%=wCol6%>">
<%          if (sentenza.isSentenzaCassazione()){%>
            	<img src="/images/TickRed.gif">
<%          }else{
      	  		if(sentenza.getNumeroSentenzaCassazione()== null &&  sentenza.getAnnoSentenzaCassazione() == null ){%>
            		&nbsp;
<%      		}else{%>        		
   					<img src="/images/TickRed.gif">
<%            	}
          }%>
        </td>
<%	      }else{%>
        <td class="C" width="<%=wCol4%>">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd-MM-yyyy"))%>
        </td>
        
  		<td class="C" width="<%=wCol1%>">
         		<%=StringUtils.toStringJSP(sentenza.getNumeroSentenza()).length()>0
         			? StringUtils.toStringJSP(sentenza.getAnnoSentenza())+"/"+StringUtils.toStringJSP(sentenza.getNumeroSentenza()) : "-"%>
<%       				if(lSenFasMod.getFascicoli() != null &&  lSenFasMod.getFascicoli().length>0){%>
         					<a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Ulteriori Fascicoli" ></a>
<%       				}
%>       
       	</td>
        
        <td class="C" width="<%=wCol2%>">
          <%=StringUtils.toStringJSP(sentenza.getDescrTipoProvvedimento())%>
        </td>
        <td class="C" width="<%=wCol3%>">
          <%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%>
        </td>

        <td class="C" width="<%=wCol5%>">
<%			if (sentenza.isAltroGiudizio()){%>
            	<img src="/images/TickRed.gif">
<%      	}else{
      	  		if(sentenza.getAnnoProvvRif() == null && sentenza.getNumeroProvvRif() == null){%>
            		&nbsp;
 <%			}else{%>      	
  					<img src="/images/TickRed.gif">
<% 	  			}
          	}%>
        </td>
        <td class="C" width="<%=wCol6%>">
<%			if(sentenza.isSentenzaCassazione() ){%>
            	<img src="/images/TickRed.gif">
<%			}else{
        	  	if(sentenza.getNumeroSentenzaCassazione()== null && sentenza.getAnnoSentenzaCassazione() == null ){%>
            		&nbsp;
<%			}else{%>         		
   					<img src="/images/TickRed.gif">
<%             	}
          	}
%>
        </td>
<%
      }
%>
      <td class="C">
        <jsp:include page="<%=ICostantiSentenza.PG_BUTTONS_SENTENZA%>">
           <jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
           <jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
           <jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdSentenza()%>"/>
           <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>"/>
           <jsp:param name="ValoreIdEntitaProvv" value="<%=sentenza.getCodTipoProvvedimento()%>"/>
        </jsp:include>
      </td>
    </tr>
<%
				//Caricamento Altri Esiti.
				if (lSenFasMod!=  null && lSenFasMod.getFascicoli() != null && lSenFasMod.getFascicoli().length>0)
				{
 					for(int j=0;j<lSenFasMod.getFascicoli().length;j++)
 					{
   					FascicoloSiepModel lFasMod = new FascicoloSiepModel(lSenFasMod.getFascicoli()[j]);
   					lFascicoli.add(lFasMod);
 					}
%>
 					</table>
   				<div id="elenco<%=jPA%>" style="width: 100%; display:none">
     				<%@include file="/jsp/files/siap/siep/sentenza/ListaFascicoliPerSentenza.jspf" %>
   				</div>
<%
   				lFascicoli.clear();
   				jPA++;
				}
				else
				{%>
 					</table>
			<%}
		}
%>
  </FORM>
  <br>

</body>
</html>