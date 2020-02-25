<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>


<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo" %>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria" %>

<jsp:useBean id="ParentFormName"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ParentFormType"  scope="request" class="java.lang.String"/>

<jsp:useBean id="IdTitoloPrincipale"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaTitoli"  scope="request" class="java.util.Vector"/>

<!-- 							LoadPopupElencoTitoli								 -->

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] -Popup di Elenco Titoli associati all'Istruttoria </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
      window.focus();

      function insertIT(id_Titolo,
                        CodProvv,
                        DescrTipoProvv,
                        DataIrr,
                        DataProvv,
                        AnnoProvv, 
                        NumeroProvv,
                        DescrTipoAutorEmittente,
                        ComuneAutorEmittente,
                        CodTipoAutorEmittente,
                        CodLuogoAutorEmittente
          )
      {
        formname = '<%=ParentFormName%>';
        formtype = '<%=ParentFormType%>';
        //alert('insertIT - formname = '+formname);
        //alert('insertIT - formtype = '+formtype);

        window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value=id_Titolo;

        // Costruzione della stringa con le descrizione del Titolo
        var lEstremiSentenza='';
        lEstremiSentenza += DescrTipoProvv+" N. "+AnnoProvv+"/"+NumeroProvv+"  Emessa da "+DescrTipoAutorEmittente+" di "+ComuneAutorEmittente;
        lEstremiSentenza +="  il "+DataProvv+"  definitiva il "+DataIrr;
      
        // ICostantiRichiestePmInCumulo.ESTREMI_TITOLO_REVOCANTE è visualizzato nella form 'Inserisci Richiesta Revoca Benefici'
        //window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_TITOLO_REVOCANTE%>.value = lEstremiSentenza;
        
        var lDescrTitoloRevocante = "<font class='campo'>";
        lDescrTitoloRevocante+=lEstremiSentenza;
        lDescrTitoloRevocante+="</font>"
        window.parent.opener.document.getElementById('descTitoloRevocante').innerHTML = lDescrTitoloRevocante;


        if (formname=="ModRichGERevocaBen"){
          try{
            window.parent.opener.document.getElementById('divTitRevOld').style.display="none";
            window.parent.opener.document.getElementById('tabDatiTitRevOld').style.display="none";
          } catch(err){}
        }
        window.parent.opener.document.getElementById('divTitRev').style.display="block";
        window.parent.opener.document.getElementById('tabDatiTitRev').style.display="block";
        
        window.parent.close();
      }

      function controlla()
      {
        if(document.elenco.numeroLiberazioni.value==0)
        {
          alert("Nessun Titolo Presente");
          window.parent.close();
        }
      }

    </script>

  </head>

  <body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  
    <input type="HIDDEN" name="ParentFormName" value="<%=ParentFormName%>">
    <input type="HIDDEN" name="ParentFormType" value="<%=ParentFormType%>">
    <input type="hidden" name="numeroLiberazioni" value="<%=ListaTitoli.size()%>">
    <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.FORM_TYPE_RIC_REV_BENEFICI%>" value="RichiestaRevBenefici" >
  
    <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
        </td>
        <td class="LBG">
          <font class=label>Funzione :</font>
          <font class=campo>Elenco Titoli associati all'Istruttoria </font>
        </td>
      </tr>
    </table>
    <br>
    
     <!--				Elenco Titoli				-->  

  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="titolo" colspan=3 width=100%>Elenco Titoli in Istruttoria</td>
    </tr>
  </table>  
  
  <table>
    <tr>
      <td class="int" width="8%"  >Titolo</td>
      <td class="int" width="10%" >Data Titolo </td>
      <td class="int" width="8%"  >Numero sentenza</td>
      <td class="int" width="18%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="8%"  >Numero SIEP</td>
      <td class="int" width="25%" >Ufficio Esecuzione</td>
      <td class="int" width="5%"  >Azioni</td>
    </tr>
<%
	Iterator itx = ListaTitoli.iterator();

	while ( itx.hasNext())
	{
	   TitoloCumulatoModel lTitoCum = (TitoloCumulatoModel) itx.next();
	    
	   String nSiep = "";
       String AutoritaSiep = "";
       ProcedimentoCumulatoModel lProcedimentoCumulatoModel = lTitoCum.getProcedimentoCumulato();
       if (lProcedimentoCumulatoModel!=null)
       {
         if ("S".equals(lProcedimentoCumulatoModel.getFlagAccorpato()) ){
           UfficioModel lUfficioOrigine = lProcedimentoCumulatoModel.getUfficioOrigine();

           nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrOrigine();
           nSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>"; 
         }
         else {
           nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
         }
         
         
         
         AutoritaSiep = StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
       }
%>
    <tr>
      <td class="L">
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoProvvedimento(),"")%>
      </td>
      <td class="C" nowrap>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"))%>
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"") %>
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoAutoritaEmittente()+" "+lTitoCum.getDescrLuogoEmittente())%> 
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
      </td>
	  <td class="C" nowrap > <%=nSiep%>&nbsp;</td>
	  <td class="C" > <%=AutoritaSiep%>&nbsp;</td>
<%	  if(!lTitoCum.getIdTitoloCumulato().equals(new BigDecimal(IdTitoloPrincipale)) )
	  {	%>
	    <td class="C" >      
          <a href="Javascript:insertIT( '<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())%>', 
        							  '<%=StringUtils.toStringJSP(lTitoCum.getCodTipoProvvedimento())%>',
        							  '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lTitoCum.getDescrTipoProvvedimento() ),"-") %>',
        							  '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"),"")%>',
        							  '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"),"")%>',
        							  '<%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>',
        							  '<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza())%>',
        							
        							  '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lTitoCum.getDescrTipoAutoritaEmittente() ),"-") %>',
        							  '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lTitoCum.getDescrLuogoEmittente() ),"-") %>',
        							
        							  '<%=StringUtils.toStringJSP(lTitoCum.getCodTipoAutoritaEmittente())%>',
        							  '<%=StringUtils.toStringJSP(lTitoCum.getCodLuogoEmittente())%>'
        							);">
            <img align="middle" src="/images/fileselected.gif" border=0>
          </a>
        </td>
<%	}
	else
	{	%>
	    <td class="C" >&nbsp;</td>
	
<%	}	 %>	        

    </tr>
<%	} %>				
    </table>
  </form>
  </body>
</html>