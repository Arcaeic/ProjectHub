<?php
$con = mysql_connect("localhost","root","password");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("GameGrumps", $con);

mysql_query("UPDATE Characters SET Winner='$_POST[Winner]'
WHERE Name='$_POST[Name]'");

mysql_close($con);
?>
 <script language="javascript" type="text/javascript">
     <!--
     window.setTimeout('window.location="http://localhost/mysql2/ControlPanel.php"; ',01);
     // -->
 </script>