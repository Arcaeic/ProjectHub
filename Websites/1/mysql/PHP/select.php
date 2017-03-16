<html>
<head>
<link rel="stylesheet" href="CSS/ControlPanel.css"/>
<title>Select Form</title>
</head>
</html>

<?php
$con = mysql_connect("localhost","root","password");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("my_db", $con);

$result = mysql_query("SELECT * FROM Persons
WHERE FirstName='$_POST[firstname]'");

while($row = mysql_fetch_array($result))
  {
  echo "<table border='1'>
<tr>
<th>Firstname</th>
<th>Lastname</th>
<th>Age</th>
</tr>";
  echo "<td>" . $row['FirstName'] . "</td>";
  echo "<td>" . $row['LastName'] . "</td>";
  echo "<td>" . $row['Age'] . "</td>";
  echo "</table>";
  echo "<br />";
  }
?> 

</br>

<a href="index.php" title="Index" <h2>Back</h2></a>

 <script language="javascript" type="text/javascript">
     <!--
     window.setTimeout('window.location="http://localhost/mysql/ControlPanel.php"; ',5000);
     // -->
 </script>
