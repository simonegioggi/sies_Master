<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>

<jsp:useBean id="UtenteConnesso"  		scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="modalita"        		scope="request" class="java.lang.String"/>
<jsp:useBean id="provenienza"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIGE" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSigeEsteso" 	scope="request" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="fascicoloPadre" 	    scope="request" class="siap.sige.fascicolo.model.FascicoloSigeModel" />
<jsp:useBean id="soggFascPadre" 	    scope="request" class="siap.sico.soggetto.model.SoggettoModel" />

<%
	String lOperazione="";
	String lChiaveAnno="";
	String lChiaveProgr="";

	String aCognome="";
	String aNome="";
	String aComuneNas="";
	String aDataNas="";

	String aCognomePadre="";
	String aNomePadre="";
	String aComuneNasPadre="";
	String aDataNasPadre="";

  if( modalita.equals("M")  )
  {
  	lOperazione="Modifica";
  	lChiaveAnno=fascicoloPadre.getChiaveAnno().toString();
  	lChiaveProgr=fascicoloPadre.getChiaveProgr().toString();
  }
  	else lOperazione="Inserimento";

  aCognome = fascicoloSigeEsteso.getSoggetto().getCognome();
  aNome = fascicoloSigeEsteso.getSoggetto().getNome();
  aComuneNas = fascicoloSigeEsteso.getSoggetto().getDescrComuneNascita();
  aDataNas = fascicoloSigeEsteso.getSoggetto().getDataNascita().toString();
  if(fascicoloPadre != null && fascicoloPadre.getIdFascicoloSige() != null && soggFascPadre != null && soggFascPadre.getIdSoggetto() !=null)
  {
	aCognomePadre = soggFascPadre.getCognome();
  	aNomePadre = soggFascPadre.getNome();
  	aComuneNasPadre = soggFascPadre.getDescrComuneNascita();
  	aDataNasPadre = soggFascPadre.getDataNascita().toString();
  }

%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - <%=lOperazione%> Collegamento</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>

    <script language="JavaScript">
      function VerifyInit()
      {
			// Al primo caricamento della form di Modifica, i controlli sul soggetto non sono effettuati,
			// il pulsante di conferma è disabilitato e non si esegue l'azione di verifica.
        	document.LoadModificaCollegamento.CONFERMA.disabled=true;
			var aProvenienza = "<%=provenienza.charAt(0)%>";
			//alert("Provenienza : "+aProvenienza);
			if (aProvenienza == 'R') // dalla Ricerca
				VerifyControlli(aProvenienza);
			if (aProvenienza == 'F') // Fallita ricerca
        	{
				var aMessaggio = "<%=provenienza.substring(1)%>";
				alert(aMessaggio);
        	}
      }

      function VerifyControlli(aProvenienza)
      {
        // Controllo Anno/Numero Fascicolo SIGE.
        if ( document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>.value.length<4 || document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO %>.value<1900 || isNaN(document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>.value) )
        {
          alert ("Anno Fascicolo SIGE Non Valido");
          document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>.focus();
          return false;
        }
        if ( document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>.value.length<=0 || document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR %>.value<0 || isNaN(document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>.value) )
        {
          alert ("Numero Fascicolo SIGE Non Valido");
          document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>.focus();
          return false;
        }
        // Controllo obbligatorietà campi relativi all'autorità.
        if (document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_COD_MITTENTE_ATTO%>.value=="-")
        {
          alert("Il tipo autorità è un campo obbligatorio");
          document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_COD_MITTENTE_ATTO%>.focus();
          return false;
        }
        if (document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_DESCR_SEDE_MITTENTE%>.value=="")
        {
          alert("La sede dell'autorità è un campo obbligatorio");
          document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_DESCR_SEDE_MITTENTE%>.focus();
          return false;
        }

		if (aProvenienza == 'D') // dal Dettaglio
        {
        	document.LoadModificaCollegamento.CONFERMA.disabled=true;
          	return true;
        }
        else
        {
        	if (aProvenienza == 'R') // dalla Ricerca
        	{
        		// Controllo soggetti.
        		var soggettoOri = "<%=aCognome%>"+"<%=aNome%>"+"<%=aComuneNas%>"+"<%=aDataNas%>";
	  			var soggettoPadre = "<%=aCognomePadre%>"+"<%=aNomePadre%>"+"<%=aComuneNasPadre%>"+"<%=aDataNasPadre%>";
	  			if (soggettoOri != soggettoPadre)
	  			{
          			alert("Attenzione! Soggetto del Procedimento da collegare differente dal soggetto del procedimento corrente.");
        			document.LoadModificaCollegamento.CONFERMA.disabled=false;
	      			return false;
	  			}else{
          			alert("Verifica positiva: fascicolo da collegare conforme.");
        			document.LoadModificaCollegamento.CONFERMA.disabled=false;
        			return false;
        		}
        	}
      	}
      }

      function VerifyConferma()
      {
        // Controllo Anno/Numero Fascicolo SIUS.
        if ( document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>.value.length<4 || document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO %>.value<1900 || isNaN(document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>.value) )
        {
          alert ("Anno Fascicolo SIUS Non Valido");
          document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>.focus();
          return false;
        }
        if ( document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>.value.length<=0 || document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR %>.value<0 || isNaN(document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>.value) )
        {
          alert ("Numero Fascicolo SIUS Non Valido");
          document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>.focus();
          return false;
        }
        // Controllo obbligatorietà campi relativi all'autorità.
        if (document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_COD_MITTENTE_ATTO%>.value=="-")
        {
          alert("Il tipo autorità è un campo obbligatorio");
          document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_COD_MITTENTE_ATTO%>.focus();
          return false;
        }
        if (document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_DESCR_SEDE_MITTENTE%>.value=="")
        {
          alert("La sede dell'autorità è un campo obbligatorio");
          document.LoadModificaCollegamento.<%=ICostantiFascicoloSige.CAMPO_DESCR_SEDE_MITTENTE%>.focus();
          return false;
        }
        // Controllo soggetti.
        var soggettoOri = "<%=aCognome%>"+"<%=aNome%>"+"<%=aComuneNas%>"+"<%=aDataNas%>";
        var soggettoPadre = "<%=aCognomePadre%>"+"<%=aNomePadre%>"+"<%=aComuneNasPadre%>"+"<%=aDataNasPadre%>";
        if (soggettoOri != soggettoPadre)
        {
			if (confirm("Attenzione! I Soggetti dei Procedimenti da collegare sono differenti. Vuoi continuare? ")== true)
            {
          		document.LoadModificaCollegamento.CONFERMA.disabled=false;
          		return true;
          	}else{
          		document.LoadModificaCollegamento.CONFERMA.disabled=true;
          		return false;
          }
        }
      }

      function Ricerca()
      {
      	if (VerifyControlli("D"))
        {
        	// Impostazione action di ricerca.
        	document.LoadModificaCollegamento.<%=IWebConstants.ACTION_FIELD%>.value="siap.sige.fascicolo.action.ActRicercaCollegamento"
      		return true;
        } else {
      		return false;
        }
      }

      function Modifica()
      {
        // Impostazione action di Inserimento/Modifica.
        document.LoadModificaCollegamento.<%=IWebConstants.ACTION_FIELD%>.value="siap.sige.fascicolo.action.ActModificaCollegamento";
        VerifyConferma();
        return true;
  	  }
  </script>

  </head>

  <body class="corpo" onload="javascript: VerifyInit()">

  <FORM method="POST" name="LoadModificaCollegamento" action="<%=IWebConstants.PG_MAIN%>">
    <table>
      <tr>
      	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class=label>Funzione :</font> <font class=campo> <%=lOperazione%> Estremi Procedimento Collegato</font> </td>
      	<!-- BOTTONE DI RITORNO AL DETTAGLIO FASCICOLO -->
      	<td class="LBG">
        	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&IdFascicoloSige=<%=fascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>" >
          	<img  align="middle" src="/images/arrowleft24.gif" alt="Ritorna al Dettaglio Fascicolo" width="24" height="24" border="0">
        	</a>
      	</td>
      </tr>
    </table>
    <br>
     <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    <br>
<%
    String lAction = "siap.sige.fascicolo.action.ActModificaCollegamento";
%>
    <table>
      <tr>
        <td colspan='2'><font class="campo"> Da Collegare al fascicolo :</font></td>
      </tr>
      <tr>
        <td class="l">Anno/Numero SIGE</td>

        <td class="l">
	        <input Title="Anno SIGE" value="<%=lChiaveAnno%>" name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>"type="text" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
  	      <input Title="Numero SIGE" value="<%=lChiaveProgr%>" name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>"type="text" maxlength="14" size="14" onkeypress="return TicTabNumField(this,event)">
				</td>
      </tr>
      <tr>
        <td class="l">Autorità</td>
    		<td class="L">
      		<select title="Autorità" class=small name="<%=ICostantiFascicoloSige.CAMPO_COD_MITTENTE_ATTO%>">
        	<%= tipoUfficioSIGE %>
      </select>
      </tr>
      <tr>
        <td class="l">Sede</td>
        <td class="l">
      		<input Title="Sede Autorità" name="<%=ICostantiFascicoloSige.CAMPO_DESCR_SEDE_MITTENTE%>" value="<%=StringUtils.toStringJSP(fascicoloPadre.getDescrUfficio())%>" type="text" maxlength="35" size="35">
      					<a href="Javascript:ListaComuni('LoadModificaCollegamento','<%=ICostantiFascicoloSige.CAMPO_DESCR_SEDE_MITTENTE%>');">
      						<img src="/images/filefolder.gif" border=0> </a>
        <td class="l">
        <td>
          <input class="bottone" type="submit" name="RICERCA" value="Verifica" title="Verifica Fascicolo da Collegare" onClick="javascript:return Ricerca();">
        </td>
      </tr>
    </table>

    <table cellspacing=2 cellpadding=2>
      <tr>
      </tr>
      <tr>
        <td>
          <input class="bottone" type="submit"  name="CONFERMA" value="Conferma" onClick="javascript:return Modifica();">
        </td>
      </tr>
    </table>

  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
	  <input type="HIDDEN" name="IdFascicoloPadre" value="<%=fascicoloPadre.getIdFascicoloSige()%>" >
		</form>
  </body>
</html>