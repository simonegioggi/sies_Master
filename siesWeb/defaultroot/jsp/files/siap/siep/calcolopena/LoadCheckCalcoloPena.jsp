<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloCheckModel" %>
<%@ page import="java.math.BigDecimal" %>

<jsp:useBean id="ListaFascicoli"     scope="request" class="java.util.Vector" />
<jsp:useBean id="ListaFascicoliSIEP" scope="request" class="java.util.Vector" />
<jsp:useBean id="ListaFascicoliRES"  scope="request" class="java.util.Vector" />


<html>
<head>
  <title> [S.I.E.S.] - Calcolo Pena - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  //============================================================================
  // Verifica che almeno un elemento sia selezionato per la ricerca
  //============================================================================
  function Verify()
  {
    //alert(" verify");
    //alert("document.all = "+document.all);
    return true;
  }
  
  function selectAll()
  {
    //document
  }
  </script>
</head>

<body class="corpo">
<FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Verifica Calcolo Pena</font>
      </td>
    </tr>
  </table>

  <br>


  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">
        Elenco dei fascicoli su cui attivare la funzione di controllo
      </td>
    </tr>
  </table>

  <br>

  <table>
    <tr>
      <td class="l">&nbsp;</td>
      <td class="l" style="text-align:center">TOT<br><input type="radio" name="TipoRicerca" value="Tutti" checked></td>
      <td class="l" style="text-align:center">Migrati<br><input type="radio" name="TipoRicerca" value="Migrati"></td>
      <td class="l" style="text-align:center">Iscritti SIEP<br><input type="radio" name="TipoRicerca" value="SIEP"></td>
    </tr>
    
    <% 
    for (int i=0; i<ListaFascicoli.size(); i++) 
    { 
      FascicoloCheckModel lCheckModel = (FascicoloCheckModel) ListaFascicoli.elementAt(i);
      BigDecimal lAnno = lCheckModel.getChiaveAnno();
      
      BigDecimal lMigratiRES = new BigDecimal(0);
      for (int j=0; j<ListaFascicoliRES.size(); j++ )
      {
        FascicoloCheckModel lCheckModelRES = (FascicoloCheckModel) ListaFascicoliRES.elementAt(j);
        if (lAnno.compareTo(lCheckModelRES.getChiaveAnno())==0)
        {
          lMigratiRES = lCheckModelRES.getNumFascicoli();
          break;
        }
      }
      
      BigDecimal lIscrittiSIEP = new BigDecimal(0);
      for (int j=0; j<ListaFascicoliSIEP.size(); j++ )
      {
        FascicoloCheckModel lCheckModelSIEP = (FascicoloCheckModel) ListaFascicoliSIEP.elementAt(j);
        if (lAnno.compareTo(lCheckModelSIEP.getChiaveAnno())==0)
        {
          lIscrittiSIEP = lCheckModelSIEP.getNumFascicoli();
          break;
        }
      }
    %>
    <tr>
      <td class="l"><input type="checkbox" name="<%=lAnno%>" value="<%=lAnno%>"><font class="label">Anno: <%=lAnno%> </font></td>
      <td class="l"><font class="label"><%=lCheckModel.getNumFascicoli()%></font></td>
      <td class="l"><font class="label"><%=lMigratiRES%></font></td>
      <td class="l"><font class="label"><%=lIscrittiSIEP%></font></td>
    </tr>
    <%
    }
    %>
  </table>
  
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActCheckCalcoloPenaF5">
  <table>
    <tr>
      <td class="l"colspan=4>
        <INPUT class="bottone" type="submit" name="conferma" value="Avvia Controllo">
      </td>
    </tr>
  </table>
  

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>