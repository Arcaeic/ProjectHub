<?php
$con = mysql_connect("localhost","root","password");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("GameGrumps", $con);

mysql_query("DELETE FROM Characters WHERE Id='$_POST[Id]'");

mysql_close($con);
?> 

<script language="javascript" type="text/javascript">
     <!--
     window.setTimeout('window.location="http://localhost/mysql2/ControlPanel.php"; ',01);
     // -->
 </script>

