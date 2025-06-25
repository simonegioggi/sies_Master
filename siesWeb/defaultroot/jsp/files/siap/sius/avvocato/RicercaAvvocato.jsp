<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<html>
<head>
	<title>[S.I.E.S.] - Lista Avvocati</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <% if (! modalita.equals("NoPop"))
  {
  %>
	<script language="JavaScript">
  function controlla(id)
  {
         if(id=="S")
         {
          alert("Il difensore risulta sospeso");
          return false;
         }
         if(id=="R")
         {
          alert("Il difensore risulta radiato");
          return false;
         }
         <%-- MEV_21 --%>
         if (id!="A"	&& id!="-") {
           alert("Il difensore risulta non in attività");
           return false;
         }
         return true;
/*
         if(id=="A")
         {
          alert("Il difensore risulta non in attività");
          return false;
         }
         if(id=="B")
         {
          return true;
         }
*/
 }



  function insertIT(id,cognome,nome,foro,indirizzo,telefono,fax,email,
                    pec,  <%-- MEV_21: nuovo dato --%>
                    codicefiscale,luogoNascita,
                    codStatoNascita, <%-- MEV_21: nuovo dato --%>
                    giornoNascita,meseNascita,annoNascita,
                    descComuneStudio, <%-- MEV_21: redidenza diventa descComuneStudio  --%>
                    stato) <%-- MEV_21: nuovo dato --%>
  {
    var flag=controlla(stato);

    if(flag)
    {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=id;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome;
          if(nome == "-")
          {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value="";
          }else
          {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value=nome;
          }
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FORO%>.value=foro;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>.value=indirizzo;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_TELEFONO%>.value=telefono;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FAX%>.value=fax;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_E_MAIL%>.value=email;
          <%-- MEV_21: aggiunte PEC --%>
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_PEC%>.value = pec;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>.value=codicefiscale;
  
          <%-- MEV_21: aggiunte visualizzazioneUlteriori informazioni --%>
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.value = codStatoNascita;
          if (codStatoNascita == "039") {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = luogoNascita;
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = "";
          } else {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = "";
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = luogoNascita;
          }
          <%-- MEV_21: FINE --%>
    
          // Popola altri campi
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value=giornoNascita;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value=meseNascita;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value=annoNascita;
		      <%-- MEV_21 sostituzione di CAMPO_COD_COMUNE_RESIDENZA con CAMPO_DESC_COMUNE_STUDIO  --%>
          <%-- window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value=luogoNascita;  --%>
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>.value = descComuneStudio;
          <%-- window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>.value=residenza; --%>
          <%-- MEV_21 aggiunta gestione CAMPO_COD_NON_ATTIVITA  --%>          
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>.value = stato;         
 
          window.parent.close();
     }
	}

  <%-- MEV_21: aggiunte visualizzazione Ulteriori informazioni --%>
  function altreInfo (idRecord) {
    var riga = document.getElementById(idRecord);
    if (riga.style.display =="none") {
        riga.style.display = "block";
        document.images["image_"+idRecord].src = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
        document.images["image_"+idRecord].alt = "Collassa";
    } else {
        riga.style.display = "none";
        document.images["image_"+idRecord].src = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
        document.images["image_"+idRecord].alt = "Espandi";
    }
  }
  <%-- MEV_21: FINE --%>

  function avvocati() {
    if (<%=avvocato.size()%> == 0) {
      // MEV_21 in caso di chiusura infruttuosa della ricerca Avv. su SIES, si chiede se se ne vuole inserire uno non certificato Reginde.
      // In caso di risposta affermativa si abilitano tutti i campi per la digitazione e il pulsante di inserimento
      // manuale dell'avvocato.
      var msgConfirm = "Attenzione! Nessun Difensore trovato.\nSi vuole procedere con l'inserimento di un difensore\nnon certificato ReGIndE ? "; 
      if (window.confirm(msgConfirm)) {
        //FIXME da terminare
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('lTipoInserimento').value = "manuale";
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('inserimento').style.visibility = 'visible';
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('confermaBtn').style.visibility = 'hidden';
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('ricReginde').style.visibility = 'hidden';

        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_COGNOME%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_NOME%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>').disabled = false;
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_FORO%>').disabled = false;
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_TELEFONO%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_FAX%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_E_MAIL%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_PEC%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>').readOnly = false; 
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>').disabled = false;
        
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('IconComuneNascita').style.visibility = 'visible';
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('IconComuneStudio').style.visibility = 'visible';
      } else {
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('lTipoInserimento').value = "reginde";
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('inserimento').style.visibility = 'hidden';
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('confermaBtn').style.visibility = 'visible';
        window.parent.opener.document.<%=request.getParameter("formname")%>.document.getElementById('ricReginde').style.visibility = 'visible';
      }
      window.parent.close();
    }
  }

	</script>
 <%
  }
 %>
</head>

<body class="corpo" onload ="avvocati();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Difensori</font></td>
    </tr>
  </table>
 <% if (modalita.equals("NoPop"))
  {
  %>
 <BR>
 <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
 <BR>
  <% } %>

<form name="f">
 <table width="100%">
  <%-- MEV_21 previsti nuovi campi 
  <tr>
    <td class=int  width=40%>Nome</td>
    <td class=int  width=10%>Foro</td>
    <td class=int  width=40%>Indirizzo</td>
    <% if (! modalita.equals("NoPop")) { %>
      <td class=int width=10%>Seleziona</td>
    <% } %>
  </tr>
  --%>

  <tr>
    <td class="int">Cognome e Nome</td>
    <td class="int">Codice Fiscale</td>
    <td class="int">Foro</td>
    <td class="int">Luogo e Data Nascita</td>
    <td class="int">Indirizzo Studio</td>
    <td class="int">Stato</td>
    <% if (! modalita.equals("NoPop")) { %>
    <td class="int">Seleziona</td>
    <% } %>
  </tr>


  <%
  	Iterator itx = avvocato.iterator();
    int id_record = 0;
  	while ( itx.hasNext())
  	{
      id_record += 1;
    	AvvocatoModel lAvv = ((AvvocatoSiusModel)itx.next()).getAvvocato();


      /* MEV_21 lo stato viene recuperato dal CodNOnAttivita
      String stato=null;
      if(lAvv.getDataSospensione()!= null){
        stato="S";
      }else if(lAvv.getDataRadiazione()!= null){
        stato="R";
      }else if(!lAvv.getCodNonAttivita().equals("-")){
        stato="A";
      }else{      
        stato="B";
      }
      */
      String stato = StringUtils.cStrForJS(lAvv.getCodNonAttivita());

//FIXME SIEP gestiscei i fori soppressi


	%>
  <tr>

    <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome()) + " " +  StringUtils.toStringJSP(lAvv.getNome())%>
      <a href="javascript:altreInfo('rec_<%=id_record%>')">
        <img name="image_rec_<%=id_record%>" style="vertical-align: middle;" align="middle" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" alt="Espandi" border="0">
      </a>
    </td>
    <td class=l><%=StringUtils.toStringJSP(lAvv.getCodiceFiscale())%></td>
    <td class=l><%=StringUtils.toStringJSP(lAvv.getForo(),"-")%></td>
		<td class=l><%=StringUtils.toStringJSP(lAvv.getDescLuogoNascita())%>,&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getDataNascita(),"dd-MM-yyyy"))%></td>
    <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo(),"-") + " - " + StringUtils.toStringJSP(lAvv.getDescrComuneStudio())%></td>
    <td class=l><%=StringUtils.toStringJSP(lAvv.getDescrNonAttivita())%></td>
        
    <% if (! modalita.equals("NoPop")) { %>
      <td class=c>
        <a href="Javascript:insertIT(
          '<%=lAvv.getIdAvvocato()%>',
          '<%=StringUtils.cStrForJS(lAvv.getCognome())%>','<%=StringUtils.cStrForJS(lAvv.getNome())%>',
          '<%=StringUtils.cStrForJS(lAvv.getForo())%>',
          '<%=StringUtils.cStrForJS(lAvv.getIndirizzo())%>',
          '<%=StringUtils.cStrForJS(lAvv.getTelefono())%>','<%=StringUtils.cStrForJS(lAvv.getFax())%>',
          '<%=StringUtils.cStrForJS(lAvv.getEMail())%>',
          '<%=StringUtils.cStrForJS(lAvv.getPec())%>',  <%-- MEV_21: aggiunto--%>
          '<%=StringUtils.cStrForJS(lAvv.getCodiceFiscale())%>',
          '<%=StringUtils.cStrForJS(lAvv.getDescLuogoNascita())%>',
          '<%=StringUtils.cStrForJS(lAvv.getCodStatoNascita())%>',  <%-- MEV_21: aggiunto--%>
          '<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"dd"))%>',
          '<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"MM"))%>',
          '<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"yyyy"))%>',
           <%-- MEV_21: sostituito '<%=StringUtils.cStrForJS(lAvv.getDescComuneResidenza())%>',  --%>
          '<%=StringUtils.cStrForJS(lAvv.getDescrComuneStudio())%>', <%-- MEV_21: aggiunto--%>
          '<%=stato%>' <%-- MEV_21: aggiunto--%>
        );">
        <img align="middle" src="/images/fileselected.gif" border=0></a>
      </td>
    <% } %>
  </tr>

	<tr id="rec_<%=id_record%>" style="display:none;">
		<td class=l colspan="7">
			pec:&nbsp;<%=StringUtils.toStringJSP(lAvv.getPec())%>&nbsp;&nbsp;&nbsp;
			Tel:&nbsp;<%=StringUtils.cStrForJS(lAvv.getTelefono())%>&nbsp;&nbsp;&nbsp;
			Fax:&nbsp;<%=StringUtils.cStrForJS(lAvv.getFax())%>&nbsp;&nbsp;&nbsp;
			e-mail:&nbsp;<%=StringUtils.cStrForJS(lAvv.getEMail())%>
		</td>
	</tr>
	<%
	}
%>

</table>
</form>
</body>
</html>
