<?php
$con = mysql_connect("localhost","root","password");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("my_db", $con);

$sql="INSERT INTO Persons (firstname, lastname, age)
VALUES
('$_POST[firstname]','$_POST[lastname]','$_POST[age]')";

if (!mysql_query($sql,$con))
  {
  die('Error: ' . mysql_error());
  }

mysql_close($con);
?>

 <script language="javascript" type="text/javascript">
     <!--
     window.setTimeout('window.location="http://localhost/mysql/ControlPanel.php"; ',1);
     // -->
 </script>




