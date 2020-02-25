<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%@ page import="siap.sius.ulterioreistanza.model.UlterioreIstanzaModel" %>
<%@ page import="siap.sius.ulterioreistanza.action.ICostantiUlterioreIstanza" %>

<jsp:useBean id="modalita"  					scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAtto" 						scope="request" class="java.lang.String"/>
<jsp:useBean id="mittenteAtto" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto" 						scope="request" class="java.lang.String"/>
<jsp:useBean id="idAltraCausa" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="idFascicoloOrigine" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="ulterioreIstanza"		scope="request" class="siap.sius.ulterioreistanza.model.UlterioreIstanzaModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Ulteriore Istanza SIUS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      // Chiamata funzione lista Comuni. 
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>

    <script language="JavaScript">
      // Chiamata funzione lista Oggetti.
      var desktop;
      function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet )
      {
        // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
        var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
            aLink += "&formname="+a_formname;
            aLink += "&field_contenuto="+a_field_contenuto;
            aLink += "&fieldname="+a_fieldname;
            aLink += "&fieldcodes="+a_fieldcodes;
            aLink += "&fieldcodesdet="+a_fieldcodesdet;
            aLink += "&ifieldcodes="+i_fieldcodes;
            aLink += "&ifieldcodesdet="+i_fieldcodesdet;
        desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
      }
    </script>

    <script language="JavaScript">
      // Chiamata alla verify per controllo 
      function Verify()
      {
		// Verifica se la lunghezza è pari a 1 rettifica il valore in due cifre. ( Giorno e Mese ).
        if (document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value.length==1)
            document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value = ( '0' + document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value ) ;
            
        if (document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value.length==1)
            document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value= ( '0' + document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value ) ;

        // Controllo obbligatorietà tipo atto.
        var tipoAtto=document.LoadInserisciUlterioreIstanza.<%= ICostantiUlterioreIstanza.CAMPO_COD_TIPO_ATTO%>[document.LoadInserisciUlterioreIstanza.<%= ICostantiUlterioreIstanza.CAMPO_COD_TIPO_ATTO%>.selectedIndex].value;

        if(tipoAtto =="-")
        {
          alert("Il Campo Tipo Atto è obbligatorio");
          return false;
        }

        // Controllo della data atto solo se valorizzata.
        var dataAtto =	document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_RICHIESTA%>.value +'/'+ 
        								document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_MESE_DATA_RICHIESTA%>.value +'/'+ 
        								document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_RICHIESTA%>.value;
        				
        var dataSistema ='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'
        
        if ( dataAtto!='//')
        {
          if (! ControllaData(dataAtto))
          {
            alert('Data atto non valida');
            return false;
          }
          // Controllo della data atto <= data di sistema
          if (! CompareDate(dataAtto, dataSistema))
          {
            alert('Data atto > della data odierna');
            return false;
          }
        }

        // Controllo obbligatorietà contenuto.
        var contenuto = document.LoadInserisciUlterioreIstanza.<%= ICostantiUlterioreIstanza.CAMPO_COD_CONTENUTO%>[document.LoadInserisciUlterioreIstanza.<%= ICostantiUlterioreIstanza.CAMPO_COD_CONTENUTO%>.selectedIndex].value;

        if(contenuto =="-")
        {
          alert("Il Campo Contenuto è obbligatorio");
          return false;
        }

        // Controllo della data arrivo
        var dataArrivo= document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value + '/' + 
        								document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value + '/'+ 
        								document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>.value;
        
        if (! ControllaData(dataArrivo))
        {
          alert('Data di arrivo in cancelleria non valida');
          return false;
        }

        // Controllo della data arrivo <= data di sistema
        if (! CompareDate(dataArrivo, dataSistema))
        {
          alert('Data di arrivo > della data odierna');
          return false;
        }

        // Controllo della data atto <= data arrivo
        if (( data_atto!='//') && (! CompareDate(dataAtto, dataArrivo)))
        {
          alert('Data atto > data arrivo in cancelleria');
          return false;
        }
      	return true;
      }
    </script>
  </head>

  <body class="corpo">
  <table>
    <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class=LBG><font class="label">Funzione :</font>&nbsp;
<%			
				// Imposta l'azione da chiamare per l'inserimento o la modifica.
				String lAction = new String();
      	//FascicoloGPModel lFascicolo = new FascicoloGPModel();

        if( modalita.equals("I") )
        {
          lAction = "siap.sius.ulterioreistanza.action.ActInserisciUlterioreIstanza";
          //lFascicolo = (FascicoloGPModel)session.getAttribute("fascicoloSiusGP"); %>
          <font class="campo">Iscrizione Ulteriore Istanza</font>
<%			
				}
        else if( modalita.equals("M") )
        {
          //lFascicolo = (FascicoloGPModel)session.getAttribute("fascicoloSiusGP");
          lAction = "siap.sius.ulterioreistanza.action.ActModificaUlterioreIstanza"; %>
          <font class="campo">Modifica Ulteriore Istanza</font>
<%			
				}
%>
      </td>
    </tr>
  </table>

  <jsp:include page="/jsp/files/siap/sius/fascicolo/SintesiProcedimentoSius.jsp"/>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciUlterioreIstanza">
  <table cellspacing="2" cellpadding="2">

  <tr>
    <td class="l">Tipo Atto <font class=ob>(*)</font></td>
    <td class="L">
      <select title="tipoAtto" class="small" name="<%=ICostantiUlterioreIstanza.CAMPO_COD_TIPO_ATTO%>">
        <%= tipoAtto %>
      </select>
    </td>
  </tr>

  <tr>
    <td class="l">Data atto </td>
    <td class="L">
      <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(ulterioreIstanza.getDataRichiesta(),"dd")) %>" type="text" name="<%=ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_RICHIESTA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
      /
      <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(ulterioreIstanza.getDataRichiesta(),"MM")) %>" type="text" name="<%= ICostantiUlterioreIstanza.CAMPO_MESE_DATA_RICHIESTA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
      /
      <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(ulterioreIstanza.getDataRichiesta(),"yyyy")) %>" type="text" name="<%= ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_RICHIESTA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>

  <tr>
    <td class="l">Mittente </td>
    <td class="L">
      <select title="mittenteAtto" class=small name="<%=ICostantiUlterioreIstanza.CAMPO_COD_TIPO_MITTENTE_ATTO%>">
        <%= mittenteAtto %>
      </select>
      &nbsp;&nbsp;
<%
      if( modalita.equals("M") )
      {
        // Da cambiare 
%>		
				<input Title="descrMittente" value="<%=StringUtils.toStringJSP( ulterioreIstanza.getDescrMittente())%>" name="<%=ICostantiUlterioreIstanza.CAMPO_DESCR_MITTENTE%>"type="text" maxlength="200" size="50">
<%	  
			}
      else
      {
%>
        <input Title="descrMittente" name="<%=ICostantiUlterioreIstanza.CAMPO_DESCR_MITTENTE%>"type="text" maxlength="200" size="50">
<%	  
	  	}
%>
    </td>
  </tr>
  <tr>
    <td class="l">Sede Mittente </td>
    <td class="l">
      <input Title="Sede Mittente" name="<%=ICostantiUlterioreIstanza.CAMPO_SEDE_MITTENTE%>"
      value="<%=StringUtils.toStringJSP(ulterioreIstanza.getSedeMittente())%>" type="text" maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciUlterioreIstanza','<%=ICostantiUlterioreIstanza.CAMPO_SEDE_MITTENTE %>');">
      <img src="/images/filefolder.gif" border=0> </a>
    </td>
  </tr>
  <tr>
    <td class="l">Contenuto <font class="ob">(*)</font></td>
    <td class="L">
      <select title="contenuto" class=small name="<%=ICostantiUlterioreIstanza.CAMPO_COD_CONTENUTO%>">
        <%= contenuto %>
      </select>
  </tr>
  <tr>
    <td class="l">Oggetto </td>
    <td class="l">
    <Textarea Title="Oggetto" name="<%=ICostantiUlterioreIstanza.CAMPO_DESCR_OGGETTO_PROCEDIMENTO %>" cols="88" rows="3" readonly>
		<%
		String lCodOggetto=""; 
		String lCodDettagli=""; 
    
		if( modalita.equals("M") )
    {
    	String strDescOggetto= new String();
    	
    	if( ulterioreIstanza.getUltIstTenori().length > 0 )
    	{
    	  strDescOggetto = ulterioreIstanza.getUltIstTenori()[0].getDescrOggettoTenore()+"\n";
        lCodOggetto = ulterioreIstanza.getUltIstTenori()[0].getCodOggettoTenore()+ "|";

        int lSize = ulterioreIstanza.getUltIstTenori().length;
        for( int x=1; x<lSize; x++ )
        {
          lCodOggetto += ulterioreIstanza.getUltIstTenori()[x].getCodOggettoTenore()+"|";
          strDescOggetto += ulterioreIstanza.getUltIstTenori()[x].getDescrOggettoTenore()+"\n";
          // Gestione dettaglio Oggetto.
          if (ulterioreIstanza.getUltIstTenori()[x].getCodDettaglioOggetto() != null && 
              ulterioreIstanza.getUltIstTenori()[x].getCodDettaglioOggetto().length()>1 )
            lCodDettagli += ulterioreIstanza.getUltIstTenori()[x].getCodOggettoTenore() + ulterioreIstanza.getUltIstTenori()[x].getCodDettaglioOggetto() + "|";
        }		        
    	}
    	
      if (!(strDescOggetto.indexOf("\n")>0))
      { 
        if (strDescOggetto.length()>0)
        {
         %>
        	<%=strDescOggetto%>
         <%
        }
        else
        {
            %>-&nbsp;<%
        }
      }
      else
      {
      	while (strDescOggetto.indexOf("\n")>0)
        {
        %>
        	<%=strDescOggetto.substring(0, strDescOggetto.indexOf("\n")+1)%>
        <%
        	strDescOggetto=strDescOggetto.substring(strDescOggetto.indexOf("\n")+1);
        } // End while
        %>
        <%=strDescOggetto%>
    <%
      }
     }
     else
     {
     %>
     <%=StringUtils.toStringJSP(ulterioreIstanza.getDescrOggettoProcedimento()) %>
   <%}%>
   </Textarea>
      
      <a href="Javascript:ListaOggetti('LoadInserisciUlterioreIstanza',
      									document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_COD_CONTENUTO%>[document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_COD_CONTENUTO%>.selectedIndex].value, '<%= ICostantiUlterioreIstanza.CAMPO_DESCR_OGGETTO_PROCEDIMENTO %>', '<%=ICostantiUlterioreIstanza.CAMPO_COD_OGGETTO_PROCEDIMENTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', 
      									document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_COD_OGGETTO_PROCEDIMENTO%>.value, 
      									document.LoadInserisciUlterioreIstanza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      									
      <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
      &nbsp;
      <a href="Javascript:ListaOggetti('LoadInserisciUlterioreIstanza',
      								   '-', 
      								   '<%=ICostantiUlterioreIstanza.CAMPO_DESCR_OGGETTO_PROCEDIMENTO %>', 
      								   '<%=ICostantiUlterioreIstanza.CAMPO_COD_OGGETTO_PROCEDIMENTO%>', 
      								   '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', 
      								   document.LoadInserisciUlterioreIstanza.<%=ICostantiUlterioreIstanza.CAMPO_COD_OGGETTO_PROCEDIMENTO%>.value, 
      								   document.LoadInserisciUlterioreIstanza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      								   
      <img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0></a>
    </td>
  </tr>
  <tr>
    <td class="l">Data arrivo in cancelleria <font class=ob>(*)</font></td>
    <td class="L">
<%
    if( modalita.equals("M") )
    {      
%>
      <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(ulterioreIstanza.getDataArrivoCancelleria(),"dd")) %>" type="text" name="<%=ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
      /
      <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(ulterioreIstanza.getDataArrivoCancelleria(),"MM")) %>" type="text" name="<%= ICostantiUlterioreIstanza.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
      /
      <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(ulterioreIstanza.getDataArrivoCancelleria(),"yyyy")) %>" type="text" name="<%= ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%  }
    else
    {
%>
      <input type="text" name="<%=ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
      /
      <input type="text" name="<%=ICostantiUlterioreIstanza.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
      /
      <input type="text" name="<%=ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
<%  
		}
%>
    </td>
  </tr>

  <tr>
    <td class="l">Note</td>
    <td class="l">
      <Textarea Title="Note" name="<%= ICostantiUlterioreIstanza.CAMPO_NOTE %>" cols="80" rows="5"><%=StringUtils.toStringJSP(ulterioreIstanza.getNote()) %></textarea>
    </td>
  </tr>

  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_SEDE_MITTENTE%>" >
  <input type="HIDDEN" name="<%=ICostantiUlterioreIstanza.CAMPO_COD_OGGETTO_PROCEDIMENTO%>" value="<%=lCodOggetto%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=lCodDettagli%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_ALTRA_CAUSA%>" value="<%=idAltraCausa%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_FASCICOLO_SIUS_ORIGINE%>" value="<%=idFascicoloOrigine%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_REGISTRO%>">
  <input type="HIDDEN" name="<%=ICostantiUlterioreIstanza.CAMPO_ID_ULTERIORE_ISTANZA%>" value="<%=ulterioreIstanza.getIdUlterioreIstanza()%>">

</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciUlterioreIstanza");

    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_RICHIESTA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_MESE_DATA_RICHIESTA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_RICHIESTA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_RICHIESTA%>","minlen=4","La lunghezza del campo Anno Data Atto deve essere di 4 caratteri");

    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","req", "Il campo Giorno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","req", "Il campo Mese Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","req", "Il campo Anno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUlterioreIstanza.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","minlen=4","La lunghezza del campo Anno Data Arrivo in cancelleria deve essere di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>